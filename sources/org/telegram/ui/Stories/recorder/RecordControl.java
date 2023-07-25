package org.telegram.ui.Stories.recorder;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.core.graphics.ColorUtils;
import com.google.zxing.common.detector.MathUtils;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.CircularProgressDrawable;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Point;
/* loaded from: classes4.dex */
public class RecordControl extends View {
    private final Paint buttonPaint;
    private final Paint buttonPaintWhite;
    private Path circlePath;
    private float cx;
    private float cy;
    private Delegate delegate;
    private boolean discardParentTouch;
    private boolean dual;
    private final AnimatedFloat dualT;
    private final ButtonBounce flipButton;
    private boolean flipButtonWasPressed;
    private final Drawable flipDrawableBlack;
    private float flipDrawableRotate;
    private final AnimatedFloat flipDrawableRotateT;
    private final Drawable flipDrawableWhite;
    private final ImageReceiver galleryImage;
    private final Point h1;
    private final Point h2;
    private final Point h3;
    private final Point h4;
    private final Paint hintLinePaintBlack;
    private final Paint hintLinePaintWhite;
    private long lastDuration;
    private float leftCx;
    private float[] loadingSegments;
    private final ButtonBounce lockButton;
    private final Drawable lockDrawable;
    private final AnimatedFloat lockedT;
    private boolean longpressRecording;
    private final Paint mainPaint;
    private Path metaballsPath;
    private final CombinedDrawable noGalleryDrawable;
    private final Runnable onFlipLongPressRunnable;
    private final Runnable onRecordLongPressRunnable;
    private final Paint outlineFilledPaint;
    private final Paint outlinePaint;
    private float overrideStartModeIsVideoT;
    private final Point p1;
    private final Point p2;
    private final Point p3;
    private final Point p4;
    private final Drawable pauseDrawable;
    private final ButtonBounce recordButton;
    private final AnimatedFloat recordCx;
    private boolean recording;
    private boolean recordingLoading;
    private long recordingLoadingStart;
    private final AnimatedFloat recordingLoadingT;
    private final AnimatedFloat recordingLongT;
    private long recordingStart;
    private final AnimatedFloat recordingT;
    private RadialGradient redGradient;
    private final Matrix redMatrix;
    private final Paint redPaint;
    private float rightCx;
    private boolean showLock;
    private boolean startModeIsVideo;
    private final AnimatedFloat startModeIsVideoT;
    private boolean touch;
    private final AnimatedFloat touchIsButtonT;
    private final AnimatedFloat touchIsCenter2T;
    private final AnimatedFloat touchIsCenterT;
    private final AnimatedFloat touchT;
    private float touchX;
    private final Drawable unlockDrawable;

    /* loaded from: classes4.dex */
    public interface Delegate {
        boolean canRecordAudio();

        void onFlipClick();

        void onFlipLongClick();

        void onGalleryClick();

        void onPhotoShoot();

        void onVideoDuration(long j);

        void onVideoRecordEnd(boolean z);

        void onVideoRecordLocked();

        void onVideoRecordStart(boolean z, Runnable runnable);

        void onZoom(float f);
    }

    public void startAsVideo(boolean z) {
        this.overrideStartModeIsVideoT = -1.0f;
        this.startModeIsVideo = z;
        invalidate();
    }

    public void startAsVideoT(float f) {
        this.overrideStartModeIsVideoT = f;
        invalidate();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public RecordControl(Context context) {
        super(context);
        ImageReceiver imageReceiver = new ImageReceiver();
        this.galleryImage = imageReceiver;
        this.mainPaint = new Paint(1);
        Paint paint = new Paint(1);
        this.outlinePaint = paint;
        Paint paint2 = new Paint(1);
        this.outlineFilledPaint = paint2;
        Paint paint3 = new Paint(1);
        this.buttonPaint = paint3;
        Paint paint4 = new Paint(1);
        this.buttonPaintWhite = paint4;
        Paint paint5 = new Paint(1);
        this.redPaint = paint5;
        Paint paint6 = new Paint(1);
        this.hintLinePaintWhite = paint6;
        Paint paint7 = new Paint(1);
        this.hintLinePaintBlack = paint7;
        Matrix matrix = new Matrix();
        this.redMatrix = matrix;
        this.recordButton = new ButtonBounce(this);
        this.flipButton = new ButtonBounce(this);
        this.lockButton = new ButtonBounce(this);
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.flipDrawableRotateT = new AnimatedFloat(this, 0L, 310L, cubicBezierInterpolator);
        this.dualT = new AnimatedFloat(this, 0L, 330L, cubicBezierInterpolator);
        this.startModeIsVideoT = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.overrideStartModeIsVideoT = -1.0f;
        this.startModeIsVideo = true;
        this.recordingT = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.recordingLongT = new AnimatedFloat(this, 0L, 850L, cubicBezierInterpolator);
        this.loadingSegments = new float[2];
        this.recordingLoadingT = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.touchT = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
        this.touchIsCenterT = new AnimatedFloat(this, 0L, 650L, cubicBezierInterpolator);
        this.touchIsCenter2T = new AnimatedFloat(this, 0L, 160L, CubicBezierInterpolator.EASE_IN);
        this.recordCx = new AnimatedFloat(this, 0L, 750L, cubicBezierInterpolator);
        this.touchIsButtonT = new AnimatedFloat(this, 0L, 650L, cubicBezierInterpolator);
        this.lockedT = new AnimatedFloat(this, 0L, 320L, cubicBezierInterpolator);
        this.onRecordLongPressRunnable = new Runnable() { // from class: org.telegram.ui.Stories.recorder.RecordControl$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                RecordControl.this.lambda$new$1();
            }
        };
        this.onFlipLongPressRunnable = new Runnable() { // from class: org.telegram.ui.Stories.recorder.RecordControl$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                RecordControl.this.lambda$new$2();
            }
        };
        this.metaballsPath = new Path();
        this.circlePath = new Path();
        this.p1 = new Point();
        this.p2 = new Point();
        this.p3 = new Point();
        this.p4 = new Point();
        this.h1 = new Point();
        this.h2 = new Point();
        this.h3 = new Point();
        this.h4 = new Point();
        setWillNotDraw(false);
        RadialGradient radialGradient = new RadialGradient(0.0f, 0.0f, AndroidUtilities.dp(48.0f), new int[]{-577231, -577231, -1}, new float[]{0.0f, 0.64f, 1.0f}, Shader.TileMode.CLAMP);
        this.redGradient = radialGradient;
        radialGradient.setLocalMatrix(matrix);
        paint5.setShader(this.redGradient);
        paint.setColor(-1);
        paint.setStyle(Paint.Style.STROKE);
        paint2.setColor(-577231);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint2.setStyle(Paint.Style.STROKE);
        paint3.setColor(1677721600);
        paint4.setColor(-1);
        paint6.setColor(1493172223);
        paint6.setStyle(Paint.Style.STROKE);
        paint6.setStrokeCap(Paint.Cap.ROUND);
        paint7.setColor(402653184);
        paint7.setStyle(Paint.Style.STROKE);
        paint7.setStrokeCap(Paint.Cap.ROUND);
        imageReceiver.setParentView(this);
        imageReceiver.setCrossfadeWithOldImage(true);
        imageReceiver.setRoundRadius(AndroidUtilities.dp(6.0f));
        Drawable mutate = context.getResources().getDrawable(R.drawable.msg_media_gallery).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(1308622847, PorterDuff.Mode.MULTIPLY));
        CombinedDrawable combinedDrawable = new CombinedDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), -13750737), mutate);
        this.noGalleryDrawable = combinedDrawable;
        combinedDrawable.setFullsize(false);
        combinedDrawable.setIconSize(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
        Resources resources = context.getResources();
        int i = R.drawable.msg_photo_switch2;
        Drawable mutate2 = resources.getDrawable(i).mutate();
        this.flipDrawableWhite = mutate2;
        mutate2.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.MULTIPLY));
        Drawable mutate3 = context.getResources().getDrawable(i).mutate();
        this.flipDrawableBlack = mutate3;
        mutate3.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
        Drawable mutate4 = context.getResources().getDrawable(R.drawable.msg_filled_unlockedrecord).mutate();
        this.unlockDrawable = mutate4;
        mutate4.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.MULTIPLY));
        Drawable mutate5 = context.getResources().getDrawable(R.drawable.msg_filled_lockedrecord).mutate();
        this.lockDrawable = mutate5;
        mutate5.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
        Drawable mutate6 = context.getResources().getDrawable(R.drawable.msg_round_pause_m).mutate();
        this.pauseDrawable = mutate6;
        mutate6.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.MULTIPLY));
        updateGalleryImage();
    }

    public void updateGalleryImage() {
        String str;
        ArrayList<MediaController.PhotoEntry> arrayList;
        ArrayList<StoryEntry> arrayList2 = MessagesController.getInstance(this.galleryImage.getCurrentAccount()).getStoriesController().getDraftsController().drafts;
        this.galleryImage.setOrientation(0, 0, true);
        if (arrayList2 != null && !arrayList2.isEmpty() && arrayList2.get(0).draftThumbFile != null) {
            this.galleryImage.setImage(ImageLocation.getForPath(arrayList2.get(0).draftThumbFile.getAbsolutePath()), "80_80", null, null, this.noGalleryDrawable, 0L, null, null, 0);
            return;
        }
        MediaController.AlbumEntry albumEntry = MediaController.allMediaAlbumEntry;
        MediaController.PhotoEntry photoEntry = null;
        if (albumEntry != null && (arrayList = albumEntry.photos) != null && !arrayList.isEmpty()) {
            photoEntry = albumEntry.photos.get(0);
        }
        if (photoEntry != null && (str = photoEntry.thumbPath) != null) {
            this.galleryImage.setImage(ImageLocation.getForPath(str), "80_80", null, null, this.noGalleryDrawable, 0L, null, null, 0);
        } else if (photoEntry != null && photoEntry.path != null) {
            if (photoEntry.isVideo) {
                ImageReceiver imageReceiver = this.galleryImage;
                imageReceiver.setImage(ImageLocation.getForPath("vthumb://" + photoEntry.imageId + ":" + photoEntry.path), "80_80", null, null, this.noGalleryDrawable, 0L, null, null, 0);
                return;
            }
            this.galleryImage.setOrientation(photoEntry.orientation, photoEntry.invert, true);
            ImageReceiver imageReceiver2 = this.galleryImage;
            imageReceiver2.setImage(ImageLocation.getForPath("thumb://" + photoEntry.imageId + ":" + photoEntry.path), "80_80", null, null, this.noGalleryDrawable, 0L, null, null, 0);
        } else {
            this.galleryImage.setImageBitmap(this.noGalleryDrawable);
        }
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.galleryImage.onAttachedToWindow();
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        this.galleryImage.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int dp = AndroidUtilities.dp(100.0f);
        float f = size;
        this.cx = f / 2.0f;
        this.cy = dp / 2.0f;
        float min = Math.min(AndroidUtilities.dp(135.0f), f * 0.35f);
        float f2 = this.cx;
        this.leftCx = f2 - min;
        float f3 = f2 + min;
        this.rightCx = f3;
        setDrawableBounds(this.flipDrawableWhite, f3, this.cy, AndroidUtilities.dp(14.0f));
        setDrawableBounds(this.flipDrawableBlack, this.rightCx, this.cy, AndroidUtilities.dp(14.0f));
        setDrawableBounds(this.unlockDrawable, this.leftCx, this.cy);
        setDrawableBounds(this.lockDrawable, this.leftCx, this.cy);
        setDrawableBounds(this.pauseDrawable, this.leftCx, this.cy);
        this.galleryImage.setImageCoords(this.leftCx - AndroidUtilities.dp(20.0f), this.cy - AndroidUtilities.dp(20.0f), AndroidUtilities.dp(40.0f), AndroidUtilities.dp(40.0f));
        this.redMatrix.reset();
        this.redMatrix.postTranslate(this.cx, this.cy);
        this.redGradient.setLocalMatrix(this.redMatrix);
        setMeasuredDimension(size, dp);
    }

    private static void setDrawableBounds(Drawable drawable, float f, float f2) {
        setDrawableBounds(drawable, f, f2, Math.max(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()) / 2.0f);
    }

    private static void setDrawableBounds(Drawable drawable, float f, float f2, float f3) {
        drawable.setBounds((int) (f - f3), (int) (f2 - f3), (int) (f + f3), (int) (f2 + f3));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        if (this.recording) {
            return;
        }
        if (!this.delegate.canRecordAudio()) {
            this.touch = false;
            this.recordButton.setPressed(false);
            this.flipButton.setPressed(false);
            this.lockButton.setPressed(false);
            return;
        }
        this.longpressRecording = true;
        this.showLock = true;
        this.delegate.onVideoRecordStart(true, new Runnable() { // from class: org.telegram.ui.Stories.recorder.RecordControl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                RecordControl.this.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.recordingStart = System.currentTimeMillis();
        this.recording = true;
        Delegate delegate = this.delegate;
        this.lastDuration = 0L;
        delegate.onVideoDuration(0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        if (this.recording) {
            return;
        }
        this.delegate.onFlipLongClick();
        rotateFlip(360.0f);
        this.touch = false;
        this.recordButton.setPressed(false);
        this.flipButton.setPressed(false);
        this.lockButton.setPressed(false);
    }

    /* JADX WARN: Removed duplicated region for block: B:122:0x06af  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x06e3  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onDraw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        long j;
        RectF rectF;
        float f6;
        float f7;
        float f8;
        float f9;
        float scale;
        float f10;
        double d;
        double d2;
        float f11 = this.recordingT.set(this.recording ? 1.0f : 0.0f);
        float f12 = this.recordingLongT.set(this.recording ? 1.0f : 0.0f);
        float f13 = this.overrideStartModeIsVideoT;
        if (f13 < 0.0f) {
            f13 = this.startModeIsVideoT.set(this.startModeIsVideo ? 1.0f : 0.0f);
        }
        float max = Math.max(f11, f13);
        float f14 = this.touchT.set(this.touch ? 1.0f : 0.0f);
        float f15 = f14 * this.touchIsCenterT.set((Math.abs(this.touchX - this.cx) >= ((float) AndroidUtilities.dp(64.0f)) || !(this.recording || this.recordButton.isPressed())) ? 0.0f : 1.0f);
        float f16 = f14 * this.touchIsCenter2T.set(Math.abs(this.touchX - this.cx) < ((float) AndroidUtilities.dp(64.0f)) ? 1.0f : 0.0f);
        float clamp = Utilities.clamp((this.touchX - this.cx) / AndroidUtilities.dp(16.0f), 1.0f, -1.0f);
        float clamp2 = Utilities.clamp((this.touchX - this.cx) / AndroidUtilities.dp(64.0f), 1.0f, -1.0f);
        float f17 = f14 * this.touchIsButtonT.set(Math.min(Math.abs(this.touchX - this.rightCx), Math.abs(this.touchX - this.leftCx)) < ((float) AndroidUtilities.dp(16.0f)) ? 1.0f : 0.0f);
        float f18 = this.longpressRecording ? f11 * max * f14 : 0.0f;
        if (f18 > 0.0f) {
            float dp = this.cx - AndroidUtilities.dp(50.0f);
            float dp2 = this.cx + AndroidUtilities.dp(50.0f);
            this.hintLinePaintWhite.setStrokeWidth(AndroidUtilities.dp(2.0f));
            this.hintLinePaintBlack.setStrokeWidth(AndroidUtilities.dp(2.0f));
            f3 = f16;
            f2 = f14;
            float f19 = f18;
            f = f17;
            canvas.drawLine(dp2, this.cy, AndroidUtilities.lerp(dp2, this.rightCx - AndroidUtilities.dp(30.0f), f18), this.cy, this.hintLinePaintBlack);
            canvas.drawLine(dp2, this.cy, AndroidUtilities.lerp(dp2, this.rightCx - AndroidUtilities.dp(30.0f), f19), this.cy, this.hintLinePaintWhite);
            canvas.drawLine(dp, this.cy, AndroidUtilities.lerp(dp, this.leftCx + AndroidUtilities.dp(30.0f), f19), this.cy, this.hintLinePaintBlack);
            canvas.drawLine(dp, this.cy, AndroidUtilities.lerp(dp, this.leftCx + AndroidUtilities.dp(30.0f), f19), this.cy, this.hintLinePaintWhite);
        } else {
            f = f17;
            f2 = f14;
            f3 = f16;
        }
        canvas.save();
        float lerp = AndroidUtilities.lerp(this.recordButton.getScale(this.startModeIsVideo ? 0.0f : 0.2f), 1.0f, f11);
        canvas.scale(lerp, lerp, this.cx, this.cy);
        this.mainPaint.setColor(ColorUtils.blendARGB(-1, -577231, max));
        float f20 = this.cx;
        float lerp2 = AndroidUtilities.lerp(f20, this.recordCx.set((AndroidUtilities.dp(4.0f) * clamp) + f20), f15);
        float lerp3 = AndroidUtilities.lerp(AndroidUtilities.lerp(AndroidUtilities.dp(29.0f), AndroidUtilities.dp(12.0f), f11), AndroidUtilities.dp(32.0f) - (AndroidUtilities.dp(4.0f) * Math.abs(clamp2)), f15);
        float lerp4 = AndroidUtilities.lerp(AndroidUtilities.lerp(AndroidUtilities.dp(32.0f), AndroidUtilities.dp(7.0f), f11), AndroidUtilities.dp(32.0f), f15);
        RectF rectF2 = AndroidUtilities.rectTmp;
        float f21 = lerp2 - lerp3;
        float f22 = this.cy;
        float f23 = lerp2 + lerp3;
        rectF2.set(f21, f22 - lerp3, f23, f22 + lerp3);
        canvas.drawRoundRect(rectF2, lerp4, lerp4, this.mainPaint);
        canvas.restore();
        canvas.save();
        float max2 = Math.max(lerp, 1.0f);
        canvas.scale(max2, max2, this.cx, this.cy);
        this.outlinePaint.setStrokeWidth(AndroidUtilities.dp(3.0f));
        float max3 = Math.max(AndroidUtilities.dpf2(33.5f), AndroidUtilities.lerp(AndroidUtilities.dpf2(4.5f), AndroidUtilities.dp(9.0f), f15) + lerp3);
        canvas.drawCircle(this.cx, this.cy, max3, this.outlinePaint);
        long currentTimeMillis = System.currentTimeMillis() - this.recordingStart;
        float f24 = this.cx;
        float f25 = this.cy;
        rectF2.set(f24 - max3, f25 - max3, f24 + max3, f25 + max3);
        float f26 = this.recording ? 0.0f : 1.0f - f12;
        float f27 = (((float) currentTimeMillis) / 60000.0f) * 360.0f;
        float f28 = this.recordingLoadingT.set(this.recordingLoading);
        this.outlineFilledPaint.setStrokeWidth(AndroidUtilities.dp(3.0f));
        this.outlineFilledPaint.setAlpha((int) (Math.max(0.7f * f28, 1.0f - f26) * 255.0f));
        if (f28 <= 0.0f) {
            f5 = lerp2;
            j = currentTimeMillis;
            f4 = f23;
            canvas.drawArc(rectF2, -90.0f, f27, false, this.outlineFilledPaint);
            rectF = rectF2;
            f6 = f21;
        } else {
            f4 = f23;
            f5 = lerp2;
            j = currentTimeMillis;
            CircularProgressDrawable.getSegments((float) ((SystemClock.elapsedRealtime() - this.recordingLoadingStart) % 5400), this.loadingSegments);
            invalidate();
            float[] fArr = this.loadingSegments;
            float f29 = fArr[0];
            float f30 = fArr[1];
            float f31 = (f29 + f30) / 2.0f;
            float abs = Math.abs(f30 - f29) / 2.0f;
            if (this.recordingLoading) {
                float f32 = f27 / 2.0f;
                f31 = AndroidUtilities.lerp((-90.0f) + f32, f31, f28);
                abs = AndroidUtilities.lerp(f32, abs, f28);
            }
            rectF = rectF2;
            f6 = f21;
            canvas.drawArc(rectF2, f31 - abs, abs * 2.0f, false, this.outlineFilledPaint);
        }
        if (this.recording) {
            invalidate();
            long j2 = j / 1000;
            if (j2 != this.lastDuration / 1000) {
                this.delegate.onVideoDuration(j2);
            }
            if (j >= 60000) {
                post(new Runnable() { // from class: org.telegram.ui.Stories.recorder.RecordControl$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        RecordControl.this.lambda$onDraw$3();
                    }
                });
            }
            this.lastDuration = j;
        }
        canvas.restore();
        if (this.showLock) {
            float scale2 = this.lockButton.getScale(0.2f) * f11;
            if (scale2 > 0.0f) {
                canvas.save();
                canvas.scale(scale2, scale2, this.leftCx, this.cy);
                canvas.drawCircle(this.leftCx, this.cy, AndroidUtilities.dp(22.0f), this.buttonPaint);
                this.unlockDrawable.draw(canvas);
                canvas.restore();
            }
        }
        float scale3 = this.lockButton.getScale(0.2f) * (1.0f - f11);
        if (scale3 > 0.0f) {
            canvas.save();
            canvas.scale(scale3, scale3, this.leftCx, this.cy);
            this.galleryImage.draw(canvas);
            canvas.restore();
        }
        float f33 = this.dualT.set(this.dual ? 1.0f : 0.0f);
        if (f33 > 0.0f) {
            canvas.save();
            float scale4 = this.flipButton.getScale(0.2f) * f33;
            canvas.scale(scale4, scale4, this.rightCx, this.cy);
            canvas.rotate(this.flipDrawableRotateT.set(this.flipDrawableRotate), this.rightCx, this.cy);
            canvas.drawCircle(this.rightCx, this.cy, AndroidUtilities.dp(22.0f), this.buttonPaintWhite);
            this.flipDrawableBlack.draw(canvas);
            canvas.restore();
        }
        if (f33 < 1.0f) {
            canvas.save();
            float scale5 = this.flipButton.getScale(0.2f) * (1.0f - f33);
            canvas.scale(scale5, scale5, this.rightCx, this.cy);
            canvas.rotate(this.flipDrawableRotateT.set(this.flipDrawableRotate), this.rightCx, this.cy);
            canvas.drawCircle(this.rightCx, this.cy, AndroidUtilities.dp(22.0f), this.buttonPaint);
            this.flipDrawableWhite.draw(canvas);
            canvas.restore();
        }
        if (this.longpressRecording) {
            float f34 = f;
            f7 = f2 * max * f11 * AndroidUtilities.lerp(AndroidUtilities.dp(16.0f), AndroidUtilities.lerp(AndroidUtilities.dp(8.0f) + (AndroidUtilities.dp(8.0f) * Math.abs(clamp2)), AndroidUtilities.dp(22.0f), f34), Math.max(f34, f15));
        } else {
            f7 = 0.0f;
        }
        float f35 = this.lockedT.set((this.longpressRecording || !this.recording) ? 0.0f : 1.0f);
        if (f7 > 0.0f) {
            this.redPaint.setAlpha(255);
            canvas.drawCircle(this.touchX, this.cy, f7, this.redPaint);
            float f36 = this.touchX;
            float clamp3 = Utilities.clamp(1.0f - ((Math.abs(clamp2) * f2) / 1.3f), 1.0f, 0.0f);
            float abs2 = Math.abs(f5 - f36);
            if (abs2 < lerp3 + (f7 * 2.0f) && clamp3 < 0.6f) {
                float f37 = lerp3 + f7;
                if (abs2 < f37) {
                    float f38 = lerp3 * lerp3;
                    float f39 = abs2 * abs2;
                    float f40 = f7 * f7;
                    d = Math.acos(((f38 + f39) - f40) / ((lerp3 * 2.0f) * abs2));
                    d2 = Math.acos(((f40 + f39) - f38) / (f10 * abs2));
                } else {
                    d = 0.0d;
                    d2 = 0.0d;
                }
                double d3 = f36 > f5 ? 0.0d : 3.141592653589793d;
                f8 = f7;
                double acos = (float) Math.acos((lerp3 - f7) / abs2);
                Double.isNaN(acos);
                f9 = f35;
                float f41 = f6;
                double d4 = clamp3;
                Double.isNaN(d4);
                double d5 = (acos - d) * d4;
                double d6 = d3 + d + d5;
                double d7 = (d3 - d) - d5;
                Double.isNaN(acos);
                Double.isNaN(d4);
                double d8 = ((3.141592653589793d - d2) - acos) * d4;
                double d9 = ((d3 + 3.141592653589793d) - d2) - d8;
                double d10 = (d3 - 3.141592653589793d) + d2 + d8;
                float f42 = f5;
                getVector(f42, this.cy, d6, lerp3, this.p1);
                getVector(f42, this.cy, d7, lerp3, this.p2);
                getVector(f36, this.cy, d9, f8, this.p3);
                getVector(f36, this.cy, d10, f8, this.p4);
                float min = Math.min(clamp3 * 2.4f, dist(this.p1, this.p3) / f37) * Math.min(1.0f, (abs2 * 2.0f) / f37);
                float f43 = lerp3 * min;
                float f44 = f8 * min;
                Point point = this.p1;
                getVector(point.x, point.y, d6 - 1.5707963705062866d, f43, this.h1);
                Point point2 = this.p2;
                getVector(point2.x, point2.y, d7 + 1.5707963705062866d, f43, this.h2);
                Point point3 = this.p3;
                getVector(point3.x, point3.y, d9 + 1.5707963705062866d, f44, this.h3);
                Point point4 = this.p4;
                getVector(point4.x, point4.y, d10 - 1.5707963705062866d, f44, this.h4);
                float f45 = f2 * max * f11 * f3;
                if (f45 > 0.0f) {
                    this.metaballsPath.rewind();
                    Path path = this.metaballsPath;
                    Point point5 = this.p1;
                    path.moveTo(point5.x, point5.y);
                    Path path2 = this.metaballsPath;
                    Point point6 = this.h1;
                    float f46 = point6.x;
                    float f47 = point6.y;
                    Point point7 = this.h3;
                    float f48 = point7.x;
                    float f49 = point7.y;
                    Point point8 = this.p3;
                    path2.cubicTo(f46, f47, f48, f49, point8.x, point8.y);
                    Path path3 = this.metaballsPath;
                    Point point9 = this.p4;
                    path3.lineTo(point9.x, point9.y);
                    Path path4 = this.metaballsPath;
                    Point point10 = this.h4;
                    float f50 = point10.x;
                    float f51 = point10.y;
                    Point point11 = this.h2;
                    float f52 = point11.x;
                    float f53 = point11.y;
                    Point point12 = this.p2;
                    path4.cubicTo(f50, f51, f52, f53, point12.x, point12.y);
                    Path path5 = this.metaballsPath;
                    Point point13 = this.p1;
                    path5.lineTo(point13.x, point13.y);
                    this.redPaint.setAlpha((int) (f45 * 255.0f));
                    canvas.drawPath(this.metaballsPath, this.redPaint);
                    float f54 = this.cy;
                    RectF rectF3 = rectF;
                    rectF3.set(f41, f54 - lerp3, f4, f54 + lerp3);
                    canvas.drawRoundRect(rectF3, lerp4, lerp4, this.redPaint);
                }
                if (f8 <= 0.0f || f9 > 0.0f) {
                    scale = this.lockButton.getScale(0.2f) * f11;
                    canvas.save();
                    this.circlePath.rewind();
                    if (f8 > 0.0f) {
                        this.circlePath.addCircle(this.touchX, this.cy, f8, Path.Direction.CW);
                    }
                    if (f9 > 0.0f && this.showLock) {
                        this.circlePath.addCircle(this.leftCx, this.cy, f9 * AndroidUtilities.dp(22.0f) * scale, Path.Direction.CW);
                    }
                    canvas.clipPath(this.circlePath);
                    if (this.showLock) {
                        canvas.save();
                        canvas.scale(scale, scale, this.leftCx, this.cy);
                        canvas.drawCircle(this.leftCx, this.cy, AndroidUtilities.dp(22.0f), this.buttonPaintWhite);
                        this.lockDrawable.draw(canvas);
                        canvas.restore();
                    }
                    float scale6 = this.flipButton.getScale(0.2f);
                    canvas.save();
                    canvas.scale(scale6, scale6, this.rightCx, this.cy);
                    canvas.rotate(this.flipDrawableRotateT.set(this.flipDrawableRotate), this.rightCx, this.cy);
                    canvas.drawCircle(this.rightCx, this.cy, AndroidUtilities.dp(22.0f), this.buttonPaintWhite);
                    this.flipDrawableBlack.draw(canvas);
                    canvas.restore();
                    canvas.restore();
                }
                return;
            }
        }
        f8 = f7;
        f9 = f35;
        if (f8 <= 0.0f) {
        }
        scale = this.lockButton.getScale(0.2f) * f11;
        canvas.save();
        this.circlePath.rewind();
        if (f8 > 0.0f) {
        }
        if (f9 > 0.0f) {
            this.circlePath.addCircle(this.leftCx, this.cy, f9 * AndroidUtilities.dp(22.0f) * scale, Path.Direction.CW);
        }
        canvas.clipPath(this.circlePath);
        if (this.showLock) {
        }
        float scale62 = this.flipButton.getScale(0.2f);
        canvas.save();
        canvas.scale(scale62, scale62, this.rightCx, this.cy);
        canvas.rotate(this.flipDrawableRotateT.set(this.flipDrawableRotate), this.rightCx, this.cy);
        canvas.drawCircle(this.rightCx, this.cy, AndroidUtilities.dp(22.0f), this.buttonPaintWhite);
        this.flipDrawableBlack.draw(canvas);
        canvas.restore();
        canvas.restore();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDraw$3() {
        this.recording = false;
        this.longpressRecording = false;
        this.recordingLoadingStart = SystemClock.elapsedRealtime();
        this.recordingLoading = true;
        this.touch = false;
        this.recordButton.setPressed(false);
        this.flipButton.setPressed(false);
        this.lockButton.setPressed(false);
        this.delegate.onVideoRecordEnd(true);
    }

    private void getVector(float f, float f2, double d, float f3, Point point) {
        double d2 = f;
        double cos = Math.cos(d);
        double d3 = f3;
        Double.isNaN(d3);
        Double.isNaN(d2);
        point.x = (float) (d2 + (cos * d3));
        double d4 = f2;
        double sin = Math.sin(d);
        Double.isNaN(d3);
        Double.isNaN(d4);
        point.y = (float) (d4 + (sin * d3));
    }

    private float dist(Point point, Point point2) {
        return MathUtils.distance(point.x, point.y, point2.x, point2.y);
    }

    public void rotateFlip(float f) {
        this.flipDrawableRotateT.setDuration(f > 180.0f ? 620L : 310L);
        this.flipDrawableRotate += f;
        invalidate();
    }

    private boolean isPressed(float f, float f2, float f3, float f4, float f5, boolean z) {
        return this.recording ? (!z || f4 - f2 <= ((float) AndroidUtilities.dp(100.0f))) && Math.abs(f3 - f) <= f5 : MathUtils.distance(f, f2, f3, f4) <= f5;
    }

    public boolean isTouch() {
        return this.discardParentTouch;
    }

    public void setDual(boolean z) {
        if (z != this.dual) {
            this.dual = z;
            invalidate();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float clamp = Utilities.clamp(motionEvent.getX() + 0.0f, this.rightCx, this.leftCx);
        float y = motionEvent.getY() + 0.0f;
        boolean isPressed = isPressed(clamp, y, this.rightCx, this.cy, AndroidUtilities.dp(7.0f), true);
        boolean z = false;
        if (this.recordingLoading) {
            this.recordButton.setPressed(false);
            this.flipButton.setPressed(false);
            this.lockButton.setPressed(false);
        } else if (action == 0 || this.touch) {
            this.recordButton.setPressed(isPressed(clamp, y, this.cx, this.cy, AndroidUtilities.dp(60.0f), false));
            this.flipButton.setPressed(isPressed(clamp, y, this.rightCx, this.cy, AndroidUtilities.dp(30.0f), true));
            this.lockButton.setPressed(isPressed(clamp, y, this.leftCx, this.cy, AndroidUtilities.dp(30.0f), false));
        }
        if (action == 0) {
            this.touch = true;
            this.discardParentTouch = (this.recordButton.isPressed() || this.flipButton.isPressed()) ? true : true;
            System.currentTimeMillis();
            this.touchX = clamp;
            if (Math.abs(clamp - this.cx) < AndroidUtilities.dp(50.0f)) {
                AndroidUtilities.runOnUIThread(this.onRecordLongPressRunnable, ViewConfiguration.getLongPressTimeout());
            }
            if (this.flipButton.isPressed()) {
                AndroidUtilities.runOnUIThread(this.onFlipLongPressRunnable, ViewConfiguration.getLongPressTimeout());
            }
        } else if (action == 2) {
            if (!this.touch) {
                return false;
            }
            this.touchX = Utilities.clamp(clamp, this.rightCx, this.leftCx);
            invalidate();
            if (this.recording && !this.flipButtonWasPressed && isPressed) {
                rotateFlip(180.0f);
                this.delegate.onFlipClick();
            }
            if (this.recording && this.longpressRecording) {
                this.delegate.onZoom(Utilities.clamp(((this.cy - AndroidUtilities.dp(48.0f)) - y) / (AndroidUtilities.displaySize.y / 2.0f), 1.0f, 0.0f));
            }
        } else {
            if (action == 1 || action == 3) {
                if (!this.touch) {
                    return false;
                }
                this.touch = false;
                this.discardParentTouch = false;
                AndroidUtilities.cancelRunOnUIThread(this.onRecordLongPressRunnable);
                AndroidUtilities.cancelRunOnUIThread(this.onFlipLongPressRunnable);
                if (!this.recording && this.lockButton.isPressed()) {
                    this.delegate.onGalleryClick();
                } else if (this.recording && this.longpressRecording) {
                    if (this.lockButton.isPressed()) {
                        this.longpressRecording = false;
                        this.lockedT.set(1.0f, true);
                        this.delegate.onVideoRecordLocked();
                    } else {
                        this.recording = false;
                        this.recordingLoadingStart = SystemClock.elapsedRealtime();
                        this.recordingLoading = true;
                        this.delegate.onVideoRecordEnd(false);
                    }
                } else if (this.recordButton.isPressed()) {
                    if (!this.startModeIsVideo && !this.recording && !this.longpressRecording) {
                        this.delegate.onPhotoShoot();
                    } else if (!this.recording) {
                        if (this.delegate.canRecordAudio()) {
                            this.lastDuration = 0L;
                            this.recordingStart = System.currentTimeMillis();
                            this.showLock = false;
                            this.delegate.onVideoRecordStart(false, new Runnable() { // from class: org.telegram.ui.Stories.recorder.RecordControl$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    RecordControl.this.lambda$onTouchEvent$4();
                                }
                            });
                        }
                    } else {
                        this.recording = false;
                        this.recordingLoadingStart = SystemClock.elapsedRealtime();
                        this.recordingLoading = true;
                        this.delegate.onVideoRecordEnd(false);
                    }
                }
                this.longpressRecording = false;
                if (this.flipButton.isPressed()) {
                    rotateFlip(180.0f);
                    this.delegate.onFlipClick();
                }
                this.recordButton.setPressed(false);
                this.flipButton.setPressed(false);
                this.lockButton.setPressed(false);
                invalidate();
            }
            this.flipButtonWasPressed = isPressed;
            return z;
        }
        z = true;
        this.flipButtonWasPressed = isPressed;
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTouchEvent$4() {
        this.recordingStart = System.currentTimeMillis();
        this.lastDuration = 0L;
        this.recording = true;
        this.delegate.onVideoDuration(0L);
    }

    public void stopRecording() {
        if (this.recording) {
            this.recording = false;
            this.recordingLoadingStart = SystemClock.elapsedRealtime();
            this.recordingLoading = true;
            this.delegate.onVideoRecordEnd(false);
            this.recordButton.setPressed(false);
            this.flipButton.setPressed(false);
            this.lockButton.setPressed(false);
            invalidate();
        }
    }

    public void stopRecordingLoading(boolean z) {
        this.recordingLoading = false;
        if (!z) {
            this.recordingLoadingT.set(false, true);
        }
        invalidate();
    }
}
