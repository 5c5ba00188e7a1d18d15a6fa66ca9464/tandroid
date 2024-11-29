package org.telegram.ui.Stories;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import java.util.ArrayList;
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.FileStreamLoadOperation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.support.LongSparseIntArray;
import org.telegram.messenger.video.VideoPlayerHolderBase;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ArticleViewer;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgress;
import org.telegram.ui.Components.ReactionsContainerLayout;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.Stories.PeerStoriesView;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.StoriesListPlaceProvider;
import org.telegram.ui.Stories.StoriesUtilities;
import org.telegram.ui.Stories.StoryViewer;
import org.webrtc.MediaStreamTrack;

/* loaded from: classes5.dex */
public class StoryViewer implements NotificationCenter.NotificationCenterDelegate, BaseFragment.AttachedSheet {
    public static boolean animationInProgress;
    private static boolean isInSilentMode;
    private static TL_stories.StoryItem lastStoryItem;
    private static boolean runOpenAnimationAfterLayout;
    boolean allowIntercept;
    boolean allowSelfStoriesView;
    boolean allowSwipeToDissmiss;
    boolean allowSwipeToReply;
    private boolean animateAvatar;
    StoriesListPlaceProvider.AvatarOverlaysView animateFromCell;
    AspectRatioFrameLayout aspectRatioFrameLayout;
    float clipBottom;
    float clipTop;
    HwFrameLayout containerView;
    public int currentAccount;
    Dialog currentDialog;
    PeerStoriesView.VideoPlayerSharedScope currentPlayerScope;
    BaseFragment.AttachedSheet currentSheet;
    public int dayStoryId;
    private Runnable delayedTapRunnable;
    private boolean flingCalled;
    BaseFragment fragment;
    public boolean fromBottomSheet;
    float fromDismissOffset;
    float fromHeight;
    private int fromRadius;
    float fromWidth;
    float fromX;
    float fromXCell;
    float fromY;
    float fromYCell;
    private boolean fullyVisible;
    GestureDetector gestureDetector;
    private float hideEnterViewProgress;
    boolean inSeekingMode;
    boolean inSwipeToDissmissMode;
    Paint inputBackgroundPaint;
    private boolean invalidateOutRect;
    private boolean isBulletinVisible;
    private boolean isCaption;
    private boolean isCaptionPartVisible;
    private boolean isHintVisible;
    private boolean isInPinchToZoom;
    private boolean isInTextSelectionMode;
    private boolean isInTouchMode;
    private boolean isLikesReactions;
    public boolean isLongpressed;
    private boolean isOverlayVisible;
    private boolean isPopupVisible;
    private boolean isRecording;
    boolean isShowing;
    boolean isSingleStory;
    private boolean isSwiping;
    private boolean isWaiting;
    int j;
    boolean keyboardVisible;
    long lastDialogId;
    int lastPosition;
    private float lastStoryContainerHeight;
    Uri lastUri;
    private int messageId;
    private Runnable onCloseListener;
    ValueAnimator openCloseAnimator;
    boolean openedFromLightNavigationBar;
    private boolean opening;
    TL_stories.PeerStories overrideUserStories;
    LaunchActivity parentActivity;
    private boolean paused;
    public PlaceProvider placeProvider;
    VideoPlayerHolder playerHolder;
    private long playerSavedPosition;
    float progressToDismiss;
    float progressToOpen;
    private int realKeyboardHeight;
    boolean reversed;
    float selfStoriesViewsOffset;
    SelfStoryViewsView selfStoryViewsView;
    private boolean showViewsAfterOpening;
    TL_stories.StoryItem singleStory;
    private StoriesIntro storiesIntro;
    StoriesController.StoriesList storiesList;
    public StoriesViewPager storiesViewPager;
    private SurfaceView surfaceView;
    float swipeToDismissHorizontalDirection;
    float swipeToDismissHorizontalOffset;
    float swipeToDismissOffset;
    ValueAnimator swipeToDissmissBackAnimator;
    ValueAnimator swipeToReplyBackAnimator;
    float swipeToReplyOffset;
    float swipeToReplyProgress;
    boolean swipeToReplyWaitingKeyboard;
    ValueAnimator swipeToViewsAnimator;
    private TextureView textureView;
    public boolean unreadStateChanged;
    boolean verticalScrollDetected;
    private StoriesVolumeControl volumeControl;
    WindowManager.LayoutParams windowLayoutParams;
    WindowManager windowManager;
    public SizeNotifierFrameLayout windowView;
    public static ArrayList globalInstances = new ArrayList();
    public static float currentSpeed = 1.0f;
    private static boolean checkSilentMode = true;
    private static final LongSparseArray replyDrafts = new LongSparseArray();
    static int J = 0;
    public boolean USE_SURFACE_VIEW = SharedConfig.useSurfaceInStories;
    public boolean ATTACH_TO_FRAGMENT = true;
    public boolean foundViewToClose = false;
    public boolean allowScreenshots = true;
    Theme.ResourcesProvider resourcesProvider = new DarkThemeResourceProvider();
    RectF avatarRectTmp = new RectF();
    float[] pointPosition = new float[2];
    public final TransitionViewHolder transitionViewHolder = new TransitionViewHolder();
    private boolean allowTouchesByViewpager = false;
    ArrayList doOnAnimationReadyRunnables = new ArrayList();
    private boolean isClosed = true;
    AnimationNotificationsLocker locker = new AnimationNotificationsLocker();
    ArrayList preparedPlayers = new ArrayList();
    public boolean isTranslating = false;
    Runnable longPressRunnable = new Runnable() { // from class: org.telegram.ui.Stories.StoryViewer$$ExternalSyntheticLambda4
        @Override // java.lang.Runnable
        public final void run() {
            StoryViewer.this.lambda$new$0();
        }
    };
    public LongSparseIntArray savedPositions = new LongSparseIntArray();

    class 10 extends AnimatorListenerAdapter {
        10() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0(View view) {
            StoryViewer.this.storiesIntro.animate().alpha(0.0f).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.StoryViewer.10.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (StoryViewer.this.storiesIntro != null) {
                        StoryViewer.this.storiesIntro.stopAnimation();
                        StoryViewer storyViewer = StoryViewer.this;
                        storyViewer.containerView.removeView(storyViewer.storiesIntro);
                    }
                    StoryViewer.this.storiesIntro = null;
                    StoryViewer.this.updatePlayingMode();
                }
            }).start();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            StoryViewer storyViewer = StoryViewer.this;
            storyViewer.progressToOpen = 1.0f;
            storyViewer.checkNavBarColor();
            StoryViewer.animationInProgress = false;
            HwFrameLayout hwFrameLayout = StoryViewer.this.containerView;
            if (hwFrameLayout != null) {
                hwFrameLayout.disableHwAcceleration();
            }
            SizeNotifierFrameLayout sizeNotifierFrameLayout = StoryViewer.this.windowView;
            if (sizeNotifierFrameLayout != null) {
                sizeNotifierFrameLayout.invalidate();
            }
            StoryViewer storyViewer2 = StoryViewer.this;
            ImageReceiver imageReceiver = storyViewer2.transitionViewHolder.avatarImage;
            if (imageReceiver != null && !storyViewer2.foundViewToClose) {
                imageReceiver.setVisible(true, true);
                StoryViewer.this.transitionViewHolder.avatarImage = null;
            }
            StoryViewer storyViewer3 = StoryViewer.this;
            ImageReceiver imageReceiver2 = storyViewer3.transitionViewHolder.storyImage;
            if (imageReceiver2 != null && !storyViewer3.foundViewToClose) {
                imageReceiver2.setAlpha(1.0f);
                StoryViewer.this.transitionViewHolder.storyImage.setVisible(true, true);
                StoryViewer.this.transitionViewHolder.storyImage = null;
            }
            PeerStoriesView currentPeerView = StoryViewer.this.getCurrentPeerView();
            if (currentPeerView != null) {
                currentPeerView.updatePosition();
            }
            if (StoryViewer.this.showViewsAfterOpening) {
                StoryViewer.this.showViewsAfterOpening = false;
                StoryViewer.this.openViews();
            } else if (!SharedConfig.storiesIntroShown) {
                if (StoryViewer.this.storiesIntro == null) {
                    StoryViewer storyViewer4 = StoryViewer.this;
                    if (storyViewer4.containerView != null) {
                        storyViewer4.storiesIntro = new StoriesIntro(StoryViewer.this.containerView.getContext(), StoryViewer.this.windowView);
                        StoryViewer.this.storiesIntro.setAlpha(0.0f);
                        StoryViewer storyViewer5 = StoryViewer.this;
                        storyViewer5.containerView.addView(storyViewer5.storiesIntro);
                    }
                }
                if (StoryViewer.this.storiesIntro != null) {
                    StoryViewer.this.storiesIntro.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.StoryViewer$10$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            StoryViewer.10.this.lambda$onAnimationEnd$0(view);
                        }
                    });
                    StoryViewer.this.storiesIntro.animate().alpha(1.0f).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.StoryViewer.10.2
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator2) {
                            super.onAnimationEnd(animator2);
                            if (StoryViewer.this.storiesIntro != null) {
                                StoryViewer.this.storiesIntro.startAnimation(true);
                            }
                        }
                    }).start();
                }
                SharedConfig.setStoriesIntroShown(true);
            }
            StoryViewer.this.updatePlayingMode();
            StoryViewer.this.locker.unlock();
        }
    }

    class 11 extends AnimatorListenerAdapter {
        11() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0() {
            try {
                StoryViewer storyViewer = StoryViewer.this;
                SizeNotifierFrameLayout sizeNotifierFrameLayout = storyViewer.windowView;
                if (sizeNotifierFrameLayout == null) {
                    return;
                }
                if (storyViewer.ATTACH_TO_FRAGMENT) {
                    AndroidUtilities.removeFromParent(sizeNotifierFrameLayout);
                } else {
                    storyViewer.windowManager.removeView(sizeNotifierFrameLayout);
                }
                StoryViewer.this.windowView = null;
            } catch (Exception unused) {
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            PeerStoriesView currentPeerView;
            RadialProgress radialProgress;
            super.onAnimationEnd(animator);
            HwFrameLayout hwFrameLayout = StoryViewer.this.containerView;
            if (hwFrameLayout != null) {
                hwFrameLayout.disableHwAcceleration();
            }
            StoryViewer.this.checkNavBarColor();
            StoryViewer.this.locker.unlock();
            if (StoryViewer.this.storiesIntro != null) {
                StoryViewer.this.storiesIntro.stopAnimation();
                AndroidUtilities.removeFromParent(StoryViewer.this.storiesIntro);
                StoryViewer.this.storiesIntro = null;
            }
            ImageReceiver imageReceiver = StoryViewer.this.transitionViewHolder.avatarImage;
            if (imageReceiver != null) {
                imageReceiver.setVisible(true, true);
                StoryViewer.this.transitionViewHolder.avatarImage = null;
            }
            ImageReceiver imageReceiver2 = StoryViewer.this.transitionViewHolder.storyImage;
            if (imageReceiver2 != null) {
                imageReceiver2.setAlpha(1.0f);
                StoryViewer.this.transitionViewHolder.storyImage.setVisible(true, true);
            }
            StoryViewer storyViewer = StoryViewer.this;
            if (storyViewer.transitionViewHolder.radialProgressUpload != null && (currentPeerView = storyViewer.getCurrentPeerView()) != null && (radialProgress = currentPeerView.headerView.radialProgress) != null) {
                StoryViewer.this.transitionViewHolder.radialProgressUpload.copyParams(radialProgress);
            }
            PeerStoriesView.VideoPlayerSharedScope videoPlayerSharedScope = StoryViewer.this.currentPlayerScope;
            if (videoPlayerSharedScope != null) {
                videoPlayerSharedScope.invalidate();
            }
            StoryViewer.this.release();
            try {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoryViewer$11$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        StoryViewer.11.this.lambda$onAnimationEnd$0();
                    }
                });
            } catch (Exception unused) {
            }
            StoryViewer storyViewer2 = StoryViewer.this;
            storyViewer2.isShowing = false;
            storyViewer2.foundViewToClose = false;
            if (storyViewer2.onCloseListener != null) {
                StoryViewer.this.onCloseListener.run();
                StoryViewer.this.onCloseListener = null;
            }
        }
    }

    class 2 extends SizeNotifierFrameLayout {
        float lastTouchX;
        SparseArray lastX;
        final RectF outFromRectAvatar;
        final RectF outFromRectContainer;
        final Path path;
        final RectF rect1;
        final RectF rect2;
        final RectF rect3;
        float startX;
        float startY;
        final /* synthetic */ BaseFragment val$fragment;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        2(Context context, BaseFragment baseFragment) {
            super(context);
            this.val$fragment = baseFragment;
            this.path = new Path();
            this.rect1 = new RectF();
            this.rect2 = new RectF();
            this.rect3 = new RectF();
            this.outFromRectAvatar = new RectF();
            this.outFromRectContainer = new RectF();
            this.lastX = new SparseArray();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$dispatchTouchEvent$0(ValueAnimator valueAnimator) {
            StoryViewer.this.swipeToDismissHorizontalOffset = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            StoryViewer.this.updateProgressToDismiss();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onInterceptTouchEvent$1() {
            StoryViewer.this.setInTouchMode(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTouchEvent$2(ValueAnimator valueAnimator) {
            StoryViewer.this.swipeToDismissOffset = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            StoryViewer.this.updateProgressToDismiss();
        }

        /* JADX WARN: Removed duplicated region for block: B:165:0x0587 A[LOOP:1: B:157:0x0553->B:165:0x0587, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:166:0x058f A[EDGE_INSN: B:166:0x058f->B:167:0x058f BREAK  A[LOOP:1: B:157:0x0553->B:165:0x0587], SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:46:0x07e0  */
        /* JADX WARN: Removed duplicated region for block: B:49:? A[RETURN, SYNTHETIC] */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void dispatchDraw(Canvas canvas) {
            PeerStoriesView.PeerHeaderView peerHeaderView;
            float f;
            float y;
            float f2;
            Paint paint;
            HolderDrawAbove holderDrawAbove;
            HolderClip holderClip;
            PeerStoriesView currentPeerView;
            StoryViewer storyViewer;
            TransitionViewHolder transitionViewHolder;
            float y2;
            canvas.drawColor(ColorUtils.setAlphaComponent(-16777216, (int) (StoryViewer.this.getBlackoutAlpha() * 255.0f)));
            StoryViewer storyViewer2 = StoryViewer.this;
            if (storyViewer2.ATTACH_TO_FRAGMENT) {
                boolean z = storyViewer2.progressToOpen * (1.0f - storyViewer2.progressToDismiss) == 1.0f;
                if (storyViewer2.fullyVisible != z) {
                    StoryViewer.this.fullyVisible = z;
                    if (this.val$fragment.getLayoutContainer() != null) {
                        this.val$fragment.getLayoutContainer().invalidate();
                    }
                }
            }
            PeerStoriesView currentPeerView2 = StoryViewer.this.storiesViewPager.getCurrentPeerView();
            if (currentPeerView2 != null) {
                peerHeaderView = currentPeerView2.headerView;
                if (StoryViewer.this.animateAvatar) {
                    peerHeaderView.backupImageView.getImageReceiver().setVisible(StoryViewer.this.progressToOpen == 1.0f, true);
                } else {
                    peerHeaderView.backupImageView.getImageReceiver().setVisible(true, false);
                }
                if (StoryViewer.this.invalidateOutRect) {
                    StoryViewer.this.invalidateOutRect = false;
                    float f3 = 0.0f;
                    float f4 = 0.0f;
                    for (View view = peerHeaderView.backupImageView; view != this; view = (View) view.getParent()) {
                        if (view.getParent() == this) {
                            f3 += view.getLeft();
                            y2 = view.getTop();
                        } else if (view.getParent() != StoryViewer.this.storiesViewPager) {
                            f3 += view.getX();
                            y2 = view.getY();
                        }
                        f4 += y2;
                    }
                    this.outFromRectAvatar.set(f3, f4, peerHeaderView.backupImageView.getMeasuredWidth() + f3, peerHeaderView.backupImageView.getMeasuredHeight() + f4);
                    this.outFromRectContainer.set(0.0f, currentPeerView2.getTop() + currentPeerView2.storyContainer.getTop(), StoryViewer.this.containerView.getMeasuredWidth(), StoryViewer.this.containerView.getMeasuredHeight());
                    StoryViewer.this.containerView.getMatrix().mapRect(this.outFromRectAvatar);
                    StoryViewer.this.containerView.getMatrix().mapRect(this.outFromRectContainer);
                }
            } else {
                peerHeaderView = null;
            }
            StoryViewer.this.volumeControl.setAlpha(1.0f - StoryViewer.this.progressToDismiss);
            StoryViewer storyViewer3 = StoryViewer.this;
            float clamp = storyViewer3.swipeToDismissHorizontalOffset == 0.0f ? 1.0f - Utilities.clamp(Math.abs(storyViewer3.swipeToDismissOffset / getMeasuredHeight()), 1.0f, 0.0f) : 1.0f;
            StoryViewer storyViewer4 = StoryViewer.this;
            storyViewer4.storiesViewPager.setHorizontalProgressToDismiss((storyViewer4.swipeToDismissHorizontalOffset / storyViewer4.containerView.getMeasuredWidth()) * StoryViewer.this.progressToOpen);
            StoryViewer storyViewer5 = StoryViewer.this;
            if (storyViewer5.fromX != 0.0f || storyViewer5.fromY != 0.0f) {
                float f5 = storyViewer5.progressToOpen;
                if (f5 != 1.0f) {
                    if (storyViewer5.isClosed && StoryViewer.this.animateAvatar) {
                        f5 = StoryViewer.this.progressToOpen;
                        float clamp2 = 1.0f - Utilities.clamp(((1.0f - f5) - 0.8f) / 0.100000024f, 1.0f, 0.0f);
                        f = Utilities.clamp(f5 - ((1.0f - clamp2) * 0.05f), 1.0f, 0.0f);
                        StoryViewer.this.containerView.setAlpha(clamp2);
                    } else {
                        StoryViewer.this.containerView.setAlpha(1.0f);
                        f = f5;
                    }
                    if (!StoryViewer.this.isClosed || (transitionViewHolder = (storyViewer = StoryViewer.this).transitionViewHolder) == null || transitionViewHolder.storyImage == null) {
                        boolean unused = StoryViewer.this.isClosed;
                    } else {
                        HwFrameLayout hwFrameLayout = storyViewer.containerView;
                        hwFrameLayout.setAlpha(hwFrameLayout.getAlpha() * ((float) Math.pow(f5, 0.20000000298023224d)));
                    }
                    StoryViewer storyViewer6 = StoryViewer.this;
                    HwFrameLayout hwFrameLayout2 = storyViewer6.containerView;
                    float left = (storyViewer6.fromX - hwFrameLayout2.getLeft()) - (StoryViewer.this.containerView.getMeasuredWidth() / 2.0f);
                    StoryViewer storyViewer7 = StoryViewer.this;
                    float f6 = storyViewer7.progressToOpen;
                    hwFrameLayout2.setTranslationX((left * (1.0f - f6)) + (storyViewer7.swipeToDismissHorizontalOffset * f6));
                    StoryViewer storyViewer8 = StoryViewer.this;
                    HwFrameLayout hwFrameLayout3 = storyViewer8.containerView;
                    float top = (storyViewer8.fromY - hwFrameLayout3.getTop()) - (StoryViewer.this.containerView.getMeasuredHeight() / 2.0f);
                    StoryViewer storyViewer9 = StoryViewer.this;
                    float f7 = storyViewer9.progressToOpen;
                    hwFrameLayout3.setTranslationY((top * (1.0f - f7)) + (storyViewer9.swipeToDismissOffset * f7));
                    float lerp = AndroidUtilities.lerp(StoryViewer.this.fromWidth / r3.containerView.getMeasuredWidth(), (clamp * 0.15f) + 0.85f, f);
                    StoryViewer.this.containerView.setScaleX(lerp);
                    StoryViewer.this.containerView.setScaleY(lerp);
                    this.path.rewind();
                    RectF rectF = this.rect1;
                    StoryViewer storyViewer10 = StoryViewer.this;
                    float f8 = storyViewer10.fromX;
                    float f9 = storyViewer10.fromWidth / 2.0f;
                    float f10 = storyViewer10.fromY;
                    float f11 = storyViewer10.fromHeight / 2.0f;
                    rectF.set(f8 - f9, f10 - f11, f8 + f9, f10 + f11);
                    if (StoryViewer.this.isClosed && StoryViewer.this.animateAvatar) {
                        this.rect2.set(this.outFromRectContainer);
                    } else if (currentPeerView2 != null) {
                        this.rect2.set(0.0f, currentPeerView2.storyContainer.getTop() + StoryViewer.this.fromDismissOffset, getMeasuredWidth(), getMeasuredHeight() + StoryViewer.this.fromDismissOffset);
                    } else {
                        this.rect2.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                    }
                    if (StoryViewer.this.isClosed && StoryViewer.this.animateAvatar) {
                        this.rect1.inset(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f));
                    }
                    float lerp2 = AndroidUtilities.lerp(this.rect1.centerX(), this.rect2.centerX(), StoryViewer.this.progressToOpen);
                    float lerp3 = AndroidUtilities.lerp(this.rect1.centerY(), this.rect2.centerY(), StoryViewer.this.progressToOpen);
                    float lerp4 = AndroidUtilities.lerp(this.rect1.height(), this.rect2.height(), f);
                    float lerp5 = AndroidUtilities.lerp(this.rect1.width(), this.rect2.width(), f);
                    if (StoryViewer.this.isClosed && StoryViewer.this.animateAvatar) {
                        this.rect1.inset(-AndroidUtilities.dp(12.0f), -AndroidUtilities.dp(12.0f));
                    }
                    RectF rectF2 = AndroidUtilities.rectTmp;
                    float f12 = lerp5 / 2.0f;
                    float f13 = lerp4 / 2.0f;
                    rectF2.set(lerp2 - f12, lerp3 - f13, lerp2 + f12, lerp3 + f13);
                    float lerp6 = StoryViewer.this.animateAvatar ? AndroidUtilities.lerp(StoryViewer.this.fromWidth / 2.0f, 0.0f, f) : AndroidUtilities.lerp(StoryViewer.this.fromRadius, 0.0f, f5);
                    this.path.addRoundRect(rectF2, lerp6, lerp6, Path.Direction.CCW);
                    canvas.save();
                    StoryViewer storyViewer11 = StoryViewer.this;
                    float f14 = storyViewer11.clipTop;
                    if (f14 != 0.0f && storyViewer11.clipBottom != 0.0f) {
                        float lerp7 = AndroidUtilities.lerp(0.0f, f14, (float) Math.pow(1.0f - storyViewer11.progressToOpen, 0.4000000059604645d));
                        float measuredWidth = getMeasuredWidth();
                        float measuredHeight = getMeasuredHeight();
                        StoryViewer storyViewer12 = StoryViewer.this;
                        canvas.clipRect(0.0f, lerp7, measuredWidth, AndroidUtilities.lerp(measuredHeight, storyViewer12.clipBottom, 1.0f - storyViewer12.progressToOpen));
                    }
                    canvas.save();
                    canvas.clipPath(this.path);
                    super.dispatchDraw(canvas);
                    StoryViewer storyViewer13 = StoryViewer.this;
                    TransitionViewHolder transitionViewHolder2 = storyViewer13.transitionViewHolder;
                    if (transitionViewHolder2 != null && transitionViewHolder2.storyImage != null && (currentPeerView = storyViewer13.storiesViewPager.getCurrentPeerView()) != null && currentPeerView.storyContainer != null) {
                        boolean visible = StoryViewer.this.transitionViewHolder.storyImage.getVisible();
                        this.rect2.set(StoryViewer.this.swipeToDismissHorizontalOffset + r5.containerView.getLeft() + currentPeerView.getX() + currentPeerView.storyContainer.getX(), StoryViewer.this.swipeToDismissOffset + r5.containerView.getTop() + currentPeerView.getY() + currentPeerView.storyContainer.getY(), ((StoryViewer.this.swipeToDismissHorizontalOffset + r5.containerView.getRight()) - (StoryViewer.this.containerView.getWidth() - currentPeerView.getRight())) - (currentPeerView.getWidth() - currentPeerView.storyContainer.getRight()), ((StoryViewer.this.swipeToDismissOffset + r5.containerView.getBottom()) - (StoryViewer.this.containerView.getHeight() - currentPeerView.getBottom())) - (currentPeerView.getHeight() - currentPeerView.storyContainer.getBottom()));
                        AndroidUtilities.lerp(this.rect1, this.rect2, f5, this.rect3);
                        float imageX = StoryViewer.this.transitionViewHolder.storyImage.getImageX();
                        float imageY = StoryViewer.this.transitionViewHolder.storyImage.getImageY();
                        float imageWidth = StoryViewer.this.transitionViewHolder.storyImage.getImageWidth();
                        float imageHeight = StoryViewer.this.transitionViewHolder.storyImage.getImageHeight();
                        StoryViewer.this.transitionViewHolder.storyImage.setImageCoords(this.rect3);
                        float f15 = 1.0f - f5;
                        StoryViewer.this.transitionViewHolder.storyImage.setAlpha(f15);
                        StoryViewer.this.transitionViewHolder.storyImage.setVisible(true, false);
                        int saveCount = canvas.getSaveCount();
                        StoryViewer storyViewer14 = StoryViewer.this;
                        HolderClip holderClip2 = storyViewer14.transitionViewHolder.drawClip;
                        if (holderClip2 != null) {
                            holderClip2.clip(canvas, this.rect3, f15, storyViewer14.opening);
                        }
                        StoryViewer.this.transitionViewHolder.storyImage.draw(canvas);
                        StoryViewer storyViewer15 = StoryViewer.this;
                        HolderDrawAbove holderDrawAbove2 = storyViewer15.transitionViewHolder.drawAbove;
                        if (holderDrawAbove2 != null) {
                            holderDrawAbove2.draw(canvas, this.rect3, f15, storyViewer15.opening);
                        }
                        StoryViewer.this.transitionViewHolder.storyImage.setVisible(visible, false);
                        StoryViewer.this.transitionViewHolder.storyImage.setImageCoords(imageX, imageY, imageWidth, imageHeight);
                        canvas.restoreToCount(saveCount);
                    }
                    canvas.restore();
                    if (peerHeaderView != null) {
                        StoryViewer storyViewer16 = StoryViewer.this;
                        float f16 = storyViewer16.swipeToDismissHorizontalOffset;
                        float f17 = storyViewer16.swipeToDismissOffset;
                        if (storyViewer16.isClosed && StoryViewer.this.animateAvatar) {
                            this.rect2.set(this.outFromRectAvatar);
                        } else {
                            for (View view2 = peerHeaderView.backupImageView; view2 != this && view2 != null; view2 = (View) view2.getParent()) {
                                if (view2.getParent() == this) {
                                    f16 += view2.getLeft();
                                    y = view2.getTop();
                                } else {
                                    if (view2.getParent() != StoryViewer.this.storiesViewPager) {
                                        f16 += view2.getX();
                                        y = view2.getY();
                                    }
                                    if (view2.getParent() instanceof View) {
                                        break;
                                    }
                                }
                                f17 += y;
                                if (view2.getParent() instanceof View) {
                                }
                            }
                            this.rect2.set(f16, f17, peerHeaderView.backupImageView.getMeasuredWidth() + f16, peerHeaderView.backupImageView.getMeasuredHeight() + f17);
                        }
                        AndroidUtilities.lerp(this.rect1, this.rect2, StoryViewer.this.progressToOpen, this.rect3);
                        int saveCount2 = canvas.getSaveCount();
                        StoryViewer storyViewer17 = StoryViewer.this;
                        TransitionViewHolder transitionViewHolder3 = storyViewer17.transitionViewHolder;
                        if (transitionViewHolder3 != null && (holderClip = transitionViewHolder3.drawClip) != null) {
                            holderClip.clip(canvas, this.rect3, 1.0f - f5, storyViewer17.opening);
                        }
                        if (StoryViewer.this.animateAvatar) {
                            StoryViewer storyViewer18 = StoryViewer.this;
                            TransitionViewHolder transitionViewHolder4 = storyViewer18.transitionViewHolder;
                            boolean z2 = (transitionViewHolder4 == null || transitionViewHolder4.crossfadeToAvatarImage == null) ? false : true;
                            if (!z2 || storyViewer18.progressToOpen != 0.0f) {
                                peerHeaderView.backupImageView.getImageReceiver().setImageCoords(this.rect3);
                                TransitionViewHolder transitionViewHolder5 = StoryViewer.this.transitionViewHolder;
                                peerHeaderView.backupImageView.getImageReceiver().setRoundRadius((int) AndroidUtilities.lerp(this.rect3.width() / 2.0f, (transitionViewHolder5 != null ? transitionViewHolder5.getAvatarImageRoundRadius() : null) != null ? r4.intValue() : this.rect3.width() / 2.0f, 1.0f - StoryViewer.this.progressToOpen));
                                peerHeaderView.backupImageView.getImageReceiver().setVisible(true, false);
                                float f18 = z2 ? StoryViewer.this.progressToOpen : 1.0f;
                                TransitionViewHolder transitionViewHolder6 = StoryViewer.this.transitionViewHolder;
                                if (transitionViewHolder6 == null || transitionViewHolder6.alpha >= 1.0f || (paint = transitionViewHolder6.bgPaint) == null) {
                                    f2 = f18;
                                } else {
                                    paint.setAlpha((int) ((1.0f - f5) * 255.0f));
                                    canvas.drawCircle(this.rect3.centerX(), this.rect3.centerY(), this.rect3.width() / 2.0f, StoryViewer.this.transitionViewHolder.bgPaint);
                                    f2 = AndroidUtilities.lerp(StoryViewer.this.transitionViewHolder.alpha, f18, f5);
                                }
                                peerHeaderView.backupImageView.getImageReceiver().setAlpha(f2);
                                peerHeaderView.drawUploadingProgress(canvas, this.rect3, !StoryViewer.runOpenAnimationAfterLayout, StoryViewer.this.progressToOpen);
                                peerHeaderView.backupImageView.getImageReceiver().draw(canvas);
                                peerHeaderView.backupImageView.getImageReceiver().setAlpha(f18);
                                peerHeaderView.backupImageView.getImageReceiver().setVisible(false, false);
                            }
                            StoryViewer storyViewer19 = StoryViewer.this;
                            if (storyViewer19.progressToOpen != 1.0f && z2) {
                                storyViewer19.avatarRectTmp.set(storyViewer19.transitionViewHolder.crossfadeToAvatarImage.getImageX(), StoryViewer.this.transitionViewHolder.crossfadeToAvatarImage.getImageY(), StoryViewer.this.transitionViewHolder.crossfadeToAvatarImage.getImageX2(), StoryViewer.this.transitionViewHolder.crossfadeToAvatarImage.getImageY2());
                                int i = StoryViewer.this.transitionViewHolder.crossfadeToAvatarImage.getRoundRadius()[0];
                                boolean visible2 = StoryViewer.this.transitionViewHolder.crossfadeToAvatarImage.getVisible();
                                StoryViewer.this.transitionViewHolder.crossfadeToAvatarImage.setImageCoords(this.rect3);
                                StoryViewer.this.transitionViewHolder.crossfadeToAvatarImage.setRoundRadius((int) (this.rect3.width() / 2.0f));
                                StoryViewer.this.transitionViewHolder.crossfadeToAvatarImage.setVisible(true, false);
                                canvas.saveLayerAlpha(this.rect3, (int) ((1.0f - StoryViewer.this.progressToOpen) * 255.0f), 31);
                                StoryViewer.this.transitionViewHolder.crossfadeToAvatarImage.draw(canvas);
                                canvas.restore();
                                StoryViewer.this.transitionViewHolder.crossfadeToAvatarImage.setVisible(visible2, false);
                                StoryViewer storyViewer20 = StoryViewer.this;
                                storyViewer20.transitionViewHolder.crossfadeToAvatarImage.setImageCoords(storyViewer20.avatarRectTmp);
                                StoryViewer.this.transitionViewHolder.crossfadeToAvatarImage.setRoundRadius(i);
                            }
                            StoryViewer storyViewer21 = StoryViewer.this;
                            TransitionViewHolder transitionViewHolder7 = storyViewer21.transitionViewHolder;
                            if (transitionViewHolder7 != null && (holderDrawAbove = transitionViewHolder7.drawAbove) != null) {
                                holderDrawAbove.draw(canvas, this.rect3, 1.0f - f5, storyViewer21.opening);
                            }
                        }
                        canvas.restoreToCount(saveCount2);
                    }
                    StoryViewer storyViewer22 = StoryViewer.this;
                    if (storyViewer22.animateFromCell != null) {
                        float clamp3 = Utilities.clamp(storyViewer22.progressToOpen / 0.4f, 1.0f, 0.0f);
                        if (clamp3 != 1.0f) {
                            RectF rectF3 = AndroidUtilities.rectTmp;
                            StoryViewer storyViewer23 = StoryViewer.this;
                            float f19 = storyViewer23.fromX;
                            float f20 = storyViewer23.fromY;
                            rectF3.set(f19, f20, storyViewer23.fromWidth + f19, storyViewer23.fromHeight + f20);
                            rectF3.inset(-AndroidUtilities.dp(16.0f), -AndroidUtilities.dp(16.0f));
                            if (clamp3 != 0.0f) {
                                canvas.saveLayerAlpha(rectF3, (int) ((1.0f - clamp3) * 255.0f), 31);
                            } else {
                                canvas.save();
                            }
                            StoryViewer storyViewer24 = StoryViewer.this;
                            canvas.translate(storyViewer24.fromXCell, storyViewer24.fromYCell);
                            StoryViewer.this.animateFromCell.drawAvatarOverlays(canvas);
                            canvas.restore();
                        }
                    }
                    canvas.restore();
                    if (StoryViewer.runOpenAnimationAfterLayout) {
                        return;
                    }
                    StoryViewer.this.startOpenAnimation();
                    boolean unused2 = StoryViewer.runOpenAnimationAfterLayout = false;
                    return;
                }
            }
            storyViewer5.containerView.setAlpha(storyViewer5.progressToOpen);
            StoryViewer storyViewer25 = StoryViewer.this;
            float f21 = (storyViewer25.progressToOpen * 0.1f) + 0.75f + (clamp * 0.15f);
            storyViewer25.containerView.setScaleX(f21);
            StoryViewer.this.containerView.setScaleY(f21);
            StoryViewer storyViewer26 = StoryViewer.this;
            storyViewer26.containerView.setTranslationY(storyViewer26.swipeToDismissOffset);
            StoryViewer storyViewer27 = StoryViewer.this;
            storyViewer27.containerView.setTranslationX(storyViewer27.swipeToDismissHorizontalOffset);
            super.dispatchDraw(canvas);
            if (StoryViewer.runOpenAnimationAfterLayout) {
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == 24 || keyEvent.getKeyCode() == 25) {
                StoryViewer.this.dispatchVolumeEvent(keyEvent);
                return true;
            }
            if (keyEvent.getKeyCode() != 4 || keyEvent.getAction() != 1) {
                return super.dispatchKeyEventPreIme(keyEvent);
            }
            StoryViewer.this.onAttachedBackPressed();
            return true;
        }

        /* JADX WARN: Removed duplicated region for block: B:59:0x016e  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x01e8 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:81:0x01e9  */
        @Override // android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            boolean z;
            boolean z2;
            PeerStoriesView currentPeerView = StoryViewer.this.storiesViewPager.getCurrentPeerView();
            if (currentPeerView != null && currentPeerView.checkTextSelectionEvent(motionEvent)) {
                return true;
            }
            if (StoryViewer.this.isLikesReactions && currentPeerView != null && currentPeerView.checkReactionEvent(motionEvent)) {
                return true;
            }
            if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                StoryViewer storyViewer = StoryViewer.this;
                storyViewer.inSwipeToDissmissMode = false;
                AndroidUtilities.cancelRunOnUIThread(storyViewer.longPressRunnable);
                StoryViewer storyViewer2 = StoryViewer.this;
                float f = storyViewer2.swipeToDismissHorizontalOffset;
                if (f != 0.0f) {
                    storyViewer2.swipeToDissmissBackAnimator = ValueAnimator.ofFloat(f, 0.0f);
                    StoryViewer.this.swipeToDissmissBackAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.StoryViewer$2$$ExternalSyntheticLambda1
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                            StoryViewer.2.this.lambda$dispatchTouchEvent$0(valueAnimator);
                        }
                    });
                    StoryViewer.this.swipeToDissmissBackAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.StoryViewer.2.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            StoryViewer storyViewer3 = StoryViewer.this;
                            storyViewer3.swipeToDismissHorizontalOffset = 0.0f;
                            storyViewer3.updateProgressToDismiss();
                        }
                    });
                    StoryViewer.this.swipeToDissmissBackAnimator.setDuration(250L);
                    StoryViewer.this.swipeToDissmissBackAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                    StoryViewer.this.swipeToDissmissBackAnimator.start();
                }
                StoryViewer storyViewer3 = StoryViewer.this;
                if (storyViewer3.progressToDismiss >= 0.3f) {
                    storyViewer3.close(true);
                }
                StoryViewer.this.setInTouchMode(false);
                StoryViewer.this.setLongPressed(false);
                z = true;
            } else {
                z = false;
            }
            if (motionEvent.getAction() == 0) {
                StoryViewer.this.swipeToReplyWaitingKeyboard = false;
                if (currentPeerView != null) {
                    currentPeerView.onActionDown(motionEvent);
                }
                StoryViewer.this.storiesViewPager.onTouchEvent(MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0));
            }
            StoryViewer storyViewer4 = StoryViewer.this;
            boolean z3 = (storyViewer4.keyboardVisible || storyViewer4.isClosed || StoryViewer.this.isRecording) ? false : true;
            StoryViewer storyViewer5 = StoryViewer.this;
            if (storyViewer5.selfStoriesViewsOffset == 0.0f && !storyViewer5.inSwipeToDissmissMode && storyViewer5.storiesViewPager.currentState == 1 && motionEvent.getAction() == 2 && z3) {
                float floatValue = ((Float) this.lastX.get(motionEvent.getPointerId(0), Float.valueOf(0.0f))).floatValue() - motionEvent.getX(0);
                if ((floatValue != 0.0f && !StoryViewer.this.storiesViewPager.canScroll(floatValue)) || StoryViewer.this.swipeToDismissHorizontalOffset != 0.0f) {
                    StoryViewer storyViewer6 = StoryViewer.this;
                    float f2 = storyViewer6.swipeToDismissHorizontalOffset;
                    if (f2 == 0.0f) {
                        storyViewer6.swipeToDismissHorizontalDirection = -floatValue;
                    }
                    if ((floatValue < 0.0f && storyViewer6.swipeToDismissHorizontalDirection > 0.0f) || (floatValue > 0.0f && storyViewer6.swipeToDismissHorizontalDirection < 0.0f)) {
                        floatValue *= 0.2f;
                    }
                    storyViewer6.swipeToDismissHorizontalOffset = f2 - floatValue;
                    storyViewer6.updateProgressToDismiss();
                    StoryViewer storyViewer7 = StoryViewer.this;
                    float f3 = storyViewer7.swipeToDismissHorizontalOffset;
                    if ((f3 > 0.0f && storyViewer7.swipeToDismissHorizontalDirection < 0.0f) || (f3 < 0.0f && storyViewer7.swipeToDismissHorizontalDirection > 0.0f)) {
                        storyViewer7.swipeToDismissHorizontalOffset = 0.0f;
                    }
                    z2 = true;
                    if (currentPeerView != null) {
                        StoryViewer storyViewer8 = StoryViewer.this;
                        if (storyViewer8.selfStoriesViewsOffset == 0.0f && !storyViewer8.inSwipeToDissmissMode && !storyViewer8.isCaption && !StoryViewer.this.isRecording) {
                            StoryViewer storyViewer9 = StoryViewer.this;
                            if (storyViewer9.storiesViewPager.currentState != 1) {
                                AndroidUtilities.getViewPositionInParent(currentPeerView.storyContainer, this, storyViewer9.pointPosition);
                                float[] fArr = StoryViewer.this.pointPosition;
                                motionEvent.offsetLocation(-fArr[0], -fArr[1]);
                                StoryViewer.this.storiesViewPager.getCurrentPeerView().checkPinchToZoom(motionEvent);
                                float[] fArr2 = StoryViewer.this.pointPosition;
                                motionEvent.offsetLocation(fArr2[0], fArr2[1]);
                            }
                        }
                    }
                    if (motionEvent.getAction() != 1 || motionEvent.getAction() == 3) {
                        this.lastX.clear();
                    } else {
                        for (int i = 0; i < motionEvent.getPointerCount(); i++) {
                            this.lastX.put(motionEvent.getPointerId(i), Float.valueOf(motionEvent.getX(i)));
                        }
                    }
                    if (!z2) {
                        return true;
                    }
                    boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
                    if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                        StoryViewer storyViewer10 = StoryViewer.this;
                        if (storyViewer10.selfStoriesViewsOffset != 0.0f && !storyViewer10.flingCalled && StoryViewer.this.realKeyboardHeight < AndroidUtilities.dp(20.0f)) {
                            StoryViewer storyViewer11 = StoryViewer.this;
                            storyViewer11.cancelSwipeToViews(storyViewer11.selfStoryViewsView.progressToOpen > 0.5f);
                        }
                        PeerStoriesView currentPeerView2 = StoryViewer.this.getCurrentPeerView();
                        if (currentPeerView2 != null) {
                            currentPeerView2.cancelTouch();
                        }
                    }
                    if (z) {
                        StoryViewer storyViewer12 = StoryViewer.this;
                        if (!storyViewer12.swipeToReplyWaitingKeyboard) {
                            storyViewer12.cancelSwipeToReply();
                        }
                    }
                    return dispatchTouchEvent || (StoryViewer.animationInProgress && StoryViewer.this.isInTouchMode);
                }
            }
            z2 = false;
            if (currentPeerView != null) {
            }
            if (motionEvent.getAction() != 1) {
            }
            this.lastX.clear();
            if (!z2) {
            }
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (view == StoryViewer.this.aspectRatioFrameLayout) {
                return false;
            }
            return super.drawChild(canvas, view, j);
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (StoryViewer.this.ATTACH_TO_FRAGMENT) {
                AndroidUtilities.requestAdjustResize(this.val$fragment.getParentActivity(), this.val$fragment.getClassGuid());
            }
            Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: org.telegram.ui.Stories.StoryViewer.2.3
                float[] position = new float[2];

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ boolean allowLayoutChanges() {
                    return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ boolean bottomOffsetAnimated() {
                    return Bulletin.Delegate.-CC.$default$bottomOffsetAnimated(this);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ boolean clipWithGradient(int i) {
                    return Bulletin.Delegate.-CC.$default$clipWithGradient(this, i);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public int getBottomOffset(int i) {
                    PeerStoriesView currentPeerView = StoryViewer.this.getCurrentPeerView();
                    if (currentPeerView == null) {
                        return 0;
                    }
                    AndroidUtilities.getViewPositionInParent(currentPeerView.storyContainer, StoryViewer.this.windowView, this.position);
                    return (int) (2.this.getMeasuredHeight() - (this.position[1] + currentPeerView.storyContainer.getMeasuredHeight()));
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ int getTopOffset(int i) {
                    return Bulletin.Delegate.-CC.$default$getTopOffset(this, i);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ void onBottomOffsetChange(float f) {
                    Bulletin.Delegate.-CC.$default$onBottomOffsetChange(this, f);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ void onHide(Bulletin bulletin) {
                    Bulletin.Delegate.-CC.$default$onHide(this, bulletin);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ void onShow(Bulletin bulletin) {
                    Bulletin.Delegate.-CC.$default$onShow(this, bulletin);
                }
            });
            NotificationCenter.getInstance(StoryViewer.this.currentAccount).addObserver(StoryViewer.this, NotificationCenter.storiesListUpdated);
            NotificationCenter.getInstance(StoryViewer.this.currentAccount).addObserver(StoryViewer.this, NotificationCenter.storiesUpdated);
            NotificationCenter.getInstance(StoryViewer.this.currentAccount).addObserver(StoryViewer.this, NotificationCenter.articleClosed);
            NotificationCenter.getInstance(StoryViewer.this.currentAccount).addObserver(StoryViewer.this, NotificationCenter.openArticle);
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            Bulletin.removeDelegate(this);
            NotificationCenter.getInstance(StoryViewer.this.currentAccount).removeObserver(StoryViewer.this, NotificationCenter.storiesListUpdated);
            NotificationCenter.getInstance(StoryViewer.this.currentAccount).removeObserver(StoryViewer.this, NotificationCenter.storiesUpdated);
            NotificationCenter.getInstance(StoryViewer.this.currentAccount).removeObserver(StoryViewer.this, NotificationCenter.articleClosed);
            NotificationCenter.getInstance(StoryViewer.this.currentAccount).removeObserver(StoryViewer.this, NotificationCenter.openArticle);
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x0049  */
        /* JADX WARN: Removed duplicated region for block: B:17:0x0066  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0078  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x00a1  */
        @Override // android.view.ViewGroup
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            VideoPlayerHolder videoPlayerHolder;
            PeerStoriesView.VideoPlayerSharedScope videoPlayerSharedScope;
            PeerStoriesView currentPeerView;
            PeerStoriesView.StoryItemHolder storyItemHolder;
            TL_stories.StoryItem storyItem;
            TLRPC.MessageMedia messageMedia;
            TLRPC.Document document;
            boolean z;
            StoryViewer storyViewer;
            boolean z2;
            StoryViewer storyViewer2;
            StoryViewer storyViewer3;
            if (motionEvent.getAction() == 0 && StoryViewer.this.progressToOpen == 1.0f) {
                float x = motionEvent.getX();
                this.lastTouchX = x;
                this.startX = x;
                this.startY = motionEvent.getY();
                StoryViewer storyViewer4 = StoryViewer.this;
                storyViewer4.verticalScrollDetected = false;
                if (!storyViewer4.isRecording) {
                    StoryViewer storyViewer5 = StoryViewer.this;
                    if (!storyViewer5.findClickableView(storyViewer5.windowView, motionEvent.getX(), motionEvent.getY(), false)) {
                        z = true;
                        storyViewer4.allowIntercept = z;
                        storyViewer = StoryViewer.this;
                        if (!storyViewer.isRecording) {
                            StoryViewer storyViewer6 = StoryViewer.this;
                            if (!storyViewer6.findClickableView(storyViewer6.windowView, motionEvent.getX(), motionEvent.getY(), true)) {
                                z2 = true;
                                storyViewer.allowSwipeToDissmiss = z2;
                                StoryViewer storyViewer7 = StoryViewer.this;
                                storyViewer7.setInTouchMode((storyViewer7.allowIntercept || storyViewer7.isCaptionPartVisible) ? false : true);
                                storyViewer2 = StoryViewer.this;
                                if (storyViewer2.allowIntercept && !storyViewer2.isRecording && StoryViewer.this.isCaptionPartVisible) {
                                    StoryViewer.this.delayedTapRunnable = new Runnable() { // from class: org.telegram.ui.Stories.StoryViewer$2$$ExternalSyntheticLambda0
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            StoryViewer.2.this.lambda$onInterceptTouchEvent$1();
                                        }
                                    };
                                    AndroidUtilities.runOnUIThread(StoryViewer.this.delayedTapRunnable, 150L);
                                }
                                storyViewer3 = StoryViewer.this;
                                if (storyViewer3.allowIntercept && !storyViewer3.keyboardVisible && !storyViewer3.isRecording && !StoryViewer.this.isInTextSelectionMode) {
                                    AndroidUtilities.runOnUIThread(StoryViewer.this.longPressRunnable, 400L);
                                }
                            }
                        }
                        z2 = false;
                        storyViewer.allowSwipeToDissmiss = z2;
                        StoryViewer storyViewer72 = StoryViewer.this;
                        storyViewer72.setInTouchMode((storyViewer72.allowIntercept || storyViewer72.isCaptionPartVisible) ? false : true);
                        storyViewer2 = StoryViewer.this;
                        if (storyViewer2.allowIntercept) {
                            StoryViewer.this.delayedTapRunnable = new Runnable() { // from class: org.telegram.ui.Stories.StoryViewer$2$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    StoryViewer.2.this.lambda$onInterceptTouchEvent$1();
                                }
                            };
                            AndroidUtilities.runOnUIThread(StoryViewer.this.delayedTapRunnable, 150L);
                        }
                        storyViewer3 = StoryViewer.this;
                        if (storyViewer3.allowIntercept) {
                            AndroidUtilities.runOnUIThread(StoryViewer.this.longPressRunnable, 400L);
                        }
                    }
                }
                z = false;
                storyViewer4.allowIntercept = z;
                storyViewer = StoryViewer.this;
                if (!storyViewer.isRecording) {
                }
                z2 = false;
                storyViewer.allowSwipeToDissmiss = z2;
                StoryViewer storyViewer722 = StoryViewer.this;
                storyViewer722.setInTouchMode((storyViewer722.allowIntercept || storyViewer722.isCaptionPartVisible) ? false : true);
                storyViewer2 = StoryViewer.this;
                if (storyViewer2.allowIntercept) {
                }
                storyViewer3 = StoryViewer.this;
                if (storyViewer3.allowIntercept) {
                }
            } else if (motionEvent.getAction() == 2) {
                float abs = Math.abs(this.startY - motionEvent.getY());
                float abs2 = Math.abs(this.startX - motionEvent.getX());
                StoryViewer storyViewer8 = StoryViewer.this;
                if (storyViewer8.isLongpressed && storyViewer8.inSeekingMode && !storyViewer8.isInPinchToZoom) {
                    StoryViewer storyViewer9 = StoryViewer.this;
                    if (!storyViewer9.inSwipeToDissmissMode && (videoPlayerSharedScope = storyViewer9.currentPlayerScope) != null && videoPlayerSharedScope.player != null && (currentPeerView = storyViewer9.storiesViewPager.getCurrentPeerView()) != null && (storyItemHolder = currentPeerView.currentStory) != null && storyItemHolder.uploadingStory == null && storyItemHolder.isVideo()) {
                        long j = currentPeerView.videoDuration;
                        if (j <= 0 && (storyItem = currentPeerView.currentStory.storyItem) != null && (messageMedia = storyItem.media) != null && (document = messageMedia.document) != null) {
                            j = (long) (MessageObject.getDocumentDuration(document) * 1000.0d);
                        }
                        if (j > 0) {
                            float x2 = motionEvent.getX();
                            VideoPlayerHolder videoPlayerHolder2 = StoryViewer.this.currentPlayerScope.player;
                            if (((int) (videoPlayerHolder2.seek((x2 - this.lastTouchX) / AndroidUtilities.dp(220.0f), j) * 10.0f)) != ((int) (videoPlayerHolder2.currentSeek * 10.0f))) {
                                try {
                                    currentPeerView.performHapticFeedback(9, 1);
                                } catch (Exception unused) {
                                }
                            }
                            currentPeerView.storyContainer.invalidate();
                            this.lastTouchX = x2;
                        }
                    }
                }
                if (abs > abs2) {
                    StoryViewer storyViewer10 = StoryViewer.this;
                    if (!storyViewer10.inSeekingMode && !storyViewer10.verticalScrollDetected && abs > AndroidUtilities.touchSlop * 2.0f) {
                        storyViewer10.verticalScrollDetected = true;
                    }
                }
                StoryViewer storyViewer11 = StoryViewer.this;
                if (!storyViewer11.inSwipeToDissmissMode && !storyViewer11.inSeekingMode && !storyViewer11.keyboardVisible && storyViewer11.allowSwipeToDissmiss) {
                    if (abs > abs2 && abs > AndroidUtilities.touchSlop * 2.0f) {
                        storyViewer11.inSwipeToDissmissMode = true;
                        PeerStoriesView currentPeerView2 = storyViewer11.storiesViewPager.getCurrentPeerView();
                        if (currentPeerView2 != null) {
                            currentPeerView2.cancelTextSelection();
                        }
                        boolean z3 = currentPeerView2 != null && currentPeerView2.viewsAllowed();
                        StoryViewer storyViewer12 = StoryViewer.this;
                        storyViewer12.allowSwipeToReply = (z3 || currentPeerView2 == null || currentPeerView2.isChannel || currentPeerView2.isPremiumBlocked || storyViewer12.storiesIntro != null) ? false : true;
                        StoryViewer storyViewer13 = StoryViewer.this;
                        storyViewer13.allowSelfStoriesView = z3 && !currentPeerView2.unsupported && currentPeerView2.currentStory.storyItem != null && storyViewer13.storiesIntro == null;
                        StoryViewer storyViewer14 = StoryViewer.this;
                        if (storyViewer14.allowSelfStoriesView && this.keyboardHeight != 0) {
                            storyViewer14.allowSelfStoriesView = false;
                        }
                        if (storyViewer14.allowSelfStoriesView) {
                            storyViewer14.checkSelfStoriesView();
                        }
                        StoryViewer storyViewer15 = StoryViewer.this;
                        storyViewer15.swipeToReplyOffset = 0.0f;
                        if (storyViewer15.delayedTapRunnable != null) {
                            AndroidUtilities.cancelRunOnUIThread(StoryViewer.this.delayedTapRunnable);
                            StoryViewer.this.delayedTapRunnable.run();
                            StoryViewer.this.delayedTapRunnable = null;
                        }
                        AndroidUtilities.cancelRunOnUIThread(StoryViewer.this.longPressRunnable);
                    }
                    StoryViewer.this.layoutAndFindView();
                }
            } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                AndroidUtilities.cancelRunOnUIThread(StoryViewer.this.longPressRunnable);
                if (StoryViewer.this.delayedTapRunnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(StoryViewer.this.delayedTapRunnable);
                    StoryViewer.this.delayedTapRunnable = null;
                }
                StoryViewer.this.setInTouchMode(false);
                StoryViewer storyViewer16 = StoryViewer.this;
                storyViewer16.verticalScrollDetected = false;
                storyViewer16.inSeekingMode = false;
                PeerStoriesView.VideoPlayerSharedScope videoPlayerSharedScope2 = storyViewer16.currentPlayerScope;
                if (videoPlayerSharedScope2 != null && (videoPlayerHolder = videoPlayerSharedScope2.player) != null) {
                    videoPlayerHolder.setSeeking(false);
                }
            }
            StoryViewer storyViewer17 = StoryViewer.this;
            SelfStoryViewsView selfStoryViewsView = storyViewer17.selfStoryViewsView;
            boolean z4 = selfStoryViewsView != null && selfStoryViewsView.progressToOpen == 1.0f;
            if (!storyViewer17.inSwipeToDissmissMode && !z4) {
                storyViewer17.gestureDetector.onTouchEvent(motionEvent);
            }
            return StoryViewer.this.inSwipeToDissmissMode || super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            ((FrameLayout.LayoutParams) StoryViewer.this.volumeControl.getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight - AndroidUtilities.dp(2.0f);
            StoryViewer.this.volumeControl.getLayoutParams().height = AndroidUtilities.dp(2.0f);
            super.onMeasure(i, i2);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                StoryViewer storyViewer = StoryViewer.this;
                storyViewer.inSwipeToDissmissMode = false;
                storyViewer.setInTouchMode(false);
                StoryViewer storyViewer2 = StoryViewer.this;
                if (storyViewer2.progressToDismiss >= 1.0f) {
                    storyViewer2.close(true);
                } else if (!storyViewer2.isClosed) {
                    StoryViewer storyViewer3 = StoryViewer.this;
                    storyViewer3.swipeToDissmissBackAnimator = ValueAnimator.ofFloat(storyViewer3.swipeToDismissOffset, 0.0f);
                    StoryViewer.this.swipeToDissmissBackAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.StoryViewer$2$$ExternalSyntheticLambda2
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                            StoryViewer.2.this.lambda$onTouchEvent$2(valueAnimator);
                        }
                    });
                    StoryViewer.this.swipeToDissmissBackAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.StoryViewer.2.2
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            StoryViewer storyViewer4 = StoryViewer.this;
                            storyViewer4.swipeToDismissOffset = 0.0f;
                            storyViewer4.swipeToReplyOffset = 0.0f;
                            storyViewer4.updateProgressToDismiss();
                        }
                    });
                    StoryViewer.this.swipeToDissmissBackAnimator.setDuration(150L);
                    StoryViewer.this.swipeToDissmissBackAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                    StoryViewer.this.swipeToDissmissBackAnimator.start();
                }
            }
            StoryViewer storyViewer4 = StoryViewer.this;
            if (!storyViewer4.inSwipeToDissmissMode && !storyViewer4.keyboardVisible && storyViewer4.swipeToReplyOffset == 0.0f && ((storyViewer4.selfStoriesViewsOffset == 0.0f || (!storyViewer4.allowIntercept && !storyViewer4.verticalScrollDetected)) && !storyViewer4.isInTextSelectionMode)) {
                return false;
            }
            StoryViewer.this.gestureDetector.onTouchEvent(motionEvent);
            return true;
        }

        @Override // android.view.ViewGroup, android.view.ViewParent
        public void requestDisallowInterceptTouchEvent(boolean z) {
            super.requestDisallowInterceptTouchEvent(z);
            StoryViewer.this.allowIntercept = false;
        }
    }

    class 5 implements PeerStoriesView.Delegate {
        final /* synthetic */ ArrayList val$peerIds;
        final /* synthetic */ StoriesController.StoriesList val$storiesList;

        5(StoriesController.StoriesList storiesList, ArrayList arrayList) {
            this.val$storiesList = storiesList;
            this.val$peerIds = arrayList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$preparePlayer$2(VideoPlayerHolder videoPlayerHolder) {
            FrameLayout frameLayout;
            PeerStoriesView.VideoPlayerSharedScope videoPlayerSharedScope;
            PeerStoriesView currentPeerView = StoryViewer.this.storiesViewPager.getCurrentPeerView();
            if (currentPeerView == null || (frameLayout = currentPeerView.storyContainer) == null || (videoPlayerSharedScope = StoryViewer.this.currentPlayerScope) == null || videoPlayerSharedScope.player != videoPlayerHolder) {
                return;
            }
            frameLayout.invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$switchToNextAndRemoveCurrentPeer$0(StoriesController.StoriesList storiesList, ArrayList arrayList) {
            StoryViewer storyViewer = StoryViewer.this;
            storyViewer.storiesViewPager.setDays(storiesList.dialogId, arrayList, storyViewer.currentAccount);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$switchToNextAndRemoveCurrentPeer$1(ArrayList arrayList, int i) {
            StoryViewer storyViewer = StoryViewer.this;
            storyViewer.storiesViewPager.setPeerIds(arrayList, storyViewer.currentAccount, i);
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public int getKeyboardHeight() {
            return StoryViewer.this.realKeyboardHeight;
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public float getProgressToDismiss() {
            return StoryViewer.this.progressToDismiss;
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public boolean isClosed() {
            return StoryViewer.this.isClosed;
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void onPeerSelected(long j, int i) {
            StoryViewer storyViewer = StoryViewer.this;
            if (storyViewer.lastPosition == i && storyViewer.lastDialogId == j) {
                return;
            }
            storyViewer.lastDialogId = j;
            storyViewer.lastPosition = i;
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void preparePlayer(ArrayList arrayList, ArrayList arrayList2) {
            if (SharedConfig.deviceIsHigh() && SharedConfig.allowPreparingHevcPlayers() && !StoryViewer.this.isClosed) {
                for (int i = 0; i < StoryViewer.this.preparedPlayers.size(); i++) {
                    for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                        if (((Uri) arrayList2.get(i2)).equals(((VideoPlayerHolder) StoryViewer.this.preparedPlayers.get(i)).uri)) {
                            arrayList2.remove(i2);
                        }
                    }
                }
                for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                    Uri uri = (Uri) arrayList2.get(i3);
                    StoryViewer storyViewer = StoryViewer.this;
                    final VideoPlayerHolder videoPlayerHolder = storyViewer.new VideoPlayerHolder(storyViewer.surfaceView, StoryViewer.this.textureView);
                    videoPlayerHolder.setOnSeekUpdate(new Runnable() { // from class: org.telegram.ui.Stories.StoryViewer$5$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            StoryViewer.5.this.lambda$preparePlayer$2(videoPlayerHolder);
                        }
                    });
                    videoPlayerHolder.uri = uri;
                    TLRPC.Document document = (TLRPC.Document) arrayList.get(i3);
                    videoPlayerHolder.document = document;
                    FileStreamLoadOperation.setPriorityForDocument(document, 0);
                    videoPlayerHolder.preparePlayer(uri, StoryViewer.isInSilentMode, StoryViewer.currentSpeed);
                    StoryViewer.this.preparedPlayers.add(videoPlayerHolder);
                    if (StoryViewer.this.preparedPlayers.size() > 2) {
                        ((VideoPlayerHolder) StoryViewer.this.preparedPlayers.remove(0)).release(null);
                    }
                }
            }
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public boolean releasePlayer(Runnable runnable) {
            VideoPlayerHolder videoPlayerHolder = StoryViewer.this.playerHolder;
            if (videoPlayerHolder == null) {
                return false;
            }
            boolean release = videoPlayerHolder.release(runnable);
            StoryViewer.this.playerHolder = null;
            return release;
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void requestAdjust(boolean z) {
            StoryViewer.this.requestAdjust(z);
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x0192  */
        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void requestPlayer(TLRPC.Document document, Uri uri, long j, PeerStoriesView.VideoPlayerSharedScope videoPlayerSharedScope) {
            String str;
            long j2;
            StoryViewer storyViewer;
            StoryViewer storyViewer2;
            VideoPlayerHolder videoPlayerHolder;
            if (!StoryViewer.this.isClosed) {
                StoryViewer storyViewer3 = StoryViewer.this;
                if (storyViewer3.progressToOpen >= 0.9f) {
                    Uri uri2 = storyViewer3.lastUri;
                    boolean equals = Objects.equals(uri2 == null ? null : uri2.getAuthority(), uri == null ? null : uri.getAuthority());
                    if (equals && (videoPlayerHolder = (storyViewer2 = StoryViewer.this).playerHolder) != null) {
                        if (equals) {
                            storyViewer2.currentPlayerScope = videoPlayerSharedScope;
                            videoPlayerSharedScope.player = videoPlayerHolder;
                            videoPlayerHolder.setSpeed(StoryViewer.currentSpeed);
                            StoryViewer storyViewer4 = StoryViewer.this;
                            PeerStoriesView.VideoPlayerSharedScope videoPlayerSharedScope2 = storyViewer4.currentPlayerScope;
                            videoPlayerSharedScope2.firstFrameRendered = storyViewer4.playerHolder.firstFrameRendered;
                            videoPlayerSharedScope2.renderView = storyViewer4.aspectRatioFrameLayout;
                            videoPlayerSharedScope2.textureView = storyViewer4.textureView;
                            StoryViewer storyViewer5 = StoryViewer.this;
                            storyViewer5.currentPlayerScope.surfaceView = storyViewer5.surfaceView;
                            str = "StoryViewer requestPlayer: same url";
                            FileLog.d(str);
                        }
                        storyViewer = StoryViewer.this;
                        if (storyViewer.USE_SURFACE_VIEW) {
                        }
                        StoryViewer.this.playerSavedPosition = 0L;
                        StoryViewer.this.updatePlayingMode();
                        return;
                    }
                    StoryViewer storyViewer6 = StoryViewer.this;
                    storyViewer6.lastUri = uri;
                    VideoPlayerHolder videoPlayerHolder2 = storyViewer6.playerHolder;
                    if (videoPlayerHolder2 != null) {
                        videoPlayerHolder2.release(null);
                        StoryViewer.this.playerHolder = null;
                    }
                    PeerStoriesView.VideoPlayerSharedScope videoPlayerSharedScope3 = StoryViewer.this.currentPlayerScope;
                    if (videoPlayerSharedScope3 != null) {
                        videoPlayerSharedScope3.player = null;
                        videoPlayerSharedScope3.firstFrameRendered = false;
                        videoPlayerSharedScope3.renderView = null;
                        videoPlayerSharedScope3.textureView = null;
                        videoPlayerSharedScope3.surfaceView = null;
                        videoPlayerSharedScope3.invalidate();
                        StoryViewer.this.currentPlayerScope = null;
                    }
                    if (uri == null) {
                        str = "StoryViewer requestPlayer: url is null (1)";
                        FileLog.d(str);
                        storyViewer = StoryViewer.this;
                        if (storyViewer.USE_SURFACE_VIEW) {
                        }
                        StoryViewer.this.playerSavedPosition = 0L;
                        StoryViewer.this.updatePlayingMode();
                        return;
                    }
                    StoryViewer.this.currentPlayerScope = videoPlayerSharedScope;
                    int i = 0;
                    while (true) {
                        if (i >= StoryViewer.this.preparedPlayers.size()) {
                            break;
                        }
                        if (((VideoPlayerHolder) StoryViewer.this.preparedPlayers.get(i)).uri.equals(uri)) {
                            StoryViewer storyViewer7 = StoryViewer.this;
                            storyViewer7.playerHolder = (VideoPlayerHolder) storyViewer7.preparedPlayers.remove(i);
                            break;
                        }
                        i++;
                    }
                    StoryViewer storyViewer8 = StoryViewer.this;
                    if (storyViewer8.playerHolder == null) {
                        storyViewer8.playerHolder = storyViewer8.new VideoPlayerHolder(storyViewer8.surfaceView, StoryViewer.this.textureView);
                        StoryViewer.this.playerHolder.document = document;
                    }
                    VideoPlayerHolder videoPlayerHolder3 = StoryViewer.this.playerHolder;
                    videoPlayerHolder3.uri = uri;
                    videoPlayerHolder3.setSpeed(StoryViewer.currentSpeed);
                    StoryViewer storyViewer9 = StoryViewer.this;
                    PeerStoriesView.VideoPlayerSharedScope videoPlayerSharedScope4 = storyViewer9.currentPlayerScope;
                    videoPlayerSharedScope4.player = storyViewer9.playerHolder;
                    videoPlayerSharedScope4.firstFrameRendered = false;
                    videoPlayerSharedScope4.renderView = storyViewer9.aspectRatioFrameLayout;
                    videoPlayerSharedScope4.textureView = storyViewer9.textureView;
                    StoryViewer storyViewer10 = StoryViewer.this;
                    storyViewer10.currentPlayerScope.surfaceView = storyViewer10.surfaceView;
                    FileStreamLoadOperation.setPriorityForDocument(StoryViewer.this.playerHolder.document, 3);
                    FileLoader.getInstance(StoryViewer.this.currentAccount).changePriority(3, StoryViewer.this.playerHolder.document, null, null, null, null, null);
                    if (j != 0 || StoryViewer.this.playerSavedPosition == 0) {
                        j2 = j;
                    } else {
                        long j3 = StoryViewer.this.playerSavedPosition;
                        StoryViewer.this.currentPlayerScope.firstFrameRendered = true;
                        j2 = j3;
                    }
                    FileLog.d("StoryViewer requestPlayer: currentPlayerScope.player start " + uri);
                    StoryViewer storyViewer11 = StoryViewer.this;
                    storyViewer11.currentPlayerScope.player.start(false, storyViewer11.isPaused(), uri, j2, StoryViewer.isInSilentMode, StoryViewer.currentSpeed);
                    StoryViewer.this.currentPlayerScope.invalidate();
                    storyViewer = StoryViewer.this;
                    if (storyViewer.USE_SURFACE_VIEW) {
                        SurfaceView surfaceView = storyViewer.surfaceView;
                        if (uri == null) {
                            surfaceView.setVisibility(4);
                        } else {
                            surfaceView.setVisibility(0);
                        }
                    }
                    StoryViewer.this.playerSavedPosition = 0L;
                    StoryViewer.this.updatePlayingMode();
                    return;
                }
            }
            FileLog.d("StoryViewer requestPlayer ignored, because closed: " + StoryViewer.this.isClosed + ", " + StoryViewer.this.progressToOpen);
            videoPlayerSharedScope.firstFrameRendered = false;
            videoPlayerSharedScope.player = null;
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setAllowTouchesByViewPager(boolean z) {
            StoryViewer storyViewer = StoryViewer.this;
            storyViewer.allowTouchesByViewpager = storyViewer.allowTouchesByViewpager;
            StoryViewer.this.updatePlayingMode();
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setBulletinIsVisible(boolean z) {
            StoryViewer.this.isBulletinVisible = z;
            StoryViewer.this.updatePlayingMode();
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setHideEnterViewProgress(float f) {
            if (StoryViewer.this.hideEnterViewProgress != f) {
                StoryViewer.this.hideEnterViewProgress = f;
                StoryViewer.this.containerView.invalidate();
            }
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setIsCaption(boolean z) {
            StoryViewer.this.isCaption = z;
            StoryViewer.this.updatePlayingMode();
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setIsCaptionPartVisible(boolean z) {
            StoryViewer.this.isCaptionPartVisible = z;
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setIsHintVisible(boolean z) {
            StoryViewer.this.isHintVisible = z;
            StoryViewer.this.updatePlayingMode();
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setIsInPinchToZoom(boolean z) {
            VideoPlayerHolder videoPlayerHolder;
            if (!StoryViewer.this.isInPinchToZoom && z) {
                StoryViewer storyViewer = StoryViewer.this;
                if (storyViewer.inSeekingMode) {
                    storyViewer.inSeekingMode = false;
                    PeerStoriesView.VideoPlayerSharedScope videoPlayerSharedScope = storyViewer.currentPlayerScope;
                    if (videoPlayerSharedScope != null && (videoPlayerHolder = videoPlayerSharedScope.player) != null) {
                        videoPlayerHolder.setSeeking(false);
                    }
                    PeerStoriesView currentPeerView = StoryViewer.this.getCurrentPeerView();
                    if (currentPeerView != null) {
                        currentPeerView.invalidate();
                    }
                }
            }
            StoryViewer.this.isInPinchToZoom = z;
            StoryViewer.this.updatePlayingMode();
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setIsInSelectionMode(boolean z) {
            StoryViewer.this.isInTextSelectionMode = z;
            StoryViewer.this.updatePlayingMode();
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setIsLikesReaction(boolean z) {
            StoryViewer.this.isLikesReactions = z;
            StoryViewer.this.updatePlayingMode();
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setIsRecording(boolean z) {
            StoryViewer.this.isRecording = z;
            StoryViewer.this.updatePlayingMode();
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setIsSwiping(boolean z) {
            StoryViewer.this.isSwiping = z;
            StoryViewer.this.updatePlayingMode();
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setIsWaiting(boolean z) {
            StoryViewer.this.isWaiting = z;
            StoryViewer.this.updatePlayingMode();
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setKeyboardVisible(boolean z) {
            StoryViewer storyViewer = StoryViewer.this;
            if (storyViewer.keyboardVisible != z) {
                storyViewer.keyboardVisible = z;
                storyViewer.updatePlayingMode();
            }
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setPopupIsVisible(boolean z) {
            StoryViewer.this.isPopupVisible = z;
            StoryViewer.this.updatePlayingMode();
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void setTranslating(boolean z) {
            StoryViewer storyViewer = StoryViewer.this;
            storyViewer.isTranslating = z;
            storyViewer.updatePlayingMode();
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void shouldSwitchToNext() {
            if (StoryViewer.this.storiesViewPager.getCurrentPeerView().switchToNext(true) || StoryViewer.this.storiesViewPager.switchToNext(true)) {
                return;
            }
            StoryViewer.this.close(true);
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void showDialog(Dialog dialog) {
            StoryViewer.this.showDialog(dialog);
        }

        @Override // org.telegram.ui.Stories.PeerStoriesView.Delegate
        public void switchToNextAndRemoveCurrentPeer() {
            if (this.val$storiesList == null) {
                final ArrayList arrayList = new ArrayList(this.val$peerIds);
                final int indexOf = arrayList.indexOf(Long.valueOf(StoryViewer.this.storiesViewPager.getCurrentPeerView().getCurrentPeer()));
                if (indexOf >= 0) {
                    arrayList.remove(indexOf);
                    if (StoryViewer.this.storiesViewPager.switchToNext(true)) {
                        StoryViewer.this.storiesViewPager.onNextIdle(new Runnable() { // from class: org.telegram.ui.Stories.StoryViewer$5$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                StoryViewer.5.this.lambda$switchToNextAndRemoveCurrentPeer$1(arrayList, indexOf);
                            }
                        });
                        return;
                    }
                    StoryViewer.this.close(false);
                    return;
                }
                StoryViewer.this.close(false);
            }
            if (StoryViewer.this.storiesViewPager.days == null) {
                return;
            }
            final ArrayList arrayList2 = new ArrayList(StoryViewer.this.storiesViewPager.days);
            int indexOf2 = StoryViewer.this.storiesViewPager.getCurrentPeerView() == null ? -1 : arrayList2.indexOf(StoryViewer.this.storiesViewPager.getCurrentPeerView().getCurrentDay());
            if (indexOf2 >= 0) {
                arrayList2.remove(indexOf2);
                if (StoryViewer.this.storiesViewPager.switchToNext(true)) {
                    StoriesViewPager storiesViewPager = StoryViewer.this.storiesViewPager;
                    final StoriesController.StoriesList storiesList = this.val$storiesList;
                    storiesViewPager.onNextIdle(new Runnable() { // from class: org.telegram.ui.Stories.StoryViewer$5$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            StoryViewer.5.this.lambda$switchToNextAndRemoveCurrentPeer$0(storiesList, arrayList2);
                        }
                    });
                    return;
                }
                StoryViewer.this.close(false);
                return;
            }
            StoryViewer.this.close(false);
        }
    }

    public interface HolderClip {
        void clip(Canvas canvas, RectF rectF, float f, boolean z);
    }

    public interface HolderDrawAbove {
        void draw(Canvas canvas, RectF rectF, float f, boolean z);
    }

    public interface PlaceProvider {

        public abstract /* synthetic */ class -CC {
            public static void $default$loadNext(PlaceProvider placeProvider, boolean z) {
            }
        }

        boolean findView(long j, int i, int i2, int i3, TransitionViewHolder transitionViewHolder);

        void loadNext(boolean z);

        void preLayout(long j, int i, Runnable runnable);
    }

    public static class TransitionViewHolder {
        public float alpha = 1.0f;
        public ImageReceiver avatarImage;
        public Paint bgPaint;
        public boolean checkParentScale;
        public float clipBottom;
        public View clipParent;
        public float clipTop;
        public ImageReceiver crossfadeToAvatarImage;
        public HolderDrawAbove drawAbove;
        public HolderClip drawClip;
        public StoriesUtilities.AvatarStoryParams params;
        public RadialProgress radialProgressUpload;
        public int storyId;
        public ImageReceiver storyImage;
        public View view;

        public void clear() {
            this.view = null;
            this.params = null;
            this.avatarImage = null;
            this.storyImage = null;
            this.drawAbove = null;
            this.drawClip = null;
            this.clipParent = null;
            this.radialProgressUpload = null;
            this.crossfadeToAvatarImage = null;
            this.clipTop = 0.0f;
            this.clipBottom = 0.0f;
            this.storyId = 0;
            this.bgPaint = null;
            this.alpha = 1.0f;
        }

        public Integer getAvatarImageRoundRadius() {
            View view;
            if (this.avatarImage != null) {
                return Integer.valueOf((int) (this.avatarImage.getRoundRadius()[0] * ((!this.checkParentScale || (view = this.view) == null || view.getParent() == null) ? 1.0f : ((ViewGroup) this.view.getParent()).getScaleY())));
            }
            return null;
        }
    }

    public class VideoPlayerHolder extends VideoPlayerHolderBase {
        boolean logBuffering;

        public VideoPlayerHolder(SurfaceView surfaceView, TextureView textureView) {
            if (StoryViewer.this.USE_SURFACE_VIEW) {
                with(surfaceView);
            } else {
                with(textureView);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStateChanged$0() {
            PeerStoriesView currentPeerView = StoryViewer.this.getCurrentPeerView();
            if (currentPeerView == null || currentPeerView.currentStory.storyItem == null) {
                return;
            }
            FileLog.d("StoryViewer displayed story buffering dialogId=" + currentPeerView.getCurrentPeer() + " storyId=" + currentPeerView.currentStory.storyItem.id);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStateChanged$1() {
            PeerStoriesView currentPeerView = StoryViewer.this.getCurrentPeerView();
            if (currentPeerView == null || currentPeerView.currentStory.storyItem == null) {
                return;
            }
            FileLog.d("StoryViewer displayed story playing dialogId=" + currentPeerView.getCurrentPeer() + " storyId=" + currentPeerView.currentStory.storyItem.id);
        }

        @Override // org.telegram.messenger.video.VideoPlayerHolderBase
        public boolean needRepeat() {
            return StoryViewer.this.isCaptionPartVisible;
        }

        @Override // org.telegram.messenger.video.VideoPlayerHolderBase
        public void onRenderedFirstFrame() {
            PeerStoriesView.VideoPlayerSharedScope videoPlayerSharedScope = StoryViewer.this.currentPlayerScope;
            if (videoPlayerSharedScope == null) {
                return;
            }
            videoPlayerSharedScope.firstFrameRendered = true;
            this.firstFrameRendered = true;
            videoPlayerSharedScope.invalidate();
            if (!this.paused || StoryViewer.this.surfaceView == null) {
                return;
            }
            prepareStub();
        }

        @Override // org.telegram.messenger.video.VideoPlayerHolderBase
        public void onStateChanged(boolean z, int i) {
            if (i == 3 || i == 2) {
                if (this.firstFrameRendered && i == 2) {
                    this.logBuffering = true;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoryViewer$VideoPlayerHolder$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            StoryViewer.VideoPlayerHolder.this.lambda$onStateChanged$0();
                        }
                    });
                }
                if (this.logBuffering && i == 3) {
                    this.logBuffering = false;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoryViewer$VideoPlayerHolder$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            StoryViewer.VideoPlayerHolder.this.lambda$onStateChanged$1();
                        }
                    });
                }
            }
        }
    }

    public StoryViewer(BaseFragment baseFragment) {
        int i = J;
        J = i + 1;
        this.j = i;
        this.inputBackgroundPaint = new Paint(1);
        this.fragment = baseFragment;
    }

    private void checkInSilentMode() {
        if (checkSilentMode) {
            checkSilentMode = false;
            isInSilentMode = ((AudioManager) this.windowView.getContext().getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND)).getRingerMode() != 2;
        }
    }

    public static void closeGlobalInstances() {
        for (int i = 0; i < globalInstances.size(); i++) {
            ((StoryViewer) globalInstances.get(i)).close(false);
        }
        globalInstances.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchVolumeEvent(KeyEvent keyEvent) {
        if (isInSilentMode) {
            toggleSilentMode();
            return;
        }
        PeerStoriesView currentPeerView = this.storiesViewPager.getCurrentPeerView();
        if (currentPeerView == null || currentPeerView.currentStory.hasSound() || !currentPeerView.currentStory.isVideo()) {
            this.volumeControl.onKeyDown(keyEvent.getKeyCode(), keyEvent);
        } else {
            currentPeerView.showNoSoundHint(true);
        }
    }

    private long draftHash(long j, TL_stories.StoryItem storyItem) {
        return j + (j >> 16) + (storyItem.id << 16);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean findClickableView(FrameLayout frameLayout, float f, float f2, boolean z) {
        ChatActivityEnterView chatActivityEnterView;
        ChatActivityEnterView chatActivityEnterView2;
        if (frameLayout == null) {
            return false;
        }
        if (this.isPopupVisible) {
            return true;
        }
        if (this.selfStoryViewsView != null && this.selfStoriesViewsOffset != 0.0f) {
            return true;
        }
        PeerStoriesView currentPeerView = this.storiesViewPager.getCurrentPeerView();
        if (currentPeerView != null) {
            if (currentPeerView.findClickableView(currentPeerView, ((f - this.containerView.getX()) - this.storiesViewPager.getX()) - currentPeerView.getX(), ((f2 - this.containerView.getY()) - this.storiesViewPager.getY()) - currentPeerView.getY(), z)) {
                return true;
            }
            if (currentPeerView.keyboardVisible) {
                return false;
            }
        }
        if (z) {
            return false;
        }
        if (currentPeerView != null && (chatActivityEnterView2 = currentPeerView.chatActivityEnterView) != null && chatActivityEnterView2.getVisibility() == 0 && f2 > this.containerView.getY() + this.storiesViewPager.getY() + currentPeerView.getY() + currentPeerView.chatActivityEnterView.getY()) {
            return true;
        }
        if ((currentPeerView == null || (chatActivityEnterView = currentPeerView.chatActivityEnterView) == null || !chatActivityEnterView.isRecordingAudioVideo()) && this.storiesIntro == null) {
            return AndroidUtilities.findClickableView(frameLayout, f, f2, currentPeerView);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getBlackoutAlpha() {
        return this.progressToOpen * (((1.0f - this.progressToDismiss) * 0.5f) + 0.5f);
    }

    public static boolean isShowingImage(MessageObject messageObject) {
        if (lastStoryItem != null) {
            return (messageObject.type == 23 || messageObject.isWebpage()) && !runOpenAnimationAfterLayout && lastStoryItem.messageId == messageObject.getId() && lastStoryItem.messageType != 3;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelSwipeToReply$4(ValueAnimator valueAnimator) {
        this.swipeToReplyOffset = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.swipeToReplyProgress = Utilities.clamp(this.swipeToReplyOffset / AndroidUtilities.dp(200.0f), 1.0f, 0.0f);
        StoriesViewPager storiesViewPager = this.storiesViewPager;
        PeerStoriesView currentPeerView = storiesViewPager == null ? null : storiesViewPager.getCurrentPeerView();
        if (currentPeerView != null) {
            currentPeerView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelSwipeToViews$1(ValueAnimator valueAnimator) {
        this.selfStoriesViewsOffset = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.containerView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$layoutAndFindView$5() {
        updateTransitionParams();
        ImageReceiver imageReceiver = this.transitionViewHolder.avatarImage;
        if (imageReceiver != null) {
            imageReceiver.setVisible(false, true);
        }
        ImageReceiver imageReceiver2 = this.transitionViewHolder.storyImage;
        if (imageReceiver2 != null) {
            imageReceiver2.setVisible(false, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$listenToAttachedSheet$3() {
        this.currentSheet = null;
        updatePlayingMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        setLongPressed(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openViews$9() {
        this.allowSelfStoriesView = true;
        cancelSwipeToViews(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showDialog$2(DialogInterface dialogInterface) {
        if (dialogInterface == this.currentDialog) {
            this.currentDialog = null;
            updatePlayingMode();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startCloseAnimation$7(ValueAnimator valueAnimator) {
        this.progressToOpen = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        checkNavBarColor();
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.windowView;
        if (sizeNotifierFrameLayout != null) {
            sizeNotifierFrameLayout.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startCloseAnimation$8() {
        if (this.openCloseAnimator == null) {
            return;
        }
        HwFrameLayout hwFrameLayout = this.containerView;
        if (hwFrameLayout != null) {
            hwFrameLayout.enableHwAcceleration();
        }
        this.openCloseAnimator.addListener(new 11());
        this.openCloseAnimator.setDuration(400L);
        this.openCloseAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.openCloseAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startOpenAnimation$6(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.progressToOpen = floatValue;
        HwFrameLayout hwFrameLayout = this.containerView;
        if (hwFrameLayout != null) {
            hwFrameLayout.checkHwAcceleration(floatValue);
        }
        checkNavBarColor();
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.windowView;
        if (sizeNotifierFrameLayout != null) {
            sizeNotifierFrameLayout.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void layoutAndFindView() {
        PeerStoriesView currentPeerView;
        int selectedPosition;
        this.foundViewToClose = true;
        ImageReceiver imageReceiver = this.transitionViewHolder.avatarImage;
        if (imageReceiver != null) {
            imageReceiver.setVisible(true, true);
        }
        ImageReceiver imageReceiver2 = this.transitionViewHolder.storyImage;
        if (imageReceiver2 != null) {
            imageReceiver2.setAlpha(1.0f);
            this.transitionViewHolder.storyImage.setVisible(true, true);
        }
        if (this.storiesList != null && (currentPeerView = this.storiesViewPager.getCurrentPeerView()) != null && (selectedPosition = currentPeerView.getSelectedPosition()) >= 0 && selectedPosition < this.storiesList.messageObjects.size()) {
            this.messageId = ((MessageObject) this.storiesList.messageObjects.get(selectedPosition)).getId();
        }
        PlaceProvider placeProvider = this.placeProvider;
        if (placeProvider != null) {
            placeProvider.preLayout(this.storiesViewPager.getCurrentDialogId(), this.messageId, new Runnable() { // from class: org.telegram.ui.Stories.StoryViewer$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    StoryViewer.this.lambda$layoutAndFindView$5();
                }
            });
        }
    }

    private void lockOrientation(boolean z) {
        Activity findActivity = AndroidUtilities.findActivity(this.fragment.getContext());
        if (findActivity != null) {
            try {
                findActivity.setRequestedOrientation(z ? 1 : -1);
            } catch (Exception unused) {
            }
            if (z) {
                findActivity.getWindow().addFlags(128);
            } else {
                findActivity.getWindow().clearFlags(128);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestAdjust(boolean z) {
        if (this.ATTACH_TO_FRAGMENT) {
            if (z) {
                AndroidUtilities.requestAdjustNothing(this.fragment.getParentActivity(), this.fragment.getClassGuid());
                return;
            } else {
                AndroidUtilities.requestAdjustResize(this.fragment.getParentActivity(), this.fragment.getClassGuid());
                return;
            }
        }
        WindowManager.LayoutParams layoutParams = this.windowLayoutParams;
        layoutParams.softInputMode = z ? 48 : 16;
        try {
            this.windowManager.updateViewLayout(this.windowView, layoutParams);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setInTouchMode(boolean z) {
        this.isInTouchMode = z;
        if (z) {
            this.volumeControl.hide();
        }
        updatePlayingMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLongPressed(boolean z) {
        PeerStoriesView currentPeerView;
        PeerStoriesView currentPeerView2;
        PeerStoriesView.StoryItemHolder storyItemHolder;
        VideoPlayerHolder videoPlayerHolder;
        PeerStoriesView.VideoPlayerSharedScope videoPlayerSharedScope;
        if (this.isLongpressed != z) {
            this.isLongpressed = z;
            if (z && !this.isInPinchToZoom && (currentPeerView2 = this.storiesViewPager.getCurrentPeerView()) != null && (storyItemHolder = currentPeerView2.currentStory) != null && storyItemHolder.uploadingStory == null) {
                if (!this.inSeekingMode && !this.inSwipeToDissmissMode && (videoPlayerSharedScope = this.currentPlayerScope) != null && videoPlayerSharedScope.player != null) {
                    currentPeerView2.storyContainer.invalidate();
                    BotWebViewVibrationEffect.IMPACT_LIGHT.vibrate();
                }
                PeerStoriesView.VideoPlayerSharedScope videoPlayerSharedScope2 = this.currentPlayerScope;
                if (videoPlayerSharedScope2 != null && (videoPlayerHolder = videoPlayerSharedScope2.player) != null && !this.inSeekingMode) {
                    videoPlayerHolder.setSeeking(true);
                }
                this.inSeekingMode = true;
            }
            updatePlayingMode();
            StoriesViewPager storiesViewPager = this.storiesViewPager;
            if (storiesViewPager == null || (currentPeerView = storiesViewPager.getCurrentPeerView()) == null) {
                return;
            }
            currentPeerView.setLongpressed(this.isLongpressed);
        }
    }

    private void setNavigationButtonsColor(boolean z) {
        LaunchActivity launchActivity = LaunchActivity.instance;
        if (!this.ATTACH_TO_FRAGMENT || launchActivity == null) {
            return;
        }
        if (z) {
            this.openedFromLightNavigationBar = launchActivity.isLightNavigationBar();
        }
        if (this.openedFromLightNavigationBar) {
            launchActivity.setLightNavigationBar(!z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showKeyboard() {
        PeerStoriesView currentPeerView = this.storiesViewPager.getCurrentPeerView();
        if (currentPeerView == null || !currentPeerView.showKeyboard()) {
            cancelSwipeToReply();
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoryViewer$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    StoryViewer.this.cancelSwipeToReply();
                }
            }, 200L);
        }
    }

    private void startCloseAnimation(boolean z) {
        setNavigationButtonsColor(false);
        updateTransitionParams();
        this.locker.lock();
        this.fromDismissOffset = this.swipeToDismissOffset;
        this.opening = false;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.progressToOpen, 0.0f);
        this.openCloseAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.StoryViewer$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                StoryViewer.this.lambda$startCloseAnimation$7(valueAnimator);
            }
        });
        if (z) {
            layoutAndFindView();
        } else {
            this.fromY = 0.0f;
            this.fromX = 0.0f;
            ImageReceiver imageReceiver = this.transitionViewHolder.avatarImage;
            if (imageReceiver != null) {
                imageReceiver.setVisible(true, true);
            }
            ImageReceiver imageReceiver2 = this.transitionViewHolder.storyImage;
            if (imageReceiver2 != null) {
                imageReceiver2.setVisible(true, true);
            }
            TransitionViewHolder transitionViewHolder = this.transitionViewHolder;
            transitionViewHolder.storyImage = null;
            transitionViewHolder.avatarImage = null;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoryViewer$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                StoryViewer.this.lambda$startCloseAnimation$8();
            }
        }, 16L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startOpenAnimation() {
        PeerStoriesView currentPeerView;
        RadialProgress radialProgress;
        updateTransitionParams();
        this.progressToOpen = 0.0f;
        setNavigationButtonsColor(true);
        this.foundViewToClose = false;
        animationInProgress = true;
        this.fromDismissOffset = this.swipeToDismissOffset;
        if (this.transitionViewHolder.radialProgressUpload != null && (currentPeerView = getCurrentPeerView()) != null && (radialProgress = currentPeerView.headerView.radialProgress) != null) {
            radialProgress.copyParams(this.transitionViewHolder.radialProgressUpload);
        }
        this.opening = true;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.openCloseAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.StoryViewer$$ExternalSyntheticLambda8
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                StoryViewer.this.lambda$startOpenAnimation$6(valueAnimator);
            }
        });
        this.locker.lock();
        HwFrameLayout hwFrameLayout = this.containerView;
        if (hwFrameLayout != null) {
            hwFrameLayout.enableHwAcceleration();
        }
        this.openCloseAnimator.addListener(new 10());
        this.openCloseAnimator.setStartDelay(40L);
        this.openCloseAnimator.setDuration(250L);
        this.openCloseAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.openCloseAnimator.start();
        if (this.doOnAnimationReadyRunnables.isEmpty()) {
            return;
        }
        for (int i = 0; i < this.doOnAnimationReadyRunnables.size(); i++) {
            ((Runnable) this.doOnAnimationReadyRunnables.get(i)).run();
        }
        this.doOnAnimationReadyRunnables.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProgressToDismiss() {
        float f = this.swipeToDismissHorizontalOffset;
        if (f == 0.0f) {
            f = this.swipeToDismissOffset;
        }
        float clamp = MathUtils.clamp(Math.abs(f / AndroidUtilities.dp(80.0f)), 0.0f, 1.0f);
        if (this.progressToDismiss != clamp) {
            this.progressToDismiss = clamp;
            checkNavBarColor();
            PeerStoriesView currentPeerView = this.storiesViewPager.getCurrentPeerView();
            if (currentPeerView != null) {
                currentPeerView.progressToDismissUpdated();
            }
        }
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.windowView;
        if (sizeNotifierFrameLayout != null) {
            sizeNotifierFrameLayout.invalidate();
        }
    }

    private void updateTransitionParams() {
        float f = 0.0f;
        if (this.placeProvider != null) {
            ImageReceiver imageReceiver = this.transitionViewHolder.avatarImage;
            if (imageReceiver != null) {
                imageReceiver.setVisible(true, true);
            }
            ImageReceiver imageReceiver2 = this.transitionViewHolder.storyImage;
            if (imageReceiver2 != null) {
                imageReceiver2.setAlpha(1.0f);
                this.transitionViewHolder.storyImage.setVisible(true, true);
            }
            PeerStoriesView currentPeerView = this.storiesViewPager.getCurrentPeerView();
            int selectedPosition = currentPeerView == null ? 0 : currentPeerView.getSelectedPosition();
            int i = (currentPeerView == null || selectedPosition < 0 || selectedPosition >= currentPeerView.storyItems.size()) ? 0 : ((TL_stories.StoryItem) currentPeerView.storyItems.get(selectedPosition)).id;
            TL_stories.StoryItem storyItem = (currentPeerView == null || selectedPosition < 0 || selectedPosition >= currentPeerView.storyItems.size()) ? null : (TL_stories.StoryItem) currentPeerView.storyItems.get(selectedPosition);
            if (storyItem == null && this.isSingleStory) {
                storyItem = this.singleStory;
            }
            long currentDialogId = this.storiesViewPager.getCurrentDialogId();
            StoriesController.StoriesList storiesList = this.storiesList;
            if ((storiesList instanceof StoriesController.SearchStoriesList) && storyItem != null) {
                currentDialogId = storyItem.dialogId;
                i = storyItem.messageId;
            } else if (storiesList != null) {
                i = this.dayStoryId;
            }
            long j = currentDialogId;
            this.transitionViewHolder.clear();
            if (this.placeProvider.findView(j, this.messageId, i, storyItem == null ? -1 : storyItem.messageType, this.transitionViewHolder)) {
                TransitionViewHolder transitionViewHolder = this.transitionViewHolder;
                transitionViewHolder.storyId = i;
                View view = transitionViewHolder.view;
                if (view != null) {
                    int[] iArr = new int[2];
                    view.getLocationOnScreen(iArr);
                    float f2 = iArr[0];
                    this.fromXCell = f2;
                    this.fromYCell = iArr[1];
                    TransitionViewHolder transitionViewHolder2 = this.transitionViewHolder;
                    KeyEvent.Callback callback = transitionViewHolder2.view;
                    if (callback instanceof StoriesListPlaceProvider.AvatarOverlaysView) {
                        this.animateFromCell = (StoriesListPlaceProvider.AvatarOverlaysView) callback;
                    } else {
                        this.animateFromCell = null;
                    }
                    this.animateAvatar = false;
                    ImageReceiver imageReceiver3 = transitionViewHolder2.avatarImage;
                    if (imageReceiver3 != null) {
                        this.fromX = f2 + imageReceiver3.getCenterX();
                        this.fromY = iArr[1] + this.transitionViewHolder.avatarImage.getCenterY();
                        this.fromWidth = this.transitionViewHolder.avatarImage.getImageWidth();
                        this.fromHeight = this.transitionViewHolder.avatarImage.getImageHeight();
                        StoriesUtilities.AvatarStoryParams avatarStoryParams = this.transitionViewHolder.params;
                        if (avatarStoryParams != null) {
                            this.fromWidth *= avatarStoryParams.getScale();
                            this.fromHeight *= this.transitionViewHolder.params.getScale();
                        }
                        if (this.transitionViewHolder.view.getParent() instanceof View) {
                            View view2 = (View) this.transitionViewHolder.view.getParent();
                            this.fromX = iArr[0] + (this.transitionViewHolder.avatarImage.getCenterX() * view2.getScaleX());
                            this.fromY = iArr[1] + (this.transitionViewHolder.avatarImage.getCenterY() * view2.getScaleY());
                            this.fromWidth *= view2.getScaleX();
                            this.fromHeight *= view2.getScaleY();
                        }
                        this.animateAvatar = true;
                    } else {
                        ImageReceiver imageReceiver4 = transitionViewHolder2.storyImage;
                        if (imageReceiver4 != null) {
                            this.fromX = f2 + imageReceiver4.getCenterX();
                            this.fromY = iArr[1] + this.transitionViewHolder.storyImage.getCenterY();
                            this.fromWidth = this.transitionViewHolder.storyImage.getImageWidth();
                            this.fromHeight = this.transitionViewHolder.storyImage.getImageHeight();
                            this.fromRadius = this.transitionViewHolder.storyImage.getRoundRadius()[0];
                        }
                    }
                    this.transitionViewHolder.clipParent.getLocationOnScreen(iArr);
                    TransitionViewHolder transitionViewHolder3 = this.transitionViewHolder;
                    float f3 = transitionViewHolder3.clipTop;
                    if (f3 == 0.0f && transitionViewHolder3.clipBottom == 0.0f) {
                        this.clipTop = 0.0f;
                    } else {
                        float f4 = iArr[1];
                        this.clipTop = f3 + f4;
                        f = f4 + transitionViewHolder3.clipBottom;
                    }
                    this.clipBottom = f;
                    return;
                }
            }
        }
        this.animateAvatar = false;
        this.fromY = 0.0f;
        this.fromX = 0.0f;
    }

    public void allowScreenshots(boolean z) {
        WindowManager.LayoutParams layoutParams;
        int i;
        if (BuildVars.DEBUG_PRIVATE_VERSION) {
            return;
        }
        boolean z2 = !this.isShowing || z;
        if (this.allowScreenshots != z2) {
            this.allowScreenshots = z2;
            SurfaceView surfaceView = this.surfaceView;
            if (surfaceView != null) {
                surfaceView.setSecure(!z2);
            }
            if (this.ATTACH_TO_FRAGMENT) {
                if (this.fragment.getParentActivity() != null) {
                    if (z2) {
                        this.fragment.getParentActivity().getWindow().clearFlags(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM);
                        return;
                    } else {
                        this.fragment.getParentActivity().getWindow().addFlags(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM);
                        return;
                    }
                }
                return;
            }
            if (z2) {
                layoutParams = this.windowLayoutParams;
                i = layoutParams.flags & (-8193);
            } else {
                layoutParams = this.windowLayoutParams;
                i = layoutParams.flags | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM;
            }
            layoutParams.flags = i;
            try {
                this.windowManager.updateViewLayout(this.windowView, this.windowLayoutParams);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public boolean attachedToParent() {
        return this.ATTACH_TO_FRAGMENT && this.windowView != null;
    }

    public void cancelSwipeToReply() {
        if (this.swipeToReplyBackAnimator == null) {
            this.inSwipeToDissmissMode = false;
            this.allowSwipeToReply = false;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.swipeToReplyOffset, 0.0f);
            this.swipeToReplyBackAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.StoryViewer$$ExternalSyntheticLambda9
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    StoryViewer.this.lambda$cancelSwipeToReply$4(valueAnimator);
                }
            });
            this.swipeToReplyBackAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.StoryViewer.9
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    StoryViewer storyViewer = StoryViewer.this;
                    storyViewer.swipeToReplyBackAnimator = null;
                    storyViewer.swipeToReplyOffset = 0.0f;
                    storyViewer.swipeToReplyProgress = 0.0f;
                    StoriesViewPager storiesViewPager = storyViewer.storiesViewPager;
                    PeerStoriesView currentPeerView = storiesViewPager != null ? storiesViewPager.getCurrentPeerView() : null;
                    if (currentPeerView != null) {
                        currentPeerView.invalidate();
                    }
                }
            });
            this.swipeToReplyBackAnimator.setDuration(250L);
            this.swipeToReplyBackAnimator.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
            this.swipeToReplyBackAnimator.start();
        }
    }

    public void cancelSwipeToViews(final boolean z) {
        ValueAnimator valueAnimator;
        CubicBezierInterpolator cubicBezierInterpolator;
        if (this.swipeToViewsAnimator != null) {
            return;
        }
        if (this.realKeyboardHeight != 0) {
            AndroidUtilities.hideKeyboard(this.selfStoryViewsView);
            return;
        }
        if (this.allowSelfStoriesView || this.selfStoriesViewsOffset != 0.0f) {
            this.locker.lock();
            if (!z) {
                float f = this.selfStoriesViewsOffset;
                SelfStoryViewsView selfStoryViewsView = this.selfStoryViewsView;
                float f2 = selfStoryViewsView.maxSelfStoriesViewsOffset;
                if (f == f2) {
                    float f3 = f2 - 1.0f;
                    this.selfStoriesViewsOffset = f3;
                    selfStoryViewsView.setOffset(f3);
                }
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.selfStoriesViewsOffset, z ? this.selfStoryViewsView.maxSelfStoriesViewsOffset : 0.0f);
            this.swipeToViewsAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stories.StoryViewer$$ExternalSyntheticLambda5
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    StoryViewer.this.lambda$cancelSwipeToViews$1(valueAnimator2);
                }
            });
            this.swipeToViewsAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stories.StoryViewer.8
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    StoryViewer.this.locker.unlock();
                    StoryViewer storyViewer = StoryViewer.this;
                    storyViewer.selfStoriesViewsOffset = z ? storyViewer.selfStoryViewsView.maxSelfStoriesViewsOffset : 0.0f;
                    PeerStoriesView currentPeerView = storyViewer.storiesViewPager.getCurrentPeerView();
                    if (currentPeerView != null) {
                        currentPeerView.invalidate();
                    }
                    StoryViewer.this.containerView.invalidate();
                    StoryViewer.this.swipeToViewsAnimator = null;
                }
            });
            if (z) {
                this.swipeToViewsAnimator.setDuration(350L);
                valueAnimator = this.swipeToViewsAnimator;
                cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            } else {
                this.swipeToViewsAnimator.setDuration(350L);
                valueAnimator = this.swipeToViewsAnimator;
                cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
            }
            valueAnimator.setInterpolator(cubicBezierInterpolator);
            this.swipeToViewsAnimator.start();
        }
    }

    public void checkNavBarColor() {
        LaunchActivity launchActivity;
        if (!this.ATTACH_TO_FRAGMENT || (launchActivity = LaunchActivity.instance) == null) {
            return;
        }
        launchActivity.checkSystemBarColors(true, true, true, false);
    }

    public void checkSelfStoriesView() {
        if (this.selfStoryViewsView == null) {
            SelfStoryViewsView selfStoryViewsView = new SelfStoryViewsView(this.containerView.getContext(), this);
            this.selfStoryViewsView = selfStoryViewsView;
            this.containerView.addView(selfStoryViewsView, 0);
        }
        PeerStoriesView currentPeerView = this.storiesViewPager.getCurrentPeerView();
        if (currentPeerView != null) {
            if (this.storiesList == null) {
                this.selfStoryViewsView.setItems(currentPeerView.getCurrentPeer(), currentPeerView.getStoryItems(), currentPeerView.getSelectedPosition());
                return;
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.storiesList.messageObjects.size(); i++) {
                arrayList.add(((MessageObject) this.storiesList.messageObjects.get(i)).storyItem);
            }
            this.selfStoryViewsView.setItems(this.storiesList.dialogId, arrayList, currentPeerView.getListPosition());
        }
    }

    public void clearDraft(long j, TL_stories.StoryItem storyItem) {
        if (j == 0 || storyItem == null) {
            return;
        }
        replyDrafts.remove(draftHash(j, storyItem));
    }

    public void close(boolean z) {
        AndroidUtilities.hideKeyboard(this.windowView);
        this.isClosed = true;
        this.invalidateOutRect = true;
        updatePlayingMode();
        startCloseAnimation(z);
        if (this.unreadStateChanged) {
            this.unreadStateChanged = false;
        }
    }

    public boolean closeKeyboardOrEmoji() {
        PeerStoriesView currentPeerView;
        StoriesViewPager storiesViewPager = this.storiesViewPager;
        if (storiesViewPager == null || (currentPeerView = storiesViewPager.getCurrentPeerView()) == null) {
            return false;
        }
        return currentPeerView.closeKeyboardOrEmoji();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        int i3 = 0;
        if (i == NotificationCenter.storiesListUpdated) {
            if (this.storiesList == ((StoriesController.StoriesList) objArr[0])) {
                getCurrentPeerView();
                StoriesViewPager storiesViewPager = this.storiesViewPager;
                StoriesController.StoriesList storiesList = this.storiesList;
                storiesViewPager.setDays(storiesList.dialogId, storiesList.getDays(), this.currentAccount);
                SelfStoryViewsView selfStoryViewsView = this.selfStoryViewsView;
                if (selfStoryViewsView != null) {
                    TL_stories.StoryItem selectedStory = selfStoryViewsView.getSelectedStory();
                    ArrayList arrayList = new ArrayList();
                    int i4 = 0;
                    while (i3 < this.storiesList.messageObjects.size()) {
                        if (selectedStory != null && selectedStory.id == ((MessageObject) this.storiesList.messageObjects.get(i3)).storyItem.id) {
                            i4 = i3;
                        }
                        arrayList.add(((MessageObject) this.storiesList.messageObjects.get(i3)).storyItem);
                        i3++;
                    }
                    this.selfStoryViewsView.setItems(this.storiesList.dialogId, arrayList, i4);
                    return;
                }
                return;
            }
            return;
        }
        if (i != NotificationCenter.storiesUpdated) {
            int i5 = NotificationCenter.openArticle;
            if (i == i5 || i == NotificationCenter.articleClosed) {
                updatePlayingMode();
                if (i != i5) {
                    if (this.paused || getCurrentPeerView() == null) {
                        return;
                    }
                    getCurrentPeerView().updatePosition();
                    return;
                }
                VideoPlayerHolder videoPlayerHolder = this.playerHolder;
                if (videoPlayerHolder == null) {
                    this.playerSavedPosition = 0L;
                    return;
                }
                this.playerSavedPosition = videoPlayerHolder.currentPosition;
                this.playerHolder.release(null);
                this.playerHolder = null;
                return;
            }
            return;
        }
        PlaceProvider placeProvider = this.placeProvider;
        if (placeProvider instanceof StoriesListPlaceProvider) {
            StoriesListPlaceProvider storiesListPlaceProvider = (StoriesListPlaceProvider) placeProvider;
            if (!storiesListPlaceProvider.hasPaginationParams || storiesListPlaceProvider.onlySelfStories) {
                return;
            }
            StoriesController storiesController = MessagesController.getInstance(this.currentAccount).getStoriesController();
            ArrayList hiddenList = storiesListPlaceProvider.hiddedStories ? storiesController.getHiddenList() : storiesController.getDialogListStories();
            ArrayList<Long> dialogIds = this.storiesViewPager.getDialogIds();
            boolean z = false;
            while (i3 < hiddenList.size()) {
                long peerDialogId = DialogObject.getPeerDialogId(((TL_stories.PeerStories) hiddenList.get(i3)).peer);
                if ((!storiesListPlaceProvider.onlyUnreadStories || storiesController.hasUnreadStories(peerDialogId)) && !dialogIds.contains(Long.valueOf(peerDialogId))) {
                    dialogIds.add(Long.valueOf(peerDialogId));
                    z = true;
                }
                i3++;
            }
            if (z) {
                this.storiesViewPager.getAdapter().notifyDataSetChanged();
            }
        }
        SelfStoryViewsView selfStoryViewsView2 = this.selfStoryViewsView;
        if (selfStoryViewsView2 != null) {
            selfStoryViewsView2.selfStoriesPreviewView.update();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet, android.content.DialogInterface
    public void dismiss() {
        close(true);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public /* synthetic */ void dismiss(boolean z) {
        dismiss();
    }

    public void dismissVisibleDialogs() {
        Dialog dialog = this.currentDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
        BaseFragment.AttachedSheet attachedSheet = this.currentSheet;
        if (attachedSheet != null) {
            attachedSheet.dismiss();
        }
        PeerStoriesView currentPeerView = getCurrentPeerView();
        if (currentPeerView != null) {
            ReactionsContainerLayout reactionsContainerLayout = currentPeerView.reactionsContainerLayout;
            if (reactionsContainerLayout != null && reactionsContainerLayout.getReactionsWindow() != null) {
                currentPeerView.reactionsContainerLayout.getReactionsWindow().dismiss();
            }
            ShareAlert shareAlert = currentPeerView.shareAlert;
            if (shareAlert != null) {
                shareAlert.dismiss();
            }
            currentPeerView.needEnterText();
        }
    }

    public void dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 24 || keyEvent.getKeyCode() == 25) {
            dispatchVolumeEvent(keyEvent);
        }
    }

    public void doOnAnimationReady(Runnable runnable) {
        if (runnable != null) {
            this.doOnAnimationReadyRunnables.add(runnable);
        }
    }

    public FrameLayout getContainerForBulletin() {
        PeerStoriesView currentPeerView = this.storiesViewPager.getCurrentPeerView();
        if (currentPeerView != null) {
            return currentPeerView.storyContainer;
        }
        return null;
    }

    public PeerStoriesView getCurrentPeerView() {
        StoriesViewPager storiesViewPager = this.storiesViewPager;
        if (storiesViewPager == null) {
            return null;
        }
        return storiesViewPager.getCurrentPeerView();
    }

    public CharSequence getDraft(long j, TL_stories.StoryItem storyItem) {
        return (j == 0 || storyItem == null) ? "" : (CharSequence) replyDrafts.get(draftHash(j, storyItem), "");
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public int getNavigationBarColor(int i) {
        return ColorUtils.blendARGB(i, -16777216, getBlackoutAlpha());
    }

    public float getProgressToSelfViews() {
        SelfStoryViewsView selfStoryViewsView = this.selfStoryViewsView;
        if (selfStoryViewsView == null) {
            return 0.0f;
        }
        return selfStoryViewsView.progressToOpen;
    }

    public Theme.ResourcesProvider getResourceProvider() {
        return this.resourcesProvider;
    }

    public boolean getStoryRect(RectF rectF) {
        PeerStoriesView currentPeerView;
        StoriesViewPager storiesViewPager = this.storiesViewPager;
        if (storiesViewPager == null || (currentPeerView = storiesViewPager.getCurrentPeerView()) == null || currentPeerView.storyContainer == null) {
            return false;
        }
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.windowView;
        float x = sizeNotifierFrameLayout == null ? 0.0f : sizeNotifierFrameLayout.getX();
        SizeNotifierFrameLayout sizeNotifierFrameLayout2 = this.windowView;
        float y = sizeNotifierFrameLayout2 != null ? sizeNotifierFrameLayout2.getY() : 0.0f;
        rectF.set(this.swipeToDismissHorizontalOffset + x + this.containerView.getLeft() + currentPeerView.getX() + currentPeerView.storyContainer.getX(), this.swipeToDismissOffset + y + this.containerView.getTop() + currentPeerView.getY() + currentPeerView.storyContainer.getY(), (((x + this.swipeToDismissHorizontalOffset) + this.containerView.getRight()) - (this.containerView.getWidth() - currentPeerView.getRight())) - (currentPeerView.getWidth() - currentPeerView.storyContainer.getRight()), (((y + this.swipeToDismissOffset) + this.containerView.getBottom()) - (this.containerView.getHeight() - currentPeerView.getBottom())) - (currentPeerView.getHeight() - currentPeerView.storyContainer.getBottom()));
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public View getWindowView() {
        return this.windowView;
    }

    public void instantClose() {
        if (this.isShowing) {
            AndroidUtilities.hideKeyboard(this.windowView);
            this.isClosed = true;
            this.fullyVisible = false;
            this.progressToOpen = 0.0f;
            this.progressToDismiss = 0.0f;
            updatePlayingMode();
            this.fromY = 0.0f;
            this.fromX = 0.0f;
            ImageReceiver imageReceiver = this.transitionViewHolder.avatarImage;
            if (imageReceiver != null) {
                imageReceiver.setVisible(true, true);
            }
            ImageReceiver imageReceiver2 = this.transitionViewHolder.storyImage;
            if (imageReceiver2 != null) {
                imageReceiver2.setVisible(true, true);
            }
            TransitionViewHolder transitionViewHolder = this.transitionViewHolder;
            transitionViewHolder.storyImage = null;
            transitionViewHolder.avatarImage = null;
            HwFrameLayout hwFrameLayout = this.containerView;
            if (hwFrameLayout != null) {
                hwFrameLayout.disableHwAcceleration();
            }
            this.locker.unlock();
            PeerStoriesView.VideoPlayerSharedScope videoPlayerSharedScope = this.currentPlayerScope;
            if (videoPlayerSharedScope != null) {
                videoPlayerSharedScope.invalidate();
            }
            release();
            if (this.ATTACH_TO_FRAGMENT) {
                AndroidUtilities.removeFromParent(this.windowView);
            } else {
                this.windowManager.removeView(this.windowView);
            }
            this.windowView = null;
            this.isShowing = false;
            this.foundViewToClose = false;
            checkNavBarColor();
            Runnable runnable = this.onCloseListener;
            if (runnable != null) {
                runnable.run();
                this.onCloseListener = null;
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public boolean isAttachedLightStatusBar() {
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public boolean isFullyVisible() {
        return this.fullyVisible;
    }

    public boolean isPaused() {
        BaseFragment baseFragment;
        return this.isPopupVisible || this.isTranslating || this.isBulletinVisible || this.isCaption || this.isWaiting || this.isInTouchMode || this.keyboardVisible || this.currentDialog != null || this.currentSheet != null || this.allowTouchesByViewpager || this.isClosed || this.isRecording || this.progressToOpen != 1.0f || this.selfStoriesViewsOffset != 0.0f || this.isHintVisible || (this.isSwiping && this.USE_SURFACE_VIEW) || this.isOverlayVisible || this.isInTextSelectionMode || this.isLikesReactions || this.progressToDismiss != 0.0f || this.storiesIntro != null || !(!this.ATTACH_TO_FRAGMENT || (baseFragment = this.fragment) == null || baseFragment.getLastStoryViewer() == this);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public boolean isShown() {
        return !this.isClosed;
    }

    public boolean listenToAttachedSheet(BaseFragment.AttachedSheet attachedSheet) {
        this.currentSheet = attachedSheet;
        attachedSheet.setOnDismissListener(new Runnable() { // from class: org.telegram.ui.Stories.StoryViewer$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                StoryViewer.this.lambda$listenToAttachedSheet$3();
            }
        });
        return true;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        PeerStoriesView currentPeerView = this.storiesViewPager.getCurrentPeerView();
        if (currentPeerView != null) {
            currentPeerView.onActivityResult(i, i2, intent);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public boolean onAttachedBackPressed() {
        if (this.selfStoriesViewsOffset != 0.0f) {
            if (this.selfStoryViewsView.onBackPressed()) {
                return true;
            }
            cancelSwipeToViews(false);
            return true;
        }
        if (closeKeyboardOrEmoji()) {
            return true;
        }
        close(true);
        return true;
    }

    public void onPause() {
        this.paused = true;
        VideoPlayerHolder videoPlayerHolder = this.playerHolder;
        if (videoPlayerHolder != null) {
            videoPlayerHolder.release(null);
            this.playerHolder = null;
        }
        StoriesIntro storiesIntro = this.storiesIntro;
        if (storiesIntro != null) {
            storiesIntro.stopAnimation();
        }
    }

    public void onResume() {
        this.paused = false;
        if (!ArticleViewer.getInstance().isVisible() && getCurrentPeerView() != null) {
            getCurrentPeerView().updatePosition();
        }
        StoriesIntro storiesIntro = this.storiesIntro;
        if (storiesIntro != null) {
            storiesIntro.startAnimation(false);
        }
    }

    public void open(Context context, int i, StoriesController.StoriesList storiesList, PlaceProvider placeProvider) {
        this.currentAccount = UserConfig.selectedAccount;
        ArrayList arrayList = new ArrayList();
        arrayList.add(Long.valueOf(storiesList.dialogId));
        this.dayStoryId = i;
        open(context, null, arrayList, 0, storiesList, null, placeProvider, false);
    }

    public void open(Context context, long j, PlaceProvider placeProvider) {
        this.currentAccount = UserConfig.selectedAccount;
        ArrayList arrayList = new ArrayList();
        arrayList.add(Long.valueOf(j));
        MessagesController.getInstance(this.currentAccount).getStoriesController().checkExpiredStories(j);
        open(context, null, arrayList, 0, null, null, placeProvider, false);
    }

    public void open(Context context, TL_stories.PeerStories peerStories, PlaceProvider placeProvider) {
        ArrayList<TL_stories.StoryItem> arrayList;
        if (peerStories == null || (arrayList = peerStories.stories) == null || arrayList.isEmpty()) {
            this.doOnAnimationReadyRunnables.clear();
            return;
        }
        this.currentAccount = UserConfig.selectedAccount;
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(Long.valueOf(DialogObject.getPeerDialogId(peerStories.peer)));
        open(context, peerStories.stories.get(0), arrayList2, 0, null, peerStories, placeProvider, false);
    }

    public void open(Context context, TL_stories.StoryItem storyItem, int i, StoriesController.StoriesList storiesList, boolean z, PlaceProvider placeProvider) {
        this.currentAccount = UserConfig.selectedAccount;
        ArrayList arrayList = new ArrayList();
        arrayList.add(Long.valueOf(storiesList.dialogId));
        this.dayStoryId = i;
        open(context, storyItem, arrayList, 0, storiesList, null, placeProvider, z);
    }

    public void open(Context context, TL_stories.StoryItem storyItem, ArrayList arrayList, int i, StoriesController.StoriesList storiesList, TL_stories.PeerStories peerStories, PlaceProvider placeProvider, boolean z) {
        if (context == null) {
            this.doOnAnimationReadyRunnables.clear();
            return;
        }
        ValueAnimator valueAnimator = this.openCloseAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.openCloseAnimator = null;
        }
        if (this.isShowing) {
            this.doOnAnimationReadyRunnables.clear();
            return;
        }
        setSpeed(1.0f);
        boolean z2 = (AndroidUtilities.isTablet() || this.fromBottomSheet) ? false : true;
        this.ATTACH_TO_FRAGMENT = z2;
        this.USE_SURFACE_VIEW = SharedConfig.useSurfaceInStories && z2;
        this.messageId = storyItem == null ? 0 : storyItem.messageId;
        this.isSingleStory = storyItem != null && storiesList == null && peerStories == null;
        if (storyItem != null) {
            this.singleStory = storyItem;
            lastStoryItem = storyItem;
        }
        this.storiesList = storiesList;
        this.overrideUserStories = peerStories;
        this.placeProvider = placeProvider;
        this.reversed = z;
        this.currentAccount = UserConfig.selectedAccount;
        this.swipeToDismissOffset = 0.0f;
        this.swipeToDismissHorizontalOffset = 0.0f;
        StoriesViewPager storiesViewPager = this.storiesViewPager;
        if (storiesViewPager != null) {
            storiesViewPager.setHorizontalProgressToDismiss(0.0f);
            this.storiesViewPager.currentState = 0;
        }
        this.swipeToReplyProgress = 0.0f;
        this.swipeToReplyOffset = 0.0f;
        this.allowSwipeToReply = false;
        this.progressToDismiss = 0.0f;
        this.isShowing = true;
        this.isLongpressed = false;
        this.isTranslating = false;
        this.savedPositions.clear();
        AndroidUtilities.cancelRunOnUIThread(this.longPressRunnable);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        this.windowLayoutParams = layoutParams;
        layoutParams.height = -1;
        layoutParams.format = -3;
        layoutParams.width = -1;
        layoutParams.gravity = 51;
        layoutParams.type = 99;
        layoutParams.softInputMode = 16;
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 28) {
            layoutParams.layoutInDisplayCutoutMode = 1;
        }
        if (i2 >= 21) {
            layoutParams.flags = -2147417728;
        }
        this.isClosed = false;
        this.unreadStateChanged = false;
        BaseFragment lastFragment = LaunchActivity.getLastFragment();
        if (this.windowView == null) {
            this.gestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() { // from class: org.telegram.ui.Stories.StoryViewer.1
                @Override // android.view.GestureDetector.OnGestureListener
                public boolean onDown(MotionEvent motionEvent) {
                    StoryViewer.this.flingCalled = false;
                    StoryViewer storyViewer = StoryViewer.this;
                    return !storyViewer.findClickableView(storyViewer.windowView, motionEvent.getX(), motionEvent.getY(), false);
                }

                @Override // android.view.GestureDetector.OnGestureListener
                public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                    StoryViewer storyViewer = StoryViewer.this;
                    if (storyViewer.swipeToReplyOffset != 0.0f && storyViewer.storiesIntro == null && f2 < -1000.0f) {
                        StoryViewer storyViewer2 = StoryViewer.this;
                        if (!storyViewer2.swipeToReplyWaitingKeyboard) {
                            storyViewer2.swipeToReplyWaitingKeyboard = true;
                            storyViewer2.windowView.performHapticFeedback(3);
                            StoryViewer.this.showKeyboard();
                        }
                    }
                    StoryViewer storyViewer3 = StoryViewer.this;
                    if (storyViewer3.selfStoriesViewsOffset != 0.0f) {
                        if (f2 < -1000.0f) {
                            storyViewer3.cancelSwipeToViews(true);
                        } else if (f2 > 1000.0f) {
                            storyViewer3.cancelSwipeToViews(false);
                        } else {
                            storyViewer3.cancelSwipeToViews(storyViewer3.selfStoryViewsView.progressToOpen > 0.5f);
                        }
                    }
                    StoryViewer.this.flingCalled = true;
                    return false;
                }

                @Override // android.view.GestureDetector.OnGestureListener
                public void onLongPress(MotionEvent motionEvent) {
                }

                @Override // android.view.GestureDetector.OnGestureListener
                public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                    float f3;
                    StoryViewer storyViewer = StoryViewer.this;
                    if (!storyViewer.inSwipeToDissmissMode) {
                        return false;
                    }
                    if (storyViewer.allowSwipeToReply) {
                        storyViewer.swipeToReplyOffset += f2;
                        int dp = AndroidUtilities.dp(200.0f);
                        StoryViewer storyViewer2 = StoryViewer.this;
                        float f4 = dp;
                        if (storyViewer2.swipeToReplyOffset > f4 && !storyViewer2.swipeToReplyWaitingKeyboard) {
                            storyViewer2.swipeToReplyWaitingKeyboard = true;
                            storyViewer2.showKeyboard();
                            StoryViewer.this.windowView.performHapticFeedback(3);
                        }
                        StoryViewer storyViewer3 = StoryViewer.this;
                        storyViewer3.swipeToReplyProgress = Utilities.clamp(storyViewer3.swipeToReplyOffset / f4, 1.0f, 0.0f);
                        if (StoryViewer.this.storiesViewPager.getCurrentPeerView() != null) {
                            StoryViewer.this.storiesViewPager.getCurrentPeerView().invalidate();
                        }
                        StoryViewer storyViewer4 = StoryViewer.this;
                        if (storyViewer4.swipeToReplyOffset >= 0.0f) {
                            return true;
                        }
                        storyViewer4.swipeToReplyOffset = 0.0f;
                        storyViewer4.allowSwipeToReply = false;
                    }
                    StoryViewer storyViewer5 = StoryViewer.this;
                    if (storyViewer5.allowSelfStoriesView) {
                        float f5 = storyViewer5.selfStoriesViewsOffset;
                        storyViewer5.selfStoriesViewsOffset = (f5 <= storyViewer5.selfStoryViewsView.maxSelfStoriesViewsOffset || f2 <= 0.0f) ? f5 + f2 : f5 + (0.05f * f2);
                        Bulletin.hideVisible(storyViewer5.windowView);
                        if (StoryViewer.this.storiesViewPager.getCurrentPeerView() != null) {
                            StoryViewer.this.storiesViewPager.getCurrentPeerView().invalidate();
                        }
                        StoryViewer.this.containerView.invalidate();
                        StoryViewer storyViewer6 = StoryViewer.this;
                        if (storyViewer6.selfStoriesViewsOffset >= 0.0f) {
                            return true;
                        }
                        storyViewer6.selfStoriesViewsOffset = 0.0f;
                        storyViewer6.allowSelfStoriesView = false;
                    }
                    StoryViewer storyViewer7 = StoryViewer.this;
                    if (storyViewer7.progressToDismiss > 0.8f) {
                        float f6 = -f2;
                        if ((f6 > 0.0f && storyViewer7.swipeToDismissOffset > 0.0f) || (f6 < 0.0f && storyViewer7.swipeToDismissOffset < 0.0f)) {
                            f3 = 0.3f;
                            storyViewer7.swipeToDismissOffset -= f2 * f3;
                            Bulletin.hideVisible(storyViewer7.windowView);
                            StoryViewer.this.updateProgressToDismiss();
                            return true;
                        }
                    }
                    f3 = 0.6f;
                    storyViewer7.swipeToDismissOffset -= f2 * f3;
                    Bulletin.hideVisible(storyViewer7.windowView);
                    StoryViewer.this.updateProgressToDismiss();
                    return true;
                }

                @Override // android.view.GestureDetector.OnGestureListener
                public void onShowPress(MotionEvent motionEvent) {
                }

                @Override // android.view.GestureDetector.OnGestureListener
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    StoryViewer storyViewer = StoryViewer.this;
                    if (storyViewer.selfStoriesViewsOffset == 0.0f && storyViewer.allowIntercept) {
                        if (storyViewer.keyboardVisible || storyViewer.isCaption || StoryViewer.this.isCaptionPartVisible || StoryViewer.this.isHintVisible || StoryViewer.this.isInTextSelectionMode) {
                            StoryViewer.this.closeKeyboardOrEmoji();
                        } else {
                            StoryViewer.this.switchByTap(motionEvent.getX() > ((float) StoryViewer.this.containerView.getMeasuredWidth()) * 0.33f);
                        }
                    }
                    return false;
                }
            });
            this.windowView = new 2(context, lastFragment);
        }
        if (this.containerView == null) {
            this.containerView = new HwFrameLayout(context) { // from class: org.telegram.ui.Stories.StoryViewer.3
                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    PeerStoriesView currentPeerView = StoryViewer.this.storiesViewPager.getCurrentPeerView();
                    StoryViewer storyViewer = StoryViewer.this;
                    SelfStoryViewsView selfStoryViewsView = storyViewer.selfStoryViewsView;
                    if (selfStoryViewsView != null && currentPeerView != null) {
                        selfStoryViewsView.setOffset(storyViewer.selfStoriesViewsOffset);
                        StoryViewer storyViewer2 = StoryViewer.this;
                        storyViewer2.storiesViewPager.setVisibility(storyViewer2.selfStoryViewsView.progressToOpen == 1.0f ? 4 : 0);
                        StoryViewer.this.storiesViewPager.checkPageVisibility();
                        float top = currentPeerView.getTop() + currentPeerView.storyContainer.getTop();
                        float f = StoryViewer.this.selfStoryViewsView.progressToOpen;
                        getMeasuredHeight();
                        float f2 = StoryViewer.this.selfStoriesViewsOffset;
                        getMeasuredHeight();
                        if (currentPeerView.storyContainer.getMeasuredHeight() > 0) {
                            StoryViewer.this.lastStoryContainerHeight = currentPeerView.storyContainer.getMeasuredHeight();
                        }
                        StoryViewer storyViewer3 = StoryViewer.this;
                        float lerp = AndroidUtilities.lerp(1.0f, storyViewer3.selfStoryViewsView.toHeight / storyViewer3.lastStoryContainerHeight, f);
                        StoryViewer.this.storiesViewPager.setPivotY(top);
                        StoryViewer.this.storiesViewPager.setPivotX(getMeasuredWidth() / 2.0f);
                        StoryViewer.this.storiesViewPager.setScaleX(lerp);
                        StoryViewer.this.storiesViewPager.setScaleY(lerp);
                        currentPeerView.forceUpdateOffsets = true;
                        StoryViewer storyViewer4 = StoryViewer.this;
                        if (storyViewer4.selfStoriesViewsOffset == 0.0f) {
                            currentPeerView.setViewsThumbImageReceiver(0.0f, 0.0f, 0.0f, null);
                        } else {
                            currentPeerView.setViewsThumbImageReceiver(f, lerp, top, storyViewer4.selfStoryViewsView.getCrossfadeToImage());
                        }
                        currentPeerView.invalidate();
                        if (Build.VERSION.SDK_INT >= 21) {
                            currentPeerView.outlineProvider.radiusInDp = (int) AndroidUtilities.lerp(10.0f, 6.0f / r5, StoryViewer.this.selfStoryViewsView.progressToOpen);
                            currentPeerView.storyContainer.invalidateOutline();
                        }
                        StoryViewer storyViewer5 = StoryViewer.this;
                        storyViewer5.storiesViewPager.setTranslationY((storyViewer5.selfStoryViewsView.toY - top) * f);
                    }
                    if (currentPeerView != null) {
                        StoryViewer.this.volumeControl.setTranslationY(currentPeerView.storyContainer.getY() - AndroidUtilities.dp(4.0f));
                    }
                    super.dispatchDraw(canvas);
                }

                public int measureKeyboardHeight() {
                    View rootView = getRootView();
                    Rect rect = AndroidUtilities.rectTmp2;
                    getWindowVisibleDisplayFrame(rect);
                    if (rect.bottom == 0 && rect.top == 0) {
                        return 0;
                    }
                    return Math.max(0, ((rootView.getHeight() - (rect.top != 0 ? AndroidUtilities.statusBarHeight : 0)) - AndroidUtilities.getViewInset(rootView)) - (rect.bottom - rect.top));
                }

                @Override // android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i3, int i4) {
                    int size = View.MeasureSpec.getSize(i4);
                    StoryViewer storyViewer = StoryViewer.this;
                    if (!storyViewer.ATTACH_TO_FRAGMENT) {
                        storyViewer.setKeyboardHeightFromParent(measureKeyboardHeight());
                        size += StoryViewer.this.realKeyboardHeight;
                    }
                    int size2 = View.MeasureSpec.getSize(i3);
                    int i5 = (int) ((size2 * 16.0f) / 9.0f);
                    if (size > i5) {
                        StoryViewer.this.storiesViewPager.getLayoutParams().width = -1;
                        size = i5;
                    } else {
                        int i6 = (int) ((size / 16.0f) * 9.0f);
                        StoryViewer.this.storiesViewPager.getLayoutParams().width = i6;
                        size2 = i6;
                    }
                    StoryViewer.this.aspectRatioFrameLayout.getLayoutParams().height = size + 1;
                    StoryViewer.this.aspectRatioFrameLayout.getLayoutParams().width = size2;
                    ((FrameLayout.LayoutParams) StoryViewer.this.aspectRatioFrameLayout.getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight;
                    super.onMeasure(i3, i4);
                }
            };
            HwStoriesViewPager hwStoriesViewPager = new HwStoriesViewPager(context, this, this.resourcesProvider) { // from class: org.telegram.ui.Stories.StoryViewer.4
                @Override // org.telegram.ui.Stories.StoriesViewPager
                public void onStateChanged() {
                    StoryViewer storyViewer = StoryViewer.this;
                    if (storyViewer.storiesViewPager.currentState == 1) {
                        AndroidUtilities.cancelRunOnUIThread(storyViewer.longPressRunnable);
                    }
                }
            };
            this.storiesViewPager = hwStoriesViewPager;
            hwStoriesViewPager.setDelegate(new 5(storiesList, arrayList));
            this.containerView.addView(this.storiesViewPager, LayoutHelper.createFrame(-1, -1, 1));
            this.aspectRatioFrameLayout = new AspectRatioFrameLayout(context);
            if (this.USE_SURFACE_VIEW) {
                SurfaceView surfaceView = new SurfaceView(context);
                this.surfaceView = surfaceView;
                surfaceView.setZOrderMediaOverlay(false);
                this.surfaceView.setZOrderOnTop(false);
                this.aspectRatioFrameLayout.addView(this.surfaceView);
            } else {
                HwTextureView hwTextureView = new HwTextureView(context) { // from class: org.telegram.ui.Stories.StoryViewer.6
                    @Override // org.telegram.ui.Stories.HwTextureView, android.view.View
                    public void invalidate() {
                        super.invalidate();
                        PeerStoriesView.VideoPlayerSharedScope videoPlayerSharedScope = StoryViewer.this.currentPlayerScope;
                        if (videoPlayerSharedScope != null) {
                            videoPlayerSharedScope.invalidate();
                        }
                    }
                };
                this.textureView = hwTextureView;
                this.aspectRatioFrameLayout.addView(hwTextureView);
            }
            StoriesVolumeControl storiesVolumeControl = new StoriesVolumeControl(context);
            this.volumeControl = storiesVolumeControl;
            this.containerView.addView(storiesVolumeControl, LayoutHelper.createFrame(-1, -1.0f, 0, 4.0f, 0.0f, 4.0f, 0.0f));
        }
        AndroidUtilities.removeFromParent(this.aspectRatioFrameLayout);
        this.windowView.addView(this.aspectRatioFrameLayout);
        SurfaceView surfaceView2 = this.surfaceView;
        if (surfaceView2 != null) {
            surfaceView2.setVisibility(4);
        }
        AndroidUtilities.removeFromParent(this.containerView);
        this.windowView.addView(this.containerView);
        this.windowView.setClipChildren(false);
        if (this.ATTACH_TO_FRAGMENT && (lastFragment.getParentActivity() instanceof LaunchActivity)) {
            ((LaunchActivity) lastFragment.getParentActivity()).requestCustomNavigationBar();
        }
        if (this.isSingleStory) {
            updateTransitionParams();
        }
        if (storiesList != null) {
            this.storiesViewPager.setDays(storiesList.dialogId, storiesList.getDays(), this.currentAccount);
        } else {
            this.storiesViewPager.setPeerIds(arrayList, this.currentAccount, i);
        }
        this.windowManager = (WindowManager) context.getSystemService("window");
        if (lastFragment == null || lastFragment.getLayoutContainer() == null) {
            this.ATTACH_TO_FRAGMENT = false;
        }
        if (this.ATTACH_TO_FRAGMENT) {
            AndroidUtilities.removeFromParent(this.windowView);
            this.windowView.setFitsSystemWindows(true);
            lastFragment.getLayoutContainer().addView(this.windowView);
            AndroidUtilities.requestAdjustResize(lastFragment.getParentActivity(), lastFragment.getClassGuid());
        } else {
            this.windowView.setFocusable(false);
            this.containerView.setFocusable(false);
            if (i2 >= 21) {
                this.windowView.setFitsSystemWindows(true);
                this.containerView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: org.telegram.ui.Stories.StoryViewer.7
                    @Override // android.view.View.OnApplyWindowInsetsListener
                    public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                        int systemWindowInsetTop;
                        int systemWindowInsetBottom;
                        int systemWindowInsetLeft;
                        int systemWindowInsetRight;
                        WindowInsets consumeSystemWindowInsets;
                        WindowInsets windowInsets2;
                        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) StoryViewer.this.containerView.getLayoutParams();
                        systemWindowInsetTop = windowInsets.getSystemWindowInsetTop();
                        marginLayoutParams.topMargin = systemWindowInsetTop;
                        systemWindowInsetBottom = windowInsets.getSystemWindowInsetBottom();
                        marginLayoutParams.bottomMargin = systemWindowInsetBottom;
                        systemWindowInsetLeft = windowInsets.getSystemWindowInsetLeft();
                        marginLayoutParams.leftMargin = systemWindowInsetLeft;
                        systemWindowInsetRight = windowInsets.getSystemWindowInsetRight();
                        marginLayoutParams.rightMargin = systemWindowInsetRight;
                        StoryViewer.this.windowView.requestLayout();
                        StoryViewer.this.containerView.requestLayout();
                        if (Build.VERSION.SDK_INT >= 30) {
                            windowInsets2 = WindowInsets.CONSUMED;
                            return windowInsets2;
                        }
                        consumeSystemWindowInsets = windowInsets.consumeSystemWindowInsets();
                        return consumeSystemWindowInsets;
                    }
                });
                this.containerView.setSystemUiVisibility(1792);
            }
            AndroidUtilities.setPreferredMaxRefreshRate(this.windowManager, this.windowView, this.windowLayoutParams);
            this.windowManager.addView(this.windowView, this.windowLayoutParams);
        }
        this.windowView.requestLayout();
        runOpenAnimationAfterLayout = true;
        updateTransitionParams();
        this.progressToOpen = 0.0f;
        checkNavBarColor();
        animationInProgress = true;
        checkInSilentMode();
        if (this.ATTACH_TO_FRAGMENT) {
            lockOrientation(true);
        }
        if (!this.ATTACH_TO_FRAGMENT) {
            globalInstances.add(this);
        }
        AndroidUtilities.hideKeyboard(lastFragment.getFragmentView());
    }

    public void open(Context context, TL_stories.StoryItem storyItem, PlaceProvider placeProvider) {
        if (storyItem == null) {
            return;
        }
        int i = UserConfig.selectedAccount;
        this.currentAccount = i;
        if (storyItem.dialogId <= 0 || MessagesController.getInstance(i).getUser(Long.valueOf(storyItem.dialogId)) != null) {
            if (storyItem.dialogId >= 0 || MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-storyItem.dialogId)) != null) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(Long.valueOf(storyItem.dialogId));
                open(context, storyItem, arrayList, 0, null, null, placeProvider, false);
            }
        }
    }

    public void openFor(BaseFragment baseFragment, RecyclerListView recyclerListView, ChatActionCell chatActionCell) {
        MessageObject messageObject = chatActionCell.getMessageObject();
        if (baseFragment == null || baseFragment.getContext() == null || messageObject.type != 24) {
            return;
        }
        TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
        TL_stories.StoryItem storyItem = messageMedia.storyItem;
        storyItem.dialogId = DialogObject.getPeerDialogId(messageMedia.peer);
        storyItem.messageId = messageObject.getId();
        open(baseFragment.getContext(), messageObject.messageOwner.media.storyItem, StoriesListPlaceProvider.of(recyclerListView));
    }

    public void openViews() {
        checkSelfStoriesView();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoryViewer$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                StoryViewer.this.lambda$openViews$9();
            }
        }, 30L);
    }

    public void presentFragment(BaseFragment baseFragment) {
        BaseFragment lastFragment = LaunchActivity.getLastFragment();
        if (lastFragment == null) {
            return;
        }
        boolean z = this.ATTACH_TO_FRAGMENT;
        lastFragment.presentFragment(baseFragment);
        if (z) {
            return;
        }
        close(false);
    }

    public void release() {
        this.lastUri = null;
        setInTouchMode(false);
        allowScreenshots(true);
        VideoPlayerHolder videoPlayerHolder = this.playerHolder;
        if (videoPlayerHolder != null) {
            videoPlayerHolder.release(null);
            this.playerHolder = null;
        }
        for (int i = 0; i < this.preparedPlayers.size(); i++) {
            ((VideoPlayerHolder) this.preparedPlayers.get(i)).release(null);
        }
        this.preparedPlayers.clear();
        MessagesController.getInstance(this.currentAccount).getStoriesController().stopAllPollers();
        if (this.ATTACH_TO_FRAGMENT) {
            lockOrientation(false);
            BaseFragment baseFragment = this.fragment;
            if (baseFragment != null) {
                baseFragment.removeSheet(this);
            }
        }
        globalInstances.remove(this);
        this.doOnAnimationReadyRunnables.clear();
        this.selfStoriesViewsOffset = 0.0f;
        lastStoryItem = null;
    }

    public void saveDraft(long j, TL_stories.StoryItem storyItem, CharSequence charSequence) {
        if (j == 0 || storyItem == null) {
            return;
        }
        replyDrafts.put(draftHash(j, storyItem), charSequence);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void setKeyboardHeightFromParent(int i) {
        if (this.realKeyboardHeight != i) {
            this.realKeyboardHeight = i;
            this.storiesViewPager.setKeyboardHeight(i);
            this.storiesViewPager.requestLayout();
            SelfStoryViewsView selfStoryViewsView = this.selfStoryViewsView;
            if (selfStoryViewsView != null) {
                selfStoryViewsView.setKeyboardHeight(i);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public /* synthetic */ void setLastVisible(boolean z) {
        BaseFragment.AttachedSheet.-CC.$default$setLastVisible(this, z);
    }

    public void setOnCloseListener(Runnable runnable) {
        this.onCloseListener = runnable;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void setOnDismissListener(Runnable runnable) {
    }

    public void setOverlayVisible(boolean z) {
        this.isOverlayVisible = z;
        updatePlayingMode();
    }

    public void setSelfStoriesViewsOffset(float f) {
        this.selfStoriesViewsOffset = f;
        PeerStoriesView currentPeerView = this.storiesViewPager.getCurrentPeerView();
        if (currentPeerView != null) {
            currentPeerView.invalidate();
        }
        HwFrameLayout hwFrameLayout = this.containerView;
        if (hwFrameLayout != null) {
            hwFrameLayout.invalidate();
        }
    }

    public void setSpeed(float f) {
        currentSpeed = f;
        VideoPlayerHolder videoPlayerHolder = this.playerHolder;
        if (videoPlayerHolder != null) {
            videoPlayerHolder.setSpeed(f);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public boolean showDialog(Dialog dialog) {
        try {
            this.currentDialog = dialog;
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stories.StoryViewer$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    StoryViewer.this.lambda$showDialog$2(dialogInterface);
                }
            });
            dialog.show();
            updatePlayingMode();
            return true;
        } catch (Throwable th) {
            FileLog.e(th);
            this.currentDialog = null;
            return false;
        }
    }

    public void showViewsAfterOpening() {
        this.showViewsAfterOpening = true;
    }

    public boolean soundEnabled() {
        return !isInSilentMode;
    }

    public void startActivityForResult(Intent intent, int i) {
        if (this.fragment.getParentActivity() == null) {
            return;
        }
        this.fragment.getParentActivity().startActivityForResult(intent, i);
    }

    public void switchByTap(boolean z) {
        PeerStoriesView currentPeerView = this.storiesViewPager.getCurrentPeerView();
        if (currentPeerView == null || currentPeerView.switchToNext(z)) {
            return;
        }
        if (this.storiesViewPager.switchToNext(z)) {
            this.storiesViewPager.lockTouchEvent(150L);
            return;
        }
        if (z) {
            close(true);
            return;
        }
        VideoPlayerHolder videoPlayerHolder = this.playerHolder;
        if (videoPlayerHolder != null) {
            videoPlayerHolder.loopBack();
        }
    }

    public void toggleSilentMode() {
        boolean z = isInSilentMode;
        isInSilentMode = !z;
        VideoPlayerHolder videoPlayerHolder = this.playerHolder;
        if (videoPlayerHolder != null) {
            videoPlayerHolder.setAudioEnabled(z, false);
        }
        for (int i = 0; i < this.preparedPlayers.size(); i++) {
            ((VideoPlayerHolder) this.preparedPlayers.get(i)).setAudioEnabled(!isInSilentMode, true);
        }
        PeerStoriesView currentPeerView = this.storiesViewPager.getCurrentPeerView();
        if (currentPeerView != null) {
            currentPeerView.sharedResources.setIconMuted(!soundEnabled(), true);
        }
        if (isInSilentMode) {
            return;
        }
        this.volumeControl.unmute();
    }

    public void updatePlayingMode() {
        if (this.storiesViewPager == null) {
            return;
        }
        boolean isPaused = isPaused();
        if (this.ATTACH_TO_FRAGMENT && (this.fragment.isPaused() || !this.fragment.isLastFragment())) {
            isPaused = true;
        }
        if (ArticleViewer.getInstance().isVisible()) {
            isPaused = true;
        }
        this.storiesViewPager.setPaused(isPaused);
        VideoPlayerHolder videoPlayerHolder = this.playerHolder;
        if (videoPlayerHolder != null) {
            if (isPaused) {
                videoPlayerHolder.pause();
            } else {
                videoPlayerHolder.play(currentSpeed);
            }
        }
        this.storiesViewPager.enableTouch((this.keyboardVisible || this.isClosed || this.isRecording || this.isLongpressed || this.isInPinchToZoom || this.selfStoriesViewsOffset != 0.0f || this.isInTextSelectionMode) ? false : true);
    }
}
