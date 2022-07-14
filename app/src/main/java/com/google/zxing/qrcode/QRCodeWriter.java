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
import com.google.zxing.qrcode.encoder.QRCode;
import java.util.Arrays;
import java.util.Map;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.beta.R;
import org.telegram.ui.Components.RLottieDrawable;
/* loaded from: classes3.dex */
public final class QRCodeWriter {
    private static final int QUIET_ZONE_SIZE = 4;
    private int imageBlockX;
    private int imageBloks;
    private int imageSize;
    private ByteMatrix input;
    private float[] radii = new float[8];
    private int sideQuadSize;

    public Bitmap encode(String contents, int width, int height, Map<EncodeHintType, ?> hints, Bitmap bitmap) throws WriterException {
        return encode(contents, width, height, hints, bitmap, 1.0f, -1, -16777216);
    }

    /* JADX WARN: Removed duplicated region for block: B:128:0x0406 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00e3  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0116  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x027c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Bitmap encode(String contents, int width, int height, Map<EncodeHintType, ?> hints, Bitmap bitmap, float radiusFactor, int backgroundColor, int color) throws WriterException {
        Bitmap bitmap2;
        int round;
        int a;
        int inputWidth;
        int inputHeight;
        int y;
        int outputY;
        int inputHeight2;
        int inputWidth2;
        int inputHeight3;
        float r;
        boolean has;
        boolean has2;
        boolean has3;
        boolean has4;
        float f;
        int size;
        int size2;
        int x;
        int padding;
        int inputHeight4;
        int inputWidth3;
        int imageX;
        Bitmap bitmap3;
        RectF rectF;
        Path clipPath;
        int i = color;
        if (!contents.isEmpty()) {
            if (width < 0 || height < 0) {
                throw new IllegalArgumentException("Requested dimensions are too small: " + width + 'x' + height);
            }
            ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
            int quietZone = 4;
            if (hints != null) {
                if (hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
                    errorCorrectionLevel = ErrorCorrectionLevel.valueOf(hints.get(EncodeHintType.ERROR_CORRECTION).toString());
                }
                if (hints.containsKey(EncodeHintType.MARGIN)) {
                    quietZone = Integer.parseInt(hints.get(EncodeHintType.MARGIN).toString());
                }
            }
            QRCode code = Encoder.encode(contents, errorCorrectionLevel, hints);
            ByteMatrix matrix = code.getMatrix();
            this.input = matrix;
            if (matrix == null) {
                throw new IllegalStateException();
            }
            int inputWidth4 = matrix.getWidth();
            int inputHeight5 = this.input.getHeight();
            for (int x2 = 0; x2 < inputWidth4 && has(x2, 0); x2++) {
                this.sideQuadSize++;
            }
            int x3 = quietZone * 2;
            int qrWidth = x3 + inputWidth4;
            int qrHeight = (quietZone * 2) + inputHeight5;
            int outputWidth = Math.max(width, qrWidth);
            int outputHeight = Math.max(height, qrHeight);
            int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
            int padding2 = 16;
            int size3 = (multiple * inputWidth4) + (16 * 2);
            if (bitmap != null && bitmap.getWidth() == size3) {
                bitmap2 = bitmap;
                Canvas canvas = new Canvas(bitmap2);
                canvas.drawColor(backgroundColor);
                Paint blackPaint = new Paint(1);
                blackPaint.setColor(i);
                GradientDrawable rect = new GradientDrawable();
                rect.setShape(0);
                rect.setCornerRadii(this.radii);
                round = Math.round(((size3 - 32) / 4.65f) / multiple);
                this.imageBloks = round;
                if (round % 2 != inputWidth4 % 2) {
                    this.imageBloks = round + 1;
                }
                int i2 = this.imageBloks;
                this.imageBlockX = (inputWidth4 - i2) / 2;
                int i3 = (i2 * multiple) - 24;
                this.imageSize = i3;
                int imageX2 = (size3 - i3) / 2;
                boolean isTransparentBackground = Color.alpha(backgroundColor) != 0;
                Path clipPath2 = new Path();
                RectF rectF2 = new RectF();
                a = 0;
                while (a < 3) {
                    if (a == 0) {
                        x = padding2;
                        size = size3;
                        size2 = padding2;
                    } else if (a == 1) {
                        x = (size3 - (this.sideQuadSize * multiple)) - padding2;
                        size = size3;
                        size2 = padding2;
                    } else {
                        int x4 = padding2;
                        int x5 = this.sideQuadSize;
                        size = size3;
                        size2 = (size3 - (x5 * multiple)) - padding2;
                        x = x4;
                    }
                    if (!isTransparentBackground) {
                        imageX = imageX2;
                        bitmap3 = bitmap2;
                        inputWidth3 = inputWidth4;
                        inputHeight4 = inputHeight5;
                        padding = padding2;
                        rectF = rectF2;
                        clipPath = clipPath2;
                    } else {
                        bitmap3 = bitmap2;
                        imageX = imageX2;
                        int imageX3 = size2 + multiple;
                        inputWidth3 = inputWidth4;
                        int inputWidth5 = this.sideQuadSize;
                        inputHeight4 = inputHeight5;
                        padding = padding2;
                        rectF = rectF2;
                        rectF.set(x + multiple, imageX3, x + ((inputWidth5 - 1) * multiple), ((inputWidth5 - 1) * multiple) + size2);
                        float r2 = ((this.sideQuadSize * multiple) / 4.0f) * radiusFactor;
                        clipPath2.reset();
                        clipPath = clipPath2;
                        clipPath.addRoundRect(rectF, r2, r2, Path.Direction.CW);
                        clipPath.close();
                        canvas.save();
                        canvas.clipPath(clipPath, Region.Op.DIFFERENCE);
                    }
                    int imageX4 = this.sideQuadSize;
                    float r3 = ((imageX4 * multiple) / 3.0f) * radiusFactor;
                    Arrays.fill(this.radii, r3);
                    rect.setColor(i);
                    int i4 = this.sideQuadSize;
                    rect.setBounds(x, size2, (i4 * multiple) + x, (i4 * multiple) + size2);
                    rect.draw(canvas);
                    float r4 = r3;
                    int i5 = this.sideQuadSize;
                    Path clipPath3 = clipPath;
                    canvas.drawRect(x + multiple, size2 + multiple, x + ((i5 - 1) * multiple), ((i5 - 1) * multiple) + size2, blackPaint);
                    if (isTransparentBackground) {
                        canvas.restore();
                    }
                    if (!isTransparentBackground) {
                        float r5 = ((this.sideQuadSize * multiple) / 4.0f) * radiusFactor;
                        Arrays.fill(this.radii, r5);
                        rect.setColor(backgroundColor);
                        int i6 = this.sideQuadSize;
                        r4 = r5;
                        rect.setBounds(x + multiple, size2 + multiple, x + ((i6 - 1) * multiple), ((i6 - 1) * multiple) + size2);
                        rect.draw(canvas);
                    }
                    float r6 = (((this.sideQuadSize - 2) * multiple) / 4.0f) * radiusFactor;
                    Arrays.fill(this.radii, r6);
                    rect.setColor(i);
                    int i7 = this.sideQuadSize;
                    rect.setBounds((multiple * 2) + x, (multiple * 2) + size2, x + ((i7 - 2) * multiple), ((i7 - 2) * multiple) + size2);
                    rect.draw(canvas);
                    a++;
                    clipPath2 = clipPath3;
                    size3 = size;
                    bitmap2 = bitmap3;
                    imageX2 = imageX;
                    inputWidth4 = inputWidth3;
                    inputHeight5 = inputHeight4;
                    rectF2 = rectF;
                    padding2 = padding;
                }
                int imageX5 = imageX2;
                Bitmap bitmap4 = bitmap2;
                inputWidth = inputWidth4;
                inputHeight = inputHeight5;
                int padding3 = padding2;
                float r7 = (multiple / 2.0f) * radiusFactor;
                y = 0;
                outputY = padding3;
                while (true) {
                    inputHeight2 = inputHeight;
                    if (y >= inputHeight2) {
                        int x6 = 0;
                        int outputX = padding3;
                        while (true) {
                            inputWidth2 = inputWidth;
                            if (x6 < inputWidth2) {
                                inputWidth = inputWidth2;
                                if (has(x6, y)) {
                                    Arrays.fill(this.radii, r7);
                                    if (!has(x6, y - 1)) {
                                        f = 0.0f;
                                    } else {
                                        float[] fArr = this.radii;
                                        f = 0.0f;
                                        fArr[1] = 0.0f;
                                        fArr[0] = 0.0f;
                                        fArr[3] = 0.0f;
                                        fArr[2] = 0.0f;
                                    }
                                    if (has(x6, y + 1)) {
                                        float[] fArr2 = this.radii;
                                        fArr2[7] = f;
                                        fArr2[6] = f;
                                        fArr2[5] = f;
                                        fArr2[4] = f;
                                    }
                                    if (has(x6 - 1, y)) {
                                        float[] fArr3 = this.radii;
                                        fArr3[1] = f;
                                        fArr3[0] = f;
                                        fArr3[7] = f;
                                        fArr3[6] = f;
                                    }
                                    if (has(x6 + 1, y)) {
                                        float[] fArr4 = this.radii;
                                        fArr4[3] = f;
                                        fArr4[2] = f;
                                        fArr4[5] = f;
                                        fArr4[4] = f;
                                    }
                                    rect.setColor(i);
                                    rect.setBounds(outputX, outputY, outputX + multiple, outputY + multiple);
                                    rect.draw(canvas);
                                    r = r7;
                                    inputHeight3 = inputHeight2;
                                } else {
                                    Arrays.fill(this.radii, 0.0f);
                                    if (has(x6 - 1, y - 1) && has(x6 - 1, y) && has(x6, y - 1)) {
                                        float[] fArr5 = this.radii;
                                        fArr5[1] = r7;
                                        fArr5[0] = r7;
                                        has = true;
                                    } else {
                                        has = false;
                                    }
                                    boolean has5 = has;
                                    if (has(x6 + 1, y - 1) && has(x6 + 1, y) && has(x6, y - 1)) {
                                        float[] fArr6 = this.radii;
                                        fArr6[3] = r7;
                                        fArr6[2] = r7;
                                        has2 = true;
                                    } else {
                                        has2 = has5;
                                    }
                                    boolean has6 = has2;
                                    if (has(x6 - 1, y + 1) && has(x6 - 1, y) && has(x6, y + 1)) {
                                        float[] fArr7 = this.radii;
                                        fArr7[7] = r7;
                                        fArr7[6] = r7;
                                        has3 = true;
                                    } else {
                                        has3 = has6;
                                    }
                                    boolean has7 = has3;
                                    if (has(x6 + 1, y + 1) && has(x6 + 1, y) && has(x6, y + 1)) {
                                        float[] fArr8 = this.radii;
                                        fArr8[5] = r7;
                                        fArr8[4] = r7;
                                        has4 = true;
                                    } else {
                                        has4 = has7;
                                    }
                                    if (!has4 || isTransparentBackground) {
                                        r = r7;
                                        inputHeight3 = inputHeight2;
                                    } else {
                                        r = r7;
                                        inputHeight3 = inputHeight2;
                                        canvas.drawRect(outputX, outputY, outputX + multiple, outputY + multiple, blackPaint);
                                        rect.setColor(backgroundColor);
                                        rect.setBounds(outputX, outputY, outputX + multiple, outputY + multiple);
                                        rect.draw(canvas);
                                    }
                                }
                                x6++;
                                outputX += multiple;
                                i = color;
                                r7 = r;
                                inputHeight2 = inputHeight3;
                            }
                        }
                        inputWidth = inputWidth2;
                        y++;
                        outputY += multiple;
                        i = color;
                        inputHeight = inputHeight2;
                    } else {
                        String svg = RLottieDrawable.readRes(null, R.raw.qr_logo);
                        int i8 = this.imageSize;
                        Bitmap icon = SvgHelper.getBitmap(svg, i8, i8, false);
                        canvas.drawBitmap(icon, imageX5, imageX5, (Paint) null);
                        icon.recycle();
                        canvas.setBitmap(null);
                        return bitmap4;
                    }
                }
            }
            bitmap2 = Bitmap.createBitmap(size3, size3, Bitmap.Config.ARGB_8888);
            Canvas canvas2 = new Canvas(bitmap2);
            canvas2.drawColor(backgroundColor);
            Paint blackPaint2 = new Paint(1);
            blackPaint2.setColor(i);
            GradientDrawable rect2 = new GradientDrawable();
            rect2.setShape(0);
            rect2.setCornerRadii(this.radii);
            round = Math.round(((size3 - 32) / 4.65f) / multiple);
            this.imageBloks = round;
            if (round % 2 != inputWidth4 % 2) {
            }
            int i22 = this.imageBloks;
            this.imageBlockX = (inputWidth4 - i22) / 2;
            int i32 = (i22 * multiple) - 24;
            this.imageSize = i32;
            int imageX22 = (size3 - i32) / 2;
            if (Color.alpha(backgroundColor) != 0) {
            }
            Path clipPath22 = new Path();
            RectF rectF22 = new RectF();
            a = 0;
            while (a < 3) {
            }
            int imageX52 = imageX22;
            Bitmap bitmap42 = bitmap2;
            inputWidth = inputWidth4;
            inputHeight = inputHeight5;
            int padding32 = padding2;
            float r72 = (multiple / 2.0f) * radiusFactor;
            y = 0;
            outputY = padding32;
            while (true) {
                inputHeight2 = inputHeight;
                if (y >= inputHeight2) {
                }
                inputWidth = inputWidth2;
                y++;
                outputY += multiple;
                i = color;
                inputHeight = inputHeight2;
            }
        } else {
            throw new IllegalArgumentException("Found empty contents");
        }
    }

    private boolean has(int x, int y) {
        int i = this.imageBlockX;
        if (x >= i) {
            int i2 = this.imageBloks;
            if (x < i + i2 && y >= i && y < i + i2) {
                return false;
            }
        }
        if ((x < this.sideQuadSize || x >= this.input.getWidth() - this.sideQuadSize) && y < this.sideQuadSize) {
            return false;
        }
        return (x >= this.sideQuadSize || y < this.input.getHeight() - this.sideQuadSize) && x >= 0 && y >= 0 && x < this.input.getWidth() && y < this.input.getHeight() && this.input.get(x, y) == 1;
    }

    public int getImageSize() {
        return this.imageSize;
    }
}
