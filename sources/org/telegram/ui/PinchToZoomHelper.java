package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import androidx.core.graphics.ColorUtils;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.WebFile;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.Components.spoilers.SpoilerEffect2;

/* loaded from: classes4.dex */
public class PinchToZoomHelper {
    private ImageReceiver blurImage;
    Callback callback;
    private View child;
    private ImageReceiver childImage;
    private View childTextureView;
    private View childTextureViewContainer;
    ClipBoundsListener clipBoundsListener;
    private float[] clipTopBottom;
    private float enterProgress;
    private ColorMatrixColorFilter fancyBlurFilter;
    private float finishProgress;
    ValueAnimator finishTransition;
    float fragmentOffsetX;
    float fragmentOffsetY;
    private final ViewGroup fragmentView;
    private ImageReceiver fullImage;
    private float fullImageHeight;
    private float fullImageWidth;
    private boolean hasMediaSpoiler;
    private float imageHeight;
    private float imageWidth;
    private float imageX;
    private float imageY;
    private boolean inOverlayMode;
    private boolean isHardwareVideo;
    boolean isInPinchToZoomTouchMode;
    private final boolean isSimple;
    private SpoilerEffect mediaSpoilerEffect;
    private SpoilerEffect2 mediaSpoilerEffect2;
    private MessageObject messageObject;
    private ZoomOverlayView overlayView;
    float parentOffsetX;
    float parentOffsetY;
    private final ViewGroup parentView;
    private Path path;
    float pinchCenterX;
    float pinchCenterY;
    float pinchScale;
    float pinchStartCenterX;
    float pinchStartCenterY;
    float pinchStartDistance;
    float pinchTranslationX;
    float pinchTranslationY;
    private int pointerId1;
    private int pointerId2;
    private float progressToFullView;
    private float[] spoilerRadii;

    public interface Callback {

        public abstract /* synthetic */ class -CC {
            public static TextureView $default$getCurrentTextureView(Callback callback) {
                return null;
            }

            public static void $default$onZoomFinished(Callback callback, MessageObject messageObject) {
            }
        }

        TextureView getCurrentTextureView();

        void onZoomFinished(MessageObject messageObject);

        void onZoomStarted(MessageObject messageObject);
    }

    public interface ClipBoundsListener {
        void getClipTopBottom(float[] fArr);
    }

    private class ZoomOverlayView extends FrameLayout {
        private Paint aspectPaint;
        private Path aspectPath;
        private AspectRatioFrameLayout aspectRatioFrameLayout;
        private BackupImageView backupImageView;
        private FrameLayout videoPlayerContainer;
        private TextureView videoTextureView;

        public ZoomOverlayView(Context context) {
            super(context);
            this.aspectPath = new Path();
            this.aspectPaint = new Paint(1);
            if (Build.VERSION.SDK_INT >= 21) {
                FrameLayout frameLayout = new FrameLayout(context);
                this.videoPlayerContainer = frameLayout;
                frameLayout.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.PinchToZoomHelper.ZoomOverlayView.1
                    @Override // android.view.ViewOutlineProvider
                    public void getOutline(View view, Outline outline) {
                        ImageReceiver imageReceiver = (ImageReceiver) view.getTag(R.id.parent_tag);
                        if (imageReceiver == null) {
                            int i = AndroidUtilities.roundMessageSize;
                            outline.setOval(0, 0, i, i);
                            return;
                        }
                        int[] roundRadius = imageReceiver.getRoundRadius(true);
                        int i2 = 0;
                        for (int i3 = 0; i3 < 4; i3++) {
                            i2 = Math.max(i2, roundRadius[i3]);
                        }
                        outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight(), i2);
                    }
                });
                this.videoPlayerContainer.setClipToOutline(true);
            } else {
                this.videoPlayerContainer = new FrameLayout(context) { // from class: org.telegram.ui.PinchToZoomHelper.ZoomOverlayView.2
                    RectF rect = new RectF();

                    @Override // android.view.ViewGroup, android.view.View
                    protected void dispatchDraw(Canvas canvas) {
                        super.dispatchDraw(canvas);
                        if (getTag() == null) {
                            canvas.drawPath(ZoomOverlayView.this.aspectPath, ZoomOverlayView.this.aspectPaint);
                        }
                    }

                    @Override // android.view.View
                    protected void onSizeChanged(int i, int i2, int i3, int i4) {
                        super.onSizeChanged(i, i2, i3, i4);
                        ZoomOverlayView.this.aspectPath.reset();
                        ImageReceiver imageReceiver = (ImageReceiver) getTag(R.id.parent_tag);
                        if (imageReceiver != null) {
                            int[] roundRadius = imageReceiver.getRoundRadius(true);
                            int i5 = 0;
                            for (int i6 = 0; i6 < 4; i6++) {
                                i5 = Math.max(i5, roundRadius[i6]);
                            }
                            this.rect.set(0.0f, 0.0f, i, i2);
                            ZoomOverlayView.this.aspectPath.addRoundRect(this.rect, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), Path.Direction.CW);
                        } else {
                            float f = i / 2;
                            ZoomOverlayView.this.aspectPath.addCircle(f, i2 / 2, f, Path.Direction.CW);
                        }
                        ZoomOverlayView.this.aspectPath.toggleInverseFillType();
                    }

                    @Override // android.view.View
                    public void setVisibility(int i) {
                        super.setVisibility(i);
                        if (i == 0) {
                            setLayerType(2, null);
                        }
                    }
                };
                this.aspectPath = new Path();
                Paint paint = new Paint(1);
                this.aspectPaint = paint;
                paint.setColor(-16777216);
                this.aspectPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            }
            BackupImageView backupImageView = new BackupImageView(context);
            this.backupImageView = backupImageView;
            this.videoPlayerContainer.addView(backupImageView);
            this.videoPlayerContainer.setWillNotDraw(false);
            AspectRatioFrameLayout aspectRatioFrameLayout = new AspectRatioFrameLayout(context);
            this.aspectRatioFrameLayout = aspectRatioFrameLayout;
            aspectRatioFrameLayout.setBackgroundColor(0);
            this.videoPlayerContainer.addView(this.aspectRatioFrameLayout, LayoutHelper.createFrame(-1, -1, 17));
            TextureView textureView = new TextureView(context);
            this.videoTextureView = textureView;
            textureView.setOpaque(false);
            this.aspectRatioFrameLayout.addView(this.videoTextureView, LayoutHelper.createFrame(-1, -1.0f));
            addView(this.videoPlayerContainer, LayoutHelper.createFrame(-2, -2.0f));
            setWillNotDraw(false);
        }

        private void drawImage(Canvas canvas) {
            if (!PinchToZoomHelper.this.inOverlayMode || PinchToZoomHelper.this.child == null || PinchToZoomHelper.this.parentView == null) {
                return;
            }
            PinchToZoomHelper.this.updateViewsLocation();
            float left = PinchToZoomHelper.this.parentOffsetX - getLeft();
            float top = PinchToZoomHelper.this.parentOffsetY - getTop();
            canvas.save();
            PinchToZoomHelper pinchToZoomHelper = PinchToZoomHelper.this;
            float f = ((pinchToZoomHelper.pinchScale * pinchToZoomHelper.finishProgress) + 1.0f) - PinchToZoomHelper.this.finishProgress;
            PinchToZoomHelper pinchToZoomHelper2 = PinchToZoomHelper.this;
            canvas.scale(f, f, pinchToZoomHelper2.pinchCenterX + left, pinchToZoomHelper2.pinchCenterY + top);
            PinchToZoomHelper pinchToZoomHelper3 = PinchToZoomHelper.this;
            float f2 = (pinchToZoomHelper3.pinchTranslationX * pinchToZoomHelper3.finishProgress) + left;
            PinchToZoomHelper pinchToZoomHelper4 = PinchToZoomHelper.this;
            canvas.translate(f2, (pinchToZoomHelper4.pinchTranslationY * pinchToZoomHelper4.finishProgress) + top);
            if (PinchToZoomHelper.this.fullImage != null && PinchToZoomHelper.this.fullImage.hasNotThumb()) {
                if (PinchToZoomHelper.this.progressToFullView != 1.0f) {
                    PinchToZoomHelper.access$1416(PinchToZoomHelper.this, 0.10666667f);
                    if (PinchToZoomHelper.this.progressToFullView > 1.0f) {
                        PinchToZoomHelper.this.progressToFullView = 1.0f;
                    } else {
                        PinchToZoomHelper.this.invalidateViews();
                    }
                }
                PinchToZoomHelper.this.fullImage.setAlpha(PinchToZoomHelper.this.progressToFullView);
            }
            float f3 = PinchToZoomHelper.this.imageX;
            float f4 = PinchToZoomHelper.this.imageY;
            if (PinchToZoomHelper.this.imageHeight != PinchToZoomHelper.this.fullImageHeight || PinchToZoomHelper.this.imageWidth != PinchToZoomHelper.this.fullImageWidth) {
                float f5 = f < 1.0f ? 0.0f : f < 1.4f ? (f - 1.0f) / 0.4f : 1.0f;
                float f6 = (PinchToZoomHelper.this.fullImageHeight - PinchToZoomHelper.this.imageHeight) / 2.0f;
                float f7 = ((PinchToZoomHelper.this.fullImageWidth - PinchToZoomHelper.this.imageWidth) / 2.0f) * f5;
                float f8 = PinchToZoomHelper.this.imageX - f7;
                float f9 = f6 * f5;
                float f10 = PinchToZoomHelper.this.imageY - f9;
                if (PinchToZoomHelper.this.childImage != null) {
                    PinchToZoomHelper.this.childImage.setImageCoords(f8, f10, PinchToZoomHelper.this.imageWidth + (f7 * 2.0f), PinchToZoomHelper.this.imageHeight + (f9 * 2.0f));
                }
                f4 = f10;
                f3 = f8;
            }
            if (PinchToZoomHelper.this.isHardwareVideo) {
                FrameLayout frameLayout = this.videoPlayerContainer;
                PinchToZoomHelper pinchToZoomHelper5 = PinchToZoomHelper.this;
                frameLayout.setPivotX(pinchToZoomHelper5.pinchCenterX - pinchToZoomHelper5.imageX);
                FrameLayout frameLayout2 = this.videoPlayerContainer;
                PinchToZoomHelper pinchToZoomHelper6 = PinchToZoomHelper.this;
                frameLayout2.setPivotY(pinchToZoomHelper6.pinchCenterY - pinchToZoomHelper6.imageY);
                this.videoPlayerContainer.setScaleY(f);
                this.videoPlayerContainer.setScaleX(f);
                FrameLayout frameLayout3 = this.videoPlayerContainer;
                float f11 = f3 + left;
                PinchToZoomHelper pinchToZoomHelper7 = PinchToZoomHelper.this;
                frameLayout3.setTranslationX(f11 + (pinchToZoomHelper7.pinchTranslationX * f * pinchToZoomHelper7.finishProgress));
                FrameLayout frameLayout4 = this.videoPlayerContainer;
                float f12 = f4 + top;
                PinchToZoomHelper pinchToZoomHelper8 = PinchToZoomHelper.this;
                frameLayout4.setTranslationY(f12 + (pinchToZoomHelper8.pinchTranslationY * f * pinchToZoomHelper8.finishProgress));
            } else {
                if (PinchToZoomHelper.this.childImage != null) {
                    if (PinchToZoomHelper.this.progressToFullView != 1.0f) {
                        if (PinchToZoomHelper.this.childImage.getLottieAnimation() != null || PinchToZoomHelper.this.childImage.getAnimation() != null || PinchToZoomHelper.this.fullImage.getLottieAnimation() != null || PinchToZoomHelper.this.fullImage.getAnimation() != null) {
                            invalidate();
                        }
                        PinchToZoomHelper.this.childImage.draw(canvas);
                        PinchToZoomHelper.this.fullImage.setImageCoords(PinchToZoomHelper.this.childImage.getImageX(), PinchToZoomHelper.this.childImage.getImageY(), PinchToZoomHelper.this.childImage.getImageWidth(), PinchToZoomHelper.this.childImage.getImageHeight());
                        PinchToZoomHelper.this.fullImage.draw(canvas);
                    } else {
                        PinchToZoomHelper.this.fullImage.setImageCoords(PinchToZoomHelper.this.childImage.getImageX(), PinchToZoomHelper.this.childImage.getImageY(), PinchToZoomHelper.this.childImage.getImageWidth(), PinchToZoomHelper.this.childImage.getImageHeight());
                        PinchToZoomHelper.this.fullImage.draw(canvas);
                        if (PinchToZoomHelper.this.fullImage.getLottieAnimation() != null || PinchToZoomHelper.this.fullImage.getAnimation() != null) {
                            invalidate();
                        }
                    }
                }
                if (PinchToZoomHelper.this.childTextureViewContainer != null) {
                    if (PinchToZoomHelper.this.childTextureView == null) {
                        View unused = PinchToZoomHelper.this.childTextureViewContainer;
                    }
                    canvas.save();
                    canvas.translate(PinchToZoomHelper.this.childImage.getImageX(), PinchToZoomHelper.this.childImage.getImageY());
                    float max = Math.max(PinchToZoomHelper.this.childImage.getImageWidth() / PinchToZoomHelper.this.childTextureViewContainer.getMeasuredWidth(), PinchToZoomHelper.this.childImage.getImageHeight() / PinchToZoomHelper.this.childTextureViewContainer.getMeasuredHeight());
                    if (PinchToZoomHelper.this.childImage.isAspectFit()) {
                        canvas.scale(max, max, PinchToZoomHelper.this.childTextureViewContainer.getMeasuredWidth() / 2.0f, 0.0f);
                    } else {
                        canvas.scale(max, max);
                    }
                    PinchToZoomHelper.this.childTextureViewContainer.draw(canvas);
                    canvas.restore();
                }
            }
            if (PinchToZoomHelper.this.hasMediaSpoiler) {
                PinchToZoomHelper.this.blurImage.setAlpha(PinchToZoomHelper.this.childImage.getAlpha());
                PinchToZoomHelper.this.blurImage.setRoundRadius(PinchToZoomHelper.this.childImage.getRoundRadius(true));
                PinchToZoomHelper.this.blurImage.setImageCoords(PinchToZoomHelper.this.childImage.getImageX(), PinchToZoomHelper.this.childImage.getImageY(), PinchToZoomHelper.this.childImage.getImageWidth(), PinchToZoomHelper.this.childImage.getImageHeight());
                PinchToZoomHelper.this.blurImage.draw(canvas);
                int[] roundRadius = PinchToZoomHelper.this.childImage.getRoundRadius(true);
                float[] fArr = PinchToZoomHelper.this.spoilerRadii;
                float[] fArr2 = PinchToZoomHelper.this.spoilerRadii;
                float f13 = roundRadius[0];
                fArr2[1] = f13;
                fArr[0] = f13;
                float[] fArr3 = PinchToZoomHelper.this.spoilerRadii;
                float[] fArr4 = PinchToZoomHelper.this.spoilerRadii;
                float f14 = roundRadius[1];
                fArr4[3] = f14;
                fArr3[2] = f14;
                float[] fArr5 = PinchToZoomHelper.this.spoilerRadii;
                float[] fArr6 = PinchToZoomHelper.this.spoilerRadii;
                float f15 = roundRadius[2];
                fArr6[5] = f15;
                fArr5[4] = f15;
                float[] fArr7 = PinchToZoomHelper.this.spoilerRadii;
                float[] fArr8 = PinchToZoomHelper.this.spoilerRadii;
                float f16 = roundRadius[3];
                fArr8[7] = f16;
                fArr7[6] = f16;
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(PinchToZoomHelper.this.childImage.getImageX(), PinchToZoomHelper.this.childImage.getImageY(), PinchToZoomHelper.this.childImage.getImageX2(), PinchToZoomHelper.this.childImage.getImageY2());
                PinchToZoomHelper.this.path.rewind();
                PinchToZoomHelper.this.path.addRoundRect(rectF, PinchToZoomHelper.this.spoilerRadii, Path.Direction.CW);
                canvas.save();
                canvas.clipPath(PinchToZoomHelper.this.path);
                if (PinchToZoomHelper.this.mediaSpoilerEffect2 != null) {
                    canvas.translate(PinchToZoomHelper.this.childImage.getImageX(), PinchToZoomHelper.this.childImage.getImageY());
                    PinchToZoomHelper.this.mediaSpoilerEffect2.draw(canvas, PinchToZoomHelper.this.overlayView, (int) PinchToZoomHelper.this.childImage.getImageWidth(), (int) PinchToZoomHelper.this.childImage.getImageHeight());
                } else {
                    PinchToZoomHelper.this.mediaSpoilerEffect.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(-1) * 0.325f * PinchToZoomHelper.this.childImage.getAlpha())));
                    PinchToZoomHelper.this.mediaSpoilerEffect.setBounds((int) PinchToZoomHelper.this.childImage.getImageX(), (int) PinchToZoomHelper.this.childImage.getImageY(), (int) PinchToZoomHelper.this.childImage.getImageX2(), (int) PinchToZoomHelper.this.childImage.getImageY2());
                    PinchToZoomHelper.this.mediaSpoilerEffect.draw(canvas);
                }
                canvas.restore();
                invalidate();
            }
            canvas.restore();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            float f;
            float f2;
            PinchToZoomHelper pinchToZoomHelper;
            ClipBoundsListener clipBoundsListener;
            PinchToZoomHelper pinchToZoomHelper2 = PinchToZoomHelper.this;
            if (pinchToZoomHelper2.finishTransition == null && pinchToZoomHelper2.enterProgress != 1.0f) {
                PinchToZoomHelper.access$616(PinchToZoomHelper.this, 0.07272727f);
                if (PinchToZoomHelper.this.enterProgress > 1.0f) {
                    PinchToZoomHelper.this.enterProgress = 1.0f;
                } else {
                    PinchToZoomHelper.this.invalidateViews();
                }
            }
            float interpolation = PinchToZoomHelper.this.finishProgress * CubicBezierInterpolator.DEFAULT.getInterpolation(PinchToZoomHelper.this.enterProgress);
            float measuredHeight = getMeasuredHeight();
            if (interpolation == 1.0f || (clipBoundsListener = (pinchToZoomHelper = PinchToZoomHelper.this).clipBoundsListener) == null) {
                drawImage(canvas);
                super.dispatchDraw(canvas);
                f = measuredHeight;
                f2 = 0.0f;
            } else {
                clipBoundsListener.getClipTopBottom(pinchToZoomHelper.clipTopBottom);
                canvas.save();
                float f3 = 1.0f - interpolation;
                float f4 = PinchToZoomHelper.this.clipTopBottom[0] * f3;
                float measuredHeight2 = (getMeasuredHeight() * interpolation) + (PinchToZoomHelper.this.clipTopBottom[1] * f3);
                canvas.clipRect(0.0f, f4, getMeasuredWidth(), measuredHeight2);
                drawImage(canvas);
                super.dispatchDraw(canvas);
                canvas.restore();
                f2 = f4;
                f = measuredHeight2;
            }
            PinchToZoomHelper.this.drawOverlays(canvas, 1.0f - interpolation, PinchToZoomHelper.this.parentOffsetX - getLeft(), PinchToZoomHelper.this.parentOffsetY - getTop(), f2, f);
        }
    }

    public PinchToZoomHelper() {
        this.fullImage = new ImageReceiver();
        this.blurImage = new ImageReceiver();
        this.mediaSpoilerEffect = new SpoilerEffect();
        this.path = new Path();
        this.spoilerRadii = new float[8];
        this.clipTopBottom = new float[2];
        this.parentView = null;
        this.fragmentView = null;
        this.isSimple = true;
    }

    public PinchToZoomHelper(ViewGroup viewGroup, ViewGroup viewGroup2) {
        this.fullImage = new ImageReceiver();
        this.blurImage = new ImageReceiver();
        this.mediaSpoilerEffect = new SpoilerEffect();
        this.path = new Path();
        this.spoilerRadii = new float[8];
        this.clipTopBottom = new float[2];
        this.parentView = viewGroup;
        this.fragmentView = viewGroup2;
        this.isSimple = false;
    }

    static /* synthetic */ float access$1416(PinchToZoomHelper pinchToZoomHelper, float f) {
        float f2 = pinchToZoomHelper.progressToFullView + f;
        pinchToZoomHelper.progressToFullView = f2;
        return f2;
    }

    static /* synthetic */ float access$616(PinchToZoomHelper pinchToZoomHelper, float f) {
        float f2 = pinchToZoomHelper.enterProgress + f;
        pinchToZoomHelper.enterProgress = f2;
        return f2;
    }

    private boolean checkPointerIds(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() < 2) {
            return false;
        }
        if (this.pointerId1 == motionEvent.getPointerId(0) && this.pointerId2 == motionEvent.getPointerId(1)) {
            return true;
        }
        return this.pointerId1 == motionEvent.getPointerId(1) && this.pointerId2 == motionEvent.getPointerId(0);
    }

    private ColorMatrixColorFilter getFancyBlurFilter() {
        if (this.fancyBlurFilter == null) {
            ColorMatrix colorMatrix = new ColorMatrix();
            AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, 0.9f);
            AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, 0.6f);
            this.fancyBlurFilter = new ColorMatrixColorFilter(colorMatrix);
        }
        return this.fancyBlurFilter;
    }

    private ImageLocation getImageLocation(MessageObject messageObject, int[] iArr) {
        TLRPC.Message message = messageObject.messageOwner;
        if (!(message instanceof TLRPC.TL_messageService)) {
            TLRPC.MessageMedia messageMedia = message.media;
            if ((!(messageMedia instanceof TLRPC.TL_messageMediaPhoto) || messageMedia.photo == null) && (!(messageMedia instanceof TLRPC.TL_messageMediaWebPage) || messageMedia.webpage == null)) {
                if (messageMedia instanceof TLRPC.TL_messageMediaInvoice) {
                    return ImageLocation.getForWebFile(WebFile.createWithWebDocument(((TLRPC.TL_messageMediaInvoice) messageMedia).webPhoto));
                }
                if (messageObject.getDocument() != null) {
                    TLRPC.Document document = messageObject.getDocument();
                    if (MessageObject.isDocumentHasThumb(messageObject.getDocument())) {
                        TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
                        if (iArr != null) {
                            int i = closestPhotoSizeWithSize.size;
                            iArr[0] = i;
                            if (i == 0) {
                                iArr[0] = -1;
                            }
                        }
                        return ImageLocation.getForDocument(closestPhotoSizeWithSize, document);
                    }
                }
            } else {
                if (messageObject.isGif()) {
                    return ImageLocation.getForDocument(messageObject.getDocument());
                }
                TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize(), false, null, true);
                if (closestPhotoSizeWithSize2 != null) {
                    if (iArr != null) {
                        int i2 = closestPhotoSizeWithSize2.size;
                        iArr[0] = i2;
                        if (i2 == 0) {
                            iArr[0] = -1;
                        }
                    }
                    return ImageLocation.getForObject(closestPhotoSizeWithSize2, messageObject.photoThumbsObject);
                }
                if (iArr != null) {
                    iArr[0] = -1;
                }
            }
        } else {
            if (message.action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                return null;
            }
            TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
            if (closestPhotoSizeWithSize3 != null) {
                if (iArr != null) {
                    int i3 = closestPhotoSizeWithSize3.size;
                    iArr[0] = i3;
                    if (i3 == 0) {
                        iArr[0] = -1;
                    }
                }
                return ImageLocation.getForObject(closestPhotoSizeWithSize3, messageObject.photoThumbsObject);
            }
            if (iArr != null) {
                iArr[0] = -1;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishZoom$0(ValueAnimator valueAnimator) {
        this.finishProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidateViews();
    }

    private void setFullImage(MessageObject messageObject) {
        if (messageObject != null && messageObject.isPhoto()) {
            ImageLocation imageLocation = getImageLocation(messageObject, new int[1]);
            if (imageLocation != null) {
                this.fullImage.setImage(imageLocation, null, null, null, null, r0[0], null, messageObject, messageObject.isWebpage() ? 1 : 0);
                this.fullImage.setCrossfadeAlpha((byte) 2);
            }
            updateViewsLocation();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updateViewsLocation() {
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        for (View view = this.child; view != this.parentView; view = (View) view.getParent()) {
            if (view == null) {
                return false;
            }
            f3 += view.getLeft();
            f2 += view.getTop();
            if (!(view.getParent() instanceof View)) {
                break;
            }
        }
        float f4 = 0.0f;
        for (View view2 = this.child; view2 != this.fragmentView; view2 = (View) view2.getParent()) {
            if (view2 == null) {
                return false;
            }
            f += view2.getLeft();
            f4 += view2.getTop();
        }
        this.fragmentOffsetX = f;
        this.fragmentOffsetY = f4;
        this.parentOffsetX = f3;
        this.parentOffsetY = f2;
        return true;
    }

    public void applyTransform(Canvas canvas) {
        if (this.inOverlayMode) {
            canvas.save();
            float f = this.pinchScale;
            float f2 = this.finishProgress;
            float f3 = ((f * f2) + 1.0f) - f2;
            canvas.scale(f3, f3, this.parentOffsetX + this.pinchCenterX, this.parentOffsetY + this.pinchCenterY);
            float f4 = this.parentOffsetX;
            float f5 = this.pinchTranslationX;
            float f6 = this.finishProgress;
            canvas.translate(f4 + (f5 * f6), this.parentOffsetY + (this.pinchTranslationY * f6));
        }
    }

    public boolean checkPinchToZoom(MotionEvent motionEvent, View view, ImageReceiver imageReceiver, View view2, View view3, MessageObject messageObject) {
        return checkPinchToZoom(motionEvent, view, imageReceiver, view2, view3, messageObject, 0);
    }

    public boolean checkPinchToZoom(MotionEvent motionEvent, View view, ImageReceiver imageReceiver, View view2, View view3, MessageObject messageObject, int i) {
        if (!zoomEnabled(view, imageReceiver)) {
            return false;
        }
        if (motionEvent.getActionMasked() == 0 || motionEvent.getActionMasked() == 5) {
            if (!this.isInPinchToZoomTouchMode && motionEvent.getPointerCount() == 2) {
                this.pinchStartDistance = (float) Math.hypot(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0));
                float x = (motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f;
                this.pinchCenterX = x;
                this.pinchStartCenterX = x;
                float y = (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f;
                this.pinchCenterY = y;
                this.pinchStartCenterY = y;
                this.pinchScale = 1.0f;
                this.pointerId1 = motionEvent.getPointerId(0);
                this.pointerId2 = motionEvent.getPointerId(1);
                this.isInPinchToZoomTouchMode = true;
            }
        } else if (motionEvent.getActionMasked() == 2 && this.isInPinchToZoomTouchMode) {
            int i2 = -1;
            int i3 = -1;
            for (int i4 = 0; i4 < motionEvent.getPointerCount(); i4++) {
                if (this.pointerId1 == motionEvent.getPointerId(i4)) {
                    i2 = i4;
                }
                if (this.pointerId2 == motionEvent.getPointerId(i4)) {
                    i3 = i4;
                }
            }
            if (i2 == -1 || i3 == -1) {
                this.isInPinchToZoomTouchMode = false;
                view.getParent().requestDisallowInterceptTouchEvent(false);
                finishZoom();
                return false;
            }
            float hypot = ((float) Math.hypot(motionEvent.getX(i3) - motionEvent.getX(i2), motionEvent.getY(i3) - motionEvent.getY(i2))) / this.pinchStartDistance;
            this.pinchScale = hypot;
            if (hypot > 1.005f && !isInOverlayMode()) {
                this.pinchStartDistance = (float) Math.hypot(motionEvent.getX(i3) - motionEvent.getX(i2), motionEvent.getY(i3) - motionEvent.getY(i2));
                float x2 = (motionEvent.getX(i2) + motionEvent.getX(i3)) / 2.0f;
                this.pinchCenterX = x2;
                this.pinchStartCenterX = x2;
                float y2 = (motionEvent.getY(i2) + motionEvent.getY(i3)) / 2.0f;
                this.pinchCenterY = y2;
                this.pinchStartCenterY = y2;
                this.pinchScale = 1.0f;
                this.pinchTranslationX = 0.0f;
                this.pinchTranslationY = 0.0f;
                view.getParent().requestDisallowInterceptTouchEvent(true);
                startZoom(view, imageReceiver, view2, view3, messageObject, i);
            }
            float x3 = (motionEvent.getX(i2) + motionEvent.getX(i3)) / 2.0f;
            float y3 = (motionEvent.getY(i2) + motionEvent.getY(i3)) / 2.0f;
            float f = this.pinchStartCenterX - x3;
            float f2 = this.pinchStartCenterY - y3;
            float f3 = -f;
            float f4 = this.pinchScale;
            this.pinchTranslationX = f3 / f4;
            this.pinchTranslationY = (-f2) / f4;
            invalidateViews();
        } else if ((motionEvent.getActionMasked() == 1 || ((motionEvent.getActionMasked() == 6 && checkPointerIds(motionEvent)) || motionEvent.getActionMasked() == 3)) && this.isInPinchToZoomTouchMode) {
            this.isInPinchToZoomTouchMode = false;
            if (view != null && view.getParent() != null) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
            finishZoom();
        }
        return isInOverlayModeFor(view);
    }

    public void clear() {
        if (this.inOverlayMode) {
            Callback callback = this.callback;
            if (callback != null) {
                callback.onZoomFinished(this.messageObject);
            }
            this.inOverlayMode = false;
        }
        ZoomOverlayView zoomOverlayView = this.overlayView;
        if (zoomOverlayView != null && zoomOverlayView.getParent() != null) {
            this.parentView.removeView(this.overlayView);
            this.overlayView.backupImageView.getImageReceiver().clearImage();
            SpoilerEffect2 spoilerEffect2 = this.mediaSpoilerEffect2;
            if (spoilerEffect2 != null) {
                spoilerEffect2.detach(this.overlayView);
                this.mediaSpoilerEffect2 = null;
            }
            ImageReceiver imageReceiver = this.childImage;
            if (imageReceiver != null) {
                Drawable drawable = imageReceiver.getDrawable();
                if (drawable instanceof AnimatedFileDrawable) {
                    ((AnimatedFileDrawable) drawable).removeSecondParentView(this.overlayView);
                }
            }
        }
        View view = this.child;
        if (view != null) {
            view.invalidate();
            this.child = null;
        }
        ImageReceiver imageReceiver2 = this.childImage;
        if (imageReceiver2 != null) {
            imageReceiver2.onDetachedFromWindow();
            this.childImage.clearImage();
            this.childImage = null;
        }
        ImageReceiver imageReceiver3 = this.fullImage;
        if (imageReceiver3 != null) {
            imageReceiver3.onDetachedFromWindow();
            this.fullImage.clearImage();
            this.fullImage = null;
        }
        ImageReceiver imageReceiver4 = this.blurImage;
        if (imageReceiver4 != null) {
            imageReceiver4.onDetachedFromWindow();
            this.blurImage.clearImage();
            this.blurImage = null;
        }
        this.messageObject = null;
    }

    protected void drawOverlays(Canvas canvas, float f, float f2, float f3, float f4, float f5) {
    }

    public void finishZoom() {
        if (this.finishTransition == null && this.inOverlayMode) {
            if (!this.isSimple && !updateViewsLocation()) {
                clear();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            this.finishTransition = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.PinchToZoomHelper$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    PinchToZoomHelper.this.lambda$finishZoom$0(valueAnimator);
                }
            });
            this.finishTransition.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.PinchToZoomHelper.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    PinchToZoomHelper pinchToZoomHelper = PinchToZoomHelper.this;
                    if (pinchToZoomHelper.finishTransition != null) {
                        pinchToZoomHelper.finishTransition = null;
                        pinchToZoomHelper.clear();
                    }
                }
            });
            this.finishTransition.setDuration(220L);
            this.finishTransition.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.finishTransition.start();
        }
    }

    public View getChild() {
        return this.child;
    }

    public ImageReceiver getPhotoImage() {
        return this.childImage;
    }

    public Bitmap getVideoBitmap(int i, int i2) {
        ZoomOverlayView zoomOverlayView = this.overlayView;
        if (zoomOverlayView == null) {
            return null;
        }
        return zoomOverlayView.videoTextureView.getBitmap(i, i2);
    }

    protected void invalidateViews() {
        View view;
        if (this.isSimple && (view = this.child) != null) {
            view.invalidate();
        }
        ZoomOverlayView zoomOverlayView = this.overlayView;
        if (zoomOverlayView != null) {
            zoomOverlayView.invalidate();
        }
    }

    public boolean isInOverlayMode() {
        return this.inOverlayMode;
    }

    public boolean isInOverlayModeFor(View view) {
        return this.inOverlayMode && view == this.child;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!updateViewsLocation() || this.child == null) {
            return false;
        }
        motionEvent.offsetLocation(-this.fragmentOffsetX, -this.fragmentOffsetY);
        return this.child.onTouchEvent(motionEvent);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setClipBoundsListener(ClipBoundsListener clipBoundsListener) {
        this.clipBoundsListener = clipBoundsListener;
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x0218  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void startZoom(View view, ImageReceiver imageReceiver, View view2, View view3, MessageObject messageObject, int i) {
        ImageReceiver imageReceiver2;
        Drawable drawable;
        this.child = view;
        this.messageObject = messageObject;
        if (this.overlayView == null && !this.isSimple) {
            ZoomOverlayView zoomOverlayView = new ZoomOverlayView(this.parentView.getContext());
            this.overlayView = zoomOverlayView;
            zoomOverlayView.setFocusable(false);
            this.overlayView.setFocusableInTouchMode(false);
            this.overlayView.setEnabled(false);
        }
        if (this.fullImage == null) {
            ImageReceiver imageReceiver3 = new ImageReceiver();
            this.fullImage = imageReceiver3;
            imageReceiver3.setCrossfadeAlpha((byte) 2);
            this.fullImage.setCrossfadeWithOldImage(false);
            this.fullImage.onAttachedToWindow();
            ImageReceiver imageReceiver4 = new ImageReceiver();
            this.blurImage = imageReceiver4;
            imageReceiver4.setCrossfadeAlpha((byte) 2);
            this.blurImage.setCrossfadeWithOldImage(false);
            this.blurImage.onAttachedToWindow();
        }
        this.inOverlayMode = true;
        this.finishProgress = 1.0f;
        this.progressToFullView = 0.0f;
        if (!this.isSimple) {
            this.parentView.addView(this.overlayView);
            boolean z = (messageObject == null || !messageObject.hasMediaSpoilers() || messageObject.isMediaSpoilersRevealed) ? false : true;
            this.hasMediaSpoiler = z;
            if (z && this.mediaSpoilerEffect2 == null && SpoilerEffect2.supports()) {
                SpoilerEffect2 spoilerEffect2 = SpoilerEffect2.getInstance(this.overlayView);
                this.mediaSpoilerEffect2 = spoilerEffect2;
                if (spoilerEffect2 != null) {
                    spoilerEffect2.reassignAttach(this.overlayView, i);
                }
            }
            ColorMatrixColorFilter colorMatrixColorFilter = null;
            if (this.blurImage.getBitmap() != null) {
                this.blurImage.getBitmap().recycle();
                this.blurImage.setImageBitmap((Bitmap) null);
            }
            if (imageReceiver.getBitmap() == null || imageReceiver.getBitmap().isRecycled() || !this.hasMediaSpoiler) {
                imageReceiver2 = this.blurImage;
            } else {
                this.blurImage.setImageBitmap(Utilities.stackBlurBitmapMax(imageReceiver.getBitmap()));
                imageReceiver2 = this.blurImage;
                colorMatrixColorFilter = getFancyBlurFilter();
            }
            imageReceiver2.setColorFilter(colorMatrixColorFilter);
            setFullImage(messageObject);
            this.imageX = imageReceiver.getImageX();
            this.imageY = imageReceiver.getImageY();
            this.imageHeight = imageReceiver.getImageHeight();
            this.imageWidth = imageReceiver.getImageWidth();
            this.fullImageHeight = imageReceiver.getBitmapHeight();
            float bitmapWidth = imageReceiver.getBitmapWidth();
            this.fullImageWidth = bitmapWidth;
            float f = this.fullImageHeight;
            float f2 = f / bitmapWidth;
            float f3 = this.imageHeight;
            float f4 = this.imageWidth;
            float f5 = f3 / f4;
            if (f2 == f5) {
                this.fullImageHeight = f3;
            } else if (f2 < f5) {
                this.fullImageWidth = (bitmapWidth / f) * f3;
                this.fullImageHeight = f3;
                if (messageObject == null && messageObject.isVideo() && MediaController.getInstance().isPlayingMessage(messageObject)) {
                    this.isHardwareVideo = true;
                    MediaController.getInstance().setTextureView(this.overlayView.videoTextureView, this.overlayView.aspectRatioFrameLayout, this.overlayView.videoPlayerContainer, true);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.overlayView.videoPlayerContainer.getLayoutParams();
                    this.overlayView.videoPlayerContainer.setTag(R.id.parent_tag, imageReceiver);
                    if (layoutParams.width != imageReceiver.getImageWidth() || layoutParams.height != imageReceiver.getImageHeight()) {
                        this.overlayView.aspectRatioFrameLayout.setResizeMode(3);
                        layoutParams.width = (int) imageReceiver.getImageWidth();
                        layoutParams.height = (int) imageReceiver.getImageHeight();
                        this.overlayView.videoPlayerContainer.setLayoutParams(layoutParams);
                    }
                    this.overlayView.videoTextureView.setScaleX(1.0f);
                    this.overlayView.videoTextureView.setScaleY(1.0f);
                    if (this.callback != null) {
                        this.overlayView.backupImageView.setImageBitmap(this.callback.getCurrentTextureView().getBitmap((int) this.fullImageWidth, (int) this.fullImageHeight));
                        this.overlayView.backupImageView.setSize((int) this.fullImageWidth, (int) this.fullImageHeight);
                        this.overlayView.backupImageView.getImageReceiver().setRoundRadius(imageReceiver.getRoundRadius(true));
                    }
                    this.overlayView.videoPlayerContainer.setVisibility(0);
                } else {
                    this.isHardwareVideo = false;
                    ImageReceiver imageReceiver5 = new ImageReceiver();
                    this.childImage = imageReceiver5;
                    this.childTextureViewContainer = view2;
                    this.childTextureView = view3;
                    imageReceiver5.onAttachedToWindow();
                    drawable = imageReceiver.getDrawable();
                    this.childImage.setImageBitmap(drawable);
                    if (drawable instanceof AnimatedFileDrawable) {
                        AnimatedFileDrawable animatedFileDrawable = (AnimatedFileDrawable) drawable;
                        animatedFileDrawable.addSecondParentView(this.overlayView);
                        animatedFileDrawable.setInvalidateParentViewWithSecond(true);
                    }
                    this.childImage.setImageCoords(this.imageX, this.imageY, this.imageWidth, this.imageHeight);
                    this.childImage.setAspectFit(imageReceiver.isAspectFit());
                    this.childImage.setRoundRadius(imageReceiver.getRoundRadius(true));
                    this.fullImage.setRoundRadius(imageReceiver.getRoundRadius(true));
                    this.fullImage.setAspectFit(imageReceiver.isAspectFit());
                    this.overlayView.videoPlayerContainer.setVisibility(8);
                }
            } else {
                this.fullImageHeight = f2 * f4;
            }
            this.fullImageWidth = f4;
            if (messageObject == null) {
            }
            this.isHardwareVideo = false;
            ImageReceiver imageReceiver52 = new ImageReceiver();
            this.childImage = imageReceiver52;
            this.childTextureViewContainer = view2;
            this.childTextureView = view3;
            imageReceiver52.onAttachedToWindow();
            drawable = imageReceiver.getDrawable();
            this.childImage.setImageBitmap(drawable);
            if (drawable instanceof AnimatedFileDrawable) {
            }
            this.childImage.setImageCoords(this.imageX, this.imageY, this.imageWidth, this.imageHeight);
            this.childImage.setAspectFit(imageReceiver.isAspectFit());
            this.childImage.setRoundRadius(imageReceiver.getRoundRadius(true));
            this.fullImage.setRoundRadius(imageReceiver.getRoundRadius(true));
            this.fullImage.setAspectFit(imageReceiver.isAspectFit());
            this.overlayView.videoPlayerContainer.setVisibility(8);
        }
        Callback callback = this.callback;
        if (callback != null) {
            callback.onZoomStarted(messageObject);
        }
        this.enterProgress = 0.0f;
    }

    protected boolean zoomEnabled(View view, ImageReceiver imageReceiver) {
        if (this.isSimple) {
            return true;
        }
        return imageReceiver.getDrawable() instanceof AnimatedFileDrawable ? !((AnimatedFileDrawable) imageReceiver.getDrawable()).isLoadingStream() : imageReceiver.hasNotThumbOrOnlyStaticThumb();
    }
}
