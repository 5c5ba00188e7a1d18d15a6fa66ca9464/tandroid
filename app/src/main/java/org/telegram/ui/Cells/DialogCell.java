package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
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
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$DraftMessage;
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
    private TLRPC$EncryptedChat encryptedChat;
    private int errorLeft;
    private int errorTop;
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
    private int nameLeft;
    private int nameLockLeft;
    private int nameLockTop;
    private int nameMuteLeft;
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
        int i5;
        if (this.currentDialogId == 0 && this.customDialog == null) {
            return;
        }
        if (this.checkBox != null) {
            float f = 45.0f;
            if (LocaleController.isRTL) {
                int i6 = i3 - i;
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    f = 43.0f;
                }
                i5 = i6 - AndroidUtilities.dp(f);
            } else {
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    f = 43.0f;
                }
                i5 = AndroidUtilities.dp(f);
            }
            int dp = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 48.0f : 42.0f);
            CheckBox2 checkBox2 = this.checkBox;
            checkBox2.layout(i5, dp, checkBox2.getMeasuredWidth() + i5, this.checkBox.getMeasuredHeight() + dp);
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
        String str;
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
                str = tLRPC$Chat.title.replace('\n', ' ');
            } else if (tLRPC$User == null) {
                continue;
            } else if (UserObject.isDeleted(tLRPC$User)) {
                str = LocaleController.getString("HiddenName", R.string.HiddenName);
            } else {
                str = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name).replace('\n', ' ');
            }
            if (spannableStringBuilder.length() > 0) {
                spannableStringBuilder.append((CharSequence) ", ");
            }
            int length = spannableStringBuilder.length();
            int length2 = str.length() + length;
            spannableStringBuilder.append((CharSequence) str);
            if (tLRPC$Dialog.unread_count > 0) {
                spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM), 0, Theme.getColor("chats_nameArchived", this.resourcesProvider)), length, length2, 33);
            }
            if (spannableStringBuilder.length() > 150) {
                break;
            }
        }
        return Emoji.replaceEmoji(spannableStringBuilder, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(17:456|(2:458|(1:460)(1:461))(2:462|(2:464|(1:466)(1:467))(2:468|(1:470)(14:472|(2:474|(1:476)(1:477))(1:478)|479|1160|480|(1:482)(1:483)|484|518|(1:534)(6:1164|526|527|1166|528|529)|535|(1:539)|540|(4:542|(1:544)|545|(1:547)(1:548))|549)))|471|479|1160|480|(0)(0)|484|518|(1:520)|522|534|535|(2:537|539)|540|(0)|549) */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x0472, code lost:
        if (r2.post_messages == false) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x047e, code lost:
        if (r2.kicked != false) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0af0, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x0af1, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:657:0x0ec5, code lost:
        if (r3 != null) goto L659;
     */
    /* JADX WARN: Removed duplicated region for block: B:1016:0x17a6 A[Catch: Exception -> 0x1868, TryCatch #6 {Exception -> 0x1868, blocks: (B:1000:0x1771, B:1002:0x1775, B:1004:0x1779, B:1006:0x177d, B:1008:0x1782, B:1010:0x178d, B:1013:0x1793, B:1014:0x17a2, B:1016:0x17a6, B:1018:0x17ba, B:1020:0x17c0, B:1022:0x17c4, B:1024:0x17ce, B:1025:0x17d1, B:1026:0x17d4, B:1028:0x17d8, B:1031:0x17dd, B:1033:0x17e1, B:1035:0x17ed, B:1036:0x17f7, B:1037:0x180f, B:1040:0x1815, B:1041:0x181c, B:1045:0x1832, B:1046:0x1842), top: B:1172:0x1771 }] */
    /* JADX WARN: Removed duplicated region for block: B:1033:0x17e1 A[Catch: Exception -> 0x1868, TryCatch #6 {Exception -> 0x1868, blocks: (B:1000:0x1771, B:1002:0x1775, B:1004:0x1779, B:1006:0x177d, B:1008:0x1782, B:1010:0x178d, B:1013:0x1793, B:1014:0x17a2, B:1016:0x17a6, B:1018:0x17ba, B:1020:0x17c0, B:1022:0x17c4, B:1024:0x17ce, B:1025:0x17d1, B:1026:0x17d4, B:1028:0x17d8, B:1031:0x17dd, B:1033:0x17e1, B:1035:0x17ed, B:1036:0x17f7, B:1037:0x180f, B:1040:0x1815, B:1041:0x181c, B:1045:0x1832, B:1046:0x1842), top: B:1172:0x1771 }] */
    /* JADX WARN: Removed duplicated region for block: B:1043:0x182d  */
    /* JADX WARN: Removed duplicated region for block: B:1044:0x1830  */
    /* JADX WARN: Removed duplicated region for block: B:1051:0x1873  */
    /* JADX WARN: Removed duplicated region for block: B:1102:0x19da  */
    /* JADX WARN: Removed duplicated region for block: B:1135:0x1a6c  */
    /* JADX WARN: Removed duplicated region for block: B:1139:0x1a7d A[Catch: Exception -> 0x1aa8, TryCatch #5 {Exception -> 0x1aa8, blocks: (B:1137:0x1a70, B:1139:0x1a7d, B:1140:0x1a7f, B:1142:0x1a98, B:1143:0x1a9f), top: B:1170:0x1a70 }] */
    /* JADX WARN: Removed duplicated region for block: B:1142:0x1a98 A[Catch: Exception -> 0x1aa8, TryCatch #5 {Exception -> 0x1aa8, blocks: (B:1137:0x1a70, B:1139:0x1a7d, B:1140:0x1a7f, B:1142:0x1a98, B:1143:0x1a9f), top: B:1170:0x1a70 }] */
    /* JADX WARN: Removed duplicated region for block: B:1148:0x1ab0  */
    /* JADX WARN: Removed duplicated region for block: B:1183:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0423  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x042f  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0484  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0489  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x050e  */
    /* JADX WARN: Removed duplicated region for block: B:482:0x0add A[Catch: Exception -> 0x0af0, TryCatch #0 {Exception -> 0x0af0, blocks: (B:480:0x0ad2, B:482:0x0add, B:484:0x0ae5), top: B:1160:0x0ad2 }] */
    /* JADX WARN: Removed duplicated region for block: B:483:0x0ae4  */
    /* JADX WARN: Removed duplicated region for block: B:520:0x0b97  */
    /* JADX WARN: Removed duplicated region for block: B:537:0x0be2  */
    /* JADX WARN: Removed duplicated region for block: B:542:0x0bf3  */
    /* JADX WARN: Removed duplicated region for block: B:553:0x0c50  */
    /* JADX WARN: Removed duplicated region for block: B:637:0x0e1e  */
    /* JADX WARN: Removed duplicated region for block: B:642:0x0e2e  */
    /* JADX WARN: Removed duplicated region for block: B:660:0x0ece  */
    /* JADX WARN: Removed duplicated region for block: B:663:0x0ed8  */
    /* JADX WARN: Removed duplicated region for block: B:667:0x0ee6  */
    /* JADX WARN: Removed duplicated region for block: B:668:0x0eee  */
    /* JADX WARN: Removed duplicated region for block: B:677:0x0f0b  */
    /* JADX WARN: Removed duplicated region for block: B:678:0x0f1f  */
    /* JADX WARN: Removed duplicated region for block: B:741:0x1041  */
    /* JADX WARN: Removed duplicated region for block: B:755:0x10a0  */
    /* JADX WARN: Removed duplicated region for block: B:759:0x10a9  */
    /* JADX WARN: Removed duplicated region for block: B:761:0x10b9  */
    /* JADX WARN: Removed duplicated region for block: B:783:0x110b  */
    /* JADX WARN: Removed duplicated region for block: B:785:0x1117  */
    /* JADX WARN: Removed duplicated region for block: B:789:0x1156  */
    /* JADX WARN: Removed duplicated region for block: B:792:0x1161  */
    /* JADX WARN: Removed duplicated region for block: B:793:0x1171  */
    /* JADX WARN: Removed duplicated region for block: B:796:0x1189  */
    /* JADX WARN: Removed duplicated region for block: B:799:0x119d  */
    /* JADX WARN: Removed duplicated region for block: B:804:0x11cb  */
    /* JADX WARN: Removed duplicated region for block: B:827:0x126e  */
    /* JADX WARN: Removed duplicated region for block: B:830:0x1284  */
    /* JADX WARN: Removed duplicated region for block: B:847:0x12cd  */
    /* JADX WARN: Removed duplicated region for block: B:870:0x1436  */
    /* JADX WARN: Removed duplicated region for block: B:871:0x1454  */
    /* JADX WARN: Removed duplicated region for block: B:875:0x14a1  */
    /* JADX WARN: Removed duplicated region for block: B:881:0x14c6  */
    /* JADX WARN: Removed duplicated region for block: B:886:0x14f6  */
    /* JADX WARN: Removed duplicated region for block: B:956:0x1696  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void buildLayout() {
        int i;
        boolean z;
        String str;
        String str2;
        int i2;
        int i3;
        int i4;
        SpannableStringBuilder spannableStringBuilder;
        CharSequence charSequence;
        TLRPC$DraftMessage tLRPC$DraftMessage;
        String str3;
        MessageObject messageObject;
        String str4;
        boolean z2;
        String str5;
        String str6;
        int i5;
        String str7;
        SpannableStringBuilder spannableStringBuilder2;
        String str8;
        String userName;
        String str9;
        int i6;
        SpannableStringBuilder spannableStringBuilder3;
        String string;
        boolean z3;
        boolean z4;
        boolean z5;
        String str10;
        String str11;
        MessageObject messageObject2;
        String str12;
        String str13;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$User tLRPC$User;
        String str14;
        String str15;
        boolean z6;
        TLRPC$Chat tLRPC$Chat2;
        boolean isEmpty;
        CharSequence charSequence2;
        CharSequence charSequence3;
        CharSequence charSequence4;
        CharSequence charSequence5;
        String str16;
        MessageObject messageObject3;
        String str17;
        String str18;
        String str19;
        TLRPC$Message tLRPC$Message;
        String str20;
        SpannableStringBuilder spannableStringBuilder4;
        int i7;
        Exception e;
        int i8;
        ForegroundColorSpanThemable foregroundColorSpanThemable;
        SpannableStringBuilder replaceEmoji;
        CharSequence highlightText;
        SpannableStringBuilder valueOf;
        TLRPC$Message tLRPC$Message2;
        char c;
        String str21;
        String str22;
        TLRPC$Message tLRPC$Message3;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        String str23;
        String str24;
        ArrayList<TLRPC$MessageEntity> arrayList;
        SpannableStringBuilder spannableStringBuilder5;
        MessageObject messageObject4;
        boolean z7;
        CharSequence charSequence6;
        int i9;
        int i10;
        int i11;
        int i12;
        CharSequence charSequence7;
        int i13;
        int i14;
        int max;
        boolean z8;
        StaticLayout staticLayout;
        StaticLayout staticLayout2;
        float f;
        float f2;
        int i15;
        int length;
        int i16;
        int ceil;
        int lineCount;
        int lineCount2;
        boolean z9;
        CharacterStyle[] characterStyleArr;
        MessageObject messageObject5;
        CharSequence highlightText2;
        CharSequence charSequence8;
        CharSequence highlightText3;
        int i17;
        int i18;
        int i19;
        int i20;
        int dp;
        CharSequence highlightText4;
        int i21;
        CharSequence charSequence9;
        String str25;
        SpannableStringBuilder spannableStringBuilder6;
        int i22 = 1;
        int i23 = 0;
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
        CharSequence charSequence10 = messageObject6 != null ? messageObject6.messageText : null;
        boolean z11 = charSequence10 instanceof Spannable;
        SpannableStringBuilder spannableStringBuilder7 = charSequence10;
        if (z11) {
            SpannableStringBuilder spannableStringBuilder8 = new SpannableStringBuilder(charSequence10);
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
                charSequence = LocaleController.getString("FromYou", R.string.FromYou);
                CustomDialog customDialog3 = this.customDialog;
                if (customDialog3.isMedia) {
                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                    spannableStringBuilder6 = SpannableStringBuilder.valueOf(String.format(str, this.message.messageText));
                    spannableStringBuilder6.setSpan(new ForegroundColorSpanThemable("chats_attachMessage", this.resourcesProvider), 0, spannableStringBuilder6.length(), 33);
                } else {
                    String str26 = customDialog3.message;
                    if (str26.length() > 150) {
                        str26 = str26.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                    }
                    spannableStringBuilder6 = (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? SpannableStringBuilder.valueOf(String.format(str, str26, charSequence)) : SpannableStringBuilder.valueOf(String.format(str, str26.replace('\n', ' '), charSequence));
                }
                charSequence9 = Emoji.replaceEmoji(spannableStringBuilder6, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                i6 = 0;
            } else {
                charSequence9 = customDialog2.message;
                if (customDialog2.isMedia) {
                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                }
                charSequence = null;
                i6 = 1;
            }
            str7 = LocaleController.stringForMessageListDate(this.customDialog.date);
            int i24 = this.customDialog.unread_count;
            if (i24 != 0) {
                this.drawCount = true;
                str25 = String.format("%d", Integer.valueOf(i24));
            } else {
                this.drawCount = false;
                str25 = null;
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
            str8 = customDialog4.name;
            str9 = charSequence9;
            str6 = str25;
            i3 = -1;
            str4 = null;
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
                TLRPC$Chat tLRPC$Chat3 = this.chat;
                if (tLRPC$Chat3 != null) {
                    if (tLRPC$Chat3.scam) {
                        this.drawScam = 1;
                        Theme.dialogs_scamDrawable.checkText();
                    } else if (tLRPC$Chat3.fake) {
                        this.drawScam = 2;
                        Theme.dialogs_fakeDrawable.checkText();
                    } else {
                        this.drawVerified = tLRPC$Chat3.verified;
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
                                z7 = true;
                                this.drawPremium = z7;
                                i2 = this.lastMessageDate;
                                if (i2 == 0 && (messageObject4 = this.message) != null) {
                                    i2 = messageObject4.messageOwner.date;
                                }
                                if (this.isDialogCell) {
                                    TLRPC$DraftMessage draft = MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, 0);
                                    this.draftMessage = draft;
                                    if (draft == null || ((!TextUtils.isEmpty(draft.message) || this.draftMessage.reply_to_msg_id != 0) && (i2 <= this.draftMessage.date || this.unreadCount == 0))) {
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
                                        TLRPC$Chat tLRPC$Chat5 = this.chat;
                                        if (tLRPC$Chat5 != null) {
                                            if (!tLRPC$Chat5.left) {
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
                                    SpannableStringBuilder spannableStringBuilder9 = new SpannableStringBuilder();
                                    CharSequence replace = TextUtils.replace(printingString, new String[]{"..."}, new String[]{""});
                                    i3 = this.printingStringType == 5 ? replace.toString().indexOf("**oo**") : -1;
                                    if (i3 >= 0) {
                                        spannableStringBuilder9.append(replace).setSpan(new FixedWidthSpan(Theme.getChatStatusDrawable(this.printingStringType).getIntrinsicWidth()), i3, i3 + 6, 0);
                                    } else {
                                        spannableStringBuilder9.append((CharSequence) " ").append(replace).setSpan(new FixedWidthSpan(intrinsicWidth), 0, 1, 0);
                                    }
                                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                    charSequence = null;
                                    i4 = 0;
                                    i22 = 0;
                                    i23 = 1;
                                    spannableStringBuilder = spannableStringBuilder9;
                                } else {
                                    this.lastPrintString = null;
                                    if (this.draftMessage != null) {
                                        charSequence = LocaleController.getString("Draft", R.string.Draft);
                                        if (TextUtils.isEmpty(this.draftMessage.message)) {
                                            if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                                spannableStringBuilder5 = "";
                                            } else {
                                                SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(charSequence);
                                                valueOf2.setSpan(new ForegroundColorSpanThemable("chats_draft", this.resourcesProvider), 0, charSequence.length(), 33);
                                                spannableStringBuilder5 = valueOf2;
                                            }
                                        } else {
                                            String str27 = this.draftMessage.message;
                                            if (str27.length() > 150) {
                                                str27 = str27.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                            }
                                            SpannableStringBuilder spannableStringBuilder10 = new SpannableStringBuilder(str27);
                                            MediaDataController.addTextStyleRuns(this.draftMessage, spannableStringBuilder10, 256);
                                            TLRPC$DraftMessage tLRPC$DraftMessage2 = this.draftMessage;
                                            if (tLRPC$DraftMessage2 != null && (arrayList = tLRPC$DraftMessage2.entities) != null) {
                                                MediaDataController.addAnimatedEmojiSpans(arrayList, spannableStringBuilder10, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                            }
                                            SpannableStringBuilder formatSpannable = AndroidUtilities.formatSpannable(str2, AndroidUtilities.replaceNewLines(spannableStringBuilder10), charSequence);
                                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                                formatSpannable.setSpan(new ForegroundColorSpanThemable("chats_draft", this.resourcesProvider), 0, charSequence.length() + 1, 33);
                                            }
                                            spannableStringBuilder5 = Emoji.replaceEmoji(formatSpannable, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                        }
                                        i4 = 0;
                                        i22 = 0;
                                        str13 = spannableStringBuilder5;
                                    } else {
                                        String str28 = str2;
                                        if (this.clearingDialog) {
                                            textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                            str24 = LocaleController.getString("HistoryCleared", R.string.HistoryCleared);
                                        } else {
                                            MessageObject messageObject7 = this.message;
                                            if (messageObject7 == null) {
                                                TLRPC$EncryptedChat tLRPC$EncryptedChat = this.encryptedChat;
                                                if (tLRPC$EncryptedChat != null) {
                                                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                    if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatRequested) {
                                                        str24 = LocaleController.getString("EncryptionProcessing", R.string.EncryptionProcessing);
                                                    } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatWaiting) {
                                                        str24 = LocaleController.formatString("AwaitingEncryption", R.string.AwaitingEncryption, UserObject.getFirstName(this.user));
                                                    } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatDiscarded) {
                                                        str24 = LocaleController.getString("EncryptionRejected", R.string.EncryptionRejected);
                                                    } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat) {
                                                        str24 = tLRPC$EncryptedChat.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId() ? LocaleController.formatString("EncryptedChatStartedOutgoing", R.string.EncryptedChatStartedOutgoing, UserObject.getFirstName(this.user)) : LocaleController.getString("EncryptedChatStartedIncoming", R.string.EncryptedChatStartedIncoming);
                                                    }
                                                } else if (this.dialogsType == 3 && UserObject.isUserSelf(this.user)) {
                                                    charSequence = null;
                                                    i4 = 0;
                                                    z10 = false;
                                                    str12 = LocaleController.getString("SavedMessagesInfo", R.string.SavedMessagesInfo);
                                                    i3 = -1;
                                                    spannableStringBuilder = str12;
                                                }
                                                str24 = "";
                                            } else {
                                                String restrictionReason = MessagesController.getRestrictionReason(messageObject7.messageOwner.restriction_reason);
                                                long fromChatId = this.message.getFromChatId();
                                                if (DialogObject.isUserDialog(fromChatId)) {
                                                    tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(fromChatId));
                                                    tLRPC$Chat = null;
                                                } else {
                                                    tLRPC$Chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-fromChatId));
                                                    tLRPC$User = null;
                                                }
                                                this.drawCount2 = true;
                                                int i25 = this.dialogsType;
                                                if (i25 == 2) {
                                                    TLRPC$Chat tLRPC$Chat6 = this.chat;
                                                    if (tLRPC$Chat6 != null) {
                                                        if (ChatObject.isChannel(tLRPC$Chat6)) {
                                                            TLRPC$Chat tLRPC$Chat7 = this.chat;
                                                            if (!tLRPC$Chat7.megagroup) {
                                                                int i26 = tLRPC$Chat7.participants_count;
                                                                if (i26 != 0) {
                                                                    str15 = LocaleController.formatPluralStringComma("Subscribers", i26);
                                                                } else if (TextUtils.isEmpty(tLRPC$Chat7.username)) {
                                                                    str15 = LocaleController.getString("ChannelPrivate", R.string.ChannelPrivate).toLowerCase();
                                                                } else {
                                                                    str15 = LocaleController.getString("ChannelPublic", R.string.ChannelPublic).toLowerCase();
                                                                }
                                                            }
                                                        }
                                                        TLRPC$Chat tLRPC$Chat8 = this.chat;
                                                        int i27 = tLRPC$Chat8.participants_count;
                                                        if (i27 != 0) {
                                                            str15 = LocaleController.formatPluralStringComma("Members", i27);
                                                        } else if (tLRPC$Chat8.has_geo) {
                                                            str15 = LocaleController.getString("MegaLocation", R.string.MegaLocation);
                                                        } else if (TextUtils.isEmpty(tLRPC$Chat8.username)) {
                                                            str15 = LocaleController.getString("MegaPrivate", R.string.MegaPrivate).toLowerCase();
                                                        } else {
                                                            str15 = LocaleController.getString("MegaPublic", R.string.MegaPublic).toLowerCase();
                                                        }
                                                    } else {
                                                        str15 = "";
                                                    }
                                                    this.drawCount2 = false;
                                                } else if (i25 == 3 && UserObject.isUserSelf(this.user)) {
                                                    str15 = LocaleController.getString("SavedMessagesInfo", R.string.SavedMessagesInfo);
                                                } else {
                                                    if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout && this.currentDialogFolderId != 0) {
                                                        str14 = formatArchivedDialogNames();
                                                        charSequence = null;
                                                        i4 = 0;
                                                    } else {
                                                        if (this.message.messageOwner instanceof TLRPC$TL_messageService) {
                                                            if (ChatObject.isChannelAndNotMegaGroup(this.chat) && (this.message.messageOwner.action instanceof TLRPC$TL_messageActionChannelMigrateFrom)) {
                                                                spannableStringBuilder7 = "";
                                                                z10 = false;
                                                            }
                                                            textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                            charSequence3 = spannableStringBuilder7;
                                                            charSequence = null;
                                                            i4 = 0;
                                                        } else {
                                                            if (TextUtils.isEmpty(restrictionReason) && this.currentDialogFolderId == 0 && this.encryptedChat == null && !this.message.needDrawBluredPreview() && (this.message.isPhoto() || this.message.isNewGif() || this.message.isVideo())) {
                                                                String str29 = this.message.isWebpage() ? this.message.messageOwner.media.webpage.type : null;
                                                                if (!"app".equals(str29) && !"profile".equals(str29) && !"article".equals(str29) && (str29 == null || !str29.startsWith("telegram_"))) {
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
                                                                        z6 = false;
                                                                        tLRPC$Chat2 = this.chat;
                                                                        if (tLRPC$Chat2 == null && tLRPC$Chat2.id > 0 && tLRPC$Chat == null && (!ChatObject.isChannel(tLRPC$Chat2) || ChatObject.isMegagroup(this.chat))) {
                                                                            if (this.message.isOutOwner()) {
                                                                                str20 = LocaleController.getString("FromYou", R.string.FromYou);
                                                                            } else {
                                                                                MessageObject messageObject9 = this.message;
                                                                                if (messageObject9 != null && (tLRPC$MessageFwdHeader = messageObject9.messageOwner.fwd_from) != null && (str23 = tLRPC$MessageFwdHeader.from_name) != null) {
                                                                                    str20 = str23;
                                                                                } else if (tLRPC$User == null) {
                                                                                    str20 = "DELETED";
                                                                                } else if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                                                                    if (UserObject.isDeleted(tLRPC$User)) {
                                                                                        str20 = LocaleController.getString("HiddenName", R.string.HiddenName);
                                                                                    } else {
                                                                                        str20 = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name).replace("\n", "");
                                                                                    }
                                                                                } else {
                                                                                    str20 = UserObject.getFirstName(tLRPC$User).replace("\n", "");
                                                                                }
                                                                            }
                                                                            if (!TextUtils.isEmpty(restrictionReason)) {
                                                                                valueOf = SpannableStringBuilder.valueOf(String.format(str28, restrictionReason, str20));
                                                                            } else {
                                                                                MessageObject messageObject10 = this.message;
                                                                                CharSequence charSequence11 = messageObject10.caption;
                                                                                if (charSequence11 != null) {
                                                                                    CharSequence charSequence12 = charSequence11.toString();
                                                                                    if (!z6) {
                                                                                        str22 = "";
                                                                                    } else if (this.message.isVideo()) {
                                                                                        str22 = "📹 ";
                                                                                    } else if (this.message.isVoice()) {
                                                                                        str22 = "🎤 ";
                                                                                    } else if (this.message.isMusic()) {
                                                                                        str22 = "🎧 ";
                                                                                    } else {
                                                                                        str22 = this.message.isPhoto() ? "🖼 " : "📎 ";
                                                                                    }
                                                                                    if (this.message.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                        String str30 = this.message.messageTrimmedToHighlight;
                                                                                        int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(119.0f);
                                                                                        if (z) {
                                                                                            if (!TextUtils.isEmpty(str20)) {
                                                                                                measuredWidth = (int) (measuredWidth - textPaint5.measureText(str20.toString()));
                                                                                            }
                                                                                            measuredWidth = (int) (measuredWidth - textPaint5.measureText(": "));
                                                                                        }
                                                                                        if (measuredWidth > 0) {
                                                                                            str30 = AndroidUtilities.ellipsizeCenterEnd(str30, this.message.highlightedWords.get(0), measuredWidth, textPaint5, 130).toString();
                                                                                        }
                                                                                        valueOf = new SpannableStringBuilder(str22).append((CharSequence) str30);
                                                                                    } else {
                                                                                        if (charSequence12.length() > 150) {
                                                                                            charSequence12 = charSequence12.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                        }
                                                                                        SpannableStringBuilder spannableStringBuilder11 = new SpannableStringBuilder(charSequence12);
                                                                                        MediaDataController.addTextStyleRuns(this.message.messageOwner.entities, charSequence12, spannableStringBuilder11, 256);
                                                                                        MessageObject messageObject11 = this.message;
                                                                                        if (messageObject11 != null && (tLRPC$Message3 = messageObject11.messageOwner) != null) {
                                                                                            MediaDataController.addAnimatedEmojiSpans(tLRPC$Message3.entities, spannableStringBuilder11, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                        }
                                                                                        valueOf = AndroidUtilities.formatSpannable(str28, new SpannableStringBuilder(str22).append(AndroidUtilities.replaceNewLines(spannableStringBuilder11)), str20);
                                                                                    }
                                                                                } else if (messageObject10.messageOwner.media != null && !messageObject10.isMediaEmpty()) {
                                                                                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                    MessageObject messageObject12 = this.message;
                                                                                    TLRPC$MessageMedia tLRPC$MessageMedia = messageObject12.messageOwner.media;
                                                                                    if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                                                                                        TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia;
                                                                                        str21 = Build.VERSION.SDK_INT >= 18 ? String.format("📊 \u2068%s\u2069", tLRPC$TL_messageMediaPoll.poll.question) : String.format("📊 %s", tLRPC$TL_messageMediaPoll.poll.question);
                                                                                    } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                                                                                        str21 = Build.VERSION.SDK_INT >= 18 ? String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title) : String.format("🎮 %s", tLRPC$MessageMedia.game.title);
                                                                                    } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                        str21 = tLRPC$MessageMedia.title;
                                                                                    } else {
                                                                                        if (messageObject12.type == 14) {
                                                                                            if (Build.VERSION.SDK_INT >= 18) {
                                                                                                c = 1;
                                                                                                str21 = String.format("🎧 \u2068%s - %s\u2069", messageObject12.getMusicAuthor(), this.message.getMusicTitle());
                                                                                            } else {
                                                                                                c = 1;
                                                                                                str21 = String.format("🎧 %s - %s", messageObject12.getMusicAuthor(), this.message.getMusicTitle());
                                                                                            }
                                                                                        } else {
                                                                                            c = 1;
                                                                                            str21 = spannableStringBuilder7.toString();
                                                                                        }
                                                                                        CharSequence[] charSequenceArr = new CharSequence[2];
                                                                                        charSequenceArr[0] = str21.replace('\n', ' ');
                                                                                        charSequenceArr[c] = str20;
                                                                                        spannableStringBuilder4 = AndroidUtilities.formatSpannable(str28, charSequenceArr);
                                                                                        spannableStringBuilder4.setSpan(new ForegroundColorSpanThemable("chats_attachMessage", this.resourcesProvider), !z ? str20.length() + 2 : 0, spannableStringBuilder4.length(), 33);
                                                                                        if ((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && (this.currentDialogFolderId == 0 || spannableStringBuilder4.length() <= 0)) {
                                                                                            i8 = 0;
                                                                                            i7 = 0;
                                                                                        } else {
                                                                                            try {
                                                                                                foregroundColorSpanThemable = new ForegroundColorSpanThemable("chats_nameMessage", this.resourcesProvider);
                                                                                                i7 = str20.length() + 1;
                                                                                            } catch (Exception e2) {
                                                                                                e = e2;
                                                                                                i7 = 0;
                                                                                            }
                                                                                            try {
                                                                                                spannableStringBuilder4.setSpan(foregroundColorSpanThemable, 0, i7, 33);
                                                                                                i8 = i7;
                                                                                            } catch (Exception e3) {
                                                                                                e = e3;
                                                                                                FileLog.e(e);
                                                                                                i8 = 0;
                                                                                                replaceEmoji = Emoji.replaceEmoji(spannableStringBuilder4, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                if (this.message.hasHighlightedWords()) {
                                                                                                }
                                                                                                if (this.hasMessageThumb) {
                                                                                                }
                                                                                                i4 = i8;
                                                                                                charSequence = str20;
                                                                                                i22 = 1;
                                                                                                i23 = 0;
                                                                                                str14 = replaceEmoji;
                                                                                                if (this.currentDialogFolderId != 0) {
                                                                                                }
                                                                                                i3 = -1;
                                                                                                int i28 = i23;
                                                                                                i23 = i22;
                                                                                                i22 = i28;
                                                                                                spannableStringBuilder = str14;
                                                                                                if (this.draftMessage == null) {
                                                                                                }
                                                                                                messageObject = this.message;
                                                                                                if (messageObject != null) {
                                                                                                }
                                                                                                this.promoDialog = z2;
                                                                                                MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                                                                                                str6 = str5;
                                                                                                if (this.dialogsType != 0) {
                                                                                                }
                                                                                                spannableStringBuilder2 = spannableStringBuilder3;
                                                                                                str7 = str3;
                                                                                                if (this.currentDialogFolderId == 0) {
                                                                                                }
                                                                                                str9 = spannableStringBuilder2;
                                                                                                i6 = i22;
                                                                                                i22 = i23;
                                                                                                i23 = i5;
                                                                                                charSequence6 = charSequence;
                                                                                                if (i22 == 0) {
                                                                                                }
                                                                                                if (LocaleController.isRTL) {
                                                                                                }
                                                                                                if (this.drawNameLock) {
                                                                                                }
                                                                                                if (!this.drawClock) {
                                                                                                }
                                                                                                if (!this.dialogMuted) {
                                                                                                }
                                                                                                if (!this.drawVerified) {
                                                                                                }
                                                                                                dp = i10 - AndroidUtilities.dp(12.0f);
                                                                                                if (dp < 0) {
                                                                                                }
                                                                                                CharSequence replaceEmoji2 = Emoji.replaceEmoji(TextUtils.ellipsize(str8.replace('\n', ' '), Theme.dialogs_namePaint[this.paintIndex], dp, TextUtils.TruncateAt.END), Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                MessageObject messageObject13 = this.message;
                                                                                                this.nameLayout = new StaticLayout((messageObject13 != null || !messageObject13.hasHighlightedWords() || (highlightText4 = AndroidUtilities.highlightText(replaceEmoji2, this.message.highlightedWords, this.resourcesProvider)) == null) ? replaceEmoji2 : highlightText4, Theme.dialogs_namePaint[this.paintIndex], i10, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                                                                                                if (!this.useForceThreeLines) {
                                                                                                }
                                                                                                i11 = i23;
                                                                                                charSequence7 = "";
                                                                                                i12 = i3;
                                                                                                int dp2 = AndroidUtilities.dp(11.0f);
                                                                                                this.messageNameTop = AndroidUtilities.dp(32.0f);
                                                                                                this.timeTop = AndroidUtilities.dp(13.0f);
                                                                                                this.errorTop = AndroidUtilities.dp(43.0f);
                                                                                                this.pinTop = AndroidUtilities.dp(43.0f);
                                                                                                this.countTop = AndroidUtilities.dp(43.0f);
                                                                                                this.checkDrawTop = AndroidUtilities.dp(13.0f);
                                                                                                int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp(93.0f);
                                                                                                if (!LocaleController.isRTL) {
                                                                                                }
                                                                                                this.avatarImage.setImageCoords(i18, dp2, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                                                                                                this.thumbImage.setImageCoords(i17, AndroidUtilities.dp(31.0f) + dp2, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                                                                                                i14 = dp2;
                                                                                                i13 = measuredWidth2;
                                                                                                if (this.drawPin) {
                                                                                                }
                                                                                                if (!this.drawError) {
                                                                                                }
                                                                                                int i29 = i13;
                                                                                                if (i6 != 0) {
                                                                                                }
                                                                                                max = Math.max(AndroidUtilities.dp(12.0f), i29);
                                                                                                z8 = this.useForceThreeLines;
                                                                                                if (!z8) {
                                                                                                }
                                                                                                try {
                                                                                                    messageObject5 = this.message;
                                                                                                    if (messageObject5 != null) {
                                                                                                        charSequence6 = highlightText2;
                                                                                                    }
                                                                                                    this.messageNameLayout = StaticLayoutEx.createStaticLayout(charSequence6, Theme.dialogs_messageNamePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max, 1);
                                                                                                } catch (Exception e4) {
                                                                                                    FileLog.e(e4);
                                                                                                }
                                                                                                this.messageTop = AndroidUtilities.dp(51.0f);
                                                                                                this.thumbImage.setImageY(i14 + AndroidUtilities.dp(40.0f));
                                                                                                z9 = this.useForceThreeLines;
                                                                                                if (!z9) {
                                                                                                }
                                                                                                textPaint5 = Theme.dialogs_messagePaint[this.paintIndex];
                                                                                                str9 = charSequence6;
                                                                                                charSequence6 = null;
                                                                                                if (str9 instanceof Spannable) {
                                                                                                }
                                                                                                if (!this.useForceThreeLines) {
                                                                                                    if (this.hasMessageThumb) {
                                                                                                    }
                                                                                                    this.messageLayout = new StaticLayout(str9, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
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
                                                                                                if (this.hasMessageThumb) {
                                                                                                    max += AndroidUtilities.dp(6.0f);
                                                                                                }
                                                                                                this.messageLayout = StaticLayoutEx.createStaticLayout(str9, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence6 == null ? 1 : 2);
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
                                                                                        replaceEmoji = Emoji.replaceEmoji(spannableStringBuilder4, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                        if (this.message.hasHighlightedWords() && (highlightText = AndroidUtilities.highlightText(replaceEmoji, this.message.highlightedWords, this.resourcesProvider)) != null) {
                                                                                            replaceEmoji = highlightText;
                                                                                        }
                                                                                        if (this.hasMessageThumb) {
                                                                                            boolean z12 = replaceEmoji instanceof SpannableStringBuilder;
                                                                                            replaceEmoji = replaceEmoji;
                                                                                            if (!z12) {
                                                                                                replaceEmoji = new SpannableStringBuilder(replaceEmoji);
                                                                                            }
                                                                                            SpannableStringBuilder spannableStringBuilder12 = (SpannableStringBuilder) replaceEmoji;
                                                                                            if (i7 >= spannableStringBuilder12.length()) {
                                                                                                spannableStringBuilder12.append((CharSequence) " ");
                                                                                                spannableStringBuilder12.setSpan(new FixedWidthSpan(AndroidUtilities.dp(i + 6)), spannableStringBuilder12.length() - 1, spannableStringBuilder12.length(), 33);
                                                                                            } else {
                                                                                                spannableStringBuilder12.insert(i7, (CharSequence) " ");
                                                                                                spannableStringBuilder12.setSpan(new FixedWidthSpan(AndroidUtilities.dp(i + 6)), i7, i7 + 1, 33);
                                                                                            }
                                                                                        }
                                                                                        i4 = i8;
                                                                                        charSequence = str20;
                                                                                        i22 = 1;
                                                                                        i23 = 0;
                                                                                        str14 = replaceEmoji;
                                                                                    }
                                                                                    c = 1;
                                                                                    CharSequence[] charSequenceArr2 = new CharSequence[2];
                                                                                    charSequenceArr2[0] = str21.replace('\n', ' ');
                                                                                    charSequenceArr2[c] = str20;
                                                                                    spannableStringBuilder4 = AndroidUtilities.formatSpannable(str28, charSequenceArr2);
                                                                                    spannableStringBuilder4.setSpan(new ForegroundColorSpanThemable("chats_attachMessage", this.resourcesProvider), !z ? str20.length() + 2 : 0, spannableStringBuilder4.length(), 33);
                                                                                    if (!this.useForceThreeLines) {
                                                                                    }
                                                                                    i8 = 0;
                                                                                    i7 = 0;
                                                                                    replaceEmoji = Emoji.replaceEmoji(spannableStringBuilder4, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                    if (this.message.hasHighlightedWords()) {
                                                                                        replaceEmoji = highlightText;
                                                                                    }
                                                                                    if (this.hasMessageThumb) {
                                                                                    }
                                                                                    i4 = i8;
                                                                                    charSequence = str20;
                                                                                    i22 = 1;
                                                                                    i23 = 0;
                                                                                    str14 = replaceEmoji;
                                                                                } else {
                                                                                    MessageObject messageObject14 = this.message;
                                                                                    CharSequence charSequence13 = messageObject14.messageOwner.message;
                                                                                    if (charSequence13 != null) {
                                                                                        if (messageObject14.hasHighlightedWords()) {
                                                                                            String str31 = this.message.messageTrimmedToHighlight;
                                                                                            if (str31 != null) {
                                                                                                charSequence13 = str31;
                                                                                            }
                                                                                            int measuredWidth3 = getMeasuredWidth() - AndroidUtilities.dp(105.0f);
                                                                                            if (z) {
                                                                                                if (!TextUtils.isEmpty(str20)) {
                                                                                                    measuredWidth3 = (int) (measuredWidth3 - textPaint5.measureText(str20.toString()));
                                                                                                }
                                                                                                measuredWidth3 = (int) (measuredWidth3 - textPaint5.measureText(": "));
                                                                                            }
                                                                                            if (measuredWidth3 > 0) {
                                                                                                charSequence13 = AndroidUtilities.ellipsizeCenterEnd(charSequence13, this.message.highlightedWords.get(0), measuredWidth3, textPaint5, 130).toString();
                                                                                            }
                                                                                        } else {
                                                                                            if (charSequence13.length() > 150) {
                                                                                                charSequence13 = charSequence13.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                            }
                                                                                            charSequence13 = AndroidUtilities.replaceNewLines(charSequence13);
                                                                                        }
                                                                                        SpannableStringBuilder spannableStringBuilder13 = new SpannableStringBuilder(charSequence13);
                                                                                        MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder13, 256);
                                                                                        MessageObject messageObject15 = this.message;
                                                                                        if (messageObject15 != null && (tLRPC$Message2 = messageObject15.messageOwner) != null) {
                                                                                            MediaDataController.addAnimatedEmojiSpans(tLRPC$Message2.entities, spannableStringBuilder13, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                        }
                                                                                        valueOf = AndroidUtilities.formatSpannable(str28, spannableStringBuilder13, str20);
                                                                                    } else {
                                                                                        valueOf = SpannableStringBuilder.valueOf("");
                                                                                    }
                                                                                }
                                                                            }
                                                                            spannableStringBuilder4 = valueOf;
                                                                            if (!this.useForceThreeLines) {
                                                                            }
                                                                            i8 = 0;
                                                                            i7 = 0;
                                                                            replaceEmoji = Emoji.replaceEmoji(spannableStringBuilder4, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                            if (this.message.hasHighlightedWords()) {
                                                                            }
                                                                            if (this.hasMessageThumb) {
                                                                            }
                                                                            i4 = i8;
                                                                            charSequence = str20;
                                                                            i22 = 1;
                                                                            i23 = 0;
                                                                            str14 = replaceEmoji;
                                                                        } else {
                                                                            isEmpty = TextUtils.isEmpty(restrictionReason);
                                                                            String str32 = restrictionReason;
                                                                            if (isEmpty) {
                                                                                MessageObject messageObject16 = this.message;
                                                                                TLRPC$MessageMedia tLRPC$MessageMedia2 = messageObject16.messageOwner.media;
                                                                                if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia2.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                                                                                    str32 = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
                                                                                } else if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument) && (tLRPC$MessageMedia2.document instanceof TLRPC$TL_documentEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                                                                                    str32 = LocaleController.getString("AttachVideoExpired", R.string.AttachVideoExpired);
                                                                                } else if (messageObject16.caption != null) {
                                                                                    if (!z6) {
                                                                                        str19 = "";
                                                                                    } else if (messageObject16.isVideo()) {
                                                                                        str19 = "📹 ";
                                                                                    } else if (this.message.isVoice()) {
                                                                                        str19 = "🎤 ";
                                                                                    } else if (this.message.isMusic()) {
                                                                                        str19 = "🎧 ";
                                                                                    } else {
                                                                                        str19 = this.message.isPhoto() ? "🖼 " : "📎 ";
                                                                                    }
                                                                                    if (this.message.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                        String str33 = this.message.messageTrimmedToHighlight;
                                                                                        int measuredWidth4 = getMeasuredWidth() - AndroidUtilities.dp(119.0f);
                                                                                        if (z) {
                                                                                            if (!TextUtils.isEmpty(null)) {
                                                                                                throw null;
                                                                                            }
                                                                                            measuredWidth4 = (int) (measuredWidth4 - textPaint5.measureText(": "));
                                                                                        }
                                                                                        if (measuredWidth4 > 0) {
                                                                                            str33 = AndroidUtilities.ellipsizeCenterEnd(str33, this.message.highlightedWords.get(0), measuredWidth4, textPaint5, 130).toString();
                                                                                        }
                                                                                        str32 = str19 + str33;
                                                                                    } else {
                                                                                        SpannableStringBuilder spannableStringBuilder14 = new SpannableStringBuilder(this.message.caption);
                                                                                        MessageObject messageObject17 = this.message;
                                                                                        if (messageObject17 != null && (tLRPC$Message = messageObject17.messageOwner) != null) {
                                                                                            MediaDataController.addTextStyleRuns(tLRPC$Message.entities, messageObject17.caption, spannableStringBuilder14, 256);
                                                                                            MediaDataController.addAnimatedEmojiSpans(this.message.messageOwner.entities, spannableStringBuilder14, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                        }
                                                                                        str32 = new SpannableStringBuilder(str19).append((CharSequence) spannableStringBuilder14);
                                                                                    }
                                                                                } else {
                                                                                    if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPoll) {
                                                                                        str18 = "📊 " + ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia2).poll.question;
                                                                                    } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGame) {
                                                                                        str18 = "🎮 " + this.message.messageOwner.media.game.title;
                                                                                    } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                        str18 = tLRPC$MessageMedia2.title;
                                                                                    } else {
                                                                                        if (messageObject16.type == 14) {
                                                                                            str16 = String.format("🎧 %s - %s", messageObject16.getMusicAuthor(), this.message.getMusicTitle());
                                                                                        } else {
                                                                                            if (messageObject16.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                str17 = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp(95.0f), textPaint5, 130).toString();
                                                                                            } else {
                                                                                                SpannableStringBuilder spannableStringBuilder15 = new SpannableStringBuilder(spannableStringBuilder7);
                                                                                                MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder15, 256);
                                                                                                MessageObject messageObject18 = this.message;
                                                                                                str17 = spannableStringBuilder15;
                                                                                                if (messageObject18 != null) {
                                                                                                    TLRPC$Message tLRPC$Message4 = messageObject18.messageOwner;
                                                                                                    str17 = spannableStringBuilder15;
                                                                                                    if (tLRPC$Message4 != null) {
                                                                                                        MediaDataController.addAnimatedEmojiSpans(tLRPC$Message4.entities, spannableStringBuilder15, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                                        str17 = spannableStringBuilder15;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            AndroidUtilities.highlightText(str17, this.message.highlightedWords, this.resourcesProvider);
                                                                                            str16 = str17;
                                                                                        }
                                                                                        messageObject3 = this.message;
                                                                                        charSequence2 = str16;
                                                                                        if (messageObject3.messageOwner.media != null) {
                                                                                            charSequence2 = str16;
                                                                                            if (!messageObject3.isMediaEmpty()) {
                                                                                                textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                charSequence2 = str16;
                                                                                            }
                                                                                        }
                                                                                        if (!this.hasMessageThumb) {
                                                                                            if (this.message.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                charSequence4 = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp((i + 95) + 6), textPaint5, 130).toString();
                                                                                            } else {
                                                                                                int length2 = charSequence2.length();
                                                                                                CharSequence charSequence14 = charSequence2;
                                                                                                if (length2 > 150) {
                                                                                                    charSequence14 = charSequence2.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                                }
                                                                                                charSequence4 = AndroidUtilities.replaceNewLines(charSequence14);
                                                                                            }
                                                                                            boolean z13 = charSequence4 instanceof SpannableStringBuilder;
                                                                                            SpannableStringBuilder spannableStringBuilder16 = charSequence4;
                                                                                            if (!z13) {
                                                                                                spannableStringBuilder16 = new SpannableStringBuilder(charSequence4);
                                                                                            }
                                                                                            SpannableStringBuilder spannableStringBuilder17 = (SpannableStringBuilder) spannableStringBuilder16;
                                                                                            spannableStringBuilder17.insert(0, (CharSequence) " ");
                                                                                            spannableStringBuilder17.setSpan(new FixedWidthSpan(AndroidUtilities.dp(i + 6)), 0, 1, 33);
                                                                                            Emoji.replaceEmoji(spannableStringBuilder17, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
                                                                                            if (this.message.hasHighlightedWords()) {
                                                                                                CharSequence highlightText5 = AndroidUtilities.highlightText(spannableStringBuilder17, this.message.highlightedWords, this.resourcesProvider);
                                                                                                charSequence5 = highlightText5;
                                                                                            }
                                                                                            charSequence5 = spannableStringBuilder16;
                                                                                            charSequence = null;
                                                                                            i4 = 0;
                                                                                            i22 = 1;
                                                                                            i23 = 0;
                                                                                            str14 = charSequence5;
                                                                                        } else {
                                                                                            charSequence3 = charSequence2;
                                                                                            charSequence = null;
                                                                                            i4 = 0;
                                                                                            i22 = 1;
                                                                                        }
                                                                                    }
                                                                                    str16 = str18;
                                                                                    messageObject3 = this.message;
                                                                                    charSequence2 = str16;
                                                                                    if (messageObject3.messageOwner.media != null) {
                                                                                    }
                                                                                    if (!this.hasMessageThumb) {
                                                                                    }
                                                                                }
                                                                            }
                                                                            charSequence2 = str32;
                                                                            if (!this.hasMessageThumb) {
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            z6 = true;
                                                            tLRPC$Chat2 = this.chat;
                                                            if (tLRPC$Chat2 == null) {
                                                            }
                                                            isEmpty = TextUtils.isEmpty(restrictionReason);
                                                            String str322 = restrictionReason;
                                                            if (isEmpty) {
                                                            }
                                                            charSequence2 = str322;
                                                            if (!this.hasMessageThumb) {
                                                            }
                                                        }
                                                        i23 = 1;
                                                        str14 = charSequence3;
                                                    }
                                                    if (this.currentDialogFolderId != 0) {
                                                        charSequence = formatArchivedDialogNames();
                                                    }
                                                    i3 = -1;
                                                    int i282 = i23;
                                                    i23 = i22;
                                                    i22 = i282;
                                                    spannableStringBuilder = str14;
                                                }
                                                str14 = str15;
                                                charSequence = null;
                                                i4 = 0;
                                                i22 = 0;
                                                i23 = 1;
                                                z10 = false;
                                                if (this.currentDialogFolderId != 0) {
                                                }
                                                i3 = -1;
                                                int i2822 = i23;
                                                i23 = i22;
                                                i22 = i2822;
                                                spannableStringBuilder = str14;
                                            }
                                        }
                                        charSequence = null;
                                        i4 = 0;
                                        str13 = str24;
                                    }
                                    i23 = 1;
                                    str12 = str13;
                                    i3 = -1;
                                    spannableStringBuilder = str12;
                                }
                                if (this.draftMessage == null) {
                                    str3 = LocaleController.stringForMessageListDate(tLRPC$DraftMessage.date);
                                } else {
                                    int i30 = this.lastMessageDate;
                                    if (i30 != 0) {
                                        str3 = LocaleController.stringForMessageListDate(i30);
                                    } else {
                                        str3 = this.message != null ? LocaleController.stringForMessageListDate(messageObject2.messageOwner.date) : "";
                                    }
                                }
                                messageObject = this.message;
                                if (messageObject != null) {
                                    this.drawCheck1 = false;
                                    this.drawCheck2 = false;
                                    this.drawClock = false;
                                    this.drawCount = false;
                                    this.drawMention = false;
                                    this.drawReactionMention = false;
                                    this.drawError = false;
                                    str5 = null;
                                    z2 = false;
                                    str4 = null;
                                } else {
                                    if (this.currentDialogFolderId != 0) {
                                        int i31 = this.unreadCount;
                                        int i32 = this.mentionCount;
                                        if (i31 + i32 <= 0) {
                                            z5 = false;
                                            this.drawCount = false;
                                            this.drawMention = false;
                                            str11 = null;
                                        } else if (i31 > i32) {
                                            this.drawCount = true;
                                            this.drawMention = false;
                                            z5 = false;
                                            str10 = String.format("%d", Integer.valueOf(i31 + i32));
                                            str11 = null;
                                            this.drawReactionMention = z5;
                                            str4 = str11;
                                            str5 = str10;
                                        } else {
                                            this.drawCount = false;
                                            this.drawMention = true;
                                            z5 = false;
                                            str11 = String.format("%d", Integer.valueOf(i31 + i32));
                                        }
                                        str10 = null;
                                        this.drawReactionMention = z5;
                                        str4 = str11;
                                        str5 = str10;
                                    } else {
                                        if (this.clearingDialog) {
                                            this.drawCount = false;
                                            str5 = null;
                                            z10 = false;
                                            z4 = true;
                                            z3 = false;
                                        } else {
                                            int i33 = this.unreadCount;
                                            if (i33 != 0 && (i33 != 1 || i33 != this.mentionCount || messageObject == null || !messageObject.messageOwner.mentioned)) {
                                                z4 = true;
                                                this.drawCount = true;
                                                z3 = false;
                                                str5 = String.format("%d", Integer.valueOf(i33));
                                            } else {
                                                z4 = true;
                                                z3 = false;
                                                if (this.markUnread) {
                                                    this.drawCount = true;
                                                    str5 = "";
                                                } else {
                                                    this.drawCount = false;
                                                    str5 = null;
                                                }
                                            }
                                        }
                                        if (this.mentionCount != 0) {
                                            this.drawMention = z4;
                                            str4 = "@";
                                        } else {
                                            this.drawMention = z3;
                                            str4 = null;
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
                                                z2 = false;
                                                this.drawCheck1 = false;
                                                this.drawCheck2 = false;
                                                this.drawClock = true;
                                                this.drawError = false;
                                            } else {
                                                z2 = false;
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
                                                    z2 = false;
                                                    this.drawClock = false;
                                                    this.drawError = false;
                                                } else {
                                                    z2 = false;
                                                }
                                            }
                                        }
                                    }
                                    z2 = false;
                                    this.drawCheck1 = false;
                                    this.drawCheck2 = false;
                                    this.drawClock = false;
                                    this.drawError = false;
                                }
                                this.promoDialog = z2;
                                MessagesController messagesController2 = MessagesController.getInstance(this.currentAccount);
                                str6 = str5;
                                if (this.dialogsType != 0) {
                                    spannableStringBuilder3 = spannableStringBuilder;
                                    i5 = i4;
                                    if (messagesController2.isPromoDialog(this.currentDialogId, true)) {
                                        this.drawPinBackground = true;
                                        this.promoDialog = true;
                                        int i34 = messagesController2.promoDialogType;
                                        if (i34 == MessagesController.PROMO_TYPE_PROXY) {
                                            string = LocaleController.getString("UseProxySponsor", R.string.UseProxySponsor);
                                        } else if (i34 == MessagesController.PROMO_TYPE_PSA) {
                                            string = LocaleController.getString("PsaType_" + messagesController2.promoPsaType);
                                            if (TextUtils.isEmpty(string)) {
                                                string = LocaleController.getString("PsaTypeDefault", R.string.PsaTypeDefault);
                                            }
                                            if (!TextUtils.isEmpty(messagesController2.promoPsaMessage)) {
                                                String str34 = messagesController2.promoPsaMessage;
                                                this.hasMessageThumb = false;
                                                str7 = string;
                                                spannableStringBuilder2 = str34;
                                                if (this.currentDialogFolderId == 0) {
                                                    str8 = LocaleController.getString("ArchivedChats", R.string.ArchivedChats);
                                                } else {
                                                    TLRPC$Chat tLRPC$Chat9 = this.chat;
                                                    if (tLRPC$Chat9 != null) {
                                                        userName = tLRPC$Chat9.title;
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
                                                            str8 = "";
                                                            if (str8.length() == 0) {
                                                                str8 = LocaleController.getString("HiddenName", R.string.HiddenName);
                                                            }
                                                        }
                                                    }
                                                    str8 = userName;
                                                    if (str8.length() == 0) {
                                                    }
                                                }
                                                str9 = spannableStringBuilder2;
                                                i6 = i22;
                                                i22 = i23;
                                                i23 = i5;
                                            }
                                        }
                                        str7 = string;
                                        spannableStringBuilder2 = spannableStringBuilder3;
                                        if (this.currentDialogFolderId == 0) {
                                        }
                                        str9 = spannableStringBuilder2;
                                        i6 = i22;
                                        i22 = i23;
                                        i23 = i5;
                                    }
                                } else {
                                    spannableStringBuilder3 = spannableStringBuilder;
                                    i5 = i4;
                                }
                                spannableStringBuilder2 = spannableStringBuilder3;
                                str7 = str3;
                                if (this.currentDialogFolderId == 0) {
                                }
                                str9 = spannableStringBuilder2;
                                i6 = i22;
                                i22 = i23;
                                i23 = i5;
                            }
                        } else {
                            str2 = str;
                        }
                        z7 = false;
                        this.drawPremium = z7;
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
                        messageObject = this.message;
                        if (messageObject != null) {
                        }
                        this.promoDialog = z2;
                        MessagesController messagesController22 = MessagesController.getInstance(this.currentAccount);
                        str6 = str5;
                        if (this.dialogsType != 0) {
                        }
                        spannableStringBuilder2 = spannableStringBuilder3;
                        str7 = str3;
                        if (this.currentDialogFolderId == 0) {
                        }
                        str9 = spannableStringBuilder2;
                        i6 = i22;
                        i22 = i23;
                        i23 = i5;
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
            messageObject = this.message;
            if (messageObject != null) {
            }
            this.promoDialog = z2;
            MessagesController messagesController222 = MessagesController.getInstance(this.currentAccount);
            str6 = str5;
            if (this.dialogsType != 0) {
            }
            spannableStringBuilder2 = spannableStringBuilder3;
            str7 = str3;
            if (this.currentDialogFolderId == 0) {
            }
            str9 = spannableStringBuilder2;
            i6 = i22;
            i22 = i23;
            i23 = i5;
        }
        charSequence6 = charSequence;
        if (i22 == 0) {
            i9 = (int) Math.ceil(Theme.dialogs_timePaint.measureText(str7));
            this.timeLayout = new StaticLayout(str7, Theme.dialogs_timePaint, i9, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
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
            i10 = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(14.0f)) - i9;
        } else {
            i10 = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(77.0f)) - i9;
            this.nameLeft += i9;
        }
        if (this.drawNameLock) {
            i10 -= AndroidUtilities.dp(4.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
        }
        if (!this.drawClock) {
            int intrinsicWidth2 = Theme.dialogs_clockDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            int i35 = i10 - intrinsicWidth2;
            if (!LocaleController.isRTL) {
                this.clockDrawLeft = this.timeLeft - intrinsicWidth2;
                i21 = i35;
            } else {
                i21 = i35;
                this.clockDrawLeft = this.timeLeft + i9 + AndroidUtilities.dp(5.0f);
                this.nameLeft += intrinsicWidth2;
            }
            i10 = i21;
        } else if (this.drawCheck2) {
            int intrinsicWidth3 = Theme.dialogs_checkDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            i10 -= intrinsicWidth3;
            if (this.drawCheck1) {
                i10 -= Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0f);
                if (!LocaleController.isRTL) {
                    int i36 = this.timeLeft - intrinsicWidth3;
                    this.halfCheckDrawLeft = i36;
                    this.checkDrawLeft = i36 - AndroidUtilities.dp(5.5f);
                } else {
                    int dp3 = this.timeLeft + i9 + AndroidUtilities.dp(5.0f);
                    this.checkDrawLeft = dp3;
                    this.halfCheckDrawLeft = dp3 + AndroidUtilities.dp(5.5f);
                    this.nameLeft += (intrinsicWidth3 + Theme.dialogs_halfCheckDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(8.0f);
                }
            } else if (!LocaleController.isRTL) {
                this.checkDrawLeft1 = this.timeLeft - intrinsicWidth3;
            } else {
                this.checkDrawLeft1 = this.timeLeft + i9 + AndroidUtilities.dp(5.0f);
                this.nameLeft += intrinsicWidth3;
            }
        }
        if (!this.dialogMuted && !this.drawVerified && this.drawScam == 0) {
            int dp4 = AndroidUtilities.dp(6.0f) + Theme.dialogs_muteDrawable.getIntrinsicWidth();
            i10 -= dp4;
            if (LocaleController.isRTL) {
                this.nameLeft += dp4;
            }
        } else if (!this.drawVerified) {
            int dp5 = AndroidUtilities.dp(6.0f) + Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
            i10 -= dp5;
            if (LocaleController.isRTL) {
                this.nameLeft += dp5;
            }
        } else if (this.drawPremium) {
            int dp6 = AndroidUtilities.dp(6.0f) + PremiumGradient.getInstance().premiumStarDrawableMini.getIntrinsicWidth();
            i10 -= dp6;
            if (LocaleController.isRTL) {
                this.nameLeft += dp6;
            }
        } else if (this.drawScam != 0) {
            int dp7 = AndroidUtilities.dp(6.0f) + (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).getIntrinsicWidth();
            i10 -= dp7;
            if (LocaleController.isRTL) {
                this.nameLeft += dp7;
            }
        }
        try {
            dp = i10 - AndroidUtilities.dp(12.0f);
            if (dp < 0) {
                dp = 0;
            }
            CharSequence replaceEmoji22 = Emoji.replaceEmoji(TextUtils.ellipsize(str8.replace('\n', ' '), Theme.dialogs_namePaint[this.paintIndex], dp, TextUtils.TruncateAt.END), Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            MessageObject messageObject132 = this.message;
            this.nameLayout = new StaticLayout((messageObject132 != null || !messageObject132.hasHighlightedWords() || (highlightText4 = AndroidUtilities.highlightText(replaceEmoji22, this.message.highlightedWords, this.resourcesProvider)) == null) ? replaceEmoji22 : highlightText4, Theme.dialogs_namePaint[this.paintIndex], i10, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        } catch (Exception e5) {
            FileLog.e(e5);
        }
        if (!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
            i11 = i23;
            charSequence7 = "";
            i12 = i3;
            int dp22 = AndroidUtilities.dp(11.0f);
            this.messageNameTop = AndroidUtilities.dp(32.0f);
            this.timeTop = AndroidUtilities.dp(13.0f);
            this.errorTop = AndroidUtilities.dp(43.0f);
            this.pinTop = AndroidUtilities.dp(43.0f);
            this.countTop = AndroidUtilities.dp(43.0f);
            this.checkDrawTop = AndroidUtilities.dp(13.0f);
            int measuredWidth22 = getMeasuredWidth() - AndroidUtilities.dp(93.0f);
            if (!LocaleController.isRTL) {
                int dp8 = AndroidUtilities.dp(16.0f);
                this.messageNameLeft = dp8;
                this.messageLeft = dp8;
                i18 = getMeasuredWidth() - AndroidUtilities.dp(66.0f);
                i17 = i18 - AndroidUtilities.dp(31.0f);
            } else {
                int dp9 = AndroidUtilities.dp(78.0f);
                this.messageNameLeft = dp9;
                this.messageLeft = dp9;
                i18 = AndroidUtilities.dp(10.0f);
                i17 = AndroidUtilities.dp(69.0f) + i18;
            }
            this.avatarImage.setImageCoords(i18, dp22, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            this.thumbImage.setImageCoords(i17, AndroidUtilities.dp(31.0f) + dp22, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
            i14 = dp22;
            i13 = measuredWidth22;
        } else {
            int dp10 = AndroidUtilities.dp(9.0f);
            this.messageNameTop = AndroidUtilities.dp(31.0f);
            this.timeTop = AndroidUtilities.dp(16.0f);
            this.errorTop = AndroidUtilities.dp(39.0f);
            this.pinTop = AndroidUtilities.dp(39.0f);
            this.countTop = AndroidUtilities.dp(39.0f);
            this.checkDrawTop = AndroidUtilities.dp(17.0f);
            int measuredWidth5 = getMeasuredWidth() - AndroidUtilities.dp(95.0f);
            if (LocaleController.isRTL) {
                int dp11 = AndroidUtilities.dp(22.0f);
                this.messageNameLeft = dp11;
                this.messageLeft = dp11;
                i20 = getMeasuredWidth() - AndroidUtilities.dp(64.0f);
                i19 = i20 - AndroidUtilities.dp(i + 11);
            } else {
                int dp12 = AndroidUtilities.dp(76.0f);
                this.messageNameLeft = dp12;
                this.messageLeft = dp12;
                i20 = AndroidUtilities.dp(10.0f);
                i19 = AndroidUtilities.dp(67.0f) + i20;
            }
            i13 = measuredWidth5;
            charSequence7 = "";
            i12 = i3;
            i11 = i23;
            this.avatarImage.setImageCoords(i20, dp10, AndroidUtilities.dp(54.0f), AndroidUtilities.dp(54.0f));
            float f3 = i;
            this.thumbImage.setImageCoords(i19, AndroidUtilities.dp(30.0f) + dp10, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3));
            i14 = dp10;
        }
        if (this.drawPin) {
            if (!LocaleController.isRTL) {
                this.pinLeft = (getMeasuredWidth() - Theme.dialogs_pinnedDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(14.0f);
            } else {
                this.pinLeft = AndroidUtilities.dp(14.0f);
            }
        }
        if (!this.drawError) {
            int dp13 = AndroidUtilities.dp(31.0f);
            i13 -= dp13;
            if (!LocaleController.isRTL) {
                this.errorLeft = getMeasuredWidth() - AndroidUtilities.dp(34.0f);
            } else {
                this.errorLeft = AndroidUtilities.dp(11.0f);
                this.messageLeft += dp13;
                this.messageNameLeft += dp13;
            }
        } else if (str6 != null || str4 != null || this.drawReactionMention) {
            if (str6 != null) {
                this.countWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str6)));
                this.countLayout = new StaticLayout(str6, Theme.dialogs_countTextPaint, this.countWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                int dp14 = this.countWidth + AndroidUtilities.dp(18.0f);
                i13 -= dp14;
                if (!LocaleController.isRTL) {
                    this.countLeft = (getMeasuredWidth() - this.countWidth) - AndroidUtilities.dp(20.0f);
                } else {
                    this.countLeft = AndroidUtilities.dp(20.0f);
                    this.messageLeft += dp14;
                    this.messageNameLeft += dp14;
                }
                this.drawCount = true;
            } else {
                this.countWidth = 0;
            }
            if (str4 != null) {
                if (this.currentDialogFolderId != 0) {
                    this.mentionWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str4)));
                    this.mentionLayout = new StaticLayout(str4, Theme.dialogs_countTextPaint, this.mentionWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                } else {
                    this.mentionWidth = AndroidUtilities.dp(12.0f);
                }
                int dp15 = this.mentionWidth + AndroidUtilities.dp(18.0f);
                i13 -= dp15;
                if (!LocaleController.isRTL) {
                    int measuredWidth6 = (getMeasuredWidth() - this.mentionWidth) - AndroidUtilities.dp(20.0f);
                    int i37 = this.countWidth;
                    this.mentionLeft = measuredWidth6 - (i37 != 0 ? i37 + AndroidUtilities.dp(18.0f) : 0);
                } else {
                    int dp16 = AndroidUtilities.dp(20.0f);
                    int i38 = this.countWidth;
                    this.mentionLeft = dp16 + (i38 != 0 ? i38 + AndroidUtilities.dp(18.0f) : 0);
                    this.messageLeft += dp15;
                    this.messageNameLeft += dp15;
                }
                this.drawMention = true;
            } else {
                this.mentionWidth = 0;
            }
            if (this.drawReactionMention) {
                int dp17 = AndroidUtilities.dp(24.0f);
                i13 -= dp17;
                if (!LocaleController.isRTL) {
                    int measuredWidth7 = getMeasuredWidth() - AndroidUtilities.dp(32.0f);
                    this.reactionMentionLeft = measuredWidth7;
                    if (this.drawMention) {
                        int i39 = this.mentionWidth;
                        this.reactionMentionLeft = measuredWidth7 - (i39 != 0 ? i39 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i40 = this.reactionMentionLeft;
                        int i41 = this.countWidth;
                        this.reactionMentionLeft = i40 - (i41 != 0 ? i41 + AndroidUtilities.dp(18.0f) : 0);
                    }
                } else {
                    int dp18 = AndroidUtilities.dp(20.0f);
                    this.reactionMentionLeft = dp18;
                    if (this.drawMention) {
                        int i42 = this.mentionWidth;
                        this.reactionMentionLeft = dp18 + (i42 != 0 ? i42 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i43 = this.reactionMentionLeft;
                        int i44 = this.countWidth;
                        this.reactionMentionLeft = i43 + (i44 != 0 ? i44 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    this.messageLeft += dp17;
                    this.messageNameLeft += dp17;
                }
            }
        } else {
            if (this.drawPin) {
                int intrinsicWidth4 = Theme.dialogs_pinnedDrawable.getIntrinsicWidth() + AndroidUtilities.dp(8.0f);
                i13 -= intrinsicWidth4;
                if (LocaleController.isRTL) {
                    this.messageLeft += intrinsicWidth4;
                    this.messageNameLeft += intrinsicWidth4;
                }
            }
            this.drawCount = false;
            this.drawMention = false;
        }
        int i292 = i13;
        if (i6 != 0) {
            CharSequence charSequence15 = str9 == null ? charSequence7 : str9;
            if (charSequence15.length() > 150) {
                charSequence15 = charSequence15.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
            }
            if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || charSequence6 != null) {
                charSequence8 = AndroidUtilities.replaceNewLines(charSequence15);
            } else {
                charSequence8 = AndroidUtilities.replaceTwoNewLinesToOne(charSequence15);
            }
            str9 = Emoji.replaceEmoji(charSequence8, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
            MessageObject messageObject20 = this.message;
            if (messageObject20 != null && (highlightText3 = AndroidUtilities.highlightText(str9, messageObject20.highlightedWords, this.resourcesProvider)) != null) {
                str9 = highlightText3;
            }
        }
        max = Math.max(AndroidUtilities.dp(12.0f), i292);
        z8 = this.useForceThreeLines;
        if ((!z8 || SharedConfig.useThreeLinesLayout) && charSequence6 != null && (this.currentDialogFolderId == 0 || this.currentDialogFolderDialogsCount == 1)) {
            messageObject5 = this.message;
            if (messageObject5 != null && messageObject5.hasHighlightedWords() && (highlightText2 = AndroidUtilities.highlightText(charSequence6, this.message.highlightedWords, this.resourcesProvider)) != null) {
                charSequence6 = highlightText2;
            }
            this.messageNameLayout = StaticLayoutEx.createStaticLayout(charSequence6, Theme.dialogs_messageNamePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max, 1);
            this.messageTop = AndroidUtilities.dp(51.0f);
            this.thumbImage.setImageY(i14 + AndroidUtilities.dp(40.0f));
        } else {
            this.messageNameLayout = null;
            if (z8 || SharedConfig.useThreeLinesLayout) {
                this.messageTop = AndroidUtilities.dp(32.0f);
                this.thumbImage.setImageY(i14 + AndroidUtilities.dp(21.0f));
            } else {
                this.messageTop = AndroidUtilities.dp(39.0f);
            }
        }
        try {
            z9 = this.useForceThreeLines;
            if ((!z9 || SharedConfig.useThreeLinesLayout) && this.currentDialogFolderId != 0 && this.currentDialogFolderDialogsCount > 1) {
                textPaint5 = Theme.dialogs_messagePaint[this.paintIndex];
                str9 = charSequence6;
                charSequence6 = null;
            } else if ((!z9 && !SharedConfig.useThreeLinesLayout) || charSequence6 != null) {
                str9 = TextUtils.ellipsize(str9, textPaint5, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END);
            }
            if (str9 instanceof Spannable) {
                Spannable spannable = (Spannable) str9;
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
            this.messageLayout = new StaticLayout(str9, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.spoilersPool.addAll(this.spoilers);
            this.spoilers.clear();
            SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
            if (!LocaleController.isRTL) {
                StaticLayout staticLayout3 = this.nameLayout;
                if (staticLayout3 != null && staticLayout3.getLineCount() > 0) {
                    float lineLeft = this.nameLayout.getLineLeft(0);
                    double ceil2 = Math.ceil(this.nameLayout.getLineWidth(0));
                    if (this.dialogMuted && !this.drawVerified && this.drawScam == 0) {
                        double d = this.nameLeft;
                        double d2 = i10;
                        Double.isNaN(d2);
                        Double.isNaN(d);
                        double d3 = d + (d2 - ceil2);
                        double dp19 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(dp19);
                        double d4 = d3 - dp19;
                        double intrinsicWidth5 = Theme.dialogs_muteDrawable.getIntrinsicWidth();
                        Double.isNaN(intrinsicWidth5);
                        this.nameMuteLeft = (int) (d4 - intrinsicWidth5);
                    } else if (this.drawVerified) {
                        double d5 = this.nameLeft;
                        double d6 = i10;
                        Double.isNaN(d6);
                        Double.isNaN(d5);
                        double d7 = d5 + (d6 - ceil2);
                        double dp20 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(dp20);
                        double d8 = d7 - dp20;
                        double intrinsicWidth6 = Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
                        Double.isNaN(intrinsicWidth6);
                        this.nameMuteLeft = (int) (d8 - intrinsicWidth6);
                    } else if (this.drawPremium) {
                        double d9 = this.nameLeft;
                        double d10 = i10;
                        Double.isNaN(d10);
                        Double.isNaN(d9);
                        double d11 = d9 + (d10 - ceil2);
                        double dp21 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(dp21);
                        double d12 = d11 - dp21;
                        double intrinsicWidth7 = PremiumGradient.getInstance().premiumStarDrawableMini.getIntrinsicWidth();
                        Double.isNaN(intrinsicWidth7);
                        this.nameMuteLeft = (int) (d12 - intrinsicWidth7);
                    } else if (this.drawScam != 0) {
                        double d13 = this.nameLeft;
                        double d14 = i10;
                        Double.isNaN(d14);
                        Double.isNaN(d13);
                        double d15 = d13 + (d14 - ceil2);
                        double dp23 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(dp23);
                        double d16 = d15 - dp23;
                        double intrinsicWidth8 = (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).getIntrinsicWidth();
                        Double.isNaN(intrinsicWidth8);
                        this.nameMuteLeft = (int) (d16 - intrinsicWidth8);
                    }
                    if (lineLeft == 0.0f) {
                        double d17 = i10;
                        if (ceil2 < d17) {
                            double d18 = this.nameLeft;
                            Double.isNaN(d17);
                            Double.isNaN(d18);
                            this.nameLeft = (int) (d18 + (d17 - ceil2));
                        }
                    }
                }
                StaticLayout staticLayout4 = this.messageLayout;
                if (staticLayout4 != null && (lineCount2 = staticLayout4.getLineCount()) > 0) {
                    int i45 = 0;
                    int i46 = Integer.MAX_VALUE;
                    while (true) {
                        if (i45 >= lineCount2) {
                            break;
                        }
                        if (this.messageLayout.getLineLeft(i45) != 0.0f) {
                            i46 = 0;
                            break;
                        }
                        double ceil3 = Math.ceil(this.messageLayout.getLineWidth(i45));
                        double d19 = max;
                        Double.isNaN(d19);
                        i46 = Math.min(i46, (int) (d19 - ceil3));
                        i45++;
                    }
                    if (i46 != Integer.MAX_VALUE) {
                        this.messageLeft += i46;
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
                    if (lineRight == i10) {
                        double ceil5 = Math.ceil(this.nameLayout.getLineWidth(0));
                        double d22 = i10;
                        if (ceil5 < d22) {
                            double d23 = this.nameLeft;
                            Double.isNaN(d22);
                            Double.isNaN(d23);
                            this.nameLeft = (int) (d23 - (d22 - ceil5));
                        }
                    }
                    if (this.dialogMuted || this.drawVerified || this.drawPremium || this.drawScam != 0) {
                        this.nameMuteLeft = (int) (this.nameLeft + lineRight + AndroidUtilities.dp(6.0f));
                    }
                }
                StaticLayout staticLayout7 = this.messageLayout;
                if (staticLayout7 != null && (lineCount = staticLayout7.getLineCount()) > 0) {
                    float f4 = 2.14748365E9f;
                    for (int i47 = 0; i47 < lineCount; i47++) {
                        f4 = Math.min(f4, this.messageLayout.getLineLeft(i47));
                    }
                    this.messageLeft = (int) (this.messageLeft - f4);
                }
                StaticLayout staticLayout8 = this.messageNameLayout;
                if (staticLayout8 != null && staticLayout8.getLineCount() > 0) {
                    this.messageNameLeft = (int) (this.messageNameLeft - this.messageNameLayout.getLineLeft(0));
                }
            }
            staticLayout = this.messageLayout;
            if (staticLayout != null && this.hasMessageThumb) {
                try {
                    length = staticLayout.getText().length();
                    i16 = i11;
                    if (i16 >= length) {
                        i16 = length - 1;
                    }
                    ceil = (int) Math.ceil(Math.min(this.messageLayout.getPrimaryHorizontal(i16), this.messageLayout.getPrimaryHorizontal(i16 + 1)));
                    if (ceil != 0) {
                        ceil += AndroidUtilities.dp(3.0f);
                    }
                    this.thumbImage.setImageX(this.messageLeft + ceil);
                } catch (Exception e7) {
                    FileLog.e(e7);
                }
            }
            staticLayout2 = this.messageLayout;
            if (staticLayout2 != null || this.printingStringType < 0) {
            }
            if (i12 >= 0 && (i15 = i12 + 1) < staticLayout2.getText().length()) {
                f2 = this.messageLayout.getPrimaryHorizontal(i12);
                f = this.messageLayout.getPrimaryHorizontal(i15);
            } else {
                f2 = this.messageLayout.getPrimaryHorizontal(0);
                f = this.messageLayout.getPrimaryHorizontal(1);
            }
            if (f2 < f) {
                this.statusDrawableLeft = (int) (this.messageLeft + f2);
                return;
            } else {
                this.statusDrawableLeft = (int) (this.messageLeft + f + AndroidUtilities.dp(3.0f));
                return;
            }
        }
        if (this.hasMessageThumb && charSequence6 != null) {
            max += AndroidUtilities.dp(6.0f);
        }
        this.messageLayout = StaticLayoutEx.createStaticLayout(str9, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence6 == null ? 1 : 2);
        this.spoilersPool.addAll(this.spoilers);
        this.spoilers.clear();
        SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
        if (!LocaleController.isRTL) {
        }
        staticLayout = this.messageLayout;
        if (staticLayout != null) {
            length = staticLayout.getText().length();
            i16 = i11;
            if (i16 >= length) {
            }
            ceil = (int) Math.ceil(Math.min(this.messageLayout.getPrimaryHorizontal(i16), this.messageLayout.getPrimaryHorizontal(i16 + 1)));
            if (ceil != 0) {
            }
            this.thumbImage.setImageX(this.messageLeft + ceil);
        }
        staticLayout2 = this.messageLayout;
        if (staticLayout2 != null) {
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

    /* JADX WARN: Removed duplicated region for block: B:102:0x01ac  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x025b  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0260  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x0577  */
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
                                    i6 = MessagesStorage.getInstance(this.currentAccount).getArchiveUnreadCount();
                                } else if (tLRPC$Dialog2 != null) {
                                    i6 = tLRPC$Dialog2.unread_count;
                                    i5 = tLRPC$Dialog2.unread_mentions_count;
                                    i4 = tLRPC$Dialog2.unread_reactions_count;
                                    if (tLRPC$Dialog2 != null && (this.unreadCount != i6 || this.markUnread != tLRPC$Dialog2.unread_mark || this.mentionCount != i5 || this.reactionMentionCount != i4)) {
                                        this.unreadCount = i6;
                                        this.mentionCount = i5;
                                        this.markUnread = tLRPC$Dialog2.unread_mark;
                                        this.reactionMentionCount = i4;
                                        z2 = true;
                                    }
                                } else {
                                    i6 = 0;
                                }
                                i5 = 0;
                                i4 = 0;
                                if (tLRPC$Dialog2 != null) {
                                    this.unreadCount = i6;
                                    this.mentionCount = i5;
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

    public /* synthetic */ void lambda$update$0(ValueAnimator valueAnimator) {
        this.countChangeProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

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

    /* JADX WARN: Code restructure failed: missing block: B:200:0x0639, code lost:
        if (r0.type != 2) goto L202;
     */
    /* JADX WARN: Removed duplicated region for block: B:273:0x0858  */
    /* JADX WARN: Removed duplicated region for block: B:333:0x0923  */
    /* JADX WARN: Removed duplicated region for block: B:368:0x0991  */
    /* JADX WARN: Removed duplicated region for block: B:369:0x0994  */
    /* JADX WARN: Removed duplicated region for block: B:372:0x09a3  */
    /* JADX WARN: Removed duplicated region for block: B:373:0x09dc  */
    /* JADX WARN: Removed duplicated region for block: B:376:0x09e7  */
    /* JADX WARN: Removed duplicated region for block: B:389:0x0a2e  */
    /* JADX WARN: Removed duplicated region for block: B:422:0x0ab6  */
    /* JADX WARN: Removed duplicated region for block: B:423:0x0b02  */
    /* JADX WARN: Removed duplicated region for block: B:536:0x0ef0  */
    /* JADX WARN: Removed duplicated region for block: B:546:0x0f23  */
    /* JADX WARN: Removed duplicated region for block: B:551:0x0f5b  */
    /* JADX WARN: Removed duplicated region for block: B:601:0x1041  */
    /* JADX WARN: Removed duplicated region for block: B:662:0x121b  */
    /* JADX WARN: Removed duplicated region for block: B:667:0x12a8  */
    /* JADX WARN: Removed duplicated region for block: B:672:0x12bd  */
    /* JADX WARN: Removed duplicated region for block: B:677:0x12d1  */
    /* JADX WARN: Removed duplicated region for block: B:683:0x12e4  */
    /* JADX WARN: Removed duplicated region for block: B:692:0x1303  */
    /* JADX WARN: Removed duplicated region for block: B:695:0x130a  */
    /* JADX WARN: Removed duplicated region for block: B:702:0x132f  */
    /* JADX WARN: Removed duplicated region for block: B:723:0x1393  */
    /* JADX WARN: Removed duplicated region for block: B:729:0x13e0  */
    /* JADX WARN: Removed duplicated region for block: B:733:0x13ec  */
    /* JADX WARN: Removed duplicated region for block: B:739:0x13ff  */
    /* JADX WARN: Removed duplicated region for block: B:747:0x1417  */
    /* JADX WARN: Removed duplicated region for block: B:755:0x1441  */
    /* JADX WARN: Removed duplicated region for block: B:766:0x1470  */
    /* JADX WARN: Removed duplicated region for block: B:772:0x1485  */
    /* JADX WARN: Removed duplicated region for block: B:783:0x14af  */
    /* JADX WARN: Removed duplicated region for block: B:794:0x14d2  */
    /* JADX WARN: Removed duplicated region for block: B:804:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    @SuppressLint({"DrawAllocation"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        float f;
        boolean z;
        long j;
        String str;
        int i;
        float f2;
        int i2;
        String str2;
        Canvas canvas2;
        float f3;
        long j2;
        float f4;
        boolean z2;
        float f5;
        boolean z3;
        TLRPC$User tLRPC$User;
        TLRPC$Chat tLRPC$Chat;
        float f6;
        int i3;
        float f7;
        float f8;
        float f9;
        float dp;
        float dp2;
        float f10;
        float dp3;
        float dp4;
        float f11;
        float f12;
        PullForegroundDrawable pullForegroundDrawable;
        int dp5;
        int dp6;
        int dp7;
        float f13;
        float f14;
        int i4;
        boolean z4;
        float f15;
        int i5;
        Exception e;
        int i6;
        StatusDrawable chatStatusDrawable;
        String str3;
        int i7;
        int i8;
        int i9;
        int i10;
        String str4;
        String str5;
        RLottieDrawable rLottieDrawable;
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
        float f16 = 8.0f;
        if (this.translationX != 0.0f || this.cornerProgress != 0.0f) {
            canvas.save();
            if (this.currentDialogFolderId != 0) {
                if (this.archiveHidden) {
                    i9 = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
                    i8 = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                    str3 = LocaleController.getString("UnhideFromTop", R.string.UnhideFromTop);
                    this.translationDrawable = Theme.dialogs_unpinArchiveDrawable;
                    i7 = R.string.UnhideFromTop;
                } else {
                    i9 = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                    i8 = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
                    str3 = LocaleController.getString("HideOnTop", R.string.HideOnTop);
                    this.translationDrawable = Theme.dialogs_pinArchiveDrawable;
                    i7 = R.string.HideOnTop;
                }
            } else if (this.promoDialog) {
                i9 = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                i8 = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
                str3 = LocaleController.getString("PsaHide", R.string.PsaHide);
                this.translationDrawable = Theme.dialogs_hidePsaDrawable;
                i7 = R.string.PsaHide;
            } else if (this.folderId == 0) {
                i9 = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                i8 = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
                if (SharedConfig.getChatSwipeAction(this.currentAccount) == 3) {
                    if (this.dialogMuted) {
                        str3 = LocaleController.getString("SwipeUnmute", R.string.SwipeUnmute);
                        this.translationDrawable = Theme.dialogs_swipeUnmuteDrawable;
                        i7 = R.string.SwipeUnmute;
                    } else {
                        str3 = LocaleController.getString("SwipeMute", R.string.SwipeMute);
                        this.translationDrawable = Theme.dialogs_swipeMuteDrawable;
                        i7 = R.string.SwipeMute;
                    }
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 4) {
                    str3 = LocaleController.getString("SwipeDeleteChat", R.string.SwipeDeleteChat);
                    i9 = Theme.getColor("dialogSwipeRemove", this.resourcesProvider);
                    this.translationDrawable = Theme.dialogs_swipeDeleteDrawable;
                    i7 = R.string.SwipeDeleteChat;
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 1) {
                    if (this.unreadCount > 0 || this.markUnread) {
                        str3 = LocaleController.getString("SwipeMarkAsRead", R.string.SwipeMarkAsRead);
                        this.translationDrawable = Theme.dialogs_swipeReadDrawable;
                        i7 = R.string.SwipeMarkAsRead;
                    } else {
                        str3 = LocaleController.getString("SwipeMarkAsUnread", R.string.SwipeMarkAsUnread);
                        this.translationDrawable = Theme.dialogs_swipeUnreadDrawable;
                        i7 = R.string.SwipeMarkAsUnread;
                    }
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 0) {
                    if (this.drawPin) {
                        str3 = LocaleController.getString("SwipeUnpin", R.string.SwipeUnpin);
                        this.translationDrawable = Theme.dialogs_swipeUnpinDrawable;
                        i7 = R.string.SwipeUnpin;
                    } else {
                        str3 = LocaleController.getString("SwipePin", R.string.SwipePin);
                        this.translationDrawable = Theme.dialogs_swipePinDrawable;
                        i7 = R.string.SwipePin;
                    }
                } else {
                    str3 = LocaleController.getString("Archive", R.string.Archive);
                    this.translationDrawable = Theme.dialogs_archiveDrawable;
                    i7 = R.string.Archive;
                }
            } else {
                i9 = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
                i8 = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                str3 = LocaleController.getString("Unarchive", R.string.Unarchive);
                this.translationDrawable = Theme.dialogs_unarchiveDrawable;
                i7 = R.string.Unarchive;
            }
            String str6 = str3;
            int i11 = i8;
            if (this.swipeCanceled && (rLottieDrawable = this.lastDrawTranslationDrawable) != null) {
                this.translationDrawable = rLottieDrawable;
                i7 = this.lastDrawSwipeMessageStringId;
            } else {
                this.lastDrawTranslationDrawable = this.translationDrawable;
                this.lastDrawSwipeMessageStringId = i7;
            }
            int i12 = i7;
            if (!this.translationAnimationStarted && Math.abs(this.translationX) > AndroidUtilities.dp(43.0f)) {
                this.translationAnimationStarted = true;
                this.translationDrawable.setProgress(0.0f);
                this.translationDrawable.setCallback(this);
                this.translationDrawable.start();
            }
            float measuredWidth = this.translationX + getMeasuredWidth();
            if (this.currentRevealProgress < 1.0f) {
                Theme.dialogs_pinnedPaint.setColor(i9);
                str4 = str6;
                i10 = i11;
                str5 = "chats_archivePinBackground";
                f = 1.0f;
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
                str4 = str6;
                i10 = i11;
                str5 = "chats_archivePinBackground";
                f = 1.0f;
            }
            int measuredWidth2 = (getMeasuredWidth() - AndroidUtilities.dp(43.0f)) - (this.translationDrawable.getIntrinsicWidth() / 2);
            int dp8 = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.0f : 9.0f);
            int intrinsicWidth = (this.translationDrawable.getIntrinsicWidth() / 2) + measuredWidth2;
            int intrinsicHeight = (this.translationDrawable.getIntrinsicHeight() / 2) + dp8;
            if (this.currentRevealProgress > 0.0f) {
                canvas.save();
                canvas.clipRect(measuredWidth - AndroidUtilities.dp(8.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                Theme.dialogs_pinnedPaint.setColor(i10);
                canvas.drawCircle(intrinsicWidth, intrinsicHeight, ((float) Math.sqrt((intrinsicWidth * intrinsicWidth) + ((intrinsicHeight - getMeasuredHeight()) * (intrinsicHeight - getMeasuredHeight())))) * AndroidUtilities.accelerateInterpolator.getInterpolation(this.currentRevealProgress), Theme.dialogs_pinnedPaint);
                canvas.restore();
                if (!Theme.dialogs_archiveDrawableRecolored) {
                    Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor(str5));
                    Theme.dialogs_archiveDrawableRecolored = true;
                }
                if (!Theme.dialogs_hidePsaDrawableRecolored) {
                    Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(str5));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(str5));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(str5));
                    Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                    Theme.dialogs_hidePsaDrawableRecolored = true;
                }
            }
            canvas.save();
            canvas.translate(measuredWidth2, dp8);
            float f17 = this.currentRevealBounceProgress;
            if (f17 != 0.0f && f17 != f) {
                float interpolation = this.interpolator.getInterpolation(f17) + f;
                canvas.scale(interpolation, interpolation, this.translationDrawable.getIntrinsicWidth() / 2, this.translationDrawable.getIntrinsicHeight() / 2);
            }
            BaseCell.setDrawableBounds((Drawable) this.translationDrawable, 0, 0);
            this.translationDrawable.draw(canvas);
            canvas.restore();
            canvas.clipRect(measuredWidth, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            String str7 = str4;
            int ceil = (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str7));
            if (this.swipeMessageTextId != i12 || this.swipeMessageWidth != getMeasuredWidth()) {
                this.swipeMessageTextId = i12;
                this.swipeMessageWidth = getMeasuredWidth();
                StaticLayout staticLayout = new StaticLayout(str7, Theme.dialogs_archiveTextPaint, Math.min(AndroidUtilities.dp(80.0f), ceil), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                this.swipeMessageTextLayout = staticLayout;
                if (staticLayout.getLineCount() > 1) {
                    this.swipeMessageTextLayout = new StaticLayout(str7, Theme.dialogs_archiveTextPaintSmall, Math.min(AndroidUtilities.dp(82.0f), ceil), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
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
        float dp9 = AndroidUtilities.dp(8.0f) * this.cornerProgress;
        if (this.isSelected) {
            this.rect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            canvas.drawRoundRect(this.rect, dp9, dp9, Theme.dialogs_tabletSeletedPaint);
        }
        if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != 0.0f)) {
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
            canvas.drawRoundRect(this.rect, dp9, dp9, Theme.dialogs_pinnedPaint);
            if (this.isSelected) {
                canvas.drawRoundRect(this.rect, dp9, dp9, Theme.dialogs_tabletSeletedPaint);
            }
            if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != 0.0f)) {
                Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor("chats_pinnedOverlay", this.resourcesProvider), this.archiveBackgroundProgress, f));
                canvas.drawRoundRect(this.rect, dp9, dp9, Theme.dialogs_pinnedPaint);
            } else if (this.drawPin || this.drawPinBackground) {
                Theme.dialogs_pinnedPaint.setColor(Theme.getColor("chats_pinnedOverlay", this.resourcesProvider));
                canvas.drawRoundRect(this.rect, dp9, dp9, Theme.dialogs_pinnedPaint);
            }
            canvas.restore();
        }
        if (this.translationX != 0.0f) {
            float f18 = this.cornerProgress;
            if (f18 < f) {
                float f19 = f18 + (((float) j4) / 150.0f);
                this.cornerProgress = f19;
                if (f19 > f) {
                    this.cornerProgress = f;
                }
                z = true;
            }
            z = false;
        } else {
            float f20 = this.cornerProgress;
            if (f20 > 0.0f) {
                float f21 = f20 - (((float) j4) / 150.0f);
                this.cornerProgress = f21;
                if (f21 < 0.0f) {
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
        float f22 = 10.0f;
        if (this.nameLayout != null) {
            if (this.currentDialogFolderId != 0) {
                TextPaint[] textPaintArr = Theme.dialogs_namePaint;
                int i13 = this.paintIndex;
                TextPaint textPaint = textPaintArr[i13];
                TextPaint textPaint2 = textPaintArr[i13];
                int color = Theme.getColor("chats_nameArchived", this.resourcesProvider);
                textPaint2.linkColor = color;
                textPaint.setColor(color);
            } else {
                if (this.encryptedChat == null) {
                    CustomDialog customDialog = this.customDialog;
                    if (customDialog != null) {
                    }
                    TextPaint[] textPaintArr2 = Theme.dialogs_namePaint;
                    int i14 = this.paintIndex;
                    TextPaint textPaint3 = textPaintArr2[i14];
                    TextPaint textPaint4 = textPaintArr2[i14];
                    int color2 = Theme.getColor("chats_name", this.resourcesProvider);
                    textPaint4.linkColor = color2;
                    textPaint3.setColor(color2);
                }
                TextPaint[] textPaintArr3 = Theme.dialogs_namePaint;
                int i15 = this.paintIndex;
                TextPaint textPaint5 = textPaintArr3[i15];
                TextPaint textPaint6 = textPaintArr3[i15];
                int color3 = Theme.getColor("chats_secretName", this.resourcesProvider);
                textPaint6.linkColor = color3;
                textPaint5.setColor(color3);
            }
            canvas.save();
            canvas.translate(this.nameLeft, AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 10.0f : 13.0f));
            this.nameLayout.draw(canvas);
            canvas.restore();
        }
        if (this.timeLayout != null && this.currentDialogFolderId == 0) {
            canvas.save();
            canvas.translate(this.timeLeft, this.timeTop);
            this.timeLayout.draw(canvas);
            canvas.restore();
        }
        if (this.messageNameLayout != null) {
            if (this.currentDialogFolderId != 0) {
                TextPaint textPaint7 = Theme.dialogs_messageNamePaint;
                int color4 = Theme.getColor("chats_nameMessageArchived_threeLines", this.resourcesProvider);
                textPaint7.linkColor = color4;
                textPaint7.setColor(color4);
            } else if (this.draftMessage != null) {
                TextPaint textPaint8 = Theme.dialogs_messageNamePaint;
                int color5 = Theme.getColor("chats_draft", this.resourcesProvider);
                textPaint8.linkColor = color5;
                textPaint8.setColor(color5);
            } else {
                TextPaint textPaint9 = Theme.dialogs_messageNamePaint;
                int color6 = Theme.getColor("chats_nameMessage_threeLines", this.resourcesProvider);
                textPaint9.linkColor = color6;
                textPaint9.setColor(color6);
            }
            canvas.save();
            canvas.translate(this.messageNameLeft, this.messageNameTop);
            try {
                this.messageNameLayout.draw(canvas);
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            canvas.restore();
        }
        if (this.messageLayout != null) {
            if (this.currentDialogFolderId != 0) {
                if (this.chat != null) {
                    TextPaint[] textPaintArr4 = Theme.dialogs_messagePaint;
                    int i16 = this.paintIndex;
                    TextPaint textPaint10 = textPaintArr4[i16];
                    TextPaint textPaint11 = textPaintArr4[i16];
                    int color7 = Theme.getColor("chats_nameMessageArchived", this.resourcesProvider);
                    textPaint11.linkColor = color7;
                    textPaint10.setColor(color7);
                } else {
                    TextPaint[] textPaintArr5 = Theme.dialogs_messagePaint;
                    int i17 = this.paintIndex;
                    TextPaint textPaint12 = textPaintArr5[i17];
                    TextPaint textPaint13 = textPaintArr5[i17];
                    int color8 = Theme.getColor("chats_messageArchived", this.resourcesProvider);
                    textPaint13.linkColor = color8;
                    textPaint12.setColor(color8);
                }
            } else {
                TextPaint[] textPaintArr6 = Theme.dialogs_messagePaint;
                int i18 = this.paintIndex;
                TextPaint textPaint14 = textPaintArr6[i18];
                TextPaint textPaint15 = textPaintArr6[i18];
                int color9 = Theme.getColor("chats_message", this.resourcesProvider);
                textPaint15.linkColor = color9;
                textPaint14.setColor(color9);
            }
            canvas.save();
            canvas.translate(this.messageLeft, this.messageTop);
            if (!this.spoilers.isEmpty()) {
                try {
                    canvas.save();
                    SpoilerEffect.clipOutCanvas(canvas, this.spoilers);
                    this.messageLayout.draw(canvas);
                    f15 = 1.0f;
                    str = "windowBackgroundWhite";
                    i5 = 1;
                    j = j4;
                } catch (Exception e3) {
                    e = e3;
                    str = "windowBackgroundWhite";
                    j = j4;
                    i5 = 1;
                    f15 = 1.0f;
                }
                try {
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageLayout, this.animatedEmojiStack, -0.075f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f);
                    canvas.restore();
                    for (int i19 = 0; i19 < this.spoilers.size(); i19++) {
                        SpoilerEffect spoilerEffect = this.spoilers.get(i19);
                        spoilerEffect.setColor(this.messageLayout.getPaint().getColor());
                        spoilerEffect.draw(canvas);
                    }
                } catch (Exception e4) {
                    e = e4;
                    FileLog.e(e);
                    canvas.restore();
                    i6 = this.printingStringType;
                    if (i6 >= 0) {
                    }
                    i = 4;
                    if (this.currentDialogFolderId != 0) {
                    }
                    if (this.dialogsType == i2) {
                    }
                    if (!this.drawVerified) {
                    }
                    if (!this.drawReorder) {
                    }
                    Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                    BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                    Theme.dialogs_reorderDrawable.draw(canvas2);
                    if (!this.drawError) {
                    }
                    if (this.animatingArchiveAvatar) {
                    }
                    if (this.currentDialogFolderId != 0) {
                    }
                    this.avatarImage.draw(canvas2);
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
                        f4 = 0.0f;
                        if (this.translationX != f4) {
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
                    f4 = 0.0f;
                    if (this.translationX != f4) {
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
                str = "windowBackgroundWhite";
                j = j4;
                i5 = 1;
                f15 = 1.0f;
                this.messageLayout.draw(canvas);
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageLayout, this.animatedEmojiStack, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
            }
            canvas.restore();
            i6 = this.printingStringType;
            if (i6 >= 0 || (chatStatusDrawable = Theme.getChatStatusDrawable(i6)) == null) {
                i = 4;
            } else {
                canvas.save();
                int i20 = this.printingStringType;
                i = 4;
                if (i20 == i5 || i20 == 4) {
                    canvas.translate(this.statusDrawableLeft, this.messageTop + (i20 == i5 ? AndroidUtilities.dp(f15) : 0));
                } else {
                    canvas.translate(this.statusDrawableLeft, this.messageTop + ((AndroidUtilities.dp(18.0f) - chatStatusDrawable.getIntrinsicHeight()) / 2.0f));
                }
                chatStatusDrawable.draw(canvas);
                int i21 = this.statusDrawableLeft;
                invalidate(i21, this.messageTop, chatStatusDrawable.getIntrinsicWidth() + i21, this.messageTop + chatStatusDrawable.getIntrinsicHeight());
                canvas.restore();
            }
        } else {
            str = "windowBackgroundWhite";
            j = j4;
            i = 4;
        }
        if (this.currentDialogFolderId != 0) {
            int i22 = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
            int i23 = this.lastStatusDrawableParams;
            if (i23 >= 0 && i23 != i22 && !this.statusDrawableAnimationInProgress) {
                createStatusDrawableAnimator(i23, i22);
            }
            boolean z5 = this.statusDrawableAnimationInProgress;
            if (z5) {
                i22 = this.animateToStatusDrawableParams;
            }
            boolean z6 = (i22 & 1) != 0;
            boolean z7 = (i22 & 2) != 0;
            boolean z8 = (i22 & i) != 0;
            if (z5) {
                int i24 = this.animateFromStatusDrawableParams;
                boolean z9 = (i24 & 1) != 0;
                boolean z10 = (i24 & 2) != 0;
                boolean z11 = (i24 & i) != 0;
                if (!z6 && !z9 && z11 && !z10 && z7 && z8) {
                    i2 = 2;
                    f2 = 0.0f;
                    canvas2 = canvas;
                    drawCheckStatus(canvas, z6, z7, z8, true, this.statusDrawableProgress);
                    str2 = str;
                    f3 = 1.0f;
                } else {
                    i2 = 2;
                    f2 = 0.0f;
                    f3 = 1.0f;
                    boolean z12 = z9;
                    boolean z13 = z10;
                    boolean z14 = z11;
                    str2 = str;
                    canvas2 = canvas;
                    drawCheckStatus(canvas, z12, z13, z14, false, 1.0f - this.statusDrawableProgress);
                    drawCheckStatus(canvas, z6, z7, z8, false, this.statusDrawableProgress);
                }
            } else {
                canvas2 = canvas;
                str2 = str;
                f3 = 1.0f;
                i2 = 2;
                f2 = 0.0f;
                drawCheckStatus(canvas, z6, z7, z8, false, 1.0f);
            }
            this.lastStatusDrawableParams = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
        } else {
            canvas2 = canvas;
            str2 = str;
            f3 = 1.0f;
            i2 = 2;
            f2 = 0.0f;
        }
        if (this.dialogsType == i2 && (((z4 = this.dialogMuted) || this.dialogMutedProgress > f2) && !this.drawVerified && this.drawScam == 0 && !this.drawPremium)) {
            if (z4) {
                float f23 = this.dialogMutedProgress;
                if (f23 != f3) {
                    float f24 = f23 + 0.10666667f;
                    this.dialogMutedProgress = f24;
                    if (f24 > f3) {
                        this.dialogMutedProgress = f3;
                    } else {
                        invalidate();
                    }
                    BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f), AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f));
                    if (this.dialogMutedProgress == f3) {
                        canvas.save();
                        float f25 = this.dialogMutedProgress;
                        canvas2.scale(f25, f25, Theme.dialogs_muteDrawable.getBounds().centerX(), Theme.dialogs_muteDrawable.getBounds().centerY());
                        Theme.dialogs_muteDrawable.setAlpha((int) (this.dialogMutedProgress * 255.0f));
                        Theme.dialogs_muteDrawable.draw(canvas2);
                        Theme.dialogs_muteDrawable.setAlpha(255);
                        canvas.restore();
                    } else {
                        Theme.dialogs_muteDrawable.draw(canvas2);
                    }
                }
            }
            if (!z4) {
                float f26 = this.dialogMutedProgress;
                if (f26 != f2) {
                    float f27 = f26 - 0.10666667f;
                    this.dialogMutedProgress = f27;
                    if (f27 < f2) {
                        this.dialogMutedProgress = f2;
                    } else {
                        invalidate();
                    }
                }
            }
            BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f), AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f));
            if (this.dialogMutedProgress == f3) {
            }
        } else if (!this.drawVerified) {
            BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft - AndroidUtilities.dp(f3), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
            BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft - AndroidUtilities.dp(f3), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
            Theme.dialogs_verifiedDrawable.draw(canvas2);
            Theme.dialogs_verifiedCheckDrawable.draw(canvas2);
        } else if (this.drawPremium) {
            Drawable drawable = PremiumGradient.getInstance().premiumStarDrawableMini;
            BaseCell.setDrawableBounds(drawable, this.nameMuteLeft - AndroidUtilities.dp(f3), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f));
            drawable.draw(canvas2);
        } else {
            int i25 = this.drawScam;
            if (i25 != 0) {
                BaseCell.setDrawableBounds((Drawable) (i25 == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable), this.nameMuteLeft, AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.0f : 15.0f));
                (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).draw(canvas2);
            }
        }
        if (!this.drawReorder || this.reorderIconProgress != f2) {
            Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
            BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
            Theme.dialogs_reorderDrawable.draw(canvas2);
        }
        if (!this.drawError) {
            Theme.dialogs_errorDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
            this.rect.set(this.errorLeft, this.errorTop, i4 + AndroidUtilities.dp(23.0f), this.errorTop + AndroidUtilities.dp(23.0f));
            RectF rectF = this.rect;
            float f28 = AndroidUtilities.density;
            canvas2.drawRoundRect(rectF, f28 * 11.5f, f28 * 11.5f, Theme.dialogs_errorPaint);
            BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
            Theme.dialogs_errorDrawable.draw(canvas2);
        } else {
            boolean z15 = this.drawCount;
            if (((z15 || this.drawMention) && this.drawCount2) || this.countChangeProgress != f3 || this.drawReactionMention || this.reactionsMentionsChangeProgress != f3) {
                if ((z15 && this.drawCount2) || this.countChangeProgress != f3) {
                    int i26 = this.unreadCount;
                    float f29 = (i26 != 0 || this.markUnread) ? this.countChangeProgress : f3 - this.countChangeProgress;
                    StaticLayout staticLayout2 = this.countOldLayout;
                    if (staticLayout2 == null || i26 == 0) {
                        if (i26 != 0) {
                            staticLayout2 = this.countLayout;
                        }
                        Paint paint = (this.dialogMuted || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
                        paint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                        Theme.dialogs_countTextPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                        this.rect.set(this.countLeft - AndroidUtilities.dp(5.5f), this.countTop, dp7 + this.countWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                        if (f29 != f3) {
                            if (this.drawPin) {
                                Theme.dialogs_pinnedDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                                canvas.save();
                                float f30 = f3 - f29;
                                canvas2.scale(f30, f30, Theme.dialogs_pinnedDrawable.getBounds().centerX(), Theme.dialogs_pinnedDrawable.getBounds().centerY());
                                Theme.dialogs_pinnedDrawable.draw(canvas2);
                                canvas.restore();
                            }
                            canvas.save();
                            canvas2.scale(f29, f29, this.rect.centerX(), this.rect.centerY());
                        }
                        RectF rectF2 = this.rect;
                        float f31 = AndroidUtilities.density;
                        canvas2.drawRoundRect(rectF2, f31 * 11.5f, f31 * 11.5f, paint);
                        if (staticLayout2 != null) {
                            canvas.save();
                            canvas2.translate(this.countLeft, this.countTop + AndroidUtilities.dp(4.0f));
                            staticLayout2.draw(canvas2);
                            canvas.restore();
                        }
                        if (f29 != f3) {
                            canvas.restore();
                        }
                    } else {
                        Paint paint2 = (this.dialogMuted || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
                        paint2.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                        Theme.dialogs_countTextPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                        float f32 = f29 * 2.0f;
                        float f33 = f32 > f3 ? 1.0f : f32;
                        float f34 = f3 - f33;
                        float f35 = (this.countLeft * f33) + (this.countLeftOld * f34);
                        float dp10 = f35 - AndroidUtilities.dp(5.5f);
                        this.rect.set(dp10, this.countTop, (this.countWidth * f33) + dp10 + (this.countWidthOld * f34) + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                        if (f29 <= 0.5f) {
                            f14 = 0.1f;
                            f13 = CubicBezierInterpolator.EASE_OUT.getInterpolation(f32);
                        } else {
                            f14 = 0.1f;
                            f13 = CubicBezierInterpolator.EASE_IN.getInterpolation(f3 - ((f29 - 0.5f) * 2.0f));
                        }
                        float f36 = (f13 * f14) + f3;
                        canvas.save();
                        canvas2.scale(f36, f36, this.rect.centerX(), this.rect.centerY());
                        RectF rectF3 = this.rect;
                        float f37 = AndroidUtilities.density;
                        canvas2.drawRoundRect(rectF3, f37 * 11.5f, f37 * 11.5f, paint2);
                        if (this.countAnimationStableLayout != null) {
                            canvas.save();
                            canvas2.translate(f35, this.countTop + AndroidUtilities.dp(4.0f));
                            this.countAnimationStableLayout.draw(canvas2);
                            canvas.restore();
                        }
                        int alpha = Theme.dialogs_countTextPaint.getAlpha();
                        float f38 = alpha;
                        Theme.dialogs_countTextPaint.setAlpha((int) (f38 * f33));
                        if (this.countAnimationInLayout != null) {
                            canvas.save();
                            canvas2.translate(f35, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * f34) + this.countTop + AndroidUtilities.dp(4.0f));
                            this.countAnimationInLayout.draw(canvas2);
                            canvas.restore();
                        } else if (this.countLayout != null) {
                            canvas.save();
                            canvas2.translate(f35, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * f34) + this.countTop + AndroidUtilities.dp(4.0f));
                            this.countLayout.draw(canvas2);
                            canvas.restore();
                        }
                        if (this.countOldLayout != null) {
                            Theme.dialogs_countTextPaint.setAlpha((int) (f38 * f34));
                            canvas.save();
                            canvas2.translate(f35, ((this.countAnimationIncrement ? -AndroidUtilities.dp(13.0f) : AndroidUtilities.dp(13.0f)) * f33) + this.countTop + AndroidUtilities.dp(4.0f));
                            this.countOldLayout.draw(canvas2);
                            canvas.restore();
                        }
                        Theme.dialogs_countTextPaint.setAlpha(alpha);
                        canvas.restore();
                    }
                }
                if (this.drawMention) {
                    Theme.dialogs_countPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                    this.rect.set(this.mentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp6 + this.mentionWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                    Paint paint3 = (!this.dialogMuted || this.folderId == 0) ? Theme.dialogs_countPaint : Theme.dialogs_countGrayPaint;
                    RectF rectF4 = this.rect;
                    float f39 = AndroidUtilities.density;
                    canvas2.drawRoundRect(rectF4, f39 * 11.5f, f39 * 11.5f, paint3);
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
                    this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp5 + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                    Paint paint4 = Theme.dialogs_reactionsCountPaint;
                    canvas.save();
                    float f40 = this.reactionsMentionsChangeProgress;
                    if (f40 != f3) {
                        if (!this.drawReactionMention) {
                            f40 = f3 - f40;
                        }
                        canvas2.scale(f40, f40, this.rect.centerX(), this.rect.centerY());
                    }
                    RectF rectF5 = this.rect;
                    float f41 = AndroidUtilities.density;
                    canvas2.drawRoundRect(rectF5, f41 * 11.5f, f41 * 11.5f, paint4);
                    Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                    BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    Theme.dialogs_reactionsMentionDrawable.draw(canvas2);
                    canvas.restore();
                }
            } else if (this.drawPin) {
                Theme.dialogs_pinnedDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                Theme.dialogs_pinnedDrawable.draw(canvas2);
            }
        }
        if (this.animatingArchiveAvatar) {
            canvas.save();
            float interpolation2 = this.interpolator.getInterpolation(this.animatingArchiveAvatarProgress / 170.0f) + f3;
            canvas2.scale(interpolation2, interpolation2, this.avatarImage.getCenterX(), this.avatarImage.getCenterY());
        }
        if (this.currentDialogFolderId != 0 || (pullForegroundDrawable = this.archivedChatsDrawable) == null || !pullForegroundDrawable.isDraw()) {
            this.avatarImage.draw(canvas2);
        }
        if (this.hasMessageThumb) {
            this.thumbImage.draw(canvas2);
            if (this.drawPlay) {
                BaseCell.setDrawableBounds(Theme.dialogs_playDrawable, (int) (this.thumbImage.getCenterX() - (Theme.dialogs_playDrawable.getIntrinsicWidth() / 2)), (int) (this.thumbImage.getCenterY() - (Theme.dialogs_playDrawable.getIntrinsicHeight() / 2)));
                Theme.dialogs_playDrawable.draw(canvas2);
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
                        f16 = 6.0f;
                    }
                    int dp11 = (int) (imageY2 - AndroidUtilities.dp(f16));
                    if (LocaleController.isRTL) {
                        float imageX = this.avatarImage.getImageX();
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f22 = 6.0f;
                        }
                        f12 = imageX + AndroidUtilities.dp(f22);
                    } else {
                        float imageX2 = this.avatarImage.getImageX2();
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f22 = 6.0f;
                        }
                        f12 = imageX2 - AndroidUtilities.dp(f22);
                    }
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(str2, this.resourcesProvider));
                    float f42 = (int) f12;
                    float f43 = dp11;
                    canvas2.drawCircle(f42, f43, AndroidUtilities.dp(7.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle", this.resourcesProvider));
                    canvas2.drawCircle(f42, f43, AndroidUtilities.dp(5.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    if (isOnline) {
                        float f44 = this.onlineProgress;
                        if (f44 < f3) {
                            j2 = j;
                            float f45 = f44 + (((float) j2) / 150.0f);
                            this.onlineProgress = f45;
                            if (f45 > f3) {
                                this.onlineProgress = f3;
                            }
                        }
                    } else {
                        j2 = j;
                        float f46 = this.onlineProgress;
                        if (f46 > 0.0f) {
                            float f47 = f46 - (((float) j2) / 150.0f);
                            this.onlineProgress = f47;
                            if (f47 < 0.0f) {
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
                        float progress = (checkBox2 == null || !checkBox2.isChecked()) ? 1.0f : f3 - this.checkBox.getProgress();
                        float imageY22 = this.avatarImage.getImageY2();
                        if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                            f16 = 6.0f;
                        }
                        int dp12 = (int) (imageY22 - AndroidUtilities.dp(f16));
                        if (LocaleController.isRTL) {
                            float imageX3 = this.avatarImage.getImageX();
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                f22 = 6.0f;
                            }
                            f6 = imageX3 + AndroidUtilities.dp(f22);
                        } else {
                            float imageX22 = this.avatarImage.getImageX2();
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                f22 = 6.0f;
                            }
                            f6 = imageX22 - AndroidUtilities.dp(f22);
                        }
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(str2, this.resourcesProvider));
                        float f48 = (int) f6;
                        float f49 = dp12;
                        canvas2.drawCircle(f48, f49, AndroidUtilities.dp(11.0f) * this.chatCallProgress * progress, Theme.dialogs_onlineCirclePaint);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle", this.resourcesProvider));
                        canvas2.drawCircle(f48, f49, AndroidUtilities.dp(9.0f) * this.chatCallProgress * progress, Theme.dialogs_onlineCirclePaint);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(str2, this.resourcesProvider));
                        int i27 = this.progressStage;
                        if (i27 == 0) {
                            f8 = AndroidUtilities.dp(f3) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            dp3 = AndroidUtilities.dp(3.0f);
                            dp4 = AndroidUtilities.dp(2.0f);
                            f11 = this.innerProgress;
                        } else {
                            if (i27 == 1) {
                                f8 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                dp = AndroidUtilities.dp(f3);
                                dp2 = AndroidUtilities.dp(4.0f);
                                f10 = this.innerProgress;
                            } else if (i27 == 2) {
                                f8 = AndroidUtilities.dp(f3) + (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                dp3 = AndroidUtilities.dp(5.0f);
                                dp4 = AndroidUtilities.dp(4.0f);
                                f11 = this.innerProgress;
                            } else if (i27 == 3) {
                                f8 = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                dp = AndroidUtilities.dp(f3);
                                dp2 = AndroidUtilities.dp(2.0f);
                                f10 = this.innerProgress;
                            } else if (i27 == 4) {
                                f8 = AndroidUtilities.dp(f3) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                dp3 = AndroidUtilities.dp(3.0f);
                                dp4 = AndroidUtilities.dp(2.0f);
                                f11 = this.innerProgress;
                            } else if (i27 == 5) {
                                f8 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                dp = AndroidUtilities.dp(f3);
                                dp2 = AndroidUtilities.dp(4.0f);
                                f10 = this.innerProgress;
                            } else if (i27 == 6) {
                                f8 = AndroidUtilities.dp(f3) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                f7 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                if (this.chatCallProgress >= f3 || progress < f3) {
                                    canvas.save();
                                    float f50 = this.chatCallProgress;
                                    canvas2.scale(f50 * progress, f50 * progress, f48, f49);
                                }
                                this.rect.set(i3 - AndroidUtilities.dp(f3), f49 - f8, AndroidUtilities.dp(f3) + i3, f8 + f49);
                                canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                                float f51 = f49 - f7;
                                float f52 = f49 + f7;
                                this.rect.set(i3 - AndroidUtilities.dp(5.0f), f51, i3 - AndroidUtilities.dp(3.0f), f52);
                                canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                                this.rect.set(AndroidUtilities.dp(3.0f) + i3, f51, i3 + AndroidUtilities.dp(5.0f), f52);
                                canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                                if (this.chatCallProgress >= f3 || progress < f3) {
                                    canvas.restore();
                                }
                                float f53 = (float) j2;
                                f9 = this.innerProgress + (f53 / 400.0f);
                                this.innerProgress = f9;
                                if (f9 >= f3) {
                                    this.innerProgress = 0.0f;
                                    int i28 = this.progressStage + 1;
                                    this.progressStage = i28;
                                    if (i28 >= 8) {
                                        this.progressStage = 0;
                                    }
                                }
                                if (this.hasCall) {
                                    float f54 = this.chatCallProgress;
                                    if (f54 < f3) {
                                        float f55 = f54 + (f53 / 150.0f);
                                        this.chatCallProgress = f55;
                                        if (f55 > f3) {
                                            this.chatCallProgress = f3;
                                        }
                                    }
                                    f4 = 0.0f;
                                } else {
                                    float f56 = this.chatCallProgress;
                                    f4 = 0.0f;
                                    if (f56 > 0.0f) {
                                        float f57 = f56 - (f53 / 150.0f);
                                        this.chatCallProgress = f57;
                                        if (f57 < 0.0f) {
                                            this.chatCallProgress = 0.0f;
                                        }
                                    }
                                }
                                z = true;
                                if (this.translationX != f4) {
                                    canvas.restore();
                                }
                                if (this.currentDialogFolderId != 0 && this.translationX == f4 && this.archivedChatsDrawable != null) {
                                    canvas.save();
                                    canvas2.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                                    this.archivedChatsDrawable.draw(canvas2);
                                    canvas.restore();
                                }
                                if (this.useSeparator) {
                                    int dp13 = (this.fullSeparator || (this.currentDialogFolderId != 0 && this.archiveHidden && !this.fullSeparator2) || (this.fullSeparator2 && !this.archiveHidden)) ? 0 : AndroidUtilities.dp(72.0f);
                                    if (LocaleController.isRTL) {
                                        canvas.drawLine(0.0f, getMeasuredHeight() - 1, getMeasuredWidth() - dp13, getMeasuredHeight() - 1, Theme.dividerPaint);
                                    } else {
                                        canvas.drawLine(dp13, getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
                                        if (this.clipProgress != 0.0f) {
                                            if (Build.VERSION.SDK_INT != 24) {
                                                canvas.restore();
                                            } else {
                                                Theme.dialogs_pinnedPaint.setColor(Theme.getColor(str2, this.resourcesProvider));
                                                canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), this.topClip * this.clipProgress, Theme.dialogs_pinnedPaint);
                                                canvas.drawRect(0.0f, getMeasuredHeight() - ((int) (this.bottomClip * this.clipProgress)), getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
                                            }
                                        }
                                        z2 = this.drawReorder;
                                        if (!z2 || this.reorderIconProgress != 0.0f) {
                                            if (!z2) {
                                                float f58 = this.reorderIconProgress;
                                                if (f58 < f3) {
                                                    float f59 = f58 + (((float) j2) / 170.0f);
                                                    this.reorderIconProgress = f59;
                                                    if (f59 > f3) {
                                                        this.reorderIconProgress = f3;
                                                    }
                                                    f5 = 0.0f;
                                                }
                                            } else {
                                                float f60 = this.reorderIconProgress;
                                                f5 = 0.0f;
                                                if (f60 > 0.0f) {
                                                    float f61 = f60 - (((float) j2) / 170.0f);
                                                    this.reorderIconProgress = f61;
                                                    if (f61 < 0.0f) {
                                                        this.reorderIconProgress = 0.0f;
                                                    }
                                                }
                                                if (!this.archiveHidden) {
                                                    float f62 = this.archiveBackgroundProgress;
                                                    if (f62 > f5) {
                                                        float f63 = f62 - (((float) j2) / 230.0f);
                                                        this.archiveBackgroundProgress = f63;
                                                        if (f63 < f5) {
                                                            this.archiveBackgroundProgress = f5;
                                                        }
                                                        if (this.avatarDrawable.getAvatarType() == 2) {
                                                            this.avatarDrawable.setArchivedAvatarHiddenProgress(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.archiveBackgroundProgress));
                                                        }
                                                        z = true;
                                                    }
                                                    if (this.animatingArchiveAvatar) {
                                                        float f64 = this.animatingArchiveAvatarProgress + ((float) j2);
                                                        this.animatingArchiveAvatarProgress = f64;
                                                        if (f64 >= 170.0f) {
                                                            this.animatingArchiveAvatarProgress = 170.0f;
                                                            this.animatingArchiveAvatar = false;
                                                        }
                                                        z = true;
                                                    }
                                                    if (!this.drawRevealBackground) {
                                                        float f65 = this.currentRevealBounceProgress;
                                                        if (f65 < f3) {
                                                            float f66 = f65 + (((float) j2) / 170.0f);
                                                            this.currentRevealBounceProgress = f66;
                                                            if (f66 > f3) {
                                                                this.currentRevealBounceProgress = f3;
                                                                z = true;
                                                            }
                                                        }
                                                        float f67 = this.currentRevealProgress;
                                                        if (f67 < f3) {
                                                            float f68 = f67 + (((float) j2) / 300.0f);
                                                            this.currentRevealProgress = f68;
                                                            if (f68 > f3) {
                                                                this.currentRevealProgress = f3;
                                                            }
                                                            z3 = true;
                                                            if (!z3) {
                                                                return;
                                                            }
                                                            invalidate();
                                                            return;
                                                        }
                                                        z3 = z;
                                                        if (!z3) {
                                                        }
                                                    } else {
                                                        if (this.currentRevealBounceProgress == f3) {
                                                            this.currentRevealBounceProgress = 0.0f;
                                                            z3 = true;
                                                        } else {
                                                            z3 = z;
                                                        }
                                                        float f69 = this.currentRevealProgress;
                                                        if (f69 > 0.0f) {
                                                            float f70 = f69 - (((float) j2) / 300.0f);
                                                            this.currentRevealProgress = f70;
                                                            if (f70 < 0.0f) {
                                                                this.currentRevealProgress = 0.0f;
                                                            }
                                                            z3 = true;
                                                        }
                                                        if (!z3) {
                                                        }
                                                    }
                                                } else {
                                                    float f71 = this.archiveBackgroundProgress;
                                                    if (f71 < f3) {
                                                        float f72 = f71 + (((float) j2) / 230.0f);
                                                        this.archiveBackgroundProgress = f72;
                                                        if (f72 > f3) {
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
                                        f5 = 0.0f;
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
                                f8 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                dp = AndroidUtilities.dp(f3);
                                dp2 = AndroidUtilities.dp(2.0f);
                                f10 = this.innerProgress;
                            }
                            f7 = dp + (dp2 * f10);
                            if (this.chatCallProgress >= f3) {
                            }
                            canvas.save();
                            float f502 = this.chatCallProgress;
                            canvas2.scale(f502 * progress, f502 * progress, f48, f49);
                            this.rect.set(i3 - AndroidUtilities.dp(f3), f49 - f8, AndroidUtilities.dp(f3) + i3, f8 + f49);
                            canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                            float f512 = f49 - f7;
                            float f522 = f49 + f7;
                            this.rect.set(i3 - AndroidUtilities.dp(5.0f), f512, i3 - AndroidUtilities.dp(3.0f), f522);
                            canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                            this.rect.set(AndroidUtilities.dp(3.0f) + i3, f512, i3 + AndroidUtilities.dp(5.0f), f522);
                            canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                            if (this.chatCallProgress >= f3) {
                            }
                            canvas.restore();
                            float f532 = (float) j2;
                            f9 = this.innerProgress + (f532 / 400.0f);
                            this.innerProgress = f9;
                            if (f9 >= f3) {
                            }
                            if (this.hasCall) {
                            }
                            z = true;
                            if (this.translationX != f4) {
                            }
                            if (this.currentDialogFolderId != 0) {
                                canvas.save();
                                canvas2.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                                this.archivedChatsDrawable.draw(canvas2);
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
                        f7 = dp3 - (dp4 * f11);
                        if (this.chatCallProgress >= f3) {
                        }
                        canvas.save();
                        float f5022 = this.chatCallProgress;
                        canvas2.scale(f5022 * progress, f5022 * progress, f48, f49);
                        this.rect.set(i3 - AndroidUtilities.dp(f3), f49 - f8, AndroidUtilities.dp(f3) + i3, f8 + f49);
                        canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                        float f5122 = f49 - f7;
                        float f5222 = f49 + f7;
                        this.rect.set(i3 - AndroidUtilities.dp(5.0f), f5122, i3 - AndroidUtilities.dp(3.0f), f5222);
                        canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                        this.rect.set(AndroidUtilities.dp(3.0f) + i3, f5122, i3 + AndroidUtilities.dp(5.0f), f5222);
                        canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f3), AndroidUtilities.dp(f3), Theme.dialogs_onlineCirclePaint);
                        if (this.chatCallProgress >= f3) {
                        }
                        canvas.restore();
                        float f5322 = (float) j2;
                        f9 = this.innerProgress + (f5322 / 400.0f);
                        this.innerProgress = f9;
                        if (f9 >= f3) {
                        }
                        if (this.hasCall) {
                        }
                        z = true;
                        if (this.translationX != f4) {
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
            f4 = 0.0f;
            if (this.translationX != f4) {
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
        f4 = 0.0f;
        if (this.translationX != f4) {
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
