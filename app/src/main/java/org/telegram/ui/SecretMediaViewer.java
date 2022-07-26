package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Property;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import androidx.annotation.Keep;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import java.io.File;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$TL_documentEmpty;
import org.telegram.tgnet.TLRPC$TL_photoEmpty;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Scroller;
import org.telegram.ui.Components.TimerParticles;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.SecretMediaViewer;
/* loaded from: classes3.dex */
public class SecretMediaViewer implements NotificationCenter.NotificationCenterDelegate, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    @SuppressLint({"StaticFieldLeak"})
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
    private int playerRetryPlayCount;
    private Scroller scroller;
    private SecretDeleteTimer secretDeleteTimer;
    private boolean textureUploaded;
    private float translationX;
    private float translationY;
    private boolean useOvershootForScale;
    private VelocityTracker velocityTracker;
    private float videoCrossfadeAlpha;
    private long videoCrossfadeAlphaLastTime;
    private boolean videoCrossfadeStarted;
    private VideoPlayer videoPlayer;
    private TextureView videoTextureView;
    private boolean videoWatchedOneTime;
    private boolean wasLightNavigationBar;
    private int wasNavigationBarColor;
    private WindowManager.LayoutParams windowLayoutParams;
    private FrameLayout windowView;
    private boolean zoomAnimation;
    private boolean zooming;
    private ImageReceiver centerImage = new ImageReceiver();
    private boolean isActionBarVisible = true;
    private PhotoBackgroundDrawable photoBackgroundDrawable = new PhotoBackgroundDrawable(-16777216);
    private Paint blackPaint = new Paint();
    private float scale = 1.0f;
    private DecelerateInterpolator interpolator = new DecelerateInterpolator(1.5f);
    private float pinchStartScale = 1.0f;
    private boolean canDragDown = true;
    private Path roundRectPath = new Path();

    @Override // android.view.GestureDetector.OnDoubleTapListener
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent motionEvent) {
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

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    static /* synthetic */ int access$1210(SecretMediaViewer secretMediaViewer) {
        int i = secretMediaViewer.playerRetryPlayCount;
        secretMediaViewer.playerRetryPlayCount = i - 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class FrameLayoutDrawer extends FrameLayout {
        public FrameLayoutDrawer(Context context) {
            super(context);
            setWillNotDraw(false);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            SecretMediaViewer.this.processTouchEvent(motionEvent);
            return true;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            SecretMediaViewer.this.onDraw(canvas);
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class SecretDeleteTimer extends FrameLayout {
        private Paint afterDeleteProgressPaint;
        private Paint circlePaint;
        private long destroyTime;
        private long destroyTtl;
        private Drawable drawable;
        private Paint particlePaint;
        private boolean useVideoProgress;
        private RectF deleteProgressRect = new RectF();
        private TimerParticles timerParticles = new TimerParticles();

        public SecretDeleteTimer(Context context) {
            super(context);
            setWillNotDraw(false);
            Paint paint = new Paint(1);
            this.particlePaint = paint;
            paint.setStrokeWidth(AndroidUtilities.dp(1.5f));
            this.particlePaint.setColor(-1644826);
            this.particlePaint.setStrokeCap(Paint.Cap.ROUND);
            this.particlePaint.setStyle(Paint.Style.STROKE);
            Paint paint2 = new Paint(1);
            this.afterDeleteProgressPaint = paint2;
            paint2.setStyle(Paint.Style.STROKE);
            this.afterDeleteProgressPaint.setStrokeCap(Paint.Cap.ROUND);
            this.afterDeleteProgressPaint.setColor(-1644826);
            this.afterDeleteProgressPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
            Paint paint3 = new Paint(1);
            this.circlePaint = paint3;
            paint3.setColor(2130706432);
            this.drawable = context.getResources().getDrawable(R.drawable.flame_small);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDestroyTime(long j, long j2, boolean z) {
            this.destroyTime = j;
            this.destroyTtl = j2;
            this.useVideoProgress = z;
            invalidate();
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            int measuredHeight = (getMeasuredHeight() / 2) - (AndroidUtilities.dp(28.0f) / 2);
            this.deleteProgressRect.set(getMeasuredWidth() - AndroidUtilities.dp(49.0f), measuredHeight, getMeasuredWidth() - AndroidUtilities.dp(21.0f), measuredHeight + AndroidUtilities.dp(28.0f));
        }

        @Override // android.view.View
        @SuppressLint({"DrawAllocation"})
        protected void onDraw(Canvas canvas) {
            if (SecretMediaViewer.this.currentMessageObject == null || SecretMediaViewer.this.currentMessageObject.messageOwner.destroyTime == 0) {
                return;
            }
            canvas.drawCircle(getMeasuredWidth() - AndroidUtilities.dp(35.0f), getMeasuredHeight() / 2, AndroidUtilities.dp(16.0f), this.circlePaint);
            float f = 1.0f;
            if (this.useVideoProgress) {
                if (SecretMediaViewer.this.videoPlayer != null) {
                    long duration = SecretMediaViewer.this.videoPlayer.getDuration();
                    long currentPosition = SecretMediaViewer.this.videoPlayer.getCurrentPosition();
                    if (duration != -9223372036854775807L && currentPosition != -9223372036854775807L) {
                        f = 1.0f - (((float) currentPosition) / ((float) duration));
                    }
                }
            } else {
                f = ((float) Math.max(0L, this.destroyTime - (System.currentTimeMillis() + (ConnectionsManager.getInstance(SecretMediaViewer.this.currentAccount).getTimeDifference() * 1000)))) / (((float) this.destroyTtl) * 1000.0f);
            }
            int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(40.0f);
            int measuredHeight = ((getMeasuredHeight() - AndroidUtilities.dp(14.0f)) / 2) - AndroidUtilities.dp(0.5f);
            this.drawable.setBounds(measuredWidth, measuredHeight, AndroidUtilities.dp(10.0f) + measuredWidth, AndroidUtilities.dp(14.0f) + measuredHeight);
            this.drawable.draw(canvas);
            float f2 = (-360.0f) * f;
            canvas.drawArc(this.deleteProgressRect, -90.0f, f2, false, this.afterDeleteProgressPaint);
            this.timerParticles.draw(canvas, this.particlePaint, this.deleteProgressRect, f2, 1.0f);
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class PhotoBackgroundDrawable extends ColorDrawable {
        private Runnable drawRunnable;
        private int frame;

        public PhotoBackgroundDrawable(int i) {
            super(i);
        }

        @Override // android.graphics.drawable.ColorDrawable, android.graphics.drawable.Drawable
        @Keep
        public void setAlpha(int i) {
            if (SecretMediaViewer.this.parentActivity instanceof LaunchActivity) {
                ((LaunchActivity) SecretMediaViewer.this.parentActivity).drawerLayoutContainer.setAllowDrawContent(!SecretMediaViewer.this.isPhotoVisible || i != 255);
            }
            super.setAlpha(i);
        }

        @Override // android.graphics.drawable.ColorDrawable, android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Runnable runnable;
            super.draw(canvas);
            if (getAlpha() != 0) {
                if (this.frame == 2 && (runnable = this.drawRunnable) != null) {
                    runnable.run();
                    this.drawRunnable = null;
                } else {
                    invalidateSelf();
                }
                this.frame++;
            }
        }
    }

    public static SecretMediaViewer getInstance() {
        SecretMediaViewer secretMediaViewer = Instance;
        if (secretMediaViewer == null) {
            synchronized (PhotoViewer.class) {
                secretMediaViewer = Instance;
                if (secretMediaViewer == null) {
                    secretMediaViewer = new SecretMediaViewer();
                    Instance = secretMediaViewer;
                }
            }
        }
        return secretMediaViewer;
    }

    public static boolean hasInstance() {
        return Instance != null;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.messagesDeleted) {
            if (((Boolean) objArr[2]).booleanValue() || this.currentMessageObject == null || ((Long) objArr[1]).longValue() != 0 || !((ArrayList) objArr[0]).contains(Integer.valueOf(this.currentMessageObject.getId()))) {
                return;
            }
            if (this.isVideo && !this.videoWatchedOneTime) {
                this.closeVideoAfterWatch = true;
            } else if (!closePhoto(true, true)) {
                this.closeAfterAnimation = true;
            }
        } else if (i == NotificationCenter.didCreatedNewDeleteTask) {
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
        } else if (i == NotificationCenter.updateMessageMedia && this.currentMessageObject.getId() == ((TLRPC$Message) objArr[0]).id) {
            if (this.isVideo && !this.videoWatchedOneTime) {
                this.closeVideoAfterWatch = true;
            } else if (!closePhoto(true, true)) {
                this.closeAfterAnimation = true;
            }
        }
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
            aspectRatioFrameLayout.setVisibility(4);
            this.containerView.addView(this.aspectRatioFrameLayout, 0, LayoutHelper.createFrame(-1, -1, 17));
            TextureView textureView = new TextureView(this.parentActivity);
            this.videoTextureView = textureView;
            textureView.setOpaque(false);
            this.aspectRatioFrameLayout.addView(this.videoTextureView, LayoutHelper.createFrame(-1, -1, 17));
        }
        this.textureUploaded = false;
        this.videoCrossfadeStarted = false;
        TextureView textureView2 = this.videoTextureView;
        this.videoCrossfadeAlpha = 0.0f;
        textureView2.setAlpha(0.0f);
        if (this.videoPlayer == null) {
            VideoPlayer videoPlayer = new VideoPlayer();
            this.videoPlayer = videoPlayer;
            videoPlayer.setTextureView(this.videoTextureView);
            this.videoPlayer.setDelegate(new 1(file));
        }
        this.videoPlayer.preparePlayer(Uri.fromFile(file), "other");
        this.videoPlayer.setPlayWhenReady(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 1 implements VideoPlayer.VideoPlayerDelegate {
        final /* synthetic */ File val$file;

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
        public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        1(File file) {
            this.val$file = file;
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onStateChanged(boolean z, int i) {
            if (SecretMediaViewer.this.videoPlayer == null || SecretMediaViewer.this.currentMessageObject == null) {
                return;
            }
            if (i == 4 || i == 1) {
                try {
                    SecretMediaViewer.this.parentActivity.getWindow().clearFlags(ConnectionsManager.RequestFlagNeedQuickAck);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            } else {
                try {
                    SecretMediaViewer.this.parentActivity.getWindow().addFlags(ConnectionsManager.RequestFlagNeedQuickAck);
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
            if (i == 3 && SecretMediaViewer.this.aspectRatioFrameLayout.getVisibility() != 0) {
                SecretMediaViewer.this.aspectRatioFrameLayout.setVisibility(0);
            }
            if (!SecretMediaViewer.this.videoPlayer.isPlaying() || i == 4) {
                if (!SecretMediaViewer.this.isPlaying) {
                    return;
                }
                SecretMediaViewer.this.isPlaying = false;
                if (i != 4) {
                    return;
                }
                SecretMediaViewer.this.videoWatchedOneTime = true;
                if (!SecretMediaViewer.this.closeVideoAfterWatch) {
                    SecretMediaViewer.this.videoPlayer.seekTo(0L);
                    SecretMediaViewer.this.videoPlayer.play();
                    return;
                }
                SecretMediaViewer.this.closePhoto(true, true);
            } else if (SecretMediaViewer.this.isPlaying) {
            } else {
                SecretMediaViewer.this.isPlaying = true;
            }
        }

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onError(VideoPlayer videoPlayer, Exception exc) {
            if (SecretMediaViewer.this.playerRetryPlayCount > 0) {
                SecretMediaViewer.access$1210(SecretMediaViewer.this);
                final File file = this.val$file;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        SecretMediaViewer.1.this.lambda$onError$0(file);
                    }
                }, 100L);
                return;
            }
            FileLog.e(exc);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onError$0(File file) {
            SecretMediaViewer.this.preparePlayer(file);
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

        @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
        public void onRenderedFirstFrame() {
            if (!SecretMediaViewer.this.textureUploaded) {
                SecretMediaViewer.this.textureUploaded = true;
                SecretMediaViewer.this.containerView.invalidate();
            }
        }
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
                activity.getWindow().clearFlags(ConnectionsManager.RequestFlagNeedQuickAck);
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

    public void setParentActivity(Activity activity) {
        int i = UserConfig.selectedAccount;
        this.currentAccount = i;
        this.centerImage.setCurrentAccount(i);
        if (this.parentActivity == activity) {
            return;
        }
        this.parentActivity = activity;
        this.scroller = new Scroller(activity);
        FrameLayout frameLayout = new FrameLayout(activity) { // from class: org.telegram.ui.SecretMediaViewer.2
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i2, int i3) {
                int size = View.MeasureSpec.getSize(i2);
                int size2 = View.MeasureSpec.getSize(i3);
                int i4 = Build.VERSION.SDK_INT;
                if (i4 >= 21 && SecretMediaViewer.this.lastInsets != null) {
                    WindowInsets windowInsets = (WindowInsets) SecretMediaViewer.this.lastInsets;
                    if (AndroidUtilities.incorrectDisplaySizeFix) {
                        int i5 = AndroidUtilities.displaySize.y;
                        if (size2 > i5) {
                            size2 = i5;
                        }
                        size2 += AndroidUtilities.statusBarHeight;
                    }
                    size2 -= windowInsets.getSystemWindowInsetBottom();
                    size -= windowInsets.getSystemWindowInsetRight();
                } else {
                    int i6 = AndroidUtilities.displaySize.y;
                    if (size2 > i6) {
                        size2 = i6;
                    }
                }
                setMeasuredDimension(size, size2);
                if (i4 >= 21 && SecretMediaViewer.this.lastInsets != null) {
                    size -= ((WindowInsets) SecretMediaViewer.this.lastInsets).getSystemWindowInsetLeft();
                }
                SecretMediaViewer.this.containerView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i2, int i3, int i4, int i5) {
                int systemWindowInsetLeft = (Build.VERSION.SDK_INT < 21 || SecretMediaViewer.this.lastInsets == null) ? 0 : ((WindowInsets) SecretMediaViewer.this.lastInsets).getSystemWindowInsetLeft() + 0;
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

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                if (Build.VERSION.SDK_INT < 21 || !SecretMediaViewer.this.isVisible || SecretMediaViewer.this.lastInsets == null) {
                    return;
                }
                WindowInsets windowInsets = (WindowInsets) SecretMediaViewer.this.lastInsets;
                if (SecretMediaViewer.this.photoAnimationInProgress != 0) {
                    SecretMediaViewer.this.blackPaint.setAlpha(SecretMediaViewer.this.photoBackgroundDrawable.getAlpha());
                } else {
                    SecretMediaViewer.this.blackPaint.setAlpha(255);
                }
                canvas.drawRect(0.0f, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight() + windowInsets.getSystemWindowInsetBottom(), SecretMediaViewer.this.blackPaint);
            }
        };
        this.windowView = frameLayout;
        frameLayout.setBackgroundDrawable(this.photoBackgroundDrawable);
        this.windowView.setFocusable(true);
        this.windowView.setFocusableInTouchMode(true);
        FrameLayoutDrawer frameLayoutDrawer = new FrameLayoutDrawer(activity) { // from class: org.telegram.ui.SecretMediaViewer.3
            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i2, int i3, int i4, int i5) {
                super.onLayout(z, i2, i3, i4, i5);
                if (SecretMediaViewer.this.secretDeleteTimer != null) {
                    int currentActionBarHeight = ((ActionBar.getCurrentActionBarHeight() - SecretMediaViewer.this.secretDeleteTimer.getMeasuredHeight()) / 2) + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0);
                    SecretMediaViewer.this.secretDeleteTimer.layout(SecretMediaViewer.this.secretDeleteTimer.getLeft(), currentActionBarHeight, SecretMediaViewer.this.secretDeleteTimer.getRight(), SecretMediaViewer.this.secretDeleteTimer.getMeasuredHeight() + currentActionBarHeight);
                }
            }
        };
        this.containerView = frameLayoutDrawer;
        frameLayoutDrawer.setFocusable(false);
        this.windowView.addView(this.containerView);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.containerView.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.gravity = 51;
        this.containerView.setLayoutParams(layoutParams);
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 21) {
            this.containerView.setFitsSystemWindows(true);
            this.containerView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda0
                @Override // android.view.View.OnApplyWindowInsetsListener
                public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    WindowInsets lambda$setParentActivity$0;
                    lambda$setParentActivity$0 = SecretMediaViewer.this.lambda$setParentActivity$0(view, windowInsets);
                    return lambda$setParentActivity$0;
                }
            });
            this.containerView.setSystemUiVisibility(1280);
        }
        GestureDetector gestureDetector = new GestureDetector(this.containerView.getContext(), this);
        this.gestureDetector = gestureDetector;
        gestureDetector.setOnDoubleTapListener(this);
        ActionBar actionBar = new ActionBar(activity);
        this.actionBar = actionBar;
        actionBar.setTitleColor(-1);
        this.actionBar.setSubtitleColor(-1);
        this.actionBar.setBackgroundColor(2130706432);
        this.actionBar.setOccupyStatusBar(i2 >= 21);
        this.actionBar.setItemsBackgroundColor(1090519039, false);
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setTitleRightMargin(AndroidUtilities.dp(70.0f));
        this.containerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.SecretMediaViewer.4
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i3) {
                if (i3 == -1) {
                    SecretMediaViewer.this.closePhoto(true, false);
                }
            }
        });
        SecretDeleteTimer secretDeleteTimer = new SecretDeleteTimer(activity);
        this.secretDeleteTimer = secretDeleteTimer;
        this.containerView.addView(secretDeleteTimer, LayoutHelper.createFrame(119, 48.0f, 53, 0.0f, 0.0f, 0.0f, 0.0f));
        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams();
        this.windowLayoutParams = layoutParams2;
        layoutParams2.height = -1;
        layoutParams2.format = -3;
        layoutParams2.width = -1;
        layoutParams2.gravity = 48;
        layoutParams2.type = 99;
        if (i2 >= 21) {
            layoutParams2.flags = -2147417848;
        } else {
            layoutParams2.flags = 8;
        }
        layoutParams2.flags |= 8192;
        this.centerImage.setParentView(this.containerView);
        this.centerImage.setForceCrossfade(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ WindowInsets lambda$setParentActivity$0(View view, WindowInsets windowInsets) {
        WindowInsets windowInsets2 = (WindowInsets) this.lastInsets;
        this.lastInsets = windowInsets;
        if (windowInsets2 == null || !windowInsets2.toString().equals(windowInsets.toString())) {
            this.windowView.requestLayout();
        }
        if (Build.VERSION.SDK_INT >= 30) {
            return WindowInsets.CONSUMED;
        }
        return windowInsets.consumeSystemWindowInsets();
    }

    public void openMedia(MessageObject messageObject, PhotoViewer.PhotoViewerProvider photoViewerProvider, final Runnable runnable) {
        final PhotoViewer.PlaceProviderObject placeForPhoto;
        int i;
        int i2;
        char c;
        if (this.parentActivity == null || messageObject == null || !messageObject.needDrawBluredPreview() || photoViewerProvider == null || (placeForPhoto = photoViewerProvider.getPlaceForPhoto(messageObject, null, 0, true)) == null) {
            return;
        }
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
        RectF drawRegion = placeForPhoto.imageReceiver.getDrawRegion();
        float width = drawRegion.width();
        float height = drawRegion.height();
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
        float f = drawRegion.left;
        this.translationX = ((placeForPhoto.viewX + f) + (width / 2.0f)) - (i3 / 2);
        this.translationY = ((placeForPhoto.viewY + drawRegion.top) + (height / 2.0f)) - (i / 2);
        this.clipHorizontal = Math.abs(f - placeForPhoto.imageReceiver.getImageX());
        int abs = (int) Math.abs(drawRegion.top - placeForPhoto.imageReceiver.getImageY());
        int[] iArr3 = new int[2];
        placeForPhoto.parentView.getLocationInWindow(iArr3);
        int i5 = iArr3[1];
        int i6 = Build.VERSION.SDK_INT;
        float f2 = ((i5 - (i6 >= 21 ? 0 : AndroidUtilities.statusBarHeight)) - (placeForPhoto.viewY + drawRegion.top)) + placeForPhoto.clipTopAddition;
        this.clipTop = f2;
        float f3 = abs;
        this.clipTop = Math.max(0.0f, Math.max(f2, f3));
        float height2 = (((placeForPhoto.viewY + drawRegion.top) + ((int) height)) - ((iArr3[1] + placeForPhoto.parentView.getHeight()) - (i6 >= 21 ? 0 : AndroidUtilities.statusBarHeight))) + placeForPhoto.clipBottomAddition;
        this.clipBottom = height2;
        this.clipBottom = Math.max(0.0f, Math.max(height2, f3));
        this.clipTopOrigin = 0.0f;
        this.clipTopOrigin = Math.max(0.0f, Math.max(0.0f, f3));
        this.clipBottomOrigin = 0.0f;
        this.clipBottomOrigin = Math.max(0.0f, Math.max(0.0f, f3));
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
        toggleActionBar(true, false);
        this.currentMessageObject = messageObject;
        TLRPC$Document document = messageObject.getDocument();
        ImageReceiver.BitmapHolder bitmapHolder = this.currentThumb;
        if (bitmapHolder != null) {
            bitmapHolder.release();
            this.currentThumb = null;
        }
        this.currentThumb = placeForPhoto.imageReceiver.getThumbBitmapSafe();
        if (document != null) {
            if (MessageObject.isGifDocument(document)) {
                this.actionBar.setTitle(LocaleController.getString("DisappearingGif", R.string.DisappearingGif));
                i2 = 21;
                c = 4;
                this.centerImage.setImage(ImageLocation.getForDocument(document), (String) null, this.currentThumb != null ? new BitmapDrawable(this.currentThumb.bitmap) : null, -1L, (String) null, messageObject, 1);
                SecretDeleteTimer secretDeleteTimer = this.secretDeleteTimer;
                TLRPC$Message tLRPC$Message = messageObject.messageOwner;
                secretDeleteTimer.setDestroyTime(tLRPC$Message.destroyTime * 1000, tLRPC$Message.ttl, false);
            } else {
                i2 = 21;
                c = 4;
                this.playerRetryPlayCount = 1;
                this.actionBar.setTitle(LocaleController.getString("DisappearingVideo", R.string.DisappearingVideo));
                File file = new File(messageObject.messageOwner.attachPath);
                if (file.exists()) {
                    preparePlayer(file);
                } else {
                    File pathToMessage = FileLoader.getInstance(this.currentAccount).getPathToMessage(messageObject.messageOwner);
                    File file2 = new File(pathToMessage.getAbsolutePath() + ".enc");
                    if (file2.exists()) {
                        pathToMessage = file2;
                    }
                    preparePlayer(pathToMessage);
                }
                this.isVideo = true;
                this.centerImage.setImage((ImageLocation) null, (String) null, this.currentThumb != null ? new BitmapDrawable(this.currentThumb.bitmap) : null, -1L, (String) null, messageObject, 2);
                if (messageObject.getDuration() * 1000 > (messageObject.messageOwner.destroyTime * 1000) - (System.currentTimeMillis() + (ConnectionsManager.getInstance(this.currentAccount).getTimeDifference() * 1000))) {
                    this.secretDeleteTimer.setDestroyTime(-1L, -1L, true);
                } else {
                    SecretDeleteTimer secretDeleteTimer2 = this.secretDeleteTimer;
                    TLRPC$Message tLRPC$Message2 = messageObject.messageOwner;
                    secretDeleteTimer2.setDestroyTime(tLRPC$Message2.destroyTime * 1000, tLRPC$Message2.ttl, false);
                }
            }
        } else {
            i2 = 21;
            c = 4;
            this.actionBar.setTitle(LocaleController.getString("DisappearingPhoto", R.string.DisappearingPhoto));
            this.centerImage.setImage(ImageLocation.getForObject(FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize()), messageObject.photoThumbsObject), (String) null, this.currentThumb != null ? new BitmapDrawable(this.currentThumb.bitmap) : null, -1L, (String) null, messageObject, 2);
            SecretDeleteTimer secretDeleteTimer3 = this.secretDeleteTimer;
            TLRPC$Message tLRPC$Message3 = messageObject.messageOwner;
            secretDeleteTimer3.setDestroyTime(tLRPC$Message3.destroyTime * 1000, tLRPC$Message3.ttl, false);
        }
        try {
            if (this.windowView.getParent() != null) {
                ((WindowManager) this.parentActivity.getSystemService("window")).removeView(this.windowView);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        ((WindowManager) this.parentActivity.getSystemService("window")).addView(this.windowView, this.windowLayoutParams);
        this.secretDeleteTimer.invalidate();
        this.isVisible = true;
        Window window = this.parentActivity.getWindow();
        int i7 = Build.VERSION.SDK_INT;
        if (i7 >= i2) {
            this.wasNavigationBarColor = window.getNavigationBarColor();
            this.wasLightNavigationBar = AndroidUtilities.getLightNavigationBar(window);
            AndroidUtilities.setLightNavigationBar(window, false);
            AndroidUtilities.setNavigationBarColor(window, -16777216);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        this.imageMoveAnimation = animatorSet;
        Animator[] animatorArr = new Animator[5];
        animatorArr[0] = ObjectAnimator.ofFloat(this.actionBar, View.ALPHA, 0.0f, 1.0f);
        animatorArr[1] = ObjectAnimator.ofFloat(this.secretDeleteTimer, View.ALPHA, 0.0f, 1.0f);
        animatorArr[2] = ObjectAnimator.ofInt(this.photoBackgroundDrawable, (Property<PhotoBackgroundDrawable, Integer>) AnimationProperties.COLOR_DRAWABLE_ALPHA, 0, 255);
        animatorArr[3] = ObjectAnimator.ofFloat(this.secretDeleteTimer, View.ALPHA, 0.0f, 1.0f);
        animatorArr[c] = ObjectAnimator.ofFloat(this, "animationValue", 0.0f, 1.0f);
        animatorSet.playTogether(animatorArr);
        this.photoAnimationInProgress = 3;
        this.photoAnimationEndRunnable = new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                SecretMediaViewer.this.lambda$openMedia$1(runnable);
            }
        };
        this.imageMoveAnimation.setDuration(250L);
        this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SecretMediaViewer.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (SecretMediaViewer.this.photoAnimationEndRunnable != null) {
                    SecretMediaViewer.this.photoAnimationEndRunnable.run();
                    SecretMediaViewer.this.photoAnimationEndRunnable = null;
                }
            }
        });
        this.photoTransitionAnimationStartTime = System.currentTimeMillis();
        if (i7 >= 18) {
            this.containerView.setLayerType(2, null);
        }
        this.imageMoveAnimation.setInterpolator(new DecelerateInterpolator());
        this.photoBackgroundDrawable.frame = 0;
        this.photoBackgroundDrawable.drawRunnable = new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                SecretMediaViewer.this.lambda$openMedia$2(placeForPhoto);
            }
        };
        this.imageMoveAnimation.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openMedia$1(Runnable runnable) {
        this.photoAnimationInProgress = 0;
        this.imageMoveAnimation = null;
        if (runnable != null) {
            runnable.run();
        }
        FrameLayoutDrawer frameLayoutDrawer = this.containerView;
        if (frameLayoutDrawer == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            frameLayoutDrawer.setLayerType(0, null);
        }
        this.containerView.invalidate();
        if (!this.closeAfterAnimation) {
            return;
        }
        closePhoto(true, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openMedia$2(PhotoViewer.PlaceProviderObject placeProviderObject) {
        this.disableShowCheck = false;
        placeProviderObject.imageReceiver.setVisible(false, true);
    }

    public boolean isShowingImage(MessageObject messageObject) {
        MessageObject messageObject2;
        return this.isVisible && !this.disableShowCheck && messageObject != null && (messageObject2 = this.currentMessageObject) != null && messageObject2.getId() == messageObject.getId();
    }

    private void toggleActionBar(boolean z, boolean z2) {
        if (z) {
            this.actionBar.setVisibility(0);
        }
        this.actionBar.setEnabled(z);
        this.isActionBarVisible = z;
        float f = 1.0f;
        if (z2) {
            ArrayList arrayList = new ArrayList();
            ActionBar actionBar = this.actionBar;
            Property property = View.ALPHA;
            float[] fArr = new float[1];
            if (!z) {
                f = 0.0f;
            }
            fArr[0] = f;
            arrayList.add(ObjectAnimator.ofFloat(actionBar, property, fArr));
            AnimatorSet animatorSet = new AnimatorSet();
            this.currentActionBarAnimation = animatorSet;
            animatorSet.playTogether(arrayList);
            if (!z) {
                this.currentActionBarAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SecretMediaViewer.6
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (SecretMediaViewer.this.currentActionBarAnimation == null || !SecretMediaViewer.this.currentActionBarAnimation.equals(animator)) {
                            return;
                        }
                        SecretMediaViewer.this.actionBar.setVisibility(8);
                        SecretMediaViewer.this.currentActionBarAnimation = null;
                    }
                });
            }
            this.currentActionBarAnimation.setDuration(200L);
            this.currentActionBarAnimation.start();
            return;
        }
        ActionBar actionBar2 = this.actionBar;
        if (!z) {
            f = 0.0f;
        }
        actionBar2.setAlpha(f);
        if (z) {
            return;
        }
        this.actionBar.setVisibility(8);
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void destroyPhotoViewer() {
        FrameLayout frameLayout;
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

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:69:0x02d4  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0315  */
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
        float f12;
        float f13;
        float f14;
        if (!this.isPhotoVisible) {
            return;
        }
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
                float f18 = this.translationY;
                f2 = f18 + ((this.animateToY - f18) * f14);
                float f19 = this.translationX;
                f3 = f19 + ((this.animateToX - f19) * f14);
                float f20 = this.clipTop;
                f4 = f20 + ((this.animateToClipTop - f20) * f14);
                float f21 = this.clipBottom;
                f5 = f21 + ((this.animateToClipBottom - f21) * f14);
                float f22 = this.clipTopOrigin;
                f6 = f22 + ((this.animateToClipTopOrigin - f22) * f14);
                float f23 = this.clipBottomOrigin;
                f7 = f23 + ((this.animateToClipBottomOrigin - f23) * f14);
                float f24 = this.clipHorizontal;
                f8 = f24 + ((this.animateToClipHorizontal - f24) * f14);
            } else {
                float f25 = this.scale;
                float f26 = this.animationValue;
                f = ((this.animateToScale - f25) * f26) + f25;
                float f27 = this.translationY;
                float f28 = f27 + ((this.animateToY - f27) * f26);
                float f29 = this.translationX;
                f3 = f29 + ((this.animateToX - f29) * f26);
                float f30 = this.clipTop;
                f4 = f30 + ((this.animateToClipTop - f30) * f26);
                float f31 = this.clipBottom;
                f5 = f31 + ((this.animateToClipBottom - f31) * f26);
                float f32 = this.clipTopOrigin;
                f6 = f32 + ((this.animateToClipTopOrigin - f32) * f26);
                float f33 = this.clipBottomOrigin;
                f7 = f33 + ((this.animateToClipBottomOrigin - f33) * f26);
                float f34 = this.clipHorizontal;
                f8 = f34 + ((this.animateToClipHorizontal - f34) * f26);
                f2 = f28;
            }
            f9 = (this.animateToScale == 1.0f && this.scale == 1.0f && this.translationX == 0.0f) ? f2 : -1.0f;
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
                float f35 = this.animateToScale;
                this.scale = f35;
                this.animationStartTime = 0L;
                updateMinMax(f35);
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
            float f36 = this.animateToRadius ? this.animationValue : 1.0f - this.animationValue;
            int i = 0;
            z = true;
            for (int i2 = 8; i < i2; i2 = 8) {
                float[] fArr = this.currentRadii;
                float lerp = AndroidUtilities.lerp(this.animateFromRadius[i / 2] * 2.0f, 0.0f, f36);
                fArr[i + 1] = lerp;
                fArr[i] = lerp;
                if (this.currentRadii[i] > 0.0f) {
                    z = false;
                }
                i += 2;
            }
        } else {
            z = true;
        }
        if (this.photoAnimationInProgress != 3) {
            if (this.scale == 1.0f && f9 != -1.0f && !this.zoomAnimation) {
                float containerViewHeight = getContainerViewHeight() / 4.0f;
                this.photoBackgroundDrawable.setAlpha((int) Math.max(127.0f, (1.0f - (Math.min(Math.abs(f9), containerViewHeight) / containerViewHeight)) * 255.0f));
            } else {
                this.photoBackgroundDrawable.setAlpha(255);
            }
            if (!this.zoomAnimation) {
                float f37 = this.maxX;
                if (f3 > f37) {
                    float min = Math.min(1.0f, (f3 - f37) / canvas.getWidth());
                    f11 = 0.3f * min;
                    f10 = 1.0f - min;
                    f3 = this.maxX;
                    AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
                    z2 = aspectRatioFrameLayout == null && aspectRatioFrameLayout.getVisibility() == 0;
                    canvas.save();
                    float f38 = f - f11;
                    canvas.translate((getContainerViewWidth() / 2) + f3, (getContainerViewHeight() / 2) + f2);
                    canvas.scale(f38, f38);
                    bitmapWidth = this.centerImage.getBitmapWidth();
                    bitmapHeight = this.centerImage.getBitmapHeight();
                    if (z2 && this.textureUploaded && Math.abs((bitmapWidth / bitmapHeight) - (this.videoTextureView.getMeasuredWidth() / this.videoTextureView.getMeasuredHeight())) > 0.01f) {
                        bitmapWidth = this.videoTextureView.getMeasuredWidth();
                        bitmapHeight = this.videoTextureView.getMeasuredHeight();
                    }
                    float f39 = bitmapHeight;
                    float f40 = bitmapWidth;
                    float min2 = Math.min(getContainerViewHeight() / f39, getContainerViewWidth() / f40);
                    int i3 = (int) (f40 * min2);
                    int i4 = (int) (f39 * min2);
                    f12 = (-i3) / 2;
                    float f41 = f8 / f38;
                    float f42 = f12 + f41;
                    f13 = (-i4) / 2;
                    float f43 = (i3 / 2) - f41;
                    float f44 = i4 / 2;
                    canvas.clipRect(f42, (f4 / f38) + f13, f43, f44 - (f5 / f38));
                    if (!z) {
                        this.roundRectPath.reset();
                        RectF rectF = AndroidUtilities.rectTmp;
                        rectF.set(f42, (f6 / f38) + f13, f43, f44 - (f7 / f38));
                        this.roundRectPath.addRoundRect(rectF, this.currentRadii, Path.Direction.CW);
                        canvas.clipPath(this.roundRectPath);
                    }
                    if (z2 || !this.textureUploaded || !this.videoCrossfadeStarted || this.videoCrossfadeAlpha != 1.0f) {
                        this.centerImage.setAlpha(f10);
                        this.centerImage.setImageCoords(f12, f13, i3, i4);
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
                            this.videoCrossfadeAlpha += ((float) j) / 200.0f;
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
        float f382 = f - f11;
        canvas.translate((getContainerViewWidth() / 2) + f3, (getContainerViewHeight() / 2) + f2);
        canvas.scale(f382, f382);
        bitmapWidth = this.centerImage.getBitmapWidth();
        bitmapHeight = this.centerImage.getBitmapHeight();
        if (z2) {
            bitmapWidth = this.videoTextureView.getMeasuredWidth();
            bitmapHeight = this.videoTextureView.getMeasuredHeight();
        }
        float f392 = bitmapHeight;
        float f402 = bitmapWidth;
        float min22 = Math.min(getContainerViewHeight() / f392, getContainerViewWidth() / f402);
        int i32 = (int) (f402 * min22);
        int i42 = (int) (f392 * min22);
        f12 = (-i32) / 2;
        float f412 = f8 / f382;
        float f422 = f12 + f412;
        f13 = (-i42) / 2;
        float f432 = (i32 / 2) - f412;
        float f442 = i42 / 2;
        canvas.clipRect(f422, (f4 / f382) + f13, f432, f442 - (f5 / f382));
        if (!z) {
        }
        if (z2) {
        }
        this.centerImage.setAlpha(f10);
        this.centerImage.setImageCoords(f12, f13, i32, i42);
        this.centerImage.draw(canvas);
        if (z2) {
        }
        canvas.restore();
    }

    @Keep
    public float getVideoCrossfadeAlpha() {
        return this.videoCrossfadeAlpha;
    }

    @Keep
    public void setVideoCrossfadeAlpha(float f) {
        this.videoCrossfadeAlpha = f;
        this.containerView.invalidate();
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

    public long getOpenTime() {
        return this.openTime;
    }

    public long getCloseTime() {
        return this.closeTime;
    }

    public MessageObject getCurrentMessageObject() {
        return this.currentMessageObject;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x007a  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0277  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean closePhoto(boolean z, boolean z2) {
        final PhotoViewer.PlaceProviderObject placeProviderObject;
        VideoPlayer videoPlayer;
        int i;
        int[] iArr;
        if (this.parentActivity == null || !this.isPhotoVisible || checkPhotoAnimation()) {
            return false;
        }
        Activity activity = this.parentActivity;
        if (activity != null) {
            Window window = activity.getWindow();
            AndroidUtilities.setLightNavigationBar(window, this.wasLightNavigationBar);
            AndroidUtilities.setNavigationBarColor(window, this.wasNavigationBarColor);
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
            TLRPC$MessageMedia tLRPC$MessageMedia = messageObject.messageOwner.media;
            if (!(tLRPC$MessageMedia.photo instanceof TLRPC$TL_photoEmpty) && !(tLRPC$MessageMedia.document instanceof TLRPC$TL_documentEmpty)) {
                placeProviderObject = photoViewerProvider.getPlaceForPhoto(messageObject, null, 0, true);
                videoPlayer = this.videoPlayer;
                if (videoPlayer != null) {
                    videoPlayer.pause();
                }
                if (!z) {
                    this.photoAnimationInProgress = 3;
                    this.containerView.invalidate();
                    this.imageMoveAnimation = new AnimatorSet();
                    if (placeProviderObject != null && placeProviderObject.imageReceiver.getThumbBitmap() != null && !z2) {
                        placeProviderObject.imageReceiver.setVisible(false, true);
                        RectF drawRegion = placeProviderObject.imageReceiver.getDrawRegion();
                        float f = drawRegion.right - drawRegion.left;
                        float f2 = drawRegion.bottom - drawRegion.top;
                        Point point = AndroidUtilities.displaySize;
                        int i2 = point.x;
                        int i3 = point.y;
                        int i4 = Build.VERSION.SDK_INT;
                        this.animateToScale = Math.max(f / i2, f2 / (i3 + (i4 >= 21 ? AndroidUtilities.statusBarHeight : 0)));
                        float f3 = drawRegion.left;
                        this.animateToX = ((placeProviderObject.viewX + f3) + (f / 2.0f)) - (i2 / 2);
                        this.animateToY = ((placeProviderObject.viewY + drawRegion.top) + (f2 / 2.0f)) - (i / 2);
                        this.animateToClipHorizontal = Math.abs(f3 - placeProviderObject.imageReceiver.getImageX());
                        int abs = (int) Math.abs(drawRegion.top - placeProviderObject.imageReceiver.getImageY());
                        placeProviderObject.parentView.getLocationInWindow(new int[2]);
                        float f4 = ((iArr[1] - (i4 >= 21 ? 0 : AndroidUtilities.statusBarHeight)) - (placeProviderObject.viewY + drawRegion.top)) + placeProviderObject.clipTopAddition;
                        this.animateToClipTop = f4;
                        float f5 = abs;
                        this.animateToClipTop = Math.max(0.0f, Math.max(f4, f5));
                        float height = (((placeProviderObject.viewY + drawRegion.top) + ((int) f2)) - ((iArr[1] + placeProviderObject.parentView.getHeight()) - (i4 >= 21 ? 0 : AndroidUtilities.statusBarHeight))) + placeProviderObject.clipBottomAddition;
                        this.animateToClipBottom = height;
                        this.animateToClipBottom = Math.max(0.0f, Math.max(height, f5));
                        this.animateToClipTopOrigin = 0.0f;
                        this.animateToClipTopOrigin = Math.max(0.0f, Math.max(0.0f, f5));
                        this.animateToClipBottomOrigin = 0.0f;
                        this.animateToClipBottomOrigin = Math.max(0.0f, Math.max(0.0f, f5));
                        this.animationStartTime = System.currentTimeMillis();
                        this.zoomAnimation = true;
                    } else {
                        int i5 = AndroidUtilities.displaySize.y + (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0);
                        if (this.translationY < 0.0f) {
                            i5 = -i5;
                        }
                        this.animateToY = i5;
                    }
                    this.animateToRadius = false;
                    if (this.isVideo) {
                        this.videoCrossfadeStarted = false;
                        this.textureUploaded = false;
                        this.imageMoveAnimation.playTogether(ObjectAnimator.ofInt(this.photoBackgroundDrawable, (Property<PhotoBackgroundDrawable, Integer>) AnimationProperties.COLOR_DRAWABLE_ALPHA, 0), ObjectAnimator.ofFloat(this, "animationValue", 0.0f, 1.0f), ObjectAnimator.ofFloat(this.actionBar, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.secretDeleteTimer, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this, "videoCrossfadeAlpha", 0.0f));
                    } else {
                        this.centerImage.setManualAlphaAnimator(true);
                        this.imageMoveAnimation.playTogether(ObjectAnimator.ofInt(this.photoBackgroundDrawable, (Property<PhotoBackgroundDrawable, Integer>) AnimationProperties.COLOR_DRAWABLE_ALPHA, 0), ObjectAnimator.ofFloat(this, "animationValue", 0.0f, 1.0f), ObjectAnimator.ofFloat(this.actionBar, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.secretDeleteTimer, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.centerImage, "currentAlpha", 0.0f));
                    }
                    this.photoAnimationEndRunnable = new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            SecretMediaViewer.this.lambda$closePhoto$3(placeProviderObject);
                        }
                    };
                    this.imageMoveAnimation.setInterpolator(new DecelerateInterpolator());
                    this.imageMoveAnimation.setDuration(250L);
                    this.imageMoveAnimation.addListener(new 7(placeProviderObject));
                    this.photoTransitionAnimationStartTime = System.currentTimeMillis();
                    if (Build.VERSION.SDK_INT >= 18) {
                        this.containerView.setLayerType(2, null);
                    }
                    this.imageMoveAnimation.start();
                } else {
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(ObjectAnimator.ofFloat(this.containerView, View.SCALE_X, 0.9f), ObjectAnimator.ofFloat(this.containerView, View.SCALE_Y, 0.9f), ObjectAnimator.ofInt(this.photoBackgroundDrawable, (Property<PhotoBackgroundDrawable, Integer>) AnimationProperties.COLOR_DRAWABLE_ALPHA, 0), ObjectAnimator.ofFloat(this.actionBar, View.ALPHA, 0.0f));
                    this.photoAnimationInProgress = 2;
                    this.photoAnimationEndRunnable = new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            SecretMediaViewer.this.lambda$closePhoto$4(placeProviderObject);
                        }
                    };
                    animatorSet.setDuration(200L);
                    animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SecretMediaViewer.8
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (SecretMediaViewer.this.photoAnimationEndRunnable != null) {
                                SecretMediaViewer.this.photoAnimationEndRunnable.run();
                                SecretMediaViewer.this.photoAnimationEndRunnable = null;
                            }
                        }
                    });
                    this.photoTransitionAnimationStartTime = System.currentTimeMillis();
                    if (Build.VERSION.SDK_INT >= 18) {
                        this.containerView.setLayerType(2, null);
                    }
                    animatorSet.start();
                }
                return true;
            }
        }
        placeProviderObject = null;
        videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
        }
        if (!z) {
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closePhoto$3(PhotoViewer.PlaceProviderObject placeProviderObject) {
        this.imageMoveAnimation = null;
        this.photoAnimationInProgress = 0;
        if (Build.VERSION.SDK_INT >= 18) {
            this.containerView.setLayerType(0, null);
        }
        this.containerView.setVisibility(4);
        onPhotoClosed(placeProviderObject);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 7 extends AnimatorListenerAdapter {
        final /* synthetic */ PhotoViewer.PlaceProviderObject val$object;

        7(PhotoViewer.PlaceProviderObject placeProviderObject) {
            this.val$object = placeProviderObject;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            PhotoViewer.PlaceProviderObject placeProviderObject = this.val$object;
            if (placeProviderObject != null) {
                placeProviderObject.imageReceiver.setVisible(true, true);
            }
            SecretMediaViewer.this.isVisible = false;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SecretMediaViewer.7.this.lambda$onAnimationEnd$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0() {
            if (SecretMediaViewer.this.photoAnimationEndRunnable != null) {
                SecretMediaViewer.this.photoAnimationEndRunnable.run();
                SecretMediaViewer.this.photoAnimationEndRunnable = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closePhoto$4(PhotoViewer.PlaceProviderObject placeProviderObject) {
        FrameLayoutDrawer frameLayoutDrawer = this.containerView;
        if (frameLayoutDrawer == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            frameLayoutDrawer.setLayerType(0, null);
        }
        this.containerView.setVisibility(4);
        this.photoAnimationInProgress = 0;
        onPhotoClosed(placeProviderObject);
        this.containerView.setScaleX(1.0f);
        this.containerView.setScaleY(1.0f);
    }

    private void onPhotoClosed(PhotoViewer.PlaceProviderObject placeProviderObject) {
        this.isVisible = false;
        this.currentProvider = null;
        this.disableShowCheck = false;
        releasePlayer();
        new ArrayList();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.SecretMediaViewer$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SecretMediaViewer.this.lambda$onPhotoClosed$5();
            }
        }, 50L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPhotoClosed$5() {
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
            return;
        }
        this.maxY = 0.0f;
        this.minY = 0.0f;
    }

    private int getContainerViewWidth() {
        return this.containerView.getWidth();
    }

    private int getContainerViewHeight() {
        return this.containerView.getHeight();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x027b, code lost:
        if (r0 > r3) goto L114;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x026c, code lost:
        if (r13 > r3) goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x02ee, code lost:
        if (r2 > r3) goto L140;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x02dd, code lost:
        if (r2 > r3) goto L137;
     */
    /* JADX WARN: Removed duplicated region for block: B:81:0x01de  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean processTouchEvent(MotionEvent motionEvent) {
        if (this.photoAnimationInProgress == 0 && this.animationStartTime == 0) {
            if (motionEvent.getPointerCount() == 1 && this.gestureDetector.onTouchEvent(motionEvent) && this.doubleTap) {
                this.doubleTap = false;
                this.moving = false;
                this.zooming = false;
                checkMinMax(false);
                return true;
            } else if (motionEvent.getActionMasked() == 0 || motionEvent.getActionMasked() == 5) {
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
                        VelocityTracker velocityTracker = this.velocityTracker;
                        if (velocityTracker != null) {
                            velocityTracker.clear();
                        }
                    } else if (motionEvent.getPointerCount() == 1) {
                        this.moveStartX = motionEvent.getX();
                        float y = motionEvent.getY();
                        this.moveStartY = y;
                        this.dragY = y;
                        this.draggingDown = false;
                        this.canDragDown = true;
                        VelocityTracker velocityTracker2 = this.velocityTracker;
                        if (velocityTracker2 != null) {
                            velocityTracker2.clear();
                        }
                    }
                }
            } else {
                float f = 0.0f;
                if (motionEvent.getActionMasked() == 2) {
                    if (motionEvent.getPointerCount() == 2 && !this.draggingDown && this.zooming) {
                        this.discardTap = true;
                        this.scale = (((float) Math.hypot(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0))) / this.pinchStartDistance) * this.pinchStartScale;
                        this.translationX = (this.pinchCenterX - (getContainerViewWidth() / 2)) - (((this.pinchCenterX - (getContainerViewWidth() / 2)) - this.pinchStartX) * (this.scale / this.pinchStartScale));
                        float containerViewHeight = this.pinchCenterY - (getContainerViewHeight() / 2);
                        float containerViewHeight2 = (this.pinchCenterY - (getContainerViewHeight() / 2)) - this.pinchStartY;
                        float f2 = this.scale;
                        this.translationY = containerViewHeight - (containerViewHeight2 * (f2 / this.pinchStartScale));
                        updateMinMax(f2);
                        this.containerView.invalidate();
                    } else if (motionEvent.getPointerCount() == 1) {
                        VelocityTracker velocityTracker3 = this.velocityTracker;
                        if (velocityTracker3 != null) {
                            velocityTracker3.addMovement(motionEvent);
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
                        } else if (this.draggingDown) {
                            this.translationY = motionEvent.getY() - this.dragY;
                            this.containerView.invalidate();
                        } else if (!this.invalidCoords && this.animationStartTime == 0) {
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
                                float f3 = this.translationX;
                                if (f3 < this.minX || f3 > this.maxX) {
                                    x /= 3.0f;
                                }
                                float f4 = this.maxY;
                                if (f4 == 0.0f) {
                                    float f5 = this.minY;
                                    if (f5 == 0.0f) {
                                        float f6 = this.translationY;
                                        if (f6 - y2 < f5) {
                                            this.translationY = f5;
                                        } else {
                                            if (f6 - y2 > f4) {
                                                this.translationY = f4;
                                            }
                                            f = y2;
                                        }
                                        this.translationX = f3 - x;
                                        if (this.scale != 1.0f) {
                                            this.translationY -= f;
                                        }
                                        this.containerView.invalidate();
                                    }
                                }
                                float f7 = this.translationY;
                                if (f7 < this.minY || f7 > f4) {
                                    f = y2 / 3.0f;
                                    this.translationX = f3 - x;
                                    if (this.scale != 1.0f) {
                                    }
                                    this.containerView.invalidate();
                                }
                                f = y2;
                                this.translationX = f3 - x;
                                if (this.scale != 1.0f) {
                                }
                                this.containerView.invalidate();
                            }
                        } else {
                            this.invalidCoords = false;
                            this.moveStartX = motionEvent.getX();
                            this.moveStartY = motionEvent.getY();
                        }
                    }
                } else if (motionEvent.getActionMasked() == 3 || motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 6) {
                    if (this.zooming) {
                        this.invalidCoords = true;
                        float f8 = this.scale;
                        if (f8 < 1.0f) {
                            updateMinMax(1.0f);
                            animateTo(1.0f, 0.0f, 0.0f, true);
                        } else if (f8 > 3.0f) {
                            float containerViewWidth = (this.pinchCenterX - (getContainerViewWidth() / 2)) - (((this.pinchCenterX - (getContainerViewWidth() / 2)) - this.pinchStartX) * (3.0f / this.pinchStartScale));
                            float containerViewHeight3 = (this.pinchCenterY - (getContainerViewHeight() / 2)) - (((this.pinchCenterY - (getContainerViewHeight() / 2)) - this.pinchStartY) * (3.0f / this.pinchStartScale));
                            updateMinMax(3.0f);
                            float f9 = this.minX;
                            if (containerViewWidth >= f9) {
                                f9 = this.maxX;
                            }
                            containerViewWidth = f9;
                            float f10 = this.minY;
                            if (containerViewHeight3 >= f10) {
                                f10 = this.maxY;
                            }
                            containerViewHeight3 = f10;
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
                        float f11 = this.translationX;
                        float f12 = this.translationY;
                        updateMinMax(this.scale);
                        this.moving = false;
                        this.canDragDown = true;
                        VelocityTracker velocityTracker4 = this.velocityTracker;
                        if (velocityTracker4 != null && this.scale == 1.0f) {
                            velocityTracker4.computeCurrentVelocity(1000);
                        }
                        float f13 = this.translationX;
                        float f14 = this.minX;
                        if (f13 >= f14) {
                            f14 = this.maxX;
                        }
                        f11 = f14;
                        float f15 = this.translationY;
                        float f16 = this.minY;
                        if (f15 >= f16) {
                            f16 = this.maxY;
                        }
                        f12 = f16;
                        animateTo(this.scale, f11, f12, false);
                    }
                }
            }
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0028, code lost:
        if (r2 > r3) goto L6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0017, code lost:
        if (r2 > r3) goto L3;
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

    private void animateTo(float f, float f2, float f3, boolean z) {
        animateTo(f, f2, f3, z, 250);
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
        this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.SecretMediaViewer.9
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SecretMediaViewer.this.imageMoveAnimation = null;
                SecretMediaViewer.this.containerView.invalidate();
            }
        });
        this.imageMoveAnimation.start();
    }

    @Keep
    public void setAnimationValue(float f) {
        this.animationValue = f;
        this.containerView.invalidate();
    }

    @Keep
    public float getAnimationValue() {
        return this.animationValue;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (this.scale != 1.0f) {
            this.scroller.abortAnimation();
            this.scroller.fling(Math.round(this.translationX), Math.round(this.translationY), Math.round(f), Math.round(f2), (int) this.minX, (int) this.maxX, (int) this.minY, (int) this.maxY);
            this.containerView.postInvalidate();
            return false;
        }
        return false;
    }

    @Override // android.view.GestureDetector.OnDoubleTapListener
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        if (this.discardTap) {
            return false;
        }
        toggleActionBar(!this.isActionBarVisible, true);
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x008e, code lost:
        if (r2 > r10) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x007f, code lost:
        if (r0 > r10) goto L17;
     */
    @Override // android.view.GestureDetector.OnDoubleTapListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onDoubleTap(MotionEvent motionEvent) {
        float f = this.scale;
        boolean z = false;
        if (f != 1.0f || (this.translationY == 0.0f && this.translationX == 0.0f)) {
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
        return false;
    }
}
