package org.telegram.ui.Stories;

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
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageMediaGeo;
import org.telegram.tgnet.TLRPC$TL_messageMediaVenue;
import org.telegram.tgnet.tl.TL_stories$MediaArea;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.tgnet.tl.TL_stories$TL_mediaAreaChannelPost;
import org.telegram.tgnet.tl.TL_stories$TL_mediaAreaCoordinates;
import org.telegram.tgnet.tl.TL_stories$TL_mediaAreaGeoPoint;
import org.telegram.tgnet.tl.TL_stories$TL_mediaAreaSuggestedReaction;
import org.telegram.tgnet.tl.TL_stories$TL_mediaAreaVenue;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.EmojiAnimationsOverlay;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.Stories.StoryMediaAreasView;
import org.telegram.ui.Stories.recorder.HintView2;
import org.telegram.ui.Stories.recorder.StoryEntry;
/* loaded from: classes4.dex */
public class StoryMediaAreasView extends FrameLayout implements View.OnClickListener {
    private final Path clipPath;
    private final Paint cutPaint;
    private HintView2 hintView;
    private final FrameLayout hintsContainer;
    private ArrayList<TL_stories$MediaArea> lastMediaAreas;
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

    protected Bitmap getPlayingBitmap() {
        return null;
    }

    protected void onHintVisible(boolean z) {
    }

    protected void presentFragment(BaseFragment baseFragment) {
    }

    public void showEffect(StoryReactionWidgetView storyReactionWidgetView) {
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
    }

    public static ArrayList<TL_stories$MediaArea> getMediaAreasFor(StoryEntry storyEntry) {
        if (storyEntry == null || storyEntry.mediaEntities == null) {
            return null;
        }
        ArrayList<TL_stories$MediaArea> arrayList = new ArrayList<>();
        for (int i = 0; i < storyEntry.mediaEntities.size(); i++) {
            if (storyEntry.mediaEntities.get(i).mediaArea instanceof TL_stories$TL_mediaAreaSuggestedReaction) {
                arrayList.add(storyEntry.mediaEntities.get(i).mediaArea);
            }
        }
        return arrayList;
    }

    public void set(TL_stories$StoryItem tL_stories$StoryItem, EmojiAnimationsOverlay emojiAnimationsOverlay) {
        set(tL_stories$StoryItem, tL_stories$StoryItem != null ? tL_stories$StoryItem.media_areas : null, emojiAnimationsOverlay);
    }

    public void set(TL_stories$StoryItem tL_stories$StoryItem, ArrayList<TL_stories$MediaArea> arrayList, EmojiAnimationsOverlay emojiAnimationsOverlay) {
        StoryReactionWidgetView storyReactionWidgetView;
        ArrayList<TL_stories$MediaArea> arrayList2 = this.lastMediaAreas;
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
            TL_stories$MediaArea tL_stories$MediaArea = arrayList.get(i2);
            if (tL_stories$MediaArea != null && tL_stories$MediaArea.coordinates != null) {
                if (tL_stories$MediaArea instanceof TL_stories$TL_mediaAreaSuggestedReaction) {
                    StoryReactionWidgetView storyReactionWidgetView2 = new StoryReactionWidgetView(getContext(), this, (TL_stories$TL_mediaAreaSuggestedReaction) tL_stories$MediaArea, emojiAnimationsOverlay);
                    if (tL_stories$StoryItem != null) {
                        storyReactionWidgetView2.setViews(tL_stories$StoryItem.views, false);
                    }
                    ScaleStateListAnimator.apply(storyReactionWidgetView2);
                    storyReactionWidgetView = storyReactionWidgetView2;
                } else {
                    storyReactionWidgetView = new AreaView(getContext(), this.parentView, tL_stories$MediaArea);
                }
                storyReactionWidgetView.setOnClickListener(this);
                addView(storyReactionWidgetView);
                TL_stories$TL_mediaAreaCoordinates tL_stories$TL_mediaAreaCoordinates = tL_stories$MediaArea.coordinates;
                double d = tL_stories$TL_mediaAreaCoordinates.w;
                double d2 = tL_stories$TL_mediaAreaCoordinates.h;
            }
        }
        this.malicious = false;
        this.hintsContainer.bringToFront();
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            View childAt = getChildAt(i3);
            FrameLayout frameLayout = this.hintsContainer;
            if (childAt == frameLayout) {
                frameLayout.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
            } else if (childAt instanceof AreaView) {
                AreaView areaView = (AreaView) getChildAt(i3);
                double d = size;
                Double.isNaN(d);
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) Math.ceil((areaView.mediaArea.coordinates.w / 100.0d) * d), 1073741824);
                double d2 = size2;
                Double.isNaN(d2);
                areaView.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec((int) Math.ceil((areaView.mediaArea.coordinates.h / 100.0d) * d2), 1073741824));
            }
        }
        setMeasuredDimension(size, size2);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view instanceof AreaView) {
            if (view instanceof StoryReactionWidgetView) {
                showEffect((StoryReactionWidgetView) view);
                return;
            }
            AreaView areaView = this.selectedArea;
            if (areaView == view) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stories.StoryMediaAreasView$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        StoryMediaAreasView.this.lambda$onClick$0();
                    }
                }, 200L);
                if (this.selectedArea.mediaArea instanceof TL_stories$TL_mediaAreaChannelPost) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("chat_id", ((TL_stories$TL_mediaAreaChannelPost) this.selectedArea.mediaArea).channel_id);
                    bundle.putInt("message_id", ((TL_stories$TL_mediaAreaChannelPost) this.selectedArea.mediaArea).msg_id);
                    presentFragment(new ChatActivity(bundle));
                    this.selectedArea = null;
                    invalidate();
                    return;
                }
                LocationActivity locationActivity = new LocationActivity(this, 3) { // from class: org.telegram.ui.Stories.StoryMediaAreasView.1
                    @Override // org.telegram.ui.LocationActivity
                    protected boolean disablePermissionCheck() {
                        return true;
                    }
                };
                locationActivity.setResourceProvider(this.resourcesProvider);
                TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
                TL_stories$MediaArea tL_stories$MediaArea = this.selectedArea.mediaArea;
                if (tL_stories$MediaArea instanceof TL_stories$TL_mediaAreaVenue) {
                    TL_stories$TL_mediaAreaVenue tL_stories$TL_mediaAreaVenue = (TL_stories$TL_mediaAreaVenue) tL_stories$MediaArea;
                    TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue = new TLRPC$TL_messageMediaVenue();
                    tLRPC$TL_messageMediaVenue.venue_id = tL_stories$TL_mediaAreaVenue.venue_id;
                    tLRPC$TL_messageMediaVenue.venue_type = tL_stories$TL_mediaAreaVenue.venue_type;
                    tLRPC$TL_messageMediaVenue.title = tL_stories$TL_mediaAreaVenue.title;
                    tLRPC$TL_messageMediaVenue.address = tL_stories$TL_mediaAreaVenue.address;
                    tLRPC$TL_messageMediaVenue.provider = tL_stories$TL_mediaAreaVenue.provider;
                    tLRPC$TL_messageMediaVenue.geo = tL_stories$TL_mediaAreaVenue.geo;
                    tLRPC$TL_message.media = tLRPC$TL_messageMediaVenue;
                } else if (tL_stories$MediaArea instanceof TL_stories$TL_mediaAreaGeoPoint) {
                    locationActivity.setInitialMaxZoom(true);
                    TLRPC$TL_messageMediaGeo tLRPC$TL_messageMediaGeo = new TLRPC$TL_messageMediaGeo();
                    tLRPC$TL_messageMediaGeo.geo = ((TL_stories$TL_mediaAreaGeoPoint) this.selectedArea.mediaArea).geo;
                    tLRPC$TL_message.media = tLRPC$TL_messageMediaGeo;
                } else {
                    this.selectedArea = null;
                    invalidate();
                    return;
                }
                locationActivity.setSharingAllowed(false);
                locationActivity.setMessageObject(new MessageObject(UserConfig.selectedAccount, tLRPC$TL_message, false, false));
                presentFragment(locationActivity);
                this.selectedArea = null;
                invalidate();
            } else if (areaView != null && this.malicious) {
                onClickAway();
            } else {
                AreaView areaView2 = (AreaView) view;
                this.lastSelectedArea = areaView2;
                this.selectedArea = areaView2;
                invalidate();
                HintView2 hintView2 = this.hintView;
                if (hintView2 != null) {
                    hintView2.hide();
                    this.hintView = null;
                }
                boolean z = this.selectedArea.getTranslationY() < ((float) AndroidUtilities.dp(100.0f));
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                if (this.selectedArea.mediaArea instanceof TL_stories$TL_mediaAreaChannelPost) {
                    spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.StoryViewMessage));
                } else {
                    spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.StoryViewLocation));
                }
                SpannableString spannableString = new SpannableString(">");
                ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.photos_arrow);
                coloredImageSpan.translate(AndroidUtilities.dp(2.0f), AndroidUtilities.dp(1.0f));
                spannableString.setSpan(coloredImageSpan, 0, spannableString.length(), 33);
                SpannableString spannableString2 = new SpannableString("<");
                ColoredImageSpan coloredImageSpan2 = new ColoredImageSpan(R.drawable.attach_arrow_right);
                coloredImageSpan2.translate(AndroidUtilities.dp(-2.0f), AndroidUtilities.dp(1.0f));
                coloredImageSpan2.setScale(-1.0f, 1.0f);
                spannableString2.setSpan(coloredImageSpan2, 0, spannableString2.length(), 33);
                if (AndroidUtilities.isRTL(spannableStringBuilder)) {
                    spannableString = spannableString2;
                }
                AndroidUtilities.replaceCharSequence(">", spannableStringBuilder, spannableString);
                final HintView2 duration = new HintView2(getContext(), z ? 1 : 3).setText(spannableStringBuilder).setSelectorColor(687865855).setJointPx(0.0f, this.selectedArea.getTranslationX() - AndroidUtilities.dp(8.0f)).setDuration(5000L);
                this.hintView = duration;
                duration.setOnHiddenListener(new Runnable() { // from class: org.telegram.ui.Stories.StoryMediaAreasView$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        StoryMediaAreasView.this.lambda$onClick$1(duration);
                    }
                });
                AreaView areaView3 = this.selectedArea;
                if ((areaView3.mediaArea instanceof TL_stories$TL_mediaAreaChannelPost) && (!z ? (areaView3.getTranslationY() - (this.selectedArea.getMeasuredHeight() / 2.0f)) - AndroidUtilities.dp(50.0f) >= AndroidUtilities.dp(120.0f) : areaView3.getTranslationY() + (this.selectedArea.getMeasuredHeight() / 2.0f) <= getMeasuredHeight() - AndroidUtilities.dp(120.0f))) {
                    this.hintView.setTranslationY(this.selectedArea.getTranslationY() - (this.selectedArea.getMeasuredHeight() / 3.0f));
                } else if (z) {
                    this.hintView.setTranslationY(this.selectedArea.getTranslationY() + (this.selectedArea.getMeasuredHeight() / 2.0f));
                } else {
                    this.hintView.setTranslationY((this.selectedArea.getTranslationY() - (this.selectedArea.getMeasuredHeight() / 2.0f)) - AndroidUtilities.dp(50.0f));
                }
                this.hintView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stories.StoryMediaAreasView$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        StoryMediaAreasView.this.lambda$onClick$2(view2);
                    }
                });
                this.hintView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                this.hintsContainer.addView(this.hintView, LayoutHelper.createFrame(-1, 50.0f));
                this.hintView.show();
                onHintVisible(true);
            }
        }
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

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            if (childAt == this.hintsContainer) {
                childAt.layout(0, 0, i3 - i, i4 - i2);
            } else if (childAt instanceof AreaView) {
                AreaView areaView = (AreaView) childAt;
                int measuredWidth = areaView.getMeasuredWidth();
                int measuredHeight = areaView.getMeasuredHeight();
                areaView.layout((-measuredWidth) / 2, (-measuredHeight) / 2, measuredWidth / 2, measuredHeight / 2);
                double measuredWidth2 = getMeasuredWidth();
                Double.isNaN(measuredWidth2);
                areaView.setTranslationX((float) ((areaView.mediaArea.coordinates.x / 100.0d) * measuredWidth2));
                double measuredHeight2 = getMeasuredHeight();
                Double.isNaN(measuredHeight2);
                areaView.setTranslationY((float) ((areaView.mediaArea.coordinates.y / 100.0d) * measuredHeight2));
                areaView.setRotation((float) areaView.mediaArea.coordinates.rotation);
            }
        }
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

    private void drawHighlight(Canvas canvas) {
        AnimatedFloat animatedFloat = this.parentHighlightAlpha;
        AreaView areaView = this.selectedArea;
        float f = animatedFloat.set((areaView == null || !areaView.supportsBounds || this.selectedArea.scaleOnTap) ? false : true);
        AreaView areaView2 = this.selectedArea;
        boolean z = areaView2 != null && areaView2.scaleOnTap;
        float f2 = this.parentHighlightScaleAlpha.set(z);
        if (f > 0.0f) {
            canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), 255, 31);
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
                float lerp = AndroidUtilities.lerp(1.0f, 1.05f, f2);
                canvas.scale(lerp, lerp, this.rectF.centerX(), this.rectF.centerY());
                canvas.rotate(this.lastSelectedArea.getRotation(), this.rectF.centerX(), this.rectF.centerY());
                float[] fArr = this.radii;
                float dp = AndroidUtilities.dp(16.0f);
                fArr[1] = dp;
                fArr[0] = dp;
                float[] fArr2 = this.radii;
                float dp2 = AndroidUtilities.dp(16.0f);
                fArr2[3] = dp2;
                fArr2[2] = dp2;
                float[] fArr3 = this.radii;
                float dp3 = AndroidUtilities.dp(16.0f);
                fArr3[5] = dp3;
                fArr3[4] = dp3;
                float[] fArr4 = this.radii;
                float dp4 = AndroidUtilities.dp(8.0f);
                fArr4[7] = dp4;
                fArr4[6] = dp4;
                this.clipPath.addRoundRect(this.rectF, this.radii, Path.Direction.CW);
                canvas.clipPath(this.clipPath);
                RectF rectF2 = AndroidUtilities.rectTmp;
                rectF2.set(0.0f, 0.0f, getWidth(), getHeight());
                this.rect.set(0, 0, this.parentBitmap.getWidth(), this.parentBitmap.getHeight());
                canvas.rotate(-this.lastSelectedArea.getRotation(), this.rectF.centerX(), this.rectF.centerY());
                canvas.drawBitmap(this.parentBitmap, this.rect, rectF2, (Paint) null);
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

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Bitmap bitmap = this.parentBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.parentBitmap = null;
        }
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

    public boolean hasSelected() {
        return this.selectedArea != null;
    }

    public boolean hasSelectedForScale() {
        AreaView areaView = this.selectedArea;
        return areaView != null && (areaView.scaleOnTap || this.selectedArea.supportsBounds);
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

    public void onStoryItemUpdated(TL_stories$StoryItem tL_stories$StoryItem, boolean z) {
        if (tL_stories$StoryItem == null) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof StoryReactionWidgetView) {
                ((StoryReactionWidgetView) getChildAt(i)).setViews(tL_stories$StoryItem.views, z);
            }
        }
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

    /* loaded from: classes4.dex */
    public static class AreaView extends View {
        private LinearGradient gradient;
        private final Matrix gradientMatrix;
        private final Paint gradientPaint;
        public final AnimatedFloat highlightAlpha;
        public final TL_stories$MediaArea mediaArea;
        private boolean scaleOnTap;
        private final Runnable shineRunnable;
        private boolean shining;
        private long startTime;
        private LinearGradient strokeGradient;
        private final Paint strokeGradientPaint;
        private boolean supportsBounds;
        private boolean supportsShining;

        public void customDraw(Canvas canvas) {
        }

        public AreaView(Context context, View view, TL_stories$MediaArea tL_stories$MediaArea) {
            super(context);
            boolean z = true;
            this.gradientPaint = new Paint(1);
            Paint paint = new Paint(1);
            this.strokeGradientPaint = paint;
            this.gradientMatrix = new Matrix();
            this.supportsBounds = false;
            this.supportsShining = false;
            this.shining = false;
            this.shineRunnable = new Runnable() { // from class: org.telegram.ui.Stories.StoryMediaAreasView$AreaView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StoryMediaAreasView.AreaView.this.shineInternal();
                }
            };
            this.mediaArea = tL_stories$MediaArea;
            boolean z2 = tL_stories$MediaArea instanceof TL_stories$TL_mediaAreaGeoPoint;
            this.supportsBounds = z2 || (tL_stories$MediaArea instanceof TL_stories$TL_mediaAreaVenue);
            if (!z2 && !(tL_stories$MediaArea instanceof TL_stories$TL_mediaAreaVenue)) {
                z = false;
            }
            this.supportsShining = z;
            this.scaleOnTap = false;
            this.highlightAlpha = new AnimatedFloat(view, 0L, 120L, new LinearInterpolator());
            paint.setStyle(Paint.Style.STROKE);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (this.supportsShining && this.shining && this.gradient != null) {
                float measuredWidth = getMeasuredWidth() * 0.7f;
                float currentTimeMillis = ((float) (System.currentTimeMillis() - this.startTime)) / 600.0f;
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
                canvas.drawRoundRect(rectF, getMeasuredHeight() * 0.2f, getMeasuredHeight() * 0.2f, this.gradientPaint);
                this.strokeGradient.setLocalMatrix(this.gradientMatrix);
                this.strokeGradientPaint.setShader(this.strokeGradient);
                float dpf2 = AndroidUtilities.dpf2(1.5f);
                this.strokeGradientPaint.setStrokeWidth(dpf2);
                float f = dpf2 / 2.0f;
                rectF.inset(f, f);
                canvas.drawRoundRect(rectF, (getMeasuredHeight() * 0.2f) - f, (getMeasuredHeight() * 0.2f) - f, this.strokeGradientPaint);
                invalidate();
            }
        }

        public void shine() {
            if (this.supportsShining) {
                AndroidUtilities.cancelRunOnUIThread(this.shineRunnable);
                AndroidUtilities.runOnUIThread(this.shineRunnable, 400L);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void shineInternal() {
            if (this.supportsShining) {
                this.shining = true;
                this.startTime = System.currentTimeMillis();
                this.gradient = new LinearGradient(0.0f, 0.0f, 40.0f, 0.0f, new int[]{16777215, 771751935, 771751935, 16777215}, new float[]{0.0f, 0.4f, 0.6f, 1.0f}, Shader.TileMode.CLAMP);
                this.strokeGradient = new LinearGradient(0.0f, 0.0f, 40.0f, 0.0f, new int[]{16777215, 553648127, 553648127, 16777215}, new float[]{0.0f, 0.4f, 0.6f, 1.0f}, Shader.TileMode.CLAMP);
                invalidate();
            }
        }
    }
}
