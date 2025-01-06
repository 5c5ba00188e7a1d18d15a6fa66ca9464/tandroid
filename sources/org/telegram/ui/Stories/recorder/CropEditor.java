package org.telegram.ui.Stories.recorder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.BubbleActivity;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.Crop.CropRotationWheel;
import org.telegram.ui.Components.Crop.CropTransform;
import org.telegram.ui.Components.Crop.CropView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;

/* loaded from: classes5.dex */
public abstract class CropEditor extends FrameLayout {
    private final AnimatedFloat animatedMirror;
    private final AnimatedFloat animatedOrientation;
    private float appearProgress;
    public boolean applied;
    public final FrameLayout buttonsLayout;
    public final TextView cancelButton;
    public boolean closing;
    public final ContentView contentView;
    public final FrameLayout controlsLayout;
    public final TextView cropButton;
    private final CropTransform cropTransform;
    public final CropView cropView;
    private StoryEntry entry;
    private int lastOrientation;
    private final int[] previewLocation;
    private final PreviewView previewView;
    public final TextView resetButton;
    private final Theme.ResourcesProvider resourcesProvider;
    private final int[] thisLocation;
    public final CropRotationWheel wheel;

    public class ContentView extends View {
        private final Matrix clipMatrix;
        private final Matrix cropMatrix;
        private final Paint dimPaint;
        private final Matrix identityMatrix;
        private final Matrix invertedClipMatrix;
        private final Matrix matrix;
        private final Path previewClipPath;
        private final RectF previewClipRect;
        private final Matrix previewMatrix;

        public ContentView(Context context) {
            super(context);
            this.dimPaint = new Paint(1);
            this.previewClipPath = new Path();
            this.previewClipRect = new RectF();
            this.identityMatrix = new Matrix();
            this.matrix = new Matrix();
            this.previewMatrix = new Matrix();
            this.cropMatrix = new Matrix();
            this.clipMatrix = new Matrix();
            this.invertedClipMatrix = new Matrix();
        }

        /* JADX WARN: Code restructure failed: missing block: B:38:0x0127, code lost:
        
            if (r13 != false) goto L50;
         */
        /* JADX WARN: Code restructure failed: missing block: B:46:0x00a1, code lost:
        
            if (r13 != false) goto L27;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void applyCrop(Matrix matrix, boolean z) {
            int currentWidth = CropEditor.this.getCurrentWidth();
            int currentHeight = CropEditor.this.getCurrentHeight();
            int orientation = CropEditor.this.cropTransform.getOrientation();
            if (orientation == 90 || orientation == 270) {
                currentHeight = currentWidth;
                currentWidth = currentHeight;
            }
            float f = 1.0f;
            float f2 = 0.0f;
            float trueCropScale = ((CropEditor.this.cropTransform.getTrueCropScale() - 1.0f) * (z ? 1.0f : 0.0f)) + 1.0f;
            float f3 = currentWidth;
            float containerWidth = getContainerWidth() / f3;
            float f4 = currentHeight;
            if (containerWidth * f4 > getContainerHeight()) {
                containerWidth = getContainerHeight() / f4;
            }
            boolean z2 = (CropEditor.this.entry.orientation / 90) % 2 == 1;
            matrix.preTranslate(CropEditor.this.cropTransform.getCropAreaX(), CropEditor.this.cropTransform.getCropAreaY());
            float scale = (CropEditor.this.cropTransform.getScale() / trueCropScale) * containerWidth;
            if (CropEditor.this.entry != null && CropEditor.this.entry.crop != null) {
                if (z) {
                    f = CropEditor.this.entry.crop.cropScale;
                }
                f = scale;
            }
            matrix.preScale(f, f);
            float cropPx = CropEditor.this.cropTransform.getCropPx();
            float cropPy = CropEditor.this.cropTransform.getCropPy();
            CropEditor cropEditor = CropEditor.this;
            if (cropEditor.closing && z) {
                if (cropEditor.entry.crop == null) {
                    cropPx = 0.0f;
                } else {
                    MediaController.CropState cropState = CropEditor.this.entry.crop;
                    cropPx = !z2 ? cropState.cropPx : cropState.cropPy;
                }
                cropPy = CropEditor.this.entry.crop == null ? 0.0f : !z2 ? CropEditor.this.entry.crop.cropPy : CropEditor.this.entry.crop.cropPx;
            }
            matrix.preTranslate(cropPx * f3, cropPy * f4);
            float rotation = CropEditor.this.entry.orientation + CropEditor.this.cropTransform.getRotation() + orientation;
            if (CropEditor.this.entry.crop != null) {
                if (z) {
                    f2 = CropEditor.this.entry.crop.cropRotate + CropEditor.this.entry.crop.transformRotation;
                }
                f2 = rotation;
            }
            matrix.preRotate(f2);
        }

        private float getContainerHeight() {
            return ((getHeight() - ((Build.VERSION.SDK_INT < 21 || (getContext() instanceof BubbleActivity)) ? 0 : AndroidUtilities.statusBarHeight)) - CropEditor.this.cropView.bottomPadding) - AndroidUtilities.dp(32.0f);
        }

        private float getContainerWidth() {
            return getWidth() - AndroidUtilities.dp(32.0f);
        }

        @Override // android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (CropEditor.this.entry == null) {
                return;
            }
            drawImage(canvas, false);
        }

        public void drawImage(Canvas canvas, boolean z) {
            boolean z2 = true;
            if (z) {
                if (CropEditor.this.appearProgress >= 1.0f) {
                    return;
                }
                canvas.saveLayerAlpha(0.0f, 0.0f, CropEditor.this.previewView.getWidth(), CropEditor.this.previewView.getHeight(), (int) (Math.min(1.0f, (1.0f - CropEditor.this.appearProgress) * 2.0f) * 255.0f), 31);
                canvas.translate(CropEditor.this.thisLocation[0] - CropEditor.this.previewLocation[0], CropEditor.this.thisLocation[1] - CropEditor.this.previewLocation[1]);
            }
            canvas.save();
            this.dimPaint.setColor(-16777216);
            this.dimPaint.setAlpha((int) (CropEditor.this.appearProgress * 255.0f));
            canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), this.dimPaint);
            if (CropEditor.this.appearProgress < 1.0f && !z) {
                this.previewClipPath.rewind();
                this.previewClipRect.set(0.0f, 0.0f, CropEditor.this.previewView.getWidth(), CropEditor.this.previewView.getHeight());
                this.previewClipRect.offset(CropEditor.this.previewLocation[0], CropEditor.this.previewLocation[1]);
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getWidth(), getHeight());
                AndroidUtilities.lerp(this.previewClipRect, rectF, CropEditor.this.appearProgress, this.previewClipRect);
                float lerp = AndroidUtilities.lerp(AndroidUtilities.dp(12.0f), 0, CropEditor.this.appearProgress);
                this.previewClipPath.addRoundRect(this.previewClipRect, lerp, lerp, Path.Direction.CW);
                canvas.clipPath(this.previewClipPath);
            }
            float unused = CropEditor.this.appearProgress;
            float f = CropEditor.this.appearProgress;
            this.previewMatrix.reset();
            this.cropMatrix.reset();
            this.previewMatrix.preTranslate(-CropEditor.this.thisLocation[0], -CropEditor.this.thisLocation[1]);
            this.previewMatrix.preTranslate(CropEditor.this.previewLocation[0], CropEditor.this.previewLocation[1]);
            this.previewMatrix.preScale(CropEditor.this.previewView.getWidth() / CropEditor.this.entry.resultWidth, CropEditor.this.previewView.getHeight() / CropEditor.this.entry.resultHeight);
            this.previewMatrix.preConcat(CropEditor.this.entry.matrix);
            this.previewMatrix.preTranslate(CropEditor.this.previewView.getContentWidth() / 2.0f, CropEditor.this.previewView.getContentHeight() / 2.0f);
            this.cropMatrix.preTranslate(AndroidUtilities.dp(16.0f) + (getContainerWidth() / 2.0f), ((Build.VERSION.SDK_INT < 21 || (getContext() instanceof BubbleActivity)) ? 0 : AndroidUtilities.statusBarHeight) + ((getContainerHeight() + AndroidUtilities.dp(32.0f)) / 2.0f));
            if (z) {
                AndroidUtilities.lerp(this.previewMatrix, this.identityMatrix, CropEditor.this.appearProgress, this.clipMatrix);
                this.clipMatrix.preRotate(-CropEditor.this.entry.orientation);
                if (this.clipMatrix.invert(this.invertedClipMatrix)) {
                    boolean z3 = ((CropEditor.this.entry.orientation + (CropEditor.this.entry.crop != null ? CropEditor.this.entry.crop.transformRotation : 0)) / 90) % 2 == 1;
                    float contentWidth = CropEditor.this.previewView.getContentWidth();
                    float contentHeight = CropEditor.this.previewView.getContentHeight();
                    float f2 = CropEditor.this.entry.crop != null ? CropEditor.this.entry.crop.cropPw : 1.0f;
                    float f3 = CropEditor.this.entry.crop != null ? CropEditor.this.entry.crop.cropPh : 1.0f;
                    float f4 = ((z3 ? contentHeight : contentWidth) * f2) / 2.0f;
                    if (!z3) {
                        contentWidth = contentHeight;
                    }
                    float f5 = (contentWidth * f3) / 2.0f;
                    float lerp2 = AndroidUtilities.lerp(1.0f, 4.0f, f);
                    canvas.concat(this.clipMatrix);
                    canvas.clipRect((-f4) * lerp2, (-f5) * lerp2, f4 * lerp2, f5 * lerp2);
                    canvas.concat(this.invertedClipMatrix);
                }
            }
            applyCrop(this.previewMatrix, true);
            applyCrop(this.cropMatrix, false);
            AnimatedFloat animatedFloat = CropEditor.this.animatedMirror;
            CropEditor cropEditor = CropEditor.this;
            if (!cropEditor.closing) {
                z2 = cropEditor.cropView.isMirrored();
            } else if (cropEditor.entry.crop == null || !CropEditor.this.entry.crop.mirrored) {
                z2 = false;
            }
            float f6 = animatedFloat.set(z2);
            float f7 = 1.0f - (f6 * 2.0f);
            this.cropMatrix.preScale(f7, 1.0f);
            this.previewMatrix.preScale(f7, 1.0f);
            float f8 = 4.0f * f6 * (1.0f - f6) * 0.25f;
            this.cropMatrix.preSkew(0.0f, f8);
            this.previewMatrix.preSkew(0.0f, f8);
            this.cropMatrix.preTranslate((-CropEditor.this.previewView.getContentWidth()) / 2.0f, (-CropEditor.this.previewView.getContentHeight()) / 2.0f);
            this.previewMatrix.preTranslate((-CropEditor.this.previewView.getContentWidth()) / 2.0f, (-CropEditor.this.previewView.getContentHeight()) / 2.0f);
            AndroidUtilities.lerp(this.previewMatrix, this.cropMatrix, CropEditor.this.appearProgress, this.matrix);
            canvas.concat(this.matrix);
            CropEditor.this.previewView.drawContent(canvas);
            canvas.restore();
            if (z) {
                canvas.restore();
            }
        }
    }

    public CropEditor(Context context, PreviewView previewView, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.lastOrientation = 0;
        this.appearProgress = 0.0f;
        this.thisLocation = new int[2];
        this.previewLocation = new int[2];
        this.cropTransform = new CropTransform();
        this.previewView = previewView;
        this.resourcesProvider = resourcesProvider;
        ContentView contentView = new ContentView(context);
        this.contentView = contentView;
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.animatedMirror = new AnimatedFloat(contentView, 0L, 320L, cubicBezierInterpolator);
        this.animatedOrientation = new AnimatedFloat(this, 0L, 360L, cubicBezierInterpolator);
        CropView cropView = new CropView(context) { // from class: org.telegram.ui.Stories.recorder.CropEditor.1
            @Override // org.telegram.ui.Components.Crop.CropView
            public int getCurrentHeight() {
                return CropEditor.this.getCurrentHeight();
            }

            @Override // org.telegram.ui.Components.Crop.CropView
            public int getCurrentWidth() {
                return CropEditor.this.getCurrentWidth();
            }
        };
        this.cropView = cropView;
        cropView.setListener(new CropView.CropViewListener() { // from class: org.telegram.ui.Stories.recorder.CropEditor.2
            @Override // org.telegram.ui.Components.Crop.CropView.CropViewListener
            public void onAspectLock(boolean z) {
            }

            @Override // org.telegram.ui.Components.Crop.CropView.CropViewListener
            public void onChange(boolean z) {
            }

            @Override // org.telegram.ui.Components.Crop.CropView.CropViewListener
            public void onTapUp() {
            }

            @Override // org.telegram.ui.Components.Crop.CropView.CropViewListener
            public void onUpdate() {
                CropEditor.this.contentView.invalidate();
            }
        });
        addView(cropView);
        FrameLayout frameLayout = new FrameLayout(context);
        this.controlsLayout = frameLayout;
        addView(frameLayout, LayoutHelper.createFrame(-1, -1, 119));
        CropRotationWheel cropRotationWheel = new CropRotationWheel(context);
        this.wheel = cropRotationWheel;
        cropRotationWheel.setListener(new CropRotationWheel.RotationWheelListener() { // from class: org.telegram.ui.Stories.recorder.CropEditor.3
            @Override // org.telegram.ui.Components.Crop.CropRotationWheel.RotationWheelListener
            public void aspectRatioPressed() {
                CropEditor.this.cropView.showAspectRatioDialog();
            }

            @Override // org.telegram.ui.Components.Crop.CropRotationWheel.RotationWheelListener
            public boolean mirror() {
                CropEditor.this.contentView.invalidate();
                return CropEditor.this.cropView.mirror();
            }

            @Override // org.telegram.ui.Components.Crop.CropRotationWheel.RotationWheelListener
            public void onChange(float f) {
                CropEditor.this.cropView.setRotation(f);
            }

            @Override // org.telegram.ui.Components.Crop.CropRotationWheel.RotationWheelListener
            public void onEnd(float f) {
                CropEditor.this.cropView.onRotationEnded();
            }

            @Override // org.telegram.ui.Components.Crop.CropRotationWheel.RotationWheelListener
            public void onStart() {
                CropEditor.this.cropView.onRotationBegan();
            }

            @Override // org.telegram.ui.Components.Crop.CropRotationWheel.RotationWheelListener
            public boolean rotate90Pressed() {
                boolean rotate = CropEditor.this.cropView.rotate(-90.0f);
                CropEditor.this.cropView.maximize(true);
                CropEditor.this.contentView.invalidate();
                return rotate;
            }
        });
        frameLayout.addView(cropRotationWheel, LayoutHelper.createFrame(-1, -2.0f, 81, 0.0f, 0.0f, 0.0f, 52.0f));
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.buttonsLayout = frameLayout2;
        frameLayout.addView(frameLayout2, LayoutHelper.createFrame(-1, 52.0f, 80, 0.0f, 0.0f, 0.0f, 0.0f));
        TextView textView = new TextView(context);
        this.cancelButton = textView;
        textView.setTextSize(1, 14.0f);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setBackground(Theme.createSelectorDrawable(-12763843, 0));
        textView.setTextColor(-1);
        textView.setText(LocaleController.getString(R.string.Cancel));
        textView.setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
        frameLayout2.addView(textView, LayoutHelper.createFrame(-2, -1, 115));
        textView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.recorder.CropEditor$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CropEditor.this.lambda$new$0(view);
            }
        });
        TextView textView2 = new TextView(context);
        this.resetButton = textView2;
        textView2.setTextSize(1, 14.0f);
        textView2.setTypeface(AndroidUtilities.bold());
        textView2.setBackground(Theme.createSelectorDrawable(-12763843, 0));
        textView2.setTextColor(-1);
        textView2.setText(LocaleController.getString(R.string.CropReset));
        textView2.setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
        frameLayout2.addView(textView2, LayoutHelper.createFrame(-2, -1, 113));
        textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.recorder.CropEditor$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CropEditor.this.lambda$new$1(view);
            }
        });
        TextView textView3 = new TextView(context);
        this.cropButton = textView3;
        textView3.setTextSize(1, 14.0f);
        textView3.setTypeface(AndroidUtilities.bold());
        textView3.setBackground(Theme.createSelectorDrawable(-12763843, 0));
        textView3.setTextColor(-15098625);
        textView3.setText(LocaleController.getString(R.string.StoryCrop));
        textView3.setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
        frameLayout2.addView(textView3, LayoutHelper.createFrame(-2, -1, 117));
        textView3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.recorder.CropEditor$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CropEditor.this.lambda$new$2(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentHeight() {
        StoryEntry storyEntry = this.entry;
        if (storyEntry == null) {
            return 1;
        }
        int i = storyEntry.orientation;
        return (i == 90 || i == 270) ? this.previewView.getContentWidth() : this.previewView.getContentHeight();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentWidth() {
        StoryEntry storyEntry = this.entry;
        if (storyEntry == null) {
            return 1;
        }
        int i = storyEntry.orientation;
        return (i == 90 || i == 270) ? this.previewView.getContentHeight() : this.previewView.getContentWidth();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        this.cropView.reset(true);
        this.wheel.setRotated(false);
        this.wheel.setMirrored(false);
        this.wheel.setRotation(0.0f, true);
        this.contentView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view) {
        apply();
        close();
    }

    public void apply() {
        StoryEntry storyEntry = this.entry;
        if (storyEntry == null) {
            return;
        }
        this.applied = true;
        storyEntry.crop = new MediaController.CropState();
        this.cropView.applyToCropState(this.entry.crop);
        StoryEntry storyEntry2 = this.entry;
        storyEntry2.crop.orientation = storyEntry2.orientation;
    }

    protected abstract void close();

    public void disappearStarts() {
        this.previewView.setCropEditorDrawing(this);
        this.closing = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    public float getAppearProgress() {
        return this.appearProgress;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.cropView.setBottomPadding(this.controlsLayout.getPaddingBottom() + AndroidUtilities.dp(116.0f));
        super.onLayout(z, i, i2, i3, i4);
    }

    public void setAppearProgress(float f) {
        if (Math.abs(this.appearProgress - f) < 0.001f) {
            return;
        }
        this.appearProgress = f;
        this.contentView.setAlpha(f);
        this.contentView.invalidate();
        this.cropView.areaView.setDimAlpha(0.5f * f);
        this.cropView.areaView.setFrameAlpha(f);
        this.cropView.areaView.invalidate();
        this.previewView.invalidate();
    }

    public void setEntry(StoryEntry storyEntry) {
        if (storyEntry == null) {
            return;
        }
        this.entry = storyEntry;
        this.applied = false;
        this.closing = false;
        this.cropView.onShow();
        getLocationOnScreen(this.thisLocation);
        this.previewView.getLocationOnScreen(this.previewLocation);
        MediaController.CropState cropState = storyEntry.crop;
        if (cropState == null) {
            cropState = null;
        }
        this.cropView.start(storyEntry.orientation, true, false, this.cropTransform, cropState);
        this.wheel.setRotation(this.cropView.getRotation());
        if (cropState != null) {
            this.wheel.setRotation(cropState.cropRotate, false);
            this.wheel.setRotated(cropState.transformRotation != 0);
            this.wheel.setMirrored(cropState.mirrored);
            this.animatedMirror.set(cropState.mirrored, false);
        } else {
            this.wheel.setRotation(0.0f, false);
            this.wheel.setRotated(false);
            this.wheel.setMirrored(false);
            this.animatedMirror.set(false, false);
        }
        this.cropView.updateMatrix();
        this.animatedOrientation.set(((this.lastOrientation / 360) * 360) + this.cropTransform.getOrientation(), true);
        this.contentView.setVisibility(0);
        this.contentView.invalidate();
        this.previewView.setCropEditorDrawing(this);
    }

    public void stop() {
        this.entry = null;
        this.cropView.stop();
        this.cropView.onHide();
        this.contentView.setVisibility(8);
        this.previewView.setCropEditorDrawing(null);
    }
}
