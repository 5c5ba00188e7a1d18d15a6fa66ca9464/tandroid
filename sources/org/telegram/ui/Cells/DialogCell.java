package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ChatThemeController;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
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
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$DraftMessage;
import org.telegram.tgnet.TLRPC$EmojiStatus;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$MessageFwdHeader;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$MessagePeerReaction;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_dialog;
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
import org.telegram.ui.Components.PullForegroundDrawable;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.StaticLayoutEx;
import org.telegram.ui.Components.StatusDrawable;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.URLSpanNoUnderlineBold;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.DialogsActivity;
/* loaded from: classes3.dex */
public class DialogCell extends BaseCell {
    private int animateFromStatusDrawableParams;
    private int animateToStatusDrawableParams;
    private AnimatedEmojiSpan.EmojiGroupedSpans animatedEmojiStack;
    private AnimatedEmojiSpan.EmojiGroupedSpans animatedEmojiStack2;
    private AnimatedEmojiSpan.EmojiGroupedSpans animatedEmojiStack3;
    private boolean animatingArchiveAvatar;
    private float animatingArchiveAvatarProgress;
    private float archiveBackgroundProgress;
    private boolean archiveHidden;
    private PullForegroundDrawable archivedChatsDrawable;
    private AvatarDrawable avatarDrawable;
    public ImageReceiver avatarImage;
    private int bottomClip;
    private Paint buttonBackgroundPaint;
    private StaticLayout buttonLayout;
    private int buttonTop;
    CanvasButton canvasButton;
    private TLRPC$Chat chat;
    private float chatCallProgress;
    private CheckBox2 checkBox;
    private int checkDrawLeft;
    private int checkDrawLeft1;
    private int checkDrawTop;
    public float chekBoxPaddingTop;
    private boolean clearingDialog;
    private float clipProgress;
    private int clockDrawLeft;
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
    private int index;
    private float innerProgress;
    private BounceInterpolator interpolator;
    private boolean isDialogCell;
    private boolean isForum;
    private boolean isSelected;
    private boolean isSliding;
    private boolean isTopic;
    long lastDialogChangedTime;
    private int lastDrawSwipeMessageStringId;
    private RLottieDrawable lastDrawTranslationDrawable;
    private int lastMessageDate;
    private CharSequence lastMessageString;
    private CharSequence lastPrintString;
    private int lastSendState;
    int lastSize;
    private int lastStatusDrawableParams;
    private boolean lastUnreadState;
    private long lastUpdateTime;
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
    private ImageReceiver[] thumbImage;
    int thumbSize;
    private int thumbsCount;
    private StaticLayout timeLayout;
    private int timeLeft;
    private int timeTop;
    private int topClip;
    int topMessageTopicEndIndex;
    int topMessageTopicStartIndex;
    private Paint topicCounterPaint;
    private boolean topicMuted;
    protected int translateY;
    private boolean translationAnimationStarted;
    private RLottieDrawable translationDrawable;
    private float translationX;
    private boolean twoLinesForName;
    private int unreadCount;
    public boolean useForceThreeLines;
    private boolean useMeForMyMessages;
    public boolean useSeparator;
    private TLRPC$User user;

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

    public void setForumTopic(TLRPC$TL_forumTopic tLRPC$TL_forumTopic, long j, MessageObject messageObject, boolean z) {
        this.forumTopic = tLRPC$TL_forumTopic;
        boolean z2 = true;
        this.isTopic = tLRPC$TL_forumTopic != null;
        if (this.currentDialogId != j) {
            this.lastStatusDrawableParams = -1;
        }
        ForumBubbleDrawable[] forumBubbleDrawableArr = messageObject.topicIconDrawable;
        if (forumBubbleDrawableArr[0] != null) {
            forumBubbleDrawableArr[0].setColor(tLRPC$TL_forumTopic.icon_color);
        }
        this.currentDialogId = j;
        this.lastDialogChangedTime = System.currentTimeMillis();
        this.message = messageObject;
        this.isDialogCell = false;
        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
        this.lastMessageDate = tLRPC$Message.date;
        this.currentEditDate = tLRPC$Message.edit_date;
        this.markUnread = false;
        this.messageId = messageObject.getId();
        if (!messageObject.isUnread()) {
            z2 = false;
        }
        this.lastUnreadState = z2;
        MessageObject messageObject2 = this.message;
        if (messageObject2 != null) {
            this.lastSendState = messageObject2.messageOwner.send_state;
        }
        if (!z) {
            this.lastStatusDrawableParams = -1;
        }
        if (tLRPC$TL_forumTopic != null) {
            this.groupMessages = tLRPC$TL_forumTopic.groupedMessages;
        }
        update(0, z);
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
        this.hasUnmutedTopics = false;
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
        update(0, false);
        checkOnline();
        checkGroupCall();
        checkChatTheme();
    }

    public void setDialogIndex(int i) {
        this.index = i;
    }

    public void setDialog(CustomDialog customDialog) {
        this.customDialog = customDialog;
        this.messageId = 0;
        update(0);
        checkOnline();
        checkGroupCall();
        checkChatTheme();
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

    public int getDialogIndex() {
        return this.index;
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
        if (isForumCell()) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 86.0f : (this.useSeparator ? 1 : 0) + 91));
        } else {
            setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? this.heightThreeLines : this.heightDefault) + (this.useSeparator ? 1 : 0) + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0));
        }
        this.topClip = 0;
        this.bottomClip = getMeasuredHeight();
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
        if (this.currentDialogId == 0 && this.customDialog == null) {
            return;
        }
        if (this.checkBox != null) {
            int dp = AndroidUtilities.dp(this.messagePaddingStart - ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 29 : 27));
            if (LocaleController.isRTL) {
                dp = (i3 - i) - dp;
            }
            int dp2 = AndroidUtilities.dp(this.chekBoxPaddingTop + ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 6 : 0));
            CheckBox2 checkBox2 = this.checkBox;
            checkBox2.layout(dp, dp2, checkBox2.getMeasuredWidth() + dp, this.checkBox.getMeasuredHeight() + dp2);
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

    /* JADX WARN: Can't wrap try/catch for region: R(41:422|423|(1:454)(5:427|428|429|430|(34:432|433|434|435|(1:445)(1:439)|440|(2:441|(1:443)(1:444))|203|(1:205)|206|207|208|(1:210)(1:394)|211|212|213|214|(1:390)(1:(2:221|(1:384)(1:227)))|228|(3:230|(3:232|(2:241|242)|239)|243)|244|(10:249|(2:251|(1:253))|254|255|256|(6:258|(6:262|(1:264)|265|(1:291)(2:269|(1:271)(2:277|(1:279)(2:280|(3:282|(1:284)(1:286)|285))))|272|(2:274|(1:276)))|292|(4:296|(1:(1:306)(2:298|(1:300)(2:301|302)))|303|(1:305))|307|(2:313|(1:315)))(6:334|(6:338|(1:340)|341|(4:343|(1:345)|346|(1:348))|349|(1:359))|360|(4:364|(1:366)|367|368)|369|(1:373))|316|(3:(1:330)(1:325)|326|(1:328)(1:329))|331|332)|375|(1:378)|379|(1:381)(1:383)|382|255|256|(0)(0)|316|(6:318|320|(1:323)|330|326|(0)(0))|331|332))|449|433|434|435|(1:437)|445|440|(3:441|(0)(0)|443)|203|(0)|206|207|208|(0)(0)|211|212|213|214|(1:216)|386|388|390|228|(0)|244|(11:246|249|(0)|254|255|256|(0)(0)|316|(0)|331|332)|375|(1:378)|379|(0)(0)|382|255|256|(0)(0)|316|(0)|331|332) */
    /* JADX WARN: Can't wrap try/catch for region: R(74:1|(1:1307)(1:5)|6|(1:1306)(1:12)|13|(1:1305)(1:17)|18|(1:20)|21|(2:23|(1:1294)(1:27))(2:1295|(1:1304)(1:1299))|28|(1:30)(1:1289)|31|(7:33|(1:35)|36|37|(1:39)|40|41)|42|(9:44|(2:46|(2:621|(1:623)(1:624))(2:50|(1:52)(1:620)))(4:625|(1:642)(1:629)|630|(2:638|(1:640)(1:641))(2:634|(1:636)(1:637)))|53|(3:55|(1:57)(4:607|(1:609)|610|(1:615)(1:614))|58)(3:616|(1:618)|619)|59|(1:61)(1:606)|62|(1:64)(1:(1:602)(1:(1:604)(1:605)))|65)(32:643|(2:1285|(1:1287)(1:1288))(2:647|(1:649)(1:1284))|650|(2:652|(2:654|(2:662|(1:664)(1:665))(2:658|(1:660)(1:661))))(2:1232|(2:1234|(2:1236|(1:1238)(2:1239|(1:1241)(3:1242|(1:1248)(1:1246)|1247)))(2:1249|(34:1251|(1:1253)(2:1274|(1:1276)(3:1277|(1:1283)(1:1281)|1282))|1254|(2:1256|(24:1260|1261|(2:1263|(2:1268|(1:1270)(1:1271))(1:1267))|667|(1:671)|672|(2:674|(1:678))(2:1228|(1:1230)(1:1231))|679|(6:1206|(2:1208|(2:1210|(2:1212|(1:1214))))|1216|(2:1218|(1:1220))|1222|(16:1224|(1:1226)|686|(3:688|(1:690)|691)(1:(15:837|(1:839)(1:848)|840|(1:842)(1:847)|(1:844)(1:846)|845|695|(1:697)(2:829|(1:831)(2:832|(1:834)(1:835)))|698|(1:700)(5:746|(4:748|(1:(1:751)(2:802|753))(1:803)|752|753)(7:804|(1:806)(6:816|(2:825|(1:827)(1:828))(1:824)|808|(1:810)(1:815)|811|(1:813)(1:814))|807|808|(0)(0)|811|(0)(0))|754|(2:759|(2:761|(1:763)(2:764|(1:766)(2:767|(3:769|(3:771|(1:773)(1:776)|774)(2:777|(3:779|(1:791)(1:783)|784)(3:792|(1:800)(1:798)|799))|775)))))|801)|701|(2:703|(2:705|(1:707)(2:708|(4:710|(1:712)|713|(1:715)))))|716|(1:718)(3:720|(2:722|(1:724)(1:729))(2:730|(2:732|(1:734)(2:735|(2:737|(1:739)(3:740|(1:742)|743))(1:744)))(1:745))|(1:728))|719)(12:849|(4:851|(2:853|(2:860|859)(1:857))(7:861|(1:863)|864|(3:868|(1:870)(1:872)|871)|873|(1:877)|878)|858|859)(3:879|(1:881)(2:883|(3:885|(2:887|(1:889)(2:890|(1:892)(2:893|(1:895)(2:896|(2:898|(1:900)(1:901))))))(2:903|(2:907|693))|902)(13:908|(1:910)(1:1201)|911|(2:925|(2:927|(10:929|(10:931|(1:933)(3:1194|(1:1196)(1:1198)|1197)|934|(1:936)(7:944|(3:946|(4:948|(2:950|(2:952|(1:954)(2:957|(1:959)(1:960))))|961|(1:963)(2:964|(1:966)(2:967|(1:969)(1:970))))(1:971)|955)(2:972|(7:977|(2:986|(2:999|(4:1058|(2:1060|(5:1062|(1:1074)|1068|(1:1072)|1073)(2:1075|(2:1082|(2:1089|(4:1091|(1:1093)(2:1115|(1:1117)(2:1118|(1:1120)(2:1121|(1:1123)(2:1124|(1:1126)(1:1127)))))|1094|(3:1107|(3:1109|(1:1111)(1:1113)|1112)|1114)(4:1098|(2:1100|(1:1102)(1:1103))|(1:1105)|1106))(2:1128|(3:1130|(3:1132|(1:1134)(1:1137)|1135)(3:1138|(1:1140)(1:1142)|1141)|1136)(4:1143|(1:1145)(2:1151|(1:1153)(2:1154|(1:1156)(2:1157|(1:1159)(3:1160|(2:1166|(2:1168|(3:1170|(1:1172)(1:1174)|1173)))(1:1164)|1165))))|1146|(2:1148|(1:1150)))))(1:1088))(1:1081)))|1175|(6:1177|(3:1189|(1:1191)|1192)(1:1181)|1182|(1:1184)|1185|(1:1187))(1:1193))(12:1010|(2:1014|(3:1016|(1:1022)(1:1020)|1021))|1023|(1:1057)(5:1027|1028|1029|1030|1031)|1032|(1:1036)|1037|(4:1039|(1:1041)|1042|(1:1044)(1:1045))|1046|940|(1:942)|943))(8:992|(1:998)(1:996)|997|938|939|940|(0)|943))(1:983)|984|985|940|(0)|943)(1:976))|956|939|940|(0)|943)|937|938|939|940|(0)|943)|1199|(0)(0)|937|938|939|940|(0)|943)))|1200|1199|(0)(0)|937|938|939|940|(0)|943))|882)|694|695|(0)(0)|698|(0)(0)|701|(0)|716|(0)(0)|719))|692|693|694|695|(0)(0)|698|(0)(0)|701|(0)|716|(0)(0)|719))|685|686|(0)(0)|692|693|694|695|(0)(0)|698|(0)(0)|701|(0)|716|(0)(0)|719))(1:1273)|1272|1261|(0)|667|(2:669|671)|672|(0)(0)|679|(2:681|1202)|1206|(0)|1216|(0)|1222|(0)|685|686|(0)(0)|692|693|694|695|(0)(0)|698|(0)(0)|701|(0)|716|(0)(0)|719))))|666|667|(0)|672|(0)(0)|679|(0)|1206|(0)|1216|(0)|1222|(0)|685|686|(0)(0)|692|693|694|695|(0)(0)|698|(0)(0)|701|(0)|716|(0)(0)|719)|(2:67|(1:69)(1:599))(1:600)|70|(3:72|(1:74)(1:597)|75)(1:598)|76|(1:78)(1:596)|79|(1:81)|82|(2:84|(1:86)(1:583))(2:584|(2:586|(2:588|(1:590)(1:591))(2:592|(1:594)(1:595))))|87|(2:553|(2:580|(1:582))(2:557|(2:559|(1:561))(2:562|(2:564|(1:566))(2:567|(4:569|(1:571)(1:575)|572|(1:574))))))(2:91|(1:93))|94|(16:95|96|(1:98)|99|(3:101|(1:103)(1:105)|104)|106|(1:108)(1:550)|109|(1:111)|112|(1:549)(1:118)|119|(1:121)(1:548)|122|(1:547)(1:126)|127)|128|(4:531|(1:533)(1:545)|534|(2:535|(3:537|(2:539|540)(2:542|543)|541)(1:544)))(8:132|(1:134)(1:530)|135|(1:137)(1:529)|138|(1:140)(1:528)|141|(2:142|(3:144|(2:146|147)(2:149|150)|148)(1:151)))|152|(1:154)|155|(2:157|(1:159)(1:160))|161|(2:163|(1:165)(1:458))(1:(4:(3:470|(1:472)(1:526)|473)(1:527)|(5:475|(1:477)(1:524)|478|(3:480|(1:482)(1:518)|483)(3:519|(1:521)(1:523)|522)|484)(1:525)|485|(2:487|(4:489|(3:491|(1:493)(1:495)|494)|496|(3:498|(1:500)(1:502)|501))(5:503|(3:505|(1:507)(1:509)|508)|510|(3:512|(1:514)(1:516)|515)|517)))(3:463|(2:465|(1:467))|468))|(7:(1:168)|169|(1:171)|172|(1:183)(1:176)|177|(1:181))|184|(1:457)(1:188)|189|(5:191|(1:396)(1:195)|196|(2:197|(1:199)(1:200))|201)(2:397|(41:422|423|(1:454)(5:427|428|429|430|(34:432|433|434|435|(1:445)(1:439)|440|(2:441|(1:443)(1:444))|203|(1:205)|206|207|208|(1:210)(1:394)|211|212|213|214|(1:390)(1:(2:221|(1:384)(1:227)))|228|(3:230|(3:232|(2:241|242)|239)|243)|244|(10:249|(2:251|(1:253))|254|255|256|(6:258|(6:262|(1:264)|265|(1:291)(2:269|(1:271)(2:277|(1:279)(2:280|(3:282|(1:284)(1:286)|285))))|272|(2:274|(1:276)))|292|(4:296|(1:(1:306)(2:298|(1:300)(2:301|302)))|303|(1:305))|307|(2:313|(1:315)))(6:334|(6:338|(1:340)|341|(4:343|(1:345)|346|(1:348))|349|(1:359))|360|(4:364|(1:366)|367|368)|369|(1:373))|316|(3:(1:330)(1:325)|326|(1:328)(1:329))|331|332)|375|(1:378)|379|(1:381)(1:383)|382|255|256|(0)(0)|316|(6:318|320|(1:323)|330|326|(0)(0))|331|332))|449|433|434|435|(1:437)|445|440|(3:441|(0)(0)|443)|203|(0)|206|207|208|(0)(0)|211|212|213|214|(1:216)|386|388|390|228|(0)|244|(11:246|249|(0)|254|255|256|(0)(0)|316|(0)|331|332)|375|(1:378)|379|(0)(0)|382|255|256|(0)(0)|316|(0)|331|332)(2:401|(4:406|(1:416)(1:410)|411|(2:412|(1:414)(1:415)))(1:405)))|202|203|(0)|206|207|208|(0)(0)|211|212|213|214|(0)|386|388|390|228|(0)|244|(0)|375|(0)|379|(0)(0)|382|255|256|(0)(0)|316|(0)|331|332|(1:(0))) */
    /* JADX WARN: Can't wrap try/catch for region: R(89:1|(1:1307)(1:5)|6|(1:1306)(1:12)|13|(1:1305)(1:17)|18|(1:20)|21|(2:23|(1:1294)(1:27))(2:1295|(1:1304)(1:1299))|28|(1:30)(1:1289)|31|(7:33|(1:35)|36|37|(1:39)|40|41)|42|(9:44|(2:46|(2:621|(1:623)(1:624))(2:50|(1:52)(1:620)))(4:625|(1:642)(1:629)|630|(2:638|(1:640)(1:641))(2:634|(1:636)(1:637)))|53|(3:55|(1:57)(4:607|(1:609)|610|(1:615)(1:614))|58)(3:616|(1:618)|619)|59|(1:61)(1:606)|62|(1:64)(1:(1:602)(1:(1:604)(1:605)))|65)(32:643|(2:1285|(1:1287)(1:1288))(2:647|(1:649)(1:1284))|650|(2:652|(2:654|(2:662|(1:664)(1:665))(2:658|(1:660)(1:661))))(2:1232|(2:1234|(2:1236|(1:1238)(2:1239|(1:1241)(3:1242|(1:1248)(1:1246)|1247)))(2:1249|(34:1251|(1:1253)(2:1274|(1:1276)(3:1277|(1:1283)(1:1281)|1282))|1254|(2:1256|(24:1260|1261|(2:1263|(2:1268|(1:1270)(1:1271))(1:1267))|667|(1:671)|672|(2:674|(1:678))(2:1228|(1:1230)(1:1231))|679|(6:1206|(2:1208|(2:1210|(2:1212|(1:1214))))|1216|(2:1218|(1:1220))|1222|(16:1224|(1:1226)|686|(3:688|(1:690)|691)(1:(15:837|(1:839)(1:848)|840|(1:842)(1:847)|(1:844)(1:846)|845|695|(1:697)(2:829|(1:831)(2:832|(1:834)(1:835)))|698|(1:700)(5:746|(4:748|(1:(1:751)(2:802|753))(1:803)|752|753)(7:804|(1:806)(6:816|(2:825|(1:827)(1:828))(1:824)|808|(1:810)(1:815)|811|(1:813)(1:814))|807|808|(0)(0)|811|(0)(0))|754|(2:759|(2:761|(1:763)(2:764|(1:766)(2:767|(3:769|(3:771|(1:773)(1:776)|774)(2:777|(3:779|(1:791)(1:783)|784)(3:792|(1:800)(1:798)|799))|775)))))|801)|701|(2:703|(2:705|(1:707)(2:708|(4:710|(1:712)|713|(1:715)))))|716|(1:718)(3:720|(2:722|(1:724)(1:729))(2:730|(2:732|(1:734)(2:735|(2:737|(1:739)(3:740|(1:742)|743))(1:744)))(1:745))|(1:728))|719)(12:849|(4:851|(2:853|(2:860|859)(1:857))(7:861|(1:863)|864|(3:868|(1:870)(1:872)|871)|873|(1:877)|878)|858|859)(3:879|(1:881)(2:883|(3:885|(2:887|(1:889)(2:890|(1:892)(2:893|(1:895)(2:896|(2:898|(1:900)(1:901))))))(2:903|(2:907|693))|902)(13:908|(1:910)(1:1201)|911|(2:925|(2:927|(10:929|(10:931|(1:933)(3:1194|(1:1196)(1:1198)|1197)|934|(1:936)(7:944|(3:946|(4:948|(2:950|(2:952|(1:954)(2:957|(1:959)(1:960))))|961|(1:963)(2:964|(1:966)(2:967|(1:969)(1:970))))(1:971)|955)(2:972|(7:977|(2:986|(2:999|(4:1058|(2:1060|(5:1062|(1:1074)|1068|(1:1072)|1073)(2:1075|(2:1082|(2:1089|(4:1091|(1:1093)(2:1115|(1:1117)(2:1118|(1:1120)(2:1121|(1:1123)(2:1124|(1:1126)(1:1127)))))|1094|(3:1107|(3:1109|(1:1111)(1:1113)|1112)|1114)(4:1098|(2:1100|(1:1102)(1:1103))|(1:1105)|1106))(2:1128|(3:1130|(3:1132|(1:1134)(1:1137)|1135)(3:1138|(1:1140)(1:1142)|1141)|1136)(4:1143|(1:1145)(2:1151|(1:1153)(2:1154|(1:1156)(2:1157|(1:1159)(3:1160|(2:1166|(2:1168|(3:1170|(1:1172)(1:1174)|1173)))(1:1164)|1165))))|1146|(2:1148|(1:1150)))))(1:1088))(1:1081)))|1175|(6:1177|(3:1189|(1:1191)|1192)(1:1181)|1182|(1:1184)|1185|(1:1187))(1:1193))(12:1010|(2:1014|(3:1016|(1:1022)(1:1020)|1021))|1023|(1:1057)(5:1027|1028|1029|1030|1031)|1032|(1:1036)|1037|(4:1039|(1:1041)|1042|(1:1044)(1:1045))|1046|940|(1:942)|943))(8:992|(1:998)(1:996)|997|938|939|940|(0)|943))(1:983)|984|985|940|(0)|943)(1:976))|956|939|940|(0)|943)|937|938|939|940|(0)|943)|1199|(0)(0)|937|938|939|940|(0)|943)))|1200|1199|(0)(0)|937|938|939|940|(0)|943))|882)|694|695|(0)(0)|698|(0)(0)|701|(0)|716|(0)(0)|719))|692|693|694|695|(0)(0)|698|(0)(0)|701|(0)|716|(0)(0)|719))|685|686|(0)(0)|692|693|694|695|(0)(0)|698|(0)(0)|701|(0)|716|(0)(0)|719))(1:1273)|1272|1261|(0)|667|(2:669|671)|672|(0)(0)|679|(2:681|1202)|1206|(0)|1216|(0)|1222|(0)|685|686|(0)(0)|692|693|694|695|(0)(0)|698|(0)(0)|701|(0)|716|(0)(0)|719))))|666|667|(0)|672|(0)(0)|679|(0)|1206|(0)|1216|(0)|1222|(0)|685|686|(0)(0)|692|693|694|695|(0)(0)|698|(0)(0)|701|(0)|716|(0)(0)|719)|(2:67|(1:69)(1:599))(1:600)|70|(3:72|(1:74)(1:597)|75)(1:598)|76|(1:78)(1:596)|79|(1:81)|82|(2:84|(1:86)(1:583))(2:584|(2:586|(2:588|(1:590)(1:591))(2:592|(1:594)(1:595))))|87|(2:553|(2:580|(1:582))(2:557|(2:559|(1:561))(2:562|(2:564|(1:566))(2:567|(4:569|(1:571)(1:575)|572|(1:574))))))(2:91|(1:93))|94|95|96|(1:98)|99|(3:101|(1:103)(1:105)|104)|106|(1:108)(1:550)|109|(1:111)|112|(1:549)(1:118)|119|(1:121)(1:548)|122|(1:547)(1:126)|127|128|(4:531|(1:533)(1:545)|534|(2:535|(3:537|(2:539|540)(2:542|543)|541)(1:544)))(8:132|(1:134)(1:530)|135|(1:137)(1:529)|138|(1:140)(1:528)|141|(2:142|(3:144|(2:146|147)(2:149|150)|148)(1:151)))|152|(1:154)|155|(2:157|(1:159)(1:160))|161|(2:163|(1:165)(1:458))(1:(4:(3:470|(1:472)(1:526)|473)(1:527)|(5:475|(1:477)(1:524)|478|(3:480|(1:482)(1:518)|483)(3:519|(1:521)(1:523)|522)|484)(1:525)|485|(2:487|(4:489|(3:491|(1:493)(1:495)|494)|496|(3:498|(1:500)(1:502)|501))(5:503|(3:505|(1:507)(1:509)|508)|510|(3:512|(1:514)(1:516)|515)|517)))(3:463|(2:465|(1:467))|468))|(7:(1:168)|169|(1:171)|172|(1:183)(1:176)|177|(1:181))|184|(1:457)(1:188)|189|(5:191|(1:396)(1:195)|196|(2:197|(1:199)(1:200))|201)(2:397|(41:422|423|(1:454)(5:427|428|429|430|(34:432|433|434|435|(1:445)(1:439)|440|(2:441|(1:443)(1:444))|203|(1:205)|206|207|208|(1:210)(1:394)|211|212|213|214|(1:390)(1:(2:221|(1:384)(1:227)))|228|(3:230|(3:232|(2:241|242)|239)|243)|244|(10:249|(2:251|(1:253))|254|255|256|(6:258|(6:262|(1:264)|265|(1:291)(2:269|(1:271)(2:277|(1:279)(2:280|(3:282|(1:284)(1:286)|285))))|272|(2:274|(1:276)))|292|(4:296|(1:(1:306)(2:298|(1:300)(2:301|302)))|303|(1:305))|307|(2:313|(1:315)))(6:334|(6:338|(1:340)|341|(4:343|(1:345)|346|(1:348))|349|(1:359))|360|(4:364|(1:366)|367|368)|369|(1:373))|316|(3:(1:330)(1:325)|326|(1:328)(1:329))|331|332)|375|(1:378)|379|(1:381)(1:383)|382|255|256|(0)(0)|316|(6:318|320|(1:323)|330|326|(0)(0))|331|332))|449|433|434|435|(1:437)|445|440|(3:441|(0)(0)|443)|203|(0)|206|207|208|(0)(0)|211|212|213|214|(1:216)|386|388|390|228|(0)|244|(11:246|249|(0)|254|255|256|(0)(0)|316|(0)|331|332)|375|(1:378)|379|(0)(0)|382|255|256|(0)(0)|316|(0)|331|332)(2:401|(4:406|(1:416)(1:410)|411|(2:412|(1:414)(1:415)))(1:405)))|202|203|(0)|206|207|208|(0)(0)|211|212|213|214|(0)|386|388|390|228|(0)|244|(0)|375|(0)|379|(0)(0)|382|255|256|(0)(0)|316|(0)|331|332|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:1188:0x0e1d, code lost:
        if (r4 != null) goto L985;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1215:0x058c, code lost:
        if (r4.post_messages == false) goto L685;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1221:0x0598, code lost:
        if (r4.kicked != false) goto L685;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1227:0x05a6, code lost:
        if (r49.isTopic == false) goto L685;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x1a88, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x1a89, code lost:
        r49.messageLayout = null;
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:447:0x1847, code lost:
        r0 = e;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:101:0x12c2 A[Catch: Exception -> 0x139b, TryCatch #3 {Exception -> 0x139b, blocks: (B:96:0x12ac, B:99:0x12b6, B:101:0x12c2, B:104:0x12dc, B:106:0x12e5, B:109:0x12fb, B:111:0x1301, B:112:0x130d, B:114:0x1323, B:116:0x1329, B:119:0x133a, B:121:0x133e, B:122:0x137a, B:124:0x137e, B:126:0x1386, B:127:0x1390, B:548:0x135d), top: B:95:0x12ac }] */
    /* JADX WARN: Removed duplicated region for block: B:1039:0x0aa2  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x12f8  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x1301 A[Catch: Exception -> 0x139b, TryCatch #3 {Exception -> 0x139b, blocks: (B:96:0x12ac, B:99:0x12b6, B:101:0x12c2, B:104:0x12dc, B:106:0x12e5, B:109:0x12fb, B:111:0x1301, B:112:0x130d, B:114:0x1323, B:116:0x1329, B:119:0x133a, B:121:0x133e, B:122:0x137a, B:124:0x137e, B:126:0x1386, B:127:0x1390, B:548:0x135d), top: B:95:0x12ac }] */
    /* JADX WARN: Removed duplicated region for block: B:1208:0x057c  */
    /* JADX WARN: Removed duplicated region for block: B:1218:0x0592  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x133e A[Catch: Exception -> 0x139b, TryCatch #3 {Exception -> 0x139b, blocks: (B:96:0x12ac, B:99:0x12b6, B:101:0x12c2, B:104:0x12dc, B:106:0x12e5, B:109:0x12fb, B:111:0x1301, B:112:0x130d, B:114:0x1323, B:116:0x1329, B:119:0x133a, B:121:0x133e, B:122:0x137a, B:124:0x137e, B:126:0x1386, B:127:0x1390, B:548:0x135d), top: B:95:0x12ac }] */
    /* JADX WARN: Removed duplicated region for block: B:1224:0x059e  */
    /* JADX WARN: Removed duplicated region for block: B:1228:0x0543  */
    /* JADX WARN: Removed duplicated region for block: B:1263:0x04ca  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x1561  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x1570  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x1595  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x175f  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x17cb  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x18c6  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x18e5 A[Catch: Exception -> 0x193b, TryCatch #2 {Exception -> 0x193b, blocks: (B:208:0x18df, B:210:0x18e5, B:394:0x1938), top: B:207:0x18df }] */
    /* JADX WARN: Removed duplicated region for block: B:216:0x194f A[Catch: Exception -> 0x1a88, TryCatch #5 {Exception -> 0x1a88, blocks: (B:214:0x194b, B:216:0x194f, B:219:0x1969, B:221:0x196f, B:223:0x1975, B:225:0x1979, B:227:0x198c, B:228:0x19bb, B:230:0x19bf, B:232:0x19d3, B:234:0x19d9, B:236:0x19dd, B:239:0x19ea, B:241:0x19e7, B:244:0x19ed, B:246:0x19f1, B:249:0x19f6, B:251:0x19fa, B:253:0x1a0d, B:254:0x1a22, B:255:0x1a72, B:375:0x1a3c, B:378:0x1a43, B:379:0x1a4a, B:382:0x1a62, B:384:0x19aa, B:386:0x1953, B:388:0x1957, B:390:0x195c), top: B:213:0x194b }] */
    /* JADX WARN: Removed duplicated region for block: B:230:0x19bf A[Catch: Exception -> 0x1a88, TryCatch #5 {Exception -> 0x1a88, blocks: (B:214:0x194b, B:216:0x194f, B:219:0x1969, B:221:0x196f, B:223:0x1975, B:225:0x1979, B:227:0x198c, B:228:0x19bb, B:230:0x19bf, B:232:0x19d3, B:234:0x19d9, B:236:0x19dd, B:239:0x19ea, B:241:0x19e7, B:244:0x19ed, B:246:0x19f1, B:249:0x19f6, B:251:0x19fa, B:253:0x1a0d, B:254:0x1a22, B:255:0x1a72, B:375:0x1a3c, B:378:0x1a43, B:379:0x1a4a, B:382:0x1a62, B:384:0x19aa, B:386:0x1953, B:388:0x1957, B:390:0x195c), top: B:213:0x194b }] */
    /* JADX WARN: Removed duplicated region for block: B:246:0x19f1 A[Catch: Exception -> 0x1a88, TryCatch #5 {Exception -> 0x1a88, blocks: (B:214:0x194b, B:216:0x194f, B:219:0x1969, B:221:0x196f, B:223:0x1975, B:225:0x1979, B:227:0x198c, B:228:0x19bb, B:230:0x19bf, B:232:0x19d3, B:234:0x19d9, B:236:0x19dd, B:239:0x19ea, B:241:0x19e7, B:244:0x19ed, B:246:0x19f1, B:249:0x19f6, B:251:0x19fa, B:253:0x1a0d, B:254:0x1a22, B:255:0x1a72, B:375:0x1a3c, B:378:0x1a43, B:379:0x1a4a, B:382:0x1a62, B:384:0x19aa, B:386:0x1953, B:388:0x1957, B:390:0x195c), top: B:213:0x194b }] */
    /* JADX WARN: Removed duplicated region for block: B:251:0x19fa A[Catch: Exception -> 0x1a88, TryCatch #5 {Exception -> 0x1a88, blocks: (B:214:0x194b, B:216:0x194f, B:219:0x1969, B:221:0x196f, B:223:0x1975, B:225:0x1979, B:227:0x198c, B:228:0x19bb, B:230:0x19bf, B:232:0x19d3, B:234:0x19d9, B:236:0x19dd, B:239:0x19ea, B:241:0x19e7, B:244:0x19ed, B:246:0x19f1, B:249:0x19f6, B:251:0x19fa, B:253:0x1a0d, B:254:0x1a22, B:255:0x1a72, B:375:0x1a3c, B:378:0x1a43, B:379:0x1a4a, B:382:0x1a62, B:384:0x19aa, B:386:0x1953, B:388:0x1957, B:390:0x195c), top: B:213:0x194b }] */
    /* JADX WARN: Removed duplicated region for block: B:258:0x1aa3  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x1cd4  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x1d11  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x1d19  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x1c22  */
    /* JADX WARN: Removed duplicated region for block: B:377:0x1a41 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:381:0x1a5d  */
    /* JADX WARN: Removed duplicated region for block: B:383:0x1a60  */
    /* JADX WARN: Removed duplicated region for block: B:394:0x1938 A[Catch: Exception -> 0x193b, TRY_LEAVE, TryCatch #2 {Exception -> 0x193b, blocks: (B:208:0x18df, B:210:0x18e5, B:394:0x1938), top: B:207:0x18df }] */
    /* JADX WARN: Removed duplicated region for block: B:397:0x17fa  */
    /* JADX WARN: Removed duplicated region for block: B:437:0x185c  */
    /* JADX WARN: Removed duplicated region for block: B:443:0x186c A[LOOP:8: B:441:0x1867->B:443:0x186c, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:444:0x18c2 A[EDGE_INSN: B:444:0x18c2->B:203:0x18c2 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:459:0x15c3  */
    /* JADX WARN: Removed duplicated region for block: B:533:0x14dc  */
    /* JADX WARN: Removed duplicated region for block: B:537:0x152f  */
    /* JADX WARN: Removed duplicated region for block: B:544:0x155c A[EDGE_INSN: B:544:0x155c->B:152:0x155c ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:545:0x14fa  */
    /* JADX WARN: Removed duplicated region for block: B:548:0x135d A[Catch: Exception -> 0x139b, TryCatch #3 {Exception -> 0x139b, blocks: (B:96:0x12ac, B:99:0x12b6, B:101:0x12c2, B:104:0x12dc, B:106:0x12e5, B:109:0x12fb, B:111:0x1301, B:112:0x130d, B:114:0x1323, B:116:0x1329, B:119:0x133a, B:121:0x133e, B:122:0x137a, B:124:0x137e, B:126:0x1386, B:127:0x1390, B:548:0x135d), top: B:95:0x12ac }] */
    /* JADX WARN: Removed duplicated region for block: B:550:0x12fa  */
    /* JADX WARN: Removed duplicated region for block: B:582:0x1249  */
    /* JADX WARN: Removed duplicated region for block: B:584:0x1183  */
    /* JADX WARN: Removed duplicated region for block: B:596:0x111e  */
    /* JADX WARN: Removed duplicated region for block: B:598:0x1106  */
    /* JADX WARN: Removed duplicated region for block: B:600:0x10c5  */
    /* JADX WARN: Removed duplicated region for block: B:669:0x0518  */
    /* JADX WARN: Removed duplicated region for block: B:674:0x0524  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x1084  */
    /* JADX WARN: Removed duplicated region for block: B:681:0x055c  */
    /* JADX WARN: Removed duplicated region for block: B:688:0x05b0  */
    /* JADX WARN: Removed duplicated region for block: B:697:0x0e34  */
    /* JADX WARN: Removed duplicated region for block: B:700:0x0e5a  */
    /* JADX WARN: Removed duplicated region for block: B:703:0x0fb8  */
    /* JADX WARN: Removed duplicated region for block: B:718:0x100e  */
    /* JADX WARN: Removed duplicated region for block: B:720:0x1020  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x10d3  */
    /* JADX WARN: Removed duplicated region for block: B:746:0x0e6c  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x110b  */
    /* JADX WARN: Removed duplicated region for block: B:810:0x0ee9  */
    /* JADX WARN: Removed duplicated region for block: B:813:0x0ef5  */
    /* JADX WARN: Removed duplicated region for block: B:814:0x0ef8  */
    /* JADX WARN: Removed duplicated region for block: B:815:0x0eee  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x113c  */
    /* JADX WARN: Removed duplicated region for block: B:829:0x0e3c  */
    /* JADX WARN: Removed duplicated region for block: B:836:0x05fe  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x1155  */
    /* JADX WARN: Removed duplicated region for block: B:936:0x08c7  */
    /* JADX WARN: Removed duplicated region for block: B:942:0x0e28  */
    /* JADX WARN: Removed duplicated region for block: B:944:0x08cf  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x12b5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void buildLayout() {
        String str;
        String str2;
        boolean z;
        int i;
        TLRPC$DraftMessage tLRPC$DraftMessage;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Chat chat;
        boolean z2;
        String str3;
        boolean z3;
        String str4;
        CharSequence replaceNewLines;
        CharSequence highlightText;
        String str5;
        String str6;
        String str7;
        String str8;
        TLRPC$TL_forumTopic findTopic;
        int i2;
        SpannableStringBuilder replaceEmoji;
        boolean z4;
        boolean z5;
        CharSequence charSequence;
        String str9;
        CharSequence highlightText2;
        ForegroundColorSpanThemable foregroundColorSpanThemable;
        String str10;
        String string;
        String str11;
        boolean z6;
        SpannableStringBuilder spannableStringBuilder;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        ArrayList<TLRPC$MessagePeerReaction> arrayList;
        String str12;
        String str13;
        ArrayList<TLRPC$MessageEntity> arrayList2;
        CharSequence charSequence2;
        SpannableStringBuilder spannableStringBuilder2;
        SpannableStringBuilder spannableStringBuilder3;
        int i3;
        TLRPC$DraftMessage tLRPC$DraftMessage2;
        MessageObject messageObject;
        String stringForMessageListDate;
        MessageObject messageObject2;
        String str14;
        String str15;
        CharSequence charSequence3;
        String str16;
        SpannableStringBuilder spannableStringBuilder4;
        boolean z7;
        int i4;
        String str17;
        String str18;
        String str19;
        String str20;
        MessageObject messageObject3;
        String str21;
        int i5;
        int i6;
        int dp;
        int measuredWidth;
        int dp2;
        int dp3;
        int i7;
        ImageReceiver[] imageReceiverArr;
        String str22;
        String str23;
        int dp4;
        int i8;
        ImageReceiver[] imageReceiverArr2;
        MessageObject messageObject4;
        CharSequence highlightText3;
        String str24;
        int lineCount;
        StaticLayout staticLayout;
        float primaryHorizontal;
        float primaryHorizontal2;
        int i9;
        int lineCount2;
        boolean z8;
        int i10;
        CharacterStyle[] characterStyleArr;
        CharSequence replaceTwoNewLinesToOne;
        CharSequence highlightText4;
        int dp5;
        int dp6;
        int dp7;
        CharSequence highlightText5;
        String str25;
        String str26;
        SpannableStringBuilder valueOf;
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
        boolean z9 = !UserObject.isUserSelf(this.user) && !this.useMeForMyMessages;
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
        MessageObject messageObject5 = this.message;
        CharSequence charSequence4 = messageObject5 != null ? messageObject5.messageText : null;
        boolean z10 = charSequence4 instanceof Spannable;
        SpannableStringBuilder spannableStringBuilder5 = charSequence4;
        if (z10) {
            SpannableStringBuilder spannableStringBuilder6 = new SpannableStringBuilder(charSequence4);
            for (URLSpanNoUnderlineBold uRLSpanNoUnderlineBold : (URLSpanNoUnderlineBold[]) spannableStringBuilder6.getSpans(0, spannableStringBuilder6.length(), URLSpanNoUnderlineBold.class)) {
                spannableStringBuilder6.removeSpan(uRLSpanNoUnderlineBold);
            }
            for (URLSpanNoUnderline uRLSpanNoUnderline : (URLSpanNoUnderline[]) spannableStringBuilder6.getSpans(0, spannableStringBuilder6.length(), URLSpanNoUnderline.class)) {
                spannableStringBuilder6.removeSpan(uRLSpanNoUnderline);
            }
            spannableStringBuilder5 = spannableStringBuilder6;
        }
        this.lastMessageString = spannableStringBuilder5;
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
                str25 = LocaleController.getString("FromYou", R.string.FromYou);
                CustomDialog customDialog3 = this.customDialog;
                if (customDialog3.isMedia) {
                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                    valueOf = SpannableStringBuilder.valueOf(String.format(str, this.message.messageText));
                    valueOf.setSpan(new ForegroundColorSpanThemable("chats_attachMessage", this.resourcesProvider), 0, valueOf.length(), 33);
                } else {
                    String str27 = customDialog3.message;
                    if (str27.length() > 150) {
                        str27 = str27.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                    }
                    valueOf = (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? SpannableStringBuilder.valueOf(String.format(str, str27, str25)) : SpannableStringBuilder.valueOf(String.format(str, str27.replace('\n', ' '), str25));
                }
                charSequence3 = Emoji.replaceEmoji(valueOf, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                z7 = false;
            } else {
                charSequence3 = customDialog2.message;
                if (customDialog2.isMedia) {
                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                }
                str25 = null;
                z7 = true;
            }
            String stringForMessageListDate2 = LocaleController.stringForMessageListDate(this.customDialog.date);
            int i11 = this.customDialog.unread_count;
            if (i11 != 0) {
                this.drawCount = true;
                str26 = String.format("%d", Integer.valueOf(i11));
            } else {
                this.drawCount = false;
                str26 = null;
            }
            CustomDialog customDialog4 = this.customDialog;
            int i12 = customDialog4.sent;
            if (i12 == 0) {
                this.drawClock = true;
                this.drawCheck1 = false;
                this.drawCheck2 = false;
            } else if (i12 == 2) {
                this.drawCheck1 = true;
                this.drawCheck2 = true;
                this.drawClock = false;
            } else if (i12 == 1) {
                this.drawCheck1 = false;
                this.drawCheck2 = true;
                this.drawClock = false;
            } else {
                this.drawClock = false;
                this.drawCheck1 = false;
                this.drawCheck2 = false;
            }
            this.drawError = false;
            str17 = str25;
            str15 = null;
            spannableStringBuilder4 = null;
            str18 = customDialog4.name;
            z4 = true;
            str14 = str26;
            stringForMessageListDate = stringForMessageListDate2;
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
                    } else {
                        this.drawVerified = !this.forbidVerified && tLRPC$Chat2.verified;
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
                            str2 = "%d";
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
                                i = this.lastMessageDate;
                                if (i == 0 && (messageObject3 = this.message) != null) {
                                    i = messageObject3.messageOwner.date;
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
                                tLRPC$DraftMessage = this.draftMessage;
                                if (tLRPC$DraftMessage != null || ((!TextUtils.isEmpty(tLRPC$DraftMessage.message) || this.draftMessage.reply_to_msg_id != 0) && (i <= this.draftMessage.date || this.unreadCount == 0))) {
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
                                    tLRPC$Chat = this.chat;
                                    if (tLRPC$Chat != null) {
                                        if (tLRPC$Chat.left) {
                                        }
                                    }
                                    if (!this.forbidDraft) {
                                        if (ChatObject.isForum(tLRPC$Chat)) {
                                        }
                                        if (!isForumCell()) {
                                            this.draftMessage = null;
                                            this.needEmoji = true;
                                            updateMessageThumbs();
                                            str9 = getMessageNameString();
                                            CharSequence formatTopicsNames = formatTopicsNames();
                                            spannableStringBuilder = getMessageStringFormatted(str, MessagesController.getRestrictionReason(this.message.messageOwner.restriction_reason), str9, true);
                                            if (spannableStringBuilder.length() >= 0) {
                                                spannableStringBuilder = SpannableStringBuilder.valueOf(spannableStringBuilder);
                                                spannableStringBuilder.setSpan(new ForegroundColorSpanThemable("chats_name", this.resourcesProvider), 0, Math.min(spannableStringBuilder.length(), str9.length() + 1), 0);
                                            }
                                            this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                                            str12 = formatTopicsNames;
                                        } else if (printingString != null) {
                                            this.lastPrintString = printingString;
                                            int intValue = MessagesController.getInstance(this.currentAccount).getPrintingStringType(this.currentDialogId, getTopicId()).intValue();
                                            this.printingStringType = intValue;
                                            StatusDrawable chatStatusDrawable = Theme.getChatStatusDrawable(intValue);
                                            int intrinsicWidth = chatStatusDrawable != null ? chatStatusDrawable.getIntrinsicWidth() + AndroidUtilities.dp(3.0f) : 0;
                                            SpannableStringBuilder spannableStringBuilder7 = new SpannableStringBuilder();
                                            CharSequence replace = TextUtils.replace(printingString, new String[]{"..."}, new String[]{""});
                                            int indexOf = this.printingStringType == 5 ? replace.toString().indexOf("**oo**") : -1;
                                            if (indexOf >= 0) {
                                                spannableStringBuilder7.append(replace).setSpan(new FixedWidthSpan(Theme.getChatStatusDrawable(this.printingStringType).getIntrinsicWidth()), indexOf, indexOf + 6, 0);
                                            } else {
                                                spannableStringBuilder7.append((CharSequence) " ").append(replace).setSpan(new FixedWidthSpan(intrinsicWidth), 0, 1, 0);
                                            }
                                            this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                            str9 = null;
                                            spannableStringBuilder3 = spannableStringBuilder7;
                                            i3 = indexOf;
                                            z6 = false;
                                            spannableStringBuilder = null;
                                            z4 = true;
                                            if (this.draftMessage != null) {
                                                stringForMessageListDate = LocaleController.stringForMessageListDate(tLRPC$DraftMessage2.date);
                                            } else {
                                                int i13 = this.lastMessageDate;
                                                if (i13 != 0) {
                                                    stringForMessageListDate = LocaleController.stringForMessageListDate(i13);
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
                                                str14 = null;
                                                str15 = null;
                                            } else {
                                                if (this.currentDialogFolderId != 0) {
                                                    int i14 = this.unreadCount;
                                                    int i15 = this.mentionCount;
                                                    if (i14 + i15 <= 0) {
                                                        this.drawCount = false;
                                                        this.drawMention = false;
                                                        str14 = null;
                                                    } else if (i14 > i15) {
                                                        this.drawCount = true;
                                                        this.drawMention = false;
                                                        str14 = String.format(str2, Integer.valueOf(i14 + i15));
                                                    } else {
                                                        this.drawCount = false;
                                                        this.drawMention = true;
                                                        str15 = String.format(str2, Integer.valueOf(i14 + i15));
                                                        str14 = null;
                                                        this.drawReactionMention = false;
                                                    }
                                                    str15 = null;
                                                    this.drawReactionMention = false;
                                                } else {
                                                    String str28 = str2;
                                                    if (this.clearingDialog) {
                                                        this.drawCount = false;
                                                        z9 = false;
                                                    } else {
                                                        int i16 = this.unreadCount;
                                                        if (i16 != 0 && (i16 != 1 || i16 != this.mentionCount || messageObject2 == null || !messageObject2.messageOwner.mentioned)) {
                                                            this.drawCount = true;
                                                            str14 = String.format(str28, Integer.valueOf(i16));
                                                        } else if (this.markUnread) {
                                                            this.drawCount = true;
                                                            str14 = "";
                                                        } else {
                                                            this.drawCount = false;
                                                        }
                                                        if (this.mentionCount == 0) {
                                                            this.drawMention = true;
                                                            str15 = "@";
                                                        } else {
                                                            this.drawMention = false;
                                                            str15 = null;
                                                        }
                                                        if (this.reactionMentionCount <= 0) {
                                                            this.drawReactionMention = true;
                                                        } else {
                                                            this.drawReactionMention = false;
                                                        }
                                                    }
                                                    str14 = null;
                                                    if (this.mentionCount == 0) {
                                                    }
                                                    if (this.reactionMentionCount <= 0) {
                                                    }
                                                }
                                                if (this.message.isOut() && this.draftMessage == null && z9) {
                                                    MessageObject messageObject6 = this.message;
                                                    if (!(messageObject6.messageOwner.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                                                        if (messageObject6.isSending()) {
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
                                                                int i17 = this.readOutboxMaxId;
                                                                this.drawCheck1 = (i17 > 0 && i17 >= this.message.getId()) || !this.message.isUnread() || (ChatObject.isChannel(this.chat) && !this.chat.megagroup);
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
                                            MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                                            String str29 = spannableStringBuilder3;
                                            if (this.dialogsType == 0) {
                                                str29 = spannableStringBuilder3;
                                                if (messagesController.isPromoDialog(this.currentDialogId, true)) {
                                                    this.drawPinBackground = true;
                                                    this.promoDialog = true;
                                                    int i18 = messagesController.promoDialogType;
                                                    if (i18 == MessagesController.PROMO_TYPE_PROXY) {
                                                        stringForMessageListDate = LocaleController.getString("UseProxySponsor", R.string.UseProxySponsor);
                                                        str29 = spannableStringBuilder3;
                                                    } else {
                                                        str29 = spannableStringBuilder3;
                                                        if (i18 == MessagesController.PROMO_TYPE_PSA) {
                                                            stringForMessageListDate = LocaleController.getString("PsaType_" + messagesController.promoPsaType);
                                                            if (TextUtils.isEmpty(stringForMessageListDate)) {
                                                                stringForMessageListDate = LocaleController.getString("PsaTypeDefault", R.string.PsaTypeDefault);
                                                            }
                                                            str29 = spannableStringBuilder3;
                                                            if (!TextUtils.isEmpty(messagesController.promoPsaMessage)) {
                                                                String str30 = messagesController.promoPsaMessage;
                                                                this.thumbsCount = 0;
                                                                str29 = str30;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            charSequence3 = str29;
                                            if (this.currentDialogFolderId != 0) {
                                                str16 = LocaleController.getString("ArchivedChats", R.string.ArchivedChats);
                                            } else {
                                                TLRPC$Chat tLRPC$Chat4 = this.chat;
                                                if (tLRPC$Chat4 != null) {
                                                    if (this.isTopic) {
                                                        str16 = this.forumTopic.title;
                                                    } else {
                                                        str16 = tLRPC$Chat4.title;
                                                    }
                                                } else {
                                                    TLRPC$User tLRPC$User2 = this.user;
                                                    if (tLRPC$User2 == null) {
                                                        str16 = "";
                                                    } else if (UserObject.isReplyUser(tLRPC$User2)) {
                                                        str16 = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                                                    } else if (UserObject.isUserSelf(this.user)) {
                                                        if (this.useMeForMyMessages) {
                                                            str16 = LocaleController.getString("FromYou", R.string.FromYou);
                                                        } else {
                                                            if (this.dialogsType == 3) {
                                                                this.drawPinBackground = true;
                                                            }
                                                            str16 = LocaleController.getString("SavedMessages", R.string.SavedMessages);
                                                        }
                                                    } else {
                                                        str16 = UserObject.getUserName(this.user);
                                                    }
                                                }
                                                if (str16 != null && str16.length() == 0) {
                                                    str16 = LocaleController.getString("HiddenName", R.string.HiddenName);
                                                }
                                            }
                                            spannableStringBuilder4 = spannableStringBuilder;
                                            z7 = z6;
                                            i4 = i3;
                                            String str31 = str16;
                                            str17 = str9;
                                            str18 = str31;
                                        } else {
                                            this.lastPrintString = null;
                                            if (this.draftMessage != null) {
                                                str9 = LocaleController.getString("Draft", R.string.Draft);
                                                if (TextUtils.isEmpty(this.draftMessage.message)) {
                                                    if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                                        spannableStringBuilder = null;
                                                        charSequence2 = "";
                                                        z4 = true;
                                                        z6 = false;
                                                        str19 = charSequence2;
                                                    } else {
                                                        SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(str9);
                                                        valueOf2.setSpan(new ForegroundColorSpanThemable("chats_draft", this.resourcesProvider), 0, str9.length(), 33);
                                                        spannableStringBuilder2 = valueOf2;
                                                    }
                                                } else {
                                                    String str32 = this.draftMessage.message;
                                                    if (str32.length() > 150) {
                                                        str32 = str32.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                    }
                                                    SpannableStringBuilder spannableStringBuilder8 = new SpannableStringBuilder(str32);
                                                    MediaDataController.addTextStyleRuns(this.draftMessage, spannableStringBuilder8, 256);
                                                    TLRPC$DraftMessage tLRPC$DraftMessage3 = this.draftMessage;
                                                    if (tLRPC$DraftMessage3 != null && (arrayList2 = tLRPC$DraftMessage3.entities) != null) {
                                                        TextPaint textPaint5 = this.currentMessagePaint;
                                                        MediaDataController.addAnimatedEmojiSpans(arrayList2, spannableStringBuilder8, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                    }
                                                    SpannableStringBuilder formatSpannable = AndroidUtilities.formatSpannable(str, AndroidUtilities.replaceNewLines(spannableStringBuilder8), str9);
                                                    if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                                        formatSpannable.setSpan(new ForegroundColorSpanThemable("chats_draft", this.resourcesProvider), 0, str9.length() + 1, 33);
                                                    }
                                                    spannableStringBuilder2 = Emoji.replaceEmoji(formatSpannable, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                }
                                                spannableStringBuilder = null;
                                                charSequence2 = spannableStringBuilder2;
                                                z4 = true;
                                                z6 = false;
                                                str19 = charSequence2;
                                            } else {
                                                if (this.clearingDialog) {
                                                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                    str13 = LocaleController.getString("HistoryCleared", R.string.HistoryCleared);
                                                } else {
                                                    MessageObject messageObject7 = this.message;
                                                    if (messageObject7 == null) {
                                                        TLRPC$EncryptedChat tLRPC$EncryptedChat = this.encryptedChat;
                                                        if (tLRPC$EncryptedChat != null) {
                                                            this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                            if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatRequested) {
                                                                str13 = LocaleController.getString("EncryptionProcessing", R.string.EncryptionProcessing);
                                                            } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatWaiting) {
                                                                str13 = LocaleController.formatString("AwaitingEncryption", R.string.AwaitingEncryption, UserObject.getFirstName(this.user));
                                                            } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatDiscarded) {
                                                                str13 = LocaleController.getString("EncryptionRejected", R.string.EncryptionRejected);
                                                            } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat) {
                                                                if (tLRPC$EncryptedChat.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                                    str13 = LocaleController.formatString("EncryptedChatStartedOutgoing", R.string.EncryptedChatStartedOutgoing, UserObject.getFirstName(this.user));
                                                                } else {
                                                                    str13 = LocaleController.getString("EncryptedChatStartedIncoming", R.string.EncryptedChatStartedIncoming);
                                                                }
                                                            }
                                                        } else if (this.dialogsType == 3 && UserObject.isUserSelf(this.user)) {
                                                            str9 = null;
                                                            spannableStringBuilder = null;
                                                            z4 = false;
                                                            z9 = false;
                                                            str20 = LocaleController.getString("SavedMessagesInfo", R.string.SavedMessagesInfo);
                                                            z6 = true;
                                                            str19 = str20;
                                                        }
                                                        str9 = null;
                                                        spannableStringBuilder = null;
                                                        str12 = "";
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
                                                                long j3 = tLRPC$MessagePeerReaction.peer_id.user_id;
                                                                if (j3 != 0) {
                                                                    z2 = z9;
                                                                    if (j3 != UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                                        ReactionsLayoutInBubble.VisibleReaction fromTLReaction = ReactionsLayoutInBubble.VisibleReaction.fromTLReaction(tLRPC$MessagePeerReaction.reaction);
                                                                        this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                        String str33 = fromTLReaction.emojicon;
                                                                        if (str33 != null) {
                                                                            str3 = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, str33);
                                                                        } else {
                                                                            String formatString = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, "**reaction**");
                                                                            int indexOf2 = formatString.indexOf("**reaction**");
                                                                            SpannableStringBuilder spannableStringBuilder9 = new SpannableStringBuilder(formatString.replace("**reaction**", "d"));
                                                                            long j4 = fromTLReaction.documentId;
                                                                            TextPaint textPaint6 = this.currentMessagePaint;
                                                                            spannableStringBuilder9.setSpan(new AnimatedEmojiSpan(j4, textPaint6 == null ? null : textPaint6.getFontMetricsInt()), indexOf2, indexOf2 + 1, 0);
                                                                            str3 = spannableStringBuilder9;
                                                                        }
                                                                        z3 = true;
                                                                        if (z3) {
                                                                            str4 = str3;
                                                                        } else {
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
                                                                                                string = LocaleController.getString("ChannelPrivate", R.string.ChannelPrivate).toLowerCase();
                                                                                            } else {
                                                                                                string = LocaleController.getString("ChannelPublic", R.string.ChannelPublic).toLowerCase();
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    TLRPC$Chat tLRPC$Chat7 = this.chat;
                                                                                    int i21 = tLRPC$Chat7.participants_count;
                                                                                    if (i21 != 0) {
                                                                                        string = LocaleController.formatPluralStringComma("Members", i21);
                                                                                    } else if (tLRPC$Chat7.has_geo) {
                                                                                        string = LocaleController.getString("MegaLocation", R.string.MegaLocation);
                                                                                    } else if (!ChatObject.isPublic(tLRPC$Chat7)) {
                                                                                        string = LocaleController.getString("MegaPrivate", R.string.MegaPrivate).toLowerCase();
                                                                                    } else {
                                                                                        string = LocaleController.getString("MegaPublic", R.string.MegaPublic).toLowerCase();
                                                                                    }
                                                                                } else {
                                                                                    string = "";
                                                                                }
                                                                                this.drawCount2 = false;
                                                                            } else if (i19 == 3 && UserObject.isUserSelf(this.user)) {
                                                                                string = LocaleController.getString("SavedMessagesInfo", R.string.SavedMessagesInfo);
                                                                            } else {
                                                                                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout && this.currentDialogFolderId != 0) {
                                                                                    replaceNewLines = formatArchivedDialogNames();
                                                                                } else {
                                                                                    MessageObject messageObject8 = this.message;
                                                                                    if ((messageObject8.messageOwner instanceof TLRPC$TL_messageService) && (!MessageObject.isTopicActionMessage(messageObject8) || (this.message.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate))) {
                                                                                        if (!ChatObject.isChannelAndNotMegaGroup(this.chat) || !(this.message.messageOwner.action instanceof TLRPC$TL_messageActionChannelMigrateFrom)) {
                                                                                            z9 = z2;
                                                                                        } else {
                                                                                            spannableStringBuilder5 = "";
                                                                                            z9 = false;
                                                                                        }
                                                                                        this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                        str10 = spannableStringBuilder5;
                                                                                        str9 = null;
                                                                                        z4 = true;
                                                                                        str11 = str10;
                                                                                        z5 = true;
                                                                                        charSequence = str11;
                                                                                        if (this.currentDialogFolderId != 0) {
                                                                                        }
                                                                                        z6 = z5;
                                                                                        spannableStringBuilder = null;
                                                                                        str19 = charSequence;
                                                                                    } else {
                                                                                        this.needEmoji = true;
                                                                                        updateMessageThumbs();
                                                                                        TLRPC$Chat tLRPC$Chat8 = this.chat;
                                                                                        if (tLRPC$Chat8 != null && tLRPC$Chat8.id > 0 && chat == null && ((!ChatObject.isChannel(tLRPC$Chat8) || ChatObject.isMegagroup(this.chat)) && !ForumUtilities.isTopicCreateMessage(this.message))) {
                                                                                            String messageNameString = getMessageNameString();
                                                                                            if (this.chat.forum && !this.isTopic) {
                                                                                                CharSequence topicIconName = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopicIconName(this.chat, this.message, this.currentMessagePaint);
                                                                                                if (!TextUtils.isEmpty(topicIconName)) {
                                                                                                    SpannableStringBuilder spannableStringBuilder10 = new SpannableStringBuilder("-");
                                                                                                    ColoredImageSpan coloredImageSpan = new ColoredImageSpan(ContextCompat.getDrawable(ApplicationLoader.applicationContext, R.drawable.msg_mini_forumarrow).mutate());
                                                                                                    coloredImageSpan.setColorKey((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? null : "chats_nameMessage");
                                                                                                    spannableStringBuilder10.setSpan(coloredImageSpan, 0, 1, 0);
                                                                                                    SpannableStringBuilder spannableStringBuilder11 = new SpannableStringBuilder();
                                                                                                    spannableStringBuilder11.append((CharSequence) messageNameString).append((CharSequence) spannableStringBuilder10).append(topicIconName);
                                                                                                    messageNameString = spannableStringBuilder11;
                                                                                                }
                                                                                            }
                                                                                            SpannableStringBuilder messageStringFormatted = getMessageStringFormatted(str, restrictionReason, messageNameString, false);
                                                                                            if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && (this.currentDialogFolderId == 0 || messageStringFormatted.length() <= 0)) {
                                                                                                i2 = 0;
                                                                                            } else {
                                                                                                try {
                                                                                                    foregroundColorSpanThemable = new ForegroundColorSpanThemable("chats_nameMessage", this.resourcesProvider);
                                                                                                    i2 = messageNameString.length() + 1;
                                                                                                } catch (Exception e) {
                                                                                                    e = e;
                                                                                                    i2 = 0;
                                                                                                }
                                                                                                try {
                                                                                                    messageStringFormatted.setSpan(foregroundColorSpanThemable, 0, i2, 33);
                                                                                                } catch (Exception e2) {
                                                                                                    e = e2;
                                                                                                    FileLog.e(e);
                                                                                                    replaceEmoji = Emoji.replaceEmoji(messageStringFormatted, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                    if (this.message.hasHighlightedWords()) {
                                                                                                        replaceEmoji = highlightText2;
                                                                                                    }
                                                                                                    if (this.thumbsCount > 0) {
                                                                                                    }
                                                                                                    z9 = z2;
                                                                                                    z4 = true;
                                                                                                    z5 = false;
                                                                                                    String str34 = messageNameString;
                                                                                                    charSequence = replaceEmoji;
                                                                                                    str9 = str34;
                                                                                                    if (this.currentDialogFolderId != 0) {
                                                                                                    }
                                                                                                    z6 = z5;
                                                                                                    spannableStringBuilder = null;
                                                                                                    str19 = charSequence;
                                                                                                    i3 = -1;
                                                                                                    spannableStringBuilder3 = str19;
                                                                                                    if (this.draftMessage != null) {
                                                                                                    }
                                                                                                    messageObject2 = this.message;
                                                                                                    if (messageObject2 == null) {
                                                                                                    }
                                                                                                    this.promoDialog = false;
                                                                                                    MessagesController messagesController2 = MessagesController.getInstance(this.currentAccount);
                                                                                                    String str292 = spannableStringBuilder3;
                                                                                                    if (this.dialogsType == 0) {
                                                                                                    }
                                                                                                    charSequence3 = str292;
                                                                                                    if (this.currentDialogFolderId != 0) {
                                                                                                    }
                                                                                                    spannableStringBuilder4 = spannableStringBuilder;
                                                                                                    z7 = z6;
                                                                                                    i4 = i3;
                                                                                                    String str312 = str16;
                                                                                                    str17 = str9;
                                                                                                    str18 = str312;
                                                                                                    if (!z4) {
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
                                                                                                    int dp8 = AndroidUtilities.dp(6.0f) + Theme.dialogs_muteDrawable.getIntrinsicWidth();
                                                                                                    this.nameWidth -= dp8;
                                                                                                    if (LocaleController.isRTL) {
                                                                                                    }
                                                                                                    dp7 = this.nameWidth - AndroidUtilities.dp(12.0f);
                                                                                                    if (dp7 < 0) {
                                                                                                    }
                                                                                                    CharSequence replace2 = str18.replace('\n', ' ');
                                                                                                    if (this.nameLayoutEllipsizeByGradient) {
                                                                                                    }
                                                                                                    float f = dp7;
                                                                                                    this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(replace2.toString()) <= f;
                                                                                                    if (!this.twoLinesForName) {
                                                                                                    }
                                                                                                    CharSequence replaceEmoji2 = Emoji.replaceEmoji(replace2, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                    MessageObject messageObject9 = this.message;
                                                                                                    if (messageObject9 != null) {
                                                                                                    }
                                                                                                    if (!this.twoLinesForName) {
                                                                                                    }
                                                                                                    this.nameLayoutTranslateX = (this.nameLayoutEllipsizeByGradient || !this.nameLayout.isRtlCharAt(0)) ? 0.0f : -AndroidUtilities.dp(36.0f);
                                                                                                    this.nameLayoutEllipsizeLeft = this.nameLayout.isRtlCharAt(0);
                                                                                                    if (!this.useForceThreeLines) {
                                                                                                    }
                                                                                                    dp = AndroidUtilities.dp(11.0f);
                                                                                                    this.messageNameTop = AndroidUtilities.dp(32.0f);
                                                                                                    this.timeTop = AndroidUtilities.dp(13.0f);
                                                                                                    this.errorTop = AndroidUtilities.dp(43.0f);
                                                                                                    this.pinTop = AndroidUtilities.dp(43.0f);
                                                                                                    this.countTop = AndroidUtilities.dp(43.0f);
                                                                                                    this.checkDrawTop = AndroidUtilities.dp(13.0f);
                                                                                                    measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 21);
                                                                                                    if (!LocaleController.isRTL) {
                                                                                                    }
                                                                                                    this.avatarImage.setImageCoords(dp2, dp, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                                                                                                    i7 = 0;
                                                                                                    while (true) {
                                                                                                        imageReceiverArr = this.thumbImage;
                                                                                                        if (i7 < imageReceiverArr.length) {
                                                                                                        }
                                                                                                        imageReceiverArr[i7].setImageCoords(((this.thumbSize + 2) * i7) + dp3, AndroidUtilities.dp(31.0f) + dp + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                                                                                                        i7++;
                                                                                                    }
                                                                                                    int i22 = dp;
                                                                                                    if (this.twoLinesForName) {
                                                                                                    }
                                                                                                    if (getIsPinned()) {
                                                                                                    }
                                                                                                    if (!this.drawError) {
                                                                                                    }
                                                                                                    if (z7) {
                                                                                                    }
                                                                                                    int max = Math.max(AndroidUtilities.dp(12.0f), measuredWidth);
                                                                                                    this.buttonTop = AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 58.0f : 62.0f);
                                                                                                    if (!isForumCell()) {
                                                                                                    }
                                                                                                    str23 = str24;
                                                                                                    if (this.twoLinesForName) {
                                                                                                    }
                                                                                                    this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack2, this.messageNameLayout);
                                                                                                    if (TextUtils.isEmpty(spannableStringBuilder4)) {
                                                                                                    }
                                                                                                    this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack3, this.buttonLayout);
                                                                                                    z8 = this.useForceThreeLines;
                                                                                                    if (!z8) {
                                                                                                    }
                                                                                                    this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                                                                                                    charSequence3 = str23;
                                                                                                    str23 = null;
                                                                                                    if (charSequence3 instanceof Spannable) {
                                                                                                    }
                                                                                                    if (!this.useForceThreeLines) {
                                                                                                    }
                                                                                                    if (this.thumbsCount > 0) {
                                                                                                    }
                                                                                                    this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence3, this.currentMessagePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, str23 == null ? 1 : 2);
                                                                                                    this.spoilersPool.addAll(this.spoilers);
                                                                                                    this.spoilers.clear();
                                                                                                    SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
                                                                                                    this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                                                                                                    if (!LocaleController.isRTL) {
                                                                                                    }
                                                                                                    staticLayout = this.messageLayout;
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
                                                                                                boolean z11 = replaceEmoji instanceof SpannableStringBuilder;
                                                                                                replaceEmoji = replaceEmoji;
                                                                                                if (!z11) {
                                                                                                    replaceEmoji = new SpannableStringBuilder(replaceEmoji);
                                                                                                }
                                                                                                SpannableStringBuilder spannableStringBuilder12 = (SpannableStringBuilder) replaceEmoji;
                                                                                                if (i2 >= spannableStringBuilder12.length()) {
                                                                                                    spannableStringBuilder12.append((CharSequence) " ");
                                                                                                    spannableStringBuilder12.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 5)), spannableStringBuilder12.length() - 1, spannableStringBuilder12.length(), 33);
                                                                                                } else {
                                                                                                    spannableStringBuilder12.insert(i2, (CharSequence) " ");
                                                                                                    spannableStringBuilder12.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 5)), i2, i2 + 1, 33);
                                                                                                }
                                                                                            }
                                                                                            z9 = z2;
                                                                                            z4 = true;
                                                                                            z5 = false;
                                                                                            String str342 = messageNameString;
                                                                                            charSequence = replaceEmoji;
                                                                                            str9 = str342;
                                                                                            if (this.currentDialogFolderId != 0) {
                                                                                                str9 = formatArchivedDialogNames();
                                                                                            }
                                                                                            z6 = z5;
                                                                                            spannableStringBuilder = null;
                                                                                            str19 = charSequence;
                                                                                        } else {
                                                                                            boolean isEmpty = TextUtils.isEmpty(restrictionReason);
                                                                                            String str35 = restrictionReason;
                                                                                            if (isEmpty) {
                                                                                                if (MessageObject.isTopicActionMessage(this.message)) {
                                                                                                    MessageObject messageObject10 = this.message;
                                                                                                    CharSequence charSequence5 = messageObject10.messageTextShort;
                                                                                                    if (charSequence5 == null || ((messageObject10.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate) && this.isTopic)) {
                                                                                                        charSequence5 = messageObject10.messageText;
                                                                                                    }
                                                                                                    if (messageObject10.topicIconDrawable[0] != null && (findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-this.message.getDialogId(), MessageObject.getTopicId(this.message.messageOwner))) != null) {
                                                                                                        this.message.topicIconDrawable[0].setColor(findTopic.icon_color);
                                                                                                    }
                                                                                                    str35 = charSequence5;
                                                                                                } else {
                                                                                                    TLRPC$MessageMedia tLRPC$MessageMedia = this.message.messageOwner.media;
                                                                                                    if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia.ttl_seconds != 0) {
                                                                                                        str35 = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
                                                                                                    } else if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) && (tLRPC$MessageMedia.document instanceof TLRPC$TL_documentEmpty) && tLRPC$MessageMedia.ttl_seconds != 0) {
                                                                                                        str35 = LocaleController.getString("AttachVideoExpired", R.string.AttachVideoExpired);
                                                                                                    } else if (getCaptionMessage() != null) {
                                                                                                        MessageObject captionMessage = getCaptionMessage();
                                                                                                        if (!this.needEmoji) {
                                                                                                            str8 = "";
                                                                                                        } else if (captionMessage.isVideo()) {
                                                                                                            str8 = "📹 ";
                                                                                                        } else if (captionMessage.isVoice()) {
                                                                                                            str8 = "🎤 ";
                                                                                                        } else if (captionMessage.isMusic()) {
                                                                                                            str8 = "🎧 ";
                                                                                                        } else {
                                                                                                            str8 = captionMessage.isPhoto() ? "🖼 " : "📎 ";
                                                                                                        }
                                                                                                        if (captionMessage.hasHighlightedWords() && !TextUtils.isEmpty(captionMessage.messageOwner.message)) {
                                                                                                            String str36 = captionMessage.messageTrimmedToHighlight;
                                                                                                            int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) + 24);
                                                                                                            if (this.hasNameInMessage) {
                                                                                                                if (!TextUtils.isEmpty(null)) {
                                                                                                                    throw null;
                                                                                                                }
                                                                                                                measuredWidth2 = (int) (measuredWidth2 - this.currentMessagePaint.measureText(": "));
                                                                                                            }
                                                                                                            if (measuredWidth2 > 0) {
                                                                                                                str36 = AndroidUtilities.ellipsizeCenterEnd(str36, captionMessage.highlightedWords.get(0), measuredWidth2, this.currentMessagePaint, 130).toString();
                                                                                                            }
                                                                                                            str35 = str8 + str36;
                                                                                                        } else {
                                                                                                            SpannableStringBuilder spannableStringBuilder13 = new SpannableStringBuilder(captionMessage.caption);
                                                                                                            TLRPC$Message tLRPC$Message = captionMessage.messageOwner;
                                                                                                            if (tLRPC$Message != null) {
                                                                                                                MediaDataController.addTextStyleRuns(tLRPC$Message.entities, captionMessage.caption, spannableStringBuilder13, 256);
                                                                                                                ArrayList<TLRPC$MessageEntity> arrayList3 = captionMessage.messageOwner.entities;
                                                                                                                TextPaint textPaint7 = this.currentMessagePaint;
                                                                                                                MediaDataController.addAnimatedEmojiSpans(arrayList3, spannableStringBuilder13, textPaint7 == null ? null : textPaint7.getFontMetricsInt());
                                                                                                            }
                                                                                                            str35 = new SpannableStringBuilder(str8).append((CharSequence) spannableStringBuilder13);
                                                                                                        }
                                                                                                    } else if (this.thumbsCount > 1) {
                                                                                                        if (this.hasVideoThumb) {
                                                                                                            ArrayList<MessageObject> arrayList4 = this.groupMessages;
                                                                                                            str7 = LocaleController.formatPluralString("Media", arrayList4 == null ? 0 : arrayList4.size(), new Object[0]);
                                                                                                        } else {
                                                                                                            ArrayList<MessageObject> arrayList5 = this.groupMessages;
                                                                                                            str7 = LocaleController.formatPluralString("Photos", arrayList5 == null ? 0 : arrayList5.size(), new Object[0]);
                                                                                                        }
                                                                                                        this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                        str35 = str7;
                                                                                                    } else {
                                                                                                        MessageObject messageObject11 = this.message;
                                                                                                        TLRPC$MessageMedia tLRPC$MessageMedia2 = messageObject11.messageOwner.media;
                                                                                                        if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPoll) {
                                                                                                            str6 = "📊 " + ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia2).poll.question;
                                                                                                        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGame) {
                                                                                                            str6 = "🎮 " + this.message.messageOwner.media.game.title;
                                                                                                        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                                            str6 = tLRPC$MessageMedia2.title;
                                                                                                        } else if (messageObject11.type == 14) {
                                                                                                            str6 = String.format("🎧 %s - %s", messageObject11.getMusicAuthor(), this.message.getMusicTitle());
                                                                                                        } else {
                                                                                                            if (messageObject11.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                                str5 = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 23), this.currentMessagePaint, 130).toString();
                                                                                                            } else {
                                                                                                                SpannableStringBuilder spannableStringBuilder14 = new SpannableStringBuilder(spannableStringBuilder5);
                                                                                                                MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder14, 256);
                                                                                                                MessageObject messageObject12 = this.message;
                                                                                                                str5 = spannableStringBuilder14;
                                                                                                                if (messageObject12 != null) {
                                                                                                                    TLRPC$Message tLRPC$Message2 = messageObject12.messageOwner;
                                                                                                                    str5 = spannableStringBuilder14;
                                                                                                                    if (tLRPC$Message2 != null) {
                                                                                                                        ArrayList<TLRPC$MessageEntity> arrayList6 = tLRPC$Message2.entities;
                                                                                                                        TextPaint textPaint8 = this.currentMessagePaint;
                                                                                                                        MediaDataController.addAnimatedEmojiSpans(arrayList6, spannableStringBuilder14, textPaint8 == null ? null : textPaint8.getFontMetricsInt());
                                                                                                                        str5 = spannableStringBuilder14;
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                            AndroidUtilities.highlightText(str5, this.message.highlightedWords, this.resourcesProvider);
                                                                                                            str6 = str5;
                                                                                                        }
                                                                                                        MessageObject messageObject13 = this.message;
                                                                                                        str35 = str6;
                                                                                                        if (messageObject13.messageOwner.media != null) {
                                                                                                            str35 = str6;
                                                                                                            if (!messageObject13.isMediaEmpty()) {
                                                                                                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                                str35 = str6;
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            if (this.thumbsCount > 0) {
                                                                                                if (this.message.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                    replaceNewLines = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp((((this.messagePaddingStart + 23) + ((this.thumbSize + 2) * this.thumbsCount)) - 2) + 5), this.currentMessagePaint, 130).toString();
                                                                                                } else {
                                                                                                    int length = str35.length();
                                                                                                    String str37 = str35;
                                                                                                    if (length > 150) {
                                                                                                        str37 = str35.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                                    }
                                                                                                    replaceNewLines = AndroidUtilities.replaceNewLines(str37);
                                                                                                }
                                                                                                if (!(replaceNewLines instanceof SpannableStringBuilder)) {
                                                                                                    replaceNewLines = new SpannableStringBuilder(replaceNewLines);
                                                                                                }
                                                                                                SpannableStringBuilder spannableStringBuilder15 = (SpannableStringBuilder) replaceNewLines;
                                                                                                spannableStringBuilder15.insert(0, (CharSequence) " ");
                                                                                                spannableStringBuilder15.setSpan(new FixedWidthSpan(AndroidUtilities.dp((((this.thumbSize + 2) * this.thumbsCount) - 2) + 5)), 0, 1, 33);
                                                                                                Emoji.replaceEmoji(spannableStringBuilder15, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
                                                                                                if (this.message.hasHighlightedWords()) {
                                                                                                    highlightText = AndroidUtilities.highlightText(spannableStringBuilder15, this.message.highlightedWords, this.resourcesProvider);
                                                                                                }
                                                                                            } else {
                                                                                                str4 = str35;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                highlightText = replaceNewLines;
                                                                                z9 = z2;
                                                                                str9 = null;
                                                                                z4 = true;
                                                                                z5 = false;
                                                                                charSequence = highlightText;
                                                                                if (this.currentDialogFolderId != 0) {
                                                                                }
                                                                                z6 = z5;
                                                                                spannableStringBuilder = null;
                                                                                str19 = charSequence;
                                                                            }
                                                                            str11 = string;
                                                                            str9 = null;
                                                                            z4 = false;
                                                                            z9 = false;
                                                                            z5 = true;
                                                                            charSequence = str11;
                                                                            if (this.currentDialogFolderId != 0) {
                                                                            }
                                                                            z6 = z5;
                                                                            spannableStringBuilder = null;
                                                                            str19 = charSequence;
                                                                        }
                                                                        z9 = z2;
                                                                        str10 = str4;
                                                                        str9 = null;
                                                                        z4 = true;
                                                                        str11 = str10;
                                                                        z5 = true;
                                                                        charSequence = str11;
                                                                        if (this.currentDialogFolderId != 0) {
                                                                        }
                                                                        z6 = z5;
                                                                        spannableStringBuilder = null;
                                                                        str19 = charSequence;
                                                                    }
                                                                    str3 = "";
                                                                    z3 = false;
                                                                    if (z3) {
                                                                    }
                                                                    z9 = z2;
                                                                    str10 = str4;
                                                                    str9 = null;
                                                                    z4 = true;
                                                                    str11 = str10;
                                                                    z5 = true;
                                                                    charSequence = str11;
                                                                    if (this.currentDialogFolderId != 0) {
                                                                    }
                                                                    z6 = z5;
                                                                    spannableStringBuilder = null;
                                                                    str19 = charSequence;
                                                                }
                                                            }
                                                        }
                                                        z2 = z9;
                                                        str3 = "";
                                                        z3 = false;
                                                        if (z3) {
                                                        }
                                                        z9 = z2;
                                                        str10 = str4;
                                                        str9 = null;
                                                        z4 = true;
                                                        str11 = str10;
                                                        z5 = true;
                                                        charSequence = str11;
                                                        if (this.currentDialogFolderId != 0) {
                                                        }
                                                        z6 = z5;
                                                        spannableStringBuilder = null;
                                                        str19 = charSequence;
                                                    }
                                                }
                                                str9 = null;
                                                spannableStringBuilder = null;
                                                str12 = str13;
                                            }
                                            i3 = -1;
                                            spannableStringBuilder3 = str19;
                                            if (this.draftMessage != null) {
                                            }
                                            messageObject2 = this.message;
                                            if (messageObject2 == null) {
                                            }
                                            this.promoDialog = false;
                                            MessagesController messagesController22 = MessagesController.getInstance(this.currentAccount);
                                            String str2922 = spannableStringBuilder3;
                                            if (this.dialogsType == 0) {
                                            }
                                            charSequence3 = str2922;
                                            if (this.currentDialogFolderId != 0) {
                                            }
                                            spannableStringBuilder4 = spannableStringBuilder;
                                            z7 = z6;
                                            i4 = i3;
                                            String str3122 = str16;
                                            str17 = str9;
                                            str18 = str3122;
                                        }
                                        z4 = true;
                                        str20 = str12;
                                        z6 = true;
                                        str19 = str20;
                                        i3 = -1;
                                        spannableStringBuilder3 = str19;
                                        if (this.draftMessage != null) {
                                        }
                                        messageObject2 = this.message;
                                        if (messageObject2 == null) {
                                        }
                                        this.promoDialog = false;
                                        MessagesController messagesController222 = MessagesController.getInstance(this.currentAccount);
                                        String str29222 = spannableStringBuilder3;
                                        if (this.dialogsType == 0) {
                                        }
                                        charSequence3 = str29222;
                                        if (this.currentDialogFolderId != 0) {
                                        }
                                        spannableStringBuilder4 = spannableStringBuilder;
                                        z7 = z6;
                                        i4 = i3;
                                        String str31222 = str16;
                                        str17 = str9;
                                        str18 = str31222;
                                    }
                                }
                                this.draftMessage = null;
                                if (!isForumCell()) {
                                }
                                z4 = true;
                                str20 = str12;
                                z6 = true;
                                str19 = str20;
                                i3 = -1;
                                spannableStringBuilder3 = str19;
                                if (this.draftMessage != null) {
                                }
                                messageObject2 = this.message;
                                if (messageObject2 == null) {
                                }
                                this.promoDialog = false;
                                MessagesController messagesController2222 = MessagesController.getInstance(this.currentAccount);
                                String str292222 = spannableStringBuilder3;
                                if (this.dialogsType == 0) {
                                }
                                charSequence3 = str292222;
                                if (this.currentDialogFolderId != 0) {
                                }
                                spannableStringBuilder4 = spannableStringBuilder;
                                z7 = z6;
                                i4 = i3;
                                String str312222 = str16;
                                str17 = str9;
                                str18 = str312222;
                            }
                        } else {
                            str2 = "%d";
                        }
                        z = false;
                        this.drawPremium = z;
                        if (z) {
                        }
                        i = this.lastMessageDate;
                        if (i == 0) {
                            i = messageObject3.messageOwner.date;
                        }
                        if (this.isTopic) {
                        }
                        tLRPC$DraftMessage = this.draftMessage;
                        if (tLRPC$DraftMessage != null) {
                        }
                        if (ChatObject.isChannel(this.chat)) {
                        }
                        tLRPC$Chat = this.chat;
                        if (tLRPC$Chat != null) {
                        }
                        if (!this.forbidDraft) {
                        }
                        this.draftMessage = null;
                        if (!isForumCell()) {
                        }
                        z4 = true;
                        str20 = str12;
                        z6 = true;
                        str19 = str20;
                        i3 = -1;
                        spannableStringBuilder3 = str19;
                        if (this.draftMessage != null) {
                        }
                        messageObject2 = this.message;
                        if (messageObject2 == null) {
                        }
                        this.promoDialog = false;
                        MessagesController messagesController22222 = MessagesController.getInstance(this.currentAccount);
                        String str2922222 = spannableStringBuilder3;
                        if (this.dialogsType == 0) {
                        }
                        charSequence3 = str2922222;
                        if (this.currentDialogFolderId != 0) {
                        }
                        spannableStringBuilder4 = spannableStringBuilder;
                        z7 = z6;
                        i4 = i3;
                        String str3122222 = str16;
                        str17 = str9;
                        str18 = str3122222;
                    }
                }
            }
            str2 = "%d";
            i = this.lastMessageDate;
            if (i == 0) {
            }
            if (this.isTopic) {
            }
            tLRPC$DraftMessage = this.draftMessage;
            if (tLRPC$DraftMessage != null) {
            }
            if (ChatObject.isChannel(this.chat)) {
            }
            tLRPC$Chat = this.chat;
            if (tLRPC$Chat != null) {
            }
            if (!this.forbidDraft) {
            }
            this.draftMessage = null;
            if (!isForumCell()) {
            }
            z4 = true;
            str20 = str12;
            z6 = true;
            str19 = str20;
            i3 = -1;
            spannableStringBuilder3 = str19;
            if (this.draftMessage != null) {
            }
            messageObject2 = this.message;
            if (messageObject2 == null) {
            }
            this.promoDialog = false;
            MessagesController messagesController222222 = MessagesController.getInstance(this.currentAccount);
            String str29222222 = spannableStringBuilder3;
            if (this.dialogsType == 0) {
            }
            charSequence3 = str29222222;
            if (this.currentDialogFolderId != 0) {
            }
            spannableStringBuilder4 = spannableStringBuilder;
            z7 = z6;
            i4 = i3;
            String str31222222 = str16;
            str17 = str9;
            str18 = str31222222;
        }
        if (!z4) {
            str21 = str17;
            i5 = (int) Math.ceil(Theme.dialogs_timePaint.measureText(stringForMessageListDate));
            this.timeLayout = new StaticLayout(stringForMessageListDate, Theme.dialogs_timePaint, i5, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (!LocaleController.isRTL) {
                this.timeLeft = (getMeasuredWidth() - AndroidUtilities.dp(15.0f)) - i5;
            } else {
                this.timeLeft = AndroidUtilities.dp(15.0f);
            }
        } else {
            str21 = str17;
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
            int i23 = this.nameWidth - intrinsicWidth3;
            this.nameWidth = i23;
            if (this.drawCheck1) {
                this.nameWidth = i23 - (Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0f));
                if (!LocaleController.isRTL) {
                    int i24 = (this.timeLeft - i6) - intrinsicWidth3;
                    this.halfCheckDrawLeft = i24;
                    this.checkDrawLeft = i24 - AndroidUtilities.dp(5.5f);
                } else {
                    int dp9 = this.timeLeft + i5 + AndroidUtilities.dp(5.0f);
                    this.checkDrawLeft = dp9;
                    this.halfCheckDrawLeft = dp9 + AndroidUtilities.dp(5.5f);
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
            int dp10 = AndroidUtilities.dp(36.0f);
            this.nameWidth -= dp10;
            if (LocaleController.isRTL) {
                this.nameLeft += dp10;
            }
        } else if ((!this.dialogMuted || this.drawUnmute) && !this.drawVerified && this.drawScam == 0) {
            int dp82 = AndroidUtilities.dp(6.0f) + Theme.dialogs_muteDrawable.getIntrinsicWidth();
            this.nameWidth -= dp82;
            if (LocaleController.isRTL) {
                this.nameLeft += dp82;
            }
        } else if (this.drawVerified) {
            int dp11 = AndroidUtilities.dp(6.0f) + Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
            this.nameWidth -= dp11;
            if (LocaleController.isRTL) {
                this.nameLeft += dp11;
            }
        } else if (this.drawPremium) {
            int dp12 = AndroidUtilities.dp(36.0f);
            this.nameWidth -= dp12;
            if (LocaleController.isRTL) {
                this.nameLeft += dp12;
            }
        } else if (this.drawScam != 0) {
            int dp13 = AndroidUtilities.dp(6.0f) + (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).getIntrinsicWidth();
            this.nameWidth -= dp13;
            if (LocaleController.isRTL) {
                this.nameLeft += dp13;
            }
        }
        try {
            dp7 = this.nameWidth - AndroidUtilities.dp(12.0f);
            if (dp7 < 0) {
                dp7 = 0;
            }
            CharSequence replace22 = str18.replace('\n', ' ');
            if (this.nameLayoutEllipsizeByGradient) {
                this.nameLayoutFits = replace22.length() == TextUtils.ellipsize(replace22, Theme.dialogs_namePaint[this.paintIndex], (float) dp7, TextUtils.TruncateAt.END).length();
                dp7 += AndroidUtilities.dp(48.0f);
            }
            float f2 = dp7;
            this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(replace22.toString()) <= f2;
            if (!this.twoLinesForName) {
                replace22 = TextUtils.ellipsize(replace22, Theme.dialogs_namePaint[this.paintIndex], f2, TextUtils.TruncateAt.END);
            }
            CharSequence replaceEmoji22 = Emoji.replaceEmoji(replace22, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            MessageObject messageObject92 = this.message;
            CharSequence charSequence6 = (messageObject92 != null || !messageObject92.hasHighlightedWords() || (highlightText5 = AndroidUtilities.highlightText(replaceEmoji22, this.message.highlightedWords, this.resourcesProvider)) == null) ? replaceEmoji22 : highlightText5;
            if (!this.twoLinesForName) {
                this.nameLayout = StaticLayoutEx.createStaticLayout(charSequence6, Theme.dialogs_namePaint[this.paintIndex], dp7, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, dp7, 2);
            } else {
                this.nameLayout = new StaticLayout(charSequence6, Theme.dialogs_namePaint[this.paintIndex], Math.max(dp7, this.nameWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
            this.nameLayoutTranslateX = (this.nameLayoutEllipsizeByGradient || !this.nameLayout.isRtlCharAt(0)) ? 0.0f : -AndroidUtilities.dp(36.0f);
            this.nameLayoutEllipsizeLeft = this.nameLayout.isRtlCharAt(0);
        } catch (Exception e3) {
            FileLog.e(e3);
        }
        if (!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
            dp = AndroidUtilities.dp(11.0f);
            this.messageNameTop = AndroidUtilities.dp(32.0f);
            this.timeTop = AndroidUtilities.dp(13.0f);
            this.errorTop = AndroidUtilities.dp(43.0f);
            this.pinTop = AndroidUtilities.dp(43.0f);
            this.countTop = AndroidUtilities.dp(43.0f);
            this.checkDrawTop = AndroidUtilities.dp(13.0f);
            measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 21);
            if (!LocaleController.isRTL) {
                int dp14 = AndroidUtilities.dp(16.0f);
                this.messageNameLeft = dp14;
                this.messageLeft = dp14;
                dp2 = getMeasuredWidth() - AndroidUtilities.dp(66.0f);
                dp3 = dp2 - AndroidUtilities.dp(31.0f);
            } else {
                int dp15 = AndroidUtilities.dp(this.messagePaddingStart + 6);
                this.messageNameLeft = dp15;
                this.messageLeft = dp15;
                dp2 = AndroidUtilities.dp(10.0f);
                dp3 = AndroidUtilities.dp(69.0f) + dp2;
            }
            this.avatarImage.setImageCoords(dp2, dp, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            i7 = 0;
            while (true) {
                imageReceiverArr = this.thumbImage;
                if (i7 < imageReceiverArr.length) {
                    break;
                }
                imageReceiverArr[i7].setImageCoords(((this.thumbSize + 2) * i7) + dp3, AndroidUtilities.dp(31.0f) + dp + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                i7++;
            }
        } else {
            dp = AndroidUtilities.dp(9.0f);
            this.messageNameTop = AndroidUtilities.dp(31.0f);
            this.timeTop = AndroidUtilities.dp(16.0f);
            this.errorTop = AndroidUtilities.dp(39.0f);
            this.pinTop = AndroidUtilities.dp(39.0f);
            this.countTop = this.isTopic ? AndroidUtilities.dp(36.0f) : AndroidUtilities.dp(39.0f);
            this.checkDrawTop = AndroidUtilities.dp(17.0f);
            measuredWidth = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) - (LocaleController.isRTL ? 0 : 12));
            if (LocaleController.isRTL) {
                int dp16 = AndroidUtilities.dp(22.0f);
                this.messageNameLeft = dp16;
                this.messageLeft = dp16;
                dp5 = getMeasuredWidth() - AndroidUtilities.dp(64.0f);
                dp6 = dp5 - AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 11);
            } else {
                int dp17 = AndroidUtilities.dp(this.messagePaddingStart + 4);
                this.messageNameLeft = dp17;
                this.messageLeft = dp17;
                dp5 = AndroidUtilities.dp(10.0f);
                dp6 = AndroidUtilities.dp(67.0f) + dp5;
            }
            this.avatarImage.setImageCoords(dp5, dp, AndroidUtilities.dp(54.0f), AndroidUtilities.dp(54.0f));
            int i25 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr3 = this.thumbImage;
                if (i25 >= imageReceiverArr3.length) {
                    break;
                }
                imageReceiverArr3[i25].setImageCoords(((this.thumbSize + 2) * i25) + dp6, AndroidUtilities.dp(30.0f) + dp + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0), AndroidUtilities.dp(this.thumbSize), AndroidUtilities.dp(this.thumbSize));
                i25++;
            }
        }
        int i222 = dp;
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
            int dp18 = AndroidUtilities.dp(31.0f);
            measuredWidth -= dp18;
            if (!LocaleController.isRTL) {
                this.errorLeft = getMeasuredWidth() - AndroidUtilities.dp(34.0f);
            } else {
                this.errorLeft = AndroidUtilities.dp(11.0f);
                this.messageLeft += dp18;
                this.messageNameLeft += dp18;
            }
        } else if (str14 != null || str15 != null || this.drawReactionMention) {
            if (str14 != null) {
                this.countWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str14)));
                this.countLayout = new StaticLayout(str14, Theme.dialogs_countTextPaint, this.countWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                int dp19 = this.countWidth + AndroidUtilities.dp(18.0f);
                measuredWidth -= dp19;
                if (!LocaleController.isRTL) {
                    this.countLeft = (getMeasuredWidth() - this.countWidth) - AndroidUtilities.dp(20.0f);
                } else {
                    this.countLeft = AndroidUtilities.dp(20.0f);
                    this.messageLeft += dp19;
                    this.messageNameLeft += dp19;
                }
                this.drawCount = true;
            } else {
                this.countWidth = 0;
            }
            if (str15 != null) {
                if (this.currentDialogFolderId != 0) {
                    this.mentionWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str15)));
                    this.mentionLayout = new StaticLayout(str15, Theme.dialogs_countTextPaint, this.mentionWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                } else {
                    this.mentionWidth = AndroidUtilities.dp(12.0f);
                }
                int dp20 = this.mentionWidth + AndroidUtilities.dp(18.0f);
                measuredWidth -= dp20;
                if (!LocaleController.isRTL) {
                    int measuredWidth3 = (getMeasuredWidth() - this.mentionWidth) - AndroidUtilities.dp(20.0f);
                    int i26 = this.countWidth;
                    this.mentionLeft = measuredWidth3 - (i26 != 0 ? i26 + AndroidUtilities.dp(18.0f) : 0);
                } else {
                    int dp21 = AndroidUtilities.dp(20.0f);
                    int i27 = this.countWidth;
                    this.mentionLeft = dp21 + (i27 != 0 ? i27 + AndroidUtilities.dp(18.0f) : 0);
                    this.messageLeft += dp20;
                    this.messageNameLeft += dp20;
                }
                this.drawMention = true;
            } else {
                this.mentionWidth = 0;
            }
            if (this.drawReactionMention) {
                int dp22 = AndroidUtilities.dp(24.0f);
                measuredWidth -= dp22;
                if (!LocaleController.isRTL) {
                    int measuredWidth4 = getMeasuredWidth() - AndroidUtilities.dp(32.0f);
                    this.reactionMentionLeft = measuredWidth4;
                    if (this.drawMention) {
                        int i28 = this.mentionWidth;
                        this.reactionMentionLeft = measuredWidth4 - (i28 != 0 ? i28 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i29 = this.reactionMentionLeft;
                        int i30 = this.countWidth;
                        this.reactionMentionLeft = i29 - (i30 != 0 ? i30 + AndroidUtilities.dp(18.0f) : 0);
                    }
                } else {
                    int dp23 = AndroidUtilities.dp(20.0f);
                    this.reactionMentionLeft = dp23;
                    if (this.drawMention) {
                        int i31 = this.mentionWidth;
                        this.reactionMentionLeft = dp23 + (i31 != 0 ? i31 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i32 = this.reactionMentionLeft;
                        int i33 = this.countWidth;
                        this.reactionMentionLeft = i32 + (i33 != 0 ? i33 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    this.messageLeft += dp22;
                    this.messageNameLeft += dp22;
                }
            }
        } else {
            if (getIsPinned()) {
                int intrinsicWidth4 = Theme.dialogs_pinnedDrawable.getIntrinsicWidth() + AndroidUtilities.dp(8.0f);
                measuredWidth -= intrinsicWidth4;
                if (LocaleController.isRTL) {
                    this.messageLeft += intrinsicWidth4;
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
            if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || str21 != null) {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceNewLines(charSequence3);
            } else {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceTwoNewLinesToOne(charSequence3);
            }
            charSequence3 = Emoji.replaceEmoji(replaceTwoNewLinesToOne, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
            MessageObject messageObject14 = this.message;
            if (messageObject14 != null && (highlightText4 = AndroidUtilities.highlightText(charSequence3, messageObject14.highlightedWords, this.resourcesProvider)) != null) {
                charSequence3 = highlightText4;
            }
        }
        int max2 = Math.max(AndroidUtilities.dp(12.0f), measuredWidth);
        this.buttonTop = AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 58.0f : 62.0f);
        if (!isForumCell()) {
            if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                this.messageTop = AndroidUtilities.dp(34.0f);
            } else {
                this.messageTop = AndroidUtilities.dp(39.0f);
            }
            int i34 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr4 = this.thumbImage;
                if (i34 >= imageReceiverArr4.length) {
                    break;
                }
                imageReceiverArr4[i34].setImageY(this.buttonTop);
                i34++;
            }
            str24 = str21;
        } else {
            boolean z12 = this.useForceThreeLines;
            if ((z12 || SharedConfig.useThreeLinesLayout) && str21 != null && (this.currentDialogFolderId == 0 || this.currentDialogFolderDialogsCount == 1)) {
                try {
                    messageObject4 = this.message;
                } catch (Exception e4) {
                    e = e4;
                    str22 = str21;
                }
                if (messageObject4 == null || !messageObject4.hasHighlightedWords()) {
                    str22 = str21;
                } else {
                    str22 = str21;
                    try {
                        highlightText3 = AndroidUtilities.highlightText(str22, this.message.highlightedWords, this.resourcesProvider);
                    } catch (Exception e5) {
                        e = e5;
                        str23 = str22;
                        FileLog.e(e);
                        this.messageTop = AndroidUtilities.dp(51.0f);
                        if (this.nameIsEllipsized) {
                        }
                        i8 = 0;
                        while (true) {
                            imageReceiverArr2 = this.thumbImage;
                            if (i8 < imageReceiverArr2.length) {
                            }
                            imageReceiverArr2[i8].setImageY(i222 + dp4 + AndroidUtilities.dp(40.0f));
                            i8++;
                        }
                        if (this.twoLinesForName) {
                        }
                        this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack2, this.messageNameLayout);
                        if (TextUtils.isEmpty(spannableStringBuilder4)) {
                        }
                        this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack3, this.buttonLayout);
                        z8 = this.useForceThreeLines;
                        if (!z8) {
                        }
                        this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                        charSequence3 = str23;
                        str23 = null;
                        if (charSequence3 instanceof Spannable) {
                        }
                        if (!this.useForceThreeLines) {
                        }
                        if (this.thumbsCount > 0) {
                        }
                        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence3, this.currentMessagePaint, max2, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max2, str23 == null ? 1 : 2);
                        this.spoilersPool.addAll(this.spoilers);
                        this.spoilers.clear();
                        SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
                        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                        if (!LocaleController.isRTL) {
                        }
                        staticLayout = this.messageLayout;
                        if (staticLayout != null) {
                        }
                        updateThumbsPosition();
                    }
                    if (highlightText3 != null) {
                        str23 = highlightText3;
                        this.messageNameLayout = StaticLayoutEx.createStaticLayout(str23, Theme.dialogs_messageNamePaint, max2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max2, 1);
                        this.messageTop = AndroidUtilities.dp(51.0f);
                        dp4 = (this.nameIsEllipsized || !this.isTopic) ? 0 : AndroidUtilities.dp(20.0f);
                        i8 = 0;
                        while (true) {
                            imageReceiverArr2 = this.thumbImage;
                            if (i8 < imageReceiverArr2.length) {
                                break;
                            }
                            imageReceiverArr2[i8].setImageY(i222 + dp4 + AndroidUtilities.dp(40.0f));
                            i8++;
                        }
                        if (this.twoLinesForName) {
                            this.messageTop += AndroidUtilities.dp(20.0f);
                        }
                        this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack2, this.messageNameLayout);
                        if (TextUtils.isEmpty(spannableStringBuilder4)) {
                            this.buttonLayout = new StaticLayout(Emoji.replaceEmoji(TextUtils.ellipsize(spannableStringBuilder4, this.currentMessagePaint, max2 - AndroidUtilities.dp(24.0f), TextUtils.TruncateAt.END), this.currentMessagePaint.getFontMetricsInt(), AndroidUtilities.dp(17.0f), false), this.currentMessagePaint, max2 - AndroidUtilities.dp(20.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                            this.spoilersPool2.addAll(this.spoilers2);
                            this.spoilers2.clear();
                            SpoilerEffect.addSpoilers(this, this.buttonLayout, this.spoilersPool2, this.spoilers2);
                        } else {
                            this.buttonLayout = null;
                        }
                        this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack3, this.buttonLayout);
                        z8 = this.useForceThreeLines;
                        if ((!z8 || SharedConfig.useThreeLinesLayout) && this.currentDialogFolderId != 0 && this.currentDialogFolderDialogsCount > 1) {
                            this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                            charSequence3 = str23;
                            str23 = null;
                        } else if ((!z8 && !SharedConfig.useThreeLinesLayout) || str23 != null) {
                            if (!isForumCell() && (charSequence3 instanceof Spanned) && ((FixedWidthSpan[]) ((Spanned) charSequence3).getSpans(0, charSequence3.length(), FixedWidthSpan.class)).length <= 0) {
                                charSequence3 = TextUtils.ellipsize(charSequence3, this.currentMessagePaint, max2 - AndroidUtilities.dp((((this.thumbsCount * (this.thumbSize + 2)) - 2) + 12) + 5), TextUtils.TruncateAt.END);
                            } else {
                                charSequence3 = TextUtils.ellipsize(charSequence3, this.currentMessagePaint, max2 - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END);
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
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            if (this.thumbsCount > 0) {
                                max2 += AndroidUtilities.dp(((i10 * (this.thumbSize + 2)) - 2) + 5);
                                if (LocaleController.isRTL) {
                                    this.messageLeft -= AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 5);
                                }
                            }
                            this.messageLayout = new StaticLayout(charSequence3, this.currentMessagePaint, max2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                            this.spoilersPool.addAll(this.spoilers);
                            this.spoilers.clear();
                            SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
                            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                            if (!LocaleController.isRTL) {
                                StaticLayout staticLayout2 = this.nameLayout;
                                if (staticLayout2 != null && staticLayout2.getLineCount() > 0) {
                                    float lineLeft = this.nameLayout.getLineLeft(0);
                                    double ceil = Math.ceil(this.nameLayout.getLineWidth(0));
                                    int dp24 = this.nameWidth + AndroidUtilities.dp(12.0f);
                                    this.nameWidth = dp24;
                                    if (this.nameLayoutEllipsizeByGradient) {
                                        ceil = Math.min(dp24, ceil);
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
                                    }
                                    if (lineLeft == 0.0f) {
                                        int i35 = this.nameWidth;
                                        if (ceil < i35) {
                                            double d17 = this.nameLeft;
                                            double d18 = i35;
                                            Double.isNaN(d18);
                                            Double.isNaN(d17);
                                            this.nameLeft = (int) (d17 + (d18 - ceil));
                                        }
                                    }
                                }
                                StaticLayout staticLayout3 = this.messageLayout;
                                if (staticLayout3 != null && (lineCount2 = staticLayout3.getLineCount()) > 0) {
                                    int i36 = 0;
                                    int i37 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                    while (true) {
                                        if (i36 >= lineCount2) {
                                            break;
                                        }
                                        if (this.messageLayout.getLineLeft(i36) != 0.0f) {
                                            i37 = 0;
                                            break;
                                        }
                                        double ceil2 = Math.ceil(this.messageLayout.getLineWidth(i36));
                                        double d19 = max2;
                                        Double.isNaN(d19);
                                        i37 = Math.min(i37, (int) (d19 - ceil2));
                                        i36++;
                                    }
                                    if (i37 != Integer.MAX_VALUE) {
                                        this.messageLeft += i37;
                                    }
                                }
                                StaticLayout staticLayout4 = this.messageNameLayout;
                                if (staticLayout4 != null && staticLayout4.getLineCount() > 0 && this.messageNameLayout.getLineLeft(0) == 0.0f) {
                                    double ceil3 = Math.ceil(this.messageNameLayout.getLineWidth(0));
                                    double d20 = max2;
                                    if (ceil3 < d20) {
                                        double d21 = this.messageNameLeft;
                                        Double.isNaN(d20);
                                        Double.isNaN(d21);
                                        this.messageNameLeft = (int) (d21 + (d20 - ceil3));
                                    }
                                }
                            } else {
                                StaticLayout staticLayout5 = this.nameLayout;
                                if (staticLayout5 != null && staticLayout5.getLineCount() > 0) {
                                    float lineRight = this.nameLayout.getLineRight(0);
                                    if (this.nameLayoutEllipsizeByGradient) {
                                        lineRight = Math.min(this.nameWidth, lineRight);
                                    }
                                    if (lineRight == this.nameWidth) {
                                        double ceil4 = Math.ceil(this.nameLayout.getLineWidth(0));
                                        if (this.nameLayoutEllipsizeByGradient) {
                                            ceil4 = Math.min(this.nameWidth, ceil4);
                                        }
                                        int i38 = this.nameWidth;
                                        if (ceil4 < i38) {
                                            double d22 = this.nameLeft;
                                            double d23 = i38;
                                            Double.isNaN(d23);
                                            Double.isNaN(d22);
                                            this.nameLeft = (int) (d22 - (d23 - ceil4));
                                        }
                                    }
                                    if (this.dialogMuted || this.drawUnmute || this.drawVerified || this.drawPremium || this.drawScam != 0) {
                                        this.nameMuteLeft = (int) (this.nameLeft + lineRight + AndroidUtilities.dp(6.0f));
                                    }
                                }
                                StaticLayout staticLayout6 = this.messageLayout;
                                if (staticLayout6 != null && (lineCount = staticLayout6.getLineCount()) > 0) {
                                    float f3 = 2.14748365E9f;
                                    for (int i39 = 0; i39 < lineCount; i39++) {
                                        f3 = Math.min(f3, this.messageLayout.getLineLeft(i39));
                                    }
                                    this.messageLeft = (int) (this.messageLeft - f3);
                                }
                                StaticLayout staticLayout7 = this.messageNameLayout;
                                if (staticLayout7 != null && staticLayout7.getLineCount() > 0) {
                                    this.messageNameLeft = (int) (this.messageNameLeft - this.messageNameLayout.getLineLeft(0));
                                }
                            }
                            staticLayout = this.messageLayout;
                            if (staticLayout != null && this.printingStringType >= 0 && staticLayout.getText().length() > 0) {
                                if (i4 < 0 && (i9 = i4 + 1) < this.messageLayout.getText().length()) {
                                    primaryHorizontal = this.messageLayout.getPrimaryHorizontal(i4);
                                    primaryHorizontal2 = this.messageLayout.getPrimaryHorizontal(i9);
                                } else {
                                    primaryHorizontal = this.messageLayout.getPrimaryHorizontal(0);
                                    primaryHorizontal2 = this.messageLayout.getPrimaryHorizontal(1);
                                }
                                if (primaryHorizontal >= primaryHorizontal2) {
                                    this.statusDrawableLeft = (int) (this.messageLeft + primaryHorizontal);
                                } else {
                                    this.statusDrawableLeft = (int) (this.messageLeft + primaryHorizontal2 + AndroidUtilities.dp(3.0f));
                                }
                            }
                            updateThumbsPosition();
                        }
                        if (this.thumbsCount > 0 && str23 != null) {
                            max2 += AndroidUtilities.dp(5.0f);
                        }
                        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence3, this.currentMessagePaint, max2, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max2, str23 == null ? 1 : 2);
                        this.spoilersPool.addAll(this.spoilers);
                        this.spoilers.clear();
                        SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
                        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                        if (!LocaleController.isRTL) {
                        }
                        staticLayout = this.messageLayout;
                        if (staticLayout != null) {
                            if (i4 < 0) {
                            }
                            primaryHorizontal = this.messageLayout.getPrimaryHorizontal(0);
                            primaryHorizontal2 = this.messageLayout.getPrimaryHorizontal(1);
                            if (primaryHorizontal >= primaryHorizontal2) {
                            }
                        }
                        updateThumbsPosition();
                    }
                }
                str23 = str22;
                this.messageNameLayout = StaticLayoutEx.createStaticLayout(str23, Theme.dialogs_messageNamePaint, max2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max2, 1);
                this.messageTop = AndroidUtilities.dp(51.0f);
                if (this.nameIsEllipsized) {
                }
                i8 = 0;
                while (true) {
                    imageReceiverArr2 = this.thumbImage;
                    if (i8 < imageReceiverArr2.length) {
                    }
                    imageReceiverArr2[i8].setImageY(i222 + dp4 + AndroidUtilities.dp(40.0f));
                    i8++;
                }
                if (this.twoLinesForName) {
                }
                this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack2, this.messageNameLayout);
                if (TextUtils.isEmpty(spannableStringBuilder4)) {
                }
                this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack3, this.buttonLayout);
                z8 = this.useForceThreeLines;
                if (!z8) {
                }
                this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                charSequence3 = str23;
                str23 = null;
                if (charSequence3 instanceof Spannable) {
                }
                if (!this.useForceThreeLines) {
                    if (this.thumbsCount > 0) {
                    }
                    this.messageLayout = new StaticLayout(charSequence3, this.currentMessagePaint, max2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.spoilersPool.addAll(this.spoilers);
                    this.spoilers.clear();
                    SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
                    this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                    if (!LocaleController.isRTL) {
                    }
                    staticLayout = this.messageLayout;
                    if (staticLayout != null) {
                    }
                    updateThumbsPosition();
                }
                if (this.thumbsCount > 0) {
                    max2 += AndroidUtilities.dp(5.0f);
                }
                this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence3, this.currentMessagePaint, max2, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max2, str23 == null ? 1 : 2);
                this.spoilersPool.addAll(this.spoilers);
                this.spoilers.clear();
                SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
                this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                if (!LocaleController.isRTL) {
                }
                staticLayout = this.messageLayout;
                if (staticLayout != null) {
                }
                updateThumbsPosition();
            }
            str24 = str21;
            this.messageNameLayout = null;
            if (z12 || SharedConfig.useThreeLinesLayout) {
                this.messageTop = AndroidUtilities.dp(32.0f);
                int dp29 = (!this.nameIsEllipsized || !this.isTopic) ? 0 : AndroidUtilities.dp(20.0f);
                int i40 = 0;
                while (true) {
                    ImageReceiver[] imageReceiverArr5 = this.thumbImage;
                    if (i40 >= imageReceiverArr5.length) {
                        break;
                    }
                    imageReceiverArr5[i40].setImageY(i222 + dp29 + AndroidUtilities.dp(21.0f));
                    i40++;
                }
            } else {
                this.messageTop = AndroidUtilities.dp(39.0f);
            }
        }
        str23 = str24;
        if (this.twoLinesForName) {
        }
        this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack2, this.messageNameLayout);
        if (TextUtils.isEmpty(spannableStringBuilder4)) {
        }
        this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack3, this.buttonLayout);
        z8 = this.useForceThreeLines;
        if (!z8) {
        }
        this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
        charSequence3 = str23;
        str23 = null;
        if (charSequence3 instanceof Spannable) {
        }
        if (!this.useForceThreeLines) {
        }
        if (this.thumbsCount > 0) {
        }
        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence3, this.currentMessagePaint, max2, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max2, str23 == null ? 1 : 2);
        this.spoilersPool.addAll(this.spoilers);
        this.spoilers.clear();
        SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
        if (!LocaleController.isRTL) {
        }
        staticLayout = this.messageLayout;
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
                if (!(text instanceof Spannable)) {
                    return;
                }
                FixedWidthSpan[] fixedWidthSpanArr = (FixedWidthSpan[]) ((Spannable) text).getSpans(0, text.length(), FixedWidthSpan.class);
                if (fixedWidthSpanArr == null || fixedWidthSpanArr.length <= 0) {
                    return;
                }
                int spanStart = ((Spannable) text).getSpanStart(fixedWidthSpanArr[0]);
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
        int i2;
        this.topMessageTopicStartIndex = 0;
        this.topMessageTopicEndIndex = 0;
        if (this.chat != null) {
            ArrayList<TLRPC$TL_forumTopic> topics = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopics(this.chat.id);
            if (topics != null && !topics.isEmpty()) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                MessageObject messageObject = this.message;
                if (messageObject != null) {
                    i = MessageObject.getTopicId(messageObject.messageOwner);
                    TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(this.chat.id, i);
                    if (findTopic != null) {
                        CharSequence topicSpannedName = ForumUtilities.getTopicSpannedName(findTopic, this.currentMessagePaint);
                        spannableStringBuilder.append(topicSpannedName);
                        i2 = findTopic.unread_count > 0 ? topicSpannedName.length() : 0;
                        this.topMessageTopicStartIndex = 0;
                        this.topMessageTopicEndIndex = topicSpannedName.length();
                    } else {
                        i2 = 0;
                    }
                    spannableStringBuilder.append((CharSequence) " ");
                    spannableStringBuilder.setSpan(new FixedWidthSpan(AndroidUtilities.dp(3.0f)), spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);
                } else {
                    i = 0;
                    i2 = 0;
                }
                for (int i3 = 0; i3 < Math.min(5, topics.size()); i3++) {
                    if (topics.get(i3).id != i) {
                        if (spannableStringBuilder.length() != 0) {
                            spannableStringBuilder.append((CharSequence) " ");
                        }
                        spannableStringBuilder.append(ForumUtilities.getTopicSpannedName(topics.get(i3), this.currentMessagePaint));
                    }
                }
                if (i2 > 0) {
                    spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM), 0, Theme.getColor("chats_name", this.resourcesProvider)), 0, Math.min(spannableStringBuilder.length(), i2 + 2), 0);
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

    protected boolean isForumCell() {
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

    public boolean checkCurrentDialogIndex(boolean z) {
        MessageObject messageObject;
        MessageObject messageObject2;
        DialogsActivity dialogsActivity = this.parentFragment;
        boolean z2 = false;
        if (dialogsActivity == null) {
            return false;
        }
        ArrayList<TLRPC$Dialog> dialogsArray = dialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, z);
        if (this.index < dialogsArray.size()) {
            TLRPC$Dialog tLRPC$Dialog = dialogsArray.get(this.index);
            boolean z3 = true;
            MessageObject messageObject3 = null;
            TLRPC$Dialog tLRPC$Dialog2 = this.index + 1 < dialogsArray.size() ? dialogsArray.get(this.index + 1) : null;
            TLRPC$DraftMessage draft = MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, 0);
            if (this.currentDialogFolderId != 0) {
                messageObject = findFolderTopMessage();
                this.groupMessages = null;
            } else {
                ArrayList<MessageObject> arrayList = MessagesController.getInstance(this.currentAccount).dialogMessage.get(tLRPC$Dialog.id);
                this.groupMessages = arrayList;
                if (arrayList != null && arrayList.size() > 0) {
                    messageObject3 = this.groupMessages.get(0);
                }
                messageObject = messageObject3;
            }
            if (this.currentDialogId != tLRPC$Dialog.id || (((messageObject2 = this.message) != null && messageObject2.getId() != tLRPC$Dialog.top_message) || ((messageObject != null && messageObject.messageOwner.edit_date != this.currentEditDate) || this.unreadCount != tLRPC$Dialog.unread_count || this.mentionCount != tLRPC$Dialog.unread_mentions_count || this.markUnread != tLRPC$Dialog.unread_mark || this.message != messageObject || draft != this.draftMessage || this.drawPin != tLRPC$Dialog.pinned))) {
                boolean z4 = this.currentDialogId != tLRPC$Dialog.id;
                boolean z5 = this.isForum != MessagesController.getInstance(this.currentAccount).isForum(tLRPC$Dialog.id);
                this.isForum = MessagesController.getInstance(this.currentAccount).isForum(tLRPC$Dialog.id);
                this.currentDialogId = tLRPC$Dialog.id;
                if (z4) {
                    this.lastDialogChangedTime = System.currentTimeMillis();
                    ValueAnimator valueAnimator = this.statusDrawableAnimator;
                    if (valueAnimator != null) {
                        valueAnimator.removeAllListeners();
                        this.statusDrawableAnimator.cancel();
                    }
                    this.statusDrawableAnimationInProgress = false;
                    this.lastStatusDrawableParams = -1;
                }
                boolean z6 = tLRPC$Dialog instanceof TLRPC$TL_dialogFolder;
                if (z6) {
                    this.currentDialogFolderId = ((TLRPC$TL_dialogFolder) tLRPC$Dialog).folder.id;
                } else {
                    this.currentDialogFolderId = 0;
                }
                int i = this.dialogsType;
                if (i == 7 || i == 8) {
                    MessagesController.DialogFilter dialogFilter = MessagesController.getInstance(this.currentAccount).selectedDialogFilter[this.dialogsType == 8 ? (char) 1 : (char) 0];
                    if (!(tLRPC$Dialog instanceof TLRPC$TL_dialog) || tLRPC$Dialog2 == null || dialogFilter == null || dialogFilter.pinnedDialogs.indexOfKey(tLRPC$Dialog.id) < 0 || dialogFilter.pinnedDialogs.indexOfKey(tLRPC$Dialog2.id) >= 0) {
                        z3 = false;
                    }
                    this.fullSeparator = z3;
                    this.fullSeparator2 = false;
                } else {
                    this.fullSeparator = (tLRPC$Dialog instanceof TLRPC$TL_dialog) && tLRPC$Dialog.pinned && tLRPC$Dialog2 != null && !tLRPC$Dialog2.pinned;
                    if (!z6 || tLRPC$Dialog2 == null || tLRPC$Dialog2.pinned) {
                        z3 = false;
                    }
                    this.fullSeparator2 = z3;
                }
                update(0, !z4);
                if (z4) {
                    this.reorderIconProgress = (!this.drawPin || !this.drawReorder) ? 0.0f : 1.0f;
                }
                checkOnline();
                checkGroupCall();
                checkChatTheme();
                z2 = z5;
            }
        }
        if (z2) {
            requestLayout();
        }
        return z2;
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
        if (this.checkBox == null) {
            CheckBox2 checkBox2 = new CheckBox2(getContext(), 21, this.resourcesProvider);
            this.checkBox = checkBox2;
            checkBox2.setColor(null, "windowBackgroundWhite", "checkboxCheck");
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(3);
            addView(this.checkBox);
        }
        this.checkBox.setChecked(z, z2);
    }

    private MessageObject findFolderTopMessage() {
        DialogsActivity dialogsActivity = this.parentFragment;
        if (dialogsActivity == null) {
            return null;
        }
        ArrayList<TLRPC$Dialog> dialogsArray = dialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.currentDialogFolderId, false);
        if (dialogsArray.isEmpty()) {
            return null;
        }
        int size = dialogsArray.size();
        MessageObject messageObject = null;
        for (int i = 0; i < size; i++) {
            TLRPC$Dialog tLRPC$Dialog = dialogsArray.get(i);
            ArrayList<MessageObject> arrayList = MessagesController.getInstance(this.currentAccount).dialogMessage.get(tLRPC$Dialog.id);
            MessageObject messageObject2 = (arrayList == null || arrayList.size() <= 0) ? null : arrayList.get(0);
            if (messageObject2 != null && (messageObject == null || messageObject2.messageOwner.date > messageObject.messageOwner.date)) {
                messageObject = messageObject2;
            }
            if (tLRPC$Dialog.pinnedNum == 0 && messageObject != null) {
                break;
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

    /* JADX WARN: Removed duplicated region for block: B:134:0x02b8  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x02ef  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x03a7  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x03ac  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x03b0  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x03bf  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0425  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x04cc  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x058a  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x0619  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x06a5  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x06b5  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x06b7  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x06c3  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x06c5  */
    /* JADX WARN: Removed duplicated region for block: B:319:0x04f0  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x03d2  */
    /* JADX WARN: Removed duplicated region for block: B:359:0x03b2  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01a8  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01b9  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01bd  */
    /* JADX WARN: Type inference failed for: r3v103 */
    /* JADX WARN: Type inference failed for: r3v2 */
    /* JADX WARN: Type inference failed for: r3v3, types: [org.telegram.tgnet.TLRPC$Chat, org.telegram.tgnet.TLRPC$User, org.telegram.tgnet.TLRPC$EncryptedChat] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean update(int i, boolean z) {
        boolean z2;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic;
        ?? r3;
        long j;
        TLRPC$Chat tLRPC$Chat;
        ValueAnimator valueAnimator;
        String format;
        String format2;
        TLRPC$Chat chat;
        boolean z3;
        MessageObject messageObject;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        TLRPC$Chat chat2;
        MessageObject messageObject2;
        CustomDialog customDialog = this.customDialog;
        boolean z4 = true;
        boolean z5 = false;
        if (customDialog != null) {
            this.lastMessageDate = customDialog.date;
            int i7 = customDialog.unread_count;
            if (i7 == 0) {
                z4 = false;
            }
            this.lastUnreadState = z4;
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
            boolean z6 = this.reactionMentionCount != 0;
            boolean z7 = this.markUnread;
            boolean isForumCell = isForumCell();
            this.hasUnmutedTopics = false;
            this.readOutboxMaxId = -1;
            if (this.isDialogCell) {
                TLRPC$Dialog tLRPC$Dialog = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                if (tLRPC$Dialog != null) {
                    this.readOutboxMaxId = tLRPC$Dialog.read_outbox_max_id;
                    if (i == 0) {
                        this.clearingDialog = MessagesController.getInstance(this.currentAccount).isClearingDialog(tLRPC$Dialog.id);
                        ArrayList<MessageObject> arrayList = MessagesController.getInstance(this.currentAccount).dialogMessage.get(tLRPC$Dialog.id);
                        this.groupMessages = arrayList;
                        MessageObject messageObject3 = (arrayList == null || arrayList.size() <= 0) ? null : this.groupMessages.get(0);
                        this.message = messageObject3;
                        this.lastUnreadState = messageObject3 != null && messageObject3.isUnread();
                        TLRPC$Chat chat3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-tLRPC$Dialog.id));
                        z2 = (chat3 != null && chat3.forum && !this.isTopic) != isForumCell;
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
                        MessageObject messageObject4 = this.message;
                        this.currentEditDate = messageObject4 != null ? messageObject4.messageOwner.edit_date : 0;
                        this.lastMessageDate = tLRPC$Dialog.last_message_date;
                        int i10 = this.dialogsType;
                        if (i10 == 7 || i10 == 8) {
                            MessagesController.DialogFilter dialogFilter = MessagesController.getInstance(this.currentAccount).selectedDialogFilter[this.dialogsType == 8 ? (char) 1 : (char) 0];
                            this.drawPin = dialogFilter != null && dialogFilter.pinnedDialogs.indexOfKey(tLRPC$Dialog.id) >= 0;
                        } else {
                            this.drawPin = this.currentDialogFolderId == 0 && tLRPC$Dialog.pinned;
                        }
                        MessageObject messageObject5 = this.message;
                        if (messageObject5 != null) {
                            this.lastSendState = messageObject5.messageOwner.send_state;
                        }
                        tLRPC$TL_forumTopic = this.forumTopic;
                        if (tLRPC$TL_forumTopic != null) {
                            this.unreadCount = tLRPC$TL_forumTopic.unread_count;
                            this.mentionCount = tLRPC$TL_forumTopic.unread_mentions_count;
                            this.reactionMentionCount = tLRPC$TL_forumTopic.unread_reactions_count;
                        }
                        if (this.dialogsType == 2) {
                            this.drawPin = false;
                        }
                        if (i == 0) {
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
                                    z3 = true;
                                    if (!z3 && (i & MessagesController.UPDATE_MASK_MESSAGE_TEXT) != 0 && (messageObject2 = this.message) != null && messageObject2.messageText != this.lastMessageString) {
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
                                        MessageObject messageObject6 = this.message;
                                        if (messageObject6 != null && this.lastUnreadState != messageObject6.isUnread()) {
                                            this.lastUnreadState = this.message.isUnread();
                                            z3 = true;
                                        }
                                        if (this.isDialogCell) {
                                            TLRPC$Dialog tLRPC$Dialog2 = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                                            TLRPC$Chat chat4 = tLRPC$Dialog2 == null ? null : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-tLRPC$Dialog2.id));
                                            if (chat4 != null && chat4.forum) {
                                                int[] forumUnreadCount2 = MessagesController.getInstance(this.currentAccount).getTopicsController().getForumUnreadCount(chat4.id);
                                                i5 = forumUnreadCount2[0];
                                                i6 = forumUnreadCount2[1];
                                                int i11 = forumUnreadCount2[2];
                                                this.hasUnmutedTopics = forumUnreadCount2[3] != 0;
                                                i4 = i11;
                                            } else {
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
                                                z3 = true;
                                            }
                                        }
                                    }
                                    if (!z3 && (i & MessagesController.UPDATE_MASK_SEND_STATE) != 0 && (messageObject = this.message) != null) {
                                        i2 = this.lastSendState;
                                        i3 = messageObject.messageOwner.send_state;
                                        if (i2 != i3) {
                                            this.lastSendState = i3;
                                            z3 = true;
                                        }
                                    }
                                    if (z3) {
                                        invalidate();
                                        return z2;
                                    }
                                    r3 = 0;
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
                                i3 = messageObject.messageOwner.send_state;
                                if (i2 != i3) {
                                }
                            }
                            if (z3) {
                            }
                        } else {
                            r3 = 0;
                        }
                        this.user = r3;
                        this.chat = r3;
                        this.encryptedChat = r3;
                        if (this.currentDialogFolderId == 0) {
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
                        if (this.currentDialogFolderId == 0) {
                            Theme.dialogs_archiveAvatarDrawable.setCallback(this);
                            this.avatarDrawable.setAvatarType(2);
                            this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
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
                                TLRPC$Chat tLRPC$Chat2 = this.chat;
                                if (tLRPC$Chat2 != null) {
                                    this.avatarDrawable.setInfo(tLRPC$Chat2);
                                    this.avatarImage.setForUserOrChat(this.chat, this.avatarDrawable);
                                }
                            }
                        }
                        if (z && ((i9 != this.unreadCount || z7 != this.markUnread) && (!this.isDialogCell || System.currentTimeMillis() - this.lastDialogChangedTime > 100))) {
                            valueAnimator = this.countAnimator;
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
                            this.countAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.1
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public void onAnimationEnd(Animator animator) {
                                    DialogCell.this.countChangeProgress = 1.0f;
                                    DialogCell.this.countOldLayout = null;
                                    DialogCell.this.countAnimationStableLayout = null;
                                    DialogCell.this.countAnimationInLayout = null;
                                    DialogCell.this.invalidate();
                                }
                            });
                            if ((i9 != 0 || this.markUnread) && (this.markUnread || !z7)) {
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
                                format = String.format("%d", Integer.valueOf(i9));
                                format2 = String.format("%d", Integer.valueOf(this.unreadCount));
                                if (format.length() != format2.length()) {
                                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(format);
                                    SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(format2);
                                    SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(format2);
                                    for (int i12 = 0; i12 < format.length(); i12++) {
                                        if (format.charAt(i12) == format2.charAt(i12)) {
                                            int i13 = i12 + 1;
                                            spannableStringBuilder.setSpan(new EmptyStubSpan(), i12, i13, 0);
                                            spannableStringBuilder2.setSpan(new EmptyStubSpan(), i12, i13, 0);
                                        } else {
                                            spannableStringBuilder3.setSpan(new EmptyStubSpan(), i12, i12 + 1, 0);
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
                            this.countAnimationIncrement = this.unreadCount <= i9;
                            this.countAnimator.start();
                        }
                        boolean z8 = this.reactionMentionCount == 0;
                        if (!z && z8 != z6) {
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
                            this.reactionsMentionsAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.2
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
                        this.avatarImage.setRoundRadius(AndroidUtilities.dp((this.isDialogCell || (tLRPC$Chat = this.chat) == null || !tLRPC$Chat.forum || this.currentDialogFolderId != 0) ? 28.0f : 16.0f));
                        z5 = z2;
                    }
                } else {
                    this.unreadCount = 0;
                    this.mentionCount = 0;
                    this.reactionMentionCount = 0;
                    this.currentEditDate = 0;
                    this.lastMessageDate = 0;
                    this.clearingDialog = false;
                }
            } else {
                this.drawPin = false;
            }
            z2 = false;
            tLRPC$TL_forumTopic = this.forumTopic;
            if (tLRPC$TL_forumTopic != null) {
            }
            if (this.dialogsType == 2) {
            }
            if (i == 0) {
            }
            this.user = r3;
            this.chat = r3;
            this.encryptedChat = r3;
            if (this.currentDialogFolderId == 0) {
            }
            if (j != 0) {
            }
            if (this.currentDialogFolderId == 0) {
            }
            if (z) {
                valueAnimator = this.countAnimator;
                if (valueAnimator != null) {
                }
                ValueAnimator ofFloat3 = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.countAnimator = ofFloat3;
                ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda2
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator22) {
                        DialogCell.this.lambda$update$0(valueAnimator22);
                    }
                });
                this.countAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        DialogCell.this.countChangeProgress = 1.0f;
                        DialogCell.this.countOldLayout = null;
                        DialogCell.this.countAnimationStableLayout = null;
                        DialogCell.this.countAnimationInLayout = null;
                        DialogCell.this.invalidate();
                    }
                });
                if (i9 != 0) {
                }
                this.countAnimator.setDuration(220L);
                this.countAnimator.setInterpolator(new OvershootInterpolator());
                if (this.drawCount) {
                    format = String.format("%d", Integer.valueOf(i9));
                    format2 = String.format("%d", Integer.valueOf(this.unreadCount));
                    if (format.length() != format2.length()) {
                    }
                }
                this.countWidthOld = this.countWidth;
                this.countLeftOld = this.countLeft;
                this.countAnimationIncrement = this.unreadCount <= i9;
                this.countAnimator.start();
            }
            if (this.reactionMentionCount == 0) {
            }
            if (!z) {
            }
            this.avatarImage.setRoundRadius(AndroidUtilities.dp((this.isDialogCell || (tLRPC$Chat = this.chat) == null || !tLRPC$Chat.forum || this.currentDialogFolderId != 0) ? 28.0f : 16.0f));
            z5 = z2;
        }
        if (!this.isTopic && (getMeasuredWidth() != 0 || getMeasuredHeight() != 0)) {
            buildLayout();
        } else {
            requestLayout();
        }
        if (!z) {
            this.dialogMutedProgress = (this.dialogMuted || this.drawUnmute) ? 1.0f : 0.0f;
            ValueAnimator valueAnimator3 = this.countAnimator;
            if (valueAnimator3 != null) {
                valueAnimator3.cancel();
            }
        }
        invalidate();
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

    private int getTopicId() {
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
    /* JADX WARN: Code restructure failed: missing block: B:625:0x122c, code lost:
        if (r40.reactionsMentionsChangeProgress != 1.0f) goto L626;
     */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0878  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x099c  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x0b1d  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x0c5e  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x0c78  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x0dec  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x0e14  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x12cf  */
    /* JADX WARN: Removed duplicated region for block: B:275:0x12f1  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x1308  */
    /* JADX WARN: Removed duplicated region for block: B:296:0x1350  */
    /* JADX WARN: Removed duplicated region for block: B:299:0x1357  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x170f  */
    /* JADX WARN: Removed duplicated region for block: B:337:0x1716  */
    /* JADX WARN: Removed duplicated region for block: B:344:0x173b  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x17a2  */
    /* JADX WARN: Removed duplicated region for block: B:369:0x17ef  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x1828  */
    /* JADX WARN: Removed duplicated region for block: B:385:0x1883  */
    /* JADX WARN: Removed duplicated region for block: B:391:0x1898  */
    /* JADX WARN: Removed duplicated region for block: B:398:0x18b5  */
    /* JADX WARN: Removed duplicated region for block: B:403:0x18e8  */
    /* JADX WARN: Removed duplicated region for block: B:405:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:407:0x18c3  */
    /* JADX WARN: Removed duplicated region for block: B:416:0x1852  */
    /* JADX WARN: Removed duplicated region for block: B:425:0x17fb  */
    /* JADX WARN: Removed duplicated region for block: B:432:0x1810  */
    /* JADX WARN: Removed duplicated region for block: B:453:0x1438  */
    /* JADX WARN: Removed duplicated region for block: B:486:0x1621  */
    /* JADX WARN: Removed duplicated region for block: B:490:0x16b0  */
    /* JADX WARN: Removed duplicated region for block: B:494:0x16c7  */
    /* JADX WARN: Removed duplicated region for block: B:499:0x16db  */
    /* JADX WARN: Removed duplicated region for block: B:506:0x16f0  */
    /* JADX WARN: Removed duplicated region for block: B:543:0x0e62  */
    /* JADX WARN: Removed duplicated region for block: B:685:0x0ccc  */
    /* JADX WARN: Removed duplicated region for block: B:689:0x0c61  */
    /* JADX WARN: Removed duplicated region for block: B:701:0x0ce2  */
    /* JADX WARN: Removed duplicated region for block: B:714:0x0d2a  */
    /* JADX WARN: Removed duplicated region for block: B:768:0x0be8  */
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
        String str;
        String str2;
        float f;
        RLottieDrawable rLottieDrawable;
        float f2;
        boolean z;
        int i3;
        long j;
        String str3;
        Canvas canvas2;
        float f3;
        int i4;
        String str4;
        Canvas canvas3;
        float f4;
        int i5;
        float f5;
        boolean z2;
        int dp;
        int dp2;
        Paint paint;
        boolean z3;
        int dp3;
        float interpolation;
        long j2;
        float f6;
        boolean z4;
        float f7;
        boolean z5;
        float f8;
        TLRPC$User tLRPC$User;
        TLRPC$Chat tLRPC$Chat;
        float dp4;
        int i6;
        float dp5;
        float f9;
        float dp6;
        float dp7;
        float f10;
        float dp8;
        float dp9;
        float dp10;
        float f11;
        float f12;
        float dp11;
        PullForegroundDrawable pullForegroundDrawable;
        int i7;
        StatusDrawable chatStatusDrawable;
        CustomDialog customDialog;
        PullForegroundDrawable pullForegroundDrawable2;
        if (this.currentDialogId == 0 && this.customDialog == null) {
            return;
        }
        if (this.currentDialogFolderId != 0 && (pullForegroundDrawable2 = this.archivedChatsDrawable) != null && pullForegroundDrawable2.outProgress == 0.0f && this.translationX == 0.0f) {
            if (this.drawingForBlur) {
                return;
            }
            canvas.save();
            canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
            this.archivedChatsDrawable.draw(canvas);
            canvas.restore();
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j3 = elapsedRealtime - this.lastUpdateTime;
        if (j3 > 17) {
            j3 = 17;
        }
        long j4 = j3;
        this.lastUpdateTime = elapsedRealtime;
        if (this.clipProgress != 0.0f && Build.VERSION.SDK_INT != 24) {
            canvas.save();
            canvas.clipRect(0.0f, this.topClip * this.clipProgress, getMeasuredWidth(), getMeasuredHeight() - ((int) (this.bottomClip * this.clipProgress)));
        }
        float f13 = 8.0f;
        if (this.translationX != 0.0f || this.cornerProgress != 0.0f) {
            canvas.save();
            if (this.currentDialogFolderId != 0) {
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
            int i8 = color2;
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
                i2 = i;
                str = string;
                str2 = "chats_archivePinBackground";
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
                i2 = i;
                str = string;
                str2 = "chats_archivePinBackground";
            }
            int measuredWidth2 = (getMeasuredWidth() - AndroidUtilities.dp(43.0f)) - (this.translationDrawable.getIntrinsicWidth() / 2);
            int dp12 = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.0f : 9.0f);
            int intrinsicWidth = (this.translationDrawable.getIntrinsicWidth() / 2) + measuredWidth2;
            int intrinsicHeight = (this.translationDrawable.getIntrinsicHeight() / 2) + dp12;
            if (this.currentRevealProgress > 0.0f) {
                canvas.save();
                canvas.clipRect(measuredWidth - AndroidUtilities.dp(8.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                Theme.dialogs_pinnedPaint.setColor(i8);
                canvas.drawCircle(intrinsicWidth, intrinsicHeight, ((float) Math.sqrt((intrinsicWidth * intrinsicWidth) + ((intrinsicHeight - getMeasuredHeight()) * (intrinsicHeight - getMeasuredHeight())))) * AndroidUtilities.accelerateInterpolator.getInterpolation(this.currentRevealProgress), Theme.dialogs_pinnedPaint);
                canvas.restore();
                if (!Theme.dialogs_archiveDrawableRecolored) {
                    Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor(str2));
                    Theme.dialogs_archiveDrawableRecolored = true;
                }
                if (!Theme.dialogs_hidePsaDrawableRecolored) {
                    Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(str2));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(str2));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(str2));
                    Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                    Theme.dialogs_hidePsaDrawableRecolored = true;
                }
            }
            canvas.save();
            canvas.translate(measuredWidth2, dp12);
            float f14 = this.currentRevealBounceProgress;
            f = 1.0f;
            if (f14 != 0.0f && f14 != 1.0f) {
                float interpolation2 = this.interpolator.getInterpolation(f14) + 1.0f;
                canvas.scale(interpolation2, interpolation2, this.translationDrawable.getIntrinsicWidth() / 2, this.translationDrawable.getIntrinsicHeight() / 2);
            }
            BaseCell.setDrawableBounds((Drawable) this.translationDrawable, 0, 0);
            this.translationDrawable.draw(canvas);
            canvas.restore();
            canvas.clipRect(measuredWidth, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            int ceil = (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str));
            int i9 = i2;
            if (this.swipeMessageTextId != i9 || this.swipeMessageWidth != getMeasuredWidth()) {
                this.swipeMessageTextId = i9;
                this.swipeMessageWidth = getMeasuredWidth();
                StaticLayout staticLayout = new StaticLayout(str, Theme.dialogs_archiveTextPaint, Math.min(AndroidUtilities.dp(80.0f), ceil), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                this.swipeMessageTextLayout = staticLayout;
                if (staticLayout.getLineCount() > 1) {
                    this.swipeMessageTextLayout = new StaticLayout(str, Theme.dialogs_archiveTextPaintSmall, Math.min(AndroidUtilities.dp(82.0f), ceil), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                }
            }
            if (this.swipeMessageTextLayout != null) {
                canvas.save();
                canvas.translate((getMeasuredWidth() - AndroidUtilities.dp(43.0f)) - (this.swipeMessageTextLayout.getWidth() / 2.0f), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 50.0f : 47.0f) + (this.swipeMessageTextLayout.getLineCount() > 1 ? -AndroidUtilities.dp(4.0f) : 0.0f));
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
            f = 1.0f;
        }
        if (this.translationX != 0.0f) {
            canvas.save();
            canvas.translate(this.translationX, 0.0f);
        }
        float dp13 = AndroidUtilities.dp(8.0f) * this.cornerProgress;
        if (this.isSelected) {
            f2 = 0.0f;
            this.rect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            canvas.drawRoundRect(this.rect, dp13, dp13, Theme.dialogs_tabletSeletedPaint);
        } else {
            f2 = 0.0f;
        }
        if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != f2)) {
            Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor("chats_pinnedOverlay", this.resourcesProvider), this.archiveBackgroundProgress, f));
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight() - this.translateY, Theme.dialogs_pinnedPaint);
        } else if (getIsPinned() || this.drawPinBackground) {
            Theme.dialogs_pinnedPaint.setColor(Theme.getColor("chats_pinnedOverlay", this.resourcesProvider));
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight() - this.translateY, Theme.dialogs_pinnedPaint);
        }
        if (this.translationX != 0.0f || this.cornerProgress != 0.0f) {
            canvas.save();
            Theme.dialogs_pinnedPaint.setColor(Theme.getColor("windowBackgroundWhite", this.resourcesProvider));
            this.rect.set(getMeasuredWidth() - AndroidUtilities.dp(64.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
            canvas.drawRoundRect(this.rect, dp13, dp13, Theme.dialogs_pinnedPaint);
            if (this.isSelected) {
                canvas.drawRoundRect(this.rect, dp13, dp13, Theme.dialogs_tabletSeletedPaint);
            }
            if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != 0.0f)) {
                Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor("chats_pinnedOverlay", this.resourcesProvider), this.archiveBackgroundProgress, f));
                canvas.drawRoundRect(this.rect, dp13, dp13, Theme.dialogs_pinnedPaint);
            } else if (getIsPinned() || this.drawPinBackground) {
                Theme.dialogs_pinnedPaint.setColor(Theme.getColor("chats_pinnedOverlay", this.resourcesProvider));
                canvas.drawRoundRect(this.rect, dp13, dp13, Theme.dialogs_pinnedPaint);
            }
            canvas.restore();
        }
        if (this.translationX != 0.0f) {
            float f15 = this.cornerProgress;
            if (f15 < f) {
                float f16 = f15 + (((float) j4) / 150.0f);
                this.cornerProgress = f16;
                if (f16 > f) {
                    this.cornerProgress = f;
                }
                z = true;
            }
            z = false;
        } else {
            float f17 = this.cornerProgress;
            if (f17 > 0.0f) {
                float f18 = f17 - (((float) j4) / 150.0f);
                this.cornerProgress = f18;
                if (f18 < 0.0f) {
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
        float f19 = 10.0f;
        if (this.nameLayout != null) {
            if (!this.nameLayoutEllipsizeByGradient || this.nameLayoutFits) {
                i3 = 0;
            } else {
                if (this.nameLayoutEllipsizeLeft && this.fadePaint == null) {
                    Paint paint2 = new Paint();
                    this.fadePaint = paint2;
                    paint2.setShader(new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(24.0f), 0.0f, new int[]{-1, 0}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                    this.fadePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                } else if (this.fadePaintBack == null) {
                    Paint paint3 = new Paint();
                    this.fadePaintBack = paint3;
                    paint3.setShader(new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(24.0f), 0.0f, new int[]{0, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                    this.fadePaintBack.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                }
                canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), 255, 31);
                int i10 = this.nameLeft;
                i3 = 0;
                canvas.clipRect(i10, 0, this.nameWidth + i10, getMeasuredHeight());
            }
            if (this.currentDialogFolderId != 0) {
                TextPaint[] textPaintArr = Theme.dialogs_namePaint;
                int i11 = this.paintIndex;
                TextPaint textPaint = textPaintArr[i11];
                TextPaint textPaint2 = textPaintArr[i11];
                int color3 = Theme.getColor("chats_nameArchived", this.resourcesProvider);
                textPaint2.linkColor = color3;
                textPaint.setColor(color3);
            } else if (this.encryptedChat != null || ((customDialog = this.customDialog) != null && customDialog.type == 2)) {
                TextPaint[] textPaintArr2 = Theme.dialogs_namePaint;
                int i12 = this.paintIndex;
                TextPaint textPaint3 = textPaintArr2[i12];
                TextPaint textPaint4 = textPaintArr2[i12];
                int color4 = Theme.getColor("chats_secretName", this.resourcesProvider);
                textPaint4.linkColor = color4;
                textPaint3.setColor(color4);
            } else {
                TextPaint[] textPaintArr3 = Theme.dialogs_namePaint;
                int i13 = this.paintIndex;
                TextPaint textPaint5 = textPaintArr3[i13];
                TextPaint textPaint6 = textPaintArr3[i13];
                int color5 = Theme.getColor("chats_name", this.resourcesProvider);
                textPaint6.linkColor = color5;
                textPaint5.setColor(color5);
            }
            canvas.save();
            canvas.translate(this.nameLeft + this.nameLayoutTranslateX, AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 10.0f : 13.0f));
            this.nameLayout.draw(canvas);
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
            }
        } else {
            i3 = 0;
        }
        if (this.timeLayout != null && this.currentDialogFolderId == 0) {
            canvas.save();
            canvas.translate(this.timeLeft, this.timeTop);
            this.timeLayout.draw(canvas);
            canvas.restore();
        }
        if (drawLock2()) {
            Theme.dialogs_lock2Drawable.setBounds(this.lock2Left, this.timeTop + ((this.timeLayout.getHeight() - Theme.dialogs_lock2Drawable.getIntrinsicHeight()) / 2), this.lock2Left + Theme.dialogs_lock2Drawable.getIntrinsicWidth(), this.timeTop + ((this.timeLayout.getHeight() - Theme.dialogs_lock2Drawable.getIntrinsicHeight()) / 2) + Theme.dialogs_lock2Drawable.getIntrinsicHeight());
            Theme.dialogs_lock2Drawable.draw(canvas);
        }
        if (this.messageNameLayout == null || isForumCell()) {
            j = j4;
            str3 = "windowBackgroundWhite";
            canvas2 = canvas;
            f3 = 1.0f;
            i4 = 1;
        } else {
            if (this.currentDialogFolderId != 0) {
                TextPaint textPaint7 = Theme.dialogs_messageNamePaint;
                int color6 = Theme.getColor("chats_nameMessageArchived_threeLines", this.resourcesProvider);
                textPaint7.linkColor = color6;
                textPaint7.setColor(color6);
            } else if (this.draftMessage != null) {
                TextPaint textPaint8 = Theme.dialogs_messageNamePaint;
                int color7 = Theme.getColor("chats_draft", this.resourcesProvider);
                textPaint8.linkColor = color7;
                textPaint8.setColor(color7);
            } else {
                TextPaint textPaint9 = Theme.dialogs_messageNamePaint;
                int color8 = Theme.getColor("chats_nameMessage_threeLines", this.resourcesProvider);
                textPaint9.linkColor = color8;
                textPaint9.setColor(color8);
            }
            canvas.save();
            canvas.translate(this.messageNameLeft, this.messageNameTop);
            try {
                this.messageNameLayout.draw(canvas);
                i4 = 1;
                j = j4;
                str3 = "windowBackgroundWhite";
                f3 = 1.0f;
                canvas2 = canvas;
            } catch (Exception e) {
                e = e;
                j = j4;
                str3 = "windowBackgroundWhite";
                canvas2 = canvas;
                f3 = 1.0f;
                i4 = 1;
            }
            try {
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageNameLayout, this.animatedEmojiStack2, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
            } catch (Exception e2) {
                e = e2;
                FileLog.e(e);
                canvas.restore();
                if (this.messageLayout != null) {
                }
                if (this.buttonLayout != null) {
                }
                if (this.currentDialogFolderId != 0) {
                }
                if (!this.drawUnmute) {
                }
                if (this.dialogsType == i5) {
                }
                if (!this.drawVerified) {
                }
                if (!this.drawReorder) {
                }
                Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                Theme.dialogs_reorderDrawable.draw(canvas3);
                if (this.drawError) {
                }
                if (this.animatingArchiveAvatar) {
                }
                if (this.drawAvatar) {
                }
                if (this.thumbsCount > 0) {
                }
                if (this.animatingArchiveAvatar) {
                }
                if (this.isDialogCell) {
                }
                j2 = j;
                f6 = 0.0f;
                if (this.translationX != f6) {
                }
                if (this.currentDialogFolderId != 0) {
                }
                if (this.useSeparator) {
                }
                if (this.clipProgress != 0.0f) {
                }
                z4 = this.drawReorder;
                if (!z4) {
                }
                if (z4) {
                }
                z = true;
                if (this.archiveHidden) {
                }
            }
            canvas.restore();
        }
        if (this.messageLayout != null) {
            if (this.currentDialogFolderId != 0) {
                if (this.chat != null) {
                    TextPaint[] textPaintArr4 = Theme.dialogs_messagePaint;
                    int i14 = this.paintIndex;
                    TextPaint textPaint10 = textPaintArr4[i14];
                    TextPaint textPaint11 = textPaintArr4[i14];
                    int color9 = Theme.getColor("chats_nameMessageArchived", this.resourcesProvider);
                    textPaint11.linkColor = color9;
                    textPaint10.setColor(color9);
                } else {
                    TextPaint[] textPaintArr5 = Theme.dialogs_messagePaint;
                    int i15 = this.paintIndex;
                    TextPaint textPaint12 = textPaintArr5[i15];
                    TextPaint textPaint13 = textPaintArr5[i15];
                    int color10 = Theme.getColor("chats_messageArchived", this.resourcesProvider);
                    textPaint13.linkColor = color10;
                    textPaint12.setColor(color10);
                }
            } else {
                TextPaint[] textPaintArr6 = Theme.dialogs_messagePaint;
                int i16 = this.paintIndex;
                TextPaint textPaint14 = textPaintArr6[i16];
                TextPaint textPaint15 = textPaintArr6[i16];
                int color11 = Theme.getColor("chats_message", this.resourcesProvider);
                textPaint15.linkColor = color11;
                textPaint14.setColor(color11);
            }
            canvas.save();
            canvas2.translate(this.messageLeft, this.messageTop);
            if (!this.spoilers.isEmpty()) {
                try {
                    canvas.save();
                    SpoilerEffect.clipOutCanvas(canvas2, this.spoilers);
                    this.messageLayout.draw(canvas2);
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageLayout, this.animatedEmojiStack, -0.075f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f);
                    canvas.restore();
                    for (int i17 = 0; i17 < this.spoilers.size(); i17++) {
                        SpoilerEffect spoilerEffect = this.spoilers.get(i17);
                        spoilerEffect.setColor(this.messageLayout.getPaint().getColor());
                        spoilerEffect.draw(canvas2);
                    }
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
            } else {
                this.messageLayout.draw(canvas2);
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageLayout, this.animatedEmojiStack, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
            }
            canvas.restore();
            int i18 = this.printingStringType;
            if (i18 >= 0 && (chatStatusDrawable = Theme.getChatStatusDrawable(i18)) != null) {
                canvas.save();
                int i19 = this.printingStringType;
                if (i19 == i4 || i19 == 4) {
                    canvas2.translate(this.statusDrawableLeft, this.messageTop + (i19 == i4 ? AndroidUtilities.dp(f3) : 0));
                } else {
                    canvas2.translate(this.statusDrawableLeft, this.messageTop + ((AndroidUtilities.dp(18.0f) - chatStatusDrawable.getIntrinsicHeight()) / 2.0f));
                }
                chatStatusDrawable.draw(canvas2);
                int i20 = this.statusDrawableLeft;
                invalidate(i20, this.messageTop, chatStatusDrawable.getIntrinsicWidth() + i20, this.messageTop + chatStatusDrawable.getIntrinsicHeight());
                canvas.restore();
            }
        }
        if (this.buttonLayout != null) {
            canvas.save();
            if (this.buttonBackgroundPaint == null) {
                this.buttonBackgroundPaint = new Paint(i4);
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
            }
            this.canvasButton.setColor(ColorUtils.setAlphaComponent(this.currentMessagePaint.getColor(), 26));
            this.canvasButton.rewind();
            if (this.topMessageTopicEndIndex != this.topMessageTopicStartIndex) {
                RectF rectF = AndroidUtilities.rectTmp;
                StaticLayout staticLayout2 = this.messageLayout;
                rectF.set(this.messageLeft + AndroidUtilities.dp(2.0f), this.messageTop, (this.messageLeft + staticLayout2.getPrimaryHorizontal(Math.min(staticLayout2.getText().length(), this.topMessageTopicEndIndex))) - AndroidUtilities.dp(3.0f), this.buttonTop - AndroidUtilities.dp(4.0f));
                rectF.inset(-AndroidUtilities.dp(8.0f), -AndroidUtilities.dp(4.0f));
                this.canvasButton.addRect(rectF);
            }
            RectF rectF2 = AndroidUtilities.rectTmp;
            rectF2.set(this.messageLeft + AndroidUtilities.dp(2.0f), this.buttonTop + AndroidUtilities.dp(2.0f), this.messageLeft + this.buttonLayout.getLineWidth(i3) + AndroidUtilities.dp(12.0f), this.buttonTop + this.buttonLayout.getHeight());
            rectF2.inset(-AndroidUtilities.dp(8.0f), -AndroidUtilities.dp(3.0f));
            this.canvasButton.addRect(rectF2);
            this.canvasButton.draw(canvas2);
            Theme.dialogs_forum_arrowDrawable.setAlpha(125);
            BaseCell.setDrawableBounds(Theme.dialogs_forum_arrowDrawable, rectF2.right - AndroidUtilities.dp(18.0f), rectF2.top + ((rectF2.height() - Theme.dialogs_forum_arrowDrawable.getIntrinsicHeight()) / 2.0f));
            Theme.dialogs_forum_arrowDrawable.draw(canvas2);
            canvas2.translate(this.messageLeft - this.buttonLayout.getLineLeft(i3), this.buttonTop);
            if (!this.spoilers2.isEmpty()) {
                try {
                    canvas.save();
                    SpoilerEffect.clipOutCanvas(canvas2, this.spoilers2);
                    this.buttonLayout.draw(canvas2);
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.buttonLayout, this.animatedEmojiStack3, -0.075f, this.spoilers2, 0.0f, 0.0f, 0.0f, 1.0f);
                    canvas.restore();
                    for (int i21 = 0; i21 < this.spoilers2.size(); i21++) {
                        SpoilerEffect spoilerEffect2 = this.spoilers2.get(i21);
                        spoilerEffect2.setColor(this.buttonLayout.getPaint().getColor());
                        spoilerEffect2.draw(canvas2);
                    }
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
            } else {
                this.buttonLayout.draw(canvas2);
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.buttonLayout, this.animatedEmojiStack3, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
            }
            canvas.restore();
        }
        if (this.currentDialogFolderId != 0) {
            int i22 = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
            int i23 = this.lastStatusDrawableParams;
            if (i23 >= 0 && i23 != i22 && !this.statusDrawableAnimationInProgress) {
                createStatusDrawableAnimator(i23, i22);
            }
            boolean z6 = this.statusDrawableAnimationInProgress;
            if (z6) {
                i22 = this.animateToStatusDrawableParams;
            }
            boolean z7 = (i22 & 1) != 0;
            boolean z8 = (i22 & 2) != 0;
            boolean z9 = (i22 & 4) != 0;
            if (z6) {
                int i24 = this.animateFromStatusDrawableParams;
                boolean z10 = (i24 & 1) != 0;
                boolean z11 = (i24 & 2) != 0;
                boolean z12 = (i24 & 4) != 0;
                if (!z7 && !z10 && z12 && !z11 && z8 && z9) {
                    str4 = str3;
                    f5 = 0.0f;
                    i5 = 2;
                    drawCheckStatus(canvas, z7, z8, z9, true, this.statusDrawableProgress);
                    canvas3 = canvas2;
                    f4 = 1.0f;
                } else {
                    str4 = str3;
                    Canvas canvas4 = canvas2;
                    i5 = 2;
                    f5 = 0.0f;
                    boolean z13 = z10;
                    f4 = 1.0f;
                    boolean z14 = z11;
                    canvas3 = canvas4;
                    drawCheckStatus(canvas, z13, z14, z12, false, 1.0f - this.statusDrawableProgress);
                    drawCheckStatus(canvas, z7, z8, z9, false, this.statusDrawableProgress);
                }
            } else {
                str4 = str3;
                canvas3 = canvas2;
                f4 = 1.0f;
                i5 = 2;
                f5 = 0.0f;
                drawCheckStatus(canvas, z7, z8, z9, false, 1.0f);
            }
            this.lastStatusDrawableParams = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
        } else {
            str4 = str3;
            canvas3 = canvas2;
            f4 = 1.0f;
            i5 = 2;
            f5 = 0.0f;
        }
        boolean z15 = !this.drawUnmute || this.dialogMuted;
        if (this.dialogsType == i5 && ((z15 || this.dialogMutedProgress > f5) && !this.drawVerified && this.drawScam == 0 && !this.drawPremium)) {
            if (z15) {
                float f20 = this.dialogMutedProgress;
                if (f20 != f4) {
                    float f21 = f20 + 0.10666667f;
                    this.dialogMutedProgress = f21;
                    if (f21 > f4) {
                        this.dialogMutedProgress = f4;
                    } else {
                        invalidate();
                    }
                    float dp14 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                    float dp15 = AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
                    BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp14, dp15);
                    BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp14, dp15);
                    if (this.dialogMutedProgress == f4) {
                        canvas.save();
                        float f22 = this.dialogMutedProgress;
                        canvas3.scale(f22, f22, Theme.dialogs_muteDrawable.getBounds().centerX(), Theme.dialogs_muteDrawable.getBounds().centerY());
                        if (this.drawUnmute) {
                            Theme.dialogs_unmuteDrawable.setAlpha((int) (this.dialogMutedProgress * 255.0f));
                            Theme.dialogs_unmuteDrawable.draw(canvas3);
                            Theme.dialogs_unmuteDrawable.setAlpha(255);
                        } else {
                            Theme.dialogs_muteDrawable.setAlpha((int) (this.dialogMutedProgress * 255.0f));
                            Theme.dialogs_muteDrawable.draw(canvas3);
                            Theme.dialogs_muteDrawable.setAlpha(255);
                        }
                        canvas.restore();
                    } else if (this.drawUnmute) {
                        Theme.dialogs_unmuteDrawable.draw(canvas3);
                    } else {
                        Theme.dialogs_muteDrawable.draw(canvas3);
                    }
                }
            }
            if (!z15) {
                float f23 = this.dialogMutedProgress;
                if (f23 != f5) {
                    float f24 = f23 - 0.10666667f;
                    this.dialogMutedProgress = f24;
                    if (f24 < f5) {
                        this.dialogMutedProgress = f5;
                    } else {
                        invalidate();
                    }
                }
            }
            float dp142 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
            float dp152 = AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
            BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp142, dp152);
            BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp142, dp152);
            if (this.dialogMutedProgress == f4) {
            }
        } else if (!this.drawVerified) {
            BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft - AndroidUtilities.dp(f4), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
            BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft - AndroidUtilities.dp(f4), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
            Theme.dialogs_verifiedDrawable.draw(canvas3);
            Theme.dialogs_verifiedCheckDrawable.draw(canvas3);
        } else if (this.drawPremium) {
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatus;
            if (swapAnimatedEmojiDrawable != null) {
                swapAnimatedEmojiDrawable.setBounds(this.nameMuteLeft - AndroidUtilities.dp(2.0f), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f) - AndroidUtilities.dp(4.0f), this.nameMuteLeft + AndroidUtilities.dp(20.0f), (AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f) - AndroidUtilities.dp(4.0f)) + AndroidUtilities.dp(22.0f));
                this.emojiStatus.setColor(Integer.valueOf(Theme.getColor("chats_verifiedBackground", this.resourcesProvider)));
                this.emojiStatus.draw(canvas3);
            } else {
                Drawable drawable = PremiumGradient.getInstance().premiumStarDrawableMini;
                BaseCell.setDrawableBounds(drawable, this.nameMuteLeft - AndroidUtilities.dp(f4), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f));
                drawable.draw(canvas3);
            }
        } else {
            int i25 = this.drawScam;
            if (i25 != 0) {
                BaseCell.setDrawableBounds((Drawable) (i25 == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable), this.nameMuteLeft, AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.0f : 15.0f));
                (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).draw(canvas3);
                if (!this.drawReorder || this.reorderIconProgress != f5) {
                    Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                    BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                    Theme.dialogs_reorderDrawable.draw(canvas3);
                }
                if (this.drawError) {
                    Theme.dialogs_errorDrawable.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                    this.rect.set(this.errorLeft, this.errorTop, i7 + AndroidUtilities.dp(23.0f), this.errorTop + AndroidUtilities.dp(23.0f));
                    RectF rectF3 = this.rect;
                    float f25 = AndroidUtilities.density;
                    canvas3.drawRoundRect(rectF3, f25 * 11.5f, f25 * 11.5f, Theme.dialogs_errorPaint);
                    BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
                    Theme.dialogs_errorDrawable.draw(canvas3);
                } else {
                    boolean z16 = this.drawCount;
                    if (((z16 || this.drawMention) && this.drawCount2) || this.countChangeProgress != f4 || this.drawReactionMention || this.reactionsMentionsChangeProgress != f4) {
                        boolean z17 = this.isTopic;
                        if (z17) {
                            z2 = this.topicMuted;
                        } else {
                            TLRPC$Chat tLRPC$Chat2 = this.chat;
                            z2 = (tLRPC$Chat2 == null || !tLRPC$Chat2.forum || this.forumTopic != null) ? this.dialogMuted : !this.hasUnmutedTopics;
                        }
                        if ((z16 && this.drawCount2) || this.countChangeProgress != f4) {
                            float f26 = (this.unreadCount != 0 || this.markUnread) ? this.countChangeProgress : f4 - this.countChangeProgress;
                            int i26 = 255;
                            if (z17 && this.forumTopic.read_inbox_max_id == 0) {
                                if (this.topicCounterPaint == null) {
                                    this.topicCounterPaint = new Paint();
                                }
                                paint = this.topicCounterPaint;
                                int color12 = Theme.getColor(z2 ? "topics_unreadCounterMuted" : "topics_unreadCounter", this.resourcesProvider);
                                paint.setColor(color12);
                                Theme.dialogs_countTextPaint.setColor(color12);
                                i26 = z2 ? 30 : 40;
                                z3 = true;
                            } else {
                                paint = (z2 || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
                                z3 = false;
                            }
                            StaticLayout staticLayout3 = this.countOldLayout;
                            if (staticLayout3 == null || this.unreadCount == 0) {
                                if (this.unreadCount != 0) {
                                    staticLayout3 = this.countLayout;
                                }
                                paint.setAlpha((int) ((1.0f - this.reorderIconProgress) * i26));
                                Theme.dialogs_countTextPaint.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                                this.rect.set(this.countLeft - AndroidUtilities.dp(5.5f), this.countTop, dp3 + this.countWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                if (f26 != 1.0f) {
                                    if (getIsPinned()) {
                                        Theme.dialogs_pinnedDrawable.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                                        BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                                        canvas.save();
                                        float f27 = 1.0f - f26;
                                        canvas3.scale(f27, f27, Theme.dialogs_pinnedDrawable.getBounds().centerX(), Theme.dialogs_pinnedDrawable.getBounds().centerY());
                                        Theme.dialogs_pinnedDrawable.draw(canvas3);
                                        canvas.restore();
                                    }
                                    canvas.save();
                                    canvas3.scale(f26, f26, this.rect.centerX(), this.rect.centerY());
                                }
                                RectF rectF4 = this.rect;
                                float f28 = AndroidUtilities.density;
                                canvas3.drawRoundRect(rectF4, f28 * 11.5f, f28 * 11.5f, paint);
                                if (staticLayout3 != null) {
                                    canvas.save();
                                    canvas3.translate(this.countLeft, this.countTop + AndroidUtilities.dp(4.0f));
                                    staticLayout3.draw(canvas3);
                                    canvas.restore();
                                }
                                if (f26 != 1.0f) {
                                    canvas.restore();
                                }
                            } else {
                                paint.setAlpha((int) ((f4 - this.reorderIconProgress) * i26));
                                Theme.dialogs_countTextPaint.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                float f29 = f26 * 2.0f;
                                float f30 = f29 > f4 ? 1.0f : f29;
                                float f31 = f4 - f30;
                                float f32 = (this.countLeft * f30) + (this.countLeftOld * f31);
                                float dp16 = f32 - AndroidUtilities.dp(5.5f);
                                this.rect.set(dp16, this.countTop, (this.countWidth * f30) + dp16 + (this.countWidthOld * f31) + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                if (f26 <= 0.5f) {
                                    interpolation = (CubicBezierInterpolator.EASE_OUT.getInterpolation(f29) * 0.1f) + 1.0f;
                                } else {
                                    interpolation = (CubicBezierInterpolator.EASE_IN.getInterpolation(1.0f - ((f26 - 0.5f) * 2.0f)) * 0.1f) + 1.0f;
                                }
                                canvas.save();
                                canvas3.scale(interpolation, interpolation, this.rect.centerX(), this.rect.centerY());
                                RectF rectF5 = this.rect;
                                float f33 = AndroidUtilities.density;
                                canvas3.drawRoundRect(rectF5, f33 * 11.5f, f33 * 11.5f, paint);
                                if (this.countAnimationStableLayout != null) {
                                    canvas.save();
                                    canvas3.translate(f32, this.countTop + AndroidUtilities.dp(4.0f));
                                    this.countAnimationStableLayout.draw(canvas3);
                                    canvas.restore();
                                }
                                int alpha = Theme.dialogs_countTextPaint.getAlpha();
                                float f34 = alpha;
                                Theme.dialogs_countTextPaint.setAlpha((int) (f34 * f30));
                                if (this.countAnimationInLayout != null) {
                                    canvas.save();
                                    canvas3.translate(f32, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * f31) + this.countTop + AndroidUtilities.dp(4.0f));
                                    this.countAnimationInLayout.draw(canvas3);
                                    canvas.restore();
                                } else if (this.countLayout != null) {
                                    canvas.save();
                                    canvas3.translate(f32, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * f31) + this.countTop + AndroidUtilities.dp(4.0f));
                                    this.countLayout.draw(canvas3);
                                    canvas.restore();
                                }
                                if (this.countOldLayout != null) {
                                    Theme.dialogs_countTextPaint.setAlpha((int) (f34 * f31));
                                    canvas.save();
                                    canvas3.translate(f32, ((this.countAnimationIncrement ? -AndroidUtilities.dp(13.0f) : AndroidUtilities.dp(13.0f)) * f30) + this.countTop + AndroidUtilities.dp(4.0f));
                                    this.countOldLayout.draw(canvas3);
                                    canvas.restore();
                                }
                                Theme.dialogs_countTextPaint.setAlpha(alpha);
                                canvas.restore();
                            }
                            if (z3) {
                                Theme.dialogs_countTextPaint.setColor(Theme.getColor("chats_unreadCounterText"));
                            }
                        }
                        if (this.drawMention) {
                            Theme.dialogs_countPaint.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                            this.rect.set(this.mentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp2 + this.mentionWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                            Paint paint4 = (!z2 || this.folderId == 0) ? Theme.dialogs_countPaint : Theme.dialogs_countGrayPaint;
                            RectF rectF6 = this.rect;
                            float f35 = AndroidUtilities.density;
                            canvas3.drawRoundRect(rectF6, f35 * 11.5f, f35 * 11.5f, paint4);
                            if (this.mentionLayout != null) {
                                Theme.dialogs_countTextPaint.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                                canvas.save();
                                canvas3.translate(this.mentionLeft, this.countTop + AndroidUtilities.dp(4.0f));
                                this.mentionLayout.draw(canvas3);
                                canvas.restore();
                            } else {
                                Theme.dialogs_mentionDrawable.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                                BaseCell.setDrawableBounds(Theme.dialogs_mentionDrawable, this.mentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.2f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                Theme.dialogs_mentionDrawable.draw(canvas3);
                            }
                        }
                        float f36 = !this.drawReactionMention ? 1.0f : 1.0f;
                        Theme.dialogs_reactionsCountPaint.setAlpha((int) ((f36 - this.reorderIconProgress) * 255.0f));
                        this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                        Paint paint5 = Theme.dialogs_reactionsCountPaint;
                        canvas.save();
                        float f37 = this.reactionsMentionsChangeProgress;
                        if (f37 != 1.0f) {
                            if (!this.drawReactionMention) {
                                f37 = 1.0f - f37;
                            }
                            canvas3.scale(f37, f37, this.rect.centerX(), this.rect.centerY());
                        }
                        RectF rectF7 = this.rect;
                        float f38 = AndroidUtilities.density;
                        canvas3.drawRoundRect(rectF7, f38 * 11.5f, f38 * 11.5f, paint5);
                        Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                        BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                        Theme.dialogs_reactionsMentionDrawable.draw(canvas3);
                        canvas.restore();
                    } else if (getIsPinned()) {
                        Theme.dialogs_pinnedDrawable.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                        BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                        Theme.dialogs_pinnedDrawable.draw(canvas3);
                    }
                }
                if (this.animatingArchiveAvatar) {
                    canvas.save();
                    float interpolation3 = this.interpolator.getInterpolation(this.animatingArchiveAvatarProgress / 170.0f) + 1.0f;
                    canvas3.scale(interpolation3, interpolation3, this.avatarImage.getCenterX(), this.avatarImage.getCenterY());
                }
                if (this.drawAvatar && (this.currentDialogFolderId == 0 || (pullForegroundDrawable = this.archivedChatsDrawable) == null || !pullForegroundDrawable.isDraw())) {
                    this.avatarImage.draw(canvas3);
                }
                if (this.thumbsCount > 0) {
                    for (int i27 = 0; i27 < this.thumbsCount; i27++) {
                        this.thumbImage[i27].draw(canvas3);
                        if (this.drawPlay[i27]) {
                            BaseCell.setDrawableBounds(Theme.dialogs_playDrawable, (int) (this.thumbImage[i27].getCenterX() - (Theme.dialogs_playDrawable.getIntrinsicWidth() / 2)), (int) (this.thumbImage[i27].getCenterY() - (Theme.dialogs_playDrawable.getIntrinsicHeight() / 2)));
                            Theme.dialogs_playDrawable.draw(canvas3);
                        }
                    }
                }
                if (this.animatingArchiveAvatar) {
                    canvas.restore();
                }
                if (this.isDialogCell && this.currentDialogFolderId == 0) {
                    tLRPC$User = this.user;
                    if (tLRPC$User == null && !MessagesController.isSupportUser(tLRPC$User) && !this.user.bot) {
                        boolean isOnline = isOnline();
                        if (isOnline || this.onlineProgress != 0.0f) {
                            float imageY2 = this.avatarImage.getImageY2();
                            if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                f13 = 6.0f;
                            }
                            int dp17 = (int) (imageY2 - AndroidUtilities.dp(f13));
                            if (LocaleController.isRTL) {
                                float imageX = this.avatarImage.getImageX();
                                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                    f19 = 6.0f;
                                }
                                dp11 = imageX + AndroidUtilities.dp(f19);
                            } else {
                                float imageX2 = this.avatarImage.getImageX2();
                                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                    f19 = 6.0f;
                                }
                                dp11 = imageX2 - AndroidUtilities.dp(f19);
                            }
                            Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(str4, this.resourcesProvider));
                            float f39 = (int) dp11;
                            float f40 = dp17;
                            canvas3.drawCircle(f39, f40, AndroidUtilities.dp(7.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                            Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle", this.resourcesProvider));
                            canvas3.drawCircle(f39, f40, AndroidUtilities.dp(5.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                            if (isOnline) {
                                float f41 = this.onlineProgress;
                                if (f41 < 1.0f) {
                                    j2 = j;
                                    float f42 = f41 + (((float) j2) / 150.0f);
                                    this.onlineProgress = f42;
                                    if (f42 > 1.0f) {
                                        this.onlineProgress = 1.0f;
                                    }
                                }
                            } else {
                                j2 = j;
                                float f43 = this.onlineProgress;
                                if (f43 > 0.0f) {
                                    float f44 = f43 - (((float) j2) / 150.0f);
                                    this.onlineProgress = f44;
                                    if (f44 < 0.0f) {
                                        this.onlineProgress = 0.0f;
                                    }
                                }
                            }
                            z = true;
                        }
                    } else {
                        j2 = j;
                        tLRPC$Chat = this.chat;
                        if (tLRPC$Chat != null) {
                            boolean z18 = tLRPC$Chat.call_active && tLRPC$Chat.call_not_empty;
                            this.hasCall = z18;
                            if (z18 || this.chatCallProgress != 0.0f) {
                                CheckBox2 checkBox2 = this.checkBox;
                                float progress = (checkBox2 == null || !checkBox2.isChecked()) ? 1.0f : 1.0f - this.checkBox.getProgress();
                                float imageY22 = this.avatarImage.getImageY2();
                                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                    f13 = 6.0f;
                                }
                                int dp18 = (int) (imageY22 - AndroidUtilities.dp(f13));
                                if (LocaleController.isRTL) {
                                    float imageX3 = this.avatarImage.getImageX();
                                    if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                        f19 = 6.0f;
                                    }
                                    dp4 = imageX3 + AndroidUtilities.dp(f19);
                                } else {
                                    float imageX22 = this.avatarImage.getImageX2();
                                    if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                        f19 = 6.0f;
                                    }
                                    dp4 = imageX22 - AndroidUtilities.dp(f19);
                                }
                                Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(str4, this.resourcesProvider));
                                float f45 = (int) dp4;
                                float f46 = dp18;
                                canvas3.drawCircle(f45, f46, AndroidUtilities.dp(11.0f) * this.chatCallProgress * progress, Theme.dialogs_onlineCirclePaint);
                                Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle", this.resourcesProvider));
                                canvas3.drawCircle(f45, f46, AndroidUtilities.dp(9.0f) * this.chatCallProgress * progress, Theme.dialogs_onlineCirclePaint);
                                Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(str4, this.resourcesProvider));
                                int i28 = this.progressStage;
                                if (i28 == 0) {
                                    dp5 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                    dp9 = AndroidUtilities.dp(3.0f);
                                    dp10 = AndroidUtilities.dp(2.0f);
                                    f11 = this.innerProgress;
                                } else {
                                    if (i28 == 1) {
                                        dp5 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                        f9 = 1.0f;
                                        dp6 = AndroidUtilities.dp(1.0f);
                                        dp7 = AndroidUtilities.dp(4.0f);
                                        f10 = this.innerProgress;
                                    } else if (i28 == 2) {
                                        dp5 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                        dp9 = AndroidUtilities.dp(5.0f);
                                        dp10 = AndroidUtilities.dp(4.0f);
                                        f11 = this.innerProgress;
                                    } else if (i28 == 3) {
                                        dp5 = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                        f9 = 1.0f;
                                        dp6 = AndroidUtilities.dp(1.0f);
                                        dp7 = AndroidUtilities.dp(2.0f);
                                        f10 = this.innerProgress;
                                    } else if (i28 == 4) {
                                        dp5 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                        dp9 = AndroidUtilities.dp(3.0f);
                                        dp10 = AndroidUtilities.dp(2.0f);
                                        f11 = this.innerProgress;
                                    } else if (i28 == 5) {
                                        dp5 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                        f9 = 1.0f;
                                        dp6 = AndroidUtilities.dp(1.0f);
                                        dp7 = AndroidUtilities.dp(4.0f);
                                        f10 = this.innerProgress;
                                    } else if (i28 == 6) {
                                        dp5 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                        dp8 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                        f9 = 1.0f;
                                        if (this.chatCallProgress >= f9 || progress < f9) {
                                            canvas.save();
                                            float f47 = this.chatCallProgress;
                                            canvas3.scale(f47 * progress, f47 * progress, f45, f46);
                                        }
                                        this.rect.set(i6 - AndroidUtilities.dp(1.0f), f46 - dp5, AndroidUtilities.dp(1.0f) + i6, dp5 + f46);
                                        canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                        float f48 = f46 - dp8;
                                        float f49 = f46 + dp8;
                                        this.rect.set(i6 - AndroidUtilities.dp(5.0f), f48, i6 - AndroidUtilities.dp(3.0f), f49);
                                        canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                        this.rect.set(AndroidUtilities.dp(3.0f) + i6, f48, i6 + AndroidUtilities.dp(5.0f), f49);
                                        canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                        if (this.chatCallProgress >= 1.0f || progress < 1.0f) {
                                            canvas.restore();
                                        }
                                        float f50 = (float) j2;
                                        f12 = this.innerProgress + (f50 / 400.0f);
                                        this.innerProgress = f12;
                                        if (f12 >= 1.0f) {
                                            this.innerProgress = 0.0f;
                                            int i29 = this.progressStage + 1;
                                            this.progressStage = i29;
                                            if (i29 >= 8) {
                                                this.progressStage = 0;
                                            }
                                        }
                                        if (this.hasCall) {
                                            float f51 = this.chatCallProgress;
                                            if (f51 < 1.0f) {
                                                float f52 = f51 + (f50 / 150.0f);
                                                this.chatCallProgress = f52;
                                                if (f52 > 1.0f) {
                                                    this.chatCallProgress = 1.0f;
                                                }
                                            }
                                            f6 = 0.0f;
                                        } else {
                                            float f53 = this.chatCallProgress;
                                            f6 = 0.0f;
                                            if (f53 > 0.0f) {
                                                float f54 = f53 - (f50 / 150.0f);
                                                this.chatCallProgress = f54;
                                                if (f54 < 0.0f) {
                                                    this.chatCallProgress = 0.0f;
                                                }
                                            }
                                        }
                                        z = true;
                                        if (this.translationX != f6) {
                                            canvas.restore();
                                        }
                                        if (this.currentDialogFolderId != 0 && this.translationX == f6 && this.archivedChatsDrawable != null) {
                                            canvas.save();
                                            canvas3.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                                            this.archivedChatsDrawable.draw(canvas3);
                                            canvas.restore();
                                        }
                                        if (this.useSeparator) {
                                            int dp19 = (this.fullSeparator || (this.currentDialogFolderId != 0 && this.archiveHidden && !this.fullSeparator2) || (this.fullSeparator2 && !this.archiveHidden)) ? 0 : AndroidUtilities.dp(this.messagePaddingStart);
                                            if (LocaleController.isRTL) {
                                                canvas.drawLine(0.0f, getMeasuredHeight() - 1, getMeasuredWidth() - dp19, getMeasuredHeight() - 1, Theme.dividerPaint);
                                            } else {
                                                canvas.drawLine(dp19, getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
                                                if (this.clipProgress != 0.0f) {
                                                    if (Build.VERSION.SDK_INT != 24) {
                                                        canvas.restore();
                                                    } else {
                                                        Theme.dialogs_pinnedPaint.setColor(Theme.getColor(str4, this.resourcesProvider));
                                                        canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), this.topClip * this.clipProgress, Theme.dialogs_pinnedPaint);
                                                        canvas.drawRect(0.0f, getMeasuredHeight() - ((int) (this.bottomClip * this.clipProgress)), getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
                                                    }
                                                }
                                                z4 = this.drawReorder;
                                                if (!z4 || this.reorderIconProgress != 0.0f) {
                                                    if (z4) {
                                                        float f55 = this.reorderIconProgress;
                                                        if (f55 < 1.0f) {
                                                            float f56 = f55 + (((float) j2) / 170.0f);
                                                            this.reorderIconProgress = f56;
                                                            if (f56 > 1.0f) {
                                                                this.reorderIconProgress = 1.0f;
                                                            }
                                                            f7 = 0.0f;
                                                        }
                                                    } else {
                                                        float f57 = this.reorderIconProgress;
                                                        f7 = 0.0f;
                                                        if (f57 > 0.0f) {
                                                            float f58 = f57 - (((float) j2) / 170.0f);
                                                            this.reorderIconProgress = f58;
                                                            if (f58 < 0.0f) {
                                                                this.reorderIconProgress = 0.0f;
                                                            }
                                                        }
                                                        if (this.archiveHidden) {
                                                            float f59 = this.archiveBackgroundProgress;
                                                            if (f59 > f7) {
                                                                float f60 = f59 - (((float) j2) / 230.0f);
                                                                this.archiveBackgroundProgress = f60;
                                                                if (f60 < f7) {
                                                                    this.archiveBackgroundProgress = f7;
                                                                }
                                                                if (this.avatarDrawable.getAvatarType() == 2) {
                                                                    this.avatarDrawable.setArchivedAvatarHiddenProgress(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.archiveBackgroundProgress));
                                                                }
                                                                z = true;
                                                            }
                                                            if (this.animatingArchiveAvatar) {
                                                                float f61 = this.animatingArchiveAvatarProgress + ((float) j2);
                                                                this.animatingArchiveAvatarProgress = f61;
                                                                if (f61 >= 170.0f) {
                                                                    this.animatingArchiveAvatarProgress = 170.0f;
                                                                    this.animatingArchiveAvatar = false;
                                                                }
                                                                z = true;
                                                            }
                                                            if (!this.drawRevealBackground) {
                                                                float f62 = this.currentRevealBounceProgress;
                                                                if (f62 < 1.0f) {
                                                                    float f63 = f62 + (((float) j2) / 170.0f);
                                                                    this.currentRevealBounceProgress = f63;
                                                                    if (f63 > 1.0f) {
                                                                        this.currentRevealBounceProgress = 1.0f;
                                                                        z5 = true;
                                                                        f8 = this.currentRevealProgress;
                                                                        if (f8 < 1.0f) {
                                                                            float f64 = f8 + (((float) j2) / 300.0f);
                                                                            this.currentRevealProgress = f64;
                                                                            if (f64 > 1.0f) {
                                                                                this.currentRevealProgress = 1.0f;
                                                                            }
                                                                            z5 = true;
                                                                        }
                                                                        if (!z5) {
                                                                            return;
                                                                        }
                                                                        invalidate();
                                                                        return;
                                                                    }
                                                                }
                                                                z5 = z;
                                                                f8 = this.currentRevealProgress;
                                                                if (f8 < 1.0f) {
                                                                }
                                                                if (!z5) {
                                                                }
                                                            } else {
                                                                if (this.currentRevealBounceProgress == 1.0f) {
                                                                    this.currentRevealBounceProgress = 0.0f;
                                                                    z5 = true;
                                                                } else {
                                                                    z5 = z;
                                                                }
                                                                float f65 = this.currentRevealProgress;
                                                                if (f65 > 0.0f) {
                                                                    float f66 = f65 - (((float) j2) / 300.0f);
                                                                    this.currentRevealProgress = f66;
                                                                    if (f66 < 0.0f) {
                                                                        this.currentRevealProgress = 0.0f;
                                                                    }
                                                                    z5 = true;
                                                                }
                                                                if (!z5) {
                                                                }
                                                            }
                                                        } else {
                                                            float f67 = this.archiveBackgroundProgress;
                                                            if (f67 < 1.0f) {
                                                                float f68 = f67 + (((float) j2) / 230.0f);
                                                                this.archiveBackgroundProgress = f68;
                                                                if (f68 > 1.0f) {
                                                                    this.archiveBackgroundProgress = 1.0f;
                                                                }
                                                                if (this.avatarDrawable.getAvatarType() == 2) {
                                                                    this.avatarDrawable.setArchivedAvatarHiddenProgress(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.archiveBackgroundProgress));
                                                                }
                                                                z = true;
                                                            }
                                                            if (this.animatingArchiveAvatar) {
                                                            }
                                                            if (!this.drawRevealBackground) {
                                                            }
                                                        }
                                                    }
                                                    z = true;
                                                    if (this.archiveHidden) {
                                                    }
                                                }
                                                f7 = 0.0f;
                                                if (this.archiveHidden) {
                                                }
                                            }
                                        }
                                        if (this.clipProgress != 0.0f) {
                                        }
                                        z4 = this.drawReorder;
                                        if (!z4) {
                                        }
                                        if (z4) {
                                        }
                                        z = true;
                                        if (this.archiveHidden) {
                                        }
                                    } else {
                                        dp5 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                        f9 = 1.0f;
                                        dp6 = AndroidUtilities.dp(1.0f);
                                        dp7 = AndroidUtilities.dp(2.0f);
                                        f10 = this.innerProgress;
                                    }
                                    dp8 = dp6 + (dp7 * f10);
                                    if (this.chatCallProgress >= f9) {
                                    }
                                    canvas.save();
                                    float f472 = this.chatCallProgress;
                                    canvas3.scale(f472 * progress, f472 * progress, f45, f46);
                                    this.rect.set(i6 - AndroidUtilities.dp(1.0f), f46 - dp5, AndroidUtilities.dp(1.0f) + i6, dp5 + f46);
                                    canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                    float f482 = f46 - dp8;
                                    float f492 = f46 + dp8;
                                    this.rect.set(i6 - AndroidUtilities.dp(5.0f), f482, i6 - AndroidUtilities.dp(3.0f), f492);
                                    canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                    this.rect.set(AndroidUtilities.dp(3.0f) + i6, f482, i6 + AndroidUtilities.dp(5.0f), f492);
                                    canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                    if (this.chatCallProgress >= 1.0f) {
                                    }
                                    canvas.restore();
                                    float f502 = (float) j2;
                                    f12 = this.innerProgress + (f502 / 400.0f);
                                    this.innerProgress = f12;
                                    if (f12 >= 1.0f) {
                                    }
                                    if (this.hasCall) {
                                    }
                                    z = true;
                                    if (this.translationX != f6) {
                                    }
                                    if (this.currentDialogFolderId != 0) {
                                        canvas.save();
                                        canvas3.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                                        this.archivedChatsDrawable.draw(canvas3);
                                        canvas.restore();
                                    }
                                    if (this.useSeparator) {
                                    }
                                    if (this.clipProgress != 0.0f) {
                                    }
                                    z4 = this.drawReorder;
                                    if (!z4) {
                                    }
                                    if (z4) {
                                    }
                                    z = true;
                                    if (this.archiveHidden) {
                                    }
                                }
                                dp8 = dp9 - (dp10 * f11);
                                f9 = 1.0f;
                                if (this.chatCallProgress >= f9) {
                                }
                                canvas.save();
                                float f4722 = this.chatCallProgress;
                                canvas3.scale(f4722 * progress, f4722 * progress, f45, f46);
                                this.rect.set(i6 - AndroidUtilities.dp(1.0f), f46 - dp5, AndroidUtilities.dp(1.0f) + i6, dp5 + f46);
                                canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                float f4822 = f46 - dp8;
                                float f4922 = f46 + dp8;
                                this.rect.set(i6 - AndroidUtilities.dp(5.0f), f4822, i6 - AndroidUtilities.dp(3.0f), f4922);
                                canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                this.rect.set(AndroidUtilities.dp(3.0f) + i6, f4822, i6 + AndroidUtilities.dp(5.0f), f4922);
                                canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                if (this.chatCallProgress >= 1.0f) {
                                }
                                canvas.restore();
                                float f5022 = (float) j2;
                                f12 = this.innerProgress + (f5022 / 400.0f);
                                this.innerProgress = f12;
                                if (f12 >= 1.0f) {
                                }
                                if (this.hasCall) {
                                }
                                z = true;
                                if (this.translationX != f6) {
                                }
                                if (this.currentDialogFolderId != 0) {
                                }
                                if (this.useSeparator) {
                                }
                                if (this.clipProgress != 0.0f) {
                                }
                                z4 = this.drawReorder;
                                if (!z4) {
                                }
                                if (z4) {
                                }
                                z = true;
                                if (this.archiveHidden) {
                                }
                            }
                        }
                    }
                    f6 = 0.0f;
                    if (this.translationX != f6) {
                    }
                    if (this.currentDialogFolderId != 0) {
                    }
                    if (this.useSeparator) {
                    }
                    if (this.clipProgress != 0.0f) {
                    }
                    z4 = this.drawReorder;
                    if (!z4) {
                    }
                    if (z4) {
                    }
                    z = true;
                    if (this.archiveHidden) {
                    }
                }
                j2 = j;
                f6 = 0.0f;
                if (this.translationX != f6) {
                }
                if (this.currentDialogFolderId != 0) {
                }
                if (this.useSeparator) {
                }
                if (this.clipProgress != 0.0f) {
                }
                z4 = this.drawReorder;
                if (!z4) {
                }
                if (z4) {
                }
                z = true;
                if (this.archiveHidden) {
                }
            }
        }
        if (!this.drawReorder) {
        }
        Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
        BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
        Theme.dialogs_reorderDrawable.draw(canvas3);
        if (this.drawError) {
        }
        if (this.animatingArchiveAvatar) {
        }
        if (this.drawAvatar) {
            this.avatarImage.draw(canvas3);
        }
        if (this.thumbsCount > 0) {
        }
        if (this.animatingArchiveAvatar) {
        }
        if (this.isDialogCell) {
            tLRPC$User = this.user;
            if (tLRPC$User == null) {
            }
            j2 = j;
            tLRPC$Chat = this.chat;
            if (tLRPC$Chat != null) {
            }
            f6 = 0.0f;
            if (this.translationX != f6) {
            }
            if (this.currentDialogFolderId != 0) {
            }
            if (this.useSeparator) {
            }
            if (this.clipProgress != 0.0f) {
            }
            z4 = this.drawReorder;
            if (!z4) {
            }
            if (z4) {
            }
            z = true;
            if (this.archiveHidden) {
            }
        }
        j2 = j;
        f6 = 0.0f;
        if (this.translationX != f6) {
        }
        if (this.currentDialogFolderId != 0) {
        }
        if (this.useSeparator) {
        }
        if (this.clipProgress != 0.0f) {
        }
        z4 = this.drawReorder;
        if (!z4) {
        }
        if (z4) {
        }
        z = true;
        if (this.archiveHidden) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDraw$2() {
        this.delegate.onButtonClicked(this);
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
                DialogCell.this.lambda$createStatusDrawableAnimator$3(valueAnimator);
            }
        });
        this.statusDrawableAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.3
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
    public /* synthetic */ void lambda$createStatusDrawableAnimator$3(ValueAnimator valueAnimator) {
        this.statusDrawableProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    public void startOutAnimation() {
        PullForegroundDrawable pullForegroundDrawable = this.archivedChatsDrawable;
        if (pullForegroundDrawable != null) {
            pullForegroundDrawable.outCy = this.avatarImage.getCenterY();
            this.archivedChatsDrawable.outCx = this.avatarImage.getCenterX();
            this.archivedChatsDrawable.outRadius = this.avatarImage.getImageWidth() / 2.0f;
            this.archivedChatsDrawable.outImageSize = this.avatarImage.getBitmapWidth();
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
                sb2.append(captionMessage);
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
        String str;
        String restrictionReason = MessagesController.getRestrictionReason(this.message.messageOwner.restriction_reason);
        ArrayList<MessageObject> arrayList = this.groupMessages;
        int i = 40;
        String str2 = "article";
        int i2 = 1;
        if (arrayList != null && arrayList.size() > 1 && TextUtils.isEmpty(restrictionReason) && this.currentDialogFolderId == 0 && this.encryptedChat == null) {
            this.thumbsCount = 0;
            this.hasVideoThumb = false;
            Collections.sort(this.groupMessages, DialogCell$$ExternalSyntheticLambda4.INSTANCE);
            int i3 = 0;
            while (i3 < this.groupMessages.size()) {
                MessageObject messageObject = this.groupMessages.get(i3);
                if (messageObject != null && !messageObject.needDrawBluredPreview() && (messageObject.isPhoto() || messageObject.isNewGif() || messageObject.isVideo() || messageObject.isRoundVideo())) {
                    String str3 = messageObject.isWebpage() ? messageObject.messageOwner.media.webpage.type : null;
                    if (!"app".equals(str3) && !"profile".equals(str3) && !str2.equals(str3) && (str3 == null || !str3.startsWith("telegram_"))) {
                        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, i);
                        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                        if (closestPhotoSizeWithSize == closestPhotoSizeWithSize2) {
                            closestPhotoSizeWithSize2 = null;
                        }
                        if (closestPhotoSizeWithSize != null) {
                            this.hasVideoThumb = this.hasVideoThumb || messageObject.isVideo() || messageObject.isRoundVideo();
                            if (i3 < 2) {
                                this.thumbsCount += i2;
                                this.drawPlay[i3] = messageObject.isVideo() || messageObject.isRoundVideo();
                                str = str2;
                                this.thumbImage[i3].setImage(ImageLocation.getForObject(closestPhotoSizeWithSize2, messageObject.photoThumbsObject), "20_20", ImageLocation.getForObject(closestPhotoSizeWithSize, messageObject.photoThumbsObject), "20_20", (messageObject.type != i2 || closestPhotoSizeWithSize2 == null) ? 0 : closestPhotoSizeWithSize2.size, null, messageObject, 0);
                                this.thumbImage[i3].setRoundRadius(messageObject.isRoundVideo() ? AndroidUtilities.dp(18.0f) : AndroidUtilities.dp(2.0f));
                                this.needEmoji = false;
                                i3++;
                                str2 = str;
                                i = 40;
                                i2 = 1;
                            }
                        }
                    }
                }
                str = str2;
                i3++;
                str2 = str;
                i = 40;
                i2 = 1;
            }
            return;
        }
        MessageObject messageObject2 = this.message;
        if (messageObject2 == null || this.currentDialogFolderId != 0) {
            return;
        }
        this.thumbsCount = 0;
        this.hasVideoThumb = false;
        if (messageObject2.needDrawBluredPreview()) {
            return;
        }
        if (!this.message.isPhoto() && !this.message.isNewGif() && !this.message.isVideo() && !this.message.isRoundVideo()) {
            return;
        }
        String str4 = this.message.isWebpage() ? this.message.messageOwner.media.webpage.type : null;
        if ("app".equals(str4) || "profile".equals(str4) || str2.equals(str4)) {
            return;
        }
        if (str4 != null && str4.startsWith("telegram_")) {
            return;
        }
        TLRPC$PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, 40);
        TLRPC$PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, AndroidUtilities.getPhotoSize());
        TLRPC$PhotoSize tLRPC$PhotoSize = closestPhotoSizeWithSize3 == closestPhotoSizeWithSize4 ? null : closestPhotoSizeWithSize4;
        if (closestPhotoSizeWithSize3 == null) {
            return;
        }
        this.hasVideoThumb = this.hasVideoThumb || this.message.isVideo() || this.message.isRoundVideo();
        int i4 = this.thumbsCount;
        if (i4 >= 3) {
            return;
        }
        this.thumbsCount = i4 + 1;
        this.drawPlay[0] = this.message.isVideo() || this.message.isRoundVideo();
        MessageObject messageObject3 = this.message;
        this.thumbImage[0].setImage(ImageLocation.getForObject(tLRPC$PhotoSize, messageObject3.photoThumbsObject), "20_20", ImageLocation.getForObject(closestPhotoSizeWithSize3, this.message.photoThumbsObject), "20_20", (messageObject3.type != 1 || tLRPC$PhotoSize == null) ? 0 : tLRPC$PhotoSize.size, null, this.message, 0);
        this.thumbImage[0].setRoundRadius(this.message.isRoundVideo() ? AndroidUtilities.dp(18.0f) : AndroidUtilities.dp(2.0f));
        this.needEmoji = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$updateMessageThumbs$4(MessageObject messageObject, MessageObject messageObject2) {
        return messageObject.getId() - messageObject2.getId();
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
    /* JADX WARN: Removed duplicated region for block: B:85:0x0257  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x026b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r15v0, types: [org.telegram.ui.Cells.DialogCell, android.view.ViewGroup] */
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
        int i = 0;
        if (!TextUtils.isEmpty(str2)) {
            return SpannableStringBuilder.valueOf(AndroidUtilities.formatSpannable(str, str2, charSequence));
        }
        if (MessageObject.isTopicActionMessage(this.message)) {
            MessageObject messageObject2 = this.message;
            CharSequence charSequence5 = messageObject2.messageTextShort;
            if (charSequence5 == null || ((messageObject2.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate) && this.isTopic)) {
                charSequence5 = messageObject2.messageText;
            }
            SpannableStringBuilder formatSpannable = AndroidUtilities.formatSpannable(str, charSequence5, charSequence);
            if (this.message.topicIconDrawable[0] == null || (findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-this.message.getDialogId(), MessageObject.getTopicId(this.message.messageOwner))) == null) {
                return formatSpannable;
            }
            this.message.topicIconDrawable[0].setColor(findTopic.icon_color);
            return formatSpannable;
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
            TLRPC$Message tLRPC$Message2 = captionMessage.messageOwner;
            if (tLRPC$Message2 != null) {
                ArrayList<TLRPC$MessageEntity> arrayList = tLRPC$Message2.entities;
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
        }
        MessageObject messageObject3 = this.message;
        if (messageObject3.messageOwner.media != null && !messageObject3.isMediaEmpty()) {
            this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
            MessageObject messageObject4 = this.message;
            TLRPC$MessageMedia tLRPC$MessageMedia = messageObject4.messageOwner.media;
            String str5 = "chats_actionMessage";
            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia;
                charSequence2 = Build.VERSION.SDK_INT >= 18 ? String.format("📊 \u2068%s\u2069", tLRPC$TL_messageMediaPoll.poll.question) : String.format("📊 %s", tLRPC$TL_messageMediaPoll.poll.question);
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                charSequence2 = Build.VERSION.SDK_INT >= 18 ? String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title) : String.format("🎮 %s", tLRPC$MessageMedia.game.title);
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) {
                charSequence2 = tLRPC$MessageMedia.title;
            } else if (messageObject4.type == 14) {
                charSequence2 = Build.VERSION.SDK_INT >= 18 ? String.format("🎧 \u2068%s - %s\u2069", messageObject4.getMusicAuthor(), this.message.getMusicTitle()) : String.format("🎧 %s - %s", messageObject4.getMusicAuthor(), this.message.getMusicTitle());
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
        }
        MessageObject messageObject5 = this.message;
        CharSequence charSequence7 = messageObject5.messageOwner.message;
        if (charSequence7 != null) {
            if (messageObject5.hasHighlightedWords()) {
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
            MessageObject messageObject6 = this.message;
            if (messageObject6 != null && (tLRPC$Message = messageObject6.messageOwner) != null) {
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

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        CanvasButton canvasButton;
        DialogCellDelegate dialogCellDelegate = this.delegate;
        if ((dialogCellDelegate == null || dialogCellDelegate.canClickButtonInside()) && (canvasButton = this.canvasButton) != null && this.buttonLayout != null && canvasButton.checkTouchEvent(motionEvent)) {
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
}
