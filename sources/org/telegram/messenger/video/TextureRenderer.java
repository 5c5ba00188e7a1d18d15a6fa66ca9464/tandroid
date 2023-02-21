package org.telegram.messenger.video;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Build;
import android.text.SpannableString;
import android.view.View;
import androidx.exifinterface.media.ExifInterface;
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
import org.telegram.ui.Components.EditTextEffects;
import org.telegram.ui.Components.FilterShaders;
import org.telegram.ui.Components.Paint.PaintTypeface;
import org.telegram.ui.Components.Paint.Views.EditTextOutline;
import org.telegram.ui.Components.RLottieDrawable;
/* loaded from: classes.dex */
public class TextureRenderer {
    private static final String FRAGMENT_EXTERNAL_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
    private static final String FRAGMENT_SHADER = "precision highp float;\nvarying vec2 vTextureCoord;\nuniform sampler2D sTexture;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
    private static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
    private FloatBuffer bitmapVerticesBuffer;
    private boolean blendEnabled;
    private ArrayList<AnimatedEmojiDrawable> emojiDrawables;
    private FilterShaders filterShaders;
    private int imageOrientation;
    private String imagePath;
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
    Path path;
    private FloatBuffer renderTextureBuffer;
    private int simpleInputTexCoordHandle;
    private int simplePositionHandle;
    private int simpleShaderProgram;
    private int simpleSourceImageHandle;
    private Bitmap stickerBitmap;
    private Canvas stickerCanvas;
    private int[] stickerTexture;
    private FloatBuffer textureBuffer;
    private int transformedHeight;
    private int transformedWidth;
    private FloatBuffer verticesBuffer;
    private float videoFps;
    Paint xRefPaint;
    float[] bitmapData = {-1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f};
    private float[] mMVPMatrix = new float[16];
    private float[] mSTMatrix = new float[16];
    private float[] mSTMatrixIdentity = new float[16];
    private boolean firstFrame = true;

    public TextureRenderer(MediaController.SavedFilterState savedFilterState, String str, String str2, ArrayList<VideoEditedInfo.MediaEntity> arrayList, MediaController.CropState cropState, int i, int i2, int i3, int i4, int i5, float f, boolean z) {
        int i6;
        int i7 = i;
        int i8 = i2;
        float f2 = f;
        this.isPhoto = z;
        float[] fArr = {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("start textureRenderer w = " + i7 + " h = " + i8 + " r = " + i5 + " fps = " + f2);
            if (cropState != null) {
                FileLog.d("cropState px = " + cropState.cropPx + " py = " + cropState.cropPy + " cScale = " + cropState.cropScale + " cropRotate = " + cropState.cropRotate + " pw = " + cropState.cropPw + " ph = " + cropState.cropPh + " tw = " + cropState.transformWidth + " th = " + cropState.transformHeight + " tr = " + cropState.transformRotation + " mirror = " + cropState.mirrored);
            }
        }
        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.textureBuffer = asFloatBuffer;
        asFloatBuffer.put(fArr).position(0);
        FloatBuffer asFloatBuffer2 = ByteBuffer.allocateDirect(this.bitmapData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.bitmapVerticesBuffer = asFloatBuffer2;
        asFloatBuffer2.put(this.bitmapData).position(0);
        Matrix.setIdentityM(this.mSTMatrix, 0);
        Matrix.setIdentityM(this.mSTMatrixIdentity, 0);
        if (savedFilterState != null) {
            FilterShaders filterShaders = new FilterShaders(true);
            this.filterShaders = filterShaders;
            filterShaders.setDelegate(FilterShaders.getFilterShadersDelegate(savedFilterState));
        }
        this.transformedWidth = i7;
        this.transformedHeight = i8;
        this.originalWidth = i3;
        this.originalHeight = i4;
        this.imagePath = str;
        this.paintPath = str2;
        this.mediaEntities = arrayList;
        this.videoFps = f2 == 0.0f ? 30.0f : f2;
        int i9 = this.filterShaders != null ? 2 : 1;
        this.mProgram = new int[i9];
        this.muMVPMatrixHandle = new int[i9];
        this.muSTMatrixHandle = new int[i9];
        this.maPositionHandle = new int[i9];
        this.maTextureHandle = new int[i9];
        Matrix.setIdentityM(this.mMVPMatrix, 0);
        if (cropState != null) {
            float[] fArr2 = new float[8];
            fArr2[0] = 0.0f;
            fArr2[1] = 0.0f;
            float f3 = i7;
            fArr2[2] = f3;
            fArr2[3] = 0.0f;
            fArr2[4] = 0.0f;
            float f4 = i8;
            fArr2[5] = f4;
            fArr2[6] = f3;
            fArr2[7] = f4;
            i6 = cropState.transformRotation;
            this.transformedWidth = (int) (this.transformedWidth * cropState.cropPw);
            this.transformedHeight = (int) (this.transformedHeight * cropState.cropPh);
            double d = -cropState.cropRotate;
            Double.isNaN(d);
            float f5 = (float) (d * 0.017453292519943295d);
            int i10 = 0;
            for (int i11 = 4; i10 < i11; i11 = 4) {
                int i12 = i10 * 2;
                int i13 = i12 + 1;
                float f6 = f3;
                double d2 = fArr2[i12] - (i7 / 2);
                double d3 = f5;
                double cos = Math.cos(d3);
                Double.isNaN(d2);
                double d4 = fArr2[i13] - (i8 / 2);
                double sin = Math.sin(d3);
                Double.isNaN(d4);
                double d5 = cropState.cropPx * f6;
                Double.isNaN(d5);
                float f7 = ((float) (((cos * d2) - (sin * d4)) + d5)) * cropState.cropScale;
                double sin2 = Math.sin(d3);
                Double.isNaN(d2);
                double cos2 = Math.cos(d3);
                Double.isNaN(d4);
                double d6 = (d2 * sin2) + (d4 * cos2);
                double d7 = cropState.cropPy * f4;
                Double.isNaN(d7);
                float f8 = ((float) (d6 - d7)) * cropState.cropScale;
                fArr2[i12] = (f7 / this.transformedWidth) * 2.0f;
                fArr2[i13] = (f8 / this.transformedHeight) * 2.0f;
                i10++;
                f3 = f6;
                i7 = i;
                i8 = i2;
            }
            FloatBuffer asFloatBuffer3 = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
            this.verticesBuffer = asFloatBuffer3;
            asFloatBuffer3.put(fArr2).position(0);
        } else {
            FloatBuffer asFloatBuffer4 = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
            this.verticesBuffer = asFloatBuffer4;
            asFloatBuffer4.put(new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f}).position(0);
            i6 = 0;
        }
        float[] fArr3 = this.filterShaders != null ? i6 == 90 ? new float[]{1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f} : i6 == 180 ? new float[]{1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f} : i6 == 270 ? new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f} : new float[]{0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f} : i6 == 90 ? new float[]{1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f} : i6 == 180 ? new float[]{1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f} : i6 == 270 ? new float[]{0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f} : new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        if (cropState != null && cropState.mirrored) {
            int i14 = 0;
            for (int i15 = 4; i14 < i15; i15 = 4) {
                int i16 = i14 * 2;
                if (fArr3[i16] > 0.5f) {
                    fArr3[i16] = 0.0f;
                } else {
                    fArr3[i16] = 1.0f;
                }
                i14++;
            }
        }
        FloatBuffer asFloatBuffer5 = ByteBuffer.allocateDirect(fArr3.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.renderTextureBuffer = asFloatBuffer5;
        asFloatBuffer5.put(fArr3).position(0);
    }

    public int getTextureId() {
        return this.mTextureID;
    }

    public void drawFrame(SurfaceTexture surfaceTexture) {
        int i;
        int i2;
        float[] fArr;
        char c;
        Bitmap bitmap;
        if (this.isPhoto) {
            GLES20.glUseProgram(this.simpleShaderProgram);
            GLES20.glActiveTexture(33984);
            GLES20.glUniform1i(this.simpleSourceImageHandle, 0);
            GLES20.glEnableVertexAttribArray(this.simpleInputTexCoordHandle);
            GLES20.glVertexAttribPointer(this.simpleInputTexCoordHandle, 2, 5126, false, 8, (Buffer) this.textureBuffer);
            GLES20.glEnableVertexAttribArray(this.simplePositionHandle);
        } else {
            surfaceTexture.getTransformMatrix(this.mSTMatrix);
            if (BuildVars.LOGS_ENABLED && this.firstFrame) {
                StringBuilder sb = new StringBuilder();
                int i3 = 0;
                while (true) {
                    float[] fArr2 = this.mSTMatrix;
                    if (i3 >= fArr2.length) {
                        break;
                    }
                    sb.append(fArr2[i3]);
                    sb.append(", ");
                    i3++;
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
                int i4 = this.transformedWidth;
                if (i4 != this.originalWidth || this.transformedHeight != this.originalHeight) {
                    GLES20.glViewport(0, 0, i4, this.transformedHeight);
                }
                i = this.filterShaders.getRenderTexture(!drawBlurPass);
                fArr = this.mSTMatrixIdentity;
                i2 = 3553;
                c = 1;
            } else {
                i = this.mTextureID;
                i2 = 36197;
                fArr = this.mSTMatrix;
                c = 0;
            }
            GLES20.glUseProgram(this.mProgram[c]);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(i2, i);
            GLES20.glVertexAttribPointer(this.maPositionHandle[c], 2, 5126, false, 8, (Buffer) this.verticesBuffer);
            GLES20.glEnableVertexAttribArray(this.maPositionHandle[c]);
            GLES20.glVertexAttribPointer(this.maTextureHandle[c], 2, 5126, false, 8, (Buffer) this.renderTextureBuffer);
            GLES20.glEnableVertexAttribArray(this.maTextureHandle[c]);
            GLES20.glUniformMatrix4fv(this.muSTMatrixHandle[c], 1, false, fArr, 0);
            GLES20.glUniformMatrix4fv(this.muMVPMatrixHandle[c], 1, false, this.mMVPMatrix, 0);
            GLES20.glDrawArrays(5, 0, 4);
        }
        if (this.paintTexture != null || this.stickerTexture != null) {
            GLES20.glUseProgram(this.simpleShaderProgram);
            GLES20.glActiveTexture(33984);
            GLES20.glUniform1i(this.simpleSourceImageHandle, 0);
            GLES20.glEnableVertexAttribArray(this.simpleInputTexCoordHandle);
            GLES20.glVertexAttribPointer(this.simpleInputTexCoordHandle, 2, 5126, false, 8, (Buffer) this.textureBuffer);
            GLES20.glEnableVertexAttribArray(this.simplePositionHandle);
        }
        if (this.paintTexture != null) {
            int i5 = 0;
            while (true) {
                int[] iArr = this.paintTexture;
                if (i5 >= iArr.length) {
                    break;
                }
                drawTexture(true, iArr[i5]);
                i5++;
            }
        }
        if (this.stickerTexture != null) {
            int size = this.mediaEntities.size();
            for (int i6 = 0; i6 < size; i6++) {
                VideoEditedInfo.MediaEntity mediaEntity = this.mediaEntities.get(i6);
                long j = mediaEntity.ptr;
                if (j != 0) {
                    Bitmap bitmap2 = this.stickerBitmap;
                    RLottieDrawable.getFrame(j, (int) mediaEntity.currentFrame, bitmap2, LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS, bitmap2.getRowBytes(), true);
                    applyRoundRadius(mediaEntity, this.stickerBitmap);
                    GLES20.glBindTexture(3553, this.stickerTexture[0]);
                    GLUtils.texImage2D(3553, 0, this.stickerBitmap, 0);
                    float f = mediaEntity.currentFrame + mediaEntity.framesPerDraw;
                    mediaEntity.currentFrame = f;
                    if (f >= mediaEntity.metadata[0]) {
                        mediaEntity.currentFrame = 0.0f;
                    }
                    drawTexture(false, this.stickerTexture[0], mediaEntity.x, mediaEntity.y, mediaEntity.width, mediaEntity.height, mediaEntity.rotation, (mediaEntity.subType & 2) != 0);
                } else if (mediaEntity.animatedFileDrawable != null) {
                    float f2 = mediaEntity.currentFrame;
                    int i7 = (int) f2;
                    float f3 = f2 + mediaEntity.framesPerDraw;
                    mediaEntity.currentFrame = f3;
                    for (int i8 = (int) f3; i7 != i8; i8--) {
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
                        Bitmap bitmap3 = this.stickerBitmap;
                        if (bitmap3 != null) {
                            bitmap3.eraseColor(0);
                            this.stickerCanvas.drawBitmap(backgroundBitmap, 0.0f, 0.0f, (Paint) null);
                            applyRoundRadius(mediaEntity, this.stickerBitmap);
                            GLES20.glBindTexture(3553, this.stickerTexture[0]);
                            GLUtils.texImage2D(3553, 0, this.stickerBitmap, 0);
                            drawTexture(false, this.stickerTexture[0], mediaEntity.x, mediaEntity.y, mediaEntity.width, mediaEntity.height, mediaEntity.rotation, (mediaEntity.subType & 2) != 0);
                        }
                    }
                } else if (mediaEntity.view != null && mediaEntity.canvas != null && (bitmap = mediaEntity.bitmap) != null) {
                    bitmap.eraseColor(0);
                    float f4 = mediaEntity.currentFrame;
                    int i9 = (int) f4;
                    float f5 = f4 + mediaEntity.framesPerDraw;
                    mediaEntity.currentFrame = f5;
                    ((EditTextEffects) mediaEntity.view).incrementFrames(((int) f5) - i9);
                    mediaEntity.view.draw(mediaEntity.canvas);
                    applyRoundRadius(mediaEntity, mediaEntity.bitmap);
                    GLES20.glBindTexture(3553, this.stickerTexture[0]);
                    GLUtils.texImage2D(3553, 0, mediaEntity.bitmap, 0);
                    drawTexture(false, this.stickerTexture[0], mediaEntity.x, mediaEntity.y, mediaEntity.width, mediaEntity.height, mediaEntity.rotation, (mediaEntity.subType & 2) != 0);
                } else if (mediaEntity.bitmap != null) {
                    GLES20.glBindTexture(3553, this.stickerTexture[0]);
                    GLUtils.texImage2D(3553, 0, mediaEntity.bitmap, 0);
                    drawTexture(false, this.stickerTexture[0], mediaEntity.x, mediaEntity.y, mediaEntity.width, mediaEntity.height, mediaEntity.rotation, (mediaEntity.subType & 2) != 0);
                }
            }
        }
        GLES20.glFinish();
    }

    private void applyRoundRadius(VideoEditedInfo.MediaEntity mediaEntity, Bitmap bitmap) {
        if (bitmap == null || mediaEntity == null || mediaEntity.roundRadius == 0.0f) {
            return;
        }
        if (mediaEntity.roundRadiusCanvas == null) {
            mediaEntity.roundRadiusCanvas = new Canvas(bitmap);
        }
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

    private void drawTexture(boolean z, int i) {
        drawTexture(z, i, -10000.0f, -10000.0f, -10000.0f, -10000.0f, 0.0f, false);
    }

    private void drawTexture(boolean z, int i, float f, float f2, float f3, float f4, float f5, boolean z2) {
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
            int i2 = 0;
            for (int i3 = 4; i2 < i3; i3 = 4) {
                float[] fArr4 = this.bitmapData;
                int i4 = i2 * 2;
                int i5 = i4 + 1;
                double d = fArr4[i4] - f10;
                double d2 = f5;
                double cos = Math.cos(d2);
                Double.isNaN(d);
                double d3 = (fArr4[i5] - f14) / f13;
                double sin = Math.sin(d2);
                Double.isNaN(d3);
                fArr4[i4] = ((float) ((cos * d) - (sin * d3))) + f10;
                float[] fArr5 = this.bitmapData;
                double sin2 = Math.sin(d2);
                Double.isNaN(d);
                double cos2 = Math.cos(d2);
                Double.isNaN(d3);
                fArr5[i5] = (((float) ((d * sin2) + (d3 * cos2))) * f13) + f14;
                i2++;
            }
        }
        this.bitmapVerticesBuffer.put(this.bitmapData).position(0);
        GLES20.glVertexAttribPointer(this.simplePositionHandle, 2, 5126, false, 8, (Buffer) this.bitmapVerticesBuffer);
        if (z) {
            GLES20.glBindTexture(3553, i);
        }
        GLES20.glDrawArrays(5, 0, 4);
    }

    public void setBreakStrategy(EditTextOutline editTextOutline) {
        editTextOutline.setBreakStrategy(0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:124:0x0429, code lost:
        if (org.telegram.messenger.LocaleController.isRTL != false) goto L83;
     */
    /* JADX WARN: Removed duplicated region for block: B:170:0x0209 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x016a  */
    @SuppressLint({"WrongConstant"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void surfaceCreated() {
        String str;
        int i;
        Bitmap decodeFile;
        float max;
        int i2;
        AnimatedEmojiSpan animatedEmojiSpan;
        Typeface typeface;
        Bitmap bitmap;
        int i3 = 0;
        while (true) {
            int[] iArr = this.mProgram;
            if (i3 >= iArr.length) {
                break;
            }
            iArr[i3] = createProgram(VERTEX_SHADER, i3 == 0 ? FRAGMENT_EXTERNAL_SHADER : FRAGMENT_SHADER);
            this.maPositionHandle[i3] = GLES20.glGetAttribLocation(this.mProgram[i3], "aPosition");
            this.maTextureHandle[i3] = GLES20.glGetAttribLocation(this.mProgram[i3], "aTextureCoord");
            this.muMVPMatrixHandle[i3] = GLES20.glGetUniformLocation(this.mProgram[i3], "uMVPMatrix");
            this.muSTMatrixHandle[i3] = GLES20.glGetUniformLocation(this.mProgram[i3], "uSTMatrix");
            i3++;
        }
        int i4 = 1;
        int[] iArr2 = new int[1];
        GLES20.glGenTextures(1, iArr2, 0);
        int i5 = iArr2[0];
        this.mTextureID = i5;
        GLES20.glBindTexture(36197, i5);
        GLES20.glTexParameteri(36197, 10241, 9729);
        GLES20.glTexParameteri(36197, 10240, 9729);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        if (this.filterShaders != null || this.imagePath != null || this.paintPath != null || this.mediaEntities != null) {
            int loadShader = FilterShaders.loadShader(35633, "attribute vec4 position;attribute vec2 inputTexCoord;varying vec2 texCoord;void main() {gl_Position = position;texCoord = inputTexCoord;}");
            int loadShader2 = FilterShaders.loadShader(35632, "varying highp vec2 texCoord;uniform sampler2D sourceImage;void main() {gl_FragColor = texture2D(sourceImage, texCoord);}");
            if (loadShader != 0 && loadShader2 != 0) {
                int glCreateProgram = GLES20.glCreateProgram();
                this.simpleShaderProgram = glCreateProgram;
                GLES20.glAttachShader(glCreateProgram, loadShader);
                GLES20.glAttachShader(this.simpleShaderProgram, loadShader2);
                GLES20.glBindAttribLocation(this.simpleShaderProgram, 0, "position");
                GLES20.glBindAttribLocation(this.simpleShaderProgram, 1, "inputTexCoord");
                GLES20.glLinkProgram(this.simpleShaderProgram);
                int[] iArr3 = new int[1];
                GLES20.glGetProgramiv(this.simpleShaderProgram, 35714, iArr3, 0);
                if (iArr3[0] == 0) {
                    GLES20.glDeleteProgram(this.simpleShaderProgram);
                    this.simpleShaderProgram = 0;
                } else {
                    this.simplePositionHandle = GLES20.glGetAttribLocation(this.simpleShaderProgram, "position");
                    this.simpleInputTexCoordHandle = GLES20.glGetAttribLocation(this.simpleShaderProgram, "inputTexCoord");
                    this.simpleSourceImageHandle = GLES20.glGetUniformLocation(this.simpleShaderProgram, "sourceImage");
                }
            }
        }
        FilterShaders filterShaders = this.filterShaders;
        if (filterShaders != null) {
            filterShaders.create();
            this.filterShaders.setRenderData(null, 0, this.mTextureID, this.originalWidth, this.originalHeight);
        }
        String str2 = this.imagePath;
        int i6 = -16777216;
        int i7 = 3;
        if (str2 != null || this.paintPath != null) {
            int[] iArr4 = new int[(str2 != null ? 1 : 0) + (this.paintPath != null ? 1 : 0)];
            this.paintTexture = iArr4;
            GLES20.glGenTextures(iArr4.length, iArr4, 0);
            int i8 = 0;
            while (i8 < this.paintTexture.length) {
                try {
                    if (i8 == 0 && (str = this.imagePath) != null) {
                        try {
                            int attributeInt = new ExifInterface(str).getAttributeInt("Orientation", i4);
                            if (attributeInt == i7) {
                                i = 180;
                            } else if (attributeInt == 6) {
                                i = 90;
                            } else if (attributeInt == 8) {
                                i = 270;
                            }
                        } catch (Throwable unused) {
                        }
                        decodeFile = BitmapFactory.decodeFile(str);
                        if (decodeFile != null) {
                            if (i8 == 0 && this.imagePath != null) {
                                Bitmap createBitmap = Bitmap.createBitmap(this.transformedWidth, this.transformedHeight, Bitmap.Config.ARGB_8888);
                                createBitmap.eraseColor(i6);
                                Canvas canvas = new Canvas(createBitmap);
                                if (i != 90 && i != 270) {
                                    max = Math.max(decodeFile.getWidth() / this.transformedWidth, decodeFile.getHeight() / this.transformedHeight);
                                    android.graphics.Matrix matrix = new android.graphics.Matrix();
                                    matrix.postTranslate((-decodeFile.getWidth()) / 2, (-decodeFile.getHeight()) / 2);
                                    float f = 1.0f / max;
                                    matrix.postScale(f, f);
                                    matrix.postRotate(i);
                                    matrix.postTranslate(createBitmap.getWidth() / 2, createBitmap.getHeight() / 2);
                                    canvas.drawBitmap(decodeFile, matrix, new Paint(2));
                                    decodeFile = createBitmap;
                                }
                                max = Math.max(decodeFile.getHeight() / this.transformedWidth, decodeFile.getWidth() / this.transformedHeight);
                                android.graphics.Matrix matrix2 = new android.graphics.Matrix();
                                matrix2.postTranslate((-decodeFile.getWidth()) / 2, (-decodeFile.getHeight()) / 2);
                                float f2 = 1.0f / max;
                                matrix2.postScale(f2, f2);
                                matrix2.postRotate(i);
                                matrix2.postTranslate(createBitmap.getWidth() / 2, createBitmap.getHeight() / 2);
                                canvas.drawBitmap(decodeFile, matrix2, new Paint(2));
                                decodeFile = createBitmap;
                            }
                            GLES20.glBindTexture(3553, this.paintTexture[i8]);
                            GLES20.glTexParameteri(3553, 10241, 9729);
                            GLES20.glTexParameteri(3553, 10240, 9729);
                            GLES20.glTexParameteri(3553, 10242, 33071);
                            GLES20.glTexParameteri(3553, 10243, 33071);
                            GLUtils.texImage2D(3553, 0, decodeFile, 0);
                        }
                        i8++;
                        i4 = 1;
                        i6 = -16777216;
                        i7 = 3;
                    } else {
                        str = this.paintPath;
                    }
                    i = 0;
                    decodeFile = BitmapFactory.decodeFile(str);
                    if (decodeFile != null) {
                    }
                    i8++;
                    i4 = 1;
                    i6 = -16777216;
                    i7 = 3;
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
        }
        if (this.mediaEntities != null) {
            try {
                this.stickerBitmap = Bitmap.createBitmap(LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS, Bitmap.Config.ARGB_8888);
                int[] iArr5 = new int[1];
                this.stickerTexture = iArr5;
                GLES20.glGenTextures(1, iArr5, 0);
                GLES20.glBindTexture(3553, this.stickerTexture[0]);
                GLES20.glTexParameteri(3553, 10241, 9729);
                GLES20.glTexParameteri(3553, 10240, 9729);
                GLES20.glTexParameteri(3553, 10242, 33071);
                GLES20.glTexParameteri(3553, 10243, 33071);
                int size = this.mediaEntities.size();
                for (int i9 = 0; i9 < size; i9++) {
                    VideoEditedInfo.MediaEntity mediaEntity = this.mediaEntities.get(i9);
                    byte b = mediaEntity.type;
                    if (b == 0) {
                        byte b2 = mediaEntity.subType;
                        if ((b2 & 1) != 0) {
                            int[] iArr6 = new int[3];
                            mediaEntity.metadata = iArr6;
                            mediaEntity.ptr = RLottieDrawable.create(mediaEntity.text, null, LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS, iArr6, false, null, false, 0);
                            mediaEntity.framesPerDraw = mediaEntity.metadata[1] / this.videoFps;
                        } else if ((b2 & 4) != 0) {
                            mediaEntity.animatedFileDrawable = new AnimatedFileDrawable(new File(mediaEntity.text), true, 0L, 0, null, null, null, 0L, UserConfig.selectedAccount, true, LiteMode.FLAG_CALLS_ANIMATIONS, LiteMode.FLAG_CALLS_ANIMATIONS, null);
                            mediaEntity.framesPerDraw = this.videoFps / 30.0f;
                            mediaEntity.currentFrame = 0.0f;
                        } else {
                            if (Build.VERSION.SDK_INT >= 19) {
                                mediaEntity.bitmap = BitmapFactory.decodeFile(mediaEntity.text);
                            } else {
                                File file = new File(mediaEntity.text);
                                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                                MappedByteBuffer map = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = true;
                                Utilities.loadWebpImage(null, map, map.limit(), options, true);
                                Bitmap createBitmap2 = Bitmaps.createBitmap(options.outWidth, options.outHeight, Bitmap.Config.ARGB_8888);
                                mediaEntity.bitmap = createBitmap2;
                                Utilities.loadWebpImage(createBitmap2, map, map.limit(), null, true);
                                randomAccessFile.close();
                            }
                            if (mediaEntity.bitmap != null) {
                                float width = bitmap.getWidth() / mediaEntity.bitmap.getHeight();
                                if (width > 1.0f) {
                                    float f3 = mediaEntity.height;
                                    float f4 = f3 / width;
                                    mediaEntity.y += (f3 - f4) / 2.0f;
                                    mediaEntity.height = f4;
                                } else if (width < 1.0f) {
                                    float f5 = mediaEntity.width;
                                    float f6 = width * f5;
                                    mediaEntity.x += (f5 - f6) / 2.0f;
                                    mediaEntity.width = f6;
                                }
                            }
                        }
                    } else if (b == 1) {
                        EditTextOutline editTextOutline = new EditTextOutline(ApplicationLoader.applicationContext) { // from class: org.telegram.messenger.video.TextureRenderer.1
                            {
                                this.animatedEmojiOffsetX = AndroidUtilities.dp(8.0f);
                                this.animatedEmojiRawDraw = true;
                                this.animatedEmojiRawDrawFps = (int) TextureRenderer.this.videoFps;
                            }
                        };
                        editTextOutline.setBackgroundColor(0);
                        editTextOutline.setPadding(AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f));
                        PaintTypeface paintTypeface = mediaEntity.textTypeface;
                        if (paintTypeface != null && (typeface = paintTypeface.getTypeface()) != null) {
                            editTextOutline.setTypeface(typeface);
                        }
                        editTextOutline.setTextSize(0, mediaEntity.fontSize);
                        SpannableString spannableString = new SpannableString(mediaEntity.text);
                        Iterator<VideoEditedInfo.EmojiEntity> it = mediaEntity.entities.iterator();
                        boolean z = false;
                        while (it.hasNext()) {
                            VideoEditedInfo.EmojiEntity next = it.next();
                            if (next.document != null) {
                                animatedEmojiSpan = new AnimatedEmojiSpan(next.document, editTextOutline.getPaint().getFontMetricsInt());
                            } else {
                                animatedEmojiSpan = new AnimatedEmojiSpan(next.document_id, editTextOutline.getPaint().getFontMetricsInt());
                            }
                            animatedEmojiSpan.cacheType = 12;
                            animatedEmojiSpan.documentAbsolutePath = next.documentAbsolutePath;
                            int i10 = next.offset;
                            spannableString.setSpan(animatedEmojiSpan, i10, next.length + i10, 33);
                            z = true;
                        }
                        editTextOutline.setText(Emoji.replaceEmoji(spannableString, editTextOutline.getPaint().getFontMetricsInt(), (int) (editTextOutline.getTextSize() * 0.8f), false));
                        editTextOutline.setTextColor(mediaEntity.color);
                        int i11 = mediaEntity.textAlign;
                        editTextOutline.setGravity(i11 != 1 ? i11 != 2 ? 19 : 21 : 17);
                        int i12 = Build.VERSION.SDK_INT;
                        if (i12 >= 17) {
                            int i13 = mediaEntity.textAlign;
                            if (i13 == 1) {
                                i2 = 4;
                            } else if (i13 == 2) {
                                if (LocaleController.isRTL) {
                                    i2 = 2;
                                }
                                i2 = 3;
                            }
                            editTextOutline.setTextAlignment(i2);
                        }
                        editTextOutline.setHorizontallyScrolling(false);
                        editTextOutline.setImeOptions(268435456);
                        editTextOutline.setFocusableInTouchMode(true);
                        editTextOutline.setInputType(editTextOutline.getInputType() | 16384);
                        if (i12 >= 23) {
                            setBreakStrategy(editTextOutline);
                        }
                        byte b3 = mediaEntity.subType;
                        if ((b3 & 1) != 0) {
                            editTextOutline.setTextColor(-1);
                            editTextOutline.setStrokeColor(mediaEntity.color);
                            editTextOutline.setFrameColor(0);
                            editTextOutline.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
                        } else if ((b3 & 4) != 0) {
                            editTextOutline.setTextColor(-16777216);
                            editTextOutline.setStrokeColor(0);
                            editTextOutline.setFrameColor(mediaEntity.color);
                            editTextOutline.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
                        } else {
                            editTextOutline.setTextColor(mediaEntity.color);
                            editTextOutline.setStrokeColor(0);
                            editTextOutline.setFrameColor(0);
                            editTextOutline.setShadowLayer(5.0f, 0.0f, 1.0f, 1711276032);
                        }
                        editTextOutline.measure(View.MeasureSpec.makeMeasureSpec(mediaEntity.viewWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(mediaEntity.viewHeight, 1073741824));
                        editTextOutline.layout(0, 0, mediaEntity.viewWidth, mediaEntity.viewHeight);
                        mediaEntity.bitmap = Bitmap.createBitmap(mediaEntity.viewWidth, mediaEntity.viewHeight, Bitmap.Config.ARGB_8888);
                        Canvas canvas2 = new Canvas(mediaEntity.bitmap);
                        editTextOutline.draw(canvas2);
                        if (z) {
                            mediaEntity.view = editTextOutline;
                            mediaEntity.canvas = canvas2;
                            mediaEntity.framesPerDraw = this.videoFps / 30.0f;
                            mediaEntity.currentFrame = 0.0f;
                        }
                    }
                }
            } catch (Throwable th2) {
                FileLog.e(th2);
            }
        }
    }

    private int createProgram(String str, String str2) {
        int loadShader;
        int glCreateProgram;
        int loadShader2 = FilterShaders.loadShader(35633, str);
        if (loadShader2 == 0 || (loadShader = FilterShaders.loadShader(35632, str2)) == 0 || (glCreateProgram = GLES20.glCreateProgram()) == 0) {
            return 0;
        }
        GLES20.glAttachShader(glCreateProgram, loadShader2);
        GLES20.glAttachShader(glCreateProgram, loadShader);
        GLES20.glLinkProgram(glCreateProgram);
        int[] iArr = new int[1];
        GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
        if (iArr[0] != 1) {
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
            }
        }
    }

    public void changeFragmentShader(String str, String str2) {
        GLES20.glDeleteProgram(this.mProgram[0]);
        this.mProgram[0] = createProgram(VERTEX_SHADER, str);
        int[] iArr = this.mProgram;
        if (iArr.length > 1) {
            iArr[1] = createProgram(VERTEX_SHADER, str2);
        }
    }
}
