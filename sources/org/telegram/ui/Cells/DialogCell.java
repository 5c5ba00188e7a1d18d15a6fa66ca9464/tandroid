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

    /* JADX WARN: Code restructure failed: missing block: B:1419:0x054d, code lost:
        if (r2.post_messages == false) goto L617;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1425:0x0559, code lost:
        if (r2.kicked != false) goto L617;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:102:0x1766  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x176f A[Catch: Exception -> 0x180e, TryCatch #1 {Exception -> 0x180e, blocks: (B:90:0x171a, B:93:0x1724, B:95:0x1730, B:98:0x174a, B:100:0x1753, B:103:0x1769, B:105:0x176f, B:106:0x177b, B:108:0x1792, B:110:0x1798, B:113:0x17a9, B:115:0x17ad, B:116:0x17eb, B:118:0x17ef, B:120:0x17f8, B:121:0x1802, B:488:0x17ce), top: B:89:0x171a }] */
    /* JADX WARN: Removed duplicated region for block: B:1081:0x0bed  */
    /* JADX WARN: Removed duplicated region for block: B:1094:0x0c51  */
    /* JADX WARN: Removed duplicated region for block: B:1098:0x0f1a  */
    /* JADX WARN: Removed duplicated region for block: B:1108:0x0f65  */
    /* JADX WARN: Removed duplicated region for block: B:1113:0x0f76  */
    /* JADX WARN: Removed duplicated region for block: B:1132:0x0c65  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x17ad A[Catch: Exception -> 0x180e, TryCatch #1 {Exception -> 0x180e, blocks: (B:90:0x171a, B:93:0x1724, B:95:0x1730, B:98:0x174a, B:100:0x1753, B:103:0x1769, B:105:0x176f, B:106:0x177b, B:108:0x1792, B:110:0x1798, B:113:0x17a9, B:115:0x17ad, B:116:0x17eb, B:118:0x17ef, B:120:0x17f8, B:121:0x1802, B:488:0x17ce), top: B:89:0x171a }] */
    /* JADX WARN: Removed duplicated region for block: B:1412:0x053d  */
    /* JADX WARN: Removed duplicated region for block: B:1422:0x0553  */
    /* JADX WARN: Removed duplicated region for block: B:1426:0x04ff  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x19d3  */
    /* JADX WARN: Removed duplicated region for block: B:1455:0x0481  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x19e2  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x1a07  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x1bd1  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x1cc3  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x1d11 A[Catch: Exception -> 0x1de1, TryCatch #6 {Exception -> 0x1de1, blocks: (B:188:0x1cdc, B:190:0x1ce0, B:193:0x1cf8, B:195:0x1cfe, B:196:0x1d0d, B:198:0x1d11, B:200:0x1d25, B:202:0x1d2b, B:204:0x1d2f, B:207:0x1d3c, B:209:0x1d39, B:212:0x1d3f, B:214:0x1d43, B:217:0x1d48, B:219:0x1d4c, B:221:0x1d5e, B:222:0x1d70, B:223:0x1dbb, B:359:0x1d88, B:362:0x1d8e, B:363:0x1d95, B:366:0x1dab, B:369:0x1ce4, B:371:0x1ce8, B:373:0x1ced), top: B:187:0x1cdc }] */
    /* JADX WARN: Removed duplicated region for block: B:219:0x1d4c A[Catch: Exception -> 0x1de1, TryCatch #6 {Exception -> 0x1de1, blocks: (B:188:0x1cdc, B:190:0x1ce0, B:193:0x1cf8, B:195:0x1cfe, B:196:0x1d0d, B:198:0x1d11, B:200:0x1d25, B:202:0x1d2b, B:204:0x1d2f, B:207:0x1d3c, B:209:0x1d39, B:212:0x1d3f, B:214:0x1d43, B:217:0x1d48, B:219:0x1d4c, B:221:0x1d5e, B:222:0x1d70, B:223:0x1dbb, B:359:0x1d88, B:362:0x1d8e, B:363:0x1d95, B:366:0x1dab, B:369:0x1ce4, B:371:0x1ce8, B:373:0x1ced), top: B:187:0x1cdc }] */
    /* JADX WARN: Removed duplicated region for block: B:226:0x1dec  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x2016  */
    /* JADX WARN: Removed duplicated region for block: B:289:0x2025 A[Catch: Exception -> 0x2064, TryCatch #4 {Exception -> 0x2064, blocks: (B:287:0x201a, B:289:0x2025, B:290:0x2027, B:292:0x2040, B:294:0x2048, B:296:0x204c), top: B:286:0x201a }] */
    /* JADX WARN: Removed duplicated region for block: B:292:0x2040 A[Catch: Exception -> 0x2064, TryCatch #4 {Exception -> 0x2064, blocks: (B:287:0x201a, B:289:0x2025, B:290:0x2027, B:292:0x2040, B:294:0x2048, B:296:0x204c), top: B:286:0x201a }] */
    /* JADX WARN: Removed duplicated region for block: B:296:0x204c A[Catch: Exception -> 0x2064, TRY_LEAVE, TryCatch #4 {Exception -> 0x2064, blocks: (B:287:0x201a, B:289:0x2025, B:290:0x2027, B:292:0x2040, B:294:0x2048, B:296:0x204c), top: B:286:0x201a }] */
    /* JADX WARN: Removed duplicated region for block: B:304:0x206c  */
    /* JADX WARN: Removed duplicated region for block: B:319:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:320:0x1f68  */
    /* JADX WARN: Removed duplicated region for block: B:365:0x1da6  */
    /* JADX WARN: Removed duplicated region for block: B:367:0x1da9  */
    /* JADX WARN: Removed duplicated region for block: B:398:0x1c7c A[LOOP:8: B:396:0x1c77->B:398:0x1c7c, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:399:0x1cbf A[EDGE_INSN: B:399:0x1cbf->B:183:0x1cbf ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:404:0x1a35  */
    /* JADX WARN: Removed duplicated region for block: B:478:0x1952  */
    /* JADX WARN: Removed duplicated region for block: B:482:0x19a7 A[LOOP:9: B:480:0x19a2->B:482:0x19a7, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:483:0x19cb A[EDGE_INSN: B:483:0x19cb->B:484:0x19cb ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:485:0x1970  */
    /* JADX WARN: Removed duplicated region for block: B:488:0x17ce A[Catch: Exception -> 0x180e, TryCatch #1 {Exception -> 0x180e, blocks: (B:90:0x171a, B:93:0x1724, B:95:0x1730, B:98:0x174a, B:100:0x1753, B:103:0x1769, B:105:0x176f, B:106:0x177b, B:108:0x1792, B:110:0x1798, B:113:0x17a9, B:115:0x17ad, B:116:0x17eb, B:118:0x17ef, B:120:0x17f8, B:121:0x1802, B:488:0x17ce), top: B:89:0x171a }] */
    /* JADX WARN: Removed duplicated region for block: B:490:0x1768  */
    /* JADX WARN: Removed duplicated region for block: B:504:0x16c0  */
    /* JADX WARN: Removed duplicated region for block: B:507:0x16da  */
    /* JADX WARN: Removed duplicated region for block: B:522:0x15f0  */
    /* JADX WARN: Removed duplicated region for block: B:534:0x158c  */
    /* JADX WARN: Removed duplicated region for block: B:536:0x1575  */
    /* JADX WARN: Removed duplicated region for block: B:538:0x1534  */
    /* JADX WARN: Removed duplicated region for block: B:601:0x04d3  */
    /* JADX WARN: Removed duplicated region for block: B:606:0x04df  */
    /* JADX WARN: Removed duplicated region for block: B:613:0x051a  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x14f5  */
    /* JADX WARN: Removed duplicated region for block: B:620:0x0560  */
    /* JADX WARN: Removed duplicated region for block: B:631:0x12be  */
    /* JADX WARN: Removed duplicated region for block: B:634:0x12e3  */
    /* JADX WARN: Removed duplicated region for block: B:637:0x1427  */
    /* JADX WARN: Removed duplicated region for block: B:652:0x147e  */
    /* JADX WARN: Removed duplicated region for block: B:654:0x1491  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x1541  */
    /* JADX WARN: Removed duplicated region for block: B:680:0x12f7  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x157a  */
    /* JADX WARN: Removed duplicated region for block: B:750:0x12c6  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x15ab  */
    /* JADX WARN: Removed duplicated region for block: B:760:0x05e9  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x15c3  */
    /* JADX WARN: Removed duplicated region for block: B:849:0x0835  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x1723  */
    /* JADX WARN: Removed duplicated region for block: B:937:0x12b0  */
    /* JADX WARN: Removed duplicated region for block: B:939:0x097c  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x1730 A[Catch: Exception -> 0x180e, TryCatch #1 {Exception -> 0x180e, blocks: (B:90:0x171a, B:93:0x1724, B:95:0x1730, B:98:0x174a, B:100:0x1753, B:103:0x1769, B:105:0x176f, B:106:0x177b, B:108:0x1792, B:110:0x1798, B:113:0x17a9, B:115:0x17ad, B:116:0x17eb, B:118:0x17ef, B:120:0x17f8, B:121:0x1802, B:488:0x17ce), top: B:89:0x171a }] */
    /* JADX WARN: Type inference failed for: r0v38, types: [android.text.SpannableStringBuilder, java.lang.CharSequence, android.text.Spannable] */
    /* JADX WARN: Type inference failed for: r2v188, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r5v45, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r7v67, types: [android.text.SpannableStringBuilder] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void buildLayout() {
        int i;
        String str;
        boolean z;
        int i2;
        boolean z2;
        int i3;
        TLRPC$DraftMessage tLRPC$DraftMessage;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$Chat chat;
        TLRPC$User tLRPC$User;
        String str2;
        SpannableStringBuilder spannableStringBuilder;
        boolean z3;
        boolean z4;
        String str3;
        String str4;
        CharSequence replaceNewLines;
        CharSequence charSequence;
        CharSequence highlightText;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        SpannableStringBuilder valueOf;
        TLRPC$Message tLRPC$Message;
        String charSequence2;
        SpannableStringBuilder formatSpannable;
        CharSequence charSequence3;
        String str11;
        int i4;
        int i5;
        CharSequence replaceEmoji;
        boolean z5;
        boolean z6;
        CharSequence highlightText2;
        String str12;
        CharSequence charSequence4;
        String string;
        String str13;
        String str14;
        CharSequence charSequence5;
        int i6;
        boolean z7;
        CharSequence charSequence6;
        CharSequence charSequence7;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        ArrayList<TLRPC$MessagePeerReaction> arrayList;
        String str15;
        String str16;
        String str17;
        String str18;
        String str19;
        String str20;
        boolean z8;
        ArrayList<TLRPC$MessageEntity> arrayList2;
        String str21;
        String str22;
        SpannableStringBuilder spannableStringBuilder2;
        String str23;
        SpannableStringBuilder spannableStringBuilder3;
        TLRPC$DraftMessage tLRPC$DraftMessage2;
        MessageObject messageObject;
        String stringForMessageListDate;
        MessageObject messageObject2;
        boolean z9;
        boolean z10;
        String str24;
        String str25;
        String str26;
        boolean z11;
        String str27;
        String str28;
        String str29;
        boolean z12;
        String str30;
        String str31;
        MessageObject messageObject3;
        CharSequence charSequence8;
        int i7;
        int i8;
        String str32;
        int i9;
        int dp;
        int dp2;
        int dp3;
        int i10;
        int i11;
        int i12;
        ImageReceiver[] imageReceiverArr;
        int i13;
        int max;
        boolean z13;
        int i14;
        ImageReceiver[] imageReceiverArr2;
        MessageObject messageObject4;
        CharSequence highlightText3;
        int lineCount;
        StaticLayout staticLayout;
        StaticLayout staticLayout2;
        float primaryHorizontal;
        float primaryHorizontal2;
        int i15;
        int length;
        int ceil;
        int i16;
        int lineCount2;
        boolean z14;
        int i17;
        CharacterStyle[] characterStyleArr;
        CharSequence replaceTwoNewLinesToOne;
        CharSequence highlightText4;
        int dp4;
        int dp5;
        int dp6;
        CharSequence highlightText5;
        CharSequence charSequence9;
        String str33;
        boolean z15;
        String str34;
        SpannableStringBuilder valueOf2;
        int i18 = 0;
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
        CharSequence charSequence10 = messageObject5 != null ? messageObject5.messageText : null;
        boolean z17 = charSequence10 instanceof Spannable;
        SpannableStringBuilder spannableStringBuilder4 = charSequence10;
        if (z17) {
            SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder(charSequence10);
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
                str33 = LocaleController.getString("FromYou", R.string.FromYou);
                CustomDialog customDialog3 = this.customDialog;
                if (customDialog3.isMedia) {
                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                    valueOf2 = SpannableStringBuilder.valueOf(String.format(str, this.message.messageText));
                    valueOf2.setSpan(new ForegroundColorSpanThemable("chats_attachMessage", this.resourcesProvider), 0, valueOf2.length(), 33);
                } else {
                    String str35 = customDialog3.message;
                    if (str35.length() > 150) {
                        str35 = str35.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                    }
                    valueOf2 = (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? SpannableStringBuilder.valueOf(String.format(str, str35, str33)) : SpannableStringBuilder.valueOf(String.format(str, str35.replace('\n', ' '), str33));
                }
                charSequence9 = Emoji.replaceEmoji(valueOf2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                z15 = false;
            } else {
                charSequence9 = customDialog2.message;
                if (customDialog2.isMedia) {
                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                }
                str33 = null;
                z15 = true;
            }
            str27 = LocaleController.stringForMessageListDate(this.customDialog.date);
            int i19 = this.customDialog.unread_count;
            if (i19 != 0) {
                this.drawCount = true;
                str34 = String.format("%d", Integer.valueOf(i19));
            } else {
                this.drawCount = false;
                str34 = null;
            }
            CustomDialog customDialog4 = this.customDialog;
            int i20 = customDialog4.sent;
            if (i20 == 0) {
                this.drawClock = true;
                this.drawCheck1 = false;
                this.drawCheck2 = false;
            } else if (i20 == 2) {
                this.drawCheck1 = true;
                this.drawCheck2 = true;
                this.drawClock = false;
            } else if (i20 == 1) {
                this.drawCheck1 = false;
                this.drawCheck2 = true;
                this.drawClock = false;
            } else {
                this.drawClock = false;
                this.drawCheck1 = false;
                this.drawCheck2 = false;
            }
            this.drawError = false;
            str30 = customDialog4.name;
            z6 = z15;
            i2 = i;
            str23 = "";
            str26 = str34;
            str31 = null;
            i6 = -1;
            charSequence8 = charSequence9;
            str29 = str33;
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
                            long j = UserConfig.getInstance(this.currentAccount).clientUserId;
                            i2 = i;
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
                                i3 = this.lastMessageDate;
                                if (i3 == 0 && (messageObject3 = this.message) != null) {
                                    i3 = messageObject3.messageOwner.date;
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
                                if (tLRPC$DraftMessage != null || ((!TextUtils.isEmpty(tLRPC$DraftMessage.message) || this.draftMessage.reply_to_msg_id != 0) && (i3 <= this.draftMessage.date || this.unreadCount == 0))) {
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
                                        SpannableStringBuilder spannableStringBuilder6 = new SpannableStringBuilder();
                                        CharSequence replace = TextUtils.replace(printingString, new String[]{"..."}, new String[]{""});
                                        i6 = this.printingStringType == 5 ? replace.toString().indexOf("**oo**") : -1;
                                        if (i6 >= 0) {
                                            spannableStringBuilder6.append(replace).setSpan(new FixedWidthSpan(Theme.getChatStatusDrawable(this.printingStringType).getIntrinsicWidth()), i6, i6 + 6, 0);
                                        } else {
                                            spannableStringBuilder6.append((CharSequence) " ").append(replace).setSpan(new FixedWidthSpan(intrinsicWidth), 0, 1, 0);
                                        }
                                        textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                        str23 = "";
                                        charSequence6 = null;
                                        z7 = true;
                                        z6 = false;
                                        i18 = 0;
                                        spannableStringBuilder3 = spannableStringBuilder6;
                                    } else {
                                        this.lastPrintString = null;
                                        if (this.draftMessage != null) {
                                            charSequence6 = LocaleController.getString("Draft", R.string.Draft);
                                            if (TextUtils.isEmpty(this.draftMessage.message)) {
                                                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                                    String str36 = "";
                                                    str21 = str36;
                                                    str22 = str36;
                                                    z7 = true;
                                                    z6 = false;
                                                    str20 = str22;
                                                    str15 = str21;
                                                } else {
                                                    SpannableStringBuilder valueOf3 = SpannableStringBuilder.valueOf(charSequence6);
                                                    valueOf3.setSpan(new ForegroundColorSpanThemable("chats_draft", this.resourcesProvider), 0, charSequence6.length(), 33);
                                                    spannableStringBuilder2 = valueOf3;
                                                }
                                            } else {
                                                String str37 = this.draftMessage.message;
                                                if (str37.length() > 150) {
                                                    str37 = str37.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                }
                                                SpannableStringBuilder spannableStringBuilder7 = new SpannableStringBuilder(str37);
                                                MediaDataController.addTextStyleRuns(this.draftMessage, spannableStringBuilder7, 256);
                                                TLRPC$DraftMessage tLRPC$DraftMessage3 = this.draftMessage;
                                                if (tLRPC$DraftMessage3 != null && (arrayList2 = tLRPC$DraftMessage3.entities) != null) {
                                                    MediaDataController.addAnimatedEmojiSpans(arrayList2, spannableStringBuilder7, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                }
                                                SpannableStringBuilder formatSpannable2 = AndroidUtilities.formatSpannable(str, AndroidUtilities.replaceNewLines(spannableStringBuilder7), charSequence6);
                                                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                                    z8 = false;
                                                } else {
                                                    z8 = false;
                                                    formatSpannable2.setSpan(new ForegroundColorSpanThemable("chats_draft", this.resourcesProvider), 0, charSequence6.length() + 1, 33);
                                                }
                                                spannableStringBuilder2 = Emoji.replaceEmoji(formatSpannable2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), z8);
                                            }
                                            str21 = "";
                                            str22 = spannableStringBuilder2;
                                            z7 = true;
                                            z6 = false;
                                            str20 = str22;
                                            str15 = str21;
                                        } else {
                                            if (this.clearingDialog) {
                                                textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                str18 = LocaleController.getString("HistoryCleared", R.string.HistoryCleared);
                                            } else {
                                                MessageObject messageObject6 = this.message;
                                                if (messageObject6 == null) {
                                                    TLRPC$EncryptedChat tLRPC$EncryptedChat = this.encryptedChat;
                                                    if (tLRPC$EncryptedChat != null) {
                                                        textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                        if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatRequested) {
                                                            str18 = LocaleController.getString("EncryptionProcessing", R.string.EncryptionProcessing);
                                                        } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatWaiting) {
                                                            str18 = LocaleController.formatString("AwaitingEncryption", R.string.AwaitingEncryption, UserObject.getFirstName(this.user));
                                                        } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatDiscarded) {
                                                            str18 = LocaleController.getString("EncryptionRejected", R.string.EncryptionRejected);
                                                        } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat) {
                                                            if (tLRPC$EncryptedChat.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                                str18 = LocaleController.formatString("EncryptedChatStartedOutgoing", R.string.EncryptedChatStartedOutgoing, UserObject.getFirstName(this.user));
                                                            } else {
                                                                str18 = LocaleController.getString("EncryptedChatStartedIncoming", R.string.EncryptedChatStartedIncoming);
                                                            }
                                                        }
                                                    } else if (this.dialogsType == 3 && UserObject.isUserSelf(this.user)) {
                                                        str15 = "";
                                                        charSequence6 = null;
                                                        z7 = false;
                                                        z6 = true;
                                                        i18 = 0;
                                                        z16 = false;
                                                        str19 = LocaleController.getString("SavedMessagesInfo", R.string.SavedMessagesInfo);
                                                        i6 = -1;
                                                        spannableStringBuilder3 = str19;
                                                        str23 = str15;
                                                    }
                                                    String str38 = "";
                                                    str16 = str38;
                                                    str17 = str38;
                                                    charSequence6 = null;
                                                    z7 = true;
                                                    z6 = true;
                                                    str20 = str17;
                                                    str15 = str16;
                                                } else {
                                                    String restrictionReason = MessagesController.getRestrictionReason(messageObject6.messageOwner.restriction_reason);
                                                    long fromChatId = this.message.getFromChatId();
                                                    if (DialogObject.isUserDialog(fromChatId)) {
                                                        tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(fromChatId));
                                                        chat = null;
                                                    } else {
                                                        chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-fromChatId));
                                                        tLRPC$User = null;
                                                    }
                                                    this.drawCount2 = true;
                                                    TLRPC$Chat tLRPC$Chat4 = chat;
                                                    if (this.dialogsType == 0 && this.currentDialogId > 0 && this.message.isOutOwner() && (tLRPC$TL_messageReactions = this.message.messageOwner.reactions) != null && (arrayList = tLRPC$TL_messageReactions.recent_reactions) != null && !arrayList.isEmpty() && this.reactionMentionCount > 0) {
                                                        TLRPC$MessagePeerReaction tLRPC$MessagePeerReaction = this.message.messageOwner.reactions.recent_reactions.get(0);
                                                        if (tLRPC$MessagePeerReaction.unread) {
                                                            str2 = "";
                                                            long j3 = tLRPC$MessagePeerReaction.peer_id.user_id;
                                                            if (j3 != 0 && j3 != UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                                ReactionsLayoutInBubble.VisibleReaction fromTLReaction = ReactionsLayoutInBubble.VisibleReaction.fromTLReaction(tLRPC$MessagePeerReaction.reaction);
                                                                textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                String str39 = fromTLReaction.emojicon;
                                                                if (str39 != null) {
                                                                    z3 = true;
                                                                    spannableStringBuilder = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, str39);
                                                                } else {
                                                                    String formatString = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, "**reaction**");
                                                                    int indexOf = formatString.indexOf("**reaction**");
                                                                    ?? spannableStringBuilder8 = new SpannableStringBuilder(formatString.replace("**reaction**", "d"));
                                                                    spannableStringBuilder8.setSpan(new AnimatedEmojiSpan(fromTLReaction.documentId, textPaint5 == null ? null : textPaint5.getFontMetricsInt()), indexOf, indexOf + 1, 0);
                                                                    spannableStringBuilder = spannableStringBuilder8;
                                                                    z3 = true;
                                                                }
                                                                if (z3) {
                                                                    String str40 = spannableStringBuilder;
                                                                    if (this.groupMessages != null) {
                                                                        str40 = spannableStringBuilder;
                                                                        if (TextUtils.isEmpty(restrictionReason)) {
                                                                            str40 = spannableStringBuilder;
                                                                            if (this.currentDialogFolderId == 0) {
                                                                                str40 = spannableStringBuilder;
                                                                                if (this.encryptedChat == null) {
                                                                                    this.thumbsCount = 0;
                                                                                    this.hasVideoThumb = false;
                                                                                    Collections.sort(this.groupMessages, DialogCell$$ExternalSyntheticLambda3.INSTANCE);
                                                                                    for (int i21 = 0; i21 < this.groupMessages.size(); i21++) {
                                                                                        MessageObject messageObject7 = this.groupMessages.get(i21);
                                                                                        if (messageObject7 != null && !messageObject7.needDrawBluredPreview() && (messageObject7.isPhoto() || messageObject7.isNewGif() || messageObject7.isVideo() || messageObject7.isRoundVideo())) {
                                                                                            String str41 = messageObject7.isWebpage() ? messageObject7.messageOwner.media.webpage.type : null;
                                                                                            if (!"app".equals(str41) && !"profile".equals(str41) && !"article".equals(str41) && (str41 == null || !str41.startsWith("telegram_"))) {
                                                                                                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject7.photoThumbs, 40);
                                                                                                TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageObject7.photoThumbs, AndroidUtilities.getPhotoSize());
                                                                                                if (closestPhotoSizeWithSize == closestPhotoSizeWithSize2) {
                                                                                                    closestPhotoSizeWithSize2 = null;
                                                                                                }
                                                                                                if (closestPhotoSizeWithSize != null) {
                                                                                                    this.hasVideoThumb = this.hasVideoThumb || messageObject7.isVideo() || messageObject7.isRoundVideo();
                                                                                                    int i22 = this.thumbsCount;
                                                                                                    if (i22 < 3) {
                                                                                                        this.thumbsCount = i22 + 1;
                                                                                                        this.drawPlay[i21] = messageObject7.isVideo() || messageObject7.isRoundVideo();
                                                                                                        this.thumbImage[i21].setImage(ImageLocation.getForObject(closestPhotoSizeWithSize2, messageObject7.photoThumbsObject), "20_20", ImageLocation.getForObject(closestPhotoSizeWithSize, messageObject7.photoThumbsObject), "20_20", (messageObject7.type != 1 || closestPhotoSizeWithSize2 == null) ? 0 : closestPhotoSizeWithSize2.size, null, messageObject7, 0);
                                                                                                        this.thumbImage[i21].setRoundRadius(messageObject7.isRoundVideo() ? AndroidUtilities.dp(18.0f) : AndroidUtilities.dp(2.0f));
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    boolean z18 = spannableStringBuilder instanceof SpannableStringBuilder;
                                                                                    SpannableStringBuilder spannableStringBuilder9 = spannableStringBuilder;
                                                                                    if (!z18) {
                                                                                        spannableStringBuilder9 = new SpannableStringBuilder(spannableStringBuilder);
                                                                                    }
                                                                                    SpannableStringBuilder spannableStringBuilder10 = spannableStringBuilder9;
                                                                                    spannableStringBuilder10.insert(0, (CharSequence) " ");
                                                                                    spannableStringBuilder10.setSpan(new FixedWidthSpan(AndroidUtilities.dp((((i2 + 2) * this.thumbsCount) - 2) + 5)), 0, 1, 33);
                                                                                    str40 = spannableStringBuilder10;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    str12 = str40;
                                                                } else {
                                                                    int i23 = this.dialogsType;
                                                                    if (i23 == 2) {
                                                                        TLRPC$Chat tLRPC$Chat5 = this.chat;
                                                                        if (tLRPC$Chat5 != null) {
                                                                            if (ChatObject.isChannel(tLRPC$Chat5)) {
                                                                                TLRPC$Chat tLRPC$Chat6 = this.chat;
                                                                                if (!tLRPC$Chat6.megagroup) {
                                                                                    int i24 = tLRPC$Chat6.participants_count;
                                                                                    if (i24 != 0) {
                                                                                        string = LocaleController.formatPluralStringComma("Subscribers", i24);
                                                                                    } else if (!ChatObject.isPublic(tLRPC$Chat6)) {
                                                                                        string = LocaleController.getString("ChannelPrivate", R.string.ChannelPrivate).toLowerCase();
                                                                                    } else {
                                                                                        string = LocaleController.getString("ChannelPublic", R.string.ChannelPublic).toLowerCase();
                                                                                    }
                                                                                }
                                                                            }
                                                                            TLRPC$Chat tLRPC$Chat7 = this.chat;
                                                                            int i25 = tLRPC$Chat7.participants_count;
                                                                            if (i25 != 0) {
                                                                                string = LocaleController.formatPluralStringComma("Members", i25);
                                                                            } else if (tLRPC$Chat7.has_geo) {
                                                                                string = LocaleController.getString("MegaLocation", R.string.MegaLocation);
                                                                            } else if (!ChatObject.isPublic(tLRPC$Chat7)) {
                                                                                string = LocaleController.getString("MegaPrivate", R.string.MegaPrivate).toLowerCase();
                                                                            } else {
                                                                                string = LocaleController.getString("MegaPublic", R.string.MegaPublic).toLowerCase();
                                                                            }
                                                                        } else {
                                                                            string = str2;
                                                                        }
                                                                        this.drawCount2 = false;
                                                                    } else if (i23 == 3 && UserObject.isUserSelf(this.user)) {
                                                                        string = LocaleController.getString("SavedMessagesInfo", R.string.SavedMessagesInfo);
                                                                    } else {
                                                                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout && this.currentDialogFolderId != 0) {
                                                                            charSequence = formatArchivedDialogNames();
                                                                            str3 = str2;
                                                                        } else if (this.message.messageOwner instanceof TLRPC$TL_messageService) {
                                                                            String str42 = spannableStringBuilder4;
                                                                            if (ChatObject.isChannelAndNotMegaGroup(this.chat)) {
                                                                                str42 = spannableStringBuilder4;
                                                                                if (this.message.messageOwner.action instanceof TLRPC$TL_messageActionChannelMigrateFrom) {
                                                                                    str42 = str2;
                                                                                    z16 = false;
                                                                                }
                                                                            }
                                                                            textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                            str12 = str42;
                                                                        } else {
                                                                            if (this.groupMessages == null || !TextUtils.isEmpty(restrictionReason) || this.currentDialogFolderId != 0 || this.encryptedChat != null) {
                                                                                z4 = true;
                                                                            } else {
                                                                                this.thumbsCount = 0;
                                                                                this.hasVideoThumb = false;
                                                                                Collections.sort(this.groupMessages, DialogCell$$ExternalSyntheticLambda4.INSTANCE);
                                                                                z4 = true;
                                                                                for (int i26 = 0; i26 < this.groupMessages.size(); i26++) {
                                                                                    MessageObject messageObject8 = this.groupMessages.get(i26);
                                                                                    if (messageObject8 != null && !messageObject8.needDrawBluredPreview() && (messageObject8.isPhoto() || messageObject8.isNewGif() || messageObject8.isVideo() || messageObject8.isRoundVideo())) {
                                                                                        String str43 = messageObject8.isWebpage() ? messageObject8.messageOwner.media.webpage.type : null;
                                                                                        if (!"app".equals(str43) && !"profile".equals(str43) && !"article".equals(str43) && (str43 == null || !str43.startsWith("telegram_"))) {
                                                                                            TLRPC$PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(messageObject8.photoThumbs, 40);
                                                                                            TLRPC$PhotoSize closestPhotoSizeWithSize4 = FileLoader.getClosestPhotoSizeWithSize(messageObject8.photoThumbs, AndroidUtilities.getPhotoSize());
                                                                                            if (closestPhotoSizeWithSize3 == closestPhotoSizeWithSize4) {
                                                                                                closestPhotoSizeWithSize4 = null;
                                                                                            }
                                                                                            if (closestPhotoSizeWithSize3 != null) {
                                                                                                this.hasVideoThumb = this.hasVideoThumb || messageObject8.isVideo() || messageObject8.isRoundVideo();
                                                                                                int i27 = this.thumbsCount;
                                                                                                if (i27 < 3) {
                                                                                                    this.thumbsCount = i27 + 1;
                                                                                                    this.drawPlay[i26] = messageObject8.isVideo() || messageObject8.isRoundVideo();
                                                                                                    this.thumbImage[i26].setImage(ImageLocation.getForObject(closestPhotoSizeWithSize4, messageObject8.photoThumbsObject), "20_20", ImageLocation.getForObject(closestPhotoSizeWithSize3, messageObject8.photoThumbsObject), "20_20", (messageObject8.type != 1 || closestPhotoSizeWithSize4 == null) ? 0 : closestPhotoSizeWithSize4.size, null, messageObject8, 0);
                                                                                                    this.thumbImage[i26].setRoundRadius(messageObject8.isRoundVideo() ? AndroidUtilities.dp(18.0f) : AndroidUtilities.dp(2.0f));
                                                                                                    z4 = false;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            TLRPC$Chat tLRPC$Chat8 = this.chat;
                                                                            if (tLRPC$Chat8 != null && tLRPC$Chat8.id > 0 && tLRPC$Chat4 == null && (!ChatObject.isChannel(tLRPC$Chat8) || ChatObject.isMegagroup(this.chat))) {
                                                                                if (this.message.isOutOwner()) {
                                                                                    str10 = LocaleController.getString("FromYou", R.string.FromYou);
                                                                                } else {
                                                                                    MessageObject messageObject9 = this.message;
                                                                                    if (messageObject9 == null || (tLRPC$MessageFwdHeader = messageObject9.messageOwner.fwd_from) == null || (str10 = tLRPC$MessageFwdHeader.from_name) == null) {
                                                                                        if (tLRPC$User == null) {
                                                                                            str9 = str2;
                                                                                            str10 = "DELETED";
                                                                                        } else if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                                                                            str9 = str2;
                                                                                            if (UserObject.isDeleted(tLRPC$User)) {
                                                                                                str10 = LocaleController.getString("HiddenName", R.string.HiddenName);
                                                                                            } else {
                                                                                                str10 = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name).replace("\n", str9);
                                                                                            }
                                                                                        } else {
                                                                                            str9 = str2;
                                                                                            str10 = UserObject.getFirstName(tLRPC$User).replace("\n", str9);
                                                                                        }
                                                                                        String str44 = str10;
                                                                                        if (this.chat.forum) {
                                                                                            str44 = str10;
                                                                                            if (!this.isTopic) {
                                                                                                CharSequence topicIconName = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopicIconName(this.chat, this.message, textPaint5);
                                                                                                str44 = str10;
                                                                                                if (!TextUtils.isEmpty(topicIconName)) {
                                                                                                    SpannableStringBuilder spannableStringBuilder11 = new SpannableStringBuilder("-");
                                                                                                    ColoredImageSpan coloredImageSpan = new ColoredImageSpan(ContextCompat.getDrawable(ApplicationLoader.applicationContext, R.drawable.msg_mini_forumarrow).mutate());
                                                                                                    coloredImageSpan.setColorKey((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? null : "chats_nameMessage");
                                                                                                    spannableStringBuilder11.setSpan(coloredImageSpan, 0, 1, 0);
                                                                                                    ?? spannableStringBuilder12 = new SpannableStringBuilder();
                                                                                                    spannableStringBuilder12.append(str10).append((CharSequence) spannableStringBuilder11).append(topicIconName);
                                                                                                    str44 = spannableStringBuilder12;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        MessageObject captionMessage = getCaptionMessage();
                                                                                        if (TextUtils.isEmpty(restrictionReason)) {
                                                                                            valueOf = SpannableStringBuilder.valueOf(String.format(str, restrictionReason, str44));
                                                                                        } else if (captionMessage != null && (charSequence3 = captionMessage.caption) != null) {
                                                                                            CharSequence charSequence11 = charSequence3.toString();
                                                                                            if (!z4) {
                                                                                                str11 = str9;
                                                                                            } else if (captionMessage.isVideo()) {
                                                                                                str11 = "📹 ";
                                                                                            } else if (captionMessage.isVoice()) {
                                                                                                str11 = "🎤 ";
                                                                                            } else if (captionMessage.isMusic()) {
                                                                                                str11 = "🎧 ";
                                                                                            } else {
                                                                                                str11 = captionMessage.isPhoto() ? "🖼 " : "📎 ";
                                                                                            }
                                                                                            if (captionMessage.hasHighlightedWords() && !TextUtils.isEmpty(captionMessage.messageOwner.message)) {
                                                                                                String str45 = captionMessage.messageTrimmedToHighlight;
                                                                                                int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) + 24);
                                                                                                if (z) {
                                                                                                    if (!TextUtils.isEmpty(str44)) {
                                                                                                        measuredWidth = (int) (measuredWidth - textPaint5.measureText(str44.toString()));
                                                                                                    }
                                                                                                    measuredWidth = (int) (measuredWidth - textPaint5.measureText(": "));
                                                                                                }
                                                                                                if (measuredWidth > 0) {
                                                                                                    str45 = AndroidUtilities.ellipsizeCenterEnd(str45, captionMessage.highlightedWords.get(0), measuredWidth, textPaint5, 130).toString();
                                                                                                }
                                                                                                valueOf = new SpannableStringBuilder(str11).append((CharSequence) str45);
                                                                                            } else {
                                                                                                if (charSequence11.length() > 150) {
                                                                                                    charSequence11 = charSequence11.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                                }
                                                                                                SpannableStringBuilder spannableStringBuilder13 = new SpannableStringBuilder(charSequence11);
                                                                                                MediaDataController.addTextStyleRuns(captionMessage.messageOwner.entities, charSequence11, spannableStringBuilder13, 256);
                                                                                                TLRPC$Message tLRPC$Message2 = captionMessage.messageOwner;
                                                                                                if (tLRPC$Message2 != null) {
                                                                                                    MediaDataController.addAnimatedEmojiSpans(tLRPC$Message2.entities, spannableStringBuilder13, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                                }
                                                                                                valueOf = AndroidUtilities.formatSpannable(str, new SpannableStringBuilder(str11).append(AndroidUtilities.replaceNewLines(spannableStringBuilder13)), str44);
                                                                                            }
                                                                                        } else {
                                                                                            MessageObject messageObject10 = this.message;
                                                                                            if (messageObject10.messageOwner.media != null && !messageObject10.isMediaEmpty()) {
                                                                                                textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                MessageObject messageObject11 = this.message;
                                                                                                TLRPC$MessageMedia tLRPC$MessageMedia = messageObject11.messageOwner.media;
                                                                                                String str46 = "chats_attachMessage";
                                                                                                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                                                                                                    TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia;
                                                                                                    charSequence2 = Build.VERSION.SDK_INT >= 18 ? String.format("📊 \u2068%s\u2069", tLRPC$TL_messageMediaPoll.poll.question) : String.format("📊 %s", tLRPC$TL_messageMediaPoll.poll.question);
                                                                                                } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                                                                                                    charSequence2 = Build.VERSION.SDK_INT >= 18 ? String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title) : String.format("🎮 %s", tLRPC$MessageMedia.game.title);
                                                                                                } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                                    charSequence2 = tLRPC$MessageMedia.title;
                                                                                                } else if (messageObject11.type == 14) {
                                                                                                    charSequence2 = Build.VERSION.SDK_INT >= 18 ? String.format("🎧 \u2068%s - %s\u2069", messageObject11.getMusicAuthor(), this.message.getMusicTitle()) : String.format("🎧 %s - %s", messageObject11.getMusicAuthor(), this.message.getMusicTitle());
                                                                                                } else if (this.thumbsCount > 1) {
                                                                                                    if (this.hasVideoThumb) {
                                                                                                        ArrayList<MessageObject> arrayList3 = this.groupMessages;
                                                                                                        charSequence2 = LocaleController.formatPluralString("Media", arrayList3 == null ? 0 : arrayList3.size(), new Object[0]);
                                                                                                    } else {
                                                                                                        ArrayList<MessageObject> arrayList4 = this.groupMessages;
                                                                                                        charSequence2 = LocaleController.formatPluralString("Photos", arrayList4 == null ? 0 : arrayList4.size(), new Object[0]);
                                                                                                    }
                                                                                                    str46 = "chats_actionMessage";
                                                                                                } else {
                                                                                                    charSequence2 = spannableStringBuilder4.toString();
                                                                                                    str46 = "chats_actionMessage";
                                                                                                }
                                                                                                formatSpannable = AndroidUtilities.formatSpannable(str, charSequence2.replace('\n', ' '), str44);
                                                                                                try {
                                                                                                    formatSpannable.setSpan(new ForegroundColorSpanThemable(str46, this.resourcesProvider), z ? str44.length() + 2 : 0, formatSpannable.length(), 33);
                                                                                                } catch (Exception e) {
                                                                                                    FileLog.e(e);
                                                                                                }
                                                                                                if ((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && (this.currentDialogFolderId == 0 || formatSpannable.length() <= 0)) {
                                                                                                    i4 = 0;
                                                                                                    i5 = 0;
                                                                                                } else {
                                                                                                    try {
                                                                                                        ForegroundColorSpanThemable foregroundColorSpanThemable = new ForegroundColorSpanThemable("chats_nameMessage", this.resourcesProvider);
                                                                                                        i5 = str44.length() + 1;
                                                                                                        try {
                                                                                                            formatSpannable.setSpan(foregroundColorSpanThemable, 0, i5, 33);
                                                                                                            i4 = i5;
                                                                                                        } catch (Exception e2) {
                                                                                                            e = e2;
                                                                                                            FileLog.e(e);
                                                                                                            i4 = 0;
                                                                                                            replaceEmoji = Emoji.replaceEmoji(formatSpannable, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                            if (this.message.hasHighlightedWords()) {
                                                                                                            }
                                                                                                            CharSequence charSequence12 = replaceEmoji;
                                                                                                            if (this.thumbsCount > 0) {
                                                                                                            }
                                                                                                            i18 = i4;
                                                                                                            z5 = true;
                                                                                                            z6 = false;
                                                                                                            charSequence5 = str44;
                                                                                                            str13 = charSequence12;
                                                                                                            str14 = str9;
                                                                                                            if (this.currentDialogFolderId != 0) {
                                                                                                            }
                                                                                                            i6 = -1;
                                                                                                            CharSequence charSequence13 = charSequence5;
                                                                                                            z7 = z5;
                                                                                                            charSequence6 = charSequence13;
                                                                                                            spannableStringBuilder3 = str13;
                                                                                                            str23 = str14;
                                                                                                            if (this.draftMessage != null) {
                                                                                                            }
                                                                                                            messageObject2 = this.message;
                                                                                                            if (messageObject2 == null) {
                                                                                                            }
                                                                                                            this.promoDialog = z11;
                                                                                                            MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                                                                                                            String str47 = spannableStringBuilder3;
                                                                                                            if (this.dialogsType == 0) {
                                                                                                            }
                                                                                                            str27 = stringForMessageListDate;
                                                                                                            if (this.currentDialogFolderId != 0) {
                                                                                                            }
                                                                                                            boolean z19 = z7;
                                                                                                            str29 = charSequence6;
                                                                                                            z12 = z19;
                                                                                                            String str48 = str25;
                                                                                                            str30 = str28;
                                                                                                            str31 = str48;
                                                                                                            charSequence8 = str47;
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
                                                                                                            if (!this.drawVerified) {
                                                                                                            }
                                                                                                            dp6 = this.nameWidth - AndroidUtilities.dp(12.0f);
                                                                                                            if (dp6 < 0) {
                                                                                                            }
                                                                                                            CharSequence replace2 = str30.replace('\n', ' ');
                                                                                                            if (this.nameLayoutEllipsizeByGradient) {
                                                                                                            }
                                                                                                            float f = dp6;
                                                                                                            this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(replace2.toString()) <= f;
                                                                                                            if (!this.twoLinesForName) {
                                                                                                            }
                                                                                                            CharSequence replaceEmoji2 = Emoji.replaceEmoji(replace2, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                            MessageObject messageObject12 = this.message;
                                                                                                            if (messageObject12 != null) {
                                                                                                            }
                                                                                                            if (!this.twoLinesForName) {
                                                                                                            }
                                                                                                            this.nameLayoutTranslateX = (this.nameLayoutEllipsizeByGradient || !this.nameLayout.isRtlCharAt(0)) ? 0.0f : -AndroidUtilities.dp(36.0f);
                                                                                                            this.nameLayoutEllipsizeLeft = this.nameLayout.isRtlCharAt(0);
                                                                                                            if (!this.useForceThreeLines) {
                                                                                                            }
                                                                                                            str32 = str23;
                                                                                                            i9 = i2;
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
                                                                                                            i10 = measuredWidth2;
                                                                                                            i11 = i6;
                                                                                                            this.avatarImage.setImageCoords(dp2, dp, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                                                                                                            i12 = 0;
                                                                                                            while (true) {
                                                                                                                imageReceiverArr = this.thumbImage;
                                                                                                                if (i12 < imageReceiverArr.length) {
                                                                                                                }
                                                                                                                imageReceiverArr[i12].setImageCoords(((i9 + 2) * i12) + dp3, AndroidUtilities.dp(31.0f) + dp, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                                                                                                                i12++;
                                                                                                                dp = dp;
                                                                                                            }
                                                                                                            i13 = dp;
                                                                                                            int i28 = i10;
                                                                                                            if (this.twoLinesForName) {
                                                                                                            }
                                                                                                            if (getIsPinned()) {
                                                                                                            }
                                                                                                            if (!this.drawError) {
                                                                                                            }
                                                                                                            if (z6) {
                                                                                                            }
                                                                                                            max = Math.max(AndroidUtilities.dp(12.0f), i28);
                                                                                                            z13 = this.useForceThreeLines;
                                                                                                            if (!z13) {
                                                                                                            }
                                                                                                            try {
                                                                                                                messageObject4 = this.message;
                                                                                                                if (messageObject4 != null) {
                                                                                                                    str29 = highlightText3;
                                                                                                                }
                                                                                                                this.messageNameLayout = StaticLayoutEx.createStaticLayout(str29, Theme.dialogs_messageNamePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max, 1);
                                                                                                            } catch (Exception e3) {
                                                                                                                FileLog.e(e3);
                                                                                                            }
                                                                                                            this.messageTop = AndroidUtilities.dp(51.0f);
                                                                                                            i14 = 0;
                                                                                                            while (true) {
                                                                                                                imageReceiverArr2 = this.thumbImage;
                                                                                                                if (i14 < imageReceiverArr2.length) {
                                                                                                                }
                                                                                                                imageReceiverArr2[i14].setImageY(i13 + AndroidUtilities.dp(40.0f));
                                                                                                                i14++;
                                                                                                            }
                                                                                                            if (this.twoLinesForName) {
                                                                                                            }
                                                                                                            this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack2, this.messageNameLayout);
                                                                                                            z14 = this.useForceThreeLines;
                                                                                                            if (!z14) {
                                                                                                            }
                                                                                                            textPaint5 = Theme.dialogs_messagePaint[this.paintIndex];
                                                                                                            charSequence8 = str29;
                                                                                                            str29 = null;
                                                                                                            if (charSequence8 instanceof Spannable) {
                                                                                                            }
                                                                                                            if (!this.useForceThreeLines) {
                                                                                                                i17 = this.thumbsCount;
                                                                                                                if (i17 > 0) {
                                                                                                                }
                                                                                                                this.messageLayout = new StaticLayout(charSequence8, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
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
                                                                                                            this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence8, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, str29 == null ? 1 : 2);
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
                                                                                                    } catch (Exception e4) {
                                                                                                        e = e4;
                                                                                                        i5 = 0;
                                                                                                    }
                                                                                                }
                                                                                                replaceEmoji = Emoji.replaceEmoji(formatSpannable, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                if (this.message.hasHighlightedWords() && (highlightText2 = AndroidUtilities.highlightText(replaceEmoji, this.message.highlightedWords, this.resourcesProvider)) != null) {
                                                                                                    replaceEmoji = highlightText2;
                                                                                                }
                                                                                                CharSequence charSequence122 = replaceEmoji;
                                                                                                if (this.thumbsCount > 0) {
                                                                                                    boolean z20 = replaceEmoji instanceof SpannableStringBuilder;
                                                                                                    SpannableStringBuilder spannableStringBuilder14 = replaceEmoji;
                                                                                                    if (!z20) {
                                                                                                        spannableStringBuilder14 = new SpannableStringBuilder(replaceEmoji);
                                                                                                    }
                                                                                                    SpannableStringBuilder spannableStringBuilder15 = (SpannableStringBuilder) spannableStringBuilder14;
                                                                                                    if (i5 >= spannableStringBuilder15.length()) {
                                                                                                        spannableStringBuilder15.append((CharSequence) " ");
                                                                                                        spannableStringBuilder15.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbsCount * (i2 + 2)) - 2) + 5)), spannableStringBuilder15.length() - 1, spannableStringBuilder15.length(), 33);
                                                                                                        charSequence122 = spannableStringBuilder14;
                                                                                                    } else {
                                                                                                        spannableStringBuilder15.insert(i5, (CharSequence) " ");
                                                                                                        spannableStringBuilder15.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbsCount * (i2 + 2)) - 2) + 5)), i5, i5 + 1, 33);
                                                                                                        charSequence122 = spannableStringBuilder14;
                                                                                                    }
                                                                                                }
                                                                                                i18 = i4;
                                                                                                z5 = true;
                                                                                                z6 = false;
                                                                                                charSequence5 = str44;
                                                                                                str13 = charSequence122;
                                                                                                str14 = str9;
                                                                                                if (this.currentDialogFolderId != 0) {
                                                                                                }
                                                                                                i6 = -1;
                                                                                                CharSequence charSequence132 = charSequence5;
                                                                                                z7 = z5;
                                                                                                charSequence6 = charSequence132;
                                                                                                spannableStringBuilder3 = str13;
                                                                                                str23 = str14;
                                                                                            } else {
                                                                                                MessageObject messageObject13 = this.message;
                                                                                                CharSequence charSequence14 = messageObject13.messageOwner.message;
                                                                                                if (charSequence14 != null) {
                                                                                                    if (messageObject13.hasHighlightedWords()) {
                                                                                                        String str49 = this.message.messageTrimmedToHighlight;
                                                                                                        if (str49 != null) {
                                                                                                            charSequence14 = str49;
                                                                                                        }
                                                                                                        int measuredWidth3 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) + 10);
                                                                                                        if (z) {
                                                                                                            if (!TextUtils.isEmpty(str44)) {
                                                                                                                measuredWidth3 = (int) (measuredWidth3 - textPaint5.measureText(str44.toString()));
                                                                                                            }
                                                                                                            measuredWidth3 = (int) (measuredWidth3 - textPaint5.measureText(": "));
                                                                                                        }
                                                                                                        if (measuredWidth3 > 0) {
                                                                                                            charSequence14 = AndroidUtilities.ellipsizeCenterEnd(charSequence14, this.message.highlightedWords.get(0), measuredWidth3, textPaint5, 130).toString();
                                                                                                        }
                                                                                                    } else {
                                                                                                        if (charSequence14.length() > 150) {
                                                                                                            charSequence14 = charSequence14.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                                        }
                                                                                                        charSequence14 = AndroidUtilities.replaceNewLines(charSequence14);
                                                                                                    }
                                                                                                    SpannableStringBuilder spannableStringBuilder16 = new SpannableStringBuilder(charSequence14);
                                                                                                    MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder16, 256);
                                                                                                    MessageObject messageObject14 = this.message;
                                                                                                    if (messageObject14 != null && (tLRPC$Message = messageObject14.messageOwner) != null) {
                                                                                                        MediaDataController.addAnimatedEmojiSpans(tLRPC$Message.entities, spannableStringBuilder16, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                                    }
                                                                                                    valueOf = AndroidUtilities.formatSpannable(str, spannableStringBuilder16, str44);
                                                                                                } else {
                                                                                                    valueOf = SpannableStringBuilder.valueOf(str9);
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
                                                                                        CharSequence charSequence1222 = replaceEmoji;
                                                                                        if (this.thumbsCount > 0) {
                                                                                        }
                                                                                        i18 = i4;
                                                                                        z5 = true;
                                                                                        z6 = false;
                                                                                        charSequence5 = str44;
                                                                                        str13 = charSequence1222;
                                                                                        str14 = str9;
                                                                                        if (this.currentDialogFolderId != 0) {
                                                                                        }
                                                                                        i6 = -1;
                                                                                        CharSequence charSequence1322 = charSequence5;
                                                                                        z7 = z5;
                                                                                        charSequence6 = charSequence1322;
                                                                                        spannableStringBuilder3 = str13;
                                                                                        str23 = str14;
                                                                                    }
                                                                                }
                                                                                str9 = str2;
                                                                                String str442 = str10;
                                                                                if (this.chat.forum) {
                                                                                }
                                                                                MessageObject captionMessage2 = getCaptionMessage();
                                                                                if (TextUtils.isEmpty(restrictionReason)) {
                                                                                }
                                                                                formatSpannable = valueOf;
                                                                                if (!this.useForceThreeLines) {
                                                                                }
                                                                                i4 = 0;
                                                                                i5 = 0;
                                                                                replaceEmoji = Emoji.replaceEmoji(formatSpannable, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                if (this.message.hasHighlightedWords()) {
                                                                                }
                                                                                CharSequence charSequence12222 = replaceEmoji;
                                                                                if (this.thumbsCount > 0) {
                                                                                }
                                                                                i18 = i4;
                                                                                z5 = true;
                                                                                z6 = false;
                                                                                charSequence5 = str442;
                                                                                str13 = charSequence12222;
                                                                                str14 = str9;
                                                                                if (this.currentDialogFolderId != 0) {
                                                                                }
                                                                                i6 = -1;
                                                                                CharSequence charSequence13222 = charSequence5;
                                                                                z7 = z5;
                                                                                charSequence6 = charSequence13222;
                                                                                spannableStringBuilder3 = str13;
                                                                                str23 = str14;
                                                                            } else {
                                                                                str3 = str2;
                                                                                boolean isEmpty = TextUtils.isEmpty(restrictionReason);
                                                                                String str50 = restrictionReason;
                                                                                if (isEmpty) {
                                                                                    TLRPC$MessageMedia tLRPC$MessageMedia2 = this.message.messageOwner.media;
                                                                                    if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia2.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                                                                                        str50 = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
                                                                                    } else if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument) && (tLRPC$MessageMedia2.document instanceof TLRPC$TL_documentEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                                                                                        str50 = LocaleController.getString("AttachVideoExpired", R.string.AttachVideoExpired);
                                                                                    } else if (getCaptionMessage() != null) {
                                                                                        MessageObject captionMessage3 = getCaptionMessage();
                                                                                        if (!z4) {
                                                                                            str8 = str3;
                                                                                        } else if (captionMessage3.isVideo()) {
                                                                                            str8 = "📹 ";
                                                                                        } else if (captionMessage3.isVoice()) {
                                                                                            str8 = "🎤 ";
                                                                                        } else if (captionMessage3.isMusic()) {
                                                                                            str8 = "🎧 ";
                                                                                        } else {
                                                                                            str8 = captionMessage3.isPhoto() ? "🖼 " : "📎 ";
                                                                                        }
                                                                                        if (captionMessage3.hasHighlightedWords() && !TextUtils.isEmpty(captionMessage3.messageOwner.message)) {
                                                                                            String str51 = captionMessage3.messageTrimmedToHighlight;
                                                                                            int measuredWidth4 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) + 24);
                                                                                            if (z) {
                                                                                                if (!TextUtils.isEmpty(null)) {
                                                                                                    throw null;
                                                                                                }
                                                                                                measuredWidth4 = (int) (measuredWidth4 - textPaint5.measureText(": "));
                                                                                            }
                                                                                            if (measuredWidth4 > 0) {
                                                                                                str51 = AndroidUtilities.ellipsizeCenterEnd(str51, captionMessage3.highlightedWords.get(0), measuredWidth4, textPaint5, 130).toString();
                                                                                            }
                                                                                            str50 = str8 + str51;
                                                                                        } else {
                                                                                            SpannableStringBuilder spannableStringBuilder17 = new SpannableStringBuilder(captionMessage3.caption);
                                                                                            TLRPC$Message tLRPC$Message3 = captionMessage3.messageOwner;
                                                                                            if (tLRPC$Message3 != null) {
                                                                                                MediaDataController.addTextStyleRuns(tLRPC$Message3.entities, captionMessage3.caption, spannableStringBuilder17, 256);
                                                                                                MediaDataController.addAnimatedEmojiSpans(captionMessage3.messageOwner.entities, spannableStringBuilder17, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                            }
                                                                                            str50 = new SpannableStringBuilder(str8).append((CharSequence) spannableStringBuilder17);
                                                                                        }
                                                                                    } else if (this.thumbsCount > 1) {
                                                                                        if (this.hasVideoThumb) {
                                                                                            ArrayList<MessageObject> arrayList5 = this.groupMessages;
                                                                                            str7 = LocaleController.formatPluralString("Media", arrayList5 == null ? 0 : arrayList5.size(), new Object[0]);
                                                                                        } else {
                                                                                            ArrayList<MessageObject> arrayList6 = this.groupMessages;
                                                                                            str7 = LocaleController.formatPluralString("Photos", arrayList6 == null ? 0 : arrayList6.size(), new Object[0]);
                                                                                        }
                                                                                        textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                        str50 = str7;
                                                                                    } else {
                                                                                        MessageObject messageObject15 = this.message;
                                                                                        TLRPC$MessageMedia tLRPC$MessageMedia3 = messageObject15.messageOwner.media;
                                                                                        if (tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaPoll) {
                                                                                            str6 = "📊 " + ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia3).poll.question;
                                                                                        } else if (tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaGame) {
                                                                                            str6 = "🎮 " + this.message.messageOwner.media.game.title;
                                                                                        } else if (tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                            str6 = tLRPC$MessageMedia3.title;
                                                                                        } else if (messageObject15.type == 14) {
                                                                                            str6 = String.format("🎧 %s - %s", messageObject15.getMusicAuthor(), this.message.getMusicTitle());
                                                                                        } else {
                                                                                            if (messageObject15.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                str5 = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 23), textPaint5, 130).toString();
                                                                                            } else {
                                                                                                ?? spannableStringBuilder18 = new SpannableStringBuilder(spannableStringBuilder4);
                                                                                                MediaDataController.addTextStyleRuns(this.message, (Spannable) spannableStringBuilder18, 256);
                                                                                                MessageObject messageObject16 = this.message;
                                                                                                str5 = spannableStringBuilder18;
                                                                                                if (messageObject16 != null) {
                                                                                                    TLRPC$Message tLRPC$Message4 = messageObject16.messageOwner;
                                                                                                    str5 = spannableStringBuilder18;
                                                                                                    if (tLRPC$Message4 != null) {
                                                                                                        MediaDataController.addAnimatedEmojiSpans(tLRPC$Message4.entities, spannableStringBuilder18, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                                        str5 = spannableStringBuilder18;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            AndroidUtilities.highlightText(str5, this.message.highlightedWords, this.resourcesProvider);
                                                                                            str6 = str5;
                                                                                        }
                                                                                        MessageObject messageObject17 = this.message;
                                                                                        str50 = str6;
                                                                                        if (messageObject17.messageOwner.media != null) {
                                                                                            str50 = str6;
                                                                                            if (!messageObject17.isMediaEmpty()) {
                                                                                                textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                str50 = str6;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if (this.thumbsCount > 0) {
                                                                                    if (this.message.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                        replaceNewLines = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp((((this.messagePaddingStart + 23) + ((i2 + 2) * this.thumbsCount)) - 2) + 5), textPaint5, 130).toString();
                                                                                    } else {
                                                                                        int length2 = str50.length();
                                                                                        String str52 = str50;
                                                                                        if (length2 > 150) {
                                                                                            str52 = str50.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                        }
                                                                                        replaceNewLines = AndroidUtilities.replaceNewLines(str52);
                                                                                    }
                                                                                    boolean z21 = replaceNewLines instanceof SpannableStringBuilder;
                                                                                    SpannableStringBuilder spannableStringBuilder19 = replaceNewLines;
                                                                                    if (!z21) {
                                                                                        spannableStringBuilder19 = new SpannableStringBuilder(replaceNewLines);
                                                                                    }
                                                                                    SpannableStringBuilder spannableStringBuilder20 = (SpannableStringBuilder) spannableStringBuilder19;
                                                                                    spannableStringBuilder20.insert(0, (CharSequence) " ");
                                                                                    spannableStringBuilder20.setSpan(new FixedWidthSpan(AndroidUtilities.dp((((i2 + 2) * this.thumbsCount) - 2) + 5)), 0, 1, 33);
                                                                                    Emoji.replaceEmoji(spannableStringBuilder20, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
                                                                                    charSequence = (!this.message.hasHighlightedWords() || (highlightText = AndroidUtilities.highlightText(spannableStringBuilder20, this.message.highlightedWords, this.resourcesProvider)) == null) ? spannableStringBuilder19 : highlightText;
                                                                                } else {
                                                                                    str4 = str50;
                                                                                    z5 = true;
                                                                                    charSequence4 = null;
                                                                                    z6 = true;
                                                                                    charSequence7 = str4;
                                                                                    i18 = 0;
                                                                                    charSequence5 = charSequence4;
                                                                                    str13 = charSequence7;
                                                                                    str14 = str3;
                                                                                    if (this.currentDialogFolderId != 0) {
                                                                                        charSequence5 = formatArchivedDialogNames();
                                                                                    }
                                                                                    i6 = -1;
                                                                                    CharSequence charSequence132222 = charSequence5;
                                                                                    z7 = z5;
                                                                                    charSequence6 = charSequence132222;
                                                                                    spannableStringBuilder3 = str13;
                                                                                    str23 = str14;
                                                                                }
                                                                            }
                                                                        }
                                                                        z5 = true;
                                                                        charSequence4 = null;
                                                                        z6 = false;
                                                                        charSequence7 = charSequence;
                                                                        i18 = 0;
                                                                        charSequence5 = charSequence4;
                                                                        str13 = charSequence7;
                                                                        str14 = str3;
                                                                        if (this.currentDialogFolderId != 0) {
                                                                        }
                                                                        i6 = -1;
                                                                        CharSequence charSequence1322222 = charSequence5;
                                                                        z7 = z5;
                                                                        charSequence6 = charSequence1322222;
                                                                        spannableStringBuilder3 = str13;
                                                                        str23 = str14;
                                                                    }
                                                                    str13 = string;
                                                                    str14 = str2;
                                                                    z5 = false;
                                                                    charSequence5 = null;
                                                                    z6 = true;
                                                                    i18 = 0;
                                                                    z16 = false;
                                                                    if (this.currentDialogFolderId != 0) {
                                                                    }
                                                                    i6 = -1;
                                                                    CharSequence charSequence13222222 = charSequence5;
                                                                    z7 = z5;
                                                                    charSequence6 = charSequence13222222;
                                                                    spannableStringBuilder3 = str13;
                                                                    str23 = str14;
                                                                }
                                                                str3 = str2;
                                                                str4 = str12;
                                                                z5 = true;
                                                                charSequence4 = null;
                                                                z6 = true;
                                                                charSequence7 = str4;
                                                                i18 = 0;
                                                                charSequence5 = charSequence4;
                                                                str13 = charSequence7;
                                                                str14 = str3;
                                                                if (this.currentDialogFolderId != 0) {
                                                                }
                                                                i6 = -1;
                                                                CharSequence charSequence132222222 = charSequence5;
                                                                z7 = z5;
                                                                charSequence6 = charSequence132222222;
                                                                spannableStringBuilder3 = str13;
                                                                str23 = str14;
                                                            }
                                                            spannableStringBuilder = str2;
                                                            z3 = false;
                                                            if (z3) {
                                                            }
                                                            str3 = str2;
                                                            str4 = str12;
                                                            z5 = true;
                                                            charSequence4 = null;
                                                            z6 = true;
                                                            charSequence7 = str4;
                                                            i18 = 0;
                                                            charSequence5 = charSequence4;
                                                            str13 = charSequence7;
                                                            str14 = str3;
                                                            if (this.currentDialogFolderId != 0) {
                                                            }
                                                            i6 = -1;
                                                            CharSequence charSequence1322222222 = charSequence5;
                                                            z7 = z5;
                                                            charSequence6 = charSequence1322222222;
                                                            spannableStringBuilder3 = str13;
                                                            str23 = str14;
                                                        }
                                                    }
                                                    str2 = "";
                                                    spannableStringBuilder = str2;
                                                    z3 = false;
                                                    if (z3) {
                                                    }
                                                    str3 = str2;
                                                    str4 = str12;
                                                    z5 = true;
                                                    charSequence4 = null;
                                                    z6 = true;
                                                    charSequence7 = str4;
                                                    i18 = 0;
                                                    charSequence5 = charSequence4;
                                                    str13 = charSequence7;
                                                    str14 = str3;
                                                    if (this.currentDialogFolderId != 0) {
                                                    }
                                                    i6 = -1;
                                                    CharSequence charSequence13222222222 = charSequence5;
                                                    z7 = z5;
                                                    charSequence6 = charSequence13222222222;
                                                    spannableStringBuilder3 = str13;
                                                    str23 = str14;
                                                }
                                            }
                                            str16 = "";
                                            str17 = str18;
                                            charSequence6 = null;
                                            z7 = true;
                                            z6 = true;
                                            str20 = str17;
                                            str15 = str16;
                                        }
                                        i18 = 0;
                                        str19 = str20;
                                        i6 = -1;
                                        spannableStringBuilder3 = str19;
                                        str23 = str15;
                                    }
                                    if (this.draftMessage != null) {
                                        stringForMessageListDate = LocaleController.stringForMessageListDate(tLRPC$DraftMessage2.date);
                                    } else {
                                        int i29 = this.lastMessageDate;
                                        if (i29 != 0) {
                                            stringForMessageListDate = LocaleController.stringForMessageListDate(i29);
                                        } else {
                                            stringForMessageListDate = this.message != null ? LocaleController.stringForMessageListDate(messageObject.messageOwner.date) : str23;
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
                                        str26 = null;
                                        z11 = false;
                                        str25 = null;
                                    } else {
                                        if (this.currentDialogFolderId != 0) {
                                            int i30 = this.unreadCount;
                                            int i31 = this.mentionCount;
                                            if (i30 + i31 <= 0) {
                                                this.drawCount = false;
                                                this.drawMention = false;
                                                str26 = null;
                                            } else if (i30 > i31) {
                                                this.drawCount = true;
                                                this.drawMention = false;
                                                str26 = String.format("%d", Integer.valueOf(i30 + i31));
                                            } else {
                                                this.drawCount = false;
                                                this.drawMention = true;
                                                str25 = String.format("%d", Integer.valueOf(i30 + i31));
                                                str26 = null;
                                                this.drawReactionMention = false;
                                            }
                                            str25 = null;
                                            this.drawReactionMention = false;
                                        } else {
                                            if (this.clearingDialog) {
                                                this.drawCount = false;
                                                z9 = true;
                                                z16 = false;
                                                str24 = null;
                                                z10 = false;
                                            } else {
                                                int i32 = this.unreadCount;
                                                if (i32 != 0 && (i32 != 1 || i32 != this.mentionCount || messageObject2 == null || !messageObject2.messageOwner.mentioned)) {
                                                    z9 = true;
                                                    this.drawCount = true;
                                                    z10 = false;
                                                    str24 = String.format("%d", Integer.valueOf(i32));
                                                } else {
                                                    z9 = true;
                                                    z10 = false;
                                                    if (this.markUnread) {
                                                        this.drawCount = true;
                                                        str24 = str23;
                                                    } else {
                                                        this.drawCount = false;
                                                        str24 = null;
                                                    }
                                                }
                                            }
                                            if (this.mentionCount != 0) {
                                                this.drawMention = z9;
                                                str25 = "@";
                                            } else {
                                                this.drawMention = z10;
                                                str25 = null;
                                            }
                                            if (this.reactionMentionCount > 0) {
                                                this.drawReactionMention = z9;
                                            } else {
                                                this.drawReactionMention = z10;
                                            }
                                            str26 = str24;
                                        }
                                        if (this.message.isOut() && this.draftMessage == null && z16) {
                                            MessageObject messageObject18 = this.message;
                                            if (!(messageObject18.messageOwner.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                                                if (messageObject18.isSending()) {
                                                    z11 = false;
                                                    this.drawCheck1 = false;
                                                    this.drawCheck2 = false;
                                                    this.drawClock = true;
                                                    this.drawError = false;
                                                } else {
                                                    z11 = false;
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
                                                        z11 = false;
                                                        this.drawClock = false;
                                                        this.drawError = false;
                                                    } else {
                                                        z11 = false;
                                                    }
                                                }
                                            }
                                        }
                                        z11 = false;
                                        this.drawCheck1 = false;
                                        this.drawCheck2 = false;
                                        this.drawClock = false;
                                        this.drawError = false;
                                    }
                                    this.promoDialog = z11;
                                    MessagesController messagesController2 = MessagesController.getInstance(this.currentAccount);
                                    String str472 = spannableStringBuilder3;
                                    if (this.dialogsType == 0) {
                                        str472 = spannableStringBuilder3;
                                        if (messagesController2.isPromoDialog(this.currentDialogId, true)) {
                                            this.drawPinBackground = true;
                                            this.promoDialog = true;
                                            int i33 = messagesController2.promoDialogType;
                                            if (i33 == MessagesController.PROMO_TYPE_PROXY) {
                                                stringForMessageListDate = LocaleController.getString("UseProxySponsor", R.string.UseProxySponsor);
                                                str472 = spannableStringBuilder3;
                                            } else {
                                                str472 = spannableStringBuilder3;
                                                if (i33 == MessagesController.PROMO_TYPE_PSA) {
                                                    stringForMessageListDate = LocaleController.getString("PsaType_" + messagesController2.promoPsaType);
                                                    if (TextUtils.isEmpty(stringForMessageListDate)) {
                                                        stringForMessageListDate = LocaleController.getString("PsaTypeDefault", R.string.PsaTypeDefault);
                                                    }
                                                    str472 = spannableStringBuilder3;
                                                    if (!TextUtils.isEmpty(messagesController2.promoPsaMessage)) {
                                                        String str53 = messagesController2.promoPsaMessage;
                                                        this.thumbsCount = 0;
                                                        str472 = str53;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    str27 = stringForMessageListDate;
                                    if (this.currentDialogFolderId != 0) {
                                        str28 = LocaleController.getString("ArchivedChats", R.string.ArchivedChats);
                                    } else {
                                        TLRPC$Chat tLRPC$Chat9 = this.chat;
                                        if (tLRPC$Chat9 != null) {
                                            if (this.isTopic) {
                                                str28 = this.forumTopic.title;
                                            } else {
                                                str28 = tLRPC$Chat9.title;
                                            }
                                        } else {
                                            TLRPC$User tLRPC$User3 = this.user;
                                            if (tLRPC$User3 == null) {
                                                str28 = str23;
                                            } else if (UserObject.isReplyUser(tLRPC$User3)) {
                                                str28 = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                                            } else if (UserObject.isUserSelf(this.user)) {
                                                if (this.useMeForMyMessages) {
                                                    str28 = LocaleController.getString("FromYou", R.string.FromYou);
                                                } else {
                                                    if (this.dialogsType == 3) {
                                                        this.drawPinBackground = true;
                                                    }
                                                    str28 = LocaleController.getString("SavedMessages", R.string.SavedMessages);
                                                }
                                            } else {
                                                str28 = UserObject.getUserName(this.user);
                                            }
                                        }
                                        if (str28 != null && str28.length() == 0) {
                                            str28 = LocaleController.getString("HiddenName", R.string.HiddenName);
                                        }
                                    }
                                    boolean z192 = z7;
                                    str29 = charSequence6;
                                    z12 = z192;
                                    String str482 = str25;
                                    str30 = str28;
                                    str31 = str482;
                                    charSequence8 = str472;
                                }
                                this.draftMessage = null;
                                if (printingString == null) {
                                }
                                if (this.draftMessage != null) {
                                }
                                messageObject2 = this.message;
                                if (messageObject2 == null) {
                                }
                                this.promoDialog = z11;
                                MessagesController messagesController22 = MessagesController.getInstance(this.currentAccount);
                                String str4722 = spannableStringBuilder3;
                                if (this.dialogsType == 0) {
                                }
                                str27 = stringForMessageListDate;
                                if (this.currentDialogFolderId != 0) {
                                }
                                boolean z1922 = z7;
                                str29 = charSequence6;
                                z12 = z1922;
                                String str4822 = str25;
                                str30 = str28;
                                str31 = str4822;
                                charSequence8 = str4722;
                            }
                        } else {
                            i2 = i;
                        }
                        z2 = false;
                        this.drawPremium = z2;
                        if (z2) {
                        }
                        i3 = this.lastMessageDate;
                        if (i3 == 0) {
                            i3 = messageObject3.messageOwner.date;
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
                        this.promoDialog = z11;
                        MessagesController messagesController222 = MessagesController.getInstance(this.currentAccount);
                        String str47222 = spannableStringBuilder3;
                        if (this.dialogsType == 0) {
                        }
                        str27 = stringForMessageListDate;
                        if (this.currentDialogFolderId != 0) {
                        }
                        boolean z19222 = z7;
                        str29 = charSequence6;
                        z12 = z19222;
                        String str48222 = str25;
                        str30 = str28;
                        str31 = str48222;
                        charSequence8 = str47222;
                    }
                }
            }
            i2 = i;
            i3 = this.lastMessageDate;
            if (i3 == 0) {
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
            this.promoDialog = z11;
            MessagesController messagesController2222 = MessagesController.getInstance(this.currentAccount);
            String str472222 = spannableStringBuilder3;
            if (this.dialogsType == 0) {
            }
            str27 = stringForMessageListDate;
            if (this.currentDialogFolderId != 0) {
            }
            boolean z192222 = z7;
            str29 = charSequence6;
            z12 = z192222;
            String str482222 = str25;
            str30 = str28;
            str31 = str482222;
            charSequence8 = str472222;
        }
        if (!z12) {
            i7 = (int) Math.ceil(Theme.dialogs_timePaint.measureText(str27));
            this.timeLayout = new StaticLayout(str27, Theme.dialogs_timePaint, i7, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (!LocaleController.isRTL) {
                this.timeLeft = (getMeasuredWidth() - AndroidUtilities.dp(15.0f)) - i7;
            } else {
                this.timeLeft = AndroidUtilities.dp(15.0f);
            }
        } else {
            this.timeLayout = null;
            this.timeLeft = 0;
            i7 = 0;
        }
        if (!drawLock2()) {
            if (LocaleController.isRTL) {
                this.lock2Left = this.timeLeft + i7 + AndroidUtilities.dp(4.0f);
            } else {
                this.lock2Left = (this.timeLeft - Theme.dialogs_lock2Drawable.getIntrinsicWidth()) - AndroidUtilities.dp(4.0f);
            }
            i8 = Theme.dialogs_lock2Drawable.getIntrinsicWidth() + AndroidUtilities.dp(4.0f) + 0;
            i7 += i8;
        } else {
            i8 = 0;
        }
        if (LocaleController.isRTL) {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(22.0f)) - i7;
        } else {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp((this.messagePaddingStart + 5) + 8)) - i7;
            this.nameLeft += i7;
        }
        if (this.drawNameLock) {
            this.nameWidth -= AndroidUtilities.dp(4.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
        }
        if (!this.drawClock) {
            int intrinsicWidth2 = Theme.dialogs_clockDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            this.nameWidth -= intrinsicWidth2;
            if (!LocaleController.isRTL) {
                this.clockDrawLeft = (this.timeLeft - i8) - intrinsicWidth2;
            } else {
                this.clockDrawLeft = this.timeLeft + i7 + AndroidUtilities.dp(5.0f);
                this.nameLeft += intrinsicWidth2;
            }
        } else if (this.drawCheck2) {
            int intrinsicWidth3 = Theme.dialogs_checkDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            int i34 = this.nameWidth - intrinsicWidth3;
            this.nameWidth = i34;
            if (this.drawCheck1) {
                this.nameWidth = i34 - (Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0f));
                if (!LocaleController.isRTL) {
                    int i35 = (this.timeLeft - i8) - intrinsicWidth3;
                    this.halfCheckDrawLeft = i35;
                    this.checkDrawLeft = i35 - AndroidUtilities.dp(5.5f);
                } else {
                    int dp7 = this.timeLeft + i7 + AndroidUtilities.dp(5.0f);
                    this.checkDrawLeft = dp7;
                    this.halfCheckDrawLeft = dp7 + AndroidUtilities.dp(5.5f);
                    this.nameLeft += (intrinsicWidth3 + Theme.dialogs_halfCheckDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(8.0f);
                }
            } else if (!LocaleController.isRTL) {
                this.checkDrawLeft1 = (this.timeLeft - i8) - intrinsicWidth3;
            } else {
                this.checkDrawLeft1 = this.timeLeft + i7 + AndroidUtilities.dp(5.0f);
                this.nameLeft += intrinsicWidth3;
            }
        }
        if (!this.drawPremium && this.emojiStatus.getDrawable() != null) {
            int dp8 = AndroidUtilities.dp(36.0f);
            this.nameWidth -= dp8;
            if (LocaleController.isRTL) {
                this.nameLeft += dp8;
            }
        } else if (!this.dialogMuted && !this.drawVerified && this.drawScam == 0) {
            int dp9 = AndroidUtilities.dp(6.0f) + Theme.dialogs_muteDrawable.getIntrinsicWidth();
            this.nameWidth -= dp9;
            if (LocaleController.isRTL) {
                this.nameLeft += dp9;
            }
        } else if (!this.drawVerified) {
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
            CharSequence replace22 = str30.replace('\n', ' ');
            if (this.nameLayoutEllipsizeByGradient) {
                this.nameLayoutFits = replace22.length() == TextUtils.ellipsize(replace22, Theme.dialogs_namePaint[this.paintIndex], (float) dp6, TextUtils.TruncateAt.END).length();
                dp6 += AndroidUtilities.dp(48.0f);
            }
            float f2 = dp6;
            this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(replace22.toString()) <= f2;
            if (!this.twoLinesForName) {
                replace22 = TextUtils.ellipsize(replace22, Theme.dialogs_namePaint[this.paintIndex], f2, TextUtils.TruncateAt.END);
            }
            CharSequence replaceEmoji22 = Emoji.replaceEmoji(replace22, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            MessageObject messageObject122 = this.message;
            CharSequence charSequence15 = (messageObject122 != null || !messageObject122.hasHighlightedWords() || (highlightText5 = AndroidUtilities.highlightText(replaceEmoji22, this.message.highlightedWords, this.resourcesProvider)) == null) ? replaceEmoji22 : highlightText5;
            if (!this.twoLinesForName) {
                this.nameLayout = StaticLayoutEx.createStaticLayout(charSequence15, Theme.dialogs_namePaint[this.paintIndex], dp6, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, dp6, 2);
            } else {
                this.nameLayout = new StaticLayout(charSequence15, Theme.dialogs_namePaint[this.paintIndex], Math.max(dp6, this.nameWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
            this.nameLayoutTranslateX = (this.nameLayoutEllipsizeByGradient || !this.nameLayout.isRtlCharAt(0)) ? 0.0f : -AndroidUtilities.dp(36.0f);
            this.nameLayoutEllipsizeLeft = this.nameLayout.isRtlCharAt(0);
        } catch (Exception e5) {
            FileLog.e(e5);
        }
        if (!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
            str32 = str23;
            i9 = i2;
            dp = AndroidUtilities.dp(11.0f);
            this.messageNameTop = AndroidUtilities.dp(32.0f);
            this.timeTop = AndroidUtilities.dp(13.0f);
            this.errorTop = AndroidUtilities.dp(43.0f);
            this.pinTop = AndroidUtilities.dp(43.0f);
            this.countTop = AndroidUtilities.dp(43.0f);
            this.checkDrawTop = AndroidUtilities.dp(13.0f);
            int measuredWidth22 = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 21);
            if (!LocaleController.isRTL) {
                int dp13 = AndroidUtilities.dp(16.0f);
                this.messageNameLeft = dp13;
                this.messageLeft = dp13;
                dp2 = getMeasuredWidth() - AndroidUtilities.dp(66.0f);
                dp3 = dp2 - AndroidUtilities.dp(31.0f);
            } else {
                int dp14 = AndroidUtilities.dp(this.messagePaddingStart + 6);
                this.messageNameLeft = dp14;
                this.messageLeft = dp14;
                dp2 = AndroidUtilities.dp(10.0f);
                dp3 = AndroidUtilities.dp(69.0f) + dp2;
            }
            i10 = measuredWidth22;
            i11 = i6;
            this.avatarImage.setImageCoords(dp2, dp, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            i12 = 0;
            while (true) {
                imageReceiverArr = this.thumbImage;
                if (i12 < imageReceiverArr.length) {
                    break;
                }
                imageReceiverArr[i12].setImageCoords(((i9 + 2) * i12) + dp3, AndroidUtilities.dp(31.0f) + dp, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                i12++;
                dp = dp;
            }
            i13 = dp;
        } else {
            int dp15 = AndroidUtilities.dp(9.0f);
            this.messageNameTop = AndroidUtilities.dp(31.0f);
            this.timeTop = AndroidUtilities.dp(16.0f);
            this.errorTop = AndroidUtilities.dp(39.0f);
            this.pinTop = AndroidUtilities.dp(39.0f);
            this.countTop = AndroidUtilities.dp(this.isTopic ? 36.0f : 39.0f);
            this.checkDrawTop = AndroidUtilities.dp(17.0f);
            int measuredWidth5 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) - (LocaleController.isRTL ? 0 : 12));
            if (LocaleController.isRTL) {
                int dp16 = AndroidUtilities.dp(22.0f);
                this.messageNameLeft = dp16;
                this.messageLeft = dp16;
                dp4 = getMeasuredWidth() - AndroidUtilities.dp(64.0f);
                dp5 = dp4 - AndroidUtilities.dp(((this.thumbsCount * (i2 + 2)) - 2) + 11);
            } else {
                int dp17 = AndroidUtilities.dp(this.messagePaddingStart + 4);
                this.messageNameLeft = dp17;
                this.messageLeft = dp17;
                dp4 = AndroidUtilities.dp(10.0f);
                dp5 = AndroidUtilities.dp(67.0f) + dp4;
            }
            str32 = str23;
            i10 = measuredWidth5;
            this.avatarImage.setImageCoords(dp4, dp15, AndroidUtilities.dp(54.0f), AndroidUtilities.dp(54.0f));
            int i36 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr3 = this.thumbImage;
                if (i36 >= imageReceiverArr3.length) {
                    break;
                }
                int i37 = dp15;
                float f3 = i2;
                imageReceiverArr3[i36].setImageCoords(((i2 + 2) * i36) + dp5, AndroidUtilities.dp(30.0f) + dp15, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3));
                i36++;
                dp15 = i37;
                dp5 = dp5;
            }
            i13 = dp15;
            i9 = i2;
            i11 = i6;
        }
        int i282 = i10;
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
            i282 -= dp18;
            if (!LocaleController.isRTL) {
                this.errorLeft = getMeasuredWidth() - AndroidUtilities.dp(34.0f);
            } else {
                this.errorLeft = AndroidUtilities.dp(11.0f);
                this.messageLeft += dp18;
                this.messageNameLeft += dp18;
            }
        } else if (str26 != null || str31 != null || this.drawReactionMention) {
            if (str26 != null) {
                this.countWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str26)));
                this.countLayout = new StaticLayout(str26, Theme.dialogs_countTextPaint, this.countWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                int dp19 = this.countWidth + AndroidUtilities.dp(18.0f);
                i282 -= dp19;
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
            if (str31 != null) {
                if (this.currentDialogFolderId != 0) {
                    this.mentionWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str31)));
                    this.mentionLayout = new StaticLayout(str31, Theme.dialogs_countTextPaint, this.mentionWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                } else {
                    this.mentionWidth = AndroidUtilities.dp(12.0f);
                }
                int dp20 = this.mentionWidth + AndroidUtilities.dp(18.0f);
                i282 -= dp20;
                if (!LocaleController.isRTL) {
                    int measuredWidth6 = (getMeasuredWidth() - this.mentionWidth) - AndroidUtilities.dp(20.0f);
                    int i38 = this.countWidth;
                    this.mentionLeft = measuredWidth6 - (i38 != 0 ? i38 + AndroidUtilities.dp(18.0f) : 0);
                } else {
                    int dp21 = AndroidUtilities.dp(20.0f);
                    int i39 = this.countWidth;
                    this.mentionLeft = dp21 + (i39 != 0 ? i39 + AndroidUtilities.dp(18.0f) : 0);
                    this.messageLeft += dp20;
                    this.messageNameLeft += dp20;
                }
                this.drawMention = true;
            } else {
                this.mentionWidth = 0;
            }
            if (this.drawReactionMention) {
                int dp22 = AndroidUtilities.dp(24.0f);
                i282 -= dp22;
                if (!LocaleController.isRTL) {
                    int measuredWidth7 = getMeasuredWidth() - AndroidUtilities.dp(32.0f);
                    this.reactionMentionLeft = measuredWidth7;
                    if (this.drawMention) {
                        int i40 = this.mentionWidth;
                        this.reactionMentionLeft = measuredWidth7 - (i40 != 0 ? i40 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i41 = this.reactionMentionLeft;
                        int i42 = this.countWidth;
                        this.reactionMentionLeft = i41 - (i42 != 0 ? i42 + AndroidUtilities.dp(18.0f) : 0);
                    }
                } else {
                    int dp23 = AndroidUtilities.dp(20.0f);
                    this.reactionMentionLeft = dp23;
                    if (this.drawMention) {
                        int i43 = this.mentionWidth;
                        this.reactionMentionLeft = dp23 + (i43 != 0 ? i43 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i44 = this.reactionMentionLeft;
                        int i45 = this.countWidth;
                        this.reactionMentionLeft = i44 + (i45 != 0 ? i45 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    this.messageLeft += dp22;
                    this.messageNameLeft += dp22;
                }
            }
        } else {
            if (getIsPinned()) {
                int intrinsicWidth4 = Theme.dialogs_pinnedDrawable.getIntrinsicWidth() + AndroidUtilities.dp(8.0f);
                i282 -= intrinsicWidth4;
                if (LocaleController.isRTL) {
                    this.messageLeft += intrinsicWidth4;
                    this.messageNameLeft += intrinsicWidth4;
                }
            }
            this.drawCount = false;
            this.drawMention = false;
        }
        if (z6) {
            CharSequence charSequence16 = charSequence8 == null ? str32 : charSequence8;
            if (charSequence16.length() > 150) {
                charSequence16 = charSequence16.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
            }
            if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || str29 != null) {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceNewLines(charSequence16);
            } else {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceTwoNewLinesToOne(charSequence16);
            }
            charSequence8 = Emoji.replaceEmoji(replaceTwoNewLinesToOne, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
            MessageObject messageObject19 = this.message;
            if (messageObject19 != null && (highlightText4 = AndroidUtilities.highlightText(charSequence8, messageObject19.highlightedWords, this.resourcesProvider)) != null) {
                charSequence8 = highlightText4;
            }
        }
        max = Math.max(AndroidUtilities.dp(12.0f), i282);
        z13 = this.useForceThreeLines;
        if ((!z13 || SharedConfig.useThreeLinesLayout) && str29 != null && (this.currentDialogFolderId == 0 || this.currentDialogFolderDialogsCount == 1)) {
            messageObject4 = this.message;
            if (messageObject4 != null && messageObject4.hasHighlightedWords() && (highlightText3 = AndroidUtilities.highlightText(str29, this.message.highlightedWords, this.resourcesProvider)) != null) {
                str29 = highlightText3;
            }
            this.messageNameLayout = StaticLayoutEx.createStaticLayout(str29, Theme.dialogs_messageNamePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max, 1);
            this.messageTop = AndroidUtilities.dp(51.0f);
            i14 = 0;
            while (true) {
                imageReceiverArr2 = this.thumbImage;
                if (i14 < imageReceiverArr2.length) {
                    break;
                }
                imageReceiverArr2[i14].setImageY(i13 + AndroidUtilities.dp(40.0f));
                i14++;
            }
        } else {
            this.messageNameLayout = null;
            if (z13 || SharedConfig.useThreeLinesLayout) {
                this.messageTop = AndroidUtilities.dp(32.0f);
                int i46 = 0;
                while (true) {
                    ImageReceiver[] imageReceiverArr4 = this.thumbImage;
                    if (i46 >= imageReceiverArr4.length) {
                        break;
                    }
                    imageReceiverArr4[i46].setImageY(i13 + AndroidUtilities.dp(21.0f));
                    i46++;
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
                charSequence8 = str29;
                str29 = null;
            } else if ((!z14 && !SharedConfig.useThreeLinesLayout) || str29 != null) {
                charSequence8 = TextUtils.ellipsize(charSequence8, textPaint5, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END);
            }
            if (charSequence8 instanceof Spannable) {
                Spannable spannable = (Spannable) charSequence8;
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
            i17 = this.thumbsCount;
            if (i17 > 0) {
                int i47 = i9 + 2;
                max += ((i17 * i47) - 2) + AndroidUtilities.dp(5.0f);
                if (LocaleController.isRTL) {
                    this.messageLeft -= ((this.thumbsCount * i47) - 2) + AndroidUtilities.dp(5.0f);
                }
            }
            this.messageLayout = new StaticLayout(charSequence8, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.spoilersPool.addAll(this.spoilers);
            this.spoilers.clear();
            SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
            if (!LocaleController.isRTL) {
                StaticLayout staticLayout3 = this.nameLayout;
                if (staticLayout3 != null && staticLayout3.getLineCount() > 0) {
                    float lineLeft = this.nameLayout.getLineLeft(0);
                    double ceil2 = Math.ceil(this.nameLayout.getLineWidth(0));
                    int dp24 = this.nameWidth + AndroidUtilities.dp(12.0f);
                    this.nameWidth = dp24;
                    if (this.nameLayoutEllipsizeByGradient) {
                        ceil2 = Math.min(dp24, ceil2);
                    }
                    if (this.dialogMuted && !this.drawVerified && this.drawScam == 0) {
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
                    }
                    if (lineLeft == 0.0f) {
                        int i48 = this.nameWidth;
                        if (ceil2 < i48) {
                            double d17 = this.nameLeft;
                            double d18 = i48;
                            Double.isNaN(d18);
                            Double.isNaN(d17);
                            this.nameLeft = (int) (d17 + (d18 - ceil2));
                        }
                    }
                }
                StaticLayout staticLayout4 = this.messageLayout;
                if (staticLayout4 != null && (lineCount2 = staticLayout4.getLineCount()) > 0) {
                    int i49 = 0;
                    int i50 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    while (true) {
                        if (i49 >= lineCount2) {
                            break;
                        }
                        if (this.messageLayout.getLineLeft(i49) != 0.0f) {
                            i50 = 0;
                            break;
                        }
                        double ceil3 = Math.ceil(this.messageLayout.getLineWidth(i49));
                        double d19 = max;
                        Double.isNaN(d19);
                        i50 = Math.min(i50, (int) (d19 - ceil3));
                        i49++;
                    }
                    if (i50 != Integer.MAX_VALUE) {
                        this.messageLeft += i50;
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
                        int i51 = this.nameWidth;
                        if (ceil5 < i51) {
                            double d22 = this.nameLeft;
                            double d23 = i51;
                            Double.isNaN(d23);
                            Double.isNaN(d22);
                            this.nameLeft = (int) (d22 - (d23 - ceil5));
                        }
                    }
                    if (this.dialogMuted || this.drawVerified || this.drawPremium || this.drawScam != 0) {
                        this.nameMuteLeft = (int) (this.nameLeft + lineRight + AndroidUtilities.dp(6.0f));
                    }
                }
                StaticLayout staticLayout7 = this.messageLayout;
                if (staticLayout7 != null && (lineCount = staticLayout7.getLineCount()) > 0) {
                    float f4 = 2.14748365E9f;
                    for (int i52 = 0; i52 < lineCount; i52++) {
                        f4 = Math.min(f4, this.messageLayout.getLineLeft(i52));
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
                    if (i18 >= length) {
                        i18 = length - 1;
                    }
                    ceil = (int) Math.ceil(Math.min(this.messageLayout.getPrimaryHorizontal(i18), this.messageLayout.getPrimaryHorizontal(i18 + 1)));
                    if (ceil != 0) {
                        ceil += AndroidUtilities.dp(3.0f);
                    }
                    for (i16 = 0; i16 < this.thumbsCount; i16++) {
                        this.thumbImage[i16].setImageX(this.messageLeft + ceil + AndroidUtilities.dp((i9 + 2) * i16));
                    }
                } catch (Exception e7) {
                    FileLog.e(e7);
                }
            }
            staticLayout2 = this.messageLayout;
            if (staticLayout2 != null || this.printingStringType < 0) {
            }
            if (i11 >= 0 && (i15 = i11 + 1) < staticLayout2.getText().length()) {
                primaryHorizontal = this.messageLayout.getPrimaryHorizontal(i11);
                primaryHorizontal2 = this.messageLayout.getPrimaryHorizontal(i15);
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
        if (this.thumbsCount > 0 && str29 != null) {
            max += AndroidUtilities.dp(5.0f);
        }
        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence8, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, str29 == null ? 1 : 2);
        this.spoilersPool.addAll(this.spoilers);
        this.spoilers.clear();
        SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
        if (!LocaleController.isRTL) {
        }
        staticLayout = this.messageLayout;
        if (staticLayout != null) {
            length = staticLayout.getText().length();
            if (i18 >= length) {
            }
            ceil = (int) Math.ceil(Math.min(this.messageLayout.getPrimaryHorizontal(i18), this.messageLayout.getPrimaryHorizontal(i18 + 1)));
            if (ceil != 0) {
            }
            while (i16 < this.thumbsCount) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$buildLayout$1(MessageObject messageObject, MessageObject messageObject2) {
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

    /* JADX WARN: Removed duplicated region for block: B:146:0x0282  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x02b9  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x036c  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0371  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x0375  */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v1, types: [org.telegram.tgnet.TLRPC$Chat, org.telegram.tgnet.TLRPC$User, org.telegram.tgnet.TLRPC$EncryptedChat] */
    /* JADX WARN: Type inference failed for: r3v77 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void update(int i, boolean z) {
        ?? r3;
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
        int i7 = 0;
        if (customDialog != null) {
            this.lastMessageDate = customDialog.date;
            int i8 = customDialog.unread_count;
            if (i8 == 0) {
                z3 = false;
            }
            this.lastUnreadState = z3;
            this.unreadCount = i8;
            this.drawPin = customDialog.pinned;
            this.dialogMuted = customDialog.muted;
            this.hasUnmutedTopics = false;
            this.avatarDrawable.setInfo(customDialog.id, customDialog.name, null);
            this.avatarImage.setImage(null, "50_50", this.avatarDrawable, null, 0L);
            while (true) {
                ImageReceiver[] imageReceiverArr = this.thumbImage;
                if (i7 >= imageReceiverArr.length) {
                    break;
                }
                imageReceiverArr[i7].setImageBitmap((Drawable) null);
                i7++;
            }
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
                                    i5 = 0;
                                    i6 = 0;
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
                        if (!z2 && (i & MessagesController.UPDATE_MASK_SEND_STATE) != 0 && (messageObject = this.message) != null) {
                            i2 = this.lastSendState;
                            i3 = messageObject.messageOwner.send_state;
                            if (i2 != i3) {
                                this.lastSendState = i3;
                                z2 = true;
                            }
                        }
                        if (z2) {
                            invalidate();
                            return;
                        }
                        r3 = 0;
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
                if (z2) {
                }
            } else {
                r3 = 0;
            }
            this.user = r3;
            this.chat = r3;
            this.encryptedChat = r3;
            if (this.currentDialogFolderId != 0) {
                this.dialogMuted = false;
                MessageObject findFolderTopMessage = findFolderTopMessage();
                this.message = findFolderTopMessage;
                j = findFolderTopMessage != null ? findFolderTopMessage.getDialogId() : 0L;
            } else {
                this.dialogMuted = (this.forumTopic != null || this.isDialogCell) && MessagesController.getInstance(this.currentAccount).isDialogMuted(this.currentDialogId, getTopicId());
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
                        DialogCell.this.lambda$update$2(valueAnimator2);
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
                        for (int i11 = 0; i11 < valueOf.length(); i11++) {
                            if (valueOf.charAt(i11) == valueOf2.charAt(i11)) {
                                int i12 = i11 + 1;
                                spannableStringBuilder.setSpan(new EmptyStubSpan(), i11, i12, 0);
                                spannableStringBuilder2.setSpan(new EmptyStubSpan(), i11, i12, 0);
                            } else {
                                spannableStringBuilder3.setSpan(new EmptyStubSpan(), i11, i11 + 1, 0);
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
                ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                        DialogCell.this.lambda$update$3(valueAnimator3);
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
            this.avatarImage.setRoundRadius(AndroidUtilities.dp((!this.isDialogCell || (tLRPC$Chat = this.chat) == null || !tLRPC$Chat.forum) ? 28.0f : 16.0f));
        }
        if (!this.isTopic && (getMeasuredWidth() != 0 || getMeasuredHeight() != 0)) {
            buildLayout();
        } else {
            requestLayout();
        }
        if (!z) {
            this.dialogMutedProgress = this.dialogMuted ? 1.0f : 0.0f;
            ValueAnimator valueAnimator3 = this.countAnimator;
            if (valueAnimator3 != null) {
                valueAnimator3.cancel();
            }
        }
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$2(ValueAnimator valueAnimator) {
        this.countChangeProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$3(ValueAnimator valueAnimator) {
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
        float f2 = (int) f;
        this.translationX = f2;
        RLottieDrawable rLottieDrawable = this.translationDrawable;
        boolean z = false;
        if (rLottieDrawable != null && f2 == 0.0f) {
            rLottieDrawable.setProgress(0.0f);
            this.translationAnimationStarted = false;
            this.archiveHidden = SharedConfig.archiveHidden;
            this.currentRevealProgress = 0.0f;
            this.isSliding = false;
        }
        float f3 = this.translationX;
        if (f3 != 0.0f) {
            this.isSliding = true;
        } else {
            this.currentRevealBounceProgress = 0.0f;
            this.currentRevealProgress = 0.0f;
            this.drawRevealBackground = false;
        }
        if (this.isSliding && !this.swipeCanceled) {
            boolean z2 = this.drawRevealBackground;
            if (Math.abs(f3) >= getMeasuredWidth() * 0.45f) {
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
    /* JADX WARN: Removed duplicated region for block: B:201:0x0a66  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x0ac7  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0ad9  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0c26  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0c4e  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x109d  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x10bd  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x10d4  */
    /* JADX WARN: Removed duplicated region for block: B:262:0x111c  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x1123  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x14c4  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x14cb  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x14f0  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x1557  */
    /* JADX WARN: Removed duplicated region for block: B:335:0x15a4  */
    /* JADX WARN: Removed duplicated region for block: B:340:0x15db  */
    /* JADX WARN: Removed duplicated region for block: B:351:0x1634  */
    /* JADX WARN: Removed duplicated region for block: B:357:0x1649  */
    /* JADX WARN: Removed duplicated region for block: B:364:0x1664  */
    /* JADX WARN: Removed duplicated region for block: B:369:0x1695  */
    /* JADX WARN: Removed duplicated region for block: B:371:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:373:0x1672  */
    /* JADX WARN: Removed duplicated region for block: B:382:0x1605  */
    /* JADX WARN: Removed duplicated region for block: B:391:0x15b0  */
    /* JADX WARN: Removed duplicated region for block: B:398:0x15c3  */
    /* JADX WARN: Removed duplicated region for block: B:419:0x1202  */
    /* JADX WARN: Removed duplicated region for block: B:451:0x13dc  */
    /* JADX WARN: Removed duplicated region for block: B:455:0x1469  */
    /* JADX WARN: Removed duplicated region for block: B:459:0x147e  */
    /* JADX WARN: Removed duplicated region for block: B:464:0x1492  */
    /* JADX WARN: Removed duplicated region for block: B:471:0x14a5  */
    /* JADX WARN: Removed duplicated region for block: B:508:0x0c9c  */
    /* JADX WARN: Removed duplicated region for block: B:624:0x0b12  */
    /* JADX WARN: Removed duplicated region for block: B:625:0x0aca  */
    /* JADX WARN: Removed duplicated region for block: B:637:0x0b1d  */
    /* JADX WARN: Removed duplicated region for block: B:650:0x0b65  */
    /* JADX WARN: Removed duplicated region for block: B:702:0x0a5a  */
    /* JADX WARN: Removed duplicated region for block: B:713:0x0861  */
    /* JADX WARN: Removed duplicated region for block: B:714:0x07de  */
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
        int dp;
        int dp2;
        int dp3;
        float f6;
        float interpolation;
        long j2;
        float f7;
        boolean z2;
        float f8;
        boolean z3;
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
        boolean z4;
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
                            z2 = this.drawReorder;
                            if (!z2) {
                            }
                            if (!z2) {
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
                if (this.dialogsType == i4 && (((z4 = this.dialogMuted) || this.dialogMutedProgress > f5) && !this.drawVerified && this.drawScam == 0 && !this.drawPremium)) {
                    if (z4) {
                        float f20 = this.dialogMutedProgress;
                        if (f20 != f4) {
                            float f21 = f20 + 0.10666667f;
                            this.dialogMutedProgress = f21;
                            if (f21 > f4) {
                                this.dialogMutedProgress = f4;
                            } else {
                                invalidate();
                            }
                            BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f), AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f));
                            if (this.dialogMutedProgress == f4) {
                                canvas.save();
                                float f22 = this.dialogMutedProgress;
                                canvas3.scale(f22, f22, Theme.dialogs_muteDrawable.getBounds().centerX(), Theme.dialogs_muteDrawable.getBounds().centerY());
                                Theme.dialogs_muteDrawable.setAlpha((int) (this.dialogMutedProgress * 255.0f));
                                Theme.dialogs_muteDrawable.draw(canvas3);
                                Theme.dialogs_muteDrawable.setAlpha(255);
                                canvas.restore();
                            } else {
                                Theme.dialogs_muteDrawable.draw(canvas3);
                            }
                        }
                    }
                    if (!z4) {
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
                    BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f), AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f));
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
                    }
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
                        boolean z14 = this.drawCount;
                        if (((z14 || this.drawMention) && this.drawCount2) || this.countChangeProgress != f4 || this.drawReactionMention || this.reactionsMentionsChangeProgress != f4) {
                            boolean z15 = this.dialogMuted && !this.hasUnmutedTopics;
                            if ((z14 && this.drawCount2) || this.countChangeProgress != f4) {
                                int i24 = this.unreadCount;
                                float f26 = (i24 != 0 || this.markUnread) ? this.countChangeProgress : f4 - this.countChangeProgress;
                                StaticLayout staticLayout2 = this.countOldLayout;
                                if (staticLayout2 == null || i24 == 0) {
                                    if (i24 != 0) {
                                        staticLayout2 = this.countLayout;
                                    }
                                    Paint paint3 = (z15 || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
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
                                    Paint paint4 = (z15 || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
                                    paint4.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                    Theme.dialogs_countTextPaint.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                    float f29 = f26 * 2.0f;
                                    float f30 = f29 > f4 ? 1.0f : f29;
                                    float f31 = f4 - f30;
                                    float f32 = (this.countLeft * f30) + (this.countLeftOld * f31);
                                    float dp14 = f32 - AndroidUtilities.dp(5.5f);
                                    this.rect.set(dp14, this.countTop, (this.countWidth * f30) + dp14 + (this.countWidthOld * f31) + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
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
                                Paint paint5 = (!z15 || this.folderId == 0) ? Theme.dialogs_countPaint : Theme.dialogs_countGrayPaint;
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
                                int dp15 = (int) (imageY2 - AndroidUtilities.dp(f13));
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
                                float f40 = dp15;
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
                                    int dp16 = (int) (imageY22 - AndroidUtilities.dp(f13));
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
                                    float f46 = dp16;
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
                                                int dp17 = (this.fullSeparator || (this.currentDialogFolderId != 0 && this.archiveHidden && !this.fullSeparator2) || (this.fullSeparator2 && !this.archiveHidden)) ? 0 : AndroidUtilities.dp(this.messagePaddingStart);
                                                if (LocaleController.isRTL) {
                                                    canvas.drawLine(0.0f, getMeasuredHeight() - 1, getMeasuredWidth() - dp17, getMeasuredHeight() - 1, Theme.dividerPaint);
                                                } else {
                                                    canvas.drawLine(dp17, getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
                                                    if (this.clipProgress != 0.0f) {
                                                        if (Build.VERSION.SDK_INT != 24) {
                                                            canvas.restore();
                                                        } else {
                                                            Theme.dialogs_pinnedPaint.setColor(Theme.getColor(str4, this.resourcesProvider));
                                                            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), this.topClip * this.clipProgress, Theme.dialogs_pinnedPaint);
                                                            canvas.drawRect(0.0f, getMeasuredHeight() - ((int) (this.bottomClip * this.clipProgress)), getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
                                                        }
                                                    }
                                                    z2 = this.drawReorder;
                                                    if (!z2 || this.reorderIconProgress != 0.0f) {
                                                        if (!z2) {
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
                                                                            z3 = true;
                                                                            f9 = this.currentRevealProgress;
                                                                            if (f9 < f4) {
                                                                                float f64 = f9 + (((float) j2) / 300.0f);
                                                                                this.currentRevealProgress = f64;
                                                                                if (f64 > f4) {
                                                                                    this.currentRevealProgress = f4;
                                                                                }
                                                                                z3 = true;
                                                                            }
                                                                            if (!z3) {
                                                                                return;
                                                                            }
                                                                            invalidate();
                                                                            return;
                                                                        }
                                                                    }
                                                                    z3 = z;
                                                                    f9 = this.currentRevealProgress;
                                                                    if (f9 < f4) {
                                                                    }
                                                                    if (!z3) {
                                                                    }
                                                                } else {
                                                                    if (this.currentRevealBounceProgress == f4) {
                                                                        this.currentRevealBounceProgress = 0.0f;
                                                                        z3 = true;
                                                                    } else {
                                                                        z3 = z;
                                                                    }
                                                                    float f65 = this.currentRevealProgress;
                                                                    if (f65 > 0.0f) {
                                                                        float f66 = f65 - (((float) j2) / 300.0f);
                                                                        this.currentRevealProgress = f66;
                                                                        if (f66 < 0.0f) {
                                                                            this.currentRevealProgress = 0.0f;
                                                                        }
                                                                        z3 = true;
                                                                    }
                                                                    if (!z3) {
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
                                            z2 = this.drawReorder;
                                            if (!z2) {
                                            }
                                            if (!z2) {
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
                                        z2 = this.drawReorder;
                                        if (!z2) {
                                        }
                                        if (!z2) {
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
                                    z2 = this.drawReorder;
                                    if (!z2) {
                                    }
                                    if (!z2) {
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
                        z2 = this.drawReorder;
                        if (!z2) {
                        }
                        if (!z2) {
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
                    z2 = this.drawReorder;
                    if (!z2) {
                    }
                    if (!z2) {
                    }
                    z = true;
                    if (!this.archiveHidden) {
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
                    z2 = this.drawReorder;
                    if (!z2) {
                    }
                    if (!z2) {
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
                z2 = this.drawReorder;
                if (!z2) {
                }
                if (!z2) {
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
        z2 = this.drawReorder;
        if (!z2) {
        }
        if (!z2) {
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
        this.statusDrawableAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                DialogCell.this.lambda$createStatusDrawableAnimator$4(valueAnimator);
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
    public /* synthetic */ void lambda$createStatusDrawableAnimator$4(ValueAnimator valueAnimator) {
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
