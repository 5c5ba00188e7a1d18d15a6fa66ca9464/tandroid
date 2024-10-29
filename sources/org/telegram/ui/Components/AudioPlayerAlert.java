package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import androidx.core.graphics.ColorUtils;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSlider;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AudioPlayerCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AudioPlayerAlert;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SeekBarView;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.TopicsFragment;

/* loaded from: classes3.dex */
public class AudioPlayerAlert extends BottomSheet implements NotificationCenter.NotificationCenterDelegate, DownloadController.FileDownloadProgressListener {
    private static final float[] speeds = {0.5f, 1.0f, 1.2f, 1.5f, 1.7f, 2.0f};
    private int TAG;
    private ActionBar actionBar;
    private AnimatorSet actionBarAnimation;
    private View actionBarShadow;
    private ClippingTextViewSwitcher authorTextView;
    private BackupImageView bigAlbumConver;
    private boolean blurredAnimationInProgress;
    private FrameLayout blurredView;
    private View[] buttons;
    private CoverContainer coverContainer;
    private boolean currentAudioFinishedLoading;
    private String currentFile;
    private boolean draggingSeekBar;
    private TextView durationTextView;
    private ImageView emptyImageView;
    private TextView emptySubtitleTextView;
    private TextView emptyTitleTextView;
    private LinearLayout emptyView;
    private final Runnable forwardSeek;
    private boolean inFullSize;
    private long lastBufferedPositionCheck;
    private int lastDuration;
    private MessageObject lastMessageObject;
    private long lastPlaybackClick;
    long lastRewindingTime;
    private int lastTime;
    long lastUpdateRewindingPlayerTime;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private RLottieImageView nextButton;
    private ActionBarMenuItem optionsButton;
    private LaunchActivity parentActivity;
    private ImageView playButton;
    private PlayPauseDrawable playPauseDrawable;
    private ActionBarMenuItem playbackSpeedButton;
    private FrameLayout playerLayout;
    private View playerShadow;
    private ArrayList playlist;
    private RLottieImageView prevButton;
    private LineProgressView progressView;
    private ActionBarMenuItem repeatButton;
    private ActionBarMenuSubItem repeatListItem;
    private ActionBarMenuSubItem repeatSongItem;
    private ActionBarMenuSubItem reverseOrderItem;
    int rewindingForwardPressedCount;
    float rewindingProgress;
    int rewindingState;
    private int scrollOffsetY;
    private boolean scrollToSong;
    private ActionBarMenuItem searchItem;
    private int searchOpenOffset;
    private int searchOpenPosition;
    private boolean searchWas;
    private boolean searching;
    private SpringAnimation seekBarBufferSpring;
    private SeekBarView seekBarView;
    private ActionBarMenuSubItem shuffleListItem;
    private boolean slidingSpeed;
    private HintView speedHintView;
    private SpeedIconDrawable speedIcon;
    private ActionBarMenuSubItem[] speedItems;
    private ActionBarMenuSlider.SpeedSlider speedSlider;
    private SimpleTextView timeTextView;
    private ClippingTextViewSwitcher titleTextView;
    private boolean wasLight;

    /* loaded from: classes3.dex */
    class 13 extends RLottieImageView {
        private final Runnable backSeek;
        long lastTime;
        long lastUpdateTime;
        int pressedCount;
        private final Runnable pressedRunnable;
        long startTime;
        float startX;
        float startY;
        final /* synthetic */ float val$touchSlop;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        13(Context context, float f) {
            super(context);
            this.val$touchSlop = f;
            this.pressedCount = 0;
            this.pressedRunnable = new Runnable() { // from class: org.telegram.ui.Components.AudioPlayerAlert.13.1
                @Override // java.lang.Runnable
                public void run() {
                    13 r0 = 13.this;
                    int i = r0.pressedCount + 1;
                    r0.pressedCount = i;
                    if (i != 1) {
                        if (i == 2) {
                            AndroidUtilities.runOnUIThread(this, 2000L);
                            return;
                        }
                        return;
                    }
                    AudioPlayerAlert audioPlayerAlert = AudioPlayerAlert.this;
                    audioPlayerAlert.rewindingState = -1;
                    audioPlayerAlert.rewindingProgress = MediaController.getInstance().getPlayingMessageObject().audioProgress;
                    13.this.lastTime = System.currentTimeMillis();
                    AndroidUtilities.runOnUIThread(this, 2000L);
                    AndroidUtilities.runOnUIThread(13.this.backSeek);
                }
            };
            this.backSeek = new Runnable() { // from class: org.telegram.ui.Components.AudioPlayerAlert.13.2
                @Override // java.lang.Runnable
                public void run() {
                    long duration = MediaController.getInstance().getDuration();
                    if (duration == 0 || duration == -9223372036854775807L) {
                        13.this.lastTime = System.currentTimeMillis();
                        return;
                    }
                    float f2 = AudioPlayerAlert.this.rewindingProgress;
                    long currentTimeMillis = System.currentTimeMillis();
                    13 r5 = 13.this;
                    long j = currentTimeMillis - r5.lastTime;
                    r5.lastTime = currentTimeMillis;
                    long j2 = currentTimeMillis - r5.lastUpdateTime;
                    int i = r5.pressedCount;
                    float f3 = ((f2 * r0) - ((float) (j * (i == 1 ? 3L : i == 2 ? 6L : 12L)))) / ((float) duration);
                    if (f3 < 0.0f) {
                        f3 = 0.0f;
                    }
                    AudioPlayerAlert.this.rewindingProgress = f3;
                    MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                    if (playingMessageObject != null && playingMessageObject.isMusic()) {
                        AudioPlayerAlert.this.updateProgress(playingMessageObject);
                    }
                    13 r2 = 13.this;
                    AudioPlayerAlert audioPlayerAlert = AudioPlayerAlert.this;
                    if (audioPlayerAlert.rewindingState != -1 || r2.pressedCount <= 0) {
                        return;
                    }
                    if (j2 > 200 || audioPlayerAlert.rewindingProgress == 0.0f) {
                        r2.lastUpdateTime = currentTimeMillis;
                        if (audioPlayerAlert.rewindingProgress == 0.0f) {
                            MediaController.getInstance().seekToProgress(MediaController.getInstance().getPlayingMessageObject(), 0.0f);
                            MediaController.getInstance().pauseByRewind();
                        } else {
                            MediaController.getInstance().seekToProgress(MediaController.getInstance().getPlayingMessageObject(), f3);
                        }
                    }
                    13 r1 = 13.this;
                    if (r1.pressedCount <= 0 || AudioPlayerAlert.this.rewindingProgress <= 0.0f) {
                        return;
                    }
                    AndroidUtilities.runOnUIThread(r1.backSeek, 16L);
                }
            };
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.addAction(16);
        }

        /* JADX WARN: Code restructure failed: missing block: B:11:0x002c, code lost:
        
            if (r4 != 3) goto L37;
         */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (AudioPlayerAlert.this.seekBarView.isDragging() || AudioPlayerAlert.this.rewindingState == 1) {
                return false;
            }
            float rawX = motionEvent.getRawX();
            float rawY = motionEvent.getRawY();
            int action = motionEvent.getAction();
            if (action != 0) {
                if (action != 1) {
                    if (action == 2) {
                        float f = rawX - this.startX;
                        float f2 = rawY - this.startY;
                        float f3 = (f * f) + (f2 * f2);
                        float f4 = this.val$touchSlop;
                        if (f3 > f4 * f4 && AudioPlayerAlert.this.rewindingState == 0) {
                            AndroidUtilities.cancelRunOnUIThread(this.pressedRunnable);
                            setPressed(false);
                        }
                    }
                }
                AndroidUtilities.cancelRunOnUIThread(this.pressedRunnable);
                AndroidUtilities.cancelRunOnUIThread(this.backSeek);
                if (AudioPlayerAlert.this.rewindingState == 0 && motionEvent.getAction() == 1 && System.currentTimeMillis() - this.startTime < 300) {
                    MediaController.getInstance().playPreviousMessage();
                    AudioPlayerAlert.this.prevButton.setProgress(0.0f);
                    AudioPlayerAlert.this.prevButton.playAnimation();
                }
                if (this.pressedCount > 0) {
                    this.lastUpdateTime = 0L;
                    this.backSeek.run();
                    MediaController.getInstance().resumeByRewind();
                }
                AudioPlayerAlert.this.rewindingProgress = -1.0f;
                setPressed(false);
                AudioPlayerAlert.this.rewindingState = 0;
                this.pressedCount = 0;
            } else {
                this.startX = rawX;
                this.startY = rawY;
                this.startTime = System.currentTimeMillis();
                AudioPlayerAlert.this.rewindingState = 0;
                AndroidUtilities.runOnUIThread(this.pressedRunnable, 300L);
                if (Build.VERSION.SDK_INT >= 21 && getBackground() != null) {
                    getBackground().setHotspot(this.startX, this.startY);
                }
                setPressed(true);
            }
            return true;
        }
    }

    /* loaded from: classes3.dex */
    class 14 extends RLottieImageView {
        boolean pressed;
        private final Runnable pressedRunnable;
        float startX;
        float startY;
        final /* synthetic */ float val$touchSlop;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        14(Context context, float f) {
            super(context);
            this.val$touchSlop = f;
            this.pressedRunnable = new Runnable() { // from class: org.telegram.ui.Components.AudioPlayerAlert.14.1
                @Override // java.lang.Runnable
                public void run() {
                    MediaController mediaController;
                    float f2;
                    if (MediaController.getInstance().getPlayingMessageObject() == null) {
                        return;
                    }
                    14 r0 = 14.this;
                    AudioPlayerAlert audioPlayerAlert = AudioPlayerAlert.this;
                    int i = audioPlayerAlert.rewindingForwardPressedCount + 1;
                    audioPlayerAlert.rewindingForwardPressedCount = i;
                    if (i == 1) {
                        r0.pressed = true;
                        audioPlayerAlert.rewindingState = 1;
                        if (MediaController.getInstance().isMessagePaused()) {
                            AudioPlayerAlert.this.startForwardRewindingSeek();
                        } else {
                            AudioPlayerAlert audioPlayerAlert2 = AudioPlayerAlert.this;
                            if (audioPlayerAlert2.rewindingState == 1) {
                                AndroidUtilities.cancelRunOnUIThread(audioPlayerAlert2.forwardSeek);
                                AudioPlayerAlert.this.lastUpdateRewindingPlayerTime = 0L;
                            }
                        }
                        mediaController = MediaController.getInstance();
                        f2 = 4.0f;
                    } else if (i != 2) {
                        MediaController.getInstance().setPlaybackSpeed(true, 13.0f);
                        return;
                    } else {
                        mediaController = MediaController.getInstance();
                        f2 = 7.0f;
                    }
                    mediaController.setPlaybackSpeed(true, f2);
                    AndroidUtilities.runOnUIThread(this, 2000L);
                }
            };
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.addAction(16);
        }

        /* JADX WARN: Code restructure failed: missing block: B:11:0x002b, code lost:
        
            if (r3 != 3) goto L39;
         */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (AudioPlayerAlert.this.seekBarView.isDragging() || AudioPlayerAlert.this.rewindingState == -1) {
                return false;
            }
            float rawX = motionEvent.getRawX();
            float rawY = motionEvent.getRawY();
            int action = motionEvent.getAction();
            if (action != 0) {
                if (action != 1) {
                    if (action == 2) {
                        float f = rawX - this.startX;
                        float f2 = rawY - this.startY;
                        float f3 = (f * f) + (f2 * f2);
                        float f4 = this.val$touchSlop;
                        if (f3 > f4 * f4 && !this.pressed) {
                            AndroidUtilities.cancelRunOnUIThread(this.pressedRunnable);
                            setPressed(false);
                        }
                    }
                }
                if (!this.pressed && motionEvent.getAction() == 1 && isPressed()) {
                    MediaController.getInstance().playNextMessage();
                    AudioPlayerAlert.this.nextButton.setProgress(0.0f);
                    AudioPlayerAlert.this.nextButton.playAnimation();
                }
                AndroidUtilities.cancelRunOnUIThread(this.pressedRunnable);
                if (AudioPlayerAlert.this.rewindingForwardPressedCount > 0) {
                    MediaController.getInstance().setPlaybackSpeed(true, 1.0f);
                    if (MediaController.getInstance().isMessagePaused()) {
                        AudioPlayerAlert audioPlayerAlert = AudioPlayerAlert.this;
                        audioPlayerAlert.lastUpdateRewindingPlayerTime = 0L;
                        audioPlayerAlert.forwardSeek.run();
                    }
                }
                AudioPlayerAlert.this.rewindingState = 0;
                setPressed(false);
                AudioPlayerAlert audioPlayerAlert2 = AudioPlayerAlert.this;
                audioPlayerAlert2.rewindingForwardPressedCount = 0;
                audioPlayerAlert2.rewindingProgress = -1.0f;
            } else {
                this.pressed = false;
                this.startX = rawX;
                this.startY = rawY;
                AndroidUtilities.runOnUIThread(this.pressedRunnable, 300L);
                if (Build.VERSION.SDK_INT >= 21 && getBackground() != null) {
                    getBackground().setHotspot(this.startX, this.startY);
                }
                setPressed(true);
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 9 extends ClippingTextViewSwitcher {
        final /* synthetic */ Context val$context;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        9(Context context, Context context2) {
            super(context);
            this.val$context = context2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createTextView$0(TextView textView, View view) {
            if (MessagesController.getInstance(((BottomSheet) AudioPlayerAlert.this).currentAccount).getTotalDialogsCount() <= 10 || TextUtils.isEmpty(textView.getText().toString())) {
                return;
            }
            String charSequence = textView.getText().toString();
            if (AudioPlayerAlert.this.parentActivity.getActionBarLayout().getLastFragment() instanceof DialogsActivity) {
                DialogsActivity dialogsActivity = (DialogsActivity) AudioPlayerAlert.this.parentActivity.getActionBarLayout().getLastFragment();
                if (!dialogsActivity.onlyDialogsAdapter()) {
                    dialogsActivity.setShowSearch(charSequence, 3);
                    AudioPlayerAlert.this.dismiss();
                }
            }
            DialogsActivity dialogsActivity2 = new DialogsActivity(null);
            dialogsActivity2.setSearchString(charSequence);
            dialogsActivity2.setInitialSearchType(3);
            AudioPlayerAlert.this.parentActivity.presentFragment(dialogsActivity2, false, false);
            AudioPlayerAlert.this.dismiss();
        }

        @Override // org.telegram.ui.Components.AudioPlayerAlert.ClippingTextViewSwitcher
        protected TextView createTextView() {
            final TextView textView = new TextView(this.val$context);
            textView.setTextColor(AudioPlayerAlert.this.getThemedColor(Theme.key_player_time));
            textView.setTextSize(1, 13.0f);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setSingleLine(true);
            textView.setPadding(AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(1.0f));
            textView.setBackground(Theme.createRadSelectorDrawable(AudioPlayerAlert.this.getThemedColor(Theme.key_listSelector), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f)));
            textView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert$9$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AudioPlayerAlert.9.this.lambda$createTextView$0(textView, view);
                }
            });
            return textView;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class ClippingTextViewSwitcher extends FrameLayout {
        private int activeIndex;
        private AnimatorSet animatorSet;
        private final float[] clipProgress;
        private final Paint erasePaint;
        private final Matrix gradientMatrix;
        private final Paint gradientPaint;
        private LinearGradient gradientShader;
        private final int gradientSize;
        private final RectF rectF;
        private int stableOffest;
        private final TextView[] textViews;

        public ClippingTextViewSwitcher(Context context) {
            super(context);
            this.textViews = new TextView[2];
            this.clipProgress = new float[]{0.0f, 0.75f};
            this.gradientSize = AndroidUtilities.dp(24.0f);
            this.stableOffest = -1;
            this.rectF = new RectF();
            for (int i = 0; i < 2; i++) {
                this.textViews[i] = createTextView();
                if (i == 1) {
                    this.textViews[i].setAlpha(0.0f);
                    this.textViews[i].setVisibility(8);
                }
                addView(this.textViews[i], LayoutHelper.createFrame(-2, -1.0f));
            }
            this.gradientMatrix = new Matrix();
            Paint paint = new Paint(1);
            this.gradientPaint = paint;
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            Paint paint2 = new Paint(1);
            this.erasePaint = paint2;
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setText$0(int i, ValueAnimator valueAnimator) {
            this.clipProgress[i] = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setText$1(int i, ValueAnimator valueAnimator) {
            this.clipProgress[i] = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        protected abstract TextView createTextView();

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            boolean z;
            TextView[] textViewArr = this.textViews;
            boolean z2 = true;
            int i = view == textViewArr[0] ? 0 : 1;
            if (this.stableOffest <= 0 || textViewArr[this.activeIndex].getAlpha() == 1.0f || this.textViews[this.activeIndex].getLayout() == null) {
                z = false;
            } else {
                float primaryHorizontal = this.textViews[this.activeIndex].getLayout().getPrimaryHorizontal(0);
                float primaryHorizontal2 = this.textViews[this.activeIndex].getLayout().getPrimaryHorizontal(this.stableOffest);
                if (primaryHorizontal == primaryHorizontal2) {
                    z2 = false;
                } else if (primaryHorizontal2 > primaryHorizontal) {
                    this.rectF.set(primaryHorizontal, 0.0f, primaryHorizontal2, getMeasuredHeight());
                } else {
                    this.rectF.set(primaryHorizontal2, 0.0f, primaryHorizontal, getMeasuredHeight());
                }
                if (z2 && i == this.activeIndex) {
                    canvas.save();
                    canvas.clipRect(this.rectF);
                    this.textViews[0].draw(canvas);
                    canvas.restore();
                }
                z = z2;
            }
            if (this.clipProgress[i] <= 0.0f && !z) {
                return super.drawChild(canvas, view, j);
            }
            float width = view.getWidth();
            float height = view.getHeight();
            int saveLayer = canvas.saveLayer(0.0f, 0.0f, width, height, null, 31);
            boolean drawChild = super.drawChild(canvas, view, j);
            float f = width * (1.0f - this.clipProgress[i]);
            float f2 = f + this.gradientSize;
            this.gradientMatrix.setTranslate(f, 0.0f);
            this.gradientShader.setLocalMatrix(this.gradientMatrix);
            canvas.drawRect(f, 0.0f, f2, height, this.gradientPaint);
            if (width > f2) {
                canvas.drawRect(f2, 0.0f, width, height, this.erasePaint);
            }
            if (z) {
                canvas.drawRect(this.rectF, this.erasePaint);
            }
            canvas.restoreToCount(saveLayer);
            return drawChild;
        }

        public TextView getNextTextView() {
            return this.textViews[this.activeIndex == 0 ? (char) 1 : (char) 0];
        }

        public TextView getTextView() {
            return this.textViews[this.activeIndex];
        }

        @Override // android.view.View
        protected void onSizeChanged(int i, int i2, int i3, int i4) {
            super.onSizeChanged(i, i2, i3, i4);
            LinearGradient linearGradient = new LinearGradient(this.gradientSize, 0.0f, 0.0f, 0.0f, 0, -16777216, Shader.TileMode.CLAMP);
            this.gradientShader = linearGradient;
            this.gradientPaint.setShader(linearGradient);
        }

        public void setText(CharSequence charSequence) {
            setText(charSequence, true);
        }

        public void setText(CharSequence charSequence, boolean z) {
            CharSequence text = this.textViews[this.activeIndex].getText();
            if (TextUtils.isEmpty(text) || !z) {
                this.textViews[this.activeIndex].setText(charSequence);
                return;
            }
            if (TextUtils.equals(charSequence, text)) {
                return;
            }
            this.stableOffest = 0;
            int min = Math.min(charSequence.length(), text.length());
            for (int i = 0; i < min && charSequence.charAt(i) == text.charAt(i); i++) {
                this.stableOffest++;
            }
            if (this.stableOffest <= 3) {
                this.stableOffest = -1;
            }
            final int i2 = this.activeIndex;
            final int i3 = i2 == 0 ? 1 : 0;
            this.activeIndex = i3;
            AnimatorSet animatorSet = this.animatorSet;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animatorSet = animatorSet2;
            animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.AudioPlayerAlert.ClippingTextViewSwitcher.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ClippingTextViewSwitcher.this.textViews[i2].setVisibility(8);
                }
            });
            this.textViews[i3].setText(charSequence);
            this.textViews[i3].bringToFront();
            this.textViews[i3].setVisibility(0);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.clipProgress[i2], 0.75f);
            ofFloat.setDuration(200L);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert$ClippingTextViewSwitcher$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    AudioPlayerAlert.ClippingTextViewSwitcher.this.lambda$setText$0(i2, valueAnimator);
                }
            });
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.clipProgress[i3], 0.0f);
            ofFloat2.setStartDelay(100L);
            ofFloat2.setDuration(200L);
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert$ClippingTextViewSwitcher$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    AudioPlayerAlert.ClippingTextViewSwitcher.this.lambda$setText$1(i3, valueAnimator);
                }
            });
            TextView textView = this.textViews[i2];
            Property property = View.ALPHA;
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(textView, (Property<TextView, Float>) property, 0.0f);
            ofFloat3.setStartDelay(75L);
            ofFloat3.setDuration(150L);
            ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this.textViews[i3], (Property<TextView, Float>) property, 1.0f);
            ofFloat4.setStartDelay(75L);
            ofFloat4.setDuration(150L);
            this.animatorSet.playTogether(ofFloat, ofFloat2, ofFloat3, ofFloat4);
            this.animatorSet.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static abstract class CoverContainer extends FrameLayout {
        private int activeIndex;
        private AnimatorSet animatorSet;
        private final BackupImageView[] imageViews;

        public CoverContainer(Context context) {
            super(context);
            this.imageViews = new BackupImageView[2];
            for (final int i = 0; i < 2; i++) {
                this.imageViews[i] = new BackupImageView(context);
                this.imageViews[i].getImageReceiver().setDelegate(new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.Components.AudioPlayerAlert$CoverContainer$$ExternalSyntheticLambda0
                    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                    public final void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
                        AudioPlayerAlert.CoverContainer.this.lambda$new$0(i, imageReceiver, z, z2, z3);
                    }

                    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                    public /* synthetic */ void didSetImageBitmap(int i2, String str, Drawable drawable) {
                        ImageReceiver.ImageReceiverDelegate.-CC.$default$didSetImageBitmap(this, i2, str, drawable);
                    }

                    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                    public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
                        ImageReceiver.ImageReceiverDelegate.-CC.$default$onAnimationReady(this, imageReceiver);
                    }
                });
                this.imageViews[i].setRoundRadius(AndroidUtilities.dp(4.0f));
                if (i == 1) {
                    this.imageViews[i].setVisibility(8);
                }
                addView(this.imageViews[i], LayoutHelper.createFrame(-1, -1.0f));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(int i, ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
            if (i == this.activeIndex) {
                onImageUpdated(imageReceiver);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$switchImageViews$1(BackupImageView backupImageView, boolean z, ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            backupImageView.setScaleX(floatValue);
            backupImageView.setScaleY(floatValue);
            if (z) {
                return;
            }
            backupImageView.setAlpha(valueAnimator.getAnimatedFraction());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$switchImageViews$2(BackupImageView backupImageView, BackupImageView backupImageView2, ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            backupImageView.setScaleX(floatValue);
            backupImageView.setScaleY(floatValue);
            float animatedFraction = valueAnimator.getAnimatedFraction();
            if (animatedFraction <= 0.25f || backupImageView2.getImageReceiver().hasBitmapImage()) {
                return;
            }
            backupImageView.setAlpha(1.0f - ((animatedFraction - 0.25f) * 1.3333334f));
        }

        public final ImageReceiver getImageReceiver() {
            return getImageView().getImageReceiver();
        }

        public final BackupImageView getImageView() {
            return this.imageViews[this.activeIndex];
        }

        public final BackupImageView getNextImageView() {
            return this.imageViews[this.activeIndex == 0 ? (char) 1 : (char) 0];
        }

        protected abstract void onImageUpdated(ImageReceiver imageReceiver);

        public final void switchImageViews() {
            AnimatorSet animatorSet = this.animatorSet;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            this.animatorSet = new AnimatorSet();
            int i = this.activeIndex == 0 ? 1 : 0;
            this.activeIndex = i;
            BackupImageView[] backupImageViewArr = this.imageViews;
            final BackupImageView backupImageView = backupImageViewArr[i ^ 1];
            final BackupImageView backupImageView2 = backupImageViewArr[i];
            final boolean hasBitmapImage = backupImageView.getImageReceiver().hasBitmapImage();
            backupImageView2.setAlpha(hasBitmapImage ? 1.0f : 0.0f);
            backupImageView2.setScaleX(0.8f);
            backupImageView2.setScaleY(0.8f);
            backupImageView2.setVisibility(0);
            if (hasBitmapImage) {
                backupImageView.bringToFront();
            } else {
                backupImageView.setVisibility(8);
                backupImageView.setImageDrawable(null);
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.8f, 1.0f);
            ofFloat.setDuration(125L);
            ofFloat.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert$CoverContainer$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    AudioPlayerAlert.CoverContainer.lambda$switchImageViews$1(BackupImageView.this, hasBitmapImage, valueAnimator);
                }
            });
            if (hasBitmapImage) {
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(backupImageView.getScaleX(), 0.8f);
                ofFloat2.setDuration(125L);
                ofFloat2.setInterpolator(CubicBezierInterpolator.EASE_IN);
                ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert$CoverContainer$$ExternalSyntheticLambda2
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        AudioPlayerAlert.CoverContainer.lambda$switchImageViews$2(BackupImageView.this, backupImageView2, valueAnimator);
                    }
                });
                ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.AudioPlayerAlert.CoverContainer.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        backupImageView.setVisibility(8);
                        backupImageView.setImageDrawable(null);
                        backupImageView.setAlpha(1.0f);
                    }
                });
                this.animatorSet.playSequentially(ofFloat2, ofFloat);
            } else {
                this.animatorSet.play(ofFloat);
            }
            this.animatorSet.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context context;
        private ArrayList searchResult = new ArrayList();
        private Runnable searchRunnable;

        public ListAdapter(Context context) {
            this.context = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processSearch$1(String str, ArrayList arrayList) {
            boolean z;
            String str2;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                updateSearchResults(new ArrayList(), str);
                return;
            }
            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
            if (lowerCase.equals(translitString) || translitString.length() == 0) {
                translitString = null;
            }
            int i = (translitString != null ? 1 : 0) + 1;
            String[] strArr = new String[i];
            strArr[0] = lowerCase;
            if (translitString != null) {
                strArr[1] = translitString;
            }
            ArrayList arrayList2 = new ArrayList();
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                MessageObject messageObject = (MessageObject) arrayList.get(i2);
                for (int i3 = 0; i3 < i; i3++) {
                    String str3 = strArr[i3];
                    String documentName = messageObject.getDocumentName();
                    if (documentName != null && documentName.length() != 0) {
                        if (!documentName.toLowerCase().contains(str3)) {
                            TLRPC.Document document = messageObject.type == 0 ? messageObject.messageOwner.media.webpage.document : messageObject.messageOwner.media.document;
                            int i4 = 0;
                            while (true) {
                                if (i4 >= document.attributes.size()) {
                                    z = false;
                                    break;
                                }
                                TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i4);
                                if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                                    String str4 = documentAttribute.performer;
                                    z = str4 != null ? str4.toLowerCase().contains(str3) : false;
                                    if (!z && (str2 = documentAttribute.title) != null) {
                                        z = str2.toLowerCase().contains(str3);
                                    }
                                } else {
                                    i4++;
                                }
                            }
                            if (z) {
                            }
                        }
                        arrayList2.add(messageObject);
                        break;
                    }
                }
            }
            updateSearchResults(arrayList2, str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processSearch$2(final String str) {
            final ArrayList arrayList = new ArrayList(AudioPlayerAlert.this.playlist);
            Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.AudioPlayerAlert$ListAdapter$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    AudioPlayerAlert.ListAdapter.this.lambda$processSearch$1(str, arrayList);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$search$0(String str) {
            this.searchRunnable = null;
            processSearch(str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateSearchResults$3(ArrayList arrayList, String str) {
            if (AudioPlayerAlert.this.searching) {
                AudioPlayerAlert.this.searchWas = true;
                this.searchResult = arrayList;
                notifyDataSetChanged();
                AudioPlayerAlert.this.layoutManager.scrollToPosition(0);
                AudioPlayerAlert.this.emptySubtitleTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("NoAudioFoundPlayerInfo", R.string.NoAudioFoundPlayerInfo, str)));
            }
        }

        private void processSearch(final String str) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AudioPlayerAlert$ListAdapter$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AudioPlayerAlert.ListAdapter.this.lambda$processSearch$2(str);
                }
            });
        }

        private void updateSearchResults(final ArrayList arrayList, final String str) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.AudioPlayerAlert$ListAdapter$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    AudioPlayerAlert.ListAdapter.this.lambda$updateSearchResults$3(arrayList, str);
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (AudioPlayerAlert.this.searchWas) {
                return this.searchResult.size();
            }
            if (AudioPlayerAlert.this.playlist.size() > 1) {
                return AudioPlayerAlert.this.playlist.size();
            }
            return 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (AudioPlayerAlert.this.playlist.size() > 1) {
                AudioPlayerAlert.this.playerLayout.setBackgroundColor(AudioPlayerAlert.this.getThemedColor(Theme.key_player_background));
                AudioPlayerAlert.this.playerShadow.setVisibility(0);
                AudioPlayerAlert.this.listView.setPadding(0, AudioPlayerAlert.this.listView.getPaddingTop(), 0, AndroidUtilities.dp(179.0f));
            } else {
                AudioPlayerAlert.this.playerLayout.setBackground(null);
                AudioPlayerAlert.this.playerShadow.setVisibility(4);
                AudioPlayerAlert.this.listView.setPadding(0, AudioPlayerAlert.this.listView.getPaddingTop(), 0, 0);
            }
            AudioPlayerAlert.this.updateEmptyView();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            Object obj;
            ArrayList arrayList;
            AudioPlayerCell audioPlayerCell = (AudioPlayerCell) viewHolder.itemView;
            if (AudioPlayerAlert.this.searchWas) {
                arrayList = this.searchResult;
            } else {
                if (!SharedConfig.playOrderReversed) {
                    obj = AudioPlayerAlert.this.playlist.get((AudioPlayerAlert.this.playlist.size() - i) - 1);
                    audioPlayerCell.setMessageObject((MessageObject) obj);
                }
                arrayList = AudioPlayerAlert.this.playlist;
            }
            obj = arrayList.get(i);
            audioPlayerCell.setMessageObject((MessageObject) obj);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            Context context = this.context;
            boolean currentPlaylistIsGlobalSearch = MediaController.getInstance().currentPlaylistIsGlobalSearch();
            return new RecyclerListView.Holder(new AudioPlayerCell(context, currentPlaylistIsGlobalSearch ? 1 : 0, ((BottomSheet) AudioPlayerAlert.this).resourcesProvider));
        }

        public void search(final String str) {
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            if (str == null) {
                this.searchResult.clear();
                notifyDataSetChanged();
            } else {
                DispatchQueue dispatchQueue = Utilities.searchQueue;
                Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.AudioPlayerAlert$ListAdapter$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        AudioPlayerAlert.ListAdapter.this.lambda$search$0(str);
                    }
                };
                this.searchRunnable = runnable;
                dispatchQueue.postRunnable(runnable, 300L);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0154, code lost:
    
        if (r1 != null) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0195, code lost:
    
        r2 = r38.actionBar;
        r1 = org.telegram.messenger.ContactsController.formatName(r1.first_name, r1.last_name);
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0193, code lost:
    
        if (r1 != null) goto L29;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public AudioPlayerAlert(final Context context, final Theme.ResourcesProvider resourcesProvider) {
        super(context, true, resourcesProvider);
        int i;
        ActionBar actionBar;
        String str;
        TLRPC.User user;
        this.speedItems = new ActionBarMenuSubItem[6];
        this.buttons = new View[5];
        this.scrollToSong = true;
        this.searchOpenPosition = -1;
        this.scrollOffsetY = ConnectionsManager.DEFAULT_DATACENTER_ID;
        this.rewindingProgress = -1.0f;
        this.forwardSeek = new Runnable() { // from class: org.telegram.ui.Components.AudioPlayerAlert.1
            @Override // java.lang.Runnable
            public void run() {
                long duration = MediaController.getInstance().getDuration();
                if (duration == 0 || duration == -9223372036854775807L) {
                    AudioPlayerAlert.this.lastRewindingTime = System.currentTimeMillis();
                    return;
                }
                float f = AudioPlayerAlert.this.rewindingProgress;
                long currentTimeMillis = System.currentTimeMillis();
                AudioPlayerAlert audioPlayerAlert = AudioPlayerAlert.this;
                long j = currentTimeMillis - audioPlayerAlert.lastRewindingTime;
                audioPlayerAlert.lastRewindingTime = currentTimeMillis;
                long j2 = currentTimeMillis - audioPlayerAlert.lastUpdateRewindingPlayerTime;
                int i2 = audioPlayerAlert.rewindingForwardPressedCount;
                float f2 = ((f * r0) + ((float) (((i2 == 1 ? 3L : i2 == 2 ? 6L : 12L) * j) - j))) / ((float) duration);
                if (f2 < 0.0f) {
                    f2 = 0.0f;
                }
                audioPlayerAlert.rewindingProgress = f2;
                MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                if (playingMessageObject != null && playingMessageObject.isMusic()) {
                    if (!MediaController.getInstance().isMessagePaused()) {
                        MediaController.getInstance().getPlayingMessageObject().audioProgress = AudioPlayerAlert.this.rewindingProgress;
                    }
                    AudioPlayerAlert.this.updateProgress(playingMessageObject);
                }
                AudioPlayerAlert audioPlayerAlert2 = AudioPlayerAlert.this;
                if (audioPlayerAlert2.rewindingState == 1 && audioPlayerAlert2.rewindingForwardPressedCount > 0 && MediaController.getInstance().isMessagePaused()) {
                    if (j2 > 200 || AudioPlayerAlert.this.rewindingProgress == 0.0f) {
                        AudioPlayerAlert.this.lastUpdateRewindingPlayerTime = currentTimeMillis;
                        MediaController.getInstance().seekToProgress(MediaController.getInstance().getPlayingMessageObject(), f2);
                    }
                    AudioPlayerAlert audioPlayerAlert3 = AudioPlayerAlert.this;
                    if (audioPlayerAlert3.rewindingForwardPressedCount <= 0 || audioPlayerAlert3.rewindingProgress <= 0.0f) {
                        return;
                    }
                    AndroidUtilities.runOnUIThread(audioPlayerAlert3.forwardSeek, 16L);
                }
            }
        };
        fixNavigationBar();
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        this.currentAccount = playingMessageObject != null ? playingMessageObject.currentAccount : UserConfig.selectedAccount;
        this.parentActivity = (LaunchActivity) context;
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStart);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoadProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.musicDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.moreMusicDidLoad);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.messagePlayingSpeedChanged);
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.Components.AudioPlayerAlert.2
            private int lastMeasturedHeight;
            private int lastMeasturedWidth;
            private RectF rect = new RectF();
            private boolean ignoreLayout = false;

            @Override // android.view.ViewGroup, android.view.View
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: org.telegram.ui.Components.AudioPlayerAlert.2.1
                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ boolean allowLayoutChanges() {
                        return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ boolean bottomOffsetAnimated() {
                        return Bulletin.Delegate.-CC.$default$bottomOffsetAnimated(this);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ boolean clipWithGradient(int i2) {
                        return Bulletin.Delegate.-CC.$default$clipWithGradient(this, i2);
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public int getBottomOffset(int i2) {
                        return AudioPlayerAlert.this.playerLayout.getHeight();
                    }

                    @Override // org.telegram.ui.Components.Bulletin.Delegate
                    public /* synthetic */ int getTopOffset(int i2) {
                        return Bulletin.Delegate.-CC.$default$getTopOffset(this, i2);
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
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                Bulletin.removeDelegate(this);
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                float f;
                if (AudioPlayerAlert.this.playlist.size() <= 1) {
                    ((BottomSheet) AudioPlayerAlert.this).shadowDrawable.setBounds(0, (getMeasuredHeight() - AudioPlayerAlert.this.playerLayout.getMeasuredHeight()) - ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop, getMeasuredWidth(), getMeasuredHeight());
                    ((BottomSheet) AudioPlayerAlert.this).shadowDrawable.draw(canvas);
                    return;
                }
                int dp = AndroidUtilities.dp(13.0f);
                int i2 = (AudioPlayerAlert.this.scrollOffsetY - ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop) - dp;
                if (((BottomSheet) AudioPlayerAlert.this).currentSheetAnimationType == 1) {
                    i2 = (int) (i2 + AudioPlayerAlert.this.listView.getTranslationY());
                }
                int dp2 = AndroidUtilities.dp(20.0f) + i2;
                int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(15.0f) + ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop;
                if (((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop + i2 < ActionBar.getCurrentActionBarHeight()) {
                    float dp3 = dp + AndroidUtilities.dp(4.0f);
                    float min = Math.min(1.0f, ((ActionBar.getCurrentActionBarHeight() - i2) - ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop) / dp3);
                    int currentActionBarHeight = (int) ((ActionBar.getCurrentActionBarHeight() - dp3) * min);
                    i2 -= currentActionBarHeight;
                    dp2 -= currentActionBarHeight;
                    measuredHeight += currentActionBarHeight;
                    f = 1.0f - min;
                } else {
                    f = 1.0f;
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    int i3 = AndroidUtilities.statusBarHeight;
                    i2 += i3;
                    dp2 += i3;
                }
                ((BottomSheet) AudioPlayerAlert.this).shadowDrawable.setBounds(0, i2, getMeasuredWidth(), measuredHeight);
                ((BottomSheet) AudioPlayerAlert.this).shadowDrawable.draw(canvas);
                if (f != 1.0f) {
                    Theme.dialogs_onlineCirclePaint.setColor(AudioPlayerAlert.this.getThemedColor(Theme.key_dialogBackground));
                    this.rect.set(((BottomSheet) AudioPlayerAlert.this).backgroundPaddingLeft, ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop + i2, getMeasuredWidth() - ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingLeft, ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop + i2 + AndroidUtilities.dp(24.0f));
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(12.0f) * f, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
                }
                if (f != 0.0f) {
                    int dp4 = AndroidUtilities.dp(36.0f);
                    this.rect.set((getMeasuredWidth() - dp4) / 2, dp2, (getMeasuredWidth() + dp4) / 2, dp2 + AndroidUtilities.dp(4.0f));
                    int themedColor = AudioPlayerAlert.this.getThemedColor(Theme.key_sheet_scrollUp);
                    int alpha = Color.alpha(themedColor);
                    Theme.dialogs_onlineCirclePaint.setColor(themedColor);
                    Theme.dialogs_onlineCirclePaint.setAlpha((int) (alpha * 1.0f * f));
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                }
            }

            @Override // android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0 || AudioPlayerAlert.this.scrollOffsetY == 0 || AudioPlayerAlert.this.actionBar.getAlpha() != 0.0f || (AudioPlayerAlert.this.listAdapter.getItemCount() <= 0 ? motionEvent.getY() >= getMeasuredHeight() - AndroidUtilities.dp(191.0f) : motionEvent.getY() >= AudioPlayerAlert.this.scrollOffsetY + AndroidUtilities.dp(12.0f))) {
                    return super.onInterceptTouchEvent(motionEvent);
                }
                AudioPlayerAlert.this.dismiss();
                return true;
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i2, int i3, int i4, int i5) {
                super.onLayout(z, i2, i3, i4, i5);
                AudioPlayerAlert.this.updateLayout();
                AudioPlayerAlert.this.updateEmptyViewPosition();
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i2, int i3) {
                int dp;
                int size = View.MeasureSpec.getSize(i3);
                int size2 = View.MeasureSpec.getSize(i2);
                if (size != this.lastMeasturedHeight || size2 != this.lastMeasturedWidth) {
                    if (AudioPlayerAlert.this.blurredView.getTag() != null) {
                        AudioPlayerAlert.this.showAlbumCover(false, false);
                    }
                    this.lastMeasturedWidth = size2;
                    this.lastMeasturedHeight = size;
                }
                this.ignoreLayout = true;
                AudioPlayerAlert.this.playerLayout.setVisibility((AudioPlayerAlert.this.searchWas || ((BottomSheet) AudioPlayerAlert.this).keyboardVisible) ? 4 : 0);
                AudioPlayerAlert.this.playerShadow.setVisibility(AudioPlayerAlert.this.playerLayout.getVisibility());
                int paddingTop = size - getPaddingTop();
                ((FrameLayout.LayoutParams) AudioPlayerAlert.this.listView.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight() + AndroidUtilities.statusBarHeight;
                ((FrameLayout.LayoutParams) AudioPlayerAlert.this.actionBarShadow.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight() + AndroidUtilities.statusBarHeight;
                ((FrameLayout.LayoutParams) AudioPlayerAlert.this.blurredView.getLayoutParams()).topMargin = -getPaddingTop();
                int dp2 = AndroidUtilities.dp(179.0f);
                if (AudioPlayerAlert.this.playlist.size() > 1) {
                    dp2 += ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop + (AudioPlayerAlert.this.playlist.size() * AndroidUtilities.dp(56.0f));
                }
                if (AudioPlayerAlert.this.searching || ((BottomSheet) AudioPlayerAlert.this).keyboardVisible) {
                    dp = AndroidUtilities.dp(8.0f);
                } else {
                    if (dp2 >= paddingTop) {
                        dp2 = (int) ((paddingTop / 5) * 3.5f);
                    }
                    dp = (paddingTop - dp2) + AndroidUtilities.dp(8.0f);
                    if (dp > paddingTop - AndroidUtilities.dp(329.0f)) {
                        dp = paddingTop - AndroidUtilities.dp(329.0f);
                    }
                    if (dp < 0) {
                        dp = 0;
                    }
                }
                if (AudioPlayerAlert.this.listView.getPaddingTop() != dp) {
                    AudioPlayerAlert.this.listView.setPadding(0, dp, 0, (AudioPlayerAlert.this.searching && ((BottomSheet) AudioPlayerAlert.this).keyboardVisible) ? 0 : AudioPlayerAlert.this.listView.getPaddingBottom());
                }
                this.ignoreLayout = false;
                super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
                AudioPlayerAlert.this.inFullSize = getMeasuredHeight() >= size;
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return !AudioPlayerAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        this.containerView = frameLayout;
        frameLayout.setWillNotDraw(false);
        ViewGroup viewGroup = this.containerView;
        int i2 = this.backgroundPaddingLeft;
        viewGroup.setPadding(i2, 0, i2, 0);
        ActionBar actionBar2 = new ActionBar(context, resourcesProvider) { // from class: org.telegram.ui.Components.AudioPlayerAlert.3
            @Override // android.view.View
            public void setAlpha(float f) {
                super.setAlpha(f);
                ((BottomSheet) AudioPlayerAlert.this).containerView.invalidate();
            }
        };
        this.actionBar = actionBar2;
        actionBar2.setBackgroundColor(getThemedColor(Theme.key_dialogBackground));
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        ActionBar actionBar3 = this.actionBar;
        int i3 = Theme.key_player_actionBarTitle;
        actionBar3.setItemsColor(getThemedColor(i3), false);
        this.actionBar.setItemsBackgroundColor(getThemedColor(Theme.key_player_actionBarSelector), false);
        this.actionBar.setTitleColor(getThemedColor(i3));
        this.actionBar.setTitle(LocaleController.getString(R.string.AttachMusic));
        this.actionBar.setSubtitleColor(getThemedColor(Theme.key_player_actionBarSubtitle));
        this.actionBar.setOccupyStatusBar(true);
        this.actionBar.setAlpha(0.0f);
        if (playingMessageObject != null && !MediaController.getInstance().currentPlaylistIsGlobalSearch()) {
            long dialogId = playingMessageObject.getDialogId();
            if (DialogObject.isEncryptedDialog(dialogId)) {
                TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(dialogId)));
                if (encryptedChat != null) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(encryptedChat.user_id));
                }
            } else if (dialogId == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                this.actionBar.setTitle(LocaleController.getString(playingMessageObject.getSavedDialogId() == UserObject.ANONYMOUS ? R.string.AnonymousForward : R.string.SavedMessages));
            } else if (DialogObject.isUserDialog(dialogId)) {
                user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(dialogId));
            } else {
                TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-dialogId));
                if (chat != null) {
                    actionBar = this.actionBar;
                    str = chat.title;
                }
            }
            actionBar.setTitle(str);
        }
        ActionBarMenuItem actionBarMenuItemSearchListener = this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert.4
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchCollapse() {
                if (AudioPlayerAlert.this.searching) {
                    AudioPlayerAlert.this.searchWas = false;
                    AudioPlayerAlert.this.searching = false;
                    AudioPlayerAlert.this.setAllowNestedScroll(true);
                    AudioPlayerAlert.this.listAdapter.search(null);
                }
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchExpand() {
                AudioPlayerAlert audioPlayerAlert = AudioPlayerAlert.this;
                audioPlayerAlert.searchOpenPosition = audioPlayerAlert.layoutManager.findLastVisibleItemPosition();
                View findViewByPosition = AudioPlayerAlert.this.layoutManager.findViewByPosition(AudioPlayerAlert.this.searchOpenPosition);
                AudioPlayerAlert.this.searchOpenOffset = findViewByPosition == null ? 0 : findViewByPosition.getTop();
                AudioPlayerAlert.this.searching = true;
                AudioPlayerAlert.this.setAllowNestedScroll(false);
                AudioPlayerAlert.this.listAdapter.notifyDataSetChanged();
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onTextChanged(EditText editText) {
                if (editText.length() > 0) {
                    AudioPlayerAlert.this.listAdapter.search(editText.getText().toString());
                } else {
                    AudioPlayerAlert.this.searchWas = false;
                    AudioPlayerAlert.this.listAdapter.search(null);
                }
            }
        });
        this.searchItem = actionBarMenuItemSearchListener;
        int i4 = R.string.Search;
        actionBarMenuItemSearchListener.setContentDescription(LocaleController.getString(i4));
        EditTextBoldCursor searchField = this.searchItem.getSearchField();
        searchField.setHint(LocaleController.getString(i4));
        searchField.setTextColor(getThemedColor(i3));
        int i5 = Theme.key_player_time;
        searchField.setHintTextColor(getThemedColor(i5));
        searchField.setCursorColor(getThemedColor(i3));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.Components.AudioPlayerAlert.5
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i6) {
                if (i6 == -1) {
                    AudioPlayerAlert.this.dismiss();
                } else {
                    AudioPlayerAlert.this.onSubItemClick(i6);
                }
            }
        });
        View view = new View(context);
        this.actionBarShadow = view;
        view.setAlpha(0.0f);
        this.actionBarShadow.setBackgroundResource(R.drawable.header_shadow);
        View view2 = new View(context);
        this.playerShadow = view2;
        view2.setBackgroundColor(getThemedColor(Theme.key_dialogShadowLine));
        this.playerLayout = new FrameLayout(context) { // from class: org.telegram.ui.Components.AudioPlayerAlert.6
            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i6, int i7, int i8, int i9) {
                super.onLayout(z, i6, i7, i8, i9);
                if (AudioPlayerAlert.this.playbackSpeedButton == null || AudioPlayerAlert.this.durationTextView == null) {
                    return;
                }
                int left = (AudioPlayerAlert.this.durationTextView.getLeft() - AndroidUtilities.dp(4.0f)) - AudioPlayerAlert.this.playbackSpeedButton.getMeasuredWidth();
                AudioPlayerAlert.this.playbackSpeedButton.layout(left, AudioPlayerAlert.this.playbackSpeedButton.getTop(), AudioPlayerAlert.this.playbackSpeedButton.getMeasuredWidth() + left, AudioPlayerAlert.this.playbackSpeedButton.getBottom());
            }
        };
        CoverContainer coverContainer = new CoverContainer(context) { // from class: org.telegram.ui.Components.AudioPlayerAlert.7
            private long pressTime;

            @Override // org.telegram.ui.Components.AudioPlayerAlert.CoverContainer
            protected void onImageUpdated(ImageReceiver imageReceiver) {
                if (AudioPlayerAlert.this.blurredView.getTag() != null) {
                    AudioPlayerAlert.this.bigAlbumConver.setImageBitmap(imageReceiver.getBitmap());
                }
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    if (getImageReceiver().hasBitmapImage()) {
                        AudioPlayerAlert.this.showAlbumCover(true, true);
                        this.pressTime = SystemClock.elapsedRealtime();
                    }
                } else if (action != 2 && SystemClock.elapsedRealtime() - this.pressTime >= 400) {
                    AudioPlayerAlert.this.showAlbumCover(false, true);
                }
                return true;
            }
        };
        this.coverContainer = coverContainer;
        this.playerLayout.addView(coverContainer, LayoutHelper.createFrame(44, 44.0f, 53, 0.0f, 20.0f, 20.0f, 0.0f));
        ClippingTextViewSwitcher clippingTextViewSwitcher = new ClippingTextViewSwitcher(context) { // from class: org.telegram.ui.Components.AudioPlayerAlert.8
            @Override // org.telegram.ui.Components.AudioPlayerAlert.ClippingTextViewSwitcher
            protected TextView createTextView() {
                TextView textView = new TextView(context);
                textView.setTextColor(AudioPlayerAlert.this.getThemedColor(Theme.key_player_actionBarTitle));
                textView.setTextSize(1, 17.0f);
                textView.setTypeface(AndroidUtilities.bold());
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setSingleLine(true);
                return textView;
            }
        };
        this.titleTextView = clippingTextViewSwitcher;
        this.playerLayout.addView(clippingTextViewSwitcher, LayoutHelper.createFrame(-1, -2.0f, 51, 20.0f, 20.0f, 72.0f, 0.0f));
        9 r1 = new 9(context, context);
        this.authorTextView = r1;
        this.playerLayout.addView(r1, LayoutHelper.createFrame(-1, -2.0f, 51, 14.0f, 47.0f, 72.0f, 0.0f));
        SeekBarView seekBarView = new SeekBarView(context, resourcesProvider) { // from class: org.telegram.ui.Components.AudioPlayerAlert.10
            @Override // org.telegram.ui.Components.SeekBarView
            boolean onTouch(MotionEvent motionEvent) {
                if (AudioPlayerAlert.this.rewindingState != 0) {
                    return false;
                }
                return super.onTouch(motionEvent);
            }
        };
        this.seekBarView = seekBarView;
        seekBarView.setLineWidth(4);
        this.seekBarView.setDelegate(new SeekBarView.SeekBarViewDelegate() { // from class: org.telegram.ui.Components.AudioPlayerAlert.11
            @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
            public CharSequence getContentDescription() {
                return LocaleController.formatString("AccDescrPlayerDuration", R.string.AccDescrPlayerDuration, LocaleController.formatPluralString("Minutes", AudioPlayerAlert.this.lastTime / 60, new Object[0]) + ' ' + LocaleController.formatPluralString("Seconds", AudioPlayerAlert.this.lastTime % 60, new Object[0]), LocaleController.formatPluralString("Minutes", AudioPlayerAlert.this.lastDuration / 60, new Object[0]) + ' ' + LocaleController.formatPluralString("Seconds", AudioPlayerAlert.this.lastDuration % 60, new Object[0]));
            }

            @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
            public /* synthetic */ int getStepsCount() {
                return SeekBarView.SeekBarViewDelegate.-CC.$default$getStepsCount(this);
            }

            @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
            public void onSeekBarDrag(boolean z, float f) {
                if (z) {
                    MediaController.getInstance().seekToProgress(MediaController.getInstance().getPlayingMessageObject(), f);
                }
                MessageObject playingMessageObject2 = MediaController.getInstance().getPlayingMessageObject();
                if (playingMessageObject2 == null || !playingMessageObject2.isMusic()) {
                    return;
                }
                AudioPlayerAlert.this.updateProgress(playingMessageObject2);
            }

            @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
            public void onSeekBarPressed(boolean z) {
                AudioPlayerAlert.this.draggingSeekBar = z;
            }
        });
        this.seekBarView.setReportChanges(true);
        this.playerLayout.addView(this.seekBarView, LayoutHelper.createFrame(-1, 44.0f, 51, 5.0f, 67.0f, 5.0f, 0.0f));
        this.seekBarBufferSpring = (SpringAnimation) new SpringAnimation(new FloatValueHolder(0.0f)).setSpring(new SpringForce().setStiffness(750.0f).setDampingRatio(1.0f)).addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda3
            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
            public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                AudioPlayerAlert.this.lambda$new$0(dynamicAnimation, f, f2);
            }
        });
        LineProgressView lineProgressView = new LineProgressView(context);
        this.progressView = lineProgressView;
        lineProgressView.setVisibility(4);
        this.progressView.setBackgroundColor(getThemedColor(Theme.key_player_progressBackground));
        this.progressView.setProgressColor(getThemedColor(Theme.key_player_progress));
        this.playerLayout.addView(this.progressView, LayoutHelper.createFrame(-1, 2.0f, 51, 21.0f, 90.0f, 21.0f, 0.0f));
        SimpleTextView simpleTextView = new SimpleTextView(context);
        this.timeTextView = simpleTextView;
        simpleTextView.setTextSize(12);
        this.timeTextView.setText("0:00");
        this.timeTextView.setTextColor(getThemedColor(i5));
        this.timeTextView.setImportantForAccessibility(2);
        this.playerLayout.addView(this.timeTextView, LayoutHelper.createFrame(100, -2.0f, 51, 20.0f, 98.0f, 0.0f, 0.0f));
        TextView textView = new TextView(context);
        this.durationTextView = textView;
        textView.setTextSize(1, 12.0f);
        this.durationTextView.setTextColor(getThemedColor(i5));
        this.durationTextView.setGravity(17);
        this.durationTextView.setImportantForAccessibility(2);
        this.playerLayout.addView(this.durationTextView, LayoutHelper.createFrame(-2, -2.0f, 53, 0.0f, 96.0f, 20.0f, 0.0f));
        ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, null, 0, getThemedColor(i5), false, resourcesProvider);
        this.playbackSpeedButton = actionBarMenuItem;
        actionBarMenuItem.setLongClickEnabled(false);
        this.playbackSpeedButton.setShowSubmenuByMove(false);
        this.playbackSpeedButton.setAdditionalYOffset(-AndroidUtilities.dp(224.0f));
        this.playbackSpeedButton.setContentDescription(LocaleController.getString(R.string.AccDescrPlayerSpeed));
        this.playbackSpeedButton.setDelegate(new ActionBarMenuItem.ActionBarMenuItemDelegate() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda6
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemDelegate
            public final void onItemClick(int i6) {
                AudioPlayerAlert.this.lambda$new$1(i6);
            }
        });
        ActionBarMenuItem actionBarMenuItem2 = this.playbackSpeedButton;
        SpeedIconDrawable speedIconDrawable = new SpeedIconDrawable(true);
        this.speedIcon = speedIconDrawable;
        actionBarMenuItem2.setIcon(speedIconDrawable);
        final float[] fArr = {1.0f, 1.5f, 2.0f};
        ActionBarMenuSlider.SpeedSlider speedSlider = new ActionBarMenuSlider.SpeedSlider(getContext(), resourcesProvider);
        this.speedSlider = speedSlider;
        speedSlider.setRoundRadiusDp(6.0f);
        this.speedSlider.setDrawShadow(true);
        this.speedSlider.setOnValueChange(new Utilities.Callback2() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda7
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                AudioPlayerAlert.this.lambda$new$2((Float) obj, (Boolean) obj2);
            }
        });
        this.speedItems[0] = this.playbackSpeedButton.addSubItem(0, R.drawable.msg_speed_slow, LocaleController.getString(R.string.SpeedSlow));
        this.speedItems[1] = this.playbackSpeedButton.addSubItem(1, R.drawable.msg_speed_normal, LocaleController.getString(R.string.SpeedNormal));
        this.speedItems[2] = this.playbackSpeedButton.addSubItem(2, R.drawable.msg_speed_medium, LocaleController.getString(R.string.SpeedMedium));
        this.speedItems[3] = this.playbackSpeedButton.addSubItem(3, R.drawable.msg_speed_fast, LocaleController.getString(R.string.SpeedFast));
        this.speedItems[4] = this.playbackSpeedButton.addSubItem(4, R.drawable.msg_speed_veryfast, LocaleController.getString(R.string.SpeedVeryFast));
        this.speedItems[5] = this.playbackSpeedButton.addSubItem(5, R.drawable.msg_speed_superfast, LocaleController.getString(R.string.SpeedSuperFast));
        if (AndroidUtilities.density >= 3.0f) {
            this.playbackSpeedButton.setPadding(0, 1, 0, 0);
        }
        this.playbackSpeedButton.setAdditionalXOffset(AndroidUtilities.dp(8.0f));
        this.playbackSpeedButton.setAdditionalYOffset(-AndroidUtilities.dp(400.0f));
        this.playbackSpeedButton.setShowedFromBottom(true);
        this.playerLayout.addView(this.playbackSpeedButton, LayoutHelper.createFrame(36, 36.0f, 53, 0.0f, 86.0f, 20.0f, 0.0f));
        this.playbackSpeedButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                AudioPlayerAlert.this.lambda$new$3(fArr, view3);
            }
        });
        this.playbackSpeedButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda9
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view3) {
                boolean lambda$new$4;
                lambda$new$4 = AudioPlayerAlert.this.lambda$new$4(resourcesProvider, view3);
                return lambda$new$4;
            }
        });
        updatePlaybackButton(false);
        FrameLayout frameLayout2 = new FrameLayout(context) { // from class: org.telegram.ui.Components.AudioPlayerAlert.12
            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i6, int i7, int i8, int i9) {
                int dp = ((i8 - i6) - AndroidUtilities.dp(248.0f)) / 4;
                for (int i10 = 0; i10 < 5; i10++) {
                    int dp2 = AndroidUtilities.dp((i10 * 48) + 4) + (dp * i10);
                    int dp3 = AndroidUtilities.dp(9.0f);
                    AudioPlayerAlert.this.buttons[i10].layout(dp2, dp3, AudioPlayerAlert.this.buttons[i10].getMeasuredWidth() + dp2, AudioPlayerAlert.this.buttons[i10].getMeasuredHeight() + dp3);
                }
            }
        };
        this.playerLayout.addView(frameLayout2, LayoutHelper.createFrame(-1, 66.0f, 51, 0.0f, 111.0f, 0.0f, 0.0f));
        View[] viewArr = this.buttons;
        ActionBarMenuItem actionBarMenuItem3 = new ActionBarMenuItem(context, null, 0, 0, false, resourcesProvider);
        this.repeatButton = actionBarMenuItem3;
        viewArr[0] = actionBarMenuItem3;
        actionBarMenuItem3.setLongClickEnabled(false);
        this.repeatButton.setShowSubmenuByMove(false);
        this.repeatButton.setAdditionalYOffset(-AndroidUtilities.dp(166.0f));
        int i6 = Build.VERSION.SDK_INT;
        if (i6 >= 21) {
            this.repeatButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector), 1, AndroidUtilities.dp(18.0f)));
        }
        frameLayout2.addView(this.repeatButton, LayoutHelper.createFrame(48, 48, 51));
        this.repeatButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                AudioPlayerAlert.this.lambda$new$5(view3);
            }
        });
        this.repeatSongItem = this.repeatButton.addSubItem(3, R.drawable.player_new_repeatone, LocaleController.getString(R.string.RepeatSong));
        this.repeatListItem = this.repeatButton.addSubItem(4, R.drawable.player_new_repeatall, LocaleController.getString(R.string.RepeatList));
        this.shuffleListItem = this.repeatButton.addSubItem(2, R.drawable.player_new_shuffle, LocaleController.getString(R.string.ShuffleList));
        this.reverseOrderItem = this.repeatButton.addSubItem(1, R.drawable.player_new_order, LocaleController.getString(R.string.ReverseOrder));
        this.repeatButton.setShowedFromBottom(true);
        this.repeatButton.setDelegate(new ActionBarMenuItem.ActionBarMenuItemDelegate() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda11
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemDelegate
            public final void onItemClick(int i7) {
                AudioPlayerAlert.this.lambda$new$6(i7);
            }
        });
        int i7 = Theme.key_player_button;
        int themedColor = getThemedColor(i7);
        float scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        View[] viewArr2 = this.buttons;
        13 r13 = new 13(context, scaledTouchSlop);
        this.prevButton = r13;
        viewArr2[1] = r13;
        ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER;
        r13.setScaleType(scaleType);
        RLottieImageView rLottieImageView = this.prevButton;
        int i8 = R.raw.player_prev;
        rLottieImageView.setAnimation(i8, 20, 20);
        this.prevButton.setLayerColor("Triangle 3.**", themedColor);
        this.prevButton.setLayerColor("Triangle 4.**", themedColor);
        this.prevButton.setLayerColor("Rectangle 4.**", themedColor);
        if (i6 >= 21) {
            this.prevButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector), 1, AndroidUtilities.dp(22.0f)));
        }
        frameLayout2.addView(this.prevButton, LayoutHelper.createFrame(48, 48, 51));
        this.prevButton.setContentDescription(LocaleController.getString(R.string.AccDescrPrevious));
        View[] viewArr3 = this.buttons;
        ImageView imageView = new ImageView(context);
        this.playButton = imageView;
        viewArr3[2] = imageView;
        imageView.setScaleType(scaleType);
        ImageView imageView2 = this.playButton;
        PlayPauseDrawable playPauseDrawable = new PlayPauseDrawable(28);
        this.playPauseDrawable = playPauseDrawable;
        imageView2.setImageDrawable(playPauseDrawable);
        this.playPauseDrawable.setPause(!MediaController.getInstance().isMessagePaused(), false);
        ImageView imageView3 = this.playButton;
        int themedColor2 = getThemedColor(i7);
        PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
        imageView3.setColorFilter(new PorterDuffColorFilter(themedColor2, mode));
        if (i6 >= 21) {
            this.playButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector), 1, AndroidUtilities.dp(24.0f)));
        }
        frameLayout2.addView(this.playButton, LayoutHelper.createFrame(48, 48, 51));
        this.playButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                AudioPlayerAlert.lambda$new$7(view3);
            }
        });
        View[] viewArr4 = this.buttons;
        14 r7 = new 14(context, scaledTouchSlop);
        this.nextButton = r7;
        viewArr4[3] = r7;
        r7.setScaleType(scaleType);
        this.nextButton.setAnimation(i8, 20, 20);
        this.nextButton.setLayerColor("Triangle 3.**", themedColor);
        this.nextButton.setLayerColor("Triangle 4.**", themedColor);
        this.nextButton.setLayerColor("Rectangle 4.**", themedColor);
        this.nextButton.setRotation(180.0f);
        if (i6 >= 21) {
            this.nextButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector), 1, AndroidUtilities.dp(22.0f)));
        }
        frameLayout2.addView(this.nextButton, LayoutHelper.createFrame(48, 48, 51));
        this.nextButton.setContentDescription(LocaleController.getString(R.string.Next));
        View[] viewArr5 = this.buttons;
        ActionBarMenuItem actionBarMenuItem4 = new ActionBarMenuItem(context, null, 0, themedColor, false, resourcesProvider);
        this.optionsButton = actionBarMenuItem4;
        viewArr5[4] = actionBarMenuItem4;
        actionBarMenuItem4.setLongClickEnabled(false);
        this.optionsButton.setShowSubmenuByMove(false);
        this.optionsButton.setIcon(R.drawable.ic_ab_other);
        this.optionsButton.setSubMenuOpenSide(2);
        this.optionsButton.setAdditionalYOffset(-AndroidUtilities.dp(157.0f));
        if (i6 >= 21) {
            i = 1;
            this.optionsButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector), 1, AndroidUtilities.dp(18.0f)));
        } else {
            i = 1;
        }
        frameLayout2.addView(this.optionsButton, LayoutHelper.createFrame(48, 48, 51));
        this.optionsButton.addSubItem(i, R.drawable.msg_forward, LocaleController.getString(R.string.Forward));
        this.optionsButton.addSubItem(2, R.drawable.msg_shareout, LocaleController.getString(R.string.ShareFile));
        this.optionsButton.addSubItem(5, R.drawable.msg_download, LocaleController.getString(R.string.SaveToMusic));
        this.optionsButton.addSubItem(4, R.drawable.msg_message, LocaleController.getString(R.string.ShowInChat));
        this.optionsButton.setShowedFromBottom(true);
        this.optionsButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                AudioPlayerAlert.this.lambda$new$8(view3);
            }
        });
        this.optionsButton.setDelegate(new ActionBarMenuItem.ActionBarMenuItemDelegate() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda14
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemDelegate
            public final void onItemClick(int i9) {
                AudioPlayerAlert.this.onSubItemClick(i9);
            }
        });
        this.optionsButton.setContentDescription(LocaleController.getString(R.string.AccDescrMoreOptions));
        LinearLayout linearLayout = new LinearLayout(context);
        this.emptyView = linearLayout;
        linearLayout.setOrientation(1);
        this.emptyView.setGravity(17);
        this.emptyView.setVisibility(8);
        this.containerView.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        this.emptyView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda4
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view3, MotionEvent motionEvent) {
                boolean lambda$new$9;
                lambda$new$9 = AudioPlayerAlert.lambda$new$9(view3, motionEvent);
                return lambda$new$9;
            }
        });
        ImageView imageView4 = new ImageView(context);
        this.emptyImageView = imageView4;
        imageView4.setImageResource(R.drawable.music_empty);
        this.emptyImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_dialogEmptyImage), mode));
        this.emptyView.addView(this.emptyImageView, LayoutHelper.createLinear(-2, -2));
        TextView textView2 = new TextView(context);
        this.emptyTitleTextView = textView2;
        int i9 = Theme.key_dialogEmptyText;
        textView2.setTextColor(getThemedColor(i9));
        this.emptyTitleTextView.setGravity(17);
        this.emptyTitleTextView.setText(LocaleController.getString(R.string.NoAudioFound));
        this.emptyTitleTextView.setTypeface(AndroidUtilities.bold());
        this.emptyTitleTextView.setTextSize(1, 17.0f);
        this.emptyTitleTextView.setPadding(AndroidUtilities.dp(40.0f), 0, AndroidUtilities.dp(40.0f), 0);
        this.emptyView.addView(this.emptyTitleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 11, 0, 0));
        TextView textView3 = new TextView(context);
        this.emptySubtitleTextView = textView3;
        textView3.setTextColor(getThemedColor(i9));
        this.emptySubtitleTextView.setGravity(17);
        this.emptySubtitleTextView.setTextSize(1, 15.0f);
        this.emptySubtitleTextView.setPadding(AndroidUtilities.dp(40.0f), 0, AndroidUtilities.dp(40.0f), 0);
        this.emptyView.addView(this.emptySubtitleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 6, 0, 0));
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.Components.AudioPlayerAlert.15
            boolean ignoreLayout;

            @Override // org.telegram.ui.Components.RecyclerListView
            protected boolean allowSelectChildAtPosition(float f, float f2) {
                return f2 < AudioPlayerAlert.this.playerLayout.getY() - ((float) AudioPlayerAlert.this.listView.getTop());
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
            public void onLayout(boolean z, int i10, int i11, int i12, int i13) {
                super.onLayout(z, i10, i11, i12, i13);
                if (AudioPlayerAlert.this.searchOpenPosition != -1 && !AudioPlayerAlert.this.actionBar.isSearchFieldVisible()) {
                    this.ignoreLayout = true;
                    AudioPlayerAlert.this.layoutManager.scrollToPositionWithOffset(AudioPlayerAlert.this.searchOpenPosition, AudioPlayerAlert.this.searchOpenOffset - AudioPlayerAlert.this.listView.getPaddingTop());
                    super.onLayout(false, i10, i11, i12, i13);
                    this.ignoreLayout = false;
                    AudioPlayerAlert.this.searchOpenPosition = -1;
                    return;
                }
                if (AudioPlayerAlert.this.scrollToSong) {
                    AudioPlayerAlert.this.scrollToSong = false;
                    this.ignoreLayout = true;
                    if (AudioPlayerAlert.this.scrollToCurrentSong(true)) {
                        super.onLayout(false, i10, i11, i12, i13);
                    }
                    this.ignoreLayout = false;
                }
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setClipToPadding(false);
        RecyclerListView recyclerListView2 = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView2.setLayoutManager(linearLayoutManager);
        this.listView.setHorizontalScrollBarEnabled(false);
        this.listView.setVerticalScrollBarEnabled(false);
        this.containerView.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        RecyclerListView recyclerListView3 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.listAdapter = listAdapter;
        recyclerListView3.setAdapter(listAdapter);
        this.listView.setGlowColor(getThemedColor(Theme.key_dialogScrollGlow));
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda5
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view3, int i10) {
                AudioPlayerAlert.lambda$new$10(view3, i10);
            }
        });
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.AudioPlayerAlert.16
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i10) {
                if (i10 != 0) {
                    if (i10 == 1) {
                        AndroidUtilities.hideKeyboard(AudioPlayerAlert.this.getCurrentFocus());
                        return;
                    }
                    return;
                }
                if (((AudioPlayerAlert.this.scrollOffsetY - ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop) - AndroidUtilities.dp(13.0f)) + ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop >= ActionBar.getCurrentActionBarHeight() || !AudioPlayerAlert.this.listView.canScrollVertically(1)) {
                    return;
                }
                AudioPlayerAlert.this.listView.getChildAt(0);
                RecyclerListView.Holder holder = (RecyclerListView.Holder) AudioPlayerAlert.this.listView.findViewHolderForAdapterPosition(0);
                if (holder == null || holder.itemView.getTop() <= AndroidUtilities.dp(7.0f)) {
                    return;
                }
                AudioPlayerAlert.this.listView.smoothScrollBy(0, holder.itemView.getTop() - AndroidUtilities.dp(7.0f));
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i10, int i11) {
                AudioPlayerAlert.this.updateLayout();
                AudioPlayerAlert.this.updateEmptyViewPosition();
                if (AudioPlayerAlert.this.searchWas) {
                    return;
                }
                int findFirstVisibleItemPosition = AudioPlayerAlert.this.layoutManager.findFirstVisibleItemPosition();
                int abs = findFirstVisibleItemPosition == -1 ? 0 : Math.abs(AudioPlayerAlert.this.layoutManager.findLastVisibleItemPosition() - findFirstVisibleItemPosition) + 1;
                int itemCount = recyclerView.getAdapter().getItemCount();
                MediaController.getInstance().getPlayingMessageObject();
                if (SharedConfig.playOrderReversed) {
                    if (findFirstVisibleItemPosition >= 10) {
                        return;
                    }
                } else if (findFirstVisibleItemPosition + abs <= itemCount - 10) {
                    return;
                }
                MediaController.getInstance().loadMoreMusic();
            }
        });
        this.playlist = MediaController.getInstance().getPlaylist();
        this.listAdapter.notifyDataSetChanged();
        this.containerView.addView(this.playerLayout, LayoutHelper.createFrame(-1, NotificationCenter.suggestedFiltersLoaded, 83));
        this.containerView.addView(this.playerShadow, new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83));
        ((FrameLayout.LayoutParams) this.playerShadow.getLayoutParams()).bottomMargin = AndroidUtilities.dp(179.0f);
        this.containerView.addView(this.actionBarShadow, LayoutHelper.createFrame(-1, 3.0f));
        this.containerView.addView(this.actionBar);
        FrameLayout frameLayout3 = new FrameLayout(context) { // from class: org.telegram.ui.Components.AudioPlayerAlert.17
            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (AudioPlayerAlert.this.blurredView.getTag() != null) {
                    AudioPlayerAlert.this.showAlbumCover(false, true);
                }
                return true;
            }
        };
        this.blurredView = frameLayout3;
        frameLayout3.setAlpha(0.0f);
        this.blurredView.setVisibility(4);
        getContainer().addView(this.blurredView);
        BackupImageView backupImageView = new BackupImageView(context);
        this.bigAlbumConver = backupImageView;
        backupImageView.setAspectFit(true);
        this.bigAlbumConver.setRoundRadius(AndroidUtilities.dp(8.0f));
        this.bigAlbumConver.setScaleX(0.9f);
        this.bigAlbumConver.setScaleY(0.9f);
        this.blurredView.addView(this.bigAlbumConver, LayoutHelper.createFrame(-1, -1.0f, 51, 30.0f, 30.0f, 30.0f, 30.0f));
        updateTitle(false);
        updateRepeatButton();
        updateEmptyView();
    }

    private void checkIfMusicDownloaded(MessageObject messageObject) {
        String str = messageObject.messageOwner.attachPath;
        File file = null;
        if (str != null && str.length() > 0) {
            File file2 = new File(messageObject.messageOwner.attachPath);
            if (file2.exists()) {
                file = file2;
            }
        }
        if (file == null) {
            file = FileLoader.getInstance(this.currentAccount).getPathToMessage(messageObject.messageOwner);
        }
        boolean z = SharedConfig.streamMedia && ((int) messageObject.getDialogId()) != 0 && messageObject.isMusic();
        if (file.exists() || z) {
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            this.progressView.setVisibility(4);
            this.seekBarView.setVisibility(0);
            this.playButton.setEnabled(true);
            return;
        }
        String fileName = messageObject.getFileName();
        DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(fileName, this);
        Float fileProgress = ImageLoader.getInstance().getFileProgress(fileName);
        this.progressView.setProgress(fileProgress != null ? fileProgress.floatValue() : 0.0f, false);
        this.progressView.setVisibility(0);
        this.seekBarView.setVisibility(4);
        this.playButton.setEnabled(false);
    }

    private void checkSpeedHint() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.lastPlaybackClick > 300) {
            int i = MessagesController.getGlobalNotificationsSettings().getInt("speedhint", 0) + 1;
            if (i > 2) {
                i = -10;
            }
            MessagesController.getGlobalNotificationsSettings().edit().putInt("speedhint", i).apply();
            if (i >= 0) {
                showSpeedHint();
            }
        }
        this.lastPlaybackClick = currentTimeMillis;
    }

    private boolean equals(float f, float f2) {
        return Math.abs(f - f2) < 0.05f;
    }

    private ImageLocation getArtworkThumbImageLocation(MessageObject messageObject) {
        TLRPC.Document document = messageObject.getDocument();
        TLRPC.PhotoSize closestPhotoSizeWithSize = document != null ? FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 360) : null;
        if (!(closestPhotoSizeWithSize instanceof TLRPC.TL_photoSize) && !(closestPhotoSizeWithSize instanceof TLRPC.TL_photoSizeProgressive)) {
            closestPhotoSizeWithSize = null;
        }
        if (closestPhotoSizeWithSize != null) {
            return ImageLocation.getForDocument(closestPhotoSizeWithSize, document);
        }
        String artworkUrl = messageObject.getArtworkUrl(true);
        if (artworkUrl != null) {
            return ImageLocation.getForPath(artworkUrl);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$13() {
        this.searchItem.getSearchField().setCursorColor(getThemedColor(Theme.key_player_actionBarTitle));
        ActionBarMenuItem actionBarMenuItem = this.repeatButton;
        actionBarMenuItem.setIconColor(getThemedColor(((Integer) actionBarMenuItem.getTag()).intValue()));
        Drawable background = this.repeatButton.getBackground();
        int i = Theme.key_listSelector;
        Theme.setSelectorDrawableColor(background, getThemedColor(i), true);
        this.optionsButton.setIconColor(getThemedColor(Theme.key_player_button));
        Theme.setSelectorDrawableColor(this.optionsButton.getBackground(), getThemedColor(i), true);
        this.progressView.setBackgroundColor(getThemedColor(Theme.key_player_progressBackground));
        this.progressView.setProgressColor(getThemedColor(Theme.key_player_progress));
        updateSubMenu();
        ActionBarMenuItem actionBarMenuItem2 = this.repeatButton;
        int i2 = Theme.key_actionBarDefaultSubmenuBackground;
        actionBarMenuItem2.redrawPopup(getThemedColor(i2));
        ActionBarMenuItem actionBarMenuItem3 = this.optionsButton;
        int i3 = Theme.key_actionBarDefaultSubmenuItem;
        actionBarMenuItem3.setPopupItemsColor(getThemedColor(i3), false);
        this.optionsButton.setPopupItemsColor(getThemedColor(i3), true);
        this.optionsButton.redrawPopup(getThemedColor(i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(DynamicAnimation dynamicAnimation, float f, float f2) {
        this.seekBarView.setBufferedProgress(f / 1000.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(int i) {
        if (i >= 0) {
            float[] fArr = speeds;
            if (i >= fArr.length) {
                return;
            }
            MediaController.getInstance().setPlaybackSpeed(true, fArr[i]);
            updatePlaybackButton(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$10(View view, int i) {
        if (view instanceof AudioPlayerCell) {
            ((AudioPlayerCell) view).didPressedButton();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(Float f, Boolean bool) {
        this.slidingSpeed = !bool.booleanValue();
        MediaController.getInstance().setPlaybackSpeed(true, this.speedSlider.getSpeed(f.floatValue()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(float[] fArr, View view) {
        float playbackSpeed = MediaController.getInstance().getPlaybackSpeed(true);
        int i = 0;
        while (true) {
            if (i >= fArr.length) {
                i = -1;
                break;
            } else if (playbackSpeed - 0.1f <= fArr[i]) {
                break;
            } else {
                i++;
            }
        }
        int i2 = i + 1;
        MediaController.getInstance().setPlaybackSpeed(true, fArr[i2 < fArr.length ? i2 : 0]);
        checkSpeedHint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$4(Theme.ResourcesProvider resourcesProvider, View view) {
        this.speedSlider.setSpeed(MediaController.getInstance().getPlaybackSpeed(true), false);
        this.speedSlider.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSubmenuBackground, resourcesProvider));
        updatePlaybackButton(false);
        this.playbackSpeedButton.setDimMenu(0.15f);
        this.playbackSpeedButton.toggleSubMenu(this.speedSlider, null);
        MessagesController.getGlobalNotificationsSettings().edit().putInt("speedhint", -15).apply();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(View view) {
        updateSubMenu();
        this.repeatButton.toggleSubMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(int i) {
        if (i == 1 || i == 2) {
            boolean z = SharedConfig.playOrderReversed;
            if ((z && i == 1) || (SharedConfig.shuffleMusic && i == 2)) {
                MediaController.getInstance().setPlaybackOrderType(0);
            } else {
                MediaController.getInstance().setPlaybackOrderType(i);
            }
            this.listAdapter.notifyDataSetChanged();
            if (z != SharedConfig.playOrderReversed) {
                this.listView.stopScroll();
                scrollToCurrentSong(false);
            }
        } else if (i == 4) {
            if (SharedConfig.repeatMode != 1) {
                SharedConfig.setRepeatMode(1);
            }
            SharedConfig.setRepeatMode(0);
        } else {
            if (SharedConfig.repeatMode != 2) {
                SharedConfig.setRepeatMode(2);
            }
            SharedConfig.setRepeatMode(0);
        }
        updateRepeatButton();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$7(View view) {
        if (MediaController.getInstance().isDownloadingCurrentMessage()) {
            return;
        }
        if (MediaController.getInstance().isMessagePaused()) {
            MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
        } else {
            MediaController.getInstance().lambda$startAudioAgain$7(MediaController.getInstance().getPlayingMessageObject());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8(View view) {
        this.optionsButton.toggleSubMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$new$9(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onSubItemClick$11(ArrayList arrayList, DialogsActivity dialogsActivity, ArrayList arrayList2, CharSequence charSequence, boolean z, boolean z2, int i, TopicsFragment topicsFragment) {
        long j;
        String str;
        if (arrayList2.size() > 1 || ((MessagesStorage.TopicKey) arrayList2.get(0)).dialogId == UserConfig.getInstance(this.currentAccount).getClientUserId() || charSequence != null) {
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                long j2 = ((MessagesStorage.TopicKey) arrayList2.get(i2)).dialogId;
                if (charSequence != null) {
                    j = j2;
                    SendMessagesHelper.getInstance(this.currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(charSequence.toString(), j2, null, null, null, true, null, null, null, true, 0, null, false));
                } else {
                    j = j2;
                }
                SendMessagesHelper.getInstance(this.currentAccount).sendMessage(arrayList, j, false, false, true, 0);
            }
        } else {
            MessagesStorage.TopicKey topicKey = (MessagesStorage.TopicKey) arrayList2.get(0);
            long j3 = topicKey.dialogId;
            Bundle bundle = new Bundle();
            bundle.putBoolean("scrollToTopOnResume", true);
            if (DialogObject.isEncryptedDialog(j3)) {
                bundle.putInt("enc_id", DialogObject.getEncryptedChatId(j3));
            } else {
                if (DialogObject.isUserDialog(j3)) {
                    str = "user_id";
                } else {
                    j3 = -j3;
                    str = "chat_id";
                }
                bundle.putLong(str, j3);
            }
            ChatActivity chatActivity = new ChatActivity(bundle);
            if (topicKey.topicId != 0) {
                ForumUtilities.applyTopic(chatActivity, topicKey);
            }
            if (this.parentActivity.presentFragment(chatActivity, true, false)) {
                chatActivity.showFieldPanelForForward(true, arrayList);
                if (topicKey.topicId != 0) {
                    dialogsActivity.removeSelfFromStack();
                }
                return true;
            }
        }
        dialogsActivity.lambda$onBackPressed$319();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSubItemClick$12(Uri uri) {
        BulletinFactory.of((FrameLayout) this.containerView, this.resourcesProvider).createDownloadBulletin(BulletinFactory.FileType.AUDIO).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x006b, code lost:
    
        if (r12.exists() == false) goto L20;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onSubItemClick(int i) {
        LaunchActivity launchActivity;
        File file;
        int checkSelfPermission;
        String str;
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject == null || (launchActivity = this.parentActivity) == null) {
            return;
        }
        if (i == 1) {
            int i2 = UserConfig.selectedAccount;
            int i3 = this.currentAccount;
            if (i2 != i3) {
                launchActivity.switchToAccount(i3, true);
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean("onlySelect", true);
            bundle.putInt("dialogsType", 3);
            bundle.putBoolean("canSelectTopics", true);
            DialogsActivity dialogsActivity = new DialogsActivity(bundle);
            final ArrayList arrayList = new ArrayList();
            arrayList.add(playingMessageObject);
            dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda15
                @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList2, CharSequence charSequence, boolean z, boolean z2, int i4, TopicsFragment topicsFragment) {
                    boolean lambda$onSubItemClick$11;
                    lambda$onSubItemClick$11 = AudioPlayerAlert.this.lambda$onSubItemClick$11(arrayList, dialogsActivity2, arrayList2, charSequence, z, z2, i4, topicsFragment);
                    return lambda$onSubItemClick$11;
                }
            });
            this.parentActivity.lambda$runLinkRequest$93(dialogsActivity);
        } else {
            if (i == 2) {
                try {
                    if (!TextUtils.isEmpty(playingMessageObject.messageOwner.attachPath)) {
                        file = new File(playingMessageObject.messageOwner.attachPath);
                    }
                    file = null;
                    if (file == null) {
                        file = FileLoader.getInstance(this.currentAccount).getPathToMessage(playingMessageObject.messageOwner);
                    }
                    if (!file.exists()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this.parentActivity);
                        builder.setTitle(LocaleController.getString(R.string.AppName));
                        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
                        builder.setMessage(LocaleController.getString(R.string.PleaseDownload));
                        builder.show();
                        return;
                    }
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType(playingMessageObject.getMimeType());
                    if (Build.VERSION.SDK_INT >= 24) {
                        try {
                            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(ApplicationLoader.applicationContext, ApplicationLoader.getApplicationId() + ".provider", file));
                            intent.setFlags(1);
                        } catch (Exception unused) {
                        }
                        this.parentActivity.startActivityForResult(Intent.createChooser(intent, LocaleController.getString(R.string.ShareFile)), 500);
                        return;
                    }
                    intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
                    this.parentActivity.startActivityForResult(Intent.createChooser(intent, LocaleController.getString(R.string.ShareFile)), 500);
                    return;
                } catch (Exception e) {
                    FileLog.e(e);
                    return;
                }
            }
            if (i != 4) {
                if (i == 5) {
                    int i4 = Build.VERSION.SDK_INT;
                    if (i4 >= 23 && (i4 <= 28 || BuildVars.NO_SCOPED_STORAGE)) {
                        checkSelfPermission = launchActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
                        if (checkSelfPermission != 0) {
                            this.parentActivity.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
                            return;
                        }
                    }
                    String documentFileName = FileLoader.getDocumentFileName(playingMessageObject.getDocument());
                    if (TextUtils.isEmpty(documentFileName)) {
                        documentFileName = playingMessageObject.getFileName();
                    }
                    String str2 = documentFileName;
                    String str3 = playingMessageObject.messageOwner.attachPath;
                    String str4 = (str3 == null || str3.length() <= 0 || new File(str3).exists()) ? str3 : null;
                    MediaController.saveFile((str4 == null || str4.length() == 0) ? FileLoader.getInstance(this.currentAccount).getPathToMessage(playingMessageObject.messageOwner).toString() : str4, this.parentActivity, 3, str2, playingMessageObject.getDocument() != null ? playingMessageObject.getDocument().mime_type : "", new Utilities.Callback() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda16
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            AudioPlayerAlert.this.lambda$onSubItemClick$12((Uri) obj);
                        }
                    });
                    return;
                }
                return;
            }
            int i5 = UserConfig.selectedAccount;
            int i6 = this.currentAccount;
            if (i5 != i6) {
                launchActivity.switchToAccount(i6, true);
            }
            Bundle bundle2 = new Bundle();
            long dialogId = playingMessageObject.getDialogId();
            if (DialogObject.isEncryptedDialog(dialogId)) {
                bundle2.putInt("enc_id", DialogObject.getEncryptedChatId(dialogId));
            } else {
                if (DialogObject.isUserDialog(dialogId)) {
                    str = "user_id";
                } else {
                    TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-dialogId));
                    if (chat != null && chat.migrated_to != null) {
                        bundle2.putLong("migrated_to", dialogId);
                        dialogId = -chat.migrated_to.channel_id;
                    }
                    dialogId = -dialogId;
                    str = "chat_id";
                }
                bundle2.putLong(str, dialogId);
            }
            bundle2.putInt("message_id", playingMessageObject.getId());
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
            this.parentActivity.presentFragment(new ChatActivity(bundle2), false, false);
        }
        dismiss();
    }

    private void preloadNeighboringThumbs() {
        MediaController mediaController = MediaController.getInstance();
        ArrayList<MessageObject> playlist = mediaController.getPlaylist();
        if (playlist.size() <= 1) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        int playingMessageObjectNum = mediaController.getPlayingMessageObjectNum();
        int i = playingMessageObjectNum + 1;
        int i2 = playingMessageObjectNum - 1;
        if (i >= playlist.size()) {
            i = 0;
        }
        if (i2 <= -1) {
            i2 = playlist.size() - 1;
        }
        arrayList.add(playlist.get(i));
        if (i != i2) {
            arrayList.add(playlist.get(i2));
        }
        int size = arrayList.size();
        for (int i3 = 0; i3 < size; i3++) {
            MessageObject messageObject = (MessageObject) arrayList.get(i3);
            ImageLocation artworkThumbImageLocation = getArtworkThumbImageLocation(messageObject);
            if (artworkThumbImageLocation != null) {
                if (artworkThumbImageLocation.path != null) {
                    ImageLoader.getInstance().preloadArtwork(artworkThumbImageLocation.path);
                } else {
                    FileLoader.getInstance(this.currentAccount).loadFile(artworkThumbImageLocation, messageObject, null, 0, 1);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0033, code lost:
    
        if (r3.getBottom() <= r5.listView.getMeasuredHeight()) goto L23;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean scrollToCurrentSong(boolean z) {
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject != null) {
            if (z) {
                int childCount = this.listView.getChildCount();
                int i = 0;
                while (true) {
                    if (i >= childCount) {
                        break;
                    }
                    View childAt = this.listView.getChildAt(i);
                    if (!(childAt instanceof AudioPlayerCell) || ((AudioPlayerCell) childAt).getMessageObject() != playingMessageObject) {
                        i++;
                    }
                }
            }
            int indexOf = this.playlist.indexOf(playingMessageObject);
            if (indexOf >= 0) {
                if (SharedConfig.playOrderReversed) {
                    this.layoutManager.scrollToPosition(indexOf);
                    return true;
                }
                this.layoutManager.scrollToPosition(this.playlist.size() - indexOf);
                return true;
            }
        }
        return false;
    }

    private void setMenuItemChecked(ActionBarMenuSubItem actionBarMenuSubItem, boolean z) {
        int i = z ? Theme.key_player_buttonActive : Theme.key_actionBarDefaultSubmenuItem;
        actionBarMenuSubItem.setTextColor(getThemedColor(i));
        actionBarMenuSubItem.setIconColor(getThemedColor(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAlbumCover(boolean z, boolean z2) {
        ViewPropertyAnimator scaleY;
        if (z) {
            if (this.blurredView.getVisibility() == 0 || this.blurredAnimationInProgress) {
                return;
            }
            this.blurredView.setTag(1);
            this.bigAlbumConver.setImageBitmap(this.coverContainer.getImageReceiver().getBitmap());
            this.blurredAnimationInProgress = true;
            View fragmentView = ((BaseFragment) this.parentActivity.getActionBarLayout().getFragmentStack().get(this.parentActivity.getActionBarLayout().getFragmentStack().size() - 1)).getFragmentView();
            if (fragmentView != null) {
                int measuredWidth = (int) (fragmentView.getMeasuredWidth() / 6.0f);
                int measuredHeight = (int) (fragmentView.getMeasuredHeight() / 6.0f);
                Bitmap createBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                canvas.scale(0.16666667f, 0.16666667f);
                fragmentView.draw(canvas);
                canvas.translate(this.containerView.getLeft() - getLeftInset(), 0.0f);
                this.containerView.draw(canvas);
                Utilities.stackBlurBitmap(createBitmap, Math.max(7, Math.max(measuredWidth, measuredHeight) / NotificationCenter.updateBotMenuButton));
                this.blurredView.setBackground(new BitmapDrawable(createBitmap));
            }
            this.blurredView.setVisibility(0);
            this.blurredView.animate().alpha(1.0f).setDuration(180L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.AudioPlayerAlert.19
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    AudioPlayerAlert.this.blurredAnimationInProgress = false;
                }
            }).start();
            scaleY = this.bigAlbumConver.animate().scaleX(1.0f).scaleY(1.0f);
        } else {
            if (this.blurredView.getVisibility() != 0) {
                return;
            }
            this.blurredView.setTag(null);
            if (!z2) {
                this.blurredView.setAlpha(0.0f);
                this.blurredView.setVisibility(4);
                this.bigAlbumConver.setImageBitmap(null);
                this.bigAlbumConver.setScaleX(0.9f);
                this.bigAlbumConver.setScaleY(0.9f);
                return;
            }
            this.blurredAnimationInProgress = true;
            this.blurredView.animate().alpha(0.0f).setDuration(180L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.AudioPlayerAlert.20
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    AudioPlayerAlert.this.blurredView.setVisibility(4);
                    AudioPlayerAlert.this.bigAlbumConver.setImageBitmap(null);
                    AudioPlayerAlert.this.blurredAnimationInProgress = false;
                }
            }).start();
            scaleY = this.bigAlbumConver.animate().scaleX(0.9f).scaleY(0.9f);
        }
        scaleY.setDuration(180L).start();
    }

    private void showSpeedHint() {
        if (this.containerView != null) {
            HintView hintView = new HintView(getContext(), 5, false) { // from class: org.telegram.ui.Components.AudioPlayerAlert.18
                @Override // android.view.View
                public void setVisibility(int i) {
                    super.setVisibility(i);
                    if (i != 0) {
                        try {
                            ((ViewGroup) getParent()).removeView(this);
                        } catch (Exception unused) {
                        }
                    }
                }
            };
            this.speedHintView = hintView;
            hintView.setExtraTranslationY(AndroidUtilities.dp(6.0f));
            this.speedHintView.setText(LocaleController.getString("SpeedHint"));
            this.playerLayout.addView(this.speedHintView, LayoutHelper.createFrame(-2, -2.0f, 48, 0.0f, 0.0f, 6.0f, 0.0f));
            this.speedHintView.showForView(this.playbackSpeedButton, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startForwardRewindingSeek() {
        if (this.rewindingState == 1) {
            this.lastRewindingTime = System.currentTimeMillis();
            this.rewindingProgress = MediaController.getInstance().getPlayingMessageObject().audioProgress;
            AndroidUtilities.cancelRunOnUIThread(this.forwardSeek);
            AndroidUtilities.runOnUIThread(this.forwardSeek);
        }
    }

    private void updateCover(MessageObject messageObject, boolean z) {
        long j;
        int i;
        ImageLocation imageLocation;
        CoverContainer coverContainer = this.coverContainer;
        BackupImageView nextImageView = z ? coverContainer.getNextImageView() : coverContainer.getImageView();
        AudioInfo audioInfo = MediaController.getInstance().getAudioInfo();
        if (audioInfo == null || audioInfo.getCover() == null) {
            this.currentFile = FileLoader.getAttachFileName(messageObject.getDocument());
            this.currentAudioFinishedLoading = false;
            String artworkUrl = messageObject.getArtworkUrl(false);
            ImageLocation artworkThumbImageLocation = getArtworkThumbImageLocation(messageObject);
            if (!TextUtils.isEmpty(artworkUrl)) {
                imageLocation = ImageLocation.getForPath(artworkUrl);
                j = 0;
                i = 1;
            } else if (artworkThumbImageLocation != null) {
                j = 0;
                i = 1;
                imageLocation = null;
            } else {
                nextImageView.setImageDrawable(null);
                nextImageView.invalidate();
            }
            nextImageView.setImage(imageLocation, null, artworkThumbImageLocation, null, null, j, i, messageObject);
            nextImageView.invalidate();
        } else {
            nextImageView.setImageBitmap(audioInfo.getCover());
            this.currentFile = null;
            this.currentAudioFinishedLoading = true;
        }
        if (z) {
            this.coverContainer.switchImageViews();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEmptyView() {
        this.emptyView.setVisibility((this.searching && this.listAdapter.getItemCount() == 0) ? 0 : 8);
        updateEmptyViewPosition();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEmptyViewPosition() {
        if (this.emptyView.getVisibility() != 0) {
            return;
        }
        int dp = this.playerLayout.getVisibility() == 0 ? AndroidUtilities.dp(150.0f) : -AndroidUtilities.dp(30.0f);
        this.emptyView.setTranslationY(((r1.getMeasuredHeight() - this.containerView.getMeasuredHeight()) - dp) / 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLayout() {
        if (this.listView.getChildCount() <= 0) {
            RecyclerListView recyclerListView = this.listView;
            int paddingTop = recyclerListView.getPaddingTop();
            this.scrollOffsetY = paddingTop;
            recyclerListView.setTopGlowOffset(paddingTop);
            this.containerView.invalidate();
            return;
        }
        View childAt = this.listView.getChildAt(0);
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
        int top = childAt.getTop();
        int dp = AndroidUtilities.dp(7.0f);
        if (top < AndroidUtilities.dp(7.0f) || holder == null || holder.getAdapterPosition() != 0) {
            top = dp;
        }
        boolean z = top <= AndroidUtilities.dp(12.0f);
        if ((z && this.actionBar.getTag() == null) || (!z && this.actionBar.getTag() != null)) {
            this.actionBar.setTag(z ? 1 : null);
            AnimatorSet animatorSet = this.actionBarAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.actionBarAnimation = null;
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.actionBarAnimation = animatorSet2;
            animatorSet2.setDuration(180L);
            AnimatorSet animatorSet3 = this.actionBarAnimation;
            ActionBar actionBar = this.actionBar;
            Property property = View.ALPHA;
            animatorSet3.playTogether(ObjectAnimator.ofFloat(actionBar, (Property<ActionBar, Float>) property, z ? 1.0f : 0.0f), ObjectAnimator.ofFloat(this.actionBarShadow, (Property<View, Float>) property, z ? 1.0f : 0.0f));
            this.actionBarAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.AudioPlayerAlert.21
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    AudioPlayerAlert.this.actionBarAnimation = null;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                }
            });
            this.actionBarAnimation.start();
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.listView.getLayoutParams();
        int dp2 = top + ((layoutParams.topMargin - AndroidUtilities.statusBarHeight) - AndroidUtilities.dp(11.0f));
        if (this.scrollOffsetY != dp2) {
            RecyclerListView recyclerListView2 = this.listView;
            this.scrollOffsetY = dp2;
            recyclerListView2.setTopGlowOffset((dp2 - layoutParams.topMargin) - AndroidUtilities.statusBarHeight);
            this.containerView.invalidate();
        }
        int dp3 = AndroidUtilities.dp(13.0f);
        int i = (this.scrollOffsetY - this.backgroundPaddingTop) - dp3;
        if (this.currentSheetAnimationType == 1) {
            i = (int) (i + this.listView.getTranslationY());
        }
        boolean z2 = (this.backgroundPaddingTop + i < ActionBar.getCurrentActionBarHeight() ? 1.0f - Math.min(1.0f, ((float) ((ActionBar.getCurrentActionBarHeight() - i) - this.backgroundPaddingTop)) / ((float) (dp3 + AndroidUtilities.dp(4.0f)))) : 1.0f) <= 0.5f && ColorUtils.calculateLuminance(getThemedColor(Theme.key_dialogBackground)) > 0.699999988079071d;
        if (z2 != this.wasLight) {
            Window window = getWindow();
            this.wasLight = z2;
            AndroidUtilities.setLightStatusBar(window, z2);
        }
    }

    private void updatePlaybackButton(boolean z) {
        ActionBarMenuSubItem actionBarMenuSubItem;
        int i;
        if (this.playbackSpeedButton == null) {
            return;
        }
        float playbackSpeed = MediaController.getInstance().getPlaybackSpeed(true);
        this.speedIcon.setValue(playbackSpeed, z);
        this.speedSlider.setSpeed(playbackSpeed, z);
        updateColors();
        boolean z2 = !this.slidingSpeed;
        this.slidingSpeed = false;
        for (int i2 = 0; i2 < this.speedItems.length; i2++) {
            if (z2 && equals(playbackSpeed, speeds[i2])) {
                actionBarMenuSubItem = this.speedItems[i2];
                i = Theme.key_featuredStickers_addButtonPressed;
            } else {
                actionBarMenuSubItem = this.speedItems[i2];
                i = Theme.key_actionBarDefaultSubmenuItem;
            }
            actionBarMenuSubItem.setColors(getThemedColor(i), getThemedColor(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProgress(MessageObject messageObject) {
        updateProgress(messageObject, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0034, code lost:
    
        if (org.telegram.messenger.MediaController.getInstance().isMessagePaused() == false) goto L15;
     */
    /* JADX WARN: Removed duplicated region for block: B:21:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0095  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0042  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateProgress(MessageObject messageObject, boolean z) {
        boolean z2;
        float f;
        int i;
        SeekBarView seekBarView = this.seekBarView;
        if (seekBarView != null) {
            if (seekBarView.isDragging()) {
                double duration = messageObject.getDuration();
                double progress = this.seekBarView.getProgress();
                Double.isNaN(progress);
                i = (int) (duration * progress);
            } else {
                if (this.rewindingProgress >= 0.0f) {
                    int i2 = this.rewindingState;
                    z2 = true;
                    if (i2 != -1) {
                        if (i2 == 1) {
                        }
                    }
                    this.seekBarView.setProgress(!z2 ? this.rewindingProgress : messageObject.audioProgress, z);
                    if (!this.currentAudioFinishedLoading) {
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        if (Math.abs(elapsedRealtime - this.lastBufferedPositionCheck) >= 500) {
                            f = MediaController.getInstance().isStreamingCurrentAudio() ? FileLoader.getInstance(this.currentAccount).getBufferedProgressFromPosition(messageObject.audioProgress, this.currentFile) : 1.0f;
                            this.lastBufferedPositionCheck = elapsedRealtime;
                        } else {
                            f = -1.0f;
                        }
                    }
                    if (f != -1.0f) {
                        this.seekBarBufferSpring.getSpring().setFinalPosition(f * 1000.0f);
                        this.seekBarBufferSpring.start();
                    }
                    if (z2) {
                        i = messageObject.audioProgressSec;
                    } else {
                        double duration2 = messageObject.getDuration();
                        double progress2 = this.seekBarView.getProgress();
                        Double.isNaN(progress2);
                        i = (int) (duration2 * progress2);
                        messageObject.audioProgressSec = i;
                    }
                }
                z2 = false;
                this.seekBarView.setProgress(!z2 ? this.rewindingProgress : messageObject.audioProgress, z);
                if (!this.currentAudioFinishedLoading) {
                }
                if (f != -1.0f) {
                }
                if (z2) {
                }
            }
            if (this.lastTime != i) {
                this.lastTime = i;
                this.timeTextView.setText(AndroidUtilities.formatShortDuration(i));
            }
            this.seekBarView.updateTimestamps(messageObject, null);
        }
    }

    private void updateRepeatButton() {
        ActionBarMenuItem actionBarMenuItem;
        int i;
        ActionBarMenuItem actionBarMenuItem2;
        int i2;
        int i3 = SharedConfig.repeatMode;
        if (i3 == 0 || i3 == 1) {
            if (SharedConfig.shuffleMusic) {
                actionBarMenuItem = this.repeatButton;
                i = i3 == 0 ? R.drawable.player_new_shuffle : R.drawable.player_new_repeat_shuffle;
            } else if (SharedConfig.playOrderReversed) {
                actionBarMenuItem = this.repeatButton;
                i = i3 == 0 ? R.drawable.player_new_order : R.drawable.player_new_repeat_reverse;
            } else {
                actionBarMenuItem = this.repeatButton;
                i = R.drawable.player_new_repeatall;
            }
            actionBarMenuItem.setIcon(i);
            if (i3 != 0 || SharedConfig.shuffleMusic || SharedConfig.playOrderReversed) {
                ActionBarMenuItem actionBarMenuItem3 = this.repeatButton;
                int i4 = Theme.key_player_buttonActive;
                actionBarMenuItem3.setTag(Integer.valueOf(i4));
                this.repeatButton.setIconColor(getThemedColor(i4));
                Theme.setSelectorDrawableColor(this.repeatButton.getBackground(), 436207615 & getThemedColor(i4), true);
                if (i3 != 0) {
                    actionBarMenuItem2 = this.repeatButton;
                    i2 = R.string.AccDescrRepeatList;
                } else if (SharedConfig.shuffleMusic) {
                    actionBarMenuItem2 = this.repeatButton;
                    i2 = R.string.ShuffleList;
                } else {
                    actionBarMenuItem2 = this.repeatButton;
                    i2 = R.string.ReverseOrder;
                }
            } else {
                ActionBarMenuItem actionBarMenuItem4 = this.repeatButton;
                int i5 = Theme.key_player_button;
                actionBarMenuItem4.setTag(Integer.valueOf(i5));
                this.repeatButton.setIconColor(getThemedColor(i5));
                Theme.setSelectorDrawableColor(this.repeatButton.getBackground(), getThemedColor(Theme.key_listSelector), true);
                actionBarMenuItem2 = this.repeatButton;
                i2 = R.string.AccDescrRepeatOff;
            }
        } else {
            if (i3 != 2) {
                return;
            }
            this.repeatButton.setIcon(R.drawable.player_new_repeatone);
            ActionBarMenuItem actionBarMenuItem5 = this.repeatButton;
            int i6 = Theme.key_player_buttonActive;
            actionBarMenuItem5.setTag(Integer.valueOf(i6));
            this.repeatButton.setIconColor(getThemedColor(i6));
            Theme.setSelectorDrawableColor(this.repeatButton.getBackground(), 436207615 & getThemedColor(i6), true);
            actionBarMenuItem2 = this.repeatButton;
            i2 = R.string.AccDescrRepeatOne;
        }
        actionBarMenuItem2.setContentDescription(LocaleController.getString(i2));
    }

    private void updateSubMenu() {
        setMenuItemChecked(this.shuffleListItem, SharedConfig.shuffleMusic);
        setMenuItemChecked(this.reverseOrderItem, SharedConfig.playOrderReversed);
        setMenuItemChecked(this.repeatListItem, SharedConfig.repeatMode == 1);
        setMenuItemChecked(this.repeatSongItem, SharedConfig.repeatMode == 2);
    }

    private void updateTitle(boolean z) {
        ActionBarMenuItem actionBarMenuItem;
        float f;
        ImageView imageView;
        int i;
        ActionBarMenuItem actionBarMenuItem2;
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if ((playingMessageObject == null && z) || (playingMessageObject != null && !playingMessageObject.isMusic())) {
            dismiss();
            return;
        }
        if (playingMessageObject == null) {
            this.lastMessageObject = null;
            return;
        }
        int i2 = 0;
        boolean z2 = playingMessageObject == this.lastMessageObject;
        this.lastMessageObject = playingMessageObject;
        if (playingMessageObject.eventId != 0 || playingMessageObject.getId() <= -2000000000) {
            this.optionsButton.setVisibility(4);
        } else {
            this.optionsButton.setVisibility(0);
        }
        long dialogId = playingMessageObject.getDialogId();
        if ((dialogId < 0 && MessagesController.getInstance(this.currentAccount).isChatNoForwards(-dialogId)) || MessagesController.getInstance(this.currentAccount).isChatNoForwards(playingMessageObject.getChatId()) || playingMessageObject.messageOwner.noforwards) {
            this.optionsButton.hideSubItem(1);
            this.optionsButton.hideSubItem(2);
            this.optionsButton.hideSubItem(5);
            actionBarMenuItem = this.optionsButton;
            f = 16.0f;
        } else {
            this.optionsButton.showSubItem(1);
            this.optionsButton.showSubItem(2);
            this.optionsButton.showSubItem(5);
            actionBarMenuItem = this.optionsButton;
            f = 157.0f;
        }
        actionBarMenuItem.setAdditionalYOffset(-AndroidUtilities.dp(f));
        checkIfMusicDownloaded(playingMessageObject);
        boolean z3 = !z2;
        updateProgress(playingMessageObject, z3);
        updateCover(playingMessageObject, z3);
        if (MediaController.getInstance().isMessagePaused()) {
            this.playPauseDrawable.setPause(false);
            imageView = this.playButton;
            i = R.string.AccActionPlay;
        } else {
            this.playPauseDrawable.setPause(true);
            imageView = this.playButton;
            i = R.string.AccActionPause;
        }
        imageView.setContentDescription(LocaleController.getString(i));
        String musicTitle = playingMessageObject.getMusicTitle();
        String musicAuthor = playingMessageObject.getMusicAuthor();
        this.titleTextView.setText(musicTitle);
        this.authorTextView.setText(musicAuthor);
        int duration = (int) playingMessageObject.getDuration();
        this.lastDuration = duration;
        TextView textView = this.durationTextView;
        if (textView != null) {
            textView.setText(duration != 0 ? AndroidUtilities.formatShortDuration(duration) : "-:--");
        }
        if (duration > 600) {
            actionBarMenuItem2 = this.playbackSpeedButton;
        } else {
            actionBarMenuItem2 = this.playbackSpeedButton;
            i2 = 8;
        }
        actionBarMenuItem2.setVisibility(i2);
        if (z2) {
            return;
        }
        preloadNeighboringThumbs();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BottomSheet
    public boolean canDismissWithSwipe() {
        return false;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        AudioPlayerCell audioPlayerCell;
        MessageObject messageObject;
        AudioPlayerCell audioPlayerCell2;
        MessageObject messageObject2;
        MessageObject playingMessageObject;
        if (i == NotificationCenter.messagePlayingDidStart || i == NotificationCenter.messagePlayingPlayStateChanged || i == NotificationCenter.messagePlayingDidReset) {
            int i3 = NotificationCenter.messagePlayingDidReset;
            updateTitle(i == i3 && ((Boolean) objArr[1]).booleanValue());
            if (i != i3 && i != NotificationCenter.messagePlayingPlayStateChanged) {
                if (((MessageObject) objArr[0]).eventId != 0) {
                    return;
                }
                int childCount = this.listView.getChildCount();
                for (int i4 = 0; i4 < childCount; i4++) {
                    View childAt = this.listView.getChildAt(i4);
                    if ((childAt instanceof AudioPlayerCell) && (messageObject2 = (audioPlayerCell2 = (AudioPlayerCell) childAt).getMessageObject()) != null && (messageObject2.isVoice() || messageObject2.isMusic())) {
                        audioPlayerCell2.updateButtonState(false, true);
                    }
                }
                return;
            }
            int childCount2 = this.listView.getChildCount();
            for (int i5 = 0; i5 < childCount2; i5++) {
                View childAt2 = this.listView.getChildAt(i5);
                if ((childAt2 instanceof AudioPlayerCell) && (messageObject = (audioPlayerCell = (AudioPlayerCell) childAt2).getMessageObject()) != null && (messageObject.isVoice() || messageObject.isMusic())) {
                    audioPlayerCell.updateButtonState(false, true);
                }
            }
            if (i != NotificationCenter.messagePlayingPlayStateChanged || MediaController.getInstance().getPlayingMessageObject() == null) {
                return;
            }
            if (MediaController.getInstance().isMessagePaused()) {
                startForwardRewindingSeek();
                return;
            }
            if (this.rewindingState != 1 || this.rewindingProgress == -1.0f) {
                return;
            }
            AndroidUtilities.cancelRunOnUIThread(this.forwardSeek);
            this.lastUpdateRewindingPlayerTime = 0L;
            this.forwardSeek.run();
            this.rewindingProgress = -1.0f;
            return;
        }
        if (i == NotificationCenter.messagePlayingProgressDidChanged) {
            MessageObject playingMessageObject2 = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject2 == null || !playingMessageObject2.isMusic()) {
                return;
            }
            updateProgress(playingMessageObject2);
            return;
        }
        if (i == NotificationCenter.messagePlayingSpeedChanged) {
            updatePlaybackButton(true);
            return;
        }
        if (i == NotificationCenter.musicDidLoad) {
            this.playlist = MediaController.getInstance().getPlaylist();
            this.listAdapter.notifyDataSetChanged();
            return;
        }
        if (i == NotificationCenter.moreMusicDidLoad) {
            this.playlist = MediaController.getInstance().getPlaylist();
            this.listAdapter.notifyDataSetChanged();
            if (SharedConfig.playOrderReversed) {
                this.listView.stopScroll();
                int intValue = ((Integer) objArr[0]).intValue();
                this.layoutManager.findFirstVisibleItemPosition();
                int findLastVisibleItemPosition = this.layoutManager.findLastVisibleItemPosition();
                if (findLastVisibleItemPosition != -1) {
                    View findViewByPosition = this.layoutManager.findViewByPosition(findLastVisibleItemPosition);
                    this.layoutManager.scrollToPositionWithOffset(findLastVisibleItemPosition + intValue, findViewByPosition != null ? findViewByPosition.getTop() : 0);
                    return;
                }
                return;
            }
            return;
        }
        if (i == NotificationCenter.fileLoaded) {
            if (((String) objArr[0]).equals(this.currentFile)) {
                updateTitle(false);
                this.currentAudioFinishedLoading = true;
                return;
            }
            return;
        }
        if (i == NotificationCenter.fileLoadProgressChanged && ((String) objArr[0]).equals(this.currentFile) && (playingMessageObject = MediaController.getInstance().getPlayingMessageObject()) != null) {
            if (!this.currentAudioFinishedLoading) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                if (Math.abs(elapsedRealtime - this.lastBufferedPositionCheck) >= 500) {
                    r11 = MediaController.getInstance().isStreamingCurrentAudio() ? FileLoader.getInstance(this.currentAccount).getBufferedProgressFromPosition(playingMessageObject.audioProgress, this.currentFile) : 1.0f;
                    this.lastBufferedPositionCheck = elapsedRealtime;
                } else {
                    r11 = -1.0f;
                }
            }
            if (r11 != -1.0f) {
                this.seekBarBufferSpring.getSpring().setFinalPosition(r11 * 1000.0f);
                this.seekBarBufferSpring.start();
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void dismiss() {
        super.dismiss();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStart);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoadProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.musicDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.moreMusicDidLoad);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.messagePlayingSpeedChanged);
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public int getContainerViewHeight() {
        if (this.playerLayout == null) {
            return 0;
        }
        if (this.playlist.size() <= 1) {
            return this.playerLayout.getMeasuredHeight() + this.backgroundPaddingTop;
        }
        int dp = AndroidUtilities.dp(13.0f);
        int i = (this.scrollOffsetY - this.backgroundPaddingTop) - dp;
        if (this.currentSheetAnimationType == 1) {
            i = (int) (i + this.listView.getTranslationY());
        }
        if (this.backgroundPaddingTop + i < ActionBar.getCurrentActionBarHeight()) {
            float dp2 = dp + AndroidUtilities.dp(4.0f);
            i -= (int) ((ActionBar.getCurrentActionBarHeight() - dp2) * Math.min(1.0f, ((ActionBar.getCurrentActionBarHeight() - i) - this.backgroundPaddingTop) / dp2));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            i += AndroidUtilities.statusBarHeight;
        }
        return this.container.getMeasuredHeight() - i;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.Components.AudioPlayerAlert$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                AudioPlayerAlert.this.lambda$getThemeDescriptions$13();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_dialogBackground;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        ActionBar actionBar2 = this.actionBar;
        int i3 = ThemeDescription.FLAG_AB_ITEMSCOLOR;
        int i4 = Theme.key_player_actionBarTitle;
        arrayList.add(new ThemeDescription(actionBar2, i3, null, null, null, themeDescriptionDelegate, i4));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBTITLECOLOR, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_player_actionBarSelector));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, i4));
        ActionBar actionBar3 = this.actionBar;
        int i5 = ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER;
        int i6 = Theme.key_player_time;
        arrayList.add(new ThemeDescription(actionBar3, i5, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, Theme.key_chat_inLoader));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, Theme.key_chat_outLoader));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, Theme.key_chat_inLoaderSelected));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, Theme.key_chat_inMediaIcon));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, Theme.key_chat_inMediaIconSelected));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, Theme.key_chat_inAudioSelectedProgress));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, Theme.key_chat_inAudioProgress));
        arrayList.add(new ThemeDescription(this.containerView, 0, null, null, new Drawable[]{this.shadowDrawable}, null, i2));
        LineProgressView lineProgressView = this.progressView;
        int i7 = Theme.key_player_progressBackground;
        arrayList.add(new ThemeDescription(lineProgressView, 0, null, null, null, null, i7));
        LineProgressView lineProgressView2 = this.progressView;
        int i8 = Theme.key_player_progress;
        arrayList.add(new ThemeDescription(lineProgressView2, 0, null, null, null, null, i8));
        arrayList.add(new ThemeDescription(this.seekBarView, 0, null, null, null, null, i7));
        arrayList.add(new ThemeDescription(this.seekBarView, 0, null, null, null, null, Theme.key_player_progressCachedBackground));
        arrayList.add(new ThemeDescription(this.seekBarView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, i8));
        arrayList.add(new ThemeDescription(this.playbackSpeedButton, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_inappPlayerPlayPause));
        arrayList.add(new ThemeDescription(this.playbackSpeedButton, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_inappPlayerClose));
        ActionBarMenuItem actionBarMenuItem = this.repeatButton;
        int i9 = Theme.key_player_button;
        arrayList.add(new ThemeDescription(actionBarMenuItem, 0, null, null, null, themeDescriptionDelegate, i9));
        arrayList.add(new ThemeDescription(this.repeatButton, 0, null, null, null, themeDescriptionDelegate, Theme.key_player_buttonActive));
        ActionBarMenuItem actionBarMenuItem2 = this.repeatButton;
        int i10 = Theme.key_listSelector;
        arrayList.add(new ThemeDescription(actionBarMenuItem2, 0, null, null, null, themeDescriptionDelegate, i10));
        ActionBarMenuItem actionBarMenuItem3 = this.repeatButton;
        int i11 = Theme.key_actionBarDefaultSubmenuItem;
        arrayList.add(new ThemeDescription(actionBarMenuItem3, 0, null, null, null, themeDescriptionDelegate, i11));
        ActionBarMenuItem actionBarMenuItem4 = this.repeatButton;
        int i12 = Theme.key_actionBarDefaultSubmenuBackground;
        arrayList.add(new ThemeDescription(actionBarMenuItem4, 0, null, null, null, themeDescriptionDelegate, i12));
        arrayList.add(new ThemeDescription(this.optionsButton, 0, null, null, null, themeDescriptionDelegate, i9));
        arrayList.add(new ThemeDescription(this.optionsButton, 0, null, null, null, themeDescriptionDelegate, i10));
        arrayList.add(new ThemeDescription(this.optionsButton, 0, null, null, null, themeDescriptionDelegate, i11));
        arrayList.add(new ThemeDescription(this.optionsButton, 0, null, null, null, themeDescriptionDelegate, i12));
        RLottieImageView rLottieImageView = this.prevButton;
        arrayList.add(new ThemeDescription(rLottieImageView, 0, (Class[]) null, new RLottieDrawable[]{rLottieImageView.getAnimatedDrawable()}, "Triangle 3", i9));
        RLottieImageView rLottieImageView2 = this.prevButton;
        arrayList.add(new ThemeDescription(rLottieImageView2, 0, (Class[]) null, new RLottieDrawable[]{rLottieImageView2.getAnimatedDrawable()}, "Triangle 4", i9));
        RLottieImageView rLottieImageView3 = this.prevButton;
        arrayList.add(new ThemeDescription(rLottieImageView3, 0, (Class[]) null, new RLottieDrawable[]{rLottieImageView3.getAnimatedDrawable()}, "Rectangle 4", i9));
        arrayList.add(new ThemeDescription(this.prevButton, ThemeDescription.FLAG_IMAGECOLOR | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, null, null, null, null, i10));
        arrayList.add(new ThemeDescription(this.playButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, i9));
        arrayList.add(new ThemeDescription(this.playButton, ThemeDescription.FLAG_IMAGECOLOR | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, null, null, null, null, i10));
        RLottieImageView rLottieImageView4 = this.nextButton;
        arrayList.add(new ThemeDescription(rLottieImageView4, 0, (Class[]) null, new RLottieDrawable[]{rLottieImageView4.getAnimatedDrawable()}, "Triangle 3", i9));
        RLottieImageView rLottieImageView5 = this.nextButton;
        arrayList.add(new ThemeDescription(rLottieImageView5, 0, (Class[]) null, new RLottieDrawable[]{rLottieImageView5.getAnimatedDrawable()}, "Triangle 4", i9));
        RLottieImageView rLottieImageView6 = this.nextButton;
        arrayList.add(new ThemeDescription(rLottieImageView6, 0, (Class[]) null, new RLottieDrawable[]{rLottieImageView6.getAnimatedDrawable()}, "Rectangle 4", i9));
        arrayList.add(new ThemeDescription(this.nextButton, ThemeDescription.FLAG_IMAGECOLOR | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, null, null, null, null, i10));
        arrayList.add(new ThemeDescription(this.playerLayout, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_player_background));
        arrayList.add(new ThemeDescription(this.playerShadow, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_dialogShadowLine));
        arrayList.add(new ThemeDescription(this.emptyImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_dialogEmptyImage));
        TextView textView = this.emptyTitleTextView;
        int i13 = ThemeDescription.FLAG_IMAGECOLOR;
        int i14 = Theme.key_dialogEmptyText;
        arrayList.add(new ThemeDescription(textView, i13, null, null, null, null, i14));
        arrayList.add(new ThemeDescription(this.emptySubtitleTextView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, i14));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_dialogScrollGlow));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, i10));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.progressView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder));
        arrayList.add(new ThemeDescription(this.progressView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle));
        arrayList.add(new ThemeDescription(this.durationTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.timeTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.titleTextView.getTextView(), ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.titleTextView.getNextTextView(), ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.authorTextView.getTextView(), ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.authorTextView.getNextTextView(), ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.containerView, 0, null, null, null, null, Theme.key_sheet_scrollUp));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onBackPressed() {
        ActionBar actionBar = this.actionBar;
        if (actionBar != null && actionBar.isSearchFieldVisible()) {
            this.actionBar.closeSearchField();
        } else if (this.blurredView.getTag() != null) {
            showAlbumCover(false, true);
        } else {
            super.onBackPressed();
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean onCustomLayout(View view, int i, int i2, int i3, int i4) {
        int i5 = i3 - i;
        int i6 = i4 - i2;
        FrameLayout frameLayout = this.blurredView;
        if (view != frameLayout) {
            return false;
        }
        frameLayout.layout(i, 0, i5 + i, i6);
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public boolean onCustomMeasure(View view, int i, int i2) {
        FrameLayout frameLayout = this.blurredView;
        if (view != frameLayout) {
            return false;
        }
        frameLayout.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(i2, 1073741824));
        return true;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String str, long j, long j2) {
        this.progressView.setProgress(Math.min(1.0f, ((float) j) / ((float) j2)), true);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String str, long j, long j2, boolean z) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String str) {
    }

    public void updateColors() {
        if (this.playbackSpeedButton != null) {
            int themedColor = getThemedColor(!equals(MediaController.getInstance().getPlaybackSpeed(true), 1.0f) ? Theme.key_featuredStickers_addButtonPressed : Theme.key_inappPlayerClose);
            SpeedIconDrawable speedIconDrawable = this.speedIcon;
            if (speedIconDrawable != null) {
                speedIconDrawable.setColor(themedColor);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                this.playbackSpeedButton.setBackground(Theme.createSelectorDrawable(themedColor & 436207615, 1, AndroidUtilities.dp(14.0f)));
            }
        }
    }
}
