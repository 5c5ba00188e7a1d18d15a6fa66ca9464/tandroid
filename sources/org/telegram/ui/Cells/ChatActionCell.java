package org.telegram.ui.Cells;

import android.annotation.SuppressLint;
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
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.Stories.StoriesUtilities;
import org.telegram.ui.Stories.UploadingDotsSpannable;
import org.telegram.ui.Stories.recorder.HintView2;
import org.telegram.ui.Stories.recorder.PreviewView;
/* loaded from: classes4.dex */
public class ChatActionCell extends BaseCell implements DownloadController.FileDownloadProgressListener, NotificationCenter.NotificationCenterDelegate {
    private static Map<Integer, String> monthsToEmoticon;
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
    private ArrayList<Integer> lineHeights;
    private ArrayList<Integer> lineWidths;
    private LoadingDrawable loadingDrawable;
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
    public List<SpoilerEffect> spoilers;
    private Stack<SpoilerEffect> spoilersPool;
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
        public final /* synthetic */ class -CC {
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

    public boolean isFloating() {
        return false;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String str, long j, long j2) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String str, long j, long j2, boolean z) {
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

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        MessageObject messageObject;
        if (i == NotificationCenter.startSpoilers) {
            setSpoilersSuppressed(false);
        } else if (i == NotificationCenter.stopSpoilers) {
            setSpoilersSuppressed(true);
        } else if (i == NotificationCenter.didUpdatePremiumGiftStickers) {
            MessageObject messageObject2 = this.currentMessageObject;
            if (messageObject2 != null) {
                setMessageObject(messageObject2, true);
            }
        } else if (i == NotificationCenter.diceStickersDidLoad && Objects.equals(objArr[0], UserConfig.getInstance(this.currentAccount).premiumGiftsStickerPack) && (messageObject = this.currentMessageObject) != null) {
            setMessageObject(messageObject, true);
        }
    }

    public void setSpoilersSuppressed(boolean z) {
        for (SpoilerEffect spoilerEffect : this.spoilers) {
            spoilerEffect.setSuppressUpdates(z);
        }
    }

    public void setInvalidateWithParent(View view) {
        this.invalidateWithParent = view;
    }

    public boolean hasButton() {
        MessageObject messageObject = this.currentMessageObject;
        return (messageObject == null || !isButtonLayout(messageObject) || this.giftPremiumButtonLayout == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        final RLottieDrawable lottieAnimation;
        ChatActionCellDelegate chatActionCellDelegate;
        if (!z || (lottieAnimation = this.imageReceiver.getLottieAnimation()) == null) {
            return;
        }
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && !messageObject.playedGiftAnimation) {
            messageObject.playedGiftAnimation = true;
            lottieAnimation.setCurrentFrame(0, false);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    RLottieDrawable.this.restart();
                }
            });
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
                return;
            }
            return;
        }
        lottieAnimation.stop();
        lottieAnimation.setCurrentFrame(lottieAnimation.getFramesCount() - 1, false);
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
        this.spoilersPool = new Stack<>();
        this.overrideBackground = -1;
        this.overrideText = -1;
        this.lineWidths = new ArrayList<>();
        this.lineHeights = new ArrayList<>();
        this.backgroundPath = new Path();
        this.rect = new RectF();
        this.invalidatePath = true;
        this.invalidateColors = false;
        this.buttonClickableAsImage = true;
        this.giftTitlePaint = new TextPaint(1);
        this.giftSubtitlePaint = new TextPaint(1);
        this.radialProgress = new RadialProgress2(this);
        this.giftStickerDelegate = new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda4
            @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
            public final void didSetImage(ImageReceiver imageReceiver, boolean z2, boolean z3, boolean z4) {
                ChatActionCell.this.lambda$new$0(imageReceiver, z2, z3, z4);
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

    public void setDelegate(ChatActionCellDelegate chatActionCellDelegate) {
        this.delegate = chatActionCellDelegate;
    }

    public void setCustomDate(int i, boolean z, boolean z2) {
        String formatDateChat;
        int i2 = this.customDate;
        if (i2 == i || i2 / 3600 == i / 3600) {
            return;
        }
        if (!z) {
            formatDateChat = LocaleController.formatDateChat(i);
        } else if (i == 2147483646) {
            formatDateChat = LocaleController.getString("MessageScheduledUntilOnline", R.string.MessageScheduledUntilOnline);
        } else {
            formatDateChat = LocaleController.formatString("MessageScheduledOn", R.string.MessageScheduledOn, LocaleController.formatDateChat(i));
        }
        this.customDate = i;
        CharSequence charSequence = this.customText;
        if (charSequence == null || !TextUtils.equals(formatDateChat, charSequence)) {
            this.customText = formatDateChat;
            this.accessibilityText = null;
            updateTextInternal(z2);
        }
    }

    private void updateTextInternal(boolean z) {
        if (getMeasuredWidth() != 0) {
            createLayout(this.customText, getMeasuredWidth());
            invalidate();
        }
        if (this.wasLayout) {
            buildLayout();
        } else if (z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActionCell.this.requestLayout();
                }
            });
        } else {
            requestLayout();
        }
    }

    public void setCustomText(CharSequence charSequence) {
        this.customText = charSequence;
        if (charSequence != null) {
            updateTextInternal(false);
        }
    }

    public void setOverrideColor(int i, int i2) {
        this.overrideBackground = i;
        this.overrideText = i2;
    }

    public void setMessageObject(MessageObject messageObject) {
        setMessageObject(messageObject, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:69:0x0120  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0145  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01f0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setMessageObject(MessageObject messageObject, boolean z) {
        TLRPC$Document tLRPC$Document;
        Iterator<TLRPC$TL_stickerPack> it;
        TLRPC$PhotoSize tLRPC$PhotoSize;
        float f;
        TLRPC$WallPaper tLRPC$WallPaper;
        TLRPC$MessageAction tLRPC$MessageAction;
        TLRPC$Document tLRPC$Document2;
        String str;
        StaticLayout staticLayout;
        if (this.currentMessageObject != messageObject || (!((staticLayout = this.textLayout) == null || TextUtils.equals(staticLayout.getText(), messageObject.messageText)) || (!(this.hasReplyMessage || messageObject.replyMessageObject == null) || z || messageObject.type == 21 || messageObject.forceUpdate))) {
            if (BuildVars.DEBUG_PRIVATE_VERSION && Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
                FileLog.e(new IllegalStateException("Wrong thread!!!"));
            }
            TLRPC$VideoSize tLRPC$VideoSize = null;
            r1 = null;
            TLRPC$PhotoSize tLRPC$PhotoSize2 = null;
            tLRPC$VideoSize = null;
            tLRPC$VideoSize = null;
            this.accessibilityText = null;
            MessageObject messageObject2 = this.currentMessageObject;
            boolean z2 = messageObject2 == null || messageObject2.stableId != messageObject.stableId;
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
            if (messageObject.isStoryMention()) {
                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.messageOwner.media.user_id));
                this.avatarDrawable.setInfo(this.currentAccount, user);
                TL_stories$StoryItem tL_stories$StoryItem = messageObject.messageOwner.media.storyItem;
                if (tL_stories$StoryItem != null && tL_stories$StoryItem.noforwards) {
                    this.imageReceiver.setForUserOrChat(user, this.avatarDrawable, null, true, 0, true);
                } else {
                    StoriesUtilities.setImage(this.imageReceiver, tL_stories$StoryItem);
                }
                this.imageReceiver.setRoundRadius((int) (this.stickerSize / 2.0f));
            } else {
                int i = messageObject.type;
                if (i == 22) {
                    if (messageObject.strippedThumb == null) {
                        int size = messageObject.photoThumbs.size();
                        for (int i2 = 0; i2 < size && !(messageObject.photoThumbs.get(i2) instanceof TLRPC$TL_photoStrippedSize); i2++) {
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
                            } else if (tLRPC$WallPaper != null && (str = tLRPC$WallPaper.uploadingImage) != null) {
                                this.imageReceiver.setImage(ImageLocation.getForPath(str), "150_150_wallpaper" + tLRPC$WallPaper.id + ChatBackgroundDrawable.hash(tLRPC$WallPaper.settings), null, null, ChatBackgroundDrawable.createThumb(tLRPC$WallPaper), 0L, null, tLRPC$WallPaper, 1);
                                this.wallpaperPreviewDrawable = null;
                            } else if (tLRPC$WallPaper != null) {
                                TLObject tLObject = messageObject.photoThumbsObject;
                                if (tLObject instanceof TLRPC$Document) {
                                    tLRPC$Document2 = (TLRPC$Document) tLObject;
                                } else {
                                    tLRPC$Document2 = tLRPC$WallPaper.document;
                                }
                                this.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$Document2), "150_150_wallpaper" + tLRPC$WallPaper.id + ChatBackgroundDrawable.hash(tLRPC$WallPaper.settings), null, null, ChatBackgroundDrawable.createThumb(tLRPC$WallPaper), 0L, null, tLRPC$WallPaper, 1);
                                this.wallpaperPreviewDrawable = null;
                            } else {
                                this.wallpaperPreviewDrawable = null;
                            }
                            this.imageReceiver.setRoundRadius((int) (this.stickerSize / 2.0f));
                            if (getUploadingInfoProgress(messageObject) != 1.0f) {
                                this.radialProgress.setProgress(1.0f, !z2);
                                this.radialProgress.setIcon(4, !z2, !z2);
                            } else {
                                this.radialProgress.setIcon(3, !z2, !z2);
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
                } else if (i == 21) {
                    this.imageReceiver.setRoundRadius((int) (this.stickerSize / 2.0f));
                    this.imageReceiver.setAllowStartLottieAnimation(true);
                    this.imageReceiver.setDelegate(null);
                    TLRPC$TL_messageActionSuggestProfilePhoto tLRPC$TL_messageActionSuggestProfilePhoto = (TLRPC$TL_messageActionSuggestProfilePhoto) messageObject.messageOwner.action;
                    TLRPC$VideoSize closestVideoSizeWithSize = FileLoader.getClosestVideoSizeWithSize(tLRPC$TL_messageActionSuggestProfilePhoto.photo.video_sizes, 1000);
                    ArrayList<TLRPC$VideoSize> arrayList = tLRPC$TL_messageActionSuggestProfilePhoto.photo.video_sizes;
                    ImageLocation forPhoto = (arrayList == null || arrayList.isEmpty()) ? null : ImageLocation.getForPhoto(closestVideoSizeWithSize, tLRPC$TL_messageActionSuggestProfilePhoto.photo);
                    TLRPC$Photo tLRPC$Photo = messageObject.messageOwner.action.photo;
                    if (messageObject.strippedThumb == null) {
                        int size2 = messageObject.photoThumbs.size();
                        int i3 = 0;
                        while (true) {
                            if (i3 >= size2) {
                                break;
                            }
                            TLRPC$PhotoSize tLRPC$PhotoSize3 = messageObject.photoThumbs.get(i3);
                            if (tLRPC$PhotoSize3 instanceof TLRPC$TL_photoStrippedSize) {
                                tLRPC$PhotoSize2 = tLRPC$PhotoSize3;
                                break;
                            }
                            i3++;
                        }
                    }
                    TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, 1000);
                    if (closestPhotoSizeWithSize != null) {
                        if (closestVideoSizeWithSize != null) {
                            this.imageReceiver.setImage(forPhoto, ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForPhoto(closestPhotoSizeWithSize, tLRPC$Photo), "150_150", ImageLocation.getForObject(tLRPC$PhotoSize2, messageObject.photoThumbsObject), "50_50_b", messageObject.strippedThumb, 0L, null, messageObject, 0);
                        } else {
                            this.imageReceiver.setImage(ImageLocation.getForPhoto(closestPhotoSizeWithSize, tLRPC$Photo), "150_150", ImageLocation.getForObject(tLRPC$PhotoSize2, messageObject.photoThumbsObject), "50_50_b", messageObject.strippedThumb, 0L, null, messageObject, 0);
                        }
                    }
                    this.imageReceiver.setAllowStartLottieAnimation(false);
                    ImageUpdater imageUpdater = MessagesController.getInstance(this.currentAccount).photoSuggestion.get(messageObject.messageOwner.local_id);
                    if (imageUpdater != null) {
                        f = 1.0f;
                        if (imageUpdater.getCurrentImageProgress() != 1.0f) {
                            this.radialProgress.setIcon(3, !z2, !z2);
                        }
                    } else {
                        f = 1.0f;
                    }
                    this.radialProgress.setProgress(f, !z2);
                    this.radialProgress.setIcon(4, !z2, !z2);
                } else if (i == 18 || i == 25) {
                    this.imageReceiver.setRoundRadius(0);
                    String str2 = UserConfig.getInstance(this.currentAccount).premiumGiftsStickerPack;
                    if (str2 == null) {
                        MediaDataController.getInstance(this.currentAccount).checkPremiumGiftStickers();
                        return;
                    }
                    TLRPC$TL_messages_stickerSet stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByName(str2);
                    if (stickerSetByName == null) {
                        stickerSetByName = MediaDataController.getInstance(this.currentAccount).getStickerSetByEmojiOrName(str2);
                    }
                    if (stickerSetByName != null) {
                        String str3 = monthsToEmoticon.get(Integer.valueOf(messageObject.messageOwner.action.months));
                        Iterator<TLRPC$TL_stickerPack> it2 = stickerSetByName.packs.iterator();
                        tLRPC$Document = null;
                        while (it2.hasNext()) {
                            TLRPC$TL_stickerPack next = it2.next();
                            if (Objects.equals(next.emoticon, str3)) {
                                Iterator<Long> it3 = next.documents.iterator();
                                while (it3.hasNext()) {
                                    long longValue = it3.next().longValue();
                                    Iterator<TLRPC$Document> it4 = stickerSetByName.documents.iterator();
                                    while (true) {
                                        if (!it4.hasNext()) {
                                            it = it2;
                                            break;
                                        }
                                        TLRPC$Document next2 = it4.next();
                                        it = it2;
                                        if (next2.id == longValue) {
                                            tLRPC$Document = next2;
                                            break;
                                        }
                                        it2 = it;
                                    }
                                    if (tLRPC$Document != null) {
                                        break;
                                    }
                                    it2 = it;
                                }
                            }
                            it = it2;
                            if (tLRPC$Document != null) {
                                break;
                            }
                            it2 = it;
                        }
                        if (tLRPC$Document == null && !stickerSetByName.documents.isEmpty()) {
                            tLRPC$Document = stickerSetByName.documents.get(0);
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
                        int i4 = 0;
                        while (true) {
                            if (i4 >= tLRPC$Document.video_thumbs.size()) {
                                break;
                            } else if ("f".equals(tLRPC$Document.video_thumbs.get(i4).type)) {
                                this.giftEffectAnimation = tLRPC$Document.video_thumbs.get(i4);
                                break;
                            } else {
                                i4++;
                            }
                        }
                        SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(tLRPC$Document, Theme.key_windowBackgroundGray, 0.3f);
                        this.imageReceiver.setAutoRepeat(0);
                        this.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$Document), String.format(Locale.US, "%d_%d_nr_messageId=%d", 160, 160, Integer.valueOf(messageObject.stableId)), svgThumb, "tgs", stickerSetByName, 1);
                    } else {
                        MediaDataController.getInstance(this.currentAccount).loadStickersByEmojiOrName(str2, false, stickerSetByName == null);
                    }
                } else if (i == 11) {
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
                            for (int i5 = 0; i5 < size3; i5++) {
                                tLRPC$PhotoSize = messageObject.photoThumbs.get(i5);
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
                                this.imageReceiver.setImage(ImageLocation.getForPhoto(tLRPC$VideoSize, tLRPC$Photo2), ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForObject(tLRPC$PhotoSize, messageObject.photoThumbsObject), "50_50_b", messageObject.strippedThumb, 0L, null, messageObject, 1);
                            } else {
                                this.imageReceiver.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize2, messageObject.photoThumbsObject), "150_150", ImageLocation.getForObject(tLRPC$PhotoSize, messageObject.photoThumbsObject), "50_50_b", messageObject.strippedThumb, 0L, null, messageObject, 1);
                            }
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
            }
            this.rippleView.setVisibility(isButtonLayout(messageObject) ? 0 : 8);
            ForumUtilities.applyTopicToMessage(messageObject);
            requestLayout();
        }
    }

    private float getUploadingInfoProgress(MessageObject messageObject) {
        MessagesController messagesController;
        String str;
        if (messageObject == null || messageObject.type != 22 || (str = (messagesController = MessagesController.getInstance(this.currentAccount)).uploadingWallpaper) == null || !TextUtils.equals(messageObject.messageOwner.action.wallpaper.uploadingImage, str)) {
            return 1.0f;
        }
        return messagesController.uploadingWallpaperInfo.uploadingProgress;
    }

    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }

    public ImageReceiver getPhotoImage() {
        return this.imageReceiver;
    }

    public void setVisiblePart(float f, int i) {
        this.visiblePartSet = true;
        this.backgroundHeight = i;
        this.viewTop = f;
        this.viewTranslationX = 0.0f;
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

    @Override // org.telegram.ui.Cells.BaseCell
    protected boolean onLongPress() {
        ChatActionCellDelegate chatActionCellDelegate = this.delegate;
        if (chatActionCellDelegate != null) {
            return chatActionCellDelegate.didLongPress(this, this.lastTouchX, this.lastTouchY);
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        View view = this.rippleView;
        RectF rectF = this.giftButtonRect;
        view.layout((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
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
            this.starParticlesDrawable.particles.get(i).lifeTime += System.currentTimeMillis() - this.starParticlesDrawable.pausedTime;
        }
        invalidate();
    }

    /* JADX WARN: Removed duplicated region for block: B:111:0x01a8  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0220  */
    /* JADX WARN: Removed duplicated region for block: B:141:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        ImageUpdater imageUpdater;
        boolean z2;
        StaticLayout staticLayout;
        int i;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null) {
            return super.onTouchEvent(motionEvent);
        }
        float x = motionEvent.getX();
        this.lastTouchX = x;
        float y = motionEvent.getY();
        this.lastTouchY = y;
        boolean z3 = true;
        if (motionEvent.getAction() == 0) {
            if (this.delegate != null) {
                if ((messageObject.type == 11 || isButtonLayout(messageObject)) && this.imageReceiver.isInsideImage(x, y)) {
                    this.imagePressed = true;
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (this.radialProgress.getIcon() == 4 && (((i = messageObject.type) == 21 || i == 22) && this.backgroundRect.contains(x, y))) {
                    this.imagePressed = true;
                    z2 = true;
                }
                if (isButtonLayout(messageObject) && this.giftPremiumButtonLayout != null && (this.giftButtonRect.contains(x, y) || (this.buttonClickableAsImage && this.backgroundRect.contains(x, y)))) {
                    View view = this.rippleView;
                    this.giftButtonPressed = true;
                    view.setPressed(true);
                    this.bounce.setPressed(true);
                    z2 = true;
                }
                if (z2) {
                    startCheckLongPress();
                }
                if (!z2 && (motionEvent.getAction() == 0 || (this.pressedLink != null && motionEvent.getAction() == 1))) {
                    staticLayout = this.textLayout;
                    if (staticLayout != null) {
                        int i2 = this.textX;
                        if (x >= i2) {
                            int i3 = this.textY;
                            if (y >= i3 && x <= i2 + this.textWidth && y <= this.textHeight + i3) {
                                float f = x - this.textXLeft;
                                int lineForVertical = staticLayout.getLineForVertical((int) (y - i3));
                                int offsetForHorizontal = this.textLayout.getOffsetForHorizontal(lineForVertical, f);
                                float lineLeft = this.textLayout.getLineLeft(lineForVertical);
                                if (lineLeft <= f && lineLeft + this.textLayout.getLineWidth(lineForVertical) >= f) {
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
                                            z2 = z3;
                                        } else {
                                            this.pressedLink = null;
                                        }
                                        z3 = z2;
                                        z2 = z3;
                                    }
                                }
                                this.pressedLink = null;
                            }
                        }
                    }
                    this.pressedLink = null;
                }
                return z2 ? super.onTouchEvent(motionEvent) : z2;
            }
        } else {
            if (motionEvent.getAction() != 2) {
                cancelCheckLongPress();
            }
            if (this.giftButtonPressed) {
                int action = motionEvent.getAction();
                if (action == 1) {
                    this.imagePressed = false;
                    View view2 = this.rippleView;
                    this.giftButtonPressed = false;
                    view2.setPressed(false);
                    this.bounce.setPressed(false);
                    if (this.delegate != null) {
                        int i4 = messageObject.type;
                        if (i4 == 25) {
                            openPremiumGiftChannel();
                        } else if (i4 == 18) {
                            playSoundEffect(0);
                            openPremiumGiftPreview();
                        } else if (MessagesController.getInstance(this.currentAccount).photoSuggestion.get(messageObject.messageOwner.local_id) == null) {
                            if (this.buttonClickableAsImage) {
                                this.delegate.didClickImage(this);
                            } else {
                                this.delegate.didClickButton(this);
                            }
                        }
                    }
                } else if (action != 2) {
                    if (action == 3) {
                        this.imagePressed = false;
                        View view3 = this.rippleView;
                        this.giftButtonPressed = false;
                        view3.setPressed(false);
                        this.bounce.setPressed(false);
                    }
                } else if (!isButtonLayout(messageObject) || (!this.giftButtonRect.contains(x, y) && !this.backgroundRect.contains(x, y))) {
                    View view4 = this.rippleView;
                    this.giftButtonPressed = false;
                    view4.setPressed(false);
                    this.bounce.setPressed(false);
                }
            } else if (this.imagePressed) {
                int action2 = motionEvent.getAction();
                if (action2 == 1) {
                    this.imagePressed = false;
                    int i5 = messageObject.type;
                    if (i5 == 25) {
                        openPremiumGiftChannel();
                    } else if (i5 == 18) {
                        openPremiumGiftPreview();
                    } else if (this.delegate != null) {
                        if (i5 != 21 || (imageUpdater = MessagesController.getInstance(this.currentAccount).photoSuggestion.get(messageObject.messageOwner.local_id)) == null) {
                            z = false;
                        } else {
                            imageUpdater.cancel();
                            z = true;
                        }
                        if (!z) {
                            this.delegate.didClickImage(this);
                            playSoundEffect(0);
                        }
                    }
                } else if (action2 != 2) {
                    if (action2 == 3) {
                        this.imagePressed = false;
                    }
                } else if (isNewStyleButtonLayout()) {
                    if (!this.backgroundRect.contains(x, y)) {
                        this.imagePressed = false;
                    }
                } else if (!this.imageReceiver.isInsideImage(x, y)) {
                    this.imagePressed = false;
                }
            }
        }
        z2 = false;
        if (!z2) {
            staticLayout = this.textLayout;
            if (staticLayout != null) {
            }
            this.pressedLink = null;
        }
        if (z2) {
        }
    }

    private void openPremiumGiftChannel() {
        if (this.delegate != null) {
            final TLRPC$TL_messageActionGiftCode tLRPC$TL_messageActionGiftCode = (TLRPC$TL_messageActionGiftCode) this.currentMessageObject.messageOwner.action;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActionCell.this.lambda$openPremiumGiftChannel$1(tLRPC$TL_messageActionGiftCode);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openPremiumGiftChannel$1(TLRPC$TL_messageActionGiftCode tLRPC$TL_messageActionGiftCode) {
        this.delegate.didOpenPremiumGiftChannel(this, tLRPC$TL_messageActionGiftCode.slug, false);
    }

    private boolean isSelfGiftCode() {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            TLRPC$Message tLRPC$Message = messageObject.messageOwner;
            if ((tLRPC$Message.action instanceof TLRPC$TL_messageActionGiftCode) && (tLRPC$Message.from_id instanceof TLRPC$TL_peerUser)) {
                return MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.currentMessageObject.messageOwner.from_id.user_id)).self;
            }
            return false;
        }
        return false;
    }

    private boolean isGiftCode() {
        MessageObject messageObject = this.currentMessageObject;
        return messageObject != null && (messageObject.messageOwner.action instanceof TLRPC$TL_messageActionGiftCode);
    }

    private void openPremiumGiftPreview() {
        final TLRPC$TL_premiumGiftOption tLRPC$TL_premiumGiftOption = new TLRPC$TL_premiumGiftOption();
        TLRPC$MessageAction tLRPC$MessageAction = this.currentMessageObject.messageOwner.action;
        tLRPC$TL_premiumGiftOption.amount = tLRPC$MessageAction.amount;
        tLRPC$TL_premiumGiftOption.months = tLRPC$MessageAction.months;
        tLRPC$TL_premiumGiftOption.currency = tLRPC$MessageAction.currency;
        final String str = null;
        if (isGiftCode() && !isSelfGiftCode()) {
            str = ((TLRPC$TL_messageActionGiftCode) this.currentMessageObject.messageOwner.action).slug;
        }
        if (this.delegate != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActionCell.this.lambda$openPremiumGiftPreview$2(tLRPC$TL_premiumGiftOption, str);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openPremiumGiftPreview$2(TLRPC$TL_premiumGiftOption tLRPC$TL_premiumGiftOption, String str) {
        this.delegate.didOpenPremiumGift(this, tLRPC$TL_premiumGiftOption, str, false);
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

    private void createLayout(CharSequence charSequence, int i) {
        TextPaint textPaint;
        ChatActionCellDelegate chatActionCellDelegate;
        int dp = i - AndroidUtilities.dp(30.0f);
        if (dp < 0) {
            return;
        }
        this.invalidatePath = true;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null && messageObject.drawServiceWithDefaultTypeface) {
            textPaint = (TextPaint) getThemedPaint("paintChatActionText2");
        } else {
            textPaint = (TextPaint) getThemedPaint("paintChatActionText");
        }
        TextPaint textPaint2 = textPaint;
        textPaint2.linkColor = textPaint2.getColor();
        this.textLayout = new StaticLayout(charSequence, textPaint2, dp, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, (!this.canDrawInParent || (chatActionCellDelegate = this.delegate) == null || chatActionCellDelegate.canDrawOutboundsContent()) ? false : true, this.animatedEmojiStack, this.textLayout);
        this.textHeight = 0;
        this.textWidth = 0;
        try {
            int lineCount = this.textLayout.getLineCount();
            for (int i2 = 0; i2 < lineCount; i2++) {
                try {
                    float lineWidth = this.textLayout.getLineWidth(i2);
                    float f = dp;
                    if (lineWidth > f) {
                        lineWidth = f;
                    }
                    this.textHeight = (int) Math.max(this.textHeight, Math.ceil(this.textLayout.getLineBottom(i2)));
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
            int i3 = this.textX;
            SpoilerEffect.addSpoilers(this, staticLayout, i3, i3 + this.textWidth, (Spannable) charSequence, this.spoilersPool, this.spoilers, null);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x00d8  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int i, int i2) {
        int i3;
        float dp;
        StaticLayout staticLayout;
        float dp2;
        StaticLayout staticLayout2;
        int i4;
        int dp3;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null && this.customText == null) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), this.textHeight + AndroidUtilities.dp(14.0f));
            return;
        }
        int i5 = 0;
        if (isButtonLayout(messageObject)) {
            this.giftRectSize = Math.min((int) (AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() * 0.6f : (AndroidUtilities.displaySize.x * 0.62f) - AndroidUtilities.dp(34.0f)), ((AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.statusBarHeight) - AndroidUtilities.dp(64.0f));
            if (!AndroidUtilities.isTablet() && messageObject.type == 18) {
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
                i4 = AndroidUtilities.roundMessageSize;
                dp3 = AndroidUtilities.dp(10.0f);
            } else if (isButtonLayout(messageObject)) {
                i4 = this.giftRectSize;
                dp3 = AndroidUtilities.dp(12.0f);
            }
            i3 = i4 + dp3;
            if (isButtonLayout(messageObject)) {
                boolean isGiftChannel = isGiftChannel(messageObject);
                int imageSize = getImageSize(messageObject);
                if (isNewStyleButtonLayout()) {
                    dp = this.textY + this.textHeight + AndroidUtilities.dp(4.0f) + (AndroidUtilities.dp(16.0f) * 2) + imageSize + this.giftPremiumSubtitleLayout.getHeight() + AndroidUtilities.dp(4.0f);
                } else {
                    dp = this.textY + this.textHeight + (this.giftRectSize * 0.075f) + imageSize + AndroidUtilities.dp(4.0f) + AndroidUtilities.dp(4.0f) + this.giftPremiumSubtitleLayout.getHeight();
                }
                this.giftPremiumAdditionalHeight = 0;
                if (this.giftPremiumTitleLayout != null) {
                    dp2 = dp + staticLayout.getHeight() + AndroidUtilities.dp(isGiftChannel ? 6.0f : 0.0f);
                } else {
                    dp2 = dp - AndroidUtilities.dp(12.0f);
                    this.giftPremiumAdditionalHeight -= AndroidUtilities.dp(30.0f);
                }
                if (this.giftPremiumSubtitleLayout.getLineCount() > 2) {
                    this.giftPremiumAdditionalHeight += ((this.giftPremiumSubtitleLayout.getLineBottom(0) - this.giftPremiumSubtitleLayout.getLineTop(0)) * this.giftPremiumSubtitleLayout.getLineCount()) - 2;
                }
                int dp4 = this.giftPremiumAdditionalHeight - AndroidUtilities.dp(isGiftChannel ? 14.0f : 0.0f);
                this.giftPremiumAdditionalHeight = dp4;
                i3 += dp4;
                int dp5 = this.textHeight + i3 + AndroidUtilities.dp(14.0f);
                StaticLayout staticLayout3 = this.giftPremiumButtonLayout;
                if (staticLayout3 != null) {
                    float height = dp2 + ((((dp5 - dp2) - (staticLayout3 != null ? staticLayout3.getHeight() : 0)) - AndroidUtilities.dp(8.0f)) / 2.0f);
                    float f = (this.previousWidth - this.giftPremiumButtonWidth) / 2.0f;
                    this.giftButtonRect.set(f - AndroidUtilities.dp(18.0f), height - AndroidUtilities.dp(8.0f), f + this.giftPremiumButtonWidth + AndroidUtilities.dp(18.0f), height + (this.giftPremiumButtonLayout != null ? staticLayout2.getHeight() : 0) + AndroidUtilities.dp(8.0f));
                } else {
                    i3 -= AndroidUtilities.dp(40.0f);
                    this.giftPremiumAdditionalHeight -= AndroidUtilities.dp(40.0f);
                }
                int measuredWidth = getMeasuredWidth() << (getMeasuredHeight() + 16);
                this.starParticlesDrawable.rect.set(this.giftButtonRect);
                this.starParticlesDrawable.rect2.set(this.giftButtonRect);
                if (this.starsSize != measuredWidth) {
                    this.starsSize = measuredWidth;
                    this.starParticlesDrawable.resetPositions();
                }
                if (isNewStyleButtonLayout()) {
                    int dp6 = this.textY + this.textHeight + AndroidUtilities.dp(4.0f);
                    this.backgroundRectHeight = 0;
                    int dp7 = 0 + (AndroidUtilities.dp(16.0f) * 2) + imageSize;
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
                    i5 = dp6 + dp8 + AndroidUtilities.dp(6.0f);
                }
            }
            if (messageObject == null && isNewStyleButtonLayout()) {
                setMeasuredDimension(max, i5);
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

    private boolean isNewStyleButtonLayout() {
        MessageObject messageObject = this.currentMessageObject;
        int i = messageObject.type;
        return i == 21 || i == 22 || messageObject.isStoryMention();
    }

    private int getImageSize(MessageObject messageObject) {
        return (messageObject.type == 21 || isNewStyleButtonLayout()) ? AndroidUtilities.dp(78.0f) : this.stickerSize;
    }

    private void buildLayout() {
        CharSequence charSequence;
        CharSequence charSequence2;
        String string;
        CharSequence charSequence3;
        boolean z;
        String formatString;
        String string2;
        ArrayList<TLRPC$VideoSize> arrayList;
        TLRPC$Photo tLRPC$Photo;
        ArrayList<TLRPC$VideoSize> arrayList2;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            if (messageObject.isExpiredStory()) {
                if (messageObject.messageOwner.media.user_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                    charSequence = StoriesUtilities.createExpiredStoryString(true, "ExpiredStoryMention", R.string.ExpiredStoryMention, new Object[0]);
                } else {
                    charSequence = StoriesUtilities.createExpiredStoryString(true, "ExpiredStoryMentioned", R.string.ExpiredStoryMentioned, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())).first_name);
                }
            } else {
                charSequence = (this.delegate.getTopicId() == 0 && MessageObject.isTopicActionMessage(messageObject)) ? ForumUtilities.createActionTextWithTopic(MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-messageObject.getDialogId(), MessageObject.getTopicId(this.currentAccount, messageObject.messageOwner, true)), messageObject) : null;
            }
            if (charSequence == null) {
                TLRPC$Message tLRPC$Message = messageObject.messageOwner;
                if (tLRPC$Message != null && (tLRPC$MessageMedia = tLRPC$Message.media) != null && tLRPC$MessageMedia.ttl_seconds != 0) {
                    if (tLRPC$MessageMedia.photo != null) {
                        charSequence = LocaleController.getString(R.string.AttachPhotoExpired);
                    } else {
                        TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
                        if ((tLRPC$Document instanceof TLRPC$TL_documentEmpty) || ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) && tLRPC$Document == null)) {
                            if (tLRPC$MessageMedia.voice) {
                                charSequence = LocaleController.getString(R.string.AttachVoiceExpired);
                            } else if (tLRPC$MessageMedia.round) {
                                charSequence = LocaleController.getString(R.string.AttachRoundExpired);
                            } else {
                                charSequence = LocaleController.getString(R.string.AttachVideoExpired);
                            }
                        } else {
                            charSequence = AnimatedEmojiSpan.cloneSpans(messageObject.messageText);
                        }
                    }
                } else {
                    charSequence = AnimatedEmojiSpan.cloneSpans(messageObject.messageText);
                }
            }
        } else {
            charSequence = this.customText;
        }
        createLayout(charSequence, this.previousWidth);
        if (messageObject != null) {
            int i = messageObject.type;
            if (i == 11) {
                int i2 = AndroidUtilities.roundMessageSize;
                this.imageReceiver.setImageCoords((this.previousWidth - AndroidUtilities.roundMessageSize) / 2.0f, this.textHeight + AndroidUtilities.dp(19.0f), i2, i2);
            } else if (i == 25) {
                createGiftPremiumChannelLayouts();
            } else if (i == 18) {
                createGiftPremiumLayouts(LocaleController.getString(R.string.ActionGiftPremiumTitle), LocaleController.formatString(R.string.ActionGiftPremiumSubtitle, LocaleController.formatPluralString("Months", messageObject.messageOwner.action.months, new Object[0])), (!isGiftCode() || isSelfGiftCode()) ? LocaleController.getString("ActionGiftPremiumView", R.string.ActionGiftPremiumView) : LocaleController.getString("GiftPremiumUseGiftBtn", R.string.GiftPremiumUseGiftBtn), this.giftRectSize, true);
            } else if (i == 21) {
                TLRPC$TL_messageActionSuggestProfilePhoto tLRPC$TL_messageActionSuggestProfilePhoto = (TLRPC$TL_messageActionSuggestProfilePhoto) messageObject.messageOwner.action;
                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.isOutOwner() ? 0L : messageObject.getDialogId()));
                boolean z2 = tLRPC$TL_messageActionSuggestProfilePhoto.video || !((tLRPC$Photo = tLRPC$TL_messageActionSuggestProfilePhoto.photo) == null || (arrayList2 = tLRPC$Photo.video_sizes) == null || arrayList2.isEmpty());
                if (user.id == UserConfig.getInstance(this.currentAccount).clientUserId) {
                    TLRPC$User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId()));
                    if (z2) {
                        formatString = LocaleController.formatString(R.string.ActionSuggestVideoFromYouDescription, user2.first_name);
                    } else {
                        formatString = LocaleController.formatString(R.string.ActionSuggestPhotoFromYouDescription, user2.first_name);
                    }
                } else if (z2) {
                    formatString = LocaleController.formatString(R.string.ActionSuggestVideoToYouDescription, user.first_name);
                } else {
                    formatString = LocaleController.formatString(R.string.ActionSuggestPhotoToYouDescription, user.first_name);
                }
                String str = formatString;
                if (tLRPC$TL_messageActionSuggestProfilePhoto.video || ((arrayList = tLRPC$TL_messageActionSuggestProfilePhoto.photo.video_sizes) != null && !arrayList.isEmpty())) {
                    string2 = LocaleController.getString(R.string.ViewVideoAction);
                } else {
                    string2 = LocaleController.getString(R.string.ViewPhotoAction);
                }
                createGiftPremiumLayouts(null, str, string2, this.giftRectSize, true);
                this.textLayout = null;
                this.textHeight = 0;
                this.textY = 0;
            } else if (i == 22) {
                TLRPC$User user3 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.isOutOwner() ? 0L : messageObject.getDialogId()));
                if (messageObject.getDialogId() < 0) {
                    charSequence3 = messageObject.messageText;
                } else if (!messageObject.isOutOwner() && messageObject.isWallpaperForBoth() && messageObject.isCurrentWallpaper()) {
                    charSequence2 = messageObject.messageText;
                    string = LocaleController.getString(R.string.RemoveWallpaperAction);
                    z = false;
                    createGiftPremiumLayouts(null, charSequence2, string, this.giftRectSize, z);
                    this.textLayout = null;
                    this.textHeight = 0;
                    this.textY = 0;
                } else if (user3 != null && user3.id == UserConfig.getInstance(this.currentAccount).clientUserId) {
                    charSequence3 = messageObject.messageText;
                } else {
                    charSequence2 = messageObject.messageText;
                    string = LocaleController.getString(R.string.ViewWallpaperAction);
                    z = true;
                    createGiftPremiumLayouts(null, charSequence2, string, this.giftRectSize, z);
                    this.textLayout = null;
                    this.textHeight = 0;
                    this.textY = 0;
                }
                charSequence2 = charSequence3;
                string = null;
                z = true;
                createGiftPremiumLayouts(null, charSequence2, string, this.giftRectSize, z);
                this.textLayout = null;
                this.textHeight = 0;
                this.textY = 0;
            } else if (messageObject.isStoryMention()) {
                TLRPC$User user4 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.messageOwner.media.user_id));
                createGiftPremiumLayouts(null, user4.self ? AndroidUtilities.replaceTags(LocaleController.formatString("StoryYouMentionedTitle", R.string.StoryYouMentionedTitle, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(messageObject.getDialogId())).first_name)) : AndroidUtilities.replaceTags(LocaleController.formatString("StoryMentionedTitle", R.string.StoryMentionedTitle, user4.first_name)), LocaleController.getString(R.string.StoryMentionedAction), this.giftRectSize, true);
                this.textLayout = null;
                this.textHeight = 0;
                this.textY = 0;
            }
        }
    }

    private void createGiftPremiumChannelLayouts() {
        String string;
        SpannableStringBuilder spannableStringBuilder;
        int dp = this.giftRectSize - AndroidUtilities.dp(16.0f);
        this.giftTitlePaint.setTextSize(AndroidUtilities.dp(14.0f));
        this.giftSubtitlePaint.setTextSize(AndroidUtilities.dp(13.0f));
        TLRPC$TL_messageActionGiftCode tLRPC$TL_messageActionGiftCode = (TLRPC$TL_messageActionGiftCode) this.currentMessageObject.messageOwner.action;
        int i = tLRPC$TL_messageActionGiftCode.months;
        TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-DialogObject.getPeerDialogId(tLRPC$TL_messageActionGiftCode.boost_peer)));
        String str = chat == null ? null : chat.title;
        boolean z = tLRPC$TL_messageActionGiftCode.via_giveaway;
        if (tLRPC$TL_messageActionGiftCode.unclaimed) {
            string = LocaleController.getString("BoostingUnclaimedPrize", R.string.BoostingUnclaimedPrize);
        } else {
            string = LocaleController.getString("BoostingCongratulations", R.string.BoostingCongratulations);
        }
        String formatPluralString = i == 12 ? LocaleController.formatPluralString("BoldYears", 1, new Object[0]) : LocaleController.formatPluralString("BoldMonths", i, new Object[0]);
        if (z) {
            if (tLRPC$TL_messageActionGiftCode.unclaimed) {
                spannableStringBuilder = new SpannableStringBuilder(AndroidUtilities.replaceTags(LocaleController.formatString("BoostingYouHaveUnclaimedPrize", R.string.BoostingYouHaveUnclaimedPrize, str)));
                spannableStringBuilder.append((CharSequence) "\n\n");
                spannableStringBuilder.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatString("BoostingUnclaimedPrizeDuration", R.string.BoostingUnclaimedPrizeDuration, formatPluralString)));
            } else {
                spannableStringBuilder = new SpannableStringBuilder(AndroidUtilities.replaceTags(LocaleController.formatString("BoostingReceivedPrizeFrom", R.string.BoostingReceivedPrizeFrom, str)));
                spannableStringBuilder.append((CharSequence) "\n\n");
                spannableStringBuilder.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatString("BoostingReceivedPrizeDuration", R.string.BoostingReceivedPrizeDuration, formatPluralString)));
            }
        } else {
            spannableStringBuilder = new SpannableStringBuilder(AndroidUtilities.replaceTags(str == null ? LocaleController.getString("BoostingReceivedGiftNoName", R.string.BoostingReceivedGiftNoName) : LocaleController.formatString("BoostingReceivedGiftFrom", R.string.BoostingReceivedGiftFrom, str)));
            spannableStringBuilder.append((CharSequence) "\n\n");
            spannableStringBuilder.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatString("BoostingReceivedGiftDuration", R.string.BoostingReceivedGiftDuration, formatPluralString)));
        }
        String string2 = LocaleController.getString("BoostingReceivedGiftOpenBtn", R.string.BoostingReceivedGiftOpenBtn);
        SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(string);
        valueOf.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM)), 0, valueOf.length(), 33);
        this.giftPremiumTitleLayout = new StaticLayout(valueOf, this.giftTitlePaint, dp, Layout.Alignment.ALIGN_CENTER, 1.1f, 0.0f, false);
        this.giftPremiumSubtitleWidth = dp;
        this.giftPremiumSubtitleLayout = new StaticLayout(spannableStringBuilder, this.giftSubtitlePaint, dp, Layout.Alignment.ALIGN_CENTER, 1.1f, 0.0f, false);
        SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(string2);
        valueOf2.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM)), 0, valueOf2.length(), 33);
        StaticLayout staticLayout = new StaticLayout(valueOf2, (TextPaint) getThemedPaint("paintChatActionText"), dp, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        this.giftPremiumButtonLayout = staticLayout;
        this.buttonClickableAsImage = true;
        this.giftPremiumButtonWidth = measureLayoutWidth(staticLayout);
    }

    private void createGiftPremiumLayouts(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, boolean z) {
        int cutInFancyHalf;
        CharSequence charSequence4 = charSequence2;
        int dp = i - AndroidUtilities.dp(16.0f);
        if (charSequence != null) {
            this.giftTitlePaint.setTextSize(AndroidUtilities.dp(16.0f));
            SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(charSequence);
            valueOf.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM)), 0, valueOf.length(), 33);
            this.giftPremiumTitleLayout = new StaticLayout(valueOf, this.giftTitlePaint, dp, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        } else {
            this.giftPremiumTitleLayout = null;
        }
        if (this.currentMessageObject != null && isNewStyleButtonLayout()) {
            this.giftSubtitlePaint.setTextSize(AndroidUtilities.dp(13.0f));
        } else {
            this.giftSubtitlePaint.setTextSize(AndroidUtilities.dp(15.0f));
        }
        this.giftPremiumSubtitleWidth = dp;
        MessageObject messageObject = this.currentMessageObject;
        int i2 = (messageObject == null || messageObject.type != 22 || messageObject.getDialogId() < 0 || (cutInFancyHalf = HintView2.cutInFancyHalf(charSequence4, this.giftSubtitlePaint)) >= dp || ((float) cutInFancyHalf) <= ((float) dp) / 5.0f) ? dp : cutInFancyHalf;
        try {
            charSequence4 = Emoji.replaceEmoji(charSequence4, this.giftSubtitlePaint.getFontMetricsInt(), false);
        } catch (Exception unused) {
        }
        this.giftPremiumSubtitleLayout = new StaticLayout(charSequence4, this.giftSubtitlePaint, i2, Layout.Alignment.ALIGN_CENTER, 1.0f, AndroidUtilities.dp(1.66f), false);
        if (charSequence3 != null) {
            SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(charSequence3);
            valueOf2.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM)), 0, valueOf2.length(), 33);
            StaticLayout staticLayout = new StaticLayout(valueOf2, (TextPaint) getThemedPaint("paintChatActionText"), dp, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            this.giftPremiumButtonLayout = staticLayout;
            this.buttonClickableAsImage = z;
            this.giftPremiumButtonWidth = measureLayoutWidth(staticLayout);
            return;
        }
        this.giftPremiumButtonLayout = null;
        this.buttonClickableAsImage = false;
        this.giftPremiumButtonWidth = 0.0f;
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

    public boolean showingCancelButton() {
        RadialProgress2 radialProgress2 = this.radialProgress;
        return radialProgress2 != null && radialProgress2.getIcon() == 3;
    }

    public int getCustomDate() {
        return this.customDate;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:183:0x0731  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x07b5  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x080c  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        int i;
        int i2;
        float f;
        float f2;
        float dp;
        float f3;
        float dp2;
        float f4;
        float clamp;
        int i3;
        MessageObject messageObject = this.currentMessageObject;
        int i4 = this.stickerSize;
        if (isButtonLayout(messageObject)) {
            this.stickerSize = this.giftRectSize - AndroidUtilities.dp(106.0f);
            if (isNewStyleButtonLayout()) {
                i4 = getImageSize(messageObject);
                float f5 = (this.previousWidth - i4) / 2.0f;
                float dp3 = this.textY + this.textHeight + AndroidUtilities.dp(4.0f) + AndroidUtilities.dp(16.0f);
                if (messageObject.isStoryMention()) {
                    this.avatarStoryParams.storyItem = messageObject.messageOwner.media.storyItem;
                }
                float f6 = i4;
                this.avatarStoryParams.originalAvatarRect.set(f5, dp3, f5 + f6, dp3 + f6);
                this.imageReceiver.setImageCoords(f5, dp3, f6, f6);
            } else {
                int i5 = messageObject.type;
                if (i5 == 11) {
                    ImageReceiver imageReceiver = this.imageReceiver;
                    int i6 = this.previousWidth;
                    int i7 = this.stickerSize;
                    imageReceiver.setImageCoords((i6 - i7) / 2.0f, this.textY + this.textHeight + (this.giftRectSize * 0.075f), i7, i7);
                } else if (i5 == 25) {
                    i4 = (int) (this.stickerSize * (AndroidUtilities.isTablet() ? 1.0f : 1.2f));
                    float f7 = i4;
                    this.imageReceiver.setImageCoords((this.previousWidth - i4) / 2.0f, ((this.textY + this.textHeight) + (this.giftRectSize * 0.075f)) - AndroidUtilities.dp(22.0f), f7, f7);
                } else {
                    i4 = (int) (this.stickerSize * 1.0f);
                    float f8 = i4;
                    this.imageReceiver.setImageCoords((this.previousWidth - i4) / 2.0f, ((this.textY + this.textHeight) + (this.giftRectSize * 0.075f)) - AndroidUtilities.dp(4.0f), f8, f8);
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
                    if (imageUpdater.getCurrentImageProgress() == 1.0f) {
                        this.radialProgress.setIcon(4, true, true);
                    } else {
                        this.radialProgress.setIcon(3, true, true);
                    }
                }
                this.radialProgress.draw(canvas);
            } else if (i9 == 22) {
                float uploadingInfoProgress = getUploadingInfoProgress(messageObject);
                this.radialProgress.setProgress(uploadingInfoProgress, true);
                this.radialProgress.setCircleRadius(AndroidUtilities.dp(26.0f));
                this.radialProgress.setMaxIconSize(AndroidUtilities.dp(24.0f));
                this.radialProgress.setColorKeys(Theme.key_chat_mediaLoaderPhoto, Theme.key_chat_mediaLoaderPhotoSelected, Theme.key_chat_mediaLoaderPhotoIcon, Theme.key_chat_mediaLoaderPhotoIconSelected);
                if (uploadingInfoProgress == 1.0f) {
                    this.radialProgress.setIcon(4, true, true);
                } else {
                    this.radialProgress.setIcon(3, true, true);
                }
                this.radialProgress.draw(canvas);
            }
        }
        if (this.textPaint == null || this.textLayout == null) {
            i = i8;
            i2 = 21;
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
            this.textLayout.draw(canvas);
            ChatActionCellDelegate chatActionCellDelegate = this.delegate;
            if (chatActionCellDelegate == null || chatActionCellDelegate.canDrawOutboundsContent()) {
                StaticLayout staticLayout = this.textLayout;
                i2 = 21;
                f = 2.0f;
                i = i8;
                f2 = 16.0f;
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout, this.animatedEmojiStack, 0.0f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f, staticLayout == null ? null : getAdaptiveEmojiColorFilter(staticLayout.getPaint().getColor()));
            } else {
                i = i8;
                i2 = 21;
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
        if (isButtonLayout(messageObject)) {
            canvas.save();
            float dp4 = ((this.previousWidth - this.giftRectSize) / f) + AndroidUtilities.dp(8.0f);
            if (isNewStyleButtonLayout()) {
                RectF rectF = this.backgroundRect;
                dp = (rectF != null ? rectF.top : this.textY + this.textHeight + AndroidUtilities.dp(4.0f)) + (AndroidUtilities.dp(f2) * 2) + i;
            } else {
                dp = this.textY + this.textHeight + (this.giftRectSize * 0.075f) + (messageObject.type == i2 ? i : this.stickerSize) + AndroidUtilities.dp(4.0f);
                if (messageObject.type == i2) {
                    dp += AndroidUtilities.dp(f2);
                }
                if (this.giftPremiumButtonLayout == null) {
                    dp -= AndroidUtilities.dp(24.0f);
                }
            }
            canvas.translate(dp4, dp);
            StaticLayout staticLayout2 = this.giftPremiumTitleLayout;
            if (staticLayout2 != null) {
                staticLayout2.draw(canvas);
                dp2 = dp + this.giftPremiumTitleLayout.getHeight() + AndroidUtilities.dp(messageObject.type == 25 ? 6.0f : 0.0f);
                f3 = 4.0f;
            } else {
                f3 = 4.0f;
                dp2 = dp - AndroidUtilities.dp(4.0f);
            }
            canvas.restore();
            canvas.save();
            canvas.translate(dp4, dp2 + AndroidUtilities.dp(f3));
            if (messageObject.type == 22) {
                f4 = 1.0f;
                if (this.radialProgress.getTransitionProgress() != 1.0f || this.radialProgress.getIcon() != 4) {
                    if (this.settingWallpaperLayout == null) {
                        TextPaint textPaint4 = new TextPaint();
                        this.settingWallpaperPaint = textPaint4;
                        textPaint4.setTextSize(AndroidUtilities.dp(13.0f));
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString(R.string.ActionSettingWallpaper));
                        int indexOf = spannableStringBuilder.toString().indexOf("...");
                        if (indexOf < 0) {
                            indexOf = spannableStringBuilder.toString().indexOf("…");
                            i3 = 1;
                        } else {
                            i3 = 3;
                        }
                        if (indexOf >= 0) {
                            SpannableString spannableString = new SpannableString("…");
                            UploadingDotsSpannable uploadingDotsSpannable = new UploadingDotsSpannable();
                            uploadingDotsSpannable.fixTop = true;
                            uploadingDotsSpannable.setParent(this, false);
                            spannableString.setSpan(uploadingDotsSpannable, 0, spannableString.length(), 33);
                            spannableStringBuilder.replace(indexOf, indexOf + i3, (CharSequence) spannableString);
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
                        float f9 = 1.0f - transitionProgress;
                        this.settingWallpaperPaint.setAlpha((int) (Color.alpha(color) * f9));
                        this.giftSubtitlePaint.setAlpha((int) (Color.alpha(color) * transitionProgress));
                        TextPaint textPaint5 = this.giftSubtitlePaint;
                        textPaint5.linkColor = textPaint5.getColor();
                        float f10 = (transitionProgress * 0.2f) + 0.8f;
                        canvas.save();
                        canvas.scale(f10, f10, this.giftPremiumSubtitleWidth / f, this.giftPremiumSubtitleLayout.getHeight() / f);
                        canvas.translate((this.giftPremiumSubtitleWidth - this.giftPremiumSubtitleLayout.getWidth()) / f, 0.0f);
                        this.giftPremiumSubtitleLayout.draw(canvas);
                        canvas.restore();
                        this.giftSubtitlePaint.setAlpha((int) (Color.alpha(color) * f9));
                        TextPaint textPaint6 = this.giftSubtitlePaint;
                        textPaint6.linkColor = textPaint6.getColor();
                        float f11 = (f9 * 0.2f) + 0.8f;
                        canvas.save();
                        canvas.scale(f11, f11, this.settingWallpaperLayout.getWidth() / f, this.settingWallpaperLayout.getHeight() / f);
                        this.settingWallpaperLayout.draw(canvas);
                        canvas.restore();
                        canvas.save();
                        canvas.translate(0.0f, this.settingWallpaperLayout.getHeight() + AndroidUtilities.dp(4.0f));
                        canvas.scale(f11, f11, this.settingWallpaperProgressTextLayout.getWidth() / f, this.settingWallpaperProgressTextLayout.getHeight() / f);
                        this.settingWallpaperProgressTextLayout.draw(canvas);
                        canvas.restore();
                        this.giftSubtitlePaint.setColor(color);
                        this.giftSubtitlePaint.linkColor = color;
                    } else {
                        this.settingWallpaperLayout.draw(canvas);
                        canvas.save();
                        canvas.translate(0.0f, this.settingWallpaperLayout.getHeight() + AndroidUtilities.dp(4.0f));
                        this.settingWallpaperProgressTextLayout.draw(canvas);
                        canvas.restore();
                    }
                } else {
                    canvas.save();
                    canvas.translate((this.giftPremiumSubtitleWidth - this.giftPremiumSubtitleLayout.getWidth()) / f, 0.0f);
                    this.giftPremiumSubtitleLayout.draw(canvas);
                    canvas.restore();
                }
            } else {
                f4 = 1.0f;
                if (this.giftPremiumSubtitleLayout != null) {
                    canvas.save();
                    canvas.translate((this.giftPremiumSubtitleWidth - this.giftPremiumSubtitleLayout.getWidth()) / f, 0.0f);
                    this.giftPremiumSubtitleLayout.draw(canvas);
                    canvas.restore();
                }
            }
            canvas.restore();
            if (this.giftPremiumTitleLayout == null) {
                AndroidUtilities.dp(8.0f);
            }
            this.giftPremiumSubtitleLayout.getHeight();
            StaticLayout staticLayout3 = this.giftPremiumButtonLayout;
            if (staticLayout3 != null) {
                staticLayout3.getHeight();
            }
            getHeight();
            AndroidUtilities.dp(8.0f);
            Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
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
                if (this.dimAmount > 0.0f) {
                    canvas.drawRoundRect(this.giftButtonRect, AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), this.dimPaint);
                }
                if (getMessageObject().type != 21 && getMessageObject().type != 22 && getMessageObject().type != 24) {
                    this.starsPath.rewind();
                    this.starsPath.addRoundRect(this.giftButtonRect, AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), Path.Direction.CW);
                    canvas.save();
                    canvas.clipPath(this.starsPath);
                    this.starParticlesDrawable.onDraw(canvas);
                    if (!this.starParticlesDrawable.paused) {
                        invalidate();
                    }
                    canvas.restore();
                } else {
                    invalidate();
                }
            }
            boolean z = messageObject.settingAvatar;
            if (z) {
                float f12 = this.progressToProgress;
                if (f12 != f4) {
                    this.progressToProgress = f12 + 0.10666667f;
                    clamp = Utilities.clamp(this.progressToProgress, f4, 0.0f);
                    this.progressToProgress = clamp;
                    if (clamp != 0.0f) {
                        if (this.progressView == null) {
                            this.progressView = new RadialProgressView(getContext());
                        }
                        int dp5 = AndroidUtilities.dp(f2);
                        canvas.save();
                        float f13 = this.progressToProgress;
                        canvas.scale(f13, f13, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                        this.progressView.setSize(dp5);
                        this.progressView.setProgressColor(Theme.getColor(Theme.key_chat_serviceText));
                        this.progressView.draw(canvas, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                        canvas.restore();
                    }
                    if (this.progressToProgress != f4 && this.giftPremiumButtonLayout != null) {
                        canvas.save();
                        float f14 = f4 - this.progressToProgress;
                        canvas.scale(f14, f14, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                        canvas.translate(dp4, this.giftButtonRect.top + AndroidUtilities.dp(8.0f));
                        this.giftPremiumButtonLayout.draw(canvas);
                        canvas.restore();
                    }
                    if (!messageObject.flickerLoading) {
                        if (this.loadingDrawable == null) {
                            LoadingDrawable loadingDrawable = new LoadingDrawable(this.themeDelegate);
                            this.loadingDrawable = loadingDrawable;
                            loadingDrawable.setGradientScale(f);
                            this.loadingDrawable.setAppearByGradient(true);
                            this.loadingDrawable.setColors(Theme.multAlpha(-1, 0.08f), Theme.multAlpha(-1, 0.2f), Theme.multAlpha(-1, 0.2f), Theme.multAlpha(-1, 0.7f));
                            this.loadingDrawable.strokePaint.setStrokeWidth(AndroidUtilities.dp(f4));
                        }
                        this.loadingDrawable.resetDisappear();
                        this.loadingDrawable.setBounds(this.giftButtonRect);
                        this.loadingDrawable.setRadiiDp(16.0f);
                        this.loadingDrawable.draw(canvas);
                    } else {
                        LoadingDrawable loadingDrawable2 = this.loadingDrawable;
                        if (loadingDrawable2 != null) {
                            loadingDrawable2.setBounds(this.giftButtonRect);
                            this.loadingDrawable.setRadiiDp(16.0f);
                            this.loadingDrawable.disappear();
                            this.loadingDrawable.draw(canvas);
                            if (this.loadingDrawable.isDisappeared()) {
                                this.loadingDrawable.reset();
                            }
                        }
                    }
                    canvas.restore();
                }
            }
            if (!z) {
                float f15 = this.progressToProgress;
                if (f15 != 0.0f) {
                    this.progressToProgress = f15 - 0.10666667f;
                }
            }
            clamp = Utilities.clamp(this.progressToProgress, f4, 0.0f);
            this.progressToProgress = clamp;
            if (clamp != 0.0f) {
            }
            if (this.progressToProgress != f4) {
                canvas.save();
                float f142 = f4 - this.progressToProgress;
                canvas.scale(f142, f142, this.giftButtonRect.centerX(), this.giftButtonRect.centerY());
                canvas.translate(dp4, this.giftButtonRect.top + AndroidUtilities.dp(8.0f));
                this.giftPremiumButtonLayout.draw(canvas);
                canvas.restore();
            }
            if (!messageObject.flickerLoading) {
            }
            canvas.restore();
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

    private void checkLeftRightBounds() {
        this.backgroundLeft = (int) Math.min(this.backgroundLeft, this.rect.left);
        this.backgroundRight = (int) Math.max(this.backgroundRight, this.rect.right);
    }

    public void drawBackground(Canvas canvas, boolean z) {
        Paint paint;
        Paint paint2;
        Paint paint3;
        Paint paint4;
        int i;
        int i2;
        int i3;
        Paint paint5;
        Paint paint6;
        int i4;
        float f;
        float f2;
        int i5;
        int i6;
        int i7;
        int i8;
        float f3;
        int i9;
        int i10;
        int i11;
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
        int i12 = this.overrideBackground;
        if (i12 >= 0) {
            int themedColor = getThemedColor(i12);
            if (this.overrideBackgroundPaint == null) {
                Paint paint7 = new Paint(1);
                this.overrideBackgroundPaint = paint7;
                paint7.setColor(themedColor);
                TextPaint textPaint = new TextPaint(1);
                this.overrideTextPaint = textPaint;
                textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
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
            int i13 = 0;
            for (int i14 = 0; i14 < lineCount; i14++) {
                int ceil = (int) Math.ceil(this.textLayout.getLineWidth(i14));
                if (i14 == 0 || (i11 = i13 - ceil) <= 0 || i11 > dp + dp2) {
                    i13 = ceil;
                }
                this.lineWidths.add(Integer.valueOf(i13));
            }
            for (int i15 = lineCount - 2; i15 >= 0; i15--) {
                int intValue = this.lineWidths.get(i15).intValue();
                int i16 = i13 - intValue;
                if (i16 <= 0 || i16 > dp + dp2) {
                    i13 = intValue;
                }
                this.lineWidths.set(i15, Integer.valueOf(i13));
            }
            int dp3 = AndroidUtilities.dp(4.0f);
            int measuredWidth = getMeasuredWidth() / 2;
            int dp4 = AndroidUtilities.dp(3.0f);
            int dp5 = AndroidUtilities.dp(6.0f);
            int i17 = dp - dp4;
            this.lineHeights.clear();
            this.backgroundPath.reset();
            float f4 = measuredWidth;
            this.backgroundPath.moveTo(f4, dp3);
            int i18 = 0;
            int i19 = 0;
            while (i18 < lineCount) {
                int intValue2 = this.lineWidths.get(i18).intValue();
                int i20 = dp2;
                int lineBottom = this.textLayout.getLineBottom(i18);
                int i21 = lineCount - 1;
                if (i18 < i21) {
                    paint6 = themedPaint2;
                    paint5 = themedPaint;
                    i4 = this.lineWidths.get(i18 + 1).intValue();
                } else {
                    paint5 = themedPaint;
                    paint6 = themedPaint2;
                    i4 = 0;
                }
                int i22 = lineBottom - i19;
                if (i18 == 0 || intValue2 > i13) {
                    f = 3.0f;
                    i22 += AndroidUtilities.dp(3.0f);
                } else {
                    f = 3.0f;
                }
                if (i18 == i21 || intValue2 > i4) {
                    i22 += AndroidUtilities.dp(f);
                }
                float f5 = (intValue2 / 2.0f) + f4;
                int i23 = (i18 == i21 || intValue2 >= i4 || i18 == 0 || intValue2 >= i13) ? i20 : dp5;
                if (i18 == 0 || intValue2 > i13) {
                    f2 = f4;
                    i5 = lineCount;
                    i6 = lineBottom;
                    i7 = measuredWidth;
                    i8 = i13;
                    this.rect.set((f5 - dp4) - dp, dp3, i17 + f5, (dp * 2) + dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, -90.0f, 90.0f);
                } else {
                    f2 = f4;
                    if (intValue2 < i13) {
                        i6 = lineBottom;
                        float f6 = i17 + f5;
                        i7 = measuredWidth;
                        i5 = lineCount;
                        i8 = i13;
                        this.rect.set(f6, dp3, (i23 * 2) + f6, i10 + dp3);
                        checkLeftRightBounds();
                        this.backgroundPath.arcTo(this.rect, -90.0f, -90.0f);
                    } else {
                        i5 = lineCount;
                        i6 = lineBottom;
                        i7 = measuredWidth;
                        i8 = i13;
                    }
                }
                dp3 += i22;
                if (i18 == i21 || intValue2 >= i4) {
                    f3 = 3.0f;
                } else {
                    f3 = 3.0f;
                    dp3 -= AndroidUtilities.dp(3.0f);
                    i22 -= AndroidUtilities.dp(3.0f);
                }
                if (i18 != 0 && intValue2 < i8) {
                    dp3 -= AndroidUtilities.dp(f3);
                    i22 -= AndroidUtilities.dp(f3);
                }
                this.lineHeights.add(Integer.valueOf(i22));
                if (i18 == i21 || intValue2 > i4) {
                    this.rect.set((f5 - dp4) - dp, dp3 - (dp * 2), f5 + i17, dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 0.0f, 90.0f);
                } else if (intValue2 < i4) {
                    float f7 = f5 + i17;
                    this.rect.set(f7, dp3 - i9, (i23 * 2) + f7, dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 180.0f, -90.0f);
                }
                i18++;
                i13 = intValue2;
                dp2 = i20;
                themedPaint2 = paint6;
                themedPaint = paint5;
                f4 = f2;
                i19 = i6;
                measuredWidth = i7;
                lineCount = i5;
            }
            paint = themedPaint;
            paint2 = themedPaint2;
            int i24 = dp2;
            int i25 = measuredWidth;
            int i26 = lineCount - 1;
            int i27 = i26;
            while (i27 >= 0) {
                int intValue3 = i27 != 0 ? this.lineWidths.get(i27 - 1).intValue() : 0;
                int intValue4 = this.lineWidths.get(i27).intValue();
                int intValue5 = i27 != i26 ? this.lineWidths.get(i27 + 1).intValue() : 0;
                this.textLayout.getLineBottom(i27);
                float f8 = i25 - (intValue4 / 2);
                int i28 = (i27 == i26 || intValue4 >= intValue5 || i27 == 0 || intValue4 >= intValue3) ? i24 : dp5;
                if (i27 == i26 || intValue4 > intValue5) {
                    this.rect.set(f8 - i17, dp3 - (dp * 2), dp4 + f8 + dp, dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 90.0f, 90.0f);
                } else if (intValue4 < intValue5) {
                    float f9 = f8 - i17;
                    this.rect.set(f9 - (i28 * 2), dp3 - i3, f9, dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 90.0f, -90.0f);
                }
                dp3 -= this.lineHeights.get(i27).intValue();
                if (i27 == 0 || intValue4 > intValue3) {
                    this.rect.set(f8 - i17, dp3, f8 + dp4 + dp, (dp * 2) + dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 180.0f, 90.0f);
                } else if (intValue4 < intValue3) {
                    float f10 = f8 - i17;
                    this.rect.set(f10 - (i28 * 2), dp3, f10, i2 + dp3);
                    checkLeftRightBounds();
                    this.backgroundPath.arcTo(this.rect, 0.0f, -90.0f);
                }
                i27--;
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
        int i29 = -1;
        if (z && (getAlpha() != 1.0f || isFloating())) {
            i29 = paint.getAlpha();
            i = paint2.getAlpha();
            paint4 = paint;
            paint4.setAlpha((int) (i29 * getAlpha() * (isFloating() ? 0.75f : 1.0f)));
            paint3 = paint2;
            paint3.setAlpha((int) (i * getAlpha() * (isFloating() ? 0.75f : 1.0f)));
        } else {
            paint3 = paint2;
            paint4 = paint;
            if (isFloating()) {
                i29 = paint4.getAlpha();
                i = paint3.getAlpha();
                paint4.setAlpha((int) (i29 * (isFloating() ? 0.75f : 1.0f)));
                paint3.setAlpha((int) (i * (isFloating() ? 0.75f : 1.0f)));
            } else {
                i = -1;
            }
        }
        canvas.drawPath(this.backgroundPath, paint4);
        if (hasGradientService()) {
            canvas.drawPath(this.backgroundPath, paint3);
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
            float f11 = this.textY + this.textHeight;
            if (isNewStyleButtonLayout()) {
                float dp6 = f11 + AndroidUtilities.dp(4.0f);
                AndroidUtilities.rectTmp.set(width, dp6, this.giftRectSize + width, this.backgroundRectHeight + dp6);
            } else {
                float dp7 = f11 + AndroidUtilities.dp(12.0f);
                RectF rectF = AndroidUtilities.rectTmp;
                int i30 = this.giftRectSize;
                rectF.set(width, dp7, i30 + width, i30 + dp7 + this.giftPremiumAdditionalHeight);
            }
            if (this.backgroundRect == null) {
                this.backgroundRect = new RectF();
            }
            this.backgroundRect.set(AndroidUtilities.rectTmp);
            canvas.drawRoundRect(this.backgroundRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), paint4);
            if (hasGradientService()) {
                canvas.drawRoundRect(this.backgroundRect, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), paint3);
            }
        }
        if (i29 >= 0) {
            paint4.setAlpha(i29);
            paint3.setAlpha(i);
        }
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

    public boolean hasGradientService() {
        Theme.ResourcesProvider resourcesProvider;
        return this.overrideBackgroundPaint == null && ((resourcesProvider = this.themeDelegate) == null ? Theme.hasGradientService() : resourcesProvider.hasGradientService());
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String str) {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject == null || messageObject.type != 11) {
            return;
        }
        TLRPC$PhotoSize tLRPC$PhotoSize = null;
        int i = 0;
        int size = messageObject.photoThumbs.size();
        while (true) {
            if (i >= size) {
                break;
            }
            TLRPC$PhotoSize tLRPC$PhotoSize2 = messageObject.photoThumbs.get(i);
            if (tLRPC$PhotoSize2 instanceof TLRPC$TL_photoStrippedSize) {
                tLRPC$PhotoSize = tLRPC$PhotoSize2;
                break;
            }
            i++;
        }
        this.imageReceiver.setImage(this.currentVideoLocation, ImageLoader.AUTOPLAY_FILTER, ImageLocation.getForObject(tLRPC$PhotoSize, messageObject.photoThumbsObject), "50_50_b", this.avatarDrawable, 0L, null, messageObject, 1);
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
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

    public void setInvalidateColors(boolean z) {
        if (this.invalidateColors == z) {
            return;
        }
        this.invalidateColors = z;
        invalidate();
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.themeDelegate);
    }

    protected Paint getThemedPaint(String str) {
        Theme.ResourcesProvider resourcesProvider = this.themeDelegate;
        Paint paint = resourcesProvider != null ? resourcesProvider.getPaint(str) : null;
        return paint != null ? paint : Theme.getThemePaint(str);
    }

    public void drawOutboundsContent(Canvas canvas) {
        canvas.save();
        canvas.translate(this.textXLeft, this.textY);
        StaticLayout staticLayout = this.textLayout;
        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout, this.animatedEmojiStack, 0.0f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f, staticLayout != null ? getAdaptiveEmojiColorFilter(staticLayout.getPaint().getColor()) : null);
        canvas.restore();
    }

    private boolean isButtonLayout(MessageObject messageObject) {
        int i;
        return messageObject != null && ((i = messageObject.type) == 18 || i == 25 || isNewStyleButtonLayout());
    }

    private boolean isGiftChannel(MessageObject messageObject) {
        return messageObject != null && messageObject.type == 25;
    }

    @Override // android.view.View
    public void invalidate() {
        super.invalidate();
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

    @Override // android.view.View
    public void invalidate(int i, int i2, int i3, int i4) {
        super.invalidate(i, i2, i3, i4);
        View view = this.invalidateWithParent;
        if (view != null) {
            view.invalidate();
        }
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return drawable == this.wallpaperPreviewDrawable || super.verifyDrawable(drawable);
    }

    private ColorFilter getAdaptiveEmojiColorFilter(int i) {
        if (i != this.adaptiveEmojiColor || this.adaptiveEmojiColorFilter == null) {
            this.adaptiveEmojiColor = i;
            this.adaptiveEmojiColorFilter = new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN);
        }
        return this.adaptiveEmojiColorFilter;
    }
}
