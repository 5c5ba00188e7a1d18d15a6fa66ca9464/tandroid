package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
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
import org.telegram.tgnet.TLRPC$ChannelAdminLogEventAction;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEvent;
import org.telegram.tgnet.TLRPC$TL_channelAdminLogEventActionChangeWallpaper;
import org.telegram.tgnet.TLRPC$TL_chatInviteExported;
import org.telegram.tgnet.TLRPC$TL_documentEmpty;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_messageActionGiftCode;
import org.telegram.tgnet.TLRPC$TL_messageActionGiftStars;
import org.telegram.tgnet.TLRPC$TL_messageActionSuggestProfilePhoto;
import org.telegram.tgnet.TLRPC$TL_messageActionUserUpdatedPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_photoStrippedSize;
import org.telegram.tgnet.TLRPC$TL_premiumGiftOption;
import org.telegram.tgnet.TLRPC$TL_stickerPack;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.tgnet.TLRPC$WallPaper;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
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
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
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
    private TLRPC$VideoSize giftEffectAnimation;
    private int giftPremiumAdditionalHeight;
    private StaticLayout giftPremiumButtonLayout;
    private float giftPremiumButtonWidth;
    private StaticLayout giftPremiumSubtitleLayout;
    private int giftPremiumSubtitleWidth;
    private StaticLayout giftPremiumTitleLayout;
    private int giftRectSize;
    private TLRPC$Document giftSticker;
    private ImageReceiver.ImageReceiverDelegate giftStickerDelegate;
    private TextPaint giftSubtitlePaint;
    private TextPaint giftTitlePaint;
    private boolean hasReplyMessage;
    private boolean imagePressed;
    private ImageReceiver imageReceiver;
    private boolean invalidateColors;
    private boolean invalidatePath;
    private View invalidateWithParent;
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

            public static void $default$didOpenPremiumGift(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, TLRPC$TL_premiumGiftOption tLRPC$TL_premiumGiftOption, String str, boolean z) {
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

            public static void $default$needOpenInviteLink(ChatActionCellDelegate chatActionCellDelegate, TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported) {
            }

            public static void $default$needOpenUserProfile(ChatActionCellDelegate chatActionCellDelegate, long j) {
            }

            public static void $default$needShowEffectOverlay(ChatActionCellDelegate chatActionCellDelegate, ChatActionCell chatActionCell, TLRPC$Document tLRPC$Document, TLRPC$VideoSize tLRPC$VideoSize) {
            }
        }

        boolean canDrawOutboundsContent();

        void didClickButton(ChatActionCell chatActionCell);

        void didClickImage(ChatActionCell chatActionCell);

        boolean didLongPress(ChatActionCell chatActionCell, float f, float f2);

        void didOpenPremiumGift(ChatActionCell chatActionCell, TLRPC$TL_premiumGiftOption tLRPC$TL_premiumGiftOption, String str, boolean z);

        void didOpenPremiumGiftChannel(ChatActionCell chatActionCell, String str, boolean z);

        void didPressReplyMessage(ChatActionCell chatActionCell, int i);

        BaseFragment getBaseFragment();

        long getDialogId();

        long getTopicId();

        void needOpenInviteLink(TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported);

        void needOpenUserProfile(long j);

        void needShowEffectOverlay(ChatActionCell chatActionCell, TLRPC$Document tLRPC$Document, TLRPC$VideoSize tLRPC$VideoSize);
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
        this.buttonClickableAsImage = true;
        this.giftTitlePaint = new TextPaint(1);
        this.giftSubtitlePaint = new TextPaint(1);
        this.radialProgress = new RadialProgress2(this);
        this.giftStickerDelegate = new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda1
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

    private void buildLayout() {
        CharSequence charSequence;
        SpannableStringBuilder replaceTags;
        String string;
        int i;
        CharSequence charSequence2;
        boolean z;
        String formatString;
        ArrayList arrayList;
        TLRPC$Photo tLRPC$Photo;
        ArrayList arrayList2;
        String string2;
        String string3;
        CharSequence formatString2;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        int i2;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            charSequence = messageObject.isExpiredStory() ? messageObject.messageOwner.media.user_id != UserConfig.getInstance(this.currentAccount).getClientUserId() ? StoriesUtilities.createExpiredStoryString(true, "ExpiredStoryMention", R.string.ExpiredStoryMention, new Object[0]) : StoriesUtilities.createExpiredStoryString(true, "ExpiredStoryMentioned", R.string.ExpiredStoryMentioned, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())).first_name) : (this.delegate.getTopicId() == 0 && MessageObject.isTopicActionMessage(messageObject)) ? ForumUtilities.createActionTextWithTopic(MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-messageObject.getDialogId(), MessageObject.getTopicId(this.currentAccount, messageObject.messageOwner, true)), messageObject) : null;
            if (charSequence == null) {
                TLRPC$Message tLRPC$Message = messageObject.messageOwner;
                if (tLRPC$Message != null && (tLRPC$MessageMedia = tLRPC$Message.media) != null && tLRPC$MessageMedia.ttl_seconds != 0) {
                    if (tLRPC$MessageMedia.photo != null) {
                        i2 = R.string.AttachPhotoExpired;
                    } else {
                        TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
                        if ((tLRPC$Document instanceof TLRPC$TL_documentEmpty) || ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) && tLRPC$Document == null)) {
                            i2 = tLRPC$MessageMedia.voice ? R.string.AttachVoiceExpired : tLRPC$MessageMedia.round ? R.string.AttachRoundExpired : R.string.AttachVideoExpired;
                        }
                    }
                    charSequence = LocaleController.getString(i2);
                }
                charSequence = AnimatedEmojiSpan.cloneSpans(messageObject.messageText);
            }
        } else {
            charSequence = this.customText;
        }
        createLayout(charSequence, this.previousWidth);
        if (messageObject != null) {
            int i3 = messageObject.type;
            if (i3 == 11) {
                float f = AndroidUtilities.roundMessageSize;
                this.imageReceiver.setImageCoords((this.previousWidth - AndroidUtilities.roundMessageSize) / 2.0f, this.textHeight + AndroidUtilities.dp(19.0f), f, f);
            } else if (i3 == 25) {
                createGiftPremiumChannelLayouts();
            } else {
                if (i3 == 30) {
                    TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.currentMessageObject.getDialogId()));
                    string3 = LocaleController.formatPluralStringComma("ActionGiftStarsTitle", (int) ((TLRPC$TL_messageActionGiftStars) messageObject.messageOwner.action).stars);
                    formatString2 = AndroidUtilities.replaceTags(this.currentMessageObject.isOutOwner() ? LocaleController.formatString(R.string.ActionGiftStarsSubtitle, UserObject.getForcedFirstName(user)) : LocaleController.getString(R.string.ActionGiftStarsSubtitleYou));
                    string2 = LocaleController.getString(R.string.ActionGiftStarsView);
                } else if (i3 != 18) {
                    if (i3 == 21) {
                        TLRPC$TL_messageActionSuggestProfilePhoto tLRPC$TL_messageActionSuggestProfilePhoto = (TLRPC$TL_messageActionSuggestProfilePhoto) messageObject.messageOwner.action;
                        TLRPC$User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.isOutOwner() ? 0L : messageObject.getDialogId()));
                        boolean z2 = tLRPC$TL_messageActionSuggestProfilePhoto.video || !((tLRPC$Photo = tLRPC$TL_messageActionSuggestProfilePhoto.photo) == null || (arrayList2 = tLRPC$Photo.video_sizes) == null || arrayList2.isEmpty());
                        if (user2.id == UserConfig.getInstance(this.currentAccount).clientUserId) {
                            TLRPC$User user3 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId()));
                            formatString = z2 ? LocaleController.formatString(R.string.ActionSuggestVideoFromYouDescription, user3.first_name) : LocaleController.formatString(R.string.ActionSuggestPhotoFromYouDescription, user3.first_name);
                        } else {
                            formatString = z2 ? LocaleController.formatString(R.string.ActionSuggestVideoToYouDescription, user2.first_name) : LocaleController.formatString(R.string.ActionSuggestPhotoToYouDescription, user2.first_name);
                        }
                        createGiftPremiumLayouts(null, formatString, LocaleController.getString((tLRPC$TL_messageActionSuggestProfilePhoto.video || !((arrayList = tLRPC$TL_messageActionSuggestProfilePhoto.photo.video_sizes) == null || arrayList.isEmpty())) ? R.string.ViewVideoAction : R.string.ViewPhotoAction), this.giftRectSize, true);
                    } else {
                        if (i3 == 22) {
                            TLRPC$User user4 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.isOutOwner() ? 0L : messageObject.getDialogId()));
                            if (messageObject.getDialogId() >= 0) {
                                if (!messageObject.isOutOwner() && messageObject.isWallpaperForBoth() && messageObject.isCurrentWallpaper()) {
                                    replaceTags = messageObject.messageText;
                                    string = LocaleController.getString(R.string.RemoveWallpaperAction);
                                    z = false;
                                    i = this.giftRectSize;
                                    charSequence2 = null;
                                } else if (user4 == null || user4.id != UserConfig.getInstance(this.currentAccount).clientUserId) {
                                    replaceTags = messageObject.messageText;
                                    string = LocaleController.getString(R.string.ViewWallpaperAction);
                                    z = true;
                                    i = this.giftRectSize;
                                    charSequence2 = null;
                                }
                            }
                            replaceTags = messageObject.messageText;
                            string = null;
                            z = true;
                            i = this.giftRectSize;
                            charSequence2 = null;
                        } else if (!messageObject.isStoryMention()) {
                            return;
                        } else {
                            TLRPC$User user5 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.messageOwner.media.user_id));
                            replaceTags = AndroidUtilities.replaceTags(user5.self ? LocaleController.formatString("StoryYouMentionedTitle", R.string.StoryYouMentionedTitle, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())).first_name) : LocaleController.formatString("StoryMentionedTitle", R.string.StoryMentionedTitle, user5.first_name));
                            string = LocaleController.getString(R.string.StoryMentionedAction);
                            i = this.giftRectSize;
                            charSequence2 = null;
                            z = true;
                        }
                        createGiftPremiumLayouts(charSequence2, replaceTags, string, i, z);
                    }
                    this.textLayout = null;
                    this.textHeight = 0;
                    this.textY = 0;
                    return;
                } else {
                    string2 = LocaleController.getString((!isGiftCode() || isSelfGiftCode()) ? R.string.ActionGiftPremiumView : R.string.GiftPremiumUseGiftBtn);
                    string3 = LocaleController.getString(R.string.ActionGiftPremiumTitle);
                    formatString2 = LocaleController.formatString(R.string.ActionGiftPremiumSubtitle, LocaleController.formatPluralString("Months", messageObject.messageOwner.action.months, new Object[0]));
                }
                createGiftPremiumLayouts(string3, formatString2, string2, this.giftRectSize, true);
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
        int dp = this.giftRectSize - AndroidUtilities.dp(16.0f);
        this.giftTitlePaint.setTextSize(AndroidUtilities.dp(14.0f));
        this.giftSubtitlePaint.setTextSize(AndroidUtilities.dp(13.0f));
        TLRPC$TL_messageActionGiftCode tLRPC$TL_messageActionGiftCode = (TLRPC$TL_messageActionGiftCode) this.currentMessageObject.messageOwner.action;
        int i2 = tLRPC$TL_messageActionGiftCode.months;
        TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-DialogObject.getPeerDialogId(tLRPC$TL_messageActionGiftCode.boost_peer)));
        String str2 = chat == null ? null : chat.title;
        boolean z = tLRPC$TL_messageActionGiftCode.via_giveaway;
        if (tLRPC$TL_messageActionGiftCode.unclaimed) {
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
        } else if (tLRPC$TL_messageActionGiftCode.unclaimed) {
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
        this.giftPremiumSubtitleLayout = new StaticLayout(spannableStringBuilder, this.giftSubtitlePaint, dp, alignment, 1.1f, 0.0f, false);
        SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(string2);
        valueOf2.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, valueOf2.length(), 33);
        StaticLayout staticLayout = new StaticLayout(valueOf2, (TextPaint) getThemedPaint("paintChatActionText"), dp, alignment, 1.0f, 0.0f, false);
        this.giftPremiumButtonLayout = staticLayout;
        this.buttonClickableAsImage = true;
        this.giftPremiumButtonWidth = measureLayoutWidth(staticLayout);
    }

    private void createGiftPremiumLayouts(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, boolean z) {
        TextPaint textPaint;
        float f;
        float f2;
        int cutInFancyHalf;
        CharSequence charSequence4 = charSequence2;
        int dp = i - AndroidUtilities.dp(16.0f);
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
        if (this.currentMessageObject == null || !(isNewStyleButtonLayout() || this.currentMessageObject.type == 30)) {
            textPaint = this.giftSubtitlePaint;
            f = 15.0f;
        } else {
            textPaint = this.giftSubtitlePaint;
            f = 13.0f;
        }
        textPaint.setTextSize(AndroidUtilities.dp(f));
        this.giftPremiumSubtitleWidth = dp;
        MessageObject messageObject3 = this.currentMessageObject;
        int i2 = (messageObject3 == null || messageObject3.type != 22 || messageObject3.getDialogId() < 0 || (cutInFancyHalf = HintView2.cutInFancyHalf(charSequence4, this.giftSubtitlePaint)) >= dp || ((float) cutInFancyHalf) <= ((float) dp) / 5.0f) ? dp : cutInFancyHalf;
        try {
            charSequence4 = Emoji.replaceEmoji(charSequence4, this.giftSubtitlePaint.getFontMetricsInt(), false);
        } catch (Exception unused) {
        }
        CharSequence charSequence5 = charSequence4;
        TextPaint textPaint2 = this.giftSubtitlePaint;
        Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
        this.giftPremiumSubtitleLayout = new StaticLayout(charSequence5, textPaint2, i2, alignment, 1.0f, AndroidUtilities.dp(1.66f), false);
        if (charSequence3 != null) {
            SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(charSequence3);
            valueOf2.setSpan(new TypefaceSpan(AndroidUtilities.bold()), 0, valueOf2.length(), 33);
            StaticLayout staticLayout = new StaticLayout(valueOf2, (TextPaint) getThemedPaint("paintChatActionText"), dp, alignment, 1.0f, 0.0f, false);
            this.giftPremiumButtonLayout = staticLayout;
            this.buttonClickableAsImage = z;
            f2 = measureLayoutWidth(staticLayout);
        } else {
            this.giftPremiumButtonLayout = null;
            this.buttonClickableAsImage = false;
            f2 = 0.0f;
        }
        this.giftPremiumButtonWidth = f2;
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
        return messageObject != null && (messageObject.messageOwner.action instanceof TLRPC$TL_messageActionGiftCode);
    }

    private boolean isNewStyleButtonLayout() {
        MessageObject messageObject = this.currentMessageObject;
        int i = messageObject.type;
        return i == 21 || i == 22 || messageObject.isStoryMention();
    }

    private boolean isSelfGiftCode() {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            TLRPC$Message tLRPC$Message = messageObject.messageOwner;
            TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
            if (((tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftCode) || (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGiftStars)) && (tLRPC$Message.from_id instanceof TLRPC$TL_peerUser)) {
                return UserObject.isUserSelf(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.currentMessageObject.messageOwner.from_id.user_id)));
            }
            return false;
        }
        return false;
    }

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
        AndroidUtilities.runOnUIThread(new ChatActionCell$$ExternalSyntheticLambda4(lottieAnimation));
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
            TLRPC$VideoSize tLRPC$VideoSize = this.giftEffectAnimation;
            if (tLRPC$VideoSize == null || (chatActionCellDelegate = this.delegate) == null) {
                return;
            }
            chatActionCellDelegate.needShowEffectOverlay(this, this.giftSticker, tLRPC$VideoSize);
        }
    }

    public /* synthetic */ void lambda$openPremiumGiftChannel$1(TLRPC$TL_messageActionGiftCode tLRPC$TL_messageActionGiftCode) {
        this.delegate.didOpenPremiumGiftChannel(this, tLRPC$TL_messageActionGiftCode.slug, false);
    }

    public /* synthetic */ void lambda$openPremiumGiftPreview$2(TLRPC$TL_premiumGiftOption tLRPC$TL_premiumGiftOption, String str) {
        this.delegate.didOpenPremiumGift(this, tLRPC$TL_premiumGiftOption, str, false);
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

    public void openLink(CharacterStyle characterStyle) {
        if (this.delegate == null || !(characterStyle instanceof URLSpan)) {
            return;
        }
        String url = ((URLSpan) characterStyle).getURL();
        if (url.startsWith("topic")) {
            URLSpan uRLSpan = this.pressedLink;
            if (uRLSpan instanceof URLSpanNoUnderline) {
                TLObject object = ((URLSpanNoUnderline) uRLSpan).getObject();
                if (object instanceof TLRPC$TL_forumTopic) {
                    ForumUtilities.openTopic(this.delegate.getBaseFragment(), -this.delegate.getDialogId(), (TLRPC$TL_forumTopic) object, 0);
                    return;
                }
                return;
            }
        }
        if (url.startsWith("invite")) {
            URLSpan uRLSpan2 = this.pressedLink;
            if (uRLSpan2 instanceof URLSpanNoUnderline) {
                TLObject object2 = ((URLSpanNoUnderline) uRLSpan2).getObject();
                if (object2 instanceof TLRPC$TL_chatInviteExported) {
                    this.delegate.needOpenInviteLink((TLRPC$TL_chatInviteExported) object2);
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
            final TLRPC$TL_messageActionGiftCode tLRPC$TL_messageActionGiftCode = (TLRPC$TL_messageActionGiftCode) this.currentMessageObject.messageOwner.action;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActionCell.this.lambda$openPremiumGiftChannel$1(tLRPC$TL_messageActionGiftCode);
                }
            });
        }
    }

    private void openPremiumGiftPreview() {
        final TLRPC$TL_premiumGiftOption tLRPC$TL_premiumGiftOption = new TLRPC$TL_premiumGiftOption();
        TLRPC$MessageAction tLRPC$MessageAction = this.currentMessageObject.messageOwner.action;
        tLRPC$TL_premiumGiftOption.amount = tLRPC$MessageAction.amount;
        tLRPC$TL_premiumGiftOption.months = tLRPC$MessageAction.months;
        tLRPC$TL_premiumGiftOption.currency = tLRPC$MessageAction.currency;
        final String str = (!isGiftCode() || isSelfGiftCode()) ? null : ((TLRPC$TL_messageActionGiftCode) this.currentMessageObject.messageOwner.action).slug;
        if (this.delegate != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActionCell.this.lambda$openPremiumGiftPreview$2(tLRPC$TL_premiumGiftOption, str);
                }
            });
        }
    }

    private void openStarsGiftTransaction() {
        TLRPC$Message tLRPC$Message;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || (tLRPC$Message = messageObject.messageOwner) == null || !(tLRPC$Message.action instanceof TLRPC$TL_messageActionGiftStars)) {
            return;
        }
        Context context = getContext();
        int i = this.currentAccount;
        TLRPC$Message tLRPC$Message2 = this.currentMessageObject.messageOwner;
        StarsIntroActivity.showTransactionSheet(context, i, tLRPC$Message2.date, tLRPC$Message2.from_id, tLRPC$Message2.peer_id, (TLRPC$TL_messageActionGiftStars) tLRPC$Message2.action, this.avatarStoryParams.resourcesProvider);
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
            if (i == NotificationCenter.didUpdatePremiumGiftStickers) {
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

    /* JADX WARN: Code restructure failed: missing block: B:343:0x0400, code lost:
        if (isFloating() != false) goto L150;
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x0428, code lost:
        if (isFloating() != false) goto L150;
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x042b, code lost:
        r1 = 1.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x042d, code lost:
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
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdatePremiumGiftStickers);
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
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdatePremiumGiftStickers);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.diceStickersDidLoad);
        this.avatarStoryParams.onDetachFromWindow();
    }

    /* JADX WARN: Code restructure failed: missing block: B:269:0x0249, code lost:
        if (r1.getCurrentImageProgress() == 1.0f) goto L200;
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x027a, code lost:
        if (r1 == 1.0f) goto L200;
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x027c, code lost:
        r33.radialProgress.setIcon(4, true, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x0282, code lost:
        r33.radialProgress.setIcon(3, true, true);
     */
    /* JADX WARN: Removed duplicated region for block: B:364:0x0613  */
    /* JADX WARN: Removed duplicated region for block: B:367:0x061f  */
    /* JADX WARN: Removed duplicated region for block: B:370:0x062c  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x0640  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x0672  */
    /* JADX WARN: Removed duplicated region for block: B:393:0x0710  */
    /* JADX WARN: Removed duplicated region for block: B:397:0x071f  */
    /* JADX WARN: Removed duplicated region for block: B:402:0x0737  */
    /* JADX WARN: Removed duplicated region for block: B:408:0x0788  */
    /* JADX WARN: Removed duplicated region for block: B:413:0x07ce  */
    /* JADX WARN: Removed duplicated region for block: B:417:0x0825  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        int i;
        float f;
        float f2;
        int i2;
        float dp;
        float dp2;
        float f3;
        int dp3;
        StaticLayout staticLayout;
        Theme.ResourcesProvider resourcesProvider;
        boolean z;
        float clamp;
        StaticLayout staticLayout2;
        float f4;
        float f5;
        int i3;
        MessageObject messageObject = this.currentMessageObject;
        int i4 = this.stickerSize;
        if (isButtonLayout(messageObject)) {
            this.stickerSize = this.giftRectSize - AndroidUtilities.dp(106.0f);
            if (isNewStyleButtonLayout()) {
                i4 = getImageSize(messageObject);
                float f6 = (this.previousWidth - i4) / 2.0f;
                float dp4 = this.textY + this.textHeight + AndroidUtilities.dp(4.0f) + AndroidUtilities.dp(16.0f);
                if (messageObject.isStoryMention()) {
                    this.avatarStoryParams.storyItem = messageObject.messageOwner.media.storyItem;
                }
                float f7 = i4;
                this.avatarStoryParams.originalAvatarRect.set(f6, dp4, f6 + f7, dp4 + f7);
                this.imageReceiver.setImageCoords(f6, dp4, f7, f7);
            } else {
                int i5 = messageObject.type;
                if (i5 == 11) {
                    ImageReceiver imageReceiver = this.imageReceiver;
                    int i6 = this.previousWidth;
                    float f8 = this.stickerSize;
                    imageReceiver.setImageCoords((i6 - i3) / 2.0f, this.textY + this.textHeight + (this.giftRectSize * 0.075f), f8, f8);
                } else {
                    if (i5 == 25) {
                        f4 = this.stickerSize;
                        f5 = AndroidUtilities.isTablet() ? 1.0f : 1.2f;
                    } else if (i5 == 30) {
                        f4 = this.stickerSize;
                        f5 = 1.1f;
                    } else {
                        i4 = (int) (this.stickerSize * 1.0f);
                        float f9 = i4;
                        this.imageReceiver.setImageCoords((this.previousWidth - i4) / 2.0f, ((this.textY + this.textHeight) + (this.giftRectSize * 0.075f)) - AndroidUtilities.dp(4.0f), f9, f9);
                    }
                    int i7 = (int) (f4 * f5);
                    float f10 = i7;
                    this.imageReceiver.setImageCoords((this.previousWidth - i7) / 2.0f, ((this.textY + this.textHeight) + (this.giftRectSize * 0.075f)) - AndroidUtilities.dp(22.0f), f10, f10);
                    i4 = i7;
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
        int i8 = i4;
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
                TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
                long j = tLRPC$MessageMedia.user_id;
                StoriesUtilities.AvatarStoryParams avatarStoryParams = this.avatarStoryParams;
                avatarStoryParams.storyId = tLRPC$MessageMedia.id;
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
            i2 = 3;
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
                StaticLayout staticLayout3 = this.textLayout;
                i2 = 3;
                f = 2.0f;
                i = i8;
                f2 = 16.0f;
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout3, this.animatedEmojiStack, 0.0f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f, staticLayout3 == null ? null : getAdaptiveEmojiColorFilter(staticLayout3.getPaint().getColor()));
            } else {
                i = i8;
                f = 2.0f;
                f2 = 16.0f;
                i2 = 3;
            }
            canvas.restore();
            for (SpoilerEffect spoilerEffect : this.spoilers) {
                spoilerEffect.setColor(this.textLayout.getPaint().getColor());
                spoilerEffect.draw(canvas);
            }
            canvas.restore();
        }
        if (isButtonLayout(messageObject)) {
            canvas.save();
            float dp5 = ((this.previousWidth - this.giftRectSize) / f) + AndroidUtilities.dp(8.0f);
            if (isNewStyleButtonLayout()) {
                RectF rectF = this.backgroundRect;
                dp = (rectF != null ? rectF.top : this.textY + this.textHeight + AndroidUtilities.dp(4.0f)) + (AndroidUtilities.dp(f2) * 2) + i;
            } else {
                dp = this.textY + this.textHeight + (this.giftRectSize * 0.075f) + (messageObject.type == 21 ? i : this.stickerSize) + AndroidUtilities.dp(4.0f);
                if (messageObject.type == 21) {
                    dp += AndroidUtilities.dp(f2);
                }
                if (messageObject.type == 30) {
                    dp -= AndroidUtilities.dp(3.66f);
                }
                if (this.giftPremiumButtonLayout == null) {
                    dp -= AndroidUtilities.dp(24.0f);
                }
            }
            canvas.translate(dp5, dp);
            if (this.giftPremiumTitleLayout != null) {
                canvas.save();
                canvas.translate(((this.giftRectSize - AndroidUtilities.dp(f2)) - this.giftPremiumTitleLayout.getWidth()) / f, 0.0f);
                this.giftPremiumTitleLayout.draw(canvas);
                canvas.restore();
                dp2 = dp + this.giftPremiumTitleLayout.getHeight() + AndroidUtilities.dp(messageObject.type == 25 ? 6.0f : 0.0f);
            } else {
                dp2 = dp - AndroidUtilities.dp(4.0f);
            }
            canvas.restore();
            canvas.save();
            canvas.translate(dp5, dp2 + AndroidUtilities.dp(4.0f));
            if (messageObject.type != 22) {
                f3 = 1.0f;
                if (this.giftPremiumSubtitleLayout != null) {
                    canvas.save();
                    dp3 = this.giftRectSize - AndroidUtilities.dp(f2);
                    canvas.translate((dp3 - this.giftPremiumSubtitleLayout.getWidth()) / f, 0.0f);
                    staticLayout2 = this.giftPremiumSubtitleLayout;
                }
                canvas.restore();
                if (this.giftPremiumTitleLayout == null) {
                }
                this.giftPremiumSubtitleLayout.getHeight();
                staticLayout = this.giftPremiumButtonLayout;
                if (staticLayout != null) {
                }
                getHeight();
                AndroidUtilities.dp(8.0f);
                resourcesProvider = this.themeDelegate;
                if (resourcesProvider != null) {
                }
                float scale = this.bounce.getScale(0.02f);
                canvas.save();
                canvas.scale(scale, scale, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                if (this.giftPremiumButtonLayout != null) {
                }
                z = messageObject.settingAvatar;
                if (z) {
                }
                if (!z) {
                }
                clamp = Utilities.clamp(this.progressToProgress, f3, 0.0f);
                this.progressToProgress = clamp;
                if (clamp != 0.0f) {
                }
                if (this.progressToProgress != f3) {
                }
                if (messageObject.flickerLoading) {
                }
                canvas.restore();
            }
            f3 = 1.0f;
            if (this.radialProgress.getTransitionProgress() == 1.0f && this.radialProgress.getIcon() == 4) {
                canvas.save();
                dp3 = this.giftPremiumSubtitleWidth;
                canvas.translate((dp3 - this.giftPremiumSubtitleLayout.getWidth()) / f, 0.0f);
                staticLayout2 = this.giftPremiumSubtitleLayout;
            } else {
                if (this.settingWallpaperLayout == null) {
                    TextPaint textPaint4 = new TextPaint();
                    this.settingWallpaperPaint = textPaint4;
                    textPaint4.setTextSize(AndroidUtilities.dp(13.0f));
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString(R.string.ActionSettingWallpaper));
                    int indexOf = spannableStringBuilder.toString().indexOf("...");
                    if (indexOf < 0) {
                        indexOf = spannableStringBuilder.toString().indexOf("…");
                        i2 = 1;
                    }
                    if (indexOf >= 0) {
                        SpannableString spannableString = new SpannableString("…");
                        UploadingDotsSpannable uploadingDotsSpannable = new UploadingDotsSpannable();
                        uploadingDotsSpannable.fixTop = true;
                        uploadingDotsSpannable.setParent(this, false);
                        spannableString.setSpan(uploadingDotsSpannable, 0, spannableString.length(), 33);
                        spannableStringBuilder.replace(indexOf, indexOf + i2, (CharSequence) spannableString);
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
                    float f11 = 1.0f - transitionProgress;
                    this.settingWallpaperPaint.setAlpha((int) (Color.alpha(color) * f11));
                    this.giftSubtitlePaint.setAlpha((int) (Color.alpha(color) * transitionProgress));
                    TextPaint textPaint5 = this.giftSubtitlePaint;
                    textPaint5.linkColor = textPaint5.getColor();
                    float f12 = (transitionProgress * 0.2f) + 0.8f;
                    canvas.save();
                    canvas.scale(f12, f12, this.giftPremiumSubtitleWidth / f, this.giftPremiumSubtitleLayout.getHeight() / f);
                    canvas.translate((this.giftPremiumSubtitleWidth - this.giftPremiumSubtitleLayout.getWidth()) / f, 0.0f);
                    SpoilerEffect.layoutDrawMaybe(this.giftPremiumSubtitleLayout, canvas);
                    canvas.restore();
                    this.giftSubtitlePaint.setAlpha((int) (Color.alpha(color) * f11));
                    TextPaint textPaint6 = this.giftSubtitlePaint;
                    textPaint6.linkColor = textPaint6.getColor();
                    float f13 = (f11 * 0.2f) + 0.8f;
                    canvas.save();
                    canvas.scale(f13, f13, this.settingWallpaperLayout.getWidth() / f, this.settingWallpaperLayout.getHeight() / f);
                    SpoilerEffect.layoutDrawMaybe(this.settingWallpaperLayout, canvas);
                    canvas.restore();
                    canvas.save();
                    canvas.translate(0.0f, this.settingWallpaperLayout.getHeight() + AndroidUtilities.dp(4.0f));
                    canvas.scale(f13, f13, this.settingWallpaperProgressTextLayout.getWidth() / f, this.settingWallpaperProgressTextLayout.getHeight() / f);
                    SpoilerEffect.layoutDrawMaybe(this.settingWallpaperProgressTextLayout, canvas);
                    canvas.restore();
                    this.giftSubtitlePaint.setColor(color);
                    this.giftSubtitlePaint.linkColor = color;
                    canvas.restore();
                    if (this.giftPremiumTitleLayout == null) {
                        AndroidUtilities.dp(8.0f);
                    }
                    this.giftPremiumSubtitleLayout.getHeight();
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
                        float f14 = this.progressToProgress;
                        if (f14 != f3) {
                            this.progressToProgress = f14 + 0.10666667f;
                            clamp = Utilities.clamp(this.progressToProgress, f3, 0.0f);
                            this.progressToProgress = clamp;
                            if (clamp != 0.0f) {
                                if (this.progressView == null) {
                                    this.progressView = new RadialProgressView(getContext());
                                }
                                int dp6 = AndroidUtilities.dp(f2);
                                canvas.save();
                                float f15 = this.progressToProgress;
                                canvas.scale(f15, f15, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                                this.progressView.setSize(dp6);
                                this.progressView.setProgressColor(Theme.getColor(Theme.key_chat_serviceText));
                                this.progressView.draw(canvas, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                                canvas.restore();
                            }
                            if (this.progressToProgress != f3 && this.giftPremiumButtonLayout != null) {
                                canvas.save();
                                float f16 = f3 - this.progressToProgress;
                                canvas.scale(f16, f16, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                                canvas.translate(dp5, this.giftButtonRect.top + AndroidUtilities.dp(8.0f));
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
                                    this.loadingDrawable.setColors(Theme.multAlpha(-1, 0.08f), Theme.multAlpha(-1, 0.2f), Theme.multAlpha(-1, 0.2f), Theme.multAlpha(-1, 0.7f));
                                    this.loadingDrawable.strokePaint.setStrokeWidth(AndroidUtilities.dp(f3));
                                }
                                this.loadingDrawable.resetDisappear();
                                this.loadingDrawable.setBounds(this.giftButtonRect);
                                this.loadingDrawable.setRadiiDp(16.0f);
                                this.loadingDrawable.draw(canvas);
                            }
                            canvas.restore();
                        }
                    }
                    if (!z) {
                        float f17 = this.progressToProgress;
                        if (f17 != 0.0f) {
                            this.progressToProgress = f17 - 0.10666667f;
                        }
                    }
                    clamp = Utilities.clamp(this.progressToProgress, f3, 0.0f);
                    this.progressToProgress = clamp;
                    if (clamp != 0.0f) {
                    }
                    if (this.progressToProgress != f3) {
                        canvas.save();
                        float f162 = f3 - this.progressToProgress;
                        canvas.scale(f162, f162, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                        canvas.translate(dp5, this.giftButtonRect.top + AndroidUtilities.dp(8.0f));
                        canvas.translate(((this.giftRectSize - AndroidUtilities.dp(f2)) - this.giftPremiumButtonLayout.getWidth()) / f, 0.0f);
                        this.giftPremiumButtonLayout.draw(canvas);
                        canvas.restore();
                    }
                    if (messageObject.flickerLoading) {
                    }
                    canvas.restore();
                }
                this.settingWallpaperLayout.draw(canvas);
                canvas.save();
                canvas.translate(0.0f, this.settingWallpaperLayout.getHeight() + AndroidUtilities.dp(4.0f));
                staticLayout2 = this.settingWallpaperProgressTextLayout;
            }
            SpoilerEffect.layoutDrawMaybe(staticLayout2, canvas);
            canvas.restore();
            canvas.restore();
            if (this.giftPremiumTitleLayout == null) {
            }
            this.giftPremiumSubtitleLayout.getHeight();
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
            clamp = Utilities.clamp(this.progressToProgress, f3, 0.0f);
            this.progressToProgress = clamp;
            if (clamp != 0.0f) {
            }
            if (this.progressToProgress != f3) {
            }
            if (messageObject.flickerLoading) {
            }
            canvas.restore();
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
                    {
                        ChatActionCell.this = this;
                    }

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

    /* JADX WARN: Removed duplicated region for block: B:124:0x00dc  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x019d  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x01b9  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x023d  */
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
        int measuredWidth;
        StaticLayout staticLayout3;
        int i5;
        int dp2;
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
                dp2 = AndroidUtilities.dp(10.0f);
            } else if (isButtonLayout(messageObject)) {
                i5 = this.giftRectSize;
                dp2 = AndroidUtilities.dp(12.0f);
            }
            i3 = i5 + dp2;
            if (isButtonLayout(messageObject)) {
                boolean isGiftChannel = isGiftChannel(messageObject);
                int imageSize = getImageSize(messageObject);
                float dp3 = isNewStyleButtonLayout() ? this.textY + this.textHeight + AndroidUtilities.dp(4.0f) + (AndroidUtilities.dp(16.0f) * 2) + imageSize + this.giftPremiumSubtitleLayout.getHeight() + AndroidUtilities.dp(4.0f) : this.textY + this.textHeight + (this.giftRectSize * 0.075f) + imageSize + AndroidUtilities.dp(4.0f) + AndroidUtilities.dp(4.0f) + this.giftPremiumSubtitleLayout.getHeight();
                this.giftPremiumAdditionalHeight = 0;
                if (this.giftPremiumTitleLayout != null) {
                    dp = dp3 + staticLayout.getHeight() + AndroidUtilities.dp(isGiftChannel ? 6.0f : 0.0f);
                } else {
                    dp = dp3 - AndroidUtilities.dp(12.0f);
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
                    int dp4 = this.giftPremiumAdditionalHeight - AndroidUtilities.dp(isGiftChannel ? 14.0f : 0.0f);
                    this.giftPremiumAdditionalHeight = dp4;
                    i3 += dp4;
                    int dp5 = this.textHeight + i3 + AndroidUtilities.dp(14.0f);
                    if (this.giftPremiumButtonLayout == null) {
                        float height = dp + ((((dp5 - dp) - staticLayout2.getHeight()) - AndroidUtilities.dp(8.0f)) / 2.0f);
                        float f = (this.previousWidth - this.giftPremiumButtonWidth) / 2.0f;
                        this.giftButtonRect.set(f - AndroidUtilities.dp(18.0f), height - AndroidUtilities.dp(8.0f), f + this.giftPremiumButtonWidth + AndroidUtilities.dp(18.0f), height + (this.giftPremiumButtonLayout != null ? staticLayout3.getHeight() : 0) + AndroidUtilities.dp(8.0f));
                    } else {
                        i3 -= AndroidUtilities.dp(40.0f);
                        this.giftPremiumAdditionalHeight -= AndroidUtilities.dp(40.0f);
                    }
                    measuredWidth = getMeasuredWidth() << (getMeasuredHeight() + 16);
                    this.starParticlesDrawable.rect.set(this.giftButtonRect);
                    this.starParticlesDrawable.rect2.set(this.giftButtonRect);
                    if (this.starsSize != measuredWidth) {
                        this.starsSize = measuredWidth;
                        this.starParticlesDrawable.resetPositions();
                    }
                    if (isNewStyleButtonLayout()) {
                        int dp6 = this.textY + this.textHeight + AndroidUtilities.dp(4.0f);
                        this.backgroundRectHeight = 0;
                        int dp7 = (AndroidUtilities.dp(16.0f) * 2) + imageSize;
                        this.backgroundRectHeight = dp7;
                        int height2 = dp7 + this.giftPremiumSubtitleLayout.getHeight();
                        this.backgroundRectHeight = height2;
                        if (this.giftPremiumButtonLayout != null) {
                            this.backgroundButtonTop = height2 + dp6 + AndroidUtilities.dp(10.0f);
                            float f2 = (this.previousWidth - this.giftPremiumButtonWidth) / 2.0f;
                            this.giftButtonRect.set(f2 - AndroidUtilities.dp(18.0f), this.backgroundButtonTop, f2 + this.giftPremiumButtonWidth + AndroidUtilities.dp(18.0f), this.backgroundButtonTop + this.giftPremiumButtonLayout.getHeight() + (AndroidUtilities.dp(8.0f) * 2));
                            this.backgroundRectHeight = (int) (this.backgroundRectHeight + AndroidUtilities.dp(10.0f) + this.giftButtonRect.height());
                        }
                        int dp8 = this.backgroundRectHeight + AndroidUtilities.dp(16.0f);
                        this.backgroundRectHeight = dp8;
                        i7 = dp6 + dp8 + AndroidUtilities.dp(6.0f);
                    }
                }
                this.giftPremiumAdditionalHeight = i4 + lineBottom;
                int dp42 = this.giftPremiumAdditionalHeight - AndroidUtilities.dp(isGiftChannel ? 14.0f : 0.0f);
                this.giftPremiumAdditionalHeight = dp42;
                i3 += dp42;
                int dp52 = this.textHeight + i3 + AndroidUtilities.dp(14.0f);
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
        TLRPC$PhotoSize tLRPC$PhotoSize;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || messageObject.type != 11) {
            return;
        }
        int size = messageObject.photoThumbs.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                tLRPC$PhotoSize = null;
                break;
            }
            tLRPC$PhotoSize = messageObject.photoThumbs.get(i);
            if (tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) {
                break;
            }
            i++;
        }
        this.imageReceiver.setImage(this.currentVideoLocation, ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForObject(tLRPC$PhotoSize, messageObject.photoThumbsObject), "50_50_b", this.avatarDrawable, 0L, null, messageObject, 1);
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
    }

    /* JADX WARN: Code restructure failed: missing block: B:201:0x00cf, code lost:
        if (r12.backgroundRect.contains(r1, r2) != false) goto L77;
     */
    /* JADX WARN: Removed duplicated region for block: B:252:0x0188  */
    /* JADX WARN: Removed duplicated region for block: B:260:0x019c  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x020d  */
    /* JADX WARN: Removed duplicated region for block: B:287:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ImageUpdater imageUpdater;
        boolean z;
        StaticLayout staticLayout;
        int i;
        int i2;
        int i3;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null) {
            return super.onTouchEvent(motionEvent);
        }
        float x = motionEvent.getX();
        this.lastTouchX = x;
        float y = motionEvent.getY();
        this.lastTouchY = y;
        boolean z2 = true;
        if (motionEvent.getAction() != 0) {
            if (motionEvent.getAction() != 2) {
                cancelCheckLongPress();
            }
            if (this.giftButtonPressed) {
                int action = motionEvent.getAction();
                if (action == 1) {
                    this.imagePressed = false;
                    View view = this.rippleView;
                    this.giftButtonPressed = false;
                    view.setPressed(false);
                    this.bounce.setPressed(false);
                    if (this.delegate != null) {
                        int i4 = messageObject.type;
                        if (i4 == 25) {
                            playSoundEffect(0);
                            openPremiumGiftChannel();
                        } else if (i4 == 18) {
                            playSoundEffect(0);
                            openPremiumGiftPreview();
                        } else if (i4 == 30) {
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
                } else if (action == 2) {
                    if (isButtonLayout(messageObject)) {
                        if (!this.giftButtonRect.contains(x, y)) {
                        }
                    }
                    View view2 = this.rippleView;
                    this.giftButtonPressed = false;
                    view2.setPressed(false);
                    this.bounce.setPressed(false);
                } else if (action == 3) {
                    this.imagePressed = false;
                    View view22 = this.rippleView;
                    this.giftButtonPressed = false;
                    view22.setPressed(false);
                    this.bounce.setPressed(false);
                }
            } else if (this.imagePressed) {
                int action2 = motionEvent.getAction();
                if (action2 == 1) {
                    this.imagePressed = false;
                    int i5 = messageObject.type;
                    if (i5 != 25) {
                        if (i5 != 18) {
                            if (i5 != 30) {
                                if (this.delegate != null) {
                                    if (i5 != 21 || (imageUpdater = MessagesController.getInstance(this.currentAccount).photoSuggestion.get(messageObject.messageOwner.local_id)) == null) {
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
                } else if (action2 == 2 ? !(!isNewStyleButtonLayout() ? this.imageReceiver.isInsideImage(x, y) : this.backgroundRect.contains(x, y)) : action2 == 3) {
                    this.imagePressed = false;
                }
            }
            if (!z) {
                staticLayout = this.textLayout;
                if (staticLayout != null) {
                }
                this.pressedLink = null;
            }
            z2 = z;
            if (z2) {
            }
        } else if (this.delegate != null) {
            if ((messageObject.type == 11 || isButtonLayout(messageObject)) && this.imageReceiver.isInsideImage(x, y)) {
                this.imagePressed = true;
                z = true;
            } else {
                z = false;
            }
            if (this.radialProgress.getIcon() == 4 && (((i3 = messageObject.type) == 21 || i3 == 22) && this.backgroundRect.contains(x, y))) {
                this.imagePressed = true;
                z = true;
            }
            if (isButtonLayout(messageObject) && this.giftPremiumButtonLayout != null && (this.giftButtonRect.contains(x, y) || (this.buttonClickableAsImage && this.backgroundRect.contains(x, y)))) {
                View view3 = this.rippleView;
                this.giftButtonPressed = true;
                view3.setPressed(true);
                this.bounce.setPressed(true);
                z = true;
            }
            if (z) {
                startCheckLongPress();
            }
            if (!z && (motionEvent.getAction() == 0 || (this.pressedLink != null && motionEvent.getAction() == 1))) {
                staticLayout = this.textLayout;
                if (staticLayout != null) {
                    if (x >= this.textX) {
                        float f = this.textY;
                        if (y >= f && x <= i + this.textWidth && y <= i2 + this.textHeight) {
                            float f2 = x - this.textXLeft;
                            int lineForVertical = staticLayout.getLineForVertical((int) (y - f));
                            int offsetForHorizontal = this.textLayout.getOffsetForHorizontal(lineForVertical, f2);
                            float lineLeft = this.textLayout.getLineLeft(lineForVertical);
                            if (lineLeft <= f2 && lineLeft + this.textLayout.getLineWidth(lineForVertical) >= f2) {
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
                this.pressedLink = null;
            }
            z2 = z;
            if (z2) {
            }
        }
        z = false;
        if (!z) {
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

    public void setMessageObject(MessageObject messageObject) {
        setMessageObject(messageObject, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:350:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:357:0x0188  */
    /* JADX WARN: Removed duplicated region for block: B:372:0x01f3  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x01ff  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setMessageObject(MessageObject messageObject, boolean z) {
        TLRPC$Document tLRPC$Document;
        TLRPC$PhotoSize tLRPC$PhotoSize;
        BitmapDrawable bitmapDrawable;
        String str;
        int i;
        String str2;
        ImageReceiver imageReceiver;
        ImageLocation imageLocation;
        ImageLocation imageLocation2;
        String str3;
        long j;
        boolean z2;
        float f;
        RadialProgress2 radialProgress2;
        TLRPC$WallPaper tLRPC$WallPaper;
        TLRPC$MessageAction tLRPC$MessageAction;
        ImageReceiver imageReceiver2;
        ImageLocation forDocument;
        StringBuilder sb;
        String str4;
        StaticLayout staticLayout;
        if (this.currentMessageObject != messageObject || (!((staticLayout = this.textLayout) == null || TextUtils.equals(staticLayout.getText(), messageObject.messageText)) || (!(this.hasReplyMessage || messageObject.replyMessageObject == null) || z || messageObject.type == 21 || messageObject.forceUpdate))) {
            if (BuildVars.DEBUG_PRIVATE_VERSION && Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
                FileLog.e(new IllegalStateException("Wrong thread!!!"));
            }
            TLRPC$VideoSize tLRPC$VideoSize = null;
            r2 = null;
            TLRPC$PhotoSize tLRPC$PhotoSize2 = null;
            tLRPC$VideoSize = null;
            tLRPC$VideoSize = null;
            this.accessibilityText = null;
            MessageObject messageObject2 = this.currentMessageObject;
            boolean z3 = messageObject2 == null || messageObject2.stableId != messageObject.stableId;
            this.currentMessageObject = messageObject;
            messageObject.forceUpdate = false;
            this.hasReplyMessage = messageObject.replyMessageObject != null;
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            this.previousWidth = 0;
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
                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.messageOwner.media.user_id));
                this.avatarDrawable.setInfo(this.currentAccount, user);
                TL_stories$StoryItem tL_stories$StoryItem = messageObject.messageOwner.media.storyItem;
                if (tL_stories$StoryItem == null || !tL_stories$StoryItem.noforwards) {
                    StoriesUtilities.setImage(this.imageReceiver, tL_stories$StoryItem);
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
                        for (int i4 = 0; i4 < size && !(messageObject.photoThumbs.get(i4) instanceof TLRPC$TL_photoStrippedSize); i4++) {
                        }
                    }
                    TLRPC$TL_channelAdminLogEvent tLRPC$TL_channelAdminLogEvent = messageObject.currentEvent;
                    if (tLRPC$TL_channelAdminLogEvent != null) {
                        TLRPC$ChannelAdminLogEventAction tLRPC$ChannelAdminLogEventAction = tLRPC$TL_channelAdminLogEvent.action;
                        if (tLRPC$ChannelAdminLogEventAction instanceof TLRPC$TL_channelAdminLogEventActionChangeWallpaper) {
                            tLRPC$WallPaper = ((TLRPC$TL_channelAdminLogEventActionChangeWallpaper) tLRPC$ChannelAdminLogEventAction).new_value;
                            if (TextUtils.isEmpty(ChatThemeController.getWallpaperEmoticon(tLRPC$WallPaper))) {
                                Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
                                boolean isDark = resourcesProvider != null ? resourcesProvider.isDark() : Theme.isCurrentThemeDark();
                                this.imageReceiver.clearImage();
                                Drawable backgroundDrawableFromTheme = PreviewView.getBackgroundDrawableFromTheme(this.currentAccount, ChatThemeController.getWallpaperEmoticon(tLRPC$WallPaper), isDark, false);
                                this.wallpaperPreviewDrawable = backgroundDrawableFromTheme;
                                if (backgroundDrawableFromTheme != null) {
                                    backgroundDrawableFromTheme.setCallback(this);
                                }
                            } else {
                                if (tLRPC$WallPaper == null || (str4 = tLRPC$WallPaper.uploadingImage) == null) {
                                    if (tLRPC$WallPaper != null) {
                                        TLObject tLObject = messageObject.photoThumbsObject;
                                        TLRPC$Document tLRPC$Document2 = tLObject instanceof TLRPC$Document ? (TLRPC$Document) tLObject : tLRPC$WallPaper.document;
                                        imageReceiver2 = this.imageReceiver;
                                        forDocument = ImageLocation.getForDocument(tLRPC$Document2);
                                        sb = new StringBuilder();
                                    }
                                    this.wallpaperPreviewDrawable = null;
                                } else {
                                    imageReceiver2 = this.imageReceiver;
                                    forDocument = ImageLocation.getForPath(str4);
                                    sb = new StringBuilder();
                                }
                                sb.append("150_150_wallpaper");
                                sb.append(tLRPC$WallPaper.id);
                                sb.append(ChatBackgroundDrawable.hash(tLRPC$WallPaper.settings));
                                imageReceiver2.setImage(forDocument, sb.toString(), null, null, ChatBackgroundDrawable.createThumb(tLRPC$WallPaper), 0L, null, tLRPC$WallPaper, 1);
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
                    TLRPC$Message tLRPC$Message = messageObject.messageOwner;
                    tLRPC$WallPaper = (tLRPC$Message == null || (tLRPC$MessageAction = tLRPC$Message.action) == null) ? null : tLRPC$MessageAction.wallpaper;
                    if (TextUtils.isEmpty(ChatThemeController.getWallpaperEmoticon(tLRPC$WallPaper))) {
                    }
                    this.imageReceiver.setRoundRadius((int) (this.stickerSize / 2.0f));
                    if (getUploadingInfoProgress(messageObject) != 1.0f) {
                    }
                } else if (i2 == 21) {
                    this.imageReceiver.setRoundRadius((int) (this.stickerSize / 2.0f));
                    this.imageReceiver.setAllowStartLottieAnimation(true);
                    this.imageReceiver.setDelegate(null);
                    TLRPC$TL_messageActionSuggestProfilePhoto tLRPC$TL_messageActionSuggestProfilePhoto = (TLRPC$TL_messageActionSuggestProfilePhoto) messageObject.messageOwner.action;
                    TLRPC$VideoSize closestVideoSizeWithSize = FileLoader.getClosestVideoSizeWithSize(tLRPC$TL_messageActionSuggestProfilePhoto.photo.video_sizes, 1000);
                    ArrayList arrayList = tLRPC$TL_messageActionSuggestProfilePhoto.photo.video_sizes;
                    ImageLocation forPhoto = (arrayList == null || arrayList.isEmpty()) ? null : ImageLocation.getForPhoto(closestVideoSizeWithSize, tLRPC$TL_messageActionSuggestProfilePhoto.photo);
                    TLRPC$Photo tLRPC$Photo = messageObject.messageOwner.action.photo;
                    if (messageObject.strippedThumb == null) {
                        int size2 = messageObject.photoThumbs.size();
                        int i5 = 0;
                        while (true) {
                            if (i5 >= size2) {
                                break;
                            }
                            TLRPC$PhotoSize tLRPC$PhotoSize3 = messageObject.photoThumbs.get(i5);
                            if (tLRPC$PhotoSize3 instanceof TLRPC$TL_photoStrippedSize) {
                                tLRPC$PhotoSize2 = tLRPC$PhotoSize3;
                                break;
                            }
                            i5++;
                        }
                    }
                    TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 1000);
                    if (closestPhotoSizeWithSize == null) {
                        z2 = false;
                    } else if (closestVideoSizeWithSize != null) {
                        z2 = false;
                        this.imageReceiver.setImage(forPhoto, ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForPhoto(closestPhotoSizeWithSize, tLRPC$Photo), "150_150", ImageLocation.getForObject(tLRPC$PhotoSize2, messageObject.photoThumbsObject), "50_50_b", messageObject.strippedThumb, 0L, null, messageObject, 0);
                    } else {
                        z2 = false;
                        this.imageReceiver.setImage(ImageLocation.getForPhoto(closestPhotoSizeWithSize, tLRPC$Photo), "150_150", ImageLocation.getForObject(tLRPC$PhotoSize2, messageObject.photoThumbsObject), "50_50_b", messageObject.strippedThumb, 0L, null, messageObject, 0);
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
                    String str5 = UserConfig.getInstance(this.currentAccount).premiumGiftsStickerPack;
                    if (str5 == null) {
                        MediaDataController.getInstance(this.currentAccount).checkPremiumGiftStickers();
                        return;
                    }
                    TLRPC$TL_messages_stickerSet stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByName(str5);
                    if (stickerSetByName == null) {
                        stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByEmojiOrName(str5);
                    }
                    if (stickerSetByName != null) {
                        TLRPC$MessageAction tLRPC$MessageAction2 = messageObject.messageOwner.action;
                        int i6 = tLRPC$MessageAction2.months;
                        if (messageObject.type == 30) {
                            long j2 = ((TLRPC$TL_messageActionGiftStars) tLRPC$MessageAction2).stars;
                            String str6 = j2 <= 1000 ? "2⃣" : j2 < 2500 ? "3⃣" : "4⃣";
                            int i7 = 0;
                            while (true) {
                                if (i7 >= stickerSetByName.packs.size()) {
                                    break;
                                }
                                TLRPC$TL_stickerPack tLRPC$TL_stickerPack = (TLRPC$TL_stickerPack) stickerSetByName.packs.get(i7);
                                if (TextUtils.equals(tLRPC$TL_stickerPack.emoticon, str6) && !tLRPC$TL_stickerPack.documents.isEmpty()) {
                                    long longValue = ((Long) tLRPC$TL_stickerPack.documents.get(0)).longValue();
                                    for (int i8 = 0; i8 < stickerSetByName.documents.size(); i8++) {
                                        tLRPC$Document = (TLRPC$Document) stickerSetByName.documents.get(i8);
                                        if (tLRPC$Document != null && tLRPC$Document.id == longValue) {
                                            break;
                                        }
                                    }
                                } else {
                                    i7++;
                                }
                            }
                            tLRPC$Document = null;
                        } else {
                            String str7 = (String) monthsToEmoticon.get(Integer.valueOf(i6));
                            Iterator it = stickerSetByName.packs.iterator();
                            TLRPC$Document tLRPC$Document3 = null;
                            while (it.hasNext()) {
                                TLRPC$TL_stickerPack tLRPC$TL_stickerPack2 = (TLRPC$TL_stickerPack) it.next();
                                if (Objects.equals(tLRPC$TL_stickerPack2.emoticon, str7)) {
                                    Iterator it2 = tLRPC$TL_stickerPack2.documents.iterator();
                                    while (it2.hasNext()) {
                                        long longValue2 = ((Long) it2.next()).longValue();
                                        Iterator it3 = stickerSetByName.documents.iterator();
                                        while (true) {
                                            if (!it3.hasNext()) {
                                                break;
                                            }
                                            TLRPC$Document tLRPC$Document4 = (TLRPC$Document) it3.next();
                                            if (tLRPC$Document4.id == longValue2) {
                                                tLRPC$Document3 = tLRPC$Document4;
                                                break;
                                            }
                                        }
                                        if (tLRPC$Document3 != null) {
                                            break;
                                        }
                                    }
                                }
                                if (tLRPC$Document3 != null) {
                                    break;
                                }
                            }
                            tLRPC$Document = tLRPC$Document3;
                        }
                        if (tLRPC$Document == null && !stickerSetByName.documents.isEmpty()) {
                            tLRPC$Document = (TLRPC$Document) stickerSetByName.documents.get(0);
                        }
                    } else {
                        tLRPC$Document = null;
                    }
                    this.forceWasUnread = messageObject.wasUnread;
                    this.giftSticker = tLRPC$Document;
                    if (tLRPC$Document != null) {
                        this.imageReceiver.setAllowStartLottieAnimation(true);
                        this.imageReceiver.setDelegate(this.giftStickerDelegate);
                        this.giftEffectAnimation = null;
                        int i9 = 0;
                        while (true) {
                            if (i9 >= tLRPC$Document.video_thumbs.size()) {
                                break;
                            } else if ("f".equals(tLRPC$Document.video_thumbs.get(i9).type)) {
                                this.giftEffectAnimation = tLRPC$Document.video_thumbs.get(i9);
                                break;
                            } else {
                                i9++;
                            }
                        }
                        SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(tLRPC$Document, Theme.key_windowBackgroundGray, 0.3f);
                        this.imageReceiver.setAutoRepeat(0);
                        this.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$Document), String.format(Locale.US, "%d_%d_nr_messageId=%d", Integer.valueOf((int) NotificationCenter.audioRouteChanged), Integer.valueOf((int) NotificationCenter.audioRouteChanged), Integer.valueOf(messageObject.stableId)), svgThumb, "tgs", stickerSetByName, 1);
                    } else {
                        MediaDataController.getInstance(this.currentAccount).loadStickersByEmojiOrName(str5, false, stickerSetByName == null);
                    }
                } else if (i2 == 11) {
                    this.imageReceiver.setAllowStartLottieAnimation(true);
                    this.imageReceiver.setDelegate(null);
                    this.imageReceiver.setRoundRadius(AndroidUtilities.roundMessageSize / 2);
                    this.imageReceiver.setAutoRepeatCount(1);
                    this.avatarDrawable.setInfo(messageObject.getDialogId(), null, null);
                    if (messageObject.messageOwner.action instanceof TLRPC$TL_messageActionUserUpdatedPhoto) {
                        this.imageReceiver.setImage(null, null, this.avatarDrawable, null, messageObject, 0);
                    } else {
                        if (messageObject.strippedThumb == null) {
                            int size3 = messageObject.photoThumbs.size();
                            for (int i10 = 0; i10 < size3; i10++) {
                                tLRPC$PhotoSize = messageObject.photoThumbs.get(i10);
                                if (tLRPC$PhotoSize instanceof TLRPC$TL_photoStrippedSize) {
                                    break;
                                }
                            }
                        }
                        tLRPC$PhotoSize = null;
                        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 640);
                        if (closestPhotoSizeWithSize2 != null) {
                            TLRPC$Photo tLRPC$Photo2 = messageObject.messageOwner.action.photo;
                            if (!tLRPC$Photo2.video_sizes.isEmpty() && SharedConfig.isAutoplayGifs()) {
                                TLRPC$VideoSize closestVideoSizeWithSize2 = FileLoader.getClosestVideoSizeWithSize(tLRPC$Photo2.video_sizes, 1000);
                                if (messageObject.mediaExists || DownloadController.getInstance(this.currentAccount).canDownloadMedia(4, closestVideoSizeWithSize2.size)) {
                                    tLRPC$VideoSize = closestVideoSizeWithSize2;
                                } else {
                                    this.currentVideoLocation = ImageLocation.getForPhoto(closestVideoSizeWithSize2, tLRPC$Photo2);
                                    DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(FileLoader.getAttachFileName(closestVideoSizeWithSize2), messageObject, this);
                                }
                            }
                            if (tLRPC$VideoSize != null) {
                                imageReceiver = this.imageReceiver;
                                imageLocation = ImageLocation.getForPhoto(tLRPC$VideoSize, tLRPC$Photo2);
                                imageLocation2 = ImageLocation.getForObject(tLRPC$PhotoSize, messageObject.photoThumbsObject);
                                bitmapDrawable = messageObject.strippedThumb;
                                str = null;
                                i = 1;
                                str2 = ImageLoader.AUTOPLAY_FILTER;
                                str3 = "50_50_b";
                                j = 0;
                            } else {
                                ImageReceiver imageReceiver3 = this.imageReceiver;
                                ImageLocation forObject = ImageLocation.getForObject(closestPhotoSizeWithSize2, messageObject.photoThumbsObject);
                                ImageLocation forObject2 = ImageLocation.getForObject(tLRPC$PhotoSize, messageObject.photoThumbsObject);
                                bitmapDrawable = messageObject.strippedThumb;
                                str = null;
                                i = 1;
                                str2 = "150_150";
                                imageReceiver = imageReceiver3;
                                imageLocation = forObject;
                                imageLocation2 = forObject2;
                                str3 = "50_50_b";
                                j = 0;
                            }
                            imageReceiver.setImage(imageLocation, str2, imageLocation2, str3, bitmapDrawable, j, str, messageObject, i);
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
