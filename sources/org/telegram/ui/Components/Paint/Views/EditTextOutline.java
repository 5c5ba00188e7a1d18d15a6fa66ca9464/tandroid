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
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import java.util.Arrays;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.EditTextBoldCursor;
/* loaded from: classes3.dex */
public class EditTextOutline extends EditTextBoldCursor {
    private boolean isFrameDirty;
    private RectF[] lines;
    private Bitmap mCache;
    private Canvas mCanvas;
    private int mFrameColor;
    private int mStrokeColor;
    private float mStrokeWidth;
    private boolean mUpdateCachedBitmap;
    private Paint paint;
    private Path path;
    private TextPaint textPaint;

    public EditTextOutline(Context context) {
        super(context);
        this.mCanvas = new Canvas();
        this.textPaint = new TextPaint(1);
        this.paint = new Paint(1);
        this.path = new Path();
        this.mStrokeColor = 0;
        setInputType(getInputType() | 131072 | 524288);
        this.mUpdateCachedBitmap = true;
        this.isFrameDirty = true;
        this.textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.EditTextEffects, android.widget.TextView
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        super.onTextChanged(charSequence, i, i2, i3);
        this.mUpdateCachedBitmap = true;
        this.isFrameDirty = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.EditTextEffects, android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i > 0 && i2 > 0) {
            this.mUpdateCachedBitmap = true;
            this.isFrameDirty = true;
            Bitmap bitmap = this.mCache;
            if (bitmap != null) {
                bitmap.recycle();
            }
            this.mCache = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            return;
        }
        this.mCache = null;
    }

    @Override // android.widget.TextView
    public void setGravity(int i) {
        super.setGravity(i);
        this.mUpdateCachedBitmap = true;
        this.isFrameDirty = true;
        invalidate();
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
            this.isFrameDirty = true;
        }
        this.mUpdateCachedBitmap = true;
        invalidate();
    }

    public void setStrokeWidth(float f) {
        this.mStrokeWidth = f;
        this.mUpdateCachedBitmap = true;
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0345, code lost:
        if (r8[r10].width() != 0.0f) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0369, code lost:
        if (r8[r9].width() != 0.0f) goto L100;
     */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0377  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x032d  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0353  */
    @Override // org.telegram.ui.Components.EditTextBoldCursor, org.telegram.ui.Components.EditTextEffects, android.widget.TextView, android.view.View
    @SuppressLint({"DrawAllocation"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        RectF[] rectFArr;
        RectF[] rectFArr2;
        if (this.mCache != null && this.mStrokeColor != 0) {
            if (this.mUpdateCachedBitmap) {
                int measuredWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
                int measuredHeight = getMeasuredHeight();
                Editable text = getText();
                this.mCanvas.setBitmap(this.mCache);
                this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                float f = this.mStrokeWidth;
                if (f <= 0.0f) {
                    f = (float) Math.ceil(getTextSize() / 11.5f);
                }
                this.textPaint.setStrokeWidth(f);
                this.textPaint.setColor(this.mStrokeColor);
                this.textPaint.setTextSize(getTextSize());
                this.textPaint.setTypeface(getTypeface());
                this.textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                StaticLayout staticLayout = new StaticLayout(text, this.textPaint, measuredWidth, getLayout().getAlignment(), 1.0f, 0.0f, true);
                this.mCanvas.save();
                this.mCanvas.translate(getPaddingLeft(), ((((measuredHeight - getPaddingTop()) - getPaddingBottom()) - staticLayout.getHeight()) / 2.0f) + getPaddingTop());
                staticLayout.draw(this.mCanvas);
                this.mCanvas.restore();
                this.mUpdateCachedBitmap = false;
            }
            canvas.drawBitmap(this.mCache, 0.0f, 0.0f, this.textPaint);
        }
        int i = this.mFrameColor;
        if (i != 0) {
            this.paint.setColor(i);
            Layout layout = getLayout();
            if (layout == null) {
                super.onDraw(canvas);
                return;
            }
            RectF[] rectFArr3 = this.lines;
            if (rectFArr3 == null || rectFArr3.length != layout.getLineCount()) {
                this.lines = new RectF[layout.getLineCount()];
                this.isFrameDirty = true;
            }
            if (this.isFrameDirty) {
                this.isFrameDirty = false;
                for (int i2 = 0; i2 < layout.getLineCount(); i2++) {
                    RectF[] rectFArr4 = this.lines;
                    if (rectFArr4[i2] == null) {
                        rectFArr4[i2] = new RectF();
                    }
                    this.lines[i2].set(layout.getLineLeft(i2), layout.getLineTop(i2), layout.getLineRight(i2), layout.getLineBottom(i2));
                    if (this.lines[i2].width() > AndroidUtilities.dp(1.0f)) {
                        int dp = AndroidUtilities.dp(6.0f);
                        this.lines[i2].right += AndroidUtilities.dp(32.0f);
                        this.lines[i2].bottom += dp;
                    } else {
                        RectF[] rectFArr5 = this.lines;
                        rectFArr5[i2].left = rectFArr5[i2].right;
                    }
                }
            }
            this.path.rewind();
            float dp2 = AndroidUtilities.dp(16.0f);
            float dp3 = AndroidUtilities.dp(8.0f);
            int i3 = 0;
            while (true) {
                RectF[] rectFArr6 = this.lines;
                if (i3 >= rectFArr6.length) {
                    break;
                }
                if (rectFArr6[i3].width() != 0.0f) {
                    RectF[] rectFArr7 = this.lines;
                    if (i3 != rectFArr7.length - 1) {
                        int i4 = i3 + 1;
                        if (rectFArr7[i3].left > rectFArr7[i4].left && rectFArr7[i3].left - rectFArr7[i4].left > dp2 + dp3) {
                            float f2 = rectFArr7[i4].top;
                            RectF rectF = AndroidUtilities.rectTmp;
                            float f3 = dp2 * 2.0f;
                            rectF.set(rectFArr7[i3].left - f3, f2 - f3, rectFArr7[i3].left, f2);
                            this.path.moveTo(rectF.left, rectF.bottom);
                            this.path.arcTo(rectF, 90.0f, -90.0f);
                            this.path.lineTo(rectF.right, rectF.bottom);
                            this.path.lineTo(rectF.left, rectF.bottom);
                        }
                    }
                    RectF[] rectFArr8 = this.lines;
                    if (i3 != rectFArr8.length - 1) {
                        int i5 = i3 + 1;
                        if (rectFArr8[i3].right < rectFArr8[i5].right && rectFArr8[i5].right - rectFArr8[i3].right > dp2 + dp3) {
                            float f4 = rectFArr8[i5].top;
                            RectF rectF2 = AndroidUtilities.rectTmp;
                            float f5 = dp2 * 2.0f;
                            rectF2.set(rectFArr8[i3].right, f4 - f5, rectFArr8[i3].right + f5, f4);
                            this.path.moveTo(rectF2.right, rectF2.bottom);
                            this.path.arcTo(rectF2, 90.0f, 90.0f);
                            this.path.lineTo(rectF2.left, rectF2.bottom);
                            this.path.lineTo(rectF2.right, rectF2.bottom);
                        }
                    }
                    if (i3 != 0) {
                        RectF[] rectFArr9 = this.lines;
                        int i6 = i3 - 1;
                        if (rectFArr9[i3].left > rectFArr9[i6].left) {
                            if (rectFArr9[i3].left - rectFArr9[i6].left > dp2 + dp3) {
                                float f6 = rectFArr9[i6].bottom;
                                RectF rectF3 = AndroidUtilities.rectTmp;
                                float f7 = dp2 * 2.0f;
                                rectF3.set(rectFArr9[i3].left - f7, f6, rectFArr9[i3].left, f7 + f6);
                                this.path.moveTo(rectF3.left, rectF3.top);
                                this.path.arcTo(rectF3, -90.0f, 90.0f);
                                this.path.lineTo(rectF3.right, rectF3.top);
                                this.path.lineTo(rectF3.left, rectF3.top);
                            } else {
                                rectFArr9[i3].left = rectFArr9[i6].left;
                            }
                        }
                    }
                    if (i3 != 0) {
                        RectF[] rectFArr10 = this.lines;
                        int i7 = i3 - 1;
                        if (rectFArr10[i3].right < rectFArr10[i7].right) {
                            if (rectFArr10[i7].right - rectFArr10[i3].right > dp2 + dp3) {
                                float f8 = rectFArr10[i7].bottom;
                                RectF rectF4 = AndroidUtilities.rectTmp;
                                float f9 = dp2 * 2.0f;
                                rectF4.set(rectFArr10[i3].right, f8, rectFArr10[i3].right + f9, f9 + f8);
                                this.path.moveTo(rectF4.right, rectF4.top);
                                this.path.arcTo(rectF4, -90.0f, -90.0f);
                                this.path.lineTo(rectF4.left, rectF4.top);
                                this.path.lineTo(rectF4.right, rectF4.top);
                            } else {
                                rectFArr10[i3].right = rectFArr10[i7].right;
                            }
                        }
                    }
                }
                i3++;
            }
            float[] fArr = new float[8];
            for (int i8 = 0; i8 < this.lines.length; i8++) {
                Arrays.fill(fArr, 0.0f);
                if (i8 != 0) {
                    RectF[] rectFArr11 = this.lines;
                    int i9 = i8 - 1;
                    if (rectFArr11[i8].left >= rectFArr11[i9].left && rectFArr11[i9].width() != 0.0f) {
                        if (i8 != 0) {
                            RectF[] rectFArr12 = this.lines;
                            int i10 = i8 - 1;
                            if (rectFArr12[i8].right <= rectFArr12[i10].right) {
                            }
                        }
                        fArr[3] = dp2;
                        fArr[2] = dp2;
                        rectFArr = this.lines;
                        if (i8 != rectFArr.length - 1) {
                            int i11 = i8 + 1;
                            if (rectFArr[i11].left <= rectFArr[i8].left) {
                            }
                        }
                        fArr[7] = dp2;
                        fArr[6] = dp2;
                        rectFArr2 = this.lines;
                        if (i8 != rectFArr2.length - 1) {
                            int i12 = i8 + 1;
                            if (rectFArr2[i12].right >= rectFArr2[i8].right && rectFArr2[i12].width() != 0.0f) {
                                RectF rectF5 = AndroidUtilities.rectTmp;
                                rectF5.set(this.lines[i8]);
                                this.path.addRoundRect(rectF5, fArr, Path.Direction.CW);
                            }
                        }
                        fArr[5] = dp2;
                        fArr[4] = dp2;
                        RectF rectF52 = AndroidUtilities.rectTmp;
                        rectF52.set(this.lines[i8]);
                        this.path.addRoundRect(rectF52, fArr, Path.Direction.CW);
                    }
                }
                fArr[1] = dp2;
                fArr[0] = dp2;
                if (i8 != 0) {
                }
                fArr[3] = dp2;
                fArr[2] = dp2;
                rectFArr = this.lines;
                if (i8 != rectFArr.length - 1) {
                }
                fArr[7] = dp2;
                fArr[6] = dp2;
                rectFArr2 = this.lines;
                if (i8 != rectFArr2.length - 1) {
                }
                fArr[5] = dp2;
                fArr[4] = dp2;
                RectF rectF522 = AndroidUtilities.rectTmp;
                rectF522.set(this.lines[i8]);
                this.path.addRoundRect(rectF522, fArr, Path.Direction.CW);
            }
            this.path.close();
            canvas.drawPath(this.path, this.paint);
        }
        super.onDraw(canvas);
    }
}
