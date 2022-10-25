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
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import androidx.core.content.ContextCompat;
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
import org.telegram.tgnet.TLRPC$TL_photoEmpty;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DialogsAdapter;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EmptyStubSpan;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
import org.telegram.ui.Components.Forum.ForumBubbleDrawable;
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
    private boolean animatingArchiveAvatar;
    private float animatingArchiveAvatarProgress;
    private float archiveBackgroundProgress;
    private boolean archiveHidden;
    private PullForegroundDrawable archivedChatsDrawable;
    private AvatarDrawable avatarDrawable;
    public ImageReceiver avatarImage;
    private int bottomClip;
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
    private float currentRevealBounceProgress;
    private float currentRevealProgress;
    private CustomDialog customDialog;
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
    public TLRPC$TL_forumTopic forumTopic;
    public boolean fullSeparator;
    public boolean fullSeparator2;
    private ArrayList<MessageObject> groupMessages;
    private int halfCheckDrawLeft;
    private boolean hasCall;
    private boolean hasUnmutedTopics;
    private boolean hasVideoThumb;
    public int heightDefault;
    public int heightThreeLines;
    private int index;
    private float innerProgress;
    private BounceInterpolator interpolator;
    private boolean isDialogCell;
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
    private RectF rect;
    private float reorderIconProgress;
    private final Theme.ResourcesProvider resourcesProvider;
    private List<SpoilerEffect> spoilers;
    private Stack<SpoilerEffect> spoilersPool;
    private boolean statusDrawableAnimationInProgress;
    private ValueAnimator statusDrawableAnimator;
    private int statusDrawableLeft;
    private float statusDrawableProgress;
    public boolean swipeCanceled;
    private int swipeMessageTextId;
    private StaticLayout swipeMessageTextLayout;
    private int swipeMessageWidth;
    private ImageReceiver[] thumbImage;
    private int thumbsCount;
    private StaticLayout timeLayout;
    private int timeLeft;
    private int timeTop;
    private int topClip;
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
        this.drawCount2 = true;
        this.countChangeProgress = 1.0f;
        this.reactionsMentionsChangeProgress = 1.0f;
        this.rect = new RectF();
        this.lastStatusDrawableParams = -1;
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
        TLRPC$User tLRPC$User = this.user;
        if (tLRPC$User != null && !tLRPC$User.self) {
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
        setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? this.heightThreeLines : this.heightDefault) + (this.useSeparator ? 1 : 0) + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0));
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
        buildLayout();
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

    /* JADX WARN: Code restructure failed: missing block: B:1445:0x0546, code lost:
        if (r2.post_messages == false) goto L640;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1451:0x0552, code lost:
        if (r2.kicked != false) goto L640;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x17cc  */
    /* JADX WARN: Removed duplicated region for block: B:1048:0x0f7b  */
    /* JADX WARN: Removed duplicated region for block: B:1053:0x0f8c  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x17d5 A[Catch: Exception -> 0x1874, TryCatch #5 {Exception -> 0x1874, blocks: (B:90:0x1780, B:93:0x178a, B:95:0x1796, B:98:0x17b0, B:100:0x17b9, B:103:0x17cf, B:105:0x17d5, B:106:0x17e1, B:108:0x17f8, B:110:0x17fe, B:113:0x180f, B:115:0x1813, B:116:0x1851, B:118:0x1855, B:120:0x185e, B:121:0x1868, B:509:0x1834), top: B:89:0x1780 }] */
    /* JADX WARN: Removed duplicated region for block: B:115:0x1813 A[Catch: Exception -> 0x1874, TryCatch #5 {Exception -> 0x1874, blocks: (B:90:0x1780, B:93:0x178a, B:95:0x1796, B:98:0x17b0, B:100:0x17b9, B:103:0x17cf, B:105:0x17d5, B:106:0x17e1, B:108:0x17f8, B:110:0x17fe, B:113:0x180f, B:115:0x1813, B:116:0x1851, B:118:0x1855, B:120:0x185e, B:121:0x1868, B:509:0x1834), top: B:89:0x1780 }] */
    /* JADX WARN: Removed duplicated region for block: B:1438:0x0536  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x1a38  */
    /* JADX WARN: Removed duplicated region for block: B:1448:0x054c  */
    /* JADX WARN: Removed duplicated region for block: B:1452:0x04f9  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x1a47  */
    /* JADX WARN: Removed duplicated region for block: B:1481:0x047f  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x1a6c  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x1c36  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x1d46  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x1dc8 A[Catch: Exception -> 0x1e98, TryCatch #1 {Exception -> 0x1e98, blocks: (B:188:0x1d5f, B:190:0x1d63, B:193:0x1d7b, B:195:0x1d81, B:197:0x1d94, B:199:0x1da7, B:200:0x1dc3, B:202:0x1dc8, B:204:0x1ddc, B:206:0x1de2, B:208:0x1de6, B:211:0x1df3, B:213:0x1df0, B:216:0x1df6, B:218:0x1dfa, B:221:0x1dff, B:223:0x1e03, B:225:0x1e15, B:226:0x1e27, B:227:0x1e72, B:367:0x1e3f, B:370:0x1e45, B:371:0x1e4c, B:374:0x1e62, B:378:0x1d67, B:380:0x1d6b, B:382:0x1d70), top: B:187:0x1d5f }] */
    /* JADX WARN: Removed duplicated region for block: B:223:0x1e03 A[Catch: Exception -> 0x1e98, TryCatch #1 {Exception -> 0x1e98, blocks: (B:188:0x1d5f, B:190:0x1d63, B:193:0x1d7b, B:195:0x1d81, B:197:0x1d94, B:199:0x1da7, B:200:0x1dc3, B:202:0x1dc8, B:204:0x1ddc, B:206:0x1de2, B:208:0x1de6, B:211:0x1df3, B:213:0x1df0, B:216:0x1df6, B:218:0x1dfa, B:221:0x1dff, B:223:0x1e03, B:225:0x1e15, B:226:0x1e27, B:227:0x1e72, B:367:0x1e3f, B:370:0x1e45, B:371:0x1e4c, B:374:0x1e62, B:378:0x1d67, B:380:0x1d6b, B:382:0x1d70), top: B:187:0x1d5f }] */
    /* JADX WARN: Removed duplicated region for block: B:230:0x1ea3  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x20d5  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x20e4 A[Catch: Exception -> 0x2123, TryCatch #6 {Exception -> 0x2123, blocks: (B:293:0x20d9, B:295:0x20e4, B:296:0x20e6, B:298:0x20ff, B:300:0x2107, B:302:0x210b), top: B:292:0x20d9 }] */
    /* JADX WARN: Removed duplicated region for block: B:298:0x20ff A[Catch: Exception -> 0x2123, TryCatch #6 {Exception -> 0x2123, blocks: (B:293:0x20d9, B:295:0x20e4, B:296:0x20e6, B:298:0x20ff, B:300:0x2107, B:302:0x210b), top: B:292:0x20d9 }] */
    /* JADX WARN: Removed duplicated region for block: B:302:0x210b A[Catch: Exception -> 0x2123, TRY_LEAVE, TryCatch #6 {Exception -> 0x2123, blocks: (B:293:0x20d9, B:295:0x20e4, B:296:0x20e6, B:298:0x20ff, B:300:0x2107, B:302:0x210b), top: B:292:0x20d9 }] */
    /* JADX WARN: Removed duplicated region for block: B:310:0x212b  */
    /* JADX WARN: Removed duplicated region for block: B:325:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:326:0x2023  */
    /* JADX WARN: Removed duplicated region for block: B:373:0x1e5d  */
    /* JADX WARN: Removed duplicated region for block: B:375:0x1e60  */
    /* JADX WARN: Removed duplicated region for block: B:418:0x1cef A[LOOP:8: B:416:0x1cea->B:418:0x1cef, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:419:0x1d42 A[EDGE_INSN: B:419:0x1d42->B:183:0x1d42 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:425:0x1a9a  */
    /* JADX WARN: Removed duplicated region for block: B:499:0x19b7  */
    /* JADX WARN: Removed duplicated region for block: B:503:0x1a0c A[LOOP:9: B:501:0x1a07->B:503:0x1a0c, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:504:0x1a30 A[EDGE_INSN: B:504:0x1a30->B:505:0x1a30 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:506:0x19d5  */
    /* JADX WARN: Removed duplicated region for block: B:509:0x1834 A[Catch: Exception -> 0x1874, TryCatch #5 {Exception -> 0x1874, blocks: (B:90:0x1780, B:93:0x178a, B:95:0x1796, B:98:0x17b0, B:100:0x17b9, B:103:0x17cf, B:105:0x17d5, B:106:0x17e1, B:108:0x17f8, B:110:0x17fe, B:113:0x180f, B:115:0x1813, B:116:0x1851, B:118:0x1855, B:120:0x185e, B:121:0x1868, B:509:0x1834), top: B:89:0x1780 }] */
    /* JADX WARN: Removed duplicated region for block: B:511:0x17ce  */
    /* JADX WARN: Removed duplicated region for block: B:543:0x171c  */
    /* JADX WARN: Removed duplicated region for block: B:545:0x1652  */
    /* JADX WARN: Removed duplicated region for block: B:557:0x15ea  */
    /* JADX WARN: Removed duplicated region for block: B:559:0x15d2  */
    /* JADX WARN: Removed duplicated region for block: B:561:0x1591  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x1552  */
    /* JADX WARN: Removed duplicated region for block: B:624:0x04cd  */
    /* JADX WARN: Removed duplicated region for block: B:629:0x04d9  */
    /* JADX WARN: Removed duplicated region for block: B:636:0x0513  */
    /* JADX WARN: Removed duplicated region for block: B:643:0x0559  */
    /* JADX WARN: Removed duplicated region for block: B:654:0x1322  */
    /* JADX WARN: Removed duplicated region for block: B:657:0x1347  */
    /* JADX WARN: Removed duplicated region for block: B:660:0x1482  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x159e  */
    /* JADX WARN: Removed duplicated region for block: B:675:0x14d7  */
    /* JADX WARN: Removed duplicated region for block: B:677:0x14ee  */
    /* JADX WARN: Removed duplicated region for block: B:704:0x135b  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x15d7  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x160b  */
    /* JADX WARN: Removed duplicated region for block: B:772:0x132a  */
    /* JADX WARN: Removed duplicated region for block: B:782:0x05de  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x1624  */
    /* JADX WARN: Removed duplicated region for block: B:870:0x082f  */
    /* JADX WARN: Removed duplicated region for block: B:875:0x130f  */
    /* JADX WARN: Removed duplicated region for block: B:877:0x0836  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x1789  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x1796 A[Catch: Exception -> 0x1874, TryCatch #5 {Exception -> 0x1874, blocks: (B:90:0x1780, B:93:0x178a, B:95:0x1796, B:98:0x17b0, B:100:0x17b9, B:103:0x17cf, B:105:0x17d5, B:106:0x17e1, B:108:0x17f8, B:110:0x17fe, B:113:0x180f, B:115:0x1813, B:116:0x1851, B:118:0x1855, B:120:0x185e, B:121:0x1868, B:509:0x1834), top: B:89:0x1780 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void buildLayout() {
        int i;
        String str;
        boolean z;
        String str2;
        boolean z2;
        int i2;
        TLRPC$DraftMessage tLRPC$DraftMessage;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Chat chat;
        TLRPC$User tLRPC$User;
        int i3;
        String str3;
        CharSequence charSequence;
        boolean z3;
        boolean z4;
        char c;
        boolean z5;
        CharSequence charSequence2;
        CharSequence replaceNewLines;
        CharSequence highlightText;
        CharSequence charSequence3;
        CharSequence charSequence4;
        CharSequence charSequence5;
        CharSequence formatPluralString;
        String str4;
        TLRPC$TL_forumTopic findTopic;
        CharSequence charSequence6;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        SpannableStringBuilder valueOf;
        TLRPC$Message tLRPC$Message;
        String charSequence7;
        SpannableStringBuilder formatSpannable;
        CharSequence charSequence8;
        String str5;
        TLRPC$TL_forumTopic findTopic2;
        int i4;
        int i5;
        CharSequence replaceEmoji;
        int i6;
        boolean z6;
        boolean z7;
        CharSequence highlightText2;
        ForegroundColorSpanThemable foregroundColorSpanThemable;
        CharSequence charSequence9;
        CharSequence string;
        CharSequence charSequence10;
        CharSequence charSequence11;
        int i7;
        boolean z8;
        CharSequence charSequence12;
        int i8;
        CharSequence charSequence13;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        ArrayList<TLRPC$MessagePeerReaction> arrayList;
        CharSequence charSequence14;
        CharSequence charSequence15;
        CharSequence charSequence16;
        ArrayList<TLRPC$MessageEntity> arrayList2;
        CharSequence charSequence17;
        CharSequence charSequence18;
        CharSequence charSequence19;
        TLRPC$DraftMessage tLRPC$DraftMessage2;
        MessageObject messageObject;
        String stringForMessageListDate;
        MessageObject messageObject2;
        boolean z9;
        String str6;
        String str7;
        boolean z10;
        String str8;
        String str9;
        String userName;
        boolean z11;
        CharSequence charSequence20;
        boolean z12;
        int i9;
        String str10;
        String str11;
        String str12;
        MessageObject messageObject3;
        CharSequence charSequence21;
        int i10;
        int i11;
        CharSequence charSequence22;
        int i12;
        int dp;
        int dp2;
        int dp3;
        int i13;
        int i14;
        int i15;
        ImageReceiver[] imageReceiverArr;
        int i16;
        int max;
        boolean z13;
        int dp4;
        int i17;
        ImageReceiver[] imageReceiverArr2;
        MessageObject messageObject4;
        CharSequence highlightText3;
        int lineCount;
        StaticLayout staticLayout;
        StaticLayout staticLayout2;
        float primaryHorizontal;
        float primaryHorizontal2;
        int i18;
        int length;
        int ceil;
        int i19;
        int lineCount2;
        boolean z14;
        int i20;
        CharacterStyle[] characterStyleArr;
        CharSequence replaceTwoNewLinesToOne;
        CharSequence highlightText4;
        int dp5;
        int dp6;
        int dp7;
        CharSequence highlightText5;
        CharSequence charSequence23;
        CharSequence charSequence24;
        SpannableStringBuilder valueOf2;
        boolean z15 = false;
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
            i = 18;
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
            i = 19;
        }
        this.currentDialogFolderDialogsCount = 0;
        CharSequence printingString = this.isDialogCell ? MessagesController.getInstance(this.currentAccount).getPrintingString(this.currentDialogId, 0, true) : null;
        TextPaint textPaint5 = Theme.dialogs_messagePaint[this.paintIndex];
        this.drawNameLock = false;
        this.drawVerified = false;
        this.drawPremium = false;
        this.drawScam = 0;
        this.drawPinBackground = false;
        this.thumbsCount = 0;
        this.hasVideoThumb = false;
        this.nameLayoutEllipsizeByGradient = false;
        boolean z16 = !UserObject.isUserSelf(this.user) && !this.useMeForMyMessages;
        this.printingStringType = -1;
        if (Build.VERSION.SDK_INT >= 18) {
            if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && this.currentDialogFolderId == 0) {
                str = "\u2068%s\u2069";
                z = false;
            } else {
                str = "%2$s: \u2068%1$s\u2069";
                z = true;
            }
        } else if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && this.currentDialogFolderId == 0) {
            str = "%1$s";
            z = false;
        } else {
            str = "%2$s: %1$s";
            z = true;
        }
        MessageObject messageObject5 = this.message;
        CharSequence charSequence25 = messageObject5 != null ? messageObject5.messageText : null;
        boolean z17 = charSequence25 instanceof Spannable;
        CharSequence charSequence26 = charSequence25;
        if (z17) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence25);
            for (URLSpanNoUnderlineBold uRLSpanNoUnderlineBold : (URLSpanNoUnderlineBold[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), URLSpanNoUnderlineBold.class)) {
                spannableStringBuilder.removeSpan(uRLSpanNoUnderlineBold);
            }
            for (URLSpanNoUnderline uRLSpanNoUnderline : (URLSpanNoUnderline[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), URLSpanNoUnderline.class)) {
                spannableStringBuilder.removeSpan(uRLSpanNoUnderline);
            }
            charSequence26 = spannableStringBuilder;
        }
        this.lastMessageString = charSequence26;
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
                this.drawVerified = customDialog.verified;
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
                charSequence24 = LocaleController.getString("FromYou", R.string.FromYou);
                CustomDialog customDialog3 = this.customDialog;
                if (customDialog3.isMedia) {
                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                    valueOf2 = SpannableStringBuilder.valueOf(String.format(str, this.message.messageText));
                    valueOf2.setSpan(new ForegroundColorSpanThemable("chats_attachMessage", this.resourcesProvider), 0, valueOf2.length(), 33);
                } else {
                    String str13 = customDialog3.message;
                    if (str13.length() > 150) {
                        str13 = str13.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                    }
                    valueOf2 = (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? SpannableStringBuilder.valueOf(String.format(str, str13, charSequence24)) : SpannableStringBuilder.valueOf(String.format(str, str13.replace('\n', ' '), charSequence24));
                }
                charSequence23 = Emoji.replaceEmoji(valueOf2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                z11 = false;
            } else {
                charSequence23 = customDialog2.message;
                if (customDialog2.isMedia) {
                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                }
                charSequence24 = null;
                z11 = true;
            }
            str12 = LocaleController.stringForMessageListDate(this.customDialog.date);
            int i21 = this.customDialog.unread_count;
            if (i21 != 0) {
                this.drawCount = true;
                str11 = String.format("%d", Integer.valueOf(i21));
            } else {
                this.drawCount = false;
                str11 = null;
            }
            CustomDialog customDialog4 = this.customDialog;
            int i22 = customDialog4.sent;
            if (i22 == 0) {
                this.drawClock = true;
                this.drawCheck1 = false;
                this.drawCheck2 = false;
            } else if (i22 == 2) {
                this.drawCheck1 = true;
                this.drawCheck2 = true;
                this.drawClock = false;
            } else if (i22 == 1) {
                this.drawCheck1 = false;
                this.drawCheck2 = true;
                this.drawClock = false;
            } else {
                this.drawClock = false;
                this.drawCheck1 = false;
                this.drawCheck2 = false;
            }
            this.drawError = false;
            str9 = customDialog4.name;
            charSequence21 = charSequence23;
            i3 = i;
            str10 = null;
            i9 = 0;
            i7 = -1;
            charSequence20 = charSequence24;
            z12 = true;
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
                        this.drawVerified = tLRPC$Chat2.verified;
                    }
                } else {
                    TLRPC$User tLRPC$User2 = this.user;
                    if (tLRPC$User2 != null) {
                        if (tLRPC$User2.scam) {
                            this.drawScam = 1;
                            Theme.dialogs_scamDrawable.checkText();
                        } else if (tLRPC$User2.fake) {
                            this.drawScam = 2;
                            Theme.dialogs_fakeDrawable.checkText();
                        } else {
                            this.drawVerified = tLRPC$User2.verified;
                        }
                        if (MessagesController.getInstance(this.currentAccount).isPremiumUser(this.user)) {
                            str2 = str;
                            long j = UserConfig.getInstance(this.currentAccount).clientUserId;
                            long j2 = this.user.id;
                            if (j != j2 && j2 != 0) {
                                z2 = true;
                                this.drawPremium = z2;
                                if (z2) {
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
                                i2 = this.lastMessageDate;
                                if (i2 == 0 && (messageObject3 = this.message) != null) {
                                    i2 = messageObject3.messageOwner.date;
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
                                if (tLRPC$DraftMessage != null || ((!TextUtils.isEmpty(tLRPC$DraftMessage.message) || this.draftMessage.reply_to_msg_id != 0) && (i2 <= this.draftMessage.date || this.unreadCount == 0))) {
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
                                    if (printingString == null) {
                                        this.lastPrintString = printingString;
                                        int intValue = MessagesController.getInstance(this.currentAccount).getPrintingStringType(this.currentDialogId, 0).intValue();
                                        this.printingStringType = intValue;
                                        StatusDrawable chatStatusDrawable = Theme.getChatStatusDrawable(intValue);
                                        int intrinsicWidth = chatStatusDrawable != null ? chatStatusDrawable.getIntrinsicWidth() + AndroidUtilities.dp(3.0f) : 0;
                                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
                                        CharSequence replace = TextUtils.replace(printingString, new String[]{"..."}, new String[]{""});
                                        i7 = this.printingStringType == 5 ? replace.toString().indexOf("**oo**") : -1;
                                        if (i7 >= 0) {
                                            spannableStringBuilder2.append(replace).setSpan(new FixedWidthSpan(Theme.getChatStatusDrawable(this.printingStringType).getIntrinsicWidth()), i7, i7 + 6, 0);
                                        } else {
                                            spannableStringBuilder2.append((CharSequence) " ").append(replace).setSpan(new FixedWidthSpan(intrinsicWidth), 0, 1, 0);
                                        }
                                        textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                        i3 = i;
                                        charSequence12 = null;
                                        z8 = true;
                                        i8 = 0;
                                        charSequence19 = spannableStringBuilder2;
                                    } else {
                                        this.lastPrintString = null;
                                        if (this.draftMessage != null) {
                                            charSequence12 = LocaleController.getString("Draft", R.string.Draft);
                                            if (TextUtils.isEmpty(this.draftMessage.message)) {
                                                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                                    i3 = i;
                                                    charSequence17 = "";
                                                    z8 = true;
                                                    i8 = 0;
                                                    charSequence16 = charSequence17;
                                                } else {
                                                    SpannableStringBuilder valueOf3 = SpannableStringBuilder.valueOf(charSequence12);
                                                    valueOf3.setSpan(new ForegroundColorSpanThemable("chats_draft", this.resourcesProvider), 0, charSequence12.length(), 33);
                                                    charSequence18 = valueOf3;
                                                }
                                            } else {
                                                String str14 = this.draftMessage.message;
                                                if (str14.length() > 150) {
                                                    str14 = str14.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                }
                                                SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(str14);
                                                MediaDataController.addTextStyleRuns(this.draftMessage, spannableStringBuilder3, 256);
                                                TLRPC$DraftMessage tLRPC$DraftMessage3 = this.draftMessage;
                                                if (tLRPC$DraftMessage3 != null && (arrayList2 = tLRPC$DraftMessage3.entities) != null) {
                                                    MediaDataController.addAnimatedEmojiSpans(arrayList2, spannableStringBuilder3, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                }
                                                SpannableStringBuilder formatSpannable2 = AndroidUtilities.formatSpannable(str2, AndroidUtilities.replaceNewLines(spannableStringBuilder3), charSequence12);
                                                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                                    formatSpannable2.setSpan(new ForegroundColorSpanThemable("chats_draft", this.resourcesProvider), 0, charSequence12.length() + 1, 33);
                                                }
                                                charSequence18 = Emoji.replaceEmoji(formatSpannable2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                            }
                                            i3 = i;
                                            charSequence17 = charSequence18;
                                            z8 = true;
                                            i8 = 0;
                                            charSequence16 = charSequence17;
                                        } else {
                                            String str15 = str2;
                                            if (this.clearingDialog) {
                                                textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                charSequence15 = LocaleController.getString("HistoryCleared", R.string.HistoryCleared);
                                            } else {
                                                MessageObject messageObject6 = this.message;
                                                if (messageObject6 == null) {
                                                    TLRPC$EncryptedChat tLRPC$EncryptedChat = this.encryptedChat;
                                                    if (tLRPC$EncryptedChat != null) {
                                                        textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                        if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatRequested) {
                                                            charSequence15 = LocaleController.getString("EncryptionProcessing", R.string.EncryptionProcessing);
                                                        } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatWaiting) {
                                                            charSequence15 = LocaleController.formatString("AwaitingEncryption", R.string.AwaitingEncryption, UserObject.getFirstName(this.user));
                                                        } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatDiscarded) {
                                                            charSequence15 = LocaleController.getString("EncryptionRejected", R.string.EncryptionRejected);
                                                        } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat) {
                                                            if (tLRPC$EncryptedChat.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                                charSequence15 = LocaleController.formatString("EncryptedChatStartedOutgoing", R.string.EncryptedChatStartedOutgoing, UserObject.getFirstName(this.user));
                                                            } else {
                                                                charSequence15 = LocaleController.getString("EncryptedChatStartedIncoming", R.string.EncryptedChatStartedIncoming);
                                                            }
                                                        }
                                                    } else if (this.dialogsType == 3 && UserObject.isUserSelf(this.user)) {
                                                        i3 = i;
                                                        charSequence12 = null;
                                                        z8 = false;
                                                        i8 = 0;
                                                        z15 = true;
                                                        z16 = false;
                                                        charSequence16 = LocaleController.getString("SavedMessagesInfo", R.string.SavedMessagesInfo);
                                                    }
                                                    i3 = i;
                                                    charSequence14 = "";
                                                    charSequence12 = null;
                                                    z8 = true;
                                                    i8 = 0;
                                                    z15 = true;
                                                    charSequence16 = charSequence14;
                                                } else {
                                                    CharSequence restrictionReason = MessagesController.getRestrictionReason(messageObject6.messageOwner.restriction_reason);
                                                    long fromChatId = this.message.getFromChatId();
                                                    if (DialogObject.isUserDialog(fromChatId)) {
                                                        tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(fromChatId));
                                                        chat = null;
                                                    } else {
                                                        chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-fromChatId));
                                                        tLRPC$User = null;
                                                    }
                                                    this.drawCount2 = true;
                                                    TLRPC$User tLRPC$User3 = tLRPC$User;
                                                    if (this.dialogsType == 0 && this.currentDialogId > 0 && this.message.isOutOwner() && (tLRPC$TL_messageReactions = this.message.messageOwner.reactions) != null && (arrayList = tLRPC$TL_messageReactions.recent_reactions) != null && !arrayList.isEmpty() && this.reactionMentionCount > 0) {
                                                        TLRPC$MessagePeerReaction tLRPC$MessagePeerReaction = this.message.messageOwner.reactions.recent_reactions.get(0);
                                                        if (tLRPC$MessagePeerReaction.unread) {
                                                            str3 = str15;
                                                            long j3 = tLRPC$MessagePeerReaction.peer_id.user_id;
                                                            if (j3 != 0) {
                                                                i3 = i;
                                                                if (j3 != UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                                    ReactionsLayoutInBubble.VisibleReaction fromTLReaction = ReactionsLayoutInBubble.VisibleReaction.fromTLReaction(tLRPC$MessagePeerReaction.reaction);
                                                                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                    String str16 = fromTLReaction.emojicon;
                                                                    if (str16 != null) {
                                                                        charSequence = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, str16);
                                                                    } else {
                                                                        String formatString = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, "**reaction**");
                                                                        int indexOf = formatString.indexOf("**reaction**");
                                                                        SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder(formatString.replace("**reaction**", "d"));
                                                                        spannableStringBuilder4.setSpan(new AnimatedEmojiSpan(fromTLReaction.documentId, textPaint5 == null ? null : textPaint5.getFontMetricsInt()), indexOf, indexOf + 1, 0);
                                                                        charSequence = spannableStringBuilder4;
                                                                    }
                                                                    z3 = true;
                                                                    if (z3) {
                                                                        charSequence2 = charSequence;
                                                                    } else {
                                                                        int i23 = this.dialogsType;
                                                                        if (i23 == 2) {
                                                                            TLRPC$Chat tLRPC$Chat4 = this.chat;
                                                                            if (tLRPC$Chat4 != null) {
                                                                                if (ChatObject.isChannel(tLRPC$Chat4)) {
                                                                                    TLRPC$Chat tLRPC$Chat5 = this.chat;
                                                                                    if (!tLRPC$Chat5.megagroup) {
                                                                                        int i24 = tLRPC$Chat5.participants_count;
                                                                                        if (i24 != 0) {
                                                                                            string = LocaleController.formatPluralStringComma("Subscribers", i24);
                                                                                        } else if (!ChatObject.isPublic(tLRPC$Chat5)) {
                                                                                            string = LocaleController.getString("ChannelPrivate", R.string.ChannelPrivate).toLowerCase();
                                                                                        } else {
                                                                                            string = LocaleController.getString("ChannelPublic", R.string.ChannelPublic).toLowerCase();
                                                                                        }
                                                                                    }
                                                                                }
                                                                                TLRPC$Chat tLRPC$Chat6 = this.chat;
                                                                                int i25 = tLRPC$Chat6.participants_count;
                                                                                if (i25 != 0) {
                                                                                    string = LocaleController.formatPluralStringComma("Members", i25);
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
                                                                        } else if (i23 == 3 && UserObject.isUserSelf(this.user)) {
                                                                            string = LocaleController.getString("SavedMessagesInfo", R.string.SavedMessagesInfo);
                                                                        } else {
                                                                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout && this.currentDialogFolderId != 0) {
                                                                                replaceNewLines = formatArchivedDialogNames();
                                                                            } else {
                                                                                MessageObject messageObject7 = this.message;
                                                                                if ((messageObject7.messageOwner instanceof TLRPC$TL_messageService) && !MessageObject.isTopicActionMessage(messageObject7)) {
                                                                                    if (ChatObject.isChannelAndNotMegaGroup(this.chat) && (this.message.messageOwner.action instanceof TLRPC$TL_messageActionChannelMigrateFrom)) {
                                                                                        charSequence26 = "";
                                                                                        z16 = false;
                                                                                    }
                                                                                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                    charSequence2 = charSequence26;
                                                                                } else {
                                                                                    if (this.groupMessages != null && TextUtils.isEmpty(restrictionReason) && this.currentDialogFolderId == 0 && this.encryptedChat == null) {
                                                                                        this.thumbsCount = 0;
                                                                                        this.hasVideoThumb = false;
                                                                                        Collections.sort(this.groupMessages, DialogCell$$ExternalSyntheticLambda3.INSTANCE);
                                                                                        z4 = true;
                                                                                        for (int i26 = 0; i26 < this.groupMessages.size(); i26++) {
                                                                                            MessageObject messageObject8 = this.groupMessages.get(i26);
                                                                                            if (messageObject8 != null && !messageObject8.needDrawBluredPreview() && (messageObject8.isPhoto() || messageObject8.isNewGif() || messageObject8.isVideo() || messageObject8.isRoundVideo())) {
                                                                                                String str17 = messageObject8.isWebpage() ? messageObject8.messageOwner.media.webpage.type : null;
                                                                                                if (!"app".equals(str17) && !"profile".equals(str17) && !"article".equals(str17) && (str17 == null || !str17.startsWith("telegram_"))) {
                                                                                                    TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject8.photoThumbs, 40);
                                                                                                    TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageObject8.photoThumbs, AndroidUtilities.getPhotoSize());
                                                                                                    if (closestPhotoSizeWithSize == closestPhotoSizeWithSize2) {
                                                                                                        closestPhotoSizeWithSize2 = null;
                                                                                                    }
                                                                                                    if (closestPhotoSizeWithSize != null) {
                                                                                                        this.hasVideoThumb = this.hasVideoThumb || messageObject8.isVideo() || messageObject8.isRoundVideo();
                                                                                                        int i27 = this.thumbsCount;
                                                                                                        if (i27 < 2) {
                                                                                                            this.thumbsCount = i27 + 1;
                                                                                                            this.drawPlay[i26] = messageObject8.isVideo() || messageObject8.isRoundVideo();
                                                                                                            this.thumbImage[i26].setImage(ImageLocation.getForObject(closestPhotoSizeWithSize2, messageObject8.photoThumbsObject), "20_20", ImageLocation.getForObject(closestPhotoSizeWithSize, messageObject8.photoThumbsObject), "20_20", (messageObject8.type != 1 || closestPhotoSizeWithSize2 == null) ? 0 : closestPhotoSizeWithSize2.size, null, messageObject8, 0);
                                                                                                            this.thumbImage[i26].setRoundRadius(messageObject8.isRoundVideo() ? AndroidUtilities.dp(18.0f) : AndroidUtilities.dp(2.0f));
                                                                                                            z4 = false;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        MessageObject messageObject9 = this.message;
                                                                                        if (messageObject9 != null && this.currentDialogFolderId == 0) {
                                                                                            this.thumbsCount = 0;
                                                                                            this.hasVideoThumb = false;
                                                                                            if (!messageObject9.needDrawBluredPreview() && (this.message.isPhoto() || this.message.isNewGif() || this.message.isVideo() || this.message.isRoundVideo())) {
                                                                                                String str18 = this.message.isWebpage() ? this.message.messageOwner.media.webpage.type : null;
                                                                                                if (!"app".equals(str18) && !"profile".equals(str18) && !"article".equals(str18) && (str18 == null || !str18.startsWith("telegram_"))) {
                                                                                                    TLRPC$PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, 40);
                                                                                                    TLRPC$PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, AndroidUtilities.getPhotoSize());
                                                                                                    if (closestPhotoSizeWithSize3 == closestPhotoSizeWithSize4) {
                                                                                                        closestPhotoSizeWithSize4 = null;
                                                                                                    }
                                                                                                    if (closestPhotoSizeWithSize3 != null) {
                                                                                                        this.hasVideoThumb = this.hasVideoThumb || this.message.isVideo() || this.message.isRoundVideo();
                                                                                                        int i28 = this.thumbsCount;
                                                                                                        if (i28 < 3) {
                                                                                                            this.thumbsCount = i28 + 1;
                                                                                                            boolean[] zArr = this.drawPlay;
                                                                                                            if (this.message.isVideo() || this.message.isRoundVideo()) {
                                                                                                                c = 0;
                                                                                                                z5 = true;
                                                                                                            } else {
                                                                                                                c = 0;
                                                                                                                z5 = false;
                                                                                                            }
                                                                                                            zArr[c] = z5;
                                                                                                            MessageObject messageObject10 = this.message;
                                                                                                            this.thumbImage[c].setImage(ImageLocation.getForObject(closestPhotoSizeWithSize4, messageObject10.photoThumbsObject), "20_20", ImageLocation.getForObject(closestPhotoSizeWithSize3, this.message.photoThumbsObject), "20_20", (messageObject10.type != 1 || closestPhotoSizeWithSize4 == null) ? 0 : closestPhotoSizeWithSize4.size, null, this.message, 0);
                                                                                                            this.thumbImage[0].setRoundRadius(this.message.isRoundVideo() ? AndroidUtilities.dp(18.0f) : AndroidUtilities.dp(2.0f));
                                                                                                            z4 = false;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        z4 = true;
                                                                                    }
                                                                                    TLRPC$Chat tLRPC$Chat7 = this.chat;
                                                                                    if (tLRPC$Chat7 != null && tLRPC$Chat7.id > 0 && chat == null && (!ChatObject.isChannel(tLRPC$Chat7) || ChatObject.isMegagroup(this.chat))) {
                                                                                        if (this.message.isOutOwner()) {
                                                                                            charSequence6 = LocaleController.getString("FromYou", R.string.FromYou);
                                                                                        } else {
                                                                                            MessageObject messageObject11 = this.message;
                                                                                            if (messageObject11 == null || (tLRPC$MessageFwdHeader = messageObject11.messageOwner.fwd_from) == null || (charSequence6 = tLRPC$MessageFwdHeader.from_name) == null) {
                                                                                                if (tLRPC$User3 == null) {
                                                                                                    charSequence6 = "DELETED";
                                                                                                } else if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                                                                                    if (UserObject.isDeleted(tLRPC$User3)) {
                                                                                                        charSequence6 = LocaleController.getString("HiddenName", R.string.HiddenName);
                                                                                                    } else {
                                                                                                        charSequence6 = ContactsController.formatName(tLRPC$User3.first_name, tLRPC$User3.last_name).replace("\n", "");
                                                                                                    }
                                                                                                } else {
                                                                                                    charSequence6 = UserObject.getFirstName(tLRPC$User3).replace("\n", "");
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        CharSequence charSequence27 = charSequence6;
                                                                                        if (this.chat.forum) {
                                                                                            charSequence27 = charSequence6;
                                                                                            if (!this.isTopic) {
                                                                                                CharSequence topicIconName = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopicIconName(this.chat, this.message, textPaint5);
                                                                                                charSequence27 = charSequence6;
                                                                                                if (!TextUtils.isEmpty(topicIconName)) {
                                                                                                    SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder("-");
                                                                                                    ColoredImageSpan coloredImageSpan = new ColoredImageSpan(ContextCompat.getDrawable(ApplicationLoader.applicationContext, R.drawable.msg_mini_forumarrow).mutate());
                                                                                                    coloredImageSpan.setColorKey((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? null : "chats_nameMessage");
                                                                                                    spannableStringBuilder5.setSpan(coloredImageSpan, 0, 1, 0);
                                                                                                    SpannableStringBuilder spannableStringBuilder6 = new SpannableStringBuilder();
                                                                                                    spannableStringBuilder6.append(charSequence6).append((CharSequence) spannableStringBuilder5).append(topicIconName);
                                                                                                    charSequence27 = spannableStringBuilder6;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        MessageObject captionMessage = getCaptionMessage();
                                                                                        if (!TextUtils.isEmpty(restrictionReason)) {
                                                                                            valueOf = SpannableStringBuilder.valueOf(String.format(str3, restrictionReason, charSequence27));
                                                                                        } else {
                                                                                            String str19 = str3;
                                                                                            if (MessageObject.isTopicActionMessage(this.message)) {
                                                                                                MessageObject messageObject12 = this.message;
                                                                                                CharSequence charSequence28 = messageObject12.messageTextShort;
                                                                                                if (charSequence28 == null || ((messageObject12.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate) && this.isTopic)) {
                                                                                                    charSequence28 = messageObject12.messageText;
                                                                                                }
                                                                                                valueOf = AndroidUtilities.formatSpannable(str19, charSequence28, charSequence27);
                                                                                                if (this.message.topicIconDrawable[0] != null && (findTopic2 = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-this.message.getDialogId(), MessageObject.getTopicId(this.message.messageOwner))) != null) {
                                                                                                    this.message.topicIconDrawable[0].setColor(findTopic2.icon_color);
                                                                                                }
                                                                                            } else if (captionMessage != null && (charSequence8 = captionMessage.caption) != null) {
                                                                                                CharSequence charSequence29 = charSequence8.toString();
                                                                                                if (!z4) {
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
                                                                                                    String str20 = captionMessage.messageTrimmedToHighlight;
                                                                                                    int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) + 24);
                                                                                                    if (z) {
                                                                                                        if (!TextUtils.isEmpty(charSequence27)) {
                                                                                                            measuredWidth = (int) (measuredWidth - textPaint5.measureText(charSequence27.toString()));
                                                                                                        }
                                                                                                        measuredWidth = (int) (measuredWidth - textPaint5.measureText(": "));
                                                                                                    }
                                                                                                    if (measuredWidth > 0) {
                                                                                                        str20 = AndroidUtilities.ellipsizeCenterEnd(str20, captionMessage.highlightedWords.get(0), measuredWidth, textPaint5, 130).toString();
                                                                                                    }
                                                                                                    valueOf = new SpannableStringBuilder(str5).append((CharSequence) str20);
                                                                                                } else {
                                                                                                    if (charSequence29.length() > 150) {
                                                                                                        charSequence29 = charSequence29.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                                    }
                                                                                                    SpannableStringBuilder spannableStringBuilder7 = new SpannableStringBuilder(charSequence29);
                                                                                                    MediaDataController.addTextStyleRuns(captionMessage.messageOwner.entities, charSequence29, spannableStringBuilder7, 256);
                                                                                                    TLRPC$Message tLRPC$Message2 = captionMessage.messageOwner;
                                                                                                    if (tLRPC$Message2 != null) {
                                                                                                        MediaDataController.addAnimatedEmojiSpans(tLRPC$Message2.entities, spannableStringBuilder7, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                                    }
                                                                                                    valueOf = AndroidUtilities.formatSpannable(str19, new SpannableStringBuilder(str5).append(AndroidUtilities.replaceNewLines(spannableStringBuilder7)), charSequence27);
                                                                                                }
                                                                                            } else {
                                                                                                MessageObject messageObject13 = this.message;
                                                                                                if (messageObject13.messageOwner.media != null && !messageObject13.isMediaEmpty()) {
                                                                                                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                    MessageObject messageObject14 = this.message;
                                                                                                    TLRPC$MessageMedia tLRPC$MessageMedia = messageObject14.messageOwner.media;
                                                                                                    String str21 = "chats_attachMessage";
                                                                                                    if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                                                                                                        TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia;
                                                                                                        charSequence7 = Build.VERSION.SDK_INT >= 18 ? String.format("📊 \u2068%s\u2069", tLRPC$TL_messageMediaPoll.poll.question) : String.format("📊 %s", tLRPC$TL_messageMediaPoll.poll.question);
                                                                                                    } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                                                                                                        charSequence7 = Build.VERSION.SDK_INT >= 18 ? String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title) : String.format("🎮 %s", tLRPC$MessageMedia.game.title);
                                                                                                    } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                                        charSequence7 = tLRPC$MessageMedia.title;
                                                                                                    } else if (messageObject14.type == 14) {
                                                                                                        charSequence7 = Build.VERSION.SDK_INT >= 18 ? String.format("🎧 \u2068%s - %s\u2069", messageObject14.getMusicAuthor(), this.message.getMusicTitle()) : String.format("🎧 %s - %s", messageObject14.getMusicAuthor(), this.message.getMusicTitle());
                                                                                                    } else if (this.thumbsCount > 1) {
                                                                                                        if (this.hasVideoThumb) {
                                                                                                            ArrayList<MessageObject> arrayList3 = this.groupMessages;
                                                                                                            charSequence7 = LocaleController.formatPluralString("Media", arrayList3 == null ? 0 : arrayList3.size(), new Object[0]);
                                                                                                        } else {
                                                                                                            ArrayList<MessageObject> arrayList4 = this.groupMessages;
                                                                                                            charSequence7 = LocaleController.formatPluralString("Photos", arrayList4 == null ? 0 : arrayList4.size(), new Object[0]);
                                                                                                        }
                                                                                                        str21 = "chats_actionMessage";
                                                                                                    } else {
                                                                                                        charSequence7 = charSequence26.toString();
                                                                                                        str21 = "chats_actionMessage";
                                                                                                    }
                                                                                                    formatSpannable = AndroidUtilities.formatSpannable(str19, charSequence7.replace('\n', ' '), charSequence27);
                                                                                                    try {
                                                                                                        formatSpannable.setSpan(new ForegroundColorSpanThemable(str21, this.resourcesProvider), z ? charSequence27.length() + 2 : 0, formatSpannable.length(), 33);
                                                                                                    } catch (Exception e) {
                                                                                                        FileLog.e(e);
                                                                                                    }
                                                                                                    if ((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && (this.currentDialogFolderId == 0 || formatSpannable.length() <= 0)) {
                                                                                                        i4 = 0;
                                                                                                        i5 = 0;
                                                                                                    } else {
                                                                                                        try {
                                                                                                            foregroundColorSpanThemable = new ForegroundColorSpanThemable("chats_nameMessage", this.resourcesProvider);
                                                                                                            i5 = charSequence27.length() + 1;
                                                                                                        } catch (Exception e2) {
                                                                                                            e = e2;
                                                                                                            i5 = 0;
                                                                                                        }
                                                                                                        try {
                                                                                                            formatSpannable.setSpan(foregroundColorSpanThemable, 0, i5, 33);
                                                                                                            i4 = i5;
                                                                                                        } catch (Exception e3) {
                                                                                                            e = e3;
                                                                                                            FileLog.e(e);
                                                                                                            i4 = 0;
                                                                                                            replaceEmoji = Emoji.replaceEmoji(formatSpannable, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                            if (this.message.hasHighlightedWords()) {
                                                                                                            }
                                                                                                            CharSequence charSequence30 = replaceEmoji;
                                                                                                            if (this.thumbsCount > 0) {
                                                                                                            }
                                                                                                            i6 = i4;
                                                                                                            z6 = true;
                                                                                                            z7 = false;
                                                                                                            charSequence11 = charSequence27;
                                                                                                            charSequence10 = charSequence30;
                                                                                                            if (this.currentDialogFolderId != 0) {
                                                                                                            }
                                                                                                            i7 = -1;
                                                                                                            CharSequence charSequence31 = charSequence11;
                                                                                                            z8 = z6;
                                                                                                            charSequence12 = charSequence31;
                                                                                                            int i29 = i6;
                                                                                                            z15 = z7;
                                                                                                            i8 = i29;
                                                                                                            charSequence19 = charSequence10;
                                                                                                            if (this.draftMessage != null) {
                                                                                                            }
                                                                                                            messageObject2 = this.message;
                                                                                                            if (messageObject2 == null) {
                                                                                                            }
                                                                                                            this.promoDialog = z10;
                                                                                                            MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                                                                                                            CharSequence charSequence32 = charSequence19;
                                                                                                            if (this.dialogsType == 0) {
                                                                                                            }
                                                                                                            if (this.currentDialogFolderId != 0) {
                                                                                                            }
                                                                                                            z11 = z15;
                                                                                                            boolean z18 = z8;
                                                                                                            charSequence20 = charSequence12;
                                                                                                            z12 = z18;
                                                                                                            String str22 = stringForMessageListDate;
                                                                                                            i9 = i8;
                                                                                                            str10 = str7;
                                                                                                            str11 = str6;
                                                                                                            str12 = str22;
                                                                                                            charSequence21 = charSequence32;
                                                                                                            if (!z12) {
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
                                                                                                            CharSequence replace2 = str9.replace('\n', ' ');
                                                                                                            if (this.nameLayoutEllipsizeByGradient) {
                                                                                                            }
                                                                                                            float f = dp7;
                                                                                                            this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(replace2.toString()) <= f;
                                                                                                            if (!this.twoLinesForName) {
                                                                                                            }
                                                                                                            CharSequence replaceEmoji2 = Emoji.replaceEmoji(replace2, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                            MessageObject messageObject15 = this.message;
                                                                                                            if (messageObject15 != null) {
                                                                                                            }
                                                                                                            if (!this.twoLinesForName) {
                                                                                                            }
                                                                                                            this.nameLayoutTranslateX = (this.nameLayoutEllipsizeByGradient || !this.nameLayout.isRtlCharAt(0)) ? 0.0f : -AndroidUtilities.dp(36.0f);
                                                                                                            this.nameLayoutEllipsizeLeft = this.nameLayout.isRtlCharAt(0);
                                                                                                            if (!this.useForceThreeLines) {
                                                                                                            }
                                                                                                            charSequence22 = "";
                                                                                                            i12 = i3;
                                                                                                            dp = AndroidUtilities.dp(11.0f);
                                                                                                            this.messageNameTop = AndroidUtilities.dp(32.0f);
                                                                                                            this.timeTop = AndroidUtilities.dp(13.0f);
                                                                                                            this.errorTop = AndroidUtilities.dp(43.0f);
                                                                                                            this.pinTop = AndroidUtilities.dp(43.0f);
                                                                                                            this.countTop = AndroidUtilities.dp(43.0f);
                                                                                                            this.checkDrawTop = AndroidUtilities.dp(13.0f);
                                                                                                            int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 21);
                                                                                                            if (!LocaleController.isRTL) {
                                                                                                            }
                                                                                                            i13 = measuredWidth2;
                                                                                                            i14 = i7;
                                                                                                            this.avatarImage.setImageCoords(dp2, dp, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                                                                                                            i15 = 0;
                                                                                                            while (true) {
                                                                                                                imageReceiverArr = this.thumbImage;
                                                                                                                if (i15 < imageReceiverArr.length) {
                                                                                                                }
                                                                                                                imageReceiverArr[i15].setImageCoords(((i12 + 2) * i15) + dp3, AndroidUtilities.dp(31.0f) + dp, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                                                                                                                i15++;
                                                                                                                dp = dp;
                                                                                                            }
                                                                                                            i16 = dp;
                                                                                                            int i30 = i13;
                                                                                                            if (this.twoLinesForName) {
                                                                                                            }
                                                                                                            if (getIsPinned()) {
                                                                                                            }
                                                                                                            if (!this.drawError) {
                                                                                                            }
                                                                                                            if (z11) {
                                                                                                            }
                                                                                                            max = Math.max(AndroidUtilities.dp(12.0f), i30);
                                                                                                            z13 = this.useForceThreeLines;
                                                                                                            if (!z13) {
                                                                                                            }
                                                                                                            try {
                                                                                                                messageObject4 = this.message;
                                                                                                                if (messageObject4 != null) {
                                                                                                                    charSequence20 = highlightText3;
                                                                                                                }
                                                                                                                this.messageNameLayout = StaticLayoutEx.createStaticLayout(charSequence20, Theme.dialogs_messageNamePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max, 1);
                                                                                                            } catch (Exception e4) {
                                                                                                                FileLog.e(e4);
                                                                                                            }
                                                                                                            this.messageTop = AndroidUtilities.dp(51.0f);
                                                                                                            if (this.nameIsEllipsized) {
                                                                                                            }
                                                                                                            i17 = 0;
                                                                                                            while (true) {
                                                                                                                imageReceiverArr2 = this.thumbImage;
                                                                                                                if (i17 < imageReceiverArr2.length) {
                                                                                                                }
                                                                                                                imageReceiverArr2[i17].setImageY(i16 + dp4 + AndroidUtilities.dp(40.0f));
                                                                                                                i17++;
                                                                                                            }
                                                                                                            if (this.twoLinesForName) {
                                                                                                            }
                                                                                                            this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack2, this.messageNameLayout);
                                                                                                            z14 = this.useForceThreeLines;
                                                                                                            if (!z14) {
                                                                                                            }
                                                                                                            textPaint5 = Theme.dialogs_messagePaint[this.paintIndex];
                                                                                                            charSequence21 = charSequence20;
                                                                                                            charSequence20 = null;
                                                                                                            if (charSequence21 instanceof Spannable) {
                                                                                                            }
                                                                                                            if (!this.useForceThreeLines) {
                                                                                                                if (this.thumbsCount > 0) {
                                                                                                                }
                                                                                                                this.messageLayout = new StaticLayout(charSequence21, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                                                                                                                this.spoilersPool.addAll(this.spoilers);
                                                                                                                this.spoilers.clear();
                                                                                                                SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
                                                                                                                this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                                                                                                                if (!LocaleController.isRTL) {
                                                                                                                }
                                                                                                                staticLayout = this.messageLayout;
                                                                                                                if (staticLayout != null) {
                                                                                                                }
                                                                                                                staticLayout2 = this.messageLayout;
                                                                                                                if (staticLayout2 != null) {
                                                                                                                }
                                                                                                            }
                                                                                                            if (this.thumbsCount > 0) {
                                                                                                                max += AndroidUtilities.dp(5.0f);
                                                                                                            }
                                                                                                            this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence21, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence20 == null ? 1 : 2);
                                                                                                            this.spoilersPool.addAll(this.spoilers);
                                                                                                            this.spoilers.clear();
                                                                                                            SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
                                                                                                            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                                                                                                            if (!LocaleController.isRTL) {
                                                                                                            }
                                                                                                            staticLayout = this.messageLayout;
                                                                                                            if (staticLayout != null) {
                                                                                                            }
                                                                                                            staticLayout2 = this.messageLayout;
                                                                                                            if (staticLayout2 != null) {
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                    replaceEmoji = Emoji.replaceEmoji(formatSpannable, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                    if (this.message.hasHighlightedWords() && (highlightText2 = AndroidUtilities.highlightText(replaceEmoji, this.message.highlightedWords, this.resourcesProvider)) != null) {
                                                                                                        replaceEmoji = highlightText2;
                                                                                                    }
                                                                                                    CharSequence charSequence302 = replaceEmoji;
                                                                                                    if (this.thumbsCount > 0) {
                                                                                                        boolean z19 = replaceEmoji instanceof SpannableStringBuilder;
                                                                                                        CharSequence charSequence33 = replaceEmoji;
                                                                                                        if (!z19) {
                                                                                                            charSequence33 = new SpannableStringBuilder(replaceEmoji);
                                                                                                        }
                                                                                                        SpannableStringBuilder spannableStringBuilder8 = (SpannableStringBuilder) charSequence33;
                                                                                                        if (i5 >= spannableStringBuilder8.length()) {
                                                                                                            spannableStringBuilder8.append((CharSequence) " ");
                                                                                                            spannableStringBuilder8.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbsCount * (i3 + 2)) - 2) + 5)), spannableStringBuilder8.length() - 1, spannableStringBuilder8.length(), 33);
                                                                                                            charSequence302 = charSequence33;
                                                                                                        } else {
                                                                                                            spannableStringBuilder8.insert(i5, (CharSequence) " ");
                                                                                                            spannableStringBuilder8.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbsCount * (i3 + 2)) - 2) + 5)), i5, i5 + 1, 33);
                                                                                                            charSequence302 = charSequence33;
                                                                                                        }
                                                                                                    }
                                                                                                    i6 = i4;
                                                                                                    z6 = true;
                                                                                                    z7 = false;
                                                                                                    charSequence11 = charSequence27;
                                                                                                    charSequence10 = charSequence302;
                                                                                                    if (this.currentDialogFolderId != 0) {
                                                                                                    }
                                                                                                    i7 = -1;
                                                                                                    CharSequence charSequence312 = charSequence11;
                                                                                                    z8 = z6;
                                                                                                    charSequence12 = charSequence312;
                                                                                                    int i292 = i6;
                                                                                                    z15 = z7;
                                                                                                    i8 = i292;
                                                                                                    charSequence19 = charSequence10;
                                                                                                } else {
                                                                                                    MessageObject messageObject16 = this.message;
                                                                                                    CharSequence charSequence34 = messageObject16.messageOwner.message;
                                                                                                    if (charSequence34 != null) {
                                                                                                        if (messageObject16.hasHighlightedWords()) {
                                                                                                            String str23 = this.message.messageTrimmedToHighlight;
                                                                                                            if (str23 != null) {
                                                                                                                charSequence34 = str23;
                                                                                                            }
                                                                                                            int measuredWidth3 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) + 10);
                                                                                                            if (z) {
                                                                                                                if (!TextUtils.isEmpty(charSequence27)) {
                                                                                                                    measuredWidth3 = (int) (measuredWidth3 - textPaint5.measureText(charSequence27.toString()));
                                                                                                                }
                                                                                                                measuredWidth3 = (int) (measuredWidth3 - textPaint5.measureText(": "));
                                                                                                            }
                                                                                                            if (measuredWidth3 > 0) {
                                                                                                                charSequence34 = AndroidUtilities.ellipsizeCenterEnd(charSequence34, this.message.highlightedWords.get(0), measuredWidth3, textPaint5, 130).toString();
                                                                                                            }
                                                                                                        } else {
                                                                                                            if (charSequence34.length() > 150) {
                                                                                                                charSequence34 = charSequence34.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                                            }
                                                                                                            charSequence34 = AndroidUtilities.replaceNewLines(charSequence34);
                                                                                                        }
                                                                                                        SpannableStringBuilder spannableStringBuilder9 = new SpannableStringBuilder(charSequence34);
                                                                                                        MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder9, 256);
                                                                                                        MessageObject messageObject17 = this.message;
                                                                                                        if (messageObject17 != null && (tLRPC$Message = messageObject17.messageOwner) != null) {
                                                                                                            MediaDataController.addAnimatedEmojiSpans(tLRPC$Message.entities, spannableStringBuilder9, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                                        }
                                                                                                        valueOf = AndroidUtilities.formatSpannable(str19, spannableStringBuilder9, charSequence27);
                                                                                                    } else {
                                                                                                        valueOf = SpannableStringBuilder.valueOf("");
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        formatSpannable = valueOf;
                                                                                        if (!this.useForceThreeLines) {
                                                                                        }
                                                                                        i4 = 0;
                                                                                        i5 = 0;
                                                                                        replaceEmoji = Emoji.replaceEmoji(formatSpannable, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                        if (this.message.hasHighlightedWords()) {
                                                                                            replaceEmoji = highlightText2;
                                                                                        }
                                                                                        CharSequence charSequence3022 = replaceEmoji;
                                                                                        if (this.thumbsCount > 0) {
                                                                                        }
                                                                                        i6 = i4;
                                                                                        z6 = true;
                                                                                        z7 = false;
                                                                                        charSequence11 = charSequence27;
                                                                                        charSequence10 = charSequence3022;
                                                                                        if (this.currentDialogFolderId != 0) {
                                                                                        }
                                                                                        i7 = -1;
                                                                                        CharSequence charSequence3122 = charSequence11;
                                                                                        z8 = z6;
                                                                                        charSequence12 = charSequence3122;
                                                                                        int i2922 = i6;
                                                                                        z15 = z7;
                                                                                        i8 = i2922;
                                                                                        charSequence19 = charSequence10;
                                                                                    } else {
                                                                                        boolean isEmpty = TextUtils.isEmpty(restrictionReason);
                                                                                        CharSequence charSequence35 = restrictionReason;
                                                                                        if (isEmpty) {
                                                                                            if (MessageObject.isTopicActionMessage(this.message)) {
                                                                                                MessageObject messageObject18 = this.message;
                                                                                                CharSequence charSequence36 = messageObject18.messageTextShort;
                                                                                                if (charSequence36 == null || ((messageObject18.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate) && this.isTopic)) {
                                                                                                    charSequence36 = messageObject18.messageText;
                                                                                                }
                                                                                                if (messageObject18.topicIconDrawable[0] != null && (findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-this.message.getDialogId(), MessageObject.getTopicId(this.message.messageOwner))) != null) {
                                                                                                    this.message.topicIconDrawable[0].setColor(findTopic.icon_color);
                                                                                                }
                                                                                                charSequence35 = charSequence36;
                                                                                            } else {
                                                                                                TLRPC$MessageMedia tLRPC$MessageMedia2 = this.message.messageOwner.media;
                                                                                                if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia2.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                                                                                                    charSequence35 = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
                                                                                                } else if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument) && (tLRPC$MessageMedia2.document instanceof TLRPC$TL_documentEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                                                                                                    charSequence35 = LocaleController.getString("AttachVideoExpired", R.string.AttachVideoExpired);
                                                                                                } else if (getCaptionMessage() != null) {
                                                                                                    MessageObject captionMessage2 = getCaptionMessage();
                                                                                                    if (!z4) {
                                                                                                        str4 = "";
                                                                                                    } else if (captionMessage2.isVideo()) {
                                                                                                        str4 = "📹 ";
                                                                                                    } else if (captionMessage2.isVoice()) {
                                                                                                        str4 = "🎤 ";
                                                                                                    } else if (captionMessage2.isMusic()) {
                                                                                                        str4 = "🎧 ";
                                                                                                    } else {
                                                                                                        str4 = captionMessage2.isPhoto() ? "🖼 " : "📎 ";
                                                                                                    }
                                                                                                    if (captionMessage2.hasHighlightedWords() && !TextUtils.isEmpty(captionMessage2.messageOwner.message)) {
                                                                                                        String str24 = captionMessage2.messageTrimmedToHighlight;
                                                                                                        int measuredWidth4 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) + 24);
                                                                                                        if (z) {
                                                                                                            if (!TextUtils.isEmpty(null)) {
                                                                                                                throw null;
                                                                                                            }
                                                                                                            measuredWidth4 = (int) (measuredWidth4 - textPaint5.measureText(": "));
                                                                                                        }
                                                                                                        if (measuredWidth4 > 0) {
                                                                                                            str24 = AndroidUtilities.ellipsizeCenterEnd(str24, captionMessage2.highlightedWords.get(0), measuredWidth4, textPaint5, 130).toString();
                                                                                                        }
                                                                                                        charSequence35 = str4 + str24;
                                                                                                    } else {
                                                                                                        SpannableStringBuilder spannableStringBuilder10 = new SpannableStringBuilder(captionMessage2.caption);
                                                                                                        TLRPC$Message tLRPC$Message3 = captionMessage2.messageOwner;
                                                                                                        if (tLRPC$Message3 != null) {
                                                                                                            MediaDataController.addTextStyleRuns(tLRPC$Message3.entities, captionMessage2.caption, spannableStringBuilder10, 256);
                                                                                                            MediaDataController.addAnimatedEmojiSpans(captionMessage2.messageOwner.entities, spannableStringBuilder10, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                                        }
                                                                                                        charSequence35 = new SpannableStringBuilder(str4).append((CharSequence) spannableStringBuilder10);
                                                                                                    }
                                                                                                } else if (this.thumbsCount > 1) {
                                                                                                    if (this.hasVideoThumb) {
                                                                                                        ArrayList<MessageObject> arrayList5 = this.groupMessages;
                                                                                                        formatPluralString = LocaleController.formatPluralString("Media", arrayList5 == null ? 0 : arrayList5.size(), new Object[0]);
                                                                                                    } else {
                                                                                                        ArrayList<MessageObject> arrayList6 = this.groupMessages;
                                                                                                        formatPluralString = LocaleController.formatPluralString("Photos", arrayList6 == null ? 0 : arrayList6.size(), new Object[0]);
                                                                                                    }
                                                                                                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                    charSequence35 = formatPluralString;
                                                                                                } else {
                                                                                                    MessageObject messageObject19 = this.message;
                                                                                                    TLRPC$MessageMedia tLRPC$MessageMedia3 = messageObject19.messageOwner.media;
                                                                                                    if (tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaPoll) {
                                                                                                        charSequence5 = "📊 " + ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia3).poll.question;
                                                                                                    } else if (tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaGame) {
                                                                                                        charSequence5 = "🎮 " + this.message.messageOwner.media.game.title;
                                                                                                    } else if (tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                                        charSequence5 = tLRPC$MessageMedia3.title;
                                                                                                    } else if (messageObject19.type == 14) {
                                                                                                        charSequence5 = String.format("🎧 %s - %s", messageObject19.getMusicAuthor(), this.message.getMusicTitle());
                                                                                                    } else {
                                                                                                        if (messageObject19.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                            charSequence4 = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 23), textPaint5, 130).toString();
                                                                                                        } else {
                                                                                                            SpannableStringBuilder spannableStringBuilder11 = new SpannableStringBuilder(charSequence26);
                                                                                                            MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder11, 256);
                                                                                                            MessageObject messageObject20 = this.message;
                                                                                                            charSequence4 = spannableStringBuilder11;
                                                                                                            if (messageObject20 != null) {
                                                                                                                TLRPC$Message tLRPC$Message4 = messageObject20.messageOwner;
                                                                                                                charSequence4 = spannableStringBuilder11;
                                                                                                                if (tLRPC$Message4 != null) {
                                                                                                                    MediaDataController.addAnimatedEmojiSpans(tLRPC$Message4.entities, spannableStringBuilder11, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                                                    charSequence4 = spannableStringBuilder11;
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                        AndroidUtilities.highlightText(charSequence4, this.message.highlightedWords, this.resourcesProvider);
                                                                                                        charSequence5 = charSequence4;
                                                                                                    }
                                                                                                    MessageObject messageObject21 = this.message;
                                                                                                    charSequence35 = charSequence5;
                                                                                                    if (messageObject21.messageOwner.media != null) {
                                                                                                        charSequence35 = charSequence5;
                                                                                                        if (!messageObject21.isMediaEmpty()) {
                                                                                                            textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                            charSequence35 = charSequence5;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if (this.thumbsCount > 0) {
                                                                                            if (this.message.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                replaceNewLines = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp((((this.messagePaddingStart + 23) + ((i3 + 2) * this.thumbsCount)) - 2) + 5), textPaint5, 130).toString();
                                                                                            } else {
                                                                                                if (charSequence35.length() > 150) {
                                                                                                    charSequence35 = charSequence35.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                                }
                                                                                                replaceNewLines = AndroidUtilities.replaceNewLines(charSequence35);
                                                                                            }
                                                                                            if (!(replaceNewLines instanceof SpannableStringBuilder)) {
                                                                                                replaceNewLines = new SpannableStringBuilder(replaceNewLines);
                                                                                            }
                                                                                            SpannableStringBuilder spannableStringBuilder12 = (SpannableStringBuilder) replaceNewLines;
                                                                                            spannableStringBuilder12.insert(0, (CharSequence) " ");
                                                                                            spannableStringBuilder12.setSpan(new FixedWidthSpan(AndroidUtilities.dp((((i3 + 2) * this.thumbsCount) - 2) + 5)), 0, 1, 33);
                                                                                            Emoji.replaceEmoji(spannableStringBuilder12, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
                                                                                            if (this.message.hasHighlightedWords() && (highlightText = AndroidUtilities.highlightText(spannableStringBuilder12, this.message.highlightedWords, this.resourcesProvider)) != null) {
                                                                                                charSequence3 = highlightText;
                                                                                                z6 = true;
                                                                                                charSequence9 = null;
                                                                                                z7 = false;
                                                                                                charSequence13 = charSequence3;
                                                                                                i6 = 0;
                                                                                                charSequence11 = charSequence9;
                                                                                                charSequence10 = charSequence13;
                                                                                                if (this.currentDialogFolderId != 0) {
                                                                                                    charSequence11 = formatArchivedDialogNames();
                                                                                                }
                                                                                                i7 = -1;
                                                                                                CharSequence charSequence31222 = charSequence11;
                                                                                                z8 = z6;
                                                                                                charSequence12 = charSequence31222;
                                                                                                int i29222 = i6;
                                                                                                z15 = z7;
                                                                                                i8 = i29222;
                                                                                                charSequence19 = charSequence10;
                                                                                            }
                                                                                        } else {
                                                                                            charSequence2 = charSequence35;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            charSequence3 = replaceNewLines;
                                                                            z6 = true;
                                                                            charSequence9 = null;
                                                                            z7 = false;
                                                                            charSequence13 = charSequence3;
                                                                            i6 = 0;
                                                                            charSequence11 = charSequence9;
                                                                            charSequence10 = charSequence13;
                                                                            if (this.currentDialogFolderId != 0) {
                                                                            }
                                                                            i7 = -1;
                                                                            CharSequence charSequence312222 = charSequence11;
                                                                            z8 = z6;
                                                                            charSequence12 = charSequence312222;
                                                                            int i292222 = i6;
                                                                            z15 = z7;
                                                                            i8 = i292222;
                                                                            charSequence19 = charSequence10;
                                                                        }
                                                                        charSequence10 = string;
                                                                        z6 = false;
                                                                        charSequence11 = null;
                                                                        z7 = true;
                                                                        i6 = 0;
                                                                        z16 = false;
                                                                        if (this.currentDialogFolderId != 0) {
                                                                        }
                                                                        i7 = -1;
                                                                        CharSequence charSequence3122222 = charSequence11;
                                                                        z8 = z6;
                                                                        charSequence12 = charSequence3122222;
                                                                        int i2922222 = i6;
                                                                        z15 = z7;
                                                                        i8 = i2922222;
                                                                        charSequence19 = charSequence10;
                                                                    }
                                                                    z6 = true;
                                                                    charSequence9 = null;
                                                                    z7 = true;
                                                                    charSequence13 = charSequence2;
                                                                    i6 = 0;
                                                                    charSequence11 = charSequence9;
                                                                    charSequence10 = charSequence13;
                                                                    if (this.currentDialogFolderId != 0) {
                                                                    }
                                                                    i7 = -1;
                                                                    CharSequence charSequence31222222 = charSequence11;
                                                                    z8 = z6;
                                                                    charSequence12 = charSequence31222222;
                                                                    int i29222222 = i6;
                                                                    z15 = z7;
                                                                    i8 = i29222222;
                                                                    charSequence19 = charSequence10;
                                                                }
                                                            } else {
                                                                i3 = i;
                                                            }
                                                            charSequence = "";
                                                            z3 = false;
                                                            if (z3) {
                                                            }
                                                            z6 = true;
                                                            charSequence9 = null;
                                                            z7 = true;
                                                            charSequence13 = charSequence2;
                                                            i6 = 0;
                                                            charSequence11 = charSequence9;
                                                            charSequence10 = charSequence13;
                                                            if (this.currentDialogFolderId != 0) {
                                                            }
                                                            i7 = -1;
                                                            CharSequence charSequence312222222 = charSequence11;
                                                            z8 = z6;
                                                            charSequence12 = charSequence312222222;
                                                            int i292222222 = i6;
                                                            z15 = z7;
                                                            i8 = i292222222;
                                                            charSequence19 = charSequence10;
                                                        }
                                                    }
                                                    i3 = i;
                                                    str3 = str15;
                                                    charSequence = "";
                                                    z3 = false;
                                                    if (z3) {
                                                    }
                                                    z6 = true;
                                                    charSequence9 = null;
                                                    z7 = true;
                                                    charSequence13 = charSequence2;
                                                    i6 = 0;
                                                    charSequence11 = charSequence9;
                                                    charSequence10 = charSequence13;
                                                    if (this.currentDialogFolderId != 0) {
                                                    }
                                                    i7 = -1;
                                                    CharSequence charSequence3122222222 = charSequence11;
                                                    z8 = z6;
                                                    charSequence12 = charSequence3122222222;
                                                    int i2922222222 = i6;
                                                    z15 = z7;
                                                    i8 = i2922222222;
                                                    charSequence19 = charSequence10;
                                                }
                                            }
                                            i3 = i;
                                            charSequence14 = charSequence15;
                                            charSequence12 = null;
                                            z8 = true;
                                            i8 = 0;
                                            z15 = true;
                                            charSequence16 = charSequence14;
                                        }
                                        i7 = -1;
                                        charSequence19 = charSequence16;
                                    }
                                    if (this.draftMessage != null) {
                                        stringForMessageListDate = LocaleController.stringForMessageListDate(tLRPC$DraftMessage2.date);
                                    } else {
                                        int i31 = this.lastMessageDate;
                                        if (i31 != 0) {
                                            stringForMessageListDate = LocaleController.stringForMessageListDate(i31);
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
                                        z10 = false;
                                        str6 = null;
                                        str7 = null;
                                    } else {
                                        if (this.currentDialogFolderId != 0) {
                                            int i32 = this.unreadCount;
                                            int i33 = this.mentionCount;
                                            if (i32 + i33 <= 0) {
                                                this.drawCount = false;
                                                this.drawMention = false;
                                                str6 = null;
                                            } else if (i32 > i33) {
                                                this.drawCount = true;
                                                this.drawMention = false;
                                                str6 = String.format("%d", Integer.valueOf(i32 + i33));
                                            } else {
                                                this.drawCount = false;
                                                this.drawMention = true;
                                                str8 = String.format("%d", Integer.valueOf(i32 + i33));
                                                str6 = null;
                                                this.drawReactionMention = false;
                                                str7 = str8;
                                            }
                                            str8 = null;
                                            this.drawReactionMention = false;
                                            str7 = str8;
                                        } else {
                                            if (this.clearingDialog) {
                                                this.drawCount = false;
                                                z16 = false;
                                                str6 = null;
                                                z9 = false;
                                            } else {
                                                int i34 = this.unreadCount;
                                                if (i34 != 0 && (i34 != 1 || i34 != this.mentionCount || messageObject2 == null || !messageObject2.messageOwner.mentioned)) {
                                                    this.drawCount = true;
                                                    z9 = false;
                                                    str6 = String.format("%d", Integer.valueOf(i34));
                                                } else {
                                                    z9 = false;
                                                    if (this.markUnread) {
                                                        this.drawCount = true;
                                                        str6 = "";
                                                    } else {
                                                        this.drawCount = false;
                                                        str6 = null;
                                                    }
                                                }
                                            }
                                            if (this.mentionCount != 0) {
                                                this.drawMention = true;
                                                str7 = "@";
                                            } else {
                                                this.drawMention = z9;
                                                str7 = null;
                                            }
                                            if (this.reactionMentionCount > 0) {
                                                this.drawReactionMention = true;
                                            } else {
                                                this.drawReactionMention = z9;
                                            }
                                        }
                                        if (this.message.isOut() && this.draftMessage == null && z16) {
                                            MessageObject messageObject22 = this.message;
                                            if (!(messageObject22.messageOwner.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                                                if (messageObject22.isSending()) {
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
                                    MessagesController messagesController2 = MessagesController.getInstance(this.currentAccount);
                                    CharSequence charSequence322 = charSequence19;
                                    if (this.dialogsType == 0) {
                                        charSequence322 = charSequence19;
                                        if (messagesController2.isPromoDialog(this.currentDialogId, true)) {
                                            this.drawPinBackground = true;
                                            this.promoDialog = true;
                                            int i35 = messagesController2.promoDialogType;
                                            if (i35 == MessagesController.PROMO_TYPE_PROXY) {
                                                stringForMessageListDate = LocaleController.getString("UseProxySponsor", R.string.UseProxySponsor);
                                                charSequence322 = charSequence19;
                                            } else {
                                                charSequence322 = charSequence19;
                                                if (i35 == MessagesController.PROMO_TYPE_PSA) {
                                                    stringForMessageListDate = LocaleController.getString("PsaType_" + messagesController2.promoPsaType);
                                                    if (TextUtils.isEmpty(stringForMessageListDate)) {
                                                        stringForMessageListDate = LocaleController.getString("PsaTypeDefault", R.string.PsaTypeDefault);
                                                    }
                                                    charSequence322 = charSequence19;
                                                    if (!TextUtils.isEmpty(messagesController2.promoPsaMessage)) {
                                                        CharSequence charSequence37 = messagesController2.promoPsaMessage;
                                                        this.thumbsCount = 0;
                                                        charSequence322 = charSequence37;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (this.currentDialogFolderId != 0) {
                                        str9 = LocaleController.getString("ArchivedChats", R.string.ArchivedChats);
                                    } else {
                                        TLRPC$Chat tLRPC$Chat8 = this.chat;
                                        if (tLRPC$Chat8 != null) {
                                            if (this.isTopic) {
                                                userName = this.forumTopic.title;
                                            } else {
                                                userName = tLRPC$Chat8.title;
                                            }
                                        } else {
                                            TLRPC$User tLRPC$User4 = this.user;
                                            if (tLRPC$User4 != null) {
                                                if (UserObject.isReplyUser(tLRPC$User4)) {
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
                                            } else {
                                                str9 = "";
                                                if (str9 != null && str9.length() == 0) {
                                                    str9 = LocaleController.getString("HiddenName", R.string.HiddenName);
                                                }
                                            }
                                        }
                                        str9 = userName;
                                        if (str9 != null) {
                                            str9 = LocaleController.getString("HiddenName", R.string.HiddenName);
                                        }
                                    }
                                    z11 = z15;
                                    boolean z182 = z8;
                                    charSequence20 = charSequence12;
                                    z12 = z182;
                                    String str222 = stringForMessageListDate;
                                    i9 = i8;
                                    str10 = str7;
                                    str11 = str6;
                                    str12 = str222;
                                    charSequence21 = charSequence322;
                                }
                                this.draftMessage = null;
                                if (printingString == null) {
                                }
                                if (this.draftMessage != null) {
                                }
                                messageObject2 = this.message;
                                if (messageObject2 == null) {
                                }
                                this.promoDialog = z10;
                                MessagesController messagesController22 = MessagesController.getInstance(this.currentAccount);
                                CharSequence charSequence3222 = charSequence19;
                                if (this.dialogsType == 0) {
                                }
                                if (this.currentDialogFolderId != 0) {
                                }
                                z11 = z15;
                                boolean z1822 = z8;
                                charSequence20 = charSequence12;
                                z12 = z1822;
                                String str2222 = stringForMessageListDate;
                                i9 = i8;
                                str10 = str7;
                                str11 = str6;
                                str12 = str2222;
                                charSequence21 = charSequence3222;
                            }
                        } else {
                            str2 = str;
                        }
                        z2 = false;
                        this.drawPremium = z2;
                        if (z2) {
                        }
                        i2 = this.lastMessageDate;
                        if (i2 == 0) {
                            i2 = messageObject3.messageOwner.date;
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
                        if (printingString == null) {
                        }
                        if (this.draftMessage != null) {
                        }
                        messageObject2 = this.message;
                        if (messageObject2 == null) {
                        }
                        this.promoDialog = z10;
                        MessagesController messagesController222 = MessagesController.getInstance(this.currentAccount);
                        CharSequence charSequence32222 = charSequence19;
                        if (this.dialogsType == 0) {
                        }
                        if (this.currentDialogFolderId != 0) {
                        }
                        z11 = z15;
                        boolean z18222 = z8;
                        charSequence20 = charSequence12;
                        z12 = z18222;
                        String str22222 = stringForMessageListDate;
                        i9 = i8;
                        str10 = str7;
                        str11 = str6;
                        str12 = str22222;
                        charSequence21 = charSequence32222;
                    }
                }
            }
            str2 = str;
            i2 = this.lastMessageDate;
            if (i2 == 0) {
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
            if (printingString == null) {
            }
            if (this.draftMessage != null) {
            }
            messageObject2 = this.message;
            if (messageObject2 == null) {
            }
            this.promoDialog = z10;
            MessagesController messagesController2222 = MessagesController.getInstance(this.currentAccount);
            CharSequence charSequence322222 = charSequence19;
            if (this.dialogsType == 0) {
            }
            if (this.currentDialogFolderId != 0) {
            }
            z11 = z15;
            boolean z182222 = z8;
            charSequence20 = charSequence12;
            z12 = z182222;
            String str222222 = stringForMessageListDate;
            i9 = i8;
            str10 = str7;
            str11 = str6;
            str12 = str222222;
            charSequence21 = charSequence322222;
        }
        if (!z12) {
            i10 = (int) Math.ceil(Theme.dialogs_timePaint.measureText(str12));
            this.timeLayout = new StaticLayout(str12, Theme.dialogs_timePaint, i10, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (!LocaleController.isRTL) {
                this.timeLeft = (getMeasuredWidth() - AndroidUtilities.dp(15.0f)) - i10;
            } else {
                this.timeLeft = AndroidUtilities.dp(15.0f);
            }
        } else {
            this.timeLayout = null;
            this.timeLeft = 0;
            i10 = 0;
        }
        if (!drawLock2()) {
            if (LocaleController.isRTL) {
                this.lock2Left = this.timeLeft + i10 + AndroidUtilities.dp(4.0f);
            } else {
                this.lock2Left = (this.timeLeft - Theme.dialogs_lock2Drawable.getIntrinsicWidth()) - AndroidUtilities.dp(4.0f);
            }
            i11 = Theme.dialogs_lock2Drawable.getIntrinsicWidth() + AndroidUtilities.dp(4.0f) + 0;
            i10 += i11;
        } else {
            i11 = 0;
        }
        if (LocaleController.isRTL) {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(22.0f)) - i10;
        } else {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp((this.messagePaddingStart + 5) + 8)) - i10;
            this.nameLeft += i10;
        }
        if (this.drawNameLock) {
            this.nameWidth -= AndroidUtilities.dp(4.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
        }
        if (!this.drawClock) {
            int intrinsicWidth2 = Theme.dialogs_clockDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            this.nameWidth -= intrinsicWidth2;
            if (!LocaleController.isRTL) {
                this.clockDrawLeft = (this.timeLeft - i11) - intrinsicWidth2;
            } else {
                this.clockDrawLeft = this.timeLeft + i10 + AndroidUtilities.dp(5.0f);
                this.nameLeft += intrinsicWidth2;
            }
        } else if (this.drawCheck2) {
            int intrinsicWidth3 = Theme.dialogs_checkDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            int i36 = this.nameWidth - intrinsicWidth3;
            this.nameWidth = i36;
            if (this.drawCheck1) {
                this.nameWidth = i36 - (Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0f));
                if (!LocaleController.isRTL) {
                    int i37 = (this.timeLeft - i11) - intrinsicWidth3;
                    this.halfCheckDrawLeft = i37;
                    this.checkDrawLeft = i37 - AndroidUtilities.dp(5.5f);
                } else {
                    int dp9 = this.timeLeft + i10 + AndroidUtilities.dp(5.0f);
                    this.checkDrawLeft = dp9;
                    this.halfCheckDrawLeft = dp9 + AndroidUtilities.dp(5.5f);
                    this.nameLeft += (intrinsicWidth3 + Theme.dialogs_halfCheckDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(8.0f);
                }
            } else if (!LocaleController.isRTL) {
                this.checkDrawLeft1 = (this.timeLeft - i11) - intrinsicWidth3;
            } else {
                this.checkDrawLeft1 = this.timeLeft + i10 + AndroidUtilities.dp(5.0f);
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
            CharSequence replace22 = str9.replace('\n', ' ');
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
            MessageObject messageObject152 = this.message;
            CharSequence charSequence38 = (messageObject152 != null || !messageObject152.hasHighlightedWords() || (highlightText5 = AndroidUtilities.highlightText(replaceEmoji22, this.message.highlightedWords, this.resourcesProvider)) == null) ? replaceEmoji22 : highlightText5;
            if (!this.twoLinesForName) {
                this.nameLayout = StaticLayoutEx.createStaticLayout(charSequence38, Theme.dialogs_namePaint[this.paintIndex], dp7, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, dp7, 2);
            } else {
                this.nameLayout = new StaticLayout(charSequence38, Theme.dialogs_namePaint[this.paintIndex], Math.max(dp7, this.nameWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
            this.nameLayoutTranslateX = (this.nameLayoutEllipsizeByGradient || !this.nameLayout.isRtlCharAt(0)) ? 0.0f : -AndroidUtilities.dp(36.0f);
            this.nameLayoutEllipsizeLeft = this.nameLayout.isRtlCharAt(0);
        } catch (Exception e5) {
            FileLog.e(e5);
        }
        if (!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
            charSequence22 = "";
            i12 = i3;
            dp = AndroidUtilities.dp(11.0f);
            this.messageNameTop = AndroidUtilities.dp(32.0f);
            this.timeTop = AndroidUtilities.dp(13.0f);
            this.errorTop = AndroidUtilities.dp(43.0f);
            this.pinTop = AndroidUtilities.dp(43.0f);
            this.countTop = AndroidUtilities.dp(43.0f);
            this.checkDrawTop = AndroidUtilities.dp(13.0f);
            int measuredWidth22 = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 21);
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
            i13 = measuredWidth22;
            i14 = i7;
            this.avatarImage.setImageCoords(dp2, dp, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            i15 = 0;
            while (true) {
                imageReceiverArr = this.thumbImage;
                if (i15 < imageReceiverArr.length) {
                    break;
                }
                imageReceiverArr[i15].setImageCoords(((i12 + 2) * i15) + dp3, AndroidUtilities.dp(31.0f) + dp, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                i15++;
                dp = dp;
            }
            i16 = dp;
        } else {
            int dp16 = AndroidUtilities.dp(9.0f);
            this.messageNameTop = AndroidUtilities.dp(31.0f);
            this.timeTop = AndroidUtilities.dp(16.0f);
            this.errorTop = AndroidUtilities.dp(39.0f);
            this.pinTop = AndroidUtilities.dp(39.0f);
            this.countTop = AndroidUtilities.dp(this.isTopic ? 36.0f : 39.0f);
            this.checkDrawTop = AndroidUtilities.dp(17.0f);
            int measuredWidth5 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) - (LocaleController.isRTL ? 0 : 12));
            if (LocaleController.isRTL) {
                int dp17 = AndroidUtilities.dp(22.0f);
                this.messageNameLeft = dp17;
                this.messageLeft = dp17;
                dp5 = getMeasuredWidth() - AndroidUtilities.dp(64.0f);
                dp6 = dp5 - AndroidUtilities.dp(((this.thumbsCount * (i3 + 2)) - 2) + 11);
            } else {
                int dp18 = AndroidUtilities.dp(this.messagePaddingStart + 4);
                this.messageNameLeft = dp18;
                this.messageLeft = dp18;
                dp5 = AndroidUtilities.dp(10.0f);
                dp6 = AndroidUtilities.dp(67.0f) + dp5;
            }
            i13 = measuredWidth5;
            charSequence22 = "";
            this.avatarImage.setImageCoords(dp5, dp16, AndroidUtilities.dp(54.0f), AndroidUtilities.dp(54.0f));
            int i38 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr3 = this.thumbImage;
                if (i38 >= imageReceiverArr3.length) {
                    break;
                }
                int i39 = dp16;
                float f3 = i3;
                imageReceiverArr3[i38].setImageCoords(((i3 + 2) * i38) + dp6, AndroidUtilities.dp(30.0f) + dp16, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3));
                i38++;
                dp16 = i39;
                dp6 = dp6;
            }
            i16 = dp16;
            i12 = i3;
            i14 = i7;
        }
        int i302 = i13;
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
            int dp19 = AndroidUtilities.dp(31.0f);
            i302 -= dp19;
            if (!LocaleController.isRTL) {
                this.errorLeft = getMeasuredWidth() - AndroidUtilities.dp(34.0f);
            } else {
                this.errorLeft = AndroidUtilities.dp(11.0f);
                this.messageLeft += dp19;
                this.messageNameLeft += dp19;
            }
        } else if (str11 != null || str10 != null || this.drawReactionMention) {
            if (str11 != null) {
                this.countWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str11)));
                this.countLayout = new StaticLayout(str11, Theme.dialogs_countTextPaint, this.countWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                int dp20 = this.countWidth + AndroidUtilities.dp(18.0f);
                i302 -= dp20;
                if (!LocaleController.isRTL) {
                    this.countLeft = (getMeasuredWidth() - this.countWidth) - AndroidUtilities.dp(20.0f);
                } else {
                    this.countLeft = AndroidUtilities.dp(20.0f);
                    this.messageLeft += dp20;
                    this.messageNameLeft += dp20;
                }
                this.drawCount = true;
            } else {
                this.countWidth = 0;
            }
            if (str10 != null) {
                if (this.currentDialogFolderId != 0) {
                    this.mentionWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str10)));
                    this.mentionLayout = new StaticLayout(str10, Theme.dialogs_countTextPaint, this.mentionWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                } else {
                    this.mentionWidth = AndroidUtilities.dp(12.0f);
                }
                int dp21 = this.mentionWidth + AndroidUtilities.dp(18.0f);
                i302 -= dp21;
                if (!LocaleController.isRTL) {
                    int measuredWidth6 = (getMeasuredWidth() - this.mentionWidth) - AndroidUtilities.dp(20.0f);
                    int i40 = this.countWidth;
                    this.mentionLeft = measuredWidth6 - (i40 != 0 ? i40 + AndroidUtilities.dp(18.0f) : 0);
                } else {
                    int dp22 = AndroidUtilities.dp(20.0f);
                    int i41 = this.countWidth;
                    this.mentionLeft = dp22 + (i41 != 0 ? i41 + AndroidUtilities.dp(18.0f) : 0);
                    this.messageLeft += dp21;
                    this.messageNameLeft += dp21;
                }
                this.drawMention = true;
            } else {
                this.mentionWidth = 0;
            }
            if (this.drawReactionMention) {
                int dp23 = AndroidUtilities.dp(24.0f);
                i302 -= dp23;
                if (!LocaleController.isRTL) {
                    int measuredWidth7 = getMeasuredWidth() - AndroidUtilities.dp(32.0f);
                    this.reactionMentionLeft = measuredWidth7;
                    if (this.drawMention) {
                        int i42 = this.mentionWidth;
                        this.reactionMentionLeft = measuredWidth7 - (i42 != 0 ? i42 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i43 = this.reactionMentionLeft;
                        int i44 = this.countWidth;
                        this.reactionMentionLeft = i43 - (i44 != 0 ? i44 + AndroidUtilities.dp(18.0f) : 0);
                    }
                } else {
                    int dp24 = AndroidUtilities.dp(20.0f);
                    this.reactionMentionLeft = dp24;
                    if (this.drawMention) {
                        int i45 = this.mentionWidth;
                        this.reactionMentionLeft = dp24 + (i45 != 0 ? i45 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i46 = this.reactionMentionLeft;
                        int i47 = this.countWidth;
                        this.reactionMentionLeft = i46 + (i47 != 0 ? i47 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    this.messageLeft += dp23;
                    this.messageNameLeft += dp23;
                }
            }
        } else {
            if (getIsPinned()) {
                int intrinsicWidth4 = Theme.dialogs_pinnedDrawable.getIntrinsicWidth() + AndroidUtilities.dp(8.0f);
                i302 -= intrinsicWidth4;
                if (LocaleController.isRTL) {
                    this.messageLeft += intrinsicWidth4;
                    this.messageNameLeft += intrinsicWidth4;
                }
            }
            this.drawCount = false;
            this.drawMention = false;
        }
        if (z11) {
            CharSequence charSequence39 = charSequence21 == null ? charSequence22 : charSequence21;
            if (charSequence39.length() > 150) {
                charSequence39 = charSequence39.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
            }
            if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || charSequence20 != null) {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceNewLines(charSequence39);
            } else {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceTwoNewLinesToOne(charSequence39);
            }
            charSequence21 = Emoji.replaceEmoji(replaceTwoNewLinesToOne, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
            MessageObject messageObject23 = this.message;
            if (messageObject23 != null && (highlightText4 = AndroidUtilities.highlightText(charSequence21, messageObject23.highlightedWords, this.resourcesProvider)) != null) {
                charSequence21 = highlightText4;
            }
        }
        max = Math.max(AndroidUtilities.dp(12.0f), i302);
        z13 = this.useForceThreeLines;
        if ((!z13 || SharedConfig.useThreeLinesLayout) && charSequence20 != null && (this.currentDialogFolderId == 0 || this.currentDialogFolderDialogsCount == 1)) {
            messageObject4 = this.message;
            if (messageObject4 != null && messageObject4.hasHighlightedWords() && (highlightText3 = AndroidUtilities.highlightText(charSequence20, this.message.highlightedWords, this.resourcesProvider)) != null) {
                charSequence20 = highlightText3;
            }
            this.messageNameLayout = StaticLayoutEx.createStaticLayout(charSequence20, Theme.dialogs_messageNamePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max, 1);
            this.messageTop = AndroidUtilities.dp(51.0f);
            dp4 = (this.nameIsEllipsized || !this.isTopic) ? 0 : AndroidUtilities.dp(20.0f);
            i17 = 0;
            while (true) {
                imageReceiverArr2 = this.thumbImage;
                if (i17 < imageReceiverArr2.length) {
                    break;
                }
                imageReceiverArr2[i17].setImageY(i16 + dp4 + AndroidUtilities.dp(40.0f));
                i17++;
            }
        } else {
            this.messageNameLayout = null;
            if (z13 || SharedConfig.useThreeLinesLayout) {
                this.messageTop = AndroidUtilities.dp(32.0f);
                int dp25 = (!this.nameIsEllipsized || !this.isTopic) ? 0 : AndroidUtilities.dp(20.0f);
                int i48 = 0;
                while (true) {
                    ImageReceiver[] imageReceiverArr4 = this.thumbImage;
                    if (i48 >= imageReceiverArr4.length) {
                        break;
                    }
                    imageReceiverArr4[i48].setImageY(i16 + dp25 + AndroidUtilities.dp(21.0f));
                    i48++;
                }
            } else {
                this.messageTop = AndroidUtilities.dp(39.0f);
            }
        }
        if (this.twoLinesForName) {
            this.messageTop += AndroidUtilities.dp(20.0f);
        }
        this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack2, this.messageNameLayout);
        try {
            z14 = this.useForceThreeLines;
            if ((!z14 || SharedConfig.useThreeLinesLayout) && this.currentDialogFolderId != 0 && this.currentDialogFolderDialogsCount > 1) {
                textPaint5 = Theme.dialogs_messagePaint[this.paintIndex];
                charSequence21 = charSequence20;
                charSequence20 = null;
            } else if ((!z14 && !SharedConfig.useThreeLinesLayout) || charSequence20 != null) {
                CharSequence ellipsize = TextUtils.ellipsize(charSequence21, textPaint5, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END);
                charSequence21 = (!(ellipsize instanceof Spanned) || ((FixedWidthSpan[]) ((Spanned) ellipsize).getSpans(0, ellipsize.length(), FixedWidthSpan.class)).length > 0) ? ellipsize : TextUtils.ellipsize(charSequence21, textPaint5, max - AndroidUtilities.dp((((this.thumbsCount * (i12 + 2)) - 2) + 12) + 5), TextUtils.TruncateAt.END);
            }
            if (charSequence21 instanceof Spannable) {
                Spannable spannable = (Spannable) charSequence21;
                for (CharacterStyle characterStyle : (CharacterStyle[]) spannable.getSpans(0, spannable.length(), CharacterStyle.class)) {
                    if ((characterStyle instanceof ClickableSpan) || ((characterStyle instanceof StyleSpan) && ((StyleSpan) characterStyle).getStyle() == 1)) {
                        spannable.removeSpan(characterStyle);
                    }
                }
            }
        } catch (Exception e6) {
            this.messageLayout = null;
            FileLog.e(e6);
        }
        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
            if (this.thumbsCount > 0) {
                int i49 = i12 + 2;
                max += AndroidUtilities.dp(((i20 * i49) - 2) + 5);
                if (LocaleController.isRTL) {
                    this.messageLeft -= AndroidUtilities.dp(((this.thumbsCount * i49) - 2) + 5);
                }
            }
            this.messageLayout = new StaticLayout(charSequence21, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.spoilersPool.addAll(this.spoilers);
            this.spoilers.clear();
            SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
            if (!LocaleController.isRTL) {
                StaticLayout staticLayout3 = this.nameLayout;
                if (staticLayout3 != null && staticLayout3.getLineCount() > 0) {
                    float lineLeft = this.nameLayout.getLineLeft(0);
                    double ceil2 = Math.ceil(this.nameLayout.getLineWidth(0));
                    int dp26 = this.nameWidth + AndroidUtilities.dp(12.0f);
                    this.nameWidth = dp26;
                    if (this.nameLayoutEllipsizeByGradient) {
                        ceil2 = Math.min(dp26, ceil2);
                    }
                    if ((this.dialogMuted || this.drawUnmute) && !this.drawVerified && this.drawScam == 0) {
                        double d = this.nameLeft;
                        double d2 = this.nameWidth;
                        Double.isNaN(d2);
                        Double.isNaN(d);
                        double d3 = d + (d2 - ceil2);
                        double dp27 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(dp27);
                        double d4 = d3 - dp27;
                        double intrinsicWidth5 = Theme.dialogs_muteDrawable.getIntrinsicWidth();
                        Double.isNaN(intrinsicWidth5);
                        this.nameMuteLeft = (int) (d4 - intrinsicWidth5);
                    } else if (this.drawVerified) {
                        double d5 = this.nameLeft;
                        double d6 = this.nameWidth;
                        Double.isNaN(d6);
                        Double.isNaN(d5);
                        double d7 = d5 + (d6 - ceil2);
                        double dp28 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(dp28);
                        double d8 = d7 - dp28;
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
                        double dp29 = AndroidUtilities.dp(24.0f);
                        Double.isNaN(dp29);
                        this.nameMuteLeft = (int) (d12 - dp29);
                    } else if (this.drawScam != 0) {
                        double d13 = this.nameLeft;
                        double d14 = this.nameWidth;
                        Double.isNaN(d14);
                        Double.isNaN(d13);
                        double d15 = d13 + (d14 - ceil2);
                        double dp30 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(dp30);
                        double d16 = d15 - dp30;
                        double intrinsicWidth7 = (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).getIntrinsicWidth();
                        Double.isNaN(intrinsicWidth7);
                        this.nameMuteLeft = (int) (d16 - intrinsicWidth7);
                    }
                    if (lineLeft == 0.0f) {
                        int i50 = this.nameWidth;
                        if (ceil2 < i50) {
                            double d17 = this.nameLeft;
                            double d18 = i50;
                            Double.isNaN(d18);
                            Double.isNaN(d17);
                            this.nameLeft = (int) (d17 + (d18 - ceil2));
                        }
                    }
                }
                StaticLayout staticLayout4 = this.messageLayout;
                if (staticLayout4 != null && (lineCount2 = staticLayout4.getLineCount()) > 0) {
                    int i51 = 0;
                    int i52 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    while (true) {
                        if (i51 >= lineCount2) {
                            break;
                        }
                        if (this.messageLayout.getLineLeft(i51) != 0.0f) {
                            i52 = 0;
                            break;
                        }
                        double ceil3 = Math.ceil(this.messageLayout.getLineWidth(i51));
                        double d19 = max;
                        Double.isNaN(d19);
                        i52 = Math.min(i52, (int) (d19 - ceil3));
                        i51++;
                    }
                    if (i52 != Integer.MAX_VALUE) {
                        this.messageLeft += i52;
                    }
                }
                StaticLayout staticLayout5 = this.messageNameLayout;
                if (staticLayout5 != null && staticLayout5.getLineCount() > 0 && this.messageNameLayout.getLineLeft(0) == 0.0f) {
                    double ceil4 = Math.ceil(this.messageNameLayout.getLineWidth(0));
                    double d20 = max;
                    if (ceil4 < d20) {
                        double d21 = this.messageNameLeft;
                        Double.isNaN(d20);
                        Double.isNaN(d21);
                        this.messageNameLeft = (int) (d21 + (d20 - ceil4));
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
                        int i53 = this.nameWidth;
                        if (ceil5 < i53) {
                            double d22 = this.nameLeft;
                            double d23 = i53;
                            Double.isNaN(d23);
                            Double.isNaN(d22);
                            this.nameLeft = (int) (d22 - (d23 - ceil5));
                        }
                    }
                    if (this.dialogMuted || this.drawUnmute || this.drawVerified || this.drawPremium || this.drawScam != 0) {
                        this.nameMuteLeft = (int) (this.nameLeft + lineRight + AndroidUtilities.dp(6.0f));
                    }
                }
                StaticLayout staticLayout7 = this.messageLayout;
                if (staticLayout7 != null && (lineCount = staticLayout7.getLineCount()) > 0) {
                    float f4 = 2.14748365E9f;
                    for (int i54 = 0; i54 < lineCount; i54++) {
                        f4 = Math.min(f4, this.messageLayout.getLineLeft(i54));
                    }
                    this.messageLeft = (int) (this.messageLeft - f4);
                }
                StaticLayout staticLayout8 = this.messageNameLayout;
                if (staticLayout8 != null && staticLayout8.getLineCount() > 0) {
                    this.messageNameLeft = (int) (this.messageNameLeft - this.messageNameLayout.getLineLeft(0));
                }
            }
            staticLayout = this.messageLayout;
            if (staticLayout != null && this.thumbsCount > 0) {
                try {
                    length = staticLayout.getText().length();
                    if (i9 >= length) {
                        i9 = length - 1;
                    }
                    ceil = (int) Math.ceil(Math.min(this.messageLayout.getPrimaryHorizontal(i9), this.messageLayout.getPrimaryHorizontal(i9 + 1)));
                    if (ceil != 0) {
                        ceil += AndroidUtilities.dp(3.0f);
                    }
                    for (i19 = 0; i19 < this.thumbsCount; i19++) {
                        this.thumbImage[i19].setImageX(this.messageLeft + ceil + AndroidUtilities.dp((i12 + 2) * i19));
                    }
                } catch (Exception e7) {
                    FileLog.e(e7);
                }
            }
            staticLayout2 = this.messageLayout;
            if (staticLayout2 != null || this.printingStringType < 0) {
            }
            if (i14 >= 0 && (i18 = i14 + 1) < staticLayout2.getText().length()) {
                primaryHorizontal = this.messageLayout.getPrimaryHorizontal(i14);
                primaryHorizontal2 = this.messageLayout.getPrimaryHorizontal(i18);
            } else {
                primaryHorizontal = this.messageLayout.getPrimaryHorizontal(0);
                primaryHorizontal2 = this.messageLayout.getPrimaryHorizontal(1);
            }
            if (primaryHorizontal < primaryHorizontal2) {
                this.statusDrawableLeft = (int) (this.messageLeft + primaryHorizontal);
                return;
            } else {
                this.statusDrawableLeft = (int) (this.messageLeft + primaryHorizontal2 + AndroidUtilities.dp(3.0f));
                return;
            }
        }
        if (this.thumbsCount > 0 && charSequence20 != null) {
            max += AndroidUtilities.dp(5.0f);
        }
        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence21, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence20 == null ? 1 : 2);
        this.spoilersPool.addAll(this.spoilers);
        this.spoilers.clear();
        SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
        if (!LocaleController.isRTL) {
        }
        staticLayout = this.messageLayout;
        if (staticLayout != null) {
            length = staticLayout.getText().length();
            if (i9 >= length) {
            }
            ceil = (int) Math.ceil(Math.min(this.messageLayout.getPrimaryHorizontal(i9), this.messageLayout.getPrimaryHorizontal(i9 + 1)));
            if (ceil != 0) {
            }
            while (i19 < this.thumbsCount) {
            }
        }
        staticLayout2 = this.messageLayout;
        if (staticLayout2 != null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$buildLayout$0(MessageObject messageObject, MessageObject messageObject2) {
        return messageObject.getId() - messageObject2.getId();
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

    public void checkCurrentDialogIndex(boolean z) {
        MessageObject messageObject;
        MessageObject messageObject2;
        DialogsActivity dialogsActivity = this.parentFragment;
        if (dialogsActivity == null) {
            return;
        }
        ArrayList<TLRPC$Dialog> dialogsArray = dialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, z);
        if (this.index >= dialogsArray.size()) {
            return;
        }
        TLRPC$Dialog tLRPC$Dialog = dialogsArray.get(this.index);
        boolean z2 = true;
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
        if (this.currentDialogId == tLRPC$Dialog.id && (((messageObject2 = this.message) == null || messageObject2.getId() == tLRPC$Dialog.top_message) && ((messageObject == null || messageObject.messageOwner.edit_date == this.currentEditDate) && this.unreadCount == tLRPC$Dialog.unread_count && this.mentionCount == tLRPC$Dialog.unread_mentions_count && this.markUnread == tLRPC$Dialog.unread_mark && this.message == messageObject && draft == this.draftMessage && this.drawPin == tLRPC$Dialog.pinned))) {
            return;
        }
        long j = this.currentDialogId;
        long j2 = tLRPC$Dialog.id;
        boolean z3 = j != j2;
        this.currentDialogId = j2;
        if (z3) {
            this.lastDialogChangedTime = System.currentTimeMillis();
            ValueAnimator valueAnimator = this.statusDrawableAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.statusDrawableAnimator.cancel();
            }
            this.statusDrawableAnimationInProgress = false;
            this.lastStatusDrawableParams = -1;
        }
        boolean z4 = tLRPC$Dialog instanceof TLRPC$TL_dialogFolder;
        if (z4) {
            this.currentDialogFolderId = ((TLRPC$TL_dialogFolder) tLRPC$Dialog).folder.id;
        } else {
            this.currentDialogFolderId = 0;
        }
        int i = this.dialogsType;
        if (i == 7 || i == 8) {
            MessagesController.DialogFilter dialogFilter = MessagesController.getInstance(this.currentAccount).selectedDialogFilter[this.dialogsType == 8 ? (char) 1 : (char) 0];
            if (!(tLRPC$Dialog instanceof TLRPC$TL_dialog) || tLRPC$Dialog2 == null || dialogFilter == null || dialogFilter.pinnedDialogs.indexOfKey(tLRPC$Dialog.id) < 0 || dialogFilter.pinnedDialogs.indexOfKey(tLRPC$Dialog2.id) >= 0) {
                z2 = false;
            }
            this.fullSeparator = z2;
            this.fullSeparator2 = false;
        } else {
            this.fullSeparator = (tLRPC$Dialog instanceof TLRPC$TL_dialog) && tLRPC$Dialog.pinned && tLRPC$Dialog2 != null && !tLRPC$Dialog2.pinned;
            if (!z4 || tLRPC$Dialog2 == null || tLRPC$Dialog2.pinned) {
                z2 = false;
            }
            this.fullSeparator2 = z2;
        }
        update(0, !z3);
        if (z3) {
            this.reorderIconProgress = (!this.drawPin || !this.drawReorder) ? 0.0f : 1.0f;
        }
        checkOnline();
        checkGroupCall();
        checkChatTheme();
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

    public void update(int i) {
        update(i, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:148:0x0291  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x02c8  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0380  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x0385  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void update(int i, boolean z) {
        long j;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Chat chat;
        boolean z2;
        MessageObject messageObject;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        TLRPC$Chat chat2;
        MessageObject messageObject2;
        CustomDialog customDialog = this.customDialog;
        boolean z3 = true;
        if (customDialog != null) {
            this.lastMessageDate = customDialog.date;
            int i7 = customDialog.unread_count;
            if (i7 == 0) {
                z3 = false;
            }
            this.lastUnreadState = z3;
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
            boolean z4 = this.reactionMentionCount != 0;
            boolean z5 = this.markUnread;
            this.hasUnmutedTopics = false;
            if (this.isDialogCell) {
                TLRPC$Dialog tLRPC$Dialog = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                if (tLRPC$Dialog == null) {
                    this.unreadCount = 0;
                    this.mentionCount = 0;
                    this.reactionMentionCount = 0;
                    this.currentEditDate = 0;
                    this.lastMessageDate = 0;
                    this.clearingDialog = false;
                } else if (i == 0) {
                    this.clearingDialog = MessagesController.getInstance(this.currentAccount).isClearingDialog(tLRPC$Dialog.id);
                    ArrayList<MessageObject> arrayList = MessagesController.getInstance(this.currentAccount).dialogMessage.get(tLRPC$Dialog.id);
                    this.groupMessages = arrayList;
                    MessageObject messageObject3 = (arrayList == null || arrayList.size() <= 0) ? null : this.groupMessages.get(0);
                    this.message = messageObject3;
                    this.lastUnreadState = messageObject3 != null && messageObject3.isUnread();
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
                if (this.isDialogCell && (i & MessagesController.UPDATE_MASK_USER_PRINT) != 0) {
                    CharSequence printingString = MessagesController.getInstance(this.currentAccount).getPrintingString(this.currentDialogId, 0, true);
                    CharSequence charSequence = this.lastPrintString;
                    if ((charSequence != null && printingString == null) || ((charSequence == null && printingString != null) || (charSequence != null && !charSequence.equals(printingString)))) {
                        z2 = true;
                        if (!z2 && (i & MessagesController.UPDATE_MASK_MESSAGE_TEXT) != 0 && (messageObject2 = this.message) != null && messageObject2.messageText != this.lastMessageString) {
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
                            MessageObject messageObject6 = this.message;
                            if (messageObject6 != null && this.lastUnreadState != messageObject6.isUnread()) {
                                this.lastUnreadState = this.message.isUnread();
                                z2 = true;
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
                                    z2 = true;
                                }
                            }
                        }
                        if (!z2 && (i & MessagesController.UPDATE_MASK_SEND_STATE) != 0 && (messageObject = this.message) != null) {
                            i2 = this.lastSendState;
                            i3 = messageObject.messageOwner.send_state;
                            if (i2 != i3) {
                                this.lastSendState = i3;
                                z2 = true;
                            }
                        }
                        if (!z2) {
                            invalidate();
                            return;
                        }
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
                    i3 = messageObject.messageOwner.send_state;
                    if (i2 != i3) {
                    }
                }
                if (!z2) {
                }
            }
            this.user = null;
            this.chat = null;
            this.encryptedChat = null;
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
            if (z && ((i9 != this.unreadCount || z5 != this.markUnread) && (!this.isDialogCell || System.currentTimeMillis() - this.lastDialogChangedTime > 100))) {
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
                if ((i9 == 0 || this.markUnread) && (this.markUnread || !z5)) {
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
                    String valueOf = String.valueOf(i9);
                    String valueOf2 = String.valueOf(this.unreadCount);
                    if (valueOf.length() == valueOf2.length()) {
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(valueOf);
                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(valueOf2);
                        SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(valueOf2);
                        for (int i12 = 0; i12 < valueOf.length(); i12++) {
                            if (valueOf.charAt(i12) == valueOf2.charAt(i12)) {
                                int i13 = i12 + 1;
                                spannableStringBuilder.setSpan(new EmptyStubSpan(), i12, i13, 0);
                                spannableStringBuilder2.setSpan(new EmptyStubSpan(), i12, i13, 0);
                            } else {
                                spannableStringBuilder3.setSpan(new EmptyStubSpan(), i12, i12 + 1, 0);
                            }
                        }
                        int max = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(valueOf)));
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
            if (z && z6 != z4) {
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
                this.reactionsMentionsAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.2
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
            this.avatarImage.setRoundRadius(AndroidUtilities.dp((!this.isDialogCell || (tLRPC$Chat = this.chat) == null || !tLRPC$Chat.forum || this.currentDialogFolderId != 0) ? 28.0f : 16.0f));
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
    /* JADX WARN: Removed duplicated region for block: B:102:0x07e3  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x086d  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x098f  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x0a64  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x0a72 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0ad0  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0aea  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x0c5e  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x0c86  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x10e7  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x1107  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x111e  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x1166  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x116d  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x150e  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x1515  */
    /* JADX WARN: Removed duplicated region for block: B:317:0x153a  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x15a1  */
    /* JADX WARN: Removed duplicated region for block: B:342:0x15ee  */
    /* JADX WARN: Removed duplicated region for block: B:347:0x1625  */
    /* JADX WARN: Removed duplicated region for block: B:358:0x167e  */
    /* JADX WARN: Removed duplicated region for block: B:364:0x1693  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x16ae  */
    /* JADX WARN: Removed duplicated region for block: B:376:0x16df  */
    /* JADX WARN: Removed duplicated region for block: B:378:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:380:0x16bc  */
    /* JADX WARN: Removed duplicated region for block: B:389:0x164f  */
    /* JADX WARN: Removed duplicated region for block: B:398:0x15fa  */
    /* JADX WARN: Removed duplicated region for block: B:405:0x160d  */
    /* JADX WARN: Removed duplicated region for block: B:426:0x124c  */
    /* JADX WARN: Removed duplicated region for block: B:458:0x1426  */
    /* JADX WARN: Removed duplicated region for block: B:462:0x14b3  */
    /* JADX WARN: Removed duplicated region for block: B:466:0x14c8  */
    /* JADX WARN: Removed duplicated region for block: B:471:0x14dc  */
    /* JADX WARN: Removed duplicated region for block: B:478:0x14ef  */
    /* JADX WARN: Removed duplicated region for block: B:515:0x0cd4  */
    /* JADX WARN: Removed duplicated region for block: B:640:0x0b3e  */
    /* JADX WARN: Removed duplicated region for block: B:644:0x0ad3  */
    /* JADX WARN: Removed duplicated region for block: B:656:0x0b54  */
    /* JADX WARN: Removed duplicated region for block: B:669:0x0b9c  */
    /* JADX WARN: Removed duplicated region for block: B:723:0x0a5a  */
    /* JADX WARN: Removed duplicated region for block: B:734:0x0861  */
    /* JADX WARN: Removed duplicated region for block: B:735:0x07de  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x079d  */
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
        long j;
        String str3;
        Canvas canvas2;
        float f3;
        int i3;
        String str4;
        Canvas canvas3;
        float f4;
        int i4;
        float f5;
        boolean z2;
        int dp;
        int dp2;
        int dp3;
        float f6;
        float interpolation;
        long j2;
        float f7;
        boolean z3;
        float f8;
        boolean z4;
        float f9;
        TLRPC$User tLRPC$User;
        TLRPC$Chat tLRPC$Chat;
        float dp4;
        int i5;
        float dp5;
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
        int i6;
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
            int i7 = color2;
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
                Theme.dialogs_pinnedPaint.setColor(i7);
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
            int i8 = i2;
            if (this.swipeMessageTextId != i8 || this.swipeMessageWidth != getMeasuredWidth()) {
                this.swipeMessageTextId = i8;
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
            if (this.nameLayoutEllipsizeByGradient && !this.nameLayoutFits) {
                if (this.nameLayoutEllipsizeLeft && this.fadePaint == null) {
                    Paint paint = new Paint();
                    this.fadePaint = paint;
                    paint.setShader(new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(24.0f), 0.0f, new int[]{-1, 0}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                    this.fadePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                } else if (this.fadePaintBack == null) {
                    Paint paint2 = new Paint();
                    this.fadePaintBack = paint2;
                    paint2.setShader(new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(24.0f), 0.0f, new int[]{0, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                    this.fadePaintBack.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                }
                canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), 255, 31);
                int i9 = this.nameLeft;
                canvas.clipRect(i9, 0, this.nameWidth + i9, getMeasuredHeight());
            }
            if (this.currentDialogFolderId != 0) {
                TextPaint[] textPaintArr = Theme.dialogs_namePaint;
                int i10 = this.paintIndex;
                TextPaint textPaint = textPaintArr[i10];
                TextPaint textPaint2 = textPaintArr[i10];
                int color3 = Theme.getColor("chats_nameArchived", this.resourcesProvider);
                textPaint2.linkColor = color3;
                textPaint.setColor(color3);
            } else if (this.encryptedChat != null || ((customDialog = this.customDialog) != null && customDialog.type == 2)) {
                TextPaint[] textPaintArr2 = Theme.dialogs_namePaint;
                int i11 = this.paintIndex;
                TextPaint textPaint3 = textPaintArr2[i11];
                TextPaint textPaint4 = textPaintArr2[i11];
                int color4 = Theme.getColor("chats_secretName", this.resourcesProvider);
                textPaint4.linkColor = color4;
                textPaint3.setColor(color4);
            } else {
                TextPaint[] textPaintArr3 = Theme.dialogs_namePaint;
                int i12 = this.paintIndex;
                TextPaint textPaint5 = textPaintArr3[i12];
                TextPaint textPaint6 = textPaintArr3[i12];
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
                if (this.timeLayout != null && this.currentDialogFolderId == 0) {
                    canvas.save();
                    canvas.translate(this.timeLeft, this.timeTop);
                    this.timeLayout.draw(canvas);
                    canvas.restore();
                }
                if (!drawLock2()) {
                    Theme.dialogs_lock2Drawable.setBounds(this.lock2Left, this.timeTop + ((this.timeLayout.getHeight() - Theme.dialogs_lock2Drawable.getIntrinsicHeight()) / 2), this.lock2Left + Theme.dialogs_lock2Drawable.getIntrinsicWidth(), this.timeTop + ((this.timeLayout.getHeight() - Theme.dialogs_lock2Drawable.getIntrinsicHeight()) / 2) + Theme.dialogs_lock2Drawable.getIntrinsicHeight());
                    Theme.dialogs_lock2Drawable.draw(canvas);
                }
                if (this.messageNameLayout == null) {
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
                        i3 = 1;
                        j = j4;
                        str3 = "windowBackgroundWhite";
                        f3 = 1.0f;
                        canvas2 = canvas;
                        try {
                            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageNameLayout, this.animatedEmojiStack2, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
                        } catch (Exception e) {
                            e = e;
                            FileLog.e(e);
                            canvas.restore();
                            if (this.messageLayout != null) {
                            }
                            if (this.currentDialogFolderId == 0) {
                            }
                            if (!this.drawUnmute) {
                            }
                            if (this.dialogsType == i4) {
                            }
                            if (this.drawVerified) {
                            }
                            if (!this.drawReorder) {
                            }
                            Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                            BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                            Theme.dialogs_reorderDrawable.draw(canvas3);
                            if (!this.drawError) {
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
                            f7 = 0.0f;
                            if (this.translationX != f7) {
                            }
                            if (this.currentDialogFolderId != 0) {
                            }
                            if (this.useSeparator) {
                            }
                            if (this.clipProgress != 0.0f) {
                            }
                            z3 = this.drawReorder;
                            if (!z3) {
                            }
                            if (!z3) {
                            }
                            z = true;
                            if (!this.archiveHidden) {
                            }
                        }
                    } catch (Exception e2) {
                        e = e2;
                        j = j4;
                        str3 = "windowBackgroundWhite";
                        canvas2 = canvas;
                        f3 = 1.0f;
                        i3 = 1;
                    }
                    canvas.restore();
                } else {
                    j = j4;
                    str3 = "windowBackgroundWhite";
                    canvas2 = canvas;
                    f3 = 1.0f;
                    i3 = 1;
                }
                if (this.messageLayout != null) {
                    if (this.currentDialogFolderId != 0) {
                        if (this.chat != null) {
                            TextPaint[] textPaintArr4 = Theme.dialogs_messagePaint;
                            int i13 = this.paintIndex;
                            TextPaint textPaint10 = textPaintArr4[i13];
                            TextPaint textPaint11 = textPaintArr4[i13];
                            int color9 = Theme.getColor("chats_nameMessageArchived", this.resourcesProvider);
                            textPaint11.linkColor = color9;
                            textPaint10.setColor(color9);
                        } else {
                            TextPaint[] textPaintArr5 = Theme.dialogs_messagePaint;
                            int i14 = this.paintIndex;
                            TextPaint textPaint12 = textPaintArr5[i14];
                            TextPaint textPaint13 = textPaintArr5[i14];
                            int color10 = Theme.getColor("chats_messageArchived", this.resourcesProvider);
                            textPaint13.linkColor = color10;
                            textPaint12.setColor(color10);
                        }
                    } else {
                        TextPaint[] textPaintArr6 = Theme.dialogs_messagePaint;
                        int i15 = this.paintIndex;
                        TextPaint textPaint14 = textPaintArr6[i15];
                        TextPaint textPaint15 = textPaintArr6[i15];
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
                            for (int i16 = 0; i16 < this.spoilers.size(); i16++) {
                                SpoilerEffect spoilerEffect = this.spoilers.get(i16);
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
                    int i17 = this.printingStringType;
                    if (i17 >= 0 && (chatStatusDrawable = Theme.getChatStatusDrawable(i17)) != null) {
                        canvas.save();
                        int i18 = this.printingStringType;
                        if (i18 == i3 || i18 == 4) {
                            canvas2.translate(this.statusDrawableLeft, this.messageTop + (i18 == i3 ? AndroidUtilities.dp(f3) : 0));
                        } else {
                            canvas2.translate(this.statusDrawableLeft, this.messageTop + ((AndroidUtilities.dp(18.0f) - chatStatusDrawable.getIntrinsicHeight()) / 2.0f));
                        }
                        chatStatusDrawable.draw(canvas2);
                        int i19 = this.statusDrawableLeft;
                        invalidate(i19, this.messageTop, chatStatusDrawable.getIntrinsicWidth() + i19, this.messageTop + chatStatusDrawable.getIntrinsicHeight());
                        canvas.restore();
                    }
                }
                if (this.currentDialogFolderId == 0) {
                    int i20 = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
                    int i21 = this.lastStatusDrawableParams;
                    if (i21 >= 0 && i21 != i20 && !this.statusDrawableAnimationInProgress) {
                        createStatusDrawableAnimator(i21, i20);
                    }
                    boolean z5 = this.statusDrawableAnimationInProgress;
                    if (z5) {
                        i20 = this.animateToStatusDrawableParams;
                    }
                    boolean z6 = (i20 & 1) != 0;
                    boolean z7 = (i20 & 2) != 0;
                    boolean z8 = (i20 & 4) != 0;
                    if (z5) {
                        int i22 = this.animateFromStatusDrawableParams;
                        boolean z9 = (i22 & 1) != 0;
                        boolean z10 = (i22 & 2) != 0;
                        boolean z11 = (i22 & 4) != 0;
                        if (!z6 && !z9 && z11 && !z10 && z7 && z8) {
                            str4 = str3;
                            f5 = 0.0f;
                            i4 = 2;
                            drawCheckStatus(canvas, z6, z7, z8, true, this.statusDrawableProgress);
                            canvas3 = canvas2;
                            f4 = 1.0f;
                        } else {
                            str4 = str3;
                            Canvas canvas4 = canvas2;
                            i4 = 2;
                            f5 = 0.0f;
                            boolean z12 = z9;
                            f4 = 1.0f;
                            boolean z13 = z10;
                            canvas3 = canvas4;
                            drawCheckStatus(canvas, z12, z13, z11, false, 1.0f - this.statusDrawableProgress);
                            drawCheckStatus(canvas, z6, z7, z8, false, this.statusDrawableProgress);
                        }
                    } else {
                        str4 = str3;
                        canvas3 = canvas2;
                        f4 = 1.0f;
                        i4 = 2;
                        f5 = 0.0f;
                        drawCheckStatus(canvas, z6, z7, z8, false, 1.0f);
                    }
                    this.lastStatusDrawableParams = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
                } else {
                    str4 = str3;
                    canvas3 = canvas2;
                    f4 = 1.0f;
                    i4 = 2;
                    f5 = 0.0f;
                }
                boolean z14 = !this.drawUnmute || this.dialogMuted;
                if (this.dialogsType == i4 && ((z14 || this.dialogMutedProgress > f5) && !this.drawVerified && this.drawScam == 0 && !this.drawPremium)) {
                    if (z14) {
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
                    if (!z14) {
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
                } else if (this.drawVerified) {
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
                    int i23 = this.drawScam;
                    if (i23 != 0) {
                        BaseCell.setDrawableBounds((Drawable) (i23 == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable), this.nameMuteLeft, AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.0f : 15.0f));
                        (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).draw(canvas3);
                        if (!this.drawReorder || this.reorderIconProgress != f5) {
                            Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                            BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                            Theme.dialogs_reorderDrawable.draw(canvas3);
                        }
                        if (!this.drawError) {
                            Theme.dialogs_errorDrawable.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                            this.rect.set(this.errorLeft, this.errorTop, i6 + AndroidUtilities.dp(23.0f), this.errorTop + AndroidUtilities.dp(23.0f));
                            RectF rectF = this.rect;
                            float f25 = AndroidUtilities.density;
                            canvas3.drawRoundRect(rectF, f25 * 11.5f, f25 * 11.5f, Theme.dialogs_errorPaint);
                            BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
                            Theme.dialogs_errorDrawable.draw(canvas3);
                        } else {
                            boolean z15 = this.drawCount;
                            if (((z15 || this.drawMention) && this.drawCount2) || this.countChangeProgress != f4 || this.drawReactionMention || this.reactionsMentionsChangeProgress != f4) {
                                if (this.isTopic) {
                                    z2 = this.topicMuted;
                                } else {
                                    TLRPC$Chat tLRPC$Chat2 = this.chat;
                                    z2 = (tLRPC$Chat2 == null || !tLRPC$Chat2.forum || this.forumTopic != null) ? this.dialogMuted : !this.hasUnmutedTopics;
                                }
                                if ((z15 && this.drawCount2) || this.countChangeProgress != f4) {
                                    int i24 = this.unreadCount;
                                    float f26 = (i24 != 0 || this.markUnread) ? this.countChangeProgress : f4 - this.countChangeProgress;
                                    StaticLayout staticLayout2 = this.countOldLayout;
                                    if (staticLayout2 == null || i24 == 0) {
                                        if (i24 != 0) {
                                            staticLayout2 = this.countLayout;
                                        }
                                        Paint paint3 = (z2 || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
                                        paint3.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                        Theme.dialogs_countTextPaint.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                        this.rect.set(this.countLeft - AndroidUtilities.dp(5.5f), this.countTop, dp3 + this.countWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                        if (f26 != f4) {
                                            if (getIsPinned()) {
                                                Theme.dialogs_pinnedDrawable.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                                BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                                                canvas.save();
                                                float f27 = f4 - f26;
                                                canvas3.scale(f27, f27, Theme.dialogs_pinnedDrawable.getBounds().centerX(), Theme.dialogs_pinnedDrawable.getBounds().centerY());
                                                Theme.dialogs_pinnedDrawable.draw(canvas3);
                                                canvas.restore();
                                            }
                                            canvas.save();
                                            canvas3.scale(f26, f26, this.rect.centerX(), this.rect.centerY());
                                        }
                                        RectF rectF2 = this.rect;
                                        float f28 = AndroidUtilities.density;
                                        canvas3.drawRoundRect(rectF2, f28 * 11.5f, f28 * 11.5f, paint3);
                                        if (staticLayout2 != null) {
                                            canvas.save();
                                            canvas3.translate(this.countLeft, this.countTop + AndroidUtilities.dp(4.0f));
                                            staticLayout2.draw(canvas3);
                                            canvas.restore();
                                        }
                                        if (f26 != f4) {
                                            canvas.restore();
                                        }
                                    } else {
                                        Paint paint4 = (z2 || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
                                        paint4.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                        Theme.dialogs_countTextPaint.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                        float f29 = f26 * 2.0f;
                                        float f30 = f29 > f4 ? 1.0f : f29;
                                        float f31 = f4 - f30;
                                        float f32 = (this.countLeft * f30) + (this.countLeftOld * f31);
                                        float dp16 = f32 - AndroidUtilities.dp(5.5f);
                                        this.rect.set(dp16, this.countTop, (this.countWidth * f30) + dp16 + (this.countWidthOld * f31) + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                        if (f26 <= 0.5f) {
                                            f6 = 0.1f;
                                            interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(f29);
                                        } else {
                                            f6 = 0.1f;
                                            interpolation = CubicBezierInterpolator.EASE_IN.getInterpolation(f4 - ((f26 - 0.5f) * 2.0f));
                                        }
                                        float f33 = (interpolation * f6) + f4;
                                        canvas.save();
                                        canvas3.scale(f33, f33, this.rect.centerX(), this.rect.centerY());
                                        RectF rectF3 = this.rect;
                                        float f34 = AndroidUtilities.density;
                                        canvas3.drawRoundRect(rectF3, f34 * 11.5f, f34 * 11.5f, paint4);
                                        if (this.countAnimationStableLayout != null) {
                                            canvas.save();
                                            canvas3.translate(f32, this.countTop + AndroidUtilities.dp(4.0f));
                                            this.countAnimationStableLayout.draw(canvas3);
                                            canvas.restore();
                                        }
                                        int alpha = Theme.dialogs_countTextPaint.getAlpha();
                                        float f35 = alpha;
                                        Theme.dialogs_countTextPaint.setAlpha((int) (f35 * f30));
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
                                            Theme.dialogs_countTextPaint.setAlpha((int) (f35 * f31));
                                            canvas.save();
                                            canvas3.translate(f32, ((this.countAnimationIncrement ? -AndroidUtilities.dp(13.0f) : AndroidUtilities.dp(13.0f)) * f30) + this.countTop + AndroidUtilities.dp(4.0f));
                                            this.countOldLayout.draw(canvas3);
                                            canvas.restore();
                                        }
                                        Theme.dialogs_countTextPaint.setAlpha(alpha);
                                        canvas.restore();
                                    }
                                }
                                if (this.drawMention) {
                                    Theme.dialogs_countPaint.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                    this.rect.set(this.mentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp2 + this.mentionWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                    Paint paint5 = (!z2 || this.folderId == 0) ? Theme.dialogs_countPaint : Theme.dialogs_countGrayPaint;
                                    RectF rectF4 = this.rect;
                                    float f36 = AndroidUtilities.density;
                                    canvas3.drawRoundRect(rectF4, f36 * 11.5f, f36 * 11.5f, paint5);
                                    if (this.mentionLayout != null) {
                                        Theme.dialogs_countTextPaint.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                        canvas.save();
                                        canvas3.translate(this.mentionLeft, this.countTop + AndroidUtilities.dp(4.0f));
                                        this.mentionLayout.draw(canvas3);
                                        canvas.restore();
                                    } else {
                                        Theme.dialogs_mentionDrawable.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                        BaseCell.setDrawableBounds(Theme.dialogs_mentionDrawable, this.mentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.2f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                        Theme.dialogs_mentionDrawable.draw(canvas3);
                                    }
                                }
                                if (this.drawReactionMention || this.reactionsMentionsChangeProgress != f4) {
                                    Theme.dialogs_reactionsCountPaint.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                    this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                    Paint paint6 = Theme.dialogs_reactionsCountPaint;
                                    canvas.save();
                                    float f37 = this.reactionsMentionsChangeProgress;
                                    if (f37 != f4) {
                                        if (!this.drawReactionMention) {
                                            f37 = f4 - f37;
                                        }
                                        canvas3.scale(f37, f37, this.rect.centerX(), this.rect.centerY());
                                    }
                                    RectF rectF5 = this.rect;
                                    float f38 = AndroidUtilities.density;
                                    canvas3.drawRoundRect(rectF5, f38 * 11.5f, f38 * 11.5f, paint6);
                                    Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                    BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                    Theme.dialogs_reactionsMentionDrawable.draw(canvas3);
                                    canvas.restore();
                                }
                            } else if (getIsPinned()) {
                                Theme.dialogs_pinnedDrawable.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                                Theme.dialogs_pinnedDrawable.draw(canvas3);
                            }
                        }
                        if (this.animatingArchiveAvatar) {
                            canvas.save();
                            float interpolation3 = this.interpolator.getInterpolation(this.animatingArchiveAvatarProgress / 170.0f) + f4;
                            canvas3.scale(interpolation3, interpolation3, this.avatarImage.getCenterX(), this.avatarImage.getCenterY());
                        }
                        if (this.drawAvatar && (this.currentDialogFolderId == 0 || (pullForegroundDrawable = this.archivedChatsDrawable) == null || !pullForegroundDrawable.isDraw())) {
                            this.avatarImage.draw(canvas3);
                        }
                        if (this.thumbsCount > 0) {
                            for (int i25 = 0; i25 < this.thumbsCount; i25++) {
                                this.thumbImage[i25].draw(canvas3);
                                if (this.drawPlay[i25]) {
                                    BaseCell.setDrawableBounds(Theme.dialogs_playDrawable, (int) (this.thumbImage[i25].getCenterX() - (Theme.dialogs_playDrawable.getIntrinsicWidth() / 2)), (int) (this.thumbImage[i25].getCenterY() - (Theme.dialogs_playDrawable.getIntrinsicHeight() / 2)));
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
                                        if (f41 < f4) {
                                            j2 = j;
                                            float f42 = f41 + (((float) j2) / 150.0f);
                                            this.onlineProgress = f42;
                                            if (f42 > f4) {
                                                this.onlineProgress = f4;
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
                                    boolean z16 = tLRPC$Chat.call_active && tLRPC$Chat.call_not_empty;
                                    this.hasCall = z16;
                                    if (z16 || this.chatCallProgress != 0.0f) {
                                        CheckBox2 checkBox2 = this.checkBox;
                                        float progress = (checkBox2 == null || !checkBox2.isChecked()) ? 1.0f : f4 - this.checkBox.getProgress();
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
                                        int i26 = this.progressStage;
                                        if (i26 == 0) {
                                            dp5 = AndroidUtilities.dp(f4) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                            dp9 = AndroidUtilities.dp(3.0f);
                                            dp10 = AndroidUtilities.dp(2.0f);
                                            f11 = this.innerProgress;
                                        } else {
                                            if (i26 == 1) {
                                                dp5 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                                dp6 = AndroidUtilities.dp(f4);
                                                dp7 = AndroidUtilities.dp(4.0f);
                                                f10 = this.innerProgress;
                                            } else if (i26 == 2) {
                                                dp5 = AndroidUtilities.dp(f4) + (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                                dp9 = AndroidUtilities.dp(5.0f);
                                                dp10 = AndroidUtilities.dp(4.0f);
                                                f11 = this.innerProgress;
                                            } else if (i26 == 3) {
                                                dp5 = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                                dp6 = AndroidUtilities.dp(f4);
                                                dp7 = AndroidUtilities.dp(2.0f);
                                                f10 = this.innerProgress;
                                            } else if (i26 == 4) {
                                                dp5 = AndroidUtilities.dp(f4) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                                dp9 = AndroidUtilities.dp(3.0f);
                                                dp10 = AndroidUtilities.dp(2.0f);
                                                f11 = this.innerProgress;
                                            } else if (i26 == 5) {
                                                dp5 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                                dp6 = AndroidUtilities.dp(f4);
                                                dp7 = AndroidUtilities.dp(4.0f);
                                                f10 = this.innerProgress;
                                            } else if (i26 == 6) {
                                                dp5 = AndroidUtilities.dp(f4) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                                dp8 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                                if (this.chatCallProgress >= f4 || progress < f4) {
                                                    canvas.save();
                                                    float f47 = this.chatCallProgress;
                                                    canvas3.scale(f47 * progress, f47 * progress, f45, f46);
                                                }
                                                this.rect.set(i5 - AndroidUtilities.dp(f4), f46 - dp5, AndroidUtilities.dp(f4) + i5, dp5 + f46);
                                                canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                                float f48 = f46 - dp8;
                                                float f49 = f46 + dp8;
                                                this.rect.set(i5 - AndroidUtilities.dp(5.0f), f48, i5 - AndroidUtilities.dp(3.0f), f49);
                                                canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                                this.rect.set(AndroidUtilities.dp(3.0f) + i5, f48, i5 + AndroidUtilities.dp(5.0f), f49);
                                                canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                                if (this.chatCallProgress >= f4 || progress < f4) {
                                                    canvas.restore();
                                                }
                                                float f50 = (float) j2;
                                                f12 = this.innerProgress + (f50 / 400.0f);
                                                this.innerProgress = f12;
                                                if (f12 >= f4) {
                                                    this.innerProgress = 0.0f;
                                                    int i27 = this.progressStage + 1;
                                                    this.progressStage = i27;
                                                    if (i27 >= 8) {
                                                        this.progressStage = 0;
                                                    }
                                                }
                                                if (this.hasCall) {
                                                    float f51 = this.chatCallProgress;
                                                    if (f51 < f4) {
                                                        float f52 = f51 + (f50 / 150.0f);
                                                        this.chatCallProgress = f52;
                                                        if (f52 > f4) {
                                                            this.chatCallProgress = f4;
                                                        }
                                                    }
                                                    f7 = 0.0f;
                                                } else {
                                                    float f53 = this.chatCallProgress;
                                                    f7 = 0.0f;
                                                    if (f53 > 0.0f) {
                                                        float f54 = f53 - (f50 / 150.0f);
                                                        this.chatCallProgress = f54;
                                                        if (f54 < 0.0f) {
                                                            this.chatCallProgress = 0.0f;
                                                        }
                                                    }
                                                }
                                                z = true;
                                                if (this.translationX != f7) {
                                                    canvas.restore();
                                                }
                                                if (this.currentDialogFolderId != 0 && this.translationX == f7 && this.archivedChatsDrawable != null) {
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
                                                        z3 = this.drawReorder;
                                                        if (!z3 || this.reorderIconProgress != 0.0f) {
                                                            if (!z3) {
                                                                float f55 = this.reorderIconProgress;
                                                                if (f55 < f4) {
                                                                    float f56 = f55 + (((float) j2) / 170.0f);
                                                                    this.reorderIconProgress = f56;
                                                                    if (f56 > f4) {
                                                                        this.reorderIconProgress = f4;
                                                                    }
                                                                    f8 = 0.0f;
                                                                }
                                                            } else {
                                                                float f57 = this.reorderIconProgress;
                                                                f8 = 0.0f;
                                                                if (f57 > 0.0f) {
                                                                    float f58 = f57 - (((float) j2) / 170.0f);
                                                                    this.reorderIconProgress = f58;
                                                                    if (f58 < 0.0f) {
                                                                        this.reorderIconProgress = 0.0f;
                                                                    }
                                                                }
                                                                if (!this.archiveHidden) {
                                                                    float f59 = this.archiveBackgroundProgress;
                                                                    if (f59 > f8) {
                                                                        float f60 = f59 - (((float) j2) / 230.0f);
                                                                        this.archiveBackgroundProgress = f60;
                                                                        if (f60 < f8) {
                                                                            this.archiveBackgroundProgress = f8;
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
                                                                        if (f62 < f4) {
                                                                            float f63 = f62 + (((float) j2) / 170.0f);
                                                                            this.currentRevealBounceProgress = f63;
                                                                            if (f63 > f4) {
                                                                                this.currentRevealBounceProgress = f4;
                                                                                z4 = true;
                                                                                f9 = this.currentRevealProgress;
                                                                                if (f9 < f4) {
                                                                                    float f64 = f9 + (((float) j2) / 300.0f);
                                                                                    this.currentRevealProgress = f64;
                                                                                    if (f64 > f4) {
                                                                                        this.currentRevealProgress = f4;
                                                                                    }
                                                                                    z4 = true;
                                                                                }
                                                                                if (!z4) {
                                                                                    return;
                                                                                }
                                                                                invalidate();
                                                                                return;
                                                                            }
                                                                        }
                                                                        z4 = z;
                                                                        f9 = this.currentRevealProgress;
                                                                        if (f9 < f4) {
                                                                        }
                                                                        if (!z4) {
                                                                        }
                                                                    } else {
                                                                        if (this.currentRevealBounceProgress == f4) {
                                                                            this.currentRevealBounceProgress = 0.0f;
                                                                            z4 = true;
                                                                        } else {
                                                                            z4 = z;
                                                                        }
                                                                        float f65 = this.currentRevealProgress;
                                                                        if (f65 > 0.0f) {
                                                                            float f66 = f65 - (((float) j2) / 300.0f);
                                                                            this.currentRevealProgress = f66;
                                                                            if (f66 < 0.0f) {
                                                                                this.currentRevealProgress = 0.0f;
                                                                            }
                                                                            z4 = true;
                                                                        }
                                                                        if (!z4) {
                                                                        }
                                                                    }
                                                                } else {
                                                                    float f67 = this.archiveBackgroundProgress;
                                                                    if (f67 < f4) {
                                                                        float f68 = f67 + (((float) j2) / 230.0f);
                                                                        this.archiveBackgroundProgress = f68;
                                                                        if (f68 > f4) {
                                                                            this.archiveBackgroundProgress = f4;
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
                                                            if (!this.archiveHidden) {
                                                            }
                                                        }
                                                        f8 = 0.0f;
                                                        if (!this.archiveHidden) {
                                                        }
                                                    }
                                                }
                                                if (this.clipProgress != 0.0f) {
                                                }
                                                z3 = this.drawReorder;
                                                if (!z3) {
                                                }
                                                if (!z3) {
                                                }
                                                z = true;
                                                if (!this.archiveHidden) {
                                                }
                                            } else {
                                                dp5 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                                dp6 = AndroidUtilities.dp(f4);
                                                dp7 = AndroidUtilities.dp(2.0f);
                                                f10 = this.innerProgress;
                                            }
                                            dp8 = dp6 + (dp7 * f10);
                                            if (this.chatCallProgress >= f4) {
                                            }
                                            canvas.save();
                                            float f472 = this.chatCallProgress;
                                            canvas3.scale(f472 * progress, f472 * progress, f45, f46);
                                            this.rect.set(i5 - AndroidUtilities.dp(f4), f46 - dp5, AndroidUtilities.dp(f4) + i5, dp5 + f46);
                                            canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                            float f482 = f46 - dp8;
                                            float f492 = f46 + dp8;
                                            this.rect.set(i5 - AndroidUtilities.dp(5.0f), f482, i5 - AndroidUtilities.dp(3.0f), f492);
                                            canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                            this.rect.set(AndroidUtilities.dp(3.0f) + i5, f482, i5 + AndroidUtilities.dp(5.0f), f492);
                                            canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                            if (this.chatCallProgress >= f4) {
                                            }
                                            canvas.restore();
                                            float f502 = (float) j2;
                                            f12 = this.innerProgress + (f502 / 400.0f);
                                            this.innerProgress = f12;
                                            if (f12 >= f4) {
                                            }
                                            if (this.hasCall) {
                                            }
                                            z = true;
                                            if (this.translationX != f7) {
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
                                            z3 = this.drawReorder;
                                            if (!z3) {
                                            }
                                            if (!z3) {
                                            }
                                            z = true;
                                            if (!this.archiveHidden) {
                                            }
                                        }
                                        dp8 = dp9 - (dp10 * f11);
                                        if (this.chatCallProgress >= f4) {
                                        }
                                        canvas.save();
                                        float f4722 = this.chatCallProgress;
                                        canvas3.scale(f4722 * progress, f4722 * progress, f45, f46);
                                        this.rect.set(i5 - AndroidUtilities.dp(f4), f46 - dp5, AndroidUtilities.dp(f4) + i5, dp5 + f46);
                                        canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                        float f4822 = f46 - dp8;
                                        float f4922 = f46 + dp8;
                                        this.rect.set(i5 - AndroidUtilities.dp(5.0f), f4822, i5 - AndroidUtilities.dp(3.0f), f4922);
                                        canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                        this.rect.set(AndroidUtilities.dp(3.0f) + i5, f4822, i5 + AndroidUtilities.dp(5.0f), f4922);
                                        canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                        if (this.chatCallProgress >= f4) {
                                        }
                                        canvas.restore();
                                        float f5022 = (float) j2;
                                        f12 = this.innerProgress + (f5022 / 400.0f);
                                        this.innerProgress = f12;
                                        if (f12 >= f4) {
                                        }
                                        if (this.hasCall) {
                                        }
                                        z = true;
                                        if (this.translationX != f7) {
                                        }
                                        if (this.currentDialogFolderId != 0) {
                                        }
                                        if (this.useSeparator) {
                                        }
                                        if (this.clipProgress != 0.0f) {
                                        }
                                        z3 = this.drawReorder;
                                        if (!z3) {
                                        }
                                        if (!z3) {
                                        }
                                        z = true;
                                        if (!this.archiveHidden) {
                                        }
                                    }
                                }
                            }
                            f7 = 0.0f;
                            if (this.translationX != f7) {
                            }
                            if (this.currentDialogFolderId != 0) {
                            }
                            if (this.useSeparator) {
                            }
                            if (this.clipProgress != 0.0f) {
                            }
                            z3 = this.drawReorder;
                            if (!z3) {
                            }
                            if (!z3) {
                            }
                            z = true;
                            if (!this.archiveHidden) {
                            }
                        }
                        j2 = j;
                        f7 = 0.0f;
                        if (this.translationX != f7) {
                        }
                        if (this.currentDialogFolderId != 0) {
                        }
                        if (this.useSeparator) {
                        }
                        if (this.clipProgress != 0.0f) {
                        }
                        z3 = this.drawReorder;
                        if (!z3) {
                        }
                        if (!z3) {
                        }
                        z = true;
                        if (!this.archiveHidden) {
                        }
                    }
                }
                if (!this.drawReorder) {
                }
                Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                Theme.dialogs_reorderDrawable.draw(canvas3);
                if (!this.drawError) {
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
                    f7 = 0.0f;
                    if (this.translationX != f7) {
                    }
                    if (this.currentDialogFolderId != 0) {
                    }
                    if (this.useSeparator) {
                    }
                    if (this.clipProgress != 0.0f) {
                    }
                    z3 = this.drawReorder;
                    if (!z3) {
                    }
                    if (!z3) {
                    }
                    z = true;
                    if (!this.archiveHidden) {
                    }
                }
                j2 = j;
                f7 = 0.0f;
                if (this.translationX != f7) {
                }
                if (this.currentDialogFolderId != 0) {
                }
                if (this.useSeparator) {
                }
                if (this.clipProgress != 0.0f) {
                }
                z3 = this.drawReorder;
                if (!z3) {
                }
                if (!z3) {
                }
                z = true;
                if (!this.archiveHidden) {
                }
            }
        }
        if (this.timeLayout != null) {
            canvas.save();
            canvas.translate(this.timeLeft, this.timeTop);
            this.timeLayout.draw(canvas);
            canvas.restore();
        }
        if (!drawLock2()) {
        }
        if (this.messageNameLayout == null) {
        }
        if (this.messageLayout != null) {
        }
        if (this.currentDialogFolderId == 0) {
        }
        if (!this.drawUnmute) {
        }
        if (this.dialogsType == i4) {
        }
        if (this.drawVerified) {
        }
        if (!this.drawReorder) {
        }
        Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
        BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
        Theme.dialogs_reorderDrawable.draw(canvas3);
        if (!this.drawError) {
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
        f7 = 0.0f;
        if (this.translationX != f7) {
        }
        if (this.currentDialogFolderId != 0) {
        }
        if (this.useSeparator) {
        }
        if (this.clipProgress != 0.0f) {
        }
        z3 = this.drawReorder;
        if (!z3) {
        }
        if (!z3) {
        }
        z = true;
        if (!this.archiveHidden) {
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
}
