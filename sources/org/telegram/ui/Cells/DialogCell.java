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
import android.text.style.CharacterStyle;
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
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
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
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$DraftMessage;
import org.telegram.tgnet.TLRPC$EmojiStatus;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$ForumTopic;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$MessageFwdHeader;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$MessagePeerReaction;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_dialogFolder;
import org.telegram.tgnet.TLRPC$TL_documentEmpty;
import org.telegram.tgnet.TLRPC$TL_emojiStatus;
import org.telegram.tgnet.TLRPC$TL_emojiStatusUntil;
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
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC$TL_messageReactions;
import org.telegram.tgnet.TLRPC$TL_messageService;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_photoEmpty;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DialogsAdapter;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.CanvasButton;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EmptyStubSpan;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
import org.telegram.ui.Components.Forum.ForumBubbleDrawable;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.Premium.PremiumLockIconView;
import org.telegram.ui.Components.PullForegroundDrawable;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.StaticLayoutEx;
import org.telegram.ui.Components.StatusDrawable;
import org.telegram.ui.Components.TimerDrawable;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.URLSpanNoUnderlineBold;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.RightSlidingDialogContainer;
/* loaded from: classes3.dex */
public class DialogCell extends BaseCell {
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
    private AvatarDrawable avatarDrawable;
    public ImageReceiver avatarImage;
    private int bottomClip;
    private Paint buttonBackgroundPaint;
    private boolean buttonCreated;
    private StaticLayout buttonLayout;
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
    public boolean drawAvatar;
    private boolean drawCheck1;
    private boolean drawCheck2;
    private boolean drawClock;
    private boolean drawCount;
    private boolean drawCount2;
    private boolean drawError;
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
    protected String overrideSwipeActionBackgroundColorKey;
    protected RLottieDrawable overrideSwipeActionDrawable;
    protected String overrideSwipeActionRevealBackgroundColorKey;
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
    public boolean swipeCanceled;
    private int swipeMessageTextId;
    private StaticLayout swipeMessageTextLayout;
    private int swipeMessageWidth;
    private Paint thumbBackgroundPaint;
    private ImageReceiver[] thumbImage;
    int thumbSize;
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
    public boolean useForceThreeLines;
    public boolean useFromUserAsAvatar;
    private boolean useMeForMyMessages;
    public boolean useSeparator;
    private TLRPC$User user;
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
        this.drawAvatar = true;
        this.messagePaddingStart = 72;
        this.heightDefault = 72;
        this.heightThreeLines = 78;
        this.chekBoxPaddingTop = 42.0f;
        this.collapseOffset = 0.0f;
        this.hasUnmutedTopics = false;
        this.overrideSwipeAction = false;
        this.thumbImage = new ImageReceiver[3];
        this.drawPlay = new boolean[3];
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
        this.resourcesProvider = resourcesProvider;
        this.parentFragment = dialogsActivity;
        Theme.createDialogsResources(context);
        this.avatarImage.setRoundRadius(AndroidUtilities.dp(28.0f));
        int i2 = 0;
        while (true) {
            ImageReceiver[] imageReceiverArr = this.thumbImage;
            if (i2 < imageReceiverArr.length) {
                imageReceiverArr[i2] = new ImageReceiver(this);
                this.thumbImage[i2].setRoundRadius(AndroidUtilities.dp(2.0f));
                i2++;
            } else {
                this.useForceThreeLines = z2;
                this.currentAccount = i;
                AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this, AndroidUtilities.dp(22.0f));
                this.emojiStatus = swapAnimatedEmojiDrawable;
                swapAnimatedEmojiDrawable.center = false;
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
        boolean z = this.ttlPeriod > 0 && !this.hasCall && !isOnline();
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
        if (!(tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatTheme) || !this.lastUnreadState) {
            return;
        }
        ChatThemeController.getInstance(this.currentAccount).setDialogTheme(this.currentDialogId, ((TLRPC$TL_messageActionSetChatTheme) tLRPC$MessageAction).emoticon, false);
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
        this.reorderIconProgress = (!getIsPinned() || !this.drawReorder) ? 0.0f : 1.0f;
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
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.avatarImage.onAttachedToWindow();
        int i = 0;
        while (true) {
            ImageReceiver[] imageReceiverArr = this.thumbImage;
            if (i < imageReceiverArr.length) {
                imageReceiverArr[i].onAttachedToWindow();
                i++;
            } else {
                resetPinnedArchiveState();
                this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack2, this.messageNameLayout);
                this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack3, this.buttonLayout);
                this.animatedEmojiStackName = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStackName, this.nameLayout);
                return;
            }
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
        if (!getIsPinned() || !this.drawReorder) {
            f = 0.0f;
        }
        this.reorderIconProgress = f;
        this.cornerProgress = 0.0f;
        setTranslationX(0.0f);
        setTranslationY(0.0f);
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatus;
        if (swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.attach();
        }
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
            if (!this.nameIsEllipsized) {
                return;
            }
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
        if (measuredHeight == this.lastSize) {
            return;
        }
        this.lastSize = measuredHeight;
        try {
            buildLayout();
        } catch (Exception e) {
            FileLog.e(e);
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
        ArrayList<TLRPC$Dialog> dialogs = MessagesController.getInstance(this.currentAccount).getDialogs(this.currentDialogFolderId);
        this.currentDialogFolderDialogsCount = dialogs.size();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        int size = dialogs.size();
        for (int i = 0; i < size; i++) {
            TLRPC$Dialog tLRPC$Dialog = dialogs.get(i);
            TLRPC$Chat tLRPC$Chat = null;
            if (DialogObject.isEncryptedDialog(tLRPC$Dialog.id)) {
                TLRPC$EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(tLRPC$Dialog.id)));
                tLRPC$User = encryptedChat != null ? MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(encryptedChat.user_id)) : null;
            } else if (DialogObject.isUserDialog(tLRPC$Dialog.id)) {
                tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$Dialog.id));
            } else {
                tLRPC$Chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-tLRPC$Dialog.id));
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
                spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM), 0, Theme.getColor("chats_nameArchived", this.resourcesProvider)), length, length2, 33);
            }
            if (spannableStringBuilder.length() > 150) {
                break;
            }
        }
        return Emoji.replaceEmoji(spannableStringBuilder, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(68:10|(1:1375)(1:14)|15|(1:1374)(1:21)|22|(1:1373)(1:26)|27|(1:29)|30|(2:32|(1:1362)(1:36))(2:1363|(1:1372)(1:1367))|37|(1:39)(1:1357)|40|(7:42|(1:44)|45|46|(1:48)|49|50)|51|(9:53|(2:55|(2:661|(1:663)(1:664))(2:59|(1:61)(1:660)))(4:665|(1:682)(1:669)|670|(2:678|(1:680)(1:681))(2:674|(1:676)(1:677)))|62|(3:64|(1:66)(4:647|(1:649)|650|(1:655)(1:654))|67)(3:656|(1:658)|659)|68|(1:70)(1:646)|71|(1:73)(1:(1:642)(1:(1:644)(1:645)))|74)(22:683|(2:1353|(1:1355)(1:1356))(2:687|(1:689)(1:1352))|690|(2:692|(2:694|(2:702|(1:704)(1:705))(2:698|(1:700)(1:701))))(2:1301|(2:1303|(2:1305|(1:1307)(2:1308|(1:1310)(3:1311|(1:1317)(1:1315)|1316)))(2:1318|(7:1320|(1:1322)(2:1342|(1:1344)(3:1345|(1:1351)(1:1349)|1350))|1323|(2:1325|(3:1329|1330|(2:1332|(2:1337|(1:1339)(1:1340))(1:1336))))|1341|1330|(0)))))|706|(1:710)|711|(2:713|(1:717))(2:1297|(1:1299)(1:1300))|718|(6:1275|(2:1277|(2:1279|(2:1281|(1:1283))))|1285|(2:1287|(1:1289))|1291|(13:1293|(1:1295)|725|(7:727|(1:729)(1:896)|730|(1:732)(1:895)|733|(2:735|(1:738))|739)(3:(6:898|(1:900)(1:1269)|901|(1:903)(1:1268)|(1:905)(1:1267)|906)(1:1270)|907|(4:909|(2:911|(2:918|917)(1:915))(7:919|(1:921)|922|(3:926|(1:928)(1:930)|929)|931|(1:935)|936)|916|917)(5:937|(1:939)(2:943|(5:945|(2:947|(1:949)(2:950|(1:952)(2:953|(1:955)(2:956|(2:958|(1:960)(1:961))))))(2:963|(2:967|942))|962|941|942)(14:968|(1:970)(1:1266)|971|(2:985|(11:987|(10:989|(10:991|(1:993)(3:1258|(1:1260)(1:1262)|1261)|994|(1:996)(7:1004|(3:1006|(4:1008|(2:1010|(2:1012|(1:1014)(2:1017|(1:1019)(1:1020))))|1021|(1:1023)(2:1024|(1:1026)(2:1027|(1:1029)(1:1030))))(1:1031)|1015)(2:1032|(9:1037|(2:1046|(2:1059|(4:1123|(2:1125|(5:1127|(1:1139)|1133|(1:1137)|1138)(2:1140|(2:1147|(2:1154|(4:1156|(1:1158)(2:1180|(1:1182)(2:1183|(1:1185)(2:1186|(1:1188)(2:1189|(1:1191)(1:1192)))))|1159|(3:1172|(3:1174|(1:1176)(1:1178)|1177)|1179)(4:1163|(2:1165|(1:1167)(1:1168))|(1:1170)|1171))(2:1193|(3:1195|(3:1197|(1:1199)(1:1202)|1200)(3:1203|(1:1205)(1:1207)|1206)|1201)(4:1208|(1:1210)(2:1216|(1:1218)(2:1219|(1:1221)(2:1222|(1:1224)(3:1225|(2:1231|(2:1233|(3:1235|(1:1237)(1:1239)|1238)))(1:1229)|1230))))|1211|(2:1213|(1:1215)))))(1:1153))(1:1146)))|1240|(6:1242|(3:1254|(1:1256)|1257)(1:1246)|1247|(1:1249)|1250|(1:1252)))(14:1070|(2:1076|(13:1078|(1:1121)(1:1082)|1083|1084|(1:1120)(5:1090|1091|1092|1093|1094)|1095|(1:1099)|1100|(4:1102|(1:1104)|1105|(1:1107)(1:1108))|1109|1000|(1:1002)|1003))|1122|1084|(2:1086|1116)|1120|1095|(2:1097|1099)|1100|(0)|1109|1000|(0)|1003))(8:1052|(1:1058)(1:1056)|1057|998|999|1000|(0)|1003))(1:1043)|1044|1045|998|999|1000|(0)|1003)(1:1036))|1016|999|1000|(0)|1003)|997|998|999|1000|(0)|1003)|1263|(0)(0)|997|998|999|1000|(0)|1003)|1264|1263|(0)(0)|997|998|999|1000|(0)|1003))|1265|1264|1263|(0)(0)|997|998|999|1000|(0)|1003))|940|941|942))|740|(1:742)(2:888|(1:890)(2:891|(1:893)(1:894)))|743|(1:745)(5:805|(4:807|(1:(1:810)(2:861|812))(1:862)|811|812)(7:863|(1:865)(6:875|(2:884|(1:886)(1:887))(1:883)|867|(1:869)(1:874)|870|(1:872)(1:873))|866|867|(0)(0)|870|(0)(0))|813|(2:818|(2:820|(1:822)(2:823|(1:825)(2:826|(3:828|(3:830|(1:832)(1:835)|833)(2:836|(3:838|(1:850)(1:842)|843)(3:851|(1:859)(1:857)|858))|834)))))|860)|746|(2:750|(1:752)(2:753|(4:755|(1:757)|758|(1:760))))|761|(1:763)(2:765|(4:767|(3:769|(1:771)|772)(2:780|(4:782|(1:784)|785|(1:787)(1:788))(1:789))|(1:777)|778)(5:790|(4:792|(1:794)(2:795|(2:797|(1:799)(3:800|(1:802)|803))(1:804))|(2:775|777)|778)|779|(0)|778))|764))|724|725|(0)(0)|740|(0)(0)|743|(0)(0)|746|(3:748|750|(0)(0))|761|(0)(0)|764)|(2:76|(1:78)(1:639))(1:640)|79|(3:81|(1:83)(1:637)|84)(1:638)|85|(1:87)(1:636)|88|(1:90)|91|(2:93|(1:95)(1:623))(2:624|(2:626|(2:628|(1:630)(1:631))(2:632|(1:634)(1:635))))|96|(2:593|(2:620|(1:622))(2:597|(2:599|(1:601))(2:602|(2:604|(1:606))(2:607|(4:609|(1:611)(1:615)|612|(1:614))))))(2:100|(1:102))|103|(18:104|105|(1:107)|108|(1:110)|111|(3:113|(1:115)(1:117)|116)|118|(1:120)(1:590)|121|(1:123)|124|(1:589)(1:130)|131|(1:133)(1:588)|134|(1:587)(1:138)|139)|140|(4:571|(1:573)(1:585)|574|(2:575|(3:577|(2:579|580)(2:582|583)|581)(1:584)))(8:144|(1:146)(1:570)|147|(1:149)(1:569)|150|(1:152)(1:568)|153|(2:154|(3:156|(2:158|159)(2:161|162)|160)(1:163)))|164|(1:166)|167|(2:169|(1:171)(1:172))|173|(2:175|(1:177)(1:498))(1:(4:(3:510|(1:512)(1:566)|513)(1:567)|(5:515|(1:517)(1:564)|518|(3:520|(1:522)(1:558)|523)(3:559|(1:561)(1:563)|562)|524)(1:565)|525|(2:527|(4:529|(3:531|(1:533)(1:535)|534)|536|(3:538|(1:540)(1:542)|541))(5:543|(3:545|(1:547)(1:549)|548)|550|(3:552|(1:554)(1:556)|555)|557)))(3:503|(2:505|(1:507))|508))|(7:(1:180)|181|(1:183)|184|(1:195)(1:188)|189|(1:193))|196|(1:497)(1:200)|201|(4:203|(1:448)(1:207)|208|(2:209|(1:211)(1:212)))(2:449|(8:474|475|(2:477|(2:479|(1:481)))|482|483|(1:493)(1:487)|488|(2:489|(1:491)(1:492)))(2:453|(4:458|(1:468)(1:462)|463|(2:464|(1:466)(1:467)))(1:457)))|213|(1:215)|216|217|218|(1:220)(1:446)|221|222|(6:223|224|(1:442)(1:(2:231|(1:436)(1:237)))|238|(3:240|(3:242|(2:251|252)|249)|253)|254)|(14:259|(2:261|(1:263))|264|265|266|267|(12:269|(1:274)|275|276|277|278|279|(8:281|(6:285|(1:287)|288|(1:314)(2:292|(1:294)(2:300|(1:302)(2:303|(3:305|(1:307)(1:309)|308))))|295|(2:297|(1:299)))|315|(4:319|(1:(1:329)(2:321|(1:323)(2:324|325)))|326|(1:328))|330|(4:334|(1:(1:344)(2:336|(1:338)(2:339|340)))|341|(1:343))|345|(2:351|(1:353)))(8:371|(6:375|(1:377)|378|(4:380|(1:382)|383|(1:385))|386|(1:396))|397|(4:401|(1:403)|404|405)|406|(4:410|(1:412)|413|414)|415|(1:419))|354|(3:(1:368)(1:363)|364|(1:366)(1:367))|369|370)|423|279|(0)(0)|354|(6:356|358|(1:361)|368|364|(0)(0))|369|370)|427|(1:430)|431|(1:433)(1:435)|434|265|266|267|(0)|423|279|(0)(0)|354|(0)|369|370) */
    /* JADX WARN: Code restructure failed: missing block: B:1253:0x0e50, code lost:
        if (r2 != null) goto L1045;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1284:0x0597, code lost:
        if (r2.post_messages == false) goto L724;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1290:0x05a3, code lost:
        if (r2.kicked != false) goto L724;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1296:0x05b1, code lost:
        if (r48.isTopic == false) goto L724;
     */
    /* JADX WARN: Code restructure failed: missing block: B:424:0x1b85, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x1b86, code lost:
        r4 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:773:0x107a, code lost:
        if (r4 == null) goto L779;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1002:0x0e58  */
    /* JADX WARN: Removed duplicated region for block: B:1004:0x08f5  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x1333  */
    /* JADX WARN: Removed duplicated region for block: B:1097:0x0abf  */
    /* JADX WARN: Removed duplicated region for block: B:1102:0x0ad0  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x1338 A[Catch: Exception -> 0x1422, TryCatch #6 {Exception -> 0x1422, blocks: (B:105:0x132a, B:108:0x1334, B:110:0x1338, B:111:0x1342, B:113:0x1346, B:116:0x1360, B:118:0x1369, B:121:0x137f, B:123:0x1385, B:124:0x1391, B:126:0x13a8, B:128:0x13ae, B:131:0x13bf, B:133:0x13c3, B:134:0x13ff, B:136:0x1403, B:138:0x140c, B:139:0x1416, B:588:0x13e2), top: B:104:0x132a }] */
    /* JADX WARN: Removed duplicated region for block: B:113:0x1346 A[Catch: Exception -> 0x1422, TryCatch #6 {Exception -> 0x1422, blocks: (B:105:0x132a, B:108:0x1334, B:110:0x1338, B:111:0x1342, B:113:0x1346, B:116:0x1360, B:118:0x1369, B:121:0x137f, B:123:0x1385, B:124:0x1391, B:126:0x13a8, B:128:0x13ae, B:131:0x13bf, B:133:0x13c3, B:134:0x13ff, B:136:0x1403, B:138:0x140c, B:139:0x1416, B:588:0x13e2), top: B:104:0x132a }] */
    /* JADX WARN: Removed duplicated region for block: B:120:0x137c  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x1385 A[Catch: Exception -> 0x1422, TryCatch #6 {Exception -> 0x1422, blocks: (B:105:0x132a, B:108:0x1334, B:110:0x1338, B:111:0x1342, B:113:0x1346, B:116:0x1360, B:118:0x1369, B:121:0x137f, B:123:0x1385, B:124:0x1391, B:126:0x13a8, B:128:0x13ae, B:131:0x13bf, B:133:0x13c3, B:134:0x13ff, B:136:0x1403, B:138:0x140c, B:139:0x1416, B:588:0x13e2), top: B:104:0x132a }] */
    /* JADX WARN: Removed duplicated region for block: B:1332:0x04d7  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x13c3 A[Catch: Exception -> 0x1422, TryCatch #6 {Exception -> 0x1422, blocks: (B:105:0x132a, B:108:0x1334, B:110:0x1338, B:111:0x1342, B:113:0x1346, B:116:0x1360, B:118:0x1369, B:121:0x137f, B:123:0x1385, B:124:0x1391, B:126:0x13a8, B:128:0x13ae, B:131:0x13bf, B:133:0x13c3, B:134:0x13ff, B:136:0x1403, B:138:0x140c, B:139:0x1416, B:588:0x13e2), top: B:104:0x132a }] */
    /* JADX WARN: Removed duplicated region for block: B:166:0x160e  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x161d  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x1642  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x181f  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x188a  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x1972  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x1993 A[Catch: Exception -> 0x19e8, TryCatch #3 {Exception -> 0x19e8, blocks: (B:218:0x198b, B:220:0x1993, B:446:0x19e5), top: B:217:0x198b }] */
    /* JADX WARN: Removed duplicated region for block: B:240:0x1a6a A[Catch: Exception -> 0x1b33, TryCatch #5 {Exception -> 0x1b33, blocks: (B:224:0x19f8, B:226:0x19fc, B:229:0x1a16, B:231:0x1a1c, B:233:0x1a22, B:235:0x1a26, B:237:0x1a39, B:238:0x1a66, B:240:0x1a6a, B:242:0x1a7e, B:244:0x1a84, B:246:0x1a88, B:249:0x1a95, B:251:0x1a92, B:254:0x1a98, B:256:0x1a9c, B:259:0x1aa1, B:261:0x1aa5, B:263:0x1ab8, B:264:0x1acd, B:265:0x1b1d, B:427:0x1ae7, B:430:0x1aee, B:431:0x1af5, B:434:0x1b0d, B:436:0x1a57, B:438:0x1a00, B:440:0x1a04, B:442:0x1a09), top: B:223:0x19f8 }] */
    /* JADX WARN: Removed duplicated region for block: B:261:0x1aa5 A[Catch: Exception -> 0x1b33, TryCatch #5 {Exception -> 0x1b33, blocks: (B:224:0x19f8, B:226:0x19fc, B:229:0x1a16, B:231:0x1a1c, B:233:0x1a22, B:235:0x1a26, B:237:0x1a39, B:238:0x1a66, B:240:0x1a6a, B:242:0x1a7e, B:244:0x1a84, B:246:0x1a88, B:249:0x1a95, B:251:0x1a92, B:254:0x1a98, B:256:0x1a9c, B:259:0x1aa1, B:261:0x1aa5, B:263:0x1ab8, B:264:0x1acd, B:265:0x1b1d, B:427:0x1ae7, B:430:0x1aee, B:431:0x1af5, B:434:0x1b0d, B:436:0x1a57, B:438:0x1a00, B:440:0x1a04, B:442:0x1a09), top: B:223:0x19f8 }] */
    /* JADX WARN: Removed duplicated region for block: B:269:0x1b40 A[Catch: Exception -> 0x1b85, TryCatch #4 {Exception -> 0x1b85, blocks: (B:267:0x1b3a, B:269:0x1b40, B:271:0x1b44, B:274:0x1b49, B:275:0x1b5f), top: B:266:0x1b3a }] */
    /* JADX WARN: Removed duplicated region for block: B:281:0x1b9e  */
    /* JADX WARN: Removed duplicated region for block: B:356:0x1e2d  */
    /* JADX WARN: Removed duplicated region for block: B:366:0x1e6a  */
    /* JADX WARN: Removed duplicated region for block: B:367:0x1e72  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x1d58  */
    /* JADX WARN: Removed duplicated region for block: B:433:0x1b08  */
    /* JADX WARN: Removed duplicated region for block: B:435:0x1b0b  */
    /* JADX WARN: Removed duplicated region for block: B:446:0x19e5 A[Catch: Exception -> 0x19e8, TRY_LEAVE, TryCatch #3 {Exception -> 0x19e8, blocks: (B:218:0x198b, B:220:0x1993, B:446:0x19e5), top: B:217:0x198b }] */
    /* JADX WARN: Removed duplicated region for block: B:449:0x18b5  */
    /* JADX WARN: Removed duplicated region for block: B:499:0x1675  */
    /* JADX WARN: Removed duplicated region for block: B:573:0x157c  */
    /* JADX WARN: Removed duplicated region for block: B:577:0x15d5  */
    /* JADX WARN: Removed duplicated region for block: B:584:0x1606 A[EDGE_INSN: B:584:0x1606->B:164:0x1606 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:585:0x159c  */
    /* JADX WARN: Removed duplicated region for block: B:588:0x13e2 A[Catch: Exception -> 0x1422, TryCatch #6 {Exception -> 0x1422, blocks: (B:105:0x132a, B:108:0x1334, B:110:0x1338, B:111:0x1342, B:113:0x1346, B:116:0x1360, B:118:0x1369, B:121:0x137f, B:123:0x1385, B:124:0x1391, B:126:0x13a8, B:128:0x13ae, B:131:0x13bf, B:133:0x13c3, B:134:0x13ff, B:136:0x1403, B:138:0x140c, B:139:0x1416, B:588:0x13e2), top: B:104:0x132a }] */
    /* JADX WARN: Removed duplicated region for block: B:590:0x137e  */
    /* JADX WARN: Removed duplicated region for block: B:622:0x12c4  */
    /* JADX WARN: Removed duplicated region for block: B:624:0x11fe  */
    /* JADX WARN: Removed duplicated region for block: B:636:0x119a  */
    /* JADX WARN: Removed duplicated region for block: B:638:0x1183  */
    /* JADX WARN: Removed duplicated region for block: B:640:0x1142  */
    /* JADX WARN: Removed duplicated region for block: B:727:0x05bb  */
    /* JADX WARN: Removed duplicated region for block: B:742:0x0e66  */
    /* JADX WARN: Removed duplicated region for block: B:745:0x0e8c  */
    /* JADX WARN: Removed duplicated region for block: B:748:0x0fea  */
    /* JADX WARN: Removed duplicated region for block: B:752:0x0ffc  */
    /* JADX WARN: Removed duplicated region for block: B:753:0x1006  */
    /* JADX WARN: Removed duplicated region for block: B:763:0x103f  */
    /* JADX WARN: Removed duplicated region for block: B:765:0x104f  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x1103  */
    /* JADX WARN: Removed duplicated region for block: B:775:0x10ec  */
    /* JADX WARN: Removed duplicated region for block: B:805:0x0e9e  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x114f  */
    /* JADX WARN: Removed duplicated region for block: B:869:0x0f1b  */
    /* JADX WARN: Removed duplicated region for block: B:872:0x0f27  */
    /* JADX WARN: Removed duplicated region for block: B:873:0x0f2a  */
    /* JADX WARN: Removed duplicated region for block: B:874:0x0f20  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x1188  */
    /* JADX WARN: Removed duplicated region for block: B:888:0x0e6e  */
    /* JADX WARN: Removed duplicated region for block: B:897:0x061d  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x11b9  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x11d1  */
    /* JADX WARN: Removed duplicated region for block: B:996:0x08ee  */
    /* JADX WARN: Type inference failed for: r0v178, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r10v50, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r3v69, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r9v27, types: [android.text.SpannableStringBuilder] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void buildLayout() {
        String str;
        boolean z;
        SpannableStringBuilder spannableStringBuilder;
        boolean z2;
        int i;
        TLRPC$Chat chat;
        boolean z3;
        String str2;
        String str3;
        boolean z4;
        CharSequence replaceNewLines;
        CharSequence highlightText;
        String str4;
        String str5;
        String str6;
        String str7;
        TLRPC$TL_forumTopic findTopic;
        String str8;
        int i2;
        SpannableStringBuilder replaceEmoji;
        CharSequence charSequence;
        boolean z5;
        boolean z6;
        CharSequence highlightText2;
        ForegroundColorSpanThemable foregroundColorSpanThemable;
        String str9;
        String string;
        String str10;
        boolean z7;
        String str11;
        boolean z8;
        boolean z9;
        SpannableStringBuilder spannableStringBuilder2;
        String str12;
        String str13;
        String str14;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        ArrayList<TLRPC$MessagePeerReaction> arrayList;
        String string2;
        ArrayList<TLRPC$MessageEntity> arrayList2;
        CharSequence charSequence2;
        SpannableStringBuilder spannableStringBuilder3;
        String str15;
        TLRPC$DraftMessage tLRPC$DraftMessage;
        MessageObject messageObject;
        String stringForMessageListDate;
        MessageObject messageObject2;
        String str16;
        String str17;
        MessagesController messagesController;
        CharSequence userName;
        String str18;
        String str19;
        int i3;
        CharSequence charSequence3;
        String str20;
        int i4;
        MessageObject messageObject3;
        int i5;
        int i6;
        int dp;
        int dp2;
        int dp3;
        int i7;
        int i8;
        ImageReceiver[] imageReceiverArr;
        int max;
        String str21;
        int i9;
        int lineCount;
        int lineCount2;
        StaticLayout staticLayout;
        float primaryHorizontal;
        float primaryHorizontal2;
        int i10;
        int lineCount3;
        int lineCount4;
        boolean z10;
        int i11;
        CharacterStyle[] characterStyleArr;
        CharSequence replaceTwoNewLinesToOne;
        int dp4;
        int dp5;
        int dp6;
        CharSequence highlightText3;
        CharSequence charSequence4;
        String str22;
        SpannableStringBuilder valueOf;
        if (this.isTransitionSupport) {
            return;
        }
        if (this.isDialogCell && !this.updateHelper.update()) {
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
            int color = Theme.getColor("chats_message_threeLines", this.resourcesProvider);
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
            int color2 = Theme.getColor("chats_message", this.resourcesProvider);
            textPaint4.linkColor = color2;
            textPaint3.setColor(color2);
            this.paintIndex = 0;
            this.thumbSize = 19;
        }
        this.currentDialogFolderDialogsCount = 0;
        CharSequence printingString = (isForumCell() || (!this.isDialogCell && !this.isTopic)) ? null : MessagesController.getInstance(this.currentAccount).getPrintingString(this.currentDialogId, getTopicId(), true);
        this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
        this.drawNameLock = false;
        this.drawVerified = false;
        this.drawPremium = false;
        this.drawScam = 0;
        this.drawPinBackground = false;
        this.thumbsCount = 0;
        this.hasVideoThumb = false;
        this.nameLayoutEllipsizeByGradient = false;
        boolean z11 = !UserObject.isUserSelf(this.user) && !this.useMeForMyMessages;
        this.printingStringType = -1;
        if (!isForumCell()) {
            this.buttonLayout = null;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || this.currentDialogFolderId != 0 || isForumCell()) {
                this.hasNameInMessage = true;
                str = "%2$s: \u2068%1$s\u2069";
            } else {
                this.hasNameInMessage = false;
                str = "\u2068%1$s\u2069";
            }
        } else if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || this.currentDialogFolderId != 0 || isForumCell()) {
            this.hasNameInMessage = true;
            str = "%2$s: %1$s";
        } else {
            this.hasNameInMessage = false;
            str = "%1$s";
        }
        MessageObject messageObject4 = this.message;
        CharSequence charSequence5 = messageObject4 != null ? messageObject4.messageText : null;
        boolean z12 = charSequence5 instanceof Spannable;
        SpannableStringBuilder spannableStringBuilder4 = charSequence5;
        if (z12) {
            SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder(charSequence5);
            for (URLSpanNoUnderlineBold uRLSpanNoUnderlineBold : (URLSpanNoUnderlineBold[]) spannableStringBuilder5.getSpans(0, spannableStringBuilder5.length(), URLSpanNoUnderlineBold.class)) {
                spannableStringBuilder5.removeSpan(uRLSpanNoUnderlineBold);
            }
            for (URLSpanNoUnderline uRLSpanNoUnderline : (URLSpanNoUnderline[]) spannableStringBuilder5.getSpans(0, spannableStringBuilder5.length(), URLSpanNoUnderline.class)) {
                spannableStringBuilder5.removeSpan(uRLSpanNoUnderline);
            }
            spannableStringBuilder4 = spannableStringBuilder5;
        }
        this.lastMessageString = spannableStringBuilder4;
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
                str22 = LocaleController.getString("FromYou", R.string.FromYou);
                CustomDialog customDialog3 = this.customDialog;
                if (customDialog3.isMedia) {
                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                    valueOf = SpannableStringBuilder.valueOf(String.format(str, this.message.messageText));
                    valueOf.setSpan(new ForegroundColorSpanThemable("chats_attachMessage", this.resourcesProvider), 0, valueOf.length(), 33);
                } else {
                    String str23 = customDialog3.message;
                    if (str23.length() > 150) {
                        str23 = str23.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                    }
                    valueOf = (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? SpannableStringBuilder.valueOf(String.format(str, str23, str22)) : SpannableStringBuilder.valueOf(String.format(str, str23.replace('\n', ' '), str22));
                }
                charSequence4 = Emoji.replaceEmoji(valueOf, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                z7 = false;
            } else {
                charSequence4 = customDialog2.message;
                if (customDialog2.isMedia) {
                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                }
                str22 = null;
                z7 = true;
            }
            stringForMessageListDate = LocaleController.stringForMessageListDate(this.customDialog.date);
            int i12 = this.customDialog.unread_count;
            if (i12 != 0) {
                this.drawCount = true;
                str16 = String.format("%d", Integer.valueOf(i12));
            } else {
                this.drawCount = false;
                str16 = null;
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
            str19 = customDialog4.name;
            str20 = str22;
            str18 = null;
            str17 = null;
            charSequence3 = charSequence4;
            spannableStringBuilder2 = "";
            z9 = true;
            i3 = -1;
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
                                    TLRPC$EmojiStatus tLRPC$EmojiStatus = this.user.emoji_status;
                                    if ((tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusUntil) && ((TLRPC$TL_emojiStatusUntil) tLRPC$EmojiStatus).until > ((int) (System.currentTimeMillis() / 1000))) {
                                        this.nameLayoutEllipsizeByGradient = true;
                                        this.emojiStatus.set(((TLRPC$TL_emojiStatusUntil) this.user.emoji_status).document_id, false);
                                    } else {
                                        TLRPC$EmojiStatus tLRPC$EmojiStatus2 = this.user.emoji_status;
                                        if (tLRPC$EmojiStatus2 instanceof TLRPC$TL_emojiStatus) {
                                            this.nameLayoutEllipsizeByGradient = true;
                                            this.emojiStatus.set(((TLRPC$TL_emojiStatus) tLRPC$EmojiStatus2).document_id, false);
                                        } else {
                                            this.nameLayoutEllipsizeByGradient = true;
                                            this.emojiStatus.set(PremiumGradient.getInstance().premiumStarDrawableMini, false);
                                        }
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
            if (tLRPC$DraftMessage2 == null || ((!TextUtils.isEmpty(tLRPC$DraftMessage2.message) || this.draftMessage.reply_to_msg_id != 0) && (i14 <= this.draftMessage.date || this.unreadCount == 0))) {
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
                        str11 = getMessageNameString();
                        CharSequence formatTopicsNames = formatTopicsNames();
                        MessageObject messageObject5 = this.message;
                        String messageStringFormatted = this.message != null ? getMessageStringFormatted(str, messageObject5 != null ? MessagesController.getRestrictionReason(messageObject5.messageOwner.restriction_reason) : null, str11, true) : "";
                        String str24 = messageStringFormatted;
                        if (this.applyName) {
                            int length = messageStringFormatted.length();
                            str24 = messageStringFormatted;
                            str24 = messageStringFormatted;
                            if (length >= 0 && str11 != null) {
                                ?? valueOf2 = SpannableStringBuilder.valueOf(messageStringFormatted);
                                valueOf2.setSpan(new ForegroundColorSpanThemable("chats_name", this.resourcesProvider), 0, Math.min(valueOf2.length(), str11.length() + 1), 0);
                                str24 = valueOf2;
                            }
                        }
                        this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                        z8 = z11;
                        spannableStringBuilder2 = "";
                        z9 = true;
                        z7 = true;
                        i = -1;
                        str15 = formatTopicsNames;
                        str12 = str24;
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
                            i = indexOf;
                            z2 = false;
                        } else {
                            this.lastPrintString = null;
                            this.printingStringType = -1;
                            spannableStringBuilder = "";
                            z2 = true;
                            i = -1;
                        }
                        if (this.draftMessage != null) {
                            str11 = LocaleController.getString("Draft", R.string.Draft);
                            if (TextUtils.isEmpty(this.draftMessage.message)) {
                                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                    z8 = z11;
                                    charSequence2 = "";
                                    z9 = true;
                                    z7 = false;
                                    spannableStringBuilder2 = spannableStringBuilder;
                                    str12 = null;
                                    str15 = charSequence2;
                                } else {
                                    SpannableStringBuilder valueOf3 = SpannableStringBuilder.valueOf(str11);
                                    valueOf3.setSpan(new ForegroundColorSpanThemable("chats_draft", this.resourcesProvider), 0, str11.length(), 33);
                                    spannableStringBuilder3 = valueOf3;
                                }
                            } else {
                                String str25 = this.draftMessage.message;
                                if (str25.length() > 150) {
                                    str25 = str25.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                }
                                SpannableStringBuilder spannableStringBuilder6 = new SpannableStringBuilder(str25);
                                MediaDataController.addTextStyleRuns(this.draftMessage, spannableStringBuilder6, 256);
                                TLRPC$DraftMessage tLRPC$DraftMessage3 = this.draftMessage;
                                if (tLRPC$DraftMessage3 != null && (arrayList2 = tLRPC$DraftMessage3.entities) != null) {
                                    TextPaint textPaint5 = this.currentMessagePaint;
                                    MediaDataController.addAnimatedEmojiSpans(arrayList2, spannableStringBuilder6, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                }
                                SpannableStringBuilder formatSpannable = AndroidUtilities.formatSpannable(str, AndroidUtilities.replaceNewLines(spannableStringBuilder6), str11);
                                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                    formatSpannable.setSpan(new ForegroundColorSpanThemable("chats_draft", this.resourcesProvider), 0, str11.length() + 1, 33);
                                }
                                spannableStringBuilder3 = Emoji.replaceEmoji(formatSpannable, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                            }
                            z8 = z11;
                            charSequence2 = spannableStringBuilder3;
                            z9 = true;
                            z7 = false;
                            spannableStringBuilder2 = spannableStringBuilder;
                            str12 = null;
                            str15 = charSequence2;
                        } else {
                            if (this.clearingDialog) {
                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                string2 = LocaleController.getString("HistoryCleared", R.string.HistoryCleared);
                            } else {
                                MessageObject messageObject6 = this.message;
                                if (messageObject6 == null) {
                                    TLRPC$EncryptedChat tLRPC$EncryptedChat = this.encryptedChat;
                                    if (tLRPC$EncryptedChat != null) {
                                        this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                        if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatRequested) {
                                            string2 = LocaleController.getString("EncryptionProcessing", R.string.EncryptionProcessing);
                                        } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatWaiting) {
                                            string2 = LocaleController.formatString("AwaitingEncryption", R.string.AwaitingEncryption, UserObject.getFirstName(this.user));
                                        } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatDiscarded) {
                                            string2 = LocaleController.getString("EncryptionRejected", R.string.EncryptionRejected);
                                        } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat) {
                                            if (tLRPC$EncryptedChat.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                string2 = LocaleController.formatString("EncryptedChatStartedOutgoing", R.string.EncryptedChatStartedOutgoing, UserObject.getFirstName(this.user));
                                            } else {
                                                string2 = LocaleController.getString("EncryptedChatStartedIncoming", R.string.EncryptedChatStartedIncoming);
                                            }
                                        }
                                    } else if (this.dialogsType == 3 && UserObject.isUserSelf(this.user)) {
                                        string2 = LocaleController.getString("SavedMessagesInfo", R.string.SavedMessagesInfo);
                                        z7 = z2;
                                        str11 = null;
                                        z9 = false;
                                        z8 = false;
                                        spannableStringBuilder2 = spannableStringBuilder;
                                        str12 = str11;
                                        str15 = string2;
                                    }
                                    z7 = z2;
                                    str11 = null;
                                    z8 = z11;
                                    string2 = "";
                                    z9 = true;
                                    spannableStringBuilder2 = spannableStringBuilder;
                                    str12 = str11;
                                    str15 = string2;
                                } else {
                                    String restrictionReason = MessagesController.getRestrictionReason(messageObject6.messageOwner.restriction_reason);
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
                                            z3 = z11;
                                            long j3 = tLRPC$MessagePeerReaction.peer_id.user_id;
                                            if (j3 != 0) {
                                                str2 = str;
                                                if (j3 != UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                    ReactionsLayoutInBubble.VisibleReaction fromTLReaction = ReactionsLayoutInBubble.VisibleReaction.fromTLReaction(tLRPC$MessagePeerReaction.reaction);
                                                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                    String str26 = fromTLReaction.emojicon;
                                                    if (str26 != null) {
                                                        str3 = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, str26);
                                                    } else {
                                                        String formatString = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, "**reaction**");
                                                        int indexOf2 = formatString.indexOf("**reaction**");
                                                        ?? spannableStringBuilder7 = new SpannableStringBuilder(formatString.replace("**reaction**", "d"));
                                                        long j4 = fromTLReaction.documentId;
                                                        TextPaint textPaint6 = this.currentMessagePaint;
                                                        spannableStringBuilder7.setSpan(new AnimatedEmojiSpan(j4, textPaint6 == null ? null : textPaint6.getFontMetricsInt()), indexOf2, indexOf2 + 1, 0);
                                                        str3 = spannableStringBuilder7;
                                                    }
                                                    z4 = true;
                                                    if (!z4) {
                                                        str14 = str3;
                                                    } else {
                                                        int i15 = this.dialogsType;
                                                        if (i15 == 2) {
                                                            TLRPC$Chat tLRPC$Chat4 = this.chat;
                                                            if (tLRPC$Chat4 != null) {
                                                                if (ChatObject.isChannel(tLRPC$Chat4)) {
                                                                    TLRPC$Chat tLRPC$Chat5 = this.chat;
                                                                    if (!tLRPC$Chat5.megagroup) {
                                                                        int i16 = tLRPC$Chat5.participants_count;
                                                                        if (i16 != 0) {
                                                                            string = LocaleController.formatPluralStringComma("Subscribers", i16);
                                                                        } else if (!ChatObject.isPublic(tLRPC$Chat5)) {
                                                                            string = LocaleController.getString("ChannelPrivate", R.string.ChannelPrivate).toLowerCase();
                                                                        } else {
                                                                            string = LocaleController.getString("ChannelPublic", R.string.ChannelPublic).toLowerCase();
                                                                        }
                                                                    }
                                                                }
                                                                TLRPC$Chat tLRPC$Chat6 = this.chat;
                                                                int i17 = tLRPC$Chat6.participants_count;
                                                                if (i17 != 0) {
                                                                    string = LocaleController.formatPluralStringComma("Members", i17);
                                                                } else if (tLRPC$Chat6.has_geo) {
                                                                    string = LocaleController.getString("MegaLocation", R.string.MegaLocation);
                                                                } else if (!ChatObject.isPublic(tLRPC$Chat6)) {
                                                                    string = LocaleController.getString("MegaPrivate", R.string.MegaPrivate).toLowerCase();
                                                                } else {
                                                                    string = LocaleController.getString("MegaPublic", R.string.MegaPublic).toLowerCase();
                                                                }
                                                            } else {
                                                                string = "";
                                                            }
                                                            this.drawCount2 = false;
                                                        } else if (i15 == 3 && UserObject.isUserSelf(this.user)) {
                                                            string = LocaleController.getString("SavedMessagesInfo", R.string.SavedMessagesInfo);
                                                        } else {
                                                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout && this.currentDialogFolderId != 0) {
                                                                replaceNewLines = formatArchivedDialogNames();
                                                            } else {
                                                                MessageObject messageObject7 = this.message;
                                                                if ((messageObject7.messageOwner instanceof TLRPC$TL_messageService) && (!MessageObject.isTopicActionMessage(messageObject7) || (this.message.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate))) {
                                                                    if (!ChatObject.isChannelAndNotMegaGroup(this.chat) || !(this.message.messageOwner.action instanceof TLRPC$TL_messageActionChannelMigrateFrom)) {
                                                                        z5 = z3;
                                                                    } else {
                                                                        spannableStringBuilder4 = "";
                                                                        z5 = false;
                                                                    }
                                                                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                    str9 = spannableStringBuilder4;
                                                                    z6 = true;
                                                                    str13 = str9;
                                                                    str10 = null;
                                                                    charSequence = str13;
                                                                    String str27 = str10;
                                                                    if (this.currentDialogFolderId != 0) {
                                                                    }
                                                                    z7 = z2;
                                                                    str11 = str27;
                                                                    z8 = z5;
                                                                    z9 = z6;
                                                                    spannableStringBuilder2 = spannableStringBuilder;
                                                                    str12 = null;
                                                                    str15 = charSequence;
                                                                } else {
                                                                    this.needEmoji = true;
                                                                    updateMessageThumbs();
                                                                    TLRPC$Chat tLRPC$Chat7 = this.chat;
                                                                    if (tLRPC$Chat7 != null && tLRPC$Chat7.id > 0 && chat == null && ((!ChatObject.isChannel(tLRPC$Chat7) || ChatObject.isMegagroup(this.chat)) && !ForumUtilities.isTopicCreateMessage(this.message))) {
                                                                        String messageNameString = getMessageNameString();
                                                                        if (this.chat.forum && !this.isTopic && !this.useFromUserAsAvatar) {
                                                                            CharSequence topicIconName = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopicIconName(this.chat, this.message, this.currentMessagePaint);
                                                                            if (!TextUtils.isEmpty(topicIconName)) {
                                                                                SpannableStringBuilder spannableStringBuilder8 = new SpannableStringBuilder("-");
                                                                                ColoredImageSpan coloredImageSpan = new ColoredImageSpan(ContextCompat.getDrawable(ApplicationLoader.applicationContext, R.drawable.msg_mini_forumarrow).mutate());
                                                                                coloredImageSpan.setColorKey((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? null : "chats_nameMessage");
                                                                                spannableStringBuilder8.setSpan(coloredImageSpan, 0, 1, 0);
                                                                                ?? spannableStringBuilder9 = new SpannableStringBuilder();
                                                                                spannableStringBuilder9.append(messageNameString).append((CharSequence) spannableStringBuilder8).append(topicIconName);
                                                                                str8 = spannableStringBuilder9;
                                                                                SpannableStringBuilder messageStringFormatted2 = getMessageStringFormatted(str2, restrictionReason, str8, false);
                                                                                if (!this.useFromUserAsAvatar || ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && (this.currentDialogFolderId == 0 || messageStringFormatted2.length() <= 0))) {
                                                                                    i2 = 0;
                                                                                } else {
                                                                                    try {
                                                                                        foregroundColorSpanThemable = new ForegroundColorSpanThemable("chats_nameMessage", this.resourcesProvider);
                                                                                        i2 = str8.length() + 1;
                                                                                    } catch (Exception e) {
                                                                                        e = e;
                                                                                        i2 = 0;
                                                                                    }
                                                                                    try {
                                                                                        messageStringFormatted2.setSpan(foregroundColorSpanThemable, 0, i2, 33);
                                                                                    } catch (Exception e2) {
                                                                                        e = e2;
                                                                                        FileLog.e(e);
                                                                                        replaceEmoji = Emoji.replaceEmoji(messageStringFormatted2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                        if (this.message.hasHighlightedWords()) {
                                                                                        }
                                                                                        if (this.thumbsCount > 0) {
                                                                                        }
                                                                                        charSequence = replaceEmoji;
                                                                                        z5 = z3;
                                                                                        z2 = false;
                                                                                        z6 = true;
                                                                                        str10 = str8;
                                                                                        String str272 = str10;
                                                                                        if (this.currentDialogFolderId != 0) {
                                                                                        }
                                                                                        z7 = z2;
                                                                                        str11 = str272;
                                                                                        z8 = z5;
                                                                                        z9 = z6;
                                                                                        spannableStringBuilder2 = spannableStringBuilder;
                                                                                        str12 = null;
                                                                                        str15 = charSequence;
                                                                                        if (this.draftMessage != null) {
                                                                                        }
                                                                                        messageObject2 = this.message;
                                                                                        if (messageObject2 == null) {
                                                                                        }
                                                                                        this.promoDialog = false;
                                                                                        messagesController = MessagesController.getInstance(this.currentAccount);
                                                                                        if (this.dialogsType == 0) {
                                                                                        }
                                                                                        if (this.currentDialogFolderId != 0) {
                                                                                        }
                                                                                        str20 = str11;
                                                                                        if (!z9) {
                                                                                        }
                                                                                        if (!drawLock2()) {
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
                                                                                        if (str19 instanceof String) {
                                                                                        }
                                                                                        if (this.nameLayoutEllipsizeByGradient) {
                                                                                        }
                                                                                        float f = dp6;
                                                                                        this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(str19.toString()) <= f;
                                                                                        if (!this.twoLinesForName) {
                                                                                        }
                                                                                        CharSequence replaceEmoji2 = Emoji.replaceEmoji(str19, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                        MessageObject messageObject8 = this.message;
                                                                                        if (messageObject8 != null) {
                                                                                        }
                                                                                        if (!this.twoLinesForName) {
                                                                                        }
                                                                                        this.nameLayoutTranslateX = (this.nameLayoutEllipsizeByGradient || !this.nameLayout.isRtlCharAt(0)) ? 0.0f : -AndroidUtilities.dp(36.0f);
                                                                                        this.nameLayoutEllipsizeLeft = this.nameLayout.isRtlCharAt(0);
                                                                                        this.animatedEmojiStackName = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStackName, this.nameLayout);
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
                                                                                        i7 = measuredWidth;
                                                                                        this.avatarImage.setImageCoords(dp2, dp, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                                                                                        i8 = 0;
                                                                                        while (true) {
                                                                                            imageReceiverArr = this.thumbImage;
                                                                                            if (i8 < imageReceiverArr.length) {
                                                                                            }
                                                                                            imageReceiverArr[i8].setImageCoords(((this.thumbSize + 2) * i8) + dp3, AndroidUtilities.dp(31.0f) + dp + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                                                                                            i8++;
                                                                                            dp = dp;
                                                                                        }
                                                                                        int i18 = dp;
                                                                                        int i19 = i7;
                                                                                        if (this.twoLinesForName) {
                                                                                        }
                                                                                        if (getIsPinned()) {
                                                                                        }
                                                                                        if (!this.drawError) {
                                                                                        }
                                                                                        if (z7) {
                                                                                        }
                                                                                        max = Math.max(AndroidUtilities.dp(12.0f), i19);
                                                                                        this.buttonTop = AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 58.0f : 62.0f);
                                                                                        if (!isForumCell()) {
                                                                                        }
                                                                                        str21 = str20;
                                                                                        if (this.twoLinesForName) {
                                                                                        }
                                                                                        this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack2, this.messageNameLayout);
                                                                                        this.buttonCreated = false;
                                                                                        if (TextUtils.isEmpty(str18)) {
                                                                                        }
                                                                                        this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack3, this.buttonLayout);
                                                                                        z10 = this.useForceThreeLines;
                                                                                        if (!z10) {
                                                                                        }
                                                                                        this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                                                                                        charSequence3 = str21;
                                                                                        str21 = null;
                                                                                        if (charSequence3 instanceof Spannable) {
                                                                                        }
                                                                                        if (!this.useForceThreeLines) {
                                                                                            if (this.thumbsCount > 0) {
                                                                                            }
                                                                                            this.messageLayout = new StaticLayout(charSequence3, this.currentMessagePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                                                                                            this.spoilersPool.addAll(this.spoilers);
                                                                                            this.spoilers.clear();
                                                                                            SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
                                                                                            if (!TextUtils.isEmpty(spannableStringBuilder2)) {
                                                                                            }
                                                                                            i9 = i3;
                                                                                            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                                                                                            if (!LocaleController.isRTL) {
                                                                                            }
                                                                                            staticLayout = this.typingLayout;
                                                                                            if (staticLayout != null) {
                                                                                            }
                                                                                            updateThumbsPosition();
                                                                                        }
                                                                                        if (this.thumbsCount > 0) {
                                                                                            max += AndroidUtilities.dp(5.0f);
                                                                                        }
                                                                                        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence3, this.currentMessagePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, str21 == null ? 1 : 2);
                                                                                        this.spoilersPool.addAll(this.spoilers);
                                                                                        this.spoilers.clear();
                                                                                        SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
                                                                                        if (!TextUtils.isEmpty(spannableStringBuilder2)) {
                                                                                        }
                                                                                        i9 = i3;
                                                                                        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                                                                                        if (!LocaleController.isRTL) {
                                                                                        }
                                                                                        staticLayout = this.typingLayout;
                                                                                        if (staticLayout != null) {
                                                                                        }
                                                                                        updateThumbsPosition();
                                                                                    }
                                                                                }
                                                                                replaceEmoji = Emoji.replaceEmoji(messageStringFormatted2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                if (this.message.hasHighlightedWords() && (highlightText2 = AndroidUtilities.highlightText(replaceEmoji, this.message.highlightedWords, this.resourcesProvider)) != null) {
                                                                                    replaceEmoji = highlightText2;
                                                                                }
                                                                                if (this.thumbsCount > 0) {
                                                                                    boolean z13 = replaceEmoji instanceof SpannableStringBuilder;
                                                                                    replaceEmoji = replaceEmoji;
                                                                                    if (!z13) {
                                                                                        replaceEmoji = new SpannableStringBuilder(replaceEmoji);
                                                                                    }
                                                                                    SpannableStringBuilder spannableStringBuilder10 = (SpannableStringBuilder) replaceEmoji;
                                                                                    if (i2 >= spannableStringBuilder10.length()) {
                                                                                        spannableStringBuilder10.append((CharSequence) " ");
                                                                                        spannableStringBuilder10.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 5)), spannableStringBuilder10.length() - 1, spannableStringBuilder10.length(), 33);
                                                                                    } else {
                                                                                        spannableStringBuilder10.insert(i2, (CharSequence) " ");
                                                                                        spannableStringBuilder10.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 5)), i2, i2 + 1, 33);
                                                                                    }
                                                                                }
                                                                                charSequence = replaceEmoji;
                                                                                z5 = z3;
                                                                                z2 = false;
                                                                                z6 = true;
                                                                                str10 = str8;
                                                                                String str2722 = str10;
                                                                                if (this.currentDialogFolderId != 0) {
                                                                                    str2722 = formatArchivedDialogNames();
                                                                                }
                                                                                z7 = z2;
                                                                                str11 = str2722;
                                                                                z8 = z5;
                                                                                z9 = z6;
                                                                                spannableStringBuilder2 = spannableStringBuilder;
                                                                                str12 = null;
                                                                                str15 = charSequence;
                                                                            }
                                                                        }
                                                                        str8 = messageNameString;
                                                                        SpannableStringBuilder messageStringFormatted22 = getMessageStringFormatted(str2, restrictionReason, str8, false);
                                                                        if (!this.useFromUserAsAvatar) {
                                                                        }
                                                                        i2 = 0;
                                                                        replaceEmoji = Emoji.replaceEmoji(messageStringFormatted22, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                        if (this.message.hasHighlightedWords()) {
                                                                            replaceEmoji = highlightText2;
                                                                        }
                                                                        if (this.thumbsCount > 0) {
                                                                        }
                                                                        charSequence = replaceEmoji;
                                                                        z5 = z3;
                                                                        z2 = false;
                                                                        z6 = true;
                                                                        str10 = str8;
                                                                        String str27222 = str10;
                                                                        if (this.currentDialogFolderId != 0) {
                                                                        }
                                                                        z7 = z2;
                                                                        str11 = str27222;
                                                                        z8 = z5;
                                                                        z9 = z6;
                                                                        spannableStringBuilder2 = spannableStringBuilder;
                                                                        str12 = null;
                                                                        str15 = charSequence;
                                                                    } else {
                                                                        boolean isEmpty = TextUtils.isEmpty(restrictionReason);
                                                                        String str28 = restrictionReason;
                                                                        if (isEmpty) {
                                                                            if (MessageObject.isTopicActionMessage(this.message)) {
                                                                                MessageObject messageObject9 = this.message;
                                                                                CharSequence charSequence6 = messageObject9.messageTextShort;
                                                                                if (charSequence6 == null || ((messageObject9.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate) && this.isTopic)) {
                                                                                    charSequence6 = messageObject9.messageText;
                                                                                }
                                                                                if ((messageObject9.topicIconDrawable[0] instanceof ForumBubbleDrawable) && (findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-this.message.getDialogId(), MessageObject.getTopicId(this.message.messageOwner, true))) != null) {
                                                                                    ((ForumBubbleDrawable) this.message.topicIconDrawable[0]).setColor(findTopic.icon_color);
                                                                                }
                                                                                str28 = charSequence6;
                                                                            } else {
                                                                                TLRPC$MessageMedia tLRPC$MessageMedia = this.message.messageOwner.media;
                                                                                if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia.ttl_seconds != 0) {
                                                                                    str28 = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
                                                                                } else if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) && (tLRPC$MessageMedia.document instanceof TLRPC$TL_documentEmpty) && tLRPC$MessageMedia.ttl_seconds != 0) {
                                                                                    str28 = LocaleController.getString("AttachVideoExpired", R.string.AttachVideoExpired);
                                                                                } else if (getCaptionMessage() != null) {
                                                                                    MessageObject captionMessage = getCaptionMessage();
                                                                                    if (!this.needEmoji) {
                                                                                        str7 = "";
                                                                                    } else if (captionMessage.isVideo()) {
                                                                                        str7 = "📹 ";
                                                                                    } else if (captionMessage.isVoice()) {
                                                                                        str7 = "🎤 ";
                                                                                    } else if (captionMessage.isMusic()) {
                                                                                        str7 = "🎧 ";
                                                                                    } else {
                                                                                        str7 = captionMessage.isPhoto() ? "🖼 " : "📎 ";
                                                                                    }
                                                                                    if (captionMessage.hasHighlightedWords() && !TextUtils.isEmpty(captionMessage.messageOwner.message)) {
                                                                                        String str29 = captionMessage.messageTrimmedToHighlight;
                                                                                        int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) + 24);
                                                                                        if (this.hasNameInMessage) {
                                                                                            if (!TextUtils.isEmpty(null)) {
                                                                                                throw null;
                                                                                            }
                                                                                            measuredWidth2 = (int) (measuredWidth2 - this.currentMessagePaint.measureText(": "));
                                                                                        }
                                                                                        if (measuredWidth2 > 0) {
                                                                                            str29 = AndroidUtilities.ellipsizeCenterEnd(str29, captionMessage.highlightedWords.get(0), measuredWidth2, this.currentMessagePaint, 130).toString();
                                                                                        }
                                                                                        str28 = str7 + str29;
                                                                                    } else {
                                                                                        SpannableStringBuilder spannableStringBuilder11 = new SpannableStringBuilder(captionMessage.caption);
                                                                                        TLRPC$Message tLRPC$Message = captionMessage.messageOwner;
                                                                                        if (tLRPC$Message != null) {
                                                                                            MediaDataController.addTextStyleRuns(tLRPC$Message.entities, captionMessage.caption, spannableStringBuilder11, 256);
                                                                                            ArrayList<TLRPC$MessageEntity> arrayList3 = captionMessage.messageOwner.entities;
                                                                                            TextPaint textPaint7 = this.currentMessagePaint;
                                                                                            MediaDataController.addAnimatedEmojiSpans(arrayList3, spannableStringBuilder11, textPaint7 == null ? null : textPaint7.getFontMetricsInt());
                                                                                        }
                                                                                        str28 = new SpannableStringBuilder(str7).append((CharSequence) spannableStringBuilder11);
                                                                                    }
                                                                                } else if (this.thumbsCount > 1) {
                                                                                    if (this.hasVideoThumb) {
                                                                                        ArrayList<MessageObject> arrayList4 = this.groupMessages;
                                                                                        str6 = LocaleController.formatPluralString("Media", arrayList4 == null ? 0 : arrayList4.size(), new Object[0]);
                                                                                    } else {
                                                                                        ArrayList<MessageObject> arrayList5 = this.groupMessages;
                                                                                        str6 = LocaleController.formatPluralString("Photos", arrayList5 == null ? 0 : arrayList5.size(), new Object[0]);
                                                                                    }
                                                                                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                    str28 = str6;
                                                                                } else {
                                                                                    MessageObject messageObject10 = this.message;
                                                                                    TLRPC$MessageMedia tLRPC$MessageMedia2 = messageObject10.messageOwner.media;
                                                                                    if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPoll) {
                                                                                        str5 = "📊 " + ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia2).poll.question;
                                                                                    } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGame) {
                                                                                        str5 = "🎮 " + this.message.messageOwner.media.game.title;
                                                                                    } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                        str5 = tLRPC$MessageMedia2.title;
                                                                                    } else if (messageObject10.type == 14) {
                                                                                        str5 = String.format("🎧 %s - %s", messageObject10.getMusicAuthor(), this.message.getMusicTitle());
                                                                                    } else {
                                                                                        if (messageObject10.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                            str4 = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 23), this.currentMessagePaint, 130).toString();
                                                                                        } else {
                                                                                            SpannableStringBuilder spannableStringBuilder12 = new SpannableStringBuilder(spannableStringBuilder4);
                                                                                            MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder12, 256);
                                                                                            MessageObject messageObject11 = this.message;
                                                                                            str4 = spannableStringBuilder12;
                                                                                            if (messageObject11 != null) {
                                                                                                TLRPC$Message tLRPC$Message2 = messageObject11.messageOwner;
                                                                                                str4 = spannableStringBuilder12;
                                                                                                if (tLRPC$Message2 != null) {
                                                                                                    ArrayList<TLRPC$MessageEntity> arrayList6 = tLRPC$Message2.entities;
                                                                                                    TextPaint textPaint8 = this.currentMessagePaint;
                                                                                                    MediaDataController.addAnimatedEmojiSpans(arrayList6, spannableStringBuilder12, textPaint8 == null ? null : textPaint8.getFontMetricsInt());
                                                                                                    str4 = spannableStringBuilder12;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        AndroidUtilities.highlightText(str4, this.message.highlightedWords, this.resourcesProvider);
                                                                                        str5 = str4;
                                                                                    }
                                                                                    MessageObject messageObject12 = this.message;
                                                                                    str28 = str5;
                                                                                    if (messageObject12.messageOwner.media != null) {
                                                                                        str28 = str5;
                                                                                        if (!messageObject12.isMediaEmpty()) {
                                                                                            this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                            str28 = str5;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        str14 = str28;
                                                                        if (this.thumbsCount > 0) {
                                                                            if (this.message.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                replaceNewLines = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp((((this.messagePaddingStart + 23) + ((this.thumbSize + 2) * this.thumbsCount)) - 2) + 5), this.currentMessagePaint, 130).toString();
                                                                            } else {
                                                                                int length2 = str28.length();
                                                                                String str30 = str28;
                                                                                if (length2 > 150) {
                                                                                    str30 = str28.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                }
                                                                                replaceNewLines = AndroidUtilities.replaceNewLines(str30);
                                                                            }
                                                                            if (!(replaceNewLines instanceof SpannableStringBuilder)) {
                                                                                replaceNewLines = new SpannableStringBuilder(replaceNewLines);
                                                                            }
                                                                            SpannableStringBuilder spannableStringBuilder13 = (SpannableStringBuilder) replaceNewLines;
                                                                            spannableStringBuilder13.insert(0, (CharSequence) " ");
                                                                            spannableStringBuilder13.setSpan(new FixedWidthSpan(AndroidUtilities.dp((((this.thumbSize + 2) * this.thumbsCount) - 2) + 5)), 0, 1, 33);
                                                                            Emoji.replaceEmoji(spannableStringBuilder13, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
                                                                            if (this.message.hasHighlightedWords()) {
                                                                                highlightText = AndroidUtilities.highlightText(spannableStringBuilder13, this.message.highlightedWords, this.resourcesProvider);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            highlightText = replaceNewLines;
                                                            z5 = z3;
                                                            z2 = false;
                                                            str9 = highlightText;
                                                            z6 = true;
                                                            str13 = str9;
                                                            str10 = null;
                                                            charSequence = str13;
                                                            String str272222 = str10;
                                                            if (this.currentDialogFolderId != 0) {
                                                            }
                                                            z7 = z2;
                                                            str11 = str272222;
                                                            z8 = z5;
                                                            z9 = z6;
                                                            spannableStringBuilder2 = spannableStringBuilder;
                                                            str12 = null;
                                                            str15 = charSequence;
                                                        }
                                                        z6 = false;
                                                        z5 = false;
                                                        str13 = string;
                                                        str10 = null;
                                                        charSequence = str13;
                                                        String str2722222 = str10;
                                                        if (this.currentDialogFolderId != 0) {
                                                        }
                                                        z7 = z2;
                                                        str11 = str2722222;
                                                        z8 = z5;
                                                        z9 = z6;
                                                        spannableStringBuilder2 = spannableStringBuilder;
                                                        str12 = null;
                                                        str15 = charSequence;
                                                    }
                                                    z5 = z3;
                                                    str9 = str14;
                                                    z6 = true;
                                                    str13 = str9;
                                                    str10 = null;
                                                    charSequence = str13;
                                                    String str27222222 = str10;
                                                    if (this.currentDialogFolderId != 0) {
                                                    }
                                                    z7 = z2;
                                                    str11 = str27222222;
                                                    z8 = z5;
                                                    z9 = z6;
                                                    spannableStringBuilder2 = spannableStringBuilder;
                                                    str12 = null;
                                                    str15 = charSequence;
                                                }
                                                str3 = "";
                                                z4 = false;
                                                if (!z4) {
                                                }
                                                z5 = z3;
                                                str9 = str14;
                                                z6 = true;
                                                str13 = str9;
                                                str10 = null;
                                                charSequence = str13;
                                                String str272222222 = str10;
                                                if (this.currentDialogFolderId != 0) {
                                                }
                                                z7 = z2;
                                                str11 = str272222222;
                                                z8 = z5;
                                                z9 = z6;
                                                spannableStringBuilder2 = spannableStringBuilder;
                                                str12 = null;
                                                str15 = charSequence;
                                            }
                                            str2 = str;
                                            str3 = "";
                                            z4 = false;
                                            if (!z4) {
                                            }
                                            z5 = z3;
                                            str9 = str14;
                                            z6 = true;
                                            str13 = str9;
                                            str10 = null;
                                            charSequence = str13;
                                            String str2722222222 = str10;
                                            if (this.currentDialogFolderId != 0) {
                                            }
                                            z7 = z2;
                                            str11 = str2722222222;
                                            z8 = z5;
                                            z9 = z6;
                                            spannableStringBuilder2 = spannableStringBuilder;
                                            str12 = null;
                                            str15 = charSequence;
                                        }
                                    }
                                    z3 = z11;
                                    str2 = str;
                                    str3 = "";
                                    z4 = false;
                                    if (!z4) {
                                    }
                                    z5 = z3;
                                    str9 = str14;
                                    z6 = true;
                                    str13 = str9;
                                    str10 = null;
                                    charSequence = str13;
                                    String str27222222222 = str10;
                                    if (this.currentDialogFolderId != 0) {
                                    }
                                    z7 = z2;
                                    str11 = str27222222222;
                                    z8 = z5;
                                    z9 = z6;
                                    spannableStringBuilder2 = spannableStringBuilder;
                                    str12 = null;
                                    str15 = charSequence;
                                }
                            }
                            z7 = z2;
                            str11 = null;
                            z8 = z11;
                            z9 = true;
                            spannableStringBuilder2 = spannableStringBuilder;
                            str12 = str11;
                            str15 = string2;
                        }
                    }
                    if (this.draftMessage != null) {
                        stringForMessageListDate = LocaleController.stringForMessageListDate(tLRPC$DraftMessage.date);
                    } else {
                        int i20 = this.lastMessageDate;
                        if (i20 != 0) {
                            stringForMessageListDate = LocaleController.stringForMessageListDate(i20);
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
                        str16 = null;
                        str17 = null;
                    } else {
                        if (this.currentDialogFolderId != 0) {
                            int i21 = this.unreadCount;
                            int i22 = this.mentionCount;
                            if (i21 + i22 <= 0) {
                                this.drawCount = false;
                                this.drawMention = false;
                                str16 = null;
                            } else if (i21 > i22) {
                                this.drawCount = true;
                                this.drawMention = false;
                                str16 = String.format("%d", Integer.valueOf(i21 + i22));
                            } else {
                                this.drawCount = false;
                                this.drawMention = true;
                                str17 = String.format("%d", Integer.valueOf(i21 + i22));
                                str16 = null;
                                this.drawReactionMention = false;
                            }
                            str17 = null;
                            this.drawReactionMention = false;
                        } else {
                            if (this.clearingDialog) {
                                this.drawCount = false;
                                z8 = false;
                            } else {
                                int i23 = this.unreadCount;
                                if (i23 != 0 && (i23 != 1 || i23 != this.mentionCount || messageObject2 == null || !messageObject2.messageOwner.mentioned)) {
                                    this.drawCount = true;
                                    str16 = String.format("%d", Integer.valueOf(i23));
                                } else if (this.markUnread) {
                                    this.drawCount = true;
                                    str16 = "";
                                } else {
                                    this.drawCount = false;
                                }
                                if (this.mentionCount == 0) {
                                    this.drawMention = true;
                                    str17 = "@";
                                } else {
                                    this.drawMention = false;
                                    str17 = null;
                                }
                                if (this.reactionMentionCount <= 0) {
                                    this.drawReactionMention = true;
                                } else {
                                    this.drawReactionMention = false;
                                }
                            }
                            str16 = null;
                            if (this.mentionCount == 0) {
                            }
                            if (this.reactionMentionCount <= 0) {
                            }
                        }
                        if (this.message.isOut() && this.draftMessage == null && z8) {
                            MessageObject messageObject13 = this.message;
                            if (!(messageObject13.messageOwner.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                                if (messageObject13.isSending()) {
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
                                        int i24 = this.readOutboxMaxId;
                                        this.drawCheck1 = (i24 > 0 && i24 >= this.message.getId()) || !this.message.isUnread() || (ChatObject.isChannel(this.chat) && !this.chat.megagroup);
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
                        i4 = messagesController.promoDialogType;
                        if (i4 != MessagesController.PROMO_TYPE_PROXY) {
                            stringForMessageListDate = LocaleController.getString("UseProxySponsor", R.string.UseProxySponsor);
                        } else if (i4 == MessagesController.PROMO_TYPE_PSA) {
                            stringForMessageListDate = LocaleController.getString("PsaType_" + messagesController.promoPsaType);
                            if (TextUtils.isEmpty(stringForMessageListDate)) {
                                stringForMessageListDate = LocaleController.getString("PsaTypeDefault", R.string.PsaTypeDefault);
                            }
                            if (!TextUtils.isEmpty(messagesController.promoPsaMessage)) {
                                str15 = messagesController.promoPsaMessage;
                                this.thumbsCount = 0;
                            }
                        }
                    }
                    if (this.currentDialogFolderId != 0) {
                        charSequence3 = str15;
                        str18 = str12;
                        str19 = LocaleController.getString("ArchivedChats", R.string.ArchivedChats);
                        i3 = i;
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
                                userName = this.showTopicIconInName ? ForumUtilities.getTopicSpannedName(this.forumTopic, Theme.dialogs_namePaint[this.paintIndex], drawableArr) : this.forumTopic.title;
                            } else {
                                userName = tLRPC$Chat8.title;
                            }
                            if (userName != null && userName.length() == 0) {
                                userName = LocaleController.getString("HiddenName", R.string.HiddenName);
                            }
                            str18 = str12;
                            str19 = userName;
                            i3 = i;
                            charSequence3 = str15;
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
                                str18 = str12;
                                str19 = userName;
                                i3 = i;
                                charSequence3 = str15;
                            }
                            userName = "";
                            if (userName != null) {
                            }
                            str18 = str12;
                            str19 = userName;
                            i3 = i;
                            charSequence3 = str15;
                        }
                    }
                    str20 = str11;
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
            if (this.dialogsType == 0) {
                this.drawPinBackground = true;
                this.promoDialog = true;
                i4 = messagesController.promoDialogType;
                if (i4 != MessagesController.PROMO_TYPE_PROXY) {
                }
            }
            if (this.currentDialogFolderId != 0) {
            }
            str20 = str11;
        }
        if (!z9) {
            i5 = (int) Math.ceil(Theme.dialogs_timePaint.measureText(stringForMessageListDate));
            this.timeLayout = new StaticLayout(stringForMessageListDate, Theme.dialogs_timePaint, i5, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (!LocaleController.isRTL) {
                this.timeLeft = (getMeasuredWidth() - AndroidUtilities.dp(15.0f)) - i5;
            } else {
                this.timeLeft = AndroidUtilities.dp(15.0f);
            }
        } else {
            this.timeLayout = null;
            this.timeLeft = 0;
            i5 = 0;
        }
        if (!drawLock2()) {
            if (LocaleController.isRTL) {
                this.lock2Left = this.timeLeft + i5 + AndroidUtilities.dp(4.0f);
            } else {
                this.lock2Left = (this.timeLeft - Theme.dialogs_lock2Drawable.getIntrinsicWidth()) - AndroidUtilities.dp(4.0f);
            }
            i6 = Theme.dialogs_lock2Drawable.getIntrinsicWidth() + AndroidUtilities.dp(4.0f) + 0;
            i5 += i6;
        } else {
            i6 = 0;
        }
        if (LocaleController.isRTL) {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(22.0f)) - i5;
        } else {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp((this.messagePaddingStart + 5) + 8)) - i5;
            this.nameLeft += i5;
        }
        if (this.drawNameLock) {
            this.nameWidth -= AndroidUtilities.dp(4.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
        }
        if (!this.drawClock) {
            int intrinsicWidth2 = Theme.dialogs_clockDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            this.nameWidth -= intrinsicWidth2;
            if (!LocaleController.isRTL) {
                this.clockDrawLeft = (this.timeLeft - i6) - intrinsicWidth2;
            } else {
                this.clockDrawLeft = this.timeLeft + i5 + AndroidUtilities.dp(5.0f);
                this.nameLeft += intrinsicWidth2;
            }
        } else if (this.drawCheck2) {
            int intrinsicWidth3 = Theme.dialogs_checkDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            int i25 = this.nameWidth - intrinsicWidth3;
            this.nameWidth = i25;
            if (this.drawCheck1) {
                this.nameWidth = i25 - (Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0f));
                if (!LocaleController.isRTL) {
                    int i26 = (this.timeLeft - i6) - intrinsicWidth3;
                    this.halfCheckDrawLeft = i26;
                    this.checkDrawLeft = i26 - AndroidUtilities.dp(5.5f);
                } else {
                    int dp8 = this.timeLeft + i5 + AndroidUtilities.dp(5.0f);
                    this.checkDrawLeft = dp8;
                    this.halfCheckDrawLeft = dp8 + AndroidUtilities.dp(5.5f);
                    this.nameLeft += (intrinsicWidth3 + Theme.dialogs_halfCheckDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(8.0f);
                }
            } else if (!LocaleController.isRTL) {
                this.checkDrawLeft1 = (this.timeLeft - i6) - intrinsicWidth3;
            } else {
                this.checkDrawLeft1 = this.timeLeft + i5 + AndroidUtilities.dp(5.0f);
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
            if (str19 instanceof String) {
                str19 = ((String) str19).replace('\n', ' ');
            }
            if (this.nameLayoutEllipsizeByGradient) {
                this.nameLayoutFits = str19.length() == TextUtils.ellipsize(str19, Theme.dialogs_namePaint[this.paintIndex], (float) dp6, TextUtils.TruncateAt.END).length();
                dp6 += AndroidUtilities.dp(48.0f);
            }
            float f2 = dp6;
            this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(str19.toString()) <= f2;
            if (!this.twoLinesForName) {
                str19 = TextUtils.ellipsize(str19, Theme.dialogs_namePaint[this.paintIndex], f2, TextUtils.TruncateAt.END);
            }
            CharSequence replaceEmoji22 = Emoji.replaceEmoji(str19, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            MessageObject messageObject82 = this.message;
            CharSequence charSequence7 = (messageObject82 != null || !messageObject82.hasHighlightedWords() || (highlightText3 = AndroidUtilities.highlightText(replaceEmoji22, this.message.highlightedWords, this.resourcesProvider)) == null) ? replaceEmoji22 : highlightText3;
            if (!this.twoLinesForName) {
                this.nameLayout = StaticLayoutEx.createStaticLayout(charSequence7, Theme.dialogs_namePaint[this.paintIndex], dp6, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, dp6, 2);
            } else {
                this.nameLayout = new StaticLayout(charSequence7, Theme.dialogs_namePaint[this.paintIndex], Math.max(dp6, this.nameWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
            this.nameLayoutTranslateX = (this.nameLayoutEllipsizeByGradient || !this.nameLayout.isRtlCharAt(0)) ? 0.0f : -AndroidUtilities.dp(36.0f);
            this.nameLayoutEllipsizeLeft = this.nameLayout.isRtlCharAt(0);
        } catch (Exception e3) {
            FileLog.e(e3);
        }
        this.animatedEmojiStackName = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStackName, this.nameLayout);
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
                dp2 = getMeasuredWidth() - AndroidUtilities.dp(66.0f);
                dp3 = dp2 - AndroidUtilities.dp(31.0f);
            } else {
                int dp14 = AndroidUtilities.dp(this.messagePaddingStart + 6);
                this.messageNameLeft = dp14;
                this.messageLeft = dp14;
                this.typingLeft = dp14;
                dp2 = AndroidUtilities.dp(10.0f);
                dp3 = AndroidUtilities.dp(69.0f) + dp2;
            }
            i7 = measuredWidth3;
            this.avatarImage.setImageCoords(dp2, dp, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            i8 = 0;
            while (true) {
                imageReceiverArr = this.thumbImage;
                if (i8 < imageReceiverArr.length) {
                    break;
                }
                imageReceiverArr[i8].setImageCoords(((this.thumbSize + 2) * i8) + dp3, AndroidUtilities.dp(31.0f) + dp + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                i8++;
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
                dp4 = getMeasuredWidth() - AndroidUtilities.dp(64.0f);
                dp5 = dp4 - AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 11);
            } else {
                int dp16 = AndroidUtilities.dp(this.messagePaddingStart + 4);
                this.messageNameLeft = dp16;
                this.messageLeft = dp16;
                this.typingLeft = dp16;
                dp4 = AndroidUtilities.dp(10.0f);
                dp5 = AndroidUtilities.dp(67.0f) + dp4;
            }
            i7 = measuredWidth4;
            this.avatarImage.setImageCoords(dp4, dp, AndroidUtilities.dp(54.0f), AndroidUtilities.dp(54.0f));
            int i27 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr2 = this.thumbImage;
                if (i27 >= imageReceiverArr2.length) {
                    break;
                }
                imageReceiverArr2[i27].setImageCoords(((this.thumbSize + 2) * i27) + dp5, AndroidUtilities.dp(30.0f) + dp + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0), AndroidUtilities.dp(this.thumbSize), AndroidUtilities.dp(this.thumbSize));
                i27++;
                dp = dp;
            }
        }
        int i182 = dp;
        int i192 = i7;
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
            i192 -= dp17;
            if (!LocaleController.isRTL) {
                this.errorLeft = getMeasuredWidth() - AndroidUtilities.dp(34.0f);
            } else {
                this.errorLeft = AndroidUtilities.dp(11.0f);
                this.messageLeft += dp17;
                this.typingLeft += dp17;
                this.messageNameLeft += dp17;
            }
        } else if (str16 != null || str17 != null || this.drawReactionMention) {
            if (str16 != null) {
                this.countWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str16)));
                this.countLayout = new StaticLayout(str16, Theme.dialogs_countTextPaint, this.countWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                int dp18 = this.countWidth + AndroidUtilities.dp(18.0f);
                i192 -= dp18;
                if (!LocaleController.isRTL) {
                    this.countLeft = (getMeasuredWidth() - this.countWidth) - AndroidUtilities.dp(20.0f);
                } else {
                    this.countLeft = AndroidUtilities.dp(20.0f);
                    this.messageLeft += dp18;
                    this.typingLeft += dp18;
                    this.messageNameLeft += dp18;
                }
                this.drawCount = true;
            } else {
                this.countWidth = 0;
            }
            if (str17 != null) {
                if (this.currentDialogFolderId != 0) {
                    this.mentionWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str17)));
                    this.mentionLayout = new StaticLayout(str17, Theme.dialogs_countTextPaint, this.mentionWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                } else {
                    this.mentionWidth = AndroidUtilities.dp(12.0f);
                }
                int dp19 = this.mentionWidth + AndroidUtilities.dp(18.0f);
                i192 -= dp19;
                if (!LocaleController.isRTL) {
                    int measuredWidth5 = (getMeasuredWidth() - this.mentionWidth) - AndroidUtilities.dp(20.0f);
                    int i28 = this.countWidth;
                    this.mentionLeft = measuredWidth5 - (i28 != 0 ? i28 + AndroidUtilities.dp(18.0f) : 0);
                } else {
                    int dp20 = AndroidUtilities.dp(20.0f);
                    int i29 = this.countWidth;
                    this.mentionLeft = dp20 + (i29 != 0 ? i29 + AndroidUtilities.dp(18.0f) : 0);
                    this.messageLeft += dp19;
                    this.typingLeft += dp19;
                    this.messageNameLeft += dp19;
                }
                this.drawMention = true;
            } else {
                this.mentionWidth = 0;
            }
            if (this.drawReactionMention) {
                int dp21 = AndroidUtilities.dp(24.0f);
                i192 -= dp21;
                if (!LocaleController.isRTL) {
                    int measuredWidth6 = getMeasuredWidth() - AndroidUtilities.dp(32.0f);
                    this.reactionMentionLeft = measuredWidth6;
                    if (this.drawMention) {
                        int i30 = this.mentionWidth;
                        this.reactionMentionLeft = measuredWidth6 - (i30 != 0 ? i30 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i31 = this.reactionMentionLeft;
                        int i32 = this.countWidth;
                        this.reactionMentionLeft = i31 - (i32 != 0 ? i32 + AndroidUtilities.dp(18.0f) : 0);
                    }
                } else {
                    int dp22 = AndroidUtilities.dp(20.0f);
                    this.reactionMentionLeft = dp22;
                    if (this.drawMention) {
                        int i33 = this.mentionWidth;
                        this.reactionMentionLeft = dp22 + (i33 != 0 ? i33 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i34 = this.reactionMentionLeft;
                        int i35 = this.countWidth;
                        this.reactionMentionLeft = i34 + (i35 != 0 ? i35 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    this.messageLeft += dp21;
                    this.typingLeft += dp21;
                    this.messageNameLeft += dp21;
                }
            }
        } else {
            if (getIsPinned()) {
                int intrinsicWidth4 = Theme.dialogs_pinnedDrawable.getIntrinsicWidth() + AndroidUtilities.dp(8.0f);
                i192 -= intrinsicWidth4;
                if (LocaleController.isRTL) {
                    this.messageLeft += intrinsicWidth4;
                    this.typingLeft += intrinsicWidth4;
                    this.messageNameLeft += intrinsicWidth4;
                }
            }
            this.drawCount = false;
            this.drawMention = false;
        }
        if (z7) {
            if (charSequence3 == null) {
                charSequence3 = "";
            }
            if (charSequence3.length() > 150) {
                charSequence3 = charSequence3.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
            }
            if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || str20 != null) {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceNewLines(charSequence3);
            } else {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceTwoNewLinesToOne(charSequence3);
            }
            CharSequence replaceEmoji3 = Emoji.replaceEmoji(replaceTwoNewLinesToOne, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
            MessageObject messageObject14 = this.message;
            if (messageObject14 == null || (charSequence3 = AndroidUtilities.highlightText(replaceEmoji3, messageObject14.highlightedWords, this.resourcesProvider)) == null) {
                charSequence3 = replaceEmoji3;
            }
        }
        max = Math.max(AndroidUtilities.dp(12.0f), i192);
        this.buttonTop = AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 58.0f : 62.0f);
        if (!isForumCell()) {
            if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                this.messageTop = AndroidUtilities.dp(34.0f);
            } else {
                this.messageTop = AndroidUtilities.dp(39.0f);
            }
            int i36 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr3 = this.thumbImage;
                if (i36 >= imageReceiverArr3.length) {
                    break;
                }
                imageReceiverArr3[i36].setImageY(this.buttonTop);
                i36++;
            }
        } else {
            boolean z14 = this.useForceThreeLines;
            if ((z14 || SharedConfig.useThreeLinesLayout) && str20 != null && (this.currentDialogFolderId == 0 || this.currentDialogFolderDialogsCount == 1)) {
                try {
                    MessageObject messageObject15 = this.message;
                    str20 = str20;
                    if (messageObject15 != null) {
                        str20 = str20;
                        if (messageObject15.hasHighlightedWords()) {
                            ?? highlightText4 = AndroidUtilities.highlightText(str20, this.message.highlightedWords, this.resourcesProvider);
                            str20 = str20;
                            if (highlightText4 != null) {
                                str20 = highlightText4;
                            }
                        }
                    }
                    this.messageNameLayout = StaticLayoutEx.createStaticLayout(str20, Theme.dialogs_messageNamePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max, 1);
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
                this.messageTop = AndroidUtilities.dp(51.0f);
                int dp23 = (!this.nameIsEllipsized || !this.isTopic) ? 0 : AndroidUtilities.dp(20.0f);
                int i37 = 0;
                while (true) {
                    ImageReceiver[] imageReceiverArr4 = this.thumbImage;
                    if (i37 >= imageReceiverArr4.length) {
                        break;
                    }
                    imageReceiverArr4[i37].setImageY(i182 + dp23 + AndroidUtilities.dp(40.0f));
                    i37++;
                }
            } else {
                this.messageNameLayout = null;
                if (z14 || SharedConfig.useThreeLinesLayout) {
                    this.messageTop = AndroidUtilities.dp(32.0f);
                    int dp24 = (!this.nameIsEllipsized || !this.isTopic) ? 0 : AndroidUtilities.dp(20.0f);
                    int i38 = 0;
                    while (true) {
                        ImageReceiver[] imageReceiverArr5 = this.thumbImage;
                        if (i38 >= imageReceiverArr5.length) {
                            break;
                        }
                        imageReceiverArr5[i38].setImageY(i182 + dp24 + AndroidUtilities.dp(21.0f));
                        i38++;
                    }
                } else {
                    this.messageTop = AndroidUtilities.dp(39.0f);
                }
            }
        }
        str21 = str20;
        if (this.twoLinesForName) {
            this.messageTop += AndroidUtilities.dp(20.0f);
        }
        this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack2, this.messageNameLayout);
        try {
            this.buttonCreated = false;
            if (TextUtils.isEmpty(str18)) {
                this.buttonLayout = new StaticLayout(TextUtils.ellipsize(Emoji.replaceEmoji(str18, this.currentMessagePaint.getFontMetricsInt(), AndroidUtilities.dp(17.0f), false), this.currentMessagePaint, max - AndroidUtilities.dp(26.0f), TextUtils.TruncateAt.END), this.currentMessagePaint, max - AndroidUtilities.dp(20.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.spoilersPool2.addAll(this.spoilers2);
                this.spoilers2.clear();
                SpoilerEffect.addSpoilers(this, this.buttonLayout, this.spoilersPool2, this.spoilers2);
            } else {
                this.buttonLayout = null;
            }
        } catch (Exception unused) {
        }
        this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack3, this.buttonLayout);
        try {
            z10 = this.useForceThreeLines;
            if ((!z10 || SharedConfig.useThreeLinesLayout) && this.currentDialogFolderId != 0 && this.currentDialogFolderDialogsCount > 1) {
                this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                charSequence3 = str21;
                str21 = null;
            } else if ((!z10 && !SharedConfig.useThreeLinesLayout) || str21 != null) {
                if (!isForumCell() && (charSequence3 instanceof Spanned) && ((FixedWidthSpan[]) ((Spanned) charSequence3).getSpans(0, charSequence3.length(), FixedWidthSpan.class)).length <= 0) {
                    charSequence3 = TextUtils.ellipsize(charSequence3, this.currentMessagePaint, max - AndroidUtilities.dp((((this.thumbsCount * (this.thumbSize + 2)) - 2) + 12) + 5), TextUtils.TruncateAt.END);
                } else {
                    charSequence3 = TextUtils.ellipsize(charSequence3, this.currentMessagePaint, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END);
                }
            }
            if (charSequence3 instanceof Spannable) {
                Spannable spannable = (Spannable) charSequence3;
                for (CharacterStyle characterStyle : (CharacterStyle[]) spannable.getSpans(0, spannable.length(), CharacterStyle.class)) {
                    if ((characterStyle instanceof ClickableSpan) || ((characterStyle instanceof StyleSpan) && ((StyleSpan) characterStyle).getStyle() == 1)) {
                        spannable.removeSpan(characterStyle);
                    }
                }
            }
        } catch (Exception e5) {
            this.messageLayout = null;
            FileLog.e(e5);
        }
        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
            if (this.thumbsCount > 0) {
                max += AndroidUtilities.dp(((i11 * (this.thumbSize + 2)) - 2) + 5);
                if (LocaleController.isRTL) {
                    this.messageLeft -= AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 5);
                }
            }
            this.messageLayout = new StaticLayout(charSequence3, this.currentMessagePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.spoilersPool.addAll(this.spoilers);
            this.spoilers.clear();
            SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
            if (!TextUtils.isEmpty(spannableStringBuilder2)) {
                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                    this.typingLayout = new StaticLayout(spannableStringBuilder2, Theme.dialogs_messagePrintingPaint[this.paintIndex], max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                }
                i9 = i3;
                try {
                    this.typingLayout = StaticLayoutEx.createStaticLayout(spannableStringBuilder2, Theme.dialogs_messagePrintingPaint[this.paintIndex], max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, 1);
                } catch (Exception e6) {
                    e = e6;
                    FileLog.e(e);
                    this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                    if (!LocaleController.isRTL) {
                    }
                    staticLayout = this.typingLayout;
                    if (staticLayout != null) {
                    }
                    updateThumbsPosition();
                }
                this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                if (!LocaleController.isRTL) {
                    StaticLayout staticLayout2 = this.nameLayout;
                    if (staticLayout2 != null && staticLayout2.getLineCount() > 0) {
                        float lineLeft = this.nameLayout.getLineLeft(0);
                        double ceil = Math.ceil(this.nameLayout.getLineWidth(0));
                        int dp25 = this.nameWidth + AndroidUtilities.dp(12.0f);
                        this.nameWidth = dp25;
                        if (this.nameLayoutEllipsizeByGradient) {
                            ceil = Math.min(dp25, ceil);
                        }
                        if ((this.dialogMuted || this.drawUnmute) && !this.drawVerified && this.drawScam == 0) {
                            double d = this.nameLeft;
                            double d2 = this.nameWidth;
                            Double.isNaN(d2);
                            Double.isNaN(d);
                            double d3 = d + (d2 - ceil);
                            double dp26 = AndroidUtilities.dp(6.0f);
                            Double.isNaN(dp26);
                            double d4 = d3 - dp26;
                            double intrinsicWidth5 = Theme.dialogs_muteDrawable.getIntrinsicWidth();
                            Double.isNaN(intrinsicWidth5);
                            this.nameMuteLeft = (int) (d4 - intrinsicWidth5);
                        } else if (this.drawVerified) {
                            double d5 = this.nameLeft;
                            double d6 = this.nameWidth;
                            Double.isNaN(d6);
                            Double.isNaN(d5);
                            double d7 = d5 + (d6 - ceil);
                            double dp27 = AndroidUtilities.dp(6.0f);
                            Double.isNaN(dp27);
                            double d8 = d7 - dp27;
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
                            double dp28 = AndroidUtilities.dp(24.0f);
                            Double.isNaN(dp28);
                            this.nameMuteLeft = (int) (d12 - dp28);
                        } else if (this.drawScam != 0) {
                            double d13 = this.nameLeft;
                            double d14 = this.nameWidth;
                            Double.isNaN(d14);
                            Double.isNaN(d13);
                            double d15 = d13 + (d14 - ceil);
                            double dp29 = AndroidUtilities.dp(6.0f);
                            Double.isNaN(dp29);
                            double d16 = d15 - dp29;
                            double intrinsicWidth7 = (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).getIntrinsicWidth();
                            Double.isNaN(intrinsicWidth7);
                            this.nameMuteLeft = (int) (d16 - intrinsicWidth7);
                        }
                        if (lineLeft == 0.0f) {
                            int i39 = this.nameWidth;
                            if (ceil < i39) {
                                double d17 = this.nameLeft;
                                double d18 = i39;
                                Double.isNaN(d18);
                                Double.isNaN(d17);
                                this.nameLeft = (int) (d17 + (d18 - ceil));
                            }
                        }
                    }
                    StaticLayout staticLayout3 = this.messageLayout;
                    if (staticLayout3 != null && (lineCount4 = staticLayout3.getLineCount()) > 0) {
                        int i40 = 0;
                        int i41 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                        while (true) {
                            if (i40 >= lineCount4) {
                                break;
                            }
                            if (this.messageLayout.getLineLeft(i40) != 0.0f) {
                                i41 = 0;
                                break;
                            }
                            double ceil2 = Math.ceil(this.messageLayout.getLineWidth(i40));
                            double d19 = max;
                            Double.isNaN(d19);
                            i41 = Math.min(i41, (int) (d19 - ceil2));
                            i40++;
                        }
                        if (i41 != Integer.MAX_VALUE) {
                            this.messageLeft += i41;
                        }
                    }
                    StaticLayout staticLayout4 = this.typingLayout;
                    if (staticLayout4 != null && (lineCount3 = staticLayout4.getLineCount()) > 0) {
                        int i42 = 0;
                        int i43 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                        while (true) {
                            if (i42 >= lineCount3) {
                                break;
                            }
                            if (this.typingLayout.getLineLeft(i42) != 0.0f) {
                                i43 = 0;
                                break;
                            }
                            double ceil3 = Math.ceil(this.typingLayout.getLineWidth(i42));
                            double d20 = max;
                            Double.isNaN(d20);
                            i43 = Math.min(i43, (int) (d20 - ceil3));
                            i42++;
                        }
                        if (i43 != Integer.MAX_VALUE) {
                            this.typingLeft += i43;
                        }
                    }
                    StaticLayout staticLayout5 = this.messageNameLayout;
                    if (staticLayout5 != null && staticLayout5.getLineCount() > 0 && this.messageNameLayout.getLineLeft(0) == 0.0f) {
                        double ceil4 = Math.ceil(this.messageNameLayout.getLineWidth(0));
                        double d21 = max;
                        if (ceil4 < d21) {
                            double d22 = this.messageNameLeft;
                            Double.isNaN(d21);
                            Double.isNaN(d22);
                            this.messageNameLeft = (int) (d22 + (d21 - ceil4));
                        }
                    }
                } else {
                    StaticLayout staticLayout6 = this.nameLayout;
                    if (staticLayout6 != null && staticLayout6.getLineCount() > 0) {
                        float lineRight = this.nameLayout.getLineRight(0);
                        if (this.nameLayoutEllipsizeByGradient) {
                            lineRight = Math.min(this.nameWidth, lineRight);
                        }
                        if (lineRight == this.nameWidth) {
                            double ceil5 = Math.ceil(this.nameLayout.getLineWidth(0));
                            if (this.nameLayoutEllipsizeByGradient) {
                                ceil5 = Math.min(this.nameWidth, ceil5);
                            }
                            int i44 = this.nameWidth;
                            if (ceil5 < i44) {
                                double d23 = this.nameLeft;
                                double d24 = i44;
                                Double.isNaN(d24);
                                Double.isNaN(d23);
                                this.nameLeft = (int) (d23 - (d24 - ceil5));
                            }
                        }
                        if (this.dialogMuted || this.drawUnmute || this.drawVerified || this.drawPremium || this.drawScam != 0) {
                            this.nameMuteLeft = (int) (this.nameLeft + lineRight + AndroidUtilities.dp(6.0f));
                        }
                    }
                    StaticLayout staticLayout7 = this.messageLayout;
                    if (staticLayout7 != null && (lineCount2 = staticLayout7.getLineCount()) > 0) {
                        float f3 = 2.14748365E9f;
                        for (int i45 = 0; i45 < lineCount2; i45++) {
                            f3 = Math.min(f3, this.messageLayout.getLineLeft(i45));
                        }
                        this.messageLeft = (int) (this.messageLeft - f3);
                    }
                    StaticLayout staticLayout8 = this.typingLayout;
                    if (staticLayout8 != null && (lineCount = staticLayout8.getLineCount()) > 0) {
                        float f4 = 2.14748365E9f;
                        for (int i46 = 0; i46 < lineCount; i46++) {
                            f4 = Math.min(f4, this.typingLayout.getLineLeft(i46));
                        }
                        this.typingLeft = (int) (this.typingLeft - f4);
                    }
                    StaticLayout staticLayout9 = this.messageNameLayout;
                    if (staticLayout9 != null && staticLayout9.getLineCount() > 0) {
                        this.messageNameLeft = (int) (this.messageNameLeft - this.messageNameLayout.getLineLeft(0));
                    }
                }
                staticLayout = this.typingLayout;
                if (staticLayout != null && this.printingStringType >= 0 && staticLayout.getText().length() > 0) {
                    if (i9 < 0 && (i10 = i9 + 1) < this.typingLayout.getText().length()) {
                        primaryHorizontal = this.typingLayout.getPrimaryHorizontal(i9);
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
            i9 = i3;
            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
            if (!LocaleController.isRTL) {
            }
            staticLayout = this.typingLayout;
            if (staticLayout != null) {
                if (i9 < 0) {
                }
                primaryHorizontal = this.typingLayout.getPrimaryHorizontal(0);
                primaryHorizontal2 = this.typingLayout.getPrimaryHorizontal(1);
                if (primaryHorizontal >= primaryHorizontal2) {
                }
            }
            updateThumbsPosition();
        }
        if (this.thumbsCount > 0 && str21 != null) {
            max += AndroidUtilities.dp(5.0f);
        }
        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence3, this.currentMessagePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, str21 == null ? 1 : 2);
        this.spoilersPool.addAll(this.spoilers);
        this.spoilers.clear();
        SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
        if (!TextUtils.isEmpty(spannableStringBuilder2)) {
        }
        i9 = i3;
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
        if (!LocaleController.isRTL) {
        }
        staticLayout = this.typingLayout;
        if (staticLayout != null) {
        }
        updateThumbsPosition();
    }

    private void updateThumbsPosition() {
        if (this.thumbsCount > 0) {
            StaticLayout staticLayout = isForumCell() ? this.buttonLayout : this.messageLayout;
            if (staticLayout == null) {
                return;
            }
            try {
                CharSequence text = staticLayout.getText();
                if (!(text instanceof Spanned)) {
                    return;
                }
                FixedWidthSpan[] fixedWidthSpanArr = (FixedWidthSpan[]) ((Spanned) text).getSpans(0, text.length(), FixedWidthSpan.class);
                if (fixedWidthSpanArr == null || fixedWidthSpanArr.length <= 0) {
                    return;
                }
                int spanStart = ((Spanned) text).getSpanStart(fixedWidthSpanArr[0]);
                if (spanStart < 0) {
                    spanStart = 0;
                }
                int ceil = (int) Math.ceil(Math.min(staticLayout.getPrimaryHorizontal(spanStart), staticLayout.getPrimaryHorizontal(spanStart + 1)));
                if (ceil != 0) {
                    ceil += AndroidUtilities.dp(3.0f);
                }
                for (int i = 0; i < this.thumbsCount; i++) {
                    this.thumbImage[i].setImageX(this.messageLeft + ceil + AndroidUtilities.dp((this.thumbSize + 2) * i));
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
        int i;
        boolean z;
        int i2;
        this.topMessageTopicStartIndex = 0;
        this.topMessageTopicEndIndex = 0;
        if (this.chat != null) {
            ArrayList<TLRPC$TL_forumTopic> topics = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopics(this.chat.id);
            if (topics != null && !topics.isEmpty()) {
                ArrayList arrayList = new ArrayList(topics);
                Collections.sort(arrayList, Comparator$-CC.comparingInt(DialogCell$$ExternalSyntheticLambda6.INSTANCE));
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                MessageObject messageObject = this.message;
                boolean z2 = true;
                if (messageObject != null) {
                    i = MessageObject.getTopicId(messageObject.messageOwner, true);
                    TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(this.chat.id, i);
                    if (findTopic != null) {
                        CharSequence topicSpannedName = ForumUtilities.getTopicSpannedName(findTopic, this.currentMessagePaint);
                        spannableStringBuilder.append(topicSpannedName);
                        i2 = findTopic.unread_count > 0 ? topicSpannedName.length() : 0;
                        this.topMessageTopicStartIndex = 0;
                        this.topMessageTopicEndIndex = topicSpannedName.length();
                        if (this.message.isOutOwner()) {
                            this.lastTopicMessageUnread = findTopic.read_inbox_max_id < this.message.getId();
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
                    i = 0;
                    z = false;
                    i2 = 0;
                }
                for (int i3 = 0; i3 < Math.min(5, arrayList.size()); i3++) {
                    if (((TLRPC$TL_forumTopic) arrayList.get(i3)).id != i) {
                        if (spannableStringBuilder.length() != 0) {
                            if (z2 && z) {
                                spannableStringBuilder.append((CharSequence) " ");
                            } else {
                                spannableStringBuilder.append((CharSequence) ", ");
                            }
                        }
                        spannableStringBuilder.append(ForumUtilities.getTopicSpannedName((TLRPC$ForumTopic) arrayList.get(i3), this.currentMessagePaint));
                        z2 = false;
                    }
                }
                if (i2 > 0) {
                    spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM), 0, "chats_name", null), 0, Math.min(spannableStringBuilder.length(), i2 + 2), 0);
                }
                return spannableStringBuilder;
            } else if (MessagesController.getInstance(this.currentAccount).getTopicsController().endIsReached(this.chat.id)) {
                return "no created topics";
            } else {
                MessagesController.getInstance(this.currentAccount).getTopicsController().preloadTopics(this.chat.id);
                return "Loading...";
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$formatTopicsNames$0(TLRPC$TL_forumTopic tLRPC$TL_forumTopic) {
        return -tLRPC$TL_forumTopic.top_message;
    }

    public boolean isForumCell() {
        TLRPC$Chat tLRPC$Chat;
        return !isDialogFolder() && (tLRPC$Chat = this.chat) != null && tLRPC$Chat.forum && !this.isTopic;
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
            } else if (!z3) {
            } else {
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
                if (f == 1.0f) {
                    return;
                }
                canvas.restore();
                Theme.dialogs_checkDrawable.setAlpha(255);
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
                CheckBox2 checkBox22 = new CheckBox2(getContext(), 21, this.resourcesProvider) { // from class: org.telegram.ui.Cells.DialogCell.1
                    @Override // android.view.View
                    public void invalidate() {
                        super.invalidate();
                        DialogCell.this.invalidate();
                    }
                };
                this.checkBox = checkBox22;
                checkBox22.setColor(null, "windowBackgroundWhite", "checkboxCheck");
                this.checkBox.setDrawUnchecked(false);
                this.checkBox.setDrawBackgroundAsArc(3);
                addView(this.checkBox);
            }
            this.checkBox.setChecked(z, z2);
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

    /* JADX WARN: Removed duplicated region for block: B:154:0x02ba  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x02f1  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x03a5  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x03aa  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x03ae  */
    /* JADX WARN: Type inference failed for: r5v106 */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v3, types: [org.telegram.tgnet.TLRPC$Chat, org.telegram.tgnet.TLRPC$User, org.telegram.tgnet.TLRPC$EncryptedChat] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean update(int i, boolean z) {
        ?? r5;
        long j;
        MessageObject messageObject;
        TLRPC$Chat chat;
        boolean z2;
        MessageObject messageObject2;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        TLRPC$Chat chat2;
        MessageObject messageObject3;
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
        } else {
            int i9 = this.unreadCount;
            boolean z3 = this.reactionMentionCount != 0;
            boolean z4 = this.markUnread;
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
                            boolean z5 = this.isTopic;
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
                if (this.user != null && (i & MessagesController.UPDATE_MASK_STATUS) != 0) {
                    this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id));
                    invalidate();
                }
                if (this.user != null && (i & MessagesController.UPDATE_MASK_EMOJI_STATUS) != 0) {
                    TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id));
                    this.user = user;
                    TLRPC$EmojiStatus tLRPC$EmojiStatus = user.emoji_status;
                    if ((tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusUntil) && ((TLRPC$TL_emojiStatusUntil) tLRPC$EmojiStatus).until > ((int) (System.currentTimeMillis() / 1000))) {
                        this.nameLayoutEllipsizeByGradient = true;
                        this.emojiStatus.set(((TLRPC$TL_emojiStatusUntil) this.user.emoji_status).document_id, z);
                    } else {
                        TLRPC$EmojiStatus tLRPC$EmojiStatus2 = this.user.emoji_status;
                        if (tLRPC$EmojiStatus2 instanceof TLRPC$TL_emojiStatus) {
                            this.nameLayoutEllipsizeByGradient = true;
                            this.emojiStatus.set(((TLRPC$TL_emojiStatus) tLRPC$EmojiStatus2).document_id, z);
                        } else {
                            this.nameLayoutEllipsizeByGradient = true;
                            this.emojiStatus.set(PremiumGradient.getInstance().premiumStarDrawableMini, z);
                        }
                    }
                    invalidate();
                }
                if ((this.isDialogCell || this.isTopic) && (i & MessagesController.UPDATE_MASK_USER_PRINT) != 0) {
                    CharSequence printingString = MessagesController.getInstance(this.currentAccount).getPrintingString(this.currentDialogId, getTopicId(), true);
                    CharSequence charSequence = this.lastPrintString;
                    if ((charSequence != null && printingString == null) || ((charSequence == null && printingString != null) || (charSequence != null && !charSequence.equals(printingString)))) {
                        z2 = true;
                        if (!z2 && (i & MessagesController.UPDATE_MASK_MESSAGE_TEXT) != 0 && (messageObject3 = this.message) != null && messageObject3.messageText != this.lastMessageString) {
                            z2 = true;
                        }
                        if (!z2 && (i & MessagesController.UPDATE_MASK_CHAT) != 0 && this.chat != null) {
                            chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.id));
                            if ((!chat2.call_active && chat2.call_not_empty) != this.hasCall) {
                                z2 = true;
                            }
                        }
                        if (!z2 && (i & MessagesController.UPDATE_MASK_AVATAR) != 0 && this.chat == null) {
                            z2 = true;
                        }
                        if (!z2 && (i & MessagesController.UPDATE_MASK_NAME) != 0 && this.chat == null) {
                            z2 = true;
                        }
                        if (!z2 && (i & MessagesController.UPDATE_MASK_CHAT_AVATAR) != 0 && this.user == null) {
                            z2 = true;
                        }
                        if (!z2 && (i & MessagesController.UPDATE_MASK_CHAT_NAME) != 0 && this.user == null) {
                            z2 = true;
                        }
                        if (!z2) {
                            MessageObject messageObject7 = this.message;
                            if (messageObject7 != null && this.lastUnreadState != messageObject7.isUnread()) {
                                this.lastUnreadState = this.message.isUnread();
                                z2 = true;
                            }
                            if (this.isDialogCell) {
                                TLRPC$Dialog tLRPC$Dialog2 = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                                TLRPC$Chat chat4 = tLRPC$Dialog2 == null ? null : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-tLRPC$Dialog2.id));
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
                                    z2 = true;
                                }
                            }
                        }
                        if (!z2 && (i & MessagesController.UPDATE_MASK_SEND_STATE) != 0 && (messageObject2 = this.message) != null) {
                            i2 = this.lastSendState;
                            i3 = messageObject2.messageOwner.send_state;
                            if (i2 != i3) {
                                this.lastSendState = i3;
                                z2 = true;
                            }
                        }
                        if (z2) {
                            invalidate();
                            return false;
                        }
                        r5 = 0;
                    }
                }
                z2 = false;
                if (!z2) {
                    z2 = true;
                }
                if (!z2) {
                    chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.id));
                    if ((!chat2.call_active && chat2.call_not_empty) != this.hasCall) {
                    }
                }
                if (!z2) {
                    z2 = true;
                }
                if (!z2) {
                    z2 = true;
                }
                if (!z2) {
                    z2 = true;
                }
                if (!z2) {
                    z2 = true;
                }
                if (!z2) {
                }
                if (!z2) {
                    i2 = this.lastSendState;
                    i3 = messageObject2.messageOwner.send_state;
                    if (i2 != i3) {
                    }
                }
                if (z2) {
                }
            } else {
                r5 = 0;
            }
            this.user = r5;
            this.chat = r5;
            this.encryptedChat = r5;
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
                TLRPC$User tLRPC$User = this.user;
                if (tLRPC$User != null) {
                    this.avatarDrawable.setInfo(tLRPC$User);
                    if (UserObject.isReplyUser(this.user)) {
                        this.avatarDrawable.setAvatarType(12);
                        this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
                    } else if (UserObject.isUserSelf(this.user) && !this.useMeForMyMessages) {
                        this.avatarDrawable.setAvatarType(1);
                        this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
                    } else {
                        this.avatarImage.setForUserOrChat(this.user, this.avatarDrawable, null, true);
                    }
                } else {
                    TLRPC$Chat tLRPC$Chat = this.chat;
                    if (tLRPC$Chat != null) {
                        this.avatarDrawable.setInfo(tLRPC$Chat);
                        this.avatarImage.setForUserOrChat(this.chat, this.avatarDrawable);
                    }
                }
            }
            if (z && ((i9 != this.unreadCount || z4 != this.markUnread) && (!this.isDialogCell || System.currentTimeMillis() - this.lastDialogChangedTime > 100))) {
                ValueAnimator valueAnimator = this.countAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.countAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        DialogCell.this.lambda$update$1(valueAnimator2);
                    }
                });
                this.countAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        DialogCell.this.countChangeProgress = 1.0f;
                        DialogCell.this.countOldLayout = null;
                        DialogCell.this.countAnimationStableLayout = null;
                        DialogCell.this.countAnimationInLayout = null;
                        DialogCell.this.invalidate();
                    }
                });
                if ((i9 == 0 || this.markUnread) && (this.markUnread || !z4)) {
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
            boolean z6 = this.reactionMentionCount != 0;
            if (z && z6 != z3) {
                ValueAnimator valueAnimator2 = this.reactionsMentionsAnimator;
                if (valueAnimator2 != null) {
                    valueAnimator2.cancel();
                }
                this.reactionsMentionsChangeProgress = 0.0f;
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.reactionsMentionsAnimator = ofFloat2;
                ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda2
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                        DialogCell.this.lambda$update$2(valueAnimator3);
                    }
                });
                this.reactionsMentionsAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.3
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        DialogCell.this.reactionsMentionsChangeProgress = 1.0f;
                        DialogCell.this.invalidate();
                    }
                });
                if (z6) {
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
        if (!this.isTopic && (getMeasuredWidth() != 0 || getMeasuredHeight() != 0)) {
            buildLayout();
        }
        if (!z) {
            this.dialogMutedProgress = (this.dialogMuted || this.drawUnmute) ? 1.0f : 0.0f;
            ValueAnimator valueAnimator3 = this.countAnimator;
            if (valueAnimator3 != null) {
                valueAnimator3.cancel();
            }
        }
        invalidate();
        return isForumCell() != isForumCell;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$1(ValueAnimator valueAnimator) {
        this.countChangeProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$2(ValueAnimator valueAnimator) {
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
        boolean z = false;
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
            boolean z2 = this.drawRevealBackground;
            if (Math.abs(f2) >= getMeasuredWidth() * 0.45f) {
                z = true;
            }
            this.drawRevealBackground = z;
            if (z2 != z && this.archiveHidden == SharedConfig.archiveHidden) {
                try {
                    performHapticFeedback(3, 2);
                } catch (Exception unused) {
                }
            }
        }
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x0b39, code lost:
        if (r2.lastKnownTypingType >= 0) goto L161;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x13e6, code lost:
        if (r1 > 0) goto L398;
     */
    /* JADX WARN: Removed duplicated region for block: B:104:0x08f8  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x09b7  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0bc4  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x0d74  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x0e3f  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x0e4b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:292:0x0ea5  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x0ebf  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x1030  */
    /* JADX WARN: Removed duplicated region for block: B:305:0x1054  */
    /* JADX WARN: Removed duplicated region for block: B:309:0x1255  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x1358  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x1369  */
    /* JADX WARN: Removed duplicated region for block: B:357:0x13ae  */
    /* JADX WARN: Removed duplicated region for block: B:381:0x13f4  */
    /* JADX WARN: Removed duplicated region for block: B:384:0x141a  */
    /* JADX WARN: Removed duplicated region for block: B:387:0x1459  */
    /* JADX WARN: Removed duplicated region for block: B:391:0x1465  */
    /* JADX WARN: Removed duplicated region for block: B:397:0x1429  */
    /* JADX WARN: Removed duplicated region for block: B:412:0x1860  */
    /* JADX WARN: Removed duplicated region for block: B:423:0x1890  */
    /* JADX WARN: Removed duplicated region for block: B:426:0x1894  */
    /* JADX WARN: Removed duplicated region for block: B:439:0x1901  */
    /* JADX WARN: Removed duplicated region for block: B:442:0x190a  */
    /* JADX WARN: Removed duplicated region for block: B:453:0x1949  */
    /* JADX WARN: Removed duplicated region for block: B:481:0x19d1  */
    /* JADX WARN: Removed duplicated region for block: B:492:0x1a53  */
    /* JADX WARN: Removed duplicated region for block: B:503:0x1aa9  */
    /* JADX WARN: Removed duplicated region for block: B:509:0x1ac1  */
    /* JADX WARN: Removed duplicated region for block: B:521:0x1b05  */
    /* JADX WARN: Removed duplicated region for block: B:523:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:524:0x1ae7  */
    /* JADX WARN: Removed duplicated region for block: B:532:0x1a7c  */
    /* JADX WARN: Removed duplicated region for block: B:541:0x1a26  */
    /* JADX WARN: Removed duplicated region for block: B:547:0x1a39  */
    /* JADX WARN: Removed duplicated region for block: B:557:0x186b  */
    /* JADX WARN: Removed duplicated region for block: B:639:0x181e  */
    /* JADX WARN: Removed duplicated region for block: B:644:0x1830  */
    /* JADX WARN: Removed duplicated region for block: B:651:0x1857  */
    /* JADX WARN: Removed duplicated region for block: B:652:0x1841  */
    /* JADX WARN: Removed duplicated region for block: B:689:0x10a7  */
    /* JADX WARN: Removed duplicated region for block: B:743:0x0f13  */
    /* JADX WARN: Removed duplicated region for block: B:747:0x0ea8  */
    /* JADX WARN: Removed duplicated region for block: B:759:0x0f29  */
    /* JADX WARN: Removed duplicated region for block: B:772:0x0f71  */
    /* JADX WARN: Removed duplicated region for block: B:825:0x0e36  */
    @Override // android.view.View
    @SuppressLint({"DrawAllocation"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        PullForegroundDrawable pullForegroundDrawable;
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
        String str2;
        float f2;
        int i4;
        float f3;
        Canvas canvas2;
        boolean z;
        String str3;
        boolean z2;
        boolean z3;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic;
        boolean z4;
        float dp;
        float dp2;
        float dp3;
        float dp4;
        float f4;
        float dp5;
        float dp6;
        float f5;
        float f6;
        float f7;
        float dp7;
        float imageX2;
        PullForegroundDrawable pullForegroundDrawable2;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic2;
        int i5;
        Paint paint2;
        Paint paint3;
        int i6;
        int i7;
        float f8;
        int i8;
        int i9;
        int i10;
        int i11;
        boolean z5;
        int dp8;
        int dp9;
        int i12;
        float dp10;
        float f9;
        int i13;
        float f10;
        float f11;
        int color3;
        float f12;
        CustomDialog customDialog;
        int i14;
        Paint paint4;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic3;
        if (this.currentDialogId == 0 && this.customDialog == null) {
            return;
        }
        if ((this.currentDialogFolderId != 0 || (this.isTopic && (tLRPC$TL_forumTopic3 = this.forumTopic) != null && tLRPC$TL_forumTopic3.id == 1)) && (pullForegroundDrawable = this.archivedChatsDrawable) != null && pullForegroundDrawable.outProgress == 0.0f && this.translationX == 0.0f) {
            if (this.drawingForBlur) {
                return;
            }
            canvas.save();
            canvas.translate(0.0f, -this.translateY);
            canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
            this.archivedChatsDrawable.draw(canvas);
            canvas.restore();
            return;
        }
        if (this.clipProgress != 0.0f && Build.VERSION.SDK_INT != 24) {
            canvas.save();
            canvas.clipRect(0.0f, this.topClip * this.clipProgress, getMeasuredWidth(), getMeasuredHeight() - ((int) (this.bottomClip * this.clipProgress)));
        }
        float f13 = 8.0f;
        if (this.translationX != 0.0f || this.cornerProgress != 0.0f) {
            canvas.save();
            canvas.translate(0.0f, -this.translateY);
            if (this.overrideSwipeAction) {
                color = Theme.getColor(this.overrideSwipeActionBackgroundColorKey, this.resourcesProvider);
                color2 = Theme.getColor(this.overrideSwipeActionRevealBackgroundColorKey, this.resourcesProvider);
                String str4 = this.overrideSwipeActionStringKey;
                i = this.overrideSwipeActionStringId;
                string = LocaleController.getString(str4, i);
                this.translationDrawable = this.overrideSwipeActionDrawable;
            } else if (this.currentDialogFolderId != 0) {
                if (this.archiveHidden) {
                    color = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
                    color2 = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                    i = R.string.UnhideFromTop;
                    string = LocaleController.getString("UnhideFromTop", i);
                    this.translationDrawable = Theme.dialogs_unpinArchiveDrawable;
                } else {
                    color = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                    color2 = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
                    i = R.string.HideOnTop;
                    string = LocaleController.getString("HideOnTop", i);
                    this.translationDrawable = Theme.dialogs_pinArchiveDrawable;
                }
            } else if (this.promoDialog) {
                color = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                color2 = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
                i = R.string.PsaHide;
                string = LocaleController.getString("PsaHide", i);
                this.translationDrawable = Theme.dialogs_hidePsaDrawable;
            } else if (this.folderId == 0) {
                color = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                color2 = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
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
                    color = Theme.getColor("dialogSwipeRemove", this.resourcesProvider);
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
                color = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
                color2 = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                i = R.string.Unarchive;
                string = LocaleController.getString("Unarchive", i);
                this.translationDrawable = Theme.dialogs_unarchiveDrawable;
            }
            int i15 = color2;
            String str5 = string;
            if (this.swipeCanceled && (rLottieDrawable = this.lastDrawTranslationDrawable) != null) {
                this.translationDrawable = rLottieDrawable;
                i = this.lastDrawSwipeMessageStringId;
            } else {
                this.lastDrawTranslationDrawable = this.translationDrawable;
                this.lastDrawSwipeMessageStringId = i;
            }
            if (!this.translationAnimationStarted && Math.abs(this.translationX) > AndroidUtilities.dp(43.0f)) {
                this.translationAnimationStarted = true;
                this.translationDrawable.setProgress(0.0f);
                this.translationDrawable.setCallback(this);
                this.translationDrawable.start();
            }
            float measuredWidth = this.translationX + getMeasuredWidth();
            if (this.currentRevealProgress < 1.0f) {
                Theme.dialogs_pinnedPaint.setColor(color);
                f = measuredWidth;
                i2 = i;
                str = str5;
                i3 = i15;
                canvas.drawRect(measuredWidth - AndroidUtilities.dp(8.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
                if (this.currentRevealProgress == 0.0f) {
                    if (Theme.dialogs_archiveDrawableRecolored) {
                        Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor("chats_archiveBackground"));
                        Theme.dialogs_archiveDrawableRecolored = false;
                    }
                    if (Theme.dialogs_hidePsaDrawableRecolored) {
                        Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 1.**", Theme.getNonAnimatedColor("chats_archiveBackground"));
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor("chats_archiveBackground"));
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor("chats_archiveBackground"));
                        Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                        Theme.dialogs_hidePsaDrawableRecolored = false;
                    }
                }
            } else {
                f = measuredWidth;
                i2 = i;
                str = str5;
                i3 = i15;
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
                    Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor("chats_archivePinBackground"));
                    Theme.dialogs_archiveDrawableRecolored = true;
                }
                if (!Theme.dialogs_hidePsaDrawableRecolored) {
                    Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 1.**", Theme.getNonAnimatedColor("chats_archivePinBackground"));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor("chats_archivePinBackground"));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor("chats_archivePinBackground"));
                    Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                    Theme.dialogs_hidePsaDrawableRecolored = true;
                }
            }
            canvas.save();
            canvas.translate(measuredWidth2, measuredHeight);
            float f14 = this.currentRevealBounceProgress;
            if (f14 != 0.0f && f14 != 1.0f) {
                float interpolation = this.interpolator.getInterpolation(f14) + 1.0f;
                canvas.scale(interpolation, interpolation, this.translationDrawable.getIntrinsicWidth() / 2, this.translationDrawable.getIntrinsicHeight() / 2);
            }
            BaseCell.setDrawableBounds((Drawable) this.translationDrawable, 0, 0);
            this.translationDrawable.draw(canvas);
            canvas.restore();
            canvas.clipRect(f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            String str6 = str;
            int ceil = (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str6));
            if (this.swipeMessageTextId != i2 || this.swipeMessageWidth != getMeasuredWidth()) {
                this.swipeMessageTextId = i2;
                this.swipeMessageWidth = getMeasuredWidth();
                StaticLayout staticLayout = new StaticLayout(str6, Theme.dialogs_archiveTextPaint, Math.min(AndroidUtilities.dp(80.0f), ceil), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                this.swipeMessageTextLayout = staticLayout;
                if (staticLayout.getLineCount() > 1) {
                    this.swipeMessageTextLayout = new StaticLayout(str6, Theme.dialogs_archiveTextPaintSmall, Math.min(AndroidUtilities.dp(82.0f), ceil), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
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
            RLottieDrawable rLottieDrawable2 = this.translationDrawable;
            if (rLottieDrawable2 != null) {
                rLottieDrawable2.stop();
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
        float dp11 = AndroidUtilities.dp(8.0f) * this.cornerProgress;
        if (this.isSelected) {
            this.rect.set(0.0f, 0.0f, getMeasuredWidth(), AndroidUtilities.lerp(getMeasuredHeight(), getCollapsedHeight(), this.rightFragmentOpenedProgress));
            this.rect.offset(0.0f, (-this.translateY) + this.collapseOffset);
            canvas.drawRoundRect(this.rect, dp11, dp11, Theme.dialogs_tabletSeletedPaint);
        }
        if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != 0.0f)) {
            Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor("chats_pinnedOverlay", this.resourcesProvider), this.archiveBackgroundProgress, 1.0f));
            Theme.dialogs_pinnedPaint.setAlpha((int) (paint4.getAlpha() * (1.0f - this.rightFragmentOpenedProgress)));
            canvas.drawRect(-this.xOffset, 0.0f, getMeasuredWidth(), getMeasuredHeight() - this.translateY, Theme.dialogs_pinnedPaint);
        } else if (getIsPinned() || this.drawPinBackground) {
            Theme.dialogs_pinnedPaint.setColor(Theme.getColor("chats_pinnedOverlay", this.resourcesProvider));
            Theme.dialogs_pinnedPaint.setAlpha((int) (paint.getAlpha() * (1.0f - this.rightFragmentOpenedProgress)));
            canvas.drawRect(-this.xOffset, 0.0f, getMeasuredWidth(), getMeasuredHeight() - this.translateY, Theme.dialogs_pinnedPaint);
        }
        this.updateHelper.updateAnimationValues();
        if (this.collapseOffset != 0.0f) {
            canvas.save();
            canvas.translate(0.0f, this.collapseOffset);
        }
        float f15 = this.rightFragmentOpenedProgress;
        float f16 = 10.0f;
        if (f15 != 1.0f) {
            if (f15 != 0.0f) {
                float clamp = Utilities.clamp(f15 / 0.4f, 1.0f, 0.0f);
                if (SharedConfig.getDevicePerformanceClass() >= 2) {
                    i14 = canvas.saveLayerAlpha(AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize() + 1) - (AndroidUtilities.dp(8.0f) * (1.0f - clamp)), 0.0f, getMeasuredWidth(), getMeasuredHeight(), (int) ((1.0f - this.rightFragmentOpenedProgress) * 255.0f), 31);
                } else {
                    int save = canvas.save();
                    canvas.clipRect(AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize() + 1) - (AndroidUtilities.dp(8.0f) * (1.0f - clamp)), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                    i14 = save;
                }
                canvas.translate((-(getMeasuredWidth() - AndroidUtilities.dp(74.0f))) * 0.7f * this.rightFragmentOpenedProgress, 0.0f);
                i5 = i14;
            } else {
                i5 = -1;
            }
            if (this.translationX != 0.0f || this.cornerProgress != 0.0f) {
                canvas.save();
                Theme.dialogs_pinnedPaint.setColor(Theme.getColor("windowBackgroundWhite", this.resourcesProvider));
                this.rect.set(getMeasuredWidth() - AndroidUtilities.dp(64.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                this.rect.offset(0.0f, -this.translateY);
                canvas.drawRoundRect(this.rect, dp11, dp11, Theme.dialogs_pinnedPaint);
                if (this.isSelected) {
                    canvas.drawRoundRect(this.rect, dp11, dp11, Theme.dialogs_tabletSeletedPaint);
                }
                if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != 0.0f)) {
                    Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor("chats_pinnedOverlay", this.resourcesProvider), this.archiveBackgroundProgress, 1.0f));
                    Theme.dialogs_pinnedPaint.setAlpha((int) (paint3.getAlpha() * (1.0f - this.rightFragmentOpenedProgress)));
                    canvas.drawRoundRect(this.rect, dp11, dp11, Theme.dialogs_pinnedPaint);
                } else if (getIsPinned() || this.drawPinBackground) {
                    Theme.dialogs_pinnedPaint.setColor(Theme.getColor("chats_pinnedOverlay", this.resourcesProvider));
                    Theme.dialogs_pinnedPaint.setAlpha((int) (paint2.getAlpha() * (1.0f - this.rightFragmentOpenedProgress)));
                    canvas.drawRoundRect(this.rect, dp11, dp11, Theme.dialogs_pinnedPaint);
                }
                canvas.restore();
            }
            if (this.translationX != 0.0f) {
                float f17 = this.cornerProgress;
                if (f17 < 1.0f) {
                    float f18 = f17 + 0.10666667f;
                    this.cornerProgress = f18;
                    if (f18 > 1.0f) {
                        this.cornerProgress = 1.0f;
                    }
                    z = true;
                }
                z = false;
            } else {
                float f19 = this.cornerProgress;
                if (f19 > 0.0f) {
                    float f20 = f19 - 0.10666667f;
                    this.cornerProgress = f20;
                    if (f20 < 0.0f) {
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
                    int i16 = this.nameLeft;
                    canvas.clipRect(i16, 0, this.nameWidth + i16, getMeasuredHeight());
                }
                if (this.currentDialogFolderId != 0) {
                    TextPaint[] textPaintArr = Theme.dialogs_namePaint;
                    int i17 = this.paintIndex;
                    TextPaint textPaint = textPaintArr[i17];
                    TextPaint textPaint2 = textPaintArr[i17];
                    int color4 = Theme.getColor("chats_nameArchived", this.resourcesProvider);
                    textPaint2.linkColor = color4;
                    textPaint.setColor(color4);
                } else if (this.encryptedChat != null || ((customDialog = this.customDialog) != null && customDialog.type == 2)) {
                    TextPaint[] textPaintArr2 = Theme.dialogs_namePaint;
                    int i18 = this.paintIndex;
                    TextPaint textPaint3 = textPaintArr2[i18];
                    TextPaint textPaint4 = textPaintArr2[i18];
                    int color5 = Theme.getColor("chats_secretName", this.resourcesProvider);
                    textPaint4.linkColor = color5;
                    textPaint3.setColor(color5);
                } else {
                    TextPaint[] textPaintArr3 = Theme.dialogs_namePaint;
                    int i19 = this.paintIndex;
                    TextPaint textPaint5 = textPaintArr3[i19];
                    TextPaint textPaint6 = textPaintArr3[i19];
                    int color6 = Theme.getColor("chats_name", this.resourcesProvider);
                    textPaint6.linkColor = color6;
                    textPaint5.setColor(color6);
                }
                canvas.save();
                canvas.translate(this.nameLeft + this.nameLayoutTranslateX, AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 10.0f : 13.0f));
                this.nameLayout.draw(canvas);
                i7 = 2;
                i6 = i5;
                str2 = "windowBackgroundWhite";
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.nameLayout, this.animatedEmojiStackName, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
                canvas.restore();
                if (this.nameLayoutEllipsizeByGradient && !this.nameLayoutFits) {
                    canvas.save();
                    if (this.nameLayoutEllipsizeLeft) {
                        canvas.translate(this.nameLeft, 0.0f);
                        canvas.drawRect(0.0f, 0.0f, AndroidUtilities.dp(24.0f), getMeasuredHeight(), this.fadePaint);
                    } else {
                        canvas.translate((this.nameLeft + this.nameWidth) - AndroidUtilities.dp(24.0f), 0.0f);
                        canvas.drawRect(0.0f, 0.0f, AndroidUtilities.dp(24.0f), getMeasuredHeight(), this.fadePaintBack);
                    }
                    canvas.restore();
                    canvas.restore();
                    if (this.timeLayout != null && this.currentDialogFolderId == 0) {
                        canvas.save();
                        canvas.translate(this.timeLeft, this.timeTop);
                        this.timeLayout.draw(canvas);
                        canvas.restore();
                    }
                    if (drawLock2()) {
                        Theme.dialogs_lock2Drawable.setBounds(this.lock2Left, this.timeTop + ((this.timeLayout.getHeight() - Theme.dialogs_lock2Drawable.getIntrinsicHeight()) / i7), this.lock2Left + Theme.dialogs_lock2Drawable.getIntrinsicWidth(), this.timeTop + ((this.timeLayout.getHeight() - Theme.dialogs_lock2Drawable.getIntrinsicHeight()) / i7) + Theme.dialogs_lock2Drawable.getIntrinsicHeight());
                        Theme.dialogs_lock2Drawable.draw(canvas);
                    }
                    if (this.messageNameLayout != null || isForumCell()) {
                        f8 = 0.0f;
                        i8 = 1;
                        i9 = 0;
                    } else {
                        if (this.currentDialogFolderId != 0) {
                            TextPaint textPaint7 = Theme.dialogs_messageNamePaint;
                            int color7 = Theme.getColor("chats_nameMessageArchived_threeLines", this.resourcesProvider);
                            textPaint7.linkColor = color7;
                            textPaint7.setColor(color7);
                        } else if (this.draftMessage != null) {
                            TextPaint textPaint8 = Theme.dialogs_messageNamePaint;
                            int color8 = Theme.getColor("chats_draft", this.resourcesProvider);
                            textPaint8.linkColor = color8;
                            textPaint8.setColor(color8);
                        } else {
                            TextPaint textPaint9 = Theme.dialogs_messageNamePaint;
                            int color9 = Theme.getColor("chats_nameMessage_threeLines", this.resourcesProvider);
                            textPaint9.linkColor = color9;
                            textPaint9.setColor(color9);
                        }
                        canvas.save();
                        canvas.translate(this.messageNameLeft, this.messageNameTop);
                        try {
                            this.messageNameLayout.draw(canvas);
                            i9 = 0;
                            i8 = 1;
                            f8 = 0.0f;
                        } catch (Exception e) {
                            e = e;
                            f8 = 0.0f;
                            i8 = 1;
                            i9 = 0;
                        }
                        try {
                            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageNameLayout, this.animatedEmojiStack2, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
                        } catch (Exception e2) {
                            e = e2;
                            FileLog.e(e);
                            canvas.restore();
                            if (this.messageLayout != null) {
                            }
                            i10 = 4;
                            if (this.buttonLayout != null) {
                            }
                            if (this.currentDialogFolderId != 0) {
                            }
                            if (!this.drawUnmute) {
                            }
                            if (this.dialogsType == i11) {
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
                            i12 = i6;
                            if (i12 != -1) {
                            }
                            if (this.animatingArchiveAvatar) {
                            }
                            if (this.drawAvatar) {
                                this.avatarImage.draw(canvas2);
                            }
                            if (this.animatingArchiveAvatar) {
                            }
                            if (this.isDialogCell) {
                            }
                            str3 = str2;
                            if (this.rightFragmentOpenedProgress > f2) {
                                if (!this.isTopic) {
                                }
                                drawCounter(canvas, z4, (int) ((this.avatarImage.getImageY() + this.avatarImage.getImageHeight()) - AndroidUtilities.dp(22.0f)), (int) (((this.avatarImage.getImageX() + this.avatarImage.getImageWidth()) - this.countWidth) - AndroidUtilities.dp(5.0f)), (int) (((this.avatarImage.getImageX() + this.avatarImage.getImageWidth()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
                            }
                            if (this.collapseOffset != f2) {
                            }
                            if (this.translationX != f2) {
                            }
                            if (this.currentDialogFolderId == 0) {
                            }
                            canvas.save();
                            canvas2.translate(f2, -this.translateY);
                            canvas2.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                            this.archivedChatsDrawable.draw(canvas2);
                            canvas.restore();
                            if (this.useSeparator) {
                            }
                            if (this.clipProgress != f2) {
                            }
                            z2 = this.drawReorder;
                            if (!z2) {
                            }
                            if (!z2) {
                            }
                            if (this.archiveHidden) {
                            }
                        }
                        canvas.restore();
                    }
                    if (this.messageLayout != null) {
                        if (this.currentDialogFolderId != 0) {
                            if (this.chat != null) {
                                TextPaint[] textPaintArr4 = Theme.dialogs_messagePaint;
                                int i20 = this.paintIndex;
                                TextPaint textPaint10 = textPaintArr4[i20];
                                TextPaint textPaint11 = textPaintArr4[i20];
                                int color10 = Theme.getColor("chats_nameMessageArchived", this.resourcesProvider);
                                textPaint11.linkColor = color10;
                                textPaint10.setColor(color10);
                            } else {
                                TextPaint[] textPaintArr5 = Theme.dialogs_messagePaint;
                                int i21 = this.paintIndex;
                                TextPaint textPaint12 = textPaintArr5[i21];
                                TextPaint textPaint13 = textPaintArr5[i21];
                                int color11 = Theme.getColor("chats_messageArchived", this.resourcesProvider);
                                textPaint13.linkColor = color11;
                                textPaint12.setColor(color11);
                            }
                        } else {
                            TextPaint[] textPaintArr6 = Theme.dialogs_messagePaint;
                            int i22 = this.paintIndex;
                            TextPaint textPaint14 = textPaintArr6[i22];
                            TextPaint textPaint15 = textPaintArr6[i22];
                            int color12 = Theme.getColor("chats_message", this.resourcesProvider);
                            textPaint15.linkColor = color12;
                            textPaint14.setColor(color12);
                        }
                        float dp12 = AndroidUtilities.dp(14.0f);
                        DialogUpdateHelper dialogUpdateHelper = this.updateHelper;
                        if (dialogUpdateHelper.typingOutToTop) {
                            f10 = this.messageTop - (dialogUpdateHelper.typingProgres * dp12);
                        } else {
                            f10 = this.messageTop + (dialogUpdateHelper.typingProgres * dp12);
                        }
                        if (dialogUpdateHelper.typingProgres != 1.0f) {
                            canvas.save();
                            canvas.translate(this.messageLeft, f10);
                            int alpha = this.messageLayout.getPaint().getAlpha();
                            this.messageLayout.getPaint().setAlpha((int) (alpha * (1.0f - this.updateHelper.typingProgres)));
                            if (!this.spoilers.isEmpty()) {
                                try {
                                    canvas.save();
                                    SpoilerEffect.clipOutCanvas(canvas, this.spoilers);
                                    this.messageLayout.draw(canvas);
                                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageLayout, this.animatedEmojiStack, -0.075f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f);
                                    canvas.restore();
                                    for (int i23 = 0; i23 < this.spoilers.size(); i23++) {
                                        SpoilerEffect spoilerEffect = this.spoilers.get(i23);
                                        spoilerEffect.setColor(this.messageLayout.getPaint().getColor());
                                        spoilerEffect.draw(canvas);
                                    }
                                } catch (Exception e3) {
                                    FileLog.e(e3);
                                }
                            } else {
                                this.messageLayout.draw(canvas);
                                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageLayout, this.animatedEmojiStack, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
                            }
                            this.messageLayout.getPaint().setAlpha(alpha);
                            canvas.restore();
                        }
                        canvas.save();
                        DialogUpdateHelper dialogUpdateHelper2 = this.updateHelper;
                        if (dialogUpdateHelper2.typingOutToTop) {
                            f11 = this.messageTop + ((1.0f - dialogUpdateHelper2.typingProgres) * dp12);
                        } else {
                            f11 = this.messageTop - ((1.0f - dialogUpdateHelper2.typingProgres) * dp12);
                        }
                        canvas.translate(this.typingLeft, f11);
                        StaticLayout staticLayout2 = this.typingLayout;
                        if (staticLayout2 != null && this.updateHelper.typingProgres > f8) {
                            int alpha2 = staticLayout2.getPaint().getAlpha();
                            this.typingLayout.getPaint().setAlpha((int) (alpha2 * this.updateHelper.typingProgres));
                            this.typingLayout.draw(canvas);
                            this.typingLayout.getPaint().setAlpha(alpha2);
                        }
                        canvas.restore();
                        if (this.typingLayout != null) {
                            int i24 = this.printingStringType;
                            if (i24 < 0) {
                                DialogUpdateHelper dialogUpdateHelper3 = this.updateHelper;
                                if (dialogUpdateHelper3.typingProgres > f8) {
                                }
                            }
                            if (i24 < 0) {
                                i24 = this.updateHelper.lastKnownTypingType;
                            }
                            StatusDrawable chatStatusDrawable = Theme.getChatStatusDrawable(i24);
                            if (chatStatusDrawable != null) {
                                canvas.save();
                                chatStatusDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor("chats_actionMessage"), (int) (Color.alpha(color3) * this.updateHelper.typingProgres)));
                                DialogUpdateHelper dialogUpdateHelper4 = this.updateHelper;
                                if (dialogUpdateHelper4.typingOutToTop) {
                                    f12 = this.messageTop + (dp12 * (1.0f - dialogUpdateHelper4.typingProgres));
                                } else {
                                    f12 = this.messageTop - (dp12 * (1.0f - dialogUpdateHelper4.typingProgres));
                                }
                                i10 = 4;
                                if (i24 == i8 || i24 == 4) {
                                    canvas.translate(this.statusDrawableLeft, f12 + (i24 == i8 ? AndroidUtilities.dp(1.0f) : 0));
                                } else {
                                    canvas.translate(this.statusDrawableLeft, f12 + ((AndroidUtilities.dp(18.0f) - chatStatusDrawable.getIntrinsicHeight()) / 2.0f));
                                }
                                chatStatusDrawable.draw(canvas);
                                invalidate();
                                canvas.restore();
                                if (this.buttonLayout != null) {
                                    canvas.save();
                                    if (this.buttonBackgroundPaint == null) {
                                        this.buttonBackgroundPaint = new Paint(i8);
                                    }
                                    if (this.canvasButton == null) {
                                        CanvasButton canvasButton = new CanvasButton(this);
                                        this.canvasButton = canvasButton;
                                        canvasButton.setDelegate(new Runnable() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda4
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                DialogCell.this.lambda$onDraw$3();
                                            }
                                        });
                                        this.canvasButton.setLongPress(new Runnable() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda3
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                DialogCell.this.lambda$onDraw$4();
                                            }
                                        });
                                    }
                                    if (this.lastTopicMessageUnread && this.topMessageTopicEndIndex != this.topMessageTopicStartIndex) {
                                        this.canvasButton.setColor(ColorUtils.setAlphaComponent(this.currentMessagePaint.getColor(), Theme.isCurrentThemeDark() ? 36 : 26));
                                        if (!this.buttonCreated) {
                                            this.canvasButton.rewind();
                                            int i25 = this.topMessageTopicEndIndex;
                                            if (i25 != this.topMessageTopicStartIndex && i25 > 0) {
                                                RectF rectF = AndroidUtilities.rectTmp;
                                                StaticLayout staticLayout3 = this.messageLayout;
                                                rectF.set(this.messageLeft + AndroidUtilities.dp(2.0f), this.messageTop, (this.messageLeft + staticLayout3.getPrimaryHorizontal(Math.min(staticLayout3.getText().length(), this.topMessageTopicEndIndex))) - AndroidUtilities.dp(3.0f), this.buttonTop - AndroidUtilities.dp(4.0f));
                                                rectF.inset(-AndroidUtilities.dp(8.0f), -AndroidUtilities.dp(4.0f));
                                                if (rectF.right > rectF.left) {
                                                    this.canvasButton.addRect(rectF);
                                                }
                                            }
                                            RectF rectF2 = AndroidUtilities.rectTmp;
                                            rectF2.set(this.messageLeft + AndroidUtilities.dp(2.0f), this.buttonTop + AndroidUtilities.dp(2.0f), this.messageLeft + this.buttonLayout.getLineWidth(i9) + AndroidUtilities.dp(12.0f), this.buttonTop + this.buttonLayout.getHeight());
                                            rectF2.inset(-AndroidUtilities.dp(8.0f), -AndroidUtilities.dp(3.0f));
                                            this.canvasButton.addRect(rectF2);
                                        }
                                        this.canvasButton.draw(canvas);
                                        Theme.dialogs_forum_arrowDrawable.setAlpha(125);
                                        Drawable drawable = Theme.dialogs_forum_arrowDrawable;
                                        RectF rectF3 = AndroidUtilities.rectTmp;
                                        BaseCell.setDrawableBounds(drawable, rectF3.right - AndroidUtilities.dp(18.0f), rectF3.top + ((rectF3.height() - Theme.dialogs_forum_arrowDrawable.getIntrinsicHeight()) / 2.0f));
                                        Theme.dialogs_forum_arrowDrawable.draw(canvas);
                                    }
                                    canvas.translate(this.messageLeft - this.buttonLayout.getLineLeft(i9), this.buttonTop);
                                    if (!this.spoilers2.isEmpty()) {
                                        try {
                                            canvas.save();
                                            SpoilerEffect.clipOutCanvas(canvas, this.spoilers2);
                                            this.buttonLayout.draw(canvas);
                                            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.buttonLayout, this.animatedEmojiStack3, -0.075f, this.spoilers2, 0.0f, 0.0f, 0.0f, 1.0f);
                                            canvas.restore();
                                            for (int i26 = 0; i26 < this.spoilers2.size(); i26++) {
                                                SpoilerEffect spoilerEffect2 = this.spoilers2.get(i26);
                                                spoilerEffect2.setColor(this.buttonLayout.getPaint().getColor());
                                                spoilerEffect2.draw(canvas);
                                            }
                                        } catch (Exception e4) {
                                            FileLog.e(e4);
                                        }
                                    } else {
                                        this.buttonLayout.draw(canvas);
                                        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.buttonLayout, this.animatedEmojiStack3, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
                                    }
                                    canvas.restore();
                                }
                                if (this.currentDialogFolderId != 0) {
                                    int i27 = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
                                    int i28 = this.lastStatusDrawableParams;
                                    if (i28 >= 0 && i28 != i27 && !this.statusDrawableAnimationInProgress) {
                                        createStatusDrawableAnimator(i28, i27);
                                    }
                                    boolean z6 = this.statusDrawableAnimationInProgress;
                                    if (z6) {
                                        i27 = this.animateToStatusDrawableParams;
                                    }
                                    boolean z7 = (i27 & 1) != 0;
                                    boolean z8 = (i27 & 2) != 0;
                                    boolean z9 = (i27 & i10) != 0;
                                    if (z6) {
                                        int i29 = this.animateFromStatusDrawableParams;
                                        boolean z10 = (i29 & 1) != 0;
                                        boolean z11 = (i29 & 2) != 0;
                                        boolean z12 = (i29 & i10) != 0;
                                        if (!z7 && !z10 && z12 && !z11 && z8 && z9) {
                                            f3 = 1.0f;
                                            drawCheckStatus(canvas, z7, z8, z9, true, this.statusDrawableProgress);
                                            f2 = 0.0f;
                                            i4 = 1;
                                            i11 = 2;
                                        } else {
                                            f3 = 1.0f;
                                            boolean z13 = z10;
                                            f2 = 0.0f;
                                            boolean z14 = z11;
                                            i4 = 1;
                                            boolean z15 = z12;
                                            i11 = 2;
                                            drawCheckStatus(canvas, z13, z14, z15, false, 1.0f - this.statusDrawableProgress);
                                            drawCheckStatus(canvas, z7, z8, z9, false, this.statusDrawableProgress);
                                        }
                                    } else {
                                        f2 = 0.0f;
                                        i4 = 1;
                                        i11 = 2;
                                        f3 = 1.0f;
                                        drawCheckStatus(canvas, z7, z8, z9, false, 1.0f);
                                    }
                                    this.lastStatusDrawableParams = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
                                } else {
                                    f2 = 0.0f;
                                    i4 = 1;
                                    i11 = 2;
                                    f3 = 1.0f;
                                }
                                boolean z16 = !this.drawUnmute || this.dialogMuted;
                                if (this.dialogsType == i11 && ((z16 || this.dialogMutedProgress > f2) && !this.drawVerified && this.drawScam == 0 && !this.drawPremium)) {
                                    if (z16) {
                                        float f21 = this.dialogMutedProgress;
                                        if (f21 != f3) {
                                            float f22 = f21 + 0.10666667f;
                                            this.dialogMutedProgress = f22;
                                            if (f22 > f3) {
                                                this.dialogMutedProgress = f3;
                                            } else {
                                                invalidate();
                                            }
                                            float dp13 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                                            float dp14 = AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
                                            BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp13, dp14);
                                            BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp13, dp14);
                                            if (this.dialogMutedProgress == f3) {
                                                canvas.save();
                                                float f23 = this.dialogMutedProgress;
                                                canvas.scale(f23, f23, Theme.dialogs_muteDrawable.getBounds().centerX(), Theme.dialogs_muteDrawable.getBounds().centerY());
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
                                    if (!z16) {
                                        float f24 = this.dialogMutedProgress;
                                        if (f24 != f2) {
                                            float f25 = f24 - 0.10666667f;
                                            this.dialogMutedProgress = f25;
                                            if (f25 < f2) {
                                                this.dialogMutedProgress = f2;
                                            } else {
                                                invalidate();
                                            }
                                        }
                                    }
                                    float dp132 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                                    float dp142 = AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
                                    BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp132, dp142);
                                    BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp132, dp142);
                                    if (this.dialogMutedProgress == f3) {
                                    }
                                } else if (!this.drawVerified) {
                                    BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft - AndroidUtilities.dp(f3), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
                                    BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft - AndroidUtilities.dp(f3), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
                                    Theme.dialogs_verifiedDrawable.draw(canvas);
                                    Theme.dialogs_verifiedCheckDrawable.draw(canvas);
                                } else if (this.drawPremium) {
                                    AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatus;
                                    if (swapAnimatedEmojiDrawable != null) {
                                        swapAnimatedEmojiDrawable.setBounds(this.nameMuteLeft - AndroidUtilities.dp(2.0f), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f) - AndroidUtilities.dp(4.0f), this.nameMuteLeft + AndroidUtilities.dp(20.0f), (AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f) - AndroidUtilities.dp(4.0f)) + AndroidUtilities.dp(22.0f));
                                        this.emojiStatus.setColor(Integer.valueOf(Theme.getColor("chats_verifiedBackground", this.resourcesProvider)));
                                        this.emojiStatus.draw(canvas);
                                    } else {
                                        Drawable drawable2 = PremiumGradient.getInstance().premiumStarDrawableMini;
                                        BaseCell.setDrawableBounds(drawable2, this.nameMuteLeft - AndroidUtilities.dp(f3), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f));
                                        drawable2.draw(canvas);
                                    }
                                } else {
                                    int i30 = this.drawScam;
                                    if (i30 != 0) {
                                        BaseCell.setDrawableBounds((Drawable) (i30 == i4 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable), this.nameMuteLeft, AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.0f : 15.0f));
                                        (this.drawScam == i4 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).draw(canvas);
                                    }
                                }
                                if (!this.drawReorder || this.reorderIconProgress != f2) {
                                    Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                                    BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                                    Theme.dialogs_reorderDrawable.draw(canvas);
                                }
                                if (!this.drawError) {
                                    Theme.dialogs_errorDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                    this.rect.set(this.errorLeft, this.errorTop, i13 + AndroidUtilities.dp(23.0f), this.errorTop + AndroidUtilities.dp(23.0f));
                                    RectF rectF4 = this.rect;
                                    float f26 = AndroidUtilities.density;
                                    canvas.drawRoundRect(rectF4, f26 * 11.5f, f26 * 11.5f, Theme.dialogs_errorPaint);
                                    BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
                                    Theme.dialogs_errorDrawable.draw(canvas);
                                } else if (((this.drawCount || this.drawMention) && this.drawCount2) || this.countChangeProgress != f3 || this.drawReactionMention || this.reactionsMentionsChangeProgress != f3) {
                                    if (this.isTopic) {
                                        z5 = this.topicMuted;
                                    } else {
                                        TLRPC$Chat tLRPC$Chat = this.chat;
                                        z5 = (tLRPC$Chat == null || !tLRPC$Chat.forum || this.forumTopic != null) ? this.dialogMuted : !this.hasUnmutedTopics;
                                    }
                                    canvas2 = canvas;
                                    drawCounter(canvas, z5, this.countTop, this.countLeft, this.countLeftOld, 1.0f, false);
                                    if (this.drawMention) {
                                        Theme.dialogs_countPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                        this.rect.set(this.mentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp9 + this.mentionWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                        Paint paint7 = (!z5 || this.folderId == 0) ? Theme.dialogs_countPaint : Theme.dialogs_countGrayPaint;
                                        RectF rectF5 = this.rect;
                                        float f27 = AndroidUtilities.density;
                                        canvas2.drawRoundRect(rectF5, f27 * 11.5f, f27 * 11.5f, paint7);
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
                                        this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp8 + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                        Paint paint8 = Theme.dialogs_reactionsCountPaint;
                                        canvas.save();
                                        float f28 = this.reactionsMentionsChangeProgress;
                                        if (f28 != f3) {
                                            if (!this.drawReactionMention) {
                                                f28 = f3 - f28;
                                            }
                                            canvas2.scale(f28, f28, this.rect.centerX(), this.rect.centerY());
                                        }
                                        RectF rectF6 = this.rect;
                                        float f29 = AndroidUtilities.density;
                                        canvas2.drawRoundRect(rectF6, f29 * 11.5f, f29 * 11.5f, paint8);
                                        Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                        BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                        Theme.dialogs_reactionsMentionDrawable.draw(canvas2);
                                        canvas.restore();
                                    }
                                    if (this.thumbsCount > 0) {
                                        float f30 = this.updateHelper.typingProgres;
                                        if (f30 != f3) {
                                            if (f30 > f2) {
                                                canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) ((f3 - f30) * 255.0f), 31);
                                                if (this.updateHelper.typingOutToTop) {
                                                    dp10 = -AndroidUtilities.dp(14.0f);
                                                    f9 = this.updateHelper.typingProgres;
                                                } else {
                                                    dp10 = AndroidUtilities.dp(14.0f);
                                                    f9 = this.updateHelper.typingProgres;
                                                }
                                                canvas2.translate(f2, dp10 * f9);
                                            }
                                            for (int i31 = 0; i31 < this.thumbsCount; i31++) {
                                                if (this.thumbBackgroundPaint == null) {
                                                    Paint paint9 = new Paint(i4);
                                                    this.thumbBackgroundPaint = paint9;
                                                    paint9.setShadowLayer(AndroidUtilities.dp(1.34f), f2, AndroidUtilities.dp(0.34f), 402653184);
                                                    this.thumbBackgroundPaint.setColor(0);
                                                }
                                                RectF rectF7 = AndroidUtilities.rectTmp;
                                                rectF7.set(this.thumbImage[i31].getImageX(), this.thumbImage[i31].getImageY(), this.thumbImage[i31].getImageX2(), this.thumbImage[i31].getImageY2());
                                                canvas2.drawRoundRect(rectF7, this.thumbImage[i31].getRoundRadius()[0], this.thumbImage[i31].getRoundRadius()[i4], this.thumbBackgroundPaint);
                                                this.thumbImage[i31].draw(canvas2);
                                                if (this.drawPlay[i31]) {
                                                    BaseCell.setDrawableBounds(Theme.dialogs_playDrawable, (int) (this.thumbImage[i31].getCenterX() - (Theme.dialogs_playDrawable.getIntrinsicWidth() / 2)), (int) (this.thumbImage[i31].getCenterY() - (Theme.dialogs_playDrawable.getIntrinsicHeight() / 2)));
                                                    Theme.dialogs_playDrawable.draw(canvas2);
                                                }
                                            }
                                            if (this.updateHelper.typingProgres > f2) {
                                                canvas.restore();
                                            }
                                        }
                                    }
                                    i12 = i6;
                                    if (i12 != -1) {
                                        canvas2.restoreToCount(i12);
                                    }
                                } else if (getIsPinned()) {
                                    Theme.dialogs_pinnedDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                    BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                                    Theme.dialogs_pinnedDrawable.draw(canvas);
                                }
                                canvas2 = canvas;
                                if (this.thumbsCount > 0) {
                                }
                                i12 = i6;
                                if (i12 != -1) {
                                }
                            }
                        }
                    }
                    i10 = 4;
                    if (this.buttonLayout != null) {
                    }
                    if (this.currentDialogFolderId != 0) {
                    }
                    if (!this.drawUnmute) {
                    }
                    if (this.dialogsType == i11) {
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
                    i12 = i6;
                    if (i12 != -1) {
                    }
                }
            } else {
                i6 = i5;
                str2 = "windowBackgroundWhite";
                i7 = 2;
            }
            if (this.timeLayout != null) {
                canvas.save();
                canvas.translate(this.timeLeft, this.timeTop);
                this.timeLayout.draw(canvas);
                canvas.restore();
            }
            if (drawLock2()) {
            }
            if (this.messageNameLayout != null) {
            }
            f8 = 0.0f;
            i8 = 1;
            i9 = 0;
            if (this.messageLayout != null) {
            }
            i10 = 4;
            if (this.buttonLayout != null) {
            }
            if (this.currentDialogFolderId != 0) {
            }
            if (!this.drawUnmute) {
            }
            if (this.dialogsType == i11) {
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
            i12 = i6;
            if (i12 != -1) {
            }
        } else {
            str2 = "windowBackgroundWhite";
            f2 = 0.0f;
            i4 = 1;
            f3 = 1.0f;
            canvas2 = canvas;
            z = false;
        }
        if (this.animatingArchiveAvatar) {
            canvas.save();
            float interpolation2 = this.interpolator.getInterpolation(this.animatingArchiveAvatarProgress / 170.0f) + f3;
            canvas2.scale(interpolation2, interpolation2, this.avatarImage.getCenterX(), this.avatarImage.getCenterY());
        }
        if (this.drawAvatar && ((this.currentDialogFolderId == 0 && (!this.isTopic || (tLRPC$TL_forumTopic2 = this.forumTopic) == null || tLRPC$TL_forumTopic2.id != i4)) || (pullForegroundDrawable2 = this.archivedChatsDrawable) == null || !pullForegroundDrawable2.isDraw())) {
            this.avatarImage.draw(canvas2);
        }
        if (this.animatingArchiveAvatar) {
            canvas.restore();
        }
        if (this.isDialogCell || this.currentDialogFolderId != 0) {
            str3 = str2;
        } else {
            boolean z17 = this.ttlPeriod > 0 && !isOnline() && !this.hasCall;
            this.showTtl = z17;
            if (this.rightFragmentOpenedProgress != f3 && (z17 || this.ttlProgress > f2)) {
                TimerDrawable timerDrawable = this.timerDrawable;
                if (timerDrawable != null) {
                    int time = timerDrawable.getTime();
                    int i32 = this.ttlPeriod;
                    if (time != i32) {
                    }
                    if (this.timerPaint == null) {
                        this.timerPaint = new Paint(i4);
                        Paint paint10 = new Paint(i4);
                        this.timerPaint2 = paint10;
                        paint10.setColor(838860800);
                    }
                    int imageY2 = (int) (this.avatarImage.getImageY2() - AndroidUtilities.dp(9.0f));
                    if (!LocaleController.isRTL) {
                        imageX2 = this.avatarImage.getImageX() + AndroidUtilities.dp(9.0f);
                    } else {
                        imageX2 = this.avatarImage.getImageX2() - AndroidUtilities.dp(9.0f);
                    }
                    int i33 = (int) imageX2;
                    this.timerDrawable.setBounds(0, 0, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(22.0f));
                    this.timerDrawable.setTime(this.ttlPeriod);
                    if (!this.avatarImage.updateThumbShaderMatrix()) {
                        BitmapShader bitmapShader = this.avatarImage.thumbShader;
                        if (bitmapShader != null) {
                            this.timerPaint.setShader(bitmapShader);
                        }
                    } else {
                        this.timerPaint.setShader(null);
                        if (this.avatarImage.getBitmap() != null && !this.avatarImage.getBitmap().isRecycled()) {
                            this.timerPaint.setColor(PremiumLockIconView.getDominantColor(this.avatarImage.getBitmap()));
                        } else {
                            this.timerPaint.setColor(this.avatarDrawable.getColor2());
                        }
                    }
                    canvas.save();
                    float f31 = this.ttlProgress * (f3 - this.rightFragmentOpenedProgress);
                    float f32 = i33;
                    float f33 = imageY2;
                    canvas2.scale(f31, f31, f32, f33);
                    canvas2.drawCircle(f32, f33, AndroidUtilities.dpf2(11.0f), this.timerPaint);
                    canvas2.drawCircle(f32, f33, AndroidUtilities.dpf2(11.0f), this.timerPaint2);
                    canvas.save();
                    canvas2.translate(f32 - AndroidUtilities.dpf2(11.0f), f33 - AndroidUtilities.dpf2(11.0f));
                    this.timerDrawable.draw(canvas2);
                    canvas.restore();
                    canvas.restore();
                }
                this.timerDrawable = TimerDrawable.getTtlIconForDialogs(this.ttlPeriod);
                if (this.timerPaint == null) {
                }
                int imageY22 = (int) (this.avatarImage.getImageY2() - AndroidUtilities.dp(9.0f));
                if (!LocaleController.isRTL) {
                }
                int i332 = (int) imageX2;
                this.timerDrawable.setBounds(0, 0, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(22.0f));
                this.timerDrawable.setTime(this.ttlPeriod);
                if (!this.avatarImage.updateThumbShaderMatrix()) {
                }
                canvas.save();
                float f312 = this.ttlProgress * (f3 - this.rightFragmentOpenedProgress);
                float f322 = i332;
                float f332 = imageY22;
                canvas2.scale(f312, f312, f322, f332);
                canvas2.drawCircle(f322, f332, AndroidUtilities.dpf2(11.0f), this.timerPaint);
                canvas2.drawCircle(f322, f332, AndroidUtilities.dpf2(11.0f), this.timerPaint2);
                canvas.save();
                canvas2.translate(f322 - AndroidUtilities.dpf2(11.0f), f332 - AndroidUtilities.dpf2(11.0f));
                this.timerDrawable.draw(canvas2);
                canvas.restore();
                canvas.restore();
            }
            TLRPC$User tLRPC$User = this.user;
            if (tLRPC$User != null && !MessagesController.isSupportUser(tLRPC$User) && !this.user.bot) {
                boolean isOnline = isOnline();
                if (isOnline || this.onlineProgress != f2) {
                    float imageY23 = this.avatarImage.getImageY2();
                    if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                        f13 = 6.0f;
                    }
                    int dp15 = (int) (imageY23 - AndroidUtilities.dp(f13));
                    if (LocaleController.isRTL) {
                        float imageX = this.avatarImage.getImageX();
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f16 = 6.0f;
                        }
                        dp7 = imageX + AndroidUtilities.dp(f16);
                    } else {
                        float imageX22 = this.avatarImage.getImageX2();
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f16 = 6.0f;
                        }
                        dp7 = imageX22 - AndroidUtilities.dp(f16);
                    }
                    str3 = str2;
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(str3, this.resourcesProvider));
                    float f34 = (int) dp7;
                    float f35 = dp15;
                    canvas2.drawCircle(f34, f35, AndroidUtilities.dp(7.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle", this.resourcesProvider));
                    canvas2.drawCircle(f34, f35, AndroidUtilities.dp(5.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    if (isOnline) {
                        float f36 = this.onlineProgress;
                        if (f36 < f3) {
                            float f37 = f36 + 0.10666667f;
                            this.onlineProgress = f37;
                            if (f37 > f3) {
                                this.onlineProgress = f3;
                            }
                            z = true;
                        }
                    } else {
                        float f38 = this.onlineProgress;
                        if (f38 > f2) {
                            float f39 = f38 - 0.10666667f;
                            this.onlineProgress = f39;
                            if (f39 < f2) {
                                this.onlineProgress = f2;
                            }
                            z = true;
                        }
                    }
                } else {
                    str3 = str2;
                }
                if (!this.showTtl) {
                }
            } else {
                str3 = str2;
                TLRPC$Chat tLRPC$Chat2 = this.chat;
                if (tLRPC$Chat2 != null) {
                    boolean z18 = tLRPC$Chat2.call_active && tLRPC$Chat2.call_not_empty;
                    this.hasCall = z18;
                    if ((z18 || this.chatCallProgress != f2) && this.rightFragmentOpenedProgress < f3) {
                        CheckBox2 checkBox2 = this.checkBox;
                        float progress = (checkBox2 == null || !checkBox2.isChecked()) ? 1.0f : f3 - this.checkBox.getProgress();
                        float imageY24 = this.avatarImage.getImageY2();
                        if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                            f13 = 6.0f;
                        }
                        int dp16 = (int) (imageY24 - AndroidUtilities.dp(f13));
                        if (LocaleController.isRTL) {
                            float imageX3 = this.avatarImage.getImageX();
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                f16 = 6.0f;
                            }
                            dp = imageX3 + AndroidUtilities.dp(f16);
                        } else {
                            float imageX23 = this.avatarImage.getImageX2();
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                f16 = 6.0f;
                            }
                            dp = imageX23 - AndroidUtilities.dp(f16);
                        }
                        int i34 = (int) dp;
                        if (this.rightFragmentOpenedProgress != f2) {
                            canvas.save();
                            float f40 = f3 - this.rightFragmentOpenedProgress;
                            canvas2.scale(f40, f40, i34, dp16);
                        }
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(str3, this.resourcesProvider));
                        float f41 = i34;
                        float f42 = dp16;
                        canvas2.drawCircle(f41, f42, AndroidUtilities.dp(11.0f) * this.chatCallProgress * progress, Theme.dialogs_onlineCirclePaint);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle", this.resourcesProvider));
                        canvas2.drawCircle(f41, f42, AndroidUtilities.dp(9.0f) * this.chatCallProgress * progress, Theme.dialogs_onlineCirclePaint);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(str3, this.resourcesProvider));
                        int i35 = this.progressStage;
                        if (i35 == 0) {
                            dp2 = AndroidUtilities.dp(f3) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            dp5 = AndroidUtilities.dp(3.0f);
                            dp6 = AndroidUtilities.dp(2.0f);
                            f5 = this.innerProgress;
                        } else {
                            if (i35 == i4) {
                                dp2 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                dp3 = AndroidUtilities.dp(f3);
                                dp4 = AndroidUtilities.dp(4.0f);
                                f4 = this.innerProgress;
                            } else if (i35 == 2) {
                                dp2 = AndroidUtilities.dp(f3) + (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                dp5 = AndroidUtilities.dp(5.0f);
                                dp6 = AndroidUtilities.dp(4.0f);
                                f5 = this.innerProgress;
                            } else if (i35 == 3) {
                                dp2 = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                dp3 = AndroidUtilities.dp(f3);
                                dp4 = AndroidUtilities.dp(2.0f);
                                f4 = this.innerProgress;
                            } else if (i35 == 4) {
                                dp2 = AndroidUtilities.dp(f3) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                dp5 = AndroidUtilities.dp(3.0f);
                                dp6 = AndroidUtilities.dp(2.0f);
                                f5 = this.innerProgress;
                            } else if (i35 == 5) {
                                dp2 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                dp3 = AndroidUtilities.dp(f3);
                                dp4 = AndroidUtilities.dp(4.0f);
                                f4 = this.innerProgress;
                            } else if (i35 == 6) {
                                dp2 = AndroidUtilities.dp(f3) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                dp5 = AndroidUtilities.dp(5.0f);
                                dp6 = AndroidUtilities.dp(4.0f);
                                f5 = this.innerProgress;
                            } else {
                                dp2 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                dp3 = AndroidUtilities.dp(f3);
                                dp4 = AndroidUtilities.dp(2.0f);
                                f4 = this.innerProgress;
                            }
                            f6 = dp3 + (dp4 * f4);
                            if (this.chatCallProgress >= f3 || progress < f3) {
                                canvas.save();
                                float f43 = this.chatCallProgress;
                                canvas2.scale(f43 * progress, f43 * progress, f41, f42);
                            }
                            this.rect.set(i34 - AndroidUtilities.dp(f3), f42 - dp2, AndroidUtilities.dp(f3) + i34, dp2 + f42);
                            canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                            float f44 = f42 - f6;
                            float f45 = f42 + f6;
                            this.rect.set(i34 - AndroidUtilities.dp(5.0f), f44, i34 - AndroidUtilities.dp(3.0f), f45);
                            canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                            this.rect.set(AndroidUtilities.dp(3.0f) + i34, f44, i34 + AndroidUtilities.dp(5.0f), f45);
                            canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                            if (this.chatCallProgress >= f3 || progress < f3) {
                                canvas.restore();
                            }
                            f7 = this.innerProgress + 0.04f;
                            this.innerProgress = f7;
                            if (f7 >= f3) {
                                this.innerProgress = f2;
                                int i36 = this.progressStage + i4;
                                this.progressStage = i36;
                                if (i36 >= 8) {
                                    this.progressStage = 0;
                                }
                            }
                            if (!this.hasCall) {
                                float f46 = this.chatCallProgress;
                                if (f46 < f3) {
                                    float f47 = f46 + 0.10666667f;
                                    this.chatCallProgress = f47;
                                    if (f47 > f3) {
                                        this.chatCallProgress = f3;
                                    }
                                }
                            } else {
                                float f48 = this.chatCallProgress;
                                if (f48 > f2) {
                                    float f49 = f48 - 0.10666667f;
                                    this.chatCallProgress = f49;
                                    if (f49 < f2) {
                                        this.chatCallProgress = f2;
                                    }
                                }
                            }
                            if (this.rightFragmentOpenedProgress != f2) {
                                canvas.restore();
                            }
                            z = true;
                        }
                        f6 = dp5 - (dp6 * f5);
                        if (this.chatCallProgress >= f3) {
                        }
                        canvas.save();
                        float f432 = this.chatCallProgress;
                        canvas2.scale(f432 * progress, f432 * progress, f41, f42);
                        this.rect.set(i34 - AndroidUtilities.dp(f3), f42 - dp2, AndroidUtilities.dp(f3) + i34, dp2 + f42);
                        canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                        float f442 = f42 - f6;
                        float f452 = f42 + f6;
                        this.rect.set(i34 - AndroidUtilities.dp(5.0f), f442, i34 - AndroidUtilities.dp(3.0f), f452);
                        canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                        this.rect.set(AndroidUtilities.dp(3.0f) + i34, f442, i34 + AndroidUtilities.dp(5.0f), f452);
                        canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                        if (this.chatCallProgress >= f3) {
                        }
                        canvas.restore();
                        f7 = this.innerProgress + 0.04f;
                        this.innerProgress = f7;
                        if (f7 >= f3) {
                        }
                        if (!this.hasCall) {
                        }
                        if (this.rightFragmentOpenedProgress != f2) {
                        }
                        z = true;
                    }
                }
                if (!this.showTtl) {
                    float f50 = this.ttlProgress;
                    if (f50 < f3) {
                        this.ttlProgress = f50 + 0.10666667f;
                        z = true;
                    }
                    this.ttlProgress = Utilities.clamp(this.ttlProgress, f3, f2);
                } else {
                    float f51 = this.ttlProgress;
                    if (f51 > f2) {
                        this.ttlProgress = f51 - 0.10666667f;
                        z = true;
                    }
                    this.ttlProgress = Utilities.clamp(this.ttlProgress, f3, f2);
                }
            }
        }
        if (this.rightFragmentOpenedProgress > f2 && this.currentDialogFolderId == 0) {
            if (!this.isTopic) {
                z4 = this.topicMuted;
            } else {
                TLRPC$Chat tLRPC$Chat3 = this.chat;
                z4 = (tLRPC$Chat3 == null || !tLRPC$Chat3.forum || this.forumTopic != null) ? this.dialogMuted : !this.hasUnmutedTopics;
            }
            drawCounter(canvas, z4, (int) ((this.avatarImage.getImageY() + this.avatarImage.getImageHeight()) - AndroidUtilities.dp(22.0f)), (int) (((this.avatarImage.getImageX() + this.avatarImage.getImageWidth()) - this.countWidth) - AndroidUtilities.dp(5.0f)), (int) (((this.avatarImage.getImageX() + this.avatarImage.getImageWidth()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
        }
        if (this.collapseOffset != f2) {
            canvas.restore();
        }
        if (this.translationX != f2) {
            canvas.restore();
        }
        if ((this.currentDialogFolderId == 0 || (this.isTopic && (tLRPC$TL_forumTopic = this.forumTopic) != null && tLRPC$TL_forumTopic.id == i4)) && this.translationX == f2 && this.archivedChatsDrawable != null) {
            canvas.save();
            canvas2.translate(f2, -this.translateY);
            canvas2.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
            this.archivedChatsDrawable.draw(canvas2);
            canvas.restore();
        }
        if (this.useSeparator) {
            int dp17 = (this.fullSeparator || (this.currentDialogFolderId != 0 && this.archiveHidden && !this.fullSeparator2) || (this.fullSeparator2 && !this.archiveHidden)) ? 0 : AndroidUtilities.dp(this.messagePaddingStart);
            if (this.rightFragmentOpenedProgress != f3) {
                int alpha3 = Theme.dividerPaint.getAlpha();
                float f52 = this.rightFragmentOpenedProgress;
                if (f52 != f2) {
                    Theme.dividerPaint.setAlpha((int) (alpha3 * (f3 - f52)));
                }
                if (LocaleController.isRTL) {
                    canvas.drawLine(0.0f, getMeasuredHeight() - i4, getMeasuredWidth() - dp17, getMeasuredHeight() - i4, Theme.dividerPaint);
                } else {
                    canvas.drawLine(dp17, getMeasuredHeight() - i4, getMeasuredWidth(), getMeasuredHeight() - i4, Theme.dividerPaint);
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
                Theme.dialogs_pinnedPaint.setColor(Theme.getColor(str3, this.resourcesProvider));
                canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), this.topClip * this.clipProgress, Theme.dialogs_pinnedPaint);
                canvas.drawRect(0.0f, getMeasuredHeight() - ((int) (this.bottomClip * this.clipProgress)), getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
            }
        }
        z2 = this.drawReorder;
        if (!z2 || this.reorderIconProgress != f2) {
            if (!z2) {
                float f53 = this.reorderIconProgress;
                if (f53 < f3) {
                    float f54 = f53 + 0.09411765f;
                    this.reorderIconProgress = f54;
                    if (f54 > f3) {
                        this.reorderIconProgress = f3;
                    }
                    z3 = true;
                }
            } else {
                float f55 = this.reorderIconProgress;
                if (f55 > f2) {
                    float f56 = f55 - 0.09411765f;
                    this.reorderIconProgress = f56;
                    if (f56 < f2) {
                        this.reorderIconProgress = f2;
                    }
                    z3 = true;
                }
            }
            if (this.archiveHidden) {
                float f57 = this.archiveBackgroundProgress;
                if (f57 > f2) {
                    float f58 = f57 - 0.069565214f;
                    this.archiveBackgroundProgress = f58;
                    if (f58 < f2) {
                        this.archiveBackgroundProgress = f2;
                    }
                    if (this.avatarDrawable.getAvatarType() == 2) {
                        this.avatarDrawable.setArchivedAvatarHiddenProgress(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.archiveBackgroundProgress));
                    }
                    z3 = true;
                }
                if (this.animatingArchiveAvatar) {
                    float f59 = this.animatingArchiveAvatarProgress + 16.0f;
                    this.animatingArchiveAvatarProgress = f59;
                    if (f59 >= 170.0f) {
                        this.animatingArchiveAvatarProgress = 170.0f;
                        this.animatingArchiveAvatar = false;
                    }
                    z3 = true;
                }
                if (!this.drawRevealBackground) {
                    float f60 = this.currentRevealBounceProgress;
                    if (f60 < f3) {
                        float f61 = f60 + 0.09411765f;
                        this.currentRevealBounceProgress = f61;
                        if (f61 > f3) {
                            this.currentRevealBounceProgress = f3;
                            z3 = true;
                        }
                    }
                    float f62 = this.currentRevealProgress;
                    if (f62 < f3) {
                        float f63 = f62 + 0.053333335f;
                        this.currentRevealProgress = f63;
                        if (f63 > f3) {
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
                float f64 = this.currentRevealProgress;
                if (f64 > f2) {
                    float f65 = f64 - 0.053333335f;
                    this.currentRevealProgress = f65;
                    if (f65 < f2) {
                        this.currentRevealProgress = f2;
                    }
                    z3 = true;
                }
                if (z3) {
                }
            } else {
                float f66 = this.archiveBackgroundProgress;
                if (f66 < f3) {
                    float f67 = f66 + 0.069565214f;
                    this.archiveBackgroundProgress = f67;
                    if (f67 > f3) {
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
        if (this.archiveHidden) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDraw$3() {
        DialogCellDelegate dialogCellDelegate = this.delegate;
        if (dialogCellDelegate != null) {
            dialogCellDelegate.onButtonClicked(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDraw$4() {
        DialogCellDelegate dialogCellDelegate = this.delegate;
        if (dialogCellDelegate != null) {
            dialogCellDelegate.onButtonLongPress(this);
        }
    }

    private void drawCounter(Canvas canvas, boolean z, int i, int i2, int i3, float f, boolean z2) {
        Paint paint;
        boolean z3;
        int dp;
        float f2;
        float interpolation;
        int color;
        if ((!this.drawCount || !this.drawCount2) && this.countChangeProgress == 1.0f) {
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
            }
            this.counterPaintOutline.setColor(ColorUtils.blendARGB(Theme.getColor("windowBackgroundWhite"), ColorUtils.setAlphaComponent(Theme.getColor("chats_pinnedOverlay"), 255), Color.alpha(color) / 255.0f));
        }
        if (this.isTopic && this.forumTopic.read_inbox_max_id == 0) {
            if (this.topicCounterPaint == null) {
                this.topicCounterPaint = new Paint();
            }
            paint = this.topicCounterPaint;
            int color2 = Theme.getColor(z ? "topics_unreadCounterMuted" : "topics_unreadCounter", this.resourcesProvider);
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
            if (f != 1.0f) {
                canvas.save();
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
                canvas.save();
                canvas.scale(f3, f3, this.rect.centerX(), this.rect.centerY());
            }
            RectF rectF = this.rect;
            float f5 = AndroidUtilities.density;
            canvas.drawRoundRect(rectF, f5 * 11.5f, f5 * 11.5f, paint);
            if (z2) {
                RectF rectF2 = this.rect;
                float f6 = AndroidUtilities.density;
                canvas.drawRoundRect(rectF2, f6 * 11.5f, f6 * 11.5f, this.counterPaintOutline);
            }
            if (staticLayout != null) {
                canvas.save();
                canvas.translate(i2, i + AndroidUtilities.dp(4.0f));
                staticLayout.draw(canvas);
                canvas.restore();
            }
            if (f3 != 1.0f) {
                canvas.restore();
            }
            if (f != 1.0f) {
                canvas.restore();
            }
        } else {
            paint.setAlpha((int) ((1.0f - this.reorderIconProgress) * i4));
            Theme.dialogs_countTextPaint.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
            float f7 = f3 * 2.0f;
            float f8 = f7 > 1.0f ? 1.0f : f7;
            float f9 = 1.0f - f8;
            float f10 = (i2 * f8) + (i3 * f9);
            float dp2 = f10 - AndroidUtilities.dp(5.5f);
            float f11 = i;
            this.rect.set(dp2, f11, (this.countWidth * f8) + dp2 + (this.countWidthOld * f9) + AndroidUtilities.dp(11.0f), AndroidUtilities.dp(23.0f) + i);
            if (f3 <= 0.5f) {
                interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(f7) * 0.1f;
                f2 = 1.0f;
            } else {
                f2 = 1.0f;
                interpolation = CubicBezierInterpolator.EASE_IN.getInterpolation(1.0f - ((f3 - 0.5f) * 2.0f)) * 0.1f;
            }
            canvas.save();
            float f12 = (interpolation + f2) * f;
            canvas.scale(f12, f12, this.rect.centerX(), this.rect.centerY());
            RectF rectF3 = this.rect;
            float f13 = AndroidUtilities.density;
            canvas.drawRoundRect(rectF3, f13 * 11.5f, f13 * 11.5f, paint);
            if (z2) {
                RectF rectF4 = this.rect;
                float f14 = AndroidUtilities.density;
                canvas.drawRoundRect(rectF4, f14 * 11.5f, f14 * 11.5f, this.counterPaintOutline);
            }
            if (this.countAnimationStableLayout != null) {
                canvas.save();
                canvas.translate(f10, i + AndroidUtilities.dp(4.0f));
                this.countAnimationStableLayout.draw(canvas);
                canvas.restore();
            }
            int alpha = Theme.dialogs_countTextPaint.getAlpha();
            float f15 = alpha;
            Theme.dialogs_countTextPaint.setAlpha((int) (f15 * f8));
            if (this.countAnimationInLayout != null) {
                canvas.save();
                canvas.translate(f10, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * f9) + f11 + AndroidUtilities.dp(4.0f));
                this.countAnimationInLayout.draw(canvas);
                canvas.restore();
            } else if (this.countLayout != null) {
                canvas.save();
                canvas.translate(f10, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * f9) + f11 + AndroidUtilities.dp(4.0f));
                this.countLayout.draw(canvas);
                canvas.restore();
            }
            if (this.countOldLayout != null) {
                Theme.dialogs_countTextPaint.setAlpha((int) (f15 * f9));
                canvas.save();
                canvas.translate(f10, ((this.countAnimationIncrement ? -AndroidUtilities.dp(13.0f) : AndroidUtilities.dp(13.0f)) * f8) + f11 + AndroidUtilities.dp(4.0f));
                this.countOldLayout.draw(canvas);
                canvas.restore();
            }
            Theme.dialogs_countTextPaint.setAlpha(alpha);
            canvas.restore();
        }
        if (!z3) {
            return;
        }
        Theme.dialogs_countTextPaint.setColor(Theme.getColor("chats_unreadCounterText"));
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
        this.statusDrawableAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                DialogCell.this.lambda$createStatusDrawableAnimator$5(valueAnimator);
            }
        });
        this.statusDrawableAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.4
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
    public /* synthetic */ void lambda$createStatusDrawableAnimator$5(ValueAnimator valueAnimator) {
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
                pullForegroundDrawable.outCy = this.avatarImage.getCenterY();
                this.archivedChatsDrawable.outCx = this.avatarImage.getCenterX();
                this.archivedChatsDrawable.outRadius = this.avatarImage.getImageWidth() / 2.0f;
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
        float f = 1.0f;
        if (z2) {
            if (z) {
                f = 0.0f;
            }
            this.reorderIconProgress = f;
        } else {
            if (!z) {
                f = 0.0f;
            }
            this.reorderIconProgress = f;
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
            accessibilityEvent.setContentDescription(sb.toString());
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
        accessibilityEvent.setContentDescription(sb.toString());
    }

    private MessageObject getCaptionMessage() {
        CharSequence charSequence;
        if (this.groupMessages == null) {
            MessageObject messageObject = this.message;
            if (messageObject != null && messageObject.caption != null) {
                return messageObject;
            }
            return null;
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
        if (i <= 1) {
            return messageObject2;
        }
        return null;
    }

    public void updateMessageThumbs() {
        MessageObject messageObject = this.message;
        if (messageObject == null) {
            return;
        }
        String restrictionReason = MessagesController.getRestrictionReason(messageObject.messageOwner.restriction_reason);
        ArrayList<MessageObject> arrayList = this.groupMessages;
        String str = null;
        if (arrayList != null && arrayList.size() > 1 && TextUtils.isEmpty(restrictionReason) && this.currentDialogFolderId == 0 && this.encryptedChat == null) {
            this.thumbsCount = 0;
            this.hasVideoThumb = false;
            Collections.sort(this.groupMessages, DialogCell$$ExternalSyntheticLambda5.INSTANCE);
            for (int i = 0; i < Math.min(3, this.groupMessages.size()); i++) {
                MessageObject messageObject2 = this.groupMessages.get(i);
                if (messageObject2 != null && !messageObject2.needDrawBluredPreview() && (messageObject2.isPhoto() || messageObject2.isNewGif() || messageObject2.isVideo() || messageObject2.isRoundVideo())) {
                    String str2 = messageObject2.isWebpage() ? messageObject2.messageOwner.media.webpage.type : null;
                    if (!"app".equals(str2) && !"profile".equals(str2) && !"article".equals(str2) && (str2 == null || !str2.startsWith("telegram_"))) {
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
        if (!this.message.isPhoto() && !this.message.isNewGif() && !this.message.isVideo() && !this.message.isRoundVideo()) {
            return;
        }
        if (this.message.isWebpage()) {
            str = this.message.messageOwner.media.webpage.type;
        }
        if ("app".equals(str) || "profile".equals(str) || "article".equals(str)) {
            return;
        }
        if (str != null && str.startsWith("telegram_")) {
            return;
        }
        setThumb(0, this.message);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$updateMessageThumbs$6(MessageObject messageObject, MessageObject messageObject2) {
        return messageObject.getId() - messageObject2.getId();
    }

    private void setThumb(int i, MessageObject messageObject) {
        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 40);
        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
        if (closestPhotoSizeWithSize == closestPhotoSizeWithSize2) {
            closestPhotoSizeWithSize2 = null;
        }
        if (closestPhotoSizeWithSize2 == null || DownloadController.getInstance(this.currentAccount).canDownloadMedia(messageObject.messageOwner) == 0) {
            closestPhotoSizeWithSize2 = closestPhotoSizeWithSize;
        }
        if (closestPhotoSizeWithSize != null) {
            this.hasVideoThumb = this.hasVideoThumb || messageObject.isVideo() || messageObject.isRoundVideo();
            int i2 = this.thumbsCount;
            if (i2 >= 3) {
                return;
            }
            this.thumbsCount = i2 + 1;
            this.drawPlay[i] = messageObject.isVideo() || messageObject.isRoundVideo();
            this.thumbImage[i].setImage(ImageLocation.getForObject(closestPhotoSizeWithSize2, messageObject.photoThumbsObject), "20_20", ImageLocation.getForObject(closestPhotoSizeWithSize, messageObject.photoThumbsObject), "20_20", (messageObject.type != 1 || closestPhotoSizeWithSize2 == null) ? 0 : closestPhotoSizeWithSize2.size, null, messageObject, 0);
            this.thumbImage[i].setRoundRadius(AndroidUtilities.dp(messageObject.isRoundVideo() ? 18.0f : 2.0f));
            this.needEmoji = false;
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
        if (messageObject3 != null && (tLRPC$Message = messageObject3.messageOwner) != null && (tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) != null && (str2 = tLRPC$MessageFwdHeader.from_name) != null) {
            return str2;
        }
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

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0264  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0278 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r16v0, types: [org.telegram.ui.Cells.DialogCell, android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r2v5, types: [android.text.SpannableStringBuilder, java.lang.CharSequence, android.text.Spannable] */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r3v11, types: [java.lang.CharSequence[]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SpannableStringBuilder getMessageStringFormatted(String str, String str2, CharSequence charSequence, boolean z) {
        TLRPC$Message tLRPC$Message;
        String charSequence2;
        CharSequence charSequence3;
        TLRPC$TL_forumTopic findTopic;
        MessageObject captionMessage = getCaptionMessage();
        MessageObject messageObject = this.message;
        Paint.FontMetricsInt fontMetricsInt = null;
        CharSequence charSequence4 = messageObject != null ? messageObject.messageText : null;
        this.applyName = true;
        int i = 0;
        if (!TextUtils.isEmpty(str2)) {
            return SpannableStringBuilder.valueOf(AndroidUtilities.formatSpannable(str, str2, charSequence));
        }
        MessageObject messageObject2 = this.message;
        TLRPC$Message tLRPC$Message2 = messageObject2.messageOwner;
        if (tLRPC$Message2 instanceof TLRPC$TL_messageService) {
            CharSequence charSequence5 = messageObject2.messageTextShort;
            if (charSequence5 == null || ((tLRPC$Message2.action instanceof TLRPC$TL_messageActionTopicCreate) && this.isTopic)) {
                charSequence5 = messageObject2.messageText;
            }
            if (MessageObject.isTopicActionMessage(messageObject2)) {
                SpannableStringBuilder formatSpannable = AndroidUtilities.formatSpannable(str, charSequence5, charSequence);
                if (!(this.message.topicIconDrawable[0] instanceof ForumBubbleDrawable) || (findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-this.message.getDialogId(), MessageObject.getTopicId(this.message.messageOwner, true))) == null) {
                    return formatSpannable;
                }
                ((ForumBubbleDrawable) this.message.topicIconDrawable[0]).setColor(findTopic.icon_color);
                return formatSpannable;
            }
            this.applyName = false;
            return SpannableStringBuilder.valueOf(charSequence5);
        }
        String str3 = "";
        if (captionMessage != null && (charSequence3 = captionMessage.caption) != null) {
            CharSequence charSequence6 = charSequence3.toString();
            if (this.needEmoji) {
                if (captionMessage.isVideo()) {
                    str3 = "📹 ";
                } else if (captionMessage.isVoice()) {
                    str3 = "🎤 ";
                } else if (captionMessage.isMusic()) {
                    str3 = "🎧 ";
                } else {
                    str3 = captionMessage.isPhoto() ? "🖼 " : "📎 ";
                }
            }
            if (captionMessage.hasHighlightedWords() && !TextUtils.isEmpty(captionMessage.messageOwner.message)) {
                String str4 = captionMessage.messageTrimmedToHighlight;
                int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) + 24);
                if (this.hasNameInMessage) {
                    if (!TextUtils.isEmpty(charSequence)) {
                        measuredWidth = (int) (measuredWidth - this.currentMessagePaint.measureText(charSequence.toString()));
                    }
                    measuredWidth = (int) (measuredWidth - this.currentMessagePaint.measureText(": "));
                }
                if (measuredWidth > 0) {
                    str4 = AndroidUtilities.ellipsizeCenterEnd(str4, captionMessage.highlightedWords.get(0), measuredWidth, this.currentMessagePaint, 130).toString();
                }
                return new SpannableStringBuilder(str3).append((CharSequence) str4);
            }
            if (charSequence6.length() > 150) {
                charSequence6 = charSequence6.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence6);
            MediaDataController.addTextStyleRuns(captionMessage.messageOwner.entities, charSequence6, spannableStringBuilder, 256);
            TLRPC$Message tLRPC$Message3 = captionMessage.messageOwner;
            if (tLRPC$Message3 != null) {
                ArrayList<TLRPC$MessageEntity> arrayList = tLRPC$Message3.entities;
                TextPaint textPaint = this.currentMessagePaint;
                if (textPaint != null) {
                    fontMetricsInt = textPaint.getFontMetricsInt();
                }
                MediaDataController.addAnimatedEmojiSpans(arrayList, spannableStringBuilder, fontMetricsInt);
            }
            CharSequence append = new SpannableStringBuilder(str3).append(AndroidUtilities.replaceNewLines(spannableStringBuilder));
            if (z) {
                append = applyThumbs(append);
            }
            return AndroidUtilities.formatSpannable(str, append, charSequence);
        } else if (tLRPC$Message2.media != null && !messageObject2.isMediaEmpty()) {
            this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
            MessageObject messageObject3 = this.message;
            TLRPC$MessageMedia tLRPC$MessageMedia = messageObject3.messageOwner.media;
            String str5 = "chats_actionMessage";
            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia;
                charSequence2 = Build.VERSION.SDK_INT >= 18 ? String.format("📊 \u2068%s\u2069", tLRPC$TL_messageMediaPoll.poll.question) : String.format("📊 %s", tLRPC$TL_messageMediaPoll.poll.question);
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                charSequence2 = Build.VERSION.SDK_INT >= 18 ? String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title) : String.format("🎮 %s", tLRPC$MessageMedia.game.title);
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) {
                charSequence2 = tLRPC$MessageMedia.title;
            } else if (messageObject3.type == 14) {
                charSequence2 = Build.VERSION.SDK_INT >= 18 ? String.format("🎧 \u2068%s - %s\u2069", messageObject3.getMusicAuthor(), this.message.getMusicTitle()) : String.format("🎧 %s - %s", messageObject3.getMusicAuthor(), this.message.getMusicTitle());
            } else {
                if (this.thumbsCount > 1) {
                    if (this.hasVideoThumb) {
                        ArrayList<MessageObject> arrayList2 = this.groupMessages;
                        charSequence2 = LocaleController.formatPluralString("Media", arrayList2 == null ? 0 : arrayList2.size(), new Object[0]);
                    } else {
                        ArrayList<MessageObject> arrayList3 = this.groupMessages;
                        charSequence2 = LocaleController.formatPluralString("Photos", arrayList3 == null ? 0 : arrayList3.size(), new Object[0]);
                    }
                } else {
                    charSequence2 = charSequence4.toString();
                }
                CharSequence replace = charSequence2.replace('\n', ' ');
                if (z) {
                    replace = applyThumbs(replace);
                }
                SpannableStringBuilder formatSpannable2 = AndroidUtilities.formatSpannable(str, replace, charSequence);
                if (!isForumCell()) {
                    try {
                        ForegroundColorSpanThemable foregroundColorSpanThemable = new ForegroundColorSpanThemable(str5, this.resourcesProvider);
                        if (this.hasNameInMessage) {
                            i = charSequence.length() + 2;
                        }
                        formatSpannable2.setSpan(foregroundColorSpanThemable, i, formatSpannable2.length(), 33);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                return formatSpannable2;
            }
            str5 = "chats_attachMessage";
            CharSequence replace2 = charSequence2.replace('\n', ' ');
            if (z) {
            }
            SpannableStringBuilder formatSpannable22 = AndroidUtilities.formatSpannable(str, replace2, charSequence);
            if (!isForumCell()) {
            }
            return formatSpannable22;
        } else {
            MessageObject messageObject4 = this.message;
            CharSequence charSequence7 = messageObject4.messageOwner.message;
            if (charSequence7 != null) {
                if (messageObject4.hasHighlightedWords()) {
                    String str6 = this.message.messageTrimmedToHighlight;
                    if (str6 != null) {
                        charSequence7 = str6;
                    }
                    int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) + 10);
                    if (this.hasNameInMessage) {
                        if (!TextUtils.isEmpty(charSequence)) {
                            measuredWidth2 = (int) (measuredWidth2 - this.currentMessagePaint.measureText(charSequence.toString()));
                        }
                        measuredWidth2 = (int) (measuredWidth2 - this.currentMessagePaint.measureText(": "));
                    }
                    if (measuredWidth2 > 0) {
                        charSequence7 = AndroidUtilities.ellipsizeCenterEnd(charSequence7, this.message.highlightedWords.get(0), measuredWidth2, this.currentMessagePaint, 130).toString();
                    }
                } else {
                    if (charSequence7.length() > 150) {
                        charSequence7 = charSequence7.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                    }
                    charSequence7 = AndroidUtilities.replaceNewLines(charSequence7);
                }
                ?? spannableStringBuilder2 = new SpannableStringBuilder(charSequence7);
                MediaDataController.addTextStyleRuns(this.message, (Spannable) spannableStringBuilder2, 256);
                MessageObject messageObject5 = this.message;
                if (messageObject5 != null && (tLRPC$Message = messageObject5.messageOwner) != null) {
                    ArrayList<TLRPC$MessageEntity> arrayList4 = tLRPC$Message.entities;
                    TextPaint textPaint2 = this.currentMessagePaint;
                    if (textPaint2 != null) {
                        fontMetricsInt = textPaint2.getFontMetricsInt();
                    }
                    MediaDataController.addAnimatedEmojiSpans(arrayList4, spannableStringBuilder2, fontMetricsInt);
                }
                if (z) {
                    spannableStringBuilder2 = applyThumbs(spannableStringBuilder2);
                }
                return AndroidUtilities.formatSpannable(str, new CharSequence[]{spannableStringBuilder2, charSequence});
            }
            return SpannableStringBuilder.valueOf(str3);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        CanvasButton canvasButton;
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
        public int lastKnownTypingType;
        public int lastTopicsCount;
        long startWaitingTime;
        public boolean typingOutToTop;
        public float typingProgres;
        boolean waitngNewMessageFroTypingAnimation;

        private DialogUpdateHelper() {
            this.waitngNewMessageFroTypingAnimation = false;
        }

        /* JADX WARN: Code restructure failed: missing block: B:33:0x0108, code lost:
            if (org.telegram.messenger.MessagesController.getInstance(r17.this$0.currentAccount).getTopicsController().endIsReached(-r17.this$0.currentDialogId) != false) goto L104;
         */
        /* JADX WARN: Removed duplicated region for block: B:100:0x013a  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x0113  */
        /* JADX WARN: Removed duplicated region for block: B:41:0x0158  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x0187  */
        /* JADX WARN: Removed duplicated region for block: B:72:0x01d3  */
        /* JADX WARN: Removed duplicated region for block: B:76:0x0214  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x01de  */
        /* JADX WARN: Removed duplicated region for block: B:96:0x01c6  */
        /* JADX WARN: Removed duplicated region for block: B:98:0x015a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean update() {
            int i;
            int i2;
            TLRPC$Dialog tLRPC$Dialog = MessagesController.getInstance(DialogCell.this.currentAccount).dialogs_dict.get(DialogCell.this.currentDialogId);
            if (tLRPC$Dialog == null) {
                return false;
            }
            int id = DialogCell.this.message == null ? 0 : DialogCell.this.message.getId();
            long j = tLRPC$Dialog.read_inbox_max_id + (tLRPC$Dialog.read_outbox_max_id << 8) + ((tLRPC$Dialog.unread_count + (tLRPC$Dialog.unread_mark ? -1 : 0)) << 16) + (tLRPC$Dialog.unread_reactions_count > 0 ? 262144 : 0) + (tLRPC$Dialog.unread_mentions_count > 0 ? 524288 : 0);
            TLRPC$DraftMessage tLRPC$DraftMessage = null;
            Integer printingStringType = (DialogCell.this.isForumCell() || (!DialogCell.this.isDialogCell && !DialogCell.this.isTopic) || TextUtils.isEmpty(MessagesController.getInstance(DialogCell.this.currentAccount).getPrintingString(DialogCell.this.currentDialogId, DialogCell.this.getTopicId(), true))) ? null : MessagesController.getInstance(DialogCell.this.currentAccount).getPrintingStringType(DialogCell.this.currentDialogId, DialogCell.this.getTopicId());
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
                int hashCode = tLRPC$DraftMessage != null ? 0 : tLRPC$DraftMessage.message.hashCode() + (tLRPC$DraftMessage.reply_to_msg_id << 16);
                boolean z = DialogCell.this.chat == null && DialogCell.this.chat.call_active && DialogCell.this.chat.call_not_empty;
                if (this.lastDrawnSizeHash != measuredWidth) {
                    i2 = measuredWidth;
                    if (this.lastDrawnMessageId == id && this.lastDrawnDialogId == DialogCell.this.currentDialogId && this.lastDrawnDialogIsFolder == tLRPC$Dialog.isFolder && this.lastDrawnReadState == j && Objects.equals(this.lastDrawnPrintingType, printingStringType) && this.lastTopicsCount == i && hashCode == this.lastDrawnDraftHash && this.lastDrawnPinned == DialogCell.this.drawPin && this.lastDrawnHasCall == z) {
                        return false;
                    }
                } else {
                    i2 = measuredWidth;
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
                    }
                    if (this.lastDrawnMessageId != id) {
                        this.typingOutToTop = false;
                    } else {
                        this.typingOutToTop = true;
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
                this.lastDrawnSizeHash = i2;
                this.lastDrawnDraftHash = hashCode;
                this.lastTopicsCount = i;
                this.lastDrawnPinned = DialogCell.this.drawPin;
                this.lastDrawnHasCall = z;
                return true;
            }
            i = 0;
            if (!DialogCell.this.isTopic) {
            }
            if (tLRPC$DraftMessage != null) {
            }
            if (DialogCell.this.chat == null) {
            }
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
            this.lastDrawnSizeHash = i2;
            this.lastDrawnDraftHash = hashCode;
            this.lastTopicsCount = i;
            this.lastDrawnPinned = DialogCell.this.drawPin;
            this.lastDrawnHasCall = z;
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
}
