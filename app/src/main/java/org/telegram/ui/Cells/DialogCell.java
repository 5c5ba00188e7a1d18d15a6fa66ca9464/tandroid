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
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.avatarImage.onAttachedToWindow();
        this.thumbImage.onAttachedToWindow();
        resetPinnedArchiveState();
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

    /* JADX WARN: Can't wrap try/catch for region: R(17:439|(2:441|(1:443)(1:444))(2:445|(2:447|(1:449)(1:450))(2:451|(1:453)(14:455|(2:457|(1:459)(1:460))(1:461)|462|1125|463|(1:465)(1:466)|467|492|(1:508)(6:1121|500|501|1123|502|503)|509|(1:513)|514|(4:516|(1:518)|519|(1:521)(1:522))|523)))|454|462|1125|463|(0)(0)|467|492|(1:494)|496|508|509|(2:511|513)|514|(0)|523) */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x0472, code lost:
        if (r2.post_messages == false) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x047e, code lost:
        if (r2.kicked != false) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:468:0x0ac7, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0ac8, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:614:0x0e5c, code lost:
        if (r3 != null) goto L616;
     */
    /* JADX WARN: Removed duplicated region for block: B:1000:0x17c4  */
    /* JADX WARN: Removed duplicated region for block: B:1001:0x17c7  */
    /* JADX WARN: Removed duplicated region for block: B:1008:0x17fa  */
    /* JADX WARN: Removed duplicated region for block: B:1059:0x1961  */
    /* JADX WARN: Removed duplicated region for block: B:1092:0x19f3  */
    /* JADX WARN: Removed duplicated region for block: B:1096:0x1a04 A[Catch: Exception -> 0x1a2f, TryCatch #0 {Exception -> 0x1a2f, blocks: (B:1094:0x19f7, B:1096:0x1a04, B:1097:0x1a06, B:1099:0x1a1f, B:1100:0x1a26), top: B:1117:0x19f7 }] */
    /* JADX WARN: Removed duplicated region for block: B:1099:0x1a1f A[Catch: Exception -> 0x1a2f, TryCatch #0 {Exception -> 0x1a2f, blocks: (B:1094:0x19f7, B:1096:0x1a04, B:1097:0x1a06, B:1099:0x1a1f, B:1100:0x1a26), top: B:1117:0x19f7 }] */
    /* JADX WARN: Removed duplicated region for block: B:1105:0x1a37  */
    /* JADX WARN: Removed duplicated region for block: B:1140:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0423  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x042f  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0484  */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0489  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x050e  */
    /* JADX WARN: Removed duplicated region for block: B:465:0x0ab4 A[Catch: Exception -> 0x0ac7, TryCatch #4 {Exception -> 0x0ac7, blocks: (B:463:0x0aa9, B:465:0x0ab4, B:467:0x0abc), top: B:1125:0x0aa9 }] */
    /* JADX WARN: Removed duplicated region for block: B:466:0x0abb  */
    /* JADX WARN: Removed duplicated region for block: B:494:0x0b58  */
    /* JADX WARN: Removed duplicated region for block: B:511:0x0ba3  */
    /* JADX WARN: Removed duplicated region for block: B:516:0x0bb4  */
    /* JADX WARN: Removed duplicated region for block: B:527:0x0c11  */
    /* JADX WARN: Removed duplicated region for block: B:594:0x0db5  */
    /* JADX WARN: Removed duplicated region for block: B:599:0x0dc5  */
    /* JADX WARN: Removed duplicated region for block: B:617:0x0e65  */
    /* JADX WARN: Removed duplicated region for block: B:620:0x0e6f  */
    /* JADX WARN: Removed duplicated region for block: B:624:0x0e7d  */
    /* JADX WARN: Removed duplicated region for block: B:625:0x0e85  */
    /* JADX WARN: Removed duplicated region for block: B:634:0x0ea2  */
    /* JADX WARN: Removed duplicated region for block: B:635:0x0eb6  */
    /* JADX WARN: Removed duplicated region for block: B:698:0x0fd8  */
    /* JADX WARN: Removed duplicated region for block: B:712:0x1037  */
    /* JADX WARN: Removed duplicated region for block: B:716:0x1040  */
    /* JADX WARN: Removed duplicated region for block: B:718:0x1050  */
    /* JADX WARN: Removed duplicated region for block: B:740:0x10a2  */
    /* JADX WARN: Removed duplicated region for block: B:742:0x10ae  */
    /* JADX WARN: Removed duplicated region for block: B:746:0x10ed  */
    /* JADX WARN: Removed duplicated region for block: B:749:0x10f8  */
    /* JADX WARN: Removed duplicated region for block: B:750:0x1108  */
    /* JADX WARN: Removed duplicated region for block: B:753:0x1120  */
    /* JADX WARN: Removed duplicated region for block: B:756:0x1134  */
    /* JADX WARN: Removed duplicated region for block: B:761:0x1162  */
    /* JADX WARN: Removed duplicated region for block: B:784:0x1205  */
    /* JADX WARN: Removed duplicated region for block: B:787:0x121b  */
    /* JADX WARN: Removed duplicated region for block: B:804:0x1264  */
    /* JADX WARN: Removed duplicated region for block: B:827:0x13cd  */
    /* JADX WARN: Removed duplicated region for block: B:828:0x13eb  */
    /* JADX WARN: Removed duplicated region for block: B:832:0x1438  */
    /* JADX WARN: Removed duplicated region for block: B:838:0x145d  */
    /* JADX WARN: Removed duplicated region for block: B:843:0x148d  */
    /* JADX WARN: Removed duplicated region for block: B:913:0x162d  */
    /* JADX WARN: Removed duplicated region for block: B:973:0x173d A[Catch: Exception -> 0x17ef, TryCatch #1 {Exception -> 0x17ef, blocks: (B:957:0x1708, B:959:0x170c, B:961:0x1710, B:963:0x1714, B:965:0x1719, B:967:0x1724, B:970:0x172a, B:971:0x1739, B:973:0x173d, B:975:0x1751, B:977:0x1757, B:979:0x175b, B:981:0x1765, B:982:0x1768, B:983:0x176b, B:985:0x176f, B:988:0x1774, B:990:0x1778, B:992:0x1784, B:993:0x178e, B:994:0x17a6, B:997:0x17ac, B:998:0x17b3, B:1002:0x17c9, B:1003:0x17d9), top: B:1119:0x1708 }] */
    /* JADX WARN: Removed duplicated region for block: B:990:0x1778 A[Catch: Exception -> 0x17ef, TryCatch #1 {Exception -> 0x17ef, blocks: (B:957:0x1708, B:959:0x170c, B:961:0x1710, B:963:0x1714, B:965:0x1719, B:967:0x1724, B:970:0x172a, B:971:0x1739, B:973:0x173d, B:975:0x1751, B:977:0x1757, B:979:0x175b, B:981:0x1765, B:982:0x1768, B:983:0x176b, B:985:0x176f, B:988:0x1774, B:990:0x1778, B:992:0x1784, B:993:0x178e, B:994:0x17a6, B:997:0x17ac, B:998:0x17b3, B:1002:0x17c9, B:1003:0x17d9), top: B:1119:0x1708 }] */
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
        String str20;
        SpannableStringBuilder spannableStringBuilder4;
        int i7;
        Exception e;
        int i8;
        ForegroundColorSpanThemable foregroundColorSpanThemable;
        SpannableStringBuilder replaceEmoji;
        CharSequence highlightText;
        SpannableStringBuilder valueOf;
        char c;
        char c2;
        String str21;
        String str22;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        String str23;
        String str24;
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
                                                                                        valueOf = AndroidUtilities.formatSpannable(str28, new SpannableStringBuilder(str22).append(AndroidUtilities.replaceNewLines(spannableStringBuilder11)), str20);
                                                                                    }
                                                                                } else if (messageObject10.messageOwner.media != null && !messageObject10.isMediaEmpty()) {
                                                                                    textPaint5 = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                    MessageObject messageObject11 = this.message;
                                                                                    TLRPC$MessageMedia tLRPC$MessageMedia = messageObject11.messageOwner.media;
                                                                                    if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                                                                                        TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia;
                                                                                        str21 = Build.VERSION.SDK_INT >= 18 ? String.format("📊 \u2068%s\u2069", tLRPC$TL_messageMediaPoll.poll.question) : String.format("📊 %s", tLRPC$TL_messageMediaPoll.poll.question);
                                                                                    } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                                                                                        str21 = Build.VERSION.SDK_INT >= 18 ? String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title) : String.format("🎮 %s", tLRPC$MessageMedia.game.title);
                                                                                    } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                        str21 = tLRPC$MessageMedia.title;
                                                                                    } else {
                                                                                        if (messageObject11.type == 14) {
                                                                                            if (Build.VERSION.SDK_INT >= 18) {
                                                                                                c2 = 1;
                                                                                                str21 = String.format("🎧 \u2068%s - %s\u2069", messageObject11.getMusicAuthor(), this.message.getMusicTitle());
                                                                                            } else {
                                                                                                c2 = 1;
                                                                                                str21 = String.format("🎧 %s - %s", messageObject11.getMusicAuthor(), this.message.getMusicTitle());
                                                                                            }
                                                                                        } else {
                                                                                            c2 = 1;
                                                                                            str21 = spannableStringBuilder7.toString();
                                                                                        }
                                                                                        CharSequence[] charSequenceArr = new CharSequence[2];
                                                                                        charSequenceArr[0] = str21.replace('\n', ' ');
                                                                                        charSequenceArr[c2] = str20;
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
                                                                                                MessageObject messageObject12 = this.message;
                                                                                                this.nameLayout = new StaticLayout((messageObject12 != null || !messageObject12.hasHighlightedWords() || (highlightText4 = AndroidUtilities.highlightText(replaceEmoji2, this.message.highlightedWords, this.resourcesProvider)) == null) ? replaceEmoji2 : highlightText4, Theme.dialogs_namePaint[this.paintIndex], i10, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
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
                                                                                    c2 = 1;
                                                                                    CharSequence[] charSequenceArr2 = new CharSequence[2];
                                                                                    charSequenceArr2[0] = str21.replace('\n', ' ');
                                                                                    charSequenceArr2[c2] = str20;
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
                                                                                    MessageObject messageObject13 = this.message;
                                                                                    CharSequence charSequence13 = messageObject13.messageOwner.message;
                                                                                    if (charSequence13 != null) {
                                                                                        if (messageObject13.hasHighlightedWords()) {
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
                                                                                                c = 0;
                                                                                                charSequence13 = AndroidUtilities.ellipsizeCenterEnd(charSequence13, this.message.highlightedWords.get(0), measuredWidth3, textPaint5, 130).toString();
                                                                                            } else {
                                                                                                c = 0;
                                                                                            }
                                                                                        } else {
                                                                                            c = 0;
                                                                                            if (charSequence13.length() > 150) {
                                                                                                charSequence13 = charSequence13.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                            }
                                                                                            charSequence13 = AndroidUtilities.replaceNewLines(charSequence13);
                                                                                        }
                                                                                        SpannableStringBuilder spannableStringBuilder13 = new SpannableStringBuilder(charSequence13);
                                                                                        MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder13, 256);
                                                                                        CharSequence[] charSequenceArr3 = new CharSequence[2];
                                                                                        charSequenceArr3[c] = spannableStringBuilder13;
                                                                                        charSequenceArr3[1] = str20;
                                                                                        valueOf = AndroidUtilities.formatSpannable(str28, charSequenceArr3);
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
                                                                                MessageObject messageObject14 = this.message;
                                                                                TLRPC$MessageMedia tLRPC$MessageMedia2 = messageObject14.messageOwner.media;
                                                                                if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia2.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                                                                                    str32 = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
                                                                                } else if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaDocument) && (tLRPC$MessageMedia2.document instanceof TLRPC$TL_documentEmpty) && tLRPC$MessageMedia2.ttl_seconds != 0) {
                                                                                    str32 = LocaleController.getString("AttachVideoExpired", R.string.AttachVideoExpired);
                                                                                } else if (messageObject14.caption != null) {
                                                                                    if (!z6) {
                                                                                        str19 = "";
                                                                                    } else if (messageObject14.isVideo()) {
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
                                                                                        MessageObject messageObject15 = this.message;
                                                                                        MediaDataController.addTextStyleRuns(messageObject15.messageOwner.entities, messageObject15.caption, spannableStringBuilder14, 256);
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
                                                                                        if (messageObject14.type == 14) {
                                                                                            str16 = String.format("🎧 %s - %s", messageObject14.getMusicAuthor(), this.message.getMusicTitle());
                                                                                        } else {
                                                                                            if (messageObject14.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                str17 = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp(95.0f), textPaint5, 130).toString();
                                                                                            } else {
                                                                                                SpannableStringBuilder spannableStringBuilder15 = new SpannableStringBuilder(spannableStringBuilder7);
                                                                                                MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder15, 256);
                                                                                                str17 = spannableStringBuilder15;
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
                                        MessageObject messageObject16 = this.message;
                                        if (!(messageObject16.messageOwner.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                                            if (messageObject16.isSending()) {
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
            MessageObject messageObject122 = this.message;
            this.nameLayout = new StaticLayout((messageObject122 != null || !messageObject122.hasHighlightedWords() || (highlightText4 = AndroidUtilities.highlightText(replaceEmoji22, this.message.highlightedWords, this.resourcesProvider)) == null) ? replaceEmoji22 : highlightText4, Theme.dialogs_namePaint[this.paintIndex], i10, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
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
            MessageObject messageObject17 = this.message;
            if (messageObject17 != null && (highlightText3 = AndroidUtilities.highlightText(str9, messageObject17.highlightedWords, this.resourcesProvider)) != null) {
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
                    int i46 = ConnectionsManager.DEFAULT_DATACENTER_ID;
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

    /* JADX WARN: Code restructure failed: missing block: B:201:0x0636, code lost:
        if (r0.type != 2) goto L203;
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0e0a, code lost:
        if (r30.reactionsMentionsChangeProgress != 1.0f) goto L525;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:364:0x0927  */
    /* JADX WARN: Removed duplicated region for block: B:365:0x092a  */
    /* JADX WARN: Removed duplicated region for block: B:368:0x0939  */
    /* JADX WARN: Removed duplicated region for block: B:369:0x0972  */
    /* JADX WARN: Removed duplicated region for block: B:510:0x0d4e  */
    /* JADX WARN: Removed duplicated region for block: B:522:0x0e04  */
    /* JADX WARN: Removed duplicated region for block: B:524:0x0e0d  */
    /* JADX WARN: Removed duplicated region for block: B:527:0x0e4a  */
    /* JADX WARN: Removed duplicated region for block: B:665:0x11f7  */
    /* JADX WARN: Removed duplicated region for block: B:670:0x128a  */
    /* JADX WARN: Removed duplicated region for block: B:675:0x12a1  */
    /* JADX WARN: Removed duplicated region for block: B:680:0x12b5  */
    /* JADX WARN: Removed duplicated region for block: B:686:0x12ca  */
    /* JADX WARN: Removed duplicated region for block: B:694:0x12e4  */
    /* JADX WARN: Removed duplicated region for block: B:697:0x12eb  */
    /* JADX WARN: Removed duplicated region for block: B:704:0x1310  */
    /* JADX WARN: Removed duplicated region for block: B:725:0x1375  */
    /* JADX WARN: Removed duplicated region for block: B:731:0x13c2  */
    /* JADX WARN: Removed duplicated region for block: B:735:0x13ce  */
    /* JADX WARN: Removed duplicated region for block: B:741:0x13e3  */
    /* JADX WARN: Removed duplicated region for block: B:750:0x13fd  */
    /* JADX WARN: Removed duplicated region for block: B:758:0x1427  */
    /* JADX WARN: Removed duplicated region for block: B:769:0x1457  */
    /* JADX WARN: Removed duplicated region for block: B:775:0x146b  */
    /* JADX WARN: Removed duplicated region for block: B:785:0x1493  */
    /* JADX WARN: Removed duplicated region for block: B:795:0x14b5  */
    /* JADX WARN: Removed duplicated region for block: B:803:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    @SuppressLint({"DrawAllocation"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        int i;
        float f;
        boolean z;
        float f2;
        int i2;
        float f3;
        boolean z2;
        boolean z3;
        float f4;
        float f5;
        int i3;
        float f6;
        float f7;
        float f8;
        float f9;
        float dp;
        float dp2;
        float f10;
        float dp3;
        float dp4;
        float f11;
        boolean z4;
        float f12;
        PullForegroundDrawable pullForegroundDrawable;
        float f13;
        int dp5;
        float f14;
        int dp6;
        int dp7;
        float f15;
        int i4;
        boolean z5;
        boolean z6;
        int i5;
        boolean z7;
        int i6;
        StatusDrawable chatStatusDrawable;
        String str;
        int i7;
        int i8;
        int i9;
        String str2;
        String str3;
        int i10;
        boolean z8;
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
        long j = elapsedRealtime - this.lastUpdateTime;
        if (j > 17) {
            j = 17;
        }
        long j2 = j;
        this.lastUpdateTime = elapsedRealtime;
        if (this.clipProgress != 0.0f && Build.VERSION.SDK_INT != 24) {
            canvas.save();
            canvas.clipRect(0.0f, this.topClip * this.clipProgress, getMeasuredWidth(), getMeasuredHeight() - ((int) (this.bottomClip * this.clipProgress)));
        }
        if (this.translationX != 0.0f || this.cornerProgress != 0.0f) {
            canvas.save();
            if (this.currentDialogFolderId != 0) {
                if (this.archiveHidden) {
                    i9 = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
                    i8 = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                    str = LocaleController.getString("UnhideFromTop", R.string.UnhideFromTop);
                    this.translationDrawable = Theme.dialogs_unpinArchiveDrawable;
                    i7 = R.string.UnhideFromTop;
                } else {
                    i9 = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                    i8 = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
                    str = LocaleController.getString("HideOnTop", R.string.HideOnTop);
                    this.translationDrawable = Theme.dialogs_pinArchiveDrawable;
                    i7 = R.string.HideOnTop;
                }
            } else if (this.promoDialog) {
                i9 = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                i8 = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
                str = LocaleController.getString("PsaHide", R.string.PsaHide);
                this.translationDrawable = Theme.dialogs_hidePsaDrawable;
                i7 = R.string.PsaHide;
            } else if (this.folderId == 0) {
                i9 = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                i8 = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
                if (SharedConfig.getChatSwipeAction(this.currentAccount) == 3) {
                    if (this.dialogMuted) {
                        str = LocaleController.getString("SwipeUnmute", R.string.SwipeUnmute);
                        this.translationDrawable = Theme.dialogs_swipeUnmuteDrawable;
                        i7 = R.string.SwipeUnmute;
                    } else {
                        str = LocaleController.getString("SwipeMute", R.string.SwipeMute);
                        this.translationDrawable = Theme.dialogs_swipeMuteDrawable;
                        i7 = R.string.SwipeMute;
                    }
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 4) {
                    str = LocaleController.getString("SwipeDeleteChat", R.string.SwipeDeleteChat);
                    i9 = Theme.getColor("dialogSwipeRemove", this.resourcesProvider);
                    this.translationDrawable = Theme.dialogs_swipeDeleteDrawable;
                    i7 = R.string.SwipeDeleteChat;
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 1) {
                    if (this.unreadCount > 0 || this.markUnread) {
                        str = LocaleController.getString("SwipeMarkAsRead", R.string.SwipeMarkAsRead);
                        this.translationDrawable = Theme.dialogs_swipeReadDrawable;
                        i7 = R.string.SwipeMarkAsRead;
                    } else {
                        str = LocaleController.getString("SwipeMarkAsUnread", R.string.SwipeMarkAsUnread);
                        this.translationDrawable = Theme.dialogs_swipeUnreadDrawable;
                        i7 = R.string.SwipeMarkAsUnread;
                    }
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 0) {
                    if (this.drawPin) {
                        str = LocaleController.getString("SwipeUnpin", R.string.SwipeUnpin);
                        this.translationDrawable = Theme.dialogs_swipeUnpinDrawable;
                        i7 = R.string.SwipeUnpin;
                    } else {
                        str = LocaleController.getString("SwipePin", R.string.SwipePin);
                        this.translationDrawable = Theme.dialogs_swipePinDrawable;
                        i7 = R.string.SwipePin;
                    }
                } else {
                    str = LocaleController.getString("Archive", R.string.Archive);
                    this.translationDrawable = Theme.dialogs_archiveDrawable;
                    i7 = R.string.Archive;
                }
            } else {
                i9 = Theme.getColor("chats_archivePinBackground", this.resourcesProvider);
                i8 = Theme.getColor("chats_archiveBackground", this.resourcesProvider);
                str = LocaleController.getString("Unarchive", R.string.Unarchive);
                this.translationDrawable = Theme.dialogs_unarchiveDrawable;
                i7 = R.string.Unarchive;
            }
            String str4 = str;
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
                str2 = str4;
                str3 = "chats_archivePinBackground";
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
                str2 = str4;
                str3 = "chats_archivePinBackground";
            }
            int measuredWidth2 = (getMeasuredWidth() - AndroidUtilities.dp(43.0f)) - (this.translationDrawable.getIntrinsicWidth() / 2);
            int dp8 = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.0f : 9.0f);
            int intrinsicWidth = (this.translationDrawable.getIntrinsicWidth() / 2) + measuredWidth2;
            int intrinsicHeight = (this.translationDrawable.getIntrinsicHeight() / 2) + dp8;
            if (this.currentRevealProgress > 0.0f) {
                canvas.save();
                canvas.clipRect(measuredWidth - AndroidUtilities.dp(8.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                Theme.dialogs_pinnedPaint.setColor(i11);
                canvas.drawCircle(intrinsicWidth, intrinsicHeight, ((float) Math.sqrt((intrinsicWidth * intrinsicWidth) + ((intrinsicHeight - getMeasuredHeight()) * (intrinsicHeight - getMeasuredHeight())))) * AndroidUtilities.accelerateInterpolator.getInterpolation(this.currentRevealProgress), Theme.dialogs_pinnedPaint);
                canvas.restore();
                if (!Theme.dialogs_archiveDrawableRecolored) {
                    Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor(str3));
                    z8 = 1;
                    Theme.dialogs_archiveDrawableRecolored = true;
                } else {
                    z8 = 1;
                }
                i10 = z8;
                if (!Theme.dialogs_hidePsaDrawableRecolored) {
                    Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(str3));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(str3));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(str3));
                    Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                    Theme.dialogs_hidePsaDrawableRecolored = z8;
                    i10 = z8;
                }
            } else {
                i10 = 1;
            }
            canvas.save();
            canvas.translate(measuredWidth2, dp8);
            float f16 = this.currentRevealBounceProgress;
            f = 1.0f;
            if (f16 != 0.0f && f16 != 1.0f) {
                float interpolation = this.interpolator.getInterpolation(f16) + 1.0f;
                canvas.scale(interpolation, interpolation, this.translationDrawable.getIntrinsicWidth() / 2, this.translationDrawable.getIntrinsicHeight() / 2);
            }
            BaseCell.setDrawableBounds((Drawable) this.translationDrawable, 0, 0);
            this.translationDrawable.draw(canvas);
            canvas.restore();
            canvas.clipRect(measuredWidth, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            String str5 = str2;
            int ceil = (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str5));
            if (this.swipeMessageTextId != i12 || this.swipeMessageWidth != getMeasuredWidth()) {
                this.swipeMessageTextId = i12;
                this.swipeMessageWidth = getMeasuredWidth();
                StaticLayout staticLayout = new StaticLayout(str5, Theme.dialogs_archiveTextPaint, Math.min(AndroidUtilities.dp(80.0f), ceil), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                this.swipeMessageTextLayout = staticLayout;
                if (staticLayout.getLineCount() > i10) {
                    this.swipeMessageTextLayout = new StaticLayout(str5, Theme.dialogs_archiveTextPaintSmall, Math.min(AndroidUtilities.dp(82.0f), ceil), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                }
            }
            if (this.swipeMessageTextLayout != null) {
                canvas.save();
                canvas.translate((getMeasuredWidth() - AndroidUtilities.dp(43.0f)) - (this.swipeMessageTextLayout.getWidth() / 2.0f), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 50.0f : 47.0f) + (this.swipeMessageTextLayout.getLineCount() > i10 ? -AndroidUtilities.dp(4.0f) : 0.0f));
                this.swipeMessageTextLayout.draw(canvas);
                canvas.restore();
            }
            canvas.restore();
            i = i10;
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
            i = 1;
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
            float f17 = this.cornerProgress;
            if (f17 < f) {
                float f18 = f17 + (((float) j2) / 150.0f);
                this.cornerProgress = f18;
                if (f18 > f) {
                    this.cornerProgress = f;
                }
                z = true;
            }
            z = false;
        } else {
            float f19 = this.cornerProgress;
            if (f19 > 0.0f) {
                float f20 = f19 - (((float) j2) / 150.0f);
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
        float f21 = 10.0f;
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
            } catch (Exception e) {
                FileLog.e(e);
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
                    canvas.restore();
                    for (int i19 = 0; i19 < this.spoilers.size(); i19++) {
                        SpoilerEffect spoilerEffect = this.spoilers.get(i19);
                        spoilerEffect.setColor(this.messageLayout.getPaint().getColor());
                        spoilerEffect.draw(canvas);
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            } else {
                this.messageLayout.draw(canvas);
            }
            canvas.restore();
            int i20 = this.printingStringType;
            if (i20 >= 0 && (chatStatusDrawable = Theme.getChatStatusDrawable(i20)) != null) {
                canvas.save();
                int i21 = this.printingStringType;
                if (i21 == i || i21 == 4) {
                    canvas.translate(this.statusDrawableLeft, this.messageTop + (i21 == i ? AndroidUtilities.dp(f) : 0));
                } else {
                    canvas.translate(this.statusDrawableLeft, this.messageTop + ((AndroidUtilities.dp(18.0f) - chatStatusDrawable.getIntrinsicHeight()) / 2.0f));
                }
                chatStatusDrawable.draw(canvas);
                int i22 = this.statusDrawableLeft;
                invalidate(i22, this.messageTop, chatStatusDrawable.getIntrinsicWidth() + i22, this.messageTop + chatStatusDrawable.getIntrinsicHeight());
                canvas.restore();
            }
        }
        if (this.currentDialogFolderId == 0) {
            int i23 = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
            int i24 = this.lastStatusDrawableParams;
            if (i24 >= 0 && i24 != i23 && !this.statusDrawableAnimationInProgress) {
                createStatusDrawableAnimator(i24, i23);
            }
            boolean z9 = this.statusDrawableAnimationInProgress;
            if (z9) {
                i23 = this.animateToStatusDrawableParams;
            }
            boolean z10 = (i23 & 1) != 0;
            if ((i23 & 2) != 0) {
                i5 = 4;
                z6 = true;
            } else {
                i5 = 4;
                z6 = false;
            }
            boolean z11 = (i23 & i5) != 0;
            if (z9) {
                int i25 = this.animateFromStatusDrawableParams;
                boolean z12 = (i25 & 1) != 0;
                if ((i25 & 2) != 0) {
                    i6 = 4;
                    z7 = true;
                } else {
                    i6 = 4;
                    z7 = false;
                }
                boolean z13 = (i25 & i6) != 0;
                if (!z10 && !z12 && z13 && !z7 && z6 && z11) {
                    i2 = 2;
                    f2 = 1.0f;
                    drawCheckStatus(canvas, z10, z6, z11, true, this.statusDrawableProgress);
                } else {
                    i2 = 2;
                    f2 = 1.0f;
                    drawCheckStatus(canvas, z12, z7, z13, false, 1.0f - this.statusDrawableProgress);
                    drawCheckStatus(canvas, z10, z6, z11, false, this.statusDrawableProgress);
                }
            } else {
                i2 = 2;
                f2 = 1.0f;
                drawCheckStatus(canvas, z10, z6, z11, false, 1.0f);
            }
            this.lastStatusDrawableParams = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
        } else {
            i2 = 2;
            f2 = 1.0f;
        }
        if (this.dialogsType != i2 && (((z5 = this.dialogMuted) || this.dialogMutedProgress > 0.0f) && !this.drawVerified && this.drawScam == 0 && !this.drawPremium)) {
            if (z5) {
                float f22 = this.dialogMutedProgress;
                if (f22 != f2) {
                    float f23 = f22 + 0.10666667f;
                    this.dialogMutedProgress = f23;
                    if (f23 > f2) {
                        this.dialogMutedProgress = f2;
                    } else {
                        invalidate();
                    }
                    BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f), AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f));
                    if (this.dialogMutedProgress == f2) {
                        canvas.save();
                        float f24 = this.dialogMutedProgress;
                        canvas.scale(f24, f24, Theme.dialogs_muteDrawable.getBounds().centerX(), Theme.dialogs_muteDrawable.getBounds().centerY());
                        Theme.dialogs_muteDrawable.setAlpha((int) (this.dialogMutedProgress * 255.0f));
                        Theme.dialogs_muteDrawable.draw(canvas);
                        Theme.dialogs_muteDrawable.setAlpha(255);
                        canvas.restore();
                    } else {
                        Theme.dialogs_muteDrawable.draw(canvas);
                    }
                }
            }
            if (!z5) {
                float f25 = this.dialogMutedProgress;
                if (f25 != 0.0f) {
                    float f26 = f25 - 0.10666667f;
                    this.dialogMutedProgress = f26;
                    if (f26 < 0.0f) {
                        this.dialogMutedProgress = 0.0f;
                    } else {
                        invalidate();
                    }
                }
            }
            BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f), AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f));
            if (this.dialogMutedProgress == f2) {
            }
        } else if (this.drawVerified) {
            BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft - AndroidUtilities.dp(f2), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
            BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft - AndroidUtilities.dp(f2), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
            Theme.dialogs_verifiedDrawable.draw(canvas);
            Theme.dialogs_verifiedCheckDrawable.draw(canvas);
        } else if (this.drawPremium) {
            Drawable drawable = PremiumGradient.getInstance().premiumStarDrawableMini;
            BaseCell.setDrawableBounds(drawable, this.nameMuteLeft - AndroidUtilities.dp(f2), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f));
            drawable.draw(canvas);
        } else {
            int i26 = this.drawScam;
            if (i26 != 0) {
                BaseCell.setDrawableBounds((Drawable) (i26 == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable), this.nameMuteLeft, AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.0f : 15.0f));
                (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).draw(canvas);
            }
        }
        if (this.drawReorder || this.reorderIconProgress != 0.0f) {
            Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
            BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
            Theme.dialogs_reorderDrawable.draw(canvas);
        }
        if (this.drawError) {
            Theme.dialogs_errorDrawable.setAlpha((int) ((f2 - this.reorderIconProgress) * 255.0f));
            this.rect.set(this.errorLeft, this.errorTop, i4 + AndroidUtilities.dp(23.0f), this.errorTop + AndroidUtilities.dp(23.0f));
            RectF rectF = this.rect;
            float f27 = AndroidUtilities.density;
            canvas.drawRoundRect(rectF, f27 * 11.5f, f27 * 11.5f, Theme.dialogs_errorPaint);
            BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
            Theme.dialogs_errorDrawable.draw(canvas);
        } else {
            boolean z14 = this.drawCount;
            if (((z14 || this.drawMention) && this.drawCount2) || this.countChangeProgress != f2 || this.drawReactionMention || this.reactionsMentionsChangeProgress != f2) {
                if ((z14 && this.drawCount2) || this.countChangeProgress != f2) {
                    int i27 = this.unreadCount;
                    float f28 = (i27 != 0 || this.markUnread) ? this.countChangeProgress : f2 - this.countChangeProgress;
                    StaticLayout staticLayout2 = this.countOldLayout;
                    if (staticLayout2 == null || i27 == 0) {
                        if (i27 != 0) {
                            staticLayout2 = this.countLayout;
                        }
                        Paint paint = (this.dialogMuted || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
                        paint.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                        Theme.dialogs_countTextPaint.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                        this.rect.set(this.countLeft - AndroidUtilities.dp(5.5f), this.countTop, dp7 + this.countWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                        if (f28 != 1.0f) {
                            if (this.drawPin) {
                                Theme.dialogs_pinnedDrawable.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                                BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                                canvas.save();
                                float f29 = 1.0f - f28;
                                canvas.scale(f29, f29, Theme.dialogs_pinnedDrawable.getBounds().centerX(), Theme.dialogs_pinnedDrawable.getBounds().centerY());
                                Theme.dialogs_pinnedDrawable.draw(canvas);
                                canvas.restore();
                            }
                            canvas.save();
                            canvas.scale(f28, f28, this.rect.centerX(), this.rect.centerY());
                        }
                        RectF rectF2 = this.rect;
                        float f30 = AndroidUtilities.density;
                        canvas.drawRoundRect(rectF2, f30 * 11.5f, f30 * 11.5f, paint);
                        if (staticLayout2 != null) {
                            canvas.save();
                            canvas.translate(this.countLeft, this.countTop + AndroidUtilities.dp(4.0f));
                            staticLayout2.draw(canvas);
                            canvas.restore();
                        }
                        f13 = 1.0f;
                        if (f28 != 1.0f) {
                            canvas.restore();
                        }
                        if (this.drawMention) {
                            Theme.dialogs_countPaint.setAlpha((int) ((f13 - this.reorderIconProgress) * 255.0f));
                            this.rect.set(this.mentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp6 + this.mentionWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                            Paint paint2 = (!this.dialogMuted || this.folderId == 0) ? Theme.dialogs_countPaint : Theme.dialogs_countGrayPaint;
                            RectF rectF3 = this.rect;
                            float f31 = AndroidUtilities.density;
                            canvas.drawRoundRect(rectF3, f31 * 11.5f, f31 * 11.5f, paint2);
                            if (this.mentionLayout != null) {
                                Theme.dialogs_countTextPaint.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                                canvas.save();
                                canvas.translate(this.mentionLeft, this.countTop + AndroidUtilities.dp(4.0f));
                                this.mentionLayout.draw(canvas);
                                canvas.restore();
                            } else {
                                Theme.dialogs_mentionDrawable.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                                BaseCell.setDrawableBounds(Theme.dialogs_mentionDrawable, this.mentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.2f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                Theme.dialogs_mentionDrawable.draw(canvas);
                            }
                        }
                        float f32 = this.drawReactionMention ? 1.0f : 1.0f;
                        Theme.dialogs_reactionsCountPaint.setAlpha((int) ((f32 - this.reorderIconProgress) * 255.0f));
                        this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp5 + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                        Paint paint3 = Theme.dialogs_reactionsCountPaint;
                        canvas.save();
                        f14 = this.reactionsMentionsChangeProgress;
                        if (f14 != 1.0f) {
                            if (!this.drawReactionMention) {
                                f14 = 1.0f - f14;
                            }
                            canvas.scale(f14, f14, this.rect.centerX(), this.rect.centerY());
                        }
                        RectF rectF4 = this.rect;
                        float f33 = AndroidUtilities.density;
                        canvas.drawRoundRect(rectF4, f33 * 11.5f, f33 * 11.5f, paint3);
                        Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                        BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                        Theme.dialogs_reactionsMentionDrawable.draw(canvas);
                        canvas.restore();
                    } else {
                        Paint paint4 = (this.dialogMuted || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
                        paint4.setAlpha((int) ((f2 - this.reorderIconProgress) * 255.0f));
                        Theme.dialogs_countTextPaint.setAlpha((int) ((f2 - this.reorderIconProgress) * 255.0f));
                        float f34 = f28 * 2.0f;
                        float f35 = f34 > f2 ? 1.0f : f34;
                        float f36 = f2 - f35;
                        float f37 = (this.countLeft * f35) + (this.countLeftOld * f36);
                        float dp10 = f37 - AndroidUtilities.dp(5.5f);
                        this.rect.set(dp10, this.countTop, (this.countWidth * f35) + dp10 + (this.countWidthOld * f36) + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                        if (f28 <= 0.5f) {
                            f15 = (CubicBezierInterpolator.EASE_OUT.getInterpolation(f34) * 0.1f) + 1.0f;
                        } else {
                            f15 = (CubicBezierInterpolator.EASE_IN.getInterpolation(1.0f - ((f28 - 0.5f) * 2.0f)) * 0.1f) + 1.0f;
                        }
                        canvas.save();
                        canvas.scale(f15, f15, this.rect.centerX(), this.rect.centerY());
                        RectF rectF5 = this.rect;
                        float f38 = AndroidUtilities.density;
                        canvas.drawRoundRect(rectF5, f38 * 11.5f, f38 * 11.5f, paint4);
                        if (this.countAnimationStableLayout != null) {
                            canvas.save();
                            canvas.translate(f37, this.countTop + AndroidUtilities.dp(4.0f));
                            this.countAnimationStableLayout.draw(canvas);
                            canvas.restore();
                        }
                        int alpha = Theme.dialogs_countTextPaint.getAlpha();
                        float f39 = alpha;
                        Theme.dialogs_countTextPaint.setAlpha((int) (f39 * f35));
                        if (this.countAnimationInLayout != null) {
                            canvas.save();
                            canvas.translate(f37, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * f36) + this.countTop + AndroidUtilities.dp(4.0f));
                            this.countAnimationInLayout.draw(canvas);
                            canvas.restore();
                        } else if (this.countLayout != null) {
                            canvas.save();
                            canvas.translate(f37, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * f36) + this.countTop + AndroidUtilities.dp(4.0f));
                            this.countLayout.draw(canvas);
                            canvas.restore();
                        }
                        if (this.countOldLayout != null) {
                            Theme.dialogs_countTextPaint.setAlpha((int) (f39 * f36));
                            canvas.save();
                            canvas.translate(f37, ((this.countAnimationIncrement ? -AndroidUtilities.dp(13.0f) : AndroidUtilities.dp(13.0f)) * f35) + this.countTop + AndroidUtilities.dp(4.0f));
                            this.countOldLayout.draw(canvas);
                            canvas.restore();
                        }
                        Theme.dialogs_countTextPaint.setAlpha(alpha);
                        canvas.restore();
                    }
                }
                f13 = 1.0f;
                if (this.drawMention) {
                }
                if (this.drawReactionMention) {
                }
                Theme.dialogs_reactionsCountPaint.setAlpha((int) ((f32 - this.reorderIconProgress) * 255.0f));
                this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp5 + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                Paint paint32 = Theme.dialogs_reactionsCountPaint;
                canvas.save();
                f14 = this.reactionsMentionsChangeProgress;
                if (f14 != 1.0f) {
                }
                RectF rectF42 = this.rect;
                float f332 = AndroidUtilities.density;
                canvas.drawRoundRect(rectF42, f332 * 11.5f, f332 * 11.5f, paint32);
                Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                Theme.dialogs_reactionsMentionDrawable.draw(canvas);
                canvas.restore();
            } else if (this.drawPin) {
                Theme.dialogs_pinnedDrawable.setAlpha((int) ((f2 - this.reorderIconProgress) * 255.0f));
                BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                Theme.dialogs_pinnedDrawable.draw(canvas);
            }
        }
        if (this.animatingArchiveAvatar) {
            canvas.save();
            float interpolation2 = this.interpolator.getInterpolation(this.animatingArchiveAvatarProgress / 170.0f) + 1.0f;
            canvas.scale(interpolation2, interpolation2, this.avatarImage.getCenterX(), this.avatarImage.getCenterY());
        }
        if (this.currentDialogFolderId == 0 || (pullForegroundDrawable = this.archivedChatsDrawable) == null || !pullForegroundDrawable.isDraw()) {
            this.avatarImage.draw(canvas);
        }
        if (this.hasMessageThumb) {
            this.thumbImage.draw(canvas);
            if (this.drawPlay) {
                BaseCell.setDrawableBounds(Theme.dialogs_playDrawable, (int) (this.thumbImage.getCenterX() - (Theme.dialogs_playDrawable.getIntrinsicWidth() / 2)), (int) (this.thumbImage.getCenterY() - (Theme.dialogs_playDrawable.getIntrinsicHeight() / 2)));
                Theme.dialogs_playDrawable.draw(canvas);
            }
        }
        if (this.animatingArchiveAvatar) {
            canvas.restore();
        }
        if (this.isDialogCell && this.currentDialogFolderId == 0) {
            TLRPC$User tLRPC$User = this.user;
            if (tLRPC$User != null && !MessagesController.isSupportUser(tLRPC$User) && !this.user.bot) {
                boolean isOnline = isOnline();
                if (isOnline || this.onlineProgress != 0.0f) {
                    int imageY2 = (int) (this.avatarImage.getImageY2() - AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 6.0f : 8.0f));
                    if (LocaleController.isRTL) {
                        float imageX = this.avatarImage.getImageX();
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f21 = 6.0f;
                        }
                        f12 = imageX + AndroidUtilities.dp(f21);
                    } else {
                        float imageX2 = this.avatarImage.getImageX2();
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f21 = 6.0f;
                        }
                        f12 = imageX2 - AndroidUtilities.dp(f21);
                    }
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("windowBackgroundWhite", this.resourcesProvider));
                    float f40 = (int) f12;
                    float f41 = imageY2;
                    canvas.drawCircle(f40, f41, AndroidUtilities.dp(7.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle", this.resourcesProvider));
                    canvas.drawCircle(f40, f41, AndroidUtilities.dp(5.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    if (isOnline) {
                        float f42 = this.onlineProgress;
                        if (f42 < 1.0f) {
                            float f43 = f42 + (((float) j2) / 150.0f);
                            this.onlineProgress = f43;
                            if (f43 > 1.0f) {
                                this.onlineProgress = 1.0f;
                            }
                            z4 = true;
                        }
                    } else {
                        float f44 = this.onlineProgress;
                        if (f44 > 0.0f) {
                            float f45 = f44 - (((float) j2) / 150.0f);
                            this.onlineProgress = f45;
                            if (f45 < 0.0f) {
                                this.onlineProgress = 0.0f;
                            }
                            z4 = true;
                        }
                    }
                    z = z4;
                }
                z4 = z;
                z = z4;
            } else {
                TLRPC$Chat tLRPC$Chat = this.chat;
                if (tLRPC$Chat != null) {
                    boolean z15 = tLRPC$Chat.call_active && tLRPC$Chat.call_not_empty;
                    this.hasCall = z15;
                    if (z15 || this.chatCallProgress != 0.0f) {
                        CheckBox2 checkBox2 = this.checkBox;
                        float progress = (checkBox2 == null || !checkBox2.isChecked()) ? 1.0f : 1.0f - this.checkBox.getProgress();
                        int imageY22 = (int) (this.avatarImage.getImageY2() - AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 6.0f : 8.0f));
                        if (LocaleController.isRTL) {
                            float imageX3 = this.avatarImage.getImageX();
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                f21 = 6.0f;
                            }
                            f5 = imageX3 + AndroidUtilities.dp(f21);
                        } else {
                            float imageX22 = this.avatarImage.getImageX2();
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                f21 = 6.0f;
                            }
                            f5 = imageX22 - AndroidUtilities.dp(f21);
                        }
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("windowBackgroundWhite", this.resourcesProvider));
                        float f46 = (int) f5;
                        float f47 = imageY22;
                        canvas.drawCircle(f46, f47, AndroidUtilities.dp(11.0f) * this.chatCallProgress * progress, Theme.dialogs_onlineCirclePaint);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle", this.resourcesProvider));
                        canvas.drawCircle(f46, f47, AndroidUtilities.dp(9.0f) * this.chatCallProgress * progress, Theme.dialogs_onlineCirclePaint);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("windowBackgroundWhite", this.resourcesProvider));
                        int i28 = this.progressStage;
                        if (i28 == 0) {
                            f8 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            dp3 = AndroidUtilities.dp(3.0f);
                            dp4 = AndroidUtilities.dp(2.0f);
                            f11 = this.innerProgress;
                        } else {
                            if (i28 == 1) {
                                f8 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                f7 = 1.0f;
                                dp = AndroidUtilities.dp(1.0f);
                                dp2 = AndroidUtilities.dp(4.0f);
                                f10 = this.innerProgress;
                            } else if (i28 == 2) {
                                f8 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                dp3 = AndroidUtilities.dp(5.0f);
                                dp4 = AndroidUtilities.dp(4.0f);
                                f11 = this.innerProgress;
                            } else if (i28 == 3) {
                                f8 = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                f7 = 1.0f;
                                dp = AndroidUtilities.dp(1.0f);
                                dp2 = AndroidUtilities.dp(2.0f);
                                f10 = this.innerProgress;
                            } else if (i28 == 4) {
                                f8 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                dp3 = AndroidUtilities.dp(3.0f);
                                dp4 = AndroidUtilities.dp(2.0f);
                                f11 = this.innerProgress;
                            } else if (i28 == 5) {
                                f8 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                f7 = 1.0f;
                                dp = AndroidUtilities.dp(1.0f);
                                dp2 = AndroidUtilities.dp(4.0f);
                                f10 = this.innerProgress;
                            } else if (i28 == 6) {
                                f8 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                f6 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                f7 = 1.0f;
                                if (this.chatCallProgress >= f7 || progress < f7) {
                                    canvas.save();
                                    float f48 = this.chatCallProgress;
                                    canvas.scale(f48 * progress, f48 * progress, f46, f47);
                                }
                                this.rect.set(i3 - AndroidUtilities.dp(1.0f), f47 - f8, AndroidUtilities.dp(1.0f) + i3, f8 + f47);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                float f49 = f47 - f6;
                                float f50 = f47 + f6;
                                this.rect.set(i3 - AndroidUtilities.dp(5.0f), f49, i3 - AndroidUtilities.dp(3.0f), f50);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                this.rect.set(AndroidUtilities.dp(3.0f) + i3, f49, i3 + AndroidUtilities.dp(5.0f), f50);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                if (this.chatCallProgress >= 1.0f || progress < 1.0f) {
                                    canvas.restore();
                                }
                                float f51 = (float) j2;
                                f9 = this.innerProgress + (f51 / 400.0f);
                                this.innerProgress = f9;
                                if (f9 >= 1.0f) {
                                    this.innerProgress = 0.0f;
                                    int i29 = this.progressStage + 1;
                                    this.progressStage = i29;
                                    if (i29 >= 8) {
                                        this.progressStage = 0;
                                    }
                                }
                                if (this.hasCall) {
                                    float f52 = this.chatCallProgress;
                                    if (f52 < 1.0f) {
                                        float f53 = f52 + (f51 / 150.0f);
                                        this.chatCallProgress = f53;
                                        if (f53 > 1.0f) {
                                            this.chatCallProgress = 1.0f;
                                        }
                                    }
                                    f3 = 0.0f;
                                } else {
                                    float f54 = this.chatCallProgress;
                                    f3 = 0.0f;
                                    if (f54 > 0.0f) {
                                        float f55 = f54 - (f51 / 150.0f);
                                        this.chatCallProgress = f55;
                                        if (f55 < 0.0f) {
                                            this.chatCallProgress = 0.0f;
                                        }
                                    }
                                }
                                z = true;
                                if (this.translationX != f3) {
                                    canvas.restore();
                                }
                                if (this.currentDialogFolderId != 0 && this.translationX == f3 && this.archivedChatsDrawable != null) {
                                    canvas.save();
                                    canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                                    this.archivedChatsDrawable.draw(canvas);
                                    canvas.restore();
                                }
                                if (this.useSeparator) {
                                    int dp11 = (this.fullSeparator || (this.currentDialogFolderId != 0 && this.archiveHidden && !this.fullSeparator2) || (this.fullSeparator2 && !this.archiveHidden)) ? 0 : AndroidUtilities.dp(72.0f);
                                    if (LocaleController.isRTL) {
                                        canvas.drawLine(0.0f, getMeasuredHeight() - 1, getMeasuredWidth() - dp11, getMeasuredHeight() - 1, Theme.dividerPaint);
                                    } else {
                                        canvas.drawLine(dp11, getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
                                        if (this.clipProgress != 0.0f) {
                                            if (Build.VERSION.SDK_INT != 24) {
                                                canvas.restore();
                                            } else {
                                                Theme.dialogs_pinnedPaint.setColor(Theme.getColor("windowBackgroundWhite", this.resourcesProvider));
                                                canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), this.topClip * this.clipProgress, Theme.dialogs_pinnedPaint);
                                                canvas.drawRect(0.0f, getMeasuredHeight() - ((int) (this.bottomClip * this.clipProgress)), getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
                                            }
                                        }
                                        z2 = this.drawReorder;
                                        if (!z2 || this.reorderIconProgress != 0.0f) {
                                            if (z2) {
                                                float f56 = this.reorderIconProgress;
                                                if (f56 < 1.0f) {
                                                    float f57 = f56 + (((float) j2) / 170.0f);
                                                    this.reorderIconProgress = f57;
                                                    if (f57 > 1.0f) {
                                                        this.reorderIconProgress = 1.0f;
                                                    }
                                                    f4 = 0.0f;
                                                }
                                            } else {
                                                float f58 = this.reorderIconProgress;
                                                f4 = 0.0f;
                                                if (f58 > 0.0f) {
                                                    float f59 = f58 - (((float) j2) / 170.0f);
                                                    this.reorderIconProgress = f59;
                                                    if (f59 < 0.0f) {
                                                        this.reorderIconProgress = 0.0f;
                                                    }
                                                }
                                                z3 = z;
                                                if (this.archiveHidden) {
                                                    float f60 = this.archiveBackgroundProgress;
                                                    if (f60 > f4) {
                                                        float f61 = f60 - (((float) j2) / 230.0f);
                                                        this.archiveBackgroundProgress = f61;
                                                        if (f61 < f4) {
                                                            this.archiveBackgroundProgress = f4;
                                                        }
                                                        if (this.avatarDrawable.getAvatarType() == 2) {
                                                            this.avatarDrawable.setArchivedAvatarHiddenProgress(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.archiveBackgroundProgress));
                                                        }
                                                        z3 = true;
                                                    }
                                                    if (this.animatingArchiveAvatar) {
                                                        float f62 = this.animatingArchiveAvatarProgress + ((float) j2);
                                                        this.animatingArchiveAvatarProgress = f62;
                                                        if (f62 >= 170.0f) {
                                                            this.animatingArchiveAvatarProgress = 170.0f;
                                                            this.animatingArchiveAvatar = false;
                                                        }
                                                        z3 = true;
                                                    }
                                                    if (!this.drawRevealBackground) {
                                                        float f63 = this.currentRevealBounceProgress;
                                                        if (f63 < 1.0f) {
                                                            float f64 = f63 + (((float) j2) / 170.0f);
                                                            this.currentRevealBounceProgress = f64;
                                                            if (f64 > 1.0f) {
                                                                this.currentRevealBounceProgress = 1.0f;
                                                                z3 = true;
                                                            }
                                                        }
                                                        float f65 = this.currentRevealProgress;
                                                        if (f65 < 1.0f) {
                                                            float f66 = f65 + (((float) j2) / 300.0f);
                                                            this.currentRevealProgress = f66;
                                                            if (f66 > 1.0f) {
                                                                this.currentRevealProgress = 1.0f;
                                                            }
                                                            z3 = true;
                                                        }
                                                        if (z3) {
                                                            return;
                                                        }
                                                        invalidate();
                                                        return;
                                                    }
                                                    if (this.currentRevealBounceProgress == 1.0f) {
                                                        this.currentRevealBounceProgress = 0.0f;
                                                        z3 = true;
                                                    }
                                                    float f67 = this.currentRevealProgress;
                                                    if (f67 > 0.0f) {
                                                        float f68 = f67 - (((float) j2) / 300.0f);
                                                        this.currentRevealProgress = f68;
                                                        if (f68 < 0.0f) {
                                                            this.currentRevealProgress = 0.0f;
                                                        }
                                                        z3 = true;
                                                    }
                                                    if (z3) {
                                                    }
                                                } else {
                                                    float f69 = this.archiveBackgroundProgress;
                                                    if (f69 < 1.0f) {
                                                        float f70 = f69 + (((float) j2) / 230.0f);
                                                        this.archiveBackgroundProgress = f70;
                                                        if (f70 > 1.0f) {
                                                            this.archiveBackgroundProgress = 1.0f;
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
                                        f4 = 0.0f;
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
                                f8 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                f7 = 1.0f;
                                dp = AndroidUtilities.dp(1.0f);
                                dp2 = AndroidUtilities.dp(2.0f);
                                f10 = this.innerProgress;
                            }
                            f6 = dp + (dp2 * f10);
                            if (this.chatCallProgress >= f7) {
                            }
                            canvas.save();
                            float f482 = this.chatCallProgress;
                            canvas.scale(f482 * progress, f482 * progress, f46, f47);
                            this.rect.set(i3 - AndroidUtilities.dp(1.0f), f47 - f8, AndroidUtilities.dp(1.0f) + i3, f8 + f47);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                            float f492 = f47 - f6;
                            float f502 = f47 + f6;
                            this.rect.set(i3 - AndroidUtilities.dp(5.0f), f492, i3 - AndroidUtilities.dp(3.0f), f502);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                            this.rect.set(AndroidUtilities.dp(3.0f) + i3, f492, i3 + AndroidUtilities.dp(5.0f), f502);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                            if (this.chatCallProgress >= 1.0f) {
                            }
                            canvas.restore();
                            float f512 = (float) j2;
                            f9 = this.innerProgress + (f512 / 400.0f);
                            this.innerProgress = f9;
                            if (f9 >= 1.0f) {
                            }
                            if (this.hasCall) {
                            }
                            z = true;
                            if (this.translationX != f3) {
                            }
                            if (this.currentDialogFolderId != 0) {
                                canvas.save();
                                canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                                this.archivedChatsDrawable.draw(canvas);
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
                        f6 = dp3 - (dp4 * f11);
                        f7 = 1.0f;
                        if (this.chatCallProgress >= f7) {
                        }
                        canvas.save();
                        float f4822 = this.chatCallProgress;
                        canvas.scale(f4822 * progress, f4822 * progress, f46, f47);
                        this.rect.set(i3 - AndroidUtilities.dp(1.0f), f47 - f8, AndroidUtilities.dp(1.0f) + i3, f8 + f47);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        float f4922 = f47 - f6;
                        float f5022 = f47 + f6;
                        this.rect.set(i3 - AndroidUtilities.dp(5.0f), f4922, i3 - AndroidUtilities.dp(3.0f), f5022);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        this.rect.set(AndroidUtilities.dp(3.0f) + i3, f4922, i3 + AndroidUtilities.dp(5.0f), f5022);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        if (this.chatCallProgress >= 1.0f) {
                        }
                        canvas.restore();
                        float f5122 = (float) j2;
                        f9 = this.innerProgress + (f5122 / 400.0f);
                        this.innerProgress = f9;
                        if (f9 >= 1.0f) {
                        }
                        if (this.hasCall) {
                        }
                        z = true;
                        if (this.translationX != f3) {
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
        }
        f3 = 0.0f;
        if (this.translationX != f3) {
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
