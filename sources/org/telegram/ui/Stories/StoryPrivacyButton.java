package org.telegram.ui.Stories;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.recorder.StoryPrivacyBottomSheet;

/* loaded from: classes5.dex */
public class StoryPrivacyButton extends View {
    private final Paint arrowPaint;
    private final Path arrowPath;
    private final Paint[] backgroundPaint;
    private int bottomColor;
    private final ButtonBounce bounce;
    private final AnimatedFloat crossfadeT;
    public boolean draw;
    private boolean drawArrow;
    private final Matrix gradientMatrix;
    private final Drawable[] icon;
    private int iconResId;
    private final float[] iconSize;
    private int topColor;

    public StoryPrivacyButton(Context context) {
        super(context);
        this.gradientMatrix = new Matrix();
        this.backgroundPaint = new Paint[]{new Paint(1), new Paint(1)};
        this.crossfadeT = new AnimatedFloat(this, 0L, 260L, CubicBezierInterpolator.EASE_OUT_QUINT);
        this.icon = new Drawable[2];
        this.iconSize = new float[2];
        Paint paint = new Paint(1);
        this.arrowPaint = paint;
        this.arrowPath = new Path();
        this.bounce = new ButtonBounce(this, 0.6f, 5.0f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(-1);
    }

    private void setIcon(int i, float f) {
        Drawable[] drawableArr = this.icon;
        Drawable drawable = drawableArr[0];
        drawableArr[1] = drawable;
        float[] fArr = this.iconSize;
        fArr[1] = fArr[0];
        if (drawable == null || i != this.iconResId) {
            Resources resources = getContext().getResources();
            this.iconResId = i;
            drawableArr[0] = resources.getDrawable(i).mutate();
            this.icon[0].setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
            this.iconSize[0] = AndroidUtilities.dpf2(f);
            invalidate();
        }
    }

    private void setupGradient(int i, int i2) {
        Paint[] paintArr = this.backgroundPaint;
        paintArr[1].setShader(paintArr[0].getShader());
        if (this.topColor == i && this.bottomColor == i2) {
            return;
        }
        float dp = AndroidUtilities.dp(23.0f);
        this.topColor = i;
        this.bottomColor = i2;
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, dp, new int[]{i, i2}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
        this.gradientMatrix.reset();
        this.gradientMatrix.postTranslate(0.0f, AndroidUtilities.dp(8.0f));
        linearGradient.setLocalMatrix(this.gradientMatrix);
        this.backgroundPaint[0].setShader(linearGradient);
        invalidate();
    }

    public float getCenterX() {
        return getX() + (getWidth() / 2.0f) + (this.drawArrow ? 0 : AndroidUtilities.dp(14.0f));
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.draw) {
            float dpf2 = this.drawArrow ? 0.0f : AndroidUtilities.dpf2(7.0f);
            float dpf22 = this.drawArrow ? AndroidUtilities.dpf2(43.0f) : AndroidUtilities.dpf2(23.66f);
            float dpf23 = AndroidUtilities.dpf2(23.66f);
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(((getWidth() - dpf22) / 2.0f) + dpf2, (getHeight() - dpf23) / 2.0f, dpf2 + ((getWidth() + dpf22) / 2.0f), (getHeight() + dpf23) / 2.0f);
            float scale = this.bounce.getScale(0.075f);
            canvas.save();
            canvas.scale(scale, scale, rectF.centerX(), rectF.centerY());
            float f = this.crossfadeT.set(0.0f);
            if (f > 0.0f) {
                this.backgroundPaint[1].setAlpha(NotificationCenter.liveLocationsChanged);
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), this.backgroundPaint[1]);
            }
            if (f < 1.0f) {
                this.backgroundPaint[0].setAlpha((int) ((1.0f - f) * 255.0f));
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), this.backgroundPaint[0]);
            }
            float abs = Math.abs(f - 0.5f) + 0.5f;
            if (this.icon[1] != null && f > 0.5f) {
                float dpf24 = this.drawArrow ? rectF.left + AndroidUtilities.dpf2(14.66f) : rectF.centerX();
                Drawable drawable = this.icon[1];
                int i = (int) (dpf24 - ((this.iconSize[1] / 2.0f) * abs));
                float centerY = rectF.centerY();
                float f2 = (this.iconSize[1] / 2.0f) * abs;
                drawable.setBounds(i, (int) (centerY - f2), (int) (dpf24 + f2), (int) (rectF.centerY() + ((this.iconSize[1] / 2.0f) * abs)));
                this.icon[1].draw(canvas);
            }
            if (this.icon[0] != null && f <= 0.5f) {
                float dpf25 = this.drawArrow ? rectF.left + AndroidUtilities.dpf2(14.66f) : rectF.centerX();
                Drawable drawable2 = this.icon[0];
                int i2 = (int) (dpf25 - ((this.iconSize[0] / 2.0f) * abs));
                float centerY2 = rectF.centerY();
                float f3 = (this.iconSize[0] / 2.0f) * abs;
                drawable2.setBounds(i2, (int) (centerY2 - f3), (int) (dpf25 + f3), (int) (rectF.centerY() + ((this.iconSize[0] / 2.0f) * abs)));
                this.icon[0].draw(canvas);
            }
            if (this.drawArrow) {
                this.arrowPath.rewind();
                this.arrowPath.moveTo(rectF.right - AndroidUtilities.dpf2(15.66f), rectF.centerY() - AndroidUtilities.dpf2(1.33f));
                this.arrowPath.lineTo(rectF.right - AndroidUtilities.dpf2(12.0f), rectF.centerY() + AndroidUtilities.dpf2(2.33f));
                this.arrowPath.lineTo(rectF.right - AndroidUtilities.dpf2(8.16f), rectF.centerY() - AndroidUtilities.dpf2(1.33f));
                this.arrowPaint.setStrokeWidth(AndroidUtilities.dpf2(1.33f));
                canvas.drawPath(this.arrowPath, this.arrowPaint);
            }
            canvas.restore();
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(60.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(40.0f), 1073741824));
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x005a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean set(boolean z, TL_stories.StoryItem storyItem, boolean z2) {
        int i;
        int i2;
        this.drawArrow = z;
        this.draw = true;
        if (storyItem != null) {
            if (storyItem.close_friends) {
                setIcon(R.drawable.msg_stories_closefriends, 15.0f);
                i = -7808710;
                i2 = -13781445;
            } else if (storyItem.contacts) {
                setIcon(R.drawable.msg_folders_private, 17.33f);
                i = -3905294;
                i2 = -6923014;
            } else if (storyItem.selected_contacts) {
                setIcon(R.drawable.msg_folders_groups, 17.33f);
                i = -18621;
                i2 = -618956;
            } else if (z) {
                setIcon(R.drawable.msg_folders_channels, 17.33f);
                i = -15292942;
                i2 = -15630089;
            }
            setupGradient(i, i2);
            this.crossfadeT.set(z2, true);
            setVisibility(this.draw ? 0 : 8);
            invalidate();
            return this.draw;
        }
        this.draw = false;
        setVisibility(this.draw ? 0 : 8);
        invalidate();
        return this.draw;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0061  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean set(boolean z, StoriesController.UploadingStory uploadingStory, boolean z2) {
        StoryPrivacyBottomSheet.StoryPrivacy storyPrivacy;
        int i;
        int i2;
        this.drawArrow = z;
        this.draw = true;
        if (uploadingStory != null && (storyPrivacy = uploadingStory.entry.privacy) != null) {
            int i3 = storyPrivacy.type;
            if (i3 == 1) {
                setIcon(R.drawable.msg_stories_closefriends, 15.0f);
                i = -7808710;
                i2 = -13781445;
            } else if (i3 == 2) {
                setIcon(R.drawable.msg_folders_private, 17.33f);
                i = -3905294;
                i2 = -6923014;
            } else if (i3 == 3) {
                setIcon(R.drawable.msg_folders_groups, 17.33f);
                i = -18621;
                i2 = -618956;
            } else if (z) {
                setIcon(R.drawable.msg_folders_channels, 17.33f);
                i = -15292942;
                i2 = -15630089;
            }
            setupGradient(i, i2);
            this.crossfadeT.set(z2, !z2);
            setVisibility(this.draw ? 0 : 8);
            invalidate();
            return this.draw;
        }
        this.draw = false;
        setVisibility(this.draw ? 0 : 8);
        invalidate();
        return this.draw;
    }

    @Override // android.view.View
    public void setPressed(boolean z) {
        super.setPressed(z);
        this.bounce.setPressed(z);
    }
}
