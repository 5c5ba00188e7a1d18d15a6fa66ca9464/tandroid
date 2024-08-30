package org.telegram.ui.Stories;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
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
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
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
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.recyclerview.widget.ChatListItemAnimator;
import com.google.android.exoplayer2.util.Consumer;
import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChannelBoostsController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FactCheckController$Key$$ExternalSyntheticBackport0;
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
import org.telegram.messenger.MessagesStorage;
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
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$Reaction;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.tgnet.TLRPC$TL_channels_sendAsPeers;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_messageEntityCustomEmoji;
import org.telegram.tgnet.TLRPC$TL_messageMediaUnsupported;
import org.telegram.tgnet.TLRPC$TL_reactionCustomEmoji;
import org.telegram.tgnet.TLRPC$TL_textWithEntities;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.tl.TL_stories$MediaArea;
import org.telegram.tgnet.tl.TL_stories$PeerStories;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.tgnet.tl.TL_stories$StoryViews;
import org.telegram.tgnet.tl.TL_stories$TL_mediaAreaSuggestedReaction;
import org.telegram.tgnet.tl.TL_stories$TL_premium_boostsStatus;
import org.telegram.tgnet.tl.TL_stories$TL_storiesStealthMode;
import org.telegram.tgnet.tl.TL_stories$TL_stories_editStory;
import org.telegram.tgnet.tl.TL_stories$TL_stories_exportStoryLink;
import org.telegram.tgnet.tl.TL_stories$TL_stories_getStoriesByID;
import org.telegram.tgnet.tl.TL_stories$TL_stories_stories;
import org.telegram.tgnet.tl.TL_stories$TL_storyItemDeleted;
import org.telegram.tgnet.tl.TL_stories$TL_storyItemSkipped;
import org.telegram.tgnet.tl.TL_stories$TL_storyViews;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.AvatarSpan;
import org.telegram.ui.Cells.TextSelectionHelper;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ChooseSpeedLayout;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.AvatarsImageView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BitmapShaderTools;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.ChatAttachAlertDocumentLayout;
import org.telegram.ui.Components.ColoredImageSpan;
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
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgress;
import org.telegram.ui.Components.Reactions.AnimatedEmojiEffect;
import org.telegram.ui.Components.Reactions.ReactionImageHolder;
import org.telegram.ui.Components.Reactions.ReactionsEffectOverlay;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.Reactions.ReactionsUtils;
import org.telegram.ui.Components.ReactionsContainerLayout;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.SpeedIconDrawable;
import org.telegram.ui.Components.TranslateAlert2;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;
import org.telegram.ui.Components.voip.CellFlickerDrawable;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.EmojiAnimationsOverlay;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.MessageStatisticActivity;
import org.telegram.ui.NotificationsCustomSettingsActivity;
import org.telegram.ui.PinchToZoomHelper;
import org.telegram.ui.PremiumPreviewFragment;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stories.DialogStoriesCell;
import org.telegram.ui.Stories.PeerStoriesView;
import org.telegram.ui.Stories.SelfStoriesPreviewView;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.StoryCaptionView;
import org.telegram.ui.Stories.StoryViewer;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.Stories.recorder.CaptionContainerView;
import org.telegram.ui.Stories.recorder.DraftsController;
import org.telegram.ui.Stories.recorder.HintView2;
import org.telegram.ui.Stories.recorder.StoryEntry;
import org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet;
import org.telegram.ui.Stories.recorder.StoryRecorder;
import org.telegram.ui.WrappedResourceProvider;
/* loaded from: classes4.dex */
public abstract class PeerStoriesView extends SizeNotifierFrameLayout implements NotificationCenter.NotificationCenterDelegate {
    public static boolean DISABLE_STORY_REPOSTING = false;
    private static int activeCount;
    private boolean BIG_SCREEN;
    private boolean allowDrawSurface;
    Runnable allowDrawSurfaceRunnable;
    private boolean allowRepost;
    private boolean allowShare;
    private boolean allowShareLink;
    private float alpha;
    boolean animateKeyboardOpening;
    private float animatingKeyboardHeight;
    private boolean attachedToWindow;
    private final AvatarDrawable avatarDrawable;
    private final BitmapShaderTools bitmapShaderTools;
    private TL_stories$TL_premium_boostsStatus boostsStatus;
    private final LinearLayout bottomActionsLinearLayout;
    private ChannelBoostsController.CanApplyBoost canApplyBoost;
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
    ArrayList day;
    Delegate delegate;
    private boolean deletedPeer;
    private long dialogId;
    ArrayList documentsToPrepare;
    private boolean drawAnimatedEmojiAsMovingReaction;
    private boolean drawReactionEffect;
    public boolean editOpened;
    ActionBarMenuSubItem editStoryItem;
    private boolean editedPrivacy;
    private EmojiAnimationsOverlay emojiAnimationsOverlay;
    private AnimatedEmojiEffect emojiReactionEffect;
    private int enterViewBottomOffset;
    private StoryFailView failView;
    private ViewPropertyAnimator failViewAnimator;
    public boolean forceUpdateOffsets;
    PeerHeaderView headerView;
    private boolean imageChanged;
    private final ImageReceiver imageReceiver;
    boolean inBlackoutMode;
    Paint inputBackgroundPaint;
    private InstantCameraView instantCameraView;
    boolean isActive;
    private boolean isCaptionPartVisible;
    boolean isChannel;
    private boolean isEditing;
    private boolean isFailed;
    boolean isGroup;
    private boolean isLongPressed;
    boolean isPremiumBlocked;
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
    private final FrameLayout likeButtonContainer;
    private ReactionsContainerLayout likesReactionLayout;
    private float likesReactionShowProgress;
    private boolean likesReactionShowing;
    private AnimatedFloat linesAlpha;
    private int linesCount;
    private int linesPosition;
    private int listPosition;
    private HintView mediaBanTooltip;
    private MentionsContainerView mentionContainer;
    private boolean messageSent;
    private boolean movingReaction;
    private int movingReactionFromSize;
    private int movingReactionFromX;
    private int movingReactionFromY;
    private float movingReactionProgress;
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
    private final ArrayList preloadReactionHolders;
    private LinearLayout premiumBlockedText;
    private TextView premiumBlockedText1;
    private TextView premiumBlockedText2;
    private float prevToHideProgress;
    private int previousSelectedPotision;
    private final StoryPrivacyButton privacyButton;
    private HintView2 privacyHint;
    float progressToDismiss;
    private AnimatedFloat progressToHideInterface;
    float progressToKeyboard;
    AnimatedFloat progressToRecording;
    float progressToReply;
    AnimatedFloat progressToStickerExpanded;
    AnimatedFloat progressToTextA;
    private ImageReceiver reactionEffectImageReceiver;
    private AnimatedEmojiDrawable reactionMoveDrawable;
    private ImageReceiver reactionMoveImageReceiver;
    private int reactionsContainerIndex;
    ReactionsContainerLayout reactionsContainerLayout;
    private AnimatedTextView.AnimatedTextDrawable reactionsCounter;
    private AnimatedFloat reactionsCounterProgress;
    private boolean reactionsCounterVisible;
    private HintView2 reactionsLongpressTooltip;
    private Runnable reactionsTooltipRunnable;
    private int realKeyboardHeight;
    private TextView replyDisabledTextView;
    private ImageView repostButton;
    private FrameLayout repostButtonContainer;
    private AnimatedTextView.AnimatedTextDrawable repostCounter;
    private AnimatedFloat repostCounterProgress;
    private boolean repostCounterVisible;
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
    private final Runnable showTapToSoundHint;
    boolean showViewsProgress;
    private HintView2 soundTooltip;
    private ActionBarMenuSubItem speedItem;
    private ChooseSpeedLayout speedLayout;
    private boolean stealthModeIsActive;
    StoriesController storiesController;
    private StoriesLikeButton storiesLikeButton;
    private StoryMediaAreasView storyAreasView;
    private final StoryCaptionView storyCaptionView;
    public FrameLayout storyContainer;
    private CaptionContainerView storyEditCaptionView;
    final ArrayList storyItems;
    private final StoryLinesDrawable storyLines;
    private StoryPositionView storyPositionView;
    private final StoryViewer storyViewer;
    private boolean switchEventSent;
    private int totalStoriesCount;
    public boolean unsupported;
    private FrameLayout unsupportedContainer;
    Runnable updateStealthModeTimer;
    final ArrayList uploadingStories;
    ArrayList uriesToPrepare;
    private boolean userCanSeeViews;
    TL_stories$PeerStories userStories;
    public long videoDuration;
    private float viewsThumbAlpha;
    private SelfStoriesPreviewView.ImageHolder viewsThumbImageReceiver;
    private float viewsThumbPivotY;
    private float viewsThumbScale;
    private boolean wasBigScreen;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 17 extends ChatActivityEnterView {
        private int chatActivityEnterViewAnimateFromTop;
        int lastContentViewHeight;
        private Animator messageEditTextAnimator;
        int messageEditTextPredrawHeigth;
        int messageEditTextPredrawScrollY;

        17(Activity activity, SizeNotifierFrameLayout sizeNotifierFrameLayout, ChatActivity chatActivity, boolean z, Theme.ResourcesProvider resourcesProvider) {
            super(activity, sizeNotifierFrameLayout, chatActivity, z, resourcesProvider);
        }

        private void checkRecording() {
            FrameLayout frameLayout;
            boolean z = PeerStoriesView.this.isRecording;
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.isRecording = peerStoriesView.chatActivityEnterView.isRecordingAudioVideo() || PeerStoriesView.this.chatActivityEnterView.seekbarVisible() || ((frameLayout = this.recordedAudioPanel) != null && frameLayout.getVisibility() == 0);
            if (z != PeerStoriesView.this.isRecording) {
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                if (peerStoriesView2.isActive) {
                    peerStoriesView2.delegate.setIsRecording(peerStoriesView2.isRecording);
                }
                invalidate();
                PeerStoriesView.this.storyContainer.invalidate();
            }
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
                PeerStoriesView.this.changeBoundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$17$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        PeerStoriesView.17.this.lambda$checkAnimation$0(valueAnimator);
                    }
                });
                PeerStoriesView.this.changeBoundAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.17.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        PeerStoriesView.this.invalidate();
                        ((ChatActivityEnterView) 17.this).animatedTop = 0;
                        17 r4 = 17.this;
                        PeerStoriesView.this.forceUpdateOffsets = true;
                        if (((ChatActivityEnterView) r4).topView != null && ((ChatActivityEnterView) 17.this).topView.getVisibility() == 0) {
                            ((ChatActivityEnterView) 17.this).topView.setTranslationY(((ChatActivityEnterView) 17.this).animatedTop + ((1.0f - ((ChatActivityEnterView) 17.this).topViewEnterProgress) * ((ChatActivityEnterView) 17.this).topView.getLayoutParams().height));
                            if (((ChatActivityEnterView) 17.this).topLineView != null) {
                                ((ChatActivityEnterView) 17.this).topLineView.setTranslationY(((ChatActivityEnterView) 17.this).animatedTop);
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
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$17$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        PeerStoriesView.17.this.lambda$checkAnimation$1(valueAnimator);
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
            this.lastContentViewHeight = getMeasuredHeight();
        }

        /* JADX WARN: Code restructure failed: missing block: B:25:0x007e, code lost:
            if (r5.this$0.premiumBlockedText2 != null) goto L28;
         */
        @Override // android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (isEnabled()) {
                return super.dispatchTouchEvent(motionEvent);
            }
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(0.0f, 0.0f, getWidth() + (PeerStoriesView.this.premiumBlockedText2 != null ? this.attachLayoutPaddingTranslationX * 1.5f : 0.0f), getHeight());
            boolean contains = rectF.contains(motionEvent.getX(), motionEvent.getY());
            if (motionEvent.getAction() == 0) {
                if (contains && PeerStoriesView.this.premiumBlockedText2 != null) {
                    PeerStoriesView.this.premiumBlockedText2.setPressed(true);
                }
            } else if (motionEvent.getAction() == 1) {
                if (PeerStoriesView.this.premiumBlockedText2 != null) {
                    if (contains && PeerStoriesView.this.premiumBlockedText2.isPressed()) {
                        PeerStoriesView.this.showPremiumBlockedToast();
                    }
                    PeerStoriesView.this.premiumBlockedText2.setPressed(false);
                }
            } else if (motionEvent.getAction() == 3) {
            }
            return PeerStoriesView.this.premiumBlockedText2 != null && PeerStoriesView.this.premiumBlockedText2.isPressed();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        public void extendActionMode(Menu menu) {
            ChatActivity.fillActionModeMenu(menu, null, false);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        protected void isRecordingStateChanged() {
            super.isRecordingStateChanged();
            checkRecording();
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

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        public void setHorizontalPadding(float f, float f2, boolean z) {
            float f3 = (-f) * (1.0f - f2);
            if (PeerStoriesView.this.premiumBlockedText != null) {
                PeerStoriesView.this.premiumBlockedText.setTranslationX(-f3);
            }
            super.setHorizontalPadding(f, f2, z);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        protected boolean showConfirmAlert(Runnable runnable) {
            return PeerStoriesView.this.applyMessageToChat(runnable);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView
        protected void updateRecordInterface(int i, boolean z) {
            super.updateRecordInterface(i, z);
            checkRecording();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 18 implements ChatActivityEnterView.ChatActivityEnterViewDelegate {
        18() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMessageSend$0() {
            PeerStoriesView.this.afterMessageSend();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ void bottomPanelTranslationYChanged(float f) {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$bottomPanelTranslationYChanged(this, f);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ boolean checkCanRemoveRestrictionsByBoosts() {
            return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$checkCanRemoveRestrictionsByBoosts(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void didPressAttachButton() {
            PeerStoriesView.this.openAttachMenu();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ int getContentViewHeight() {
            return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$getContentViewHeight(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public /* synthetic */ ChatActivity.ReplyQuote getReplyQuote() {
            return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$getReplyQuote(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public TL_stories$StoryItem getReplyToStory() {
            return PeerStoriesView.this.currentStory.storyItem;
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
        public void needChangeVideoPreviewState(int i, float f) {
            if (PeerStoriesView.this.instantCameraView != null) {
                PeerStoriesView.this.instantCameraView.changeVideoPreviewState(i, f);
            }
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void needSendTyping() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void needShowMediaBanHint() {
            String str;
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            if (peerStoriesView.isGroup) {
                peerStoriesView.showPremiumBlockedToast();
                return;
            }
            if (peerStoriesView.mediaBanTooltip == null) {
                PeerStoriesView.this.mediaBanTooltip = new HintView(PeerStoriesView.this.getContext(), 9, PeerStoriesView.this.resourcesProvider);
                PeerStoriesView.this.mediaBanTooltip.setVisibility(8);
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                peerStoriesView2.addView(peerStoriesView2.mediaBanTooltip, LayoutHelper.createFrame(-2, -2.0f, 51, 10.0f, 0.0f, 10.0f, 0.0f));
            }
            int i = (PeerStoriesView.this.dialogId > 0L ? 1 : (PeerStoriesView.this.dialogId == 0L ? 0 : -1));
            MessagesController messagesController = MessagesController.getInstance(PeerStoriesView.this.currentAccount);
            long j = PeerStoriesView.this.dialogId;
            if (i >= 0) {
                str = UserObject.getFirstName(messagesController.getUser(Long.valueOf(j)));
            } else {
                TLRPC$Chat chat = messagesController.getChat(Long.valueOf(-j));
                str = chat != null ? chat.title : "";
            }
            PeerStoriesView.this.mediaBanTooltip.setText(AndroidUtilities.replaceTags(LocaleController.formatString(PeerStoriesView.this.chatActivityEnterView.isInVideoMode() ? R.string.VideoMessagesRestrictedByPrivacy : R.string.VoiceMessagesRestrictedByPrivacy, str)));
            PeerStoriesView.this.mediaBanTooltip.showForView(PeerStoriesView.this.chatActivityEnterView.getAudioVideoButtonContainer(), true);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void needStartRecordAudio(int i) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void needStartRecordVideo(int i, boolean z, int i2, int i3, long j) {
            PeerStoriesView.this.checkInstantCameraView();
            if (PeerStoriesView.this.instantCameraView != null) {
                if (i == 0) {
                    PeerStoriesView.this.instantCameraView.showCamera(false);
                } else if (i == 1 || i == 3 || i == 4) {
                    PeerStoriesView.this.instantCameraView.send(i, z, i2, i3, j);
                } else if (i == 2 || i == 5) {
                    PeerStoriesView.this.instantCameraView.cancel(i == 2);
                }
            }
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
        public /* synthetic */ void onKeyboardRequested() {
            ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onKeyboardRequested(this);
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onMessageEditEnd(boolean z) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onMessageSend(CharSequence charSequence, boolean z, int i) {
            if (PeerStoriesView.this.isRecording) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$18$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.18.this.lambda$onMessageSend$0();
                    }
                }, 200L);
            } else {
                PeerStoriesView.this.afterMessageSend();
            }
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onPreAudioVideoRecord() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onSendLongClick() {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onStickersExpandedChange() {
            PeerStoriesView.this.requestLayout();
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onStickersTab(boolean z) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onSwitchRecordMode(boolean z) {
        }

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void onTextChanged(CharSequence charSequence, boolean z, boolean z2) {
            if (PeerStoriesView.this.mentionContainer == null) {
                PeerStoriesView.this.createMentionsContainer();
            }
            if (PeerStoriesView.this.mentionContainer.getAdapter() != null) {
                PeerStoriesView.this.mentionContainer.setDialogId(PeerStoriesView.this.dialogId);
                PeerStoriesView.this.mentionContainer.getAdapter().setUserOrChat(MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId)), null);
                PeerStoriesView.this.mentionContainer.getAdapter().lambda$searchUsernameOrHashtag$7(charSequence, PeerStoriesView.this.chatActivityEnterView.getCursorPosition(), null, false, false);
            }
            PeerStoriesView.this.invalidate();
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
        public boolean onceVoiceAvailable() {
            TLRPC$User user;
            return (PeerStoriesView.this.dialogId < 0 || (user = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId))) == null || UserObject.isUserSelf(user) || user.bot) ? false : true;
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

        @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
        public void toggleVideoRecordingPause() {
            if (PeerStoriesView.this.instantCameraView != null) {
                PeerStoriesView.this.instantCameraView.togglePause();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 35 implements ReactionsContainerLayout.ReactionsContainerDelegate {
        35() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReactionClickedInternal$0(View view, ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2) {
            onReactionClickedInternal(view, visibleReaction, z, z2, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReactionClickedInternal$1() {
            PeerStoriesView.this.openChat();
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public /* synthetic */ boolean drawBackground() {
            return ReactionsContainerLayout.ReactionsContainerDelegate.-CC.$default$drawBackground(this);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public void drawRoundRect(Canvas canvas, RectF rectF, float f, float f2, float f3, int i, boolean z) {
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

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public void onReactionClicked(View view, ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2) {
            onReactionClickedInternal(view, visibleReaction, z, z2, !z);
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x00f8, code lost:
            if (r19.this$0.reactionsContainerLayout.getReactionsWindow() != null) goto L19;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x00fa, code lost:
            r19.this$0.reactionsContainerLayout.getReactionsWindow().dismissWithAlpha();
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x0105, code lost:
            r19.this$0.closeKeyboardOrEmoji();
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:0x010a, code lost:
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:25:0x0178, code lost:
            if (r19.this$0.reactionsContainerLayout.getReactionsWindow() != null) goto L19;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        void onReactionClickedInternal(final View view, final ReactionsLayoutInBubble.VisibleReaction visibleReaction, final boolean z, final boolean z2, boolean z3) {
            ReactionsLayoutInBubble.VisibleReaction visibleReaction2;
            ReactionsEffectOverlay reactionsEffectOverlay;
            TLRPC$Document tLRPC$Document;
            if (z3 && PeerStoriesView.this.applyMessageToChat(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$35$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.35.this.lambda$onReactionClickedInternal$0(view, visibleReaction, z, z2);
                }
            })) {
                return;
            }
            if (!z || visibleReaction.emojicon == null) {
                visibleReaction2 = visibleReaction;
                Context context = view.getContext();
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                reactionsEffectOverlay = new ReactionsEffectOverlay(context, null, peerStoriesView.reactionsContainerLayout, null, view, peerStoriesView.getMeasuredWidth() / 2.0f, PeerStoriesView.this.getMeasuredHeight() / 2.0f, visibleReaction, PeerStoriesView.this.currentAccount, 2, true);
            } else {
                PeerStoriesView.this.performHapticFeedback(0);
                Context context2 = view.getContext();
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                visibleReaction2 = visibleReaction;
                reactionsEffectOverlay = new ReactionsEffectOverlay(context2, null, peerStoriesView2.reactionsContainerLayout, null, view, peerStoriesView2.getMeasuredWidth() / 2.0f, PeerStoriesView.this.getMeasuredHeight() / 2.0f, visibleReaction, PeerStoriesView.this.currentAccount, 0, true);
            }
            ReactionsEffectOverlay.currentOverlay = reactionsEffectOverlay;
            reactionsEffectOverlay.windowView.setTag(R.id.parent_tag, 1);
            PeerStoriesView.this.addView(reactionsEffectOverlay.windowView);
            reactionsEffectOverlay.started = true;
            reactionsEffectOverlay.startTime = System.currentTimeMillis();
            if (visibleReaction2.emojicon != null) {
                tLRPC$Document = MediaDataController.getInstance(PeerStoriesView.this.currentAccount).getEmojiAnimatedSticker(visibleReaction2.emojicon);
                SendMessagesHelper.SendMessageParams of = SendMessagesHelper.SendMessageParams.of(visibleReaction2.emojicon, PeerStoriesView.this.dialogId);
                PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                of.replyToStoryItem = peerStoriesView3.currentStory.storyItem;
                SendMessagesHelper.getInstance(peerStoriesView3.currentAccount).sendMessage(of);
            } else {
                TLRPC$Document findDocument = AnimatedEmojiDrawable.findDocument(PeerStoriesView.this.currentAccount, visibleReaction2.documentId);
                String findAnimatedEmojiEmoticon = MessageObject.findAnimatedEmojiEmoticon(findDocument, null);
                if (findAnimatedEmojiEmoticon != null) {
                    SendMessagesHelper.SendMessageParams of2 = SendMessagesHelper.SendMessageParams.of(findAnimatedEmojiEmoticon, PeerStoriesView.this.dialogId);
                    of2.entities = new ArrayList<>();
                    TLRPC$TL_messageEntityCustomEmoji tLRPC$TL_messageEntityCustomEmoji = new TLRPC$TL_messageEntityCustomEmoji();
                    tLRPC$TL_messageEntityCustomEmoji.document_id = visibleReaction2.documentId;
                    tLRPC$TL_messageEntityCustomEmoji.offset = 0;
                    tLRPC$TL_messageEntityCustomEmoji.length = findAnimatedEmojiEmoticon.length();
                    of2.entities.add(tLRPC$TL_messageEntityCustomEmoji);
                    PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                    of2.replyToStoryItem = peerStoriesView4.currentStory.storyItem;
                    SendMessagesHelper.getInstance(peerStoriesView4.currentAccount).sendMessage(of2);
                    tLRPC$Document = findDocument;
                }
            }
            PeerStoriesView peerStoriesView5 = PeerStoriesView.this;
            BulletinFactory.of(peerStoriesView5.storyContainer, peerStoriesView5.resourcesProvider).createEmojiBulletin(tLRPC$Document, LocaleController.getString(R.string.ReactionSent), LocaleController.getString(R.string.ViewInChat), new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$35$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.35.this.lambda$onReactionClickedInternal$1();
                }
            }).setDuration(5000).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 37 implements ReactionsContainerLayout.ReactionsContainerDelegate {
        37() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReactionClicked$0(ValueAnimator valueAnimator, boolean[] zArr, ValueAnimator valueAnimator2) {
            PeerStoriesView.this.movingReactionProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            PeerStoriesView.this.invalidate();
            if (PeerStoriesView.this.movingReactionProgress <= 0.8f || zArr[0]) {
                return;
            }
            zArr[0] = true;
            PeerStoriesView.this.drawReactionEffect = true;
            PeerStoriesView.this.performHapticFeedback(3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReactionClicked$1(ReactionsLayoutInBubble.VisibleReaction visibleReaction, View view) {
            TLRPC$TL_availableReaction tLRPC$TL_availableReaction;
            PeerStoriesView.this.movingReaction = true;
            final boolean[] zArr = {false};
            final StoriesLikeButton storiesLikeButton = PeerStoriesView.this.storiesLikeButton;
            storiesLikeButton.animate().alpha(0.0f).scaleX(0.8f).scaleY(0.8f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.37.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    AndroidUtilities.removeFromParent(storiesLikeButton);
                }
            }).setDuration(150L).start();
            int dp = AndroidUtilities.dp(8.0f);
            PeerStoriesView.this.storiesLikeButton = new StoriesLikeButton(PeerStoriesView.this.getContext(), PeerStoriesView.this.sharedResources);
            PeerStoriesView.this.storiesLikeButton.setPadding(dp, dp, dp, dp);
            PeerStoriesView.this.likeButtonContainer.addView(PeerStoriesView.this.storiesLikeButton, LayoutHelper.createFrame(40, 40, 3));
            if (PeerStoriesView.this.reactionMoveDrawable != null) {
                PeerStoriesView.this.reactionMoveDrawable.removeView(PeerStoriesView.this);
                PeerStoriesView.this.reactionMoveDrawable = null;
            }
            if (PeerStoriesView.this.emojiReactionEffect != null) {
                PeerStoriesView.this.emojiReactionEffect.removeView(PeerStoriesView.this);
                PeerStoriesView.this.emojiReactionEffect = null;
            }
            PeerStoriesView.this.drawAnimatedEmojiAsMovingReaction = false;
            if (visibleReaction.documentId != 0) {
                PeerStoriesView.this.drawAnimatedEmojiAsMovingReaction = true;
                PeerStoriesView.this.reactionMoveDrawable = new AnimatedEmojiDrawable(2, PeerStoriesView.this.currentAccount, visibleReaction.documentId);
                PeerStoriesView.this.reactionMoveDrawable.addView(PeerStoriesView.this);
            } else if (visibleReaction.emojicon != null && (tLRPC$TL_availableReaction = MediaDataController.getInstance(PeerStoriesView.this.currentAccount).getReactionsMap().get(visibleReaction.emojicon)) != null) {
                PeerStoriesView.this.reactionMoveImageReceiver.setImage(null, null, ImageLocation.getForDocument(tLRPC$TL_availableReaction.select_animation), "60_60", null, null, null, 0L, null, null, 0);
                PeerStoriesView.this.reactionEffectImageReceiver.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.around_animation), ReactionsEffectOverlay.getFilterForAroundAnimation(), null, null, null, 0);
                if (PeerStoriesView.this.reactionEffectImageReceiver.getLottieAnimation() != null) {
                    PeerStoriesView.this.reactionEffectImageReceiver.getLottieAnimation().setCurrentFrame(0, false, true);
                }
            }
            PeerStoriesView.this.storiesLikeButton.setReaction(visibleReaction);
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            if (peerStoriesView.isChannel) {
                TL_stories$StoryItem tL_stories$StoryItem = peerStoriesView.currentStory.storyItem;
                if (tL_stories$StoryItem.sent_reaction == null) {
                    if (tL_stories$StoryItem.views == null) {
                        tL_stories$StoryItem.views = new TL_stories$TL_storyViews();
                    }
                    TL_stories$StoryItem tL_stories$StoryItem2 = PeerStoriesView.this.currentStory.storyItem;
                    TL_stories$StoryViews tL_stories$StoryViews = tL_stories$StoryItem2.views;
                    tL_stories$StoryViews.reactions_count++;
                    ReactionsUtils.applyForStoryViews(null, tL_stories$StoryItem2.sent_reaction, tL_stories$StoryViews);
                    PeerStoriesView.this.updateUserViews(true);
                }
            }
            if (visibleReaction.documentId != 0 && PeerStoriesView.this.storiesLikeButton.emojiDrawable != null) {
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                peerStoriesView2.emojiReactionEffect = AnimatedEmojiEffect.createFrom(peerStoriesView2.storiesLikeButton.emojiDrawable, false, true);
                PeerStoriesView.this.emojiReactionEffect.setView(PeerStoriesView.this);
            }
            PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
            peerStoriesView3.storiesController.setStoryReaction(peerStoriesView3.dialogId, PeerStoriesView.this.currentStory.storyItem, visibleReaction);
            int[] iArr = new int[2];
            view.getLocationInWindow(iArr);
            int[] iArr2 = new int[2];
            PeerStoriesView.this.getLocationInWindow(iArr2);
            PeerStoriesView.this.movingReactionFromX = iArr[0] - iArr2[0];
            PeerStoriesView.this.movingReactionFromY = iArr[1] - iArr2[1];
            PeerStoriesView.this.movingReactionFromSize = view.getMeasuredHeight();
            final ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            PeerStoriesView.this.movingReactionProgress = 0.0f;
            PeerStoriesView.this.invalidate();
            final StoriesLikeButton storiesLikeButton2 = PeerStoriesView.this.storiesLikeButton;
            storiesLikeButton2.setAllowDrawReaction(false);
            storiesLikeButton2.prepareAnimateReaction(visibleReaction);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$37$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    PeerStoriesView.37.this.lambda$onReactionClicked$0(ofFloat, zArr, valueAnimator);
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.37.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    PeerStoriesView.this.movingReaction = false;
                    PeerStoriesView.this.movingReactionProgress = 1.0f;
                    PeerStoriesView.this.invalidate();
                    boolean[] zArr2 = zArr;
                    if (!zArr2[0]) {
                        zArr2[0] = true;
                        PeerStoriesView.this.drawReactionEffect = true;
                        PeerStoriesView.this.performHapticFeedback(3);
                    }
                    storiesLikeButton2.setAllowDrawReaction(true);
                    storiesLikeButton2.animateVisibleReaction();
                    if (PeerStoriesView.this.reactionMoveDrawable != null) {
                        PeerStoriesView.this.reactionMoveDrawable.removeView(PeerStoriesView.this);
                        PeerStoriesView.this.reactionMoveDrawable = null;
                    }
                }
            });
            ofFloat.setDuration(220L);
            ofFloat.start();
            PeerStoriesView.this.showLikesReaction(false);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public /* synthetic */ boolean drawBackground() {
            return ReactionsContainerLayout.ReactionsContainerDelegate.-CC.$default$drawBackground(this);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public /* synthetic */ void drawRoundRect(Canvas canvas, RectF rectF, float f, float f2, float f3, int i, boolean z) {
            ReactionsContainerLayout.ReactionsContainerDelegate.-CC.$default$drawRoundRect(this, canvas, rectF, f, f2, f3, i, z);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public boolean needEnterText() {
            PeerStoriesView.this.delegate.requestAdjust(false);
            return false;
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public /* synthetic */ void onEmojiWindowDismissed() {
            ReactionsContainerLayout.ReactionsContainerDelegate.-CC.$default$onEmojiWindowDismissed(this);
        }

        @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
        public void onReactionClicked(final View view, final ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2) {
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$37$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.37.this.lambda$onReactionClicked$1(visibleReaction, view);
                }
            };
            if (z) {
                runnable.run();
            } else {
                PeerStoriesView.this.applyMessageToChat(runnable);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 4 extends HwFrameLayout {
        boolean drawOverlayed;
        final CellFlickerDrawable loadingDrawable;
        final AnimatedFloat loadingDrawableAlpha;
        final AnimatedFloat loadingDrawableAlpha2;
        final AnimatedFloat progressToAudio;
        final AnimatedFloat progressToFullBlackoutA;
        boolean splitDrawing;
        final /* synthetic */ SharedResources val$sharedResources;
        final /* synthetic */ StoryViewer val$storyViewer;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        4(Context context, SharedResources sharedResources, StoryViewer storyViewer) {
            super(context);
            this.val$sharedResources = sharedResources;
            this.val$storyViewer = storyViewer;
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            this.progressToAudio = new AnimatedFloat(this, 150L, cubicBezierInterpolator);
            this.progressToFullBlackoutA = new AnimatedFloat(this, 150L, cubicBezierInterpolator);
            this.loadingDrawable = new CellFlickerDrawable(32, 102, NotificationCenter.reloadInterface);
            AnimatedFloat animatedFloat = new AnimatedFloat(this);
            this.loadingDrawableAlpha2 = animatedFloat;
            AnimatedFloat animatedFloat2 = new AnimatedFloat(this);
            this.loadingDrawableAlpha = animatedFloat2;
            animatedFloat.setDuration(500L);
            animatedFloat2.setDuration(100L);
        }

        /* JADX WARN: Removed duplicated region for block: B:53:0x0119  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x0125  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x0156  */
        /* JADX WARN: Removed duplicated region for block: B:72:0x0163  */
        /* JADX WARN: Removed duplicated region for block: B:76:0x01c7  */
        /* JADX WARN: Removed duplicated region for block: B:82:0x01de  */
        /* JADX WARN: Removed duplicated region for block: B:94:0x0205 A[ADDED_TO_REGION] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void drawLines(Canvas canvas) {
            float clamp;
            PeerStoriesView peerStoriesView;
            VideoPlayerSharedScope videoPlayerSharedScope;
            float f;
            boolean z;
            StoriesController.StoriesList storiesList;
            StoryItemHolder storyItemHolder;
            StoryViewer storyViewer;
            StoryViewer.VideoPlayerHolder videoPlayerHolder;
            float f2;
            if (PeerStoriesView.this.imageReceiver.hasNotThumb() || (PeerStoriesView.this.currentStory.isVideo && PeerStoriesView.this.playerSharedScope.firstFrameRendered)) {
                PeerStoriesView.this.currentStory.checkSendView();
            }
            float hideInterfaceAlpha = PeerStoriesView.this.getHideInterfaceAlpha();
            if (!PeerStoriesView.this.currentStory.isVideo()) {
                if (!PeerStoriesView.this.paused) {
                    PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                    if (peerStoriesView2.isActive && !peerStoriesView2.isUploading && !PeerStoriesView.this.isEditing && !PeerStoriesView.this.isFailed && PeerStoriesView.this.imageReceiver.hasNotThumb()) {
                        long currentTimeMillis = System.currentTimeMillis();
                        if (PeerStoriesView.this.lastDrawTime != 0 && !PeerStoriesView.this.isCaptionPartVisible) {
                            if (PeerStoriesView.this.currentImageTime <= 0 && currentTimeMillis - PeerStoriesView.this.lastDrawTime > 0 && PeerStoriesView.this.storyAreasView != null) {
                                PeerStoriesView.this.storyAreasView.shine();
                            }
                            PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                            PeerStoriesView.access$2714(peerStoriesView3, currentTimeMillis - peerStoriesView3.lastDrawTime);
                        }
                        PeerStoriesView.this.lastDrawTime = currentTimeMillis;
                        clamp = Utilities.clamp(((float) PeerStoriesView.this.currentImageTime) / 10000.0f, 1.0f, 0.0f);
                    }
                }
                clamp = Utilities.clamp(((float) PeerStoriesView.this.currentImageTime) / 10000.0f, 1.0f, 0.0f);
                float f3 = clamp;
                peerStoriesView = PeerStoriesView.this;
                videoPlayerSharedScope = peerStoriesView.playerSharedScope;
                if (videoPlayerSharedScope != null && (videoPlayerHolder = videoPlayerSharedScope.player) != null) {
                    f2 = videoPlayerHolder.currentSeek;
                    if (f2 >= 0.0f) {
                        f = f2;
                        z = true;
                        if (!peerStoriesView.switchEventSent && f3 == 1.0f && ((!PeerStoriesView.this.currentStory.isVideo || !PeerStoriesView.this.isCaptionPartVisible) && !PeerStoriesView.this.isLongPressed)) {
                            PeerStoriesView.this.switchEventSent = true;
                            post(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$4$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    PeerStoriesView.4.this.lambda$drawLines$0();
                                }
                            });
                        }
                        storiesList = this.val$storyViewer.storiesList;
                        if (storiesList != null && storiesList.type != 3) {
                            if (PeerStoriesView.this.storyPositionView == null) {
                                PeerStoriesView.this.storyPositionView = new StoryPositionView();
                            }
                            PeerStoriesView.this.storyPositionView.draw(canvas, (1.0f - PeerStoriesView.this.outT) * hideInterfaceAlpha * PeerStoriesView.this.alpha, PeerStoriesView.this.listPosition, this.val$storyViewer.storiesList.getCount(), this, PeerStoriesView.this.headerView);
                        }
                        canvas.save();
                        canvas.translate(0.0f, AndroidUtilities.dp(8.0f) - (AndroidUtilities.dp(8.0f) * PeerStoriesView.this.outT));
                        boolean z2 = !PeerStoriesView.this.currentStory.isVideo() && PeerStoriesView.this.playerSharedScope.isBuffering();
                        boolean z3 = (PeerStoriesView.this.isLongPressed || (storyItemHolder = PeerStoriesView.this.currentStory) == null || !storyItemHolder.isVideo || (storyViewer = this.val$storyViewer) == null || !storyViewer.inSeekingMode) ? false : true;
                        AnimatedFloat animatedFloat = PeerStoriesView.this.linesAlpha;
                        if (PeerStoriesView.this.isLongPressed && !z3) {
                            z = false;
                        }
                        PeerStoriesView.this.storyLines.draw(canvas, getMeasuredWidth(), PeerStoriesView.this.linesPosition, f3, PeerStoriesView.this.linesCount, animatedFloat.set(z), PeerStoriesView.this.alpha * (1.0f - PeerStoriesView.this.outT), z2, z3, f);
                        canvas.restore();
                    }
                }
                f = f3;
                z = true;
                if (!peerStoriesView.switchEventSent) {
                    PeerStoriesView.this.switchEventSent = true;
                    post(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$4$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            PeerStoriesView.4.this.lambda$drawLines$0();
                        }
                    });
                }
                storiesList = this.val$storyViewer.storiesList;
                if (storiesList != null) {
                    if (PeerStoriesView.this.storyPositionView == null) {
                    }
                    PeerStoriesView.this.storyPositionView.draw(canvas, (1.0f - PeerStoriesView.this.outT) * hideInterfaceAlpha * PeerStoriesView.this.alpha, PeerStoriesView.this.listPosition, this.val$storyViewer.storiesList.getCount(), this, PeerStoriesView.this.headerView);
                }
                canvas.save();
                canvas.translate(0.0f, AndroidUtilities.dp(8.0f) - (AndroidUtilities.dp(8.0f) * PeerStoriesView.this.outT));
                if (PeerStoriesView.this.currentStory.isVideo()) {
                }
                if (PeerStoriesView.this.isLongPressed) {
                }
                AnimatedFloat animatedFloat2 = PeerStoriesView.this.linesAlpha;
                if (PeerStoriesView.this.isLongPressed) {
                    z = false;
                }
                PeerStoriesView.this.storyLines.draw(canvas, getMeasuredWidth(), PeerStoriesView.this.linesPosition, f3, PeerStoriesView.this.linesCount, animatedFloat2.set(z), PeerStoriesView.this.alpha * (1.0f - PeerStoriesView.this.outT), z2, z3, f);
                canvas.restore();
            }
            PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
            StoryViewer.VideoPlayerHolder videoPlayerHolder2 = peerStoriesView4.playerSharedScope.player;
            if (videoPlayerHolder2 != null) {
                clamp = Utilities.clamp(videoPlayerHolder2.getPlaybackProgress(peerStoriesView4.videoDuration), 1.0f, 0.0f);
                PeerStoriesView peerStoriesView5 = PeerStoriesView.this;
                if (peerStoriesView5.playerSharedScope.firstFrameRendered && peerStoriesView5.storyAreasView != null) {
                    PeerStoriesView.this.storyAreasView.shine();
                }
            } else {
                clamp = 0.0f;
            }
            invalidate();
            float f32 = clamp;
            peerStoriesView = PeerStoriesView.this;
            videoPlayerSharedScope = peerStoriesView.playerSharedScope;
            if (videoPlayerSharedScope != null) {
                f2 = videoPlayerHolder.currentSeek;
                if (f2 >= 0.0f) {
                }
            }
            f = f32;
            z = true;
            if (!peerStoriesView.switchEventSent) {
            }
            storiesList = this.val$storyViewer.storiesList;
            if (storiesList != null) {
            }
            canvas.save();
            canvas.translate(0.0f, AndroidUtilities.dp(8.0f) - (AndroidUtilities.dp(8.0f) * PeerStoriesView.this.outT));
            if (PeerStoriesView.this.currentStory.isVideo()) {
            }
            if (PeerStoriesView.this.isLongPressed) {
            }
            AnimatedFloat animatedFloat22 = PeerStoriesView.this.linesAlpha;
            if (PeerStoriesView.this.isLongPressed) {
            }
            PeerStoriesView.this.storyLines.draw(canvas, getMeasuredWidth(), PeerStoriesView.this.linesPosition, f32, PeerStoriesView.this.linesCount, animatedFloat22.set(z), PeerStoriesView.this.alpha * (1.0f - PeerStoriesView.this.outT), z2, z3, f);
            canvas.restore();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$drawLines$0() {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            if (peerStoriesView.delegate != null) {
                if (!peerStoriesView.isUploading && !PeerStoriesView.this.isEditing && !PeerStoriesView.this.isFailed) {
                    PeerStoriesView.this.delegate.shouldSwitchToNext();
                } else if (PeerStoriesView.this.currentStory.isVideo()) {
                    PeerStoriesView.this.playerSharedScope.player.loopBack();
                } else {
                    PeerStoriesView.this.currentImageTime = 0L;
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:114:0x0370, code lost:
            if (((r5 - (((int) (r16.this$0.storyCaptionView.getMaxTop() - org.telegram.messenger.AndroidUtilities.dp(24.0f))) + r16.this$0.storyCaptionView.getTop())) / org.telegram.messenger.AndroidUtilities.dp(60.0f)) > 0.0f) goto L161;
         */
        /* JADX WARN: Code restructure failed: missing block: B:119:0x039f, code lost:
            if (((r5 - (((int) (r7.storyCaptionView.getMaxTop() - org.telegram.messenger.AndroidUtilities.dp(24.0f))) + r16.this$0.storyCaptionView.getTop())) / org.telegram.messenger.AndroidUtilities.dp(60.0f)) > 0.0f) goto L161;
         */
        /* JADX WARN: Code restructure failed: missing block: B:120:0x03a1, code lost:
            r16.this$0.inBlackoutMode = true;
         */
        /* JADX WARN: Removed duplicated region for block: B:175:0x0598  */
        /* JADX WARN: Removed duplicated region for block: B:178:0x05a2  */
        /* JADX WARN: Removed duplicated region for block: B:195:0x05f4  */
        /* JADX WARN: Removed duplicated region for block: B:198:0x0602  */
        /* JADX WARN: Removed duplicated region for block: B:200:? A[RETURN, SYNTHETIC] */
        @Override // android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void dispatchDraw(Canvas canvas) {
            float f;
            PeerStoriesView peerStoriesView;
            boolean z;
            boolean hasNotThumb;
            StoryViewer.VideoPlayerHolder videoPlayerHolder;
            StoryViewer.VideoPlayerHolder videoPlayerHolder2;
            PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
            boolean z2 = true;
            if (!peerStoriesView2.isActive) {
                peerStoriesView2.headerView.backupImageView.getImageReceiver().setVisible(true, true);
            }
            PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
            if (peerStoriesView3.unsupported) {
                canvas.drawColor(ColorUtils.blendARGB(-16777216, -1, 0.2f));
            } else {
                if (peerStoriesView3.playerSharedScope.renderView != null || (peerStoriesView3.storyAreasView != null && (PeerStoriesView.this.storyAreasView.hasSelectedForScale() || PeerStoriesView.this.storyAreasView.parentHighlightScaleAlpha.isInProgress()))) {
                    invalidate();
                }
                canvas.save();
                PeerStoriesView.this.pinchToZoomHelper.applyTransform(canvas);
                PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                VideoPlayerSharedScope videoPlayerSharedScope = peerStoriesView4.playerSharedScope;
                View view = videoPlayerSharedScope.renderView;
                if (view == null || !videoPlayerSharedScope.firstFrameRendered) {
                    if (view != null) {
                        invalidate();
                    }
                    PeerStoriesView peerStoriesView5 = PeerStoriesView.this;
                    if (peerStoriesView5.currentStory.skipped) {
                        canvas.drawColor(ColorUtils.blendARGB(-16777216, -1, 0.2f));
                    } else {
                        if (!peerStoriesView5.imageReceiver.hasBitmapImage()) {
                            this.val$sharedResources.imageBackgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight() + 1);
                            this.val$sharedResources.imageBackgroundDrawable.draw(canvas);
                        }
                        PeerStoriesView.this.imageReceiver.setImageCoords(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight() + 1);
                        PeerStoriesView.this.imageReceiver.draw(canvas);
                    }
                } else {
                    if (!peerStoriesView4.imageReceiver.hasBitmapImage()) {
                        this.val$sharedResources.imageBackgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight() + 1);
                        this.val$sharedResources.imageBackgroundDrawable.draw(canvas);
                    }
                    PeerStoriesView.this.imageReceiver.setImageCoords(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight() + 1);
                    PeerStoriesView.this.imageReceiver.draw(canvas);
                    PeerStoriesView peerStoriesView6 = PeerStoriesView.this;
                    if (peerStoriesView6.isActive) {
                        boolean z3 = this.val$storyViewer.USE_SURFACE_VIEW;
                        if (z3 && (videoPlayerHolder2 = peerStoriesView6.playerSharedScope.player) != null && videoPlayerHolder2.paused && videoPlayerHolder2.playerStubBitmap != null && videoPlayerHolder2.stubAvailable) {
                            canvas.save();
                            canvas.scale(getMeasuredWidth() / PeerStoriesView.this.playerSharedScope.player.playerStubBitmap.getWidth(), getMeasuredHeight() / PeerStoriesView.this.playerSharedScope.player.playerStubBitmap.getHeight());
                            StoryViewer.VideoPlayerHolder videoPlayerHolder3 = PeerStoriesView.this.playerSharedScope.player;
                            canvas.drawBitmap(videoPlayerHolder3.playerStubBitmap, 0.0f, 0.0f, videoPlayerHolder3.playerStubPaint);
                            canvas.restore();
                        } else if (!z3 || (peerStoriesView6.allowDrawSurface && this.val$storyViewer.isShown())) {
                            PeerStoriesView.this.playerSharedScope.renderView.draw(canvas);
                        }
                    }
                }
                canvas.restore();
                if (PeerStoriesView.this.imageChanged) {
                    this.loadingDrawableAlpha2.set(0.0f, true);
                    this.loadingDrawableAlpha.set(0.0f, true);
                }
                if (PeerStoriesView.this.currentStory.isVideo) {
                    VideoPlayerSharedScope videoPlayerSharedScope2 = PeerStoriesView.this.playerSharedScope;
                    hasNotThumb = (videoPlayerSharedScope2.renderView == null || (videoPlayerHolder = videoPlayerSharedScope2.player) == null || !videoPlayerSharedScope2.firstFrameRendered || (videoPlayerHolder.progress == 0.0f && videoPlayerSharedScope2.isBuffering() && !PeerStoriesView.this.playerSharedScope.player.paused)) ? false : true;
                } else {
                    hasNotThumb = PeerStoriesView.this.imageReceiver.hasNotThumb();
                }
                AnimatedFloat animatedFloat = this.loadingDrawableAlpha2;
                PeerStoriesView peerStoriesView7 = PeerStoriesView.this;
                animatedFloat.set((peerStoriesView7.isActive && !hasNotThumb && peerStoriesView7.currentStory.uploadingStory == null) ? 1.0f : 0.0f);
                this.loadingDrawableAlpha.set(this.loadingDrawableAlpha2.get() == 1.0f ? 1.0f : 0.0f);
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
            if (PeerStoriesView.this.storyCaptionView.getAlpha() > 0.0f) {
                if (PeerStoriesView.this.storyCaptionView.getAlpha() == 1.0f) {
                    canvas.save();
                } else {
                    canvas.saveLayerAlpha(0.0f, 0.0f, PeerStoriesView.this.storyCaptionView.getMeasuredWidth(), PeerStoriesView.this.storyCaptionView.getMeasuredHeight(), (int) (PeerStoriesView.this.storyCaptionView.getAlpha() * 255.0f), 31);
                }
                PeerStoriesView.this.storyAreasView.draw(canvas);
                canvas.restore();
            }
            if (!PeerStoriesView.this.lastNoThumb && PeerStoriesView.this.imageReceiver.hasNotThumb()) {
                PeerStoriesView.this.lastNoThumb = true;
                PeerStoriesView.this.invalidate();
            }
            float hideInterfaceAlpha = PeerStoriesView.this.getHideInterfaceAlpha();
            this.val$sharedResources.topOverlayGradient.setAlpha(NotificationCenter.voipServiceCreated);
            this.val$sharedResources.topOverlayGradient.draw(canvas);
            PeerStoriesView peerStoriesView8 = PeerStoriesView.this;
            if (peerStoriesView8.isSelf || !peerStoriesView8.BIG_SCREEN || PeerStoriesView.this.storyCaptionView.getVisibility() == 0) {
                if (PeerStoriesView.this.storyCaptionView.getVisibility() == 0) {
                    int dp = AndroidUtilities.dp(72.0f);
                    int textTop = ((int) (PeerStoriesView.this.storyCaptionView.getTextTop() - AndroidUtilities.dp(24.0f))) + PeerStoriesView.this.storyCaptionView.getTop();
                    int i = dp + textTop;
                    float measuredHeight = getMeasuredHeight() * 0.65f;
                    boolean hideCaptionWithInterface = PeerStoriesView.this.hideCaptionWithInterface();
                    if ((measuredHeight - textTop) / AndroidUtilities.dp(60.0f) <= 0.0f || !PeerStoriesView.this.storyCaptionView.isTouched() || !PeerStoriesView.this.storyCaptionView.hasScroll()) {
                        PeerStoriesView peerStoriesView9 = PeerStoriesView.this;
                        if (peerStoriesView9.checkBlackoutMode) {
                            peerStoriesView9.checkBlackoutMode = false;
                        } else if (peerStoriesView9.storyCaptionView.getProgressToBlackout() == 0.0f) {
                            PeerStoriesView.this.inBlackoutMode = false;
                        }
                        if (!hideCaptionWithInterface) {
                            hideInterfaceAlpha = 1.0f;
                        }
                        f = this.progressToFullBlackoutA.set(PeerStoriesView.this.inBlackoutMode ? 1.0f : 0.0f);
                        if (f > 0.0f) {
                            this.splitDrawing = true;
                            this.drawOverlayed = false;
                            super.dispatchDraw(canvas);
                            this.splitDrawing = false;
                            drawLines(canvas);
                            this.val$sharedResources.gradientBackgroundPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (153.0f * f * hideInterfaceAlpha)));
                            canvas.drawPaint(this.val$sharedResources.gradientBackgroundPaint);
                        }
                        if (f < 1.0f) {
                            canvas.save();
                            float f2 = 1.0f - f;
                            this.val$sharedResources.gradientBackgroundPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (129.03f * f2 * hideInterfaceAlpha)));
                            this.val$sharedResources.bottomOverlayGradient.setAlpha((int) (f2 * 255.0f * hideInterfaceAlpha));
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
                    }
                    if (PeerStoriesView.this.viewsThumbAlpha != 0.0f && PeerStoriesView.this.viewsThumbImageReceiver != null) {
                        PeerStoriesView.this.viewsThumbImageReceiver.draw(canvas, PeerStoriesView.this.viewsThumbAlpha, PeerStoriesView.this.viewsThumbScale, 0, 0, getMeasuredWidth(), getMeasuredHeight() + 1);
                    }
                    this.progressToAudio.set(PeerStoriesView.this.isRecording ? 1.0f : 0.0f);
                    peerStoriesView = PeerStoriesView.this;
                    if (peerStoriesView.isActive) {
                        if (peerStoriesView.storyCaptionView.getVisibility() == 0) {
                            PeerStoriesView peerStoriesView10 = PeerStoriesView.this;
                            if (peerStoriesView10.inBlackoutMode || peerStoriesView10.storyCaptionView.isTouched()) {
                                z = true;
                                PeerStoriesView peerStoriesView11 = PeerStoriesView.this;
                                peerStoriesView11.isCaptionPartVisible = (peerStoriesView11.storyCaptionView.getVisibility() == 0 || PeerStoriesView.this.storyCaptionView.getProgressToBlackout() <= 0.0f) ? false : false;
                                PeerStoriesView.this.delegate.setIsCaption(z);
                                PeerStoriesView peerStoriesView12 = PeerStoriesView.this;
                                peerStoriesView12.delegate.setIsCaptionPartVisible(peerStoriesView12.isCaptionPartVisible);
                            }
                        }
                        z = false;
                        PeerStoriesView peerStoriesView112 = PeerStoriesView.this;
                        peerStoriesView112.isCaptionPartVisible = (peerStoriesView112.storyCaptionView.getVisibility() == 0 || PeerStoriesView.this.storyCaptionView.getProgressToBlackout() <= 0.0f) ? false : false;
                        PeerStoriesView.this.delegate.setIsCaption(z);
                        PeerStoriesView peerStoriesView122 = PeerStoriesView.this;
                        peerStoriesView122.delegate.setIsCaptionPartVisible(peerStoriesView122.isCaptionPartVisible);
                    }
                    if (f <= 0.0f) {
                        super.dispatchDraw(canvas);
                        drawLines(canvas);
                    }
                    if (PeerStoriesView.this.emojiAnimationsOverlay == null) {
                        PeerStoriesView.this.emojiAnimationsOverlay.draw(canvas);
                        return;
                    }
                    return;
                }
                int dp2 = AndroidUtilities.dp(PeerStoriesView.this.BIG_SCREEN ? 56.0f : 110.0f);
                PeerStoriesView peerStoriesView13 = PeerStoriesView.this;
                if ((peerStoriesView13.isSelf || !peerStoriesView13.BIG_SCREEN) && PeerStoriesView.this.storyCaptionView.getVisibility() == 0) {
                    dp2 = (int) (dp2 * 2.5f);
                }
                this.val$sharedResources.bottomOverlayGradient.setBounds(0, PeerStoriesView.this.storyContainer.getMeasuredHeight() - dp2, getMeasuredWidth(), PeerStoriesView.this.storyContainer.getMeasuredHeight());
                this.val$sharedResources.bottomOverlayGradient.setAlpha((int) (hideInterfaceAlpha * 255.0f));
                this.val$sharedResources.bottomOverlayGradient.draw(canvas);
            }
            f = 0.0f;
            if (PeerStoriesView.this.viewsThumbAlpha != 0.0f) {
                PeerStoriesView.this.viewsThumbImageReceiver.draw(canvas, PeerStoriesView.this.viewsThumbAlpha, PeerStoriesView.this.viewsThumbScale, 0, 0, getMeasuredWidth(), getMeasuredHeight() + 1);
            }
            this.progressToAudio.set(PeerStoriesView.this.isRecording ? 1.0f : 0.0f);
            peerStoriesView = PeerStoriesView.this;
            if (peerStoriesView.isActive) {
            }
            if (f <= 0.0f) {
            }
            if (PeerStoriesView.this.emojiAnimationsOverlay == null) {
            }
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (view == PeerStoriesView.this.storyAreasView) {
                return true;
            }
            if (this.splitDrawing) {
                if (Bulletin.getVisibleBulletin() == null || view != Bulletin.getVisibleBulletin().getLayout()) {
                    return super.drawChild(canvas, view, j);
                }
                if (this.drawOverlayed) {
                    return super.drawChild(canvas, view, j);
                }
                return true;
            }
            return super.drawChild(canvas, view, j);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            PeerStoriesView.this.emojiAnimationsOverlay.onAttachedToWindow();
            Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.4.1
                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ boolean allowLayoutChanges() {
                    return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ boolean bottomOffsetAnimated() {
                    return Bulletin.Delegate.-CC.$default$bottomOffsetAnimated(this);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public boolean clipWithGradient(int i) {
                    return i == 1 || i == 2 || i == 3;
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public int getBottomOffset(int i) {
                    if (PeerStoriesView.this.BIG_SCREEN) {
                        return 0;
                    }
                    return AndroidUtilities.dp(64.0f);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public int getTopOffset(int i) {
                    return AndroidUtilities.dp(58.0f);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ void onBottomOffsetChange(float f) {
                    Bulletin.Delegate.-CC.$default$onBottomOffsetChange(this, f);
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
                public void onShow(Bulletin bulletin) {
                    Delegate delegate;
                    if (bulletin == null || bulletin.tag != 2 || (delegate = PeerStoriesView.this.delegate) == null) {
                        return;
                    }
                    delegate.setBulletinIsVisible(true);
                }
            });
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            PeerStoriesView.this.emojiAnimationsOverlay.onDetachedFromWindow();
            Bulletin.removeDelegate(this);
            Delegate delegate = PeerStoriesView.this.delegate;
            if (delegate != null) {
                delegate.setBulletinIsVisible(false);
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            float f;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) PeerStoriesView.this.muteIconContainer.getLayoutParams();
            if (PeerStoriesView.this.drawLinesAsCounter()) {
                layoutParams.rightMargin = AndroidUtilities.dp(2.0f);
                f = 55.0f;
            } else {
                layoutParams.rightMargin = AndroidUtilities.dp(42.0f);
                f = 15.0f;
            }
            layoutParams.topMargin = AndroidUtilities.dp(f);
            super.onMeasure(i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 5 extends StoryCaptionView {
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;
        final /* synthetic */ StoryViewer val$storyViewer;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        5(Context context, Theme.ResourcesProvider resourcesProvider, StoryViewer storyViewer, Theme.ResourcesProvider resourcesProvider2) {
            super(context, resourcesProvider);
            this.val$storyViewer = storyViewer;
            this.val$resourcesProvider = resourcesProvider2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEmojiClick$4(StoryViewer storyViewer, Theme.ResourcesProvider resourcesProvider, TLRPC$InputStickerSet tLRPC$InputStickerSet) {
            ArrayList arrayList = new ArrayList(1);
            arrayList.add(tLRPC$InputStickerSet);
            EmojiPacksAlert emojiPacksAlert = new EmojiPacksAlert(storyViewer.fragment, getContext(), resourcesProvider, arrayList);
            Delegate delegate = PeerStoriesView.this.delegate;
            if (delegate != null) {
                delegate.showDialog(emojiPacksAlert);
            }
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

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReplyClick$3(StoryCaptionView.Reply reply, final StoryViewer storyViewer, Theme.ResourcesProvider resourcesProvider, TL_stories$StoryItem tL_stories$StoryItem) {
            if (tL_stories$StoryItem == null) {
                BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createSimpleBulletin(R.raw.story_bomb2, LocaleController.getString(R.string.StoryNotFound)).setTag(3).show(true);
                return;
            }
            BaseFragment lastFragment = LaunchActivity.getLastFragment();
            if (lastFragment == null) {
                return;
            }
            tL_stories$StoryItem.dialogId = reply.peerId.longValue();
            StoryViewer createOverlayStoryViewer = lastFragment.createOverlayStoryViewer();
            createOverlayStoryViewer.open(getContext(), tL_stories$StoryItem, (StoryViewer.PlaceProvider) null);
            createOverlayStoryViewer.setOnCloseListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$5$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    StoryViewer.this.updatePlayingMode();
                }
            });
            storyViewer.updatePlayingMode();
        }

        private void processExternalUrl(int i, String str, CharacterStyle characterStyle, boolean z) {
            boolean z2;
            if (!z && !AndroidUtilities.shouldShowUrlInAlert(str)) {
                if (i == 0) {
                    Browser.openUrl(getContext(), Uri.parse(str), true, true, null);
                } else if (i == 1) {
                    Browser.openUrl(getContext(), Uri.parse(str), false, false, null);
                } else if (i == 2) {
                    Browser.openUrl(getContext(), Uri.parse(str), false, true, null);
                }
            } else if (i != 0 && i != 2) {
                if (i == 1) {
                    AlertsCreator.showOpenUrlAlert(this.val$storyViewer.fragment, str, true, true, false, null, this.val$resourcesProvider);
                }
            } else {
                if (characterStyle instanceof URLSpanReplacement) {
                    URLSpanReplacement uRLSpanReplacement = (URLSpanReplacement) characterStyle;
                    if (uRLSpanReplacement.getTextStyleRun() != null && (uRLSpanReplacement.getTextStyleRun().flags & 1024) != 0) {
                        z2 = true;
                        AlertsCreator.showOpenUrlAlert(this.val$storyViewer.fragment, str, true, true, true, z2, null, this.val$resourcesProvider);
                    }
                }
                z2 = false;
                AlertsCreator.showOpenUrlAlert(this.val$storyViewer.fragment, str, true, true, true, z2, null, this.val$resourcesProvider);
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
                Bulletin createContainsEmojiBulletin = of.createContainsEmojiBulletin(tLRPC$Document, 2, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$5$$ExternalSyntheticLambda1
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        PeerStoriesView.5.this.lambda$onEmojiClick$4(storyViewer, resourcesProvider, (TLRPC$InputStickerSet) obj);
                    }
                });
                if (createContainsEmojiBulletin == null) {
                    return;
                }
                createContainsEmojiBulletin.tag = 1;
                createContainsEmojiBulletin.show(true);
            }
        }

        @Override // org.telegram.ui.Stories.StoryCaptionView
        public void onLinkClick(CharacterStyle characterStyle, View view) {
            if (characterStyle instanceof URLSpanUserMention) {
                TLRPC$User user = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Utilities.parseLong(((URLSpanUserMention) characterStyle).getURL()));
                if (user != null) {
                    MessagesController.getInstance(PeerStoriesView.this.currentAccount).openChatOrProfileWith(user, null, this.val$storyViewer.fragment, 0, false);
                }
            } else if (!(characterStyle instanceof URLSpanNoUnderline)) {
                if (characterStyle instanceof URLSpan) {
                    processExternalUrl(2, ((URLSpan) characterStyle).getURL(), characterStyle, characterStyle instanceof URLSpanReplacement);
                } else if (characterStyle instanceof URLSpanMono) {
                    ((URLSpanMono) characterStyle).copyToClipboard();
                    BulletinFactory.of(PeerStoriesView.this.storyContainer, this.val$resourcesProvider).createCopyBulletin(LocaleController.getString(R.string.TextCopied)).show();
                } else if (characterStyle instanceof ClickableSpan) {
                    ((ClickableSpan) characterStyle).onClick(view);
                }
            } else {
                String url = ((URLSpanNoUnderline) characterStyle).getURL();
                if (url == null || !(url.startsWith("#") || url.startsWith("$"))) {
                    String extractUsername = Browser.extractUsername(url);
                    if (extractUsername != null) {
                        String lowerCase = extractUsername.toLowerCase();
                        if (url.startsWith("@")) {
                            MessagesController.getInstance(PeerStoriesView.this.currentAccount).openByUserName(lowerCase, this.val$storyViewer.fragment, 0, null);
                            return;
                        }
                    }
                    processExternalUrl(0, url, characterStyle, false);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("type", 3);
                bundle.putString("hashtag", url);
                StoryViewer storyViewer = this.val$storyViewer;
                if (storyViewer != null) {
                    storyViewer.presentFragment(new MediaActivity(bundle, null));
                }
            }
        }

        @Override // org.telegram.ui.Stories.StoryCaptionView
        public void onLinkLongPress(final URLSpan uRLSpan, final View view, final Runnable runnable) {
            final String url = uRLSpan.getURL();
            String url2 = uRLSpan.getURL();
            try {
                try {
                    Uri parse = Uri.parse(url2);
                    url2 = Browser.replaceHostname(parse, Browser.IDN_toUnicode(parse.getHost()), null);
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
            CharSequence[] charSequenceArr = (storyItemHolder == null || storyItemHolder.allowScreenshots()) ? new CharSequence[]{LocaleController.getString(R.string.Open), LocaleController.getString(R.string.Copy)} : new CharSequence[]{LocaleController.getString(R.string.Open)};
            final Theme.ResourcesProvider resourcesProvider = this.val$resourcesProvider;
            builder.setItems(charSequenceArr, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$5$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    PeerStoriesView.5.this.lambda$onLinkLongPress$0(uRLSpan, view, url, resourcesProvider, dialogInterface, i);
                }
            });
            builder.setOnPreDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$5$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    runnable.run();
                }
            });
            BottomSheet create = builder.create();
            create.fixNavigationBar(Theme.getColor(Theme.key_dialogBackground, this.val$resourcesProvider));
            PeerStoriesView.this.delegate.showDialog(create);
        }

        @Override // org.telegram.ui.Stories.StoryCaptionView
        public void onReplyClick(final StoryCaptionView.Reply reply) {
            String str;
            if (reply == null) {
                return;
            }
            if (!reply.isRepostMessage || reply.peerId == null || reply.messageId == null) {
                if (reply.peerId == null || reply.storyId == null) {
                    BulletinFactory.of(PeerStoriesView.this.storyContainer, this.val$resourcesProvider).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.StoryHidAccount)).setTag(3).show(true);
                    return;
                }
                StoriesController storiesController = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getStoriesController();
                long longValue = reply.peerId.longValue();
                int intValue = reply.storyId.intValue();
                final StoryViewer storyViewer = this.val$storyViewer;
                final Theme.ResourcesProvider resourcesProvider = this.val$resourcesProvider;
                storiesController.resolveStoryLink(longValue, intValue, new Consumer() { // from class: org.telegram.ui.Stories.PeerStoriesView$5$$ExternalSyntheticLambda0
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        PeerStoriesView.5.this.lambda$onReplyClick$3(reply, storyViewer, resourcesProvider, (TL_stories$StoryItem) obj);
                    }
                });
                return;
            }
            Bundle bundle = new Bundle();
            int i = (reply.peerId.longValue() > 0L ? 1 : (reply.peerId.longValue() == 0L ? 0 : -1));
            long longValue2 = reply.peerId.longValue();
            if (i >= 0) {
                str = "user_id";
            } else {
                longValue2 = -longValue2;
                str = "chat_id";
            }
            bundle.putLong(str, longValue2);
            bundle.putInt("message_id", reply.messageId.intValue());
            this.val$storyViewer.presentFragment(new ChatActivity(bundle));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 8 extends CustomPopupMenu {
        private boolean edit;
        final /* synthetic */ boolean val$canEditStory;
        final /* synthetic */ Context val$context;
        final /* synthetic */ boolean[] val$popupStillVisible;
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;
        final /* synthetic */ SharedResources val$sharedResources;
        final /* synthetic */ boolean val$speedControl;
        final /* synthetic */ StoryViewer val$storyViewer;
        final /* synthetic */ boolean val$userCanEditStory;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        8(Context context, Theme.ResourcesProvider resourcesProvider, boolean z, Theme.ResourcesProvider resourcesProvider2, StoryViewer storyViewer, boolean z2, boolean z3, boolean z4, Context context2, SharedResources sharedResources, boolean[] zArr) {
            super(context, resourcesProvider, z);
            this.val$resourcesProvider = resourcesProvider2;
            this.val$storyViewer = storyViewer;
            this.val$speedControl = z2;
            this.val$canEditStory = z3;
            this.val$userCanEditStory = z4;
            this.val$context = context2;
            this.val$sharedResources = sharedResources;
            this.val$popupStillVisible = zArr;
        }

        private void addSpeedLayout(final ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, boolean z) {
            PeerStoriesView peerStoriesView;
            StoryItemHolder storyItemHolder;
            if (!this.val$speedControl || ((storyItemHolder = (peerStoriesView = PeerStoriesView.this).currentStory) != null && storyItemHolder.uploadingStory != null)) {
                PeerStoriesView.this.speedLayout = null;
                PeerStoriesView.this.speedItem = null;
                return;
            }
            peerStoriesView.speedLayout = new ChooseSpeedLayout(peerStoriesView.getContext(), actionBarPopupWindowLayout.getSwipeBack(), new ChooseSpeedLayout.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView.8.2
                @Override // org.telegram.ui.ChooseSpeedLayout.Callback
                public void onSpeedSelected(float f, boolean z2, boolean z3) {
                    StoryViewer storyViewer = 8.this.val$storyViewer;
                    if (storyViewer != null) {
                        storyViewer.setSpeed(f);
                    }
                    PeerStoriesView.this.updateSpeedItem(z2);
                    if (!z3 || actionBarPopupWindowLayout.getSwipeBack() == null) {
                        return;
                    }
                    actionBarPopupWindowLayout.getSwipeBack().closeForeground();
                }
            });
            PeerStoriesView.this.speedLayout.update(StoryViewer.currentSpeed, true);
            PeerStoriesView.this.speedItem = new ActionBarMenuSubItem(PeerStoriesView.this.getContext(), false, false, false, this.val$resourcesProvider);
            PeerStoriesView.this.speedItem.setTextAndIcon(LocaleController.getString(R.string.Speed), R.drawable.msg_speed, null);
            PeerStoriesView.this.updateSpeedItem(true);
            PeerStoriesView.this.speedItem.setMinimumWidth(AndroidUtilities.dp(196.0f));
            PeerStoriesView.this.speedItem.setRightIcon(R.drawable.msg_arrowright);
            actionBarPopupWindowLayout.addView(PeerStoriesView.this.speedItem);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) PeerStoriesView.this.speedItem.getLayoutParams();
            if (LocaleController.isRTL) {
                layoutParams.gravity = 5;
            }
            layoutParams.width = -1;
            layoutParams.height = AndroidUtilities.dp(48.0f);
            PeerStoriesView.this.speedItem.setLayoutParams(layoutParams);
            final int addViewToSwipeBack = actionBarPopupWindowLayout.addViewToSwipeBack(PeerStoriesView.this.speedLayout.speedSwipeBackLayout);
            PeerStoriesView.this.speedItem.openSwipeBackLayout = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda31
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.8.lambda$addSpeedLayout$1(ActionBarPopupWindow.ActionBarPopupWindowLayout.this, addViewToSwipeBack);
                }
            };
            PeerStoriesView.this.speedItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda32
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PeerStoriesView.8.this.lambda$addSpeedLayout$2(view);
                }
            });
            actionBarPopupWindowLayout.swipeBackGravityRight = true;
            if (z) {
                ActionBarPopupWindow.GapView gapView = new ActionBarPopupWindow.GapView(PeerStoriesView.this.getContext(), this.val$resourcesProvider, Theme.key_actionBarDefaultSubmenuSeparator);
                gapView.setTag(R.id.fit_width_tag, 1);
                actionBarPopupWindowLayout.addView((View) gapView, LayoutHelper.createLinear(-1, 8));
            }
        }

        private void addViewStatistics(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, final TL_stories$StoryItem tL_stories$StoryItem) {
            final TLRPC$Chat chat;
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            if (!peerStoriesView.isChannel || (chat = MessagesController.getInstance(peerStoriesView.currentAccount).getChat(Long.valueOf(-PeerStoriesView.this.dialogId))) == null) {
                return;
            }
            TLRPC$ChatFull chatFull = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getChatFull(chat.id);
            if (chatFull == null) {
                chatFull = MessagesStorage.getInstance(PeerStoriesView.this.currentAccount).loadChatInfo(chat.id, true, new CountDownLatch(1), false, false);
            }
            if (chatFull == null || !chatFull.can_view_stats) {
                return;
            }
            ActionBarMenuSubItem addItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_stats, LocaleController.getString(R.string.ViewStatistics), false, this.val$resourcesProvider);
            final StoryViewer storyViewer = this.val$storyViewer;
            addItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda37
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PeerStoriesView.8.this.lambda$addViewStatistics$0(tL_stories$StoryItem, storyViewer, chat, view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$addSpeedLayout$1(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, int i) {
            if (actionBarPopupWindowLayout.getSwipeBack() != null) {
                actionBarPopupWindowLayout.getSwipeBack().openForeground(i);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addSpeedLayout$2(View view) {
            PeerStoriesView.this.speedItem.openSwipeBack();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addViewStatistics$0(TL_stories$StoryItem tL_stories$StoryItem, StoryViewer storyViewer, TLRPC$Chat tLRPC$Chat, View view) {
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
            tL_stories$StoryItem.dialogId = PeerStoriesView.this.dialogId;
            tL_stories$StoryItem.messageId = tL_stories$StoryItem.id;
            MessageObject messageObject = new MessageObject(PeerStoriesView.this.currentAccount, tL_stories$StoryItem);
            messageObject.generateThumbs(false);
            storyViewer.presentFragment(new MessageStatisticActivity(messageObject, tLRPC$Chat.id, false) { // from class: org.telegram.ui.Stories.PeerStoriesView.8.1
                @Override // org.telegram.ui.ActionBar.BaseFragment
                public Theme.ResourcesProvider getResourceProvider() {
                    return new DarkThemeResourceProvider();
                }

                @Override // org.telegram.ui.MessageStatisticActivity, org.telegram.ui.ActionBar.BaseFragment
                public boolean isLightStatusBar() {
                    return false;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$10(Theme.ResourcesProvider resourcesProvider, Context context, final StoryViewer storyViewer, final SharedResources sharedResources, View view) {
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
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.8.this.lambda$onCreate$9(findActivity, storyViewer, sharedResources);
                }
            };
            if (PeerStoriesView.this.delegate.releasePlayer(runnable)) {
                return;
            }
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onCreate$11(StoriesController.BotPreviewsList botPreviewsList, TL_stories$StoryItem tL_stories$StoryItem, Utilities.Callback callback) {
            TL_stories$StoryItem tL_stories$StoryItem2;
            TLRPC$MessageMedia tLRPC$MessageMedia;
            TLRPC$Document tLRPC$Document;
            TLRPC$Document tLRPC$Document2;
            for (int i = 0; i < botPreviewsList.messageObjects.size(); i++) {
                MessageObject messageObject = (MessageObject) botPreviewsList.messageObjects.get(i);
                if (messageObject != null && (tL_stories$StoryItem2 = messageObject.storyItem) != null && (tLRPC$MessageMedia = tL_stories$StoryItem2.media) != null && (tLRPC$Document = tL_stories$StoryItem.media.document) != null && (tLRPC$Document2 = tLRPC$MessageMedia.document) != null && tLRPC$Document2.id == tLRPC$Document.id) {
                    callback.run(tLRPC$Document2);
                    return;
                }
            }
            callback.run(null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$12(TLObject tLObject, TL_stories$StoryItem tL_stories$StoryItem, Utilities.Callback callback) {
            TLRPC$Document tLRPC$Document;
            if (tLObject instanceof TL_stories$TL_stories_stories) {
                TL_stories$TL_stories_stories tL_stories$TL_stories_stories = (TL_stories$TL_stories_stories) tLObject;
                MessagesController.getInstance(PeerStoriesView.this.currentAccount).putUsers(tL_stories$TL_stories_stories.users, false);
                MessagesController.getInstance(PeerStoriesView.this.currentAccount).putChats(tL_stories$TL_stories_stories.chats, false);
                for (int i = 0; i < tL_stories$TL_stories_stories.stories.size(); i++) {
                    if (((TL_stories$StoryItem) tL_stories$TL_stories_stories.stories.get(i)).id == tL_stories$StoryItem.id) {
                        tLRPC$Document = ((TL_stories$StoryItem) tL_stories$TL_stories_stories.stories.get(i)).media.document;
                        break;
                    }
                }
            }
            tLRPC$Document = null;
            callback.run(tLRPC$Document);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$13(final TL_stories$StoryItem tL_stories$StoryItem, final Utilities.Callback callback, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.8.this.lambda$onCreate$12(tLObject, tL_stories$StoryItem, callback);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$14(final TL_stories$StoryItem tL_stories$StoryItem, final TL_stories$StoryItem tL_stories$StoryItem2, final Utilities.Callback callback) {
            final StoriesController.BotPreviewsList botPreviewsList;
            if ((tL_stories$StoryItem instanceof StoriesController.BotPreview) && (botPreviewsList = ((StoriesController.BotPreview) tL_stories$StoryItem).list) != null) {
                botPreviewsList.reload(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.8.lambda$onCreate$11(StoriesController.BotPreviewsList.this, tL_stories$StoryItem2, callback);
                    }
                });
                return;
            }
            TL_stories$TL_stories_getStoriesByID tL_stories$TL_stories_getStoriesByID = new TL_stories$TL_stories_getStoriesByID();
            tL_stories$TL_stories_getStoriesByID.peer = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getInputPeer(tL_stories$StoryItem.dialogId);
            tL_stories$TL_stories_getStoriesByID.id.add(Integer.valueOf(tL_stories$StoryItem.id));
            ConnectionsManager.getInstance(PeerStoriesView.this.currentAccount).sendRequest(tL_stories$TL_stories_getStoriesByID, new RequestDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda1
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    PeerStoriesView.8.this.lambda$onCreate$13(tL_stories$StoryItem, callback, tLObject, tLRPC$TL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$15() {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.editOpened = true;
            peerStoriesView.setActive(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onCreate$16(Runnable runnable) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            AndroidUtilities.runOnUIThread(runnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onCreate$17(Runnable runnable, long j) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            AndroidUtilities.runOnUIThread(runnable, Math.max(0L, 32 - (System.currentTimeMillis() - j)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$18(SharedResources sharedResources, Long l, final Runnable runnable, Boolean bool, Long l2) {
            final long currentTimeMillis = System.currentTimeMillis();
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            VideoPlayerSharedScope videoPlayerSharedScope = peerStoriesView.playerSharedScope;
            StoryViewer.VideoPlayerHolder videoPlayerHolder = videoPlayerSharedScope.player;
            if (videoPlayerHolder == null) {
                peerStoriesView.delegate.setPopupIsVisible(false);
                PeerStoriesView.this.setActive(true);
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                peerStoriesView2.editOpened = false;
                peerStoriesView2.onImageReceiverThumbLoaded = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.8.lambda$onCreate$16(runnable);
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
            videoPlayerHolder.setOnReadyListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.8.lambda$onCreate$17(runnable, currentTimeMillis);
                }
            });
            PeerStoriesView.this.delegate.setPopupIsVisible(false);
            if (PeerStoriesView.this.muteIconView != null) {
                PeerStoriesView.this.muteIconView.setAnimation(sharedResources.muteDrawable);
            }
            PeerStoriesView.this.setActive(((PeerStoriesView.this.videoDuration <= 0 || l.longValue() <= PeerStoriesView.this.videoDuration - 1400) ? l : 0L).longValue(), true);
            PeerStoriesView.this.editOpened = false;
            AndroidUtilities.runOnUIThread(runnable, 400L);
            if (bool.booleanValue()) {
                PeerStoriesView.this.updatePosition();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$19(Activity activity, final TL_stories$StoryItem tL_stories$StoryItem, StoryViewer storyViewer, final SharedResources sharedResources) {
            StoryViewer.VideoPlayerHolder videoPlayerHolder;
            StoryRecorder storyRecorder = StoryRecorder.getInstance(activity, PeerStoriesView.this.currentAccount);
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            VideoPlayerSharedScope videoPlayerSharedScope = peerStoriesView.playerSharedScope;
            long j = (videoPlayerSharedScope == null || (videoPlayerHolder = videoPlayerSharedScope.player) == null) ? 0L : videoPlayerHolder.currentPosition;
            StoryEntry fromStoryItem = StoryEntry.fromStoryItem(peerStoriesView.currentStory.getPath(), PeerStoriesView.this.currentStory.storyItem);
            fromStoryItem.editStoryPeerId = PeerStoriesView.this.dialogId;
            fromStoryItem.cover = StoryEntry.getCoverTime(PeerStoriesView.this.currentStory.storyItem);
            StoryEntry copy = fromStoryItem.copy();
            copy.isEditingCover = true;
            final TL_stories$StoryItem tL_stories$StoryItem2 = PeerStoriesView.this.currentStory.storyItem;
            copy.editingCoverDocument = tL_stories$StoryItem2.media.document;
            copy.updateDocumentRef = new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda38
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    PeerStoriesView.8.this.lambda$onCreate$14(tL_stories$StoryItem2, tL_stories$StoryItem, (Utilities.Callback) obj);
                }
            };
            if (PeerStoriesView.this.isBotsPreview()) {
                copy.botId = PeerStoriesView.this.dialogId;
                copy.editingBotPreview = MessagesController.toInputMedia(PeerStoriesView.this.currentStory.storyItem.media);
                StoriesController.StoriesList storiesList = storyViewer.storiesList;
                if (storiesList instanceof StoriesController.BotPreviewsList) {
                    copy.botLang = ((StoriesController.BotPreviewsList) storiesList).lang_code;
                }
            }
            storyRecorder.openEdit(StoryRecorder.SourceView.fromStoryViewer(storyViewer), copy, j, true);
            storyRecorder.setOnFullyOpenListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda39
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.8.this.lambda$onCreate$15();
                }
            });
            storyRecorder.setOnPrepareCloseListener(new Utilities.Callback4() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda40
                @Override // org.telegram.messenger.Utilities.Callback4
                public final void run(Object obj, Object obj2, Object obj3, Object obj4) {
                    PeerStoriesView.8.this.lambda$onCreate$18(sharedResources, (Long) obj, (Runnable) obj2, (Boolean) obj3, (Long) obj4);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$20(Context context, final TL_stories$StoryItem tL_stories$StoryItem, final StoryViewer storyViewer, final SharedResources sharedResources, View view) {
            File path = PeerStoriesView.this.currentStory.getPath();
            if (path == null || !path.exists()) {
                PeerStoriesView.this.showDownloadAlert();
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
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda36
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.8.this.lambda$onCreate$19(findActivity, tL_stories$StoryItem, storyViewer, sharedResources);
                }
            };
            if (PeerStoriesView.this.delegate.releasePlayer(runnable)) {
                return;
            }
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$21(TL_stories$StoryItem tL_stories$StoryItem, boolean z, Theme.ResourcesProvider resourcesProvider, Boolean bool) {
            BulletinFactory of;
            int i;
            int i2;
            Bulletin createSimpleBulletin;
            if (bool.booleanValue()) {
                tL_stories$StoryItem.pinned = z;
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                boolean z2 = peerStoriesView.isSelf;
                of = BulletinFactory.of(peerStoriesView.storyContainer, resourcesProvider);
                if (z2) {
                    createSimpleBulletin = of.createSimpleBulletin(z ? R.raw.contact_check : R.raw.chats_archived, LocaleController.getString(z ? R.string.StoryPinnedToProfile : R.string.StoryArchivedFromProfile));
                } else if (z) {
                    createSimpleBulletin = of.createSimpleBulletin(R.raw.contact_check, LocaleController.getString(R.string.StoryPinnedToPosts), LocaleController.getString(R.string.StoryPinnedToPostsDescription));
                } else {
                    i = R.raw.chats_archived;
                    i2 = R.string.StoryUnpinnedFromPosts;
                }
                createSimpleBulletin.show();
            }
            of = BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider);
            i = R.raw.error;
            i2 = R.string.UnknownError;
            createSimpleBulletin = of.createSimpleBulletin(i, LocaleController.getString(i2));
            createSimpleBulletin.show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$22(final TL_stories$StoryItem tL_stories$StoryItem, final boolean z, final Theme.ResourcesProvider resourcesProvider, View view) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(tL_stories$StoryItem);
            MessagesController.getInstance(PeerStoriesView.this.currentAccount).getStoriesController().updateStoriesPinned(PeerStoriesView.this.dialogId, arrayList, z, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda29
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    PeerStoriesView.8.this.lambda$onCreate$21(tL_stories$StoryItem, z, resourcesProvider, (Boolean) obj);
                }
            });
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$23(View view) {
            PeerStoriesView.this.saveToGallery();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$24(View view) {
            AndroidUtilities.addToClipboard(PeerStoriesView.this.currentStory.createLink());
            PeerStoriesView.this.onLinkCopied();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$25(View view) {
            PeerStoriesView.this.shareStory(false);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$26(View view) {
            PeerStoriesView.this.deleteStory();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$27(String str, Theme.ResourcesProvider resourcesProvider, TLObject tLObject, String str2, View view) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(PeerStoriesView.this.currentAccount).edit();
            edit.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + str, false).apply();
            NotificationsController.getInstance(PeerStoriesView.this.currentAccount).updateServerNotificationsSettings(PeerStoriesView.this.dialogId, 0L);
            BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createUsersBulletin(Arrays.asList(tLObject), AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsStoryMutedHint", R.string.NotificationsStoryMutedHint, str2))).setTag(2).show();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$28(String str, Theme.ResourcesProvider resourcesProvider, TLObject tLObject, String str2, View view) {
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(PeerStoriesView.this.currentAccount).edit();
            edit.putBoolean(NotificationsSettingsFacade.PROPERTY_STORIES_NOTIFY + str, true).apply();
            NotificationsController.getInstance(PeerStoriesView.this.currentAccount).updateServerNotificationsSettings(PeerStoriesView.this.dialogId, 0L);
            BulletinFactory.of(PeerStoriesView.this.storyContainer, resourcesProvider).createUsersBulletin(Arrays.asList(tLObject), AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsStoryUnmutedHint", R.string.NotificationsStoryUnmutedHint, str2))).setTag(2).show();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$29(View view) {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.toggleArchiveForStory(peerStoriesView.dialogId);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$3(View view) {
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
        public /* synthetic */ void lambda$onCreate$30(View view) {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.toggleArchiveForStory(peerStoriesView.dialogId);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$31(View view) {
            PeerStoriesView.this.saveToGallery();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$32(StoryViewer storyViewer) {
            PeerStoriesView.this.delegate.showDialog(new PremiumFeatureBottomSheet(storyViewer.fragment, 14, false));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$33(ActionBarMenuSubItem actionBarMenuSubItem, final StoryViewer storyViewer, View view) {
            actionBarMenuSubItem.performHapticFeedback(3);
            BulletinFactory global = BulletinFactory.global();
            if (global != null) {
                global.createSimpleBulletin(R.raw.ic_save_to_gallery, AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.SaveStoryToGalleryPremiumHint), new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda35
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.8.this.lambda$onCreate$32(storyViewer);
                    }
                })).show();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$34(View view) {
            AndroidUtilities.addToClipboard(PeerStoriesView.this.currentStory.createLink());
            PeerStoriesView.this.onLinkCopied();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$35(View view) {
            PeerStoriesView.this.shareStory(false);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$36(View view) {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.currentStory.storyItem.translated = false;
            StoriesStorage storiesStorage = MessagesController.getInstance(peerStoriesView.currentAccount).getStoriesController().getStoriesStorage();
            TL_stories$StoryItem tL_stories$StoryItem = PeerStoriesView.this.currentStory.storyItem;
            storiesStorage.updateStoryItem(tL_stories$StoryItem.dialogId, tL_stories$StoryItem);
            PeerStoriesView.this.cancelTextSelection();
            PeerStoriesView.this.updatePosition();
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$37() {
            Delegate delegate = PeerStoriesView.this.delegate;
            if (delegate != null) {
                delegate.setTranslating(false);
            }
            PeerStoriesView.this.updatePosition();
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.checkBlackoutMode = true;
            peerStoriesView.storyCaptionView.expand(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onCreate$38(Runnable runnable, long j) {
            AndroidUtilities.runOnUIThread(runnable, Math.max(0L, 500 - (System.currentTimeMillis() - j)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$39(View view) {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.currentStory.storyItem.translated = true;
            peerStoriesView.cancelTextSelection();
            Delegate delegate = PeerStoriesView.this.delegate;
            if (delegate != null) {
                delegate.setTranslating(true);
            }
            StoriesStorage storiesStorage = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getStoriesController().getStoriesStorage();
            TL_stories$StoryItem tL_stories$StoryItem = PeerStoriesView.this.currentStory.storyItem;
            storiesStorage.updateStoryItem(tL_stories$StoryItem.dialogId, tL_stories$StoryItem);
            final long currentTimeMillis = System.currentTimeMillis();
            final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda33
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.8.this.lambda$onCreate$37();
                }
            };
            MessagesController.getInstance(PeerStoriesView.this.currentAccount).getTranslateController().translateStory(PeerStoriesView.this.currentStory.storyItem, new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.8.lambda$onCreate$38(runnable, currentTimeMillis);
                }
            });
            PeerStoriesView.this.updatePosition();
            PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
            peerStoriesView2.checkBlackoutMode = true;
            peerStoriesView2.storyCaptionView.expand(true);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$4(StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy, TL_stories$StoryItem tL_stories$StoryItem, View view) {
            PeerStoriesView.this.editPrivacy(storyPrivacy, tL_stories$StoryItem);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$40(StoryViewer storyViewer, Theme.ResourcesProvider resourcesProvider, View view) {
            AlertsCreator.createReportAlert(PeerStoriesView.this.getContext(), PeerStoriesView.this.dialogId, 0, PeerStoriesView.this.currentStory.storyItem.id, storyViewer.fragment, resourcesProvider, null);
            CustomPopupMenu customPopupMenu = PeerStoriesView.this.popupMenu;
            if (customPopupMenu != null) {
                customPopupMenu.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$41(StoryContainsEmojiButton storyContainsEmojiButton, View view) {
            Delegate delegate;
            EmojiPacksAlert alert = storyContainsEmojiButton.getAlert();
            if (alert == null || (delegate = PeerStoriesView.this.delegate) == null) {
                return;
            }
            delegate.showDialog(alert);
            PeerStoriesView.this.popupMenu.dismiss();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$5() {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.editOpened = true;
            peerStoriesView.setActive(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onCreate$6(Runnable runnable) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            AndroidUtilities.runOnUIThread(runnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onCreate$7(Runnable runnable, long j) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            AndroidUtilities.runOnUIThread(runnable, Math.max(0L, 32 - (System.currentTimeMillis() - j)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$8(SharedResources sharedResources, Long l, final Runnable runnable, Boolean bool, Long l2) {
            final long currentTimeMillis = System.currentTimeMillis();
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            VideoPlayerSharedScope videoPlayerSharedScope = peerStoriesView.playerSharedScope;
            StoryViewer.VideoPlayerHolder videoPlayerHolder = videoPlayerSharedScope.player;
            if (videoPlayerHolder == null) {
                peerStoriesView.delegate.setPopupIsVisible(false);
                PeerStoriesView.this.setActive(true);
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                peerStoriesView2.editOpened = false;
                peerStoriesView2.onImageReceiverThumbLoaded = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.8.lambda$onCreate$6(runnable);
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
            videoPlayerHolder.setOnReadyListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.8.lambda$onCreate$7(runnable, currentTimeMillis);
                }
            });
            PeerStoriesView.this.delegate.setPopupIsVisible(false);
            if (PeerStoriesView.this.muteIconView != null) {
                PeerStoriesView.this.muteIconView.setAnimation(sharedResources.muteDrawable);
            }
            PeerStoriesView.this.setActive(((PeerStoriesView.this.videoDuration <= 0 || l.longValue() <= PeerStoriesView.this.videoDuration - 1400) ? l : 0L).longValue(), true);
            PeerStoriesView.this.editOpened = false;
            AndroidUtilities.runOnUIThread(runnable, 400L);
            if (bool.booleanValue()) {
                PeerStoriesView.this.updatePosition();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$9(Activity activity, StoryViewer storyViewer, final SharedResources sharedResources) {
            File file;
            StoryViewer.VideoPlayerHolder videoPlayerHolder;
            StoryRecorder storyRecorder = StoryRecorder.getInstance(activity, PeerStoriesView.this.currentAccount);
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            VideoPlayerSharedScope videoPlayerSharedScope = peerStoriesView.playerSharedScope;
            long j = (videoPlayerSharedScope == null || (videoPlayerHolder = videoPlayerSharedScope.player) == null) ? 0L : videoPlayerHolder.currentPosition;
            DraftsController draftsController = MessagesController.getInstance(peerStoriesView.currentAccount).getStoriesController().getDraftsController();
            TL_stories$StoryItem tL_stories$StoryItem = PeerStoriesView.this.currentStory.storyItem;
            StoryEntry forEdit = draftsController.getForEdit(tL_stories$StoryItem.dialogId, tL_stories$StoryItem);
            if (forEdit == null || forEdit.isRepostMessage || (file = forEdit.file) == null || !file.exists()) {
                forEdit = StoryEntry.fromStoryItem(PeerStoriesView.this.currentStory.getPath(), PeerStoriesView.this.currentStory.storyItem);
                forEdit.editStoryPeerId = PeerStoriesView.this.dialogId;
            }
            StoryEntry copy = forEdit.copy();
            if (PeerStoriesView.this.isBotsPreview()) {
                copy.botId = PeerStoriesView.this.dialogId;
                copy.editingBotPreview = MessagesController.toInputMedia(PeerStoriesView.this.currentStory.storyItem.media);
                StoriesController.StoriesList storiesList = storyViewer.storiesList;
                if (storiesList instanceof StoriesController.BotPreviewsList) {
                    copy.botLang = ((StoriesController.BotPreviewsList) storiesList).lang_code;
                }
            }
            storyRecorder.openEdit(StoryRecorder.SourceView.fromStoryViewer(storyViewer), copy, j, true);
            storyRecorder.setOnFullyOpenListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda41
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.8.this.lambda$onCreate$5();
                }
            });
            storyRecorder.setOnPrepareCloseListener(new Utilities.Callback4() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda42
                @Override // org.telegram.messenger.Utilities.Callback4
                public final void run(Object obj, Object obj2, Object obj3, Object obj4) {
                    PeerStoriesView.8.this.lambda$onCreate$8(sharedResources, (Long) obj, (Runnable) obj2, (Boolean) obj3, (Long) obj4);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDismissed$42() {
            PeerStoriesView.this.delegate.setPopupIsVisible(false);
        }

        /* JADX WARN: Code restructure failed: missing block: B:36:0x010c, code lost:
            if (r9.stories_hidden != false) goto L30;
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x011e, code lost:
            if (r8.stories_hidden != false) goto L30;
         */
        /* JADX WARN: Code restructure failed: missing block: B:46:0x0120, code lost:
            r1 = true;
         */
        @Override // org.telegram.ui.Components.CustomPopupMenu
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onCreate(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout) {
            TL_stories$StoryItem tL_stories$StoryItem;
            TLRPC$MessageMedia tLRPC$MessageMedia;
            TLRPC$Photo tLRPC$Photo;
            TLRPC$User tLRPC$User;
            TLRPC$Chat chat;
            TLRPC$Chat tLRPC$Chat;
            ActionBarMenuSubItem addItem;
            View.OnClickListener onClickListener;
            TLRPC$Chat tLRPC$Chat2;
            TLRPC$User tLRPC$User2;
            ActionBarMenuSubItem addItem2;
            boolean z;
            boolean z2;
            ActionBarMenuSubItem addItem3;
            View.OnClickListener onClickListener2;
            if (this.val$canEditStory || PeerStoriesView.this.currentStory.uploadingStory != null) {
                StoryItemHolder storyItemHolder = PeerStoriesView.this.currentStory;
                final TL_stories$StoryItem tL_stories$StoryItem2 = storyItemHolder.storyItem;
                if (storyItemHolder.uploadingStory != null) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_cancel, LocaleController.getString(R.string.Cancel), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda9
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.8.this.lambda$onCreate$3(view);
                        }
                    });
                }
                if (tL_stories$StoryItem2 == null) {
                    return;
                }
                String string = LocaleController.getString(PeerStoriesView.this.currentStory.isVideo() ? R.string.SaveVideo : R.string.SaveImage);
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                if (peerStoriesView.isSelf) {
                    final StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy = new StoryPrivacyBottomSheet.StoryPrivacy(peerStoriesView.currentAccount, tL_stories$StoryItem2.privacy);
                    ActionBarMenuSubItem addItem4 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_view_file, LocaleController.getString(R.string.WhoCanSee), false, this.val$resourcesProvider);
                    addItem4.setSubtext(storyPrivacy.toString());
                    addItem4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda10
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.8.this.lambda$onCreate$4(storyPrivacy, tL_stories$StoryItem2, view);
                        }
                    });
                    addItem4.setItemHeight(56);
                }
                addSpeedLayout(actionBarPopupWindowLayout, false);
                if (PeerStoriesView.this.isSelf || this.val$speedControl) {
                    View gapView = new ActionBarPopupWindow.GapView(PeerStoriesView.this.getContext(), this.val$resourcesProvider, Theme.key_actionBarDefaultSubmenuSeparator);
                    gapView.setTag(R.id.fit_width_tag, 1);
                    actionBarPopupWindowLayout.addView(gapView, LayoutHelper.createLinear(-1, 8));
                }
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                if (!peerStoriesView2.unsupported && ((peerStoriesView2.isBotsPreview() || MessagesController.getInstance(PeerStoriesView.this.currentAccount).storiesEnabled()) && this.val$userCanEditStory)) {
                    PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                    peerStoriesView3.editStoryItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_edit, LocaleController.getString(peerStoriesView3.isBotsPreview() ? R.string.EditBotPreview : R.string.EditStory), false, this.val$resourcesProvider);
                    ActionBarMenuSubItem actionBarMenuSubItem = PeerStoriesView.this.editStoryItem;
                    final Theme.ResourcesProvider resourcesProvider = this.val$resourcesProvider;
                    final Context context = this.val$context;
                    final StoryViewer storyViewer = this.val$storyViewer;
                    final SharedResources sharedResources = this.val$sharedResources;
                    actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda11
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.8.this.lambda$onCreate$10(resourcesProvider, context, storyViewer, sharedResources, view);
                        }
                    });
                    PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                    if (peerStoriesView4.storiesController.hasUploadingStories(peerStoriesView4.dialogId) && PeerStoriesView.this.currentStory.isVideo && !SharedConfig.allowPreparingHevcPlayers()) {
                        PeerStoriesView.this.editStoryItem.setAlpha(0.5f);
                    }
                }
                StoryItemHolder storyItemHolder2 = PeerStoriesView.this.currentStory;
                if (storyItemHolder2.storyItem != null && storyItemHolder2.isVideo) {
                    PeerStoriesView peerStoriesView5 = PeerStoriesView.this;
                    if (peerStoriesView5.currentStory.storyItem.pinned || peerStoriesView5.isEditBotsPreview()) {
                        ActionBarMenuSubItem addItem5 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.menu_cover_stories, LocaleController.getString(R.string.StoryEditCoverMenu), false, this.val$resourcesProvider);
                        final Context context2 = this.val$context;
                        final StoryViewer storyViewer2 = this.val$storyViewer;
                        final SharedResources sharedResources2 = this.val$sharedResources;
                        addItem5.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda12
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PeerStoriesView.8.this.lambda$onCreate$20(context2, tL_stories$StoryItem2, storyViewer2, sharedResources2, view);
                            }
                        });
                    }
                }
                PeerStoriesView peerStoriesView6 = PeerStoriesView.this;
                if (peerStoriesView6.isSelf || (peerStoriesView6.isChannel && MessagesController.getInstance(peerStoriesView6.currentAccount).getStoriesController().canEditStories(tL_stories$StoryItem2.dialogId))) {
                    final boolean z3 = !tL_stories$StoryItem2.pinned;
                    ActionBarMenuSubItem addItem6 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, z3 ? R.drawable.msg_save_story : R.drawable.menu_unsave_story, LocaleController.getString(PeerStoriesView.this.isSelf ? z3 ? R.string.SaveToProfile : R.string.ArchiveStory : z3 ? R.string.SaveToPosts : R.string.RemoveFromPosts), false, this.val$resourcesProvider);
                    final Theme.ResourcesProvider resourcesProvider2 = this.val$resourcesProvider;
                    addItem6.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda13
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.8.this.lambda$onCreate$22(tL_stories$StoryItem2, z3, resourcesProvider2, view);
                        }
                    });
                }
                addViewStatistics(actionBarPopupWindowLayout, tL_stories$StoryItem2);
                if (!PeerStoriesView.this.unsupported) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_gallery, string, false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda14
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.8.this.lambda$onCreate$23(view);
                        }
                    });
                }
                if (!MessagesController.getInstance(PeerStoriesView.this.currentAccount).premiumFeaturesBlocked()) {
                    PeerStoriesView peerStoriesView7 = PeerStoriesView.this;
                    if (!peerStoriesView7.isChannel) {
                        peerStoriesView7.createStealthModeItem(actionBarPopupWindowLayout);
                    }
                }
                PeerStoriesView peerStoriesView8 = PeerStoriesView.this;
                if (peerStoriesView8.isChannel && peerStoriesView8.allowShareLink) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_link, LocaleController.getString(R.string.CopyLink), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda15
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.8.this.lambda$onCreate$24(view);
                        }
                    });
                }
                if (PeerStoriesView.this.allowShareLink) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_shareout, LocaleController.getString(R.string.BotShare), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda16
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.8.this.lambda$onCreate$25(view);
                        }
                    });
                }
                PeerStoriesView peerStoriesView9 = PeerStoriesView.this;
                if (peerStoriesView9.isSelf || MessagesController.getInstance(peerStoriesView9.currentAccount).getStoriesController().canDeleteStory(PeerStoriesView.this.currentStory.storyItem)) {
                    ActionBarMenuSubItem addItem7 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_delete, LocaleController.getString(R.string.Delete), false, this.val$resourcesProvider);
                    int i = Theme.key_text_RedBold;
                    addItem7.setSelectorColor(Theme.multAlpha(Theme.getColor(i, this.val$resourcesProvider), 0.12f));
                    addItem7.setColors(this.val$resourcesProvider.getColor(i), this.val$resourcesProvider.getColor(i));
                    addItem7.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda17
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.8.this.lambda$onCreate$26(view);
                        }
                    });
                }
            } else {
                addSpeedLayout(actionBarPopupWindowLayout, true);
                final String sharedPrefKey = NotificationsController.getSharedPrefKey(PeerStoriesView.this.dialogId, 0L);
                boolean z4 = !NotificationsCustomSettingsActivity.areStoriesNotMuted(PeerStoriesView.this.currentAccount, PeerStoriesView.this.dialogId);
                int i2 = (PeerStoriesView.this.dialogId > 0L ? 1 : (PeerStoriesView.this.dialogId == 0L ? 0 : -1));
                MessagesController messagesController = MessagesController.getInstance(PeerStoriesView.this.currentAccount);
                PeerStoriesView peerStoriesView10 = PeerStoriesView.this;
                if (i2 > 0) {
                    TLRPC$User user = messagesController.getUser(Long.valueOf(peerStoriesView10.dialogId));
                    chat = null;
                    tLRPC$User = user;
                    tLRPC$Chat = tLRPC$User;
                } else {
                    tLRPC$User = null;
                    chat = messagesController.getChat(Long.valueOf(-peerStoriesView10.dialogId));
                    tLRPC$Chat = chat;
                }
                String trim = tLRPC$User == null ? chat == null ? "" : chat.title : UserObject.getFirstName(tLRPC$User).trim();
                int indexOf = trim.indexOf(" ");
                if (indexOf > 0) {
                    trim = trim.substring(0, indexOf);
                }
                final String str = trim;
                if (!UserObject.isService(PeerStoriesView.this.dialogId) && !PeerStoriesView.this.isBotsPreview()) {
                    if (z4) {
                        tLRPC$Chat2 = chat;
                        tLRPC$User2 = tLRPC$User;
                        addItem2 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_unmute, LocaleController.getString(R.string.NotificationsStoryUnmute2), false, this.val$resourcesProvider);
                        final Theme.ResourcesProvider resourcesProvider3 = this.val$resourcesProvider;
                        final TLRPC$Chat tLRPC$Chat3 = tLRPC$Chat;
                        addItem2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda18
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PeerStoriesView.8.this.lambda$onCreate$28(sharedPrefKey, resourcesProvider3, tLRPC$Chat3, str, view);
                            }
                        });
                    } else {
                        ActionBarMenuSubItem addItem8 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_mute, LocaleController.getString(R.string.NotificationsStoryMute2), false, this.val$resourcesProvider);
                        final Theme.ResourcesProvider resourcesProvider4 = this.val$resourcesProvider;
                        addItem2 = addItem8;
                        tLRPC$Chat2 = chat;
                        final TLRPC$Chat tLRPC$Chat4 = tLRPC$Chat;
                        tLRPC$User2 = tLRPC$User;
                        addItem2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda7
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PeerStoriesView.8.this.lambda$onCreate$27(sharedPrefKey, resourcesProvider4, tLRPC$Chat4, str, view);
                            }
                        });
                    }
                    addItem2.setMultiline(false);
                    if (PeerStoriesView.this.dialogId > 0) {
                        z = tLRPC$User2 != null && tLRPC$User2.contact;
                        if (tLRPC$User2 != null) {
                        }
                        z2 = false;
                    } else {
                        z = (tLRPC$Chat2 == null || ChatObject.isNotInChat(tLRPC$Chat2)) ? false : true;
                        if (tLRPC$Chat2 != null) {
                        }
                        z2 = false;
                    }
                    if (z) {
                        if (z2) {
                            addItem3 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_unarchive, LocaleController.getString(R.string.UnarchiveStories), false, this.val$resourcesProvider);
                            onClickListener2 = new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda21
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    PeerStoriesView.8.this.lambda$onCreate$30(view);
                                }
                            };
                        } else {
                            addItem3 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_archive, LocaleController.getString(R.string.ArchivePeerStories), false, this.val$resourcesProvider);
                            onClickListener2 = new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda20
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    PeerStoriesView.8.this.lambda$onCreate$29(view);
                                }
                            };
                        }
                        addItem3.setOnClickListener(onClickListener2);
                    }
                }
                if (!MessagesController.getInstance(PeerStoriesView.this.currentAccount).premiumFeaturesBlocked() && PeerStoriesView.this.currentStory.isVideo) {
                    PeerStoriesView.this.createQualityItem(actionBarPopupWindowLayout);
                }
                PeerStoriesView peerStoriesView11 = PeerStoriesView.this;
                if (!peerStoriesView11.unsupported && peerStoriesView11.allowShare) {
                    if (UserConfig.getInstance(PeerStoriesView.this.currentAccount).isPremium()) {
                        ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_gallery, LocaleController.getString(R.string.SaveToGallery), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda22
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PeerStoriesView.8.this.lambda$onCreate$31(view);
                            }
                        });
                    } else if (!MessagesController.getInstance(PeerStoriesView.this.currentAccount).premiumFeaturesBlocked()) {
                        Drawable drawable = ContextCompat.getDrawable(this.val$context, R.drawable.msg_gallery_locked2);
                        drawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(-1, -16777216, 0.5f), PorterDuff.Mode.MULTIPLY));
                        CombinedDrawable combinedDrawable = new CombinedDrawable(ContextCompat.getDrawable(this.val$context, R.drawable.msg_gallery_locked1), drawable) { // from class: org.telegram.ui.Stories.PeerStoriesView.8.3
                            @Override // org.telegram.ui.Components.CombinedDrawable, android.graphics.drawable.Drawable
                            public void setColorFilter(ColorFilter colorFilter) {
                            }
                        };
                        final ActionBarMenuSubItem addItem9 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_gallery, LocaleController.getString(R.string.SaveToGallery), false, this.val$resourcesProvider);
                        addItem9.setIcon(combinedDrawable);
                        final StoryViewer storyViewer3 = this.val$storyViewer;
                        addItem9.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda23
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PeerStoriesView.8.this.lambda$onCreate$33(addItem9, storyViewer3, view);
                            }
                        });
                    }
                }
                if (!MessagesController.getInstance(PeerStoriesView.this.currentAccount).premiumFeaturesBlocked()) {
                    PeerStoriesView peerStoriesView12 = PeerStoriesView.this;
                    if (!peerStoriesView12.isChannel) {
                        peerStoriesView12.createStealthModeItem(actionBarPopupWindowLayout);
                    }
                }
                if (PeerStoriesView.this.allowShareLink) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_link2, LocaleController.getString(R.string.CopyLink), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda24
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.8.this.lambda$onCreate$34(view);
                        }
                    });
                }
                if (PeerStoriesView.this.allowShareLink) {
                    ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_shareout, LocaleController.getString(R.string.BotShare), false, this.val$resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda25
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.8.this.lambda$onCreate$35(view);
                        }
                    });
                }
                TL_stories$StoryItem tL_stories$StoryItem3 = PeerStoriesView.this.currentStory.storyItem;
                if (tL_stories$StoryItem3 != null) {
                    if (tL_stories$StoryItem3.translated && TextUtils.equals(tL_stories$StoryItem3.translatedLng, TranslateAlert2.getToLanguage())) {
                        addItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_translate, LocaleController.getString(R.string.HideTranslation), false, this.val$resourcesProvider);
                        onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda26
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PeerStoriesView.8.this.lambda$onCreate$36(view);
                            }
                        };
                    } else if (MessagesController.getInstance(PeerStoriesView.this.currentAccount).getTranslateController().canTranslateStory(PeerStoriesView.this.currentStory.storyItem)) {
                        addItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_translate, LocaleController.getString(R.string.TranslateMessage), false, this.val$resourcesProvider);
                        onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda27
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PeerStoriesView.8.this.lambda$onCreate$39(view);
                            }
                        };
                    }
                    addItem.setOnClickListener(onClickListener);
                }
                addViewStatistics(actionBarPopupWindowLayout, PeerStoriesView.this.currentStory.storyItem);
                PeerStoriesView peerStoriesView13 = PeerStoriesView.this;
                if (!peerStoriesView13.unsupported && !UserObject.isService(peerStoriesView13.dialogId) && !PeerStoriesView.this.isBotsPreview()) {
                    ActionBarMenuSubItem addItem10 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_report, LocaleController.getString(R.string.ReportChat), false, this.val$resourcesProvider);
                    final StoryViewer storyViewer4 = this.val$storyViewer;
                    final Theme.ResourcesProvider resourcesProvider5 = this.val$resourcesProvider;
                    addItem10.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda8
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PeerStoriesView.8.this.lambda$onCreate$40(storyViewer4, resourcesProvider5, view);
                        }
                    });
                }
            }
            StoryItemHolder storyItemHolder3 = PeerStoriesView.this.currentStory;
            boolean z5 = (storyItemHolder3 == null || (tL_stories$StoryItem = storyItemHolder3.storyItem) == null || (tLRPC$MessageMedia = tL_stories$StoryItem.media) == null || (!MessageObject.isDocumentHasAttachedStickers(tLRPC$MessageMedia.document) && ((tLRPC$Photo = PeerStoriesView.this.currentStory.storyItem.media.photo) == null || !tLRPC$Photo.has_stickers))) ? false : true;
            PeerStoriesView peerStoriesView14 = PeerStoriesView.this;
            ArrayList animatedEmojiSets = peerStoriesView14.getAnimatedEmojiSets(peerStoriesView14.currentStory);
            boolean z6 = (animatedEmojiSets == null || animatedEmojiSets.isEmpty()) ? false : true;
            if (z5 || z6) {
                View gapView2 = new ActionBarPopupWindow.GapView(this.val$context, this.val$resourcesProvider, Theme.key_actionBarDefaultSubmenuSeparator);
                int i3 = R.id.fit_width_tag;
                gapView2.setTag(i3, 1);
                actionBarPopupWindowLayout.addView(gapView2, LayoutHelper.createLinear(-1, 8));
                TLRPC$MessageMedia tLRPC$MessageMedia2 = PeerStoriesView.this.currentStory.storyItem.media;
                TLObject tLObject = tLRPC$MessageMedia2.document;
                final StoryContainsEmojiButton storyContainsEmojiButton = new StoryContainsEmojiButton(this.val$context, PeerStoriesView.this.currentAccount, tLObject != null ? tLObject : tLRPC$MessageMedia2.photo, PeerStoriesView.this.currentStory.storyItem, z5, animatedEmojiSets, this.val$resourcesProvider);
                storyContainsEmojiButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda19
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PeerStoriesView.8.this.lambda$onCreate$41(storyContainsEmojiButton, view);
                    }
                });
                storyContainsEmojiButton.setTag(i3, 1);
                actionBarPopupWindowLayout.addView((View) storyContainsEmojiButton, LayoutHelper.createLinear(-1, -2));
            }
        }

        @Override // org.telegram.ui.Components.CustomPopupMenu
        protected void onDismissed() {
            if (!this.edit && !this.val$popupStillVisible[0]) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$8$$ExternalSyntheticLambda28
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.8.this.lambda$onDismissed$42();
                    }
                });
            }
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            peerStoriesView.popupMenu = null;
            peerStoriesView.editStoryItem = null;
        }
    }

    /* loaded from: classes4.dex */
    public interface Delegate {
        int getKeyboardHeight();

        float getProgressToDismiss();

        boolean isClosed();

        void onPeerSelected(long j, int i);

        void preparePlayer(ArrayList arrayList, ArrayList arrayList2);

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

        void setIsInSelectionMode(boolean z);

        void setIsLikesReaction(boolean z);

        void setIsRecording(boolean z);

        void setIsSwiping(boolean z);

        void setIsWaiting(boolean z);

        void setKeyboardVisible(boolean z);

        void setPopupIsVisible(boolean z);

        void setTranslating(boolean z);

        void shouldSwitchToNext();

        void showDialog(Dialog dialog);

        void switchToNextAndRemoveCurrentPeer();
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
            SimpleTextView simpleTextView = new SimpleTextView(context) { // from class: org.telegram.ui.Stories.PeerStoriesView.PeerHeaderView.2
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.ActionBar.SimpleTextView, android.view.View
                public void onMeasure(int i, int i2) {
                    super.onMeasure(i, i2);
                    setPivotY(getMeasuredHeight() / 2.0f);
                }
            };
            this.titleView = simpleTextView;
            simpleTextView.setTextSize(14);
            this.titleView.setTypeface(AndroidUtilities.bold());
            this.titleView.setMaxLines(1);
            this.titleView.setEllipsizeByGradient(AndroidUtilities.dp(4.0f));
            this.titleView.setPivotX(0.0f);
            NotificationCenter.listenEmojiLoading(this.titleView);
            addView(this.titleView, LayoutHelper.createFrame(-2, -2.0f, 0, 54.0f, 0.0f, 86.0f, 0.0f));
            for (int i = 0; i < 2; i++) {
                this.subtitleView[i] = new TextView(context);
                this.subtitleView[i].setTextSize(1, 12.0f);
                this.subtitleView[i].setMaxLines(1);
                this.subtitleView[i].setSingleLine(true);
                this.subtitleView[i].setEllipsize(TextUtils.TruncateAt.MIDDLE);
                this.subtitleView[i].setTextColor(-1);
                this.subtitleView[i].setPadding(AndroidUtilities.dp(3.0f), 0, AndroidUtilities.dp(3.0f), AndroidUtilities.dp(1.0f));
                addView(this.subtitleView[i], LayoutHelper.createFrame(-2, -2.0f, 0, 51.0f, 18.0f, 83.0f, 0.0f));
            }
            this.titleView.setTextColor(-1);
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
            if (storyItemHolder == null || (uploadingStory = storyItemHolder.uploadingStory) == null || uploadingStory.failed) {
                if (this.uploading) {
                    this.uploading = false;
                    this.uploadedTooFast = this.radialProgress.getAnimatedProgress() < 0.2f;
                }
                if (!this.uploadedTooFast) {
                    this.progressToUploading = Utilities.clamp(this.progressToUploading - ((1000.0f / AndroidUtilities.screenRefreshRate) / 300.0f), 1.0f, 0.0f);
                }
                f2 = 1.0f;
                z2 = true;
            } else {
                this.progressToUploading = 1.0f;
                f2 = uploadingStory.progress;
                if (!this.uploading) {
                    this.uploading = true;
                }
                z2 = false;
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
                    Paint unreadCirclePaint = StoriesUtilities.getUnreadCirclePaint(imageReceiver, false);
                    unreadCirclePaint.setAlpha((int) (this.progressToUploading * 255.0f));
                    this.radialProgress.setPaint(unreadCirclePaint);
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

        public void setOnSubtitleClick(View.OnClickListener onClickListener) {
            this.subtitleView[0].setOnClickListener(onClickListener);
            this.subtitleView[0].setClickable(onClickListener != null);
            this.subtitleView[0].setBackground(onClickListener == null ? null : Theme.createSelectorDrawable(822083583, 7));
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
            if (!z) {
                this.subtitleView[0].setVisibility(0);
                this.subtitleView[0].setAlpha(1.0f);
                this.subtitleView[0].setText(charSequence);
                this.subtitleView[1].setVisibility(8);
                this.subtitleView[1].setAlpha(0.0f);
                return;
            }
            this.subtitleView[1].setOnClickListener(null);
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
            this.subtitleAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.PeerHeaderView.3
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
        }
    }

    /* loaded from: classes4.dex */
    public static class SharedResources {
        public final Paint barPaint;
        private final Drawable bottomOverlayGradient;
        public Drawable deleteDrawable;
        private final Paint gradientBackgroundPaint;
        public final Drawable imageBackgroundDrawable;
        public Drawable likeDrawable;
        public Drawable likeDrawableFilled;
        public RLottieDrawable muteDrawable;
        public RLottieDrawable noSoundDrawable;
        public Drawable optionsDrawable;
        public Drawable repostDrawable;
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
            this.likeDrawable = ContextCompat.getDrawable(context, R.drawable.media_like);
            this.repostDrawable = ContextCompat.getDrawable(context, R.drawable.media_repost);
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.media_like_active);
            this.likeDrawableFilled = drawable;
            drawable.setColorFilter(new PorterDuffColorFilter(-53704, PorterDuff.Mode.MULTIPLY));
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
                return;
            }
            if (z) {
                if (this.muteDrawable.getCurrentFrame() > 20) {
                    this.muteDrawable.setCurrentFrame(0, false);
                }
                this.muteDrawable.setCustomEndFrame(20);
            } else if (this.muteDrawable.getCurrentFrame() == 0 || this.muteDrawable.getCurrentFrame() >= 43) {
                return;
            } else {
                this.muteDrawable.setCustomEndFrame(43);
            }
            this.muteDrawable.start();
        }
    }

    /* loaded from: classes4.dex */
    public class StoryItemHolder {
        public CharSequence caption;
        public boolean captionTranslated;
        public TL_stories$StoryItem editingSourceItem;
        private boolean isVideo;
        private StoryCaptionView.Reply reply;
        boolean skipped;
        public TL_stories$StoryItem storyItem = null;
        public StoriesController.UploadingStory uploadingStory = null;

        public StoryItemHolder() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getMediaDebugString() {
            StringBuilder sb;
            TLRPC$MessageMedia tLRPC$MessageMedia;
            int i;
            TL_stories$StoryItem tL_stories$StoryItem = this.storyItem;
            if (tL_stories$StoryItem != null && (tLRPC$MessageMedia = tL_stories$StoryItem.media) != null) {
                if (tLRPC$MessageMedia.photo != null) {
                    sb = new StringBuilder();
                    sb.append("photo#");
                    sb.append(this.storyItem.media.photo.id);
                    sb.append("at");
                    i = this.storyItem.media.photo.dc_id;
                } else if (tLRPC$MessageMedia.document == null) {
                    return "unknown";
                } else {
                    sb = new StringBuilder();
                    sb.append("doc#");
                    sb.append(this.storyItem.media.document.id);
                    sb.append("at");
                    i = this.storyItem.media.document.dc_id;
                }
                sb.append(i);
                sb.append("dc");
            } else if (this.uploadingStory == null) {
                return "unknown";
            } else {
                sb = new StringBuilder();
                sb.append("uploading from ");
                sb.append(this.uploadingStory.path);
            }
            return sb.toString();
        }

        private boolean isVideoInternal() {
            String str;
            TLRPC$MessageMedia tLRPC$MessageMedia;
            StoriesController.UploadingStory uploadingStory = this.uploadingStory;
            if (uploadingStory != null) {
                return uploadingStory.isVideo;
            }
            TL_stories$StoryItem tL_stories$StoryItem = this.storyItem;
            if (tL_stories$StoryItem != null && (tLRPC$MessageMedia = tL_stories$StoryItem.media) != null && tLRPC$MessageMedia.getDocument() != null) {
                TLRPC$Document document = this.storyItem.media.getDocument();
                return MessageObject.isVideoDocument(document) || "video/mp4".equals(document.mime_type);
            }
            TL_stories$StoryItem tL_stories$StoryItem2 = this.storyItem;
            if (tL_stories$StoryItem2 == null || tL_stories$StoryItem2.media != null || (str = tL_stories$StoryItem2.attachPath) == null) {
                return false;
            }
            return str.toLowerCase().endsWith(".mp4");
        }

        public boolean allowScreenshots() {
            StoriesController.UploadingStory uploadingStory = this.uploadingStory;
            if (uploadingStory != null) {
                return uploadingStory.entry.allowScreenshots;
            }
            TL_stories$StoryItem tL_stories$StoryItem = this.storyItem;
            if (tL_stories$StoryItem != null) {
                if (tL_stories$StoryItem.noforwards) {
                    return false;
                }
                if (tL_stories$StoryItem.pinned) {
                    TLRPC$Chat chat = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getChat(Long.valueOf(-tL_stories$StoryItem.dialogId));
                    return chat == null || !chat.noforwards;
                }
                return true;
            }
            return true;
        }

        void cancelOrDelete() {
            TL_stories$StoryItem tL_stories$StoryItem = this.storyItem;
            if (tL_stories$StoryItem instanceof StoriesController.BotPreview) {
                ((StoriesController.BotPreview) tL_stories$StoryItem).list.delete(tL_stories$StoryItem.media);
            } else if (tL_stories$StoryItem != null) {
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                peerStoriesView.storiesController.deleteStory(peerStoriesView.dialogId, this.storyItem);
            } else {
                StoriesController.UploadingStory uploadingStory = this.uploadingStory;
                if (uploadingStory != null) {
                    uploadingStory.cancel();
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x0054, code lost:
            if (r0 <= r1.storiesController.dialogIdToMaxReadId.get(r1.dialogId, 0)) goto L30;
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x005a, code lost:
            if (r6.this$0.isSelf != false) goto L19;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void checkSendView() {
            TL_stories$StoryItem tL_stories$StoryItem;
            TLRPC$UserFull userFull;
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            TL_stories$PeerStories tL_stories$PeerStories = peerStoriesView.userStories;
            if (tL_stories$PeerStories == null && (tL_stories$PeerStories = peerStoriesView.storiesController.getStories(peerStoriesView.dialogId)) == null && (userFull = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUserFull(PeerStoriesView.this.dialogId)) != null) {
                tL_stories$PeerStories = userFull.stories;
            }
            if (PeerStoriesView.this.isActive && (tL_stories$StoryItem = this.storyItem) != null && tL_stories$PeerStories != null) {
                if (!StoriesUtilities.hasExpiredViews(tL_stories$StoryItem)) {
                    int i = this.storyItem.id;
                    if (i <= tL_stories$PeerStories.max_read_id) {
                        PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                    }
                    if (PeerStoriesView.this.storyViewer.overrideUserStories != null) {
                        PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                        if (!peerStoriesView3.storiesController.markStoryAsRead(peerStoriesView3.storyViewer.overrideUserStories, this.storyItem, true)) {
                            return;
                        }
                    } else {
                        PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                        if (!peerStoriesView4.storiesController.markStoryAsRead(peerStoriesView4.dialogId, this.storyItem)) {
                            return;
                        }
                    }
                    PeerStoriesView.this.storyViewer.unreadStateChanged = true;
                }
            }
            PeerStoriesView peerStoriesView5 = PeerStoriesView.this;
            if (!peerStoriesView5.isActive || this.storyItem == null || peerStoriesView5.storyViewer.storiesList == null || !PeerStoriesView.this.storyViewer.storiesList.markAsRead(this.storyItem.id)) {
                return;
            }
            PeerStoriesView.this.storyViewer.unreadStateChanged = true;
        }

        public void clear() {
            this.uploadingStory = null;
            this.storyItem = null;
        }

        public String createLink() {
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            if (peerStoriesView.currentStory.storyItem == null) {
                return null;
            }
            int i = (peerStoriesView.dialogId > 0L ? 1 : (peerStoriesView.dialogId == 0L ? 0 : -1));
            MessagesController messagesController = MessagesController.getInstance(PeerStoriesView.this.currentAccount);
            long j = PeerStoriesView.this.dialogId;
            if (i > 0) {
                TLRPC$User user = messagesController.getUser(Long.valueOf(j));
                if (UserObject.getPublicUsername(user) == null) {
                    return null;
                }
                return String.format(Locale.US, "https://t.me/%1$s/s/%2$s", UserObject.getPublicUsername(user), Integer.valueOf(PeerStoriesView.this.currentStory.storyItem.id));
            }
            TLRPC$Chat chat = messagesController.getChat(Long.valueOf(-j));
            if (ChatObject.getPublicUsername(chat) == null) {
                return null;
            }
            return String.format(Locale.US, "https://t.me/%1$s/s/%2$s", ChatObject.getPublicUsername(chat), Integer.valueOf(PeerStoriesView.this.currentStory.storyItem.id));
        }

        public String getLocalPath() {
            TL_stories$StoryItem tL_stories$StoryItem = this.storyItem;
            if (tL_stories$StoryItem != null) {
                return tL_stories$StoryItem.attachPath;
            }
            return null;
        }

        public File getPath() {
            TLRPC$Photo tLRPC$Photo;
            if (getLocalPath() != null) {
                return new File(getLocalPath());
            }
            TL_stories$StoryItem tL_stories$StoryItem = this.storyItem;
            if (tL_stories$StoryItem != null) {
                TLRPC$MessageMedia tLRPC$MessageMedia = tL_stories$StoryItem.media;
                if (tLRPC$MessageMedia == null || tLRPC$MessageMedia.getDocument() == null) {
                    TLRPC$MessageMedia tLRPC$MessageMedia2 = this.storyItem.media;
                    if (tLRPC$MessageMedia2 == null || (tLRPC$Photo = tLRPC$MessageMedia2.photo) == null) {
                        return null;
                    }
                    TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, ConnectionsManager.DEFAULT_DATACENTER_ID);
                    File pathToAttach = FileLoader.getInstance(PeerStoriesView.this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, true);
                    return !pathToAttach.exists() ? FileLoader.getInstance(PeerStoriesView.this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, false) : pathToAttach;
                }
                return FileLoader.getInstance(PeerStoriesView.this.currentAccount).getPathToAttach(this.storyItem.media.getDocument());
            }
            return null;
        }

        public StoryCaptionView.Reply getReply() {
            StoryCaptionView.Reply from;
            if (this.reply == null) {
                if (this.storyItem != null) {
                    from = StoryCaptionView.Reply.from(PeerStoriesView.this.currentAccount, this.storyItem);
                } else {
                    StoriesController.UploadingStory uploadingStory = this.uploadingStory;
                    if (uploadingStory != null) {
                        from = StoryCaptionView.Reply.from(uploadingStory);
                    }
                }
                this.reply = from;
            }
            return this.reply;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean hasSound() {
            TLRPC$MessageMedia tLRPC$MessageMedia;
            TLRPC$Document document;
            if (this.isVideo) {
                TL_stories$StoryItem tL_stories$StoryItem = this.storyItem;
                if (tL_stories$StoryItem == null || (tLRPC$MessageMedia = tL_stories$StoryItem.media) == null || (document = tLRPC$MessageMedia.getDocument()) == null) {
                    StoriesController.UploadingStory uploadingStory = this.uploadingStory;
                    if (uploadingStory != null) {
                        return !uploadingStory.entry.muted;
                    }
                    return true;
                }
                for (int i = 0; i < document.attributes.size(); i++) {
                    TLRPC$DocumentAttribute tLRPC$DocumentAttribute = document.attributes.get(i);
                    if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) && tLRPC$DocumentAttribute.nosound) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isVideo() {
            return this.isVideo;
        }

        void set(TL_stories$StoryItem tL_stories$StoryItem) {
            this.storyItem = tL_stories$StoryItem;
            this.reply = null;
            this.uploadingStory = null;
            this.skipped = tL_stories$StoryItem instanceof TL_stories$TL_storyItemSkipped;
            this.isVideo = isVideoInternal();
        }

        void set(StoriesController.UploadingStory uploadingStory) {
            this.uploadingStory = uploadingStory;
            this.reply = null;
            this.storyItem = null;
            this.skipped = false;
            this.isVideo = isVideoInternal();
        }

        public void updateCaption() {
            SpannableStringBuilder valueOf;
            ArrayList arrayList;
            int i = 0;
            this.captionTranslated = false;
            PeerStoriesView peerStoriesView = PeerStoriesView.this;
            StoryItemHolder storyItemHolder = peerStoriesView.currentStory;
            StoriesController.UploadingStory uploadingStory = storyItemHolder.uploadingStory;
            if (uploadingStory != null) {
                CharSequence charSequence = uploadingStory.entry.caption;
                this.caption = charSequence;
                CharSequence replaceEmoji = Emoji.replaceEmoji(charSequence, peerStoriesView.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                this.caption = replaceEmoji;
                SpannableStringBuilder valueOf2 = SpannableStringBuilder.valueOf(replaceEmoji);
                TLRPC$User user = MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId));
                if (PeerStoriesView.this.dialogId < 0 || MessagesController.getInstance(PeerStoriesView.this.currentAccount).storyEntitiesAllowed(user)) {
                    MessageObject.addLinks(true, valueOf2);
                    return;
                }
                return;
            }
            TL_stories$StoryItem tL_stories$StoryItem = storyItemHolder.storyItem;
            if (tL_stories$StoryItem != null) {
                if (tL_stories$StoryItem.translated && tL_stories$StoryItem.translatedText != null && TextUtils.equals(tL_stories$StoryItem.translatedLng, TranslateAlert2.getToLanguage())) {
                    this.captionTranslated = true;
                    PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                    TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities = peerStoriesView2.currentStory.storyItem.translatedText;
                    String str = tLRPC$TL_textWithEntities.text;
                    this.caption = str;
                    CharSequence replaceEmoji2 = Emoji.replaceEmoji((CharSequence) str, peerStoriesView2.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    this.caption = replaceEmoji2;
                    if (replaceEmoji2 == null || tLRPC$TL_textWithEntities.entities == null) {
                        return;
                    }
                    valueOf = SpannableStringBuilder.valueOf(MessageObject.replaceAnimatedEmoji(new SpannableStringBuilder(tLRPC$TL_textWithEntities.text), tLRPC$TL_textWithEntities.entities, PeerStoriesView.this.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), false));
                    SpannableStringBuilder.valueOf(Emoji.replaceEmoji(valueOf, PeerStoriesView.this.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), false));
                    i = (PeerStoriesView.this.dialogId < 0 || MessagesController.getInstance(PeerStoriesView.this.currentAccount).storyEntitiesAllowed(MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId)))) ? 1 : 1;
                    if (i != 0) {
                        MessageObject.addLinks(true, valueOf);
                    }
                    arrayList = tLRPC$TL_textWithEntities.entities;
                } else {
                    PeerStoriesView peerStoriesView3 = PeerStoriesView.this;
                    String str2 = peerStoriesView3.currentStory.storyItem.caption;
                    this.caption = str2;
                    CharSequence replaceEmoji3 = Emoji.replaceEmoji((CharSequence) str2, peerStoriesView3.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    this.caption = replaceEmoji3;
                    if (replaceEmoji3 == null || PeerStoriesView.this.currentStory.storyItem.entities == null) {
                        return;
                    }
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(PeerStoriesView.this.currentStory.storyItem.caption);
                    PeerStoriesView peerStoriesView4 = PeerStoriesView.this;
                    valueOf = SpannableStringBuilder.valueOf(MessageObject.replaceAnimatedEmoji(spannableStringBuilder, peerStoriesView4.currentStory.storyItem.entities, peerStoriesView4.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), false));
                    SpannableStringBuilder.valueOf(Emoji.replaceEmoji(valueOf, PeerStoriesView.this.storyCaptionView.captionTextview.getPaint().getFontMetricsInt(), false));
                    i = (PeerStoriesView.this.dialogId < 0 || MessagesController.getInstance(PeerStoriesView.this.currentAccount).storyEntitiesAllowed(MessagesController.getInstance(PeerStoriesView.this.currentAccount).getUser(Long.valueOf(PeerStoriesView.this.dialogId)))) ? 1 : 1;
                    if (i != 0) {
                        MessageObject.addLinks(true, valueOf);
                    }
                    arrayList = PeerStoriesView.this.currentStory.storyItem.entities;
                }
                MessageObject.addEntitiesToText(valueOf, arrayList, false, true, true, false, i ^ 1);
                this.caption = valueOf;
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class VideoPlayerSharedScope {
        boolean firstFrameRendered;
        StoryViewer.VideoPlayerHolder player;
        View renderView;
        SurfaceView surfaceView;
        TextureView textureView;
        ArrayList viewsToInvalidate = new ArrayList();

        public void invalidate() {
            for (int i = 0; i < this.viewsToInvalidate.size(); i++) {
                ((View) this.viewsToInvalidate.get(i)).invalidate();
            }
        }

        public boolean isBuffering() {
            StoryViewer.VideoPlayerHolder videoPlayerHolder = this.player;
            return videoPlayerHolder != null && videoPlayerHolder.isBuffering();
        }
    }

    public PeerStoriesView(final Context context, final StoryViewer storyViewer, final SharedResources sharedResources, final Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.allowDrawSurface = true;
        this.preloadReactionHolders = new ArrayList();
        this.shiftDp = -5;
        this.alpha = 1.0f;
        this.previousSelectedPotision = -1;
        StoryItemHolder storyItemHolder = new StoryItemHolder();
        this.currentStory = storyItemHolder;
        this.progressToKeyboard = -1.0f;
        this.progressToDismiss = -1.0f;
        this.lastAnimatingKeyboardHeight = -1.0f;
        this.classGuid = ConnectionsManager.generateClassGuid();
        this.progressToHideInterface = new AnimatedFloat(this);
        this.linesAlpha = new AnimatedFloat(this);
        this.pinchToZoomHelper = new PinchToZoomHelper();
        this.muteIconViewAlpha = 1.0f;
        this.updateStealthModeTimer = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.this.lambda$new$37();
            }
        };
        this.showTapToSoundHint = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.this.lambda$new$42();
            }
        };
        this.uriesToPrepare = new ArrayList();
        this.documentsToPrepare = new ArrayList();
        this.allowDrawSurfaceRunnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView.31
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
            public void onZoomFinished(MessageObject messageObject) {
                PeerStoriesView.this.delegate.setIsInPinchToZoom(false);
            }

            @Override // org.telegram.ui.PinchToZoomHelper.Callback
            public void onZoomStarted(MessageObject messageObject) {
                PeerStoriesView.this.delegate.setIsInPinchToZoom(true);
            }
        });
        this.playerSharedScope = new VideoPlayerSharedScope();
        this.notificationsLocker = new AnimationNotificationsLocker();
        this.storyItems = new ArrayList();
        this.uploadingStories = new ArrayList();
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
        ImageReceiver imageReceiver2 = new ImageReceiver(this);
        this.reactionEffectImageReceiver = imageReceiver2;
        imageReceiver2.setAllowLoadingOnAttachedOnly(true);
        ImageReceiver imageReceiver3 = this.reactionEffectImageReceiver;
        imageReceiver3.ignoreNotifications = true;
        imageReceiver3.setFileLoadingPriority(3);
        ImageReceiver imageReceiver4 = new ImageReceiver(this);
        this.reactionMoveImageReceiver = imageReceiver4;
        imageReceiver4.setAllowLoadingOnAttachedOnly(true);
        ImageReceiver imageReceiver5 = this.reactionMoveImageReceiver;
        imageReceiver5.ignoreNotifications = true;
        imageReceiver5.setFileLoadingPriority(3);
        ImageReceiver imageReceiver6 = new ImageReceiver();
        this.leftPreloadImageReceiver = imageReceiver6;
        imageReceiver6.setAllowLoadingOnAttachedOnly(true);
        imageReceiver6.ignoreNotifications = true;
        imageReceiver6.setFileLoadingPriority(0);
        ImageReceiver imageReceiver7 = new ImageReceiver();
        this.rightPreloadImageReceiver = imageReceiver7;
        imageReceiver7.setAllowLoadingOnAttachedOnly(true);
        imageReceiver7.ignoreNotifications = true;
        imageReceiver7.setFileLoadingPriority(0);
        imageReceiver.setPreloadingReceivers(Arrays.asList(imageReceiver6, imageReceiver7));
        this.avatarDrawable = new AvatarDrawable();
        this.storyViewer = storyViewer;
        this.sharedResources = sharedResources;
        this.bitmapShaderTools = sharedResources.bitmapShaderTools;
        this.storiesController = MessagesController.getInstance(UserConfig.selectedAccount).getStoriesController();
        sharedResources.dimPaint.setColor(-16777216);
        this.inputBackgroundPaint = new Paint(1);
        this.resourcesProvider = resourcesProvider;
        setClipChildren(false);
        this.storyAreasView = new StoryMediaAreasView(context, this.storyContainer, resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.3
            @Override // org.telegram.ui.Stories.StoryMediaAreasView
            protected Bitmap getPlayingBitmap() {
                return PeerStoriesView.this.getPlayingBitmap();
            }

            @Override // org.telegram.ui.Stories.StoryMediaAreasView
            protected void onHintVisible(boolean z) {
                Delegate delegate = PeerStoriesView.this.delegate;
                if (delegate != null) {
                    delegate.setIsHintVisible(z);
                }
            }

            @Override // org.telegram.ui.Stories.StoryMediaAreasView
            protected void presentFragment(BaseFragment baseFragment) {
                StoryViewer storyViewer2 = storyViewer;
                if (storyViewer2 != null) {
                    storyViewer2.presentFragment(baseFragment);
                }
            }

            @Override // org.telegram.ui.Stories.StoryMediaAreasView
            public void showEffect(StoryReactionWidgetView storyReactionWidgetView) {
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                if (!peerStoriesView.isSelf && peerStoriesView.currentStory.storyItem != null) {
                    ReactionsLayoutInBubble.VisibleReaction fromTL = ReactionsLayoutInBubble.VisibleReaction.fromTL(storyReactionWidgetView.mediaArea.reaction);
                    if (!Objects.equals(fromTL, ReactionsLayoutInBubble.VisibleReaction.fromTL(PeerStoriesView.this.currentStory.storyItem.sent_reaction))) {
                        PeerStoriesView.this.likeStory(fromTL);
                    }
                }
                storyReactionWidgetView.performHapticFeedback(3);
                storyReactionWidgetView.playAnimation();
                PeerStoriesView.this.emojiAnimationsOverlay.showAnimationForWidget(storyReactionWidgetView);
            }
        };
        4 r0 = new 4(context, sharedResources, storyViewer);
        this.storyContainer = r0;
        r0.setClipChildren(false);
        this.emojiAnimationsOverlay = new EmojiAnimationsOverlay(this.storyContainer, this.currentAccount);
        this.storyContainer.addView(this.storyAreasView, LayoutHelper.createFrame(-1, -1.0f));
        5 r10 = new 5(getContext(), storyViewer.resourcesProvider, storyViewer, resourcesProvider);
        this.storyCaptionView = r10;
        r10.captionTextview.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$new$0(view);
            }
        });
        ImageView imageView = new ImageView(context);
        this.shareButton = imageView;
        imageView.setImageDrawable(sharedResources.shareDrawable);
        int dp = AndroidUtilities.dp(8.0f);
        imageView.setPadding(dp, dp, dp, dp);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$new$1(view);
            }
        });
        ScaleStateListAnimator.apply(imageView);
        if (!DISABLE_STORY_REPOSTING) {
            ImageView imageView2 = new ImageView(context);
            this.repostButton = imageView2;
            imageView2.setImageDrawable(sharedResources.repostDrawable);
            this.repostButton.setPadding(dp, dp, dp, dp);
            FrameLayout frameLayout = new FrameLayout(getContext()) { // from class: org.telegram.ui.Stories.PeerStoriesView.6
                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    super.dispatchDraw(canvas);
                    PeerStoriesView peerStoriesView = PeerStoriesView.this;
                    if (!peerStoriesView.isChannel || peerStoriesView.repostCounter == null) {
                        return;
                    }
                    canvas.save();
                    canvas.translate((getMeasuredWidth() - PeerStoriesView.this.repostCounter.getCurrentWidth()) - AndroidUtilities.dp(6.0f), 0.0f);
                    float f = PeerStoriesView.this.repostCounterProgress.set(PeerStoriesView.this.repostCounterVisible ? 1.0f : 0.0f);
                    canvas.scale(f, f, PeerStoriesView.this.repostCounter.getCurrentWidth() / 2.0f, AndroidUtilities.dp(20.0f));
                    PeerStoriesView.this.repostCounter.setAlpha(NotificationCenter.voipServiceCreated);
                    PeerStoriesView.this.repostCounter.draw(canvas);
                    canvas.restore();
                }

                @Override // android.view.View
                protected boolean verifyDrawable(Drawable drawable) {
                    return drawable == PeerStoriesView.this.repostCounter || super.verifyDrawable(drawable);
                }
            };
            this.repostButtonContainer = frameLayout;
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.repostCounter;
            if (animatedTextDrawable != null) {
                animatedTextDrawable.setCallback(frameLayout);
            }
            this.repostButtonContainer.setWillNotDraw(false);
            this.repostButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda10
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PeerStoriesView.this.lambda$new$2(view);
                }
            });
        }
        FrameLayout frameLayout2 = new FrameLayout(getContext()) { // from class: org.telegram.ui.Stories.PeerStoriesView.7
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                if (!peerStoriesView.isChannel || peerStoriesView.reactionsCounter == null) {
                    return;
                }
                canvas.save();
                canvas.translate((getMeasuredWidth() - PeerStoriesView.this.reactionsCounter.getCurrentWidth()) - AndroidUtilities.dp(6.0f), 0.0f);
                float f = PeerStoriesView.this.reactionsCounterProgress.set(PeerStoriesView.this.reactionsCounterVisible ? 1.0f : 0.0f);
                canvas.scale(f, f, PeerStoriesView.this.reactionsCounter.getCurrentWidth() / 2.0f, AndroidUtilities.dp(20.0f));
                PeerStoriesView.this.reactionsCounter.setAlpha(NotificationCenter.voipServiceCreated);
                PeerStoriesView.this.reactionsCounter.draw(canvas);
                canvas.restore();
            }

            @Override // android.view.View
            protected boolean verifyDrawable(Drawable drawable) {
                return drawable == PeerStoriesView.this.reactionsCounter || super.verifyDrawable(drawable);
            }
        };
        this.likeButtonContainer = frameLayout2;
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = this.reactionsCounter;
        if (animatedTextDrawable2 != null) {
            animatedTextDrawable2.setCallback(frameLayout2);
        }
        frameLayout2.setWillNotDraw(false);
        frameLayout2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$new$4(view);
            }
        });
        frameLayout2.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda12
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$new$5;
                lambda$new$5 = PeerStoriesView.this.lambda$new$5(storyViewer, view);
                return lambda$new$5;
            }
        });
        StoriesLikeButton storiesLikeButton = new StoriesLikeButton(context, sharedResources);
        this.storiesLikeButton = storiesLikeButton;
        storiesLikeButton.setPadding(dp, dp, dp, dp);
        frameLayout2.addView(this.storiesLikeButton, LayoutHelper.createFrame(40, 40, 3));
        FrameLayout frameLayout3 = this.repostButtonContainer;
        if (frameLayout3 != null) {
            frameLayout3.addView(this.repostButton, LayoutHelper.createFrame(40, 40, 3));
        }
        ScaleStateListAnimator.apply(frameLayout2, 0.3f, 5.0f);
        FrameLayout frameLayout4 = this.repostButtonContainer;
        if (frameLayout4 != null) {
            ScaleStateListAnimator.apply(frameLayout4, 0.3f, 5.0f);
        }
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
        peerHeaderView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$new$6(storyViewer, view);
            }
        });
        this.storyContainer.addView(this.headerView, LayoutHelper.createFrame(-1, -2.0f, 0, 0.0f, 17.0f, 0.0f, 0.0f));
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(150L);
        layoutTransition.disableTransitionType(2);
        layoutTransition.enableTransitionType(4);
        LinearLayout linearLayout = new LinearLayout(context);
        this.bottomActionsLinearLayout = linearLayout;
        linearLayout.setOrientation(0);
        linearLayout.setLayoutTransition(layoutTransition);
        linearLayout.addView(imageView, LayoutHelper.createLinear(40, 40, 5));
        FrameLayout frameLayout5 = this.repostButtonContainer;
        if (frameLayout5 != null) {
            linearLayout.addView(frameLayout5, LayoutHelper.createLinear(40, 40, 5));
        }
        linearLayout.addView(frameLayout2, LayoutHelper.createLinear(40, 40, 5));
        addView(linearLayout, LayoutHelper.createFrame(-2, -2.0f, 5, 0.0f, 0.0f, 4.0f, 0.0f));
        ImageView imageView3 = new ImageView(context);
        this.optionsIconView = imageView3;
        imageView3.setImageDrawable(sharedResources.optionsDrawable);
        imageView3.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
        imageView3.setBackground(Theme.createSelectorDrawable(-1));
        this.storyContainer.addView(imageView3, LayoutHelper.createFrame(40, 40.0f, 53, 2.0f, 15.0f, 2.0f, 0.0f));
        imageView3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$new$7(resourcesProvider, storyViewer, context, sharedResources, view);
            }
        });
        FrameLayout frameLayout6 = new FrameLayout(context);
        this.muteIconContainer = frameLayout6;
        this.storyContainer.addView(frameLayout6, LayoutHelper.createFrame(40, 40.0f, 53, 2.0f, 15.0f, 42.0f, 0.0f));
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.muteIconView = rLottieImageView;
        rLottieImageView.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f));
        frameLayout6.addView(rLottieImageView);
        ImageView imageView4 = new ImageView(context);
        this.noSoundIconView = imageView4;
        imageView4.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f));
        imageView4.setImageDrawable(sharedResources.noSoundDrawable);
        frameLayout6.addView(imageView4);
        imageView4.setVisibility(8);
        StoryPrivacyButton storyPrivacyButton = new StoryPrivacyButton(context);
        this.privacyButton = storyPrivacyButton;
        storyPrivacyButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$new$9(view);
            }
        });
        this.storyContainer.addView(storyPrivacyButton, LayoutHelper.createFrame(60, 40.0f, 53, 2.0f, 15.0f, 42.0f, 0.0f));
        frameLayout6.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$new$10(storyViewer, view);
            }
        });
        this.storyLines = new StoryLinesDrawable(this, sharedResources);
        this.storyContainer.addView(r10, LayoutHelper.createFrame(-1, -1.0f, 0, 0.0f, 64.0f, 0.0f, 0.0f));
        frameLayout6.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100)));
        imageView3.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100)));
        imageView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100)));
        frameLayout2.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100)));
        FrameLayout frameLayout7 = this.repostButtonContainer;
        if (frameLayout7 != null) {
            frameLayout7.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(20.0f), 0, ColorUtils.setAlphaComponent(-1, 100)));
        }
        View overlayView = r10.textSelectionHelper.getOverlayView(context);
        if (overlayView != null) {
            AndroidUtilities.removeFromParent(overlayView);
            addView(overlayView);
        }
        r10.textSelectionHelper.setCallback(new TextSelectionHelper.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView.9
            @Override // org.telegram.ui.Cells.TextSelectionHelper.Callback
            public void onStateChanged(boolean z) {
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                peerStoriesView.delegate.setIsInSelectionMode(peerStoriesView.storyCaptionView.textSelectionHelper.isInSelectionMode());
            }
        });
        r10.textSelectionHelper.setParentView(this);
    }

    static /* synthetic */ long access$2714(PeerStoriesView peerStoriesView, long j) {
        long j2 = peerStoriesView.currentImageTime + j;
        peerStoriesView.currentImageTime = j2;
        return j2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void afterMessageSend() {
        InstantCameraView instantCameraView = this.instantCameraView;
        if (instantCameraView != null) {
            instantCameraView.resetCameraFile();
            this.instantCameraView.cancel(false);
        }
        this.storyViewer.clearDraft(this.dialogId, this.currentStory.storyItem);
        this.messageSent = true;
        this.storyViewer.closeKeyboardOrEmoji();
        BulletinFactory of = BulletinFactory.of(this.storyContainer, this.resourcesProvider);
        if (of != null) {
            of.createSimpleBulletin(R.raw.forward, LocaleController.getString(R.string.MessageSent), LocaleController.getString(R.string.ViewInChat), 5000, new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda25
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.this.openChat();
                }
            }).hideAfterBottomSheet(false).show(false);
        }
        MessagesController.getInstance(this.currentAccount).ensureMessagesLoaded(this.dialogId, 0, null);
    }

    private void animateLikeButton() {
        final StoriesLikeButton storiesLikeButton = this.storiesLikeButton;
        storiesLikeButton.animate().alpha(0.0f).scaleX(0.8f).scaleY(0.8f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.14
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                AndroidUtilities.removeFromParent(storiesLikeButton);
            }
        }).setDuration(150L).start();
        int dp = AndroidUtilities.dp(8.0f);
        StoriesLikeButton storiesLikeButton2 = new StoriesLikeButton(getContext(), this.sharedResources);
        this.storiesLikeButton = storiesLikeButton2;
        storiesLikeButton2.setPadding(dp, dp, dp, dp);
        this.storiesLikeButton.setAlpha(0.0f);
        this.storiesLikeButton.setScaleX(0.8f);
        this.storiesLikeButton.setScaleY(0.8f);
        this.storiesLikeButton.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(150L);
        this.likeButtonContainer.addView(this.storiesLikeButton, LayoutHelper.createFrame(40, 40, 3));
        this.drawReactionEffect = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean applyMessageToChat(final Runnable runnable) {
        int i = SharedConfig.stealthModeSendMessageConfirm;
        if (i <= 0 || !this.stealthModeIsActive) {
            runnable.run();
        } else {
            int i2 = i - 1;
            SharedConfig.stealthModeSendMessageConfirm = i2;
            SharedConfig.updateStealthModeSendMessageConfirm(i2);
            AlertDialog alertDialog = new AlertDialog(getContext(), 0, this.resourcesProvider);
            alertDialog.setTitle(LocaleController.getString(R.string.StealthModeConfirmTitle));
            alertDialog.setMessage(LocaleController.getString(R.string.StealthModeConfirmMessage));
            alertDialog.setPositiveButton(LocaleController.getString(R.string.Proceed), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda14
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    runnable.run();
                }
            });
            alertDialog.setNegativeButton(LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda15
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();
        }
        return true;
    }

    private void bindInternal(int i) {
        int i2;
        this.deletedPeer = false;
        this.forceUpdateOffsets = true;
        this.userCanSeeViews = false;
        this.isChannel = false;
        this.isGroup = false;
        long j = this.dialogId;
        if (j >= 0) {
            this.isSelf = j == UserConfig.getInstance(this.currentAccount).getClientUserId();
            TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId));
            this.isPremiumBlocked = !UserConfig.getInstance(this.currentAccount).isPremium() && MessagesController.getInstance(this.currentAccount).isUserPremiumBlocked(this.dialogId);
            this.avatarDrawable.setInfo(this.currentAccount, user);
            this.headerView.backupImageView.getImageReceiver().setForUserOrChat(user, this.avatarDrawable);
            if (this.isSelf) {
                this.headerView.titleView.setText(LocaleController.getString(R.string.SelfStoryTitle));
                this.headerView.titleView.setRightDrawable((Drawable) null);
            } else {
                if (user == null || !user.verified) {
                    this.headerView.titleView.setRightDrawable((Drawable) null);
                } else {
                    Drawable mutate = ContextCompat.getDrawable(getContext(), R.drawable.verified_profile).mutate();
                    mutate.setAlpha(NotificationCenter.voipServiceCreated);
                    CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, null);
                    combinedDrawable.setFullsize(true);
                    combinedDrawable.setCustomSize(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    this.headerView.titleView.setRightDrawable(combinedDrawable);
                }
                if (user != null) {
                    this.headerView.titleView.setText(Emoji.replaceEmoji(AndroidUtilities.removeDiacritics(ContactsController.formatName(user)), this.headerView.titleView.getPaint().getFontMetricsInt(), false));
                } else {
                    this.headerView.titleView.setText(null);
                }
            }
        } else {
            this.isSelf = false;
            this.isChannel = true;
            if (this.storiesController.canEditStories(j) || BuildVars.DEBUG_PRIVATE_VERSION) {
                this.userCanSeeViews = true;
            }
            TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
            boolean z = !ChatObject.isChannelAndNotMegaGroup(chat);
            this.isGroup = z;
            if (z && MessagesController.getInstance(this.currentAccount).getChatFull(-this.dialogId) == null) {
                MessagesStorage.getInstance(this.currentAccount).loadChatInfo(-this.dialogId, true, new CountDownLatch(1), false, false);
            }
            this.isPremiumBlocked = this.isGroup && !ChatObject.canSendPlain(chat);
            this.avatarDrawable.setInfo(this.currentAccount, chat);
            this.headerView.backupImageView.getImageReceiver().setForUserOrChat(chat, this.avatarDrawable);
            this.headerView.titleView.setText(AndroidUtilities.removeDiacritics(chat.title));
            if (chat.verified) {
                Drawable mutate2 = ContextCompat.getDrawable(getContext(), R.drawable.verified_profile).mutate();
                mutate2.setAlpha(NotificationCenter.voipServiceCreated);
                CombinedDrawable combinedDrawable2 = new CombinedDrawable(mutate2, null);
                combinedDrawable2.setFullsize(true);
                combinedDrawable2.setCustomSize(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                this.headerView.titleView.setRightDrawable(combinedDrawable2);
            }
            this.headerView.titleView.setRightDrawable((Drawable) null);
        }
        if (this.isActive && (this.isSelf || this.isChannel)) {
            this.storiesController.pollViewsForSelfStories(this.dialogId, true);
        }
        updateStoryItems();
        this.selectedPosition = i;
        if (i < 0) {
            this.selectedPosition = 0;
        }
        this.currentImageTime = 0L;
        this.switchEventSent = false;
        this.boostsStatus = null;
        this.canApplyBoost = null;
        int i3 = 8;
        if (this.isChannel) {
            createSelfPeerView();
            if (this.chatActivityEnterView == null && this.isGroup) {
                createEnterView();
            }
            if (this.chatActivityEnterView != null) {
                TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
                ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
                if (!isBotsPreview() && this.isGroup && (ChatObject.canSendPlain(chat2) || ChatObject.isPossibleRemoveChatRestrictionsByBoosts(chat2))) {
                    i3 = 0;
                }
                chatActivityEnterView.setVisibility(i3);
                this.chatActivityEnterView.getEditField().setText(this.storyViewer.getDraft(this.dialogId, this.currentStory.storyItem));
                this.chatActivityEnterView.setDialogId(this.dialogId, this.currentAccount);
                this.chatActivityEnterView.updateRecordButton(chat2, null);
            }
            if (this.reactionsCounter == null) {
                AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable();
                this.reactionsCounter = animatedTextDrawable;
                animatedTextDrawable.setCallback(this.likeButtonContainer);
                this.reactionsCounter.setTextColor(this.resourcesProvider.getColor(Theme.key_windowBackgroundWhiteBlackText));
                this.reactionsCounter.setTextSize(AndroidUtilities.dp(14.0f));
                this.reactionsCounterProgress = new AnimatedFloat(this.likeButtonContainer);
            }
            if (this.repostButtonContainer != null && this.repostCounter == null) {
                AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2 = new AnimatedTextView.AnimatedTextDrawable();
                this.repostCounter = animatedTextDrawable2;
                animatedTextDrawable2.setCallback(this.repostButtonContainer);
                this.repostCounter.setTextColor(this.resourcesProvider.getColor(Theme.key_windowBackgroundWhiteBlackText));
                this.repostCounter.setTextSize(AndroidUtilities.dp(14.0f));
                this.repostCounterProgress = new AnimatedFloat(this.repostButtonContainer);
            }
            if (i == -1) {
                updateSelectedPosition();
            }
            updatePosition();
            this.count = getStoriesCount();
        } else if (this.isSelf) {
            createSelfPeerView();
            this.selfView.setVisibility(0);
            ChatActivityEnterView chatActivityEnterView2 = this.chatActivityEnterView;
            if (chatActivityEnterView2 != null) {
                chatActivityEnterView2.setVisibility(8);
            }
            if (i == -1) {
                ArrayList arrayList = this.day;
                if (arrayList != null) {
                    int indexOf = arrayList.indexOf(Integer.valueOf(this.storyViewer.dayStoryId));
                    if (indexOf < 0 && !this.day.isEmpty()) {
                        if (this.storyViewer.dayStoryId > ((Integer) this.day.get(0)).intValue()) {
                            indexOf = 0;
                        } else {
                            int i4 = this.storyViewer.dayStoryId;
                            ArrayList arrayList2 = this.day;
                            if (i4 < ((Integer) arrayList2.get(arrayList2.size() - 1)).intValue()) {
                                indexOf = this.day.size() - 1;
                            }
                        }
                    }
                    i2 = Math.max(0, indexOf);
                } else if (this.uploadingStories.isEmpty()) {
                    i2 = 0;
                    while (i2 < this.storyItems.size()) {
                        if (!((TL_stories$StoryItem) this.storyItems.get(i2)).justUploaded && ((TL_stories$StoryItem) this.storyItems.get(i2)).id <= this.storiesController.dialogIdToMaxReadId.get(this.dialogId)) {
                            i2++;
                        }
                    }
                } else {
                    i2 = this.storyItems.size();
                }
                this.selectedPosition = i2;
                break;
            }
            updatePosition();
        } else {
            if (this.chatActivityEnterView == null) {
                createEnterView();
            }
            if (this.isPremiumBlocked && this.premiumBlockedText == null) {
                createPremiumBlockedText();
            }
            if (this.premiumBlockedText != null) {
                if (this.isPremiumBlocked) {
                    updatePremiumBlockedText();
                }
                this.premiumBlockedText.setVisibility(this.isPremiumBlocked ? 0 : 8);
            }
            StoryFailView storyFailView = this.failView;
            if (storyFailView != null) {
                storyFailView.setVisibility(8);
            }
            if (i == -1) {
                updateSelectedPosition();
            }
            updatePosition();
            ChatActivityEnterView chatActivityEnterView3 = this.chatActivityEnterView;
            if (chatActivityEnterView3 != null) {
                chatActivityEnterView3.setVisibility((isBotsPreview() || UserObject.isService(this.dialogId)) ? 8 : 0);
                this.chatActivityEnterView.getEditField().setText(this.storyViewer.getDraft(this.dialogId, this.currentStory.storyItem));
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
        }
        this.storyContainer.invalidate();
        invalidate();
        checkStealthMode(false);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void checkInstantCameraView() {
        if (this.instantCameraView != null) {
            return;
        }
        InstantCameraView instantCameraView = new InstantCameraView(getContext(), new InstantCameraView.Delegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.33
            @Override // org.telegram.ui.Components.InstantCameraView.Delegate
            public int getClassGuid() {
                return PeerStoriesView.this.classGuid;
            }

            @Override // org.telegram.ui.Components.InstantCameraView.Delegate
            public long getDialogId() {
                return PeerStoriesView.this.dialogId;
            }

            @Override // org.telegram.ui.Components.InstantCameraView.Delegate
            public View getFragmentView() {
                return PeerStoriesView.this;
            }

            @Override // org.telegram.ui.Components.InstantCameraView.Delegate
            public Activity getParentActivity() {
                return AndroidUtilities.findActivity(PeerStoriesView.this.getContext());
            }

            @Override // org.telegram.ui.Components.InstantCameraView.Delegate
            public /* synthetic */ boolean isInScheduleMode() {
                return InstantCameraView.Delegate.-CC.$default$isInScheduleMode(this);
            }

            @Override // org.telegram.ui.Components.InstantCameraView.Delegate
            public /* synthetic */ boolean isSecretChat() {
                return InstantCameraView.Delegate.-CC.$default$isSecretChat(this);
            }

            @Override // org.telegram.ui.Components.InstantCameraView.Delegate
            public void sendMedia(MediaController.PhotoEntry photoEntry, VideoEditedInfo videoEditedInfo, boolean z, int i, boolean z2) {
                AccountInstance accountInstance;
                String str;
                VideoEditedInfo videoEditedInfo2;
                MessageObject messageObject;
                MessageObject messageObject2;
                ChatActivity.ReplyQuote replyQuote;
                MessageObject messageObject3;
                String str2;
                if (photoEntry == null) {
                    return;
                }
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                TL_stories$StoryItem tL_stories$StoryItem = peerStoriesView.currentStory.storyItem;
                if (tL_stories$StoryItem == null || (tL_stories$StoryItem instanceof TL_stories$TL_storyItemSkipped)) {
                    return;
                }
                tL_stories$StoryItem.dialogId = peerStoriesView.dialogId;
                if (photoEntry.isVideo) {
                    AccountInstance accountInstance2 = PeerStoriesView.this.getAccountInstance();
                    String str3 = photoEntry.path;
                    long j = PeerStoriesView.this.dialogId;
                    ArrayList<TLRPC$MessageEntity> arrayList = photoEntry.entities;
                    int i2 = photoEntry.ttl;
                    boolean z3 = photoEntry.hasSpoiler;
                    CharSequence charSequence = photoEntry.caption;
                    if (videoEditedInfo != null) {
                        messageObject = null;
                        messageObject2 = null;
                        replyQuote = null;
                        messageObject3 = null;
                        str2 = null;
                        videoEditedInfo2 = videoEditedInfo;
                    } else {
                        videoEditedInfo2 = null;
                        messageObject = null;
                        messageObject2 = null;
                        replyQuote = null;
                        messageObject3 = null;
                        str2 = null;
                    }
                    SendMessagesHelper.prepareSendingVideo(accountInstance2, str3, videoEditedInfo2, j, messageObject, messageObject2, tL_stories$StoryItem, replyQuote, arrayList, i2, messageObject3, z, i, z2, z3, charSequence, str2, 0, 0L);
                } else {
                    if (photoEntry.imagePath != null) {
                        accountInstance = PeerStoriesView.this.getAccountInstance();
                        str = photoEntry.imagePath;
                    } else if (photoEntry.path != null) {
                        accountInstance = PeerStoriesView.this.getAccountInstance();
                        str = photoEntry.path;
                    }
                    String str4 = str;
                    SendMessagesHelper.prepareSendingPhoto(accountInstance, str4, photoEntry.thumbPath, null, PeerStoriesView.this.dialogId, null, null, tL_stories$StoryItem, null, photoEntry.entities, photoEntry.stickers, null, photoEntry.ttl, null, videoEditedInfo, z, i, 0, z2, photoEntry.caption, null, 0, 0L);
                }
                PeerStoriesView.this.afterMessageSend();
            }
        }, this.resourcesProvider);
        this.instantCameraView = instantCameraView;
        instantCameraView.drawBlur = false;
        addView(this.instantCameraView, Math.min(indexOfChild(this.chatActivityEnterView.getRecordCircle()), indexOfChild(this.chatActivityEnterView.controlsView)), LayoutHelper.createFrame(-1, -1, 51));
    }

    private void checkStealthMode(boolean z) {
        ChatActivityEnterView chatActivityEnterView;
        String string;
        if (this.chatActivityEnterView != null && this.isVisible && this.attachedToWindow) {
            AndroidUtilities.cancelRunOnUIThread(this.updateStealthModeTimer);
            TL_stories$TL_storiesStealthMode stealthMode = this.storiesController.getStealthMode();
            if (this.isPremiumBlocked) {
                this.stealthModeIsActive = false;
                this.chatActivityEnterView.setEnabled(false);
                chatActivityEnterView = this.chatActivityEnterView;
                string = " ";
            } else {
                if (stealthMode != null) {
                    int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                    int i = stealthMode.active_until_date;
                    if (currentTime < i) {
                        this.stealthModeIsActive = true;
                        int currentTime2 = i - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                        int i2 = currentTime2 / 60;
                        int i3 = currentTime2 % 60;
                        int i4 = R.string.StealthModeActiveHintShort;
                        Locale locale = Locale.US;
                        String formatString = LocaleController.formatString("StealthModeActiveHint", i4, String.format(locale, "%02d:%02d", 99, 99));
                        this.chatActivityEnterView.setEnabled(true);
                        if (((int) this.chatActivityEnterView.getEditField().getPaint().measureText(formatString)) * 1.2f >= this.chatActivityEnterView.getEditField().getMeasuredWidth()) {
                            this.chatActivityEnterView.setOverrideHint(LocaleController.formatString("StealthModeActiveHintShort", i4, ""), String.format(locale, "%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i3)), z);
                        } else {
                            this.chatActivityEnterView.setOverrideHint(LocaleController.formatString("StealthModeActiveHint", R.string.StealthModeActiveHint, String.format(locale, "%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i3))), z);
                        }
                        AndroidUtilities.runOnUIThread(this.updateStealthModeTimer, 1000L);
                        return;
                    }
                }
                this.stealthModeIsActive = false;
                this.chatActivityEnterView.setEnabled(true);
                chatActivityEnterView = this.chatActivityEnterView;
                string = LocaleController.getString(this.isGroup ? R.string.ReplyToGroupStory : R.string.ReplyPrivately);
            }
            chatActivityEnterView.setOverrideHint(string, z);
        }
    }

    private void createChatAttachView() {
        if (this.chatAttachAlert == null) {
            ChatAttachAlert chatAttachAlert = new ChatAttachAlert(getContext(), null, false, false, true, this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.21
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
            chatAttachAlert.setDelegate(new ChatAttachAlert.ChatAttachViewDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.22
                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public void didPressedButton(int i, boolean z, boolean z2, int i2, long j, boolean z3, boolean z4) {
                    String str;
                    if (PeerStoriesView.this.storyViewer.isShowing) {
                        PeerStoriesView peerStoriesView = PeerStoriesView.this;
                        TL_stories$StoryItem tL_stories$StoryItem = peerStoriesView.currentStory.storyItem;
                        if (tL_stories$StoryItem == null || (tL_stories$StoryItem instanceof TL_stories$TL_storyItemSkipped)) {
                            return;
                        }
                        int i3 = 4;
                        if (i != 8 && i != 7 && (i != 4 || peerStoriesView.chatAttachAlert.getPhotoLayout().getSelectedPhotos().isEmpty())) {
                            if (PeerStoriesView.this.chatAttachAlert != null) {
                                PeerStoriesView.this.chatAttachAlert.dismissWithButtonClick(i);
                                return;
                            }
                            return;
                        }
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
                                    boolean z5 = photoEntry.isVideo;
                                    if ((!z5 && (str = photoEntry.imagePath) != null) || (str = photoEntry.path) != null) {
                                        sendingMediaInfo.path = str;
                                    }
                                    sendingMediaInfo.thumbPath = photoEntry.thumbPath;
                                    sendingMediaInfo.isVideo = z5;
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
                            SendMessagesHelper.prepareSendingMedia(PeerStoriesView.this.getAccountInstance(), arrayList, PeerStoriesView.this.dialogId, null, null, tL_stories$StoryItem, null, i == i3 || z4, z, null, z2, i2, 0, i5 == 0 ? ((SendMessagesHelper.SendingMediaInfo) arrayList.get(i4)).updateStickersOrder : false, null, null, 0, 0L, false);
                            i5++;
                            selectedPhotos = selectedPhotos;
                            selectedPhotosOrder = selectedPhotosOrder;
                            i4 = 0;
                            i3 = 4;
                        }
                        PeerStoriesView.this.chatActivityEnterView.setFieldText("");
                        PeerStoriesView.this.afterMessageSend();
                    }
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public /* synthetic */ void didSelectBot(TLRPC$User tLRPC$User) {
                    ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$didSelectBot(this, tLRPC$User);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public void doOnIdle(Runnable runnable) {
                    NotificationCenter.getInstance(PeerStoriesView.this.currentAccount).doOnIdle(runnable);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public boolean needEnterComment() {
                    return PeerStoriesView.this.needEnterText();
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public void onCameraOpened() {
                    PeerStoriesView.this.chatActivityEnterView.closeKeyboard();
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
                public /* synthetic */ boolean selectItemOnClicking() {
                    return ChatAttachAlert.ChatAttachViewDelegate.-CC.$default$selectItemOnClicking(this);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate
                public void sendAudio(ArrayList arrayList, CharSequence charSequence, boolean z, int i, long j, boolean z2) {
                    PeerStoriesView peerStoriesView = PeerStoriesView.this;
                    TL_stories$StoryItem tL_stories$StoryItem = peerStoriesView.currentStory.storyItem;
                    if (tL_stories$StoryItem == null || (tL_stories$StoryItem instanceof TL_stories$TL_storyItemSkipped)) {
                        return;
                    }
                    SendMessagesHelper.prepareSendingAudioDocuments(peerStoriesView.getAccountInstance(), arrayList, charSequence != null ? charSequence : null, PeerStoriesView.this.dialogId, null, null, tL_stories$StoryItem, z, i, null, null, 0, j, z2);
                    PeerStoriesView.this.afterMessageSend();
                }
            });
            this.chatAttachAlert.getPhotoLayout().loadGalleryPhotos();
            this.chatAttachAlert.setAllowEnterCaption(true);
            this.chatAttachAlert.init();
            this.chatAttachAlert.setDocumentsDelegate(new ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.23
                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public void didSelectFiles(ArrayList arrayList, String str, ArrayList arrayList2, boolean z, int i, long j, boolean z2) {
                    PeerStoriesView peerStoriesView = PeerStoriesView.this;
                    TL_stories$StoryItem tL_stories$StoryItem = peerStoriesView.currentStory.storyItem;
                    if (tL_stories$StoryItem == null || (tL_stories$StoryItem instanceof TL_stories$TL_storyItemSkipped)) {
                        return;
                    }
                    SendMessagesHelper.prepareSendingDocuments(peerStoriesView.getAccountInstance(), arrayList, arrayList, null, str, null, PeerStoriesView.this.dialogId, null, null, tL_stories$StoryItem, null, null, z, i, null, null, 0, 0L, false);
                    PeerStoriesView.this.afterMessageSend();
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public /* synthetic */ void didSelectPhotos(ArrayList arrayList, boolean z, int i) {
                    ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate.-CC.$default$didSelectPhotos(this, arrayList, z, i);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public void startDocumentSelectActivity() {
                    try {
                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
                        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                        intent.setType("*/*");
                        PeerStoriesView.this.storyViewer.startActivityForResult(intent, 21);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public /* synthetic */ void startMusicSelectActivity() {
                    ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate.-CC.$default$startMusicSelectActivity(this);
                }
            });
            this.chatAttachAlert.getCommentTextView().setText(this.chatActivityEnterView.getFieldText());
        }
    }

    private void createEnterView() {
        17 r7 = new 17(AndroidUtilities.findActivity(getContext()), this, null, true, new WrappedResourceProvider(this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.16
            @Override // org.telegram.ui.WrappedResourceProvider
            public void appendColors() {
                this.sparseIntArray.put(Theme.key_chat_emojiPanelBackground, ColorUtils.setAlphaComponent(-1, 30));
            }
        });
        this.chatActivityEnterView = r7;
        r7.getEditField().useAnimatedTextDrawable();
        this.chatActivityEnterView.setOverrideKeyboardAnimation(true);
        this.chatActivityEnterView.setClipChildren(false);
        this.chatActivityEnterView.setDelegate(new 18());
        setDelegate(this.chatActivityEnterView);
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        chatActivityEnterView.shouldDrawBackground = false;
        chatActivityEnterView.shouldDrawRecordedAudioPanelInParent = true;
        chatActivityEnterView.setAllowStickersAndGifs(true, true, true);
        this.chatActivityEnterView.updateColors();
        ChatActivityEnterView chatActivityEnterView2 = this.chatActivityEnterView;
        chatActivityEnterView2.isStories = true;
        addView(chatActivityEnterView2, LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
        this.chatActivityEnterView.recordingGuid = this.classGuid;
        this.playerSharedScope.viewsToInvalidate.add(this.storyContainer);
        this.playerSharedScope.viewsToInvalidate.add(this);
        if (this.attachedToWindow) {
            this.chatActivityEnterView.onResume();
        }
        checkStealthMode(false);
        if (isBotsPreview()) {
            this.chatActivityEnterView.setVisibility(8);
        }
        this.reactionsContainerIndex = getChildCount();
    }

    private void createFailView() {
        if (this.failView != null) {
            return;
        }
        StoryFailView storyFailView = new StoryFailView(getContext(), this.resourcesProvider);
        this.failView = storyFailView;
        storyFailView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda31
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$createFailView$20(view);
            }
        });
        this.failView.setAlpha(0.0f);
        this.failView.setVisibility(8);
        addView(this.failView, LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createMentionsContainer() {
        MentionsContainerView mentionsContainerView = new MentionsContainerView(getContext(), this.dialogId, 0L, this.storyViewer.fragment, this, this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.19
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
        mentionsContainerView.withDelegate(new MentionsContainerView.Delegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.20
            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public void addEmojiToRecent(String str) {
                PeerStoriesView.this.chatActivityEnterView.addEmojiToRecent(str);
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public Paint.FontMetricsInt getFontMetrics() {
                return PeerStoriesView.this.chatActivityEnterView.getEditField().getPaint().getFontMetricsInt();
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public void onStickerSelected(TLRPC$TL_document tLRPC$TL_document, String str, Object obj) {
                SendMessagesHelper.getInstance(PeerStoriesView.this.currentAccount).sendSticker(tLRPC$TL_document, str, PeerStoriesView.this.dialogId, null, null, PeerStoriesView.this.currentStory.storyItem, null, null, true, 0, false, obj, null, 0);
                PeerStoriesView.this.chatActivityEnterView.addStickerToRecent(tLRPC$TL_document);
                PeerStoriesView.this.chatActivityEnterView.setFieldText("");
                PeerStoriesView.this.afterMessageSend();
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public void replaceText(int i, int i2, CharSequence charSequence, boolean z) {
                PeerStoriesView.this.chatActivityEnterView.replaceWithText(i, i2, charSequence, z);
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public void sendBotInlineResult(TLRPC$BotInlineResult tLRPC$BotInlineResult, boolean z, int i) {
                long contextBotId = PeerStoriesView.this.mentionContainer.getAdapter().getContextBotId();
                HashMap hashMap = new HashMap();
                hashMap.put("id", tLRPC$BotInlineResult.id);
                hashMap.put("query_id", "" + tLRPC$BotInlineResult.query_id);
                hashMap.put("bot", "" + contextBotId);
                hashMap.put("bot_name", PeerStoriesView.this.mentionContainer.getAdapter().getContextBotName());
                SendMessagesHelper.prepareSendingBotContextResult(PeerStoriesView.this.storyViewer.fragment, PeerStoriesView.this.getAccountInstance(), tLRPC$BotInlineResult, hashMap, PeerStoriesView.this.dialogId, null, null, PeerStoriesView.this.currentStory.storyItem, null, z, i, null, 0);
                PeerStoriesView.this.chatActivityEnterView.setFieldText("");
                PeerStoriesView.this.afterMessageSend();
                MediaDataController.getInstance(PeerStoriesView.this.currentAccount).increaseInlineRating(contextBotId);
            }
        });
        addView(this.mentionContainer, LayoutHelper.createFrame(-1, -1, 83));
    }

    private void createPremiumBlockedText() {
        if (this.premiumBlockedText != null) {
            return;
        }
        if (this.chatActivityEnterView == null) {
            createEnterView();
        }
        LinearLayout linearLayout = new LinearLayout(getContext());
        this.premiumBlockedText = linearLayout;
        linearLayout.setOrientation(0);
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setScaleX(1.35f);
        imageView.setScaleY(1.35f);
        imageView.setImageResource(R.drawable.mini_switch_lock);
        imageView.setColorFilter(new PorterDuffColorFilter(-8026747, PorterDuff.Mode.SRC_IN));
        TextView textView = new TextView(getContext());
        this.premiumBlockedText1 = textView;
        textView.setTextColor(-8026747);
        this.premiumBlockedText1.setTextSize(1, 16.0f);
        this.premiumBlockedText1.setText(LocaleController.getString(this.isGroup ? R.string.StoryGroupRepliesLocked : R.string.StoryRepliesLocked));
        TextView textView2 = new TextView(getContext());
        this.premiumBlockedText2 = textView2;
        textView2.setTextColor(-1);
        this.premiumBlockedText2.setTextSize(1, 12.0f);
        this.premiumBlockedText2.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(40.0f), 452984831, 855638015));
        this.premiumBlockedText2.setGravity(17);
        ScaleStateListAnimator.apply(this.premiumBlockedText2);
        this.premiumBlockedText2.setText(LocaleController.getString(R.string.StoryRepliesLockedButton));
        this.premiumBlockedText2.setPadding(AndroidUtilities.dp(7.0f), 0, AndroidUtilities.dp(7.0f), 0);
        this.premiumBlockedText.addView(imageView, LayoutHelper.createLinear(22, 22, 16, 12, 1, 4, 0));
        this.premiumBlockedText.addView(this.premiumBlockedText1, LayoutHelper.createLinear(-2, -2, 16));
        this.premiumBlockedText.addView(this.premiumBlockedText2, LayoutHelper.createLinear(-2, 19, 16, 5, 0, 0, 0));
        this.chatActivityEnterView.addView(this.premiumBlockedText, LayoutHelper.createFrame(-1, -1.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createQualityItem(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout) {
        final boolean z = MessagesController.getInstance(this.currentAccount).storyQualityFull;
        if (UserConfig.getInstance(this.currentAccount).isPremium()) {
            ActionBarMenuItem.addItem(actionBarPopupWindowLayout, z ? R.drawable.menu_quality_sd : R.drawable.menu_quality_hd, LocaleController.getString(z ? R.string.StoryQualityDecrease : R.string.StoryQualityIncrease), false, this.resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda39
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PeerStoriesView.this.lambda$createQualityItem$13(z, view);
                }
            });
            return;
        }
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.msg_gallery_locked2);
        drawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(-1, -16777216, 0.5f), PorterDuff.Mode.MULTIPLY));
        CombinedDrawable combinedDrawable = new CombinedDrawable(ContextCompat.getDrawable(getContext(), R.drawable.menu_quality_hd2), drawable) { // from class: org.telegram.ui.Stories.PeerStoriesView.11
            @Override // org.telegram.ui.Components.CombinedDrawable, android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
            }
        };
        combinedDrawable.setIconSize(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
        combinedDrawable.setIconOffset(AndroidUtilities.dp(1.0f), -AndroidUtilities.dp(2.0f));
        ActionBarMenuSubItem addItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.menu_quality_hd, LocaleController.getString(R.string.StoryQualityIncrease), false, this.resourcesProvider);
        addItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda40
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$createQualityItem$15(view);
            }
        });
        addItem.setIcon(combinedDrawable);
    }

    private void createReplyDisabledView() {
        if (this.replyDisabledTextView != null) {
            return;
        }
        TextView textView = new TextView(getContext()) { // from class: org.telegram.ui.Stories.PeerStoriesView.30
            @Override // android.view.View
            public void setTranslationY(float f) {
                super.setTranslationY(f);
            }
        };
        this.replyDisabledTextView = textView;
        textView.setTextSize(1, 14.0f);
        this.replyDisabledTextView.setTextColor(ColorUtils.blendARGB(-16777216, -1, 0.5f));
        this.replyDisabledTextView.setGravity(19);
        this.replyDisabledTextView.setText(LocaleController.getString(R.string.StoryReplyDisabled));
        addView(this.replyDisabledTextView, LayoutHelper.createFrame(-2, 40.0f, 3, 16.0f, 0.0f, 16.0f, 0.0f));
    }

    private void createSelfPeerView() {
        if (this.selfView != null) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(getContext()) { // from class: org.telegram.ui.Stories.PeerStoriesView.28
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
        addView(this.selfView, LayoutHelper.createFrame(-1, 48.0f, 48, 0.0f, 0.0f, 136.0f, 0.0f));
        View view = new View(getContext()) { // from class: org.telegram.ui.Stories.PeerStoriesView.29
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
        view.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda24
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PeerStoriesView.this.lambda$createSelfPeerView$34(view2);
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
    public void createStealthModeItem(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout) {
        if (isBotsPreview()) {
            return;
        }
        if (UserConfig.getInstance(this.currentAccount).isPremium()) {
            ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_stories_stealth2, LocaleController.getString(R.string.StealthModeButton), false, this.resourcesProvider).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda43
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PeerStoriesView.this.lambda$createStealthModeItem$11(view);
                }
            });
            return;
        }
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.msg_gallery_locked2);
        drawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(-1, -16777216, 0.5f), PorterDuff.Mode.MULTIPLY));
        CombinedDrawable combinedDrawable = new CombinedDrawable(ContextCompat.getDrawable(getContext(), R.drawable.msg_stealth_locked), drawable) { // from class: org.telegram.ui.Stories.PeerStoriesView.10
            @Override // org.telegram.ui.Components.CombinedDrawable, android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
            }
        };
        ActionBarMenuSubItem addItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, R.drawable.msg_stories_stealth2, LocaleController.getString(R.string.StealthModeButton), false, this.resourcesProvider);
        addItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda44
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$createStealthModeItem$12(view);
            }
        });
        addItem.setIcon(combinedDrawable);
    }

    private void createUnsupportedContainer() {
        if (this.unsupportedContainer != null) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(getContext());
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        TextView textView = new TextView(getContext());
        textView.setTypeface(AndroidUtilities.bold());
        textView.setGravity(1);
        textView.setTextSize(1, 16.0f);
        textView.setText(LocaleController.getString(R.string.StoryUnsupported));
        textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
        TextView textView2 = new TextView(getContext());
        ScaleStateListAnimator.apply(textView2);
        textView2.setText(LocaleController.getString(R.string.AppUpdate));
        int i = Theme.key_featuredStickers_buttonText;
        textView2.setTextColor(Theme.getColor(i, this.resourcesProvider));
        textView2.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f));
        textView2.setGravity(17);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setTextSize(1, 15.0f);
        textView2.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(8.0f), Theme.getColor(Theme.key_featuredStickers_addButton, this.resourcesProvider), ColorUtils.setAlphaComponent(Theme.getColor(i, this.resourcesProvider), 30)));
        textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda28
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PeerStoriesView.this.lambda$createUnsupportedContainer$33(view);
            }
        });
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2));
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 0.0f, 24.0f, 0.0f, 0.0f));
        frameLayout.addView(linearLayout, LayoutHelper.createFrame(-1, -2.0f, 17, 72.0f, 0.0f, 72.0f, 0.0f));
        this.storyContainer.addView(frameLayout);
        this.unsupportedContainer = frameLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteStory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        builder.setTitle(LocaleController.getString(isBotsPreview() ? R.string.DeleteBotPreviewTitle : R.string.DeleteStoryTitle));
        builder.setMessage(LocaleController.getString(isBotsPreview() ? R.string.DeleteBotPreviewSubtitle : R.string.DeleteStorySubtitle));
        builder.setPositiveButton(LocaleController.getString(R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda35
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PeerStoriesView.this.lambda$deleteStory$35(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda36
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog create = builder.create();
        this.delegate.showDialog(create);
        create.redPositive();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean drawLinesAsCounter() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void editPrivacy(StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy, final TL_stories$StoryItem tL_stories$StoryItem) {
        this.delegate.showDialog(new StoryPrivacyBottomSheet(getContext(), tL_stories$StoryItem.pinned ? ConnectionsManager.DEFAULT_DATACENTER_ID : tL_stories$StoryItem.expire_date - tL_stories$StoryItem.date, this.resourcesProvider).setValue(storyPrivacy).enableSharing(false).isEdit(true).whenSelectedRules(new StoryPrivacyBottomSheet.DoneCallback() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda33
            @Override // org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet.DoneCallback
            public final void done(StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy2, boolean z, boolean z2, TLRPC$InputPeer tLRPC$InputPeer, Runnable runnable) {
                PeerStoriesView.this.lambda$editPrivacy$47(tL_stories$StoryItem, storyPrivacy2, z, z2, tLRPC$InputPeer, runnable);
            }
        }, false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Activity findActivity() {
        Activity findActivity;
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer == null || (findActivity = storyViewer.parentActivity) == null) {
            findActivity = AndroidUtilities.findActivity(getContext());
        }
        return findActivity == null ? LaunchActivity.instance : findActivity;
    }

    private BaseFragment fragmentForLimit() {
        return new BaseFragment() { // from class: org.telegram.ui.Stories.PeerStoriesView.15
            @Override // org.telegram.ui.ActionBar.BaseFragment
            public Activity getParentActivity() {
                return PeerStoriesView.this.findActivity();
            }

            @Override // org.telegram.ui.ActionBar.BaseFragment
            public Theme.ResourcesProvider getResourceProvider() {
                return new WrappedResourceProvider(PeerStoriesView.this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.15.1
                    @Override // org.telegram.ui.WrappedResourceProvider
                    public void appendColors() {
                        this.sparseIntArray.append(Theme.key_dialogBackground, -14737633);
                        this.sparseIntArray.append(Theme.key_windowBackgroundGray, -13421773);
                    }
                };
            }

            @Override // org.telegram.ui.ActionBar.BaseFragment
            public boolean isLightStatusBar() {
                return false;
            }

            @Override // org.telegram.ui.ActionBar.BaseFragment
            public boolean presentFragment(BaseFragment baseFragment) {
                if (PeerStoriesView.this.storyViewer != null) {
                    PeerStoriesView.this.storyViewer.presentFragment(baseFragment);
                    return true;
                }
                return true;
            }

            @Override // org.telegram.ui.ActionBar.BaseFragment
            public Dialog showDialog(Dialog dialog) {
                if (PeerStoriesView.this.storyViewer != null) {
                    PeerStoriesView.this.storyViewer.showDialog(dialog);
                } else if (dialog != null) {
                    dialog.show();
                }
                return dialog;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AccountInstance getAccountInstance() {
        return AccountInstance.getInstance(this.currentAccount);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList getAnimatedEmojiSets(StoryItemHolder storyItemHolder) {
        StoryEntry storyEntry;
        AnimatedEmojiSpan[] animatedEmojiSpanArr;
        TL_stories$MediaArea tL_stories$MediaArea;
        TLRPC$InputStickerSet inputStickerSet;
        ArrayList arrayList;
        TLRPC$InputStickerSet inputStickerSet2;
        if (storyItemHolder != null) {
            HashSet hashSet = new HashSet();
            ArrayList arrayList2 = new ArrayList();
            TL_stories$StoryItem tL_stories$StoryItem = storyItemHolder.storyItem;
            int i = 0;
            if (tL_stories$StoryItem != null && tL_stories$StoryItem.media_areas != null) {
                for (int i2 = 0; i2 < storyItemHolder.storyItem.media_areas.size(); i2++) {
                    TL_stories$MediaArea tL_stories$MediaArea2 = (TL_stories$MediaArea) storyItemHolder.storyItem.media_areas.get(i2);
                    if (tL_stories$MediaArea2 instanceof TL_stories$TL_mediaAreaSuggestedReaction) {
                        TLRPC$Reaction tLRPC$Reaction = tL_stories$MediaArea2.reaction;
                        if (tLRPC$Reaction instanceof TLRPC$TL_reactionCustomEmoji) {
                            TLRPC$Document findDocument = AnimatedEmojiDrawable.findDocument(this.currentAccount, ((TLRPC$TL_reactionCustomEmoji) tLRPC$Reaction).document_id);
                            if (findDocument != null && (inputStickerSet2 = MessageObject.getInputStickerSet(findDocument)) != null && !hashSet.contains(Long.valueOf(inputStickerSet2.id))) {
                                hashSet.add(Long.valueOf(inputStickerSet2.id));
                                arrayList2.add(inputStickerSet2);
                            }
                        }
                    }
                }
            }
            TL_stories$StoryItem tL_stories$StoryItem2 = storyItemHolder.storyItem;
            if (tL_stories$StoryItem2 == null || (arrayList = tL_stories$StoryItem2.entities) == null || arrayList.isEmpty()) {
                StoriesController.UploadingStory uploadingStory = storyItemHolder.uploadingStory;
                if (uploadingStory != null && (storyEntry = uploadingStory.entry) != null) {
                    if (storyEntry.mediaEntities != null) {
                        for (int i3 = 0; i3 < storyItemHolder.uploadingStory.entry.mediaEntities.size(); i3++) {
                            VideoEditedInfo.MediaEntity mediaEntity = (VideoEditedInfo.MediaEntity) storyItemHolder.uploadingStory.entry.mediaEntities.get(i3);
                            if (mediaEntity.type == 4 && (tL_stories$MediaArea = mediaEntity.mediaArea) != null) {
                                TLRPC$Reaction tLRPC$Reaction2 = tL_stories$MediaArea.reaction;
                                if (tLRPC$Reaction2 instanceof TLRPC$TL_reactionCustomEmoji) {
                                    TLRPC$Document findDocument2 = AnimatedEmojiDrawable.findDocument(this.currentAccount, ((TLRPC$TL_reactionCustomEmoji) tLRPC$Reaction2).document_id);
                                    if (findDocument2 != null && (inputStickerSet = MessageObject.getInputStickerSet(findDocument2)) != null && !hashSet.contains(Long.valueOf(inputStickerSet.id))) {
                                        hashSet.add(Long.valueOf(inputStickerSet.id));
                                        arrayList2.add(inputStickerSet);
                                    }
                                }
                            }
                        }
                    }
                    CharSequence charSequence = storyItemHolder.uploadingStory.entry.caption;
                    if (!(charSequence instanceof Spanned) || (animatedEmojiSpanArr = (AnimatedEmojiSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), AnimatedEmojiSpan.class)) == null) {
                        return arrayList2;
                    }
                    while (i < animatedEmojiSpanArr.length) {
                        AnimatedEmojiSpan animatedEmojiSpan = animatedEmojiSpanArr[i];
                        TLRPC$Document tLRPC$Document = animatedEmojiSpan.document;
                        if (tLRPC$Document == null) {
                            tLRPC$Document = AnimatedEmojiDrawable.findDocument(this.currentAccount, animatedEmojiSpan.documentId);
                        }
                        if (tLRPC$Document != null) {
                            TLRPC$InputStickerSet inputStickerSet3 = MessageObject.getInputStickerSet(tLRPC$Document);
                            if (!hashSet.contains(Long.valueOf(inputStickerSet3.id))) {
                                hashSet.add(Long.valueOf(inputStickerSet3.id));
                                arrayList2.add(inputStickerSet3);
                            }
                        }
                        i++;
                    }
                }
            } else {
                while (i < storyItemHolder.storyItem.entities.size()) {
                    TLRPC$MessageEntity tLRPC$MessageEntity = (TLRPC$MessageEntity) storyItemHolder.storyItem.entities.get(i);
                    if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCustomEmoji) {
                        TLRPC$TL_messageEntityCustomEmoji tLRPC$TL_messageEntityCustomEmoji = (TLRPC$TL_messageEntityCustomEmoji) tLRPC$MessageEntity;
                        TLRPC$Document tLRPC$Document2 = tLRPC$TL_messageEntityCustomEmoji.document;
                        if (tLRPC$Document2 == null) {
                            tLRPC$Document2 = AnimatedEmojiDrawable.findDocument(this.currentAccount, tLRPC$TL_messageEntityCustomEmoji.document_id);
                        }
                        if (tLRPC$Document2 != null) {
                            TLRPC$InputStickerSet inputStickerSet4 = MessageObject.getInputStickerSet(tLRPC$Document2);
                            if (!hashSet.contains(Long.valueOf(inputStickerSet4.id))) {
                                hashSet.add(Long.valueOf(inputStickerSet4.id));
                                arrayList2.add(inputStickerSet4);
                            }
                        }
                    }
                    i++;
                }
            }
            return arrayList2;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getHideInterfaceAlpha() {
        return (1.0f - this.progressToHideInterface.get()) * (1.0f - this.storyViewer.getProgressToSelfViews());
    }

    public static int getStoryId(TL_stories$StoryItem tL_stories$StoryItem, StoriesController.UploadingStory uploadingStory) {
        StoryEntry storyEntry;
        if (tL_stories$StoryItem != null) {
            return tL_stories$StoryItem.id;
        }
        if (uploadingStory == null || (storyEntry = uploadingStory.entry) == null) {
            return 0;
        }
        return storyEntry.editStoryId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hideCaptionWithInterface() {
        return true;
    }

    private boolean hitButton(View view, MotionEvent motionEvent) {
        float x = getX() + this.storyContainer.getX() + view.getX();
        float y = getY() + this.storyContainer.getY() + view.getY();
        return motionEvent.getX() >= x && motionEvent.getX() <= x + ((float) view.getWidth()) && motionEvent.getY() >= y && motionEvent.getY() <= y + ((float) view.getHeight());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBotsPreview() {
        StoriesController.StoriesList storiesList;
        StoryViewer storyViewer = this.storyViewer;
        return (storyViewer == null || (storiesList = storyViewer.storiesList) == null || storiesList.type != 4) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isEditBotsPreview() {
        TLRPC$User user;
        return isBotsPreview() && (user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.storyViewer.storiesList.dialogId))) != null && user.bot && user.bot_can_edit;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateOut$49(ValueAnimator valueAnimator) {
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
        Delegate delegate = this.delegate;
        float progressToDismiss = delegate == null ? 0.0f : delegate.getProgressToDismiss();
        float hideInterfaceAlpha = getHideInterfaceAlpha();
        FrameLayout frameLayout2 = this.likeButtonContainer;
        if (frameLayout2 != null) {
            frameLayout2.setAlpha((1.0f - progressToDismiss) * hideInterfaceAlpha * (1.0f - this.outT));
        }
        ImageView imageView = this.shareButton;
        if (imageView != null) {
            imageView.setAlpha((1.0f - progressToDismiss) * hideInterfaceAlpha * (1.0f - this.outT));
        }
        FrameLayout frameLayout3 = this.repostButtonContainer;
        if (frameLayout3 != null) {
            frameLayout3.setAlpha(hideInterfaceAlpha * (1.0f - progressToDismiss) * (1.0f - this.outT));
        }
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.setAlpha(1.0f - this.outT);
            invalidate();
        }
        this.storyContainer.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkRecordLocked$48(boolean z, DialogInterface dialogInterface, int i) {
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            if (z) {
                this.storyViewer.close(true);
            } else {
                chatActivityEnterView.cancelRecordingAudioVideo();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createFailView$20(View view) {
        StoriesController.UploadingStory uploadingStory;
        StoryItemHolder storyItemHolder = this.currentStory;
        if (storyItemHolder == null || (uploadingStory = storyItemHolder.uploadingStory) == null) {
            return;
        }
        uploadingStory.tryAgain();
        updatePosition();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createQualityItem$13(boolean z, View view) {
        boolean z2 = !z;
        MessagesController.getInstance(this.currentAccount).setStoryQuality(z2);
        BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.chats_infotip, LocaleController.getString(z2 ? R.string.StoryQualityIncreasedTitle : R.string.StoryQualityDecreasedTitle), LocaleController.getString(z2 ? R.string.StoryQualityIncreasedMessage : R.string.StoryQualityDecreasedMessage)).show();
        CustomPopupMenu customPopupMenu = this.popupMenu;
        if (customPopupMenu != null) {
            customPopupMenu.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createQualityItem$14(BottomSheet bottomSheet, View view) {
        this.delegate.showDialog(new PremiumFeatureBottomSheet(this.storyViewer.fragment, 14, false));
        bottomSheet.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createQualityItem$15(View view) {
        final BottomSheet bottomSheet = new BottomSheet(getContext(), false, this.resourcesProvider);
        bottomSheet.fixNavigationBar(Theme.getColor(Theme.key_dialogBackground, this.resourcesProvider));
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        linearLayout.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
        BackupImageView backupImageView = new BackupImageView(getContext());
        backupImageView.getImageReceiver().setAutoRepeat(1);
        MediaDataController.getInstance(this.currentAccount).setPlaceholderImage(backupImageView, AndroidUtilities.STICKERS_PLACEHOLDER_PACK_NAME_2, "😎", "150_150");
        linearLayout.addView(backupImageView, LayoutHelper.createLinear(150, 150, 1, 0, 16, 0, 16));
        TextView textView = new TextView(getContext());
        textView.setTypeface(AndroidUtilities.bold());
        textView.setGravity(17);
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, this.resourcesProvider));
        textView.setTextSize(1, 20.0f);
        textView.setText(LocaleController.getString(R.string.StoryQualityPremium));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 1, 12, 0, 12, 0));
        TextView textView2 = new TextView(getContext());
        textView2.setGravity(17);
        textView2.setTextColor(Theme.getColor(Theme.key_dialogTextGray3, this.resourcesProvider));
        textView2.setTextSize(1, 14.0f);
        textView2.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.StoryQualityPremiumText)));
        linearLayout.addView(textView2, LayoutHelper.createLinear(-1, -2, 1, 32, 9, 32, 19));
        ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(getContext(), this.resourcesProvider);
        buttonWithCounterView.setText(LocaleController.getString(R.string.StoryQualityIncrease), false);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("l");
        ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.mini_switch_lock);
        coloredImageSpan.setTopOffset(1);
        spannableStringBuilder.setSpan(coloredImageSpan, 0, 1, 33);
        buttonWithCounterView.setSubText(new SpannableStringBuilder().append((CharSequence) spannableStringBuilder).append((CharSequence) LocaleController.getString(R.string.OptionPremiumRequiredTitle)), false);
        linearLayout.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48, 1));
        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda48
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PeerStoriesView.this.lambda$createQualityItem$14(bottomSheet, view2);
            }
        });
        bottomSheet.setCustomView(linearLayout);
        this.delegate.showDialog(bottomSheet);
        CustomPopupMenu customPopupMenu = this.popupMenu;
        if (customPopupMenu != null) {
            customPopupMenu.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSelfPeerView$34(View view) {
        showUserViewsDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createStealthModeItem$11(View view) {
        if (this.stealthModeIsActive) {
            StealthModeAlert.showStealthModeEnabledBulletin();
        } else {
            this.delegate.showDialog(new StealthModeAlert(getContext(), getY() + this.storyContainer.getY(), 0, this.resourcesProvider));
        }
        CustomPopupMenu customPopupMenu = this.popupMenu;
        if (customPopupMenu != null) {
            customPopupMenu.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createStealthModeItem$12(View view) {
        this.delegate.showDialog(new StealthModeAlert(getContext(), getY() + this.storyContainer.getY(), 0, this.resourcesProvider));
        CustomPopupMenu customPopupMenu = this.popupMenu;
        if (customPopupMenu != null) {
            customPopupMenu.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createUnsupportedContainer$33(View view) {
        Context context;
        String str;
        if (ApplicationLoader.isStandaloneBuild()) {
            LaunchActivity launchActivity = LaunchActivity.instance;
            if (launchActivity != null) {
                launchActivity.checkAppUpdate(true, null);
                return;
            }
            return;
        }
        if (BuildVars.isHuaweiStoreApp()) {
            context = getContext();
            str = BuildVars.HUAWEI_STORE_URL;
        } else {
            context = getContext();
            str = BuildVars.PLAYSTORE_APP_URL;
        }
        Browser.openUrl(context, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteStory$35(DialogInterface dialogInterface, int i) {
        this.currentStory.cancelOrDelete();
        updateStoryItems();
        if (this.isActive && this.count == 0) {
            this.delegate.switchToNextAndRemoveCurrentPeer();
            return;
        }
        int i2 = this.selectedPosition;
        int i3 = this.count;
        if (i2 >= i3) {
            this.selectedPosition = i3 - 1;
        } else if (i2 < 0) {
            this.selectedPosition = 0;
        }
        updatePosition();
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer != null) {
            storyViewer.checkSelfStoriesView();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$editPrivacy$45(Runnable runnable, TLRPC$TL_error tLRPC$TL_error, TL_stories$StoryItem tL_stories$StoryItem, StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy) {
        Bulletin createSimpleBulletin;
        BulletinFactory of;
        int i;
        String str;
        String string;
        if (runnable != null) {
            runnable.run();
        }
        if (tLRPC$TL_error == null || "STORY_NOT_MODIFIED".equals(tLRPC$TL_error.text)) {
            tL_stories$StoryItem.parsedPrivacy = storyPrivacy;
            tL_stories$StoryItem.privacy = storyPrivacy.toValue();
            int i2 = storyPrivacy.type;
            tL_stories$StoryItem.close_friends = i2 == 1;
            tL_stories$StoryItem.contacts = i2 == 2;
            tL_stories$StoryItem.selected_contacts = i2 == 3;
            MessagesController.getInstance(this.currentAccount).getStoriesController().updateStoryItem(tL_stories$StoryItem.dialogId, tL_stories$StoryItem);
            this.editedPrivacy = true;
            int i3 = storyPrivacy.type;
            if (i3 == 4) {
                of = BulletinFactory.of(this.storyContainer, this.resourcesProvider);
                i = R.raw.contact_check;
                str = "StorySharedToEveryone";
            } else if (i3 != 1) {
                if (i3 != 2) {
                    if (i3 == 3) {
                        HashSet hashSet = new HashSet();
                        hashSet.addAll(storyPrivacy.selectedUserIds);
                        for (ArrayList arrayList : storyPrivacy.selectedUserIdsByGroup.values()) {
                            hashSet.addAll(arrayList);
                        }
                        createSimpleBulletin = BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, LocaleController.formatPluralString("StorySharedToContacts", hashSet.size(), new Object[0]));
                    }
                    updatePosition();
                } else if (storyPrivacy.selectedUserIds.isEmpty()) {
                    of = BulletinFactory.of(this.storyContainer, this.resourcesProvider);
                    i = R.raw.contact_check;
                    str = "StorySharedToAllContacts";
                } else {
                    createSimpleBulletin = BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, LocaleController.formatPluralString("StorySharedToAllContactsExcluded", storyPrivacy.selectedUserIds.size(), new Object[0]));
                }
                createSimpleBulletin.show();
                updatePosition();
            } else {
                of = BulletinFactory.of(this.storyContainer, this.resourcesProvider);
                i = R.raw.contact_check;
                str = "StorySharedToCloseFriends";
            }
            string = LocaleController.getString(str);
        } else {
            of = BulletinFactory.of(this.storyContainer, this.resourcesProvider);
            i = R.raw.error;
            string = LocaleController.getString(R.string.UnknownError);
        }
        createSimpleBulletin = of.createSimpleBulletin(i, string);
        createSimpleBulletin.show();
        updatePosition();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$editPrivacy$46(final Runnable runnable, final TL_stories$StoryItem tL_stories$StoryItem, final StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy, TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.this.lambda$editPrivacy$45(runnable, tLRPC$TL_error, tL_stories$StoryItem, storyPrivacy);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$editPrivacy$47(final TL_stories$StoryItem tL_stories$StoryItem, final StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy, boolean z, boolean z2, TLRPC$InputPeer tLRPC$InputPeer, final Runnable runnable) {
        TL_stories$TL_stories_editStory tL_stories$TL_stories_editStory = new TL_stories$TL_stories_editStory();
        tL_stories$TL_stories_editStory.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(tL_stories$StoryItem.dialogId);
        tL_stories$TL_stories_editStory.id = tL_stories$StoryItem.id;
        tL_stories$TL_stories_editStory.flags |= 4;
        tL_stories$TL_stories_editStory.privacy_rules = storyPrivacy.rules;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_stories$TL_stories_editStory, new RequestDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda0
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PeerStoriesView.this.lambda$editPrivacy$46(runnable, tL_stories$StoryItem, storyPrivacy, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$needEnterText$44() {
        this.delegate.requestAdjust(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        StoryCaptionView storyCaptionView = this.storyCaptionView;
        if (!storyCaptionView.expanded) {
            this.checkBlackoutMode = true;
            storyCaptionView.expand();
        } else if (storyCaptionView.textSelectionHelper.isInSelectionMode()) {
            this.storyCaptionView.checkCancelTextSelection();
        } else {
            this.storyCaptionView.collapse();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        shareStory(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$10(StoryViewer storyViewer, View view) {
        if (!this.currentStory.hasSound()) {
            showNoSoundHint(true);
            return;
        }
        storyViewer.toggleSilentMode();
        if (storyViewer.soundEnabled()) {
            MessagesController.getGlobalMainSettings().edit().putInt("taptostorysoundhint", 3).apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view) {
        tryToOpenRepostStory();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3() {
        likeStory(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$37() {
        checkStealthMode(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(View view) {
        TL_stories$StoryItem tL_stories$StoryItem = this.currentStory.storyItem;
        if (tL_stories$StoryItem == null || tL_stories$StoryItem.sent_reaction != null) {
            likeStory(null);
        } else {
            applyMessageToChat(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.this.lambda$new$3();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$42() {
        showNoSoundHint(false);
        MessagesController.getGlobalMainSettings().edit().putInt("taptostorysoundhint", MessagesController.getGlobalMainSettings().getInt("taptostorysoundhint", 0) + 1).apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$5(StoryViewer storyViewer, View view) {
        Runnable runnable = this.reactionsTooltipRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.reactionsTooltipRunnable = null;
        }
        SharedConfig.setStoriesReactionsLongPressHintUsed(true);
        HintView2 hintView2 = this.reactionsLongpressTooltip;
        if (hintView2 != null) {
            hintView2.hide();
        }
        checkReactionsLayoutForLike();
        storyViewer.windowView.dispatchTouchEvent(AndroidUtilities.emptyMotionEvent());
        showLikesReaction(true);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(StoryViewer storyViewer, View view) {
        long j = UserConfig.getInstance(this.currentAccount).clientUserId;
        long j2 = this.dialogId;
        if (j != j2) {
            storyViewer.presentFragment(j2 > 0 ? ProfileActivity.of(j2) : ChatActivity.of(j2));
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putLong("dialog_id", this.dialogId);
        storyViewer.presentFragment(new MediaActivity(bundle, null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$7(Theme.ResourcesProvider resourcesProvider, StoryViewer storyViewer, Context context, SharedResources sharedResources, View view) {
        this.delegate.setPopupIsVisible(true);
        this.editStoryItem = null;
        boolean[] zArr = {false};
        if (this.isSelf) {
            MessagesController.getInstance(this.currentAccount).getStoriesController().loadBlocklistAtFirst();
            MessagesController.getInstance(this.currentAccount).getStoriesController().loadSendAs();
            MessagesController.getInstance(this.currentAccount).getStoriesController().getDraftsController().load();
        }
        boolean z = this.isSelf || MessagesController.getInstance(this.currentAccount).getStoriesController().canEditStory(this.currentStory.storyItem);
        boolean z2 = this.isSelf || ((this.isChannel || isBotsPreview()) && z);
        boolean z3 = this.currentStory.isVideo;
        8 r15 = new 8(getContext(), resourcesProvider, z3, resourcesProvider, storyViewer, z3, z2, z, context, sharedResources, zArr);
        this.popupMenu = r15;
        r15.show(this.optionsIconView, 0, (-ActionBar.getCurrentActionBarHeight()) + AndroidUtilities.dp(6.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8() {
        this.delegate.setIsHintVisible(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00ef  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0123  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$new$9(View view) {
        String formatString;
        SpannableStringBuilder replaceTags;
        boolean z;
        TL_stories$StoryItem tL_stories$StoryItem = this.currentStory.storyItem;
        if (tL_stories$StoryItem == null) {
            return;
        }
        if (this.isSelf) {
            editPrivacy(new StoryPrivacyBottomSheet.StoryPrivacy(this.currentAccount, tL_stories$StoryItem.privacy), tL_stories$StoryItem);
            return;
        }
        if (this.privacyHint == null) {
            HintView2 onHiddenListener = new HintView2(getContext(), 1).setMultilineText(true).setTextAlign(Layout.Alignment.ALIGN_CENTER).setOnHiddenListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda27
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.this.lambda$new$8();
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
        if (tL_stories$StoryItem.close_friends) {
            this.privacyHint.setInnerPadding(15, 8, 15, 8);
            formatString = LocaleController.formatString("StoryCloseFriendsHint", R.string.StoryCloseFriendsHint, str);
        } else if (tL_stories$StoryItem.contacts) {
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
        } else if (!tL_stories$StoryItem.selected_contacts) {
            return;
        } else {
            this.privacyHint.setInnerPadding(15, 8, 15, 8);
            formatString = LocaleController.formatString("StorySelectedContactsHint", R.string.StorySelectedContactsHint, str);
        }
        replaceTags = AndroidUtilities.replaceTags(formatString);
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
    public /* synthetic */ void lambda$onMeasure$43(ValueAnimator valueAnimator) {
        this.animatingKeyboardHeight = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openRepostStory$27() {
        this.editOpened = true;
        setActive(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openRepostStory$28(DialogStoriesCell.StoryCell storyCell, DialogStoriesCell dialogStoriesCell, Long l, StoryRecorder storyRecorder, Runnable runnable) {
        if (storyCell == null) {
            storyCell = dialogStoriesCell.findStoryCell(l.longValue());
        }
        storyRecorder.replaceSourceView(StoryRecorder.SourceView.fromStoryCell(storyCell));
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openRepostStory$29(Runnable runnable) {
        AndroidUtilities.cancelRunOnUIThread(runnable);
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openRepostStory$30(Runnable runnable, long j) {
        AndroidUtilities.cancelRunOnUIThread(runnable);
        AndroidUtilities.runOnUIThread(runnable, Math.max(0L, 32 - (System.currentTimeMillis() - j)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0067  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$openRepostStory$31(final StoryRecorder storyRecorder, Long l, final Runnable runnable, Boolean bool, final Long l2) {
        DialogStoriesCell dialogStoriesCell;
        BaseFragment baseFragment;
        INavigationLayout parentLayout;
        if (!bool.booleanValue()) {
            final long currentTimeMillis = System.currentTimeMillis();
            VideoPlayerSharedScope videoPlayerSharedScope = this.playerSharedScope;
            if (videoPlayerSharedScope != null && videoPlayerSharedScope.player == null) {
                this.delegate.setPopupIsVisible(false);
                setActive(true);
                this.editOpened = false;
                this.onImageReceiverThumbLoaded = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda46
                    @Override // java.lang.Runnable
                    public final void run() {
                        PeerStoriesView.lambda$openRepostStory$29(runnable);
                    }
                };
                if (bool.booleanValue()) {
                    updatePosition();
                }
                AndroidUtilities.runOnUIThread(runnable, 400L);
                return;
            }
            StoryViewer.VideoPlayerHolder videoPlayerHolder = videoPlayerSharedScope.player;
            videoPlayerHolder.firstFrameRendered = false;
            videoPlayerSharedScope.firstFrameRendered = false;
            videoPlayerHolder.setOnReadyListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda47
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.lambda$openRepostStory$30(runnable, currentTimeMillis);
                }
            });
            this.delegate.setPopupIsVisible(false);
            RLottieImageView rLottieImageView = this.muteIconView;
            if (rLottieImageView != null) {
                rLottieImageView.setAnimation(this.sharedResources.muteDrawable);
            }
            setActive(((this.videoDuration <= 0 || l.longValue() <= this.videoDuration - 1400) ? l : 0L).longValue(), true);
            this.editOpened = false;
            AndroidUtilities.runOnUIThread(runnable, 400L);
            if (bool.booleanValue()) {
                updatePosition();
                return;
            }
            return;
        }
        BaseFragment baseFragment2 = this.storyViewer.fragment;
        if (baseFragment2 != null && (parentLayout = baseFragment2.getParentLayout()) != null) {
            List fragmentStack = parentLayout.getFragmentStack();
            ArrayList arrayList = new ArrayList();
            for (int size = fragmentStack.size() - 1; size >= 0; size--) {
                BaseFragment baseFragment3 = (BaseFragment) fragmentStack.get(size);
                if (baseFragment3 instanceof DialogsActivity) {
                    DialogsActivity dialogsActivity = (DialogsActivity) baseFragment3;
                    dialogsActivity.closeSearching();
                    DialogStoriesCell dialogStoriesCell2 = dialogsActivity.dialogStoriesCell;
                    r7 = dialogStoriesCell2 != null ? dialogStoriesCell2.findStoryCell(l2.longValue()) : null;
                    for (int i = 0; i < arrayList.size(); i++) {
                        parentLayout.removeFragmentFromStack((BaseFragment) arrayList.get(i));
                    }
                    dialogStoriesCell = dialogStoriesCell2;
                    baseFragment = this.storyViewer.fragment;
                    if (baseFragment != null) {
                        baseFragment.clearSheets();
                    }
                    this.storyViewer.instantClose();
                    this.editOpened = false;
                    if (dialogStoriesCell != null || !dialogStoriesCell.scrollTo(l2.longValue())) {
                        storyRecorder.replaceSourceView(StoryRecorder.SourceView.fromStoryCell(r7));
                        AndroidUtilities.runOnUIThread(runnable, 400L);
                    }
                    final DialogStoriesCell.StoryCell storyCell = r7;
                    final DialogStoriesCell dialogStoriesCell3 = dialogStoriesCell;
                    dialogStoriesCell.afterNextLayout(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda45
                        @Override // java.lang.Runnable
                        public final void run() {
                            PeerStoriesView.lambda$openRepostStory$28(DialogStoriesCell.StoryCell.this, dialogStoriesCell3, l2, storyRecorder, runnable);
                        }
                    });
                    return;
                }
                arrayList.add(baseFragment3);
            }
        }
        dialogStoriesCell = null;
        baseFragment = this.storyViewer.fragment;
        if (baseFragment != null) {
        }
        this.storyViewer.instantClose();
        this.editOpened = false;
        if (dialogStoriesCell != null) {
        }
        storyRecorder.replaceSourceView(StoryRecorder.SourceView.fromStoryCell(r7));
        AndroidUtilities.runOnUIThread(runnable, 400L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openRepostStory$32(Activity activity) {
        StoryViewer.VideoPlayerHolder videoPlayerHolder;
        final StoryRecorder storyRecorder = StoryRecorder.getInstance(activity, this.currentAccount);
        VideoPlayerSharedScope videoPlayerSharedScope = this.playerSharedScope;
        storyRecorder.openForward(StoryRecorder.SourceView.fromStoryViewer(this.storyViewer), StoryEntry.repostStoryItem(this.currentStory.getPath(), this.currentStory.storyItem), (videoPlayerSharedScope == null || (videoPlayerHolder = videoPlayerSharedScope.player) == null) ? 0L : videoPlayerHolder.currentPosition, true);
        storyRecorder.setOnFullyOpenListener(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.this.lambda$openRepostStory$27();
            }
        });
        storyRecorder.setOnPrepareCloseListener(new Utilities.Callback4() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda42
            @Override // org.telegram.messenger.Utilities.Callback4
            public final void run(Object obj, Object obj2, Object obj3, Object obj4) {
                PeerStoriesView.this.lambda$openRepostStory$31(storyRecorder, (Long) obj, (Runnable) obj2, (Boolean) obj3, (Long) obj4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveToGallery$26(boolean z, Uri uri) {
        BulletinFactory.createSaveToGalleryBulletin(this.storyContainer, z, this.resourcesProvider).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLikesReaction$16(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.likesReactionShowProgress = floatValue;
        this.likesReactionLayout.setTransitionProgress(floatValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPremiumBlockedToast$21(TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus, ChannelBoostsController.CanApplyBoost canApplyBoost) {
        this.canApplyBoost = canApplyBoost;
        LimitReachedBottomSheet.openBoostsForRemoveRestrictions(fragmentForLimit(), tL_stories$TL_premium_boostsStatus, canApplyBoost, this.dialogId, true);
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer != null) {
            storyViewer.setOverlayVisible(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPremiumBlockedToast$22(final TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus) {
        if (tL_stories$TL_premium_boostsStatus != null) {
            this.boostsStatus = tL_stories$TL_premium_boostsStatus;
            MessagesController.getInstance(this.currentAccount).getBoostsController().userCanBoostChannel(this.dialogId, tL_stories$TL_premium_boostsStatus, new Consumer() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda29
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    PeerStoriesView.this.lambda$showPremiumBlockedToast$21(tL_stories$TL_premium_boostsStatus, (ChannelBoostsController.CanApplyBoost) obj);
                }
            });
            return;
        }
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer != null) {
            storyViewer.setOverlayVisible(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPremiumBlockedToast$23() {
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer != null) {
            storyViewer.presentFragment(new PremiumPreviewFragment("noncontacts"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toggleArchiveForStory$17(MessagesController messagesController, long j, boolean z) {
        messagesController.getStoriesController().toggleHidden(j, !z, false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toggleArchiveForStory$18(MessagesController messagesController, long j, boolean z) {
        messagesController.getStoriesController().toggleHidden(j, z, true, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleArchiveForStory$19(final MessagesController messagesController, final long j, final boolean z, String str, TLObject tLObject) {
        messagesController.getStoriesController().toggleHidden(j, z, false, true);
        BulletinFactory.UndoObject undoObject = new BulletinFactory.UndoObject();
        undoObject.onUndo = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda50
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.lambda$toggleArchiveForStory$17(MessagesController.this, j, z);
            }
        };
        undoObject.onAction = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda51
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.lambda$toggleArchiveForStory$18(MessagesController.this, j, z);
            }
        };
        BulletinFactory.of(this.storyContainer, this.resourcesProvider).createUsersBulletin(Arrays.asList(tLObject), AndroidUtilities.replaceTags(!z ? LocaleController.formatString("StoriesMovedToDialogs", R.string.StoriesMovedToDialogs, ContactsController.formatName(str, null, 10)) : LocaleController.formatString("StoriesMovedToContacts", R.string.StoriesMovedToContacts, ContactsController.formatName(str, null, 10))), null, undoObject).setTag(2).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePosition$38(StoryCaptionView.Reply reply, View view) {
        String str;
        StoryViewer storyViewer;
        BaseFragment profileActivity;
        Integer num;
        if (reply.peerId == null) {
            BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.StoryHidAccount)).setTag(3).show(true);
            return;
        }
        Bundle bundle = new Bundle();
        int i = (reply.peerId.longValue() > 0L ? 1 : (reply.peerId.longValue() == 0L ? 0 : -1));
        long longValue = reply.peerId.longValue();
        if (i >= 0) {
            str = "user_id";
        } else {
            longValue = -longValue;
            str = "chat_id";
        }
        bundle.putLong(str, longValue);
        if (!reply.isRepostMessage || (num = reply.messageId) == null) {
            storyViewer = this.storyViewer;
            profileActivity = new ProfileActivity(bundle);
        } else {
            bundle.putInt("message_id", num.intValue());
            storyViewer = this.storyViewer;
            profileActivity = new ChatActivity(bundle);
        }
        storyViewer.presentFragment(profileActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePosition$39(long j, View view) {
        String str;
        Bundle bundle = new Bundle();
        if (j >= 0) {
            str = "user_id";
        } else {
            j = -j;
            str = "chat_id";
        }
        bundle.putLong(str, j);
        this.storyViewer.presentFragment(new ProfileActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePosition$40() {
        this.failView.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePosition$41() {
        if (this.storyViewer.isShown()) {
            this.reactionsTooltipRunnable = null;
            if (this.reactionsLongpressTooltip == null) {
                HintView2 joint = new HintView2(getContext(), 3).setJoint(1.0f, -22.0f);
                this.reactionsLongpressTooltip = joint;
                joint.setBgColor(ColorUtils.setAlphaComponent(ColorUtils.blendARGB(-16777216, -1, 0.13f), NotificationCenter.reloadInterface));
                this.reactionsLongpressTooltip.setBounce(false);
                this.reactionsLongpressTooltip.setText(LocaleController.getString(R.string.ReactionLongTapHint));
                this.reactionsLongpressTooltip.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(1.0f));
                this.storyContainer.addView(this.reactionsLongpressTooltip, LayoutHelper.createFrame(-1, -2.0f, 85, 0.0f, 0.0f, 0.0f, this.BIG_SCREEN ? 0.0f : 56.0f));
            }
            this.reactionsLongpressTooltip.show();
            SharedConfig.setStoriesReactionsLongPressHintUsed(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void likeStory(ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        boolean z;
        TLRPC$Reaction tLRPC$Reaction;
        TL_stories$StoryItem tL_stories$StoryItem = this.currentStory.storyItem;
        if (tL_stories$StoryItem == null) {
            return;
        }
        TLRPC$Reaction tLRPC$Reaction2 = tL_stories$StoryItem.sent_reaction;
        boolean z2 = tLRPC$Reaction2 != null;
        if (tLRPC$Reaction2 == null || visibleReaction != null) {
            if (visibleReaction == null) {
                TLRPC$TL_availableReaction tLRPC$TL_availableReaction = MediaDataController.getInstance(this.currentAccount).getReactionsMap().get("❤");
                if (tLRPC$TL_availableReaction != null) {
                    this.drawAnimatedEmojiAsMovingReaction = false;
                    this.reactionEffectImageReceiver.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.around_animation), ReactionsEffectOverlay.getFilterForAroundAnimation(), null, null, null, 0);
                    if (this.reactionEffectImageReceiver.getLottieAnimation() != null) {
                        this.reactionEffectImageReceiver.getLottieAnimation().setCurrentFrame(0, false, true);
                    }
                    this.drawReactionEffect = true;
                    visibleReaction = ReactionsLayoutInBubble.VisibleReaction.fromEmojicon(tLRPC$TL_availableReaction);
                }
            } else {
                animateLikeButton();
            }
            this.storiesController.setStoryReaction(this.dialogId, this.currentStory.storyItem, visibleReaction);
        } else {
            animateLikeButton();
            this.storiesController.setStoryReaction(this.dialogId, this.currentStory.storyItem, null);
        }
        TL_stories$StoryItem tL_stories$StoryItem2 = this.currentStory.storyItem;
        if (tL_stories$StoryItem2 == null || (tLRPC$Reaction = tL_stories$StoryItem2.sent_reaction) == null) {
            this.storiesLikeButton.setReaction(null);
            z = false;
        } else {
            z2 = !z2;
            this.storiesLikeButton.setReaction(ReactionsLayoutInBubble.VisibleReaction.fromTL(tLRPC$Reaction));
            performHapticFeedback(3);
            z = true;
        }
        if (this.isChannel && z2) {
            TL_stories$StoryItem tL_stories$StoryItem3 = this.currentStory.storyItem;
            if (tL_stories$StoryItem3.views == null) {
                tL_stories$StoryItem3.views = new TL_stories$TL_storyViews();
            }
            TL_stories$StoryViews tL_stories$StoryViews = this.currentStory.storyItem.views;
            int i = tL_stories$StoryViews.reactions_count + (z ? 1 : -1);
            tL_stories$StoryViews.reactions_count = i;
            if (i < 0) {
                tL_stories$StoryViews.reactions_count = 0;
            }
        }
        TL_stories$StoryItem tL_stories$StoryItem4 = this.currentStory.storyItem;
        ReactionsUtils.applyForStoryViews(tLRPC$Reaction2, tL_stories$StoryItem4.sent_reaction, tL_stories$StoryItem4.views);
        updateUserViews(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLinkCopied() {
        if (this.currentStory.storyItem == null) {
            return;
        }
        TL_stories$TL_stories_exportStoryLink tL_stories$TL_stories_exportStoryLink = new TL_stories$TL_stories_exportStoryLink();
        tL_stories$TL_stories_exportStoryLink.id = this.currentStory.storyItem.id;
        tL_stories$TL_stories_exportStoryLink.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.dialogId);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_stories$TL_stories_exportStoryLink, new RequestDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.27
            @Override // org.telegram.tgnet.RequestDelegate
            public void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            }
        });
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
        this.chatAttachAlert.setDialogId(this.dialogId);
        this.chatAttachAlert.init();
        this.chatAttachAlert.getCommentTextView().setText(this.chatActivityEnterView.getFieldText());
        this.delegate.showDialog(this.chatAttachAlert);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openChat() {
        String str;
        Bundle bundle = new Bundle();
        long j = this.dialogId;
        if (j < 0) {
            j = -j;
            str = "chat_id";
        } else {
            str = "user_id";
        }
        bundle.putLong(str, j);
        TLRPC$Dialog dialog = MessagesController.getInstance(this.currentAccount).getDialog(this.dialogId);
        if (dialog != null) {
            bundle.putInt("message_id", dialog.top_message);
        }
        this.storyViewer.presentFragment(new ChatActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openRepostStory() {
        final Activity findActivity = AndroidUtilities.findActivity(getContext());
        if (findActivity == null) {
            return;
        }
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.this.lambda$openRepostStory$32(findActivity);
            }
        };
        if (this.delegate.releasePlayer(runnable)) {
            return;
        }
        AndroidUtilities.runOnUIThread(runnable, 80L);
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0170  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void requestVideoPlayer(long j) {
        TLRPC$Document tLRPC$Document;
        Uri uri;
        TLRPC$Document tLRPC$Document2;
        if (!this.isActive) {
            this.playerSharedScope.renderView = null;
        } else if (!this.currentStory.isVideo()) {
            FileLog.d("PeerStoriesView.requestVideoPlayer(" + j + "): null, not a video");
            this.delegate.requestPlayer(null, null, 0L, this.playerSharedScope);
            VideoPlayerSharedScope videoPlayerSharedScope = this.playerSharedScope;
            videoPlayerSharedScope.renderView = null;
            videoPlayerSharedScope.firstFrameRendered = false;
        } else {
            if (this.currentStory.getLocalPath() == null || !new File(this.currentStory.getLocalPath()).exists()) {
                TL_stories$StoryItem tL_stories$StoryItem = this.currentStory.storyItem;
                if (tL_stories$StoryItem != null) {
                    tL_stories$StoryItem.dialogId = this.dialogId;
                    try {
                        tLRPC$Document2 = tL_stories$StoryItem.media.getDocument();
                        try {
                            TL_stories$StoryItem tL_stories$StoryItem2 = this.currentStory.storyItem;
                            if (tL_stories$StoryItem2.fileReference == 0) {
                                tL_stories$StoryItem2.fileReference = FileLoader.getInstance(this.currentAccount).getFileReference(this.currentStory.storyItem);
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
                            sb.append("&sid=");
                            sb.append(this.currentStory.storyItem.id);
                            sb.append("&did=");
                            sb.append(this.currentStory.storyItem.dialogId);
                            Uri parse = Uri.parse("tg://" + FileLoader.getAttachFileName(tLRPC$Document2) + sb.toString());
                            FileLog.d("StoryViewer requestVideoPlayer(" + j + "): playing from " + parse);
                            this.videoDuration = (long) (MessageObject.getDocumentDuration(tLRPC$Document2) * 1000.0d);
                            uri = parse;
                            tLRPC$Document = tLRPC$Document2;
                        } catch (Exception unused) {
                            tLRPC$Document = tLRPC$Document2;
                            uri = null;
                            if (uri == null) {
                            }
                            this.delegate.requestPlayer(tLRPC$Document, uri, j, this.playerSharedScope);
                            this.storyContainer.invalidate();
                        }
                    } catch (Exception unused2) {
                        tLRPC$Document2 = null;
                    }
                } else {
                    tLRPC$Document = null;
                    uri = null;
                }
            } else {
                Uri fromFile = Uri.fromFile(new File(this.currentStory.getLocalPath()));
                FileLog.d("StoryViewer requestVideoPlayer(" + j + "): playing from attachPath " + fromFile);
                this.videoDuration = 0L;
                uri = fromFile;
                tLRPC$Document = null;
            }
            if (uri == null) {
                FileLog.d("PeerStoriesView.requestVideoPlayer(" + j + "): playing from null?");
            }
            this.delegate.requestPlayer(tLRPC$Document, uri, j, this.playerSharedScope);
            this.storyContainer.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveToGallery() {
        StoryItemHolder storyItemHolder = this.currentStory;
        TL_stories$StoryItem tL_stories$StoryItem = storyItemHolder.storyItem;
        if ((tL_stories$StoryItem == null && storyItemHolder.uploadingStory == null) || (tL_stories$StoryItem instanceof TL_stories$TL_storyItemSkipped)) {
            return;
        }
        File path = storyItemHolder.getPath();
        final boolean isVideo = this.currentStory.isVideo();
        if (path == null || !path.exists()) {
            showDownloadAlert();
            return;
        }
        MediaController.saveFile(path.toString(), getContext(), isVideo ? 1 : 0, null, null, new Utilities.Callback() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda38
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                PeerStoriesView.this.lambda$saveToGallery$26(isVideo, (Uri) obj);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0086  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void sendUriAsDocument(Uri uri) {
        TL_stories$StoryItem tL_stories$StoryItem;
        String str;
        int indexOf;
        Uri parse;
        String str2;
        String str3;
        String str4;
        MessageObject messageObject;
        MessageObject messageObject2;
        ChatActivity.ReplyQuote replyQuote;
        MessageObject messageObject3;
        boolean z;
        int i;
        InputContentInfoCompat inputContentInfoCompat;
        String str5;
        String str6;
        if (uri == null || (tL_stories$StoryItem = this.currentStory.storyItem) == null || (tL_stories$StoryItem instanceof TL_stories$TL_storyItemSkipped)) {
            return;
        }
        String uri2 = uri.toString();
        boolean z2 = false;
        if (uri2.contains("com.google.android.apps.photos.contentprovider")) {
            try {
                str = uri2.split("/1/")[1];
                indexOf = str.indexOf("/ACTUAL");
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (indexOf != -1) {
                parse = Uri.parse(URLDecoder.decode(str.substring(0, indexOf), "UTF-8"));
                String path = AndroidUtilities.getPath(parse);
                if (BuildVars.NO_SCOPED_STORAGE) {
                    str2 = path;
                    z2 = true;
                } else if (path == null) {
                    String uri3 = parse.toString();
                    String copyFileToCache = MediaController.copyFileToCache(parse, "file");
                    if (copyFileToCache == null) {
                        showAttachmentError();
                        return;
                    } else {
                        str2 = uri3;
                        path = copyFileToCache;
                    }
                } else {
                    str2 = path;
                }
                AccountInstance accountInstance = getAccountInstance();
                long j = this.dialogId;
                if (z2) {
                    parse = null;
                    str3 = null;
                    str4 = null;
                    messageObject = null;
                    messageObject2 = null;
                    replyQuote = null;
                    messageObject3 = null;
                    z = true;
                    i = 0;
                    inputContentInfoCompat = null;
                    str5 = null;
                    str6 = path;
                } else {
                    str6 = null;
                    str2 = null;
                    str3 = null;
                    str4 = null;
                    messageObject = null;
                    messageObject2 = null;
                    replyQuote = null;
                    messageObject3 = null;
                    z = true;
                    i = 0;
                    inputContentInfoCompat = null;
                    str5 = null;
                }
                SendMessagesHelper.prepareSendingDocument(accountInstance, str6, str2, parse, str3, str4, j, messageObject, messageObject2, tL_stories$StoryItem, replyQuote, messageObject3, z, i, inputContentInfoCompat, str5, 0, false);
            }
        }
        parse = uri;
        String path2 = AndroidUtilities.getPath(parse);
        if (BuildVars.NO_SCOPED_STORAGE) {
        }
        AccountInstance accountInstance2 = getAccountInstance();
        long j2 = this.dialogId;
        if (z2) {
        }
        SendMessagesHelper.prepareSendingDocument(accountInstance2, str6, str2, parse, str3, str4, j2, messageObject, messageObject2, tL_stories$StoryItem, replyQuote, messageObject3, z, i, inputContentInfoCompat, str5, 0, false);
    }

    private void setStoryImage(TL_stories$StoryItem tL_stories$StoryItem, ImageReceiver imageReceiver, String str) {
        ArrayList arrayList;
        ImageLocation forPhoto;
        String str2;
        int i;
        ImageLocation imageLocation;
        String str3;
        Drawable drawable;
        long j;
        ImageReceiver imageReceiver2;
        ImageLocation imageLocation2;
        String str4;
        String str5;
        ImageLocation imageLocation3;
        String str6;
        Drawable drawable2;
        long j2;
        Object obj;
        StoriesController.UploadingStory findEditingStory = this.storiesController.findEditingStory(this.dialogId, tL_stories$StoryItem);
        if (findEditingStory != null) {
            setStoryImage(findEditingStory, imageReceiver, str);
            return;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = tL_stories$StoryItem.media;
        boolean z = tLRPC$MessageMedia != null && MessageObject.isVideoDocument(tLRPC$MessageMedia.getDocument());
        String str7 = tL_stories$StoryItem.attachPath;
        if (str7 != null) {
            if (tL_stories$StoryItem.media == null) {
                z = str7.toLowerCase().endsWith(".mp4");
            }
            if (!z) {
                imageReceiver.setImage(ImageLocation.getForPath(tL_stories$StoryItem.attachPath), str, null, null, null, 0L, null, null, 0);
                return;
            }
            obj = null;
            i = 0;
            imageReceiver2 = imageReceiver;
            imageLocation2 = ImageLocation.getForPath(tL_stories$StoryItem.attachPath);
            str4 = str + "_pframe";
            forPhoto = ImageLocation.getForPath(tL_stories$StoryItem.firstFramePath);
            str5 = str;
            imageLocation3 = null;
            str6 = null;
            drawable2 = null;
            j2 = 0;
            str2 = null;
        } else {
            if (z) {
                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_stories$StoryItem.media.getDocument().thumbs, 1000);
                ImageLocation forDocument = ImageLocation.getForDocument(tL_stories$StoryItem.media.getDocument());
                ImageLocation forDocument2 = ImageLocation.getForDocument(closestPhotoSizeWithSize, tL_stories$StoryItem.media.getDocument());
                str2 = null;
                i = 0;
                imageLocation = null;
                str3 = null;
                drawable = null;
                j = 0;
                imageReceiver2 = imageReceiver;
                imageLocation2 = forDocument;
                str4 = str + "_pframe";
                forPhoto = forDocument2;
            } else {
                TLRPC$MessageMedia tLRPC$MessageMedia2 = tL_stories$StoryItem.media;
                TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia2 != null ? tLRPC$MessageMedia2.photo : null;
                if (tLRPC$Photo == null || (arrayList = tLRPC$Photo.sizes) == null) {
                    imageReceiver.clearImage();
                    return;
                }
                TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(arrayList, ConnectionsManager.DEFAULT_DATACENTER_ID);
                FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, 800);
                forPhoto = ImageLocation.getForPhoto(closestPhotoSizeWithSize2, tLRPC$Photo);
                str2 = null;
                i = 0;
                imageLocation = null;
                str3 = null;
                drawable = null;
                j = 0;
                imageReceiver2 = imageReceiver;
                imageLocation2 = null;
                str4 = null;
            }
            str5 = str;
            imageLocation3 = imageLocation;
            str6 = str3;
            drawable2 = drawable;
            j2 = j;
            obj = tL_stories$StoryItem;
        }
        imageReceiver2.setImage(imageLocation2, str4, forPhoto, str5, imageLocation3, str6, drawable2, j2, str2, obj, i);
    }

    private void setStoryImage(StoriesController.UploadingStory uploadingStory, ImageReceiver imageReceiver, String str) {
        if (uploadingStory.isVideo) {
            imageReceiver.setImage(null, null, ImageLocation.getForPath(uploadingStory.firstFramePath), str, null, null, null, 0L, null, null, 0);
        } else {
            imageReceiver.setImage(ImageLocation.getForPath(uploadingStory.path), str, null, null, null, 0L, null, null, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shareStory(boolean z) {
        StoryItemHolder storyItemHolder = this.currentStory;
        if (storyItemHolder.storyItem == null || this.storyViewer.fragment == null) {
            return;
        }
        String createLink = storyItemHolder.createLink();
        if (!z) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", createLink);
            LaunchActivity.instance.startActivityForResult(Intent.createChooser(intent, LocaleController.getString(R.string.StickersShare)), 500);
            return;
        }
        ShareAlert shareAlert = new ShareAlert(this.storyViewer.fragment.getContext(), null, null, createLink, null, false, createLink, null, false, false, !DISABLE_STORY_REPOSTING && MessagesController.getInstance(this.currentAccount).storiesEnabled() && (!(this.isChannel || UserObject.isService(this.dialogId)) || ChatObject.isPublic(this.isChannel ? MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId)) : null)), new WrappedResourceProvider(this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.24
            @Override // org.telegram.ui.WrappedResourceProvider
            public void appendColors() {
                this.sparseIntArray.put(Theme.key_chat_emojiPanelBackground, ColorUtils.blendARGB(-16777216, -1, 0.2f));
                this.sparseIntArray.put(Theme.key_chat_messagePanelIcons, ColorUtils.blendARGB(-16777216, -1, 0.5f));
            }
        }) { // from class: org.telegram.ui.Stories.PeerStoriesView.25
            @Override // org.telegram.ui.Components.ShareAlert, org.telegram.ui.ActionBar.BottomSheet
            public void dismissInternal() {
                super.dismissInternal();
                PeerStoriesView.this.shareAlert = null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.ShareAlert
            public void onSend(LongSparseArray longSparseArray, int i, TLRPC$TL_forumTopic tLRPC$TL_forumTopic) {
                Bulletin createSimpleBulletin;
                super.onSend(longSparseArray, i, tLRPC$TL_forumTopic);
                PeerStoriesView peerStoriesView = PeerStoriesView.this;
                BulletinFactory of = BulletinFactory.of(peerStoriesView.storyContainer, peerStoriesView.resourcesProvider);
                if (of != null) {
                    if (longSparseArray.size() == 1) {
                        long keyAt = longSparseArray.keyAt(0);
                        if (keyAt == UserConfig.getInstance(this.currentAccount).clientUserId) {
                            createSimpleBulletin = of.createSimpleBulletin(R.raw.saved_messages, AndroidUtilities.replaceTags(LocaleController.formatString("StorySharedToSavedMessages", R.string.StorySharedToSavedMessages, new Object[0])), 5000);
                        } else if (keyAt < 0) {
                            createSimpleBulletin = of.createSimpleBulletin(R.raw.forward, AndroidUtilities.replaceTags(LocaleController.formatString("StorySharedTo", R.string.StorySharedTo, tLRPC$TL_forumTopic != null ? tLRPC$TL_forumTopic.title : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-keyAt)).title)), 5000);
                        } else {
                            createSimpleBulletin = of.createSimpleBulletin(R.raw.forward, AndroidUtilities.replaceTags(LocaleController.formatString("StorySharedTo", R.string.StorySharedTo, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(keyAt)).first_name)), 5000);
                        }
                    } else {
                        createSimpleBulletin = of.createSimpleBulletin(R.raw.forward, AndroidUtilities.replaceTags(LocaleController.formatPluralString("StorySharedToManyChats", longSparseArray.size(), Integer.valueOf(longSparseArray.size()))));
                    }
                    createSimpleBulletin.hideAfterBottomSheet(false).show();
                    PeerStoriesView.this.performHapticFeedback(3);
                }
            }

            @Override // org.telegram.ui.Components.ShareAlert
            protected void onShareStory(View view) {
                PeerStoriesView.this.tryToOpenRepostStory();
            }
        };
        this.shareAlert = shareAlert;
        shareAlert.forceDarkThemeForHint = true;
        TL_stories$StoryItem tL_stories$StoryItem = this.currentStory.storyItem;
        tL_stories$StoryItem.dialogId = this.dialogId;
        shareAlert.setStoryToShare(tL_stories$StoryItem);
        this.shareAlert.setDelegate(new ShareAlert.ShareAlertDelegate() { // from class: org.telegram.ui.Stories.PeerStoriesView.26
            @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
            public boolean didCopy() {
                PeerStoriesView.this.onLinkCopied();
                return true;
            }

            @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
            public /* synthetic */ void didShare() {
                ShareAlert.ShareAlertDelegate.-CC.$default$didShare(this);
            }
        });
        this.delegate.showDialog(this.shareAlert);
    }

    private void showAttachmentError() {
        BulletinFactory.of(this.storyContainer, this.resourcesProvider).createErrorBulletin(LocaleController.getString(R.string.UnsupportedAttachment), this.resourcesProvider).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDownloadAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        builder.setTitle(LocaleController.getString(R.string.AppName));
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        builder.setMessage(LocaleController.getString(R.string.PleaseDownload));
        this.delegate.showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showLikesReaction(final boolean z) {
        if (this.likesReactionShowing == z || this.currentStory.storyItem == null) {
            return;
        }
        this.likesReactionShowing = z;
        if (z) {
            this.likesReactionLayout.setVisibility(0);
        }
        this.likesReactionLayout.setStoryItem(this.currentStory.storyItem);
        this.delegate.setIsLikesReaction(z);
        if (!z) {
            if (this.likesReactionLayout.getReactionsWindow() != null) {
                this.likesReactionLayout.getReactionsWindow().dismissWithAlpha();
            }
            this.likesReactionLayout.animate().alpha(0.0f).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.13
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    PeerStoriesView.this.likesReactionShowProgress = 0.0f;
                    PeerStoriesView.this.likesReactionLayout.setAlpha(1.0f);
                    PeerStoriesView.this.likesReactionLayout.setVisibility(8);
                    PeerStoriesView.this.likesReactionLayout.reset();
                }
            }).start();
            return;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.likesReactionShowProgress, z ? 1.0f : 0.0f);
        this.likesReactionLayout.setTransitionProgress(this.likesReactionShowProgress);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda26
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PeerStoriesView.this.lambda$showLikesReaction$16(valueAnimator);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.12
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (z) {
                    return;
                }
                PeerStoriesView.this.likesReactionLayout.setVisibility(8);
                PeerStoriesView.this.likesReactionLayout.reset();
            }
        });
        ofFloat.setDuration(200L);
        ofFloat.setInterpolator(CubicBezierInterpolator.EASE_OUT);
        ofFloat.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPremiumBlockedToast() {
        if (!this.isGroup) {
            ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
            int i = -this.shiftDp;
            this.shiftDp = i;
            AndroidUtilities.shakeViewSpring(chatActivityEnterView, i);
            BotWebViewVibrationEffect.APP_ERROR.vibrate();
            String userName = this.dialogId >= 0 ? UserObject.getUserName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId))) : "";
            (MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked() ? BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.star_premium_2, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.UserBlockedRepliesNonPremium, userName))) : BulletinFactory.of(this.storyContainer, this.resourcesProvider).createSimpleBulletin(R.raw.star_premium_2, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.UserBlockedRepliesNonPremium, userName)), LocaleController.getString(R.string.UserBlockedNonPremiumButton), new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.this.lambda$showPremiumBlockedToast$23();
                }
            })).show();
        } else if (this.boostsStatus != null && this.canApplyBoost != null) {
            LimitReachedBottomSheet.openBoostsForRemoveRestrictions(fragmentForLimit(), this.boostsStatus, this.canApplyBoost, this.dialogId, true);
        } else {
            StoryViewer storyViewer = this.storyViewer;
            if (storyViewer != null) {
                storyViewer.setOverlayVisible(true);
            }
            MessagesController.getInstance(this.currentAccount).getBoostsController().getBoostsStats(this.dialogId, new Consumer() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda20
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    PeerStoriesView.this.lambda$showPremiumBlockedToast$22((TL_stories$TL_premium_boostsStatus) obj);
                }
            });
        }
    }

    private void showUserViewsDialog() {
        this.storyViewer.openViews();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleArchiveForStory(final long j) {
        String str;
        boolean z;
        TLRPC$Chat tLRPC$Chat;
        int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        if (i > 0) {
            TLRPC$User user = messagesController.getUser(Long.valueOf(j));
            str = user.first_name;
            z = user.stories_hidden;
            tLRPC$Chat = user;
        } else {
            TLRPC$Chat chat = messagesController.getChat(Long.valueOf(-j));
            str = chat.title;
            z = chat.stories_hidden;
            tLRPC$Chat = chat;
        }
        final TLRPC$Chat tLRPC$Chat2 = tLRPC$Chat;
        final String str2 = str;
        final boolean z2 = !z;
        final MessagesController messagesController2 = MessagesController.getInstance(this.currentAccount);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.this.lambda$toggleArchiveForStory$19(messagesController2, j, z2, str2, tLRPC$Chat2);
            }
        }, 200L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryToOpenRepostStory() {
        if (MessagesController.getInstance(this.currentAccount).storiesEnabled()) {
            File path = this.currentStory.getPath();
            if (path == null || !path.exists()) {
                showDownloadAlert();
                return;
            }
            ShareAlert shareAlert = this.shareAlert;
            if (shareAlert != null) {
                shareAlert.dismiss();
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    PeerStoriesView.this.openRepostStory();
                }
            }, 120L);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:191:0x047c, code lost:
        r15 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x069e, code lost:
        if (r4 != null) goto L438;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x086c, code lost:
        if (r1.captionTranslated == (r4 == null && r4.translated && r4.translatedText != null && android.text.TextUtils.equals(r4.translatedLng, org.telegram.ui.Components.TranslateAlert2.getToLanguage()))) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x08b4, code lost:
        if (r1 != null) goto L102;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0494 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:201:0x04aa  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x04c2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:215:0x04da  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x04f4  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x0519  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x055f  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x0572  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x058b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:261:0x05a5  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x05c2  */
    /* JADX WARN: Removed duplicated region for block: B:322:0x07f8  */
    /* JADX WARN: Removed duplicated region for block: B:337:0x083d  */
    /* JADX WARN: Removed duplicated region for block: B:340:0x0844  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x084d  */
    /* JADX WARN: Removed duplicated region for block: B:359:0x0879  */
    /* JADX WARN: Removed duplicated region for block: B:368:0x088e  */
    /* JADX WARN: Removed duplicated region for block: B:378:0x08bc  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:441:0x097b  */
    /* JADX WARN: Removed duplicated region for block: B:449:0x099a  */
    /* JADX WARN: Removed duplicated region for block: B:459:0x09b1  */
    /* JADX WARN: Removed duplicated region for block: B:460:0x09b3  */
    /* JADX WARN: Removed duplicated region for block: B:468:0x09dc  */
    /* JADX WARN: Removed duplicated region for block: B:473:0x09ef  */
    /* JADX WARN: Removed duplicated region for block: B:489:0x0a18  */
    /* JADX WARN: Removed duplicated region for block: B:503:0x0a5a  */
    /* JADX WARN: Removed duplicated region for block: B:506:0x0a70  */
    /* JADX WARN: Removed duplicated region for block: B:511:0x0a85  */
    /* JADX WARN: Removed duplicated region for block: B:519:0x0aad  */
    /* JADX WARN: Removed duplicated region for block: B:522:0x0abc  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0166  */
    /* JADX WARN: Removed duplicated region for block: B:531:0x0afe  */
    /* JADX WARN: Removed duplicated region for block: B:534:0x0b09  */
    /* JADX WARN: Removed duplicated region for block: B:542:0x0b1b  */
    /* JADX WARN: Removed duplicated region for block: B:561:0x0b4f  */
    /* JADX WARN: Removed duplicated region for block: B:562:0x0b58  */
    /* JADX WARN: Removed duplicated region for block: B:565:0x0b5e  */
    /* JADX WARN: Removed duplicated region for block: B:574:0x0b84  */
    /* JADX WARN: Removed duplicated region for block: B:586:0x0bcc  */
    /* JADX WARN: Removed duplicated region for block: B:596:0x0c1c  */
    /* JADX WARN: Removed duplicated region for block: B:601:0x0c56  */
    /* JADX WARN: Removed duplicated region for block: B:604:0x0c6b  */
    /* JADX WARN: Removed duplicated region for block: B:610:0x0c93  */
    /* JADX WARN: Removed duplicated region for block: B:621:0x0cb3  */
    /* JADX WARN: Removed duplicated region for block: B:631:0x0cda  */
    /* JADX WARN: Removed duplicated region for block: B:634:0x0cea  */
    /* JADX WARN: Removed duplicated region for block: B:646:0x0a9f A[EDGE_INSN: B:646:0x0a9f->B:517:0x0a9f ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:650:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r2v88 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updatePosition(boolean z) {
        TL_stories$StoryItem tL_stories$StoryItem;
        TL_stories$StoryItem tL_stories$StoryItem2;
        StoriesController.UploadingStory uploadingStory;
        StoriesController.UploadingStory uploadingStory2;
        boolean z2;
        boolean z3;
        boolean z4;
        int i;
        StoryViewer.TransitionViewHolder transitionViewHolder;
        ImageReceiver imageReceiver;
        boolean z5;
        ArrayList arrayList;
        Drawable createStripedBitmap;
        ImageLocation forPhoto;
        String str;
        int i2;
        ImageLocation imageLocation;
        ImageLocation imageLocation2;
        ImageReceiver imageReceiver2;
        String str2;
        String str3;
        long j;
        boolean z6;
        Drawable drawable;
        Object obj;
        int i3;
        ImageLocation imageLocation3;
        String str4;
        long j2;
        String str5;
        String str6;
        boolean z7;
        TL_stories$StoryItem tL_stories$StoryItem3;
        TL_stories$StoryItem tL_stories$StoryItem4;
        ChatActivityEnterView chatActivityEnterView;
        StoriesController.UploadingStory uploadingStory3;
        boolean z8;
        TL_stories$StoryItem tL_stories$StoryItem5;
        StoriesController.UploadingStory uploadingStory4;
        String str7;
        CharSequence charSequence;
        TL_stories$StoryItem tL_stories$StoryItem6;
        SpannableStringBuilder spannableStringBuilder;
        CharSequence charSequence2;
        String str8;
        int i4;
        TLRPC$MessageMedia tLRPC$MessageMedia;
        int i5;
        HintView2 hintView2;
        HintView2 hintView22;
        StoriesController.StoriesList storiesList;
        TL_stories$StoryItem tL_stories$StoryItem7;
        CharSequence charSequence3;
        TL_stories$StoryItem tL_stories$StoryItem8;
        StoryItemHolder storyItemHolder;
        Delegate delegate;
        ?? r2;
        ChatActivityEnterView chatActivityEnterView2;
        LinearLayout linearLayout;
        FrameLayout frameLayout;
        ChatActivityEnterView chatActivityEnterView3;
        StoryItemHolder storyItemHolder2;
        boolean z9;
        TL_stories$StoryItem tL_stories$StoryItem9;
        StoriesController.UploadingStory uploadingStory5;
        StoriesController.UploadingStory uploadingStory6;
        ViewPropertyAnimator withEndAction;
        StoriesController.StoriesList storiesList2;
        int i6;
        int i7;
        HintView2 hintView23;
        ImageView imageView;
        StoryItemHolder storyItemHolder3;
        TLRPC$Reaction tLRPC$Reaction;
        TL_stories$StoryItem tL_stories$StoryItem10;
        int i8;
        int i9;
        String str9;
        TL_stories$StoryItem tL_stories$StoryItem11;
        BitmapDrawable bitmapDrawable;
        StoriesController.UploadingStory uploadingStory7;
        ImageReceiver imageReceiver3;
        ImageLocation forPath;
        Object obj2;
        ImageLocation imageLocation4;
        String str10;
        ImageLocation imageLocation5;
        long j3;
        String str11;
        String str12;
        String str13;
        BitmapDrawable bitmapDrawable2;
        int i10;
        StoriesController.StoriesList storiesList3;
        if (this.storyItems.isEmpty() && this.uploadingStories.isEmpty()) {
            return;
        }
        this.forceUpdateOffsets = true;
        StoryItemHolder storyItemHolder4 = this.currentStory;
        TL_stories$StoryItem tL_stories$StoryItem12 = storyItemHolder4.storyItem;
        StoriesController.UploadingStory uploadingStory8 = storyItemHolder4.uploadingStory;
        String storyImageFilter = StoriesUtilities.getStoryImageFilter();
        this.lastNoThumb = false;
        this.unsupported = false;
        int i11 = this.selectedPosition;
        boolean z10 = this.isUploading;
        boolean z11 = this.isEditing;
        boolean z12 = this.isFailed;
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer == null || (storiesList3 = storyViewer.storiesList) == null || storiesList3.type != 4) {
            tL_stories$StoryItem = (i11 < 0 || i11 >= this.storyItems.size()) ? null : (TL_stories$StoryItem) this.storyItems.get(i11);
            int size = i11 - this.storyItems.size();
            if (size < 0 || size >= this.uploadingStories.size()) {
                tL_stories$StoryItem2 = tL_stories$StoryItem;
                uploadingStory = null;
                this.currentStory.editingSourceItem = null;
                if (uploadingStory == null) {
                    this.isEditing = false;
                    boolean z13 = uploadingStory.failed;
                    this.isFailed = z13;
                    this.isUploading = !z13;
                    this.imageReceiver.setCrossfadeWithOldImage(false);
                    this.imageReceiver.setCrossfadeDuration(150);
                    Bitmap bitmap = uploadingStory.entry.thumbBitmap;
                    if (bitmap != null) {
                        Bitmap createBitmap = Bitmap.createBitmap(bitmap);
                        Utilities.blurBitmap(createBitmap, 3, 1, createBitmap.getWidth(), createBitmap.getHeight(), createBitmap.getRowBytes());
                        bitmapDrawable = new BitmapDrawable(createBitmap);
                    } else {
                        bitmapDrawable = null;
                    }
                    if (uploadingStory.isVideo || uploadingStory.hadFailed) {
                        uploadingStory7 = uploadingStory;
                        z2 = z12;
                        z3 = z10;
                        z4 = z11;
                        i = i11;
                        imageReceiver3 = this.imageReceiver;
                        forPath = ImageLocation.getForPath(uploadingStory7.firstFramePath);
                        obj2 = null;
                        imageLocation4 = null;
                        str10 = null;
                        imageLocation5 = null;
                        j3 = 0;
                        str11 = null;
                        str12 = storyImageFilter;
                        str13 = null;
                        bitmapDrawable2 = bitmapDrawable;
                        i10 = 0;
                    } else {
                        imageReceiver3 = this.imageReceiver;
                        imageLocation4 = null;
                        str10 = null;
                        uploadingStory7 = uploadingStory;
                        forPath = ImageLocation.getForPath(uploadingStory.path);
                        str12 = storyImageFilter;
                        imageLocation5 = null;
                        str13 = null;
                        z2 = z12;
                        bitmapDrawable2 = bitmapDrawable;
                        z3 = z10;
                        z4 = z11;
                        j3 = 0;
                        i = i11;
                        str11 = null;
                        obj2 = null;
                        i10 = 0;
                    }
                    imageReceiver3.setImage(imageLocation4, str10, forPath, str12, imageLocation5, str13, bitmapDrawable2, j3, str11, obj2, i10);
                    this.currentStory.set(uploadingStory7);
                    this.storyAreasView.set(null, StoryMediaAreasView.getMediaAreasFor(uploadingStory7.entry), this.emojiAnimationsOverlay);
                } else {
                    z2 = z12;
                    z3 = z10;
                    z4 = z11;
                    i = i11;
                    this.isUploading = false;
                    this.isEditing = false;
                    this.isFailed = false;
                    if (tL_stories$StoryItem2 == null) {
                        this.storyViewer.close(true);
                        return;
                    }
                    StoriesController.UploadingStory findEditingStory = this.storiesController.findEditingStory(this.dialogId, tL_stories$StoryItem2);
                    if (findEditingStory != null) {
                        this.isEditing = true;
                        this.imageReceiver.setCrossfadeWithOldImage(false);
                        this.imageReceiver.setCrossfadeDuration(this.onImageReceiverThumbLoaded != null ? 0 : 150);
                        this.imageReceiver.setImage(null, null, ImageLocation.getForPath(findEditingStory.firstFramePath), storyImageFilter, null, 0L, null, null, 0);
                        this.currentStory.set(findEditingStory);
                        this.storyAreasView.set(null, StoryMediaAreasView.getMediaAreasFor(findEditingStory.entry), this.emojiAnimationsOverlay);
                        this.currentStory.editingSourceItem = tL_stories$StoryItem2;
                    } else {
                        TL_stories$StoryItem tL_stories$StoryItem13 = tL_stories$StoryItem2;
                        TLRPC$MessageMedia tLRPC$MessageMedia2 = tL_stories$StoryItem13.media;
                        boolean z14 = tLRPC$MessageMedia2 != null && MessageObject.isVideoDocument(tLRPC$MessageMedia2.getDocument());
                        tL_stories$StoryItem13.dialogId = this.dialogId;
                        this.imageReceiver.setCrossfadeWithOldImage(z4);
                        this.imageReceiver.setCrossfadeDuration(150);
                        TLRPC$MessageMedia tLRPC$MessageMedia3 = tL_stories$StoryItem13.media;
                        if (tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaUnsupported) {
                            this.unsupported = true;
                            z5 = z4;
                        } else {
                            String str14 = tL_stories$StoryItem13.attachPath;
                            if (str14 != null) {
                                if (tLRPC$MessageMedia3 == null) {
                                    z14 = str14.toLowerCase().endsWith(".mp4");
                                }
                                if (z14) {
                                    TLRPC$MessageMedia tLRPC$MessageMedia4 = tL_stories$StoryItem13.media;
                                    Drawable createStripedBitmap2 = tLRPC$MessageMedia4 != null ? ImageLoader.createStripedBitmap(tLRPC$MessageMedia4.getDocument().thumbs) : null;
                                    if (tL_stories$StoryItem13.firstFramePath != null) {
                                        if (ImageLoader.getInstance().isInMemCache(ImageLocation.getForPath(tL_stories$StoryItem13.firstFramePath).getKey(null, null, false) + "@" + storyImageFilter, false)) {
                                            z7 = z4;
                                            this.imageReceiver.setImage(null, null, ImageLocation.getForPath(tL_stories$StoryItem13.firstFramePath), storyImageFilter, null, null, createStripedBitmap2, 0L, null, null, 0);
                                            z5 = z7;
                                        }
                                    }
                                    z7 = z4;
                                    this.imageReceiver.setImage(null, null, ImageLocation.getForPath(tL_stories$StoryItem13.attachPath), storyImageFilter + "_pframe", null, null, createStripedBitmap2, 0L, null, null, 0);
                                    z5 = z7;
                                } else {
                                    TLRPC$MessageMedia tLRPC$MessageMedia5 = tL_stories$StoryItem13.media;
                                    TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia5 != null ? tLRPC$MessageMedia5.photo : null;
                                    if (tLRPC$Photo != null) {
                                        drawable = ImageLoader.createStripedBitmap(tLRPC$Photo.sizes);
                                        z6 = z4;
                                    } else {
                                        z6 = z4;
                                        drawable = null;
                                    }
                                    ImageReceiver imageReceiver4 = this.imageReceiver;
                                    ImageLocation forPath2 = ImageLocation.getForPath(tL_stories$StoryItem13.attachPath);
                                    if (z6) {
                                        imageLocation3 = ImageLocation.getForPath(tL_stories$StoryItem13.firstFramePath);
                                        obj = null;
                                        i3 = 0;
                                        j2 = 0;
                                        str5 = null;
                                        str6 = storyImageFilter;
                                        str4 = storyImageFilter;
                                    } else {
                                        obj = null;
                                        i3 = 0;
                                        imageLocation3 = null;
                                        str4 = null;
                                        j2 = 0;
                                        str5 = null;
                                        str6 = storyImageFilter;
                                    }
                                    imageReceiver4.setImage(forPath2, str6, imageLocation3, str4, drawable, j2, str5, obj, i3);
                                    z5 = z6;
                                }
                            } else {
                                StoryViewer storyViewer2 = this.storyViewer;
                                Drawable drawable2 = ((storyViewer2.storiesList != null || storyViewer2.isSingleStory) && (transitionViewHolder = storyViewer2.transitionViewHolder) != null && (imageReceiver = transitionViewHolder.storyImage) != null && transitionViewHolder.storyId == tL_stories$StoryItem13.id) ? imageReceiver.getDrawable() : null;
                                tL_stories$StoryItem13.dialogId = this.dialogId;
                                if (z14) {
                                    TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tL_stories$StoryItem13.media.getDocument().thumbs, 1000);
                                    createStripedBitmap = drawable2 == null ? ImageLoader.createStripedBitmap(tL_stories$StoryItem13.media.getDocument().thumbs) : drawable2;
                                    ImageReceiver imageReceiver5 = this.imageReceiver;
                                    forPhoto = ImageLocation.getForDocument(tL_stories$StoryItem13.media.getDocument());
                                    str3 = storyImageFilter + "_pframe";
                                    imageLocation2 = ImageLocation.getForDocument(closestPhotoSizeWithSize, tL_stories$StoryItem13.media.getDocument());
                                    i2 = 0;
                                    imageReceiver2 = imageReceiver5;
                                    imageLocation = null;
                                    str2 = null;
                                    j = 0;
                                    z5 = z4;
                                    str = null;
                                } else {
                                    z5 = z4;
                                    TLRPC$MessageMedia tLRPC$MessageMedia6 = tL_stories$StoryItem13.media;
                                    TLRPC$Photo tLRPC$Photo2 = tLRPC$MessageMedia6 != null ? tLRPC$MessageMedia6.photo : null;
                                    if (tLRPC$Photo2 == null || (arrayList = tLRPC$Photo2.sizes) == null) {
                                        this.imageReceiver.clearImage();
                                    } else {
                                        createStripedBitmap = drawable2 == null ? ImageLoader.createStripedBitmap(arrayList) : drawable2;
                                        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo2.sizes, ConnectionsManager.DEFAULT_DATACENTER_ID);
                                        FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo2.sizes, 800);
                                        ImageReceiver imageReceiver6 = this.imageReceiver;
                                        forPhoto = ImageLocation.getForPhoto(closestPhotoSizeWithSize2, tLRPC$Photo2);
                                        str = null;
                                        i2 = 0;
                                        imageLocation = null;
                                        imageLocation2 = null;
                                        imageReceiver2 = imageReceiver6;
                                        str2 = null;
                                        str3 = storyImageFilter;
                                        storyImageFilter = null;
                                        j = 0;
                                    }
                                }
                                imageReceiver2.setImage(imageLocation, str2, forPhoto, str3, imageLocation2, storyImageFilter, createStripedBitmap, j, str, tL_stories$StoryItem13, i2);
                            }
                        }
                        tL_stories$StoryItem13.dialogId = this.dialogId;
                        this.storyAreasView.set(z ? null : tL_stories$StoryItem13, this.emojiAnimationsOverlay);
                        this.currentStory.set(tL_stories$StoryItem13);
                        boolean z15 = (this.unsupported || (tL_stories$StoryItem3 = this.currentStory.storyItem) == null || (tL_stories$StoryItem3 instanceof TL_stories$TL_storyItemDeleted) || (tL_stories$StoryItem3 instanceof TL_stories$TL_storyItemSkipped)) ? false : true;
                        this.allowShareLink = z15;
                        this.allowShare = z15;
                        if (z15) {
                            this.allowShare = this.currentStory.allowScreenshots() && this.currentStory.storyItem.isPublic;
                        }
                        if (this.allowShare) {
                            TL_stories$StoryItem tL_stories$StoryItem14 = this.currentStory.storyItem;
                            this.allowShare = tL_stories$StoryItem14.pinned || !StoriesUtilities.isExpired(this.currentAccount, tL_stories$StoryItem14);
                        }
                        boolean z16 = this.allowShare;
                        this.allowRepost = z16;
                        if (z16 && this.isChannel) {
                            TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
                            this.allowRepost = chat != null && ChatObject.isPublic(chat);
                        }
                        if (this.allowShareLink) {
                            boolean z17 = this.isChannel ? false : false;
                            this.allowShareLink = z17;
                        }
                        NotificationsController.getInstance(this.currentAccount).processReadStories(this.dialogId, tL_stories$StoryItem13.id);
                        tL_stories$StoryItem4 = this.currentStory.storyItem;
                        if (tL_stories$StoryItem4 != null && !z) {
                            this.storyViewer.dayStoryId = tL_stories$StoryItem4.id;
                        }
                        this.storyViewer.storiesViewPager.checkAllowScreenshots();
                        this.imageChanged = true;
                        if (!this.isSelf || this.isChannel) {
                            updateUserViews(false);
                        }
                        StoryItemHolder storyItemHolder5 = this.currentStory;
                        boolean z18 = (getStoryId(storyItemHolder5.storyItem, storyItemHolder5.uploadingStory) == getStoryId(tL_stories$StoryItem12, uploadingStory8) && (uploadingStory8 == null || (tL_stories$StoryItem11 = this.currentStory.storyItem) == null || !TextUtils.equals(uploadingStory8.path, tL_stories$StoryItem11.attachPath))) ? false : true;
                        boolean z19 = (z18 || (this.isEditing == z5 && this.isUploading == z3 && this.isFailed == z2)) ? false : true;
                        if (!(uploadingStory8 == null && (str9 = uploadingStory8.path) != null && str9.equals(this.currentStory.getLocalPath())) && (tL_stories$StoryItem12 == null || (tL_stories$StoryItem5 = this.currentStory.storyItem) == null || tL_stories$StoryItem12.id != tL_stories$StoryItem5.id)) {
                            chatActivityEnterView = this.chatActivityEnterView;
                            if (chatActivityEnterView != null) {
                                if (tL_stories$StoryItem12 != null && !TextUtils.isEmpty(chatActivityEnterView.getEditField().getText())) {
                                    this.storyViewer.saveDraft(tL_stories$StoryItem12.dialogId, tL_stories$StoryItem12, this.chatActivityEnterView.getEditField().getText());
                                }
                                this.chatActivityEnterView.getEditField().setText(this.storyViewer.getDraft(this.dialogId, this.currentStory.storyItem));
                            }
                            this.emojiAnimationsOverlay.clear();
                            this.currentImageTime = 0L;
                            this.switchEventSent = false;
                            uploadingStory3 = this.currentStory.uploadingStory;
                            if (uploadingStory3 != null) {
                                RadialProgress radialProgress = this.headerView.radialProgress;
                                if (radialProgress != null) {
                                    radialProgress.setProgress(uploadingStory3.progress, false);
                                }
                                this.headerView.backupImageView.invalidate();
                            } else if (!z19) {
                                this.headerView.progressToUploading = 0.0f;
                            }
                            Bulletin.hideVisible(this.storyContainer);
                            this.storyCaptionView.reset();
                            cancelWaiting();
                            z8 = true;
                        } else {
                            z8 = false;
                        }
                        if (!z8 || (uploadingStory8 != null && this.currentStory.uploadingStory == null)) {
                            this.headerView.setOnSubtitleClick(null);
                            uploadingStory4 = this.currentStory.uploadingStory;
                            if (uploadingStory4 != null) {
                                if (uploadingStory4.failed) {
                                    i4 = R.string.FailedToUploadStory;
                                    charSequence3 = LocaleController.getString(i4);
                                    str7 = " ";
                                    charSequence = charSequence3;
                                } else {
                                    charSequence3 = StoriesUtilities.getUploadingStr(this.headerView.subtitleView[0], false, this.isEditing);
                                    str7 = " ";
                                    charSequence = charSequence3;
                                }
                            } else if (isBotsPreview()) {
                                TL_stories$StoryItem tL_stories$StoryItem15 = this.currentStory.storyItem;
                                if (tL_stories$StoryItem15 != null && (tLRPC$MessageMedia = tL_stories$StoryItem15.media) != null) {
                                    TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
                                    if (tLRPC$Document != null) {
                                        i5 = tLRPC$Document.date;
                                    } else {
                                        TLRPC$Photo tLRPC$Photo3 = tLRPC$MessageMedia.photo;
                                        if (tLRPC$Photo3 != null) {
                                            i5 = tLRPC$Photo3.date;
                                        }
                                    }
                                    charSequence3 = LocaleController.formatStoryDate(i5);
                                    str7 = " ";
                                    charSequence = charSequence3;
                                }
                                charSequence3 = "";
                                str7 = " ";
                                charSequence = charSequence3;
                            } else {
                                StoryItemHolder storyItemHolder6 = this.currentStory;
                                TL_stories$StoryItem tL_stories$StoryItem16 = storyItemHolder6.storyItem;
                                if (tL_stories$StoryItem16 == null) {
                                    str7 = " ";
                                    charSequence = null;
                                } else if (tL_stories$StoryItem16.date == -1) {
                                    i4 = R.string.CachedStory;
                                    charSequence3 = LocaleController.getString(i4);
                                    str7 = " ";
                                    charSequence = charSequence3;
                                } else {
                                    if (storyItemHolder6.getReply() != null) {
                                        final StoryCaptionView.Reply reply = this.currentStory.getReply();
                                        spannableStringBuilder = new SpannableStringBuilder();
                                        SpannableString spannableString = new SpannableString("r");
                                        spannableString.setSpan(new ColoredImageSpan(R.drawable.mini_repost_story), 0, spannableString.length(), 33);
                                        spannableStringBuilder.append((CharSequence) spannableString).append((CharSequence) " ");
                                        if (reply.peerId != null) {
                                            AvatarSpan avatarSpan = new AvatarSpan(this.headerView.subtitleView[0], this.currentAccount, 15.0f);
                                            SpannableString spannableString2 = new SpannableString("a");
                                            spannableString2.setSpan(avatarSpan, 0, 1, 33);
                                            spannableStringBuilder.append((CharSequence) spannableString2).append((CharSequence) " ");
                                            if (reply.peerId.longValue() > 0) {
                                                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(reply.peerId);
                                                avatarSpan.setUser(user);
                                                spannableStringBuilder.append((CharSequence) UserObject.getUserName(user));
                                                charSequence2 = ".";
                                            } else {
                                                charSequence2 = ".";
                                                TLRPC$Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-reply.peerId.longValue()));
                                                avatarSpan.setChat(chat2);
                                                if (chat2 != null) {
                                                    str8 = chat2.title;
                                                    spannableStringBuilder.append((CharSequence) str8);
                                                }
                                            }
                                            this.headerView.setOnSubtitleClick(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda16
                                                @Override // android.view.View.OnClickListener
                                                public final void onClick(View view) {
                                                    PeerStoriesView.this.lambda$updatePosition$38(reply, view);
                                                }
                                            });
                                            SpannableString spannableString3 = new SpannableString(charSequence2);
                                            DotDividerSpan dotDividerSpan = new DotDividerSpan();
                                            dotDividerSpan.setTopPadding(AndroidUtilities.dp(1.5f));
                                            dotDividerSpan.setSize(5.0f);
                                            spannableString3.setSpan(dotDividerSpan, 0, spannableString3.length(), 33);
                                            spannableStringBuilder.append((CharSequence) " ").append((CharSequence) spannableString3).append((CharSequence) " ").append((CharSequence) LocaleController.formatShortDate(this.currentStory.storyItem.date));
                                            str7 = " ";
                                        } else {
                                            charSequence2 = ".";
                                            str8 = this.currentStory.storyItem.fwd_from.from_name;
                                        }
                                    } else if (!this.isGroup || (tL_stories$StoryItem6 = this.currentStory.storyItem) == null || tL_stories$StoryItem6.from_id == null) {
                                        str7 = " ";
                                        CharSequence formatStoryDate = LocaleController.formatStoryDate(this.currentStory.storyItem.date);
                                        CharSequence charSequence4 = formatStoryDate;
                                        if (this.currentStory.storyItem.edited) {
                                            SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(formatStoryDate);
                                            DotDividerSpan dotDividerSpan2 = new DotDividerSpan();
                                            dotDividerSpan2.setTopPadding(AndroidUtilities.dp(1.5f));
                                            dotDividerSpan2.setSize(5.0f);
                                            valueOf.append((CharSequence) " . ").setSpan(dotDividerSpan2, valueOf.length() - 2, valueOf.length() - 1, 0);
                                            valueOf.append((CharSequence) LocaleController.getString(R.string.EditedMessage));
                                            charSequence4 = valueOf;
                                        }
                                        charSequence = charSequence4;
                                    } else {
                                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
                                        AvatarSpan avatarSpan2 = new AvatarSpan(this.headerView.subtitleView[0], this.currentAccount, 15.0f);
                                        SpannableString spannableString4 = new SpannableString("a");
                                        spannableString4.setSpan(avatarSpan2, 0, 1, 33);
                                        spannableStringBuilder2.append((CharSequence) spannableString4).append((CharSequence) " ");
                                        final long peerDialogId = DialogObject.getPeerDialogId(this.currentStory.storyItem.from_id);
                                        if (peerDialogId > 0) {
                                            TLRPC$User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peerDialogId));
                                            avatarSpan2.setUser(user2);
                                            spannableStringBuilder2.append((CharSequence) UserObject.getUserName(user2));
                                            str7 = " ";
                                        } else {
                                            str7 = " ";
                                            TLRPC$Chat chat3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-peerDialogId));
                                            avatarSpan2.setChat(chat3);
                                            if (chat3 != null) {
                                                spannableStringBuilder2.append((CharSequence) chat3.title);
                                            }
                                        }
                                        this.headerView.setOnSubtitleClick(new View.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda17
                                            @Override // android.view.View.OnClickListener
                                            public final void onClick(View view) {
                                                PeerStoriesView.this.lambda$updatePosition$39(peerDialogId, view);
                                            }
                                        });
                                        SpannableString spannableString5 = new SpannableString(".");
                                        DotDividerSpan dotDividerSpan3 = new DotDividerSpan();
                                        dotDividerSpan3.setTopPadding(AndroidUtilities.dp(1.5f));
                                        dotDividerSpan3.setSize(5.0f);
                                        spannableString5.setSpan(dotDividerSpan3, 0, spannableString5.length(), 33);
                                        spannableStringBuilder2.append((CharSequence) str7).append((CharSequence) spannableString5).append((CharSequence) str7).append((CharSequence) LocaleController.formatShortDate(this.currentStory.storyItem.date));
                                        spannableStringBuilder = spannableStringBuilder2;
                                    }
                                    z19 = false;
                                    charSequence = spannableStringBuilder;
                                }
                            }
                            if (charSequence != null) {
                                StoryViewer storyViewer3 = this.storyViewer;
                                if (storyViewer3 != null && (storiesList = storyViewer3.storiesList) != null && (tL_stories$StoryItem7 = this.currentStory.storyItem) != null && storiesList.isPinned(tL_stories$StoryItem7.id)) {
                                    if (!(charSequence instanceof SpannableStringBuilder)) {
                                        charSequence = new SpannableStringBuilder(charSequence);
                                    }
                                    SpannableString spannableString6 = new SpannableString("p ");
                                    spannableString6.setSpan(new ColoredImageSpan(R.drawable.msg_pin_mini), 0, 1, 33);
                                    ((SpannableStringBuilder) charSequence).insert(0, (CharSequence) spannableString6);
                                }
                                this.headerView.setSubtitle(charSequence, z19);
                            }
                            hintView2 = this.privacyHint;
                            if (hintView2 != null) {
                                hintView2.hide(false);
                            }
                            hintView22 = this.soundTooltip;
                            if (hintView22 != null) {
                                hintView22.hide(false);
                            }
                        } else {
                            str7 = " ";
                        }
                        StoryItemHolder storyItemHolder7 = this.currentStory;
                        tL_stories$StoryItem8 = storyItemHolder7.storyItem;
                        if (tL_stories$StoryItem12 == tL_stories$StoryItem8 && uploadingStory8 == storyItemHolder7.uploadingStory) {
                        }
                        this.currentStory.updateCaption();
                        storyItemHolder = this.currentStory;
                        if ((storyItemHolder.captionTranslated && tL_stories$StoryItem12 == storyItemHolder.storyItem) || (delegate = this.delegate) == null) {
                            r2 = 0;
                        } else {
                            r2 = 0;
                            delegate.setTranslating(false);
                        }
                        if (this.unsupported) {
                            createUnsupportedContainer();
                            createReplyDisabledView();
                            this.unsupportedContainer.setVisibility(r2);
                            this.replyDisabledTextView.setVisibility(r2);
                            this.allowShareLink = r2;
                            this.allowRepost = r2;
                            this.allowShare = r2;
                            ChatActivityEnterView chatActivityEnterView4 = this.chatActivityEnterView;
                            if (chatActivityEnterView4 != null) {
                                chatActivityEnterView4.setVisibility(8);
                            }
                            FrameLayout frameLayout2 = this.selfView;
                            if (frameLayout2 != null) {
                                frameLayout2.setVisibility(8);
                            }
                            linearLayout = this.bottomActionsLinearLayout;
                        } else {
                            TLRPC$Chat chat4 = this.dialogId < 0 ? MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId)) : null;
                            if ((UserObject.isService(this.dialogId) || isBotsPreview()) && (chatActivityEnterView2 = this.chatActivityEnterView) != null) {
                                chatActivityEnterView2.setVisibility(8);
                            } else if (!this.isSelf && ((!this.isChannel || (this.isGroup && (ChatObject.canSendPlain(chat4) || ChatObject.isPossibleRemoveChatRestrictionsByBoosts(chat4)))) && (chatActivityEnterView3 = this.chatActivityEnterView) != null)) {
                                chatActivityEnterView3.setVisibility(0);
                            }
                            if (this.isPremiumBlocked && this.premiumBlockedText == null) {
                                createPremiumBlockedText();
                            }
                            if (this.premiumBlockedText != null) {
                                if (this.isPremiumBlocked) {
                                    updatePremiumBlockedText();
                                }
                                this.premiumBlockedText.setVisibility(this.isPremiumBlocked ? 0 : 8);
                            }
                            ChatActivityEnterView chatActivityEnterView5 = this.chatActivityEnterView;
                            if (chatActivityEnterView5 != null) {
                                chatActivityEnterView5.setEnabled(!this.isPremiumBlocked);
                            }
                            if (this.isSelf && (frameLayout = this.selfView) != null) {
                                frameLayout.setVisibility(0);
                            }
                            FrameLayout frameLayout3 = this.unsupportedContainer;
                            if (frameLayout3 != null) {
                                frameLayout3.setVisibility(8);
                            }
                            if (UserObject.isService(this.dialogId)) {
                                createReplyDisabledView();
                                this.replyDisabledTextView.setVisibility(0);
                            } else {
                                TextView textView = this.replyDisabledTextView;
                                if (textView != null) {
                                    textView.setVisibility(8);
                                }
                            }
                            linearLayout = this.bottomActionsLinearLayout;
                            if (linearLayout != null) {
                                i9 = isBotsPreview() ? 8 : 8;
                                i9 = 0;
                            }
                            storyItemHolder2 = this.currentStory;
                            if ((storyItemHolder2.caption == null || storyItemHolder2.getReply() != null) && !this.unsupported) {
                                StoryCaptionView.StoryCaptionTextView storyCaptionTextView = this.storyCaptionView.captionTextview;
                                StoryItemHolder storyItemHolder8 = this.currentStory;
                                CharSequence charSequence5 = storyItemHolder8.caption;
                                StoryCaptionView.Reply reply2 = storyItemHolder8.getReply();
                                if (this.storyViewer.isTranslating) {
                                    StoryItemHolder storyItemHolder9 = this.currentStory;
                                    if (!storyItemHolder9.captionTranslated && (tL_stories$StoryItem9 = storyItemHolder9.storyItem) != null && tL_stories$StoryItem9.translated) {
                                        z9 = true;
                                        storyCaptionTextView.setText(charSequence5, reply2, z9, tL_stories$StoryItem12 == this.currentStory.storyItem);
                                        this.storyCaptionView.setVisibility(0);
                                    }
                                }
                                z9 = false;
                                storyCaptionTextView.setText(charSequence5, reply2, z9, tL_stories$StoryItem12 == this.currentStory.storyItem);
                                this.storyCaptionView.setVisibility(0);
                            } else {
                                if (this.isActive) {
                                    this.delegate.setIsCaption(false);
                                    Delegate delegate2 = this.delegate;
                                    this.isCaptionPartVisible = false;
                                    delegate2.setIsCaptionPartVisible(false);
                                }
                                this.storyCaptionView.setVisibility(8);
                            }
                            this.storyContainer.invalidate();
                            if (this.delegate != null && isSelectedPeer()) {
                                this.delegate.onPeerSelected(this.dialogId, this.selectedPosition);
                            }
                            if (this.isChannel) {
                                this.shareButton.setVisibility(this.allowShare ? 0 : 4);
                                FrameLayout frameLayout4 = this.repostButtonContainer;
                                if (frameLayout4 != null) {
                                    frameLayout4.setVisibility(8);
                                }
                                this.likeButtonContainer.setVisibility(this.isSelf ? 8 : 0);
                                this.likeButtonContainer.getLayoutParams().width = AndroidUtilities.dp(40.0f);
                            } else {
                                this.shareButton.setVisibility(this.allowShare ? 0 : 4);
                                FrameLayout frameLayout5 = this.repostButtonContainer;
                                if (frameLayout5 != null) {
                                    frameLayout5.setVisibility(this.allowRepost ? 0 : 8);
                                }
                                this.likeButtonContainer.setVisibility(this.isFailed ? 8 : 0);
                            }
                            this.likeButtonContainer.requestLayout();
                            this.storyViewer.savedPositions.append(this.dialogId, i);
                            if (this.isActive) {
                                requestVideoPlayer(0L);
                                updatePreloadImages();
                                this.imageReceiver.bumpPriority();
                            }
                            this.listPosition = 0;
                            if (this.storyViewer.storiesList != null && (tL_stories$StoryItem10 = this.currentStory.storyItem) != null) {
                                int i12 = tL_stories$StoryItem10.id;
                                i8 = 0;
                                while (true) {
                                    if (i8 < this.storyViewer.storiesList.messageObjects.size()) {
                                        MessageObject messageObject = (MessageObject) this.storyViewer.storiesList.messageObjects.get(i8);
                                        if (messageObject != null && messageObject.getId() == i12) {
                                            this.listPosition = i8;
                                            break;
                                        }
                                        i8++;
                                    } else {
                                        break;
                                    }
                                }
                            }
                            int i13 = this.selectedPosition;
                            this.linesPosition = i13;
                            int i14 = this.count;
                            this.linesCount = i14;
                            if (this.storyViewer.reversed) {
                                this.linesPosition = (i14 - 1) - i13;
                            }
                            if (this.currentStory.isVideo()) {
                                this.muteIconContainer.setVisibility(8);
                            } else {
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
                            }
                            StoryItemHolder storyItemHolder10 = this.currentStory;
                            uploadingStory5 = storyItemHolder10.uploadingStory;
                            if (uploadingStory5 == null) {
                                this.privacyButton.set(this.isSelf, uploadingStory5, z18 && this.editedPrivacy);
                            } else {
                                TL_stories$StoryItem tL_stories$StoryItem17 = storyItemHolder10.storyItem;
                                if (tL_stories$StoryItem17 != null) {
                                    this.privacyButton.set(this.isSelf, tL_stories$StoryItem17, z18 && this.editedPrivacy);
                                } else {
                                    this.privacyButton.set(this.isSelf, (TL_stories$StoryItem) null, z18 && this.editedPrivacy);
                                }
                            }
                            this.editedPrivacy = false;
                            this.privacyButton.setTranslationX(this.muteIconContainer.getVisibility() != 0 ? -AndroidUtilities.dp(44.0f) : 0.0f);
                            if (z8) {
                                this.drawReactionEffect = false;
                                TL_stories$StoryItem tL_stories$StoryItem18 = this.currentStory.storyItem;
                                if (tL_stories$StoryItem18 == null || (tLRPC$Reaction = tL_stories$StoryItem18.sent_reaction) == null) {
                                    this.storiesLikeButton.setReaction(null);
                                } else {
                                    this.storiesLikeButton.setReaction(ReactionsLayoutInBubble.VisibleReaction.fromTL(tLRPC$Reaction));
                                }
                            }
                            uploadingStory6 = this.currentStory.uploadingStory;
                            if (uploadingStory6 != null || !uploadingStory6.failed) {
                                if (this.failView != null) {
                                    ViewPropertyAnimator viewPropertyAnimator = this.failViewAnimator;
                                    if (viewPropertyAnimator != null) {
                                        viewPropertyAnimator.cancel();
                                        this.failViewAnimator = null;
                                    }
                                    if (z18 && this.failView.getVisibility() == 0) {
                                        withEndAction = this.failView.animate().alpha(0.0f).setDuration(180L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).withEndAction(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda18
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                PeerStoriesView.this.lambda$updatePosition$40();
                                            }
                                        });
                                        this.failViewAnimator = withEndAction;
                                        withEndAction.start();
                                    } else {
                                        this.failView.setAlpha(0.0f);
                                        this.failView.setVisibility(8);
                                    }
                                }
                                this.sharedResources.setIconMuted(!this.storyViewer.soundEnabled(), false);
                                if (this.isActive && this.currentStory.storyItem != null) {
                                    FileLog.d("StoryViewer displayed story dialogId=" + this.dialogId + " storyId=" + this.currentStory.storyItem.id + str7 + this.currentStory.getMediaDebugString());
                                }
                                if (this.isSelf) {
                                    SelfStoryViewsPage.preload(this.currentAccount, this.dialogId, this.currentStory.storyItem);
                                }
                                SimpleTextView simpleTextView = this.headerView.titleView;
                                storiesList2 = this.storyViewer.storiesList;
                                if (storiesList2 != null || storiesList2.getCount() == this.linesCount) {
                                    i6 = 0;
                                    i7 = 0;
                                } else {
                                    i7 = AndroidUtilities.dp(56.0f);
                                    i6 = 0;
                                }
                                simpleTextView.setPadding(i6, i6, i7, i6);
                                MessagesController.getInstance(this.currentAccount).getTranslateController().detectStoryLanguage(this.currentStory.storyItem);
                                if (!z && !this.isSelf && this.reactionsTooltipRunnable == null && !SharedConfig.storyReactionsLongPressHint && SharedConfig.storiesIntroShown) {
                                    Runnable runnable = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda19
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            PeerStoriesView.this.lambda$updatePosition$41();
                                        }
                                    };
                                    this.reactionsTooltipRunnable = runnable;
                                    AndroidUtilities.runOnUIThread(runnable, 500L);
                                }
                                hintView23 = this.soundTooltip;
                                if ((hintView23 != null || !hintView23.shown()) && this.currentStory.hasSound() && !this.storyViewer.soundEnabled()) {
                                    if (MessagesController.getGlobalMainSettings().getInt("taptostorysoundhint", 0) < 2) {
                                        AndroidUtilities.cancelRunOnUIThread(this.showTapToSoundHint);
                                        AndroidUtilities.runOnUIThread(this.showTapToSoundHint, 250L);
                                    }
                                }
                                imageView = this.optionsIconView;
                                if (imageView == null) {
                                    imageView.setVisibility((!isBotsPreview() || isEditBotsPreview() || ((storyItemHolder3 = this.currentStory) != null && storyItemHolder3.isVideo)) ? 0 : 8);
                                    return;
                                }
                                return;
                            }
                            createFailView();
                            this.failView.set(this.currentStory.uploadingStory.entry.error);
                            this.failView.setVisibility(0);
                            ViewPropertyAnimator viewPropertyAnimator2 = this.failViewAnimator;
                            if (viewPropertyAnimator2 != null) {
                                viewPropertyAnimator2.cancel();
                                this.failViewAnimator = null;
                            }
                            StoryFailView storyFailView = this.failView;
                            if (z18) {
                                withEndAction = storyFailView.animate().alpha(1.0f).setDuration(180L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                                this.failViewAnimator = withEndAction;
                                withEndAction.start();
                                this.sharedResources.setIconMuted(!this.storyViewer.soundEnabled(), false);
                                if (this.isActive) {
                                    FileLog.d("StoryViewer displayed story dialogId=" + this.dialogId + " storyId=" + this.currentStory.storyItem.id + str7 + this.currentStory.getMediaDebugString());
                                }
                                if (this.isSelf) {
                                }
                                SimpleTextView simpleTextView2 = this.headerView.titleView;
                                storiesList2 = this.storyViewer.storiesList;
                                if (storiesList2 != null) {
                                }
                                i6 = 0;
                                i7 = 0;
                                simpleTextView2.setPadding(i6, i6, i7, i6);
                                MessagesController.getInstance(this.currentAccount).getTranslateController().detectStoryLanguage(this.currentStory.storyItem);
                                if (!z) {
                                    Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda19
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            PeerStoriesView.this.lambda$updatePosition$41();
                                        }
                                    };
                                    this.reactionsTooltipRunnable = runnable2;
                                    AndroidUtilities.runOnUIThread(runnable2, 500L);
                                }
                                hintView23 = this.soundTooltip;
                                if (hintView23 != null) {
                                }
                                if (MessagesController.getGlobalMainSettings().getInt("taptostorysoundhint", 0) < 2) {
                                }
                                imageView = this.optionsIconView;
                                if (imageView == null) {
                                }
                            } else {
                                storyFailView.setAlpha(1.0f);
                                this.sharedResources.setIconMuted(!this.storyViewer.soundEnabled(), false);
                                if (this.isActive) {
                                }
                                if (this.isSelf) {
                                }
                                SimpleTextView simpleTextView22 = this.headerView.titleView;
                                storiesList2 = this.storyViewer.storiesList;
                                if (storiesList2 != null) {
                                }
                                i6 = 0;
                                i7 = 0;
                                simpleTextView22.setPadding(i6, i6, i7, i6);
                                MessagesController.getInstance(this.currentAccount).getTranslateController().detectStoryLanguage(this.currentStory.storyItem);
                                if (!z) {
                                }
                                hintView23 = this.soundTooltip;
                                if (hintView23 != null) {
                                }
                                if (MessagesController.getGlobalMainSettings().getInt("taptostorysoundhint", 0) < 2) {
                                }
                                imageView = this.optionsIconView;
                                if (imageView == null) {
                                }
                            }
                        }
                        linearLayout.setVisibility(i9);
                        storyItemHolder2 = this.currentStory;
                        if (storyItemHolder2.caption == null) {
                        }
                        StoryCaptionView.StoryCaptionTextView storyCaptionTextView2 = this.storyCaptionView.captionTextview;
                        StoryItemHolder storyItemHolder82 = this.currentStory;
                        CharSequence charSequence52 = storyItemHolder82.caption;
                        StoryCaptionView.Reply reply22 = storyItemHolder82.getReply();
                        if (this.storyViewer.isTranslating) {
                        }
                        z9 = false;
                        storyCaptionTextView2.setText(charSequence52, reply22, z9, tL_stories$StoryItem12 == this.currentStory.storyItem);
                        this.storyCaptionView.setVisibility(0);
                        this.storyContainer.invalidate();
                        if (this.delegate != null) {
                            this.delegate.onPeerSelected(this.dialogId, this.selectedPosition);
                        }
                        if (this.isChannel) {
                        }
                        this.likeButtonContainer.requestLayout();
                        this.storyViewer.savedPositions.append(this.dialogId, i);
                        if (this.isActive) {
                        }
                        this.listPosition = 0;
                        if (this.storyViewer.storiesList != null) {
                            int i122 = tL_stories$StoryItem10.id;
                            i8 = 0;
                            while (true) {
                                if (i8 < this.storyViewer.storiesList.messageObjects.size()) {
                                }
                                i8++;
                            }
                        }
                        int i132 = this.selectedPosition;
                        this.linesPosition = i132;
                        int i142 = this.count;
                        this.linesCount = i142;
                        if (this.storyViewer.reversed) {
                        }
                        if (this.currentStory.isVideo()) {
                        }
                        StoryItemHolder storyItemHolder102 = this.currentStory;
                        uploadingStory5 = storyItemHolder102.uploadingStory;
                        if (uploadingStory5 == null) {
                        }
                        this.editedPrivacy = false;
                        this.privacyButton.setTranslationX(this.muteIconContainer.getVisibility() != 0 ? -AndroidUtilities.dp(44.0f) : 0.0f);
                        if (z8) {
                        }
                        uploadingStory6 = this.currentStory.uploadingStory;
                        if (uploadingStory6 != null) {
                        }
                        if (this.failView != null) {
                        }
                        this.sharedResources.setIconMuted(!this.storyViewer.soundEnabled(), false);
                        if (this.isActive) {
                        }
                        if (this.isSelf) {
                        }
                        SimpleTextView simpleTextView222 = this.headerView.titleView;
                        storiesList2 = this.storyViewer.storiesList;
                        if (storiesList2 != null) {
                        }
                        i6 = 0;
                        i7 = 0;
                        simpleTextView222.setPadding(i6, i6, i7, i6);
                        MessagesController.getInstance(this.currentAccount).getTranslateController().detectStoryLanguage(this.currentStory.storyItem);
                        if (!z) {
                        }
                        hintView23 = this.soundTooltip;
                        if (hintView23 != null) {
                        }
                        if (MessagesController.getGlobalMainSettings().getInt("taptostorysoundhint", 0) < 2) {
                        }
                        imageView = this.optionsIconView;
                        if (imageView == null) {
                        }
                    }
                }
                this.allowShareLink = false;
                this.allowRepost = false;
                this.allowShare = false;
                z5 = z4;
                tL_stories$StoryItem4 = this.currentStory.storyItem;
                if (tL_stories$StoryItem4 != null) {
                    this.storyViewer.dayStoryId = tL_stories$StoryItem4.id;
                }
                this.storyViewer.storiesViewPager.checkAllowScreenshots();
                this.imageChanged = true;
                if (!this.isSelf) {
                }
                updateUserViews(false);
                StoryItemHolder storyItemHolder52 = this.currentStory;
                if (getStoryId(storyItemHolder52.storyItem, storyItemHolder52.uploadingStory) == getStoryId(tL_stories$StoryItem12, uploadingStory8)) {
                }
                if (z18) {
                }
                if (uploadingStory8 == null) {
                }
                chatActivityEnterView = this.chatActivityEnterView;
                if (chatActivityEnterView != null) {
                }
                this.emojiAnimationsOverlay.clear();
                this.currentImageTime = 0L;
                this.switchEventSent = false;
                uploadingStory3 = this.currentStory.uploadingStory;
                if (uploadingStory3 != null) {
                }
                Bulletin.hideVisible(this.storyContainer);
                this.storyCaptionView.reset();
                cancelWaiting();
                z8 = true;
                if (z8) {
                }
                this.headerView.setOnSubtitleClick(null);
                uploadingStory4 = this.currentStory.uploadingStory;
                if (uploadingStory4 != null) {
                }
                if (charSequence != null) {
                }
                hintView2 = this.privacyHint;
                if (hintView2 != null) {
                }
                hintView22 = this.soundTooltip;
                if (hintView22 != null) {
                }
                StoryItemHolder storyItemHolder72 = this.currentStory;
                tL_stories$StoryItem8 = storyItemHolder72.storyItem;
                if (tL_stories$StoryItem12 == tL_stories$StoryItem8) {
                }
                this.currentStory.updateCaption();
                storyItemHolder = this.currentStory;
                if (storyItemHolder.captionTranslated) {
                }
                r2 = 0;
                delegate.setTranslating(false);
                if (this.unsupported) {
                }
                linearLayout.setVisibility(i9);
                storyItemHolder2 = this.currentStory;
                if (storyItemHolder2.caption == null) {
                }
                StoryCaptionView.StoryCaptionTextView storyCaptionTextView22 = this.storyCaptionView.captionTextview;
                StoryItemHolder storyItemHolder822 = this.currentStory;
                CharSequence charSequence522 = storyItemHolder822.caption;
                StoryCaptionView.Reply reply222 = storyItemHolder822.getReply();
                if (this.storyViewer.isTranslating) {
                }
                z9 = false;
                storyCaptionTextView22.setText(charSequence522, reply222, z9, tL_stories$StoryItem12 == this.currentStory.storyItem);
                this.storyCaptionView.setVisibility(0);
                this.storyContainer.invalidate();
                if (this.delegate != null) {
                }
                if (this.isChannel) {
                }
                this.likeButtonContainer.requestLayout();
                this.storyViewer.savedPositions.append(this.dialogId, i);
                if (this.isActive) {
                }
                this.listPosition = 0;
                if (this.storyViewer.storiesList != null) {
                }
                int i1322 = this.selectedPosition;
                this.linesPosition = i1322;
                int i1422 = this.count;
                this.linesCount = i1422;
                if (this.storyViewer.reversed) {
                }
                if (this.currentStory.isVideo()) {
                }
                StoryItemHolder storyItemHolder1022 = this.currentStory;
                uploadingStory5 = storyItemHolder1022.uploadingStory;
                if (uploadingStory5 == null) {
                }
                this.editedPrivacy = false;
                this.privacyButton.setTranslationX(this.muteIconContainer.getVisibility() != 0 ? -AndroidUtilities.dp(44.0f) : 0.0f);
                if (z8) {
                }
                uploadingStory6 = this.currentStory.uploadingStory;
                if (uploadingStory6 != null) {
                }
                if (this.failView != null) {
                }
                this.sharedResources.setIconMuted(!this.storyViewer.soundEnabled(), false);
                if (this.isActive) {
                }
                if (this.isSelf) {
                }
                SimpleTextView simpleTextView2222 = this.headerView.titleView;
                storiesList2 = this.storyViewer.storiesList;
                if (storiesList2 != null) {
                }
                i6 = 0;
                i7 = 0;
                simpleTextView2222.setPadding(i6, i6, i7, i6);
                MessagesController.getInstance(this.currentAccount).getTranslateController().detectStoryLanguage(this.currentStory.storyItem);
                if (!z) {
                }
                hintView23 = this.soundTooltip;
                if (hintView23 != null) {
                }
                if (MessagesController.getGlobalMainSettings().getInt("taptostorysoundhint", 0) < 2) {
                }
                imageView = this.optionsIconView;
                if (imageView == null) {
                }
            } else {
                uploadingStory2 = (StoriesController.UploadingStory) this.uploadingStories.get(size);
                uploadingStory = uploadingStory2;
                tL_stories$StoryItem2 = tL_stories$StoryItem;
                this.currentStory.editingSourceItem = null;
                if (uploadingStory == null) {
                }
                this.allowShareLink = false;
                this.allowRepost = false;
                this.allowShare = false;
                z5 = z4;
                tL_stories$StoryItem4 = this.currentStory.storyItem;
                if (tL_stories$StoryItem4 != null) {
                }
                this.storyViewer.storiesViewPager.checkAllowScreenshots();
                this.imageChanged = true;
                if (!this.isSelf) {
                }
                updateUserViews(false);
                StoryItemHolder storyItemHolder522 = this.currentStory;
                if (getStoryId(storyItemHolder522.storyItem, storyItemHolder522.uploadingStory) == getStoryId(tL_stories$StoryItem12, uploadingStory8)) {
                }
                if (z18) {
                }
                if (uploadingStory8 == null) {
                }
                chatActivityEnterView = this.chatActivityEnterView;
                if (chatActivityEnterView != null) {
                }
                this.emojiAnimationsOverlay.clear();
                this.currentImageTime = 0L;
                this.switchEventSent = false;
                uploadingStory3 = this.currentStory.uploadingStory;
                if (uploadingStory3 != null) {
                }
                Bulletin.hideVisible(this.storyContainer);
                this.storyCaptionView.reset();
                cancelWaiting();
                z8 = true;
                if (z8) {
                }
                this.headerView.setOnSubtitleClick(null);
                uploadingStory4 = this.currentStory.uploadingStory;
                if (uploadingStory4 != null) {
                }
                if (charSequence != null) {
                }
                hintView2 = this.privacyHint;
                if (hintView2 != null) {
                }
                hintView22 = this.soundTooltip;
                if (hintView22 != null) {
                }
                StoryItemHolder storyItemHolder722 = this.currentStory;
                tL_stories$StoryItem8 = storyItemHolder722.storyItem;
                if (tL_stories$StoryItem12 == tL_stories$StoryItem8) {
                }
                this.currentStory.updateCaption();
                storyItemHolder = this.currentStory;
                if (storyItemHolder.captionTranslated) {
                }
                r2 = 0;
                delegate.setTranslating(false);
                if (this.unsupported) {
                }
                linearLayout.setVisibility(i9);
                storyItemHolder2 = this.currentStory;
                if (storyItemHolder2.caption == null) {
                }
                StoryCaptionView.StoryCaptionTextView storyCaptionTextView222 = this.storyCaptionView.captionTextview;
                StoryItemHolder storyItemHolder8222 = this.currentStory;
                CharSequence charSequence5222 = storyItemHolder8222.caption;
                StoryCaptionView.Reply reply2222 = storyItemHolder8222.getReply();
                if (this.storyViewer.isTranslating) {
                }
                z9 = false;
                storyCaptionTextView222.setText(charSequence5222, reply2222, z9, tL_stories$StoryItem12 == this.currentStory.storyItem);
                this.storyCaptionView.setVisibility(0);
                this.storyContainer.invalidate();
                if (this.delegate != null) {
                }
                if (this.isChannel) {
                }
                this.likeButtonContainer.requestLayout();
                this.storyViewer.savedPositions.append(this.dialogId, i);
                if (this.isActive) {
                }
                this.listPosition = 0;
                if (this.storyViewer.storiesList != null) {
                }
                int i13222 = this.selectedPosition;
                this.linesPosition = i13222;
                int i14222 = this.count;
                this.linesCount = i14222;
                if (this.storyViewer.reversed) {
                }
                if (this.currentStory.isVideo()) {
                }
                StoryItemHolder storyItemHolder10222 = this.currentStory;
                uploadingStory5 = storyItemHolder10222.uploadingStory;
                if (uploadingStory5 == null) {
                }
                this.editedPrivacy = false;
                this.privacyButton.setTranslationX(this.muteIconContainer.getVisibility() != 0 ? -AndroidUtilities.dp(44.0f) : 0.0f);
                if (z8) {
                }
                uploadingStory6 = this.currentStory.uploadingStory;
                if (uploadingStory6 != null) {
                }
                if (this.failView != null) {
                }
                this.sharedResources.setIconMuted(!this.storyViewer.soundEnabled(), false);
                if (this.isActive) {
                }
                if (this.isSelf) {
                }
                SimpleTextView simpleTextView22222 = this.headerView.titleView;
                storiesList2 = this.storyViewer.storiesList;
                if (storiesList2 != null) {
                }
                i6 = 0;
                i7 = 0;
                simpleTextView22222.setPadding(i6, i6, i7, i6);
                MessagesController.getInstance(this.currentAccount).getTranslateController().detectStoryLanguage(this.currentStory.storyItem);
                if (!z) {
                }
                hintView23 = this.soundTooltip;
                if (hintView23 != null) {
                }
                if (MessagesController.getGlobalMainSettings().getInt("taptostorysoundhint", 0) < 2) {
                }
                imageView = this.optionsIconView;
                if (imageView == null) {
                }
            }
        } else {
            uploadingStory2 = (i11 < 0 || i11 >= this.uploadingStories.size()) ? null : (StoriesController.UploadingStory) this.uploadingStories.get(i11);
            int size2 = i11 - this.uploadingStories.size();
            if (size2 < 0 || size2 >= this.storyItems.size()) {
                uploadingStory = uploadingStory2;
                tL_stories$StoryItem2 = null;
                this.currentStory.editingSourceItem = null;
                if (uploadingStory == null) {
                }
                this.allowShareLink = false;
                this.allowRepost = false;
                this.allowShare = false;
                z5 = z4;
                tL_stories$StoryItem4 = this.currentStory.storyItem;
                if (tL_stories$StoryItem4 != null) {
                }
                this.storyViewer.storiesViewPager.checkAllowScreenshots();
                this.imageChanged = true;
                if (!this.isSelf) {
                }
                updateUserViews(false);
                StoryItemHolder storyItemHolder5222 = this.currentStory;
                if (getStoryId(storyItemHolder5222.storyItem, storyItemHolder5222.uploadingStory) == getStoryId(tL_stories$StoryItem12, uploadingStory8)) {
                }
                if (z18) {
                }
                if (uploadingStory8 == null) {
                }
                chatActivityEnterView = this.chatActivityEnterView;
                if (chatActivityEnterView != null) {
                }
                this.emojiAnimationsOverlay.clear();
                this.currentImageTime = 0L;
                this.switchEventSent = false;
                uploadingStory3 = this.currentStory.uploadingStory;
                if (uploadingStory3 != null) {
                }
                Bulletin.hideVisible(this.storyContainer);
                this.storyCaptionView.reset();
                cancelWaiting();
                z8 = true;
                if (z8) {
                }
                this.headerView.setOnSubtitleClick(null);
                uploadingStory4 = this.currentStory.uploadingStory;
                if (uploadingStory4 != null) {
                }
                if (charSequence != null) {
                }
                hintView2 = this.privacyHint;
                if (hintView2 != null) {
                }
                hintView22 = this.soundTooltip;
                if (hintView22 != null) {
                }
                StoryItemHolder storyItemHolder7222 = this.currentStory;
                tL_stories$StoryItem8 = storyItemHolder7222.storyItem;
                if (tL_stories$StoryItem12 == tL_stories$StoryItem8) {
                }
                this.currentStory.updateCaption();
                storyItemHolder = this.currentStory;
                if (storyItemHolder.captionTranslated) {
                }
                r2 = 0;
                delegate.setTranslating(false);
                if (this.unsupported) {
                }
                linearLayout.setVisibility(i9);
                storyItemHolder2 = this.currentStory;
                if (storyItemHolder2.caption == null) {
                }
                StoryCaptionView.StoryCaptionTextView storyCaptionTextView2222 = this.storyCaptionView.captionTextview;
                StoryItemHolder storyItemHolder82222 = this.currentStory;
                CharSequence charSequence52222 = storyItemHolder82222.caption;
                StoryCaptionView.Reply reply22222 = storyItemHolder82222.getReply();
                if (this.storyViewer.isTranslating) {
                }
                z9 = false;
                storyCaptionTextView2222.setText(charSequence52222, reply22222, z9, tL_stories$StoryItem12 == this.currentStory.storyItem);
                this.storyCaptionView.setVisibility(0);
                this.storyContainer.invalidate();
                if (this.delegate != null) {
                }
                if (this.isChannel) {
                }
                this.likeButtonContainer.requestLayout();
                this.storyViewer.savedPositions.append(this.dialogId, i);
                if (this.isActive) {
                }
                this.listPosition = 0;
                if (this.storyViewer.storiesList != null) {
                }
                int i132222 = this.selectedPosition;
                this.linesPosition = i132222;
                int i142222 = this.count;
                this.linesCount = i142222;
                if (this.storyViewer.reversed) {
                }
                if (this.currentStory.isVideo()) {
                }
                StoryItemHolder storyItemHolder102222 = this.currentStory;
                uploadingStory5 = storyItemHolder102222.uploadingStory;
                if (uploadingStory5 == null) {
                }
                this.editedPrivacy = false;
                this.privacyButton.setTranslationX(this.muteIconContainer.getVisibility() != 0 ? -AndroidUtilities.dp(44.0f) : 0.0f);
                if (z8) {
                }
                uploadingStory6 = this.currentStory.uploadingStory;
                if (uploadingStory6 != null) {
                }
                if (this.failView != null) {
                }
                this.sharedResources.setIconMuted(!this.storyViewer.soundEnabled(), false);
                if (this.isActive) {
                }
                if (this.isSelf) {
                }
                SimpleTextView simpleTextView222222 = this.headerView.titleView;
                storiesList2 = this.storyViewer.storiesList;
                if (storiesList2 != null) {
                }
                i6 = 0;
                i7 = 0;
                simpleTextView222222.setPadding(i6, i6, i7, i6);
                MessagesController.getInstance(this.currentAccount).getTranslateController().detectStoryLanguage(this.currentStory.storyItem);
                if (!z) {
                }
                hintView23 = this.soundTooltip;
                if (hintView23 != null) {
                }
                if (MessagesController.getGlobalMainSettings().getInt("taptostorysoundhint", 0) < 2) {
                }
                imageView = this.optionsIconView;
                if (imageView == null) {
                }
            } else {
                tL_stories$StoryItem = (TL_stories$StoryItem) this.storyItems.get(size2);
                uploadingStory = uploadingStory2;
                tL_stories$StoryItem2 = tL_stories$StoryItem;
                this.currentStory.editingSourceItem = null;
                if (uploadingStory == null) {
                }
                this.allowShareLink = false;
                this.allowRepost = false;
                this.allowShare = false;
                z5 = z4;
                tL_stories$StoryItem4 = this.currentStory.storyItem;
                if (tL_stories$StoryItem4 != null) {
                }
                this.storyViewer.storiesViewPager.checkAllowScreenshots();
                this.imageChanged = true;
                if (!this.isSelf) {
                }
                updateUserViews(false);
                StoryItemHolder storyItemHolder52222 = this.currentStory;
                if (getStoryId(storyItemHolder52222.storyItem, storyItemHolder52222.uploadingStory) == getStoryId(tL_stories$StoryItem12, uploadingStory8)) {
                }
                if (z18) {
                }
                if (uploadingStory8 == null) {
                }
                chatActivityEnterView = this.chatActivityEnterView;
                if (chatActivityEnterView != null) {
                }
                this.emojiAnimationsOverlay.clear();
                this.currentImageTime = 0L;
                this.switchEventSent = false;
                uploadingStory3 = this.currentStory.uploadingStory;
                if (uploadingStory3 != null) {
                }
                Bulletin.hideVisible(this.storyContainer);
                this.storyCaptionView.reset();
                cancelWaiting();
                z8 = true;
                if (z8) {
                }
                this.headerView.setOnSubtitleClick(null);
                uploadingStory4 = this.currentStory.uploadingStory;
                if (uploadingStory4 != null) {
                }
                if (charSequence != null) {
                }
                hintView2 = this.privacyHint;
                if (hintView2 != null) {
                }
                hintView22 = this.soundTooltip;
                if (hintView22 != null) {
                }
                StoryItemHolder storyItemHolder72222 = this.currentStory;
                tL_stories$StoryItem8 = storyItemHolder72222.storyItem;
                if (tL_stories$StoryItem12 == tL_stories$StoryItem8) {
                }
                this.currentStory.updateCaption();
                storyItemHolder = this.currentStory;
                if (storyItemHolder.captionTranslated) {
                }
                r2 = 0;
                delegate.setTranslating(false);
                if (this.unsupported) {
                }
                linearLayout.setVisibility(i9);
                storyItemHolder2 = this.currentStory;
                if (storyItemHolder2.caption == null) {
                }
                StoryCaptionView.StoryCaptionTextView storyCaptionTextView22222 = this.storyCaptionView.captionTextview;
                StoryItemHolder storyItemHolder822222 = this.currentStory;
                CharSequence charSequence522222 = storyItemHolder822222.caption;
                StoryCaptionView.Reply reply222222 = storyItemHolder822222.getReply();
                if (this.storyViewer.isTranslating) {
                }
                z9 = false;
                storyCaptionTextView22222.setText(charSequence522222, reply222222, z9, tL_stories$StoryItem12 == this.currentStory.storyItem);
                this.storyCaptionView.setVisibility(0);
                this.storyContainer.invalidate();
                if (this.delegate != null) {
                }
                if (this.isChannel) {
                }
                this.likeButtonContainer.requestLayout();
                this.storyViewer.savedPositions.append(this.dialogId, i);
                if (this.isActive) {
                }
                this.listPosition = 0;
                if (this.storyViewer.storiesList != null) {
                }
                int i1322222 = this.selectedPosition;
                this.linesPosition = i1322222;
                int i1422222 = this.count;
                this.linesCount = i1422222;
                if (this.storyViewer.reversed) {
                }
                if (this.currentStory.isVideo()) {
                }
                StoryItemHolder storyItemHolder1022222 = this.currentStory;
                uploadingStory5 = storyItemHolder1022222.uploadingStory;
                if (uploadingStory5 == null) {
                }
                this.editedPrivacy = false;
                this.privacyButton.setTranslationX(this.muteIconContainer.getVisibility() != 0 ? -AndroidUtilities.dp(44.0f) : 0.0f);
                if (z8) {
                }
                uploadingStory6 = this.currentStory.uploadingStory;
                if (uploadingStory6 != null) {
                }
                if (this.failView != null) {
                }
                this.sharedResources.setIconMuted(!this.storyViewer.soundEnabled(), false);
                if (this.isActive) {
                }
                if (this.isSelf) {
                }
                SimpleTextView simpleTextView2222222 = this.headerView.titleView;
                storiesList2 = this.storyViewer.storiesList;
                if (storiesList2 != null) {
                }
                i6 = 0;
                i7 = 0;
                simpleTextView2222222.setPadding(i6, i6, i7, i6);
                MessagesController.getInstance(this.currentAccount).getTranslateController().detectStoryLanguage(this.currentStory.storyItem);
                if (!z) {
                }
                hintView23 = this.soundTooltip;
                if (hintView23 != null) {
                }
                if (MessagesController.getGlobalMainSettings().getInt("taptostorysoundhint", 0) < 2) {
                }
                imageView = this.optionsIconView;
                if (imageView == null) {
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x005e, code lost:
        if (r4 < 0) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0069, code lost:
        if (r4 >= getStoriesCount()) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x006b, code lost:
        r5.clearImage();
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0076, code lost:
        if (r10.uploadingStories.isEmpty() != false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x007e, code lost:
        if (r4 < r10.storyItems.size()) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0080, code lost:
        setStoryImage((org.telegram.ui.Stories.StoriesController.UploadingStory) r10.uploadingStories.get(r4 - r10.storyItems.size()), r5, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x009a, code lost:
        if (r10.storyItems.isEmpty() == false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x009e, code lost:
        if (r4 >= 0) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00a0, code lost:
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00a7, code lost:
        if (r4 < r10.storyItems.size()) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00a9, code lost:
        r4 = r10.storyItems.size() - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00b1, code lost:
        r4 = (org.telegram.tgnet.tl.TL_stories$StoryItem) r10.storyItems.get(r4);
        r4.dialogId = r10.dialogId;
        setStoryImage(r4, r5, r1);
        r5 = r4.media;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00c2, code lost:
        if (r5 == null) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00cc, code lost:
        if (org.telegram.messenger.MessageObject.isVideoDocument(r5.getDocument()) == false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00ce, code lost:
        r5 = r4.media.getDocument();
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00d6, code lost:
        if (r4.fileReference != 0) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00d8, code lost:
        r4.fileReference = org.telegram.messenger.FileLoader.getInstance(r10.currentAccount).getFileReference(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00e4, code lost:
        r6 = new java.lang.StringBuilder();
        r6.append("?account=");
        r6.append(r10.currentAccount);
        r6.append("&id=");
        r6.append(r5.id);
        r6.append("&hash=");
        r6.append(r5.access_hash);
        r6.append("&dc=");
        r6.append(r5.dc_id);
        r6.append("&size=");
        r6.append(r5.size);
        r6.append("&mime=");
        r6.append(java.net.URLEncoder.encode(r5.mime_type, "UTF-8"));
        r6.append("&rid=");
        r6.append(r4.fileReference);
        r6.append("&name=");
        r6.append(java.net.URLEncoder.encode(org.telegram.messenger.FileLoader.getDocumentFileName(r5), "UTF-8"));
        r6.append("&reference=");
        r7 = r5.file_reference;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x014a, code lost:
        if (r7 == null) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x014d, code lost:
        r7 = new byte[0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x014f, code lost:
        r6.append(org.telegram.messenger.Utilities.bytesToHex(r7));
        r6.append("&sid=");
        r6.append(r4.id);
        r6.append("&did=");
        r6.append(r4.dialogId);
        r6 = r6.toString();
        r10.uriesToPrepare.add(android.net.Uri.parse("tg://" + org.telegram.messenger.FileLoader.getAttachFileName(r5) + r6));
        r10.documentsToPrepare.add(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0195, code lost:
        r5 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0196, code lost:
        r5.printStackTrace();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updatePreloadImages() {
        int i;
        ImageReceiver imageReceiver;
        TL_stories$StoryItem tL_stories$StoryItem;
        int max = (int) (Math.max(AndroidUtilities.getRealScreenSize().x, AndroidUtilities.getRealScreenSize().y) / AndroidUtilities.density);
        String str = max + "_" + max;
        this.uriesToPrepare.clear();
        this.documentsToPrepare.clear();
        for (int i2 = 0; i2 < this.preloadReactionHolders.size(); i2++) {
            ((ReactionImageHolder) this.preloadReactionHolders.get(i2)).onAttachedToWindow(false);
        }
        this.preloadReactionHolders.clear();
        for (int i3 = 0; i3 < 2; i3++) {
            int i4 = this.selectedPosition;
            if (i3 == 0) {
                i = i4 - 1;
                imageReceiver = this.leftPreloadImageReceiver;
            } else {
                i = i4 + 1;
                imageReceiver = this.rightPreloadImageReceiver;
            }
        }
        this.delegate.preparePlayer(this.documentsToPrepare, this.uriesToPrepare);
        return;
        if (tL_stories$StoryItem.media_areas != null) {
            for (int i5 = 0; i5 < tL_stories$StoryItem.media_areas.size(); i5++) {
                if (tL_stories$StoryItem.media_areas.get(i5) instanceof TL_stories$TL_mediaAreaSuggestedReaction) {
                    ReactionImageHolder reactionImageHolder = new ReactionImageHolder(this);
                    reactionImageHolder.setVisibleReaction(ReactionsLayoutInBubble.VisibleReaction.fromTL(((TL_stories$TL_mediaAreaSuggestedReaction) tL_stories$StoryItem.media_areas.get(i5)).reaction));
                    reactionImageHolder.onAttachedToWindow(this.attachedToWindow);
                    this.preloadReactionHolders.add(reactionImageHolder);
                }
            }
        }
    }

    private void updatePremiumBlockedText() {
        TextView textView = this.premiumBlockedText1;
        if (textView != null) {
            textView.setText(LocaleController.getString(this.isGroup ? R.string.StoryGroupRepliesLocked : R.string.StoryRepliesLocked));
        }
        TextView textView2 = this.premiumBlockedText2;
        if (textView2 != null) {
            textView2.setText(LocaleController.getString(R.string.StoryRepliesLockedButton));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateSelectedPosition() {
        TL_stories$PeerStories tL_stories$PeerStories;
        int i;
        int i2;
        ArrayList arrayList;
        if (this.day == null) {
            int i3 = this.storyViewer.savedPositions.get(this.dialogId, -1);
            this.selectedPosition = i3;
            if (i3 == -1 && !this.storyViewer.isSingleStory && (tL_stories$PeerStories = this.userStories) != null && tL_stories$PeerStories.max_read_id > 0) {
                i = 0;
                while (i < this.storyItems.size()) {
                    if (((TL_stories$StoryItem) this.storyItems.get(i)).id <= this.userStories.max_read_id) {
                        i++;
                    }
                }
            }
            if (this.selectedPosition != -1) {
                this.selectedPosition = 0;
                return;
            }
            return;
        }
        ArrayList arrayList2 = this.uploadingStories;
        if (arrayList2 == null || arrayList2.isEmpty()) {
            i2 = 0;
        } else {
            i2 = this.uploadingStories.size();
            for (int i4 = 0; i4 < this.uploadingStories.size(); i4++) {
                if (FactCheckController$Key$$ExternalSyntheticBackport0.m(((StoriesController.UploadingStory) this.uploadingStories.get(i4)).random_id) == this.storyViewer.dayStoryId) {
                    this.selectedPosition = i4;
                    return;
                }
            }
        }
        int indexOf = this.day.indexOf(Integer.valueOf(this.storyViewer.dayStoryId));
        if (indexOf < 0 && !this.day.isEmpty()) {
            if (this.storyViewer.dayStoryId > ((Integer) this.day.get(0)).intValue()) {
                indexOf = 0;
            } else {
                if (this.storyViewer.dayStoryId < ((Integer) this.day.get(arrayList.size() - 1)).intValue()) {
                    indexOf = this.day.size() - 1;
                }
            }
        }
        i = i2 + indexOf;
        this.selectedPosition = i;
        if (this.selectedPosition != -1) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSpeedItem(boolean z) {
        ActionBarMenuSubItem actionBarMenuSubItem;
        String formatString;
        int i;
        ActionBarMenuSubItem actionBarMenuSubItem2 = this.speedItem;
        if (actionBarMenuSubItem2 == null || this.speedLayout == null || actionBarMenuSubItem2.getVisibility() != 0) {
            return;
        }
        if (z) {
            if (Math.abs(StoryViewer.currentSpeed - 0.2f) < 0.05f) {
                actionBarMenuSubItem = this.speedItem;
                i = R.string.VideoSpeedVerySlow;
            } else if (Math.abs(StoryViewer.currentSpeed - 0.5f) < 0.05f) {
                actionBarMenuSubItem = this.speedItem;
                i = R.string.VideoSpeedSlow;
            } else if (Math.abs(StoryViewer.currentSpeed - 1.0f) < 0.05f) {
                actionBarMenuSubItem = this.speedItem;
                i = R.string.VideoSpeedNormal;
            } else if (Math.abs(StoryViewer.currentSpeed - 1.5f) < 0.05f) {
                actionBarMenuSubItem = this.speedItem;
                i = R.string.VideoSpeedFast;
            } else if (Math.abs(StoryViewer.currentSpeed - 2.0f) < 0.05f) {
                actionBarMenuSubItem = this.speedItem;
                i = R.string.VideoSpeedVeryFast;
            } else {
                actionBarMenuSubItem = this.speedItem;
                int i2 = R.string.VideoSpeedCustom;
                formatString = LocaleController.formatString(i2, SpeedIconDrawable.formatNumber(StoryViewer.currentSpeed) + "x");
                actionBarMenuSubItem.setSubtext(formatString);
            }
            formatString = LocaleController.getString(i);
            actionBarMenuSubItem.setSubtext(formatString);
        }
        this.speedLayout.update(StoryViewer.currentSpeed, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0106  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateStoryItems() {
        TL_stories$PeerStories storiesFromFullPeer;
        TL_stories$PeerStories tL_stories$PeerStories;
        ArrayList uploadingStories;
        StoriesController.StoriesList storiesList;
        TL_stories$StoryItem tL_stories$StoryItem;
        this.storyItems.clear();
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer.isSingleStory) {
            this.storyItems.add(storyViewer.singleStory);
        } else {
            int i = 0;
            if (this.day != null && (storiesList = storyViewer.storiesList) != null) {
                if (storiesList instanceof StoriesController.BotPreviewsList) {
                    this.uploadingStories.clear();
                    ArrayList uploadingStories2 = MessagesController.getInstance(this.currentAccount).getStoriesController().getUploadingStories(this.dialogId);
                    String str = ((StoriesController.BotPreviewsList) this.storyViewer.storiesList).lang_code;
                    if (uploadingStories2 != null) {
                        while (i < uploadingStories2.size()) {
                            StoriesController.UploadingStory uploadingStory = (StoriesController.UploadingStory) uploadingStories2.get(i);
                            StoryEntry storyEntry = uploadingStory.entry;
                            if (storyEntry != null && !storyEntry.isEdit && TextUtils.equals(storyEntry.botLang, str)) {
                                this.uploadingStories.add(uploadingStory);
                            }
                            i++;
                        }
                    }
                }
                Iterator it = this.day.iterator();
                while (it.hasNext()) {
                    MessageObject findMessageObject = this.storyViewer.storiesList.findMessageObject(((Integer) it.next()).intValue());
                    if (findMessageObject != null && (tL_stories$StoryItem = findMessageObject.storyItem) != null) {
                        this.storyItems.add(tL_stories$StoryItem);
                    }
                }
            } else if (storyViewer.storiesList != null) {
                while (i < this.storyViewer.storiesList.messageObjects.size()) {
                    this.storyItems.add(((MessageObject) this.storyViewer.storiesList.messageObjects.get(i)).storyItem);
                    i++;
                }
            } else {
                TL_stories$PeerStories tL_stories$PeerStories2 = storyViewer.overrideUserStories;
                if (tL_stories$PeerStories2 == null || DialogObject.getPeerDialogId(tL_stories$PeerStories2.peer) != this.dialogId) {
                    TL_stories$PeerStories stories = this.storiesController.getStories(this.dialogId);
                    this.userStories = stories;
                    if (stories == null) {
                        storiesFromFullPeer = this.storiesController.getStoriesFromFullPeer(this.dialogId);
                    }
                    this.totalStoriesCount = 0;
                    tL_stories$PeerStories = this.userStories;
                    if (tL_stories$PeerStories != null) {
                        this.totalStoriesCount = tL_stories$PeerStories.stories.size();
                        this.storyItems.addAll(this.userStories.stories);
                    }
                    this.uploadingStories.clear();
                    uploadingStories = this.storiesController.getUploadingStories(this.dialogId);
                    if (uploadingStories != null) {
                        this.uploadingStories.addAll(uploadingStories);
                    }
                } else {
                    storiesFromFullPeer = this.storyViewer.overrideUserStories;
                }
                this.userStories = storiesFromFullPeer;
                this.totalStoriesCount = 0;
                tL_stories$PeerStories = this.userStories;
                if (tL_stories$PeerStories != null) {
                }
                this.uploadingStories.clear();
                uploadingStories = this.storiesController.getUploadingStories(this.dialogId);
                if (uploadingStories != null) {
                }
            }
        }
        this.count = getStoriesCount();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00ab, code lost:
        if (org.telegram.messenger.ChatObject.isPossibleRemoveChatRestrictionsByBoosts(r8) == false) goto L68;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0112  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x011f  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x013f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateUserViews(boolean z) {
        TextView textView;
        float dp;
        String str;
        FrameLayout frameLayout;
        int i;
        StoryItemHolder storyItemHolder = this.currentStory;
        TL_stories$StoryItem tL_stories$StoryItem = storyItemHolder.storyItem;
        if (tL_stories$StoryItem == null) {
            tL_stories$StoryItem = storyItemHolder.editingSourceItem;
        }
        boolean z2 = this.isChannel;
        if (z2 || this.isSelf) {
            String str2 = "";
            if (tL_stories$StoryItem == null) {
                this.selfStatusView.setText("");
                this.selfAvatarsContainer.setVisibility(8);
                this.selfAvatarsView.setVisibility(8);
            } else if (!z2) {
                TL_stories$StoryViews tL_stories$StoryViews = tL_stories$StoryItem.views;
                if (tL_stories$StoryViews == null || tL_stories$StoryViews.views_count <= 0) {
                    this.selfStatusView.setText(LocaleController.getString(this.storyViewer.storiesList == null ? R.string.NobodyViews : R.string.NobodyViewsArchived));
                    this.selfStatusView.setTranslationX(AndroidUtilities.dp(16.0f));
                    this.selfAvatarsView.setVisibility(8);
                    this.selfAvatarsContainer.setVisibility(8);
                } else {
                    int i2 = 0;
                    for (int i3 = 0; i3 < tL_stories$StoryItem.views.recent_viewers.size(); i3++) {
                        TLObject userOrChat = MessagesController.getInstance(this.currentAccount).getUserOrChat(((Long) tL_stories$StoryItem.views.recent_viewers.get(i3)).longValue());
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
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.formatPluralStringComma("Views", tL_stories$StoryItem.views.views_count));
                    if (tL_stories$StoryItem.views.reactions_count > 0) {
                        spannableStringBuilder.append((CharSequence) "  d ");
                        ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.mini_views_likes);
                        coloredImageSpan.setOverrideColor(-53704);
                        coloredImageSpan.setTopOffset(AndroidUtilities.dp(0.2f));
                        spannableStringBuilder.setSpan(coloredImageSpan, spannableStringBuilder.length() - 2, spannableStringBuilder.length() - 1, 0);
                        spannableStringBuilder.append((CharSequence) String.valueOf(tL_stories$StoryItem.views.reactions_count));
                    }
                    if (tL_stories$StoryItem.views.forwards_count > 0) {
                        spannableStringBuilder.append((CharSequence) "  d ");
                        ColoredImageSpan coloredImageSpan2 = new ColoredImageSpan(R.drawable.mini_repost_story);
                        coloredImageSpan2.setOverrideColor(-14161823);
                        coloredImageSpan2.setTopOffset(AndroidUtilities.dp(0.2f));
                        spannableStringBuilder.setSpan(coloredImageSpan2, spannableStringBuilder.length() - 2, spannableStringBuilder.length() - 1, 0);
                        spannableStringBuilder.append((CharSequence) String.valueOf(tL_stories$StoryItem.views.forwards_count));
                    }
                    this.selfStatusView.setText(spannableStringBuilder);
                    AvatarsImageView avatarsImageView = this.selfAvatarsView;
                    if (i2 == 0) {
                        avatarsImageView.setVisibility(8);
                        textView = this.selfStatusView;
                        dp = AndroidUtilities.dp(16.0f);
                    } else {
                        avatarsImageView.setVisibility(0);
                        textView = this.selfStatusView;
                        dp = AndroidUtilities.dp(13.0f) + AndroidUtilities.dp(24.0f) + (AndroidUtilities.dp(20.0f) * (i2 - 1)) + AndroidUtilities.dp(10.0f);
                    }
                    textView.setTranslationX(dp);
                    this.selfAvatarsContainer.setVisibility(0);
                }
                this.likeButtonContainer.getLayoutParams().width = AndroidUtilities.dp(40.0f);
                this.bottomActionsLinearLayout.requestLayout();
            } else {
                if (tL_stories$StoryItem.views == null) {
                    tL_stories$StoryItem.views = new TL_stories$TL_storyViews();
                }
                TL_stories$StoryViews tL_stories$StoryViews2 = tL_stories$StoryItem.views;
                if (tL_stories$StoryViews2.views_count <= 0) {
                    tL_stories$StoryViews2.views_count = 1;
                }
                AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.repostCounter;
                if (animatedTextDrawable == null || (i = tL_stories$StoryViews2.forwards_count) <= 0) {
                    this.repostCounterVisible = false;
                } else {
                    animatedTextDrawable.setText(Integer.toString(i), z && this.repostCounterVisible);
                    this.repostCounterVisible = true;
                }
                int i5 = tL_stories$StoryItem.views.reactions_count;
                if (i5 > 0) {
                    this.reactionsCounter.setText(Integer.toString(i5), z && this.reactionsCounterVisible);
                    this.reactionsCounterVisible = true;
                } else {
                    this.reactionsCounterVisible = false;
                }
                if (!z) {
                    this.reactionsCounterProgress.set(this.reactionsCounterVisible ? 1.0f : 0.0f, true);
                    AnimatedFloat animatedFloat = this.repostCounterProgress;
                    if (animatedFloat != null) {
                        animatedFloat.set(this.repostCounterVisible ? 1.0f : 0.0f, true);
                    }
                }
                TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
                if (this.isGroup) {
                    str = str2;
                    if (!ChatObject.canSendPlain(chat)) {
                        str = str2;
                    }
                    this.selfStatusView.setText(str);
                    this.likeButtonContainer.getLayoutParams().width = (int) (AndroidUtilities.dp(40.0f) + (!this.reactionsCounterVisible ? this.reactionsCounter.getAnimateToWidth() + AndroidUtilities.dp(4.0f) : 0.0f));
                    ((ViewGroup.MarginLayoutParams) this.selfView.getLayoutParams()).rightMargin = AndroidUtilities.dp(40.0f) + this.likeButtonContainer.getLayoutParams().width;
                    frameLayout = this.repostButtonContainer;
                    if (frameLayout != null) {
                        frameLayout.getLayoutParams().width = (int) (AndroidUtilities.dp(40.0f) + (this.repostCounterVisible ? this.repostCounter.getAnimateToWidth() + AndroidUtilities.dp(4.0f) : 0.0f));
                        ((ViewGroup.MarginLayoutParams) this.selfView.getLayoutParams()).rightMargin += this.repostButtonContainer.getLayoutParams().width;
                        this.repostButtonContainer.requestLayout();
                    }
                    this.selfView.requestLayout();
                    this.likeButtonContainer.requestLayout();
                    this.selfAvatarsView.setVisibility(8);
                    this.selfAvatarsContainer.setVisibility(8);
                    this.storyAreasView.onStoryItemUpdated(this.currentStory.storyItem, z);
                }
                str = str2;
                if (tL_stories$StoryItem.views.views_count > 0) {
                    this.selfStatusView.setText(LocaleController.getString(this.storyViewer.storiesList == null ? R.string.NobodyViews : R.string.NobodyViewsArchived));
                    this.selfStatusView.setTranslationX(AndroidUtilities.dp(16.0f));
                    SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
                    spannableStringBuilder2.append((CharSequence) "d  ");
                    spannableStringBuilder2.setSpan(new ColoredImageSpan(R.drawable.filled_views), spannableStringBuilder2.length() - 3, spannableStringBuilder2.length() - 2, 0);
                    spannableStringBuilder2.append((CharSequence) AndroidUtilities.formatWholeNumber(tL_stories$StoryItem.views.views_count, 0));
                    str = spannableStringBuilder2;
                }
                this.selfStatusView.setText(str);
                this.likeButtonContainer.getLayoutParams().width = (int) (AndroidUtilities.dp(40.0f) + (!this.reactionsCounterVisible ? this.reactionsCounter.getAnimateToWidth() + AndroidUtilities.dp(4.0f) : 0.0f));
                ((ViewGroup.MarginLayoutParams) this.selfView.getLayoutParams()).rightMargin = AndroidUtilities.dp(40.0f) + this.likeButtonContainer.getLayoutParams().width;
                frameLayout = this.repostButtonContainer;
                if (frameLayout != null) {
                }
                this.selfView.requestLayout();
                this.likeButtonContainer.requestLayout();
                this.selfAvatarsView.setVisibility(8);
                this.selfAvatarsContainer.setVisibility(8);
                this.storyAreasView.onStoryItemUpdated(this.currentStory.storyItem, z);
            }
        }
    }

    private void updateViewOffsets() {
        ReactionsContainerLayout reactionsContainerLayout;
        Paint paint;
        float progressToDismiss = this.delegate.getProgressToDismiss();
        this.progressToHideInterface.set(this.isLongPressed ? 1.0f : 0.0f);
        int i = this.lastOpenedKeyboardHeight;
        float clamp = (i == 0 || !this.animateKeyboardOpening) ? this.keyboardVisible ? 1.0f : 0.0f : MathUtils.clamp(this.animatingKeyboardHeight / i, 0.0f, 1.0f);
        float f = this.progressToRecording.get();
        float f2 = this.progressToTextA.get();
        float f3 = this.progressToStickerExpanded.get();
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
        boolean z = true;
        boolean z2 = chatActivityEnterView4 != null && chatActivityEnterView4.isPopupShowing();
        float hideInterfaceAlpha = getHideInterfaceAlpha();
        if (this.BIG_SCREEN) {
            this.inputBackgroundPaint.setColor(ColorUtils.blendARGB(ColorUtils.blendARGB(-16777216, -1, 0.13f), ColorUtils.setAlphaComponent(-16777216, NotificationCenter.groupCallVisibilityChanged), this.progressToKeyboard));
            this.inputBackgroundPaint.setAlpha((int) (paint.getAlpha() * (1.0f - this.progressToDismiss) * hideInterfaceAlpha * (1.0f - this.outT)));
        } else {
            this.inputBackgroundPaint.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (140.0f * hideInterfaceAlpha * (1.0f - this.outT))));
        }
        if (!this.forceUpdateOffsets && this.progressToReply == this.storyViewer.swipeToReplyProgress && this.progressToHideInterface.get() == this.prevToHideProgress && this.lastAnimatingKeyboardHeight == this.animatingKeyboardHeight && clamp == this.progressToKeyboard && progressToDismiss == this.progressToDismiss && f == this.progressToRecording.get() && !z2 && f3 == this.progressToStickerExpanded.get() && f2 == this.progressToTextA.get()) {
            return;
        }
        this.forceUpdateOffsets = false;
        this.lastAnimatingKeyboardHeight = this.animatingKeyboardHeight;
        if (this.progressToHideInterface.get() != this.prevToHideProgress) {
            this.storyContainer.invalidate();
        }
        if (progressToDismiss != 0.0f) {
            this.storyContainer.setLayerType(2, null);
        } else {
            this.storyContainer.setLayerType(0, null);
        }
        this.prevToHideProgress = this.progressToHideInterface.get();
        this.progressToDismiss = progressToDismiss;
        this.progressToKeyboard = clamp;
        this.progressToReply = this.storyViewer.swipeToReplyProgress;
        ReactionsContainerLayout reactionsContainerLayout2 = this.reactionsContainerLayout;
        if (reactionsContainerLayout2 != null) {
            reactionsContainerLayout2.setVisibility(clamp > 0.0f ? 0 : 8);
        }
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childAt = getChildAt(i2);
            if (childAt.getVisibility() == 0 && childAt != this.selfView && childAt.getTag(R.id.parent_tag) == null && childAt != this.storyCaptionView.textSelectionHelper.getOverlayView(getContext())) {
                ChatActivityEnterView chatActivityEnterView5 = this.chatActivityEnterView;
                if (chatActivityEnterView5 != null && childAt == chatActivityEnterView5.getEmojiView()) {
                    childAt.setTranslationY(this.chatActivityEnterView.getEmojiView().getMeasuredHeight() - this.animatingKeyboardHeight);
                } else if (childAt instanceof HintView) {
                    ((HintView) childAt).updatePosition();
                } else if (childAt != this.instantCameraView && childAt != this.storyContainer && childAt != this.shareButton && childAt != this.bottomActionsLinearLayout && childAt != this.repostButtonContainer && childAt != this.mediaBanTooltip && childAt != this.likeButtonContainer && ((reactionsContainerLayout = this.likesReactionLayout) == null || reactionsContainerLayout.getReactionsWindow() == null || childAt != this.likesReactionLayout.getReactionsWindow().windowView)) {
                    float dp = ((((-this.enterViewBottomOffset) * (1.0f - this.progressToKeyboard)) - this.animatingKeyboardHeight) - (AndroidUtilities.dp(8.0f) * (1.0f - this.progressToKeyboard))) - (AndroidUtilities.dp(20.0f) * this.storyViewer.swipeToReplyProgress);
                    float f4 = this.BIG_SCREEN ? (1.0f - this.progressToDismiss) * hideInterfaceAlpha : hideInterfaceAlpha * 1.0f;
                    if (childAt == this.replyDisabledTextView) {
                        dp = (-AndroidUtilities.dp(20.0f)) * this.storyViewer.swipeToReplyProgress;
                    }
                    if (childAt == this.mentionContainer) {
                        dp -= this.chatActivityEnterView.getMeasuredHeight() - this.chatActivityEnterView.getAnimatedTop();
                        f4 = this.progressToKeyboard;
                        childAt.invalidate();
                    }
                    if (childAt == this.reactionsContainerLayout) {
                        float f5 = this.progressToKeyboard * (1.0f - this.progressToRecording.get()) * (1.0f - f3) * (1.0f - this.progressToTextA.get());
                        float f6 = f4 * f5 * 1.0f;
                        if (childAt.getAlpha() != 0.0f && f6 == 0.0f) {
                            this.reactionsContainerLayout.reset();
                        }
                        childAt.setAlpha(f6);
                        float f7 = (f5 * 0.2f) + 0.8f;
                        childAt.setScaleX(f7);
                        childAt.setScaleY(f7);
                    } else {
                        childAt.setTranslationY(dp);
                        ChatActivityEnterView chatActivityEnterView6 = this.chatActivityEnterView;
                        if (chatActivityEnterView6 == null || childAt != chatActivityEnterView6.controlsView) {
                            childAt.setAlpha(f4);
                        }
                    }
                }
            } else if (childAt == this.selfView) {
                childAt.setAlpha(this.BIG_SCREEN ? (1.0f - this.progressToDismiss) * hideInterfaceAlpha * (1.0f - this.outT) : (1.0f - this.outT) * hideInterfaceAlpha);
            }
        }
        float f8 = (1.0f - progressToDismiss) * hideInterfaceAlpha;
        this.shareButton.setAlpha((1.0f - this.outT) * f8);
        this.likeButtonContainer.setAlpha((1.0f - this.outT) * f8);
        FrameLayout frameLayout = this.repostButtonContainer;
        if (frameLayout != null) {
            frameLayout.setAlpha(f8 * (1.0f - this.outT));
        }
        for (int i3 = 0; i3 < this.storyContainer.getChildCount(); i3++) {
            View childAt2 = this.storyContainer.getChildAt(i3);
            if (childAt2 != null) {
                if (childAt2 == this.headerView || childAt2 == this.optionsIconView || childAt2 == this.muteIconContainer || childAt2 == this.selfView || childAt2 == this.storyCaptionView || childAt2 == this.privacyButton) {
                    float f9 = childAt2 == this.muteIconContainer ? this.muteIconViewAlpha : 1.0f;
                    childAt2.setAlpha((childAt2 == this.storyCaptionView ? f9 * (hideCaptionWithInterface() ? hideInterfaceAlpha : 1.0f) : f9 * hideInterfaceAlpha) * (1.0f - this.outT));
                } else {
                    childAt2.setAlpha(hideInterfaceAlpha);
                }
            }
        }
        ChatActivityEnterView chatActivityEnterView7 = this.chatActivityEnterView;
        if (chatActivityEnterView7 != null) {
            float dp2 = AndroidUtilities.dp(10.0f);
            float f10 = this.progressToKeyboard;
            if (!this.allowShare && !this.isGroup) {
                z = false;
            }
            chatActivityEnterView7.setHorizontalPadding(dp2, f10, z);
            if (this.chatActivityEnterView.getEmojiView() != null) {
                this.chatActivityEnterView.getEmojiView().setAlpha(this.progressToKeyboard);
            }
        }
    }

    public void animateOut(final boolean z) {
        ValueAnimator valueAnimator = this.outAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.outT, z ? 1.0f : 0.0f);
        this.outAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda49
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                PeerStoriesView.this.lambda$animateOut$49(valueAnimator2);
            }
        });
        this.outAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.38
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
                Delegate delegate = PeerStoriesView.this.delegate;
                float progressToDismiss = delegate != null ? delegate.getProgressToDismiss() : 0.0f;
                float hideInterfaceAlpha = PeerStoriesView.this.getHideInterfaceAlpha();
                if (PeerStoriesView.this.likeButtonContainer != null) {
                    PeerStoriesView.this.likeButtonContainer.setAlpha((1.0f - progressToDismiss) * hideInterfaceAlpha * (1.0f - PeerStoriesView.this.outT));
                }
                if (PeerStoriesView.this.shareButton != null) {
                    PeerStoriesView.this.shareButton.setAlpha((1.0f - progressToDismiss) * hideInterfaceAlpha * (1.0f - PeerStoriesView.this.outT));
                }
                if (PeerStoriesView.this.repostButtonContainer != null) {
                    PeerStoriesView.this.repostButtonContainer.setAlpha(hideInterfaceAlpha * (1.0f - progressToDismiss) * (1.0f - PeerStoriesView.this.outT));
                }
                PeerStoriesView peerStoriesView2 = PeerStoriesView.this;
                ChatActivityEnterView chatActivityEnterView = peerStoriesView2.chatActivityEnterView;
                if (chatActivityEnterView != null) {
                    chatActivityEnterView.setAlpha(1.0f - peerStoriesView2.outT);
                    PeerStoriesView.this.invalidate();
                }
                PeerStoriesView.this.storyContainer.invalidate();
            }
        });
        this.outAnimator.setDuration(420L);
        this.outAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.outAnimator.start();
    }

    public void cancelTextSelection() {
        if (this.storyCaptionView.textSelectionHelper.isInSelectionMode()) {
            this.storyCaptionView.textSelectionHelper.clear();
        }
    }

    public void cancelTouch() {
        this.storyCaptionView.cancelTouch();
    }

    public void checkPinchToZoom(MotionEvent motionEvent) {
        this.pinchToZoomHelper.checkPinchToZoom(motionEvent, this.storyContainer, null, null, null, null);
    }

    public boolean checkReactionEvent(MotionEvent motionEvent) {
        View view = this.likesReactionLayout;
        if (view != null) {
            float x = getX();
            float y = getY() + ((View) getParent()).getY();
            if (this.likesReactionLayout.getReactionsWindow() == null || this.likesReactionLayout.getReactionsWindow().windowView == null) {
                Rect rect = AndroidUtilities.rectTmp2;
                view.getHitRect(rect);
                rect.offset((int) x, (int) y);
                if (motionEvent.getAction() == 0 && !rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                    showLikesReaction(false);
                    return true;
                }
                motionEvent.offsetLocation(-rect.left, -rect.top);
            } else {
                motionEvent.offsetLocation(-x, (-y) - this.likesReactionLayout.getReactionsWindow().windowView.getTranslationY());
                view = this.likesReactionLayout.getReactionsWindow().windowView;
            }
            view.dispatchTouchEvent(motionEvent);
            return true;
        }
        return false;
    }

    void checkReactionsLayout() {
        if (this.reactionsContainerLayout == null) {
            ReactionsContainerLayout reactionsContainerLayout = new ReactionsContainerLayout(1, LaunchActivity.getLastFragment(), getContext(), this.currentAccount, new WrappedResourceProvider(this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.34
                @Override // org.telegram.ui.WrappedResourceProvider
                public void appendColors() {
                    this.sparseIntArray.put(Theme.key_chat_emojiPanelBackground, ColorUtils.setAlphaComponent(-1, 30));
                }
            });
            this.reactionsContainerLayout = reactionsContainerLayout;
            reactionsContainerLayout.setHint(LocaleController.getString(this.isGroup ? R.string.StoryGroupReactionsHint : R.string.StoryReactionsHint));
            ReactionsContainerLayout reactionsContainerLayout2 = this.reactionsContainerLayout;
            reactionsContainerLayout2.skipEnterAnimation = true;
            addView(reactionsContainerLayout2, this.reactionsContainerIndex, LayoutHelper.createFrame(-2, 72.0f, 49, 0.0f, 0.0f, 0.0f, 64.0f));
            this.reactionsContainerLayout.setDelegate(new 35());
            this.reactionsContainerLayout.setMessage(null, null, true);
        }
        this.reactionsContainerLayout.setFragment(LaunchActivity.getLastFragment());
        this.reactionsContainerLayout.setHint(LocaleController.getString(this.isGroup ? R.string.StoryGroupReactionsHint : R.string.StoryReactionsHint));
    }

    void checkReactionsLayoutForLike() {
        ReactionsContainerLayout reactionsContainerLayout = this.likesReactionLayout;
        if (reactionsContainerLayout == null) {
            ReactionsContainerLayout reactionsContainerLayout2 = new ReactionsContainerLayout(2, LaunchActivity.getLastFragment(), getContext(), this.currentAccount, new WrappedResourceProvider(this.resourcesProvider) { // from class: org.telegram.ui.Stories.PeerStoriesView.36
                @Override // org.telegram.ui.WrappedResourceProvider
                public void appendColors() {
                    this.sparseIntArray.put(Theme.key_chat_emojiPanelBackground, ColorUtils.setAlphaComponent(-1, 30));
                }
            });
            this.likesReactionLayout = reactionsContainerLayout2;
            reactionsContainerLayout2.setPadding(0, 0, 0, AndroidUtilities.dp(22.0f));
            addView(this.likesReactionLayout, getChildCount() - 1, LayoutHelper.createFrame(-2, 74.0f, 53, 0.0f, 0.0f, 12.0f, 64.0f));
            this.likesReactionLayout.setVisibility(8);
            this.likesReactionLayout.setDelegate(new 37());
            this.likesReactionLayout.setMessage(null, null, true);
        } else {
            bringChildToFront(reactionsContainerLayout);
            this.likesReactionLayout.reset();
        }
        this.likesReactionLayout.setFragment(LaunchActivity.getLastFragment());
    }

    public boolean checkRecordLocked(final boolean z) {
        int i;
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView == null || !chatActivityEnterView.isRecordLocked()) {
            return false;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
        if (this.chatActivityEnterView.isInVideoMode()) {
            builder.setTitle(LocaleController.getString(R.string.DiscardVideoMessageTitle));
            i = R.string.DiscardVideoMessageDescription;
        } else {
            builder.setTitle(LocaleController.getString(R.string.DiscardVoiceMessageTitle));
            i = R.string.DiscardVoiceMessageDescription;
        }
        builder.setMessage(LocaleController.getString(i));
        builder.setPositiveButton(LocaleController.getString(R.string.DiscardVoiceMessageAction), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                PeerStoriesView.this.lambda$checkRecordLocked$48(z, dialogInterface, i2);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Continue), null);
        this.delegate.showDialog(builder.create());
        return true;
    }

    public boolean checkTextSelectionEvent(MotionEvent motionEvent) {
        if (this.storyCaptionView.textSelectionHelper.isInSelectionMode()) {
            float x = getX();
            float y = getY() + ((View) getParent()).getY();
            motionEvent.offsetLocation(-x, -y);
            if (this.storyCaptionView.textSelectionHelper.getOverlayView(getContext()).onTouchEvent(motionEvent)) {
                return true;
            }
            motionEvent.offsetLocation(x, y);
            return false;
        }
        return false;
    }

    public boolean closeKeyboardOrEmoji() {
        if (this.likesReactionShowing) {
            if (this.likesReactionLayout.getReactionsWindow() == null) {
                showLikesReaction(false);
                return true;
            }
            if (this.realKeyboardHeight > 0) {
                AndroidUtilities.hideKeyboard(this.likesReactionLayout.getReactionsWindow().windowView);
            } else {
                this.likesReactionLayout.getReactionsWindow().dismiss();
            }
            return true;
        }
        StoryMediaAreasView storyMediaAreasView = this.storyAreasView;
        if (storyMediaAreasView != null) {
            storyMediaAreasView.closeHint();
        }
        if (this.storyCaptionView.textSelectionHelper.isInSelectionMode()) {
            this.storyCaptionView.textSelectionHelper.clear(false);
            return true;
        }
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
                    ChatActivityEnterView chatActivityEnterView2 = this.chatActivityEnterView;
                    if (chatActivityEnterView2 != null) {
                        this.storyViewer.saveDraft(this.dialogId, this.currentStory.storyItem, chatActivityEnterView2.getEditText());
                    }
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

    public void createBlurredBitmap(Canvas canvas, Bitmap bitmap) {
        drawPlayingBitmap(bitmap.getWidth(), bitmap.getHeight(), canvas);
        if (AndroidUtilities.computePerceivedBrightness(AndroidUtilities.getDominantColor(bitmap)) < 0.15f) {
            canvas.drawColor(ColorUtils.setAlphaComponent(-1, 102));
        }
        Utilities.blurBitmap(bitmap, 3, 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
        Utilities.blurBitmap(bitmap, 3, 1, bitmap.getWidth(), bitmap.getHeight(), bitmap.getRowBytes());
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i != NotificationCenter.storiesUpdated) {
            boolean z = false;
            if (i != NotificationCenter.storiesListUpdated || this.storyViewer.storiesList != objArr[0]) {
                if (i != NotificationCenter.storyQualityUpdate) {
                    if (i == NotificationCenter.emojiLoaded) {
                        this.storyCaptionView.captionTextview.invalidate();
                        return;
                    }
                    if (i != NotificationCenter.stealthModeChanged) {
                        if (i == NotificationCenter.storiesLimitUpdate) {
                            StoriesController.StoryLimit checkStoryLimit = MessagesController.getInstance(this.currentAccount).getStoriesController().checkStoryLimit();
                            if (checkStoryLimit == null || this.delegate == null) {
                                return;
                            }
                            this.delegate.showDialog(new LimitReachedBottomSheet(fragmentForLimit(), findActivity(), checkStoryLimit.getLimitReachedType(), this.currentAccount, null));
                            return;
                        } else if (i == NotificationCenter.userIsPremiumBlockedUpadted) {
                            boolean z2 = this.isPremiumBlocked;
                            if (this.dialogId >= 0 && !UserConfig.getInstance(this.currentAccount).isPremium() && MessagesController.getInstance(this.currentAccount).isUserPremiumBlocked(this.dialogId)) {
                                z = true;
                            }
                            this.isPremiumBlocked = z;
                            if (z == z2) {
                                return;
                            }
                            updatePosition();
                        } else if (i != NotificationCenter.chatInfoDidLoad) {
                            return;
                        } else {
                            Object obj = objArr[0];
                            if (!(obj instanceof TLRPC$ChatFull) || this.dialogId != (-((TLRPC$ChatFull) obj).id)) {
                                return;
                            }
                        }
                    }
                    checkStealthMode(true);
                    return;
                }
                updatePosition();
                return;
            }
        }
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
                if (this.isSelf || this.isChannel) {
                    updateUserViews(true);
                }
            }
            TL_stories$PeerStories tL_stories$PeerStories = this.storyViewer.overrideUserStories;
            if (tL_stories$PeerStories != null) {
                this.storiesController.loadSkippedStories(tL_stories$PeerStories, true);
            } else {
                long j = this.dialogId;
                if (j != 0) {
                    this.storiesController.loadSkippedStories(j);
                }
            }
            ActionBarMenuSubItem actionBarMenuSubItem = this.editStoryItem;
            if (actionBarMenuSubItem != null) {
                actionBarMenuSubItem.animate().alpha((this.storiesController.hasUploadingStories(this.dialogId) && this.currentStory.isVideo && !SharedConfig.allowPreparingHevcPlayers()) ? 0.5f : 1.0f).start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0109, code lost:
        if (r9.reactionEffectImageReceiver.getLottieAnimation().isLastFrame() != false) goto L27;
     */
    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void dispatchDraw(Canvas canvas) {
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable;
        AnimatedTextView.AnimatedTextDrawable animatedTextDrawable2;
        updateViewOffsets();
        if (this.isChannel && (animatedTextDrawable2 = this.reactionsCounter) != null) {
            animatedTextDrawable2.setBounds(0, 0, getMeasuredWidth(), AndroidUtilities.dp(40.0f));
        }
        if (this.isChannel && (animatedTextDrawable = this.repostCounter) != null) {
            animatedTextDrawable.setBounds(0, 0, getMeasuredWidth(), AndroidUtilities.dp(40.0f));
        }
        super.dispatchDraw(canvas);
        if (this.movingReaction) {
            float x = this.bottomActionsLinearLayout.getX() + this.likeButtonContainer.getX() + (this.likeButtonContainer.getMeasuredWidth() / 2.0f);
            float y = this.bottomActionsLinearLayout.getY() + this.likeButtonContainer.getY() + (this.likeButtonContainer.getMeasuredHeight() / 2.0f);
            int dp = AndroidUtilities.dp(24.0f);
            float f = dp / 2.0f;
            float lerp = AndroidUtilities.lerp(this.movingReactionFromX, x - f, CubicBezierInterpolator.EASE_OUT.getInterpolation(this.movingReactionProgress));
            float lerp2 = AndroidUtilities.lerp(this.movingReactionFromY, y - f, this.movingReactionProgress);
            int lerp3 = AndroidUtilities.lerp(this.movingReactionFromSize, dp, this.movingReactionProgress);
            if (this.drawAnimatedEmojiAsMovingReaction) {
                AnimatedEmojiDrawable animatedEmojiDrawable = this.reactionMoveDrawable;
                if (animatedEmojiDrawable != null) {
                    float f2 = lerp3;
                    animatedEmojiDrawable.setBounds((int) lerp, (int) lerp2, (int) (lerp + f2), (int) (lerp2 + f2));
                    this.reactionMoveDrawable.draw(canvas);
                }
            } else {
                float f3 = lerp3;
                this.reactionMoveImageReceiver.setImageCoords(lerp, lerp2, f3, f3);
                this.reactionMoveImageReceiver.draw(canvas);
            }
        }
        if (this.drawReactionEffect) {
            float x2 = this.bottomActionsLinearLayout.getX() + this.likeButtonContainer.getX() + (this.likeButtonContainer.getMeasuredWidth() / 2.0f);
            float y2 = this.bottomActionsLinearLayout.getY() + this.likeButtonContainer.getY() + (this.likeButtonContainer.getMeasuredHeight() / 2.0f);
            int dp2 = AndroidUtilities.dp(120.0f);
            if (this.drawAnimatedEmojiAsMovingReaction) {
                AnimatedEmojiEffect animatedEmojiEffect = this.emojiReactionEffect;
                if (animatedEmojiEffect != null) {
                    float f4 = dp2 / 2.0f;
                    animatedEmojiEffect.setBounds((int) (x2 - f4), (int) (y2 - f4), (int) (x2 + f4), (int) (y2 + f4));
                    this.emojiReactionEffect.draw(canvas);
                    if (this.emojiReactionEffect.isDone()) {
                        this.emojiReactionEffect.removeView(this);
                        this.emojiReactionEffect = null;
                    }
                }
                this.drawReactionEffect = false;
            } else {
                float f5 = dp2;
                float f6 = f5 / 2.0f;
                this.reactionEffectImageReceiver.setImageCoords(x2 - f6, y2 - f6, f5, f5);
                this.reactionEffectImageReceiver.draw(canvas);
                if (this.reactionEffectImageReceiver.getLottieAnimation() != null) {
                }
            }
        }
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.drawRecordedPannel(canvas);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0175, code lost:
        if (r8.progressToKeyboard < 0.5f) goto L30;
     */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected boolean drawChild(Canvas canvas, View view, long j) {
        ReactionsContainerLayout reactionsContainerLayout;
        ReactionsContainerLayout reactionsContainerLayout2;
        if (view == this.mentionContainer) {
            canvas.save();
            canvas.clipRect(0.0f, this.mentionContainer.getY(), getMeasuredWidth(), this.mentionContainer.getY() + this.mentionContainer.getMeasuredHeight());
        } else {
            ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
            if (view == chatActivityEnterView) {
                this.sharedResources.rect1.set(0.0f, this.chatActivityEnterView.getY() + this.chatActivityEnterView.getAnimatedTop(), getMeasuredWidth() + AndroidUtilities.dp(20.0f), getMeasuredHeight());
                float dp = AndroidUtilities.dp(40.0f);
                if (this.allowShare) {
                    dp += AndroidUtilities.dp(46.0f);
                }
                if (this.allowRepost && this.isChannel) {
                    dp += AndroidUtilities.dp(46.0f);
                }
                FrameLayout frameLayout = this.likeButtonContainer;
                if (frameLayout != null && frameLayout.getVisibility() == 0) {
                    dp = (dp - AndroidUtilities.dp(40.0f)) + this.likeButtonContainer.getLayoutParams().width;
                }
                this.sharedResources.rect2.set(AndroidUtilities.dp(10.0f), (this.chatActivityEnterView.getBottom() - AndroidUtilities.dp(48.0f)) + this.chatActivityEnterView.getTranslationY() + AndroidUtilities.dp(2.0f), (getMeasuredWidth() - AndroidUtilities.dp(10.0f)) - dp, (this.chatActivityEnterView.getY() + this.chatActivityEnterView.getMeasuredHeight()) - AndroidUtilities.dp(2.0f));
                if (this.chatActivityEnterView.getMeasuredHeight() > AndroidUtilities.dp(50.0f)) {
                    this.chatActivityEnterView.getEditField().setTranslationY((1.0f - this.progressToKeyboard) * (this.chatActivityEnterView.getMeasuredHeight() - AndroidUtilities.dp(50.0f)));
                } else {
                    this.chatActivityEnterView.getEditField().setTranslationY(0.0f);
                }
                float dp2 = (AndroidUtilities.dp(50.0f) / 2.0f) * (1.0f - this.progressToKeyboard);
                this.bitmapShaderTools.setBounds(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                AndroidUtilities.lerp(this.sharedResources.rect2, this.sharedResources.rect1, this.progressToKeyboard, this.sharedResources.finalRect);
                float f = this.progressToKeyboard;
                if (f > 0.0f) {
                    this.bitmapShaderTools.paint.setAlpha((int) (f * 255.0f));
                    canvas.drawRoundRect(this.sharedResources.finalRect, dp2, dp2, this.bitmapShaderTools.paint);
                }
                canvas.drawRoundRect(this.sharedResources.finalRect, dp2, dp2, this.inputBackgroundPaint);
            } else {
                if (chatActivityEnterView == null || !chatActivityEnterView.isPopupView(view)) {
                    if (view != this.reactionsContainerLayout || this.chatActivityEnterView == null) {
                        if (view == this.likesReactionLayout) {
                            view.setTranslationY((((-(reactionsContainerLayout2.getMeasuredHeight() - this.likesReactionLayout.getPaddingBottom())) + this.likeButtonContainer.getY()) + this.bottomActionsLinearLayout.getY()) - AndroidUtilities.dp(18.0f));
                        }
                    } else {
                        view.setTranslationY(((-reactionsContainerLayout.getMeasuredHeight()) + (this.chatActivityEnterView.getY() + this.chatActivityEnterView.getAnimatedTop())) - AndroidUtilities.dp(18.0f));
                        if (this.progressToKeyboard > 0.0f) {
                            this.sharedResources.dimPaint.setAlpha((int) (this.progressToKeyboard * 125.0f));
                            canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), this.chatActivityEnterView.getY() + this.chatActivityEnterView.getAnimatedTop(), this.sharedResources.dimPaint);
                        }
                    }
                    return super.drawChild(canvas, view, j);
                }
                canvas.save();
                canvas.clipRect(this.sharedResources.finalRect);
            }
        }
        boolean drawChild = super.drawChild(canvas, view, j);
        canvas.restore();
        return drawChild;
    }

    public void drawPlayingBitmap(int i, int i2, Canvas canvas) {
        TextureView textureView;
        Bitmap bitmap;
        VideoPlayerSharedScope videoPlayerSharedScope = this.playerSharedScope;
        View view = videoPlayerSharedScope.renderView;
        if (view != null && videoPlayerSharedScope.surfaceView != null) {
            bitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            if (Build.VERSION.SDK_INT >= 24) {
                AndroidUtilities.getBitmapFromSurface(this.playerSharedScope.surfaceView, bitmap);
            }
            if (bitmap == null) {
                return;
            }
        } else if (view == null || (textureView = videoPlayerSharedScope.textureView) == null) {
            canvas.save();
            canvas.scale(i / this.storyContainer.getMeasuredWidth(), i2 / this.storyContainer.getMeasuredHeight());
            this.imageReceiver.draw(canvas);
            canvas.restore();
            return;
        } else {
            bitmap = textureView.getBitmap(i, i2);
            if (bitmap == null) {
                return;
            }
        }
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
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
                        StoryMediaAreasView storyMediaAreasView = this.storyAreasView;
                        if (childAt != storyMediaAreasView || storyMediaAreasView.hasSelected() || (f >= AndroidUtilities.dp(60.0f) && f <= viewGroup.getMeasuredWidth() - AndroidUtilities.dp(60.0f))) {
                            if (this.keyboardVisible && childAt == this.chatActivityEnterView && f2 > rect2.top) {
                                return true;
                            }
                            if (!z && rect2.contains((int) f, (int) f2) && (((childAt.isClickable() || childAt == this.reactionsContainerLayout) && childAt.isEnabled()) || ((chatActivityEnterView = this.chatActivityEnterView) != null && childAt == chatActivityEnterView.getRecordCircle()))) {
                                return true;
                            }
                            if (childAt.isEnabled() && (childAt instanceof ViewGroup) && findClickableView((ViewGroup) childAt, f - childAt.getX(), f2 - childAt.getY(), z)) {
                                return true;
                            }
                        } else if (this.storyAreasView.hasClickableViews(f, f2)) {
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

    public ArrayList<Integer> getCurrentDay() {
        return this.day;
    }

    public long getCurrentPeer() {
        return this.dialogId;
    }

    public int getListPosition() {
        return this.listPosition;
    }

    public Bitmap getPlayingBitmap() {
        Bitmap createBitmap = Bitmap.createBitmap(this.storyContainer.getWidth(), this.storyContainer.getHeight(), Bitmap.Config.ARGB_8888);
        drawPlayingBitmap(createBitmap.getWidth(), createBitmap.getHeight(), new Canvas(createBitmap));
        return createBitmap;
    }

    public int getSelectedPosition() {
        return this.selectedPosition;
    }

    public int getStoriesCount() {
        return this.uploadingStories.size() + Math.max(this.totalStoriesCount, this.storyItems.size());
    }

    public ArrayList<TL_stories$StoryItem> getStoryItems() {
        return this.storyItems;
    }

    public abstract boolean isSelectedPeer();

    public boolean needEnterText() {
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView == null) {
            return false;
        }
        boolean isKeyboardVisible = chatActivityEnterView.isKeyboardVisible();
        if (isKeyboardVisible) {
            this.chatActivityEnterView.showEmojiView();
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                PeerStoriesView.this.lambda$needEnterText$44();
            }
        }, 300L);
        return isKeyboardVisible;
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attachedToWindow = true;
        this.imageReceiver.onAttachedToWindow();
        this.rightPreloadImageReceiver.onAttachedToWindow();
        this.leftPreloadImageReceiver.onAttachedToWindow();
        this.reactionEffectImageReceiver.onAttachedToWindow();
        this.reactionMoveImageReceiver.onAttachedToWindow();
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onResume();
        }
        for (int i = 0; i < this.preloadReactionHolders.size(); i++) {
            ((ReactionImageHolder) this.preloadReactionHolders.get(i)).onAttachedToWindow(true);
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storiesUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storyQualityUpdate);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storiesListUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stealthModeChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.storiesLimitUpdate);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.userIsPremiumBlockedUpadted);
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
        this.reactionEffectImageReceiver.onDetachedFromWindow();
        this.reactionMoveImageReceiver.onDetachedFromWindow();
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onPause();
        }
        AnimatedEmojiDrawable animatedEmojiDrawable = this.reactionMoveDrawable;
        if (animatedEmojiDrawable != null) {
            animatedEmojiDrawable.removeView(this);
            this.reactionMoveDrawable = null;
        }
        AnimatedEmojiEffect animatedEmojiEffect = this.emojiReactionEffect;
        if (animatedEmojiEffect != null) {
            animatedEmojiEffect.removeView(this);
            this.emojiReactionEffect = null;
        }
        for (int i = 0; i < this.preloadReactionHolders.size(); i++) {
            ((ReactionImageHolder) this.preloadReactionHolders.get(i)).onAttachedToWindow(false);
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storiesUpdated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storyQualityUpdate);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storiesListUpdated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stealthModeChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.storiesLimitUpdate);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.userIsPremiumBlockedUpadted);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.sharedResources.topOverlayGradient.setBounds(0, 0, getMeasuredWidth(), AndroidUtilities.dp(72.0f));
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int dp;
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
        int size = this.storyViewer.ATTACH_TO_FRAGMENT ? View.MeasureSpec.getSize(i2) : View.MeasureSpec.getSize(i2) + this.realKeyboardHeight;
        int size2 = (int) ((View.MeasureSpec.getSize(i) * 16.0f) / 9.0f);
        if (size <= size2 || size2 > size) {
            size2 = size;
        }
        if (this.realKeyboardHeight < AndroidUtilities.dp(20.0f)) {
            this.realKeyboardHeight = 0;
        }
        int i3 = this.realKeyboardHeight;
        ReactionsContainerLayout reactionsContainerLayout2 = this.likesReactionLayout;
        if (reactionsContainerLayout2 == null || reactionsContainerLayout2.getReactionsWindow() == null || !this.likesReactionLayout.getReactionsWindow().isShowing()) {
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
        } else {
            this.likesReactionLayout.getReactionsWindow().windowView.animate().translationY(-this.realKeyboardHeight).setDuration(250L).setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator).start();
            i3 = 0;
        }
        boolean z = this.keyboardVisible;
        if (this.lastKeyboardHeight != i3) {
            this.keyboardVisible = false;
            if (i3 <= 0 || !this.isActive) {
                ChatActivityEnterView chatActivityEnterView2 = this.chatActivityEnterView;
                if (chatActivityEnterView2 != null) {
                    this.storyViewer.saveDraft(this.dialogId, this.currentStory.storyItem, chatActivityEnterView2.getEditText());
                }
            } else {
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
            ChatActivityEnterView chatActivityEnterView3 = this.chatActivityEnterView;
            if (chatActivityEnterView3 != null) {
                chatActivityEnterView3.checkReactionsButton(!this.keyboardVisible);
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
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.PeerStoriesView$$ExternalSyntheticLambda23
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    PeerStoriesView.this.lambda$onMeasure$43(valueAnimator2);
                }
            });
            this.keyboardAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.PeerStoriesView.32
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    PeerStoriesView.this.notificationsLocker.unlock();
                    PeerStoriesView peerStoriesView = PeerStoriesView.this;
                    peerStoriesView.animatingKeyboardHeight = peerStoriesView.lastKeyboardHeight;
                    ChatActivityEnterView chatActivityEnterView4 = PeerStoriesView.this.chatActivityEnterView;
                    if (chatActivityEnterView4 != null) {
                        chatActivityEnterView4.onOverrideAnimationEnd();
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
                    ChatActivityEnterView chatActivityEnterView4 = this.chatActivityEnterView;
                    if (chatActivityEnterView4 != null) {
                        chatActivityEnterView4.getEditField().clearFocus();
                    }
                }
                this.animateKeyboardOpening = true;
            } else {
                this.animateKeyboardOpening = false;
            }
        }
        ChatActivityEnterView chatActivityEnterView5 = this.chatActivityEnterView;
        if (chatActivityEnterView5 != null && chatActivityEnterView5.getEmojiView() != null) {
            ((FrameLayout.LayoutParams) this.chatActivityEnterView.getEmojiView().getLayoutParams()).gravity = 80;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.storyContainer.getLayoutParams();
        layoutParams.height = size2;
        boolean z3 = size - size2 > AndroidUtilities.dp(64.0f);
        this.BIG_SCREEN = z3;
        int dp2 = (size - ((z3 ? AndroidUtilities.dp(64.0f) : 0) + size2)) >> 1;
        layoutParams.topMargin = dp2;
        this.enterViewBottomOffset = this.BIG_SCREEN ? (((-dp2) + size) - size2) - AndroidUtilities.dp(64.0f) : ((-dp2) + size) - size2;
        if (this.BIG_SCREEN != this.wasBigScreen) {
            this.storyContainer.setLayoutParams(layoutParams);
        }
        FrameLayout frameLayout = this.selfView;
        if (frameLayout != null) {
            ((FrameLayout.LayoutParams) frameLayout.getLayoutParams()).topMargin = this.BIG_SCREEN ? dp2 + size2 + AndroidUtilities.dp(8.0f) : (dp2 + size2) - AndroidUtilities.dp(48.0f);
        }
        TextView textView = this.replyDisabledTextView;
        if (textView != null) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) textView.getLayoutParams();
            if (this.BIG_SCREEN) {
                this.replyDisabledTextView.setTextColor(ColorUtils.blendARGB(-16777216, -1, 0.5f));
                dp = dp2 + size2 + AndroidUtilities.dp(12.0f);
            } else {
                this.replyDisabledTextView.setTextColor(ColorUtils.setAlphaComponent(-1, NotificationCenter.unconfirmedAuthUpdate));
                dp = ((dp2 + size2) - AndroidUtilities.dp(12.0f)) - AndroidUtilities.dp(40.0f);
            }
            layoutParams2.topMargin = dp;
        }
        InstantCameraView instantCameraView = this.instantCameraView;
        if (instantCameraView != null) {
            ((FrameLayout.LayoutParams) instantCameraView.getLayoutParams()).bottomMargin = i3 == 0 ? size - ((dp2 + size2) - AndroidUtilities.dp(64.0f)) : i3 + AndroidUtilities.dp(64.0f);
        }
        if (this.BIG_SCREEN) {
            ((FrameLayout.LayoutParams) this.bottomActionsLinearLayout.getLayoutParams()).topMargin = dp2 + size2 + AndroidUtilities.dp(12.0f);
            ((FrameLayout.LayoutParams) this.storyCaptionView.getLayoutParams()).bottomMargin = AndroidUtilities.dp(8.0f);
            if (this.wasBigScreen != this.BIG_SCREEN) {
                StoryCaptionView storyCaptionView = this.storyCaptionView;
                storyCaptionView.setLayoutParams((FrameLayout.LayoutParams) storyCaptionView.getLayoutParams());
            }
            this.storyCaptionView.blackoutBottomOffset = AndroidUtilities.dp(8.0f);
        } else {
            ((FrameLayout.LayoutParams) this.bottomActionsLinearLayout.getLayoutParams()).topMargin = ((dp2 + size2) - AndroidUtilities.dp(12.0f)) - AndroidUtilities.dp(40.0f);
            int dp3 = this.isSelf ? AndroidUtilities.dp(40.0f) : AndroidUtilities.dp(56.0f);
            ((FrameLayout.LayoutParams) this.storyCaptionView.getLayoutParams()).bottomMargin = dp3;
            if (this.wasBigScreen != this.BIG_SCREEN) {
                StoryCaptionView storyCaptionView2 = this.storyCaptionView;
                storyCaptionView2.setLayoutParams((FrameLayout.LayoutParams) storyCaptionView2.getLayoutParams());
            }
            this.storyCaptionView.blackoutBottomOffset = dp3;
        }
        this.forceUpdateOffsets = true;
        float dp4 = AndroidUtilities.dp(8.0f) + AndroidUtilities.dp(40.0f);
        if (this.privacyButton.getVisibility() == 0) {
            dp4 += AndroidUtilities.dp(60.0f);
        }
        if (this.muteIconContainer.getVisibility() == 0) {
            dp4 += AndroidUtilities.dp(40.0f);
        }
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.headerView.titleView.getLayoutParams();
        if (layoutParams3.rightMargin != dp4) {
            int i4 = (int) dp4;
            layoutParams3.rightMargin = i4;
            ((FrameLayout.LayoutParams) this.headerView.subtitleView[0].getLayoutParams()).rightMargin = i4;
            ((FrameLayout.LayoutParams) this.headerView.subtitleView[1].getLayoutParams()).rightMargin = i4;
            this.headerView.forceLayout();
        }
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
        this.wasBigScreen = this.BIG_SCREEN;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.progressToKeyboard = -1.0f;
        this.forceUpdateOffsets = true;
        invalidate();
    }

    public void preloadMainImage(long j) {
        if (this.dialogId == j && this.day == null) {
            return;
        }
        this.dialogId = j;
        updateStoryItems();
        updateSelectedPosition();
        updatePosition(true);
        TL_stories$PeerStories tL_stories$PeerStories = this.storyViewer.overrideUserStories;
        if (tL_stories$PeerStories != null) {
            this.storiesController.loadSkippedStories(tL_stories$PeerStories, true);
        } else {
            this.storiesController.loadSkippedStories(j);
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
            this.chatActivityEnterView.setAlpha(1.0f - this.outT);
        }
        ReactionsContainerLayout reactionsContainerLayout = this.reactionsContainerLayout;
        if (reactionsContainerLayout != null) {
            reactionsContainerLayout.reset();
        }
        ReactionsContainerLayout reactionsContainerLayout2 = this.likesReactionLayout;
        if (reactionsContainerLayout2 != null) {
            reactionsContainerLayout2.reset();
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
        cancelTextSelection();
    }

    public void selectPosition(int i) {
        if (this.selectedPosition != i) {
            this.selectedPosition = i;
            updatePosition();
        }
    }

    public void setAccount(int i) {
        this.currentAccount = i;
        this.storiesController = MessagesController.getInstance(i).storiesController;
        this.emojiAnimationsOverlay.setAccount(i);
        ReactionsContainerLayout reactionsContainerLayout = this.reactionsContainerLayout;
        if (reactionsContainerLayout != null) {
            reactionsContainerLayout.setCurrentAccount(i);
            this.reactionsContainerLayout.setMessage(null, null, true);
        }
        ReactionsContainerLayout reactionsContainerLayout2 = this.likesReactionLayout;
        if (reactionsContainerLayout2 != null) {
            reactionsContainerLayout2.setCurrentAccount(i);
        }
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
                this.headerView.backupImageView.getImageReceiver().setVisible(true, true);
                if (this.currentStory.storyItem != null) {
                    FileLog.d("StoryViewer displayed story dialogId=" + this.dialogId + " storyId=" + this.currentStory.storyItem.id + " " + this.currentStory.getMediaDebugString());
                }
            } else {
                cancelTextSelection();
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
            if (this.isSelf || this.isChannel) {
                this.storiesController.pollViewsForSelfStories(this.dialogId, this.isActive);
            }
        }
    }

    public void setActive(boolean z) {
        setActive(0L, z);
    }

    public void setDay(long j, ArrayList arrayList, int i) {
        this.dialogId = j;
        this.day = arrayList;
        bindInternal(i);
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public void setDialogId(long j, int i) {
        if (this.dialogId != j) {
            this.currentStory.clear();
        }
        this.dialogId = j;
        this.day = null;
        bindInternal(i);
        TL_stories$PeerStories tL_stories$PeerStories = this.storyViewer.overrideUserStories;
        if (tL_stories$PeerStories != null) {
            this.storiesController.loadSkippedStories(tL_stories$PeerStories, true);
        } else {
            this.storiesController.loadSkippedStories(j);
        }
    }

    public void setIsVisible(boolean z) {
        if (this.isVisible == z) {
            return;
        }
        this.isVisible = z;
        if (z) {
            this.imageReceiver.setCurrentAlpha(1.0f);
            checkStealthMode(false);
        }
    }

    public void setLongpressed(boolean z) {
        if (this.isActive) {
            this.isLongPressed = z;
            invalidate();
        }
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

    public void setPaused(boolean z) {
        if (this.paused != z) {
            this.paused = z;
            stopPlaying(z);
            this.lastDrawTime = 0L;
            this.storyContainer.invalidate();
        }
    }

    public void setViewsThumbImageReceiver(float f, float f2, float f3, SelfStoriesPreviewView.ImageHolder imageHolder) {
        this.viewsThumbAlpha = f;
        this.viewsThumbScale = 1.0f / f2;
        this.viewsThumbPivotY = f3;
        if (this.viewsThumbImageReceiver == imageHolder) {
            return;
        }
        this.viewsThumbImageReceiver = imageHolder;
        if (imageHolder == null || imageHolder.receiver.getBitmap() == null) {
            return;
        }
        this.imageReceiver.updateStaticDrawableThump(imageHolder.receiver.getBitmap().copy(Bitmap.Config.ARGB_8888, false));
    }

    public boolean showKeyboard() {
        TextView textView;
        EditTextCaption editField;
        if (this.chatActivityEnterView == null || (((textView = this.replyDisabledTextView) != null && textView.getVisibility() == 0) || (editField = this.chatActivityEnterView.getEditField()) == null)) {
            return false;
        }
        editField.requestFocus();
        AndroidUtilities.showKeyboard(editField);
        return true;
    }

    public void showNoSoundHint(boolean z) {
        if (this.soundTooltip == null) {
            HintView2 joint = new HintView2(getContext(), 1).setJoint(1.0f, -56.0f);
            this.soundTooltip = joint;
            joint.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
            this.storyContainer.addView(this.soundTooltip, LayoutHelper.createFrame(-1, -2.0f, 55, 0.0f, 52.0f, 0.0f, 0.0f));
        }
        this.soundTooltip.setText(LocaleController.getString(z ? R.string.StoryNoSound : R.string.StoryTapToSound));
        this.soundTooltip.show();
    }

    public void stopPlaying(boolean z) {
        ImageReceiver imageReceiver;
        boolean z2;
        if (z) {
            this.imageReceiver.stopAnimation();
            imageReceiver = this.imageReceiver;
            z2 = false;
        } else {
            this.imageReceiver.startAnimation();
            imageReceiver = this.imageReceiver;
            z2 = true;
        }
        imageReceiver.setAllowStartAnimation(z2);
    }

    public boolean switchToNext(boolean z) {
        int i;
        if (this.storyViewer.reversed) {
            z = !z;
        }
        if (!z) {
            int i2 = this.selectedPosition;
            if (i2 <= 0) {
                return false;
            }
            i = i2 - 1;
        } else if (this.selectedPosition >= getStoriesCount() - 1) {
            return false;
        } else {
            i = this.selectedPosition + 1;
        }
        this.selectedPosition = i;
        updatePosition();
        return true;
    }

    public void updatePosition() {
        updatePosition(false);
    }

    public boolean useSurfaceInViewPagerWorkAround() {
        return this.storyViewer.USE_SURFACE_VIEW && Build.VERSION.SDK_INT < 33;
    }

    public boolean viewsAllowed() {
        return this.isSelf || (this.isChannel && this.userCanSeeViews);
    }
}
