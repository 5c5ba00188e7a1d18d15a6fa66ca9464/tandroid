package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatThemeController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stars;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.AvatarSpan;
import org.telegram.ui.ChannelAdminLogActivity;
import org.telegram.ui.ChatBackgroundDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.LoadingDrawable;
import org.telegram.ui.Components.Premium.StarParticlesView;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.Gifts.GiftSheet;
import org.telegram.ui.GradientClip;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stars.StarGiftSheet;
import org.telegram.ui.Stars.StarGiftUniqueActionLayout;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.StoriesUtilities;
import org.telegram.ui.Stories.UploadingDotsSpannable;
import org.telegram.ui.Stories.recorder.HintView2;
import org.telegram.ui.Stories.recorder.PreviewView;

/* loaded from: classes4.dex */
public class ChatActionCell extends BaseCell implements DownloadController.FileDownloadProgressListener, NotificationCenter.NotificationCenterDelegate {
    private static Map monthsToEmoticon;
    private int TAG;
    private SpannableStringBuilder accessibilityText;
    private int adaptiveEmojiColor;
    private ColorFilter adaptiveEmojiColorFilter;
    private AnimatedEmojiSpan.EmojiGroupedSpans animatedEmojiStack;
    private AvatarDrawable avatarDrawable;
    StoriesUtilities.AvatarStoryParams avatarStoryParams;
    private int backgroundButtonTop;
    private int backgroundHeight;
    private int backgroundLeft;
    private Path backgroundPath;
    private RectF backgroundRect;
    private int backgroundRectHeight;
    private int backgroundRight;
    private ButtonBounce bounce;
    private boolean buttonClickableAsImage;
    private boolean canDrawInParent;
    private Path clipPath;
    private int currentAccount;
    private MessageObject currentMessageObject;
    private ImageLocation currentVideoLocation;
    private int customDate;
    private CharSequence customText;
    private ChatActionCellDelegate delegate;
    private float dimAmount;
    private final Paint dimPaint;
    private boolean forceWasUnread;
    private boolean giftButtonPressed;
    private RectF giftButtonRect;
    private TLRPC.VideoSize giftEffectAnimation;
    private int giftPremiumAdditionalHeight;
    private StaticLayout giftPremiumButtonLayout;
    private float giftPremiumButtonWidth;
    private StaticLayout giftPremiumSubtitleLayout;
    private TextLayout giftPremiumText;
    private GradientClip giftPremiumTextClip;
    private boolean giftPremiumTextCollapsed;
    private int giftPremiumTextCollapsedHeight;
    private AnimatedFloat giftPremiumTextExpandedAnimated;
    private Text giftPremiumTextMore;
    private int giftPremiumTextMoreH;
    private int giftPremiumTextMoreX;
    private int giftPremiumTextMoreY;
    private boolean giftPremiumTextUncollapsed;
    private StaticLayout giftPremiumTitleLayout;
    private int giftRectSize;
    private CornerPathEffect giftRibbonPaintEffect;
    private ColorMatrixColorFilter giftRibbonPaintFilter;
    private boolean giftRibbonPaintFilterDark;
    private Path giftRibbonPath;
    private Text giftRibbonText;
    private TLRPC.Document giftSticker;
    private ImageReceiver.ImageReceiverDelegate giftStickerDelegate;
    private TextPaint giftSubtitlePaint;
    private TextPaint giftTextPaint;
    private TextPaint giftTitlePaint;
    private boolean hasReplyMessage;
    private boolean imagePressed;
    private ImageReceiver imageReceiver;
    private boolean invalidateColors;
    private boolean invalidatePath;
    private View invalidateWithParent;
    private boolean invalidatesParent;
    private boolean isSpoilerRevealing;
    private float lastTouchX;
    private float lastTouchY;
    private ArrayList lineHeights;
    private ArrayList lineWidths;
    private LoadingDrawable loadingDrawable;
    private int overriddenMaxWidth;
    private int overrideBackground;
    private Paint overrideBackgroundPaint;
    private int overrideText;
    private TextPaint overrideTextPaint;
    private URLSpan pressedLink;
    private int previousWidth;
    float progressToProgress;
    RadialProgressView progressView;
    private RadialProgress2 radialProgress;
    public final ReactionsLayoutInBubble reactionsLayoutInBubble;
    private RectF rect;
    private View rippleView;
    private StaticLayout settingWallpaperLayout;
    TextPaint settingWallpaperPaint;
    private float settingWallpaperProgress;
    private StaticLayout settingWallpaperProgressTextLayout;
    private SpoilerEffect spoilerPressed;
    public List spoilers;
    private Stack spoilersPool;
    public final StarGiftUniqueActionLayout starGiftLayout;
    private float starGiftLayoutX;
    private float starGiftLayoutY;
    private StarParticlesView.Drawable starParticlesDrawable;
    private Path starsPath;
    private int starsSize;
    private int stickerSize;
    private int textHeight;
    private StaticLayout textLayout;
    TextPaint textPaint;
    private boolean textPressed;
    private int textWidth;
    private int textX;
    private int textXLeft;
    private int textY;
    private Theme.ResourcesProvider themeDelegate;
    public final TransitionParams transitionParams;
    private float viewTop;
    private float viewTranslationX;
    private boolean visiblePartSet;
    private Drawable wallpaperPreviewDrawable;
    private boolean wasLayout;

    public interface ChatActionCellDelegate {

        public abstract /* synthetic */ class -CC {
            public static boolean $default$canDrawOutboundsContent(ChatActionCellDelegate chatActionCellDelegate) {
                return true;
            }

            public static void $default$didClickButton(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell) {
            }

            public static void $default$didClickImage(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell) {
            }

            public static boolean $default$didLongPress(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, float f, float f2) {
                return false;
            }

            public static void $default$didOpenPremiumGift(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, TLRPC.TL_premiumGiftOption tL_premiumGiftOption, String str, boolean z) {
            }

            public static void $default$didOpenPremiumGiftChannel(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, String str, boolean z) {
            }

            public static void $default$didPressReaction(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, TLRPC.ReactionCount reactionCount, boolean z, float f, float f2) {
            }

            public static void $default$didPressReplyMessage(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, int i) {
            }

            public static void $default$forceUpdate(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, boolean z) {
            }

            public static BaseFragment $default$getBaseFragment(ChatActionCellDelegate chatActionCellDelegate) {
                return null;
            }

            public static long $default$getDialogId(ChatActionCellDelegate chatActionCellDelegate) {
                return 0L;
            }

            public static long $default$getTopicId(ChatActionCellDelegate chatActionCellDelegate) {
                return 0L;
            }

            public static void $default$needOpenInviteLink(ChatActionCellDelegate chatActionCellDelegate, TLRPC.TL_chatInviteExported tL_chatInviteExported) {
            }

            public static void $default$needOpenUserProfile(ChatActionCellDelegate chatActionCellDelegate, long j) {
            }

            public static void $default$needShowEffectOverlay(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, TLRPC.Document document, TLRPC.VideoSize videoSize) {
            }
        }

        boolean canDrawOutboundsContent();

        void didClickButton(ChatActionCell chatActionCell);

        void didClickImage(ChatActionCell chatActionCell);

        boolean didLongPress(ChatActionCell chatActionCell, float f, float f2);

        void didOpenPremiumGift(ChatActionCell chatActionCell, TLRPC.TL_premiumGiftOption tL_premiumGiftOption, String str, boolean z);

        void didOpenPremiumGiftChannel(ChatActionCell chatActionCell, String str, boolean z);

        void didPressReaction(ChatActionCell chatActionCell, TLRPC.ReactionCount reactionCount, boolean z, float f, float f2);

        void didPressReplyMessage(ChatActionCell chatActionCell, int i);

        void forceUpdate(ChatActionCell chatActionCell, boolean z);

        BaseFragment getBaseFragment();

        long getDialogId();

        long getTopicId();

        void needOpenInviteLink(TLRPC.TL_chatInviteExported tL_chatInviteExported);

        void needOpenUserProfile(long j);

        void needShowEffectOverlay(ChatActionCell chatActionCell, TLRPC.Document document, TLRPC.VideoSize videoSize);
    }

    class TextLayout {
        public AnimatedEmojiSpan.EmojiGroupedSpans emoji;
        public StaticLayout layout;
        public TextPaint paint;
        public int width;
        public float x;
        public float y;
        public List spoilers = new ArrayList();
        public final AtomicReference patchedLayout = new AtomicReference();

        TextLayout() {
        }

        public void attach() {
            this.emoji = AnimatedEmojiSpan.update(0, (View) ChatActionCell.this, false, this.emoji, this.layout);
        }

        public void detach() {
            AnimatedEmojiSpan.release(ChatActionCell.this, this.emoji);
        }

        public void setText(CharSequence charSequence, TextPaint textPaint, int i) {
            this.paint = textPaint;
            this.width = i;
            this.layout = new StaticLayout(charSequence, textPaint, i, Layout.Alignment.ALIGN_CENTER, 1.1f, 0.0f, false);
            if (ChatActionCell.this.currentMessageObject == null || !ChatActionCell.this.currentMessageObject.isSpoilersRevealed) {
                SpoilerEffect.addSpoilers(ChatActionCell.this, this.layout, -1, i, null, this.spoilers);
            } else {
                List list = this.spoilers;
                if (list != null) {
                    list.clear();
                }
            }
            attach();
        }
    }

    public interface ThemeDelegate extends Theme.ResourcesProvider {

        public abstract /* synthetic */ class -CC {
        }
    }

    public class TransitionParams {
        public boolean animateChange;
        public float animateChangeProgress = 1.0f;
        public boolean wasDraw;

        public TransitionParams() {
        }

        public boolean animateChange() {
            if (this.wasDraw) {
                return ChatActionCell.this.reactionsLayoutInBubble.animateChange();
            }
            return false;
        }

        public void onDetach() {
            this.wasDraw = false;
        }

        public void recordDrawingState() {
            this.wasDraw = true;
            ChatActionCell.this.reactionsLayoutInBubble.recordDrawingState();
        }

        public void resetAnimation() {
            this.animateChange = false;
            this.animateChangeProgress = 1.0f;
        }

        public boolean supportChangeAnimation() {
            return true;
        }
    }

    static {
        HashMap hashMap = new HashMap();
        monthsToEmoticon = hashMap;
        hashMap.put(1, "1⃣");
        monthsToEmoticon.put(3, "2⃣");
        monthsToEmoticon.put(6, "3⃣");
        monthsToEmoticon.put(12, "4⃣");
        monthsToEmoticon.put(24, "5⃣");
    }

    public ChatActionCell(Context context) {
        this(context, false, null);
    }

    public ChatActionCell(Context context, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.bounce = new ButtonBounce(this);
        this.currentAccount = UserConfig.selectedAccount;
        this.avatarStoryParams = new StoriesUtilities.AvatarStoryParams(false);
        this.giftButtonRect = new RectF();
        this.spoilers = new ArrayList();
        this.spoilersPool = new Stack();
        this.reactionsLayoutInBubble = new ReactionsLayoutInBubble(this);
        this.overrideBackground = -1;
        this.overrideText = -1;
        this.lineWidths = new ArrayList();
        this.lineHeights = new ArrayList();
        this.backgroundPath = new Path();
        this.rect = new RectF();
        this.invalidatePath = true;
        this.invalidateColors = false;
        this.giftPremiumTextUncollapsed = false;
        this.giftPremiumTextCollapsed = false;
        this.giftPremiumTextExpandedAnimated = new AnimatedFloat(this, 0L, 320L, CubicBezierInterpolator.EASE_OUT_QUINT);
        this.buttonClickableAsImage = true;
        this.giftTitlePaint = new TextPaint(1);
        this.giftTextPaint = new TextPaint(1);
        this.giftSubtitlePaint = new TextPaint(1);
        this.radialProgress = new RadialProgress2(this);
        this.giftStickerDelegate = new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda2
            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public final void didSetImage(ImageReceiver imageReceiver, boolean z2, boolean z3, boolean z4) {
                ChatActionCell.this.lambda$new$0(imageReceiver, z2, z3, z4);
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public /* synthetic */ void didSetImageBitmap(int i, String str, Drawable drawable) {
                ImageReceiver.ImageReceiverDelegate.-CC.$default$didSetImageBitmap(this, i, str, drawable);
            }

            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
                ImageReceiver.ImageReceiverDelegate.-CC.$default$onAnimationReady(this, imageReceiver);
            }
        };
        this.starsPath = new Path();
        this.dimPaint = new Paint(1);
        this.transitionParams = new TransitionParams();
        this.avatarStoryParams.drawSegments = false;
        this.canDrawInParent = z;
        this.themeDelegate = resourcesProvider;
        ImageReceiver imageReceiver = new ImageReceiver(this);
        this.imageReceiver = imageReceiver;
        imageReceiver.setRoundRadius(AndroidUtilities.roundMessageSize / 2);
        this.avatarDrawable = new AvatarDrawable();
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        this.starGiftLayout = new StarGiftUniqueActionLayout(this.currentAccount, this, resourcesProvider);
        this.giftTitlePaint.setTextSize(TypedValue.applyDimension(1, 16.0f, getResources().getDisplayMetrics()));
        this.giftSubtitlePaint.setTextSize(TypedValue.applyDimension(1, 15.0f, getResources().getDisplayMetrics()));
        this.giftTextPaint.setTextSize(TypedValue.applyDimension(1, 15.0f, getResources().getDisplayMetrics()));
        View view = new View(context);
        this.rippleView = view;
        view.setBackground(Theme.createSelectorDrawable(Theme.multAlpha(-16777216, 0.1f), 7, AndroidUtilities.dp(16.0f)));
        this.rippleView.setVisibility(8);
        addView(this.rippleView);
        StarParticlesView.Drawable drawable = new StarParticlesView.Drawable(10);
        this.starParticlesDrawable = drawable;
        drawable.type = 100;
        drawable.isCircle = false;
        drawable.roundEffect = true;
        drawable.useRotate = false;
        drawable.useBlur = true;
        drawable.checkBounds = true;
        drawable.size1 = 1;
        drawable.k3 = 0.98f;
        drawable.k2 = 0.98f;
        drawable.k1 = 0.98f;
        drawable.paused = false;
        drawable.speedScale = 0.0f;
        drawable.minLifeTime = 750L;
        drawable.randLifeTime = 750;
        drawable.init();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:107:0x03d2  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x03e6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:122:0x02b2  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x023b  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0231  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x02a9  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x03a8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void buildLayout() {
        CharSequence charSequence;
        CharSequence replaceTags;
        int i;
        CharSequence charSequence2;
        boolean z;
        String str;
        CharSequence charSequence3;
        boolean z2;
        int i2;
        String str2;
        ArrayList<TLRPC.VideoSize> arrayList;
        TLRPC.Photo photo;
        ArrayList<TLRPC.VideoSize> arrayList2;
        Spannable spannable;
        CharSequence string;
        String string2;
        int i3;
        String str3;
        boolean z3;
        CharSequence charSequence4;
        boolean z4;
        int i4;
        String str4;
        ChatActionCell chatActionCell;
        String str5;
        String str6;
        CharSequence charSequence5;
        boolean z5;
        String forcedFirstName;
        int i5;
        String str7;
        int i6;
        String formatString;
        int i7;
        CharSequence replaceTags2;
        TL_stars.StarGift starGift;
        String str8;
        String str9;
        char c;
        Object valueOf;
        TLRPC.MessageMedia messageMedia;
        int i8;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            charSequence = messageObject.isExpiredStory() ? messageObject.messageOwner.media.user_id != UserConfig.getInstance(this.currentAccount).getClientUserId() ? StoriesUtilities.createExpiredStoryString(true, R.string.ExpiredStoryMention, new Object[0]) : StoriesUtilities.createExpiredStoryString(true, R.string.ExpiredStoryMentioned, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())).first_name) : (this.delegate.getTopicId() == 0 && MessageObject.isTopicActionMessage(messageObject)) ? ForumUtilities.createActionTextWithTopic(MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-messageObject.getDialogId(), MessageObject.getTopicId(this.currentAccount, messageObject.messageOwner, true)), messageObject) : null;
            if (charSequence == null) {
                TLRPC.Message message = messageObject.messageOwner;
                if (message != null && (messageMedia = message.media) != null && messageMedia.ttl_seconds != 0) {
                    if (messageMedia.photo != null) {
                        i8 = R.string.AttachPhotoExpired;
                    } else {
                        TLRPC.Document document = messageMedia.document;
                        if ((document instanceof TLRPC.TL_documentEmpty) || ((messageMedia instanceof TLRPC.TL_messageMediaDocument) && document == null)) {
                            i8 = messageMedia.voice ? R.string.AttachVoiceExpired : messageMedia.round ? R.string.AttachRoundExpired : R.string.AttachVideoExpired;
                        }
                    }
                    charSequence = LocaleController.getString(i8);
                }
                charSequence = AnimatedEmojiSpan.cloneSpans(messageObject.messageText);
            }
        } else {
            charSequence = this.customText;
        }
        createLayout(charSequence, this.previousWidth);
        if (messageObject != null) {
            int i9 = messageObject.type;
            if (i9 == 11) {
                float dp = this.textHeight + AndroidUtilities.dp(19.0f);
                float f = AndroidUtilities.roundMessageSize;
                this.imageReceiver.setImageCoords((this.previousWidth - AndroidUtilities.roundMessageSize) / 2.0f, dp, f, f);
            } else if (i9 == 25) {
                createGiftPremiumChannelLayouts();
            } else {
                if (i9 == 30) {
                    TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.currentMessageObject.getDialogId()));
                    TLRPC.MessageAction messageAction = messageObject.messageOwner.action;
                    if (messageAction instanceof TLRPC.TL_messageActionGiftStars) {
                        String formatPluralStringComma = LocaleController.formatPluralStringComma("ActionGiftStarsTitle", (int) ((TLRPC.TL_messageActionGiftStars) messageAction).stars);
                        string = AndroidUtilities.replaceTags(this.currentMessageObject.isOutOwner() ? LocaleController.formatString(R.string.ActionGiftStarsSubtitle, UserObject.getForcedFirstName(user)) : LocaleController.getString(R.string.ActionGiftStarsSubtitleYou));
                        string2 = LocaleController.getString(R.string.ActionGiftStarsView);
                        i3 = this.giftRectSize;
                        str3 = null;
                        z3 = true;
                        charSequence4 = null;
                        z4 = false;
                        str6 = formatPluralStringComma;
                        i4 = 11;
                        str5 = str6;
                        chatActionCell = this;
                        str4 = str5;
                    } else if ((messageAction instanceof TLRPC.TL_messageActionStarGiftUnique) && ((TLRPC.TL_messageActionStarGiftUnique) messageAction).refunded) {
                        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                        if (messageObject.isOutOwner() != (!((TLRPC.TL_messageActionStarGiftUnique) messageObject.messageOwner.action).upgrade)) {
                            clientUserId = messageObject.getDialogId();
                        }
                        TLRPC.User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(clientUserId));
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                        spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.Gift2ActionTitle)).append((CharSequence) " ");
                        if (user2 != null && user2.photo != null) {
                            spannableStringBuilder.append((CharSequence) "a ");
                            AvatarSpan avatarSpan = new AvatarSpan(this, this.currentAccount, 18.0f);
                            avatarSpan.setUser(user2);
                            spannableStringBuilder.setSpan(avatarSpan, spannableStringBuilder.length() - 2, spannableStringBuilder.length() - 1, 33);
                        }
                        spannableStringBuilder.append((CharSequence) UserObject.getForcedFirstName(user2));
                        string = LocaleController.getString(R.string.Gift2ActionUpgradeRefundedText);
                        string2 = LocaleController.getString(R.string.ActionGiftStarsView);
                        str3 = LocaleController.getString(R.string.Gift2UniqueRibbon);
                        i3 = this.giftRectSize;
                        i4 = 12;
                        z3 = true;
                        charSequence4 = null;
                        z4 = false;
                        str5 = spannableStringBuilder;
                        chatActionCell = this;
                        str4 = str5;
                    } else if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
                        TLRPC.TL_messageActionStarGift tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction;
                        long j = tL_messageActionStarGift.convert_stars;
                        boolean z6 = messageObject.getDialogId() == UserConfig.getInstance(this.currentAccount).getClientUserId();
                        long fromChatId = messageObject.getFromChatId();
                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
                        TLRPC.User user3 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(fromChatId));
                        if (!tL_messageActionStarGift.can_upgrade || tL_messageActionStarGift.converted) {
                            charSequence5 = "a ";
                        } else {
                            charSequence5 = "a ";
                            if (tL_messageActionStarGift.upgrade_stars > 0 && !tL_messageActionStarGift.upgraded) {
                                z5 = true;
                                if (z6) {
                                    spannableStringBuilder2.append((CharSequence) LocaleController.getString(R.string.Gift2ActionTitle)).append((CharSequence) " ");
                                    if (user3 != null && user3.photo != null) {
                                        spannableStringBuilder2.append(charSequence5);
                                        AvatarSpan avatarSpan2 = new AvatarSpan(this, this.currentAccount, 18.0f);
                                        avatarSpan2.setUser(user3);
                                        spannableStringBuilder2.setSpan(avatarSpan2, spannableStringBuilder2.length() - 2, spannableStringBuilder2.length() - 1, 33);
                                    }
                                    forcedFirstName = UserObject.getForcedFirstName(user3);
                                } else {
                                    forcedFirstName = LocaleController.getString(R.string.Gift2ActionSelfTitle);
                                }
                                spannableStringBuilder2.append((CharSequence) forcedFirstName);
                                boolean z7 = !((messageObject.isOutOwner() || z6) && tL_messageActionStarGift.converted) && tL_messageActionStarGift.convert_stars > 0 && MessagesController.getInstance(this.currentAccount).stargiftsConvertPeriodMax - (ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - messageObject.messageOwner.date) > 0 && !tL_messageActionStarGift.refunded;
                                if (tL_messageActionStarGift.refunded) {
                                    TLRPC.TL_textWithEntities tL_textWithEntities = tL_messageActionStarGift.message;
                                    if (tL_textWithEntities != null && !TextUtils.isEmpty(tL_textWithEntities.text)) {
                                        SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(tL_messageActionStarGift.message.text);
                                        this.giftTextPaint.setTextSize(AndroidUtilities.dp(13.0f));
                                        MessageObject.addEntitiesToText(spannableStringBuilder3, tL_messageActionStarGift.message.entities, false, false, true, true);
                                        replaceTags2 = MessageObject.replaceAnimatedEmoji(Emoji.replaceEmoji((CharSequence) spannableStringBuilder3, this.giftTextPaint.getFontMetricsInt(), false, (int[]) null), tL_messageActionStarGift.message.entities, this.giftTextPaint.getFontMetricsInt());
                                    } else if (z6) {
                                        if (!tL_messageActionStarGift.converted || j <= 0) {
                                            i7 = tL_messageActionStarGift.can_upgrade ? R.string.Gift2ActionSelfInfoUpgrade : R.string.Gift2ActionSelfInfoNoConvert;
                                            formatString = LocaleController.getString(i7);
                                            replaceTags2 = AndroidUtilities.replaceTags(formatString);
                                        }
                                        i5 = (int) j;
                                        str7 = "Gift2ActionConvertedInfo";
                                        replaceTags2 = LocaleController.formatPluralStringComma(str7, i5);
                                    } else {
                                        if (z5) {
                                            if (messageObject.isOutOwner()) {
                                                formatString = LocaleController.formatString(R.string.Gift2ActionUpgradeOut, UserObject.getForcedFirstName(user));
                                            } else {
                                                i7 = R.string.Gift2ActionUpgrade;
                                                formatString = LocaleController.getString(i7);
                                            }
                                        } else if (messageObject.isOutOwner()) {
                                            formatString = (!z7 || j <= 0) ? tL_messageActionStarGift.can_upgrade ? LocaleController.formatString(R.string.Gift2ActionOutInfoUpgrade, UserObject.getForcedFirstName(user)) : LocaleController.formatString(R.string.Gift2ActionOutInfoNoConvert, UserObject.getForcedFirstName(user)) : LocaleController.formatPluralStringComma("Gift2ActionOutInfo", (int) j, UserObject.getForcedFirstName(user));
                                        } else {
                                            if (!tL_messageActionStarGift.converted) {
                                                if (tL_messageActionStarGift.saved) {
                                                    i6 = !z7 ? R.string.Gift2ActionBotSavedInfo : R.string.Gift2ActionSavedInfo;
                                                } else if (z7) {
                                                    i5 = (int) j;
                                                    str7 = "Gift2ActionInfo";
                                                    replaceTags2 = LocaleController.formatPluralStringComma(str7, i5);
                                                } else {
                                                    i6 = R.string.Gift2ActionBotInfo;
                                                }
                                            }
                                            i5 = (int) j;
                                            str7 = "Gift2ActionConvertedInfo";
                                            replaceTags2 = LocaleController.formatPluralStringComma(str7, i5);
                                        }
                                        replaceTags2 = AndroidUtilities.replaceTags(formatString);
                                    }
                                    string = replaceTags2;
                                    starGift = tL_messageActionStarGift.gift;
                                    if (starGift == null && starGift.limited) {
                                        int i10 = R.string.Gift2Limited1OfRibbon;
                                        int i11 = starGift.availability_total;
                                        if (i11 > 1500) {
                                            c = 0;
                                            valueOf = AndroidUtilities.formatWholeNumber(i11, 0);
                                        } else {
                                            c = 0;
                                            valueOf = Integer.valueOf(i11);
                                        }
                                        Object[] objArr = new Object[1];
                                        objArr[c] = valueOf;
                                        str8 = LocaleController.formatString(i10, objArr);
                                    } else {
                                        str8 = null;
                                    }
                                    if (messageObject.isOutOwner() || tL_messageActionStarGift.forceIn || z5) {
                                        if (!messageObject.isOutOwner() && z5) {
                                            SpannableStringBuilder spannableStringBuilder4 = new SpannableStringBuilder();
                                            spannableStringBuilder4.append((CharSequence) "^  ");
                                            ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.gift_unpack);
                                            coloredImageSpan.setScale(0.8f, 0.8f);
                                            spannableStringBuilder4.setSpan(coloredImageSpan, 0, 1, 33);
                                            spannableStringBuilder4.append((CharSequence) LocaleController.getString(R.string.Gift2Unpack));
                                            str9 = spannableStringBuilder4;
                                            string2 = str9;
                                            i4 = 11;
                                            charSequence4 = null;
                                            z4 = false;
                                            chatActionCell = this;
                                            str4 = spannableStringBuilder2;
                                            str3 = str8;
                                            i3 = this.giftRectSize;
                                            z3 = true;
                                        }
                                    } else if (!tL_messageActionStarGift.refunded) {
                                        string2 = null;
                                        i4 = 11;
                                        charSequence4 = null;
                                        z4 = false;
                                        chatActionCell = this;
                                        str4 = spannableStringBuilder2;
                                        str3 = str8;
                                        i3 = this.giftRectSize;
                                        z3 = true;
                                    }
                                    str9 = LocaleController.getString(R.string.ActionGiftStarsView);
                                    string2 = str9;
                                    i4 = 11;
                                    charSequence4 = null;
                                    z4 = false;
                                    chatActionCell = this;
                                    str4 = spannableStringBuilder2;
                                    str3 = str8;
                                    i3 = this.giftRectSize;
                                    z3 = true;
                                } else {
                                    i6 = R.string.Gift2ActionConvertRefundedText;
                                }
                                replaceTags2 = LocaleController.getString(i6);
                                string = replaceTags2;
                                starGift = tL_messageActionStarGift.gift;
                                if (starGift == null) {
                                }
                                str8 = null;
                                if (messageObject.isOutOwner()) {
                                }
                                if (!messageObject.isOutOwner()) {
                                    SpannableStringBuilder spannableStringBuilder42 = new SpannableStringBuilder();
                                    spannableStringBuilder42.append((CharSequence) "^  ");
                                    ColoredImageSpan coloredImageSpan2 = new ColoredImageSpan(R.drawable.gift_unpack);
                                    coloredImageSpan2.setScale(0.8f, 0.8f);
                                    spannableStringBuilder42.setSpan(coloredImageSpan2, 0, 1, 33);
                                    spannableStringBuilder42.append((CharSequence) LocaleController.getString(R.string.Gift2Unpack));
                                    str9 = spannableStringBuilder42;
                                    string2 = str9;
                                    i4 = 11;
                                    charSequence4 = null;
                                    z4 = false;
                                    chatActionCell = this;
                                    str4 = spannableStringBuilder2;
                                    str3 = str8;
                                    i3 = this.giftRectSize;
                                    z3 = true;
                                }
                                str9 = LocaleController.getString(R.string.ActionGiftStarsView);
                                string2 = str9;
                                i4 = 11;
                                charSequence4 = null;
                                z4 = false;
                                chatActionCell = this;
                                str4 = spannableStringBuilder2;
                                str3 = str8;
                                i3 = this.giftRectSize;
                                z3 = true;
                            }
                        }
                        z5 = false;
                        if (z6) {
                        }
                        spannableStringBuilder2.append((CharSequence) forcedFirstName);
                        if (messageObject.isOutOwner()) {
                        }
                        if (tL_messageActionStarGift.refunded) {
                        }
                        replaceTags2 = LocaleController.getString(i6);
                        string = replaceTags2;
                        starGift = tL_messageActionStarGift.gift;
                        if (starGift == null) {
                        }
                        str8 = null;
                        if (messageObject.isOutOwner()) {
                        }
                        if (!messageObject.isOutOwner()) {
                        }
                        str9 = LocaleController.getString(R.string.ActionGiftStarsView);
                        string2 = str9;
                        i4 = 11;
                        charSequence4 = null;
                        z4 = false;
                        chatActionCell = this;
                        str4 = spannableStringBuilder2;
                        str3 = str8;
                        i3 = this.giftRectSize;
                        z3 = true;
                    } else {
                        long j2 = ((TLRPC.TL_messageActionPrizeStars) messageAction).stars;
                        String string3 = LocaleController.getString(R.string.ActionStarGiveawayPrizeTitle);
                        CharSequence charSequence6 = this.currentMessageObject.messageText;
                        str = LocaleController.getString(R.string.ActionGiftStarsView);
                        i2 = this.giftRectSize;
                        z2 = true;
                        charSequence3 = charSequence6;
                        str2 = string3;
                        createGiftPremiumLayouts(str2, null, charSequence3, false, str, 11, null, i2, z2);
                        this.textLayout = null;
                        this.textHeight = 0;
                        this.textY = 0;
                    }
                } else if (i9 == 18) {
                    TLRPC.MessageAction messageAction2 = messageObject.messageOwner.action;
                    TLRPC.TL_textWithEntities tL_textWithEntities2 = messageAction2 instanceof TLRPC.TL_messageActionGiftPremium ? ((TLRPC.TL_messageActionGiftPremium) messageAction2).message : messageAction2 instanceof TLRPC.TL_messageActionGiftCode ? ((TLRPC.TL_messageActionGiftCode) messageAction2).message : null;
                    if (tL_textWithEntities2 == null || TextUtils.isEmpty(tL_textWithEntities2.text)) {
                        spannable = null;
                    } else {
                        SpannableStringBuilder spannableStringBuilder5 = new SpannableStringBuilder(tL_textWithEntities2.text);
                        this.giftTextPaint.setTextSize(AndroidUtilities.dp(13.0f));
                        MessageObject.addEntitiesToText(spannableStringBuilder5, tL_textWithEntities2.entities, false, false, true, true);
                        spannable = MessageObject.replaceAnimatedEmoji(Emoji.replaceEmoji((CharSequence) spannableStringBuilder5, this.giftTextPaint.getFontMetricsInt(), false, (int[]) null), tL_textWithEntities2.entities, this.giftTextPaint.getFontMetricsInt());
                    }
                    string = spannable == null ? LocaleController.getString(R.string.ActionGiftPremiumText) : spannable;
                    string2 = LocaleController.getString((!isGiftCode() || isSelfGiftCode()) ? R.string.ActionGiftPremiumView : R.string.GiftPremiumUseGiftBtn);
                    String formatPluralStringComma2 = LocaleController.formatPluralStringComma("ActionGiftPremiumTitle2", messageObject.messageOwner.action.months);
                    i3 = this.giftRectSize;
                    str3 = null;
                    z3 = false;
                    charSequence4 = null;
                    z4 = true;
                    str6 = formatPluralStringComma2;
                    i4 = 11;
                    str5 = str6;
                    chatActionCell = this;
                    str4 = str5;
                } else {
                    if (i9 == 21) {
                        TLRPC.TL_messageActionSuggestProfilePhoto tL_messageActionSuggestProfilePhoto = (TLRPC.TL_messageActionSuggestProfilePhoto) messageObject.messageOwner.action;
                        TLRPC.User user4 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.isOutOwner() ? 0L : messageObject.getDialogId()));
                        boolean z8 = tL_messageActionSuggestProfilePhoto.video || !((photo = tL_messageActionSuggestProfilePhoto.photo) == null || (arrayList2 = photo.video_sizes) == null || arrayList2.isEmpty());
                        if (user4.id == UserConfig.getInstance(this.currentAccount).clientUserId) {
                            TLRPC.User user5 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId()));
                            replaceTags = z8 ? LocaleController.formatString(R.string.ActionSuggestVideoFromYouDescription, user5.first_name) : LocaleController.formatString(R.string.ActionSuggestPhotoFromYouDescription, user5.first_name);
                        } else {
                            replaceTags = z8 ? LocaleController.formatString(R.string.ActionSuggestVideoToYouDescription, user4.first_name) : LocaleController.formatString(R.string.ActionSuggestPhotoToYouDescription, user4.first_name);
                        }
                        i = (tL_messageActionSuggestProfilePhoto.video || !((arrayList = tL_messageActionSuggestProfilePhoto.photo.video_sizes) == null || arrayList.isEmpty())) ? R.string.ViewVideoAction : R.string.ViewPhotoAction;
                    } else if (i9 == 22) {
                        TLRPC.User user6 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.isOutOwner() ? 0L : messageObject.getDialogId()));
                        if (messageObject.getDialogId() >= 0) {
                            if (!messageObject.isOutOwner() && messageObject.isWallpaperForBoth() && messageObject.isCurrentWallpaper()) {
                                charSequence2 = messageObject.messageText;
                                str = LocaleController.getString(R.string.RemoveWallpaperAction);
                                z = false;
                            } else if (user6 == null || user6.id != UserConfig.getInstance(this.currentAccount).clientUserId) {
                                charSequence2 = messageObject.messageText;
                                str = LocaleController.getString(R.string.ViewWallpaperAction);
                                z = true;
                            }
                            charSequence3 = charSequence2;
                            z2 = z;
                            i2 = this.giftRectSize;
                            str2 = null;
                            createGiftPremiumLayouts(str2, null, charSequence3, false, str, 11, null, i2, z2);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.textY = 0;
                        }
                        charSequence2 = messageObject.messageText;
                        z = true;
                        str = null;
                        charSequence3 = charSequence2;
                        z2 = z;
                        i2 = this.giftRectSize;
                        str2 = null;
                        createGiftPremiumLayouts(str2, null, charSequence3, false, str, 11, null, i2, z2);
                        this.textLayout = null;
                        this.textHeight = 0;
                        this.textY = 0;
                    } else if (messageObject.isStoryMention()) {
                        TLRPC.User user7 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.messageOwner.media.user_id));
                        replaceTags = AndroidUtilities.replaceTags(user7.self ? LocaleController.formatString("StoryYouMentionedTitle", R.string.StoryYouMentionedTitle, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())).first_name) : LocaleController.formatString("StoryMentionedTitle", R.string.StoryMentionedTitle, user7.first_name));
                        i = R.string.StoryMentionedAction;
                    }
                    charSequence3 = replaceTags;
                    str = LocaleController.getString(i);
                    i2 = this.giftRectSize;
                    str2 = null;
                    z2 = true;
                    createGiftPremiumLayouts(str2, null, charSequence3, false, str, 11, null, i2, z2);
                    this.textLayout = null;
                    this.textHeight = 0;
                    this.textY = 0;
                }
                chatActionCell.createGiftPremiumLayouts(str4, charSequence4, string, z4, string2, i4, str3, i3, z3);
            }
        }
        this.reactionsLayoutInBubble.x = AndroidUtilities.dp(12.0f);
        this.reactionsLayoutInBubble.measure(this.previousWidth - AndroidUtilities.dp(24.0f), 1);
    }

    private void checkLeftRightBounds() {
        this.backgroundLeft = (int) Math.min(this.backgroundLeft, this.rect.left);
        this.backgroundRight = (int) Math.max(this.backgroundRight, this.rect.right);
    }

    private void createGiftPremiumChannelLayouts() {
        int i;
        String str;
        SpannableStringBuilder spannableStringBuilder;
        String formatString;
        int dp = this.giftRectSize - AndroidUtilities.dp(16.0f);
        this.giftTitlePaint.setTextSize(AndroidUtilities.dp(14.0f));
        this.giftTextPaint.setTextSize(AndroidUtilities.dp(13.0f));
        TLRPC.TL_messageActionGiftCode tL_messageActionGiftCode = (TLRPC.TL_messageActionGiftCode) this.currentMessageObject.messageOwner.action;
        int i2 = tL_messageActionGiftCode.months;
        TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-DialogObject.getPeerDialogId(tL_messageActionGiftCode.boost_peer)));
        String str2 = chat == null ? null : chat.title;
        boolean z = tL_messageActionGiftCode.via_giveaway;
        if (tL_messageActionGiftCode.unclaimed) {
            i = R.string.BoostingUnclaimedPrize;
            str = "BoostingUnclaimedPrize";
        } else {
            i = R.string.BoostingCongratulations;
            str = "BoostingCongratulations";
        }
        String string = LocaleController.getString(str, i);
        String formatPluralString = i2 == 12 ? LocaleController.formatPluralString("BoldYears", 1, new Object[0]) : LocaleController.formatPluralString("BoldMonths", i2, new Object[0]);
        if (!z) {
            spannableStringBuilder = new SpannableStringBuilder(AndroidUtilities.replaceTags(str2 == null ? LocaleController.getString("BoostingReceivedGiftNoName", R.string.BoostingReceivedGiftNoName) : LocaleController.formatString("BoostingReceivedGiftFrom", R.string.BoostingReceivedGiftFrom, str2)));
            spannableStringBuilder.append((CharSequence) "\n\n");
            formatString = LocaleController.formatString("BoostingReceivedGiftDuration", R.string.BoostingReceivedGiftDuration, formatPluralString);
        } else if (tL_messageActionGiftCode.unclaimed) {
            spannableStringBuilder = new SpannableStringBuilder(AndroidUtilities.replaceTags(LocaleController.formatString("BoostingYouHaveUnclaimedPrize", R.string.BoostingYouHaveUnclaimedPrize, str2)));
            spannableStringBuilder.append((CharSequence) "\n\n");
            formatString = LocaleController.formatString("BoostingUnclaimedPrizeDuration", R.string.BoostingUnclaimedPrizeDuration, formatPluralString);
        } else {
            spannableStringBuilder = new SpannableStringBuilder(AndroidUtilities.replaceTags(LocaleController.formatString("BoostingReceivedPrizeFrom", R.string.BoostingReceivedPrizeFrom, str2)));
            spannableStringBuilder.append((CharSequence) "\n\n");
            formatString = LocaleController.formatString("BoostingReceivedPrizeDuration", R.string.BoostingReceivedPrizeDuration, formatPluralString);
        }
        spannableStringBuilder.append((CharSequence) AndroidUtilities.replaceTags(formatString));
        String string2 = LocaleController.getString("BoostingReceivedGiftOpenBtn", R.string.BoostingReceivedGiftOpenBtn);
        SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(string);
        valueOf.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, valueOf.length(), 33);
        TextPaint textPaint = this.giftTitlePaint;
        Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
        this.giftPremiumTitleLayout = new StaticLayout(valueOf, textPaint, dp, alignment, 1.1f, 0.0f, false);
        this.giftPremiumSubtitleLayout = null;
        TextLayout textLayout = this.giftPremiumText;
        if (textLayout != null) {
            textLayout.detach();
        }
        TextLayout textLayout2 = new TextLayout();
        this.giftPremiumText = textLayout2;
        textLayout2.setText(spannableStringBuilder, this.giftTextPaint, dp);
        SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(string2);
        valueOf2.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, valueOf2.length(), 33);
        this.giftPremiumTextCollapsed = false;
        this.giftPremiumTextCollapsedHeight = 0;
        this.giftPremiumTextMore = null;
        StaticLayout staticLayout = new StaticLayout(valueOf2, (TextPaint) getThemedPaint("paintChatActionText"), dp, alignment, 1.0f, 0.0f, false);
        this.giftPremiumButtonLayout = staticLayout;
        this.buttonClickableAsImage = true;
        this.giftPremiumButtonWidth = measureLayoutWidth(staticLayout);
    }

    private void createGiftPremiumLayouts(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, boolean z, CharSequence charSequence4, int i, CharSequence charSequence5, int i2, boolean z2) {
        int i3;
        float f;
        int cutInFancyHalf;
        int i4;
        CharSequence charSequence6 = charSequence3;
        int dp = i2 - AndroidUtilities.dp(16.0f);
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && messageObject.type == 30) {
            dp -= AndroidUtilities.dp(16.0f);
        }
        if (charSequence != null) {
            MessageObject messageObject2 = this.currentMessageObject;
            if (messageObject2 == null || messageObject2.type != 30) {
                this.giftTitlePaint.setTextSize(AndroidUtilities.dp(16.0f));
            } else {
                this.giftTitlePaint.setTextSize(AndroidUtilities.dp(14.0f));
            }
            SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(charSequence);
            valueOf.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, valueOf.length(), 33);
            this.giftPremiumTitleLayout = new StaticLayout(valueOf, this.giftTitlePaint, dp, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        } else {
            this.giftPremiumTitleLayout = null;
        }
        if (charSequence2 != null) {
            this.giftSubtitlePaint.setTextSize(AndroidUtilities.dp(13.0f));
            this.giftPremiumSubtitleLayout = new StaticLayout(charSequence2, this.giftSubtitlePaint, dp, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        } else {
            this.giftPremiumSubtitleLayout = null;
        }
        if (this.currentMessageObject == null || !(isNewStyleButtonLayout() || (i4 = this.currentMessageObject.type) == 30 || i4 == 18)) {
            this.giftTextPaint.setTextSize(AndroidUtilities.dp(15.0f));
        } else {
            this.giftTextPaint.setTextSize(AndroidUtilities.dp(13.0f));
        }
        int dp2 = dp - AndroidUtilities.dp(12.0f);
        MessageObject messageObject3 = this.currentMessageObject;
        if (messageObject3 != null && messageObject3.type == 22 && messageObject3.getDialogId() >= 0 && (cutInFancyHalf = HintView2.cutInFancyHalf(charSequence6, this.giftTextPaint)) < dp2 && cutInFancyHalf > dp2 / 5.0f) {
            dp2 = cutInFancyHalf;
        }
        if (charSequence6 == null) {
            TextLayout textLayout = this.giftPremiumText;
            if (textLayout != null) {
                textLayout.detach();
                this.giftPremiumText = null;
            }
            this.giftPremiumTextCollapsed = false;
            i3 = 0;
        } else {
            if (this.giftPremiumText == null) {
                this.giftPremiumText = new TextLayout();
            }
            try {
                charSequence6 = Emoji.replaceEmoji(charSequence6, this.giftTextPaint.getFontMetricsInt(), false);
            } catch (Exception unused) {
            }
            this.giftPremiumText.setText(charSequence6, this.giftTextPaint, dp2);
            if (!z || this.giftPremiumText.layout.getLineCount() <= 3) {
                i3 = 0;
                this.giftPremiumTextCollapsed = false;
                this.giftPremiumTextExpandedAnimated.set(true, true);
                this.giftPremiumTextCollapsedHeight = 0;
            } else {
                this.giftPremiumTextCollapsed = !this.giftPremiumTextUncollapsed;
                this.giftPremiumTextCollapsedHeight = this.giftPremiumText.layout.getLineBottom(2);
                this.giftPremiumTextMore = new Text(LocaleController.getString(R.string.Gift2CaptionMore), this.giftTextPaint.getTextSize() / AndroidUtilities.density, AndroidUtilities.bold());
                int lineBottom = this.giftPremiumText.layout.getLineBottom(2);
                this.giftPremiumTextMoreY = lineBottom;
                this.giftPremiumTextMoreH = lineBottom - this.giftPremiumText.layout.getLineTop(2);
                this.giftPremiumTextMoreX = (int) this.giftPremiumText.layout.getLineRight(2);
                i3 = 0;
            }
            if (this.giftPremiumTextCollapsed) {
                this.giftPremiumText.setText(charSequence6.subSequence(i3, this.giftPremiumText.layout.getLineEnd(2) - 1), this.giftTextPaint, dp2);
            }
        }
        if (charSequence4 != null) {
            SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(charSequence4);
            valueOf2.setSpan(new TypefaceSpan(AndroidUtilities.bold()), i3, valueOf2.length(), 33);
            StaticLayout staticLayout = new StaticLayout(valueOf2, (TextPaint) getThemedPaint("paintChatActionText"), dp, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            this.giftPremiumButtonLayout = staticLayout;
            this.buttonClickableAsImage = z2 && !this.giftPremiumTextCollapsed;
            f = measureLayoutWidth(staticLayout);
        } else {
            this.giftPremiumButtonLayout = null;
            this.buttonClickableAsImage = false;
            f = 0.0f;
        }
        this.giftPremiumButtonWidth = f;
        if (charSequence5 == null) {
            this.giftRibbonPath = null;
            this.giftRibbonText = null;
            return;
        }
        if (this.giftRibbonPaintEffect == null) {
            this.giftRibbonPaintEffect = new CornerPathEffect(AndroidUtilities.dp(5.0f));
        }
        if (this.giftRibbonPath == null) {
            Path path = new Path();
            this.giftRibbonPath = path;
            GiftSheet.RibbonDrawable.fillRibbonPath(path, 1.35f);
        }
        Text text = new Text(charSequence5, i, AndroidUtilities.bold());
        this.giftRibbonText = text;
        text.ellipsize(AndroidUtilities.dp(62.0f));
    }

    private void createLayout(CharSequence charSequence, int i) {
        ChatActionCellDelegate chatActionCellDelegate;
        int dp = i - AndroidUtilities.dp(30.0f);
        if (dp < 0) {
            return;
        }
        int i2 = this.overriddenMaxWidth;
        if (i2 > 0) {
            dp = Math.min(i2, dp);
        }
        this.invalidatePath = true;
        MessageObject messageObject = this.currentMessageObject;
        TextPaint textPaint = (TextPaint) getThemedPaint((messageObject == null || !messageObject.drawServiceWithDefaultTypeface) ? "paintChatActionText" : "paintChatActionText2");
        textPaint.linkColor = textPaint.getColor();
        this.textLayout = new StaticLayout(charSequence, textPaint, dp, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, (!this.canDrawInParent || (chatActionCellDelegate = this.delegate) == null || chatActionCellDelegate.canDrawOutboundsContent()) ? false : true, this.animatedEmojiStack, this.textLayout);
        this.textHeight = 0;
        this.textWidth = 0;
        try {
            int lineCount = this.textLayout.getLineCount();
            for (int i3 = 0; i3 < lineCount; i3++) {
                try {
                    float lineWidth = this.textLayout.getLineWidth(i3);
                    float f = dp;
                    if (lineWidth > f) {
                        lineWidth = f;
                    }
                    this.textHeight = (int) Math.max(this.textHeight, Math.ceil(this.textLayout.getLineBottom(i3)));
                    this.textWidth = (int) Math.max(this.textWidth, Math.ceil(lineWidth));
                } catch (Exception e) {
                    FileLog.e(e);
                    return;
                }
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        this.textX = (i - this.textWidth) / 2;
        this.textY = AndroidUtilities.dp(7.0f);
        this.textXLeft = (i - this.textLayout.getWidth()) / 2;
        this.spoilersPool.addAll(this.spoilers);
        this.spoilers.clear();
        if (charSequence instanceof Spannable) {
            StaticLayout staticLayout = this.textLayout;
            int i4 = this.textX;
            SpoilerEffect.addSpoilers(this, staticLayout, i4, i4 + this.textWidth, (Spannable) charSequence, this.spoilersPool, this.spoilers, null);
        }
    }

    private ColorFilter getAdaptiveEmojiColorFilter(int i) {
        if (i != this.adaptiveEmojiColor || this.adaptiveEmojiColorFilter == null) {
            this.adaptiveEmojiColor = i;
            this.adaptiveEmojiColorFilter = new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN);
        }
        return this.adaptiveEmojiColorFilter;
    }

    private int getImageSize(MessageObject messageObject) {
        return (messageObject.type == 21 || isNewStyleButtonLayout()) ? AndroidUtilities.dp(78.0f) : this.stickerSize;
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.themeDelegate);
    }

    private float getUploadingInfoProgress(MessageObject messageObject) {
        MessagesController messagesController;
        String str;
        if (messageObject == null) {
            return 1.0f;
        }
        try {
            if (messageObject.type == 22 && (str = (messagesController = MessagesController.getInstance(this.currentAccount)).uploadingWallpaper) != null && TextUtils.equals(messageObject.messageOwner.action.wallpaper.uploadingImage, str)) {
                return messagesController.uploadingWallpaperInfo.uploadingProgress;
            }
            return 1.0f;
        } catch (Exception e) {
            FileLog.e(e);
            return 1.0f;
        }
    }

    private boolean isButtonLayout(MessageObject messageObject) {
        int i;
        return messageObject != null && ((i = messageObject.type) == 30 || i == 18 || i == 25 || isNewStyleButtonLayout());
    }

    private boolean isGiftChannel(MessageObject messageObject) {
        return messageObject != null && messageObject.type == 25;
    }

    private boolean isGiftCode() {
        MessageObject messageObject = this.currentMessageObject;
        return messageObject != null && (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionGiftCode);
    }

    private boolean isNewStyleButtonLayout() {
        MessageObject messageObject;
        int i;
        return this.starGiftLayout.has() || (i = (messageObject = this.currentMessageObject).type) == 21 || i == 22 || messageObject.isStoryMention();
    }

    private boolean isSelfGiftCode() {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null) {
            return false;
        }
        TLRPC.Message message = messageObject.messageOwner;
        TLRPC.MessageAction messageAction = message.action;
        if (((messageAction instanceof TLRPC.TL_messageActionGiftCode) || (messageAction instanceof TLRPC.TL_messageActionGiftStars)) && (message.from_id instanceof TLRPC.TL_peerUser)) {
            return UserObject.isUserSelf(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.currentMessageObject.messageOwner.from_id.user_id)));
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        RLottieDrawable lottieAnimation;
        ChatActionCellDelegate chatActionCellDelegate;
        if (!z || (lottieAnimation = this.imageReceiver.getLottieAnimation()) == null) {
            return;
        }
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || messageObject.playedGiftAnimation) {
            if (lottieAnimation.getCurrentFrame() < 1) {
                lottieAnimation.stop();
                lottieAnimation.setCurrentFrame(lottieAnimation.getFramesCount() - 1, false);
                return;
            }
            return;
        }
        messageObject.playedGiftAnimation = true;
        lottieAnimation.setCurrentFrame(0, false);
        AndroidUtilities.runOnUIThread(new ChatActionCell$$ExternalSyntheticLambda7(lottieAnimation));
        if (messageObject.wasUnread || this.forceWasUnread) {
            messageObject.wasUnread = false;
            this.forceWasUnread = false;
            try {
                performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            if (getContext() instanceof LaunchActivity) {
                ((LaunchActivity) getContext()).getFireworksOverlay().start();
            }
            TLRPC.VideoSize videoSize = this.giftEffectAnimation;
            if (videoSize == null || (chatActionCellDelegate = this.delegate) == null) {
                return;
            }
            chatActionCellDelegate.needShowEffectOverlay(this, this.giftSticker, videoSize);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTouchEvent$1() {
        this.isSpoilerRevealing = false;
        getMessageObject().isSpoilersRevealed = true;
        List list = this.giftPremiumText.spoilers;
        if (list != null) {
            list.clear();
        }
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTouchEvent$2() {
        post(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ChatActionCell.this.lambda$onTouchEvent$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openPremiumGiftChannel$3(TLRPC.TL_messageActionGiftCode tL_messageActionGiftCode) {
        this.delegate.didOpenPremiumGiftChannel(this, tL_messageActionGiftCode.slug, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openPremiumGiftPreview$4(TLRPC.TL_premiumGiftOption tL_premiumGiftOption, String str) {
        this.delegate.didOpenPremiumGift(this, tL_premiumGiftOption, str, false);
    }

    private float measureLayoutWidth(Layout layout) {
        float f = 0.0f;
        for (int i = 0; i < layout.getLineCount(); i++) {
            float ceil = (int) Math.ceil(layout.getLineWidth(i));
            if (ceil > f) {
                f = ceil;
            }
        }
        return f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openLink(CharacterStyle characterStyle) {
        if (this.delegate == null || !(characterStyle instanceof URLSpan)) {
            return;
        }
        String url = ((URLSpan) characterStyle).getURL();
        if (url.startsWith("topic")) {
            URLSpan uRLSpan = this.pressedLink;
            if (uRLSpan instanceof URLSpanNoUnderline) {
                TLObject object = ((URLSpanNoUnderline) uRLSpan).getObject();
                if (object instanceof TLRPC.TL_forumTopic) {
                    ForumUtilities.openTopic(this.delegate.getBaseFragment(), -this.delegate.getDialogId(), (TLRPC.TL_forumTopic) object, 0);
                    return;
                }
                return;
            }
        }
        if (url.startsWith("invite")) {
            URLSpan uRLSpan2 = this.pressedLink;
            if (uRLSpan2 instanceof URLSpanNoUnderline) {
                TLObject object2 = ((URLSpanNoUnderline) uRLSpan2).getObject();
                if (object2 instanceof TLRPC.TL_chatInviteExported) {
                    this.delegate.needOpenInviteLink((TLRPC.TL_chatInviteExported) object2);
                    return;
                }
                return;
            }
        }
        if (url.startsWith("game")) {
            this.delegate.didPressReplyMessage(this, this.currentMessageObject.getReplyMsgId());
        } else if (url.startsWith("http")) {
            Browser.openUrl(getContext(), url);
        } else {
            this.delegate.needOpenUserProfile(Long.parseLong(url));
        }
    }

    private void openPremiumGiftChannel() {
        if (this.delegate != null) {
            final TLRPC.TL_messageActionGiftCode tL_messageActionGiftCode = (TLRPC.TL_messageActionGiftCode) this.currentMessageObject.messageOwner.action;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActionCell.this.lambda$openPremiumGiftChannel$3(tL_messageActionGiftCode);
                }
            });
        }
    }

    private void openPremiumGiftPreview() {
        final TLRPC.TL_premiumGiftOption tL_premiumGiftOption = new TLRPC.TL_premiumGiftOption();
        TLRPC.MessageAction messageAction = this.currentMessageObject.messageOwner.action;
        tL_premiumGiftOption.amount = messageAction.amount;
        tL_premiumGiftOption.months = messageAction.months;
        tL_premiumGiftOption.currency = messageAction.currency;
        final String str = (!isGiftCode() || isSelfGiftCode()) ? null : ((TLRPC.TL_messageActionGiftCode) this.currentMessageObject.messageOwner.action).slug;
        if (this.delegate != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActionCell.this.lambda$openPremiumGiftPreview$4(tL_premiumGiftOption, str);
                }
            });
        }
    }

    private void openStarsGiftTransaction() {
        TLRPC.Message message;
        StarGiftSheet starGiftSheet;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || (message = messageObject.messageOwner) == null) {
            return;
        }
        TLRPC.MessageAction messageAction = message.action;
        if (messageAction instanceof TLRPC.TL_messageActionGiftStars) {
            Context context = getContext();
            int i = this.currentAccount;
            TLRPC.Message message2 = this.currentMessageObject.messageOwner;
            StarsIntroActivity.showTransactionSheet(context, i, message2.date, message2.from_id, message2.peer_id, (TLRPC.TL_messageActionGiftStars) message2.action, this.avatarStoryParams.resourcesProvider);
            return;
        }
        if (messageAction instanceof TLRPC.TL_messageActionPrizeStars) {
            Context context2 = getContext();
            int i2 = this.currentAccount;
            TLRPC.Message message3 = this.currentMessageObject.messageOwner;
            StarsIntroActivity.showTransactionSheet(context2, i2, message3.date, message3.from_id, message3.peer_id, (TLRPC.TL_messageActionPrizeStars) message3.action, this.avatarStoryParams.resourcesProvider);
            return;
        }
        if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
            if (((TLRPC.TL_messageActionStarGift) messageAction).forceIn) {
                return;
            } else {
                starGiftSheet = new StarGiftSheet(getContext(), this.currentAccount, this.currentMessageObject.getDialogId(), this.themeDelegate);
            }
        } else if (!(messageAction instanceof TLRPC.TL_messageActionStarGiftUnique)) {
            return;
        } else {
            starGiftSheet = new StarGiftSheet(getContext(), this.currentAccount, this.currentMessageObject.getDialogId(), this.themeDelegate);
        }
        starGiftSheet.set(this.currentMessageObject).show();
    }

    private void setStarsPaused(boolean z) {
        StarParticlesView.Drawable drawable = this.starParticlesDrawable;
        if (z == drawable.paused) {
            return;
        }
        drawable.paused = z;
        if (z) {
            drawable.pausedTime = System.currentTimeMillis();
            return;
        }
        for (int i = 0; i < this.starParticlesDrawable.particles.size(); i++) {
            ((StarParticlesView.Drawable.Particle) this.starParticlesDrawable.particles.get(i)).lifeTime += System.currentTimeMillis() - this.starParticlesDrawable.pausedTime;
        }
        invalidate();
    }

    private void updateTextInternal(boolean z) {
        if (getMeasuredWidth() != 0) {
            createLayout(this.customText, getMeasuredWidth());
            invalidate();
        }
        if (this.wasLayout) {
            buildLayout();
        } else if (z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActionCell.this.requestLayout();
                }
            });
        } else {
            requestLayout();
        }
    }

    public boolean checkUnreadReactions(float f, int i) {
        if (!this.reactionsLayoutInBubble.hasUnreadReactions) {
            return false;
        }
        float y = getY();
        float f2 = y + r2.y;
        return f2 > f && (f2 + ((float) this.reactionsLayoutInBubble.height)) - ((float) AndroidUtilities.dp(16.0f)) < ((float) i);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        MessageObject messageObject;
        if (i == NotificationCenter.startSpoilers) {
            setSpoilersSuppressed(false);
            return;
        }
        if (i == NotificationCenter.stopSpoilers) {
            setSpoilersSuppressed(true);
            return;
        }
        if (i == NotificationCenter.didUpdatePremiumGiftStickers || i == NotificationCenter.starGiftsLoaded) {
            messageObject = this.currentMessageObject;
            if (messageObject == null) {
                return;
            }
        } else if (i != NotificationCenter.diceStickersDidLoad || !Objects.equals(objArr[0], UserConfig.getInstance(this.currentAccount).premiumGiftsStickerPack) || (messageObject = this.currentMessageObject) == null) {
            return;
        }
        setMessageObject(messageObject, true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:148:0x0400, code lost:
    
        if (isFloating() != false) goto L158;
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x042b, code lost:
    
        r1 = 1.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x042d, code lost:
    
        r1 = (int) (r5 * r1);
        r5 = r20;
        r5.setAlpha(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x04dc, code lost:
    
        if (hasGradientService() != false) goto L198;
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x057d, code lost:
    
        r29.drawRoundRect(r28.backgroundRect, org.telegram.messenger.AndroidUtilities.dp(16.0f), org.telegram.messenger.AndroidUtilities.dp(16.0f), r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x057b, code lost:
    
        if (hasGradientService() != false) goto L198;
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0428, code lost:
    
        if (isFloating() != false) goto L158;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void drawBackground(Canvas canvas, boolean z) {
        Paint paint;
        Paint paint2;
        Paint paint3;
        Paint paint4;
        int i;
        int i2;
        float f;
        TextLayout textLayout;
        float f2;
        Paint paint5;
        Paint paint6;
        int i3;
        float f3;
        int i4;
        float f4;
        int i5;
        int i6;
        int i7;
        float f5;
        Path path;
        RectF rectF;
        float f6;
        float f7;
        int i8;
        if (this.canDrawInParent) {
            if (hasGradientService() && !z) {
                return;
            }
            if (!hasGradientService() && z) {
                return;
            }
        }
        Paint themedPaint = getThemedPaint("paintChatActionBackground");
        Paint themedPaint2 = getThemedPaint("paintChatActionBackgroundDarken");
        this.textPaint = (TextPaint) getThemedPaint("paintChatActionText");
        int i9 = this.overrideBackground;
        if (i9 >= 0) {
            int themedColor = getThemedColor(i9);
            if (this.overrideBackgroundPaint == null) {
                Paint paint7 = new Paint(1);
                this.overrideBackgroundPaint = paint7;
                paint7.setColor(themedColor);
                TextPaint textPaint = new TextPaint(1);
                this.overrideTextPaint = textPaint;
                textPaint.setTypeface(AndroidUtilities.bold());
                this.overrideTextPaint.setTextSize(AndroidUtilities.dp(Math.max(16, SharedConfig.fontSize) - 2));
                this.overrideTextPaint.setColor(getThemedColor(this.overrideText));
            }
            themedPaint = this.overrideBackgroundPaint;
            this.textPaint = this.overrideTextPaint;
        }
        if (this.invalidatePath) {
            this.invalidatePath = false;
            this.backgroundLeft = getWidth();
            this.backgroundRight = 0;
            this.lineWidths.clear();
            StaticLayout staticLayout = this.textLayout;
            int lineCount = staticLayout == null ? 0 : staticLayout.getLineCount();
            int dp = AndroidUtilities.dp(11.0f);
            int dp2 = AndroidUtilities.dp(8.0f);
            int i10 = 0;
            int i11 = 0;
            while (true) {
                f2 = 1.5f;
                if (i10 >= lineCount) {
                    break;
                }
                int ceil = (int) Math.ceil(this.textLayout.getLineWidth(i10));
                if (i10 == 0 || (i8 = i11 - ceil) <= 0 || i8 > (dp * 1.5f) + dp2) {
                    i11 = ceil;
                }
                this.lineWidths.add(Integer.valueOf(i11));
                i10++;
            }
            int i12 = lineCount - 2;
            while (i12 >= 0) {
                int intValue = ((Integer) this.lineWidths.get(i12)).intValue();
                int i13 = i11 - intValue;
                if (i13 <= 0 || i13 > (dp * f2) + dp2) {
                    i11 = intValue;
                }
                this.lineWidths.set(i12, Integer.valueOf(i11));
                i12--;
                f2 = 1.5f;
            }
            int dp3 = AndroidUtilities.dp(4.0f);
            int measuredWidth = getMeasuredWidth() / 2;
            int dp4 = AndroidUtilities.dp(3.0f);
            int dp5 = AndroidUtilities.dp(6.0f);
            int i14 = dp - dp4;
            this.lineHeights.clear();
            this.backgroundPath.reset();
            float f8 = measuredWidth;
            this.backgroundPath.moveTo(f8, dp3);
            int i15 = 0;
            int i16 = 0;
            while (i15 < lineCount) {
                int intValue2 = ((Integer) this.lineWidths.get(i15)).intValue();
                int i17 = dp5;
                int lineBottom = this.textLayout.getLineBottom(i15);
                int i18 = lineCount - 1;
                if (i15 < i18) {
                    paint6 = themedPaint2;
                    paint5 = themedPaint;
                    i3 = ((Integer) this.lineWidths.get(i15 + 1)).intValue();
                } else {
                    paint5 = themedPaint;
                    paint6 = themedPaint2;
                    i3 = 0;
                }
                int i19 = lineBottom - i16;
                if (i15 == 0 || intValue2 > i11) {
                    f3 = 3.0f;
                    i19 += AndroidUtilities.dp(3.0f);
                } else {
                    f3 = 3.0f;
                }
                if (i15 == i18 || intValue2 > i3) {
                    i19 += AndroidUtilities.dp(f3);
                }
                float f9 = (intValue2 / 2.0f) + f8;
                int i20 = (i15 == i18 || intValue2 >= i3 || i15 == 0 || intValue2 >= i11) ? dp2 : i17;
                if (i15 == 0 || intValue2 > i11) {
                    i4 = measuredWidth;
                    f4 = f8;
                    i5 = lineCount;
                    i6 = i11;
                    i7 = lineBottom;
                    this.rect.set((f9 - dp4) - dp, dp3, i14 + f9, (dp * 2) + dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, -90.0f, 90.0f);
                } else if (intValue2 < i11) {
                    f4 = f8;
                    i7 = lineBottom;
                    float f10 = i14 + f9;
                    i4 = measuredWidth;
                    i5 = lineCount;
                    i6 = i11;
                    this.rect.set(f10, dp3, (i20 * 2) + f10, r10 + dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, -90.0f, -90.0f);
                } else {
                    i4 = measuredWidth;
                    f4 = f8;
                    i5 = lineCount;
                    i6 = i11;
                    i7 = lineBottom;
                }
                dp3 += i19;
                if (i15 == i18 || intValue2 >= i3) {
                    f5 = 3.0f;
                } else {
                    f5 = 3.0f;
                    dp3 -= AndroidUtilities.dp(3.0f);
                    i19 -= AndroidUtilities.dp(3.0f);
                }
                if (i15 != 0 && intValue2 < i6) {
                    dp3 -= AndroidUtilities.dp(f5);
                    i19 -= AndroidUtilities.dp(f5);
                }
                this.lineHeights.add(Integer.valueOf(i19));
                if (i15 == i18 || intValue2 > i3) {
                    this.rect.set((f9 - dp4) - dp, dp3 - (dp * 2), f9 + i14, dp3);
                    checkLeftRightBounds();
                    path = this.backgroundPath;
                    rectF = this.rect;
                    f6 = 0.0f;
                    f7 = 90.0f;
                } else if (intValue2 < i3) {
                    float f11 = f9 + i14;
                    this.rect.set(f11, dp3 - r2, (i20 * 2) + f11, dp3);
                    checkLeftRightBounds();
                    path = this.backgroundPath;
                    rectF = this.rect;
                    f6 = 180.0f;
                    f7 = -90.0f;
                } else {
                    i15++;
                    i11 = intValue2;
                    dp5 = i17;
                    themedPaint2 = paint6;
                    themedPaint = paint5;
                    f8 = f4;
                    i16 = i7;
                    measuredWidth = i4;
                    lineCount = i5;
                }
                path.arcTo(rectF, f6, f7);
                i15++;
                i11 = intValue2;
                dp5 = i17;
                themedPaint2 = paint6;
                themedPaint = paint5;
                f8 = f4;
                i16 = i7;
                measuredWidth = i4;
                lineCount = i5;
            }
            paint = themedPaint;
            paint2 = themedPaint2;
            int i21 = measuredWidth;
            int i22 = dp5;
            int i23 = lineCount - 1;
            int i24 = i23;
            while (i24 >= 0) {
                int intValue3 = i24 != 0 ? ((Integer) this.lineWidths.get(i24 - 1)).intValue() : 0;
                int intValue4 = ((Integer) this.lineWidths.get(i24)).intValue();
                int intValue5 = i24 != i23 ? ((Integer) this.lineWidths.get(i24 + 1)).intValue() : 0;
                this.textLayout.getLineBottom(i24);
                float f12 = i21 - (intValue4 / 2);
                int i25 = (i24 == i23 || intValue4 >= intValue5 || i24 == 0 || intValue4 >= intValue3) ? dp2 : i22;
                if (i24 == i23 || intValue4 > intValue5) {
                    this.rect.set(f12 - i14, dp3 - (dp * 2), dp4 + f12 + dp, dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 90.0f, 90.0f);
                } else if (intValue4 < intValue5) {
                    float f13 = f12 - i14;
                    this.rect.set(f13 - (i25 * 2), dp3 - r12, f13, dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 90.0f, -90.0f);
                }
                dp3 -= ((Integer) this.lineHeights.get(i24)).intValue();
                if (i24 == 0 || intValue4 > intValue3) {
                    this.rect.set(f12 - i14, dp3, f12 + dp4 + dp, (dp * 2) + dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 180.0f, 90.0f);
                } else if (intValue4 < intValue3) {
                    float f14 = f12 - i14;
                    this.rect.set(f14 - (i25 * 2), dp3, f14, r7 + dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 0.0f, -90.0f);
                }
                i24--;
            }
            this.backgroundPath.close();
        } else {
            paint = themedPaint;
            paint2 = themedPaint2;
        }
        if (!this.visiblePartSet) {
            this.backgroundHeight = ((ViewGroup) getParent()).getMeasuredHeight();
        }
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        if (resourcesProvider != null) {
            resourcesProvider.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        } else {
            Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        }
        float f15 = 0.75f;
        if (!z || (getAlpha() == 1.0f && !isFloating())) {
            paint3 = paint;
            if (isFloating()) {
                i = paint3.getAlpha();
                i2 = paint2.getAlpha();
                paint3.setAlpha((int) (i * (isFloating() ? 0.75f : 1.0f)));
                f = i2;
            } else {
                paint4 = paint2;
                i = -1;
                i2 = -1;
            }
        } else {
            i = paint.getAlpha();
            i2 = paint2.getAlpha();
            paint3 = paint;
            paint3.setAlpha((int) (i * getAlpha() * (isFloating() ? 0.75f : 1.0f)));
            f = i2 * getAlpha();
        }
        canvas.drawPath(this.backgroundPath, paint3);
        if (hasGradientService()) {
            canvas.drawPath(this.backgroundPath, paint4);
        }
        if (this.dimAmount > 0.0f) {
            int alpha = this.dimPaint.getAlpha();
            if (z) {
                this.dimPaint.setAlpha((int) (alpha * getAlpha()));
            }
            canvas.drawPath(this.backgroundPath, this.dimPaint);
            this.dimPaint.setAlpha(alpha);
        }
        MessageObject messageObject = this.currentMessageObject;
        if (this.starGiftLayout.has()) {
            float width = this.starGiftLayout.getWidth() + AndroidUtilities.dp(8.0f);
            float width2 = (getWidth() - width) / 2.0f;
            float dp6 = this.textY + this.textHeight + AndroidUtilities.dp(12.0f);
            RectF rectF2 = AndroidUtilities.rectTmp;
            rectF2.set(width2, dp6, width + width2, this.starGiftLayout.getHeight() + dp6 + AndroidUtilities.dp(8.0f));
            if (this.backgroundRect == null) {
                this.backgroundRect = new RectF();
            }
            this.backgroundRect.set(rectF2);
            canvas.drawRoundRect(this.backgroundRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), paint3);
        } else if (isButtonLayout(messageObject)) {
            float width3 = (getWidth() - this.giftRectSize) / 2.0f;
            float f16 = this.textY + this.textHeight;
            if (isNewStyleButtonLayout()) {
                float dp7 = f16 + AndroidUtilities.dp(4.0f);
                AndroidUtilities.rectTmp.set(width3, dp7, this.giftRectSize + width3, this.backgroundRectHeight + dp7);
            } else {
                float dp8 = f16 + AndroidUtilities.dp(12.0f);
                RectF rectF3 = AndroidUtilities.rectTmp;
                float f17 = this.giftRectSize;
                rectF3.set(width3, dp8, width3 + f17, f17 + dp8 + this.giftPremiumAdditionalHeight);
            }
            if (messageObject != null && messageObject.type == 18 && !this.giftPremiumTextCollapsed && (textLayout = this.giftPremiumText) != null && this.giftPremiumTextCollapsedHeight > 0) {
                AndroidUtilities.rectTmp.bottom -= (textLayout.layout.getHeight() - this.giftPremiumTextCollapsedHeight) * (1.0f - this.giftPremiumTextExpandedAnimated.get());
            }
            if (this.backgroundRect == null) {
                this.backgroundRect = new RectF();
            }
            this.backgroundRect.set(AndroidUtilities.rectTmp);
            canvas.drawRoundRect(this.backgroundRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), paint3);
        }
        if (i >= 0) {
            paint3.setAlpha(i);
            paint4.setAlpha(i2);
        }
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        if (view != this.rippleView) {
            return super.drawChild(canvas, view, j);
        }
        float scale = this.bounce.getScale(0.02f);
        canvas.save();
        canvas.scale(scale, scale, view.getX() + (view.getMeasuredWidth() / 2.0f), view.getY() + (view.getMeasuredHeight() / 2.0f));
        boolean drawChild = super.drawChild(canvas, view, j);
        canvas.restore();
        return drawChild;
    }

    public void drawOutboundsContent(Canvas canvas) {
        canvas.save();
        canvas.translate(this.textXLeft, this.textY);
        StaticLayout staticLayout = this.textLayout;
        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout, this.animatedEmojiStack, 0.0f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f, staticLayout != null ? getAdaptiveEmojiColorFilter(staticLayout.getPaint().getColor()) : null);
        canvas.restore();
        if (this.starGiftLayout.has()) {
            canvas.save();
            canvas.translate((getWidth() - this.starGiftLayout.getWidth()) / 2.0f, this.textY + this.textHeight + AndroidUtilities.dp(16.0f));
            this.starGiftLayout.drawOutbounds(canvas);
            canvas.restore();
        }
    }

    public void drawReactions(Canvas canvas, boolean z) {
        float alpha = z ? getAlpha() : 1.0f;
        if (this.canDrawInParent) {
            if (hasGradientService() && !z) {
                return;
            }
            if (!hasGradientService() && z) {
                return;
            }
        }
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        if (resourcesProvider != null) {
            resourcesProvider.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        } else {
            Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        }
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || !messageObject.shouldDrawReactions()) {
            return;
        }
        ReactionsLayoutInBubble reactionsLayoutInBubble = this.reactionsLayoutInBubble;
        if (!reactionsLayoutInBubble.isSmall || (this.transitionParams.animateChange && reactionsLayoutInBubble.animateHeight)) {
            reactionsLayoutInBubble.drawServiceShaderBackground = 1.0f;
            if (alpha < 1.0f) {
                canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) (255.0f * alpha), 31);
            }
            ReactionsLayoutInBubble reactionsLayoutInBubble2 = this.reactionsLayoutInBubble;
            TransitionParams transitionParams = this.transitionParams;
            reactionsLayoutInBubble2.draw(canvas, transitionParams.animateChange ? transitionParams.animateChangeProgress : 1.0f, null);
            if (alpha < 1.0f) {
                canvas.restore();
            }
        }
    }

    public void drawScrimReaction(Canvas canvas, Integer num, float f, boolean z) {
        if (this.reactionsLayoutInBubble.isSmall) {
            return;
        }
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        if (resourcesProvider != null) {
            resourcesProvider.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        } else {
            Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        }
        this.reactionsLayoutInBubble.setScrimProgress(f, z);
        this.reactionsLayoutInBubble.draw(canvas, this.transitionParams.animateChangeProgress, num);
    }

    public void drawScrimReactionPreview(View view, Canvas canvas, int i, Integer num, float f) {
        if (this.reactionsLayoutInBubble.isSmall) {
            return;
        }
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        if (resourcesProvider != null) {
            resourcesProvider.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        } else {
            Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
        }
        this.reactionsLayoutInBubble.setScrimProgress(f);
        this.reactionsLayoutInBubble.drawPreview(view, canvas, i, num);
    }

    @Override // org.telegram.ui.Cells.BaseCell
    public int getBoundsLeft() {
        int width;
        if (this.starGiftLayout.has()) {
            width = (int) (getWidth() - (this.starGiftLayout.getWidth() + AndroidUtilities.dp(12.0f)));
        } else {
            if (!isButtonLayout(this.currentMessageObject)) {
                int i = this.backgroundLeft;
                ImageReceiver imageReceiver = this.imageReceiver;
                return (imageReceiver == null || !imageReceiver.getVisible()) ? i : Math.min((int) this.imageReceiver.getImageX(), i);
            }
            width = getWidth() - this.giftRectSize;
        }
        return width / 2;
    }

    @Override // org.telegram.ui.Cells.BaseCell
    public int getBoundsRight() {
        int width;
        if (this.starGiftLayout.has()) {
            width = (int) (getWidth() + this.starGiftLayout.getWidth() + AndroidUtilities.dp(12.0f));
        } else {
            if (!isButtonLayout(this.currentMessageObject)) {
                int i = this.backgroundRight;
                ImageReceiver imageReceiver = this.imageReceiver;
                return (imageReceiver == null || !imageReceiver.getVisible()) ? i : Math.max((int) this.imageReceiver.getImageX2(), i);
            }
            width = getWidth() + this.giftRectSize;
        }
        return width / 2;
    }

    public int getCustomDate() {
        return this.customDate;
    }

    public ChatActionCellDelegate getDelegate() {
        return this.delegate;
    }

    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
    }

    public ImageReceiver getPhotoImage() {
        return this.imageReceiver;
    }

    public ReactionsLayoutInBubble.ReactionButton getReactionButton(ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        return this.reactionsLayoutInBubble.getReactionButton(visibleReaction);
    }

    protected Paint getThemedPaint(String str) {
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        Paint paint = resourcesProvider != null ? resourcesProvider.getPaint(str) : null;
        return paint != null ? paint : Theme.getThemePaint(str);
    }

    public TransitionParams getTransitionParams() {
        return this.transitionParams;
    }

    public boolean hasButton() {
        MessageObject messageObject = this.currentMessageObject;
        return (messageObject == null || !isButtonLayout(messageObject) || this.giftPremiumButtonLayout == null) ? false : true;
    }

    public boolean hasGradientService() {
        Theme.ResourcesProvider resourcesProvider;
        return this.overrideBackgroundPaint == null && ((resourcesProvider = this.themeDelegate) == null ? Theme.hasGradientService() : resourcesProvider.hasGradientService());
    }

    @Override // org.telegram.ui.Cells.BaseCell, android.view.View
    public void invalidate() {
        super.invalidate();
        View view = this.invalidateWithParent;
        if (view != null) {
            view.invalidate();
        }
        if (!this.invalidatesParent || getParent() == null) {
            return;
        }
        View view2 = (View) getParent();
        if (view2.getParent() != null) {
            view2.invalidate();
            ((View) view2.getParent()).invalidate();
        }
    }

    @Override // android.view.View
    public void invalidate(int i, int i2, int i3, int i4) {
        super.invalidate(i, i2, i3, i4);
        View view = this.invalidateWithParent;
        if (view != null) {
            view.invalidate();
        }
        if (!this.invalidatesParent || getParent() == null) {
            return;
        }
        View view2 = (View) getParent();
        if (view2.getParent() != null) {
            view2.invalidate();
            ((View) view2.getParent()).invalidate();
        }
    }

    @Override // android.view.View
    public void invalidate(Rect rect) {
        super.invalidate(rect);
        View view = this.invalidateWithParent;
        if (view != null) {
            view.invalidate();
        }
        if (!this.invalidatesParent || getParent() == null) {
            return;
        }
        View view2 = (View) getParent();
        if (view2.getParent() != null) {
            view2.invalidate();
            ((View) view2.getParent()).invalidate();
        }
    }

    public void invalidateOutbounds() {
        ChatActionCellDelegate chatActionCellDelegate = this.delegate;
        if (chatActionCellDelegate != null && chatActionCellDelegate.canDrawOutboundsContent()) {
            super.invalidate();
        } else if (getParent() instanceof View) {
            ((View) getParent()).invalidate();
        }
    }

    public boolean isFloating() {
        return false;
    }

    public void markReactionsAsRead() {
        this.reactionsLayoutInBubble.hasUnreadReactions = false;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null) {
            return;
        }
        messageObject.markReactionsAsRead();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        ChatActionCellDelegate chatActionCellDelegate;
        super.onAttachedToWindow();
        this.imageReceiver.onAttachedToWindow();
        setStarsPaused(false);
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, (!this.canDrawInParent || (chatActionCellDelegate = this.delegate) == null || chatActionCellDelegate.canDrawOutboundsContent()) ? false : true, this.animatedEmojiStack, this.textLayout);
        TextLayout textLayout = this.giftPremiumText;
        if (textLayout != null) {
            textLayout.attach();
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdatePremiumGiftStickers);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starGiftsLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.diceStickersDidLoad);
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && messageObject.type == 21) {
            setMessageObject(messageObject, true);
        }
        this.starGiftLayout.attach();
        this.reactionsLayoutInBubble.onAttachToWindow();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
        this.imageReceiver.onDetachedFromWindow();
        setStarsPaused(true);
        this.wasLayout = false;
        AnimatedEmojiSpan.release(this, this.animatedEmojiStack);
        TextLayout textLayout = this.giftPremiumText;
        if (textLayout != null) {
            textLayout.detach();
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdatePremiumGiftStickers);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starGiftsLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.diceStickersDidLoad);
        this.avatarStoryParams.onDetachFromWindow();
        this.transitionParams.onDetach();
        this.starGiftLayout.detach();
        this.reactionsLayoutInBubble.onDetachFromWindow();
    }

    /* JADX WARN: Code restructure failed: missing block: B:308:0x0312, code lost:
    
        if (r0.getCurrentImageProgress() == 1.0f) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x0351, code lost:
    
        r42.radialProgress.setIcon(3, true, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x034a, code lost:
    
        r42.radialProgress.setIcon(4, true, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x0348, code lost:
    
        if (r0 == 1.0f) goto L91;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0539  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x09c5  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x09cc  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x09db  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x09e8  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0a2e  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0acc  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0af3  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0b44  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x0b8e  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0c18  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x0c4e  */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0c8b  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x0cf4  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0cba  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x0cc6  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x0cca  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0cbe  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0c90  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x0c64  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0bea  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0adb  */
    /* JADX WARN: Removed duplicated region for block: B:208:0x09fc  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0818  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x050f  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x0448  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0429  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x049b  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0524  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        float f;
        float f2;
        float dp;
        float dp2;
        float f3;
        float f4;
        float f5;
        float f6;
        Text text;
        TextLayout textLayout;
        StaticLayout staticLayout;
        Theme.ResourcesProvider resourcesProvider;
        boolean z;
        float clamp;
        Paint themedPaint;
        Theme.ResourcesProvider resourcesProvider2;
        boolean isDark;
        ColorMatrix colorMatrix;
        int i;
        int i2;
        ImageReceiver imageReceiver;
        float f7;
        float f8;
        int dp3;
        float f9;
        MessageObject messageObject = this.currentMessageObject;
        float f10 = this.giftPremiumTextExpandedAnimated.set(!this.giftPremiumTextCollapsed);
        int i3 = this.stickerSize;
        if (!this.starGiftLayout.has() && isButtonLayout(messageObject)) {
            this.stickerSize = this.giftRectSize - AndroidUtilities.dp(106.0f);
            if (isNewStyleButtonLayout()) {
                i3 = getImageSize(messageObject);
                float f11 = (this.previousWidth - i3) / 2.0f;
                float dp4 = this.textY + this.textHeight + AndroidUtilities.dp(4.0f) + AndroidUtilities.dp(16.0f);
                if (messageObject.isStoryMention()) {
                    this.avatarStoryParams.storyItem = messageObject.messageOwner.media.storyItem;
                }
                float f12 = i3;
                this.avatarStoryParams.originalAvatarRect.set(f11, dp4, f11 + f12, dp4 + f12);
                this.imageReceiver.setImageCoords(f11, dp4, f12, f12);
            } else {
                int i4 = messageObject.type;
                if (i4 == 11) {
                    ImageReceiver imageReceiver2 = this.imageReceiver;
                    int i5 = this.previousWidth;
                    float f13 = this.stickerSize;
                    imageReceiver2.setImageCoords((i5 - r4) / 2.0f, this.textY + this.textHeight + (this.giftRectSize * 0.075f), f13, f13);
                } else {
                    if (i4 == 25) {
                        i3 = (int) (this.stickerSize * (AndroidUtilities.isTablet() ? 1.0f : 1.2f));
                    } else {
                        if (messageObject.isStarGiftAction()) {
                            imageReceiver = this.imageReceiver;
                            f7 = (this.previousWidth - i3) / 2.0f;
                            f9 = this.textY + this.textHeight + (this.giftRectSize * 0.075f) + AndroidUtilities.dp(2.0f);
                        } else {
                            if (messageObject.type == 30) {
                                i3 = (int) (this.stickerSize * 1.1f);
                                TLRPC.Message message = messageObject.messageOwner;
                                if (message == null || (message.action instanceof TLRPC.TL_messageActionStarGift)) {
                                    imageReceiver = this.imageReceiver;
                                    f7 = (this.previousWidth - i3) / 2.0f;
                                    f8 = this.textY + this.textHeight + (this.giftRectSize * 0.075f);
                                    dp3 = AndroidUtilities.dp(12.0f);
                                }
                            } else {
                                i3 = (int) (this.stickerSize * 1.0f);
                                imageReceiver = this.imageReceiver;
                                f7 = (this.previousWidth - i3) / 2.0f;
                                f8 = this.textY + this.textHeight + (this.giftRectSize * 0.075f);
                                dp3 = AndroidUtilities.dp(4.0f);
                            }
                            f9 = f8 - dp3;
                        }
                        float f14 = i3;
                        imageReceiver.setImageCoords(f7, f9, f14, f14);
                    }
                    float f15 = i3;
                    this.imageReceiver.setImageCoords((this.previousWidth - i3) / 2.0f, ((this.textY + this.textHeight) + (this.giftRectSize * 0.075f)) - AndroidUtilities.dp(22.0f), f15, f15);
                }
            }
            TextPaint textPaint = (TextPaint) getThemedPaint("paintChatActionText");
            this.textPaint = textPaint;
            if (textPaint != null) {
                TextPaint textPaint2 = this.giftTitlePaint;
                if (textPaint2 != null && textPaint2.getColor() != this.textPaint.getColor()) {
                    this.giftTitlePaint.setColor(this.textPaint.getColor());
                }
                TextPaint textPaint3 = this.giftSubtitlePaint;
                if (textPaint3 != null && textPaint3.getColor() != this.textPaint.getColor()) {
                    this.giftSubtitlePaint.setColor(this.textPaint.getColor());
                    this.giftSubtitlePaint.linkColor = this.textPaint.getColor();
                }
                TextPaint textPaint4 = this.giftTextPaint;
                if (textPaint4 != null && textPaint4.getColor() != this.textPaint.getColor()) {
                    this.giftTextPaint.setColor(this.textPaint.getColor());
                    this.giftTextPaint.linkColor = this.textPaint.getColor();
                }
            }
        }
        int i6 = i3;
        drawBackground(canvas, false);
        if (this.starGiftLayout.has()) {
            canvas.save();
            float width = (getWidth() - this.starGiftLayout.getWidth()) / 2.0f;
            this.starGiftLayoutX = width;
            float dp5 = this.textY + this.textHeight + AndroidUtilities.dp(16.0f);
            this.starGiftLayoutY = dp5;
            canvas.translate(width, dp5);
            this.starGiftLayout.draw(canvas);
            ChatActionCellDelegate chatActionCellDelegate = this.delegate;
            if (chatActionCellDelegate == null || chatActionCellDelegate.canDrawOutboundsContent()) {
                this.starGiftLayout.drawOutbounds(canvas);
            }
            canvas.restore();
        } else if (isButtonLayout(messageObject) || (messageObject != null && messageObject.type == 11)) {
            if (this.wallpaperPreviewDrawable != null) {
                canvas.save();
                canvas.translate(this.imageReceiver.getImageX(), this.imageReceiver.getImageY());
                Path path = this.clipPath;
                if (path == null) {
                    this.clipPath = new Path();
                } else {
                    path.rewind();
                }
                this.clipPath.addCircle(this.imageReceiver.getImageWidth() / 2.0f, this.imageReceiver.getImageHeight() / 2.0f, this.imageReceiver.getImageWidth() / 2.0f, Path.Direction.CW);
                canvas.clipPath(this.clipPath);
                this.wallpaperPreviewDrawable.setBounds(0, 0, (int) this.imageReceiver.getImageWidth(), (int) this.imageReceiver.getImageHeight());
                this.wallpaperPreviewDrawable.draw(canvas);
                canvas.restore();
            } else if (messageObject.isStoryMention()) {
                TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
                long j = messageMedia.user_id;
                StoriesUtilities.AvatarStoryParams avatarStoryParams = this.avatarStoryParams;
                avatarStoryParams.storyId = messageMedia.id;
                StoriesUtilities.drawAvatarWithStory(j, canvas, this.imageReceiver, avatarStoryParams);
            } else {
                this.imageReceiver.draw(canvas);
            }
            this.radialProgress.setProgressRect(this.imageReceiver.getImageX(), this.imageReceiver.getImageY(), this.imageReceiver.getImageX() + this.imageReceiver.getImageWidth(), this.imageReceiver.getImageY() + this.imageReceiver.getImageHeight());
            int i7 = messageObject.type;
            if (i7 == 21) {
                ImageUpdater imageUpdater = MessagesController.getInstance(this.currentAccount).photoSuggestion.get(messageObject.messageOwner.local_id);
                if (imageUpdater != null) {
                    this.radialProgress.setProgress(imageUpdater.getCurrentImageProgress(), true);
                    this.radialProgress.setCircleRadius(((int) (this.imageReceiver.getImageWidth() * 0.5f)) + 1);
                    this.radialProgress.setMaxIconSize(AndroidUtilities.dp(24.0f));
                    this.radialProgress.setColorKeys(Theme.key_chat_mediaLoaderPhoto, Theme.key_chat_mediaLoaderPhotoSelected, Theme.key_chat_mediaLoaderPhotoIcon, Theme.key_chat_mediaLoaderPhotoIconSelected);
                }
                this.radialProgress.draw(canvas);
            } else if (i7 == 22) {
                float uploadingInfoProgress = getUploadingInfoProgress(messageObject);
                this.radialProgress.setProgress(uploadingInfoProgress, true);
                this.radialProgress.setCircleRadius(AndroidUtilities.dp(26.0f));
                this.radialProgress.setMaxIconSize(AndroidUtilities.dp(24.0f));
                this.radialProgress.setColorKeys(Theme.key_chat_mediaLoaderPhoto, Theme.key_chat_mediaLoaderPhotoSelected, Theme.key_chat_mediaLoaderPhotoIcon, Theme.key_chat_mediaLoaderPhotoIconSelected);
            }
            if (this.textPaint != null || this.textLayout == null) {
                f = 2.0f;
                f2 = 16.0f;
            } else {
                canvas.save();
                canvas.translate(this.textXLeft, this.textY);
                if (this.textLayout.getPaint() != this.textPaint) {
                    buildLayout();
                }
                canvas.save();
                SpoilerEffect.clipOutCanvas(canvas, this.spoilers);
                SpoilerEffect.layoutDrawMaybe(this.textLayout, canvas);
                ChatActionCellDelegate chatActionCellDelegate2 = this.delegate;
                if (chatActionCellDelegate2 == null || chatActionCellDelegate2.canDrawOutboundsContent()) {
                    StaticLayout staticLayout2 = this.textLayout;
                    f = 2.0f;
                    f2 = 16.0f;
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout2, this.animatedEmojiStack, 0.0f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f, staticLayout2 == null ? null : getAdaptiveEmojiColorFilter(staticLayout2.getPaint().getColor()));
                } else {
                    f = 2.0f;
                    f2 = 16.0f;
                }
                canvas.restore();
                for (SpoilerEffect spoilerEffect : this.spoilers) {
                    spoilerEffect.setColor(this.textLayout.getPaint().getColor());
                    spoilerEffect.draw(canvas);
                }
                canvas.restore();
            }
            if (!this.starGiftLayout.has() && isButtonLayout(messageObject)) {
                canvas.save();
                float dp6 = ((this.previousWidth - this.giftRectSize) / f) + AndroidUtilities.dp(8.0f);
                if (isNewStyleButtonLayout()) {
                    float f16 = this.textY + this.textHeight + (this.giftRectSize * 0.075f);
                    if (messageObject.type != 21) {
                        i6 = this.stickerSize;
                    }
                    dp = f16 + i6 + AndroidUtilities.dp(4.0f);
                    if (messageObject.type == 21) {
                        dp += AndroidUtilities.dp(f2);
                    }
                    if (messageObject.isStarGiftAction()) {
                        dp += AndroidUtilities.dp(12.0f);
                    } else if (messageObject.type == 30 && !messageObject.isStarGiftAction()) {
                        dp -= AndroidUtilities.dp(3.66f);
                    }
                } else {
                    RectF rectF = this.backgroundRect;
                    dp = (rectF != null ? rectF.top : this.textY + this.textHeight + AndroidUtilities.dp(4.0f)) + (AndroidUtilities.dp(f2) * 2) + i6;
                }
                canvas.translate(dp6, dp);
                if (this.giftPremiumTitleLayout == null) {
                    canvas.save();
                    canvas.translate(((this.giftRectSize - AndroidUtilities.dp(f2)) - this.giftPremiumTitleLayout.getWidth()) / f, 0.0f);
                    this.giftPremiumTitleLayout.draw(canvas);
                    canvas.restore();
                    float height = dp + this.giftPremiumTitleLayout.getHeight();
                    if (this.giftPremiumSubtitleLayout != null) {
                        canvas.save();
                        canvas.translate(((this.giftRectSize - AndroidUtilities.dp(f2)) - this.giftPremiumSubtitleLayout.getWidth()) / f, this.giftPremiumTitleLayout.getHeight() + AndroidUtilities.dp(4.0f));
                        this.giftPremiumSubtitleLayout.draw(canvas);
                        canvas.restore();
                        height += this.giftPremiumSubtitleLayout.getHeight() + AndroidUtilities.dp(10.0f);
                    }
                    dp2 = height + AndroidUtilities.dp(messageObject.type == 25 ? 6.0f : 0.0f);
                } else {
                    dp2 = dp - AndroidUtilities.dp(4.0f);
                }
                canvas.restore();
                float dp7 = dp2 + AndroidUtilities.dp(4.0f);
                if (messageObject.type == 18) {
                    dp7 += AndroidUtilities.dp(f);
                }
                canvas.save();
                canvas.translate(dp6, dp7);
                if (messageObject.type != 22) {
                    if (this.radialProgress.getTransitionProgress() != 1.0f || this.radialProgress.getIcon() != 4) {
                        f4 = dp6;
                        if (this.settingWallpaperLayout == null) {
                            TextPaint textPaint5 = new TextPaint();
                            this.settingWallpaperPaint = textPaint5;
                            textPaint5.setTextSize(AndroidUtilities.dp(13.0f));
                            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString(R.string.ActionSettingWallpaper));
                            int indexOf = spannableStringBuilder.toString().indexOf("...");
                            if (indexOf < 0) {
                                indexOf = spannableStringBuilder.toString().indexOf("…");
                                i2 = 1;
                            } else {
                                i2 = 3;
                            }
                            if (indexOf >= 0) {
                                SpannableString spannableString = new SpannableString("…");
                                UploadingDotsSpannable uploadingDotsSpannable = new UploadingDotsSpannable();
                                uploadingDotsSpannable.fixTop = true;
                                uploadingDotsSpannable.setParent(this, false);
                                spannableString.setSpan(uploadingDotsSpannable, 0, spannableString.length(), 33);
                                spannableStringBuilder.replace(indexOf, i2 + indexOf, (CharSequence) spannableString);
                            }
                            TextPaint textPaint6 = this.settingWallpaperPaint;
                            TextLayout textLayout2 = this.giftPremiumText;
                            this.settingWallpaperLayout = new StaticLayout(spannableStringBuilder, textPaint6, textLayout2 == null ? 1 : textLayout2.width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                        }
                        float uploadingInfoProgress2 = getUploadingInfoProgress(messageObject);
                        if (this.settingWallpaperProgressTextLayout == null || this.settingWallpaperProgress != uploadingInfoProgress2) {
                            this.settingWallpaperProgress = uploadingInfoProgress2;
                            String str = ((int) (uploadingInfoProgress2 * 100.0f)) + "%";
                            TextPaint textPaint7 = this.giftTextPaint;
                            TextLayout textLayout3 = this.giftPremiumText;
                            this.settingWallpaperProgressTextLayout = new StaticLayout(str, textPaint7, textLayout3 == null ? 1 : textLayout3.width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                        }
                        this.settingWallpaperPaint.setColor(this.giftTextPaint.getColor());
                        if (this.radialProgress.getIcon() == 4) {
                            float transitionProgress = this.radialProgress.getTransitionProgress();
                            int color = this.giftTextPaint.getColor();
                            float f17 = 1.0f - transitionProgress;
                            this.settingWallpaperPaint.setAlpha((int) (Color.alpha(color) * f17));
                            this.giftTextPaint.setAlpha((int) (Color.alpha(color) * transitionProgress));
                            TextPaint textPaint8 = this.giftTextPaint;
                            textPaint8.linkColor = textPaint8.getColor();
                            if (this.giftPremiumText != null) {
                                float f18 = (transitionProgress * 0.2f) + 0.8f;
                                canvas.save();
                                TextLayout textLayout4 = this.giftPremiumText;
                                canvas.scale(f18, f18, textLayout4.width / 2.0f, textLayout4.layout.getHeight() / 2.0f);
                                TextLayout textLayout5 = this.giftPremiumText;
                                canvas.translate((textLayout5.width - textLayout5.layout.getWidth()) / 2.0f, 0.0f);
                                this.giftPremiumText.x = ((r1.width - r1.layout.getWidth()) / 2.0f) + f4;
                                this.giftPremiumText.y = dp7;
                                int color2 = this.giftTextPaint.getColor();
                                TextLayout textLayout6 = this.giftPremiumText;
                                f3 = f4;
                                i = color;
                                SpoilerEffect.renderWithRipple(this, false, color2, 0, textLayout6.patchedLayout, 1, textLayout6.layout, textLayout6.spoilers, canvas, false);
                                TextLayout textLayout7 = this.giftPremiumText;
                                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, textLayout7.layout, textLayout7.emoji, 0.0f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(this.giftTextPaint.getColor()));
                                canvas.restore();
                            } else {
                                i = color;
                                f3 = f4;
                            }
                            this.giftTextPaint.setAlpha((int) (Color.alpha(i) * f17));
                            TextPaint textPaint9 = this.giftTextPaint;
                            textPaint9.linkColor = textPaint9.getColor();
                            float f19 = (f17 * 0.2f) + 0.8f;
                            canvas.save();
                            canvas.scale(f19, f19, this.settingWallpaperLayout.getWidth() / 2.0f, this.settingWallpaperLayout.getHeight() / 2.0f);
                            SpoilerEffect.layoutDrawMaybe(this.settingWallpaperLayout, canvas);
                            canvas.restore();
                            canvas.save();
                            canvas.translate(0.0f, this.settingWallpaperLayout.getHeight() + AndroidUtilities.dp(4.0f));
                            canvas.scale(f19, f19, this.settingWallpaperProgressTextLayout.getWidth() / 2.0f, this.settingWallpaperProgressTextLayout.getHeight() / 2.0f);
                            SpoilerEffect.layoutDrawMaybe(this.settingWallpaperProgressTextLayout, canvas);
                            canvas.restore();
                            int i8 = i;
                            this.giftTextPaint.setColor(i8);
                            this.giftTextPaint.linkColor = i8;
                        } else {
                            this.settingWallpaperLayout.draw(canvas);
                            canvas.save();
                            canvas.translate(0.0f, this.settingWallpaperLayout.getHeight() + AndroidUtilities.dp(4.0f));
                            SpoilerEffect.layoutDrawMaybe(this.settingWallpaperProgressTextLayout, canvas);
                        }
                    } else if (this.giftPremiumText != null) {
                        canvas.save();
                        TextLayout textLayout8 = this.giftPremiumText;
                        canvas.translate((textLayout8.width - textLayout8.layout.getWidth()) / f, 0.0f);
                        this.giftPremiumText.x = ((r1.width - r1.layout.getWidth()) / f) + dp6;
                        this.giftPremiumText.y = dp7;
                        int color3 = this.giftTextPaint.getColor();
                        TextLayout textLayout9 = this.giftPremiumText;
                        f4 = dp6;
                        SpoilerEffect.renderWithRipple(this, false, color3, 0, textLayout9.patchedLayout, 1, textLayout9.layout, textLayout9.spoilers, canvas, false);
                        TextLayout textLayout10 = this.giftPremiumText;
                        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, textLayout10.layout, textLayout10.emoji, 0.0f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(this.giftTextPaint.getColor()));
                    } else {
                        f4 = dp6;
                        f6 = 1.0f;
                        f5 = 0.0f;
                        canvas.restore();
                        if (this.giftPremiumTitleLayout == null) {
                            AndroidUtilities.dp(8.0f);
                        }
                        textLayout = this.giftPremiumText;
                        if (textLayout != null) {
                            AndroidUtilities.lerp(this.giftPremiumTextCollapsedHeight, textLayout.layout.getHeight(), f10);
                        }
                        staticLayout = this.giftPremiumButtonLayout;
                        if (staticLayout != null) {
                            staticLayout.getHeight();
                        }
                        getHeight();
                        AndroidUtilities.dp(8.0f);
                        resourcesProvider = this.themeDelegate;
                        if (resourcesProvider != null) {
                            resourcesProvider.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
                        } else {
                            Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
                        }
                        float scale = this.bounce.getScale(0.02f);
                        canvas.save();
                        canvas.scale(scale, scale, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                        if (this.giftPremiumButtonLayout != null) {
                            canvas.drawRoundRect(this.giftButtonRect, AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), getThemedPaint("paintChatActionBackgroundSelected"));
                            if (hasGradientService()) {
                                canvas.drawRoundRect(this.giftButtonRect, AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), getThemedPaint("paintChatActionBackgroundDarken"));
                            }
                            if (this.dimAmount > f5) {
                                canvas.drawRoundRect(this.giftButtonRect, AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), this.dimPaint);
                            }
                            if (getMessageObject().type == 21 || getMessageObject().type == 22 || getMessageObject().type == 24) {
                                invalidate();
                            } else {
                                this.starsPath.rewind();
                                this.starsPath.addRoundRect(this.giftButtonRect, AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), Path.Direction.CW);
                                canvas.save();
                                canvas.clipPath(this.starsPath);
                                this.starParticlesDrawable.onDraw(canvas);
                                if (!this.starParticlesDrawable.paused) {
                                    invalidate();
                                }
                                canvas.restore();
                            }
                        }
                        z = messageObject.settingAvatar;
                        if (z) {
                            float f20 = this.progressToProgress;
                            if (f20 != f6) {
                                this.progressToProgress = f20 + 0.10666667f;
                                clamp = Utilities.clamp(this.progressToProgress, f6, f5);
                                this.progressToProgress = clamp;
                                if (clamp != f5) {
                                    if (this.progressView == null) {
                                        this.progressView = new RadialProgressView(getContext());
                                    }
                                    int dp8 = AndroidUtilities.dp(f2);
                                    canvas.save();
                                    float f21 = this.progressToProgress;
                                    canvas.scale(f21, f21, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                                    this.progressView.setSize(dp8);
                                    this.progressView.setProgressColor(Theme.getColor(Theme.key_chat_serviceText));
                                    this.progressView.draw(canvas, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                                    canvas.restore();
                                }
                                if (this.progressToProgress != f6 && this.giftPremiumButtonLayout != null) {
                                    canvas.save();
                                    float f22 = f6 - this.progressToProgress;
                                    canvas.scale(f22, f22, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                                    canvas.translate(f4, this.giftButtonRect.top + AndroidUtilities.dp(8.0f));
                                    canvas.translate(((this.giftRectSize - AndroidUtilities.dp(f2)) - this.giftPremiumButtonLayout.getWidth()) / 2.0f, f5);
                                    this.giftPremiumButtonLayout.draw(canvas);
                                    canvas.restore();
                                }
                                if (messageObject.flickerLoading) {
                                    LoadingDrawable loadingDrawable = this.loadingDrawable;
                                    if (loadingDrawable != null) {
                                        loadingDrawable.setBounds(this.giftButtonRect);
                                        this.loadingDrawable.setRadiiDp(16.0f);
                                        this.loadingDrawable.disappear();
                                        this.loadingDrawable.draw(canvas);
                                        if (this.loadingDrawable.isDisappeared()) {
                                            this.loadingDrawable.reset();
                                        }
                                    }
                                } else {
                                    if (this.loadingDrawable == null) {
                                        LoadingDrawable loadingDrawable2 = new LoadingDrawable(this.themeDelegate);
                                        this.loadingDrawable = loadingDrawable2;
                                        loadingDrawable2.setGradientScale(2.0f);
                                        this.loadingDrawable.setAppearByGradient(true);
                                        this.loadingDrawable.setColors(Theme.multAlpha(-1, 0.08f), Theme.multAlpha(-1, 0.2f), Theme.multAlpha(-1, 0.2f), Theme.multAlpha(-1, 0.7f));
                                        this.loadingDrawable.strokePaint.setStrokeWidth(AndroidUtilities.dp(f6));
                                    }
                                    this.loadingDrawable.resetDisappear();
                                    this.loadingDrawable.setBounds(this.giftButtonRect);
                                    this.loadingDrawable.setRadiiDp(16.0f);
                                    this.loadingDrawable.draw(canvas);
                                }
                                canvas.restore();
                                if (this.backgroundRect != null && this.giftRibbonPath != null && this.giftRibbonText != null) {
                                    themedPaint = getThemedPaint("paintChatActionBackground");
                                    Paint themedPaint2 = getThemedPaint("paintChatActionBackgroundDarken");
                                    float dp9 = (this.backgroundRect.right - AndroidUtilities.dp(65.0f)) + AndroidUtilities.dp(2.0f);
                                    float dp10 = this.backgroundRect.top - AndroidUtilities.dp(2.0f);
                                    resourcesProvider2 = this.themeDelegate;
                                    if (resourcesProvider2 == null) {
                                        resourcesProvider2.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX + dp9, this.viewTop + AndroidUtilities.dp(4.0f) + dp10);
                                    } else {
                                        Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX + dp9, this.viewTop + AndroidUtilities.dp(4.0f) + dp10);
                                    }
                                    canvas.save();
                                    canvas.translate(dp9, dp10);
                                    ColorFilter colorFilter = themedPaint.getColorFilter();
                                    PathEffect pathEffect = themedPaint.getPathEffect();
                                    Theme.ResourcesProvider resourcesProvider3 = this.themeDelegate;
                                    isDark = resourcesProvider3 == null ? resourcesProvider3.isDark() : Theme.isCurrentThemeDark();
                                    if (this.giftRibbonPaintFilter != null || this.giftRibbonPaintFilterDark != isDark) {
                                        colorMatrix = new ColorMatrix();
                                        if ((themedPaint.getColorFilter() instanceof ColorMatrixColorFilter) && Build.VERSION.SDK_INT >= 26) {
                                            ((ColorMatrixColorFilter) themedPaint.getColorFilter()).getColorMatrix(colorMatrix);
                                        }
                                        AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, !isDark ? 0.1f : -0.08f);
                                        AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, !isDark ? 0.15f : 0.1f);
                                        this.giftRibbonPaintFilter = new ColorMatrixColorFilter(colorMatrix);
                                        this.giftRibbonPaintFilterDark = isDark;
                                    }
                                    themedPaint.setColorFilter(this.giftRibbonPaintFilter);
                                    themedPaint.setPathEffect(this.giftRibbonPaintEffect);
                                    canvas.drawPath(this.giftRibbonPath, themedPaint);
                                    themedPaint.setColorFilter(colorFilter);
                                    themedPaint.setPathEffect(pathEffect);
                                    if (hasGradientService()) {
                                        PathEffect pathEffect2 = themedPaint2.getPathEffect();
                                        themedPaint2.setPathEffect(this.giftRibbonPaintEffect);
                                        canvas.drawPath(this.giftRibbonPath, themedPaint2);
                                        themedPaint2.setPathEffect(pathEffect2);
                                    }
                                    canvas.rotate(45.0f, AndroidUtilities.dp(40.43f), AndroidUtilities.dp(24.56f));
                                    this.giftRibbonText.draw(canvas, AndroidUtilities.dp(40.43f) - (this.giftRibbonText.getCurrentWidth() / 2.0f), AndroidUtilities.dp(26.0f), -1, 1.0f);
                                    canvas.restore();
                                }
                            }
                        }
                        if (!z) {
                            float f23 = this.progressToProgress;
                            if (f23 != f5) {
                                this.progressToProgress = f23 - 0.10666667f;
                            }
                        }
                        clamp = Utilities.clamp(this.progressToProgress, f6, f5);
                        this.progressToProgress = clamp;
                        if (clamp != f5) {
                        }
                        if (this.progressToProgress != f6) {
                            canvas.save();
                            float f222 = f6 - this.progressToProgress;
                            canvas.scale(f222, f222, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                            canvas.translate(f4, this.giftButtonRect.top + AndroidUtilities.dp(8.0f));
                            canvas.translate(((this.giftRectSize - AndroidUtilities.dp(f2)) - this.giftPremiumButtonLayout.getWidth()) / 2.0f, f5);
                            this.giftPremiumButtonLayout.draw(canvas);
                            canvas.restore();
                        }
                        if (messageObject.flickerLoading) {
                        }
                        canvas.restore();
                        if (this.backgroundRect != null) {
                            themedPaint = getThemedPaint("paintChatActionBackground");
                            Paint themedPaint22 = getThemedPaint("paintChatActionBackgroundDarken");
                            float dp92 = (this.backgroundRect.right - AndroidUtilities.dp(65.0f)) + AndroidUtilities.dp(2.0f);
                            float dp102 = this.backgroundRect.top - AndroidUtilities.dp(2.0f);
                            resourcesProvider2 = this.themeDelegate;
                            if (resourcesProvider2 == null) {
                            }
                            canvas.save();
                            canvas.translate(dp92, dp102);
                            ColorFilter colorFilter2 = themedPaint.getColorFilter();
                            PathEffect pathEffect3 = themedPaint.getPathEffect();
                            Theme.ResourcesProvider resourcesProvider32 = this.themeDelegate;
                            if (resourcesProvider32 == null) {
                            }
                            if (this.giftRibbonPaintFilter != null) {
                            }
                            colorMatrix = new ColorMatrix();
                            if (themedPaint.getColorFilter() instanceof ColorMatrixColorFilter) {
                                ((ColorMatrixColorFilter) themedPaint.getColorFilter()).getColorMatrix(colorMatrix);
                            }
                            AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, !isDark ? 0.1f : -0.08f);
                            AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, !isDark ? 0.15f : 0.1f);
                            this.giftRibbonPaintFilter = new ColorMatrixColorFilter(colorMatrix);
                            this.giftRibbonPaintFilterDark = isDark;
                            themedPaint.setColorFilter(this.giftRibbonPaintFilter);
                            themedPaint.setPathEffect(this.giftRibbonPaintEffect);
                            canvas.drawPath(this.giftRibbonPath, themedPaint);
                            themedPaint.setColorFilter(colorFilter2);
                            themedPaint.setPathEffect(pathEffect3);
                            if (hasGradientService()) {
                            }
                            canvas.rotate(45.0f, AndroidUtilities.dp(40.43f), AndroidUtilities.dp(24.56f));
                            this.giftRibbonText.draw(canvas, AndroidUtilities.dp(40.43f) - (this.giftRibbonText.getCurrentWidth() / 2.0f), AndroidUtilities.dp(26.0f), -1, 1.0f);
                            canvas.restore();
                        }
                    }
                    canvas.restore();
                    f6 = 1.0f;
                    f5 = 0.0f;
                    canvas.restore();
                    if (this.giftPremiumTitleLayout == null) {
                    }
                    textLayout = this.giftPremiumText;
                    if (textLayout != null) {
                    }
                    staticLayout = this.giftPremiumButtonLayout;
                    if (staticLayout != null) {
                    }
                    getHeight();
                    AndroidUtilities.dp(8.0f);
                    resourcesProvider = this.themeDelegate;
                    if (resourcesProvider != null) {
                    }
                    float scale2 = this.bounce.getScale(0.02f);
                    canvas.save();
                    canvas.scale(scale2, scale2, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                    if (this.giftPremiumButtonLayout != null) {
                    }
                    z = messageObject.settingAvatar;
                    if (z) {
                    }
                    if (!z) {
                    }
                    clamp = Utilities.clamp(this.progressToProgress, f6, f5);
                    this.progressToProgress = clamp;
                    if (clamp != f5) {
                    }
                    if (this.progressToProgress != f6) {
                    }
                    if (messageObject.flickerLoading) {
                    }
                    canvas.restore();
                    if (this.backgroundRect != null) {
                    }
                } else {
                    f3 = dp6;
                    TextLayout textLayout11 = this.giftPremiumText;
                    if (textLayout11 != null) {
                        float height2 = textLayout11.layout.getHeight();
                        if (f10 < 1.0f) {
                            height2 = AndroidUtilities.lerp(this.giftPremiumTextCollapsedHeight, height2, f10);
                            RectF rectF2 = AndroidUtilities.rectTmp;
                            rectF2.set(0.0f, -AndroidUtilities.dp(20.0f), getWidth(), height2);
                            canvas.saveLayerAlpha(rectF2, NotificationCenter.newLocationAvailable, 31);
                        } else {
                            canvas.save();
                        }
                        float f24 = height2;
                        canvas.translate(((this.giftRectSize - AndroidUtilities.dp(f2)) - this.giftPremiumText.layout.getWidth()) / 2.0f, 0.0f);
                        this.giftPremiumText.x = f3 + (((this.giftRectSize - AndroidUtilities.dp(f2)) - this.giftPremiumText.layout.getWidth()) / 2.0f);
                        TextLayout textLayout12 = this.giftPremiumText;
                        textLayout12.y = dp7;
                        int color4 = textLayout12.paint.getColor();
                        TextLayout textLayout13 = this.giftPremiumText;
                        f4 = f3;
                        f5 = 0.0f;
                        SpoilerEffect.renderWithRipple(this, false, color4, 0, textLayout13.patchedLayout, 1, textLayout13.layout, textLayout13.spoilers, canvas, false);
                        TextLayout textLayout14 = this.giftPremiumText;
                        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, textLayout14.layout, textLayout14.emoji, 0.0f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(this.giftTextPaint.getColor()));
                        f6 = 1.0f;
                        if (f10 < 1.0f && this.giftPremiumTextMore != null) {
                            canvas.save();
                            if (this.giftPremiumTextClip == null) {
                                this.giftPremiumTextClip = new GradientClip();
                            }
                            canvas.translate((-((this.giftRectSize - AndroidUtilities.dp(f2)) - this.giftPremiumText.layout.getWidth())) / 2.0f, 0.0f);
                            RectF rectF3 = AndroidUtilities.rectTmp;
                            rectF3.set((this.giftPremiumTextMoreX - this.giftPremiumTextMore.getCurrentWidth()) + AndroidUtilities.dp(8.0f), (this.giftPremiumTextMoreY - this.giftPremiumTextMoreH) - AndroidUtilities.dp(6.0f), this.giftPremiumTextMoreX + AndroidUtilities.dp(6.0f), this.giftPremiumTextMoreY);
                            float f25 = 1.0f - f10;
                            this.giftPremiumTextClip.clipOut(canvas, rectF3, f25);
                            rectF3.set((this.giftPremiumTextMoreX - this.giftPremiumTextMore.getCurrentWidth()) - AndroidUtilities.dp(f2), (this.giftPremiumTextMoreY - this.giftPremiumTextMoreH) - AndroidUtilities.dp(6.0f), (this.giftPremiumTextMoreX - this.giftPremiumTextMore.getCurrentWidth()) + AndroidUtilities.dp(8.0f), this.giftPremiumTextMoreY);
                            this.giftPremiumTextClip.draw(canvas, rectF3, 2, f25);
                            rectF3.set(0.0f, f24 - AndroidUtilities.dp(12.0f), getWidth(), f24);
                            this.giftPremiumTextClip.draw(canvas, rectF3, 3, f25 * 4.0f * (1.0f - f25));
                            canvas.restore();
                        }
                        canvas.restore();
                        if (f10 < 1.0f && (text = this.giftPremiumTextMore) != null) {
                            text.draw(canvas, AndroidUtilities.dp(5.0f) + (this.giftPremiumTextMoreX - text.getCurrentWidth()), (this.giftPremiumTextMoreY - (this.giftPremiumTextMoreH / 2.0f)) - AndroidUtilities.dp(1.0f), this.giftPremiumText.paint.getColor(), 1.0f - f10);
                        }
                        canvas.restore();
                        if (this.giftPremiumTitleLayout == null) {
                        }
                        textLayout = this.giftPremiumText;
                        if (textLayout != null) {
                        }
                        staticLayout = this.giftPremiumButtonLayout;
                        if (staticLayout != null) {
                        }
                        getHeight();
                        AndroidUtilities.dp(8.0f);
                        resourcesProvider = this.themeDelegate;
                        if (resourcesProvider != null) {
                        }
                        float scale22 = this.bounce.getScale(0.02f);
                        canvas.save();
                        canvas.scale(scale22, scale22, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                        if (this.giftPremiumButtonLayout != null) {
                        }
                        z = messageObject.settingAvatar;
                        if (z) {
                        }
                        if (!z) {
                        }
                        clamp = Utilities.clamp(this.progressToProgress, f6, f5);
                        this.progressToProgress = clamp;
                        if (clamp != f5) {
                        }
                        if (this.progressToProgress != f6) {
                        }
                        if (messageObject.flickerLoading) {
                        }
                        canvas.restore();
                        if (this.backgroundRect != null) {
                        }
                    }
                }
                f4 = f3;
                f6 = 1.0f;
                f5 = 0.0f;
                canvas.restore();
                if (this.giftPremiumTitleLayout == null) {
                }
                textLayout = this.giftPremiumText;
                if (textLayout != null) {
                }
                staticLayout = this.giftPremiumButtonLayout;
                if (staticLayout != null) {
                }
                getHeight();
                AndroidUtilities.dp(8.0f);
                resourcesProvider = this.themeDelegate;
                if (resourcesProvider != null) {
                }
                float scale222 = this.bounce.getScale(0.02f);
                canvas.save();
                canvas.scale(scale222, scale222, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                if (this.giftPremiumButtonLayout != null) {
                }
                z = messageObject.settingAvatar;
                if (z) {
                }
                if (!z) {
                }
                clamp = Utilities.clamp(this.progressToProgress, f6, f5);
                this.progressToProgress = clamp;
                if (clamp != f5) {
                }
                if (this.progressToProgress != f6) {
                }
                if (messageObject.flickerLoading) {
                }
                canvas.restore();
                if (this.backgroundRect != null) {
                }
            }
            drawReactions(canvas, false);
            this.transitionParams.recordDrawingState();
        }
        if (this.textPaint != null) {
        }
        f = 2.0f;
        f2 = 16.0f;
        if (!this.starGiftLayout.has()) {
            canvas.save();
            float dp62 = ((this.previousWidth - this.giftRectSize) / f) + AndroidUtilities.dp(8.0f);
            if (isNewStyleButtonLayout()) {
            }
            canvas.translate(dp62, dp);
            if (this.giftPremiumTitleLayout == null) {
            }
            canvas.restore();
            float dp72 = dp2 + AndroidUtilities.dp(4.0f);
            if (messageObject.type == 18) {
            }
            canvas.save();
            canvas.translate(dp62, dp72);
            if (messageObject.type != 22) {
            }
            f4 = f3;
            f6 = 1.0f;
            f5 = 0.0f;
            canvas.restore();
            if (this.giftPremiumTitleLayout == null) {
            }
            textLayout = this.giftPremiumText;
            if (textLayout != null) {
            }
            staticLayout = this.giftPremiumButtonLayout;
            if (staticLayout != null) {
            }
            getHeight();
            AndroidUtilities.dp(8.0f);
            resourcesProvider = this.themeDelegate;
            if (resourcesProvider != null) {
            }
            float scale2222 = this.bounce.getScale(0.02f);
            canvas.save();
            canvas.scale(scale2222, scale2222, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
            if (this.giftPremiumButtonLayout != null) {
            }
            z = messageObject.settingAvatar;
            if (z) {
            }
            if (!z) {
            }
            clamp = Utilities.clamp(this.progressToProgress, f6, f5);
            this.progressToProgress = clamp;
            if (clamp != f5) {
            }
            if (this.progressToProgress != f6) {
            }
            if (messageObject.flickerLoading) {
            }
            canvas.restore();
            if (this.backgroundRect != null) {
            }
        }
        drawReactions(canvas, false);
        this.transitionParams.recordDrawingState();
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        MessageObject messageObject = this.currentMessageObject;
        if (TextUtils.isEmpty(this.customText) && messageObject == null) {
            return;
        }
        if (this.accessibilityText == null) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(!TextUtils.isEmpty(this.customText) ? this.customText : messageObject.messageText);
            for (final CharacterStyle characterStyle : (CharacterStyle[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ClickableSpan.class)) {
                int spanStart = spannableStringBuilder.getSpanStart(characterStyle);
                int spanEnd = spannableStringBuilder.getSpanEnd(characterStyle);
                spannableStringBuilder.removeSpan(characterStyle);
                spannableStringBuilder.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Cells.ChatActionCell.1
                    @Override // android.text.style.ClickableSpan
                    public void onClick(View view) {
                        if (ChatActionCell.this.delegate != null) {
                            ChatActionCell.this.openLink(characterStyle);
                        }
                    }
                }, spanStart, spanEnd, 33);
            }
            this.accessibilityText = spannableStringBuilder;
        }
        if (Build.VERSION.SDK_INT < 24) {
            accessibilityNodeInfo.setContentDescription(this.accessibilityText.toString());
        } else {
            accessibilityNodeInfo.setText(this.accessibilityText);
        }
        accessibilityNodeInfo.setEnabled(true);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        View view = this.rippleView;
        RectF rectF = this.giftButtonRect;
        view.layout((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
    }

    @Override // org.telegram.ui.Cells.BaseCell
    protected boolean onLongPress() {
        ChatActionCellDelegate chatActionCellDelegate = this.delegate;
        if (chatActionCellDelegate != null) {
            return chatActionCellDelegate.didLongPress(this, this.lastTouchX, this.lastTouchY);
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x00fa, code lost:
    
        if (r3.isEmpty == false) goto L134;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x03dd, code lost:
    
        r3.totalHeight = r3.height + org.telegram.messenger.AndroidUtilities.dp(8.0f);
        r7 = r7 + r20.reactionsLayoutInBubble.totalHeight;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x03db, code lost:
    
        if (r3.isEmpty == false) goto L134;
     */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00de  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x03f3  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00fe  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int i, int i2) {
        int i3;
        float dp;
        float dp2;
        int i4;
        float f;
        int dp3;
        int i5;
        TLRPC.Message message;
        int dp4;
        ReactionsLayoutInBubble reactionsLayoutInBubble;
        ReactionsLayoutInBubble reactionsLayoutInBubble2;
        int i6;
        int dp5;
        int i7;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null && this.customText == null) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), this.textHeight + AndroidUtilities.dp(14.0f));
            return;
        }
        int i8 = 0;
        if (isButtonLayout(messageObject)) {
            this.giftRectSize = Math.min((int) (AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() * 0.6f : (AndroidUtilities.displaySize.x * 0.62f) - AndroidUtilities.dp(34.0f)), ((AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.statusBarHeight) - AndroidUtilities.dp(64.0f));
            if (!AndroidUtilities.isTablet() && ((i7 = messageObject.type) == 18 || i7 == 30)) {
                this.giftRectSize = (int) (this.giftRectSize * 1.2f);
            }
            this.stickerSize = this.giftRectSize - AndroidUtilities.dp(106.0f);
            if (isNewStyleButtonLayout()) {
                this.imageReceiver.setRoundRadius(this.stickerSize / 2);
            } else {
                this.imageReceiver.setRoundRadius(0);
            }
        }
        int max = Math.max(AndroidUtilities.dp(30.0f), View.MeasureSpec.getSize(i));
        if (this.previousWidth != max) {
            this.wasLayout = true;
            this.previousWidth = max;
            buildLayout();
        }
        if (messageObject != null) {
            if (messageObject.type == 11) {
                i6 = AndroidUtilities.roundMessageSize;
                dp5 = AndroidUtilities.dp(10.0f);
            } else if (isButtonLayout(messageObject)) {
                i6 = this.giftRectSize;
                dp5 = AndroidUtilities.dp(12.0f);
            }
            i3 = i6 + dp5;
            if (!this.starGiftLayout.has()) {
                i8 = this.textY + this.textHeight + AndroidUtilities.dp(10.0f) + ((int) this.starGiftLayout.getHeight()) + AndroidUtilities.dp(14.0f);
                reactionsLayoutInBubble = this.reactionsLayoutInBubble;
            } else if (isButtonLayout(messageObject)) {
                boolean isGiftChannel = isGiftChannel(messageObject);
                int imageSize = getImageSize(messageObject);
                if (isNewStyleButtonLayout()) {
                    int dp6 = this.textY + this.textHeight + AndroidUtilities.dp(4.0f) + (AndroidUtilities.dp(16.0f) * 2) + imageSize;
                    TextLayout textLayout = this.giftPremiumText;
                    dp = dp6 + (textLayout == null ? 0 : textLayout.layout.getHeight() + AndroidUtilities.dp(4.0f));
                } else {
                    dp = this.textY + this.textHeight + (this.giftRectSize * 0.075f) + imageSize + AndroidUtilities.dp(4.0f) + (this.giftPremiumText == null ? 0 : r14.layout.getHeight() + AndroidUtilities.dp(4.0f));
                }
                this.giftPremiumAdditionalHeight = 0;
                if (this.giftPremiumTitleLayout != null) {
                    float height = dp + r14.getHeight();
                    if (this.giftPremiumTitleLayout.getLineCount() > 1) {
                        this.giftPremiumAdditionalHeight += this.giftPremiumTitleLayout.getHeight() - this.giftPremiumTitleLayout.getLineTop(1);
                    }
                    dp2 = height + AndroidUtilities.dp(isGiftChannel ? 6.0f : 0.0f);
                    if (this.giftPremiumSubtitleLayout != null) {
                        dp2 += r3.getHeight() + AndroidUtilities.dp(9.0f);
                    }
                } else {
                    dp2 = dp - AndroidUtilities.dp(12.0f);
                    this.giftPremiumAdditionalHeight -= AndroidUtilities.dp(30.0f);
                }
                TextLayout textLayout2 = this.giftPremiumText;
                int height2 = textLayout2 == null ? 0 : textLayout2.layout.getHeight();
                if (this.giftPremiumText == null) {
                    this.giftPremiumAdditionalHeight = 0;
                } else {
                    if (this.giftPremiumSubtitleLayout != null) {
                        i4 = this.giftPremiumAdditionalHeight;
                        dp3 = AndroidUtilities.dp(10.0f) + height2;
                    } else {
                        MessageObject messageObject2 = this.currentMessageObject;
                        if (messageObject2.type == 18 || messageObject2.isStarGiftAction()) {
                            i4 = this.giftPremiumAdditionalHeight;
                            f = this.giftPremiumButtonLayout == null ? 0.0f : 10.0f;
                        } else if (this.currentMessageObject.type == 30) {
                            i4 = this.giftPremiumAdditionalHeight;
                            f = 20.0f;
                        } else if (this.giftPremiumTextCollapsed) {
                            i5 = this.giftPremiumAdditionalHeight + height2;
                            this.giftPremiumAdditionalHeight = i5;
                        } else if (this.giftPremiumText.layout.getLineCount() > 2) {
                            i4 = this.giftPremiumAdditionalHeight;
                            dp3 = ((this.giftPremiumText.layout.getLineBottom(0) - this.giftPremiumText.layout.getLineTop(0)) * this.giftPremiumText.layout.getLineCount()) - 2;
                        }
                        dp3 = height2 - AndroidUtilities.dp(f);
                    }
                    i5 = i4 + dp3;
                    this.giftPremiumAdditionalHeight = i5;
                }
                int dp7 = this.giftPremiumAdditionalHeight - AndroidUtilities.dp(isGiftChannel ? 14.0f : 0.0f);
                this.giftPremiumAdditionalHeight = dp7;
                i3 += dp7;
                int dp8 = this.textHeight + i3 + AndroidUtilities.dp(14.0f);
                if (this.giftPremiumButtonLayout != null) {
                    float height3 = dp2 + ((((dp8 - dp2) - r5.getHeight()) - AndroidUtilities.dp(8.0f)) / 2.0f);
                    if (this.currentMessageObject.isStarGiftAction()) {
                        height3 += AndroidUtilities.dp(4.0f);
                    }
                    float f2 = (this.previousWidth - this.giftPremiumButtonWidth) / 2.0f;
                    this.giftButtonRect.set(f2 - AndroidUtilities.dp(18.0f), height3 - AndroidUtilities.dp(8.0f), f2 + this.giftPremiumButtonWidth + AndroidUtilities.dp(18.0f), height3 + (this.giftPremiumButtonLayout != null ? r14.getHeight() : 0) + AndroidUtilities.dp(8.0f));
                } else {
                    i3 -= AndroidUtilities.dp(40.0f);
                    this.giftPremiumAdditionalHeight -= AndroidUtilities.dp(40.0f);
                    MessageObject messageObject3 = this.currentMessageObject;
                    if (messageObject3 != null && (message = messageObject3.messageOwner) != null && (message.action instanceof TLRPC.TL_messageActionStarGift)) {
                        i3 -= AndroidUtilities.dp(8.0f);
                        this.giftPremiumAdditionalHeight -= AndroidUtilities.dp(8.0f);
                    }
                }
                int measuredWidth = getMeasuredWidth() << (getMeasuredHeight() + 16);
                this.starParticlesDrawable.rect.set(this.giftButtonRect);
                this.starParticlesDrawable.rect2.set(this.giftButtonRect);
                if (this.starsSize != measuredWidth) {
                    this.starsSize = measuredWidth;
                    this.starParticlesDrawable.resetPositions();
                }
                if (isNewStyleButtonLayout()) {
                    int dp9 = this.textY + this.textHeight + AndroidUtilities.dp(4.0f);
                    this.backgroundRectHeight = 0;
                    int dp10 = (AndroidUtilities.dp(16.0f) * 2) + imageSize;
                    this.backgroundRectHeight = dp10;
                    StaticLayout staticLayout = this.giftPremiumSubtitleLayout;
                    if (staticLayout != null) {
                        this.backgroundRectHeight = dp10 + staticLayout.getHeight() + AndroidUtilities.dp(10.0f);
                    }
                    int i9 = this.backgroundRectHeight + height2;
                    this.backgroundRectHeight = i9;
                    float f3 = (this.previousWidth - this.giftPremiumButtonWidth) / 2.0f;
                    if (this.giftPremiumButtonLayout != null) {
                        this.backgroundButtonTop = i9 + dp9 + AndroidUtilities.dp(10.0f);
                        this.giftButtonRect.set(f3 - AndroidUtilities.dp(18.0f), this.backgroundButtonTop, f3 + this.giftPremiumButtonWidth + AndroidUtilities.dp(18.0f), this.backgroundButtonTop + this.giftPremiumButtonLayout.getHeight() + (AndroidUtilities.dp(8.0f) * 2));
                        dp4 = (int) (this.backgroundRectHeight + AndroidUtilities.dp(10.0f) + this.giftButtonRect.height());
                    } else {
                        this.giftButtonRect.set(f3 - AndroidUtilities.dp(18.0f), this.backgroundButtonTop, f3 + this.giftPremiumButtonWidth + AndroidUtilities.dp(18.0f), this.backgroundButtonTop + AndroidUtilities.dp(17.0f) + (AndroidUtilities.dp(8.0f) * 2));
                        dp4 = this.backgroundRectHeight + AndroidUtilities.dp(17.0f);
                    }
                    this.backgroundRectHeight = dp4;
                    int dp11 = this.backgroundRectHeight + AndroidUtilities.dp(16.0f);
                    this.backgroundRectHeight = dp11;
                    i8 = dp9 + dp11 + AndroidUtilities.dp(6.0f);
                    reactionsLayoutInBubble = this.reactionsLayoutInBubble;
                }
            }
            reactionsLayoutInBubble2 = this.reactionsLayoutInBubble;
            if (!reactionsLayoutInBubble2.isEmpty) {
                reactionsLayoutInBubble2.totalHeight = reactionsLayoutInBubble2.height + AndroidUtilities.dp(8.0f);
                i3 += this.reactionsLayoutInBubble.totalHeight;
            }
            if (messageObject == null && isNewStyleButtonLayout()) {
                setMeasuredDimension(max, i8);
            } else {
                setMeasuredDimension(max, this.textHeight + i3 + AndroidUtilities.dp(14.0f));
            }
            this.reactionsLayoutInBubble.y = getMeasuredHeight() - this.reactionsLayoutInBubble.totalHeight;
        }
        i3 = 0;
        if (!this.starGiftLayout.has()) {
        }
        reactionsLayoutInBubble2 = this.reactionsLayoutInBubble;
        if (!reactionsLayoutInBubble2.isEmpty) {
        }
        if (messageObject == null) {
        }
        setMeasuredDimension(max, this.textHeight + i3 + AndroidUtilities.dp(14.0f));
        this.reactionsLayoutInBubble.y = getMeasuredHeight() - this.reactionsLayoutInBubble.totalHeight;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String str, long j, long j2) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String str, long j, long j2, boolean z) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String str) {
        TLRPC.PhotoSize photoSize;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || messageObject.type != 11) {
            return;
        }
        int size = messageObject.photoThumbs.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                photoSize = null;
                break;
            }
            photoSize = messageObject.photoThumbs.get(i);
            if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
                break;
            } else {
                i++;
            }
        }
        this.imageReceiver.setImage(this.currentVideoLocation, ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForObject(photoSize, messageObject.photoThumbsObject), "50_50_b", this.avatarDrawable, 0L, null, messageObject, 1);
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
    }

    /* JADX WARN: Code restructure failed: missing block: B:137:0x0124, code lost:
    
        if (r5.contains(r1, r3) == false) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x01a3, code lost:
    
        if (r12.backgroundRect.contains(r1, r3) != false) goto L162;
     */
    /* JADX WARN: Removed duplicated region for block: B:109:0x03b7  */
    /* JADX WARN: Removed duplicated region for block: B:111:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ImageUpdater imageUpdater;
        TextLayout textLayout;
        TextLayout textLayout2;
        boolean z;
        StaticLayout staticLayout;
        List list;
        int i;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null) {
            return super.onTouchEvent(motionEvent);
        }
        boolean z2 = true;
        if ((this.starGiftLayout.has() && this.starGiftLayout.onTouchEvent(this.starGiftLayoutX, this.starGiftLayoutY, motionEvent)) || this.reactionsLayoutInBubble.checkTouchEvent(motionEvent)) {
            return true;
        }
        float x = motionEvent.getX();
        this.lastTouchX = x;
        float y = motionEvent.getY();
        this.lastTouchY = y;
        if (motionEvent.getAction() == 0) {
            if (this.delegate != null) {
                if ((messageObject.type == 11 || isButtonLayout(messageObject)) && this.imageReceiver.isInsideImage(x, y)) {
                    this.imagePressed = true;
                    z = true;
                } else {
                    z = false;
                }
                if (this.radialProgress.getIcon() == 4 && (((i = messageObject.type) == 21 || i == 22) && this.backgroundRect.contains(x, y))) {
                    this.imagePressed = true;
                    z = true;
                }
                TextLayout textLayout3 = this.giftPremiumText;
                if (textLayout3 != null && this.giftPremiumTextCollapsed) {
                    RectF rectF = AndroidUtilities.rectTmp;
                    float f = textLayout3.x;
                    rectF.set(f, textLayout3.y, textLayout3.layout.getWidth() + f, this.giftPremiumText.y + r10.layout.getHeight());
                    if (rectF.contains(x, y)) {
                        this.textPressed = true;
                        z = true;
                    }
                }
                if (isButtonLayout(messageObject) && this.giftPremiumButtonLayout != null && (this.giftButtonRect.contains(x, y) || (this.buttonClickableAsImage && this.backgroundRect.contains(x, y)))) {
                    View view = this.rippleView;
                    this.giftButtonPressed = true;
                    view.setPressed(true);
                    this.bounce.setPressed(true);
                    z = true;
                }
                if (z) {
                    startCheckLongPress();
                }
            }
            z = false;
        } else {
            if (motionEvent.getAction() != 2) {
                cancelCheckLongPress();
            }
            if (this.textPressed) {
                int action = motionEvent.getAction();
                if (action == 1) {
                    View view2 = this.rippleView;
                    this.textPressed = false;
                    view2.setPressed(false);
                    this.bounce.setPressed(false);
                    if (this.giftPremiumTextCollapsed && !this.giftPremiumTextUncollapsed && (textLayout2 = this.giftPremiumText) != null) {
                        int height = textLayout2.layout.getHeight() - this.giftPremiumTextCollapsedHeight;
                        this.giftPremiumTextUncollapsed = true;
                        ChatActionCellDelegate chatActionCellDelegate = this.delegate;
                        if (chatActionCellDelegate != null) {
                            chatActionCellDelegate.forceUpdate(this, false);
                            if (getParent() instanceof RecyclerListView) {
                                ((RecyclerListView) getParent()).smoothScrollBy(0, height + AndroidUtilities.dp(24.0f));
                            }
                        }
                        return true;
                    }
                } else if (action == 2) {
                    TextLayout textLayout4 = this.giftPremiumText;
                    if (textLayout4 != null && this.giftPremiumTextCollapsed) {
                        RectF rectF2 = AndroidUtilities.rectTmp;
                        float f2 = textLayout4.x;
                        rectF2.set(f2, textLayout4.y, textLayout4.layout.getWidth() + f2, this.giftPremiumText.y + r9.layout.getHeight());
                    }
                    this.textPressed = false;
                    z = true;
                } else if (action == 3) {
                    this.textPressed = false;
                    this.bounce.setPressed(false);
                }
                z = false;
            } else if (this.giftButtonPressed) {
                int action2 = motionEvent.getAction();
                if (action2 == 1) {
                    this.imagePressed = false;
                    View view3 = this.rippleView;
                    this.giftButtonPressed = false;
                    view3.setPressed(false);
                    this.bounce.setPressed(false);
                    if (this.delegate != null) {
                        int i2 = messageObject.type;
                        if (i2 == 25) {
                            playSoundEffect(0);
                            openPremiumGiftChannel();
                        } else if (i2 == 18) {
                            playSoundEffect(0);
                            openPremiumGiftPreview();
                        } else if (i2 == 30) {
                            playSoundEffect(0);
                            openStarsGiftTransaction();
                        } else if (MessagesController.getInstance(this.currentAccount).photoSuggestion.get(messageObject.messageOwner.local_id) == null) {
                            if (this.buttonClickableAsImage) {
                                this.delegate.didClickImage(this);
                            } else {
                                this.delegate.didClickButton(this);
                            }
                        }
                    }
                } else if (action2 == 2) {
                    if (isButtonLayout(messageObject)) {
                        if (!this.giftButtonRect.contains(x, y)) {
                        }
                    }
                    View view4 = this.rippleView;
                    this.giftButtonPressed = false;
                    view4.setPressed(false);
                    this.bounce.setPressed(false);
                } else if (action2 == 3) {
                    this.imagePressed = false;
                    View view42 = this.rippleView;
                    this.giftButtonPressed = false;
                    view42.setPressed(false);
                    this.bounce.setPressed(false);
                }
                z = false;
            } else {
                if (this.imagePressed) {
                    int action3 = motionEvent.getAction();
                    if (action3 == 1) {
                        this.imagePressed = false;
                        if (this.giftPremiumTextCollapsed && !this.giftPremiumTextUncollapsed && (textLayout = this.giftPremiumText) != null) {
                            int height2 = textLayout.layout.getHeight() - this.giftPremiumTextCollapsedHeight;
                            this.giftPremiumTextUncollapsed = true;
                            ChatActionCellDelegate chatActionCellDelegate2 = this.delegate;
                            if (chatActionCellDelegate2 != null) {
                                chatActionCellDelegate2.forceUpdate(this, false);
                                if (getParent() instanceof RecyclerListView) {
                                    ((RecyclerListView) getParent()).smoothScrollBy(0, height2 + AndroidUtilities.dp(16.0f));
                                }
                            }
                            return true;
                        }
                        int i3 = messageObject.type;
                        if (i3 != 25) {
                            if (i3 != 18) {
                                if (i3 != 30) {
                                    if (this.delegate != null) {
                                        if (i3 != 21 || (imageUpdater = MessagesController.getInstance(this.currentAccount).photoSuggestion.get(messageObject.messageOwner.local_id)) == null) {
                                            this.delegate.didClickImage(this);
                                            playSoundEffect(0);
                                        } else {
                                            imageUpdater.cancel();
                                        }
                                    }
                                }
                                openStarsGiftTransaction();
                            }
                            openPremiumGiftPreview();
                        }
                        openPremiumGiftChannel();
                    } else if (action3 == 2 ? !(!isNewStyleButtonLayout() ? this.imageReceiver.isInsideImage(x, y) : this.backgroundRect.contains(x, y)) : action3 == 3) {
                        this.imagePressed = false;
                    }
                }
                z = false;
            }
        }
        if (!z && (motionEvent.getAction() == 0 || ((this.pressedLink != null || this.spoilerPressed != null) && motionEvent.getAction() == 1))) {
            TextLayout textLayout5 = this.giftPremiumText;
            if (textLayout5 != null && (list = textLayout5.spoilers) != null && !list.isEmpty() && !this.isSpoilerRevealing) {
                Iterator it = this.giftPremiumText.spoilers.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    SpoilerEffect spoilerEffect = (SpoilerEffect) it.next();
                    Rect bounds = spoilerEffect.getBounds();
                    TextLayout textLayout6 = this.giftPremiumText;
                    if (bounds.contains((int) (x - textLayout6.x), (int) (y - textLayout6.y))) {
                        this.pressedLink = null;
                        if (motionEvent.getAction() == 0) {
                            this.spoilerPressed = spoilerEffect;
                        } else {
                            SpoilerEffect spoilerEffect2 = this.spoilerPressed;
                            if (spoilerEffect == spoilerEffect2) {
                                this.isSpoilerRevealing = true;
                                spoilerEffect2.setOnRippleEndCallback(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda1
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        ChatActionCell.this.lambda$onTouchEvent$2();
                                    }
                                });
                                float sqrt = (float) Math.sqrt(Math.pow(this.giftPremiumText.layout.getWidth(), 2.0d) + Math.pow(this.giftPremiumText.layout.getHeight(), 2.0d));
                                SpoilerEffect spoilerEffect3 = this.spoilerPressed;
                                TextLayout textLayout7 = this.giftPremiumText;
                                spoilerEffect3.startRipple((int) (x - textLayout7.x), (int) (y - textLayout7.y), sqrt);
                                invalidate();
                            }
                        }
                        z = true;
                    }
                }
            }
            if (!z && (staticLayout = this.textLayout) != null) {
                if (x >= this.textX) {
                    float f3 = this.textY;
                    if (y >= f3 && x <= r8 + this.textWidth && y <= r9 + this.textHeight) {
                        float f4 = y - f3;
                        float f5 = x - this.textXLeft;
                        if (!z) {
                            int lineForVertical = staticLayout.getLineForVertical((int) f4);
                            int offsetForHorizontal = this.textLayout.getOffsetForHorizontal(lineForVertical, f5);
                            float lineLeft = this.textLayout.getLineLeft(lineForVertical);
                            if (lineLeft <= f5 && lineLeft + this.textLayout.getLineWidth(lineForVertical) >= f5) {
                                CharSequence charSequence = messageObject.messageText;
                                if (charSequence instanceof Spannable) {
                                    URLSpan[] uRLSpanArr = (URLSpan[]) ((Spannable) charSequence).getSpans(offsetForHorizontal, offsetForHorizontal, URLSpan.class);
                                    if (uRLSpanArr.length != 0) {
                                        if (motionEvent.getAction() == 0) {
                                            this.pressedLink = uRLSpanArr[0];
                                        } else {
                                            URLSpan uRLSpan = uRLSpanArr[0];
                                            URLSpan uRLSpan2 = this.pressedLink;
                                            if (uRLSpan == uRLSpan2) {
                                                openLink(uRLSpan2);
                                            }
                                        }
                                        return z2 ? super.onTouchEvent(motionEvent) : z2;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.pressedLink = null;
        }
        z2 = z;
        if (z2) {
        }
    }

    public void setCustomDate(int i, boolean z, boolean z2) {
        int i2 = this.customDate;
        if (i2 == i || i2 / 3600 == i / 3600) {
            return;
        }
        String string = z ? i == 2147483646 ? LocaleController.getString("MessageScheduledUntilOnline", R.string.MessageScheduledUntilOnline) : LocaleController.formatString("MessageScheduledOn", R.string.MessageScheduledOn, LocaleController.formatDateChat(i)) : LocaleController.formatDateChat(i);
        this.customDate = i;
        CharSequence charSequence = this.customText;
        if (charSequence == null || !TextUtils.equals(string, charSequence)) {
            this.customText = string;
            this.accessibilityText = null;
            updateTextInternal(z2);
        }
    }

    public void setCustomText(CharSequence charSequence) {
        this.customText = charSequence;
        if (charSequence != null) {
            updateTextInternal(false);
        }
    }

    public void setDelegate(ChatActionCellDelegate chatActionCellDelegate) {
        this.delegate = chatActionCellDelegate;
    }

    public void setInvalidateColors(boolean z) {
        if (this.invalidateColors == z) {
            return;
        }
        this.invalidateColors = z;
        invalidate();
    }

    public void setInvalidateWithParent(View view) {
        this.invalidateWithParent = view;
    }

    public void setInvalidatesParent(boolean z) {
        this.invalidatesParent = z;
    }

    public void setMessageObject(MessageObject messageObject) {
        setMessageObject(messageObject, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:193:0x05db  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x0658  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01ac  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0245  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0251  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01d0  */
    /* JADX WARN: Type inference failed for: r6v11, types: [org.telegram.tgnet.TLRPC$messages_StickerSet] */
    /* JADX WARN: Type inference failed for: r6v36 */
    /* JADX WARN: Type inference failed for: r6v37 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setMessageObject(MessageObject messageObject, boolean z) {
        TLRPC.TL_messageReactions tL_messageReactions;
        String str;
        TLRPC.Document document;
        MessageObject messageObject2;
        String str2;
        TLRPC.Document document2;
        Object obj;
        TLRPC.PhotoSize photoSize;
        BitmapDrawable bitmapDrawable;
        String str3;
        int i;
        String str4;
        ImageReceiver imageReceiver;
        ImageLocation imageLocation;
        ImageLocation imageLocation2;
        String str5;
        long j;
        boolean z2;
        boolean z3;
        float f;
        boolean z4;
        RadialProgress2 radialProgress2;
        int i2;
        TLRPC.WallPaper wallPaper;
        TLRPC.MessageAction messageAction;
        ImageReceiver imageReceiver2;
        ImageLocation forDocument;
        StringBuilder sb;
        String str6;
        StaticLayout staticLayout;
        if (this.currentMessageObject != messageObject || (!((staticLayout = this.textLayout) == null || TextUtils.equals(staticLayout.getText(), messageObject.messageText)) || (!(this.hasReplyMessage || messageObject.replyMessageObject == null) || z || messageObject.type == 21 || messageObject.forceUpdate))) {
            if (BuildVars.DEBUG_PRIVATE_VERSION && Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
                FileLog.e(new IllegalStateException("Wrong thread!!!"));
            }
            TLRPC.VideoSize videoSize = null;
            r2 = null;
            TLRPC.PhotoSize photoSize2 = null;
            videoSize = null;
            videoSize = null;
            this.accessibilityText = null;
            MessageObject messageObject3 = this.currentMessageObject;
            boolean z5 = messageObject3 == null || messageObject3.stableId != messageObject.stableId;
            if (messageObject3 != null) {
                messageObject.playedGiftAnimation = messageObject3.playedGiftAnimation;
            }
            this.currentMessageObject = messageObject;
            messageObject.forceUpdate = false;
            this.hasReplyMessage = messageObject.replyMessageObject != null;
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            this.previousWidth = 0;
            this.isSpoilerRevealing = false;
            TextLayout textLayout = this.giftPremiumText;
            if (textLayout != null && z5) {
                textLayout.detach();
                this.giftPremiumText = null;
                this.giftPremiumTextUncollapsed = false;
            }
            if (z5 || messageObject.reactionsChanged) {
                messageObject.reactionsChanged = false;
                TLRPC.Message message = messageObject.messageOwner;
                boolean z6 = (message == null || (tL_messageReactions = message.reactions) == null || !tL_messageReactions.reactions_as_tags) ? false : true;
                if (messageObject.shouldDrawReactions()) {
                    this.reactionsLayoutInBubble.setMessage(messageObject, !messageObject.shouldDrawReactionsInLayout(), z6, this.themeDelegate);
                } else {
                    this.reactionsLayoutInBubble.setMessage(null, false, false, this.themeDelegate);
                }
            }
            boolean z7 = !z5;
            this.starGiftLayout.set(messageObject, z7);
            this.imageReceiver.setAutoRepeatCount(0);
            this.imageReceiver.clearDecorators();
            if (messageObject.type != 22) {
                this.wallpaperPreviewDrawable = null;
            }
            if (messageObject.actionDeleteGroupEventId != -1) {
                ScaleStateListAnimator.apply(this, 0.02f, 1.2f);
                this.overriddenMaxWidth = Math.max(AndroidUtilities.dp(250.0f), HintView2.cutInFancyHalf(messageObject.messageText, (TextPaint) getThemedPaint("paintChatActionText")));
                ProfileActivity.ShowDrawable findDrawable = ChannelAdminLogActivity.findDrawable(messageObject.messageText);
                if (findDrawable != null) {
                    findDrawable.setView(this);
                }
            } else {
                ScaleStateListAnimator.reset(this);
                this.overriddenMaxWidth = 0;
            }
            if (messageObject.isStoryMention()) {
                TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.messageOwner.media.user_id));
                this.avatarDrawable.setInfo(this.currentAccount, user);
                TL_stories.StoryItem storyItem = messageObject.messageOwner.media.storyItem;
                if (storyItem == null || !storyItem.noforwards) {
                    StoriesUtilities.setImage(this.imageReceiver, storyItem);
                } else {
                    this.imageReceiver.setForUserOrChat(user, this.avatarDrawable, null, true, 0, true);
                }
                this.imageReceiver.setRoundRadius((int) (this.stickerSize / 2.0f));
            } else {
                int i3 = messageObject.type;
                if (i3 == 22) {
                    if (messageObject.strippedThumb == null) {
                        int size = messageObject.photoThumbs.size();
                        for (int i4 = 0; i4 < size && !(messageObject.photoThumbs.get(i4) instanceof TLRPC.TL_photoStrippedSize); i4++) {
                        }
                    }
                    TLRPC.TL_channelAdminLogEvent tL_channelAdminLogEvent = messageObject.currentEvent;
                    if (tL_channelAdminLogEvent != null) {
                        TLRPC.ChannelAdminLogEventAction channelAdminLogEventAction = tL_channelAdminLogEvent.action;
                        if (channelAdminLogEventAction instanceof TLRPC.TL_channelAdminLogEventActionChangeWallpaper) {
                            wallPaper = ((TLRPC.TL_channelAdminLogEventActionChangeWallpaper) channelAdminLogEventAction).new_value;
                            if (TextUtils.isEmpty(ChatThemeController.getWallpaperEmoticon(wallPaper))) {
                                Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
                                boolean isDark = resourcesProvider != null ? resourcesProvider.isDark() : Theme.isCurrentThemeDark();
                                this.imageReceiver.clearImage();
                                Drawable backgroundDrawableFromTheme = PreviewView.getBackgroundDrawableFromTheme(this.currentAccount, ChatThemeController.getWallpaperEmoticon(wallPaper), isDark, false);
                                this.wallpaperPreviewDrawable = backgroundDrawableFromTheme;
                                if (backgroundDrawableFromTheme != null) {
                                    backgroundDrawableFromTheme.setCallback(this);
                                }
                            } else {
                                if (wallPaper == null || (str6 = wallPaper.uploadingImage) == null) {
                                    if (wallPaper != null) {
                                        TLObject tLObject = messageObject.photoThumbsObject;
                                        TLRPC.Document document3 = tLObject instanceof TLRPC.Document ? (TLRPC.Document) tLObject : wallPaper.document;
                                        imageReceiver2 = this.imageReceiver;
                                        forDocument = ImageLocation.getForDocument(document3);
                                        sb = new StringBuilder();
                                        sb.append("150_150_wallpaper");
                                        sb.append(wallPaper.id);
                                    }
                                    this.wallpaperPreviewDrawable = null;
                                } else {
                                    imageReceiver2 = this.imageReceiver;
                                    forDocument = ImageLocation.getForPath(str6);
                                    sb = new StringBuilder();
                                    sb.append("150_150_wallpaper");
                                    sb.append(wallPaper.id);
                                }
                                sb.append(ChatBackgroundDrawable.hash(wallPaper.settings));
                                imageReceiver2.setImage(forDocument, sb.toString(), null, null, ChatBackgroundDrawable.createThumb(wallPaper), 0L, null, wallPaper, 1);
                                this.wallpaperPreviewDrawable = null;
                            }
                            this.imageReceiver.setRoundRadius((int) (this.stickerSize / 2.0f));
                            if (getUploadingInfoProgress(messageObject) != 1.0f) {
                                this.radialProgress.setProgress(1.0f, z7);
                                radialProgress2 = this.radialProgress;
                                z4 = z7;
                                i2 = 4;
                            } else {
                                radialProgress2 = this.radialProgress;
                                z4 = z7;
                                i2 = 3;
                            }
                        }
                    }
                    TLRPC.Message message2 = messageObject.messageOwner;
                    wallPaper = (message2 == null || (messageAction = message2.action) == null) ? null : messageAction.wallpaper;
                    if (TextUtils.isEmpty(ChatThemeController.getWallpaperEmoticon(wallPaper))) {
                    }
                    this.imageReceiver.setRoundRadius((int) (this.stickerSize / 2.0f));
                    if (getUploadingInfoProgress(messageObject) != 1.0f) {
                    }
                } else if (i3 == 21) {
                    this.imageReceiver.setRoundRadius((int) (this.stickerSize / 2.0f));
                    this.imageReceiver.setAllowStartLottieAnimation(true);
                    this.imageReceiver.setDelegate(null);
                    TLRPC.TL_messageActionSuggestProfilePhoto tL_messageActionSuggestProfilePhoto = (TLRPC.TL_messageActionSuggestProfilePhoto) messageObject.messageOwner.action;
                    TLRPC.VideoSize closestVideoSizeWithSize = FileLoader.getClosestVideoSizeWithSize(tL_messageActionSuggestProfilePhoto.photo.video_sizes, 1000);
                    ArrayList<TLRPC.VideoSize> arrayList = tL_messageActionSuggestProfilePhoto.photo.video_sizes;
                    ImageLocation forPhoto = (arrayList == null || arrayList.isEmpty()) ? null : ImageLocation.getForPhoto(closestVideoSizeWithSize, tL_messageActionSuggestProfilePhoto.photo);
                    TLRPC.Photo photo = messageObject.messageOwner.action.photo;
                    if (messageObject.strippedThumb == null) {
                        int size2 = messageObject.photoThumbs.size();
                        int i5 = 0;
                        while (true) {
                            if (i5 >= size2) {
                                break;
                            }
                            TLRPC.PhotoSize photoSize3 = messageObject.photoThumbs.get(i5);
                            if (photoSize3 instanceof TLRPC.TL_photoStrippedSize) {
                                photoSize2 = photoSize3;
                                break;
                            }
                            i5++;
                        }
                    }
                    TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 1000);
                    if (closestPhotoSizeWithSize == null) {
                        z2 = z7;
                        z3 = false;
                    } else if (closestVideoSizeWithSize != null) {
                        z2 = z7;
                        z3 = false;
                        this.imageReceiver.setImage(forPhoto, ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForPhoto(closestPhotoSizeWithSize, photo), "150_150", ImageLocation.getForObject(photoSize2, messageObject.photoThumbsObject), "50_50_b", messageObject.strippedThumb, 0L, null, messageObject, 0);
                    } else {
                        z2 = z7;
                        z3 = false;
                        this.imageReceiver.setImage(ImageLocation.getForPhoto(closestPhotoSizeWithSize, photo), "150_150", ImageLocation.getForObject(photoSize2, messageObject.photoThumbsObject), "50_50_b", messageObject.strippedThumb, 0L, null, messageObject, 0);
                    }
                    this.imageReceiver.setAllowStartLottieAnimation(z3);
                    ImageUpdater imageUpdater = MessagesController.getInstance(this.currentAccount).photoSuggestion.get(messageObject.messageOwner.local_id);
                    if (imageUpdater != null) {
                        f = 1.0f;
                        if (imageUpdater.getCurrentImageProgress() != 1.0f) {
                            radialProgress2 = this.radialProgress;
                            z4 = z2;
                            i2 = 3;
                        }
                    } else {
                        f = 1.0f;
                    }
                    z4 = z2;
                    this.radialProgress.setProgress(f, z4);
                    radialProgress2 = this.radialProgress;
                    i2 = 4;
                } else if (i3 == 30 || i3 == 18 || i3 == 25) {
                    this.imageReceiver.setRoundRadius(0);
                    TLRPC.MessageAction messageAction2 = messageObject.messageOwner.action;
                    if (messageAction2 instanceof TLRPC.TL_messageActionStarGift) {
                        TL_stars.StarGift starGift = ((TLRPC.TL_messageActionStarGift) messageAction2).gift;
                        if (starGift != null) {
                            document = starGift.sticker;
                            str2 = null;
                            obj = null;
                            messageObject2 = messageObject;
                            this.forceWasUnread = messageObject.wasUnread;
                            this.giftSticker = document;
                            if (document != null) {
                                this.imageReceiver.setAllowStartLottieAnimation(true);
                                this.imageReceiver.setDelegate(this.giftStickerDelegate);
                                this.giftEffectAnimation = null;
                                int i6 = 0;
                                while (true) {
                                    if (i6 >= document.video_thumbs.size()) {
                                        break;
                                    }
                                    if ("f".equals(document.video_thumbs.get(i6).type)) {
                                        this.giftEffectAnimation = document.video_thumbs.get(i6);
                                        break;
                                    }
                                    i6++;
                                }
                                if (z5 || messageObject.type != 18) {
                                    SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(document, Theme.key_windowBackgroundGray, 0.3f);
                                    this.imageReceiver.setAutoRepeat(0);
                                    this.imageReceiver.setImage(ImageLocation.getForDocument(document), String.format(Locale.US, "%d_%d_nr_messageId=%d", Integer.valueOf(NotificationCenter.audioRouteChanged), Integer.valueOf(NotificationCenter.audioRouteChanged), Integer.valueOf(messageObject.stableId)), svgThumb, "tgs", messageObject2, 1);
                                }
                            } else if (str2 != null) {
                                MediaDataController.getInstance(this.currentAccount).loadStickersByEmojiOrName(str2, false, obj == null);
                            }
                        }
                        document = null;
                        str2 = null;
                        obj = null;
                        messageObject2 = messageObject;
                        this.forceWasUnread = messageObject.wasUnread;
                        this.giftSticker = document;
                        if (document != null) {
                        }
                    } else {
                        if (messageAction2 instanceof TLRPC.TL_messageActionStarGiftUnique) {
                            TLRPC.TL_messageActionStarGiftUnique tL_messageActionStarGiftUnique = (TLRPC.TL_messageActionStarGiftUnique) messageAction2;
                            if (tL_messageActionStarGiftUnique.refunded) {
                                TL_stars.StarGift starGift2 = tL_messageActionStarGiftUnique.gift;
                                if (starGift2 != null) {
                                    document = starGift2.getDocument();
                                    str2 = null;
                                    obj = null;
                                    messageObject2 = messageObject;
                                    this.forceWasUnread = messageObject.wasUnread;
                                    this.giftSticker = document;
                                    if (document != null) {
                                    }
                                }
                                document = null;
                                str2 = null;
                                obj = null;
                                messageObject2 = messageObject;
                                this.forceWasUnread = messageObject.wasUnread;
                                this.giftSticker = document;
                                if (document != null) {
                                }
                            }
                        }
                        String str7 = UserConfig.getInstance(this.currentAccount).premiumGiftsStickerPack;
                        if (str7 == null) {
                            MediaDataController.getInstance(this.currentAccount).checkPremiumGiftStickers();
                            return;
                        }
                        TLRPC.TL_messages_stickerSet stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByName(str7);
                        ?? r6 = stickerSetByName;
                        if (stickerSetByName == null) {
                            r6 = MediaDataController.getInstance(this.currentAccount).getStickerSetByEmojiOrName(str7);
                        }
                        if (r6 != 0) {
                            TLRPC.MessageAction messageAction3 = messageObject.messageOwner.action;
                            int i7 = messageAction3.months;
                            if (messageObject.type == 30) {
                                long j2 = messageAction3 instanceof TLRPC.TL_messageActionGiftStars ? ((TLRPC.TL_messageActionGiftStars) messageAction3).stars : ((TLRPC.TL_messageActionPrizeStars) messageAction3).stars;
                                String str8 = j2 <= 1000 ? "2⃣" : j2 < 2500 ? "3⃣" : "4⃣";
                                int i8 = 0;
                                while (true) {
                                    if (i8 >= r6.packs.size()) {
                                        break;
                                    }
                                    TLRPC.TL_stickerPack tL_stickerPack = r6.packs.get(i8);
                                    if (!TextUtils.equals(tL_stickerPack.emoticon, str8) || tL_stickerPack.documents.isEmpty()) {
                                        i8++;
                                    } else {
                                        long longValue = tL_stickerPack.documents.get(0).longValue();
                                        for (int i9 = 0; i9 < r6.documents.size(); i9++) {
                                            document2 = r6.documents.get(i9);
                                            if (document2 == null || document2.id != longValue) {
                                            }
                                        }
                                    }
                                }
                                str = str7;
                                document = null;
                                if (document == null && !r6.documents.isEmpty()) {
                                    document = r6.documents.get(0);
                                }
                                messageObject2 = r6;
                            } else {
                                String str9 = (String) monthsToEmoticon.get(Integer.valueOf(i7));
                                Iterator<TLRPC.TL_stickerPack> it = r6.packs.iterator();
                                document2 = null;
                                while (it.hasNext()) {
                                    TLRPC.TL_stickerPack next = it.next();
                                    if (Objects.equals(next.emoticon, str9)) {
                                        Iterator<Long> it2 = next.documents.iterator();
                                        while (it2.hasNext()) {
                                            long longValue2 = it2.next().longValue();
                                            Iterator<TLRPC.Document> it3 = r6.documents.iterator();
                                            while (true) {
                                                if (!it3.hasNext()) {
                                                    str = str7;
                                                    break;
                                                }
                                                TLRPC.Document next2 = it3.next();
                                                str = str7;
                                                if (next2.id == longValue2) {
                                                    document2 = next2;
                                                    break;
                                                }
                                                str7 = str;
                                            }
                                            if (document2 != null) {
                                                break;
                                            } else {
                                                str7 = str;
                                            }
                                        }
                                    }
                                    str = str7;
                                    if (document2 != null) {
                                        break;
                                    } else {
                                        str7 = str;
                                    }
                                }
                            }
                            str = str7;
                            document = document2;
                            if (document == null) {
                                document = r6.documents.get(0);
                            }
                            messageObject2 = r6;
                        } else {
                            str = str7;
                            document = null;
                            messageObject2 = null;
                        }
                        str2 = str;
                        obj = r6;
                        this.forceWasUnread = messageObject.wasUnread;
                        this.giftSticker = document;
                        if (document != null) {
                        }
                    }
                } else if (i3 == 11) {
                    this.imageReceiver.setAllowStartLottieAnimation(true);
                    this.imageReceiver.setDelegate(null);
                    this.imageReceiver.setRoundRadius(AndroidUtilities.roundMessageSize / 2);
                    this.imageReceiver.setAutoRepeatCount(1);
                    this.avatarDrawable.setInfo(messageObject.getDialogId(), null, null);
                    if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                        this.imageReceiver.setImage(null, null, this.avatarDrawable, null, messageObject, 0);
                    } else {
                        if (messageObject.strippedThumb == null) {
                            int size3 = messageObject.photoThumbs.size();
                            for (int i10 = 0; i10 < size3; i10++) {
                                photoSize = messageObject.photoThumbs.get(i10);
                                if (photoSize instanceof TLRPC.TL_photoStrippedSize) {
                                    break;
                                }
                            }
                        }
                        photoSize = null;
                        TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 640);
                        if (closestPhotoSizeWithSize2 != null) {
                            TLRPC.Photo photo2 = messageObject.messageOwner.action.photo;
                            if (!photo2.video_sizes.isEmpty() && SharedConfig.isAutoplayGifs()) {
                                TLRPC.VideoSize closestVideoSizeWithSize2 = FileLoader.getClosestVideoSizeWithSize(photo2.video_sizes, 1000);
                                if (messageObject.mediaExists || DownloadController.getInstance(this.currentAccount).canDownloadMedia(4, closestVideoSizeWithSize2.size)) {
                                    videoSize = closestVideoSizeWithSize2;
                                } else {
                                    this.currentVideoLocation = ImageLocation.getForPhoto(closestVideoSizeWithSize2, photo2);
                                    DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(FileLoader.getAttachFileName(closestVideoSizeWithSize2), messageObject, this);
                                }
                            }
                            if (videoSize != null) {
                                imageReceiver = this.imageReceiver;
                                imageLocation = ImageLocation.getForPhoto(videoSize, photo2);
                                imageLocation2 = ImageLocation.getForObject(photoSize, messageObject.photoThumbsObject);
                                bitmapDrawable = messageObject.strippedThumb;
                                str3 = null;
                                i = 1;
                                str4 = ImageLoader.AUTOPLAY_FILTER;
                                str5 = "50_50_b";
                                j = 0;
                            } else {
                                ImageReceiver imageReceiver3 = this.imageReceiver;
                                ImageLocation forObject = ImageLocation.getForObject(closestPhotoSizeWithSize2, messageObject.photoThumbsObject);
                                ImageLocation forObject2 = ImageLocation.getForObject(photoSize, messageObject.photoThumbsObject);
                                bitmapDrawable = messageObject.strippedThumb;
                                str3 = null;
                                i = 1;
                                str4 = "150_150";
                                imageReceiver = imageReceiver3;
                                imageLocation = forObject;
                                imageLocation2 = forObject2;
                                str5 = "50_50_b";
                                j = 0;
                            }
                            imageReceiver.setImage(imageLocation, str4, imageLocation2, str5, bitmapDrawable, j, str3, messageObject, i);
                        } else {
                            this.imageReceiver.setImageBitmap(this.avatarDrawable);
                        }
                    }
                    this.imageReceiver.setVisible(!PhotoViewer.isShowingImage(messageObject), false);
                } else {
                    this.imageReceiver.setAllowStartLottieAnimation(true);
                    this.imageReceiver.setDelegate(null);
                    this.imageReceiver.setImageBitmap((Bitmap) null);
                }
                radialProgress2.setIcon(i2, z4, z4);
            }
            this.rippleView.setVisibility((!isButtonLayout(messageObject) || this.starGiftLayout.has()) ? 8 : 0);
            ForumUtilities.applyTopicToMessage(messageObject);
            requestLayout();
        }
    }

    public void setOverrideColor(int i, int i2) {
        this.overrideBackground = i;
        this.overrideText = i2;
    }

    public void setOverrideTextMaxWidth(int i) {
        this.overriddenMaxWidth = i;
    }

    public void setScrimReaction(Integer num) {
        this.reactionsLayoutInBubble.setScrimReaction(num);
    }

    public void setSpoilersSuppressed(boolean z) {
        Iterator it = this.spoilers.iterator();
        while (it.hasNext()) {
            ((SpoilerEffect) it.next()).setSuppressUpdates(z);
        }
    }

    public void setVisiblePart(float f, float f2, int i, float f3) {
        this.visiblePartSet = true;
        this.backgroundHeight = i;
        this.viewTop = f;
        this.viewTranslationX = f2;
        this.dimAmount = f3;
        this.dimPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (f3 * 255.0f)));
        invalidate();
    }

    public void setVisiblePart(float f, int i) {
        this.visiblePartSet = true;
        this.backgroundHeight = i;
        this.viewTop = f;
        this.viewTranslationX = 0.0f;
    }

    public boolean showingCancelButton() {
        RadialProgress2 radialProgress2 = this.radialProgress;
        return radialProgress2 != null && radialProgress2.getIcon() == 3;
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return drawable == this.wallpaperPreviewDrawable || super.verifyDrawable(drawable);
    }
}
