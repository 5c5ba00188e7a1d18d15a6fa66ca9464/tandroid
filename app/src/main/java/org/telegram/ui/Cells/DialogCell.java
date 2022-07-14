package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import com.google.android.exoplayer2.C;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatThemeController;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
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
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DialogsAdapter;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EmptyStubSpan;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.PullForegroundDrawable;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.StatusDrawable;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.DialogsActivity;
/* loaded from: classes4.dex */
public class DialogCell extends BaseCell {
    private int animateFromStatusDrawableParams;
    private int animateToStatusDrawableParams;
    private boolean animatingArchiveAvatar;
    private float animatingArchiveAvatarProgress;
    private float archiveBackgroundProgress;
    private boolean archiveHidden;
    private PullForegroundDrawable archivedChatsDrawable;
    private boolean attachedToWindow;
    private AvatarDrawable avatarDrawable;
    private ImageReceiver avatarImage;
    private int bottomClip;
    private TLRPC.Chat chat;
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
    private TLRPC.DraftMessage draftMessage;
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
    private TLRPC.EncryptedChat encryptedChat;
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
    private TLRPC.User user;

    /* loaded from: classes4.dex */
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

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isMoving() {
        return this.moving;
    }

    /* loaded from: classes4.dex */
    public static class FixedWidthSpan extends ReplacementSpan {
        private int width;

        public FixedWidthSpan(int w) {
            this.width = w;
        }

        @Override // android.text.style.ReplacementSpan
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            if (fm == null) {
                fm = paint.getFontMetricsInt();
            }
            if (fm != null) {
                int h = fm.descent - fm.ascent;
                int i = 1 - h;
                fm.descent = i;
                fm.bottom = i;
                fm.ascent = -1;
                fm.top = -1;
            }
            int h2 = this.width;
            return h2;
        }

        @Override // android.text.style.ReplacementSpan
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        }
    }

    /* loaded from: classes4.dex */
    public static class BounceInterpolator implements Interpolator {
        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float t) {
            if (t < 0.33f) {
                return (t / 0.33f) * 0.1f;
            }
            float t2 = t - 0.33f;
            if (t2 < 0.33f) {
                return 0.1f - ((t2 / 0.34f) * 0.15f);
            }
            return (((t2 - 0.34f) / 0.33f) * 0.05f) - 0.05f;
        }
    }

    public DialogCell(DialogsActivity fragment, Context context, boolean needCheck, boolean forceThreeLines) {
        this(fragment, context, needCheck, forceThreeLines, UserConfig.selectedAccount, null);
    }

    public DialogCell(DialogsActivity fragment, Context context, boolean needCheck, boolean forceThreeLines, int account, Theme.ResourcesProvider resourcesProvider) {
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
        this.parentFragment = fragment;
        Theme.createDialogsResources(context);
        this.avatarImage.setRoundRadius(AndroidUtilities.dp(28.0f));
        this.thumbImage.setRoundRadius(AndroidUtilities.dp(2.0f));
        this.useForceThreeLines = forceThreeLines;
        this.currentAccount = account;
        if (needCheck) {
            CheckBox2 checkBox2 = new CheckBox2(context, 21, resourcesProvider);
            this.checkBox = checkBox2;
            checkBox2.setColor(null, Theme.key_windowBackgroundWhite, Theme.key_checkboxCheck);
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(3);
            addView(this.checkBox);
        }
    }

    public void setDialog(TLRPC.Dialog dialog, int type, int folder) {
        if (this.currentDialogId != dialog.id) {
            ValueAnimator valueAnimator = this.statusDrawableAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.statusDrawableAnimator.cancel();
            }
            this.statusDrawableAnimationInProgress = false;
            this.lastStatusDrawableParams = -1;
        }
        this.currentDialogId = dialog.id;
        this.lastDialogChangedTime = System.currentTimeMillis();
        this.isDialogCell = true;
        if (dialog instanceof TLRPC.TL_dialogFolder) {
            TLRPC.TL_dialogFolder dialogFolder = (TLRPC.TL_dialogFolder) dialog;
            this.currentDialogFolderId = dialogFolder.folder.id;
            PullForegroundDrawable pullForegroundDrawable = this.archivedChatsDrawable;
            if (pullForegroundDrawable != null) {
                pullForegroundDrawable.setCell(this);
            }
        } else {
            this.currentDialogFolderId = 0;
        }
        this.dialogsType = type;
        this.folderId = folder;
        this.messageId = 0;
        update(0, false);
        checkOnline();
        checkGroupCall();
        checkChatTheme();
    }

    public void setDialogIndex(int i) {
        this.index = i;
    }

    public void setDialog(CustomDialog dialog) {
        this.customDialog = dialog;
        this.messageId = 0;
        update(0);
        checkOnline();
        checkGroupCall();
        checkChatTheme();
    }

    private void checkOnline() {
        TLRPC.User newUser;
        if (this.user != null && (newUser = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id))) != null) {
            this.user = newUser;
        }
        boolean isOnline = isOnline();
        this.onlineProgress = isOnline ? 1.0f : 0.0f;
    }

    private boolean isOnline() {
        TLRPC.User user = this.user;
        if (user == null || user.self) {
            return false;
        }
        if (this.user.status != null && this.user.status.expires <= 0 && MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(Long.valueOf(this.user.id))) {
            return true;
        }
        return this.user.status != null && this.user.status.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
    }

    private void checkGroupCall() {
        TLRPC.Chat chat = this.chat;
        boolean z = chat != null && chat.call_active && this.chat.call_not_empty;
        this.hasCall = z;
        this.chatCallProgress = z ? 1.0f : 0.0f;
    }

    private void checkChatTheme() {
        MessageObject messageObject = this.message;
        if (messageObject != null && messageObject.messageOwner != null && (this.message.messageOwner.action instanceof TLRPC.TL_messageActionSetChatTheme) && this.lastUnreadState) {
            TLRPC.TL_messageActionSetChatTheme setThemeAction = (TLRPC.TL_messageActionSetChatTheme) this.message.messageOwner.action;
            ChatThemeController.getInstance(this.currentAccount).setDialogTheme(this.currentDialogId, setThemeAction.emoticon, false);
        }
    }

    public void setDialog(long dialog_id, MessageObject messageObject, int date, boolean useMe) {
        if (this.currentDialogId != dialog_id) {
            this.lastStatusDrawableParams = -1;
        }
        this.currentDialogId = dialog_id;
        this.lastDialogChangedTime = System.currentTimeMillis();
        this.message = messageObject;
        this.useMeForMyMessages = useMe;
        this.isDialogCell = false;
        this.lastMessageDate = date;
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

    public void setPreloader(DialogsAdapter.DialogsPreloader preloader) {
        this.preloader = preloader;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isSliding = false;
        this.drawRevealBackground = false;
        this.currentRevealProgress = 0.0f;
        this.attachedToWindow = false;
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
        this.attachedToWindow = true;
        this.cornerProgress = 0.0f;
        setTranslationX(0.0f);
        setTranslationY(0.0f);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 != null) {
            checkBox2.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), C.BUFFER_FLAG_ENCRYPTED), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), C.BUFFER_FLAG_ENCRYPTED));
        }
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 78.0f : 72.0f) + (this.useSeparator ? 1 : 0));
        this.topClip = 0;
        this.bottomClip = getMeasuredHeight();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int x;
        if (this.currentDialogId == 0 && this.customDialog == null) {
            return;
        }
        if (this.checkBox != null) {
            float f = 45.0f;
            if (LocaleController.isRTL) {
                int i = right - left;
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    f = 43.0f;
                }
                x = i - AndroidUtilities.dp(f);
            } else {
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    f = 43.0f;
                }
                x = AndroidUtilities.dp(f);
            }
            int y = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 48.0f : 42.0f);
            CheckBox2 checkBox2 = this.checkBox;
            checkBox2.layout(x, y, checkBox2.getMeasuredWidth() + x, this.checkBox.getMeasuredHeight() + y);
        }
        int size = (getMeasuredHeight() + getMeasuredWidth()) << 16;
        if (size != this.lastSize) {
            this.lastSize = size;
            try {
                buildLayout();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public boolean isUnread() {
        return (this.unreadCount != 0 || this.markUnread) && !this.dialogMuted;
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
        String title;
        ArrayList<TLRPC.Dialog> dialogs = MessagesController.getInstance(this.currentAccount).getDialogs(this.currentDialogFolderId);
        this.currentDialogFolderDialogsCount = dialogs.size();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        int N = dialogs.size();
        for (int a = 0; a < N; a++) {
            TLRPC.Dialog dialog = dialogs.get(a);
            TLRPC.User currentUser = null;
            TLRPC.Chat currentChat = null;
            if (DialogObject.isEncryptedDialog(dialog.id)) {
                TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(dialog.id)));
                if (encryptedChat != null) {
                    currentUser = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(encryptedChat.user_id));
                }
            } else if (DialogObject.isUserDialog(dialog.id)) {
                currentUser = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(dialog.id));
            } else {
                currentChat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-dialog.id));
            }
            if (currentChat != null) {
                title = currentChat.title.replace('\n', ' ');
            } else if (currentUser == null) {
                continue;
            } else {
                title = UserObject.isDeleted(currentUser) ? LocaleController.getString("HiddenName", R.string.HiddenName) : ContactsController.formatName(currentUser.first_name, currentUser.last_name).replace('\n', ' ');
            }
            if (builder.length() > 0) {
                builder.append((CharSequence) ", ");
            }
            int boldStart = builder.length();
            int boldEnd = title.length() + boldStart;
            builder.append((CharSequence) title);
            if (dialog.unread_count > 0) {
                builder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM), 0, Theme.getColor(Theme.key_chats_nameArchived, this.resourcesProvider)), boldStart, boldEnd, 33);
            }
            if (builder.length() > 150) {
                break;
            }
        }
        return Emoji.replaceEmoji(builder, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:1022:0x1af9
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:92)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:44)
        */
    public void buildLayout() {
        /*
            Method dump skipped, instructions count: 7741
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.DialogCell.buildLayout():void");
    }

    private void drawCheckStatus(Canvas canvas, boolean drawClock, boolean drawCheck1, boolean drawCheck2, boolean moveCheck, float alpha) {
        if (alpha == 0.0f && !moveCheck) {
            return;
        }
        float scale = (alpha * 0.5f) + 0.5f;
        if (drawClock) {
            setDrawableBounds(Theme.dialogs_clockDrawable, this.clockDrawLeft, this.checkDrawTop);
            if (alpha != 1.0f) {
                canvas.save();
                canvas.scale(scale, scale, Theme.dialogs_clockDrawable.getBounds().centerX(), Theme.dialogs_halfCheckDrawable.getBounds().centerY());
                Theme.dialogs_clockDrawable.setAlpha((int) (255.0f * alpha));
            }
            Theme.dialogs_clockDrawable.draw(canvas);
            if (alpha != 1.0f) {
                canvas.restore();
                Theme.dialogs_clockDrawable.setAlpha(255);
            }
            invalidate();
        } else if (drawCheck2) {
            if (drawCheck1) {
                setDrawableBounds(Theme.dialogs_halfCheckDrawable, this.halfCheckDrawLeft, this.checkDrawTop);
                if (moveCheck) {
                    canvas.save();
                    canvas.scale(scale, scale, Theme.dialogs_halfCheckDrawable.getBounds().centerX(), Theme.dialogs_halfCheckDrawable.getBounds().centerY());
                    Theme.dialogs_halfCheckDrawable.setAlpha((int) (alpha * 255.0f));
                }
                if (!moveCheck && alpha != 0.0f) {
                    canvas.save();
                    canvas.scale(scale, scale, Theme.dialogs_halfCheckDrawable.getBounds().centerX(), Theme.dialogs_halfCheckDrawable.getBounds().centerY());
                    Theme.dialogs_halfCheckDrawable.setAlpha((int) (alpha * 255.0f));
                    Theme.dialogs_checkReadDrawable.setAlpha((int) (255.0f * alpha));
                }
                Theme.dialogs_halfCheckDrawable.draw(canvas);
                if (moveCheck) {
                    canvas.restore();
                    canvas.save();
                    canvas.translate(AndroidUtilities.dp(4.0f) * (1.0f - alpha), 0.0f);
                }
                setDrawableBounds(Theme.dialogs_checkReadDrawable, this.checkDrawLeft, this.checkDrawTop);
                Theme.dialogs_checkReadDrawable.draw(canvas);
                if (moveCheck) {
                    canvas.restore();
                    Theme.dialogs_halfCheckDrawable.setAlpha(255);
                }
                if (!moveCheck && alpha != 0.0f) {
                    canvas.restore();
                    Theme.dialogs_halfCheckDrawable.setAlpha(255);
                    Theme.dialogs_checkReadDrawable.setAlpha(255);
                    return;
                }
                return;
            }
            setDrawableBounds(Theme.dialogs_checkDrawable, this.checkDrawLeft1, this.checkDrawTop);
            if (alpha != 1.0f) {
                canvas.save();
                canvas.scale(scale, scale, Theme.dialogs_checkDrawable.getBounds().centerX(), Theme.dialogs_halfCheckDrawable.getBounds().centerY());
                Theme.dialogs_checkDrawable.setAlpha((int) (255.0f * alpha));
            }
            Theme.dialogs_checkDrawable.draw(canvas);
            if (alpha != 1.0f) {
                canvas.restore();
                Theme.dialogs_checkDrawable.setAlpha(255);
            }
        }
    }

    public boolean isPointInsideAvatar(float x, float y) {
        return !LocaleController.isRTL ? x >= 0.0f && x < ((float) AndroidUtilities.dp(60.0f)) : x >= ((float) (getMeasuredWidth() - AndroidUtilities.dp(60.0f))) && x < ((float) getMeasuredWidth());
    }

    public void setDialogSelected(boolean value) {
        if (this.isSelected != value) {
            invalidate();
        }
        this.isSelected = value;
    }

    public void checkCurrentDialogIndex(boolean frozen) {
        MessageObject newMessageObject;
        MessageObject messageObject;
        DialogsActivity dialogsActivity = this.parentFragment;
        if (dialogsActivity == null) {
            return;
        }
        ArrayList<TLRPC.Dialog> dialogsArray = dialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, frozen);
        if (this.index < dialogsArray.size()) {
            TLRPC.Dialog dialog = dialogsArray.get(this.index);
            boolean z = true;
            TLRPC.Dialog nextDialog = this.index + 1 < dialogsArray.size() ? dialogsArray.get(this.index + 1) : null;
            TLRPC.DraftMessage newDraftMessage = MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, 0);
            if (this.currentDialogFolderId != 0) {
                newMessageObject = findFolderTopMessage();
            } else {
                newMessageObject = MessagesController.getInstance(this.currentAccount).dialogMessage.get(dialog.id);
            }
            if (this.currentDialogId != dialog.id || (((messageObject = this.message) != null && messageObject.getId() != dialog.top_message) || ((newMessageObject != null && newMessageObject.messageOwner.edit_date != this.currentEditDate) || this.unreadCount != dialog.unread_count || this.mentionCount != dialog.unread_mentions_count || this.markUnread != dialog.unread_mark || this.message != newMessageObject || newDraftMessage != this.draftMessage || this.drawPin != dialog.pinned))) {
                boolean dialogChanged = this.currentDialogId != dialog.id;
                this.currentDialogId = dialog.id;
                if (dialogChanged) {
                    this.lastDialogChangedTime = System.currentTimeMillis();
                    ValueAnimator valueAnimator = this.statusDrawableAnimator;
                    if (valueAnimator != null) {
                        valueAnimator.removeAllListeners();
                        this.statusDrawableAnimator.cancel();
                    }
                    this.statusDrawableAnimationInProgress = false;
                    this.lastStatusDrawableParams = -1;
                }
                if (dialog instanceof TLRPC.TL_dialogFolder) {
                    TLRPC.TL_dialogFolder dialogFolder = (TLRPC.TL_dialogFolder) dialog;
                    this.currentDialogFolderId = dialogFolder.folder.id;
                } else {
                    this.currentDialogFolderId = 0;
                }
                int i = this.dialogsType;
                if (i == 7 || i == 8) {
                    MessagesController.DialogFilter filter = MessagesController.getInstance(this.currentAccount).selectedDialogFilter[this.dialogsType == 8 ? (char) 1 : (char) 0];
                    this.fullSeparator = (dialog instanceof TLRPC.TL_dialog) && nextDialog != null && filter != null && filter.pinnedDialogs.indexOfKey(dialog.id) >= 0 && filter.pinnedDialogs.indexOfKey(nextDialog.id) < 0;
                    this.fullSeparator2 = false;
                } else {
                    this.fullSeparator = (dialog instanceof TLRPC.TL_dialog) && dialog.pinned && nextDialog != null && !nextDialog.pinned;
                    this.fullSeparator2 = (dialog instanceof TLRPC.TL_dialogFolder) && nextDialog != null && !nextDialog.pinned;
                }
                if (dialogChanged) {
                    z = false;
                }
                update(0, z);
                if (dialogChanged) {
                    this.reorderIconProgress = (!this.drawPin || !this.drawReorder) ? 0.0f : 1.0f;
                }
                checkOnline();
                checkGroupCall();
                checkChatTheme();
            }
        }
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

    public void setChecked(boolean checked, boolean animated) {
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 == null) {
            return;
        }
        checkBox2.setChecked(checked, animated);
    }

    private MessageObject findFolderTopMessage() {
        DialogsActivity dialogsActivity = this.parentFragment;
        if (dialogsActivity == null) {
            return null;
        }
        ArrayList<TLRPC.Dialog> dialogs = dialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.currentDialogFolderId, false);
        MessageObject maxMessage = null;
        if (!dialogs.isEmpty()) {
            int N = dialogs.size();
            for (int a = 0; a < N; a++) {
                TLRPC.Dialog dialog = dialogs.get(a);
                MessageObject object = MessagesController.getInstance(this.currentAccount).dialogMessage.get(dialog.id);
                if (object != null && (maxMessage == null || object.messageOwner.date > maxMessage.messageOwner.date)) {
                    maxMessage = object;
                }
                if (dialog.pinnedNum == 0 && maxMessage != null) {
                    break;
                }
            }
        }
        return maxMessage;
    }

    public boolean isFolderCell() {
        return this.currentDialogFolderId != 0;
    }

    public void update(int mask) {
        update(mask, true);
    }

    public void update(int mask, boolean animated) {
        long dialogId;
        TLRPC.Chat chat2;
        MessageObject messageObject;
        int newMentionCount;
        int newCount;
        MessageObject messageObject2;
        CustomDialog customDialog = this.customDialog;
        boolean z = true;
        if (customDialog != null) {
            this.lastMessageDate = customDialog.date;
            if (this.customDialog.unread_count == 0) {
                z = false;
            }
            this.lastUnreadState = z;
            this.unreadCount = this.customDialog.unread_count;
            this.drawPin = this.customDialog.pinned;
            this.dialogMuted = this.customDialog.muted;
            this.avatarDrawable.setInfo(this.customDialog.id, this.customDialog.name, null);
            this.avatarImage.setImage(null, "50_50", this.avatarDrawable, null, 0L);
            this.thumbImage.setImageBitmap((Drawable) null);
        } else {
            int oldUnreadCount = this.unreadCount;
            boolean oldHasReactionsMentions = this.reactionMentionCount != 0;
            boolean oldMarkUnread = this.markUnread;
            if (this.isDialogCell) {
                TLRPC.Dialog dialog = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                if (dialog != null) {
                    this.clearingDialog = MessagesController.getInstance(this.currentAccount).isClearingDialog(dialog.id);
                    MessageObject messageObject3 = MessagesController.getInstance(this.currentAccount).dialogMessage.get(dialog.id);
                    this.message = messageObject3;
                    this.lastUnreadState = messageObject3 != null && messageObject3.isUnread();
                    if (dialog instanceof TLRPC.TL_dialogFolder) {
                        this.unreadCount = MessagesStorage.getInstance(this.currentAccount).getArchiveUnreadCount();
                        this.mentionCount = 0;
                        this.reactionMentionCount = 0;
                    } else {
                        this.unreadCount = dialog.unread_count;
                        this.mentionCount = dialog.unread_mentions_count;
                        this.reactionMentionCount = dialog.unread_reactions_count;
                    }
                    this.markUnread = dialog.unread_mark;
                    MessageObject messageObject4 = this.message;
                    this.currentEditDate = messageObject4 != null ? messageObject4.messageOwner.edit_date : 0;
                    this.lastMessageDate = dialog.last_message_date;
                    int i = this.dialogsType;
                    if (i == 7 || i == 8) {
                        MessagesController.DialogFilter filter = MessagesController.getInstance(this.currentAccount).selectedDialogFilter[this.dialogsType == 8 ? (char) 1 : (char) 0];
                        this.drawPin = filter != null && filter.pinnedDialogs.indexOfKey(dialog.id) >= 0;
                    } else {
                        this.drawPin = this.currentDialogFolderId == 0 && dialog.pinned;
                    }
                    MessageObject messageObject5 = this.message;
                    if (messageObject5 != null) {
                        this.lastSendState = messageObject5.messageOwner.send_state;
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
            if (this.dialogsType == 2) {
                this.drawPin = false;
            }
            if (mask != 0) {
                boolean continueUpdate = false;
                if (this.user != null && (mask & MessagesController.UPDATE_MASK_STATUS) != 0) {
                    this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id));
                    invalidate();
                }
                if (this.isDialogCell && (mask & MessagesController.UPDATE_MASK_USER_PRINT) != 0) {
                    CharSequence printString = MessagesController.getInstance(this.currentAccount).getPrintingString(this.currentDialogId, 0, true);
                    CharSequence charSequence = this.lastPrintString;
                    if ((charSequence != null && printString == null) || ((charSequence == null && printString != null) || (charSequence != null && !charSequence.equals(printString)))) {
                        continueUpdate = true;
                    }
                }
                if (!continueUpdate && (mask & MessagesController.UPDATE_MASK_MESSAGE_TEXT) != 0 && (messageObject2 = this.message) != null && messageObject2.messageText != this.lastMessageString) {
                    continueUpdate = true;
                }
                if (!continueUpdate && (mask & MessagesController.UPDATE_MASK_CHAT) != 0 && this.chat != null) {
                    TLRPC.Chat newChat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.id));
                    if ((newChat.call_active && newChat.call_not_empty) != this.hasCall) {
                        continueUpdate = true;
                    }
                }
                if (!continueUpdate && (mask & MessagesController.UPDATE_MASK_AVATAR) != 0 && this.chat == null) {
                    continueUpdate = true;
                }
                if (!continueUpdate && (mask & MessagesController.UPDATE_MASK_NAME) != 0 && this.chat == null) {
                    continueUpdate = true;
                }
                if (!continueUpdate && (mask & MessagesController.UPDATE_MASK_CHAT_AVATAR) != 0 && this.user == null) {
                    continueUpdate = true;
                }
                if (!continueUpdate && (mask & MessagesController.UPDATE_MASK_CHAT_NAME) != 0 && this.user == null) {
                    continueUpdate = true;
                }
                if (!continueUpdate && (mask & MessagesController.UPDATE_MASK_READ_DIALOG_MESSAGE) != 0) {
                    MessageObject messageObject6 = this.message;
                    if (messageObject6 != null && this.lastUnreadState != messageObject6.isUnread()) {
                        this.lastUnreadState = this.message.isUnread();
                        continueUpdate = true;
                    }
                    if (this.isDialogCell) {
                        TLRPC.Dialog dialog2 = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                        int newReactionCout = 0;
                        if (dialog2 instanceof TLRPC.TL_dialogFolder) {
                            newCount = MessagesStorage.getInstance(this.currentAccount).getArchiveUnreadCount();
                            newMentionCount = 0;
                        } else if (dialog2 != null) {
                            newCount = dialog2.unread_count;
                            newMentionCount = dialog2.unread_mentions_count;
                            newReactionCout = dialog2.unread_reactions_count;
                        } else {
                            newCount = 0;
                            newMentionCount = 0;
                        }
                        if (dialog2 != null && (this.unreadCount != newCount || this.markUnread != dialog2.unread_mark || this.mentionCount != newMentionCount || this.reactionMentionCount != newReactionCout)) {
                            this.unreadCount = newCount;
                            this.mentionCount = newMentionCount;
                            this.markUnread = dialog2.unread_mark;
                            this.reactionMentionCount = newReactionCout;
                            continueUpdate = true;
                        }
                    }
                }
                if (!continueUpdate && (mask & MessagesController.UPDATE_MASK_SEND_STATE) != 0 && (messageObject = this.message) != null && this.lastSendState != messageObject.messageOwner.send_state) {
                    this.lastSendState = this.message.messageOwner.send_state;
                    continueUpdate = true;
                }
                if (!continueUpdate) {
                    invalidate();
                    return;
                }
            }
            this.user = null;
            this.chat = null;
            this.encryptedChat = null;
            if (this.currentDialogFolderId == 0) {
                this.dialogMuted = this.isDialogCell && MessagesController.getInstance(this.currentAccount).isDialogMuted(this.currentDialogId);
                dialogId = this.currentDialogId;
            } else {
                this.dialogMuted = false;
                MessageObject findFolderTopMessage = findFolderTopMessage();
                this.message = findFolderTopMessage;
                if (findFolderTopMessage != null) {
                    dialogId = findFolderTopMessage.getDialogId();
                } else {
                    dialogId = 0;
                }
            }
            if (dialogId != 0) {
                if (DialogObject.isEncryptedDialog(dialogId)) {
                    TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(dialogId)));
                    this.encryptedChat = encryptedChat;
                    if (encryptedChat != null) {
                        this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.encryptedChat.user_id));
                    }
                } else if (DialogObject.isUserDialog(dialogId)) {
                    this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(dialogId));
                } else {
                    TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-dialogId));
                    this.chat = chat;
                    if (!this.isDialogCell && chat != null && chat.migrated_to != null && (chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.migrated_to.channel_id))) != null) {
                        this.chat = chat2;
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
                TLRPC.User user = this.user;
                if (user != null) {
                    this.avatarDrawable.setInfo(user);
                    if (UserObject.isReplyUser(this.user)) {
                        this.avatarDrawable.setAvatarType(12);
                        this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
                    } else if (!UserObject.isUserSelf(this.user) || this.useMeForMyMessages) {
                        this.avatarImage.setForUserOrChat(this.user, this.avatarDrawable, null, true);
                    } else {
                        this.avatarDrawable.setAvatarType(1);
                        this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
                    }
                } else {
                    TLRPC.Chat chat3 = this.chat;
                    if (chat3 != null) {
                        this.avatarDrawable.setInfo(chat3);
                        this.avatarImage.setForUserOrChat(this.chat, this.avatarDrawable);
                    }
                }
            }
            if (animated && ((oldUnreadCount != this.unreadCount || oldMarkUnread != this.markUnread) && System.currentTimeMillis() - this.lastDialogChangedTime > 100)) {
                ValueAnimator valueAnimator = this.countAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.countAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        DialogCell.this.m1642lambda$update$0$orgtelegramuiCellsDialogCell(valueAnimator2);
                    }
                });
                this.countAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animation) {
                        DialogCell.this.countChangeProgress = 1.0f;
                        DialogCell.this.countOldLayout = null;
                        DialogCell.this.countAnimationStableLayout = null;
                        DialogCell.this.countAnimationInLayout = null;
                        DialogCell.this.invalidate();
                    }
                });
                if ((oldUnreadCount == 0 || this.markUnread) && (this.markUnread || !oldMarkUnread)) {
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
                    String oldStr = String.valueOf(oldUnreadCount);
                    String newStr = String.valueOf(this.unreadCount);
                    if (oldStr.length() != newStr.length()) {
                        this.countOldLayout = this.countLayout;
                    } else {
                        SpannableStringBuilder oldSpannableStr = new SpannableStringBuilder(oldStr);
                        SpannableStringBuilder newSpannableStr = new SpannableStringBuilder(newStr);
                        SpannableStringBuilder stableStr = new SpannableStringBuilder(newStr);
                        for (int i2 = 0; i2 < oldStr.length(); i2++) {
                            if (oldStr.charAt(i2) == newStr.charAt(i2)) {
                                oldSpannableStr.setSpan(new EmptyStubSpan(), i2, i2 + 1, 0);
                                newSpannableStr.setSpan(new EmptyStubSpan(), i2, i2 + 1, 0);
                            } else {
                                stableStr.setSpan(new EmptyStubSpan(), i2, i2 + 1, 0);
                            }
                        }
                        int countOldWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(oldStr)));
                        this.countOldLayout = new StaticLayout(oldSpannableStr, Theme.dialogs_countTextPaint, countOldWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                        this.countAnimationStableLayout = new StaticLayout(stableStr, Theme.dialogs_countTextPaint, countOldWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                        this.countAnimationInLayout = new StaticLayout(newSpannableStr, Theme.dialogs_countTextPaint, countOldWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                    }
                }
                this.countWidthOld = this.countWidth;
                this.countLeftOld = this.countLeft;
                this.countAnimationIncrement = this.unreadCount > oldUnreadCount;
                this.countAnimator.start();
            }
            boolean newHasReactionsMentions = this.reactionMentionCount != 0;
            if (animated && newHasReactionsMentions != oldHasReactionsMentions) {
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
                        DialogCell.this.m1643lambda$update$1$orgtelegramuiCellsDialogCell(valueAnimator3);
                    }
                });
                this.reactionsMentionsAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animation) {
                        DialogCell.this.reactionsMentionsChangeProgress = 1.0f;
                        DialogCell.this.invalidate();
                    }
                });
                if (newHasReactionsMentions) {
                    this.reactionsMentionsAnimator.setDuration(220L);
                    this.reactionsMentionsAnimator.setInterpolator(new OvershootInterpolator());
                } else {
                    this.reactionsMentionsAnimator.setDuration(150L);
                    this.reactionsMentionsAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                }
                this.reactionsMentionsAnimator.start();
            }
        }
        if (getMeasuredWidth() != 0 || getMeasuredHeight() != 0) {
            buildLayout();
        } else {
            requestLayout();
        }
        if (!animated) {
            this.dialogMutedProgress = this.dialogMuted ? 1.0f : 0.0f;
            ValueAnimator valueAnimator3 = this.countAnimator;
            if (valueAnimator3 != null) {
                valueAnimator3.cancel();
            }
        }
        invalidate();
    }

    /* renamed from: lambda$update$0$org-telegram-ui-Cells-DialogCell */
    public /* synthetic */ void m1642lambda$update$0$orgtelegramuiCellsDialogCell(ValueAnimator valueAnimator) {
        this.countChangeProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    /* renamed from: lambda$update$1$org-telegram-ui-Cells-DialogCell */
    public /* synthetic */ void m1643lambda$update$1$orgtelegramuiCellsDialogCell(ValueAnimator valueAnimator) {
        this.reactionsMentionsChangeProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    @Override // android.view.View
    public float getTranslationX() {
        return this.translationX;
    }

    @Override // android.view.View
    public void setTranslationX(float value) {
        float f = (int) value;
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
            boolean prevValue = this.drawRevealBackground;
            if (Math.abs(f2) >= getMeasuredWidth() * 0.45f) {
                z = true;
            }
            this.drawRevealBackground = z;
            if (prevValue != z && this.archiveHidden == SharedConfig.archiveHidden) {
                try {
                    performHapticFeedback(3, 2);
                } catch (Exception e) {
                }
            }
        }
        invalidate();
    }

    /* JADX WARN: Code restructure failed: missing block: B:519:0x0eba, code lost:
        if (r34.reactionsMentionsChangeProgress != 1.0f) goto L521;
     */
    /* JADX WARN: Removed duplicated region for block: B:362:0x099a  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x099d  */
    /* JADX WARN: Removed duplicated region for block: B:366:0x09ae  */
    /* JADX WARN: Removed duplicated region for block: B:367:0x09e7  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        long dt;
        boolean needInvalidate;
        int backgroundColor;
        float f;
        int left;
        int left2;
        float size2;
        float f2;
        float size1;
        int left3;
        PullForegroundDrawable pullForegroundDrawable;
        int i;
        boolean z;
        StatusDrawable statusDrawable;
        CustomDialog customDialog;
        int backgroundColor2;
        int revealBackgroundColor;
        String swipeMessage;
        int backgroundColor3;
        int swipeMessageStringId;
        String swipeMessage2;
        String str;
        int revealBackgroundColor2;
        int i2;
        int swipeMessageStringId2;
        boolean z2;
        RLottieDrawable rLottieDrawable;
        PullForegroundDrawable pullForegroundDrawable2;
        if (this.currentDialogId == 0 && this.customDialog == null) {
            return;
        }
        if (this.currentDialogFolderId != 0 && (pullForegroundDrawable2 = this.archivedChatsDrawable) != null && pullForegroundDrawable2.outProgress == 0.0f && this.translationX == 0.0f) {
            if (!this.drawingForBlur) {
                canvas.save();
                canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                this.archivedChatsDrawable.draw(canvas);
                canvas.restore();
                return;
            }
            return;
        }
        long newTime = SystemClock.elapsedRealtime();
        long dt2 = newTime - this.lastUpdateTime;
        if (dt2 <= 17) {
            dt = dt2;
        } else {
            dt = 17;
        }
        this.lastUpdateTime = newTime;
        if (this.clipProgress != 0.0f && Build.VERSION.SDK_INT != 24) {
            canvas.save();
            canvas.clipRect(0.0f, this.topClip * this.clipProgress, getMeasuredWidth(), getMeasuredHeight() - ((int) (this.bottomClip * this.clipProgress)));
        }
        float f3 = 8.0f;
        if (this.translationX != 0.0f || this.cornerProgress != 0.0f) {
            canvas.save();
            if (this.currentDialogFolderId != 0) {
                if (this.archiveHidden) {
                    int backgroundColor4 = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                    int revealBackgroundColor3 = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                    swipeMessage = LocaleController.getString("UnhideFromTop", R.string.UnhideFromTop);
                    this.translationDrawable = Theme.dialogs_unpinArchiveDrawable;
                    backgroundColor2 = backgroundColor4;
                    revealBackgroundColor = revealBackgroundColor3;
                    backgroundColor3 = R.string.UnhideFromTop;
                } else {
                    int backgroundColor5 = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                    int revealBackgroundColor4 = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                    swipeMessage = LocaleController.getString("HideOnTop", R.string.HideOnTop);
                    this.translationDrawable = Theme.dialogs_pinArchiveDrawable;
                    backgroundColor2 = backgroundColor5;
                    revealBackgroundColor = revealBackgroundColor4;
                    backgroundColor3 = R.string.HideOnTop;
                }
            } else if (this.promoDialog) {
                int backgroundColor6 = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                int revealBackgroundColor5 = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                swipeMessage = LocaleController.getString("PsaHide", R.string.PsaHide);
                this.translationDrawable = Theme.dialogs_hidePsaDrawable;
                backgroundColor2 = backgroundColor6;
                revealBackgroundColor = revealBackgroundColor5;
                backgroundColor3 = R.string.PsaHide;
            } else if (this.folderId == 0) {
                int backgroundColor7 = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                int revealBackgroundColor6 = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                if (SharedConfig.getChatSwipeAction(this.currentAccount) == 3) {
                    if (this.dialogMuted) {
                        swipeMessage = LocaleController.getString("SwipeUnmute", R.string.SwipeUnmute);
                        this.translationDrawable = Theme.dialogs_swipeUnmuteDrawable;
                        backgroundColor2 = backgroundColor7;
                        revealBackgroundColor = revealBackgroundColor6;
                        backgroundColor3 = R.string.SwipeUnmute;
                    } else {
                        swipeMessage = LocaleController.getString("SwipeMute", R.string.SwipeMute);
                        this.translationDrawable = Theme.dialogs_swipeMuteDrawable;
                        backgroundColor2 = backgroundColor7;
                        revealBackgroundColor = revealBackgroundColor6;
                        backgroundColor3 = R.string.SwipeMute;
                    }
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 4) {
                    swipeMessage = LocaleController.getString("SwipeDeleteChat", R.string.SwipeDeleteChat);
                    int backgroundColor8 = Theme.getColor(Theme.key_dialogSwipeRemove, this.resourcesProvider);
                    this.translationDrawable = Theme.dialogs_swipeDeleteDrawable;
                    backgroundColor2 = backgroundColor8;
                    revealBackgroundColor = revealBackgroundColor6;
                    backgroundColor3 = R.string.SwipeDeleteChat;
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 1) {
                    if (this.unreadCount > 0 || this.markUnread) {
                        swipeMessage = LocaleController.getString("SwipeMarkAsRead", R.string.SwipeMarkAsRead);
                        this.translationDrawable = Theme.dialogs_swipeReadDrawable;
                        backgroundColor2 = backgroundColor7;
                        revealBackgroundColor = revealBackgroundColor6;
                        backgroundColor3 = R.string.SwipeMarkAsRead;
                    } else {
                        swipeMessage = LocaleController.getString("SwipeMarkAsUnread", R.string.SwipeMarkAsUnread);
                        this.translationDrawable = Theme.dialogs_swipeUnreadDrawable;
                        backgroundColor2 = backgroundColor7;
                        revealBackgroundColor = revealBackgroundColor6;
                        backgroundColor3 = R.string.SwipeMarkAsUnread;
                    }
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 0) {
                    if (this.drawPin) {
                        swipeMessage = LocaleController.getString("SwipeUnpin", R.string.SwipeUnpin);
                        this.translationDrawable = Theme.dialogs_swipeUnpinDrawable;
                        backgroundColor2 = backgroundColor7;
                        revealBackgroundColor = revealBackgroundColor6;
                        backgroundColor3 = R.string.SwipeUnpin;
                    } else {
                        swipeMessage = LocaleController.getString("SwipePin", R.string.SwipePin);
                        this.translationDrawable = Theme.dialogs_swipePinDrawable;
                        backgroundColor2 = backgroundColor7;
                        revealBackgroundColor = revealBackgroundColor6;
                        backgroundColor3 = R.string.SwipePin;
                    }
                } else {
                    swipeMessage = LocaleController.getString("Archive", R.string.Archive);
                    this.translationDrawable = Theme.dialogs_archiveDrawable;
                    backgroundColor2 = backgroundColor7;
                    revealBackgroundColor = revealBackgroundColor6;
                    backgroundColor3 = R.string.Archive;
                }
            } else {
                int backgroundColor9 = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                int revealBackgroundColor7 = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                swipeMessage = LocaleController.getString("Unarchive", R.string.Unarchive);
                this.translationDrawable = Theme.dialogs_unarchiveDrawable;
                backgroundColor2 = backgroundColor9;
                revealBackgroundColor = revealBackgroundColor7;
                backgroundColor3 = R.string.Unarchive;
            }
            if (!this.swipeCanceled || (rLottieDrawable = this.lastDrawTranslationDrawable) == null) {
                this.lastDrawTranslationDrawable = this.translationDrawable;
                this.lastDrawSwipeMessageStringId = backgroundColor3;
                swipeMessageStringId = backgroundColor3;
            } else {
                this.translationDrawable = rLottieDrawable;
                swipeMessageStringId = this.lastDrawSwipeMessageStringId;
            }
            if (!this.translationAnimationStarted && Math.abs(this.translationX) > AndroidUtilities.dp(43.0f)) {
                this.translationAnimationStarted = true;
                this.translationDrawable.setProgress(0.0f);
                this.translationDrawable.setCallback(this);
                this.translationDrawable.start();
            }
            float tx = this.translationX + getMeasuredWidth();
            if (this.currentRevealProgress >= 1.0f) {
                needInvalidate = false;
                swipeMessageStringId2 = swipeMessageStringId;
                str = Theme.key_chats_archivePinBackground;
                swipeMessage2 = swipeMessage;
                i2 = 2;
                revealBackgroundColor2 = revealBackgroundColor;
            } else {
                Theme.dialogs_pinnedPaint.setColor(backgroundColor2);
                Paint paint = Theme.dialogs_pinnedPaint;
                needInvalidate = false;
                swipeMessageStringId2 = swipeMessageStringId;
                str = Theme.key_chats_archivePinBackground;
                swipeMessage2 = swipeMessage;
                i2 = 2;
                revealBackgroundColor2 = revealBackgroundColor;
                canvas.drawRect(tx - AndroidUtilities.dp(8.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight(), paint);
                if (this.currentRevealProgress == 0.0f) {
                    if (Theme.dialogs_archiveDrawableRecolored) {
                        Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor(Theme.key_chats_archiveBackground));
                        Theme.dialogs_archiveDrawableRecolored = false;
                    }
                    if (Theme.dialogs_hidePsaDrawableRecolored) {
                        Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(Theme.key_chats_archiveBackground));
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(Theme.key_chats_archiveBackground));
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(Theme.key_chats_archiveBackground));
                        Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                        Theme.dialogs_hidePsaDrawableRecolored = false;
                    }
                }
            }
            int drawableX = (getMeasuredWidth() - AndroidUtilities.dp(43.0f)) - (this.translationDrawable.getIntrinsicWidth() / i2);
            int drawableY = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.0f : 9.0f);
            int drawableCx = (this.translationDrawable.getIntrinsicWidth() / i2) + drawableX;
            int drawableCy = (this.translationDrawable.getIntrinsicHeight() / i2) + drawableY;
            if (this.currentRevealProgress > 0.0f) {
                canvas.save();
                canvas.clipRect(tx - AndroidUtilities.dp(8.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                Theme.dialogs_pinnedPaint.setColor(revealBackgroundColor2);
                float rad = (float) Math.sqrt((drawableCx * drawableCx) + ((drawableCy - getMeasuredHeight()) * (drawableCy - getMeasuredHeight())));
                canvas.drawCircle(drawableCx, drawableCy, AndroidUtilities.accelerateInterpolator.getInterpolation(this.currentRevealProgress) * rad, Theme.dialogs_pinnedPaint);
                canvas.restore();
                if (Theme.dialogs_archiveDrawableRecolored) {
                    z2 = true;
                } else {
                    Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor(str));
                    z2 = true;
                    Theme.dialogs_archiveDrawableRecolored = true;
                }
                if (!Theme.dialogs_hidePsaDrawableRecolored) {
                    Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(str));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(str));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(str));
                    Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                    Theme.dialogs_hidePsaDrawableRecolored = z2;
                }
            }
            canvas.save();
            canvas.translate(drawableX, drawableY);
            float f4 = this.currentRevealBounceProgress;
            if (f4 != 0.0f && f4 != 1.0f) {
                float scale = this.interpolator.getInterpolation(f4) + 1.0f;
                canvas.scale(scale, scale, this.translationDrawable.getIntrinsicWidth() / 2, this.translationDrawable.getIntrinsicHeight() / 2);
            }
            setDrawableBounds((Drawable) this.translationDrawable, 0, 0);
            this.translationDrawable.draw(canvas);
            canvas.restore();
            canvas.clipRect(tx, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            String swipeMessage3 = swipeMessage2;
            int width = (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(swipeMessage3));
            if (this.swipeMessageTextId != swipeMessageStringId2 || this.swipeMessageWidth != getMeasuredWidth()) {
                this.swipeMessageTextId = swipeMessageStringId2;
                this.swipeMessageWidth = getMeasuredWidth();
                StaticLayout staticLayout = new StaticLayout(swipeMessage3, Theme.dialogs_archiveTextPaint, Math.min(AndroidUtilities.dp(80.0f), width), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                this.swipeMessageTextLayout = staticLayout;
                if (staticLayout.getLineCount() > 1) {
                    this.swipeMessageTextLayout = new StaticLayout(swipeMessage3, Theme.dialogs_archiveTextPaintSmall, Math.min(AndroidUtilities.dp(82.0f), width), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                }
            }
            if (this.swipeMessageTextLayout != null) {
                canvas.save();
                float yOffset = this.swipeMessageTextLayout.getLineCount() > 1 ? -AndroidUtilities.dp(4.0f) : 0.0f;
                canvas.translate((getMeasuredWidth() - AndroidUtilities.dp(43.0f)) - (this.swipeMessageTextLayout.getWidth() / 2.0f), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 50.0f : 47.0f) + yOffset);
                this.swipeMessageTextLayout.draw(canvas);
                canvas.restore();
            }
            canvas.restore();
            backgroundColor = backgroundColor2;
        } else {
            RLottieDrawable rLottieDrawable2 = this.translationDrawable;
            if (rLottieDrawable2 != null) {
                rLottieDrawable2.stop();
                this.translationDrawable.setProgress(0.0f);
                this.translationDrawable.setCallback(null);
                this.translationDrawable = null;
                this.translationAnimationStarted = false;
            }
            needInvalidate = false;
            backgroundColor = 0;
        }
        if (this.translationX != 0.0f) {
            canvas.save();
            canvas.translate(this.translationX, 0.0f);
        }
        float cornersRadius = AndroidUtilities.dp(8.0f) * this.cornerProgress;
        if (this.isSelected) {
            this.rect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            canvas.drawRoundRect(this.rect, cornersRadius, cornersRadius, Theme.dialogs_tabletSeletedPaint);
        }
        if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != 0.0f)) {
            Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider), this.archiveBackgroundProgress, 1.0f));
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
        } else if (this.drawPin || this.drawPinBackground) {
            Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider));
            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
        }
        if (this.translationX != 0.0f || this.cornerProgress != 0.0f) {
            canvas.save();
            Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
            this.rect.set(getMeasuredWidth() - AndroidUtilities.dp(64.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
            canvas.drawRoundRect(this.rect, cornersRadius, cornersRadius, Theme.dialogs_pinnedPaint);
            if (this.isSelected) {
                canvas.drawRoundRect(this.rect, cornersRadius, cornersRadius, Theme.dialogs_tabletSeletedPaint);
            }
            if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != 0.0f)) {
                Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider), this.archiveBackgroundProgress, 1.0f));
                canvas.drawRoundRect(this.rect, cornersRadius, cornersRadius, Theme.dialogs_pinnedPaint);
            } else if (this.drawPin || this.drawPinBackground) {
                Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider));
                canvas.drawRoundRect(this.rect, cornersRadius, cornersRadius, Theme.dialogs_pinnedPaint);
            }
            canvas.restore();
        }
        if (this.translationX != 0.0f) {
            float f5 = this.cornerProgress;
            if (f5 < 1.0f) {
                float f6 = f5 + (((float) dt) / 150.0f);
                this.cornerProgress = f6;
                if (f6 > 1.0f) {
                    this.cornerProgress = 1.0f;
                }
                needInvalidate = true;
            }
        } else {
            float f7 = this.cornerProgress;
            if (f7 > 0.0f) {
                float f8 = f7 - (((float) dt) / 150.0f);
                this.cornerProgress = f8;
                if (f8 < 0.0f) {
                    this.cornerProgress = 0.0f;
                }
                needInvalidate = true;
            }
        }
        if (this.drawNameLock) {
            setDrawableBounds(Theme.dialogs_lockDrawable, this.nameLockLeft, this.nameLockTop);
            Theme.dialogs_lockDrawable.draw(canvas);
        }
        float f9 = 10.0f;
        if (this.nameLayout != null) {
            if (this.currentDialogFolderId != 0) {
                TextPaint textPaint = Theme.dialogs_namePaint[this.paintIndex];
                TextPaint textPaint2 = Theme.dialogs_namePaint[this.paintIndex];
                int color = Theme.getColor(Theme.key_chats_nameArchived, this.resourcesProvider);
                textPaint2.linkColor = color;
                textPaint.setColor(color);
            } else if (this.encryptedChat != null || ((customDialog = this.customDialog) != null && customDialog.type == 2)) {
                TextPaint textPaint3 = Theme.dialogs_namePaint[this.paintIndex];
                TextPaint textPaint4 = Theme.dialogs_namePaint[this.paintIndex];
                int color2 = Theme.getColor(Theme.key_chats_secretName, this.resourcesProvider);
                textPaint4.linkColor = color2;
                textPaint3.setColor(color2);
            } else {
                TextPaint textPaint5 = Theme.dialogs_namePaint[this.paintIndex];
                TextPaint textPaint6 = Theme.dialogs_namePaint[this.paintIndex];
                int color3 = Theme.getColor(Theme.key_chats_name, this.resourcesProvider);
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
                TextPaint textPaint8 = Theme.dialogs_messageNamePaint;
                int color4 = Theme.getColor(Theme.key_chats_nameMessageArchived_threeLines, this.resourcesProvider);
                textPaint8.linkColor = color4;
                textPaint7.setColor(color4);
            } else if (this.draftMessage != null) {
                TextPaint textPaint9 = Theme.dialogs_messageNamePaint;
                TextPaint textPaint10 = Theme.dialogs_messageNamePaint;
                int color5 = Theme.getColor(Theme.key_chats_draft, this.resourcesProvider);
                textPaint10.linkColor = color5;
                textPaint9.setColor(color5);
            } else {
                TextPaint textPaint11 = Theme.dialogs_messageNamePaint;
                TextPaint textPaint12 = Theme.dialogs_messageNamePaint;
                int color6 = Theme.getColor(Theme.key_chats_nameMessage_threeLines, this.resourcesProvider);
                textPaint12.linkColor = color6;
                textPaint11.setColor(color6);
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
            if (this.currentDialogFolderId == 0) {
                TextPaint textPaint13 = Theme.dialogs_messagePaint[this.paintIndex];
                TextPaint textPaint14 = Theme.dialogs_messagePaint[this.paintIndex];
                int color7 = Theme.getColor(Theme.key_chats_message, this.resourcesProvider);
                textPaint14.linkColor = color7;
                textPaint13.setColor(color7);
            } else if (this.chat != null) {
                TextPaint textPaint15 = Theme.dialogs_messagePaint[this.paintIndex];
                TextPaint textPaint16 = Theme.dialogs_messagePaint[this.paintIndex];
                int color8 = Theme.getColor(Theme.key_chats_nameMessageArchived, this.resourcesProvider);
                textPaint16.linkColor = color8;
                textPaint15.setColor(color8);
            } else {
                TextPaint textPaint17 = Theme.dialogs_messagePaint[this.paintIndex];
                TextPaint textPaint18 = Theme.dialogs_messagePaint[this.paintIndex];
                int color9 = Theme.getColor(Theme.key_chats_messageArchived, this.resourcesProvider);
                textPaint18.linkColor = color9;
                textPaint17.setColor(color9);
            }
            canvas.save();
            canvas.translate(this.messageLeft, this.messageTop);
            if (!this.spoilers.isEmpty()) {
                try {
                    canvas.save();
                    SpoilerEffect.clipOutCanvas(canvas, this.spoilers);
                    this.messageLayout.draw(canvas);
                    canvas.restore();
                    for (int i3 = 0; i3 < this.spoilers.size(); i3++) {
                        SpoilerEffect eff = this.spoilers.get(i3);
                        eff.setColor(this.messageLayout.getPaint().getColor());
                        eff.draw(canvas);
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            } else {
                this.messageLayout.draw(canvas);
            }
            canvas.restore();
            int i4 = this.printingStringType;
            if (i4 >= 0 && (statusDrawable = Theme.getChatStatusDrawable(i4)) != null) {
                canvas.save();
                int i5 = this.printingStringType;
                if (i5 != 1 && i5 != 4) {
                    canvas.translate(this.statusDrawableLeft, this.messageTop + ((AndroidUtilities.dp(18.0f) - statusDrawable.getIntrinsicHeight()) / 2.0f));
                } else {
                    canvas.translate(this.statusDrawableLeft, this.messageTop + (i5 == 1 ? AndroidUtilities.dp(1.0f) : 0));
                }
                statusDrawable.draw(canvas);
                int i6 = this.statusDrawableLeft;
                invalidate(i6, this.messageTop, statusDrawable.getIntrinsicWidth() + i6, this.messageTop + statusDrawable.getIntrinsicHeight());
                canvas.restore();
            }
        }
        if (this.currentDialogFolderId == 0) {
            int currentStatus = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
            int i7 = this.lastStatusDrawableParams;
            if (i7 >= 0 && i7 != currentStatus && !this.statusDrawableAnimationInProgress) {
                createStatusDrawableAnimator(i7, currentStatus);
            }
            boolean z3 = this.statusDrawableAnimationInProgress;
            if (z3) {
                currentStatus = this.animateToStatusDrawableParams;
            }
            boolean drawClock = (currentStatus & 1) != 0;
            boolean drawCheck1 = (currentStatus & 2) != 0;
            boolean drawCheck2 = (currentStatus & 4) != 0;
            if (z3) {
                int i8 = this.animateFromStatusDrawableParams;
                boolean outDrawClock = (i8 & 1) != 0;
                boolean outDrawCheck1 = (i8 & 2) != 0;
                boolean outDrawCheck2 = (i8 & 4) != 0;
                if (!drawClock && !outDrawClock && outDrawCheck2 && !outDrawCheck1 && drawCheck1 && drawCheck2) {
                    drawCheckStatus(canvas, drawClock, drawCheck1, drawCheck2, true, this.statusDrawableProgress);
                } else {
                    drawCheckStatus(canvas, outDrawClock, outDrawCheck1, outDrawCheck2, false, 1.0f - this.statusDrawableProgress);
                    drawCheckStatus(canvas, drawClock, drawCheck1, drawCheck2, false, this.statusDrawableProgress);
                }
            } else {
                drawCheckStatus(canvas, drawClock, drawCheck1, drawCheck2, false, 1.0f);
            }
            this.lastStatusDrawableParams = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
        }
        if (this.dialogsType != 2 && (((z = this.dialogMuted) || this.dialogMutedProgress > 0.0f) && !this.drawVerified && this.drawScam == 0 && !this.drawPremium)) {
            if (z) {
                float f10 = this.dialogMutedProgress;
                if (f10 != 1.0f) {
                    float f11 = f10 + 0.10666667f;
                    this.dialogMutedProgress = f11;
                    if (f11 > 1.0f) {
                        this.dialogMutedProgress = 1.0f;
                    } else {
                        invalidate();
                    }
                    setDrawableBounds(Theme.dialogs_muteDrawable, this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f), AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f));
                    if (this.dialogMutedProgress == 1.0f) {
                        canvas.save();
                        float f12 = this.dialogMutedProgress;
                        canvas.scale(f12, f12, Theme.dialogs_muteDrawable.getBounds().centerX(), Theme.dialogs_muteDrawable.getBounds().centerY());
                        Theme.dialogs_muteDrawable.setAlpha((int) (this.dialogMutedProgress * 255.0f));
                        Theme.dialogs_muteDrawable.draw(canvas);
                        Theme.dialogs_muteDrawable.setAlpha(255);
                        canvas.restore();
                    } else {
                        Theme.dialogs_muteDrawable.draw(canvas);
                    }
                }
            }
            if (!z) {
                float f13 = this.dialogMutedProgress;
                if (f13 != 0.0f) {
                    float f14 = f13 - 0.10666667f;
                    this.dialogMutedProgress = f14;
                    if (f14 < 0.0f) {
                        this.dialogMutedProgress = 0.0f;
                    } else {
                        invalidate();
                    }
                }
            }
            setDrawableBounds(Theme.dialogs_muteDrawable, this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f), AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f));
            if (this.dialogMutedProgress == 1.0f) {
            }
        } else if (this.drawVerified) {
            setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft - AndroidUtilities.dp(1.0f), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
            setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft - AndroidUtilities.dp(1.0f), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
            Theme.dialogs_verifiedDrawable.draw(canvas);
            Theme.dialogs_verifiedCheckDrawable.draw(canvas);
        } else if (this.drawPremium) {
            Drawable premiumDrawable = PremiumGradient.getInstance().premiumStarDrawableMini;
            setDrawableBounds(premiumDrawable, this.nameMuteLeft - AndroidUtilities.dp(1.0f), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f));
            premiumDrawable.draw(canvas);
        } else {
            int i9 = this.drawScam;
            if (i9 != 0) {
                setDrawableBounds((Drawable) (i9 == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable), this.nameMuteLeft, AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.0f : 15.0f));
                (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).draw(canvas);
            }
        }
        if (this.drawReorder || this.reorderIconProgress != 0.0f) {
            Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
            setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
            Theme.dialogs_reorderDrawable.draw(canvas);
        }
        if (this.drawError) {
            Theme.dialogs_errorDrawable.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
            this.rect.set(this.errorLeft, this.errorTop, i + AndroidUtilities.dp(23.0f), this.errorTop + AndroidUtilities.dp(23.0f));
            canvas.drawRoundRect(this.rect, AndroidUtilities.density * 11.5f, AndroidUtilities.density * 11.5f, Theme.dialogs_errorPaint);
            setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
            Theme.dialogs_errorDrawable.draw(canvas);
        } else {
            boolean z4 = this.drawCount;
            if (((z4 || this.drawMention) && this.drawCount2) || this.countChangeProgress != 1.0f || this.drawReactionMention || this.reactionsMentionsChangeProgress != 1.0f) {
                if ((z4 && this.drawCount2) || this.countChangeProgress != 1.0f) {
                    int i10 = this.unreadCount;
                    float progressFinal = (i10 != 0 || this.markUnread) ? this.countChangeProgress : 1.0f - this.countChangeProgress;
                    StaticLayout staticLayout2 = this.countOldLayout;
                    if (staticLayout2 == null || i10 == 0) {
                        if (i10 != 0) {
                            staticLayout2 = this.countLayout;
                        }
                        StaticLayout drawLayout = staticLayout2;
                        Paint paint2 = (this.dialogMuted || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
                        paint2.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                        Theme.dialogs_countTextPaint.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                        int x = this.countLeft - AndroidUtilities.dp(5.5f);
                        this.rect.set(x, this.countTop, this.countWidth + x + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                        if (progressFinal != 1.0f) {
                            if (this.drawPin) {
                                Theme.dialogs_pinnedDrawable.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                                setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                                canvas.save();
                                canvas.scale(1.0f - progressFinal, 1.0f - progressFinal, Theme.dialogs_pinnedDrawable.getBounds().centerX(), Theme.dialogs_pinnedDrawable.getBounds().centerY());
                                Theme.dialogs_pinnedDrawable.draw(canvas);
                                canvas.restore();
                            }
                            canvas.save();
                            canvas.scale(progressFinal, progressFinal, this.rect.centerX(), this.rect.centerY());
                        }
                        canvas.drawRoundRect(this.rect, AndroidUtilities.density * 11.5f, AndroidUtilities.density * 11.5f, paint2);
                        if (drawLayout != null) {
                            canvas.save();
                            canvas.translate(this.countLeft, this.countTop + AndroidUtilities.dp(4.0f));
                            drawLayout.draw(canvas);
                            canvas.restore();
                        }
                        if (progressFinal != 1.0f) {
                            canvas.restore();
                        }
                    } else {
                        Paint paint3 = (this.dialogMuted || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
                        paint3.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                        Theme.dialogs_countTextPaint.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                        float progressHalf = progressFinal * 2.0f;
                        if (progressHalf > 1.0f) {
                            progressHalf = 1.0f;
                        }
                        float countLeft = (this.countLeft * progressHalf) + (this.countLeftOld * (1.0f - progressHalf));
                        float x2 = countLeft - AndroidUtilities.dp(5.5f);
                        this.rect.set(x2, this.countTop, (this.countWidth * progressHalf) + x2 + (this.countWidthOld * (1.0f - progressHalf)) + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                        float scale2 = progressFinal <= 0.5f ? 1.0f + (CubicBezierInterpolator.EASE_OUT.getInterpolation(progressFinal * 2.0f) * 0.1f) : 1.0f + (CubicBezierInterpolator.EASE_IN.getInterpolation(1.0f - ((progressFinal - 0.5f) * 2.0f)) * 0.1f);
                        canvas.save();
                        canvas.scale(scale2, scale2, this.rect.centerX(), this.rect.centerY());
                        canvas.drawRoundRect(this.rect, AndroidUtilities.density * 11.5f, AndroidUtilities.density * 11.5f, paint3);
                        if (this.countAnimationStableLayout != null) {
                            canvas.save();
                            canvas.translate(countLeft, this.countTop + AndroidUtilities.dp(4.0f));
                            this.countAnimationStableLayout.draw(canvas);
                            canvas.restore();
                        }
                        int textAlpha = Theme.dialogs_countTextPaint.getAlpha();
                        Theme.dialogs_countTextPaint.setAlpha((int) (textAlpha * progressHalf));
                        if (this.countAnimationInLayout != null) {
                            canvas.save();
                            canvas.translate(countLeft, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * (1.0f - progressHalf)) + this.countTop + AndroidUtilities.dp(4.0f));
                            this.countAnimationInLayout.draw(canvas);
                            canvas.restore();
                        } else if (this.countLayout != null) {
                            canvas.save();
                            canvas.translate(countLeft, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * (1.0f - progressHalf)) + this.countTop + AndroidUtilities.dp(4.0f));
                            this.countLayout.draw(canvas);
                            canvas.restore();
                        }
                        if (this.countOldLayout != null) {
                            Theme.dialogs_countTextPaint.setAlpha((int) (textAlpha * (1.0f - progressHalf)));
                            canvas.save();
                            canvas.translate(countLeft, ((this.countAnimationIncrement ? -AndroidUtilities.dp(13.0f) : AndroidUtilities.dp(13.0f)) * progressHalf) + this.countTop + AndroidUtilities.dp(4.0f));
                            this.countOldLayout.draw(canvas);
                            canvas.restore();
                        }
                        Theme.dialogs_countTextPaint.setAlpha(textAlpha);
                        canvas.restore();
                    }
                }
                if (this.drawMention) {
                    Theme.dialogs_countPaint.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                    int x3 = this.mentionLeft - AndroidUtilities.dp(5.5f);
                    this.rect.set(x3, this.countTop, this.mentionWidth + x3 + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                    Paint paint4 = (!this.dialogMuted || this.folderId == 0) ? Theme.dialogs_countPaint : Theme.dialogs_countGrayPaint;
                    canvas.drawRoundRect(this.rect, AndroidUtilities.density * 11.5f, AndroidUtilities.density * 11.5f, paint4);
                    if (this.mentionLayout != null) {
                        Theme.dialogs_countTextPaint.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                        canvas.save();
                        canvas.translate(this.mentionLeft, this.countTop + AndroidUtilities.dp(4.0f));
                        this.mentionLayout.draw(canvas);
                        canvas.restore();
                    } else {
                        Theme.dialogs_mentionDrawable.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                        setDrawableBounds(Theme.dialogs_mentionDrawable, this.mentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.2f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                        Theme.dialogs_mentionDrawable.draw(canvas);
                    }
                }
                float f15 = !this.drawReactionMention ? 1.0f : 1.0f;
                Theme.dialogs_reactionsCountPaint.setAlpha((int) ((f15 - this.reorderIconProgress) * 255.0f));
                int x4 = this.reactionMentionLeft - AndroidUtilities.dp(5.5f);
                this.rect.set(x4, this.countTop, AndroidUtilities.dp(23.0f) + x4, this.countTop + AndroidUtilities.dp(23.0f));
                Paint paint5 = Theme.dialogs_reactionsCountPaint;
                canvas.save();
                float s = this.reactionsMentionsChangeProgress;
                if (s != 1.0f) {
                    if (!this.drawReactionMention) {
                        s = 1.0f - s;
                    }
                    canvas.scale(s, s, this.rect.centerX(), this.rect.centerY());
                }
                canvas.drawRoundRect(this.rect, AndroidUtilities.density * 11.5f, AndroidUtilities.density * 11.5f, paint5);
                Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                Theme.dialogs_reactionsMentionDrawable.draw(canvas);
                canvas.restore();
            } else if (this.drawPin) {
                Theme.dialogs_pinnedDrawable.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                Theme.dialogs_pinnedDrawable.draw(canvas);
            }
        }
        if (this.animatingArchiveAvatar) {
            canvas.save();
            float scale3 = this.interpolator.getInterpolation(this.animatingArchiveAvatarProgress / 170.0f) + 1.0f;
            canvas.scale(scale3, scale3, this.avatarImage.getCenterX(), this.avatarImage.getCenterY());
        }
        if (this.currentDialogFolderId == 0 || (pullForegroundDrawable = this.archivedChatsDrawable) == null || !pullForegroundDrawable.isDraw()) {
            this.avatarImage.draw(canvas);
        }
        if (this.hasMessageThumb) {
            this.thumbImage.draw(canvas);
            if (this.drawPlay) {
                int y = (int) (this.thumbImage.getCenterY() - (Theme.dialogs_playDrawable.getIntrinsicHeight() / 2));
                setDrawableBounds(Theme.dialogs_playDrawable, (int) (this.thumbImage.getCenterX() - (Theme.dialogs_playDrawable.getIntrinsicWidth() / 2)), y);
                Theme.dialogs_playDrawable.draw(canvas);
            }
        }
        if (this.animatingArchiveAvatar) {
            canvas.restore();
        }
        if (this.isDialogCell && this.currentDialogFolderId == 0) {
            TLRPC.User user = this.user;
            if (user == null || MessagesController.isSupportUser(user) || this.user.bot) {
                TLRPC.Chat chat = this.chat;
                if (chat != null) {
                    boolean z5 = chat.call_active && this.chat.call_not_empty;
                    this.hasCall = z5;
                    if (z5 || this.chatCallProgress != 0.0f) {
                        CheckBox2 checkBox2 = this.checkBox;
                        float checkProgress = (checkBox2 == null || !checkBox2.isChecked()) ? 1.0f : 1.0f - this.checkBox.getProgress();
                        float imageY2 = this.avatarImage.getImageY2();
                        if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                            f3 = 6.0f;
                        }
                        int top = (int) (imageY2 - AndroidUtilities.dp(f3));
                        if (LocaleController.isRTL) {
                            float imageX = this.avatarImage.getImageX();
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                f9 = 6.0f;
                            }
                            left2 = (int) (imageX + AndroidUtilities.dp(f9));
                        } else {
                            float imageX2 = this.avatarImage.getImageX2();
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                f9 = 6.0f;
                            }
                            left2 = (int) (imageX2 - AndroidUtilities.dp(f9));
                        }
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                        canvas.drawCircle(left2, top, AndroidUtilities.dp(11.0f) * this.chatCallProgress * checkProgress, Theme.dialogs_onlineCirclePaint);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_chats_onlineCircle, this.resourcesProvider));
                        canvas.drawCircle(left2, top, AndroidUtilities.dp(9.0f) * this.chatCallProgress * checkProgress, Theme.dialogs_onlineCirclePaint);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                        int i11 = this.progressStage;
                        if (i11 == 0) {
                            size1 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            size2 = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(2.0f) * this.innerProgress);
                            f2 = 1.0f;
                        } else if (i11 == 1) {
                            size1 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            f2 = 1.0f;
                            size2 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                        } else if (i11 == 2) {
                            size1 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(2.0f) * this.innerProgress);
                            size2 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            f2 = 1.0f;
                        } else if (i11 == 3) {
                            size1 = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(2.0f) * this.innerProgress);
                            f2 = 1.0f;
                            size2 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(2.0f) * this.innerProgress);
                        } else if (i11 == 4) {
                            size1 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            size2 = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(2.0f) * this.innerProgress);
                            f2 = 1.0f;
                        } else if (i11 == 5) {
                            size1 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            size2 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            f2 = 1.0f;
                        } else if (i11 == 6) {
                            size1 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            size2 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            f2 = 1.0f;
                        } else {
                            size1 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            f2 = 1.0f;
                            size2 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(2.0f) * this.innerProgress);
                        }
                        if (this.chatCallProgress < f2 || checkProgress < f2) {
                            canvas.save();
                            float f16 = this.chatCallProgress;
                            canvas.scale(f16 * checkProgress, f16 * checkProgress, left2, top);
                        }
                        this.rect.set(left2 - AndroidUtilities.dp(1.0f), top - size1, left2 + AndroidUtilities.dp(1.0f), top + size1);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        this.rect.set(left2 - AndroidUtilities.dp(5.0f), top - size2, left2 - AndroidUtilities.dp(3.0f), top + size2);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        this.rect.set(AndroidUtilities.dp(3.0f) + left2, top - size2, AndroidUtilities.dp(5.0f) + left2, top + size2);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        if (this.chatCallProgress < 1.0f || checkProgress < 1.0f) {
                            canvas.restore();
                        }
                        float f17 = this.innerProgress + (((float) dt) / 400.0f);
                        this.innerProgress = f17;
                        if (f17 >= 1.0f) {
                            this.innerProgress = 0.0f;
                            int i12 = this.progressStage + 1;
                            this.progressStage = i12;
                            if (i12 >= 8) {
                                this.progressStage = 0;
                            }
                        }
                        needInvalidate = true;
                        if (this.hasCall) {
                            float f18 = this.chatCallProgress;
                            if (f18 < 1.0f) {
                                float f19 = f18 + (((float) dt) / 150.0f);
                                this.chatCallProgress = f19;
                                if (f19 > 1.0f) {
                                    this.chatCallProgress = 1.0f;
                                }
                            }
                        } else {
                            float f20 = this.chatCallProgress;
                            if (f20 > 0.0f) {
                                float f21 = f20 - (((float) dt) / 150.0f);
                                this.chatCallProgress = f21;
                                if (f21 < 0.0f) {
                                    this.chatCallProgress = 0.0f;
                                }
                            }
                        }
                    }
                }
            } else {
                boolean isOnline = isOnline();
                if (isOnline || this.onlineProgress != 0.0f) {
                    float imageY22 = this.avatarImage.getImageY2();
                    if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                        f3 = 6.0f;
                    }
                    int top2 = (int) (imageY22 - AndroidUtilities.dp(f3));
                    if (LocaleController.isRTL) {
                        float imageX3 = this.avatarImage.getImageX();
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f9 = 6.0f;
                        }
                        left3 = (int) (imageX3 + AndroidUtilities.dp(f9));
                    } else {
                        float imageX22 = this.avatarImage.getImageX2();
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f9 = 6.0f;
                        }
                        left3 = (int) (imageX22 - AndroidUtilities.dp(f9));
                    }
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                    canvas.drawCircle(left3, top2, AndroidUtilities.dp(7.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_chats_onlineCircle, this.resourcesProvider));
                    canvas.drawCircle(left3, top2, AndroidUtilities.dp(5.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    if (isOnline) {
                        float f22 = this.onlineProgress;
                        if (f22 < 1.0f) {
                            float f23 = f22 + (((float) dt) / 150.0f);
                            this.onlineProgress = f23;
                            if (f23 > 1.0f) {
                                this.onlineProgress = 1.0f;
                            }
                            needInvalidate = true;
                        }
                    } else {
                        float f24 = this.onlineProgress;
                        if (f24 > 0.0f) {
                            float f25 = f24 - (((float) dt) / 150.0f);
                            this.onlineProgress = f25;
                            if (f25 < 0.0f) {
                                this.onlineProgress = 0.0f;
                            }
                            needInvalidate = true;
                        }
                    }
                }
            }
        }
        if (this.translationX != 0.0f) {
            canvas.restore();
        }
        if (this.currentDialogFolderId != 0 && this.translationX == 0.0f && this.archivedChatsDrawable != null) {
            canvas.save();
            canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
            this.archivedChatsDrawable.draw(canvas);
            canvas.restore();
        }
        if (this.useSeparator) {
            if (this.fullSeparator || ((this.currentDialogFolderId != 0 && this.archiveHidden && !this.fullSeparator2) || (this.fullSeparator2 && !this.archiveHidden))) {
                left = 0;
            } else {
                left = AndroidUtilities.dp(72.0f);
            }
            if (LocaleController.isRTL) {
                canvas.drawLine(0.0f, getMeasuredHeight() - 1, getMeasuredWidth() - left, getMeasuredHeight() - 1, Theme.dividerPaint);
            } else {
                canvas.drawLine(left, getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
            }
        }
        if (this.clipProgress != 0.0f) {
            if (Build.VERSION.SDK_INT == 24) {
                Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), this.topClip * this.clipProgress, Theme.dialogs_pinnedPaint);
                canvas.drawRect(0.0f, getMeasuredHeight() - ((int) (this.bottomClip * this.clipProgress)), getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
            } else {
                canvas.restore();
            }
        }
        boolean z6 = this.drawReorder;
        if (z6 || this.reorderIconProgress != 0.0f) {
            if (z6) {
                float f26 = this.reorderIconProgress;
                if (f26 < 1.0f) {
                    float f27 = f26 + (((float) dt) / 170.0f);
                    this.reorderIconProgress = f27;
                    if (f27 > 1.0f) {
                        this.reorderIconProgress = 1.0f;
                    }
                    needInvalidate = true;
                }
            } else {
                float f28 = this.reorderIconProgress;
                if (f28 > 0.0f) {
                    float f29 = f28 - (((float) dt) / 170.0f);
                    this.reorderIconProgress = f29;
                    if (f29 < 0.0f) {
                        this.reorderIconProgress = 0.0f;
                    }
                    needInvalidate = true;
                }
            }
        }
        if (this.archiveHidden) {
            float f30 = this.archiveBackgroundProgress;
            if (f30 > 0.0f) {
                float f31 = f30 - (((float) dt) / 230.0f);
                this.archiveBackgroundProgress = f31;
                if (f31 < 0.0f) {
                    this.archiveBackgroundProgress = 0.0f;
                }
                if (this.avatarDrawable.getAvatarType() == 2) {
                    this.avatarDrawable.setArchivedAvatarHiddenProgress(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.archiveBackgroundProgress));
                }
                needInvalidate = true;
            }
        } else {
            float f32 = this.archiveBackgroundProgress;
            if (f32 < 1.0f) {
                float f33 = f32 + (((float) dt) / 230.0f);
                this.archiveBackgroundProgress = f33;
                if (f33 > 1.0f) {
                    this.archiveBackgroundProgress = 1.0f;
                }
                if (this.avatarDrawable.getAvatarType() == 2) {
                    this.avatarDrawable.setArchivedAvatarHiddenProgress(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.archiveBackgroundProgress));
                }
                needInvalidate = true;
            }
        }
        if (this.animatingArchiveAvatar) {
            float f34 = this.animatingArchiveAvatarProgress + ((float) dt);
            this.animatingArchiveAvatarProgress = f34;
            if (f34 >= 170.0f) {
                this.animatingArchiveAvatarProgress = 170.0f;
                this.animatingArchiveAvatar = false;
            }
            needInvalidate = true;
        }
        if (this.drawRevealBackground) {
            float f35 = this.currentRevealBounceProgress;
            if (f35 < 1.0f) {
                float f36 = f35 + (((float) dt) / 170.0f);
                this.currentRevealBounceProgress = f36;
                if (f36 > 1.0f) {
                    this.currentRevealBounceProgress = 1.0f;
                    needInvalidate = true;
                }
            }
            float f37 = this.currentRevealProgress;
            if (f37 < 1.0f) {
                float f38 = f37 + (((float) dt) / 300.0f);
                this.currentRevealProgress = f38;
                if (f38 > 1.0f) {
                    this.currentRevealProgress = 1.0f;
                }
                needInvalidate = true;
            }
        } else {
            if (this.currentRevealBounceProgress != 1.0f) {
                f = 0.0f;
            } else {
                f = 0.0f;
                this.currentRevealBounceProgress = 0.0f;
                needInvalidate = true;
            }
            float f39 = this.currentRevealProgress;
            if (f39 > f) {
                float f40 = f39 - (((float) dt) / 300.0f);
                this.currentRevealProgress = f40;
                if (f40 < f) {
                    this.currentRevealProgress = f;
                }
                needInvalidate = true;
            }
        }
        if (needInvalidate) {
            invalidate();
        }
    }

    public void createStatusDrawableAnimator(int lastStatusDrawableParams, int currentStatus) {
        this.statusDrawableProgress = 0.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.statusDrawableAnimator = ofFloat;
        ofFloat.setDuration(220L);
        this.statusDrawableAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.animateFromStatusDrawableParams = lastStatusDrawableParams;
        this.animateToStatusDrawableParams = currentStatus;
        this.statusDrawableAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                DialogCell.this.m1641x2a941262(valueAnimator);
            }
        });
        this.statusDrawableAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                int currentStatus2 = (DialogCell.this.drawClock ? 1 : 0) + (DialogCell.this.drawCheck1 ? 2 : 0) + (DialogCell.this.drawCheck2 ? 4 : 0);
                if (DialogCell.this.animateToStatusDrawableParams == currentStatus2) {
                    DialogCell.this.statusDrawableAnimationInProgress = false;
                    DialogCell dialogCell = DialogCell.this;
                    dialogCell.lastStatusDrawableParams = dialogCell.animateToStatusDrawableParams;
                } else {
                    DialogCell dialogCell2 = DialogCell.this;
                    dialogCell2.createStatusDrawableAnimator(dialogCell2.animateToStatusDrawableParams, currentStatus2);
                }
                DialogCell.this.invalidate();
            }
        });
        this.statusDrawableAnimationInProgress = true;
        this.statusDrawableAnimator.start();
    }

    /* renamed from: lambda$createStatusDrawableAnimator$2$org-telegram-ui-Cells-DialogCell */
    public /* synthetic */ void m1641x2a941262(ValueAnimator valueAnimator) {
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

    public void onReorderStateChanged(boolean reordering, boolean animated) {
        boolean z = this.drawPin;
        if ((!z && reordering) || this.drawReorder == reordering) {
            if (!z) {
                this.drawReorder = false;
                return;
            }
            return;
        }
        this.drawReorder = reordering;
        float f = 1.0f;
        if (animated) {
            if (reordering) {
                f = 0.0f;
            }
            this.reorderIconProgress = f;
        } else {
            if (!reordering) {
                f = 0.0f;
            }
            this.reorderIconProgress = f;
        }
        invalidate();
    }

    public void setSliding(boolean value) {
        this.isSliding = value;
    }

    @Override // android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable who) {
        if (who == this.translationDrawable || who == Theme.dialogs_archiveAvatarDrawable) {
            invalidate(who.getBounds());
        } else {
            super.invalidateDrawable(who);
        }
    }

    @Override // org.telegram.ui.Cells.BaseCell, android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        DialogsActivity dialogsActivity;
        if (action == R.id.acc_action_chat_preview && (dialogsActivity = this.parentFragment) != null) {
            dialogsActivity.showChatPreview(this);
            return true;
        }
        return super.performAccessibilityAction(action, arguments);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        if (isFolderCell() && this.archivedChatsDrawable != null && SharedConfig.archiveHidden && this.archivedChatsDrawable.pullProgress == 0.0f) {
            info.setVisibleToUser(false);
        } else {
            info.addAction(16);
            info.addAction(32);
            if (!isFolderCell() && this.parentFragment != null && Build.VERSION.SDK_INT >= 21) {
                info.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.acc_action_chat_preview, LocaleController.getString("AccActionChatPreview", R.string.AccActionChatPreview)));
            }
        }
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 != null && checkBox2.isChecked()) {
            info.setClassName("android.widget.CheckBox");
            info.setCheckable(true);
            info.setChecked(true);
        }
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        TLRPC.User fromUser;
        super.onPopulateAccessibilityEvent(event);
        StringBuilder sb = new StringBuilder();
        if (this.currentDialogFolderId == 1) {
            sb.append(LocaleController.getString("ArchivedChats", R.string.ArchivedChats));
            sb.append(". ");
        } else {
            if (this.encryptedChat != null) {
                sb.append(LocaleController.getString("AccDescrSecretChat", R.string.AccDescrSecretChat));
                sb.append(". ");
            }
            TLRPC.User user = this.user;
            if (user != null) {
                if (UserObject.isReplyUser(user)) {
                    sb.append(LocaleController.getString("RepliesTitle", R.string.RepliesTitle));
                } else {
                    if (this.user.bot) {
                        sb.append(LocaleController.getString("Bot", R.string.Bot));
                        sb.append(". ");
                    }
                    if (this.user.self) {
                        sb.append(LocaleController.getString("SavedMessages", R.string.SavedMessages));
                    } else {
                        sb.append(ContactsController.formatName(this.user.first_name, this.user.last_name));
                    }
                }
                sb.append(". ");
            } else {
                TLRPC.Chat chat = this.chat;
                if (chat != null) {
                    if (chat.broadcast) {
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
            event.setContentDescription(sb.toString());
            return;
        }
        int lastDate = this.lastMessageDate;
        if (this.lastMessageDate == 0) {
            lastDate = messageObject.messageOwner.date;
        }
        String date = LocaleController.formatDateAudio(lastDate, true);
        if (this.message.isOut()) {
            sb.append(LocaleController.formatString("AccDescrSentDate", R.string.AccDescrSentDate, date));
        } else {
            sb.append(LocaleController.formatString("AccDescrReceivedDate", R.string.AccDescrReceivedDate, date));
        }
        sb.append(". ");
        if (this.chat != null && !this.message.isOut() && this.message.isFromUser() && this.message.messageOwner.action == null && (fromUser = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.message.messageOwner.from_id.user_id))) != null) {
            sb.append(ContactsController.formatName(fromUser.first_name, fromUser.last_name));
            sb.append(". ");
        }
        if (this.encryptedChat == null) {
            StringBuilder messageString = new StringBuilder();
            messageString.append(this.message.messageText);
            if (!this.message.isMediaEmpty() && !TextUtils.isEmpty(this.message.caption)) {
                messageString.append(". ");
                messageString.append(this.message.caption);
            }
            StaticLayout staticLayout = this.messageLayout;
            int len = staticLayout == null ? -1 : staticLayout.getText().length();
            if (len > 0) {
                int index = messageString.length();
                int b = messageString.indexOf("\n", len);
                if (b < index && b >= 0) {
                    index = b;
                }
                int b2 = messageString.indexOf("\t", len);
                if (b2 < index && b2 >= 0) {
                    index = b2;
                }
                int b3 = messageString.indexOf(" ", len);
                if (b3 < index && b3 >= 0) {
                    index = b3;
                }
                sb.append(messageString.substring(0, index));
            } else {
                sb.append((CharSequence) messageString);
            }
        }
        event.setContentDescription(sb.toString());
    }

    public void setClipProgress(float value) {
        this.clipProgress = value;
        invalidate();
    }

    public float getClipProgress() {
        return this.clipProgress;
    }

    public void setTopClip(int value) {
        this.topClip = value;
    }

    public void setBottomClip(int value) {
        this.bottomClip = value;
    }

    public void setArchivedPullAnimation(PullForegroundDrawable drawable) {
        this.archivedChatsDrawable = drawable;
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
