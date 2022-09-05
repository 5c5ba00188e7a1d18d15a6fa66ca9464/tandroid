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
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.telegram.messenger.AndroidUtilities;
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
import org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom;
import org.telegram.tgnet.TLRPC$TL_messageActionHistoryClear;
import org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaGame;
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
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
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EmptyStubSpan;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.PullForegroundDrawable;
import org.telegram.ui.Components.RLottieDrawable;
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
    private boolean animatingArchiveAvatar;
    private float animatingArchiveAvatarProgress;
    private float archiveBackgroundProgress;
    private boolean archiveHidden;
    private PullForegroundDrawable archivedChatsDrawable;
    private AvatarDrawable avatarDrawable;
    private ImageReceiver avatarImage;
    private int bottomClip;
    private TLRPC$Chat chat;
    private float chatCallProgress;
    private CheckBox2 checkBox;
    private int checkDrawLeft;
    private int checkDrawLeft1;
    private int checkDrawTop;
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
    private boolean drawPlay;
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
    public boolean fullSeparator;
    public boolean fullSeparator2;
    private int halfCheckDrawLeft;
    private boolean hasCall;
    private boolean hasMessageThumb;
    private int index;
    private float innerProgress;
    private BounceInterpolator interpolator;
    private boolean isDialogCell;
    private boolean isSelected;
    private boolean isSliding;
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
    private int messageTop;
    boolean moving;
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
    private ImageReceiver thumbImage;
    private StaticLayout timeLayout;
    private int timeLeft;
    private int timeTop;
    private int topClip;
    private boolean translationAnimationStarted;
    private RLottieDrawable translationDrawable;
    private float translationX;
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
        public boolean sent;
        public int type;
        public int unread_count;
        public boolean verified;
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
        this.thumbImage = new ImageReceiver(this);
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
        this.thumbImage.setRoundRadius(AndroidUtilities.dp(2.0f));
        this.useForceThreeLines = z2;
        this.currentAccount = i;
        if (z) {
            CheckBox2 checkBox2 = new CheckBox2(context, 21, resourcesProvider);
            this.checkBox = checkBox2;
            checkBox2.setColor(null, "windowBackgroundWhite", "checkboxCheck");
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(3);
            addView(this.checkBox);
        }
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this, AndroidUtilities.dp(22.0f));
        this.emojiStatus = swapAnimatedEmojiDrawable;
        swapAnimatedEmojiDrawable.center = false;
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

    public void setDialog(long j, MessageObject messageObject, int i, boolean z) {
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
        update(0);
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

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isSliding = false;
        this.drawRevealBackground = false;
        this.currentRevealProgress = 0.0f;
        this.reorderIconProgress = (!this.drawPin || !this.drawReorder) ? 0.0f : 1.0f;
        this.avatarImage.onDetachedFromWindow();
        this.thumbImage.onDetachedFromWindow();
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
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.avatarImage.onAttachedToWindow();
        this.thumbImage.onAttachedToWindow();
        resetPinnedArchiveState();
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
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
        if (!this.drawPin || !this.drawReorder) {
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
        setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 78.0f : 72.0f) + (this.useSeparator ? 1 : 0));
        this.topClip = 0;
        this.bottomClip = getMeasuredHeight();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int dp;
        if (this.currentDialogId == 0 && this.customDialog == null) {
            return;
        }
        if (this.checkBox != null) {
            float f = 45.0f;
            if (LocaleController.isRTL) {
                int i5 = i3 - i;
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    f = 43.0f;
                }
                dp = i5 - AndroidUtilities.dp(f);
            } else {
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    f = 43.0f;
                }
                dp = AndroidUtilities.dp(f);
            }
            int dp2 = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 48.0f : 42.0f);
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
        return this.drawPin;
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

    /* JADX WARN: Can't wrap try/catch for region: R(17:947|(2:949|(1:951)(1:963))(2:964|(2:966|(1:968)(1:969))(2:970|(1:972)(14:973|(2:975|(1:977)(1:978))(1:979)|953|954|955|(1:957)(1:960)|958|863|(1:898)(6:867|868|869|870|871|872)|873|(1:877)|878|(4:880|(1:882)|883|(1:885)(1:886))|887)))|952|953|954|955|(0)(0)|958|863|(1:865)|894|898|873|(2:875|877)|878|(0)|887) */
    /* JADX WARN: Code restructure failed: missing block: B:1162:0x04bf, code lost:
        if (r2.post_messages == false) goto L560;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1168:0x04cb, code lost:
        if (r2.kicked != false) goto L560;
     */
    /* JADX WARN: Code restructure failed: missing block: B:961:0x0b2d, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:962:0x0b2e, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:1034:0x0e69  */
    /* JADX WARN: Removed duplicated region for block: B:1052:0x0f09  */
    /* JADX WARN: Removed duplicated region for block: B:1053:0x0c8d  */
    /* JADX WARN: Removed duplicated region for block: B:1113:0x0e59  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x13f0  */
    /* JADX WARN: Removed duplicated region for block: B:1169:0x04d1  */
    /* JADX WARN: Removed duplicated region for block: B:1195:0x0422  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x1553  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x1578  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x1748  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x17a0  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x1827 A[Catch: Exception -> 0x191a, TryCatch #3 {Exception -> 0x191a, blocks: (B:159:0x1823, B:161:0x1827, B:164:0x183f, B:166:0x1845, B:167:0x1854, B:169:0x1858, B:171:0x186c, B:173:0x1872, B:175:0x1876, B:178:0x1883, B:180:0x1880, B:183:0x1886, B:185:0x188a, B:188:0x188f, B:190:0x1893, B:192:0x189f, B:193:0x18a9, B:194:0x18f4, B:325:0x18c1, B:328:0x18c7, B:329:0x18ce, B:332:0x18e4, B:335:0x182b, B:337:0x182f, B:339:0x1834), top: B:158:0x1823 }] */
    /* JADX WARN: Removed duplicated region for block: B:169:0x1858 A[Catch: Exception -> 0x191a, TryCatch #3 {Exception -> 0x191a, blocks: (B:159:0x1823, B:161:0x1827, B:164:0x183f, B:166:0x1845, B:167:0x1854, B:169:0x1858, B:171:0x186c, B:173:0x1872, B:175:0x1876, B:178:0x1883, B:180:0x1880, B:183:0x1886, B:185:0x188a, B:188:0x188f, B:190:0x1893, B:192:0x189f, B:193:0x18a9, B:194:0x18f4, B:325:0x18c1, B:328:0x18c7, B:329:0x18ce, B:332:0x18e4, B:335:0x182b, B:337:0x182f, B:339:0x1834), top: B:158:0x1823 }] */
    /* JADX WARN: Removed duplicated region for block: B:185:0x188a A[Catch: Exception -> 0x191a, TryCatch #3 {Exception -> 0x191a, blocks: (B:159:0x1823, B:161:0x1827, B:164:0x183f, B:166:0x1845, B:167:0x1854, B:169:0x1858, B:171:0x186c, B:173:0x1872, B:175:0x1876, B:178:0x1883, B:180:0x1880, B:183:0x1886, B:185:0x188a, B:188:0x188f, B:190:0x1893, B:192:0x189f, B:193:0x18a9, B:194:0x18f4, B:325:0x18c1, B:328:0x18c7, B:329:0x18ce, B:332:0x18e4, B:335:0x182b, B:337:0x182f, B:339:0x1834), top: B:158:0x1823 }] */
    /* JADX WARN: Removed duplicated region for block: B:190:0x1893 A[Catch: Exception -> 0x191a, TryCatch #3 {Exception -> 0x191a, blocks: (B:159:0x1823, B:161:0x1827, B:164:0x183f, B:166:0x1845, B:167:0x1854, B:169:0x1858, B:171:0x186c, B:173:0x1872, B:175:0x1876, B:178:0x1883, B:180:0x1880, B:183:0x1886, B:185:0x188a, B:188:0x188f, B:190:0x1893, B:192:0x189f, B:193:0x18a9, B:194:0x18f4, B:325:0x18c1, B:328:0x18c7, B:329:0x18ce, B:332:0x18e4, B:335:0x182b, B:337:0x182f, B:339:0x1834), top: B:158:0x1823 }] */
    /* JADX WARN: Removed duplicated region for block: B:197:0x1925  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x1b46  */
    /* JADX WARN: Removed duplicated region for block: B:260:0x1b55 A[Catch: Exception -> 0x1b80, TryCatch #0 {Exception -> 0x1b80, blocks: (B:258:0x1b4a, B:260:0x1b55, B:261:0x1b57, B:263:0x1b70, B:264:0x1b77), top: B:257:0x1b4a }] */
    /* JADX WARN: Removed duplicated region for block: B:263:0x1b70 A[Catch: Exception -> 0x1b80, TryCatch #0 {Exception -> 0x1b80, blocks: (B:258:0x1b4a, B:260:0x1b55, B:261:0x1b57, B:263:0x1b70, B:264:0x1b77), top: B:257:0x1b4a }] */
    /* JADX WARN: Removed duplicated region for block: B:270:0x1b88  */
    /* JADX WARN: Removed duplicated region for block: B:285:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:286:0x1a98  */
    /* JADX WARN: Removed duplicated region for block: B:327:0x18c5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:331:0x18df  */
    /* JADX WARN: Removed duplicated region for block: B:333:0x18e2  */
    /* JADX WARN: Removed duplicated region for block: B:351:0x17b3 A[Catch: Exception -> 0x17e1, TryCatch #7 {Exception -> 0x17e1, blocks: (B:349:0x17af, B:351:0x17b3, B:353:0x17b9, B:356:0x17c6), top: B:348:0x17af }] */
    /* JADX WARN: Removed duplicated region for block: B:362:0x15a8  */
    /* JADX WARN: Removed duplicated region for block: B:434:0x14ea  */
    /* JADX WARN: Removed duplicated region for block: B:436:0x1508  */
    /* JADX WARN: Removed duplicated region for block: B:444:0x1369  */
    /* JADX WARN: Removed duplicated region for block: B:458:0x12d3  */
    /* JADX WARN: Removed duplicated region for block: B:461:0x12ed  */
    /* JADX WARN: Removed duplicated region for block: B:476:0x1205  */
    /* JADX WARN: Removed duplicated region for block: B:488:0x11a7  */
    /* JADX WARN: Removed duplicated region for block: B:490:0x118a  */
    /* JADX WARN: Removed duplicated region for block: B:549:0x0470  */
    /* JADX WARN: Removed duplicated region for block: B:554:0x047c  */
    /* JADX WARN: Removed duplicated region for block: B:563:0x04d6  */
    /* JADX WARN: Removed duplicated region for block: B:574:0x0f21  */
    /* JADX WARN: Removed duplicated region for block: B:577:0x0f46  */
    /* JADX WARN: Removed duplicated region for block: B:580:0x107c  */
    /* JADX WARN: Removed duplicated region for block: B:588:0x10e2  */
    /* JADX WARN: Removed duplicated region for block: B:590:0x10f1  */
    /* JADX WARN: Removed duplicated region for block: B:596:0x1140  */
    /* JADX WARN: Removed duplicated region for block: B:622:0x10d9  */
    /* JADX WARN: Removed duplicated region for block: B:623:0x0f5a  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x114b  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x1195  */
    /* JADX WARN: Removed duplicated region for block: B:684:0x0f29  */
    /* JADX WARN: Removed duplicated region for block: B:694:0x055b  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x11c1  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x11d9  */
    /* JADX WARN: Removed duplicated region for block: B:771:0x0f13  */
    /* JADX WARN: Removed duplicated region for block: B:865:0x0bd4  */
    /* JADX WARN: Removed duplicated region for block: B:875:0x0c1f  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x1336  */
    /* JADX WARN: Removed duplicated region for block: B:880:0x0c30  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x1343 A[Catch: Exception -> 0x13e6, TRY_LEAVE, TryCatch #5 {Exception -> 0x13e6, blocks: (B:85:0x132d, B:88:0x1337, B:90:0x1343), top: B:84:0x132d }] */
    /* JADX WARN: Removed duplicated region for block: B:957:0x0b1a A[Catch: Exception -> 0x0b2d, TryCatch #1 {Exception -> 0x0b2d, blocks: (B:955:0x0b0f, B:957:0x0b1a, B:958:0x0b22), top: B:954:0x0b0f }] */
    /* JADX WARN: Removed duplicated region for block: B:960:0x0b21  */
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
        TLRPC$Chat chat;
        TLRPC$User tLRPC$User;
        boolean z3;
        TLRPC$Chat tLRPC$Chat;
        boolean isEmpty;
        String str3;
        String str4;
        String str5;
        MessageObject messageObject;
        String str6;
        TLRPC$Message tLRPC$Message;
        CharSequence charSequence;
        CharSequence charSequence2;
        CharSequence charSequence3;
        int i3;
        CharSequence replaceNewLines;
        CharSequence charSequence4;
        CharSequence charSequence5;
        String str7;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        String str8;
        SpannableStringBuilder valueOf;
        TLRPC$Message tLRPC$Message2;
        char c;
        String charSequence6;
        SpannableStringBuilder formatSpannable;
        String str9;
        TLRPC$Message tLRPC$Message3;
        int i4;
        int i5;
        SpannableStringBuilder replaceEmoji;
        String str10;
        CharSequence highlightText;
        ForegroundColorSpanThemable foregroundColorSpanThemable;
        String string;
        int i6;
        String str11;
        String str12;
        String str13;
        ArrayList<TLRPC$MessageEntity> arrayList;
        SpannableStringBuilder spannableStringBuilder;
        SpannableStringBuilder spannableStringBuilder2;
        TLRPC$DraftMessage tLRPC$DraftMessage;
        MessageObject messageObject2;
        String stringForMessageListDate;
        MessageObject messageObject3;
        boolean z4;
        boolean z5;
        String str14;
        String str15;
        boolean z6;
        boolean z7;
        String str16;
        String str17;
        String str18;
        CharSequence charSequence7;
        int i7;
        String str19;
        String str20;
        String str21;
        String userName;
        CharSequence charSequence8;
        int i8;
        String string2;
        MessageObject messageObject4;
        CharSequence charSequence9;
        int i9;
        CharSequence charSequence10;
        int i10;
        int dp;
        int dp2;
        int dp3;
        int i11;
        int i12;
        int max;
        boolean z8;
        MessageObject messageObject5;
        CharSequence highlightText2;
        int lineCount;
        StaticLayout staticLayout;
        StaticLayout staticLayout2;
        float primaryHorizontal;
        float primaryHorizontal2;
        int i13;
        int length;
        int ceil;
        int lineCount2;
        boolean z9;
        CharacterStyle[] characterStyleArr;
        CharSequence replaceTwoNewLinesToOne;
        CharSequence highlightText3;
        int dp4;
        int dp5;
        int dp6;
        CharSequence highlightText4;
        CharSequence charSequence11;
        String str22;
        SpannableStringBuilder valueOf2;
        int i14 = 1;
        int i15 = 0;
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
        this.hasMessageThumb = false;
        this.nameLayoutEllipsizeByGradient = false;
        boolean z10 = !UserObject.isUserSelf(this.user) && !this.useMeForMyMessages;
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
        MessageObject messageObject6 = this.message;
        CharSequence charSequence12 = messageObject6 != null ? messageObject6.messageText : null;
        boolean z11 = charSequence12 instanceof Spannable;
        SpannableStringBuilder spannableStringBuilder3 = charSequence12;
        if (z11) {
            SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder(charSequence12);
            for (URLSpanNoUnderlineBold uRLSpanNoUnderlineBold : (URLSpanNoUnderlineBold[]) spannableStringBuilder4.getSpans(0, spannableStringBuilder4.length(), URLSpanNoUnderlineBold.class)) {
                spannableStringBuilder4.removeSpan(uRLSpanNoUnderlineBold);
            }
            for (URLSpanNoUnderline uRLSpanNoUnderline : (URLSpanNoUnderline[]) spannableStringBuilder4.getSpans(0, spannableStringBuilder4.length(), URLSpanNoUnderline.class)) {
                spannableStringBuilder4.removeSpan(uRLSpanNoUnderline);
            }
            spannableStringBuilder3 = spannableStringBuilder4;
        }
        this.lastMessageString = spannableStringBuilder3;
        CustomDialog customDialog = this.customDialog;
        if (customDialog != null) {
            if (customDialog.type == 2) {
                this.drawNameLock = true;
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    this.nameLockTop = AndroidUtilities.dp(12.5f);
                    if (!LocaleController.isRTL) {
                        this.nameLockLeft = AndroidUtilities.dp(78.0f);
                        this.nameLeft = AndroidUtilities.dp(82.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                    } else {
                        this.nameLockLeft = (getMeasuredWidth() - AndroidUtilities.dp(78.0f)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        this.nameLeft = AndroidUtilities.dp(22.0f);
                    }
                } else {
                    this.nameLockTop = AndroidUtilities.dp(16.5f);
                    if (!LocaleController.isRTL) {
                        this.nameLockLeft = AndroidUtilities.dp(76.0f);
                        this.nameLeft = AndroidUtilities.dp(80.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                    } else {
                        this.nameLockLeft = (getMeasuredWidth() - AndroidUtilities.dp(76.0f)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        this.nameLeft = AndroidUtilities.dp(18.0f);
                    }
                }
            } else {
                this.drawVerified = customDialog.verified;
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    if (!LocaleController.isRTL) {
                        this.nameLeft = AndroidUtilities.dp(78.0f);
                    } else {
                        this.nameLeft = AndroidUtilities.dp(22.0f);
                    }
                } else if (!LocaleController.isRTL) {
                    this.nameLeft = AndroidUtilities.dp(76.0f);
                } else {
                    this.nameLeft = AndroidUtilities.dp(18.0f);
                }
            }
            CustomDialog customDialog2 = this.customDialog;
            if (customDialog2.type == 1) {
                charSequence5 = LocaleController.getString("FromYou", R.string.FromYou);
                CustomDialog customDialog3 = this.customDialog;
                if (customDialog3.isMedia) {
                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                    valueOf2 = SpannableStringBuilder.valueOf(String.format(str, this.message.messageText));
                    valueOf2.setSpan(new ForegroundColorSpanThemable("chats_attachMessage", this.resourcesProvider), 0, valueOf2.length(), 33);
                } else {
                    String str23 = customDialog3.message;
                    if (str23.length() > 150) {
                        str23 = str23.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                    }
                    valueOf2 = (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? SpannableStringBuilder.valueOf(String.format(str, str23, charSequence5)) : SpannableStringBuilder.valueOf(String.format(str, str23.replace('\n', ' '), charSequence5));
                }
                charSequence11 = Emoji.replaceEmoji(valueOf2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                i8 = 0;
            } else {
                charSequence11 = customDialog2.message;
                if (customDialog2.isMedia) {
                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                }
                charSequence5 = null;
                i8 = 1;
            }
            str20 = LocaleController.stringForMessageListDate(this.customDialog.date);
            int i16 = this.customDialog.unread_count;
            if (i16 != 0) {
                this.drawCount = true;
                str22 = String.format("%d", Integer.valueOf(i16));
            } else {
                this.drawCount = false;
                str22 = null;
            }
            CustomDialog customDialog4 = this.customDialog;
            if (customDialog4.sent) {
                this.drawCheck1 = true;
                this.drawCheck2 = true;
            } else {
                this.drawCheck1 = false;
                this.drawCheck2 = false;
            }
            this.drawClock = false;
            this.drawError = false;
            str21 = customDialog4.name;
            charSequence8 = charSequence11;
            str18 = str22;
            i6 = -1;
            str15 = null;
        } else {
            if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                if (!LocaleController.isRTL) {
                    this.nameLeft = AndroidUtilities.dp(78.0f);
                } else {
                    this.nameLeft = AndroidUtilities.dp(22.0f);
                }
            } else if (!LocaleController.isRTL) {
                this.nameLeft = AndroidUtilities.dp(76.0f);
            } else {
                this.nameLeft = AndroidUtilities.dp(18.0f);
            }
            if (this.encryptedChat != null) {
                if (this.currentDialogFolderId == 0) {
                    this.drawNameLock = true;
                    if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                        this.nameLockTop = AndroidUtilities.dp(12.5f);
                        if (!LocaleController.isRTL) {
                            this.nameLockLeft = AndroidUtilities.dp(78.0f);
                            this.nameLeft = AndroidUtilities.dp(82.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        } else {
                            this.nameLockLeft = (getMeasuredWidth() - AndroidUtilities.dp(78.0f)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                            this.nameLeft = AndroidUtilities.dp(22.0f);
                        }
                    } else {
                        this.nameLockTop = AndroidUtilities.dp(16.5f);
                        if (!LocaleController.isRTL) {
                            this.nameLockLeft = AndroidUtilities.dp(76.0f);
                            this.nameLeft = AndroidUtilities.dp(80.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        } else {
                            this.nameLockLeft = (getMeasuredWidth() - AndroidUtilities.dp(76.0f)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
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
                            str2 = str;
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
                                if (i2 == 0 && (messageObject4 = this.message) != null) {
                                    i2 = messageObject4.messageOwner.date;
                                }
                                if (this.isDialogCell) {
                                    TLRPC$DraftMessage draft = MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, 0);
                                    this.draftMessage = draft;
                                    if (draft == null || ((!TextUtils.isEmpty(draft.message) || this.draftMessage.reply_to_msg_id != 0) && (i2 <= this.draftMessage.date || this.unreadCount == 0))) {
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
                                        if (tLRPC$Chat4 != null) {
                                            if (!tLRPC$Chat4.left) {
                                            }
                                        }
                                    }
                                    this.draftMessage = null;
                                } else {
                                    this.draftMessage = null;
                                }
                                if (printingString != null) {
                                    this.lastPrintString = printingString;
                                    int intValue = MessagesController.getInstance(this.currentAccount).getPrintingStringType(this.currentDialogId, 0).intValue();
                                    this.printingStringType = intValue;
                                    StatusDrawable chatStatusDrawable = Theme.getChatStatusDrawable(intValue);
                                    int intrinsicWidth = chatStatusDrawable != null ? chatStatusDrawable.getIntrinsicWidth() + AndroidUtilities.dp(3.0f) : 0;
                                    SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder();
                                    CharSequence replace = TextUtils.replace(printingString, new String[]{"..."}, new String[]{""});
                                    i6 = this.printingStringType == 5 ? replace.toString().indexOf("**oo**") : -1;
                                    if (i6 >= 0) {
                                        spannableStringBuilder5.append(replace).setSpan(new FixedWidthSpan(Theme.getChatStatusDrawable(this.printingStringType).getIntrinsicWidth()), i6, i6 + 6, 0);
                                    } else {
                                        spannableStringBuilder5.append((CharSequence) " ").append(replace).setSpan(new FixedWidthSpan(intrinsicWidth), 0, 1, 0);
                                    }
                                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                    charSequence5 = null;
                                    i3 = 0;
                                    i14 = 0;
                                    i15 = 1;
                                    spannableStringBuilder2 = spannableStringBuilder5;
                                } else {
                                    this.lastPrintString = null;
                                    if (this.draftMessage != null) {
                                        charSequence5 = LocaleController.getString("Draft", R.string.Draft);
                                        if (TextUtils.isEmpty(this.draftMessage.message)) {
                                            if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                                spannableStringBuilder = "";
                                            } else {
                                                SpannableStringBuilder valueOf3 = SpannableStringBuilder.valueOf(charSequence5);
                                                valueOf3.setSpan(new ForegroundColorSpanThemable("chats_draft", this.resourcesProvider), 0, charSequence5.length(), 33);
                                                spannableStringBuilder = valueOf3;
                                            }
                                        } else {
                                            String str24 = this.draftMessage.message;
                                            if (str24.length() > 150) {
                                                str24 = str24.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                            }
                                            SpannableStringBuilder spannableStringBuilder6 = new SpannableStringBuilder(str24);
                                            MediaDataController.addTextStyleRuns(this.draftMessage, spannableStringBuilder6, 256);
                                            TLRPC$DraftMessage tLRPC$DraftMessage2 = this.draftMessage;
                                            if (tLRPC$DraftMessage2 != null && (arrayList = tLRPC$DraftMessage2.entities) != null) {
                                                MediaDataController.addAnimatedEmojiSpans(arrayList, spannableStringBuilder6, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                            }
                                            SpannableStringBuilder formatSpannable2 = AndroidUtilities.formatSpannable(str2, AndroidUtilities.replaceNewLines(spannableStringBuilder6), charSequence5);
                                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                                formatSpannable2.setSpan(new ForegroundColorSpanThemable("chats_draft", this.resourcesProvider), 0, charSequence5.length() + 1, 33);
                                            }
                                            spannableStringBuilder = Emoji.replaceEmoji(formatSpannable2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                        }
                                        i3 = 0;
                                        i14 = 0;
                                        str13 = spannableStringBuilder;
                                    } else {
                                        String str25 = str2;
                                        if (this.clearingDialog) {
                                            textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                            str11 = LocaleController.getString("HistoryCleared", R.string.HistoryCleared);
                                        } else {
                                            MessageObject messageObject7 = this.message;
                                            if (messageObject7 == null) {
                                                TLRPC$EncryptedChat tLRPC$EncryptedChat = this.encryptedChat;
                                                if (tLRPC$EncryptedChat != null) {
                                                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                    if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatRequested) {
                                                        str11 = LocaleController.getString("EncryptionProcessing", R.string.EncryptionProcessing);
                                                    } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatWaiting) {
                                                        str11 = LocaleController.formatString("AwaitingEncryption", R.string.AwaitingEncryption, UserObject.getFirstName(this.user));
                                                    } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatDiscarded) {
                                                        str11 = LocaleController.getString("EncryptionRejected", R.string.EncryptionRejected);
                                                    } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat) {
                                                        if (tLRPC$EncryptedChat.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                            str11 = LocaleController.formatString("EncryptedChatStartedOutgoing", R.string.EncryptedChatStartedOutgoing, UserObject.getFirstName(this.user));
                                                        } else {
                                                            str11 = LocaleController.getString("EncryptedChatStartedIncoming", R.string.EncryptedChatStartedIncoming);
                                                        }
                                                    }
                                                } else if (this.dialogsType == 3 && UserObject.isUserSelf(this.user)) {
                                                    charSequence5 = null;
                                                    i3 = 0;
                                                    z10 = false;
                                                    str12 = LocaleController.getString("SavedMessagesInfo", R.string.SavedMessagesInfo);
                                                    i6 = -1;
                                                    spannableStringBuilder2 = str12;
                                                }
                                                str11 = "";
                                            } else {
                                                String restrictionReason = MessagesController.getRestrictionReason(messageObject7.messageOwner.restriction_reason);
                                                long fromChatId = this.message.getFromChatId();
                                                if (DialogObject.isUserDialog(fromChatId)) {
                                                    tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(fromChatId));
                                                    chat = null;
                                                } else {
                                                    chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-fromChatId));
                                                    tLRPC$User = null;
                                                }
                                                this.drawCount2 = true;
                                                int i17 = this.dialogsType;
                                                if (i17 == 2) {
                                                    TLRPC$Chat tLRPC$Chat5 = this.chat;
                                                    if (tLRPC$Chat5 != null) {
                                                        if (ChatObject.isChannel(tLRPC$Chat5)) {
                                                            TLRPC$Chat tLRPC$Chat6 = this.chat;
                                                            if (!tLRPC$Chat6.megagroup) {
                                                                int i18 = tLRPC$Chat6.participants_count;
                                                                if (i18 != 0) {
                                                                    string = LocaleController.formatPluralStringComma("Subscribers", i18);
                                                                } else if (TextUtils.isEmpty(tLRPC$Chat6.username)) {
                                                                    string = LocaleController.getString("ChannelPrivate", R.string.ChannelPrivate).toLowerCase();
                                                                } else {
                                                                    string = LocaleController.getString("ChannelPublic", R.string.ChannelPublic).toLowerCase();
                                                                }
                                                            }
                                                        }
                                                        TLRPC$Chat tLRPC$Chat7 = this.chat;
                                                        int i19 = tLRPC$Chat7.participants_count;
                                                        if (i19 != 0) {
                                                            string = LocaleController.formatPluralStringComma("Members", i19);
                                                        } else if (tLRPC$Chat7.has_geo) {
                                                            string = LocaleController.getString("MegaLocation", R.string.MegaLocation);
                                                        } else if (TextUtils.isEmpty(tLRPC$Chat7.username)) {
                                                            string = LocaleController.getString("MegaPrivate", R.string.MegaPrivate).toLowerCase();
                                                        } else {
                                                            string = LocaleController.getString("MegaPublic", R.string.MegaPublic).toLowerCase();
                                                        }
                                                    } else {
                                                        string = "";
                                                    }
                                                    this.drawCount2 = false;
                                                } else if (i17 == 3 && UserObject.isUserSelf(this.user)) {
                                                    string = LocaleController.getString("SavedMessagesInfo", R.string.SavedMessagesInfo);
                                                } else {
                                                    if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout && this.currentDialogFolderId != 0) {
                                                        str10 = formatArchivedDialogNames();
                                                        charSequence5 = null;
                                                        i3 = 0;
                                                    } else {
                                                        if (this.message.messageOwner instanceof TLRPC$TL_messageService) {
                                                            if (ChatObject.isChannelAndNotMegaGroup(this.chat) && (this.message.messageOwner.action instanceof TLRPC$TL_messageActionChannelMigrateFrom)) {
                                                                spannableStringBuilder3 = "";
                                                                z10 = false;
                                                            }
                                                            textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                            charSequence2 = spannableStringBuilder3;
                                                            charSequence3 = null;
                                                            i3 = 0;
                                                        } else {
                                                            if (TextUtils.isEmpty(restrictionReason) && this.currentDialogFolderId == 0 && this.encryptedChat == null && !this.message.needDrawBluredPreview() && (this.message.isPhoto() || this.message.isNewGif() || this.message.isVideo())) {
                                                                String str26 = this.message.isWebpage() ? this.message.messageOwner.media.webpage.type : null;
                                                                if (!"app".equals(str26) && !"profile".equals(str26) && !"article".equals(str26) && (str26 == null || !str26.startsWith("telegram_"))) {
                                                                    TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, 40);
                                                                    TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(this.message.photoThumbs, AndroidUtilities.getPhotoSize());
                                                                    if (closestPhotoSizeWithSize == closestPhotoSizeWithSize2) {
                                                                        closestPhotoSizeWithSize2 = null;
                                                                    }
                                                                    if (closestPhotoSizeWithSize != null) {
                                                                        this.hasMessageThumb = true;
                                                                        this.drawPlay = this.message.isVideo();
                                                                        String attachFileName = FileLoader.getAttachFileName(closestPhotoSizeWithSize2);
                                                                        if (this.message.mediaExists || DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.message) || FileLoader.getInstance(this.currentAccount).isLoadingFile(attachFileName)) {
                                                                            MessageObject messageObject8 = this.message;
                                                                            this.thumbImage.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize2, messageObject8.photoThumbsObject), "20_20", ImageLocation.getForObject(closestPhotoSizeWithSize, this.message.photoThumbsObject), "20_20", (messageObject8.type != 1 || closestPhotoSizeWithSize2 == null) ? 0 : closestPhotoSizeWithSize2.size, null, this.message, 0);
                                                                        } else {
                                                                            this.thumbImage.setImage((ImageLocation) null, (String) null, ImageLocation.getForObject(closestPhotoSizeWithSize, this.message.photoThumbsObject), "20_20", (Drawable) null, this.message, 0);
                                                                        }
                                                                        z3 = false;
                                                                        tLRPC$Chat = this.chat;
                                                                        if (tLRPC$Chat == null && tLRPC$Chat.id > 0 && chat == null && (!ChatObject.isChannel(tLRPC$Chat) || ChatObject.isMegagroup(this.chat))) {
                                                                            if (this.message.isOutOwner()) {
                                                                                str7 = LocaleController.getString("FromYou", R.string.FromYou);
                                                                            } else {
                                                                                MessageObject messageObject9 = this.message;
                                                                                if (messageObject9 != null && (tLRPC$MessageFwdHeader = messageObject9.messageOwner.fwd_from) != null && (str8 = tLRPC$MessageFwdHeader.from_name) != null) {
                                                                                    str7 = str8;
                                                                                } else if (tLRPC$User == null) {
                                                                                    str7 = "DELETED";
                                                                                } else if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                                                                    if (UserObject.isDeleted(tLRPC$User)) {
                                                                                        str7 = LocaleController.getString("HiddenName", R.string.HiddenName);
                                                                                    } else {
                                                                                        str7 = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name).replace("\n", "");
                                                                                    }
                                                                                } else {
                                                                                    str7 = UserObject.getFirstName(tLRPC$User).replace("\n", "");
                                                                                }
                                                                            }
                                                                            if (!TextUtils.isEmpty(restrictionReason)) {
                                                                                valueOf = SpannableStringBuilder.valueOf(String.format(str25, restrictionReason, str7));
                                                                            } else {
                                                                                MessageObject messageObject10 = this.message;
                                                                                CharSequence charSequence13 = messageObject10.caption;
                                                                                if (charSequence13 != null) {
                                                                                    CharSequence charSequence14 = charSequence13.toString();
                                                                                    if (!z3) {
                                                                                        str9 = "";
                                                                                    } else if (this.message.isVideo()) {
                                                                                        str9 = "📹 ";
                                                                                    } else if (this.message.isVoice()) {
                                                                                        str9 = "🎤 ";
                                                                                    } else if (this.message.isMusic()) {
                                                                                        str9 = "🎧 ";
                                                                                    } else {
                                                                                        str9 = this.message.isPhoto() ? "🖼 " : "📎 ";
                                                                                    }
                                                                                    if (this.message.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                        String str27 = this.message.messageTrimmedToHighlight;
                                                                                        int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(119.0f);
                                                                                        if (z) {
                                                                                            if (!TextUtils.isEmpty(str7)) {
                                                                                                measuredWidth = (int) (measuredWidth - textPaint5.measureText(str7.toString()));
                                                                                            }
                                                                                            measuredWidth = (int) (measuredWidth - textPaint5.measureText(": "));
                                                                                        }
                                                                                        if (measuredWidth > 0) {
                                                                                            str27 = AndroidUtilities.ellipsizeCenterEnd(str27, this.message.highlightedWords.get(0), measuredWidth, textPaint5, 130).toString();
                                                                                        }
                                                                                        valueOf = new SpannableStringBuilder(str9).append((CharSequence) str27);
                                                                                    } else {
                                                                                        if (charSequence14.length() > 150) {
                                                                                            charSequence14 = charSequence14.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                        }
                                                                                        SpannableStringBuilder spannableStringBuilder7 = new SpannableStringBuilder(charSequence14);
                                                                                        MediaDataController.addTextStyleRuns(this.message.messageOwner.entities, charSequence14, spannableStringBuilder7, 256);
                                                                                        MessageObject messageObject11 = this.message;
                                                                                        if (messageObject11 != null && (tLRPC$Message3 = messageObject11.messageOwner) != null) {
                                                                                            MediaDataController.addAnimatedEmojiSpans(tLRPC$Message3.entities, spannableStringBuilder7, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                        }
                                                                                        valueOf = AndroidUtilities.formatSpannable(str25, new SpannableStringBuilder(str9).append(AndroidUtilities.replaceNewLines(spannableStringBuilder7)), str7);
                                                                                    }
                                                                                } else if (messageObject10.messageOwner.media != null && !messageObject10.isMediaEmpty()) {
                                                                                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                    MessageObject messageObject12 = this.message;
                                                                                    TLRPC$MessageMedia tLRPC$MessageMedia = messageObject12.messageOwner.media;
                                                                                    if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                                                                                        TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia;
                                                                                        charSequence6 = Build.VERSION.SDK_INT >= 18 ? String.format("📊 \u2068%s\u2069", tLRPC$TL_messageMediaPoll.poll.question) : String.format("📊 %s", tLRPC$TL_messageMediaPoll.poll.question);
                                                                                    } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                                                                                        charSequence6 = Build.VERSION.SDK_INT >= 18 ? String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title) : String.format("🎮 %s", tLRPC$MessageMedia.game.title);
                                                                                    } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                        charSequence6 = tLRPC$MessageMedia.title;
                                                                                    } else {
                                                                                        if (messageObject12.type == 14) {
                                                                                            if (Build.VERSION.SDK_INT >= 18) {
                                                                                                c = 1;
                                                                                                charSequence6 = String.format("🎧 \u2068%s - %s\u2069", messageObject12.getMusicAuthor(), this.message.getMusicTitle());
                                                                                            } else {
                                                                                                c = 1;
                                                                                                charSequence6 = String.format("🎧 %s - %s", messageObject12.getMusicAuthor(), this.message.getMusicTitle());
                                                                                            }
                                                                                        } else {
                                                                                            c = 1;
                                                                                            charSequence6 = spannableStringBuilder3.toString();
                                                                                        }
                                                                                        CharSequence[] charSequenceArr = new CharSequence[2];
                                                                                        charSequenceArr[0] = charSequence6.replace('\n', ' ');
                                                                                        charSequenceArr[c] = str7;
                                                                                        formatSpannable = AndroidUtilities.formatSpannable(str25, charSequenceArr);
                                                                                        formatSpannable.setSpan(new ForegroundColorSpanThemable("chats_attachMessage", this.resourcesProvider), !z ? str7.length() + 2 : 0, formatSpannable.length(), 33);
                                                                                        if ((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && (this.currentDialogFolderId == 0 || formatSpannable.length() <= 0)) {
                                                                                            i4 = 0;
                                                                                            i5 = 0;
                                                                                        } else {
                                                                                            try {
                                                                                                foregroundColorSpanThemable = new ForegroundColorSpanThemable("chats_nameMessage", this.resourcesProvider);
                                                                                                i5 = str7.length() + 1;
                                                                                            } catch (Exception e) {
                                                                                                e = e;
                                                                                                i5 = 0;
                                                                                            }
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
                                                                                                if (this.hasMessageThumb) {
                                                                                                }
                                                                                                i3 = i4;
                                                                                                charSequence5 = str7;
                                                                                                i14 = 1;
                                                                                                i15 = 0;
                                                                                                str10 = replaceEmoji;
                                                                                                if (this.currentDialogFolderId != 0) {
                                                                                                }
                                                                                                i6 = -1;
                                                                                                int i20 = i15;
                                                                                                i15 = i14;
                                                                                                i14 = i20;
                                                                                                spannableStringBuilder2 = str10;
                                                                                                if (this.draftMessage == null) {
                                                                                                }
                                                                                                messageObject3 = this.message;
                                                                                                if (messageObject3 != null) {
                                                                                                }
                                                                                                this.promoDialog = z6;
                                                                                                MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                                                                                                str18 = str14;
                                                                                                if (this.dialogsType != 0) {
                                                                                                }
                                                                                                str19 = charSequence7;
                                                                                                str20 = stringForMessageListDate;
                                                                                                if (this.currentDialogFolderId == 0) {
                                                                                                }
                                                                                                charSequence8 = str19;
                                                                                                i8 = i14;
                                                                                                i14 = i15;
                                                                                                i15 = i7;
                                                                                                charSequence9 = charSequence5;
                                                                                                if (i14 == 0) {
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
                                                                                                String replace2 = str21.replace('\n', ' ');
                                                                                                if (!this.nameLayoutEllipsizeByGradient) {
                                                                                                }
                                                                                                CharSequence replaceEmoji2 = Emoji.replaceEmoji(TextUtils.ellipsize(replace2, Theme.dialogs_namePaint[this.paintIndex], dp6, TextUtils.TruncateAt.END), Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                MessageObject messageObject13 = this.message;
                                                                                                StaticLayout staticLayout3 = new StaticLayout((messageObject13 != null || !messageObject13.hasHighlightedWords() || (highlightText4 = AndroidUtilities.highlightText(replaceEmoji2, this.message.highlightedWords, this.resourcesProvider)) == null) ? replaceEmoji2 : highlightText4, Theme.dialogs_namePaint[this.paintIndex], Math.max(dp6, this.nameWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                                                                                                this.nameLayout = staticLayout3;
                                                                                                this.nameLayoutTranslateX = (this.nameLayoutEllipsizeByGradient || !staticLayout3.isRtlCharAt(0)) ? 0.0f : -AndroidUtilities.dp(36.0f);
                                                                                                this.nameLayoutEllipsizeLeft = this.nameLayout.isRtlCharAt(0);
                                                                                                if (!this.useForceThreeLines) {
                                                                                                }
                                                                                                i10 = i6;
                                                                                                dp = AndroidUtilities.dp(11.0f);
                                                                                                this.messageNameTop = AndroidUtilities.dp(32.0f);
                                                                                                this.timeTop = AndroidUtilities.dp(13.0f);
                                                                                                this.errorTop = AndroidUtilities.dp(43.0f);
                                                                                                this.pinTop = AndroidUtilities.dp(43.0f);
                                                                                                this.countTop = AndroidUtilities.dp(43.0f);
                                                                                                this.checkDrawTop = AndroidUtilities.dp(13.0f);
                                                                                                int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp(93.0f);
                                                                                                if (LocaleController.isRTL) {
                                                                                                }
                                                                                                i11 = measuredWidth2;
                                                                                                this.avatarImage.setImageCoords(dp2, dp, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                                                                                                this.thumbImage.setImageCoords(dp3, AndroidUtilities.dp(31.0f) + dp, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                                                                                                i12 = dp;
                                                                                                if (this.drawPin) {
                                                                                                }
                                                                                                if (this.drawError) {
                                                                                                }
                                                                                                int i21 = i11;
                                                                                                if (i8 != 0) {
                                                                                                }
                                                                                                max = Math.max(AndroidUtilities.dp(12.0f), i21);
                                                                                                z8 = this.useForceThreeLines;
                                                                                                if (!z8) {
                                                                                                }
                                                                                                try {
                                                                                                    messageObject5 = this.message;
                                                                                                    if (messageObject5 != null) {
                                                                                                        charSequence9 = highlightText2;
                                                                                                    }
                                                                                                    this.messageNameLayout = StaticLayoutEx.createStaticLayout(charSequence9, Theme.dialogs_messageNamePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max, 1);
                                                                                                } catch (Exception e3) {
                                                                                                    FileLog.e(e3);
                                                                                                }
                                                                                                this.messageTop = AndroidUtilities.dp(51.0f);
                                                                                                this.thumbImage.setImageY(i12 + AndroidUtilities.dp(40.0f));
                                                                                                z9 = this.useForceThreeLines;
                                                                                                if (!z9) {
                                                                                                }
                                                                                                textPaint5 = Theme.dialogs_messagePaint[this.paintIndex];
                                                                                                charSequence8 = charSequence9;
                                                                                                charSequence9 = null;
                                                                                                if (charSequence8 instanceof Spannable) {
                                                                                                }
                                                                                                if (!this.useForceThreeLines) {
                                                                                                    if (this.hasMessageThumb) {
                                                                                                    }
                                                                                                    this.messageLayout = new StaticLayout(charSequence8, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                                                                                                    this.spoilersPool.addAll(this.spoilers);
                                                                                                    this.spoilers.clear();
                                                                                                    SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
                                                                                                    this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                                                                                                    if (LocaleController.isRTL) {
                                                                                                    }
                                                                                                    staticLayout = this.messageLayout;
                                                                                                    if (staticLayout != null) {
                                                                                                    }
                                                                                                    staticLayout2 = this.messageLayout;
                                                                                                    if (staticLayout2 == null) {
                                                                                                    }
                                                                                                }
                                                                                                if (this.hasMessageThumb) {
                                                                                                    max += AndroidUtilities.dp(6.0f);
                                                                                                }
                                                                                                this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence8, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence9 != null ? 1 : 2);
                                                                                                this.spoilersPool.addAll(this.spoilers);
                                                                                                this.spoilers.clear();
                                                                                                SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
                                                                                                this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                                                                                                if (LocaleController.isRTL) {
                                                                                                }
                                                                                                staticLayout = this.messageLayout;
                                                                                                if (staticLayout != null) {
                                                                                                }
                                                                                                staticLayout2 = this.messageLayout;
                                                                                                if (staticLayout2 == null) {
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        replaceEmoji = Emoji.replaceEmoji(formatSpannable, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                        if (this.message.hasHighlightedWords() && (highlightText = AndroidUtilities.highlightText(replaceEmoji, this.message.highlightedWords, this.resourcesProvider)) != null) {
                                                                                            replaceEmoji = highlightText;
                                                                                        }
                                                                                        if (this.hasMessageThumb) {
                                                                                            boolean z12 = replaceEmoji instanceof SpannableStringBuilder;
                                                                                            replaceEmoji = replaceEmoji;
                                                                                            if (!z12) {
                                                                                                replaceEmoji = new SpannableStringBuilder(replaceEmoji);
                                                                                            }
                                                                                            SpannableStringBuilder spannableStringBuilder8 = (SpannableStringBuilder) replaceEmoji;
                                                                                            if (i5 >= spannableStringBuilder8.length()) {
                                                                                                spannableStringBuilder8.append((CharSequence) " ");
                                                                                                spannableStringBuilder8.setSpan(new FixedWidthSpan(AndroidUtilities.dp(i + 6)), spannableStringBuilder8.length() - 1, spannableStringBuilder8.length(), 33);
                                                                                            } else {
                                                                                                spannableStringBuilder8.insert(i5, (CharSequence) " ");
                                                                                                spannableStringBuilder8.setSpan(new FixedWidthSpan(AndroidUtilities.dp(i + 6)), i5, i5 + 1, 33);
                                                                                            }
                                                                                        }
                                                                                        i3 = i4;
                                                                                        charSequence5 = str7;
                                                                                        i14 = 1;
                                                                                        i15 = 0;
                                                                                        str10 = replaceEmoji;
                                                                                    }
                                                                                    c = 1;
                                                                                    CharSequence[] charSequenceArr2 = new CharSequence[2];
                                                                                    charSequenceArr2[0] = charSequence6.replace('\n', ' ');
                                                                                    charSequenceArr2[c] = str7;
                                                                                    formatSpannable = AndroidUtilities.formatSpannable(str25, charSequenceArr2);
                                                                                    formatSpannable.setSpan(new ForegroundColorSpanThemable("chats_attachMessage", this.resourcesProvider), !z ? str7.length() + 2 : 0, formatSpannable.length(), 33);
                                                                                    if (!this.useForceThreeLines) {
                                                                                    }
                                                                                    i4 = 0;
                                                                                    i5 = 0;
                                                                                    replaceEmoji = Emoji.replaceEmoji(formatSpannable, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                    if (this.message.hasHighlightedWords()) {
                                                                                        replaceEmoji = highlightText;
                                                                                    }
                                                                                    if (this.hasMessageThumb) {
                                                                                    }
                                                                                    i3 = i4;
                                                                                    charSequence5 = str7;
                                                                                    i14 = 1;
                                                                                    i15 = 0;
                                                                                    str10 = replaceEmoji;
                                                                                } else {
                                                                                    MessageObject messageObject14 = this.message;
                                                                                    CharSequence charSequence15 = messageObject14.messageOwner.message;
                                                                                    if (charSequence15 != null) {
                                                                                        if (messageObject14.hasHighlightedWords()) {
                                                                                            String str28 = this.message.messageTrimmedToHighlight;
                                                                                            if (str28 != null) {
                                                                                                charSequence15 = str28;
                                                                                            }
                                                                                            int measuredWidth3 = getMeasuredWidth() - AndroidUtilities.dp(105.0f);
                                                                                            if (z) {
                                                                                                if (!TextUtils.isEmpty(str7)) {
                                                                                                    measuredWidth3 = (int) (measuredWidth3 - textPaint5.measureText(str7.toString()));
                                                                                                }
                                                                                                measuredWidth3 = (int) (measuredWidth3 - textPaint5.measureText(": "));
                                                                                            }
                                                                                            if (measuredWidth3 > 0) {
                                                                                                charSequence15 = AndroidUtilities.ellipsizeCenterEnd(charSequence15, this.message.highlightedWords.get(0), measuredWidth3, textPaint5, 130).toString();
                                                                                            }
                                                                                        } else {
                                                                                            if (charSequence15.length() > 150) {
                                                                                                charSequence15 = charSequence15.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                            }
                                                                                            charSequence15 = AndroidUtilities.replaceNewLines(charSequence15);
                                                                                        }
                                                                                        SpannableStringBuilder spannableStringBuilder9 = new SpannableStringBuilder(charSequence15);
                                                                                        MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder9, 256);
                                                                                        MessageObject messageObject15 = this.message;
                                                                                        if (messageObject15 != null && (tLRPC$Message2 = messageObject15.messageOwner) != null) {
                                                                                            MediaDataController.addAnimatedEmojiSpans(tLRPC$Message2.entities, spannableStringBuilder9, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                        }
                                                                                        valueOf = AndroidUtilities.formatSpannable(str25, spannableStringBuilder9, str7);
                                                                                    } else {
                                                                                        valueOf = SpannableStringBuilder.valueOf("");
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
                                                                            }
                                                                            if (this.hasMessageThumb) {
                                                                            }
                                                                            i3 = i4;
                                                                            charSequence5 = str7;
                                                                            i14 = 1;
                                                                            i15 = 0;
                                                                            str10 = replaceEmoji;
                                                                        } else {
                                                                            isEmpty = TextUtils.isEmpty(restrictionReason);
                                                                            String str29 = restrictionReason;
                                                                            if (isEmpty) {
                                                                                MessageObject messageObject16 = this.message;
                                                                                TLRPC$MessageMedia tLRPC$MessageMedia2 = messageObject16.messageOwner.media;
                                                                                if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia2.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                                                                                    str29 = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
                                                                                } else if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument) && (tLRPC$MessageMedia2.document instanceof TLRPC$TL_documentEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                                                                                    str29 = LocaleController.getString("AttachVideoExpired", R.string.AttachVideoExpired);
                                                                                } else if (messageObject16.caption != null) {
                                                                                    if (!z3) {
                                                                                        str6 = "";
                                                                                    } else if (messageObject16.isVideo()) {
                                                                                        str6 = "📹 ";
                                                                                    } else if (this.message.isVoice()) {
                                                                                        str6 = "🎤 ";
                                                                                    } else if (this.message.isMusic()) {
                                                                                        str6 = "🎧 ";
                                                                                    } else {
                                                                                        str6 = this.message.isPhoto() ? "🖼 " : "📎 ";
                                                                                    }
                                                                                    if (this.message.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                        String str30 = this.message.messageTrimmedToHighlight;
                                                                                        int measuredWidth4 = getMeasuredWidth() - AndroidUtilities.dp(119.0f);
                                                                                        if (z) {
                                                                                            if (!TextUtils.isEmpty(null)) {
                                                                                                throw null;
                                                                                            }
                                                                                            measuredWidth4 = (int) (measuredWidth4 - textPaint5.measureText(": "));
                                                                                        }
                                                                                        if (measuredWidth4 > 0) {
                                                                                            str30 = AndroidUtilities.ellipsizeCenterEnd(str30, this.message.highlightedWords.get(0), measuredWidth4, textPaint5, 130).toString();
                                                                                        }
                                                                                        str29 = str6 + str30;
                                                                                    } else {
                                                                                        SpannableStringBuilder spannableStringBuilder10 = new SpannableStringBuilder(this.message.caption);
                                                                                        MessageObject messageObject17 = this.message;
                                                                                        if (messageObject17 != null && (tLRPC$Message = messageObject17.messageOwner) != null) {
                                                                                            MediaDataController.addTextStyleRuns(tLRPC$Message.entities, messageObject17.caption, spannableStringBuilder10, 256);
                                                                                            MediaDataController.addAnimatedEmojiSpans(this.message.messageOwner.entities, spannableStringBuilder10, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                        }
                                                                                        str29 = new SpannableStringBuilder(str6).append((CharSequence) spannableStringBuilder10);
                                                                                    }
                                                                                } else {
                                                                                    if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPoll) {
                                                                                        str4 = "📊 " + ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia2).poll.question;
                                                                                    } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGame) {
                                                                                        str4 = "🎮 " + this.message.messageOwner.media.game.title;
                                                                                    } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                        str4 = tLRPC$MessageMedia2.title;
                                                                                    } else {
                                                                                        if (messageObject16.type == 14) {
                                                                                            str5 = String.format("🎧 %s - %s", messageObject16.getMusicAuthor(), this.message.getMusicTitle());
                                                                                        } else {
                                                                                            if (messageObject16.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                str3 = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp(95.0f), textPaint5, 130).toString();
                                                                                            } else {
                                                                                                SpannableStringBuilder spannableStringBuilder11 = new SpannableStringBuilder(spannableStringBuilder3);
                                                                                                MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder11, 256);
                                                                                                MessageObject messageObject18 = this.message;
                                                                                                str3 = spannableStringBuilder11;
                                                                                                if (messageObject18 != null) {
                                                                                                    TLRPC$Message tLRPC$Message4 = messageObject18.messageOwner;
                                                                                                    str3 = spannableStringBuilder11;
                                                                                                    if (tLRPC$Message4 != null) {
                                                                                                        MediaDataController.addAnimatedEmojiSpans(tLRPC$Message4.entities, spannableStringBuilder11, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                                        str3 = spannableStringBuilder11;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            AndroidUtilities.highlightText(str3, this.message.highlightedWords, this.resourcesProvider);
                                                                                            str5 = str3;
                                                                                        }
                                                                                        messageObject = this.message;
                                                                                        charSequence = str5;
                                                                                        if (messageObject.messageOwner.media != null) {
                                                                                            charSequence = str5;
                                                                                            if (!messageObject.isMediaEmpty()) {
                                                                                                textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                charSequence = str5;
                                                                                            }
                                                                                        }
                                                                                        if (!this.hasMessageThumb) {
                                                                                            if (this.message.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                replaceNewLines = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp((i + 95) + 6), textPaint5, 130).toString();
                                                                                            } else {
                                                                                                int length2 = charSequence.length();
                                                                                                CharSequence charSequence16 = charSequence;
                                                                                                if (length2 > 150) {
                                                                                                    charSequence16 = charSequence.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                                }
                                                                                                replaceNewLines = AndroidUtilities.replaceNewLines(charSequence16);
                                                                                            }
                                                                                            boolean z13 = replaceNewLines instanceof SpannableStringBuilder;
                                                                                            SpannableStringBuilder spannableStringBuilder12 = replaceNewLines;
                                                                                            if (!z13) {
                                                                                                spannableStringBuilder12 = new SpannableStringBuilder(replaceNewLines);
                                                                                            }
                                                                                            SpannableStringBuilder spannableStringBuilder13 = (SpannableStringBuilder) spannableStringBuilder12;
                                                                                            spannableStringBuilder13.insert(0, (CharSequence) " ");
                                                                                            spannableStringBuilder13.setSpan(new FixedWidthSpan(AndroidUtilities.dp(i + 6)), 0, 1, 33);
                                                                                            Emoji.replaceEmoji(spannableStringBuilder13, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
                                                                                            if (!this.message.hasHighlightedWords() || (charSequence4 = AndroidUtilities.highlightText(spannableStringBuilder13, this.message.highlightedWords, this.resourcesProvider)) == null) {
                                                                                                charSequence4 = spannableStringBuilder12;
                                                                                            }
                                                                                            charSequence5 = null;
                                                                                            i3 = 0;
                                                                                            i14 = 1;
                                                                                            i15 = 0;
                                                                                            str10 = charSequence4;
                                                                                        } else {
                                                                                            charSequence2 = charSequence;
                                                                                            charSequence3 = null;
                                                                                            i3 = 0;
                                                                                            i14 = 1;
                                                                                        }
                                                                                    }
                                                                                    str5 = str4;
                                                                                    messageObject = this.message;
                                                                                    charSequence = str5;
                                                                                    if (messageObject.messageOwner.media != null) {
                                                                                    }
                                                                                    if (!this.hasMessageThumb) {
                                                                                    }
                                                                                }
                                                                            }
                                                                            charSequence = str29;
                                                                            if (!this.hasMessageThumb) {
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            z3 = true;
                                                            tLRPC$Chat = this.chat;
                                                            if (tLRPC$Chat == null) {
                                                            }
                                                            isEmpty = TextUtils.isEmpty(restrictionReason);
                                                            String str292 = restrictionReason;
                                                            if (isEmpty) {
                                                            }
                                                            charSequence = str292;
                                                            if (!this.hasMessageThumb) {
                                                            }
                                                        }
                                                        i15 = 1;
                                                        charSequence5 = charSequence3;
                                                        str10 = charSequence2;
                                                    }
                                                    if (this.currentDialogFolderId != 0) {
                                                        charSequence5 = formatArchivedDialogNames();
                                                    }
                                                    i6 = -1;
                                                    int i202 = i15;
                                                    i15 = i14;
                                                    i14 = i202;
                                                    spannableStringBuilder2 = str10;
                                                }
                                                str10 = string;
                                                charSequence5 = null;
                                                i3 = 0;
                                                i14 = 0;
                                                i15 = 1;
                                                z10 = false;
                                                if (this.currentDialogFolderId != 0) {
                                                }
                                                i6 = -1;
                                                int i2022 = i15;
                                                i15 = i14;
                                                i14 = i2022;
                                                spannableStringBuilder2 = str10;
                                            }
                                        }
                                        charSequence5 = null;
                                        i3 = 0;
                                        str13 = str11;
                                    }
                                    i15 = 1;
                                    str12 = str13;
                                    i6 = -1;
                                    spannableStringBuilder2 = str12;
                                }
                                if (this.draftMessage == null) {
                                    stringForMessageListDate = LocaleController.stringForMessageListDate(tLRPC$DraftMessage.date);
                                } else {
                                    int i22 = this.lastMessageDate;
                                    if (i22 != 0) {
                                        stringForMessageListDate = LocaleController.stringForMessageListDate(i22);
                                    } else {
                                        stringForMessageListDate = this.message != null ? LocaleController.stringForMessageListDate(messageObject2.messageOwner.date) : "";
                                    }
                                }
                                messageObject3 = this.message;
                                if (messageObject3 != null) {
                                    this.drawCheck1 = false;
                                    this.drawCheck2 = false;
                                    this.drawClock = false;
                                    this.drawCount = false;
                                    this.drawMention = false;
                                    this.drawReactionMention = false;
                                    this.drawError = false;
                                    str14 = null;
                                    z6 = false;
                                    str15 = null;
                                } else {
                                    if (this.currentDialogFolderId != 0) {
                                        int i23 = this.unreadCount;
                                        int i24 = this.mentionCount;
                                        if (i23 + i24 <= 0) {
                                            z7 = false;
                                            this.drawCount = false;
                                            this.drawMention = false;
                                            str16 = null;
                                        } else if (i23 > i24) {
                                            this.drawCount = true;
                                            this.drawMention = false;
                                            z7 = false;
                                            str17 = String.format("%d", Integer.valueOf(i23 + i24));
                                            str16 = null;
                                            this.drawReactionMention = z7;
                                            str15 = str16;
                                            str14 = str17;
                                        } else {
                                            this.drawCount = false;
                                            this.drawMention = true;
                                            z7 = false;
                                            str16 = String.format("%d", Integer.valueOf(i23 + i24));
                                        }
                                        str17 = null;
                                        this.drawReactionMention = z7;
                                        str15 = str16;
                                        str14 = str17;
                                    } else {
                                        if (this.clearingDialog) {
                                            this.drawCount = false;
                                            str14 = null;
                                            z10 = false;
                                            z4 = true;
                                            z5 = false;
                                        } else {
                                            int i25 = this.unreadCount;
                                            if (i25 != 0 && (i25 != 1 || i25 != this.mentionCount || messageObject3 == null || !messageObject3.messageOwner.mentioned)) {
                                                z4 = true;
                                                this.drawCount = true;
                                                z5 = false;
                                                str14 = String.format("%d", Integer.valueOf(i25));
                                            } else {
                                                z4 = true;
                                                z5 = false;
                                                if (this.markUnread) {
                                                    this.drawCount = true;
                                                    str14 = "";
                                                } else {
                                                    this.drawCount = false;
                                                    str14 = null;
                                                }
                                            }
                                        }
                                        if (this.mentionCount != 0) {
                                            this.drawMention = z4;
                                            str15 = "@";
                                        } else {
                                            this.drawMention = z5;
                                            str15 = null;
                                        }
                                        if (this.reactionMentionCount > 0) {
                                            this.drawReactionMention = z4;
                                        } else {
                                            this.drawReactionMention = false;
                                        }
                                    }
                                    if (this.message.isOut() && this.draftMessage == null && z10) {
                                        MessageObject messageObject19 = this.message;
                                        if (!(messageObject19.messageOwner.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                                            if (messageObject19.isSending()) {
                                                z6 = false;
                                                this.drawCheck1 = false;
                                                this.drawCheck2 = false;
                                                this.drawClock = true;
                                                this.drawError = false;
                                            } else {
                                                z6 = false;
                                                if (this.message.isSendError()) {
                                                    this.drawCheck1 = false;
                                                    this.drawCheck2 = false;
                                                    this.drawClock = false;
                                                    this.drawError = true;
                                                    this.drawCount = false;
                                                    this.drawMention = false;
                                                } else if (this.message.isSent()) {
                                                    this.drawCheck1 = !this.message.isUnread() || (ChatObject.isChannel(this.chat) && !this.chat.megagroup);
                                                    this.drawCheck2 = true;
                                                    z6 = false;
                                                    this.drawClock = false;
                                                    this.drawError = false;
                                                } else {
                                                    z6 = false;
                                                }
                                            }
                                        }
                                    }
                                    z6 = false;
                                    this.drawCheck1 = false;
                                    this.drawCheck2 = false;
                                    this.drawClock = false;
                                    this.drawError = false;
                                }
                                this.promoDialog = z6;
                                MessagesController messagesController2 = MessagesController.getInstance(this.currentAccount);
                                str18 = str14;
                                if (this.dialogsType != 0) {
                                    charSequence7 = spannableStringBuilder2;
                                    i7 = i3;
                                    if (messagesController2.isPromoDialog(this.currentDialogId, true)) {
                                        this.drawPinBackground = true;
                                        this.promoDialog = true;
                                        int i26 = messagesController2.promoDialogType;
                                        if (i26 == MessagesController.PROMO_TYPE_PROXY) {
                                            string2 = LocaleController.getString("UseProxySponsor", R.string.UseProxySponsor);
                                        } else if (i26 == MessagesController.PROMO_TYPE_PSA) {
                                            string2 = LocaleController.getString("PsaType_" + messagesController2.promoPsaType);
                                            if (TextUtils.isEmpty(string2)) {
                                                string2 = LocaleController.getString("PsaTypeDefault", R.string.PsaTypeDefault);
                                            }
                                            if (!TextUtils.isEmpty(messagesController2.promoPsaMessage)) {
                                                String str31 = messagesController2.promoPsaMessage;
                                                this.hasMessageThumb = false;
                                                str20 = string2;
                                                str19 = str31;
                                                if (this.currentDialogFolderId == 0) {
                                                    str21 = LocaleController.getString("ArchivedChats", R.string.ArchivedChats);
                                                } else {
                                                    TLRPC$Chat tLRPC$Chat8 = this.chat;
                                                    if (tLRPC$Chat8 != null) {
                                                        userName = tLRPC$Chat8.title;
                                                    } else {
                                                        TLRPC$User tLRPC$User3 = this.user;
                                                        if (tLRPC$User3 != null) {
                                                            if (UserObject.isReplyUser(tLRPC$User3)) {
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
                                                            str21 = "";
                                                            if (str21.length() == 0) {
                                                                str21 = LocaleController.getString("HiddenName", R.string.HiddenName);
                                                            }
                                                        }
                                                    }
                                                    str21 = userName;
                                                    if (str21.length() == 0) {
                                                    }
                                                }
                                                charSequence8 = str19;
                                                i8 = i14;
                                                i14 = i15;
                                                i15 = i7;
                                            }
                                        }
                                        str20 = string2;
                                        str19 = charSequence7;
                                        if (this.currentDialogFolderId == 0) {
                                        }
                                        charSequence8 = str19;
                                        i8 = i14;
                                        i14 = i15;
                                        i15 = i7;
                                    }
                                } else {
                                    charSequence7 = spannableStringBuilder2;
                                    i7 = i3;
                                }
                                str19 = charSequence7;
                                str20 = stringForMessageListDate;
                                if (this.currentDialogFolderId == 0) {
                                }
                                charSequence8 = str19;
                                i8 = i14;
                                i14 = i15;
                                i15 = i7;
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
                            i2 = messageObject4.messageOwner.date;
                        }
                        if (this.isDialogCell) {
                        }
                        if (printingString != null) {
                        }
                        if (this.draftMessage == null) {
                        }
                        messageObject3 = this.message;
                        if (messageObject3 != null) {
                        }
                        this.promoDialog = z6;
                        MessagesController messagesController22 = MessagesController.getInstance(this.currentAccount);
                        str18 = str14;
                        if (this.dialogsType != 0) {
                        }
                        str19 = charSequence7;
                        str20 = stringForMessageListDate;
                        if (this.currentDialogFolderId == 0) {
                        }
                        charSequence8 = str19;
                        i8 = i14;
                        i14 = i15;
                        i15 = i7;
                    }
                }
            }
            str2 = str;
            i2 = this.lastMessageDate;
            if (i2 == 0) {
            }
            if (this.isDialogCell) {
            }
            if (printingString != null) {
            }
            if (this.draftMessage == null) {
            }
            messageObject3 = this.message;
            if (messageObject3 != null) {
            }
            this.promoDialog = z6;
            MessagesController messagesController222 = MessagesController.getInstance(this.currentAccount);
            str18 = str14;
            if (this.dialogsType != 0) {
            }
            str19 = charSequence7;
            str20 = stringForMessageListDate;
            if (this.currentDialogFolderId == 0) {
            }
            charSequence8 = str19;
            i8 = i14;
            i14 = i15;
            i15 = i7;
        }
        charSequence9 = charSequence5;
        if (i14 == 0) {
            i9 = (int) Math.ceil(Theme.dialogs_timePaint.measureText(str20));
            this.timeLayout = new StaticLayout(str20, Theme.dialogs_timePaint, i9, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
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
        if (LocaleController.isRTL) {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(14.0f)) - i9;
        } else {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(77.0f)) - i9;
            this.nameLeft += i9;
        }
        if (this.drawNameLock) {
            this.nameWidth -= AndroidUtilities.dp(4.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
        }
        if (!this.drawClock) {
            int intrinsicWidth2 = Theme.dialogs_clockDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            this.nameWidth -= intrinsicWidth2;
            if (!LocaleController.isRTL) {
                this.clockDrawLeft = this.timeLeft - intrinsicWidth2;
            } else {
                this.clockDrawLeft = this.timeLeft + i9 + AndroidUtilities.dp(5.0f);
                this.nameLeft += intrinsicWidth2;
            }
        } else if (this.drawCheck2) {
            int intrinsicWidth3 = Theme.dialogs_checkDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            int i27 = this.nameWidth - intrinsicWidth3;
            this.nameWidth = i27;
            if (this.drawCheck1) {
                this.nameWidth = i27 - (Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0f));
                if (!LocaleController.isRTL) {
                    int i28 = this.timeLeft - intrinsicWidth3;
                    this.halfCheckDrawLeft = i28;
                    this.checkDrawLeft = i28 - AndroidUtilities.dp(5.5f);
                } else {
                    int dp7 = this.timeLeft + i9 + AndroidUtilities.dp(5.0f);
                    this.checkDrawLeft = dp7;
                    this.halfCheckDrawLeft = dp7 + AndroidUtilities.dp(5.5f);
                    this.nameLeft += (intrinsicWidth3 + Theme.dialogs_halfCheckDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(8.0f);
                }
            } else if (!LocaleController.isRTL) {
                this.checkDrawLeft1 = this.timeLeft - intrinsicWidth3;
            } else {
                this.checkDrawLeft1 = this.timeLeft + i9 + AndroidUtilities.dp(5.0f);
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
            String replace22 = str21.replace('\n', ' ');
            if (!this.nameLayoutEllipsizeByGradient) {
                charSequence10 = "";
                try {
                    this.nameLayoutFits = replace22.length() == TextUtils.ellipsize(replace22, Theme.dialogs_namePaint[this.paintIndex], (float) dp6, TextUtils.TruncateAt.END).length();
                    dp6 += AndroidUtilities.dp(48.0f);
                } catch (Exception e4) {
                    e = e4;
                    FileLog.e(e);
                    if (!this.useForceThreeLines) {
                    }
                    i10 = i6;
                    dp = AndroidUtilities.dp(11.0f);
                    this.messageNameTop = AndroidUtilities.dp(32.0f);
                    this.timeTop = AndroidUtilities.dp(13.0f);
                    this.errorTop = AndroidUtilities.dp(43.0f);
                    this.pinTop = AndroidUtilities.dp(43.0f);
                    this.countTop = AndroidUtilities.dp(43.0f);
                    this.checkDrawTop = AndroidUtilities.dp(13.0f);
                    int measuredWidth22 = getMeasuredWidth() - AndroidUtilities.dp(93.0f);
                    if (LocaleController.isRTL) {
                    }
                    i11 = measuredWidth22;
                    this.avatarImage.setImageCoords(dp2, dp, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    this.thumbImage.setImageCoords(dp3, AndroidUtilities.dp(31.0f) + dp, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                    i12 = dp;
                    if (this.drawPin) {
                    }
                    if (this.drawError) {
                    }
                    int i212 = i11;
                    if (i8 != 0) {
                    }
                    max = Math.max(AndroidUtilities.dp(12.0f), i212);
                    z8 = this.useForceThreeLines;
                    if (!z8) {
                    }
                    messageObject5 = this.message;
                    if (messageObject5 != null) {
                    }
                    this.messageNameLayout = StaticLayoutEx.createStaticLayout(charSequence9, Theme.dialogs_messageNamePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max, 1);
                    this.messageTop = AndroidUtilities.dp(51.0f);
                    this.thumbImage.setImageY(i12 + AndroidUtilities.dp(40.0f));
                    z9 = this.useForceThreeLines;
                    if (!z9) {
                    }
                    textPaint5 = Theme.dialogs_messagePaint[this.paintIndex];
                    charSequence8 = charSequence9;
                    charSequence9 = null;
                    if (charSequence8 instanceof Spannable) {
                    }
                    if (!this.useForceThreeLines) {
                    }
                    if (this.hasMessageThumb) {
                    }
                    this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence8, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence9 != null ? 1 : 2);
                    this.spoilersPool.addAll(this.spoilers);
                    this.spoilers.clear();
                    SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
                    this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
                    if (LocaleController.isRTL) {
                    }
                    staticLayout = this.messageLayout;
                    if (staticLayout != null) {
                    }
                    staticLayout2 = this.messageLayout;
                    if (staticLayout2 == null) {
                    }
                }
            } else {
                charSequence10 = "";
            }
            CharSequence replaceEmoji22 = Emoji.replaceEmoji(TextUtils.ellipsize(replace22, Theme.dialogs_namePaint[this.paintIndex], dp6, TextUtils.TruncateAt.END), Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            MessageObject messageObject132 = this.message;
            StaticLayout staticLayout32 = new StaticLayout((messageObject132 != null || !messageObject132.hasHighlightedWords() || (highlightText4 = AndroidUtilities.highlightText(replaceEmoji22, this.message.highlightedWords, this.resourcesProvider)) == null) ? replaceEmoji22 : highlightText4, Theme.dialogs_namePaint[this.paintIndex], Math.max(dp6, this.nameWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.nameLayout = staticLayout32;
            this.nameLayoutTranslateX = (this.nameLayoutEllipsizeByGradient || !staticLayout32.isRtlCharAt(0)) ? 0.0f : -AndroidUtilities.dp(36.0f);
            this.nameLayoutEllipsizeLeft = this.nameLayout.isRtlCharAt(0);
        } catch (Exception e5) {
            e = e5;
            charSequence10 = "";
        }
        if (!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
            i10 = i6;
            dp = AndroidUtilities.dp(11.0f);
            this.messageNameTop = AndroidUtilities.dp(32.0f);
            this.timeTop = AndroidUtilities.dp(13.0f);
            this.errorTop = AndroidUtilities.dp(43.0f);
            this.pinTop = AndroidUtilities.dp(43.0f);
            this.countTop = AndroidUtilities.dp(43.0f);
            this.checkDrawTop = AndroidUtilities.dp(13.0f);
            int measuredWidth222 = getMeasuredWidth() - AndroidUtilities.dp(93.0f);
            if (LocaleController.isRTL) {
                int dp13 = AndroidUtilities.dp(16.0f);
                this.messageNameLeft = dp13;
                this.messageLeft = dp13;
                dp2 = getMeasuredWidth() - AndroidUtilities.dp(66.0f);
                dp3 = dp2 - AndroidUtilities.dp(31.0f);
            } else {
                int dp14 = AndroidUtilities.dp(78.0f);
                this.messageNameLeft = dp14;
                this.messageLeft = dp14;
                dp2 = AndroidUtilities.dp(10.0f);
                dp3 = AndroidUtilities.dp(69.0f) + dp2;
            }
            i11 = measuredWidth222;
            this.avatarImage.setImageCoords(dp2, dp, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            this.thumbImage.setImageCoords(dp3, AndroidUtilities.dp(31.0f) + dp, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
        } else {
            dp = AndroidUtilities.dp(9.0f);
            this.messageNameTop = AndroidUtilities.dp(31.0f);
            this.timeTop = AndroidUtilities.dp(16.0f);
            this.errorTop = AndroidUtilities.dp(39.0f);
            this.pinTop = AndroidUtilities.dp(39.0f);
            this.countTop = AndroidUtilities.dp(39.0f);
            this.checkDrawTop = AndroidUtilities.dp(17.0f);
            int measuredWidth5 = getMeasuredWidth() - AndroidUtilities.dp(95.0f);
            if (LocaleController.isRTL) {
                int dp15 = AndroidUtilities.dp(22.0f);
                this.messageNameLeft = dp15;
                this.messageLeft = dp15;
                dp4 = getMeasuredWidth() - AndroidUtilities.dp(64.0f);
                dp5 = dp4 - AndroidUtilities.dp(i + 11);
            } else {
                int dp16 = AndroidUtilities.dp(76.0f);
                this.messageNameLeft = dp16;
                this.messageLeft = dp16;
                dp4 = AndroidUtilities.dp(10.0f);
                dp5 = AndroidUtilities.dp(67.0f) + dp4;
            }
            i11 = measuredWidth5;
            i10 = i6;
            this.avatarImage.setImageCoords(dp4, dp, AndroidUtilities.dp(54.0f), AndroidUtilities.dp(54.0f));
            float f = i;
            this.thumbImage.setImageCoords(dp5, AndroidUtilities.dp(30.0f) + dp, AndroidUtilities.dp(f), AndroidUtilities.dp(f));
        }
        i12 = dp;
        if (this.drawPin) {
            if (!LocaleController.isRTL) {
                this.pinLeft = (getMeasuredWidth() - Theme.dialogs_pinnedDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(14.0f);
            } else {
                this.pinLeft = AndroidUtilities.dp(14.0f);
            }
        }
        if (this.drawError) {
            int dp17 = AndroidUtilities.dp(31.0f);
            i11 -= dp17;
            if (!LocaleController.isRTL) {
                this.errorLeft = getMeasuredWidth() - AndroidUtilities.dp(34.0f);
            } else {
                this.errorLeft = AndroidUtilities.dp(11.0f);
                this.messageLeft += dp17;
                this.messageNameLeft += dp17;
            }
        } else if (str18 != null || str15 != null || this.drawReactionMention) {
            if (str18 != null) {
                this.countWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str18)));
                this.countLayout = new StaticLayout(str18, Theme.dialogs_countTextPaint, this.countWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                int dp18 = this.countWidth + AndroidUtilities.dp(18.0f);
                i11 -= dp18;
                if (!LocaleController.isRTL) {
                    this.countLeft = (getMeasuredWidth() - this.countWidth) - AndroidUtilities.dp(20.0f);
                } else {
                    this.countLeft = AndroidUtilities.dp(20.0f);
                    this.messageLeft += dp18;
                    this.messageNameLeft += dp18;
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
                int dp19 = this.mentionWidth + AndroidUtilities.dp(18.0f);
                i11 -= dp19;
                if (!LocaleController.isRTL) {
                    int measuredWidth6 = (getMeasuredWidth() - this.mentionWidth) - AndroidUtilities.dp(20.0f);
                    int i29 = this.countWidth;
                    this.mentionLeft = measuredWidth6 - (i29 != 0 ? i29 + AndroidUtilities.dp(18.0f) : 0);
                } else {
                    int dp20 = AndroidUtilities.dp(20.0f);
                    int i30 = this.countWidth;
                    this.mentionLeft = dp20 + (i30 != 0 ? i30 + AndroidUtilities.dp(18.0f) : 0);
                    this.messageLeft += dp19;
                    this.messageNameLeft += dp19;
                }
                this.drawMention = true;
            } else {
                this.mentionWidth = 0;
            }
            if (this.drawReactionMention) {
                int dp21 = AndroidUtilities.dp(24.0f);
                i11 -= dp21;
                if (!LocaleController.isRTL) {
                    int measuredWidth7 = getMeasuredWidth() - AndroidUtilities.dp(32.0f);
                    this.reactionMentionLeft = measuredWidth7;
                    if (this.drawMention) {
                        int i31 = this.mentionWidth;
                        this.reactionMentionLeft = measuredWidth7 - (i31 != 0 ? i31 + AndroidUtilities.dp(18.0f) : 0);
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
                    this.messageNameLeft += dp21;
                }
            }
        } else {
            if (this.drawPin) {
                int intrinsicWidth4 = Theme.dialogs_pinnedDrawable.getIntrinsicWidth() + AndroidUtilities.dp(8.0f);
                i11 -= intrinsicWidth4;
                if (LocaleController.isRTL) {
                    this.messageLeft += intrinsicWidth4;
                    this.messageNameLeft += intrinsicWidth4;
                }
            }
            this.drawCount = false;
            this.drawMention = false;
        }
        int i2122 = i11;
        if (i8 != 0) {
            CharSequence charSequence17 = charSequence8 == null ? charSequence10 : charSequence8;
            if (charSequence17.length() > 150) {
                charSequence17 = charSequence17.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
            }
            if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || charSequence9 != null) {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceNewLines(charSequence17);
            } else {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceTwoNewLinesToOne(charSequence17);
            }
            charSequence8 = Emoji.replaceEmoji(replaceTwoNewLinesToOne, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
            MessageObject messageObject20 = this.message;
            if (messageObject20 != null && (highlightText3 = AndroidUtilities.highlightText(charSequence8, messageObject20.highlightedWords, this.resourcesProvider)) != null) {
                charSequence8 = highlightText3;
            }
        }
        max = Math.max(AndroidUtilities.dp(12.0f), i2122);
        z8 = this.useForceThreeLines;
        if ((!z8 || SharedConfig.useThreeLinesLayout) && charSequence9 != null && (this.currentDialogFolderId == 0 || this.currentDialogFolderDialogsCount == 1)) {
            messageObject5 = this.message;
            if (messageObject5 != null && messageObject5.hasHighlightedWords() && (highlightText2 = AndroidUtilities.highlightText(charSequence9, this.message.highlightedWords, this.resourcesProvider)) != null) {
                charSequence9 = highlightText2;
            }
            this.messageNameLayout = StaticLayoutEx.createStaticLayout(charSequence9, Theme.dialogs_messageNamePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max, 1);
            this.messageTop = AndroidUtilities.dp(51.0f);
            this.thumbImage.setImageY(i12 + AndroidUtilities.dp(40.0f));
        } else {
            this.messageNameLayout = null;
            if (z8 || SharedConfig.useThreeLinesLayout) {
                this.messageTop = AndroidUtilities.dp(32.0f);
                this.thumbImage.setImageY(i12 + AndroidUtilities.dp(21.0f));
            } else {
                this.messageTop = AndroidUtilities.dp(39.0f);
            }
        }
        try {
            z9 = this.useForceThreeLines;
            if ((!z9 || SharedConfig.useThreeLinesLayout) && this.currentDialogFolderId != 0 && this.currentDialogFolderDialogsCount > 1) {
                textPaint5 = Theme.dialogs_messagePaint[this.paintIndex];
                charSequence8 = charSequence9;
                charSequence9 = null;
            } else if ((!z9 && !SharedConfig.useThreeLinesLayout) || charSequence9 != null) {
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
            if (this.hasMessageThumb) {
                max += AndroidUtilities.dp(6.0f) + i;
                if (LocaleController.isRTL) {
                    this.messageLeft -= i + AndroidUtilities.dp(6.0f);
                }
            }
            this.messageLayout = new StaticLayout(charSequence8, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.spoilersPool.addAll(this.spoilers);
            this.spoilers.clear();
            SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
            if (LocaleController.isRTL) {
                StaticLayout staticLayout4 = this.nameLayout;
                if (staticLayout4 != null && staticLayout4.getLineCount() > 0) {
                    float lineLeft = this.nameLayout.getLineLeft(0);
                    double ceil2 = Math.ceil(this.nameLayout.getLineWidth(0));
                    if (this.nameLayoutEllipsizeByGradient) {
                        ceil2 = Math.min(this.nameWidth, ceil2);
                    }
                    if (this.dialogMuted && !this.drawVerified && this.drawScam == 0) {
                        double d = this.nameLeft;
                        double d2 = this.nameWidth;
                        Double.isNaN(d2);
                        Double.isNaN(d);
                        double d3 = d + (d2 - ceil2);
                        double dp23 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(dp23);
                        double d4 = d3 - dp23;
                        double intrinsicWidth5 = Theme.dialogs_muteDrawable.getIntrinsicWidth();
                        Double.isNaN(intrinsicWidth5);
                        this.nameMuteLeft = (int) (d4 - intrinsicWidth5);
                    } else if (this.drawVerified) {
                        double d5 = this.nameLeft;
                        double d6 = this.nameWidth;
                        Double.isNaN(d6);
                        Double.isNaN(d5);
                        double d7 = d5 + (d6 - ceil2);
                        double dp24 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(dp24);
                        double d8 = d7 - dp24;
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
                        double dp25 = AndroidUtilities.dp(24.0f);
                        Double.isNaN(dp25);
                        this.nameMuteLeft = (int) (d12 - dp25);
                    } else if (this.drawScam != 0) {
                        double d13 = this.nameLeft;
                        double d14 = this.nameWidth;
                        Double.isNaN(d14);
                        Double.isNaN(d13);
                        double d15 = d13 + (d14 - ceil2);
                        double dp26 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(dp26);
                        double d16 = d15 - dp26;
                        double intrinsicWidth7 = (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).getIntrinsicWidth();
                        Double.isNaN(intrinsicWidth7);
                        this.nameMuteLeft = (int) (d16 - intrinsicWidth7);
                    }
                    if (lineLeft == 0.0f) {
                        int i37 = this.nameWidth;
                        if (ceil2 < i37) {
                            double d17 = this.nameLeft;
                            double d18 = i37;
                            Double.isNaN(d18);
                            Double.isNaN(d17);
                            this.nameLeft = (int) (d17 + (d18 - ceil2));
                        }
                    }
                }
                StaticLayout staticLayout5 = this.messageLayout;
                if (staticLayout5 != null && (lineCount2 = staticLayout5.getLineCount()) > 0) {
                    int i38 = 0;
                    int i39 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    while (true) {
                        if (i38 >= lineCount2) {
                            break;
                        }
                        if (this.messageLayout.getLineLeft(i38) != 0.0f) {
                            i39 = 0;
                            break;
                        }
                        double ceil3 = Math.ceil(this.messageLayout.getLineWidth(i38));
                        double d19 = max;
                        Double.isNaN(d19);
                        i39 = Math.min(i39, (int) (d19 - ceil3));
                        i38++;
                    }
                    if (i39 != Integer.MAX_VALUE) {
                        this.messageLeft += i39;
                    }
                }
                StaticLayout staticLayout6 = this.messageNameLayout;
                if (staticLayout6 != null && staticLayout6.getLineCount() > 0 && this.messageNameLayout.getLineLeft(0) == 0.0f) {
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
                        int i40 = this.nameWidth;
                        if (ceil5 < i40) {
                            double d22 = this.nameLeft;
                            double d23 = i40;
                            Double.isNaN(d23);
                            Double.isNaN(d22);
                            this.nameLeft = (int) (d22 - (d23 - ceil5));
                        }
                    }
                    if (this.dialogMuted || this.drawVerified || this.drawPremium || this.drawScam != 0) {
                        this.nameMuteLeft = (int) (this.nameLeft + lineRight + AndroidUtilities.dp(6.0f));
                    }
                }
                StaticLayout staticLayout8 = this.messageLayout;
                if (staticLayout8 != null && (lineCount = staticLayout8.getLineCount()) > 0) {
                    float f2 = 2.14748365E9f;
                    for (int i41 = 0; i41 < lineCount; i41++) {
                        f2 = Math.min(f2, this.messageLayout.getLineLeft(i41));
                    }
                    this.messageLeft = (int) (this.messageLeft - f2);
                }
                StaticLayout staticLayout9 = this.messageNameLayout;
                if (staticLayout9 != null && staticLayout9.getLineCount() > 0) {
                    this.messageNameLeft = (int) (this.messageNameLeft - this.messageNameLayout.getLineLeft(0));
                }
            }
            staticLayout = this.messageLayout;
            if (staticLayout != null && this.hasMessageThumb) {
                try {
                    length = staticLayout.getText().length();
                    if (i15 >= length) {
                        i15 = length - 1;
                    }
                    ceil = (int) Math.ceil(Math.min(this.messageLayout.getPrimaryHorizontal(i15), this.messageLayout.getPrimaryHorizontal(i15 + 1)));
                    if (ceil != 0) {
                        ceil += AndroidUtilities.dp(3.0f);
                    }
                    this.thumbImage.setImageX(this.messageLeft + ceil);
                } catch (Exception e7) {
                    FileLog.e(e7);
                }
            }
            staticLayout2 = this.messageLayout;
            if (staticLayout2 == null || this.printingStringType < 0) {
            }
            if (i10 >= 0 && (i13 = i10 + 1) < staticLayout2.getText().length()) {
                primaryHorizontal = this.messageLayout.getPrimaryHorizontal(i10);
                primaryHorizontal2 = this.messageLayout.getPrimaryHorizontal(i13);
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
        if (this.hasMessageThumb && charSequence9 != null) {
            max += AndroidUtilities.dp(6.0f);
        }
        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence8, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence9 != null ? 1 : 2);
        this.spoilersPool.addAll(this.spoilers);
        this.spoilers.clear();
        SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
        if (LocaleController.isRTL) {
        }
        staticLayout = this.messageLayout;
        if (staticLayout != null) {
            length = staticLayout.getText().length();
            if (i15 >= length) {
            }
            ceil = (int) Math.ceil(Math.min(this.messageLayout.getPrimaryHorizontal(i15), this.messageLayout.getPrimaryHorizontal(i15 + 1)));
            if (ceil != 0) {
            }
            this.thumbImage.setImageX(this.messageLeft + ceil);
        }
        staticLayout2 = this.messageLayout;
        if (staticLayout2 == null) {
        }
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
        TLRPC$Dialog tLRPC$Dialog2 = this.index + 1 < dialogsArray.size() ? dialogsArray.get(this.index + 1) : null;
        TLRPC$DraftMessage draft = MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, 0);
        if (this.currentDialogFolderId != 0) {
            messageObject = findFolderTopMessage();
        } else {
            messageObject = MessagesController.getInstance(this.currentAccount).dialogMessage.get(tLRPC$Dialog.id);
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
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 == null) {
            return;
        }
        checkBox2.setChecked(z, z2);
    }

    private MessageObject findFolderTopMessage() {
        DialogsActivity dialogsActivity = this.parentFragment;
        MessageObject messageObject = null;
        if (dialogsActivity == null) {
            return null;
        }
        ArrayList<TLRPC$Dialog> dialogsArray = dialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.currentDialogFolderId, false);
        if (!dialogsArray.isEmpty()) {
            int size = dialogsArray.size();
            for (int i = 0; i < size; i++) {
                TLRPC$Dialog tLRPC$Dialog = dialogsArray.get(i);
                MessageObject messageObject2 = MessagesController.getInstance(this.currentAccount).dialogMessage.get(tLRPC$Dialog.id);
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

    public void update(int i) {
        update(i, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:122:0x0214  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x024b  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x05fd  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x02c3  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x02c8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void update(int i, boolean z) {
        long j;
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
            this.avatarDrawable.setInfo(customDialog.id, customDialog.name, null);
            this.avatarImage.setImage(null, "50_50", this.avatarDrawable, null, 0L);
            this.thumbImage.setImageBitmap((Drawable) null);
        } else {
            int i8 = this.unreadCount;
            boolean z4 = this.reactionMentionCount != 0;
            boolean z5 = this.markUnread;
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
                    MessageObject messageObject3 = MessagesController.getInstance(this.currentAccount).dialogMessage.get(tLRPC$Dialog.id);
                    this.message = messageObject3;
                    this.lastUnreadState = messageObject3 != null && messageObject3.isUnread();
                    if (tLRPC$Dialog instanceof TLRPC$TL_dialogFolder) {
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
                    int i9 = this.dialogsType;
                    if (i9 == 7 || i9 == 8) {
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
                                if (tLRPC$Dialog2 instanceof TLRPC$TL_dialogFolder) {
                                    i4 = MessagesStorage.getInstance(this.currentAccount).getArchiveUnreadCount();
                                } else if (tLRPC$Dialog2 != null) {
                                    i4 = tLRPC$Dialog2.unread_count;
                                    i5 = tLRPC$Dialog2.unread_mentions_count;
                                    i6 = tLRPC$Dialog2.unread_reactions_count;
                                    if (tLRPC$Dialog2 != null && (this.unreadCount != i4 || this.markUnread != tLRPC$Dialog2.unread_mark || this.mentionCount != i5 || this.reactionMentionCount != i6)) {
                                        this.unreadCount = i4;
                                        this.mentionCount = i5;
                                        this.markUnread = tLRPC$Dialog2.unread_mark;
                                        this.reactionMentionCount = i6;
                                        z2 = true;
                                    }
                                } else {
                                    i4 = 0;
                                }
                                i5 = 0;
                                i6 = 0;
                                if (tLRPC$Dialog2 != null) {
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
                MessageObject findFolderTopMessage = findFolderTopMessage();
                this.message = findFolderTopMessage;
                j = findFolderTopMessage != null ? findFolderTopMessage.getDialogId() : 0L;
            } else {
                this.dialogMuted = this.isDialogCell && MessagesController.getInstance(this.currentAccount).isDialogMuted(this.currentDialogId);
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
                    TLRPC$Chat chat3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
                    this.chat = chat3;
                    if (!this.isDialogCell && chat3 != null && chat3.migrated_to != null && (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.migrated_to.channel_id))) != null) {
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
                    TLRPC$Chat tLRPC$Chat = this.chat;
                    if (tLRPC$Chat != null) {
                        this.avatarDrawable.setInfo(tLRPC$Chat);
                        this.avatarImage.setForUserOrChat(this.chat, this.avatarDrawable);
                    }
                }
            }
            if (z && ((i8 != this.unreadCount || z5 != this.markUnread) && System.currentTimeMillis() - this.lastDialogChangedTime > 100)) {
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
                if ((i8 == 0 || this.markUnread) && (this.markUnread || !z5)) {
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
                    String valueOf = String.valueOf(i8);
                    String valueOf2 = String.valueOf(this.unreadCount);
                    if (valueOf.length() == valueOf2.length()) {
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(valueOf);
                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(valueOf2);
                        SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(valueOf2);
                        for (int i10 = 0; i10 < valueOf.length(); i10++) {
                            if (valueOf.charAt(i10) == valueOf2.charAt(i10)) {
                                int i11 = i10 + 1;
                                spannableStringBuilder.setSpan(new EmptyStubSpan(), i10, i11, 0);
                                spannableStringBuilder2.setSpan(new EmptyStubSpan(), i10, i11, 0);
                            } else {
                                spannableStringBuilder3.setSpan(new EmptyStubSpan(), i10, i10 + 1, 0);
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
                this.countAnimationIncrement = this.unreadCount > i8;
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
                if (z6) {
                    this.reactionsMentionsAnimator.setDuration(220L);
                    this.reactionsMentionsAnimator.setInterpolator(new OvershootInterpolator());
                } else {
                    this.reactionsMentionsAnimator.setDuration(150L);
                    this.reactionsMentionsAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                }
                this.reactionsMentionsAnimator.start();
                if (getMeasuredWidth() == 0 || getMeasuredHeight() != 0) {
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
        }
        if (getMeasuredWidth() == 0) {
        }
        buildLayout();
        if (!z) {
        }
        invalidate();
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

    /* JADX WARN: Code restructure failed: missing block: B:706:0x06bf, code lost:
        if (r0.type != 2) goto L707;
     */
    /* JADX WARN: Removed duplicated region for block: B:115:0x07ed  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x093a  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x0a0a  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0a6b  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0a7d  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0bc8  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x0bf0  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x102e  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x104e  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x1061  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x1099  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x10a0  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x1444  */
    /* JADX WARN: Removed duplicated region for block: B:275:0x144b  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x146f  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x14d5  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x1522  */
    /* JADX WARN: Removed duplicated region for block: B:312:0x1559  */
    /* JADX WARN: Removed duplicated region for block: B:323:0x15b2  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x15c6  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x15e1  */
    /* JADX WARN: Removed duplicated region for block: B:341:0x1612  */
    /* JADX WARN: Removed duplicated region for block: B:343:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:345:0x15ef  */
    /* JADX WARN: Removed duplicated region for block: B:354:0x1583  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x152e  */
    /* JADX WARN: Removed duplicated region for block: B:370:0x1541  */
    /* JADX WARN: Removed duplicated region for block: B:391:0x1180  */
    /* JADX WARN: Removed duplicated region for block: B:423:0x135a  */
    /* JADX WARN: Removed duplicated region for block: B:427:0x13e7  */
    /* JADX WARN: Removed duplicated region for block: B:431:0x13fc  */
    /* JADX WARN: Removed duplicated region for block: B:436:0x1412  */
    /* JADX WARN: Removed duplicated region for block: B:443:0x1425  */
    /* JADX WARN: Removed duplicated region for block: B:482:0x0c3e  */
    /* JADX WARN: Removed duplicated region for block: B:594:0x0ab6  */
    /* JADX WARN: Removed duplicated region for block: B:595:0x0a6e  */
    /* JADX WARN: Removed duplicated region for block: B:607:0x0ac1  */
    /* JADX WARN: Removed duplicated region for block: B:620:0x0b09  */
    /* JADX WARN: Removed duplicated region for block: B:672:0x09fe  */
    /* JADX WARN: Removed duplicated region for block: B:675:0x0908  */
    /* JADX WARN: Removed duplicated region for block: B:677:0x090d  */
    /* JADX WARN: Removed duplicated region for block: B:697:0x092d  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0799  */
    /* JADX WARN: Type inference failed for: r9v28, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r9v29 */
    /* JADX WARN: Type inference failed for: r9v58 */
    @Override // android.view.View
    @SuppressLint({"DrawAllocation"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
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
        int i3;
        int i4;
        String str4;
        Canvas canvas3;
        float f3;
        float f4;
        int dp;
        int dp2;
        int dp3;
        float f5;
        float interpolation;
        long j2;
        ?? r9;
        float f6;
        boolean z2;
        float f7;
        boolean z3;
        float f8;
        TLRPC$User tLRPC$User;
        TLRPC$Chat tLRPC$Chat;
        float dp4;
        int i5;
        float dp5;
        float dp6;
        float dp7;
        float f9;
        float dp8;
        float dp9;
        float dp10;
        float f10;
        float f11;
        boolean z4;
        float dp11;
        PullForegroundDrawable pullForegroundDrawable;
        int i6;
        boolean z5;
        float f12;
        int i7;
        StatusDrawable chatStatusDrawable;
        int i8;
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
                    if (this.drawPin) {
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
            int i9 = color2;
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
                Theme.dialogs_pinnedPaint.setColor(i9);
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
            int i10 = i2;
            if (this.swipeMessageTextId != i10 || this.swipeMessageWidth != getMeasuredWidth()) {
                this.swipeMessageTextId = i10;
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
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
        } else if (this.drawPin || this.drawPinBackground) {
            Theme.dialogs_pinnedPaint.setColor(Theme.getColor("chats_pinnedOverlay", this.resourcesProvider));
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
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
            } else if (this.drawPin || this.drawPinBackground) {
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
                int i11 = this.nameLeft;
                canvas.clipRect(i11, 0, this.nameWidth + i11, getMeasuredHeight());
            }
            if (this.currentDialogFolderId != 0) {
                TextPaint[] textPaintArr = Theme.dialogs_namePaint;
                int i12 = this.paintIndex;
                TextPaint textPaint = textPaintArr[i12];
                TextPaint textPaint2 = textPaintArr[i12];
                int color3 = Theme.getColor("chats_nameArchived", this.resourcesProvider);
                textPaint2.linkColor = color3;
                textPaint.setColor(color3);
            } else {
                if (this.encryptedChat == null) {
                    CustomDialog customDialog = this.customDialog;
                    if (customDialog != null) {
                    }
                    TextPaint[] textPaintArr2 = Theme.dialogs_namePaint;
                    int i13 = this.paintIndex;
                    TextPaint textPaint3 = textPaintArr2[i13];
                    TextPaint textPaint4 = textPaintArr2[i13];
                    int color4 = Theme.getColor("chats_name", this.resourcesProvider);
                    textPaint4.linkColor = color4;
                    textPaint3.setColor(color4);
                }
                TextPaint[] textPaintArr3 = Theme.dialogs_namePaint;
                int i14 = this.paintIndex;
                TextPaint textPaint5 = textPaintArr3[i14];
                TextPaint textPaint6 = textPaintArr3[i14];
                int color5 = Theme.getColor("chats_secretName", this.resourcesProvider);
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
                if (this.messageNameLayout != null) {
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
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    canvas.restore();
                }
                if (this.messageLayout == null) {
                    if (this.currentDialogFolderId != 0) {
                        if (this.chat != null) {
                            TextPaint[] textPaintArr4 = Theme.dialogs_messagePaint;
                            int i15 = this.paintIndex;
                            TextPaint textPaint10 = textPaintArr4[i15];
                            TextPaint textPaint11 = textPaintArr4[i15];
                            int color9 = Theme.getColor("chats_nameMessageArchived", this.resourcesProvider);
                            textPaint11.linkColor = color9;
                            textPaint10.setColor(color9);
                        } else {
                            TextPaint[] textPaintArr5 = Theme.dialogs_messagePaint;
                            int i16 = this.paintIndex;
                            TextPaint textPaint12 = textPaintArr5[i16];
                            TextPaint textPaint13 = textPaintArr5[i16];
                            int color10 = Theme.getColor("chats_messageArchived", this.resourcesProvider);
                            textPaint13.linkColor = color10;
                            textPaint12.setColor(color10);
                        }
                    } else {
                        TextPaint[] textPaintArr6 = Theme.dialogs_messagePaint;
                        int i17 = this.paintIndex;
                        TextPaint textPaint14 = textPaintArr6[i17];
                        TextPaint textPaint15 = textPaintArr6[i17];
                        int color11 = Theme.getColor("chats_message", this.resourcesProvider);
                        textPaint15.linkColor = color11;
                        textPaint14.setColor(color11);
                    }
                    canvas.save();
                    canvas.translate(this.messageLeft, this.messageTop);
                    if (!this.spoilers.isEmpty()) {
                        try {
                            canvas.save();
                            SpoilerEffect.clipOutCanvas(canvas, this.spoilers);
                            this.messageLayout.draw(canvas);
                            j = j4;
                            str3 = "windowBackgroundWhite";
                            f12 = 1.0f;
                            canvas2 = canvas;
                        } catch (Exception e2) {
                            e = e2;
                            j = j4;
                            str3 = "windowBackgroundWhite";
                            canvas2 = canvas;
                            f12 = 1.0f;
                        }
                        try {
                            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageLayout, this.animatedEmojiStack, -0.075f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f);
                            canvas.restore();
                            for (int i18 = 0; i18 < this.spoilers.size(); i18++) {
                                SpoilerEffect spoilerEffect = this.spoilers.get(i18);
                                spoilerEffect.setColor(this.messageLayout.getPaint().getColor());
                                spoilerEffect.draw(canvas2);
                            }
                        } catch (Exception e3) {
                            e = e3;
                            FileLog.e(e);
                            canvas.restore();
                            i7 = this.printingStringType;
                            if (i7 >= 0) {
                                canvas.save();
                                i8 = this.printingStringType;
                                i3 = 1;
                                i4 = 4;
                                if (i8 != 1) {
                                }
                                canvas2.translate(this.statusDrawableLeft, this.messageTop + (i8 != 1 ? AndroidUtilities.dp(f12) : 0));
                                chatStatusDrawable.draw(canvas2);
                                int i19 = this.statusDrawableLeft;
                                invalidate(i19, this.messageTop, chatStatusDrawable.getIntrinsicWidth() + i19, this.messageTop + chatStatusDrawable.getIntrinsicHeight());
                                canvas.restore();
                                if (this.currentDialogFolderId == 0) {
                                }
                                if (this.dialogsType == 2) {
                                }
                                if (this.drawVerified) {
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
                                if (this.currentDialogFolderId != 0) {
                                }
                                this.avatarImage.draw(canvas3);
                                if (this.hasMessageThumb) {
                                }
                                if (this.animatingArchiveAvatar) {
                                }
                                if (this.isDialogCell) {
                                }
                                j2 = j;
                                f6 = 0.0f;
                                r9 = 0;
                                if (this.translationX != f6) {
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
                            i3 = 1;
                            i4 = 4;
                            if (this.currentDialogFolderId == 0) {
                            }
                            if (this.dialogsType == 2) {
                            }
                            if (this.drawVerified) {
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
                            if (this.currentDialogFolderId != 0) {
                            }
                            this.avatarImage.draw(canvas3);
                            if (this.hasMessageThumb) {
                            }
                            if (this.animatingArchiveAvatar) {
                            }
                            if (this.isDialogCell) {
                            }
                            j2 = j;
                            f6 = 0.0f;
                            r9 = 0;
                            if (this.translationX != f6) {
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
                    } else {
                        j = j4;
                        str3 = "windowBackgroundWhite";
                        canvas2 = canvas;
                        f12 = 1.0f;
                        this.messageLayout.draw(canvas2);
                        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageLayout, this.animatedEmojiStack, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
                    }
                    canvas.restore();
                    i7 = this.printingStringType;
                    if (i7 >= 0 && (chatStatusDrawable = Theme.getChatStatusDrawable(i7)) != null) {
                        canvas.save();
                        i8 = this.printingStringType;
                        i3 = 1;
                        i4 = 4;
                        if (i8 != 1 || i8 == 4) {
                            canvas2.translate(this.statusDrawableLeft, this.messageTop + (i8 != 1 ? AndroidUtilities.dp(f12) : 0));
                        } else {
                            canvas2.translate(this.statusDrawableLeft, this.messageTop + ((AndroidUtilities.dp(18.0f) - chatStatusDrawable.getIntrinsicHeight()) / 2.0f));
                        }
                        chatStatusDrawable.draw(canvas2);
                        int i192 = this.statusDrawableLeft;
                        invalidate(i192, this.messageTop, chatStatusDrawable.getIntrinsicWidth() + i192, this.messageTop + chatStatusDrawable.getIntrinsicHeight());
                        canvas.restore();
                        if (this.currentDialogFolderId == 0) {
                            int i20 = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
                            int i21 = this.lastStatusDrawableParams;
                            if (i21 >= 0 && i21 != i20 && !this.statusDrawableAnimationInProgress) {
                                createStatusDrawableAnimator(i21, i20);
                            }
                            boolean z6 = this.statusDrawableAnimationInProgress;
                            if (z6) {
                                i20 = this.animateToStatusDrawableParams;
                            }
                            boolean z7 = (i20 & 1) != 0;
                            boolean z8 = (i20 & 2) != 0;
                            boolean z9 = (i20 & i4) != 0;
                            if (z6) {
                                int i22 = this.animateFromStatusDrawableParams;
                                boolean z10 = (i22 & 1) != 0;
                                boolean z11 = (i22 & 2) != 0;
                                boolean z12 = (i22 & i4) != 0;
                                if (!z7 && !z10 && z12 && !z11 && z8 && z9) {
                                    str4 = str3;
                                    f4 = 0.0f;
                                    drawCheckStatus(canvas, z7, z8, z9, true, this.statusDrawableProgress);
                                    canvas3 = canvas2;
                                    f3 = 1.0f;
                                } else {
                                    str4 = str3;
                                    Canvas canvas4 = canvas2;
                                    f4 = 0.0f;
                                    boolean z13 = z11;
                                    f3 = 1.0f;
                                    canvas3 = canvas4;
                                    drawCheckStatus(canvas, z10, z13, z12, false, 1.0f - this.statusDrawableProgress);
                                    drawCheckStatus(canvas, z7, z8, z9, false, this.statusDrawableProgress);
                                }
                            } else {
                                str4 = str3;
                                canvas3 = canvas2;
                                f3 = 1.0f;
                                f4 = 0.0f;
                                drawCheckStatus(canvas, z7, z8, z9, false, 1.0f);
                            }
                            this.lastStatusDrawableParams = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
                        } else {
                            str4 = str3;
                            canvas3 = canvas2;
                            f3 = 1.0f;
                            f4 = 0.0f;
                        }
                        if (this.dialogsType == 2 && (((z5 = this.dialogMuted) || this.dialogMutedProgress > f4) && !this.drawVerified && this.drawScam == 0 && !this.drawPremium)) {
                            if (z5) {
                                float f20 = this.dialogMutedProgress;
                                if (f20 != f3) {
                                    float f21 = f20 + 0.10666667f;
                                    this.dialogMutedProgress = f21;
                                    if (f21 > f3) {
                                        this.dialogMutedProgress = f3;
                                    } else {
                                        invalidate();
                                    }
                                    BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f), AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f));
                                    if (this.dialogMutedProgress == f3) {
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
                            if (!z5) {
                                float f23 = this.dialogMutedProgress;
                                if (f23 != f4) {
                                    float f24 = f23 - 0.10666667f;
                                    this.dialogMutedProgress = f24;
                                    if (f24 < f4) {
                                        this.dialogMutedProgress = f4;
                                    } else {
                                        invalidate();
                                    }
                                }
                            }
                            BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f), AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f));
                            if (this.dialogMutedProgress == f3) {
                            }
                        } else if (this.drawVerified) {
                            BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft - AndroidUtilities.dp(f3), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
                            BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft - AndroidUtilities.dp(f3), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
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
                                BaseCell.setDrawableBounds(drawable, this.nameMuteLeft - AndroidUtilities.dp(f3), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f));
                                drawable.draw(canvas3);
                            }
                        } else {
                            int i23 = this.drawScam;
                            if (i23 != 0) {
                                BaseCell.setDrawableBounds((Drawable) (i23 == i3 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable), this.nameMuteLeft, AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.0f : 15.0f));
                                (this.drawScam == i3 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).draw(canvas3);
                            }
                        }
                        if (!this.drawReorder || this.reorderIconProgress != f4) {
                            Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                            BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                            Theme.dialogs_reorderDrawable.draw(canvas3);
                        }
                        if (this.drawError) {
                            Theme.dialogs_errorDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                            this.rect.set(this.errorLeft, this.errorTop, i6 + AndroidUtilities.dp(23.0f), this.errorTop + AndroidUtilities.dp(23.0f));
                            RectF rectF = this.rect;
                            float f25 = AndroidUtilities.density;
                            canvas3.drawRoundRect(rectF, f25 * 11.5f, f25 * 11.5f, Theme.dialogs_errorPaint);
                            BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
                            Theme.dialogs_errorDrawable.draw(canvas3);
                        } else {
                            boolean z14 = this.drawCount;
                            if (((z14 || this.drawMention) && this.drawCount2) || this.countChangeProgress != f3 || this.drawReactionMention || this.reactionsMentionsChangeProgress != f3) {
                                if ((z14 && this.drawCount2) || this.countChangeProgress != f3) {
                                    int i24 = this.unreadCount;
                                    float f26 = (i24 != 0 || this.markUnread) ? this.countChangeProgress : f3 - this.countChangeProgress;
                                    StaticLayout staticLayout2 = this.countOldLayout;
                                    if (staticLayout2 == null || i24 == 0) {
                                        if (i24 != 0) {
                                            staticLayout2 = this.countLayout;
                                        }
                                        Paint paint3 = (this.dialogMuted || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
                                        paint3.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                        Theme.dialogs_countTextPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                        this.rect.set(this.countLeft - AndroidUtilities.dp(5.5f), this.countTop, dp3 + this.countWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                        if (f26 != f3) {
                                            if (this.drawPin) {
                                                Theme.dialogs_pinnedDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                                BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                                                canvas.save();
                                                float f27 = f3 - f26;
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
                                        if (f26 != f3) {
                                            canvas.restore();
                                        }
                                    } else {
                                        Paint paint4 = (this.dialogMuted || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
                                        paint4.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                        Theme.dialogs_countTextPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                        float f29 = f26 * 2.0f;
                                        float f30 = f29 > f3 ? 1.0f : f29;
                                        float f31 = f3 - f30;
                                        float f32 = (this.countLeft * f30) + (this.countLeftOld * f31);
                                        float dp14 = f32 - AndroidUtilities.dp(5.5f);
                                        this.rect.set(dp14, this.countTop, (this.countWidth * f30) + dp14 + (this.countWidthOld * f31) + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                        if (f26 <= 0.5f) {
                                            f5 = 0.1f;
                                            interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(f29);
                                        } else {
                                            f5 = 0.1f;
                                            interpolation = CubicBezierInterpolator.EASE_IN.getInterpolation(f3 - ((f26 - 0.5f) * 2.0f));
                                        }
                                        float f33 = (interpolation * f5) + f3;
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
                                    Theme.dialogs_countPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                    this.rect.set(this.mentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp2 + this.mentionWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                    Paint paint5 = (!this.dialogMuted || this.folderId == 0) ? Theme.dialogs_countPaint : Theme.dialogs_countGrayPaint;
                                    RectF rectF4 = this.rect;
                                    float f36 = AndroidUtilities.density;
                                    canvas3.drawRoundRect(rectF4, f36 * 11.5f, f36 * 11.5f, paint5);
                                    if (this.mentionLayout != null) {
                                        Theme.dialogs_countTextPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                        canvas.save();
                                        canvas3.translate(this.mentionLeft, this.countTop + AndroidUtilities.dp(4.0f));
                                        this.mentionLayout.draw(canvas3);
                                        canvas.restore();
                                    } else {
                                        Theme.dialogs_mentionDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                        BaseCell.setDrawableBounds(Theme.dialogs_mentionDrawable, this.mentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.2f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                        Theme.dialogs_mentionDrawable.draw(canvas3);
                                    }
                                }
                                if (this.drawReactionMention || this.reactionsMentionsChangeProgress != f3) {
                                    Theme.dialogs_reactionsCountPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                    this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                    Paint paint6 = Theme.dialogs_reactionsCountPaint;
                                    canvas.save();
                                    float f37 = this.reactionsMentionsChangeProgress;
                                    if (f37 != f3) {
                                        if (!this.drawReactionMention) {
                                            f37 = f3 - f37;
                                        }
                                        canvas3.scale(f37, f37, this.rect.centerX(), this.rect.centerY());
                                    }
                                    RectF rectF5 = this.rect;
                                    float f38 = AndroidUtilities.density;
                                    canvas3.drawRoundRect(rectF5, f38 * 11.5f, f38 * 11.5f, paint6);
                                    Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                    BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                    Theme.dialogs_reactionsMentionDrawable.draw(canvas3);
                                    canvas.restore();
                                }
                            } else if (this.drawPin) {
                                Theme.dialogs_pinnedDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                                Theme.dialogs_pinnedDrawable.draw(canvas3);
                            }
                        }
                        if (this.animatingArchiveAvatar) {
                            canvas.save();
                            float interpolation3 = this.interpolator.getInterpolation(this.animatingArchiveAvatarProgress / 170.0f) + f3;
                            canvas3.scale(interpolation3, interpolation3, this.avatarImage.getCenterX(), this.avatarImage.getCenterY());
                        }
                        if (this.currentDialogFolderId != 0 || (pullForegroundDrawable = this.archivedChatsDrawable) == null || !pullForegroundDrawable.isDraw()) {
                            this.avatarImage.draw(canvas3);
                        }
                        if (this.hasMessageThumb) {
                            this.thumbImage.draw(canvas3);
                            if (this.drawPlay) {
                                BaseCell.setDrawableBounds(Theme.dialogs_playDrawable, (int) (this.thumbImage.getCenterX() - (Theme.dialogs_playDrawable.getIntrinsicWidth() / 2)), (int) (this.thumbImage.getCenterY() - (Theme.dialogs_playDrawable.getIntrinsicHeight() / 2)));
                                Theme.dialogs_playDrawable.draw(canvas3);
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
                                        if (f41 < f3) {
                                            j2 = j;
                                            float f42 = f41 + (((float) j2) / 150.0f);
                                            this.onlineProgress = f42;
                                            if (f42 > f3) {
                                                this.onlineProgress = f3;
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
                                    boolean z15 = tLRPC$Chat.call_active && tLRPC$Chat.call_not_empty;
                                    this.hasCall = z15;
                                    if (z15 || this.chatCallProgress != 0.0f) {
                                        CheckBox2 checkBox2 = this.checkBox;
                                        float progress = (checkBox2 == null || !checkBox2.isChecked()) ? 1.0f : f3 - this.checkBox.getProgress();
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
                                        int i25 = this.progressStage;
                                        if (i25 == 0) {
                                            dp5 = AndroidUtilities.dp(f3) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                            dp9 = AndroidUtilities.dp(3.0f);
                                            dp10 = AndroidUtilities.dp(2.0f);
                                            f10 = this.innerProgress;
                                        } else {
                                            if (i25 == 1) {
                                                dp5 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                                dp6 = AndroidUtilities.dp(f3);
                                                dp7 = AndroidUtilities.dp(4.0f);
                                                f9 = this.innerProgress;
                                            } else if (i25 == 2) {
                                                dp5 = AndroidUtilities.dp(f3) + (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                                dp9 = AndroidUtilities.dp(5.0f);
                                                dp10 = AndroidUtilities.dp(4.0f);
                                                f10 = this.innerProgress;
                                            } else if (i25 == 3) {
                                                dp5 = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                                dp6 = AndroidUtilities.dp(f3);
                                                dp7 = AndroidUtilities.dp(2.0f);
                                                f9 = this.innerProgress;
                                            } else if (i25 == 4) {
                                                dp5 = AndroidUtilities.dp(f3) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                                dp9 = AndroidUtilities.dp(3.0f);
                                                dp10 = AndroidUtilities.dp(2.0f);
                                                f10 = this.innerProgress;
                                            } else if (i25 == 5) {
                                                dp5 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                                dp6 = AndroidUtilities.dp(f3);
                                                dp7 = AndroidUtilities.dp(4.0f);
                                                f9 = this.innerProgress;
                                            } else if (i25 == 6) {
                                                dp5 = AndroidUtilities.dp(f3) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                                dp8 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                                if (this.chatCallProgress >= f3 || progress < f3) {
                                                    canvas.save();
                                                    float f47 = this.chatCallProgress;
                                                    canvas3.scale(f47 * progress, f47 * progress, f45, f46);
                                                }
                                                this.rect.set(i5 - AndroidUtilities.dp(f3), f46 - dp5, AndroidUtilities.dp(f3) + i5, dp5 + f46);
                                                canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                                                float f48 = f46 - dp8;
                                                float f49 = f46 + dp8;
                                                this.rect.set(i5 - AndroidUtilities.dp(5.0f), f48, i5 - AndroidUtilities.dp(3.0f), f49);
                                                canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                                                this.rect.set(AndroidUtilities.dp(3.0f) + i5, f48, i5 + AndroidUtilities.dp(5.0f), f49);
                                                canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                                                if (this.chatCallProgress >= f3 || progress < f3) {
                                                    canvas.restore();
                                                }
                                                float f50 = (float) j2;
                                                f11 = this.innerProgress + (f50 / 400.0f);
                                                this.innerProgress = f11;
                                                if (f11 >= f3) {
                                                    this.innerProgress = 0.0f;
                                                    int i26 = this.progressStage + 1;
                                                    this.progressStage = i26;
                                                    if (i26 >= 8) {
                                                        z4 = false;
                                                        this.progressStage = 0;
                                                        if (!this.hasCall) {
                                                            float f51 = this.chatCallProgress;
                                                            if (f51 < f3) {
                                                                float f52 = f51 + (f50 / 150.0f);
                                                                this.chatCallProgress = f52;
                                                                if (f52 > f3) {
                                                                    this.chatCallProgress = f3;
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
                                                        r9 = z4;
                                                        if (this.translationX != f6) {
                                                            canvas.restore();
                                                        }
                                                        if (this.currentDialogFolderId != 0 && this.translationX == f6 && this.archivedChatsDrawable != null) {
                                                            canvas.save();
                                                            int measuredWidth3 = getMeasuredWidth();
                                                            int measuredHeight = getMeasuredHeight();
                                                            int i27 = r9 == true ? 1 : 0;
                                                            int i28 = r9 == true ? 1 : 0;
                                                            int i29 = r9 == true ? 1 : 0;
                                                            canvas3.clipRect(i27, (int) r9, measuredWidth3, measuredHeight);
                                                            this.archivedChatsDrawable.draw(canvas3);
                                                            canvas.restore();
                                                        }
                                                        if (this.useSeparator) {
                                                            int dp17 = (this.fullSeparator || (this.currentDialogFolderId != 0 && this.archiveHidden && !this.fullSeparator2) || (this.fullSeparator2 && !this.archiveHidden)) ? 0 : AndroidUtilities.dp(72.0f);
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
                                                                        if (f55 < f3) {
                                                                            float f56 = f55 + (((float) j2) / 170.0f);
                                                                            this.reorderIconProgress = f56;
                                                                            if (f56 > f3) {
                                                                                this.reorderIconProgress = f3;
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
                                                                        if (!this.archiveHidden) {
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
                                                                                    this.animatingArchiveAvatar = r9;
                                                                                }
                                                                                z = true;
                                                                            }
                                                                            if (!this.drawRevealBackground) {
                                                                                float f62 = this.currentRevealBounceProgress;
                                                                                if (f62 < f3) {
                                                                                    float f63 = f62 + (((float) j2) / 170.0f);
                                                                                    this.currentRevealBounceProgress = f63;
                                                                                    if (f63 > f3) {
                                                                                        this.currentRevealBounceProgress = f3;
                                                                                        z3 = true;
                                                                                        f8 = this.currentRevealProgress;
                                                                                        if (f8 < f3) {
                                                                                            float f64 = f8 + (((float) j2) / 300.0f);
                                                                                            this.currentRevealProgress = f64;
                                                                                            if (f64 > f3) {
                                                                                                this.currentRevealProgress = f3;
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
                                                                                f8 = this.currentRevealProgress;
                                                                                if (f8 < f3) {
                                                                                }
                                                                                if (!z3) {
                                                                                }
                                                                            } else {
                                                                                if (this.currentRevealBounceProgress == f3) {
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
                                                                            if (f67 < f3) {
                                                                                float f68 = f67 + (((float) j2) / 230.0f);
                                                                                this.archiveBackgroundProgress = f68;
                                                                                if (f68 > f3) {
                                                                                    this.archiveBackgroundProgress = f3;
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
                                                                f7 = 0.0f;
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
                                                    }
                                                }
                                                z4 = false;
                                                if (!this.hasCall) {
                                                }
                                                z = true;
                                                r9 = z4;
                                                if (this.translationX != f6) {
                                                }
                                                if (this.currentDialogFolderId != 0) {
                                                    canvas.save();
                                                    int measuredWidth32 = getMeasuredWidth();
                                                    int measuredHeight2 = getMeasuredHeight();
                                                    int i272 = r9 == true ? 1 : 0;
                                                    int i282 = r9 == true ? 1 : 0;
                                                    int i292 = r9 == true ? 1 : 0;
                                                    canvas3.clipRect(i272, (int) r9, measuredWidth32, measuredHeight2);
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
                                            } else {
                                                dp5 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                                dp6 = AndroidUtilities.dp(f3);
                                                dp7 = AndroidUtilities.dp(2.0f);
                                                f9 = this.innerProgress;
                                            }
                                            dp8 = dp6 + (dp7 * f9);
                                            if (this.chatCallProgress >= f3) {
                                            }
                                            canvas.save();
                                            float f472 = this.chatCallProgress;
                                            canvas3.scale(f472 * progress, f472 * progress, f45, f46);
                                            this.rect.set(i5 - AndroidUtilities.dp(f3), f46 - dp5, AndroidUtilities.dp(f3) + i5, dp5 + f46);
                                            canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                                            float f482 = f46 - dp8;
                                            float f492 = f46 + dp8;
                                            this.rect.set(i5 - AndroidUtilities.dp(5.0f), f482, i5 - AndroidUtilities.dp(3.0f), f492);
                                            canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                                            this.rect.set(AndroidUtilities.dp(3.0f) + i5, f482, i5 + AndroidUtilities.dp(5.0f), f492);
                                            canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                                            if (this.chatCallProgress >= f3) {
                                            }
                                            canvas.restore();
                                            float f502 = (float) j2;
                                            f11 = this.innerProgress + (f502 / 400.0f);
                                            this.innerProgress = f11;
                                            if (f11 >= f3) {
                                            }
                                            z4 = false;
                                            if (!this.hasCall) {
                                            }
                                            z = true;
                                            r9 = z4;
                                            if (this.translationX != f6) {
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
                                        dp8 = dp9 - (dp10 * f10);
                                        if (this.chatCallProgress >= f3) {
                                        }
                                        canvas.save();
                                        float f4722 = this.chatCallProgress;
                                        canvas3.scale(f4722 * progress, f4722 * progress, f45, f46);
                                        this.rect.set(i5 - AndroidUtilities.dp(f3), f46 - dp5, AndroidUtilities.dp(f3) + i5, dp5 + f46);
                                        canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                                        float f4822 = f46 - dp8;
                                        float f4922 = f46 + dp8;
                                        this.rect.set(i5 - AndroidUtilities.dp(5.0f), f4822, i5 - AndroidUtilities.dp(3.0f), f4922);
                                        canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                                        this.rect.set(AndroidUtilities.dp(3.0f) + i5, f4822, i5 + AndroidUtilities.dp(5.0f), f4922);
                                        canvas3.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                                        if (this.chatCallProgress >= f3) {
                                        }
                                        canvas.restore();
                                        float f5022 = (float) j2;
                                        f11 = this.innerProgress + (f5022 / 400.0f);
                                        this.innerProgress = f11;
                                        if (f11 >= f3) {
                                        }
                                        z4 = false;
                                        if (!this.hasCall) {
                                        }
                                        z = true;
                                        r9 = z4;
                                        if (this.translationX != f6) {
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
                            f6 = 0.0f;
                            r9 = 0;
                            if (this.translationX != f6) {
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
                        f6 = 0.0f;
                        r9 = 0;
                        if (this.translationX != f6) {
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
                } else {
                    j = j4;
                    str3 = "windowBackgroundWhite";
                    canvas2 = canvas;
                }
                i3 = 1;
                i4 = 4;
                if (this.currentDialogFolderId == 0) {
                }
                if (this.dialogsType == 2) {
                }
                if (this.drawVerified) {
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
                if (this.currentDialogFolderId != 0) {
                }
                this.avatarImage.draw(canvas3);
                if (this.hasMessageThumb) {
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
                    r9 = 0;
                    if (this.translationX != f6) {
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
                f6 = 0.0f;
                r9 = 0;
                if (this.translationX != f6) {
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
        if (this.messageNameLayout != null) {
        }
        if (this.messageLayout == null) {
        }
        i3 = 1;
        i4 = 4;
        if (this.currentDialogFolderId == 0) {
        }
        if (this.dialogsType == 2) {
        }
        if (this.drawVerified) {
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
        if (this.currentDialogFolderId != 0) {
        }
        this.avatarImage.draw(canvas3);
        if (this.hasMessageThumb) {
        }
        if (this.animatingArchiveAvatar) {
        }
        if (this.isDialogCell) {
        }
        j2 = j;
        f6 = 0.0f;
        r9 = 0;
        if (this.translationX != f6) {
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
        this.statusDrawableAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                DialogCell.this.lambda$createStatusDrawableAnimator$2(valueAnimator);
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
    public /* synthetic */ void lambda$createStatusDrawableAnimator$2(ValueAnimator valueAnimator) {
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
        boolean z3 = this.drawPin;
        if ((!z3 && z) || this.drawReorder == z) {
            if (z3) {
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
            if (!this.message.isMediaEmpty() && !TextUtils.isEmpty(this.message.caption)) {
                sb2.append(". ");
                sb2.append(this.message.caption);
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
