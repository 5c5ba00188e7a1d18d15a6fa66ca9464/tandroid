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
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.LoadingDrawable;
import org.telegram.ui.Components.Premium.StarParticlesView;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.Gifts.GiftSheet;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.ProfileActivity;
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
    private AnimatedEmojiSpan.EmojiGroupedSpans giftPremiumSubtitleLayoutEmoji;
    public List giftPremiumSubtitleLayoutSpoilers;
    private float giftPremiumSubtitleLayoutX;
    private float giftPremiumSubtitleLayoutY;
    private final AtomicReference giftPremiumSubtitlePatchedLayout;
    private int giftPremiumSubtitleWidth;
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
    private TextPaint giftTitlePaint;
    private boolean hasReplyMessage;
    private boolean imagePressed;
    private ImageReceiver imageReceiver;
    private boolean invalidateColors;
    private boolean invalidatePath;
    private View invalidateWithParent;
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
    private RectF rect;
    private View rippleView;
    private StaticLayout settingWallpaperLayout;
    TextPaint settingWallpaperPaint;
    private float settingWallpaperProgress;
    private StaticLayout settingWallpaperProgressTextLayout;
    private SpoilerEffect spoilerPressed;
    public List spoilers;
    private Stack spoilersPool;
    private StarParticlesView.Drawable starParticlesDrawable;
    private Path starsPath;
    private int starsSize;
    private int stickerSize;
    private int textHeight;
    private StaticLayout textLayout;
    TextPaint textPaint;
    private int textWidth;
    private int textX;
    private int textXLeft;
    private int textY;
    private Theme.ResourcesProvider themeDelegate;
    private float viewTop;
    private float viewTranslationX;
    private boolean visiblePartSet;
    private Drawable wallpaperPreviewDrawable;
    private boolean wasLayout;

    /* loaded from: classes4.dex */
    public interface ChatActionCellDelegate {

        /* loaded from: classes4.dex */
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

            public static void $default$didPressReplyMessage(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, int i) {
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

        void didPressReplyMessage(ChatActionCell chatActionCell, int i);

        BaseFragment getBaseFragment();

        long getDialogId();

        long getTopicId();

        void needOpenInviteLink(TLRPC.TL_chatInviteExported tL_chatInviteExported);

        void needOpenUserProfile(long j);

        void needShowEffectOverlay(ChatActionCell chatActionCell, TLRPC.Document document, TLRPC.VideoSize videoSize);
    }

    /* loaded from: classes4.dex */
    public interface ThemeDelegate extends Theme.ResourcesProvider {

        /* loaded from: classes4.dex */
        public abstract /* synthetic */ class -CC {
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
        this.overrideBackground = -1;
        this.overrideText = -1;
        this.lineWidths = new ArrayList();
        this.lineHeights = new ArrayList();
        this.backgroundPath = new Path();
        this.rect = new RectF();
        this.invalidatePath = true;
        this.invalidateColors = false;
        this.giftPremiumSubtitleLayoutSpoilers = new ArrayList();
        this.giftPremiumSubtitlePatchedLayout = new AtomicReference();
        this.buttonClickableAsImage = true;
        this.giftTitlePaint = new TextPaint(1);
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
        this.avatarStoryParams.drawSegments = false;
        this.canDrawInParent = z;
        this.themeDelegate = resourcesProvider;
        ImageReceiver imageReceiver = new ImageReceiver(this);
        this.imageReceiver = imageReceiver;
        imageReceiver.setRoundRadius(AndroidUtilities.roundMessageSize / 2);
        this.avatarDrawable = new AvatarDrawable();
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        this.giftTitlePaint.setTextSize(TypedValue.applyDimension(1, 16.0f, getResources().getDisplayMetrics()));
        this.giftSubtitlePaint.setTextSize(TypedValue.applyDimension(1, 15.0f, getResources().getDisplayMetrics()));
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
    private void buildLayout() {
        CharSequence charSequence;
        CharSequence replaceTags;
        int i;
        CharSequence charSequence2;
        String str;
        int i2;
        boolean z;
        CharSequence charSequence3;
        String str2;
        String str3;
        ArrayList<TLRPC.VideoSize> arrayList;
        TLRPC.Photo photo;
        ArrayList<TLRPC.VideoSize> arrayList2;
        String string;
        String string2;
        CharSequence formatString;
        int i3;
        String str4;
        boolean z2;
        ChatActionCell chatActionCell;
        boolean z3;
        String str5;
        String str6;
        String forcedFirstName;
        int i4;
        String str7;
        CharSequence string3;
        String str8;
        TLRPC.MessageMedia messageMedia;
        int i5;
        MessageObject messageObject = this.currentMessageObject;
        boolean z4 = true;
        if (messageObject != null) {
            charSequence = messageObject.isExpiredStory() ? messageObject.messageOwner.media.user_id != UserConfig.getInstance(this.currentAccount).getClientUserId() ? StoriesUtilities.createExpiredStoryString(true, "ExpiredStoryMention", R.string.ExpiredStoryMention, new Object[0]) : StoriesUtilities.createExpiredStoryString(true, "ExpiredStoryMentioned", R.string.ExpiredStoryMentioned, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())).first_name) : (this.delegate.getTopicId() == 0 && MessageObject.isTopicActionMessage(messageObject)) ? ForumUtilities.createActionTextWithTopic(MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-messageObject.getDialogId(), MessageObject.getTopicId(this.currentAccount, messageObject.messageOwner, true)), messageObject) : null;
            if (charSequence == null) {
                TLRPC.Message message = messageObject.messageOwner;
                if (message != null && (messageMedia = message.media) != null && messageMedia.ttl_seconds != 0) {
                    if (messageMedia.photo != null) {
                        i5 = R.string.AttachPhotoExpired;
                    } else {
                        TLRPC.Document document = messageMedia.document;
                        if ((document instanceof TLRPC.TL_documentEmpty) || ((messageMedia instanceof TLRPC.TL_messageMediaDocument) && document == null)) {
                            i5 = messageMedia.voice ? R.string.AttachVoiceExpired : messageMedia.round ? R.string.AttachRoundExpired : R.string.AttachVideoExpired;
                        }
                    }
                    charSequence = LocaleController.getString(i5);
                }
                charSequence = AnimatedEmojiSpan.cloneSpans(messageObject.messageText);
            }
        } else {
            charSequence = this.customText;
        }
        createLayout(charSequence, this.previousWidth);
        if (messageObject != null) {
            int i6 = messageObject.type;
            if (i6 == 11) {
                float f = AndroidUtilities.roundMessageSize;
                this.imageReceiver.setImageCoords((this.previousWidth - AndroidUtilities.roundMessageSize) / 2.0f, this.textHeight + AndroidUtilities.dp(19.0f), f, f);
            } else if (i6 == 25) {
                createGiftPremiumChannelLayouts();
            } else {
                if (i6 == 30) {
                    TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.currentMessageObject.getDialogId()));
                    TLRPC.MessageAction messageAction = messageObject.messageOwner.action;
                    if (messageAction instanceof TLRPC.TL_messageActionGiftStars) {
                        String formatPluralStringComma = LocaleController.formatPluralStringComma("ActionGiftStarsTitle", (int) ((TLRPC.TL_messageActionGiftStars) messageAction).stars);
                        SpannableStringBuilder replaceTags2 = AndroidUtilities.replaceTags(this.currentMessageObject.isOutOwner() ? LocaleController.formatString(R.string.ActionGiftStarsSubtitle, UserObject.getForcedFirstName(user)) : LocaleController.getString(R.string.ActionGiftStarsSubtitleYou));
                        string = LocaleController.getString(R.string.ActionGiftStarsView);
                        i3 = this.giftRectSize;
                        str4 = null;
                        z2 = true;
                        chatActionCell = this;
                        string2 = formatPluralStringComma;
                        formatString = replaceTags2;
                        str6 = string;
                        str5 = str4;
                        z3 = z2;
                    } else if (!(messageAction instanceof TLRPC.TL_messageActionStarGift)) {
                        long j = ((TLRPC.TL_messageActionPrizeStars) messageAction).stars;
                        String string4 = LocaleController.getString(R.string.ActionStarGiveawayPrizeTitle);
                        CharSequence charSequence4 = this.currentMessageObject.messageText;
                        str2 = LocaleController.getString(R.string.ActionGiftStarsView);
                        i2 = this.giftRectSize;
                        z = true;
                        charSequence3 = charSequence4;
                        str3 = string4;
                        createGiftPremiumLayouts(str3, charSequence3, str2, null, i2, z);
                        this.textLayout = null;
                        this.textHeight = 0;
                        this.textY = 0;
                        return;
                    } else {
                        TLRPC.TL_messageActionStarGift tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction;
                        long j2 = tL_messageActionStarGift.convert_stars;
                        long fromChatId = messageObject.getFromChatId();
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                        if (tL_messageActionStarGift.name_hidden) {
                            forcedFirstName = messageObject.isOutOwner() ? LocaleController.formatString(R.string.Gift2ActionTitleInAnonymous, UserObject.getForcedFirstName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())))) : LocaleController.getString(R.string.Gift2ActionTitleAnonymous);
                        } else {
                            TLRPC.User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(fromChatId));
                            spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.Gift2ActionTitle)).append((CharSequence) " ");
                            if (user2 != null && user2.photo != null) {
                                spannableStringBuilder.append((CharSequence) "a ");
                                AvatarSpan avatarSpan = new AvatarSpan(this, this.currentAccount, 18.0f);
                                avatarSpan.setUser(user2);
                                spannableStringBuilder.setSpan(avatarSpan, spannableStringBuilder.length() - 2, spannableStringBuilder.length() - 1, 33);
                            }
                            forcedFirstName = UserObject.getForcedFirstName(user2);
                        }
                        spannableStringBuilder.append((CharSequence) forcedFirstName);
                        TLRPC.TL_textWithEntities tL_textWithEntities = tL_messageActionStarGift.message;
                        if (tL_textWithEntities != null && !TextUtils.isEmpty(tL_textWithEntities.text)) {
                            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(tL_messageActionStarGift.message.text);
                            this.giftSubtitlePaint.setTextSize(AndroidUtilities.dp(13.0f));
                            MessageObject.addEntitiesToText(spannableStringBuilder2, tL_messageActionStarGift.message.entities, false, false, true, true);
                            string3 = MessageObject.replaceAnimatedEmoji(Emoji.replaceEmoji((CharSequence) spannableStringBuilder2, this.giftSubtitlePaint.getFontMetricsInt(), false, (int[]) null), tL_messageActionStarGift.message.entities, this.giftSubtitlePaint.getFontMetricsInt());
                        } else if (messageObject.isOutOwner()) {
                            string3 = AndroidUtilities.replaceTags(LocaleController.formatPluralStringComma("Gift2ActionOutInfo", (int) j2, UserObject.getForcedFirstName(user)));
                        } else {
                            if (tL_messageActionStarGift.converted) {
                                i4 = (int) j2;
                                str7 = "Gift2ActionConvertedInfo";
                            } else if (tL_messageActionStarGift.saved) {
                                string3 = LocaleController.getString(R.string.Gift2ActionSavedInfo);
                            } else {
                                i4 = (int) j2;
                                str7 = "Gift2ActionInfo";
                            }
                            string3 = LocaleController.formatPluralStringComma(str7, i4);
                        }
                        CharSequence charSequence5 = string3;
                        TL_stars.StarGift starGift = tL_messageActionStarGift.gift;
                        if (starGift == null || !starGift.limited) {
                            str8 = null;
                        } else {
                            int i7 = R.string.Gift2Limited1OfRibbon;
                            int i8 = starGift.availability_total;
                            str8 = LocaleController.formatString(i7, i8 > 1500 ? AndroidUtilities.formatWholeNumber(i8, 1) : Integer.valueOf(i8));
                        }
                        str6 = (!messageObject.isOutOwner() || tL_messageActionStarGift.forceIn) ? LocaleController.getString(R.string.ActionGiftStarsView) : null;
                        chatActionCell = this;
                        string2 = spannableStringBuilder;
                        formatString = charSequence5;
                        str5 = str8;
                        i3 = this.giftRectSize;
                        z3 = true;
                    }
                } else if (i6 != 18) {
                    if (i6 == 21) {
                        TLRPC.TL_messageActionSuggestProfilePhoto tL_messageActionSuggestProfilePhoto = (TLRPC.TL_messageActionSuggestProfilePhoto) messageObject.messageOwner.action;
                        TLRPC.User user3 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.isOutOwner() ? 0L : messageObject.getDialogId()));
                        boolean z5 = tL_messageActionSuggestProfilePhoto.video || !((photo = tL_messageActionSuggestProfilePhoto.photo) == null || (arrayList2 = photo.video_sizes) == null || arrayList2.isEmpty());
                        if (user3.id == UserConfig.getInstance(this.currentAccount).clientUserId) {
                            TLRPC.User user4 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId()));
                            replaceTags = z5 ? LocaleController.formatString(R.string.ActionSuggestVideoFromYouDescription, user4.first_name) : LocaleController.formatString(R.string.ActionSuggestPhotoFromYouDescription, user4.first_name);
                        } else {
                            replaceTags = z5 ? LocaleController.formatString(R.string.ActionSuggestVideoToYouDescription, user3.first_name) : LocaleController.formatString(R.string.ActionSuggestPhotoToYouDescription, user3.first_name);
                        }
                        i = (tL_messageActionSuggestProfilePhoto.video || !((arrayList = tL_messageActionSuggestProfilePhoto.photo.video_sizes) == null || arrayList.isEmpty())) ? R.string.ViewVideoAction : R.string.ViewPhotoAction;
                    } else if (i6 == 22) {
                        TLRPC.User user5 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.isOutOwner() ? 0L : messageObject.getDialogId()));
                        if (messageObject.getDialogId() >= 0) {
                            if (!messageObject.isOutOwner() && messageObject.isWallpaperForBoth() && messageObject.isCurrentWallpaper()) {
                                charSequence2 = messageObject.messageText;
                                str = LocaleController.getString(R.string.RemoveWallpaperAction);
                                z4 = false;
                            } else if (user5 == null || user5.id != UserConfig.getInstance(this.currentAccount).clientUserId) {
                                charSequence2 = messageObject.messageText;
                                str = LocaleController.getString(R.string.ViewWallpaperAction);
                            }
                            i2 = this.giftRectSize;
                            z = z4;
                            charSequence3 = charSequence2;
                            str2 = str;
                            str3 = null;
                            createGiftPremiumLayouts(str3, charSequence3, str2, null, i2, z);
                            this.textLayout = null;
                            this.textHeight = 0;
                            this.textY = 0;
                            return;
                        }
                        charSequence2 = messageObject.messageText;
                        str = null;
                        i2 = this.giftRectSize;
                        z = z4;
                        charSequence3 = charSequence2;
                        str2 = str;
                        str3 = null;
                        createGiftPremiumLayouts(str3, charSequence3, str2, null, i2, z);
                        this.textLayout = null;
                        this.textHeight = 0;
                        this.textY = 0;
                        return;
                    } else if (!messageObject.isStoryMention()) {
                        return;
                    } else {
                        TLRPC.User user6 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.messageOwner.media.user_id));
                        replaceTags = AndroidUtilities.replaceTags(user6.self ? LocaleController.formatString("StoryYouMentionedTitle", R.string.StoryYouMentionedTitle, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())).first_name) : LocaleController.formatString("StoryMentionedTitle", R.string.StoryMentionedTitle, user6.first_name));
                        i = R.string.StoryMentionedAction;
                    }
                    str2 = LocaleController.getString(i);
                    i2 = this.giftRectSize;
                    str3 = null;
                    z = true;
                    charSequence3 = replaceTags;
                    createGiftPremiumLayouts(str3, charSequence3, str2, null, i2, z);
                    this.textLayout = null;
                    this.textHeight = 0;
                    this.textY = 0;
                    return;
                } else {
                    string = LocaleController.getString((!isGiftCode() || isSelfGiftCode()) ? R.string.ActionGiftPremiumView : R.string.GiftPremiumUseGiftBtn);
                    string2 = LocaleController.getString(R.string.ActionGiftPremiumTitle);
                    formatString = LocaleController.formatString(R.string.ActionGiftPremiumSubtitle, LocaleController.formatPluralString("Months", messageObject.messageOwner.action.months, new Object[0]));
                    i3 = this.giftRectSize;
                    str4 = null;
                    z2 = true;
                    chatActionCell = this;
                    str6 = string;
                    str5 = str4;
                    z3 = z2;
                }
                chatActionCell.createGiftPremiumLayouts(string2, formatString, str6, str5, i3, z3);
            }
        }
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
        int i2;
        int i3;
        int dp = this.giftRectSize - AndroidUtilities.dp(16.0f);
        this.giftTitlePaint.setTextSize(AndroidUtilities.dp(14.0f));
        this.giftSubtitlePaint.setTextSize(AndroidUtilities.dp(13.0f));
        TLRPC.TL_messageActionGiftCode tL_messageActionGiftCode = (TLRPC.TL_messageActionGiftCode) this.currentMessageObject.messageOwner.action;
        int i4 = tL_messageActionGiftCode.months;
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
        String formatPluralString = i4 == 12 ? LocaleController.formatPluralString("BoldYears", 1, new Object[0]) : LocaleController.formatPluralString("BoldMonths", i4, new Object[0]);
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
        this.giftPremiumSubtitleWidth = dp;
        StaticLayout staticLayout = new StaticLayout(spannableStringBuilder, this.giftSubtitlePaint, dp, alignment, 1.1f, 0.0f, false);
        this.giftPremiumSubtitleLayout = staticLayout;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || !messageObject.isSpoilersRevealed) {
            i2 = 33;
            i3 = dp;
            SpoilerEffect.addSpoilers(this, staticLayout, -1, dp, null, this.giftPremiumSubtitleLayoutSpoilers);
        } else {
            List list = this.giftPremiumSubtitleLayoutSpoilers;
            if (list != null) {
                list.clear();
            }
            i3 = dp;
            i2 = 33;
        }
        this.giftPremiumSubtitleLayoutEmoji = AnimatedEmojiSpan.update(0, (View) this, false, this.giftPremiumSubtitleLayoutEmoji, null);
        SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(string2);
        valueOf2.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, valueOf2.length(), i2);
        StaticLayout staticLayout2 = new StaticLayout(valueOf2, (TextPaint) getThemedPaint("paintChatActionText"), i3, alignment, 1.0f, 0.0f, false);
        this.giftPremiumButtonLayout = staticLayout2;
        this.buttonClickableAsImage = true;
        this.giftPremiumButtonWidth = measureLayoutWidth(staticLayout2);
    }

    /* JADX WARN: Type inference failed for: r0v10, types: [android.graphics.Path, org.telegram.ui.Components.Text] */
    /* JADX WARN: Type inference failed for: r0v17 */
    /* JADX WARN: Type inference failed for: r0v21 */
    private void createGiftPremiumLayouts(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, int i, boolean z) {
        TextPaint textPaint;
        float f;
        StaticLayout staticLayout;
        int i2;
        int i3;
        float f2;
        ?? r0;
        int cutInFancyHalf;
        CharSequence charSequence5 = charSequence2;
        int dp = i - AndroidUtilities.dp(16.0f);
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && messageObject.type == 30) {
            dp -= AndroidUtilities.dp(16.0f);
        }
        int i4 = dp;
        if (charSequence != null) {
            MessageObject messageObject2 = this.currentMessageObject;
            if (messageObject2 == null || messageObject2.type != 30) {
                this.giftTitlePaint.setTextSize(AndroidUtilities.dp(16.0f));
            } else {
                this.giftTitlePaint.setTextSize(AndroidUtilities.dp(14.0f));
            }
            SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(charSequence);
            valueOf.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, valueOf.length(), 33);
            this.giftPremiumTitleLayout = new StaticLayout(valueOf, this.giftTitlePaint, i4, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        } else {
            this.giftPremiumTitleLayout = null;
        }
        if (this.currentMessageObject == null || !(isNewStyleButtonLayout() || this.currentMessageObject.type == 30)) {
            textPaint = this.giftSubtitlePaint;
            f = 15.0f;
        } else {
            textPaint = this.giftSubtitlePaint;
            f = 13.0f;
        }
        textPaint.setTextSize(AndroidUtilities.dp(f));
        this.giftPremiumSubtitleWidth = i4;
        MessageObject messageObject3 = this.currentMessageObject;
        int i5 = (messageObject3 == null || messageObject3.type != 22 || messageObject3.getDialogId() < 0 || (cutInFancyHalf = HintView2.cutInFancyHalf(charSequence5, this.giftSubtitlePaint)) >= i4 || ((float) cutInFancyHalf) <= ((float) i4) / 5.0f) ? i4 : cutInFancyHalf;
        try {
            charSequence5 = Emoji.replaceEmoji(charSequence5, this.giftSubtitlePaint.getFontMetricsInt(), false);
        } catch (Exception unused) {
        }
        CharSequence charSequence6 = charSequence5;
        TextPaint textPaint2 = this.giftSubtitlePaint;
        Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
        StaticLayout staticLayout2 = new StaticLayout(charSequence6, textPaint2, i5, alignment, 1.0f, AndroidUtilities.dp(1.66f), false);
        this.giftPremiumSubtitleLayout = staticLayout2;
        MessageObject messageObject4 = this.currentMessageObject;
        if (messageObject4 == null || !messageObject4.isSpoilersRevealed) {
            staticLayout = null;
            i2 = 33;
            i3 = i4;
            SpoilerEffect.addSpoilers(this, staticLayout2, -1, i5, null, this.giftPremiumSubtitleLayoutSpoilers);
        } else {
            List list = this.giftPremiumSubtitleLayoutSpoilers;
            if (list != null) {
                list.clear();
            }
            staticLayout = null;
            i3 = i4;
            i2 = 33;
        }
        this.giftPremiumSubtitleLayoutEmoji = AnimatedEmojiSpan.update(0, (View) this, false, this.giftPremiumSubtitleLayoutEmoji, this.giftPremiumSubtitleLayout);
        if (charSequence3 != null) {
            SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(charSequence3);
            valueOf2.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, valueOf2.length(), i2);
            r0 = staticLayout;
            StaticLayout staticLayout3 = new StaticLayout(valueOf2, (TextPaint) getThemedPaint("paintChatActionText"), i3, alignment, 1.0f, 0.0f, false);
            this.giftPremiumButtonLayout = staticLayout3;
            this.buttonClickableAsImage = z;
            f2 = measureLayoutWidth(staticLayout3);
        } else {
            StaticLayout staticLayout4 = staticLayout;
            this.giftPremiumButtonLayout = staticLayout4;
            this.buttonClickableAsImage = false;
            f2 = 0.0f;
            r0 = staticLayout4;
        }
        this.giftPremiumButtonWidth = f2;
        if (charSequence4 == null) {
            this.giftRibbonPath = r0;
            this.giftRibbonText = r0;
            return;
        }
        if (this.giftRibbonPaintEffect == null) {
            this.giftRibbonPaintEffect = new CornerPathEffect(AndroidUtilities.dp(5.0f));
        }
        if (this.giftRibbonPath == null) {
            Path path = new Path();
            this.giftRibbonPath = path;
            GiftSheet.Ribbon.fillRibbonPath(path, 1.35f);
        }
        Text text = new Text(charSequence4, 11.0f, AndroidUtilities.bold());
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
        if (messageObject == null || messageObject.type != 22 || (str = (messagesController = MessagesController.getInstance(this.currentAccount)).uploadingWallpaper) == null || !TextUtils.equals(messageObject.messageOwner.action.wallpaper.uploadingImage, str)) {
            return 1.0f;
        }
        return messagesController.uploadingWallpaperInfo.uploadingProgress;
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
        MessageObject messageObject = this.currentMessageObject;
        int i = messageObject.type;
        return i == 21 || i == 22 || messageObject.isStoryMention();
    }

    private boolean isSelfGiftCode() {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            TLRPC.Message message = messageObject.messageOwner;
            TLRPC.MessageAction messageAction = message.action;
            if (((messageAction instanceof TLRPC.TL_messageActionGiftCode) || (messageAction instanceof TLRPC.TL_messageActionGiftStars)) && (message.from_id instanceof TLRPC.TL_peerUser)) {
                return UserObject.isUserSelf(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.currentMessageObject.messageOwner.from_id.user_id)));
            }
            return false;
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
            lottieAnimation.stop();
            lottieAnimation.setCurrentFrame(lottieAnimation.getFramesCount() - 1, false);
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
        List list = this.giftPremiumSubtitleLayoutSpoilers;
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
        } else if (messageAction instanceof TLRPC.TL_messageActionPrizeStars) {
            Context context2 = getContext();
            int i2 = this.currentAccount;
            TLRPC.Message message3 = this.currentMessageObject.messageOwner;
            StarsIntroActivity.showTransactionSheet(context2, i2, message3.date, message3.from_id, message3.peer_id, (TLRPC.TL_messageActionPrizeStars) message3.action, this.avatarStoryParams.resourcesProvider);
        } else if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
            TLRPC.TL_messageActionStarGift tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction;
            if (tL_messageActionStarGift.forceIn) {
                return;
            }
            Context context3 = getContext();
            int i3 = this.currentAccount;
            long dialogId = this.currentMessageObject.getDialogId();
            boolean isOutOwner = this.currentMessageObject.isOutOwner();
            MessageObject messageObject2 = this.currentMessageObject;
            StarsIntroActivity.showActionGiftSheet(context3, i3, dialogId, isOutOwner, messageObject2.messageOwner.date, messageObject2.getId(), tL_messageActionStarGift, this.themeDelegate);
        }
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

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        MessageObject messageObject;
        if (i == NotificationCenter.startSpoilers) {
            setSpoilersSuppressed(false);
        } else if (i == NotificationCenter.stopSpoilers) {
            setSpoilersSuppressed(true);
        } else {
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
    }

    /* JADX WARN: Code restructure failed: missing block: B:146:0x0400, code lost:
        if (isFloating() != false) goto L150;
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x0428, code lost:
        if (isFloating() != false) goto L150;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x042b, code lost:
        r1 = 1.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x042d, code lost:
        r2 = r20;
        r2.setAlpha((int) (r5 * r1));
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
        float f2;
        int i3;
        int i4;
        Paint paint5;
        Paint paint6;
        int i5;
        float f3;
        int i6;
        float f4;
        int i7;
        int i8;
        int i9;
        float f5;
        Path path;
        RectF rectF;
        float f6;
        float f7;
        int i10;
        int i11;
        int i12;
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
        int i13 = this.overrideBackground;
        if (i13 >= 0) {
            int themedColor = getThemedColor(i13);
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
            int i14 = 0;
            int i15 = 0;
            while (true) {
                f2 = 1.5f;
                if (i14 >= lineCount) {
                    break;
                }
                int ceil = (int) Math.ceil(this.textLayout.getLineWidth(i14));
                if (i14 == 0 || (i12 = i15 - ceil) <= 0 || i12 > (dp * 1.5f) + dp2) {
                    i15 = ceil;
                }
                this.lineWidths.add(Integer.valueOf(i15));
                i14++;
            }
            int i16 = lineCount - 2;
            while (i16 >= 0) {
                int intValue = ((Integer) this.lineWidths.get(i16)).intValue();
                int i17 = i15 - intValue;
                if (i17 <= 0 || i17 > (dp * f2) + dp2) {
                    i15 = intValue;
                }
                this.lineWidths.set(i16, Integer.valueOf(i15));
                i16--;
                f2 = 1.5f;
            }
            int dp3 = AndroidUtilities.dp(4.0f);
            int measuredWidth = getMeasuredWidth() / 2;
            int dp4 = AndroidUtilities.dp(3.0f);
            int dp5 = AndroidUtilities.dp(6.0f);
            int i18 = dp - dp4;
            this.lineHeights.clear();
            this.backgroundPath.reset();
            float f8 = measuredWidth;
            this.backgroundPath.moveTo(f8, dp3);
            int i19 = 0;
            int i20 = 0;
            while (i19 < lineCount) {
                int intValue2 = ((Integer) this.lineWidths.get(i19)).intValue();
                int i21 = dp5;
                int lineBottom = this.textLayout.getLineBottom(i19);
                int i22 = lineCount - 1;
                if (i19 < i22) {
                    paint6 = themedPaint2;
                    paint5 = themedPaint;
                    i5 = ((Integer) this.lineWidths.get(i19 + 1)).intValue();
                } else {
                    paint5 = themedPaint;
                    paint6 = themedPaint2;
                    i5 = 0;
                }
                int i23 = lineBottom - i20;
                if (i19 == 0 || intValue2 > i15) {
                    f3 = 3.0f;
                    i23 += AndroidUtilities.dp(3.0f);
                } else {
                    f3 = 3.0f;
                }
                if (i19 == i22 || intValue2 > i5) {
                    i23 += AndroidUtilities.dp(f3);
                }
                float f9 = (intValue2 / 2.0f) + f8;
                int i24 = (i19 == i22 || intValue2 >= i5 || i19 == 0 || intValue2 >= i15) ? dp2 : i21;
                if (i19 == 0 || intValue2 > i15) {
                    i6 = measuredWidth;
                    f4 = f8;
                    i7 = lineCount;
                    i8 = i15;
                    i9 = lineBottom;
                    this.rect.set((f9 - dp4) - dp, dp3, i18 + f9, (dp * 2) + dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, -90.0f, 90.0f);
                } else if (intValue2 < i15) {
                    f4 = f8;
                    i9 = lineBottom;
                    float f10 = i18 + f9;
                    i6 = measuredWidth;
                    i7 = lineCount;
                    i8 = i15;
                    this.rect.set(f10, dp3, (i24 * 2) + f10, i11 + dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, -90.0f, -90.0f);
                } else {
                    i6 = measuredWidth;
                    f4 = f8;
                    i7 = lineCount;
                    i8 = i15;
                    i9 = lineBottom;
                }
                dp3 += i23;
                if (i19 == i22 || intValue2 >= i5) {
                    f5 = 3.0f;
                } else {
                    f5 = 3.0f;
                    dp3 -= AndroidUtilities.dp(3.0f);
                    i23 -= AndroidUtilities.dp(3.0f);
                }
                if (i19 != 0 && intValue2 < i8) {
                    dp3 -= AndroidUtilities.dp(f5);
                    i23 -= AndroidUtilities.dp(f5);
                }
                this.lineHeights.add(Integer.valueOf(i23));
                if (i19 == i22 || intValue2 > i5) {
                    this.rect.set((f9 - dp4) - dp, dp3 - (dp * 2), f9 + i18, dp3);
                    checkLeftRightBounds();
                    path = this.backgroundPath;
                    rectF = this.rect;
                    f6 = 0.0f;
                    f7 = 90.0f;
                } else if (intValue2 < i5) {
                    float f11 = f9 + i18;
                    this.rect.set(f11, dp3 - i10, (i24 * 2) + f11, dp3);
                    checkLeftRightBounds();
                    path = this.backgroundPath;
                    rectF = this.rect;
                    f6 = 180.0f;
                    f7 = -90.0f;
                } else {
                    i19++;
                    i15 = intValue2;
                    dp5 = i21;
                    themedPaint2 = paint6;
                    themedPaint = paint5;
                    f8 = f4;
                    i20 = i9;
                    measuredWidth = i6;
                    lineCount = i7;
                }
                path.arcTo(rectF, f6, f7);
                i19++;
                i15 = intValue2;
                dp5 = i21;
                themedPaint2 = paint6;
                themedPaint = paint5;
                f8 = f4;
                i20 = i9;
                measuredWidth = i6;
                lineCount = i7;
            }
            paint = themedPaint;
            paint2 = themedPaint2;
            int i25 = measuredWidth;
            int i26 = dp5;
            int i27 = lineCount - 1;
            int i28 = i27;
            while (i28 >= 0) {
                int intValue3 = i28 != 0 ? ((Integer) this.lineWidths.get(i28 - 1)).intValue() : 0;
                int intValue4 = ((Integer) this.lineWidths.get(i28)).intValue();
                int intValue5 = i28 != i27 ? ((Integer) this.lineWidths.get(i28 + 1)).intValue() : 0;
                this.textLayout.getLineBottom(i28);
                float f12 = i25 - (intValue4 / 2);
                int i29 = (i28 == i27 || intValue4 >= intValue5 || i28 == 0 || intValue4 >= intValue3) ? dp2 : i26;
                if (i28 == i27 || intValue4 > intValue5) {
                    this.rect.set(f12 - i18, dp3 - (dp * 2), dp4 + f12 + dp, dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 90.0f, 90.0f);
                } else if (intValue4 < intValue5) {
                    float f13 = f12 - i18;
                    this.rect.set(f13 - (i29 * 2), dp3 - i4, f13, dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 90.0f, -90.0f);
                }
                dp3 -= ((Integer) this.lineHeights.get(i28)).intValue();
                if (i28 == 0 || intValue4 > intValue3) {
                    this.rect.set(f12 - i18, dp3, f12 + dp4 + dp, (dp * 2) + dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 180.0f, 90.0f);
                } else if (intValue4 < intValue3) {
                    float f14 = f12 - i18;
                    this.rect.set(f14 - (i29 * 2), dp3, f14, i3 + dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 0.0f, -90.0f);
                }
                i28--;
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
        if (isButtonLayout(this.currentMessageObject)) {
            float width = (getWidth() - this.giftRectSize) / 2.0f;
            float f16 = this.textY + this.textHeight;
            if (isNewStyleButtonLayout()) {
                float dp6 = f16 + AndroidUtilities.dp(4.0f);
                AndroidUtilities.rectTmp.set(width, dp6, this.giftRectSize + width, this.backgroundRectHeight + dp6);
            } else {
                float dp7 = f16 + AndroidUtilities.dp(12.0f);
                RectF rectF2 = AndroidUtilities.rectTmp;
                float f17 = this.giftRectSize;
                rectF2.set(width, dp7, width + f17, f17 + dp7 + this.giftPremiumAdditionalHeight);
            }
            if (this.backgroundRect == null) {
                this.backgroundRect = new RectF();
            }
            this.backgroundRect.set(AndroidUtilities.rectTmp);
            canvas.drawRoundRect(this.backgroundRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), paint3);
            if (hasGradientService()) {
                canvas.drawRoundRect(this.backgroundRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), paint4);
            }
        }
        if (i >= 0) {
            paint3.setAlpha(i);
            paint4.setAlpha(i2);
        }
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        if (view == this.rippleView) {
            float scale = this.bounce.getScale(0.02f);
            canvas.save();
            canvas.scale(scale, scale, view.getX() + (view.getMeasuredWidth() / 2.0f), view.getY() + (view.getMeasuredHeight() / 2.0f));
            boolean drawChild = super.drawChild(canvas, view, j);
            canvas.restore();
            return drawChild;
        }
        return super.drawChild(canvas, view, j);
    }

    public void drawOutboundsContent(Canvas canvas) {
        canvas.save();
        canvas.translate(this.textXLeft, this.textY);
        StaticLayout staticLayout = this.textLayout;
        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout, this.animatedEmojiStack, 0.0f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f, staticLayout != null ? getAdaptiveEmojiColorFilter(staticLayout.getPaint().getColor()) : null);
        canvas.restore();
    }

    @Override // org.telegram.ui.Cells.BaseCell
    public int getBoundsLeft() {
        if (isButtonLayout(this.currentMessageObject)) {
            return (getWidth() - this.giftRectSize) / 2;
        }
        int i = this.backgroundLeft;
        ImageReceiver imageReceiver = this.imageReceiver;
        return (imageReceiver == null || !imageReceiver.getVisible()) ? i : Math.min((int) this.imageReceiver.getImageX(), i);
    }

    @Override // org.telegram.ui.Cells.BaseCell
    public int getBoundsRight() {
        if (isButtonLayout(this.currentMessageObject)) {
            return (getWidth() + this.giftRectSize) / 2;
        }
        int i = this.backgroundRight;
        ImageReceiver imageReceiver = this.imageReceiver;
        return (imageReceiver == null || !imageReceiver.getVisible()) ? i : Math.max((int) this.imageReceiver.getImageX2(), i);
    }

    public int getCustomDate() {
        return this.customDate;
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

    protected Paint getThemedPaint(String str) {
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        Paint paint = resourcesProvider != null ? resourcesProvider.getPaint(str) : null;
        return paint != null ? paint : Theme.getThemePaint(str);
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
    }

    @Override // android.view.View
    public void invalidate(int i, int i2, int i3, int i4) {
        super.invalidate(i, i2, i3, i4);
        View view = this.invalidateWithParent;
        if (view != null) {
            view.invalidate();
        }
    }

    @Override // android.view.View
    public void invalidate(Rect rect) {
        super.invalidate(rect);
        View view = this.invalidateWithParent;
        if (view != null) {
            view.invalidate();
        }
    }

    public boolean isFloating() {
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        ChatActionCellDelegate chatActionCellDelegate;
        super.onAttachedToWindow();
        this.imageReceiver.onAttachedToWindow();
        setStarsPaused(false);
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, (!this.canDrawInParent || (chatActionCellDelegate = this.delegate) == null || chatActionCellDelegate.canDrawOutboundsContent()) ? false : true, this.animatedEmojiStack, this.textLayout);
        this.giftPremiumSubtitleLayoutEmoji = AnimatedEmojiSpan.update(0, (View) this, false, this.giftPremiumSubtitleLayoutEmoji, this.giftPremiumSubtitleLayout);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdatePremiumGiftStickers);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.starGiftsLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.diceStickersDidLoad);
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || messageObject.type != 21) {
            return;
        }
        setMessageObject(messageObject, true);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
        this.imageReceiver.onDetachedFromWindow();
        setStarsPaused(true);
        this.wasLayout = false;
        AnimatedEmojiSpan.release(this, this.animatedEmojiStack);
        AnimatedEmojiSpan.release(this, this.giftPremiumSubtitleLayoutEmoji);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdatePremiumGiftStickers);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.starGiftsLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.diceStickersDidLoad);
        this.avatarStoryParams.onDetachFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0279, code lost:
        if (r0.getCurrentImageProgress() == 1.0f) goto L249;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x02a8, code lost:
        if (r0 == 1.0f) goto L249;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x02aa, code lost:
        r37.radialProgress.setIcon(4, true, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x02b0, code lost:
        r37.radialProgress.setIcon(3, true, true);
     */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0403  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x043a  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0458  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x06e1  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x074f  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0755  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0760  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x076d  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0781  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x07b3  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0852  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0864  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x087f  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x08d2  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x091b  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0974  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x09a2  */
    /* JADX WARN: Removed duplicated region for block: B:259:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        int i;
        float f;
        float f2;
        float f3;
        float dp;
        float dp2;
        float dp3;
        float f4;
        float f5;
        float f6;
        StaticLayout staticLayout;
        Theme.ResourcesProvider resourcesProvider;
        boolean z;
        float f7;
        float clamp;
        int i2;
        int i3;
        int i4;
        MessageObject messageObject = this.currentMessageObject;
        int i5 = this.stickerSize;
        if (isButtonLayout(messageObject)) {
            this.stickerSize = this.giftRectSize - AndroidUtilities.dp(106.0f);
            if (isNewStyleButtonLayout()) {
                i5 = getImageSize(messageObject);
                float f8 = (this.previousWidth - i5) / 2.0f;
                float dp4 = this.textY + this.textHeight + AndroidUtilities.dp(4.0f) + AndroidUtilities.dp(16.0f);
                if (messageObject.isStoryMention()) {
                    this.avatarStoryParams.storyItem = messageObject.messageOwner.media.storyItem;
                }
                float f9 = i5;
                this.avatarStoryParams.originalAvatarRect.set(f8, dp4, f8 + f9, dp4 + f9);
                this.imageReceiver.setImageCoords(f8, dp4, f9, f9);
            } else {
                int i6 = messageObject.type;
                if (i6 == 11) {
                    ImageReceiver imageReceiver = this.imageReceiver;
                    int i7 = this.previousWidth;
                    float f10 = this.stickerSize;
                    imageReceiver.setImageCoords((i7 - i4) / 2.0f, this.textY + this.textHeight + (this.giftRectSize * 0.075f), f10, f10);
                } else {
                    if (i6 == 25) {
                        i3 = (int) (this.stickerSize * (AndroidUtilities.isTablet() ? 1.0f : 1.2f));
                    } else if (i6 == 30) {
                        i3 = (int) (this.stickerSize * 1.1f);
                        TLRPC.Message message = messageObject.messageOwner;
                        if (message == null || (message.action instanceof TLRPC.TL_messageActionStarGift)) {
                            float f11 = i3;
                            this.imageReceiver.setImageCoords((this.previousWidth - i3) / 2.0f, ((this.textY + this.textHeight) + (this.giftRectSize * 0.075f)) - AndroidUtilities.dp(12.0f), f11, f11);
                            i5 = i3;
                        }
                    } else {
                        i5 = (int) (this.stickerSize * 1.0f);
                        float f12 = i5;
                        this.imageReceiver.setImageCoords((this.previousWidth - i5) / 2.0f, ((this.textY + this.textHeight) + (this.giftRectSize * 0.075f)) - AndroidUtilities.dp(4.0f), f12, f12);
                    }
                    float f13 = i3;
                    this.imageReceiver.setImageCoords((this.previousWidth - i3) / 2.0f, ((this.textY + this.textHeight) + (this.giftRectSize * 0.075f)) - AndroidUtilities.dp(22.0f), f13, f13);
                    i5 = i3;
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
            }
        }
        int i8 = i5;
        drawBackground(canvas, false);
        if (isButtonLayout(messageObject) || (messageObject != null && messageObject.type == 11)) {
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
            int i9 = messageObject.type;
            if (i9 == 21) {
                ImageUpdater imageUpdater = MessagesController.getInstance(this.currentAccount).photoSuggestion.get(messageObject.messageOwner.local_id);
                if (imageUpdater != null) {
                    this.radialProgress.setProgress(imageUpdater.getCurrentImageProgress(), true);
                    this.radialProgress.setCircleRadius(((int) (this.imageReceiver.getImageWidth() * 0.5f)) + 1);
                    this.radialProgress.setMaxIconSize(AndroidUtilities.dp(24.0f));
                    this.radialProgress.setColorKeys(Theme.key_chat_mediaLoaderPhoto, Theme.key_chat_mediaLoaderPhotoSelected, Theme.key_chat_mediaLoaderPhotoIcon, Theme.key_chat_mediaLoaderPhotoIconSelected);
                }
            } else if (i9 == 22) {
                float uploadingInfoProgress = getUploadingInfoProgress(messageObject);
                this.radialProgress.setProgress(uploadingInfoProgress, true);
                this.radialProgress.setCircleRadius(AndroidUtilities.dp(26.0f));
                this.radialProgress.setMaxIconSize(AndroidUtilities.dp(24.0f));
                this.radialProgress.setColorKeys(Theme.key_chat_mediaLoaderPhoto, Theme.key_chat_mediaLoaderPhotoSelected, Theme.key_chat_mediaLoaderPhotoIcon, Theme.key_chat_mediaLoaderPhotoIconSelected);
            }
            this.radialProgress.draw(canvas);
        }
        if (this.textPaint == null || this.textLayout == null) {
            i = i8;
            f = 2.0f;
            f2 = 16.0f;
            f3 = 1.0f;
        } else {
            canvas.save();
            canvas.translate(this.textXLeft, this.textY);
            if (this.textLayout.getPaint() != this.textPaint) {
                buildLayout();
            }
            canvas.save();
            SpoilerEffect.clipOutCanvas(canvas, this.spoilers);
            SpoilerEffect.layoutDrawMaybe(this.textLayout, canvas);
            ChatActionCellDelegate chatActionCellDelegate = this.delegate;
            if (chatActionCellDelegate == null || chatActionCellDelegate.canDrawOutboundsContent()) {
                StaticLayout staticLayout2 = this.textLayout;
                f = 2.0f;
                i = i8;
                f2 = 16.0f;
                f3 = 1.0f;
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout2, this.animatedEmojiStack, 0.0f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f, staticLayout2 == null ? null : getAdaptiveEmojiColorFilter(staticLayout2.getPaint().getColor()));
            } else {
                i = i8;
                f = 2.0f;
                f2 = 16.0f;
                f3 = 1.0f;
            }
            canvas.restore();
            for (SpoilerEffect spoilerEffect : this.spoilers) {
                spoilerEffect.setColor(this.textLayout.getPaint().getColor());
                spoilerEffect.draw(canvas);
            }
            canvas.restore();
        }
        if (!isButtonLayout(messageObject)) {
            return;
        }
        canvas.save();
        float dp5 = ((this.previousWidth - this.giftRectSize) / f) + AndroidUtilities.dp(8.0f);
        if (isNewStyleButtonLayout()) {
            RectF rectF = this.backgroundRect;
            dp = (rectF != null ? rectF.top : this.textY + this.textHeight + AndroidUtilities.dp(4.0f)) + (AndroidUtilities.dp(f2) * 2);
            dp2 = i;
        } else {
            dp = this.textY + this.textHeight + (this.giftRectSize * 0.075f) + (messageObject.type == 21 ? i : this.stickerSize) + AndroidUtilities.dp(4.0f);
            if (messageObject.type == 21) {
                dp += AndroidUtilities.dp(f2);
            }
            if (messageObject.type == 30) {
                TLRPC.Message message2 = messageObject.messageOwner;
                if (message2 == null || (message2.action instanceof TLRPC.TL_messageActionStarGift)) {
                    dp2 = AndroidUtilities.dp(f3);
                } else {
                    dp -= AndroidUtilities.dp(3.66f);
                }
            }
            canvas.translate(dp5, dp);
            if (this.giftPremiumTitleLayout == null) {
                canvas.save();
                canvas.translate(((this.giftRectSize - AndroidUtilities.dp(f2)) - this.giftPremiumTitleLayout.getWidth()) / f, 0.0f);
                this.giftPremiumTitleLayout.draw(canvas);
                canvas.restore();
                dp3 = dp + this.giftPremiumTitleLayout.getHeight() + AndroidUtilities.dp(messageObject.type == 25 ? 6.0f : 0.0f);
            } else {
                dp3 = dp - AndroidUtilities.dp(4.0f);
            }
            canvas.restore();
            float dp62 = dp3 + AndroidUtilities.dp(4.0f);
            canvas.save();
            canvas.translate(dp5, dp62);
            if (messageObject.type == 22) {
                f4 = dp5;
                f5 = 0.2f;
                if (this.giftPremiumSubtitleLayout != null) {
                    canvas.save();
                    canvas.translate(((this.giftRectSize - AndroidUtilities.dp(f2)) - this.giftPremiumSubtitleLayout.getWidth()) / f, 0.0f);
                    this.giftPremiumSubtitleLayoutX = f4 + (((this.giftRectSize - AndroidUtilities.dp(f2)) - this.giftPremiumSubtitleLayout.getWidth()) / f);
                    this.giftPremiumSubtitleLayoutY = dp62;
                    SpoilerEffect.renderWithRipple(this, false, this.giftSubtitlePaint.getColor(), 0, this.giftPremiumSubtitlePatchedLayout, 1, this.giftPremiumSubtitleLayout, this.giftPremiumSubtitleLayoutSpoilers, canvas, false);
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.giftPremiumSubtitleLayout, this.giftPremiumSubtitleLayoutEmoji, 0.0f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(this.giftSubtitlePaint.getColor()));
                    canvas.restore();
                }
            } else if (this.radialProgress.getTransitionProgress() == 1.0f && this.radialProgress.getIcon() == 4) {
                canvas.save();
                canvas.translate((this.giftPremiumSubtitleWidth - this.giftPremiumSubtitleLayout.getWidth()) / f, 0.0f);
                this.giftPremiumSubtitleLayoutX = ((this.giftPremiumSubtitleWidth - this.giftPremiumSubtitleLayout.getWidth()) / f) + dp5;
                this.giftPremiumSubtitleLayoutY = dp62;
                f5 = 0.2f;
                SpoilerEffect.renderWithRipple(this, false, this.giftSubtitlePaint.getColor(), 0, this.giftPremiumSubtitlePatchedLayout, 1, this.giftPremiumSubtitleLayout, this.giftPremiumSubtitleLayoutSpoilers, canvas, false);
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.giftPremiumSubtitleLayout, this.giftPremiumSubtitleLayoutEmoji, 0.0f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(this.giftSubtitlePaint.getColor()));
                canvas.restore();
                f4 = dp5;
            } else {
                f5 = 0.2f;
                if (this.settingWallpaperLayout == null) {
                    TextPaint textPaint4 = new TextPaint();
                    this.settingWallpaperPaint = textPaint4;
                    textPaint4.setTextSize(AndroidUtilities.dp(13.0f));
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
                    this.settingWallpaperLayout = new StaticLayout(spannableStringBuilder, this.settingWallpaperPaint, this.giftPremiumSubtitleWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                }
                float uploadingInfoProgress2 = getUploadingInfoProgress(messageObject);
                if (this.settingWallpaperProgressTextLayout == null || this.settingWallpaperProgress != uploadingInfoProgress2) {
                    this.settingWallpaperProgress = uploadingInfoProgress2;
                    this.settingWallpaperProgressTextLayout = new StaticLayout(((int) (uploadingInfoProgress2 * 100.0f)) + "%", this.giftSubtitlePaint, this.giftPremiumSubtitleWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                }
                this.settingWallpaperPaint.setColor(this.giftSubtitlePaint.getColor());
                if (this.radialProgress.getIcon() == 4) {
                    float transitionProgress = this.radialProgress.getTransitionProgress();
                    int color = this.giftSubtitlePaint.getColor();
                    float f14 = 1.0f - transitionProgress;
                    this.settingWallpaperPaint.setAlpha((int) (Color.alpha(color) * f14));
                    this.giftSubtitlePaint.setAlpha((int) (Color.alpha(color) * transitionProgress));
                    TextPaint textPaint5 = this.giftSubtitlePaint;
                    textPaint5.linkColor = textPaint5.getColor();
                    float f15 = (transitionProgress * 0.2f) + 0.8f;
                    canvas.save();
                    canvas.scale(f15, f15, this.giftPremiumSubtitleWidth / f, this.giftPremiumSubtitleLayout.getHeight() / f);
                    canvas.translate((this.giftPremiumSubtitleWidth - this.giftPremiumSubtitleLayout.getWidth()) / f, 0.0f);
                    this.giftPremiumSubtitleLayoutX = ((this.giftPremiumSubtitleWidth - this.giftPremiumSubtitleLayout.getWidth()) / f) + dp5;
                    this.giftPremiumSubtitleLayoutY = dp62;
                    f4 = dp5;
                    SpoilerEffect.renderWithRipple(this, false, this.giftSubtitlePaint.getColor(), 0, this.giftPremiumSubtitlePatchedLayout, 1, this.giftPremiumSubtitleLayout, this.giftPremiumSubtitleLayoutSpoilers, canvas, false);
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.giftPremiumSubtitleLayout, this.giftPremiumSubtitleLayoutEmoji, 0.0f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(this.giftSubtitlePaint.getColor()));
                    canvas.restore();
                    this.giftSubtitlePaint.setAlpha((int) (Color.alpha(color) * f14));
                    TextPaint textPaint6 = this.giftSubtitlePaint;
                    textPaint6.linkColor = textPaint6.getColor();
                    float f16 = (f14 * 0.2f) + 0.8f;
                    canvas.save();
                    canvas.scale(f16, f16, this.settingWallpaperLayout.getWidth() / f, this.settingWallpaperLayout.getHeight() / f);
                    SpoilerEffect.layoutDrawMaybe(this.settingWallpaperLayout, canvas);
                    canvas.restore();
                    canvas.save();
                    canvas.translate(0.0f, this.settingWallpaperLayout.getHeight() + AndroidUtilities.dp(4.0f));
                    canvas.scale(f16, f16, this.settingWallpaperProgressTextLayout.getWidth() / f, this.settingWallpaperProgressTextLayout.getHeight() / f);
                    SpoilerEffect.layoutDrawMaybe(this.settingWallpaperProgressTextLayout, canvas);
                    canvas.restore();
                    this.giftSubtitlePaint.setColor(color);
                    this.giftSubtitlePaint.linkColor = color;
                } else {
                    f4 = dp5;
                    this.settingWallpaperLayout.draw(canvas);
                    canvas.save();
                    canvas.translate(0.0f, this.settingWallpaperLayout.getHeight() + AndroidUtilities.dp(4.0f));
                    SpoilerEffect.layoutDrawMaybe(this.settingWallpaperProgressTextLayout, canvas);
                    canvas.restore();
                }
            }
            canvas.restore();
            if (this.giftPremiumTitleLayout != null) {
                f6 = 8.0f;
                AndroidUtilities.dp(8.0f);
            } else {
                f6 = 8.0f;
            }
            this.giftPremiumSubtitleLayout.getHeight();
            staticLayout = this.giftPremiumButtonLayout;
            if (staticLayout != null) {
                staticLayout.getHeight();
            }
            getHeight();
            AndroidUtilities.dp(f6);
            resourcesProvider = this.themeDelegate;
            if (resourcesProvider == null) {
                resourcesProvider.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
            } else {
                Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX, this.viewTop + AndroidUtilities.dp(4.0f));
            }
            float scale2 = this.bounce.getScale(0.02f);
            canvas.save();
            canvas.scale(scale2, scale2, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
            if (this.giftPremiumButtonLayout != null) {
                canvas.drawRoundRect(this.giftButtonRect, AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), getThemedPaint("paintChatActionBackgroundSelected"));
                if (hasGradientService()) {
                    canvas.drawRoundRect(this.giftButtonRect, AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), getThemedPaint("paintChatActionBackgroundDarken"));
                }
                if (this.dimAmount > 0.0f) {
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
                float f17 = this.progressToProgress;
                if (f17 != 1.0f) {
                    this.progressToProgress = f17 + 0.10666667f;
                    f7 = 0.0f;
                    clamp = Utilities.clamp(this.progressToProgress, 1.0f, f7);
                    this.progressToProgress = clamp;
                    if (clamp != f7) {
                        if (this.progressView == null) {
                            this.progressView = new RadialProgressView(getContext());
                        }
                        int dp7 = AndroidUtilities.dp(f2);
                        canvas.save();
                        float f18 = this.progressToProgress;
                        canvas.scale(f18, f18, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                        this.progressView.setSize(dp7);
                        this.progressView.setProgressColor(Theme.getColor(Theme.key_chat_serviceText));
                        this.progressView.draw(canvas, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                        canvas.restore();
                    }
                    if (this.progressToProgress != 1.0f && this.giftPremiumButtonLayout != null) {
                        canvas.save();
                        float f192 = 1.0f - this.progressToProgress;
                        canvas.scale(f192, f192, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                        canvas.translate(f4, this.giftButtonRect.top + AndroidUtilities.dp(8.0f));
                        canvas.translate(((this.giftRectSize - AndroidUtilities.dp(f2)) - this.giftPremiumButtonLayout.getWidth()) / f, 0.0f);
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
                            loadingDrawable2.setGradientScale(f);
                            this.loadingDrawable.setAppearByGradient(true);
                            this.loadingDrawable.setColors(Theme.multAlpha(-1, 0.08f), Theme.multAlpha(-1, f5), Theme.multAlpha(-1, f5), Theme.multAlpha(-1, 0.7f));
                            this.loadingDrawable.strokePaint.setStrokeWidth(AndroidUtilities.dp(1.0f));
                        }
                        this.loadingDrawable.resetDisappear();
                        this.loadingDrawable.setBounds(this.giftButtonRect);
                        this.loadingDrawable.setRadiiDp(16.0f);
                        this.loadingDrawable.draw(canvas);
                    }
                    canvas.restore();
                    if (this.backgroundRect != null || this.giftRibbonPath == null || this.giftRibbonText == null) {
                        return;
                    }
                    Paint themedPaint = getThemedPaint("paintChatActionBackground");
                    Paint themedPaint2 = getThemedPaint("paintChatActionBackgroundDarken");
                    float dp8 = (this.backgroundRect.right - AndroidUtilities.dp(65.0f)) + AndroidUtilities.dp(f);
                    float dp9 = this.backgroundRect.top - AndroidUtilities.dp(f);
                    Theme.ResourcesProvider resourcesProvider2 = this.themeDelegate;
                    if (resourcesProvider2 != null) {
                        resourcesProvider2.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX + dp8, this.viewTop + AndroidUtilities.dp(4.0f) + dp9);
                    } else {
                        Theme.applyServiceShaderMatrix(getMeasuredWidth(), this.backgroundHeight, this.viewTranslationX + dp8, this.viewTop + AndroidUtilities.dp(4.0f) + dp9);
                    }
                    canvas.save();
                    canvas.translate(dp8, dp9);
                    ColorFilter colorFilter = themedPaint.getColorFilter();
                    PathEffect pathEffect = themedPaint.getPathEffect();
                    Theme.ResourcesProvider resourcesProvider3 = this.themeDelegate;
                    boolean isDark = resourcesProvider3 != null ? resourcesProvider3.isDark() : Theme.isCurrentThemeDark();
                    if (this.giftRibbonPaintFilter == null || this.giftRibbonPaintFilterDark != isDark) {
                        ColorMatrix colorMatrix = new ColorMatrix();
                        if ((themedPaint.getColorFilter() instanceof ColorMatrixColorFilter) && Build.VERSION.SDK_INT >= 26) {
                            ((ColorMatrixColorFilter) themedPaint.getColorFilter()).getColorMatrix(colorMatrix);
                        }
                        AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, isDark ? 0.1f : -0.08f);
                        AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, isDark ? 0.15f : 0.1f);
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
                    this.giftRibbonText.draw(canvas, AndroidUtilities.dp(40.43f) - (this.giftRibbonText.getCurrentWidth() / f), AndroidUtilities.dp(26.0f), -1, 1.0f);
                    canvas.restore();
                    return;
                }
            }
            if (!z) {
                float f20 = this.progressToProgress;
                f7 = 0.0f;
                if (f20 != 0.0f) {
                    this.progressToProgress = f20 - 0.10666667f;
                }
                clamp = Utilities.clamp(this.progressToProgress, 1.0f, f7);
                this.progressToProgress = clamp;
                if (clamp != f7) {
                }
                if (this.progressToProgress != 1.0f) {
                    canvas.save();
                    float f1922 = 1.0f - this.progressToProgress;
                    canvas.scale(f1922, f1922, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                    canvas.translate(f4, this.giftButtonRect.top + AndroidUtilities.dp(8.0f));
                    canvas.translate(((this.giftRectSize - AndroidUtilities.dp(f2)) - this.giftPremiumButtonLayout.getWidth()) / f, 0.0f);
                    this.giftPremiumButtonLayout.draw(canvas);
                    canvas.restore();
                }
                if (messageObject.flickerLoading) {
                }
                canvas.restore();
                if (this.backgroundRect != null) {
                    return;
                }
                return;
            }
            f7 = 0.0f;
            clamp = Utilities.clamp(this.progressToProgress, 1.0f, f7);
            this.progressToProgress = clamp;
            if (clamp != f7) {
            }
            if (this.progressToProgress != 1.0f) {
            }
            if (messageObject.flickerLoading) {
            }
            canvas.restore();
            if (this.backgroundRect != null) {
            }
        }
        dp += dp2;
        canvas.translate(dp5, dp);
        if (this.giftPremiumTitleLayout == null) {
        }
        canvas.restore();
        float dp622 = dp3 + AndroidUtilities.dp(4.0f);
        canvas.save();
        canvas.translate(dp5, dp622);
        if (messageObject.type == 22) {
        }
        canvas.restore();
        if (this.giftPremiumTitleLayout != null) {
        }
        this.giftPremiumSubtitleLayout.getHeight();
        staticLayout = this.giftPremiumButtonLayout;
        if (staticLayout != null) {
        }
        getHeight();
        AndroidUtilities.dp(f6);
        resourcesProvider = this.themeDelegate;
        if (resourcesProvider == null) {
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
        f7 = 0.0f;
        clamp = Utilities.clamp(this.progressToProgress, 1.0f, f7);
        this.progressToProgress = clamp;
        if (clamp != f7) {
        }
        if (this.progressToProgress != 1.0f) {
        }
        if (messageObject.flickerLoading) {
        }
        canvas.restore();
        if (this.backgroundRect != null) {
        }
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        CharacterStyle[] characterStyleArr;
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
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

    /* JADX WARN: Removed duplicated region for block: B:39:0x00dc  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x019d  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01b9  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x024c  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0259  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int i, int i2) {
        int i3;
        StaticLayout staticLayout;
        float dp;
        int i4;
        int lineBottom;
        StaticLayout staticLayout2;
        TLRPC.Message message;
        int measuredWidth;
        int dp2;
        StaticLayout staticLayout3;
        int i5;
        int dp3;
        int i6;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null && this.customText == null) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), this.textHeight + AndroidUtilities.dp(14.0f));
            return;
        }
        int i7 = 0;
        if (isButtonLayout(messageObject)) {
            this.giftRectSize = Math.min((int) (AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() * 0.6f : (AndroidUtilities.displaySize.x * 0.62f) - AndroidUtilities.dp(34.0f)), ((AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.statusBarHeight) - AndroidUtilities.dp(64.0f));
            if (!AndroidUtilities.isTablet() && ((i6 = messageObject.type) == 18 || i6 == 30)) {
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
                i5 = AndroidUtilities.roundMessageSize;
                dp3 = AndroidUtilities.dp(10.0f);
            } else if (isButtonLayout(messageObject)) {
                i5 = this.giftRectSize;
                dp3 = AndroidUtilities.dp(12.0f);
            }
            i3 = i5 + dp3;
            if (isButtonLayout(messageObject)) {
                boolean isGiftChannel = isGiftChannel(messageObject);
                int imageSize = getImageSize(messageObject);
                float dp4 = isNewStyleButtonLayout() ? this.textY + this.textHeight + AndroidUtilities.dp(4.0f) + (AndroidUtilities.dp(16.0f) * 2) + imageSize + this.giftPremiumSubtitleLayout.getHeight() + AndroidUtilities.dp(4.0f) : this.textY + this.textHeight + (this.giftRectSize * 0.075f) + imageSize + AndroidUtilities.dp(4.0f) + AndroidUtilities.dp(4.0f) + this.giftPremiumSubtitleLayout.getHeight();
                this.giftPremiumAdditionalHeight = 0;
                if (this.giftPremiumTitleLayout != null) {
                    dp = dp4 + staticLayout.getHeight() + AndroidUtilities.dp(isGiftChannel ? 6.0f : 0.0f);
                } else {
                    dp = dp4 - AndroidUtilities.dp(12.0f);
                    this.giftPremiumAdditionalHeight -= AndroidUtilities.dp(30.0f);
                }
                if (this.currentMessageObject.type == 30) {
                    i4 = this.giftPremiumAdditionalHeight;
                    lineBottom = this.giftPremiumSubtitleLayout.getHeight() - AndroidUtilities.dp(20.0f);
                } else {
                    if (this.giftPremiumSubtitleLayout.getLineCount() > 2) {
                        i4 = this.giftPremiumAdditionalHeight;
                        lineBottom = ((this.giftPremiumSubtitleLayout.getLineBottom(0) - this.giftPremiumSubtitleLayout.getLineTop(0)) * this.giftPremiumSubtitleLayout.getLineCount()) - 2;
                    }
                    int dp52 = this.giftPremiumAdditionalHeight - AndroidUtilities.dp(isGiftChannel ? 14.0f : 0.0f);
                    this.giftPremiumAdditionalHeight = dp52;
                    i3 += dp52;
                    int dp62 = this.textHeight + i3 + AndroidUtilities.dp(14.0f);
                    if (this.giftPremiumButtonLayout == null) {
                        float height = dp + ((((dp62 - dp) - staticLayout2.getHeight()) - AndroidUtilities.dp(8.0f)) / 2.0f);
                        float f = (this.previousWidth - this.giftPremiumButtonWidth) / 2.0f;
                        this.giftButtonRect.set(f - AndroidUtilities.dp(18.0f), height - AndroidUtilities.dp(8.0f), f + this.giftPremiumButtonWidth + AndroidUtilities.dp(18.0f), height + (this.giftPremiumButtonLayout != null ? staticLayout3.getHeight() : 0) + AndroidUtilities.dp(8.0f));
                    } else {
                        i3 -= AndroidUtilities.dp(40.0f);
                        this.giftPremiumAdditionalHeight -= AndroidUtilities.dp(40.0f);
                        MessageObject messageObject2 = this.currentMessageObject;
                        if (messageObject2 != null && (message = messageObject2.messageOwner) != null && (message.action instanceof TLRPC.TL_messageActionStarGift)) {
                            i3 -= AndroidUtilities.dp(8.0f);
                            this.giftPremiumAdditionalHeight -= AndroidUtilities.dp(8.0f);
                        }
                    }
                    measuredWidth = getMeasuredWidth() << (getMeasuredHeight() + 16);
                    this.starParticlesDrawable.rect.set(this.giftButtonRect);
                    this.starParticlesDrawable.rect2.set(this.giftButtonRect);
                    if (this.starsSize != measuredWidth) {
                        this.starsSize = measuredWidth;
                        this.starParticlesDrawable.resetPositions();
                    }
                    if (isNewStyleButtonLayout()) {
                        int dp7 = this.textY + this.textHeight + AndroidUtilities.dp(4.0f);
                        this.backgroundRectHeight = 0;
                        int dp8 = (AndroidUtilities.dp(16.0f) * 2) + imageSize;
                        this.backgroundRectHeight = dp8;
                        int height2 = dp8 + this.giftPremiumSubtitleLayout.getHeight();
                        this.backgroundRectHeight = height2;
                        float f2 = (this.previousWidth - this.giftPremiumButtonWidth) / 2.0f;
                        if (this.giftPremiumButtonLayout != null) {
                            this.backgroundButtonTop = height2 + dp7 + AndroidUtilities.dp(10.0f);
                            this.giftButtonRect.set(f2 - AndroidUtilities.dp(18.0f), this.backgroundButtonTop, f2 + this.giftPremiumButtonWidth + AndroidUtilities.dp(18.0f), this.backgroundButtonTop + this.giftPremiumButtonLayout.getHeight() + (AndroidUtilities.dp(8.0f) * 2));
                            dp2 = (int) (this.backgroundRectHeight + AndroidUtilities.dp(10.0f) + this.giftButtonRect.height());
                        } else {
                            this.giftButtonRect.set(f2 - AndroidUtilities.dp(18.0f), this.backgroundButtonTop, f2 + this.giftPremiumButtonWidth + AndroidUtilities.dp(18.0f), this.backgroundButtonTop + AndroidUtilities.dp(17.0f) + (AndroidUtilities.dp(8.0f) * 2));
                            dp2 = this.backgroundRectHeight + AndroidUtilities.dp(17.0f);
                        }
                        this.backgroundRectHeight = dp2;
                        int dp9 = this.backgroundRectHeight + AndroidUtilities.dp(16.0f);
                        this.backgroundRectHeight = dp9;
                        i7 = dp7 + dp9 + AndroidUtilities.dp(6.0f);
                    }
                }
                this.giftPremiumAdditionalHeight = i4 + lineBottom;
                int dp522 = this.giftPremiumAdditionalHeight - AndroidUtilities.dp(isGiftChannel ? 14.0f : 0.0f);
                this.giftPremiumAdditionalHeight = dp522;
                i3 += dp522;
                int dp622 = this.textHeight + i3 + AndroidUtilities.dp(14.0f);
                if (this.giftPremiumButtonLayout == null) {
                }
                measuredWidth = getMeasuredWidth() << (getMeasuredHeight() + 16);
                this.starParticlesDrawable.rect.set(this.giftButtonRect);
                this.starParticlesDrawable.rect2.set(this.giftButtonRect);
                if (this.starsSize != measuredWidth) {
                }
                if (isNewStyleButtonLayout()) {
                }
            }
            if (messageObject == null && isNewStyleButtonLayout()) {
                setMeasuredDimension(max, i7);
                return;
            } else {
                setMeasuredDimension(max, this.textHeight + i3 + AndroidUtilities.dp(14.0f));
            }
        }
        i3 = 0;
        if (isButtonLayout(messageObject)) {
        }
        if (messageObject == null) {
        }
        setMeasuredDimension(max, this.textHeight + i3 + AndroidUtilities.dp(14.0f));
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
            }
            i++;
        }
        this.imageReceiver.setImage(this.currentVideoLocation, ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForObject(photoSize, messageObject.photoThumbsObject), "50_50_b", this.avatarDrawable, 0L, null, messageObject, 1);
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
    }

    /*  JADX ERROR: IF instruction can be used only in fallback mode
        jadx.core.utils.exceptions.CodegenException: IF instruction can be used only in fallback mode
        	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:686)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:544)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:302)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:272)
        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.dex.regions.Region.generate(Region.java:35)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
        	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:175)
        	at jadx.core.dex.regions.loops.LoopRegion.generate(LoopRegion.java:171)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.dex.regions.Region.generate(Region.java:35)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:123)
        	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.dex.regions.Region.generate(Region.java:35)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:123)
        	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.dex.regions.Region.generate(Region.java:35)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:123)
        	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.dex.regions.Region.generate(Region.java:35)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.dex.regions.Region.generate(Region.java:35)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.dex.regions.Region.generate(Region.java:35)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:377)
        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:306)
        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:272)
        	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
        	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
        */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00cf, code lost:
        if (r12.backgroundRect.contains(r1, r2) != false) goto L102;
     */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0188  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x01b7  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x022d  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x0298  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0220 A[EDGE_INSN: B:166:0x0220->B:134:0x0220 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:168:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(android.view.MotionEvent r13) {
        /*
            Method dump skipped, instructions count: 669
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatActionCell.onTouchEvent(android.view.MotionEvent):boolean");
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

    public void setMessageObject(MessageObject messageObject) {
        setMessageObject(messageObject, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0201  */
    /* JADX WARN: Removed duplicated region for block: B:102:0x020d  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0172  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0196  */
    /* JADX WARN: Type inference failed for: r25v3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setMessageObject(MessageObject messageObject, boolean z) {
        String str;
        TLRPC.TL_messages_stickerSet stickerSetByName;
        TLRPC.Document document;
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet;
        TLRPC.PhotoSize photoSize;
        BitmapDrawable bitmapDrawable;
        String str2;
        int i;
        String str3;
        ImageReceiver imageReceiver;
        ImageLocation imageLocation;
        ImageLocation imageLocation2;
        String str4;
        long j;
        boolean z2;
        float f;
        RadialProgress2 radialProgress2;
        TLRPC.WallPaper wallPaper;
        TLRPC.MessageAction messageAction;
        ImageReceiver imageReceiver2;
        ImageLocation forDocument;
        StringBuilder sb;
        String str5;
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
            MessageObject messageObject2 = this.currentMessageObject;
            boolean z3 = messageObject2 == null || messageObject2.stableId != messageObject.stableId;
            this.currentMessageObject = messageObject;
            messageObject.forceUpdate = false;
            this.hasReplyMessage = messageObject.replyMessageObject != null;
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            this.previousWidth = 0;
            this.isSpoilerRevealing = false;
            this.giftPremiumSubtitleLayoutSpoilers.clear();
            if (z3) {
                AnimatedEmojiSpan.release(this, this.giftPremiumSubtitleLayoutEmoji);
            }
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
                int i2 = messageObject.type;
                int i3 = 4;
                if (i2 == 22) {
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
                                if (wallPaper == null || (str5 = wallPaper.uploadingImage) == null) {
                                    if (wallPaper != null) {
                                        TLObject tLObject = messageObject.photoThumbsObject;
                                        TLRPC.Document document2 = tLObject instanceof TLRPC.Document ? (TLRPC.Document) tLObject : wallPaper.document;
                                        imageReceiver2 = this.imageReceiver;
                                        forDocument = ImageLocation.getForDocument(document2);
                                        sb = new StringBuilder();
                                    }
                                    this.wallpaperPreviewDrawable = null;
                                } else {
                                    imageReceiver2 = this.imageReceiver;
                                    forDocument = ImageLocation.getForPath(str5);
                                    sb = new StringBuilder();
                                }
                                sb.append("150_150_wallpaper");
                                sb.append(wallPaper.id);
                                sb.append(ChatBackgroundDrawable.hash(wallPaper.settings));
                                imageReceiver2.setImage(forDocument, sb.toString(), null, null, ChatBackgroundDrawable.createThumb(wallPaper), 0L, null, wallPaper, 1);
                                this.wallpaperPreviewDrawable = null;
                            }
                            this.imageReceiver.setRoundRadius((int) (this.stickerSize / 2.0f));
                            if (getUploadingInfoProgress(messageObject) != 1.0f) {
                                this.radialProgress.setProgress(1.0f, !z3);
                                radialProgress2 = this.radialProgress;
                            } else {
                                radialProgress2 = this.radialProgress;
                                i3 = 3;
                            }
                        }
                    }
                    TLRPC.Message message = messageObject.messageOwner;
                    wallPaper = (message == null || (messageAction = message.action) == null) ? null : messageAction.wallpaper;
                    if (TextUtils.isEmpty(ChatThemeController.getWallpaperEmoticon(wallPaper))) {
                    }
                    this.imageReceiver.setRoundRadius((int) (this.stickerSize / 2.0f));
                    if (getUploadingInfoProgress(messageObject) != 1.0f) {
                    }
                } else if (i2 == 21) {
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
                        z2 = false;
                    } else if (closestVideoSizeWithSize != null) {
                        z2 = false;
                        this.imageReceiver.setImage(forPhoto, ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForPhoto(closestPhotoSizeWithSize, photo), "150_150", ImageLocation.getForObject(photoSize2, messageObject.photoThumbsObject), "50_50_b", messageObject.strippedThumb, 0L, null, messageObject, 0);
                    } else {
                        z2 = false;
                        this.imageReceiver.setImage(ImageLocation.getForPhoto(closestPhotoSizeWithSize, photo), "150_150", ImageLocation.getForObject(photoSize2, messageObject.photoThumbsObject), "50_50_b", messageObject.strippedThumb, 0L, null, messageObject, 0);
                    }
                    this.imageReceiver.setAllowStartLottieAnimation(z2);
                    ImageUpdater imageUpdater = MessagesController.getInstance(this.currentAccount).photoSuggestion.get(messageObject.messageOwner.local_id);
                    if (imageUpdater != null) {
                        f = 1.0f;
                        if (imageUpdater.getCurrentImageProgress() != 1.0f) {
                            radialProgress2 = this.radialProgress;
                            i3 = 3;
                        }
                    } else {
                        f = 1.0f;
                    }
                    this.radialProgress.setProgress(f, !z3);
                    radialProgress2 = this.radialProgress;
                    i3 = 4;
                } else if (i2 == 30 || i2 == 18 || i2 == 25) {
                    this.imageReceiver.setRoundRadius(0);
                    TLRPC.MessageAction messageAction2 = messageObject.messageOwner.action;
                    if (messageAction2 instanceof TLRPC.TL_messageActionStarGift) {
                        TL_stars.StarGift starGift = ((TLRPC.TL_messageActionStarGift) messageAction2).gift;
                        document = starGift != null ? starGift.sticker : null;
                        str = null;
                        stickerSetByName = null;
                        tL_messages_stickerSet = messageObject;
                    } else {
                        str = UserConfig.getInstance(this.currentAccount).premiumGiftsStickerPack;
                        if (str == null) {
                            MediaDataController.getInstance(this.currentAccount).checkPremiumGiftStickers();
                            return;
                        }
                        stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByName(str);
                        if (stickerSetByName == null) {
                            stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByEmojiOrName(str);
                        }
                        if (stickerSetByName != null) {
                            TLRPC.MessageAction messageAction3 = messageObject.messageOwner.action;
                            int i6 = messageAction3.months;
                            if (messageObject.type == 30) {
                                long j2 = messageAction3 instanceof TLRPC.TL_messageActionGiftStars ? ((TLRPC.TL_messageActionGiftStars) messageAction3).stars : ((TLRPC.TL_messageActionPrizeStars) messageAction3).stars;
                                String str6 = j2 <= 1000 ? "2⃣" : j2 < 2500 ? "3⃣" : "4⃣";
                                int i7 = 0;
                                while (true) {
                                    if (i7 >= stickerSetByName.packs.size()) {
                                        break;
                                    }
                                    TLRPC.TL_stickerPack tL_stickerPack = stickerSetByName.packs.get(i7);
                                    if (TextUtils.equals(tL_stickerPack.emoticon, str6) && !tL_stickerPack.documents.isEmpty()) {
                                        long longValue = tL_stickerPack.documents.get(0).longValue();
                                        for (int i8 = 0; i8 < stickerSetByName.documents.size(); i8++) {
                                            document = stickerSetByName.documents.get(i8);
                                            if (document != null && document.id == longValue) {
                                                break;
                                            }
                                        }
                                    } else {
                                        i7++;
                                    }
                                }
                                document = null;
                            } else {
                                String str7 = (String) monthsToEmoticon.get(Integer.valueOf(i6));
                                Iterator<TLRPC.TL_stickerPack> it = stickerSetByName.packs.iterator();
                                TLRPC.Document document3 = null;
                                while (it.hasNext()) {
                                    TLRPC.TL_stickerPack next = it.next();
                                    if (Objects.equals(next.emoticon, str7)) {
                                        Iterator<Long> it2 = next.documents.iterator();
                                        while (it2.hasNext()) {
                                            long longValue2 = it2.next().longValue();
                                            Iterator<TLRPC.Document> it3 = stickerSetByName.documents.iterator();
                                            while (true) {
                                                if (!it3.hasNext()) {
                                                    break;
                                                }
                                                TLRPC.Document next2 = it3.next();
                                                if (next2.id == longValue2) {
                                                    document3 = next2;
                                                    break;
                                                }
                                            }
                                            if (document3 != null) {
                                                break;
                                            }
                                        }
                                    }
                                    if (document3 != null) {
                                        break;
                                    }
                                }
                                document = document3;
                            }
                            if (document == null && !stickerSetByName.documents.isEmpty()) {
                                document = stickerSetByName.documents.get(0);
                            }
                            tL_messages_stickerSet = stickerSetByName;
                        } else {
                            document = null;
                            tL_messages_stickerSet = null;
                        }
                    }
                    this.forceWasUnread = messageObject.wasUnread;
                    this.giftSticker = document;
                    if (document != null) {
                        this.imageReceiver.setAllowStartLottieAnimation(true);
                        this.imageReceiver.setDelegate(this.giftStickerDelegate);
                        this.giftEffectAnimation = null;
                        int i9 = 0;
                        while (true) {
                            if (i9 >= document.video_thumbs.size()) {
                                break;
                            } else if ("f".equals(document.video_thumbs.get(i9).type)) {
                                this.giftEffectAnimation = document.video_thumbs.get(i9);
                                break;
                            } else {
                                i9++;
                            }
                        }
                        SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(document, Theme.key_windowBackgroundGray, 0.3f);
                        this.imageReceiver.setAutoRepeat(0);
                        this.imageReceiver.setImage(ImageLocation.getForDocument(document), String.format(Locale.US, "%d_%d_nr_messageId=%d", Integer.valueOf((int) NotificationCenter.audioRouteChanged), Integer.valueOf((int) NotificationCenter.audioRouteChanged), Integer.valueOf(messageObject.stableId)), svgThumb, "tgs", tL_messages_stickerSet, 1);
                    } else if (str != null) {
                        MediaDataController.getInstance(this.currentAccount).loadStickersByEmojiOrName(str, false, stickerSetByName == null);
                    }
                } else if (i2 == 11) {
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
                                str2 = null;
                                i = 1;
                                str3 = ImageLoader.AUTOPLAY_FILTER;
                                str4 = "50_50_b";
                                j = 0;
                            } else {
                                ImageReceiver imageReceiver3 = this.imageReceiver;
                                ImageLocation forObject = ImageLocation.getForObject(closestPhotoSizeWithSize2, messageObject.photoThumbsObject);
                                ImageLocation forObject2 = ImageLocation.getForObject(photoSize, messageObject.photoThumbsObject);
                                bitmapDrawable = messageObject.strippedThumb;
                                str2 = null;
                                i = 1;
                                str3 = "150_150";
                                imageReceiver = imageReceiver3;
                                imageLocation = forObject;
                                imageLocation2 = forObject2;
                                str4 = "50_50_b";
                                j = 0;
                            }
                            imageReceiver.setImage(imageLocation, str3, imageLocation2, str4, bitmapDrawable, j, str2, messageObject, i);
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
                boolean z4 = !z3;
                radialProgress2.setIcon(i3, z4, z4);
            }
            this.rippleView.setVisibility(isButtonLayout(messageObject) ? 0 : 8);
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

    public void setSpoilersSuppressed(boolean z) {
        for (SpoilerEffect spoilerEffect : this.spoilers) {
            spoilerEffect.setSuppressUpdates(z);
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
