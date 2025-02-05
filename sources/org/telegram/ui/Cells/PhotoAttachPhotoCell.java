package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.Components.spoilers.SpoilerEffect2;
import org.telegram.ui.PhotoViewer;

/* loaded from: classes4.dex */
public class PhotoAttachPhotoCell extends FrameLayout {
    private static Rect rect = new Rect();
    private AnimatorSet animator;
    private AnimatorSet animatorSet;
    private Paint backgroundPaint;
    private CheckBox2 checkBox;
    private FrameLayout checkFrame;
    private FrameLayout container;
    private Float crossfadeDuration;
    private PhotoAttachPhotoCellDelegate delegate;
    private boolean hasSpoiler;
    private BackupImageView imageView;
    private float imageViewCrossfadeProgress;
    private Bitmap imageViewCrossfadeSnapshot;
    private boolean isLast;
    private boolean isVertical;
    private int itemSize;
    private boolean itemSizeChanged;
    private SpannableString lock;
    private Path path;
    private MediaController.PhotoEntry photoEntry;
    private boolean pressed;
    private final Theme.ResourcesProvider resourcesProvider;
    private MediaController.SearchImage searchEntry;
    private SpoilerEffect spoilerEffect;
    private SpoilerEffect2 spoilerEffect2;
    private float spoilerMaxRadius;
    private float spoilerRevealProgress;
    private float spoilerRevealX;
    private float spoilerRevealY;
    private SpannableString star;
    private long stars;
    private boolean starsSelectedMultiple;
    private FrameLayout videoInfoContainer;
    private TextView videoTextView;
    private boolean zoomOnSelect;

    public interface PhotoAttachPhotoCellDelegate {
        void onCheckClick(PhotoAttachPhotoCell photoAttachPhotoCell);
    }

    public PhotoAttachPhotoCell(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.zoomOnSelect = true;
        this.backgroundPaint = new Paint();
        this.spoilerEffect = new SpoilerEffect();
        this.path = new Path();
        this.imageViewCrossfadeProgress = 1.0f;
        this.resourcesProvider = resourcesProvider;
        setWillNotDraw(false);
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.Cells.PhotoAttachPhotoCell.1
            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                if (PhotoAttachPhotoCell.this.spoilerEffect2 == null || view != PhotoAttachPhotoCell.this.imageView) {
                    return super.drawChild(canvas, view, j);
                }
                boolean drawChild = super.drawChild(canvas, view, j);
                if (PhotoAttachPhotoCell.this.hasSpoiler && PhotoAttachPhotoCell.this.spoilerRevealProgress != 1.0f && (PhotoAttachPhotoCell.this.photoEntry == null || !PhotoAttachPhotoCell.this.photoEntry.isAttachSpoilerRevealed)) {
                    if (PhotoAttachPhotoCell.this.spoilerRevealProgress != 0.0f) {
                        canvas.save();
                        PhotoAttachPhotoCell.this.path.rewind();
                        PhotoAttachPhotoCell.this.path.addCircle(PhotoAttachPhotoCell.this.spoilerRevealX, PhotoAttachPhotoCell.this.spoilerRevealY, PhotoAttachPhotoCell.this.spoilerMaxRadius * PhotoAttachPhotoCell.this.spoilerRevealProgress, Path.Direction.CW);
                        canvas.clipPath(PhotoAttachPhotoCell.this.path, Region.Op.DIFFERENCE);
                    }
                    PhotoAttachPhotoCell.this.spoilerEffect2.draw(canvas, PhotoAttachPhotoCell.this.container, PhotoAttachPhotoCell.this.imageView.getMeasuredWidth(), PhotoAttachPhotoCell.this.imageView.getMeasuredHeight());
                    if (PhotoAttachPhotoCell.this.photoEntry != null && PhotoAttachPhotoCell.this.photoEntry.starsAmount > 0) {
                        PhotoAttachPhotoCell.this.imageView.drawBlurredText(canvas, 1.0f);
                    }
                    if (PhotoAttachPhotoCell.this.spoilerRevealProgress != 0.0f) {
                        canvas.restore();
                    }
                }
                return drawChild;
            }
        };
        this.container = frameLayout;
        addView(frameLayout, LayoutHelper.createFrame(80, 80.0f));
        this.spoilerEffect.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(-1) * 0.325f)));
        BackupImageView backupImageView = new BackupImageView(context) { // from class: org.telegram.ui.Cells.PhotoAttachPhotoCell.2
            private Paint crossfadePaint = new Paint(1);
            private long lastUpdate;

            @Override // org.telegram.ui.Components.BackupImageView, android.view.View
            protected void onDraw(Canvas canvas) {
                AnimatedEmojiDrawable animatedEmojiDrawable = this.animatedEmojiDrawable;
                ImageReceiver imageReceiver = animatedEmojiDrawable != null ? animatedEmojiDrawable.getImageReceiver() : this.imageReceiver;
                if (imageReceiver == null) {
                    return;
                }
                if (this.width == -1 || this.height == -1) {
                    imageReceiver.setImageCoords(0.0f, 0.0f, getWidth(), getHeight());
                    this.blurImageReceiver.setImageCoords(0.0f, 0.0f, getWidth(), getHeight());
                } else {
                    float width = (getWidth() - this.width) / 2;
                    int height = getHeight();
                    imageReceiver.setImageCoords(width, (height - r4) / 2, this.width, this.height);
                    ImageReceiver imageReceiver2 = this.blurImageReceiver;
                    float width2 = (getWidth() - this.width) / 2;
                    int height2 = getHeight();
                    imageReceiver2.setImageCoords(width2, (height2 - r5) / 2, this.width, this.height);
                }
                imageReceiver.draw(canvas);
                if (PhotoAttachPhotoCell.this.hasSpoiler && PhotoAttachPhotoCell.this.spoilerRevealProgress != 1.0f && (PhotoAttachPhotoCell.this.photoEntry == null || !PhotoAttachPhotoCell.this.photoEntry.isAttachSpoilerRevealed)) {
                    if (PhotoAttachPhotoCell.this.spoilerRevealProgress != 0.0f) {
                        canvas.save();
                        PhotoAttachPhotoCell.this.path.rewind();
                        PhotoAttachPhotoCell.this.path.addCircle(PhotoAttachPhotoCell.this.spoilerRevealX, PhotoAttachPhotoCell.this.spoilerRevealY, PhotoAttachPhotoCell.this.spoilerMaxRadius * PhotoAttachPhotoCell.this.spoilerRevealProgress, Path.Direction.CW);
                        canvas.clipPath(PhotoAttachPhotoCell.this.path, Region.Op.DIFFERENCE);
                    }
                    this.blurImageReceiver.draw(canvas);
                    if (PhotoAttachPhotoCell.this.spoilerEffect2 == null) {
                        PhotoAttachPhotoCell.this.spoilerEffect.setBounds(0, 0, getWidth(), getHeight());
                        PhotoAttachPhotoCell.this.spoilerEffect.draw(canvas);
                    }
                    invalidate();
                    if (PhotoAttachPhotoCell.this.spoilerRevealProgress != 0.0f) {
                        canvas.restore();
                    }
                }
                if (PhotoAttachPhotoCell.this.imageViewCrossfadeProgress == 1.0f || PhotoAttachPhotoCell.this.imageViewCrossfadeSnapshot == null) {
                    if (PhotoAttachPhotoCell.this.imageViewCrossfadeProgress != 1.0f || PhotoAttachPhotoCell.this.imageViewCrossfadeSnapshot == null) {
                        return;
                    }
                    PhotoAttachPhotoCell.this.imageViewCrossfadeSnapshot.recycle();
                    PhotoAttachPhotoCell.this.imageViewCrossfadeSnapshot = null;
                    PhotoAttachPhotoCell.this.crossfadeDuration = null;
                    invalidate();
                    return;
                }
                this.crossfadePaint.setAlpha((int) (CubicBezierInterpolator.DEFAULT.getInterpolation(1.0f - PhotoAttachPhotoCell.this.imageViewCrossfadeProgress) * 255.0f));
                canvas.drawBitmap(PhotoAttachPhotoCell.this.imageViewCrossfadeSnapshot, 0.0f, 0.0f, this.crossfadePaint);
                long min = Math.min(16L, System.currentTimeMillis() - this.lastUpdate);
                float floatValue = PhotoAttachPhotoCell.this.crossfadeDuration == null ? 250.0f : PhotoAttachPhotoCell.this.crossfadeDuration.floatValue();
                PhotoAttachPhotoCell photoAttachPhotoCell = PhotoAttachPhotoCell.this;
                photoAttachPhotoCell.imageViewCrossfadeProgress = Math.min(1.0f, photoAttachPhotoCell.imageViewCrossfadeProgress + (min / floatValue));
                this.lastUpdate = System.currentTimeMillis();
                invalidate();
                if (PhotoAttachPhotoCell.this.spoilerEffect2 != null) {
                    PhotoAttachPhotoCell.this.container.invalidate();
                }
            }

            @Override // android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(i, i2);
                PhotoAttachPhotoCell photoAttachPhotoCell = PhotoAttachPhotoCell.this;
                photoAttachPhotoCell.updateSpoilers2(photoAttachPhotoCell.photoEntry != null && PhotoAttachPhotoCell.this.photoEntry.hasSpoiler);
            }
        };
        this.imageView = backupImageView;
        backupImageView.setBlurAllowed(true);
        this.container.addView(this.imageView, LayoutHelper.createFrame(-1, -1.0f));
        FrameLayout frameLayout2 = new FrameLayout(context) { // from class: org.telegram.ui.Cells.PhotoAttachPhotoCell.3
            private RectF rect = new RectF();

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                this.rect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), Theme.chat_timeBackgroundPaint);
            }
        };
        this.videoInfoContainer = frameLayout2;
        frameLayout2.setWillNotDraw(false);
        this.videoInfoContainer.setPadding(AndroidUtilities.dp(5.0f), 0, AndroidUtilities.dp(5.0f), 0);
        this.container.addView(this.videoInfoContainer, LayoutHelper.createFrame(-2, 17.0f, 83, 4.0f, 0.0f, 0.0f, 4.0f));
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.play_mini_video);
        this.videoInfoContainer.addView(imageView, LayoutHelper.createFrame(-2, -2, 19));
        TextView textView = new TextView(context);
        this.videoTextView = textView;
        textView.setTextColor(-1);
        this.videoTextView.setTypeface(AndroidUtilities.bold());
        this.videoTextView.setTextSize(1, 12.0f);
        this.videoTextView.setImportantForAccessibility(2);
        this.videoInfoContainer.addView(this.videoTextView, LayoutHelper.createFrame(-2, -2.0f, 19, 13.0f, -0.7f, 0.0f, 0.0f));
        CheckBox2 checkBox2 = new CheckBox2(context, 24, resourcesProvider);
        this.checkBox = checkBox2;
        checkBox2.setDrawBackgroundAsArc(7);
        this.checkBox.setColor(Theme.key_chat_attachCheckBoxBackground, Theme.key_chat_attachPhotoBackground, Theme.key_chat_attachCheckBoxCheck);
        addView(this.checkBox, LayoutHelper.createFrame(26, 26.0f, 51, 52.0f, 4.0f, 0.0f, 0.0f));
        this.checkBox.setVisibility(0);
        setFocusable(true);
        FrameLayout frameLayout3 = new FrameLayout(context);
        this.checkFrame = frameLayout3;
        addView(frameLayout3, LayoutHelper.createFrame(42, 42.0f, 51, 38.0f, 0.0f, 0.0f, 0.0f));
        this.itemSize = AndroidUtilities.dp(80.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSpoilers2(boolean z) {
        BackupImageView backupImageView;
        SpoilerEffect2 spoilerEffect2;
        if (this.container == null || (backupImageView = this.imageView) == null || backupImageView.getMeasuredHeight() <= 0 || this.imageView.getMeasuredWidth() <= 0) {
            return;
        }
        if (!z || !SpoilerEffect2.supports()) {
            SpoilerEffect2 spoilerEffect22 = this.spoilerEffect2;
            if (spoilerEffect22 == null) {
                return;
            }
            spoilerEffect22.detach(this);
            spoilerEffect2 = null;
        } else if (this.spoilerEffect2 != null) {
            return;
        } else {
            spoilerEffect2 = SpoilerEffect2.getInstance(this.container);
        }
        this.spoilerEffect2 = spoilerEffect2;
    }

    public void callDelegate() {
        this.delegate.onCheckClick(this);
    }

    @Override // android.view.View
    public void clearAnimation() {
        super.clearAnimation();
        AnimatorSet animatorSet = this.animator;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.animator = null;
            this.container.setScaleX(this.checkBox.isChecked() ? 0.787f : 1.0f);
            this.container.setScaleY(this.checkBox.isChecked() ? 0.787f : 1.0f);
        }
    }

    public CheckBox2 getCheckBox() {
        return this.checkBox;
    }

    public FrameLayout getCheckFrame() {
        return this.checkFrame;
    }

    public BackupImageView getImageView() {
        return this.imageView;
    }

    public MediaController.PhotoEntry getPhotoEntry() {
        return this.photoEntry;
    }

    public float getScale() {
        return this.container.getScaleX();
    }

    protected int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    public View getVideoInfoContainer() {
        return this.videoInfoContainer;
    }

    public boolean isChecked() {
        return this.checkBox.isChecked();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        SpoilerEffect2 spoilerEffect2 = this.spoilerEffect2;
        if (spoilerEffect2 != null) {
            if (spoilerEffect2.destroyed) {
                this.spoilerEffect2 = SpoilerEffect2.getInstance(this);
            } else {
                spoilerEffect2.attach(this);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SpoilerEffect2 spoilerEffect2 = this.spoilerEffect2;
        if (spoilerEffect2 != null) {
            spoilerEffect2.detach(this);
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        MediaController.PhotoEntry photoEntry;
        MediaController.SearchImage searchImage;
        if (!this.checkBox.isChecked() && this.container.getScaleX() == 1.0f && this.imageView.getImageReceiver().hasNotThumb() && this.imageView.getImageReceiver().getCurrentAlpha() == 1.0f && (((photoEntry = this.photoEntry) == null || !PhotoViewer.isShowingImage(photoEntry.path)) && ((searchImage = this.searchEntry) == null || !PhotoViewer.isShowingImage(searchImage.getPathToAttach())))) {
            return;
        }
        this.backgroundPaint.setColor(getThemedColor(Theme.key_chat_attachPhotoBackground));
        canvas.drawRect(0.0f, 0.0f, this.imageView.getMeasuredWidth(), this.imageView.getMeasuredHeight(), this.backgroundPaint);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        String string;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setEnabled(true);
        StringBuilder sb = new StringBuilder();
        MediaController.PhotoEntry photoEntry = this.photoEntry;
        if (photoEntry == null || !photoEntry.isVideo) {
            string = LocaleController.getString(R.string.AttachPhoto);
        } else {
            string = LocaleController.getString(R.string.AttachVideo) + ", " + LocaleController.formatDuration(this.photoEntry.duration);
        }
        sb.append(string);
        if (this.photoEntry != null) {
            sb.append(". ");
            sb.append(LocaleController.getInstance().getFormatterStats().format(this.photoEntry.dateTaken * 1000));
        }
        accessibilityNodeInfo.setText(sb);
        if (this.checkBox.isChecked()) {
            accessibilityNodeInfo.setSelected(true);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.acc_action_open_photo, LocaleController.getString(R.string.Open)));
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int makeMeasureSpec;
        int dp;
        if (this.itemSizeChanged) {
            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.itemSize, 1073741824);
            dp = this.itemSize + AndroidUtilities.dp(5.0f);
        } else {
            if (this.isVertical) {
                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0f), 1073741824);
                dp = AndroidUtilities.dp((this.isLast ? 0 : 6) + 80);
            } else {
                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((this.isLast ? 0 : 6) + 80), 1073741824);
                dp = AndroidUtilities.dp(80.0f);
            }
        }
        super.onMeasure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(dp, 1073741824));
    }

    /* JADX WARN: Removed duplicated region for block: B:10:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0070  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.checkFrame.getHitRect(rect);
        boolean z = true;
        if (motionEvent.getAction() == 0) {
            if (rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                this.pressed = true;
                invalidate();
                return z ? super.onTouchEvent(motionEvent) : z;
            }
        } else if (this.pressed) {
            if (motionEvent.getAction() == 1) {
                getParent().requestDisallowInterceptTouchEvent(true);
                this.pressed = false;
                playSoundEffect(0);
                sendAccessibilityEvent(1);
                this.delegate.onCheckClick(this);
            } else if (motionEvent.getAction() == 3 || (motionEvent.getAction() == 2 && !rect.contains((int) motionEvent.getX(), (int) motionEvent.getY()))) {
                this.pressed = false;
            }
            invalidate();
        }
        z = false;
        if (z) {
        }
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i, Bundle bundle) {
        if (i == R.id.acc_action_open_photo) {
            View view = (View) getParent();
            view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, getLeft(), (getTop() + getHeight()) - 1, 0));
            view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 1, getLeft(), (getTop() + getHeight()) - 1, 0));
        }
        return super.performAccessibilityAction(i, bundle);
    }

    public void setChecked(int i, final boolean z, boolean z2) {
        this.checkBox.setChecked(i, z, z2);
        if (this.itemSizeChanged) {
            AnimatorSet animatorSet = this.animator;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.animator = null;
            }
            if (!z2) {
                this.container.setScaleX(z ? 0.787f : 1.0f);
                this.container.setScaleY(z ? 0.787f : 1.0f);
                return;
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animator = animatorSet2;
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this.container, (Property<FrameLayout, Float>) View.SCALE_X, z ? 0.787f : 1.0f), ObjectAnimator.ofFloat(this.container, (Property<FrameLayout, Float>) View.SCALE_Y, z ? 0.787f : 1.0f));
            this.animator.setDuration(200L);
            this.animator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.PhotoAttachPhotoCell.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    if (PhotoAttachPhotoCell.this.animator == null || !PhotoAttachPhotoCell.this.animator.equals(animator)) {
                        return;
                    }
                    PhotoAttachPhotoCell.this.animator = null;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (PhotoAttachPhotoCell.this.animator == null || !PhotoAttachPhotoCell.this.animator.equals(animator)) {
                        return;
                    }
                    PhotoAttachPhotoCell.this.animator = null;
                    if (z) {
                        return;
                    }
                    PhotoAttachPhotoCell.this.setBackgroundColor(0);
                }
            });
            this.animator.start();
        }
    }

    public void setDelegate(PhotoAttachPhotoCellDelegate photoAttachPhotoCellDelegate) {
        this.delegate = photoAttachPhotoCellDelegate;
    }

    public void setHasSpoiler(boolean z) {
        setHasSpoiler(z, null);
    }

    public void setHasSpoiler(boolean z, Float f) {
        if (this.hasSpoiler != z) {
            float f2 = 0.0f;
            this.spoilerRevealProgress = 0.0f;
            if (isLaidOut()) {
                Bitmap bitmap = this.imageViewCrossfadeSnapshot;
                this.imageViewCrossfadeSnapshot = AndroidUtilities.snapshotView(this.imageView);
                if (bitmap != null) {
                    bitmap.recycle();
                }
            } else {
                Bitmap bitmap2 = this.imageViewCrossfadeSnapshot;
                if (bitmap2 != null) {
                    bitmap2.recycle();
                    this.imageViewCrossfadeSnapshot = null;
                }
                f2 = 1.0f;
            }
            this.imageViewCrossfadeProgress = f2;
            this.hasSpoiler = z;
            this.crossfadeDuration = f;
            this.imageView.setHasBlur(z);
            this.imageView.invalidate();
            if (z) {
                updateSpoilers2(z);
            }
        }
    }

    public void setIsVertical(boolean z) {
        this.isVertical = z;
    }

    public void setItemSize(int i) {
        this.itemSize = i;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.container.getLayoutParams();
        int i2 = this.itemSize;
        layoutParams.height = i2;
        layoutParams.width = i2;
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.checkFrame.getLayoutParams();
        layoutParams2.gravity = 53;
        layoutParams2.leftMargin = 0;
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.checkBox.getLayoutParams();
        layoutParams3.gravity = 53;
        layoutParams3.leftMargin = 0;
        int dp = AndroidUtilities.dp(5.0f);
        layoutParams3.topMargin = dp;
        layoutParams3.rightMargin = dp;
        this.checkBox.setDrawBackgroundAsArc(6);
        this.itemSizeChanged = true;
    }

    public void setNum(int i) {
        this.checkBox.setNum(i);
    }

    public void setOnCheckClickListener(View.OnClickListener onClickListener) {
        this.checkFrame.setOnClickListener(onClickListener);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00a5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setPhotoEntry(MediaController.PhotoEntry photoEntry, boolean z, boolean z2, boolean z3) {
        BackupImageView backupImageView;
        StringBuilder sb;
        String str;
        boolean z4 = false;
        this.pressed = false;
        this.photoEntry = photoEntry;
        this.isLast = z3;
        if (photoEntry.isVideo) {
            this.imageView.setOrientation(0, true);
            this.videoInfoContainer.setVisibility(0);
            this.videoTextView.setText(AndroidUtilities.formatShortDuration(this.photoEntry.duration));
        } else {
            this.videoInfoContainer.setVisibility(4);
        }
        MediaController.PhotoEntry photoEntry2 = this.photoEntry;
        String str2 = photoEntry2.coverPath;
        if (str2 != null || (str2 = photoEntry2.thumbPath) != null) {
            backupImageView = this.imageView;
        } else {
            if (photoEntry2.path == null) {
                this.imageView.setImageDrawable(Theme.chat_attachEmptyDrawable);
                if (z2 && PhotoViewer.isShowingImage(this.photoEntry.path)) {
                    z4 = true;
                }
                this.imageView.getImageReceiver().setVisible(!z4, true);
                this.checkBox.setAlpha(!z4 ? 0.0f : 1.0f);
                this.videoInfoContainer.setAlpha(z4 ? 0.0f : 1.0f);
                requestLayout();
                setHasSpoiler(photoEntry.hasSpoiler);
                setStarsPrice(photoEntry.starsAmount, z);
            }
            if (photoEntry2.isVideo) {
                backupImageView = this.imageView;
                sb = new StringBuilder();
                str = "vthumb://";
            } else {
                this.imageView.setOrientation(photoEntry2.orientation, photoEntry2.invert, true);
                backupImageView = this.imageView;
                sb = new StringBuilder();
                str = "thumb://";
            }
            sb.append(str);
            sb.append(this.photoEntry.imageId);
            sb.append(":");
            sb.append(this.photoEntry.path);
            str2 = sb.toString();
        }
        backupImageView.setImage(str2, null, Theme.chat_attachEmptyDrawable);
        if (z2) {
            z4 = true;
        }
        this.imageView.getImageReceiver().setVisible(!z4, true);
        this.checkBox.setAlpha(!z4 ? 0.0f : 1.0f);
        this.videoInfoContainer.setAlpha(z4 ? 0.0f : 1.0f);
        requestLayout();
        setHasSpoiler(photoEntry.hasSpoiler);
        setStarsPrice(photoEntry.starsAmount, z);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00e2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setPhotoEntry(MediaController.SearchImage searchImage, boolean z, boolean z2) {
        BackupImageView backupImageView;
        ImageLocation forDocument;
        this.pressed = false;
        this.searchEntry = searchImage;
        this.isLast = z2;
        Drawable drawable = this.zoomOnSelect ? Theme.chat_attachEmptyDrawable : getResources().getDrawable(R.drawable.nophotos);
        TLRPC.PhotoSize photoSize = searchImage.thumbPhotoSize;
        if (photoSize == null) {
            TLRPC.PhotoSize photoSize2 = searchImage.photoSize;
            if (photoSize2 != null) {
                this.imageView.setImage(ImageLocation.getForPhoto(photoSize2, searchImage.photo), "80_80", drawable, searchImage);
            } else {
                String str = searchImage.thumbPath;
                if (str != null) {
                    this.imageView.setImage(str, null, drawable);
                } else if (TextUtils.isEmpty(searchImage.thumbUrl)) {
                    TLRPC.Document document = searchImage.document;
                    if (document != null) {
                        MessageObject.getDocumentVideoThumb(document);
                        TLRPC.VideoSize documentVideoThumb = MessageObject.getDocumentVideoThumb(searchImage.document);
                        if (documentVideoThumb != null) {
                            this.imageView.setImage(ImageLocation.getForDocument(documentVideoThumb, searchImage.document), null, ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(searchImage.document.thumbs, 90), searchImage.document), "52_52", null, -1L, 1, searchImage);
                        } else {
                            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(searchImage.document.thumbs, 320);
                            backupImageView = this.imageView;
                            forDocument = ImageLocation.getForDocument(closestPhotoSizeWithSize, searchImage.document);
                        }
                    } else {
                        this.imageView.setImageDrawable(drawable);
                    }
                } else {
                    forDocument = ImageLocation.getForPath(searchImage.thumbUrl);
                    if (searchImage.type == 1 && searchImage.thumbUrl.endsWith("mp4")) {
                        forDocument.imageType = 2;
                    }
                    backupImageView = this.imageView;
                }
            }
            boolean z3 = !z && PhotoViewer.isShowingImage(searchImage.getPathToAttach());
            this.imageView.getImageReceiver().setVisible(!z3, true);
            this.checkBox.setAlpha(!z3 ? 0.0f : 1.0f);
            this.videoInfoContainer.setAlpha(z3 ? 0.0f : 1.0f);
            requestLayout();
            setHasSpoiler(false);
            setStarsPrice(0L, false);
        }
        backupImageView = this.imageView;
        forDocument = ImageLocation.getForPhoto(photoSize, searchImage.photo);
        backupImageView.setImage(forDocument, (String) null, drawable, searchImage);
        if (z) {
        }
        this.imageView.getImageReceiver().setVisible(!z3, true);
        this.checkBox.setAlpha(!z3 ? 0.0f : 1.0f);
        this.videoInfoContainer.setAlpha(z3 ? 0.0f : 1.0f);
        requestLayout();
        setHasSpoiler(false);
        setStarsPrice(0L, false);
    }

    public void setStarsPrice(long j, boolean z) {
        SpannableStringBuilder spannableStringBuilder;
        CharSequence l;
        if (z == this.starsSelectedMultiple && j == this.stars) {
            return;
        }
        this.stars = j;
        this.starsSelectedMultiple = z;
        if (j > 0) {
            spannableStringBuilder = new SpannableStringBuilder();
            if (this.star == null) {
                this.star = new SpannableString("⭐");
                ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.star_small_inner);
                coloredImageSpan.setScale(0.7f, 0.7f);
                SpannableString spannableString = this.star;
                spannableString.setSpan(coloredImageSpan, 0, spannableString.length(), 33);
            }
            spannableStringBuilder.append((CharSequence) this.star);
            spannableStringBuilder.append((CharSequence) "\u2009");
            if (z) {
                if (this.lock == null) {
                    this.lock = new SpannableString("l");
                    ColoredImageSpan coloredImageSpan2 = new ColoredImageSpan(R.drawable.msg_mini_lock2);
                    SpannableString spannableString2 = this.lock;
                    spannableString2.setSpan(coloredImageSpan2, 0, spannableString2.length(), 33);
                }
                l = this.lock;
            } else {
                l = Long.toString(j);
            }
            spannableStringBuilder.append(l);
        } else {
            spannableStringBuilder = null;
        }
        this.imageView.setBlurredText(spannableStringBuilder);
        this.imageView.invalidate();
        this.container.invalidate();
    }

    public void showCheck(boolean z) {
        if (z && this.checkBox.getAlpha() == 1.0f) {
            return;
        }
        if (z || this.checkBox.getAlpha() != 0.0f) {
            AnimatorSet animatorSet = this.animatorSet;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.animatorSet = null;
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.animatorSet = animatorSet2;
            animatorSet2.setInterpolator(new DecelerateInterpolator());
            this.animatorSet.setDuration(180L);
            AnimatorSet animatorSet3 = this.animatorSet;
            FrameLayout frameLayout = this.videoInfoContainer;
            Property property = View.ALPHA;
            animatorSet3.playTogether(ObjectAnimator.ofFloat(frameLayout, (Property<FrameLayout, Float>) property, z ? 1.0f : 0.0f), ObjectAnimator.ofFloat(this.checkBox, (Property<CheckBox2, Float>) property, z ? 1.0f : 0.0f));
            this.animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.PhotoAttachPhotoCell.6
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (animator.equals(PhotoAttachPhotoCell.this.animatorSet)) {
                        PhotoAttachPhotoCell.this.animatorSet = null;
                    }
                }
            });
            this.animatorSet.start();
        }
    }

    public void showImage() {
        this.imageView.getImageReceiver().setVisible(true, true);
    }
}
