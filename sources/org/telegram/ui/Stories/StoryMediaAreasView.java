package org.telegram.ui.Stories;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineHeightSpan;
import android.text.style.RelativeSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Paint.Views.LocationMarker;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.Shaker$$ExternalSyntheticLambda0;
import org.telegram.ui.EmojiAnimationsOverlay;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.Stories.StoryMediaAreasView;
import org.telegram.ui.Stories.recorder.HintView2;
import org.telegram.ui.Stories.recorder.StoryEntry;
import org.telegram.ui.Stories.recorder.Weather;

/* loaded from: classes5.dex */
public abstract class StoryMediaAreasView extends FrameLayout implements View.OnClickListener {
    private final Path clipPath;
    private final Paint cutPaint;
    private HintView2 hintView;
    private final FrameLayout hintsContainer;
    private ArrayList lastMediaAreas;
    private AreaView lastSelectedArea;
    private boolean malicious;
    Matrix matrix;
    private Bitmap parentBitmap;
    public final AnimatedFloat parentHighlightAlpha;
    public final AnimatedFloat parentHighlightScaleAlpha;
    private View parentView;
    float[] point;
    private final float[] radii;
    private final Rect rect;
    private final RectF rectF;
    private Theme.ResourcesProvider resourcesProvider;
    private AreaView selectedArea;
    private boolean shined;

    public static class AreaView extends View {
        public final ButtonBounce bounce;
        private boolean bounceOnTap;
        private final Path clipPath;
        private LinearGradient gradient;
        private final Matrix gradientMatrix;
        private final Paint gradientPaint;
        public final AnimatedFloat highlightAlpha;
        public final TL_stories.MediaArea mediaArea;
        private boolean ripple;
        private final Drawable rippleDrawable;
        private boolean scaleOnTap;
        private final Runnable shineRunnable;
        private boolean shining;
        private long startTime;
        private LinearGradient strokeGradient;
        private final Paint strokeGradientPaint;
        private boolean supportsBounds;
        private boolean supportsShining;

        public AreaView(Context context, View view, TL_stories.MediaArea mediaArea) {
            super(context);
            boolean z = true;
            this.gradientPaint = new Paint(1);
            Paint paint = new Paint(1);
            this.strokeGradientPaint = paint;
            this.gradientMatrix = new Matrix();
            Drawable createSelectorDrawable = Theme.createSelectorDrawable(1174405119, 2);
            this.rippleDrawable = createSelectorDrawable;
            this.bounce = new ButtonBounce(this);
            this.supportsBounds = false;
            this.supportsShining = false;
            this.shining = false;
            this.clipPath = new Path();
            this.shineRunnable = new Runnable() { // from class: org.telegram.ui.Stories.StoryMediaAreasView$AreaView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StoryMediaAreasView.AreaView.this.shineInternal();
                }
            };
            this.mediaArea = mediaArea;
            boolean z2 = mediaArea instanceof TL_stories.TL_mediaAreaGeoPoint;
            this.supportsBounds = z2 || (mediaArea instanceof TL_stories.TL_mediaAreaVenue) || (mediaArea instanceof TL_stories.TL_mediaAreaUrl);
            this.supportsShining = z2 || (mediaArea instanceof TL_stories.TL_mediaAreaVenue);
            if (!z2 && !(mediaArea instanceof TL_stories.TL_mediaAreaVenue) && (mediaArea.coordinates.flags & 1) == 0) {
                z = false;
            }
            this.scaleOnTap = z;
            this.ripple = z;
            this.bounceOnTap = z;
            this.highlightAlpha = new AnimatedFloat(view, 0L, 120L, new LinearInterpolator());
            paint.setStyle(Paint.Style.STROKE);
            createSelectorDrawable.setCallback(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void shineInternal() {
            if (this.supportsShining) {
                this.shining = true;
                this.startTime = System.currentTimeMillis();
                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                this.gradient = new LinearGradient(0.0f, 0.0f, 40.0f, 0.0f, new int[]{16777215, 771751935, 771751935, 16777215}, new float[]{0.0f, 0.4f, 0.6f, 1.0f}, tileMode);
                this.strokeGradient = new LinearGradient(0.0f, 0.0f, 40.0f, 0.0f, new int[]{16777215, 553648127, 553648127, 16777215}, new float[]{0.0f, 0.4f, 0.6f, 1.0f}, tileMode);
                invalidate();
            }
        }

        public void customDraw(Canvas canvas) {
        }

        @Override // android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                if (getParent() instanceof View) {
                    ButtonBounce buttonBounce = this.bounce;
                    View view = (View) getParent();
                    Objects.requireNonNull(view);
                    buttonBounce.setAdditionalInvalidate(new Shaker$$ExternalSyntheticLambda0(view));
                }
                this.bounce.setPressed(true);
                if (Build.VERSION.SDK_INT >= 21) {
                    this.rippleDrawable.setHotspot(motionEvent.getX(), motionEvent.getY());
                }
                this.rippleDrawable.setState(new int[]{R.attr.state_pressed, R.attr.state_enabled});
            } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                this.bounce.setPressed(false);
                this.rippleDrawable.setState(new int[0]);
            }
            super.dispatchTouchEvent(motionEvent);
            return true;
        }

        public void drawAbove(Canvas canvas) {
            if (this.ripple) {
                float innerRadius = getInnerRadius();
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getWidth(), getHeight());
                this.clipPath.rewind();
                this.clipPath.addRoundRect(rectF, innerRadius, innerRadius, Path.Direction.CW);
                canvas.save();
                canvas.clipPath(this.clipPath);
                this.rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
                this.rippleDrawable.draw(canvas);
                canvas.restore();
            }
        }

        public float getInnerRadius() {
            TL_stories.MediaArea mediaArea;
            TL_stories.MediaAreaCoordinates mediaAreaCoordinates;
            if (!(getParent() instanceof View) || (mediaArea = this.mediaArea) == null || (mediaAreaCoordinates = mediaArea.coordinates) == null) {
                return getMeasuredHeight() * 0.2f;
            }
            if ((mediaAreaCoordinates.flags & 1) == 0) {
                return getMeasuredHeight() * 0.2f;
            }
            double d = mediaAreaCoordinates.radius / 100.0d;
            double width = ((View) getParent()).getWidth();
            Double.isNaN(width);
            double d2 = d * width;
            double scaleX = getScaleX();
            Double.isNaN(scaleX);
            return (float) (d2 / scaleX);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float innerRadius = getInnerRadius();
            drawAbove(canvas);
            if (this.supportsShining && this.shining && this.gradient != null) {
                float measuredWidth = getMeasuredWidth() * 0.7f;
                float currentTimeMillis = (System.currentTimeMillis() - this.startTime) / 600.0f;
                float measuredWidth2 = ((getMeasuredWidth() + measuredWidth) * currentTimeMillis) - measuredWidth;
                if (currentTimeMillis >= 1.0f) {
                    this.shining = false;
                    return;
                }
                this.gradientMatrix.reset();
                this.gradientMatrix.postScale(measuredWidth / 40.0f, 1.0f);
                this.gradientMatrix.postTranslate(measuredWidth2, 0.0f);
                this.gradient.setLocalMatrix(this.gradientMatrix);
                this.gradientPaint.setShader(this.gradient);
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getWidth(), getHeight());
                canvas.drawRoundRect(rectF, innerRadius, innerRadius, this.gradientPaint);
                this.strokeGradient.setLocalMatrix(this.gradientMatrix);
                this.strokeGradientPaint.setShader(this.strokeGradient);
                float dpf2 = AndroidUtilities.dpf2(1.5f);
                this.strokeGradientPaint.setStrokeWidth(dpf2);
                float f = dpf2 / 2.0f;
                rectF.inset(f, f);
                float f2 = innerRadius - f;
                canvas.drawRoundRect(rectF, f2, f2, this.strokeGradientPaint);
                invalidate();
            }
        }

        public void shine() {
            if (this.supportsShining) {
                AndroidUtilities.cancelRunOnUIThread(this.shineRunnable);
                AndroidUtilities.runOnUIThread(this.shineRunnable, 400L);
            }
        }

        @Override // android.view.View
        protected boolean verifyDrawable(Drawable drawable) {
            return drawable == this.rippleDrawable || super.verifyDrawable(drawable);
        }
    }

    public static class FitViewWidget extends FrameLayout {
        public final View child;
        public final TL_stories.MediaArea mediaArea;

        public FitViewWidget(Context context, View view, TL_stories.MediaArea mediaArea) {
            super(context);
            this.mediaArea = mediaArea;
            this.child = view;
            addView(view);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            this.child.measure(i, i2);
            int measuredWidth = (this.child.getMeasuredWidth() - this.child.getPaddingLeft()) - this.child.getPaddingRight();
            int measuredHeight = (this.child.getMeasuredHeight() - this.child.getPaddingTop()) - this.child.getPaddingBottom();
            float f = measuredWidth;
            float f2 = f / 2.0f;
            this.child.setPivotX(r2.getPaddingLeft() + f2);
            float f3 = measuredHeight;
            float f4 = f3 / 2.0f;
            this.child.setPivotY(r2.getPaddingTop() + f4);
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            setMeasuredDimension(size, size2);
            float f5 = size;
            float f6 = size2;
            float min = Math.min(f5 / f, f6 / f3);
            this.child.setTranslationX((f5 / 2.0f) - (f2 + r1.getPaddingLeft()));
            this.child.setTranslationY((f6 / 2.0f) - (f4 + r8.getPaddingTop()));
            this.child.setScaleX(min);
            this.child.setScaleY(min);
        }
    }

    public StoryMediaAreasView(Context context, View view, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.lastSelectedArea = null;
        this.selectedArea = null;
        this.hintView = null;
        this.matrix = new Matrix();
        this.point = new float[2];
        this.rect = new Rect();
        this.rectF = new RectF();
        Paint paint = new Paint(1);
        this.cutPaint = paint;
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        paint.setColor(-1);
        this.clipPath = new Path();
        this.radii = new float[8];
        this.shined = false;
        this.parentView = view;
        this.resourcesProvider = resourcesProvider;
        this.parentHighlightAlpha = new AnimatedFloat(view, 0L, 120L, new LinearInterpolator());
        this.parentHighlightScaleAlpha = new AnimatedFloat(view, 0L, 360L, CubicBezierInterpolator.EASE_OUT_QUINT);
        setClipChildren(false);
        FrameLayout frameLayout = new FrameLayout(context);
        this.hintsContainer = frameLayout;
        addView(frameLayout);
        setLayerType(2, null);
    }

    private void drawHighlight(Canvas canvas) {
        float measuredHeight;
        AnimatedFloat animatedFloat = this.parentHighlightAlpha;
        AreaView areaView = this.selectedArea;
        float f = animatedFloat.set((areaView == null || !areaView.supportsBounds || this.selectedArea.scaleOnTap) ? false : true);
        AreaView areaView2 = this.selectedArea;
        boolean z = areaView2 != null && areaView2.scaleOnTap;
        float f2 = this.parentHighlightScaleAlpha.set(z);
        if (f > 0.0f) {
            canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), NotificationCenter.liveLocationsChanged, 31);
            canvas.drawColor(Theme.multAlpha(402653184, f));
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                if (childAt != this.hintsContainer) {
                    AnimatedFloat animatedFloat2 = ((AreaView) childAt).highlightAlpha;
                    AreaView areaView3 = this.selectedArea;
                    float f3 = animatedFloat2.set(childAt == areaView3 && areaView3.supportsBounds);
                    if (f3 > 0.0f) {
                        canvas.save();
                        this.rectF.set(childAt.getX(), childAt.getY(), childAt.getX() + childAt.getMeasuredWidth(), childAt.getY() + childAt.getMeasuredHeight());
                        canvas.rotate(childAt.getRotation(), this.rectF.centerX(), this.rectF.centerY());
                        this.cutPaint.setAlpha((int) (f3 * 255.0f));
                        RectF rectF = this.rectF;
                        canvas.drawRoundRect(rectF, rectF.height() * 0.2f, this.rectF.height() * 0.2f, this.cutPaint);
                        canvas.restore();
                    }
                }
            }
            canvas.restore();
        }
        if ((z || f2 > 0.0f) && this.lastSelectedArea != null) {
            if (this.parentBitmap == null) {
                this.parentBitmap = getPlayingBitmap();
            }
            if (this.parentBitmap != null) {
                canvas.drawColor(Theme.multAlpha(805306368, f2));
                canvas.save();
                this.clipPath.rewind();
                this.rectF.set(this.lastSelectedArea.getX(), this.lastSelectedArea.getY(), this.lastSelectedArea.getX() + this.lastSelectedArea.getMeasuredWidth(), this.lastSelectedArea.getY() + this.lastSelectedArea.getMeasuredHeight());
                float lerp = AndroidUtilities.lerp(1.0f, (this.lastSelectedArea.bounceOnTap ? this.lastSelectedArea.bounce.getScale(0.05f) : 1.0f) * 1.05f, f2);
                canvas.scale(lerp, lerp, this.rectF.centerX(), this.rectF.centerY());
                canvas.rotate(this.lastSelectedArea.getRotation(), this.rectF.centerX(), this.rectF.centerY());
                TL_stories.MediaAreaCoordinates mediaAreaCoordinates = this.lastSelectedArea.mediaArea.coordinates;
                if ((mediaAreaCoordinates.flags & 1) != 0) {
                    double d = mediaAreaCoordinates.radius / 100.0d;
                    double width = getWidth();
                    Double.isNaN(width);
                    measuredHeight = (float) (d * width);
                } else {
                    measuredHeight = r3.getMeasuredHeight() * 0.2f;
                }
                this.clipPath.addRoundRect(this.rectF, measuredHeight, measuredHeight, Path.Direction.CW);
                canvas.clipPath(this.clipPath);
                RectF rectF2 = AndroidUtilities.rectTmp;
                rectF2.set(0.0f, 0.0f, getWidth(), getHeight());
                this.rect.set(0, 0, this.parentBitmap.getWidth(), this.parentBitmap.getHeight());
                canvas.rotate(-this.lastSelectedArea.getRotation(), this.rectF.centerX(), this.rectF.centerY());
                canvas.drawBitmap(this.parentBitmap, this.rect, rectF2, (Paint) null);
                canvas.restore();
                canvas.save();
                canvas.translate(this.lastSelectedArea.getX(), this.lastSelectedArea.getY());
                canvas.rotate(this.lastSelectedArea.getRotation(), this.lastSelectedArea.getPivotX(), this.lastSelectedArea.getPivotY());
                canvas.scale(this.lastSelectedArea.getScaleX() * lerp, this.lastSelectedArea.getScaleY() * lerp, this.lastSelectedArea.getPivotX(), this.lastSelectedArea.getPivotY());
                this.lastSelectedArea.drawAbove(canvas);
                canvas.restore();
            }
        } else {
            Bitmap bitmap = this.parentBitmap;
            if (bitmap != null) {
                bitmap.recycle();
                this.parentBitmap = null;
            }
        }
        invalidate();
    }

    public static ArrayList getMediaAreasFor(StoryEntry storyEntry) {
        if (storyEntry == null || storyEntry.mediaEntities == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < storyEntry.mediaEntities.size(); i++) {
            if (((VideoEditedInfo.MediaEntity) storyEntry.mediaEntities.get(i)).mediaArea instanceof TL_stories.TL_mediaAreaSuggestedReaction) {
                arrayList.add(((VideoEditedInfo.MediaEntity) storyEntry.mediaEntities.get(i)).mediaArea);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$0() {
        HintView2 hintView2 = this.hintView;
        if (hintView2 != null) {
            hintView2.hide();
            this.hintView = null;
        }
        onHintVisible(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$1(HintView2 hintView2) {
        this.hintsContainer.removeView(hintView2);
        if (hintView2 == this.hintView) {
            this.selectedArea = null;
            invalidate();
            onHintVisible(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClick$2(View view) {
        onClick(this.selectedArea);
    }

    private void onClickAway() {
        HintView2 hintView2 = this.hintView;
        if (hintView2 != null) {
            hintView2.hide();
            this.hintView = null;
        }
        this.selectedArea = null;
        invalidate();
        onHintVisible(false);
        if (this.malicious) {
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                if (childAt != this.hintsContainer) {
                    childAt.setClickable(false);
                }
            }
        }
    }

    private static boolean rotatedRectContainsPoint(float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8 = f6 - f;
        double radians = Math.toRadians(-f5);
        double d = f8;
        double cos = Math.cos(radians);
        Double.isNaN(d);
        double d2 = f7 - f2;
        double sin = Math.sin(radians);
        Double.isNaN(d2);
        float f9 = (float) ((cos * d) - (sin * d2));
        double sin2 = Math.sin(radians);
        Double.isNaN(d);
        double cos2 = Math.cos(radians);
        Double.isNaN(d2);
        float f10 = (float) ((d * sin2) + (d2 * cos2));
        return f9 >= (-f3) / 2.0f && f9 <= f3 / 2.0f && f10 >= (-f4) / 2.0f && f10 <= f4 / 2.0f;
    }

    public void closeHint() {
        HintView2 hintView2 = this.hintView;
        if (hintView2 != null) {
            hintView2.hide();
            this.hintView = null;
        }
        this.selectedArea = null;
        invalidate();
        onHintVisible(false);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        if (view == this.hintsContainer) {
            drawHighlight(canvas);
        } else if (view instanceof AreaView) {
            canvas.save();
            canvas.translate(view.getLeft(), view.getTop());
            canvas.concat(view.getMatrix());
            ((AreaView) view).customDraw(canvas);
            canvas.restore();
        }
        return super.drawChild(canvas, view, j);
    }

    protected Bitmap getPlayingBitmap() {
        return null;
    }

    public boolean hasAreaAboveAt(float f, float f2) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if ((childAt instanceof StoryReactionWidgetView) && rotatedRectContainsPoint(childAt.getTranslationX(), childAt.getTranslationY(), childAt.getMeasuredWidth(), childAt.getMeasuredHeight(), childAt.getRotation(), f, f2)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasClickableViews(float f, float f2) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt != this.hintsContainer && (childAt instanceof StoryReactionWidgetView)) {
                childAt.getMatrix().invert(this.matrix);
                float[] fArr = this.point;
                fArr[0] = f;
                fArr[1] = f2;
                this.matrix.mapPoints(fArr);
                if (this.point[0] >= childAt.getLeft() && this.point[0] <= childAt.getRight() && this.point[1] >= childAt.getTop() && this.point[1] <= childAt.getBottom()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasSelected() {
        return this.selectedArea != null;
    }

    public boolean hasSelectedForScale() {
        AreaView areaView = this.selectedArea;
        return areaView != null && (areaView.scaleOnTap || this.selectedArea.supportsBounds);
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x020c  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0218  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x023e  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x024a  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0267  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0278  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0295  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0298  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x02a6  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x02fb  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x02fd  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x029a  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x027b  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x024d  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0241  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x021b  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x020f  */
    @Override // android.view.View.OnClickListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onClick(View view) {
        int i;
        boolean z;
        boolean z2;
        AreaView areaView;
        float f;
        float dp;
        float f2;
        TLRPC.MessageMedia tL_messageMediaGeo;
        TLRPC.GeoPoint geoPoint;
        if (!(view instanceof AreaView)) {
            return;
        }
        if (view instanceof StoryReactionWidgetView) {
            showEffect((StoryReactionWidgetView) view);
            return;
        }
        AreaView areaView2 = this.selectedArea;
        int i2 = 3;
        if (areaView2 == view) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoryMediaAreasView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StoryMediaAreasView.this.lambda$onClick$0();
                }
            }, 200L);
            TL_stories.MediaArea mediaArea = this.selectedArea.mediaArea;
            if (mediaArea instanceof TL_stories.TL_mediaAreaChannelPost) {
                Bundle bundle = new Bundle();
                bundle.putLong("chat_id", ((TL_stories.TL_mediaAreaChannelPost) this.selectedArea.mediaArea).channel_id);
                bundle.putInt("message_id", ((TL_stories.TL_mediaAreaChannelPost) this.selectedArea.mediaArea).msg_id);
                presentFragment(new ChatActivity(bundle));
                this.selectedArea = null;
                invalidate();
                return;
            }
            if (mediaArea instanceof TL_stories.TL_mediaAreaUrl) {
                Browser.openUrl(getContext(), ((TL_stories.TL_mediaAreaUrl) this.selectedArea.mediaArea).url);
                this.selectedArea = null;
                invalidate();
                return;
            }
            if (mediaArea instanceof TL_stories.TL_mediaAreaStarGift) {
                String str = ((TL_stories.TL_mediaAreaStarGift) mediaArea).slug;
                Browser.openUrl(getContext(), "https://" + MessagesController.getInstance(UserConfig.selectedAccount).linkPrefix + "/nft/" + str);
                this.selectedArea = null;
                invalidate();
                return;
            }
            LocationActivity locationActivity = new LocationActivity(i2) { // from class: org.telegram.ui.Stories.StoryMediaAreasView.1
                @Override // org.telegram.ui.LocationActivity
                protected boolean disablePermissionCheck() {
                    return true;
                }
            };
            locationActivity.fromStories = true;
            locationActivity.searchStories(this.selectedArea.mediaArea);
            locationActivity.setResourceProvider(this.resourcesProvider);
            TLRPC.TL_message tL_message = new TLRPC.TL_message();
            TL_stories.MediaArea mediaArea2 = this.selectedArea.mediaArea;
            if (mediaArea2 instanceof TL_stories.TL_mediaAreaVenue) {
                TL_stories.TL_mediaAreaVenue tL_mediaAreaVenue = (TL_stories.TL_mediaAreaVenue) mediaArea2;
                tL_messageMediaGeo = new TLRPC.TL_messageMediaVenue();
                tL_messageMediaGeo.venue_id = tL_mediaAreaVenue.venue_id;
                tL_messageMediaGeo.venue_type = tL_mediaAreaVenue.venue_type;
                tL_messageMediaGeo.title = tL_mediaAreaVenue.title;
                tL_messageMediaGeo.address = tL_mediaAreaVenue.address;
                tL_messageMediaGeo.provider = tL_mediaAreaVenue.provider;
                geoPoint = tL_mediaAreaVenue.geo;
            } else if (!(mediaArea2 instanceof TL_stories.TL_mediaAreaGeoPoint)) {
                this.selectedArea = null;
                invalidate();
                return;
            } else {
                locationActivity.setInitialMaxZoom(true);
                TL_stories.TL_mediaAreaGeoPoint tL_mediaAreaGeoPoint = (TL_stories.TL_mediaAreaGeoPoint) this.selectedArea.mediaArea;
                tL_messageMediaGeo = new TLRPC.TL_messageMediaGeo();
                geoPoint = tL_mediaAreaGeoPoint.geo;
            }
            tL_messageMediaGeo.geo = geoPoint;
            tL_message.media = tL_messageMediaGeo;
            locationActivity.setSharingAllowed(false);
            locationActivity.setMessageObject(new MessageObject(UserConfig.selectedAccount, tL_message, false, false));
            presentFragment(locationActivity);
            this.selectedArea = null;
            invalidate();
            return;
        }
        if (areaView2 != null && this.malicious) {
            onClickAway();
            return;
        }
        AreaView areaView3 = (AreaView) view;
        this.lastSelectedArea = areaView3;
        this.selectedArea = areaView3;
        invalidate();
        HintView2 hintView2 = this.hintView;
        if (hintView2 != null) {
            hintView2.hide();
            this.hintView = null;
        }
        final HintView2 duration = new HintView2(getContext()).setSelectorColor(687865855).setJointPx(0.0f, this.selectedArea.getTranslationX() - AndroidUtilities.dp(8.0f)).setDuration(5000L);
        this.hintView = duration;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        TL_stories.MediaArea mediaArea3 = this.selectedArea.mediaArea;
        if (mediaArea3 instanceof TL_stories.TL_mediaAreaChannelPost) {
            i = org.telegram.messenger.R.string.StoryViewMessage;
        } else if (mediaArea3 instanceof TL_stories.TL_mediaAreaStarGift) {
            i = org.telegram.messenger.R.string.StoryViewGift;
        } else {
            if (mediaArea3 instanceof TL_stories.TL_mediaAreaUrl) {
                duration.setMultilineText(true);
                spannableStringBuilder.append((CharSequence) LocaleController.getString(org.telegram.messenger.R.string.StoryOpenLink));
                spannableStringBuilder.append((CharSequence) "\n");
                TL_stories.TL_mediaAreaUrl tL_mediaAreaUrl = (TL_stories.TL_mediaAreaUrl) this.selectedArea.mediaArea;
                int length = spannableStringBuilder.length();
                spannableStringBuilder.append(TextUtils.ellipsize(tL_mediaAreaUrl.url, this.hintView.getTextPaint(), AndroidUtilities.displaySize.x * 0.6f, TextUtils.TruncateAt.END));
                spannableStringBuilder.setSpan(new RelativeSizeSpan(0.85f), length, spannableStringBuilder.length(), 33);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Theme.multAlpha(-1, 0.6f)), length, spannableStringBuilder.length(), 33);
                spannableStringBuilder.setSpan(new LineHeightSpan() { // from class: org.telegram.ui.Stories.StoryMediaAreasView.2
                    @Override // android.text.style.LineHeightSpan
                    public void chooseHeight(CharSequence charSequence, int i3, int i4, int i5, int i6, Paint.FontMetricsInt fontMetricsInt) {
                        fontMetricsInt.ascent -= AndroidUtilities.dp(2.0f);
                        fontMetricsInt.top -= AndroidUtilities.dp(2.0f);
                    }
                }, length, spannableStringBuilder.length(), 33);
                duration.setInnerPadding(11.0f, 7.0f, 11.0f, 7.0f);
                z = true;
                SpannableString spannableString = new SpannableString(">");
                ColoredImageSpan coloredImageSpan = new ColoredImageSpan(org.telegram.messenger.R.drawable.photos_arrow);
                coloredImageSpan.translate(AndroidUtilities.dp(!z ? 1.0f : 2.0f), AndroidUtilities.dp(!z ? 0.0f : 1.0f));
                spannableString.setSpan(coloredImageSpan, 0, spannableString.length(), 33);
                SpannableString spannableString2 = new SpannableString("<");
                ColoredImageSpan coloredImageSpan2 = new ColoredImageSpan(org.telegram.messenger.R.drawable.attach_arrow_right);
                coloredImageSpan2.translate(AndroidUtilities.dp(!z ? -1.0f : -2.0f), AndroidUtilities.dp(!z ? 0.0f : 1.0f));
                coloredImageSpan2.setScale(-1.0f, 1.0f);
                spannableString2.setSpan(coloredImageSpan2, 0, spannableString2.length(), 33);
                if (AndroidUtilities.isRTL(spannableStringBuilder)) {
                    spannableString = spannableString2;
                }
                AndroidUtilities.replaceCharSequence(">", spannableStringBuilder, spannableString);
                duration.setText(spannableStringBuilder);
                duration.setOnHiddenListener(new Runnable() { // from class: org.telegram.ui.Stories.StoryMediaAreasView$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        StoryMediaAreasView.this.lambda$onClick$1(duration);
                    }
                });
                float f3 = !z ? 100 : 50;
                z2 = this.selectedArea.getTranslationY() - ((float) AndroidUtilities.dp(f3)) < ((float) AndroidUtilities.dp(100.0f));
                duration.setDirection(!z2 ? 1 : 3);
                areaView = this.selectedArea;
                if (areaView.mediaArea instanceof TL_stories.TL_mediaAreaChannelPost) {
                    float translationY = areaView.getTranslationY();
                    float measuredHeight = this.selectedArea.getMeasuredHeight() / 2.0f;
                    if (!z2 ? (translationY - measuredHeight) - AndroidUtilities.dp(f3) < AndroidUtilities.dp(120.0f) : translationY + measuredHeight > getMeasuredHeight() - AndroidUtilities.dp(120.0f)) {
                        f = this.selectedArea.getTranslationY();
                        dp = this.selectedArea.getMeasuredHeight() / 3.0f;
                        f2 = f - dp;
                        duration.setTranslationY(f2);
                        duration.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.StoryMediaAreasView$$ExternalSyntheticLambda2
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                StoryMediaAreasView.this.lambda$onClick$2(view2);
                            }
                        });
                        duration.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                        this.hintsContainer.addView(duration, LayoutHelper.createFrame(-1, f3));
                        duration.show();
                        onHintVisible(true);
                        return;
                    }
                }
                float translationY2 = this.selectedArea.getTranslationY();
                float measuredHeight2 = this.selectedArea.getMeasuredHeight() / 2.0f;
                if (!z2) {
                    f2 = translationY2 + measuredHeight2;
                    duration.setTranslationY(f2);
                    duration.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.StoryMediaAreasView$$ExternalSyntheticLambda2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            StoryMediaAreasView.this.lambda$onClick$2(view2);
                        }
                    });
                    duration.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                    this.hintsContainer.addView(duration, LayoutHelper.createFrame(-1, f3));
                    duration.show();
                    onHintVisible(true);
                    return;
                }
                f = translationY2 - measuredHeight2;
                dp = AndroidUtilities.dp(f3);
                f2 = f - dp;
                duration.setTranslationY(f2);
                duration.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.StoryMediaAreasView$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        StoryMediaAreasView.this.lambda$onClick$2(view2);
                    }
                });
                duration.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                this.hintsContainer.addView(duration, LayoutHelper.createFrame(-1, f3));
                duration.show();
                onHintVisible(true);
                return;
            }
            i = org.telegram.messenger.R.string.StoryViewLocation;
        }
        spannableStringBuilder.append((CharSequence) LocaleController.getString(i));
        z = false;
        SpannableString spannableString3 = new SpannableString(">");
        ColoredImageSpan coloredImageSpan3 = new ColoredImageSpan(org.telegram.messenger.R.drawable.photos_arrow);
        coloredImageSpan3.translate(AndroidUtilities.dp(!z ? 1.0f : 2.0f), AndroidUtilities.dp(!z ? 0.0f : 1.0f));
        spannableString3.setSpan(coloredImageSpan3, 0, spannableString3.length(), 33);
        SpannableString spannableString22 = new SpannableString("<");
        ColoredImageSpan coloredImageSpan22 = new ColoredImageSpan(org.telegram.messenger.R.drawable.attach_arrow_right);
        coloredImageSpan22.translate(AndroidUtilities.dp(!z ? -1.0f : -2.0f), AndroidUtilities.dp(!z ? 0.0f : 1.0f));
        coloredImageSpan22.setScale(-1.0f, 1.0f);
        spannableString22.setSpan(coloredImageSpan22, 0, spannableString22.length(), 33);
        if (AndroidUtilities.isRTL(spannableStringBuilder)) {
        }
        AndroidUtilities.replaceCharSequence(">", spannableStringBuilder, spannableString3);
        duration.setText(spannableStringBuilder);
        duration.setOnHiddenListener(new Runnable() { // from class: org.telegram.ui.Stories.StoryMediaAreasView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                StoryMediaAreasView.this.lambda$onClick$1(duration);
            }
        });
        float f32 = !z ? 100 : 50;
        if (this.selectedArea.getTranslationY() - ((float) AndroidUtilities.dp(f32)) < ((float) AndroidUtilities.dp(100.0f))) {
        }
        duration.setDirection(!z2 ? 1 : 3);
        areaView = this.selectedArea;
        if (areaView.mediaArea instanceof TL_stories.TL_mediaAreaChannelPost) {
        }
        float translationY22 = this.selectedArea.getTranslationY();
        float measuredHeight22 = this.selectedArea.getMeasuredHeight() / 2.0f;
        if (!z2) {
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Bitmap bitmap = this.parentBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.parentBitmap = null;
        }
    }

    protected abstract void onHintVisible(boolean z);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        TL_stories.MediaArea mediaArea;
        FitViewWidget fitViewWidget;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            if (childAt == this.hintsContainer) {
                childAt.layout(0, 0, i3 - i, i4 - i2);
            } else {
                if (childAt instanceof AreaView) {
                    AreaView areaView = (AreaView) childAt;
                    int measuredWidth = areaView.getMeasuredWidth();
                    int measuredHeight = areaView.getMeasuredHeight();
                    areaView.layout((-measuredWidth) / 2, (-measuredHeight) / 2, measuredWidth / 2, measuredHeight / 2);
                    double d = areaView.mediaArea.coordinates.x / 100.0d;
                    double measuredWidth2 = getMeasuredWidth();
                    Double.isNaN(measuredWidth2);
                    areaView.setTranslationX((float) (d * measuredWidth2));
                    double d2 = areaView.mediaArea.coordinates.y / 100.0d;
                    double measuredHeight2 = getMeasuredHeight();
                    Double.isNaN(measuredHeight2);
                    areaView.setTranslationY((float) (d2 * measuredHeight2));
                    mediaArea = areaView.mediaArea;
                    fitViewWidget = areaView;
                } else if (childAt instanceof FitViewWidget) {
                    FitViewWidget fitViewWidget2 = (FitViewWidget) childAt;
                    int measuredWidth3 = fitViewWidget2.getMeasuredWidth();
                    int measuredHeight3 = fitViewWidget2.getMeasuredHeight();
                    fitViewWidget2.layout((-measuredWidth3) / 2, (-measuredHeight3) / 2, measuredWidth3 / 2, measuredHeight3 / 2);
                    double d3 = fitViewWidget2.mediaArea.coordinates.x / 100.0d;
                    double measuredWidth4 = getMeasuredWidth();
                    Double.isNaN(measuredWidth4);
                    fitViewWidget2.setTranslationX((float) (d3 * measuredWidth4));
                    double d4 = fitViewWidget2.mediaArea.coordinates.y / 100.0d;
                    double measuredHeight4 = getMeasuredHeight();
                    Double.isNaN(measuredHeight4);
                    fitViewWidget2.setTranslationY((float) (d4 * measuredHeight4));
                    mediaArea = fitViewWidget2.mediaArea;
                    fitViewWidget = fitViewWidget2;
                }
                fitViewWidget.setRotation((float) mediaArea.coordinates.rotation);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int makeMeasureSpec;
        TL_stories.MediaArea mediaArea;
        FitViewWidget fitViewWidget;
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            FrameLayout frameLayout = this.hintsContainer;
            if (childAt == frameLayout) {
                frameLayout.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
            } else {
                if (childAt instanceof AreaView) {
                    AreaView areaView = (AreaView) getChildAt(i3);
                    double d = areaView.mediaArea.coordinates.w / 100.0d;
                    double d2 = size;
                    Double.isNaN(d2);
                    makeMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) Math.ceil(d * d2), 1073741824);
                    mediaArea = areaView.mediaArea;
                    fitViewWidget = areaView;
                } else if (childAt instanceof FitViewWidget) {
                    FitViewWidget fitViewWidget2 = (FitViewWidget) getChildAt(i3);
                    double d3 = fitViewWidget2.mediaArea.coordinates.w / 100.0d;
                    double d4 = size;
                    Double.isNaN(d4);
                    makeMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) Math.ceil(d3 * d4), 1073741824);
                    mediaArea = fitViewWidget2.mediaArea;
                    fitViewWidget = fitViewWidget2;
                }
                double d5 = mediaArea.coordinates.h / 100.0d;
                double d6 = size2;
                Double.isNaN(d6);
                fitViewWidget.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec((int) Math.ceil(d5 * d6), 1073741824));
            }
        }
        setMeasuredDimension(size, size2);
    }

    public void onStoryItemUpdated(TL_stories.StoryItem storyItem, boolean z) {
        if (storyItem == null) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof StoryReactionWidgetView) {
                ((StoryReactionWidgetView) getChildAt(i)).setViews(storyItem.views, z);
            }
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        HintView2 hintView2;
        if (getChildCount() == 0 || (hintView2 = this.hintView) == null || !hintView2.shown()) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            onClickAway();
        }
        super.onTouchEvent(motionEvent);
        return true;
    }

    protected abstract void presentFragment(BaseFragment baseFragment);

    public void set(TL_stories.StoryItem storyItem, ArrayList arrayList, EmojiAnimationsOverlay emojiAnimationsOverlay) {
        View view;
        ArrayList arrayList2 = this.lastMediaAreas;
        if (arrayList == arrayList2 && (arrayList == null || arrayList2 == null || arrayList.size() == this.lastMediaAreas.size())) {
            return;
        }
        HintView2 hintView2 = this.hintView;
        if (hintView2 != null) {
            hintView2.hide();
            this.hintView = null;
        }
        int i = 0;
        while (i < getChildCount()) {
            View childAt = getChildAt(i);
            if (childAt != this.hintsContainer) {
                removeView(childAt);
                i--;
            }
            i++;
        }
        this.selectedArea = null;
        this.parentHighlightScaleAlpha.set(0.0f, true);
        invalidate();
        onHintVisible(false);
        this.malicious = false;
        this.lastMediaAreas = arrayList;
        if (arrayList == null) {
            return;
        }
        this.shined = false;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            TL_stories.MediaArea mediaArea = (TL_stories.MediaArea) arrayList.get(i2);
            if (mediaArea != null && mediaArea.coordinates != null) {
                if (mediaArea instanceof TL_stories.TL_mediaAreaSuggestedReaction) {
                    StoryReactionWidgetView storyReactionWidgetView = new StoryReactionWidgetView(getContext(), this, (TL_stories.TL_mediaAreaSuggestedReaction) mediaArea, emojiAnimationsOverlay);
                    if (storyItem != null) {
                        storyReactionWidgetView.setViews(storyItem.views, false);
                    }
                    ScaleStateListAnimator.apply(storyReactionWidgetView);
                    view = storyReactionWidgetView;
                } else if (mediaArea instanceof TL_stories.TL_mediaAreaWeather) {
                    TL_stories.TL_mediaAreaWeather tL_mediaAreaWeather = (TL_stories.TL_mediaAreaWeather) mediaArea;
                    Weather.State state = new Weather.State();
                    state.emoji = tL_mediaAreaWeather.emoji;
                    state.temperature = (float) tL_mediaAreaWeather.temperature_c;
                    LocationMarker locationMarker = new LocationMarker(getContext(), 1, AndroidUtilities.density, 0);
                    locationMarker.setMaxWidth(AndroidUtilities.displaySize.x);
                    locationMarker.setIsVideo(true);
                    locationMarker.setCodeEmoji(UserConfig.selectedAccount, state.getEmoji());
                    locationMarker.setText(state.getTemperature());
                    locationMarker.setType(3, tL_mediaAreaWeather.color);
                    view = new FitViewWidget(getContext(), locationMarker, mediaArea);
                } else {
                    view = new AreaView(getContext(), this.parentView, mediaArea);
                }
                view.setOnClickListener(this);
                addView(view);
                double d = mediaArea.coordinates.w;
            }
        }
        this.malicious = false;
        this.hintsContainer.bringToFront();
    }

    public void set(TL_stories.StoryItem storyItem, EmojiAnimationsOverlay emojiAnimationsOverlay) {
        set(storyItem, storyItem != null ? storyItem.media_areas : null, emojiAnimationsOverlay);
    }

    public void shine() {
        if (this.shined) {
            return;
        }
        this.shined = true;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof AreaView) {
                ((AreaView) childAt).shine();
            }
        }
    }

    public abstract void showEffect(StoryReactionWidgetView storyReactionWidgetView);
}
