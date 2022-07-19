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
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.beta.R;
import org.telegram.ui.Components.RLottieDrawable;
/* loaded from: classes.dex */
public final class QRCodeWriter {
    private int imageBlockX;
    private int imageBloks;
    private int imageSize;
    private ByteMatrix input;
    private float[] radii = new float[8];
    private int sideQuadSize;

    public Bitmap encode(String str, int i, int i2, Map<EncodeHintType, ?> map, Bitmap bitmap) throws WriterException {
        return encode(str, i, i2, map, bitmap, 1.0f, -1, -16777216);
    }

    /* JADX WARN: Removed duplicated region for block: B:114:0x03d5  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0054  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Bitmap encode(String str, int i, int i2, Map<EncodeHintType, ?> map, Bitmap bitmap, float f, int i3, int i4) throws WriterException {
        String str2;
        int i5;
        ByteMatrix matrix;
        int i6;
        float f2;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        int i7;
        int i8;
        int i9;
        float f3;
        int i10;
        int i11;
        int i12 = i4;
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
                for (int i13 = 0; i13 < width && has(i13, 0); i13++) {
                    this.sideQuadSize++;
                }
                int i14 = i5 * 2;
                int i15 = width + i14;
                int i16 = i14 + height;
                int min = Math.min(Math.max(i, i15) / i15, Math.max(i2, i16) / i16);
                int i17 = (min * width) + 32;
                Bitmap createBitmap = (bitmap == null || bitmap.getWidth() != i17) ? Bitmap.createBitmap(i17, i17, Bitmap.Config.ARGB_8888) : bitmap;
                Canvas canvas = new Canvas(createBitmap);
                canvas.drawColor(i3);
                Paint paint = new Paint(1);
                paint.setColor(i12);
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(0);
                gradientDrawable.setCornerRadii(this.radii);
                float f4 = min;
                int round = Math.round(((i17 - 32) / 4.65f) / f4);
                this.imageBloks = round;
                if (round % 2 != width % 2) {
                    this.imageBloks = round + 1;
                }
                int i18 = this.imageBloks;
                this.imageBlockX = (width - i18) / 2;
                int i19 = (i18 * min) - 24;
                this.imageSize = i19;
                int i20 = (i17 - i19) / 2;
                boolean z5 = Color.alpha(i3) == 0;
                Path path = new Path();
                RectF rectF = new RectF();
                Bitmap bitmap2 = createBitmap;
                Paint paint2 = paint;
                int i21 = 0;
                while (i21 < 3) {
                    if (i21 == 0) {
                        i7 = i17;
                        i9 = 16;
                        i8 = 16;
                    } else if (i21 == 1) {
                        i8 = (i17 - (this.sideQuadSize * min)) - 16;
                        i7 = i17;
                        i9 = 16;
                    } else {
                        i7 = i17;
                        i9 = (i17 - (this.sideQuadSize * min)) - 16;
                        i8 = 16;
                    }
                    int i22 = i20;
                    if (z5) {
                        i11 = width;
                        i10 = height;
                        int i23 = this.sideQuadSize;
                        f3 = f4;
                        rectF.set(i8 + min, i9 + min, i8 + ((i23 - 1) * min), ((i23 - 1) * min) + i9);
                        float f5 = ((this.sideQuadSize * min) / 4.0f) * f;
                        path.reset();
                        path.addRoundRect(rectF, f5, f5, Path.Direction.CW);
                        path.close();
                        canvas.save();
                        canvas.clipPath(path, Region.Op.DIFFERENCE);
                    } else {
                        i11 = width;
                        i10 = height;
                        f3 = f4;
                    }
                    Arrays.fill(this.radii, ((this.sideQuadSize * min) / 3.0f) * f);
                    gradientDrawable.setColor(i12);
                    int i24 = this.sideQuadSize;
                    gradientDrawable.setBounds(i8, i9, (i24 * min) + i8, (i24 * min) + i9);
                    gradientDrawable.draw(canvas);
                    int i25 = i8 + min;
                    int i26 = i9 + min;
                    Path path2 = path;
                    int i27 = this.sideQuadSize;
                    RectF rectF2 = rectF;
                    int i28 = i21;
                    GradientDrawable gradientDrawable2 = gradientDrawable;
                    Paint paint3 = paint2;
                    int i29 = i8;
                    Canvas canvas2 = canvas;
                    canvas.drawRect(i25, i26, i8 + ((i27 - 1) * min), ((i27 - 1) * min) + i9, paint3);
                    if (z5) {
                        canvas2.restore();
                    }
                    if (!z5) {
                        Arrays.fill(this.radii, ((this.sideQuadSize * min) / 4.0f) * f);
                        gradientDrawable2.setColor(i3);
                        int i30 = this.sideQuadSize;
                        gradientDrawable2.setBounds(i25, i26, i29 + ((i30 - 1) * min), ((i30 - 1) * min) + i9);
                        gradientDrawable2.draw(canvas2);
                    }
                    Arrays.fill(this.radii, (((this.sideQuadSize - 2) * min) / 4.0f) * f);
                    gradientDrawable2.setColor(i12);
                    int i31 = min * 2;
                    int i32 = this.sideQuadSize;
                    gradientDrawable2.setBounds(i29 + i31, i31 + i9, i29 + ((i32 - 2) * min), i9 + ((i32 - 2) * min));
                    gradientDrawable2.draw(canvas2);
                    i17 = i7;
                    f4 = f3;
                    path = path2;
                    rectF = rectF2;
                    canvas = canvas2;
                    gradientDrawable = gradientDrawable2;
                    width = i11;
                    height = i10;
                    paint2 = paint3;
                    i21 = i28 + 1;
                    i20 = i22;
                }
                Paint paint4 = paint2;
                int i33 = i20;
                int i34 = width;
                GradientDrawable gradientDrawable3 = gradientDrawable;
                Canvas canvas3 = canvas;
                float f6 = (f4 / 2.0f) * f;
                int i35 = height;
                int i36 = 0;
                int i37 = 16;
                while (i36 < i35) {
                    int i38 = i34;
                    int i39 = 0;
                    int i40 = 16;
                    while (i39 < i38) {
                        if (has(i39, i36)) {
                            Arrays.fill(this.radii, f6);
                            if (has(i39, i36 - 1)) {
                                float[] fArr = this.radii;
                                fArr[1] = 0.0f;
                                fArr[0] = 0.0f;
                                fArr[3] = 0.0f;
                                fArr[2] = 0.0f;
                            }
                            if (has(i39, i36 + 1)) {
                                float[] fArr2 = this.radii;
                                fArr2[7] = 0.0f;
                                fArr2[6] = 0.0f;
                                fArr2[5] = 0.0f;
                                fArr2[4] = 0.0f;
                            }
                            if (has(i39 - 1, i36)) {
                                float[] fArr3 = this.radii;
                                fArr3[1] = 0.0f;
                                fArr3[0] = 0.0f;
                                fArr3[7] = 0.0f;
                                fArr3[6] = 0.0f;
                            }
                            if (has(i39 + 1, i36)) {
                                float[] fArr4 = this.radii;
                                fArr4[3] = 0.0f;
                                fArr4[2] = 0.0f;
                                fArr4[5] = 0.0f;
                                fArr4[4] = 0.0f;
                            }
                            gradientDrawable3.setColor(i12);
                            gradientDrawable3.setBounds(i40, i37, i40 + min, i37 + min);
                            gradientDrawable3.draw(canvas3);
                            i6 = i35;
                            z = z5;
                            f2 = f6;
                        } else {
                            Arrays.fill(this.radii, 0.0f);
                            int i41 = i39 - 1;
                            int i42 = i36 - 1;
                            if (!has(i41, i42) || !has(i41, i36) || !has(i39, i42)) {
                                i6 = i35;
                                z2 = false;
                            } else {
                                i6 = i35;
                                float[] fArr5 = this.radii;
                                fArr5[1] = f6;
                                fArr5[0] = f6;
                                z2 = true;
                            }
                            boolean z6 = z2;
                            int i43 = i39 + 1;
                            if (!has(i43, i42) || !has(i43, i36) || !has(i39, i42)) {
                                z3 = z6;
                            } else {
                                float[] fArr6 = this.radii;
                                fArr6[3] = f6;
                                fArr6[2] = f6;
                                z3 = true;
                            }
                            int i44 = i36 + 1;
                            if (!has(i41, i44) || !has(i41, i36) || !has(i39, i44)) {
                                z4 = z3;
                            } else {
                                float[] fArr7 = this.radii;
                                fArr7[7] = f6;
                                fArr7[6] = f6;
                                z4 = true;
                            }
                            if (has(i43, i44) && has(i43, i36) && has(i39, i44)) {
                                float[] fArr8 = this.radii;
                                fArr8[5] = f6;
                                fArr8[4] = f6;
                                z4 = true;
                            }
                            if (!z4 || z5) {
                                z = z5;
                                f2 = f6;
                            } else {
                                int i45 = i40 + min;
                                z = z5;
                                int i46 = i37 + min;
                                f2 = f6;
                                canvas3.drawRect(i40, i37, i45, i46, paint4);
                                gradientDrawable3.setColor(i3);
                                gradientDrawable3.setBounds(i40, i37, i45, i46);
                                gradientDrawable3.draw(canvas3);
                            }
                        }
                        i39++;
                        i40 += min;
                        i12 = i4;
                        z5 = z;
                        f6 = f2;
                        i35 = i6;
                    }
                    i36++;
                    i37 += min;
                    i12 = i4;
                    i34 = i38;
                }
                String readRes = RLottieDrawable.readRes(null, R.raw.qr_logo);
                int i47 = this.imageSize;
                Bitmap bitmap3 = SvgHelper.getBitmap(readRes, i47, i47, false);
                float f7 = i33;
                canvas3.drawBitmap(bitmap3, f7, f7, (Paint) null);
                bitmap3.recycle();
                canvas3.setBitmap(null);
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
