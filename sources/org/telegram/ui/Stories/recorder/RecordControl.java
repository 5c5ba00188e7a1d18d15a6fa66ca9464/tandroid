package org.telegram.ui.Stories.recorder;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.CircularProgressDrawable;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Stories.recorder.FlashViews;

/* loaded from: classes5.dex */
public class RecordControl extends View implements FlashViews.Invertable {
    private final float HALF_PI;
    public float amplitude;
    public final AnimatedFloat animatedAmplitude;
    private final Paint buttonPaint;
    private final Paint buttonPaintWhite;
    private final Point check1;
    private final Point check2;
    private final Point check3;
    private final AnimatedFloat checkAnimated;
    private final Paint checkPaint;
    private final Path checkPath;
    private final Path circlePath;
    private final AnimatedFloat collage;
    private float collageProgress;
    private final AnimatedFloat collageProgressAnimated;
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
    private final Path metaballsPath;
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
    private long touchStart;
    private final AnimatedFloat touchT;
    private float touchX;
    private float touchY;
    private final Drawable unlockDrawable;

    public interface Delegate {

        public abstract /* synthetic */ class -CC {
            public static long $default$getMaxVideoDuration(Delegate delegate) {
                return 60000L;
            }

            public static boolean $default$showStoriesDrafts(Delegate delegate) {
                return true;
            }
        }

        boolean canRecordAudio();

        long getMaxVideoDuration();

        void onCheckClick();

        void onFlipClick();

        void onFlipLongClick();

        void onGalleryClick();

        void onPhotoShoot();

        void onVideoDuration(long j);

        void onVideoRecordEnd(boolean z);

        void onVideoRecordLocked();

        void onVideoRecordStart(boolean z, Runnable runnable);

        void onZoom(float f);

        boolean showStoriesDrafts();
    }

    public RecordControl(Context context) {
        super(context);
        BlendMode blendMode;
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
        Paint paint8 = new Paint(1);
        this.checkPaint = paint8;
        Matrix matrix = new Matrix();
        this.redMatrix = matrix;
        this.recordButton = new ButtonBounce(this);
        this.flipButton = new ButtonBounce(this);
        this.lockButton = new ButtonBounce(this);
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.flipDrawableRotateT = new AnimatedFloat(this, 0L, 310L, cubicBezierInterpolator);
        this.dualT = new AnimatedFloat(this, 0L, 330L, cubicBezierInterpolator);
        this.checkPath = new Path();
        this.check1 = new Point(-AndroidUtilities.dpf2(9.666667f), AndroidUtilities.dpf2(2.3333333f));
        this.check2 = new Point(-AndroidUtilities.dpf2(2.8333333f), AndroidUtilities.dpf2(8.666667f));
        this.check3 = new Point(AndroidUtilities.dpf2(9.666667f), AndroidUtilities.dpf2(-3.6666667f));
        this.animatedAmplitude = new AnimatedFloat(this, 0L, 200L, CubicBezierInterpolator.DEFAULT);
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
        this.collage = new AnimatedFloat(this, 0L, 320L, cubicBezierInterpolator);
        this.collageProgressAnimated = new AnimatedFloat(this, 0L, 320L, cubicBezierInterpolator);
        this.checkAnimated = new AnimatedFloat(this, 0L, 320L, cubicBezierInterpolator);
        this.onRecordLongPressRunnable = new Runnable() { // from class: org.telegram.ui.Stories.recorder.RecordControl$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                RecordControl.this.lambda$new$1();
            }
        };
        this.onFlipLongPressRunnable = new Runnable() { // from class: org.telegram.ui.Stories.recorder.RecordControl$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                RecordControl.this.lambda$new$2();
            }
        };
        this.metaballsPath = new Path();
        this.circlePath = new Path();
        this.HALF_PI = 1.5707964f;
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
        Paint.Style style = Paint.Style.STROKE;
        paint.setStyle(style);
        Paint.Cap cap = Paint.Cap.ROUND;
        paint.setStrokeCap(cap);
        paint2.setColor(-577231);
        paint2.setStrokeCap(cap);
        paint2.setStyle(style);
        paint3.setColor(1677721600);
        paint4.setColor(-1);
        paint6.setColor(1493172223);
        paint7.setColor(402653184);
        paint6.setStyle(style);
        paint6.setStrokeCap(cap);
        paint7.setStyle(style);
        paint7.setStrokeCap(cap);
        paint8.setStyle(style);
        paint8.setStrokeJoin(Paint.Join.ROUND);
        paint8.setStrokeCap(cap);
        if (Build.VERSION.SDK_INT >= 29) {
            blendMode = BlendMode.CLEAR;
            paint8.setBlendMode(blendMode);
        } else {
            paint8.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        imageReceiver.setParentView(this);
        imageReceiver.setCrossfadeWithOldImage(true);
        imageReceiver.setRoundRadius(AndroidUtilities.dp(6.0f));
        Drawable mutate = context.getResources().getDrawable(R.drawable.msg_media_gallery).mutate();
        PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
        mutate.setColorFilter(new PorterDuffColorFilter(1308622847, mode));
        CombinedDrawable combinedDrawable = new CombinedDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), -13750737), mutate);
        this.noGalleryDrawable = combinedDrawable;
        combinedDrawable.setFullsize(false);
        combinedDrawable.setIconSize(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f));
        Resources resources = context.getResources();
        int i = R.drawable.msg_photo_switch2;
        Drawable mutate2 = resources.getDrawable(i).mutate();
        this.flipDrawableWhite = mutate2;
        mutate2.setColorFilter(new PorterDuffColorFilter(-1, mode));
        Drawable mutate3 = context.getResources().getDrawable(i).mutate();
        this.flipDrawableBlack = mutate3;
        mutate3.setColorFilter(new PorterDuffColorFilter(-16777216, mode));
        Drawable mutate4 = context.getResources().getDrawable(R.drawable.msg_filled_unlockedrecord).mutate();
        this.unlockDrawable = mutate4;
        mutate4.setColorFilter(new PorterDuffColorFilter(-1, mode));
        Drawable mutate5 = context.getResources().getDrawable(R.drawable.msg_filled_lockedrecord).mutate();
        this.lockDrawable = mutate5;
        mutate5.setColorFilter(new PorterDuffColorFilter(-16777216, mode));
        Drawable mutate6 = context.getResources().getDrawable(R.drawable.msg_round_pause_m).mutate();
        this.pauseDrawable = mutate6;
        mutate6.setColorFilter(new PorterDuffColorFilter(-1, mode));
        updateGalleryImage();
    }

    private float dist(Point point, Point point2) {
        return MathUtils.distance(point.x, point.y, point2.x, point2.y);
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

    private boolean isPressed(float f, float f2, float f3, float f4, float f5, boolean z) {
        return this.recording ? (!z || f4 - f2 <= ((float) AndroidUtilities.dp(100.0f))) && Math.abs(f3 - f) <= f5 : MathUtils.distance(f, f2, f3, f4) <= f5;
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
    public /* synthetic */ void lambda$new$1() {
        if (this.recording || hasCheck()) {
            return;
        }
        if (this.delegate.canRecordAudio()) {
            this.longpressRecording = true;
            this.showLock = true;
            this.delegate.onVideoRecordStart(true, new Runnable() { // from class: org.telegram.ui.Stories.recorder.RecordControl$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    RecordControl.this.lambda$new$0();
                }
            });
        } else {
            this.touch = false;
            this.recordButton.setPressed(false);
            this.flipButton.setPressed(false);
            this.lockButton.setPressed(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        if (this.recording || hasCheck()) {
            return;
        }
        this.delegate.onFlipLongClick();
        rotateFlip(360.0f);
        this.touch = false;
        this.recordButton.setPressed(false);
        this.flipButton.setPressed(false);
        this.lockButton.setPressed(false);
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTouchEvent$4() {
        this.recordingStart = System.currentTimeMillis();
        this.lastDuration = 0L;
        this.recording = true;
        this.delegate.onVideoDuration(0L);
    }

    private static void setDrawableBounds(Drawable drawable, float f, float f2) {
        setDrawableBounds(drawable, f, f2, Math.max(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()) / 2.0f);
    }

    private static void setDrawableBounds(Drawable drawable, float f, float f2, float f3) {
        drawable.setBounds((int) (f - f3), (int) (f2 - f3), (int) (f + f3), (int) (f2 + f3));
    }

    public boolean hasCheck() {
        return this.collageProgress >= 1.0f;
    }

    public boolean isTouch() {
        return this.discardParentTouch;
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

    /* JADX WARN: Removed duplicated region for block: B:135:0x0841  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0873  */
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
        RectF rectF;
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;
        RectF rectF2;
        long j;
        float f12;
        Paint paint;
        boolean z;
        Canvas canvas2;
        RectF rectF3;
        float f13;
        float f14;
        float f15;
        float scale;
        double d;
        double d2;
        float f16 = this.recordingT.set(this.recording ? 1.0f : 0.0f);
        float f17 = this.recordingLongT.set(this.recording ? 1.0f : 0.0f);
        float f18 = this.overrideStartModeIsVideoT;
        if (f18 < 0.0f) {
            f18 = this.startModeIsVideoT.set(this.startModeIsVideo ? 1.0f : 0.0f);
        }
        float max = Math.max(f16, f18);
        float f19 = this.touchT.set(this.touch ? 1.0f : 0.0f);
        float f20 = f19 * this.touchIsCenterT.set((Math.abs(this.touchX - this.cx) >= ((float) AndroidUtilities.dp(64.0f)) || !(this.recording || this.recordButton.isPressed())) ? 0.0f : 1.0f);
        float f21 = f19 * this.touchIsCenter2T.set(Math.abs(this.touchX - this.cx) < ((float) AndroidUtilities.dp(64.0f)) ? 1.0f : 0.0f);
        float clamp = Utilities.clamp((this.touchX - this.cx) / AndroidUtilities.dp(16.0f), 1.0f, -1.0f);
        float clamp2 = Utilities.clamp((this.touchX - this.cx) / AndroidUtilities.dp(64.0f), 1.0f, -1.0f);
        float f22 = f19 * this.touchIsButtonT.set(Math.min(Math.abs(this.touchX - this.rightCx), Math.abs(this.touchX - this.leftCx)) < ((float) AndroidUtilities.dp(16.0f)) ? 1.0f : 0.0f);
        float f23 = 1.0f - f16;
        float f24 = this.collage.set(this.collageProgress > 0.0f) * f23;
        float f25 = this.collageProgressAnimated.set(this.collageProgress);
        float f26 = this.checkAnimated.set(hasCheck());
        float f27 = this.longpressRecording ? f16 * max * f19 : 0.0f;
        if (f27 > 0.0f) {
            float dp = this.cx - AndroidUtilities.dp(50.0f);
            float dp2 = AndroidUtilities.dp(50.0f) + this.cx;
            this.hintLinePaintWhite.setStrokeWidth(AndroidUtilities.dp(2.0f));
            this.hintLinePaintBlack.setStrokeWidth(AndroidUtilities.dp(2.0f));
            f4 = f19;
            float f28 = f27;
            f = f26;
            f3 = f17;
            f5 = f24;
            f2 = f22;
            canvas.drawLine(dp2, this.cy, AndroidUtilities.lerp(dp2, this.rightCx - AndroidUtilities.dp(30.0f), f27), this.cy, this.hintLinePaintBlack);
            canvas.drawLine(dp2, this.cy, AndroidUtilities.lerp(dp2, this.rightCx - AndroidUtilities.dp(30.0f), f28), this.cy, this.hintLinePaintWhite);
            canvas.drawLine(dp, this.cy, AndroidUtilities.lerp(dp, this.leftCx + AndroidUtilities.dp(30.0f), f28), this.cy, this.hintLinePaintBlack);
            canvas.drawLine(dp, this.cy, AndroidUtilities.lerp(dp, this.leftCx + AndroidUtilities.dp(30.0f), f28), this.cy, this.hintLinePaintWhite);
        } else {
            f = f26;
            f2 = f22;
            f3 = f17;
            f4 = f19;
            f5 = f24;
        }
        float f29 = this.cx;
        float lerp = AndroidUtilities.lerp(f29, this.recordCx.set((AndroidUtilities.dp(4.0f) * clamp) + f29), f20);
        float lerp2 = AndroidUtilities.lerp(AndroidUtilities.lerp(AndroidUtilities.dp(29.0f), AndroidUtilities.dp(12.0f), f16), AndroidUtilities.dp(32.0f) - (AndroidUtilities.dp(4.0f) * Math.abs(clamp2)), f20);
        float lerp3 = AndroidUtilities.lerp(AndroidUtilities.lerp(AndroidUtilities.dp(32.0f), AndroidUtilities.dp(7.0f), f16), AndroidUtilities.dp(32.0f), f20);
        float lerp4 = AndroidUtilities.lerp(this.recordButton.getScale(this.startModeIsVideo ? 0.0f : 0.2f), (this.animatedAmplitude.set(this.amplitude) * 0.2f) + 1.0f, f16);
        RectF rectF4 = AndroidUtilities.rectTmp;
        float f30 = lerp - lerp2;
        float f31 = this.cy;
        float f32 = lerp + lerp2;
        rectF4.set(f30, f31 - lerp2, f32, f31 + lerp2);
        float f33 = 1.0f - f;
        this.mainPaint.setColor(ColorUtils.blendARGB(-1, -577231, max * f33));
        if (f > 0.0f) {
            canvas.save();
            canvas.scale(lerp4, lerp4, this.cx, this.cy);
            this.mainPaint.setAlpha((int) (f33 * 255.0f));
            canvas.drawRoundRect(rectF4, lerp3, lerp3, this.mainPaint);
            canvas.restore();
            f8 = f30;
            rectF = rectF4;
            f6 = lerp4;
            f7 = f32;
            f9 = lerp3;
            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), NotificationCenter.liveLocationsChanged, 31);
        } else {
            rectF = rectF4;
            f6 = lerp4;
            f7 = f32;
            f8 = f30;
            f9 = lerp3;
            canvas.save();
        }
        float f34 = f6;
        canvas.scale(f34, f34, this.cx, this.cy);
        this.mainPaint.setAlpha(NotificationCenter.liveLocationsChanged);
        RectF rectF5 = rectF;
        canvas.drawRoundRect(rectF5, f9, f9, this.mainPaint);
        if (f > 0.0f) {
            this.checkPaint.setStrokeWidth(AndroidUtilities.dp(4.0f));
            this.checkPath.rewind();
            Path path = this.checkPath;
            Point point = this.check1;
            path.moveTo(point.x, point.y);
            float f35 = f / 0.3f;
            f10 = f9;
            f11 = lerp;
            this.checkPath.lineTo(AndroidUtilities.lerp(this.check1.x, this.check2.x, Utilities.clamp(f35, 1.0f, 0.0f)), AndroidUtilities.lerp(this.check1.y, this.check2.y, Utilities.clamp(f35, 1.0f, 0.0f)));
            if (f > 0.3f) {
                float f36 = (f - 0.3f) / 0.7f;
                this.checkPath.lineTo(AndroidUtilities.lerp(this.check2.x, this.check3.x, Utilities.clamp(f36, 1.0f, 0.0f)), AndroidUtilities.lerp(this.check2.y, this.check3.y, Utilities.clamp(f36, 1.0f, 0.0f)));
            }
            canvas.translate(this.cx, this.cy);
            canvas.drawPath(this.checkPath, this.checkPaint);
        } else {
            f10 = f9;
            f11 = lerp;
        }
        canvas.restore();
        canvas.save();
        float max2 = Math.max(f34, 1.0f);
        canvas.scale(max2, max2, this.cx, this.cy);
        float max3 = Math.max(AndroidUtilities.dpf2(33.5f), lerp2 + AndroidUtilities.lerp(AndroidUtilities.dpf2(4.5f), AndroidUtilities.dp(9.0f), f20) + (AndroidUtilities.dp(5.0f) * f5 * (1.0f - f20)));
        float lerp5 = AndroidUtilities.lerp(AndroidUtilities.dp(3.0f), AndroidUtilities.dp(4.0f), f5);
        float lerp6 = AndroidUtilities.lerp(max3, (lerp2 - lerp5) - AndroidUtilities.dp(4.0f), f);
        float f37 = this.cx;
        float f38 = this.cy;
        rectF5.set(f37 - lerp6, f38 - lerp6, f37 + lerp6, f38 + lerp6);
        this.outlinePaint.setStrokeWidth(lerp5);
        this.outlinePaint.setAlpha((int) (AndroidUtilities.lerp(1.0f, 0.3f, f5) * 255.0f * f33));
        canvas.drawCircle(this.cx, this.cy, lerp6, this.outlinePaint);
        if ((f25 > 0.0f) && (f5 > 0.0f)) {
            this.outlinePaint.setAlpha(NotificationCenter.liveLocationsChanged);
            rectF2 = rectF5;
            canvas.drawArc(rectF5, -90.0f, f25 * 360.0f, false, this.outlinePaint);
        } else {
            rectF2 = rectF5;
        }
        long currentTimeMillis = System.currentTimeMillis() - this.recordingStart;
        float f39 = this.recording ? 0.0f : 1.0f - f3;
        Delegate delegate = this.delegate;
        long maxVideoDuration = delegate != null ? delegate.getMaxVideoDuration() : 60000L;
        float min = Math.min((currentTimeMillis / (maxVideoDuration >= 0 ? maxVideoDuration : 60000L)) * 360.0f, 360.0f);
        float f40 = this.recordingLoadingT.set(this.recordingLoading);
        this.outlineFilledPaint.setStrokeWidth(lerp5);
        this.outlineFilledPaint.setAlpha((int) (Math.max(f40 * 0.7f, 1.0f - f39) * 255.0f));
        if (f40 <= 0.0f) {
            f12 = -90.0f;
            canvas2 = canvas;
            rectF3 = rectF2;
            j = currentTimeMillis;
            z = false;
            paint = this.outlineFilledPaint;
        } else {
            j = currentTimeMillis;
            CircularProgressDrawable.getSegments((SystemClock.elapsedRealtime() - this.recordingLoadingStart) % 5400, this.loadingSegments);
            invalidate();
            float[] fArr = this.loadingSegments;
            float f41 = fArr[0];
            float f42 = fArr[1];
            float f43 = (f41 + f42) / 2.0f;
            float abs = Math.abs(f42 - f41) / 2.0f;
            if (this.recordingLoading) {
                float f44 = min / 2.0f;
                f43 = AndroidUtilities.lerp((-90.0f) + f44, f43, f40);
                abs = AndroidUtilities.lerp(f44, abs, f40);
            }
            f12 = f43 - abs;
            min = abs * 2.0f;
            paint = this.outlineFilledPaint;
            z = false;
            canvas2 = canvas;
            rectF3 = rectF2;
        }
        canvas2.drawArc(rectF3, f12, min, z, paint);
        if (this.recording) {
            invalidate();
            long j2 = j;
            long j3 = j2 / 1000;
            if (j3 != this.lastDuration / 1000) {
                this.delegate.onVideoDuration(j3);
            }
            if (maxVideoDuration > 0 && j2 >= maxVideoDuration) {
                post(new Runnable() { // from class: org.telegram.ui.Stories.recorder.RecordControl$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        RecordControl.this.lambda$onDraw$3();
                    }
                });
            }
            this.lastDuration = j2;
        }
        canvas.restore();
        if (this.showLock) {
            f13 = 0.2f;
            float scale2 = this.lockButton.getScale(0.2f) * f16;
            if (scale2 > 0.0f) {
                canvas.save();
                canvas.scale(scale2, scale2, this.leftCx, this.cy);
                canvas.drawCircle(this.leftCx, this.cy, AndroidUtilities.dp(22.0f), this.buttonPaint);
                this.unlockDrawable.draw(canvas);
                canvas.restore();
            }
        } else {
            f13 = 0.2f;
        }
        float scale3 = this.lockButton.getScale(f13) * f23 * f33;
        if (scale3 > 0.0f) {
            canvas.save();
            canvas.scale(scale3, scale3, this.leftCx, this.cy);
            this.galleryImage.draw(canvas);
            canvas.restore();
        }
        float f45 = this.dualT.set(this.dual ? 1.0f : 0.0f);
        if (f45 > 0.0f) {
            canvas.save();
            float scale4 = this.flipButton.getScale(f13) * f45 * f33;
            canvas.scale(scale4, scale4, this.rightCx, this.cy);
            canvas.rotate(this.flipDrawableRotateT.set(this.flipDrawableRotate), this.rightCx, this.cy);
            canvas.drawCircle(this.rightCx, this.cy, AndroidUtilities.dp(22.0f), this.buttonPaintWhite);
            this.flipDrawableBlack.draw(canvas);
            canvas.restore();
        }
        if (f45 < 1.0f) {
            canvas.save();
            float scale5 = this.flipButton.getScale(f13) * (1.0f - f45) * f33;
            canvas.scale(scale5, scale5, this.rightCx, this.cy);
            canvas.rotate(this.flipDrawableRotateT.set(this.flipDrawableRotate), this.rightCx, this.cy);
            canvas.drawCircle(this.rightCx, this.cy, AndroidUtilities.dp(22.0f), this.buttonPaint);
            this.flipDrawableWhite.draw(canvas);
            canvas.restore();
        }
        if (!this.longpressRecording || hasCheck()) {
            f14 = 0.0f;
        } else {
            float f46 = f2;
            f14 = AndroidUtilities.lerp(AndroidUtilities.dp(16.0f), AndroidUtilities.lerp(AndroidUtilities.dp(8.0f) + (AndroidUtilities.dp(8.0f) * Math.abs(clamp2)), AndroidUtilities.dp(22.0f), f46), Math.max(f46, f20)) * f4 * max * f16;
        }
        float f47 = this.lockedT.set((this.longpressRecording || !this.recording) ? 0.0f : 1.0f);
        if (f14 > 0.0f) {
            this.redPaint.setAlpha(NotificationCenter.liveLocationsChanged);
            canvas.drawCircle(this.touchX, this.cy, f14, this.redPaint);
            float f48 = this.touchX;
            float clamp3 = Utilities.clamp(1.0f - ((Math.abs(clamp2) * f4) / 1.3f), 1.0f, 0.0f);
            float abs2 = Math.abs(f11 - f48);
            if (abs2 < lerp2 + (f14 * 2.0f) && clamp3 < 0.6f) {
                float f49 = lerp2 + f14;
                if (abs2 < f49) {
                    float f50 = lerp2 * lerp2;
                    float f51 = abs2 * abs2;
                    float f52 = f14 * f14;
                    d = Math.acos(((f50 + f51) - f52) / ((lerp2 * 2.0f) * abs2));
                    d2 = Math.acos(((f52 + f51) - f50) / (r0 * abs2));
                } else {
                    d = 0.0d;
                    d2 = 0.0d;
                }
                double d3 = f48 > f11 ? 0.0d : 3.141592653589793d;
                double acos = (float) Math.acos((lerp2 - f14) / abs2);
                Double.isNaN(acos);
                f15 = f16;
                RectF rectF6 = rectF2;
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
                float f53 = f11;
                getVector(f53, this.cy, d6, lerp2, this.p1);
                getVector(f53, this.cy, d7, lerp2, this.p2);
                float f54 = f14;
                getVector(f48, this.cy, d9, f54, this.p3);
                getVector(f48, this.cy, d10, f54, this.p4);
                float min2 = Math.min(clamp3 * 2.4f, dist(this.p1, this.p3) / f49) * Math.min(1.0f, (abs2 * 2.0f) / f49);
                float f55 = lerp2 * min2;
                float f56 = f14 * min2;
                Point point2 = this.p1;
                getVector(point2.x, point2.y, d6 - 1.5707963705062866d, f55, this.h1);
                Point point3 = this.p2;
                getVector(point3.x, point3.y, d7 + 1.5707963705062866d, f55, this.h2);
                Point point4 = this.p3;
                getVector(point4.x, point4.y, d9 + 1.5707963705062866d, f56, this.h3);
                Point point5 = this.p4;
                getVector(point5.x, point5.y, d10 - 1.5707963705062866d, f56, this.h4);
                float f57 = f4 * max * f15 * f21;
                if (f57 > 0.0f) {
                    this.metaballsPath.rewind();
                    Path path2 = this.metaballsPath;
                    Point point6 = this.p1;
                    path2.moveTo(point6.x, point6.y);
                    Path path3 = this.metaballsPath;
                    Point point7 = this.h1;
                    float f58 = point7.x;
                    float f59 = point7.y;
                    Point point8 = this.h3;
                    float f60 = point8.x;
                    float f61 = point8.y;
                    Point point9 = this.p3;
                    path3.cubicTo(f58, f59, f60, f61, point9.x, point9.y);
                    Path path4 = this.metaballsPath;
                    Point point10 = this.p4;
                    path4.lineTo(point10.x, point10.y);
                    Path path5 = this.metaballsPath;
                    Point point11 = this.h4;
                    float f62 = point11.x;
                    float f63 = point11.y;
                    Point point12 = this.h2;
                    float f64 = point12.x;
                    float f65 = point12.y;
                    Point point13 = this.p2;
                    path5.cubicTo(f62, f63, f64, f65, point13.x, point13.y);
                    Path path6 = this.metaballsPath;
                    Point point14 = this.p1;
                    path6.lineTo(point14.x, point14.y);
                    this.redPaint.setAlpha((int) (f57 * 255.0f));
                    canvas.drawPath(this.metaballsPath, this.redPaint);
                    float f66 = this.cy;
                    rectF6.set(f8, f66 - lerp2, f7, f66 + lerp2);
                    float f67 = f10;
                    canvas.drawRoundRect(rectF6, f67, f67, this.redPaint);
                }
                if (f14 <= 0.0f || f47 > 0.0f) {
                    scale = this.lockButton.getScale(0.2f) * f15 * f33;
                    canvas.save();
                    this.circlePath.rewind();
                    if (f14 > 0.0f) {
                        this.circlePath.addCircle(this.touchX, this.cy, f14, Path.Direction.CW);
                    }
                    if (f47 > 0.0f && this.showLock) {
                        this.circlePath.addCircle(this.leftCx, this.cy, f47 * AndroidUtilities.dp(22.0f) * scale, Path.Direction.CW);
                    }
                    canvas.clipPath(this.circlePath);
                    if (this.showLock) {
                        canvas.save();
                        canvas.scale(scale, scale, this.leftCx, this.cy);
                        canvas.drawCircle(this.leftCx, this.cy, AndroidUtilities.dp(22.0f), this.buttonPaintWhite);
                        this.lockDrawable.draw(canvas);
                        canvas.restore();
                    }
                    float scale6 = this.flipButton.getScale(0.2f) * f33;
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
        f15 = f16;
        if (f14 <= 0.0f) {
        }
        scale = this.lockButton.getScale(0.2f) * f15 * f33;
        canvas.save();
        this.circlePath.rewind();
        if (f14 > 0.0f) {
        }
        if (f47 > 0.0f) {
            this.circlePath.addCircle(this.leftCx, this.cy, f47 * AndroidUtilities.dp(22.0f) * scale, Path.Direction.CW);
        }
        canvas.clipPath(this.circlePath);
        if (this.showLock) {
        }
        float scale62 = this.flipButton.getScale(0.2f) * f33;
        canvas.save();
        canvas.scale(scale62, scale62, this.rightCx, this.cy);
        canvas.rotate(this.flipDrawableRotateT.set(this.flipDrawableRotate), this.rightCx, this.cy);
        canvas.drawCircle(this.rightCx, this.cy, AndroidUtilities.dp(22.0f), this.buttonPaintWhite);
        this.flipDrawableBlack.draw(canvas);
        canvas.restore();
        canvas.restore();
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

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float clamp = Utilities.clamp(motionEvent.getX() + 0.0f, this.rightCx, this.leftCx);
        float y = motionEvent.getY() + 0.0f;
        boolean isPressed = isPressed(clamp, y, this.rightCx, this.cy, AndroidUtilities.dp(7.0f), true);
        boolean z = true;
        if (this.recordingLoading) {
            this.recordButton.setPressed(false);
            this.flipButton.setPressed(false);
            this.lockButton.setPressed(false);
        } else if (action == 0 || this.touch) {
            this.recordButton.setPressed(isPressed(clamp, y, this.cx, this.cy, AndroidUtilities.dp(60.0f), false));
            this.flipButton.setPressed(isPressed(clamp, y, this.rightCx, this.cy, (float) AndroidUtilities.dp(30.0f), true) && !hasCheck());
            this.lockButton.setPressed(isPressed(clamp, y, this.leftCx, this.cy, (float) AndroidUtilities.dp(30.0f), false) && !hasCheck());
        }
        if (action == 0) {
            this.touch = true;
            this.discardParentTouch = this.recordButton.isPressed() || this.flipButton.isPressed();
            this.touchStart = System.currentTimeMillis();
            this.touchX = clamp;
            this.touchY = y;
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
            this.touchY = y;
            invalidate();
            if (this.recording && !this.flipButtonWasPressed && isPressed) {
                rotateFlip(180.0f);
                this.delegate.onFlipClick();
            }
            if (this.recording && this.longpressRecording) {
                this.delegate.onZoom(Utilities.clamp(((this.cy - AndroidUtilities.dp(48.0f)) - y) / (AndroidUtilities.displaySize.y / 2.0f), 1.0f, 0.0f));
            }
        } else if (action != 1 && action != 3) {
            z = false;
        } else {
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
                }
                this.recording = false;
                this.recordingLoadingStart = SystemClock.elapsedRealtime();
                this.recordingLoading = true;
                this.delegate.onVideoRecordEnd(false);
            } else if (this.recordButton.isPressed()) {
                if (hasCheck()) {
                    this.delegate.onCheckClick();
                } else if (this.startModeIsVideo || this.recording || this.longpressRecording) {
                    if (!this.recording) {
                        if (this.delegate.canRecordAudio()) {
                            this.lastDuration = 0L;
                            this.recordingStart = System.currentTimeMillis();
                            this.showLock = false;
                            this.delegate.onVideoRecordStart(false, new Runnable() { // from class: org.telegram.ui.Stories.recorder.RecordControl$$ExternalSyntheticLambda2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    RecordControl.this.lambda$onTouchEvent$4();
                                }
                            });
                        }
                    }
                    this.recording = false;
                    this.recordingLoadingStart = SystemClock.elapsedRealtime();
                    this.recordingLoading = true;
                    this.delegate.onVideoRecordEnd(false);
                } else {
                    this.delegate.onPhotoShoot();
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

    public void rotateFlip(float f) {
        this.flipDrawableRotateT.setDuration(f > 180.0f ? 620L : 310L);
        this.flipDrawableRotate += f;
        invalidate();
    }

    public void setAmplitude(float f, boolean z) {
        this.amplitude = f;
        if (z) {
            return;
        }
        this.animatedAmplitude.set(f, true);
    }

    public void setCollageProgress(float f, boolean z) {
        if (Math.abs(f - this.collageProgress) < 0.01f) {
            return;
        }
        this.collageProgress = f;
        if (!z) {
            this.collage.set(f > 0.0f && !this.recording, true);
            this.collageProgressAnimated.set(f, true);
        }
        invalidate();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public void setDual(boolean z) {
        if (z != this.dual) {
            this.dual = z;
            invalidate();
        }
    }

    @Override // org.telegram.ui.Stories.recorder.FlashViews.Invertable
    public void setInvert(float f) {
        this.outlinePaint.setColor(ColorUtils.blendARGB(-1, -16777216, f));
        this.buttonPaint.setColor(ColorUtils.blendARGB(1677721600, 369098752, f));
        this.hintLinePaintWhite.setColor(ColorUtils.blendARGB(1493172223, 285212671, f));
        this.hintLinePaintBlack.setColor(ColorUtils.blendARGB(402653184, 805306368, f));
        Drawable drawable = this.flipDrawableWhite;
        int blendARGB = ColorUtils.blendARGB(-1, -16777216, f);
        PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
        drawable.setColorFilter(new PorterDuffColorFilter(blendARGB, mode));
        this.unlockDrawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.blendARGB(-1, -16777216, f), mode));
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

    public void updateGalleryImage() {
        String str;
        ArrayList<MediaController.PhotoEntry> arrayList;
        Delegate delegate = this.delegate;
        if (delegate != null && delegate.showStoriesDrafts()) {
            ArrayList arrayList2 = MessagesController.getInstance(this.galleryImage.getCurrentAccount()).getStoriesController().getDraftsController().drafts;
            this.galleryImage.setOrientation(0, 0, true);
            if (arrayList2 != null && !arrayList2.isEmpty() && ((StoryEntry) arrayList2.get(0)).draftThumbFile != null) {
                this.galleryImage.setImage(ImageLocation.getForPath(((StoryEntry) arrayList2.get(0)).draftThumbFile.getAbsolutePath()), "80_80", null, null, this.noGalleryDrawable, 0L, null, null, 0);
                return;
            }
        }
        MediaController.AlbumEntry albumEntry = MediaController.allMediaAlbumEntry;
        MediaController.PhotoEntry photoEntry = (albumEntry == null || (arrayList = albumEntry.photos) == null || arrayList.isEmpty()) ? null : albumEntry.photos.get(0);
        if (photoEntry != null && (str = photoEntry.thumbPath) != null) {
            this.galleryImage.setImage(ImageLocation.getForPath(str), "80_80", null, null, this.noGalleryDrawable, 0L, null, null, 0);
            return;
        }
        if (photoEntry == null || photoEntry.path == null) {
            this.galleryImage.setImageBitmap(this.noGalleryDrawable);
            return;
        }
        if (photoEntry.isVideo) {
            this.galleryImage.setImage(ImageLocation.getForPath("vthumb://" + photoEntry.imageId + ":" + photoEntry.path), "80_80", null, null, this.noGalleryDrawable, 0L, null, null, 0);
            return;
        }
        this.galleryImage.setOrientation(photoEntry.orientation, photoEntry.invert, true);
        this.galleryImage.setImage(ImageLocation.getForPath("thumb://" + photoEntry.imageId + ":" + photoEntry.path), "80_80", null, null, this.noGalleryDrawable, 0L, null, null, 0);
    }
}
