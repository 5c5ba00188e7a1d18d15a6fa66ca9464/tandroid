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
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DialogsAdapter;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BubbleCounterPath;
import org.telegram.ui.Components.ButtonBounce;
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
import org.telegram.ui.Components.Text;
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
    private boolean allowBotOpenButton;
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
    private TLRPC.Chat chat;
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
    private TLRPC.DraftMessage draftMessage;
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
    private boolean drawGiftIcon;
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
    private TLRPC.EncryptedChat encryptedChat;
    private int errorLeft;
    private int errorTop;
    private Paint fadePaint;
    private Paint fadePaintBack;
    private int folderId;
    protected boolean forbidDraft;
    protected boolean forbidVerified;
    public TLRPC.TL_forumTopic forumTopic;
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
    private Utilities.Callback onOpenButtonClick;
    private float onlineProgress;
    private boolean openBot;
    private final Paint openButtonBackgroundPaint;
    private final ButtonBounce openButtonBounce;
    private final RectF openButtonRect;
    private Text openButtonText;
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
    private TLRPC.User user;
    private boolean visibleOnScreen;
    private boolean wasDrawnOnline;
    protected float xOffset;

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

    public interface DialogCellDelegate {
        boolean canClickButtonInside();

        void onButtonClicked(DialogCell dialogCell);

        void onButtonLongPress(DialogCell dialogCell);

        void openHiddenStories();

        void openStory(DialogCell dialogCell, Runnable runnable);

        void showChatPreview(DialogCell dialogCell);
    }

    private class DialogUpdateHelper {
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

        /* JADX WARN: Code restructure failed: missing block: B:39:0x0130, code lost:
        
            if (org.telegram.messenger.MessagesController.getInstance(r19.this$0.currentAccount).getTopicsController().endIsReached(-r19.this$0.currentDialogId) != false) goto L46;
         */
        /* JADX WARN: Removed duplicated region for block: B:107:0x02a8  */
        /* JADX WARN: Removed duplicated region for block: B:112:0x025c  */
        /* JADX WARN: Removed duplicated region for block: B:114:0x01c5  */
        /* JADX WARN: Removed duplicated region for block: B:122:0x0187  */
        /* JADX WARN: Removed duplicated region for block: B:34:0x00f4  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x013b  */
        /* JADX WARN: Removed duplicated region for block: B:52:0x01c3  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x01dd  */
        /* JADX WARN: Removed duplicated region for block: B:62:0x0210  */
        /* JADX WARN: Removed duplicated region for block: B:87:0x0269  */
        /* JADX WARN: Removed duplicated region for block: B:91:0x02ac  */
        /* JADX WARN: Removed duplicated region for block: B:95:0x0275  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean update() {
            Integer num;
            int i;
            boolean z;
            TLRPC.DraftMessage draft;
            int i2;
            int i3;
            boolean z2;
            TLRPC.Dialog dialog = (TLRPC.Dialog) MessagesController.getInstance(DialogCell.this.currentAccount).dialogs_dict.get(DialogCell.this.currentDialogId);
            if (dialog == null) {
                if (DialogCell.this.dialogsType != 3 || this.lastDrawnDialogId == DialogCell.this.currentDialogId) {
                    return false;
                }
                this.lastDrawnDialogId = DialogCell.this.currentDialogId;
                return true;
            }
            int id = DialogCell.this.message == null ? 0 : DialogCell.this.message.getId() + DialogCell.this.message.hashCode();
            long j = dialog.read_inbox_max_id + (dialog.read_outbox_max_id << 8) + ((dialog.unread_count + (dialog.unread_mark ? -1 : 0)) << 16) + (dialog.unread_reactions_count > 0 ? 262144 : 0) + (dialog.unread_mentions_count > 0 ? 524288 : 0);
            if (!DialogCell.this.isForumCell()) {
                DialogCell dialogCell = DialogCell.this;
                if ((dialogCell.isDialogCell || dialogCell.isTopic) && !TextUtils.isEmpty(MessagesController.getInstance(DialogCell.this.currentAccount).getPrintingString(DialogCell.this.currentDialogId, DialogCell.this.getTopicId(), true))) {
                    num = MessagesController.getInstance(DialogCell.this.currentAccount).getPrintingStringType(DialogCell.this.currentDialogId, DialogCell.this.getTopicId());
                    int measuredWidth = DialogCell.this.getMeasuredWidth() + (DialogCell.this.getMeasuredHeight() << 16);
                    if (DialogCell.this.isForumCell()) {
                        ArrayList<TLRPC.TL_forumTopic> topics = MessagesController.getInstance(DialogCell.this.currentAccount).getTopicsController().getTopics(-DialogCell.this.currentDialogId);
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
                                    this.lastDrawnDialogIsFolder = dialog.isFolder;
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
                            this.lastDrawnDialogIsFolder = dialog.isFolder;
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
                        TLRPC.DraftMessage draft2 = !z ? MediaDataController.getInstance(DialogCell.this.currentAccount).getDraft(DialogCell.this.currentDialogId, DialogCell.this.getTopicId()) : null;
                        if (draft2 == null || !TextUtils.isEmpty(draft2.message)) {
                            draft = draft2;
                            if (draft != null) {
                                i2 = 0;
                            } else {
                                int hashCode = draft.message.hashCode();
                                TLRPC.InputReplyTo inputReplyTo = draft.reply_to;
                                i2 = hashCode + (inputReplyTo != null ? inputReplyTo.reply_to_msg_id << 16 : 0);
                            }
                            boolean z3 = DialogCell.this.chat == null && DialogCell.this.chat.call_active && DialogCell.this.chat.call_not_empty;
                            boolean isTranslatingDialog22 = MessagesController.getInstance(DialogCell.this.currentAccount).getTranslateController().isTranslatingDialog(DialogCell.this.currentDialogId);
                            if (this.lastDrawnSizeHash != measuredWidth) {
                                i3 = measuredWidth;
                                if (this.lastDrawnMessageId == id && this.lastDrawnTranslated == isTranslatingDialog22 && this.lastDrawnDialogId == DialogCell.this.currentDialogId && this.lastDrawnDialogIsFolder == dialog.isFolder && this.lastDrawnReadState == j && Objects.equals(this.lastDrawnPrintingType, num) && this.lastTopicsCount == i && i2 == this.lastDrawnDraftHash && this.lastDrawnPinned == DialogCell.this.drawPin && this.lastDrawnHasCall == z3 && DialogCell.this.draftVoice == z) {
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
                            this.lastDrawnDialogIsFolder = dialog.isFolder;
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
                        this.lastDrawnDialogIsFolder = dialog.isFolder;
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
    static class ForumFormattedNames {
        CharSequence formattedNames;
        boolean isLoadingState;
        int lastMessageId;
        boolean lastTopicMessageUnread;
        int topMessageTopicEndIndex;
        int topMessageTopicStartIndex;

        private ForumFormattedNames() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:32:0x009d  */
        /* JADX WARN: Removed duplicated region for block: B:53:0x00b9  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void formatTopicsNames(int i, MessageObject messageObject, TLRPC.Chat chat) {
            long j;
            boolean z;
            int i2;
            int id = (messageObject == null || chat == null) ? 0 : messageObject.getId();
            if (this.lastMessageId != id || this.isLoadingState) {
                this.topMessageTopicStartIndex = 0;
                this.topMessageTopicEndIndex = 0;
                this.lastTopicMessageUnread = false;
                this.isLoadingState = false;
                this.lastMessageId = id;
                TextPaint textPaint = Theme.dialogs_messagePaint[0];
                if (chat != null) {
                    ArrayList<TLRPC.TL_forumTopic> topics = MessagesController.getInstance(i).getTopicsController().getTopics(chat.id);
                    boolean z2 = true;
                    if (topics == null || topics.isEmpty()) {
                        if (MessagesController.getInstance(i).getTopicsController().endIsReached(chat.id)) {
                            this.formattedNames = "no created topics";
                            return;
                        }
                        MessagesController.getInstance(i).getTopicsController().preloadTopics(chat.id);
                        this.formattedNames = LocaleController.getString(R.string.Loading);
                        this.isLoadingState = true;
                        return;
                    }
                    ArrayList arrayList = new ArrayList(topics);
                    Collections.sort(arrayList, Comparator$-CC.comparingInt(new ToIntFunction() { // from class: org.telegram.ui.Cells.DialogCell$ForumFormattedNames$$ExternalSyntheticLambda0
                        @Override // j$.util.function.ToIntFunction
                        public final int applyAsInt(Object obj) {
                            int lambda$formatTopicsNames$0;
                            lambda$formatTopicsNames$0 = DialogCell.ForumFormattedNames.lambda$formatTopicsNames$0((TLRPC.TL_forumTopic) obj);
                            return lambda$formatTopicsNames$0;
                        }
                    }));
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    if (messageObject != null) {
                        j = MessageObject.getTopicId(i, messageObject.messageOwner, true);
                        TLRPC.TL_forumTopic findTopic = MessagesController.getInstance(i).getTopicsController().findTopic(chat.id, j);
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
                        if (((TLRPC.TL_forumTopic) arrayList.get(i3)).id != j) {
                            if (spannableStringBuilder.length() != 0) {
                                if (z2 && z) {
                                    spannableStringBuilder.append((CharSequence) " ");
                                } else {
                                    spannableStringBuilder.append((CharSequence) ", ");
                                }
                            }
                            spannableStringBuilder.append(ForumUtilities.getTopicSpannedName((TLRPC.ForumTopic) arrayList.get(i3), textPaint, false));
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
        public static /* synthetic */ int lambda$formatTopicsNames$0(TLRPC.TL_forumTopic tL_forumTopic) {
            return -tL_forumTopic.top_message;
        }
    }

    public static class SharedResources {
    }

    public DialogCell(DialogsActivity dialogsActivity, Context context, boolean z, boolean z2) {
        this(dialogsActivity, context, z, z2, UserConfig.selectedAccount, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
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
        StoriesUtilities.AvatarStoryParams avatarStoryParams = new StoriesUtilities.AvatarStoryParams(0 == true ? 1 : 0) { // from class: org.telegram.ui.Cells.DialogCell.1
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
        this.openButtonBounce = new ButtonBounce(this);
        this.openButtonBackgroundPaint = new Paint(1);
        this.openButtonRect = new RectF();
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
        if (this.thumbsCount <= 0) {
            return charSequence;
        }
        SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(charSequence);
        valueOf.insert(0, (CharSequence) " ");
        valueOf.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbSize + 2) * this.thumbsCount) + 3)), 0, 1, 33);
        return valueOf;
    }

    private void checkChatTheme() {
        TLRPC.Message message;
        MessageObject messageObject = this.message;
        if (messageObject == null || (message = messageObject.messageOwner) == null) {
            return;
        }
        TLRPC.MessageAction messageAction = message.action;
        if ((messageAction instanceof TLRPC.TL_messageActionSetChatTheme) && this.lastUnreadState) {
            ChatThemeController.getInstance(this.currentAccount).setDialogTheme(this.currentDialogId, ((TLRPC.TL_messageActionSetChatTheme) messageAction).emoticon, false);
        }
    }

    private void checkGroupCall() {
        TLRPC.Chat chat = this.chat;
        boolean z = chat != null && chat.call_active && chat.call_not_empty;
        this.hasCall = z;
        this.chatCallProgress = z ? 1.0f : 0.0f;
    }

    private void checkOnline() {
        TLRPC.User user;
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
                    Theme.dialogs_clockDrawable.setAlpha(NotificationCenter.newLocationAvailable);
                }
                invalidate();
                return;
            }
            if (z3) {
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
                        Theme.dialogs_halfCheckDrawable.setAlpha(NotificationCenter.newLocationAvailable);
                    }
                    if (z4 || f == 0.0f) {
                        return;
                    }
                    canvas.restore();
                    Theme.dialogs_halfCheckDrawable.setAlpha(NotificationCenter.newLocationAvailable);
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
                drawable.setAlpha(NotificationCenter.newLocationAvailable);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:69:0x025b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void drawCounter(Canvas canvas, boolean z, int i, int i2, int i3, float f, boolean z2) {
        Paint paint;
        boolean z3;
        RectF rectF;
        float f2;
        float interpolation;
        StaticLayout staticLayout;
        RectF rectF2;
        boolean z4 = isForumCell() || isFolderCell();
        if (!(this.drawCount && this.drawCount2) && this.countChangeProgress == 1.0f) {
            return;
        }
        float f3 = (this.unreadCount != 0 || this.markUnread) ? this.countChangeProgress : 1.0f - this.countChangeProgress;
        int i4 = NotificationCenter.newLocationAvailable;
        if (z2) {
            if (this.counterPaintOutline == null) {
                Paint paint2 = new Paint();
                this.counterPaintOutline = paint2;
                paint2.setStyle(Paint.Style.STROKE);
                this.counterPaintOutline.setStrokeWidth(AndroidUtilities.dp(2.0f));
                this.counterPaintOutline.setStrokeJoin(Paint.Join.ROUND);
                this.counterPaintOutline.setStrokeCap(Paint.Cap.ROUND);
            }
            this.counterPaintOutline.setColor(ColorUtils.blendARGB(Theme.getColor(Theme.key_windowBackgroundWhite), ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_chats_pinnedOverlay), NotificationCenter.newLocationAvailable), Color.alpha(r13) / 255.0f));
        }
        if (this.isTopic && this.forumTopic.read_inbox_max_id == 0) {
            if (this.topicCounterPaint == null) {
                this.topicCounterPaint = new Paint();
            }
            paint = this.topicCounterPaint;
            int color = Theme.getColor(z ? Theme.key_topics_unreadCounterMuted : Theme.key_topics_unreadCounter, this.resourcesProvider);
            paint.setColor(color);
            Theme.dialogs_countTextPaint.setColor(color);
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
            this.rect.set(i2 - AndroidUtilities.dp(5.5f), i, r9 + this.countWidth + AndroidUtilities.dp(11.0f), AndroidUtilities.dp(23.0f) + i);
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
            float dp = f8 - AndroidUtilities.dp(5.5f);
            float f9 = i;
            this.rect.set(dp, f9, (this.countWidth * f6) + dp + (this.countWidthOld * f7) + AndroidUtilities.dp(11.0f), AndroidUtilities.dp(23.0f) + i);
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
            TLRPC.Dialog dialog = (TLRPC.Dialog) dialogsArray.get(i);
            LongSparseArray longSparseArray = MessagesController.getInstance(this.currentAccount).dialogMessage;
            if (longSparseArray != null) {
                ArrayList arrayList = (ArrayList) longSparseArray.get(dialog.id);
                MessageObject messageObject2 = (arrayList == null || arrayList.isEmpty()) ? null : (MessageObject) arrayList.get(0);
                if (messageObject2 != null && (messageObject == null || messageObject2.messageOwner.date > messageObject.messageOwner.date)) {
                    messageObject = messageObject2;
                }
                if (dialog.pinnedNum == 0 && messageObject != null) {
                    break;
                }
            }
        }
        return messageObject;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00d4 A[EDGE_INSN: B:23:0x00d4->B:24:0x00d4 BREAK  A[LOOP:0: B:2:0x001d->B:34:0x00d0], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00d0 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0079  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private CharSequence formatArchivedDialogNames() {
        long j;
        TLRPC.User user;
        String string;
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        ArrayList<TLRPC.Dialog> dialogs = messagesController.getDialogs(this.currentDialogFolderId);
        this.currentDialogFolderDialogsCount = dialogs.size();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        int size = dialogs.size();
        for (int i = 0; i < size; i++) {
            TLRPC.Dialog dialog = dialogs.get(i);
            if (!messagesController.isHiddenByUndo(dialog.id)) {
                boolean isEncryptedDialog = DialogObject.isEncryptedDialog(dialog.id);
                TLRPC.Chat chat = null;
                long j2 = dialog.id;
                if (isEncryptedDialog) {
                    TLRPC.EncryptedChat encryptedChat = messagesController.getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j2)));
                    if (encryptedChat != null) {
                        j = encryptedChat.user_id;
                        user = messagesController.getUser(Long.valueOf(j));
                        if (chat == null) {
                            string = chat.title.replace('\n', ' ');
                        } else if (user == null) {
                            continue;
                        } else {
                            string = UserObject.isDeleted(user) ? LocaleController.getString(R.string.HiddenName) : AndroidUtilities.removeDiacritics(ContactsController.formatName(user.first_name, user.last_name).replace('\n', ' '));
                        }
                        if (spannableStringBuilder.length() > 0) {
                            spannableStringBuilder.append((CharSequence) ", ");
                        }
                        int length = spannableStringBuilder.length();
                        int length2 = string.length() + length;
                        spannableStringBuilder.append((CharSequence) string);
                        if (dialog.unread_count > 0) {
                            spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.bold(), 0, Theme.getColor(Theme.key_chats_nameArchived, this.resourcesProvider)), length, length2, 33);
                        }
                        if (spannableStringBuilder.length() <= 150) {
                            break;
                        }
                    } else {
                        user = null;
                        if (chat == null) {
                        }
                        if (spannableStringBuilder.length() > 0) {
                        }
                        int length3 = spannableStringBuilder.length();
                        int length22 = string.length() + length3;
                        spannableStringBuilder.append((CharSequence) string);
                        if (dialog.unread_count > 0) {
                        }
                        if (spannableStringBuilder.length() <= 150) {
                        }
                    }
                } else {
                    boolean isUserDialog = DialogObject.isUserDialog(j2);
                    j = dialog.id;
                    if (!isUserDialog) {
                        chat = messagesController.getChat(Long.valueOf(-j));
                        user = null;
                        if (chat == null) {
                        }
                        if (spannableStringBuilder.length() > 0) {
                        }
                        int length32 = spannableStringBuilder.length();
                        int length222 = string.length() + length32;
                        spannableStringBuilder.append((CharSequence) string);
                        if (dialog.unread_count > 0) {
                        }
                        if (spannableStringBuilder.length() <= 150) {
                        }
                    }
                    user = messagesController.getUser(Long.valueOf(j));
                    if (chat == null) {
                    }
                    if (spannableStringBuilder.length() > 0) {
                    }
                    int length322 = spannableStringBuilder.length();
                    int length2222 = string.length() + length322;
                    spannableStringBuilder.append((CharSequence) string);
                    if (dialog.unread_count > 0) {
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
        } else {
            if (i != 2) {
                if (i == 3) {
                    spannableStringBuilder.append(charSequence2).append((CharSequence) ": ").append(charSequence);
                } else if (i == 4) {
                    spannableStringBuilder.append(charSequence);
                }
                return spannableStringBuilder;
            }
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
        if (!hasTags()) {
            return dp;
        }
        if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !isForumCell()) {
            return dp;
        }
        return dp + AndroidUtilities.dp(isForumCell() ? this.addForumHeightForTags : this.addHeightForTags);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTopicId() {
        TLRPC.TL_forumTopic tL_forumTopic = this.forumTopic;
        if (tL_forumTopic == null) {
            return 0;
        }
        return tL_forumTopic.id;
    }

    private boolean isOnline() {
        TLRPC.User user;
        if (!isForumCell() && (user = this.user) != null && !user.self) {
            TLRPC.UserStatus userStatus = user.status;
            if (userStatus != null && userStatus.expires <= 0 && MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(Long.valueOf(this.user.id))) {
                return true;
            }
            TLRPC.UserStatus userStatus2 = this.user.status;
            if (userStatus2 != null && userStatus2.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
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
        TLRPC.MessageMedia messageMedia;
        ArrayList<TLRPC.PhotoSize> arrayList = messageObject.photoThumbs;
        TLObject tLObject = messageObject.photoThumbsObject;
        if (messageObject.isStoryMedia()) {
            TL_stories.StoryItem storyItem = messageObject.messageOwner.media.storyItem;
            if (storyItem == null || (messageMedia = storyItem.media) == null) {
                return;
            }
            TLRPC.Document document = messageMedia.document;
            if (document != null) {
                arrayList = document.thumbs;
                tLObject = document;
            } else {
                TLRPC.Photo photo = messageMedia.photo;
                if (photo != null) {
                    arrayList = photo.sizes;
                    tLObject = photo;
                }
            }
        }
        TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, 40);
        TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize(), false, null, true);
        TLRPC.PhotoSize photoSize = closestPhotoSizeWithSize != closestPhotoSizeWithSize2 ? closestPhotoSizeWithSize2 : null;
        if (photoSize == null || DownloadController.getInstance(this.currentAccount).canDownloadMedia(messageObject.messageOwner) == 0) {
            photoSize = closestPhotoSizeWithSize;
        }
        if (closestPhotoSizeWithSize != null) {
            this.hasVideoThumb = this.hasVideoThumb || messageObject.isVideo() || messageObject.isRoundVideo();
            int i2 = this.thumbsCount;
            if (i2 < 3) {
                this.thumbsCount = i2 + 1;
                this.drawPlay[i] = (messageObject.isVideo() || messageObject.isRoundVideo()) && !messageObject.hasMediaSpoilers();
                this.drawSpoiler[i] = messageObject.hasMediaSpoilers();
                int i3 = (messageObject.type != 1 || photoSize == null) ? 0 : photoSize.size;
                String str = messageObject.hasMediaSpoilers() ? "5_5_b" : "20_20";
                this.thumbImage[i].setImage(ImageLocation.getForObject(photoSize, tLObject), str, ImageLocation.getForObject(closestPhotoSizeWithSize, tLObject), str, i3, null, messageObject, 0);
                this.thumbImage[i].setRoundRadius(AndroidUtilities.dp(messageObject.isRoundVideo() ? 18.0f : 2.0f));
                this.needEmoji = false;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:12:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0036  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setThumb(int i, TLRPC.MessageMedia messageMedia) {
        TLRPC.Document document;
        ArrayList<TLRPC.PhotoSize> arrayList;
        boolean isVideoDocument;
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        TLRPC.PhotoSize photoSize;
        if (messageMedia instanceof TLRPC.TL_messageMediaPhoto) {
            TLRPC.Photo photo = messageMedia.photo;
            arrayList = photo.sizes;
            document = photo;
        } else {
            if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                isVideoDocument = MessageObject.isVideoDocument(messageMedia.document);
                document = messageMedia.document;
                arrayList = document.thumbs;
                closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, 40);
                TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize(), false, null, true);
                photoSize = closestPhotoSizeWithSize != closestPhotoSizeWithSize2 ? closestPhotoSizeWithSize2 : null;
                if (photoSize != null || DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.message.messageOwner, messageMedia) == 0) {
                    photoSize = closestPhotoSizeWithSize;
                }
                if (closestPhotoSizeWithSize == null) {
                    this.hasVideoThumb = this.hasVideoThumb || isVideoDocument;
                    int i2 = this.thumbsCount;
                    if (i2 < 3) {
                        this.thumbsCount = i2 + 1;
                        this.drawPlay[i] = isVideoDocument;
                        this.drawSpoiler[i] = false;
                        this.thumbImage[i].setImage(ImageLocation.getForObject(photoSize, document), "20_20", ImageLocation.getForObject(closestPhotoSizeWithSize, document), "20_20", (isVideoDocument || photoSize == null) ? 0 : photoSize.size, null, this.message, 0);
                        this.thumbImage[i].setRoundRadius(AndroidUtilities.dp(2.0f));
                        this.needEmoji = false;
                        return;
                    }
                    return;
                }
                return;
            }
            document = null;
            arrayList = null;
        }
        isVideoDocument = false;
        closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, 40);
        TLRPC.PhotoSize closestPhotoSizeWithSize22 = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize(), false, null, true);
        if (closestPhotoSizeWithSize != closestPhotoSizeWithSize22) {
        }
        if (photoSize != null) {
        }
        photoSize = closestPhotoSizeWithSize;
        if (closestPhotoSizeWithSize == null) {
        }
    }

    private void setThumb(int i, TLRPC.PhotoSize photoSize) {
        if (photoSize != null) {
            this.hasVideoThumb = false;
            int i2 = this.thumbsCount;
            if (i2 < 3) {
                this.thumbsCount = i2 + 1;
                this.drawPlay[i] = false;
                this.drawSpoiler[i] = true;
                this.thumbImage[i].setImage(ImageLocation.getForObject(photoSize, this.message.messageOwner), "2_2_b", null, null, 0, null, this.message, 0);
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
                    if (ceil != 0 && !this.drawForwardIcon && !this.drawGiftIcon) {
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

    public DialogCell allowBotOpenButton(boolean z, Utilities.Callback callback) {
        this.allowBotOpenButton = z;
        this.onOpenButtonClick = callback;
        return this;
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

    /* JADX WARN: Can't wrap try/catch for region: R(68:109|110|(2:753|(1:777)(2:757|(1:759)(2:761|(1:763)(2:765|(3:767|(1:769)(1:772)|770)))))(1:114)|116|(18:117|118|(1:120)|121|(1:123)|124|(3:126|(1:128)(1:130)|129)|131|(1:133)(1:748)|134|(1:136)|137|(1:747)(1:143)|144|(1:146)(1:746)|147|(1:745)(1:151)|152)|153|(5:718|(1:720)(1:743)|721|(2:722|(5:724|(1:726)(1:740)|727|(2:738|739)(2:735|736)|737)(1:741))|742)(9:157|(1:159)(1:717)|160|(1:162)(1:716)|163|(1:165)(1:715)|166|(2:167|(5:169|(1:171)(1:185)|172|(2:183|184)(2:180|181)|182)(1:186))|187)|188|189|(1:191)(1:714)|192|(1:194)|195|(1:203)|204|(3:206|(1:208)(1:210)|209)|211|(2:213|(1:215)(2:619|620))(1:(4:(4:653|(1:655)(1:710)|656|657)(1:711)|(6:659|(1:661)(1:708)|662|(3:664|(1:666)(1:702)|667)(3:703|(1:705)(1:707)|706)|668|669)(1:709)|670|(2:672|(4:674|(3:676|(1:678)(1:680)|679)|681|(3:683|(1:685)(1:687)|686))(5:688|(3:690|(1:692)(1:694)|693)|695|(3:697|(1:699)(1:701)|700)|620)))(4:625|(2:647|(2:649|(1:651)))(5:631|(1:646)(2:635|(1:637)(1:645))|638|639|(1:641)(1:644))|642|643))|(7:(1:218)|219|(1:221)|222|(1:235)(1:226)|227|(1:231))|236|(1:618)(1:240)|241|(3:247|(1:249)(1:251)|250)|252|(4:254|(1:562)|258|(2:259|(1:261)(1:262)))(2:563|(8:592|593|(1:599)|600|601|(1:611)(1:605)|606|(2:607|(1:609)(1:610)))(3:567|568|(5:573|574|(1:584)(1:578)|579|(2:580|(1:582)(1:583)))(1:572)))|263|264|(1:266)|267|(4:268|269|(1:271)(1:560)|272)|273|(3:274|275|(6:277|(4:279|(1:281)|282|283)|285|(2:287|283)|282|283))|(4:288|289|(4:526|527|(5:529|(2:531|(4:533|(2:535|(1:537))|538|(2:540|(2:542|(4:544|(1:548)|549|550)))))|551|549|550)|552)|291)|(3:514|515|(35:517|518|(19:520|521|309|(1:506)(1:313)|314|315|(5:497|(1:500)|501|(1:503)(1:505)|504)(3:319|(2:321|(1:325))|326)|327|328|329|330|331|332|333|(10:335|(9:339|(1:341)|342|(2:346|(5:348|349|350|351|(2:353|(1:355)))(5:356|(1:358)(2:359|(3:361|(1:363)(1:365)|364))|350|351|(0)))|366|349|350|351|(0))|371|(4:375|(1:(1:385)(2:377|(1:379)(2:380|381)))|382|(1:384))|386|(1:437)(3:390|(1:(2:392|(1:394)(2:395|396))(2:435|436))|(1:398))|399|(2:405|(1:407))|408|(4:412|(1:414)|415|416))(10:438|(5:442|(1:444)|445|(4:447|(1:449)|450|(1:452))|453)|454|(4:458|(1:460)|461|462)|463|(4:467|(1:469)|470|471)|472|(4:476|(1:478)|479|480)|481|(1:485))|417|(4:(1:432)(1:426)|427|(1:429)(1:431)|430)|433|434)|296|(1:298)|508|(26:(1:511)|308|309|(1:311)|506|314|315|(1:317)|495|497|(1:500)|501|(0)(0)|504|327|328|329|330|331|332|333|(0)(0)|417|(7:419|421|(1:424)|432|427|(0)(0)|430)|433|434)|300|(1:507)(1:306)|307|308|309|(0)|506|314|315|(0)|495|497|(0)|501|(0)(0)|504|327|328|329|330|331|332|333|(0)(0)|417|(0)|433|434))|295|296|(0)|508|(0)|300|(1:302)|507|307|308|309|(0)|506|314|315|(0)|495|497|(0)|501|(0)(0)|504|327|328|329|330|331|332|333|(0)(0)|417|(0)|433|434) */
    /* JADX WARN: Code restructure failed: missing block: B:1039:0x12b3, code lost:
    
        r9 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x15a4, code lost:
    
        if (org.telegram.messenger.LocaleController.isRTL != false) goto L1148;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1355:0x09e0, code lost:
    
        if (r9.id != r3) goto L529;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1359:0x09ee, code lost:
    
        if (org.telegram.messenger.ChatObject.isMegagroup(r51.chat) != false) goto L533;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1361:0x09f6, code lost:
    
        if (org.telegram.ui.Components.Forum.ForumUtilities.isTopicCreateMessage(r51.message) == false) goto L520;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1651:0x05b9, code lost:
    
        if (r0.post_messages == false) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x1f5f, code lost:
    
        if (org.telegram.messenger.SharedConfig.useThreeLinesLayout == false) goto L1564;
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x206a, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x206b, code lost:
    
        r1 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x206c, code lost:
    
        r2 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x206e, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x206f, code lost:
    
        r10 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x1618, code lost:
    
        r51.nameLeft += r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:760:0x15e3, code lost:
    
        if (org.telegram.messenger.LocaleController.isRTL != false) goto L1148;
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x15f5, code lost:
    
        if (org.telegram.messenger.LocaleController.isRTL != false) goto L1148;
     */
    /* JADX WARN: Code restructure failed: missing block: B:771:0x1616, code lost:
    
        if (org.telegram.messenger.LocaleController.isRTL != false) goto L1148;
     */
    /* JADX WARN: Code restructure failed: missing block: B:778:0x15ca, code lost:
    
        if (org.telegram.messenger.LocaleController.isRTL != false) goto L1148;
     */
    /* JADX WARN: Code restructure failed: missing block: B:886:0x0536, code lost:
    
        if (android.text.TextUtils.isEmpty(r1.message) != false) goto L249;
     */
    /* JADX WARN: Code restructure failed: missing block: B:991:0x1396, code lost:
    
        if (r4 == null) goto L1028;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x14c9  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x14e5  */
    /* JADX WARN: Removed duplicated region for block: B:1092:0x12d0  */
    /* JADX WARN: Removed duplicated region for block: B:1099:0x115e  */
    /* JADX WARN: Removed duplicated region for block: B:1107:0x0655  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x1591  */
    /* JADX WARN: Removed duplicated region for block: B:1192:0x0898  */
    /* JADX WARN: Removed duplicated region for block: B:1198:0x1006  */
    /* JADX WARN: Removed duplicated region for block: B:1200:0x100e  */
    /* JADX WARN: Removed duplicated region for block: B:1201:0x08a1  */
    /* JADX WARN: Removed duplicated region for block: B:1322:0x0f95  */
    /* JADX WARN: Removed duplicated region for block: B:1327:0x0fa6  */
    /* JADX WARN: Removed duplicated region for block: B:1491:0x0c21  */
    /* JADX WARN: Removed duplicated region for block: B:1496:0x0d98  */
    /* JADX WARN: Removed duplicated region for block: B:1499:0x0c24  */
    /* JADX WARN: Removed duplicated region for block: B:1654:0x05bf  */
    /* JADX WARN: Removed duplicated region for block: B:1682:0x053c  */
    /* JADX WARN: Removed duplicated region for block: B:1694:0x042a  */
    /* JADX WARN: Removed duplicated region for block: B:1723:0x04ad  */
    /* JADX WARN: Removed duplicated region for block: B:1730:0x04ce  */
    /* JADX WARN: Removed duplicated region for block: B:1735:0x04c7  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x1f5d A[Catch: Exception -> 0x1f08, TRY_ENTER, TRY_LEAVE, TryCatch #3 {Exception -> 0x1f08, blocks: (B:527:0x1ee1, B:529:0x1ef3, B:531:0x1ef9, B:533:0x1efd, B:535:0x1f03, B:538:0x1f0d, B:540:0x1f11, B:542:0x1f15, B:544:0x1f19, B:546:0x1f1d, B:551:0x1f2a, B:293:0x1f34, B:298:0x1f5d, B:302:0x1f73, B:304:0x1f77, B:306:0x1f8a, B:311:0x1fb5, B:313:0x1fb9, B:317:0x1fc5, B:321:0x200c, B:323:0x201e, B:325:0x2024, B:497:0x1fcf, B:500:0x1fd5, B:501:0x1fdc, B:504:0x1fee, B:520:0x1f4a), top: B:526:0x1ee1 }] */
    /* JADX WARN: Removed duplicated region for block: B:311:0x1fb5 A[Catch: Exception -> 0x1f08, TRY_ENTER, TryCatch #3 {Exception -> 0x1f08, blocks: (B:527:0x1ee1, B:529:0x1ef3, B:531:0x1ef9, B:533:0x1efd, B:535:0x1f03, B:538:0x1f0d, B:540:0x1f11, B:542:0x1f15, B:544:0x1f19, B:546:0x1f1d, B:551:0x1f2a, B:293:0x1f34, B:298:0x1f5d, B:302:0x1f73, B:304:0x1f77, B:306:0x1f8a, B:311:0x1fb5, B:313:0x1fb9, B:317:0x1fc5, B:321:0x200c, B:323:0x201e, B:325:0x2024, B:497:0x1fcf, B:500:0x1fd5, B:501:0x1fdc, B:504:0x1fee, B:520:0x1f4a), top: B:526:0x1ee1 }] */
    /* JADX WARN: Removed duplicated region for block: B:317:0x1fc5 A[Catch: Exception -> 0x1f08, TRY_ENTER, TRY_LEAVE, TryCatch #3 {Exception -> 0x1f08, blocks: (B:527:0x1ee1, B:529:0x1ef3, B:531:0x1ef9, B:533:0x1efd, B:535:0x1f03, B:538:0x1f0d, B:540:0x1f11, B:542:0x1f15, B:544:0x1f19, B:546:0x1f1d, B:551:0x1f2a, B:293:0x1f34, B:298:0x1f5d, B:302:0x1f73, B:304:0x1f77, B:306:0x1f8a, B:311:0x1fb5, B:313:0x1fb9, B:317:0x1fc5, B:321:0x200c, B:323:0x201e, B:325:0x2024, B:497:0x1fcf, B:500:0x1fd5, B:501:0x1fdc, B:504:0x1fee, B:520:0x1f4a), top: B:526:0x1ee1 }] */
    /* JADX WARN: Removed duplicated region for block: B:335:0x208d  */
    /* JADX WARN: Removed duplicated region for block: B:353:0x2164  */
    /* JADX WARN: Removed duplicated region for block: B:419:0x2339  */
    /* JADX WARN: Removed duplicated region for block: B:429:0x2375  */
    /* JADX WARN: Removed duplicated region for block: B:431:0x237d  */
    /* JADX WARN: Removed duplicated region for block: B:438:0x2259  */
    /* JADX WARN: Removed duplicated region for block: B:499:0x1fd3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:503:0x1fe9  */
    /* JADX WARN: Removed duplicated region for block: B:505:0x1fec  */
    /* JADX WARN: Removed duplicated region for block: B:510:0x1f67  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0298  */
    /* JADX WARN: Removed duplicated region for block: B:755:0x15ac  */
    /* JADX WARN: Removed duplicated region for block: B:782:0x1512  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x031a  */
    /* JADX WARN: Removed duplicated region for block: B:794:0x14ac  */
    /* JADX WARN: Removed duplicated region for block: B:796:0x1493  */
    /* JADX WARN: Removed duplicated region for block: B:798:0x1457  */
    /* JADX WARN: Removed duplicated region for block: B:799:0x0339  */
    /* JADX WARN: Removed duplicated region for block: B:805:0x0329  */
    /* JADX WARN: Removed duplicated region for block: B:815:0x02fb  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0332  */
    /* JADX WARN: Removed duplicated region for block: B:854:0x038c  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x141e  */
    /* JADX WARN: Removed duplicated region for block: B:872:0x04f3  */
    /* JADX WARN: Removed duplicated region for block: B:877:0x04ff  */
    /* JADX WARN: Removed duplicated region for block: B:891:0x0576  */
    /* JADX WARN: Removed duplicated region for block: B:895:0x05a9  */
    /* JADX WARN: Removed duplicated region for block: B:905:0x05e2  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x1464  */
    /* JADX WARN: Removed duplicated region for block: B:921:0x10f4  */
    /* JADX WARN: Removed duplicated region for block: B:935:0x1156  */
    /* JADX WARN: Removed duplicated region for block: B:939:0x1172  */
    /* JADX WARN: Removed duplicated region for block: B:957:0x1234  */
    /* JADX WARN: Removed duplicated region for block: B:964:0x1302  */
    /* JADX WARN: Removed duplicated region for block: B:980:0x1356  */
    /* JADX WARN: Removed duplicated region for block: B:983:0x1369  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x149a  */
    /* JADX WARN: Removed duplicated region for block: B:994:0x1412  */
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
        TLRPC.DraftMessage draftMessage;
        boolean z2;
        TLRPC.DraftMessage draftMessage2;
        TLRPC.DraftMessage draftMessage3;
        TLRPC.InputReplyTo inputReplyTo;
        TLRPC.DraftMessage draftMessage4;
        CharSequence charSequence3;
        boolean z3;
        int i5;
        CharSequence charSequence4;
        String str2;
        boolean z4;
        ArrayList<TLRPC.MessageEntity> arrayList;
        CharSequence charSequence5;
        CharSequence charSequence6;
        CharSequence charSequence7;
        CharSequence charSequence8;
        CharSequence charSequence9;
        TLRPC.Chat chat;
        boolean z5;
        String str3;
        CharSequence charSequence10;
        boolean z6;
        CharSequence charSequence11;
        CharSequence charSequence12;
        CharSequence charSequence13;
        CharSequence replaceNewLines;
        CharSequence charSequence14;
        int i6;
        char c;
        String str4;
        StringBuilder sb;
        String str5;
        CharSequence charSequence15;
        MessageObject messageObject;
        TextPaint textPaint;
        boolean isChannelAndNotMegaGroup;
        CharSequence formatPluralString;
        CharSequence charSequence16;
        char c2;
        int i7;
        int i8;
        String formatPluralString2;
        CharSequence charSequence17;
        SpannableStringBuilder spannableStringBuilder;
        int i9;
        MessageObject messageObject2;
        CharSequence charSequence18;
        int i10;
        CharSequence replaceEmoji;
        CharSequence charSequence19;
        boolean z7;
        CharSequence highlightText;
        Object foregroundColorSpanThemable;
        TLRPC.User user;
        MessageObject messageObject3;
        TLRPC.User user2;
        CharSequence charSequence20;
        CharSequence charSequence21;
        CharSequence string;
        CharSequence charSequence22;
        int i11;
        int i12;
        String str6;
        CharSequence charSequence23;
        TLRPC.TL_messageReactions tL_messageReactions;
        ArrayList<TLRPC.MessagePeerReaction> arrayList2;
        CharSequence string2;
        CharSequence charSequence24;
        int i13;
        CharSequence formatString;
        TLRPC.DraftMessage draftMessage5;
        int i14;
        String str7;
        MessageObject messageObject4;
        boolean z8;
        String str8;
        String str9;
        String str10;
        String userName;
        int i15;
        CharSequence string3;
        int i16;
        boolean z9;
        boolean z10;
        CharSequence charSequence25;
        CharSequence charSequence26;
        String str11;
        int i17;
        String str12;
        CharSequence charSequence27;
        boolean z11;
        boolean z12;
        boolean z13;
        String str13;
        String str14;
        boolean z14;
        boolean z15;
        MessageObject messageObject5;
        CharSequence charSequence28;
        TLRPC.Chat chat2;
        MessageObject messageObject6;
        int dp2;
        int i18;
        float f;
        int i19;
        int intrinsicWidth;
        int i20;
        int dp3;
        int dp4;
        int dp5;
        int i21;
        int i22;
        DialogCellTags dialogCellTags;
        int dp6;
        CharSequence highlightText2;
        int i23;
        int i24;
        int lineCount;
        int lineCount2;
        int lineCount3;
        StaticLayout staticLayout;
        float primaryHorizontal;
        float primaryHorizontal2;
        int i25;
        int i26;
        int lineCount4;
        int lineCount5;
        int lineCount6;
        double d;
        Drawable drawable;
        int i27;
        CharSequence charSequence29;
        Layout.Alignment alignment;
        TextPaint textPaint2;
        float dp7;
        TextUtils.TruncateAt truncateAt;
        StaticLayout createStaticLayout;
        DialogCellTags dialogCellTags2;
        int dp8;
        int dp9;
        DialogCellTags dialogCellTags3;
        CharSequence highlightText3;
        int intrinsicWidth2;
        int i28;
        int dp10;
        CustomDialog customDialog;
        CharSequence charSequence30;
        int i29;
        String str15;
        int i30;
        SpannableStringBuilder formatInternal;
        int i31;
        String str16 = "d ";
        boolean z16 = false;
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
        this.drawGiftIcon = false;
        this.drawScam = 0;
        this.drawPinBackground = false;
        this.thumbsCount = 0;
        this.hasVideoThumb = false;
        this.nameLayoutEllipsizeByGradient = false;
        boolean z17 = (UserObject.isUserSelf(this.user) || this.useMeForMyMessages) ? false : true;
        this.printingStringType = -1;
        if (!isForumCell()) {
            this.buttonLayout = null;
        }
        setOpenBotButton(false);
        if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && this.currentDialogFolderId == 0 && !isForumCell() && !hasTags()) {
            this.hasNameInMessage = false;
            i2 = 2;
        } else {
            this.hasNameInMessage = true;
            i2 = 1;
        }
        MessageObject messageObject7 = this.message;
        if (messageObject7 != null) {
            messageObject7.updateTranslation();
        }
        MessageObject messageObject8 = this.message;
        CharSequence charSequence31 = messageObject8 != null ? messageObject8.messageText : null;
        if (charSequence31 instanceof Spannable) {
            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(charSequence31);
            for (URLSpanNoUnderlineBold uRLSpanNoUnderlineBold : (URLSpanNoUnderlineBold[]) spannableStringBuilder2.getSpans(0, spannableStringBuilder2.length(), URLSpanNoUnderlineBold.class)) {
                spannableStringBuilder2.removeSpan(uRLSpanNoUnderlineBold);
            }
            for (URLSpanNoUnderline uRLSpanNoUnderline : (URLSpanNoUnderline[]) spannableStringBuilder2.getSpans(0, spannableStringBuilder2.length(), URLSpanNoUnderline.class)) {
                spannableStringBuilder2.removeSpan(uRLSpanNoUnderline);
            }
            charSequence31 = spannableStringBuilder2;
        }
        this.lastMessageString = charSequence31;
        CustomDialog customDialog2 = this.customDialog;
        if (customDialog2 != null) {
            if (customDialog2.type == 2) {
                this.drawNameLock = true;
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    this.nameLockTop = AndroidUtilities.dp(12.5f);
                    if (LocaleController.isRTL) {
                        this.nameLockLeft = (getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 6)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        dp10 = AndroidUtilities.dp(22.0f);
                        this.nameLeft = dp10;
                        customDialog = this.customDialog;
                        if (customDialog.type != 1) {
                            charSequence30 = LocaleController.getString(R.string.FromYou);
                            CustomDialog customDialog3 = this.customDialog;
                            if (customDialog3.isMedia) {
                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                formatInternal = formatInternal(i2, this.message.messageText, null);
                                formatInternal.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage, this.resourcesProvider), 0, formatInternal.length(), 33);
                            } else {
                                String str17 = customDialog3.message;
                                if (str17.length() > 150) {
                                    str17 = str17.substring(0, 150);
                                }
                                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                    str17 = str17.replace('\n', ' ');
                                }
                                formatInternal = formatInternal(i2, str17, charSequence30);
                            }
                            charSequence26 = Emoji.replaceEmoji((CharSequence) formatInternal, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                            z9 = false;
                        } else {
                            charSequence26 = customDialog.message;
                            if (customDialog.isMedia) {
                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                            }
                            charSequence30 = null;
                            z9 = true;
                        }
                        str10 = LocaleController.stringForMessageListDate(this.customDialog.date);
                        i29 = this.customDialog.unread_count;
                        if (i29 == 0) {
                            this.drawCount = true;
                            str15 = String.format("%d", Integer.valueOf(i29));
                        } else {
                            this.drawCount = false;
                            str15 = null;
                        }
                        CustomDialog customDialog4 = this.customDialog;
                        i30 = customDialog4.sent;
                        if (i30 != 0) {
                            this.drawClock = true;
                        } else {
                            if (i30 == 2) {
                                this.drawCheck1 = true;
                            } else if (i30 == 1) {
                                this.drawCheck1 = false;
                            } else {
                                this.drawClock = false;
                            }
                            this.drawCheck2 = true;
                            this.drawClock = false;
                            this.drawError = false;
                            charSequence27 = customDialog4.name;
                            str11 = str15;
                            charSequence4 = "";
                            str12 = null;
                            i17 = -1;
                            charSequence25 = null;
                            charSequence6 = charSequence30;
                            z10 = true;
                        }
                        this.drawCheck1 = false;
                        this.drawCheck2 = false;
                        this.drawError = false;
                        charSequence27 = customDialog4.name;
                        str11 = str15;
                        charSequence4 = "";
                        str12 = null;
                        i17 = -1;
                        charSequence25 = null;
                        charSequence6 = charSequence30;
                        z10 = true;
                    } else {
                        this.nameLockLeft = AndroidUtilities.dp(this.messagePaddingStart + 6);
                        i31 = this.messagePaddingStart + 10;
                    }
                } else {
                    this.nameLockTop = AndroidUtilities.dp(16.5f);
                    if (LocaleController.isRTL) {
                        this.nameLockLeft = (getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 4)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        dp10 = AndroidUtilities.dp(18.0f);
                        this.nameLeft = dp10;
                        customDialog = this.customDialog;
                        if (customDialog.type != 1) {
                        }
                        str10 = LocaleController.stringForMessageListDate(this.customDialog.date);
                        i29 = this.customDialog.unread_count;
                        if (i29 == 0) {
                        }
                        CustomDialog customDialog42 = this.customDialog;
                        i30 = customDialog42.sent;
                        if (i30 != 0) {
                        }
                        this.drawCheck1 = false;
                        this.drawCheck2 = false;
                        this.drawError = false;
                        charSequence27 = customDialog42.name;
                        str11 = str15;
                        charSequence4 = "";
                        str12 = null;
                        i17 = -1;
                        charSequence25 = null;
                        charSequence6 = charSequence30;
                        z10 = true;
                    } else {
                        this.nameLockLeft = AndroidUtilities.dp(this.messagePaddingStart + 4);
                        i31 = this.messagePaddingStart + 8;
                    }
                }
                dp10 = AndroidUtilities.dp(i31) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                this.nameLeft = dp10;
                customDialog = this.customDialog;
                if (customDialog.type != 1) {
                }
                str10 = LocaleController.stringForMessageListDate(this.customDialog.date);
                i29 = this.customDialog.unread_count;
                if (i29 == 0) {
                }
                CustomDialog customDialog422 = this.customDialog;
                i30 = customDialog422.sent;
                if (i30 != 0) {
                }
                this.drawCheck1 = false;
                this.drawCheck2 = false;
                this.drawError = false;
                charSequence27 = customDialog422.name;
                str11 = str15;
                charSequence4 = "";
                str12 = null;
                i17 = -1;
                charSequence25 = null;
                charSequence6 = charSequence30;
                z10 = true;
            } else {
                this.drawVerified = !this.forbidVerified && customDialog2.verified;
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    if (!LocaleController.isRTL) {
                        i28 = this.messagePaddingStart + 6;
                    }
                    dp10 = AndroidUtilities.dp(22.0f);
                    this.nameLeft = dp10;
                    customDialog = this.customDialog;
                    if (customDialog.type != 1) {
                    }
                    str10 = LocaleController.stringForMessageListDate(this.customDialog.date);
                    i29 = this.customDialog.unread_count;
                    if (i29 == 0) {
                    }
                    CustomDialog customDialog4222 = this.customDialog;
                    i30 = customDialog4222.sent;
                    if (i30 != 0) {
                    }
                    this.drawCheck1 = false;
                    this.drawCheck2 = false;
                    this.drawError = false;
                    charSequence27 = customDialog4222.name;
                    str11 = str15;
                    charSequence4 = "";
                    str12 = null;
                    i17 = -1;
                    charSequence25 = null;
                    charSequence6 = charSequence30;
                    z10 = true;
                } else {
                    if (!LocaleController.isRTL) {
                        i28 = this.messagePaddingStart + 4;
                    }
                    dp10 = AndroidUtilities.dp(18.0f);
                    this.nameLeft = dp10;
                    customDialog = this.customDialog;
                    if (customDialog.type != 1) {
                    }
                    str10 = LocaleController.stringForMessageListDate(this.customDialog.date);
                    i29 = this.customDialog.unread_count;
                    if (i29 == 0) {
                    }
                    CustomDialog customDialog42222 = this.customDialog;
                    i30 = customDialog42222.sent;
                    if (i30 != 0) {
                    }
                    this.drawCheck1 = false;
                    this.drawCheck2 = false;
                    this.drawError = false;
                    charSequence27 = customDialog42222.name;
                    str11 = str15;
                    charSequence4 = "";
                    str12 = null;
                    i17 = -1;
                    charSequence25 = null;
                    charSequence6 = charSequence30;
                    z10 = true;
                }
                dp10 = AndroidUtilities.dp(i28);
                this.nameLeft = dp10;
                customDialog = this.customDialog;
                if (customDialog.type != 1) {
                }
                str10 = LocaleController.stringForMessageListDate(this.customDialog.date);
                i29 = this.customDialog.unread_count;
                if (i29 == 0) {
                }
                CustomDialog customDialog422222 = this.customDialog;
                i30 = customDialog422222.sent;
                if (i30 != 0) {
                }
                this.drawCheck1 = false;
                this.drawCheck2 = false;
                this.drawError = false;
                charSequence27 = customDialog422222.name;
                str11 = str15;
                charSequence4 = "";
                str12 = null;
                i17 = -1;
                charSequence25 = null;
                charSequence6 = charSequence30;
                z10 = true;
            }
        } else if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
            if (LocaleController.isRTL) {
                dp = AndroidUtilities.dp(22.0f);
                this.nameLeft = dp;
                String str18 = str;
                if (this.encryptedChat == null) {
                }
                charSequence2 = charSequence31;
                i4 = this.lastMessageDate;
                if (i4 == 0) {
                }
                if (this.isTopic) {
                }
                draftMessage = null;
                this.draftMessage = draftMessage;
                z2 = this.draftVoice;
                if (!z2) {
                }
                if (ChatObject.isChannel(this.chat)) {
                }
                chat2 = this.chat;
                if (chat2 != null) {
                }
                draftMessage4 = null;
                if (isForumCell()) {
                }
                z16 = true;
                string2 = charSequence28;
                CharSequence charSequence32 = string2;
                if (!this.drawForwardIcon) {
                }
                draftMessage5 = this.draftMessage;
                if (draftMessage5 != null) {
                }
                str7 = LocaleController.stringForMessageListDate(i14);
                messageObject4 = this.message;
                if (messageObject4 != null) {
                }
                this.drawCheck1 = false;
                this.drawCheck2 = false;
                this.drawClock = messageObject4 == null && messageObject4.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                z8 = false;
                this.drawCount = false;
                this.drawMention = false;
                this.drawReactionMention = false;
                this.drawError = false;
                str8 = null;
                str9 = null;
                this.promoDialog = z8;
                MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
                CharSequence charSequence33 = charSequence32;
                if (this.dialogsType == 0) {
                }
                str10 = str7;
                if (this.currentDialogFolderId == 0) {
                }
                string3 = LocaleController.getString(i16);
                CharSequence charSequence34 = string3;
                z9 = z3;
                z10 = z16;
                charSequence25 = charSequence9;
                charSequence26 = charSequence33;
                str11 = str8;
                i17 = i5;
                str12 = str9;
                charSequence27 = charSequence34;
            } else {
                i3 = this.messagePaddingStart + 6;
                dp = AndroidUtilities.dp(i3);
                this.nameLeft = dp;
                String str182 = str;
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
                    TLRPC.Chat chat3 = this.chat;
                    if (chat3 != null) {
                        if (chat3.scam) {
                            this.drawScam = 1;
                            scamDrawable2 = Theme.dialogs_scamDrawable;
                        } else if (chat3.fake) {
                            this.drawScam = 2;
                            scamDrawable2 = Theme.dialogs_fakeDrawable;
                        } else if (DialogObject.getEmojiStatusDocumentId(chat3.emoji_status) != 0) {
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
                        TLRPC.User user3 = this.user;
                        if (user3 != null) {
                            if (user3.scam) {
                                this.drawScam = 1;
                                scamDrawable = Theme.dialogs_scamDrawable;
                            } else if (user3.fake) {
                                this.drawScam = 2;
                                scamDrawable = Theme.dialogs_fakeDrawable;
                            } else {
                                this.drawVerified = !this.forbidVerified && user3.verified;
                                if (MessagesController.getInstance(this.currentAccount).isPremiumUser(this.user)) {
                                    charSequence2 = charSequence31;
                                } else {
                                    long j = UserConfig.getInstance(this.currentAccount).clientUserId;
                                    charSequence2 = charSequence31;
                                    long j2 = this.user.id;
                                    if (j != j2 && j2 != 0) {
                                        z = true;
                                        this.drawPremium = z;
                                        if (z) {
                                            Long emojiStatusDocumentId = UserObject.getEmojiStatusDocumentId(this.user);
                                            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable2 = this.emojiStatus;
                                            swapAnimatedEmojiDrawable2.center = LocaleController.isRTL;
                                            this.nameLayoutEllipsizeByGradient = true;
                                            if (emojiStatusDocumentId != null) {
                                                swapAnimatedEmojiDrawable2.set(emojiStatusDocumentId.longValue(), false);
                                            } else {
                                                swapAnimatedEmojiDrawable2.set(PremiumGradient.getInstance().premiumStarDrawableMini, false);
                                            }
                                        }
                                        i4 = this.lastMessageDate;
                                        if (i4 == 0 && (messageObject6 = this.message) != null) {
                                            i4 = messageObject6.messageOwner.date;
                                        }
                                        if (this.isTopic) {
                                            boolean z18 = MediaDataController.getInstance(this.currentAccount).getDraftVoice(this.currentDialogId, (long) getTopicId()) != null;
                                            this.draftVoice = z18;
                                            TLRPC.DraftMessage draft = !z18 ? MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, getTopicId()) : null;
                                            this.draftMessage = draft;
                                            if (draft != null) {
                                            }
                                            z2 = this.draftVoice;
                                            if ((!z2 && this.draftMessage == null) || ((z2 || (draftMessage3 = this.draftMessage) == null || !TextUtils.isEmpty(draftMessage3.message) || ((inputReplyTo = this.draftMessage.reply_to) != null && inputReplyTo.reply_to_msg_id != 0)) && ((draftMessage2 = this.draftMessage) == null || i4 <= draftMessage2.date || this.unreadCount == 0))) {
                                                if (ChatObject.isChannel(this.chat)) {
                                                    TLRPC.Chat chat4 = this.chat;
                                                    if (!chat4.megagroup) {
                                                        if (!chat4.creator) {
                                                            TLRPC.TL_chatAdminRights tL_chatAdminRights = chat4.admin_rights;
                                                            if (tL_chatAdminRights != null) {
                                                            }
                                                        }
                                                    }
                                                }
                                                chat2 = this.chat;
                                                if ((chat2 != null || (!chat2.left && !chat2.kicked)) && !this.forbidDraft && (!ChatObject.isForum(chat2) || this.isTopic)) {
                                                    draftMessage4 = null;
                                                    if (isForumCell()) {
                                                        this.draftMessage = draftMessage4;
                                                        this.draftVoice = false;
                                                        this.needEmoji = true;
                                                        updateMessageThumbs();
                                                        CharSequence removeDiacritics = AndroidUtilities.removeDiacritics(getMessageNameString());
                                                        CharSequence formatTopicsNames = formatTopicsNames();
                                                        MessageObject messageObject9 = this.message;
                                                        String messageStringFormatted = this.message != null ? getMessageStringFormatted(i2, messageObject9 != null ? MessagesController.getInstance(messageObject9.currentAccount).getRestrictionReason(this.message.messageOwner.restriction_reason) : null, removeDiacritics, true) : "";
                                                        CharSequence charSequence35 = messageStringFormatted;
                                                        if (this.applyName) {
                                                            int length = messageStringFormatted.length();
                                                            charSequence35 = messageStringFormatted;
                                                            charSequence35 = messageStringFormatted;
                                                            if (length >= 0 && removeDiacritics != null) {
                                                                SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(messageStringFormatted);
                                                                valueOf.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_name, this.resourcesProvider), 0, Math.min(valueOf.length(), removeDiacritics.length() + 1), 0);
                                                                charSequence35 = valueOf;
                                                            }
                                                        }
                                                        this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                                                        charSequence6 = removeDiacritics;
                                                        charSequence9 = charSequence35;
                                                        charSequence4 = "";
                                                        z3 = true;
                                                        i5 = -1;
                                                        charSequence28 = formatTopicsNames;
                                                    } else {
                                                        if (charSequence != null) {
                                                            this.lastPrintString = charSequence;
                                                            int intValue = MessagesController.getInstance(this.currentAccount).getPrintingStringType(this.currentDialogId, getTopicId()).intValue();
                                                            this.printingStringType = intValue;
                                                            StatusDrawable chatStatusDrawable = Theme.getChatStatusDrawable(intValue);
                                                            int intrinsicWidth3 = chatStatusDrawable != null ? chatStatusDrawable.getIntrinsicWidth() + AndroidUtilities.dp(3.0f) : 0;
                                                            SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder();
                                                            CharSequence replace = TextUtils.replace(charSequence, new String[]{"..."}, new String[]{""});
                                                            int indexOf = this.printingStringType == 5 ? replace.toString().indexOf("**oo**") : -1;
                                                            if (indexOf >= 0) {
                                                                spannableStringBuilder3.append(replace).setSpan(new FixedWidthSpan(Theme.getChatStatusDrawable(this.printingStringType).getIntrinsicWidth()), indexOf, indexOf + 6, 0);
                                                            } else {
                                                                spannableStringBuilder3.append((CharSequence) " ").append(replace).setSpan(new FixedWidthSpan(intrinsicWidth3), 0, 1, 0);
                                                            }
                                                            i5 = indexOf;
                                                            z3 = false;
                                                            charSequence3 = spannableStringBuilder3;
                                                        } else {
                                                            this.lastPrintString = null;
                                                            this.printingStringType = -1;
                                                            charSequence3 = "";
                                                            z3 = true;
                                                            i5 = -1;
                                                        }
                                                        if (this.draftVoice || this.draftMessage != null) {
                                                            charSequence4 = charSequence3;
                                                            CharSequence string4 = LocaleController.getString(R.string.Draft);
                                                            TLRPC.DraftMessage draftMessage6 = this.draftMessage;
                                                            if (draftMessage6 == null || !TextUtils.isEmpty(draftMessage6.message)) {
                                                                if (this.draftVoice) {
                                                                    str2 = LocaleController.getString(R.string.AttachAudio);
                                                                } else {
                                                                    TLRPC.DraftMessage draftMessage7 = this.draftMessage;
                                                                    if (draftMessage7 != null) {
                                                                        str2 = draftMessage7.message;
                                                                        if (str2.length() > 150) {
                                                                            str2 = str2.substring(0, 150);
                                                                        }
                                                                    } else {
                                                                        str2 = "";
                                                                    }
                                                                }
                                                                SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder(str2);
                                                                TLRPC.DraftMessage draftMessage8 = this.draftMessage;
                                                                if (draftMessage8 != null) {
                                                                    MediaDataController.addTextStyleRuns(draftMessage8, spannableStringBuilder4, NotificationCenter.webRtcMicAmplitudeEvent);
                                                                    TLRPC.DraftMessage draftMessage9 = this.draftMessage;
                                                                    if (draftMessage9 != null && (arrayList = draftMessage9.entities) != null) {
                                                                        TextPaint textPaint5 = this.currentMessagePaint;
                                                                        MediaDataController.addAnimatedEmojiSpans(arrayList, spannableStringBuilder4, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                    }
                                                                } else if (this.draftVoice) {
                                                                    spannableStringBuilder4.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_actionMessage, this.resourcesProvider), 0, spannableStringBuilder4.length(), 33);
                                                                }
                                                                SpannableStringBuilder formatInternal2 = formatInternal(i2, AndroidUtilities.replaceNewLines(spannableStringBuilder4), string4);
                                                                if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
                                                                    z4 = false;
                                                                } else {
                                                                    z4 = false;
                                                                    formatInternal2.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_draft, this.resourcesProvider), 0, string4.length() + 1, 33);
                                                                }
                                                                charSequence5 = Emoji.replaceEmoji(formatInternal2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), z4);
                                                            } else if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
                                                                charSequence7 = string4;
                                                                z3 = false;
                                                            } else {
                                                                SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(string4);
                                                                valueOf2.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_draft, this.resourcesProvider), 0, string4.length(), 33);
                                                                charSequence5 = valueOf2;
                                                            }
                                                            charSequence6 = string4;
                                                            z3 = false;
                                                            charSequence8 = charSequence5;
                                                            charSequence9 = null;
                                                            charSequence28 = charSequence8;
                                                        } else {
                                                            if (this.clearingDialog) {
                                                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                i13 = R.string.HistoryCleared;
                                                            } else {
                                                                MessageObject messageObject10 = this.message;
                                                                if (messageObject10 == null) {
                                                                    if (this.currentDialogFolderId != 0) {
                                                                        formatString = formatArchivedDialogNames();
                                                                    } else {
                                                                        TLRPC.EncryptedChat encryptedChat = this.encryptedChat;
                                                                        if (encryptedChat != null) {
                                                                            this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                            if (encryptedChat instanceof TLRPC.TL_encryptedChatRequested) {
                                                                                i13 = R.string.EncryptionProcessing;
                                                                            } else if (encryptedChat instanceof TLRPC.TL_encryptedChatWaiting) {
                                                                                formatString = LocaleController.formatString(R.string.AwaitingEncryption, UserObject.getFirstName(this.user));
                                                                            } else if (encryptedChat instanceof TLRPC.TL_encryptedChatDiscarded) {
                                                                                i13 = R.string.EncryptionRejected;
                                                                            } else if (!(encryptedChat instanceof TLRPC.TL_encryptedChat)) {
                                                                                charSequence4 = charSequence3;
                                                                                charSequence24 = "";
                                                                                charSequence9 = null;
                                                                                charSequence6 = null;
                                                                                charSequence28 = charSequence24;
                                                                            } else if (encryptedChat.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                                                formatString = LocaleController.formatString(R.string.EncryptedChatStartedOutgoing, UserObject.getFirstName(this.user));
                                                                            } else {
                                                                                i13 = R.string.EncryptedChatStartedIncoming;
                                                                            }
                                                                        } else if (this.dialogsType == 3 && UserObject.isUserSelf(this.user)) {
                                                                            DialogsActivity dialogsActivity = this.parentFragment;
                                                                            charSequence4 = charSequence3;
                                                                            string2 = LocaleController.getString((dialogsActivity == null || !dialogsActivity.isQuote) ? R.string.SavedMessagesInfo : R.string.SavedMessagesInfoQuote);
                                                                            charSequence9 = null;
                                                                            z17 = false;
                                                                            charSequence6 = null;
                                                                        } else {
                                                                            charSequence4 = charSequence3;
                                                                            charSequence7 = null;
                                                                        }
                                                                    }
                                                                    charSequence4 = charSequence3;
                                                                    charSequence24 = formatString;
                                                                    charSequence9 = null;
                                                                    charSequence6 = null;
                                                                    charSequence28 = charSequence24;
                                                                } else {
                                                                    String restrictionReason = MessagesController.getInstance(messageObject10.currentAccount).getRestrictionReason(this.message.messageOwner.restriction_reason);
                                                                    long fromChatId = this.message.getFromChatId();
                                                                    if (DialogObject.isUserDialog(fromChatId)) {
                                                                        MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(fromChatId));
                                                                        chat = null;
                                                                    } else {
                                                                        chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-fromChatId));
                                                                    }
                                                                    this.drawCount2 = true;
                                                                    if (this.dialogsType == 0 && this.currentDialogId > 0 && this.message.isOutOwner() && (tL_messageReactions = this.message.messageOwner.reactions) != null && (arrayList2 = tL_messageReactions.recent_reactions) != null && !arrayList2.isEmpty() && this.reactionMentionCount > 0) {
                                                                        TLRPC.MessagePeerReaction messagePeerReaction = this.message.messageOwner.reactions.recent_reactions.get(0);
                                                                        if (messagePeerReaction.unread) {
                                                                            str3 = "d ";
                                                                            long j3 = messagePeerReaction.peer_id.user_id;
                                                                            if (j3 == 0 || j3 == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                                                z5 = z3;
                                                                                charSequence4 = charSequence3;
                                                                                charSequence10 = "";
                                                                                z6 = false;
                                                                                if (z6) {
                                                                                    charSequence21 = charSequence10;
                                                                                } else {
                                                                                    int i32 = this.dialogsType;
                                                                                    if (i32 == 2) {
                                                                                        TLRPC.Chat chat5 = this.chat;
                                                                                        if (chat5 != null) {
                                                                                            if (ChatObject.isChannel(chat5)) {
                                                                                                TLRPC.Chat chat6 = this.chat;
                                                                                                if (!chat6.megagroup) {
                                                                                                    i11 = chat6.participants_count;
                                                                                                    if (i11 != 0) {
                                                                                                        str6 = "Subscribers";
                                                                                                        string = LocaleController.formatPluralStringComma(str6, i11);
                                                                                                    } else {
                                                                                                        i12 = !ChatObject.isPublic(chat6) ? R.string.ChannelPrivate : R.string.ChannelPublic;
                                                                                                        string = LocaleController.getString(i12).toLowerCase();
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            TLRPC.Chat chat7 = this.chat;
                                                                                            i11 = chat7.participants_count;
                                                                                            if (i11 != 0) {
                                                                                                str6 = "Members";
                                                                                                string = LocaleController.formatPluralStringComma(str6, i11);
                                                                                            } else if (chat7.has_geo) {
                                                                                                string = LocaleController.getString(R.string.MegaLocation);
                                                                                            } else {
                                                                                                i12 = !ChatObject.isPublic(chat7) ? R.string.MegaPrivate : R.string.MegaPublic;
                                                                                                string = LocaleController.getString(i12).toLowerCase();
                                                                                            }
                                                                                        } else {
                                                                                            string = "";
                                                                                        }
                                                                                        this.drawCount2 = false;
                                                                                    } else if (i32 == 3 && UserObject.isUserSelf(this.user)) {
                                                                                        DialogsActivity dialogsActivity2 = this.parentFragment;
                                                                                        string = LocaleController.getString((dialogsActivity2 == null || !dialogsActivity2.isQuote) ? R.string.SavedMessagesInfo : R.string.SavedMessagesInfoQuote);
                                                                                    } else {
                                                                                        if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout || this.currentDialogFolderId == 0) {
                                                                                            MessageObject messageObject11 = this.message;
                                                                                            if (!(messageObject11.messageOwner instanceof TLRPC.TL_messageService) || (MessageObject.isTopicActionMessage(messageObject11) && !(this.message.messageOwner.action instanceof TLRPC.TL_messageActionTopicCreate))) {
                                                                                                this.needEmoji = true;
                                                                                                updateMessageThumbs();
                                                                                                String removeDiacritics2 = (this.isSavedDialog || (user2 = this.user) == null || !user2.self || this.message.isOutOwner()) ? null : AndroidUtilities.removeDiacritics(getMessageNameString());
                                                                                                if ((!this.isSavedDialog || (user = this.user) == null || user.self || (messageObject3 = this.message) == null || !messageObject3.isOutOwner()) && removeDiacritics2 == null) {
                                                                                                    TLRPC.Chat chat8 = this.chat;
                                                                                                    if (chat8 != null) {
                                                                                                        long j4 = chat8.id;
                                                                                                        if (j4 > 0) {
                                                                                                            if (chat != null) {
                                                                                                            }
                                                                                                            if (ChatObject.isChannel(chat8)) {
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                    TLRPC.User user4 = this.user;
                                                                                                    if (user4 == null || user4.id != UserObject.VERIFY || (messageObject2 = this.message) == null || messageObject2.getForwardedFromId() == null) {
                                                                                                        boolean isEmpty = TextUtils.isEmpty(restrictionReason);
                                                                                                        CharSequence charSequence36 = restrictionReason;
                                                                                                        if (isEmpty) {
                                                                                                            if (MessageObject.isTopicActionMessage(this.message)) {
                                                                                                                MessageObject messageObject12 = this.message;
                                                                                                                CharSequence charSequence37 = messageObject12.messageTextShort;
                                                                                                                if (charSequence37 == null || ((messageObject12.messageOwner.action instanceof TLRPC.TL_messageActionTopicCreate) && this.isTopic)) {
                                                                                                                    charSequence37 = messageObject12.messageText;
                                                                                                                }
                                                                                                                CharSequence charSequence38 = charSequence37;
                                                                                                                charSequence36 = charSequence38;
                                                                                                                if (messageObject12.topicIconDrawable[0] instanceof ForumBubbleDrawable) {
                                                                                                                    TLRPC.TL_forumTopic findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-this.message.getDialogId(), MessageObject.getTopicId(this.currentAccount, this.message.messageOwner, true));
                                                                                                                    charSequence36 = charSequence38;
                                                                                                                    if (findTopic != null) {
                                                                                                                        ((ForumBubbleDrawable) this.message.topicIconDrawable[0]).setColor(findTopic.icon_color);
                                                                                                                        charSequence36 = charSequence38;
                                                                                                                    }
                                                                                                                }
                                                                                                            } else {
                                                                                                                TLRPC.MessageMedia messageMedia = this.message.messageOwner.media;
                                                                                                                if ((messageMedia instanceof TLRPC.TL_messageMediaPhoto) && (messageMedia.photo instanceof TLRPC.TL_photoEmpty) && messageMedia.ttl_seconds != 0) {
                                                                                                                    i9 = R.string.AttachPhotoExpired;
                                                                                                                } else {
                                                                                                                    if (messageMedia instanceof TLRPC.TL_messageMediaDocument) {
                                                                                                                        TLRPC.Document document = messageMedia.document;
                                                                                                                        if (((document instanceof TLRPC.TL_documentEmpty) || document == null) && messageMedia.ttl_seconds != 0) {
                                                                                                                            i9 = messageMedia.voice ? R.string.AttachVoiceExpired : messageMedia.round ? R.string.AttachRoundExpired : R.string.AttachVideoExpired;
                                                                                                                        }
                                                                                                                    }
                                                                                                                    if (getCaptionMessage() != null) {
                                                                                                                        MessageObject captionMessage = getCaptionMessage();
                                                                                                                        String str19 = !this.needEmoji ? "" : captionMessage.isVideo() ? "📹 " : captionMessage.isVoice() ? "🎤 " : captionMessage.isMusic() ? "🎧 " : captionMessage.isPhoto() ? "🖼 " : "📎 ";
                                                                                                                        if (!captionMessage.hasHighlightedWords() || TextUtils.isEmpty(captionMessage.messageOwner.message)) {
                                                                                                                            SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder(captionMessage.caption);
                                                                                                                            if (captionMessage.messageOwner != null) {
                                                                                                                                captionMessage.spoilLoginCode();
                                                                                                                                MediaDataController.addTextStyleRuns(captionMessage.messageOwner.entities, captionMessage.caption, spannableStringBuilder5, NotificationCenter.webRtcMicAmplitudeEvent);
                                                                                                                                ArrayList<TLRPC.MessageEntity> arrayList3 = captionMessage.messageOwner.entities;
                                                                                                                                TextPaint textPaint6 = this.currentMessagePaint;
                                                                                                                                MediaDataController.addAnimatedEmojiSpans(arrayList3, spannableStringBuilder5, textPaint6 == null ? null : textPaint6.getFontMetricsInt());
                                                                                                                            }
                                                                                                                            spannableStringBuilder = new SpannableStringBuilder(str19);
                                                                                                                            charSequence17 = spannableStringBuilder5;
                                                                                                                        } else {
                                                                                                                            CharSequence charSequence39 = captionMessage.messageTrimmedToHighlight;
                                                                                                                            int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 47);
                                                                                                                            if (this.hasNameInMessage) {
                                                                                                                                if (!TextUtils.isEmpty(null)) {
                                                                                                                                    throw null;
                                                                                                                                }
                                                                                                                                measuredWidth = (int) (measuredWidth - this.currentMessagePaint.measureText(": "));
                                                                                                                            }
                                                                                                                            if (measuredWidth > 0 && captionMessage.messageTrimmedToHighlightCut) {
                                                                                                                                charSequence39 = AndroidUtilities.ellipsizeCenterEnd(charSequence39, captionMessage.highlightedWords.get(0), measuredWidth, this.currentMessagePaint, NotificationCenter.walletSyncProgressChanged).toString();
                                                                                                                            }
                                                                                                                            spannableStringBuilder = new SpannableStringBuilder(str19);
                                                                                                                            charSequence17 = charSequence39;
                                                                                                                        }
                                                                                                                        charSequence36 = spannableStringBuilder.append(charSequence17);
                                                                                                                    } else {
                                                                                                                        MessageObject messageObject13 = this.message;
                                                                                                                        TLRPC.Message message = messageObject13.messageOwner;
                                                                                                                        TLRPC.MessageMedia messageMedia2 = message.media;
                                                                                                                        if (messageMedia2 instanceof TLRPC.TL_messageMediaPaidMedia) {
                                                                                                                            int size = ((TLRPC.TL_messageMediaPaidMedia) messageMedia2).extended_media.size();
                                                                                                                            if (this.hasVideoThumb) {
                                                                                                                                i7 = 1;
                                                                                                                                c2 = 0;
                                                                                                                                if (size > 1) {
                                                                                                                                    formatPluralString2 = LocaleController.formatPluralString("Media", size, new Object[0]);
                                                                                                                                    int i33 = R.string.AttachPaidMedia;
                                                                                                                                    Object[] objArr = new Object[i7];
                                                                                                                                    objArr[c2] = formatPluralString2;
                                                                                                                                    CharSequence replaceStars = StarsIntroActivity.replaceStars(LocaleController.formatString(i33, objArr));
                                                                                                                                    textPaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                                                    charSequence16 = replaceStars;
                                                                                                                                } else {
                                                                                                                                    i8 = R.string.AttachVideo;
                                                                                                                                    formatPluralString2 = LocaleController.getString(i8);
                                                                                                                                    int i332 = R.string.AttachPaidMedia;
                                                                                                                                    Object[] objArr2 = new Object[i7];
                                                                                                                                    objArr2[c2] = formatPluralString2;
                                                                                                                                    CharSequence replaceStars2 = StarsIntroActivity.replaceStars(LocaleController.formatString(i332, objArr2));
                                                                                                                                    textPaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                                                    charSequence16 = replaceStars2;
                                                                                                                                }
                                                                                                                            } else {
                                                                                                                                c2 = 0;
                                                                                                                                i7 = 1;
                                                                                                                                if (size > 1) {
                                                                                                                                    formatPluralString2 = LocaleController.formatPluralString("Photos", size, new Object[0]);
                                                                                                                                    int i3322 = R.string.AttachPaidMedia;
                                                                                                                                    Object[] objArr22 = new Object[i7];
                                                                                                                                    objArr22[c2] = formatPluralString2;
                                                                                                                                    CharSequence replaceStars22 = StarsIntroActivity.replaceStars(LocaleController.formatString(i3322, objArr22));
                                                                                                                                    textPaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                                                    charSequence16 = replaceStars22;
                                                                                                                                } else {
                                                                                                                                    i8 = R.string.AttachPhoto;
                                                                                                                                    formatPluralString2 = LocaleController.getString(i8);
                                                                                                                                    int i33222 = R.string.AttachPaidMedia;
                                                                                                                                    Object[] objArr222 = new Object[i7];
                                                                                                                                    objArr222[c2] = formatPluralString2;
                                                                                                                                    CharSequence replaceStars222 = StarsIntroActivity.replaceStars(LocaleController.formatString(i33222, objArr222));
                                                                                                                                    textPaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                                                    charSequence16 = replaceStars222;
                                                                                                                                }
                                                                                                                            }
                                                                                                                        } else if (this.thumbsCount > 1) {
                                                                                                                            if (this.hasVideoThumb) {
                                                                                                                                ArrayList arrayList4 = this.groupMessages;
                                                                                                                                formatPluralString = LocaleController.formatPluralString("Media", arrayList4 == null ? 0 : arrayList4.size(), new Object[0]);
                                                                                                                            } else {
                                                                                                                                ArrayList arrayList5 = this.groupMessages;
                                                                                                                                formatPluralString = LocaleController.formatPluralString("Photos", arrayList5 == null ? 0 : arrayList5.size(), new Object[0]);
                                                                                                                            }
                                                                                                                            charSequence16 = formatPluralString;
                                                                                                                            textPaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                                        } else {
                                                                                                                            if (messageMedia2 instanceof TLRPC.TL_messageMediaGiveaway) {
                                                                                                                                TLRPC.MessageFwdHeader messageFwdHeader = message.fwd_from;
                                                                                                                                if (messageFwdHeader != null) {
                                                                                                                                    TLRPC.Peer peer = messageFwdHeader.from_id;
                                                                                                                                    if (peer instanceof TLRPC.TL_peerChannel) {
                                                                                                                                        isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(peer.channel_id, this.currentAccount);
                                                                                                                                        i6 = !isChannelAndNotMegaGroup ? R.string.BoostingGiveawayChannelStarted : R.string.BoostingGiveawayGroupStarted;
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(this.chat);
                                                                                                                                if (!isChannelAndNotMegaGroup) {
                                                                                                                                }
                                                                                                                            } else if (messageMedia2 instanceof TLRPC.TL_messageMediaGiveawayResults) {
                                                                                                                                i6 = R.string.BoostingGiveawayResults;
                                                                                                                            } else if (messageMedia2 instanceof TLRPC.TL_messageMediaPoll) {
                                                                                                                                TLRPC.TL_messageMediaPoll tL_messageMediaPoll = (TLRPC.TL_messageMediaPoll) messageMedia2;
                                                                                                                                TLRPC.TL_textWithEntities tL_textWithEntities = tL_messageMediaPoll.poll.question;
                                                                                                                                if (tL_textWithEntities == null || tL_textWithEntities.entities == null) {
                                                                                                                                    sb = new StringBuilder();
                                                                                                                                    sb.append("📊 ");
                                                                                                                                    str5 = tL_messageMediaPoll.poll.question.text;
                                                                                                                                    sb.append(str5);
                                                                                                                                    charSequence15 = sb.toString();
                                                                                                                                    CharSequence charSequence40 = charSequence15;
                                                                                                                                    messageObject = this.message;
                                                                                                                                    charSequence36 = charSequence40;
                                                                                                                                    if (messageObject.messageOwner.media != null) {
                                                                                                                                        charSequence36 = charSequence40;
                                                                                                                                        if (!messageObject.isMediaEmpty()) {
                                                                                                                                            textPaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                                                            charSequence16 = charSequence40;
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                } else {
                                                                                                                                    SpannableStringBuilder spannableStringBuilder6 = new SpannableStringBuilder(tL_messageMediaPoll.poll.question.text);
                                                                                                                                    TLRPC.TL_textWithEntities tL_textWithEntities2 = tL_messageMediaPoll.poll.question;
                                                                                                                                    MediaDataController.addTextStyleRuns(tL_textWithEntities2.entities, tL_textWithEntities2.text, spannableStringBuilder6);
                                                                                                                                    MediaDataController.addAnimatedEmojiSpans(tL_messageMediaPoll.poll.question.entities, spannableStringBuilder6, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt());
                                                                                                                                    charSequence15 = new SpannableStringBuilder("📊 ").append((CharSequence) spannableStringBuilder6);
                                                                                                                                    CharSequence charSequence402 = charSequence15;
                                                                                                                                    messageObject = this.message;
                                                                                                                                    charSequence36 = charSequence402;
                                                                                                                                    if (messageObject.messageOwner.media != null) {
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            } else if (messageMedia2 instanceof TLRPC.TL_messageMediaGame) {
                                                                                                                                sb = new StringBuilder();
                                                                                                                                sb.append("🎮 ");
                                                                                                                                str5 = this.message.messageOwner.media.game.title;
                                                                                                                                sb.append(str5);
                                                                                                                                charSequence15 = sb.toString();
                                                                                                                                CharSequence charSequence4022 = charSequence15;
                                                                                                                                messageObject = this.message;
                                                                                                                                charSequence36 = charSequence4022;
                                                                                                                                if (messageObject.messageOwner.media != null) {
                                                                                                                                }
                                                                                                                            } else {
                                                                                                                                if (messageMedia2 instanceof TLRPC.TL_messageMediaInvoice) {
                                                                                                                                    charSequence15 = messageMedia2.title;
                                                                                                                                } else if (messageObject13.type == 14) {
                                                                                                                                    charSequence15 = String.format("🎧 %s - %s", messageObject13.getMusicAuthor(), this.message.getMusicTitle());
                                                                                                                                } else if (!(messageMedia2 instanceof TLRPC.TL_messageMediaStory) || !messageMedia2.via_mention) {
                                                                                                                                    if (!messageObject13.hasHighlightedWords() || TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                                                        SpannableStringBuilder spannableStringBuilder7 = new SpannableStringBuilder(charSequence2);
                                                                                                                                        MessageObject messageObject14 = this.message;
                                                                                                                                        if (messageObject14 != null) {
                                                                                                                                            messageObject14.spoilLoginCode();
                                                                                                                                        }
                                                                                                                                        MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder7, NotificationCenter.webRtcMicAmplitudeEvent);
                                                                                                                                        MessageObject messageObject15 = this.message;
                                                                                                                                        charSequence14 = spannableStringBuilder7;
                                                                                                                                        if (messageObject15 != null) {
                                                                                                                                            TLRPC.Message message2 = messageObject15.messageOwner;
                                                                                                                                            charSequence14 = spannableStringBuilder7;
                                                                                                                                            if (message2 != null) {
                                                                                                                                                ArrayList<TLRPC.MessageEntity> arrayList6 = message2.entities;
                                                                                                                                                TextPaint textPaint7 = this.currentMessagePaint;
                                                                                                                                                MediaDataController.addAnimatedEmojiSpans(arrayList6, spannableStringBuilder7, textPaint7 == null ? null : textPaint7.getFontMetricsInt());
                                                                                                                                                charSequence14 = spannableStringBuilder7;
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    } else {
                                                                                                                                        CharSequence charSequence41 = this.message.messageTrimmedToHighlight;
                                                                                                                                        int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 23);
                                                                                                                                        MessageObject messageObject16 = this.message;
                                                                                                                                        charSequence14 = charSequence41;
                                                                                                                                        if (messageObject16.messageTrimmedToHighlightCut) {
                                                                                                                                            charSequence14 = AndroidUtilities.ellipsizeCenterEnd(charSequence41, messageObject16.highlightedWords.get(0), measuredWidth2, this.currentMessagePaint, NotificationCenter.walletSyncProgressChanged);
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    AndroidUtilities.highlightText(charSequence14, this.message.highlightedWords, this.resourcesProvider);
                                                                                                                                    charSequence15 = charSequence14;
                                                                                                                                } else if (messageObject13.isOut()) {
                                                                                                                                    TLRPC.User user5 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.message.getDialogId()));
                                                                                                                                    if (user5 != null) {
                                                                                                                                        str4 = UserObject.getFirstName(user5);
                                                                                                                                        int indexOf2 = str4.indexOf(32);
                                                                                                                                        c = 0;
                                                                                                                                        if (indexOf2 >= 0) {
                                                                                                                                            str4 = str4.substring(0, indexOf2);
                                                                                                                                        }
                                                                                                                                    } else {
                                                                                                                                        c = 0;
                                                                                                                                        str4 = "";
                                                                                                                                    }
                                                                                                                                    int i34 = R.string.StoryYouMentionInDialog;
                                                                                                                                    Object[] objArr3 = new Object[1];
                                                                                                                                    objArr3[c] = str4;
                                                                                                                                    charSequence15 = LocaleController.formatString(i34, objArr3);
                                                                                                                                } else {
                                                                                                                                    i6 = R.string.StoryMentionInDialog;
                                                                                                                                }
                                                                                                                                CharSequence charSequence40222 = charSequence15;
                                                                                                                                messageObject = this.message;
                                                                                                                                charSequence36 = charSequence40222;
                                                                                                                                if (messageObject.messageOwner.media != null) {
                                                                                                                                }
                                                                                                                            }
                                                                                                                            charSequence15 = LocaleController.getString(i6);
                                                                                                                            CharSequence charSequence402222 = charSequence15;
                                                                                                                            messageObject = this.message;
                                                                                                                            charSequence36 = charSequence402222;
                                                                                                                            if (messageObject.messageOwner.media != null) {
                                                                                                                            }
                                                                                                                        }
                                                                                                                        this.currentMessagePaint = textPaint;
                                                                                                                        charSequence36 = charSequence16;
                                                                                                                    }
                                                                                                                }
                                                                                                                charSequence36 = LocaleController.getString(i9);
                                                                                                            }
                                                                                                        }
                                                                                                        if (this.message.isReplyToStory()) {
                                                                                                            SpannableStringBuilder spannableStringBuilder8 = new SpannableStringBuilder(charSequence36);
                                                                                                            str16 = str3;
                                                                                                            spannableStringBuilder8.insert(0, (CharSequence) str16);
                                                                                                            spannableStringBuilder8.setSpan(new ColoredImageSpan(ContextCompat.getDrawable(getContext(), R.drawable.msg_mini_replystory).mutate()), 0, 1, 0);
                                                                                                            charSequence11 = spannableStringBuilder8;
                                                                                                        } else {
                                                                                                            str16 = str3;
                                                                                                            charSequence11 = charSequence36;
                                                                                                        }
                                                                                                        if (this.thumbsCount > 0) {
                                                                                                            if (!this.message.hasHighlightedWords() || TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                                int length2 = charSequence11.length();
                                                                                                                CharSequence charSequence42 = charSequence11;
                                                                                                                if (length2 > 150) {
                                                                                                                    charSequence42 = charSequence11.subSequence(0, 150);
                                                                                                                }
                                                                                                                replaceNewLines = AndroidUtilities.replaceNewLines(charSequence42);
                                                                                                            } else {
                                                                                                                replaceNewLines = this.message.messageTrimmedToHighlight;
                                                                                                                int measuredWidth3 = getMeasuredWidth() - AndroidUtilities.dp(((this.messagePaddingStart + 23) + ((this.thumbSize + 2) * this.thumbsCount)) + 3);
                                                                                                                MessageObject messageObject17 = this.message;
                                                                                                                if (messageObject17.messageTrimmedToHighlightCut) {
                                                                                                                    replaceNewLines = AndroidUtilities.ellipsizeCenterEnd(replaceNewLines, messageObject17.highlightedWords.get(0), measuredWidth3, this.currentMessagePaint, NotificationCenter.walletSyncProgressChanged).toString();
                                                                                                                }
                                                                                                            }
                                                                                                            CharSequence spannableStringBuilder9 = !(replaceNewLines instanceof SpannableStringBuilder) ? new SpannableStringBuilder(replaceNewLines) : replaceNewLines;
                                                                                                            SpannableStringBuilder spannableStringBuilder10 = (SpannableStringBuilder) spannableStringBuilder9;
                                                                                                            spannableStringBuilder10.insert(0, (CharSequence) " ");
                                                                                                            spannableStringBuilder10.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbSize + 2) * this.thumbsCount) + 3)), 0, 1, 33);
                                                                                                            Emoji.replaceEmoji((CharSequence) spannableStringBuilder10, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
                                                                                                            CharSequence charSequence43 = spannableStringBuilder9;
                                                                                                            if (this.message.hasHighlightedWords()) {
                                                                                                                CharSequence highlightText4 = AndroidUtilities.highlightText(spannableStringBuilder10, this.message.highlightedWords, this.resourcesProvider);
                                                                                                                charSequence43 = spannableStringBuilder9;
                                                                                                                if (highlightText4 != null) {
                                                                                                                    charSequence43 = highlightText4;
                                                                                                                }
                                                                                                            }
                                                                                                            z3 = false;
                                                                                                            charSequence12 = charSequence43;
                                                                                                        } else {
                                                                                                            z3 = z5;
                                                                                                            charSequence12 = charSequence11;
                                                                                                        }
                                                                                                        if (this.message.isForwarded() && this.message.needDrawForwarded()) {
                                                                                                            this.drawForwardIcon = true;
                                                                                                            SpannableStringBuilder spannableStringBuilder11 = new SpannableStringBuilder(charSequence12);
                                                                                                            spannableStringBuilder11.insert(0, (CharSequence) str16);
                                                                                                            ColoredImageSpan coloredImageSpan = new ColoredImageSpan(ContextCompat.getDrawable(getContext(), R.drawable.mini_forwarded).mutate());
                                                                                                            coloredImageSpan.setAlpha(0.9f);
                                                                                                            spannableStringBuilder11.setSpan(coloredImageSpan, 0, 1, 0);
                                                                                                            charSequence13 = spannableStringBuilder11;
                                                                                                        } else {
                                                                                                            charSequence13 = charSequence12;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                str16 = str3;
                                                                                                if (removeDiacritics2 == null) {
                                                                                                    removeDiacritics2 = getMessageNameString();
                                                                                                }
                                                                                                CharSequence removeDiacritics3 = AndroidUtilities.removeDiacritics(removeDiacritics2);
                                                                                                TLRPC.Chat chat9 = this.chat;
                                                                                                if (chat9 != null && chat9.forum && !this.isTopic && !this.useFromUserAsAvatar) {
                                                                                                    CharSequence topicIconName = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopicIconName(this.chat, this.message, this.currentMessagePaint);
                                                                                                    if (!TextUtils.isEmpty(topicIconName)) {
                                                                                                        SpannableStringBuilder spannableStringBuilder12 = new SpannableStringBuilder("-");
                                                                                                        ColoredImageSpan coloredImageSpan2 = new ColoredImageSpan(ContextCompat.getDrawable(ApplicationLoader.applicationContext, R.drawable.msg_mini_forumarrow).mutate());
                                                                                                        coloredImageSpan2.setColorKey((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? -1 : Theme.key_chats_nameMessage);
                                                                                                        spannableStringBuilder12.setSpan(coloredImageSpan2, 0, 1, 0);
                                                                                                        SpannableStringBuilder spannableStringBuilder13 = new SpannableStringBuilder();
                                                                                                        spannableStringBuilder13.append(removeDiacritics3).append((CharSequence) spannableStringBuilder12).append(topicIconName);
                                                                                                        charSequence18 = spannableStringBuilder13;
                                                                                                        SpannableStringBuilder messageStringFormatted2 = getMessageStringFormatted(i2, restrictionReason, charSequence18, false);
                                                                                                        if (!this.useFromUserAsAvatar || ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && (this.currentDialogFolderId == 0 || messageStringFormatted2.length() <= 0))) {
                                                                                                            i10 = 0;
                                                                                                        } else {
                                                                                                            try {
                                                                                                                foregroundColorSpanThemable = new ForegroundColorSpanThemable(Theme.key_chats_nameMessage, this.resourcesProvider);
                                                                                                                i10 = charSequence18.length() + 1;
                                                                                                            } catch (Exception e) {
                                                                                                                e = e;
                                                                                                                i10 = 0;
                                                                                                            }
                                                                                                            try {
                                                                                                                messageStringFormatted2.setSpan(foregroundColorSpanThemable, 0, i10, 33);
                                                                                                            } catch (Exception e2) {
                                                                                                                e = e2;
                                                                                                                FileLog.e(e);
                                                                                                                replaceEmoji = Emoji.replaceEmoji((CharSequence) messageStringFormatted2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                                if (this.message.hasHighlightedWords()) {
                                                                                                                }
                                                                                                                if (this.thumbsCount > 0) {
                                                                                                                }
                                                                                                                charSequence19 = replaceEmoji;
                                                                                                                z3 = false;
                                                                                                                z7 = true;
                                                                                                                charSequence23 = charSequence18;
                                                                                                                if (this.currentDialogFolderId == 0) {
                                                                                                                }
                                                                                                                charSequence9 = null;
                                                                                                                string2 = charSequence19;
                                                                                                                CharSequence charSequence322 = string2;
                                                                                                                if (!this.drawForwardIcon) {
                                                                                                                }
                                                                                                                draftMessage5 = this.draftMessage;
                                                                                                                if (draftMessage5 != null) {
                                                                                                                }
                                                                                                                str7 = LocaleController.stringForMessageListDate(i14);
                                                                                                                messageObject4 = this.message;
                                                                                                                if (messageObject4 != null) {
                                                                                                                }
                                                                                                                this.drawCheck1 = false;
                                                                                                                this.drawCheck2 = false;
                                                                                                                this.drawClock = messageObject4 == null && messageObject4.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                                                                                z8 = false;
                                                                                                                this.drawCount = false;
                                                                                                                this.drawMention = false;
                                                                                                                this.drawReactionMention = false;
                                                                                                                this.drawError = false;
                                                                                                                str8 = null;
                                                                                                                str9 = null;
                                                                                                                this.promoDialog = z8;
                                                                                                                MessagesController messagesController2 = MessagesController.getInstance(this.currentAccount);
                                                                                                                CharSequence charSequence332 = charSequence322;
                                                                                                                if (this.dialogsType == 0) {
                                                                                                                }
                                                                                                                str10 = str7;
                                                                                                                if (this.currentDialogFolderId == 0) {
                                                                                                                }
                                                                                                                string3 = LocaleController.getString(i16);
                                                                                                                CharSequence charSequence342 = string3;
                                                                                                                z9 = z3;
                                                                                                                z10 = z16;
                                                                                                                charSequence25 = charSequence9;
                                                                                                                charSequence26 = charSequence332;
                                                                                                                str11 = str8;
                                                                                                                i17 = i5;
                                                                                                                str12 = str9;
                                                                                                                charSequence27 = charSequence342;
                                                                                                                if (z10) {
                                                                                                                }
                                                                                                                if (drawLock2()) {
                                                                                                                }
                                                                                                                if (LocaleController.isRTL) {
                                                                                                                }
                                                                                                                if (this.drawNameLock) {
                                                                                                                }
                                                                                                                if (this.drawClock) {
                                                                                                                }
                                                                                                                this.nameLeft = i20 + intrinsicWidth;
                                                                                                                if (!this.drawPremium) {
                                                                                                                }
                                                                                                                if (this.dialogMuted) {
                                                                                                                }
                                                                                                                dp3 = AndroidUtilities.dp(6.0f) + Theme.dialogs_muteDrawable.getIntrinsicWidth();
                                                                                                                this.nameWidth -= dp3;
                                                                                                            }
                                                                                                        }
                                                                                                        replaceEmoji = Emoji.replaceEmoji((CharSequence) messageStringFormatted2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                        if (this.message.hasHighlightedWords() && (highlightText = AndroidUtilities.highlightText(replaceEmoji, this.message.highlightedWords, this.resourcesProvider)) != null) {
                                                                                                            replaceEmoji = highlightText;
                                                                                                        }
                                                                                                        if (this.thumbsCount > 0) {
                                                                                                            if (!(replaceEmoji instanceof SpannableStringBuilder)) {
                                                                                                                replaceEmoji = new SpannableStringBuilder(replaceEmoji);
                                                                                                            }
                                                                                                            SpannableStringBuilder spannableStringBuilder14 = (SpannableStringBuilder) replaceEmoji;
                                                                                                            if (i10 >= spannableStringBuilder14.length()) {
                                                                                                                spannableStringBuilder14.append((CharSequence) " ");
                                                                                                                spannableStringBuilder14.setSpan(new FixedWidthSpan(AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 3)), spannableStringBuilder14.length() - 1, spannableStringBuilder14.length(), 33);
                                                                                                            } else {
                                                                                                                spannableStringBuilder14.insert(i10, (CharSequence) " ");
                                                                                                                spannableStringBuilder14.setSpan(new FixedWidthSpan(AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 3)), i10, i10 + 1, 33);
                                                                                                            }
                                                                                                        }
                                                                                                        charSequence19 = replaceEmoji;
                                                                                                        z3 = false;
                                                                                                        z7 = true;
                                                                                                        charSequence23 = charSequence18;
                                                                                                        if (this.currentDialogFolderId == 0) {
                                                                                                            charSequence6 = formatArchivedDialogNames();
                                                                                                            z16 = z7;
                                                                                                        } else {
                                                                                                            z16 = z7;
                                                                                                            charSequence6 = charSequence23;
                                                                                                        }
                                                                                                        charSequence9 = null;
                                                                                                        string2 = charSequence19;
                                                                                                    }
                                                                                                }
                                                                                                charSequence18 = removeDiacritics3;
                                                                                                SpannableStringBuilder messageStringFormatted22 = getMessageStringFormatted(i2, restrictionReason, charSequence18, false);
                                                                                                if (this.useFromUserAsAvatar) {
                                                                                                }
                                                                                                i10 = 0;
                                                                                                replaceEmoji = Emoji.replaceEmoji((CharSequence) messageStringFormatted22, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                if (this.message.hasHighlightedWords()) {
                                                                                                    replaceEmoji = highlightText;
                                                                                                }
                                                                                                if (this.thumbsCount > 0) {
                                                                                                }
                                                                                                charSequence19 = replaceEmoji;
                                                                                                z3 = false;
                                                                                                z7 = true;
                                                                                                charSequence23 = charSequence18;
                                                                                                if (this.currentDialogFolderId == 0) {
                                                                                                }
                                                                                                charSequence9 = null;
                                                                                                string2 = charSequence19;
                                                                                            } else {
                                                                                                if (ChatObject.isChannelAndNotMegaGroup(this.chat) && (this.message.messageOwner.action instanceof TLRPC.TL_messageActionChannelMigrateFrom)) {
                                                                                                    charSequence20 = "";
                                                                                                    z17 = false;
                                                                                                } else {
                                                                                                    CharSequence charSequence44 = this.message.messageTextShort;
                                                                                                    charSequence20 = charSequence44 != null ? charSequence44 : charSequence2;
                                                                                                }
                                                                                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                if (this.message.type == 21) {
                                                                                                    updateMessageThumbs();
                                                                                                    charSequence21 = applyThumbs(charSequence20);
                                                                                                } else {
                                                                                                    charSequence21 = charSequence20;
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            charSequence13 = formatArchivedDialogNames();
                                                                                            str16 = str3;
                                                                                            z3 = false;
                                                                                        }
                                                                                        z7 = true;
                                                                                        charSequence22 = charSequence13;
                                                                                        charSequence23 = null;
                                                                                        charSequence19 = charSequence22;
                                                                                        if (this.currentDialogFolderId == 0) {
                                                                                        }
                                                                                        charSequence9 = null;
                                                                                        string2 = charSequence19;
                                                                                    }
                                                                                    charSequence22 = string;
                                                                                    str16 = str3;
                                                                                    z3 = z5;
                                                                                    z7 = false;
                                                                                    z17 = false;
                                                                                    charSequence23 = null;
                                                                                    charSequence19 = charSequence22;
                                                                                    if (this.currentDialogFolderId == 0) {
                                                                                    }
                                                                                    charSequence9 = null;
                                                                                    string2 = charSequence19;
                                                                                }
                                                                                str16 = str3;
                                                                                z3 = z5;
                                                                                charSequence13 = charSequence21;
                                                                                z7 = true;
                                                                                charSequence22 = charSequence13;
                                                                                charSequence23 = null;
                                                                                charSequence19 = charSequence22;
                                                                                if (this.currentDialogFolderId == 0) {
                                                                                }
                                                                                charSequence9 = null;
                                                                                string2 = charSequence19;
                                                                            } else {
                                                                                ReactionsLayoutInBubble.VisibleReaction fromTL = ReactionsLayoutInBubble.VisibleReaction.fromTL(messagePeerReaction.reaction);
                                                                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                String str20 = fromTL.emojicon;
                                                                                if (str20 != null) {
                                                                                    z6 = true;
                                                                                    charSequence10 = LocaleController.formatString(R.string.ReactionInDialog, str20);
                                                                                    z5 = z3;
                                                                                    charSequence4 = charSequence3;
                                                                                } else {
                                                                                    String formatString2 = LocaleController.formatString(R.string.ReactionInDialog, str182);
                                                                                    int indexOf3 = formatString2.indexOf(str182);
                                                                                    SpannableStringBuilder spannableStringBuilder15 = new SpannableStringBuilder(formatString2.replace(str182, "d"));
                                                                                    z5 = z3;
                                                                                    charSequence4 = charSequence3;
                                                                                    long j5 = fromTL.documentId;
                                                                                    TextPaint textPaint8 = this.currentMessagePaint;
                                                                                    spannableStringBuilder15.setSpan(new AnimatedEmojiSpan(j5, textPaint8 == null ? null : textPaint8.getFontMetricsInt()), indexOf3, indexOf3 + 1, 0);
                                                                                    charSequence10 = spannableStringBuilder15;
                                                                                    z6 = true;
                                                                                }
                                                                                if (z6) {
                                                                                }
                                                                                str16 = str3;
                                                                                z3 = z5;
                                                                                charSequence13 = charSequence21;
                                                                                z7 = true;
                                                                                charSequence22 = charSequence13;
                                                                                charSequence23 = null;
                                                                                charSequence19 = charSequence22;
                                                                                if (this.currentDialogFolderId == 0) {
                                                                                }
                                                                                charSequence9 = null;
                                                                                string2 = charSequence19;
                                                                            }
                                                                        }
                                                                    }
                                                                    z5 = z3;
                                                                    charSequence4 = charSequence3;
                                                                    str3 = "d ";
                                                                    charSequence10 = "";
                                                                    z6 = false;
                                                                    if (z6) {
                                                                    }
                                                                    str16 = str3;
                                                                    z3 = z5;
                                                                    charSequence13 = charSequence21;
                                                                    z7 = true;
                                                                    charSequence22 = charSequence13;
                                                                    charSequence23 = null;
                                                                    charSequence19 = charSequence22;
                                                                    if (this.currentDialogFolderId == 0) {
                                                                    }
                                                                    charSequence9 = null;
                                                                    string2 = charSequence19;
                                                                }
                                                                CharSequence charSequence3222 = string2;
                                                                if (!this.drawForwardIcon) {
                                                                    MessageObject messageObject18 = this.message;
                                                                    charSequence3222 = string2;
                                                                    if (messageObject18 != null) {
                                                                        TLRPC.Message message3 = messageObject18.messageOwner;
                                                                        charSequence3222 = string2;
                                                                        if (message3 != null) {
                                                                            charSequence3222 = string2;
                                                                            if (message3.action instanceof TLRPC.TL_messageActionStarGift) {
                                                                                this.drawGiftIcon = true;
                                                                                SpannableStringBuilder spannableStringBuilder16 = new SpannableStringBuilder(string2);
                                                                                spannableStringBuilder16.insert(0, (CharSequence) str16);
                                                                                ColoredImageSpan coloredImageSpan3 = new ColoredImageSpan(ContextCompat.getDrawable(getContext(), R.drawable.mini_gift).mutate());
                                                                                coloredImageSpan3.setScale(1.25f, 1.25f);
                                                                                coloredImageSpan3.spaceScaleX = 0.9f;
                                                                                coloredImageSpan3.setAlpha(0.9f);
                                                                                spannableStringBuilder16.setSpan(coloredImageSpan3, 0, 1, 0);
                                                                                TLRPC.TL_textWithEntities tL_textWithEntities3 = ((TLRPC.TL_messageActionStarGift) this.message.messageOwner.action).message;
                                                                                if (tL_textWithEntities3 != null && !TextUtils.isEmpty(tL_textWithEntities3.text)) {
                                                                                    this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                                                                                }
                                                                                charSequence3222 = spannableStringBuilder16;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                draftMessage5 = this.draftMessage;
                                                                if (draftMessage5 != null) {
                                                                    i14 = draftMessage5.date;
                                                                } else {
                                                                    i14 = this.lastMessageDate;
                                                                    if (i14 == 0) {
                                                                        MessageObject messageObject19 = this.message;
                                                                        if (messageObject19 != null) {
                                                                            i14 = messageObject19.messageOwner.date;
                                                                        } else {
                                                                            str7 = "";
                                                                            messageObject4 = this.message;
                                                                            if (messageObject4 != null || this.isSavedDialog) {
                                                                                this.drawCheck1 = false;
                                                                                this.drawCheck2 = false;
                                                                                this.drawClock = messageObject4 == null && messageObject4.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                                                z8 = false;
                                                                                this.drawCount = false;
                                                                                this.drawMention = false;
                                                                                this.drawReactionMention = false;
                                                                                this.drawError = false;
                                                                                str8 = null;
                                                                                str9 = null;
                                                                            } else {
                                                                                if (this.currentDialogFolderId != 0) {
                                                                                    int i35 = this.unreadCount;
                                                                                    int i36 = this.mentionCount;
                                                                                    int i37 = i35 + i36;
                                                                                    if (i37 <= 0) {
                                                                                        this.drawCount = false;
                                                                                        this.drawMention = false;
                                                                                        str8 = null;
                                                                                    } else if (i35 > i36) {
                                                                                        this.drawCount = true;
                                                                                        this.drawMention = false;
                                                                                        str8 = String.format("%d", Integer.valueOf(i37));
                                                                                    } else {
                                                                                        this.drawCount = false;
                                                                                        this.drawMention = true;
                                                                                        str14 = String.format("%d", Integer.valueOf(i37));
                                                                                        str8 = null;
                                                                                        z12 = false;
                                                                                    }
                                                                                    str14 = null;
                                                                                    z12 = false;
                                                                                } else {
                                                                                    if (this.clearingDialog) {
                                                                                        this.drawCount = false;
                                                                                        str8 = null;
                                                                                        z11 = true;
                                                                                        z12 = false;
                                                                                        z13 = false;
                                                                                    } else {
                                                                                        int i38 = this.unreadCount;
                                                                                        z11 = true;
                                                                                        if (i38 == 0 || (i38 == 1 && i38 == this.mentionCount && messageObject4.messageOwner.mentioned)) {
                                                                                            z12 = false;
                                                                                            if (this.markUnread) {
                                                                                                this.drawCount = true;
                                                                                                z13 = z17;
                                                                                                str8 = "";
                                                                                            } else {
                                                                                                this.drawCount = false;
                                                                                                z13 = z17;
                                                                                                str8 = null;
                                                                                            }
                                                                                        } else {
                                                                                            this.drawCount = true;
                                                                                            z12 = false;
                                                                                            str8 = String.format("%d", Integer.valueOf(i38));
                                                                                            z13 = z17;
                                                                                        }
                                                                                    }
                                                                                    if (this.mentionCount != 0) {
                                                                                        this.drawMention = z11;
                                                                                        str13 = "@";
                                                                                    } else {
                                                                                        this.drawMention = z12;
                                                                                        str13 = null;
                                                                                    }
                                                                                    if (this.reactionMentionCount > 0) {
                                                                                        this.drawReactionMention = z11;
                                                                                        z14 = z13;
                                                                                        if (this.message.isOut() && this.draftMessage == null && z14) {
                                                                                            messageObject5 = this.message;
                                                                                            if (!(messageObject5.messageOwner.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                                                                                                if (messageObject5.isSending()) {
                                                                                                    z15 = false;
                                                                                                    this.drawCheck1 = false;
                                                                                                    this.drawCheck2 = false;
                                                                                                    this.drawClock = true;
                                                                                                    this.drawError = z15;
                                                                                                    str9 = str13;
                                                                                                    z8 = false;
                                                                                                } else {
                                                                                                    if (this.message.isSendError()) {
                                                                                                        this.drawCheck1 = false;
                                                                                                        this.drawCheck2 = false;
                                                                                                        this.drawClock = false;
                                                                                                        this.drawError = true;
                                                                                                        this.drawCount = false;
                                                                                                        this.drawMention = false;
                                                                                                    } else if (this.message.isSent()) {
                                                                                                        boolean z19 = (r9 = this.forumTopic) != null ? true : true;
                                                                                                        this.drawCheck1 = z19;
                                                                                                        this.drawCheck2 = true;
                                                                                                        z15 = false;
                                                                                                        this.drawClock = z15;
                                                                                                        this.drawError = z15;
                                                                                                    }
                                                                                                    str9 = str13;
                                                                                                    z8 = false;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        z15 = false;
                                                                                        this.drawCheck1 = false;
                                                                                        this.drawCheck2 = false;
                                                                                        this.drawClock = z15;
                                                                                        this.drawError = z15;
                                                                                        str9 = str13;
                                                                                        z8 = false;
                                                                                    } else {
                                                                                        str14 = str13;
                                                                                        z17 = z13;
                                                                                    }
                                                                                }
                                                                                this.drawReactionMention = z12;
                                                                                String str21 = str14;
                                                                                z14 = z17;
                                                                                str13 = str21;
                                                                                if (this.message.isOut()) {
                                                                                    messageObject5 = this.message;
                                                                                    if (!(messageObject5.messageOwner.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                                                                                    }
                                                                                }
                                                                                z15 = false;
                                                                                this.drawCheck1 = false;
                                                                                this.drawCheck2 = false;
                                                                                this.drawClock = z15;
                                                                                this.drawError = z15;
                                                                                str9 = str13;
                                                                                z8 = false;
                                                                            }
                                                                            this.promoDialog = z8;
                                                                            MessagesController messagesController22 = MessagesController.getInstance(this.currentAccount);
                                                                            CharSequence charSequence3322 = charSequence3222;
                                                                            if (this.dialogsType == 0) {
                                                                                charSequence3322 = charSequence3222;
                                                                                if (messagesController22.isPromoDialog(this.currentDialogId, true)) {
                                                                                    this.drawPinBackground = true;
                                                                                    this.promoDialog = true;
                                                                                    int i39 = messagesController22.promoDialogType;
                                                                                    if (i39 == MessagesController.PROMO_TYPE_PROXY) {
                                                                                        str7 = LocaleController.getString(R.string.UseProxySponsor);
                                                                                        charSequence3322 = charSequence3222;
                                                                                    } else {
                                                                                        charSequence3322 = charSequence3222;
                                                                                        if (i39 == MessagesController.PROMO_TYPE_PSA) {
                                                                                            str7 = LocaleController.getString("PsaType_" + messagesController22.promoPsaType);
                                                                                            if (TextUtils.isEmpty(str7)) {
                                                                                                str7 = LocaleController.getString(R.string.PsaTypeDefault);
                                                                                            }
                                                                                            charSequence3322 = charSequence3222;
                                                                                            if (!TextUtils.isEmpty(messagesController22.promoPsaMessage)) {
                                                                                                CharSequence charSequence45 = messagesController22.promoPsaMessage;
                                                                                                this.thumbsCount = 0;
                                                                                                charSequence3322 = charSequence45;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            str10 = str7;
                                                                            if (this.currentDialogFolderId == 0) {
                                                                                i16 = R.string.ArchivedChats;
                                                                            } else {
                                                                                TLRPC.Chat chat10 = this.chat;
                                                                                if (chat10 != null) {
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
                                                                                            userName = chat10.title;
                                                                                        }
                                                                                        string3 = AndroidUtilities.removeDiacritics(userName);
                                                                                    }
                                                                                    if (string3 != null && string3.length() == 0) {
                                                                                        i16 = R.string.HiddenName;
                                                                                    }
                                                                                    CharSequence charSequence3422 = string3;
                                                                                    z9 = z3;
                                                                                    z10 = z16;
                                                                                    charSequence25 = charSequence9;
                                                                                    charSequence26 = charSequence3322;
                                                                                    str11 = str8;
                                                                                    i17 = i5;
                                                                                    str12 = str9;
                                                                                    charSequence27 = charSequence3422;
                                                                                } else {
                                                                                    TLRPC.User user6 = this.user;
                                                                                    if (user6 != null) {
                                                                                        if (UserObject.isReplyUser(user6)) {
                                                                                            i15 = R.string.RepliesTitle;
                                                                                        } else if (UserObject.isAnonymous(this.user)) {
                                                                                            i15 = R.string.AnonymousForward;
                                                                                        } else if (!UserObject.isUserSelf(this.user)) {
                                                                                            userName = UserObject.getUserName(this.user);
                                                                                            string3 = AndroidUtilities.removeDiacritics(userName);
                                                                                            if (string3 != null) {
                                                                                                i16 = R.string.HiddenName;
                                                                                            }
                                                                                            CharSequence charSequence34222 = string3;
                                                                                            z9 = z3;
                                                                                            z10 = z16;
                                                                                            charSequence25 = charSequence9;
                                                                                            charSequence26 = charSequence3322;
                                                                                            str11 = str8;
                                                                                            i17 = i5;
                                                                                            str12 = str9;
                                                                                            charSequence27 = charSequence34222;
                                                                                        } else if (this.isSavedDialog) {
                                                                                            i15 = R.string.MyNotes;
                                                                                        } else if (this.useMeForMyMessages) {
                                                                                            i15 = R.string.FromYou;
                                                                                        } else {
                                                                                            if (this.dialogsType == 3) {
                                                                                                this.drawPinBackground = true;
                                                                                            }
                                                                                            i15 = R.string.SavedMessages;
                                                                                        }
                                                                                        string3 = LocaleController.getString(i15);
                                                                                        if (string3 != null) {
                                                                                        }
                                                                                        CharSequence charSequence342222 = string3;
                                                                                        z9 = z3;
                                                                                        z10 = z16;
                                                                                        charSequence25 = charSequence9;
                                                                                        charSequence26 = charSequence3322;
                                                                                        str11 = str8;
                                                                                        i17 = i5;
                                                                                        str12 = str9;
                                                                                        charSequence27 = charSequence342222;
                                                                                    }
                                                                                    string3 = "";
                                                                                    if (string3 != null) {
                                                                                    }
                                                                                    CharSequence charSequence3422222 = string3;
                                                                                    z9 = z3;
                                                                                    z10 = z16;
                                                                                    charSequence25 = charSequence9;
                                                                                    charSequence26 = charSequence3322;
                                                                                    str11 = str8;
                                                                                    i17 = i5;
                                                                                    str12 = str9;
                                                                                    charSequence27 = charSequence3422222;
                                                                                }
                                                                            }
                                                                            string3 = LocaleController.getString(i16);
                                                                            CharSequence charSequence34222222 = string3;
                                                                            z9 = z3;
                                                                            z10 = z16;
                                                                            charSequence25 = charSequence9;
                                                                            charSequence26 = charSequence3322;
                                                                            str11 = str8;
                                                                            i17 = i5;
                                                                            str12 = str9;
                                                                            charSequence27 = charSequence34222222;
                                                                        }
                                                                    }
                                                                }
                                                                str7 = LocaleController.stringForMessageListDate(i14);
                                                                messageObject4 = this.message;
                                                                if (messageObject4 != null) {
                                                                }
                                                                this.drawCheck1 = false;
                                                                this.drawCheck2 = false;
                                                                this.drawClock = messageObject4 == null && messageObject4.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                                z8 = false;
                                                                this.drawCount = false;
                                                                this.drawMention = false;
                                                                this.drawReactionMention = false;
                                                                this.drawError = false;
                                                                str8 = null;
                                                                str9 = null;
                                                                this.promoDialog = z8;
                                                                MessagesController messagesController222 = MessagesController.getInstance(this.currentAccount);
                                                                CharSequence charSequence33222 = charSequence3222;
                                                                if (this.dialogsType == 0) {
                                                                }
                                                                str10 = str7;
                                                                if (this.currentDialogFolderId == 0) {
                                                                }
                                                                string3 = LocaleController.getString(i16);
                                                                CharSequence charSequence342222222 = string3;
                                                                z9 = z3;
                                                                z10 = z16;
                                                                charSequence25 = charSequence9;
                                                                charSequence26 = charSequence33222;
                                                                str11 = str8;
                                                                i17 = i5;
                                                                str12 = str9;
                                                                charSequence27 = charSequence342222222;
                                                            }
                                                            formatString = LocaleController.getString(i13);
                                                            charSequence4 = charSequence3;
                                                            charSequence24 = formatString;
                                                            charSequence9 = null;
                                                            charSequence6 = null;
                                                            charSequence28 = charSequence24;
                                                        }
                                                        charSequence6 = charSequence7;
                                                        charSequence8 = "";
                                                        charSequence9 = null;
                                                        charSequence28 = charSequence8;
                                                    }
                                                    z16 = true;
                                                    string2 = charSequence28;
                                                    CharSequence charSequence32222 = string2;
                                                    if (!this.drawForwardIcon) {
                                                    }
                                                    draftMessage5 = this.draftMessage;
                                                    if (draftMessage5 != null) {
                                                    }
                                                    str7 = LocaleController.stringForMessageListDate(i14);
                                                    messageObject4 = this.message;
                                                    if (messageObject4 != null) {
                                                    }
                                                    this.drawCheck1 = false;
                                                    this.drawCheck2 = false;
                                                    this.drawClock = messageObject4 == null && messageObject4.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                    z8 = false;
                                                    this.drawCount = false;
                                                    this.drawMention = false;
                                                    this.drawReactionMention = false;
                                                    this.drawError = false;
                                                    str8 = null;
                                                    str9 = null;
                                                    this.promoDialog = z8;
                                                    MessagesController messagesController2222 = MessagesController.getInstance(this.currentAccount);
                                                    CharSequence charSequence332222 = charSequence32222;
                                                    if (this.dialogsType == 0) {
                                                    }
                                                    str10 = str7;
                                                    if (this.currentDialogFolderId == 0) {
                                                    }
                                                    string3 = LocaleController.getString(i16);
                                                    CharSequence charSequence3422222222 = string3;
                                                    z9 = z3;
                                                    z10 = z16;
                                                    charSequence25 = charSequence9;
                                                    charSequence26 = charSequence332222;
                                                    str11 = str8;
                                                    i17 = i5;
                                                    str12 = str9;
                                                    charSequence27 = charSequence3422222222;
                                                }
                                            }
                                            draftMessage4 = null;
                                            this.draftMessage = null;
                                            this.draftVoice = false;
                                            if (isForumCell()) {
                                            }
                                            z16 = true;
                                            string2 = charSequence28;
                                            CharSequence charSequence322222 = string2;
                                            if (!this.drawForwardIcon) {
                                            }
                                            draftMessage5 = this.draftMessage;
                                            if (draftMessage5 != null) {
                                            }
                                            str7 = LocaleController.stringForMessageListDate(i14);
                                            messageObject4 = this.message;
                                            if (messageObject4 != null) {
                                            }
                                            this.drawCheck1 = false;
                                            this.drawCheck2 = false;
                                            this.drawClock = messageObject4 == null && messageObject4.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                            z8 = false;
                                            this.drawCount = false;
                                            this.drawMention = false;
                                            this.drawReactionMention = false;
                                            this.drawError = false;
                                            str8 = null;
                                            str9 = null;
                                            this.promoDialog = z8;
                                            MessagesController messagesController22222 = MessagesController.getInstance(this.currentAccount);
                                            CharSequence charSequence3322222 = charSequence322222;
                                            if (this.dialogsType == 0) {
                                            }
                                            str10 = str7;
                                            if (this.currentDialogFolderId == 0) {
                                            }
                                            string3 = LocaleController.getString(i16);
                                            CharSequence charSequence34222222222 = string3;
                                            z9 = z3;
                                            z10 = z16;
                                            charSequence25 = charSequence9;
                                            charSequence26 = charSequence3322222;
                                            str11 = str8;
                                            i17 = i5;
                                            str12 = str9;
                                            charSequence27 = charSequence34222222222;
                                        } else {
                                            draftMessage = null;
                                            if (this.isDialogCell || this.isSavedDialogCell) {
                                                boolean z20 = MediaDataController.getInstance(this.currentAccount).getDraftVoice(this.currentDialogId, (long) getTopicId()) != null;
                                                this.draftVoice = z20;
                                                if (!z20) {
                                                    draftMessage = MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, 0L);
                                                }
                                            } else {
                                                this.draftVoice = false;
                                            }
                                            this.draftMessage = draftMessage;
                                            z2 = this.draftVoice;
                                            if (!z2) {
                                                if (ChatObject.isChannel(this.chat)) {
                                                }
                                                chat2 = this.chat;
                                                if (chat2 != null) {
                                                }
                                                draftMessage4 = null;
                                                if (isForumCell()) {
                                                }
                                                z16 = true;
                                                string2 = charSequence28;
                                                CharSequence charSequence3222222 = string2;
                                                if (!this.drawForwardIcon) {
                                                }
                                                draftMessage5 = this.draftMessage;
                                                if (draftMessage5 != null) {
                                                }
                                                str7 = LocaleController.stringForMessageListDate(i14);
                                                messageObject4 = this.message;
                                                if (messageObject4 != null) {
                                                }
                                                this.drawCheck1 = false;
                                                this.drawCheck2 = false;
                                                this.drawClock = messageObject4 == null && messageObject4.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                z8 = false;
                                                this.drawCount = false;
                                                this.drawMention = false;
                                                this.drawReactionMention = false;
                                                this.drawError = false;
                                                str8 = null;
                                                str9 = null;
                                                this.promoDialog = z8;
                                                MessagesController messagesController222222 = MessagesController.getInstance(this.currentAccount);
                                                CharSequence charSequence33222222 = charSequence3222222;
                                                if (this.dialogsType == 0) {
                                                }
                                                str10 = str7;
                                                if (this.currentDialogFolderId == 0) {
                                                }
                                                string3 = LocaleController.getString(i16);
                                                CharSequence charSequence342222222222 = string3;
                                                z9 = z3;
                                                z10 = z16;
                                                charSequence25 = charSequence9;
                                                charSequence26 = charSequence33222222;
                                                str11 = str8;
                                                i17 = i5;
                                                str12 = str9;
                                                charSequence27 = charSequence342222222222;
                                            }
                                            if (ChatObject.isChannel(this.chat)) {
                                            }
                                            chat2 = this.chat;
                                            if (chat2 != null) {
                                            }
                                            draftMessage4 = null;
                                            if (isForumCell()) {
                                            }
                                            z16 = true;
                                            string2 = charSequence28;
                                            CharSequence charSequence32222222 = string2;
                                            if (!this.drawForwardIcon) {
                                            }
                                            draftMessage5 = this.draftMessage;
                                            if (draftMessage5 != null) {
                                            }
                                            str7 = LocaleController.stringForMessageListDate(i14);
                                            messageObject4 = this.message;
                                            if (messageObject4 != null) {
                                            }
                                            this.drawCheck1 = false;
                                            this.drawCheck2 = false;
                                            this.drawClock = messageObject4 == null && messageObject4.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                            z8 = false;
                                            this.drawCount = false;
                                            this.drawMention = false;
                                            this.drawReactionMention = false;
                                            this.drawError = false;
                                            str8 = null;
                                            str9 = null;
                                            this.promoDialog = z8;
                                            MessagesController messagesController2222222 = MessagesController.getInstance(this.currentAccount);
                                            CharSequence charSequence332222222 = charSequence32222222;
                                            if (this.dialogsType == 0) {
                                            }
                                            str10 = str7;
                                            if (this.currentDialogFolderId == 0) {
                                            }
                                            string3 = LocaleController.getString(i16);
                                            CharSequence charSequence3422222222222 = string3;
                                            z9 = z3;
                                            z10 = z16;
                                            charSequence25 = charSequence9;
                                            charSequence26 = charSequence332222222;
                                            str11 = str8;
                                            i17 = i5;
                                            str12 = str9;
                                            charSequence27 = charSequence3422222222222;
                                        }
                                        draftMessage = null;
                                        this.draftMessage = draftMessage;
                                        z2 = this.draftVoice;
                                        if (!z2) {
                                        }
                                        if (ChatObject.isChannel(this.chat)) {
                                        }
                                        chat2 = this.chat;
                                        if (chat2 != null) {
                                        }
                                        draftMessage4 = null;
                                        if (isForumCell()) {
                                        }
                                        z16 = true;
                                        string2 = charSequence28;
                                        CharSequence charSequence322222222 = string2;
                                        if (!this.drawForwardIcon) {
                                        }
                                        draftMessage5 = this.draftMessage;
                                        if (draftMessage5 != null) {
                                        }
                                        str7 = LocaleController.stringForMessageListDate(i14);
                                        messageObject4 = this.message;
                                        if (messageObject4 != null) {
                                        }
                                        this.drawCheck1 = false;
                                        this.drawCheck2 = false;
                                        this.drawClock = messageObject4 == null && messageObject4.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                        z8 = false;
                                        this.drawCount = false;
                                        this.drawMention = false;
                                        this.drawReactionMention = false;
                                        this.drawError = false;
                                        str8 = null;
                                        str9 = null;
                                        this.promoDialog = z8;
                                        MessagesController messagesController22222222 = MessagesController.getInstance(this.currentAccount);
                                        CharSequence charSequence3322222222 = charSequence322222222;
                                        if (this.dialogsType == 0) {
                                        }
                                        str10 = str7;
                                        if (this.currentDialogFolderId == 0) {
                                        }
                                        string3 = LocaleController.getString(i16);
                                        CharSequence charSequence34222222222222 = string3;
                                        z9 = z3;
                                        z10 = z16;
                                        charSequence25 = charSequence9;
                                        charSequence26 = charSequence3322222222;
                                        str11 = str8;
                                        i17 = i5;
                                        str12 = str9;
                                        charSequence27 = charSequence34222222222222;
                                    }
                                }
                                z = false;
                                this.drawPremium = z;
                                if (z) {
                                }
                                i4 = this.lastMessageDate;
                                if (i4 == 0) {
                                    i4 = messageObject6.messageOwner.date;
                                }
                                if (this.isTopic) {
                                }
                                draftMessage = null;
                                this.draftMessage = draftMessage;
                                z2 = this.draftVoice;
                                if (!z2) {
                                }
                                if (ChatObject.isChannel(this.chat)) {
                                }
                                chat2 = this.chat;
                                if (chat2 != null) {
                                }
                                draftMessage4 = null;
                                if (isForumCell()) {
                                }
                                z16 = true;
                                string2 = charSequence28;
                                CharSequence charSequence3222222222 = string2;
                                if (!this.drawForwardIcon) {
                                }
                                draftMessage5 = this.draftMessage;
                                if (draftMessage5 != null) {
                                }
                                str7 = LocaleController.stringForMessageListDate(i14);
                                messageObject4 = this.message;
                                if (messageObject4 != null) {
                                }
                                this.drawCheck1 = false;
                                this.drawCheck2 = false;
                                this.drawClock = messageObject4 == null && messageObject4.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                z8 = false;
                                this.drawCount = false;
                                this.drawMention = false;
                                this.drawReactionMention = false;
                                this.drawError = false;
                                str8 = null;
                                str9 = null;
                                this.promoDialog = z8;
                                MessagesController messagesController222222222 = MessagesController.getInstance(this.currentAccount);
                                CharSequence charSequence33222222222 = charSequence3222222222;
                                if (this.dialogsType == 0) {
                                }
                                str10 = str7;
                                if (this.currentDialogFolderId == 0) {
                                }
                                string3 = LocaleController.getString(i16);
                                CharSequence charSequence342222222222222 = string3;
                                z9 = z3;
                                z10 = z16;
                                charSequence25 = charSequence9;
                                charSequence26 = charSequence33222222222;
                                str11 = str8;
                                i17 = i5;
                                str12 = str9;
                                charSequence27 = charSequence342222222222222;
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
                            draftMessage = null;
                            this.draftMessage = draftMessage;
                            z2 = this.draftVoice;
                            if (!z2) {
                            }
                            if (ChatObject.isChannel(this.chat)) {
                            }
                            chat2 = this.chat;
                            if (chat2 != null) {
                            }
                            draftMessage4 = null;
                            if (isForumCell()) {
                            }
                            z16 = true;
                            string2 = charSequence28;
                            CharSequence charSequence32222222222 = string2;
                            if (!this.drawForwardIcon) {
                            }
                            draftMessage5 = this.draftMessage;
                            if (draftMessage5 != null) {
                            }
                            str7 = LocaleController.stringForMessageListDate(i14);
                            messageObject4 = this.message;
                            if (messageObject4 != null) {
                            }
                            this.drawCheck1 = false;
                            this.drawCheck2 = false;
                            this.drawClock = messageObject4 == null && messageObject4.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                            z8 = false;
                            this.drawCount = false;
                            this.drawMention = false;
                            this.drawReactionMention = false;
                            this.drawError = false;
                            str8 = null;
                            str9 = null;
                            this.promoDialog = z8;
                            MessagesController messagesController2222222222 = MessagesController.getInstance(this.currentAccount);
                            CharSequence charSequence332222222222 = charSequence32222222222;
                            if (this.dialogsType == 0) {
                            }
                            str10 = str7;
                            if (this.currentDialogFolderId == 0) {
                            }
                            string3 = LocaleController.getString(i16);
                            CharSequence charSequence3422222222222222 = string3;
                            z9 = z3;
                            z10 = z16;
                            charSequence25 = charSequence9;
                            charSequence26 = charSequence332222222222;
                            str11 = str8;
                            i17 = i5;
                            str12 = str9;
                            charSequence27 = charSequence3422222222222222;
                        }
                    }
                }
                charSequence2 = charSequence31;
                i4 = this.lastMessageDate;
                if (i4 == 0) {
                }
                if (this.isTopic) {
                }
                draftMessage = null;
                this.draftMessage = draftMessage;
                z2 = this.draftVoice;
                if (!z2) {
                }
                if (ChatObject.isChannel(this.chat)) {
                }
                chat2 = this.chat;
                if (chat2 != null) {
                }
                draftMessage4 = null;
                if (isForumCell()) {
                }
                z16 = true;
                string2 = charSequence28;
                CharSequence charSequence322222222222 = string2;
                if (!this.drawForwardIcon) {
                }
                draftMessage5 = this.draftMessage;
                if (draftMessage5 != null) {
                }
                str7 = LocaleController.stringForMessageListDate(i14);
                messageObject4 = this.message;
                if (messageObject4 != null) {
                }
                this.drawCheck1 = false;
                this.drawCheck2 = false;
                this.drawClock = messageObject4 == null && messageObject4.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                z8 = false;
                this.drawCount = false;
                this.drawMention = false;
                this.drawReactionMention = false;
                this.drawError = false;
                str8 = null;
                str9 = null;
                this.promoDialog = z8;
                MessagesController messagesController22222222222 = MessagesController.getInstance(this.currentAccount);
                CharSequence charSequence3322222222222 = charSequence322222222222;
                if (this.dialogsType == 0) {
                }
                str10 = str7;
                if (this.currentDialogFolderId == 0) {
                }
                string3 = LocaleController.getString(i16);
                CharSequence charSequence34222222222222222 = string3;
                z9 = z3;
                z10 = z16;
                charSequence25 = charSequence9;
                charSequence26 = charSequence3322222222222;
                str11 = str8;
                i17 = i5;
                str12 = str9;
                charSequence27 = charSequence34222222222222222;
            }
        } else if (LocaleController.isRTL) {
            dp = AndroidUtilities.dp(18.0f);
            this.nameLeft = dp;
            String str1822 = str;
            if (this.encryptedChat == null) {
            }
            charSequence2 = charSequence31;
            i4 = this.lastMessageDate;
            if (i4 == 0) {
            }
            if (this.isTopic) {
            }
            draftMessage = null;
            this.draftMessage = draftMessage;
            z2 = this.draftVoice;
            if (!z2) {
            }
            if (ChatObject.isChannel(this.chat)) {
            }
            chat2 = this.chat;
            if (chat2 != null) {
            }
            draftMessage4 = null;
            if (isForumCell()) {
            }
            z16 = true;
            string2 = charSequence28;
            CharSequence charSequence3222222222222 = string2;
            if (!this.drawForwardIcon) {
            }
            draftMessage5 = this.draftMessage;
            if (draftMessage5 != null) {
            }
            str7 = LocaleController.stringForMessageListDate(i14);
            messageObject4 = this.message;
            if (messageObject4 != null) {
            }
            this.drawCheck1 = false;
            this.drawCheck2 = false;
            this.drawClock = messageObject4 == null && messageObject4.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
            z8 = false;
            this.drawCount = false;
            this.drawMention = false;
            this.drawReactionMention = false;
            this.drawError = false;
            str8 = null;
            str9 = null;
            this.promoDialog = z8;
            MessagesController messagesController222222222222 = MessagesController.getInstance(this.currentAccount);
            CharSequence charSequence33222222222222 = charSequence3222222222222;
            if (this.dialogsType == 0) {
            }
            str10 = str7;
            if (this.currentDialogFolderId == 0) {
            }
            string3 = LocaleController.getString(i16);
            CharSequence charSequence342222222222222222 = string3;
            z9 = z3;
            z10 = z16;
            charSequence25 = charSequence9;
            charSequence26 = charSequence33222222222222;
            str11 = str8;
            i17 = i5;
            str12 = str9;
            charSequence27 = charSequence342222222222222222;
        } else {
            i3 = this.messagePaddingStart + 4;
            dp = AndroidUtilities.dp(i3);
            this.nameLeft = dp;
            String str18222 = str;
            if (this.encryptedChat == null) {
            }
            charSequence2 = charSequence31;
            i4 = this.lastMessageDate;
            if (i4 == 0) {
            }
            if (this.isTopic) {
            }
            draftMessage = null;
            this.draftMessage = draftMessage;
            z2 = this.draftVoice;
            if (!z2) {
            }
            if (ChatObject.isChannel(this.chat)) {
            }
            chat2 = this.chat;
            if (chat2 != null) {
            }
            draftMessage4 = null;
            if (isForumCell()) {
            }
            z16 = true;
            string2 = charSequence28;
            CharSequence charSequence32222222222222 = string2;
            if (!this.drawForwardIcon) {
            }
            draftMessage5 = this.draftMessage;
            if (draftMessage5 != null) {
            }
            str7 = LocaleController.stringForMessageListDate(i14);
            messageObject4 = this.message;
            if (messageObject4 != null) {
            }
            this.drawCheck1 = false;
            this.drawCheck2 = false;
            this.drawClock = messageObject4 == null && messageObject4.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
            z8 = false;
            this.drawCount = false;
            this.drawMention = false;
            this.drawReactionMention = false;
            this.drawError = false;
            str8 = null;
            str9 = null;
            this.promoDialog = z8;
            MessagesController messagesController2222222222222 = MessagesController.getInstance(this.currentAccount);
            CharSequence charSequence332222222222222 = charSequence32222222222222;
            if (this.dialogsType == 0) {
            }
            str10 = str7;
            if (this.currentDialogFolderId == 0) {
            }
            string3 = LocaleController.getString(i16);
            CharSequence charSequence3422222222222222222 = string3;
            z9 = z3;
            z10 = z16;
            charSequence25 = charSequence9;
            charSequence26 = charSequence332222222222222;
            str11 = str8;
            i17 = i5;
            str12 = str9;
            charSequence27 = charSequence3422222222222222222;
        }
        if (z10) {
            this.timeLayout = null;
            this.timeLeft = 0;
            i18 = 0;
        } else {
            i18 = (int) Math.ceil(Theme.dialogs_timePaint.measureText(str10));
            this.timeLayout = new StaticLayout(str10, Theme.dialogs_timePaint, i18, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.timeLeft = !LocaleController.isRTL ? (getMeasuredWidth() - AndroidUtilities.dp(15.0f)) - i18 : AndroidUtilities.dp(15.0f);
        }
        if (drawLock2()) {
            f = 4.0f;
            i19 = 0;
        } else {
            if (LocaleController.isRTL) {
                f = 4.0f;
                intrinsicWidth2 = this.timeLeft + i18 + AndroidUtilities.dp(4.0f);
            } else {
                f = 4.0f;
                intrinsicWidth2 = (this.timeLeft - Theme.dialogs_lock2Drawable.getIntrinsicWidth()) - AndroidUtilities.dp(4.0f);
            }
            this.lock2Left = intrinsicWidth2;
            i19 = Theme.dialogs_lock2Drawable.getIntrinsicWidth() + AndroidUtilities.dp(f);
            i18 += i19;
        }
        if (LocaleController.isRTL) {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(22.0f)) - i18;
        } else {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(this.messagePaddingStart + 13)) - i18;
            this.nameLeft += i18;
        }
        if (this.drawNameLock) {
            int i40 = this.nameWidth;
            if (LocaleController.isRTL) {
                f = 8.0f;
            }
            this.nameWidth = i40 - (AndroidUtilities.dp(f) + Theme.dialogs_lockDrawable.getIntrinsicWidth());
        }
        if (this.drawClock) {
            if (this.drawCheck2) {
                intrinsicWidth = Theme.dialogs_checkDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
                int i41 = this.nameWidth - intrinsicWidth;
                this.nameWidth = i41;
                if (this.drawCheck1) {
                    this.nameWidth = i41 - (Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0f));
                    if (LocaleController.isRTL) {
                        int dp11 = this.timeLeft + i18 + AndroidUtilities.dp(5.0f);
                        this.checkDrawLeft = dp11;
                        this.halfCheckDrawLeft = dp11 + AndroidUtilities.dp(5.5f);
                        i20 = this.nameLeft;
                        intrinsicWidth = (intrinsicWidth + Theme.dialogs_halfCheckDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(8.0f);
                    } else {
                        int i42 = (this.timeLeft - i19) - intrinsicWidth;
                        this.halfCheckDrawLeft = i42;
                        this.checkDrawLeft = i42 - AndroidUtilities.dp(5.5f);
                    }
                } else if (LocaleController.isRTL) {
                    this.checkDrawLeft1 = this.timeLeft + i18 + AndroidUtilities.dp(5.0f);
                    i20 = this.nameLeft;
                } else {
                    this.checkDrawLeft1 = (this.timeLeft - i19) - intrinsicWidth;
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
                this.clockDrawLeft = (this.timeLeft - i19) - intrinsicWidth;
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
                    int dp12 = this.nameWidth - AndroidUtilities.dp(12.0f);
                    if (dp12 < 0) {
                        dp12 = 0;
                    }
                    if (charSequence27 instanceof String) {
                        charSequence27 = ((String) charSequence27).replace('\n', ' ');
                    }
                    if (this.nameLayoutEllipsizeByGradient) {
                        this.nameLayoutFits = charSequence27.length() == TextUtils.ellipsize(charSequence27, Theme.dialogs_namePaint[this.paintIndex], (float) dp12, TextUtils.TruncateAt.END).length();
                        dp12 += AndroidUtilities.dp(48.0f);
                    }
                    float f2 = dp12;
                    this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(charSequence27.toString()) > f2;
                    if (!this.twoLinesForName) {
                        charSequence27 = TextUtils.ellipsize(charSequence27, Theme.dialogs_namePaint[this.paintIndex], f2, TextUtils.TruncateAt.END);
                    }
                    CharSequence replaceEmoji2 = Emoji.replaceEmoji(charSequence27, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    MessageObject messageObject20 = this.message;
                    CharSequence charSequence46 = (messageObject20 == null || !messageObject20.hasHighlightedWords() || (highlightText3 = AndroidUtilities.highlightText(replaceEmoji2, this.message.highlightedWords, this.resourcesProvider)) == null) ? replaceEmoji2 : highlightText3;
                    if (this.twoLinesForName) {
                        this.nameLayout = StaticLayoutEx.createStaticLayout(charSequence46, Theme.dialogs_namePaint[this.paintIndex], dp12, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, dp12, 2);
                    } else {
                        this.nameLayout = new StaticLayout(charSequence46, Theme.dialogs_namePaint[this.paintIndex], Math.max(dp12, this.nameWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    }
                    this.nameLayoutTranslateX = (this.nameLayoutEllipsizeByGradient && this.nameLayout.isRtlCharAt(0)) ? -AndroidUtilities.dp(36.0f) : 0.0f;
                    this.nameLayoutEllipsizeLeft = this.nameLayout.isRtlCharAt(0);
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
                this.animatedEmojiStackName = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStackName, this.nameLayout);
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    int dp13 = AndroidUtilities.dp(11.0f);
                    this.messageNameTop = AndroidUtilities.dp(32.0f);
                    this.timeTop = AndroidUtilities.dp(13.0f);
                    this.errorTop = AndroidUtilities.dp(43.0f);
                    this.pinTop = AndroidUtilities.dp(43.0f);
                    this.countTop = AndroidUtilities.dp(43.0f);
                    this.checkDrawTop = AndroidUtilities.dp(13.0f);
                    int measuredWidth4 = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 21);
                    if (LocaleController.isRTL) {
                        int dp14 = AndroidUtilities.dp(16.0f);
                        this.messageNameLeft = dp14;
                        this.messageLeft = dp14;
                        this.typingLeft = dp14;
                        this.buttonLeft = dp14;
                        dp4 = getMeasuredWidth() - AndroidUtilities.dp(this.avatarStart + 56);
                        dp5 = dp4 - AndroidUtilities.dp(31.0f);
                    } else {
                        int dp15 = AndroidUtilities.dp(this.messagePaddingStart + 6);
                        this.messageNameLeft = dp15;
                        this.messageLeft = dp15;
                        this.typingLeft = dp15;
                        this.buttonLeft = dp15;
                        dp4 = AndroidUtilities.dp(this.avatarStart);
                        dp5 = AndroidUtilities.dp(69.0f) + dp4;
                    }
                    i21 = measuredWidth4;
                    this.storyParams.originalAvatarRect.set(dp4, dp13, dp4 + AndroidUtilities.dp(56.0f), dp13 + AndroidUtilities.dp(56.0f));
                    int i43 = 0;
                    while (true) {
                        ImageReceiver[] imageReceiverArr = this.thumbImage;
                        if (i43 >= imageReceiverArr.length) {
                            break;
                        }
                        imageReceiverArr[i43].setImageCoords(((this.thumbSize + 2) * i43) + dp5, ((dp13 + AndroidUtilities.dp(31.0f)) + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0)) - ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout || (dialogCellTags = this.tags) == null || dialogCellTags.isEmpty()) ? 0 : AndroidUtilities.dp(9.0f)), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                        i43++;
                        dp13 = dp13;
                    }
                    i22 = dp13;
                } else {
                    int dp16 = AndroidUtilities.dp(9.0f);
                    this.messageNameTop = AndroidUtilities.dp(31.0f);
                    this.timeTop = AndroidUtilities.dp(16.0f);
                    this.errorTop = AndroidUtilities.dp(39.0f);
                    this.pinTop = AndroidUtilities.dp(39.0f);
                    this.countTop = this.isTopic ? AndroidUtilities.dp(36.0f) : AndroidUtilities.dp(39.0f);
                    this.checkDrawTop = AndroidUtilities.dp(17.0f);
                    int measuredWidth5 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) - (LocaleController.isRTL ? 0 : 12));
                    if (LocaleController.isRTL) {
                        int dp17 = AndroidUtilities.dp(22.0f);
                        this.messageNameLeft = dp17;
                        this.messageLeft = dp17;
                        this.typingLeft = dp17;
                        this.buttonLeft = dp17;
                        dp8 = getMeasuredWidth() - AndroidUtilities.dp(this.avatarStart + 54);
                        dp9 = dp8 - AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 9);
                    } else {
                        int dp18 = AndroidUtilities.dp(this.messagePaddingStart + 4);
                        this.messageNameLeft = dp18;
                        this.messageLeft = dp18;
                        this.typingLeft = dp18;
                        this.buttonLeft = dp18;
                        dp8 = AndroidUtilities.dp(this.avatarStart);
                        dp9 = AndroidUtilities.dp(67.0f) + dp8;
                    }
                    i21 = measuredWidth5;
                    this.storyParams.originalAvatarRect.set(dp8, dp16, dp8 + AndroidUtilities.dp(54.0f), dp16 + AndroidUtilities.dp(54.0f));
                    int i44 = 0;
                    while (true) {
                        ImageReceiver[] imageReceiverArr2 = this.thumbImage;
                        if (i44 >= imageReceiverArr2.length) {
                            break;
                        }
                        imageReceiverArr2[i44].setImageCoords(((this.thumbSize + 2) * i44) + dp9, ((AndroidUtilities.dp(30.0f) + dp16) + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0)) - ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout || (dialogCellTags3 = this.tags) == null || dialogCellTags3.isEmpty()) ? 0 : AndroidUtilities.dp(9.0f)), AndroidUtilities.dp(this.thumbSize), AndroidUtilities.dp(this.thumbSize));
                        i44++;
                        dp16 = dp16;
                    }
                    i22 = dp16;
                }
                int i45 = i21;
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
                    dp6 = AndroidUtilities.dp(31.0f);
                    i45 -= dp6;
                    if (LocaleController.isRTL) {
                        this.errorLeft = AndroidUtilities.dp(11.0f);
                        this.messageLeft += dp6;
                        this.typingLeft += dp6;
                        this.buttonLeft += dp6;
                        this.messageNameLeft += dp6;
                    } else {
                        this.errorLeft = getMeasuredWidth() - AndroidUtilities.dp(34.0f);
                    }
                } else if (str11 == null && str12 == null && !this.drawReactionMention) {
                    if (this.allowBotOpenButton && UserObject.isBot(this.user) && this.user.bot_has_main_app) {
                        setOpenBotButton(true);
                        int dp19 = (int) (AndroidUtilities.dp(26.0f) + this.openButtonText.getCurrentWidth());
                        int dp20 = AndroidUtilities.dp(13.0f);
                        i45 -= dp19;
                        int dp21 = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 40.0f : this.isTopic ? 33.0f : 36.0f);
                        if (LocaleController.isRTL) {
                            this.openButtonRect.set(AndroidUtilities.dp(13.0f), dp21, AndroidUtilities.dp(13.0f) + dp19, dp21 + AndroidUtilities.dp(28.0f));
                            int i46 = dp19 + dp20;
                            this.messageLeft += i46;
                            this.typingLeft += i46;
                            this.buttonLeft += i46;
                            this.messageNameLeft += i46;
                        } else {
                            this.openButtonRect.set((getMeasuredWidth() - dp19) - AndroidUtilities.dp(13.0f), dp21, getMeasuredWidth() - AndroidUtilities.dp(13.0f), dp21 + AndroidUtilities.dp(28.0f));
                        }
                    } else if (getIsPinned()) {
                        int intrinsicWidth4 = Theme.dialogs_pinnedDrawable.getIntrinsicWidth() + AndroidUtilities.dp(8.0f);
                        i45 -= intrinsicWidth4;
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
                    if (str11 != null) {
                        this.countWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str11)));
                        this.countLayout = new StaticLayout(str11, Theme.dialogs_countTextPaint, this.countWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                        int dp22 = this.countWidth + AndroidUtilities.dp(18.0f);
                        i45 -= dp22;
                        if (LocaleController.isRTL) {
                            this.countLeft = AndroidUtilities.dp(20.0f);
                            this.messageLeft += dp22;
                            this.typingLeft += dp22;
                            this.buttonLeft += dp22;
                            this.messageNameLeft += dp22;
                        } else {
                            this.countLeft = (getMeasuredWidth() - this.countWidth) - AndroidUtilities.dp(20.0f);
                        }
                        this.drawCount = true;
                    } else {
                        this.countWidth = 0;
                    }
                    if (str12 != null) {
                        if (this.currentDialogFolderId != 0) {
                            this.mentionWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str12)));
                            this.mentionLayout = new StaticLayout(str12, Theme.dialogs_countTextPaint, this.mentionWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                        } else {
                            this.mentionWidth = AndroidUtilities.dp(12.0f);
                        }
                        int dp23 = this.mentionWidth + AndroidUtilities.dp(18.0f);
                        i45 -= dp23;
                        if (LocaleController.isRTL) {
                            int dp24 = AndroidUtilities.dp(20.0f);
                            int i47 = this.countWidth;
                            this.mentionLeft = dp24 + (i47 != 0 ? i47 + AndroidUtilities.dp(18.0f) : 0);
                            this.messageLeft += dp23;
                            this.typingLeft += dp23;
                            this.buttonLeft += dp23;
                            this.messageNameLeft += dp23;
                        } else {
                            int measuredWidth6 = (getMeasuredWidth() - this.mentionWidth) - AndroidUtilities.dp(20.0f);
                            int i48 = this.countWidth;
                            this.mentionLeft = measuredWidth6 - (i48 != 0 ? i48 + AndroidUtilities.dp(18.0f) : 0);
                        }
                        this.drawMention = true;
                    } else {
                        this.mentionWidth = 0;
                    }
                    if (this.drawReactionMention) {
                        dp6 = AndroidUtilities.dp(24.0f);
                        i45 -= dp6;
                        if (LocaleController.isRTL) {
                            int dp25 = AndroidUtilities.dp(20.0f);
                            this.reactionMentionLeft = dp25;
                            if (this.drawMention) {
                                int i49 = this.mentionWidth;
                                this.reactionMentionLeft = dp25 + (i49 != 0 ? i49 + AndroidUtilities.dp(18.0f) : 0);
                            }
                            if (this.drawCount) {
                                int i50 = this.reactionMentionLeft;
                                int i51 = this.countWidth;
                                this.reactionMentionLeft = i50 + (i51 != 0 ? i51 + AndroidUtilities.dp(18.0f) : 0);
                            }
                            this.messageLeft += dp6;
                            this.typingLeft += dp6;
                            this.buttonLeft += dp6;
                            this.messageNameLeft += dp6;
                        } else {
                            int measuredWidth7 = getMeasuredWidth() - AndroidUtilities.dp(32.0f);
                            this.reactionMentionLeft = measuredWidth7;
                            if (this.drawMention) {
                                int i52 = this.mentionWidth;
                                this.reactionMentionLeft = measuredWidth7 - (i52 != 0 ? i52 + AndroidUtilities.dp(18.0f) : 0);
                            }
                            if (this.drawCount) {
                                int i53 = this.reactionMentionLeft;
                                int i54 = this.countWidth;
                                this.reactionMentionLeft = i53 - (i54 != 0 ? i54 + AndroidUtilities.dp(18.0f) : 0);
                            }
                        }
                    }
                }
                if (z9) {
                    if (charSequence26 == null) {
                        charSequence26 = "";
                    }
                    if (charSequence26.length() > 150) {
                        charSequence26 = charSequence26.subSequence(0, 150);
                    }
                    CharSequence replaceEmoji3 = Emoji.replaceEmoji(((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags() && charSequence6 == null) ? AndroidUtilities.replaceTwoNewLinesToOne(charSequence26) : AndroidUtilities.replaceNewLines(charSequence26), Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
                    MessageObject messageObject21 = this.message;
                    if (messageObject21 == null || (charSequence26 = AndroidUtilities.highlightText(replaceEmoji3, messageObject21.highlightedWords, this.resourcesProvider)) == null) {
                        charSequence26 = replaceEmoji3;
                    }
                }
                int max = Math.max(AndroidUtilities.dp(12.0f), i45);
                this.buttonTop = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 58.0f : 62.0f);
                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                    this.buttonTop -= AndroidUtilities.dp(isForumCell() ? 10.0f : 12.0f);
                }
                if (isForumCell()) {
                    this.messageTop = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 34.0f : 39.0f);
                    int i55 = 0;
                    while (true) {
                        ImageReceiver[] imageReceiverArr3 = this.thumbImage;
                        if (i55 >= imageReceiverArr3.length) {
                            break;
                        }
                        imageReceiverArr3[i55].setImageY(this.buttonTop);
                        i55++;
                    }
                } else if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags() && charSequence6 != null && (this.currentDialogFolderId == 0 || this.currentDialogFolderDialogsCount == 1)) {
                    try {
                        MessageObject messageObject22 = this.message;
                        if (messageObject22 != null && messageObject22.hasHighlightedWords() && (highlightText2 = AndroidUtilities.highlightText(charSequence6, this.message.highlightedWords, this.resourcesProvider)) != null) {
                            charSequence6 = highlightText2;
                        }
                        this.messageNameLayout = StaticLayoutEx.createStaticLayout(charSequence6, Theme.dialogs_messageNamePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max, 1);
                    } catch (Exception e4) {
                        FileLog.e(e4);
                    }
                    this.messageTop = AndroidUtilities.dp(51.0f);
                    int dp26 = (this.nameIsEllipsized && this.isTopic) ? AndroidUtilities.dp(20.0f) : 0;
                    int i56 = 0;
                    while (true) {
                        ImageReceiver[] imageReceiverArr4 = this.thumbImage;
                        if (i56 >= imageReceiverArr4.length) {
                            break;
                        }
                        imageReceiverArr4[i56].setImageY(i22 + dp26 + AndroidUtilities.dp(40.0f));
                        i56++;
                    }
                } else {
                    this.messageNameLayout = null;
                    if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                        this.messageTop = AndroidUtilities.dp(32.0f);
                        int dp27 = (this.nameIsEllipsized && this.isTopic) ? AndroidUtilities.dp(20.0f) : 0;
                        int i57 = 0;
                        while (true) {
                            ImageReceiver[] imageReceiverArr5 = this.thumbImage;
                            if (i57 >= imageReceiverArr5.length) {
                                break;
                            }
                            imageReceiverArr5[i57].setImageY(i22 + dp27 + AndroidUtilities.dp(21.0f));
                            i57++;
                        }
                    } else {
                        this.messageTop = AndroidUtilities.dp(39.0f);
                    }
                }
                CharSequence charSequence47 = charSequence6;
                if (this.twoLinesForName) {
                    this.messageTop += AndroidUtilities.dp(20.0f);
                }
                this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack2, this.messageNameLayout);
                try {
                    this.buttonCreated = false;
                    if (TextUtils.isEmpty(charSequence25)) {
                        this.buttonLayout = null;
                    } else {
                        this.buttonLayout = new StaticLayout(TextUtils.ellipsize(Emoji.replaceEmoji(charSequence25, this.currentMessagePaint.getFontMetricsInt(), AndroidUtilities.dp(17.0f), false), this.currentMessagePaint, max - AndroidUtilities.dp(26.0f), TextUtils.TruncateAt.END), this.currentMessagePaint, max - AndroidUtilities.dp(20.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        this.spoilersPool2.addAll(this.spoilers2);
                        this.spoilers2.clear();
                        SpoilerEffect.addSpoilers(this, this.buttonLayout, this.spoilersPool2, this.spoilers2);
                    }
                } catch (Exception unused) {
                }
                this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack3, this.buttonLayout);
                try {
                    if (!TextUtils.isEmpty(charSequence4)) {
                        if (!this.useForceThreeLines) {
                            if (SharedConfig.useThreeLinesLayout) {
                            }
                            createStaticLayout = new StaticLayout(TextUtils.ellipsize(charSequence4, this.currentMessagePaint, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END), Theme.dialogs_messagePrintingPaint[this.paintIndex], max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                            this.typingLayout = createStaticLayout;
                        }
                        if (!hasTags()) {
                            createStaticLayout = StaticLayoutEx.createStaticLayout(charSequence4, Theme.dialogs_messagePrintingPaint[this.paintIndex], max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, 1);
                            this.typingLayout = createStaticLayout;
                        }
                        createStaticLayout = new StaticLayout(TextUtils.ellipsize(charSequence4, this.currentMessagePaint, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END), Theme.dialogs_messagePrintingPaint[this.paintIndex], max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        this.typingLayout = createStaticLayout;
                    }
                } catch (Exception e5) {
                    FileLog.e(e5);
                }
                try {
                    if (charSequence26 instanceof Spannable) {
                        try {
                            Spannable spannable = (Spannable) charSequence26;
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
                            i23 = 1;
                            this.messageLayout = staticLayout2;
                            FileLog.e(e);
                            i24 = max;
                            AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans = this.animatedEmojiStack;
                            Layout[] layoutArr = new Layout[i23];
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
                    i23 = 1;
                }
                if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
                    try {
                    } catch (Exception e8) {
                        e = e8;
                        StaticLayout staticLayout22 = null;
                        i23 = 1;
                        this.messageLayout = staticLayout22;
                        FileLog.e(e);
                        i24 = max;
                        AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans2 = this.animatedEmojiStack;
                        Layout[] layoutArr2 = new Layout[i23];
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
                            charSequence29 = charSequence47;
                            charSequence47 = null;
                            alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                            if ((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
                                if (this.thumbsCount > 0 && charSequence47 != null) {
                                    max += AndroidUtilities.dp(5.0f);
                                }
                                this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence29, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence47 == null ? 1 : 2);
                            } else {
                                if (this.thumbsCount > 0) {
                                    max += AndroidUtilities.dp((r0 * (this.thumbSize + 2)) + 3);
                                    if (LocaleController.isRTL && !isForumCell()) {
                                        this.messageLeft -= AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 3);
                                    }
                                }
                                this.messageLayout = new StaticLayout(charSequence29, this.currentMessagePaint, max, alignment, 1.0f, 0.0f, false);
                            }
                            i24 = max;
                            this.spoilersPool.addAll(this.spoilers);
                            this.spoilers.clear();
                            i23 = 1;
                            SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                            AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans22 = this.animatedEmojiStack;
                            Layout[] layoutArr22 = new Layout[i23];
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
                                            double dp28 = AndroidUtilities.dp(6.0f);
                                            Double.isNaN(dp28);
                                            d = d4 - dp28;
                                            drawable = Theme.dialogs_verifiedDrawable;
                                            i27 = drawable.getIntrinsicWidth();
                                            double d5 = i27;
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
                                                i27 = AndroidUtilities.dp(24.0f);
                                            } else if (this.drawScam != 0) {
                                                double d11 = this.nameLeft;
                                                double d12 = this.nameWidth;
                                                Double.isNaN(d12);
                                                Double.isNaN(d11);
                                                double d13 = d11 + (d12 - ceil);
                                                double dp29 = AndroidUtilities.dp(6.0f);
                                                Double.isNaN(dp29);
                                                d = d13 - dp29;
                                                i27 = (this.drawScam == i23 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).getIntrinsicWidth();
                                            }
                                            double d52 = i27;
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
                                    double dp30 = AndroidUtilities.dp(6.0f);
                                    Double.isNaN(dp30);
                                    d = d16 - dp30;
                                    drawable = Theme.dialogs_muteDrawable;
                                    i27 = drawable.getIntrinsicWidth();
                                    double d522 = i27;
                                    Double.isNaN(d522);
                                    this.nameMuteLeft = (int) (d - d522);
                                    if (lineLeft == 0.0f) {
                                    }
                                }
                                StaticLayout staticLayout4 = this.messageLayout;
                                if (staticLayout4 != null && (lineCount6 = staticLayout4.getLineCount()) > 0) {
                                    int i58 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                    int i59 = 0;
                                    while (true) {
                                        if (i59 >= lineCount6) {
                                            break;
                                        }
                                        if (this.messageLayout.getLineLeft(i59) != 0.0f) {
                                            i58 = 0;
                                            break;
                                        }
                                        double ceil2 = Math.ceil(this.messageLayout.getLineWidth(i59));
                                        double d17 = i24;
                                        Double.isNaN(d17);
                                        i58 = Math.min(i58, (int) (d17 - ceil2));
                                        i59 += i23;
                                    }
                                    if (i58 != Integer.MAX_VALUE) {
                                        this.messageLeft += i58;
                                    }
                                }
                                StaticLayout staticLayout5 = this.typingLayout;
                                if (staticLayout5 == null || (lineCount5 = staticLayout5.getLineCount()) <= 0) {
                                    i26 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                } else {
                                    int i60 = 0;
                                    int i61 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                    while (true) {
                                        if (i60 >= lineCount5) {
                                            i26 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                            break;
                                        }
                                        if (this.typingLayout.getLineLeft(i60) != 0.0f) {
                                            i26 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                            i61 = 0;
                                            break;
                                        } else {
                                            double ceil3 = Math.ceil(this.typingLayout.getLineWidth(i60));
                                            double d18 = i24;
                                            Double.isNaN(d18);
                                            i61 = Math.min(i61, (int) (d18 - ceil3));
                                            i60 += i23;
                                        }
                                    }
                                    if (i61 != i26) {
                                        this.typingLeft += i61;
                                    }
                                }
                                StaticLayout staticLayout6 = this.messageNameLayout;
                                if (staticLayout6 != null && staticLayout6.getLineCount() > 0 && this.messageNameLayout.getLineLeft(0) == 0.0f) {
                                    double ceil4 = Math.ceil(this.messageNameLayout.getLineWidth(0));
                                    double d19 = i24;
                                    if (ceil4 < d19) {
                                        double d20 = this.messageNameLeft;
                                        Double.isNaN(d19);
                                        Double.isNaN(d20);
                                        this.messageNameLeft = (int) (d20 + (d19 - ceil4));
                                    }
                                }
                                StaticLayout staticLayout7 = this.buttonLayout;
                                if (staticLayout7 != null && (lineCount4 = staticLayout7.getLineCount()) > 0) {
                                    for (int i62 = 0; i62 < lineCount4; i62 += i23) {
                                        i26 = (int) Math.min(i26, this.buttonLayout.getWidth() - this.buttonLayout.getLineRight(i62));
                                    }
                                    this.buttonLeft += i26;
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
                                    for (int i63 = 0; i63 < lineCount3; i63 += i23) {
                                        f3 = Math.min(f3, this.messageLayout.getLineLeft(i63));
                                    }
                                    this.messageLeft = (int) (this.messageLeft - f3);
                                }
                                StaticLayout staticLayout10 = this.buttonLayout;
                                if (staticLayout10 != null && (lineCount2 = staticLayout10.getLineCount()) > 0) {
                                    float f4 = 2.14748365E9f;
                                    for (int i64 = 0; i64 < lineCount2; i64 += i23) {
                                        f4 = Math.min(f4, this.buttonLayout.getLineLeft(i64));
                                    }
                                    this.buttonLeft = (int) (this.buttonLeft - f4);
                                }
                                StaticLayout staticLayout11 = this.typingLayout;
                                if (staticLayout11 != null && (lineCount = staticLayout11.getLineCount()) > 0) {
                                    float f5 = 2.14748365E9f;
                                    for (int i65 = 0; i65 < lineCount; i65 += i23) {
                                        f5 = Math.min(f5, this.typingLayout.getLineLeft(i65));
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
                                if (i17 >= 0 || (i25 = i17 + 1) >= this.typingLayout.getText().length()) {
                                    primaryHorizontal = this.typingLayout.getPrimaryHorizontal(0);
                                    primaryHorizontal2 = this.typingLayout.getPrimaryHorizontal(i23);
                                } else {
                                    primaryHorizontal = this.typingLayout.getPrimaryHorizontal(i17);
                                    primaryHorizontal2 = this.typingLayout.getPrimaryHorizontal(i25);
                                }
                                this.statusDrawableLeft = primaryHorizontal >= primaryHorizontal2 ? (int) (this.typingLeft + primaryHorizontal) : (int) (this.typingLeft + primaryHorizontal2 + AndroidUtilities.dp(3.0f));
                            }
                            updateThumbsPosition();
                            return;
                        }
                        if (!this.useForceThreeLines) {
                        }
                        if (!hasTags()) {
                            if (charSequence47 != null) {
                            }
                            charSequence29 = charSequence26;
                            alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                            if (this.useForceThreeLines) {
                            }
                            if (this.thumbsCount > 0) {
                                max += AndroidUtilities.dp(5.0f);
                            }
                            this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence29, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence47 == null ? 1 : 2);
                            i24 = max;
                            this.spoilersPool.addAll(this.spoilers);
                            this.spoilers.clear();
                            i23 = 1;
                            SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                            AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans222 = this.animatedEmojiStack;
                            Layout[] layoutArr222 = new Layout[i23];
                            layoutArr222[0] = this.messageLayout;
                            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, emojiGroupedSpans222, layoutArr222);
                            if (LocaleController.isRTL) {
                            }
                            staticLayout = this.typingLayout;
                            if (staticLayout != null) {
                                if (i17 >= 0) {
                                }
                                primaryHorizontal = this.typingLayout.getPrimaryHorizontal(0);
                                primaryHorizontal2 = this.typingLayout.getPrimaryHorizontal(i23);
                                this.statusDrawableLeft = primaryHorizontal >= primaryHorizontal2 ? (int) (this.typingLeft + primaryHorizontal) : (int) (this.typingLeft + primaryHorizontal2 + AndroidUtilities.dp(3.0f));
                            }
                            updateThumbsPosition();
                            return;
                        }
                        if (isForumCell() && (charSequence26 instanceof Spanned) && ((FixedWidthSpan[]) ((Spanned) charSequence26).getSpans(0, charSequence26.length(), FixedWidthSpan.class)).length <= 0) {
                            textPaint2 = this.currentMessagePaint;
                            dp7 = max - AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 15);
                            truncateAt = TextUtils.TruncateAt.END;
                        } else {
                            textPaint2 = this.currentMessagePaint;
                            dp7 = max - AndroidUtilities.dp(12.0f);
                            truncateAt = TextUtils.TruncateAt.END;
                        }
                        charSequence26 = TextUtils.ellipsize(charSequence26, textPaint2, dp7, truncateAt);
                        charSequence29 = charSequence26;
                        alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                        if (this.useForceThreeLines) {
                        }
                        if (this.thumbsCount > 0) {
                        }
                        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence29, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence47 == null ? 1 : 2);
                        i24 = max;
                        this.spoilersPool.addAll(this.spoilers);
                        this.spoilers.clear();
                        i23 = 1;
                        SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                        AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans2222 = this.animatedEmojiStack;
                        Layout[] layoutArr2222 = new Layout[i23];
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
                dp7 = max - AndroidUtilities.dp(12.0f);
                truncateAt = TextUtils.TruncateAt.END;
                charSequence26 = TextUtils.ellipsize(charSequence26, textPaint2, dp7, truncateAt);
                charSequence29 = charSequence26;
                alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                if (this.useForceThreeLines) {
                }
                if (this.thumbsCount > 0) {
                }
                this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence29, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence47 == null ? 1 : 2);
                i24 = max;
                this.spoilersPool.addAll(this.spoilers);
                this.spoilers.clear();
                i23 = 1;
                SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans22222 = this.animatedEmojiStack;
                Layout[] layoutArr22222 = new Layout[i23];
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
            this.clockDrawLeft = this.timeLeft + i18 + AndroidUtilities.dp(5.0f);
            i20 = this.nameLeft;
        }
        this.nameLeft = i20 + intrinsicWidth;
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

    /* JADX WARN: Code restructure failed: missing block: B:56:0x0234, code lost:
    
        if (r3 > 0) goto L61;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x06b6  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x06c4  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x05cb  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x064f  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x065c  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x0682  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x06ad  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0695  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x067b  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0242  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x029f  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0315  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02b5  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0274  */
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
        TLRPC.Chat chat = this.chat;
        if (chat == null || (chat.flags2 & 2048) == 0) {
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
                Drawable drawable = this.starBg;
                this.starBgColor = color;
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
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
            this.lockDrawable.setBounds((int) (centerX - (((r4.getIntrinsicWidth() / 2.0f) * 0.875f) * f2)), (int) (centerY - (((this.lockDrawable.getIntrinsicHeight() / 2.0f) * 0.875f) * f2)), (int) (centerX + ((this.lockDrawable.getIntrinsicWidth() / 2.0f) * 0.875f * f2)), (int) (centerY + ((this.lockDrawable.getIntrinsicHeight() / 2.0f) * 0.875f * f2)));
            this.lockDrawable.setAlpha((int) (f2 * 255.0f));
            this.lockDrawable.draw(canvas);
            canvas.restore();
            return false;
        }
        if (!this.isDialogCell || this.currentDialogFolderId != 0 || z) {
            return false;
        }
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
        TLRPC.User user = this.user;
        if (user != null && !MessagesController.isSupportUser(user) && !this.user.bot) {
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
        TLRPC.Chat chat2 = this.chat;
        if (chat2 != null) {
            boolean z5 = chat2.call_active && chat2.call_not_empty;
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
        TLRPC.Chat chat;
        TLRPC.User user;
        String str;
        TLRPC.Message message;
        TLRPC.MessageFwdHeader messageFwdHeader;
        String str2;
        MessageObject messageObject;
        TLRPC.Message message2;
        TLRPC.User user2;
        MessageObject messageObject2;
        TLRPC.Message message3;
        TLRPC.MessageFwdHeader messageFwdHeader2;
        TLRPC.Message message4;
        TLRPC.MessageFwdHeader messageFwdHeader3;
        TLRPC.MessageFwdHeader messageFwdHeader4;
        MessageObject messageObject3 = this.message;
        if (messageObject3 == null) {
            return null;
        }
        long fromChatId = messageObject3.getFromChatId();
        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        if (!this.isSavedDialog && this.currentDialogId == clientUserId) {
            long savedDialogId = this.message.getSavedDialogId();
            if (savedDialogId == clientUserId) {
                return null;
            }
            if (savedDialogId != UserObject.ANONYMOUS) {
                TLRPC.Message message5 = this.message.messageOwner;
                if (message5 != null && (messageFwdHeader4 = message5.fwd_from) != null) {
                    long peerDialogId = DialogObject.getPeerDialogId(messageFwdHeader4.saved_from_id);
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
        if (this.isSavedDialog && (message4 = this.message.messageOwner) != null && (messageFwdHeader3 = message4.fwd_from) != null) {
            fromChatId = DialogObject.getPeerDialogId(messageFwdHeader3.saved_from_id);
            if (fromChatId == 0) {
                fromChatId = DialogObject.getPeerDialogId(this.message.messageOwner.fwd_from.from_id);
            }
        }
        if (DialogObject.isUserDialog(fromChatId)) {
            user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(fromChatId));
            chat = null;
        } else {
            chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-fromChatId));
            user = null;
        }
        long j = this.currentDialogId;
        if (j == clientUserId) {
            if (user != null) {
                return AndroidUtilities.removeDiacritics(UserObject.getFirstName(user).replace("\n", ""));
            }
            if (chat != null) {
                return AndroidUtilities.removeDiacritics(chat.title.replace("\n", ""));
            }
            return null;
        }
        if (j == UserObject.VERIFY && (messageObject2 = this.message) != null && (message3 = messageObject2.messageOwner) != null && (messageFwdHeader2 = message3.fwd_from) != null) {
            String str3 = messageFwdHeader2.from_name;
            if (str3 != null) {
                return AndroidUtilities.removeDiacritics(str3);
            }
            long peerDialogId2 = DialogObject.getPeerDialogId(messageFwdHeader2.from_id);
            if (DialogObject.isUserDialog(peerDialogId2)) {
                return UserObject.getUserName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerDialogId2)));
            }
            TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerDialogId2));
            return chat2 == null ? "" : chat2.title;
        }
        if (this.message.isOutOwner() && user != null) {
            return LocaleController.getString(R.string.FromYou);
        }
        if (!this.isSavedDialog && (messageObject = this.message) != null && (message2 = messageObject.messageOwner) != null && (message2.from_id instanceof TLRPC.TL_peerUser) && (user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.message.messageOwner.from_id.user_id))) != null) {
            return AndroidUtilities.removeDiacritics(UserObject.getFirstName(user2).replace("\n", ""));
        }
        MessageObject messageObject4 = this.message;
        return (messageObject4 == null || (message = messageObject4.messageOwner) == null || (messageFwdHeader = message.fwd_from) == null || (str2 = messageFwdHeader.from_name) == null) ? user != null ? (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? UserObject.isDeleted(user) ? LocaleController.getString(R.string.HiddenName) : AndroidUtilities.removeDiacritics(ContactsController.formatName(user.first_name, user.last_name).replace("\n", "")) : AndroidUtilities.removeDiacritics(UserObject.getFirstName(user).replace("\n", "")) : (chat == null || (str = chat.title) == null) ? "DELETED" : AndroidUtilities.removeDiacritics(str.replace("\n", "")) : AndroidUtilities.removeDiacritics(str2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:172:0x035b, code lost:
    
        if (r20 != false) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0165, code lost:
    
        if (r20 != false) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x035d, code lost:
    
        r3 = applyThumbs(r3);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r16v0, types: [android.view.View, org.telegram.ui.Cells.DialogCell] */
    /* JADX WARN: Type inference failed for: r3v18, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r3v19, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r3v24, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r3v4, types: [android.text.Spannable, android.text.SpannableStringBuilder, java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r3v46, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r3v47 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public SpannableStringBuilder getMessageStringFormatted(int i, String str, CharSequence charSequence, boolean z) {
        ?? spannableStringBuilder;
        TLRPC.Message message;
        CharSequence charSequence2;
        int i2;
        String formatPluralString;
        CharSequence charSequence3;
        SpannableStringBuilder valueOf;
        TLRPC.TL_forumTopic findTopic;
        MessageObject captionMessage = getCaptionMessage();
        MessageObject messageObject = this.message;
        CharSequence charSequence4 = messageObject != null ? messageObject.messageText : null;
        this.applyName = true;
        if (TextUtils.isEmpty(str)) {
            MessageObject messageObject2 = this.message;
            TLRPC.Message message2 = messageObject2.messageOwner;
            if (message2 instanceof TLRPC.TL_messageService) {
                CharSequence charSequence5 = messageObject2.messageTextShort;
                if (charSequence5 == null || ((message2.action instanceof TLRPC.TL_messageActionTopicCreate) && this.isTopic)) {
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
                if (!z) {
                    return valueOf;
                }
                applyThumbs(valueOf);
                return valueOf;
            }
            if (captionMessage != null && (charSequence3 = captionMessage.caption) != null) {
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
                    if (measuredWidth > 0 && captionMessage.messageTrimmedToHighlightCut) {
                        charSequence7 = AndroidUtilities.ellipsizeCenterEnd(charSequence7, captionMessage.highlightedWords.get(0), measuredWidth, this.currentMessagePaint, NotificationCenter.walletSyncProgressChanged).toString();
                    }
                    return new SpannableStringBuilder(str2).append(charSequence7);
                }
                if (charSequence6.length() > 150) {
                    charSequence6 = charSequence6.subSequence(0, 150);
                }
                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(charSequence6);
                captionMessage.spoilLoginCode();
                MediaDataController.addTextStyleRuns(captionMessage.messageOwner.entities, charSequence6, spannableStringBuilder2, NotificationCenter.webRtcMicAmplitudeEvent);
                TLRPC.Message message3 = captionMessage.messageOwner;
                if (message3 != null) {
                    ArrayList<TLRPC.MessageEntity> arrayList = message3.entities;
                    TextPaint textPaint = this.currentMessagePaint;
                    MediaDataController.addAnimatedEmojiSpans(arrayList, spannableStringBuilder2, textPaint != null ? textPaint.getFontMetricsInt() : null);
                }
                spannableStringBuilder = new SpannableStringBuilder(str2).append(AndroidUtilities.replaceNewLines(spannableStringBuilder2));
            } else {
                if (message2.media != null && !messageObject2.isMediaEmpty()) {
                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                    int i3 = Theme.key_chats_attachMessage;
                    MessageObject messageObject3 = this.message;
                    TLRPC.MessageMedia messageMedia = messageObject3.messageOwner.media;
                    if (messageMedia instanceof TLRPC.TL_messageMediaPoll) {
                        TLRPC.TL_messageMediaPoll tL_messageMediaPoll = (TLRPC.TL_messageMediaPoll) messageMedia;
                        TLRPC.TL_textWithEntities tL_textWithEntities = tL_messageMediaPoll.poll.question;
                        if (tL_textWithEntities == null || tL_textWithEntities.entities == null) {
                            charSequence2 = String.format("📊 \u2068%s\u2069", tL_textWithEntities.text);
                        } else {
                            SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(tL_messageMediaPoll.poll.question.text.replace('\n', ' '));
                            TLRPC.TL_textWithEntities tL_textWithEntities2 = tL_messageMediaPoll.poll.question;
                            MediaDataController.addTextStyleRuns(tL_textWithEntities2.entities, tL_textWithEntities2.text, spannableStringBuilder3);
                            MediaDataController.addAnimatedEmojiSpans(tL_messageMediaPoll.poll.question.entities, spannableStringBuilder3, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt());
                            charSequence2 = new SpannableStringBuilder("📊 \u2068").append((CharSequence) spannableStringBuilder3).append((CharSequence) "\u2069");
                        }
                    } else if (messageMedia instanceof TLRPC.TL_messageMediaGame) {
                        charSequence2 = String.format("🎮 \u2068%s\u2069", messageMedia.game.title);
                    } else if (messageMedia instanceof TLRPC.TL_messageMediaInvoice) {
                        charSequence2 = messageMedia.title;
                    } else if (messageObject3.type == 14) {
                        charSequence2 = String.format("🎧 \u2068%s - %s\u2069", messageObject3.getMusicAuthor(), this.message.getMusicTitle());
                    } else {
                        if (messageMedia instanceof TLRPC.TL_messageMediaPaidMedia) {
                            int size = ((TLRPC.TL_messageMediaPaidMedia) messageMedia).extended_media.size();
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
                }
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
                MediaDataController.addTextStyleRuns(this.message, (Spannable) spannableStringBuilder, NotificationCenter.webRtcMicAmplitudeEvent);
                MessageObject messageObject6 = this.message;
                if (messageObject6 != null && (message = messageObject6.messageOwner) != null) {
                    ArrayList<TLRPC.MessageEntity> arrayList4 = message.entities;
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
        TLRPC.Chat chat;
        return (isDialogFolder() || (chat = this.chat) == null || !chat.forum || this.isTopic) ? false : true;
    }

    public boolean isMoving() {
        return this.moving;
    }

    public boolean isPointInsideAvatar(float f, float f2) {
        return !LocaleController.isRTL ? f >= 0.0f && f < ((float) AndroidUtilities.dp(60.0f)) : f >= ((float) (getMeasuredWidth() - AndroidUtilities.dp(60.0f))) && f < ((float) getMeasuredWidth());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
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

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
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

    /* JADX WARN: Code restructure failed: missing block: B:208:0x0bb5, code lost:
    
        if (r3.lastKnownTypingType >= 0) goto L390;
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x17e8, code lost:
    
        if (r57.avatarDrawable.getAvatarType() == 2) goto L952;
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x1804, code lost:
    
        r57.avatarDrawable.setArchivedAvatarHiddenProgress(org.telegram.ui.Components.CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(r57.archiveBackgroundProgress));
     */
    /* JADX WARN: Code restructure failed: missing block: B:599:0x1802, code lost:
    
        if (r57.avatarDrawable.getAvatarType() == 2) goto L952;
     */
    /* JADX WARN: Code restructure failed: missing block: B:653:0x1345, code lost:
    
        if (r57.reactionsMentionsChangeProgress != r14) goto L755;
     */
    /* JADX WARN: Code restructure failed: missing block: B:838:0x082e, code lost:
    
        if (r1.type != 2) goto L266;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:156:0x09e7  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x0b2d  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x0b5c  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x0b5f  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x0b71  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x0ba7  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x0c59  */
    /* JADX WARN: Removed duplicated region for block: B:288:0x0e6a  */
    /* JADX WARN: Removed duplicated region for block: B:331:0x0f16  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x0f1e  */
    /* JADX WARN: Removed duplicated region for block: B:338:0x0f2e  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x0f3a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:360:0x0f7d  */
    /* JADX WARN: Removed duplicated region for block: B:365:0x0f91  */
    /* JADX WARN: Removed duplicated region for block: B:368:0x0f9c  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x0fb3  */
    /* JADX WARN: Removed duplicated region for block: B:378:0x0fcb  */
    /* JADX WARN: Removed duplicated region for block: B:385:0x1153  */
    /* JADX WARN: Removed duplicated region for block: B:389:0x1179  */
    /* JADX WARN: Removed duplicated region for block: B:394:0x13d1  */
    /* JADX WARN: Removed duplicated region for block: B:435:0x1561  */
    /* JADX WARN: Removed duplicated region for block: B:440:0x1591  */
    /* JADX WARN: Removed duplicated region for block: B:444:0x15a2  */
    /* JADX WARN: Removed duplicated region for block: B:447:0x15c7  */
    /* JADX WARN: Removed duplicated region for block: B:459:0x15e3  */
    /* JADX WARN: Removed duplicated region for block: B:461:0x15e5  */
    /* JADX WARN: Removed duplicated region for block: B:464:0x15f3  */
    /* JADX WARN: Removed duplicated region for block: B:467:0x15fe  */
    /* JADX WARN: Removed duplicated region for block: B:472:0x160d  */
    /* JADX WARN: Removed duplicated region for block: B:476:0x1615  */
    /* JADX WARN: Removed duplicated region for block: B:479:0x1619  */
    /* JADX WARN: Removed duplicated region for block: B:492:0x1682  */
    /* JADX WARN: Removed duplicated region for block: B:495:0x168b  */
    /* JADX WARN: Removed duplicated region for block: B:498:0x1692  */
    /* JADX WARN: Removed duplicated region for block: B:513:0x16d4  */
    /* JADX WARN: Removed duplicated region for block: B:542:0x1751  */
    /* JADX WARN: Removed duplicated region for block: B:548:0x17a1  */
    /* JADX WARN: Removed duplicated region for block: B:553:0x17d1  */
    /* JADX WARN: Removed duplicated region for block: B:564:0x1816  */
    /* JADX WARN: Removed duplicated region for block: B:570:0x1829  */
    /* JADX WARN: Removed duplicated region for block: B:582:0x186b  */
    /* JADX WARN: Removed duplicated region for block: B:584:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:585:0x184d  */
    /* JADX WARN: Removed duplicated region for block: B:593:0x17eb  */
    /* JADX WARN: Removed duplicated region for block: B:601:0x17a9  */
    /* JADX WARN: Removed duplicated region for block: B:607:0x17ba  */
    /* JADX WARN: Removed duplicated region for block: B:613:0x15c1  */
    /* JADX WARN: Removed duplicated region for block: B:615:0x11c4  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0586  */
    /* JADX WARN: Removed duplicated region for block: B:640:0x12a3  */
    /* JADX WARN: Removed duplicated region for block: B:652:0x1341  */
    /* JADX WARN: Removed duplicated region for block: B:656:0x137c  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0598  */
    /* JADX WARN: Removed duplicated region for block: B:676:0x101d  */
    /* JADX WARN: Removed duplicated region for block: B:693:0x102f  */
    /* JADX WARN: Removed duplicated region for block: B:708:0x107b  */
    /* JADX WARN: Removed duplicated region for block: B:760:0x0f20  */
    /* JADX WARN: Removed duplicated region for block: B:761:0x0f18  */
    /* JADX WARN: Removed duplicated region for block: B:773:0x0f25  */
    /* JADX WARN: Removed duplicated region for block: B:793:0x0e64  */
    /* JADX WARN: Removed duplicated region for block: B:796:0x0b38  */
    /* JADX WARN: Removed duplicated region for block: B:819:0x0c53  */
    /* JADX WARN: Removed duplicated region for block: B:874:0x1597  */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v2, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r10v58 */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
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
        TLRPC.TL_forumTopic tL_forumTopic;
        boolean z6;
        boolean z7;
        TLRPC.TL_forumTopic tL_forumTopic2;
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
        float f14;
        Drawable drawable2;
        Canvas canvas3;
        int i15;
        boolean z11;
        DialogCellTags dialogCellTags;
        int i16;
        char c;
        int i17;
        Drawable drawable3;
        boolean z12;
        float f15;
        DialogCell dialogCell;
        Canvas canvas4;
        int i18;
        TextPaint textPaint;
        int i19;
        float f16;
        float f17;
        StaticLayout staticLayout;
        StaticLayout staticLayout2;
        TextPaint textPaint2;
        int i20;
        StaticLayout staticLayout3;
        TextPaint textPaint3;
        int i21;
        float dp;
        float measuredHeight;
        Paint paint4;
        float f18;
        float f19;
        Canvas canvas5;
        Paint paint5;
        PorterDuffXfermode porterDuffXfermode;
        int i22;
        PullForegroundDrawable pullForegroundDrawable2;
        TLRPC.TL_forumTopic tL_forumTopic3;
        float f20 = 12.5f;
        float f21 = 12.0f;
        if (this.currentDialogId == 0 && this.customDialog == null) {
            return;
        }
        if (!this.visibleOnScreen && !this.drawingForBlur) {
            return;
        }
        if (this.drawArchive && ((this.currentDialogFolderId != 0 || (this.isTopic && (tL_forumTopic3 = this.forumTopic) != null && tL_forumTopic3.id == 1)) && (pullForegroundDrawable2 = this.archivedChatsDrawable) != null && pullForegroundDrawable2.outProgress == 0.0f && this.translationX == 0.0f)) {
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
                    string = LocaleController.getString(i);
                    rLottieDrawable = Theme.dialogs_unpinArchiveDrawable;
                } else {
                    color = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                    color2 = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                    i = R.string.HideOnTop;
                    string = LocaleController.getString(i);
                    rLottieDrawable = Theme.dialogs_pinArchiveDrawable;
                }
            } else if (this.promoDialog) {
                color = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                color2 = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                i = R.string.PsaHide;
                string = LocaleController.getString(i);
                rLottieDrawable = Theme.dialogs_hidePsaDrawable;
            } else if (this.folderId == 0) {
                color = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                color2 = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                if (SharedConfig.getChatSwipeAction(this.currentAccount) == 3) {
                    if (this.dialogMuted) {
                        i = R.string.SwipeUnmute;
                        string = LocaleController.getString(i);
                        rLottieDrawable = Theme.dialogs_swipeUnmuteDrawable;
                    } else {
                        i = R.string.SwipeMute;
                        string = LocaleController.getString(i);
                        rLottieDrawable = Theme.dialogs_swipeMuteDrawable;
                    }
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 4) {
                    i = R.string.SwipeDeleteChat;
                    string = LocaleController.getString(i);
                    color = Theme.getColor(Theme.key_dialogSwipeRemove, this.resourcesProvider);
                    rLottieDrawable = Theme.dialogs_swipeDeleteDrawable;
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 1) {
                    if (this.unreadCount > 0 || this.markUnread) {
                        i = R.string.SwipeMarkAsRead;
                        string = LocaleController.getString(i);
                        rLottieDrawable = Theme.dialogs_swipeReadDrawable;
                    } else {
                        i = R.string.SwipeMarkAsUnread;
                        string = LocaleController.getString(i);
                        rLottieDrawable = Theme.dialogs_swipeUnreadDrawable;
                    }
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) != 0) {
                    i = R.string.Archive;
                    string = LocaleController.getString(i);
                    rLottieDrawable = Theme.dialogs_archiveDrawable;
                } else if (getIsPinned()) {
                    i = R.string.SwipeUnpin;
                    string = LocaleController.getString(i);
                    rLottieDrawable = Theme.dialogs_swipeUnpinDrawable;
                } else {
                    i = R.string.SwipePin;
                    string = LocaleController.getString(i);
                    rLottieDrawable = Theme.dialogs_swipePinDrawable;
                }
            } else {
                color = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                color2 = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                i = R.string.Unarchive;
                string = LocaleController.getString(i);
                rLottieDrawable = Theme.dialogs_unarchiveDrawable;
            }
            RLottieDrawable rLottieDrawable4 = rLottieDrawable;
            int i23 = color2;
            int i24 = i;
            String str3 = string;
            int i25 = i24;
            this.translationDrawable = rLottieDrawable4;
            if (!this.swipeCanceled || (rLottieDrawable2 = this.lastDrawTranslationDrawable) == null) {
                this.lastDrawTranslationDrawable = rLottieDrawable4;
                this.lastDrawSwipeMessageStringId = i25;
            } else {
                this.translationDrawable = rLottieDrawable2;
                i25 = this.lastDrawSwipeMessageStringId;
            }
            int i26 = i25;
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
                        int i27 = Theme.key_chats_archiveBackground;
                        rLottieDrawable5.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(i27));
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(i27));
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(i27));
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
                i2 = i26;
                str = str3;
                canvas.clipRect(f3 - AndroidUtilities.dp(8.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                Theme.dialogs_pinnedPaint.setColor(i23);
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
                    int i28 = Theme.key_chats_archivePinBackground;
                    rLottieDrawable6.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(i28));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(i28));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(i28));
                    Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                    Theme.dialogs_hidePsaDrawableRecolored = z;
                    i3 = z;
                }
            } else {
                i2 = i26;
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
            int i29 = i2;
            if (this.swipeMessageTextId != i29 || this.swipeMessageWidth != getMeasuredWidth()) {
                this.swipeMessageTextId = i29;
                this.swipeMessageWidth = getMeasuredWidth();
                TextPaint textPaint4 = Theme.dialogs_archiveTextPaint;
                int min = Math.min(AndroidUtilities.dp(80.0f), ceil);
                Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
                StaticLayout staticLayout4 = new StaticLayout(str4, textPaint4, min, alignment, 1.0f, 0.0f, false);
                this.swipeMessageTextLayout = staticLayout4;
                if (staticLayout4.getLineCount() > i3) {
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
        float dp2 = AndroidUtilities.dp(8.0f) * this.cornerProgress;
        if (this.isSelected) {
            f4 = 0.0f;
            this.rect.set(0.0f, 0.0f, getMeasuredWidth(), AndroidUtilities.lerp(getMeasuredHeight(), getCollapsedHeight(), this.rightFragmentOpenedProgress));
            this.rect.offset(0.0f, (-this.translateY) + this.collapseOffset);
            canvas.drawRoundRect(this.rect, dp2, dp2, Theme.dialogs_tabletSeletedPaint);
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
                        i22 = canvas.saveLayerAlpha(AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize() + i4) - (AndroidUtilities.dp(8.0f) * (1.0f - clamp)), 0.0f, getMeasuredWidth(), getMeasuredHeight(), (int) ((1.0f - this.rightFragmentOpenedProgress) * 255.0f), 31);
                    } else {
                        int save = canvas.save();
                        canvas.clipRect(AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize() + i4) - (AndroidUtilities.dp(8.0f) * (1.0f - clamp)), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                        i22 = save;
                    }
                    f10 = 0.0f;
                    canvas.translate((-(getMeasuredWidth() - AndroidUtilities.dp(74.0f))) * 0.7f * this.rightFragmentOpenedProgress, 0.0f);
                    i7 = i22;
                } else {
                    f10 = 0.0f;
                    i7 = -1;
                }
                if (this.translationX != f10 || this.cornerProgress != f10) {
                    canvas.save();
                    Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                    this.rect.set(getMeasuredWidth() - AndroidUtilities.dp(64.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                    this.rect.offset(0.0f, -this.translateY);
                    canvas.drawRoundRect(this.rect, dp2, dp2, Theme.dialogs_pinnedPaint);
                    if (this.isSelected) {
                        canvas.drawRoundRect(this.rect, dp2, dp2, Theme.dialogs_tabletSeletedPaint);
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
                    canvas.drawRoundRect(this.rect, dp2, dp2, Theme.dialogs_pinnedPaint);
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
                int dp3 = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 10.0f : 13.0f);
                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                    dp3 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
                }
                if (this.nameLayout != null) {
                    if (this.nameLayoutEllipsizeByGradient && !this.nameLayoutFits) {
                        if (this.nameLayoutEllipsizeLeft && this.fadePaint == null) {
                            Paint paint6 = new Paint();
                            this.fadePaint = paint6;
                            paint6.setShader(new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(24.0f), 0.0f, new int[]{-1, i5}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                            paint5 = this.fadePaint;
                            porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
                        } else {
                            if (this.fadePaintBack == null) {
                                Paint paint7 = new Paint();
                                this.fadePaintBack = paint7;
                                paint7.setShader(new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(24.0f), 0.0f, new int[]{i5, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                                paint5 = this.fadePaintBack;
                                porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
                            }
                            canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), NotificationCenter.newLocationAvailable, 31);
                            int i30 = this.nameLeft;
                            canvas.clipRect(i30, i5, this.nameWidth + i30, getMeasuredHeight());
                        }
                        paint5.setXfermode(porterDuffXfermode);
                        canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), NotificationCenter.newLocationAvailable, 31);
                        int i302 = this.nameLeft;
                        canvas.clipRect(i302, i5, this.nameWidth + i302, getMeasuredHeight());
                    }
                    if (this.currentDialogFolderId != 0) {
                        TextPaint textPaint5 = Theme.dialogs_namePaint[this.paintIndex];
                        int color4 = Theme.getColor(Theme.key_chats_nameArchived, this.resourcesProvider);
                        textPaint5.linkColor = color4;
                        textPaint5.setColor(color4);
                    } else {
                        if (this.encryptedChat == null) {
                            CustomDialog customDialog = this.customDialog;
                            if (customDialog != null) {
                            }
                            textPaint3 = Theme.dialogs_namePaint[this.paintIndex];
                            i21 = Theme.key_chats_name;
                            int color5 = Theme.getColor(i21, this.resourcesProvider);
                            textPaint3.linkColor = color5;
                            textPaint3.setColor(color5);
                        }
                        textPaint3 = Theme.dialogs_namePaint[this.paintIndex];
                        i21 = Theme.key_chats_secretName;
                        int color52 = Theme.getColor(i21, this.resourcesProvider);
                        textPaint3.linkColor = color52;
                        textPaint3.setColor(color52);
                    }
                    canvas.save();
                    canvas.translate(this.nameLeft + this.nameLayoutTranslateX, dp3);
                    SpoilerEffect.layoutDrawMaybe(this.nameLayout, canvas);
                    StaticLayout staticLayout5 = this.nameLayout;
                    i8 = i7;
                    i11 = -1;
                    i9 = 1;
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout5, this.animatedEmojiStackName, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i5, staticLayout5.getPaint().getColor()));
                    canvas.restore();
                    if (!this.nameLayoutEllipsizeByGradient || this.nameLayoutFits) {
                        i10 = 2;
                    } else {
                        canvas.save();
                        if (this.nameLayoutEllipsizeLeft) {
                            canvas.translate(this.nameLeft, 0.0f);
                            dp = AndroidUtilities.dp(24.0f);
                            measuredHeight = getMeasuredHeight();
                            canvas5 = canvas;
                            i10 = 2;
                            f18 = 0.0f;
                            f19 = 0.0f;
                            paint4 = this.fadePaint;
                        } else {
                            i10 = 2;
                            canvas.translate((this.nameLeft + this.nameWidth) - AndroidUtilities.dp(24.0f), 0.0f);
                            dp = AndroidUtilities.dp(24.0f);
                            measuredHeight = getMeasuredHeight();
                            paint4 = this.fadePaintBack;
                            f18 = 0.0f;
                            f19 = 0.0f;
                            canvas5 = canvas;
                        }
                        canvas5.drawRect(f18, f19, dp, measuredHeight, paint4);
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
                        i20 = Theme.key_chats_nameMessageArchived_threeLines;
                    } else if (this.draftMessage != null) {
                        textPaint2 = Theme.dialogs_messageNamePaint;
                        i20 = Theme.key_chats_draft;
                    } else {
                        textPaint2 = Theme.dialogs_messageNamePaint;
                        i20 = Theme.key_chats_nameMessage_threeLines;
                    }
                    int color6 = Theme.getColor(i20, this.resourcesProvider);
                    textPaint2.linkColor = color6;
                    textPaint2.setColor(color6);
                    canvas.save();
                    canvas.translate(this.messageNameLeft, this.messageNameTop);
                    try {
                        SpoilerEffect.layoutDrawMaybe(this.messageNameLayout, canvas);
                        staticLayout3 = this.messageNameLayout;
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
                        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout3, this.animatedEmojiStack2, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i9, staticLayout3.getPaint().getColor()));
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
                        canvas3 = canvas;
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
                        canvas2 = canvas3;
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
                        i19 = Theme.key_chats_message;
                    } else if (this.chat != null) {
                        textPaint = Theme.dialogs_messagePaint[this.paintIndex];
                        i19 = Theme.key_chats_nameMessageArchived;
                    } else {
                        textPaint = Theme.dialogs_messagePaint[this.paintIndex];
                        i19 = Theme.key_chats_messageArchived;
                    }
                    int color7 = Theme.getColor(i19, this.resourcesProvider);
                    textPaint.linkColor = color7;
                    textPaint.setColor(color7);
                    float dp4 = AndroidUtilities.dp(14.0f);
                    DialogUpdateHelper dialogUpdateHelper = this.updateHelper;
                    float f27 = dialogUpdateHelper.typingOutToTop ? this.messageTop - (dialogUpdateHelper.typingProgres * dp4) : this.messageTop + (dialogUpdateHelper.typingProgres * dp4);
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
                            StaticLayout staticLayout6 = this.messageLayout;
                            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout6, this.animatedEmojiStack, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i13, staticLayout6.getPaint().getColor()));
                        } else {
                            try {
                                canvas.save();
                                SpoilerEffect.clipOutCanvas(canvas, this.spoilers);
                                SpoilerEffect.layoutDrawMaybe(this.messageLayout, canvas);
                                staticLayout2 = this.messageLayout;
                                f16 = 1.0f;
                            } catch (Exception e3) {
                                e = e3;
                                f16 = 1.0f;
                            }
                            try {
                                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout2, this.animatedEmojiStack, -0.075f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i13, staticLayout2.getPaint().getColor()));
                                canvas.restore();
                                for (int i31 = 0; i31 < this.spoilers.size(); i31 += i9) {
                                    SpoilerEffect spoilerEffect = (SpoilerEffect) this.spoilers.get(i31);
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
                                    staticLayout = this.typingLayout;
                                    if (staticLayout != null) {
                                        int alpha3 = staticLayout.getPaint().getAlpha();
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
                                    canvas3 = canvas;
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
                                    canvas2 = canvas3;
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
                                staticLayout = this.typingLayout;
                                if (staticLayout != null) {
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
                                canvas3 = canvas;
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
                                canvas2 = canvas3;
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
                    f17 = !dialogUpdateHelper22.typingOutToTop ? this.messageTop + ((f16 - dialogUpdateHelper22.typingProgres) * dp4) : this.messageTop - ((f16 - dialogUpdateHelper22.typingProgres) * dp4);
                    if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                        f17 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                    }
                    canvas.translate(this.typingLeft, f17);
                    staticLayout = this.typingLayout;
                    if (staticLayout != null && this.updateHelper.typingProgres > f13) {
                        int alpha32 = staticLayout.getPaint().getAlpha();
                        this.typingLayout.getPaint().setAlpha((int) (alpha32 * this.updateHelper.typingProgres));
                        this.typingLayout.draw(canvas);
                        this.typingLayout.getPaint().setAlpha(alpha32);
                    }
                    canvas.restore();
                    if (this.typingLayout != null) {
                        int i32 = this.printingStringType;
                        if (i32 < 0) {
                            DialogUpdateHelper dialogUpdateHelper3 = this.updateHelper;
                            if (dialogUpdateHelper3.typingProgres > f13) {
                            }
                        }
                        if (i32 < 0) {
                            i32 = this.updateHelper.lastKnownTypingType;
                        }
                        StatusDrawable chatStatusDrawable = Theme.getChatStatusDrawable(i32);
                        if (chatStatusDrawable != null) {
                            canvas.save();
                            chatStatusDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_chats_actionMessage), (int) (Color.alpha(r10) * this.updateHelper.typingProgres)));
                            DialogUpdateHelper dialogUpdateHelper4 = this.updateHelper;
                            float f28 = dialogUpdateHelper4.typingOutToTop ? this.messageTop + (dp4 * (f16 - dialogUpdateHelper4.typingProgres)) : this.messageTop - (dp4 * (f16 - dialogUpdateHelper4.typingProgres));
                            if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                                f28 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                            }
                            if (i32 == i9 || i32 == i12) {
                                canvas.translate(this.statusDrawableLeft, f28 + (i32 == i9 ? AndroidUtilities.dp(f16) : 0));
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
                    if (this.lastTopicMessageUnread && this.topMessageTopicEndIndex != this.topMessageTopicStartIndex && ((i18 = this.dialogsType) == 0 || i18 == 7 || i18 == 8)) {
                        this.canvasButton.setColor(ColorUtils.setAlphaComponent(this.currentMessagePaint.getColor(), Theme.isCurrentThemeDark() ? 36 : 26));
                        if (!this.buttonCreated) {
                            this.canvasButton.rewind();
                            int i33 = this.topMessageTopicEndIndex;
                            if (i33 != this.topMessageTopicStartIndex && i33 > 0) {
                                float f29 = this.messageTop;
                                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                                    f29 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                                }
                                RectF rectF = AndroidUtilities.rectTmp;
                                float dp5 = this.messageLeft + AndroidUtilities.dp(2.0f) + this.messageLayout.getPrimaryHorizontal(0);
                                float f30 = this.messageLeft;
                                StaticLayout staticLayout7 = this.messageLayout;
                                rectF.set(dp5, f29, (f30 + staticLayout7.getPrimaryHorizontal(Math.min(staticLayout7.getText().length(), this.topMessageTopicEndIndex))) - AndroidUtilities.dp(3.0f), this.buttonTop - AndroidUtilities.dp(4.0f));
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
                        StaticLayout staticLayout8 = this.buttonLayout;
                        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout8, this.animatedEmojiStack3, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(3, staticLayout8.getPaint().getColor()));
                    } else {
                        try {
                            canvas.save();
                            SpoilerEffect.clipOutCanvas(canvas, this.spoilers2);
                            SpoilerEffect.layoutDrawMaybe(this.buttonLayout, canvas);
                            StaticLayout staticLayout9 = this.buttonLayout;
                            try {
                                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout9, this.animatedEmojiStack3, -0.075f, this.spoilers2, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(3, staticLayout9.getPaint().getColor()));
                                canvas.restore();
                                for (int i34 = 0; i34 < this.spoilers2.size(); i34 += i9) {
                                    SpoilerEffect spoilerEffect2 = (SpoilerEffect) this.spoilers2.get(i34);
                                    spoilerEffect2.setColor(this.buttonLayout.getPaint().getColor());
                                    spoilerEffect2.draw(canvas);
                                }
                            } catch (Exception e5) {
                                e = e5;
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
                                canvas3 = canvas;
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
                                canvas2 = canvas3;
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
                        } catch (Exception e6) {
                            e = e6;
                        }
                    }
                    canvas.restore();
                }
                if (this.currentDialogFolderId == 0) {
                    int i35 = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
                    int i36 = this.lastStatusDrawableParams;
                    if (i36 >= 0 && i36 != i35 && !this.statusDrawableAnimationInProgress) {
                        createStatusDrawableAnimator(i36, i35);
                    }
                    boolean z13 = this.statusDrawableAnimationInProgress;
                    if (z13) {
                        i35 = this.animateToStatusDrawableParams;
                    }
                    boolean z14 = (i35 & 1) != 0;
                    boolean z15 = (i35 & 2) != 0;
                    boolean z16 = (i35 & i12) != 0;
                    if (z13) {
                        int i37 = this.animateFromStatusDrawableParams;
                        boolean z17 = (i37 & 1) != 0;
                        boolean z18 = (i37 & 2) != 0;
                        boolean z19 = (i37 & i12) != 0;
                        if (z14 || z17 || !z19 || z18 || !z15 || !z16) {
                            f6 = 1.0f;
                            f7 = 0.0f;
                            dialogCell = this;
                            canvas4 = canvas;
                            boolean z20 = z18;
                            i14 = 2;
                            boolean z21 = z19;
                            i6 = 1;
                            dialogCell.drawCheckStatus(canvas4, z17, z20, z21, false, 1.0f - this.statusDrawableProgress);
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
                        canvas4 = canvas;
                    }
                    dialogCell.drawCheckStatus(canvas4, z14, z15, z16, z12, f15);
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
                            r24 = 16.5f;
                        }
                        float dp6 = AndroidUtilities.dp(r24);
                        if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                            dp6 -= AndroidUtilities.dp(9.0f);
                        }
                        BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft - AndroidUtilities.dp(f6), dp6);
                        BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft - AndroidUtilities.dp(f6), dp6);
                        Theme.dialogs_verifiedDrawable.draw(canvas);
                        drawable = Theme.dialogs_verifiedCheckDrawable;
                    } else {
                        if (this.drawPremium) {
                            int dp7 = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f);
                            if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                                dp7 -= AndroidUtilities.dp(9.0f);
                            }
                            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatus;
                            if (swapAnimatedEmojiDrawable != null) {
                                swapAnimatedEmojiDrawable.setBounds(this.nameMuteLeft - AndroidUtilities.dp(2.0f), dp7 - AndroidUtilities.dp(4.0f), this.nameMuteLeft + AndroidUtilities.dp(20.0f), (dp7 - AndroidUtilities.dp(4.0f)) + AndroidUtilities.dp(22.0f));
                                this.emojiStatus.setColor(Integer.valueOf(Theme.getColor(Theme.key_chats_verifiedBackground, this.resourcesProvider)));
                                this.emojiStatus.draw(canvas);
                            } else {
                                drawable = PremiumGradient.getInstance().premiumStarDrawableMini;
                                int dp8 = this.nameMuteLeft - AndroidUtilities.dp(f6);
                                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                    f20 = 15.5f;
                                }
                                BaseCell.setDrawableBounds(drawable, dp8, AndroidUtilities.dp(f20));
                            }
                        } else if (this.drawScam != 0) {
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                f21 = 15.0f;
                            }
                            int dp9 = AndroidUtilities.dp(f21);
                            if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                                dp9 -= AndroidUtilities.dp(9.0f);
                            }
                            BaseCell.setDrawableBounds((Drawable) (this.drawScam == i6 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable), this.nameMuteLeft, dp9);
                            (this.drawScam == i6 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).draw(canvas);
                        }
                        if (!this.drawReorder || this.reorderIconProgress != f7) {
                            Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                            BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                            Theme.dialogs_reorderDrawable.draw(canvas);
                        }
                        if (!this.drawError) {
                            Theme.dialogs_errorDrawable.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                            this.rect.set(this.errorLeft, this.errorTop, r1 + AndroidUtilities.dp(23.0f), this.errorTop + AndroidUtilities.dp(23.0f));
                            RectF rectF4 = this.rect;
                            float f31 = AndroidUtilities.density * 11.5f;
                            canvas.drawRoundRect(rectF4, f31, f31, Theme.dialogs_errorPaint);
                            BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
                            drawable2 = Theme.dialogs_errorDrawable;
                        } else if (((this.drawCount || this.drawMention) && this.drawCount2) || this.countChangeProgress != f6 || this.drawReactionMention || this.reactionsMentionsChangeProgress != f6) {
                            if (this.isTopic) {
                                z9 = this.topicMuted;
                            } else {
                                TLRPC.Chat chat = this.chat;
                                if (chat != null && chat.forum && this.forumTopic == null) {
                                    z10 = !this.hasUnmutedTopics;
                                    Canvas canvas6 = canvas;
                                    drawCounter(canvas, z10, this.countTop, this.countLeft, this.countLeftOld, 1.0f, false);
                                    if (this.drawMention) {
                                        Theme.dialogs_countPaint.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                                        this.rect.set(this.mentionLeft - AndroidUtilities.dp(5.5f), this.countTop, r1 + this.mentionWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                        Paint paint8 = (!z10 || this.folderId == 0) ? Theme.dialogs_countPaint : Theme.dialogs_countGrayPaint;
                                        RectF rectF5 = this.rect;
                                        float f32 = AndroidUtilities.density * 11.5f;
                                        canvas6.drawRoundRect(rectF5, f32, f32, paint8);
                                        if (this.mentionLayout != null) {
                                            Theme.dialogs_countTextPaint.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                                            canvas.save();
                                            canvas6.translate(this.mentionLeft, this.countTop + AndroidUtilities.dp(4.0f));
                                            this.mentionLayout.draw(canvas6);
                                            canvas.restore();
                                        } else {
                                            Theme.dialogs_mentionDrawable.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                                            BaseCell.setDrawableBounds(Theme.dialogs_mentionDrawable, this.mentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.2f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                            Theme.dialogs_mentionDrawable.draw(canvas6);
                                        }
                                    }
                                    if (!this.drawReactionMention) {
                                        canvas3 = canvas6;
                                    }
                                    Theme.dialogs_reactionsCountPaint.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                                    this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, r0 + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                    Paint paint9 = Theme.dialogs_reactionsCountPaint;
                                    canvas.save();
                                    f14 = this.reactionsMentionsChangeProgress;
                                    if (f14 != f6) {
                                        if (!this.drawReactionMention) {
                                            f14 = f6 - f14;
                                        }
                                        canvas6.scale(f14, f14, this.rect.centerX(), this.rect.centerY());
                                    }
                                    RectF rectF6 = this.rect;
                                    float f33 = AndroidUtilities.density * 11.5f;
                                    canvas6.drawRoundRect(rectF6, f33, f33, paint9);
                                    Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                                    BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                    Theme.dialogs_reactionsMentionDrawable.draw(canvas6);
                                    canvas.restore();
                                    canvas3 = canvas6;
                                    if (this.thumbsCount > 0) {
                                        float f34 = this.updateHelper.typingProgres;
                                        if (f34 != f6) {
                                            if (f34 > f7) {
                                                canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) ((f6 - f34) * 255.0f), 31);
                                                canvas3.translate(f7, (this.updateHelper.typingOutToTop ? -AndroidUtilities.dp(14.0f) : AndroidUtilities.dp(14.0f)) * this.updateHelper.typingProgres);
                                            }
                                            int i38 = 0;
                                            while (i38 < this.thumbsCount) {
                                                if (this.thumbImageSeen[i38]) {
                                                    if (this.thumbBackgroundPaint == null) {
                                                        Paint paint10 = new Paint(i6);
                                                        this.thumbBackgroundPaint = paint10;
                                                        paint10.setShadowLayer(AndroidUtilities.dp(1.34f), f7, AndroidUtilities.dp(0.34f), 402653184);
                                                        c = 0;
                                                        this.thumbBackgroundPaint.setColor(0);
                                                    } else {
                                                        c = 0;
                                                    }
                                                    RectF rectF7 = AndroidUtilities.rectTmp;
                                                    rectF7.set(this.thumbImage[i38].getImageX(), this.thumbImage[i38].getImageY(), this.thumbImage[i38].getImageX2(), this.thumbImage[i38].getImageY2());
                                                    canvas3.drawRoundRect(rectF7, this.thumbImage[i38].getRoundRadius()[c], this.thumbImage[i38].getRoundRadius()[i6], this.thumbBackgroundPaint);
                                                    this.thumbImage[i38].draw(canvas3);
                                                    if (this.drawSpoiler[i38]) {
                                                        Path path = this.thumbPath;
                                                        if (path == null) {
                                                            this.thumbPath = new Path();
                                                        } else {
                                                            path.rewind();
                                                        }
                                                        this.thumbPath.addRoundRect(rectF7, this.thumbImage[i38].getRoundRadius()[c], this.thumbImage[i38].getRoundRadius()[i6], Path.Direction.CW);
                                                        canvas.save();
                                                        canvas3.clipPath(this.thumbPath);
                                                        this.thumbSpoiler.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(i11) * 0.325f)));
                                                        this.thumbSpoiler.setBounds((int) this.thumbImage[i38].getImageX(), (int) this.thumbImage[i38].getImageY(), (int) this.thumbImage[i38].getImageX2(), (int) this.thumbImage[i38].getImageY2());
                                                        this.thumbSpoiler.draw(canvas3);
                                                        invalidate();
                                                        canvas.restore();
                                                    }
                                                    if (this.drawPlay[i38]) {
                                                        BaseCell.setDrawableBounds(Theme.dialogs_playDrawable, (int) (this.thumbImage[i38].getCenterX() - (Theme.dialogs_playDrawable.getIntrinsicWidth() / 2)), (int) (this.thumbImage[i38].getCenterY() - (Theme.dialogs_playDrawable.getIntrinsicHeight() / 2)));
                                                        Theme.dialogs_playDrawable.draw(canvas3);
                                                    }
                                                }
                                                i38 += i6;
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
                                                canvas3.translate(this.tagsLeft, (getMeasuredHeight() - AndroidUtilities.dp(21.66f)) - (this.useSeparator ? 1 : 0));
                                                this.tags.draw(canvas3, this.tagsRight - this.tagsLeft);
                                                canvas.restore();
                                            }
                                            i16 = i8;
                                            if (i16 != i15) {
                                                canvas3.restoreToCount(i16);
                                            }
                                            z3 = z8;
                                            r10 = z11;
                                            canvas2 = canvas3;
                                        }
                                    }
                                    i15 = -1;
                                    z11 = false;
                                    dialogCellTags = this.tags;
                                    if (dialogCellTags != null) {
                                        canvas.save();
                                        canvas3.translate(this.tagsLeft, (getMeasuredHeight() - AndroidUtilities.dp(21.66f)) - (this.useSeparator ? 1 : 0));
                                        this.tags.draw(canvas3, this.tagsRight - this.tagsLeft);
                                        canvas.restore();
                                    }
                                    i16 = i8;
                                    if (i16 != i15) {
                                    }
                                    z3 = z8;
                                    r10 = z11;
                                    canvas2 = canvas3;
                                } else {
                                    z9 = this.dialogMuted;
                                }
                            }
                            z10 = z9;
                            Canvas canvas62 = canvas;
                            drawCounter(canvas, z10, this.countTop, this.countLeft, this.countLeftOld, 1.0f, false);
                            if (this.drawMention) {
                            }
                            if (!this.drawReactionMention) {
                            }
                            Theme.dialogs_reactionsCountPaint.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                            this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, r0 + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                            Paint paint92 = Theme.dialogs_reactionsCountPaint;
                            canvas.save();
                            f14 = this.reactionsMentionsChangeProgress;
                            if (f14 != f6) {
                            }
                            RectF rectF62 = this.rect;
                            float f332 = AndroidUtilities.density * 11.5f;
                            canvas62.drawRoundRect(rectF62, f332, f332, paint92);
                            Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                            BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                            Theme.dialogs_reactionsMentionDrawable.draw(canvas62);
                            canvas.restore();
                            canvas3 = canvas62;
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
                            canvas2 = canvas3;
                        } else {
                            if (this.openBot) {
                                canvas.save();
                                float scale = this.openButtonBounce.getScale(0.05f);
                                canvas.scale(scale, scale, this.openButtonRect.centerX(), this.openButtonRect.centerY());
                                this.openButtonBackgroundPaint.setColor(Theme.getColor(Theme.key_featuredStickers_addButton, this.resourcesProvider));
                                RectF rectF8 = this.openButtonRect;
                                canvas.drawRoundRect(rectF8, rectF8.height() / 2.0f, this.openButtonRect.height() / 2.0f, this.openButtonBackgroundPaint);
                                Text text = this.openButtonText;
                                if (text != null) {
                                    text.draw(canvas, this.openButtonRect.left + AndroidUtilities.dp(13.0f), this.openButtonRect.centerY(), Theme.getColor(Theme.key_featuredStickers_buttonText, this.resourcesProvider), 1.0f);
                                }
                                canvas.restore();
                            } else if (getIsPinned()) {
                                Theme.dialogs_pinnedDrawable.setAlpha((int) ((f6 - this.reorderIconProgress) * 255.0f));
                                BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                                drawable2 = Theme.dialogs_pinnedDrawable;
                            }
                            canvas3 = canvas;
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
                            canvas2 = canvas3;
                        }
                        drawable2.draw(canvas);
                        canvas3 = canvas;
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
                        canvas2 = canvas3;
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
                    canvas3 = canvas;
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
                    canvas2 = canvas3;
                } else {
                    if (z22) {
                        float f35 = this.dialogMutedProgress;
                        if (f35 != f6) {
                            float f36 = f35 + 0.10666667f;
                            this.dialogMutedProgress = f36;
                            if (f36 > f6) {
                                this.dialogMutedProgress = f6;
                                float dp10 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                                float dp11 = AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
                                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                                    dp11 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
                                }
                                BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp10, dp11);
                                BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp10, dp11);
                                if (this.dialogMutedProgress == f6) {
                                    canvas.save();
                                    float f37 = this.dialogMutedProgress;
                                    canvas.scale(f37, f37, Theme.dialogs_muteDrawable.getBounds().centerX(), Theme.dialogs_muteDrawable.getBounds().centerY());
                                    if (this.drawUnmute) {
                                        Theme.dialogs_unmuteDrawable.setAlpha((int) (this.dialogMutedProgress * 255.0f));
                                        Theme.dialogs_unmuteDrawable.draw(canvas);
                                        drawable3 = Theme.dialogs_unmuteDrawable;
                                        i17 = NotificationCenter.newLocationAvailable;
                                    } else {
                                        i17 = NotificationCenter.newLocationAvailable;
                                        Theme.dialogs_muteDrawable.setAlpha((int) (this.dialogMutedProgress * 255.0f));
                                        Theme.dialogs_muteDrawable.draw(canvas);
                                        drawable3 = Theme.dialogs_muteDrawable;
                                    }
                                    drawable3.setAlpha(i17);
                                    canvas.restore();
                                    if (!this.drawReorder) {
                                    }
                                    Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                                    BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                                    Theme.dialogs_reorderDrawable.draw(canvas);
                                    if (!this.drawError) {
                                    }
                                    drawable2.draw(canvas);
                                    canvas3 = canvas;
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
                                    canvas2 = canvas3;
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
                                    canvas3 = canvas;
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
                                    canvas2 = canvas3;
                                }
                            }
                            invalidate();
                            float dp102 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                            float dp112 = AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
                            if (!this.useForceThreeLines) {
                                dp112 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
                                BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp102, dp112);
                                BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp102, dp112);
                                if (this.dialogMutedProgress == f6) {
                                }
                            }
                            dp112 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
                            BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp102, dp112);
                            BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp102, dp112);
                            if (this.dialogMutedProgress == f6) {
                            }
                        }
                    }
                    if (!z22) {
                        float f38 = this.dialogMutedProgress;
                        if (f38 != f7) {
                            float f39 = f38 - 0.10666667f;
                            this.dialogMutedProgress = f39;
                            if (f39 < f7) {
                                this.dialogMutedProgress = f7;
                            }
                            invalidate();
                        }
                    }
                    float dp1022 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                    float dp1122 = AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
                    if (!this.useForceThreeLines) {
                    }
                    dp1122 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
                    BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp1022, dp1122);
                    BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp1022, dp1122);
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
            if (this.drawAvatar && (!this.isTopic || (tL_forumTopic2 = this.forumTopic) == null || tL_forumTopic2.id != i6 || (pullForegroundDrawable = this.archivedChatsDrawable) == null || !pullForegroundDrawable.isDraw())) {
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
                    TLRPC.Chat chat2 = this.chat;
                    if (chat2 != null && chat2.forum && this.forumTopic == null) {
                        z7 = !this.hasUnmutedTopics;
                        RectF rectF9 = this.storyParams.originalAvatarRect;
                        int width = (int) (((rectF9.left + rectF9.width()) - this.countWidth) - AndroidUtilities.dp(5.0f));
                        RectF rectF10 = this.storyParams.originalAvatarRect;
                        drawCounter(canvas, z7, (int) ((this.avatarImage.getImageY() + this.storyParams.originalAvatarRect.height()) - AndroidUtilities.dp(22.0f)), width, (int) (((rectF10.left + rectF10.width()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
                    } else {
                        z6 = this.dialogMuted;
                    }
                } else {
                    z6 = this.topicMuted;
                }
                z7 = z6;
                RectF rectF92 = this.storyParams.originalAvatarRect;
                int width2 = (int) (((rectF92.left + rectF92.width()) - this.countWidth) - AndroidUtilities.dp(5.0f));
                RectF rectF102 = this.storyParams.originalAvatarRect;
                drawCounter(canvas, z7, (int) ((this.avatarImage.getImageY() + this.storyParams.originalAvatarRect.height()) - AndroidUtilities.dp(22.0f)), width2, (int) (((rectF102.left + rectF102.width()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
            }
            if (this.collapseOffset != f7) {
                canvas.restore();
            }
            if (this.translationX != f7) {
                canvas.restore();
            }
            if (this.drawArchive && ((this.currentDialogFolderId != 0 || (this.isTopic && (tL_forumTopic = this.forumTopic) != null && tL_forumTopic.id == i6)) && this.translationX == f7 && this.archivedChatsDrawable != null)) {
                canvas.save();
                canvas2.translate(f7, (-this.translateY) - (this.rightFragmentOffset * this.rightFragmentOpenedProgress));
                canvas2.clipRect((int) r10, (int) r10, getMeasuredWidth(), getMeasuredHeight());
                this.archivedChatsDrawable.draw(canvas2);
                canvas.restore();
            }
            if (this.useSeparator) {
                int dp12 = (this.fullSeparator || !(this.currentDialogFolderId == 0 || !this.archiveHidden || this.fullSeparator2) || (this.fullSeparator2 && !this.archiveHidden)) ? 0 : AndroidUtilities.dp(this.messagePaddingStart);
                if (this.rightFragmentOpenedProgress != f6) {
                    int alpha4 = Theme.dividerPaint.getAlpha();
                    float f40 = this.rightFragmentOpenedProgress;
                    if (f40 != f7) {
                        Theme.dividerPaint.setAlpha((int) (alpha4 * (f6 - f40)));
                    }
                    float measuredHeight3 = (getMeasuredHeight() - i6) - (this.rightFragmentOffset * this.rightFragmentOpenedProgress);
                    if (LocaleController.isRTL) {
                        measuredWidth = getMeasuredWidth() - dp12;
                        paint2 = Theme.dividerPaint;
                        f9 = 0.0f;
                    } else {
                        f9 = dp12;
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
                    float f41 = this.reorderIconProgress;
                    if (f41 > f7) {
                        float f42 = f41 - 0.09411765f;
                        this.reorderIconProgress = f42;
                        if (f42 < f7) {
                            this.reorderIconProgress = f7;
                        }
                        z5 = true;
                    }
                } else {
                    float f43 = this.reorderIconProgress;
                    if (f43 < f6) {
                        float f44 = f43 + 0.09411765f;
                        this.reorderIconProgress = f44;
                        if (f44 > f6) {
                            this.reorderIconProgress = f6;
                        }
                        z5 = true;
                    }
                }
                if (this.archiveHidden) {
                    float f45 = this.archiveBackgroundProgress;
                    if (f45 > f7) {
                        float f46 = f45 - 0.069565214f;
                        this.archiveBackgroundProgress = f46;
                        if (f46 < f7) {
                            this.archiveBackgroundProgress = f7;
                        }
                    }
                    if (this.animatingArchiveAvatar) {
                        float f47 = this.animatingArchiveAvatarProgress + 16.0f;
                        this.animatingArchiveAvatarProgress = f47;
                        if (f47 >= f8) {
                            this.animatingArchiveAvatarProgress = f8;
                            this.animatingArchiveAvatar = r10;
                        }
                        z5 = true;
                    }
                    if (this.drawRevealBackground) {
                        float f48 = this.currentRevealBounceProgress;
                        if (f48 < f6) {
                            float f49 = f48 + 0.09411765f;
                            this.currentRevealBounceProgress = f49;
                            if (f49 > f6) {
                                this.currentRevealBounceProgress = f6;
                                z5 = true;
                            }
                        }
                        float f50 = this.currentRevealProgress;
                        if (f50 < f6) {
                            float f51 = f50 + 0.053333335f;
                            this.currentRevealProgress = f51;
                            if (f51 > f6) {
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
                    float f52 = this.currentRevealProgress;
                    if (f52 > f7) {
                        float f53 = f52 - 0.053333335f;
                        this.currentRevealProgress = f53;
                        if (f53 < f7) {
                            this.currentRevealProgress = f7;
                        }
                        z5 = true;
                    }
                    if (z5) {
                    }
                } else {
                    float f54 = this.archiveBackgroundProgress;
                    if (f54 < f6) {
                        float f55 = f54 + 0.069565214f;
                        this.archiveBackgroundProgress = f55;
                        if (f55 > f6) {
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
        Theme.dialogs_pinnedPaint.setAlpha((int) (r1.getAlpha() * (1.0f - this.rightFragmentOpenedProgress)));
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
            RectF rectF922 = this.storyParams.originalAvatarRect;
            int width22 = (int) (((rectF922.left + rectF922.width()) - this.countWidth) - AndroidUtilities.dp(5.0f));
            RectF rectF1022 = this.storyParams.originalAvatarRect;
            drawCounter(canvas, z7, (int) ((this.avatarImage.getImageY() + this.storyParams.originalAvatarRect.height()) - AndroidUtilities.dp(22.0f)), width22, (int) (((rectF1022.left + rectF1022.width()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
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

    /* JADX WARN: Removed duplicated region for block: B:10:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00ff  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0119  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0190  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x013a  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x00a9  */
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
        TLRPC.User user;
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
                TLRPC.User user2 = this.user;
                if (user2 == null) {
                    TLRPC.Chat chat = this.chat;
                    if (chat != null) {
                        sb.append(LocaleController.getString(chat.broadcast ? R.string.AccDescrChannel : R.string.AccDescrGroup));
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
                if (UserObject.isReplyUser(user2)) {
                    i = R.string.RepliesTitle;
                } else if (UserObject.isAnonymous(this.user)) {
                    i = R.string.AnonymousForward;
                } else {
                    if (this.user.bot) {
                        sb.append(LocaleController.getString(R.string.Bot));
                        sb.append(". ");
                    }
                    TLRPC.User user3 = this.user;
                    if (user3.self) {
                        i = R.string.SavedMessages;
                    } else {
                        str = ContactsController.formatName(user3.first_name, user3.last_name);
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
        } else {
            if (getIsPinned()) {
                return;
            }
            this.drawReorder = false;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x006a, code lost:
    
        if (r6.getAction() == 3) goto L27;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        CanvasButton canvasButton;
        int i;
        if (this.rightFragmentOpenedProgress == 0.0f && !this.isTopic && this.storyParams.checkOnTouchEvent(motionEvent, this)) {
            return true;
        }
        DialogCellDelegate dialogCellDelegate = this.delegate;
        if (dialogCellDelegate == null || dialogCellDelegate.canClickButtonInside()) {
            if (this.openBot) {
                boolean contains = this.openButtonRect.contains(motionEvent.getX(), motionEvent.getY());
                if (motionEvent.getAction() != 0 && motionEvent.getAction() != 2) {
                    if (this.openButtonBounce.isPressed() && motionEvent.getAction() == 1) {
                        Utilities.Callback callback = this.onOpenButtonClick;
                        if (callback != null) {
                            callback.run(this.user);
                        }
                    } else if (this.openButtonBounce.isPressed()) {
                    }
                    this.openButtonBounce.setPressed(false);
                    return true;
                }
                this.openButtonBounce.setPressed(contains);
                if (contains) {
                    return true;
                }
            }
            if (this.lastTopicMessageUnread && (canvasButton = this.canvasButton) != null && this.buttonLayout != null && (((i = this.dialogsType) == 0 || i == 7 || i == 8) && canvasButton.checkTouchEvent(motionEvent))) {
                return true;
            }
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
        float f = z ? 0.0f : 1.0f;
        this.archiveBackgroundProgress = f;
        this.avatarDrawable.setArchivedAvatarHiddenProgress(f);
        this.clipProgress = 0.0f;
        this.isSliding = false;
        this.reorderIconProgress = (getIsPinned() && this.drawReorder) ? 1.0f : 0.0f;
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

    public void setDialog(TLRPC.Dialog dialog, int i, int i2) {
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
            this.currentDialogFolderId = ((TLRPC.TL_dialogFolder) dialog).folder.id;
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

    public void setForumTopic(TLRPC.TL_forumTopic tL_forumTopic, long j, MessageObject messageObject, boolean z, boolean z2) {
        PullForegroundDrawable pullForegroundDrawable;
        this.forumTopic = tL_forumTopic;
        this.isTopic = tL_forumTopic != null;
        if (this.currentDialogId != j) {
            this.lastStatusDrawableParams = -1;
        }
        Drawable drawable = messageObject.topicIconDrawable[0];
        if (drawable instanceof ForumBubbleDrawable) {
            ((ForumBubbleDrawable) drawable).setColor(tL_forumTopic.icon_color);
        }
        this.currentDialogId = j;
        this.lastDialogChangedTime = System.currentTimeMillis();
        this.message = messageObject;
        this.isDialogCell = false;
        this.showTopicIconInName = z;
        TLRPC.Message message = messageObject.messageOwner;
        this.lastMessageDate = message.date;
        this.currentEditDate = message.edit_date;
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
        if (tL_forumTopic != null) {
            this.groupMessages = tL_forumTopic.groupedMessages;
        }
        TLRPC.TL_forumTopic tL_forumTopic2 = this.forumTopic;
        if (tL_forumTopic2 != null && tL_forumTopic2.id == 1 && (pullForegroundDrawable = this.archivedChatsDrawable) != null) {
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

    public void setOpenBotButton(boolean z) {
        if (this.openBot == z) {
            return;
        }
        if (this.openButtonText == null) {
            this.openButtonText = new Text(LocaleController.getString(R.string.BotOpen), 14.0f, AndroidUtilities.bold());
        }
        this.openBot = z;
        this.openButtonBounce.setPressed(false);
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

    /* JADX WARN: Code restructure failed: missing block: B:79:0x015d, code lost:
    
        if (r8.pinned != false) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x017d, code lost:
    
        r6 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x017b, code lost:
    
        if (r6.pinnedDialogs.indexOfKey(r8.id) >= 0) goto L69;
     */
    /* JADX WARN: Removed duplicated region for block: B:122:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x032e  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0365  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0412  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x0424  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x042b  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x043d  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x04ae  */
    /* JADX WARN: Removed duplicated region for block: B:262:0x0523  */
    /* JADX WARN: Removed duplicated region for block: B:280:0x054b  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x0605  */
    /* JADX WARN: Removed duplicated region for block: B:330:0x077e  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x0783 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:343:0x07d8  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x0780  */
    /* JADX WARN: Removed duplicated region for block: B:373:0x0554  */
    /* JADX WARN: Removed duplicated region for block: B:402:0x0454  */
    /* JADX WARN: Removed duplicated region for block: B:438:0x0431  */
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
        TLRPC.User user;
        ValueAnimator valueAnimator;
        TimeInterpolator timeInterpolator;
        ValueAnimator valueAnimator2;
        TimeInterpolator overshootInterpolator;
        boolean z9;
        boolean z10;
        TLRPC.Chat chat;
        MessagesController messagesController;
        boolean z11;
        boolean z12;
        MessageObject messageObject2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        TLRPC.Chat chat2;
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
                TLRPC.Dialog dialog = (TLRPC.Dialog) MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                if (dialog != null) {
                    this.readOutboxMaxId = dialog.read_outbox_max_id;
                    this.ttlPeriod = dialog.ttl_period;
                    if (i == 0) {
                        this.clearingDialog = MessagesController.getInstance(this.currentAccount).isClearingDialog(dialog.id);
                        ArrayList arrayList = (ArrayList) MessagesController.getInstance(this.currentAccount).dialogMessage.get(dialog.id);
                        this.groupMessages = arrayList;
                        MessageObject messageObject4 = (arrayList == null || arrayList.size() <= 0) ? null : (MessageObject) this.groupMessages.get(0);
                        this.message = messageObject4;
                        this.lastUnreadState = messageObject4 != null && messageObject4.isUnread();
                        TLRPC.Chat chat3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-dialog.id));
                        if (chat3 != null && chat3.forum) {
                            int[] forumUnreadCount = MessagesController.getInstance(this.currentAccount).getTopicsController().getForumUnreadCount(chat3.id);
                            this.unreadCount = forumUnreadCount[0];
                            this.mentionCount = forumUnreadCount[1];
                            this.reactionMentionCount = forumUnreadCount[2];
                            this.hasUnmutedTopics = forumUnreadCount[3] != 0;
                        } else if (dialog instanceof TLRPC.TL_dialogFolder) {
                            this.unreadCount = MessagesStorage.getInstance(this.currentAccount).getArchiveUnreadCount();
                            this.mentionCount = 0;
                            this.reactionMentionCount = 0;
                        } else {
                            this.unreadCount = dialog.unread_count;
                            this.mentionCount = dialog.unread_mentions_count;
                            this.reactionMentionCount = dialog.unread_reactions_count;
                        }
                        this.markUnread = dialog.unread_mark;
                        MessageObject messageObject5 = this.message;
                        this.currentEditDate = messageObject5 != null ? messageObject5.messageOwner.edit_date : 0;
                        this.lastMessageDate = dialog.last_message_date;
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
            TLRPC.TL_forumTopic tL_forumTopic = this.forumTopic;
            if (tL_forumTopic != null) {
                this.unreadCount = tL_forumTopic.unread_count;
                this.mentionCount = tL_forumTopic.unread_mentions_count;
                this.reactionMentionCount = tL_forumTopic.unread_reactions_count;
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
                        TLRPC.User user2 = this.user;
                        if (user2 != null && !MessagesController.isSupportUser(user2) && !this.user.bot && (i & MessagesController.UPDATE_MASK_STATUS) != 0) {
                            this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id));
                            if (this.wasDrawnOnline != isOnline()) {
                                z5 = true;
                            }
                        }
                        if ((i & MessagesController.UPDATE_MASK_EMOJI_STATUS) != 0) {
                            if (this.user != null) {
                                TLRPC.User user3 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id));
                                this.user = user3;
                                if (user3 == null || DialogObject.getEmojiStatusDocumentId(user3.emoji_status) == 0) {
                                    this.nameLayoutEllipsizeByGradient = true;
                                    this.emojiStatus.set(PremiumGradient.getInstance().premiumStarDrawableMini, z);
                                } else {
                                    this.nameLayoutEllipsizeByGradient = true;
                                    this.emojiStatus.set(DialogObject.getEmojiStatusDocumentId(this.user.emoji_status), z);
                                }
                                z5 = true;
                            }
                            if (this.chat != null) {
                                TLRPC.Chat chat4 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.id));
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
                                        TLRPC.Dialog dialog2 = (TLRPC.Dialog) MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                                        TLRPC.Chat chat5 = dialog2 == null ? null : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-dialog2.id));
                                        if (chat5 == null || !chat5.forum) {
                                            z6 = z2;
                                            if (dialog2 instanceof TLRPC.TL_dialogFolder) {
                                                i6 = MessagesStorage.getInstance(this.currentAccount).getArchiveUnreadCount();
                                                i5 = 0;
                                            } else if (dialog2 != null) {
                                                i6 = dialog2.unread_count;
                                                i7 = dialog2.unread_mentions_count;
                                                i5 = dialog2.unread_reactions_count;
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
                                        if (dialog2 != null && (this.unreadCount != i6 || this.markUnread != dialog2.unread_mark || this.mentionCount != i7 || this.reactionMentionCount != i5)) {
                                            this.unreadCount = i6;
                                            this.mentionCount = i7;
                                            this.markUnread = dialog2.unread_mark;
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
                            TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j)));
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
                            TLRPC.Chat chat6 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
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
                            TLRPC.User user4 = this.user;
                            if (user4 != null) {
                                this.avatarDrawable.setInfo(this.currentAccount, user4);
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
                                TLRPC.Chat chat7 = this.chat;
                                if (chat7 != null) {
                                    this.avatarDrawable.setInfo(this.currentAccount, chat7);
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
                            TLRPC.Chat chat8 = this.chat;
                            imageReceiver2.setRoundRadius(AndroidUtilities.dp((!(chat8 == null && chat8.forum && this.currentDialogFolderId == 0 && !this.useFromUserAsAvatar) && (this.isSavedDialog || (user = this.user) == null || !user.self || !MessagesController.getInstance(this.currentAccount).savedViewAsChats)) ? 28.0f : 16.0f));
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
                        TLRPC.Chat chat82 = this.chat;
                        imageReceiver22.setRoundRadius(AndroidUtilities.dp((!(chat82 == null && chat82.forum && this.currentDialogFolderId == 0 && !this.useFromUserAsAvatar) && (this.isSavedDialog || (user = this.user) == null || !user.self || !MessagesController.getInstance(this.currentAccount).savedViewAsChats)) ? 28.0f : 16.0f));
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
                    TLRPC.Chat chat822 = this.chat;
                    imageReceiver222.setRoundRadius(AndroidUtilities.dp((!(chat822 == null && chat822.forum && this.currentDialogFolderId == 0 && !this.useFromUserAsAvatar) && (this.isSavedDialog || (user = this.user) == null || !user.self || !MessagesController.getInstance(this.currentAccount).savedViewAsChats)) ? 28.0f : 16.0f));
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
            TLRPC.Chat chat8222 = this.chat;
            imageReceiver2222.setRoundRadius(AndroidUtilities.dp((!(chat8222 == null && chat8222.forum && this.currentDialogFolderId == 0 && !this.useFromUserAsAvatar) && (this.isSavedDialog || (user = this.user) == null || !user.self || !MessagesController.getInstance(this.currentAccount).savedViewAsChats)) ? 28.0f : 16.0f));
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
        TLRPC.Message message;
        int i;
        MessageObject messageObject = this.message;
        if (messageObject == null) {
            return;
        }
        String restrictionReason = MessagesController.getInstance(messageObject.currentAccount).getRestrictionReason(this.message.messageOwner.restriction_reason);
        MessageObject messageObject2 = this.message;
        int i2 = 0;
        if (messageObject2 != null && (message = messageObject2.messageOwner) != null) {
            TLRPC.MessageMedia messageMedia = message.media;
            if (messageMedia instanceof TLRPC.TL_messageMediaPaidMedia) {
                this.thumbsCount = 0;
                this.hasVideoThumb = false;
                TLRPC.TL_messageMediaPaidMedia tL_messageMediaPaidMedia = (TLRPC.TL_messageMediaPaidMedia) messageMedia;
                int i3 = 0;
                while (i2 < tL_messageMediaPaidMedia.extended_media.size() && this.thumbsCount < 3) {
                    TLRPC.MessageExtendedMedia messageExtendedMedia = tL_messageMediaPaidMedia.extended_media.get(i2);
                    if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMediaPreview) {
                        i = i3 + 1;
                        setThumb(i3, ((TLRPC.TL_messageExtendedMediaPreview) messageExtendedMedia).thumb);
                    } else if (messageExtendedMedia instanceof TLRPC.TL_messageExtendedMedia) {
                        i = i3 + 1;
                        setThumb(i3, ((TLRPC.TL_messageExtendedMedia) messageExtendedMedia).media);
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
