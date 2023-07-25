package org.telegram.ui.Stories;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import androidx.recyclerview.widget.ChatListItemAnimator;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.IDN;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$StoryItem;
import org.telegram.tgnet.TLRPC$TL_channels_sendAsPeers;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_messageEntityCustomEmoji;
import org.telegram.tgnet.TLRPC$TL_messageMediaUnsupported;
import org.telegram.tgnet.TLRPC$TL_stories_editStory;
import org.telegram.tgnet.TLRPC$TL_stories_exportStoryLink;
import org.telegram.tgnet.TLRPC$TL_storyItemDeleted;
import org.telegram.tgnet.TLRPC$TL_storyItemSkipped;
import org.telegram.tgnet.TLRPC$TL_storyViews;
import org.telegram.tgnet.TLRPC$TL_userStories;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.AvatarsImageView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BitmapShaderTools;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.ChatAttachAlertDocumentLayout;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.CustomPopupMenu;
import org.telegram.ui.Components.DotDividerSpan;
import org.telegram.ui.Components.EditTextCaption;
import org.telegram.ui.Components.EmojiPacksAlert;
import org.telegram.ui.Components.HintView;
import org.telegram.ui.Components.InstantCameraView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LoadingDrawable;
import org.telegram.ui.Components.MediaActivity;
import org.telegram.ui.Components.MentionsContainerView;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgress;
import org.telegram.ui.Components.Reactions.ReactionsEffectOverlay;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.ReactionsContainerLayout;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;
import org.telegram.ui.Components.voip.CellFlickerDrawable;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.NotificationsCustomSettingsActivity;
import org.telegram.ui.PinchToZoomHelper;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stories.PeerStoriesView;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.StoryViewer;
import org.telegram.ui.Stories.recorder.CaptionContainerView;
import org.telegram.ui.Stories.recorder.HintView2;
import org.telegram.ui.Stories.recorder.StoryEntry;
import org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet;
import org.telegram.ui.Stories.recorder.StoryRecorder;
import org.telegram.ui.WrappedResourceProvider;
/* loaded from: classes4.dex */
public class PeerStoriesView extends SizeNotifierFrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private static int activeCount;
    private boolean BIG_SCREEN;
    private boolean allowDrawSurface;
    Runnable allowDrawSurfaceRunnable;
    private boolean allowShare;
    private float alpha;
    boolean animateKeyboardOpening;
    private float animatingKeyboardHeight;
    private boolean attachedToWindow;
    private final AvatarDrawable avatarDrawable;
    private final BitmapShaderTools bitmapShaderTools;
    private Runnable cancellableViews;
    private ValueAnimator changeBoundAnimator;
    ChatActivityEnterView chatActivityEnterView;
    private ChatAttachAlert chatAttachAlert;
    boolean checkBlackoutMode;
    private int classGuid;
    int count;
    private int currentAccount;
    private long currentImageTime;
    public final StoryItemHolder currentStory;
    ArrayList<Integer> day;
    Delegate delegate;
    private boolean deletedPeer;
    private long dialogId;
    ArrayList<TLRPC$Document> documentsToPrepare;
    ActionBarMenuSubItem editStoryItem;
    private boolean editedPrivacy;
    private int enterViewBottomOffset;
    public boolean forceUpdateOffsets;
    PeerHeaderView headerView;
    private boolean imageChanged;
    private final ImageReceiver imageReceiver;
    boolean inBlackoutMode;
    Paint inputBackgroundPaint;
    private InstantCameraView instantCameraView;
    boolean isActive;
    private boolean isCaptionPartVisible;
    private boolean isEditing;
    private boolean isLongPressed;
    private boolean isRecording;
    boolean isSelf;
    private boolean isUploading;
    private boolean isVisible;
    ValueAnimator keyboardAnimator;
    public boolean keyboardVisible;
    float lastAnimatingKeyboardHeight;
    private long lastDrawTime;
    int lastKeyboardHeight;
    private boolean lastNoThumb;
    int lastOpenedKeyboardHeight;
    private final ImageReceiver leftPreloadImageReceiver;
    private int linesCount;
    private int linesPosition;
    private int listPosition;
    private HintView mediaBanTooltip;
    private MentionsContainerView mentionContainer;
    private boolean messageSent;
    private final FrameLayout muteIconContainer;
    private final RLottieImageView muteIconView;
    private float muteIconViewAlpha;
    private final ImageView noSoundIconView;
    final AnimationNotificationsLocker notificationsLocker;
    private Runnable onImageReceiverThumbLoaded;
    private final ImageView optionsIconView;
    private ValueAnimator outAnimator;
    private float outT;
    RoundRectOutlineProvider outlineProvider;
    private boolean paused;
    public PinchToZoomHelper pinchToZoomHelper;
    final VideoPlayerSharedScope playerSharedScope;
    CustomPopupMenu popupMenu;
    private float prevToHideProgress;
    private final StoryPrivacyButton privacyButton;
    private HintView2 privacyHint;
    float progressToDismiss;
    private AnimatedFloat progressToHideInterface;
    float progressToKeyboard;
    AnimatedFloat progressToRecording;
    float progressToReply;
    AnimatedFloat progressToStickerExpanded;
    AnimatedFloat progressToTextA;
    private int reactionsContainerIndex;
    ReactionsContainerLayout reactionsContainerLayout;
    private int realKeyboardHeight;
    private TextView replyDisabledTextView;
    private final Theme.ResourcesProvider resourcesProvider;
    private final ImageReceiver rightPreloadImageReceiver;
    private int selectedPosition;
    private View selfAvatarsContainer;
    private AvatarsImageView selfAvatarsView;
    private TextView selfStatusView;
    private FrameLayout selfView;
    public ShareAlert shareAlert;
    private final ImageView shareButton;
    final SharedResources sharedResources;
    private int shiftDp;
    boolean showViewsProgress;
    private HintView2 soundTooltip;
    StoriesController storiesController;
    private final StoryCaptionView storyCaptionView;
    public FrameLayout storyContainer;
    private CaptionContainerView storyEditCaptionView;
    final ArrayList<TLRPC$StoryItem> storyItems;
    private final StoryLinesDrawable storyLines;
    private StoryPositionView storyPositionView;
    private final StoryViewer storyViewer;
    private boolean switchEventSent;
    private int totalStoriesCount;
    public boolean unsupported;
    private FrameLayout unsupportedContainer;
    final ArrayList<StoriesController.UploadingStory> uploadingStories;
    ArrayList<Uri> uriesToPrepare;
    TLRPC$TL_userStories userStories;
    private long videoDuration;
    private float viewsThumbAlpha;
    private ImageReceiver viewsThumbImageReceiver;

    /* loaded from: classes4.dex */
    public interface Delegate {
        int getKeyboardHeight();

        float getProgressToDismiss();

        boolean isClosed();

        void onPeerSelected(long j, int i);

        void preparePlayer(ArrayList<TLRPC$Document> arrayList, ArrayList<Uri> arrayList2);

        boolean releasePlayer(Runnable runnable);

        void requestAdjust(boolean z);

        void requestPlayer(TLRPC$Document tLRPC$Document, Uri uri, long j, VideoPlayerSharedScope videoPlayerSharedScope);

        void setAllowTouchesByViewPager(boolean z);

        void setBulletinIsVisible(boolean z);

        void setHideEnterViewProgress(float f);

        void setIsCaption(boolean z);

        void setIsCaptionPartVisible(boolean z);

        void setIsHintVisible(boolean z);

        void setIsInPinchToZoom(boolean z);

        void setIsRecording(boolean z);

        void setIsSwiping(boolean z);

        void setIsWaiting(boolean z);

        void setKeyboardVisible(boolean z);

        void setPopupIsVisible(boolean z);

        void shouldSwitchToNext();

        void showDialog(Dialog dialog);

        void switchToNextAndRemoveCurrentPeer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean drawLinesAsCounter() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hideCaptionWithInterface() {
        return true;
    }

    public boolean isSelectedPeer() {
        return false;
    }

    static /* synthetic */ long access$2314(PeerStoriesView peerStoriesView, long j) {
        long j2 = peerStoriesView.currentImageTime + j;
        peerStoriesView.currentImageTime = j2;
        return j2;
    }

    public PeerStoriesView(final Context context, final StoryViewer storyViewer, final SharedResources sharedResources, final Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.allowDrawSurface = true;
        this.shiftDp = -5;
        this.alpha = 1.0f;
        StoryItemHolder storyItemHolder = new StoryItemHolder();
        this.currentStory = storyItemHolder;
        this.progressToKeyboard = -1.0f;
        this.progressToDismiss = -1.0f;
        this.lastAnimatingKeyboardHeight = -1.0f;
        this.classGuid = ConnectionsManager.generateClassGuid();
        this.progressToHideInterface = new AnimatedFloat(this);
        this.pinchToZoomHelper = new PinchToZoomHelper();
        this.muteIconViewAlpha = 1.0f;
        this.uriesToPrepare = new ArrayList<>();
        this.documentsToPrepare = new ArrayList<>();
        this.allowDrawSurfaceRunnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView.22
            @Override // java.lang.Runnable
            public void run() {
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                if (peerStoriesView.isActive && peerStoriesView.allowDrawSurface) {
                    PeerStoriesView.this.delegate.setIsSwiping(false);
                }
            }
        };
        this.progressToRecording = new AnimatedFloat(this);
        this.progressToTextA = new AnimatedFloat(this);
        this.progressToStickerExpanded = new AnimatedFloat(this);
        this.pinchToZoomHelper.setCallback(new PinchToZoomHelper.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView.1
            @Override // org.telegram.ui.PinchToZoomHelper.Callback
            public /* synthetic */ TextureView getCurrentTextureView() {
                return PinchToZoomHelper.Callback.-CC.$default$getCurrentTextureView(this);
            }

            @Override // org.telegram.ui.PinchToZoomHelper.Callback
            public void onZoomStarted(MessageObject messageObject) {
                PeerStoriesView.this.delegate.setIsInPinchToZoom(true);
            }

            @Override // org.telegram.ui.PinchToZoomHelper.Callback
            public void onZoomFinished(MessageObject messageObject) {
                PeerStoriesView.this.delegate.setIsInPinchToZoom(false);
            }
        });
        this.playerSharedScope = new VideoPlayerSharedScope();
        this.notificationsLocker = new AnimationNotificationsLocker();
        this.storyItems = new ArrayList<>();
        this.uploadingStories = new ArrayList<>();
        ImageReceiver imageReceiver = new ImageReceiver() { // from class: org.telegram.ui.Stories.PeerStoriesView.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.messenger.ImageReceiver
            public boolean setImageBitmapByKey(Drawable drawable, String str, int i, boolean z, int i2) {
                boolean imageBitmapByKey = super.setImageBitmapByKey(drawable, str, i, z, i2);
                if (i == 1 && PeerStoriesView.this.onImageReceiverThumbLoaded != null) {
                    PeerStoriesView.this.onImageReceiverThumbLoaded.run();
                    PeerStoriesView.this.onImageReceiverThumbLoaded = null;
                }
                return imageBitmapByKey;
            }
        };
        this.imageReceiver = imageReceiver;
        imageReceiver.setCrossfadeWithOldImage(false);
        imageReceiver.setAllowLoadingOnAttachedOnly(true);
        imageReceiver.ignoreNotifications = true;
        imageReceiver.setFileLoadingPriority(0);
        ImageReceiver imageReceiver2 = new ImageReceiver();
        this.leftPreloadImageReceiver = imageReceiver2;
        imageReceiver2.setAllowLoadingOnAttachedOnly(true);
        imageReceiver2.ignoreNotifications = true;
        imageReceiver2.setFileLoadingPriority(0);
        ImageReceiver imageReceiver3 = new ImageReceiver();
        this.rightPreloadImageReceiver = imageReceiver3;
        imageReceiver3.setAllowLoadingOnAttachedOnly(true);
        imageReceiver3.ignoreNotifications = true;
        imageReceiver3.setFileLoadingPriority(0);
        imageReceiver.setPreloadingReceivers(Arrays.asList(imageReceiver2, imageReceiver3));
        this.avatarDrawable = new AvatarDrawable();
        this.storyViewer = storyViewer;
        this.sharedResources = sharedResources;
        this.bitmapShaderTools = sharedResources.bitmapShaderTools;
        this.storiesController = MessagesController.getInstance(UserConfig.selectedAccount).getStoriesController();
        sharedResources.dimPaint.setColor(-16777216);
        this.inputBackgroundPaint = new Paint(1);
        this.resourcesProvider = resourcesProvider;
        setClipChildren(false);
        3 r0 = new 3(context, sharedResources, storyViewer);
        this.storyContainer = r0;
        r0.setClipChildren(false);
        4 r15 = new 4(getContext(), storyViewer.resourcesProvider, storyViewer, resourcesProvider);
        this.storyCaptionView = r15;
        r15.captionTextview.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (PeerStoriesView.this.storyCaptionView.expanded) {
                    PeerStoriesView.this.storyCaptionView.collapse();
                    return;
                }
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                peerStoriesView.checkBlackoutMode = true;
                peerStoriesView.storyCaptionView.expand();
            }
        });
        ImageView imageView = new ImageView(context);
        this.shareButton = imageView;
        imageView.setImageDrawable(sharedResources.shareDrawable);
        int dp = AndroidUtilities.dp(8.0f);
        imageView.setPadding(dp, dp, dp, dp);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$new$0(view);
            }
        });
        imageReceiver.setAllowLoadingOnAttachedOnly(true);
        imageReceiver.setParentView(this.storyContainer);
        if (Build.VERSION.SDK_INT >= 21) {
            RoundRectOutlineProvider roundRectOutlineProvider = new RoundRectOutlineProvider(10);
            this.outlineProvider = roundRectOutlineProvider;
            this.storyContainer.setOutlineProvider(roundRectOutlineProvider);
            this.storyContainer.setClipToOutline(true);
        }
        addView(this.storyContainer);
        PeerHeaderView peerHeaderView = new PeerHeaderView(context, storyItemHolder);
        this.headerView = peerHeaderView;
        peerHeaderView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$new$1(storyViewer, view);
            }
        });
        this.storyContainer.addView(this.headerView, LayoutHelper.createFrame(-1, -2.0f, 0, 0.0f, 17.0f, 0.0f, 0.0f));
        addView(imageView, LayoutHelper.createFrame(40, 40.0f, 5, 10.0f, 10.0f, 10.0f, 10.0f));
        ImageView imageView2 = new ImageView(context);
        this.optionsIconView = imageView2;
        imageView2.setImageDrawable(sharedResources.optionsDrawable);
        imageView2.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
        imageView2.setBackground(Theme.createSelectorDrawable(-1));
        this.storyContainer.addView(imageView2, LayoutHelper.createFrame(40, 40.0f, 53, 2.0f, 15.0f, 2.0f, 0.0f));
        imageView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$new$2(resourcesProvider, context, storyViewer, sharedResources, view);
            }
        });
        FrameLayout frameLayout = new FrameLayout(context);
        this.muteIconContainer = frameLayout;
        this.storyContainer.addView(frameLayout, LayoutHelper.createFrame(40, 40.0f, 53, 2.0f, 15.0f, 42.0f, 0.0f));
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.muteIconView = rLottieImageView;
        rLottieImageView.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f));
        frameLayout.addView(rLottieImageView);
        ImageView imageView3 = new ImageView(context);
        this.noSoundIconView = imageView3;
        imageView3.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f));
        imageView3.setImageDrawable(sharedResources.noSoundDrawable);
        frameLayout.addView(imageView3);
        imageView3.setVisibility(8);
        StoryPrivacyButton storyPrivacyButton = new StoryPrivacyButton(context);
        this.privacyButton = storyPrivacyButton;
        storyPrivacyButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$new$4(view);
            }
        });
        this.storyContainer.addView(storyPrivacyButton, LayoutHelper.createFrame(60, 40.0f, 53, 2.0f, 15.0f, 42.0f, 0.0f));
        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$new$5(storyViewer, context, view);
            }
        });
        this.storyLines = new StoryLinesDrawable(this, sharedResources);
        this.storyContainer.addView(r15, LayoutHelper.createFrame(-1, -1.0f, 0, 0.0f, 64.0f, 0.0f, 0.0f));
        frameLayout.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100)));
        imageView2.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100)));
        imageView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 3 extends HwFrameLayout {
        boolean drawOverlayed;
        CellFlickerDrawable loadingDrawable;
        AnimatedFloat loadingDrawableAlpha;
        AnimatedFloat loadingDrawableAlpha2;
        AnimatedFloat progressToAudio;
        AnimatedFloat progressToFullBlackoutA;
        boolean splitDrawing;
        final /* synthetic */ SharedResources val$sharedResources;
        final /* synthetic */ StoryViewer val$storyViewer;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        3(Context context, SharedResources sharedResources, StoryViewer storyViewer) {
            super(context);
            this.val$sharedResources = sharedResources;
            this.val$storyViewer = storyViewer;
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            this.progressToAudio = new AnimatedFloat(this, 150L, cubicBezierInterpolator);
            this.progressToFullBlackoutA = new AnimatedFloat(this, 150L, cubicBezierInterpolator);
            this.loadingDrawable = new CellFlickerDrawable();
            this.loadingDrawableAlpha2 = new AnimatedFloat(this);
            this.loadingDrawableAlpha = new AnimatedFloat(this);
            this.loadingDrawableAlpha2.setDuration(500L);
            this.loadingDrawableAlpha.setDuration(100L);
        }

        /* JADX WARN: Removed duplicated region for block: B:150:0x051c  */
        /* JADX WARN: Removed duplicated region for block: B:153:0x0526  */
        /* JADX WARN: Removed duplicated region for block: B:170:0x0579  */
        /* JADX WARN: Removed duplicated region for block: B:172:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:58:0x0181  */
        /* JADX WARN: Removed duplicated region for block: B:59:0x0184  */
        /* JADX WARN: Removed duplicated region for block: B:62:0x0192  */
        @Override // android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void dispatchDraw(Canvas canvas) {
            float f;
            int i;
            PeerStoriesView peerStoriesView;
            boolean z;
            float f2;
            StoryViewer.VideoPlayerHolder videoPlayerHolder;
            PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
            if (!peerStoriesView2.isActive) {
                peerStoriesView2.headerView.backupImageView.getImageReceiver().setVisible(true, true);
            }
            PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
            if (!peerStoriesView3.unsupported) {
                if (peerStoriesView3.playerSharedScope.renderView != null) {
                    invalidate();
                }
                canvas.save();
                PeerStoriesView.this.pinchToZoomHelper.applyTransform(canvas);
                PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                VideoPlayerSharedScope videoPlayerSharedScope = peerStoriesView4.playerSharedScope;
                View view = videoPlayerSharedScope.renderView;
                if (view != null && videoPlayerSharedScope.firstFrameRendered) {
                    if (!peerStoriesView4.imageReceiver.hasBitmapImage()) {
                        this.val$sharedResources.imageBackgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight() + 1);
                        this.val$sharedResources.imageBackgroundDrawable.draw(canvas);
                    }
                    PeerStoriesView.this.imageReceiver.setImageCoords(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight() + 1);
                    PeerStoriesView.this.imageReceiver.draw(canvas);
                    PeerStoriesView peerStoriesView5 = PeerStoriesView.this;
                    if (peerStoriesView5.isActive) {
                        StoryViewer storyViewer = this.val$storyViewer;
                        boolean z2 = storyViewer.USE_SURFACE_VIEW;
                        if (!z2 || (videoPlayerHolder = peerStoriesView5.playerSharedScope.player) == null || !videoPlayerHolder.paused || storyViewer.playerStubBitmap == null || !videoPlayerHolder.stubAvailable) {
                            if (!z2 || peerStoriesView5.allowDrawSurface) {
                                PeerStoriesView.this.playerSharedScope.renderView.draw(canvas);
                            }
                        } else {
                            canvas.save();
                            canvas.scale(getMeasuredWidth() / this.val$storyViewer.playerStubBitmap.getWidth(), getMeasuredHeight() / this.val$storyViewer.playerStubBitmap.getHeight());
                            StoryViewer storyViewer2 = this.val$storyViewer;
                            canvas.drawBitmap(storyViewer2.playerStubBitmap, 0.0f, 0.0f, storyViewer2.playerStubPaint);
                            canvas.restore();
                        }
                    }
                } else {
                    if (view != null) {
                        invalidate();
                    }
                    PeerStoriesView peerStoriesView6 = PeerStoriesView.this;
                    if (!peerStoriesView6.currentStory.skipped) {
                        if (!peerStoriesView6.imageReceiver.hasBitmapImage()) {
                            this.val$sharedResources.imageBackgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight() + 1);
                            this.val$sharedResources.imageBackgroundDrawable.draw(canvas);
                        }
                        PeerStoriesView.this.imageReceiver.setImageCoords(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight() + 1);
                        PeerStoriesView.this.imageReceiver.draw(canvas);
                    } else {
                        canvas.drawColor(ColorUtils.blendARGB(-16777216, -1, 0.2f));
                    }
                }
                canvas.restore();
                if (PeerStoriesView.this.imageChanged) {
                    this.loadingDrawableAlpha2.set(0.0f, true);
                    this.loadingDrawableAlpha.set(0.0f, true);
                }
                AnimatedFloat animatedFloat = this.loadingDrawableAlpha2;
                if (!PeerStoriesView.this.imageReceiver.hasNotThumb()) {
                    PeerStoriesView peerStoriesView7 = PeerStoriesView.this;
                    VideoPlayerSharedScope videoPlayerSharedScope2 = peerStoriesView7.playerSharedScope;
                    if ((videoPlayerSharedScope2.renderView == null || !videoPlayerSharedScope2.firstFrameRendered) && peerStoriesView7.currentStory.uploadingStory == null) {
                        f2 = 1.0f;
                        animatedFloat.set(f2);
                        this.loadingDrawableAlpha.set(this.loadingDrawableAlpha2.get() != 1.0f ? 1.0f : 0.0f);
                        if (this.loadingDrawableAlpha.get() > 0.0f) {
                            RectF rectF = AndroidUtilities.rectTmp;
                            rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                            this.loadingDrawable.setAlpha((int) (this.loadingDrawableAlpha.get() * 255.0f));
                            this.loadingDrawable.setParentWidth(getMeasuredWidth() * 2);
                            CellFlickerDrawable cellFlickerDrawable = this.loadingDrawable;
                            cellFlickerDrawable.animationSpeedScale = 1.3f;
                            cellFlickerDrawable.draw(canvas, rectF, AndroidUtilities.dp(10.0f), this);
                        }
                        PeerStoriesView.this.imageChanged = false;
                    }
                }
                f2 = 0.0f;
                animatedFloat.set(f2);
                this.loadingDrawableAlpha.set(this.loadingDrawableAlpha2.get() != 1.0f ? 1.0f : 0.0f);
                if (this.loadingDrawableAlpha.get() > 0.0f) {
                }
                PeerStoriesView.this.imageChanged = false;
            } else {
                canvas.drawColor(ColorUtils.blendARGB(-16777216, -1, 0.2f));
            }
            if (!PeerStoriesView.this.lastNoThumb && PeerStoriesView.this.imageReceiver.hasNotThumb()) {
                PeerStoriesView.this.lastNoThumb = true;
                PeerStoriesView.this.invalidate();
            }
            float hideInterfaceAlpha = PeerStoriesView.this.getHideInterfaceAlpha();
            int i2 = (int) (hideInterfaceAlpha * 255.0f);
            this.val$sharedResources.topOverlayGradient.setAlpha(i2);
            this.val$sharedResources.topOverlayGradient.draw(canvas);
            PeerStoriesView peerStoriesView8 = PeerStoriesView.this;
            if (peerStoriesView8.isSelf || !peerStoriesView8.BIG_SCREEN || PeerStoriesView.this.storyCaptionView.getVisibility() == 0) {
                if (PeerStoriesView.this.storyCaptionView.getVisibility() != 0) {
                    int dp = AndroidUtilities.dp(PeerStoriesView.this.BIG_SCREEN ? 56.0f : 110.0f);
                    PeerStoriesView peerStoriesView9 = PeerStoriesView.this;
                    if ((peerStoriesView9.isSelf || !peerStoriesView9.BIG_SCREEN) && PeerStoriesView.this.storyCaptionView.getVisibility() == 0) {
                        dp = (int) (dp * 2.5f);
                    }
                    this.val$sharedResources.bottomOverlayGradient.setBounds(0, PeerStoriesView.this.storyContainer.getMeasuredHeight() - dp, getMeasuredWidth(), PeerStoriesView.this.storyContainer.getMeasuredHeight());
                    this.val$sharedResources.bottomOverlayGradient.setAlpha(i2);
                    this.val$sharedResources.bottomOverlayGradient.draw(canvas);
                } else {
                    int dp2 = AndroidUtilities.dp(72.0f);
                    int textTop = ((int) (PeerStoriesView.this.storyCaptionView.getTextTop() - AndroidUtilities.dp(24.0f))) + PeerStoriesView.this.storyCaptionView.getTop();
                    int i3 = textTop + dp2;
                    float measuredHeight = getMeasuredHeight() * 0.65f;
                    boolean hideCaptionWithInterface = PeerStoriesView.this.hideCaptionWithInterface();
                    float f3 = textTop;
                    if ((measuredHeight - f3) / AndroidUtilities.dp(60.0f) > 0.0f && PeerStoriesView.this.storyCaptionView.isTouched() && PeerStoriesView.this.storyCaptionView.hasScroll()) {
                        if ((measuredHeight - (((int) (PeerStoriesView.this.storyCaptionView.getMaxTop() - AndroidUtilities.dp(24.0f))) + PeerStoriesView.this.storyCaptionView.getTop())) / AndroidUtilities.dp(60.0f) > 0.0f) {
                            PeerStoriesView.this.inBlackoutMode = true;
                        }
                    } else {
                        PeerStoriesView peerStoriesView10 = PeerStoriesView.this;
                        if (!peerStoriesView10.checkBlackoutMode) {
                            if (peerStoriesView10.storyCaptionView.getProgressToBlackout() == 0.0f) {
                                PeerStoriesView.this.inBlackoutMode = false;
                            }
                        } else {
                            peerStoriesView10.checkBlackoutMode = false;
                            if ((measuredHeight - (((int) (peerStoriesView10.storyCaptionView.getMaxTop() - AndroidUtilities.dp(24.0f))) + PeerStoriesView.this.storyCaptionView.getTop())) / AndroidUtilities.dp(60.0f) > 0.0f) {
                                PeerStoriesView.this.inBlackoutMode = true;
                            }
                        }
                    }
                    float f4 = hideCaptionWithInterface ? hideInterfaceAlpha : 1.0f;
                    f = this.progressToFullBlackoutA.set(PeerStoriesView.this.inBlackoutMode ? 1.0f : 0.0f);
                    if (f > 0.0f) {
                        this.splitDrawing = true;
                        this.drawOverlayed = false;
                        super.dispatchDraw(canvas);
                        this.splitDrawing = false;
                        drawLines(canvas);
                        this.val$sharedResources.gradientBackgroundPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (153.0f * f * f4)));
                        canvas.drawPaint(this.val$sharedResources.gradientBackgroundPaint);
                    }
                    if (f < 1.0f) {
                        if (PeerStoriesView.this.progressToDismiss != 0.0f) {
                            i = i3;
                            canvas.saveLayerAlpha(0.0f, f3, getMeasuredWidth(), getMeasuredHeight(), 255, 31);
                        } else {
                            i = i3;
                            canvas.save();
                        }
                        float f5 = 1.0f - f;
                        this.val$sharedResources.gradientBackgroundPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (129.03f * f5 * f4)));
                        this.val$sharedResources.bottomOverlayGradient.setAlpha((int) (f5 * 255.0f * f4));
                        this.val$sharedResources.bottomOverlayGradient.setBounds(0, textTop, getMeasuredWidth(), i);
                        this.val$sharedResources.bottomOverlayGradient.draw(canvas);
                        canvas.drawRect(0.0f, i, getMeasuredWidth(), getMeasuredHeight(), this.val$sharedResources.gradientBackgroundPaint);
                        canvas.restore();
                    }
                    if (f > 0.0f && PeerStoriesView.this.storyCaptionView.getAlpha() > 0.0f) {
                        PeerStoriesView.this.storyCaptionView.disableDraw(false);
                        if (PeerStoriesView.this.storyCaptionView.getAlpha() != 1.0f) {
                            canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), (int) (PeerStoriesView.this.storyCaptionView.getAlpha() * 255.0f), 31);
                        } else {
                            canvas.save();
                        }
                        canvas.translate(PeerStoriesView.this.storyCaptionView.getX(), PeerStoriesView.this.storyCaptionView.getY() - PeerStoriesView.this.storyCaptionView.getScrollY());
                        PeerStoriesView.this.storyCaptionView.draw(canvas);
                        canvas.restore();
                    }
                    PeerStoriesView.this.storyCaptionView.disableDraw(f > 0.0f);
                    if (f > 0.0f) {
                        this.splitDrawing = true;
                        this.drawOverlayed = true;
                        super.dispatchDraw(canvas);
                        this.splitDrawing = false;
                    }
                    if (PeerStoriesView.this.viewsThumbAlpha == 0.0f && PeerStoriesView.this.viewsThumbImageReceiver != null) {
                        PeerStoriesView.this.viewsThumbImageReceiver.setImageCoords(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight() + 1);
                        PeerStoriesView.this.viewsThumbImageReceiver.setAlpha(PeerStoriesView.this.viewsThumbAlpha);
                        PeerStoriesView.this.viewsThumbImageReceiver.draw(canvas);
                        PeerStoriesView.this.viewsThumbImageReceiver.setAlpha(1.0f);
                    }
                    this.progressToAudio.set(PeerStoriesView.this.isRecording ? 1.0f : 0.0f);
                    peerStoriesView = PeerStoriesView.this;
                    if (peerStoriesView.isActive) {
                        if (peerStoriesView.storyCaptionView.getVisibility() == 0) {
                            PeerStoriesView peerStoriesView11 = PeerStoriesView.this;
                            if (peerStoriesView11.inBlackoutMode || peerStoriesView11.storyCaptionView.isTouched()) {
                                z = true;
                                PeerStoriesView peerStoriesView12 = PeerStoriesView.this;
                                peerStoriesView12.isCaptionPartVisible = peerStoriesView12.storyCaptionView.getVisibility() != 0 && PeerStoriesView.this.storyCaptionView.getProgressToBlackout() > 0.0f;
                                PeerStoriesView.this.delegate.setIsCaption(z);
                                PeerStoriesView peerStoriesView13 = PeerStoriesView.this;
                                peerStoriesView13.delegate.setIsCaptionPartVisible(peerStoriesView13.isCaptionPartVisible);
                            }
                        }
                        z = false;
                        PeerStoriesView peerStoriesView122 = PeerStoriesView.this;
                        peerStoriesView122.isCaptionPartVisible = peerStoriesView122.storyCaptionView.getVisibility() != 0 && PeerStoriesView.this.storyCaptionView.getProgressToBlackout() > 0.0f;
                        PeerStoriesView.this.delegate.setIsCaption(z);
                        PeerStoriesView peerStoriesView132 = PeerStoriesView.this;
                        peerStoriesView132.delegate.setIsCaptionPartVisible(peerStoriesView132.isCaptionPartVisible);
                    }
                    if (f > 0.0f) {
                        super.dispatchDraw(canvas);
                        drawLines(canvas);
                        return;
                    }
                    return;
                }
            }
            f = 0.0f;
            if (PeerStoriesView.this.viewsThumbAlpha == 0.0f) {
            }
            this.progressToAudio.set(PeerStoriesView.this.isRecording ? 1.0f : 0.0f);
            peerStoriesView = PeerStoriesView.this;
            if (peerStoriesView.isActive) {
            }
            if (f > 0.0f) {
            }
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (this.splitDrawing) {
                if (Bulletin.getVisibleBulletin() != null && view == Bulletin.getVisibleBulletin().getLayout()) {
                    if (this.drawOverlayed) {
                        return super.drawChild(canvas, view, j);
                    }
                    return true;
                }
                return super.drawChild(canvas, view, j);
            }
            return super.drawChild(canvas, view, j);
        }

        private void drawLines(Canvas canvas) {
            float clamp;
            if (PeerStoriesView.this.imageReceiver.hasNotThumb() || (PeerStoriesView.this.currentStory.isVideo && PeerStoriesView.this.playerSharedScope.firstFrameRendered)) {
                PeerStoriesView.this.currentStory.checkSendView();
            }
            float hideInterfaceAlpha = PeerStoriesView.this.getHideInterfaceAlpha();
            if (!PeerStoriesView.this.currentStory.isVideo()) {
                if (!PeerStoriesView.this.paused) {
                    PeerStoriesView peerStoriesView = PeerStoriesView.this;
                    if (peerStoriesView.isActive && !peerStoriesView.isUploading && !PeerStoriesView.this.isEditing && PeerStoriesView.this.imageReceiver.hasNotThumb()) {
                        long currentTimeMillis = System.currentTimeMillis();
                        if (PeerStoriesView.this.lastDrawTime != 0 && !PeerStoriesView.this.isCaptionPartVisible) {
                            PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                            PeerStoriesView.access$2314(peerStoriesView2, currentTimeMillis - peerStoriesView2.lastDrawTime);
                        }
                        PeerStoriesView.this.lastDrawTime = currentTimeMillis;
                        clamp = Utilities.clamp(((float) PeerStoriesView.this.currentImageTime) / 10000.0f, 1.0f, 0.0f);
                        invalidate();
                    }
                }
                clamp = Utilities.clamp(((float) PeerStoriesView.this.currentImageTime) / 10000.0f, 1.0f, 0.0f);
            } else {
                PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                StoryViewer.VideoPlayerHolder videoPlayerHolder = peerStoriesView3.playerSharedScope.player;
                clamp = videoPlayerHolder != null ? Utilities.clamp(videoPlayerHolder.getPlaybackProgress(peerStoriesView3.videoDuration), 1.0f, 0.0f) : 0.0f;
                invalidate();
            }
            float f = clamp;
            if (!PeerStoriesView.this.switchEventSent && f == 1.0f && (!PeerStoriesView.this.currentStory.isVideo || !PeerStoriesView.this.isCaptionPartVisible)) {
                PeerStoriesView.this.switchEventSent = true;
                post(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$3$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.3.this.lambda$drawLines$0();
                    }
                });
            }
            if (this.val$storyViewer.storiesList != null) {
                if (PeerStoriesView.this.storyPositionView == null) {
                    PeerStoriesView.this.storyPositionView = new StoryPositionView();
                }
                PeerStoriesView.this.storyPositionView.draw(canvas, (1.0f - PeerStoriesView.this.outT) * PeerStoriesView.this.alpha * hideInterfaceAlpha, PeerStoriesView.this.listPosition, this.val$storyViewer.storiesList.getCount(), this, PeerStoriesView.this.headerView);
            }
            canvas.save();
            canvas.translate(0.0f, AndroidUtilities.dp(8.0f) - (AndroidUtilities.dp(8.0f) * PeerStoriesView.this.outT));
            PeerStoriesView.this.storyLines.draw(canvas, getMeasuredWidth(), PeerStoriesView.this.linesPosition, f, PeerStoriesView.this.linesCount, hideInterfaceAlpha, PeerStoriesView.this.alpha * (1.0f - PeerStoriesView.this.outT), PeerStoriesView.this.currentStory.isVideo() && PeerStoriesView.this.playerSharedScope.isBuffering());
            canvas.restore();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$drawLines$0() {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            if (peerStoriesView.delegate != null) {
                if (peerStoriesView.isUploading || PeerStoriesView.this.isEditing) {
                    if (!PeerStoriesView.this.currentStory.isVideo()) {
                        PeerStoriesView.this.currentImageTime = 0L;
                        return;
                    } else {
                        PeerStoriesView.this.playerSharedScope.player.loopBack();
                        return;
                    }
                }
                PeerStoriesView.this.delegate.shouldSwitchToNext();
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.3.1
                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ boolean allowLayoutChanges() {
                    return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public boolean clipWithGradient(int i) {
                    return i == 1 || i == 2;
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ void onBottomOffsetChange(float f) {
                    Bulletin.Delegate.-CC.$default$onBottomOffsetChange(this, f);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public int getTopOffset(int i) {
                    return AndroidUtilities.dp(58.0f);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public void onShow(Bulletin bulletin) {
                    Delegate delegate;
                    if (bulletin == null || bulletin.tag != 2 || (delegate = PeerStoriesView.this.delegate) == null) {
                        return;
                    }
                    delegate.setBulletinIsVisible(true);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public void onHide(Bulletin bulletin) {
                    Delegate delegate;
                    if (bulletin == null || bulletin.tag != 2 || (delegate = PeerStoriesView.this.delegate) == null) {
                        return;
                    }
                    delegate.setBulletinIsVisible(false);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public int getBottomOffset(int i) {
                    if (PeerStoriesView.this.BIG_SCREEN) {
                        return 0;
                    }
                    return AndroidUtilities.dp(64.0f);
                }
            });
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            Bulletin.removeDelegate(this);
            Delegate delegate = PeerStoriesView.this.delegate;
            if (delegate != null) {
                delegate.setBulletinIsVisible(false);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) PeerStoriesView.this.muteIconContainer.getLayoutParams();
            if (PeerStoriesView.this.drawLinesAsCounter()) {
                layoutParams.rightMargin = AndroidUtilities.dp(2.0f);
                layoutParams.topMargin = AndroidUtilities.dp(55.0f);
            } else {
                layoutParams.rightMargin = AndroidUtilities.dp(42.0f);
                layoutParams.topMargin = AndroidUtilities.dp(15.0f);
            }
            super.onMeasure(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 4 extends StoryCaptionView {
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;
        final /* synthetic */ StoryViewer val$storyViewer;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        4(Context context, Theme.ResourcesProvider resourcesProvider, StoryViewer storyViewer, Theme.ResourcesProvider resourcesProvider2) {
            super(context, resourcesProvider);
            this.val$storyViewer = storyViewer;
            this.val$resourcesProvider = resourcesProvider2;
        }

        @Override // org.telegram.ui.Stories.StoryCaptionView
        public void onLinkClick(CharacterStyle characterStyle, View view) {
            if (characterStyle instanceof URLSpanUserMention) {
                TLRPC$User user = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Utilities.parseLong(((URLSpanUserMention) characterStyle).getURL()));
                if (user != null) {
                    MessagesController.openChatOrProfileWith(user, null, this.val$storyViewer.fragment, 0, false);
                }
            } else if (characterStyle instanceof URLSpanNoUnderline) {
                String url = ((URLSpanNoUnderline) characterStyle).getURL();
                String extractUsername = Browser.extractUsername(url);
                if (extractUsername != null) {
                    String lowerCase = extractUsername.toLowerCase();
                    if (url.startsWith("@")) {
                        MessagesController.getInstance(PeerStoriesView.this.currentAccount).openByUserName(lowerCase, this.val$storyViewer.fragment, 0, null);
                        return;
                    } else {
                        processExternalUrl(0, url, characterStyle, false);
                        return;
                    }
                }
                processExternalUrl(0, url, characterStyle, false);
            } else if (characterStyle instanceof URLSpan) {
                processExternalUrl(2, ((URLSpan) characterStyle).getURL(), characterStyle, characterStyle instanceof URLSpanReplacement);
            } else if (characterStyle instanceof URLSpanMono) {
                ((URLSpanMono) characterStyle).copyToClipboard();
                BulletinFactory.of(PeerStoriesView.this.storyContainer, this.val$resourcesProvider).createCopyBulletin(LocaleController.getString("TextCopied", R.string.TextCopied)).show();
            } else if (characterStyle instanceof ClickableSpan) {
                ((ClickableSpan) characterStyle).onClick(view);
            }
        }

        private void processExternalUrl(int i, String str, CharacterStyle characterStyle, boolean z) {
            if (z || AndroidUtilities.shouldShowUrlInAlert(str)) {
                if (i == 0 || i == 2) {
                    AlertsCreator.showOpenUrlAlert(this.val$storyViewer.fragment, str, true, true, true, (characterStyle instanceof URLSpanReplacement) && (((URLSpanReplacement) characterStyle).getTextStyleRun().flags & 1024) != 0, null, this.val$resourcesProvider);
                } else if (i == 1) {
                    AlertsCreator.showOpenUrlAlert(this.val$storyViewer.fragment, str, true, true, false, null, this.val$resourcesProvider);
                }
            } else if (i == 0) {
                Browser.openUrl(getContext(), Uri.parse(str), true, true, null);
            } else if (i == 1) {
                Browser.openUrl(getContext(), Uri.parse(str), false, false, null);
            } else if (i == 2) {
                Browser.openUrl(getContext(), Uri.parse(str), false, true, null);
            }
        }

        @Override // org.telegram.ui.Stories.StoryCaptionView
        public void onLinkLongPress(final URLSpan uRLSpan, final View view, final Runnable runnable) {
            final String url = uRLSpan.getURL();
            String url2 = uRLSpan.getURL();
            try {
                try {
                    Uri parse = Uri.parse(url2);
                    url2 = Browser.replaceHostname(parse, IDN.toUnicode(parse.getHost(), 1));
                } catch (Exception e) {
                    FileLog.e((Throwable) e, false);
                }
                url2 = URLDecoder.decode(url2.replaceAll("\\+", "%2b"), "UTF-8");
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            try {
                performHapticFeedback(0, 1);
            } catch (Exception unused) {
            }
            BottomSheet.Builder builder = new BottomSheet.Builder(getContext(), false, this.val$resourcesProvider);
            builder.setTitle(url2);
            builder.setTitleMultipleLines(true);
            StoryItemHolder storyItemHolder = PeerStoriesView.this.currentStory;
            CharSequence[] charSequenceArr = (storyItemHolder == null || storyItemHolder.allowScreenshots()) ? new CharSequence[]{LocaleController.getString("Open", R.string.Open), LocaleController.getString("Copy", R.string.Copy)} : new CharSequence[]{LocaleController.getString("Open", R.string.Open)};
            final Theme.ResourcesProvider resourcesProvider = this.val$resourcesProvider;
            builder.setItems(charSequenceArr, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$4$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    PeerStoriesView.4.this.lambda$onLinkLongPress$0(uRLSpan, view, url, resourcesProvider, dialogInterface, i);
                }
            });
            builder.setOnPreDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$4$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    runnable.run();
                }
            });
            BottomSheet create = builder.create();
            create.fixNavigationBar(Theme.getColor(Theme.key_dialogBackground, this.val$resourcesProvider));
            PeerStoriesView.this.delegate.showDialog(create);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLinkLongPress$0(URLSpan uRLSpan, View view, String str, Theme.ResourcesProvider resourcesProvider, DialogInterface dialogInterface, int i) {
            if (i == 0) {
                onLinkClick(uRLSpan, view);
            } else if (i == 1) {
                AndroidUtilities.addToClipboard(str);
                BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createCopyLinkBulletin().show();
            }
        }

        @Override // org.telegram.ui.Stories.StoryCaptionView
        public void onEmojiClick(AnimatedEmojiSpan animatedEmojiSpan) {
            if (animatedEmojiSpan != null) {
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                if (peerStoriesView.delegate == null) {
                    return;
                }
                TLRPC$Document tLRPC$Document = animatedEmojiSpan.document;
                if (tLRPC$Document == null) {
                    tLRPC$Document = AnimatedEmojiDrawable.findDocument(peerStoriesView.currentAccount, animatedEmojiSpan.documentId);
                }
                if (tLRPC$Document == null) {
                    return;
                }
                BulletinFactory of = BulletinFactory.of(PeerStoriesView.this.storyContainer, this.val$resourcesProvider);
                final StoryViewer storyViewer = this.val$storyViewer;
                final Theme.ResourcesProvider resourcesProvider = this.val$resourcesProvider;
                Bulletin createContainsEmojiBulletin = of.createContainsEmojiBulletin(tLRPC$Document, 2, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$4$$ExternalSyntheticLambda2
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        PeerStoriesView.4.this.lambda$onEmojiClick$2(storyViewer, resourcesProvider, (TLRPC$InputStickerSet) obj);
                    }
                });
                if (createContainsEmojiBulletin == null) {
                    return;
                }
                createContainsEmojiBulletin.tag = 1;
                createContainsEmojiBulletin.show(true);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEmojiClick$2(StoryViewer storyViewer, Theme.ResourcesProvider resourcesProvider, TLRPC$InputStickerSet tLRPC$InputStickerSet) {
            ArrayList arrayList = new ArrayList(1);
            arrayList.add(tLRPC$InputStickerSet);
            EmojiPacksAlert emojiPacksAlert = new EmojiPacksAlert(storyViewer.fragment, getContext(), resourcesProvider, arrayList);
            Delegate delegate = PeerStoriesView.this.delegate;
            if (delegate != null) {
                delegate.showDialog(emojiPacksAlert);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        shareStory(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(StoryViewer storyViewer, View view) {
        if (UserConfig.getInstance(this.currentAccount).clientUserId == this.dialogId) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            bundle.putLong("dialog_id", this.dialogId);
            storyViewer.presentFragment(new MediaActivity(bundle, null));
            return;
        }
        Bundle bundle2 = new Bundle();
        bundle2.putLong("user_id", this.dialogId);
        ProfileActivity profileActivity = new ProfileActivity(bundle2);
        BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
        bottomSheetParams.transitionFromLeft = true;
        bottomSheetParams.allowNestedScroll = false;
        storyViewer.presentFragment(profileActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(Theme.ResourcesProvider resourcesProvider, Context context, StoryViewer storyViewer, SharedResources sharedResources, View view) {
        this.delegate.setPopupIsVisible(true);
        this.editStoryItem = null;
        6 r12 = new 6(getContext(), resourcesProvider, this.isSelf, resourcesProvider, context, storyViewer, sharedResources, new boolean[]{false});
        this.popupMenu = r12;
        r12.show(this.optionsIconView, 0, (-ActionBar.getCurrentActionBarHeight()) + AndroidUtilities.dp(6.0f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 6 extends CustomPopupMenu {
        private boolean edit;
        final /* synthetic */ Context val$context;
        final /* synthetic */ boolean[] val$popupStillVisible;
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;
        final /* synthetic */ SharedResources val$sharedResources;
        final /* synthetic */ StoryViewer val$storyViewer;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        6(Context context, Theme.ResourcesProvider resourcesProvider, boolean z, Theme.ResourcesProvider resourcesProvider2, Context context2, StoryViewer storyViewer, SharedResources sharedResources, boolean[] zArr) {
            super(context, resourcesProvider, z);
            this.val$resourcesProvider = resourcesProvider2;
            this.val$context = context2;
            this.val$storyViewer = storyViewer;
            this.val$sharedResources = sharedResources;
            this.val$popupStillVisible = zArr;
        }

        /* JADX WARN: Code restructure failed: missing block: B:63:0x029f, code lost:
            if (org.telegram.messenger.MessagesController.getInstance(r0.currentAccount).storiesExportNopublicLink != false) goto L100;
         */
        /* JADX WARN: Removed duplicated region for block: B:67:0x02c1  */
        @Override // org.telegram.ui.Components.CustomPopupMenu
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onCreate(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout) {
            PeerStoriesView peerStoriesView;
            TLRPC$StoryItem tLRPC$StoryItem;
            TLRPC$MessageMedia tLRPC$MessageMedia;
            TLRPC$Photo tLRPC$Photo;
            int i;
            String str;
            PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
            boolean z = true;
            if (!peerStoriesView2.isSelf) {
                final String sharedPrefKey = NotificationsController.getSharedPrefKey(peerStoriesView2.dialogId, 0);
                boolean z2 = !NotificationsCustomSettingsActivity.areStoriesNotMuted(PeerStoriesView.this.currentAccount, PeerStoriesView.this.dialogId);
                final TLRPC$User user = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId));
                if (!UserObject.isService(PeerStoriesView.this.dialogId)) {
                    if (!z2) {
                        ActionBarMenuSubItem addItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_mute, LocaleController.getString("NotificationsStoryMute2", R.string.NotificationsStoryMute2), false, this.val$resourcesProvider);
                        final Theme.ResourcesProvider resourcesProvider = this.val$resourcesProvider;
                        addItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda10
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PeerStoriesView.6.this.lambda$onCreate$12(sharedPrefKey, user, resourcesProvider, view);
                            }
                        });
                        addItem.setMultiline(false);
                    } else {
                        ActionBarMenuSubItem addItem2 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_unmute, LocaleController.getString("NotificationsStoryUnmute2", R.string.NotificationsStoryUnmute2), false, this.val$resourcesProvider);
                        final Theme.ResourcesProvider resourcesProvider2 = this.val$resourcesProvider;
                        addItem2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda9
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PeerStoriesView.6.this.lambda$onCreate$13(sharedPrefKey, user, resourcesProvider2, view);
                            }
                        });
                        addItem2.setMultiline(false);
                    }
                    if (user.contact) {
                        if (!user.stories_hidden) {
                            ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_archive, LocaleController.getString("ArchivePeerStories", R.string.ArchivePeerStories), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda5
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    PeerStoriesView.6.this.lambda$onCreate$14(view);
                                }
                            });
                        } else {
                            ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_unarchive, LocaleController.getString("UnarchiveStories", R.string.UnarchiveStories), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda1
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    PeerStoriesView.6.this.lambda$onCreate$15(view);
                                }
                            });
                        }
                    }
                }
                PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                if (!peerStoriesView3.unsupported && (UserObject.isService(peerStoriesView3.dialogId) || PeerStoriesView.this.allowShare)) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_gallery, LocaleController.getString("SaveToGallery", R.string.SaveToGallery), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.6.this.lambda$onCreate$16(view);
                        }
                    });
                }
                if (!PeerStoriesView.this.allowShare) {
                    PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                    TLRPC$StoryItem tLRPC$StoryItem2 = peerStoriesView4.currentStory.storyItem;
                    if (tLRPC$StoryItem2 != null) {
                        if (tLRPC$StoryItem2.isPublic) {
                        }
                    }
                    if (PeerStoriesView.this.allowShare) {
                        ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_shareout, LocaleController.getString("BotShare", R.string.BotShare), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda0
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PeerStoriesView.6.this.lambda$onCreate$18(view);
                            }
                        });
                    }
                    peerStoriesView = PeerStoriesView.this;
                    if (!peerStoriesView.unsupported && !UserObject.isService(peerStoriesView.dialogId)) {
                        ActionBarMenuSubItem addItem3 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_report, LocaleController.getString("ReportChat", R.string.ReportChat), false, this.val$resourcesProvider);
                        final StoryViewer storyViewer = this.val$storyViewer;
                        final Theme.ResourcesProvider resourcesProvider3 = this.val$resourcesProvider;
                        addItem3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda14
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PeerStoriesView.6.this.lambda$onCreate$19(storyViewer, resourcesProvider3, view);
                            }
                        });
                    }
                }
                ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_link, LocaleController.getString("CopyLink", R.string.CopyLink), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda6
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PeerStoriesView.6.this.lambda$onCreate$17(view);
                    }
                });
                if (PeerStoriesView.this.allowShare) {
                }
                peerStoriesView = PeerStoriesView.this;
                if (!peerStoriesView.unsupported) {
                    ActionBarMenuSubItem addItem32 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_report, LocaleController.getString("ReportChat", R.string.ReportChat), false, this.val$resourcesProvider);
                    final StoryViewer storyViewer2 = this.val$storyViewer;
                    final Theme.ResourcesProvider resourcesProvider32 = this.val$resourcesProvider;
                    addItem32.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda14
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.6.this.lambda$onCreate$19(storyViewer2, resourcesProvider32, view);
                        }
                    });
                }
            } else {
                StoryItemHolder storyItemHolder = peerStoriesView2.currentStory;
                final TLRPC$StoryItem tLRPC$StoryItem3 = storyItemHolder.storyItem;
                if (storyItemHolder.uploadingStory != null) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_cancel, LocaleController.getString("Cancel", R.string.Cancel), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda8
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.6.this.lambda$onCreate$0(view);
                        }
                    });
                }
                if (tLRPC$StoryItem3 == null) {
                    return;
                }
                if (PeerStoriesView.this.currentStory.isVideo()) {
                    i = R.string.SaveVideo;
                    str = "SaveVideo";
                } else {
                    i = R.string.SaveImage;
                    str = "SaveImage";
                }
                String string = LocaleController.getString(str, i);
                final StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy = new StoryPrivacyBottomSheet.StoryPrivacy(PeerStoriesView.this.currentAccount, tLRPC$StoryItem3.privacy);
                ActionBarMenuSubItem addItem4 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_view_file, LocaleController.getString("WhoCanSee", R.string.WhoCanSee), false, this.val$resourcesProvider);
                addItem4.setSubtext(storyPrivacy.toString());
                addItem4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda15
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PeerStoriesView.6.this.lambda$onCreate$1(storyPrivacy, tLRPC$StoryItem3, view);
                    }
                });
                addItem4.setItemHeight(56);
                ActionBarPopupWindow.GapView gapView = new ActionBarPopupWindow.GapView(PeerStoriesView.this.getContext(), this.val$resourcesProvider, Theme.key_actionBarDefaultSubmenuSeparator);
                gapView.setTag(R.id.fit_width_tag, 1);
                actionBarPopupWindowLayout.addView((View) gapView, LayoutHelper.createLinear(-1, 8));
                PeerStoriesView peerStoriesView5 = PeerStoriesView.this;
                if (!peerStoriesView5.unsupported && MessagesController.getInstance(peerStoriesView5.currentAccount).storiesEnabled()) {
                    PeerStoriesView.this.editStoryItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_edit, LocaleController.getString("EditStory", R.string.EditStory), false, this.val$resourcesProvider);
                    ActionBarMenuSubItem actionBarMenuSubItem = PeerStoriesView.this.editStoryItem;
                    final Theme.ResourcesProvider resourcesProvider4 = this.val$resourcesProvider;
                    final Context context = this.val$context;
                    final StoryViewer storyViewer3 = this.val$storyViewer;
                    final SharedResources sharedResources = this.val$sharedResources;
                    actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda12
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.6.this.lambda$onCreate$6(resourcesProvider4, context, storyViewer3, sharedResources, view);
                        }
                    });
                    if (PeerStoriesView.this.storiesController.hasUploadingStories() && PeerStoriesView.this.currentStory.isVideo && !SharedConfig.allowPreparingHevcPlayers()) {
                        PeerStoriesView.this.editStoryItem.setAlpha(0.5f);
                    }
                }
                final boolean z3 = !tLRPC$StoryItem3.pinned;
                ActionBarMenuSubItem addItem5 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, z3 ? R.drawable.msg_save_story : R.drawable.msg_archive, z3 ? LocaleController.getString("SaveToProfile", R.string.SaveToProfile) : LocaleController.getString("ArchiveStory"), false, this.val$resourcesProvider);
                final Theme.ResourcesProvider resourcesProvider5 = this.val$resourcesProvider;
                addItem5.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda11
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PeerStoriesView.6.this.lambda$onCreate$8(tLRPC$StoryItem3, z3, resourcesProvider5, view);
                    }
                });
                if (!PeerStoriesView.this.unsupported) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_gallery, string, false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda7
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.6.this.lambda$onCreate$9(view);
                        }
                    });
                }
                if (PeerStoriesView.this.allowShare) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_shareout, LocaleController.getString("BotShare", R.string.BotShare), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.6.this.lambda$onCreate$10(view);
                        }
                    });
                }
                ActionBarMenuSubItem addItem6 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_delete, LocaleController.getString("Delete", R.string.Delete), false, this.val$resourcesProvider);
                int i2 = Theme.key_text_RedBold;
                addItem6.setSelectorColor(Theme.multAlpha(Theme.getColor(i2, this.val$resourcesProvider), 0.12f));
                addItem6.setColors(this.val$resourcesProvider.getColor(i2), this.val$resourcesProvider.getColor(i2));
                addItem6.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda3
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PeerStoriesView.6.this.lambda$onCreate$11(view);
                    }
                });
            }
            StoryItemHolder storyItemHolder2 = PeerStoriesView.this.currentStory;
            boolean z4 = (storyItemHolder2 == null || (tLRPC$StoryItem = storyItemHolder2.storyItem) == null || (tLRPC$MessageMedia = tLRPC$StoryItem.media) == null || (!MessageObject.isDocumentHasAttachedStickers(tLRPC$MessageMedia.document) && ((tLRPC$Photo = PeerStoriesView.this.currentStory.storyItem.media.photo) == null || !tLRPC$Photo.has_stickers))) ? false : true;
            PeerStoriesView peerStoriesView6 = PeerStoriesView.this;
            ArrayList animatedEmojiSets = peerStoriesView6.getAnimatedEmojiSets(peerStoriesView6.currentStory);
            z = (animatedEmojiSets == null || animatedEmojiSets.isEmpty()) ? false : false;
            if (z4 || z) {
                ActionBarPopupWindow.GapView gapView2 = new ActionBarPopupWindow.GapView(this.val$context, this.val$resourcesProvider, Theme.key_actionBarDefaultSubmenuSeparator);
                int i3 = R.id.fit_width_tag;
                gapView2.setTag(i3, 1);
                actionBarPopupWindowLayout.addView((View) gapView2, LayoutHelper.createLinear(-1, 8));
                TLRPC$MessageMedia tLRPC$MessageMedia2 = PeerStoriesView.this.currentStory.storyItem.media;
                TLObject tLObject = tLRPC$MessageMedia2.document;
                final StoryContainsEmojiButton storyContainsEmojiButton = new StoryContainsEmojiButton(this.val$context, PeerStoriesView.this.currentAccount, tLObject != null ? tLObject : tLRPC$MessageMedia2.photo, PeerStoriesView.this.currentStory.storyItem, z4, animatedEmojiSets, this.val$resourcesProvider);
                storyContainsEmojiButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda13
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PeerStoriesView.6.this.lambda$onCreate$20(storyContainsEmojiButton, view);
                    }
                });
                storyContainsEmojiButton.setTag(i3, 1);
                actionBarPopupWindowLayout.addView((View) storyContainsEmojiButton, LayoutHelper.createLinear(-1, -2));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$0(View view) {
            StoriesController.UploadingStory uploadingStory = PeerStoriesView.this.currentStory.uploadingStory;
            if (uploadingStory != null) {
                uploadingStory.cancel();
                PeerStoriesView.this.updateStoryItems();
            }
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$1(StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy, TLRPC$StoryItem tLRPC$StoryItem, View view) {
            PeerStoriesView.this.editPrivacy(storyPrivacy, tLRPC$StoryItem);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$6(Theme.ResourcesProvider resourcesProvider, Context context, final StoryViewer storyViewer, final SharedResources sharedResources, View view) {
            if (view.getAlpha() < 1.0f) {
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                AndroidUtilities.shakeViewSpring(view, peerStoriesView.shiftDp = -peerStoriesView.shiftDp);
                BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createErrorBulletin("Wait until current upload is complete").show();
                return;
            }
            final Activity findActivity = AndroidUtilities.findActivity(context);
            if (findActivity == null) {
                return;
            }
            this.edit = true;
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
            PeerStoriesView.this.setActive(false);
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.6.this.lambda$onCreate$5(findActivity, storyViewer, sharedResources);
                }
            };
            if (PeerStoriesView.this.delegate.releasePlayer(runnable)) {
                return;
            }
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$5(Activity activity, StoryViewer storyViewer, final SharedResources sharedResources) {
            StoryViewer.VideoPlayerHolder videoPlayerHolder;
            StoryRecorder storyRecorder = StoryRecorder.getInstance(activity, PeerStoriesView.this.currentAccount);
            VideoPlayerSharedScope videoPlayerSharedScope = PeerStoriesView.this.playerSharedScope;
            storyRecorder.openEdit(StoryRecorder.SourceView.fromStoryViewer(storyViewer), StoryEntry.fromStoryItem(PeerStoriesView.this.currentStory.getPath(), PeerStoriesView.this.currentStory.storyItem), (videoPlayerSharedScope == null || (videoPlayerHolder = videoPlayerSharedScope.player) == null) ? 0L : videoPlayerHolder.currentPosition, true);
            storyRecorder.setOnPrepareCloseListener(new Utilities.Callback3() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda20
                @Override // org.telegram.messenger.Utilities.Callback3
                public final void run(Object obj, Object obj2, Object obj3) {
                    PeerStoriesView.6.this.lambda$onCreate$4(sharedResources, (Long) obj, (Runnable) obj2, (Boolean) obj3);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$4(SharedResources sharedResources, Long l, final Runnable runnable, Boolean bool) {
            final long currentTimeMillis = System.currentTimeMillis();
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            VideoPlayerSharedScope videoPlayerSharedScope = peerStoriesView.playerSharedScope;
            StoryViewer.VideoPlayerHolder videoPlayerHolder = videoPlayerSharedScope.player;
            if (videoPlayerHolder == null) {
                peerStoriesView.delegate.setPopupIsVisible(false);
                PeerStoriesView.this.setActive(true);
                PeerStoriesView.this.onImageReceiverThumbLoaded = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda16
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.6.lambda$onCreate$2(runnable);
                    }
                };
                if (bool.booleanValue()) {
                    PeerStoriesView.this.updatePosition();
                }
                AndroidUtilities.runOnUIThread(runnable, 400L);
                return;
            }
            videoPlayerHolder.firstFrameRendered = false;
            videoPlayerSharedScope.firstFrameRendered = false;
            videoPlayerHolder.setOnReadyListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.6.lambda$onCreate$3(runnable, currentTimeMillis);
                }
            });
            PeerStoriesView.this.delegate.setPopupIsVisible(false);
            if (PeerStoriesView.this.muteIconView != null) {
                PeerStoriesView.this.muteIconView.setAnimation(sharedResources.muteDrawable);
            }
            if (PeerStoriesView.this.videoDuration > 0 && ((float) l.longValue()) > ((float) PeerStoriesView.this.videoDuration) * 0.4f) {
                l = 0L;
            }
            PeerStoriesView.this.setActive(l.longValue(), true);
            AndroidUtilities.runOnUIThread(runnable, 400L);
            if (bool.booleanValue()) {
                PeerStoriesView.this.updatePosition();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onCreate$2(Runnable runnable) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            AndroidUtilities.runOnUIThread(runnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onCreate$3(Runnable runnable, long j) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            AndroidUtilities.runOnUIThread(runnable, Math.max(0L, 32 - (System.currentTimeMillis() - j)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$8(final TLRPC$StoryItem tLRPC$StoryItem, final boolean z, final Theme.ResourcesProvider resourcesProvider, View view) {
            ArrayList<TLRPC$StoryItem> arrayList = new ArrayList<>();
            arrayList.add(tLRPC$StoryItem);
            MessagesController.getInstance(PeerStoriesView.this.currentAccount).getStoriesController().updateStoriesPinned(arrayList, z, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda21
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    PeerStoriesView.6.this.lambda$onCreate$7(tLRPC$StoryItem, z, resourcesProvider, (Boolean) obj);
                }
            });
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$7(TLRPC$StoryItem tLRPC$StoryItem, boolean z, Theme.ResourcesProvider resourcesProvider, Boolean bool) {
            int i;
            String str;
            if (bool.booleanValue()) {
                tLRPC$StoryItem.pinned = z;
                BulletinFactory of = BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider);
                int i2 = z ? R.raw.contact_check : R.raw.chats_archived;
                if (z) {
                    i = R.string.StoryPinnedToProfile;
                    str = "StoryPinnedToProfile";
                } else {
                    i = R.string.StoryArchivedFromProfile;
                    str = "StoryArchivedFromProfile";
                }
                of.createSimpleBulletin(i2, LocaleController.getString(str, i)).show();
                return;
            }
            BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createSimpleBulletin(R.raw.error, LocaleController.getString("UnknownError", R.string.UnknownError)).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$9(View view) {
            PeerStoriesView.this.saveToGallery();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$10(View view) {
            PeerStoriesView.this.shareStory(false);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$11(View view) {
            PeerStoriesView.this.deleteStory();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$12(String str, TLRPC$User tLRPC$User, Theme.ResourcesProvider resourcesProvider, View view) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(PeerStoriesView.this.currentAccount).edit();
            edit.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + str, false).apply();
            NotificationsController.getInstance(PeerStoriesView.this.currentAccount).updateServerNotificationsSettings(PeerStoriesView.this.dialogId, 0);
            String trim = tLRPC$User == null ? "" : tLRPC$User.first_name.trim();
            int indexOf = trim.indexOf(" ");
            if (indexOf > 0) {
                trim = trim.substring(0, indexOf);
            }
            BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createUsersBulletin(Arrays.asList(tLRPC$User), AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsStoryMutedHint", R.string.NotificationsStoryMutedHint, trim))).setTag(2).show();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$13(String str, TLRPC$User tLRPC$User, Theme.ResourcesProvider resourcesProvider, View view) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(PeerStoriesView.this.currentAccount).edit();
            edit.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + str, true).apply();
            NotificationsController.getInstance(PeerStoriesView.this.currentAccount).updateServerNotificationsSettings(PeerStoriesView.this.dialogId, 0);
            String trim = tLRPC$User == null ? "" : tLRPC$User.first_name.trim();
            int indexOf = trim.indexOf(" ");
            if (indexOf > 0) {
                trim = trim.substring(0, indexOf);
            }
            BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createUsersBulletin(Arrays.asList(tLRPC$User), AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsStoryUnmutedHint", R.string.NotificationsStoryUnmutedHint, trim))).setTag(2).show();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$14(View view) {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.toggleArchiveForStory(peerStoriesView.dialogId);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$15(View view) {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.toggleArchiveForStory(peerStoriesView.dialogId);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$16(View view) {
            PeerStoriesView.this.saveToGallery();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$17(View view) {
            AndroidUtilities.addToClipboard(PeerStoriesView.this.currentStory.createLink());
            PeerStoriesView.this.onLickCopied();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$18(View view) {
            PeerStoriesView.this.shareStory(false);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$19(StoryViewer storyViewer, Theme.ResourcesProvider resourcesProvider, View view) {
            AlertsCreator.createReportAlert(PeerStoriesView.this.getContext(), PeerStoriesView.this.dialogId, 0, PeerStoriesView.this.currentStory.storyItem.id, storyViewer.fragment, resourcesProvider, null);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$20(StoryContainsEmojiButton storyContainsEmojiButton, View view) {
            Delegate delegate;
            EmojiPacksAlert alert = storyContainsEmojiButton.getAlert();
            if (alert == null || (delegate = PeerStoriesView.this.delegate) == null) {
                return;
            }
            delegate.showDialog(alert);
            PeerStoriesView.this.popupMenu.dismiss();
        }

        @Override // org.telegram.ui.Components.CustomPopupMenu
        protected void onDismissed() {
            if (!this.edit && !this.val$popupStillVisible[0]) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$6$$ExternalSyntheticLambda18
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.6.this.lambda$onDismissed$21();
                    }
                });
            }
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.popupMenu = null;
            peerStoriesView.editStoryItem = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDismissed$21() {
            PeerStoriesView.this.delegate.setPopupIsVisible(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00ea  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0127  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$new$4(View view) {
        SpannableStringBuilder replaceTags;
        boolean z;
        TLRPC$StoryItem tLRPC$StoryItem = this.currentStory.storyItem;
        if (tLRPC$StoryItem == null) {
            return;
        }
        if (this.isSelf) {
            editPrivacy(new StoryPrivacyBottomSheet.StoryPrivacy(this.currentAccount, tLRPC$StoryItem.privacy), tLRPC$StoryItem);
            return;
        }
        if (this.privacyHint == null) {
            HintView2 onHiddenListener = new HintView2(getContext(), 1).setMultilineText(true).setTextAlign(Layout.Alignment.ALIGN_CENTER).setOnHiddenListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.this.lambda$new$3();
                }
            });
            this.privacyHint = onHiddenListener;
            onHiddenListener.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
            this.storyContainer.addView(this.privacyHint, LayoutHelper.createFrame(-1, 60.0f, 55, 0.0f, 52.0f, 0.0f, 0.0f));
        }
        TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId));
        if (user == null) {
            return;
        }
        String str = user.first_name;
        int indexOf = str.indexOf(32);
        if (indexOf > 0) {
            str = str.substring(0, indexOf);
        }
        if (tLRPC$StoryItem.close_friends) {
            this.privacyHint.setInnerPadding(15, 8, 15, 8);
            replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("StoryCloseFriendsHint", R.string.StoryCloseFriendsHint, str));
        } else if (tLRPC$StoryItem.contacts) {
            this.privacyHint.setInnerPadding(11, 6, 11, 7);
            replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("StoryContactsHint", R.string.StoryContactsHint, str));
            z = false;
            CharSequence replaceEmoji = Emoji.replaceEmoji(replaceTags, this.privacyHint.getTextPaint().getFontMetricsInt(), false);
            HintView2 hintView2 = this.privacyHint;
            hintView2.setMaxWidthPx(!z ? HintView2.cutInFancyHalf(replaceEmoji, hintView2.getTextPaint()) : this.storyContainer.getMeasuredWidth());
            this.privacyHint.setText(replaceEmoji);
            this.privacyHint.setJoint(1.0f, (-(this.storyContainer.getWidth() - this.privacyButton.getCenterX())) / AndroidUtilities.density);
            this.delegate.setIsHintVisible(true);
            if (this.privacyHint.shown()) {
                BotWebViewVibrationEffect.IMPACT_LIGHT.vibrate();
            }
            this.privacyHint.show();
        } else if (!tLRPC$StoryItem.selected_contacts) {
            return;
        } else {
            this.privacyHint.setInnerPadding(15, 8, 15, 8);
            replaceTags = AndroidUtilities.replaceTags(LocaleController.formatString("StorySelectedContactsHint", R.string.StorySelectedContactsHint, str));
        }
        z = true;
        CharSequence replaceEmoji2 = Emoji.replaceEmoji(replaceTags, this.privacyHint.getTextPaint().getFontMetricsInt(), false);
        HintView2 hintView22 = this.privacyHint;
        hintView22.setMaxWidthPx(!z ? HintView2.cutInFancyHalf(replaceEmoji2, hintView22.getTextPaint()) : this.storyContainer.getMeasuredWidth());
        this.privacyHint.setText(replaceEmoji2);
        this.privacyHint.setJoint(1.0f, (-(this.storyContainer.getWidth() - this.privacyButton.getCenterX())) / AndroidUtilities.density);
        this.delegate.setIsHintVisible(true);
        if (this.privacyHint.shown()) {
        }
        this.privacyHint.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3() {
        this.delegate.setIsHintVisible(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(StoryViewer storyViewer, Context context, View view) {
        if (this.currentStory.hasSound()) {
            storyViewer.toggleSilentMode();
            return;
        }
        if (this.soundTooltip == null) {
            HintView2 joint = new HintView2(context, 1).setJoint(1.0f, -56.0f);
            this.soundTooltip = joint;
            joint.setText(LocaleController.getString(R.string.StoryNoSound));
            this.soundTooltip.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
            this.storyContainer.addView(this.soundTooltip, LayoutHelper.createFrame(-1, -2.0f, 55, 0.0f, 52.0f, 0.0f, 0.0f));
        }
        this.soundTooltip.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<TLRPC$InputStickerSet> getAnimatedEmojiSets(StoryItemHolder storyItemHolder) {
        StoryEntry storyEntry;
        AnimatedEmojiSpan[] animatedEmojiSpanArr;
        ArrayList<TLRPC$MessageEntity> arrayList;
        if (storyItemHolder != null) {
            TLRPC$StoryItem tLRPC$StoryItem = storyItemHolder.storyItem;
            int i = 0;
            if (tLRPC$StoryItem != null && (arrayList = tLRPC$StoryItem.entities) != null && !arrayList.isEmpty()) {
                HashSet hashSet = new HashSet();
                ArrayList<TLRPC$InputStickerSet> arrayList2 = new ArrayList<>();
                while (i < storyItemHolder.storyItem.entities.size()) {
                    TLRPC$MessageEntity tLRPC$MessageEntity = storyItemHolder.storyItem.entities.get(i);
                    if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCustomEmoji) {
                        TLRPC$TL_messageEntityCustomEmoji tLRPC$TL_messageEntityCustomEmoji = (TLRPC$TL_messageEntityCustomEmoji) tLRPC$MessageEntity;
                        TLRPC$Document tLRPC$Document = tLRPC$TL_messageEntityCustomEmoji.document;
                        if (tLRPC$Document == null) {
                            tLRPC$Document = AnimatedEmojiDrawable.findDocument(this.currentAccount, tLRPC$TL_messageEntityCustomEmoji.document_id);
                        }
                        if (tLRPC$Document != null) {
                            TLRPC$InputStickerSet inputStickerSet = MessageObject.getInputStickerSet(tLRPC$Document);
                            if (!hashSet.contains(Long.valueOf(inputStickerSet.id))) {
                                hashSet.add(Long.valueOf(inputStickerSet.id));
                                arrayList2.add(inputStickerSet);
                            }
                        }
                    }
                    i++;
                }
                return arrayList2;
            }
            StoriesController.UploadingStory uploadingStory = storyItemHolder.uploadingStory;
            if (uploadingStory != null && (storyEntry = uploadingStory.entry) != null) {
                CharSequence charSequence = storyEntry.caption;
                if ((charSequence instanceof Spanned) && (animatedEmojiSpanArr = (AnimatedEmojiSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), AnimatedEmojiSpan.class)) != null) {
                    HashSet hashSet2 = new HashSet();
                    ArrayList<TLRPC$InputStickerSet> arrayList3 = new ArrayList<>();
                    while (i < animatedEmojiSpanArr.length) {
                        TLRPC$Document tLRPC$Document2 = animatedEmojiSpanArr[i].document;
                        if (tLRPC$Document2 == null) {
                            tLRPC$Document2 = AnimatedEmojiDrawable.findDocument(this.currentAccount, animatedEmojiSpanArr[i].documentId);
                        }
                        if (tLRPC$Document2 != null) {
                            TLRPC$InputStickerSet inputStickerSet2 = MessageObject.getInputStickerSet(tLRPC$Document2);
                            if (!hashSet2.contains(Long.valueOf(inputStickerSet2.id))) {
                                hashSet2.add(Long.valueOf(inputStickerSet2.id));
                                arrayList3.add(inputStickerSet2);
                            }
                        }
                        i++;
                    }
                    return arrayList3;
                }
                return null;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleArchiveForStory(final long j) {
        final TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j));
        final boolean z = !user.stories_hidden;
        final MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.this.lambda$toggleArchiveForStory$8(messagesController, j, z, user);
            }
        }, 200L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleArchiveForStory$8(final MessagesController messagesController, final long j, final boolean z, TLRPC$User tLRPC$User) {
        messagesController.getStoriesController().toggleHidden(j, z, false, true);
        BulletinFactory.UndoObject undoObject = new BulletinFactory.UndoObject();
        undoObject.onUndo = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.lambda$toggleArchiveForStory$6(MessagesController.this, j, z);
            }
        };
        undoObject.onAction = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.lambda$toggleArchiveForStory$7(MessagesController.this, j, z);
            }
        };
        BulletinFactory.of(this.storyContainer, this.resourcesProvider).createUsersBulletin(Arrays.asList(tLRPC$User), !z ? AndroidUtilities.replaceTags(LocaleController.formatString("StoriesMovedToDialogs", R.string.StoriesMovedToDialogs, ContactsController.formatName(tLRPC$User.first_name, null, 10))) : AndroidUtilities.replaceTags(LocaleController.formatString("StoriesMovedToContacts", R.string.StoriesMovedToContacts, ContactsController.formatName(tLRPC$User.first_name, null, 10))), null, undoObject).setTag(2).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toggleArchiveForStory$6(MessagesController messagesController, long j, boolean z) {
        messagesController.getStoriesController().toggleHidden(j, !z, false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toggleArchiveForStory$7(MessagesController messagesController, long j, boolean z) {
        messagesController.getStoriesController().toggleHidden(j, z, true, true);
    }

    private void createEnterView() {
        8 r7 = new 8(AndroidUtilities.findActivity(getContext()), this, null, true, new WrappedResourceProvider(this, this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.7
            @Override // org.telegram.ui.WrappedResourceProvider
            public void appendColors() {
                this.sparseIntArray.put(Theme.key_chat_emojiPanelBackground, ColorUtils.setAlphaComponent(-1, 30));
            }
        });
        this.chatActivityEnterView = r7;
        r7.setOverrideKeyboardAnimation(true);
        this.chatActivityEnterView.setOverrideHint(LocaleController.getString("ReplyPrivately", R.string.ReplyPrivately));
        this.chatActivityEnterView.setClipChildren(false);
        this.chatActivityEnterView.setDelegate(new 9());
        setDelegate(this.chatActivityEnterView);
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        chatActivityEnterView.shouldDrawBackground = false;
        chatActivityEnterView.shouldDrawRecordedAudioPanelInParent = true;
        chatActivityEnterView.setAllowStickersAndGifs(true, true, true);
        this.chatActivityEnterView.updateColors();
        addView(this.chatActivityEnterView, LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
        this.chatActivityEnterView.recordingGuid = this.classGuid;
        this.playerSharedScope.viewsToInvalidate.add(this.storyContainer);
        this.playerSharedScope.viewsToInvalidate.add(this);
        if (this.attachedToWindow) {
            this.chatActivityEnterView.onResume();
        }
        this.reactionsContainerIndex = getChildCount();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 8 extends ChatActivityEnterView {
        private int chatActivityEnterViewAnimateFromTop;
        private Animator messageEditTextAnimator;
        int messageEditTextPredrawHeigth;
        int messageEditTextPredrawScrollY;

        8(Activity activity, SizeNotifierFrameLayout sizeNotifierFrameLayout, ChatActivity chatActivity, boolean z, Theme.ResourcesProvider resourcesProvider) {
            super(activity, sizeNotifierFrameLayout, chatActivity, z, resourcesProvider);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        public void checkAnimation() {
            int backgroundTop = getBackgroundTop();
            int i = this.chatActivityEnterViewAnimateFromTop;
            if (i != 0 && backgroundTop != i) {
                int i2 = (this.animatedTop + i) - backgroundTop;
                this.animatedTop = i2;
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                peerStoriesView.forceUpdateOffsets = true;
                if (peerStoriesView.changeBoundAnimator != null) {
                    PeerStoriesView.this.changeBoundAnimator.removeAllListeners();
                    PeerStoriesView.this.changeBoundAnimator.cancel();
                }
                View view = this.topView;
                if (view != null && view.getVisibility() == 0) {
                    View view2 = this.topView;
                    view2.setTranslationY(this.animatedTop + ((1.0f - this.topViewEnterProgress) * view2.getLayoutParams().height));
                    View view3 = this.topLineView;
                    if (view3 != null) {
                        view3.setTranslationY(this.animatedTop);
                    }
                }
                PeerStoriesView.this.invalidate();
                PeerStoriesView.this.changeBoundAnimator = ValueAnimator.ofFloat(i2, 0.0f);
                PeerStoriesView.this.changeBoundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        PeerStoriesView.8.this.lambda$checkAnimation$0(valueAnimator);
                    }
                });
                PeerStoriesView.this.changeBoundAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.8.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        PeerStoriesView.this.invalidate();
                        ((ChatActivityEnterView) 8.this).animatedTop = 0;
                        8 r4 = 8.this;
                        PeerStoriesView.this.forceUpdateOffsets = true;
                        if (((ChatActivityEnterView) r4).topView != null && ((ChatActivityEnterView) 8.this).topView.getVisibility() == 0) {
                            ((ChatActivityEnterView) 8.this).topView.setTranslationY(((ChatActivityEnterView) 8.this).animatedTop + ((1.0f - ((ChatActivityEnterView) 8.this).topViewEnterProgress) * ((ChatActivityEnterView) 8.this).topView.getLayoutParams().height));
                            if (((ChatActivityEnterView) 8.this).topLineView != null) {
                                ((ChatActivityEnterView) 8.this).topLineView.setTranslationY(((ChatActivityEnterView) 8.this).animatedTop);
                            }
                        }
                        PeerStoriesView.this.changeBoundAnimator = null;
                    }
                });
                PeerStoriesView.this.changeBoundAnimator.setDuration(250L);
                PeerStoriesView.this.changeBoundAnimator.setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR);
                PeerStoriesView.this.changeBoundAnimator.start();
                this.chatActivityEnterViewAnimateFromTop = 0;
            }
            if (this.shouldAnimateEditTextWithBounds) {
                EditTextCaption editTextCaption = this.messageEditText;
                editTextCaption.setOffsetY(editTextCaption.getOffsetY() - ((this.messageEditTextPredrawHeigth - this.messageEditText.getMeasuredHeight()) + (this.messageEditTextPredrawScrollY - this.messageEditText.getScrollY())));
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.messageEditText.getOffsetY(), 0.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        PeerStoriesView.8.this.lambda$checkAnimation$1(valueAnimator);
                    }
                });
                Animator animator = this.messageEditTextAnimator;
                if (animator != null) {
                    animator.cancel();
                }
                this.messageEditTextAnimator = ofFloat;
                ofFloat.setDuration(250L);
                ofFloat.setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR);
                ofFloat.start();
                this.shouldAnimateEditTextWithBounds = false;
            }
            getMeasuredHeight();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$checkAnimation$0(ValueAnimator valueAnimator) {
            this.animatedTop = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.forceUpdateOffsets = true;
            peerStoriesView.invalidate();
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$checkAnimation$1(ValueAnimator valueAnimator) {
            this.messageEditText.setOffsetY(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        protected void onLineCountChanged(int i, int i2) {
            if (PeerStoriesView.this.chatActivityEnterView != null) {
                this.shouldAnimateEditTextWithBounds = true;
                this.messageEditTextPredrawHeigth = this.messageEditText.getMeasuredHeight();
                this.messageEditTextPredrawScrollY = this.messageEditText.getScrollY();
                invalidate();
                PeerStoriesView.this.invalidate();
                this.chatActivityEnterViewAnimateFromTop = PeerStoriesView.this.chatActivityEnterView.getBackgroundTop();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.ChatActivityEnterView
        public void updateRecordInterface(int i) {
            super.updateRecordInterface(i);
            checkRecording();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.ChatActivityEnterView
        public void isRecordingStateChanged() {
            super.isRecordingStateChanged();
            checkRecording();
        }

        private void checkRecording() {
            FrameLayout frameLayout;
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.isRecording = peerStoriesView.chatActivityEnterView.isRecordingAudioVideo() || ((frameLayout = this.recordedAudioPanel) != null && frameLayout.getVisibility() == 0);
            PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
            if (peerStoriesView2.isActive) {
                peerStoriesView2.delegate.setIsRecording(peerStoriesView2.isRecording);
            }
            invalidate();
            PeerStoriesView.this.storyContainer.invalidate();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        public void extendActionMode(Menu menu) {
            ChatActivity.fillActionModeMenu(menu, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 9 implements ChatActivityEnterView.ChatActivityEnterViewDelegate {
        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void bottomPanelTranslationYChanged(float f) {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$bottomPanelTranslationYChanged(this, f);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ int getContentViewHeight() {
            return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$getContentViewHeight(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ TLRPC$TL_channels_sendAsPeers getSendAsPeers() {
            return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$getSendAsPeers(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ boolean hasForwardingMessages() {
            return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$hasForwardingMessages(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ boolean hasScheduledMessages() {
            return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$hasScheduledMessages(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ int measureKeyboardHeight() {
            return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$measureKeyboardHeight(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void needSendTyping() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void needStartRecordAudio(int i) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onAttachButtonHidden() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onAttachButtonShow() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onAudioVideoInterfaceUpdated() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void onContextMenuClose() {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onContextMenuClose(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void onContextMenuOpen() {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onContextMenuOpen(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void onEditTextScroll() {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onEditTextScroll(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onMessageEditEnd(boolean z) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onPreAudioVideoRecord() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onSendLongClick() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onStickersTab(boolean z) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onSwitchRecordMode(boolean z) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onTextSelectionChanged(int i, int i2) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onTextSpansChanged(CharSequence charSequence) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void onTrendingStickersShowed(boolean z) {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onTrendingStickersShowed(this, z);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onUpdateSlowModeButton(View view, boolean z, CharSequence charSequence) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onWindowSizeChanged(int i) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void openScheduledMessages() {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$openScheduledMessages(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void prepareMessageSending() {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$prepareMessageSending(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void scrollToSendingMessage() {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$scrollToSendingMessage(this);
        }

        9() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onMessageSend(CharSequence charSequence, boolean z, int i) {
            if (!PeerStoriesView.this.isRecording) {
                PeerStoriesView.this.afterMessageSend();
            } else {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$9$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.9.this.lambda$onMessageSend$0();
                    }
                }, 200L);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMessageSend$0() {
            PeerStoriesView.this.afterMessageSend();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onTextChanged(CharSequence charSequence, boolean z) {
            if (PeerStoriesView.this.mentionContainer == null) {
                PeerStoriesView.this.createMentionsContainer();
            }
            if (PeerStoriesView.this.mentionContainer.getAdapter() != null) {
                PeerStoriesView.this.mentionContainer.setDialogId(PeerStoriesView.this.dialogId);
                PeerStoriesView.this.mentionContainer.getAdapter().setUserOrChar(MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId)), null);
                PeerStoriesView.this.mentionContainer.getAdapter().searchUsernameOrHashtag(charSequence, PeerStoriesView.this.chatActivityEnterView.getCursorPosition(), null, false, false);
            }
            PeerStoriesView.this.invalidate();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void didPressAttachButton() {
            PeerStoriesView.this.openAttachMenu();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void needStartRecordVideo(int i, boolean z, int i2) {
            PeerStoriesView.this.checkInstantCameraView();
            if (PeerStoriesView.this.instantCameraView != null) {
                if (i == 0) {
                    PeerStoriesView.this.instantCameraView.showCamera();
                    return;
                }
                if (i == 1 || i == 3 || i == 4) {
                    PeerStoriesView.this.instantCameraView.send(i, z, i2);
                } else if (i == 2 || i == 5) {
                    PeerStoriesView.this.instantCameraView.cancel(i == 2);
                }
            }
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void needChangeVideoPreviewState(int i, float f) {
            if (PeerStoriesView.this.instantCameraView != null) {
                PeerStoriesView.this.instantCameraView.changeVideoPreviewState(i, f);
            }
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void needShowMediaBanHint() {
            if (PeerStoriesView.this.mediaBanTooltip == null) {
                PeerStoriesView.this.mediaBanTooltip = new HintView(PeerStoriesView.this.getContext(), 9, PeerStoriesView.this.resourcesProvider);
                PeerStoriesView.this.mediaBanTooltip.setVisibility(8);
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                peerStoriesView.addView(peerStoriesView.mediaBanTooltip, LayoutHelper.createFrame(-2, -2.0f, 51, 10.0f, 0.0f, 10.0f, 0.0f));
            }
            PeerStoriesView.this.mediaBanTooltip.setText(AndroidUtilities.replaceTags(LocaleController.formatString(PeerStoriesView.this.chatActivityEnterView.isInVideoMode() ? R.string.VideoMessagesRestrictedByPrivacy : R.string.VoiceMessagesRestrictedByPrivacy, MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId)).first_name)));
            PeerStoriesView.this.mediaBanTooltip.showForView(PeerStoriesView.this.chatActivityEnterView.getAudioVideoButtonContainer(), true);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onStickersExpandedChange() {
            PeerStoriesView.this.requestLayout();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public TLRPC$StoryItem getReplyToStory() {
            return PeerStoriesView.this.currentStory.storyItem;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createMentionsContainer() {
        MentionsContainerView mentionsContainerView = new MentionsContainerView(getContext(), this.dialogId, 0, this.storyViewer.fragment, this, this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.10
            @Override // org.telegram.ui.Components.MentionsContainerView
            public void drawRoundRect(Canvas canvas, Rect rect, float f) {
                PeerStoriesView.this.bitmapShaderTools.setBounds(getX(), -getY(), getX() + getMeasuredWidth(), (-getY()) + getMeasuredHeight());
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(rect);
                rectF.offset(0.0f, 0.0f);
                canvas.drawRoundRect(rectF, f, f, PeerStoriesView.this.bitmapShaderTools.paint);
                canvas.drawRoundRect(rectF, f, f, PeerStoriesView.this.inputBackgroundPaint);
                if (rectF.top < getMeasuredHeight() - 1) {
                    canvas.drawRect(0.0f, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight() - 1, PeerStoriesView.this.resourcesProvider.getPaint("paintDivider"));
                }
            }
        };
        this.mentionContainer = mentionsContainerView;
        mentionsContainerView.withDelegate(new MentionsContainerView.Delegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.11
            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public void onStickerSelected(TLRPC$TL_document tLRPC$TL_document, String str, Object obj) {
                SendMessagesHelper.getInstance(PeerStoriesView.this.currentAccount).sendSticker(tLRPC$TL_document, str, PeerStoriesView.this.dialogId, null, null, PeerStoriesView.this.currentStory.storyItem, null, true, 0, false, obj);
                PeerStoriesView.this.chatActivityEnterView.addStickerToRecent(tLRPC$TL_document);
                PeerStoriesView.this.chatActivityEnterView.setFieldText("");
                PeerStoriesView.this.afterMessageSend();
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public void replaceText(int i, int i2, CharSequence charSequence, boolean z) {
                PeerStoriesView.this.chatActivityEnterView.replaceWithText(i, i2, charSequence, z);
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public Paint.FontMetricsInt getFontMetrics() {
                return PeerStoriesView.this.chatActivityEnterView.getEditField().getPaint().getFontMetricsInt();
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public void addEmojiToRecent(String str) {
                PeerStoriesView.this.chatActivityEnterView.addEmojiToRecent(str);
            }
        });
        addView(this.mentionContainer, LayoutHelper.createFrame(-1, -1, 83));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveToGallery() {
        StoryItemHolder storyItemHolder = this.currentStory;
        TLRPC$StoryItem tLRPC$StoryItem = storyItemHolder.storyItem;
        if ((tLRPC$StoryItem == null && storyItemHolder.uploadingStory == null) || (tLRPC$StoryItem instanceof TLRPC$TL_storyItemSkipped)) {
            return;
        }
        File path = storyItemHolder.getPath();
        final boolean isVideo = this.currentStory.isVideo();
        if (path != null && path.exists()) {
            MediaController.saveFile(path.toString(), getContext(), isVideo ? 1 : 0, null, null, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda19
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    PeerStoriesView.this.lambda$saveToGallery$9(isVideo, (Uri) obj);
                }
            });
            return;
        }
        showDownloadAlert();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveToGallery$9(boolean z, Uri uri) {
        BulletinFactory.createSaveToGalleryBulletin(this.storyContainer, z, this.resourcesProvider).show();
    }

    private void showDownloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
        builder.setMessage(LocaleController.getString("PleaseDownload", R.string.PleaseDownload));
        this.delegate.showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openAttachMenu() {
        if (this.chatActivityEnterView == null) {
            return;
        }
        createChatAttachView();
        this.chatAttachAlert.getPhotoLayout().loadGalleryPhotos();
        int i = Build.VERSION.SDK_INT;
        if (i == 21 || i == 22) {
            this.chatActivityEnterView.closeKeyboard();
        }
        this.chatAttachAlert.setMaxSelectedPhotos(-1, true);
        this.chatAttachAlert.init();
        this.chatAttachAlert.getCommentTextView().setText(this.chatActivityEnterView.getFieldText());
        this.chatAttachAlert.setDialogId(this.dialogId);
        this.delegate.showDialog(this.chatAttachAlert);
    }

    private void createChatAttachView() {
        if (this.chatAttachAlert == null) {
            ChatAttachAlert chatAttachAlert = new ChatAttachAlert(getContext(), null, false, false, true, this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.12
                @Override // org.telegram.ui.Components.ChatAttachAlert, org.telegram.ui.ActionBar.BottomSheet
                public void dismissInternal() {
                    super.dismissInternal();
                }

                @Override // org.telegram.ui.ActionBar.BottomSheet
                public void onDismissAnimationStart() {
                    if (PeerStoriesView.this.chatAttachAlert != null) {
                        PeerStoriesView.this.chatAttachAlert.setFocusable(false);
                    }
                    ChatActivityEnterView chatActivityEnterView = PeerStoriesView.this.chatActivityEnterView;
                    if (chatActivityEnterView == null || chatActivityEnterView.getEditField() == null) {
                        return;
                    }
                    PeerStoriesView.this.chatActivityEnterView.getEditField().requestFocus();
                }
            };
            this.chatAttachAlert = chatAttachAlert;
            chatAttachAlert.setDelegate(new ChatAttachAlert.ChatAttachViewDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.13
                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ void didSelectBot(TLRPC$User tLRPC$User) {
                    ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$didSelectBot(this, tLRPC$User);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ void onWallpaperSelected(Object obj) {
                    ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$onWallpaperSelected(this, obj);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ void openAvatarsSearch() {
                    ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$openAvatarsSearch(this);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public void didPressedButton(int i, boolean z, boolean z2, int i2, boolean z3) {
                    String str;
                    if (PeerStoriesView.this.storyViewer.isShowing) {
                        PeerStoriesView peerStoriesView = PeerStoriesView.this;
                        TLRPC$StoryItem tLRPC$StoryItem = peerStoriesView.currentStory.storyItem;
                        if (tLRPC$StoryItem == null || (tLRPC$StoryItem instanceof TLRPC$TL_storyItemSkipped)) {
                            return;
                        }
                        int i3 = 4;
                        if (i == 8 || i == 7 || (i == 4 && !peerStoriesView.chatAttachAlert.getPhotoLayout().getSelectedPhotos().isEmpty())) {
                            if (i != 8) {
                                PeerStoriesView.this.chatAttachAlert.dismiss(true);
                            }
                            HashMap<Object, Object> selectedPhotos = PeerStoriesView.this.chatAttachAlert.getPhotoLayout().getSelectedPhotos();
                            ArrayList<Object> selectedPhotosOrder = PeerStoriesView.this.chatAttachAlert.getPhotoLayout().getSelectedPhotosOrder();
                            if (selectedPhotos.isEmpty()) {
                                return;
                            }
                            int i4 = 0;
                            int i5 = 0;
                            while (i5 < Math.ceil(selectedPhotos.size() / 10.0f)) {
                                int i6 = i5 * 10;
                                int min = Math.min(10, selectedPhotos.size() - i6);
                                ArrayList arrayList = new ArrayList();
                                for (int i7 = 0; i7 < min; i7++) {
                                    int i8 = i6 + i7;
                                    if (i8 < selectedPhotosOrder.size()) {
                                        MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) selectedPhotos.get(selectedPhotosOrder.get(i8));
                                        SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
                                        boolean z4 = photoEntry.isVideo;
                                        if (!z4 && (str = photoEntry.imagePath) != null) {
                                            sendingMediaInfo.path = str;
                                        } else {
                                            String str2 = photoEntry.path;
                                            if (str2 != null) {
                                                sendingMediaInfo.path = str2;
                                            }
                                        }
                                        sendingMediaInfo.thumbPath = photoEntry.thumbPath;
                                        sendingMediaInfo.isVideo = z4;
                                        CharSequence charSequence = photoEntry.caption;
                                        sendingMediaInfo.caption = charSequence != null ? charSequence.toString() : null;
                                        sendingMediaInfo.entities = photoEntry.entities;
                                        sendingMediaInfo.masks = photoEntry.stickers;
                                        sendingMediaInfo.ttl = photoEntry.ttl;
                                        sendingMediaInfo.videoEditedInfo = photoEntry.editedInfo;
                                        sendingMediaInfo.canDeleteAfter = photoEntry.canDeleteAfter;
                                        sendingMediaInfo.updateStickersOrder = SendMessagesHelper.checkUpdateStickersOrder(photoEntry.caption);
                                        sendingMediaInfo.hasMediaSpoilers = photoEntry.hasSpoiler;
                                        arrayList.add(sendingMediaInfo);
                                        photoEntry.reset();
                                    }
                                }
                                SendMessagesHelper.prepareSendingMedia(PeerStoriesView.this.getAccountInstance(), arrayList, PeerStoriesView.this.dialogId, null, null, tLRPC$StoryItem, i == i3 || z3, z, null, z2, i2, i5 == 0 ? ((SendMessagesHelper.SendingMediaInfo) arrayList.get(i4)).updateStickersOrder : false, null);
                                i5++;
                                selectedPhotos = selectedPhotos;
                                selectedPhotosOrder = selectedPhotosOrder;
                                i4 = 0;
                                i3 = 4;
                            }
                            PeerStoriesView.this.afterMessageSend();
                            PeerStoriesView.this.chatActivityEnterView.setFieldText("");
                        } else if (PeerStoriesView.this.chatAttachAlert != null) {
                            PeerStoriesView.this.chatAttachAlert.dismissWithButtonClick(i);
                        }
                    }
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public void onCameraOpened() {
                    PeerStoriesView.this.chatActivityEnterView.closeKeyboard();
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public void doOnIdle(Runnable runnable) {
                    NotificationCenter.getInstance(PeerStoriesView.this.currentAccount).doOnIdle(runnable);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public void sendAudio(ArrayList<MessageObject> arrayList, CharSequence charSequence, boolean z, int i) {
                    PeerStoriesView peerStoriesView = PeerStoriesView.this;
                    TLRPC$StoryItem tLRPC$StoryItem = peerStoriesView.currentStory.storyItem;
                    if (tLRPC$StoryItem == null || (tLRPC$StoryItem instanceof TLRPC$TL_storyItemSkipped)) {
                        return;
                    }
                    SendMessagesHelper.prepareSendingAudioDocuments(peerStoriesView.getAccountInstance(), arrayList, charSequence != null ? charSequence : null, PeerStoriesView.this.dialogId, null, null, tLRPC$StoryItem, z, i, null);
                    PeerStoriesView.this.afterMessageSend();
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public boolean needEnterComment() {
                    return PeerStoriesView.this.needEnterText();
                }
            });
            this.chatAttachAlert.getPhotoLayout().loadGalleryPhotos();
            this.chatAttachAlert.setAllowEnterCaption(true);
            this.chatAttachAlert.init();
            this.chatAttachAlert.setDocumentsDelegate(new ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.14
                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public /* synthetic */ void didSelectPhotos(ArrayList arrayList, boolean z, int i) {
                    ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate.-CC.$default$didSelectPhotos(this, arrayList, z, i);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public /* synthetic */ void startMusicSelectActivity() {
                    ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate.-CC.$default$startMusicSelectActivity(this);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public void didSelectFiles(ArrayList<String> arrayList, String str, ArrayList<MessageObject> arrayList2, boolean z, int i) {
                    PeerStoriesView peerStoriesView = PeerStoriesView.this;
                    TLRPC$StoryItem tLRPC$StoryItem = peerStoriesView.currentStory.storyItem;
                    if (tLRPC$StoryItem == null || (tLRPC$StoryItem instanceof TLRPC$TL_storyItemSkipped)) {
                        return;
                    }
                    SendMessagesHelper.prepareSendingDocuments(peerStoriesView.getAccountInstance(), arrayList, arrayList, null, str, null, PeerStoriesView.this.dialogId, null, null, tLRPC$StoryItem, null, z, i, null);
                    PeerStoriesView.this.afterMessageSend();
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public void startDocumentSelectActivity() {
                    try {
                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
                        if (Build.VERSION.SDK_INT >= 18) {
                            intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                        }
                        intent.setType("*/*");
                        PeerStoriesView.this.storyViewer.startActivityForResult(intent, 21);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            });
            this.chatAttachAlert.getCommentTextView().setText(this.chatActivityEnterView.getFieldText());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shareStory(boolean z) {
        StoryItemHolder storyItemHolder = this.currentStory;
        if (storyItemHolder.storyItem == null || this.storyViewer.fragment == null) {
            return;
        }
        String createLink = storyItemHolder.createLink();
        if (z) {
            ShareAlert shareAlert = new ShareAlert(this.storyViewer.fragment.getContext(), null, createLink, false, createLink, false, new WrappedResourceProvider(this, this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.15
                @Override // org.telegram.ui.WrappedResourceProvider
                public void appendColors() {
                    this.sparseIntArray.put(Theme.key_chat_emojiPanelBackground, ColorUtils.blendARGB(-16777216, -1, 0.2f));
                }
            }) { // from class: org.telegram.ui.Stories.PeerStoriesView.16
                @Override // org.telegram.ui.Components.ShareAlert, org.telegram.ui.ActionBar.BottomSheet
                public void dismissInternal() {
                    super.dismissInternal();
                    PeerStoriesView.this.shareAlert = null;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.ShareAlert
                public void onSend(LongSparseArray<TLRPC$Dialog> longSparseArray, int i, TLRPC$TL_forumTopic tLRPC$TL_forumTopic) {
                    super.onSend(longSparseArray, i, tLRPC$TL_forumTopic);
                    PeerStoriesView peerStoriesView = PeerStoriesView.this;
                    BulletinFactory of = BulletinFactory.of(peerStoriesView.storyContainer, peerStoriesView.resourcesProvider);
                    if (of != null) {
                        if (longSparseArray.size() == 1) {
                            long keyAt = longSparseArray.keyAt(0);
                            if (keyAt == UserConfig.getInstance(this.currentAccount).clientUserId) {
                                of.createSimpleBulletin(R.raw.saved_messages, AndroidUtilities.replaceTags(LocaleController.formatString("StorySharedToSavedMessages", R.string.StorySharedToSavedMessages, new Object[0])), 5000).hideAfterBottomSheet(false).show();
                            } else if (keyAt < 0) {
                                TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-keyAt));
                                int i2 = R.raw.forward;
                                int i3 = R.string.StorySharedTo;
                                Object[] objArr = new Object[1];
                                objArr[0] = tLRPC$TL_forumTopic != null ? tLRPC$TL_forumTopic.title : chat.title;
                                of.createSimpleBulletin(i2, AndroidUtilities.replaceTags(LocaleController.formatString("StorySharedTo", i3, objArr)), 5000).hideAfterBottomSheet(false).show();
                            } else {
                                of.createSimpleBulletin(R.raw.forward, AndroidUtilities.replaceTags(LocaleController.formatString("StorySharedTo", R.string.StorySharedTo, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(keyAt)).first_name)), 5000).hideAfterBottomSheet(false).show();
                            }
                        } else {
                            of.createSimpleBulletin(R.raw.forward, AndroidUtilities.replaceTags(LocaleController.formatPluralString("StorySharedToManyChats", longSparseArray.size(), Integer.valueOf(longSparseArray.size())))).hideAfterBottomSheet(false).show();
                        }
                        PeerStoriesView.this.performHapticFeedback(3);
                    }
                }
            };
            this.shareAlert = shareAlert;
            TLRPC$StoryItem tLRPC$StoryItem = this.currentStory.storyItem;
            tLRPC$StoryItem.dialogId = this.dialogId;
            shareAlert.setStoryToShare(tLRPC$StoryItem);
            this.shareAlert.setDelegate(new ShareAlert.ShareAlertDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.17
                @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
                public /* synthetic */ void didShare() {
                    ShareAlert.ShareAlertDelegate.-CC.$default$didShare(this);
                }

                @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
                public boolean didCopy() {
                    PeerStoriesView.this.onLickCopied();
                    return true;
                }
            });
            this.delegate.showDialog(this.shareAlert);
            return;
        }
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", createLink);
        LaunchActivity.instance.startActivityForResult(Intent.createChooser(intent, LocaleController.getString("StickersShare", R.string.StickersShare)), 500);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLickCopied() {
        if (this.currentStory.storyItem == null) {
            return;
        }
        TLRPC$TL_stories_exportStoryLink tLRPC$TL_stories_exportStoryLink = new TLRPC$TL_stories_exportStoryLink();
        tLRPC$TL_stories_exportStoryLink.id = this.currentStory.storyItem.id;
        tLRPC$TL_stories_exportStoryLink.user_id = MessagesController.getInstance(this.currentAccount).getInputUser(this.dialogId);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_stories_exportStoryLink, new RequestDelegate(this) { // from class: org.telegram.ui.Stories.PeerStoriesView.18
            @Override // org.telegram.tgnet.RequestDelegate
            public void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            }
        });
    }

    public void setDay(long j, ArrayList<Integer> arrayList) {
        this.dialogId = j;
        this.day = arrayList;
        bindInternal();
    }

    public void setDialogId(long j) {
        if (this.dialogId != j) {
            this.currentStory.clear();
        }
        this.dialogId = j;
        this.day = null;
        bindInternal();
        TLRPC$TL_userStories tLRPC$TL_userStories = this.storyViewer.overrideUserStories;
        if (tLRPC$TL_userStories != null) {
            this.storiesController.loadSkippedStories(tLRPC$TL_userStories, true);
        } else {
            this.storiesController.loadSkippedStories(j);
        }
    }

    private void bindInternal() {
        this.deletedPeer = false;
        this.forceUpdateOffsets = true;
        long j = this.dialogId;
        if (j >= 0) {
            this.isSelf = j == UserConfig.getInstance(this.currentAccount).getClientUserId();
            TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId));
            this.avatarDrawable.setInfo(user);
            this.headerView.backupImageView.getImageReceiver().setForUserOrChat(user, this.avatarDrawable);
            if (this.isSelf) {
                this.headerView.titleView.setText(LocaleController.getString("SelfStoryTitle", R.string.SelfStoryTitle));
                this.headerView.titleView.setRightDrawable((Drawable) null);
            } else {
                if (user != null && user.verified) {
                    Drawable mutate = ContextCompat.getDrawable(getContext(), R.drawable.verified_profile).mutate();
                    mutate.setAlpha(255);
                    CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, null);
                    combinedDrawable.setFullsize(true);
                    combinedDrawable.setCustomSize(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    this.headerView.titleView.setRightDrawable(combinedDrawable);
                } else {
                    this.headerView.titleView.setRightDrawable((Drawable) null);
                }
                if (user != null) {
                    this.headerView.titleView.setText(Emoji.replaceEmoji(ContactsController.formatName(user), this.headerView.titleView.getPaint().getFontMetricsInt(), false));
                } else {
                    this.headerView.titleView.setText(null);
                }
            }
            if (this.isActive) {
                this.storiesController.pollViewsForSelfStories(true);
            }
        } else {
            TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
            this.avatarDrawable.setInfo(chat);
            this.headerView.backupImageView.getImageReceiver().setForUserOrChat(chat, this.avatarDrawable);
            this.headerView.titleView.setText(chat.title);
            MessagesController.getInstance(this.currentAccount).getChatFull(-this.dialogId);
        }
        updateStoryItems();
        this.selectedPosition = 0;
        this.currentImageTime = 0L;
        this.switchEventSent = false;
        if (this.isSelf) {
            createSelfPeerView();
            this.selfView.setVisibility(0);
            ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
            if (chatActivityEnterView != null) {
                chatActivityEnterView.setVisibility(8);
            }
            ArrayList<Integer> arrayList = this.day;
            if (arrayList != null) {
                int indexOf = arrayList.indexOf(Integer.valueOf(this.storyViewer.dayStoryId));
                if (indexOf < 0 && !this.day.isEmpty()) {
                    if (this.storyViewer.dayStoryId > this.day.get(0).intValue()) {
                        indexOf = 0;
                    } else {
                        int i = this.storyViewer.dayStoryId;
                        ArrayList<Integer> arrayList2 = this.day;
                        if (i < arrayList2.get(arrayList2.size() - 1).intValue()) {
                            indexOf = this.day.size() - 1;
                        }
                    }
                }
                this.selectedPosition = Math.max(0, indexOf);
            } else if (!this.uploadingStories.isEmpty()) {
                this.selectedPosition = this.storyItems.size();
            } else {
                for (int i2 = 0; i2 < this.storyItems.size(); i2++) {
                    if (this.storyItems.get(i2).justUploaded || this.storyItems.get(i2).id > this.storiesController.dialogIdToMaxReadId.get(this.dialogId)) {
                        this.selectedPosition = i2;
                        break;
                    }
                }
            }
            updatePosition();
            this.storyContainer.invalidate();
            invalidate();
            return;
        }
        if (this.chatActivityEnterView == null) {
            createEnterView();
        }
        updateSelectedPosition();
        ChatActivityEnterView chatActivityEnterView2 = this.chatActivityEnterView;
        if (chatActivityEnterView2 != null) {
            chatActivityEnterView2.setVisibility(0);
            if (!TextUtils.isEmpty(this.chatActivityEnterView.getEditField().getText())) {
                this.chatActivityEnterView.getEditField().setText("");
            }
            this.chatActivityEnterView.setDialogId(this.dialogId, this.currentAccount);
            TLRPC$UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(this.dialogId);
            if (userFull != null) {
                this.chatActivityEnterView.updateRecordButton(null, userFull);
            } else {
                MessagesController.getInstance(this.currentAccount).loadFullUser(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId)), this.classGuid, false);
            }
        }
        this.count = getStoriesCount();
        FrameLayout frameLayout = this.selfView;
        if (frameLayout != null) {
            frameLayout.setVisibility(8);
        }
        updatePosition();
        this.storyContainer.invalidate();
        invalidate();
    }

    private void createUnsupportedContainer() {
        if (this.unsupportedContainer != null) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(getContext());
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        TextView textView = new TextView(getContext());
        textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        textView.setGravity(1);
        textView.setTextSize(1, 16.0f);
        textView.setText(LocaleController.getString("StoryUnsupported", R.string.StoryUnsupported));
        textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
        TextView textView2 = new TextView(getContext());
        ScaleStateListAnimator.apply(textView2);
        textView2.setText(LocaleController.getString("AppUpdate", R.string.AppUpdate));
        int i = Theme.key_featuredStickers_buttonText;
        textView2.setTextColor(Theme.getColor(i, this.resourcesProvider));
        textView2.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f));
        textView2.setGravity(17);
        textView2.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        textView2.setTextSize(1, 15.0f);
        textView2.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), Theme.getColor(Theme.key_featuredStickers_addButton, this.resourcesProvider), ColorUtils.setAlphaComponent(Theme.getColor(i, this.resourcesProvider), 30)));
        textView2.setOnClickListener(PeerStoriesView$$ExternalSyntheticLambda10.INSTANCE);
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2));
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 0.0f, 24.0f, 0.0f, 0.0f));
        frameLayout.addView(linearLayout, LayoutHelper.createFrame(-1, -2.0f, 17, 72.0f, 0.0f, 72.0f, 0.0f));
        this.storyContainer.addView(frameLayout);
        this.unsupportedContainer = frameLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createUnsupportedContainer$10(View view) {
        LaunchActivity launchActivity = LaunchActivity.instance;
        if (launchActivity != null) {
            launchActivity.checkAppUpdate(true);
        }
    }

    public void preloadMainImage(long j) {
        if (this.dialogId == j && this.day == null) {
            return;
        }
        this.dialogId = j;
        updateStoryItems();
        updateSelectedPosition();
        updatePosition(true);
        TLRPC$TL_userStories tLRPC$TL_userStories = this.storyViewer.overrideUserStories;
        if (tLRPC$TL_userStories != null) {
            this.storiesController.loadSkippedStories(tLRPC$TL_userStories, true);
        } else {
            this.storiesController.loadSkippedStories(j);
        }
    }

    private void updateSelectedPosition() {
        TLRPC$TL_userStories tLRPC$TL_userStories;
        ArrayList<Integer> arrayList;
        ArrayList<Integer> arrayList2 = this.day;
        if (arrayList2 != null) {
            int indexOf = arrayList2.indexOf(Integer.valueOf(this.storyViewer.dayStoryId));
            if (indexOf < 0 && !this.day.isEmpty()) {
                if (this.storyViewer.dayStoryId > this.day.get(0).intValue()) {
                    indexOf = 0;
                } else {
                    if (this.storyViewer.dayStoryId < this.day.get(arrayList.size() - 1).intValue()) {
                        indexOf = this.day.size() - 1;
                    }
                }
            }
            this.selectedPosition = indexOf;
        } else {
            int i = this.storyViewer.savedPositions.get(this.dialogId, -1);
            this.selectedPosition = i;
            if (i == -1 && !this.storyViewer.isSingleStory && (tLRPC$TL_userStories = this.userStories) != null && tLRPC$TL_userStories.max_read_id > 0) {
                int i2 = 0;
                while (true) {
                    if (i2 >= this.storyItems.size()) {
                        break;
                    } else if (this.storyItems.get(i2).id > this.userStories.max_read_id) {
                        this.selectedPosition = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
            }
        }
        if (this.selectedPosition == -1) {
            this.selectedPosition = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStoryItems() {
        TLRPC$StoryItem tLRPC$StoryItem;
        this.storyItems.clear();
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer.isSingleStory) {
            this.storyItems.add(storyViewer.singleStory);
        } else {
            ArrayList<Integer> arrayList = this.day;
            if (arrayList != null && storyViewer.storiesList != null) {
                Iterator<Integer> it = arrayList.iterator();
                while (it.hasNext()) {
                    MessageObject findMessageObject = this.storyViewer.storiesList.findMessageObject(it.next().intValue());
                    if (findMessageObject != null && (tLRPC$StoryItem = findMessageObject.storyItem) != null) {
                        this.storyItems.add(tLRPC$StoryItem);
                    }
                }
            } else {
                if (storyViewer.storiesList != null) {
                    for (int i = 0; i < this.storyViewer.storiesList.messageObjects.size(); i++) {
                        this.storyItems.add(this.storyViewer.storiesList.messageObjects.get(i).storyItem);
                    }
                } else {
                    TLRPC$TL_userStories tLRPC$TL_userStories = storyViewer.overrideUserStories;
                    if (tLRPC$TL_userStories != null && tLRPC$TL_userStories.user_id == this.dialogId) {
                        this.userStories = tLRPC$TL_userStories;
                    } else {
                        this.userStories = this.storiesController.getStories(this.dialogId);
                    }
                    this.totalStoriesCount = 0;
                    TLRPC$TL_userStories tLRPC$TL_userStories2 = this.userStories;
                    if (tLRPC$TL_userStories2 != null) {
                        this.totalStoriesCount = tLRPC$TL_userStories2.stories.size();
                        this.storyItems.addAll(this.userStories.stories);
                    }
                    this.uploadingStories.clear();
                    if (this.isSelf) {
                        this.uploadingStories.addAll(this.storiesController.getUploadingStories());
                    }
                }
            }
        }
        this.count = getStoriesCount();
    }

    private void createSelfPeerView() {
        if (this.selfView != null) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(getContext()) { // from class: org.telegram.ui.Stories.PeerStoriesView.19
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                int x;
                if (PeerStoriesView.this.selfAvatarsContainer.getVisibility() == 0 && PeerStoriesView.this.selfAvatarsContainer.getLayoutParams().width != (x = (int) (((PeerStoriesView.this.selfStatusView.getX() + PeerStoriesView.this.selfStatusView.getMeasuredWidth()) - PeerStoriesView.this.selfAvatarsContainer.getX()) + AndroidUtilities.dp(10.0f)))) {
                    PeerStoriesView.this.selfAvatarsContainer.getLayoutParams().width = x;
                    PeerStoriesView.this.selfAvatarsContainer.invalidate();
                    PeerStoriesView.this.selfAvatarsContainer.requestLayout();
                }
                super.dispatchDraw(canvas);
            }
        };
        this.selfView = frameLayout;
        frameLayout.setClickable(true);
        addView(this.selfView, LayoutHelper.createFrame(-1, 48.0f, 48, 0.0f, 0.0f, 96.0f, 0.0f));
        View view = new View(getContext()) { // from class: org.telegram.ui.Stories.PeerStoriesView.20
            LoadingDrawable loadingDrawable = new LoadingDrawable();
            AnimatedFloat animatedFloat = new AnimatedFloat(250, CubicBezierInterpolator.DEFAULT);

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                this.animatedFloat.setParent(this);
                this.animatedFloat.set(PeerStoriesView.this.showViewsProgress ? 1.0f : 0.0f, false);
                if (this.animatedFloat.get() != 0.0f) {
                    if (this.animatedFloat.get() != 1.0f) {
                        canvas.saveLayerAlpha(0.0f, 0.0f, getLayoutParams().width, getMeasuredHeight(), (int) (this.animatedFloat.get() * 255.0f), 31);
                    } else {
                        canvas.save();
                    }
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(0.0f, 0.0f, getLayoutParams().width, getMeasuredHeight());
                    this.loadingDrawable.setBounds(rectF);
                    this.loadingDrawable.setRadiiDp(24.0f);
                    this.loadingDrawable.setColors(ColorUtils.setAlphaComponent(-1, 20), ColorUtils.setAlphaComponent(-1, 50), ColorUtils.setAlphaComponent(-1, 50), ColorUtils.setAlphaComponent(-1, 70));
                    this.loadingDrawable.draw(canvas);
                    invalidate();
                    canvas.restore();
                }
            }
        };
        this.selfAvatarsContainer = view;
        view.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PeerStoriesView.this.lambda$createSelfPeerView$11(view2);
            }
        });
        this.selfView.addView(this.selfAvatarsContainer, LayoutHelper.createFrame(-1, 32.0f, 0, 9.0f, 11.0f, 0.0f, 0.0f));
        HwAvatarsImageView hwAvatarsImageView = new HwAvatarsImageView(getContext(), false);
        this.selfAvatarsView = hwAvatarsImageView;
        hwAvatarsImageView.setAvatarsTextSize(AndroidUtilities.dp(18.0f));
        this.selfView.addView(this.selfAvatarsView, LayoutHelper.createFrame(-1, 28.0f, 0, 13.0f, 13.0f, 0.0f, 0.0f));
        TextView textView = new TextView(getContext());
        this.selfStatusView = textView;
        textView.setTextSize(1, 14.0f);
        this.selfStatusView.setTextColor(-1);
        this.selfView.addView(this.selfStatusView, LayoutHelper.createFrame(-2, -2.0f, 0, 0.0f, 16.0f, 0.0f, 9.0f));
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(this.sharedResources.deleteDrawable);
        this.selfAvatarsContainer.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(15.0f), 0, ColorUtils.setAlphaComponent(-1, 120)));
        imageView.setBackground(Theme.createCircleSelectorDrawable(ColorUtils.setAlphaComponent(-1, 120), -AndroidUtilities.dp(2.0f), -AndroidUtilities.dp(2.0f)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSelfPeerView$11(View view) {
        showUserViewsDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteStory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        builder.setTitle(LocaleController.getString("DeleteStoryTitle", R.string.DeleteStoryTitle));
        builder.setMessage(LocaleController.getString("DeleteStorySubtitle", R.string.DeleteStorySubtitle));
        builder.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView.21
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                PeerStoriesView.this.currentStory.cancelOrDelete();
                PeerStoriesView.this.updateStoryItems();
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                if (!peerStoriesView.isActive || peerStoriesView.count != 0) {
                    int i2 = peerStoriesView.selectedPosition;
                    PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                    int i3 = peerStoriesView2.count;
                    if (i2 >= i3) {
                        peerStoriesView2.selectedPosition = i3 - 1;
                    } else if (peerStoriesView2.selectedPosition < 0) {
                        PeerStoriesView.this.selectedPosition = 0;
                    }
                    PeerStoriesView.this.updatePosition();
                    return;
                }
                peerStoriesView.delegate.switchToNextAndRemoveCurrentPeer();
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), PeerStoriesView$$ExternalSyntheticLambda3.INSTANCE);
        AlertDialog create = builder.create();
        this.delegate.showDialog(create);
        create.redPositive();
    }

    private void showUserViewsDialog() {
        if (StoriesUtilities.hasExpiredViews(this.currentStory.storyItem)) {
            performHapticFeedback(3);
            BulletinFactory global = BulletinFactory.global();
            if (global != null) {
                global.createErrorBulletin(AndroidUtilities.replaceTags(LocaleController.getString("ExpiredViewsStub", R.string.ExpiredViewsStub))).show();
                return;
            }
            return;
        }
        this.storyViewer.openViews();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.sharedResources.topOverlayGradient.setBounds(0, 0, getMeasuredWidth(), AndroidUtilities.dp(72.0f));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        updateViewOffsets();
        super.dispatchDraw(canvas);
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.drawRecordedPannel(canvas);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attachedToWindow = true;
        this.imageReceiver.onAttachedToWindow();
        this.rightPreloadImageReceiver.onAttachedToWindow();
        this.leftPreloadImageReceiver.onAttachedToWindow();
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onResume();
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storiesUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storiesListUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachedToWindow = false;
        this.imageReceiver.onDetachedFromWindow();
        this.rightPreloadImageReceiver.onDetachedFromWindow();
        this.leftPreloadImageReceiver.onDetachedFromWindow();
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onPause();
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storiesUpdated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storiesListUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.storiesUpdated || (i == NotificationCenter.storiesListUpdated && this.storyViewer.storiesList == objArr[0])) {
            Delegate delegate = this.delegate;
            if (delegate == null || !delegate.isClosed()) {
                if (this.isActive) {
                    updateStoryItems();
                    if (this.count == 0) {
                        if (this.deletedPeer) {
                            return;
                        }
                        this.deletedPeer = true;
                        this.delegate.switchToNextAndRemoveCurrentPeer();
                        return;
                    }
                    if (this.selectedPosition >= this.storyItems.size() + this.uploadingStories.size()) {
                        this.selectedPosition = (this.storyItems.size() + this.uploadingStories.size()) - 1;
                    }
                    updatePosition();
                    if (this.isSelf) {
                        updateUserViews();
                    }
                }
                TLRPC$TL_userStories tLRPC$TL_userStories = this.storyViewer.overrideUserStories;
                if (tLRPC$TL_userStories != null) {
                    this.storiesController.loadSkippedStories(tLRPC$TL_userStories, true);
                } else {
                    long j = this.dialogId;
                    if (j != 0) {
                        this.storiesController.loadSkippedStories(j);
                    }
                }
                ActionBarMenuSubItem actionBarMenuSubItem = this.editStoryItem;
                if (actionBarMenuSubItem != null) {
                    actionBarMenuSubItem.animate().alpha((this.storiesController.hasUploadingStories() && this.currentStory.isVideo && !SharedConfig.allowPreparingHevcPlayers()) ? 0.5f : 1.0f).start();
                }
            }
        } else if (i == NotificationCenter.emojiLoaded) {
            this.storyCaptionView.captionTextview.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePosition() {
        updatePosition(false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:235:0x05f6  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x0601  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x060b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updatePosition(boolean z) {
        boolean z2;
        int i;
        boolean z3;
        TLRPC$StoryItem tLRPC$StoryItem;
        ArrayList<TLRPC$PhotoSize> arrayList;
        ImageReceiver imageReceiver;
        boolean z4;
        Drawable drawable;
        boolean z5;
        TLRPC$StoryItem tLRPC$StoryItem2;
        TLRPC$StoryItem tLRPC$StoryItem3;
        int i2;
        StoriesController.UploadingStory uploadingStory;
        boolean z6;
        TLRPC$StoryItem tLRPC$StoryItem4;
        CharSequence charSequence;
        ChatActivityEnterView chatActivityEnterView;
        int i3;
        FrameLayout frameLayout;
        FrameLayout frameLayout2;
        ChatActivityEnterView chatActivityEnterView2;
        TLRPC$StoryItem tLRPC$StoryItem5;
        boolean z7;
        int i4;
        int i5;
        TLRPC$StoryItem tLRPC$StoryItem6;
        String str;
        TLRPC$StoryItem tLRPC$StoryItem7;
        BitmapDrawable bitmapDrawable;
        StoriesController.UploadingStory uploadingStory2;
        boolean z8;
        int i6;
        if (this.storyItems.isEmpty() && this.uploadingStories.isEmpty()) {
            return;
        }
        this.forceUpdateOffsets = true;
        StoryItemHolder storyItemHolder = this.currentStory;
        TLRPC$StoryItem tLRPC$StoryItem8 = storyItemHolder.storyItem;
        StoriesController.UploadingStory uploadingStory3 = storyItemHolder.uploadingStory;
        String storyImageFilter = StoriesUtilities.getStoryImageFilter();
        this.lastNoThumb = false;
        this.unsupported = false;
        int i7 = this.selectedPosition;
        boolean z9 = this.isUploading;
        boolean z10 = this.isEditing;
        this.currentStory.editingSourceItem = null;
        boolean isEmpty = this.uploadingStories.isEmpty();
        int i8 = ImageReceiver.DEFAULT_CROSSFADE_DURATION;
        if (!isEmpty && i7 >= this.storyItems.size()) {
            this.isUploading = true;
            this.isEditing = false;
            int size = i7 - this.storyItems.size();
            if (size < 0 || size >= this.uploadingStories.size()) {
                return;
            }
            StoriesController.UploadingStory uploadingStory4 = this.uploadingStories.get(size);
            this.imageReceiver.setCrossfadeWithOldImage(false);
            this.imageReceiver.setCrossfadeDuration(ImageReceiver.DEFAULT_CROSSFADE_DURATION);
            Bitmap bitmap = uploadingStory4.entry.thumbBitmap;
            if (bitmap != null) {
                Bitmap createBitmap = Bitmap.createBitmap(bitmap);
                Utilities.blurBitmap(createBitmap, 3, 1, createBitmap.getWidth(), createBitmap.getHeight(), createBitmap.getRowBytes());
                bitmapDrawable = new BitmapDrawable(createBitmap);
            } else {
                bitmapDrawable = null;
            }
            if (uploadingStory4.isVideo) {
                uploadingStory2 = uploadingStory4;
                z2 = z9;
                z8 = z10;
                i6 = size;
                this.imageReceiver.setImage(null, null, ImageLocation.getForPath(uploadingStory4.firstFramePath), storyImageFilter, null, null, bitmapDrawable, 0L, null, null, 0);
            } else {
                uploadingStory2 = uploadingStory4;
                z2 = z9;
                z8 = z10;
                i6 = size;
                this.imageReceiver.setImage(null, null, ImageLocation.getForPath(uploadingStory2.path), storyImageFilter, null, null, bitmapDrawable, 0L, null, null, 0);
            }
            this.currentStory.set(uploadingStory2);
            this.allowShare = false;
            i2 = i6;
            z3 = z8;
        } else {
            z2 = z9;
            this.isUploading = false;
            this.isEditing = false;
            if (i7 < 0 || i7 > this.storyItems.size() - 1) {
                this.storyViewer.close(true);
                return;
            }
            TLRPC$StoryItem tLRPC$StoryItem9 = this.storyItems.get(i7);
            StoriesController.UploadingStory findEditingStory = this.storiesController.findEditingStory(tLRPC$StoryItem9);
            if (findEditingStory != null) {
                this.isEditing = true;
                this.imageReceiver.setCrossfadeWithOldImage(false);
                ImageReceiver imageReceiver2 = this.imageReceiver;
                if (this.onImageReceiverThumbLoaded != null) {
                    i8 = 0;
                }
                imageReceiver2.setCrossfadeDuration(i8);
                if (findEditingStory.isVideo) {
                    uploadingStory = findEditingStory;
                    i = i7;
                    this.imageReceiver.setImage(null, null, ImageLocation.getForPath(findEditingStory.firstFramePath), storyImageFilter, null, 0L, null, null, 0);
                } else {
                    uploadingStory = findEditingStory;
                    i = i7;
                    this.imageReceiver.setImage(null, null, ImageLocation.getForPath(uploadingStory.firstFramePath), storyImageFilter, null, 0L, null, null, 0);
                }
                this.currentStory.set(uploadingStory);
                this.currentStory.editingSourceItem = tLRPC$StoryItem9;
                this.allowShare = false;
                z3 = z10;
            } else {
                i = i7;
                TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$StoryItem9.media;
                boolean z11 = tLRPC$MessageMedia != null && MessageObject.isVideoDocument(tLRPC$MessageMedia.document);
                tLRPC$StoryItem9.dialogId = this.dialogId;
                this.imageReceiver.setCrossfadeWithOldImage(z10);
                this.imageReceiver.setCrossfadeDuration(ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$StoryItem9.media;
                if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaUnsupported) {
                    this.unsupported = true;
                    z3 = z10;
                    tLRPC$StoryItem = tLRPC$StoryItem9;
                } else {
                    String str2 = tLRPC$StoryItem9.attachPath;
                    if (str2 != null) {
                        if (tLRPC$MessageMedia2 == null) {
                            z11 = str2.toLowerCase().endsWith(".mp4");
                        }
                        if (z11) {
                            TLRPC$MessageMedia tLRPC$MessageMedia3 = tLRPC$StoryItem9.media;
                            Drawable createStripedBitmap = tLRPC$MessageMedia3 != null ? ImageLoader.createStripedBitmap(tLRPC$MessageMedia3.document.thumbs) : null;
                            if (tLRPC$StoryItem9.firstFramePath != null) {
                                if (ImageLoader.getInstance().isInMemCache(ImageLocation.getForPath(tLRPC$StoryItem9.firstFramePath).getKey(null, null, false) + "@" + storyImageFilter, false)) {
                                    z5 = z10;
                                    tLRPC$StoryItem2 = tLRPC$StoryItem9;
                                    this.imageReceiver.setImage(null, null, ImageLocation.getForPath(tLRPC$StoryItem9.firstFramePath), storyImageFilter, null, null, createStripedBitmap, 0L, null, null, 0);
                                    tLRPC$StoryItem = tLRPC$StoryItem2;
                                    z3 = z5;
                                }
                            }
                            z5 = z10;
                            tLRPC$StoryItem2 = tLRPC$StoryItem9;
                            this.imageReceiver.setImage(null, null, ImageLocation.getForPath(tLRPC$StoryItem2.attachPath), storyImageFilter + "_pframe", null, null, createStripedBitmap, 0L, null, null, 0);
                            tLRPC$StoryItem = tLRPC$StoryItem2;
                            z3 = z5;
                        } else {
                            TLRPC$MessageMedia tLRPC$MessageMedia4 = tLRPC$StoryItem9.media;
                            TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia4 != null ? tLRPC$MessageMedia4.photo : null;
                            if (tLRPC$Photo != null) {
                                drawable = ImageLoader.createStripedBitmap(tLRPC$Photo.sizes);
                                z4 = z10;
                            } else {
                                z4 = z10;
                                drawable = null;
                            }
                            if (z4) {
                                this.imageReceiver.setImage(ImageLocation.getForPath(tLRPC$StoryItem9.attachPath), storyImageFilter, ImageLocation.getForPath(tLRPC$StoryItem9.firstFramePath), storyImageFilter, drawable, 0L, null, null, 0);
                            } else {
                                this.imageReceiver.setImage(ImageLocation.getForPath(tLRPC$StoryItem9.attachPath), storyImageFilter, null, null, drawable, 0L, null, null, 0);
                            }
                            tLRPC$StoryItem = tLRPC$StoryItem9;
                            z3 = z4;
                        }
                    } else {
                        StoryViewer storyViewer = this.storyViewer;
                        Drawable drawable2 = (!storyViewer.isSingleStory || (imageReceiver = storyViewer.transitionViewHolder.storyImage) == null) ? null : imageReceiver.getDrawable();
                        tLRPC$StoryItem9.dialogId = this.dialogId;
                        if (z11) {
                            TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$StoryItem9.media.document.thumbs, 1000);
                            if (drawable2 == null) {
                                drawable2 = ImageLoader.createStripedBitmap(tLRPC$StoryItem9.media.document.thumbs);
                            }
                            ImageLocation forDocument = ImageLocation.getForDocument(closestPhotoSizeWithSize, tLRPC$StoryItem9.media.document);
                            tLRPC$StoryItem = tLRPC$StoryItem9;
                            z3 = z10;
                            this.imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$StoryItem9.media.document), storyImageFilter + "_pframe", forDocument, storyImageFilter, null, null, drawable2, 0L, null, tLRPC$StoryItem, 0);
                        } else {
                            z3 = z10;
                            TLRPC$MessageMedia tLRPC$MessageMedia5 = tLRPC$StoryItem9.media;
                            TLRPC$Photo tLRPC$Photo2 = tLRPC$MessageMedia5 != null ? tLRPC$MessageMedia5.photo : null;
                            if (tLRPC$Photo2 != null && (arrayList = tLRPC$Photo2.sizes) != null) {
                                if (drawable2 == null) {
                                    drawable2 = ImageLoader.createStripedBitmap(arrayList);
                                }
                                tLRPC$StoryItem = tLRPC$StoryItem9;
                                this.imageReceiver.setImage(null, null, ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo2.sizes, ConnectionsManager.DEFAULT_DATACENTER_ID), tLRPC$Photo2), storyImageFilter, null, null, drawable2, 0L, null, tLRPC$StoryItem9, 0);
                            } else {
                                tLRPC$StoryItem = tLRPC$StoryItem9;
                                this.imageReceiver.clearImage();
                            }
                        }
                    }
                }
                TLRPC$StoryItem tLRPC$StoryItem10 = tLRPC$StoryItem;
                tLRPC$StoryItem10.dialogId = this.dialogId;
                this.currentStory.set(tLRPC$StoryItem10);
                boolean z12 = (this.unsupported || (tLRPC$StoryItem3 = this.currentStory.storyItem) == null || (tLRPC$StoryItem3 instanceof TLRPC$TL_storyItemDeleted) || (tLRPC$StoryItem3 instanceof TLRPC$TL_storyItemSkipped)) ? false : true;
                this.allowShare = z12;
                if (z12) {
                    TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId));
                    this.allowShare = this.currentStory.allowScreenshots() && user != null && UserObject.getPublicUsername(user) != null && (this.currentStory.storyItem.isPublic || MessagesController.getInstance(this.currentAccount).storiesExportNopublicLink);
                }
                NotificationsController.getInstance(this.currentAccount).processReadStories(this.dialogId, tLRPC$StoryItem10.id);
            }
            i2 = i;
        }
        TLRPC$StoryItem tLRPC$StoryItem11 = this.currentStory.storyItem;
        if (tLRPC$StoryItem11 != null && !z) {
            this.storyViewer.dayStoryId = tLRPC$StoryItem11.id;
        }
        this.storyViewer.storiesViewPager.checkAllowScreenshots();
        this.imageChanged = true;
        if (this.isSelf) {
            updateUserViews();
        }
        StoryItemHolder storyItemHolder2 = this.currentStory;
        boolean z13 = getStoryId(storyItemHolder2.storyItem, storyItemHolder2.uploadingStory) == getStoryId(tLRPC$StoryItem8, uploadingStory3) || !(uploadingStory3 == null || (tLRPC$StoryItem7 = this.currentStory.storyItem) == null || !TextUtils.equals(uploadingStory3.path, tLRPC$StoryItem7.attachPath));
        boolean z14 = z13 && !(this.isEditing == z3 && this.isUploading == z2);
        if ((uploadingStory3 == null || (str = uploadingStory3.path) == null || !str.equals(this.currentStory.getLocalPath())) && (tLRPC$StoryItem8 == null || (tLRPC$StoryItem4 = this.currentStory.storyItem) == null || tLRPC$StoryItem8.id != tLRPC$StoryItem4.id)) {
            ChatActivityEnterView chatActivityEnterView3 = this.chatActivityEnterView;
            if (chatActivityEnterView3 != null) {
                chatActivityEnterView3.getEditField().setText("");
            }
            this.currentImageTime = 0L;
            this.switchEventSent = false;
            StoriesController.UploadingStory uploadingStory5 = this.currentStory.uploadingStory;
            if (uploadingStory5 != null) {
                RadialProgress radialProgress = this.headerView.radialProgress;
                if (radialProgress != null) {
                    radialProgress.setProgress(uploadingStory5.progress, false);
                }
                this.headerView.backupImageView.invalidate();
            } else if (!z14) {
                this.headerView.progressToUploading = 0.0f;
            }
            Bulletin.hideVisible();
            this.storyCaptionView.reset();
            cancelWaiting();
            z6 = true;
        } else {
            z6 = false;
        }
        if (z6 || (uploadingStory3 != null && this.currentStory.uploadingStory == null)) {
            StoryItemHolder storyItemHolder3 = this.currentStory;
            if (storyItemHolder3.uploadingStory != null) {
                PeerHeaderView peerHeaderView = this.headerView;
                peerHeaderView.setSubtitle(StoriesUtilities.getUploadingStr(peerHeaderView.subtitleView[0], false, this.isEditing), z14);
            } else {
                TLRPC$StoryItem tLRPC$StoryItem12 = storyItemHolder3.storyItem;
                if (tLRPC$StoryItem12 != null) {
                    int i9 = tLRPC$StoryItem12.date;
                    if (i9 == -1) {
                        this.headerView.setSubtitle(LocaleController.getString("CachedStory", R.string.CachedStory));
                    } else {
                        String formatStoryDate = LocaleController.formatStoryDate(i9);
                        String str3 = formatStoryDate;
                        if (this.currentStory.storyItem.edited) {
                            SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(formatStoryDate);
                            DotDividerSpan dotDividerSpan = new DotDividerSpan();
                            dotDividerSpan.setTopPadding(AndroidUtilities.dp(1.5f));
                            dotDividerSpan.setSize(5);
                            valueOf.append((CharSequence) " . ").setSpan(dotDividerSpan, valueOf.length() - 2, valueOf.length() - 1, 0);
                            valueOf.append((CharSequence) LocaleController.getString("EditedMessage", R.string.EditedMessage));
                            str3 = valueOf;
                        }
                        this.headerView.setSubtitle(str3, z14);
                    }
                }
            }
            HintView2 hintView2 = this.privacyHint;
            if (hintView2 != null) {
                hintView2.hide(false);
            }
            HintView2 hintView22 = this.soundTooltip;
            if (hintView22 != null) {
                hintView22.hide(false);
            }
        }
        StoryItemHolder storyItemHolder4 = this.currentStory;
        StoriesController.UploadingStory uploadingStory6 = storyItemHolder4.uploadingStory;
        if (uploadingStory6 != null) {
            charSequence = Emoji.replaceEmoji(uploadingStory6.entry.caption, this.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            MessageObject.addLinks(true, SpannableStringBuilder.valueOf(charSequence));
        } else {
            TLRPC$StoryItem tLRPC$StoryItem13 = storyItemHolder4.storyItem;
            if (tLRPC$StoryItem13 != null) {
                charSequence = Emoji.replaceEmoji(tLRPC$StoryItem13.caption, this.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                if (charSequence != null && this.currentStory.storyItem.entities != null) {
                    SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(MessageObject.replaceAnimatedEmoji(new SpannableStringBuilder(this.currentStory.storyItem.caption), this.currentStory.storyItem.entities, this.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), false));
                    SpannableStringBuilder.valueOf(Emoji.replaceEmoji(valueOf2, this.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), false));
                    MessageObject.addLinks(true, valueOf2);
                    MessageObject.addEntitiesToText(valueOf2, this.currentStory.storyItem.entities, false, true, true, false);
                    charSequence = valueOf2;
                }
            } else {
                charSequence = null;
            }
        }
        if (this.unsupported) {
            createUnsupportedContainer();
            createReplyDisabledView();
            this.unsupportedContainer.setVisibility(0);
            this.replyDisabledTextView.setVisibility(0);
            this.allowShare = false;
            ChatActivityEnterView chatActivityEnterView4 = this.chatActivityEnterView;
            if (chatActivityEnterView4 != null) {
                chatActivityEnterView4.setVisibility(8);
            }
            FrameLayout frameLayout3 = this.selfView;
            if (frameLayout3 != null) {
                frameLayout3.setVisibility(8);
            }
        } else {
            if (UserObject.isService(this.dialogId) && (chatActivityEnterView2 = this.chatActivityEnterView) != null) {
                chatActivityEnterView2.setVisibility(8);
            } else if (!this.isSelf && (chatActivityEnterView = this.chatActivityEnterView) != null) {
                i3 = 0;
                chatActivityEnterView.setVisibility(0);
                if (this.isSelf && (frameLayout2 = this.selfView) != null) {
                    frameLayout2.setVisibility(i3);
                }
                frameLayout = this.unsupportedContainer;
                if (frameLayout != null) {
                    frameLayout.setVisibility(8);
                }
                if (!UserObject.isService(this.dialogId)) {
                    createReplyDisabledView();
                    this.replyDisabledTextView.setVisibility(0);
                } else {
                    TextView textView = this.replyDisabledTextView;
                    if (textView != null) {
                        textView.setVisibility(8);
                    }
                }
            }
            i3 = 0;
            if (this.isSelf) {
                frameLayout2.setVisibility(i3);
            }
            frameLayout = this.unsupportedContainer;
            if (frameLayout != null) {
            }
            if (!UserObject.isService(this.dialogId)) {
            }
        }
        if (charSequence != null && !this.unsupported) {
            this.storyCaptionView.captionTextview.setText(charSequence);
            this.storyCaptionView.setVisibility(0);
        } else {
            if (this.isActive) {
                this.delegate.setIsCaption(false);
                Delegate delegate = this.delegate;
                this.isCaptionPartVisible = false;
                delegate.setIsCaptionPartVisible(false);
            }
            this.storyCaptionView.setVisibility(8);
        }
        this.storyContainer.invalidate();
        if (this.delegate != null && isSelectedPeer()) {
            this.delegate.onPeerSelected(this.dialogId, this.selectedPosition);
        }
        this.shareButton.setVisibility(this.allowShare ? 0 : 8);
        this.storyViewer.savedPositions.append(this.dialogId, i2);
        if (this.isActive) {
            requestVideoPlayer(0L);
            updatePreloadImages();
            this.imageReceiver.bumpPriority();
        }
        this.listPosition = 0;
        if (this.storyViewer.storiesList != null && (tLRPC$StoryItem6 = this.currentStory.storyItem) != null) {
            int i10 = tLRPC$StoryItem6.id;
            int i11 = 0;
            while (true) {
                if (i11 < this.storyViewer.storiesList.messageObjects.size()) {
                    MessageObject messageObject = this.storyViewer.storiesList.messageObjects.get(i11);
                    if (messageObject != null && messageObject.getId() == i10) {
                        this.listPosition = i11;
                        break;
                    }
                    i11++;
                } else {
                    break;
                }
            }
        }
        int i12 = this.selectedPosition;
        this.linesPosition = i12;
        int i13 = this.count;
        this.linesCount = i13;
        if (this.storyViewer.reversed) {
            this.linesPosition = (i13 - 1) - i12;
        }
        if (this.currentStory.isVideo()) {
            this.muteIconContainer.setVisibility(0);
            this.muteIconViewAlpha = this.currentStory.hasSound() ? 1.0f : 0.5f;
            if (this.currentStory.hasSound()) {
                this.muteIconView.setVisibility(0);
                this.noSoundIconView.setVisibility(8);
            } else {
                this.muteIconView.setVisibility(8);
                this.noSoundIconView.setVisibility(0);
            }
            this.muteIconContainer.setAlpha(this.muteIconViewAlpha * (1.0f - this.outT));
        } else {
            this.muteIconContainer.setVisibility(8);
        }
        StoryItemHolder storyItemHolder5 = this.currentStory;
        StoriesController.UploadingStory uploadingStory7 = storyItemHolder5.uploadingStory;
        if (uploadingStory7 != null) {
            this.privacyButton.set(this.isSelf, uploadingStory7, z13 && this.editedPrivacy);
        } else {
            TLRPC$StoryItem tLRPC$StoryItem14 = storyItemHolder5.storyItem;
            if (tLRPC$StoryItem14 != null) {
                this.privacyButton.set(this.isSelf, tLRPC$StoryItem14, z13 && this.editedPrivacy);
            } else {
                StoryPrivacyButton storyPrivacyButton = this.privacyButton;
                boolean z15 = this.isSelf;
                if (z13 && this.editedPrivacy) {
                    tLRPC$StoryItem5 = null;
                    z7 = true;
                } else {
                    tLRPC$StoryItem5 = null;
                    z7 = false;
                }
                storyPrivacyButton.set(z15, tLRPC$StoryItem5, z7);
            }
        }
        this.editedPrivacy = false;
        this.privacyButton.setTranslationX(this.muteIconContainer.getVisibility() == 0 ? -AndroidUtilities.dp(44.0f) : 0.0f);
        this.sharedResources.setIconMuted(!this.storyViewer.soundEnabled(), false);
        if (this.isActive && this.currentStory.storyItem != null) {
            FileLog.d("StoryViewer displayed story dialogId=" + this.dialogId + " storyId=" + this.currentStory.storyItem.id);
        }
        if (this.isSelf) {
            SelfStoryViewsPage.preload(this.currentAccount, this.currentStory.storyItem);
        }
        SimpleTextView simpleTextView = this.headerView.titleView;
        StoriesController.StoriesList storiesList = this.storyViewer.storiesList;
        if (storiesList == null || storiesList.getCount() == this.linesCount) {
            i4 = 0;
            i5 = 0;
        } else {
            i5 = AndroidUtilities.dp(56.0f);
            i4 = 0;
        }
        simpleTextView.setPadding(i4, i4, i5, i4);
    }

    private void createReplyDisabledView() {
        if (this.replyDisabledTextView != null) {
            return;
        }
        TextView textView = new TextView(getContext());
        this.replyDisabledTextView = textView;
        textView.setTextSize(1, 14.0f);
        this.replyDisabledTextView.setTextColor(ColorUtils.blendARGB(-16777216, -1, 0.42f));
        this.replyDisabledTextView.setGravity(3);
        this.replyDisabledTextView.setText(LocaleController.getString("StoryReplyDisabled", R.string.StoryReplyDisabled));
        addView(this.replyDisabledTextView, LayoutHelper.createFrame(-2, -2, 1));
    }

    private void updatePreloadImages() {
        int i;
        ImageReceiver imageReceiver;
        TLRPC$Document tLRPC$Document;
        int max = (int) (Math.max(AndroidUtilities.getRealScreenSize().x, AndroidUtilities.getRealScreenSize().y) / AndroidUtilities.density);
        String str = max + "_" + max;
        this.uriesToPrepare.clear();
        this.documentsToPrepare.clear();
        for (int i2 = 0; i2 < 2; i2++) {
            int i3 = this.selectedPosition;
            if (i2 == 0) {
                i = i3 - 1;
                imageReceiver = this.leftPreloadImageReceiver;
                if (i < 0) {
                    imageReceiver.clearImage();
                }
            } else {
                i = i3 + 1;
                imageReceiver = this.rightPreloadImageReceiver;
                if (i >= getStoriesCount()) {
                    imageReceiver.clearImage();
                }
            }
            if (!this.uploadingStories.isEmpty() && i >= this.storyItems.size()) {
                setStoryImage(this.uploadingStories.get(i - this.storyItems.size()), imageReceiver, str);
            } else {
                if (i < 0) {
                    i = 0;
                }
                boolean z = true;
                if (i >= this.storyItems.size()) {
                    i = this.storyItems.size() - 1;
                }
                TLRPC$StoryItem tLRPC$StoryItem = this.storyItems.get(i);
                tLRPC$StoryItem.dialogId = this.dialogId;
                setStoryImage(tLRPC$StoryItem, imageReceiver, str);
                TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$StoryItem.media;
                if ((tLRPC$MessageMedia == null || (tLRPC$Document = tLRPC$MessageMedia.document) == null || !MessageObject.isVideoDocument(tLRPC$Document)) ? false : false) {
                    TLRPC$Document tLRPC$Document2 = tLRPC$StoryItem.media.document;
                    if (tLRPC$StoryItem.fileReference == 0) {
                        tLRPC$StoryItem.fileReference = FileLoader.getInstance(this.currentAccount).getFileReference(tLRPC$StoryItem);
                    }
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append("?account=");
                        sb.append(this.currentAccount);
                        sb.append("&id=");
                        sb.append(tLRPC$Document2.id);
                        sb.append("&hash=");
                        sb.append(tLRPC$Document2.access_hash);
                        sb.append("&dc=");
                        sb.append(tLRPC$Document2.dc_id);
                        sb.append("&size=");
                        sb.append(tLRPC$Document2.size);
                        sb.append("&mime=");
                        sb.append(URLEncoder.encode(tLRPC$Document2.mime_type, "UTF-8"));
                        sb.append("&rid=");
                        sb.append(tLRPC$StoryItem.fileReference);
                        sb.append("&name=");
                        sb.append(URLEncoder.encode(FileLoader.getDocumentFileName(tLRPC$Document2), "UTF-8"));
                        sb.append("&reference=");
                        byte[] bArr = tLRPC$Document2.file_reference;
                        if (bArr == null) {
                            bArr = new byte[0];
                        }
                        sb.append(Utilities.bytesToHex(bArr));
                        String sb2 = sb.toString();
                        this.uriesToPrepare.add(Uri.parse("tg://" + FileLoader.getAttachFileName(tLRPC$Document2) + sb2));
                        this.documentsToPrepare.add(tLRPC$Document2);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        this.delegate.preparePlayer(this.documentsToPrepare, this.uriesToPrepare);
    }

    private void setStoryImage(TLRPC$StoryItem tLRPC$StoryItem, ImageReceiver imageReceiver, String str) {
        ArrayList<TLRPC$PhotoSize> arrayList;
        TLRPC$Document tLRPC$Document;
        StoriesController.UploadingStory findEditingStory = this.storiesController.findEditingStory(tLRPC$StoryItem);
        if (findEditingStory != null) {
            setStoryImage(findEditingStory, imageReceiver, str);
            return;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$StoryItem.media;
        boolean z = (tLRPC$MessageMedia == null || (tLRPC$Document = tLRPC$MessageMedia.document) == null || !MessageObject.isVideoDocument(tLRPC$Document)) ? false : true;
        String str2 = tLRPC$StoryItem.attachPath;
        if (str2 != null) {
            if (tLRPC$StoryItem.media == null) {
                z = str2.toLowerCase().endsWith(".mp4");
            }
            if (z) {
                ImageLocation forPath = ImageLocation.getForPath(tLRPC$StoryItem.attachPath);
                imageReceiver.setImage(forPath, str + "_pframe", ImageLocation.getForPath(tLRPC$StoryItem.firstFramePath), str, null, null, null, 0L, null, null, 0);
                return;
            }
            imageReceiver.setImage(ImageLocation.getForPath(tLRPC$StoryItem.attachPath), str, null, null, null, 0L, null, null, 0);
        } else if (z) {
            TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$StoryItem.media.document.thumbs, 1000);
            ImageLocation forDocument = ImageLocation.getForDocument(tLRPC$StoryItem.media.document);
            imageReceiver.setImage(forDocument, str + "_pframe", ImageLocation.getForDocument(closestPhotoSizeWithSize, tLRPC$StoryItem.media.document), str, null, null, null, 0L, null, tLRPC$StoryItem, 0);
        } else {
            TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$StoryItem.media;
            TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia2 != null ? tLRPC$MessageMedia2.photo : null;
            if (tLRPC$Photo != null && (arrayList = tLRPC$Photo.sizes) != null) {
                TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(arrayList, ConnectionsManager.DEFAULT_DATACENTER_ID);
                FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, 800);
                imageReceiver.setImage(null, null, ImageLocation.getForPhoto(closestPhotoSizeWithSize2, tLRPC$Photo), str, null, null, null, 0L, null, tLRPC$StoryItem, 0);
                return;
            }
            imageReceiver.clearImage();
        }
    }

    private void setStoryImage(StoriesController.UploadingStory uploadingStory, ImageReceiver imageReceiver, String str) {
        if (uploadingStory.isVideo) {
            imageReceiver.setImage(null, null, ImageLocation.getForPath(uploadingStory.firstFramePath), str, null, null, null, 0L, null, null, 0);
        } else {
            imageReceiver.setImage(ImageLocation.getForPath(uploadingStory.path), str, null, null, null, 0L, null, null, 0);
        }
    }

    private void cancelWaiting() {
        Runnable runnable = this.cancellableViews;
        if (runnable != null) {
            runnable.run();
            this.cancellableViews = null;
        }
        this.showViewsProgress = false;
        if (this.isActive) {
            this.delegate.setIsWaiting(false);
        }
    }

    private void updateUserViews() {
        int i;
        String str;
        StoryItemHolder storyItemHolder = this.currentStory;
        TLRPC$StoryItem tLRPC$StoryItem = storyItemHolder.storyItem;
        if (tLRPC$StoryItem == null) {
            tLRPC$StoryItem = storyItemHolder.editingSourceItem;
        }
        if (tLRPC$StoryItem != null) {
            TLRPC$TL_storyViews tLRPC$TL_storyViews = tLRPC$StoryItem.views;
            if (tLRPC$TL_storyViews != null && tLRPC$TL_storyViews.views_count > 0) {
                int i2 = 0;
                for (int i3 = 0; i3 < tLRPC$StoryItem.views.recent_viewers.size(); i3++) {
                    TLObject userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(tLRPC$StoryItem.views.recent_viewers.get(i3).longValue());
                    if (userOrChat != null) {
                        this.selfAvatarsView.setObject(i2, this.currentAccount, userOrChat);
                        i2++;
                    }
                    if (i2 >= 3) {
                        break;
                    }
                }
                for (int i4 = i2; i4 < 3; i4++) {
                    this.selfAvatarsView.setObject(i4, this.currentAccount, null);
                }
                this.selfAvatarsView.commitTransition(false);
                this.selfStatusView.setText(LocaleController.formatPluralStringComma("Views", tLRPC$StoryItem.views.views_count));
                if (i2 == 0) {
                    this.selfAvatarsView.setVisibility(8);
                    this.selfStatusView.setTranslationX(AndroidUtilities.dp(16.0f));
                } else {
                    this.selfAvatarsView.setVisibility(0);
                    this.selfStatusView.setTranslationX(AndroidUtilities.dp(13.0f) + AndroidUtilities.dp(24.0f) + (AndroidUtilities.dp(20.0f) * (i2 - 1)) + AndroidUtilities.dp(10.0f));
                }
                this.selfAvatarsContainer.setVisibility(0);
                return;
            }
            TextView textView = this.selfStatusView;
            if (this.storyViewer.storiesList == null) {
                i = R.string.NobodyViews;
                str = "NobodyViews";
            } else {
                i = R.string.NobodyViewsArchived;
                str = "NobodyViewsArchived";
            }
            textView.setText(LocaleController.getString(str, i));
            this.selfStatusView.setTranslationX(AndroidUtilities.dp(16.0f));
            this.selfAvatarsView.setVisibility(8);
            this.selfAvatarsContainer.setVisibility(8);
            return;
        }
        this.selfStatusView.setText("");
        this.selfAvatarsContainer.setVisibility(8);
        this.selfAvatarsView.setVisibility(8);
    }

    private void requestVideoPlayer(long j) {
        TLRPC$Document tLRPC$Document;
        Uri uri;
        TLRPC$Document tLRPC$Document2;
        if (this.isActive) {
            if (this.currentStory.isVideo()) {
                if (this.currentStory.getLocalPath() != null && new File(this.currentStory.getLocalPath()).exists()) {
                    Uri fromFile = Uri.fromFile(new File(this.currentStory.getLocalPath()));
                    this.videoDuration = 0L;
                    uri = fromFile;
                    tLRPC$Document = null;
                } else {
                    TLRPC$StoryItem tLRPC$StoryItem = this.currentStory.storyItem;
                    if (tLRPC$StoryItem != null) {
                        tLRPC$StoryItem.dialogId = this.dialogId;
                        try {
                            tLRPC$Document2 = tLRPC$StoryItem.media.document;
                            try {
                                if (tLRPC$StoryItem.fileReference == 0) {
                                    tLRPC$StoryItem.fileReference = FileLoader.getInstance(this.currentAccount).getFileReference(this.currentStory.storyItem);
                                }
                                StringBuilder sb = new StringBuilder();
                                sb.append("?account=");
                                sb.append(this.currentAccount);
                                sb.append("&id=");
                                sb.append(tLRPC$Document2.id);
                                sb.append("&hash=");
                                sb.append(tLRPC$Document2.access_hash);
                                sb.append("&dc=");
                                sb.append(tLRPC$Document2.dc_id);
                                sb.append("&size=");
                                sb.append(tLRPC$Document2.size);
                                sb.append("&mime=");
                                sb.append(URLEncoder.encode(tLRPC$Document2.mime_type, "UTF-8"));
                                sb.append("&rid=");
                                sb.append(this.currentStory.storyItem.fileReference);
                                sb.append("&name=");
                                sb.append(URLEncoder.encode(FileLoader.getDocumentFileName(tLRPC$Document2), "UTF-8"));
                                sb.append("&reference=");
                                byte[] bArr = tLRPC$Document2.file_reference;
                                if (bArr == null) {
                                    bArr = new byte[0];
                                }
                                sb.append(Utilities.bytesToHex(bArr));
                                Uri parse = Uri.parse("tg://" + FileLoader.getAttachFileName(tLRPC$Document2) + sb.toString());
                                this.videoDuration = (long) (MessageObject.getDocumentDuration(tLRPC$Document2) * 1000.0d);
                                uri = parse;
                            } catch (Exception unused) {
                                uri = null;
                                tLRPC$Document = tLRPC$Document2;
                                this.delegate.requestPlayer(tLRPC$Document, uri, j, this.playerSharedScope);
                                this.storyContainer.invalidate();
                                return;
                            }
                        } catch (Exception unused2) {
                            tLRPC$Document2 = null;
                        }
                        tLRPC$Document = tLRPC$Document2;
                    } else {
                        tLRPC$Document = null;
                        uri = null;
                    }
                }
                this.delegate.requestPlayer(tLRPC$Document, uri, j, this.playerSharedScope);
                this.storyContainer.invalidate();
                return;
            }
            this.delegate.requestPlayer(null, null, 0L, this.playerSharedScope);
            VideoPlayerSharedScope videoPlayerSharedScope = this.playerSharedScope;
            videoPlayerSharedScope.renderView = null;
            videoPlayerSharedScope.firstFrameRendered = false;
            return;
        }
        this.playerSharedScope.renderView = null;
    }

    public boolean switchToNext(boolean z) {
        if (this.storyViewer.reversed) {
            z = !z;
        }
        if (z) {
            if (this.selectedPosition < getStoriesCount() - 1) {
                this.selectedPosition++;
                updatePosition();
                return true;
            }
            return false;
        }
        int i = this.selectedPosition;
        if (i > 0) {
            this.selectedPosition = i - 1;
            updatePosition();
            return true;
        }
        return false;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public void createBlurredBitmap(Canvas canvas, Bitmap bitmap) {
        TextureView textureView;
        VideoPlayerSharedScope videoPlayerSharedScope = this.playerSharedScope;
        View view = videoPlayerSharedScope.renderView;
        if (view != null && videoPlayerSharedScope.surfaceView != null) {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            if (Build.VERSION.SDK_INT >= 24) {
                AndroidUtilities.getBitmapFromSurface(this.playerSharedScope.surfaceView, createBitmap);
            }
            if (createBitmap != null) {
                canvas.drawBitmap(createBitmap, 0.0f, 0.0f, (Paint) null);
            }
        } else if (view != null && (textureView = videoPlayerSharedScope.textureView) != null) {
            Bitmap bitmap2 = textureView.getBitmap(bitmap.getWidth(), bitmap.getHeight());
            if (bitmap2 != null) {
                canvas.drawBitmap(bitmap2, 0.0f, 0.0f, (Paint) null);
            }
        } else {
            canvas.save();
            canvas.scale(bitmap.getWidth() / this.storyContainer.getMeasuredWidth(), bitmap.getHeight() / this.storyContainer.getMeasuredHeight());
            this.imageReceiver.draw(canvas);
            canvas.restore();
        }
        if (AndroidUtilities.computePerceivedBrightness(AndroidUtilities.getDominantColor(bitmap)) < 0.15f) {
            canvas.drawColor(ColorUtils.setAlphaComponent(-1, 102));
        }
        Utilities.blurBitmap(bitmap, 3, 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
        Utilities.blurBitmap(bitmap, 3, 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
    }

    public void stopPlaying(boolean z) {
        if (z) {
            this.imageReceiver.stopAnimation();
            this.imageReceiver.setAllowStartAnimation(false);
            return;
        }
        this.imageReceiver.startAnimation();
        this.imageReceiver.setAllowStartAnimation(true);
    }

    public long getCurrentPeer() {
        return this.dialogId;
    }

    public ArrayList<Integer> getCurrentDay() {
        return this.day;
    }

    public void setPaused(boolean z) {
        if (this.paused != z) {
            this.paused = z;
            stopPlaying(z);
            this.lastDrawTime = 0L;
            this.storyContainer.invalidate();
        }
    }

    public int getSelectedPosition() {
        return this.selectedPosition;
    }

    public boolean closeKeyboardOrEmoji() {
        HintView2 hintView2 = this.privacyHint;
        if (hintView2 != null) {
            hintView2.hide();
        }
        HintView2 hintView22 = this.soundTooltip;
        if (hintView22 != null) {
            hintView22.hide();
        }
        HintView hintView = this.mediaBanTooltip;
        if (hintView != null) {
            hintView.hide(true);
        }
        CaptionContainerView captionContainerView = this.storyEditCaptionView;
        if (captionContainerView == null || !captionContainerView.onBackPressed()) {
            CustomPopupMenu customPopupMenu = this.popupMenu;
            if (customPopupMenu != null && customPopupMenu.isShowing()) {
                this.popupMenu.dismiss();
                return true;
            } else if (checkRecordLocked(false)) {
                return true;
            } else {
                ReactionsContainerLayout reactionsContainerLayout = this.reactionsContainerLayout;
                if (reactionsContainerLayout != null && reactionsContainerLayout.getReactionsWindow() != null && this.reactionsContainerLayout.getReactionsWindow().isShowing()) {
                    this.reactionsContainerLayout.getReactionsWindow().dismiss();
                    return true;
                }
                ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
                if (chatActivityEnterView != null && chatActivityEnterView.isPopupShowing()) {
                    if (this.realKeyboardHeight > 0) {
                        AndroidUtilities.hideKeyboard(this.chatActivityEnterView.getEmojiView());
                    } else {
                        this.chatActivityEnterView.hidePopup(true, false);
                    }
                    return true;
                } else if (getKeyboardHeight() >= AndroidUtilities.dp(20.0f)) {
                    AndroidUtilities.hideKeyboard(this.chatActivityEnterView);
                    return true;
                } else if (this.storyCaptionView.getVisibility() != 0 || this.storyCaptionView.getProgressToBlackout() <= 0.0f) {
                    return false;
                } else {
                    this.storyCaptionView.collapse();
                    this.inBlackoutMode = false;
                    this.storyContainer.invalidate();
                    return true;
                }
            }
        }
        return true;
    }

    public boolean findClickableView(ViewGroup viewGroup, float f, float f2, boolean z) {
        ChatActivityEnterView chatActivityEnterView;
        if (viewGroup == null) {
            return false;
        }
        HintView2 hintView2 = this.privacyHint;
        if (hintView2 == null || !hintView2.shown()) {
            HintView2 hintView22 = this.soundTooltip;
            if (hintView22 == null || !hintView22.shown()) {
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View childAt = viewGroup.getChildAt(i);
                    if (childAt.getVisibility() == 0) {
                        if (childAt == this.storyCaptionView) {
                            Rect rect = AndroidUtilities.rectTmp2;
                            childAt.getHitRect(rect);
                            if (rect.contains((int) f, (int) f2) && this.storyCaptionView.allowInterceptTouchEvent(f, f2 - childAt.getTop())) {
                                return true;
                            }
                        }
                        Rect rect2 = AndroidUtilities.rectTmp2;
                        childAt.getHitRect(rect2);
                        if (this.keyboardVisible && childAt == this.chatActivityEnterView && f2 > rect2.top) {
                            return true;
                        }
                        if (!z && rect2.contains((int) f, (int) f2) && (((childAt.isClickable() || childAt == this.reactionsContainerLayout) && childAt.isEnabled()) || ((chatActivityEnterView = this.chatActivityEnterView) != null && childAt == chatActivityEnterView.getRecordCircle()))) {
                            return true;
                        }
                        if (childAt.isEnabled() && (childAt instanceof ViewGroup) && findClickableView((ViewGroup) childAt, f - childAt.getX(), f2 - childAt.getY(), z)) {
                            return true;
                        }
                    }
                }
                return false;
            }
            return true;
        }
        return true;
    }

    public void setAccount(int i) {
        this.currentAccount = i;
        this.storiesController = MessagesController.getInstance(i).storiesController;
        ReactionsContainerLayout reactionsContainerLayout = this.reactionsContainerLayout;
        if (reactionsContainerLayout != null) {
            reactionsContainerLayout.setCurrentAccount(i);
            this.reactionsContainerLayout.setMessage(null, null);
        }
    }

    public void setActive(boolean z) {
        setActive(0L, z);
    }

    public void setActive(long j, boolean z) {
        if (this.isActive != z) {
            activeCount += z ? 1 : -1;
            this.isActive = z;
            if (z) {
                if (useSurfaceInViewPagerWorkAround()) {
                    this.delegate.setIsSwiping(true);
                    AndroidUtilities.cancelRunOnUIThread(this.allowDrawSurfaceRunnable);
                    AndroidUtilities.runOnUIThread(this.allowDrawSurfaceRunnable, 100L);
                }
                requestVideoPlayer(j);
                updatePreloadImages();
                this.muteIconView.setAnimation(this.sharedResources.muteDrawable);
                this.isActive = true;
            } else {
                this.muteIconView.clearAnimationDrawable();
                this.viewsThumbImageReceiver = null;
                this.isLongPressed = false;
                this.progressToHideInterface.set(0.0f, true);
                this.storyContainer.invalidate();
                invalidate();
                cancelWaiting();
                this.delegate.setIsRecording(false);
            }
            this.imageReceiver.setFileLoadingPriority(this.isActive ? 3 : 2);
            this.leftPreloadImageReceiver.setFileLoadingPriority(this.isActive ? 2 : 0);
            this.rightPreloadImageReceiver.setFileLoadingPriority(this.isActive ? 2 : 0);
            if (this.isSelf) {
                this.storiesController.pollViewsForSelfStories(this.isActive);
            }
        }
    }

    public void progressToDismissUpdated() {
        if (this.BIG_SCREEN) {
            invalidate();
        }
    }

    public void reset() {
        this.headerView.backupImageView.getImageReceiver().setVisible(true, true);
        if (this.changeBoundAnimator != null) {
            this.chatActivityEnterView.reset();
            this.chatActivityEnterView.setAlpha(1.0f);
        }
        ReactionsContainerLayout reactionsContainerLayout = this.reactionsContainerLayout;
        if (reactionsContainerLayout != null) {
            reactionsContainerLayout.reset();
        }
        InstantCameraView instantCameraView = this.instantCameraView;
        if (instantCameraView != null) {
            AndroidUtilities.removeFromParent(instantCameraView);
            this.instantCameraView.hideCamera(true);
            this.instantCameraView = null;
        }
        setActive(false);
        setIsVisible(false);
        this.isLongPressed = false;
        this.progressToHideInterface.set(0.0f, false);
        this.viewsThumbImageReceiver = null;
        this.messageSent = false;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1) {
            if (i == 0 || i == 2) {
                createChatAttachView();
                ChatAttachAlert chatAttachAlert = this.chatAttachAlert;
                if (chatAttachAlert != null) {
                    chatAttachAlert.getPhotoLayout().onActivityResultFragment(i, intent, null);
                }
            } else if (i == 21) {
                if (intent == null) {
                    showAttachmentError();
                    return;
                }
                if (intent.getData() != null) {
                    sendUriAsDocument(intent.getData());
                } else if (intent.getClipData() != null) {
                    ClipData clipData = intent.getClipData();
                    for (int i3 = 0; i3 < clipData.getItemCount(); i3++) {
                        sendUriAsDocument(clipData.getItemAt(i3).getUri());
                    }
                } else {
                    showAttachmentError();
                }
                ChatAttachAlert chatAttachAlert2 = this.chatAttachAlert;
                if (chatAttachAlert2 != null) {
                    chatAttachAlert2.dismiss();
                }
                afterMessageSend();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0081  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void sendUriAsDocument(Uri uri) {
        TLRPC$StoryItem tLRPC$StoryItem;
        Uri parse;
        String str;
        if (uri == null || (tLRPC$StoryItem = this.currentStory.storyItem) == null || (tLRPC$StoryItem instanceof TLRPC$TL_storyItemSkipped)) {
            return;
        }
        String uri2 = uri.toString();
        boolean z = true;
        if (uri2.contains("com.google.android.apps.photos.contentprovider")) {
            try {
                String str2 = uri2.split("/1/")[1];
                int indexOf = str2.indexOf("/ACTUAL");
                parse = indexOf != -1 ? Uri.parse(URLDecoder.decode(str2.substring(0, indexOf), "UTF-8")) : uri;
            } catch (Exception e) {
                FileLog.e(e);
            }
            String path = AndroidUtilities.getPath(parse);
            if (BuildVars.NO_SCOPED_STORAGE) {
                str = path;
            } else {
                if (path == null) {
                    String uri3 = parse.toString();
                    String copyFileToCache = MediaController.copyFileToCache(parse, "file");
                    if (copyFileToCache == null) {
                        showAttachmentError();
                        return;
                    } else {
                        str = uri3;
                        path = copyFileToCache;
                    }
                } else {
                    str = path;
                }
                z = false;
            }
            if (!z) {
                SendMessagesHelper.prepareSendingDocument(getAccountInstance(), null, null, parse, null, null, this.dialogId, null, null, tLRPC$StoryItem, null, true, 0, null);
                return;
            } else {
                SendMessagesHelper.prepareSendingDocument(getAccountInstance(), path, str, null, null, null, this.dialogId, null, null, tLRPC$StoryItem, null, true, 0, null);
                return;
            }
        }
        parse = uri;
        String path2 = AndroidUtilities.getPath(parse);
        if (BuildVars.NO_SCOPED_STORAGE) {
        }
        if (!z) {
        }
    }

    private void showAttachmentError() {
        BulletinFactory.of(this.storyContainer, this.resourcesProvider).createErrorBulletin(LocaleController.getString("UnsupportedAttachment", R.string.UnsupportedAttachment), this.resourcesProvider).show();
    }

    public void setLongpressed(boolean z) {
        if (this.isActive) {
            this.isLongPressed = z;
            invalidate();
        }
    }

    public void showKeyboard() {
        this.chatActivityEnterView.getEditField().requestFocus();
        AndroidUtilities.showKeyboard(this.chatActivityEnterView.getEditField());
    }

    public void checkPinchToZoom(MotionEvent motionEvent) {
        this.pinchToZoomHelper.checkPinchToZoom(motionEvent, this.storyContainer, null, null);
    }

    public void setIsVisible(boolean z) {
        if (this.isVisible == z) {
            return;
        }
        this.isVisible = z;
        if (z) {
            this.imageReceiver.setCurrentAlpha(1.0f);
        }
    }

    public ArrayList<TLRPC$StoryItem> getStoryItems() {
        return this.storyItems;
    }

    public void selectPosition(int i) {
        if (this.selectedPosition != i) {
            this.selectedPosition = i;
            updatePosition();
        }
    }

    public void cancelTouch() {
        this.storyCaptionView.cancelTouch();
    }

    public void onActionDown(MotionEvent motionEvent) {
        HintView2 hintView2 = this.privacyHint;
        if (hintView2 != null && hintView2.shown() && this.privacyButton != null && !this.privacyHint.containsTouch(motionEvent, getX() + this.storyContainer.getX() + this.privacyHint.getX(), getY() + this.storyContainer.getY() + this.privacyHint.getY()) && !hitButton(this.privacyButton, motionEvent)) {
            this.privacyHint.hide();
        }
        HintView2 hintView22 = this.soundTooltip;
        if (hintView22 == null || !hintView22.shown() || this.muteIconContainer == null || this.soundTooltip.containsTouch(motionEvent, getX() + this.storyContainer.getX() + this.soundTooltip.getX(), getY() + this.storyContainer.getY() + this.soundTooltip.getY()) || hitButton(this.muteIconContainer, motionEvent)) {
            return;
        }
        this.soundTooltip.hide();
    }

    private boolean hitButton(View view, MotionEvent motionEvent) {
        float x = getX() + this.storyContainer.getX() + view.getX();
        float y = getY() + this.storyContainer.getY() + view.getY();
        return motionEvent.getX() >= x && motionEvent.getX() <= x + ((float) view.getWidth()) && motionEvent.getY() >= y && motionEvent.getY() <= y + ((float) view.getHeight());
    }

    public void setOffset(float f) {
        boolean z = f == 0.0f;
        if (this.allowDrawSurface != z) {
            this.allowDrawSurface = z;
            this.storyContainer.invalidate();
            if (this.isActive && useSurfaceInViewPagerWorkAround()) {
                if (z) {
                    AndroidUtilities.cancelRunOnUIThread(this.allowDrawSurfaceRunnable);
                    AndroidUtilities.runOnUIThread(this.allowDrawSurfaceRunnable, 250L);
                    return;
                }
                AndroidUtilities.cancelRunOnUIThread(this.allowDrawSurfaceRunnable);
                this.delegate.setIsSwiping(true);
            }
        }
    }

    public boolean useSurfaceInViewPagerWorkAround() {
        return this.storyViewer.USE_SURFACE_VIEW && Build.VERSION.SDK_INT < 33;
    }

    public void showNoSoundHint() {
        this.muteIconContainer.callOnClick();
    }

    /* loaded from: classes4.dex */
    public static class PeerHeaderView extends FrameLayout {
        public BackupImageView backupImageView;
        private float progressToUploading;
        RadialProgress radialProgress;
        Paint radialProgressPaint;
        StoryItemHolder storyItemHolder;
        private ValueAnimator subtitleAnimator;
        private TextView[] subtitleView;
        public SimpleTextView titleView;
        private boolean uploadedTooFast;
        private boolean uploading;

        public PeerHeaderView(Context context, StoryItemHolder storyItemHolder) {
            super(context);
            this.subtitleView = new TextView[2];
            this.storyItemHolder = storyItemHolder;
            BackupImageView backupImageView = new BackupImageView(context) { // from class: org.telegram.ui.Stories.PeerStoriesView.PeerHeaderView.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.BackupImageView, android.view.View
                public void onDraw(Canvas canvas) {
                    if (this.imageReceiver.getVisible()) {
                        RectF rectF = AndroidUtilities.rectTmp;
                        rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                        PeerHeaderView.this.drawUploadingProgress(canvas, rectF, true, 1.0f);
                    }
                    super.onDraw(canvas);
                }
            };
            this.backupImageView = backupImageView;
            backupImageView.setRoundRadius(AndroidUtilities.dp(16.0f));
            addView(this.backupImageView, LayoutHelper.createFrame(32, 32.0f, 0, 12.0f, 2.0f, 0.0f, 0.0f));
            setClipChildren(false);
            SimpleTextView simpleTextView = new SimpleTextView(context);
            this.titleView = simpleTextView;
            simpleTextView.setTextSize(14);
            this.titleView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.titleView.setMaxLines(1);
            this.titleView.setEllipsizeByGradient(AndroidUtilities.dp(4.0f));
            NotificationCenter.listenEmojiLoading(this.titleView);
            addView(this.titleView, LayoutHelper.createFrame(-2, -2.0f, 0, 54.0f, 0.0f, 86.0f, 0.0f));
            for (int i = 0; i < 2; i++) {
                this.subtitleView[i] = new TextView(context);
                this.subtitleView[i].setTextSize(1, 12.0f);
                this.subtitleView[i].setMaxLines(1);
                this.subtitleView[i].setSingleLine(true);
                this.subtitleView[i].setEllipsize(TextUtils.TruncateAt.END);
                this.subtitleView[i].setTextColor(-1);
                addView(this.subtitleView[i], LayoutHelper.createFrame(-2, -2.0f, 0, 54.0f, 18.0f, 86.0f, 0.0f));
            }
            this.titleView.setTextColor(-1);
        }

        public void setSubtitle(CharSequence charSequence) {
            setSubtitle(charSequence, false);
        }

        public void setSubtitle(CharSequence charSequence, boolean z) {
            ValueAnimator valueAnimator = this.subtitleAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.subtitleAnimator = null;
            }
            if (z) {
                TextView[] textViewArr = this.subtitleView;
                textViewArr[1].setText(textViewArr[0].getText());
                this.subtitleView[1].setVisibility(0);
                this.subtitleView[1].setAlpha(1.0f);
                this.subtitleView[1].setTranslationY(0.0f);
                this.subtitleView[0].setText(charSequence);
                this.subtitleView[0].setVisibility(0);
                this.subtitleView[0].setAlpha(0.0f);
                this.subtitleView[0].setTranslationY(-AndroidUtilities.dp(4.0f));
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.subtitleAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$PeerHeaderView$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        PeerStoriesView.PeerHeaderView.this.lambda$setSubtitle$0(valueAnimator2);
                    }
                });
                this.subtitleAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.PeerHeaderView.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        PeerHeaderView.this.subtitleView[1].setVisibility(8);
                        PeerHeaderView.this.subtitleView[0].setAlpha(1.0f);
                        PeerHeaderView.this.subtitleView[0].setTranslationY(0.0f);
                    }
                });
                this.subtitleAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.subtitleAnimator.setDuration(340L);
                this.subtitleAnimator.start();
                return;
            }
            this.subtitleView[0].setVisibility(0);
            this.subtitleView[0].setAlpha(1.0f);
            this.subtitleView[0].setText(charSequence);
            this.subtitleView[1].setVisibility(8);
            this.subtitleView[1].setAlpha(0.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setSubtitle$0(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.subtitleView[0].setAlpha(floatValue);
            float f = 1.0f - floatValue;
            this.subtitleView[0].setTranslationY((-AndroidUtilities.dp(4.0f)) * f);
            this.subtitleView[1].setAlpha(f);
            this.subtitleView[1].setTranslationY(floatValue * AndroidUtilities.dp(4.0f));
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (isEnabled()) {
                return super.dispatchTouchEvent(motionEvent);
            }
            return false;
        }

        public void drawUploadingProgress(Canvas canvas, RectF rectF, boolean z, float f) {
            float f2;
            boolean z2;
            StoriesController.UploadingStory uploadingStory;
            StoryItemHolder storyItemHolder = this.storyItemHolder;
            if ((storyItemHolder == null || storyItemHolder.uploadingStory == null) && this.progressToUploading == 0.0f) {
                return;
            }
            if (storyItemHolder != null && (uploadingStory = storyItemHolder.uploadingStory) != null) {
                this.progressToUploading = 1.0f;
                f2 = uploadingStory.progress;
                if (!this.uploading) {
                    this.uploading = true;
                }
                z2 = false;
            } else {
                if (this.uploading) {
                    this.uploading = false;
                    this.uploadedTooFast = this.radialProgress.getAnimatedProgress() < 0.2f;
                }
                if (!this.uploadedTooFast) {
                    this.progressToUploading = Utilities.clamp(this.progressToUploading - ((1000.0f / AndroidUtilities.screenRefreshRate) / 300.0f), 1.0f, 0.0f);
                }
                f2 = 1.0f;
                z2 = true;
            }
            if (this.radialProgress == null) {
                RadialProgress radialProgress = new RadialProgress(this.backupImageView);
                this.radialProgress = radialProgress;
                radialProgress.setBackground(null, true, false);
            }
            this.radialProgress.setDiff(0);
            ImageReceiver imageReceiver = this.backupImageView.getImageReceiver();
            float dp = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(6.0f) * (1.0f - this.progressToUploading));
            this.radialProgress.setProgressRect((int) (rectF.left - dp), (int) (rectF.top - dp), (int) (rectF.right + dp), (int) (rectF.bottom + dp));
            this.radialProgress.setProgress(z2 ? 1.0f : Utilities.clamp(f2, 1.0f, 0.0f), true);
            if (this.uploadedTooFast && z2 && this.radialProgress.getAnimatedProgress() >= 0.9f) {
                this.progressToUploading = Utilities.clamp(this.progressToUploading - ((1000.0f / AndroidUtilities.screenRefreshRate) / 300.0f), 1.0f, 0.0f);
            }
            if (z) {
                if (f != 1.0f) {
                    Paint activeCirclePaint = StoriesUtilities.getActiveCirclePaint(imageReceiver, false);
                    activeCirclePaint.setAlpha((int) (this.progressToUploading * 255.0f));
                    this.radialProgress.setPaint(activeCirclePaint);
                    this.radialProgress.draw(canvas);
                }
                if (this.radialProgressPaint == null) {
                    Paint paint = new Paint(1);
                    this.radialProgressPaint = paint;
                    paint.setColor(-1);
                    this.radialProgressPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
                    this.radialProgressPaint.setStyle(Paint.Style.STROKE);
                    this.radialProgressPaint.setStrokeCap(Paint.Cap.ROUND);
                }
                this.radialProgressPaint.setAlpha((int) (255.0f * f * this.progressToUploading));
                this.radialProgress.setPaint(this.radialProgressPaint);
                this.radialProgress.draw(canvas);
            }
        }
    }

    public int getStoriesCount() {
        return this.uploadingStories.size() + Math.max(this.totalStoriesCount, this.storyItems.size());
    }

    /* loaded from: classes4.dex */
    public class StoryItemHolder {
        public TLRPC$StoryItem editingSourceItem;
        private boolean isVideo;
        boolean skipped;
        public TLRPC$StoryItem storyItem = null;
        public StoriesController.UploadingStory uploadingStory = null;

        public StoryItemHolder() {
        }

        void set(TLRPC$StoryItem tLRPC$StoryItem) {
            this.storyItem = tLRPC$StoryItem;
            this.uploadingStory = null;
            this.skipped = tLRPC$StoryItem instanceof TLRPC$TL_storyItemSkipped;
            this.isVideo = isVideoInternal();
        }

        private boolean isVideoInternal() {
            String str;
            TLRPC$MessageMedia tLRPC$MessageMedia;
            TLRPC$Document tLRPC$Document;
            StoriesController.UploadingStory uploadingStory = this.uploadingStory;
            if (uploadingStory != null) {
                return uploadingStory.isVideo;
            }
            TLRPC$StoryItem tLRPC$StoryItem = this.storyItem;
            if (tLRPC$StoryItem != null && (tLRPC$MessageMedia = tLRPC$StoryItem.media) != null && (tLRPC$Document = tLRPC$MessageMedia.document) != null) {
                return tLRPC$Document != null && MessageObject.isVideoDocument(tLRPC$Document);
            } else if (tLRPC$StoryItem == null || tLRPC$StoryItem.media != null || (str = tLRPC$StoryItem.attachPath) == null) {
                return false;
            } else {
                return str.toLowerCase().endsWith(".mp4");
            }
        }

        void set(StoriesController.UploadingStory uploadingStory) {
            this.uploadingStory = uploadingStory;
            this.storyItem = null;
            this.skipped = false;
            this.isVideo = isVideoInternal();
        }

        public void clear() {
            this.uploadingStory = null;
            this.storyItem = null;
        }

        void cancelOrDelete() {
            TLRPC$StoryItem tLRPC$StoryItem = this.storyItem;
            if (tLRPC$StoryItem != null) {
                PeerStoriesView.this.storiesController.deleteStory(tLRPC$StoryItem);
                return;
            }
            StoriesController.UploadingStory uploadingStory = this.uploadingStory;
            if (uploadingStory != null) {
                uploadingStory.cancel();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x0053, code lost:
            if (r0 <= r1.storiesController.dialogIdToMaxReadId.get(r1.dialogId, 0)) goto L32;
         */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0066  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x0081  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void checkSendView() {
            TLRPC$StoryItem tLRPC$StoryItem;
            TLRPC$UserFull userFull;
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            TLRPC$TL_userStories tLRPC$TL_userStories = peerStoriesView.userStories;
            if (tLRPC$TL_userStories == null && (tLRPC$TL_userStories = peerStoriesView.storiesController.getStories(peerStoriesView.dialogId)) == null && (userFull = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUserFull(PeerStoriesView.this.dialogId)) != null) {
                tLRPC$TL_userStories = userFull.stories;
            }
            if (!PeerStoriesView.this.isActive || (tLRPC$StoryItem = this.storyItem) == null || tLRPC$TL_userStories == null) {
                return;
            }
            if (!StoriesUtilities.hasExpiredViews(tLRPC$StoryItem)) {
                int i = this.storyItem.id;
                if (i <= tLRPC$TL_userStories.max_read_id) {
                    PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                }
                if (PeerStoriesView.this.storyViewer.overrideUserStories == null) {
                    PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                    if (peerStoriesView3.storiesController.markStoryAsRead(peerStoriesView3.storyViewer.overrideUserStories, this.storyItem, true)) {
                        PeerStoriesView.this.storyViewer.unreadStateChanged = true;
                        return;
                    }
                    return;
                }
                PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                if (peerStoriesView4.storiesController.markStoryAsRead(peerStoriesView4.dialogId, this.storyItem)) {
                    PeerStoriesView.this.storyViewer.unreadStateChanged = true;
                    return;
                }
                return;
            }
            if (!PeerStoriesView.this.isSelf) {
                return;
            }
            if (PeerStoriesView.this.storyViewer.overrideUserStories == null) {
            }
        }

        public String getLocalPath() {
            TLRPC$StoryItem tLRPC$StoryItem = this.storyItem;
            if (tLRPC$StoryItem != null) {
                return tLRPC$StoryItem.attachPath;
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isVideo() {
            return this.isVideo;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean hasSound() {
            TLRPC$MessageMedia tLRPC$MessageMedia;
            if (this.isVideo) {
                TLRPC$StoryItem tLRPC$StoryItem = this.storyItem;
                if (tLRPC$StoryItem != null && (tLRPC$MessageMedia = tLRPC$StoryItem.media) != null && tLRPC$MessageMedia.document != null) {
                    for (int i = 0; i < this.storyItem.media.document.attributes.size(); i++) {
                        TLRPC$DocumentAttribute tLRPC$DocumentAttribute = this.storyItem.media.document.attributes.get(i);
                        if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) && tLRPC$DocumentAttribute.nosound) {
                            return false;
                        }
                    }
                    return true;
                }
                StoriesController.UploadingStory uploadingStory = this.uploadingStory;
                if (uploadingStory != null) {
                    return !uploadingStory.entry.muted;
                }
                return true;
            }
            return false;
        }

        public String createLink() {
            return String.format(Locale.US, "https://t.me/%s/s/%s", UserObject.getPublicUsername(MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId))), Integer.valueOf(PeerStoriesView.this.currentStory.storyItem.id));
        }

        public File getPath() {
            TLRPC$Photo tLRPC$Photo;
            if (getLocalPath() != null) {
                return new File(getLocalPath());
            }
            TLRPC$StoryItem tLRPC$StoryItem = this.storyItem;
            if (tLRPC$StoryItem != null) {
                TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$StoryItem.media;
                if (tLRPC$MessageMedia == null || tLRPC$MessageMedia.document == null) {
                    if (tLRPC$MessageMedia == null || (tLRPC$Photo = tLRPC$MessageMedia.photo) == null) {
                        return null;
                    }
                    TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, ConnectionsManager.DEFAULT_DATACENTER_ID);
                    File pathToAttach = FileLoader.getInstance(PeerStoriesView.this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, true);
                    return !pathToAttach.exists() ? FileLoader.getInstance(PeerStoriesView.this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, false) : pathToAttach;
                }
                return FileLoader.getInstance(PeerStoriesView.this.currentAccount).getPathToAttach(this.storyItem.media.document);
            }
            return null;
        }

        public boolean allowScreenshots() {
            StoriesController.UploadingStory uploadingStory = this.uploadingStory;
            if (uploadingStory != null) {
                return uploadingStory.entry.allowScreenshots;
            }
            TLRPC$StoryItem tLRPC$StoryItem = this.storyItem;
            if (tLRPC$StoryItem != null) {
                return !tLRPC$StoryItem.noforwards;
            }
            return true;
        }
    }

    public static int getStoryId(TLRPC$StoryItem tLRPC$StoryItem, StoriesController.UploadingStory uploadingStory) {
        StoryEntry storyEntry;
        if (tLRPC$StoryItem != null) {
            return tLRPC$StoryItem.id;
        }
        if (uploadingStory == null || (storyEntry = uploadingStory.entry) == null) {
            return 0;
        }
        return storyEntry.editStoryId;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int size;
        ReactionsContainerLayout reactionsContainerLayout;
        MentionsContainerView mentionsContainerView;
        if (this.storyViewer.ATTACH_TO_FRAGMENT) {
            ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight;
        }
        if (this.isActive && this.shareAlert == null) {
            this.realKeyboardHeight = this.delegate.getKeyboardHeight();
        } else {
            this.realKeyboardHeight = 0;
        }
        if (this.storyViewer.ATTACH_TO_FRAGMENT) {
            size = View.MeasureSpec.getSize(i2);
        } else {
            size = View.MeasureSpec.getSize(i2) + this.realKeyboardHeight;
        }
        int size2 = (int) ((View.MeasureSpec.getSize(i) * 16.0f) / 9.0f);
        if (size <= size2 || size2 > size) {
            size2 = size;
        }
        if (this.realKeyboardHeight < AndroidUtilities.dp(20.0f)) {
            this.realKeyboardHeight = 0;
        }
        int i3 = this.realKeyboardHeight;
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null && (chatActivityEnterView.isPopupShowing() || this.chatActivityEnterView.isWaitingForKeyboard())) {
            if (this.chatActivityEnterView.getEmojiView().getMeasuredHeight() == 0) {
                i3 = this.chatActivityEnterView.getEmojiPadding();
            } else if (this.chatActivityEnterView.isStickersExpanded()) {
                this.chatActivityEnterView.checkStickresExpandHeight();
                i3 = this.chatActivityEnterView.getStickersExpandedHeight();
            } else {
                i3 = this.chatActivityEnterView.getVisibleEmojiPadding();
            }
        }
        boolean z = this.keyboardVisible;
        if (this.lastKeyboardHeight != i3) {
            this.keyboardVisible = false;
            if (i3 > 0 && this.isActive) {
                this.keyboardVisible = true;
                this.messageSent = false;
                this.lastOpenedKeyboardHeight = i3;
                checkReactionsLayout();
                ReactionsEffectOverlay.dismissAll();
            }
            if (this.keyboardVisible && (mentionsContainerView = this.mentionContainer) != null) {
                mentionsContainerView.setVisibility(0);
            }
            if (!this.keyboardVisible && (reactionsContainerLayout = this.reactionsContainerLayout) != null) {
                reactionsContainerLayout.reset();
            }
            this.headerView.setEnabled(!this.keyboardVisible);
            this.optionsIconView.setEnabled(!this.keyboardVisible);
            ChatActivityEnterView chatActivityEnterView2 = this.chatActivityEnterView;
            if (chatActivityEnterView2 != null) {
                chatActivityEnterView2.checkReactionsButton(!this.keyboardVisible);
            }
            if (this.isActive && this.keyboardVisible) {
                this.delegate.setKeyboardVisible(true);
            }
            this.lastKeyboardHeight = i3;
            ValueAnimator valueAnimator = this.keyboardAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            this.notificationsLocker.lock();
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.animatingKeyboardHeight, i3);
            this.keyboardAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    PeerStoriesView.this.lambda$onMeasure$13(valueAnimator2);
                }
            });
            this.keyboardAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.23
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    PeerStoriesView.this.notificationsLocker.unlock();
                    PeerStoriesView peerStoriesView = PeerStoriesView.this;
                    peerStoriesView.animatingKeyboardHeight = peerStoriesView.lastKeyboardHeight;
                    ChatActivityEnterView chatActivityEnterView3 = PeerStoriesView.this.chatActivityEnterView;
                    if (chatActivityEnterView3 != null) {
                        chatActivityEnterView3.onOverrideAnimationEnd();
                    }
                    PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                    if (peerStoriesView2.isActive && !peerStoriesView2.keyboardVisible) {
                        peerStoriesView2.delegate.setKeyboardVisible(false);
                    }
                    PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                    if (!peerStoriesView3.keyboardVisible && peerStoriesView3.mentionContainer != null) {
                        PeerStoriesView.this.mentionContainer.setVisibility(8);
                    }
                    PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                    peerStoriesView4.forceUpdateOffsets = true;
                    peerStoriesView4.invalidate();
                }
            });
            if (this.keyboardVisible) {
                this.keyboardAnimator.setDuration(250L);
                this.keyboardAnimator.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                this.storyViewer.cancelSwipeToReply();
            } else {
                this.keyboardAnimator.setDuration(500L);
                this.keyboardAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            }
            this.keyboardAnimator.start();
            boolean z2 = this.keyboardVisible;
            if (z2 != z) {
                if (z2) {
                    createBlurredBitmap(this.bitmapShaderTools.getCanvas(), this.bitmapShaderTools.getBitmap());
                } else {
                    ChatActivityEnterView chatActivityEnterView3 = this.chatActivityEnterView;
                    if (chatActivityEnterView3 != null) {
                        chatActivityEnterView3.getEditField().clearFocus();
                    }
                }
                this.animateKeyboardOpening = true;
            } else {
                this.animateKeyboardOpening = false;
            }
        }
        ChatActivityEnterView chatActivityEnterView4 = this.chatActivityEnterView;
        if (chatActivityEnterView4 != null && chatActivityEnterView4.getEmojiView() != null) {
            ((FrameLayout.LayoutParams) this.chatActivityEnterView.getEmojiView().getLayoutParams()).gravity = 80;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.storyContainer.getLayoutParams();
        layoutParams.height = size2;
        boolean z3 = size - size2 > AndroidUtilities.dp(64.0f);
        this.BIG_SCREEN = z3;
        int dp = (size - ((z3 ? AndroidUtilities.dp(64.0f) : 0) + size2)) >> 1;
        layoutParams.topMargin = dp;
        if (this.BIG_SCREEN) {
            this.enterViewBottomOffset = (((-dp) + size) - size2) - AndroidUtilities.dp(64.0f);
        } else {
            this.enterViewBottomOffset = ((-dp) + size) - size2;
        }
        FrameLayout frameLayout = this.selfView;
        if (frameLayout != null) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
            if (this.BIG_SCREEN) {
                layoutParams2.topMargin = dp + size2 + AndroidUtilities.dp(8.0f);
            } else {
                layoutParams2.topMargin = (dp + size2) - AndroidUtilities.dp(48.0f);
            }
        }
        TextView textView = this.replyDisabledTextView;
        if (textView != null) {
            FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) textView.getLayoutParams();
            if (this.BIG_SCREEN) {
                layoutParams3.topMargin = dp + size2 + AndroidUtilities.dp(8.0f) + AndroidUtilities.dp(24.0f);
            } else {
                layoutParams3.topMargin = ((dp + size2) - AndroidUtilities.dp(48.0f)) + AndroidUtilities.dp(24.0f);
            }
        }
        InstantCameraView instantCameraView = this.instantCameraView;
        if (instantCameraView != null) {
            FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) instantCameraView.getLayoutParams();
            if (i3 == 0) {
                layoutParams4.bottomMargin = size - ((dp + size2) - AndroidUtilities.dp(64.0f));
            } else {
                layoutParams4.bottomMargin = i3 + AndroidUtilities.dp(64.0f);
            }
        }
        if (!this.BIG_SCREEN) {
            ((FrameLayout.LayoutParams) this.shareButton.getLayoutParams()).topMargin = ((dp + size2) - AndroidUtilities.dp(12.0f)) - AndroidUtilities.dp(40.0f);
            int dp2 = this.isSelf ? AndroidUtilities.dp(40.0f) : AndroidUtilities.dp(64.0f);
            ((FrameLayout.LayoutParams) this.storyCaptionView.getLayoutParams()).bottomMargin = dp2;
            this.storyCaptionView.blackoutBottomOffset = dp2;
        } else {
            ((FrameLayout.LayoutParams) this.shareButton.getLayoutParams()).topMargin = dp + size2 + AndroidUtilities.dp(12.0f);
            ((FrameLayout.LayoutParams) this.storyCaptionView.getLayoutParams()).bottomMargin = AndroidUtilities.dp(8.0f);
            this.storyCaptionView.blackoutBottomOffset = AndroidUtilities.dp(8.0f);
        }
        this.forceUpdateOffsets = true;
        float dp3 = AndroidUtilities.dp(8.0f) + AndroidUtilities.dp(40.0f);
        if (this.privacyButton.getVisibility() == 0) {
            dp3 += AndroidUtilities.dp(60.0f);
        }
        if (this.muteIconContainer.getVisibility() == 0) {
            dp3 += AndroidUtilities.dp(40.0f);
        }
        FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) this.headerView.titleView.getLayoutParams();
        if (layoutParams5.rightMargin != dp3) {
            int i4 = (int) dp3;
            layoutParams5.rightMargin = i4;
            ((FrameLayout.LayoutParams) this.headerView.subtitleView[0].getLayoutParams()).rightMargin = i4;
            ((FrameLayout.LayoutParams) this.headerView.subtitleView[1].getLayoutParams()).rightMargin = i4;
            this.headerView.forceLayout();
        }
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMeasure$13(ValueAnimator valueAnimator) {
        this.animatingKeyboardHeight = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.progressToKeyboard = -1.0f;
        this.forceUpdateOffsets = true;
        invalidate();
    }

    private void updateViewOffsets() {
        float f;
        float progressToDismiss = this.delegate.getProgressToDismiss();
        this.progressToHideInterface.set(this.isLongPressed ? 1.0f : 0.0f);
        int i = this.lastOpenedKeyboardHeight;
        if (i != 0 && this.animateKeyboardOpening) {
            f = MathUtils.clamp(this.animatingKeyboardHeight / i, 0.0f, 1.0f);
        } else {
            f = this.keyboardVisible ? 1.0f : 0.0f;
        }
        float f2 = this.progressToRecording.get();
        float f3 = this.progressToTextA.get();
        float f4 = this.progressToStickerExpanded.get();
        this.progressToRecording.set(this.isRecording ? 1.0f : 0.0f);
        if (!this.messageSent) {
            AnimatedFloat animatedFloat = this.progressToTextA;
            ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
            animatedFloat.set((chatActivityEnterView == null || TextUtils.isEmpty(chatActivityEnterView.getFieldText())) ? 0.0f : 1.0f);
        }
        AnimatedFloat animatedFloat2 = this.progressToStickerExpanded;
        ChatActivityEnterView chatActivityEnterView2 = this.chatActivityEnterView;
        animatedFloat2.set((chatActivityEnterView2 == null || !chatActivityEnterView2.isStickersExpanded()) ? 0.0f : 1.0f);
        ChatActivityEnterView chatActivityEnterView3 = this.chatActivityEnterView;
        if (chatActivityEnterView3 != null) {
            chatActivityEnterView3.checkAnimation();
        }
        ChatActivityEnterView chatActivityEnterView4 = this.chatActivityEnterView;
        boolean z = chatActivityEnterView4 != null && chatActivityEnterView4.isPopupShowing();
        if (!this.forceUpdateOffsets && this.progressToReply == this.storyViewer.swipeToReplyProgress && this.progressToHideInterface.get() == this.prevToHideProgress && this.lastAnimatingKeyboardHeight == this.animatingKeyboardHeight && f == this.progressToKeyboard && progressToDismiss == this.progressToDismiss && f2 == this.progressToRecording.get() && !z && f4 == this.progressToStickerExpanded.get() && f3 == this.progressToTextA.get()) {
            return;
        }
        this.forceUpdateOffsets = false;
        this.lastAnimatingKeyboardHeight = this.animatingKeyboardHeight;
        if (this.progressToHideInterface.get() != this.prevToHideProgress) {
            this.storyContainer.invalidate();
        }
        this.prevToHideProgress = this.progressToHideInterface.get();
        this.progressToDismiss = progressToDismiss;
        this.progressToKeyboard = f;
        this.progressToReply = this.storyViewer.swipeToReplyProgress;
        ReactionsContainerLayout reactionsContainerLayout = this.reactionsContainerLayout;
        if (reactionsContainerLayout != null) {
            reactionsContainerLayout.setVisibility(f > 0.0f ? 0 : 8);
        }
        float hideInterfaceAlpha = getHideInterfaceAlpha();
        if (this.BIG_SCREEN) {
            this.inputBackgroundPaint.setColor(ColorUtils.blendARGB(ColorUtils.blendARGB(-16777216, -1, 0.13f), ColorUtils.setAlphaComponent(-16777216, 170), this.progressToKeyboard));
            Paint paint = this.inputBackgroundPaint;
            paint.setAlpha((int) (paint.getAlpha() * (1.0f - this.progressToDismiss) * hideInterfaceAlpha));
        } else {
            this.inputBackgroundPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (140.0f * hideInterfaceAlpha)));
        }
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childAt = getChildAt(i2);
            if (childAt.getVisibility() != 0 || childAt == this.selfView || childAt.getTag(R.id.parent_tag) != null) {
                if (childAt == this.selfView) {
                    if (this.BIG_SCREEN) {
                        childAt.setAlpha((1.0f - this.progressToDismiss) * hideInterfaceAlpha * (1.0f - this.outT));
                    } else {
                        childAt.setAlpha((1.0f - this.outT) * hideInterfaceAlpha);
                    }
                }
            } else {
                ChatActivityEnterView chatActivityEnterView5 = this.chatActivityEnterView;
                if (chatActivityEnterView5 != null && childAt == chatActivityEnterView5.getEmojiView()) {
                    childAt.setTranslationY(this.chatActivityEnterView.getEmojiView().getMeasuredHeight() - this.animatingKeyboardHeight);
                } else if (childAt instanceof HintView) {
                    ((HintView) childAt).updatePosition();
                } else if (childAt != this.instantCameraView && childAt != this.storyContainer && childAt != this.shareButton && childAt != this.mediaBanTooltip) {
                    float dp = ((((-this.enterViewBottomOffset) * (1.0f - this.progressToKeyboard)) - this.animatingKeyboardHeight) - (AndroidUtilities.dp(8.0f) * (1.0f - this.progressToKeyboard))) - (AndroidUtilities.dp(20.0f) * this.storyViewer.swipeToReplyProgress);
                    float f5 = this.BIG_SCREEN ? (1.0f - this.progressToDismiss) * hideInterfaceAlpha : hideInterfaceAlpha * 1.0f;
                    if (childAt == this.mentionContainer) {
                        dp -= this.chatActivityEnterView.getMeasuredHeight() - this.chatActivityEnterView.getAnimatedTop();
                        f5 = this.progressToKeyboard;
                        childAt.invalidate();
                    }
                    if (childAt == this.reactionsContainerLayout) {
                        float f6 = this.progressToKeyboard * (1.0f - this.progressToRecording.get()) * (1.0f - f4) * (1.0f - this.progressToTextA.get());
                        childAt.setAlpha(f5 * f6 * 1.0f);
                        float f7 = (f6 * 0.2f) + 0.8f;
                        childAt.setScaleX(f7);
                        childAt.setScaleY(f7);
                    } else {
                        childAt.setTranslationY(dp);
                        childAt.setAlpha(f5);
                    }
                }
            }
        }
        this.shareButton.setAlpha((1.0f - progressToDismiss) * hideInterfaceAlpha);
        for (int i3 = 0; i3 < this.storyContainer.getChildCount(); i3++) {
            View childAt2 = this.storyContainer.getChildAt(i3);
            if (childAt2 != null) {
                if (childAt2 == this.headerView || childAt2 == this.optionsIconView || childAt2 == this.muteIconContainer || childAt2 == this.selfView || childAt2 == this.storyCaptionView || childAt2 == this.privacyButton) {
                    float f8 = childAt2 == this.muteIconContainer ? this.muteIconViewAlpha : 1.0f;
                    if (childAt2 == this.storyCaptionView) {
                        childAt2.setAlpha(f8 * (hideCaptionWithInterface() ? hideInterfaceAlpha : 1.0f) * (1.0f - this.outT));
                    } else {
                        childAt2.setAlpha(f8 * hideInterfaceAlpha * (1.0f - this.outT));
                    }
                } else {
                    childAt2.setAlpha(hideInterfaceAlpha);
                }
            }
        }
        ChatActivityEnterView chatActivityEnterView6 = this.chatActivityEnterView;
        if (chatActivityEnterView6 != null) {
            chatActivityEnterView6.setHorizontalPadding(AndroidUtilities.dp(10.0f), this.progressToKeyboard, this.allowShare);
            if (this.chatActivityEnterView.getEmojiView() != null) {
                this.chatActivityEnterView.getEmojiView().setAlpha(this.progressToKeyboard);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getHideInterfaceAlpha() {
        return (1.0f - this.progressToHideInterface.get()) * (1.0f - this.storyViewer.getProgressToSelfViews());
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        if (view == this.mentionContainer) {
            canvas.save();
            canvas.clipRect(0.0f, this.mentionContainer.getY(), getMeasuredWidth(), this.mentionContainer.getY() + this.mentionContainer.getMeasuredHeight());
            boolean drawChild = super.drawChild(canvas, view, j);
            canvas.restore();
            return drawChild;
        }
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (view == chatActivityEnterView) {
            this.sharedResources.rect1.set(0.0f, this.chatActivityEnterView.getY() + this.chatActivityEnterView.getAnimatedTop(), getMeasuredWidth() + AndroidUtilities.dp(20.0f), getMeasuredHeight());
            this.sharedResources.rect2.set(AndroidUtilities.dp(10.0f), (this.chatActivityEnterView.getBottom() - AndroidUtilities.dp(48.0f)) + this.chatActivityEnterView.getTranslationY() + AndroidUtilities.dp(2.0f), (getMeasuredWidth() - AndroidUtilities.dp(10.0f)) - (this.allowShare ? AndroidUtilities.dp(46.0f) : 0), (this.chatActivityEnterView.getY() + this.chatActivityEnterView.getMeasuredHeight()) - AndroidUtilities.dp(2.0f));
            if (this.chatActivityEnterView.getMeasuredHeight() > AndroidUtilities.dp(50.0f)) {
                this.chatActivityEnterView.getEditField().setTranslationY((1.0f - this.progressToKeyboard) * (this.chatActivityEnterView.getMeasuredHeight() - AndroidUtilities.dp(50.0f)));
            } else {
                this.chatActivityEnterView.getEditField().setTranslationY(0.0f);
            }
            float dp = (AndroidUtilities.dp(50.0f) / 2.0f) * (1.0f - this.progressToKeyboard);
            this.bitmapShaderTools.setBounds(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            AndroidUtilities.lerp(this.sharedResources.rect2, this.sharedResources.rect1, this.progressToKeyboard, this.sharedResources.finalRect);
            float f = this.progressToKeyboard;
            if (f > 0.0f) {
                this.bitmapShaderTools.paint.setAlpha((int) (f * 255.0f));
                canvas.drawRoundRect(this.sharedResources.finalRect, dp, dp, this.bitmapShaderTools.paint);
            }
            canvas.drawRoundRect(this.sharedResources.finalRect, dp, dp, this.inputBackgroundPaint);
            if (this.progressToKeyboard < 0.5f) {
                canvas.save();
                canvas.clipRect(this.sharedResources.finalRect);
                boolean drawChild2 = super.drawChild(canvas, view, j);
                canvas.restore();
                return drawChild2;
            }
        } else if (chatActivityEnterView != null && chatActivityEnterView.isPopupView(view)) {
            canvas.save();
            canvas.clipRect(this.sharedResources.finalRect);
            boolean drawChild3 = super.drawChild(canvas, view, j);
            canvas.restore();
            return drawChild3;
        } else {
            ReactionsContainerLayout reactionsContainerLayout = this.reactionsContainerLayout;
            if (view == reactionsContainerLayout && this.chatActivityEnterView != null) {
                view.setTranslationY(((-reactionsContainerLayout.getMeasuredHeight()) + (this.chatActivityEnterView.getY() + this.chatActivityEnterView.getAnimatedTop())) - AndroidUtilities.dp(18.0f));
                if (this.progressToKeyboard > 0.0f) {
                    this.sharedResources.dimPaint.setAlpha((int) (this.progressToKeyboard * 125.0f));
                    canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), this.chatActivityEnterView.getY() + this.chatActivityEnterView.getAnimatedTop(), this.sharedResources.dimPaint);
                }
            }
        }
        return super.drawChild(canvas, view, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkInstantCameraView() {
        if (this.instantCameraView != null) {
            return;
        }
        InstantCameraView instantCameraView = new InstantCameraView(getContext(), new InstantCameraView.Delegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.24
            @Override // org.telegram.ui.Components.InstantCameraView.Delegate
            public /* synthetic */ boolean isInScheduleMode() {
                return InstantCameraView.Delegate.-CC.$default$isInScheduleMode(this);
            }

            @Override // org.telegram.ui.Components.InstantCameraView.Delegate
            public /* synthetic */ boolean isSecretChat() {
                return InstantCameraView.Delegate.-CC.$default$isSecretChat(this);
            }

            @Override // org.telegram.ui.Components.InstantCameraView.Delegate
            public View getFragmentView() {
                return PeerStoriesView.this;
            }

            @Override // org.telegram.ui.Components.InstantCameraView.Delegate
            public void sendMedia(MediaController.PhotoEntry photoEntry, VideoEditedInfo videoEditedInfo, boolean z, int i, boolean z2) {
                if (photoEntry == null) {
                    return;
                }
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                TLRPC$StoryItem tLRPC$StoryItem = peerStoriesView.currentStory.storyItem;
                if (tLRPC$StoryItem == null || (tLRPC$StoryItem instanceof TLRPC$TL_storyItemSkipped)) {
                    return;
                }
                tLRPC$StoryItem.dialogId = peerStoriesView.dialogId;
                if (photoEntry.isVideo) {
                    if (videoEditedInfo != null) {
                        SendMessagesHelper.prepareSendingVideo(PeerStoriesView.this.getAccountInstance(), photoEntry.path, videoEditedInfo, PeerStoriesView.this.dialogId, null, null, tLRPC$StoryItem, photoEntry.entities, photoEntry.ttl, null, z, i, z2, photoEntry.hasSpoiler, photoEntry.caption);
                    } else {
                        SendMessagesHelper.prepareSendingVideo(PeerStoriesView.this.getAccountInstance(), photoEntry.path, null, PeerStoriesView.this.dialogId, null, null, tLRPC$StoryItem, photoEntry.entities, photoEntry.ttl, null, z, i, z2, photoEntry.hasSpoiler, photoEntry.caption);
                    }
                } else if (photoEntry.imagePath != null) {
                    SendMessagesHelper.prepareSendingPhoto(PeerStoriesView.this.getAccountInstance(), photoEntry.imagePath, photoEntry.thumbPath, null, PeerStoriesView.this.dialogId, null, null, tLRPC$StoryItem, photoEntry.entities, photoEntry.stickers, null, photoEntry.ttl, null, videoEditedInfo, z, i, z2, photoEntry.caption);
                } else if (photoEntry.path != null) {
                    SendMessagesHelper.prepareSendingPhoto(PeerStoriesView.this.getAccountInstance(), photoEntry.path, photoEntry.thumbPath, null, PeerStoriesView.this.dialogId, null, null, tLRPC$StoryItem, photoEntry.entities, photoEntry.stickers, null, photoEntry.ttl, null, videoEditedInfo, z, i, z2, photoEntry.caption);
                }
                PeerStoriesView.this.afterMessageSend();
            }

            @Override // org.telegram.ui.Components.InstantCameraView.Delegate
            public Activity getParentActivity() {
                return AndroidUtilities.findActivity(PeerStoriesView.this.getContext());
            }

            @Override // org.telegram.ui.Components.InstantCameraView.Delegate
            public int getClassGuid() {
                return PeerStoriesView.this.classGuid;
            }

            @Override // org.telegram.ui.Components.InstantCameraView.Delegate
            public long getDialogId() {
                return PeerStoriesView.this.dialogId;
            }
        }, this.resourcesProvider);
        this.instantCameraView = instantCameraView;
        instantCameraView.drawBlur = false;
        addView(this.instantCameraView, indexOfChild(this.chatActivityEnterView.getRecordCircle()), LayoutHelper.createFrame(-1, -1, 51));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void afterMessageSend() {
        InstantCameraView instantCameraView = this.instantCameraView;
        if (instantCameraView != null) {
            instantCameraView.resetCameraFile();
            this.instantCameraView.cancel(false);
        }
        this.messageSent = true;
        this.storyViewer.closeKeyboardOrEmoji();
        BulletinFactory of = BulletinFactory.of(this.storyContainer, this.resourcesProvider);
        if (of != null) {
            of.createSimpleBulletin(R.raw.forward, LocaleController.getString("MessageSent", R.string.MessageSent), LocaleController.getString("ViewInChat", R.string.ViewInChat), 5000, new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.this.openChat();
                }
            }).hideAfterBottomSheet(false).show(false);
        }
        MessagesController.getInstance(this.currentAccount).ensureMessagesLoaded(this.dialogId, 0, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openChat() {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", this.dialogId);
        TLRPC$Dialog dialog = MessagesController.getInstance(this.currentAccount).getDialog(this.dialogId);
        if (dialog != null) {
            bundle.putInt("message_id", dialog.top_message);
        }
        this.storyViewer.presentFragment(new ChatActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AccountInstance getAccountInstance() {
        return AccountInstance.getInstance(this.currentAccount);
    }

    /* loaded from: classes4.dex */
    public static class VideoPlayerSharedScope {
        boolean firstFrameRendered;
        StoryViewer.VideoPlayerHolder player;
        View renderView;
        SurfaceView surfaceView;
        TextureView textureView;
        ArrayList<View> viewsToInvalidate = new ArrayList<>();

        public void invalidate() {
            for (int i = 0; i < this.viewsToInvalidate.size(); i++) {
                this.viewsToInvalidate.get(i).invalidate();
            }
        }

        public boolean isBuffering() {
            StoryViewer.VideoPlayerHolder videoPlayerHolder = this.player;
            return videoPlayerHolder != null && videoPlayerHolder.isBuffering();
        }
    }

    void checkReactionsLayout() {
        if (this.reactionsContainerLayout == null) {
            ReactionsContainerLayout reactionsContainerLayout = new ReactionsContainerLayout(1, LaunchActivity.getLastFragment(), getContext(), this.currentAccount, new WrappedResourceProvider(this, this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.25
                @Override // org.telegram.ui.WrappedResourceProvider
                public void appendColors() {
                    this.sparseIntArray.put(Theme.key_chat_emojiPanelBackground, ColorUtils.setAlphaComponent(-1, 30));
                }
            });
            this.reactionsContainerLayout = reactionsContainerLayout;
            reactionsContainerLayout.skipEnterAnimation = true;
            addView(reactionsContainerLayout, this.reactionsContainerIndex, LayoutHelper.createFrame(-2, 52.0f, 49, 0.0f, 0.0f, 0.0f, 64.0f));
            this.reactionsContainerLayout.setDelegate(new 26());
            this.reactionsContainerLayout.setMessage(null, null);
        }
        this.reactionsContainerLayout.setFragment(LaunchActivity.getLastFragment());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 26 implements ReactionsContainerLayout.ReactionsContainerDelegate {
        26() {
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public void onReactionClicked(View view, ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2) {
            ReactionsEffectOverlay reactionsEffectOverlay;
            TLRPC$Document findDocument;
            if (z && visibleReaction.emojicon != null) {
                PeerStoriesView.this.performHapticFeedback(0);
                Context context = view.getContext();
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                reactionsEffectOverlay = new ReactionsEffectOverlay(context, null, peerStoriesView.reactionsContainerLayout, null, view, peerStoriesView.getMeasuredWidth() / 2.0f, PeerStoriesView.this.getMeasuredHeight() / 2.0f, visibleReaction, PeerStoriesView.this.currentAccount, 0, true);
            } else {
                Context context2 = view.getContext();
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                reactionsEffectOverlay = new ReactionsEffectOverlay(context2, null, peerStoriesView2.reactionsContainerLayout, null, view, peerStoriesView2.getMeasuredWidth() / 2.0f, PeerStoriesView.this.getMeasuredHeight() / 2.0f, visibleReaction, PeerStoriesView.this.currentAccount, 2, true);
            }
            ReactionsEffectOverlay.currentOverlay = reactionsEffectOverlay;
            reactionsEffectOverlay.windowView.setTag(R.id.parent_tag, 1);
            PeerStoriesView.this.addView(reactionsEffectOverlay.windowView);
            reactionsEffectOverlay.started = true;
            reactionsEffectOverlay.startTime = System.currentTimeMillis();
            if (visibleReaction.emojicon != null) {
                findDocument = MediaDataController.getInstance(PeerStoriesView.this.currentAccount).getEmojiAnimatedSticker(visibleReaction.emojicon);
                SendMessagesHelper.SendMessageParams of = SendMessagesHelper.SendMessageParams.of(visibleReaction.emojicon, PeerStoriesView.this.dialogId);
                PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                of.replyToStoryItem = peerStoriesView3.currentStory.storyItem;
                SendMessagesHelper.getInstance(peerStoriesView3.currentAccount).sendMessage(of);
            } else {
                findDocument = AnimatedEmojiDrawable.findDocument(PeerStoriesView.this.currentAccount, visibleReaction.documentId);
                String findAnimatedEmojiEmoticon = MessageObject.findAnimatedEmojiEmoticon(findDocument, null);
                SendMessagesHelper.SendMessageParams of2 = SendMessagesHelper.SendMessageParams.of(findAnimatedEmojiEmoticon, PeerStoriesView.this.dialogId);
                of2.entities = new ArrayList<>();
                TLRPC$TL_messageEntityCustomEmoji tLRPC$TL_messageEntityCustomEmoji = new TLRPC$TL_messageEntityCustomEmoji();
                tLRPC$TL_messageEntityCustomEmoji.document_id = visibleReaction.documentId;
                tLRPC$TL_messageEntityCustomEmoji.offset = 0;
                tLRPC$TL_messageEntityCustomEmoji.length = findAnimatedEmojiEmoticon.length();
                of2.entities.add(tLRPC$TL_messageEntityCustomEmoji);
                PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                of2.replyToStoryItem = peerStoriesView4.currentStory.storyItem;
                SendMessagesHelper.getInstance(peerStoriesView4.currentAccount).sendMessage(of2);
            }
            PeerStoriesView peerStoriesView5 = PeerStoriesView.this;
            BulletinFactory.of(peerStoriesView5.storyContainer, peerStoriesView5.resourcesProvider).createEmojiBulletin(findDocument, LocaleController.getString("ReactionSent", R.string.ReactionSent), LocaleController.getString("ViewInChat", R.string.ViewInChat), new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$26$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.26.this.lambda$onReactionClicked$0();
                }
            }).setDuration(5000).show();
            if (PeerStoriesView.this.reactionsContainerLayout.getReactionsWindow() != null) {
                PeerStoriesView.this.reactionsContainerLayout.getReactionsWindow().dismissWithAlpha();
            }
            PeerStoriesView.this.closeKeyboardOrEmoji();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReactionClicked$0() {
            PeerStoriesView.this.openChat();
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public void drawRoundRect(Canvas canvas, RectF rectF, float f, float f2, float f3) {
            float f4 = -f2;
            float f5 = -f3;
            PeerStoriesView.this.bitmapShaderTools.setBounds(f4, f5, PeerStoriesView.this.getMeasuredWidth() + f4, PeerStoriesView.this.getMeasuredHeight() + f5);
            if (f > 0.0f) {
                canvas.drawRoundRect(rectF, f, f, PeerStoriesView.this.bitmapShaderTools.paint);
                canvas.drawRoundRect(rectF, f, f, PeerStoriesView.this.inputBackgroundPaint);
                return;
            }
            canvas.drawRect(rectF, PeerStoriesView.this.bitmapShaderTools.paint);
            canvas.drawRect(rectF, PeerStoriesView.this.inputBackgroundPaint);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public boolean needEnterText() {
            return PeerStoriesView.this.needEnterText();
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public void onEmojiWindowDismissed() {
            PeerStoriesView.this.delegate.requestAdjust(false);
        }
    }

    public boolean needEnterText() {
        boolean isKeyboardVisible = this.chatActivityEnterView.isKeyboardVisible();
        if (isKeyboardVisible) {
            this.chatActivityEnterView.showEmojiView();
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.this.lambda$needEnterText$14();
            }
        }, 300L);
        return isKeyboardVisible;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$needEnterText$14() {
        this.delegate.requestAdjust(true);
    }

    public void setViewsThumbImageReceiver(float f, ImageReceiver imageReceiver) {
        this.viewsThumbImageReceiver = imageReceiver;
        this.viewsThumbAlpha = f;
    }

    /* loaded from: classes4.dex */
    public static class SharedResources {
        public final Paint barPaint;
        private final Drawable bottomOverlayGradient;
        public Drawable deleteDrawable;
        private final Paint gradientBackgroundPaint;
        public final Drawable imageBackgroundDrawable;
        public RLottieDrawable muteDrawable;
        public RLottieDrawable noSoundDrawable;
        public Drawable optionsDrawable;
        public final Paint selectedBarPaint;
        public Drawable shareDrawable;
        private final Drawable topOverlayGradient;
        public final BitmapShaderTools bitmapShaderTools = new BitmapShaderTools();
        private final RectF rect1 = new RectF();
        private final RectF rect2 = new RectF();
        private final RectF finalRect = new RectF();
        private final Paint dimPaint = new Paint();

        /* JADX INFO: Access modifiers changed from: package-private */
        public SharedResources(Context context) {
            this.shareDrawable = ContextCompat.getDrawable(context, R.drawable.media_share);
            this.optionsDrawable = ContextCompat.getDrawable(context, R.drawable.media_more);
            this.deleteDrawable = ContextCompat.getDrawable(context, R.drawable.msg_delete);
            int i = R.raw.media_mute_unmute;
            this.muteDrawable = new RLottieDrawable(i, "media_mute_unmute", AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
            RLottieDrawable rLottieDrawable = new RLottieDrawable(i, "media_mute_unmute", AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
            this.noSoundDrawable = rLottieDrawable;
            rLottieDrawable.setCurrentFrame(20, false, true);
            this.noSoundDrawable.stop();
            Paint paint = new Paint(1);
            this.barPaint = paint;
            paint.setColor(1442840575);
            Paint paint2 = new Paint(1);
            this.selectedBarPaint = paint2;
            paint2.setColor(-1);
            int alphaComponent = ColorUtils.setAlphaComponent(-16777216, 102);
            this.topOverlayGradient = ContextCompat.getDrawable(context, R.drawable.shadow_story_top);
            this.bottomOverlayGradient = ContextCompat.getDrawable(context, R.drawable.shadow_story_bottom);
            Paint paint3 = new Paint();
            this.gradientBackgroundPaint = paint3;
            paint3.setColor(alphaComponent);
            this.imageBackgroundDrawable = new ColorDrawable(ColorUtils.blendARGB(-16777216, -1, 0.1f));
        }

        public void setIconMuted(boolean z, boolean z2) {
            if (!z2) {
                this.muteDrawable.setCurrentFrame(z ? 20 : 0, false);
                this.muteDrawable.setCustomEndFrame(z ? 20 : 0);
            } else if (z) {
                if (this.muteDrawable.getCurrentFrame() > 20) {
                    this.muteDrawable.setCurrentFrame(0, false);
                }
                this.muteDrawable.setCustomEndFrame(20);
                this.muteDrawable.start();
            } else if (this.muteDrawable.getCurrentFrame() == 0 || this.muteDrawable.getCurrentFrame() >= 43) {
            } else {
                this.muteDrawable.setCustomEndFrame(43);
                this.muteDrawable.start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void editPrivacy(StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy, final TLRPC$StoryItem tLRPC$StoryItem) {
        this.delegate.showDialog(new StoryPrivacyBottomSheet(getContext(), tLRPC$StoryItem.pinned ? ConnectionsManager.DEFAULT_DATACENTER_ID : tLRPC$StoryItem.expire_date - tLRPC$StoryItem.date, this.resourcesProvider).setValue(storyPrivacy).enableSharing(false).allowSmallChats(false).isEdit(true).whenSelectedRules(new Utilities.Callback4() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda18
            @Override // org.telegram.messenger.Utilities.Callback4
            public final void run(Object obj, Object obj2, Object obj3, Object obj4) {
                PeerStoriesView.this.lambda$editPrivacy$17(tLRPC$StoryItem, (StoryPrivacyBottomSheet.StoryPrivacy) obj, (Boolean) obj2, (Boolean) obj3, (Runnable) obj4);
            }
        }, false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$editPrivacy$17(final TLRPC$StoryItem tLRPC$StoryItem, final StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy, Boolean bool, Boolean bool2, final Runnable runnable) {
        TLRPC$TL_stories_editStory tLRPC$TL_stories_editStory = new TLRPC$TL_stories_editStory();
        tLRPC$TL_stories_editStory.id = tLRPC$StoryItem.id;
        tLRPC$TL_stories_editStory.flags |= 4;
        tLRPC$TL_stories_editStory.privacy_rules = storyPrivacy.rules;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_stories_editStory, new RequestDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda20
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PeerStoriesView.this.lambda$editPrivacy$16(runnable, tLRPC$StoryItem, storyPrivacy, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$editPrivacy$16(final Runnable runnable, final TLRPC$StoryItem tLRPC$StoryItem, final StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy, TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.this.lambda$editPrivacy$15(runnable, tLRPC$TL_error, tLRPC$StoryItem, storyPrivacy);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$editPrivacy$15(Runnable runnable, TLRPC$TL_error tLRPC$TL_error, TLRPC$StoryItem tLRPC$StoryItem, StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy) {
        if (runnable != null) {
            runnable.run();
        }
        if (tLRPC$TL_error == null || "STORY_NOT_MODIFIED".equals(tLRPC$TL_error.text)) {
            tLRPC$StoryItem.privacy = storyPrivacy.toValue();
            int i = storyPrivacy.type;
            tLRPC$StoryItem.close_friends = i == 1;
            tLRPC$StoryItem.contacts = i == 2;
            tLRPC$StoryItem.selected_contacts = i == 3;
            MessagesController.getInstance(this.currentAccount).getStoriesController().updateStoryItem(tLRPC$StoryItem.dialogId, tLRPC$StoryItem);
            this.editedPrivacy = true;
            int i2 = storyPrivacy.type;
            if (i2 == 4) {
                BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, LocaleController.getString("StorySharedToEveryone")).show();
            } else if (i2 == 1) {
                BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, LocaleController.getString("StorySharedToCloseFriends")).show();
            } else if (i2 == 2) {
                if (storyPrivacy.selectedUserIds.isEmpty()) {
                    BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, LocaleController.getString("StorySharedToAllContacts")).show();
                } else {
                    BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, LocaleController.formatPluralString("StorySharedToAllContactsExcluded", storyPrivacy.selectedUserIds.size(), new Object[0])).show();
                }
            } else if (i2 == 3) {
                BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, LocaleController.formatPluralString("StorySharedToContacts", storyPrivacy.selectedUserIds.size(), new Object[0])).show();
            }
        } else {
            BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.error, LocaleController.getString("UnknownError", R.string.UnknownError)).show();
        }
        updatePosition();
    }

    public boolean checkRecordLocked(final boolean z) {
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView == null || !chatActivityEnterView.isRecordLocked()) {
            return false;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        if (this.chatActivityEnterView.isInVideoMode()) {
            builder.setTitle(LocaleController.getString("DiscardVideoMessageTitle", R.string.DiscardVideoMessageTitle));
            builder.setMessage(LocaleController.getString("DiscardVideoMessageDescription", R.string.DiscardVideoMessageDescription));
        } else {
            builder.setTitle(LocaleController.getString("DiscardVoiceMessageTitle", R.string.DiscardVoiceMessageTitle));
            builder.setMessage(LocaleController.getString("DiscardVoiceMessageDescription", R.string.DiscardVoiceMessageDescription));
        }
        builder.setPositiveButton(LocaleController.getString("DiscardVoiceMessageAction", R.string.DiscardVoiceMessageAction), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PeerStoriesView.this.lambda$checkRecordLocked$18(z, dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Continue", R.string.Continue), null);
        this.delegate.showDialog(builder.create());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkRecordLocked$18(boolean z, DialogInterface dialogInterface, int i) {
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            if (z) {
                this.storyViewer.close(true);
            } else {
                chatActivityEnterView.cancelRecordingAudioVideo();
            }
        }
    }

    public void animateOut(final boolean z) {
        ValueAnimator valueAnimator = this.outAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        float[] fArr = new float[2];
        fArr[0] = this.outT;
        fArr[1] = z ? 1.0f : 0.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        this.outAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                PeerStoriesView.this.lambda$animateOut$19(valueAnimator2);
            }
        });
        this.outAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.27
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                PeerStoriesView.this.outT = z ? 1.0f : 0.0f;
                PeerStoriesView.this.headerView.setTranslationY((-AndroidUtilities.dp(8.0f)) * PeerStoriesView.this.outT);
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                peerStoriesView.headerView.setAlpha(1.0f - peerStoriesView.outT);
                PeerStoriesView.this.optionsIconView.setTranslationY((-AndroidUtilities.dp(8.0f)) * PeerStoriesView.this.outT);
                PeerStoriesView.this.optionsIconView.setAlpha(1.0f - PeerStoriesView.this.outT);
                PeerStoriesView.this.muteIconContainer.setTranslationY((-AndroidUtilities.dp(8.0f)) * PeerStoriesView.this.outT);
                PeerStoriesView.this.muteIconContainer.setAlpha(PeerStoriesView.this.muteIconViewAlpha * (1.0f - PeerStoriesView.this.outT));
                if (PeerStoriesView.this.selfView != null) {
                    PeerStoriesView.this.selfView.setTranslationY(AndroidUtilities.dp(8.0f) * PeerStoriesView.this.outT);
                    PeerStoriesView.this.selfView.setAlpha(1.0f - PeerStoriesView.this.outT);
                }
                if (PeerStoriesView.this.privacyButton != null) {
                    PeerStoriesView.this.privacyButton.setTranslationY((-AndroidUtilities.dp(8.0f)) * PeerStoriesView.this.outT);
                    PeerStoriesView.this.privacyButton.setAlpha(1.0f - PeerStoriesView.this.outT);
                }
                PeerStoriesView.this.storyCaptionView.setAlpha(1.0f - PeerStoriesView.this.outT);
                PeerStoriesView.this.storyContainer.invalidate();
            }
        });
        this.outAnimator.setDuration(420L);
        this.outAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.outAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateOut$19(ValueAnimator valueAnimator) {
        this.outT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.headerView.setTranslationY((-AndroidUtilities.dp(8.0f)) * this.outT);
        this.headerView.setAlpha(1.0f - this.outT);
        this.optionsIconView.setTranslationY((-AndroidUtilities.dp(8.0f)) * this.outT);
        this.optionsIconView.setAlpha(1.0f - this.outT);
        this.muteIconContainer.setTranslationY((-AndroidUtilities.dp(8.0f)) * this.outT);
        this.muteIconContainer.setAlpha(this.muteIconViewAlpha * (1.0f - this.outT));
        FrameLayout frameLayout = this.selfView;
        if (frameLayout != null) {
            frameLayout.setTranslationY(AndroidUtilities.dp(8.0f) * this.outT);
            this.selfView.setAlpha(1.0f - this.outT);
        }
        StoryPrivacyButton storyPrivacyButton = this.privacyButton;
        if (storyPrivacyButton != null) {
            storyPrivacyButton.setTranslationY((-AndroidUtilities.dp(8.0f)) * this.outT);
            this.privacyButton.setAlpha(1.0f - this.outT);
        }
        this.storyCaptionView.setAlpha(1.0f - this.outT);
        this.storyContainer.invalidate();
    }
}
