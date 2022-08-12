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
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
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

    /* JADX WARN: Can't wrap try/catch for region: R(17:912|(2:914|(1:916)(1:928))(2:929|(2:931|(1:933)(1:934))(2:935|(1:937)(14:938|(2:940|(1:942)(1:943))(1:944)|918|919|920|(1:922)(1:925)|923|828|(1:863)(6:832|833|834|835|836|837)|838|(1:842)|843|(4:845|(1:847)|848|(1:850)(1:851))|852)))|917|918|919|920|(0)(0)|923|828|(1:830)|859|863|838|(2:840|842)|843|(0)|852) */
    /* JADX WARN: Code restructure failed: missing block: B:1127:0x0471, code lost:
        if (r2.post_messages == false) goto L525;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1133:0x047d, code lost:
        if (r2.kicked != false) goto L525;
     */
    /* JADX WARN: Code restructure failed: missing block: B:926:0x0adf, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:927:0x0ae0, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:1017:0x0ebb  */
    /* JADX WARN: Removed duplicated region for block: B:1018:0x0c3f  */
    /* JADX WARN: Removed duplicated region for block: B:1078:0x0e0b  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x1487  */
    /* JADX WARN: Removed duplicated region for block: B:1134:0x0483  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x14ac  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x167c  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x178c A[Catch: Exception -> 0x184e, TryCatch #2 {Exception -> 0x184e, blocks: (B:148:0x1757, B:150:0x175b, B:153:0x1773, B:155:0x1779, B:156:0x1788, B:158:0x178c, B:160:0x17a0, B:162:0x17a6, B:164:0x17aa, B:167:0x17b7, B:169:0x17b4, B:172:0x17ba, B:174:0x17be, B:177:0x17c3, B:179:0x17c7, B:181:0x17d3, B:182:0x17dd, B:183:0x1828, B:305:0x17f5, B:308:0x17fb, B:309:0x1802, B:312:0x1818, B:315:0x175f, B:317:0x1763, B:319:0x1768), top: B:147:0x1757 }] */
    /* JADX WARN: Removed duplicated region for block: B:179:0x17c7 A[Catch: Exception -> 0x184e, TryCatch #2 {Exception -> 0x184e, blocks: (B:148:0x1757, B:150:0x175b, B:153:0x1773, B:155:0x1779, B:156:0x1788, B:158:0x178c, B:160:0x17a0, B:162:0x17a6, B:164:0x17aa, B:167:0x17b7, B:169:0x17b4, B:172:0x17ba, B:174:0x17be, B:177:0x17c3, B:179:0x17c7, B:181:0x17d3, B:182:0x17dd, B:183:0x1828, B:305:0x17f5, B:308:0x17fb, B:309:0x1802, B:312:0x1818, B:315:0x175f, B:317:0x1763, B:319:0x1768), top: B:147:0x1757 }] */
    /* JADX WARN: Removed duplicated region for block: B:186:0x1859  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x1a52  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x1a63 A[Catch: Exception -> 0x1a8e, TryCatch #1 {Exception -> 0x1a8e, blocks: (B:244:0x1a56, B:246:0x1a63, B:247:0x1a65, B:249:0x1a7e, B:250:0x1a85), top: B:243:0x1a56 }] */
    /* JADX WARN: Removed duplicated region for block: B:249:0x1a7e A[Catch: Exception -> 0x1a8e, TryCatch #1 {Exception -> 0x1a8e, blocks: (B:244:0x1a56, B:246:0x1a63, B:247:0x1a65, B:249:0x1a7e, B:250:0x1a85), top: B:243:0x1a56 }] */
    /* JADX WARN: Removed duplicated region for block: B:256:0x1a96  */
    /* JADX WARN: Removed duplicated region for block: B:271:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:272:0x19c0  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x1813  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x1816  */
    /* JADX WARN: Removed duplicated region for block: B:342:0x14dc  */
    /* JADX WARN: Removed duplicated region for block: B:414:0x141c  */
    /* JADX WARN: Removed duplicated region for block: B:416:0x143a  */
    /* JADX WARN: Removed duplicated region for block: B:423:0x1254  */
    /* JADX WARN: Removed duplicated region for block: B:426:0x126a  */
    /* JADX WARN: Removed duplicated region for block: B:441:0x11b1  */
    /* JADX WARN: Removed duplicated region for block: B:453:0x1157  */
    /* JADX WARN: Removed duplicated region for block: B:455:0x113c  */
    /* JADX WARN: Removed duplicated region for block: B:514:0x0422  */
    /* JADX WARN: Removed duplicated region for block: B:519:0x042e  */
    /* JADX WARN: Removed duplicated region for block: B:528:0x0488  */
    /* JADX WARN: Removed duplicated region for block: B:539:0x0ed3  */
    /* JADX WARN: Removed duplicated region for block: B:542:0x0ef8  */
    /* JADX WARN: Removed duplicated region for block: B:545:0x102e  */
    /* JADX WARN: Removed duplicated region for block: B:553:0x1094  */
    /* JADX WARN: Removed duplicated region for block: B:555:0x10a3  */
    /* JADX WARN: Removed duplicated region for block: B:561:0x10f2  */
    /* JADX WARN: Removed duplicated region for block: B:587:0x108b  */
    /* JADX WARN: Removed duplicated region for block: B:588:0x0f0c  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x10fd  */
    /* JADX WARN: Removed duplicated region for block: B:649:0x0edb  */
    /* JADX WARN: Removed duplicated region for block: B:659:0x050d  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x1147  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x116f  */
    /* JADX WARN: Removed duplicated region for block: B:736:0x0ec5  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x1183  */
    /* JADX WARN: Removed duplicated region for block: B:830:0x0b86  */
    /* JADX WARN: Removed duplicated region for block: B:840:0x0bd1  */
    /* JADX WARN: Removed duplicated region for block: B:845:0x0be2  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x12b3  */
    /* JADX WARN: Removed duplicated region for block: B:922:0x0acc A[Catch: Exception -> 0x0adf, TryCatch #0 {Exception -> 0x0adf, blocks: (B:920:0x0ac1, B:922:0x0acc, B:923:0x0ad4), top: B:919:0x0ac1 }] */
    /* JADX WARN: Removed duplicated region for block: B:925:0x0ad3  */
    /* JADX WARN: Removed duplicated region for block: B:999:0x0e1b  */
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
        int measuredWidth;
        int i10;
        CharSequence charSequence10;
        int i11;
        int dp;
        int dp2;
        int i12;
        int i13;
        int max;
        boolean z8;
        MessageObject messageObject5;
        CharSequence highlightText2;
        int lineCount;
        StaticLayout staticLayout;
        StaticLayout staticLayout2;
        float primaryHorizontal;
        float primaryHorizontal2;
        int i14;
        int length;
        int i15;
        int ceil;
        int lineCount2;
        boolean z9;
        CharacterStyle[] characterStyleArr;
        CharSequence replaceTwoNewLinesToOne;
        CharSequence highlightText3;
        int dp3;
        int dp4;
        int dp5;
        CharSequence highlightText4;
        int i16;
        CharSequence charSequence11;
        String str22;
        SpannableStringBuilder valueOf2;
        int i17 = 1;
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
            int i19 = this.customDialog.unread_count;
            if (i19 != 0) {
                this.drawCount = true;
                str22 = String.format("%d", Integer.valueOf(i19));
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
                                    i17 = 0;
                                    i18 = 1;
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
                                        i17 = 0;
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
                                                int i20 = this.dialogsType;
                                                if (i20 == 2) {
                                                    TLRPC$Chat tLRPC$Chat5 = this.chat;
                                                    if (tLRPC$Chat5 != null) {
                                                        if (ChatObject.isChannel(tLRPC$Chat5)) {
                                                            TLRPC$Chat tLRPC$Chat6 = this.chat;
                                                            if (!tLRPC$Chat6.megagroup) {
                                                                int i21 = tLRPC$Chat6.participants_count;
                                                                if (i21 != 0) {
                                                                    string = LocaleController.formatPluralStringComma("Subscribers", i21);
                                                                } else if (TextUtils.isEmpty(tLRPC$Chat6.username)) {
                                                                    string = LocaleController.getString("ChannelPrivate", R.string.ChannelPrivate).toLowerCase();
                                                                } else {
                                                                    string = LocaleController.getString("ChannelPublic", R.string.ChannelPublic).toLowerCase();
                                                                }
                                                            }
                                                        }
                                                        TLRPC$Chat tLRPC$Chat7 = this.chat;
                                                        int i22 = tLRPC$Chat7.participants_count;
                                                        if (i22 != 0) {
                                                            string = LocaleController.formatPluralStringComma("Members", i22);
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
                                                } else if (i20 == 3 && UserObject.isUserSelf(this.user)) {
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
                                                                                        int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp(119.0f);
                                                                                        if (z) {
                                                                                            if (!TextUtils.isEmpty(str7)) {
                                                                                                measuredWidth2 = (int) (measuredWidth2 - textPaint5.measureText(str7.toString()));
                                                                                            }
                                                                                            measuredWidth2 = (int) (measuredWidth2 - textPaint5.measureText(": "));
                                                                                        }
                                                                                        if (measuredWidth2 > 0) {
                                                                                            str27 = AndroidUtilities.ellipsizeCenterEnd(str27, this.message.highlightedWords.get(0), measuredWidth2, textPaint5, 130).toString();
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
                                                                                                i17 = 1;
                                                                                                i18 = 0;
                                                                                                str10 = replaceEmoji;
                                                                                                if (this.currentDialogFolderId != 0) {
                                                                                                }
                                                                                                i6 = -1;
                                                                                                int i23 = i18;
                                                                                                i18 = i17;
                                                                                                i17 = i23;
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
                                                                                                i8 = i17;
                                                                                                i17 = i18;
                                                                                                i18 = i7;
                                                                                                charSequence9 = charSequence5;
                                                                                                if (i17 == 0) {
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
                                                                                                dp5 = measuredWidth - AndroidUtilities.dp(12.0f);
                                                                                                if (dp5 < 0) {
                                                                                                }
                                                                                                CharSequence replaceEmoji2 = Emoji.replaceEmoji(TextUtils.ellipsize(str21.replace('\n', ' '), Theme.dialogs_namePaint[this.paintIndex], dp5, TextUtils.TruncateAt.END), Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                MessageObject messageObject13 = this.message;
                                                                                                this.nameLayout = new StaticLayout((messageObject13 != null || !messageObject13.hasHighlightedWords() || (highlightText4 = AndroidUtilities.highlightText(replaceEmoji2, this.message.highlightedWords, this.resourcesProvider)) == null) ? replaceEmoji2 : highlightText4, Theme.dialogs_namePaint[this.paintIndex], measuredWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                                                                                                if (!this.useForceThreeLines) {
                                                                                                }
                                                                                                i10 = i18;
                                                                                                charSequence10 = "";
                                                                                                i11 = i6;
                                                                                                int dp6 = AndroidUtilities.dp(11.0f);
                                                                                                this.messageNameTop = AndroidUtilities.dp(32.0f);
                                                                                                this.timeTop = AndroidUtilities.dp(13.0f);
                                                                                                this.errorTop = AndroidUtilities.dp(43.0f);
                                                                                                this.pinTop = AndroidUtilities.dp(43.0f);
                                                                                                this.countTop = AndroidUtilities.dp(43.0f);
                                                                                                this.checkDrawTop = AndroidUtilities.dp(13.0f);
                                                                                                int measuredWidth3 = getMeasuredWidth() - AndroidUtilities.dp(93.0f);
                                                                                                if (!LocaleController.isRTL) {
                                                                                                }
                                                                                                this.avatarImage.setImageCoords(dp, dp6, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                                                                                                this.thumbImage.setImageCoords(dp2, AndroidUtilities.dp(31.0f) + dp6, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                                                                                                i12 = dp6;
                                                                                                i13 = measuredWidth3;
                                                                                                if (this.drawPin) {
                                                                                                }
                                                                                                if (!this.drawError) {
                                                                                                }
                                                                                                int i24 = i13;
                                                                                                if (i8 != 0) {
                                                                                                }
                                                                                                max = Math.max(AndroidUtilities.dp(12.0f), i24);
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
                                                                                                this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence8, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence9 == null ? 1 : 2);
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
                                                                                        i17 = 1;
                                                                                        i18 = 0;
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
                                                                                    i17 = 1;
                                                                                    i18 = 0;
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
                                                                                            int measuredWidth4 = getMeasuredWidth() - AndroidUtilities.dp(105.0f);
                                                                                            if (z) {
                                                                                                if (!TextUtils.isEmpty(str7)) {
                                                                                                    measuredWidth4 = (int) (measuredWidth4 - textPaint5.measureText(str7.toString()));
                                                                                                }
                                                                                                measuredWidth4 = (int) (measuredWidth4 - textPaint5.measureText(": "));
                                                                                            }
                                                                                            if (measuredWidth4 > 0) {
                                                                                                charSequence15 = AndroidUtilities.ellipsizeCenterEnd(charSequence15, this.message.highlightedWords.get(0), measuredWidth4, textPaint5, 130).toString();
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
                                                                            i17 = 1;
                                                                            i18 = 0;
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
                                                                                        int measuredWidth5 = getMeasuredWidth() - AndroidUtilities.dp(119.0f);
                                                                                        if (z) {
                                                                                            if (!TextUtils.isEmpty(null)) {
                                                                                                throw null;
                                                                                            }
                                                                                            measuredWidth5 = (int) (measuredWidth5 - textPaint5.measureText(": "));
                                                                                        }
                                                                                        if (measuredWidth5 > 0) {
                                                                                            str30 = AndroidUtilities.ellipsizeCenterEnd(str30, this.message.highlightedWords.get(0), measuredWidth5, textPaint5, 130).toString();
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
                                                                                            i17 = 1;
                                                                                            i18 = 0;
                                                                                            str10 = charSequence4;
                                                                                        } else {
                                                                                            charSequence2 = charSequence;
                                                                                            charSequence3 = null;
                                                                                            i3 = 0;
                                                                                            i17 = 1;
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
                                                        i18 = 1;
                                                        charSequence5 = charSequence3;
                                                        str10 = charSequence2;
                                                    }
                                                    if (this.currentDialogFolderId != 0) {
                                                        charSequence5 = formatArchivedDialogNames();
                                                    }
                                                    i6 = -1;
                                                    int i232 = i18;
                                                    i18 = i17;
                                                    i17 = i232;
                                                    spannableStringBuilder2 = str10;
                                                }
                                                str10 = string;
                                                charSequence5 = null;
                                                i3 = 0;
                                                i17 = 0;
                                                i18 = 1;
                                                z10 = false;
                                                if (this.currentDialogFolderId != 0) {
                                                }
                                                i6 = -1;
                                                int i2322 = i18;
                                                i18 = i17;
                                                i17 = i2322;
                                                spannableStringBuilder2 = str10;
                                            }
                                        }
                                        charSequence5 = null;
                                        i3 = 0;
                                        str13 = str11;
                                    }
                                    i18 = 1;
                                    str12 = str13;
                                    i6 = -1;
                                    spannableStringBuilder2 = str12;
                                }
                                if (this.draftMessage == null) {
                                    stringForMessageListDate = LocaleController.stringForMessageListDate(tLRPC$DraftMessage.date);
                                } else {
                                    int i25 = this.lastMessageDate;
                                    if (i25 != 0) {
                                        stringForMessageListDate = LocaleController.stringForMessageListDate(i25);
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
                                        int i26 = this.unreadCount;
                                        int i27 = this.mentionCount;
                                        if (i26 + i27 <= 0) {
                                            z7 = false;
                                            this.drawCount = false;
                                            this.drawMention = false;
                                            str16 = null;
                                        } else if (i26 > i27) {
                                            this.drawCount = true;
                                            this.drawMention = false;
                                            z7 = false;
                                            str17 = String.format("%d", Integer.valueOf(i26 + i27));
                                            str16 = null;
                                            this.drawReactionMention = z7;
                                            str15 = str16;
                                            str14 = str17;
                                        } else {
                                            this.drawCount = false;
                                            this.drawMention = true;
                                            z7 = false;
                                            str16 = String.format("%d", Integer.valueOf(i26 + i27));
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
                                            int i28 = this.unreadCount;
                                            if (i28 != 0 && (i28 != 1 || i28 != this.mentionCount || messageObject3 == null || !messageObject3.messageOwner.mentioned)) {
                                                z4 = true;
                                                this.drawCount = true;
                                                z5 = false;
                                                str14 = String.format("%d", Integer.valueOf(i28));
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
                                        int i29 = messagesController2.promoDialogType;
                                        if (i29 == MessagesController.PROMO_TYPE_PROXY) {
                                            string2 = LocaleController.getString("UseProxySponsor", R.string.UseProxySponsor);
                                        } else if (i29 == MessagesController.PROMO_TYPE_PSA) {
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
                                                i8 = i17;
                                                i17 = i18;
                                                i18 = i7;
                                            }
                                        }
                                        str20 = string2;
                                        str19 = charSequence7;
                                        if (this.currentDialogFolderId == 0) {
                                        }
                                        charSequence8 = str19;
                                        i8 = i17;
                                        i17 = i18;
                                        i18 = i7;
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
                                i8 = i17;
                                i17 = i18;
                                i18 = i7;
                            }
                        } else {
                            str2 = str;
                        }
                        z2 = false;
                        this.drawPremium = z2;
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
                        i8 = i17;
                        i17 = i18;
                        i18 = i7;
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
            i8 = i17;
            i17 = i18;
            i18 = i7;
        }
        charSequence9 = charSequence5;
        if (i17 == 0) {
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
            measuredWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(14.0f)) - i9;
        } else {
            measuredWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(77.0f)) - i9;
            this.nameLeft += i9;
        }
        if (this.drawNameLock) {
            measuredWidth -= AndroidUtilities.dp(4.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
        }
        if (!this.drawClock) {
            int intrinsicWidth2 = Theme.dialogs_clockDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            int i30 = measuredWidth - intrinsicWidth2;
            if (!LocaleController.isRTL) {
                this.clockDrawLeft = this.timeLeft - intrinsicWidth2;
                i16 = i30;
            } else {
                i16 = i30;
                this.clockDrawLeft = this.timeLeft + i9 + AndroidUtilities.dp(5.0f);
                this.nameLeft += intrinsicWidth2;
            }
            measuredWidth = i16;
        } else if (this.drawCheck2) {
            int intrinsicWidth3 = Theme.dialogs_checkDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            measuredWidth -= intrinsicWidth3;
            if (this.drawCheck1) {
                measuredWidth -= Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0f);
                if (!LocaleController.isRTL) {
                    int i31 = this.timeLeft - intrinsicWidth3;
                    this.halfCheckDrawLeft = i31;
                    this.checkDrawLeft = i31 - AndroidUtilities.dp(5.5f);
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
        if (!this.dialogMuted && !this.drawVerified && this.drawScam == 0) {
            int dp8 = AndroidUtilities.dp(6.0f) + Theme.dialogs_muteDrawable.getIntrinsicWidth();
            measuredWidth -= dp8;
            if (LocaleController.isRTL) {
                this.nameLeft += dp8;
            }
        } else if (!this.drawVerified) {
            int dp9 = AndroidUtilities.dp(6.0f) + Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
            measuredWidth -= dp9;
            if (LocaleController.isRTL) {
                this.nameLeft += dp9;
            }
        } else if (this.drawPremium) {
            int dp10 = AndroidUtilities.dp(6.0f) + PremiumGradient.getInstance().premiumStarDrawableMini.getIntrinsicWidth();
            measuredWidth -= dp10;
            if (LocaleController.isRTL) {
                this.nameLeft += dp10;
            }
        } else if (this.drawScam != 0) {
            int dp11 = AndroidUtilities.dp(6.0f) + (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).getIntrinsicWidth();
            measuredWidth -= dp11;
            if (LocaleController.isRTL) {
                this.nameLeft += dp11;
            }
        }
        try {
            dp5 = measuredWidth - AndroidUtilities.dp(12.0f);
            if (dp5 < 0) {
                dp5 = 0;
            }
            CharSequence replaceEmoji22 = Emoji.replaceEmoji(TextUtils.ellipsize(str21.replace('\n', ' '), Theme.dialogs_namePaint[this.paintIndex], dp5, TextUtils.TruncateAt.END), Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            MessageObject messageObject132 = this.message;
            this.nameLayout = new StaticLayout((messageObject132 != null || !messageObject132.hasHighlightedWords() || (highlightText4 = AndroidUtilities.highlightText(replaceEmoji22, this.message.highlightedWords, this.resourcesProvider)) == null) ? replaceEmoji22 : highlightText4, Theme.dialogs_namePaint[this.paintIndex], measuredWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        } catch (Exception e4) {
            FileLog.e(e4);
        }
        if (!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
            i10 = i18;
            charSequence10 = "";
            i11 = i6;
            int dp62 = AndroidUtilities.dp(11.0f);
            this.messageNameTop = AndroidUtilities.dp(32.0f);
            this.timeTop = AndroidUtilities.dp(13.0f);
            this.errorTop = AndroidUtilities.dp(43.0f);
            this.pinTop = AndroidUtilities.dp(43.0f);
            this.countTop = AndroidUtilities.dp(43.0f);
            this.checkDrawTop = AndroidUtilities.dp(13.0f);
            int measuredWidth32 = getMeasuredWidth() - AndroidUtilities.dp(93.0f);
            if (!LocaleController.isRTL) {
                int dp12 = AndroidUtilities.dp(16.0f);
                this.messageNameLeft = dp12;
                this.messageLeft = dp12;
                dp = getMeasuredWidth() - AndroidUtilities.dp(66.0f);
                dp2 = dp - AndroidUtilities.dp(31.0f);
            } else {
                int dp13 = AndroidUtilities.dp(78.0f);
                this.messageNameLeft = dp13;
                this.messageLeft = dp13;
                dp = AndroidUtilities.dp(10.0f);
                dp2 = AndroidUtilities.dp(69.0f) + dp;
            }
            this.avatarImage.setImageCoords(dp, dp62, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            this.thumbImage.setImageCoords(dp2, AndroidUtilities.dp(31.0f) + dp62, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
            i12 = dp62;
            i13 = measuredWidth32;
        } else {
            int dp14 = AndroidUtilities.dp(9.0f);
            this.messageNameTop = AndroidUtilities.dp(31.0f);
            this.timeTop = AndroidUtilities.dp(16.0f);
            this.errorTop = AndroidUtilities.dp(39.0f);
            this.pinTop = AndroidUtilities.dp(39.0f);
            this.countTop = AndroidUtilities.dp(39.0f);
            this.checkDrawTop = AndroidUtilities.dp(17.0f);
            int measuredWidth6 = getMeasuredWidth() - AndroidUtilities.dp(95.0f);
            if (LocaleController.isRTL) {
                int dp15 = AndroidUtilities.dp(22.0f);
                this.messageNameLeft = dp15;
                this.messageLeft = dp15;
                dp3 = getMeasuredWidth() - AndroidUtilities.dp(64.0f);
                dp4 = dp3 - AndroidUtilities.dp(i + 11);
            } else {
                int dp16 = AndroidUtilities.dp(76.0f);
                this.messageNameLeft = dp16;
                this.messageLeft = dp16;
                dp3 = AndroidUtilities.dp(10.0f);
                dp4 = AndroidUtilities.dp(67.0f) + dp3;
            }
            i13 = measuredWidth6;
            charSequence10 = "";
            i11 = i6;
            i10 = i18;
            this.avatarImage.setImageCoords(dp3, dp14, AndroidUtilities.dp(54.0f), AndroidUtilities.dp(54.0f));
            float f = i;
            this.thumbImage.setImageCoords(dp4, AndroidUtilities.dp(30.0f) + dp14, AndroidUtilities.dp(f), AndroidUtilities.dp(f));
            i12 = dp14;
        }
        if (this.drawPin) {
            if (!LocaleController.isRTL) {
                this.pinLeft = (getMeasuredWidth() - Theme.dialogs_pinnedDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(14.0f);
            } else {
                this.pinLeft = AndroidUtilities.dp(14.0f);
            }
        }
        if (!this.drawError) {
            int dp17 = AndroidUtilities.dp(31.0f);
            i13 -= dp17;
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
                i13 -= dp18;
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
                i13 -= dp19;
                if (!LocaleController.isRTL) {
                    int measuredWidth7 = (getMeasuredWidth() - this.mentionWidth) - AndroidUtilities.dp(20.0f);
                    int i32 = this.countWidth;
                    this.mentionLeft = measuredWidth7 - (i32 != 0 ? i32 + AndroidUtilities.dp(18.0f) : 0);
                } else {
                    int dp20 = AndroidUtilities.dp(20.0f);
                    int i33 = this.countWidth;
                    this.mentionLeft = dp20 + (i33 != 0 ? i33 + AndroidUtilities.dp(18.0f) : 0);
                    this.messageLeft += dp19;
                    this.messageNameLeft += dp19;
                }
                this.drawMention = true;
            } else {
                this.mentionWidth = 0;
            }
            if (this.drawReactionMention) {
                int dp21 = AndroidUtilities.dp(24.0f);
                i13 -= dp21;
                if (!LocaleController.isRTL) {
                    int measuredWidth8 = getMeasuredWidth() - AndroidUtilities.dp(32.0f);
                    this.reactionMentionLeft = measuredWidth8;
                    if (this.drawMention) {
                        int i34 = this.mentionWidth;
                        this.reactionMentionLeft = measuredWidth8 - (i34 != 0 ? i34 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i35 = this.reactionMentionLeft;
                        int i36 = this.countWidth;
                        this.reactionMentionLeft = i35 - (i36 != 0 ? i36 + AndroidUtilities.dp(18.0f) : 0);
                    }
                } else {
                    int dp22 = AndroidUtilities.dp(20.0f);
                    this.reactionMentionLeft = dp22;
                    if (this.drawMention) {
                        int i37 = this.mentionWidth;
                        this.reactionMentionLeft = dp22 + (i37 != 0 ? i37 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i38 = this.reactionMentionLeft;
                        int i39 = this.countWidth;
                        this.reactionMentionLeft = i38 + (i39 != 0 ? i39 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    this.messageLeft += dp21;
                    this.messageNameLeft += dp21;
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
        int i242 = i13;
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
        max = Math.max(AndroidUtilities.dp(12.0f), i242);
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
        } catch (Exception e5) {
            this.messageLayout = null;
            FileLog.e(e5);
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
            if (!LocaleController.isRTL) {
                StaticLayout staticLayout3 = this.nameLayout;
                if (staticLayout3 != null && staticLayout3.getLineCount() > 0) {
                    float lineLeft = this.nameLayout.getLineLeft(0);
                    double ceil2 = Math.ceil(this.nameLayout.getLineWidth(0));
                    if (this.dialogMuted && !this.drawVerified && this.drawScam == 0) {
                        double d = this.nameLeft;
                        double d2 = measuredWidth;
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
                        double d6 = measuredWidth;
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
                        double d10 = measuredWidth;
                        Double.isNaN(d10);
                        Double.isNaN(d9);
                        double d11 = d9 + (d10 - ceil2);
                        double dp25 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(dp25);
                        double d12 = d11 - dp25;
                        double intrinsicWidth7 = PremiumGradient.getInstance().premiumStarDrawableMini.getIntrinsicWidth();
                        Double.isNaN(intrinsicWidth7);
                        this.nameMuteLeft = (int) (d12 - intrinsicWidth7);
                    } else if (this.drawScam != 0) {
                        double d13 = this.nameLeft;
                        double d14 = measuredWidth;
                        Double.isNaN(d14);
                        Double.isNaN(d13);
                        double d15 = d13 + (d14 - ceil2);
                        double dp26 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(dp26);
                        double d16 = d15 - dp26;
                        double intrinsicWidth8 = (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).getIntrinsicWidth();
                        Double.isNaN(intrinsicWidth8);
                        this.nameMuteLeft = (int) (d16 - intrinsicWidth8);
                    }
                    if (lineLeft == 0.0f) {
                        double d17 = measuredWidth;
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
                    int i40 = 0;
                    int i41 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    while (true) {
                        if (i40 >= lineCount2) {
                            break;
                        }
                        if (this.messageLayout.getLineLeft(i40) != 0.0f) {
                            i41 = 0;
                            break;
                        }
                        double ceil3 = Math.ceil(this.messageLayout.getLineWidth(i40));
                        double d19 = max;
                        Double.isNaN(d19);
                        i41 = Math.min(i41, (int) (d19 - ceil3));
                        i40++;
                    }
                    if (i41 != Integer.MAX_VALUE) {
                        this.messageLeft += i41;
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
                    if (lineRight == measuredWidth) {
                        double ceil5 = Math.ceil(this.nameLayout.getLineWidth(0));
                        double d22 = measuredWidth;
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
                    float f2 = 2.14748365E9f;
                    for (int i42 = 0; i42 < lineCount; i42++) {
                        f2 = Math.min(f2, this.messageLayout.getLineLeft(i42));
                    }
                    this.messageLeft = (int) (this.messageLeft - f2);
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
                    i15 = i10;
                    if (i15 >= length) {
                        i15 = length - 1;
                    }
                    ceil = (int) Math.ceil(Math.min(this.messageLayout.getPrimaryHorizontal(i15), this.messageLayout.getPrimaryHorizontal(i15 + 1)));
                    if (ceil != 0) {
                        ceil += AndroidUtilities.dp(3.0f);
                    }
                    this.thumbImage.setImageX(this.messageLeft + ceil);
                } catch (Exception e6) {
                    FileLog.e(e6);
                }
            }
            staticLayout2 = this.messageLayout;
            if (staticLayout2 != null || this.printingStringType < 0) {
            }
            if (i11 >= 0 && (i14 = i11 + 1) < staticLayout2.getText().length()) {
                primaryHorizontal = this.messageLayout.getPrimaryHorizontal(i11);
                primaryHorizontal2 = this.messageLayout.getPrimaryHorizontal(i14);
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
        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence8, textPaint5, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence9 == null ? 1 : 2);
        this.spoilersPool.addAll(this.spoilers);
        this.spoilers.clear();
        SpoilerEffect.addSpoilers(this, this.messageLayout, this.spoilersPool, this.spoilers);
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
        if (!LocaleController.isRTL) {
        }
        staticLayout = this.messageLayout;
        if (staticLayout != null) {
            length = staticLayout.getText().length();
            i15 = i10;
            if (i15 >= length) {
            }
            ceil = (int) Math.ceil(Math.min(this.messageLayout.getPrimaryHorizontal(i15), this.messageLayout.getPrimaryHorizontal(i15 + 1)));
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

    /* JADX WARN: Removed duplicated region for block: B:108:0x01ac  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0577  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x025b  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x0260  */
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

    /* JADX WARN: Code restructure failed: missing block: B:673:0x0613, code lost:
        if (r0.type != 2) goto L674;
     */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0826  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x08fb  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x095c  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x096e  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0a57  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x0a81  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x0ebb  */
    /* JADX WARN: Removed duplicated region for block: B:209:0x0edb  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x0eee  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x0f26  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x0f2d  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x12ce  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x12d5  */
    /* JADX WARN: Removed duplicated region for block: B:268:0x12fa  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x135e  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x13ab  */
    /* JADX WARN: Removed duplicated region for block: B:299:0x13e4  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x143c  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x1450  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x1496  */
    /* JADX WARN: Removed duplicated region for block: B:330:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:331:0x1476  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x140e  */
    /* JADX WARN: Removed duplicated region for block: B:348:0x13b7  */
    /* JADX WARN: Removed duplicated region for block: B:355:0x13ca  */
    /* JADX WARN: Removed duplicated region for block: B:376:0x100c  */
    /* JADX WARN: Removed duplicated region for block: B:408:0x11e6  */
    /* JADX WARN: Removed duplicated region for block: B:412:0x1273  */
    /* JADX WARN: Removed duplicated region for block: B:416:0x1288  */
    /* JADX WARN: Removed duplicated region for block: B:421:0x129c  */
    /* JADX WARN: Removed duplicated region for block: B:428:0x12af  */
    /* JADX WARN: Removed duplicated region for block: B:466:0x0acd  */
    /* JADX WARN: Removed duplicated region for block: B:578:0x09a7  */
    /* JADX WARN: Removed duplicated region for block: B:579:0x095f  */
    /* JADX WARN: Removed duplicated region for block: B:591:0x09b2  */
    /* JADX WARN: Removed duplicated region for block: B:604:0x09f9  */
    /* JADX WARN: Removed duplicated region for block: B:641:0x08ee  */
    /* JADX WARN: Removed duplicated region for block: B:644:0x07f4  */
    /* JADX WARN: Removed duplicated region for block: B:646:0x07f9  */
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
        String str3;
        long j;
        int i3;
        Canvas canvas2;
        String str4;
        float f3;
        float f4;
        int i4;
        int dp;
        int dp2;
        int dp3;
        float f5;
        float interpolation;
        long j2;
        float f6;
        boolean z2;
        float f7;
        boolean z3;
        TLRPC$User tLRPC$User;
        TLRPC$Chat tLRPC$Chat;
        float dp4;
        int i5;
        float dp5;
        float dp6;
        float dp7;
        float f8;
        float dp8;
        float dp9;
        float dp10;
        float f9;
        float f10;
        float dp11;
        PullForegroundDrawable pullForegroundDrawable;
        int i6;
        boolean z4;
        float f11;
        int i7;
        int i8;
        StatusDrawable chatStatusDrawable;
        int i9;
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
        float f12 = 8.0f;
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
            int i10 = color2;
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
                Theme.dialogs_pinnedPaint.setColor(i10);
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
            float f13 = this.currentRevealBounceProgress;
            f = 1.0f;
            if (f13 != 0.0f && f13 != 1.0f) {
                float interpolation2 = this.interpolator.getInterpolation(f13) + 1.0f;
                canvas.scale(interpolation2, interpolation2, this.translationDrawable.getIntrinsicWidth() / 2, this.translationDrawable.getIntrinsicHeight() / 2);
            }
            BaseCell.setDrawableBounds((Drawable) this.translationDrawable, 0, 0);
            this.translationDrawable.draw(canvas);
            canvas.restore();
            canvas.clipRect(measuredWidth, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            int ceil = (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str));
            int i11 = i2;
            if (this.swipeMessageTextId != i11 || this.swipeMessageWidth != getMeasuredWidth()) {
                this.swipeMessageTextId = i11;
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
            float f14 = this.cornerProgress;
            if (f14 < f) {
                float f15 = f14 + (((float) j4) / 150.0f);
                this.cornerProgress = f15;
                if (f15 > f) {
                    this.cornerProgress = f;
                }
                z = true;
            } else {
                z = false;
            }
        } else {
            float f16 = this.cornerProgress;
            if (f16 > 0.0f) {
                float f17 = f16 - (((float) j4) / 150.0f);
                this.cornerProgress = f17;
                if (f17 < 0.0f) {
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
        float f18 = 10.0f;
        if (this.nameLayout != null) {
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
        if (this.messageLayout != null) {
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
                    str3 = "windowBackgroundWhite";
                    i7 = 1;
                    j = j4;
                    f11 = 1.0f;
                } catch (Exception e2) {
                    e = e2;
                    str3 = "windowBackgroundWhite";
                    j = j4;
                    f11 = 1.0f;
                    i7 = 1;
                }
                try {
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageLayout, this.animatedEmojiStack, -0.075f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f);
                    canvas.restore();
                    for (int i18 = 0; i18 < this.spoilers.size(); i18++) {
                        SpoilerEffect spoilerEffect = this.spoilers.get(i18);
                        spoilerEffect.setColor(this.messageLayout.getPaint().getColor());
                        spoilerEffect.draw(canvas);
                    }
                } catch (Exception e3) {
                    e = e3;
                    FileLog.e(e);
                    canvas.restore();
                    i8 = this.printingStringType;
                    if (i8 >= 0) {
                        canvas.save();
                        i9 = this.printingStringType;
                        i3 = 4;
                        if (i9 != i7) {
                        }
                        canvas.translate(this.statusDrawableLeft, this.messageTop + (i9 != i7 ? AndroidUtilities.dp(f11) : 0));
                        chatStatusDrawable.draw(canvas);
                        int i19 = this.statusDrawableLeft;
                        invalidate(i19, this.messageTop, chatStatusDrawable.getIntrinsicWidth() + i19, this.messageTop + chatStatusDrawable.getIntrinsicHeight());
                        canvas.restore();
                        if (this.currentDialogFolderId != 0) {
                        }
                        if (this.dialogsType == i4) {
                        }
                        if (!this.drawVerified) {
                        }
                        if (!this.drawReorder) {
                        }
                        Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                        BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                        Theme.dialogs_reorderDrawable.draw(canvas2);
                        if (this.drawError) {
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
                        z2 = this.drawReorder;
                        if (!z2) {
                        }
                        if (z2) {
                        }
                        z3 = true;
                        if (this.archiveHidden) {
                        }
                    }
                    i3 = 4;
                    if (this.currentDialogFolderId != 0) {
                    }
                    if (this.dialogsType == i4) {
                    }
                    if (!this.drawVerified) {
                    }
                    if (!this.drawReorder) {
                    }
                    Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                    BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                    Theme.dialogs_reorderDrawable.draw(canvas2);
                    if (this.drawError) {
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
                    z2 = this.drawReorder;
                    if (!z2) {
                    }
                    if (z2) {
                    }
                    z3 = true;
                    if (this.archiveHidden) {
                    }
                }
            } else {
                str3 = "windowBackgroundWhite";
                j = j4;
                f11 = 1.0f;
                i7 = 1;
                this.messageLayout.draw(canvas);
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageLayout, this.animatedEmojiStack, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
            }
            canvas.restore();
            i8 = this.printingStringType;
            if (i8 >= 0 && (chatStatusDrawable = Theme.getChatStatusDrawable(i8)) != null) {
                canvas.save();
                i9 = this.printingStringType;
                i3 = 4;
                if (i9 != i7 || i9 == 4) {
                    canvas.translate(this.statusDrawableLeft, this.messageTop + (i9 != i7 ? AndroidUtilities.dp(f11) : 0));
                } else {
                    canvas.translate(this.statusDrawableLeft, this.messageTop + ((AndroidUtilities.dp(18.0f) - chatStatusDrawable.getIntrinsicHeight()) / 2.0f));
                }
                chatStatusDrawable.draw(canvas);
                int i192 = this.statusDrawableLeft;
                invalidate(i192, this.messageTop, chatStatusDrawable.getIntrinsicWidth() + i192, this.messageTop + chatStatusDrawable.getIntrinsicHeight());
                canvas.restore();
                if (this.currentDialogFolderId != 0) {
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
                    boolean z8 = (i20 & i3) != 0;
                    if (z5) {
                        int i22 = this.animateFromStatusDrawableParams;
                        boolean z9 = (i22 & 1) != 0;
                        boolean z10 = (i22 & 2) != 0;
                        boolean z11 = (i22 & i3) != 0;
                        if (!z6 && !z9 && z11 && !z10 && z7 && z8) {
                            f4 = 1.0f;
                            i4 = 2;
                            str4 = str3;
                            drawCheckStatus(canvas, z6, z7, z8, true, this.statusDrawableProgress);
                            canvas2 = canvas;
                            f3 = 0.0f;
                        } else {
                            str4 = str3;
                            f4 = 1.0f;
                            i4 = 2;
                            boolean z12 = z9;
                            f3 = 0.0f;
                            boolean z13 = z10;
                            canvas2 = canvas;
                            drawCheckStatus(canvas, z12, z13, z11, false, 1.0f - this.statusDrawableProgress);
                            drawCheckStatus(canvas, z6, z7, z8, false, this.statusDrawableProgress);
                        }
                    } else {
                        canvas2 = canvas;
                        str4 = str3;
                        f3 = 0.0f;
                        f4 = 1.0f;
                        i4 = 2;
                        drawCheckStatus(canvas, z6, z7, z8, false, 1.0f);
                    }
                    this.lastStatusDrawableParams = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
                } else {
                    canvas2 = canvas;
                    str4 = str3;
                    f3 = 0.0f;
                    f4 = 1.0f;
                    i4 = 2;
                }
                if (this.dialogsType == i4 && (((z4 = this.dialogMuted) || this.dialogMutedProgress > f3) && !this.drawVerified && this.drawScam == 0 && !this.drawPremium)) {
                    if (z4) {
                        float f19 = this.dialogMutedProgress;
                        if (f19 != f4) {
                            float f20 = f19 + 0.10666667f;
                            this.dialogMutedProgress = f20;
                            if (f20 > f4) {
                                this.dialogMutedProgress = f4;
                            } else {
                                invalidate();
                            }
                            BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f), AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f));
                            if (this.dialogMutedProgress == f4) {
                                canvas.save();
                                float f21 = this.dialogMutedProgress;
                                canvas2.scale(f21, f21, Theme.dialogs_muteDrawable.getBounds().centerX(), Theme.dialogs_muteDrawable.getBounds().centerY());
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
                        float f22 = this.dialogMutedProgress;
                        if (f22 != f3) {
                            float f23 = f22 - 0.10666667f;
                            this.dialogMutedProgress = f23;
                            if (f23 < f3) {
                                this.dialogMutedProgress = f3;
                            } else {
                                invalidate();
                            }
                        }
                    }
                    BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f), AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f));
                    if (this.dialogMutedProgress == f4) {
                    }
                } else if (!this.drawVerified) {
                    BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft - AndroidUtilities.dp(f4), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
                    BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft - AndroidUtilities.dp(f4), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
                    Theme.dialogs_verifiedDrawable.draw(canvas2);
                    Theme.dialogs_verifiedCheckDrawable.draw(canvas2);
                } else if (this.drawPremium) {
                    Drawable drawable = PremiumGradient.getInstance().premiumStarDrawableMini;
                    BaseCell.setDrawableBounds(drawable, this.nameMuteLeft - AndroidUtilities.dp(f4), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f));
                    drawable.draw(canvas2);
                } else {
                    int i23 = this.drawScam;
                    if (i23 != 0) {
                        BaseCell.setDrawableBounds((Drawable) (i23 == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable), this.nameMuteLeft, AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.0f : 15.0f));
                        (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).draw(canvas2);
                    }
                    if (!this.drawReorder || this.reorderIconProgress != f3) {
                        Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                        BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                        Theme.dialogs_reorderDrawable.draw(canvas2);
                    }
                    if (this.drawError) {
                        Theme.dialogs_errorDrawable.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                        this.rect.set(this.errorLeft, this.errorTop, i6 + AndroidUtilities.dp(23.0f), this.errorTop + AndroidUtilities.dp(23.0f));
                        RectF rectF = this.rect;
                        float f24 = AndroidUtilities.density;
                        canvas2.drawRoundRect(rectF, f24 * 11.5f, f24 * 11.5f, Theme.dialogs_errorPaint);
                        BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
                        Theme.dialogs_errorDrawable.draw(canvas2);
                    } else {
                        boolean z14 = this.drawCount;
                        if (((z14 || this.drawMention) && this.drawCount2) || this.countChangeProgress != f4 || this.drawReactionMention || this.reactionsMentionsChangeProgress != f4) {
                            if ((z14 && this.drawCount2) || this.countChangeProgress != f4) {
                                int i24 = this.unreadCount;
                                float f25 = (i24 != 0 || this.markUnread) ? this.countChangeProgress : f4 - this.countChangeProgress;
                                StaticLayout staticLayout2 = this.countOldLayout;
                                if (staticLayout2 == null || i24 == 0) {
                                    if (i24 != 0) {
                                        staticLayout2 = this.countLayout;
                                    }
                                    Paint paint = (this.dialogMuted || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
                                    paint.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                    Theme.dialogs_countTextPaint.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                    this.rect.set(this.countLeft - AndroidUtilities.dp(5.5f), this.countTop, dp3 + this.countWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                    if (f25 != f4) {
                                        if (this.drawPin) {
                                            Theme.dialogs_pinnedDrawable.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                            BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                                            canvas.save();
                                            float f26 = f4 - f25;
                                            canvas2.scale(f26, f26, Theme.dialogs_pinnedDrawable.getBounds().centerX(), Theme.dialogs_pinnedDrawable.getBounds().centerY());
                                            Theme.dialogs_pinnedDrawable.draw(canvas2);
                                            canvas.restore();
                                        }
                                        canvas.save();
                                        canvas2.scale(f25, f25, this.rect.centerX(), this.rect.centerY());
                                    }
                                    RectF rectF2 = this.rect;
                                    float f27 = AndroidUtilities.density;
                                    canvas2.drawRoundRect(rectF2, f27 * 11.5f, f27 * 11.5f, paint);
                                    if (staticLayout2 != null) {
                                        canvas.save();
                                        canvas2.translate(this.countLeft, this.countTop + AndroidUtilities.dp(4.0f));
                                        staticLayout2.draw(canvas2);
                                        canvas.restore();
                                    }
                                    if (f25 != f4) {
                                        canvas.restore();
                                    }
                                } else {
                                    Paint paint2 = (this.dialogMuted || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
                                    paint2.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                    Theme.dialogs_countTextPaint.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                    float f28 = f25 * 2.0f;
                                    float f29 = f28 > f4 ? 1.0f : f28;
                                    float f30 = f4 - f29;
                                    float f31 = (this.countLeft * f29) + (this.countLeftOld * f30);
                                    float dp14 = f31 - AndroidUtilities.dp(5.5f);
                                    this.rect.set(dp14, this.countTop, (this.countWidth * f29) + dp14 + (this.countWidthOld * f30) + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                    if (f25 <= 0.5f) {
                                        f5 = 0.1f;
                                        interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(f28);
                                    } else {
                                        f5 = 0.1f;
                                        interpolation = CubicBezierInterpolator.EASE_IN.getInterpolation(f4 - ((f25 - 0.5f) * 2.0f));
                                    }
                                    float f32 = (interpolation * f5) + f4;
                                    canvas.save();
                                    canvas2.scale(f32, f32, this.rect.centerX(), this.rect.centerY());
                                    RectF rectF3 = this.rect;
                                    float f33 = AndroidUtilities.density;
                                    canvas2.drawRoundRect(rectF3, f33 * 11.5f, f33 * 11.5f, paint2);
                                    if (this.countAnimationStableLayout != null) {
                                        canvas.save();
                                        canvas2.translate(f31, this.countTop + AndroidUtilities.dp(4.0f));
                                        this.countAnimationStableLayout.draw(canvas2);
                                        canvas.restore();
                                    }
                                    int alpha = Theme.dialogs_countTextPaint.getAlpha();
                                    float f34 = alpha;
                                    Theme.dialogs_countTextPaint.setAlpha((int) (f34 * f29));
                                    if (this.countAnimationInLayout != null) {
                                        canvas.save();
                                        canvas2.translate(f31, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * f30) + this.countTop + AndroidUtilities.dp(4.0f));
                                        this.countAnimationInLayout.draw(canvas2);
                                        canvas.restore();
                                    } else if (this.countLayout != null) {
                                        canvas.save();
                                        canvas2.translate(f31, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * f30) + this.countTop + AndroidUtilities.dp(4.0f));
                                        this.countLayout.draw(canvas2);
                                        canvas.restore();
                                    }
                                    if (this.countOldLayout != null) {
                                        Theme.dialogs_countTextPaint.setAlpha((int) (f34 * f30));
                                        canvas.save();
                                        canvas2.translate(f31, ((this.countAnimationIncrement ? -AndroidUtilities.dp(13.0f) : AndroidUtilities.dp(13.0f)) * f29) + this.countTop + AndroidUtilities.dp(4.0f));
                                        this.countOldLayout.draw(canvas2);
                                        canvas.restore();
                                    }
                                    Theme.dialogs_countTextPaint.setAlpha(alpha);
                                    canvas.restore();
                                }
                            }
                            if (this.drawMention) {
                                Theme.dialogs_countPaint.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                this.rect.set(this.mentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp2 + this.mentionWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                Paint paint3 = (!this.dialogMuted || this.folderId == 0) ? Theme.dialogs_countPaint : Theme.dialogs_countGrayPaint;
                                RectF rectF4 = this.rect;
                                float f35 = AndroidUtilities.density;
                                canvas2.drawRoundRect(rectF4, f35 * 11.5f, f35 * 11.5f, paint3);
                                if (this.mentionLayout != null) {
                                    Theme.dialogs_countTextPaint.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                    canvas.save();
                                    canvas2.translate(this.mentionLeft, this.countTop + AndroidUtilities.dp(4.0f));
                                    this.mentionLayout.draw(canvas2);
                                    canvas.restore();
                                } else {
                                    Theme.dialogs_mentionDrawable.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                    BaseCell.setDrawableBounds(Theme.dialogs_mentionDrawable, this.mentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.2f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                    Theme.dialogs_mentionDrawable.draw(canvas2);
                                }
                            }
                            if (this.drawReactionMention || this.reactionsMentionsChangeProgress != f4) {
                                Theme.dialogs_reactionsCountPaint.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                Paint paint4 = Theme.dialogs_reactionsCountPaint;
                                canvas.save();
                                float f36 = this.reactionsMentionsChangeProgress;
                                if (f36 != f4) {
                                    if (!this.drawReactionMention) {
                                        f36 = f4 - f36;
                                    }
                                    canvas2.scale(f36, f36, this.rect.centerX(), this.rect.centerY());
                                }
                                RectF rectF5 = this.rect;
                                float f37 = AndroidUtilities.density;
                                canvas2.drawRoundRect(rectF5, f37 * 11.5f, f37 * 11.5f, paint4);
                                Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                                BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                Theme.dialogs_reactionsMentionDrawable.draw(canvas2);
                                canvas.restore();
                            }
                        } else if (this.drawPin) {
                            Theme.dialogs_pinnedDrawable.setAlpha((int) ((f4 - this.reorderIconProgress) * 255.0f));
                            BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                            Theme.dialogs_pinnedDrawable.draw(canvas2);
                        }
                    }
                    if (this.animatingArchiveAvatar) {
                        canvas.save();
                        float interpolation3 = this.interpolator.getInterpolation(this.animatingArchiveAvatarProgress / 170.0f) + f4;
                        canvas2.scale(interpolation3, interpolation3, this.avatarImage.getCenterX(), this.avatarImage.getCenterY());
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
                                    f12 = 6.0f;
                                }
                                int dp15 = (int) (imageY2 - AndroidUtilities.dp(f12));
                                if (LocaleController.isRTL) {
                                    float imageX = this.avatarImage.getImageX();
                                    if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                        f18 = 6.0f;
                                    }
                                    dp11 = imageX + AndroidUtilities.dp(f18);
                                } else {
                                    float imageX2 = this.avatarImage.getImageX2();
                                    if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                        f18 = 6.0f;
                                    }
                                    dp11 = imageX2 - AndroidUtilities.dp(f18);
                                }
                                Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(str4, this.resourcesProvider));
                                float f38 = (int) dp11;
                                float f39 = dp15;
                                canvas2.drawCircle(f38, f39, AndroidUtilities.dp(7.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                                Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle", this.resourcesProvider));
                                canvas2.drawCircle(f38, f39, AndroidUtilities.dp(5.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                                if (isOnline) {
                                    float f40 = this.onlineProgress;
                                    if (f40 < f4) {
                                        j2 = j;
                                        float f41 = f40 + (((float) j2) / 150.0f);
                                        this.onlineProgress = f41;
                                        if (f41 > f4) {
                                            this.onlineProgress = f4;
                                        }
                                    }
                                } else {
                                    j2 = j;
                                    float f42 = this.onlineProgress;
                                    if (f42 > 0.0f) {
                                        float f43 = f42 - (((float) j2) / 150.0f);
                                        this.onlineProgress = f43;
                                        if (f43 < 0.0f) {
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
                                    float progress = (checkBox2 == null || !checkBox2.isChecked()) ? 1.0f : f4 - this.checkBox.getProgress();
                                    float imageY22 = this.avatarImage.getImageY2();
                                    if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                        f12 = 6.0f;
                                    }
                                    int dp16 = (int) (imageY22 - AndroidUtilities.dp(f12));
                                    if (LocaleController.isRTL) {
                                        float imageX3 = this.avatarImage.getImageX();
                                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                            f18 = 6.0f;
                                        }
                                        dp4 = imageX3 + AndroidUtilities.dp(f18);
                                    } else {
                                        float imageX22 = this.avatarImage.getImageX2();
                                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                            f18 = 6.0f;
                                        }
                                        dp4 = imageX22 - AndroidUtilities.dp(f18);
                                    }
                                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(str4, this.resourcesProvider));
                                    float f44 = (int) dp4;
                                    float f45 = dp16;
                                    canvas2.drawCircle(f44, f45, AndroidUtilities.dp(11.0f) * this.chatCallProgress * progress, Theme.dialogs_onlineCirclePaint);
                                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle", this.resourcesProvider));
                                    canvas2.drawCircle(f44, f45, AndroidUtilities.dp(9.0f) * this.chatCallProgress * progress, Theme.dialogs_onlineCirclePaint);
                                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(str4, this.resourcesProvider));
                                    int i25 = this.progressStage;
                                    if (i25 == 0) {
                                        dp5 = AndroidUtilities.dp(f4) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                        dp9 = AndroidUtilities.dp(3.0f);
                                        dp10 = AndroidUtilities.dp(2.0f);
                                        f9 = this.innerProgress;
                                    } else {
                                        if (i25 == 1) {
                                            dp5 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                            dp6 = AndroidUtilities.dp(f4);
                                            dp7 = AndroidUtilities.dp(4.0f);
                                            f8 = this.innerProgress;
                                        } else if (i25 == 2) {
                                            dp5 = AndroidUtilities.dp(f4) + (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                            dp9 = AndroidUtilities.dp(5.0f);
                                            dp10 = AndroidUtilities.dp(4.0f);
                                            f9 = this.innerProgress;
                                        } else if (i25 == 3) {
                                            dp5 = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                            dp6 = AndroidUtilities.dp(f4);
                                            dp7 = AndroidUtilities.dp(2.0f);
                                            f8 = this.innerProgress;
                                        } else if (i25 == 4) {
                                            dp5 = AndroidUtilities.dp(f4) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                            dp9 = AndroidUtilities.dp(3.0f);
                                            dp10 = AndroidUtilities.dp(2.0f);
                                            f9 = this.innerProgress;
                                        } else if (i25 == 5) {
                                            dp5 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                            dp6 = AndroidUtilities.dp(f4);
                                            dp7 = AndroidUtilities.dp(4.0f);
                                            f8 = this.innerProgress;
                                        } else if (i25 == 6) {
                                            dp5 = AndroidUtilities.dp(f4) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                            dp8 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                            if (this.chatCallProgress >= f4 || progress < f4) {
                                                canvas.save();
                                                float f46 = this.chatCallProgress;
                                                canvas2.scale(f46 * progress, f46 * progress, f44, f45);
                                            }
                                            this.rect.set(i5 - AndroidUtilities.dp(f4), f45 - dp5, AndroidUtilities.dp(f4) + i5, dp5 + f45);
                                            canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                            float f47 = f45 - dp8;
                                            float f48 = f45 + dp8;
                                            this.rect.set(i5 - AndroidUtilities.dp(5.0f), f47, i5 - AndroidUtilities.dp(3.0f), f48);
                                            canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                            this.rect.set(AndroidUtilities.dp(3.0f) + i5, f47, i5 + AndroidUtilities.dp(5.0f), f48);
                                            canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                            if (this.chatCallProgress >= f4 || progress < f4) {
                                                canvas.restore();
                                            }
                                            float f49 = (float) j2;
                                            f10 = this.innerProgress + (f49 / 400.0f);
                                            this.innerProgress = f10;
                                            if (f10 >= f4) {
                                                this.innerProgress = 0.0f;
                                                int i26 = this.progressStage + 1;
                                                this.progressStage = i26;
                                                if (i26 >= 8) {
                                                    this.progressStage = 0;
                                                }
                                            }
                                            if (this.hasCall) {
                                                float f50 = this.chatCallProgress;
                                                if (f50 < f4) {
                                                    float f51 = f50 + (f49 / 150.0f);
                                                    this.chatCallProgress = f51;
                                                    if (f51 > f4) {
                                                        this.chatCallProgress = f4;
                                                    }
                                                }
                                                f6 = 0.0f;
                                            } else {
                                                float f52 = this.chatCallProgress;
                                                f6 = 0.0f;
                                                if (f52 > 0.0f) {
                                                    float f53 = f52 - (f49 / 150.0f);
                                                    this.chatCallProgress = f53;
                                                    if (f53 < 0.0f) {
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
                                                canvas2.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                                                this.archivedChatsDrawable.draw(canvas2);
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
                                                        if (z2) {
                                                            float f54 = this.reorderIconProgress;
                                                            if (f54 < f4) {
                                                                float f55 = f54 + (((float) j2) / 170.0f);
                                                                this.reorderIconProgress = f55;
                                                                if (f55 > f4) {
                                                                    this.reorderIconProgress = f4;
                                                                }
                                                                f7 = 0.0f;
                                                            }
                                                        } else {
                                                            float f56 = this.reorderIconProgress;
                                                            f7 = 0.0f;
                                                            if (f56 > 0.0f) {
                                                                float f57 = f56 - (((float) j2) / 170.0f);
                                                                this.reorderIconProgress = f57;
                                                                if (f57 < 0.0f) {
                                                                    this.reorderIconProgress = 0.0f;
                                                                }
                                                            }
                                                            z3 = z;
                                                            if (this.archiveHidden) {
                                                                float f58 = this.archiveBackgroundProgress;
                                                                if (f58 > f7) {
                                                                    float f59 = f58 - (((float) j2) / 230.0f);
                                                                    this.archiveBackgroundProgress = f59;
                                                                    if (f59 < f7) {
                                                                        this.archiveBackgroundProgress = f7;
                                                                    }
                                                                    if (this.avatarDrawable.getAvatarType() == 2) {
                                                                        this.avatarDrawable.setArchivedAvatarHiddenProgress(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.archiveBackgroundProgress));
                                                                    }
                                                                    z3 = true;
                                                                }
                                                                if (this.animatingArchiveAvatar) {
                                                                    float f60 = this.animatingArchiveAvatarProgress + ((float) j2);
                                                                    this.animatingArchiveAvatarProgress = f60;
                                                                    if (f60 >= 170.0f) {
                                                                        this.animatingArchiveAvatarProgress = 170.0f;
                                                                        this.animatingArchiveAvatar = false;
                                                                    }
                                                                    z3 = true;
                                                                }
                                                                if (!this.drawRevealBackground) {
                                                                    float f61 = this.currentRevealBounceProgress;
                                                                    if (f61 < f4) {
                                                                        float f62 = f61 + (((float) j2) / 170.0f);
                                                                        this.currentRevealBounceProgress = f62;
                                                                        if (f62 > f4) {
                                                                            this.currentRevealBounceProgress = f4;
                                                                            z3 = true;
                                                                        }
                                                                    }
                                                                    float f63 = this.currentRevealProgress;
                                                                    if (f63 < f4) {
                                                                        float f64 = f63 + (((float) j2) / 300.0f);
                                                                        this.currentRevealProgress = f64;
                                                                        if (f64 > f4) {
                                                                            this.currentRevealProgress = f4;
                                                                        }
                                                                        z3 = true;
                                                                    }
                                                                    if (z3) {
                                                                        return;
                                                                    }
                                                                    invalidate();
                                                                    return;
                                                                }
                                                                if (this.currentRevealBounceProgress == f4) {
                                                                    this.currentRevealBounceProgress = 0.0f;
                                                                    z3 = true;
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
                                                                if (z3) {
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
                                                                    z3 = true;
                                                                }
                                                                if (this.animatingArchiveAvatar) {
                                                                }
                                                                if (!this.drawRevealBackground) {
                                                                }
                                                            }
                                                        }
                                                        z3 = true;
                                                        if (this.archiveHidden) {
                                                        }
                                                    }
                                                    f7 = 0.0f;
                                                    z3 = z;
                                                    if (this.archiveHidden) {
                                                    }
                                                }
                                            }
                                            if (this.clipProgress != 0.0f) {
                                            }
                                            z2 = this.drawReorder;
                                            if (!z2) {
                                            }
                                            if (z2) {
                                            }
                                            z3 = true;
                                            if (this.archiveHidden) {
                                            }
                                        } else {
                                            dp5 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                            dp6 = AndroidUtilities.dp(f4);
                                            dp7 = AndroidUtilities.dp(2.0f);
                                            f8 = this.innerProgress;
                                        }
                                        dp8 = dp6 + (dp7 * f8);
                                        if (this.chatCallProgress >= f4) {
                                        }
                                        canvas.save();
                                        float f462 = this.chatCallProgress;
                                        canvas2.scale(f462 * progress, f462 * progress, f44, f45);
                                        this.rect.set(i5 - AndroidUtilities.dp(f4), f45 - dp5, AndroidUtilities.dp(f4) + i5, dp5 + f45);
                                        canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                        float f472 = f45 - dp8;
                                        float f482 = f45 + dp8;
                                        this.rect.set(i5 - AndroidUtilities.dp(5.0f), f472, i5 - AndroidUtilities.dp(3.0f), f482);
                                        canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                        this.rect.set(AndroidUtilities.dp(3.0f) + i5, f472, i5 + AndroidUtilities.dp(5.0f), f482);
                                        canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                        if (this.chatCallProgress >= f4) {
                                        }
                                        canvas.restore();
                                        float f492 = (float) j2;
                                        f10 = this.innerProgress + (f492 / 400.0f);
                                        this.innerProgress = f10;
                                        if (f10 >= f4) {
                                        }
                                        if (this.hasCall) {
                                        }
                                        z = true;
                                        if (this.translationX != f6) {
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
                                        if (z2) {
                                        }
                                        z3 = true;
                                        if (this.archiveHidden) {
                                        }
                                    }
                                    dp8 = dp9 - (dp10 * f9);
                                    if (this.chatCallProgress >= f4) {
                                    }
                                    canvas.save();
                                    float f4622 = this.chatCallProgress;
                                    canvas2.scale(f4622 * progress, f4622 * progress, f44, f45);
                                    this.rect.set(i5 - AndroidUtilities.dp(f4), f45 - dp5, AndroidUtilities.dp(f4) + i5, dp5 + f45);
                                    canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                    float f4722 = f45 - dp8;
                                    float f4822 = f45 + dp8;
                                    this.rect.set(i5 - AndroidUtilities.dp(5.0f), f4722, i5 - AndroidUtilities.dp(3.0f), f4822);
                                    canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                    this.rect.set(AndroidUtilities.dp(3.0f) + i5, f4722, i5 + AndroidUtilities.dp(5.0f), f4822);
                                    canvas2.drawRoundRect(this.rect, AndroidUtilities.dp(f4), AndroidUtilities.dp(f4), Theme.dialogs_onlineCirclePaint);
                                    if (this.chatCallProgress >= f4) {
                                    }
                                    canvas.restore();
                                    float f4922 = (float) j2;
                                    f10 = this.innerProgress + (f4922 / 400.0f);
                                    this.innerProgress = f10;
                                    if (f10 >= f4) {
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
                                    z2 = this.drawReorder;
                                    if (!z2) {
                                    }
                                    if (z2) {
                                    }
                                    z3 = true;
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
                        z2 = this.drawReorder;
                        if (!z2) {
                        }
                        if (z2) {
                        }
                        z3 = true;
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
                    z2 = this.drawReorder;
                    if (!z2) {
                    }
                    if (z2) {
                    }
                    z3 = true;
                    if (this.archiveHidden) {
                    }
                }
                if (!this.drawReorder) {
                }
                Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                Theme.dialogs_reorderDrawable.draw(canvas2);
                if (this.drawError) {
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
                    f6 = 0.0f;
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
                    if (z2) {
                    }
                    z3 = true;
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
                z2 = this.drawReorder;
                if (!z2) {
                }
                if (z2) {
                }
                z3 = true;
                if (this.archiveHidden) {
                }
            }
        } else {
            str3 = "windowBackgroundWhite";
            j = j4;
        }
        i3 = 4;
        if (this.currentDialogFolderId != 0) {
        }
        if (this.dialogsType == i4) {
        }
        if (!this.drawVerified) {
        }
        if (!this.drawReorder) {
        }
        Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
        BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
        Theme.dialogs_reorderDrawable.draw(canvas2);
        if (this.drawError) {
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
        z2 = this.drawReorder;
        if (!z2) {
        }
        if (z2) {
        }
        z3 = true;
        if (this.archiveHidden) {
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
