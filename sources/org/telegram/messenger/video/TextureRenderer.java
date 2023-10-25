package org.telegram.messenger.video;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Pair;
import android.view.View;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.BlurringShader;
import org.telegram.ui.Components.EditTextEffects;
import org.telegram.ui.Components.FilterShaders;
import org.telegram.ui.Components.Paint.PaintTypeface;
import org.telegram.ui.Components.Paint.Views.EditTextOutline;
import org.telegram.ui.Components.Paint.Views.LocationMarker;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Stories.recorder.StoryEntry;
/* loaded from: classes.dex */
public class TextureRenderer {
    private static final String FRAGMENT_EXTERNAL_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);}\n";
    private static final String FRAGMENT_SHADER = "precision highp float;\nvarying vec2 vTextureCoord;\nuniform sampler2D sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
    private static final String GRADIENT_FRAGMENT_SHADER = "precision highp float;\nvarying vec2 vTextureCoord;\nuniform vec4 gradientTopColor;\nuniform vec4 gradientBottomColor;\nfloat interleavedGradientNoise(vec2 n) {\n    return fract(52.9829189 * fract(.06711056 * n.x + .00583715 * n.y));\n}\nvoid main() {\n  gl_FragColor = mix(gradientTopColor, gradientBottomColor, vTextureCoord.y + (.2 * interleavedGradientNoise(gl_FragCoord.xy) - .1));\n}\n";
    private static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
    private static final String VERTEX_SHADER_300 = "#version 320 es\nuniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nin vec4 aPosition;\nin vec4 aTextureCoord;\nout vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
    private int NUM_EXTERNAL_SHADER;
    private int NUM_FILTER_SHADER;
    private int NUM_GRADIENT_SHADER;
    private FloatBuffer bitmapVerticesBuffer;
    private boolean blendEnabled;
    private BlurringShader blur;
    private int blurBlurImageHandle;
    private int blurInputTexCoordHandle;
    private int blurMaskImageHandle;
    private String blurPath;
    private int blurPositionHandle;
    private int blurShaderProgram;
    private int[] blurTexture;
    private FloatBuffer blurVerticesBuffer;
    private final MediaController.CropState cropState;
    private ArrayList<AnimatedEmojiDrawable> emojiDrawables;
    private FilterShaders filterShaders;
    private int gradientBottomColor;
    private int gradientBottomColorHandle;
    private FloatBuffer gradientTextureBuffer;
    private int gradientTopColor;
    private int gradientTopColorHandle;
    private FloatBuffer gradientVerticesBuffer;
    private int imageHeight;
    private int imageOrientation;
    private String imagePath;
    private int imageWidth;
    private boolean isPhoto;
    private int[] mProgram;
    private int mTextureID;
    private int[] maPositionHandle;
    private int[] maTextureHandle;
    private ArrayList<VideoEditedInfo.MediaEntity> mediaEntities;
    private int[] muMVPMatrixHandle;
    private int[] muSTMatrixHandle;
    private int originalHeight;
    private int originalWidth;
    private String paintPath;
    private int[] paintTexture;
    private ArrayList<StoryEntry.Part> parts;
    private int[] partsTexture;
    private FloatBuffer partsTextureBuffer;
    private FloatBuffer[] partsVerticesBuffer;
    Path path;
    private FloatBuffer renderTextureBuffer;
    private int simpleInputTexCoordHandle;
    private int simplePositionHandle;
    private int simpleShaderProgram;
    private int simpleSourceImageHandle;
    private Bitmap stickerBitmap;
    private Canvas stickerCanvas;
    private int[] stickerTexture;
    private int texSizeHandle;
    Paint textColorPaint;
    private FloatBuffer textureBuffer;
    private int transformedHeight;
    private int transformedWidth;
    private boolean useMatrixForImagePath;
    private FloatBuffer verticesBuffer;
    private float videoFps;
    Paint xRefPaint;
    float[] bitmapData = {-1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f};
    private float[] mMVPMatrix = new float[16];
    private float[] mSTMatrix = new float[16];
    private float[] mSTMatrixIdentity = new float[16];
    private boolean firstFrame = true;

    /* JADX WARN: Removed duplicated region for block: B:56:0x036c  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x038e  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x03da  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public TextureRenderer(MediaController.SavedFilterState savedFilterState, String str, String str2, String str3, ArrayList<VideoEditedInfo.MediaEntity> arrayList, MediaController.CropState cropState, int i, int i2, int i3, int i4, int i5, float f, boolean z, Integer num, Integer num2, StoryEntry.HDRInfo hDRInfo, ArrayList<StoryEntry.Part> arrayList2) {
        int i6;
        int i7;
        float[] fArr;
        int i8;
        int i9;
        int i10 = i;
        int i11 = i2;
        float f2 = f;
        this.NUM_FILTER_SHADER = -1;
        this.NUM_EXTERNAL_SHADER = -1;
        this.NUM_GRADIENT_SHADER = -1;
        this.isPhoto = z;
        this.parts = arrayList2;
        float[] fArr2 = {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("start textureRenderer w = " + i10 + " h = " + i11 + " r = " + i5 + " fps = " + f2);
            if (cropState != null) {
                FileLog.d("cropState px = " + cropState.cropPx + " py = " + cropState.cropPy + " cScale = " + cropState.cropScale + " cropRotate = " + cropState.cropRotate + " pw = " + cropState.cropPw + " ph = " + cropState.cropPh + " tw = " + cropState.transformWidth + " th = " + cropState.transformHeight + " tr = " + cropState.transformRotation + " mirror = " + cropState.mirrored);
            }
        }
        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.textureBuffer = asFloatBuffer;
        asFloatBuffer.put(fArr2).position(0);
        FloatBuffer asFloatBuffer2 = ByteBuffer.allocateDirect(this.bitmapData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.bitmapVerticesBuffer = asFloatBuffer2;
        asFloatBuffer2.put(this.bitmapData).position(0);
        Matrix.setIdentityM(this.mSTMatrix, 0);
        Matrix.setIdentityM(this.mSTMatrixIdentity, 0);
        if (savedFilterState != null) {
            FilterShaders filterShaders = new FilterShaders(true, hDRInfo);
            this.filterShaders = filterShaders;
            filterShaders.setDelegate(FilterShaders.getFilterShadersDelegate(savedFilterState));
        }
        this.transformedWidth = i10;
        this.transformedHeight = i11;
        this.originalWidth = i3;
        this.originalHeight = i4;
        this.imagePath = str;
        this.paintPath = str2;
        this.blurPath = str3;
        this.mediaEntities = arrayList;
        this.videoFps = f2 == 0.0f ? 30.0f : f2;
        this.cropState = cropState;
        this.NUM_EXTERNAL_SHADER = 0;
        if (num2 == null || num == null) {
            i6 = 1;
        } else {
            this.NUM_GRADIENT_SHADER = 1;
            i6 = 2;
        }
        if (this.filterShaders != null) {
            this.NUM_FILTER_SHADER = i6;
            i6++;
        }
        this.mProgram = new int[i6];
        this.muMVPMatrixHandle = new int[i6];
        this.muSTMatrixHandle = new int[i6];
        this.maPositionHandle = new int[i6];
        this.maTextureHandle = new int[i6];
        Matrix.setIdentityM(this.mMVPMatrix, 0);
        if (num2 != null && num != null) {
            FloatBuffer asFloatBuffer3 = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
            this.gradientVerticesBuffer = asFloatBuffer3;
            asFloatBuffer3.put(new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f}).position(0);
            float[] fArr3 = new float[8];
            fArr3[0] = 0.0f;
            boolean z2 = this.isPhoto;
            fArr3[1] = z2 ? 1.0f : 0.0f;
            fArr3[2] = 1.0f;
            fArr3[3] = z2 ? 1.0f : 0.0f;
            fArr3[4] = 0.0f;
            fArr3[5] = z2 ? 0.0f : 1.0f;
            fArr3[6] = 1.0f;
            fArr3[7] = z2 ? 0.0f : 1.0f;
            FloatBuffer asFloatBuffer4 = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
            this.gradientTextureBuffer = asFloatBuffer4;
            asFloatBuffer4.put(fArr3).position(0);
            this.gradientTopColor = num.intValue();
            this.gradientBottomColor = num2.intValue();
        }
        if (cropState != null) {
            android.graphics.Matrix matrix = cropState.useMatrix;
            if (matrix != null) {
                this.useMatrixForImagePath = true;
                float[] fArr4 = new float[8];
                fArr4[0] = 0.0f;
                fArr4[1] = 0.0f;
                float f3 = i3;
                fArr4[2] = f3;
                fArr4[3] = 0.0f;
                fArr4[4] = 0.0f;
                float f4 = i4;
                fArr4[5] = f4;
                fArr4[6] = f3;
                fArr4[7] = f4;
                matrix.mapPoints(fArr4);
                for (int i12 = 0; i12 < 4; i12++) {
                    int i13 = i12 * 2;
                    fArr4[i13] = ((fArr4[i13] / i10) * 2.0f) - 1.0f;
                    int i14 = i13 + 1;
                    fArr4[i14] = 1.0f - ((fArr4[i14] / i11) * 2.0f);
                }
                FloatBuffer asFloatBuffer5 = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
                this.verticesBuffer = asFloatBuffer5;
                asFloatBuffer5.put(fArr4).position(0);
            } else {
                float[] fArr5 = new float[8];
                fArr5[0] = 0.0f;
                fArr5[1] = 0.0f;
                float f5 = i10;
                fArr5[2] = f5;
                fArr5[3] = 0.0f;
                fArr5[4] = 0.0f;
                float f6 = i11;
                fArr5[5] = f6;
                fArr5[6] = f5;
                fArr5[7] = f6;
                i7 = cropState.transformRotation;
                this.transformedWidth = (int) (this.transformedWidth * cropState.cropPw);
                this.transformedHeight = (int) (this.transformedHeight * cropState.cropPh);
                double d = -cropState.cropRotate;
                Double.isNaN(d);
                float f7 = (float) (d * 0.017453292519943295d);
                int i15 = 0;
                for (int i16 = 4; i15 < i16; i16 = 4) {
                    int i17 = i15 * 2;
                    int i18 = i17 + 1;
                    double d2 = fArr5[i17] - (i10 / 2);
                    double d3 = f7;
                    double cos = Math.cos(d3);
                    Double.isNaN(d2);
                    double d4 = fArr5[i18] - (i11 / 2);
                    double sin = Math.sin(d3);
                    Double.isNaN(d4);
                    double d5 = cropState.cropPx * f5;
                    Double.isNaN(d5);
                    float f8 = ((float) (((cos * d2) - (sin * d4)) + d5)) * cropState.cropScale;
                    double sin2 = Math.sin(d3);
                    Double.isNaN(d2);
                    double cos2 = Math.cos(d3);
                    Double.isNaN(d4);
                    double d6 = (d2 * sin2) + (d4 * cos2);
                    double d7 = cropState.cropPy * f6;
                    Double.isNaN(d7);
                    float f9 = ((float) (d6 - d7)) * cropState.cropScale;
                    fArr5[i17] = (f8 / this.transformedWidth) * 2.0f;
                    fArr5[i18] = (f9 / this.transformedHeight) * 2.0f;
                    i15++;
                    i10 = i;
                    i11 = i2;
                    i7 = i7;
                }
                FloatBuffer asFloatBuffer6 = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
                this.verticesBuffer = asFloatBuffer6;
                asFloatBuffer6.put(fArr5).position(0);
                fArr = this.filterShaders == null ? i7 == 90 ? new float[]{1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f} : i7 == 180 ? new float[]{1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f} : i7 == 270 ? new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f} : new float[]{0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f} : i7 == 90 ? new float[]{1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f} : i7 == 180 ? new float[]{1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f} : i7 == 270 ? new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f} : new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
                if (!this.isPhoto && this.useMatrixForImagePath) {
                    fArr[1] = 1.0f - fArr[1];
                    fArr[3] = 1.0f - fArr[3];
                    fArr[5] = 1.0f - fArr[5];
                    fArr[7] = 1.0f - fArr[7];
                }
                if (cropState != null && cropState.mirrored) {
                    i9 = 0;
                    for (i8 = 4; i9 < i8; i8 = 4) {
                        int i19 = i9 * 2;
                        if (fArr[i19] > 0.5f) {
                            fArr[i19] = 0.0f;
                        } else {
                            fArr[i19] = 1.0f;
                        }
                        i9++;
                    }
                }
                FloatBuffer asFloatBuffer7 = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                this.renderTextureBuffer = asFloatBuffer7;
                asFloatBuffer7.put(fArr).position(0);
            }
        } else {
            FloatBuffer asFloatBuffer8 = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
            this.verticesBuffer = asFloatBuffer8;
            asFloatBuffer8.put(new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f}).position(0);
        }
        i7 = 0;
        if (this.filterShaders == null) {
        }
        if (!this.isPhoto) {
            fArr[1] = 1.0f - fArr[1];
            fArr[3] = 1.0f - fArr[3];
            fArr[5] = 1.0f - fArr[5];
            fArr[7] = 1.0f - fArr[7];
        }
        if (cropState != null) {
            i9 = 0;
            while (i9 < i8) {
            }
        }
        FloatBuffer asFloatBuffer72 = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.renderTextureBuffer = asFloatBuffer72;
        asFloatBuffer72.put(fArr).position(0);
    }

    public int getTextureId() {
        return this.mTextureID;
    }

    private void drawGradient() {
        int i = this.NUM_GRADIENT_SHADER;
        if (i < 0) {
            return;
        }
        GLES20.glUseProgram(this.mProgram[i]);
        GLES20.glVertexAttribPointer(this.maPositionHandle[this.NUM_GRADIENT_SHADER], 2, 5126, false, 8, (Buffer) this.gradientVerticesBuffer);
        GLES20.glEnableVertexAttribArray(this.maPositionHandle[this.NUM_GRADIENT_SHADER]);
        GLES20.glVertexAttribPointer(this.maTextureHandle[this.NUM_GRADIENT_SHADER], 2, 5126, false, 8, (Buffer) this.gradientTextureBuffer);
        GLES20.glEnableVertexAttribArray(this.maTextureHandle[this.NUM_GRADIENT_SHADER]);
        GLES20.glUniformMatrix4fv(this.muSTMatrixHandle[this.NUM_GRADIENT_SHADER], 1, false, this.mSTMatrix, 0);
        GLES20.glUniformMatrix4fv(this.muMVPMatrixHandle[this.NUM_GRADIENT_SHADER], 1, false, this.mMVPMatrix, 0);
        GLES20.glUniform4f(this.gradientTopColorHandle, Color.red(this.gradientTopColor) / 255.0f, Color.green(this.gradientTopColor) / 255.0f, Color.blue(this.gradientTopColor) / 255.0f, Color.alpha(this.gradientTopColor) / 255.0f);
        GLES20.glUniform4f(this.gradientBottomColorHandle, Color.red(this.gradientBottomColor) / 255.0f, Color.green(this.gradientBottomColor) / 255.0f, Color.blue(this.gradientBottomColor) / 255.0f, Color.alpha(this.gradientBottomColor) / 255.0f);
        GLES20.glDrawArrays(5, 0, 4);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v9 */
    public void drawFrame(SurfaceTexture surfaceTexture) {
        int i;
        float[] fArr;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int[] iArr;
        if (this.isPhoto) {
            drawGradient();
            i4 = 0;
        } else {
            surfaceTexture.getTransformMatrix(this.mSTMatrix);
            if (BuildVars.LOGS_ENABLED && this.firstFrame) {
                StringBuilder sb = new StringBuilder();
                int i8 = 0;
                while (true) {
                    float[] fArr2 = this.mSTMatrix;
                    if (i8 >= fArr2.length) {
                        break;
                    }
                    sb.append(fArr2[i8]);
                    sb.append(", ");
                    i8++;
                }
                FileLog.d("stMatrix = " + ((Object) sb));
                this.firstFrame = false;
            }
            if (this.blendEnabled) {
                GLES20.glDisable(3042);
                this.blendEnabled = false;
            }
            FilterShaders filterShaders = this.filterShaders;
            if (filterShaders != null) {
                filterShaders.onVideoFrameUpdate(this.mSTMatrix);
                GLES20.glViewport(0, 0, this.originalWidth, this.originalHeight);
                this.filterShaders.drawSkinSmoothPass();
                this.filterShaders.drawEnhancePass();
                this.filterShaders.drawSharpenPass();
                this.filterShaders.drawCustomParamsPass();
                boolean drawBlurPass = this.filterShaders.drawBlurPass();
                GLES20.glBindFramebuffer(36160, 0);
                int i9 = this.transformedWidth;
                if (i9 != this.originalWidth || this.transformedHeight != this.originalHeight) {
                    GLES20.glViewport(0, 0, i9, this.transformedHeight);
                }
                int renderTexture = this.filterShaders.getRenderTexture(!drawBlurPass);
                int i10 = this.NUM_FILTER_SHADER;
                fArr = this.mSTMatrixIdentity;
                i2 = i10;
                i3 = renderTexture;
                i4 = drawBlurPass;
                i = 3553;
            } else {
                int i11 = this.mTextureID;
                int i12 = this.NUM_EXTERNAL_SHADER;
                i = 36197;
                fArr = this.mSTMatrix;
                i2 = i12;
                i3 = i11;
                i4 = 0;
            }
            drawGradient();
            GLES20.glUseProgram(this.mProgram[i2]);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(i, i3);
            GLES20.glVertexAttribPointer(this.maPositionHandle[i2], 2, 5126, false, 8, (Buffer) this.verticesBuffer);
            GLES20.glEnableVertexAttribArray(this.maPositionHandle[i2]);
            GLES20.glVertexAttribPointer(this.maTextureHandle[i2], 2, 5126, false, 8, (Buffer) this.renderTextureBuffer);
            GLES20.glEnableVertexAttribArray(this.maTextureHandle[i2]);
            int i13 = this.texSizeHandle;
            if (i13 != 0) {
                GLES20.glUniform2f(i13, this.transformedWidth, this.transformedHeight);
            }
            GLES20.glUniformMatrix4fv(this.muSTMatrixHandle[i2], 1, false, fArr, 0);
            GLES20.glUniformMatrix4fv(this.muMVPMatrixHandle[i2], 1, false, this.mMVPMatrix, 0);
            GLES20.glDrawArrays(5, 0, 4);
        }
        if (this.blur != null) {
            if (!this.blendEnabled) {
                GLES20.glEnable(3042);
                GLES20.glBlendFunc(1, 771);
                this.blendEnabled = true;
            }
            if (this.imagePath != null && (iArr = this.paintTexture) != null) {
                i5 = iArr[0];
                i6 = this.imageWidth;
                i7 = this.imageHeight;
            } else {
                FilterShaders filterShaders2 = this.filterShaders;
                if (filterShaders2 != null) {
                    i5 = filterShaders2.getRenderTexture(i4 ^ 1);
                    i6 = this.filterShaders.getRenderBufferWidth();
                    i7 = this.filterShaders.getRenderBufferHeight();
                } else {
                    i5 = -1;
                    i6 = 1;
                    i7 = 1;
                }
            }
            if (i5 != -1) {
                this.blur.draw(null, i5, i6, i7);
                GLES20.glViewport(0, 0, this.transformedWidth, this.transformedHeight);
                GLES20.glBindFramebuffer(36160, 0);
                GLES20.glUseProgram(this.blurShaderProgram);
                GLES20.glEnableVertexAttribArray(this.blurInputTexCoordHandle);
                GLES20.glVertexAttribPointer(this.blurInputTexCoordHandle, 2, 5126, false, 8, (Buffer) this.gradientTextureBuffer);
                GLES20.glEnableVertexAttribArray(this.blurPositionHandle);
                GLES20.glVertexAttribPointer(this.blurPositionHandle, 2, 5126, false, 8, (Buffer) this.blurVerticesBuffer);
                GLES20.glUniform1i(this.blurBlurImageHandle, 0);
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(3553, this.blur.getTexture());
                GLES20.glUniform1i(this.blurMaskImageHandle, 1);
                GLES20.glActiveTexture(33985);
                GLES20.glBindTexture(3553, this.blurTexture[0]);
                GLES20.glDrawArrays(5, 0, 4);
            }
        }
        if (this.isPhoto || this.paintTexture != null || this.stickerTexture != null || this.partsTexture != null) {
            GLES20.glUseProgram(this.simpleShaderProgram);
            GLES20.glActiveTexture(33984);
            GLES20.glUniform1i(this.simpleSourceImageHandle, 0);
            GLES20.glEnableVertexAttribArray(this.simpleInputTexCoordHandle);
            GLES20.glVertexAttribPointer(this.simpleInputTexCoordHandle, 2, 5126, false, 8, (Buffer) this.textureBuffer);
            GLES20.glEnableVertexAttribArray(this.simplePositionHandle);
        }
        if (this.paintTexture != null && this.imagePath != null) {
            int i14 = 0;
            while (i14 < 1) {
                drawTexture(true, this.paintTexture[i14], -10000.0f, -10000.0f, -10000.0f, -10000.0f, 0.0f, false, this.useMatrixForImagePath && this.isPhoto && i14 == 0, -1);
                i14++;
            }
        }
        if (this.partsTexture != null) {
            int i15 = 0;
            while (true) {
                int[] iArr2 = this.partsTexture;
                if (i15 >= iArr2.length) {
                    break;
                }
                drawTexture(true, iArr2[i15], -10000.0f, -10000.0f, -10000.0f, -10000.0f, 0.0f, false, false, i15);
                i15++;
            }
        }
        if (this.paintTexture != null) {
            int i16 = this.imagePath != null ? 1 : 0;
            while (true) {
                int[] iArr3 = this.paintTexture;
                if (i16 >= iArr3.length) {
                    break;
                }
                drawTexture(true, iArr3[i16], -10000.0f, -10000.0f, -10000.0f, -10000.0f, 0.0f, false, this.useMatrixForImagePath && this.isPhoto && i16 == 0, -1);
                i16++;
            }
        }
        if (this.stickerTexture != null) {
            int size = this.mediaEntities.size();
            for (int i17 = 0; i17 < size; i17++) {
                drawEntity(this.mediaEntities.get(i17), this.mediaEntities.get(i17).color);
            }
        }
        GLES20.glFinish();
    }

    private void drawEntity(VideoEditedInfo.MediaEntity mediaEntity, int i) {
        VideoEditedInfo.MediaEntity mediaEntity2;
        int i2;
        int i3;
        long j = mediaEntity.ptr;
        boolean z = true;
        if (j != 0) {
            Bitmap bitmap = mediaEntity.bitmap;
            if (bitmap == null || (i2 = mediaEntity.W) <= 0 || (i3 = mediaEntity.H) <= 0) {
                return;
            }
            RLottieDrawable.getFrame(j, (int) mediaEntity.currentFrame, bitmap, i2, i3, bitmap.getRowBytes(), true);
            applyRoundRadius(mediaEntity, mediaEntity.bitmap, (mediaEntity.subType & 8) != 0 ? i : 0);
            GLES20.glBindTexture(3553, this.stickerTexture[0]);
            GLUtils.texImage2D(3553, 0, mediaEntity.bitmap, 0);
            float f = mediaEntity.currentFrame + mediaEntity.framesPerDraw;
            mediaEntity.currentFrame = f;
            if (f >= mediaEntity.metadata[0]) {
                mediaEntity.currentFrame = 0.0f;
            }
            drawTexture(false, this.stickerTexture[0], mediaEntity.x, mediaEntity.y, mediaEntity.width, mediaEntity.height, mediaEntity.rotation, (mediaEntity.subType & 2) != 0);
        } else if (mediaEntity.animatedFileDrawable != null) {
            float f2 = mediaEntity.currentFrame;
            int i4 = (int) f2;
            float f3 = f2 + mediaEntity.framesPerDraw;
            mediaEntity.currentFrame = f3;
            for (int i5 = (int) f3; i4 != i5; i5--) {
                mediaEntity.animatedFileDrawable.getNextFrame();
            }
            Bitmap backgroundBitmap = mediaEntity.animatedFileDrawable.getBackgroundBitmap();
            if (backgroundBitmap != null) {
                if (this.stickerCanvas == null && this.stickerBitmap != null) {
                    this.stickerCanvas = new Canvas(this.stickerBitmap);
                    if (this.stickerBitmap.getHeight() != backgroundBitmap.getHeight() || this.stickerBitmap.getWidth() != backgroundBitmap.getWidth()) {
                        this.stickerCanvas.scale(this.stickerBitmap.getWidth() / backgroundBitmap.getWidth(), this.stickerBitmap.getHeight() / backgroundBitmap.getHeight());
                    }
                }
                Bitmap bitmap2 = this.stickerBitmap;
                if (bitmap2 != null) {
                    bitmap2.eraseColor(0);
                    this.stickerCanvas.drawBitmap(backgroundBitmap, 0.0f, 0.0f, (Paint) null);
                    applyRoundRadius(mediaEntity, this.stickerBitmap, (mediaEntity.subType & 8) != 0 ? i : 0);
                    GLES20.glBindTexture(3553, this.stickerTexture[0]);
                    GLUtils.texImage2D(3553, 0, this.stickerBitmap, 0);
                    drawTexture(false, this.stickerTexture[0], mediaEntity.x, mediaEntity.y, mediaEntity.width, mediaEntity.height, mediaEntity.rotation, (mediaEntity.subType & 2) != 0);
                }
            }
        } else {
            if (mediaEntity.bitmap != null) {
                GLES20.glBindTexture(3553, this.stickerTexture[0]);
                GLUtils.texImage2D(3553, 0, mediaEntity.bitmap, 0);
                int i6 = this.stickerTexture[0];
                float f4 = mediaEntity.x;
                float f5 = mediaEntity.additionalWidth;
                float f6 = f4 - (f5 / 2.0f);
                float f7 = mediaEntity.y;
                float f8 = mediaEntity.additionalHeight;
                drawTexture(false, i6, f6, f7 - (f8 / 2.0f), mediaEntity.width + f5, f8 + mediaEntity.height, mediaEntity.rotation, (mediaEntity.type != 2 || (mediaEntity.subType & 2) == 0) ? false : false);
            }
            ArrayList<VideoEditedInfo.EmojiEntity> arrayList = mediaEntity.entities;
            if (arrayList == null || arrayList.isEmpty()) {
                return;
            }
            for (int i7 = 0; i7 < mediaEntity.entities.size(); i7++) {
                VideoEditedInfo.EmojiEntity emojiEntity = mediaEntity.entities.get(i7);
                if (emojiEntity != null && (mediaEntity2 = emojiEntity.entity) != null) {
                    drawEntity(mediaEntity2, mediaEntity.color);
                }
            }
        }
    }

    private void applyRoundRadius(VideoEditedInfo.MediaEntity mediaEntity, Bitmap bitmap, int i) {
        if (bitmap == null || mediaEntity == null) {
            return;
        }
        if (mediaEntity.roundRadius == 0.0f && i == 0) {
            return;
        }
        if (mediaEntity.roundRadiusCanvas == null) {
            mediaEntity.roundRadiusCanvas = new Canvas(bitmap);
        }
        if (mediaEntity.roundRadius != 0.0f) {
            if (this.path == null) {
                this.path = new Path();
            }
            if (this.xRefPaint == null) {
                Paint paint = new Paint(1);
                this.xRefPaint = paint;
                paint.setColor(-16777216);
                this.xRefPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            }
            float min = Math.min(bitmap.getWidth(), bitmap.getHeight()) * mediaEntity.roundRadius;
            this.path.rewind();
            this.path.addRoundRect(new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight()), min, min, Path.Direction.CCW);
            this.path.toggleInverseFillType();
            mediaEntity.roundRadiusCanvas.drawPath(this.path, this.xRefPaint);
        }
        if (i != 0) {
            if (this.textColorPaint == null) {
                Paint paint2 = new Paint(1);
                this.textColorPaint = paint2;
                paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            }
            this.textColorPaint.setColor(i);
            mediaEntity.roundRadiusCanvas.drawRect(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight(), this.textColorPaint);
        }
    }

    private void drawTexture(boolean z, int i) {
        drawTexture(z, i, -10000.0f, -10000.0f, -10000.0f, -10000.0f, 0.0f, false);
    }

    private void drawTexture(boolean z, int i, float f, float f2, float f3, float f4, float f5, boolean z2) {
        drawTexture(z, i, f, f2, f3, f4, f5, z2, false, -1);
    }

    private void drawTexture(boolean z, int i, float f, float f2, float f3, float f4, float f5, boolean z2, boolean z3, int i2) {
        if (!this.blendEnabled) {
            GLES20.glEnable(3042);
            GLES20.glBlendFunc(1, 771);
            this.blendEnabled = true;
        }
        if (f <= -10000.0f) {
            float[] fArr = this.bitmapData;
            fArr[0] = -1.0f;
            fArr[1] = 1.0f;
            fArr[2] = 1.0f;
            fArr[3] = 1.0f;
            fArr[4] = -1.0f;
            fArr[5] = -1.0f;
            fArr[6] = 1.0f;
            fArr[7] = -1.0f;
        } else {
            float f6 = (f * 2.0f) - 1.0f;
            float f7 = ((1.0f - f2) * 2.0f) - 1.0f;
            float[] fArr2 = this.bitmapData;
            fArr2[0] = f6;
            fArr2[1] = f7;
            float f8 = (f3 * 2.0f) + f6;
            fArr2[2] = f8;
            fArr2[3] = f7;
            fArr2[4] = f6;
            float f9 = f7 - (f4 * 2.0f);
            fArr2[5] = f9;
            fArr2[6] = f8;
            fArr2[7] = f9;
        }
        float[] fArr3 = this.bitmapData;
        float f10 = (fArr3[0] + fArr3[2]) / 2.0f;
        if (z2) {
            float f11 = fArr3[2];
            fArr3[2] = fArr3[0];
            fArr3[0] = f11;
            float f12 = fArr3[6];
            fArr3[6] = fArr3[4];
            fArr3[4] = f12;
        }
        if (f5 != 0.0f) {
            float f13 = this.transformedWidth / this.transformedHeight;
            float f14 = (fArr3[5] + fArr3[1]) / 2.0f;
            int i3 = 0;
            for (int i4 = 4; i3 < i4; i4 = 4) {
                float[] fArr4 = this.bitmapData;
                int i5 = i3 * 2;
                int i6 = i5 + 1;
                double d = fArr4[i5] - f10;
                double d2 = f5;
                double cos = Math.cos(d2);
                Double.isNaN(d);
                double d3 = (fArr4[i6] - f14) / f13;
                double sin = Math.sin(d2);
                Double.isNaN(d3);
                fArr4[i5] = ((float) ((cos * d) - (sin * d3))) + f10;
                float[] fArr5 = this.bitmapData;
                double sin2 = Math.sin(d2);
                Double.isNaN(d);
                double cos2 = Math.cos(d2);
                Double.isNaN(d3);
                fArr5[i6] = (((float) ((d * sin2) + (d3 * cos2))) * f13) + f14;
                i3++;
            }
        }
        this.bitmapVerticesBuffer.put(this.bitmapData).position(0);
        GLES20.glVertexAttribPointer(this.simplePositionHandle, 2, 5126, false, 8, (Buffer) (i2 >= 0 ? this.partsVerticesBuffer[i2] : z3 ? this.verticesBuffer : this.bitmapVerticesBuffer));
        GLES20.glEnableVertexAttribArray(this.simpleInputTexCoordHandle);
        GLES20.glVertexAttribPointer(this.simpleInputTexCoordHandle, 2, 5126, false, 8, (Buffer) (i2 >= 0 ? this.partsTextureBuffer : z3 ? this.renderTextureBuffer : this.textureBuffer));
        if (z) {
            GLES20.glBindTexture(3553, i);
        }
        GLES20.glDrawArrays(5, 0, 4);
    }

    public void setBreakStrategy(EditTextOutline editTextOutline) {
        editTextOutline.setBreakStrategy(0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:171:0x060c, code lost:
        if (org.telegram.messenger.LocaleController.isRTL != false) goto L131;
     */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0325  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x032a  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x032d  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0322  */
    @SuppressLint({"WrongConstant"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void surfaceCreated() {
        String str;
        int i;
        int i2;
        float max;
        int i3;
        float f;
        VideoEditedInfo.MediaEntity mediaEntity;
        int i4;
        Typeface typeface;
        MediaController.CropState cropState;
        int i5 = 0;
        while (true) {
            int[] iArr = this.mProgram;
            String str2 = null;
            if (i5 >= iArr.length) {
                break;
            }
            if (i5 == this.NUM_EXTERNAL_SHADER) {
                str2 = FRAGMENT_EXTERNAL_SHADER;
            } else if (i5 == this.NUM_FILTER_SHADER) {
                str2 = FRAGMENT_SHADER;
            } else if (i5 == this.NUM_GRADIENT_SHADER) {
                str2 = GRADIENT_FRAGMENT_SHADER;
            }
            if (str2 != null) {
                iArr[i5] = createProgram(VERTEX_SHADER, str2, false);
                this.maPositionHandle[i5] = GLES20.glGetAttribLocation(this.mProgram[i5], "aPosition");
                this.maTextureHandle[i5] = GLES20.glGetAttribLocation(this.mProgram[i5], "aTextureCoord");
                this.muMVPMatrixHandle[i5] = GLES20.glGetUniformLocation(this.mProgram[i5], "uMVPMatrix");
                this.muSTMatrixHandle[i5] = GLES20.glGetUniformLocation(this.mProgram[i5], "uSTMatrix");
                if (i5 == this.NUM_GRADIENT_SHADER) {
                    this.gradientTopColorHandle = GLES20.glGetUniformLocation(this.mProgram[i5], "gradientTopColor");
                    this.gradientBottomColorHandle = GLES20.glGetUniformLocation(this.mProgram[i5], "gradientBottomColor");
                }
            }
            i5++;
        }
        boolean z = true;
        int[] iArr2 = new int[1];
        GLES20.glGenTextures(1, iArr2, 0);
        int i6 = iArr2[0];
        this.mTextureID = i6;
        GLES20.glBindTexture(36197, i6);
        GLES20.glTexParameteri(36197, 10241, 9729);
        GLES20.glTexParameteri(36197, 10240, 9729);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        if (this.blurPath != null && (cropState = this.cropState) != null && cropState.useMatrix != null) {
            BlurringShader blurringShader = new BlurringShader();
            this.blur = blurringShader;
            if (!blurringShader.setup(this.transformedWidth / this.transformedHeight, true, 0)) {
                this.blur = null;
            } else {
                this.blur.updateGradient(this.gradientTopColor, this.gradientBottomColor);
                android.graphics.Matrix matrix = new android.graphics.Matrix();
                matrix.postScale(this.originalWidth, this.originalHeight);
                matrix.postConcat(this.cropState.useMatrix);
                matrix.postScale(1.0f / this.transformedWidth, 1.0f / this.transformedHeight);
                android.graphics.Matrix matrix2 = new android.graphics.Matrix();
                matrix.invert(matrix2);
                this.blur.updateTransform(matrix2);
            }
            Bitmap decodeFile = BitmapFactory.decodeFile(this.blurPath);
            if (decodeFile != null) {
                int[] iArr3 = new int[1];
                this.blurTexture = iArr3;
                GLES20.glGenTextures(1, iArr3, 0);
                GLES20.glBindTexture(3553, this.blurTexture[0]);
                GLES20.glTexParameteri(3553, 10241, 9729);
                GLES20.glTexParameteri(3553, 10240, 9729);
                GLES20.glTexParameteri(3553, 10242, 33071);
                GLES20.glTexParameteri(3553, 10243, 33071);
                GLUtils.texImage2D(3553, 0, decodeFile, 0);
                decodeFile.recycle();
            } else {
                this.blur = null;
            }
            if (this.blur != null) {
                int loadShader = FilterShaders.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 vTextureCoord;void main() {gl_Position = position;vTextureCoord = inputTexCoord;}");
                int loadShader2 = FilterShaders.loadShader(35632, "varying highp vec2 vTextureCoord;uniform sampler2D blurImage;uniform sampler2D maskImage;void main() {gl_FragColor = texture2D(blurImage, vTextureCoord) * texture2D(maskImage, vTextureCoord).a;}");
                if (loadShader != 0 && loadShader2 != 0) {
                    int glCreateProgram = GLES20.glCreateProgram();
                    this.blurShaderProgram = glCreateProgram;
                    GLES20.glAttachShader(glCreateProgram, loadShader);
                    GLES20.glAttachShader(this.blurShaderProgram, loadShader2);
                    GLES20.glBindAttribLocation(this.blurShaderProgram, 0, "position");
                    GLES20.glBindAttribLocation(this.blurShaderProgram, 1, "inputTexCoord");
                    GLES20.glLinkProgram(this.blurShaderProgram);
                    int[] iArr4 = new int[1];
                    GLES20.glGetProgramiv(this.blurShaderProgram, 35714, iArr4, 0);
                    if (iArr4[0] == 0) {
                        GLES20.glDeleteProgram(this.blurShaderProgram);
                        this.blurShaderProgram = 0;
                    } else {
                        this.blurPositionHandle = GLES20.glGetAttribLocation(this.blurShaderProgram, "position");
                        this.blurInputTexCoordHandle = GLES20.glGetAttribLocation(this.blurShaderProgram, "inputTexCoord");
                        this.blurBlurImageHandle = GLES20.glGetUniformLocation(this.blurShaderProgram, "blurImage");
                        this.blurMaskImageHandle = GLES20.glGetUniformLocation(this.blurShaderProgram, "maskImage");
                        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
                        this.blurVerticesBuffer = asFloatBuffer;
                        asFloatBuffer.put(new float[]{-1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f}).position(0);
                    }
                } else {
                    this.blur = null;
                }
            }
        }
        if (this.filterShaders != null || this.imagePath != null || this.paintPath != null || this.mediaEntities != null || this.parts != null) {
            int loadShader3 = FilterShaders.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 vTextureCoord;void main() {gl_Position = position;vTextureCoord = inputTexCoord;}");
            int loadShader4 = FilterShaders.loadShader(35632, "varying highp vec2 vTextureCoord;uniform sampler2D sTexture;void main() {gl_FragColor = texture2D(sTexture, vTextureCoord);}");
            if (loadShader3 != 0 && loadShader4 != 0) {
                int glCreateProgram2 = GLES20.glCreateProgram();
                this.simpleShaderProgram = glCreateProgram2;
                GLES20.glAttachShader(glCreateProgram2, loadShader3);
                GLES20.glAttachShader(this.simpleShaderProgram, loadShader4);
                GLES20.glBindAttribLocation(this.simpleShaderProgram, 0, "position");
                GLES20.glBindAttribLocation(this.simpleShaderProgram, 1, "inputTexCoord");
                GLES20.glLinkProgram(this.simpleShaderProgram);
                int[] iArr5 = new int[1];
                GLES20.glGetProgramiv(this.simpleShaderProgram, 35714, iArr5, 0);
                if (iArr5[0] == 0) {
                    GLES20.glDeleteProgram(this.simpleShaderProgram);
                    this.simpleShaderProgram = 0;
                } else {
                    this.simplePositionHandle = GLES20.glGetAttribLocation(this.simpleShaderProgram, "position");
                    this.simpleInputTexCoordHandle = GLES20.glGetAttribLocation(this.simpleShaderProgram, "inputTexCoord");
                    this.simpleSourceImageHandle = GLES20.glGetUniformLocation(this.simpleShaderProgram, "sTexture");
                }
            }
        }
        FilterShaders filterShaders = this.filterShaders;
        if (filterShaders != null) {
            filterShaders.create();
            this.filterShaders.setRenderData(null, 0, this.mTextureID, this.originalWidth, this.originalHeight);
        }
        String str3 = this.imagePath;
        byte b = 2;
        if (str3 != null || this.paintPath != null) {
            int[] iArr6 = new int[(str3 != null ? 1 : 0) + (this.paintPath != null ? 1 : 0)];
            this.paintTexture = iArr6;
            GLES20.glGenTextures(iArr6.length, iArr6, 0);
            for (int i7 = 0; i7 < this.paintTexture.length; i7++) {
                try {
                    if (i7 == 0 && (str = this.imagePath) != null) {
                        Pair<Integer, Integer> imageOrientation = AndroidUtilities.getImageOrientation(str);
                        i2 = ((Integer) imageOrientation.first).intValue();
                        i = ((Integer) imageOrientation.second).intValue();
                    } else {
                        str = this.paintPath;
                        i = 0;
                        i2 = 0;
                    }
                    Bitmap decodeFile2 = BitmapFactory.decodeFile(str);
                    if (decodeFile2 != null) {
                        if (i7 == 0 && this.imagePath != null && !this.useMatrixForImagePath) {
                            Bitmap createBitmap = Bitmap.createBitmap(this.transformedWidth, this.transformedHeight, Bitmap.Config.ARGB_8888);
                            createBitmap.eraseColor(-16777216);
                            Canvas canvas = new Canvas(createBitmap);
                            if (i2 != 90 && i2 != 270) {
                                max = Math.max(decodeFile2.getWidth() / this.transformedWidth, decodeFile2.getHeight() / this.transformedHeight);
                                android.graphics.Matrix matrix3 = new android.graphics.Matrix();
                                matrix3.postTranslate((-decodeFile2.getWidth()) / 2, (-decodeFile2.getHeight()) / 2);
                                matrix3.postScale((i != 1 ? -1.0f : 1.0f) / max, (i != 2 ? -1.0f : 1.0f) / max);
                                matrix3.postRotate(i2);
                                matrix3.postTranslate(createBitmap.getWidth() / 2, createBitmap.getHeight() / 2);
                                canvas.drawBitmap(decodeFile2, matrix3, new Paint(2));
                                decodeFile2 = createBitmap;
                            }
                            max = Math.max(decodeFile2.getHeight() / this.transformedWidth, decodeFile2.getWidth() / this.transformedHeight);
                            android.graphics.Matrix matrix32 = new android.graphics.Matrix();
                            matrix32.postTranslate((-decodeFile2.getWidth()) / 2, (-decodeFile2.getHeight()) / 2);
                            matrix32.postScale((i != 1 ? -1.0f : 1.0f) / max, (i != 2 ? -1.0f : 1.0f) / max);
                            matrix32.postRotate(i2);
                            matrix32.postTranslate(createBitmap.getWidth() / 2, createBitmap.getHeight() / 2);
                            canvas.drawBitmap(decodeFile2, matrix32, new Paint(2));
                            decodeFile2 = createBitmap;
                        }
                        if (i7 == 0 && this.imagePath != null) {
                            this.imageWidth = decodeFile2.getWidth();
                            this.imageHeight = decodeFile2.getHeight();
                        }
                        GLES20.glBindTexture(3553, this.paintTexture[i7]);
                        GLES20.glTexParameteri(3553, 10241, 9729);
                        GLES20.glTexParameteri(3553, 10240, 9729);
                        GLES20.glTexParameteri(3553, 10242, 33071);
                        GLES20.glTexParameteri(3553, 10243, 33071);
                        GLUtils.texImage2D(3553, 0, decodeFile2, 0);
                    }
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
        }
        ArrayList<StoryEntry.Part> arrayList = this.parts;
        if (arrayList != null && !arrayList.isEmpty()) {
            this.partsTexture = new int[this.parts.size()];
            this.partsVerticesBuffer = new FloatBuffer[this.parts.size()];
            int[] iArr7 = this.partsTexture;
            GLES20.glGenTextures(iArr7.length, iArr7, 0);
            for (int i8 = 0; i8 < this.partsTexture.length; i8++) {
                try {
                    StoryEntry.Part part = this.parts.get(i8);
                    String absolutePath = part.file.getAbsolutePath();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(absolutePath, options);
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = StoryEntry.calculateInSampleSize(options, this.transformedWidth, this.transformedHeight);
                    Bitmap decodeFile3 = BitmapFactory.decodeFile(absolutePath, options);
                    GLES20.glBindTexture(3553, this.partsTexture[i8]);
                    GLES20.glTexParameteri(3553, 10241, 9729);
                    GLES20.glTexParameteri(3553, 10240, 9729);
                    GLES20.glTexParameteri(3553, 10242, 33071);
                    GLES20.glTexParameteri(3553, 10243, 33071);
                    GLUtils.texImage2D(3553, 0, decodeFile3, 0);
                    float[] fArr = new float[8];
                    fArr[0] = 0.0f;
                    fArr[1] = 0.0f;
                    int i9 = part.width;
                    fArr[2] = i9;
                    fArr[3] = 0.0f;
                    fArr[4] = 0.0f;
                    int i10 = part.height;
                    fArr[5] = i10;
                    fArr[6] = i9;
                    fArr[7] = i10;
                    part.matrix.mapPoints(fArr);
                    for (int i11 = 0; i11 < 4; i11++) {
                        int i12 = i11 * 2;
                        fArr[i12] = ((fArr[i12] / this.transformedWidth) * 2.0f) - 1.0f;
                        int i13 = i12 + 1;
                        fArr[i13] = 1.0f - ((fArr[i13] / this.transformedHeight) * 2.0f);
                    }
                    this.partsVerticesBuffer[i8] = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
                    this.partsVerticesBuffer[i8].put(fArr).position(0);
                } catch (Throwable th2) {
                    FileLog.e(th2);
                }
            }
            FloatBuffer asFloatBuffer2 = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
            this.partsTextureBuffer = asFloatBuffer2;
            asFloatBuffer2.put(new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f}).position(0);
        }
        if (this.mediaEntities != null) {
            try {
                this.stickerBitmap = Bitmap.createBitmap(LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS, Bitmap.Config.ARGB_8888);
                int[] iArr8 = new int[1];
                this.stickerTexture = iArr8;
                GLES20.glGenTextures(1, iArr8, 0);
                GLES20.glBindTexture(3553, this.stickerTexture[0]);
                GLES20.glTexParameteri(3553, 10241, 9729);
                GLES20.glTexParameteri(3553, 10240, 9729);
                GLES20.glTexParameteri(3553, 10242, 33071);
                GLES20.glTexParameteri(3553, 10243, 33071);
                int size = this.mediaEntities.size();
                int i14 = 0;
                while (i14 < size) {
                    VideoEditedInfo.MediaEntity mediaEntity2 = this.mediaEntities.get(i14);
                    byte b2 = mediaEntity2.type;
                    if (b2 != 0 && b2 != b) {
                        if (b2 == z) {
                            EditTextOutline editTextOutline = new EditTextOutline(ApplicationLoader.applicationContext);
                            editTextOutline.getPaint().setAntiAlias(z);
                            editTextOutline.drawAnimatedEmojiDrawables = false;
                            editTextOutline.setBackgroundColor(0);
                            editTextOutline.setPadding(AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f));
                            PaintTypeface paintTypeface = mediaEntity2.textTypeface;
                            if (paintTypeface != null && (typeface = paintTypeface.getTypeface()) != null) {
                                editTextOutline.setTypeface(typeface);
                            }
                            editTextOutline.setTextSize(0, mediaEntity2.fontSize);
                            SpannableString spannableString = new SpannableString(mediaEntity2.text);
                            Iterator<VideoEditedInfo.EmojiEntity> it = mediaEntity2.entities.iterator();
                            while (it.hasNext()) {
                                final VideoEditedInfo.EmojiEntity next = it.next();
                                if (next.documentAbsolutePath != null) {
                                    VideoEditedInfo.MediaEntity mediaEntity3 = new VideoEditedInfo.MediaEntity();
                                    next.entity = mediaEntity3;
                                    mediaEntity3.text = next.documentAbsolutePath;
                                    mediaEntity3.subType = next.subType;
                                    SpannableString spannableString2 = spannableString;
                                    final EditTextOutline editTextOutline2 = editTextOutline;
                                    final VideoEditedInfo.MediaEntity mediaEntity4 = mediaEntity2;
                                    AnimatedEmojiSpan animatedEmojiSpan = new AnimatedEmojiSpan(0L, 1.0f, editTextOutline.getPaint().getFontMetricsInt()) { // from class: org.telegram.messenger.video.TextureRenderer.1
                                        @Override // org.telegram.ui.Components.AnimatedEmojiSpan, android.text.style.ReplacementSpan
                                        public void draw(Canvas canvas2, CharSequence charSequence, int i15, int i16, float f2, int i17, int i18, int i19, Paint paint) {
                                            super.draw(canvas2, charSequence, i15, i16, f2, i17, i18, i19, paint);
                                            VideoEditedInfo.MediaEntity mediaEntity5 = mediaEntity4;
                                            float paddingLeft = mediaEntity4.x + ((((editTextOutline2.getPaddingLeft() + f2) + (this.measuredSize / 2.0f)) / mediaEntity5.viewWidth) * mediaEntity5.width);
                                            float f3 = mediaEntity5.y;
                                            VideoEditedInfo.MediaEntity mediaEntity6 = mediaEntity4;
                                            float f4 = mediaEntity6.height;
                                            float paddingTop = f3 + ((((editTextOutline2.getPaddingTop() + i17) + ((i19 - i17) / 2.0f)) / mediaEntity6.viewHeight) * f4);
                                            if (mediaEntity6.rotation != 0.0f) {
                                                float f5 = mediaEntity6.x + (mediaEntity6.width / 2.0f);
                                                float f6 = mediaEntity6.y + (f4 / 2.0f);
                                                float f7 = TextureRenderer.this.transformedWidth / TextureRenderer.this.transformedHeight;
                                                double d = paddingLeft - f5;
                                                double cos = Math.cos(-mediaEntity4.rotation);
                                                Double.isNaN(d);
                                                double d2 = (paddingTop - f6) / f7;
                                                double sin = Math.sin(-mediaEntity4.rotation);
                                                Double.isNaN(d2);
                                                float f8 = f5 + ((float) ((cos * d) - (sin * d2)));
                                                double sin2 = Math.sin(-mediaEntity4.rotation);
                                                Double.isNaN(d);
                                                double d3 = d * sin2;
                                                double cos2 = Math.cos(-mediaEntity4.rotation);
                                                Double.isNaN(d2);
                                                paddingTop = (((float) (d3 + (d2 * cos2))) * f7) + f6;
                                                paddingLeft = f8;
                                            }
                                            VideoEditedInfo.MediaEntity mediaEntity7 = next.entity;
                                            int i20 = this.measuredSize;
                                            VideoEditedInfo.MediaEntity mediaEntity8 = mediaEntity4;
                                            float f9 = (i20 / mediaEntity8.viewWidth) * mediaEntity8.width;
                                            mediaEntity7.width = f9;
                                            float f10 = (i20 / mediaEntity8.viewHeight) * mediaEntity8.height;
                                            mediaEntity7.height = f10;
                                            mediaEntity7.x = paddingLeft - (f9 / 2.0f);
                                            mediaEntity7.y = paddingTop - (f10 / 2.0f);
                                            mediaEntity7.rotation = mediaEntity8.rotation;
                                            if (mediaEntity7.bitmap == null) {
                                                TextureRenderer.this.initStickerEntity(mediaEntity7);
                                            }
                                        }
                                    };
                                    int i15 = next.offset;
                                    spannableString2.setSpan(animatedEmojiSpan, i15, next.length + i15, 33);
                                    spannableString = spannableString2;
                                    mediaEntity2 = mediaEntity2;
                                    editTextOutline = editTextOutline2;
                                    i14 = i14;
                                }
                            }
                            EditTextOutline editTextOutline3 = editTextOutline;
                            VideoEditedInfo.MediaEntity mediaEntity5 = mediaEntity2;
                            i3 = i14;
                            editTextOutline3.setText(Emoji.replaceEmoji((CharSequence) spannableString, editTextOutline3.getPaint().getFontMetricsInt(), (int) (editTextOutline3.getTextSize() * 0.8f), false));
                            editTextOutline3.setTextColor(mediaEntity5.color);
                            Editable text = editTextOutline3.getText();
                            if (text instanceof Spanned) {
                                for (Emoji.EmojiSpan emojiSpan : (Emoji.EmojiSpan[]) text.getSpans(0, text.length(), Emoji.EmojiSpan.class)) {
                                    emojiSpan.scale = 0.85f;
                                }
                            }
                            int i16 = mediaEntity5.textAlign;
                            editTextOutline3.setGravity(i16 != z ? i16 != b ? 19 : 21 : 17);
                            int i17 = Build.VERSION.SDK_INT;
                            if (i17 >= 17) {
                                int i18 = mediaEntity5.textAlign;
                                if (i18 == z) {
                                    i4 = 4;
                                } else if (i18 == b) {
                                    if (LocaleController.isRTL) {
                                        i4 = 2;
                                    }
                                    i4 = 3;
                                }
                                editTextOutline3.setTextAlignment(i4);
                            }
                            editTextOutline3.setHorizontallyScrolling(false);
                            editTextOutline3.setImeOptions(268435456);
                            editTextOutline3.setFocusableInTouchMode(z);
                            editTextOutline3.setInputType(editTextOutline3.getInputType() | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM);
                            if (i17 >= 23) {
                                setBreakStrategy(editTextOutline3);
                            }
                            byte b3 = mediaEntity5.subType;
                            if (b3 == 0) {
                                editTextOutline3.setFrameColor(mediaEntity5.color);
                                editTextOutline3.setTextColor(AndroidUtilities.computePerceivedBrightness(mediaEntity5.color) >= 0.721f ? -16777216 : -1);
                            } else if (b3 == z) {
                                editTextOutline3.setFrameColor(AndroidUtilities.computePerceivedBrightness(mediaEntity5.color) >= 0.25f ? -1728053248 : -1711276033);
                                editTextOutline3.setTextColor(mediaEntity5.color);
                            } else if (b3 == b) {
                                editTextOutline3.setFrameColor(AndroidUtilities.computePerceivedBrightness(mediaEntity5.color) >= 0.25f ? -16777216 : -1);
                                editTextOutline3.setTextColor(mediaEntity5.color);
                            } else if (b3 == 3) {
                                editTextOutline3.setFrameColor(0);
                                editTextOutline3.setTextColor(mediaEntity5.color);
                            }
                            editTextOutline3.measure(View.MeasureSpec.makeMeasureSpec(mediaEntity5.viewWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(mediaEntity5.viewHeight, 1073741824));
                            editTextOutline3.layout(0, 0, mediaEntity5.viewWidth, mediaEntity5.viewHeight);
                            mediaEntity5.bitmap = Bitmap.createBitmap(mediaEntity5.viewWidth, mediaEntity5.viewHeight, Bitmap.Config.ARGB_8888);
                            editTextOutline3.draw(new Canvas(mediaEntity5.bitmap));
                        } else {
                            i3 = i14;
                            if (b2 == 3) {
                                LocationMarker locationMarker = new LocationMarker(ApplicationLoader.applicationContext, mediaEntity2.density);
                                locationMarker.setText(mediaEntity2.text);
                                locationMarker.setType(mediaEntity2.subType, mediaEntity2.color);
                                locationMarker.setMaxWidth(mediaEntity2.viewWidth);
                                if (mediaEntity2.entities.size() == z) {
                                    locationMarker.forceEmoji();
                                }
                                locationMarker.measure(View.MeasureSpec.makeMeasureSpec(mediaEntity2.viewWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(mediaEntity2.viewHeight, 1073741824));
                                locationMarker.layout(0, 0, mediaEntity2.viewWidth, mediaEntity2.viewHeight);
                                float f2 = mediaEntity2.width * this.transformedWidth;
                                int i19 = mediaEntity2.viewWidth;
                                float f3 = f2 / i19;
                                mediaEntity2.bitmap = Bitmap.createBitmap(((int) (i19 * f3)) + 8 + 8, ((int) (mediaEntity2.viewHeight * f3)) + 8 + 8, Bitmap.Config.ARGB_8888);
                                Canvas canvas2 = new Canvas(mediaEntity2.bitmap);
                                float f4 = 8;
                                canvas2.translate(f4, f4);
                                canvas2.scale(f3, f3);
                                locationMarker.draw(canvas2);
                                float f5 = 16 * f3;
                                mediaEntity2.additionalWidth = f5 / this.transformedWidth;
                                mediaEntity2.additionalHeight = f5 / this.transformedHeight;
                                if (mediaEntity2.entities.size() == z) {
                                    VideoEditedInfo.EmojiEntity emojiEntity = mediaEntity2.entities.get(0);
                                    VideoEditedInfo.MediaEntity mediaEntity6 = new VideoEditedInfo.MediaEntity();
                                    emojiEntity.entity = mediaEntity6;
                                    mediaEntity6.text = emojiEntity.documentAbsolutePath;
                                    mediaEntity6.subType = emojiEntity.subType;
                                    RectF rectF = new RectF();
                                    locationMarker.getEmojiBounds(rectF);
                                    float centerX = mediaEntity2.x + ((rectF.centerX() / mediaEntity2.viewWidth) * mediaEntity2.width);
                                    float f6 = mediaEntity2.y;
                                    float centerY = rectF.centerY() / mediaEntity2.viewHeight;
                                    float f7 = mediaEntity2.height;
                                    float f8 = f6 + (centerY * f7);
                                    if (mediaEntity2.rotation != 0.0f) {
                                        float f9 = mediaEntity2.x + (mediaEntity2.width / 2.0f);
                                        float f10 = mediaEntity2.y + (f7 / 2.0f);
                                        float f11 = this.transformedWidth / this.transformedHeight;
                                        float f12 = (f8 - f10) / f11;
                                        double d = centerX - f9;
                                        double cos = Math.cos(-f);
                                        Double.isNaN(d);
                                        double d2 = cos * d;
                                        double d3 = f12;
                                        double sin = Math.sin(-mediaEntity2.rotation);
                                        Double.isNaN(d3);
                                        centerX = ((float) (d2 - (sin * d3))) + f9;
                                        mediaEntity = mediaEntity2;
                                        double sin2 = Math.sin(-mediaEntity.rotation);
                                        Double.isNaN(d);
                                        double d4 = d * sin2;
                                        double cos2 = Math.cos(-mediaEntity.rotation);
                                        Double.isNaN(d3);
                                        f8 = (((float) (d4 + (d3 * cos2))) * f11) + f10;
                                    } else {
                                        mediaEntity = mediaEntity2;
                                    }
                                    emojiEntity.entity.width = (rectF.width() / mediaEntity.viewWidth) * mediaEntity.width;
                                    emojiEntity.entity.height = (rectF.height() / mediaEntity.viewHeight) * mediaEntity.height;
                                    VideoEditedInfo.MediaEntity mediaEntity7 = emojiEntity.entity;
                                    float f13 = mediaEntity7.width * 1.2f;
                                    mediaEntity7.width = f13;
                                    float f14 = mediaEntity7.height * 1.2f;
                                    mediaEntity7.height = f14;
                                    mediaEntity7.x = centerX - (f13 / 2.0f);
                                    mediaEntity7.y = f8 - (f14 / 2.0f);
                                    mediaEntity7.rotation = mediaEntity.rotation;
                                    initStickerEntity(mediaEntity7);
                                    i14 = i3 + 1;
                                    z = true;
                                    b = 2;
                                }
                            }
                        }
                        i14 = i3 + 1;
                        z = true;
                        b = 2;
                    }
                    i3 = i14;
                    initStickerEntity(mediaEntity2);
                    i14 = i3 + 1;
                    z = true;
                    b = 2;
                }
            } catch (Throwable th3) {
                FileLog.e(th3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initStickerEntity(VideoEditedInfo.MediaEntity mediaEntity) {
        Bitmap bitmap;
        AnimatedFileDrawable animatedFileDrawable;
        int i;
        int i2 = (int) (mediaEntity.width * this.transformedWidth);
        mediaEntity.W = i2;
        int i3 = (int) (mediaEntity.height * this.transformedHeight);
        mediaEntity.H = i3;
        if (i2 > 512) {
            mediaEntity.H = (int) ((i3 / i2) * 512.0f);
            mediaEntity.W = LiteMode.FLAG_CALLS_ANIMATIONS;
        }
        int i4 = mediaEntity.H;
        if (i4 > 512) {
            mediaEntity.W = (int) ((mediaEntity.W / i4) * 512.0f);
            mediaEntity.H = LiteMode.FLAG_CALLS_ANIMATIONS;
        }
        byte b = mediaEntity.subType;
        if ((b & 1) != 0) {
            int i5 = mediaEntity.W;
            if (i5 <= 0 || (i = mediaEntity.H) <= 0) {
                return;
            }
            mediaEntity.bitmap = Bitmap.createBitmap(i5, i, Bitmap.Config.ARGB_8888);
            int[] iArr = new int[3];
            mediaEntity.metadata = iArr;
            mediaEntity.ptr = RLottieDrawable.create(mediaEntity.text, null, mediaEntity.W, mediaEntity.H, iArr, false, null, false, 0);
            mediaEntity.framesPerDraw = mediaEntity.metadata[1] / this.videoFps;
        } else if ((b & 4) != 0) {
            mediaEntity.animatedFileDrawable = new AnimatedFileDrawable(new File(mediaEntity.text), true, 0L, 0, null, null, null, 0L, UserConfig.selectedAccount, true, LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS, null);
            mediaEntity.framesPerDraw = animatedFileDrawable.getFps() / this.videoFps;
            mediaEntity.currentFrame = 0.0f;
            mediaEntity.animatedFileDrawable.getNextFrame();
        } else {
            if (Build.VERSION.SDK_INT >= 19) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                if (mediaEntity.type == 2) {
                    options.inMutable = true;
                }
                mediaEntity.bitmap = BitmapFactory.decodeFile(mediaEntity.text, options);
            } else {
                try {
                    File file = new File(mediaEntity.text);
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                    MappedByteBuffer map = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
                    BitmapFactory.Options options2 = new BitmapFactory.Options();
                    options2.inJustDecodeBounds = true;
                    Utilities.loadWebpImage(null, map, map.limit(), options2, true);
                    if (mediaEntity.type == 2) {
                        options2.inMutable = true;
                    }
                    Bitmap createBitmap = Bitmaps.createBitmap(options2.outWidth, options2.outHeight, Bitmap.Config.ARGB_8888);
                    mediaEntity.bitmap = createBitmap;
                    Utilities.loadWebpImage(createBitmap, map, map.limit(), null, true);
                    randomAccessFile.close();
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
            if (mediaEntity.type == 2 && mediaEntity.bitmap != null) {
                mediaEntity.roundRadius = AndroidUtilities.dp(12.0f) / Math.min(mediaEntity.viewWidth, mediaEntity.viewHeight);
                Pair<Integer, Integer> imageOrientation = AndroidUtilities.getImageOrientation(mediaEntity.text);
                double d = mediaEntity.rotation;
                double radians = Math.toRadians(((Integer) imageOrientation.first).intValue());
                Double.isNaN(d);
                mediaEntity.rotation = (float) (d - radians);
                if ((((Integer) imageOrientation.first).intValue() / 90) % 2 == 1) {
                    float f = mediaEntity.x;
                    float f2 = mediaEntity.width;
                    float f3 = f + (f2 / 2.0f);
                    float f4 = mediaEntity.y;
                    float f5 = mediaEntity.height;
                    float f6 = f4 + (f5 / 2.0f);
                    int i6 = this.transformedWidth;
                    int i7 = this.transformedHeight;
                    float f7 = (f2 * i6) / i7;
                    float f8 = (f5 * i7) / i6;
                    mediaEntity.width = f8;
                    mediaEntity.height = f7;
                    mediaEntity.x = f3 - (f8 / 2.0f);
                    mediaEntity.y = f6 - (f7 / 2.0f);
                }
                applyRoundRadius(mediaEntity, mediaEntity.bitmap, 0);
                return;
            }
            if (mediaEntity.bitmap != null) {
                float width = bitmap.getWidth() / mediaEntity.bitmap.getHeight();
                if (width > 1.0f) {
                    float f9 = mediaEntity.height;
                    float f10 = f9 / width;
                    mediaEntity.y += (f9 - f10) / 2.0f;
                    mediaEntity.height = f10;
                } else if (width < 1.0f) {
                    float f11 = mediaEntity.width;
                    float f12 = width * f11;
                    mediaEntity.x += (f11 - f12) / 2.0f;
                    mediaEntity.width = f12;
                }
            }
        }
    }

    private int createProgram(String str, String str2, boolean z) {
        int loadShader;
        int glCreateProgram;
        int loadShader2;
        int glCreateProgram2;
        if (z) {
            int loadShader3 = FilterShaders.loadShader(35633, str);
            if (loadShader3 == 0 || (loadShader2 = FilterShaders.loadShader(35632, str2)) == 0 || (glCreateProgram2 = GLES30.glCreateProgram()) == 0) {
                return 0;
            }
            GLES30.glAttachShader(glCreateProgram2, loadShader3);
            GLES30.glAttachShader(glCreateProgram2, loadShader2);
            GLES30.glLinkProgram(glCreateProgram2);
            int[] iArr = new int[1];
            GLES30.glGetProgramiv(glCreateProgram2, 35714, iArr, 0);
            if (iArr[0] != 1) {
                GLES30.glDeleteProgram(glCreateProgram2);
                return 0;
            }
            return glCreateProgram2;
        }
        int loadShader4 = FilterShaders.loadShader(35633, str);
        if (loadShader4 == 0 || (loadShader = FilterShaders.loadShader(35632, str2)) == 0 || (glCreateProgram = GLES20.glCreateProgram()) == 0) {
            return 0;
        }
        GLES20.glAttachShader(glCreateProgram, loadShader4);
        GLES20.glAttachShader(glCreateProgram, loadShader);
        GLES20.glLinkProgram(glCreateProgram);
        int[] iArr2 = new int[1];
        GLES20.glGetProgramiv(glCreateProgram, 35714, iArr2, 0);
        if (iArr2[0] != 1) {
            GLES20.glDeleteProgram(glCreateProgram);
            return 0;
        }
        return glCreateProgram;
    }

    public void release() {
        ArrayList<VideoEditedInfo.MediaEntity> arrayList = this.mediaEntities;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                VideoEditedInfo.MediaEntity mediaEntity = this.mediaEntities.get(i);
                long j = mediaEntity.ptr;
                if (j != 0) {
                    RLottieDrawable.destroy(j);
                }
                AnimatedFileDrawable animatedFileDrawable = mediaEntity.animatedFileDrawable;
                if (animatedFileDrawable != null) {
                    animatedFileDrawable.recycle();
                }
                View view = mediaEntity.view;
                if (view instanceof EditTextEffects) {
                    ((EditTextEffects) view).recycleEmojis();
                }
                Bitmap bitmap = mediaEntity.bitmap;
                if (bitmap != null) {
                    bitmap.recycle();
                    mediaEntity.bitmap = null;
                }
            }
        }
    }

    public void changeFragmentShader(String str, String str2, boolean z) {
        int i = this.NUM_EXTERNAL_SHADER;
        String str3 = VERTEX_SHADER_300;
        if (i >= 0 && i < this.mProgram.length) {
            int createProgram = createProgram(z ? VERTEX_SHADER_300 : VERTEX_SHADER, str, z);
            if (createProgram != 0) {
                GLES20.glDeleteProgram(this.mProgram[this.NUM_EXTERNAL_SHADER]);
                this.mProgram[this.NUM_EXTERNAL_SHADER] = createProgram;
                this.texSizeHandle = GLES20.glGetUniformLocation(createProgram, "texSize");
            }
        }
        int i2 = this.NUM_FILTER_SHADER;
        if (i2 < 0 || i2 >= this.mProgram.length) {
            return;
        }
        if (!z) {
            str3 = VERTEX_SHADER;
        }
        int createProgram2 = createProgram(str3, str2, z);
        if (createProgram2 != 0) {
            GLES20.glDeleteProgram(this.mProgram[this.NUM_FILTER_SHADER]);
            this.mProgram[this.NUM_FILTER_SHADER] = createProgram2;
        }
    }
}
