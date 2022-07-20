package org.telegram.ui.Components.Paint.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.EditTextBoldCursor;
/* loaded from: classes3.dex */
public class EditTextOutline extends EditTextBoldCursor {
    private float[] lines;
    private Bitmap mCache;
    private int mFrameColor;
    private float mStrokeWidth;
    private Canvas mCanvas = new Canvas();
    private TextPaint textPaint = new TextPaint(1);
    private Paint paint = new Paint(1);
    private Path path = new Path();
    private RectF rect = new RectF();
    private int mStrokeColor = 0;
    private boolean mUpdateCachedBitmap = true;

    public EditTextOutline(Context context) {
        super(context);
        setInputType(getInputType() | 131072 | 524288);
        this.textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override // org.telegram.ui.Components.EditTextEffects, android.widget.TextView
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        super.onTextChanged(charSequence, i, i2, i3);
        this.mUpdateCachedBitmap = true;
    }

    @Override // org.telegram.ui.Components.EditTextEffects, android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i > 0 && i2 > 0) {
            this.mUpdateCachedBitmap = true;
            Bitmap bitmap = this.mCache;
            if (bitmap != null) {
                bitmap.recycle();
            }
            this.mCache = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            return;
        }
        this.mCache = null;
    }

    public void setStrokeColor(int i) {
        this.mStrokeColor = i;
        this.mUpdateCachedBitmap = true;
        invalidate();
    }

    public void setFrameColor(int i) {
        int i2 = this.mFrameColor;
        if (i2 == 0 && i != 0) {
            setPadding(AndroidUtilities.dp(19.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(19.0f), AndroidUtilities.dp(7.0f));
            setCursorColor(-16777216);
        } else if (i2 != 0 && i == 0) {
            setPadding(AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f));
            setCursorColor(-1);
        }
        this.mFrameColor = i;
        if (i != 0) {
            float computePerceivedBrightness = AndroidUtilities.computePerceivedBrightness(i);
            if (computePerceivedBrightness == 0.0f) {
                computePerceivedBrightness = Color.red(this.mFrameColor) / 255.0f;
            }
            if (computePerceivedBrightness > 0.87d) {
                setTextColor(-16777216);
            } else {
                setTextColor(-1);
            }
        }
        this.mUpdateCachedBitmap = true;
        invalidate();
    }

    public void setStrokeWidth(float f) {
        this.mStrokeWidth = f;
        this.mUpdateCachedBitmap = true;
        invalidate();
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x026a  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x02bd  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x02d7  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x034f  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0366  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0399  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x03b2  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x03b6  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x023f  */
    @Override // org.telegram.ui.Components.EditTextBoldCursor, org.telegram.ui.Components.EditTextEffects, android.widget.TextView, android.view.View
    @SuppressLint({"DrawAllocation"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        int i;
        int i2;
        float f;
        Layout layout;
        int i3;
        float f2;
        boolean z;
        boolean z2;
        boolean z3;
        int i4;
        float f3;
        float f4;
        if (this.mCache != null && this.mStrokeColor != 0) {
            if (this.mUpdateCachedBitmap) {
                int measuredWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
                int measuredHeight = getMeasuredHeight();
                String obj = getText().toString();
                this.mCanvas.setBitmap(this.mCache);
                this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                float f5 = this.mStrokeWidth;
                if (f5 <= 0.0f) {
                    f5 = (float) Math.ceil(getTextSize() / 11.5f);
                }
                this.textPaint.setStrokeWidth(f5);
                this.textPaint.setColor(this.mStrokeColor);
                this.textPaint.setTextSize(getTextSize());
                this.textPaint.setTypeface(getTypeface());
                this.textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                StaticLayout staticLayout = new StaticLayout(obj, this.textPaint, measuredWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                this.mCanvas.save();
                this.mCanvas.translate(getPaddingLeft(), ((((measuredHeight - getPaddingTop()) - getPaddingBottom()) - staticLayout.getHeight()) / 2.0f) + getPaddingTop());
                staticLayout.draw(this.mCanvas);
                this.mCanvas.restore();
                this.mUpdateCachedBitmap = false;
            }
            canvas.drawBitmap(this.mCache, 0.0f, 0.0f, this.textPaint);
        }
        int i5 = this.mFrameColor;
        if (i5 != 0) {
            this.paint.setColor(i5);
            Layout layout2 = getLayout();
            if (layout2 == null) {
                super.onDraw(canvas);
                return;
            }
            float[] fArr = this.lines;
            if (fArr == null || fArr.length != layout2.getLineCount()) {
                this.lines = new float[layout2.getLineCount()];
            }
            float dp = AndroidUtilities.dp(6.0f);
            float dp2 = AndroidUtilities.dp(6.0f);
            float dp3 = AndroidUtilities.dp(26.0f);
            for (int i6 = 0; i6 < this.lines.length; i6++) {
                float ceil = (float) Math.ceil(layout2.getLineRight(i6) - layout2.getLineLeft(i6));
                if (ceil > AndroidUtilities.dp(1.0f)) {
                    this.lines[i6] = ceil + (dp2 * 2.0f);
                } else {
                    this.lines[i6] = 0.0f;
                }
            }
            while (true) {
                i = 1;
                int i7 = 1;
                boolean z4 = false;
                while (true) {
                    float[] fArr2 = this.lines;
                    if (i7 >= fArr2.length) {
                        break;
                    }
                    if (fArr2[i7] != 0.0f) {
                        int i8 = i7 - 1;
                        float f6 = fArr2[i7] - fArr2[i8];
                        if (f6 > 0.0f) {
                            if (f6 < dp3) {
                                fArr2[i8] = fArr2[i7];
                            } else {
                                if (f6 < 4.0f * dp) {
                                    double d = fArr2[i7];
                                    double ceil2 = Math.ceil(f4 - f6);
                                    Double.isNaN(d);
                                    fArr2[i7] = (float) (d + ceil2);
                                }
                            }
                            z4 = true;
                        } else if (f6 < 0.0f) {
                            float f7 = -f6;
                            if (f7 < dp3) {
                                fArr2[i7] = fArr2[i8];
                            } else {
                                if (f7 < 4.0f * dp) {
                                    double d2 = fArr2[i8];
                                    double ceil3 = Math.ceil(f3 + f6);
                                    Double.isNaN(d2);
                                    fArr2[i8] = (float) (d2 + ceil3);
                                }
                            }
                            z4 = true;
                        }
                    }
                    i7++;
                }
                if (!z4) {
                    break;
                }
            }
            int measuredWidth2 = getMeasuredWidth() / 2;
            float measuredHeight2 = (getMeasuredHeight() - layout2.getHeight()) / 2;
            int i9 = 0;
            while (i9 < this.lines.length) {
                int lineBottom = layout2.getLineBottom(i9) - layout2.getLineTop(i9);
                if (i9 != this.lines.length - i) {
                    f = 1.0f;
                    i2 = AndroidUtilities.dp(1.0f);
                } else {
                    f = 1.0f;
                    i2 = 0;
                }
                int dp4 = (lineBottom - i2) + (i9 != 0 ? AndroidUtilities.dp(f) : 0);
                float[] fArr3 = this.lines;
                float f8 = dp2 * 2.0f;
                if (fArr3[i9] <= f8) {
                    measuredHeight2 += dp4;
                    layout = layout2;
                    i3 = measuredWidth2;
                    f2 = dp2;
                } else {
                    if (i9 > 0) {
                        int i10 = i9 - 1;
                        if (fArr3[i10] > fArr3[i9] && fArr3[i10] > f8) {
                            z = true;
                            int i11 = i9 + 1;
                            boolean z5 = i11 >= fArr3.length && fArr3[i11] > fArr3[i9] && fArr3[i11] > f8;
                            z2 = (i9 == 0 && fArr3[i9 + (-1)] == fArr3[i9]) ? false : true;
                            z3 = (i9 == fArr3.length - i && fArr3[i9] == fArr3[i11]) ? false : true;
                            this.path.reset();
                            if (i9 != 0) {
                                measuredHeight2 -= 1.0f;
                                dp4++;
                            }
                            int i12 = dp4;
                            float ceil4 = (float) Math.ceil(dp4 + measuredHeight2);
                            float f9 = measuredWidth2;
                            float[] fArr4 = this.lines;
                            float f10 = (f9 - (fArr4[i9] / 2.0f)) + dp;
                            float f11 = (f9 + (fArr4[i9] / 2.0f)) - dp;
                            this.path.moveTo(f10, measuredHeight2);
                            if (z2) {
                                layout = layout2;
                                i3 = measuredWidth2;
                                f2 = dp2;
                                this.path.lineTo(f11 + dp, measuredHeight2);
                            } else if (z) {
                                float f12 = dp * 2.0f;
                                this.path.lineTo(f11 + f12, measuredHeight2);
                                layout = layout2;
                                i3 = measuredWidth2;
                                this.rect.set(f11 + dp, measuredHeight2, f11 + (dp * 3.0f), measuredHeight2 + f12);
                                f2 = dp2;
                                this.path.arcTo(this.rect, 270.0f, -90.0f, false);
                            } else {
                                layout = layout2;
                                i3 = measuredWidth2;
                                f2 = dp2;
                                this.path.lineTo(f11, measuredHeight2);
                                this.rect.set(f11 - dp, measuredHeight2, f11 + dp, (dp * 2.0f) + measuredHeight2);
                                this.path.arcTo(this.rect, 270.0f, 90.0f, false);
                            }
                            float f13 = f11 + dp;
                            this.path.lineTo(f13, ceil4 - dp);
                            if (!z3) {
                                if (z5) {
                                    float f14 = dp * 2.0f;
                                    this.rect.set(f13, ceil4 - f14, f11 + (dp * 3.0f), ceil4);
                                    this.path.arcTo(this.rect, 180.0f, -90.0f, false);
                                    this.path.lineTo(f10 - f14, ceil4);
                                } else {
                                    this.rect.set(f11 - dp, ceil4 - (dp * 2.0f), f13, ceil4);
                                    this.path.arcTo(this.rect, 0.0f, 90.0f, false);
                                    this.path.lineTo(f10, ceil4);
                                }
                                if (z5) {
                                    this.rect.set(f10 - (dp * 3.0f), ceil4 - (dp * 2.0f), f10 - dp, ceil4);
                                    this.path.arcTo(this.rect, 90.0f, -90.0f, false);
                                } else {
                                    this.rect.set(f10 - dp, ceil4 - (dp * 2.0f), f10 + dp, ceil4);
                                    this.path.arcTo(this.rect, 90.0f, 90.0f, false);
                                }
                            } else {
                                this.path.lineTo(f13, ceil4);
                                this.path.lineTo(f10 - dp, ceil4);
                            }
                            float f15 = f10 - dp;
                            this.path.lineTo(f15, measuredHeight2 - dp);
                            if (z2) {
                                this.path.lineTo(f15, measuredHeight2);
                            } else if (z) {
                                this.rect.set(f10 - (3.0f * dp), measuredHeight2, f15, (dp * 2.0f) + measuredHeight2);
                                this.path.arcTo(this.rect, 0.0f, -90.0f, false);
                            } else {
                                this.rect.set(f15, measuredHeight2, f10 + dp, (dp * 2.0f) + measuredHeight2);
                                this.path.arcTo(this.rect, 180.0f, 90.0f, false);
                            }
                            this.path.close();
                            canvas.drawPath(this.path, this.paint);
                            if (i9 == 0) {
                                measuredHeight2 += 1.0f;
                                i4 = i12 - 1;
                            } else {
                                i4 = i12;
                            }
                            measuredHeight2 += i4;
                        }
                    }
                    z = false;
                    int i112 = i9 + 1;
                    if (i112 >= fArr3.length) {
                    }
                    if (i9 == 0) {
                    }
                    if (i9 == fArr3.length - i) {
                    }
                    this.path.reset();
                    if (i9 != 0) {
                    }
                    int i122 = dp4;
                    float ceil42 = (float) Math.ceil(dp4 + measuredHeight2);
                    float f92 = measuredWidth2;
                    float[] fArr42 = this.lines;
                    float f102 = (f92 - (fArr42[i9] / 2.0f)) + dp;
                    float f112 = (f92 + (fArr42[i9] / 2.0f)) - dp;
                    this.path.moveTo(f102, measuredHeight2);
                    if (z2) {
                    }
                    float f132 = f112 + dp;
                    this.path.lineTo(f132, ceil42 - dp);
                    if (!z3) {
                    }
                    float f152 = f102 - dp;
                    this.path.lineTo(f152, measuredHeight2 - dp);
                    if (z2) {
                    }
                    this.path.close();
                    canvas.drawPath(this.path, this.paint);
                    if (i9 == 0) {
                    }
                    measuredHeight2 += i4;
                }
                i9++;
                dp2 = f2;
                measuredWidth2 = i3;
                layout2 = layout;
                i = 1;
            }
        }
        super.onDraw(canvas);
    }
}
