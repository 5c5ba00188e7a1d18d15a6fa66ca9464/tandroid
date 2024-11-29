package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.TransitionValues;
import android.util.Property;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import androidx.core.view.WindowInsetsCompat$$ExternalSyntheticApiModelOutline0;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.TextSelectionHelper;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PlayPauseDrawable;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.Scroller;
import org.telegram.ui.Components.TimerParticles;
import org.telegram.ui.Components.TranslateAlert2;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.ui.Components.VideoPlayerSeekBar;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.SecretMediaViewer;
import org.telegram.ui.Stories.DarkThemeResourceProvider;
import org.telegram.ui.Stories.recorder.HintView2;

/* loaded from: classes4.dex */
public class SecretMediaViewer implements NotificationCenter.NotificationCenterDelegate, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private static volatile SecretMediaViewer Instance;
    private ActionBar actionBar;
    private int[] animateFromRadius;
    private float animateToClipBottom;
    private float animateToClipBottomOrigin;
    private float animateToClipHorizontal;
    private float animateToClipTop;
    private float animateToClipTopOrigin;
    private boolean animateToRadius;
    private float animateToScale;
    private float animateToX;
    private float animateToY;
    private long animationStartTime;
    private float animationValue;
    private AspectRatioFrameLayout aspectRatioFrameLayout;
    private FrameLayout captionContainer;
    private boolean captionHwLayerEnabled;
    private PhotoViewer.CaptionScrollView captionScrollView;
    private PhotoViewer.CaptionTextViewSwitcher captionTextViewSwitcher;
    private float clipBottom;
    private float clipBottomOrigin;
    private float clipHorizontal;
    private float clipTop;
    private float clipTopOrigin;
    private boolean closeAfterAnimation;
    private long closeTime;
    private boolean closeVideoAfterWatch;
    private FrameLayoutDrawer containerView;
    private int currentAccount;
    private AnimatorSet currentActionBarAnimation;
    private long currentDialogId;
    private MessageObject currentMessageObject;
    private PhotoViewer.PhotoViewerProvider currentProvider;
    private float[] currentRadii;
    private ImageReceiver.BitmapHolder currentThumb;
    private boolean disableShowCheck;
    private boolean discardTap;
    private boolean doubleTap;
    private float dragY;
    private boolean draggingDown;
    private GestureDetector gestureDetector;
    private boolean ignoreDelete;
    private AnimatorSet imageMoveAnimation;
    private boolean invalidCoords;
    private boolean isPhotoVisible;
    private boolean isPlaying;
    private boolean isVideo;
    private boolean isVisible;
    private Object lastInsets;
    private float maxX;
    private float maxY;
    private float minX;
    private float minY;
    private float moveStartX;
    private float moveStartY;
    private boolean moving;
    private View navigationBar;
    private Runnable onClose;
    private long openTime;
    private Activity parentActivity;
    private Runnable photoAnimationEndRunnable;
    private int photoAnimationInProgress;
    private long photoTransitionAnimationStartTime;
    private float pinchCenterX;
    private float pinchCenterY;
    private float pinchStartDistance;
    private float pinchStartX;
    private float pinchStartY;
    private ImageView playButton;
    private PlayPauseDrawable playButtonDrawable;
    private boolean playButtonShown;
    private int playerRetryPlayCount;
    private Scroller scroller;
    private SecretDeleteTimer secretDeleteTimer;
    private HintView2 secretHint;
    private VideoPlayerSeekBar seekbar;
    private View seekbarBackground;
    private VideoPlayerControlFrameLayout seekbarContainer;
    private View seekbarView;
    private TextSelectionHelper.SimpleTextSelectionHelper textSelectionHelper;
    private boolean textureUploaded;
    private float translationX;
    private float translationY;
    private boolean useOvershootForScale;
    private VelocityTracker velocityTracker;
    private float videoCrossfadeAlpha;
    private long videoCrossfadeAlphaLastTime;
    private boolean videoCrossfadeStarted;
    private int videoHeight;
    private VideoPlayer videoPlayer;
    private SimpleTextView videoPlayerTime;
    private TextureView videoTextureView;
    private boolean videoWatchedOneTime;
    private int videoWidth;
    private boolean wasLightNavigationBar;
    private int wasNavigationBarColor;
    private WindowManager.LayoutParams windowLayoutParams;
    private FrameLayout windowView;
    private boolean zoomAnimation;
    private boolean zooming;
    private ImageReceiver centerImage = new ImageReceiver();
    private int[] coords = new int[2];
    private boolean isActionBarVisible = true;
    private PhotoBackgroundDrawable photoBackgroundDrawable = new PhotoBackgroundDrawable(-16777216);
    private Paint blackPaint = new Paint();
    private float scale = 1.0f;
    private DecelerateInterpolator interpolator = new DecelerateInterpolator(1.5f);
    private float pinchStartScale = 1.0f;
    private boolean canDragDown = true;
    private final Runnable updateProgressRunnable = new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda2
        @Override // java.lang.Runnable
        public final void run() {
            SecretMediaViewer.this.lambda$new$0();
        }
    };
    private final int[] videoPlayerCurrentTime = new int[2];
    private final int[] videoPlayerTotalTime = new int[2];
    private final Runnable hideActionBarRunnable = new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            SecretMediaViewer.this.lambda$new$6();
        }
    };
    private Path roundRectPath = new Path();
    public final Property VIDEO_CROSSFADE_ALPHA = new AnimationProperties.FloatProperty("videoCrossfadeAlpha") { // from class: org.telegram.ui.SecretMediaViewer.16
        @Override // android.util.Property
        public Float get(SecretMediaViewer secretMediaViewer) {
            return Float.valueOf(secretMediaViewer.getVideoCrossfadeAlpha());
        }

        @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
        public void setValue(SecretMediaViewer secretMediaViewer, float f) {
            secretMediaViewer.setVideoCrossfadeAlpha(f);
        }
    };
    public final Property ANIMATION_VALUE = new AnimationProperties.FloatProperty("animationValue") { // from class: org.telegram.ui.SecretMediaViewer.20
        @Override // android.util.Property
        public Float get(SecretMediaViewer secretMediaViewer) {
            return Float.valueOf(secretMediaViewer.getAnimationValue());
        }

        @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
        public void setValue(SecretMediaViewer secretMediaViewer, float f) {
            secretMediaViewer.setAnimationValue(f);
        }
    };

    class 11 extends Fade {
        final /* synthetic */ boolean val$isCaptionEmpty;
        final /* synthetic */ boolean val$isCurrentCaptionEmpty;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        11(int i, boolean z, boolean z2) {
            super(i);
            this.val$isCurrentCaptionEmpty = z;
            this.val$isCaptionEmpty = z2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAppear$0(ValueAnimator valueAnimator) {
            SecretMediaViewer.this.captionScrollView.backgroundAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            SecretMediaViewer.this.captionScrollView.invalidate();
        }

        @Override // android.transition.Fade, android.transition.Visibility
        public Animator onAppear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
            Animator onAppear = super.onAppear(viewGroup, view, transitionValues, transitionValues2);
            if (this.val$isCurrentCaptionEmpty && !this.val$isCaptionEmpty && view == SecretMediaViewer.this.captionTextViewSwitcher) {
                onAppear.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SecretMediaViewer.11.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        SecretMediaViewer.this.captionScrollView.backgroundAlpha = 1.0f;
                    }
                });
                ((ObjectAnimator) onAppear).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SecretMediaViewer$11$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        SecretMediaViewer.11.this.lambda$onAppear$0(valueAnimator);
                    }
                });
            }
            return onAppear;
        }
    }

    class 12 extends Fade {
        final /* synthetic */ boolean val$isCaptionEmpty;
        final /* synthetic */ boolean val$isCurrentCaptionEmpty;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        12(int i, boolean z, boolean z2) {
            super(i);
            this.val$isCurrentCaptionEmpty = z;
            this.val$isCaptionEmpty = z2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDisappear$0(ValueAnimator valueAnimator) {
            SecretMediaViewer.this.captionScrollView.backgroundAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            SecretMediaViewer.this.captionScrollView.invalidate();
        }

        @Override // android.transition.Fade, android.transition.Visibility
        public Animator onDisappear(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
            Animator onDisappear = super.onDisappear(viewGroup, view, transitionValues, transitionValues2);
            if (!this.val$isCurrentCaptionEmpty && this.val$isCaptionEmpty && view == SecretMediaViewer.this.captionTextViewSwitcher) {
                onDisappear.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SecretMediaViewer.12.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        SecretMediaViewer.this.captionScrollView.setVisibility(4);
                        SecretMediaViewer.this.captionScrollView.backgroundAlpha = 1.0f;
                    }
                });
                ((ObjectAnimator) onDisappear).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SecretMediaViewer$12$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        SecretMediaViewer.12.this.lambda$onDisappear$0(valueAnimator);
                    }
                });
            }
            return onDisappear;
        }
    }

    class 13 extends Transition {
        13() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createAnimator$0(ValueAnimator valueAnimator) {
            SecretMediaViewer.this.captionScrollView.scrollTo(0, ((Integer) valueAnimator.getAnimatedValue()).intValue());
        }

        @Override // android.transition.Transition
        public void captureEndValues(TransitionValues transitionValues) {
            if (transitionValues.view == SecretMediaViewer.this.captionTextViewSwitcher) {
                transitionValues.values.put("translationY", Integer.valueOf(SecretMediaViewer.this.captionScrollView.getPendingMarginTopDiff()));
            }
        }

        @Override // android.transition.Transition
        public void captureStartValues(TransitionValues transitionValues) {
            if (transitionValues.view == SecretMediaViewer.this.captionScrollView) {
                transitionValues.values.put("scrollY", Integer.valueOf(SecretMediaViewer.this.captionScrollView.getScrollY()));
            }
        }

        @Override // android.transition.Transition
        public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
            int intValue;
            if (transitionValues.view == SecretMediaViewer.this.captionScrollView) {
                ValueAnimator ofInt = ValueAnimator.ofInt(((Integer) transitionValues.values.get("scrollY")).intValue(), 0);
                ofInt.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SecretMediaViewer.13.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        SecretMediaViewer.this.captionTextViewSwitcher.getNextView().setText((CharSequence) null);
                        SecretMediaViewer.this.captionScrollView.applyPendingTopMargin();
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animator) {
                        SecretMediaViewer.this.captionScrollView.stopScrolling();
                    }
                });
                ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.SecretMediaViewer$13$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        SecretMediaViewer.13.this.lambda$createAnimator$0(valueAnimator);
                    }
                });
                return ofInt;
            }
            if (transitionValues2.view != SecretMediaViewer.this.captionTextViewSwitcher || (intValue = ((Integer) transitionValues2.values.get("translationY")).intValue()) == 0) {
                return null;
            }
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(SecretMediaViewer.this.captionTextViewSwitcher, (Property<PhotoViewer.CaptionTextViewSwitcher, Float>) View.TRANSLATION_Y, 0.0f, intValue);
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SecretMediaViewer.13.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    SecretMediaViewer.this.captionTextViewSwitcher.setTranslationY(0.0f);
                }
            });
            return ofFloat;
        }
    }

    class 17 extends AnimatorListenerAdapter {
        final /* synthetic */ PhotoViewer.PlaceProviderObject val$object;

        17(PhotoViewer.PlaceProviderObject placeProviderObject) {
            this.val$object = placeProviderObject;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0() {
            if (SecretMediaViewer.this.photoAnimationEndRunnable != null) {
                SecretMediaViewer.this.photoAnimationEndRunnable.run();
                SecretMediaViewer.this.photoAnimationEndRunnable = null;
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            PhotoViewer.PlaceProviderObject placeProviderObject = this.val$object;
            if (placeProviderObject != null) {
                placeProviderObject.imageReceiver.setVisible(true, true);
            }
            SecretMediaViewer.this.isVisible = false;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$17$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SecretMediaViewer.17.this.lambda$onAnimationEnd$0();
                }
            });
        }
    }

    class 2 implements VideoPlayer.VideoPlayerDelegate {
        final /* synthetic */ File val$file;

        2(File file) {
            this.val$file = file;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onError$0(File file) {
            SecretMediaViewer.this.preparePlayer(file);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onError(VideoPlayer videoPlayer, Exception exc) {
            if (SecretMediaViewer.this.playerRetryPlayCount <= 0) {
                FileLog.e(exc);
                return;
            }
            SecretMediaViewer.access$1510(SecretMediaViewer.this);
            final File file = this.val$file;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SecretMediaViewer.2.this.lambda$onError$0(file);
                }
            }, 100L);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onRenderedFirstFrame() {
            if (SecretMediaViewer.this.textureUploaded) {
                return;
            }
            SecretMediaViewer.this.textureUploaded = true;
            SecretMediaViewer.this.containerView.invalidate();
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* synthetic */ void onRenderedFirstFrame(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.-CC.$default$onRenderedFirstFrame(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* synthetic */ void onSeekFinished(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.-CC.$default$onSeekFinished(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public /* synthetic */ void onSeekStarted(AnalyticsListener.EventTime eventTime) {
            VideoPlayer.VideoPlayerDelegate.-CC.$default$onSeekStarted(this, eventTime);
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onStateChanged(boolean z, int i) {
            if (SecretMediaViewer.this.videoPlayer == null || SecretMediaViewer.this.currentMessageObject == null) {
                return;
            }
            AndroidUtilities.cancelRunOnUIThread(SecretMediaViewer.this.updateProgressRunnable);
            AndroidUtilities.runOnUIThread(SecretMediaViewer.this.updateProgressRunnable);
            try {
                if (i == 4 || i == 1) {
                    SecretMediaViewer.this.parentActivity.getWindow().clearFlags(128);
                } else {
                    SecretMediaViewer.this.parentActivity.getWindow().addFlags(128);
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (i == 3 && SecretMediaViewer.this.aspectRatioFrameLayout.getVisibility() != 0) {
                SecretMediaViewer.this.aspectRatioFrameLayout.setVisibility(0);
            }
            if (SecretMediaViewer.this.videoPlayer.isPlaying() && i != 4) {
                if (SecretMediaViewer.this.isPlaying) {
                    return;
                }
                SecretMediaViewer.this.isPlaying = true;
            } else if (SecretMediaViewer.this.isPlaying) {
                SecretMediaViewer.this.isPlaying = false;
                if (i == 4) {
                    SecretMediaViewer.this.videoWatchedOneTime = true;
                    if (SecretMediaViewer.this.closeVideoAfterWatch) {
                        SecretMediaViewer.this.closePhoto(true, !r4.ignoreDelete);
                    } else {
                        SecretMediaViewer.this.videoPlayer.seekTo(0L);
                        SecretMediaViewer.this.videoPlayer.play();
                    }
                }
            }
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onVideoSizeChanged(int i, int i2, int i3, float f) {
            if (SecretMediaViewer.this.aspectRatioFrameLayout != null) {
                if (i3 != 90 && i3 != 270) {
                    i2 = i;
                    i = i2;
                }
                SecretMediaViewer.this.aspectRatioFrameLayout.setAspectRatio(i == 0 ? 1.0f : (i2 * f) / i, i3);
            }
        }
    }

    private class FrameLayoutDrawer extends FrameLayout {
        public FrameLayoutDrawer(Context context) {
            super(context);
            setWillNotDraw(false);
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            return view != SecretMediaViewer.this.aspectRatioFrameLayout && super.drawChild(canvas, view, j);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            SecretMediaViewer.this.centerImage.onAttachedToWindow();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            SecretMediaViewer.this.centerImage.onDetachedFromWindow();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            SecretMediaViewer.this.onDraw(canvas);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            SecretMediaViewer.this.processTouchEvent(motionEvent);
            return true;
        }
    }

    private class PhotoBackgroundDrawable extends ColorDrawable {
        private Runnable drawRunnable;
        private int frame;

        public PhotoBackgroundDrawable(int i) {
            super(i);
        }

        @Override // android.graphics.drawable.ColorDrawable, android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Runnable runnable;
            super.draw(canvas);
            if (getAlpha() != 0) {
                if (this.frame != 2 || (runnable = this.drawRunnable) == null) {
                    invalidateSelf();
                } else {
                    runnable.run();
                    this.drawRunnable = null;
                }
                this.frame++;
            }
        }

        @Override // android.graphics.drawable.ColorDrawable, android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            if (SecretMediaViewer.this.parentActivity instanceof LaunchActivity) {
                ((LaunchActivity) SecretMediaViewer.this.parentActivity).drawerLayoutContainer.setAllowDrawContent((SecretMediaViewer.this.isPhotoVisible && i == 255) ? false : true);
            }
            super.setAlpha(i);
        }

        @Override // android.graphics.drawable.Drawable
        public void setBounds(int i, int i2, int i3, int i4) {
            super.setBounds(i, i2, i3, i4 + AndroidUtilities.navigationBarHeight);
        }

        @Override // android.graphics.drawable.Drawable
        public void setBounds(Rect rect) {
            rect.bottom += AndroidUtilities.navigationBarHeight;
            super.setBounds(rect);
        }
    }

    private class SecretDeleteTimer extends FrameLayout {
        private Paint afterDeleteProgressPaint;
        private Paint circlePaint;
        private RectF deleteProgressRect;
        private long destroyTime;
        private long destroyTtl;
        private RLottieDrawable drawable;
        private boolean once;
        private StaticLayout onceLayout;
        private float onceLayoutHeight;
        private float onceLayoutWidth;
        private TextPaint oncePaint;
        private Paint particlePaint;
        private TimerParticles timerParticles;
        private boolean useVideoProgress;

        public SecretDeleteTimer(Context context) {
            super(context);
            this.deleteProgressRect = new RectF();
            this.timerParticles = new TimerParticles();
            this.oncePaint = new TextPaint(1);
            setWillNotDraw(false);
            Paint paint = new Paint(1);
            this.particlePaint = paint;
            paint.setStrokeWidth(AndroidUtilities.dp(1.5f));
            this.particlePaint.setColor(-1644826);
            Paint paint2 = this.particlePaint;
            Paint.Cap cap = Paint.Cap.ROUND;
            paint2.setStrokeCap(cap);
            Paint paint3 = this.particlePaint;
            Paint.Style style = Paint.Style.STROKE;
            paint3.setStyle(style);
            Paint paint4 = new Paint(1);
            this.afterDeleteProgressPaint = paint4;
            paint4.setStyle(style);
            this.afterDeleteProgressPaint.setStrokeCap(cap);
            this.afterDeleteProgressPaint.setColor(-1644826);
            this.afterDeleteProgressPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
            Paint paint5 = new Paint(1);
            this.circlePaint = paint5;
            paint5.setColor(2130706432);
            int i = R.raw.fire_on;
            RLottieDrawable rLottieDrawable = new RLottieDrawable(i, "" + i, AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.drawable = rLottieDrawable;
            rLottieDrawable.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
            this.drawable.setMasterParent(this);
            this.drawable.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDestroyTime(long j, long j2, boolean z) {
            this.once = false;
            this.destroyTime = j;
            this.destroyTtl = j2;
            this.useVideoProgress = z;
            this.drawable.start();
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setOnce() {
            this.once = true;
            this.oncePaint.setTextSize(AndroidUtilities.dp(13.0f));
            this.oncePaint.setTypeface(AndroidUtilities.getTypeface("fonts/num.otf"));
            this.oncePaint.setColor(-1);
            StaticLayout staticLayout = new StaticLayout("1", this.oncePaint, 999, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.onceLayout = staticLayout;
            this.onceLayoutWidth = staticLayout.getLineCount() > 0 ? this.onceLayout.getLineWidth(0) : 0.0f;
            this.onceLayoutHeight = this.onceLayout.getHeight();
            invalidate();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float max;
            TimerParticles timerParticles;
            Paint paint;
            RectF rectF;
            float f;
            Canvas canvas2;
            float f2;
            if (SecretMediaViewer.this.currentMessageObject != null) {
                if (SecretMediaViewer.this.currentMessageObject.messageOwner.destroyTime != 0 || SecretMediaViewer.this.currentMessageObject.messageOwner.ttl == Integer.MAX_VALUE) {
                    if (this.useVideoProgress) {
                        if (SecretMediaViewer.this.videoPlayer != null) {
                            long duration = SecretMediaViewer.this.videoPlayer.getDuration();
                            long currentPosition = SecretMediaViewer.this.videoPlayer.getCurrentPosition();
                            if (duration != -9223372036854775807L && currentPosition != -9223372036854775807L) {
                                max = 1.0f - (currentPosition / duration);
                            }
                        }
                        max = 1.0f;
                    } else {
                        if (this.destroyTime != 0) {
                            max = Math.max(0L, this.destroyTime - (System.currentTimeMillis() + (ConnectionsManager.getInstance(SecretMediaViewer.this.currentAccount).getTimeDifference() * 1000))) / (this.destroyTtl * 1000.0f);
                        }
                        max = 1.0f;
                    }
                    if (this.once) {
                        canvas.save();
                        canvas.translate(this.deleteProgressRect.centerX() - (this.onceLayoutWidth / 2.0f), this.deleteProgressRect.centerY() - (this.onceLayoutHeight / 2.0f));
                        this.onceLayout.draw(canvas);
                        canvas.restore();
                        canvas.drawArc(this.deleteProgressRect, 90.0f, 180.0f, false, this.afterDeleteProgressPaint);
                        float f3 = 19.285715f;
                        for (int i = 0; i < 5; i++) {
                            canvas.drawArc(this.deleteProgressRect, f3 + 270.0f, 12.857143f, false, this.afterDeleteProgressPaint);
                            f3 += 32.14286f;
                        }
                        timerParticles = this.timerParticles;
                        paint = this.particlePaint;
                        rectF = this.deleteProgressRect;
                        f2 = 0.0f;
                        f = 1.0f;
                        canvas2 = canvas;
                    } else {
                        float centerX = this.deleteProgressRect.centerX();
                        float centerY = this.deleteProgressRect.centerY() - AndroidUtilities.dp(1.0f);
                        float dp = AndroidUtilities.dp(8.0f);
                        this.drawable.setBounds((int) (centerX - dp), (int) (centerY - dp), (int) (centerX + dp), (int) (centerY + dp));
                        this.drawable.draw(canvas);
                        float f4 = max * (-360.0f);
                        canvas.drawArc(this.deleteProgressRect, -90.0f, f4, false, this.afterDeleteProgressPaint);
                        timerParticles = this.timerParticles;
                        paint = this.particlePaint;
                        rectF = this.deleteProgressRect;
                        f = 1.0f;
                        canvas2 = canvas;
                        f2 = f4;
                    }
                    timerParticles.draw(canvas2, paint, rectF, f2, f);
                    invalidate();
                }
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            float measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(35.0f);
            float measuredHeight = getMeasuredHeight() / 2.0f;
            float dpf2 = AndroidUtilities.dpf2(10.5f);
            this.deleteProgressRect.set(measuredWidth - dpf2, measuredHeight - dpf2, measuredWidth + dpf2, dpf2 + measuredHeight);
            setPivotX(measuredWidth);
            setPivotY(measuredHeight);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class VideoPlayerControlFrameLayout extends FrameLayout {
        public final Property SEEKBAR_ALPHA;
        private boolean ignoreLayout;
        private int lastTimeWidth;
        private int parentHeight;
        private int parentWidth;
        private float progress;
        private boolean seekBarTransitionEnabled;
        private SpringAnimation timeSpring;
        private FloatValueHolder timeValue;
        private boolean translationYAnimationEnabled;

        public VideoPlayerControlFrameLayout(Context context) {
            super(context);
            this.progress = 1.0f;
            this.seekBarTransitionEnabled = true;
            this.translationYAnimationEnabled = true;
            this.timeValue = new FloatValueHolder(0.0f);
            this.timeSpring = (SpringAnimation) new SpringAnimation(this.timeValue).setSpring(new SpringForce(0.0f).setStiffness(750.0f).setDampingRatio(1.0f)).addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.SecretMediaViewer$VideoPlayerControlFrameLayout$$ExternalSyntheticLambda0
                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
                public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                    SecretMediaViewer.VideoPlayerControlFrameLayout.this.lambda$new$0(dynamicAnimation, f, f2);
                }
            });
            this.SEEKBAR_ALPHA = new AnimationProperties.FloatProperty("progress") { // from class: org.telegram.ui.SecretMediaViewer.VideoPlayerControlFrameLayout.1
                @Override // android.util.Property
                public Float get(VideoPlayerControlFrameLayout videoPlayerControlFrameLayout) {
                    return Float.valueOf(videoPlayerControlFrameLayout.getProgress());
                }

                @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
                public void setValue(VideoPlayerControlFrameLayout videoPlayerControlFrameLayout, float f) {
                    videoPlayerControlFrameLayout.setProgress(f);
                }
            };
            setWillNotDraw(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(DynamicAnimation dynamicAnimation, float f, float f2) {
            SecretMediaViewer.this.seekbar.setSize((int) (((getMeasuredWidth() - AndroidUtilities.dp(16.0f)) - f) - (this.parentWidth > this.parentHeight ? AndroidUtilities.dp(48.0f) : 0)), getMeasuredHeight());
        }

        private void onProgressChanged(float f) {
            SecretMediaViewer.this.videoPlayerTime.setAlpha(f);
            if (!this.seekBarTransitionEnabled) {
                if (this.translationYAnimationEnabled) {
                    setTranslationY(AndroidUtilities.dpf2(24.0f) * (1.0f - f));
                }
                SecretMediaViewer.this.seekbarView.setAlpha(f);
                return;
            }
            SecretMediaViewer.this.videoPlayerTime.setPivotX(SecretMediaViewer.this.videoPlayerTime.getWidth());
            SecretMediaViewer.this.videoPlayerTime.setPivotY(SecretMediaViewer.this.videoPlayerTime.getHeight());
            float f2 = 1.0f - f;
            float f3 = 1.0f - (0.1f * f2);
            SecretMediaViewer.this.videoPlayerTime.setScaleX(f3);
            SecretMediaViewer.this.videoPlayerTime.setScaleY(f3);
            SecretMediaViewer.this.seekbar.setTransitionProgress(f2);
        }

        public float getProgress() {
            return this.progress;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.timeValue.setValue(0.0f);
            this.lastTimeWidth = 0;
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            SecretMediaViewer.this.seekbar.setProgress(SecretMediaViewer.this.videoPlayer != null ? SecretMediaViewer.this.videoPlayer.getCurrentPosition() / SecretMediaViewer.this.videoPlayer.getDuration() : 0.0f);
        }

        /* JADX WARN: Code restructure failed: missing block: B:7:0x004d, code lost:
        
            if (r13 == (-9223372036854775807L)) goto L10;
         */
        @Override // android.widget.FrameLayout, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onMeasure(int i, int i2) {
            int i3;
            long j;
            this.ignoreLayout = true;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) SecretMediaViewer.this.videoPlayerTime.getLayoutParams();
            if (this.parentWidth > this.parentHeight) {
                i3 = AndroidUtilities.dp(48.0f);
                layoutParams.rightMargin = AndroidUtilities.dp(47.0f);
            } else {
                layoutParams.rightMargin = AndroidUtilities.dp(12.0f);
                i3 = 0;
            }
            this.ignoreLayout = false;
            super.onMeasure(i, i2);
            if (SecretMediaViewer.this.videoPlayer != null) {
                j = SecretMediaViewer.this.videoPlayer.getDuration();
            }
            j = 0;
            int ceil = (int) Math.ceil(SecretMediaViewer.this.videoPlayerTime.getPaint().measureText(String.format(Locale.ROOT, "%1$s / %1$s", (j / 1000) / 60 > 60 ? String.format(Locale.ROOT, "%02d:%02d:%02d", Long.valueOf(r8 / 60), Long.valueOf(r8 % 60), Long.valueOf(r13 % 60)) : String.format(Locale.ROOT, "%02d:%02d", Long.valueOf(r8), Long.valueOf(r13 % 60)))));
            this.timeSpring.cancel();
            if (this.lastTimeWidth != 0) {
                float f = ceil;
                if (this.timeValue.getValue() != f) {
                    this.timeSpring.getSpring().setFinalPosition(f);
                    this.timeSpring.start();
                    this.lastTimeWidth = ceil;
                }
            }
            SecretMediaViewer.this.seekbar.setSize(((getMeasuredWidth() - AndroidUtilities.dp(16.0f)) - ceil) - i3, getMeasuredHeight());
            this.timeValue.setValue(ceil);
            this.lastTimeWidth = ceil;
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (this.progress < 1.0f) {
                return false;
            }
            if (SecretMediaViewer.this.seekbar.onTouch(motionEvent.getAction(), motionEvent.getX() - AndroidUtilities.dp(2.0f), motionEvent.getY())) {
                getParent().requestDisallowInterceptTouchEvent(true);
                SecretMediaViewer.this.seekbarView.invalidate();
            }
            return true;
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }

        public void setProgress(float f) {
            if (this.progress != f) {
                this.progress = f;
                onProgressChanged(f);
            }
        }
    }

    static /* synthetic */ int access$1510(SecretMediaViewer secretMediaViewer) {
        int i = secretMediaViewer.playerRetryPlayCount;
        secretMediaViewer.playerRetryPlayCount = i - 1;
        return i;
    }

    private void animateTo(float f, float f2, float f3, boolean z) {
        animateTo(f, f2, f3, z, NotificationCenter.didSetNewWallpapper);
    }

    private void animateTo(float f, float f2, float f3, boolean z, int i) {
        if (this.scale == f && this.translationX == f2 && this.translationY == f3) {
            return;
        }
        this.zoomAnimation = z;
        this.animateToScale = f;
        this.animateToX = f2;
        this.animateToY = f3;
        this.animationStartTime = System.currentTimeMillis();
        AnimatorSet animatorSet = new AnimatorSet();
        this.imageMoveAnimation = animatorSet;
        animatorSet.playTogether(ObjectAnimator.ofFloat(this, "animationValue", 0.0f, 1.0f));
        this.imageMoveAnimation.setInterpolator(this.interpolator);
        this.imageMoveAnimation.setDuration(i);
        this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SecretMediaViewer.19
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SecretMediaViewer.this.imageMoveAnimation = null;
                SecretMediaViewer.this.containerView.invalidate();
            }
        });
        this.imageMoveAnimation.start();
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0028, code lost:
    
        if (r2 > r3) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0017, code lost:
    
        if (r2 > r3) goto L4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void checkMinMax(boolean z) {
        float f = this.translationX;
        float f2 = this.translationY;
        updateMinMax(this.scale);
        float f3 = this.translationX;
        float f4 = this.minX;
        if (f3 >= f4) {
            f4 = this.maxX;
        }
        f = f4;
        float f5 = this.translationY;
        float f6 = this.minY;
        if (f5 >= f6) {
            f6 = this.maxY;
        }
        f2 = f6;
        animateTo(this.scale, f, f2, z);
    }

    private boolean checkPhotoAnimation() {
        if (this.photoAnimationInProgress != 0 && Math.abs(this.photoTransitionAnimationStartTime - System.currentTimeMillis()) >= 500) {
            Runnable runnable = this.photoAnimationEndRunnable;
            if (runnable != null) {
                runnable.run();
                this.photoAnimationEndRunnable = null;
            }
            this.photoAnimationInProgress = 0;
        }
        return this.photoAnimationInProgress != 0;
    }

    private int getContainerViewHeight() {
        return this.containerView.getHeight();
    }

    private int getContainerViewWidth() {
        return this.containerView.getWidth();
    }

    public static SecretMediaViewer getInstance() {
        SecretMediaViewer secretMediaViewer = Instance;
        if (secretMediaViewer == null) {
            synchronized (PhotoViewer.class) {
                try {
                    secretMediaViewer = Instance;
                    if (secretMediaViewer == null) {
                        secretMediaViewer = new SecretMediaViewer();
                        Instance = secretMediaViewer;
                    }
                } finally {
                }
            }
        }
        return secretMediaViewer;
    }

    public static boolean hasInstance() {
        return Instance != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closePhoto$7(PhotoViewer.PlaceProviderObject placeProviderObject) {
        this.imageMoveAnimation = null;
        this.photoAnimationInProgress = 0;
        this.containerView.setLayerType(0, null);
        this.containerView.setVisibility(4);
        onPhotoClosed(placeProviderObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closePhoto$8(PhotoViewer.PlaceProviderObject placeProviderObject) {
        FrameLayoutDrawer frameLayoutDrawer = this.containerView;
        if (frameLayoutDrawer == null) {
            return;
        }
        frameLayoutDrawer.setLayerType(0, null);
        this.containerView.setVisibility(4);
        this.photoAnimationInProgress = 0;
        onPhotoClosed(placeProviderObject);
        this.containerView.setScaleX(1.0f);
        this.containerView.setScaleY(1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer == null) {
            return;
        }
        long currentPosition = videoPlayer.getCurrentPosition();
        long duration = this.videoPlayer.getDuration();
        if (duration == -9223372036854775807L) {
            currentPosition = 0;
            duration = 0;
        }
        if (duration > 0 && !this.seekbar.isDragging()) {
            this.seekbar.setProgress(currentPosition / duration);
            this.seekbarView.invalidate();
        }
        updateVideoPlayerTime();
        if (this.videoPlayer.isPlaying()) {
            AndroidUtilities.runOnUIThread(this.updateProgressRunnable, 17L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6() {
        toggleActionBar(false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPhotoClosed$9() {
        ImageReceiver.BitmapHolder bitmapHolder = this.currentThumb;
        if (bitmapHolder != null) {
            bitmapHolder.release();
            this.currentThumb = null;
        }
        this.centerImage.setImageBitmap((Bitmap) null);
        try {
            if (this.windowView.getParent() != null) {
                ((WindowManager) this.parentActivity.getSystemService("window")).removeView(this.windowView);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.isPhotoVisible = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openMedia$3(View view) {
        MessageObject messageObject = this.currentMessageObject;
        if (messageObject != null) {
            TLRPC.Message message = messageObject.messageOwner;
            if (message.destroyTime != 0 || message.ttl == Integer.MAX_VALUE) {
                if (this.secretHint.shown()) {
                    this.secretHint.hide();
                } else {
                    showSecretHint();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openMedia$4(Runnable runnable, MessageObject messageObject) {
        this.photoAnimationInProgress = 0;
        this.imageMoveAnimation = null;
        if (runnable != null) {
            runnable.run();
        }
        FrameLayoutDrawer frameLayoutDrawer = this.containerView;
        if (frameLayoutDrawer == null) {
            return;
        }
        frameLayoutDrawer.setLayerType(0, null);
        this.containerView.invalidate();
        this.secretDeleteTimer.setDestroyTime(messageObject.messageOwner.destroyTimeMillis, r8.ttl, false);
        if (this.closeAfterAnimation) {
            closePhoto(true, true);
        } else {
            if (!this.ignoreDelete || MessagesController.getGlobalMainSettings().getInt("viewoncehint", 0) >= 3) {
                return;
            }
            showSecretHint();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openMedia$5(PhotoViewer.PlaceProviderObject placeProviderObject) {
        this.disableShowCheck = false;
        placeProviderObject.imageReceiver.setVisible(false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x0016, code lost:
    
        if (r2.equals(r0) == false) goto L6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ WindowInsets lambda$setParentActivity$1(View view, WindowInsets windowInsets) {
        WindowInsets consumeSystemWindowInsets;
        WindowInsets windowInsets2;
        String windowInsets3;
        String windowInsets4;
        WindowInsets m = WindowInsetsCompat$$ExternalSyntheticApiModelOutline0.m(this.lastInsets);
        this.lastInsets = windowInsets;
        if (m != null) {
            windowInsets3 = m.toString();
            windowInsets4 = windowInsets.toString();
        }
        this.windowView.requestLayout();
        if (Build.VERSION.SDK_INT >= 30) {
            windowInsets2 = WindowInsets.CONSUMED;
            return windowInsets2;
        }
        consumeSystemWindowInsets = windowInsets.consumeSystemWindowInsets();
        return consumeSystemWindowInsets;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ View lambda$setParentActivity$2(Activity activity) {
        return new PhotoViewer.CaptionTextView(activity, this.captionScrollView, this.textSelectionHelper, new Utilities.Callback2() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda10
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                SecretMediaViewer.this.onLinkClick((ClickableSpan) obj, (TextView) obj2);
            }
        }, new Utilities.Callback3() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda11
            @Override // org.telegram.messenger.Utilities.Callback3
            public final void run(Object obj, Object obj2, Object obj3) {
                SecretMediaViewer.this.onLinkLongPress((URLSpan) obj, (TextView) obj2, (Runnable) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:72:0x02a4  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x02e5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        float f9;
        boolean z;
        float f10;
        float f11;
        boolean z2;
        int bitmapWidth;
        int bitmapHeight;
        int i;
        float f12;
        float f13;
        int i2;
        float f14;
        if (this.isPhotoVisible) {
            if (this.imageMoveAnimation != null) {
                if (!this.scroller.isFinished()) {
                    this.scroller.abortAnimation();
                }
                if (this.useOvershootForScale) {
                    float f15 = this.animationValue;
                    if (f15 < 0.9f) {
                        f14 = f15 / 0.9f;
                        float f16 = this.scale;
                        f = f16 + (((this.animateToScale * 1.02f) - f16) * f14);
                    } else {
                        float f17 = this.animateToScale;
                        f = f17 + (0.01999998f * f17 * (1.0f - ((f15 - 0.9f) / 0.100000024f)));
                        f14 = 1.0f;
                    }
                } else {
                    float f18 = this.scale;
                    float f19 = this.animateToScale - f18;
                    float f20 = this.animationValue;
                    f = (f19 * f20) + f18;
                    f14 = f20;
                }
                float f21 = this.translationY;
                f2 = f21 + ((this.animateToY - f21) * f14);
                float f22 = this.translationX;
                f3 = ((this.animateToX - f22) * f14) + f22;
                float f23 = this.clipTop;
                f4 = f23 + ((this.animateToClipTop - f23) * f14);
                float f24 = this.clipBottom;
                f5 = f24 + ((this.animateToClipBottom - f24) * f14);
                float f25 = this.clipTopOrigin;
                f6 = f25 + ((this.animateToClipTopOrigin - f25) * f14);
                float f26 = this.clipBottomOrigin;
                f7 = f26 + ((this.animateToClipBottomOrigin - f26) * f14);
                float f27 = this.clipHorizontal;
                f8 = f27 + ((this.animateToClipHorizontal - f27) * f14);
                f9 = (this.animateToScale == 1.0f && this.scale == 1.0f && f22 == 0.0f) ? f2 : -1.0f;
                this.containerView.invalidate();
            } else {
                if (this.animationStartTime != 0) {
                    this.translationX = this.animateToX;
                    this.translationY = this.animateToY;
                    this.clipBottom = this.animateToClipBottom;
                    this.clipTop = this.animateToClipTop;
                    this.clipTopOrigin = this.animateToClipTopOrigin;
                    this.clipBottomOrigin = this.animateToClipBottomOrigin;
                    this.clipHorizontal = this.animateToClipHorizontal;
                    float f28 = this.animateToScale;
                    this.scale = f28;
                    this.animationStartTime = 0L;
                    updateMinMax(f28);
                    this.zoomAnimation = false;
                    this.useOvershootForScale = false;
                }
                if (!this.scroller.isFinished() && this.scroller.computeScrollOffset()) {
                    if (this.scroller.getStartX() < this.maxX && this.scroller.getStartX() > this.minX) {
                        this.translationX = this.scroller.getCurrX();
                    }
                    if (this.scroller.getStartY() < this.maxY && this.scroller.getStartY() > this.minY) {
                        this.translationY = this.scroller.getCurrY();
                    }
                    this.containerView.invalidate();
                }
                f = this.scale;
                f2 = this.translationY;
                f3 = this.translationX;
                f4 = this.clipTop;
                f5 = this.clipBottom;
                f6 = this.clipTopOrigin;
                f7 = this.clipBottomOrigin;
                f8 = this.clipHorizontal;
                f9 = !this.moving ? f2 : -1.0f;
            }
            if (this.animateFromRadius != null) {
                if (this.currentRadii == null) {
                    this.currentRadii = new float[8];
                }
                float f29 = this.animateToRadius ? this.animationValue : 1.0f - this.animationValue;
                int i3 = 0;
                z = true;
                for (int i4 = 8; i3 < i4; i4 = 8) {
                    float[] fArr = this.currentRadii;
                    float lerp = AndroidUtilities.lerp(this.animateFromRadius[i3 / 2] * 2.0f, 0.0f, f29);
                    fArr[i3 + 1] = lerp;
                    fArr[i3] = lerp;
                    if (this.currentRadii[i3] > 0.0f) {
                        z = false;
                    }
                    i3 += 2;
                }
            } else {
                z = true;
            }
            if (this.photoAnimationInProgress != 3) {
                if (this.scale != 1.0f || f9 == -1.0f || this.zoomAnimation) {
                    this.photoBackgroundDrawable.setAlpha(NotificationCenter.newLocationAvailable);
                } else {
                    float containerViewHeight = getContainerViewHeight() / 4.0f;
                    this.photoBackgroundDrawable.setAlpha((int) Math.max(127.0f, (1.0f - (Math.min(Math.abs(f9), containerViewHeight) / containerViewHeight)) * 255.0f));
                }
                if (!this.zoomAnimation) {
                    float f30 = this.maxX;
                    if (f3 > f30) {
                        float min = Math.min(1.0f, (f3 - f30) / canvas.getWidth());
                        f11 = 0.3f * min;
                        f10 = 1.0f - min;
                        f3 = this.maxX;
                        AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
                        z2 = aspectRatioFrameLayout == null && aspectRatioFrameLayout.getVisibility() == 0;
                        canvas.save();
                        float f31 = f - f11;
                        canvas.translate((getContainerViewWidth() / 2) + f3, (getContainerViewHeight() / 2) + f2);
                        canvas.scale(f31, f31);
                        bitmapWidth = this.centerImage.getBitmapWidth();
                        bitmapHeight = this.centerImage.getBitmapHeight();
                        i = this.videoWidth;
                        if (i != 0 && (i2 = this.videoHeight) != 0) {
                            bitmapWidth = i;
                            bitmapHeight = i2;
                        }
                        if (z2 && this.textureUploaded && Math.abs((bitmapWidth / bitmapHeight) - (this.videoTextureView.getMeasuredWidth() / this.videoTextureView.getMeasuredHeight())) > 0.01f) {
                            bitmapWidth = this.videoTextureView.getMeasuredWidth();
                            bitmapHeight = this.videoTextureView.getMeasuredHeight();
                        }
                        float f32 = bitmapHeight;
                        float f33 = bitmapWidth;
                        float min2 = Math.min(getContainerViewHeight() / f32, getContainerViewWidth() / f33);
                        int i5 = (int) (f33 * min2);
                        int i6 = (int) (f32 * min2);
                        f12 = (-i5) / 2;
                        float f34 = f8 / f31;
                        float f35 = f12 + f34;
                        f13 = (-i6) / 2;
                        float f36 = (i5 / 2) - f34;
                        float f37 = i6 / 2;
                        canvas.clipRect(f35, (f4 / f31) + f13, f36, f37 - (f5 / f31));
                        if (!z) {
                            this.roundRectPath.reset();
                            RectF rectF = AndroidUtilities.rectTmp;
                            rectF.set(f35, (f6 / f31) + f13, f36, f37 - (f7 / f31));
                            this.roundRectPath.addRoundRect(rectF, this.currentRadii, Path.Direction.CW);
                            canvas.clipPath(this.roundRectPath);
                        }
                        if (z2 || !this.textureUploaded || !this.videoCrossfadeStarted || this.videoCrossfadeAlpha != 1.0f) {
                            this.centerImage.setAlpha(f10);
                            this.centerImage.setImageCoords(f12, f13, i5, i6);
                            this.centerImage.draw(canvas);
                        }
                        if (z2) {
                            if (!this.videoCrossfadeStarted && this.textureUploaded) {
                                this.videoCrossfadeStarted = true;
                                this.videoCrossfadeAlpha = 0.0f;
                                this.videoCrossfadeAlphaLastTime = System.currentTimeMillis();
                            }
                            canvas.translate(f12, f13);
                            this.videoTextureView.setAlpha(f10 * this.videoCrossfadeAlpha);
                            this.aspectRatioFrameLayout.draw(canvas);
                            if (this.videoCrossfadeStarted && this.videoCrossfadeAlpha < 1.0f) {
                                long currentTimeMillis = System.currentTimeMillis();
                                long j = currentTimeMillis - this.videoCrossfadeAlphaLastTime;
                                this.videoCrossfadeAlphaLastTime = currentTimeMillis;
                                this.videoCrossfadeAlpha += j / 200.0f;
                                this.containerView.invalidate();
                                if (this.videoCrossfadeAlpha > 1.0f) {
                                    this.videoCrossfadeAlpha = 1.0f;
                                }
                            }
                        }
                        canvas.restore();
                    }
                }
            }
            f10 = 1.0f;
            f11 = 0.0f;
            AspectRatioFrameLayout aspectRatioFrameLayout2 = this.aspectRatioFrameLayout;
            if (aspectRatioFrameLayout2 == null) {
            }
            canvas.save();
            float f312 = f - f11;
            canvas.translate((getContainerViewWidth() / 2) + f3, (getContainerViewHeight() / 2) + f2);
            canvas.scale(f312, f312);
            bitmapWidth = this.centerImage.getBitmapWidth();
            bitmapHeight = this.centerImage.getBitmapHeight();
            i = this.videoWidth;
            if (i != 0) {
                bitmapWidth = i;
                bitmapHeight = i2;
            }
            if (z2) {
                bitmapWidth = this.videoTextureView.getMeasuredWidth();
                bitmapHeight = this.videoTextureView.getMeasuredHeight();
            }
            float f322 = bitmapHeight;
            float f332 = bitmapWidth;
            float min22 = Math.min(getContainerViewHeight() / f322, getContainerViewWidth() / f332);
            int i52 = (int) (f332 * min22);
            int i62 = (int) (f322 * min22);
            f12 = (-i52) / 2;
            float f342 = f8 / f312;
            float f352 = f12 + f342;
            f13 = (-i62) / 2;
            float f362 = (i52 / 2) - f342;
            float f372 = i62 / 2;
            canvas.clipRect(f352, (f4 / f312) + f13, f362, f372 - (f5 / f312));
            if (!z) {
            }
            if (z2) {
            }
            this.centerImage.setAlpha(f10);
            this.centerImage.setImageCoords(f12, f13, i52, i62);
            this.centerImage.draw(canvas);
            if (z2) {
            }
            canvas.restore();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLinkClick(ClickableSpan clickableSpan, TextView textView) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLinkLongPress(URLSpan uRLSpan, TextView textView, Runnable runnable) {
    }

    private void onPhotoClosed(PhotoViewer.PlaceProviderObject placeProviderObject) {
        this.isVisible = false;
        this.currentProvider = null;
        this.disableShowCheck = false;
        releasePlayer();
        new ArrayList();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                SecretMediaViewer.this.lambda$onPhotoClosed$9();
            }
        }, 50L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void preparePlayer(File file) {
        if (this.parentActivity == null) {
            return;
        }
        releasePlayer();
        if (this.videoTextureView == null) {
            AspectRatioFrameLayout aspectRatioFrameLayout = new AspectRatioFrameLayout(this.parentActivity);
            this.aspectRatioFrameLayout = aspectRatioFrameLayout;
            aspectRatioFrameLayout.setVisibility(0);
            this.containerView.addView(this.aspectRatioFrameLayout, 0, LayoutHelper.createFrame(-1, -1, 17));
            TextureView textureView = new TextureView(this.parentActivity);
            this.videoTextureView = textureView;
            textureView.setOpaque(false);
            this.aspectRatioFrameLayout.addView(this.videoTextureView, LayoutHelper.createFrame(-1, -1, 17));
        }
        this.textureUploaded = false;
        this.videoCrossfadeStarted = false;
        this.videoTextureView.setAlpha(1.0f);
        if (this.videoPlayer == null) {
            VideoPlayer videoPlayer = new VideoPlayer() { // from class: org.telegram.ui.SecretMediaViewer.1
                @Override // org.telegram.ui.Components.VideoPlayer
                public void pause() {
                    super.pause();
                    SecretMediaViewer.this.playButtonDrawable.setPause(false);
                }

                @Override // org.telegram.ui.Components.VideoPlayer
                public void play() {
                    super.play();
                    SecretMediaViewer.this.playButtonDrawable.setPause(true);
                }

                @Override // org.telegram.ui.Components.VideoPlayer
                public void setPlayWhenReady(boolean z) {
                    super.setPlayWhenReady(z);
                    SecretMediaViewer.this.playButtonDrawable.setPause(z);
                }
            };
            this.videoPlayer = videoPlayer;
            videoPlayer.setTextureView(this.videoTextureView);
            this.videoPlayer.setDelegate(new 2(file));
        }
        this.videoPlayer.preparePlayer(Uri.fromFile(file), "other");
        this.videoPlayer.setPlayWhenReady(true);
        this.playButtonDrawable.setPause(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x026c, code lost:
    
        if (r0 > r3) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x025d, code lost:
    
        if (r13 > r3) goto L114;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x02df, code lost:
    
        if (r2 > r3) goto L149;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x02ce, code lost:
    
        if (r2 > r3) goto L143;
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x0347, code lost:
    
        if (r13 != null) goto L168;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x0366, code lost:
    
        r13.clear();
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x0364, code lost:
    
        if (r13 != null) goto L168;
     */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01d6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean processTouchEvent(MotionEvent motionEvent) {
        VelocityTracker velocityTracker;
        float f;
        if (this.photoAnimationInProgress == 0 && this.animationStartTime == 0) {
            if (motionEvent.getPointerCount() == 1 && this.gestureDetector.onTouchEvent(motionEvent) && this.doubleTap) {
                this.doubleTap = false;
                this.moving = false;
                this.zooming = false;
                checkMinMax(false);
                return true;
            }
            if (motionEvent.getActionMasked() == 0 || motionEvent.getActionMasked() == 5) {
                this.discardTap = false;
                if (!this.scroller.isFinished()) {
                    this.scroller.abortAnimation();
                }
                if (!this.draggingDown) {
                    if (motionEvent.getPointerCount() == 2) {
                        this.pinchStartDistance = (float) Math.hypot(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0));
                        this.pinchStartScale = this.scale;
                        this.pinchCenterX = (motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f;
                        this.pinchCenterY = (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f;
                        this.pinchStartX = this.translationX;
                        this.pinchStartY = this.translationY;
                        this.zooming = true;
                        this.moving = false;
                        velocityTracker = this.velocityTracker;
                    } else if (motionEvent.getPointerCount() == 1) {
                        this.moveStartX = motionEvent.getX();
                        float y = motionEvent.getY();
                        this.moveStartY = y;
                        this.dragY = y;
                        this.draggingDown = false;
                        this.canDragDown = true;
                        velocityTracker = this.velocityTracker;
                    }
                }
            } else {
                float f2 = 0.0f;
                if (motionEvent.getActionMasked() == 2) {
                    if (motionEvent.getPointerCount() == 2 && !this.draggingDown && this.zooming) {
                        this.discardTap = true;
                        this.scale = (((float) Math.hypot(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0))) / this.pinchStartDistance) * this.pinchStartScale;
                        this.translationX = (this.pinchCenterX - (getContainerViewWidth() / 2)) - (((this.pinchCenterX - (getContainerViewWidth() / 2)) - this.pinchStartX) * (this.scale / this.pinchStartScale));
                        float containerViewHeight = this.pinchCenterY - (getContainerViewHeight() / 2);
                        float containerViewHeight2 = (this.pinchCenterY - (getContainerViewHeight() / 2)) - this.pinchStartY;
                        float f3 = this.scale;
                        this.translationY = containerViewHeight - (containerViewHeight2 * (f3 / this.pinchStartScale));
                        updateMinMax(f3);
                    } else if (motionEvent.getPointerCount() == 1) {
                        VelocityTracker velocityTracker2 = this.velocityTracker;
                        if (velocityTracker2 != null) {
                            velocityTracker2.addMovement(motionEvent);
                        }
                        float abs = Math.abs(motionEvent.getX() - this.moveStartX);
                        float abs2 = Math.abs(motionEvent.getY() - this.dragY);
                        if (abs > AndroidUtilities.dp(3.0f) || abs2 > AndroidUtilities.dp(3.0f)) {
                            this.discardTap = true;
                        }
                        if (this.canDragDown && !this.draggingDown && this.scale == 1.0f && abs2 >= AndroidUtilities.dp(30.0f) && abs2 / 2.0f > abs) {
                            this.draggingDown = true;
                            this.moving = false;
                            this.dragY = motionEvent.getY();
                            if (this.isActionBarVisible) {
                                toggleActionBar(false, true);
                            }
                            return true;
                        }
                        if (this.draggingDown) {
                            f = motionEvent.getY() - this.dragY;
                        } else if (this.invalidCoords || this.animationStartTime != 0) {
                            this.invalidCoords = false;
                            this.moveStartX = motionEvent.getX();
                            this.moveStartY = motionEvent.getY();
                        } else {
                            float x = this.moveStartX - motionEvent.getX();
                            float y2 = this.moveStartY - motionEvent.getY();
                            if (this.moving || ((this.scale == 1.0f && Math.abs(y2) + AndroidUtilities.dp(12.0f) < Math.abs(x)) || this.scale != 1.0f)) {
                                if (!this.moving) {
                                    this.moving = true;
                                    this.canDragDown = false;
                                    x = 0.0f;
                                    y2 = 0.0f;
                                }
                                this.moveStartX = motionEvent.getX();
                                this.moveStartY = motionEvent.getY();
                                updateMinMax(this.scale);
                                float f4 = this.translationX;
                                if (f4 < this.minX || f4 > this.maxX) {
                                    x /= 3.0f;
                                }
                                float f5 = this.maxY;
                                if (f5 == 0.0f) {
                                    float f6 = this.minY;
                                    if (f6 == 0.0f) {
                                        float f7 = this.translationY - y2;
                                        if (f7 < f6) {
                                            this.translationY = f6;
                                        } else {
                                            if (f7 > f5) {
                                                this.translationY = f5;
                                            }
                                            f2 = y2;
                                        }
                                        this.translationX = f4 - x;
                                        if (this.scale != 1.0f) {
                                            f = this.translationY - f2;
                                        }
                                    }
                                }
                                float f8 = this.translationY;
                                if (f8 < this.minY || f8 > f5) {
                                    f2 = y2 / 3.0f;
                                    this.translationX = f4 - x;
                                    if (this.scale != 1.0f) {
                                    }
                                }
                                f2 = y2;
                                this.translationX = f4 - x;
                                if (this.scale != 1.0f) {
                                }
                            }
                        }
                        this.translationY = f;
                    }
                    this.containerView.invalidate();
                } else if (motionEvent.getActionMasked() == 3 || motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 6) {
                    if (this.zooming) {
                        this.invalidCoords = true;
                        float f9 = this.scale;
                        if (f9 < 1.0f) {
                            updateMinMax(1.0f);
                            animateTo(1.0f, 0.0f, 0.0f, true);
                        } else if (f9 > 3.0f) {
                            float containerViewWidth = (this.pinchCenterX - (getContainerViewWidth() / 2)) - (((this.pinchCenterX - (getContainerViewWidth() / 2)) - this.pinchStartX) * (3.0f / this.pinchStartScale));
                            float containerViewHeight3 = (this.pinchCenterY - (getContainerViewHeight() / 2)) - (((this.pinchCenterY - (getContainerViewHeight() / 2)) - this.pinchStartY) * (3.0f / this.pinchStartScale));
                            updateMinMax(3.0f);
                            float f10 = this.minX;
                            if (containerViewWidth >= f10) {
                                f10 = this.maxX;
                            }
                            containerViewWidth = f10;
                            float f11 = this.minY;
                            if (containerViewHeight3 >= f11) {
                                f11 = this.maxY;
                            }
                            containerViewHeight3 = f11;
                            animateTo(3.0f, containerViewWidth, containerViewHeight3, true);
                        } else {
                            checkMinMax(true);
                        }
                        this.zooming = false;
                    } else if (this.draggingDown) {
                        if (Math.abs(this.dragY - motionEvent.getY()) > getContainerViewHeight() / 6.0f) {
                            closePhoto(true, false);
                        } else {
                            animateTo(1.0f, 0.0f, 0.0f, false);
                        }
                        this.draggingDown = false;
                    } else if (this.moving) {
                        float f12 = this.translationX;
                        float f13 = this.translationY;
                        updateMinMax(this.scale);
                        this.moving = false;
                        this.canDragDown = true;
                        VelocityTracker velocityTracker3 = this.velocityTracker;
                        if (velocityTracker3 != null && this.scale == 1.0f) {
                            velocityTracker3.computeCurrentVelocity(1000);
                        }
                        float f14 = this.translationX;
                        float f15 = this.minX;
                        if (f14 >= f15) {
                            f15 = this.maxX;
                        }
                        f12 = f15;
                        float f16 = this.translationY;
                        float f17 = this.minY;
                        if (f16 >= f17) {
                            f17 = this.maxY;
                        }
                        f13 = f17;
                        animateTo(this.scale, f12, f13, false);
                    }
                }
            }
        }
        return false;
    }

    private void releasePlayer() {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            this.playerRetryPlayCount = 0;
            videoPlayer.releasePlayer(true);
            this.videoPlayer = null;
        }
        try {
            Activity activity = this.parentActivity;
            if (activity != null) {
                activity.getWindow().clearFlags(128);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
        if (aspectRatioFrameLayout != null) {
            this.containerView.removeView(aspectRatioFrameLayout);
            this.aspectRatioFrameLayout = null;
        }
        if (this.videoTextureView != null) {
            this.videoTextureView = null;
        }
        this.isPlaying = false;
    }

    private void setCaptionHwLayerEnabled(boolean z) {
        if (this.captionHwLayerEnabled != z) {
            this.captionHwLayerEnabled = z;
            this.captionTextViewSwitcher.setLayerType(2, null);
            this.captionTextViewSwitcher.getCurrentView().setLayerType(2, null);
            this.captionTextViewSwitcher.getNextView().setLayerType(2, null);
        }
    }

    private void setCurrentCaption(MessageObject messageObject, CharSequence charSequence, boolean z, boolean z2) {
        boolean z3;
        TLRPC.Message message;
        CharSequence cloneSpans = AnimatedEmojiSpan.cloneSpans(charSequence, 3);
        if (this.captionScrollView == null) {
            FrameLayout frameLayout = new FrameLayout(this.containerView.getContext());
            this.captionContainer = frameLayout;
            this.captionTextViewSwitcher.setContainer(frameLayout);
            PhotoViewer.CaptionScrollView captionScrollView = new PhotoViewer.CaptionScrollView(this.containerView.getContext(), this.captionTextViewSwitcher, this.captionContainer) { // from class: org.telegram.ui.SecretMediaViewer.10
                @Override // org.telegram.ui.PhotoViewer.CaptionScrollView
                protected void onScrollEnd() {
                    if (!SecretMediaViewer.this.isVideo || getScrollY() > 0) {
                        return;
                    }
                    AndroidUtilities.runOnUIThread(SecretMediaViewer.this.hideActionBarRunnable, 3000L);
                }

                @Override // org.telegram.ui.PhotoViewer.CaptionScrollView
                protected void onScrollStart() {
                    AndroidUtilities.cancelRunOnUIThread(SecretMediaViewer.this.hideActionBarRunnable);
                }

                @Override // org.telegram.ui.PhotoViewer.CaptionScrollView
                protected void onScrollUpdate() {
                    if (SecretMediaViewer.this.imageMoveAnimation == null) {
                        SecretMediaViewer.this.showPlayButton(((float) getScrollY()) < ((float) getMeasuredHeight()) / 3.0f && SecretMediaViewer.this.isActionBarVisible, true);
                    }
                }
            };
            this.captionScrollView = captionScrollView;
            this.captionTextViewSwitcher.setScrollView(captionScrollView);
            this.captionContainer.setClipChildren(false);
            this.captionScrollView.addView(this.captionContainer, new ViewGroup.LayoutParams(-1, -2));
            this.containerView.addView(this.captionScrollView, LayoutHelper.createFrame(-1, -1.0f, 80, 0.0f, 0.0f, 0.0f, 0.0f));
            this.textSelectionHelper.getOverlayView(this.containerView.getContext()).bringToFront();
        }
        if (this.captionTextViewSwitcher.getParent() != this.captionContainer) {
            this.captionTextViewSwitcher.setMeasureAllChildren(true);
            this.captionContainer.addView(this.captionTextViewSwitcher, -1, -2);
        }
        boolean isEmpty = TextUtils.isEmpty(cloneSpans);
        boolean isEmpty2 = TextUtils.isEmpty(this.captionTextViewSwitcher.getCurrentView().getText());
        PhotoViewer.CaptionTextViewSwitcher captionTextViewSwitcher = this.captionTextViewSwitcher;
        TextView nextView = z2 ? captionTextViewSwitcher.getNextView() : captionTextViewSwitcher.getCurrentView();
        int maxLines = nextView.getMaxLines();
        if (maxLines == 1) {
            this.captionTextViewSwitcher.getCurrentView().setSingleLine(false);
            this.captionTextViewSwitcher.getNextView().setSingleLine(false);
        }
        if (maxLines != Integer.MAX_VALUE) {
            this.captionTextViewSwitcher.getCurrentView().setMaxLines(ConnectionsManager.DEFAULT_DATACENTER_ID);
            this.captionTextViewSwitcher.getNextView().setMaxLines(ConnectionsManager.DEFAULT_DATACENTER_ID);
            this.captionTextViewSwitcher.getCurrentView().setEllipsize(null);
            this.captionTextViewSwitcher.getNextView().setEllipsize(null);
        }
        nextView.setScrollX(0);
        PhotoViewer.CaptionScrollView captionScrollView2 = this.captionScrollView;
        captionScrollView2.dontChangeTopMargin = false;
        if (z2) {
            if (Build.VERSION.SDK_INT >= 23) {
                TransitionManager.endTransitions(captionScrollView2);
            }
            TransitionSet duration = new TransitionSet().addTransition(new 12(2, isEmpty2, isEmpty)).addTransition(new 11(1, isEmpty2, isEmpty)).setDuration(200L);
            if (!isEmpty2) {
                this.captionScrollView.dontChangeTopMargin = true;
                duration.addTransition(new 13());
            }
            if (isEmpty2 && !isEmpty) {
                duration.addTarget((View) this.captionTextViewSwitcher);
            }
            TransitionManager.beginDelayedTransition(this.captionScrollView, duration);
            z3 = true;
        } else {
            this.captionTextViewSwitcher.getCurrentView().setText((CharSequence) null);
            PhotoViewer.CaptionScrollView captionScrollView3 = this.captionScrollView;
            if (captionScrollView3 != null) {
                captionScrollView3.scrollTo(0, 0);
            }
            z3 = false;
        }
        if (isEmpty) {
            this.captionTextViewSwitcher.setText(null, z2);
            this.captionTextViewSwitcher.getCurrentView().setTextColor(-1);
            this.captionTextViewSwitcher.setVisibility(4, !z3 || isEmpty2);
            this.captionTextViewSwitcher.setTag(null);
        } else {
            Theme.createChatResources(null, true);
            if (messageObject == null || (message = messageObject.messageOwner) == null || message.translatedText == null || !TextUtils.equals(message.translatedToLanguage, TranslateAlert2.getToLanguage())) {
                if (messageObject == null || messageObject.messageOwner.entities.isEmpty()) {
                    cloneSpans = Emoji.replaceEmoji((CharSequence) new SpannableStringBuilder(cloneSpans), nextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                } else {
                    SpannableString spannableString = new SpannableString(cloneSpans);
                    messageObject.addEntitiesToText(spannableString, true, false);
                    if (messageObject.isVideo()) {
                        MessageObject.addUrlsByPattern(messageObject.isOutOwner(), spannableString, false, 3, (int) messageObject.getDuration(), false);
                    }
                    cloneSpans = Emoji.replaceEmoji((CharSequence) spannableString, nextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                }
            }
            this.captionTextViewSwitcher.setTag(cloneSpans);
            try {
                this.captionTextViewSwitcher.setText(cloneSpans, z2, false);
                PhotoViewer.CaptionScrollView captionScrollView4 = this.captionScrollView;
                if (captionScrollView4 != null) {
                    captionScrollView4.updateTopMargin();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            nextView.setScrollY(0);
            nextView.setTextColor(-1);
            this.captionTextViewSwitcher.setVisibility(this.isActionBarVisible ? 0 : 4);
        }
        if (this.captionTextViewSwitcher.getCurrentView() instanceof PhotoViewer.CaptionTextView) {
            ((PhotoViewer.CaptionTextView) this.captionTextViewSwitcher.getCurrentView()).setLoading(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPlayButton(boolean z, boolean z2) {
        boolean z3 = this.isVideo && z;
        if (this.playButtonShown == z3 && z2) {
            return;
        }
        this.playButtonShown = z3;
        this.playButton.animate().cancel();
        if (z2) {
            this.playButton.animate().scaleX(z3 ? 1.0f : 0.6f).scaleY(z3 ? 1.0f : 0.6f).alpha(z3 ? 1.0f : 0.0f).setDuration(340L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
            return;
        }
        this.playButton.setScaleX(z3 ? 1.0f : 0.6f);
        this.playButton.setScaleY(z3 ? 1.0f : 0.6f);
        this.playButton.setAlpha(z3 ? 1.0f : 0.0f);
    }

    private void showSecretHint() {
        this.secretHint.setMultilineText(true);
        String string = LocaleController.getString(this.isVideo ? R.string.VideoShownOnce : R.string.PhotoShownOnce);
        HintView2 hintView2 = this.secretHint;
        hintView2.setMaxWidthPx(HintView2.cutInFancyHalf(string, hintView2.getTextPaint()));
        this.secretHint.setText(string);
        this.secretHint.setInnerPadding(12, 7, 11, 7);
        this.secretHint.setIconMargin(2);
        this.secretHint.setIconTranslate(0.0f, 0.0f);
        this.secretHint.setIcon(R.raw.fire_on);
        this.secretHint.show();
        MessagesController.getGlobalMainSettings().edit().putInt("viewoncehint", MessagesController.getGlobalMainSettings().getInt("viewoncehint", 0) + 1).commit();
    }

    private void toggleActionBar(boolean z, boolean z2) {
        AndroidUtilities.cancelRunOnUIThread(this.hideActionBarRunnable);
        if (z && this.isVideo) {
            AndroidUtilities.runOnUIThread(this.hideActionBarRunnable, 3000L);
        }
        if (z) {
            this.actionBar.setVisibility(0);
        }
        this.actionBar.setEnabled(z);
        this.isActionBarVisible = z;
        showPlayButton(z, z2);
        if (!z2) {
            this.actionBar.setAlpha(z ? 1.0f : 0.0f);
            this.captionScrollView.setAlpha(z ? 1.0f : 0.0f);
            this.seekbarBackground.setAlpha(z ? 1.0f : 0.0f);
            this.navigationBar.setAlpha(z ? 1.0f : 0.0f);
            if (z) {
                return;
            }
            this.actionBar.setVisibility(8);
            this.captionScrollView.scrollTo(0, 0);
            return;
        }
        ArrayList arrayList = new ArrayList();
        ActionBar actionBar = this.actionBar;
        Property property = View.ALPHA;
        arrayList.add(ObjectAnimator.ofFloat(actionBar, (Property<ActionBar, Float>) property, z ? 1.0f : 0.0f));
        VideoPlayerControlFrameLayout videoPlayerControlFrameLayout = this.seekbarContainer;
        arrayList.add(ObjectAnimator.ofFloat(videoPlayerControlFrameLayout, (Property<VideoPlayerControlFrameLayout, Float>) videoPlayerControlFrameLayout.SEEKBAR_ALPHA, z ? 1.0f : 0.0f));
        arrayList.add(ObjectAnimator.ofFloat(this.captionScrollView, (Property<PhotoViewer.CaptionScrollView, Float>) property, z ? 1.0f : 0.0f));
        arrayList.add(ObjectAnimator.ofFloat(this.seekbarBackground, (Property<View, Float>) property, z ? 1.0f : 0.0f));
        arrayList.add(ObjectAnimator.ofFloat(this.navigationBar, (Property<View, Float>) property, z ? 1.0f : 0.0f));
        AnimatorSet animatorSet = new AnimatorSet();
        this.currentActionBarAnimation = animatorSet;
        animatorSet.playTogether(arrayList);
        if (!z) {
            this.currentActionBarAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SecretMediaViewer.15
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (SecretMediaViewer.this.currentActionBarAnimation == null || !SecretMediaViewer.this.currentActionBarAnimation.equals(animator)) {
                        return;
                    }
                    SecretMediaViewer.this.actionBar.setVisibility(8);
                    SecretMediaViewer.this.currentActionBarAnimation = null;
                    SecretMediaViewer.this.captionScrollView.scrollTo(0, 0);
                }
            });
        }
        this.currentActionBarAnimation.setDuration(200L);
        this.currentActionBarAnimation.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMinMax(float f) {
        int imageWidth = ((int) ((this.centerImage.getImageWidth() * f) - getContainerViewWidth())) / 2;
        int imageHeight = ((int) ((this.centerImage.getImageHeight() * f) - getContainerViewHeight())) / 2;
        if (imageWidth > 0) {
            this.minX = -imageWidth;
            this.maxX = imageWidth;
        } else {
            this.maxX = 0.0f;
            this.minX = 0.0f;
        }
        if (imageHeight > 0) {
            this.minY = -imageHeight;
            this.maxY = imageHeight;
        } else {
            this.maxY = 0.0f;
            this.minY = 0.0f;
        }
    }

    private void updateVideoPlayerTime() {
        Arrays.fill(this.videoPlayerCurrentTime, 0);
        Arrays.fill(this.videoPlayerTotalTime, 0);
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            long max = Math.max(0L, videoPlayer.getCurrentPosition()) / 1000;
            long max2 = Math.max(0L, this.videoPlayer.getDuration()) / 1000;
            int[] iArr = this.videoPlayerCurrentTime;
            iArr[0] = (int) (max / 60);
            iArr[1] = (int) (max % 60);
            int[] iArr2 = this.videoPlayerTotalTime;
            iArr2[0] = (int) (max2 / 60);
            iArr2[1] = (int) (max2 % 60);
        }
        int i = this.videoPlayerCurrentTime[0];
        Locale locale = Locale.ROOT;
        String format = i >= 60 ? String.format(locale, "%02d:%02d:%02d", Integer.valueOf(i / 60), Integer.valueOf(this.videoPlayerCurrentTime[0] % 60), Integer.valueOf(this.videoPlayerCurrentTime[1])) : String.format(locale, "%02d:%02d", Integer.valueOf(i), Integer.valueOf(this.videoPlayerCurrentTime[1]));
        int i2 = this.videoPlayerTotalTime[0];
        this.videoPlayerTime.setText(String.format(Locale.ROOT, "%s / %s", format, i2 >= 60 ? String.format(Locale.ROOT, "%02d:%02d:%02d", Integer.valueOf(i2 / 60), Integer.valueOf(this.videoPlayerTotalTime[0] % 60), Integer.valueOf(this.videoPlayerTotalTime[1])) : String.format(Locale.ROOT, "%02d:%02d", Integer.valueOf(i2), Integer.valueOf(this.videoPlayerTotalTime[1]))));
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x03bb  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x030e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean closePhoto(boolean z, boolean z2) {
        final PhotoViewer.PlaceProviderObject placeProviderObject;
        VideoPlayer videoPlayer;
        final SecretMediaViewer secretMediaViewer;
        Runnable runnable;
        Runnable runnable2;
        if (this.parentActivity == null || !this.isPhotoVisible || checkPhotoAnimation()) {
            return false;
        }
        if (this.ignoreDelete && z2) {
            return false;
        }
        Activity activity = this.parentActivity;
        if (activity != null) {
            Window window = activity.getWindow();
            AndroidUtilities.setLightNavigationBar(window, this.wasLightNavigationBar);
            AndroidUtilities.setNavigationBarColor(window, this.wasNavigationBarColor);
            Activity activity2 = this.parentActivity;
            if (activity2 instanceof LaunchActivity) {
                ((LaunchActivity) activity2).animateNavigationBarColor(this.wasNavigationBarColor);
            } else {
                AndroidUtilities.setNavigationBarColor(window, this.wasNavigationBarColor);
            }
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateMessageMedia);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didCreatedNewDeleteTask);
        this.isActionBarVisible = false;
        VelocityTracker velocityTracker = this.velocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.velocityTracker = null;
        }
        this.closeTime = System.currentTimeMillis();
        PhotoViewer.PhotoViewerProvider photoViewerProvider = this.currentProvider;
        if (photoViewerProvider != null) {
            MessageObject messageObject = this.currentMessageObject;
            TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
            if (!(messageMedia.photo instanceof TLRPC.TL_photoEmpty) && !(messageMedia.document instanceof TLRPC.TL_documentEmpty)) {
                placeProviderObject = photoViewerProvider.getPlaceForPhoto(messageObject, null, 0, true);
                videoPlayer = this.videoPlayer;
                if (videoPlayer != null) {
                    videoPlayer.pause();
                }
                if (z) {
                    secretMediaViewer = this;
                    secretMediaViewer.showPlayButton(false, true);
                    AnimatorSet animatorSet = new AnimatorSet();
                    ObjectAnimator ofFloat = ObjectAnimator.ofFloat(secretMediaViewer.containerView, (Property<FrameLayoutDrawer, Float>) View.SCALE_X, 0.9f);
                    ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(secretMediaViewer.containerView, (Property<FrameLayoutDrawer, Float>) View.SCALE_Y, 0.9f);
                    ObjectAnimator ofInt = ObjectAnimator.ofInt(secretMediaViewer.photoBackgroundDrawable, (Property<PhotoBackgroundDrawable, Integer>) AnimationProperties.COLOR_DRAWABLE_ALPHA, 0);
                    ActionBar actionBar = secretMediaViewer.actionBar;
                    Property property = View.ALPHA;
                    ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(actionBar, (Property<ActionBar, Float>) property, 0.0f);
                    ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(secretMediaViewer.captionScrollView, (Property<PhotoViewer.CaptionScrollView, Float>) property, 0.0f);
                    ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(secretMediaViewer.navigationBar, (Property<View, Float>) property, 0.0f);
                    VideoPlayerControlFrameLayout videoPlayerControlFrameLayout = secretMediaViewer.seekbarContainer;
                    animatorSet.playTogether(ofFloat, ofFloat2, ofInt, ofFloat3, ofFloat4, ofFloat5, ObjectAnimator.ofFloat(videoPlayerControlFrameLayout, (Property<VideoPlayerControlFrameLayout, Float>) videoPlayerControlFrameLayout.SEEKBAR_ALPHA, 0.0f), ObjectAnimator.ofFloat(secretMediaViewer.seekbarContainer, (Property<VideoPlayerControlFrameLayout, Float>) property, 0.0f));
                    secretMediaViewer.photoAnimationInProgress = 2;
                    secretMediaViewer.photoAnimationEndRunnable = new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            SecretMediaViewer.this.lambda$closePhoto$8(placeProviderObject);
                        }
                    };
                    animatorSet.setDuration(200L);
                    animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SecretMediaViewer.18
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (SecretMediaViewer.this.photoAnimationEndRunnable != null) {
                                SecretMediaViewer.this.photoAnimationEndRunnable.run();
                                SecretMediaViewer.this.photoAnimationEndRunnable = null;
                            }
                        }
                    });
                    secretMediaViewer.photoTransitionAnimationStartTime = System.currentTimeMillis();
                    runnable = null;
                    secretMediaViewer.containerView.setLayerType(2, null);
                    animatorSet.start();
                } else {
                    this.photoAnimationInProgress = 3;
                    this.containerView.invalidate();
                    this.imageMoveAnimation = new AnimatorSet();
                    if (placeProviderObject == null || placeProviderObject.imageReceiver.getThumbBitmap() == null || z2 || this.onClose != null) {
                        int i = AndroidUtilities.displaySize.y + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0);
                        if (this.translationY < 0.0f) {
                            i = -i;
                        }
                        this.animateToY = i;
                    } else {
                        placeProviderObject.imageReceiver.setVisible(false, true);
                        RectF drawRegion = placeProviderObject.imageReceiver.getDrawRegion();
                        float f = drawRegion.right - drawRegion.left;
                        float f2 = drawRegion.bottom - drawRegion.top;
                        Point point = AndroidUtilities.displaySize;
                        int i2 = point.x;
                        int i3 = point.y;
                        int i4 = Build.VERSION.SDK_INT;
                        this.animateToScale = Math.max(f / i2, f2 / (i3 + (i4 >= 21 ? AndroidUtilities.statusBarHeight : 0)));
                        float f3 = placeProviderObject.viewX;
                        float f4 = drawRegion.left;
                        this.animateToX = ((f3 + f4) + (f / 2.0f)) - (i2 / 2);
                        this.animateToY = ((placeProviderObject.viewY + drawRegion.top) + (f2 / 2.0f)) - (r3 / 2);
                        this.animateToClipHorizontal = Math.abs(f4 - placeProviderObject.imageReceiver.getImageX());
                        int abs = (int) Math.abs(drawRegion.top - placeProviderObject.imageReceiver.getImageY());
                        placeProviderObject.parentView.getLocationInWindow(new int[2]);
                        float f5 = ((r4[1] - (i4 >= 21 ? 0 : AndroidUtilities.statusBarHeight)) - (placeProviderObject.viewY + drawRegion.top)) + placeProviderObject.clipTopAddition;
                        this.animateToClipTop = f5;
                        float f6 = abs;
                        this.animateToClipTop = Math.max(0.0f, Math.max(f5, f6));
                        float height = (((placeProviderObject.viewY + drawRegion.top) + ((int) f2)) - ((r4[1] + placeProviderObject.parentView.getHeight()) - (i4 >= 21 ? 0 : AndroidUtilities.statusBarHeight))) + placeProviderObject.clipBottomAddition;
                        this.animateToClipBottom = height;
                        this.animateToClipBottom = Math.max(0.0f, Math.max(height, f6));
                        this.animateToClipTopOrigin = 0.0f;
                        this.animateToClipTopOrigin = Math.max(0.0f, Math.max(0.0f, f6));
                        this.animateToClipBottomOrigin = 0.0f;
                        this.animateToClipBottomOrigin = Math.max(0.0f, Math.max(0.0f, f6));
                        this.animationStartTime = System.currentTimeMillis();
                        this.zoomAnimation = true;
                    }
                    this.animateToRadius = false;
                    showPlayButton(false, true);
                    if (this.isVideo) {
                        this.videoCrossfadeStarted = false;
                        this.textureUploaded = false;
                        AnimatorSet animatorSet2 = this.imageMoveAnimation;
                        ObjectAnimator ofInt2 = ObjectAnimator.ofInt(this.photoBackgroundDrawable, (Property<PhotoBackgroundDrawable, Integer>) AnimationProperties.COLOR_DRAWABLE_ALPHA, 0);
                        ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(this, (Property<SecretMediaViewer, Float>) this.ANIMATION_VALUE, 0.0f, 1.0f);
                        ActionBar actionBar2 = this.actionBar;
                        Property property2 = View.ALPHA;
                        ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat(actionBar2, (Property<ActionBar, Float>) property2, 0.0f);
                        ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat(this.captionScrollView, (Property<PhotoViewer.CaptionScrollView, Float>) property2, 0.0f);
                        ObjectAnimator ofFloat9 = ObjectAnimator.ofFloat(this.navigationBar, (Property<View, Float>) property2, 0.0f);
                        VideoPlayerControlFrameLayout videoPlayerControlFrameLayout2 = this.seekbarContainer;
                        animatorSet2.playTogether(ofInt2, ofFloat6, ofFloat7, ofFloat8, ofFloat9, ObjectAnimator.ofFloat(videoPlayerControlFrameLayout2, (Property<VideoPlayerControlFrameLayout, Float>) videoPlayerControlFrameLayout2.SEEKBAR_ALPHA, 0.0f), ObjectAnimator.ofFloat(this.seekbarContainer, (Property<VideoPlayerControlFrameLayout, Float>) property2, 0.0f), ObjectAnimator.ofFloat(this.secretHint, (Property<HintView2, Float>) property2, 0.0f), ObjectAnimator.ofFloat(this, (Property<SecretMediaViewer, Float>) this.VIDEO_CROSSFADE_ALPHA, 0.0f));
                    } else {
                        this.centerImage.setManualAlphaAnimator(true);
                        AnimatorSet animatorSet3 = this.imageMoveAnimation;
                        ObjectAnimator ofInt3 = ObjectAnimator.ofInt(this.photoBackgroundDrawable, (Property<PhotoBackgroundDrawable, Integer>) AnimationProperties.COLOR_DRAWABLE_ALPHA, 0);
                        ObjectAnimator ofFloat10 = ObjectAnimator.ofFloat(this, (Property<SecretMediaViewer, Float>) this.ANIMATION_VALUE, 0.0f, 1.0f);
                        ActionBar actionBar3 = this.actionBar;
                        Property property3 = View.ALPHA;
                        ObjectAnimator ofFloat11 = ObjectAnimator.ofFloat(actionBar3, (Property<ActionBar, Float>) property3, 0.0f);
                        ObjectAnimator ofFloat12 = ObjectAnimator.ofFloat(this.captionScrollView, (Property<PhotoViewer.CaptionScrollView, Float>) property3, 0.0f);
                        ObjectAnimator ofFloat13 = ObjectAnimator.ofFloat(this.navigationBar, (Property<View, Float>) property3, 0.0f);
                        VideoPlayerControlFrameLayout videoPlayerControlFrameLayout3 = this.seekbarContainer;
                        animatorSet3.playTogether(ofInt3, ofFloat10, ofFloat11, ofFloat12, ofFloat13, ObjectAnimator.ofFloat(videoPlayerControlFrameLayout3, (Property<VideoPlayerControlFrameLayout, Float>) videoPlayerControlFrameLayout3.SEEKBAR_ALPHA, 0.0f), ObjectAnimator.ofFloat(this.seekbarContainer, (Property<VideoPlayerControlFrameLayout, Float>) property3, 0.0f), ObjectAnimator.ofFloat(this.secretHint, (Property<HintView2, Float>) property3, 0.0f), ObjectAnimator.ofFloat(this.centerImage, (Property<ImageReceiver, Float>) AnimationProperties.IMAGE_RECEIVER_ALPHA, 0.0f));
                    }
                    secretMediaViewer = this;
                    secretMediaViewer.photoAnimationEndRunnable = new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            SecretMediaViewer.this.lambda$closePhoto$7(placeProviderObject);
                        }
                    };
                    secretMediaViewer.imageMoveAnimation.setInterpolator(new DecelerateInterpolator());
                    secretMediaViewer.imageMoveAnimation.setDuration(250L);
                    secretMediaViewer.imageMoveAnimation.addListener(secretMediaViewer.new 17(placeProviderObject));
                    secretMediaViewer.photoTransitionAnimationStartTime = System.currentTimeMillis();
                    secretMediaViewer.containerView.setLayerType(2, null);
                    secretMediaViewer.imageMoveAnimation.start();
                    runnable = null;
                }
                runnable2 = secretMediaViewer.onClose;
                if (runnable2 != null) {
                    runnable2.run();
                    secretMediaViewer.onClose = runnable;
                }
                return true;
            }
        }
        placeProviderObject = null;
        videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
        }
        if (z) {
        }
        runnable2 = secretMediaViewer.onClose;
        if (runnable2 != null) {
        }
        return true;
    }

    public void destroyPhotoViewer() {
        FrameLayout frameLayout;
        Runnable runnable = this.onClose;
        if (runnable != null) {
            runnable.run();
            this.onClose = null;
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateMessageMedia);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didCreatedNewDeleteTask);
        this.isVisible = false;
        this.currentProvider = null;
        ImageReceiver.BitmapHolder bitmapHolder = this.currentThumb;
        if (bitmapHolder != null) {
            bitmapHolder.release();
            this.currentThumb = null;
        }
        releasePlayer();
        if (this.parentActivity != null && (frameLayout = this.windowView) != null) {
            try {
                if (frameLayout.getParent() != null) {
                    ((WindowManager) this.parentActivity.getSystemService("window")).removeViewImmediate(this.windowView);
                }
                this.windowView = null;
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        Instance = null;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.messagesDeleted) {
            if (!((Boolean) objArr[2]).booleanValue() && this.currentMessageObject != null && ((Long) objArr[1]).longValue() == 0 && ((ArrayList) objArr[0]).contains(Integer.valueOf(this.currentMessageObject.getId()))) {
                if (!this.isVideo || this.videoWatchedOneTime) {
                    if (closePhoto(true, true)) {
                        return;
                    }
                    this.closeAfterAnimation = true;
                    return;
                }
                this.closeVideoAfterWatch = true;
                return;
            }
            return;
        }
        if (i != NotificationCenter.didCreatedNewDeleteTask) {
            if (i == NotificationCenter.updateMessageMedia) {
                if (this.currentMessageObject.getId() == ((TLRPC.Message) objArr[0]).id) {
                    if (!this.isVideo || this.videoWatchedOneTime) {
                        if (closePhoto(true, true)) {
                            return;
                        }
                        this.closeAfterAnimation = true;
                        return;
                    }
                    this.closeVideoAfterWatch = true;
                    return;
                }
                return;
            }
            return;
        }
        if (this.currentMessageObject == null || this.secretDeleteTimer == null || ((Long) objArr[0]).longValue() != this.currentDialogId) {
            return;
        }
        SparseArray sparseArray = (SparseArray) objArr[1];
        for (int i3 = 0; i3 < sparseArray.size(); i3++) {
            int keyAt = sparseArray.keyAt(i3);
            ArrayList arrayList = (ArrayList) sparseArray.get(keyAt);
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                if (this.currentMessageObject.getId() == ((Integer) arrayList.get(i4)).intValue()) {
                    this.currentMessageObject.messageOwner.destroyTime = keyAt;
                    this.secretDeleteTimer.invalidate();
                    return;
                }
            }
        }
    }

    public float getAnimationValue() {
        return this.animationValue;
    }

    public long getCloseTime() {
        return this.closeTime;
    }

    public MessageObject getCurrentMessageObject() {
        return this.currentMessageObject;
    }

    public long getOpenTime() {
        return this.openTime;
    }

    public float getVideoCrossfadeAlpha() {
        return this.videoCrossfadeAlpha;
    }

    public boolean isShowingImage(MessageObject messageObject) {
        MessageObject messageObject2;
        return (!this.isVisible || this.disableShowCheck || messageObject == null || (messageObject2 = this.currentMessageObject) == null || messageObject2.getId() != messageObject.getId()) ? false : true;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x008e, code lost:
    
        if (r2 > r10) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x007f, code lost:
    
        if (r0 > r10) goto L18;
     */
    @Override // android.view.GestureDetector.OnDoubleTapListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onDoubleTap(MotionEvent motionEvent) {
        float f = this.scale;
        boolean z = false;
        if (f == 1.0f && (this.translationY != 0.0f || this.translationX != 0.0f)) {
            return false;
        }
        if (this.animationStartTime == 0 && this.photoAnimationInProgress == 0) {
            z = true;
            if (f == 1.0f) {
                float x = (motionEvent.getX() - (getContainerViewWidth() / 2)) - (((motionEvent.getX() - (getContainerViewWidth() / 2)) - this.translationX) * (3.0f / this.scale));
                float y = (motionEvent.getY() - (getContainerViewHeight() / 2)) - (((motionEvent.getY() - (getContainerViewHeight() / 2)) - this.translationY) * (3.0f / this.scale));
                updateMinMax(3.0f);
                float f2 = this.minX;
                if (x >= f2) {
                    f2 = this.maxX;
                }
                x = f2;
                float f3 = this.minY;
                if (y >= f3) {
                    f3 = this.maxY;
                }
                y = f3;
                animateTo(3.0f, x, y, true);
            } else {
                animateTo(1.0f, 0.0f, 0.0f, true);
            }
            this.doubleTap = true;
        }
        return z;
    }

    @Override // android.view.GestureDetector.OnDoubleTapListener
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (this.scale == 1.0f) {
            return false;
        }
        this.scroller.abortAnimation();
        this.scroller.fling(Math.round(this.translationX), Math.round(this.translationY), Math.round(f), Math.round(f2), (int) this.minX, (int) this.maxX, (int) this.minY, (int) this.maxY);
        this.containerView.postInvalidate();
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override // android.view.GestureDetector.OnDoubleTapListener
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        if (this.discardTap) {
            return false;
        }
        if (this.videoPlayer == null || !this.isActionBarVisible || motionEvent.getX() < this.playButton.getX() || motionEvent.getY() < this.playButton.getY() || motionEvent.getX() > this.playButton.getX() + this.playButton.getMeasuredWidth() || motionEvent.getX() > this.playButton.getX() + this.playButton.getMeasuredWidth()) {
            toggleActionBar(!this.isActionBarVisible, true);
        } else {
            this.videoPlayer.setPlayWhenReady(!r5.getPlayWhenReady());
            if (this.videoPlayer.getPlayWhenReady()) {
                toggleActionBar(true, true);
            } else {
                showPlayButton(true, true);
            }
        }
        return true;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v2 */
    /* JADX WARN: Type inference failed for: r14v7 */
    /* JADX WARN: Type inference failed for: r14v8 */
    /* JADX WARN: Type inference failed for: r15v2, types: [android.graphics.Paint, android.view.View$OnClickListener] */
    /* JADX WARN: Type inference failed for: r15v6 */
    /* JADX WARN: Type inference failed for: r15v7 */
    /* JADX WARN: Type inference failed for: r15v8 */
    public void openMedia(final MessageObject messageObject, PhotoViewer.PhotoViewerProvider photoViewerProvider, final Runnable runnable, Runnable runnable2) {
        PhotoViewer.PlaceProviderObject placeForPhoto;
        PhotoViewer.PlaceProviderObject placeProviderObject;
        String str;
        int i;
        float f;
        ?? r15;
        int navigationBarColor;
        Object obj;
        boolean z;
        ImageReceiver imageReceiver;
        BitmapDrawable bitmapDrawable;
        String str2;
        int i2;
        ImageLocation imageLocation;
        String str3;
        long j;
        MessageObject messageObject2;
        if (this.parentActivity == null || messageObject == null || !messageObject.needDrawBluredPreview() || photoViewerProvider == null || (placeForPhoto = photoViewerProvider.getPlaceForPhoto(messageObject, null, 0, true)) == null) {
            return;
        }
        this.ignoreDelete = messageObject.messageOwner.ttl == Integer.MAX_VALUE;
        this.onClose = runnable2;
        this.currentProvider = photoViewerProvider;
        this.openTime = System.currentTimeMillis();
        this.closeTime = 0L;
        this.isActionBarVisible = true;
        this.isPhotoVisible = true;
        this.draggingDown = false;
        AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
        if (aspectRatioFrameLayout != null) {
            aspectRatioFrameLayout.setVisibility(4);
        }
        releasePlayer();
        this.pinchStartDistance = 0.0f;
        this.pinchStartScale = 1.0f;
        this.pinchCenterX = 0.0f;
        this.pinchCenterY = 0.0f;
        this.pinchStartX = 0.0f;
        this.pinchStartY = 0.0f;
        this.moveStartX = 0.0f;
        this.moveStartY = 0.0f;
        this.zooming = false;
        this.moving = false;
        this.doubleTap = false;
        this.invalidCoords = false;
        this.canDragDown = true;
        updateMinMax(this.scale);
        this.photoBackgroundDrawable.setAlpha(0);
        this.containerView.setAlpha(1.0f);
        this.containerView.setVisibility(0);
        this.secretDeleteTimer.setAlpha(1.0f);
        this.isVideo = false;
        this.videoWatchedOneTime = false;
        this.closeVideoAfterWatch = false;
        this.disableShowCheck = true;
        this.centerImage.setManualAlphaAnimator(false);
        this.videoWidth = 0;
        this.videoHeight = 0;
        RectF rectF = new RectF(placeForPhoto.imageReceiver.getDrawRegion());
        rectF.left = Math.max(rectF.left, placeForPhoto.imageReceiver.getImageX());
        rectF.top = Math.max(rectF.top, placeForPhoto.imageReceiver.getImageY());
        rectF.right = Math.min(rectF.right, placeForPhoto.imageReceiver.getImageX2());
        rectF.bottom = Math.min(rectF.bottom, placeForPhoto.imageReceiver.getImageY2());
        float width = rectF.width();
        float height = rectF.height();
        Point point = AndroidUtilities.displaySize;
        int i3 = point.x;
        this.scale = Math.max(width / i3, height / (point.y + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)));
        int[] iArr = placeForPhoto.radius;
        if (iArr != null) {
            this.animateFromRadius = new int[iArr.length];
            int i4 = 0;
            while (true) {
                int[] iArr2 = placeForPhoto.radius;
                if (i4 >= iArr2.length) {
                    break;
                }
                this.animateFromRadius[i4] = iArr2[i4];
                i4++;
            }
        } else {
            this.animateFromRadius = null;
        }
        float f2 = placeForPhoto.viewX;
        float f3 = rectF.left;
        this.translationX = ((f2 + f3) + (width / 2.0f)) - (i3 / 2);
        this.translationY = ((placeForPhoto.viewY + rectF.top) + (height / 2.0f)) - (r4 / 2);
        this.clipHorizontal = Math.abs(f3 - placeForPhoto.imageReceiver.getImageX());
        int abs = (int) Math.abs(rectF.top - placeForPhoto.imageReceiver.getImageY());
        int[] iArr3 = new int[2];
        placeForPhoto.parentView.getLocationInWindow(iArr3);
        int i5 = iArr3[1];
        int i6 = Build.VERSION.SDK_INT;
        float f4 = ((i5 - (i6 >= 21 ? 0 : AndroidUtilities.statusBarHeight)) - (placeForPhoto.viewY + rectF.top)) + placeForPhoto.clipTopAddition;
        this.clipTop = f4;
        float f5 = abs;
        this.clipTop = Math.max(0.0f, Math.max(f4, f5));
        float height2 = (((placeForPhoto.viewY + rectF.top) + ((int) height)) - ((iArr3[1] + placeForPhoto.parentView.getHeight()) - (i6 >= 21 ? 0 : AndroidUtilities.statusBarHeight))) + placeForPhoto.clipBottomAddition;
        this.clipBottom = height2;
        this.clipBottom = Math.max(0.0f, Math.max(height2, f5));
        this.clipTopOrigin = 0.0f;
        this.clipTopOrigin = Math.max(0.0f, Math.max(0.0f, f5));
        this.clipBottomOrigin = 0.0f;
        this.clipBottomOrigin = Math.max(0.0f, Math.max(0.0f, f5));
        this.animationStartTime = System.currentTimeMillis();
        this.animateToX = 0.0f;
        this.animateToY = 0.0f;
        this.animateToClipBottom = 0.0f;
        this.animateToClipBottomOrigin = 0.0f;
        this.animateToClipHorizontal = 0.0f;
        this.animateToClipTop = 0.0f;
        this.animateToClipTopOrigin = 0.0f;
        this.animateToScale = 1.0f;
        this.animateToRadius = true;
        this.zoomAnimation = true;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagesDeleted);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateMessageMedia);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didCreatedNewDeleteTask);
        this.currentDialogId = MessageObject.getPeerId(messageObject.messageOwner.peer_id);
        this.currentMessageObject = messageObject;
        TLRPC.Document document = messageObject.getDocument();
        ImageReceiver.BitmapHolder bitmapHolder = this.currentThumb;
        if (bitmapHolder != null) {
            bitmapHolder.release();
            this.currentThumb = null;
        }
        this.currentThumb = placeForPhoto.imageReceiver.getThumbBitmapSafe();
        this.seekbarContainer.setVisibility(8);
        if (document != null) {
            int i7 = 0;
            while (true) {
                if (i7 >= document.attributes.size()) {
                    break;
                }
                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i7);
                if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                    TLRPC.TL_documentAttributeVideo tL_documentAttributeVideo = (TLRPC.TL_documentAttributeVideo) documentAttribute;
                    this.videoWidth = tL_documentAttributeVideo.w;
                    this.videoHeight = tL_documentAttributeVideo.h;
                    break;
                }
                i7++;
            }
            if (MessageObject.isGifDocument(document)) {
                this.actionBar.setTitle(LocaleController.getString(R.string.DisappearingGif));
                String str4 = messageObject.messageOwner.attachPath;
                imageLocation = (str4 == null || !messageObject.attachPathExists) ? ImageLocation.getForDocument(document) : ImageLocation.getForPath(str4);
                imageReceiver = this.centerImage;
                str3 = null;
                f = 1.0f;
                bitmapDrawable = this.currentThumb != null ? new BitmapDrawable(this.currentThumb.bitmap) : null;
                placeProviderObject = placeForPhoto;
                j = -1;
                obj = null;
                str2 = null;
                str = "window";
                messageObject2 = messageObject;
                z = true;
                i2 = 1;
            } else {
                placeProviderObject = placeForPhoto;
                obj = null;
                str = "window";
                z = true;
                f = 1.0f;
                this.playerRetryPlayCount = 1;
                this.actionBar.setTitle(LocaleController.getString(R.string.DisappearingVideo));
                File file = new File(messageObject.messageOwner.attachPath);
                if (!file.exists()) {
                    file = FileLoader.getInstance(this.currentAccount).getPathToMessage(messageObject.messageOwner);
                    File file2 = new File(file.getAbsolutePath() + ".enc");
                    if (file2.exists()) {
                        file = file2;
                    }
                }
                preparePlayer(file);
                this.isVideo = true;
                this.seekbarContainer.setVisibility(0);
                imageReceiver = this.centerImage;
                bitmapDrawable = this.currentThumb != null ? new BitmapDrawable(this.currentThumb.bitmap) : null;
                str2 = null;
                i2 = 2;
                imageLocation = null;
                str3 = null;
                j = -1;
                messageObject2 = messageObject;
            }
            imageReceiver.setImage(imageLocation, str3, bitmapDrawable, j, str2, messageObject2, i2);
            i = z;
            r15 = obj;
        } else {
            placeProviderObject = placeForPhoto;
            Object obj2 = null;
            str = "window";
            i = 1;
            i = 1;
            f = 1.0f;
            this.actionBar.setTitle(LocaleController.getString(R.string.DisappearingPhoto));
            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
            this.centerImage.setImage(ImageLocation.getForObject(closestPhotoSizeWithSize, messageObject.photoThumbsObject), (String) null, this.currentThumb != null ? new BitmapDrawable(this.currentThumb.bitmap) : null, -1L, (String) null, messageObject, 2);
            r15 = obj2;
            if (closestPhotoSizeWithSize != null) {
                this.videoWidth = closestPhotoSizeWithSize.w;
                this.videoHeight = closestPhotoSizeWithSize.h;
                r15 = obj2;
            }
        }
        setCurrentCaption(messageObject, "", false, false);
        setCurrentCaption(messageObject, messageObject.caption, false, i);
        toggleActionBar(i, false);
        showPlayButton(false, false);
        this.playButtonDrawable.setPause(i);
        if (this.ignoreDelete) {
            this.secretDeleteTimer.setOnce();
            this.secretDeleteTimer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SecretMediaViewer.this.lambda$openMedia$3(view);
                }
            });
        } else {
            this.secretDeleteTimer.setOnClickListener(r15);
        }
        try {
            if (this.windowView.getParent() != null) {
                ((WindowManager) this.parentActivity.getSystemService(str)).removeView(this.windowView);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        ((WindowManager) this.parentActivity.getSystemService(str)).addView(this.windowView, this.windowLayoutParams);
        this.secretDeleteTimer.invalidate();
        this.isVisible = i;
        Window window = this.parentActivity.getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            this.wasLightNavigationBar = AndroidUtilities.getLightNavigationBar(window);
            AndroidUtilities.setLightNavigationBar(window, false);
            AndroidUtilities.setLightNavigationBar((View) this.windowView, false);
            Activity activity = this.parentActivity;
            if (activity instanceof LaunchActivity) {
                this.wasNavigationBarColor = ((LaunchActivity) activity).getNavigationBarColor();
                ((LaunchActivity) this.parentActivity).animateNavigationBarColor(-16777216);
            } else {
                navigationBarColor = window.getNavigationBarColor();
                this.wasNavigationBarColor = navigationBarColor;
                AndroidUtilities.setNavigationBarColor(window, -16777216);
            }
        }
        AnimatorSet animatorSet = new AnimatorSet();
        this.imageMoveAnimation = animatorSet;
        ActionBar actionBar = this.actionBar;
        Property property = View.ALPHA;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(actionBar, (Property<ActionBar, Float>) property, 0.0f, 1.0f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.captionScrollView, (Property<PhotoViewer.CaptionScrollView, Float>) property, 0.0f, 1.0f);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.secretHint, (Property<HintView2, Float>) property, 0.0f, 1.0f);
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this.photoBackgroundDrawable, (Property<PhotoBackgroundDrawable, Integer>) AnimationProperties.COLOR_DRAWABLE_ALPHA, 0, NotificationCenter.newLocationAvailable);
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this, (Property<SecretMediaViewer, Float>) this.ANIMATION_VALUE, 0.0f, 1.0f);
        VideoPlayerControlFrameLayout videoPlayerControlFrameLayout = this.seekbarContainer;
        Property property2 = videoPlayerControlFrameLayout.SEEKBAR_ALPHA;
        float[] fArr = new float[i];
        fArr[0] = f;
        ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(videoPlayerControlFrameLayout, (Property<VideoPlayerControlFrameLayout, Float>) property2, fArr);
        VideoPlayerControlFrameLayout videoPlayerControlFrameLayout2 = this.seekbarContainer;
        if (!this.isVideo) {
            f = 0.0f;
        }
        float[] fArr2 = new float[i];
        fArr2[0] = f;
        ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(videoPlayerControlFrameLayout2, (Property<VideoPlayerControlFrameLayout, Float>) property, fArr2);
        Animator[] animatorArr = new Animator[7];
        animatorArr[0] = ofFloat;
        animatorArr[i] = ofFloat2;
        animatorArr[2] = ofFloat3;
        animatorArr[3] = ofInt;
        animatorArr[4] = ofFloat4;
        animatorArr[5] = ofFloat5;
        animatorArr[6] = ofFloat6;
        animatorSet.playTogether(animatorArr);
        this.photoAnimationInProgress = 3;
        this.photoAnimationEndRunnable = new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                SecretMediaViewer.this.lambda$openMedia$4(runnable, messageObject);
            }
        };
        this.imageMoveAnimation.setDuration(250L);
        this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SecretMediaViewer.14
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (SecretMediaViewer.this.photoAnimationEndRunnable != null) {
                    SecretMediaViewer.this.photoAnimationEndRunnable.run();
                    SecretMediaViewer.this.photoAnimationEndRunnable = null;
                }
            }
        });
        this.photoTransitionAnimationStartTime = System.currentTimeMillis();
        if (SharedConfig.getDevicePerformanceClass() == 0) {
            this.containerView.setLayerType(2, r15);
        }
        this.imageMoveAnimation.setInterpolator(new DecelerateInterpolator());
        this.photoBackgroundDrawable.frame = 0;
        final PhotoViewer.PlaceProviderObject placeProviderObject2 = placeProviderObject;
        this.photoBackgroundDrawable.drawRunnable = new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                SecretMediaViewer.this.lambda$openMedia$5(placeProviderObject2);
            }
        };
        this.imageMoveAnimation.start();
    }

    public void setAnimationValue(float f) {
        this.animationValue = f;
        this.containerView.invalidate();
    }

    public void setOnClose(Runnable runnable) {
        this.onClose = runnable;
    }

    public void setParentActivity(final Activity activity) {
        int i = UserConfig.selectedAccount;
        this.currentAccount = i;
        this.centerImage.setCurrentAccount(i);
        if (this.parentActivity == activity) {
            return;
        }
        this.parentActivity = activity;
        this.scroller = new Scroller(activity);
        FrameLayout frameLayout = new FrameLayout(activity) { // from class: org.telegram.ui.SecretMediaViewer.3
            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i2, int i3, int i4, int i5) {
                int systemWindowInsetLeft = (Build.VERSION.SDK_INT < 21 || SecretMediaViewer.this.lastInsets == null) ? 0 : WindowInsetsCompat$$ExternalSyntheticApiModelOutline0.m(SecretMediaViewer.this.lastInsets).getSystemWindowInsetLeft();
                SecretMediaViewer.this.containerView.layout(systemWindowInsetLeft, 0, SecretMediaViewer.this.containerView.getMeasuredWidth() + systemWindowInsetLeft, SecretMediaViewer.this.containerView.getMeasuredHeight());
                if (z) {
                    if (SecretMediaViewer.this.imageMoveAnimation == null) {
                        SecretMediaViewer.this.scale = 1.0f;
                        SecretMediaViewer.this.translationX = 0.0f;
                        SecretMediaViewer.this.translationY = 0.0f;
                    }
                    SecretMediaViewer secretMediaViewer = SecretMediaViewer.this;
                    secretMediaViewer.updateMinMax(secretMediaViewer.scale);
                }
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i2, int i3) {
                int systemWindowInsetLeft;
                int systemWindowInsetBottom;
                int systemWindowInsetRight;
                int size = View.MeasureSpec.getSize(i2);
                int size2 = View.MeasureSpec.getSize(i3);
                int i4 = Build.VERSION.SDK_INT;
                if (i4 < 21 || SecretMediaViewer.this.lastInsets == null) {
                    int i5 = AndroidUtilities.displaySize.y;
                    if (size2 > i5) {
                        size2 = i5;
                    }
                } else {
                    WindowInsets m = WindowInsetsCompat$$ExternalSyntheticApiModelOutline0.m(SecretMediaViewer.this.lastInsets);
                    if (AndroidUtilities.incorrectDisplaySizeFix) {
                        int i6 = AndroidUtilities.displaySize.y;
                        if (size2 > i6) {
                            size2 = i6;
                        }
                        size2 += AndroidUtilities.statusBarHeight;
                    }
                    systemWindowInsetBottom = m.getSystemWindowInsetBottom();
                    size2 -= systemWindowInsetBottom;
                    systemWindowInsetRight = m.getSystemWindowInsetRight();
                    size -= systemWindowInsetRight;
                }
                setMeasuredDimension(size, size2);
                if (i4 >= 21 && SecretMediaViewer.this.lastInsets != null) {
                    systemWindowInsetLeft = WindowInsetsCompat$$ExternalSyntheticApiModelOutline0.m(SecretMediaViewer.this.lastInsets).getSystemWindowInsetLeft();
                    size -= systemWindowInsetLeft;
                }
                SecretMediaViewer.this.containerView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
            }
        };
        this.windowView = frameLayout;
        frameLayout.setBackgroundDrawable(this.photoBackgroundDrawable);
        this.windowView.setFocusable(true);
        this.windowView.setFocusableInTouchMode(true);
        this.windowView.setClipChildren(false);
        this.windowView.setClipToPadding(false);
        this.containerView = new FrameLayoutDrawer(activity) { // from class: org.telegram.ui.SecretMediaViewer.4
            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i2, int i3, int i4, int i5) {
                super.onLayout(z, i2, i3, i4, i5);
                if (SecretMediaViewer.this.secretDeleteTimer != null) {
                    int currentActionBarHeight = ((ActionBar.getCurrentActionBarHeight() - SecretMediaViewer.this.secretDeleteTimer.getMeasuredHeight()) / 2) + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0);
                    SecretMediaViewer.this.secretDeleteTimer.layout(SecretMediaViewer.this.secretDeleteTimer.getLeft(), currentActionBarHeight, SecretMediaViewer.this.secretDeleteTimer.getRight(), SecretMediaViewer.this.secretDeleteTimer.getMeasuredHeight() + currentActionBarHeight);
                }
                if (SecretMediaViewer.this.secretHint != null && SecretMediaViewer.this.secretDeleteTimer != null) {
                    int currentActionBarHeight2 = ((((ActionBar.getCurrentActionBarHeight() - SecretMediaViewer.this.secretDeleteTimer.getMeasuredHeight()) / 2) + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)) + SecretMediaViewer.this.secretDeleteTimer.getMeasuredHeight()) - AndroidUtilities.dp(10.0f);
                    SecretMediaViewer.this.secretHint.layout(SecretMediaViewer.this.secretHint.getLeft(), currentActionBarHeight2, SecretMediaViewer.this.secretHint.getRight(), SecretMediaViewer.this.secretHint.getMeasuredHeight() + currentActionBarHeight2);
                }
                if (SecretMediaViewer.this.captionScrollView != null) {
                    int currentActionBarHeight3 = ActionBar.getCurrentActionBarHeight() + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0);
                    SecretMediaViewer.this.captionScrollView.layout(SecretMediaViewer.this.captionScrollView.getLeft(), currentActionBarHeight3, SecretMediaViewer.this.captionScrollView.getRight(), SecretMediaViewer.this.captionScrollView.getMeasuredHeight() + currentActionBarHeight3);
                }
                if (SecretMediaViewer.this.navigationBar != null) {
                    int i6 = i5 - i3;
                    SecretMediaViewer.this.navigationBar.layout(0, i6, i4 - i2, AndroidUtilities.navigationBarHeight + i6);
                }
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i2, int i3) {
                super.onMeasure(i2, i3);
                int measuredWidth = getMeasuredWidth();
                int measuredHeight = getMeasuredHeight();
                if (SecretMediaViewer.this.captionScrollView != null) {
                    SecretMediaViewer.this.captionScrollView.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(((measuredHeight - ActionBar.getCurrentActionBarHeight()) - (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)) - (SecretMediaViewer.this.seekbarContainer.getVisibility() == 0 ? SecretMediaViewer.this.seekbarContainer.getMeasuredHeight() : 0), 1073741824));
                }
                if (SecretMediaViewer.this.navigationBar != null) {
                    SecretMediaViewer.this.navigationBar.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.navigationBarHeight, 1073741824));
                }
            }
        };
        View view = new View(activity);
        this.navigationBar = view;
        view.setBackgroundColor(2130706432);
        this.containerView.addView(this.navigationBar, LayoutHelper.createFrame(-1, -2, 80));
        this.containerView.setFocusable(false);
        this.windowView.addView(this.containerView);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.containerView.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.gravity = 51;
        this.containerView.setLayoutParams(layoutParams);
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 21) {
            this.containerView.setFitsSystemWindows(true);
            this.containerView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda5
                @Override // android.view.View.OnApplyWindowInsetsListener
                public final WindowInsets onApplyWindowInsets(View view2, WindowInsets windowInsets) {
                    WindowInsets lambda$setParentActivity$1;
                    lambda$setParentActivity$1 = SecretMediaViewer.this.lambda$setParentActivity$1(view2, windowInsets);
                    return lambda$setParentActivity$1;
                }
            });
            this.containerView.setSystemUiVisibility(1792);
        }
        GestureDetector gestureDetector = new GestureDetector(this.containerView.getContext(), this);
        this.gestureDetector = gestureDetector;
        gestureDetector.setOnDoubleTapListener(this);
        ActionBar actionBar = new ActionBar(activity) { // from class: org.telegram.ui.SecretMediaViewer.5
            @Override // android.view.View
            public void setAlpha(float f) {
                super.setAlpha(f);
                SecretMediaViewer.this.secretHint.setAlpha(f);
                SecretMediaViewer.this.secretDeleteTimer.setAlpha(f);
            }
        };
        this.actionBar = actionBar;
        actionBar.setTitleColor(-1);
        this.actionBar.setSubtitleColor(-1);
        this.actionBar.setBackgroundColor(2130706432);
        this.actionBar.setOccupyStatusBar(i2 >= 21);
        this.actionBar.setItemsBackgroundColor(1090519039, false);
        this.actionBar.setItemsColor(-1, false);
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setTitleRightMargin(AndroidUtilities.dp(70.0f));
        this.containerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.SecretMediaViewer.6
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i3) {
                if (i3 == -1) {
                    SecretMediaViewer.this.closePhoto(true, false);
                }
            }
        });
        HintView2 hintView2 = new HintView2(activity, 1);
        this.secretHint = hintView2;
        hintView2.setJoint(1.0f, -26.0f);
        this.secretHint.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
        this.containerView.addView(this.secretHint, LayoutHelper.createFrame(-1, 80.0f, 53, 0.0f, 48.0f, 0.0f, 0.0f));
        SecretDeleteTimer secretDeleteTimer = new SecretDeleteTimer(activity);
        this.secretDeleteTimer = secretDeleteTimer;
        this.containerView.addView(secretDeleteTimer, LayoutHelper.createFrame(119, 48.0f, 53, 0.0f, 0.0f, 0.0f, 0.0f));
        VideoPlayerSeekBar.SeekBarDelegate seekBarDelegate = new VideoPlayerSeekBar.SeekBarDelegate() { // from class: org.telegram.ui.SecretMediaViewer.7
            @Override // org.telegram.ui.Components.VideoPlayerSeekBar.SeekBarDelegate
            public void onSeekBarContinuousDrag(float f) {
                if (SecretMediaViewer.this.videoPlayer != null) {
                    SecretMediaViewer.this.videoPlayer.pause();
                    long duration = SecretMediaViewer.this.videoPlayer.getDuration();
                    if (duration != -9223372036854775807L) {
                        SecretMediaViewer.this.videoPlayer.seekTo((long) (f * duration), false);
                    }
                }
            }

            @Override // org.telegram.ui.Components.VideoPlayerSeekBar.SeekBarDelegate
            public void onSeekBarDrag(float f) {
                if (SecretMediaViewer.this.videoPlayer != null) {
                    long duration = SecretMediaViewer.this.videoPlayer.getDuration();
                    if (duration != -9223372036854775807L) {
                        SecretMediaViewer.this.videoPlayer.seekTo((long) (f * duration), false);
                    }
                    SecretMediaViewer.this.videoPlayer.play();
                }
            }
        };
        this.seekbarContainer = new VideoPlayerControlFrameLayout(activity);
        View view2 = new View(activity);
        this.seekbarBackground = view2;
        view2.setBackgroundColor(2130706432);
        this.seekbarContainer.addView(this.seekbarBackground, LayoutHelper.createFrame(-1, -1, 119));
        SimpleTextView simpleTextView = new SimpleTextView(this.containerView.getContext());
        this.videoPlayerTime = simpleTextView;
        simpleTextView.setTextColor(-1);
        this.videoPlayerTime.setGravity(53);
        this.videoPlayerTime.setTextSize(14);
        this.videoPlayerTime.setImportantForAccessibility(2);
        this.seekbarContainer.addView(this.videoPlayerTime, LayoutHelper.createFrame(-2, -2.0f, 53, 0.0f, 15.0f, 12.0f, 0.0f));
        View view3 = new View(activity) { // from class: org.telegram.ui.SecretMediaViewer.8
            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                SecretMediaViewer.this.seekbar.draw(canvas, this);
            }
        };
        this.seekbarView = view3;
        VideoPlayerSeekBar videoPlayerSeekBar = new VideoPlayerSeekBar(view3);
        this.seekbar = videoPlayerSeekBar;
        videoPlayerSeekBar.setHorizontalPadding(AndroidUtilities.dp(2.0f));
        this.seekbar.setColors(872415231, 872415231, -1, -1, -1, 1509949439);
        this.seekbar.setDelegate(seekBarDelegate);
        this.seekbarContainer.addView(this.seekbarView);
        this.containerView.addView(this.seekbarContainer, LayoutHelper.createFrame(-1, 48, 80));
        TextSelectionHelper.SimpleTextSelectionHelper simpleTextSelectionHelper = new TextSelectionHelper.SimpleTextSelectionHelper(null, new DarkThemeResourceProvider()) { // from class: org.telegram.ui.SecretMediaViewer.9
            @Override // org.telegram.ui.Cells.TextSelectionHelper
            public int getParentBottomPadding() {
                return 0;
            }
        };
        this.textSelectionHelper = simpleTextSelectionHelper;
        simpleTextSelectionHelper.allowScrollPrentRelative = true;
        simpleTextSelectionHelper.useMovingOffset = false;
        PhotoViewer.CaptionTextViewSwitcher captionTextViewSwitcher = new PhotoViewer.CaptionTextViewSwitcher(this.containerView.getContext());
        this.captionTextViewSwitcher = captionTextViewSwitcher;
        captionTextViewSwitcher.setFactory(new ViewSwitcher.ViewFactory() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda6
            @Override // android.widget.ViewSwitcher.ViewFactory
            public final View makeView() {
                View lambda$setParentActivity$2;
                lambda$setParentActivity$2 = SecretMediaViewer.this.lambda$setParentActivity$2(activity);
                return lambda$setParentActivity$2;
            }
        });
        this.captionTextViewSwitcher.setVisibility(4);
        setCaptionHwLayerEnabled(true);
        ImageView imageView = new ImageView(activity);
        this.playButton = imageView;
        imageView.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(64.0f), 1711276032));
        PlayPauseDrawable playPauseDrawable = new PlayPauseDrawable(28);
        this.playButtonDrawable = playPauseDrawable;
        playPauseDrawable.setCallback(this.playButton);
        this.playButton.setImageDrawable(this.playButtonDrawable);
        this.playButton.setScaleType(ImageView.ScaleType.CENTER);
        this.playButton.setScaleX(0.6f);
        this.playButton.setScaleY(0.6f);
        this.playButton.setAlpha(0.0f);
        this.playButton.setPivotX(AndroidUtilities.dp(32.0f));
        this.playButton.setPivotY(AndroidUtilities.dp(32.0f));
        this.containerView.addView(this.playButton, LayoutHelper.createFrame(64, 64, 17));
        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams();
        this.windowLayoutParams = layoutParams2;
        layoutParams2.height = -1;
        layoutParams2.format = -3;
        layoutParams2.width = -1;
        layoutParams2.gravity = 48;
        layoutParams2.type = 99;
        layoutParams2.flags = i2 >= 21 ? -2147417848 : 8;
        layoutParams2.flags |= LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM;
        this.centerImage.setParentView(this.containerView);
        this.centerImage.setForceCrossfade(true);
        TextSelectionHelper.TextSelectionOverlay overlayView = this.textSelectionHelper.getOverlayView(this.windowView.getContext());
        if (overlayView != null) {
            AndroidUtilities.removeFromParent(overlayView);
            this.containerView.addView(overlayView);
        }
        this.textSelectionHelper.setParentView(this.containerView);
        this.textSelectionHelper.setInvalidateParent();
    }

    public void setVideoCrossfadeAlpha(float f) {
        this.videoCrossfadeAlpha = f;
        this.containerView.invalidate();
    }
}
