package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
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
import j$.util.function.ToIntFunction;
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
import org.telegram.messenger.NotificationCenter;
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
import org.telegram.tgnet.TLRPC$InputReplyTo;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageExtendedMedia;
import org.telegram.tgnet.TLRPC$MessageFwdHeader;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$MessagePeerReaction;
import org.telegram.tgnet.TLRPC$Peer;
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
import org.telegram.tgnet.TLRPC$TL_messageExtendedMedia;
import org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaGame;
import org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway;
import org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults;
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC$TL_messageMediaStory;
import org.telegram.tgnet.TLRPC$TL_messageReactions;
import org.telegram.tgnet.TLRPC$TL_messageService;
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_photoEmpty;
import org.telegram.tgnet.TLRPC$TL_textWithEntities;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DialogsAdapter;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BubbleCounterPath;
import org.telegram.ui.Components.CanvasButton;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.DialogCellTags;
import org.telegram.ui.Components.EmptyStubSpan;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
import org.telegram.ui.Components.Forum.ForumBubbleDrawable;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.PullForegroundDrawable;
import org.telegram.ui.Components.QuoteSpan;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.ScamDrawable;
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
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.StoriesListPlaceProvider;
import org.telegram.ui.Stories.StoriesUtilities;
import org.telegram.ui.Stories.StoryViewer;
/* loaded from: classes4.dex */
public class DialogCell extends BaseCell implements StoriesListPlaceProvider.AvatarOverlaysView {
    private int[] adaptiveEmojiColor;
    private ColorFilter[] adaptiveEmojiColorFilter;
    public int addForumHeightForTags;
    public int addHeightForTags;
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
    public int avatarStart;
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
    private boolean draftVoice;
    public boolean drawArchive;
    public boolean drawAvatar;
    public boolean drawAvatarSelector;
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
    private ArrayList groupMessages;
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
    public boolean isDialogCell;
    private boolean isForum;
    public boolean isSavedDialog;
    public boolean isSavedDialogCell;
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
    private Drawable lockDrawable;
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
    private boolean premiumBlocked;
    private final AnimatedFloat premiumBlockedT;
    private PremiumGradient.PremiumGradientTools premiumGradient;
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
    private List spoilers;
    private List spoilers2;
    private Stack spoilersPool;
    private Stack spoilersPool2;
    private Drawable starBg;
    private int starBgColor;
    private Drawable starFg;
    private boolean statusDrawableAnimationInProgress;
    private ValueAnimator statusDrawableAnimator;
    private int statusDrawableLeft;
    private float statusDrawableProgress;
    public final StoriesUtilities.AvatarStoryParams storyParams;
    public boolean swipeCanceled;
    private int swipeMessageTextId;
    private StaticLayout swipeMessageTextLayout;
    private int swipeMessageWidth;
    public DialogCellTags tags;
    private int tagsLeft;
    private int tagsRight;
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
    private Runnable unsubscribePremiumBlocked;
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

    /* loaded from: classes4.dex */
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

    /* loaded from: classes4.dex */
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

    /* loaded from: classes4.dex */
    public interface DialogCellDelegate {
        boolean canClickButtonInside();

        void onButtonClicked(DialogCell dialogCell);

        void onButtonLongPress(DialogCell dialogCell);

        void openHiddenStories();

        void openStory(DialogCell dialogCell, Runnable runnable);

        void showChatPreview(DialogCell dialogCell);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
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

        /* JADX WARN: Code restructure failed: missing block: B:45:0x0130, code lost:
            if (org.telegram.messenger.MessagesController.getInstance(r19.this$0.currentAccount).getTopicsController().endIsReached(-r19.this$0.currentDialogId) != false) goto L132;
         */
        /* JADX WARN: Removed duplicated region for block: B:112:0x025c  */
        /* JADX WARN: Removed duplicated region for block: B:115:0x0269  */
        /* JADX WARN: Removed duplicated region for block: B:119:0x0275  */
        /* JADX WARN: Removed duplicated region for block: B:135:0x02a8  */
        /* JADX WARN: Removed duplicated region for block: B:137:0x02ac  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x00f4  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x013b  */
        /* JADX WARN: Removed duplicated region for block: B:61:0x0187  */
        /* JADX WARN: Removed duplicated region for block: B:72:0x01c3  */
        /* JADX WARN: Removed duplicated region for block: B:73:0x01c5  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x01dd  */
        /* JADX WARN: Removed duplicated region for block: B:88:0x0210  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean update() {
            Integer num;
            int i;
            boolean z;
            TLRPC$DraftMessage draft;
            int i2;
            int i3;
            boolean z2;
            TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) MessagesController.getInstance(DialogCell.this.currentAccount).dialogs_dict.get(DialogCell.this.currentDialogId);
            if (tLRPC$Dialog == null) {
                if (DialogCell.this.dialogsType != 3 || this.lastDrawnDialogId == DialogCell.this.currentDialogId) {
                    return false;
                }
                this.lastDrawnDialogId = DialogCell.this.currentDialogId;
                return true;
            }
            int id = DialogCell.this.message == null ? 0 : DialogCell.this.message.getId() + DialogCell.this.message.hashCode();
            long j = tLRPC$Dialog.read_inbox_max_id + (tLRPC$Dialog.read_outbox_max_id << 8) + ((tLRPC$Dialog.unread_count + (tLRPC$Dialog.unread_mark ? -1 : 0)) << 16) + (tLRPC$Dialog.unread_reactions_count > 0 ? 262144 : 0) + (tLRPC$Dialog.unread_mentions_count > 0 ? 524288 : 0);
            if (!DialogCell.this.isForumCell()) {
                DialogCell dialogCell = DialogCell.this;
                if ((dialogCell.isDialogCell || dialogCell.isTopic) && !TextUtils.isEmpty(MessagesController.getInstance(DialogCell.this.currentAccount).getPrintingString(DialogCell.this.currentDialogId, DialogCell.this.getTopicId(), true))) {
                    num = MessagesController.getInstance(DialogCell.this.currentAccount).getPrintingStringType(DialogCell.this.currentDialogId, DialogCell.this.getTopicId());
                    int measuredWidth = DialogCell.this.getMeasuredWidth() + (DialogCell.this.getMeasuredHeight() << 16);
                    if (DialogCell.this.isForumCell()) {
                        ArrayList<TLRPC$TL_forumTopic> topics = MessagesController.getInstance(DialogCell.this.currentAccount).getTopicsController().getTopics(-DialogCell.this.currentDialogId);
                        i = topics == null ? -1 : topics.size();
                        if (i == -1) {
                        }
                        if (!DialogCell.this.isTopic) {
                            DialogCell dialogCell2 = DialogCell.this;
                            if (dialogCell2.isDialogCell) {
                                z = MediaDataController.getInstance(dialogCell2.currentAccount).getDraftVoice(DialogCell.this.currentDialogId, 0L) != null;
                                if (!z) {
                                    draft = MediaDataController.getInstance(DialogCell.this.currentAccount).getDraft(DialogCell.this.currentDialogId, 0L);
                                    if (draft != null) {
                                    }
                                    if (DialogCell.this.chat == null) {
                                    }
                                    boolean isTranslatingDialog = MessagesController.getInstance(DialogCell.this.currentAccount).getTranslateController().isTranslatingDialog(DialogCell.this.currentDialogId);
                                    if (this.lastDrawnSizeHash != measuredWidth) {
                                    }
                                    if (this.lastDrawnDialogId == DialogCell.this.currentDialogId) {
                                    }
                                    if (num != null) {
                                    }
                                    this.lastDrawnDialogId = DialogCell.this.currentDialogId;
                                    this.lastDrawnMessageId = id;
                                    this.lastDrawnDialogIsFolder = tLRPC$Dialog.isFolder;
                                    this.lastDrawnReadState = j;
                                    this.lastDrawnPrintingType = num;
                                    this.lastDrawnSizeHash = i3;
                                    this.lastDrawnDraftHash = i2;
                                    this.lastTopicsCount = i;
                                    this.lastDrawnPinned = DialogCell.this.drawPin;
                                    this.lastDrawnHasCall = r8;
                                    this.lastDrawnTranslated = isTranslatingDialog;
                                    return true;
                                }
                            } else {
                                z = false;
                            }
                            draft = null;
                            if (draft != null) {
                            }
                            if (DialogCell.this.chat == null) {
                            }
                            boolean isTranslatingDialog2 = MessagesController.getInstance(DialogCell.this.currentAccount).getTranslateController().isTranslatingDialog(DialogCell.this.currentDialogId);
                            if (this.lastDrawnSizeHash != measuredWidth) {
                            }
                            if (this.lastDrawnDialogId == DialogCell.this.currentDialogId) {
                            }
                            if (num != null) {
                            }
                            this.lastDrawnDialogId = DialogCell.this.currentDialogId;
                            this.lastDrawnMessageId = id;
                            this.lastDrawnDialogIsFolder = tLRPC$Dialog.isFolder;
                            this.lastDrawnReadState = j;
                            this.lastDrawnPrintingType = num;
                            this.lastDrawnSizeHash = i3;
                            this.lastDrawnDraftHash = i2;
                            this.lastTopicsCount = i;
                            this.lastDrawnPinned = DialogCell.this.drawPin;
                            this.lastDrawnHasCall = r8;
                            this.lastDrawnTranslated = isTranslatingDialog2;
                            return true;
                        }
                        z = MediaDataController.getInstance(DialogCell.this.currentAccount).getDraftVoice(DialogCell.this.currentDialogId, (long) DialogCell.this.getTopicId()) != null;
                        TLRPC$DraftMessage draft2 = !z ? MediaDataController.getInstance(DialogCell.this.currentAccount).getDraft(DialogCell.this.currentDialogId, DialogCell.this.getTopicId()) : null;
                        if (draft2 == null || !TextUtils.isEmpty(draft2.message)) {
                            draft = draft2;
                            if (draft != null) {
                                i2 = 0;
                            } else {
                                int hashCode = draft.message.hashCode();
                                TLRPC$InputReplyTo tLRPC$InputReplyTo = draft.reply_to;
                                i2 = hashCode + (tLRPC$InputReplyTo != null ? tLRPC$InputReplyTo.reply_to_msg_id << 16 : 0);
                            }
                            boolean z3 = DialogCell.this.chat == null && DialogCell.this.chat.call_active && DialogCell.this.chat.call_not_empty;
                            boolean isTranslatingDialog22 = MessagesController.getInstance(DialogCell.this.currentAccount).getTranslateController().isTranslatingDialog(DialogCell.this.currentDialogId);
                            if (this.lastDrawnSizeHash != measuredWidth) {
                                i3 = measuredWidth;
                                if (this.lastDrawnMessageId == id && this.lastDrawnTranslated == isTranslatingDialog22 && this.lastDrawnDialogId == DialogCell.this.currentDialogId && this.lastDrawnDialogIsFolder == tLRPC$Dialog.isFolder && this.lastDrawnReadState == j && Objects.equals(this.lastDrawnPrintingType, num) && this.lastTopicsCount == i && i2 == this.lastDrawnDraftHash && this.lastDrawnPinned == DialogCell.this.drawPin && this.lastDrawnHasCall == z3 && DialogCell.this.draftVoice == z) {
                                    return false;
                                }
                            } else {
                                i3 = measuredWidth;
                            }
                            if (this.lastDrawnDialogId == DialogCell.this.currentDialogId) {
                                this.typingProgres = num == null ? 0.0f : 1.0f;
                                this.waitngNewMessageFroTypingAnimation = false;
                            } else if (!Objects.equals(this.lastDrawnPrintingType, num) || this.waitngNewMessageFroTypingAnimation) {
                                boolean z4 = this.waitngNewMessageFroTypingAnimation;
                                if (!z4 && num == null) {
                                    this.waitngNewMessageFroTypingAnimation = true;
                                    this.startWaitingTime = System.currentTimeMillis();
                                } else if (z4 && this.lastDrawnMessageId != id) {
                                    z2 = false;
                                    this.waitngNewMessageFroTypingAnimation = false;
                                    if (this.lastDrawnMessageId == id) {
                                        z2 = true;
                                    }
                                    this.typingOutToTop = z2;
                                }
                                z2 = false;
                                if (this.lastDrawnMessageId == id) {
                                }
                                this.typingOutToTop = z2;
                            }
                            if (num != null) {
                                this.lastKnownTypingType = num.intValue();
                            }
                            this.lastDrawnDialogId = DialogCell.this.currentDialogId;
                            this.lastDrawnMessageId = id;
                            this.lastDrawnDialogIsFolder = tLRPC$Dialog.isFolder;
                            this.lastDrawnReadState = j;
                            this.lastDrawnPrintingType = num;
                            this.lastDrawnSizeHash = i3;
                            this.lastDrawnDraftHash = i2;
                            this.lastTopicsCount = i;
                            this.lastDrawnPinned = DialogCell.this.drawPin;
                            this.lastDrawnHasCall = z3;
                            this.lastDrawnTranslated = isTranslatingDialog22;
                            return true;
                        }
                        draft = null;
                        if (draft != null) {
                        }
                        if (DialogCell.this.chat == null) {
                        }
                        boolean isTranslatingDialog222 = MessagesController.getInstance(DialogCell.this.currentAccount).getTranslateController().isTranslatingDialog(DialogCell.this.currentDialogId);
                        if (this.lastDrawnSizeHash != measuredWidth) {
                        }
                        if (this.lastDrawnDialogId == DialogCell.this.currentDialogId) {
                        }
                        if (num != null) {
                        }
                        this.lastDrawnDialogId = DialogCell.this.currentDialogId;
                        this.lastDrawnMessageId = id;
                        this.lastDrawnDialogIsFolder = tLRPC$Dialog.isFolder;
                        this.lastDrawnReadState = j;
                        this.lastDrawnPrintingType = num;
                        this.lastDrawnSizeHash = i3;
                        this.lastDrawnDraftHash = i2;
                        this.lastTopicsCount = i;
                        this.lastDrawnPinned = DialogCell.this.drawPin;
                        this.lastDrawnHasCall = z3;
                        this.lastDrawnTranslated = isTranslatingDialog222;
                        return true;
                    }
                    i = 0;
                    if (!DialogCell.this.isTopic) {
                    }
                }
            }
            num = null;
            int measuredWidth2 = DialogCell.this.getMeasuredWidth() + (DialogCell.this.getMeasuredHeight() << 16);
            if (DialogCell.this.isForumCell()) {
            }
            i = 0;
            if (!DialogCell.this.isTopic) {
            }
        }

        public void updateAnimationValues() {
            float f;
            if (this.waitngNewMessageFroTypingAnimation) {
                if (System.currentTimeMillis() - this.startWaitingTime > 100) {
                    this.waitngNewMessageFroTypingAnimation = false;
                }
                DialogCell.this.invalidate();
                return;
            }
            if (this.lastDrawnPrintingType != null && DialogCell.this.typingLayout != null) {
                float f2 = this.typingProgres;
                if (f2 != 1.0f) {
                    f = f2 + 0.08f;
                    this.typingProgres = f;
                    DialogCell.this.invalidate();
                    this.typingProgres = Utilities.clamp(this.typingProgres, 1.0f, 0.0f);
                }
            }
            if (this.lastDrawnPrintingType == null) {
                float f3 = this.typingProgres;
                if (f3 != 0.0f) {
                    f = f3 - 0.08f;
                    this.typingProgres = f;
                    DialogCell.this.invalidate();
                }
            }
            this.typingProgres = Utilities.clamp(this.typingProgres, 1.0f, 0.0f);
        }
    }

    /* loaded from: classes4.dex */
    public static class FixedWidthSpan extends ReplacementSpan {
        private int width;

        public FixedWidthSpan(int i) {
            this.width = i;
        }

        @Override // android.text.style.ReplacementSpan
        public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
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

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
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
        /* JADX WARN: Removed duplicated region for block: B:39:0x009d  */
        /* JADX WARN: Removed duplicated region for block: B:40:0x00b9  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void formatTopicsNames(int i, MessageObject messageObject, TLRPC$Chat tLRPC$Chat) {
            long j;
            boolean z;
            int i2;
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
                    if (topics == null || topics.isEmpty()) {
                        if (MessagesController.getInstance(i).getTopicsController().endIsReached(tLRPC$Chat.id)) {
                            this.formattedNames = "no created topics";
                            return;
                        }
                        MessagesController.getInstance(i).getTopicsController().preloadTopics(tLRPC$Chat.id);
                        this.formattedNames = LocaleController.getString(R.string.Loading);
                        this.isLoadingState = true;
                        return;
                    }
                    ArrayList arrayList = new ArrayList(topics);
                    Collections.sort(arrayList, Comparator$-CC.comparingInt(new ToIntFunction() { // from class: org.telegram.ui.Cells.DialogCell$ForumFormattedNames$$ExternalSyntheticLambda0
                        @Override // j$.util.function.ToIntFunction
                        public final int applyAsInt(Object obj) {
                            int lambda$formatTopicsNames$0;
                            lambda$formatTopicsNames$0 = DialogCell.ForumFormattedNames.lambda$formatTopicsNames$0((TLRPC$TL_forumTopic) obj);
                            return lambda$formatTopicsNames$0;
                        }
                    }));
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    if (messageObject != null) {
                        j = MessageObject.getTopicId(i, messageObject.messageOwner, true);
                        TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(i).getTopicsController().findTopic(tLRPC$Chat.id, j);
                        if (findTopic != null) {
                            CharSequence topicSpannedName = ForumUtilities.getTopicSpannedName(findTopic, textPaint, false);
                            spannableStringBuilder.append(topicSpannedName);
                            i2 = findTopic.unread_count > 0 ? topicSpannedName.length() : 0;
                            this.topMessageTopicStartIndex = 0;
                            this.topMessageTopicEndIndex = topicSpannedName.length();
                            if (!messageObject.isOutOwner()) {
                                this.lastTopicMessageUnread = findTopic.unread_count > 0;
                                if (this.lastTopicMessageUnread) {
                                    z = false;
                                } else {
                                    spannableStringBuilder.append((CharSequence) " ");
                                    spannableStringBuilder.setSpan(new FixedWidthSpan(AndroidUtilities.dp(3.0f)), spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);
                                    z = true;
                                }
                            }
                        } else {
                            i2 = 0;
                        }
                        this.lastTopicMessageUnread = false;
                        if (this.lastTopicMessageUnread) {
                        }
                    } else {
                        j = 0;
                        z = false;
                        i2 = 0;
                    }
                    for (int i3 = 0; i3 < Math.min(4, arrayList.size()); i3++) {
                        if (((TLRPC$TL_forumTopic) arrayList.get(i3)).id != j) {
                            if (spannableStringBuilder.length() != 0) {
                                if (z2 && z) {
                                    spannableStringBuilder.append((CharSequence) " ");
                                } else {
                                    spannableStringBuilder.append((CharSequence) ", ");
                                }
                            }
                            spannableStringBuilder.append(ForumUtilities.getTopicSpannedName((TLRPC$ForumTopic) arrayList.get(i3), textPaint, false));
                            z2 = false;
                        }
                    }
                    if (i2 > 0) {
                        spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.bold(), 0, Theme.key_chats_name, null), 0, Math.min(spannableStringBuilder.length(), i2 + 2), 0);
                    }
                    this.formattedNames = spannableStringBuilder;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ int lambda$formatTopicsNames$0(TLRPC$TL_forumTopic tLRPC$TL_forumTopic) {
            return -tLRPC$TL_forumTopic.top_message;
        }
    }

    /* loaded from: classes4.dex */
    public static class SharedResources {
    }

    public DialogCell(DialogsActivity dialogsActivity, Context context, boolean z, boolean z2) {
        this(dialogsActivity, context, z, z2, UserConfig.selectedAccount, null);
    }

    public DialogCell(DialogsActivity dialogsActivity, Context context, boolean z, boolean z2, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.drawArchive = true;
        this.drawAvatar = true;
        this.avatarStart = 10;
        this.messagePaddingStart = 72;
        this.heightDefault = 72;
        this.heightThreeLines = 78;
        this.addHeightForTags = 3;
        this.addForumHeightForTags = 11;
        this.chekBoxPaddingTop = 42.0f;
        int i2 = 0;
        StoriesUtilities.AvatarStoryParams avatarStoryParams = new StoriesUtilities.AvatarStoryParams(false) { // from class: org.telegram.ui.Cells.DialogCell.1
            @Override // org.telegram.ui.Stories.StoriesUtilities.AvatarStoryParams
            public void onLongPress() {
                DialogCell dialogCell = DialogCell.this;
                DialogCellDelegate dialogCellDelegate = dialogCell.delegate;
                if (dialogCellDelegate == null) {
                    return;
                }
                dialogCellDelegate.showChatPreview(dialogCell);
            }

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
        this.premiumBlockedT = new AnimatedFloat(this, 0L, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
        this.spoilersPool = new Stack();
        this.spoilers = new ArrayList();
        this.spoilersPool2 = new Stack();
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
            if (i2 >= imageReceiverArr.length) {
                this.useForceThreeLines = z2;
                this.currentAccount = i;
                this.emojiStatus = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this, AndroidUtilities.dp(22.0f));
                this.avatarImage.setAllowLoadingOnAttachedOnly(true);
                return;
            }
            imageReceiverArr[i2] = new ImageReceiver(this);
            ImageReceiver imageReceiver = this.thumbImage[i2];
            imageReceiver.ignoreNotifications = true;
            imageReceiver.setRoundRadius(AndroidUtilities.dp(2.0f));
            this.thumbImage[i2].setAllowLoadingOnAttachedOnly(true);
            i2++;
        }
    }

    private CharSequence applyThumbs(CharSequence charSequence) {
        if (this.thumbsCount > 0) {
            SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(charSequence);
            valueOf.insert(0, (CharSequence) " ");
            valueOf.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbSize + 2) * this.thumbsCount) + 3)), 0, 1, 33);
            return valueOf;
        }
        return charSequence;
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

    private void checkGroupCall() {
        TLRPC$Chat tLRPC$Chat = this.chat;
        boolean z = tLRPC$Chat != null && tLRPC$Chat.call_active && tLRPC$Chat.call_not_empty;
        this.hasCall = z;
        this.chatCallProgress = z ? 1.0f : 0.0f;
    }

    private void checkOnline() {
        TLRPC$User user;
        if (this.user != null && (user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id))) != null) {
            this.user = user;
        }
        this.onlineProgress = isOnline() ? 1.0f : 0.0f;
    }

    private void checkTtl() {
        CheckBox2 checkBox2;
        boolean z = this.ttlPeriod > 0 && !this.hasCall && !isOnline() && ((checkBox2 = this.checkBox) == null || !checkBox2.isChecked());
        this.showTtl = z;
        this.ttlProgress = z ? 1.0f : 0.0f;
    }

    private void checkTwoLinesForName() {
        this.twoLinesForName = false;
        if (!this.isTopic || hasTags()) {
            return;
        }
        buildLayout();
        if (this.nameIsEllipsized) {
            this.twoLinesForName = true;
            buildLayout();
        }
    }

    private int computeHeight() {
        if (!isForumCell() || this.isTransitionSupport || this.collapsed) {
            return getCollapsedHeight();
        }
        int dp = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 86.0f : 91.0f);
        if (this.useSeparator) {
            dp++;
        }
        return hasTags() ? dp + AndroidUtilities.dp(this.addForumHeightForTags) : dp;
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
        this.statusDrawableAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                DialogCell.this.lambda$createStatusDrawableAnimator$4(valueAnimator);
            }
        });
        this.statusDrawableAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                int i3 = (DialogCell.this.drawClock ? 1 : 0) + (DialogCell.this.drawCheck1 ? 2 : 0) + (DialogCell.this.drawCheck2 ? 4 : 0);
                if (DialogCell.this.animateToStatusDrawableParams != i3) {
                    DialogCell dialogCell = DialogCell.this;
                    dialogCell.createStatusDrawableAnimator(dialogCell.animateToStatusDrawableParams, i3);
                } else {
                    DialogCell.this.statusDrawableAnimationInProgress = false;
                    DialogCell dialogCell2 = DialogCell.this;
                    dialogCell2.lastStatusDrawableParams = dialogCell2.animateToStatusDrawableParams;
                }
                DialogCell.this.invalidate();
            }
        });
        this.statusDrawableAnimationInProgress = true;
        this.statusDrawableAnimator.start();
    }

    private void drawCheckStatus(Canvas canvas, boolean z, boolean z2, boolean z3, boolean z4, float f) {
        Drawable drawable;
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
                    Theme.dialogs_clockDrawable.setAlpha(NotificationCenter.didClearDatabase);
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
                        Theme.dialogs_halfCheckDrawable.setAlpha(NotificationCenter.didClearDatabase);
                    }
                    if (z4 || f == 0.0f) {
                        return;
                    }
                    canvas.restore();
                    Theme.dialogs_halfCheckDrawable.setAlpha(NotificationCenter.didClearDatabase);
                    drawable = Theme.dialogs_checkReadDrawable;
                } else {
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
                    drawable = Theme.dialogs_checkDrawable;
                }
                drawable.setAlpha(NotificationCenter.didClearDatabase);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:106:0x025b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void drawCounter(Canvas canvas, boolean z, int i, int i2, int i3, float f, boolean z2) {
        Paint paint;
        boolean z3;
        int dp;
        RectF rectF;
        float f2;
        float interpolation;
        StaticLayout staticLayout;
        RectF rectF2;
        int color;
        boolean z4 = isForumCell() || isFolderCell();
        if (!(this.drawCount && this.drawCount2) && this.countChangeProgress == 1.0f) {
            return;
        }
        float f3 = (this.unreadCount != 0 || this.markUnread) ? this.countChangeProgress : 1.0f - this.countChangeProgress;
        int i4 = NotificationCenter.didClearDatabase;
        if (z2) {
            if (this.counterPaintOutline == null) {
                Paint paint2 = new Paint();
                this.counterPaintOutline = paint2;
                paint2.setStyle(Paint.Style.STROKE);
                this.counterPaintOutline.setStrokeWidth(AndroidUtilities.dp(2.0f));
                this.counterPaintOutline.setStrokeJoin(Paint.Join.ROUND);
                this.counterPaintOutline.setStrokeCap(Paint.Cap.ROUND);
            }
            this.counterPaintOutline.setColor(ColorUtils.blendARGB(Theme.getColor(Theme.key_windowBackgroundWhite), ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_chats_pinnedOverlay), NotificationCenter.didClearDatabase), Color.alpha(color) / 255.0f));
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
        StaticLayout staticLayout2 = this.countOldLayout;
        if (staticLayout2 == null || this.unreadCount == 0) {
            if (this.unreadCount != 0) {
                staticLayout2 = this.countLayout;
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
            if (staticLayout2 != null) {
                canvas.save();
                canvas.translate(i2, i + AndroidUtilities.dp(4.0f));
                staticLayout2.draw(canvas);
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
            float f10 = interpolation + f2;
            canvas.save();
            float f11 = f10 * f;
            canvas.scale(f11, f11, this.rect.centerX(), this.rect.centerY());
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
            float f12 = alpha;
            Theme.dialogs_countTextPaint.setAlpha((int) (f12 * f6));
            if (this.countAnimationInLayout != null) {
                canvas.save();
                canvas.translate(f8, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * f7) + f9 + AndroidUtilities.dp(4.0f));
                staticLayout = this.countAnimationInLayout;
            } else {
                if (this.countLayout != null) {
                    canvas.save();
                    canvas.translate(f8, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * f7) + f9 + AndroidUtilities.dp(4.0f));
                    staticLayout = this.countLayout;
                }
                if (this.countOldLayout != null) {
                    Theme.dialogs_countTextPaint.setAlpha((int) (f12 * f7));
                    canvas.save();
                    canvas.translate(f8, ((this.countAnimationIncrement ? -AndroidUtilities.dp(13.0f) : AndroidUtilities.dp(13.0f)) * f6) + f9 + AndroidUtilities.dp(4.0f));
                    this.countOldLayout.draw(canvas);
                    canvas.restore();
                }
                Theme.dialogs_countTextPaint.setAlpha(alpha);
                canvas.restore();
            }
            staticLayout.draw(canvas);
            canvas.restore();
            if (this.countOldLayout != null) {
            }
            Theme.dialogs_countTextPaint.setAlpha(alpha);
            canvas.restore();
        }
        if (z3) {
            Theme.dialogs_countTextPaint.setColor(Theme.getColor(Theme.key_chats_unreadCounterText));
        }
    }

    private MessageObject findFolderTopMessage() {
        ArrayList dialogsArray;
        DialogsActivity dialogsActivity = this.parentFragment;
        if (dialogsActivity == null || (dialogsArray = dialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.currentDialogFolderId, false)) == null || dialogsArray.isEmpty()) {
            return null;
        }
        int size = dialogsArray.size();
        MessageObject messageObject = null;
        for (int i = 0; i < size; i++) {
            TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) dialogsArray.get(i);
            LongSparseArray longSparseArray = MessagesController.getInstance(this.currentAccount).dialogMessage;
            if (longSparseArray != null) {
                ArrayList arrayList = (ArrayList) longSparseArray.get(tLRPC$Dialog.id);
                MessageObject messageObject2 = (arrayList == null || arrayList.isEmpty()) ? null : (MessageObject) arrayList.get(0);
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

    /* JADX WARN: Removed duplicated region for block: B:21:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00d4 A[EDGE_INSN: B:45:0x00d4->B:37:0x00d4 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00d0 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private CharSequence formatArchivedDialogNames() {
        long j;
        TLRPC$User tLRPC$User;
        String string;
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        ArrayList<TLRPC$Dialog> dialogs = messagesController.getDialogs(this.currentDialogFolderId);
        this.currentDialogFolderDialogsCount = dialogs.size();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        int size = dialogs.size();
        for (int i = 0; i < size; i++) {
            TLRPC$Dialog tLRPC$Dialog = dialogs.get(i);
            if (!messagesController.isHiddenByUndo(tLRPC$Dialog.id)) {
                boolean isEncryptedDialog = DialogObject.isEncryptedDialog(tLRPC$Dialog.id);
                TLRPC$Chat tLRPC$Chat = null;
                long j2 = tLRPC$Dialog.id;
                if (isEncryptedDialog) {
                    TLRPC$EncryptedChat encryptedChat = messagesController.getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j2)));
                    if (encryptedChat != null) {
                        j = encryptedChat.user_id;
                        tLRPC$User = messagesController.getUser(Long.valueOf(j));
                        if (tLRPC$Chat == null) {
                            string = tLRPC$Chat.title.replace('\n', ' ');
                        } else if (tLRPC$User == null) {
                            continue;
                        } else {
                            string = UserObject.isDeleted(tLRPC$User) ? LocaleController.getString(R.string.HiddenName) : AndroidUtilities.removeDiacritics(ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name).replace('\n', ' '));
                        }
                        if (spannableStringBuilder.length() > 0) {
                            spannableStringBuilder.append((CharSequence) ", ");
                        }
                        int length = spannableStringBuilder.length();
                        int length2 = string.length() + length;
                        spannableStringBuilder.append((CharSequence) string);
                        if (tLRPC$Dialog.unread_count > 0) {
                            spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.bold(), 0, Theme.getColor(Theme.key_chats_nameArchived, this.resourcesProvider)), length, length2, 33);
                        }
                        if (spannableStringBuilder.length() <= 150) {
                            break;
                        }
                    } else {
                        tLRPC$User = null;
                        if (tLRPC$Chat == null) {
                        }
                        if (spannableStringBuilder.length() > 0) {
                        }
                        int length3 = spannableStringBuilder.length();
                        int length22 = string.length() + length3;
                        spannableStringBuilder.append((CharSequence) string);
                        if (tLRPC$Dialog.unread_count > 0) {
                        }
                        if (spannableStringBuilder.length() <= 150) {
                        }
                    }
                } else {
                    boolean isUserDialog = DialogObject.isUserDialog(j2);
                    j = tLRPC$Dialog.id;
                    if (!isUserDialog) {
                        tLRPC$Chat = messagesController.getChat(Long.valueOf(-j));
                        tLRPC$User = null;
                        if (tLRPC$Chat == null) {
                        }
                        if (spannableStringBuilder.length() > 0) {
                        }
                        int length32 = spannableStringBuilder.length();
                        int length222 = string.length() + length32;
                        spannableStringBuilder.append((CharSequence) string);
                        if (tLRPC$Dialog.unread_count > 0) {
                        }
                        if (spannableStringBuilder.length() <= 150) {
                        }
                    }
                    tLRPC$User = messagesController.getUser(Long.valueOf(j));
                    if (tLRPC$Chat == null) {
                    }
                    if (spannableStringBuilder.length() > 0) {
                    }
                    int length322 = spannableStringBuilder.length();
                    int length2222 = string.length() + length322;
                    spannableStringBuilder.append((CharSequence) string);
                    if (tLRPC$Dialog.unread_count > 0) {
                    }
                    if (spannableStringBuilder.length() <= 150) {
                    }
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
        return Emoji.replaceEmoji((CharSequence) spannableStringBuilder, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
    }

    private SpannableStringBuilder formatInternal(int i, CharSequence charSequence, CharSequence charSequence2) {
        SpannableStringBuilder append;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (i == 1) {
            append = spannableStringBuilder.append(charSequence2).append((CharSequence) ": \u2068");
        } else if (i != 2) {
            if (i == 3) {
                spannableStringBuilder.append(charSequence2).append((CharSequence) ": ").append(charSequence);
            } else if (i == 4) {
                spannableStringBuilder.append(charSequence);
            }
            return spannableStringBuilder;
        } else {
            append = spannableStringBuilder.append((CharSequence) "\u2068");
        }
        append.append(charSequence).append((CharSequence) "\u2069");
        return spannableStringBuilder;
    }

    private CharSequence formatTopicsNames() {
        ForumFormattedNames forumFormattedNames = new ForumFormattedNames();
        forumFormattedNames.formatTopicsNames(this.currentAccount, this.message, this.chat);
        this.topMessageTopicStartIndex = forumFormattedNames.topMessageTopicStartIndex;
        this.topMessageTopicEndIndex = forumFormattedNames.topMessageTopicEndIndex;
        this.lastTopicMessageUnread = forumFormattedNames.lastTopicMessageUnread;
        return forumFormattedNames.formattedNames;
    }

    private ColorFilter getAdaptiveEmojiColorFilter(int i, int i2) {
        if (this.adaptiveEmojiColorFilter == null) {
            this.adaptiveEmojiColor = new int[4];
            this.adaptiveEmojiColorFilter = new ColorFilter[4];
        }
        if (i2 != this.adaptiveEmojiColor[i] || this.adaptiveEmojiColorFilter[i] == null) {
            ColorFilter[] colorFilterArr = this.adaptiveEmojiColorFilter;
            this.adaptiveEmojiColor[i] = i2;
            colorFilterArr[i] = new PorterDuffColorFilter(i2, PorterDuff.Mode.SRC_IN);
        }
        return this.adaptiveEmojiColorFilter[i];
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
            MessageObject messageObject3 = (MessageObject) this.groupMessages.get(i2);
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

    private int getCollapsedHeight() {
        int dp = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? this.heightThreeLines : this.heightDefault);
        if (this.useSeparator) {
            dp++;
        }
        if (this.twoLinesForName) {
            dp += AndroidUtilities.dp(20.0f);
        }
        if (hasTags()) {
            if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !isForumCell()) {
                return dp;
            }
            return dp + AndroidUtilities.dp(isForumCell() ? this.addForumHeightForTags : this.addHeightForTags);
        }
        return dp;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTopicId() {
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic = this.forumTopic;
        if (tLRPC$TL_forumTopic == null) {
            return 0;
        }
        return tLRPC$TL_forumTopic.id;
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createStatusDrawableAnimator$4(ValueAnimator valueAnimator) {
        this.statusDrawableProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPremiumBlocked$5(Object[] objArr) {
        updatePremiumBlocked(true);
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
        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize(), false, null, true);
        TLRPC$PhotoSize tLRPC$PhotoSize = closestPhotoSizeWithSize != closestPhotoSizeWithSize2 ? closestPhotoSizeWithSize2 : null;
        if (tLRPC$PhotoSize == null || DownloadController.getInstance(this.currentAccount).canDownloadMedia(messageObject.messageOwner) == 0) {
            tLRPC$PhotoSize = closestPhotoSizeWithSize;
        }
        if (closestPhotoSizeWithSize != null) {
            this.hasVideoThumb = this.hasVideoThumb || messageObject.isVideo() || messageObject.isRoundVideo();
            int i2 = this.thumbsCount;
            if (i2 < 3) {
                this.thumbsCount = i2 + 1;
                this.drawPlay[i] = (messageObject.isVideo() || messageObject.isRoundVideo()) && !messageObject.hasMediaSpoilers();
                this.drawSpoiler[i] = messageObject.hasMediaSpoilers();
                int i3 = (messageObject.type != 1 || tLRPC$PhotoSize == null) ? 0 : tLRPC$PhotoSize.size;
                String str = messageObject.hasMediaSpoilers() ? "5_5_b" : "20_20";
                this.thumbImage[i].setImage(ImageLocation.getForObject(tLRPC$PhotoSize, tLObject), str, ImageLocation.getForObject(closestPhotoSizeWithSize, tLObject), str, i3, null, messageObject, 0);
                this.thumbImage[i].setRoundRadius(AndroidUtilities.dp(messageObject.isRoundVideo() ? 18.0f : 2.0f));
                this.needEmoji = false;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setThumb(int i, TLRPC$MessageMedia tLRPC$MessageMedia) {
        TLRPC$Document tLRPC$Document;
        ArrayList<TLRPC$PhotoSize> arrayList;
        boolean isVideoDocument;
        TLRPC$PhotoSize closestPhotoSizeWithSize;
        TLRPC$PhotoSize tLRPC$PhotoSize;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
            TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia.photo;
            arrayList = tLRPC$Photo.sizes;
            tLRPC$Document = tLRPC$Photo;
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
            isVideoDocument = MessageObject.isVideoDocument(tLRPC$MessageMedia.document);
            tLRPC$Document = tLRPC$MessageMedia.document;
            arrayList = tLRPC$Document.thumbs;
            closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, 40);
            TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize(), false, null, true);
            tLRPC$PhotoSize = closestPhotoSizeWithSize != closestPhotoSizeWithSize2 ? closestPhotoSizeWithSize2 : null;
            if (tLRPC$PhotoSize != null || DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.message.messageOwner, tLRPC$MessageMedia) == 0) {
                tLRPC$PhotoSize = closestPhotoSizeWithSize;
            }
            if (closestPhotoSizeWithSize == null) {
                this.hasVideoThumb = this.hasVideoThumb || isVideoDocument;
                int i2 = this.thumbsCount;
                if (i2 < 3) {
                    this.thumbsCount = i2 + 1;
                    this.drawPlay[i] = isVideoDocument;
                    this.drawSpoiler[i] = false;
                    this.thumbImage[i].setImage(ImageLocation.getForObject(tLRPC$PhotoSize, tLRPC$Document), "20_20", ImageLocation.getForObject(closestPhotoSizeWithSize, tLRPC$Document), "20_20", (isVideoDocument || tLRPC$PhotoSize == null) ? 0 : tLRPC$PhotoSize.size, null, this.message, 0);
                    this.thumbImage[i].setRoundRadius(AndroidUtilities.dp(2.0f));
                    this.needEmoji = false;
                    return;
                }
                return;
            }
            return;
        } else {
            tLRPC$Document = null;
            arrayList = null;
        }
        isVideoDocument = false;
        closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, 40);
        TLRPC$PhotoSize closestPhotoSizeWithSize22 = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize(), false, null, true);
        if (closestPhotoSizeWithSize != closestPhotoSizeWithSize22) {
        }
        if (tLRPC$PhotoSize != null) {
        }
        tLRPC$PhotoSize = closestPhotoSizeWithSize;
        if (closestPhotoSizeWithSize == null) {
        }
    }

    private void setThumb(int i, TLRPC$PhotoSize tLRPC$PhotoSize) {
        if (tLRPC$PhotoSize != null) {
            this.hasVideoThumb = false;
            int i2 = this.thumbsCount;
            if (i2 < 3) {
                this.thumbsCount = i2 + 1;
                this.drawPlay[i] = false;
                this.drawSpoiler[i] = true;
                this.thumbImage[i].setImage(ImageLocation.getForObject(tLRPC$PhotoSize, this.message.messageOwner), "2_2_b", null, null, 0, null, this.message, 0);
                this.thumbImage[i].setRoundRadius(AndroidUtilities.dp(2.0f));
                this.needEmoji = false;
            }
        }
    }

    private void updatePremiumBlocked(boolean z) {
        boolean z2 = this.premiumBlocked;
        boolean z3 = (this.unsubscribePremiumBlocked == null || this.user == null || !MessagesController.getInstance(this.currentAccount).isUserPremiumBlocked(this.user.id)) ? false : true;
        this.premiumBlocked = z3;
        if (z2 != z3) {
            if (!z) {
                this.premiumBlockedT.set(z3, true);
            }
            invalidate();
        }
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

    @Override // org.telegram.ui.Cells.BaseCell
    protected boolean allowCaching() {
        return this.rightFragmentOpenedProgress <= 0.0f;
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

    /* JADX WARN: Can't wrap try/catch for region: R(68:109|110|(2:729|(1:753)(2:733|(1:735)(2:737|(1:739)(2:741|(3:743|(1:745)(1:748)|746)))))(1:114)|116|(18:117|118|(1:120)|121|(1:123)|124|(3:126|(1:128)(1:130)|129)|131|(1:133)(1:724)|134|(1:136)|137|(1:723)(1:143)|144|(1:146)(1:722)|147|(1:721)(1:151)|152)|153|(4:695|(1:697)(1:719)|698|(2:699|(5:701|(1:703)(1:717)|704|(2:715|716)(2:712|713)|714)(1:718)))(9:157|(1:159)(1:694)|160|(1:162)(1:693)|163|(1:165)(1:692)|166|167|(2:168|(5:170|(1:172)(1:186)|173|(2:184|185)(2:181|182)|183)(1:187)))|188|189|(1:191)(1:691)|192|(1:194)|195|(1:203)|204|(3:206|(1:208)(1:210)|209)|211|(2:213|(1:215)(2:617|618))(1:(4:(4:630|(1:632)(1:687)|633|634)(1:688)|(6:636|(1:638)(1:685)|639|(3:641|(1:643)(1:679)|644)(3:680|(1:682)(1:684)|683)|645|646)(1:686)|647|(2:649|(4:651|(3:653|(1:655)(1:657)|656)|658|(3:660|(1:662)(1:664)|663))(5:665|(3:667|(1:669)(1:671)|670)|672|(3:674|(1:676)(1:678)|677)|618)))(3:623|(2:625|(1:627))|628))|(7:(1:218)|219|(1:221)|222|(1:235)(1:226)|227|(1:231))|236|(1:616)(1:240)|241|(3:247|(1:249)(1:251)|250)|252|(4:254|(1:560)(1:258)|259|(2:260|(1:262)(1:263)))(2:561|(8:590|591|(2:593|(2:595|(1:597)))|598|599|(1:609)(1:603)|604|(2:605|(1:607)(1:608)))(3:565|566|(5:571|572|(1:582)(1:576)|577|(2:578|(1:580)(1:581)))(1:570)))|264|265|(1:267)|268|(4:269|270|(1:272)(1:558)|273)|274|(3:275|276|(6:278|(4:280|(1:282)|283|284)|286|(2:288|284)|283|284))|(4:289|290|(4:524|525|(5:527|(2:529|(4:531|(2:533|(1:535))|536|(2:538|(2:540|(4:542|(1:546)|547|548)))))|549|547|548)|550)|292)|(3:512|513|(35:515|516|(19:518|519|310|(1:504)(1:314)|315|316|(5:495|(1:498)|499|(1:501)(1:503)|502)(3:320|(2:322|(1:326))|327)|328|329|330|331|332|333|334|(10:336|(9:340|(1:342)|343|(2:347|(5:349|350|351|352|(2:354|(1:356)))(5:357|(1:359)(2:360|(3:362|(1:364)(1:366)|365))|351|352|(0)))|367|350|351|352|(0))|372|(3:376|(1:(1:385)(2:378|(1:380)(2:381|382)))|(1:384))|386|(3:390|(1:(1:399)(2:392|(1:394)(2:395|396)))|(1:398))|400|(2:406|(1:408))|409|(4:413|(1:415)|416|417))(10:436|(5:440|(1:442)|443|(4:445|(1:447)|448|(1:450))|451)|452|(4:456|(1:458)|459|460)|461|(4:465|(1:467)|468|469)|470|(4:474|(1:476)|477|478)|479|(1:483))|418|(4:(1:433)(1:427)|428|(1:430)(1:432)|431)|434|435)|297|(1:299)|506|(26:(1:509)|309|310|(1:312)|504|315|316|(1:318)|493|495|(1:498)|499|(0)(0)|502|328|329|330|331|332|333|334|(0)(0)|418|(7:420|422|(1:425)|433|428|(0)(0)|431)|434|435)|301|(1:505)(1:307)|308|309|310|(0)|504|315|316|(0)|493|495|(0)|499|(0)(0)|502|328|329|330|331|332|333|334|(0)(0)|418|(0)|434|435))|296|297|(0)|506|(0)|301|(1:303)|505|308|309|310|(0)|504|315|316|(0)|493|495|(0)|499|(0)(0)|502|328|329|330|331|332|333|334|(0)(0)|418|(0)|434|435) */
    /* JADX WARN: Can't wrap try/catch for region: R(93:109|110|(2:729|(1:753)(2:733|(1:735)(2:737|(1:739)(2:741|(3:743|(1:745)(1:748)|746)))))(1:114)|116|117|118|(1:120)|121|(1:123)|124|(3:126|(1:128)(1:130)|129)|131|(1:133)(1:724)|134|(1:136)|137|(1:723)(1:143)|144|(1:146)(1:722)|147|(1:721)(1:151)|152|153|(4:695|(1:697)(1:719)|698|(2:699|(5:701|(1:703)(1:717)|704|(2:715|716)(2:712|713)|714)(1:718)))(9:157|(1:159)(1:694)|160|(1:162)(1:693)|163|(1:165)(1:692)|166|167|(2:168|(5:170|(1:172)(1:186)|173|(2:184|185)(2:181|182)|183)(1:187)))|188|189|(1:191)(1:691)|192|(1:194)|195|(1:203)|204|(3:206|(1:208)(1:210)|209)|211|(2:213|(1:215)(2:617|618))(1:(4:(4:630|(1:632)(1:687)|633|634)(1:688)|(6:636|(1:638)(1:685)|639|(3:641|(1:643)(1:679)|644)(3:680|(1:682)(1:684)|683)|645|646)(1:686)|647|(2:649|(4:651|(3:653|(1:655)(1:657)|656)|658|(3:660|(1:662)(1:664)|663))(5:665|(3:667|(1:669)(1:671)|670)|672|(3:674|(1:676)(1:678)|677)|618)))(3:623|(2:625|(1:627))|628))|(7:(1:218)|219|(1:221)|222|(1:235)(1:226)|227|(1:231))|236|(1:616)(1:240)|241|(3:247|(1:249)(1:251)|250)|252|(4:254|(1:560)(1:258)|259|(2:260|(1:262)(1:263)))(2:561|(8:590|591|(2:593|(2:595|(1:597)))|598|599|(1:609)(1:603)|604|(2:605|(1:607)(1:608)))(3:565|566|(5:571|572|(1:582)(1:576)|577|(2:578|(1:580)(1:581)))(1:570)))|264|265|(1:267)|268|269|270|(1:272)(1:558)|273|274|275|276|(6:278|(4:280|(1:282)|283|284)|286|(2:288|284)|283|284)|289|290|(4:524|525|(5:527|(2:529|(4:531|(2:533|(1:535))|536|(2:538|(2:540|(4:542|(1:546)|547|548)))))|549|547|548)|550)|292|(3:512|513|(35:515|516|(19:518|519|310|(1:504)(1:314)|315|316|(5:495|(1:498)|499|(1:501)(1:503)|502)(3:320|(2:322|(1:326))|327)|328|329|330|331|332|333|334|(10:336|(9:340|(1:342)|343|(2:347|(5:349|350|351|352|(2:354|(1:356)))(5:357|(1:359)(2:360|(3:362|(1:364)(1:366)|365))|351|352|(0)))|367|350|351|352|(0))|372|(3:376|(1:(1:385)(2:378|(1:380)(2:381|382)))|(1:384))|386|(3:390|(1:(1:399)(2:392|(1:394)(2:395|396)))|(1:398))|400|(2:406|(1:408))|409|(4:413|(1:415)|416|417))(10:436|(5:440|(1:442)|443|(4:445|(1:447)|448|(1:450))|451)|452|(4:456|(1:458)|459|460)|461|(4:465|(1:467)|468|469)|470|(4:474|(1:476)|477|478)|479|(1:483))|418|(4:(1:433)(1:427)|428|(1:430)(1:432)|431)|434|435)|297|(1:299)|506|(26:(1:509)|309|310|(1:312)|504|315|316|(1:318)|493|495|(1:498)|499|(0)(0)|502|328|329|330|331|332|333|334|(0)(0)|418|(7:420|422|(1:425)|433|428|(0)(0)|431)|434|435)|301|(1:505)(1:307)|308|309|310|(0)|504|315|316|(0)|493|495|(0)|499|(0)(0)|502|328|329|330|331|332|333|334|(0)(0)|418|(0)|434|435))|296|297|(0)|506|(0)|301|(1:303)|505|308|309|310|(0)|504|315|316|(0)|493|495|(0)|499|(0)(0)|502|328|329|330|331|332|333|334|(0)(0)|418|(0)|434|435) */
    /* JADX WARN: Code restructure failed: missing block: B:1084:0x1513, code lost:
        if (org.telegram.messenger.LocaleController.isRTL != false) goto L728;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1095:0x1539, code lost:
        if (org.telegram.messenger.LocaleController.isRTL != false) goto L728;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1100:0x1552, code lost:
        if (org.telegram.messenger.LocaleController.isRTL != false) goto L728;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1105:0x1566, code lost:
        if (org.telegram.messenger.LocaleController.isRTL != false) goto L728;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1114:0x1587, code lost:
        if (org.telegram.messenger.LocaleController.isRTL != false) goto L728;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1115:0x1589, code lost:
        r51.nameLeft += r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1503:0x1e34, code lost:
        if (org.telegram.messenger.SharedConfig.useThreeLinesLayout == false) goto L301;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1555:0x1f3d, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1556:0x1f3e, code lost:
        r2 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1557:0x1f3f, code lost:
        r1 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1558:0x1f41, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1559:0x1f42, code lost:
        r10 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x052c, code lost:
        if (android.text.TextUtils.isEmpty(r2.message) != false) goto L863;
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x05b0, code lost:
        if (r0.post_messages == false) goto L877;
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x09d3, code lost:
        if (r4.id == r12) goto L1314;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x09e1, code lost:
        if (org.telegram.messenger.ChatObject.isMegagroup(r51.chat) == false) goto L1314;
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x09e9, code lost:
        if (org.telegram.ui.Components.Forum.ForumUtilities.isTopicCreateMessage(r51.message) != false) goto L1314;
     */
    /* JADX WARN: Code restructure failed: missing block: B:994:0x1304, code lost:
        if (r6 == null) goto L953;
     */
    /* JADX WARN: Removed duplicated region for block: B:1030:0x1380  */
    /* JADX WARN: Removed duplicated region for block: B:1034:0x138c  */
    /* JADX WARN: Removed duplicated region for block: B:1039:0x13c5  */
    /* JADX WARN: Removed duplicated region for block: B:1042:0x13d2  */
    /* JADX WARN: Removed duplicated region for block: B:1048:0x1401  */
    /* JADX WARN: Removed duplicated region for block: B:1051:0x1408  */
    /* JADX WARN: Removed duplicated region for block: B:1052:0x141b  */
    /* JADX WARN: Removed duplicated region for block: B:1055:0x1438  */
    /* JADX WARN: Removed duplicated region for block: B:1061:0x1454  */
    /* JADX WARN: Removed duplicated region for block: B:1067:0x1481  */
    /* JADX WARN: Removed duplicated region for block: B:1081:0x14fe  */
    /* JADX WARN: Removed duplicated region for block: B:1088:0x151b  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0290  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x02f1  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0310  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x031f  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0328  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x032f  */
    /* JADX WARN: Removed duplicated region for block: B:1502:0x1e32 A[Catch: Exception -> 0x1ddd, TRY_ENTER, TRY_LEAVE, TryCatch #1 {Exception -> 0x1ddd, blocks: (B:1455:0x1db6, B:1457:0x1dc8, B:1459:0x1dce, B:1461:0x1dd2, B:1463:0x1dd8, B:1468:0x1de2, B:1470:0x1de6, B:1472:0x1dea, B:1474:0x1dee, B:1476:0x1df2, B:1480:0x1dff, B:1485:0x1e09, B:1502:0x1e32, B:1511:0x1e48, B:1513:0x1e4c, B:1515:0x1e5f, B:1521:0x1e8a, B:1523:0x1e8e, B:1528:0x1e9a, B:1544:0x1edf, B:1546:0x1ef1, B:1548:0x1ef7, B:1532:0x1ea4, B:1535:0x1eaa, B:1536:0x1eb1, B:1540:0x1ec3, B:1496:0x1e1f), top: B:1709:0x1db6 }] */
    /* JADX WARN: Removed duplicated region for block: B:1506:0x1e3c  */
    /* JADX WARN: Removed duplicated region for block: B:1521:0x1e8a A[Catch: Exception -> 0x1ddd, TRY_ENTER, TryCatch #1 {Exception -> 0x1ddd, blocks: (B:1455:0x1db6, B:1457:0x1dc8, B:1459:0x1dce, B:1461:0x1dd2, B:1463:0x1dd8, B:1468:0x1de2, B:1470:0x1de6, B:1472:0x1dea, B:1474:0x1dee, B:1476:0x1df2, B:1480:0x1dff, B:1485:0x1e09, B:1502:0x1e32, B:1511:0x1e48, B:1513:0x1e4c, B:1515:0x1e5f, B:1521:0x1e8a, B:1523:0x1e8e, B:1528:0x1e9a, B:1544:0x1edf, B:1546:0x1ef1, B:1548:0x1ef7, B:1532:0x1ea4, B:1535:0x1eaa, B:1536:0x1eb1, B:1540:0x1ec3, B:1496:0x1e1f), top: B:1709:0x1db6 }] */
    /* JADX WARN: Removed duplicated region for block: B:1528:0x1e9a A[Catch: Exception -> 0x1ddd, TRY_ENTER, TRY_LEAVE, TryCatch #1 {Exception -> 0x1ddd, blocks: (B:1455:0x1db6, B:1457:0x1dc8, B:1459:0x1dce, B:1461:0x1dd2, B:1463:0x1dd8, B:1468:0x1de2, B:1470:0x1de6, B:1472:0x1dea, B:1474:0x1dee, B:1476:0x1df2, B:1480:0x1dff, B:1485:0x1e09, B:1502:0x1e32, B:1511:0x1e48, B:1513:0x1e4c, B:1515:0x1e5f, B:1521:0x1e8a, B:1523:0x1e8e, B:1528:0x1e9a, B:1544:0x1edf, B:1546:0x1ef1, B:1548:0x1ef7, B:1532:0x1ea4, B:1535:0x1eaa, B:1536:0x1eb1, B:1540:0x1ec3, B:1496:0x1e1f), top: B:1709:0x1db6 }] */
    /* JADX WARN: Removed duplicated region for block: B:1534:0x1ea8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:1538:0x1ebe  */
    /* JADX WARN: Removed duplicated region for block: B:1539:0x1ec1  */
    /* JADX WARN: Removed duplicated region for block: B:1565:0x1f60  */
    /* JADX WARN: Removed duplicated region for block: B:1599:0x2037  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x037f  */
    /* JADX WARN: Removed duplicated region for block: B:1643:0x2121  */
    /* JADX WARN: Removed duplicated region for block: B:1690:0x2201  */
    /* JADX WARN: Removed duplicated region for block: B:1701:0x223d  */
    /* JADX WARN: Removed duplicated region for block: B:1703:0x2245  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x041b  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x049f  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x04b7  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x04be  */
    /* JADX WARN: Removed duplicated region for block: B:232:0x04e9  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x04f5  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x0532  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x056d  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x05a0  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x05b6  */
    /* JADX WARN: Removed duplicated region for block: B:312:0x05db  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x064f  */
    /* JADX WARN: Removed duplicated region for block: B:428:0x089b  */
    /* JADX WARN: Removed duplicated region for block: B:658:0x0c07  */
    /* JADX WARN: Removed duplicated region for block: B:659:0x0c0a  */
    /* JADX WARN: Removed duplicated region for block: B:717:0x0d78  */
    /* JADX WARN: Removed duplicated region for block: B:722:0x0d8e  */
    /* JADX WARN: Removed duplicated region for block: B:725:0x0db5  */
    /* JADX WARN: Removed duplicated region for block: B:795:0x0f6b  */
    /* JADX WARN: Removed duplicated region for block: B:800:0x0f7c  */
    /* JADX WARN: Removed duplicated region for block: B:810:0x0fd8  */
    /* JADX WARN: Removed duplicated region for block: B:860:0x10c7  */
    /* JADX WARN: Removed duplicated region for block: B:862:0x10cf  */
    /* JADX WARN: Removed duplicated region for block: B:871:0x10e3  */
    /* JADX WARN: Removed duplicated region for block: B:917:0x11a4  */
    /* JADX WARN: Removed duplicated region for block: B:959:0x1240  */
    /* JADX WARN: Removed duplicated region for block: B:968:0x1272  */
    /* JADX WARN: Removed duplicated region for block: B:972:0x1285  */
    /* JADX WARN: Removed duplicated region for block: B:973:0x128d  */
    /* JADX WARN: Removed duplicated region for block: B:983:0x12c5  */
    /* JADX WARN: Removed duplicated region for block: B:986:0x12d7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void buildLayout() {
        int i;
        String str;
        CharSequence charSequence;
        int i2;
        int dp;
        int i3;
        ScamDrawable scamDrawable;
        CharSequence charSequence2;
        boolean z;
        ScamDrawable scamDrawable2;
        int i4;
        TLRPC$DraftMessage tLRPC$DraftMessage;
        boolean z2;
        TLRPC$DraftMessage tLRPC$DraftMessage2;
        TLRPC$DraftMessage tLRPC$DraftMessage3;
        TLRPC$InputReplyTo tLRPC$InputReplyTo;
        boolean z3;
        TLRPC$DraftMessage tLRPC$DraftMessage4;
        SpannableStringBuilder spannableStringBuilder;
        boolean z4;
        int i5;
        SpannableStringBuilder spannableStringBuilder2;
        int i6;
        CharSequence string;
        String str2;
        boolean z5;
        ArrayList arrayList;
        CharSequence charSequence3;
        CharSequence charSequence4;
        boolean z6;
        CharSequence charSequence5;
        SpannableStringBuilder spannableStringBuilder3;
        boolean z7;
        CharSequence charSequence6;
        CharSequence charSequence7;
        TLRPC$Chat chat;
        CharSequence charSequence8;
        boolean z8;
        int i7;
        int i8;
        CharSequence charSequence9;
        int i9;
        char c;
        String str3;
        StringBuilder sb;
        String str4;
        CharSequence charSequence10;
        MessageObject messageObject;
        TextPaint textPaint;
        boolean isChannelAndNotMegaGroup;
        CharSequence charSequence11;
        CharSequence formatPluralString;
        char c2;
        int i10;
        int i11;
        String formatPluralString2;
        CharSequence charSequence12;
        CharSequence charSequence13;
        SpannableStringBuilder spannableStringBuilder4;
        CharSequence charSequence14;
        int i12;
        boolean z9;
        CharSequence charSequence15;
        CharSequence charSequence16;
        CharSequence replaceNewLines;
        CharSequence highlightText;
        CharSequence charSequence17;
        int i13;
        CharSequence replaceEmoji;
        CharSequence charSequence18;
        boolean z10;
        CharSequence highlightText2;
        Object foregroundColorSpanThemable;
        TLRPC$User tLRPC$User;
        MessageObject messageObject2;
        TLRPC$User tLRPC$User2;
        CharSequence charSequence19;
        CharSequence string2;
        int i14;
        int i15;
        String str5;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        ArrayList arrayList2;
        CharSequence charSequence20;
        int i16;
        CharSequence formatString;
        TLRPC$DraftMessage tLRPC$DraftMessage5;
        int i17;
        String str6;
        MessageObject messageObject3;
        boolean z11;
        String str7;
        String str8;
        MessagesController messagesController;
        String userName;
        int i18;
        CharSequence string3;
        int i19;
        SpannableStringBuilder spannableStringBuilder5;
        SpannableStringBuilder spannableStringBuilder6;
        CharSequence charSequence21;
        String str9;
        int i20;
        String str10;
        CharSequence charSequence22;
        boolean z12;
        int i21;
        boolean z13;
        boolean z14;
        boolean z15;
        String str11;
        String str12;
        boolean z16;
        boolean z17;
        MessageObject messageObject4;
        int i22;
        CharSequence charSequence23;
        CharSequence charSequence24;
        TLRPC$Chat tLRPC$Chat;
        MessageObject messageObject5;
        int dp2;
        CharSequence charSequence25;
        int i23;
        float f;
        int i24;
        int intrinsicWidth;
        int i25;
        int dp3;
        int dp4;
        int dp5;
        int dp6;
        int i26;
        DialogCellTags dialogCellTags;
        int dp7;
        int i27;
        int i28;
        int lineCount;
        int lineCount2;
        int lineCount3;
        StaticLayout staticLayout;
        float primaryHorizontal;
        float primaryHorizontal2;
        int i29;
        int lineCount4;
        int lineCount5;
        int lineCount6;
        double d;
        Drawable drawable;
        int i30;
        Object[] spans;
        CharSequence charSequence26;
        Layout.Alignment alignment;
        int i31;
        TextPaint textPaint2;
        float dp8;
        TextUtils.TruncateAt truncateAt;
        StaticLayout createStaticLayout;
        CharSequence highlightText3;
        DialogCellTags dialogCellTags2;
        int dp9;
        int dp10;
        DialogCellTags dialogCellTags3;
        CharSequence highlightText4;
        int intrinsicWidth2;
        int i32;
        int dp11;
        CustomDialog customDialog;
        CharSequence charSequence27;
        CharSequence charSequence28;
        int i33;
        String str13;
        int i34;
        SpannableStringBuilder formatInternal;
        int i35;
        boolean z18 = true;
        if (this.isTransitionSupport) {
            return;
        }
        if (this.isDialogCell && !this.updateHelper.update() && this.currentDialogFolderId == 0 && this.encryptedChat == null) {
            return;
        }
        if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
            Theme.dialogs_namePaint[0].setTextSize(AndroidUtilities.dp(17.0f));
            Theme.dialogs_nameEncryptedPaint[0].setTextSize(AndroidUtilities.dp(17.0f));
            Theme.dialogs_messagePaint[0].setTextSize(AndroidUtilities.dp(16.0f));
            Theme.dialogs_messagePrintingPaint[0].setTextSize(AndroidUtilities.dp(16.0f));
            Theme.dialogs_namePaint[1].setTextSize(AndroidUtilities.dp(16.0f));
            Theme.dialogs_nameEncryptedPaint[1].setTextSize(AndroidUtilities.dp(16.0f));
            Theme.dialogs_messagePaint[1].setTextSize(AndroidUtilities.dp(15.0f));
            Theme.dialogs_messagePrintingPaint[1].setTextSize(AndroidUtilities.dp(15.0f));
            TextPaint textPaint3 = Theme.dialogs_messagePaint[1];
            int color = Theme.getColor(Theme.key_chats_message_threeLines, this.resourcesProvider);
            textPaint3.linkColor = color;
            textPaint3.setColor(color);
            this.paintIndex = 1;
            i = 18;
        } else {
            Theme.dialogs_namePaint[0].setTextSize(AndroidUtilities.dp(17.0f));
            Theme.dialogs_nameEncryptedPaint[0].setTextSize(AndroidUtilities.dp(17.0f));
            Theme.dialogs_messagePaint[0].setTextSize(AndroidUtilities.dp(16.0f));
            Theme.dialogs_messagePrintingPaint[0].setTextSize(AndroidUtilities.dp(16.0f));
            TextPaint textPaint4 = Theme.dialogs_messagePaint[0];
            int color2 = Theme.getColor(Theme.key_chats_message, this.resourcesProvider);
            textPaint4.linkColor = color2;
            textPaint4.setColor(color2);
            this.paintIndex = 0;
            i = 19;
        }
        this.thumbSize = i;
        this.currentDialogFolderDialogsCount = 0;
        if (isForumCell() || !(this.isDialogCell || this.isTopic)) {
            str = "**reaction**";
            charSequence = null;
        } else {
            str = "**reaction**";
            charSequence = MessagesController.getInstance(this.currentAccount).getPrintingString(this.currentDialogId, getTopicId(), true);
        }
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
        boolean z19 = (UserObject.isUserSelf(this.user) || this.useMeForMyMessages) ? false : true;
        this.printingStringType = -1;
        if (!isForumCell()) {
            this.buttonLayout = null;
        }
        if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && this.currentDialogFolderId == 0 && !isForumCell() && !hasTags()) {
            this.hasNameInMessage = false;
            i2 = 2;
        } else {
            this.hasNameInMessage = true;
            i2 = 1;
        }
        MessageObject messageObject6 = this.message;
        if (messageObject6 != null) {
            messageObject6.updateTranslation();
        }
        MessageObject messageObject7 = this.message;
        CharSequence charSequence29 = messageObject7 != null ? messageObject7.messageText : null;
        boolean z20 = charSequence29 instanceof Spannable;
        CharSequence charSequence30 = charSequence29;
        if (z20) {
            SpannableStringBuilder spannableStringBuilder7 = new SpannableStringBuilder(charSequence29);
            for (URLSpanNoUnderlineBold uRLSpanNoUnderlineBold : (URLSpanNoUnderlineBold[]) spannableStringBuilder7.getSpans(0, spannableStringBuilder7.length(), URLSpanNoUnderlineBold.class)) {
                spannableStringBuilder7.removeSpan(uRLSpanNoUnderlineBold);
            }
            for (URLSpanNoUnderline uRLSpanNoUnderline : (URLSpanNoUnderline[]) spannableStringBuilder7.getSpans(0, spannableStringBuilder7.length(), URLSpanNoUnderline.class)) {
                spannableStringBuilder7.removeSpan(uRLSpanNoUnderline);
            }
            charSequence30 = spannableStringBuilder7;
        }
        this.lastMessageString = charSequence30;
        CustomDialog customDialog2 = this.customDialog;
        if (customDialog2 != null) {
            if (customDialog2.type == 2) {
                this.drawNameLock = true;
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    this.nameLockTop = AndroidUtilities.dp(12.5f);
                    if (LocaleController.isRTL) {
                        this.nameLockLeft = (getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 6)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        dp11 = AndroidUtilities.dp(22.0f);
                        this.nameLeft = dp11;
                        customDialog = this.customDialog;
                        if (customDialog.type != 1) {
                            charSequence28 = LocaleController.getString(R.string.FromYou);
                            CustomDialog customDialog3 = this.customDialog;
                            if (customDialog3.isMedia) {
                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                formatInternal = formatInternal(i2, this.message.messageText, null);
                                formatInternal.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage, this.resourcesProvider), 0, formatInternal.length(), 33);
                            } else {
                                String str14 = customDialog3.message;
                                if (str14.length() > 150) {
                                    str14 = str14.substring(0, 150);
                                }
                                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                    str14 = str14.replace('\n', ' ');
                                }
                                formatInternal = formatInternal(i2, str14, charSequence28);
                            }
                            charSequence27 = Emoji.replaceEmoji((CharSequence) formatInternal, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                            z12 = false;
                        } else {
                            charSequence27 = customDialog.message;
                            if (customDialog.isMedia) {
                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                            }
                            charSequence28 = null;
                            z12 = true;
                        }
                        str6 = LocaleController.stringForMessageListDate(this.customDialog.date);
                        i33 = this.customDialog.unread_count;
                        if (i33 == 0) {
                            this.drawCount = true;
                            str13 = String.format("%d", Integer.valueOf(i33));
                        } else {
                            this.drawCount = false;
                            str13 = null;
                        }
                        CustomDialog customDialog4 = this.customDialog;
                        i34 = customDialog4.sent;
                        if (i34 != 0) {
                            this.drawClock = true;
                        } else {
                            if (i34 == 2) {
                                this.drawCheck1 = true;
                            } else if (i34 == 1) {
                                this.drawCheck1 = false;
                            } else {
                                this.drawClock = false;
                            }
                            this.drawCheck2 = true;
                            this.drawClock = false;
                            this.drawError = false;
                            charSequence22 = customDialog4.name;
                            charSequence25 = charSequence28;
                            str9 = str13;
                            spannableStringBuilder6 = "";
                            str10 = null;
                            i20 = -1;
                            spannableStringBuilder5 = null;
                            charSequence21 = charSequence27;
                        }
                        this.drawCheck1 = false;
                        this.drawCheck2 = false;
                        this.drawError = false;
                        charSequence22 = customDialog4.name;
                        charSequence25 = charSequence28;
                        str9 = str13;
                        spannableStringBuilder6 = "";
                        str10 = null;
                        i20 = -1;
                        spannableStringBuilder5 = null;
                        charSequence21 = charSequence27;
                    } else {
                        this.nameLockLeft = AndroidUtilities.dp(this.messagePaddingStart + 6);
                        i35 = this.messagePaddingStart + 10;
                    }
                } else {
                    this.nameLockTop = AndroidUtilities.dp(16.5f);
                    if (LocaleController.isRTL) {
                        this.nameLockLeft = (getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 4)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        dp11 = AndroidUtilities.dp(18.0f);
                        this.nameLeft = dp11;
                        customDialog = this.customDialog;
                        if (customDialog.type != 1) {
                        }
                        str6 = LocaleController.stringForMessageListDate(this.customDialog.date);
                        i33 = this.customDialog.unread_count;
                        if (i33 == 0) {
                        }
                        CustomDialog customDialog42 = this.customDialog;
                        i34 = customDialog42.sent;
                        if (i34 != 0) {
                        }
                        this.drawCheck1 = false;
                        this.drawCheck2 = false;
                        this.drawError = false;
                        charSequence22 = customDialog42.name;
                        charSequence25 = charSequence28;
                        str9 = str13;
                        spannableStringBuilder6 = "";
                        str10 = null;
                        i20 = -1;
                        spannableStringBuilder5 = null;
                        charSequence21 = charSequence27;
                    } else {
                        this.nameLockLeft = AndroidUtilities.dp(this.messagePaddingStart + 4);
                        i35 = this.messagePaddingStart + 8;
                    }
                }
                dp11 = AndroidUtilities.dp(i35) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                this.nameLeft = dp11;
                customDialog = this.customDialog;
                if (customDialog.type != 1) {
                }
                str6 = LocaleController.stringForMessageListDate(this.customDialog.date);
                i33 = this.customDialog.unread_count;
                if (i33 == 0) {
                }
                CustomDialog customDialog422 = this.customDialog;
                i34 = customDialog422.sent;
                if (i34 != 0) {
                }
                this.drawCheck1 = false;
                this.drawCheck2 = false;
                this.drawError = false;
                charSequence22 = customDialog422.name;
                charSequence25 = charSequence28;
                str9 = str13;
                spannableStringBuilder6 = "";
                str10 = null;
                i20 = -1;
                spannableStringBuilder5 = null;
                charSequence21 = charSequence27;
            } else {
                this.drawVerified = !this.forbidVerified && customDialog2.verified;
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    if (!LocaleController.isRTL) {
                        i32 = this.messagePaddingStart + 6;
                    }
                    dp11 = AndroidUtilities.dp(22.0f);
                    this.nameLeft = dp11;
                    customDialog = this.customDialog;
                    if (customDialog.type != 1) {
                    }
                    str6 = LocaleController.stringForMessageListDate(this.customDialog.date);
                    i33 = this.customDialog.unread_count;
                    if (i33 == 0) {
                    }
                    CustomDialog customDialog4222 = this.customDialog;
                    i34 = customDialog4222.sent;
                    if (i34 != 0) {
                    }
                    this.drawCheck1 = false;
                    this.drawCheck2 = false;
                    this.drawError = false;
                    charSequence22 = customDialog4222.name;
                    charSequence25 = charSequence28;
                    str9 = str13;
                    spannableStringBuilder6 = "";
                    str10 = null;
                    i20 = -1;
                    spannableStringBuilder5 = null;
                    charSequence21 = charSequence27;
                } else {
                    if (!LocaleController.isRTL) {
                        i32 = this.messagePaddingStart + 4;
                    }
                    dp11 = AndroidUtilities.dp(18.0f);
                    this.nameLeft = dp11;
                    customDialog = this.customDialog;
                    if (customDialog.type != 1) {
                    }
                    str6 = LocaleController.stringForMessageListDate(this.customDialog.date);
                    i33 = this.customDialog.unread_count;
                    if (i33 == 0) {
                    }
                    CustomDialog customDialog42222 = this.customDialog;
                    i34 = customDialog42222.sent;
                    if (i34 != 0) {
                    }
                    this.drawCheck1 = false;
                    this.drawCheck2 = false;
                    this.drawError = false;
                    charSequence22 = customDialog42222.name;
                    charSequence25 = charSequence28;
                    str9 = str13;
                    spannableStringBuilder6 = "";
                    str10 = null;
                    i20 = -1;
                    spannableStringBuilder5 = null;
                    charSequence21 = charSequence27;
                }
                dp11 = AndroidUtilities.dp(i32);
                this.nameLeft = dp11;
                customDialog = this.customDialog;
                if (customDialog.type != 1) {
                }
                str6 = LocaleController.stringForMessageListDate(this.customDialog.date);
                i33 = this.customDialog.unread_count;
                if (i33 == 0) {
                }
                CustomDialog customDialog422222 = this.customDialog;
                i34 = customDialog422222.sent;
                if (i34 != 0) {
                }
                this.drawCheck1 = false;
                this.drawCheck2 = false;
                this.drawError = false;
                charSequence22 = customDialog422222.name;
                charSequence25 = charSequence28;
                str9 = str13;
                spannableStringBuilder6 = "";
                str10 = null;
                i20 = -1;
                spannableStringBuilder5 = null;
                charSequence21 = charSequence27;
            }
        } else if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
            if (LocaleController.isRTL) {
                dp = AndroidUtilities.dp(22.0f);
                this.nameLeft = dp;
                if (this.encryptedChat == null) {
                }
                charSequence2 = charSequence;
                i4 = this.lastMessageDate;
                if (i4 == 0) {
                }
                if (this.isTopic) {
                }
                tLRPC$DraftMessage = null;
                this.draftMessage = tLRPC$DraftMessage;
                z2 = this.draftVoice;
                if (!z2) {
                }
                if (ChatObject.isChannel(this.chat)) {
                }
                tLRPC$Chat = this.chat;
                if (tLRPC$Chat != null) {
                }
                tLRPC$DraftMessage4 = null;
                z3 = false;
                if (isForumCell()) {
                }
                z7 = true;
                charSequence6 = charSequence24;
                charSequence5 = charSequence23;
                tLRPC$DraftMessage5 = this.draftMessage;
                if (tLRPC$DraftMessage5 == null) {
                }
                str6 = LocaleController.stringForMessageListDate(i17);
                messageObject3 = this.message;
                if (messageObject3 != null) {
                }
                this.drawCheck1 = false;
                this.drawCheck2 = false;
                this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                z11 = false;
                this.drawCount = false;
                this.drawMention = false;
                this.drawReactionMention = false;
                this.drawError = false;
                str7 = null;
                str8 = null;
                this.promoDialog = z11;
                messagesController = MessagesController.getInstance(this.currentAccount);
                if (this.dialogsType == 0) {
                }
                if (this.currentDialogFolderId != 0) {
                }
                string3 = LocaleController.getString(i19);
                spannableStringBuilder5 = spannableStringBuilder3;
                z18 = z7;
                spannableStringBuilder6 = spannableStringBuilder2;
                charSequence21 = charSequence5;
                str9 = str7;
                i20 = i5;
                str10 = str8;
                charSequence22 = string3;
                z12 = z4;
                charSequence25 = charSequence6;
            } else {
                i3 = this.messagePaddingStart + 6;
                dp = AndroidUtilities.dp(i3);
                this.nameLeft = dp;
                if (this.encryptedChat == null) {
                    if (this.currentDialogFolderId == 0) {
                        this.drawNameLock = true;
                        if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                            this.nameLockTop = AndroidUtilities.dp(12.5f);
                            if (LocaleController.isRTL) {
                                this.nameLockLeft = (getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 6)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                                dp2 = AndroidUtilities.dp(22.0f);
                            } else {
                                this.nameLockLeft = AndroidUtilities.dp(this.messagePaddingStart + 6);
                                dp2 = AndroidUtilities.dp(this.messagePaddingStart + 10) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                            }
                        } else {
                            this.nameLockTop = AndroidUtilities.dp(16.5f);
                            if (LocaleController.isRTL) {
                                this.nameLockLeft = (getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 4)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                                dp2 = AndroidUtilities.dp(18.0f);
                            } else {
                                this.nameLockLeft = AndroidUtilities.dp(this.messagePaddingStart + 4);
                                dp2 = AndroidUtilities.dp(this.messagePaddingStart + 8) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                            }
                        }
                        this.nameLeft = dp2;
                    }
                } else if (this.currentDialogFolderId == 0) {
                    TLRPC$Chat tLRPC$Chat2 = this.chat;
                    if (tLRPC$Chat2 != null) {
                        if (tLRPC$Chat2.scam) {
                            this.drawScam = 1;
                            scamDrawable2 = Theme.dialogs_scamDrawable;
                        } else if (tLRPC$Chat2.fake) {
                            this.drawScam = 2;
                            scamDrawable2 = Theme.dialogs_fakeDrawable;
                        } else if (DialogObject.getEmojiStatusDocumentId(tLRPC$Chat2.emoji_status) != 0) {
                            this.drawPremium = true;
                            this.nameLayoutEllipsizeByGradient = true;
                            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatus;
                            swapAnimatedEmojiDrawable.center = LocaleController.isRTL;
                            swapAnimatedEmojiDrawable.set(DialogObject.getEmojiStatusDocumentId(this.chat.emoji_status), false);
                        } else {
                            this.drawVerified = !this.forbidVerified && this.chat.verified;
                        }
                        scamDrawable2.checkText();
                    } else {
                        TLRPC$User tLRPC$User3 = this.user;
                        if (tLRPC$User3 != null) {
                            if (tLRPC$User3.scam) {
                                this.drawScam = 1;
                                scamDrawable = Theme.dialogs_scamDrawable;
                            } else if (tLRPC$User3.fake) {
                                this.drawScam = 2;
                                scamDrawable = Theme.dialogs_fakeDrawable;
                            } else {
                                this.drawVerified = !this.forbidVerified && tLRPC$User3.verified;
                                if (MessagesController.getInstance(this.currentAccount).isPremiumUser(this.user)) {
                                    charSequence2 = charSequence;
                                } else {
                                    long j = UserConfig.getInstance(this.currentAccount).clientUserId;
                                    charSequence2 = charSequence;
                                    long j2 = this.user.id;
                                    if (j != j2 && j2 != 0) {
                                        z = true;
                                        this.drawPremium = z;
                                        if (z) {
                                            Long emojiStatusDocumentId = UserObject.getEmojiStatusDocumentId(this.user);
                                            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable2 = this.emojiStatus;
                                            swapAnimatedEmojiDrawable2.center = LocaleController.isRTL;
                                            if (emojiStatusDocumentId != null) {
                                                this.nameLayoutEllipsizeByGradient = true;
                                                swapAnimatedEmojiDrawable2.set(emojiStatusDocumentId.longValue(), false);
                                            } else {
                                                this.nameLayoutEllipsizeByGradient = true;
                                                swapAnimatedEmojiDrawable2.set(PremiumGradient.getInstance().premiumStarDrawableMini, false);
                                            }
                                        }
                                        i4 = this.lastMessageDate;
                                        if (i4 == 0 && (messageObject5 = this.message) != null) {
                                            i4 = messageObject5.messageOwner.date;
                                        }
                                        if (this.isTopic) {
                                            boolean z21 = MediaDataController.getInstance(this.currentAccount).getDraftVoice(this.currentDialogId, (long) getTopicId()) != null;
                                            this.draftVoice = z21;
                                            TLRPC$DraftMessage draft = !z21 ? MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, getTopicId()) : null;
                                            this.draftMessage = draft;
                                            if (draft != null) {
                                            }
                                            z2 = this.draftVoice;
                                            if ((!z2 && this.draftMessage == null) || ((z2 || (tLRPC$DraftMessage3 = this.draftMessage) == null || !TextUtils.isEmpty(tLRPC$DraftMessage3.message) || ((tLRPC$InputReplyTo = this.draftMessage.reply_to) != null && tLRPC$InputReplyTo.reply_to_msg_id != 0)) && ((tLRPC$DraftMessage2 = this.draftMessage) == null || i4 <= tLRPC$DraftMessage2.date || this.unreadCount == 0))) {
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
                                                if ((tLRPC$Chat != null || (!tLRPC$Chat.left && !tLRPC$Chat.kicked)) && !this.forbidDraft && (!ChatObject.isForum(tLRPC$Chat) || this.isTopic)) {
                                                    tLRPC$DraftMessage4 = null;
                                                    z3 = false;
                                                    if (isForumCell()) {
                                                        this.draftMessage = tLRPC$DraftMessage4;
                                                        this.draftVoice = z3;
                                                        this.needEmoji = true;
                                                        updateMessageThumbs();
                                                        CharSequence removeDiacritics = AndroidUtilities.removeDiacritics(getMessageNameString());
                                                        CharSequence formatTopicsNames = formatTopicsNames();
                                                        MessageObject messageObject8 = this.message;
                                                        String messageStringFormatted = this.message != null ? getMessageStringFormatted(i2, messageObject8 != null ? MessagesController.getInstance(messageObject8.currentAccount).getRestrictionReason(this.message.messageOwner.restriction_reason) : null, removeDiacritics, true) : "";
                                                        if (this.applyName && messageStringFormatted.length() >= 0 && removeDiacritics != null) {
                                                            messageStringFormatted = SpannableStringBuilder.valueOf(messageStringFormatted);
                                                            messageStringFormatted.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_name, this.resourcesProvider), 0, Math.min(messageStringFormatted.length(), removeDiacritics.length() + 1), 0);
                                                        }
                                                        this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                                                        charSequence24 = removeDiacritics;
                                                        spannableStringBuilder3 = messageStringFormatted;
                                                        spannableStringBuilder2 = "";
                                                        z4 = true;
                                                        i5 = -1;
                                                        charSequence23 = formatTopicsNames;
                                                    } else {
                                                        if (charSequence2 != null) {
                                                            CharSequence charSequence31 = charSequence2;
                                                            this.lastPrintString = charSequence31;
                                                            int intValue = MessagesController.getInstance(this.currentAccount).getPrintingStringType(this.currentDialogId, getTopicId()).intValue();
                                                            this.printingStringType = intValue;
                                                            StatusDrawable chatStatusDrawable = Theme.getChatStatusDrawable(intValue);
                                                            int intrinsicWidth3 = chatStatusDrawable != null ? chatStatusDrawable.getIntrinsicWidth() + AndroidUtilities.dp(3.0f) : 0;
                                                            spannableStringBuilder = new SpannableStringBuilder();
                                                            CharSequence replace = TextUtils.replace(charSequence31, new String[]{"..."}, new String[]{""});
                                                            int indexOf = this.printingStringType == 5 ? replace.toString().indexOf("**oo**") : -1;
                                                            if (indexOf >= 0) {
                                                                spannableStringBuilder.append(replace).setSpan(new FixedWidthSpan(Theme.getChatStatusDrawable(this.printingStringType).getIntrinsicWidth()), indexOf, indexOf + 6, 0);
                                                            } else {
                                                                spannableStringBuilder.append((CharSequence) " ").append(replace).setSpan(new FixedWidthSpan(intrinsicWidth3), 0, 1, 0);
                                                            }
                                                            i5 = indexOf;
                                                            z4 = false;
                                                        } else {
                                                            this.lastPrintString = null;
                                                            this.printingStringType = -1;
                                                            spannableStringBuilder = "";
                                                            z4 = true;
                                                            i5 = -1;
                                                        }
                                                        if (this.draftVoice || this.draftMessage != null) {
                                                            spannableStringBuilder2 = spannableStringBuilder;
                                                            i6 = i5;
                                                            string = LocaleController.getString(R.string.Draft);
                                                            TLRPC$DraftMessage tLRPC$DraftMessage6 = this.draftMessage;
                                                            if (tLRPC$DraftMessage6 == null || !TextUtils.isEmpty(tLRPC$DraftMessage6.message)) {
                                                                if (this.draftVoice) {
                                                                    str2 = LocaleController.getString(R.string.AttachAudio);
                                                                } else {
                                                                    TLRPC$DraftMessage tLRPC$DraftMessage7 = this.draftMessage;
                                                                    if (tLRPC$DraftMessage7 != null) {
                                                                        str2 = tLRPC$DraftMessage7.message;
                                                                        if (str2.length() > 150) {
                                                                            str2 = str2.substring(0, 150);
                                                                        }
                                                                    } else {
                                                                        str2 = "";
                                                                    }
                                                                }
                                                                SpannableStringBuilder spannableStringBuilder8 = new SpannableStringBuilder(str2);
                                                                TLRPC$DraftMessage tLRPC$DraftMessage8 = this.draftMessage;
                                                                if (tLRPC$DraftMessage8 != null) {
                                                                    MediaDataController.addTextStyleRuns(tLRPC$DraftMessage8, spannableStringBuilder8, (int) NotificationCenter.reactionsDidLoad);
                                                                    TLRPC$DraftMessage tLRPC$DraftMessage9 = this.draftMessage;
                                                                    if (tLRPC$DraftMessage9 != null && (arrayList = tLRPC$DraftMessage9.entities) != null) {
                                                                        TextPaint textPaint5 = this.currentMessagePaint;
                                                                        MediaDataController.addAnimatedEmojiSpans(arrayList, spannableStringBuilder8, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                    }
                                                                } else if (this.draftVoice) {
                                                                    spannableStringBuilder8.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_actionMessage, this.resourcesProvider), 0, spannableStringBuilder8.length(), 33);
                                                                }
                                                                SpannableStringBuilder formatInternal2 = formatInternal(i2, AndroidUtilities.replaceNewLines(spannableStringBuilder8), string);
                                                                if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
                                                                    z5 = false;
                                                                } else {
                                                                    z5 = false;
                                                                    formatInternal2.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_draft, this.resourcesProvider), 0, string.length() + 1, 33);
                                                                }
                                                                charSequence3 = Emoji.replaceEmoji(formatInternal2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), z5);
                                                            } else if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
                                                                z6 = false;
                                                                i5 = i6;
                                                                charSequence5 = "";
                                                                spannableStringBuilder3 = null;
                                                                z7 = true;
                                                                boolean z22 = z6;
                                                                charSequence6 = string;
                                                                z4 = z22;
                                                                tLRPC$DraftMessage5 = this.draftMessage;
                                                                if (tLRPC$DraftMessage5 == null) {
                                                                    i17 = tLRPC$DraftMessage5.date;
                                                                } else {
                                                                    i17 = this.lastMessageDate;
                                                                    if (i17 == 0) {
                                                                        MessageObject messageObject9 = this.message;
                                                                        if (messageObject9 != null) {
                                                                            i17 = messageObject9.messageOwner.date;
                                                                        } else {
                                                                            str6 = "";
                                                                            messageObject3 = this.message;
                                                                            if (messageObject3 != null || this.isSavedDialog) {
                                                                                this.drawCheck1 = false;
                                                                                this.drawCheck2 = false;
                                                                                this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                                                z11 = false;
                                                                                this.drawCount = false;
                                                                                this.drawMention = false;
                                                                                this.drawReactionMention = false;
                                                                                this.drawError = false;
                                                                                str7 = null;
                                                                                str8 = null;
                                                                            } else {
                                                                                if (this.currentDialogFolderId != 0) {
                                                                                    int i36 = this.unreadCount;
                                                                                    int i37 = this.mentionCount;
                                                                                    int i38 = i36 + i37;
                                                                                    if (i38 <= 0) {
                                                                                        this.drawCount = false;
                                                                                        this.drawMention = false;
                                                                                        str7 = null;
                                                                                    } else if (i36 > i37) {
                                                                                        this.drawCount = true;
                                                                                        this.drawMention = false;
                                                                                        str7 = String.format("%d", Integer.valueOf(i38));
                                                                                    } else {
                                                                                        this.drawCount = false;
                                                                                        this.drawMention = true;
                                                                                        str12 = String.format("%d", Integer.valueOf(i38));
                                                                                        str7 = null;
                                                                                        z14 = false;
                                                                                    }
                                                                                    str12 = null;
                                                                                    z14 = false;
                                                                                } else {
                                                                                    if (this.clearingDialog) {
                                                                                        this.drawCount = false;
                                                                                        str7 = null;
                                                                                        z13 = true;
                                                                                        z14 = false;
                                                                                        z15 = false;
                                                                                    } else {
                                                                                        int i39 = this.unreadCount;
                                                                                        z13 = true;
                                                                                        if (i39 == 0 || (i39 == 1 && i39 == this.mentionCount && messageObject3.messageOwner.mentioned)) {
                                                                                            z14 = false;
                                                                                            if (this.markUnread) {
                                                                                                this.drawCount = true;
                                                                                                z15 = z19;
                                                                                                str7 = "";
                                                                                            } else {
                                                                                                this.drawCount = false;
                                                                                                z15 = z19;
                                                                                                str7 = null;
                                                                                            }
                                                                                        } else {
                                                                                            this.drawCount = true;
                                                                                            z14 = false;
                                                                                            str7 = String.format("%d", Integer.valueOf(i39));
                                                                                            z15 = z19;
                                                                                        }
                                                                                    }
                                                                                    if (this.mentionCount != 0) {
                                                                                        this.drawMention = z13;
                                                                                        str11 = "@";
                                                                                    } else {
                                                                                        this.drawMention = z14;
                                                                                        str11 = null;
                                                                                    }
                                                                                    if (this.reactionMentionCount > 0) {
                                                                                        this.drawReactionMention = z13;
                                                                                        z16 = z15;
                                                                                        if (this.message.isOut() && this.draftMessage == null && z16) {
                                                                                            messageObject4 = this.message;
                                                                                            if (!(messageObject4.messageOwner.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                                                                                                if (messageObject4.isSending()) {
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
                                                                                                    this.drawCheck1 = tLRPC$TL_forumTopic == null ? !this.isDialogCell ? !(!this.message.isUnread() || (ChatObject.isChannel(this.chat) && !this.chat.megagroup)) : !(((i22 = this.readOutboxMaxId) > 0 && i22 >= this.message.getId()) || !this.message.isUnread() || (ChatObject.isChannel(this.chat) && !this.chat.megagroup)) : tLRPC$TL_forumTopic.read_outbox_max_id >= this.message.getId();
                                                                                                    this.drawCheck2 = true;
                                                                                                    z17 = false;
                                                                                                    this.drawClock = z17;
                                                                                                    this.drawError = z17;
                                                                                                    str8 = str11;
                                                                                                    z11 = false;
                                                                                                }
                                                                                                str8 = str11;
                                                                                                z11 = false;
                                                                                            }
                                                                                        }
                                                                                        z17 = false;
                                                                                        this.drawCheck1 = false;
                                                                                        this.drawCheck2 = false;
                                                                                        this.drawClock = z17;
                                                                                        this.drawError = z17;
                                                                                        str8 = str11;
                                                                                        z11 = false;
                                                                                    } else {
                                                                                        str12 = str11;
                                                                                        z19 = z15;
                                                                                    }
                                                                                }
                                                                                this.drawReactionMention = z14;
                                                                                String str15 = str12;
                                                                                z16 = z19;
                                                                                str11 = str15;
                                                                                if (this.message.isOut()) {
                                                                                    messageObject4 = this.message;
                                                                                    if (!(messageObject4.messageOwner.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                                                                                    }
                                                                                }
                                                                                z17 = false;
                                                                                this.drawCheck1 = false;
                                                                                this.drawCheck2 = false;
                                                                                this.drawClock = z17;
                                                                                this.drawError = z17;
                                                                                str8 = str11;
                                                                                z11 = false;
                                                                            }
                                                                            this.promoDialog = z11;
                                                                            messagesController = MessagesController.getInstance(this.currentAccount);
                                                                            if (this.dialogsType == 0 && messagesController.isPromoDialog(this.currentDialogId, true)) {
                                                                                this.drawPinBackground = true;
                                                                                this.promoDialog = true;
                                                                                i21 = messagesController.promoDialogType;
                                                                                if (i21 != MessagesController.PROMO_TYPE_PROXY) {
                                                                                    str6 = LocaleController.getString(R.string.UseProxySponsor);
                                                                                } else if (i21 == MessagesController.PROMO_TYPE_PSA) {
                                                                                    str6 = LocaleController.getString("PsaType_" + messagesController.promoPsaType);
                                                                                    if (TextUtils.isEmpty(str6)) {
                                                                                        str6 = LocaleController.getString(R.string.PsaTypeDefault);
                                                                                    }
                                                                                    if (!TextUtils.isEmpty(messagesController.promoPsaMessage)) {
                                                                                        charSequence5 = messagesController.promoPsaMessage;
                                                                                        this.thumbsCount = 0;
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (this.currentDialogFolderId != 0) {
                                                                                i19 = R.string.ArchivedChats;
                                                                            } else {
                                                                                TLRPC$Chat tLRPC$Chat4 = this.chat;
                                                                                if (tLRPC$Chat4 != null) {
                                                                                    if (this.useFromUserAsAvatar) {
                                                                                        if (this.topicIconInName == null) {
                                                                                            this.topicIconInName = new Drawable[1];
                                                                                        }
                                                                                        this.topicIconInName[0] = null;
                                                                                        string3 = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopicIconName(this.chat, this.message, this.currentMessagePaint, this.topicIconInName);
                                                                                    } else {
                                                                                        if (this.isTopic) {
                                                                                            if (this.topicIconInName == null) {
                                                                                                this.topicIconInName = new Drawable[1];
                                                                                            }
                                                                                            Drawable[] drawableArr = this.topicIconInName;
                                                                                            drawableArr[0] = null;
                                                                                            if (this.showTopicIconInName) {
                                                                                                string3 = ForumUtilities.getTopicSpannedName(this.forumTopic, Theme.dialogs_namePaint[this.paintIndex], drawableArr, false);
                                                                                            } else {
                                                                                                userName = this.forumTopic.title;
                                                                                            }
                                                                                        } else {
                                                                                            userName = tLRPC$Chat4.title;
                                                                                        }
                                                                                        string3 = AndroidUtilities.removeDiacritics(userName);
                                                                                    }
                                                                                    if (string3 != null && string3.length() == 0) {
                                                                                        i19 = R.string.HiddenName;
                                                                                    }
                                                                                    spannableStringBuilder5 = spannableStringBuilder3;
                                                                                    z18 = z7;
                                                                                    spannableStringBuilder6 = spannableStringBuilder2;
                                                                                    charSequence21 = charSequence5;
                                                                                    str9 = str7;
                                                                                    i20 = i5;
                                                                                    str10 = str8;
                                                                                    charSequence22 = string3;
                                                                                    z12 = z4;
                                                                                    charSequence25 = charSequence6;
                                                                                } else {
                                                                                    TLRPC$User tLRPC$User4 = this.user;
                                                                                    if (tLRPC$User4 != null) {
                                                                                        if (UserObject.isReplyUser(tLRPC$User4)) {
                                                                                            i18 = R.string.RepliesTitle;
                                                                                        } else if (UserObject.isAnonymous(this.user)) {
                                                                                            i18 = R.string.AnonymousForward;
                                                                                        } else if (!UserObject.isUserSelf(this.user)) {
                                                                                            userName = UserObject.getUserName(this.user);
                                                                                            string3 = AndroidUtilities.removeDiacritics(userName);
                                                                                            if (string3 != null) {
                                                                                                i19 = R.string.HiddenName;
                                                                                            }
                                                                                            spannableStringBuilder5 = spannableStringBuilder3;
                                                                                            z18 = z7;
                                                                                            spannableStringBuilder6 = spannableStringBuilder2;
                                                                                            charSequence21 = charSequence5;
                                                                                            str9 = str7;
                                                                                            i20 = i5;
                                                                                            str10 = str8;
                                                                                            charSequence22 = string3;
                                                                                            z12 = z4;
                                                                                            charSequence25 = charSequence6;
                                                                                        } else if (this.isSavedDialog) {
                                                                                            i18 = R.string.MyNotes;
                                                                                        } else if (this.useMeForMyMessages) {
                                                                                            i18 = R.string.FromYou;
                                                                                        } else {
                                                                                            if (this.dialogsType == 3) {
                                                                                                this.drawPinBackground = true;
                                                                                            }
                                                                                            i18 = R.string.SavedMessages;
                                                                                        }
                                                                                        string3 = LocaleController.getString(i18);
                                                                                        if (string3 != null) {
                                                                                        }
                                                                                        spannableStringBuilder5 = spannableStringBuilder3;
                                                                                        z18 = z7;
                                                                                        spannableStringBuilder6 = spannableStringBuilder2;
                                                                                        charSequence21 = charSequence5;
                                                                                        str9 = str7;
                                                                                        i20 = i5;
                                                                                        str10 = str8;
                                                                                        charSequence22 = string3;
                                                                                        z12 = z4;
                                                                                        charSequence25 = charSequence6;
                                                                                    }
                                                                                    string3 = "";
                                                                                    if (string3 != null) {
                                                                                    }
                                                                                    spannableStringBuilder5 = spannableStringBuilder3;
                                                                                    z18 = z7;
                                                                                    spannableStringBuilder6 = spannableStringBuilder2;
                                                                                    charSequence21 = charSequence5;
                                                                                    str9 = str7;
                                                                                    i20 = i5;
                                                                                    str10 = str8;
                                                                                    charSequence22 = string3;
                                                                                    z12 = z4;
                                                                                    charSequence25 = charSequence6;
                                                                                }
                                                                            }
                                                                            string3 = LocaleController.getString(i19);
                                                                            spannableStringBuilder5 = spannableStringBuilder3;
                                                                            z18 = z7;
                                                                            spannableStringBuilder6 = spannableStringBuilder2;
                                                                            charSequence21 = charSequence5;
                                                                            str9 = str7;
                                                                            i20 = i5;
                                                                            str10 = str8;
                                                                            charSequence22 = string3;
                                                                            z12 = z4;
                                                                            charSequence25 = charSequence6;
                                                                        }
                                                                    }
                                                                }
                                                                str6 = LocaleController.stringForMessageListDate(i17);
                                                                messageObject3 = this.message;
                                                                if (messageObject3 != null) {
                                                                }
                                                                this.drawCheck1 = false;
                                                                this.drawCheck2 = false;
                                                                this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                                z11 = false;
                                                                this.drawCount = false;
                                                                this.drawMention = false;
                                                                this.drawReactionMention = false;
                                                                this.drawError = false;
                                                                str7 = null;
                                                                str8 = null;
                                                                this.promoDialog = z11;
                                                                messagesController = MessagesController.getInstance(this.currentAccount);
                                                                if (this.dialogsType == 0) {
                                                                    this.drawPinBackground = true;
                                                                    this.promoDialog = true;
                                                                    i21 = messagesController.promoDialogType;
                                                                    if (i21 != MessagesController.PROMO_TYPE_PROXY) {
                                                                    }
                                                                }
                                                                if (this.currentDialogFolderId != 0) {
                                                                }
                                                                string3 = LocaleController.getString(i19);
                                                                spannableStringBuilder5 = spannableStringBuilder3;
                                                                z18 = z7;
                                                                spannableStringBuilder6 = spannableStringBuilder2;
                                                                charSequence21 = charSequence5;
                                                                str9 = str7;
                                                                i20 = i5;
                                                                str10 = str8;
                                                                charSequence22 = string3;
                                                                z12 = z4;
                                                                charSequence25 = charSequence6;
                                                            } else {
                                                                SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(string);
                                                                valueOf.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_draft, this.resourcesProvider), 0, string.length(), 33);
                                                                charSequence3 = valueOf;
                                                            }
                                                            charSequence4 = string;
                                                            i5 = i6;
                                                            z4 = false;
                                                            charSequence7 = charSequence3;
                                                            spannableStringBuilder3 = null;
                                                            charSequence24 = charSequence4;
                                                            charSequence23 = charSequence7;
                                                        } else {
                                                            if (this.clearingDialog) {
                                                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                i16 = R.string.HistoryCleared;
                                                            } else {
                                                                MessageObject messageObject10 = this.message;
                                                                if (messageObject10 == null) {
                                                                    if (this.currentDialogFolderId != 0) {
                                                                        formatString = formatArchivedDialogNames();
                                                                    } else {
                                                                        TLRPC$EncryptedChat tLRPC$EncryptedChat = this.encryptedChat;
                                                                        if (tLRPC$EncryptedChat != null) {
                                                                            this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                            if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatRequested) {
                                                                                i16 = R.string.EncryptionProcessing;
                                                                            } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatWaiting) {
                                                                                formatString = LocaleController.formatString(R.string.AwaitingEncryption, UserObject.getFirstName(this.user));
                                                                            } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatDiscarded) {
                                                                                i16 = R.string.EncryptionRejected;
                                                                            } else if (!(tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat)) {
                                                                                spannableStringBuilder2 = spannableStringBuilder;
                                                                                charSequence20 = "";
                                                                                charSequence4 = null;
                                                                                charSequence7 = charSequence20;
                                                                                spannableStringBuilder3 = null;
                                                                                charSequence24 = charSequence4;
                                                                                charSequence23 = charSequence7;
                                                                            } else if (tLRPC$EncryptedChat.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                                                formatString = LocaleController.formatString(R.string.EncryptedChatStartedOutgoing, UserObject.getFirstName(this.user));
                                                                            } else {
                                                                                i16 = R.string.EncryptedChatStartedIncoming;
                                                                            }
                                                                        } else if (this.dialogsType == 3 && UserObject.isUserSelf(this.user)) {
                                                                            DialogsActivity dialogsActivity = this.parentFragment;
                                                                            spannableStringBuilder2 = spannableStringBuilder;
                                                                            charSequence5 = LocaleController.getString((dialogsActivity == null || !dialogsActivity.isQuote) ? R.string.SavedMessagesInfo : R.string.SavedMessagesInfoQuote);
                                                                            charSequence6 = null;
                                                                            spannableStringBuilder3 = null;
                                                                            z7 = false;
                                                                            z19 = false;
                                                                        } else {
                                                                            z6 = z4;
                                                                            spannableStringBuilder2 = spannableStringBuilder;
                                                                            i6 = i5;
                                                                            string = null;
                                                                            i5 = i6;
                                                                            charSequence5 = "";
                                                                            spannableStringBuilder3 = null;
                                                                            z7 = true;
                                                                            boolean z222 = z6;
                                                                            charSequence6 = string;
                                                                            z4 = z222;
                                                                        }
                                                                    }
                                                                    spannableStringBuilder2 = spannableStringBuilder;
                                                                    charSequence20 = formatString;
                                                                    charSequence4 = null;
                                                                    charSequence7 = charSequence20;
                                                                    spannableStringBuilder3 = null;
                                                                    charSequence24 = charSequence4;
                                                                    charSequence23 = charSequence7;
                                                                } else {
                                                                    String restrictionReason = MessagesController.getInstance(messageObject10.currentAccount).getRestrictionReason(this.message.messageOwner.restriction_reason);
                                                                    long fromChatId = this.message.getFromChatId();
                                                                    boolean isUserDialog = DialogObject.isUserDialog(fromChatId);
                                                                    MessagesController messagesController2 = MessagesController.getInstance(this.currentAccount);
                                                                    if (isUserDialog) {
                                                                        messagesController2.getUser(Long.valueOf(fromChatId));
                                                                        chat = null;
                                                                    } else {
                                                                        chat = messagesController2.getChat(Long.valueOf(-fromChatId));
                                                                    }
                                                                    this.drawCount2 = true;
                                                                    if (this.dialogsType == 0 && this.currentDialogId > 0 && this.message.isOutOwner() && (tLRPC$TL_messageReactions = this.message.messageOwner.reactions) != null && (arrayList2 = tLRPC$TL_messageReactions.recent_reactions) != null && !arrayList2.isEmpty() && this.reactionMentionCount > 0) {
                                                                        TLRPC$MessagePeerReaction tLRPC$MessagePeerReaction = (TLRPC$MessagePeerReaction) this.message.messageOwner.reactions.recent_reactions.get(0);
                                                                        if (tLRPC$MessagePeerReaction.unread) {
                                                                            spannableStringBuilder2 = spannableStringBuilder;
                                                                            long j3 = tLRPC$MessagePeerReaction.peer_id.user_id;
                                                                            if (j3 != 0 && j3 != UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                                                ReactionsLayoutInBubble.VisibleReaction fromTL = ReactionsLayoutInBubble.VisibleReaction.fromTL(tLRPC$MessagePeerReaction.reaction);
                                                                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                String str16 = fromTL.emojicon;
                                                                                if (str16 != null) {
                                                                                    charSequence8 = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, str16);
                                                                                } else {
                                                                                    String formatString2 = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, str);
                                                                                    int indexOf2 = formatString2.indexOf(str);
                                                                                    SpannableStringBuilder spannableStringBuilder9 = new SpannableStringBuilder(formatString2.replace(str, "d"));
                                                                                    long j4 = fromTL.documentId;
                                                                                    TextPaint textPaint6 = this.currentMessagePaint;
                                                                                    spannableStringBuilder9.setSpan(new AnimatedEmojiSpan(j4, textPaint6 == null ? null : textPaint6.getFontMetricsInt()), indexOf2, indexOf2 + 1, 0);
                                                                                    charSequence8 = spannableStringBuilder9;
                                                                                }
                                                                                z8 = true;
                                                                                if (!z8) {
                                                                                    int i40 = this.dialogsType;
                                                                                    if (i40 == 2) {
                                                                                        TLRPC$Chat tLRPC$Chat5 = this.chat;
                                                                                        if (tLRPC$Chat5 != null) {
                                                                                            if (ChatObject.isChannel(tLRPC$Chat5)) {
                                                                                                TLRPC$Chat tLRPC$Chat6 = this.chat;
                                                                                                if (!tLRPC$Chat6.megagroup) {
                                                                                                    i14 = tLRPC$Chat6.participants_count;
                                                                                                    if (i14 != 0) {
                                                                                                        str5 = "Subscribers";
                                                                                                        string2 = LocaleController.formatPluralStringComma(str5, i14);
                                                                                                    } else {
                                                                                                        i15 = !ChatObject.isPublic(tLRPC$Chat6) ? R.string.ChannelPrivate : R.string.ChannelPublic;
                                                                                                        string2 = LocaleController.getString(i15).toLowerCase();
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            TLRPC$Chat tLRPC$Chat7 = this.chat;
                                                                                            i14 = tLRPC$Chat7.participants_count;
                                                                                            if (i14 != 0) {
                                                                                                str5 = "Members";
                                                                                                string2 = LocaleController.formatPluralStringComma(str5, i14);
                                                                                            } else if (tLRPC$Chat7.has_geo) {
                                                                                                string2 = LocaleController.getString(R.string.MegaLocation);
                                                                                            } else {
                                                                                                i15 = !ChatObject.isPublic(tLRPC$Chat7) ? R.string.MegaPrivate : R.string.MegaPublic;
                                                                                                string2 = LocaleController.getString(i15).toLowerCase();
                                                                                            }
                                                                                        } else {
                                                                                            string2 = "";
                                                                                        }
                                                                                        this.drawCount2 = false;
                                                                                    } else if (i40 == 3 && UserObject.isUserSelf(this.user)) {
                                                                                        DialogsActivity dialogsActivity2 = this.parentFragment;
                                                                                        string2 = LocaleController.getString((dialogsActivity2 == null || !dialogsActivity2.isQuote) ? R.string.SavedMessagesInfo : R.string.SavedMessagesInfoQuote);
                                                                                    } else {
                                                                                        if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout || this.currentDialogFolderId == 0) {
                                                                                            MessageObject messageObject11 = this.message;
                                                                                            if (!(messageObject11.messageOwner instanceof TLRPC$TL_messageService) || (MessageObject.isTopicActionMessage(messageObject11) && !(this.message.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate))) {
                                                                                                this.needEmoji = true;
                                                                                                updateMessageThumbs();
                                                                                                String removeDiacritics2 = (this.isSavedDialog || (tLRPC$User2 = this.user) == null || !tLRPC$User2.self || this.message.isOutOwner()) ? null : AndroidUtilities.removeDiacritics(getMessageNameString());
                                                                                                if ((!this.isSavedDialog || (tLRPC$User = this.user) == null || tLRPC$User.self || (messageObject2 = this.message) == null || !messageObject2.isOutOwner()) && removeDiacritics2 == null) {
                                                                                                    TLRPC$Chat tLRPC$Chat8 = this.chat;
                                                                                                    if (tLRPC$Chat8 != null) {
                                                                                                        long j5 = tLRPC$Chat8.id;
                                                                                                        if (j5 > 0) {
                                                                                                            i7 = i5;
                                                                                                            if (chat != null) {
                                                                                                            }
                                                                                                            if (ChatObject.isChannel(tLRPC$Chat8)) {
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                    i7 = i5;
                                                                                                    boolean isEmpty = TextUtils.isEmpty(restrictionReason);
                                                                                                    CharSequence charSequence32 = restrictionReason;
                                                                                                    if (isEmpty) {
                                                                                                        if (MessageObject.isTopicActionMessage(this.message)) {
                                                                                                            MessageObject messageObject12 = this.message;
                                                                                                            CharSequence charSequence33 = messageObject12.messageTextShort;
                                                                                                            if (charSequence33 == null || ((messageObject12.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate) && this.isTopic)) {
                                                                                                                charSequence33 = messageObject12.messageText;
                                                                                                            }
                                                                                                            CharSequence charSequence34 = charSequence33;
                                                                                                            charSequence32 = charSequence34;
                                                                                                            if (messageObject12.topicIconDrawable[0] instanceof ForumBubbleDrawable) {
                                                                                                                TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-this.message.getDialogId(), MessageObject.getTopicId(this.currentAccount, this.message.messageOwner, true));
                                                                                                                charSequence32 = charSequence34;
                                                                                                                if (findTopic != null) {
                                                                                                                    ((ForumBubbleDrawable) this.message.topicIconDrawable[0]).setColor(findTopic.icon_color);
                                                                                                                    charSequence32 = charSequence34;
                                                                                                                }
                                                                                                            }
                                                                                                        } else {
                                                                                                            TLRPC$MessageMedia tLRPC$MessageMedia = this.message.messageOwner.media;
                                                                                                            if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia.ttl_seconds != 0) {
                                                                                                                i12 = R.string.AttachPhotoExpired;
                                                                                                            } else {
                                                                                                                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                                                                                                                    TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
                                                                                                                    if (((tLRPC$Document instanceof TLRPC$TL_documentEmpty) || tLRPC$Document == null) && tLRPC$MessageMedia.ttl_seconds != 0) {
                                                                                                                        i12 = tLRPC$MessageMedia.voice ? R.string.AttachVoiceExpired : tLRPC$MessageMedia.round ? R.string.AttachRoundExpired : R.string.AttachVideoExpired;
                                                                                                                    }
                                                                                                                }
                                                                                                                if (getCaptionMessage() != null) {
                                                                                                                    MessageObject captionMessage = getCaptionMessage();
                                                                                                                    String str17 = !this.needEmoji ? "" : captionMessage.isVideo() ? "📹 " : captionMessage.isVoice() ? "🎤 " : captionMessage.isMusic() ? "🎧 " : captionMessage.isPhoto() ? "🖼 " : "📎 ";
                                                                                                                    if (!captionMessage.hasHighlightedWords() || TextUtils.isEmpty(captionMessage.messageOwner.message)) {
                                                                                                                        i8 = 33;
                                                                                                                        SpannableStringBuilder spannableStringBuilder10 = new SpannableStringBuilder(captionMessage.caption);
                                                                                                                        if (captionMessage.messageOwner != null) {
                                                                                                                            captionMessage.spoilLoginCode();
                                                                                                                            MediaDataController.addTextStyleRuns(captionMessage.messageOwner.entities, captionMessage.caption, spannableStringBuilder10, NotificationCenter.reactionsDidLoad);
                                                                                                                            ArrayList arrayList3 = captionMessage.messageOwner.entities;
                                                                                                                            TextPaint textPaint7 = this.currentMessagePaint;
                                                                                                                            MediaDataController.addAnimatedEmojiSpans(arrayList3, spannableStringBuilder10, textPaint7 == null ? null : textPaint7.getFontMetricsInt());
                                                                                                                        }
                                                                                                                        spannableStringBuilder4 = new SpannableStringBuilder(str17);
                                                                                                                        charSequence13 = spannableStringBuilder10;
                                                                                                                    } else {
                                                                                                                        CharSequence charSequence35 = captionMessage.messageTrimmedToHighlight;
                                                                                                                        int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 47);
                                                                                                                        if (this.hasNameInMessage) {
                                                                                                                            if (!TextUtils.isEmpty(null)) {
                                                                                                                                throw null;
                                                                                                                            }
                                                                                                                            measuredWidth = (int) (measuredWidth - this.currentMessagePaint.measureText(": "));
                                                                                                                        }
                                                                                                                        if (measuredWidth > 0) {
                                                                                                                            CharSequence ellipsizeCenterEnd = AndroidUtilities.ellipsizeCenterEnd(charSequence35, captionMessage.highlightedWords.get(0), measuredWidth, this.currentMessagePaint, NotificationCenter.walletSyncProgressChanged);
                                                                                                                            i8 = 33;
                                                                                                                            charSequence14 = ellipsizeCenterEnd.toString();
                                                                                                                        } else {
                                                                                                                            i8 = 33;
                                                                                                                            charSequence14 = charSequence35;
                                                                                                                        }
                                                                                                                        spannableStringBuilder4 = new SpannableStringBuilder(str17);
                                                                                                                        charSequence13 = charSequence14;
                                                                                                                    }
                                                                                                                    charSequence12 = spannableStringBuilder4.append(charSequence13);
                                                                                                                } else {
                                                                                                                    i8 = 33;
                                                                                                                    MessageObject messageObject13 = this.message;
                                                                                                                    TLRPC$Message tLRPC$Message = messageObject13.messageOwner;
                                                                                                                    TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$Message.media;
                                                                                                                    if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPaidMedia) {
                                                                                                                        int size = ((TLRPC$TL_messageMediaPaidMedia) tLRPC$MessageMedia2).extended_media.size();
                                                                                                                        if (this.hasVideoThumb) {
                                                                                                                            i10 = 1;
                                                                                                                            c2 = 0;
                                                                                                                            if (size > 1) {
                                                                                                                                formatPluralString2 = LocaleController.formatPluralString("Media", size, new Object[0]);
                                                                                                                                int i41 = R.string.AttachPaidMedia;
                                                                                                                                Object[] objArr = new Object[i10];
                                                                                                                                objArr[c2] = formatPluralString2;
                                                                                                                                CharSequence replaceStars = StarsIntroActivity.replaceStars(LocaleController.formatString(i41, objArr));
                                                                                                                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                                                charSequence12 = replaceStars;
                                                                                                                            } else {
                                                                                                                                i11 = R.string.AttachVideo;
                                                                                                                                formatPluralString2 = LocaleController.getString(i11);
                                                                                                                                int i412 = R.string.AttachPaidMedia;
                                                                                                                                Object[] objArr2 = new Object[i10];
                                                                                                                                objArr2[c2] = formatPluralString2;
                                                                                                                                CharSequence replaceStars2 = StarsIntroActivity.replaceStars(LocaleController.formatString(i412, objArr2));
                                                                                                                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                                                charSequence12 = replaceStars2;
                                                                                                                            }
                                                                                                                        } else {
                                                                                                                            c2 = 0;
                                                                                                                            i10 = 1;
                                                                                                                            if (size > 1) {
                                                                                                                                formatPluralString2 = LocaleController.formatPluralString("Photos", size, new Object[0]);
                                                                                                                                int i4122 = R.string.AttachPaidMedia;
                                                                                                                                Object[] objArr22 = new Object[i10];
                                                                                                                                objArr22[c2] = formatPluralString2;
                                                                                                                                CharSequence replaceStars22 = StarsIntroActivity.replaceStars(LocaleController.formatString(i4122, objArr22));
                                                                                                                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                                                charSequence12 = replaceStars22;
                                                                                                                            } else {
                                                                                                                                i11 = R.string.AttachPhoto;
                                                                                                                                formatPluralString2 = LocaleController.getString(i11);
                                                                                                                                int i41222 = R.string.AttachPaidMedia;
                                                                                                                                Object[] objArr222 = new Object[i10];
                                                                                                                                objArr222[c2] = formatPluralString2;
                                                                                                                                CharSequence replaceStars222 = StarsIntroActivity.replaceStars(LocaleController.formatString(i41222, objArr222));
                                                                                                                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                                                charSequence12 = replaceStars222;
                                                                                                                            }
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        if (this.thumbsCount > 1) {
                                                                                                                            if (this.hasVideoThumb) {
                                                                                                                                ArrayList arrayList4 = this.groupMessages;
                                                                                                                                formatPluralString = LocaleController.formatPluralString("Media", arrayList4 == null ? 0 : arrayList4.size(), new Object[0]);
                                                                                                                            } else {
                                                                                                                                ArrayList arrayList5 = this.groupMessages;
                                                                                                                                formatPluralString = LocaleController.formatPluralString("Photos", arrayList5 == null ? 0 : arrayList5.size(), new Object[0]);
                                                                                                                            }
                                                                                                                            charSequence11 = formatPluralString;
                                                                                                                            textPaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                                        } else {
                                                                                                                            if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGiveaway) {
                                                                                                                                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from;
                                                                                                                                if (tLRPC$MessageFwdHeader != null) {
                                                                                                                                    TLRPC$Peer tLRPC$Peer = tLRPC$MessageFwdHeader.from_id;
                                                                                                                                    if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
                                                                                                                                        isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(tLRPC$Peer.channel_id, this.currentAccount);
                                                                                                                                        i9 = !isChannelAndNotMegaGroup ? R.string.BoostingGiveawayChannelStarted : R.string.BoostingGiveawayGroupStarted;
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(this.chat);
                                                                                                                                if (!isChannelAndNotMegaGroup) {
                                                                                                                                }
                                                                                                                            } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGiveawayResults) {
                                                                                                                                i9 = R.string.BoostingGiveawayResults;
                                                                                                                            } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPoll) {
                                                                                                                                TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia2;
                                                                                                                                TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities = tLRPC$TL_messageMediaPoll.poll.question;
                                                                                                                                if (tLRPC$TL_textWithEntities == null || tLRPC$TL_textWithEntities.entities == null) {
                                                                                                                                    sb = new StringBuilder();
                                                                                                                                    sb.append("📊 ");
                                                                                                                                    str4 = tLRPC$TL_messageMediaPoll.poll.question.text;
                                                                                                                                    sb.append(str4);
                                                                                                                                    charSequence10 = sb.toString();
                                                                                                                                    CharSequence charSequence36 = charSequence10;
                                                                                                                                    messageObject = this.message;
                                                                                                                                    charSequence12 = charSequence36;
                                                                                                                                    if (messageObject.messageOwner.media != null) {
                                                                                                                                        charSequence12 = charSequence36;
                                                                                                                                        if (!messageObject.isMediaEmpty()) {
                                                                                                                                            textPaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                                                            charSequence11 = charSequence36;
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                } else {
                                                                                                                                    SpannableStringBuilder spannableStringBuilder11 = new SpannableStringBuilder(tLRPC$TL_messageMediaPoll.poll.question.text);
                                                                                                                                    TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities2 = tLRPC$TL_messageMediaPoll.poll.question;
                                                                                                                                    MediaDataController.addTextStyleRuns(tLRPC$TL_textWithEntities2.entities, tLRPC$TL_textWithEntities2.text, spannableStringBuilder11);
                                                                                                                                    MediaDataController.addAnimatedEmojiSpans(tLRPC$TL_messageMediaPoll.poll.question.entities, spannableStringBuilder11, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt());
                                                                                                                                    charSequence10 = new SpannableStringBuilder("📊 ").append((CharSequence) spannableStringBuilder11);
                                                                                                                                    CharSequence charSequence362 = charSequence10;
                                                                                                                                    messageObject = this.message;
                                                                                                                                    charSequence12 = charSequence362;
                                                                                                                                    if (messageObject.messageOwner.media != null) {
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGame) {
                                                                                                                                sb = new StringBuilder();
                                                                                                                                sb.append("🎮 ");
                                                                                                                                str4 = this.message.messageOwner.media.game.title;
                                                                                                                                sb.append(str4);
                                                                                                                                charSequence10 = sb.toString();
                                                                                                                                CharSequence charSequence3622 = charSequence10;
                                                                                                                                messageObject = this.message;
                                                                                                                                charSequence12 = charSequence3622;
                                                                                                                                if (messageObject.messageOwner.media != null) {
                                                                                                                                }
                                                                                                                            } else {
                                                                                                                                if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                                                                    charSequence10 = tLRPC$MessageMedia2.title;
                                                                                                                                } else if (messageObject13.type == 14) {
                                                                                                                                    charSequence10 = String.format("🎧 %s - %s", messageObject13.getMusicAuthor(), this.message.getMusicTitle());
                                                                                                                                } else if (!(tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaStory) || !tLRPC$MessageMedia2.via_mention) {
                                                                                                                                    if (!messageObject13.hasHighlightedWords() || TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                                                        SpannableStringBuilder spannableStringBuilder12 = new SpannableStringBuilder(charSequence30);
                                                                                                                                        MessageObject messageObject14 = this.message;
                                                                                                                                        if (messageObject14 != null) {
                                                                                                                                            messageObject14.spoilLoginCode();
                                                                                                                                        }
                                                                                                                                        MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder12, (int) NotificationCenter.reactionsDidLoad);
                                                                                                                                        MessageObject messageObject15 = this.message;
                                                                                                                                        charSequence9 = spannableStringBuilder12;
                                                                                                                                        if (messageObject15 != null) {
                                                                                                                                            TLRPC$Message tLRPC$Message2 = messageObject15.messageOwner;
                                                                                                                                            charSequence9 = spannableStringBuilder12;
                                                                                                                                            if (tLRPC$Message2 != null) {
                                                                                                                                                ArrayList arrayList6 = tLRPC$Message2.entities;
                                                                                                                                                TextPaint textPaint8 = this.currentMessagePaint;
                                                                                                                                                MediaDataController.addAnimatedEmojiSpans(arrayList6, spannableStringBuilder12, textPaint8 == null ? null : textPaint8.getFontMetricsInt());
                                                                                                                                                charSequence9 = spannableStringBuilder12;
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    } else {
                                                                                                                                        charSequence9 = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 23), this.currentMessagePaint, NotificationCenter.walletSyncProgressChanged);
                                                                                                                                    }
                                                                                                                                    AndroidUtilities.highlightText(charSequence9, this.message.highlightedWords, this.resourcesProvider);
                                                                                                                                    charSequence10 = charSequence9;
                                                                                                                                } else if (messageObject13.isOut()) {
                                                                                                                                    TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.message.getDialogId()));
                                                                                                                                    if (user != null) {
                                                                                                                                        str3 = UserObject.getFirstName(user);
                                                                                                                                        int indexOf3 = str3.indexOf(32);
                                                                                                                                        c = 0;
                                                                                                                                        if (indexOf3 >= 0) {
                                                                                                                                            str3 = str3.substring(0, indexOf3);
                                                                                                                                        }
                                                                                                                                    } else {
                                                                                                                                        c = 0;
                                                                                                                                        str3 = "";
                                                                                                                                    }
                                                                                                                                    int i42 = R.string.StoryYouMentionInDialog;
                                                                                                                                    Object[] objArr3 = new Object[1];
                                                                                                                                    objArr3[c] = str3;
                                                                                                                                    charSequence10 = LocaleController.formatString(i42, objArr3);
                                                                                                                                } else {
                                                                                                                                    i9 = R.string.StoryMentionInDialog;
                                                                                                                                }
                                                                                                                                CharSequence charSequence36222 = charSequence10;
                                                                                                                                messageObject = this.message;
                                                                                                                                charSequence12 = charSequence36222;
                                                                                                                                if (messageObject.messageOwner.media != null) {
                                                                                                                                }
                                                                                                                            }
                                                                                                                            charSequence10 = LocaleController.getString(i9);
                                                                                                                            CharSequence charSequence362222 = charSequence10;
                                                                                                                            messageObject = this.message;
                                                                                                                            charSequence12 = charSequence362222;
                                                                                                                            if (messageObject.messageOwner.media != null) {
                                                                                                                            }
                                                                                                                        }
                                                                                                                        this.currentMessagePaint = textPaint;
                                                                                                                        charSequence12 = charSequence11;
                                                                                                                    }
                                                                                                                }
                                                                                                                if (this.message.isReplyToStory()) {
                                                                                                                    SpannableStringBuilder spannableStringBuilder13 = new SpannableStringBuilder(charSequence12);
                                                                                                                    spannableStringBuilder13.insert(0, (CharSequence) "d ");
                                                                                                                    spannableStringBuilder13.setSpan(new ColoredImageSpan(ContextCompat.getDrawable(getContext(), R.drawable.msg_mini_replystory).mutate()), 0, 1, 0);
                                                                                                                    charSequence12 = spannableStringBuilder13;
                                                                                                                }
                                                                                                                if (this.thumbsCount > 0) {
                                                                                                                    if (!this.message.hasHighlightedWords() || TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                                        if (charSequence12.length() > 150) {
                                                                                                                            charSequence12 = charSequence12.subSequence(0, 150);
                                                                                                                        }
                                                                                                                        replaceNewLines = AndroidUtilities.replaceNewLines(charSequence12);
                                                                                                                    } else {
                                                                                                                        replaceNewLines = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp(((this.messagePaddingStart + 23) + ((this.thumbSize + 2) * this.thumbsCount)) + 3), this.currentMessagePaint, NotificationCenter.walletSyncProgressChanged).toString();
                                                                                                                    }
                                                                                                                    charSequence12 = !(replaceNewLines instanceof SpannableStringBuilder) ? new SpannableStringBuilder(replaceNewLines) : replaceNewLines;
                                                                                                                    SpannableStringBuilder spannableStringBuilder14 = (SpannableStringBuilder) charSequence12;
                                                                                                                    spannableStringBuilder14.insert(0, (CharSequence) " ");
                                                                                                                    spannableStringBuilder14.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbSize + 2) * this.thumbsCount) + 3)), 0, 1, i8);
                                                                                                                    Emoji.replaceEmoji((CharSequence) spannableStringBuilder14, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
                                                                                                                    if (this.message.hasHighlightedWords() && (highlightText = AndroidUtilities.highlightText(spannableStringBuilder14, this.message.highlightedWords, this.resourcesProvider)) != null) {
                                                                                                                        charSequence12 = highlightText;
                                                                                                                    }
                                                                                                                    z4 = false;
                                                                                                                }
                                                                                                                if (this.message.isForwarded() || !this.message.needDrawForwarded()) {
                                                                                                                    z9 = z4;
                                                                                                                    charSequence15 = charSequence12;
                                                                                                                } else {
                                                                                                                    this.drawForwardIcon = true;
                                                                                                                    SpannableStringBuilder spannableStringBuilder15 = new SpannableStringBuilder(charSequence12);
                                                                                                                    spannableStringBuilder15.insert(0, (CharSequence) "d ");
                                                                                                                    ColoredImageSpan coloredImageSpan = new ColoredImageSpan(ContextCompat.getDrawable(getContext(), R.drawable.mini_forwarded).mutate());
                                                                                                                    coloredImageSpan.setAlpha(0.9f);
                                                                                                                    spannableStringBuilder15.setSpan(coloredImageSpan, 0, 1, 0);
                                                                                                                    charSequence16 = spannableStringBuilder15;
                                                                                                                    charSequence19 = null;
                                                                                                                    z9 = z4;
                                                                                                                    z10 = true;
                                                                                                                    charSequence18 = charSequence16;
                                                                                                                    CharSequence charSequence37 = charSequence19;
                                                                                                                    if (this.currentDialogFolderId != 0) {
                                                                                                                        charSequence37 = formatArchivedDialogNames();
                                                                                                                    }
                                                                                                                    z7 = z10;
                                                                                                                    z4 = z9;
                                                                                                                    charSequence6 = charSequence37;
                                                                                                                    i5 = i7;
                                                                                                                    spannableStringBuilder3 = null;
                                                                                                                    charSequence5 = charSequence18;
                                                                                                                }
                                                                                                            }
                                                                                                            charSequence32 = LocaleController.getString(i12);
                                                                                                        }
                                                                                                    }
                                                                                                    i8 = 33;
                                                                                                    charSequence12 = charSequence32;
                                                                                                    if (this.message.isReplyToStory()) {
                                                                                                    }
                                                                                                    if (this.thumbsCount > 0) {
                                                                                                    }
                                                                                                    if (this.message.isForwarded()) {
                                                                                                    }
                                                                                                    z9 = z4;
                                                                                                    charSequence15 = charSequence12;
                                                                                                } else {
                                                                                                    i7 = i5;
                                                                                                }
                                                                                                if (removeDiacritics2 == null) {
                                                                                                    removeDiacritics2 = getMessageNameString();
                                                                                                }
                                                                                                CharSequence removeDiacritics3 = AndroidUtilities.removeDiacritics(removeDiacritics2);
                                                                                                TLRPC$Chat tLRPC$Chat9 = this.chat;
                                                                                                if (tLRPC$Chat9 != null && tLRPC$Chat9.forum && !this.isTopic && !this.useFromUserAsAvatar) {
                                                                                                    CharSequence topicIconName = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopicIconName(this.chat, this.message, this.currentMessagePaint);
                                                                                                    if (!TextUtils.isEmpty(topicIconName)) {
                                                                                                        SpannableStringBuilder spannableStringBuilder16 = new SpannableStringBuilder("-");
                                                                                                        ColoredImageSpan coloredImageSpan2 = new ColoredImageSpan(ContextCompat.getDrawable(ApplicationLoader.applicationContext, R.drawable.msg_mini_forumarrow).mutate());
                                                                                                        coloredImageSpan2.setColorKey((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? -1 : Theme.key_chats_nameMessage);
                                                                                                        spannableStringBuilder16.setSpan(coloredImageSpan2, 0, 1, 0);
                                                                                                        SpannableStringBuilder spannableStringBuilder17 = new SpannableStringBuilder();
                                                                                                        spannableStringBuilder17.append(removeDiacritics3).append((CharSequence) spannableStringBuilder16).append(topicIconName);
                                                                                                        charSequence17 = spannableStringBuilder17;
                                                                                                        SpannableStringBuilder messageStringFormatted2 = getMessageStringFormatted(i2, restrictionReason, charSequence17, false);
                                                                                                        if (!this.useFromUserAsAvatar || ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && (this.currentDialogFolderId == 0 || messageStringFormatted2.length() <= 0))) {
                                                                                                            i13 = 0;
                                                                                                        } else {
                                                                                                            try {
                                                                                                                foregroundColorSpanThemable = new ForegroundColorSpanThemable(Theme.key_chats_nameMessage, this.resourcesProvider);
                                                                                                                i13 = charSequence17.length() + 1;
                                                                                                            } catch (Exception e) {
                                                                                                                e = e;
                                                                                                                i13 = 0;
                                                                                                            }
                                                                                                            try {
                                                                                                                messageStringFormatted2.setSpan(foregroundColorSpanThemable, 0, i13, 33);
                                                                                                            } catch (Exception e2) {
                                                                                                                e = e2;
                                                                                                                FileLog.e(e);
                                                                                                                replaceEmoji = Emoji.replaceEmoji((CharSequence) messageStringFormatted2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                                if (this.message.hasHighlightedWords()) {
                                                                                                                }
                                                                                                                if (this.thumbsCount > 0) {
                                                                                                                }
                                                                                                                charSequence18 = replaceEmoji;
                                                                                                                z10 = true;
                                                                                                                z9 = false;
                                                                                                                charSequence19 = charSequence17;
                                                                                                                CharSequence charSequence372 = charSequence19;
                                                                                                                if (this.currentDialogFolderId != 0) {
                                                                                                                }
                                                                                                                z7 = z10;
                                                                                                                z4 = z9;
                                                                                                                charSequence6 = charSequence372;
                                                                                                                i5 = i7;
                                                                                                                spannableStringBuilder3 = null;
                                                                                                                charSequence5 = charSequence18;
                                                                                                                tLRPC$DraftMessage5 = this.draftMessage;
                                                                                                                if (tLRPC$DraftMessage5 == null) {
                                                                                                                }
                                                                                                                str6 = LocaleController.stringForMessageListDate(i17);
                                                                                                                messageObject3 = this.message;
                                                                                                                if (messageObject3 != null) {
                                                                                                                }
                                                                                                                this.drawCheck1 = false;
                                                                                                                this.drawCheck2 = false;
                                                                                                                this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                                                                                z11 = false;
                                                                                                                this.drawCount = false;
                                                                                                                this.drawMention = false;
                                                                                                                this.drawReactionMention = false;
                                                                                                                this.drawError = false;
                                                                                                                str7 = null;
                                                                                                                str8 = null;
                                                                                                                this.promoDialog = z11;
                                                                                                                messagesController = MessagesController.getInstance(this.currentAccount);
                                                                                                                if (this.dialogsType == 0) {
                                                                                                                }
                                                                                                                if (this.currentDialogFolderId != 0) {
                                                                                                                }
                                                                                                                string3 = LocaleController.getString(i19);
                                                                                                                spannableStringBuilder5 = spannableStringBuilder3;
                                                                                                                z18 = z7;
                                                                                                                spannableStringBuilder6 = spannableStringBuilder2;
                                                                                                                charSequence21 = charSequence5;
                                                                                                                str9 = str7;
                                                                                                                i20 = i5;
                                                                                                                str10 = str8;
                                                                                                                charSequence22 = string3;
                                                                                                                z12 = z4;
                                                                                                                charSequence25 = charSequence6;
                                                                                                                if (z18) {
                                                                                                                }
                                                                                                                if (drawLock2()) {
                                                                                                                }
                                                                                                                if (LocaleController.isRTL) {
                                                                                                                }
                                                                                                                if (this.drawNameLock) {
                                                                                                                }
                                                                                                                if (this.drawClock) {
                                                                                                                }
                                                                                                                this.nameLeft = i25 + intrinsicWidth;
                                                                                                                if (!this.drawPremium) {
                                                                                                                }
                                                                                                                if (this.dialogMuted) {
                                                                                                                }
                                                                                                                dp3 = AndroidUtilities.dp(6.0f) + Theme.dialogs_muteDrawable.getIntrinsicWidth();
                                                                                                                this.nameWidth -= dp3;
                                                                                                            }
                                                                                                        }
                                                                                                        replaceEmoji = Emoji.replaceEmoji((CharSequence) messageStringFormatted2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                        if (this.message.hasHighlightedWords() && (highlightText2 = AndroidUtilities.highlightText(replaceEmoji, this.message.highlightedWords, this.resourcesProvider)) != null) {
                                                                                                            replaceEmoji = highlightText2;
                                                                                                        }
                                                                                                        if (this.thumbsCount > 0) {
                                                                                                            boolean z23 = replaceEmoji instanceof SpannableStringBuilder;
                                                                                                            replaceEmoji = replaceEmoji;
                                                                                                            if (!z23) {
                                                                                                                replaceEmoji = new SpannableStringBuilder(replaceEmoji);
                                                                                                            }
                                                                                                            SpannableStringBuilder spannableStringBuilder18 = (SpannableStringBuilder) replaceEmoji;
                                                                                                            if (i13 >= spannableStringBuilder18.length()) {
                                                                                                                spannableStringBuilder18.append((CharSequence) " ");
                                                                                                                spannableStringBuilder18.setSpan(new FixedWidthSpan(AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 3)), spannableStringBuilder18.length() - 1, spannableStringBuilder18.length(), 33);
                                                                                                            } else {
                                                                                                                spannableStringBuilder18.insert(i13, (CharSequence) " ");
                                                                                                                spannableStringBuilder18.setSpan(new FixedWidthSpan(AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 3)), i13, i13 + 1, 33);
                                                                                                            }
                                                                                                        }
                                                                                                        charSequence18 = replaceEmoji;
                                                                                                        z10 = true;
                                                                                                        z9 = false;
                                                                                                        charSequence19 = charSequence17;
                                                                                                        CharSequence charSequence3722 = charSequence19;
                                                                                                        if (this.currentDialogFolderId != 0) {
                                                                                                        }
                                                                                                        z7 = z10;
                                                                                                        z4 = z9;
                                                                                                        charSequence6 = charSequence3722;
                                                                                                        i5 = i7;
                                                                                                        spannableStringBuilder3 = null;
                                                                                                        charSequence5 = charSequence18;
                                                                                                    }
                                                                                                }
                                                                                                charSequence17 = removeDiacritics3;
                                                                                                SpannableStringBuilder messageStringFormatted22 = getMessageStringFormatted(i2, restrictionReason, charSequence17, false);
                                                                                                if (this.useFromUserAsAvatar) {
                                                                                                }
                                                                                                i13 = 0;
                                                                                                replaceEmoji = Emoji.replaceEmoji((CharSequence) messageStringFormatted22, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                if (this.message.hasHighlightedWords()) {
                                                                                                    replaceEmoji = highlightText2;
                                                                                                }
                                                                                                if (this.thumbsCount > 0) {
                                                                                                }
                                                                                                charSequence18 = replaceEmoji;
                                                                                                z10 = true;
                                                                                                z9 = false;
                                                                                                charSequence19 = charSequence17;
                                                                                                CharSequence charSequence37222 = charSequence19;
                                                                                                if (this.currentDialogFolderId != 0) {
                                                                                                }
                                                                                                z7 = z10;
                                                                                                z4 = z9;
                                                                                                charSequence6 = charSequence37222;
                                                                                                i5 = i7;
                                                                                                spannableStringBuilder3 = null;
                                                                                                charSequence5 = charSequence18;
                                                                                            } else {
                                                                                                if (ChatObject.isChannelAndNotMegaGroup(this.chat) && (this.message.messageOwner.action instanceof TLRPC$TL_messageActionChannelMigrateFrom)) {
                                                                                                    charSequence30 = "";
                                                                                                    z19 = false;
                                                                                                }
                                                                                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                if (this.message.type == 21) {
                                                                                                    updateMessageThumbs();
                                                                                                    charSequence8 = applyThumbs(charSequence30);
                                                                                                } else {
                                                                                                    z9 = z4;
                                                                                                    i7 = i5;
                                                                                                    charSequence15 = charSequence30;
                                                                                                }
                                                                                            }
                                                                                            z10 = true;
                                                                                        } else {
                                                                                            charSequence15 = formatArchivedDialogNames();
                                                                                            i7 = i5;
                                                                                            z10 = true;
                                                                                            z9 = false;
                                                                                        }
                                                                                        charSequence19 = null;
                                                                                        charSequence18 = charSequence15;
                                                                                        CharSequence charSequence372222 = charSequence19;
                                                                                        if (this.currentDialogFolderId != 0) {
                                                                                        }
                                                                                        z7 = z10;
                                                                                        z4 = z9;
                                                                                        charSequence6 = charSequence372222;
                                                                                        i5 = i7;
                                                                                        spannableStringBuilder3 = null;
                                                                                        charSequence5 = charSequence18;
                                                                                    }
                                                                                    charSequence18 = string2;
                                                                                    i7 = i5;
                                                                                    charSequence19 = null;
                                                                                    z19 = false;
                                                                                    z9 = z4;
                                                                                    z10 = false;
                                                                                    CharSequence charSequence3722222 = charSequence19;
                                                                                    if (this.currentDialogFolderId != 0) {
                                                                                    }
                                                                                    z7 = z10;
                                                                                    z4 = z9;
                                                                                    charSequence6 = charSequence3722222;
                                                                                    i5 = i7;
                                                                                    spannableStringBuilder3 = null;
                                                                                    charSequence5 = charSequence18;
                                                                                }
                                                                                charSequence16 = charSequence8;
                                                                                i7 = i5;
                                                                                charSequence19 = null;
                                                                                z9 = z4;
                                                                                z10 = true;
                                                                                charSequence18 = charSequence16;
                                                                                CharSequence charSequence37222222 = charSequence19;
                                                                                if (this.currentDialogFolderId != 0) {
                                                                                }
                                                                                z7 = z10;
                                                                                z4 = z9;
                                                                                charSequence6 = charSequence37222222;
                                                                                i5 = i7;
                                                                                spannableStringBuilder3 = null;
                                                                                charSequence5 = charSequence18;
                                                                            }
                                                                            charSequence8 = "";
                                                                            z8 = false;
                                                                            if (!z8) {
                                                                            }
                                                                            charSequence16 = charSequence8;
                                                                            i7 = i5;
                                                                            charSequence19 = null;
                                                                            z9 = z4;
                                                                            z10 = true;
                                                                            charSequence18 = charSequence16;
                                                                            CharSequence charSequence372222222 = charSequence19;
                                                                            if (this.currentDialogFolderId != 0) {
                                                                            }
                                                                            z7 = z10;
                                                                            z4 = z9;
                                                                            charSequence6 = charSequence372222222;
                                                                            i5 = i7;
                                                                            spannableStringBuilder3 = null;
                                                                            charSequence5 = charSequence18;
                                                                        }
                                                                    }
                                                                    spannableStringBuilder2 = spannableStringBuilder;
                                                                    charSequence8 = "";
                                                                    z8 = false;
                                                                    if (!z8) {
                                                                    }
                                                                    charSequence16 = charSequence8;
                                                                    i7 = i5;
                                                                    charSequence19 = null;
                                                                    z9 = z4;
                                                                    z10 = true;
                                                                    charSequence18 = charSequence16;
                                                                    CharSequence charSequence3722222222 = charSequence19;
                                                                    if (this.currentDialogFolderId != 0) {
                                                                    }
                                                                    z7 = z10;
                                                                    z4 = z9;
                                                                    charSequence6 = charSequence3722222222;
                                                                    i5 = i7;
                                                                    spannableStringBuilder3 = null;
                                                                    charSequence5 = charSequence18;
                                                                }
                                                                tLRPC$DraftMessage5 = this.draftMessage;
                                                                if (tLRPC$DraftMessage5 == null) {
                                                                }
                                                                str6 = LocaleController.stringForMessageListDate(i17);
                                                                messageObject3 = this.message;
                                                                if (messageObject3 != null) {
                                                                }
                                                                this.drawCheck1 = false;
                                                                this.drawCheck2 = false;
                                                                this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                                z11 = false;
                                                                this.drawCount = false;
                                                                this.drawMention = false;
                                                                this.drawReactionMention = false;
                                                                this.drawError = false;
                                                                str7 = null;
                                                                str8 = null;
                                                                this.promoDialog = z11;
                                                                messagesController = MessagesController.getInstance(this.currentAccount);
                                                                if (this.dialogsType == 0) {
                                                                }
                                                                if (this.currentDialogFolderId != 0) {
                                                                }
                                                                string3 = LocaleController.getString(i19);
                                                                spannableStringBuilder5 = spannableStringBuilder3;
                                                                z18 = z7;
                                                                spannableStringBuilder6 = spannableStringBuilder2;
                                                                charSequence21 = charSequence5;
                                                                str9 = str7;
                                                                i20 = i5;
                                                                str10 = str8;
                                                                charSequence22 = string3;
                                                                z12 = z4;
                                                                charSequence25 = charSequence6;
                                                            }
                                                            formatString = LocaleController.getString(i16);
                                                            spannableStringBuilder2 = spannableStringBuilder;
                                                            charSequence20 = formatString;
                                                            charSequence4 = null;
                                                            charSequence7 = charSequence20;
                                                            spannableStringBuilder3 = null;
                                                            charSequence24 = charSequence4;
                                                            charSequence23 = charSequence7;
                                                        }
                                                    }
                                                    z7 = true;
                                                    charSequence6 = charSequence24;
                                                    charSequence5 = charSequence23;
                                                    tLRPC$DraftMessage5 = this.draftMessage;
                                                    if (tLRPC$DraftMessage5 == null) {
                                                    }
                                                    str6 = LocaleController.stringForMessageListDate(i17);
                                                    messageObject3 = this.message;
                                                    if (messageObject3 != null) {
                                                    }
                                                    this.drawCheck1 = false;
                                                    this.drawCheck2 = false;
                                                    this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                    z11 = false;
                                                    this.drawCount = false;
                                                    this.drawMention = false;
                                                    this.drawReactionMention = false;
                                                    this.drawError = false;
                                                    str7 = null;
                                                    str8 = null;
                                                    this.promoDialog = z11;
                                                    messagesController = MessagesController.getInstance(this.currentAccount);
                                                    if (this.dialogsType == 0) {
                                                    }
                                                    if (this.currentDialogFolderId != 0) {
                                                    }
                                                    string3 = LocaleController.getString(i19);
                                                    spannableStringBuilder5 = spannableStringBuilder3;
                                                    z18 = z7;
                                                    spannableStringBuilder6 = spannableStringBuilder2;
                                                    charSequence21 = charSequence5;
                                                    str9 = str7;
                                                    i20 = i5;
                                                    str10 = str8;
                                                    charSequence22 = string3;
                                                    z12 = z4;
                                                    charSequence25 = charSequence6;
                                                }
                                            }
                                            tLRPC$DraftMessage4 = null;
                                            this.draftMessage = null;
                                            z3 = false;
                                            this.draftVoice = false;
                                            if (isForumCell()) {
                                            }
                                            z7 = true;
                                            charSequence6 = charSequence24;
                                            charSequence5 = charSequence23;
                                            tLRPC$DraftMessage5 = this.draftMessage;
                                            if (tLRPC$DraftMessage5 == null) {
                                            }
                                            str6 = LocaleController.stringForMessageListDate(i17);
                                            messageObject3 = this.message;
                                            if (messageObject3 != null) {
                                            }
                                            this.drawCheck1 = false;
                                            this.drawCheck2 = false;
                                            this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                            z11 = false;
                                            this.drawCount = false;
                                            this.drawMention = false;
                                            this.drawReactionMention = false;
                                            this.drawError = false;
                                            str7 = null;
                                            str8 = null;
                                            this.promoDialog = z11;
                                            messagesController = MessagesController.getInstance(this.currentAccount);
                                            if (this.dialogsType == 0) {
                                            }
                                            if (this.currentDialogFolderId != 0) {
                                            }
                                            string3 = LocaleController.getString(i19);
                                            spannableStringBuilder5 = spannableStringBuilder3;
                                            z18 = z7;
                                            spannableStringBuilder6 = spannableStringBuilder2;
                                            charSequence21 = charSequence5;
                                            str9 = str7;
                                            i20 = i5;
                                            str10 = str8;
                                            charSequence22 = string3;
                                            z12 = z4;
                                            charSequence25 = charSequence6;
                                        } else {
                                            tLRPC$DraftMessage = null;
                                            if (this.isDialogCell || this.isSavedDialogCell) {
                                                boolean z24 = MediaDataController.getInstance(this.currentAccount).getDraftVoice(this.currentDialogId, (long) getTopicId()) != null;
                                                this.draftVoice = z24;
                                                if (!z24) {
                                                    tLRPC$DraftMessage = MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, 0L);
                                                }
                                            } else {
                                                this.draftVoice = false;
                                            }
                                            this.draftMessage = tLRPC$DraftMessage;
                                            z2 = this.draftVoice;
                                            if (!z2) {
                                                if (ChatObject.isChannel(this.chat)) {
                                                }
                                                tLRPC$Chat = this.chat;
                                                if (tLRPC$Chat != null) {
                                                }
                                                tLRPC$DraftMessage4 = null;
                                                z3 = false;
                                                if (isForumCell()) {
                                                }
                                                z7 = true;
                                                charSequence6 = charSequence24;
                                                charSequence5 = charSequence23;
                                                tLRPC$DraftMessage5 = this.draftMessage;
                                                if (tLRPC$DraftMessage5 == null) {
                                                }
                                                str6 = LocaleController.stringForMessageListDate(i17);
                                                messageObject3 = this.message;
                                                if (messageObject3 != null) {
                                                }
                                                this.drawCheck1 = false;
                                                this.drawCheck2 = false;
                                                this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                z11 = false;
                                                this.drawCount = false;
                                                this.drawMention = false;
                                                this.drawReactionMention = false;
                                                this.drawError = false;
                                                str7 = null;
                                                str8 = null;
                                                this.promoDialog = z11;
                                                messagesController = MessagesController.getInstance(this.currentAccount);
                                                if (this.dialogsType == 0) {
                                                }
                                                if (this.currentDialogFolderId != 0) {
                                                }
                                                string3 = LocaleController.getString(i19);
                                                spannableStringBuilder5 = spannableStringBuilder3;
                                                z18 = z7;
                                                spannableStringBuilder6 = spannableStringBuilder2;
                                                charSequence21 = charSequence5;
                                                str9 = str7;
                                                i20 = i5;
                                                str10 = str8;
                                                charSequence22 = string3;
                                                z12 = z4;
                                                charSequence25 = charSequence6;
                                            }
                                            if (ChatObject.isChannel(this.chat)) {
                                            }
                                            tLRPC$Chat = this.chat;
                                            if (tLRPC$Chat != null) {
                                            }
                                            tLRPC$DraftMessage4 = null;
                                            z3 = false;
                                            if (isForumCell()) {
                                            }
                                            z7 = true;
                                            charSequence6 = charSequence24;
                                            charSequence5 = charSequence23;
                                            tLRPC$DraftMessage5 = this.draftMessage;
                                            if (tLRPC$DraftMessage5 == null) {
                                            }
                                            str6 = LocaleController.stringForMessageListDate(i17);
                                            messageObject3 = this.message;
                                            if (messageObject3 != null) {
                                            }
                                            this.drawCheck1 = false;
                                            this.drawCheck2 = false;
                                            this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                            z11 = false;
                                            this.drawCount = false;
                                            this.drawMention = false;
                                            this.drawReactionMention = false;
                                            this.drawError = false;
                                            str7 = null;
                                            str8 = null;
                                            this.promoDialog = z11;
                                            messagesController = MessagesController.getInstance(this.currentAccount);
                                            if (this.dialogsType == 0) {
                                            }
                                            if (this.currentDialogFolderId != 0) {
                                            }
                                            string3 = LocaleController.getString(i19);
                                            spannableStringBuilder5 = spannableStringBuilder3;
                                            z18 = z7;
                                            spannableStringBuilder6 = spannableStringBuilder2;
                                            charSequence21 = charSequence5;
                                            str9 = str7;
                                            i20 = i5;
                                            str10 = str8;
                                            charSequence22 = string3;
                                            z12 = z4;
                                            charSequence25 = charSequence6;
                                        }
                                        tLRPC$DraftMessage = null;
                                        this.draftMessage = tLRPC$DraftMessage;
                                        z2 = this.draftVoice;
                                        if (!z2) {
                                        }
                                        if (ChatObject.isChannel(this.chat)) {
                                        }
                                        tLRPC$Chat = this.chat;
                                        if (tLRPC$Chat != null) {
                                        }
                                        tLRPC$DraftMessage4 = null;
                                        z3 = false;
                                        if (isForumCell()) {
                                        }
                                        z7 = true;
                                        charSequence6 = charSequence24;
                                        charSequence5 = charSequence23;
                                        tLRPC$DraftMessage5 = this.draftMessage;
                                        if (tLRPC$DraftMessage5 == null) {
                                        }
                                        str6 = LocaleController.stringForMessageListDate(i17);
                                        messageObject3 = this.message;
                                        if (messageObject3 != null) {
                                        }
                                        this.drawCheck1 = false;
                                        this.drawCheck2 = false;
                                        this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                        z11 = false;
                                        this.drawCount = false;
                                        this.drawMention = false;
                                        this.drawReactionMention = false;
                                        this.drawError = false;
                                        str7 = null;
                                        str8 = null;
                                        this.promoDialog = z11;
                                        messagesController = MessagesController.getInstance(this.currentAccount);
                                        if (this.dialogsType == 0) {
                                        }
                                        if (this.currentDialogFolderId != 0) {
                                        }
                                        string3 = LocaleController.getString(i19);
                                        spannableStringBuilder5 = spannableStringBuilder3;
                                        z18 = z7;
                                        spannableStringBuilder6 = spannableStringBuilder2;
                                        charSequence21 = charSequence5;
                                        str9 = str7;
                                        i20 = i5;
                                        str10 = str8;
                                        charSequence22 = string3;
                                        z12 = z4;
                                        charSequence25 = charSequence6;
                                    }
                                }
                                z = false;
                                this.drawPremium = z;
                                if (z) {
                                }
                                i4 = this.lastMessageDate;
                                if (i4 == 0) {
                                    i4 = messageObject5.messageOwner.date;
                                }
                                if (this.isTopic) {
                                }
                                tLRPC$DraftMessage = null;
                                this.draftMessage = tLRPC$DraftMessage;
                                z2 = this.draftVoice;
                                if (!z2) {
                                }
                                if (ChatObject.isChannel(this.chat)) {
                                }
                                tLRPC$Chat = this.chat;
                                if (tLRPC$Chat != null) {
                                }
                                tLRPC$DraftMessage4 = null;
                                z3 = false;
                                if (isForumCell()) {
                                }
                                z7 = true;
                                charSequence6 = charSequence24;
                                charSequence5 = charSequence23;
                                tLRPC$DraftMessage5 = this.draftMessage;
                                if (tLRPC$DraftMessage5 == null) {
                                }
                                str6 = LocaleController.stringForMessageListDate(i17);
                                messageObject3 = this.message;
                                if (messageObject3 != null) {
                                }
                                this.drawCheck1 = false;
                                this.drawCheck2 = false;
                                this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                z11 = false;
                                this.drawCount = false;
                                this.drawMention = false;
                                this.drawReactionMention = false;
                                this.drawError = false;
                                str7 = null;
                                str8 = null;
                                this.promoDialog = z11;
                                messagesController = MessagesController.getInstance(this.currentAccount);
                                if (this.dialogsType == 0) {
                                }
                                if (this.currentDialogFolderId != 0) {
                                }
                                string3 = LocaleController.getString(i19);
                                spannableStringBuilder5 = spannableStringBuilder3;
                                z18 = z7;
                                spannableStringBuilder6 = spannableStringBuilder2;
                                charSequence21 = charSequence5;
                                str9 = str7;
                                i20 = i5;
                                str10 = str8;
                                charSequence22 = string3;
                                z12 = z4;
                                charSequence25 = charSequence6;
                            }
                            scamDrawable.checkText();
                            if (MessagesController.getInstance(this.currentAccount).isPremiumUser(this.user)) {
                            }
                            z = false;
                            this.drawPremium = z;
                            if (z) {
                            }
                            i4 = this.lastMessageDate;
                            if (i4 == 0) {
                            }
                            if (this.isTopic) {
                            }
                            tLRPC$DraftMessage = null;
                            this.draftMessage = tLRPC$DraftMessage;
                            z2 = this.draftVoice;
                            if (!z2) {
                            }
                            if (ChatObject.isChannel(this.chat)) {
                            }
                            tLRPC$Chat = this.chat;
                            if (tLRPC$Chat != null) {
                            }
                            tLRPC$DraftMessage4 = null;
                            z3 = false;
                            if (isForumCell()) {
                            }
                            z7 = true;
                            charSequence6 = charSequence24;
                            charSequence5 = charSequence23;
                            tLRPC$DraftMessage5 = this.draftMessage;
                            if (tLRPC$DraftMessage5 == null) {
                            }
                            str6 = LocaleController.stringForMessageListDate(i17);
                            messageObject3 = this.message;
                            if (messageObject3 != null) {
                            }
                            this.drawCheck1 = false;
                            this.drawCheck2 = false;
                            this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                            z11 = false;
                            this.drawCount = false;
                            this.drawMention = false;
                            this.drawReactionMention = false;
                            this.drawError = false;
                            str7 = null;
                            str8 = null;
                            this.promoDialog = z11;
                            messagesController = MessagesController.getInstance(this.currentAccount);
                            if (this.dialogsType == 0) {
                            }
                            if (this.currentDialogFolderId != 0) {
                            }
                            string3 = LocaleController.getString(i19);
                            spannableStringBuilder5 = spannableStringBuilder3;
                            z18 = z7;
                            spannableStringBuilder6 = spannableStringBuilder2;
                            charSequence21 = charSequence5;
                            str9 = str7;
                            i20 = i5;
                            str10 = str8;
                            charSequence22 = string3;
                            z12 = z4;
                            charSequence25 = charSequence6;
                        }
                    }
                }
                charSequence2 = charSequence;
                i4 = this.lastMessageDate;
                if (i4 == 0) {
                }
                if (this.isTopic) {
                }
                tLRPC$DraftMessage = null;
                this.draftMessage = tLRPC$DraftMessage;
                z2 = this.draftVoice;
                if (!z2) {
                }
                if (ChatObject.isChannel(this.chat)) {
                }
                tLRPC$Chat = this.chat;
                if (tLRPC$Chat != null) {
                }
                tLRPC$DraftMessage4 = null;
                z3 = false;
                if (isForumCell()) {
                }
                z7 = true;
                charSequence6 = charSequence24;
                charSequence5 = charSequence23;
                tLRPC$DraftMessage5 = this.draftMessage;
                if (tLRPC$DraftMessage5 == null) {
                }
                str6 = LocaleController.stringForMessageListDate(i17);
                messageObject3 = this.message;
                if (messageObject3 != null) {
                }
                this.drawCheck1 = false;
                this.drawCheck2 = false;
                this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                z11 = false;
                this.drawCount = false;
                this.drawMention = false;
                this.drawReactionMention = false;
                this.drawError = false;
                str7 = null;
                str8 = null;
                this.promoDialog = z11;
                messagesController = MessagesController.getInstance(this.currentAccount);
                if (this.dialogsType == 0) {
                }
                if (this.currentDialogFolderId != 0) {
                }
                string3 = LocaleController.getString(i19);
                spannableStringBuilder5 = spannableStringBuilder3;
                z18 = z7;
                spannableStringBuilder6 = spannableStringBuilder2;
                charSequence21 = charSequence5;
                str9 = str7;
                i20 = i5;
                str10 = str8;
                charSequence22 = string3;
                z12 = z4;
                charSequence25 = charSequence6;
            }
        } else if (LocaleController.isRTL) {
            dp = AndroidUtilities.dp(18.0f);
            this.nameLeft = dp;
            if (this.encryptedChat == null) {
            }
            charSequence2 = charSequence;
            i4 = this.lastMessageDate;
            if (i4 == 0) {
            }
            if (this.isTopic) {
            }
            tLRPC$DraftMessage = null;
            this.draftMessage = tLRPC$DraftMessage;
            z2 = this.draftVoice;
            if (!z2) {
            }
            if (ChatObject.isChannel(this.chat)) {
            }
            tLRPC$Chat = this.chat;
            if (tLRPC$Chat != null) {
            }
            tLRPC$DraftMessage4 = null;
            z3 = false;
            if (isForumCell()) {
            }
            z7 = true;
            charSequence6 = charSequence24;
            charSequence5 = charSequence23;
            tLRPC$DraftMessage5 = this.draftMessage;
            if (tLRPC$DraftMessage5 == null) {
            }
            str6 = LocaleController.stringForMessageListDate(i17);
            messageObject3 = this.message;
            if (messageObject3 != null) {
            }
            this.drawCheck1 = false;
            this.drawCheck2 = false;
            this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
            z11 = false;
            this.drawCount = false;
            this.drawMention = false;
            this.drawReactionMention = false;
            this.drawError = false;
            str7 = null;
            str8 = null;
            this.promoDialog = z11;
            messagesController = MessagesController.getInstance(this.currentAccount);
            if (this.dialogsType == 0) {
            }
            if (this.currentDialogFolderId != 0) {
            }
            string3 = LocaleController.getString(i19);
            spannableStringBuilder5 = spannableStringBuilder3;
            z18 = z7;
            spannableStringBuilder6 = spannableStringBuilder2;
            charSequence21 = charSequence5;
            str9 = str7;
            i20 = i5;
            str10 = str8;
            charSequence22 = string3;
            z12 = z4;
            charSequence25 = charSequence6;
        } else {
            i3 = this.messagePaddingStart + 4;
            dp = AndroidUtilities.dp(i3);
            this.nameLeft = dp;
            if (this.encryptedChat == null) {
            }
            charSequence2 = charSequence;
            i4 = this.lastMessageDate;
            if (i4 == 0) {
            }
            if (this.isTopic) {
            }
            tLRPC$DraftMessage = null;
            this.draftMessage = tLRPC$DraftMessage;
            z2 = this.draftVoice;
            if (!z2) {
            }
            if (ChatObject.isChannel(this.chat)) {
            }
            tLRPC$Chat = this.chat;
            if (tLRPC$Chat != null) {
            }
            tLRPC$DraftMessage4 = null;
            z3 = false;
            if (isForumCell()) {
            }
            z7 = true;
            charSequence6 = charSequence24;
            charSequence5 = charSequence23;
            tLRPC$DraftMessage5 = this.draftMessage;
            if (tLRPC$DraftMessage5 == null) {
            }
            str6 = LocaleController.stringForMessageListDate(i17);
            messageObject3 = this.message;
            if (messageObject3 != null) {
            }
            this.drawCheck1 = false;
            this.drawCheck2 = false;
            this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
            z11 = false;
            this.drawCount = false;
            this.drawMention = false;
            this.drawReactionMention = false;
            this.drawError = false;
            str7 = null;
            str8 = null;
            this.promoDialog = z11;
            messagesController = MessagesController.getInstance(this.currentAccount);
            if (this.dialogsType == 0) {
            }
            if (this.currentDialogFolderId != 0) {
            }
            string3 = LocaleController.getString(i19);
            spannableStringBuilder5 = spannableStringBuilder3;
            z18 = z7;
            spannableStringBuilder6 = spannableStringBuilder2;
            charSequence21 = charSequence5;
            str9 = str7;
            i20 = i5;
            str10 = str8;
            charSequence22 = string3;
            z12 = z4;
            charSequence25 = charSequence6;
        }
        if (z18) {
            this.timeLayout = null;
            this.timeLeft = 0;
            i23 = 0;
        } else {
            i23 = (int) Math.ceil(Theme.dialogs_timePaint.measureText(str6));
            this.timeLayout = new StaticLayout(str6, Theme.dialogs_timePaint, i23, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.timeLeft = !LocaleController.isRTL ? (getMeasuredWidth() - AndroidUtilities.dp(15.0f)) - i23 : AndroidUtilities.dp(15.0f);
        }
        if (drawLock2()) {
            f = 4.0f;
            i24 = 0;
        } else {
            if (LocaleController.isRTL) {
                f = 4.0f;
                intrinsicWidth2 = this.timeLeft + i23 + AndroidUtilities.dp(4.0f);
            } else {
                f = 4.0f;
                intrinsicWidth2 = (this.timeLeft - Theme.dialogs_lock2Drawable.getIntrinsicWidth()) - AndroidUtilities.dp(4.0f);
            }
            this.lock2Left = intrinsicWidth2;
            i24 = Theme.dialogs_lock2Drawable.getIntrinsicWidth() + AndroidUtilities.dp(f);
            i23 += i24;
        }
        if (LocaleController.isRTL) {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(22.0f)) - i23;
        } else {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(this.messagePaddingStart + 13)) - i23;
            this.nameLeft += i23;
        }
        if (this.drawNameLock) {
            int i43 = this.nameWidth;
            if (LocaleController.isRTL) {
                f = 8.0f;
            }
            this.nameWidth = i43 - (AndroidUtilities.dp(f) + Theme.dialogs_lockDrawable.getIntrinsicWidth());
        }
        if (this.drawClock) {
            if (this.drawCheck2) {
                intrinsicWidth = Theme.dialogs_checkDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
                int i44 = this.nameWidth - intrinsicWidth;
                this.nameWidth = i44;
                if (this.drawCheck1) {
                    this.nameWidth = i44 - (Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0f));
                    if (LocaleController.isRTL) {
                        int dp12 = this.timeLeft + i23 + AndroidUtilities.dp(5.0f);
                        this.checkDrawLeft = dp12;
                        this.halfCheckDrawLeft = dp12 + AndroidUtilities.dp(5.5f);
                        i25 = this.nameLeft;
                        intrinsicWidth = (intrinsicWidth + Theme.dialogs_halfCheckDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(8.0f);
                    } else {
                        int i45 = (this.timeLeft - i24) - intrinsicWidth;
                        this.halfCheckDrawLeft = i45;
                        this.checkDrawLeft = i45 - AndroidUtilities.dp(5.5f);
                    }
                } else if (LocaleController.isRTL) {
                    this.checkDrawLeft1 = this.timeLeft + i23 + AndroidUtilities.dp(5.0f);
                    i25 = this.nameLeft;
                } else {
                    this.checkDrawLeft1 = (this.timeLeft - i24) - intrinsicWidth;
                }
            }
            if (!this.drawPremium) {
            }
            if (this.dialogMuted) {
            }
            dp3 = AndroidUtilities.dp(6.0f) + Theme.dialogs_muteDrawable.getIntrinsicWidth();
            this.nameWidth -= dp3;
        } else {
            intrinsicWidth = Theme.dialogs_clockDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            this.nameWidth -= intrinsicWidth;
            if (!LocaleController.isRTL) {
                this.clockDrawLeft = (this.timeLeft - i24) - intrinsicWidth;
                if (!this.drawPremium && this.emojiStatus.getDrawable() != null) {
                    dp3 = AndroidUtilities.dp(36.0f);
                    this.nameWidth -= dp3;
                } else if ((!this.dialogMuted || this.drawUnmute) && !this.drawVerified && this.drawScam == 0) {
                    dp3 = AndroidUtilities.dp(6.0f) + Theme.dialogs_muteDrawable.getIntrinsicWidth();
                    this.nameWidth -= dp3;
                } else if (this.drawVerified) {
                    dp3 = AndroidUtilities.dp(6.0f) + Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
                    this.nameWidth -= dp3;
                } else if (this.drawPremium) {
                    dp3 = AndroidUtilities.dp(36.0f);
                    this.nameWidth -= dp3;
                } else if (this.drawScam != 0) {
                    dp3 = AndroidUtilities.dp(6.0f) + (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).getIntrinsicWidth();
                    this.nameWidth -= dp3;
                }
                try {
                    int dp13 = this.nameWidth - AndroidUtilities.dp(12.0f);
                    if (dp13 < 0) {
                        dp13 = 0;
                    }
                    if (charSequence22 instanceof String) {
                        charSequence22 = ((String) charSequence22).replace('\n', ' ');
                    }
                    if (this.nameLayoutEllipsizeByGradient) {
                        this.nameLayoutFits = charSequence22.length() == TextUtils.ellipsize(charSequence22, Theme.dialogs_namePaint[this.paintIndex], (float) dp13, TextUtils.TruncateAt.END).length();
                        dp13 += AndroidUtilities.dp(48.0f);
                    }
                    float f2 = dp13;
                    this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(charSequence22.toString()) > f2;
                    if (!this.twoLinesForName) {
                        charSequence22 = TextUtils.ellipsize(charSequence22, Theme.dialogs_namePaint[this.paintIndex], f2, TextUtils.TruncateAt.END);
                    }
                    CharSequence replaceEmoji2 = Emoji.replaceEmoji(charSequence22, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    MessageObject messageObject16 = this.message;
                    CharSequence charSequence38 = (messageObject16 == null || !messageObject16.hasHighlightedWords() || (highlightText4 = AndroidUtilities.highlightText(replaceEmoji2, this.message.highlightedWords, this.resourcesProvider)) == null) ? replaceEmoji2 : highlightText4;
                    if (this.twoLinesForName) {
                        this.nameLayout = StaticLayoutEx.createStaticLayout(charSequence38, Theme.dialogs_namePaint[this.paintIndex], dp13, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, dp13, 2);
                    } else {
                        this.nameLayout = new StaticLayout(charSequence38, Theme.dialogs_namePaint[this.paintIndex], Math.max(dp13, this.nameWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    }
                    this.nameLayoutTranslateX = (this.nameLayoutEllipsizeByGradient && this.nameLayout.isRtlCharAt(0)) ? -AndroidUtilities.dp(36.0f) : 0.0f;
                    this.nameLayoutEllipsizeLeft = this.nameLayout.isRtlCharAt(0);
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
                this.animatedEmojiStackName = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStackName, this.nameLayout);
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    dp4 = AndroidUtilities.dp(11.0f);
                    this.messageNameTop = AndroidUtilities.dp(32.0f);
                    this.timeTop = AndroidUtilities.dp(13.0f);
                    this.errorTop = AndroidUtilities.dp(43.0f);
                    this.pinTop = AndroidUtilities.dp(43.0f);
                    this.countTop = AndroidUtilities.dp(43.0f);
                    this.checkDrawTop = AndroidUtilities.dp(13.0f);
                    int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 21);
                    if (LocaleController.isRTL) {
                        int dp14 = AndroidUtilities.dp(16.0f);
                        this.messageNameLeft = dp14;
                        this.messageLeft = dp14;
                        this.typingLeft = dp14;
                        this.buttonLeft = dp14;
                        dp5 = getMeasuredWidth() - AndroidUtilities.dp(this.avatarStart + 56);
                        dp6 = dp5 - AndroidUtilities.dp(31.0f);
                    } else {
                        int dp15 = AndroidUtilities.dp(this.messagePaddingStart + 6);
                        this.messageNameLeft = dp15;
                        this.messageLeft = dp15;
                        this.typingLeft = dp15;
                        this.buttonLeft = dp15;
                        dp5 = AndroidUtilities.dp(this.avatarStart);
                        dp6 = AndroidUtilities.dp(69.0f) + dp5;
                    }
                    i26 = measuredWidth2;
                    this.storyParams.originalAvatarRect.set(dp5, dp4, dp5 + AndroidUtilities.dp(56.0f), dp4 + AndroidUtilities.dp(56.0f));
                    int i46 = 0;
                    while (true) {
                        ImageReceiver[] imageReceiverArr = this.thumbImage;
                        if (i46 >= imageReceiverArr.length) {
                            break;
                        }
                        imageReceiverArr[i46].setImageCoords(((this.thumbSize + 2) * i46) + dp6, ((AndroidUtilities.dp(31.0f) + dp4) + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0)) - ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout || (dialogCellTags = this.tags) == null || dialogCellTags.isEmpty()) ? 0 : AndroidUtilities.dp(9.0f)), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                        i46++;
                        dp4 = dp4;
                    }
                } else {
                    dp4 = AndroidUtilities.dp(9.0f);
                    this.messageNameTop = AndroidUtilities.dp(31.0f);
                    this.timeTop = AndroidUtilities.dp(16.0f);
                    this.errorTop = AndroidUtilities.dp(39.0f);
                    this.pinTop = AndroidUtilities.dp(39.0f);
                    this.countTop = this.isTopic ? AndroidUtilities.dp(36.0f) : AndroidUtilities.dp(39.0f);
                    this.checkDrawTop = AndroidUtilities.dp(17.0f);
                    int measuredWidth3 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) - (LocaleController.isRTL ? 0 : 12));
                    if (LocaleController.isRTL) {
                        int dp16 = AndroidUtilities.dp(22.0f);
                        this.messageNameLeft = dp16;
                        this.messageLeft = dp16;
                        this.typingLeft = dp16;
                        this.buttonLeft = dp16;
                        dp9 = getMeasuredWidth() - AndroidUtilities.dp(this.avatarStart + 54);
                        dp10 = dp9 - AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 9);
                    } else {
                        int dp17 = AndroidUtilities.dp(this.messagePaddingStart + 4);
                        this.messageNameLeft = dp17;
                        this.messageLeft = dp17;
                        this.typingLeft = dp17;
                        this.buttonLeft = dp17;
                        dp9 = AndroidUtilities.dp(this.avatarStart);
                        dp10 = AndroidUtilities.dp(67.0f) + dp9;
                    }
                    int i47 = dp9;
                    int i48 = dp10;
                    i26 = measuredWidth3;
                    this.storyParams.originalAvatarRect.set(i47, dp4, i47 + AndroidUtilities.dp(54.0f), dp4 + AndroidUtilities.dp(54.0f));
                    int i49 = 0;
                    while (true) {
                        ImageReceiver[] imageReceiverArr2 = this.thumbImage;
                        if (i49 >= imageReceiverArr2.length) {
                            break;
                        }
                        imageReceiverArr2[i49].setImageCoords(((this.thumbSize + 2) * i49) + i48, ((AndroidUtilities.dp(30.0f) + dp4) + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0)) - ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout || (dialogCellTags3 = this.tags) == null || dialogCellTags3.isEmpty()) ? 0 : AndroidUtilities.dp(9.0f)), AndroidUtilities.dp(this.thumbSize), AndroidUtilities.dp(this.thumbSize));
                        i49++;
                        dp4 = dp4;
                    }
                }
                int i50 = dp4;
                int i51 = i26;
                if (LocaleController.isRTL) {
                    this.tagsRight = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart);
                    this.tagsLeft = AndroidUtilities.dp(64.0f);
                } else {
                    this.tagsLeft = this.messageLeft;
                    this.tagsRight = getMeasuredWidth() - AndroidUtilities.dp(64.0f);
                }
                if (this.twoLinesForName) {
                    this.messageNameTop += AndroidUtilities.dp(20.0f);
                }
                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && (dialogCellTags2 = this.tags) != null && !dialogCellTags2.isEmpty()) {
                    this.timeTop -= AndroidUtilities.dp(6.0f);
                    this.checkDrawTop -= AndroidUtilities.dp(6.0f);
                }
                if (getIsPinned()) {
                    this.pinLeft = !LocaleController.isRTL ? (getMeasuredWidth() - Theme.dialogs_pinnedDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(14.0f) : AndroidUtilities.dp(14.0f);
                }
                if (this.drawError) {
                    dp7 = AndroidUtilities.dp(31.0f);
                    i51 -= dp7;
                    if (LocaleController.isRTL) {
                        this.errorLeft = AndroidUtilities.dp(11.0f);
                        this.messageLeft += dp7;
                        this.typingLeft += dp7;
                        this.buttonLeft += dp7;
                        this.messageNameLeft += dp7;
                    } else {
                        this.errorLeft = getMeasuredWidth() - AndroidUtilities.dp(34.0f);
                    }
                } else if (str9 == null && str10 == null && !this.drawReactionMention) {
                    if (getIsPinned()) {
                        int intrinsicWidth4 = Theme.dialogs_pinnedDrawable.getIntrinsicWidth() + AndroidUtilities.dp(8.0f);
                        i51 -= intrinsicWidth4;
                        if (LocaleController.isRTL) {
                            this.messageLeft += intrinsicWidth4;
                            this.typingLeft += intrinsicWidth4;
                            this.buttonLeft += intrinsicWidth4;
                            this.messageNameLeft += intrinsicWidth4;
                        }
                    }
                    this.drawCount = false;
                    this.drawMention = false;
                } else {
                    if (str9 != null) {
                        this.countWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str9)));
                        this.countLayout = new StaticLayout(str9, Theme.dialogs_countTextPaint, this.countWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                        int dp18 = this.countWidth + AndroidUtilities.dp(18.0f);
                        i51 -= dp18;
                        if (LocaleController.isRTL) {
                            this.countLeft = AndroidUtilities.dp(20.0f);
                            this.messageLeft += dp18;
                            this.typingLeft += dp18;
                            this.buttonLeft += dp18;
                            this.messageNameLeft += dp18;
                        } else {
                            this.countLeft = (getMeasuredWidth() - this.countWidth) - AndroidUtilities.dp(20.0f);
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
                        int dp19 = this.mentionWidth + AndroidUtilities.dp(18.0f);
                        i51 -= dp19;
                        if (LocaleController.isRTL) {
                            int dp20 = AndroidUtilities.dp(20.0f);
                            int i52 = this.countWidth;
                            this.mentionLeft = dp20 + (i52 != 0 ? i52 + AndroidUtilities.dp(18.0f) : 0);
                            this.messageLeft += dp19;
                            this.typingLeft += dp19;
                            this.buttonLeft += dp19;
                            this.messageNameLeft += dp19;
                        } else {
                            int measuredWidth4 = (getMeasuredWidth() - this.mentionWidth) - AndroidUtilities.dp(20.0f);
                            int i53 = this.countWidth;
                            this.mentionLeft = measuredWidth4 - (i53 != 0 ? i53 + AndroidUtilities.dp(18.0f) : 0);
                        }
                        this.drawMention = true;
                    } else {
                        this.mentionWidth = 0;
                    }
                    if (this.drawReactionMention) {
                        dp7 = AndroidUtilities.dp(24.0f);
                        i51 -= dp7;
                        if (LocaleController.isRTL) {
                            int dp21 = AndroidUtilities.dp(20.0f);
                            this.reactionMentionLeft = dp21;
                            if (this.drawMention) {
                                int i54 = this.mentionWidth;
                                this.reactionMentionLeft = dp21 + (i54 != 0 ? i54 + AndroidUtilities.dp(18.0f) : 0);
                            }
                            if (this.drawCount) {
                                int i55 = this.reactionMentionLeft;
                                int i56 = this.countWidth;
                                this.reactionMentionLeft = i55 + (i56 != 0 ? i56 + AndroidUtilities.dp(18.0f) : 0);
                            }
                            this.messageLeft += dp7;
                            this.typingLeft += dp7;
                            this.buttonLeft += dp7;
                            this.messageNameLeft += dp7;
                        } else {
                            int measuredWidth5 = getMeasuredWidth() - AndroidUtilities.dp(32.0f);
                            this.reactionMentionLeft = measuredWidth5;
                            if (this.drawMention) {
                                int i57 = this.mentionWidth;
                                this.reactionMentionLeft = measuredWidth5 - (i57 != 0 ? i57 + AndroidUtilities.dp(18.0f) : 0);
                            }
                            if (this.drawCount) {
                                int i58 = this.reactionMentionLeft;
                                int i59 = this.countWidth;
                                this.reactionMentionLeft = i58 - (i59 != 0 ? i59 + AndroidUtilities.dp(18.0f) : 0);
                            }
                        }
                    }
                }
                if (z12) {
                    if (charSequence21 == null) {
                        charSequence21 = "";
                    }
                    if (charSequence21.length() > 150) {
                        charSequence21 = charSequence21.subSequence(0, 150);
                    }
                    charSequence21 = Emoji.replaceEmoji(((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags() && charSequence25 == null) ? AndroidUtilities.replaceTwoNewLinesToOne(charSequence21) : AndroidUtilities.replaceNewLines(charSequence21), Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
                    MessageObject messageObject17 = this.message;
                    if (messageObject17 != null && (highlightText3 = AndroidUtilities.highlightText(charSequence21, messageObject17.highlightedWords, this.resourcesProvider)) != null) {
                        charSequence21 = highlightText3;
                    }
                }
                int max = Math.max(AndroidUtilities.dp(12.0f), i51);
                this.buttonTop = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 58.0f : 62.0f);
                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                    this.buttonTop -= AndroidUtilities.dp(isForumCell() ? 10.0f : 12.0f);
                }
                if (isForumCell()) {
                    this.messageTop = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 34.0f : 39.0f);
                    int i60 = 0;
                    while (true) {
                        ImageReceiver[] imageReceiverArr3 = this.thumbImage;
                        if (i60 >= imageReceiverArr3.length) {
                            break;
                        }
                        imageReceiverArr3[i60].setImageY(this.buttonTop);
                        i60++;
                    }
                } else if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags() && charSequence25 != null && (this.currentDialogFolderId == 0 || this.currentDialogFolderDialogsCount == 1)) {
                    try {
                        MessageObject messageObject18 = this.message;
                        charSequence25 = charSequence25;
                        if (messageObject18 != null) {
                            charSequence25 = charSequence25;
                            if (messageObject18.hasHighlightedWords()) {
                                CharSequence highlightText5 = AndroidUtilities.highlightText(charSequence25, this.message.highlightedWords, this.resourcesProvider);
                                charSequence25 = charSequence25;
                                if (highlightText5 != null) {
                                    charSequence25 = highlightText5;
                                }
                            }
                        }
                        this.messageNameLayout = StaticLayoutEx.createStaticLayout(charSequence25, Theme.dialogs_messageNamePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max, 1);
                    } catch (Exception e4) {
                        FileLog.e(e4);
                    }
                    this.messageTop = AndroidUtilities.dp(51.0f);
                    int dp22 = (this.nameIsEllipsized && this.isTopic) ? AndroidUtilities.dp(20.0f) : 0;
                    int i61 = 0;
                    while (true) {
                        ImageReceiver[] imageReceiverArr4 = this.thumbImage;
                        if (i61 >= imageReceiverArr4.length) {
                            break;
                        }
                        imageReceiverArr4[i61].setImageY(i50 + dp22 + AndroidUtilities.dp(40.0f));
                        i61++;
                    }
                } else {
                    this.messageNameLayout = null;
                    if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                        this.messageTop = AndroidUtilities.dp(32.0f);
                        int dp23 = (this.nameIsEllipsized && this.isTopic) ? AndroidUtilities.dp(20.0f) : 0;
                        int i62 = 0;
                        while (true) {
                            ImageReceiver[] imageReceiverArr5 = this.thumbImage;
                            if (i62 >= imageReceiverArr5.length) {
                                break;
                            }
                            imageReceiverArr5[i62].setImageY(i50 + dp23 + AndroidUtilities.dp(21.0f));
                            i62++;
                        }
                    } else {
                        this.messageTop = AndroidUtilities.dp(39.0f);
                    }
                }
                CharSequence charSequence39 = charSequence25;
                if (this.twoLinesForName) {
                    this.messageTop += AndroidUtilities.dp(20.0f);
                }
                this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack2, this.messageNameLayout);
                try {
                    this.buttonCreated = false;
                    if (TextUtils.isEmpty(spannableStringBuilder5)) {
                        this.buttonLayout = null;
                    } else {
                        this.buttonLayout = new StaticLayout(TextUtils.ellipsize(Emoji.replaceEmoji((CharSequence) spannableStringBuilder5, this.currentMessagePaint.getFontMetricsInt(), AndroidUtilities.dp(17.0f), false), this.currentMessagePaint, max - AndroidUtilities.dp(26.0f), TextUtils.TruncateAt.END), this.currentMessagePaint, max - AndroidUtilities.dp(20.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        this.spoilersPool2.addAll(this.spoilers2);
                        this.spoilers2.clear();
                        SpoilerEffect.addSpoilers(this, this.buttonLayout, this.spoilersPool2, this.spoilers2);
                    }
                } catch (Exception unused) {
                }
                this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack3, this.buttonLayout);
                try {
                    if (!TextUtils.isEmpty(spannableStringBuilder6)) {
                        if (!this.useForceThreeLines) {
                            if (SharedConfig.useThreeLinesLayout) {
                            }
                            createStaticLayout = new StaticLayout(TextUtils.ellipsize(spannableStringBuilder6, this.currentMessagePaint, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END), Theme.dialogs_messagePrintingPaint[this.paintIndex], max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                            this.typingLayout = createStaticLayout;
                        }
                        if (!hasTags()) {
                            createStaticLayout = StaticLayoutEx.createStaticLayout(spannableStringBuilder6, Theme.dialogs_messagePrintingPaint[this.paintIndex], max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, 1);
                            this.typingLayout = createStaticLayout;
                        }
                        createStaticLayout = new StaticLayout(TextUtils.ellipsize(spannableStringBuilder6, this.currentMessagePaint, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END), Theme.dialogs_messagePrintingPaint[this.paintIndex], max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        this.typingLayout = createStaticLayout;
                    }
                } catch (Exception e5) {
                    FileLog.e(e5);
                }
                try {
                    if (charSequence21 instanceof Spannable) {
                        try {
                            Spannable spannable = (Spannable) charSequence21;
                            for (Object obj : spannable.getSpans(0, spannable.length(), Object.class)) {
                                if (!(obj instanceof ClickableSpan)) {
                                    if (!(obj instanceof CodeHighlighting.Span)) {
                                        if (!isFolderCell()) {
                                            if (!(obj instanceof TypefaceSpan)) {
                                            }
                                        }
                                        if (!(obj instanceof CodeHighlighting.ColorSpan)) {
                                            if (!(obj instanceof QuoteSpan)) {
                                                if (!(obj instanceof QuoteSpan.QuoteStyleSpan)) {
                                                    if ((obj instanceof StyleSpan) && ((StyleSpan) obj).getStyle() == 1) {
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                spannable.removeSpan(obj);
                            }
                        } catch (Exception e6) {
                            e = e6;
                            StaticLayout staticLayout2 = null;
                            i27 = 1;
                            this.messageLayout = staticLayout2;
                            FileLog.e(e);
                            i28 = max;
                            AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans = this.animatedEmojiStack;
                            Layout[] layoutArr = new Layout[i27];
                            layoutArr[0] = this.messageLayout;
                            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, emojiGroupedSpans, layoutArr);
                            if (LocaleController.isRTL) {
                            }
                            staticLayout = this.typingLayout;
                            if (staticLayout != null) {
                            }
                            updateThumbsPosition();
                            return;
                        }
                    }
                } catch (Exception e7) {
                    e = e7;
                    i27 = 1;
                }
                if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
                    try {
                    } catch (Exception e8) {
                        e = e8;
                        StaticLayout staticLayout22 = null;
                        i27 = 1;
                        this.messageLayout = staticLayout22;
                        FileLog.e(e);
                        i28 = max;
                        AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans2 = this.animatedEmojiStack;
                        Layout[] layoutArr2 = new Layout[i27];
                        layoutArr2[0] = this.messageLayout;
                        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, emojiGroupedSpans2, layoutArr2);
                        if (LocaleController.isRTL) {
                        }
                        staticLayout = this.typingLayout;
                        if (staticLayout != null) {
                        }
                        updateThumbsPosition();
                        return;
                    }
                    if (this.currentDialogFolderId != 0) {
                        if (this.currentDialogFolderDialogsCount > 1) {
                            this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                            charSequence26 = charSequence39;
                            charSequence39 = null;
                            alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                            if ((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
                                if (this.thumbsCount > 0 && charSequence39 != null) {
                                    max += AndroidUtilities.dp(5.0f);
                                }
                                this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence26, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence39 == null ? 1 : 2);
                            } else {
                                if (this.thumbsCount > 0) {
                                    max += AndroidUtilities.dp((i31 * (this.thumbSize + 2)) + 3);
                                    if (LocaleController.isRTL && !isForumCell()) {
                                        this.messageLeft -= AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 3);
                                    }
                                }
                                this.messageLayout = new StaticLayout(charSequence26, this.currentMessagePaint, max, alignment, 1.0f, 0.0f, false);
                            }
                            i28 = max;
                            this.spoilersPool.addAll(this.spoilers);
                            this.spoilers.clear();
                            i27 = 1;
                            SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                            AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans22 = this.animatedEmojiStack;
                            Layout[] layoutArr22 = new Layout[i27];
                            layoutArr22[0] = this.messageLayout;
                            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, emojiGroupedSpans22, layoutArr22);
                            if (LocaleController.isRTL) {
                                StaticLayout staticLayout3 = this.nameLayout;
                                if (staticLayout3 != null && staticLayout3.getLineCount() > 0) {
                                    float lineLeft = this.nameLayout.getLineLeft(0);
                                    double ceil = Math.ceil(this.nameLayout.getLineWidth(0));
                                    this.nameLeft += AndroidUtilities.dp(12.0f);
                                    if (this.nameLayoutEllipsizeByGradient) {
                                        ceil = Math.min(this.nameWidth, ceil);
                                    }
                                    if ((!this.dialogMuted && !this.drawUnmute) || this.drawVerified || this.drawScam != 0) {
                                        if (this.drawVerified) {
                                            double d2 = this.nameLeft;
                                            double d3 = this.nameWidth;
                                            Double.isNaN(d3);
                                            Double.isNaN(d2);
                                            double d4 = d2 + (d3 - ceil);
                                            double dp24 = AndroidUtilities.dp(6.0f);
                                            Double.isNaN(dp24);
                                            d = d4 - dp24;
                                            drawable = Theme.dialogs_verifiedDrawable;
                                            i30 = drawable.getIntrinsicWidth();
                                            double d5 = i30;
                                            Double.isNaN(d5);
                                            this.nameMuteLeft = (int) (d - d5);
                                            if (lineLeft == 0.0f) {
                                                double d6 = this.nameWidth;
                                                if (ceil < d6) {
                                                    double d7 = this.nameLeft;
                                                    Double.isNaN(d6);
                                                    Double.isNaN(d7);
                                                    this.nameLeft = (int) (d7 + (d6 - ceil));
                                                }
                                            }
                                        } else {
                                            if (this.drawPremium) {
                                                double d8 = this.nameLeft;
                                                double d9 = this.nameWidth;
                                                Double.isNaN(d9);
                                                double d10 = lineLeft;
                                                Double.isNaN(d10);
                                                Double.isNaN(d8);
                                                d = d8 + ((d9 - ceil) - d10);
                                                i30 = AndroidUtilities.dp(24.0f);
                                            } else if (this.drawScam != 0) {
                                                double d11 = this.nameLeft;
                                                double d12 = this.nameWidth;
                                                Double.isNaN(d12);
                                                Double.isNaN(d11);
                                                double d13 = d11 + (d12 - ceil);
                                                double dp25 = AndroidUtilities.dp(6.0f);
                                                Double.isNaN(dp25);
                                                d = d13 - dp25;
                                                i30 = (this.drawScam == i27 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).getIntrinsicWidth();
                                            }
                                            double d52 = i30;
                                            Double.isNaN(d52);
                                            this.nameMuteLeft = (int) (d - d52);
                                            if (lineLeft == 0.0f) {
                                            }
                                        }
                                    }
                                    double d14 = this.nameLeft;
                                    double d15 = this.nameWidth;
                                    Double.isNaN(d15);
                                    Double.isNaN(d14);
                                    double d16 = d14 + (d15 - ceil);
                                    double dp26 = AndroidUtilities.dp(6.0f);
                                    Double.isNaN(dp26);
                                    d = d16 - dp26;
                                    drawable = Theme.dialogs_muteDrawable;
                                    i30 = drawable.getIntrinsicWidth();
                                    double d522 = i30;
                                    Double.isNaN(d522);
                                    this.nameMuteLeft = (int) (d - d522);
                                    if (lineLeft == 0.0f) {
                                    }
                                }
                                StaticLayout staticLayout4 = this.messageLayout;
                                int i63 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                if (staticLayout4 != null && (lineCount6 = staticLayout4.getLineCount()) > 0) {
                                    int i64 = 0;
                                    int i65 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                    while (true) {
                                        if (i64 >= lineCount6) {
                                            break;
                                        }
                                        if (this.messageLayout.getLineLeft(i64) != 0.0f) {
                                            i65 = 0;
                                            break;
                                        }
                                        double ceil2 = Math.ceil(this.messageLayout.getLineWidth(i64));
                                        double d17 = i28;
                                        Double.isNaN(d17);
                                        i65 = Math.min(i65, (int) (d17 - ceil2));
                                        i64 += i27;
                                    }
                                    if (i65 != Integer.MAX_VALUE) {
                                        this.messageLeft += i65;
                                    }
                                }
                                StaticLayout staticLayout5 = this.typingLayout;
                                if (staticLayout5 != null && (lineCount5 = staticLayout5.getLineCount()) > 0) {
                                    int i66 = 0;
                                    int i67 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                    while (true) {
                                        if (i66 >= lineCount5) {
                                            break;
                                        }
                                        if (this.typingLayout.getLineLeft(i66) != 0.0f) {
                                            i67 = 0;
                                            break;
                                        }
                                        double ceil3 = Math.ceil(this.typingLayout.getLineWidth(i66));
                                        double d18 = i28;
                                        Double.isNaN(d18);
                                        i67 = Math.min(i67, (int) (d18 - ceil3));
                                        i66 += i27;
                                    }
                                    if (i67 != Integer.MAX_VALUE) {
                                        this.typingLeft += i67;
                                    }
                                }
                                StaticLayout staticLayout6 = this.messageNameLayout;
                                if (staticLayout6 != null && staticLayout6.getLineCount() > 0 && this.messageNameLayout.getLineLeft(0) == 0.0f) {
                                    double ceil4 = Math.ceil(this.messageNameLayout.getLineWidth(0));
                                    double d19 = i28;
                                    if (ceil4 < d19) {
                                        double d20 = this.messageNameLeft;
                                        Double.isNaN(d19);
                                        Double.isNaN(d20);
                                        this.messageNameLeft = (int) (d20 + (d19 - ceil4));
                                    }
                                }
                                StaticLayout staticLayout7 = this.buttonLayout;
                                if (staticLayout7 != null && (lineCount4 = staticLayout7.getLineCount()) > 0) {
                                    for (int i68 = 0; i68 < lineCount4; i68 += i27) {
                                        i63 = (int) Math.min(i63, this.buttonLayout.getWidth() - this.buttonLayout.getLineRight(i68));
                                    }
                                    this.buttonLeft += i63;
                                }
                            } else {
                                StaticLayout staticLayout8 = this.nameLayout;
                                if (staticLayout8 != null && staticLayout8.getLineCount() > 0) {
                                    float lineRight = this.nameLayout.getLineRight(0);
                                    if (this.nameLayoutEllipsizeByGradient) {
                                        lineRight = Math.min(this.nameWidth, lineRight);
                                    }
                                    if (lineRight == this.nameWidth) {
                                        double ceil5 = Math.ceil(this.nameLayout.getLineWidth(0));
                                        if (this.nameLayoutEllipsizeByGradient) {
                                            ceil5 = Math.min(this.nameWidth, ceil5);
                                        }
                                        double d21 = this.nameWidth;
                                        if (ceil5 < d21) {
                                            double d22 = this.nameLeft;
                                            Double.isNaN(d21);
                                            Double.isNaN(d22);
                                            this.nameLeft = (int) (d22 - (d21 - ceil5));
                                        }
                                    }
                                    this.nameMuteLeft = (int) (this.nameLeft + lineRight + AndroidUtilities.dp(6.0f));
                                }
                                StaticLayout staticLayout9 = this.messageLayout;
                                if (staticLayout9 != null && (lineCount3 = staticLayout9.getLineCount()) > 0) {
                                    float f3 = 2.14748365E9f;
                                    for (int i69 = 0; i69 < lineCount3; i69 += i27) {
                                        f3 = Math.min(f3, this.messageLayout.getLineLeft(i69));
                                    }
                                    this.messageLeft = (int) (this.messageLeft - f3);
                                }
                                StaticLayout staticLayout10 = this.buttonLayout;
                                if (staticLayout10 != null && (lineCount2 = staticLayout10.getLineCount()) > 0) {
                                    float f4 = 2.14748365E9f;
                                    for (int i70 = 0; i70 < lineCount2; i70 += i27) {
                                        f4 = Math.min(f4, this.buttonLayout.getLineLeft(i70));
                                    }
                                    this.buttonLeft = (int) (this.buttonLeft - f4);
                                }
                                StaticLayout staticLayout11 = this.typingLayout;
                                if (staticLayout11 != null && (lineCount = staticLayout11.getLineCount()) > 0) {
                                    float f5 = 2.14748365E9f;
                                    for (int i71 = 0; i71 < lineCount; i71 += i27) {
                                        f5 = Math.min(f5, this.typingLayout.getLineLeft(i71));
                                    }
                                    this.typingLeft = (int) (this.typingLeft - f5);
                                }
                                StaticLayout staticLayout12 = this.messageNameLayout;
                                if (staticLayout12 != null && staticLayout12.getLineCount() > 0) {
                                    this.messageNameLeft = (int) (this.messageNameLeft - this.messageNameLayout.getLineLeft(0));
                                }
                            }
                            staticLayout = this.typingLayout;
                            if (staticLayout != null && this.printingStringType >= 0 && staticLayout.getText().length() > 0) {
                                if (i20 >= 0 || (i29 = i20 + 1) >= this.typingLayout.getText().length()) {
                                    primaryHorizontal = this.typingLayout.getPrimaryHorizontal(0);
                                    primaryHorizontal2 = this.typingLayout.getPrimaryHorizontal(i27);
                                } else {
                                    primaryHorizontal = this.typingLayout.getPrimaryHorizontal(i20);
                                    primaryHorizontal2 = this.typingLayout.getPrimaryHorizontal(i29);
                                }
                                this.statusDrawableLeft = primaryHorizontal >= primaryHorizontal2 ? (int) (this.typingLeft + primaryHorizontal) : (int) (this.typingLeft + primaryHorizontal2 + AndroidUtilities.dp(3.0f));
                            }
                            updateThumbsPosition();
                            return;
                        }
                        if (!this.useForceThreeLines) {
                        }
                        if (!hasTags()) {
                            if (charSequence39 != null) {
                            }
                            charSequence26 = charSequence21;
                            alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                            if (this.useForceThreeLines) {
                            }
                            if (this.thumbsCount > 0) {
                                max += AndroidUtilities.dp(5.0f);
                            }
                            this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence26, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence39 == null ? 1 : 2);
                            i28 = max;
                            this.spoilersPool.addAll(this.spoilers);
                            this.spoilers.clear();
                            i27 = 1;
                            SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                            AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans222 = this.animatedEmojiStack;
                            Layout[] layoutArr222 = new Layout[i27];
                            layoutArr222[0] = this.messageLayout;
                            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, emojiGroupedSpans222, layoutArr222);
                            if (LocaleController.isRTL) {
                            }
                            staticLayout = this.typingLayout;
                            if (staticLayout != null) {
                                if (i20 >= 0) {
                                }
                                primaryHorizontal = this.typingLayout.getPrimaryHorizontal(0);
                                primaryHorizontal2 = this.typingLayout.getPrimaryHorizontal(i27);
                                this.statusDrawableLeft = primaryHorizontal >= primaryHorizontal2 ? (int) (this.typingLeft + primaryHorizontal) : (int) (this.typingLeft + primaryHorizontal2 + AndroidUtilities.dp(3.0f));
                            }
                            updateThumbsPosition();
                            return;
                        }
                        if (isForumCell() && (charSequence21 instanceof Spanned) && ((FixedWidthSpan[]) ((Spanned) charSequence21).getSpans(0, charSequence21.length(), FixedWidthSpan.class)).length <= 0) {
                            textPaint2 = this.currentMessagePaint;
                            dp8 = max - AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 15);
                            truncateAt = TextUtils.TruncateAt.END;
                        } else {
                            textPaint2 = this.currentMessagePaint;
                            dp8 = max - AndroidUtilities.dp(12.0f);
                            truncateAt = TextUtils.TruncateAt.END;
                        }
                        charSequence21 = TextUtils.ellipsize(charSequence21, textPaint2, dp8, truncateAt);
                        charSequence26 = charSequence21;
                        alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                        if (this.useForceThreeLines) {
                        }
                        if (this.thumbsCount > 0) {
                        }
                        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence26, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence39 == null ? 1 : 2);
                        i28 = max;
                        this.spoilersPool.addAll(this.spoilers);
                        this.spoilers.clear();
                        i27 = 1;
                        SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                        AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans2222 = this.animatedEmojiStack;
                        Layout[] layoutArr2222 = new Layout[i27];
                        layoutArr2222[0] = this.messageLayout;
                        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, emojiGroupedSpans2222, layoutArr2222);
                        if (LocaleController.isRTL) {
                        }
                        staticLayout = this.typingLayout;
                        if (staticLayout != null) {
                        }
                        updateThumbsPosition();
                        return;
                    }
                }
                if (!this.useForceThreeLines) {
                }
                if (!hasTags()) {
                }
                if (isForumCell()) {
                }
                textPaint2 = this.currentMessagePaint;
                dp8 = max - AndroidUtilities.dp(12.0f);
                truncateAt = TextUtils.TruncateAt.END;
                charSequence21 = TextUtils.ellipsize(charSequence21, textPaint2, dp8, truncateAt);
                charSequence26 = charSequence21;
                alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                if (this.useForceThreeLines) {
                }
                if (this.thumbsCount > 0) {
                }
                this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence26, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence39 == null ? 1 : 2);
                i28 = max;
                this.spoilersPool.addAll(this.spoilers);
                this.spoilers.clear();
                i27 = 1;
                SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans22222 = this.animatedEmojiStack;
                Layout[] layoutArr22222 = new Layout[i27];
                layoutArr22222[0] = this.messageLayout;
                this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, emojiGroupedSpans22222, layoutArr22222);
                if (LocaleController.isRTL) {
                }
                staticLayout = this.typingLayout;
                if (staticLayout != null) {
                }
                updateThumbsPosition();
                return;
            }
            this.clockDrawLeft = this.timeLeft + i23 + AndroidUtilities.dp(5.0f);
            i25 = this.nameLeft;
        }
        this.nameLeft = i25 + intrinsicWidth;
        if (!this.drawPremium) {
        }
        if (this.dialogMuted) {
        }
        dp3 = AndroidUtilities.dp(6.0f) + Theme.dialogs_muteDrawable.getIntrinsicWidth();
        this.nameWidth -= dp3;
    }

    public boolean checkCurrentDialogIndex(boolean z) {
        return false;
    }

    public void checkHeight() {
        if (getMeasuredHeight() <= 0 || getMeasuredHeight() == computeHeight()) {
            return;
        }
        requestLayout();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if ((!this.isTopic && motionEvent.getAction() == 1) || motionEvent.getAction() == 3) {
            this.storyParams.checkOnTouchEvent(motionEvent, this);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    /* JADX WARN: Code restructure failed: missing block: B:60:0x0234, code lost:
        if (r3 > 0) goto L87;
     */
    /* JADX WARN: Removed duplicated region for block: B:217:0x05cb  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x064f  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x065c  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x067b  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x0682  */
    /* JADX WARN: Removed duplicated region for block: B:242:0x0695  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x06ad  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x06b6  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x06c4  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0242  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0274  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x029f  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x02b5  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0315  */
    @Override // org.telegram.ui.Stories.StoriesListPlaceProvider.AvatarOverlaysView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean drawAvatarOverlays(Canvas canvas) {
        boolean z;
        int dp;
        float dp2;
        float dp3;
        boolean z2;
        float dp4;
        int dp5;
        float dp6;
        int dp7;
        float f;
        boolean z3;
        float dp8;
        Paint paint;
        int color2;
        CheckBox2 checkBox2;
        Drawable drawable;
        TLRPC$Chat tLRPC$Chat = this.chat;
        if (tLRPC$Chat == null || (tLRPC$Chat.flags2 & 2048) == 0) {
            z = false;
        } else {
            float imageY2 = this.avatarImage.getImageY2();
            float imageX2 = this.avatarImage.getImageX2();
            CheckBox2 checkBox22 = this.checkBox;
            float progress = (checkBox22 == null || !checkBox22.isChecked()) ? 1.0f : 1.0f - this.checkBox.getProgress();
            if (this.starBg == null) {
                this.starBg = getContext().getResources().getDrawable(R.drawable.star_small_outline).mutate();
            }
            int color = Theme.getColor(Theme.key_windowBackgroundWhite);
            if (this.starBgColor != color) {
                Drawable drawable2 = this.starBg;
                this.starBgColor = color;
                drawable2.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
            if (this.starFg == null) {
                this.starFg = getContext().getResources().getDrawable(R.drawable.star_small_inner).mutate();
            }
            int dp9 = AndroidUtilities.dp(19.33f);
            Rect rect = AndroidUtilities.rectTmp2;
            int i = (int) imageX2;
            int i2 = (int) imageY2;
            int i3 = i2 - dp9;
            rect.set((AndroidUtilities.dp(1.66f) + i) - dp9, i3, AndroidUtilities.dp(1.66f) + i, i2);
            rect.inset(-AndroidUtilities.dp(1.0f), -AndroidUtilities.dp(1.0f));
            this.starBg.setBounds(rect);
            int i4 = (int) (progress * 255.0f);
            this.starBg.setAlpha(i4);
            this.starBg.draw(canvas);
            rect.set((AndroidUtilities.dp(1.66f) + i) - dp9, i3, i + AndroidUtilities.dp(1.66f), i2);
            this.starFg.setBounds(rect);
            this.starFg.setAlpha(i4);
            this.starFg.draw(canvas);
            z = true;
        }
        float f2 = this.premiumBlockedT.set(this.premiumBlocked && !z);
        float f3 = 10.0f;
        if (f2 > 0.0f) {
            float centerY = this.avatarImage.getCenterY() + AndroidUtilities.dp(18.0f);
            float centerX = this.avatarImage.getCenterX() + AndroidUtilities.dp(18.0f);
            canvas.save();
            Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
            canvas.drawCircle(centerX, centerY, AndroidUtilities.dp(11.33f) * f2, Theme.dialogs_onlineCirclePaint);
            if (this.premiumGradient == null) {
                this.premiumGradient = new PremiumGradient.PremiumGradientTools(Theme.key_premiumGradient1, Theme.key_premiumGradient2, -1, -1, -1, this.resourcesProvider);
            }
            this.premiumGradient.gradientMatrix((int) (centerX - AndroidUtilities.dp(10.0f)), (int) (centerY - AndroidUtilities.dp(10.0f)), (int) (AndroidUtilities.dp(10.0f) + centerX), (int) (AndroidUtilities.dp(10.0f) + centerY), 0.0f, 0.0f);
            canvas.drawCircle(centerX, centerY, AndroidUtilities.dp(10.0f) * f2, this.premiumGradient.paint);
            if (this.lockDrawable == null) {
                Drawable mutate = getContext().getResources().getDrawable(R.drawable.msg_mini_lock2).mutate();
                this.lockDrawable = mutate;
                mutate.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
            }
            this.lockDrawable.setBounds((int) (centerX - (((drawable.getIntrinsicWidth() / 2.0f) * 0.875f) * f2)), (int) (centerY - (((this.lockDrawable.getIntrinsicHeight() / 2.0f) * 0.875f) * f2)), (int) (centerX + ((this.lockDrawable.getIntrinsicWidth() / 2.0f) * 0.875f * f2)), (int) (centerY + ((this.lockDrawable.getIntrinsicHeight() / 2.0f) * 0.875f * f2)));
            this.lockDrawable.setAlpha((int) (f2 * 255.0f));
            this.lockDrawable.draw(canvas);
            canvas.restore();
            return false;
        } else if (this.isDialogCell && this.currentDialogFolderId == 0 && !z) {
            boolean z4 = (this.ttlPeriod <= 0 || isOnline() || this.hasCall) ? false : true;
            this.showTtl = z4;
            if (this.rightFragmentOpenedProgress != 1.0f && (z4 || this.ttlProgress > 0.0f)) {
                TimerDrawable timerDrawable = this.timerDrawable;
                if (timerDrawable != null) {
                    int time = timerDrawable.getTime();
                    int i5 = this.ttlPeriod;
                    if (time != i5) {
                    }
                    if (this.timerPaint == null) {
                        this.timerPaint = new Paint(1);
                        Paint paint2 = new Paint(1);
                        this.timerPaint2 = paint2;
                        paint2.setColor(838860800);
                    }
                    int imageY22 = (int) (this.avatarImage.getImageY2() - AndroidUtilities.dp(9.0f));
                    int dp10 = (int) (!LocaleController.isRTL ? this.storyParams.originalAvatarRect.left + AndroidUtilities.dp(9.0f) : this.storyParams.originalAvatarRect.right - AndroidUtilities.dp(9.0f));
                    this.timerDrawable.setBounds(0, 0, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(22.0f));
                    this.timerDrawable.setTime(this.ttlPeriod);
                    if (this.avatarImage.updateThumbShaderMatrix()) {
                        this.timerPaint.setShader(null);
                        if (this.avatarImage.getBitmap() != null && !this.avatarImage.getBitmap().isRecycled()) {
                            paint = this.timerPaint;
                            color2 = AndroidUtilities.getDominantColor(this.avatarImage.getBitmap());
                        } else if (this.avatarImage.getDrawable() instanceof VectorAvatarThumbDrawable) {
                            this.timerPaint.setColor(((VectorAvatarThumbDrawable) this.avatarImage.getDrawable()).gradientTools.getAverageColor());
                        } else {
                            paint = this.timerPaint;
                            color2 = this.avatarDrawable.getColor2();
                        }
                        paint.setColor(color2);
                    } else {
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
                    }
                    canvas.save();
                    float f4 = this.ttlProgress * (1.0f - this.rightFragmentOpenedProgress);
                    checkBox2 = this.checkBox;
                    if (checkBox2 != null) {
                        f4 *= 1.0f - checkBox2.getProgress();
                    }
                    float f5 = dp10;
                    float f6 = imageY22;
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
                int imageY222 = (int) (this.avatarImage.getImageY2() - AndroidUtilities.dp(9.0f));
                int dp102 = (int) (!LocaleController.isRTL ? this.storyParams.originalAvatarRect.left + AndroidUtilities.dp(9.0f) : this.storyParams.originalAvatarRect.right - AndroidUtilities.dp(9.0f));
                this.timerDrawable.setBounds(0, 0, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(22.0f));
                this.timerDrawable.setTime(this.ttlPeriod);
                if (this.avatarImage.updateThumbShaderMatrix()) {
                }
                canvas.save();
                float f42 = this.ttlProgress * (1.0f - this.rightFragmentOpenedProgress);
                checkBox2 = this.checkBox;
                if (checkBox2 != null) {
                }
                float f52 = dp102;
                float f62 = imageY222;
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
            if (tLRPC$User != null && !MessagesController.isSupportUser(tLRPC$User) && !this.user.bot) {
                boolean isOnline = isOnline();
                this.wasDrawnOnline = isOnline;
                if (isOnline || this.onlineProgress != 0.0f) {
                    int dp11 = (int) (this.storyParams.originalAvatarRect.bottom - AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 6.0f : 8.0f));
                    if (LocaleController.isRTL) {
                        float f7 = this.storyParams.originalAvatarRect.left;
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f3 = 6.0f;
                        }
                        dp8 = f7 + AndroidUtilities.dp(f3);
                    } else {
                        float f8 = this.storyParams.originalAvatarRect.right;
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f3 = 6.0f;
                        }
                        dp8 = f8 - AndroidUtilities.dp(f3);
                    }
                    int i6 = (int) dp8;
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                    float f9 = i6;
                    float f10 = dp11;
                    canvas.drawCircle(f9, f10, AndroidUtilities.dp(7.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_chats_onlineCircle, this.resourcesProvider));
                    canvas.drawCircle(f9, f10, AndroidUtilities.dp(5.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    float f11 = this.onlineProgress;
                    if (isOnline) {
                        if (f11 < 1.0f) {
                            float f12 = f11 + 0.10666667f;
                            this.onlineProgress = f12;
                            if (f12 > 1.0f) {
                                this.onlineProgress = 1.0f;
                            }
                            z2 = true;
                        }
                    } else if (f11 > 0.0f) {
                        float f13 = f11 - 0.10666667f;
                        this.onlineProgress = f13;
                        if (f13 < 0.0f) {
                            this.onlineProgress = 0.0f;
                        }
                        z2 = true;
                    }
                    if (this.showTtl) {
                    }
                    this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                    return z3;
                }
                z2 = false;
                if (this.showTtl) {
                }
                this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                return z3;
            }
            TLRPC$Chat tLRPC$Chat2 = this.chat;
            if (tLRPC$Chat2 != null) {
                boolean z5 = tLRPC$Chat2.call_active && tLRPC$Chat2.call_not_empty;
                this.hasCall = z5;
                if ((z5 || this.chatCallProgress != 0.0f) && this.rightFragmentOpenedProgress < 1.0f) {
                    CheckBox2 checkBox23 = this.checkBox;
                    float progress2 = (checkBox23 == null || !checkBox23.isChecked()) ? 1.0f : 1.0f - this.checkBox.getProgress();
                    int dp12 = (int) (this.storyParams.originalAvatarRect.bottom - AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 6.0f : 8.0f));
                    if (LocaleController.isRTL) {
                        dp = (int) (this.storyParams.originalAvatarRect.left + AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 10.0f : 6.0f));
                    } else {
                        dp = (int) (this.storyParams.originalAvatarRect.right - AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 10.0f : 6.0f));
                    }
                    if (this.rightFragmentOpenedProgress != 0.0f) {
                        canvas.save();
                        float f14 = 1.0f - this.rightFragmentOpenedProgress;
                        canvas.scale(f14, f14, dp, dp12);
                    }
                    Paint paint3 = Theme.dialogs_onlineCirclePaint;
                    int i7 = Theme.key_windowBackgroundWhite;
                    paint3.setColor(Theme.getColor(i7, this.resourcesProvider));
                    float f15 = dp;
                    float f16 = dp12;
                    canvas.drawCircle(f15, f16, AndroidUtilities.dp(11.0f) * this.chatCallProgress * progress2, Theme.dialogs_onlineCirclePaint);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_chats_onlineCircle, this.resourcesProvider));
                    canvas.drawCircle(f15, f16, AndroidUtilities.dp(9.0f) * this.chatCallProgress * progress2, Theme.dialogs_onlineCirclePaint);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(i7, this.resourcesProvider));
                    if (!LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                        this.innerProgress = 0.65f;
                    }
                    int i8 = this.progressStage;
                    if (i8 != 0) {
                        if (i8 != 1) {
                            if (i8 != 2) {
                                if (i8 == 3) {
                                    dp4 = AndroidUtilities.dp(3.0f);
                                    dp5 = AndroidUtilities.dp(2.0f);
                                } else if (i8 != 4) {
                                    if (i8 != 5) {
                                        if (i8 == 6) {
                                            dp6 = AndroidUtilities.dp(1.0f);
                                            dp7 = AndroidUtilities.dp(4.0f);
                                        } else {
                                            dp4 = AndroidUtilities.dp(5.0f);
                                            dp5 = AndroidUtilities.dp(4.0f);
                                        }
                                    }
                                }
                                dp2 = dp4 - (dp5 * this.innerProgress);
                                dp3 = (AndroidUtilities.dp(2.0f) * this.innerProgress) + AndroidUtilities.dp(1.0f);
                                if (this.chatCallProgress >= 1.0f || progress2 < 1.0f) {
                                    canvas.save();
                                    float f17 = this.chatCallProgress * progress2;
                                    canvas.scale(f17, f17, f15, f16);
                                }
                                this.rect.set(dp - AndroidUtilities.dp(1.0f), f16 - dp2, dp + AndroidUtilities.dp(1.0f), dp2 + f16);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                float f18 = f16 - dp3;
                                float f19 = f16 + dp3;
                                this.rect.set(dp - AndroidUtilities.dp(5.0f), f18, dp - AndroidUtilities.dp(3.0f), f19);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                this.rect.set(AndroidUtilities.dp(3.0f) + dp, f18, dp + AndroidUtilities.dp(5.0f), f19);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                if (this.chatCallProgress >= 1.0f || progress2 < 1.0f) {
                                    canvas.restore();
                                }
                                if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                                    z2 = false;
                                } else {
                                    float f20 = this.innerProgress + 0.04f;
                                    this.innerProgress = f20;
                                    if (f20 >= 1.0f) {
                                        this.innerProgress = 0.0f;
                                        int i9 = this.progressStage + 1;
                                        this.progressStage = i9;
                                        if (i9 >= 8) {
                                            this.progressStage = 0;
                                        }
                                    }
                                    z2 = true;
                                }
                                if (this.hasCall) {
                                    float f21 = this.chatCallProgress;
                                    if (f21 > 0.0f) {
                                        float f22 = f21 - 0.10666667f;
                                        this.chatCallProgress = f22;
                                        if (f22 < 0.0f) {
                                            this.chatCallProgress = 0.0f;
                                        }
                                    }
                                } else {
                                    float f23 = this.chatCallProgress;
                                    if (f23 < 1.0f) {
                                        float f24 = f23 + 0.10666667f;
                                        this.chatCallProgress = f24;
                                        if (f24 > 1.0f) {
                                            this.chatCallProgress = 1.0f;
                                        }
                                    }
                                }
                                if (this.rightFragmentOpenedProgress != 0.0f) {
                                    canvas.restore();
                                }
                                if (this.showTtl) {
                                    float f25 = this.ttlProgress;
                                    if (f25 < 1.0f) {
                                        f = f25 + 0.10666667f;
                                        this.ttlProgress = f;
                                        z3 = true;
                                    }
                                    z3 = z2;
                                } else {
                                    float f26 = this.ttlProgress;
                                    if (f26 > 0.0f) {
                                        f = f26 - 0.10666667f;
                                        this.ttlProgress = f;
                                        z3 = true;
                                    }
                                    z3 = z2;
                                }
                                this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                                return z3;
                            }
                            dp6 = AndroidUtilities.dp(1.0f);
                            dp7 = AndroidUtilities.dp(2.0f);
                            dp2 = dp6 + (dp7 * this.innerProgress);
                            dp3 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            if (this.chatCallProgress >= 1.0f) {
                            }
                            canvas.save();
                            float f172 = this.chatCallProgress * progress2;
                            canvas.scale(f172, f172, f15, f16);
                            this.rect.set(dp - AndroidUtilities.dp(1.0f), f16 - dp2, dp + AndroidUtilities.dp(1.0f), dp2 + f16);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                            float f182 = f16 - dp3;
                            float f192 = f16 + dp3;
                            this.rect.set(dp - AndroidUtilities.dp(5.0f), f182, dp - AndroidUtilities.dp(3.0f), f192);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                            this.rect.set(AndroidUtilities.dp(3.0f) + dp, f182, dp + AndroidUtilities.dp(5.0f), f192);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                            if (this.chatCallProgress >= 1.0f) {
                            }
                            canvas.restore();
                            if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                            }
                            if (this.hasCall) {
                            }
                            if (this.rightFragmentOpenedProgress != 0.0f) {
                            }
                            if (this.showTtl) {
                            }
                            this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                            return z3;
                        }
                        dp2 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                        dp3 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                        if (this.chatCallProgress >= 1.0f) {
                        }
                        canvas.save();
                        float f1722 = this.chatCallProgress * progress2;
                        canvas.scale(f1722, f1722, f15, f16);
                        this.rect.set(dp - AndroidUtilities.dp(1.0f), f16 - dp2, dp + AndroidUtilities.dp(1.0f), dp2 + f16);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        float f1822 = f16 - dp3;
                        float f1922 = f16 + dp3;
                        this.rect.set(dp - AndroidUtilities.dp(5.0f), f1822, dp - AndroidUtilities.dp(3.0f), f1922);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        this.rect.set(AndroidUtilities.dp(3.0f) + dp, f1822, dp + AndroidUtilities.dp(5.0f), f1922);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        if (this.chatCallProgress >= 1.0f) {
                        }
                        canvas.restore();
                        if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                        }
                        if (this.hasCall) {
                        }
                        if (this.rightFragmentOpenedProgress != 0.0f) {
                        }
                        if (this.showTtl) {
                        }
                        this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                        return z3;
                    }
                    dp2 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                    dp3 = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(2.0f) * this.innerProgress);
                    if (this.chatCallProgress >= 1.0f) {
                    }
                    canvas.save();
                    float f17222 = this.chatCallProgress * progress2;
                    canvas.scale(f17222, f17222, f15, f16);
                    this.rect.set(dp - AndroidUtilities.dp(1.0f), f16 - dp2, dp + AndroidUtilities.dp(1.0f), dp2 + f16);
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                    float f18222 = f16 - dp3;
                    float f19222 = f16 + dp3;
                    this.rect.set(dp - AndroidUtilities.dp(5.0f), f18222, dp - AndroidUtilities.dp(3.0f), f19222);
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                    this.rect.set(AndroidUtilities.dp(3.0f) + dp, f18222, dp + AndroidUtilities.dp(5.0f), f19222);
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                    if (this.chatCallProgress >= 1.0f) {
                    }
                    canvas.restore();
                    if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                    }
                    if (this.hasCall) {
                    }
                    if (this.rightFragmentOpenedProgress != 0.0f) {
                    }
                    if (this.showTtl) {
                    }
                    this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                    return z3;
                }
            }
            z2 = false;
            if (this.showTtl) {
            }
            this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
            return z3;
        } else {
            return false;
        }
    }

    protected boolean drawLock2() {
        return false;
    }

    public float getClipProgress() {
        return this.clipProgress;
    }

    public int getCurrentDialogFolderId() {
        return this.currentDialogFolderId;
    }

    public long getDialogId() {
        return this.currentDialogId;
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

    public MessageObject getMessage() {
        return this.message;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public String getMessageNameString() {
        TLRPC$Chat chat;
        TLRPC$User tLRPC$User;
        String str;
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        String str2;
        MessageObject messageObject;
        TLRPC$Message tLRPC$Message2;
        TLRPC$User user;
        TLRPC$Message tLRPC$Message3;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader2;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader3;
        MessageObject messageObject2 = this.message;
        if (messageObject2 == null) {
            return null;
        }
        long fromChatId = messageObject2.getFromChatId();
        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        if (!this.isSavedDialog && this.currentDialogId == clientUserId) {
            long savedDialogId = this.message.getSavedDialogId();
            if (savedDialogId == clientUserId) {
                return null;
            }
            if (savedDialogId != UserObject.ANONYMOUS) {
                TLRPC$Message tLRPC$Message4 = this.message.messageOwner;
                if (tLRPC$Message4 != null && (tLRPC$MessageFwdHeader3 = tLRPC$Message4.fwd_from) != null) {
                    long peerDialogId = DialogObject.getPeerDialogId(tLRPC$MessageFwdHeader3.saved_from_id);
                    if (peerDialogId == 0) {
                        peerDialogId = DialogObject.getPeerDialogId(this.message.messageOwner.fwd_from.from_id);
                    }
                    if (peerDialogId > 0 && peerDialogId != savedDialogId) {
                        return null;
                    }
                }
                fromChatId = savedDialogId;
            }
        }
        if (this.isSavedDialog && (tLRPC$Message3 = this.message.messageOwner) != null && (tLRPC$MessageFwdHeader2 = tLRPC$Message3.fwd_from) != null) {
            fromChatId = DialogObject.getPeerDialogId(tLRPC$MessageFwdHeader2.saved_from_id);
            if (fromChatId == 0) {
                fromChatId = DialogObject.getPeerDialogId(this.message.messageOwner.fwd_from.from_id);
            }
        }
        if (DialogObject.isUserDialog(fromChatId)) {
            tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(fromChatId));
            chat = null;
        } else {
            chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-fromChatId));
            tLRPC$User = null;
        }
        if (this.currentDialogId == clientUserId) {
            if (tLRPC$User != null) {
                return AndroidUtilities.removeDiacritics(UserObject.getFirstName(tLRPC$User).replace("\n", ""));
            }
            if (chat != null) {
                return AndroidUtilities.removeDiacritics(chat.title.replace("\n", ""));
            }
            return null;
        } else if (!this.message.isOutOwner() || tLRPC$User == null) {
            if (this.isSavedDialog || (messageObject = this.message) == null || (tLRPC$Message2 = messageObject.messageOwner) == null || !(tLRPC$Message2.from_id instanceof TLRPC$TL_peerUser) || (user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.message.messageOwner.from_id.user_id))) == null) {
                MessageObject messageObject3 = this.message;
                return (messageObject3 == null || (tLRPC$Message = messageObject3.messageOwner) == null || (tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) == null || (str2 = tLRPC$MessageFwdHeader.from_name) == null) ? tLRPC$User != null ? (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? UserObject.isDeleted(tLRPC$User) ? LocaleController.getString(R.string.HiddenName) : AndroidUtilities.removeDiacritics(ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name).replace("\n", "")) : AndroidUtilities.removeDiacritics(UserObject.getFirstName(tLRPC$User).replace("\n", "")) : (chat == null || (str = chat.title) == null) ? "DELETED" : AndroidUtilities.removeDiacritics(str.replace("\n", "")) : AndroidUtilities.removeDiacritics(str2);
            }
            return AndroidUtilities.removeDiacritics(UserObject.getFirstName(user).replace("\n", ""));
        } else {
            return LocaleController.getString(R.string.FromYou);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:171:0x0357, code lost:
        if (r20 != false) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x0359, code lost:
        r3 = applyThumbs(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0161, code lost:
        if (r20 != false) goto L63;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r16v0, types: [android.view.View, org.telegram.ui.Cells.DialogCell] */
    /* JADX WARN: Type inference failed for: r3v18, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r3v19, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r3v24, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r3v4, types: [android.text.SpannableStringBuilder, java.lang.CharSequence, android.text.Spannable] */
    /* JADX WARN: Type inference failed for: r3v46, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r3v47 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SpannableStringBuilder getMessageStringFormatted(int i, String str, CharSequence charSequence, boolean z) {
        ?? spannableStringBuilder;
        TLRPC$Message tLRPC$Message;
        CharSequence charSequence2;
        int i2;
        String formatPluralString;
        CharSequence charSequence3;
        SpannableStringBuilder valueOf;
        TLRPC$TL_forumTopic findTopic;
        MessageObject captionMessage = getCaptionMessage();
        MessageObject messageObject = this.message;
        CharSequence charSequence4 = messageObject != null ? messageObject.messageText : null;
        this.applyName = true;
        if (TextUtils.isEmpty(str)) {
            MessageObject messageObject2 = this.message;
            TLRPC$Message tLRPC$Message2 = messageObject2.messageOwner;
            if (tLRPC$Message2 instanceof TLRPC$TL_messageService) {
                CharSequence charSequence5 = messageObject2.messageTextShort;
                if (charSequence5 == null || ((tLRPC$Message2.action instanceof TLRPC$TL_messageActionTopicCreate) && this.isTopic)) {
                    charSequence5 = messageObject2.messageText;
                }
                if (MessageObject.isTopicActionMessage(messageObject2)) {
                    valueOf = formatInternal(i, charSequence5, charSequence);
                    if ((this.message.topicIconDrawable[0] instanceof ForumBubbleDrawable) && (findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-this.message.getDialogId(), MessageObject.getTopicId(this.currentAccount, this.message.messageOwner, true))) != null) {
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
                String str2 = !this.needEmoji ? "" : captionMessage.isVideo() ? "📹 " : captionMessage.isVoice() ? "🎤 " : captionMessage.isMusic() ? "🎧 " : captionMessage.isPhoto() ? "🖼 " : "📎 ";
                if (captionMessage.hasHighlightedWords() && !TextUtils.isEmpty(captionMessage.messageOwner.message)) {
                    CharSequence charSequence7 = captionMessage.messageTrimmedToHighlight;
                    int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 47);
                    if (this.hasNameInMessage) {
                        if (!TextUtils.isEmpty(charSequence)) {
                            measuredWidth = (int) (measuredWidth - this.currentMessagePaint.measureText(charSequence.toString()));
                        }
                        measuredWidth = (int) (measuredWidth - this.currentMessagePaint.measureText(": "));
                    }
                    if (measuredWidth > 0) {
                        charSequence7 = AndroidUtilities.ellipsizeCenterEnd(charSequence7, captionMessage.highlightedWords.get(0), measuredWidth, this.currentMessagePaint, NotificationCenter.walletSyncProgressChanged).toString();
                    }
                    return new SpannableStringBuilder(str2).append(charSequence7);
                }
                if (charSequence6.length() > 150) {
                    charSequence6 = charSequence6.subSequence(0, 150);
                }
                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(charSequence6);
                captionMessage.spoilLoginCode();
                MediaDataController.addTextStyleRuns(captionMessage.messageOwner.entities, charSequence6, spannableStringBuilder2, NotificationCenter.reactionsDidLoad);
                TLRPC$Message tLRPC$Message3 = captionMessage.messageOwner;
                if (tLRPC$Message3 != null) {
                    ArrayList arrayList = tLRPC$Message3.entities;
                    TextPaint textPaint = this.currentMessagePaint;
                    MediaDataController.addAnimatedEmojiSpans(arrayList, spannableStringBuilder2, textPaint != null ? textPaint.getFontMetricsInt() : null);
                }
                spannableStringBuilder = new SpannableStringBuilder(str2).append(AndroidUtilities.replaceNewLines(spannableStringBuilder2));
            } else if (tLRPC$Message2.media != null && !messageObject2.isMediaEmpty()) {
                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                int i3 = Theme.key_chats_attachMessage;
                MessageObject messageObject3 = this.message;
                TLRPC$MessageMedia tLRPC$MessageMedia = messageObject3.messageOwner.media;
                if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                    TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia;
                    TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities = tLRPC$TL_messageMediaPoll.poll.question;
                    if (tLRPC$TL_textWithEntities == null || tLRPC$TL_textWithEntities.entities == null) {
                        charSequence2 = String.format("📊 \u2068%s\u2069", tLRPC$TL_textWithEntities.text);
                    } else {
                        SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(tLRPC$TL_messageMediaPoll.poll.question.text.replace('\n', ' '));
                        TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities2 = tLRPC$TL_messageMediaPoll.poll.question;
                        MediaDataController.addTextStyleRuns(tLRPC$TL_textWithEntities2.entities, tLRPC$TL_textWithEntities2.text, spannableStringBuilder3);
                        MediaDataController.addAnimatedEmojiSpans(tLRPC$TL_messageMediaPoll.poll.question.entities, spannableStringBuilder3, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt());
                        charSequence2 = new SpannableStringBuilder("📊 \u2068").append((CharSequence) spannableStringBuilder3).append((CharSequence) "\u2069");
                    }
                } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                    charSequence2 = String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title);
                } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) {
                    charSequence2 = tLRPC$MessageMedia.title;
                } else if (messageObject3.type == 14) {
                    charSequence2 = String.format("🎧 \u2068%s - %s\u2069", messageObject3.getMusicAuthor(), this.message.getMusicTitle());
                } else {
                    if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) {
                        int size = ((TLRPC$TL_messageMediaPaidMedia) tLRPC$MessageMedia).extended_media.size();
                        if (this.hasVideoThumb) {
                            if (size > 1) {
                                formatPluralString = LocaleController.formatPluralString("Media", size, new Object[0]);
                                charSequence2 = StarsIntroActivity.replaceStars(LocaleController.formatString(R.string.AttachPaidMedia, formatPluralString));
                            } else {
                                i2 = R.string.AttachVideo;
                                formatPluralString = LocaleController.getString(i2);
                                charSequence2 = StarsIntroActivity.replaceStars(LocaleController.formatString(R.string.AttachPaidMedia, formatPluralString));
                            }
                        } else if (size > 1) {
                            formatPluralString = LocaleController.formatPluralString("Photos", size, new Object[0]);
                            charSequence2 = StarsIntroActivity.replaceStars(LocaleController.formatString(R.string.AttachPaidMedia, formatPluralString));
                        } else {
                            i2 = R.string.AttachPhoto;
                            formatPluralString = LocaleController.getString(i2);
                            charSequence2 = StarsIntroActivity.replaceStars(LocaleController.formatString(R.string.AttachPaidMedia, formatPluralString));
                        }
                    } else if (this.thumbsCount <= 1) {
                        charSequence2 = charSequence4.toString();
                    } else if (this.hasVideoThumb) {
                        ArrayList arrayList2 = this.groupMessages;
                        charSequence2 = LocaleController.formatPluralString("Media", arrayList2 == null ? 0 : arrayList2.size(), new Object[0]);
                    } else {
                        ArrayList arrayList3 = this.groupMessages;
                        charSequence2 = LocaleController.formatPluralString("Photos", arrayList3 == null ? 0 : arrayList3.size(), new Object[0]);
                    }
                    i3 = Theme.key_chats_actionMessage;
                }
                if (charSequence2 instanceof String) {
                    charSequence2 = ((String) charSequence2).replace('\n', ' ');
                }
                if (z) {
                    charSequence2 = applyThumbs(charSequence2);
                }
                SpannableStringBuilder formatInternal = formatInternal(i, charSequence2, charSequence);
                if (!isForumCell()) {
                    try {
                        formatInternal.setSpan(new ForegroundColorSpanThemable(i3, this.resourcesProvider), this.hasNameInMessage ? charSequence.length() + 2 : 0, formatInternal.length(), 33);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                return formatInternal;
            } else {
                MessageObject messageObject4 = this.message;
                CharSequence charSequence8 = messageObject4.messageOwner.message;
                if (charSequence8 == null) {
                    return new SpannableStringBuilder();
                }
                if (messageObject4.hasHighlightedWords()) {
                    CharSequence charSequence9 = this.message.messageTrimmedToHighlight;
                    if (charSequence9 != null) {
                        charSequence8 = charSequence9;
                    }
                    int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 33);
                    if (this.hasNameInMessage) {
                        if (!TextUtils.isEmpty(charSequence)) {
                            measuredWidth2 = (int) (measuredWidth2 - this.currentMessagePaint.measureText(charSequence.toString()));
                        }
                        measuredWidth2 = (int) (measuredWidth2 - this.currentMessagePaint.measureText(": "));
                    }
                    if (measuredWidth2 > 0) {
                        charSequence8 = AndroidUtilities.ellipsizeCenterEnd(charSequence8, this.message.highlightedWords.get(0), measuredWidth2, this.currentMessagePaint, NotificationCenter.walletSyncProgressChanged).toString();
                    }
                } else {
                    if (charSequence8.length() > 150) {
                        charSequence8 = charSequence8.subSequence(0, 150);
                    }
                    charSequence8 = AndroidUtilities.replaceNewLines(charSequence8);
                }
                spannableStringBuilder = new SpannableStringBuilder(charSequence8);
                MessageObject messageObject5 = this.message;
                if (messageObject5 != null) {
                    messageObject5.spoilLoginCode();
                }
                MediaDataController.addTextStyleRuns(this.message, (Spannable) spannableStringBuilder, (int) NotificationCenter.reactionsDidLoad);
                MessageObject messageObject6 = this.message;
                if (messageObject6 != null && (tLRPC$Message = messageObject6.messageOwner) != null) {
                    ArrayList arrayList4 = tLRPC$Message.entities;
                    TextPaint textPaint2 = this.currentMessagePaint;
                    MediaDataController.addAnimatedEmojiSpans(arrayList4, spannableStringBuilder, textPaint2 != null ? textPaint2.getFontMetricsInt() : null);
                }
            }
        } else {
            spannableStringBuilder = str;
        }
        return formatInternal(i, spannableStringBuilder, charSequence);
    }

    @Override // android.view.View
    public float getTranslationX() {
        return this.translationX;
    }

    @Override // org.telegram.ui.Cells.BaseCell, android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public boolean hasTags() {
        DialogCellTags dialogCellTags = this.tags;
        return (dialogCellTags == null || dialogCellTags.isEmpty()) ? false : true;
    }

    @Override // org.telegram.ui.Cells.BaseCell, android.view.View
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

    @Override // android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        if (drawable == this.translationDrawable || drawable == Theme.dialogs_archiveAvatarDrawable) {
            invalidate(drawable.getBounds());
        } else {
            super.invalidateDrawable(drawable);
        }
    }

    public boolean isBlocked() {
        return this.premiumBlocked;
    }

    public boolean isDialogFolder() {
        return this.currentDialogFolderId > 0;
    }

    public boolean isFolderCell() {
        return this.currentDialogFolderId != 0;
    }

    public boolean isForumCell() {
        TLRPC$Chat tLRPC$Chat;
        return (isDialogFolder() || (tLRPC$Chat = this.chat) == null || !tLRPC$Chat.forum || this.isTopic) ? false : true;
    }

    public boolean isMoving() {
        return this.moving;
    }

    public boolean isPointInsideAvatar(float f, float f2) {
        return !LocaleController.isRTL ? f >= 0.0f && f < ((float) AndroidUtilities.dp(60.0f)) : f >= ((float) (getMeasuredWidth() - AndroidUtilities.dp(60.0f))) && f < ((float) getMeasuredWidth());
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
    /* JADX WARN: Code restructure failed: missing block: B:263:0x0847, code lost:
        if (r1.type != 2) goto L833;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x0bce, code lost:
        if (r3.lastKnownTypingType >= 0) goto L209;
     */
    /* JADX WARN: Code restructure failed: missing block: B:937:0x1795, code lost:
        if (r57.avatarDrawable.getAvatarType() == 2) goto L561;
     */
    /* JADX WARN: Code restructure failed: missing block: B:945:0x17af, code lost:
        if (r57.avatarDrawable.getAvatarType() == 2) goto L561;
     */
    /* JADX WARN: Code restructure failed: missing block: B:946:0x17b1, code lost:
        r57.avatarDrawable.setArchivedAvatarHiddenProgress(org.telegram.ui.Components.CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(r57.archiveBackgroundProgress));
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:168:0x059f  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x05b1  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x0a00  */
    /* JADX WARN: Removed duplicated region for block: B:362:0x0b46  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x0b51  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x0b75  */
    /* JADX WARN: Removed duplicated region for block: B:375:0x0b78  */
    /* JADX WARN: Removed duplicated region for block: B:379:0x0b8a  */
    /* JADX WARN: Removed duplicated region for block: B:384:0x0bc0  */
    /* JADX WARN: Removed duplicated region for block: B:422:0x0c6c  */
    /* JADX WARN: Removed duplicated region for block: B:425:0x0c72  */
    /* JADX WARN: Removed duplicated region for block: B:487:0x0e7d  */
    /* JADX WARN: Removed duplicated region for block: B:490:0x0e83  */
    /* JADX WARN: Removed duplicated region for block: B:544:0x0f2f  */
    /* JADX WARN: Removed duplicated region for block: B:545:0x0f31  */
    /* JADX WARN: Removed duplicated region for block: B:548:0x0f37  */
    /* JADX WARN: Removed duplicated region for block: B:549:0x0f39  */
    /* JADX WARN: Removed duplicated region for block: B:551:0x0f3e  */
    /* JADX WARN: Removed duplicated region for block: B:554:0x0f47  */
    /* JADX WARN: Removed duplicated region for block: B:561:0x0f53 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:585:0x0f96  */
    /* JADX WARN: Removed duplicated region for block: B:593:0x0faa  */
    /* JADX WARN: Removed duplicated region for block: B:596:0x0fb5  */
    /* JADX WARN: Removed duplicated region for block: B:605:0x0fcc  */
    /* JADX WARN: Removed duplicated region for block: B:609:0x0fe4  */
    /* JADX WARN: Removed duplicated region for block: B:615:0x1036  */
    /* JADX WARN: Removed duplicated region for block: B:622:0x1048  */
    /* JADX WARN: Removed duplicated region for block: B:638:0x1094  */
    /* JADX WARN: Removed duplicated region for block: B:694:0x116c  */
    /* JADX WARN: Removed duplicated region for block: B:699:0x1192  */
    /* JADX WARN: Removed duplicated region for block: B:702:0x11dd  */
    /* JADX WARN: Removed duplicated region for block: B:735:0x1250  */
    /* JADX WARN: Removed duplicated region for block: B:751:0x1329  */
    /* JADX WARN: Removed duplicated region for block: B:759:0x137e  */
    /* JADX WARN: Removed duplicated region for block: B:797:0x150e  */
    /* JADX WARN: Removed duplicated region for block: B:802:0x153e  */
    /* JADX WARN: Removed duplicated region for block: B:804:0x1544  */
    /* JADX WARN: Removed duplicated region for block: B:807:0x154f  */
    /* JADX WARN: Removed duplicated region for block: B:808:0x156e  */
    /* JADX WARN: Removed duplicated region for block: B:811:0x1574  */
    /* JADX WARN: Removed duplicated region for block: B:823:0x1590  */
    /* JADX WARN: Removed duplicated region for block: B:824:0x1592  */
    /* JADX WARN: Removed duplicated region for block: B:828:0x15a0  */
    /* JADX WARN: Removed duplicated region for block: B:831:0x15ab  */
    /* JADX WARN: Removed duplicated region for block: B:837:0x15ba  */
    /* JADX WARN: Removed duplicated region for block: B:841:0x15c2  */
    /* JADX WARN: Removed duplicated region for block: B:843:0x15c6  */
    /* JADX WARN: Removed duplicated region for block: B:857:0x162f  */
    /* JADX WARN: Removed duplicated region for block: B:860:0x1638  */
    /* JADX WARN: Removed duplicated region for block: B:863:0x163f  */
    /* JADX WARN: Removed duplicated region for block: B:878:0x1681  */
    /* JADX WARN: Removed duplicated region for block: B:908:0x16fe  */
    /* JADX WARN: Removed duplicated region for block: B:914:0x174e  */
    /* JADX WARN: Removed duplicated region for block: B:917:0x1756  */
    /* JADX WARN: Removed duplicated region for block: B:922:0x1767  */
    /* JADX WARN: Removed duplicated region for block: B:931:0x177e  */
    /* JADX WARN: Removed duplicated region for block: B:939:0x1798  */
    /* JADX WARN: Removed duplicated region for block: B:950:0x17c3  */
    /* JADX WARN: Removed duplicated region for block: B:956:0x17d6  */
    /* JADX WARN: Removed duplicated region for block: B:966:0x17f8  */
    /* JADX WARN: Removed duplicated region for block: B:976:0x1814  */
    /* JADX WARN: Removed duplicated region for block: B:997:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v2, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r10v58 */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        int color;
        int color2;
        int i;
        String string;
        RLottieDrawable rLottieDrawable;
        float f;
        float f2;
        int i2;
        float f3;
        String str;
        int i3;
        boolean z;
        boolean z2;
        RLottieDrawable rLottieDrawable2;
        int i4;
        float f4;
        Paint paint;
        int color3;
        int i5;
        float f5;
        Canvas canvas2;
        ?? r10;
        int i6;
        float f6;
        float f7;
        boolean z3;
        float f8;
        boolean z4;
        boolean z5;
        float f9;
        float measuredWidth;
        Paint paint2;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic;
        boolean z6;
        boolean z7;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic2;
        PullForegroundDrawable pullForegroundDrawable;
        float f10;
        int i7;
        Paint paint3;
        float alpha;
        float f11;
        float f12;
        boolean z8;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        float f13;
        int i13;
        int i14;
        Drawable drawable;
        boolean z9;
        boolean z10;
        int dp;
        float f14;
        int dp2;
        Drawable drawable2;
        int i15;
        boolean z11;
        DialogCellTags dialogCellTags;
        int i16;
        char c;
        int i17;
        int i18;
        Drawable drawable3;
        boolean z12;
        float f15;
        DialogCell dialogCell;
        Canvas canvas3;
        StaticLayout staticLayout;
        int i19;
        TextPaint textPaint;
        int i20;
        float f16;
        float f17;
        StaticLayout staticLayout2;
        int color4;
        StaticLayout staticLayout3;
        TextPaint textPaint2;
        int i21;
        StaticLayout staticLayout4;
        TextPaint textPaint3;
        int i22;
        float dp3;
        float measuredHeight;
        Paint paint4;
        float f18;
        float f19;
        Canvas canvas4;
        Paint paint5;
        PorterDuffXfermode porterDuffXfermode;
        int i23;
        Paint paint6;
        PullForegroundDrawable pullForegroundDrawable2;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic3;
        float f20 = 12.5f;
        float f21 = 12.0f;
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
        if (this.translationX == 0.0f && this.cornerProgress == 0.0f) {
            RLottieDrawable rLottieDrawable3 = this.translationDrawable;
            if (rLottieDrawable3 != null) {
                rLottieDrawable3.stop();
                this.translationDrawable.setProgress(0.0f);
                this.translationDrawable.setCallback(null);
                this.translationDrawable = null;
                this.translationAnimationStarted = false;
            }
            i4 = 1;
        } else {
            canvas.save();
            canvas.translate(0.0f, -this.translateY);
            if (this.overrideSwipeAction) {
                color = Theme.getColor(this.overrideSwipeActionBackgroundColorKey, this.resourcesProvider);
                color2 = Theme.getColor(this.overrideSwipeActionRevealBackgroundColorKey, this.resourcesProvider);
                String str2 = this.overrideSwipeActionStringKey;
                i = this.overrideSwipeActionStringId;
                string = LocaleController.getString(str2, i);
                rLottieDrawable = this.overrideSwipeActionDrawable;
            } else if (this.currentDialogFolderId != 0) {
                if (this.archiveHidden) {
                    color = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                    color2 = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                    i = R.string.UnhideFromTop;
                    string = LocaleController.getString("UnhideFromTop", i);
                    rLottieDrawable = Theme.dialogs_unpinArchiveDrawable;
                } else {
                    color = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                    color2 = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                    i = R.string.HideOnTop;
                    string = LocaleController.getString("HideOnTop", i);
                    rLottieDrawable = Theme.dialogs_pinArchiveDrawable;
                }
            } else if (this.promoDialog) {
                color = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                color2 = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                i = R.string.PsaHide;
                string = LocaleController.getString("PsaHide", i);
                rLottieDrawable = Theme.dialogs_hidePsaDrawable;
            } else if (this.folderId == 0) {
                color = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                color2 = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                if (SharedConfig.getChatSwipeAction(this.currentAccount) == 3) {
                    if (this.dialogMuted) {
                        i = R.string.SwipeUnmute;
                        string = LocaleController.getString("SwipeUnmute", i);
                        rLottieDrawable = Theme.dialogs_swipeUnmuteDrawable;
                    } else {
                        i = R.string.SwipeMute;
                        string = LocaleController.getString("SwipeMute", i);
                        rLottieDrawable = Theme.dialogs_swipeMuteDrawable;
                    }
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 4) {
                    i = R.string.SwipeDeleteChat;
                    string = LocaleController.getString("SwipeDeleteChat", i);
                    color = Theme.getColor(Theme.key_dialogSwipeRemove, this.resourcesProvider);
                    rLottieDrawable = Theme.dialogs_swipeDeleteDrawable;
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 1) {
                    if (this.unreadCount > 0 || this.markUnread) {
                        i = R.string.SwipeMarkAsRead;
                        string = LocaleController.getString("SwipeMarkAsRead", i);
                        rLottieDrawable = Theme.dialogs_swipeReadDrawable;
                    } else {
                        i = R.string.SwipeMarkAsUnread;
                        string = LocaleController.getString("SwipeMarkAsUnread", i);
                        rLottieDrawable = Theme.dialogs_swipeUnreadDrawable;
                    }
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) != 0) {
                    i = R.string.Archive;
                    string = LocaleController.getString("Archive", i);
                    rLottieDrawable = Theme.dialogs_archiveDrawable;
                } else if (getIsPinned()) {
                    i = R.string.SwipeUnpin;
                    string = LocaleController.getString("SwipeUnpin", i);
                    rLottieDrawable = Theme.dialogs_swipeUnpinDrawable;
                } else {
                    i = R.string.SwipePin;
                    string = LocaleController.getString("SwipePin", i);
                    rLottieDrawable = Theme.dialogs_swipePinDrawable;
                }
            } else {
                color = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                color2 = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                i = R.string.Unarchive;
                string = LocaleController.getString("Unarchive", i);
                rLottieDrawable = Theme.dialogs_unarchiveDrawable;
            }
            RLottieDrawable rLottieDrawable4 = rLottieDrawable;
            int i24 = color2;
            int i25 = i;
            String str3 = string;
            int i26 = i25;
            this.translationDrawable = rLottieDrawable4;
            if (!this.swipeCanceled || (rLottieDrawable2 = this.lastDrawTranslationDrawable) == null) {
                this.lastDrawTranslationDrawable = rLottieDrawable4;
                this.lastDrawSwipeMessageStringId = i26;
            } else {
                this.translationDrawable = rLottieDrawable2;
                i26 = this.lastDrawSwipeMessageStringId;
            }
            int i27 = i26;
            if (!this.translationAnimationStarted && Math.abs(this.translationX) > AndroidUtilities.dp(43.0f)) {
                this.translationAnimationStarted = true;
                this.translationDrawable.setProgress(0.0f);
                this.translationDrawable.setCallback(this);
                this.translationDrawable.start();
            }
            float measuredWidth2 = this.translationX + getMeasuredWidth();
            if (this.currentRevealProgress < 1.0f) {
                Theme.dialogs_pinnedPaint.setColor(color);
                f2 = 0.0f;
                f = measuredWidth2;
                canvas.drawRect(measuredWidth2 - AndroidUtilities.dp(8.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
                if (this.currentRevealProgress == 0.0f) {
                    if (Theme.dialogs_archiveDrawableRecolored) {
                        Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor(Theme.key_chats_archiveBackground));
                        z2 = false;
                        Theme.dialogs_archiveDrawableRecolored = false;
                    } else {
                        z2 = false;
                    }
                    if (Theme.dialogs_hidePsaDrawableRecolored) {
                        Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                        RLottieDrawable rLottieDrawable5 = Theme.dialogs_hidePsaDrawable;
                        int i28 = Theme.key_chats_archiveBackground;
                        rLottieDrawable5.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(i28));
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(i28));
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(i28));
                        Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                        Theme.dialogs_hidePsaDrawableRecolored = z2;
                    }
                }
            } else {
                f = measuredWidth2;
                f2 = 0.0f;
            }
            int measuredWidth3 = (getMeasuredWidth() - AndroidUtilities.dp(43.0f)) - (this.translationDrawable.getIntrinsicWidth() / 2);
            int measuredHeight2 = (getMeasuredHeight() - AndroidUtilities.dp(54.0f)) / 2;
            int intrinsicWidth = (this.translationDrawable.getIntrinsicWidth() / 2) + measuredWidth3;
            int intrinsicHeight = (this.translationDrawable.getIntrinsicHeight() / 2) + measuredHeight2;
            if (this.currentRevealProgress > f2) {
                canvas.save();
                f3 = f;
                i2 = i27;
                str = str3;
                canvas.clipRect(f3 - AndroidUtilities.dp(8.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                Theme.dialogs_pinnedPaint.setColor(i24);
                canvas.drawCircle(intrinsicWidth, intrinsicHeight, ((float) Math.sqrt((intrinsicWidth * intrinsicWidth) + ((intrinsicHeight - getMeasuredHeight()) * (intrinsicHeight - getMeasuredHeight())))) * AndroidUtilities.accelerateInterpolator.getInterpolation(this.currentRevealProgress), Theme.dialogs_pinnedPaint);
                canvas.restore();
                if (Theme.dialogs_archiveDrawableRecolored) {
                    z = 1;
                } else {
                    Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor(Theme.key_chats_archivePinBackground));
                    z = 1;
                    Theme.dialogs_archiveDrawableRecolored = true;
                }
                i3 = z;
                if (!Theme.dialogs_hidePsaDrawableRecolored) {
                    Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                    RLottieDrawable rLottieDrawable6 = Theme.dialogs_hidePsaDrawable;
                    int i29 = Theme.key_chats_archivePinBackground;
                    rLottieDrawable6.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(i29));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(i29));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(i29));
                    Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                    Theme.dialogs_hidePsaDrawableRecolored = z;
                    i3 = z;
                }
            } else {
                i2 = i27;
                f3 = f;
                str = str3;
                i3 = 1;
            }
            canvas.save();
            canvas.translate(measuredWidth3, measuredHeight2);
            float f22 = this.currentRevealBounceProgress;
            if (f22 != 0.0f && f22 != 1.0f) {
                float interpolation = this.interpolator.getInterpolation(f22) + 1.0f;
                canvas.scale(interpolation, interpolation, this.translationDrawable.getIntrinsicWidth() / 2, this.translationDrawable.getIntrinsicHeight() / 2);
            }
            BaseCell.setDrawableBounds((Drawable) this.translationDrawable, 0, 0);
            this.translationDrawable.draw(canvas);
            canvas.restore();
            canvas.clipRect(f3, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            String str4 = str;
            int ceil = (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str4));
            int i30 = i2;
            if (this.swipeMessageTextId != i30 || this.swipeMessageWidth != getMeasuredWidth()) {
                this.swipeMessageTextId = i30;
                this.swipeMessageWidth = getMeasuredWidth();
                TextPaint textPaint4 = Theme.dialogs_archiveTextPaint;
                int min = Math.min(AndroidUtilities.dp(80.0f), ceil);
                Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
                StaticLayout staticLayout5 = new StaticLayout(str4, textPaint4, min, alignment, 1.0f, 0.0f, false);
                this.swipeMessageTextLayout = staticLayout5;
                if (staticLayout5.getLineCount() > i3) {
                    this.swipeMessageTextLayout = new StaticLayout(str4, Theme.dialogs_archiveTextPaintSmall, Math.min(AndroidUtilities.dp(82.0f), ceil), alignment, 1.0f, 0.0f, false);
                }
            }
            if (this.swipeMessageTextLayout != null) {
                canvas.save();
                canvas.translate((getMeasuredWidth() - AndroidUtilities.dp(43.0f)) - (this.swipeMessageTextLayout.getWidth() / 2.0f), measuredHeight2 + AndroidUtilities.dp(38.0f) + (this.swipeMessageTextLayout.getLineCount() > i3 ? -AndroidUtilities.dp(4.0f) : 0.0f));
                this.swipeMessageTextLayout.draw(canvas);
                canvas.restore();
            }
            canvas.restore();
            i4 = i3;
        }
        if (this.translationX != 0.0f) {
            canvas.save();
            canvas.translate(this.translationX, 0.0f);
        }
        float dp4 = AndroidUtilities.dp(8.0f) * this.cornerProgress;
        if (this.isSelected) {
            f4 = 0.0f;
            this.rect.set(0.0f, 0.0f, getMeasuredWidth(), AndroidUtilities.lerp(getMeasuredHeight(), getCollapsedHeight(), this.rightFragmentOpenedProgress));
            this.rect.offset(0.0f, (-this.translateY) + this.collapseOffset);
            canvas.drawRoundRect(this.rect, dp4, dp4, Theme.dialogs_tabletSeletedPaint);
        } else {
            f4 = 0.0f;
        }
        canvas.save();
        canvas.translate(f4, (-this.rightFragmentOffset) * this.rightFragmentOpenedProgress);
        if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != f4)) {
            paint = Theme.dialogs_pinnedPaint;
            color3 = AndroidUtilities.getOffsetColor(0, Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider), this.archiveBackgroundProgress, 1.0f);
        } else if (getIsPinned() || this.drawPinBackground) {
            paint = Theme.dialogs_pinnedPaint;
            color3 = Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider);
        } else {
            i5 = 0;
            canvas.restore();
            this.updateHelper.updateAnimationValues();
            if (this.collapseOffset != 0.0f) {
                canvas.save();
                canvas.translate(0.0f, this.collapseOffset);
            }
            f5 = this.rightFragmentOpenedProgress;
            if (f5 == 1.0f) {
                if (f5 != 0.0f) {
                    float clamp = Utilities.clamp(f5 / 0.4f, 1.0f, 0.0f);
                    if (SharedConfig.getDevicePerformanceClass() >= 2) {
                        i23 = canvas.saveLayerAlpha(AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize() + i4) - (AndroidUtilities.dp(8.0f) * (1.0f - clamp)), 0.0f, getMeasuredWidth(), getMeasuredHeight(), (int) ((1.0f - this.rightFragmentOpenedProgress) * 255.0f), 31);
                    } else {
                        int save = canvas.save();
                        canvas.clipRect(AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize() + i4) - (AndroidUtilities.dp(8.0f) * (1.0f - clamp)), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                        i23 = save;
                    }
                    f10 = 0.0f;
                    canvas.translate((-(getMeasuredWidth() - AndroidUtilities.dp(74.0f))) * 0.7f * this.rightFragmentOpenedProgress, 0.0f);
                    i7 = i23;
                } else {
                    f10 = 0.0f;
                    i7 = -1;
                }
                if (this.translationX != f10 || this.cornerProgress != f10) {
                    canvas.save();
                    Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                    this.rect.set(getMeasuredWidth() - AndroidUtilities.dp(64.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                    this.rect.offset(0.0f, -this.translateY);
                    canvas.drawRoundRect(this.rect, dp4, dp4, Theme.dialogs_pinnedPaint);
                    if (this.isSelected) {
                        canvas.drawRoundRect(this.rect, dp4, dp4, Theme.dialogs_tabletSeletedPaint);
                    }
                    if (this.currentDialogFolderId == 0 || (SharedConfig.archiveHidden && this.archiveBackgroundProgress == 0.0f)) {
                        if (getIsPinned() || this.drawPinBackground) {
                            Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider));
                            paint3 = Theme.dialogs_pinnedPaint;
                            alpha = paint3.getAlpha();
                            f11 = this.rightFragmentOpenedProgress;
                            f12 = 1.0f;
                        }
                        canvas.restore();
                    } else {
                        f12 = 1.0f;
                        Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(i5, Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider), this.archiveBackgroundProgress, 1.0f));
                        paint3 = Theme.dialogs_pinnedPaint;
                        alpha = paint3.getAlpha();
                        f11 = this.rightFragmentOpenedProgress;
                    }
                    paint3.setAlpha((int) (alpha * (f12 - f11)));
                    canvas.drawRoundRect(this.rect, dp4, dp4, Theme.dialogs_pinnedPaint);
                    canvas.restore();
                }
                if (this.translationX != 0.0f) {
                    float f23 = this.cornerProgress;
                    if (f23 < 1.0f) {
                        float f24 = f23 + 0.10666667f;
                        this.cornerProgress = f24;
                        if (f24 > 1.0f) {
                            this.cornerProgress = 1.0f;
                        }
                        z8 = true;
                    } else {
                        z8 = false;
                    }
                } else {
                    float f25 = this.cornerProgress;
                    if (f25 > 0.0f) {
                        float f26 = f25 - 0.10666667f;
                        this.cornerProgress = f26;
                        if (f26 < 0.0f) {
                            this.cornerProgress = 0.0f;
                        }
                        z8 = true;
                    }
                    z8 = false;
                }
                if (this.drawNameLock) {
                    BaseCell.setDrawableBounds(Theme.dialogs_lockDrawable, this.nameLockLeft, this.nameLockTop);
                    Theme.dialogs_lockDrawable.draw(canvas);
                }
                int dp5 = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 10.0f : 13.0f);
                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                    dp5 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
                }
                if (this.nameLayout != null) {
                    if (this.nameLayoutEllipsizeByGradient && !this.nameLayoutFits) {
                        if (this.nameLayoutEllipsizeLeft && this.fadePaint == null) {
                            Paint paint7 = new Paint();
                            this.fadePaint = paint7;
                            paint7.setShader(new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(24.0f), 0.0f, new int[]{-1, i5}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                            paint5 = this.fadePaint;
                            porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
                        } else {
                            if (this.fadePaintBack == null) {
                                Paint paint8 = new Paint();
                                this.fadePaintBack = paint8;
                                paint8.setShader(new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(24.0f), 0.0f, new int[]{i5, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                                paint5 = this.fadePaintBack;
                                porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
                            }
                            canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), NotificationCenter.didClearDatabase, 31);
                            int i31 = this.nameLeft;
                            canvas.clipRect(i31, i5, this.nameWidth + i31, getMeasuredHeight());
                        }
                        paint5.setXfermode(porterDuffXfermode);
                        canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), NotificationCenter.didClearDatabase, 31);
                        int i312 = this.nameLeft;
                        canvas.clipRect(i312, i5, this.nameWidth + i312, getMeasuredHeight());
                    }
                    if (this.currentDialogFolderId != 0) {
                        TextPaint textPaint5 = Theme.dialogs_namePaint[this.paintIndex];
                        int color5 = Theme.getColor(Theme.key_chats_nameArchived, this.resourcesProvider);
                        textPaint5.linkColor = color5;
                        textPaint5.setColor(color5);
                    } else {
                        if (this.encryptedChat == null) {
                            CustomDialog customDialog = this.customDialog;
                            if (customDialog != null) {
                            }
                            textPaint3 = Theme.dialogs_namePaint[this.paintIndex];
                            i22 = Theme.key_chats_name;
                            int color6 = Theme.getColor(i22, this.resourcesProvider);
                            textPaint3.linkColor = color6;
                            textPaint3.setColor(color6);
                        }
                        textPaint3 = Theme.dialogs_namePaint[this.paintIndex];
                        i22 = Theme.key_chats_secretName;
                        int color62 = Theme.getColor(i22, this.resourcesProvider);
                        textPaint3.linkColor = color62;
                        textPaint3.setColor(color62);
                    }
                    canvas.save();
                    canvas.translate(this.nameLeft + this.nameLayoutTranslateX, dp5);
                    SpoilerEffect.layoutDrawMaybe(this.nameLayout, canvas);
                    StaticLayout staticLayout6 = this.nameLayout;
                    i8 = i7;
                    i11 = -1;
                    i9 = 1;
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout6, this.animatedEmojiStackName, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i5, staticLayout6.getPaint().getColor()));
                    canvas.restore();
                    if (!this.nameLayoutEllipsizeByGradient || this.nameLayoutFits) {
                        i10 = 2;
                    } else {
                        canvas.save();
                        if (this.nameLayoutEllipsizeLeft) {
                            canvas.translate(this.nameLeft, 0.0f);
                            dp3 = AndroidUtilities.dp(24.0f);
                            measuredHeight = getMeasuredHeight();
                            canvas4 = canvas;
                            i10 = 2;
                            f18 = 0.0f;
                            f19 = 0.0f;
                            paint4 = this.fadePaint;
                        } else {
                            i10 = 2;
                            canvas.translate((this.nameLeft + this.nameWidth) - AndroidUtilities.dp(24.0f), 0.0f);
                            dp3 = AndroidUtilities.dp(24.0f);
                            measuredHeight = getMeasuredHeight();
                            paint4 = this.fadePaintBack;
                            f18 = 0.0f;
                            f19 = 0.0f;
                            canvas4 = canvas;
                        }
                        canvas4.drawRect(f18, f19, dp3, measuredHeight, paint4);
                        canvas.restore();
                        canvas.restore();
                    }
                } else {
                    i8 = i7;
                    i9 = 1;
                    i10 = 2;
                    i11 = -1;
                }
                if (this.timeLayout != null && this.currentDialogFolderId == 0) {
                    canvas.save();
                    canvas.translate(this.timeLeft, this.timeTop);
                    SpoilerEffect.layoutDrawMaybe(this.timeLayout, canvas);
                    canvas.restore();
                }
                if (drawLock2()) {
                    Theme.dialogs_lock2Drawable.setBounds(this.lock2Left, this.timeTop + ((this.timeLayout.getHeight() - Theme.dialogs_lock2Drawable.getIntrinsicHeight()) / i10), this.lock2Left + Theme.dialogs_lock2Drawable.getIntrinsicWidth(), this.timeTop + ((this.timeLayout.getHeight() - Theme.dialogs_lock2Drawable.getIntrinsicHeight()) / i10) + Theme.dialogs_lock2Drawable.getIntrinsicHeight());
                    Theme.dialogs_lock2Drawable.draw(canvas);
                }
                if (this.messageNameLayout == null || isForumCell()) {
                    i12 = 4;
                    f13 = 0.0f;
                    i13 = 2;
                } else {
                    if (this.currentDialogFolderId != 0) {
                        textPaint2 = Theme.dialogs_messageNamePaint;
                        i21 = Theme.key_chats_nameMessageArchived_threeLines;
                    } else if (this.draftMessage != null) {
                        textPaint2 = Theme.dialogs_messageNamePaint;
                        i21 = Theme.key_chats_draft;
                    } else {
                        textPaint2 = Theme.dialogs_messageNamePaint;
                        i21 = Theme.key_chats_nameMessage_threeLines;
                    }
                    int color7 = Theme.getColor(i21, this.resourcesProvider);
                    textPaint2.linkColor = color7;
                    textPaint2.setColor(color7);
                    canvas.save();
                    canvas.translate(this.messageNameLeft, this.messageNameTop);
                    try {
                        SpoilerEffect.layoutDrawMaybe(this.messageNameLayout, canvas);
                        staticLayout4 = this.messageNameLayout;
                        i13 = 2;
                        f13 = 0.0f;
                        i12 = 4;
                    } catch (Exception e) {
                        e = e;
                        i12 = 4;
                        f13 = 0.0f;
                        i13 = 2;
                    }
                    try {
                        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout4, this.animatedEmojiStack2, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i9, staticLayout4.getPaint().getColor()));
                    } catch (Exception e2) {
                        e = e2;
                        FileLog.e(e);
                        canvas.restore();
                        if (this.messageLayout != null) {
                        }
                        if (this.buttonLayout == null) {
                        }
                        if (this.currentDialogFolderId == 0) {
                        }
                        if (this.drawUnmute) {
                        }
                        if (this.dialogsType != i14) {
                        }
                        if (this.drawVerified) {
                        }
                        drawable.draw(canvas);
                        if (!this.drawReorder) {
                        }
                        Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                        BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                        Theme.dialogs_reorderDrawable.draw(canvas);
                        if (!this.drawError) {
                        }
                        drawable2.draw(canvas);
                        canvas2 = canvas;
                        if (this.thumbsCount > 0) {
                        }
                        i15 = -1;
                        z11 = false;
                        dialogCellTags = this.tags;
                        if (dialogCellTags != null) {
                        }
                        i16 = i8;
                        if (i16 != i15) {
                        }
                        z3 = z8;
                        r10 = z11;
                        if (this.animatingArchiveAvatar) {
                        }
                        if (this.drawAvatar) {
                        }
                        if (this.animatingArchiveAvatar) {
                        }
                        if (this.avatarImage.getVisible()) {
                        }
                        if (this.rightFragmentOpenedProgress > f7) {
                        }
                        if (this.collapseOffset != f7) {
                        }
                        if (this.translationX != f7) {
                        }
                        if (this.drawArchive) {
                        }
                        if (this.useSeparator) {
                        }
                        if (this.clipProgress != f7) {
                        }
                        z4 = this.drawReorder;
                        if (!z4) {
                        }
                        if (z4) {
                        }
                        if (this.archiveHidden) {
                        }
                        z5 = true;
                        if (this.animatingArchiveAvatar) {
                        }
                        if (this.drawRevealBackground) {
                        }
                    }
                    canvas.restore();
                }
                if (this.messageLayout != null) {
                    if (this.currentDialogFolderId == 0) {
                        textPaint = Theme.dialogs_messagePaint[this.paintIndex];
                        i20 = Theme.key_chats_message;
                    } else if (this.chat != null) {
                        textPaint = Theme.dialogs_messagePaint[this.paintIndex];
                        i20 = Theme.key_chats_nameMessageArchived;
                    } else {
                        textPaint = Theme.dialogs_messagePaint[this.paintIndex];
                        i20 = Theme.key_chats_messageArchived;
                    }
                    int color8 = Theme.getColor(i20, this.resourcesProvider);
                    textPaint.linkColor = color8;
                    textPaint.setColor(color8);
                    float dp6 = AndroidUtilities.dp(14.0f);
                    DialogUpdateHelper dialogUpdateHelper = this.updateHelper;
                    float f27 = dialogUpdateHelper.typingOutToTop ? this.messageTop - (dialogUpdateHelper.typingProgres * dp6) : this.messageTop + (dialogUpdateHelper.typingProgres * dp6);
                    if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                        f27 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                    }
                    if (this.updateHelper.typingProgres != 1.0f) {
                        canvas.save();
                        canvas.translate(this.messageLeft, f27);
                        int alpha2 = this.messageLayout.getPaint().getAlpha();
                        this.messageLayout.getPaint().setAlpha((int) (alpha2 * (1.0f - this.updateHelper.typingProgres)));
                        if (this.spoilers.isEmpty()) {
                            f16 = 1.0f;
                            SpoilerEffect.layoutDrawMaybe(this.messageLayout, canvas);
                            StaticLayout staticLayout7 = this.messageLayout;
                            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout7, this.animatedEmojiStack, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i13, staticLayout7.getPaint().getColor()));
                        } else {
                            try {
                                canvas.save();
                                SpoilerEffect.clipOutCanvas(canvas, this.spoilers);
                                SpoilerEffect.layoutDrawMaybe(this.messageLayout, canvas);
                                staticLayout3 = this.messageLayout;
                                f16 = 1.0f;
                            } catch (Exception e3) {
                                e = e3;
                                f16 = 1.0f;
                            }
                            try {
                                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout3, this.animatedEmojiStack, -0.075f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i13, staticLayout3.getPaint().getColor()));
                                canvas.restore();
                                for (int i32 = 0; i32 < this.spoilers.size(); i32 += i9) {
                                    SpoilerEffect spoilerEffect = (SpoilerEffect) this.spoilers.get(i32);
                                    spoilerEffect.setColor(this.messageLayout.getPaint().getColor());
                                    spoilerEffect.draw(canvas);
                                }
                            } catch (Exception e4) {
                                e = e4;
                                FileLog.e(e);
                                this.messageLayout.getPaint().setAlpha(alpha2);
                                canvas.restore();
                                canvas.save();
                                DialogUpdateHelper dialogUpdateHelper2 = this.updateHelper;
                                if (!dialogUpdateHelper2.typingOutToTop) {
                                }
                                if (!this.useForceThreeLines) {
                                    f17 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                                    canvas.translate(this.typingLeft, f17);
                                    staticLayout2 = this.typingLayout;
                                    if (staticLayout2 != null) {
                                        int alpha3 = staticLayout2.getPaint().getAlpha();
                                        this.typingLayout.getPaint().setAlpha((int) (alpha3 * this.updateHelper.typingProgres));
                                        this.typingLayout.draw(canvas);
                                        this.typingLayout.getPaint().setAlpha(alpha3);
                                    }
                                    canvas.restore();
                                    if (this.typingLayout != null) {
                                    }
                                    if (this.buttonLayout == null) {
                                    }
                                    if (this.currentDialogFolderId == 0) {
                                    }
                                    if (this.drawUnmute) {
                                    }
                                    if (this.dialogsType != i14) {
                                    }
                                    if (this.drawVerified) {
                                    }
                                    drawable.draw(canvas);
                                    if (!this.drawReorder) {
                                    }
                                    Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                                    BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                                    Theme.dialogs_reorderDrawable.draw(canvas);
                                    if (!this.drawError) {
                                    }
                                    drawable2.draw(canvas);
                                    canvas2 = canvas;
                                    if (this.thumbsCount > 0) {
                                    }
                                    i15 = -1;
                                    z11 = false;
                                    dialogCellTags = this.tags;
                                    if (dialogCellTags != null) {
                                    }
                                    i16 = i8;
                                    if (i16 != i15) {
                                    }
                                    z3 = z8;
                                    r10 = z11;
                                    if (this.animatingArchiveAvatar) {
                                    }
                                    if (this.drawAvatar) {
                                    }
                                    if (this.animatingArchiveAvatar) {
                                    }
                                    if (this.avatarImage.getVisible()) {
                                    }
                                    if (this.rightFragmentOpenedProgress > f7) {
                                    }
                                    if (this.collapseOffset != f7) {
                                    }
                                    if (this.translationX != f7) {
                                    }
                                    if (this.drawArchive) {
                                    }
                                    if (this.useSeparator) {
                                    }
                                    if (this.clipProgress != f7) {
                                    }
                                    z4 = this.drawReorder;
                                    if (!z4) {
                                    }
                                    if (z4) {
                                    }
                                    if (this.archiveHidden) {
                                    }
                                    z5 = true;
                                    if (this.animatingArchiveAvatar) {
                                    }
                                    if (this.drawRevealBackground) {
                                    }
                                }
                                f17 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                                canvas.translate(this.typingLeft, f17);
                                staticLayout2 = this.typingLayout;
                                if (staticLayout2 != null) {
                                }
                                canvas.restore();
                                if (this.typingLayout != null) {
                                }
                                if (this.buttonLayout == null) {
                                }
                                if (this.currentDialogFolderId == 0) {
                                }
                                if (this.drawUnmute) {
                                }
                                if (this.dialogsType != i14) {
                                }
                                if (this.drawVerified) {
                                }
                                drawable.draw(canvas);
                                if (!this.drawReorder) {
                                }
                                Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                                BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                                Theme.dialogs_reorderDrawable.draw(canvas);
                                if (!this.drawError) {
                                }
                                drawable2.draw(canvas);
                                canvas2 = canvas;
                                if (this.thumbsCount > 0) {
                                }
                                i15 = -1;
                                z11 = false;
                                dialogCellTags = this.tags;
                                if (dialogCellTags != null) {
                                }
                                i16 = i8;
                                if (i16 != i15) {
                                }
                                z3 = z8;
                                r10 = z11;
                                if (this.animatingArchiveAvatar) {
                                }
                                if (this.drawAvatar) {
                                }
                                if (this.animatingArchiveAvatar) {
                                }
                                if (this.avatarImage.getVisible()) {
                                }
                                if (this.rightFragmentOpenedProgress > f7) {
                                }
                                if (this.collapseOffset != f7) {
                                }
                                if (this.translationX != f7) {
                                }
                                if (this.drawArchive) {
                                }
                                if (this.useSeparator) {
                                }
                                if (this.clipProgress != f7) {
                                }
                                z4 = this.drawReorder;
                                if (!z4) {
                                }
                                if (z4) {
                                }
                                if (this.archiveHidden) {
                                }
                                z5 = true;
                                if (this.animatingArchiveAvatar) {
                                }
                                if (this.drawRevealBackground) {
                                }
                            }
                        }
                        this.messageLayout.getPaint().setAlpha(alpha2);
                        canvas.restore();
                    } else {
                        f16 = 1.0f;
                    }
                    canvas.save();
                    DialogUpdateHelper dialogUpdateHelper22 = this.updateHelper;
                    f17 = !dialogUpdateHelper22.typingOutToTop ? this.messageTop + ((f16 - dialogUpdateHelper22.typingProgres) * dp6) : this.messageTop - ((f16 - dialogUpdateHelper22.typingProgres) * dp6);
                    if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                        f17 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                    }
                    canvas.translate(this.typingLeft, f17);
                    staticLayout2 = this.typingLayout;
                    if (staticLayout2 != null && this.updateHelper.typingProgres > f13) {
                        int alpha32 = staticLayout2.getPaint().getAlpha();
                        this.typingLayout.getPaint().setAlpha((int) (alpha32 * this.updateHelper.typingProgres));
                        this.typingLayout.draw(canvas);
                        this.typingLayout.getPaint().setAlpha(alpha32);
                    }
                    canvas.restore();
                    if (this.typingLayout != null) {
                        int i33 = this.printingStringType;
                        if (i33 < 0) {
                            DialogUpdateHelper dialogUpdateHelper3 = this.updateHelper;
                            if (dialogUpdateHelper3.typingProgres > f13) {
                            }
                        }
                        if (i33 < 0) {
                            i33 = this.updateHelper.lastKnownTypingType;
                        }
                        StatusDrawable chatStatusDrawable = Theme.getChatStatusDrawable(i33);
                        if (chatStatusDrawable != null) {
                            canvas.save();
                            chatStatusDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_chats_actionMessage), (int) (Color.alpha(color4) * this.updateHelper.typingProgres)));
                            DialogUpdateHelper dialogUpdateHelper4 = this.updateHelper;
                            float f28 = dialogUpdateHelper4.typingOutToTop ? this.messageTop + (dp6 * (f16 - dialogUpdateHelper4.typingProgres)) : this.messageTop - (dp6 * (f16 - dialogUpdateHelper4.typingProgres));
                            if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                                f28 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                            }
                            if (i33 == i9 || i33 == i12) {
                                canvas.translate(this.statusDrawableLeft, f28 + (i33 == i9 ? AndroidUtilities.dp(f16) : 0));
                            } else {
                                canvas.translate(this.statusDrawableLeft, f28 + ((AndroidUtilities.dp(18.0f) - chatStatusDrawable.getIntrinsicHeight()) / 2.0f));
                            }
                            chatStatusDrawable.draw(canvas);
                            invalidate();
                            canvas.restore();
                        }
                    }
                }
                if (this.buttonLayout == null) {
                    canvas.save();
                    if (this.buttonBackgroundPaint == null) {
                        this.buttonBackgroundPaint = new Paint(i9);
                    }
                    if (this.canvasButton == null) {
                        CanvasButton canvasButton = new CanvasButton(this);
                        this.canvasButton = canvasButton;
                        canvasButton.setDelegate(new Runnable() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                DialogCell.this.lambda$onDraw$2();
                            }
                        });
                        this.canvasButton.setLongPress(new Runnable() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda3
                            @Override // java.lang.Runnable
                            public final void run() {
                                DialogCell.this.lambda$onDraw$3();
                            }
                        });
                    }
                    if (this.lastTopicMessageUnread && this.topMessageTopicEndIndex != this.topMessageTopicStartIndex && ((i19 = this.dialogsType) == 0 || i19 == 7 || i19 == 8)) {
                        this.canvasButton.setColor(ColorUtils.setAlphaComponent(this.currentMessagePaint.getColor(), Theme.isCurrentThemeDark() ? 36 : 26));
                        if (!this.buttonCreated) {
                            this.canvasButton.rewind();
                            int i34 = this.topMessageTopicEndIndex;
                            if (i34 != this.topMessageTopicStartIndex && i34 > 0) {
                                float f29 = this.messageTop;
                                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                                    f29 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                                }
                                RectF rectF = AndroidUtilities.rectTmp;
                                StaticLayout staticLayout8 = this.messageLayout;
                                rectF.set(this.messageLeft + AndroidUtilities.dp(2.0f) + this.messageLayout.getPrimaryHorizontal(0), f29, (this.messageLeft + staticLayout8.getPrimaryHorizontal(Math.min(staticLayout8.getText().length(), this.topMessageTopicEndIndex))) - AndroidUtilities.dp(3.0f), this.buttonTop - AndroidUtilities.dp(4.0f));
                                rectF.inset(-AndroidUtilities.dp(8.0f), -AndroidUtilities.dp(4.0f));
                                if (rectF.right > rectF.left) {
                                    this.canvasButton.addRect(rectF);
                                }
                            }
                            float lineLeft = this.buttonLayout.getLineLeft(0);
                            RectF rectF2 = AndroidUtilities.rectTmp;
                            rectF2.set(this.buttonLeft + lineLeft + AndroidUtilities.dp(2.0f), this.buttonTop + AndroidUtilities.dp(2.0f), this.buttonLeft + lineLeft + this.buttonLayout.getLineWidth(0) + AndroidUtilities.dp(12.0f), this.buttonTop + this.buttonLayout.getHeight());
                            rectF2.inset(-AndroidUtilities.dp(8.0f), -AndroidUtilities.dp(3.0f));
                            this.canvasButton.addRect(rectF2);
                        }
                        this.canvasButton.draw(canvas);
                        Theme.dialogs_forum_arrowDrawable.setAlpha(125);
                        Drawable drawable4 = Theme.dialogs_forum_arrowDrawable;
                        RectF rectF3 = AndroidUtilities.rectTmp;
                        BaseCell.setDrawableBounds(drawable4, rectF3.right - AndroidUtilities.dp(18.0f), rectF3.top + ((rectF3.height() - Theme.dialogs_forum_arrowDrawable.getIntrinsicHeight()) / 2.0f));
                        Theme.dialogs_forum_arrowDrawable.draw(canvas);
                    }
                    canvas.translate(this.buttonLeft, this.buttonTop);
                    if (this.spoilers2.isEmpty()) {
                        SpoilerEffect.layoutDrawMaybe(this.buttonLayout, canvas);
                        StaticLayout staticLayout9 = this.buttonLayout;
                        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout9, this.animatedEmojiStack3, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(3, staticLayout9.getPaint().getColor()));
                    } else {
                        try {
                            canvas.save();
                            SpoilerEffect.clipOutCanvas(canvas, this.spoilers2);
                            SpoilerEffect.layoutDrawMaybe(this.buttonLayout, canvas);
                            staticLayout = this.buttonLayout;
                        } catch (Exception e5) {
                            e = e5;
                        }
                        try {
                            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout, this.animatedEmojiStack3, -0.075f, this.spoilers2, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(3, staticLayout.getPaint().getColor()));
                            canvas.restore();
                            for (int i35 = 0; i35 < this.spoilers2.size(); i35 += i9) {
                                SpoilerEffect spoilerEffect2 = (SpoilerEffect) this.spoilers2.get(i35);
                                spoilerEffect2.setColor(this.buttonLayout.getPaint().getColor());
                                spoilerEffect2.draw(canvas);
                            }
                        } catch (Exception e6) {
                            e = e6;
                            FileLog.e(e);
                            canvas.restore();
                            if (this.currentDialogFolderId == 0) {
                            }
                            if (this.drawUnmute) {
                            }
                            if (this.dialogsType != i14) {
                            }
                            if (this.drawVerified) {
                            }
                            drawable.draw(canvas);
                            if (!this.drawReorder) {
                            }
                            Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                            BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                            Theme.dialogs_reorderDrawable.draw(canvas);
                            if (!this.drawError) {
                            }
                            drawable2.draw(canvas);
                            canvas2 = canvas;
                            if (this.thumbsCount > 0) {
                            }
                            i15 = -1;
                            z11 = false;
                            dialogCellTags = this.tags;
                            if (dialogCellTags != null) {
                            }
                            i16 = i8;
                            if (i16 != i15) {
                            }
                            z3 = z8;
                            r10 = z11;
                            if (this.animatingArchiveAvatar) {
                            }
                            if (this.drawAvatar) {
                            }
                            if (this.animatingArchiveAvatar) {
                            }
                            if (this.avatarImage.getVisible()) {
                            }
                            if (this.rightFragmentOpenedProgress > f7) {
                            }
                            if (this.collapseOffset != f7) {
                            }
                            if (this.translationX != f7) {
                            }
                            if (this.drawArchive) {
                            }
                            if (this.useSeparator) {
                            }
                            if (this.clipProgress != f7) {
                            }
                            z4 = this.drawReorder;
                            if (!z4) {
                            }
                            if (z4) {
                            }
                            if (this.archiveHidden) {
                            }
                            z5 = true;
                            if (this.animatingArchiveAvatar) {
                            }
                            if (this.drawRevealBackground) {
                            }
                        }
                    }
                    canvas.restore();
                }
                if (this.currentDialogFolderId == 0) {
                    int i36 = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
                    int i37 = this.lastStatusDrawableParams;
                    if (i37 >= 0 && i37 != i36 && !this.statusDrawableAnimationInProgress) {
                        createStatusDrawableAnimator(i37, i36);
                    }
                    boolean z13 = this.statusDrawableAnimationInProgress;
                    if (z13) {
                        i36 = this.animateToStatusDrawableParams;
                    }
                    boolean z14 = (i36 & 1) != 0;
                    boolean z15 = (i36 & 2) != 0;
                    boolean z16 = (i36 & i12) != 0;
                    if (z13) {
                        int i38 = this.animateFromStatusDrawableParams;
                        boolean z17 = (i38 & 1) != 0;
                        boolean z18 = (i38 & 2) != 0;
                        boolean z19 = (i38 & i12) != 0;
                        if (z14 || z17 || !z19 || z18 || !z15 || !z16) {
                            f6 = 1.0f;
                            f7 = 0.0f;
                            dialogCell = this;
                            canvas3 = canvas;
                            boolean z20 = z18;
                            i14 = 2;
                            boolean z21 = z19;
                            i6 = 1;
                            dialogCell.drawCheckStatus(canvas3, z17, z20, z21, false, 1.0f - this.statusDrawableProgress);
                            f15 = this.statusDrawableProgress;
                            z12 = false;
                        } else {
                            f7 = 0.0f;
                            f6 = 1.0f;
                            i6 = 1;
                            drawCheckStatus(canvas, z14, z15, z16, true, this.statusDrawableProgress);
                            i14 = 2;
                            this.lastStatusDrawableParams = (this.drawClock ? 1 : 0) + (!this.drawCheck1 ? 2 : 0) + (!this.drawCheck2 ? 4 : 0);
                        }
                    } else {
                        i14 = 2;
                        i6 = 1;
                        f6 = 1.0f;
                        f7 = 0.0f;
                        z12 = false;
                        f15 = 1.0f;
                        dialogCell = this;
                        canvas3 = canvas;
                    }
                    dialogCell.drawCheckStatus(canvas3, z14, z15, z16, z12, f15);
                    this.lastStatusDrawableParams = (this.drawClock ? 1 : 0) + (!this.drawCheck1 ? 2 : 0) + (!this.drawCheck2 ? 4 : 0);
                } else {
                    i14 = 2;
                    i6 = 1;
                    f6 = 1.0f;
                    f7 = 0.0f;
                }
                boolean z22 = !this.drawUnmute || this.dialogMuted;
                if (this.dialogsType != i14 || ((!z22 && this.dialogMutedProgress <= f7) || this.drawVerified || this.drawScam != 0 || this.drawPremium)) {
                    if (this.drawVerified) {
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            r25 = 16.5f;
                        }
                        float dp7 = AndroidUtilities.dp(r25);
                        if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                            dp7 -= AndroidUtilities.dp(9.0f);
                        }
                        BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft - AndroidUtilities.dp(f6), dp7);
                        BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft - AndroidUtilities.dp(f6), dp7);
                        Theme.dialogs_verifiedDrawable.draw(canvas);
                        drawable = Theme.dialogs_verifiedCheckDrawable;
                    } else {
                        if (this.drawPremium) {
                            int dp8 = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f);
                            if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                                dp8 -= AndroidUtilities.dp(9.0f);
                            }
                            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatus;
                            if (swapAnimatedEmojiDrawable != null) {
                                swapAnimatedEmojiDrawable.setBounds(this.nameMuteLeft - AndroidUtilities.dp(2.0f), dp8 - AndroidUtilities.dp(4.0f), this.nameMuteLeft + AndroidUtilities.dp(20.0f), (dp8 - AndroidUtilities.dp(4.0f)) + AndroidUtilities.dp(22.0f));
                                this.emojiStatus.setColor(Integer.valueOf(Theme.getColor(Theme.key_chats_verifiedBackground, this.resourcesProvider)));
                                this.emojiStatus.draw(canvas);
                            } else {
                                drawable = PremiumGradient.getInstance().premiumStarDrawableMini;
                                int dp9 = this.nameMuteLeft - AndroidUtilities.dp(f6);
                                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                    f20 = 15.5f;
                                }
                                BaseCell.setDrawableBounds(drawable, dp9, AndroidUtilities.dp(f20));
                            }
                        } else if (this.drawScam != 0) {
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                f21 = 15.0f;
                            }
                            int dp10 = AndroidUtilities.dp(f21);
                            if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                                dp10 -= AndroidUtilities.dp(9.0f);
                            }
                            BaseCell.setDrawableBounds((Drawable) (this.drawScam == i6 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable), this.nameMuteLeft, dp10);
                            (this.drawScam == i6 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).draw(canvas);
                        }
                        if (!this.drawReorder || this.reorderIconProgress != f7) {
                            Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                            BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                            Theme.dialogs_reorderDrawable.draw(canvas);
                        }
                        if (!this.drawError) {
                            Theme.dialogs_errorDrawable.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                            this.rect.set(this.errorLeft, this.errorTop, i17 + AndroidUtilities.dp(23.0f), this.errorTop + AndroidUtilities.dp(23.0f));
                            RectF rectF4 = this.rect;
                            float f30 = AndroidUtilities.density * 11.5f;
                            canvas.drawRoundRect(rectF4, f30, f30, Theme.dialogs_errorPaint);
                            BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
                            drawable2 = Theme.dialogs_errorDrawable;
                        } else if (((this.drawCount || this.drawMention) && this.drawCount2) || this.countChangeProgress != f6 || this.drawReactionMention || this.reactionsMentionsChangeProgress != f6) {
                            if (this.isTopic) {
                                z9 = this.topicMuted;
                            } else {
                                TLRPC$Chat tLRPC$Chat = this.chat;
                                if (tLRPC$Chat != null && tLRPC$Chat.forum && this.forumTopic == null) {
                                    z10 = !this.hasUnmutedTopics;
                                    canvas2 = canvas;
                                    drawCounter(canvas, z10, this.countTop, this.countLeft, this.countLeftOld, 1.0f, false);
                                    if (this.drawMention) {
                                        Theme.dialogs_countPaint.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                                        this.rect.set(this.mentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp2 + this.mentionWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                        Paint paint9 = (!z10 || this.folderId == 0) ? Theme.dialogs_countPaint : Theme.dialogs_countGrayPaint;
                                        RectF rectF5 = this.rect;
                                        float f31 = AndroidUtilities.density * 11.5f;
                                        canvas2.drawRoundRect(rectF5, f31, f31, paint9);
                                        if (this.mentionLayout != null) {
                                            Theme.dialogs_countTextPaint.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                                            canvas.save();
                                            canvas2.translate(this.mentionLeft, this.countTop + AndroidUtilities.dp(4.0f));
                                            this.mentionLayout.draw(canvas2);
                                            canvas.restore();
                                        } else {
                                            Theme.dialogs_mentionDrawable.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                                            BaseCell.setDrawableBounds(Theme.dialogs_mentionDrawable, this.mentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.2f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                            Theme.dialogs_mentionDrawable.draw(canvas2);
                                        }
                                    }
                                    if (!this.drawReactionMention || this.reactionsMentionsChangeProgress != f6) {
                                        Theme.dialogs_reactionsCountPaint.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                                        this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                        Paint paint10 = Theme.dialogs_reactionsCountPaint;
                                        canvas.save();
                                        f14 = this.reactionsMentionsChangeProgress;
                                        if (f14 != f6) {
                                            if (!this.drawReactionMention) {
                                                f14 = f6 - f14;
                                            }
                                            canvas2.scale(f14, f14, this.rect.centerX(), this.rect.centerY());
                                        }
                                        RectF rectF6 = this.rect;
                                        float f32 = AndroidUtilities.density * 11.5f;
                                        canvas2.drawRoundRect(rectF6, f32, f32, paint10);
                                        Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                                        BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                        Theme.dialogs_reactionsMentionDrawable.draw(canvas2);
                                        canvas.restore();
                                    }
                                    if (this.thumbsCount > 0) {
                                        float f33 = this.updateHelper.typingProgres;
                                        if (f33 != f6) {
                                            if (f33 > f7) {
                                                canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) ((f6 - f33) * 255.0f), 31);
                                                canvas2.translate(f7, (this.updateHelper.typingOutToTop ? -AndroidUtilities.dp(14.0f) : AndroidUtilities.dp(14.0f)) * this.updateHelper.typingProgres);
                                            }
                                            int i39 = 0;
                                            while (i39 < this.thumbsCount) {
                                                if (this.thumbImageSeen[i39]) {
                                                    if (this.thumbBackgroundPaint == null) {
                                                        Paint paint11 = new Paint(i6);
                                                        this.thumbBackgroundPaint = paint11;
                                                        paint11.setShadowLayer(AndroidUtilities.dp(1.34f), f7, AndroidUtilities.dp(0.34f), 402653184);
                                                        c = 0;
                                                        this.thumbBackgroundPaint.setColor(0);
                                                    } else {
                                                        c = 0;
                                                    }
                                                    RectF rectF7 = AndroidUtilities.rectTmp;
                                                    rectF7.set(this.thumbImage[i39].getImageX(), this.thumbImage[i39].getImageY(), this.thumbImage[i39].getImageX2(), this.thumbImage[i39].getImageY2());
                                                    canvas2.drawRoundRect(rectF7, this.thumbImage[i39].getRoundRadius()[c], this.thumbImage[i39].getRoundRadius()[i6], this.thumbBackgroundPaint);
                                                    this.thumbImage[i39].draw(canvas2);
                                                    if (this.drawSpoiler[i39]) {
                                                        Path path = this.thumbPath;
                                                        if (path == null) {
                                                            this.thumbPath = new Path();
                                                        } else {
                                                            path.rewind();
                                                        }
                                                        this.thumbPath.addRoundRect(rectF7, this.thumbImage[i39].getRoundRadius()[c], this.thumbImage[i39].getRoundRadius()[i6], Path.Direction.CW);
                                                        canvas.save();
                                                        canvas2.clipPath(this.thumbPath);
                                                        this.thumbSpoiler.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(i11) * 0.325f)));
                                                        this.thumbSpoiler.setBounds((int) this.thumbImage[i39].getImageX(), (int) this.thumbImage[i39].getImageY(), (int) this.thumbImage[i39].getImageX2(), (int) this.thumbImage[i39].getImageY2());
                                                        this.thumbSpoiler.draw(canvas2);
                                                        invalidate();
                                                        canvas.restore();
                                                    }
                                                    if (this.drawPlay[i39]) {
                                                        BaseCell.setDrawableBounds(Theme.dialogs_playDrawable, (int) (this.thumbImage[i39].getCenterX() - (Theme.dialogs_playDrawable.getIntrinsicWidth() / 2)), (int) (this.thumbImage[i39].getCenterY() - (Theme.dialogs_playDrawable.getIntrinsicHeight() / 2)));
                                                        Theme.dialogs_playDrawable.draw(canvas2);
                                                    }
                                                }
                                                i39 += i6;
                                                i11 = -1;
                                            }
                                            i15 = -1;
                                            z11 = false;
                                            z11 = false;
                                            if (this.updateHelper.typingProgres > f7) {
                                                canvas.restore();
                                            }
                                            dialogCellTags = this.tags;
                                            if (dialogCellTags != null && !dialogCellTags.isEmpty()) {
                                                canvas.save();
                                                canvas2.translate(this.tagsLeft, (getMeasuredHeight() - AndroidUtilities.dp(21.66f)) - (this.useSeparator ? 1 : 0));
                                                this.tags.draw(canvas2, this.tagsRight - this.tagsLeft);
                                                canvas.restore();
                                            }
                                            i16 = i8;
                                            if (i16 != i15) {
                                                canvas2.restoreToCount(i16);
                                            }
                                            z3 = z8;
                                            r10 = z11;
                                        }
                                    }
                                    i15 = -1;
                                    z11 = false;
                                    dialogCellTags = this.tags;
                                    if (dialogCellTags != null) {
                                        canvas.save();
                                        canvas2.translate(this.tagsLeft, (getMeasuredHeight() - AndroidUtilities.dp(21.66f)) - (this.useSeparator ? 1 : 0));
                                        this.tags.draw(canvas2, this.tagsRight - this.tagsLeft);
                                        canvas.restore();
                                    }
                                    i16 = i8;
                                    if (i16 != i15) {
                                    }
                                    z3 = z8;
                                    r10 = z11;
                                } else {
                                    z9 = this.dialogMuted;
                                }
                            }
                            z10 = z9;
                            canvas2 = canvas;
                            drawCounter(canvas, z10, this.countTop, this.countLeft, this.countLeftOld, 1.0f, false);
                            if (this.drawMention) {
                            }
                            if (!this.drawReactionMention) {
                            }
                            Theme.dialogs_reactionsCountPaint.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                            this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                            Paint paint102 = Theme.dialogs_reactionsCountPaint;
                            canvas.save();
                            f14 = this.reactionsMentionsChangeProgress;
                            if (f14 != f6) {
                            }
                            RectF rectF62 = this.rect;
                            float f322 = AndroidUtilities.density * 11.5f;
                            canvas2.drawRoundRect(rectF62, f322, f322, paint102);
                            Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                            BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                            Theme.dialogs_reactionsMentionDrawable.draw(canvas2);
                            canvas.restore();
                            if (this.thumbsCount > 0) {
                            }
                            i15 = -1;
                            z11 = false;
                            dialogCellTags = this.tags;
                            if (dialogCellTags != null) {
                            }
                            i16 = i8;
                            if (i16 != i15) {
                            }
                            z3 = z8;
                            r10 = z11;
                        } else {
                            if (getIsPinned()) {
                                Theme.dialogs_pinnedDrawable.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                                BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                                drawable2 = Theme.dialogs_pinnedDrawable;
                            }
                            canvas2 = canvas;
                            if (this.thumbsCount > 0) {
                            }
                            i15 = -1;
                            z11 = false;
                            dialogCellTags = this.tags;
                            if (dialogCellTags != null) {
                            }
                            i16 = i8;
                            if (i16 != i15) {
                            }
                            z3 = z8;
                            r10 = z11;
                        }
                        drawable2.draw(canvas);
                        canvas2 = canvas;
                        if (this.thumbsCount > 0) {
                        }
                        i15 = -1;
                        z11 = false;
                        dialogCellTags = this.tags;
                        if (dialogCellTags != null) {
                        }
                        i16 = i8;
                        if (i16 != i15) {
                        }
                        z3 = z8;
                        r10 = z11;
                    }
                    drawable.draw(canvas);
                    if (!this.drawReorder) {
                    }
                    Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                    BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                    Theme.dialogs_reorderDrawable.draw(canvas);
                    if (!this.drawError) {
                    }
                    drawable2.draw(canvas);
                    canvas2 = canvas;
                    if (this.thumbsCount > 0) {
                    }
                    i15 = -1;
                    z11 = false;
                    dialogCellTags = this.tags;
                    if (dialogCellTags != null) {
                    }
                    i16 = i8;
                    if (i16 != i15) {
                    }
                    z3 = z8;
                    r10 = z11;
                } else {
                    if (z22) {
                        float f34 = this.dialogMutedProgress;
                        if (f34 != f6) {
                            float f35 = f34 + 0.10666667f;
                            this.dialogMutedProgress = f35;
                            if (f35 > f6) {
                                this.dialogMutedProgress = f6;
                                float dp11 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                                float dp12 = AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
                                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                                    dp12 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
                                }
                                BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp11, dp12);
                                BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp11, dp12);
                                if (this.dialogMutedProgress == f6) {
                                    canvas.save();
                                    float f36 = this.dialogMutedProgress;
                                    canvas.scale(f36, f36, Theme.dialogs_muteDrawable.getBounds().centerX(), Theme.dialogs_muteDrawable.getBounds().centerY());
                                    if (this.drawUnmute) {
                                        Theme.dialogs_unmuteDrawable.setAlpha((int) (this.dialogMutedProgress * 255.0f));
                                        Theme.dialogs_unmuteDrawable.draw(canvas);
                                        drawable3 = Theme.dialogs_unmuteDrawable;
                                        i18 = NotificationCenter.didClearDatabase;
                                    } else {
                                        i18 = NotificationCenter.didClearDatabase;
                                        Theme.dialogs_muteDrawable.setAlpha((int) (this.dialogMutedProgress * 255.0f));
                                        Theme.dialogs_muteDrawable.draw(canvas);
                                        drawable3 = Theme.dialogs_muteDrawable;
                                    }
                                    drawable3.setAlpha(i18);
                                    canvas.restore();
                                    if (!this.drawReorder) {
                                    }
                                    Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                                    BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                                    Theme.dialogs_reorderDrawable.draw(canvas);
                                    if (!this.drawError) {
                                    }
                                    drawable2.draw(canvas);
                                    canvas2 = canvas;
                                    if (this.thumbsCount > 0) {
                                    }
                                    i15 = -1;
                                    z11 = false;
                                    dialogCellTags = this.tags;
                                    if (dialogCellTags != null) {
                                    }
                                    i16 = i8;
                                    if (i16 != i15) {
                                    }
                                    z3 = z8;
                                    r10 = z11;
                                } else {
                                    drawable = this.drawUnmute ? Theme.dialogs_unmuteDrawable : Theme.dialogs_muteDrawable;
                                    drawable.draw(canvas);
                                    if (!this.drawReorder) {
                                    }
                                    Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                                    BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                                    Theme.dialogs_reorderDrawable.draw(canvas);
                                    if (!this.drawError) {
                                    }
                                    drawable2.draw(canvas);
                                    canvas2 = canvas;
                                    if (this.thumbsCount > 0) {
                                    }
                                    i15 = -1;
                                    z11 = false;
                                    dialogCellTags = this.tags;
                                    if (dialogCellTags != null) {
                                    }
                                    i16 = i8;
                                    if (i16 != i15) {
                                    }
                                    z3 = z8;
                                    r10 = z11;
                                }
                            }
                            invalidate();
                            float dp112 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                            float dp122 = AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
                            if (!this.useForceThreeLines) {
                                dp122 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
                                BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp112, dp122);
                                BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp112, dp122);
                                if (this.dialogMutedProgress == f6) {
                                }
                            }
                            dp122 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
                            BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp112, dp122);
                            BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp112, dp122);
                            if (this.dialogMutedProgress == f6) {
                            }
                        }
                    }
                    if (!z22) {
                        float f37 = this.dialogMutedProgress;
                        if (f37 != f7) {
                            float f38 = f37 - 0.10666667f;
                            this.dialogMutedProgress = f38;
                            if (f38 < f7) {
                                this.dialogMutedProgress = f7;
                            }
                            invalidate();
                        }
                    }
                    float dp1122 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                    float dp1222 = AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
                    if (!this.useForceThreeLines) {
                    }
                    dp1222 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
                    BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp1122, dp1222);
                    BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp1122, dp1222);
                    if (this.dialogMutedProgress == f6) {
                    }
                }
            } else {
                canvas2 = canvas;
                r10 = 0;
                i6 = 1;
                f6 = 1.0f;
                f7 = 0.0f;
                z3 = false;
            }
            if (this.animatingArchiveAvatar) {
                f8 = 170.0f;
            } else {
                canvas.save();
                f8 = 170.0f;
                float interpolation2 = this.interpolator.getInterpolation(this.animatingArchiveAvatarProgress / 170.0f) + f6;
                canvas2.scale(interpolation2, interpolation2, this.avatarImage.getCenterX(), this.avatarImage.getCenterY());
            }
            if (this.drawAvatar && (!this.isTopic || (tLRPC$TL_forumTopic2 = this.forumTopic) == null || tLRPC$TL_forumTopic2.id != i6 || (pullForegroundDrawable = this.archivedChatsDrawable) == null || !pullForegroundDrawable.isDraw())) {
                StoriesUtilities.AvatarStoryParams avatarStoryParams = this.storyParams;
                avatarStoryParams.drawHiddenStoriesAsSegments = this.currentDialogFolderId == 0;
                StoriesUtilities.drawAvatarWithStory(this.currentDialogId, canvas2, this.avatarImage, avatarStoryParams);
            }
            if (this.animatingArchiveAvatar) {
                canvas.restore();
            }
            boolean z23 = (this.avatarImage.getVisible() || !drawAvatarOverlays(canvas)) ? z3 : true;
            if (this.rightFragmentOpenedProgress > f7 && this.currentDialogFolderId == 0) {
                if (this.isTopic) {
                    TLRPC$Chat tLRPC$Chat2 = this.chat;
                    if (tLRPC$Chat2 != null && tLRPC$Chat2.forum && this.forumTopic == null) {
                        z7 = !this.hasUnmutedTopics;
                        RectF rectF8 = this.storyParams.originalAvatarRect;
                        int width = (int) (((rectF8.left + rectF8.width()) - this.countWidth) - AndroidUtilities.dp(5.0f));
                        RectF rectF9 = this.storyParams.originalAvatarRect;
                        drawCounter(canvas, z7, (int) ((this.avatarImage.getImageY() + this.storyParams.originalAvatarRect.height()) - AndroidUtilities.dp(22.0f)), width, (int) (((rectF9.left + rectF9.width()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
                    } else {
                        z6 = this.dialogMuted;
                    }
                } else {
                    z6 = this.topicMuted;
                }
                z7 = z6;
                RectF rectF82 = this.storyParams.originalAvatarRect;
                int width2 = (int) (((rectF82.left + rectF82.width()) - this.countWidth) - AndroidUtilities.dp(5.0f));
                RectF rectF92 = this.storyParams.originalAvatarRect;
                drawCounter(canvas, z7, (int) ((this.avatarImage.getImageY() + this.storyParams.originalAvatarRect.height()) - AndroidUtilities.dp(22.0f)), width2, (int) (((rectF92.left + rectF92.width()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
            }
            if (this.collapseOffset != f7) {
                canvas.restore();
            }
            if (this.translationX != f7) {
                canvas.restore();
            }
            if (this.drawArchive && ((this.currentDialogFolderId != 0 || (this.isTopic && (tLRPC$TL_forumTopic = this.forumTopic) != null && tLRPC$TL_forumTopic.id == i6)) && this.translationX == f7 && this.archivedChatsDrawable != null)) {
                canvas.save();
                canvas2.translate(f7, (-this.translateY) - (this.rightFragmentOffset * this.rightFragmentOpenedProgress));
                canvas2.clipRect((int) r10, (int) r10, getMeasuredWidth(), getMeasuredHeight());
                this.archivedChatsDrawable.draw(canvas2);
                canvas.restore();
            }
            if (this.useSeparator) {
                int dp13 = (this.fullSeparator || !(this.currentDialogFolderId == 0 || !this.archiveHidden || this.fullSeparator2) || (this.fullSeparator2 && !this.archiveHidden)) ? 0 : AndroidUtilities.dp(this.messagePaddingStart);
                if (this.rightFragmentOpenedProgress != f6) {
                    int alpha4 = Theme.dividerPaint.getAlpha();
                    float f39 = this.rightFragmentOpenedProgress;
                    if (f39 != f7) {
                        Theme.dividerPaint.setAlpha((int) (alpha4 * (f6 - f39)));
                    }
                    float measuredHeight3 = (getMeasuredHeight() - i6) - (this.rightFragmentOffset * this.rightFragmentOpenedProgress);
                    if (LocaleController.isRTL) {
                        measuredWidth = getMeasuredWidth() - dp13;
                        paint2 = Theme.dividerPaint;
                        f9 = 0.0f;
                    } else {
                        f9 = dp13;
                        measuredWidth = getMeasuredWidth();
                        paint2 = Theme.dividerPaint;
                    }
                    canvas.drawLine(f9, measuredHeight3, measuredWidth, measuredHeight3, paint2);
                    if (this.rightFragmentOpenedProgress != f7) {
                        Theme.dividerPaint.setAlpha(alpha4);
                    }
                }
            }
            if (this.clipProgress != f7) {
                if (Build.VERSION.SDK_INT != 24) {
                    canvas.restore();
                } else {
                    Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                    canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), this.topClip * this.clipProgress, Theme.dialogs_pinnedPaint);
                    canvas.drawRect(0.0f, getMeasuredHeight() - ((int) (this.bottomClip * this.clipProgress)), getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
                }
            }
            z4 = this.drawReorder;
            if (!z4 || this.reorderIconProgress != f7) {
                if (z4) {
                    float f40 = this.reorderIconProgress;
                    if (f40 > f7) {
                        float f41 = f40 - 0.09411765f;
                        this.reorderIconProgress = f41;
                        if (f41 < f7) {
                            this.reorderIconProgress = f7;
                        }
                        z5 = true;
                    }
                } else {
                    float f42 = this.reorderIconProgress;
                    if (f42 < f6) {
                        float f43 = f42 + 0.09411765f;
                        this.reorderIconProgress = f43;
                        if (f43 > f6) {
                            this.reorderIconProgress = f6;
                        }
                        z5 = true;
                    }
                }
                if (this.archiveHidden) {
                    float f44 = this.archiveBackgroundProgress;
                    if (f44 > f7) {
                        float f45 = f44 - 0.069565214f;
                        this.archiveBackgroundProgress = f45;
                        if (f45 < f7) {
                            this.archiveBackgroundProgress = f7;
                        }
                    }
                    if (this.animatingArchiveAvatar) {
                        float f46 = this.animatingArchiveAvatarProgress + 16.0f;
                        this.animatingArchiveAvatarProgress = f46;
                        if (f46 >= f8) {
                            this.animatingArchiveAvatarProgress = f8;
                            this.animatingArchiveAvatar = r10;
                        }
                        z5 = true;
                    }
                    if (this.drawRevealBackground) {
                        float f47 = this.currentRevealBounceProgress;
                        if (f47 < f6) {
                            float f48 = f47 + 0.09411765f;
                            this.currentRevealBounceProgress = f48;
                            if (f48 > f6) {
                                this.currentRevealBounceProgress = f6;
                                z5 = true;
                            }
                        }
                        float f49 = this.currentRevealProgress;
                        if (f49 < f6) {
                            float f50 = f49 + 0.053333335f;
                            this.currentRevealProgress = f50;
                            if (f50 > f6) {
                                this.currentRevealProgress = f6;
                            }
                            z5 = true;
                        }
                        if (z5) {
                            return;
                        }
                        invalidate();
                        return;
                    }
                    if (this.currentRevealBounceProgress == f6) {
                        this.currentRevealBounceProgress = f7;
                        z5 = true;
                    }
                    float f51 = this.currentRevealProgress;
                    if (f51 > f7) {
                        float f52 = f51 - 0.053333335f;
                        this.currentRevealProgress = f52;
                        if (f52 < f7) {
                            this.currentRevealProgress = f7;
                        }
                        z5 = true;
                    }
                    if (z5) {
                    }
                } else {
                    float f53 = this.archiveBackgroundProgress;
                    if (f53 < f6) {
                        float f54 = f53 + 0.069565214f;
                        this.archiveBackgroundProgress = f54;
                        if (f54 > f6) {
                            this.archiveBackgroundProgress = f6;
                        }
                    }
                    if (this.animatingArchiveAvatar) {
                    }
                    if (this.drawRevealBackground) {
                    }
                }
                z5 = true;
                if (this.animatingArchiveAvatar) {
                }
                if (this.drawRevealBackground) {
                }
            }
            z5 = z23;
            if (this.archiveHidden) {
            }
            z5 = true;
            if (this.animatingArchiveAvatar) {
            }
            if (this.drawRevealBackground) {
            }
        }
        paint.setColor(color3);
        Theme.dialogs_pinnedPaint.setAlpha((int) (paint6.getAlpha() * (1.0f - this.rightFragmentOpenedProgress)));
        i5 = 0;
        canvas.drawRect(-this.xOffset, 0.0f, getMeasuredWidth(), getMeasuredHeight() - this.translateY, Theme.dialogs_pinnedPaint);
        canvas.restore();
        this.updateHelper.updateAnimationValues();
        if (this.collapseOffset != 0.0f) {
        }
        f5 = this.rightFragmentOpenedProgress;
        if (f5 == 1.0f) {
        }
        if (this.animatingArchiveAvatar) {
        }
        if (this.drawAvatar) {
            StoriesUtilities.AvatarStoryParams avatarStoryParams2 = this.storyParams;
            avatarStoryParams2.drawHiddenStoriesAsSegments = this.currentDialogFolderId == 0;
            StoriesUtilities.drawAvatarWithStory(this.currentDialogId, canvas2, this.avatarImage, avatarStoryParams2);
        }
        if (this.animatingArchiveAvatar) {
        }
        if (this.avatarImage.getVisible()) {
        }
        if (this.rightFragmentOpenedProgress > f7) {
            if (this.isTopic) {
            }
            z7 = z6;
            RectF rectF822 = this.storyParams.originalAvatarRect;
            int width22 = (int) (((rectF822.left + rectF822.width()) - this.countWidth) - AndroidUtilities.dp(5.0f));
            RectF rectF922 = this.storyParams.originalAvatarRect;
            drawCounter(canvas, z7, (int) ((this.avatarImage.getImageY() + this.storyParams.originalAvatarRect.height()) - AndroidUtilities.dp(22.0f)), width22, (int) (((rectF922.left + rectF922.width()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
        }
        if (this.collapseOffset != f7) {
        }
        if (this.translationX != f7) {
        }
        if (this.drawArchive) {
            canvas.save();
            canvas2.translate(f7, (-this.translateY) - (this.rightFragmentOffset * this.rightFragmentOpenedProgress));
            canvas2.clipRect((int) r10, (int) r10, getMeasuredWidth(), getMeasuredHeight());
            this.archivedChatsDrawable.draw(canvas2);
            canvas.restore();
        }
        if (this.useSeparator) {
        }
        if (this.clipProgress != f7) {
        }
        z4 = this.drawReorder;
        if (!z4) {
        }
        if (z4) {
        }
        if (this.archiveHidden) {
        }
        z5 = true;
        if (this.animatingArchiveAvatar) {
        }
        if (this.drawRevealBackground) {
        }
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
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.acc_action_chat_preview, LocaleController.getString(R.string.AccActionChatPreview)));
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

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.rightFragmentOpenedProgress == 0.0f && !this.isTopic && this.storyParams.checkOnTouchEvent(motionEvent, this)) {
            return true;
        }
        return super.onInterceptTouchEvent(motionEvent);
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

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 != null) {
            checkBox2.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824));
        }
        if (this.isTopic) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp(((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? this.heightThreeLines : this.heightDefault) + ((!hasTags() || ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !isForumCell())) ? 0 : isForumCell() ? this.addForumHeightForTags : this.addHeightForTags)) + (this.useSeparator ? 1 : 0));
            checkTwoLinesForName();
        }
        setMeasuredDimension(View.MeasureSpec.getSize(i), computeHeight());
        this.topClip = 0;
        this.bottomClip = getMeasuredHeight();
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00ff  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0119  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x013a  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0190  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        String str;
        int i;
        int i2;
        int i3;
        MessageObject messageObject;
        int i4;
        MessageObject captionMessage;
        TLRPC$User user;
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        StringBuilder sb = new StringBuilder();
        if (this.currentDialogFolderId == 1) {
            str = LocaleController.getString(R.string.ArchivedChats);
        } else {
            if (this.encryptedChat != null) {
                sb.append(LocaleController.getString(R.string.AccDescrSecretChat));
                sb.append(". ");
            }
            if (!this.isTopic || this.forumTopic == null) {
                TLRPC$User tLRPC$User = this.user;
                if (tLRPC$User == null) {
                    TLRPC$Chat tLRPC$Chat = this.chat;
                    if (tLRPC$Chat != null) {
                        sb.append(LocaleController.getString(tLRPC$Chat.broadcast ? R.string.AccDescrChannel : R.string.AccDescrGroup));
                        sb.append(". ");
                        str = this.chat.title;
                    }
                    if (this.drawVerified) {
                        sb.append(LocaleController.getString(R.string.AccDescrVerified));
                        sb.append(". ");
                    }
                    if (this.dialogMuted) {
                        sb.append(LocaleController.getString(R.string.AccDescrNotificationsMuted));
                        sb.append(". ");
                    }
                    if (isOnline()) {
                        sb.append(LocaleController.getString(R.string.AccDescrUserOnline));
                        sb.append(". ");
                    }
                    i2 = this.unreadCount;
                    if (i2 > 0) {
                        sb.append(LocaleController.formatPluralString("NewMessages", i2, new Object[0]));
                        sb.append(". ");
                    }
                    i3 = this.mentionCount;
                    if (i3 > 0) {
                        sb.append(LocaleController.formatPluralString("AccDescrMentionCount", i3, new Object[0]));
                        sb.append(". ");
                    }
                    if (this.reactionMentionCount > 0) {
                        sb.append(LocaleController.getString(R.string.AccDescrMentionReaction));
                        sb.append(". ");
                    }
                    messageObject = this.message;
                    if (messageObject != null && this.currentDialogFolderId == 0) {
                        i4 = this.lastMessageDate;
                        if (i4 == 0) {
                            i4 = messageObject.messageOwner.date;
                        }
                        String formatDateAudio = LocaleController.formatDateAudio(i4, true);
                        sb.append(!this.message.isOut() ? LocaleController.formatString("AccDescrSentDate", R.string.AccDescrSentDate, formatDateAudio) : LocaleController.formatString("AccDescrReceivedDate", R.string.AccDescrReceivedDate, formatDateAudio));
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
                    }
                    accessibilityEvent.setContentDescription(sb);
                    setContentDescription(sb);
                }
                if (UserObject.isReplyUser(tLRPC$User)) {
                    i = R.string.RepliesTitle;
                } else if (UserObject.isAnonymous(this.user)) {
                    i = R.string.AnonymousForward;
                } else {
                    if (this.user.bot) {
                        sb.append(LocaleController.getString(R.string.Bot));
                        sb.append(". ");
                    }
                    TLRPC$User tLRPC$User2 = this.user;
                    if (tLRPC$User2.self) {
                        i = R.string.SavedMessages;
                    } else {
                        str = ContactsController.formatName(tLRPC$User2.first_name, tLRPC$User2.last_name);
                    }
                }
                str = LocaleController.getString(i);
            } else {
                sb.append(LocaleController.getString(R.string.AccDescrTopic));
                sb.append(". ");
                str = this.forumTopic.title;
            }
        }
        sb.append(str);
        sb.append(". ");
        if (this.drawVerified) {
        }
        if (this.dialogMuted) {
        }
        if (isOnline()) {
        }
        i2 = this.unreadCount;
        if (i2 > 0) {
        }
        i3 = this.mentionCount;
        if (i3 > 0) {
        }
        if (this.reactionMentionCount > 0) {
        }
        messageObject = this.message;
        if (messageObject != null) {
            i4 = this.lastMessageDate;
            if (i4 == 0) {
            }
            String formatDateAudio2 = LocaleController.formatDateAudio(i4, true);
            sb.append(!this.message.isOut() ? LocaleController.formatString("AccDescrSentDate", R.string.AccDescrSentDate, formatDateAudio2) : LocaleController.formatString("AccDescrReceivedDate", R.string.AccDescrReceivedDate, formatDateAudio2));
            sb.append(". ");
            if (this.chat != null) {
                sb.append(ContactsController.formatName(user.first_name, user.last_name));
                sb.append(". ");
            }
            if (this.encryptedChat == null) {
            }
        }
        accessibilityEvent.setContentDescription(sb);
        setContentDescription(sb);
    }

    public void onReorderStateChanged(boolean z, boolean z2) {
        if ((getIsPinned() || !z) && this.drawReorder != z) {
            this.drawReorder = z;
            this.reorderIconProgress = (!z2 ? z : !z) ? 0.0f : 1.0f;
            invalidate();
        } else if (getIsPinned()) {
        } else {
            this.drawReorder = false;
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        CanvasButton canvasButton;
        int i;
        if (this.rightFragmentOpenedProgress == 0.0f && !this.isTopic && this.storyParams.checkOnTouchEvent(motionEvent, this)) {
            return true;
        }
        DialogCellDelegate dialogCellDelegate = this.delegate;
        if ((dialogCellDelegate == null || dialogCellDelegate.canClickButtonInside()) && this.lastTopicMessageUnread && (canvasButton = this.canvasButton) != null && this.buttonLayout != null && (((i = this.dialogsType) == 0 || i == 7 || i == 8) && canvasButton.checkTouchEvent(motionEvent))) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i, Bundle bundle) {
        DialogsActivity dialogsActivity;
        if (i != R.id.acc_action_chat_preview || (dialogsActivity = this.parentFragment) == null) {
            return super.performAccessibilityAction(i, bundle);
        }
        dialogsActivity.showChatPreview(this);
        return true;
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

    public void setArchivedPullAnimation(PullForegroundDrawable pullForegroundDrawable) {
        this.archivedChatsDrawable = pullForegroundDrawable;
    }

    public void setBottomClip(int i) {
        this.bottomClip = i;
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

    public void setClipProgress(float f) {
        this.clipProgress = f;
        invalidate();
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
        showPremiumBlocked(i == 3);
        if (this.tags == null) {
            this.tags = new DialogCellTags();
        }
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

    public void setDialogCellDelegate(DialogCellDelegate dialogCellDelegate) {
        this.delegate = dialogCellDelegate;
    }

    public void setDialogSelected(boolean z) {
        if (this.isSelected != z) {
            invalidate();
        }
        this.isSelected = z;
    }

    public void setForumTopic(TLRPC$TL_forumTopic tLRPC$TL_forumTopic, long j, MessageObject messageObject, boolean z, boolean z2) {
        PullForegroundDrawable pullForegroundDrawable;
        this.forumTopic = tLRPC$TL_forumTopic;
        this.isTopic = tLRPC$TL_forumTopic != null;
        if (this.currentDialogId != j) {
            this.lastStatusDrawableParams = -1;
        }
        Drawable drawable = messageObject.topicIconDrawable[0];
        if (drawable instanceof ForumBubbleDrawable) {
            ((ForumBubbleDrawable) drawable).setColor(tLRPC$TL_forumTopic.icon_color);
        }
        this.currentDialogId = j;
        this.lastDialogChangedTime = System.currentTimeMillis();
        this.message = messageObject;
        this.isDialogCell = false;
        this.showTopicIconInName = z;
        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
        this.lastMessageDate = tLRPC$Message.date;
        this.currentEditDate = tLRPC$Message.edit_date;
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

    public void setIsTransitionSupport(boolean z) {
        this.isTransitionSupport = z;
    }

    public void setMoving(boolean z) {
        this.moving = z;
    }

    public void setPinForced(boolean z) {
        this.drawPinForced = z;
        if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0) {
            buildLayout();
        }
        invalidate();
    }

    public void setPreloader(DialogsAdapter.DialogsPreloader dialogsPreloader) {
        this.preloader = dialogsPreloader;
    }

    public void setRightFragmentOpenedProgress(float f) {
        if (this.rightFragmentOpenedProgress != f) {
            this.rightFragmentOpenedProgress = f;
            invalidate();
        }
    }

    public void setSharedResources(SharedResources sharedResources) {
    }

    public void setSliding(boolean z) {
        this.isSliding = z;
    }

    public void setTopClip(int i) {
        this.topClip = i;
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

    public void setVisible(boolean z) {
        if (this.visibleOnScreen == z) {
            return;
        }
        this.visibleOnScreen = z;
        if (z) {
            invalidate();
        }
    }

    public void showPremiumBlocked(boolean z) {
        Runnable listen;
        Runnable runnable = this.unsubscribePremiumBlocked;
        if (z != (runnable != null)) {
            if (!z && runnable != null) {
                runnable.run();
                listen = null;
            } else if (!z) {
                return;
            } else {
                listen = NotificationCenter.getInstance(this.currentAccount).listen(this, NotificationCenter.userIsPremiumBlockedUpadted, new Utilities.Callback() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda6
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        DialogCell.this.lambda$showPremiumBlocked$5((Object[]) obj);
                    }
                });
            }
            this.unsubscribePremiumBlocked = listen;
        }
    }

    public void startOutAnimation() {
        PullForegroundDrawable pullForegroundDrawable;
        float bitmapWidth;
        PullForegroundDrawable pullForegroundDrawable2 = this.archivedChatsDrawable;
        if (pullForegroundDrawable2 != null) {
            if (this.isTopic) {
                pullForegroundDrawable2.outCy = AndroidUtilities.dp(24.0f);
                this.archivedChatsDrawable.outCx = AndroidUtilities.dp(24.0f);
                pullForegroundDrawable = this.archivedChatsDrawable;
                bitmapWidth = 0.0f;
                pullForegroundDrawable.outRadius = 0.0f;
            } else {
                pullForegroundDrawable2.outCy = this.storyParams.originalAvatarRect.centerY();
                this.archivedChatsDrawable.outCx = this.storyParams.originalAvatarRect.centerX();
                this.archivedChatsDrawable.outRadius = this.storyParams.originalAvatarRect.width() / 2.0f;
                if (MessagesController.getInstance(this.currentAccount).getStoriesController().hasHiddenStories()) {
                    this.archivedChatsDrawable.outRadius -= AndroidUtilities.dpf2(3.5f);
                }
                pullForegroundDrawable = this.archivedChatsDrawable;
                bitmapWidth = this.avatarImage.getBitmapWidth();
            }
            pullForegroundDrawable.outImageSize = bitmapWidth;
            this.archivedChatsDrawable.startOutAnimation();
        }
    }

    public boolean update(int i) {
        return update(i, true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:59:0x015d, code lost:
        if (r8.pinned != false) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x017b, code lost:
        if (r6.pinnedDialogs.indexOfKey(r8.id) >= 0) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x017d, code lost:
        r6 = true;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x032e  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0365  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0412  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x0424  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x042b  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0431  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x043d  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x0454  */
    /* JADX WARN: Removed duplicated region for block: B:271:0x04ae  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x0523  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x054b  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x0554  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x0605  */
    /* JADX WARN: Removed duplicated region for block: B:383:0x077e  */
    /* JADX WARN: Removed duplicated region for block: B:384:0x0780  */
    /* JADX WARN: Removed duplicated region for block: B:386:0x0783 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:399:0x07d8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean update(int i, boolean z) {
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        long j;
        ImageReceiver imageReceiver;
        TLObject tLObject;
        AvatarDrawable avatarDrawable;
        int i2;
        MessageObject messageObject;
        boolean z7;
        boolean z8;
        TLRPC$User tLRPC$User;
        ValueAnimator valueAnimator;
        TimeInterpolator timeInterpolator;
        ValueAnimator valueAnimator2;
        TimeInterpolator overshootInterpolator;
        boolean z9;
        boolean z10;
        TLRPC$Chat chat;
        MessagesController messagesController;
        boolean z11;
        boolean z12;
        MessageObject messageObject2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        TLRPC$Chat chat2;
        MessageObject messageObject3;
        boolean z13;
        boolean isForumCell = isForumCell();
        boolean z14 = false;
        this.drawAvatarSelector = false;
        this.ttlPeriod = 0;
        CustomDialog customDialog = this.customDialog;
        if (customDialog != null) {
            this.lastMessageDate = customDialog.date;
            int i8 = customDialog.unread_count;
            this.lastUnreadState = i8 != 0;
            this.unreadCount = i8;
            this.drawPin = customDialog.pinned;
            this.dialogMuted = customDialog.muted;
            this.hasUnmutedTopics = false;
            this.avatarDrawable.setInfo(customDialog.id, customDialog.name, null);
            this.avatarImage.setImage(null, "50_50", this.avatarDrawable, null, 0L);
            int i9 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr = this.thumbImage;
                if (i9 >= imageReceiverArr.length) {
                    break;
                }
                imageReceiverArr[i9].setImageBitmap((Drawable) null);
                i9++;
            }
            this.avatarImage.setRoundRadius(AndroidUtilities.dp(28.0f));
            this.drawUnmute = false;
            z6 = isForumCell;
            z7 = false;
            z8 = false;
        } else {
            int i10 = this.unreadCount;
            boolean z15 = this.reactionMentionCount != 0;
            boolean z16 = this.markUnread;
            this.hasUnmutedTopics = false;
            this.readOutboxMaxId = -1;
            if (this.isDialogCell) {
                TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                if (tLRPC$Dialog != null) {
                    this.readOutboxMaxId = tLRPC$Dialog.read_outbox_max_id;
                    this.ttlPeriod = tLRPC$Dialog.ttl_period;
                    if (i == 0) {
                        this.clearingDialog = MessagesController.getInstance(this.currentAccount).isClearingDialog(tLRPC$Dialog.id);
                        ArrayList arrayList = (ArrayList) MessagesController.getInstance(this.currentAccount).dialogMessage.get(tLRPC$Dialog.id);
                        this.groupMessages = arrayList;
                        MessageObject messageObject4 = (arrayList == null || arrayList.size() <= 0) ? null : (MessageObject) this.groupMessages.get(0);
                        this.message = messageObject4;
                        this.lastUnreadState = messageObject4 != null && messageObject4.isUnread();
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
                        MessageObject messageObject5 = this.message;
                        this.currentEditDate = messageObject5 != null ? messageObject5.messageOwner.edit_date : 0;
                        this.lastMessageDate = tLRPC$Dialog.last_message_date;
                        int i11 = this.dialogsType;
                        if (i11 == 7 || i11 == 8) {
                            MessagesController.DialogFilter dialogFilter = MessagesController.getInstance(this.currentAccount).selectedDialogFilter[this.dialogsType == 8 ? (char) 1 : (char) 0];
                            if (dialogFilter != null) {
                            }
                            z13 = false;
                        } else {
                            if (this.currentDialogFolderId == 0) {
                            }
                            z13 = false;
                        }
                        this.drawPin = z13;
                        MessageObject messageObject6 = this.message;
                        if (messageObject6 != null) {
                            this.lastSendState = messageObject6.messageOwner.send_state;
                        }
                    }
                } else {
                    this.unreadCount = 0;
                    this.mentionCount = 0;
                    this.reactionMentionCount = 0;
                    this.currentEditDate = 0;
                    this.lastMessageDate = 0;
                    this.clearingDialog = false;
                }
                long j2 = this.currentDialogId;
                this.drawAvatarSelector = j2 != 0 && j2 == RightSlidingDialogContainer.fragmentDialogId;
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
            DialogCellTags dialogCellTags = this.tags;
            if (dialogCellTags != null) {
                boolean isEmpty = dialogCellTags.isEmpty();
                z2 = isForumCell;
                if (this.tags.update(this.currentAccount, this.dialogsType, this.currentDialogId)) {
                    if (isEmpty != this.tags.isEmpty()) {
                        z3 = true;
                        z4 = true;
                    } else {
                        z3 = false;
                        z4 = false;
                    }
                    z5 = true;
                    if (i == 0) {
                        TLRPC$User tLRPC$User2 = this.user;
                        if (tLRPC$User2 != null && !MessagesController.isSupportUser(tLRPC$User2) && !this.user.bot && (i & MessagesController.UPDATE_MASK_STATUS) != 0) {
                            this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id));
                            if (this.wasDrawnOnline != isOnline()) {
                                z5 = true;
                            }
                        }
                        if ((i & MessagesController.UPDATE_MASK_EMOJI_STATUS) != 0) {
                            if (this.user != null) {
                                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id));
                                this.user = user;
                                if (user == null || DialogObject.getEmojiStatusDocumentId(user.emoji_status) == 0) {
                                    this.nameLayoutEllipsizeByGradient = true;
                                    this.emojiStatus.set(PremiumGradient.getInstance().premiumStarDrawableMini, z);
                                } else {
                                    this.nameLayoutEllipsizeByGradient = true;
                                    this.emojiStatus.set(DialogObject.getEmojiStatusDocumentId(this.user.emoji_status), z);
                                }
                                z5 = true;
                            }
                            if (this.chat != null) {
                                TLRPC$Chat chat4 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.id));
                                this.chat = chat4;
                                if (chat4 == null || DialogObject.getEmojiStatusDocumentId(chat4.emoji_status) == 0) {
                                    this.nameLayoutEllipsizeByGradient = true;
                                    this.emojiStatus.set(PremiumGradient.getInstance().premiumStarDrawableMini, z);
                                } else {
                                    this.nameLayoutEllipsizeByGradient = true;
                                    this.emojiStatus.set(DialogObject.getEmojiStatusDocumentId(this.chat.emoji_status), z);
                                }
                                z5 = true;
                            }
                        }
                        if ((this.isDialogCell || this.isTopic) && (i & MessagesController.UPDATE_MASK_USER_PRINT) != 0) {
                            CharSequence printingString = MessagesController.getInstance(this.currentAccount).getPrintingString(this.currentDialogId, getTopicId(), true);
                            CharSequence charSequence = this.lastPrintString;
                            if ((charSequence != null && printingString == null) || ((charSequence == null && printingString != null) || (charSequence != null && !charSequence.equals(printingString)))) {
                                z11 = true;
                                if (!z11 && (i & MessagesController.UPDATE_MASK_MESSAGE_TEXT) != 0 && (messageObject3 = this.message) != null && messageObject3.messageText != this.lastMessageString) {
                                    z11 = true;
                                }
                                if (!z11 && (i & MessagesController.UPDATE_MASK_CHAT) != 0 && this.chat != null) {
                                    chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.id));
                                    if ((chat2 == null && chat2.call_active && chat2.call_not_empty) != this.hasCall) {
                                        z11 = true;
                                    }
                                }
                                if (!z11 && (i & MessagesController.UPDATE_MASK_AVATAR) != 0 && this.chat == null) {
                                    z11 = true;
                                }
                                if (!z11 && (i & MessagesController.UPDATE_MASK_NAME) != 0 && this.chat == null) {
                                    z11 = true;
                                }
                                if (!z11 && (i & MessagesController.UPDATE_MASK_CHAT_AVATAR) != 0 && this.user == null) {
                                    z11 = true;
                                }
                                if (!z11 && (i & MessagesController.UPDATE_MASK_CHAT_NAME) != 0 && this.user == null) {
                                    z11 = true;
                                }
                                if (!z11) {
                                    MessageObject messageObject7 = this.message;
                                    if (messageObject7 != null && this.lastUnreadState != messageObject7.isUnread()) {
                                        this.lastUnreadState = this.message.isUnread();
                                        z11 = true;
                                    }
                                    if (this.isDialogCell) {
                                        TLRPC$Dialog tLRPC$Dialog2 = (TLRPC$Dialog) MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                                        TLRPC$Chat chat5 = tLRPC$Dialog2 == null ? null : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-tLRPC$Dialog2.id));
                                        if (chat5 == null || !chat5.forum) {
                                            z6 = z2;
                                            if (tLRPC$Dialog2 instanceof TLRPC$TL_dialogFolder) {
                                                i6 = MessagesStorage.getInstance(this.currentAccount).getArchiveUnreadCount();
                                                i5 = 0;
                                            } else if (tLRPC$Dialog2 != null) {
                                                i6 = tLRPC$Dialog2.unread_count;
                                                i7 = tLRPC$Dialog2.unread_mentions_count;
                                                i5 = tLRPC$Dialog2.unread_reactions_count;
                                            } else {
                                                i5 = 0;
                                                i6 = 0;
                                            }
                                            i7 = 0;
                                        } else {
                                            z6 = z2;
                                            int[] forumUnreadCount2 = MessagesController.getInstance(this.currentAccount).getTopicsController().getForumUnreadCount(chat5.id);
                                            i6 = forumUnreadCount2[0];
                                            i7 = forumUnreadCount2[1];
                                            int i12 = forumUnreadCount2[2];
                                            this.hasUnmutedTopics = forumUnreadCount2[3] != 0;
                                            i5 = i12;
                                        }
                                        if (tLRPC$Dialog2 != null && (this.unreadCount != i6 || this.markUnread != tLRPC$Dialog2.unread_mark || this.mentionCount != i7 || this.reactionMentionCount != i5)) {
                                            this.unreadCount = i6;
                                            this.mentionCount = i7;
                                            this.markUnread = tLRPC$Dialog2.unread_mark;
                                            this.reactionMentionCount = i5;
                                            z11 = true;
                                        }
                                        if (!z11 && (i & MessagesController.UPDATE_MASK_SEND_STATE) != 0 && (messageObject2 = this.message) != null) {
                                            i3 = this.lastSendState;
                                            i4 = messageObject2.messageOwner.send_state;
                                            if (i3 != i4) {
                                                this.lastSendState = i4;
                                                z12 = true;
                                                if (!z12) {
                                                    invalidate();
                                                    return z3;
                                                }
                                            }
                                        }
                                        z12 = z11;
                                        if (!z12) {
                                        }
                                    }
                                }
                                z6 = z2;
                                if (!z11) {
                                    i3 = this.lastSendState;
                                    i4 = messageObject2.messageOwner.send_state;
                                    if (i3 != i4) {
                                    }
                                }
                                z12 = z11;
                                if (!z12) {
                                }
                            }
                        }
                        z11 = false;
                        if (!z11) {
                            z11 = true;
                        }
                        if (!z11) {
                            chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.id));
                            if ((chat2 == null && chat2.call_active && chat2.call_not_empty) != this.hasCall) {
                            }
                        }
                        if (!z11) {
                            z11 = true;
                        }
                        if (!z11) {
                            z11 = true;
                        }
                        if (!z11) {
                            z11 = true;
                        }
                        if (!z11) {
                            z11 = true;
                        }
                        if (!z11) {
                        }
                        z6 = z2;
                        if (!z11) {
                        }
                        z12 = z11;
                        if (!z12) {
                        }
                    } else {
                        z6 = z2;
                    }
                    this.user = null;
                    this.chat = null;
                    this.encryptedChat = null;
                    if (this.currentDialogFolderId == 0) {
                        this.dialogMuted = false;
                        this.drawUnmute = false;
                        MessageObject findFolderTopMessage = findFolderTopMessage();
                        this.message = findFolderTopMessage;
                        j = findFolderTopMessage != null ? findFolderTopMessage.getDialogId() : 0L;
                    } else {
                        this.drawUnmute = false;
                        if (this.forumTopic != null) {
                            boolean isDialogMuted = MessagesController.getInstance(this.currentAccount).isDialogMuted(this.currentDialogId, 0L);
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
                            this.dialogMuted = this.isDialogCell && MessagesController.getInstance(this.currentAccount).isDialogMuted(this.currentDialogId, (long) getTopicId());
                        }
                        j = this.currentDialogId;
                    }
                    if (j != 0) {
                        if (DialogObject.isEncryptedDialog(j)) {
                            TLRPC$EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j)));
                            this.encryptedChat = encryptedChat;
                            if (encryptedChat != null) {
                                messagesController = MessagesController.getInstance(this.currentAccount);
                                j = this.encryptedChat.user_id;
                                this.user = messagesController.getUser(Long.valueOf(j));
                            }
                            if (this.useMeForMyMessages && this.user != null && this.message.isOutOwner()) {
                                this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(UserConfig.getInstance(this.currentAccount).clientUserId));
                            }
                        } else if (DialogObject.isUserDialog(j)) {
                            messagesController = MessagesController.getInstance(this.currentAccount);
                            this.user = messagesController.getUser(Long.valueOf(j));
                            if (this.useMeForMyMessages) {
                                this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(UserConfig.getInstance(this.currentAccount).clientUserId));
                            }
                        } else {
                            TLRPC$Chat chat6 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
                            this.chat = chat6;
                            if (!this.isDialogCell && chat6 != null && chat6.migrated_to != null && (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.migrated_to.channel_id))) != null) {
                                this.chat = chat;
                            }
                            if (this.useMeForMyMessages) {
                            }
                        }
                    }
                    if (this.currentDialogFolderId == 0) {
                        Theme.dialogs_archiveAvatarDrawable.setCallback(this);
                        avatarDrawable = this.avatarDrawable;
                        i2 = 2;
                    } else {
                        if (!this.useFromUserAsAvatar || (messageObject = this.message) == null) {
                            TLRPC$User tLRPC$User3 = this.user;
                            if (tLRPC$User3 != null) {
                                this.avatarDrawable.setInfo(this.currentAccount, tLRPC$User3);
                                if (UserObject.isReplyUser(this.user)) {
                                    avatarDrawable = this.avatarDrawable;
                                    i2 = 12;
                                } else if (UserObject.isAnonymous(this.user)) {
                                    avatarDrawable = this.avatarDrawable;
                                    i2 = 21;
                                } else if (UserObject.isUserSelf(this.user) && this.isSavedDialog) {
                                    avatarDrawable = this.avatarDrawable;
                                    i2 = 22;
                                } else if (!UserObject.isUserSelf(this.user) || this.useMeForMyMessages) {
                                    this.avatarImage.setForUserOrChat(this.user, this.avatarDrawable, null, true, 1, false);
                                } else {
                                    avatarDrawable = this.avatarDrawable;
                                    i2 = 1;
                                }
                            } else {
                                TLRPC$Chat tLRPC$Chat = this.chat;
                                if (tLRPC$Chat != null) {
                                    this.avatarDrawable.setInfo(this.currentAccount, tLRPC$Chat);
                                    imageReceiver = this.avatarImage;
                                    tLObject = this.chat;
                                }
                            }
                            if (z || ((i10 == this.unreadCount && z16 == this.markUnread) || (this.isDialogCell && System.currentTimeMillis() - this.lastDialogChangedTime <= 100))) {
                                z7 = z3;
                                z8 = z4;
                            } else {
                                ValueAnimator valueAnimator3 = this.countAnimator;
                                if (valueAnimator3 != null) {
                                    valueAnimator3.cancel();
                                }
                                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                                this.countAnimator = ofFloat;
                                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda0
                                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                    public final void onAnimationUpdate(ValueAnimator valueAnimator4) {
                                        DialogCell.this.lambda$update$0(valueAnimator4);
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
                                if ((i10 == 0 || this.markUnread) && (this.markUnread || !z16)) {
                                    this.countAnimator.setDuration(220L);
                                    valueAnimator2 = this.countAnimator;
                                    overshootInterpolator = new OvershootInterpolator();
                                } else {
                                    if (this.unreadCount == 0) {
                                        this.countAnimator.setDuration(150L);
                                    } else {
                                        this.countAnimator.setDuration(430L);
                                    }
                                    valueAnimator2 = this.countAnimator;
                                    overshootInterpolator = CubicBezierInterpolator.DEFAULT;
                                }
                                valueAnimator2.setInterpolator(overshootInterpolator);
                                if (this.drawCount && this.drawCount2 && this.countLayout != null) {
                                    String format = String.format("%d", Integer.valueOf(i10));
                                    String format2 = String.format("%d", Integer.valueOf(this.unreadCount));
                                    if (format.length() == format2.length()) {
                                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(format);
                                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(format2);
                                        SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(format2);
                                        int i13 = 0;
                                        while (i13 < format.length()) {
                                            if (format.charAt(i13) == format2.charAt(i13)) {
                                                z9 = z3;
                                                int i14 = i13 + 1;
                                                spannableStringBuilder.setSpan(new EmptyStubSpan(), i13, i14, 0);
                                                spannableStringBuilder2.setSpan(new EmptyStubSpan(), i13, i14, 0);
                                                z10 = z4;
                                            } else {
                                                z9 = z3;
                                                z10 = z4;
                                                spannableStringBuilder3.setSpan(new EmptyStubSpan(), i13, i13 + 1, 0);
                                            }
                                            i13++;
                                            z3 = z9;
                                            z4 = z10;
                                        }
                                        z7 = z3;
                                        z8 = z4;
                                        int max = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(format)));
                                        TextPaint textPaint = Theme.dialogs_countTextPaint;
                                        Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
                                        this.countOldLayout = new StaticLayout(spannableStringBuilder, textPaint, max, alignment, 1.0f, 0.0f, false);
                                        this.countAnimationStableLayout = new StaticLayout(spannableStringBuilder3, Theme.dialogs_countTextPaint, max, alignment, 1.0f, 0.0f, false);
                                        this.countAnimationInLayout = new StaticLayout(spannableStringBuilder2, Theme.dialogs_countTextPaint, max, alignment, 1.0f, 0.0f, false);
                                    } else {
                                        z7 = z3;
                                        z8 = z4;
                                        this.countOldLayout = this.countLayout;
                                    }
                                } else {
                                    z7 = z3;
                                    z8 = z4;
                                }
                                this.countWidthOld = this.countWidth;
                                this.countLeftOld = this.countLeft;
                                this.countAnimationIncrement = this.unreadCount > i10;
                                this.countAnimator.start();
                            }
                            boolean z17 = this.reactionMentionCount == 0;
                            if (!z && z17 != z15) {
                                ValueAnimator valueAnimator4 = this.reactionsMentionsAnimator;
                                if (valueAnimator4 != null) {
                                    valueAnimator4.cancel();
                                }
                                this.reactionsMentionsChangeProgress = 0.0f;
                                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                                this.reactionsMentionsAnimator = ofFloat2;
                                ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda1
                                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                    public final void onAnimationUpdate(ValueAnimator valueAnimator5) {
                                        DialogCell.this.lambda$update$1(valueAnimator5);
                                    }
                                });
                                this.reactionsMentionsAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.4
                                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                    public void onAnimationEnd(Animator animator) {
                                        DialogCell.this.reactionsMentionsChangeProgress = 1.0f;
                                        DialogCell.this.invalidate();
                                    }
                                });
                                ValueAnimator valueAnimator5 = this.reactionsMentionsAnimator;
                                if (z17) {
                                    valueAnimator5.setDuration(220L);
                                    valueAnimator = this.reactionsMentionsAnimator;
                                    timeInterpolator = new OvershootInterpolator();
                                } else {
                                    valueAnimator5.setDuration(150L);
                                    valueAnimator = this.reactionsMentionsAnimator;
                                    timeInterpolator = CubicBezierInterpolator.DEFAULT;
                                }
                                valueAnimator.setInterpolator(timeInterpolator);
                                this.reactionsMentionsAnimator.start();
                            }
                            ImageReceiver imageReceiver2 = this.avatarImage;
                            TLRPC$Chat tLRPC$Chat2 = this.chat;
                            imageReceiver2.setRoundRadius(AndroidUtilities.dp((!(tLRPC$Chat2 == null && tLRPC$Chat2.forum && this.currentDialogFolderId == 0 && !this.useFromUserAsAvatar) && (this.isSavedDialog || (tLRPC$User = this.user) == null || !tLRPC$User.self || !MessagesController.getInstance(this.currentAccount).savedViewAsChats)) ? 28.0f : 16.0f));
                            z14 = z5;
                        } else {
                            this.avatarDrawable.setInfo(this.currentAccount, messageObject.getFromPeerObject());
                            imageReceiver = this.avatarImage;
                            tLObject = this.message.getFromPeerObject();
                        }
                        imageReceiver.setForUserOrChat(tLObject, this.avatarDrawable);
                        if (z) {
                        }
                        z7 = z3;
                        z8 = z4;
                        if (this.reactionMentionCount == 0) {
                        }
                        if (!z) {
                        }
                        ImageReceiver imageReceiver22 = this.avatarImage;
                        TLRPC$Chat tLRPC$Chat22 = this.chat;
                        imageReceiver22.setRoundRadius(AndroidUtilities.dp((!(tLRPC$Chat22 == null && tLRPC$Chat22.forum && this.currentDialogFolderId == 0 && !this.useFromUserAsAvatar) && (this.isSavedDialog || (tLRPC$User = this.user) == null || !tLRPC$User.self || !MessagesController.getInstance(this.currentAccount).savedViewAsChats)) ? 28.0f : 16.0f));
                        z14 = z5;
                    }
                    avatarDrawable.setAvatarType(i2);
                    this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
                    if (z) {
                    }
                    z7 = z3;
                    z8 = z4;
                    if (this.reactionMentionCount == 0) {
                    }
                    if (!z) {
                    }
                    ImageReceiver imageReceiver222 = this.avatarImage;
                    TLRPC$Chat tLRPC$Chat222 = this.chat;
                    imageReceiver222.setRoundRadius(AndroidUtilities.dp((!(tLRPC$Chat222 == null && tLRPC$Chat222.forum && this.currentDialogFolderId == 0 && !this.useFromUserAsAvatar) && (this.isSavedDialog || (tLRPC$User = this.user) == null || !tLRPC$User.self || !MessagesController.getInstance(this.currentAccount).savedViewAsChats)) ? 28.0f : 16.0f));
                    z14 = z5;
                }
            } else {
                z2 = isForumCell;
            }
            z3 = false;
            z4 = false;
            z5 = false;
            if (i == 0) {
            }
            this.user = null;
            this.chat = null;
            this.encryptedChat = null;
            if (this.currentDialogFolderId == 0) {
            }
            if (j != 0) {
            }
            if (this.currentDialogFolderId == 0) {
            }
            avatarDrawable.setAvatarType(i2);
            this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
            if (z) {
            }
            z7 = z3;
            z8 = z4;
            if (this.reactionMentionCount == 0) {
            }
            if (!z) {
            }
            ImageReceiver imageReceiver2222 = this.avatarImage;
            TLRPC$Chat tLRPC$Chat2222 = this.chat;
            imageReceiver2222.setRoundRadius(AndroidUtilities.dp((!(tLRPC$Chat2222 == null && tLRPC$Chat2222.forum && this.currentDialogFolderId == 0 && !this.useFromUserAsAvatar) && (this.isSavedDialog || (tLRPC$User = this.user) == null || !tLRPC$User.self || !MessagesController.getInstance(this.currentAccount).savedViewAsChats)) ? 28.0f : 16.0f));
            z14 = z5;
        }
        if (!this.isTopic && (getMeasuredWidth() != 0 || getMeasuredHeight() != 0)) {
            z8 = true;
        }
        if (!z14) {
            int i15 = this.storyParams.currentState;
            StoriesUtilities.getPredictiveUnreadState(MessagesController.getInstance(this.currentAccount).getStoriesController(), getDialogId());
        }
        if (!z) {
            this.dialogMutedProgress = (this.dialogMuted || this.drawUnmute) ? 1.0f : 0.0f;
            ValueAnimator valueAnimator6 = this.countAnimator;
            if (valueAnimator6 != null) {
                valueAnimator6.cancel();
            }
        }
        invalidate();
        if (isForumCell() != z6) {
            z7 = true;
        }
        if (z8) {
            if (this.attachedToWindow) {
                buildLayout();
            } else {
                this.updateLayout = true;
            }
        }
        updatePremiumBlocked(z);
        return z7;
    }

    public void updateMessageThumbs() {
        TLRPC$Message tLRPC$Message;
        int i;
        MessageObject messageObject = this.message;
        if (messageObject == null) {
            return;
        }
        String restrictionReason = MessagesController.getInstance(messageObject.currentAccount).getRestrictionReason(this.message.messageOwner.restriction_reason);
        MessageObject messageObject2 = this.message;
        int i2 = 0;
        if (messageObject2 != null && (tLRPC$Message = messageObject2.messageOwner) != null) {
            TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) {
                this.thumbsCount = 0;
                this.hasVideoThumb = false;
                TLRPC$TL_messageMediaPaidMedia tLRPC$TL_messageMediaPaidMedia = (TLRPC$TL_messageMediaPaidMedia) tLRPC$MessageMedia;
                int i3 = 0;
                while (i2 < tLRPC$TL_messageMediaPaidMedia.extended_media.size() && this.thumbsCount < 3) {
                    TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia = tLRPC$TL_messageMediaPaidMedia.extended_media.get(i2);
                    if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMediaPreview) {
                        i = i3 + 1;
                        setThumb(i3, ((TLRPC$TL_messageExtendedMediaPreview) tLRPC$MessageExtendedMedia).thumb);
                    } else if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMedia) {
                        i = i3 + 1;
                        setThumb(i3, ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia).media);
                    } else {
                        i2++;
                    }
                    i3 = i;
                    i2++;
                }
                return;
            }
        }
        ArrayList arrayList = this.groupMessages;
        if (arrayList != null && arrayList.size() > 1 && TextUtils.isEmpty(restrictionReason) && this.currentDialogFolderId == 0 && this.encryptedChat == null) {
            this.thumbsCount = 0;
            this.hasVideoThumb = false;
            Collections.sort(this.groupMessages, Comparator$-CC.comparingInt(new ToIntFunction() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda4
                @Override // j$.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((MessageObject) obj).getId();
                }
            }));
            while (i2 < Math.min(3, this.groupMessages.size())) {
                MessageObject messageObject3 = (MessageObject) this.groupMessages.get(i2);
                if (messageObject3 != null && !messageObject3.needDrawBluredPreview() && (messageObject3.isPhoto() || messageObject3.isNewGif() || messageObject3.isVideo() || messageObject3.isRoundVideo() || messageObject3.isStoryMedia())) {
                    String str = messageObject3.isWebpage() ? messageObject3.messageOwner.media.webpage.type : null;
                    if (!"app".equals(str) && !"profile".equals(str) && !"article".equals(str) && (str == null || !str.startsWith("telegram_"))) {
                        setThumb(i2, messageObject3);
                    }
                }
                i2++;
            }
            return;
        }
        MessageObject messageObject4 = this.message;
        if (messageObject4 == null || this.currentDialogFolderId != 0) {
            return;
        }
        this.thumbsCount = 0;
        this.hasVideoThumb = false;
        if (messageObject4.needDrawBluredPreview()) {
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
}
