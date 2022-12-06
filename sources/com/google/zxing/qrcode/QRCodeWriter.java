package com.google.zxing.qrcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import java.util.Arrays;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.messenger.SvgHelper;
import org.telegram.ui.Components.RLottieDrawable;
/* loaded from: classes.dex */
public final class QRCodeWriter {
    private int imageBlockX;
    private int imageBloks;
    private int imageSize;
    private ByteMatrix input;
    private int sideQuadSize;
    private float[] radii = new float[8];
    public boolean includeSideQuads = true;

    public Bitmap encode(String str, int i, int i2, Map<EncodeHintType, ?> map, Bitmap bitmap) throws WriterException {
        return encode(str, i, i2, map, bitmap, 1.0f, -1, -16777216);
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x02b1  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0054  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Bitmap encode(String str, int i, int i2, Map<EncodeHintType, ?> map, Bitmap bitmap, float f, int i3, int i4) throws WriterException {
        String str2;
        int i5;
        ByteMatrix matrix;
        float f2;
        GradientDrawable gradientDrawable;
        Paint paint;
        Canvas canvas;
        Bitmap bitmap2;
        int i6;
        int i7;
        char c;
        int i8;
        GradientDrawable gradientDrawable2;
        boolean z;
        boolean z2;
        float f3;
        boolean z3;
        Canvas canvas2;
        char c2;
        if (str.isEmpty()) {
            throw new IllegalArgumentException("Found empty contents");
        }
        if (i < 0 || i2 < 0) {
            throw new IllegalArgumentException("Requested dimensions are too small: " + i + 'x' + i2);
        }
        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        if (map != null) {
            EncodeHintType encodeHintType = EncodeHintType.ERROR_CORRECTION;
            if (map.containsKey(encodeHintType)) {
                errorCorrectionLevel = ErrorCorrectionLevel.valueOf(map.get(encodeHintType).toString());
            }
            EncodeHintType encodeHintType2 = EncodeHintType.MARGIN;
            if (map.containsKey(encodeHintType2)) {
                i5 = Integer.parseInt(map.get(encodeHintType2).toString());
                str2 = str;
                matrix = Encoder.encode(str2, errorCorrectionLevel, map).getMatrix();
                this.input = matrix;
                if (matrix != null) {
                    throw new IllegalStateException();
                }
                int width = matrix.getWidth();
                int height = this.input.getHeight();
                for (int i9 = 0; i9 < width && has(i9, 0); i9++) {
                    this.sideQuadSize++;
                }
                int i10 = i5 * 2;
                int i11 = width + i10;
                int i12 = i10 + height;
                int min = Math.min(Math.max(i, i11) / i11, Math.max(i2, i12) / i12);
                int i13 = (min * width) + 32;
                Bitmap createBitmap = (bitmap == null || bitmap.getWidth() != i13) ? Bitmap.createBitmap(i13, i13, Bitmap.Config.ARGB_8888) : bitmap;
                Canvas canvas3 = new Canvas(createBitmap);
                canvas3.drawColor(i3);
                Paint paint2 = new Paint(1);
                paint2.setColor(i4);
                GradientDrawable gradientDrawable3 = new GradientDrawable();
                gradientDrawable3.setShape(0);
                gradientDrawable3.setCornerRadii(this.radii);
                float f4 = min;
                int round = Math.round(((i13 - 32) / 4.65f) / f4);
                this.imageBloks = round;
                if (round % 2 != width % 2) {
                    this.imageBloks = round + 1;
                }
                int i14 = this.imageBloks;
                this.imageBlockX = (width - i14) / 2;
                int i15 = (i14 * min) - 24;
                this.imageSize = i15;
                int i16 = (i13 - i15) / 2;
                if (this.includeSideQuads) {
                    paint2.setColor(i4);
                    f2 = f4;
                    gradientDrawable = gradientDrawable3;
                    paint = paint2;
                    bitmap2 = createBitmap;
                    i6 = min;
                    c = 1;
                    canvas = canvas3;
                    i7 = i16;
                    i8 = height;
                    drawSideQuadsGradient(canvas3, paint2, gradientDrawable3, this.sideQuadSize, f2, 16, i13, f, this.radii, i3, i4);
                } else {
                    f2 = f4;
                    gradientDrawable = gradientDrawable3;
                    paint = paint2;
                    canvas = canvas3;
                    bitmap2 = createBitmap;
                    i6 = min;
                    i7 = i16;
                    c = 1;
                    i8 = height;
                }
                boolean z4 = Color.alpha(i3) == 0;
                float f5 = (f2 / 2.0f) * f;
                int i17 = 0;
                int i18 = 16;
                while (i17 < i8) {
                    int i19 = 0;
                    int i20 = 16;
                    while (i19 < width) {
                        if (has(i19, i17)) {
                            Arrays.fill(this.radii, f5);
                            if (has(i19, i17 - 1)) {
                                float[] fArr = this.radii;
                                fArr[c] = 0.0f;
                                c2 = 0;
                                fArr[0] = 0.0f;
                                fArr[3] = 0.0f;
                                fArr[2] = 0.0f;
                            } else {
                                c2 = 0;
                            }
                            if (has(i19, i17 + 1)) {
                                float[] fArr2 = this.radii;
                                fArr2[7] = 0.0f;
                                fArr2[6] = 0.0f;
                                fArr2[5] = 0.0f;
                                fArr2[4] = 0.0f;
                            }
                            if (has(i19 - 1, i17)) {
                                float[] fArr3 = this.radii;
                                fArr3[c] = 0.0f;
                                fArr3[c2] = 0.0f;
                                fArr3[7] = 0.0f;
                                fArr3[6] = 0.0f;
                            }
                            if (has(i19 + 1, i17)) {
                                float[] fArr4 = this.radii;
                                fArr4[3] = 0.0f;
                                fArr4[2] = 0.0f;
                                fArr4[5] = 0.0f;
                                fArr4[4] = 0.0f;
                            }
                            gradientDrawable2 = gradientDrawable;
                            gradientDrawable2.setColor(i4);
                            gradientDrawable2.setBounds(i20, i18, i20 + i6, i18 + i6);
                            Canvas canvas4 = canvas;
                            gradientDrawable2.draw(canvas4);
                            f3 = f5;
                            canvas2 = canvas4;
                            z3 = z4;
                        } else {
                            gradientDrawable2 = gradientDrawable;
                            Arrays.fill(this.radii, 0.0f);
                            int i21 = i19 - 1;
                            int i22 = i17 - 1;
                            if (!has(i21, i22) || !has(i21, i17) || !has(i19, i22)) {
                                z = false;
                            } else {
                                float[] fArr5 = this.radii;
                                fArr5[c] = f5;
                                fArr5[0] = f5;
                                z = true;
                            }
                            int i23 = i19 + 1;
                            if (has(i23, i22) && has(i23, i17) && has(i19, i22)) {
                                float[] fArr6 = this.radii;
                                fArr6[3] = f5;
                                fArr6[2] = f5;
                                z = true;
                            }
                            int i24 = i17 + 1;
                            if (has(i21, i24) && has(i21, i17) && has(i19, i24)) {
                                float[] fArr7 = this.radii;
                                fArr7[7] = f5;
                                fArr7[6] = f5;
                                z = true;
                            }
                            if (!has(i23, i24) || !has(i23, i17) || !has(i19, i24)) {
                                z2 = z;
                            } else {
                                float[] fArr8 = this.radii;
                                fArr8[5] = f5;
                                fArr8[4] = f5;
                                z2 = true;
                            }
                            if (!z2 || z4) {
                                f3 = f5;
                                z3 = z4;
                                canvas2 = canvas;
                            } else {
                                int i25 = i20 + i6;
                                f3 = f5;
                                int i26 = i18 + i6;
                                z3 = z4;
                                canvas.drawRect(i20, i18, i25, i26, paint);
                                gradientDrawable2.setColor(i3);
                                gradientDrawable2.setBounds(i20, i18, i25, i26);
                                canvas2 = canvas;
                                gradientDrawable2.draw(canvas2);
                            }
                        }
                        i19++;
                        i20 += i6;
                        canvas = canvas2;
                        f5 = f3;
                        z4 = z3;
                        gradientDrawable = gradientDrawable2;
                    }
                    i17++;
                    i18 += i6;
                    f5 = f5;
                    gradientDrawable = gradientDrawable;
                }
                Canvas canvas5 = canvas;
                String readRes = RLottieDrawable.readRes(null, R.raw.qr_logo);
                int i27 = this.imageSize;
                Bitmap bitmap3 = SvgHelper.getBitmap(readRes, i27, i27, false);
                float f6 = i7;
                canvas5.drawBitmap(bitmap3, f6, f6, (Paint) null);
                bitmap3.recycle();
                canvas5.setBitmap(null);
                return bitmap2;
            }
        }
        str2 = str;
        i5 = 4;
        matrix = Encoder.encode(str2, errorCorrectionLevel, map).getMatrix();
        this.input = matrix;
        if (matrix != null) {
        }
    }

    public static void drawSideQuadsGradient(Canvas canvas, Paint paint, GradientDrawable gradientDrawable, float f, float f2, int i, float f3, float f4, float[] fArr, int i2, int i3) {
        float f5;
        float f6;
        float f7;
        float f8;
        int i4 = i;
        int i5 = 1;
        boolean z = Color.alpha(i2) == 0;
        gradientDrawable.setShape(0);
        gradientDrawable.setCornerRadii(fArr);
        Path path = new Path();
        RectF rectF = new RectF();
        int i6 = 0;
        while (i6 < 3) {
            if (i6 == 0) {
                f8 = i4;
                f7 = f8;
            } else {
                if (i6 == i5) {
                    f6 = i4;
                    f5 = (f3 - (f * f2)) - f6;
                } else {
                    f5 = i4;
                    f6 = (f3 - (f * f2)) - f5;
                }
                f7 = f5;
                f8 = f6;
            }
            if (z) {
                float f9 = (f - 1.0f) * f2;
                rectF.set(f7 + f2, f8 + f2, f7 + f9, f9 + f8);
                float f10 = ((f * f2) / 4.0f) * f4;
                path.reset();
                path.addRoundRect(rectF, f10, f10, Path.Direction.CW);
                path.close();
                canvas.save();
                canvas.clipPath(path, Region.Op.DIFFERENCE);
            }
            float f11 = f * f2;
            Arrays.fill(fArr, (f11 / 3.0f) * f4);
            gradientDrawable.setColor(i3);
            gradientDrawable.setBounds((int) f7, (int) f8, (int) (f7 + f11), (int) (f8 + f11));
            gradientDrawable.draw(canvas);
            float f12 = f7 + f2;
            float f13 = f8 + f2;
            float f14 = (f - 1.0f) * f2;
            float f15 = f7 + f14;
            float f16 = f14 + f8;
            float f17 = f8;
            float f18 = f7;
            canvas.drawRect(f12, f13, f15, f16, paint);
            if (z) {
                canvas.restore();
            }
            if (!z) {
                Arrays.fill(fArr, (f11 / 4.0f) * f4);
                gradientDrawable.setColor(i2);
                gradientDrawable.setBounds((int) f12, (int) f13, (int) f15, (int) f16);
                gradientDrawable.draw(canvas);
            }
            float f19 = (f - 2.0f) * f2;
            Arrays.fill(fArr, (f19 / 4.0f) * f4);
            gradientDrawable.setColor(i3);
            float f20 = 2.0f * f2;
            gradientDrawable.setBounds((int) (f18 + f20), (int) (f17 + f20), (int) (f18 + f19), (int) (f17 + f19));
            gradientDrawable.draw(canvas);
            i6++;
            i4 = i;
            i5 = 1;
        }
    }

    public static void drawSideQuads(Canvas canvas, float f, float f2, Paint paint, float f3, float f4, int i, float f5, float f6, float[] fArr, boolean z) {
        float f7;
        float f8;
        Path path = new Path();
        for (int i2 = 0; i2 < 3; i2++) {
            if (i2 == 0) {
                f7 = i;
                f8 = f7;
            } else if (i2 == 1) {
                f8 = i;
                f7 = (f5 - (f3 * f4)) - f8;
            } else {
                f7 = i;
                f8 = (f5 - (f3 * f4)) - f7;
            }
            float f9 = f7 + f;
            float f10 = f8 + f2;
            if (z) {
                RectF rectF = AndroidUtilities.rectTmp;
                float f11 = (f3 - 1.0f) * f4;
                rectF.set(f9 + f4, f10 + f4, f9 + f11, f11 + f10);
                float f12 = ((f3 * f4) / 4.0f) * f6;
                path.reset();
                path.addRoundRect(rectF, f12, f12, Path.Direction.CW);
                path.close();
                canvas.save();
                canvas.clipPath(path, Region.Op.DIFFERENCE);
            }
            float f13 = f3 * f4;
            float f14 = (f13 / 3.0f) * f6;
            RectF rectF2 = AndroidUtilities.rectTmp;
            rectF2.set(f9, f10, f9 + f13, f13 + f10);
            canvas.drawRoundRect(rectF2, f14, f14, paint);
            if (z) {
                canvas.restore();
            }
            float f15 = (f3 - 2.0f) * f4;
            float f16 = (f15 / 4.0f) * f6;
            float f17 = 2.0f * f4;
            rectF2.set(f9 + f17, f17 + f10, f9 + f15, f10 + f15);
            canvas.drawRoundRect(rectF2, f16, f16, paint);
        }
    }

    private boolean has(int i, int i2) {
        int i3 = this.imageBlockX;
        if (i >= i3) {
            int i4 = this.imageBloks;
            if (i < i3 + i4 && i2 >= i3 && i2 < i3 + i4) {
                return false;
            }
        }
        if ((i < this.sideQuadSize || i >= this.input.getWidth() - this.sideQuadSize) && i2 < this.sideQuadSize) {
            return false;
        }
        return (i >= this.sideQuadSize || i2 < this.input.getHeight() - this.sideQuadSize) && i >= 0 && i2 >= 0 && i < this.input.getWidth() && i2 < this.input.getHeight() && this.input.get(i, i2) == 1;
    }

    public int getImageSize() {
        return this.imageSize;
    }
}
