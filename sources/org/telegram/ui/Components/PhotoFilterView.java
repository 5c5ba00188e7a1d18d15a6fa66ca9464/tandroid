package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.InputSerializedData;
import org.telegram.tgnet.OutputSerializedData;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.BubbleActivity;
import org.telegram.ui.Cells.PhotoEditRadioCell;
import org.telegram.ui.Cells.PhotoEditToolCell;
import org.telegram.ui.Components.BlurringShader;
import org.telegram.ui.Components.FilterShaders;
import org.telegram.ui.Components.PhotoEditorSeekBar;
import org.telegram.ui.Components.PhotoFilterBlurControl;
import org.telegram.ui.Components.PhotoFilterCurvesControl;
import org.telegram.ui.Components.PhotoFilterView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.VideoEditTextureView;
import org.telegram.ui.Stories.recorder.StoryRecorder;

/* loaded from: classes3.dex */
public class PhotoFilterView extends FrameLayout implements FilterShaders.FilterShadersDelegate, StoryRecorder.Touchable {
    private Bitmap bitmapMask;
    private Bitmap bitmapToEdit;
    private float blurAngle;
    private PhotoFilterBlurControl blurControl;
    private float blurExcludeBlurSize;
    private Point blurExcludePoint;
    private float blurExcludeSize;
    private ImageView blurItem;
    private FrameLayout blurLayout;
    private TextView blurLinearButton;
    private TextView blurOffButton;
    private TextView blurRadialButton;
    private int blurType;
    private TextView cancelTextView;
    private int contrastTool;
    private float contrastValue;
    private ImageView curveItem;
    private FrameLayout curveLayout;
    private RadioButton[] curveRadioButton;
    private PhotoFilterCurvesControl curvesControl;
    private CurvesToolValue curvesToolValue;
    private TextView doneTextView;
    private FilterGLThread eglThread;
    private int enhanceTool;
    private float enhanceValue;
    private int exposureTool;
    private float exposureValue;
    private int fadeTool;
    private float fadeValue;
    private boolean filtersEmpty;
    private int gradientBottom;
    private int gradientTop;
    private int grainTool;
    private float grainValue;
    private int highlightsTool;
    private float highlightsValue;
    private boolean inBubbleMode;
    private boolean isMirrored;
    private MediaController.SavedFilterState lastState;
    private final Matrix maskMatrix;
    private final Paint maskPaint;
    private final android.graphics.Rect maskRect;
    private int orientation;
    private boolean ownLayout;
    private boolean ownsTextureView;
    private PaintingOverlay paintingOverlay;
    private RecyclerListView recyclerListView;
    private final Theme.ResourcesProvider resourcesProvider;
    private int rowsCount;
    private int saturationTool;
    private float saturationValue;
    private int selectedTool;
    private int shadowsTool;
    private float shadowsValue;
    private int sharpenTool;
    private float sharpenValue;
    private boolean showOriginal;
    private int softenSkinTool;
    private float softenSkinValue;
    private TextureView textureView;
    private int tintHighlightsColor;
    private int tintHighlightsTool;
    private int tintShadowsColor;
    private int tintShadowsTool;
    private FrameLayout toolsView;
    private ImageView tuneItem;
    private int vignetteTool;
    private float vignetteValue;
    private int warmthTool;
    private float warmthValue;

    class 2 implements TextureView.SurfaceTextureListener {
        final /* synthetic */ BlurringShader.BlurManager val$blurManager;
        final /* synthetic */ boolean val$ownLayout;

        2(boolean z, BlurringShader.BlurManager blurManager) {
            this.val$ownLayout = z;
            this.val$blurManager = blurManager;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSurfaceTextureSizeChanged$0() {
            if (PhotoFilterView.this.eglThread != null) {
                PhotoFilterView.this.eglThread.requestRender(false, true, false);
            }
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
            if (PhotoFilterView.this.eglThread != null || surfaceTexture == null) {
                return;
            }
            PhotoFilterView.this.eglThread = new FilterGLThread(surfaceTexture, PhotoFilterView.this.bitmapToEdit, PhotoFilterView.this.orientation, PhotoFilterView.this.isMirrored, null, this.val$ownLayout, this.val$blurManager, i, i2);
            if (!this.val$ownLayout) {
                PhotoFilterView.this.eglThread.updateUiBlurGradient(PhotoFilterView.this.gradientTop, PhotoFilterView.this.gradientBottom);
                PhotoFilterView.this.eglThread.updateUiBlurTransform(PhotoFilterView.this.textureView.getTransform(null), PhotoFilterView.this.textureView.getWidth(), PhotoFilterView.this.textureView.getHeight());
            }
            PhotoFilterView.this.eglThread.setFilterGLThreadDelegate(PhotoFilterView.this);
            PhotoFilterView.this.eglThread.setSurfaceTextureSize(i, i2);
            PhotoFilterView.this.eglThread.requestRender(true, true, false);
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            if (PhotoFilterView.this.eglThread == null) {
                return true;
            }
            PhotoFilterView.this.eglThread.shutdown();
            PhotoFilterView.this.eglThread = null;
            return true;
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            if (PhotoFilterView.this.eglThread != null) {
                PhotoFilterView.this.eglThread.setSurfaceTextureSize(i, i2);
                PhotoFilterView.this.eglThread.requestRender(false, true, false);
                PhotoFilterView.this.eglThread.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.PhotoFilterView$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PhotoFilterView.2.this.lambda$onSurfaceTextureSizeChanged$0();
                    }
                });
            }
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }
    }

    public static class CurvesToolValue {
        public int activeType;
        public ByteBuffer curveBuffer;
        public CurvesValue luminanceCurve = new CurvesValue();
        public CurvesValue redCurve = new CurvesValue();
        public CurvesValue greenCurve = new CurvesValue();
        public CurvesValue blueCurve = new CurvesValue();

        public CurvesToolValue() {
            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(800);
            this.curveBuffer = allocateDirect;
            allocateDirect.order(ByteOrder.LITTLE_ENDIAN);
        }

        public void fillBuffer() {
            this.curveBuffer.position(0);
            float[] dataPoints = this.luminanceCurve.getDataPoints();
            float[] dataPoints2 = this.redCurve.getDataPoints();
            float[] dataPoints3 = this.greenCurve.getDataPoints();
            float[] dataPoints4 = this.blueCurve.getDataPoints();
            for (int i = 0; i < 200; i++) {
                this.curveBuffer.put((byte) (dataPoints2[i] * 255.0f));
                this.curveBuffer.put((byte) (dataPoints3[i] * 255.0f));
                this.curveBuffer.put((byte) (dataPoints4[i] * 255.0f));
                this.curveBuffer.put((byte) (dataPoints[i] * 255.0f));
            }
            this.curveBuffer.position(0);
        }

        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.luminanceCurve.readParams(inputSerializedData, z);
            this.redCurve.readParams(inputSerializedData, z);
            this.greenCurve.readParams(inputSerializedData, z);
            this.blueCurve.readParams(inputSerializedData, z);
        }

        public void serializeToStream(OutputSerializedData outputSerializedData) {
            this.luminanceCurve.serializeToStream(outputSerializedData);
            this.redCurve.serializeToStream(outputSerializedData);
            this.greenCurve.serializeToStream(outputSerializedData);
            this.blueCurve.serializeToStream(outputSerializedData);
        }

        public boolean shouldBeSkipped() {
            return this.luminanceCurve.isDefault() && this.redCurve.isDefault() && this.greenCurve.isDefault() && this.blueCurve.isDefault();
        }
    }

    public static class CurvesValue {
        public float[] cachedDataPoints;
        public float blacksLevel = 0.0f;
        public float shadowsLevel = 25.0f;
        public float midtonesLevel = 50.0f;
        public float highlightsLevel = 75.0f;
        public float whitesLevel = 100.0f;
        public float previousBlacksLevel = 0.0f;
        public float previousShadowsLevel = 25.0f;
        public float previousMidtonesLevel = 50.0f;
        public float previousHighlightsLevel = 75.0f;
        public float previousWhitesLevel = 100.0f;

        public float[] getDataPoints() {
            if (this.cachedDataPoints == null) {
                interpolateCurve();
            }
            return this.cachedDataPoints;
        }

        public float[] interpolateCurve() {
            float f = this.blacksLevel / 100.0f;
            float f2 = this.shadowsLevel / 100.0f;
            float f3 = this.midtonesLevel / 100.0f;
            float f4 = this.highlightsLevel / 100.0f;
            float f5 = this.whitesLevel / 100.0f;
            int i = 5;
            float f6 = 0.5f;
            float[] fArr = {-0.001f, f, 0.0f, f, 0.25f, f2, 0.5f, f3, 0.75f, f4, 1.0f, f5, 1.001f, f5};
            ArrayList arrayList = new ArrayList(100);
            ArrayList arrayList2 = new ArrayList(100);
            arrayList2.add(Float.valueOf(fArr[0]));
            arrayList2.add(Float.valueOf(fArr[1]));
            int i2 = 1;
            while (i2 < i) {
                int i3 = (i2 - 1) * 2;
                float f7 = fArr[i3];
                float f8 = fArr[i3 + 1];
                int i4 = i2 * 2;
                float f9 = fArr[i4];
                float f10 = fArr[i4 + 1];
                int i5 = i2 + 1;
                int i6 = i5 * 2;
                float f11 = fArr[i6];
                float f12 = fArr[i6 + 1];
                int i7 = (i2 + 2) * 2;
                float f13 = fArr[i7];
                float f14 = fArr[i7 + 1];
                int i8 = 1;
                while (i8 < 100) {
                    float f15 = i8 * 0.01f;
                    float f16 = f15 * f15;
                    float f17 = f16 * f15;
                    float f18 = ((f9 * 2.0f) + ((f11 - f7) * f15) + (((((f7 * 2.0f) - (f9 * 5.0f)) + (f11 * 4.0f)) - f13) * f16) + (((((f9 * 3.0f) - f7) - (f11 * 3.0f)) + f13) * f17)) * f6;
                    float max = Math.max(0.0f, Math.min(1.0f, ((f10 * 2.0f) + ((f12 - f8) * f15) + (((((2.0f * f8) - (5.0f * f10)) + (4.0f * f12)) - f14) * f16) + (((((f10 * 3.0f) - f8) - (3.0f * f12)) + f14) * f17)) * f6));
                    if (f18 > f7) {
                        arrayList2.add(Float.valueOf(f18));
                        arrayList2.add(Float.valueOf(max));
                    }
                    if ((i8 - 1) % 2 == 0) {
                        arrayList.add(Float.valueOf(max));
                    }
                    i8++;
                    f6 = 0.5f;
                }
                arrayList2.add(Float.valueOf(f11));
                arrayList2.add(Float.valueOf(f12));
                i2 = i5;
                i = 5;
                f6 = 0.5f;
            }
            arrayList2.add(Float.valueOf(fArr[12]));
            arrayList2.add(Float.valueOf(fArr[13]));
            this.cachedDataPoints = new float[arrayList.size()];
            int i9 = 0;
            while (true) {
                float[] fArr2 = this.cachedDataPoints;
                if (i9 >= fArr2.length) {
                    break;
                }
                fArr2[i9] = ((Float) arrayList.get(i9)).floatValue();
                i9++;
            }
            int size = arrayList2.size();
            float[] fArr3 = new float[size];
            for (int i10 = 0; i10 < size; i10++) {
                fArr3[i10] = ((Float) arrayList2.get(i10)).floatValue();
            }
            return fArr3;
        }

        public boolean isDefault() {
            return ((double) Math.abs(this.blacksLevel - 0.0f)) < 1.0E-5d && ((double) Math.abs(this.shadowsLevel - 25.0f)) < 1.0E-5d && ((double) Math.abs(this.midtonesLevel - 50.0f)) < 1.0E-5d && ((double) Math.abs(this.highlightsLevel - 75.0f)) < 1.0E-5d && ((double) Math.abs(this.whitesLevel - 100.0f)) < 1.0E-5d;
        }

        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            float readFloat = inputSerializedData.readFloat(z);
            this.previousBlacksLevel = readFloat;
            this.blacksLevel = readFloat;
            float readFloat2 = inputSerializedData.readFloat(z);
            this.previousShadowsLevel = readFloat2;
            this.shadowsLevel = readFloat2;
            float readFloat3 = inputSerializedData.readFloat(z);
            this.previousMidtonesLevel = readFloat3;
            this.midtonesLevel = readFloat3;
            float readFloat4 = inputSerializedData.readFloat(z);
            this.previousHighlightsLevel = readFloat4;
            this.highlightsLevel = readFloat4;
            float readFloat5 = inputSerializedData.readFloat(z);
            this.previousWhitesLevel = readFloat5;
            this.whitesLevel = readFloat5;
        }

        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeFloat(this.blacksLevel);
            outputSerializedData.writeFloat(this.shadowsLevel);
            outputSerializedData.writeFloat(this.midtonesLevel);
            outputSerializedData.writeFloat(this.highlightsLevel);
            outputSerializedData.writeFloat(this.whitesLevel);
        }
    }

    public static class EnhanceView extends View {
        private boolean allowTouch;
        private StaticLayout bottomText;
        private float bottomTextLeft;
        private TextPaint bottomTextPaint;
        private float bottomTextWidth;
        private long downTime;
        private PhotoFilterView filterView;
        private Runnable hide;
        private float lastTouchX;
        private float lastTouchY;
        private float lastVibrateValue;
        private Runnable requestFilterView;
        private AnimatedFloat showT;
        private boolean shown;
        private StaticLayout topText;
        private float topTextLeft;
        private TextPaint topTextPaint;
        private float topTextWidth;
        private boolean tracking;

        public EnhanceView(Context context, Runnable runnable) {
            super(context);
            this.topTextPaint = new TextPaint(1);
            this.bottomTextPaint = new TextPaint(1);
            this.showT = new AnimatedFloat(this, 0L, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
            this.hide = new Runnable() { // from class: org.telegram.ui.Components.PhotoFilterView$EnhanceView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoFilterView.EnhanceView.this.lambda$new$0();
                }
            };
            this.requestFilterView = runnable;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            this.shown = false;
            invalidate();
        }

        private void updateBottomText() {
            PhotoFilterView photoFilterView = this.filterView;
            StaticLayout staticLayout = new StaticLayout("" + Math.round((photoFilterView == null ? 0.0f : photoFilterView.getEnhanceValue()) * 100.0f), this.bottomTextPaint, getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.bottomText = staticLayout;
            this.bottomTextWidth = staticLayout.getLineCount() > 0 ? this.bottomText.getLineWidth(0) : 0.0f;
            this.bottomTextLeft = this.bottomText.getLineCount() > 0 ? this.bottomText.getLineLeft(0) : 0.0f;
            invalidate();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float f = this.showT.set(this.shown);
            if (f <= 0.0f || this.topText == null || this.bottomText == null) {
                return;
            }
            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) (f * 255.0f), 31);
            canvas.save();
            canvas.translate(((getWidth() - this.topTextWidth) / 2.0f) - this.topTextLeft, getHeight() * 0.22f);
            this.topText.draw(canvas);
            canvas.restore();
            canvas.save();
            canvas.translate(((getWidth() - this.bottomTextWidth) / 2.0f) - this.bottomTextLeft, (getHeight() * 0.22f) + AndroidUtilities.dp(60.0f));
            this.bottomText.draw(canvas);
            canvas.restore();
            canvas.restore();
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
            this.topTextPaint.setColor(-1);
            this.topTextPaint.setShadowLayer(AndroidUtilities.dp(8.0f), 0.0f, 0.0f, 805306368);
            this.topTextPaint.setTextSize(AndroidUtilities.dp(34.0f));
            this.bottomTextPaint.setColor(-1);
            this.bottomTextPaint.setShadowLayer(AndroidUtilities.dp(12.0f), 0.0f, 0.0f, 805306368);
            this.bottomTextPaint.setTextSize(AndroidUtilities.dp(58.0f));
            if (this.topText == null) {
                StaticLayout staticLayout = new StaticLayout(LocaleController.getString(R.string.Enhance), this.topTextPaint, getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.topText = staticLayout;
                this.topTextWidth = staticLayout.getLineCount() > 0 ? this.topText.getLineWidth(0) : 0.0f;
                this.topTextLeft = this.topText.getLineCount() > 0 ? this.topText.getLineLeft(0) : 0.0f;
            }
        }

        public boolean onTouch(MotionEvent motionEvent) {
            if (this.allowTouch && motionEvent.getPointerCount() == 1) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    this.tracking = false;
                    this.downTime = System.currentTimeMillis();
                    this.lastTouchX = motionEvent.getX();
                    this.lastTouchY = motionEvent.getY();
                    PhotoFilterView photoFilterView = this.filterView;
                    if (photoFilterView != null) {
                        this.lastVibrateValue = photoFilterView.getEnhanceValue();
                    }
                    return true;
                }
                if (action == 2) {
                    float x = motionEvent.getX();
                    float y = motionEvent.getY();
                    if (!this.tracking && System.currentTimeMillis() - this.downTime <= ViewConfiguration.getLongPressTimeout() && Math.abs(this.lastTouchY - y) < Math.abs(this.lastTouchX - x) && Math.abs(this.lastTouchX - x) > AndroidUtilities.touchSlop) {
                        this.tracking = true;
                        AndroidUtilities.cancelRunOnUIThread(this.hide);
                        this.shown = true;
                        invalidate();
                    }
                    if (this.tracking) {
                        float f = x - this.lastTouchX;
                        if (this.filterView == null) {
                            this.requestFilterView.run();
                        }
                        PhotoFilterView photoFilterView2 = this.filterView;
                        if (photoFilterView2 == null) {
                            this.tracking = false;
                            return false;
                        }
                        float enhanceValue = photoFilterView2.getEnhanceValue();
                        float clamp = Utilities.clamp((f / (AndroidUtilities.displaySize.x * 0.8f)) + enhanceValue, 1.0f, 0.0f);
                        int round = Math.round(clamp * 100.0f);
                        int round2 = Math.round(enhanceValue * 100.0f);
                        int round3 = Math.round(this.lastVibrateValue * 100.0f);
                        if (round == round2 || !(round == 100 || round == 0)) {
                            if (Math.abs(round - round3) > (SharedConfig.getDevicePerformanceClass() == 2 ? 5 : 10)) {
                                AndroidUtilities.vibrateCursor(this);
                            }
                            this.filterView.setEnhanceValue(clamp);
                            updateBottomText();
                        } else {
                            try {
                                performHapticFeedback(3, 1);
                            } catch (Exception unused) {
                            }
                        }
                        this.lastVibrateValue = clamp;
                        this.filterView.setEnhanceValue(clamp);
                        updateBottomText();
                    }
                    this.lastTouchX = x;
                    this.lastTouchY = y;
                } else if (action == 1 || action == 3) {
                    this.tracking = false;
                    this.downTime = -1L;
                    PhotoFilterView photoFilterView3 = this.filterView;
                    if (photoFilterView3 != null) {
                        this.lastVibrateValue = photoFilterView3.getEnhanceValue();
                    }
                    AndroidUtilities.runOnUIThread(this.hide, 600L);
                    return false;
                }
            } else if (this.shown) {
                this.shown = false;
                invalidate();
            }
            return false;
        }

        public void setAllowTouch(boolean z) {
            this.allowTouch = z;
        }

        public void setFilterView(PhotoFilterView photoFilterView) {
            this.filterView = photoFilterView;
        }
    }

    private static class RecyclerListViewWithShadows extends RecyclerListView {
        private boolean bottom;
        private AnimatedFloat bottomAlpha;
        private final Paint bottomPaint;
        private boolean top;
        private AnimatedFloat topAlpha;
        private final Paint topPaint;

        public RecyclerListViewWithShadows(Context context) {
            super(context);
            Paint paint = new Paint(1);
            this.topPaint = paint;
            Paint paint2 = new Paint(1);
            this.bottomPaint = paint2;
            this.topAlpha = new AnimatedFloat(this);
            this.bottomAlpha = new AnimatedFloat(this);
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            paint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, AndroidUtilities.dp(8.0f), new int[]{-16777216, 0}, new float[]{0.0f, 1.0f}, tileMode));
            paint2.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, AndroidUtilities.dp(8.0f), new int[]{0, -16777216}, new float[]{0.0f, 1.0f}, tileMode));
        }

        private void updateAlphas() {
            boolean canScrollVertically = canScrollVertically(-1);
            boolean canScrollVertically2 = canScrollVertically(1);
            if (canScrollVertically == this.top && canScrollVertically2 == this.bottom) {
                return;
            }
            this.top = canScrollVertically;
            this.bottom = canScrollVertically2;
            invalidate();
        }

        @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
        public void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            this.topPaint.setAlpha((int) (this.topAlpha.set(this.top ? 1.0f : 0.0f) * 255.0f));
            canvas.drawRect(0.0f, 0.0f, getWidth(), AndroidUtilities.dp(8.0f), this.topPaint);
            this.bottomPaint.setAlpha((int) (this.bottomAlpha.set(this.bottom ? 1.0f : 0.0f) * 255.0f));
            canvas.save();
            canvas.translate(0.0f, getHeight() - AndroidUtilities.dp(8.0f));
            canvas.drawRect(0.0f, 0.0f, getWidth(), AndroidUtilities.dp(8.0f), this.bottomPaint);
            canvas.restore();
        }

        @Override // androidx.recyclerview.widget.RecyclerView
        public void onScrolled(int i, int i2) {
            super.onScrolled(i, i2);
            updateAlphas();
        }
    }

    public class ToolsAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ToolsAdapter(Context context) {
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$0(int i, int i2) {
            if (i == PhotoFilterView.this.enhanceTool) {
                PhotoFilterView.this.enhanceValue = i2;
            } else if (i == PhotoFilterView.this.highlightsTool) {
                PhotoFilterView.this.highlightsValue = i2;
            } else if (i == PhotoFilterView.this.contrastTool) {
                PhotoFilterView.this.contrastValue = i2;
            } else if (i == PhotoFilterView.this.exposureTool) {
                PhotoFilterView.this.exposureValue = i2;
            } else if (i == PhotoFilterView.this.warmthTool) {
                PhotoFilterView.this.warmthValue = i2;
            } else if (i == PhotoFilterView.this.saturationTool) {
                PhotoFilterView.this.saturationValue = i2;
            } else if (i == PhotoFilterView.this.vignetteTool) {
                PhotoFilterView.this.vignetteValue = i2;
            } else if (i == PhotoFilterView.this.shadowsTool) {
                PhotoFilterView.this.shadowsValue = i2;
            } else if (i == PhotoFilterView.this.grainTool) {
                PhotoFilterView.this.grainValue = i2;
            } else if (i == PhotoFilterView.this.sharpenTool) {
                PhotoFilterView.this.sharpenValue = i2;
            } else if (i == PhotoFilterView.this.fadeTool) {
                PhotoFilterView.this.fadeValue = i2;
            } else if (i == PhotoFilterView.this.softenSkinTool) {
                PhotoFilterView.this.softenSkinValue = i2;
            }
            if (PhotoFilterView.this.eglThread != null) {
                PhotoFilterView.this.eglThread.requestRender(true);
            }
            PhotoFilterView.this.updateFiltersEmpty();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$1(View view) {
            PhotoEditRadioCell photoEditRadioCell = (PhotoEditRadioCell) view;
            if (((Integer) photoEditRadioCell.getTag()).intValue() == PhotoFilterView.this.tintShadowsTool) {
                PhotoFilterView.this.tintShadowsColor = photoEditRadioCell.getCurrentColor();
            } else {
                PhotoFilterView.this.tintHighlightsColor = photoEditRadioCell.getCurrentColor();
            }
            if (PhotoFilterView.this.eglThread != null) {
                PhotoFilterView.this.eglThread.requestRender(false);
            }
            PhotoFilterView.this.updateFiltersEmpty();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return PhotoFilterView.this.rowsCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return (i == PhotoFilterView.this.tintShadowsTool || i == PhotoFilterView.this.tintHighlightsTool) ? 1 : 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String string;
            float f;
            String string2;
            int i2;
            int itemViewType = viewHolder.getItemViewType();
            int i3 = 0;
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    return;
                }
                PhotoEditRadioCell photoEditRadioCell = (PhotoEditRadioCell) viewHolder.itemView;
                photoEditRadioCell.setTag(Integer.valueOf(i));
                if (i == PhotoFilterView.this.tintShadowsTool) {
                    string2 = LocaleController.getString(R.string.TintShadows);
                    i2 = PhotoFilterView.this.tintShadowsColor;
                } else {
                    if (i != PhotoFilterView.this.tintHighlightsTool) {
                        return;
                    }
                    string2 = LocaleController.getString(R.string.TintHighlights);
                    i2 = PhotoFilterView.this.tintHighlightsColor;
                }
                photoEditRadioCell.setIconAndTextAndValue(string2, 0, i2);
                return;
            }
            PhotoEditToolCell photoEditToolCell = (PhotoEditToolCell) viewHolder.itemView;
            photoEditToolCell.setTag(Integer.valueOf(i));
            if (i == PhotoFilterView.this.enhanceTool) {
                string = LocaleController.getString(R.string.Enhance);
                f = PhotoFilterView.this.enhanceValue;
            } else {
                if (i == PhotoFilterView.this.highlightsTool) {
                    string = LocaleController.getString(R.string.Highlights);
                    f = PhotoFilterView.this.highlightsValue;
                } else if (i == PhotoFilterView.this.contrastTool) {
                    string = LocaleController.getString(R.string.Contrast);
                    f = PhotoFilterView.this.contrastValue;
                } else if (i == PhotoFilterView.this.exposureTool) {
                    string = LocaleController.getString(R.string.Exposure);
                    f = PhotoFilterView.this.exposureValue;
                } else if (i == PhotoFilterView.this.warmthTool) {
                    string = LocaleController.getString(R.string.Warmth);
                    f = PhotoFilterView.this.warmthValue;
                } else if (i == PhotoFilterView.this.saturationTool) {
                    string = LocaleController.getString(R.string.Saturation);
                    f = PhotoFilterView.this.saturationValue;
                } else if (i == PhotoFilterView.this.vignetteTool) {
                    string = LocaleController.getString(R.string.Vignette);
                    f = PhotoFilterView.this.vignetteValue;
                } else if (i == PhotoFilterView.this.shadowsTool) {
                    string = LocaleController.getString(R.string.Shadows);
                    f = PhotoFilterView.this.shadowsValue;
                } else if (i == PhotoFilterView.this.grainTool) {
                    string = LocaleController.getString(R.string.Grain);
                    f = PhotoFilterView.this.grainValue;
                } else if (i == PhotoFilterView.this.sharpenTool) {
                    string = LocaleController.getString(R.string.Sharpen);
                    f = PhotoFilterView.this.sharpenValue;
                } else if (i == PhotoFilterView.this.fadeTool) {
                    string = LocaleController.getString(R.string.Fade);
                    f = PhotoFilterView.this.fadeValue;
                } else {
                    if (i != PhotoFilterView.this.softenSkinTool) {
                        return;
                    }
                    string = LocaleController.getString(R.string.SoftenSkin);
                    f = PhotoFilterView.this.softenSkinValue;
                }
                i3 = -100;
            }
            photoEditToolCell.setIconAndTextAndValue(string, f, i3, 100);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            PhotoEditRadioCell photoEditRadioCell;
            if (i == 0) {
                PhotoEditToolCell photoEditToolCell = new PhotoEditToolCell(this.mContext, PhotoFilterView.this.resourcesProvider);
                photoEditToolCell.setSeekBarDelegate(new PhotoEditorSeekBar.PhotoEditorSeekBarDelegate() { // from class: org.telegram.ui.Components.PhotoFilterView$ToolsAdapter$$ExternalSyntheticLambda0
                    @Override // org.telegram.ui.Components.PhotoEditorSeekBar.PhotoEditorSeekBarDelegate
                    public final void onProgressChanged(int i2, int i3) {
                        PhotoFilterView.ToolsAdapter.this.lambda$onCreateViewHolder$0(i2, i3);
                    }
                });
                photoEditRadioCell = photoEditToolCell;
            } else {
                PhotoEditRadioCell photoEditRadioCell2 = new PhotoEditRadioCell(this.mContext);
                photoEditRadioCell2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhotoFilterView$ToolsAdapter$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PhotoFilterView.ToolsAdapter.this.lambda$onCreateViewHolder$1(view);
                    }
                });
                photoEditRadioCell = photoEditRadioCell2;
            }
            return new RecyclerListView.Holder(photoEditRadioCell);
        }
    }

    public PhotoFilterView(Context context, VideoEditTextureView videoEditTextureView, Bitmap bitmap, int i, MediaController.SavedFilterState savedFilterState, PaintingOverlay paintingOverlay, int i2, boolean z, boolean z2, BlurringShader.BlurManager blurManager, Theme.ResourcesProvider resourcesProvider) {
        this(context, videoEditTextureView, bitmap, null, i, savedFilterState, paintingOverlay, i2, z, z2, blurManager, resourcesProvider);
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x04d7  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x04d9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public PhotoFilterView(Context context, VideoEditTextureView videoEditTextureView, Bitmap bitmap, Bitmap bitmap2, int i, MediaController.SavedFilterState savedFilterState, PaintingOverlay paintingOverlay, int i2, boolean z, boolean z2, BlurringShader.BlurManager blurManager, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        int i3;
        RadioButton radioButton;
        this.curveRadioButton = new RadioButton[4];
        this.maskRect = new android.graphics.Rect();
        this.maskMatrix = new Matrix();
        this.maskPaint = new Paint(2);
        this.ownLayout = z2;
        this.resourcesProvider = resourcesProvider;
        this.inBubbleMode = context instanceof BubbleActivity;
        this.paintingOverlay = paintingOverlay;
        this.isMirrored = z;
        this.rowsCount = 0;
        if (i2 == 1) {
            this.rowsCount = 1;
            this.softenSkinTool = 0;
        } else if (i2 == 0) {
            this.softenSkinTool = -1;
        }
        int i4 = this.rowsCount;
        this.enhanceTool = i4;
        this.exposureTool = i4 + 1;
        this.contrastTool = i4 + 2;
        this.saturationTool = i4 + 3;
        this.warmthTool = i4 + 4;
        this.fadeTool = i4 + 5;
        this.highlightsTool = i4 + 6;
        this.shadowsTool = i4 + 7;
        int i5 = i4 + 9;
        this.rowsCount = i5;
        this.vignetteTool = i4 + 8;
        if (i2 == 2) {
            this.rowsCount = i4 + 10;
            this.softenSkinTool = i5;
        }
        if (videoEditTextureView == null) {
            int i6 = this.rowsCount;
            this.rowsCount = i6 + 1;
            this.grainTool = i6;
        } else {
            this.grainTool = -1;
        }
        int i7 = this.rowsCount;
        this.sharpenTool = i7;
        this.tintShadowsTool = i7 + 1;
        this.rowsCount = i7 + 3;
        this.tintHighlightsTool = i7 + 2;
        if (savedFilterState != null) {
            this.enhanceValue = savedFilterState.enhanceValue;
            this.softenSkinValue = savedFilterState.softenSkinValue;
            this.exposureValue = savedFilterState.exposureValue;
            this.contrastValue = savedFilterState.contrastValue;
            this.warmthValue = savedFilterState.warmthValue;
            this.saturationValue = savedFilterState.saturationValue;
            this.fadeValue = savedFilterState.fadeValue;
            this.tintShadowsColor = savedFilterState.tintShadowsColor;
            this.tintHighlightsColor = savedFilterState.tintHighlightsColor;
            this.highlightsValue = savedFilterState.highlightsValue;
            this.shadowsValue = savedFilterState.shadowsValue;
            this.vignetteValue = savedFilterState.vignetteValue;
            this.grainValue = savedFilterState.grainValue;
            this.blurType = savedFilterState.blurType;
            this.sharpenValue = savedFilterState.sharpenValue;
            this.curvesToolValue = savedFilterState.curvesToolValue;
            this.blurExcludeSize = savedFilterState.blurExcludeSize;
            this.blurExcludePoint = savedFilterState.blurExcludePoint;
            this.blurExcludeBlurSize = savedFilterState.blurExcludeBlurSize;
            this.filtersEmpty = savedFilterState.isEmpty();
            this.blurAngle = savedFilterState.blurAngle;
            this.lastState = savedFilterState;
        } else {
            this.curvesToolValue = new CurvesToolValue();
            this.blurExcludeSize = 0.35f;
            this.blurExcludePoint = new Point(0.5f, 0.5f);
            this.blurExcludeBlurSize = 0.15f;
            this.blurAngle = 1.5707964f;
            this.filtersEmpty = true;
        }
        this.bitmapToEdit = bitmap;
        this.bitmapMask = bitmap2;
        this.orientation = i;
        if (videoEditTextureView != null) {
            this.textureView = videoEditTextureView;
            videoEditTextureView.setDelegate(new VideoEditTextureView.VideoEditTextureViewDelegate() { // from class: org.telegram.ui.Components.PhotoFilterView$$ExternalSyntheticLambda0
                @Override // org.telegram.ui.Components.VideoEditTextureView.VideoEditTextureViewDelegate
                public final void onEGLThreadAvailable(FilterGLThread filterGLThread) {
                    PhotoFilterView.this.lambda$new$0(filterGLThread);
                }
            });
        } else {
            this.ownsTextureView = true;
            TextureView textureView = new TextureView(context) { // from class: org.telegram.ui.Components.PhotoFilterView.1
                @Override // android.view.TextureView
                public void setTransform(Matrix matrix) {
                    super.setTransform(matrix);
                    if (PhotoFilterView.this.eglThread != null) {
                        PhotoFilterView.this.eglThread.updateUiBlurTransform(matrix, getWidth(), getHeight());
                    }
                }
            };
            this.textureView = textureView;
            if (z2) {
                addView(textureView, LayoutHelper.createFrame(-1, -1, 51));
            }
            this.textureView.setVisibility(4);
            this.textureView.setSurfaceTextureListener(new 2(z2, blurManager));
        }
        PhotoFilterBlurControl photoFilterBlurControl = new PhotoFilterBlurControl(context);
        this.blurControl = photoFilterBlurControl;
        photoFilterBlurControl.setVisibility(4);
        if (z2) {
            addView(this.blurControl, LayoutHelper.createFrame(-1, -1, 51));
        }
        this.blurControl.setDelegate(new PhotoFilterBlurControl.PhotoFilterLinearBlurControlDelegate() { // from class: org.telegram.ui.Components.PhotoFilterView$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.PhotoFilterBlurControl.PhotoFilterLinearBlurControlDelegate
            public final void valueChanged(Point point, float f, float f2, float f3) {
                PhotoFilterView.this.lambda$new$1(point, f, f2, f3);
            }
        });
        PhotoFilterCurvesControl photoFilterCurvesControl = new PhotoFilterCurvesControl(context, this.curvesToolValue);
        this.curvesControl = photoFilterCurvesControl;
        photoFilterCurvesControl.setDelegate(new PhotoFilterCurvesControl.PhotoFilterCurvesControlDelegate() { // from class: org.telegram.ui.Components.PhotoFilterView$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.Components.PhotoFilterCurvesControl.PhotoFilterCurvesControlDelegate
            public final void valueChanged() {
                PhotoFilterView.this.lambda$new$2();
            }
        });
        this.curvesControl.setVisibility(4);
        if (z2) {
            addView(this.curvesControl, LayoutHelper.createFrame(-1, -1, 51));
        }
        FrameLayout frameLayout = new FrameLayout(context);
        this.toolsView = frameLayout;
        addView(frameLayout, LayoutHelper.createFrame(-1, (!z2 ? 40 : 0) + NotificationCenter.didUpdatePremiumGiftFieldIcon, 83));
        FrameLayout frameLayout2 = new FrameLayout(context);
        frameLayout2.setBackgroundColor(-16777216);
        this.toolsView.addView(frameLayout2, LayoutHelper.createFrame(-1, 48, 83));
        TextView textView = new TextView(context);
        this.cancelTextView = textView;
        textView.setTextSize(1, 14.0f);
        this.cancelTextView.setTextColor(-1);
        this.cancelTextView.setGravity(17);
        this.cancelTextView.setBackgroundDrawable(Theme.createSelectorDrawable(-12763843, 0));
        this.cancelTextView.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.cancelTextView.setText(LocaleController.getString(R.string.Cancel).toUpperCase());
        this.cancelTextView.setTypeface(AndroidUtilities.bold());
        frameLayout2.addView(this.cancelTextView, LayoutHelper.createFrame(-2, -1, 51));
        TextView textView2 = new TextView(context);
        this.doneTextView = textView2;
        textView2.setTextSize(1, 14.0f);
        TextView textView3 = this.doneTextView;
        int i8 = Theme.key_chat_editMediaButton;
        textView3.setTextColor(getThemedColor(i8));
        this.doneTextView.setGravity(17);
        this.doneTextView.setBackgroundDrawable(Theme.createSelectorDrawable(-12763843, 0));
        this.doneTextView.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.doneTextView.setText(LocaleController.getString(R.string.Done).toUpperCase());
        this.doneTextView.setTypeface(AndroidUtilities.bold());
        frameLayout2.addView(this.doneTextView, LayoutHelper.createFrame(-2, -1, 53));
        LinearLayout linearLayout = new LinearLayout(context);
        frameLayout2.addView(linearLayout, LayoutHelper.createFrame(-2, -1, 1));
        ImageView imageView = new ImageView(context);
        this.tuneItem = imageView;
        ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER;
        imageView.setScaleType(scaleType);
        this.tuneItem.setImageResource(R.drawable.msg_photo_settings);
        this.tuneItem.setColorFilter(new PorterDuffColorFilter(getThemedColor(i8), PorterDuff.Mode.MULTIPLY));
        this.tuneItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        linearLayout.addView(this.tuneItem, LayoutHelper.createLinear(56, 48));
        this.tuneItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhotoFilterView$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PhotoFilterView.this.lambda$new$3(view);
            }
        });
        ImageView imageView2 = new ImageView(context);
        this.blurItem = imageView2;
        imageView2.setScaleType(scaleType);
        this.blurItem.setImageResource(R.drawable.msg_photo_blur);
        this.blurItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        linearLayout.addView(this.blurItem, LayoutHelper.createLinear(56, 48));
        this.blurItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhotoFilterView$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PhotoFilterView.this.lambda$new$4(view);
            }
        });
        if (videoEditTextureView != null) {
            this.blurItem.setVisibility(8);
        }
        ImageView imageView3 = new ImageView(context);
        this.curveItem = imageView3;
        imageView3.setScaleType(scaleType);
        this.curveItem.setImageResource(R.drawable.msg_photo_curve);
        this.curveItem.setBackgroundDrawable(Theme.createSelectorDrawable(1090519039));
        linearLayout.addView(this.curveItem, LayoutHelper.createLinear(56, 48));
        this.curveItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhotoFilterView$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PhotoFilterView.this.lambda$new$5(view);
            }
        });
        this.recyclerListView = new RecyclerListViewWithShadows(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(1);
        this.recyclerListView.setLayoutManager(linearLayoutManager);
        this.recyclerListView.setClipToPadding(false);
        this.recyclerListView.setOverScrollMode(2);
        this.recyclerListView.setAdapter(new ToolsAdapter(context));
        this.toolsView.addView(this.recyclerListView, LayoutHelper.createFrame(-1, (!z2 ? 60 : 0) + 120, 51));
        FrameLayout frameLayout3 = new FrameLayout(context);
        this.curveLayout = frameLayout3;
        frameLayout3.setVisibility(4);
        this.toolsView.addView(this.curveLayout, LayoutHelper.createFrame(-1, 78.0f, 1, 0.0f, (!z2 ? 40 : 0) + 40, 0.0f, 0.0f));
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(0);
        this.curveLayout.addView(linearLayout2, LayoutHelper.createFrame(-2, -2, 1));
        int i9 = 0;
        for (int i10 = 4; i9 < i10; i10 = 4) {
            FrameLayout frameLayout4 = new FrameLayout(context);
            frameLayout4.setTag(Integer.valueOf(i9));
            this.curveRadioButton[i9] = new RadioButton(context);
            this.curveRadioButton[i9].setSize(AndroidUtilities.dp(20.0f));
            frameLayout4.addView(this.curveRadioButton[i9], LayoutHelper.createFrame(30, 30, 49));
            TextView textView4 = new TextView(context);
            textView4.setTextSize(1, 12.0f);
            textView4.setGravity(16);
            if (i9 == 0) {
                String string = LocaleController.getString(R.string.CurvesAll);
                textView4.setText(string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase());
                textView4.setTextColor(-1);
                this.curveRadioButton[i9].setColor(-1, -1);
            } else {
                if (i9 == 1) {
                    String string2 = LocaleController.getString(R.string.CurvesRed);
                    textView4.setText(string2.substring(0, 1).toUpperCase() + string2.substring(1).toLowerCase());
                    i3 = -1684147;
                    textView4.setTextColor(-1684147);
                    radioButton = this.curveRadioButton[i9];
                } else if (i9 == 2) {
                    String string3 = LocaleController.getString(R.string.CurvesGreen);
                    textView4.setText(string3.substring(0, 1).toUpperCase() + string3.substring(1).toLowerCase());
                    i3 = -10831009;
                    textView4.setTextColor(-10831009);
                    radioButton = this.curveRadioButton[i9];
                } else {
                    if (i9 == 3) {
                        String string4 = LocaleController.getString(R.string.CurvesBlue);
                        textView4.setText(string4.substring(0, 1).toUpperCase() + string4.substring(1).toLowerCase());
                        textView4.setTextColor(-12734994);
                        this.curveRadioButton[i9].setColor(-12734994, -12734994);
                    }
                    frameLayout4.addView(textView4, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 38.0f, 0.0f, 0.0f));
                    linearLayout2.addView(frameLayout4, LayoutHelper.createLinear(-2, -2, i9 != 0 ? 0.0f : 30.0f, 0.0f, 0.0f, 0.0f));
                    frameLayout4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhotoFilterView$$ExternalSyntheticLambda6
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PhotoFilterView.this.lambda$new$6(view);
                        }
                    });
                    i9++;
                }
                radioButton.setColor(i3, i3);
            }
            frameLayout4.addView(textView4, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 38.0f, 0.0f, 0.0f));
            linearLayout2.addView(frameLayout4, LayoutHelper.createLinear(-2, -2, i9 != 0 ? 0.0f : 30.0f, 0.0f, 0.0f, 0.0f));
            frameLayout4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhotoFilterView$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PhotoFilterView.this.lambda$new$6(view);
                }
            });
            i9++;
        }
        FrameLayout frameLayout5 = new FrameLayout(context);
        this.blurLayout = frameLayout5;
        frameLayout5.setVisibility(4);
        this.toolsView.addView(this.blurLayout, LayoutHelper.createFrame(NotificationCenter.onUserRingtonesUpdated, 60.0f, 1, 0.0f, (z2 ? 0 : 40) + 40, 0.0f, 0.0f));
        TextView textView5 = new TextView(context);
        this.blurOffButton = textView5;
        textView5.setCompoundDrawablePadding(AndroidUtilities.dp(2.0f));
        this.blurOffButton.setTextSize(1, 13.0f);
        this.blurOffButton.setGravity(1);
        this.blurOffButton.setText(LocaleController.getString(R.string.BlurOff));
        this.blurLayout.addView(this.blurOffButton, LayoutHelper.createFrame(80, 60.0f));
        this.blurOffButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhotoFilterView$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PhotoFilterView.this.lambda$new$7(view);
            }
        });
        TextView textView6 = new TextView(context);
        this.blurRadialButton = textView6;
        textView6.setCompoundDrawablePadding(AndroidUtilities.dp(2.0f));
        this.blurRadialButton.setTextSize(1, 13.0f);
        this.blurRadialButton.setGravity(1);
        this.blurRadialButton.setText(LocaleController.getString(R.string.BlurRadial));
        this.blurLayout.addView(this.blurRadialButton, LayoutHelper.createFrame(80, 80.0f, 51, 100.0f, 0.0f, 0.0f, 0.0f));
        this.blurRadialButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhotoFilterView$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PhotoFilterView.this.lambda$new$8(view);
            }
        });
        TextView textView7 = new TextView(context);
        this.blurLinearButton = textView7;
        textView7.setCompoundDrawablePadding(AndroidUtilities.dp(2.0f));
        this.blurLinearButton.setTextSize(1, 13.0f);
        this.blurLinearButton.setGravity(1);
        this.blurLinearButton.setText(LocaleController.getString(R.string.BlurLinear));
        this.blurLayout.addView(this.blurLinearButton, LayoutHelper.createFrame(80, 80.0f, 51, 200.0f, 0.0f, 0.0f, 0.0f));
        this.blurLinearButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhotoFilterView$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PhotoFilterView.this.lambda$new$9(view);
            }
        });
        updateSelectedBlurType();
        if (Build.VERSION.SDK_INT < 21 || this.inBubbleMode || !z2) {
            return;
        }
        if (this.ownsTextureView) {
            ((FrameLayout.LayoutParams) this.textureView.getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight;
        }
        ((FrameLayout.LayoutParams) this.curvesControl.getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight;
    }

    private void fixLayout(int i, int i2) {
        float width;
        int height;
        float ceil;
        float f;
        if (this.ownLayout) {
            int dp = i - AndroidUtilities.dp(28.0f);
            int dp2 = AndroidUtilities.dp(214.0f);
            int i3 = Build.VERSION.SDK_INT;
            int i4 = i2 - (dp2 + ((i3 < 21 || this.inBubbleMode) ? 0 : AndroidUtilities.statusBarHeight));
            Bitmap bitmap = this.bitmapToEdit;
            if (bitmap != null) {
                int i5 = this.orientation % 360;
                if (i5 == 90 || i5 == 270) {
                    width = bitmap.getHeight();
                    height = this.bitmapToEdit.getWidth();
                } else {
                    width = bitmap.getWidth();
                    height = this.bitmapToEdit.getHeight();
                }
            } else {
                width = this.textureView.getWidth();
                height = this.textureView.getHeight();
            }
            float f2 = dp;
            float f3 = i4;
            if (f2 / width > f3 / height) {
                f = (int) Math.ceil(width * r9);
                ceil = f3;
            } else {
                ceil = (int) Math.ceil(r5 * r7);
                f = f2;
            }
            int ceil2 = (int) Math.ceil(((f2 - f) / 2.0f) + AndroidUtilities.dp(14.0f));
            int ceil3 = (int) Math.ceil(((f3 - ceil) / 2.0f) + AndroidUtilities.dp(14.0f) + ((i3 < 21 || this.inBubbleMode) ? 0 : AndroidUtilities.statusBarHeight));
            int i6 = (int) f;
            int i7 = (int) ceil;
            if (this.ownsTextureView) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.textureView.getLayoutParams();
                layoutParams.leftMargin = ceil2;
                layoutParams.topMargin = ceil3;
                layoutParams.width = i6;
                layoutParams.height = i7;
            }
            float f4 = i6;
            float f5 = i7;
            this.curvesControl.setActualArea(ceil2, ceil3 - ((i3 < 21 || this.inBubbleMode) ? 0 : AndroidUtilities.statusBarHeight), f4, f5);
            this.blurControl.setActualAreaSize(f4, f5);
            ((FrameLayout.LayoutParams) this.blurControl.getLayoutParams()).height = AndroidUtilities.dp(38.0f) + i4;
            ((FrameLayout.LayoutParams) this.curvesControl.getLayoutParams()).height = i4 + AndroidUtilities.dp(28.0f);
            if (AndroidUtilities.isTablet()) {
                int dp3 = AndroidUtilities.dp(86.0f) * 10;
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.recyclerListView.getLayoutParams();
                if (dp3 < dp) {
                    layoutParams2.width = dp3;
                    layoutParams2.leftMargin = (dp - dp3) / 2;
                } else {
                    layoutParams2.width = -1;
                    layoutParams2.leftMargin = 0;
                }
            }
        }
    }

    private int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(FilterGLThread filterGLThread) {
        this.eglThread = filterGLThread;
        filterGLThread.setFilterGLThreadDelegate(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Point point, float f, float f2, float f3) {
        this.blurExcludeSize = f2;
        this.blurExcludePoint = point;
        this.blurExcludeBlurSize = f;
        this.blurAngle = f3;
        FilterGLThread filterGLThread = this.eglThread;
        if (filterGLThread != null) {
            filterGLThread.requestRender(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        updateFiltersEmpty();
        FilterGLThread filterGLThread = this.eglThread;
        if (filterGLThread != null) {
            filterGLThread.requestRender(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(View view) {
        this.selectedTool = 0;
        this.tuneItem.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_editMediaButton), PorterDuff.Mode.MULTIPLY));
        this.blurItem.setColorFilter((ColorFilter) null);
        this.curveItem.setColorFilter((ColorFilter) null);
        switchMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(View view) {
        this.selectedTool = 1;
        this.tuneItem.setColorFilter((ColorFilter) null);
        this.blurItem.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_editMediaButton), PorterDuff.Mode.MULTIPLY));
        this.curveItem.setColorFilter((ColorFilter) null);
        switchMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(View view) {
        this.selectedTool = 2;
        this.tuneItem.setColorFilter((ColorFilter) null);
        this.blurItem.setColorFilter((ColorFilter) null);
        this.curveItem.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_editMediaButton), PorterDuff.Mode.MULTIPLY));
        switchMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(View view) {
        int intValue = ((Integer) view.getTag()).intValue();
        this.curvesToolValue.activeType = intValue;
        int i = 0;
        while (i < 4) {
            this.curveRadioButton[i].setChecked(i == intValue, true);
            i++;
        }
        this.curvesControl.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$7(View view) {
        this.blurType = 0;
        updateSelectedBlurType();
        this.blurControl.setVisibility(4);
        FilterGLThread filterGLThread = this.eglThread;
        if (filterGLThread != null) {
            filterGLThread.requestRender(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8(View view) {
        this.blurType = 1;
        updateSelectedBlurType();
        this.blurControl.setVisibility(0);
        this.blurControl.setType(1);
        FilterGLThread filterGLThread = this.eglThread;
        if (filterGLThread != null) {
            filterGLThread.requestRender(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$9(View view) {
        this.blurType = 2;
        updateSelectedBlurType();
        this.blurControl.setVisibility(0);
        this.blurControl.setType(0);
        FilterGLThread filterGLThread = this.eglThread;
        if (filterGLThread != null) {
            filterGLThread.requestRender(false);
        }
    }

    private void setShowOriginal(boolean z) {
        if (this.showOriginal == z) {
            return;
        }
        this.showOriginal = z;
        FilterGLThread filterGLThread = this.eglThread;
        if (filterGLThread != null) {
            filterGLThread.requestRender(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFiltersEmpty() {
        this.filtersEmpty = Math.abs(this.enhanceValue) < 0.1f && Math.abs(this.softenSkinValue) < 0.1f && Math.abs(this.exposureValue) < 0.1f && Math.abs(this.contrastValue) < 0.1f && Math.abs(this.warmthValue) < 0.1f && Math.abs(this.saturationValue) < 0.1f && Math.abs(this.fadeValue) < 0.1f && this.tintShadowsColor == 0 && this.tintHighlightsColor == 0 && Math.abs(this.highlightsValue) < 0.1f && Math.abs(this.shadowsValue) < 0.1f && Math.abs(this.vignetteValue) < 0.1f && Math.abs(this.grainValue) < 0.1f && this.blurType == 0 && Math.abs(this.sharpenValue) < 0.1f && this.curvesToolValue.shouldBeSkipped();
    }

    private void updateSelectedBlurType() {
        TextView textView;
        int i = this.blurType;
        int i2 = -1;
        if (i == 0) {
            Drawable mutate = this.blurOffButton.getContext().getResources().getDrawable(R.drawable.msg_blur_off).mutate();
            int i3 = Theme.key_chat_editMediaButton;
            mutate.setColorFilter(new PorterDuffColorFilter(getThemedColor(i3), PorterDuff.Mode.MULTIPLY));
            this.blurOffButton.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, mutate, (Drawable) null, (Drawable) null);
            this.blurOffButton.setTextColor(getThemedColor(i3));
            this.blurRadialButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.msg_blur_radial, 0, 0);
            this.blurRadialButton.setTextColor(-1);
        } else {
            if (i != 1) {
                if (i == 2) {
                    this.blurOffButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.msg_blur_off, 0, 0);
                    this.blurOffButton.setTextColor(-1);
                    this.blurRadialButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.msg_blur_radial, 0, 0);
                    this.blurRadialButton.setTextColor(-1);
                    Drawable mutate2 = this.blurOffButton.getContext().getResources().getDrawable(R.drawable.msg_blur_linear).mutate();
                    int i4 = Theme.key_chat_editMediaButton;
                    mutate2.setColorFilter(new PorterDuffColorFilter(getThemedColor(i4), PorterDuff.Mode.MULTIPLY));
                    this.blurLinearButton.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, mutate2, (Drawable) null, (Drawable) null);
                    textView = this.blurLinearButton;
                    i2 = getThemedColor(i4);
                    textView.setTextColor(i2);
                }
                updateFiltersEmpty();
            }
            this.blurOffButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.msg_blur_off, 0, 0);
            this.blurOffButton.setTextColor(-1);
            Drawable mutate3 = this.blurOffButton.getContext().getResources().getDrawable(R.drawable.msg_blur_radial).mutate();
            int i5 = Theme.key_chat_editMediaButton;
            mutate3.setColorFilter(new PorterDuffColorFilter(getThemedColor(i5), PorterDuff.Mode.MULTIPLY));
            this.blurRadialButton.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, mutate3, (Drawable) null, (Drawable) null);
            this.blurRadialButton.setTextColor(getThemedColor(i5));
        }
        this.blurLinearButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.msg_blur_linear, 0, 0);
        textView = this.blurLinearButton;
        textView.setTextColor(i2);
        updateFiltersEmpty();
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        boolean drawChild = super.drawChild(canvas, view, j);
        if (this.paintingOverlay != null && view == this.textureView) {
            canvas.save();
            canvas.translate(this.textureView.getLeft(), this.textureView.getTop());
            if (this.bitmapMask != null && this.textureView.getVisibility() == 0) {
                this.maskRect.set(0, 0, this.textureView.getMeasuredWidth(), this.textureView.getMeasuredHeight());
                if (this.orientation != 0) {
                    this.maskMatrix.reset();
                    this.maskMatrix.postRotate(this.orientation, this.bitmapMask.getWidth() / 2.0f, this.bitmapMask.getHeight() / 2.0f);
                    float height = (this.bitmapMask.getHeight() - this.bitmapMask.getWidth()) / 2.0f;
                    this.maskMatrix.postTranslate(height, -height);
                    this.maskMatrix.postScale(this.maskRect.width() / this.bitmapMask.getHeight(), this.maskRect.height() / this.bitmapMask.getWidth());
                    canvas.drawBitmap(this.bitmapMask, this.maskMatrix, this.maskPaint);
                } else {
                    canvas.drawBitmap(this.bitmapMask, (android.graphics.Rect) null, this.maskRect, this.maskPaint);
                }
            }
            float measuredWidth = this.textureView.getMeasuredWidth() / this.paintingOverlay.getMeasuredWidth();
            canvas.scale(measuredWidth, measuredWidth);
            this.paintingOverlay.draw(canvas);
            canvas.restore();
        }
        return drawChild;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public ByteBuffer fillAndGetCurveBuffer() {
        this.curvesToolValue.fillBuffer();
        return this.curvesToolValue.curveBuffer;
    }

    public Bitmap getBitmap() {
        FilterGLThread filterGLThread = this.eglThread;
        if (filterGLThread != null) {
            return filterGLThread.getTexture();
        }
        return null;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getBlurAngle() {
        return this.blurAngle;
    }

    public PhotoFilterBlurControl getBlurControl() {
        return this.blurControl;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getBlurExcludeBlurSize() {
        return this.blurExcludeBlurSize;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public Point getBlurExcludePoint() {
        return this.blurExcludePoint;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getBlurExcludeSize() {
        return this.blurExcludeSize;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public int getBlurType() {
        return this.blurType;
    }

    public TextView getCancelTextView() {
        return this.cancelTextView;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getContrastValue() {
        return ((this.contrastValue / 100.0f) * 0.3f) + 1.0f;
    }

    public PhotoFilterCurvesControl getCurveControl() {
        return this.curvesControl;
    }

    public TextView getDoneTextView() {
        return this.doneTextView;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getEnhanceValue() {
        return this.enhanceValue / 100.0f;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getExposureValue() {
        return this.exposureValue / 100.0f;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getFadeValue() {
        return this.fadeValue / 100.0f;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getGrainValue() {
        return (this.grainValue / 100.0f) * 0.04f;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getHighlightsValue() {
        return ((this.highlightsValue * 0.75f) + 100.0f) / 100.0f;
    }

    public TextureView getMyTextureView() {
        if (!this.ownsTextureView || this.ownLayout) {
            return null;
        }
        return this.textureView;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getSaturationValue() {
        float f = this.saturationValue / 100.0f;
        if (f > 0.0f) {
            f *= 1.05f;
        }
        return f + 1.0f;
    }

    public MediaController.SavedFilterState getSavedFilterState() {
        MediaController.SavedFilterState savedFilterState = new MediaController.SavedFilterState();
        savedFilterState.enhanceValue = this.enhanceValue;
        savedFilterState.exposureValue = this.exposureValue;
        savedFilterState.contrastValue = this.contrastValue;
        savedFilterState.warmthValue = this.warmthValue;
        savedFilterState.saturationValue = this.saturationValue;
        savedFilterState.fadeValue = this.fadeValue;
        savedFilterState.softenSkinValue = this.softenSkinValue;
        savedFilterState.tintShadowsColor = this.tintShadowsColor;
        savedFilterState.tintHighlightsColor = this.tintHighlightsColor;
        savedFilterState.highlightsValue = this.highlightsValue;
        savedFilterState.shadowsValue = this.shadowsValue;
        savedFilterState.vignetteValue = this.vignetteValue;
        savedFilterState.grainValue = this.grainValue;
        savedFilterState.blurType = this.blurType;
        savedFilterState.sharpenValue = this.sharpenValue;
        savedFilterState.curvesToolValue = this.curvesToolValue;
        savedFilterState.blurExcludeSize = this.blurExcludeSize;
        savedFilterState.blurExcludePoint = this.blurExcludePoint;
        savedFilterState.blurExcludeBlurSize = this.blurExcludeBlurSize;
        savedFilterState.blurAngle = this.blurAngle;
        this.lastState = savedFilterState;
        return savedFilterState;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getShadowsValue() {
        return ((this.shadowsValue * 0.55f) + 100.0f) / 100.0f;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getSharpenValue() {
        return ((this.sharpenValue / 100.0f) * 0.6f) + 0.11f;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getSoftenSkinValue() {
        return this.softenSkinValue / 100.0f;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public int getTintHighlightsColor() {
        return this.tintHighlightsColor;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getTintHighlightsIntensityValue() {
        return this.tintHighlightsColor == 0 ? 0.0f : 0.5f;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public int getTintShadowsColor() {
        return this.tintShadowsColor;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getTintShadowsIntensityValue() {
        return this.tintShadowsColor == 0 ? 0.0f : 0.5f;
    }

    public FrameLayout getToolsView() {
        return this.toolsView;
    }

    public Bitmap getUiBlurBitmap() {
        FilterGLThread filterGLThread = this.eglThread;
        if (filterGLThread == null) {
            return null;
        }
        return filterGLThread.getUiBlurBitmap();
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getVignetteValue() {
        return this.vignetteValue / 100.0f;
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public float getWarmthValue() {
        return this.warmthValue / 100.0f;
    }

    public boolean hasChanges() {
        MediaController.SavedFilterState savedFilterState = this.lastState;
        return savedFilterState != null ? (this.enhanceValue == savedFilterState.enhanceValue && this.contrastValue == savedFilterState.contrastValue && this.highlightsValue == savedFilterState.highlightsValue && this.exposureValue == savedFilterState.exposureValue && this.warmthValue == savedFilterState.warmthValue && this.saturationValue == savedFilterState.saturationValue && this.vignetteValue == savedFilterState.vignetteValue && this.shadowsValue == savedFilterState.shadowsValue && this.grainValue == savedFilterState.grainValue && this.sharpenValue == savedFilterState.sharpenValue && this.fadeValue == savedFilterState.fadeValue && this.softenSkinValue == savedFilterState.softenSkinValue && this.tintHighlightsColor == savedFilterState.tintHighlightsColor && this.tintShadowsColor == savedFilterState.tintShadowsColor && this.curvesToolValue.shouldBeSkipped()) ? false : true : (this.enhanceValue == 0.0f && this.contrastValue == 0.0f && this.highlightsValue == 0.0f && this.exposureValue == 0.0f && this.warmthValue == 0.0f && this.saturationValue == 0.0f && this.vignetteValue == 0.0f && this.shadowsValue == 0.0f && this.grainValue == 0.0f && this.sharpenValue == 0.0f && this.fadeValue == 0.0f && this.softenSkinValue == 0.0f && this.tintHighlightsColor == 0 && this.tintShadowsColor == 0 && this.curvesToolValue.shouldBeSkipped()) ? false : true;
    }

    public void init() {
        this.textureView.setVisibility(0);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        fixLayout(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
        super.onMeasure(i, i2);
    }

    @Override // org.telegram.ui.Stories.recorder.StoryRecorder.Touchable
    public boolean onTouch(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0 || motionEvent.getActionMasked() == 5) {
            TextureView textureView = this.textureView;
            if (!(textureView instanceof VideoEditTextureView) ? !(motionEvent.getX() < this.textureView.getX() || motionEvent.getY() < this.textureView.getY() || motionEvent.getX() > this.textureView.getX() + this.textureView.getWidth() || motionEvent.getY() > this.textureView.getY() + this.textureView.getHeight()) : ((VideoEditTextureView) textureView).containsPoint(motionEvent.getX(), motionEvent.getY())) {
                setShowOriginal(true);
            }
        } else if (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 6) {
            setShowOriginal(false);
        }
        return true;
    }

    public void setEnhanceValue(float f) {
        this.enhanceValue = f * 100.0f;
        updateFiltersEmpty();
        int i = 0;
        while (true) {
            if (i >= this.recyclerListView.getChildCount()) {
                break;
            }
            View childAt = this.recyclerListView.getChildAt(i);
            if ((childAt instanceof PhotoEditToolCell) && this.recyclerListView.getChildAdapterPosition(childAt) == this.enhanceTool) {
                ((PhotoEditToolCell) childAt).setIconAndTextAndValue(LocaleController.getString(R.string.Enhance), this.enhanceValue, 0, 100);
                break;
            }
            i++;
        }
        FilterGLThread filterGLThread = this.eglThread;
        if (filterGLThread != null) {
            filterGLThread.requestRender(true);
        }
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public boolean shouldDrawCurvesPass() {
        return !this.curvesToolValue.shouldBeSkipped();
    }

    @Override // org.telegram.ui.Components.FilterShaders.FilterShadersDelegate
    public boolean shouldShowOriginal() {
        return this.showOriginal || this.filtersEmpty;
    }

    public void shutdown() {
        if (this.ownsTextureView) {
            FilterGLThread filterGLThread = this.eglThread;
            if (filterGLThread != null) {
                filterGLThread.shutdown();
                this.eglThread = null;
            }
            this.textureView.setVisibility(8);
            return;
        }
        TextureView textureView = this.textureView;
        if (textureView instanceof VideoEditTextureView) {
            VideoEditTextureView videoEditTextureView = (VideoEditTextureView) textureView;
            MediaController.SavedFilterState savedFilterState = this.lastState;
            if (savedFilterState == null) {
                videoEditTextureView.setDelegate(null);
                return;
            }
            FilterGLThread filterGLThread2 = this.eglThread;
            if (filterGLThread2 != null) {
                filterGLThread2.setFilterGLThreadDelegate(FilterShaders.getFilterShadersDelegate(savedFilterState));
            }
        }
    }

    public void switchMode() {
        int i = this.selectedTool;
        if (i == 0) {
            this.blurControl.setVisibility(4);
            this.blurLayout.setVisibility(4);
            this.curveLayout.setVisibility(4);
            this.curvesControl.setVisibility(4);
            this.recyclerListView.setVisibility(0);
            return;
        }
        if (i == 1) {
            this.recyclerListView.setVisibility(4);
            this.curveLayout.setVisibility(4);
            this.curvesControl.setVisibility(4);
            this.blurLayout.setVisibility(0);
            if (this.blurType != 0) {
                this.blurControl.setVisibility(0);
            }
            updateSelectedBlurType();
            return;
        }
        if (i == 2) {
            this.recyclerListView.setVisibility(4);
            this.blurLayout.setVisibility(4);
            this.blurControl.setVisibility(4);
            this.curveLayout.setVisibility(0);
            this.curvesControl.setVisibility(0);
            this.curvesToolValue.activeType = 0;
            int i2 = 0;
            while (i2 < 4) {
                this.curveRadioButton[i2].setChecked(i2 == 0, false);
                i2++;
            }
        }
    }

    public void updateColors() {
        TextView textView = this.doneTextView;
        if (textView != null) {
            textView.setTextColor(getThemedColor(Theme.key_chat_editMediaButton));
        }
        ImageView imageView = this.tuneItem;
        if (imageView != null && imageView.getColorFilter() != null) {
            this.tuneItem.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_editMediaButton), PorterDuff.Mode.MULTIPLY));
        }
        ImageView imageView2 = this.blurItem;
        if (imageView2 != null && imageView2.getColorFilter() != null) {
            this.blurItem.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_editMediaButton), PorterDuff.Mode.MULTIPLY));
        }
        ImageView imageView3 = this.curveItem;
        if (imageView3 != null && imageView3.getColorFilter() != null) {
            this.curveItem.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_editMediaButton), PorterDuff.Mode.MULTIPLY));
        }
        updateSelectedBlurType();
    }

    public void updateUiBlurGradient(int i, int i2) {
        FilterGLThread filterGLThread = this.eglThread;
        if (filterGLThread != null) {
            filterGLThread.updateUiBlurGradient(i, i2);
        } else {
            this.gradientTop = i;
            this.gradientBottom = i2;
        }
    }
}
