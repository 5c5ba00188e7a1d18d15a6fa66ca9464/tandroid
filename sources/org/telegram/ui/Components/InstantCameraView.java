package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTimestamp;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.net.Uri;
import android.opengl.EGL14;
import android.opengl.EGLExt;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Property;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.graphics.ColorUtils;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.AutoDeleteMediaTask;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.camera.Camera2Session;
import org.telegram.messenger.camera.CameraController;
import org.telegram.messenger.camera.CameraInfo;
import org.telegram.messenger.camera.CameraSession;
import org.telegram.messenger.video.MP4Builder;
import org.telegram.messenger.video.Mp4Movie;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC$InputEncryptedFile;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.InstantCameraView;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.ui.Components.voip.CellFlickerDrawable;
import org.telegram.ui.Stories.recorder.DualCameraView;
import org.telegram.ui.Stories.recorder.FlashViews;
import org.telegram.ui.Stories.recorder.StoryEntry;
import org.webrtc.EglBase;
import org.webrtc.MediaStreamTrack;
/* loaded from: classes3.dex */
public class InstantCameraView extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private static int A;
    private static final int[] ALLOW_BIG_CAMERA_WHITELIST = {285904780, -1394191079};
    public boolean WRITE_TO_FILE_IN_BACKGROUND;
    private boolean allowSendingWhileRecording;
    private float animationTranslationY;
    private AnimatorSet animatorSet;
    private org.telegram.messenger.camera.Size aspectRatio;
    private BlurBehindDrawable blurBehindDrawable;
    private boolean bothCameras;
    private Camera2Session camera2SessionCurrent;
    private Camera2Session[] camera2Sessions;
    private InstantViewCameraContainer cameraContainer;
    private File cameraFile;
    private volatile boolean cameraReady;
    private CameraSession cameraSession;
    private final int[] cameraTexture;
    private float cameraTextureAlpha;
    private volatile boolean cameraTextureAvailable;
    private CameraGLThread cameraThread;
    private boolean cancelled;
    private int currentAccount;
    private Delegate delegate;
    public boolean drawBlur;
    private TLRPC$InputEncryptedFile encryptedFile;
    private TLRPC$InputFile file;
    ValueAnimator finishZoomTransition;
    private Bitmap firstFrameThumb;
    private final FlashViews.ImageViewInvertable flashButton;
    private RLottieDrawable flashOffDrawable;
    private RLottieDrawable flashOnDrawable;
    private final FlashViews flashViews;
    private boolean flashing;
    private boolean flipAnimationInProgress;
    private boolean frontFlashing;
    private boolean isFrontface;
    boolean isInPinchToZoomTouchMode;
    private boolean isMessageTransition;
    private boolean isSecretChat;
    private byte[] iv;
    private byte[] key;
    private Bitmap lastBitmap;
    private final float[] mMVPMatrix;
    private final float[] mSTMatrix;
    boolean maybePinchToZoomTouchMode;
    private final float[] moldSTMatrix;
    private AnimatorSet muteAnimation;
    private ImageView muteImageView;
    private boolean needDrawFlickerStub;
    private final int[] oldCameraTexture;
    private org.telegram.messenger.camera.Size oldTexturePreviewSize;
    private FloatBuffer oldTextureTextureBuffer;
    public boolean opened;
    private Paint paint;
    private float panTranslationY;
    private View parentView;
    private org.telegram.messenger.camera.Size pictureSize;
    float pinchScale;
    float pinchStartDistance;
    private int pointerId1;
    private int pointerId2;
    private final int[] position;
    private File previewFile;
    private org.telegram.messenger.camera.Size[] previewSize;
    private float progress;
    private Timer progressTimer;
    private long recordPlusTime;
    private long recordStartTime;
    private long recordedTime;
    private boolean recording;
    private int recordingGuid;
    private RectF rect;
    private final Theme.ResourcesProvider resourcesProvider;
    private float scaleX;
    private float scaleY;
    private CameraInfo selectedCamera;
    private boolean setVisibilityFromPause;
    private long size;
    private volatile int surfaceIndex;
    private final FlashViews.ImageViewInvertable switchCameraButton;
    private RLottieDrawable switchCameraDrawable;
    private FloatBuffer textureBuffer;
    private BackupImageView textureOverlayView;
    private TextureView textureView;
    private int textureViewSize;
    private boolean updateTextureViewSize;
    private final boolean useCamera2;
    private FloatBuffer vertexBuffer;
    private VideoEditedInfo videoEditedInfo;
    private VideoRecorder videoEncoder;
    private VideoPlayer videoPlayer;
    private Boolean wasFlashing;

    /* loaded from: classes3.dex */
    public class 11 extends TimerTask {
        11() {
            InstantCameraView.this = r1;
        }

        public /* synthetic */ void lambda$run$0() {
            try {
                if (InstantCameraView.this.videoPlayer == null || InstantCameraView.this.videoEditedInfo == null) {
                    return;
                }
                if (InstantCameraView.this.videoEditedInfo.endTime <= 0 || InstantCameraView.this.videoPlayer.getCurrentPosition() < InstantCameraView.this.videoEditedInfo.endTime) {
                    return;
                }
                InstantCameraView.this.videoPlayer.seekTo(InstantCameraView.this.videoEditedInfo.startTime > 0 ? InstantCameraView.this.videoEditedInfo.startTime : 0L);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$11$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    InstantCameraView.11.this.lambda$run$0();
                }
            });
        }
    }

    /* loaded from: classes3.dex */
    public static class AudioBufferInfo {
        public boolean last;
        public int lastWroteBuffer;
        public int results;
        public ByteBuffer[] buffer = new ByteBuffer[10];
        public long[] offset = new long[10];
        public int[] read = new int[10];

        public AudioBufferInfo() {
            for (int i = 0; i < 10; i++) {
                this.buffer[i] = ByteBuffer.allocateDirect(2048);
                this.buffer[i].order(ByteOrder.nativeOrder());
            }
        }
    }

    /* loaded from: classes3.dex */
    public class CameraGLThread extends DispatchQueue {
        private final int DO_FLIP;
        private final int DO_REINIT_MESSAGE;
        private final int DO_RENDER_MESSAGE;
        private final int DO_SETSESSION_MESSAGE;
        private final int DO_SHUTDOWN_MESSAGE;
        private Integer cameraId;
        private final SurfaceTexture[] cameraSurface;
        private Object currentSession;
        private int drawProgram;
        private EGL10 egl10;
        private EGLContext eglContext;
        private EGLDisplay eglDisplay;
        private EGLSurface eglSurface;
        private boolean initied;
        private int positionHandle;
        private boolean recording;
        private int surfaceHeight;
        private SurfaceTexture surfaceTexture;
        private int surfaceWidth;
        private int textureHandle;
        private int textureMatrixHandle;
        private int vertexMatrixHandle;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public CameraGLThread(SurfaceTexture surfaceTexture, int i, int i2) {
            super("CameraGLThread");
            InstantCameraView.this = r3;
            this.cameraSurface = new SurfaceTexture[2];
            this.DO_RENDER_MESSAGE = 0;
            this.DO_SHUTDOWN_MESSAGE = 1;
            this.DO_REINIT_MESSAGE = 2;
            this.DO_SETSESSION_MESSAGE = 3;
            this.DO_FLIP = 4;
            this.cameraId = 0;
            this.surfaceTexture = surfaceTexture;
            this.surfaceWidth = i;
            this.surfaceHeight = i2;
        }

        private boolean initGL() {
            String str;
            StringBuilder sb;
            String str2;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("InstantCamera start init gl");
            }
            EGL10 egl10 = (EGL10) EGLContext.getEGL();
            this.egl10 = egl10;
            EGLDisplay eglGetDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            this.eglDisplay = eglGetDisplay;
            if (eglGetDisplay == EGL10.EGL_NO_DISPLAY) {
                if (BuildVars.LOGS_ENABLED) {
                    sb = new StringBuilder();
                    str2 = "InstantCamera eglGetDisplay failed ";
                    sb.append(str2);
                    sb.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    str = sb.toString();
                }
                finish();
                return false;
            } else if (!this.egl10.eglInitialize(eglGetDisplay, new int[2])) {
                if (BuildVars.LOGS_ENABLED) {
                    sb = new StringBuilder();
                    str2 = "InstantCamera eglInitialize failed ";
                    sb.append(str2);
                    sb.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    str = sb.toString();
                }
                finish();
                return false;
            } else {
                int[] iArr = new int[1];
                EGLConfig[] eGLConfigArr = new EGLConfig[1];
                if (!this.egl10.eglChooseConfig(this.eglDisplay, new int[]{12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 0, 12325, 0, 12326, 0, 12344}, eGLConfigArr, 1, iArr)) {
                    if (BuildVars.LOGS_ENABLED) {
                        sb = new StringBuilder();
                        str2 = "InstantCamera eglChooseConfig failed ";
                        sb.append(str2);
                        sb.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                        str = sb.toString();
                    }
                    finish();
                    return false;
                } else if (iArr[0] <= 0) {
                    if (BuildVars.LOGS_ENABLED) {
                        str = "InstantCamera eglConfig not initialized";
                    }
                    finish();
                    return false;
                } else {
                    EGLConfig eGLConfig = eGLConfigArr[0];
                    EGLContext eglCreateContext = this.egl10.eglCreateContext(this.eglDisplay, eGLConfig, EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
                    this.eglContext = eglCreateContext;
                    if (eglCreateContext == null) {
                        if (BuildVars.LOGS_ENABLED) {
                            sb = new StringBuilder();
                            str2 = "InstantCamera eglCreateContext failed ";
                            sb.append(str2);
                            sb.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                            str = sb.toString();
                        }
                        finish();
                        return false;
                    }
                    SurfaceTexture surfaceTexture = this.surfaceTexture;
                    if (surfaceTexture instanceof SurfaceTexture) {
                        EGLSurface eglCreateWindowSurface = this.egl10.eglCreateWindowSurface(this.eglDisplay, eGLConfig, surfaceTexture, null);
                        this.eglSurface = eglCreateWindowSurface;
                        if (eglCreateWindowSurface == null || eglCreateWindowSurface == EGL10.EGL_NO_SURFACE) {
                            if (BuildVars.LOGS_ENABLED) {
                                sb = new StringBuilder();
                                str2 = "InstantCamera createWindowSurface failed ";
                                sb.append(str2);
                                sb.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                                str = sb.toString();
                            }
                        } else if (this.egl10.eglMakeCurrent(this.eglDisplay, eglCreateWindowSurface, eglCreateWindowSurface, this.eglContext)) {
                            updateScale();
                            float f = (1.0f / InstantCameraView.this.scaleX) / 2.0f;
                            float f2 = (1.0f / InstantCameraView.this.scaleY) / 2.0f;
                            float[] fArr = {-1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f};
                            float f3 = 0.5f - f;
                            float f4 = 0.5f - f2;
                            float f5 = f + 0.5f;
                            float f6 = f2 + 0.5f;
                            float[] fArr2 = {f3, f4, f5, f4, f3, f6, f5, f6};
                            if (InstantCameraView.this.videoEncoder == null) {
                                InstantCameraView instantCameraView = InstantCameraView.this;
                                instantCameraView.videoEncoder = new VideoRecorder();
                            }
                            InstantCameraView.this.vertexBuffer = ByteBuffer.allocateDirect(48).order(ByteOrder.nativeOrder()).asFloatBuffer();
                            InstantCameraView.this.vertexBuffer.put(fArr).position(0);
                            InstantCameraView.this.textureBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
                            InstantCameraView.this.textureBuffer.put(fArr2).position(0);
                            Matrix.setIdentityM(InstantCameraView.this.mSTMatrix, 0);
                            int loadShader = InstantCameraView.this.loadShader(35633, "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n   gl_Position = uMVPMatrix * aPosition;\n   vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n");
                            int loadShader2 = InstantCameraView.this.loadShader(35632, "#extension GL_OES_EGL_image_external : require\nprecision lowp float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n   gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n");
                            if (loadShader != 0 && loadShader2 != 0) {
                                int glCreateProgram = GLES20.glCreateProgram();
                                this.drawProgram = glCreateProgram;
                                GLES20.glAttachShader(glCreateProgram, loadShader);
                                GLES20.glAttachShader(this.drawProgram, loadShader2);
                                GLES20.glLinkProgram(this.drawProgram);
                                int[] iArr2 = new int[1];
                                GLES20.glGetProgramiv(this.drawProgram, 35714, iArr2, 0);
                                if (iArr2[0] == 0) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.e("InstantCamera failed link shader");
                                    }
                                    GLES20.glDeleteProgram(this.drawProgram);
                                    this.drawProgram = 0;
                                } else {
                                    this.positionHandle = GLES20.glGetAttribLocation(this.drawProgram, "aPosition");
                                    this.textureHandle = GLES20.glGetAttribLocation(this.drawProgram, "aTextureCoord");
                                    this.vertexMatrixHandle = GLES20.glGetUniformLocation(this.drawProgram, "uMVPMatrix");
                                    this.textureMatrixHandle = GLES20.glGetUniformLocation(this.drawProgram, "uSTMatrix");
                                }
                                Matrix.setIdentityM(InstantCameraView.this.mMVPMatrix, 0);
                                GLES20.glGenTextures(2, InstantCameraView.this.cameraTexture, 0);
                                for (final int i = 0; i < 2; i++) {
                                    GLES20.glBindTexture(36197, InstantCameraView.this.cameraTexture[i]);
                                    GLES20.glTexParameteri(36197, 10241, 9729);
                                    GLES20.glTexParameteri(36197, 10240, 9729);
                                    GLES20.glTexParameteri(36197, 10242, 33071);
                                    GLES20.glTexParameteri(36197, 10243, 33071);
                                    this.cameraSurface[i] = new SurfaceTexture(InstantCameraView.this.cameraTexture[i]);
                                    this.cameraSurface[i].setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() { // from class: org.telegram.ui.Components.InstantCameraView$CameraGLThread$$ExternalSyntheticLambda1
                                        @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
                                        public final void onFrameAvailable(SurfaceTexture surfaceTexture2) {
                                            InstantCameraView.CameraGLThread.this.lambda$initGL$0(i, surfaceTexture2);
                                        }
                                    });
                                    InstantCameraView.this.createCamera(i, this.cameraSurface[i]);
                                }
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.e("InstantCamera gl initied");
                                }
                                return true;
                            } else if (BuildVars.LOGS_ENABLED) {
                                str = "InstantCamera failed creating shader";
                            }
                        } else if (BuildVars.LOGS_ENABLED) {
                            sb = new StringBuilder();
                            str2 = "InstantCamera eglMakeCurrent failed ";
                            sb.append(str2);
                            sb.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                            str = sb.toString();
                        }
                    }
                    finish();
                    return false;
                }
            }
            FileLog.e(str);
            finish();
            return false;
        }

        public /* synthetic */ void lambda$handleMessage$3(SurfaceTexture surfaceTexture) {
            requestRender(true, false);
        }

        public /* synthetic */ void lambda$initGL$0(int i, SurfaceTexture surfaceTexture) {
            InstantCameraView.this.cameraTextureAvailable = true;
            requestRender(i == 0, i == 1);
        }

        public /* synthetic */ void lambda$onDraw$1() {
            InstantCameraView.this.textureOverlayView.animate().setDuration(120L).alpha(0.0f).setInterpolator(new DecelerateInterpolator()).start();
        }

        public /* synthetic */ void lambda$onDraw$2() {
            if (InstantCameraView.this.textureView == null) {
                return;
            }
            if (InstantCameraView.this.firstFrameThumb != null) {
                InstantCameraView.this.firstFrameThumb.recycle();
                InstantCameraView.this.firstFrameThumb = null;
            }
            InstantCameraView instantCameraView = InstantCameraView.this;
            instantCameraView.firstFrameThumb = instantCameraView.textureView.getBitmap();
        }

        private void onDraw(Integer num, boolean z, boolean z2) {
            boolean z3;
            if (this.initied) {
                if (!this.eglContext.equals(this.egl10.eglGetCurrentContext()) || !this.eglSurface.equals(this.egl10.eglGetCurrentSurface(12377))) {
                    EGL10 egl10 = this.egl10;
                    EGLDisplay eGLDisplay = this.eglDisplay;
                    EGLSurface eGLSurface = this.eglSurface;
                    if (!egl10.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.eglContext)) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.e("eglMakeCurrent failed " + GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                            return;
                        }
                        return;
                    }
                }
                if (z) {
                    this.cameraSurface[0].updateTexImage();
                }
                if (z2) {
                    this.cameraSurface[1].updateTexImage();
                }
                if (this.recording) {
                    z3 = false;
                } else {
                    if (InstantCameraView.this.videoEncoder == null) {
                        InstantCameraView instantCameraView = InstantCameraView.this;
                        instantCameraView.videoEncoder = new VideoRecorder();
                    }
                    if (InstantCameraView.this.videoEncoder.started) {
                        if (!InstantCameraView.this.cameraReady) {
                            InstantCameraView.this.cameraReady = true;
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$CameraGLThread$$ExternalSyntheticLambda2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    InstantCameraView.CameraGLThread.this.lambda$onDraw$1();
                                }
                            });
                        }
                        z3 = false;
                    } else {
                        z3 = true;
                    }
                    InstantCameraView.this.videoEncoder.startRecording(InstantCameraView.this.cameraFile, EGL14.eglGetCurrentContext());
                    Object obj = this.currentSession;
                    int currentOrientation = obj instanceof CameraSession ? ((CameraSession) obj).getCurrentOrientation() : obj instanceof Camera2Session ? ((Camera2Session) obj).getCurrentOrientation() : 0;
                    if (currentOrientation == 90 || currentOrientation == 270) {
                        float f = InstantCameraView.this.scaleX;
                        InstantCameraView instantCameraView2 = InstantCameraView.this;
                        instantCameraView2.scaleX = instantCameraView2.scaleY;
                        InstantCameraView.this.scaleY = f;
                    }
                    this.recording = true;
                    InstantCameraView.this.updateFlash();
                }
                if (InstantCameraView.this.videoEncoder != null && ((InstantCameraView.this.surfaceIndex == 0 && z) || (InstantCameraView.this.surfaceIndex == 1 && z2))) {
                    InstantCameraView.this.videoEncoder.frameAvailable(this.cameraSurface[InstantCameraView.this.surfaceIndex], Integer.valueOf(InstantCameraView.this.bothCameras ? InstantCameraView.this.surfaceIndex : num.intValue()), System.nanoTime());
                }
                this.cameraSurface[InstantCameraView.this.surfaceIndex].getTransformMatrix(InstantCameraView.this.mSTMatrix);
                GLES20.glUseProgram(this.drawProgram);
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(36197, InstantCameraView.this.cameraTexture[InstantCameraView.this.surfaceIndex]);
                GLES20.glVertexAttribPointer(this.positionHandle, 3, 5126, false, 12, (Buffer) InstantCameraView.this.vertexBuffer);
                GLES20.glEnableVertexAttribArray(this.positionHandle);
                GLES20.glVertexAttribPointer(this.textureHandle, 2, 5126, false, 8, (Buffer) InstantCameraView.this.textureBuffer);
                GLES20.glEnableVertexAttribArray(this.textureHandle);
                GLES20.glUniformMatrix4fv(this.textureMatrixHandle, 1, false, InstantCameraView.this.mSTMatrix, 0);
                GLES20.glUniformMatrix4fv(this.vertexMatrixHandle, 1, false, InstantCameraView.this.mMVPMatrix, 0);
                GLES20.glDrawArrays(5, 0, 4);
                GLES20.glDisableVertexAttribArray(this.positionHandle);
                GLES20.glDisableVertexAttribArray(this.textureHandle);
                GLES20.glBindTexture(36197, 0);
                GLES20.glUseProgram(0);
                this.egl10.eglSwapBuffers(this.eglDisplay, this.eglSurface);
                if (z3) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$CameraGLThread$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            InstantCameraView.CameraGLThread.this.lambda$onDraw$2();
                        }
                    });
                }
            }
        }

        public void updateScale() {
            if (InstantCameraView.this.previewSize[InstantCameraView.this.surfaceIndex] != null) {
                int width = InstantCameraView.this.previewSize[InstantCameraView.this.surfaceIndex].getWidth();
                int height = InstantCameraView.this.previewSize[InstantCameraView.this.surfaceIndex].getHeight();
                float min = this.surfaceWidth / Math.min(width, height);
                int i = (int) (width * min);
                int i2 = (int) (height * min);
                if (i == i2) {
                    InstantCameraView.this.scaleX = 1.0f;
                } else if (i > i2) {
                    InstantCameraView.this.scaleX = 1.0f;
                    InstantCameraView.this.scaleY = i / this.surfaceHeight;
                    FileLog.d("InstantCamera camera scaleX = " + InstantCameraView.this.scaleX + " scaleY = " + InstantCameraView.this.scaleY);
                } else {
                    InstantCameraView.this.scaleX = i2 / this.surfaceWidth;
                }
                InstantCameraView.this.scaleY = 1.0f;
                FileLog.d("InstantCamera camera scaleX = " + InstantCameraView.this.scaleX + " scaleY = " + InstantCameraView.this.scaleY);
            }
        }

        public void finish() {
            EGLContext eGLContext;
            if (this.cameraSurface != null) {
                for (int i = 0; i < 2; i++) {
                    SurfaceTexture surfaceTexture = this.cameraSurface[i];
                    if (surfaceTexture != null) {
                        surfaceTexture.release();
                        this.cameraSurface[i] = null;
                    }
                }
            }
            InstantCameraView.this.cameraTextureAvailable = false;
            if (this.eglSurface != null && (eGLContext = this.eglContext) != null) {
                if (!eGLContext.equals(this.egl10.eglGetCurrentContext()) || !this.eglSurface.equals(this.egl10.eglGetCurrentSurface(12377))) {
                    EGL10 egl10 = this.egl10;
                    EGLDisplay eGLDisplay = this.eglDisplay;
                    EGLSurface eGLSurface = this.eglSurface;
                    egl10.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.eglContext);
                }
                if (InstantCameraView.this.cameraTexture != null && InstantCameraView.this.cameraTexture[0] != Integer.MIN_VALUE) {
                    GLES20.glDeleteTextures(1, InstantCameraView.this.cameraTexture, 0);
                    InstantCameraView.this.cameraTexture[0] = Integer.MIN_VALUE;
                }
                if (InstantCameraView.this.cameraTexture != null && InstantCameraView.this.cameraTexture[1] != Integer.MIN_VALUE) {
                    GLES20.glDeleteTextures(1, InstantCameraView.this.cameraTexture, 1);
                    InstantCameraView.this.cameraTexture[1] = Integer.MIN_VALUE;
                }
            }
            if (this.eglSurface != null) {
                EGL10 egl102 = this.egl10;
                EGLDisplay eGLDisplay2 = this.eglDisplay;
                EGLSurface eGLSurface2 = EGL10.EGL_NO_SURFACE;
                egl102.eglMakeCurrent(eGLDisplay2, eGLSurface2, eGLSurface2, EGL10.EGL_NO_CONTEXT);
                this.egl10.eglDestroySurface(this.eglDisplay, this.eglSurface);
                this.eglSurface = null;
            }
            EGLContext eGLContext2 = this.eglContext;
            if (eGLContext2 != null) {
                this.egl10.eglDestroyContext(this.eglDisplay, eGLContext2);
                this.eglContext = null;
            }
            EGLDisplay eGLDisplay3 = this.eglDisplay;
            if (eGLDisplay3 != null) {
                this.egl10.eglTerminate(eGLDisplay3);
                this.eglDisplay = null;
            }
        }

        public void flipSurfaces() {
            Handler handler = getHandler();
            if (handler != null) {
                sendMessage(handler.obtainMessage(4), 0);
                requestRender(true, true);
            }
        }

        @Override // org.telegram.messenger.DispatchQueue
        public void handleMessage(Message message) {
            FloatBuffer put;
            int i = message.what;
            if (i == 0) {
                Integer valueOf = Integer.valueOf(message.arg1);
                int i2 = message.arg2;
                onDraw(valueOf, (i2 & 1) != 0, (i2 & 2) != 0);
                return;
            }
            if (i == 1) {
                finish();
                if (this.recording) {
                    Object obj = message.obj;
                    if ((!(obj instanceof SendOptions) || ((SendOptions) obj).ttl != -2) && InstantCameraView.this.videoEncoder != null) {
                        VideoRecorder videoRecorder = InstantCameraView.this.videoEncoder;
                        int i3 = message.arg1;
                        Object obj2 = message.obj;
                        videoRecorder.stopRecording(i3, obj2 instanceof SendOptions ? (SendOptions) obj2 : null);
                    }
                }
                Looper myLooper = Looper.myLooper();
                if (myLooper != null) {
                    myLooper.quit();
                    return;
                }
                return;
            }
            if (i == 2) {
                EGL10 egl10 = this.egl10;
                EGLDisplay eGLDisplay = this.eglDisplay;
                EGLSurface eGLSurface = this.eglSurface;
                if (!egl10.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.eglContext)) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("InstantCamera eglMakeCurrent failed " + GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                        return;
                    }
                    return;
                }
                SurfaceTexture surfaceTexture = this.cameraSurface[0];
                if (surfaceTexture != null) {
                    surfaceTexture.getTransformMatrix(InstantCameraView.this.moldSTMatrix);
                    this.cameraSurface[0].setOnFrameAvailableListener(null);
                    this.cameraSurface[0].release();
                    InstantCameraView.this.oldCameraTexture[0] = InstantCameraView.this.cameraTexture[0];
                    InstantCameraView.this.cameraTextureAlpha = 0.0f;
                    InstantCameraView.this.cameraTexture[0] = 0;
                    InstantCameraView instantCameraView = InstantCameraView.this;
                    instantCameraView.oldTextureTextureBuffer = instantCameraView.textureBuffer.duplicate();
                    InstantCameraView instantCameraView2 = InstantCameraView.this;
                    instantCameraView2.oldTexturePreviewSize = instantCameraView2.previewSize[0];
                }
                this.cameraId = Integer.valueOf(this.cameraId.intValue() + 1);
                InstantCameraView.this.cameraReady = false;
                GLES20.glGenTextures(1, InstantCameraView.this.cameraTexture, 0);
                GLES20.glBindTexture(36197, InstantCameraView.this.cameraTexture[0]);
                GLES20.glTexParameteri(36197, 10241, 9729);
                GLES20.glTexParameteri(36197, 10240, 9729);
                GLES20.glTexParameteri(36197, 10242, 33071);
                GLES20.glTexParameteri(36197, 10243, 33071);
                this.cameraSurface[0] = new SurfaceTexture(InstantCameraView.this.cameraTexture[0]);
                this.cameraSurface[0].setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() { // from class: org.telegram.ui.Components.InstantCameraView$CameraGLThread$$ExternalSyntheticLambda0
                    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
                    public final void onFrameAvailable(SurfaceTexture surfaceTexture2) {
                        InstantCameraView.CameraGLThread.this.lambda$handleMessage$3(surfaceTexture2);
                    }
                });
                InstantCameraView.this.createCamera(0, this.cameraSurface[0]);
                updateScale();
                float f = (1.0f / InstantCameraView.this.scaleX) / 2.0f;
                float f2 = (1.0f / InstantCameraView.this.scaleY) / 2.0f;
                float f3 = 0.5f - f;
                float f4 = 0.5f - f2;
                float f5 = f + 0.5f;
                float f6 = f2 + 0.5f;
                InstantCameraView.this.textureBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
                put = InstantCameraView.this.textureBuffer.put(new float[]{f3, f4, f5, f4, f3, f6, f5, f6});
            } else if (i == 3) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("InstantCamera set gl renderer session");
                }
                Object obj3 = message.obj;
                Object obj4 = this.currentSession;
                if (obj4 != obj3) {
                    this.currentSession = obj3;
                    return;
                }
                int worldAngle = obj4 instanceof CameraSession ? ((CameraSession) obj4).getWorldAngle() : obj4 instanceof Camera2Session ? ((Camera2Session) obj4).getWorldAngle() : 0;
                Matrix.setIdentityM(InstantCameraView.this.mMVPMatrix, 0);
                if (worldAngle != 0) {
                    Matrix.rotateM(InstantCameraView.this.mMVPMatrix, 0, worldAngle, 0.0f, 0.0f, 1.0f);
                    return;
                }
                return;
            } else if (i != 4) {
                return;
            } else {
                InstantCameraView instantCameraView3 = InstantCameraView.this;
                instantCameraView3.surfaceIndex = 1 - instantCameraView3.surfaceIndex;
                updateScale();
                float f7 = (1.0f / InstantCameraView.this.scaleX) / 2.0f;
                float f8 = (1.0f / InstantCameraView.this.scaleY) / 2.0f;
                float f9 = 0.5f - f7;
                float f10 = 0.5f - f8;
                float f11 = f7 + 0.5f;
                float f12 = f8 + 0.5f;
                InstantCameraView.this.textureBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
                put = InstantCameraView.this.textureBuffer.put(new float[]{f9, f10, f11, f10, f9, f12, f11, f12});
            }
            put.position(0);
        }

        public void reinitForNewCamera() {
            Handler handler = getHandler();
            if (handler != null) {
                sendMessage(handler.obtainMessage(2), 0);
            }
        }

        public void requestRender(boolean z, boolean z2) {
            Handler handler = getHandler();
            if (handler != null) {
                sendMessage(handler.obtainMessage(0, this.cameraId.intValue(), (z ? 1 : 0) + (z2 ? 2 : 0)), 0);
            }
        }

        @Override // org.telegram.messenger.DispatchQueue, java.lang.Thread, java.lang.Runnable
        public void run() {
            this.initied = initGL();
            super.run();
        }

        public void setCurrentSession(Camera2Session camera2Session) {
            Handler handler = getHandler();
            if (handler != null) {
                sendMessage(handler.obtainMessage(3, camera2Session), 0);
            }
        }

        public void setCurrentSession(CameraSession cameraSession) {
            Handler handler = getHandler();
            if (handler != null) {
                sendMessage(handler.obtainMessage(3, cameraSession), 0);
            }
        }

        public void shutdown(int i, boolean z, int i2, int i3, long j) {
            Handler handler = getHandler();
            if (handler != null) {
                sendMessage(handler.obtainMessage(1, i, 0, new SendOptions(z, i2, i3, j)), 0);
            }
        }
    }

    /* loaded from: classes3.dex */
    public interface Delegate {

        /* loaded from: classes3.dex */
        public abstract /* synthetic */ class -CC {
            public static boolean $default$isInScheduleMode(Delegate delegate) {
                return false;
            }

            public static boolean $default$isSecretChat(Delegate delegate) {
                return false;
            }
        }

        int getClassGuid();

        long getDialogId();

        View getFragmentView();

        Activity getParentActivity();

        boolean isInScheduleMode();

        boolean isSecretChat();

        void sendMedia(MediaController.PhotoEntry photoEntry, VideoEditedInfo videoEditedInfo, boolean z, int i, boolean z2);
    }

    /* loaded from: classes3.dex */
    public static class EncoderHandler extends Handler {
        private WeakReference mWeakEncoder;

        public EncoderHandler(VideoRecorder videoRecorder) {
            this.mWeakEncoder = new WeakReference(videoRecorder);
        }

        public void exit() {
            Looper.myLooper().quit();
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            VideoRecorder videoRecorder = (VideoRecorder) this.mWeakEncoder.get();
            if (videoRecorder == null) {
                return;
            }
            boolean z = true;
            if (i == 0) {
                try {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.e("InstantCamera start encoder");
                    }
                    if (message.arg1 != 1) {
                        z = false;
                    }
                    videoRecorder.prepareEncoder(z);
                } catch (Exception e) {
                    FileLog.e(e);
                    videoRecorder.handleStopRecording(0, null);
                    Looper.myLooper().quit();
                }
            } else if (i == 1) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("InstantCamera stop encoder");
                }
                videoRecorder.handleStopRecording(message.arg1, (SendOptions) message.obj);
            } else if (i == 2) {
                videoRecorder.handleVideoFrameAvailable((message.arg1 << 32) | (message.arg2 & 4294967295L), (Integer) message.obj);
            } else if (i == 3) {
                videoRecorder.handleAudioFrameAvailable((AudioBufferInfo) message.obj);
            } else if (i == 4) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("InstantCamera pause encoder");
                }
                videoRecorder.handlePauseRecording();
            } else if (i != 5) {
            } else {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("InstantCamera resume encoder");
                }
                videoRecorder.handleResumeRecording();
            }
        }
    }

    /* loaded from: classes3.dex */
    public class InstantViewCameraContainer extends FrameLayout {
        float imageProgress;
        ImageReceiver imageReceiver;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public InstantViewCameraContainer(Context context) {
            super(context);
            InstantCameraView.this = r1;
            r1.setWillNotDraw(false);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            float f = this.imageProgress;
            if (f != 1.0f) {
                float f2 = f + 0.064f;
                this.imageProgress = f2;
                if (f2 > 1.0f) {
                    this.imageProgress = 1.0f;
                }
                invalidate();
            }
            if (this.imageReceiver != null) {
                canvas.save();
                if (this.imageReceiver.getImageWidth() != InstantCameraView.this.textureViewSize) {
                    float imageWidth = InstantCameraView.this.textureViewSize / this.imageReceiver.getImageWidth();
                    canvas.scale(imageWidth, imageWidth);
                }
                canvas.translate(-this.imageReceiver.getImageX(), -this.imageReceiver.getImageY());
                float alpha = this.imageReceiver.getAlpha();
                this.imageReceiver.setAlpha(this.imageProgress);
                this.imageReceiver.draw(canvas);
                this.imageReceiver.setAlpha(alpha);
                canvas.restore();
            }
        }

        public void setImageReceiver(ImageReceiver imageReceiver) {
            if (this.imageReceiver == null) {
                this.imageProgress = 0.0f;
            }
            this.imageReceiver = imageReceiver;
            invalidate();
        }
    }

    /* loaded from: classes3.dex */
    public static class SendOptions {
        long effectId;
        boolean notify;
        int scheduleDate;
        int ttl;

        public SendOptions(boolean z, int i, int i2, long j) {
            this.notify = z;
            this.scheduleDate = i;
            this.ttl = i2;
            this.effectId = j;
        }
    }

    /* loaded from: classes3.dex */
    public class VideoRecorder implements Runnable {
        private int alphaHandle;
        private MediaCodec.BufferInfo audioBufferInfo;
        private long audioDiff;
        private MediaCodec audioEncoder;
        private long audioFirst;
        private long audioLast;
        private long audioLastDt;
        private AudioRecord audioRecorder;
        private long audioStartTime;
        private boolean audioStopedByTime;
        private int audioTrackIndex;
        private boolean blendEnabled;
        private ArrayBlockingQueue buffers;
        private ArrayList buffersToWrite;
        private long currentTimestamp;
        private long desyncTime;
        private int drawProgram;
        private android.opengl.EGLConfig eglConfig;
        private android.opengl.EGLContext eglContext;
        private android.opengl.EGLDisplay eglDisplay;
        private android.opengl.EGLSurface eglSurface;
        private File fileToWrite;
        DispatchQueue fileWriteQueue;
        private boolean firstEncode;
        private boolean firstVideoFrameSincePause;
        private int frameCount;
        private DispatchQueue generateKeyframeThumbsQueue;
        private volatile EncoderHandler handler;
        private ArrayList keyframeThumbs;
        private Integer lastCameraId;
        private long lastCommitedFrameTime;
        private long lastTimestamp;
        private MP4Builder mediaMuxer;
        private volatile boolean pauseRecorder;
        private int positionHandle;
        private int prependHeaderSize;
        private long prevAudioLast;
        long prevTimestamp;
        private long prevVideoLast;
        private int previewSizeHandle;
        public volatile boolean ready;
        private Runnable recorderRunnable;
        private int resolutionHandle;
        private volatile boolean running;
        private volatile int sendWhenDone;
        private volatile SendOptions sendWhenDoneOptions;
        private boolean sentMedia;
        private android.opengl.EGLContext sharedEglContext;
        private boolean skippedFirst;
        private long skippedTime;
        private boolean started;
        private Surface surface;
        private final Object sync;
        private int texelSizeHandle;
        private int textureHandle;
        private int textureMatrixHandle;
        private int vertexMatrixHandle;
        private int videoBitrate;
        private MediaCodec.BufferInfo videoBufferInfo;
        private boolean videoConvertFirstWrite;
        private long videoDiff;
        private MediaCodec videoEncoder;
        private File videoFile;
        private long videoFirst;
        private int videoHeight;
        private long videoLast;
        private long videoLastDt;
        private int videoTrackIndex;
        private int videoWidth;
        private boolean writingToDifferentFile;
        private int zeroTimeStamps;

        /* loaded from: classes3.dex */
        public class 1 implements Runnable {
            1() {
                VideoRecorder.this = r1;
            }

            public /* synthetic */ void lambda$run$0(double d) {
                NotificationCenter.getInstance(InstantCameraView.this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordProgressChanged, Integer.valueOf(InstantCameraView.this.recordingGuid), Double.valueOf(d));
            }

            /* JADX WARN: Code restructure failed: missing block: B:119:0x0047, code lost:
                if (org.telegram.ui.Components.InstantCameraView.VideoRecorder.this.sendWhenDone == 0) goto L85;
             */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void run() {
                boolean z;
                boolean z2;
                AudioBufferInfo audioBufferInfo;
                long j;
                AudioTimestamp audioTimestamp = new AudioTimestamp();
                long j2 = -1;
                long j3 = -1;
                if (Build.VERSION.SDK_INT >= 24) {
                    z = false;
                    z2 = true;
                } else {
                    z = false;
                    z2 = false;
                }
                while (!z) {
                    if ((!VideoRecorder.this.running || VideoRecorder.this.pauseRecorder) && VideoRecorder.this.audioRecorder.getRecordingState() != 1) {
                        try {
                            VideoRecorder.this.audioRecorder.stop();
                        } catch (Exception unused) {
                            z = true;
                        }
                    }
                    boolean z3 = z;
                    if (VideoRecorder.this.buffers.isEmpty()) {
                        try {
                            audioBufferInfo = new AudioBufferInfo();
                        } catch (OutOfMemoryError unused2) {
                            System.gc();
                            audioBufferInfo = new AudioBufferInfo();
                        }
                    } else {
                        audioBufferInfo = (AudioBufferInfo) VideoRecorder.this.buffers.poll();
                    }
                    AudioBufferInfo audioBufferInfo2 = audioBufferInfo;
                    audioBufferInfo2.lastWroteBuffer = 0;
                    int i = 10;
                    audioBufferInfo2.results = 10;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= i) {
                            break;
                        }
                        if (j3 == j2 && !z2) {
                            j3 = System.nanoTime() / 1000;
                        }
                        ByteBuffer byteBuffer = audioBufferInfo2.buffer[i2];
                        byteBuffer.rewind();
                        int read = VideoRecorder.this.audioRecorder.read(byteBuffer, 2048);
                        if (read > 0 && i2 % 2 == 0) {
                            byteBuffer.limit(read);
                            double d = 0.0d;
                            for (int i3 = 0; i3 < read / 2; i3++) {
                                short s = byteBuffer.getShort();
                                double d2 = s * s;
                                Double.isNaN(d2);
                                d += d2;
                            }
                            double d3 = read;
                            Double.isNaN(d3);
                            final double sqrt = Math.sqrt((d / d3) / 2.0d);
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$1$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    InstantCameraView.VideoRecorder.1.this.lambda$run$0(sqrt);
                                }
                            });
                            byteBuffer.position(0);
                        }
                        if (read <= 0) {
                            audioBufferInfo2.results = i2;
                            if (!VideoRecorder.this.running) {
                                audioBufferInfo2.last = true;
                            }
                        } else {
                            if (z2) {
                                try {
                                    VideoRecorder.this.audioRecorder.getTimestamp(audioTimestamp, 0);
                                    long j4 = j3;
                                    j3 = audioTimestamp.nanoTime / 1000;
                                    j = j4;
                                } catch (Exception e) {
                                    FileLog.e(e);
                                    j3 = System.nanoTime() / 1000;
                                    j = j3;
                                    z2 = false;
                                }
                            } else {
                                j = j3;
                            }
                            audioBufferInfo2.offset[i2] = j3;
                            audioBufferInfo2.read[i2] = read;
                            int i4 = ((read * MediaController.VIDEO_BITRATE_480) / 48000) / 2;
                            if (!z2) {
                                j += i4;
                            }
                            j3 = j;
                            i2++;
                            j2 = -1;
                            i = 10;
                        }
                    }
                    if (audioBufferInfo2.results >= 0 || audioBufferInfo2.last) {
                        z = (VideoRecorder.this.running || audioBufferInfo2.results >= 10) ? z3 : true;
                        VideoRecorder.this.handler.sendMessage(VideoRecorder.this.handler.obtainMessage(3, audioBufferInfo2));
                    } else if (VideoRecorder.this.running) {
                        try {
                            VideoRecorder.this.buffers.put(audioBufferInfo2);
                        } catch (Exception unused3) {
                        }
                        z = z3;
                    } else {
                        z = true;
                    }
                    j2 = -1;
                }
                try {
                    VideoRecorder.this.audioRecorder.release();
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                if (VideoRecorder.this.pauseRecorder) {
                    return;
                }
                VideoRecorder.this.handler.sendMessage(VideoRecorder.this.handler.obtainMessage(1, VideoRecorder.this.sendWhenDone, 0, VideoRecorder.this.sendWhenDoneOptions));
            }
        }

        /* loaded from: classes3.dex */
        public class GenerateKeyframeThumbTask implements Runnable {
            private GenerateKeyframeThumbTask() {
                VideoRecorder.this = r1;
            }

            public /* synthetic */ void lambda$run$0(Bitmap bitmap) {
                if ((bitmap == null || bitmap.getPixel(0, 0) == 0) && VideoRecorder.this.keyframeThumbs.size() > 1) {
                    VideoRecorder.this.keyframeThumbs.add((Bitmap) VideoRecorder.this.keyframeThumbs.get(VideoRecorder.this.keyframeThumbs.size() - 1));
                } else {
                    VideoRecorder.this.keyframeThumbs.add(bitmap);
                }
            }

            @Override // java.lang.Runnable
            public void run() {
                TextureView textureView = InstantCameraView.this.textureView;
                if (textureView != null) {
                    try {
                        final Bitmap bitmap = textureView.getBitmap(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$GenerateKeyframeThumbTask$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                InstantCameraView.VideoRecorder.GenerateKeyframeThumbTask.this.lambda$run$0(bitmap);
                            }
                        });
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            }
        }

        private VideoRecorder() {
            InstantCameraView.this = r5;
            this.videoConvertFirstWrite = true;
            this.eglDisplay = EGL14.EGL_NO_DISPLAY;
            this.eglContext = EGL14.EGL_NO_CONTEXT;
            this.eglSurface = EGL14.EGL_NO_SURFACE;
            this.buffersToWrite = new ArrayList();
            this.videoTrackIndex = -5;
            this.audioTrackIndex = -5;
            this.audioStartTime = -1L;
            this.currentTimestamp = 0L;
            this.lastTimestamp = -1L;
            this.sync = new Object();
            this.videoFirst = -1L;
            this.prevVideoLast = -1L;
            this.audioFirst = -1L;
            this.audioLast = -1L;
            this.audioLastDt = 0L;
            this.prevAudioLast = -1L;
            this.lastCameraId = 0;
            this.buffers = new ArrayBlockingQueue(10);
            this.keyframeThumbs = new ArrayList();
            this.recorderRunnable = new 1();
        }

        private void createKeyframeThumb() {
            if (this.generateKeyframeThumbsQueue == null || Build.VERSION.SDK_INT < 21 || SharedConfig.getDevicePerformanceClass() != 2 || this.frameCount % 33 != 0) {
                return;
            }
            this.generateKeyframeThumbsQueue.postRunnable(new GenerateKeyframeThumbTask());
        }

        /* JADX WARN: Code restructure failed: missing block: B:24:0x003b, code lost:
            if (r17 != false) goto L12;
         */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x0052, code lost:
            if (r17 != false) goto L12;
         */
        /* JADX WARN: Code restructure failed: missing block: B:28:0x0054, code lost:
            r4 = r14.length();
         */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x005d, code lost:
            r4 = 0;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void didWriteData(File file, long j, boolean z) {
            FileLoader fileLoader;
            String file2;
            boolean z2;
            long length;
            if (this.videoConvertFirstWrite) {
                FileLoader.getInstance(InstantCameraView.this.currentAccount).uploadFile(file.toString(), InstantCameraView.this.isSecretChat, false, 1L, ConnectionsManager.FileTypeVideo, false);
                this.videoConvertFirstWrite = false;
                if (!z) {
                    return;
                }
                fileLoader = FileLoader.getInstance(InstantCameraView.this.currentAccount);
                file2 = file.toString();
                z2 = InstantCameraView.this.isSecretChat;
            } else {
                fileLoader = FileLoader.getInstance(InstantCameraView.this.currentAccount);
                file2 = file.toString();
                z2 = InstantCameraView.this.isSecretChat;
            }
            fileLoader.checkUploadNewDataAvailable(file2, z2, j, length);
        }

        /* JADX WARN: Code restructure failed: missing block: B:163:0x0067, code lost:
            org.telegram.messenger.FileLog.d(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:212:0x0174, code lost:
            if (org.telegram.messenger.BuildVars.LOGS_ENABLED == false) goto L85;
         */
        /* JADX WARN: Code restructure failed: missing block: B:214:0x0178, code lost:
            if (r14 < 60000000) goto L84;
         */
        /* JADX WARN: Code restructure failed: missing block: B:215:0x017a, code lost:
            r0 = "InstantCamera stop audio encoding because recorded time more than 60s";
         */
        /* JADX WARN: Code restructure failed: missing block: B:216:0x017d, code lost:
            r0 = "InstantCamera stop audio encoding because of stoped video recording at " + r2.offset[r10] + " last video " + r19.videoLast;
         */
        /* JADX WARN: Code restructure failed: missing block: B:217:0x019c, code lost:
            org.telegram.messenger.FileLog.d(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:218:0x019f, code lost:
            r19.audioStopedByTime = true;
            r19.buffersToWrite.clear();
            r2 = null;
            r0 = true;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void handleAudioFrameAvailable(AudioBufferInfo audioBufferInfo) {
            ByteBuffer byteBuffer;
            String str;
            if (this.pauseRecorder || this.audioStopedByTime) {
                return;
            }
            AudioBufferInfo audioBufferInfo2 = audioBufferInfo;
            this.buffersToWrite.add(audioBufferInfo2);
            if (this.audioFirst == -1) {
                if (this.videoFirst == -1) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("InstantCamera video record not yet started");
                        return;
                    }
                    return;
                }
                while (true) {
                    for (int i = 0; i < audioBufferInfo2.results; i++) {
                        if (i != 0 || Math.abs(this.videoFirst - audioBufferInfo2.offset[i]) <= 10000000) {
                            long j = audioBufferInfo2.offset[i];
                            if (j >= this.videoFirst) {
                                audioBufferInfo2.lastWroteBuffer = i;
                                this.audioFirst = j;
                                if (BuildVars.LOGS_ENABLED) {
                                    str = "InstantCamera found first audio frame at " + i + " timestamp = " + audioBufferInfo2.offset[i];
                                }
                            } else {
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("InstantCamera ignore first audio frame at " + i + " timestamp = " + audioBufferInfo2.offset[i]);
                                }
                            }
                        } else {
                            long j2 = this.videoFirst;
                            long j3 = audioBufferInfo2.offset[i];
                            this.desyncTime = j2 - j3;
                            this.audioFirst = j3;
                            if (BuildVars.LOGS_ENABLED) {
                                str = "InstantCamera detected desync between audio and video " + this.desyncTime;
                            }
                        }
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("InstantCamera first audio frame not found, removing buffers " + audioBufferInfo2.results);
                    }
                    this.buffersToWrite.remove(audioBufferInfo2);
                    if (this.buffersToWrite.isEmpty()) {
                        return;
                    }
                    audioBufferInfo2 = (AudioBufferInfo) this.buffersToWrite.get(0);
                }
            }
            if (this.audioStartTime == -1) {
                this.audioStartTime = audioBufferInfo2.offset[audioBufferInfo2.lastWroteBuffer];
            }
            if (this.buffersToWrite.size() > 1) {
                audioBufferInfo2 = (AudioBufferInfo) this.buffersToWrite.get(0);
            }
            try {
                drainEncoder(false);
            } catch (Exception e) {
                FileLog.e(e);
            }
            boolean z = false;
            while (audioBufferInfo2 != null) {
                try {
                    int dequeueInputBuffer = this.audioEncoder.dequeueInputBuffer(0L);
                    if (dequeueInputBuffer >= 0) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            byteBuffer = this.audioEncoder.getInputBuffer(dequeueInputBuffer);
                        } else {
                            byteBuffer = this.audioEncoder.getInputBuffers()[dequeueInputBuffer];
                            byteBuffer.clear();
                        }
                        long[] jArr = audioBufferInfo2.offset;
                        int i2 = audioBufferInfo2.lastWroteBuffer;
                        long j4 = jArr[i2];
                        while (true) {
                            int i3 = audioBufferInfo2.results;
                            if (i2 > i3) {
                                break;
                            }
                            if (i2 < i3) {
                                long j5 = audioBufferInfo2.offset[i2] - this.audioStartTime;
                                if (this.running || (audioBufferInfo2.offset[i2] < this.videoLast - this.desyncTime && j5 < 60000000)) {
                                    if (byteBuffer.remaining() < audioBufferInfo2.read[i2]) {
                                        audioBufferInfo2.lastWroteBuffer = i2;
                                        break;
                                    }
                                    byteBuffer.put(audioBufferInfo2.buffer[i2]);
                                }
                            }
                            if (i2 >= audioBufferInfo2.results - 1) {
                                this.buffersToWrite.remove(audioBufferInfo2);
                                if (this.running) {
                                    this.buffers.put(audioBufferInfo2);
                                }
                                if (this.buffersToWrite.isEmpty()) {
                                    z = audioBufferInfo2.last;
                                    break;
                                }
                                audioBufferInfo2 = (AudioBufferInfo) this.buffersToWrite.get(0);
                            }
                            i2++;
                        }
                        audioBufferInfo2 = null;
                        long j6 = j4 == 0 ? 0L : j4 - this.audioStartTime;
                        long j7 = this.prevAudioLast;
                        if (j7 >= 0) {
                            j6 += j7;
                        }
                        long j8 = j6;
                        this.audioLastDt = j8 - this.audioLast;
                        this.audioLast = j8;
                        this.audioEncoder.queueInputBuffer(dequeueInputBuffer, 0, byteBuffer.position(), j8, z ? 4 : 0);
                    }
                } catch (Throwable th) {
                    FileLog.e(th);
                    return;
                }
            }
        }

        public void handlePauseRecording() {
            this.pauseRecorder = true;
            if (InstantCameraView.this.previewFile != null) {
                InstantCameraView.this.previewFile.delete();
                InstantCameraView.this.previewFile = null;
            }
            InstantCameraView instantCameraView = InstantCameraView.this;
            instantCameraView.previewFile = StoryEntry.makeCacheFile(instantCameraView.currentAccount, true);
            try {
                FileLog.d("InstantCamera handlePauseRecording drain encoders");
                drainEncoder(false);
            } catch (Exception e) {
                FileLog.e(e);
            }
            MP4Builder mP4Builder = this.mediaMuxer;
            if (mP4Builder != null) {
                InstantCameraView instantCameraView2 = InstantCameraView.this;
                if (instantCameraView2.WRITE_TO_FILE_IN_BACKGROUND) {
                    final CountDownLatch countDownLatch = new CountDownLatch(1);
                    this.fileWriteQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            InstantCameraView.VideoRecorder.this.lambda$handlePauseRecording$4(countDownLatch);
                        }
                    });
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                } else {
                    try {
                        mP4Builder.finishMovie(instantCameraView2.previewFile);
                    } catch (Exception e3) {
                        FileLog.e(e3);
                    }
                }
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    InstantCameraView.VideoRecorder.this.lambda$handlePauseRecording$5();
                }
            });
        }

        public void handleResumeRecording() {
            this.pauseRecorder = false;
        }

        public void handleStopRecording(final int i, final SendOptions sendOptions) {
            boolean z;
            DispatchQueue dispatchQueue;
            if (i != 1 || ((InstantCameraView.this.videoEditedInfo != null && InstantCameraView.this.videoEditedInfo.needConvert()) || InstantCameraView.this.delegate.isInScheduleMode())) {
                z = true;
            } else {
                if (!this.sentMedia) {
                    this.sentMedia = true;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            InstantCameraView.VideoRecorder.this.lambda$handleStopRecording$6(sendOptions);
                        }
                    });
                }
                z = false;
            }
            if (this.running && !this.pauseRecorder) {
                FileLog.d("InstantCamera handleStopRecording running=false");
                this.sendWhenDone = i;
                this.sendWhenDoneOptions = sendOptions;
                this.running = false;
                return;
            }
            try {
                FileLog.d("InstantCamera handleStopRecording drain encoders");
                drainEncoder(true);
            } catch (Exception e) {
                FileLog.e(e);
            }
            MediaCodec mediaCodec = this.videoEncoder;
            if (mediaCodec != null) {
                try {
                    mediaCodec.stop();
                    this.videoEncoder.release();
                    this.videoEncoder = null;
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
            MediaCodec mediaCodec2 = this.audioEncoder;
            if (mediaCodec2 != null) {
                try {
                    mediaCodec2.stop();
                    this.audioEncoder.release();
                    this.audioEncoder = null;
                    setBluetoothScoOn(false);
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
            }
            if (InstantCameraView.this.previewFile != null) {
                InstantCameraView.this.previewFile.delete();
                InstantCameraView.this.previewFile = null;
            }
            MP4Builder mP4Builder = this.mediaMuxer;
            if (mP4Builder != null) {
                if (InstantCameraView.this.WRITE_TO_FILE_IN_BACKGROUND) {
                    final CountDownLatch countDownLatch = new CountDownLatch(1);
                    this.fileWriteQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            InstantCameraView.VideoRecorder.this.lambda$handleStopRecording$7(countDownLatch);
                        }
                    });
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e4) {
                        e4.printStackTrace();
                    }
                } else {
                    try {
                        mP4Builder.finishMovie();
                    } catch (Exception e5) {
                        FileLog.e(e5);
                    }
                }
                FileLog.d("InstantCamera handleStopRecording finish muxer");
                if (this.writingToDifferentFile) {
                    if (this.videoFile.exists()) {
                        try {
                            this.videoFile.delete();
                        } catch (Exception e6) {
                            FileLog.e("InstantCamera copying fileToWrite to videoFile, deleting videoFile error " + this.videoFile);
                            FileLog.e(e6);
                        }
                    }
                    if (!this.fileToWrite.renameTo(this.videoFile)) {
                        FileLog.e("InstantCamera unable to rename file, try move file");
                        try {
                            AndroidUtilities.copyFile(this.fileToWrite, this.videoFile);
                            this.fileToWrite.delete();
                        } catch (IOException e7) {
                            FileLog.e(e7);
                            FileLog.e("InstantCamera unable to move file");
                        }
                    }
                }
            }
            if (i != 2 && (dispatchQueue = this.generateKeyframeThumbsQueue) != null) {
                dispatchQueue.cleanupQueue();
                this.generateKeyframeThumbsQueue.recycle();
                this.generateKeyframeThumbsQueue = null;
            }
            FileLog.d("InstantCamera handleStopRecording send " + i);
            if (i == 0) {
                FileLoader.getInstance(InstantCameraView.this.currentAccount).cancelFileUpload(this.videoFile.getAbsolutePath(), false);
                try {
                    this.fileToWrite.delete();
                } catch (Throwable unused) {
                }
                try {
                    this.videoFile.delete();
                } catch (Throwable unused2) {
                }
            } else {
                if (z && (i != 1 || !this.sentMedia)) {
                    this.sentMedia = true;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            InstantCameraView.VideoRecorder.this.lambda$handleStopRecording$10(i, sendOptions);
                        }
                    });
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        InstantCameraView.VideoRecorder.this.lambda$handleStopRecording$11();
                    }
                });
            }
            EGL14.eglDestroySurface(this.eglDisplay, this.eglSurface);
            this.eglSurface = EGL14.EGL_NO_SURFACE;
            Surface surface = this.surface;
            if (surface != null) {
                surface.release();
                this.surface = null;
            }
            android.opengl.EGLDisplay eGLDisplay = this.eglDisplay;
            if (eGLDisplay != EGL14.EGL_NO_DISPLAY) {
                android.opengl.EGLSurface eGLSurface = EGL14.EGL_NO_SURFACE;
                EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL14.EGL_NO_CONTEXT);
                EGL14.eglDestroyContext(this.eglDisplay, this.eglContext);
                EGL14.eglReleaseThread();
                EGL14.eglTerminate(this.eglDisplay);
            }
            this.eglDisplay = EGL14.EGL_NO_DISPLAY;
            this.eglContext = EGL14.EGL_NO_CONTEXT;
            this.eglConfig = null;
            this.handler.exit();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    InstantCameraView.VideoRecorder.this.lambda$handleStopRecording$12();
                }
            });
        }

        /* JADX WARN: Code restructure failed: missing block: B:137:0x0082, code lost:
            if (r11 < 0) goto L87;
         */
        /* JADX WARN: Removed duplicated region for block: B:142:0x0095  */
        /* JADX WARN: Removed duplicated region for block: B:148:0x00b1  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void handleVideoFrameAvailable(long j, Integer num) {
            boolean z;
            long j2;
            long j3;
            long j4;
            long j5;
            FloatBuffer floatBuffer;
            FloatBuffer floatBuffer2;
            Runnable runnable;
            if (this.pauseRecorder || !InstantCameraView.this.cameraTextureAvailable) {
                return;
            }
            try {
                drainEncoder(false);
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (this.lastCameraId.equals(num)) {
                z = false;
            } else {
                this.lastCameraId = num;
                z = true;
            }
            long j6 = this.prevVideoLast;
            if (j6 >= 0) {
                if (this.videoDiff == -1) {
                    this.videoDiff = j - j6;
                }
                j2 = j - this.videoDiff;
            } else {
                j2 = j;
            }
            if (!z) {
                long j7 = this.lastTimestamp;
                if (j7 != -1) {
                    j5 = j2 - j7;
                    this.lastTimestamp = j2;
                    j4 = j5;
                    this.firstVideoFrameSincePause = false;
                    this.lastCommitedFrameTime = System.currentTimeMillis();
                    if (!this.skippedFirst) {
                        long j8 = this.skippedTime + j5;
                        this.skippedTime = j8;
                        if (j8 < 200000000) {
                            return;
                        }
                        this.skippedFirst = true;
                    }
                    this.currentTimestamp += j5;
                    if (this.videoFirst == -1) {
                        this.videoFirst = j2 / 1000;
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("InstantCamera first video frame was at " + this.videoFirst);
                        }
                    }
                    this.videoLastDt = j2 - this.videoLast;
                    this.videoLast = j2;
                    floatBuffer = InstantCameraView.this.textureBuffer;
                    floatBuffer2 = InstantCameraView.this.vertexBuffer;
                    FloatBuffer floatBuffer3 = InstantCameraView.this.oldTextureTextureBuffer;
                    if (floatBuffer != null || floatBuffer2 == null) {
                        FileLog.d("InstantCamera handleVideoFrameAvailable skip frame " + floatBuffer + " " + floatBuffer2);
                    }
                    GLES20.glUseProgram(this.drawProgram);
                    GLES20.glActiveTexture(33984);
                    GLES20.glVertexAttribPointer(this.positionHandle, 3, 5126, false, 12, (Buffer) floatBuffer2);
                    GLES20.glEnableVertexAttribArray(this.positionHandle);
                    GLES20.glVertexAttribPointer(this.textureHandle, 2, 5126, false, 8, (Buffer) floatBuffer);
                    GLES20.glEnableVertexAttribArray(this.textureHandle);
                    GLES20.glUniformMatrix4fv(this.vertexMatrixHandle, 1, false, InstantCameraView.this.mMVPMatrix, 0);
                    GLES20.glUniform2f(this.resolutionHandle, this.videoWidth, this.videoHeight);
                    if (InstantCameraView.this.oldCameraTexture[0] != 0 && floatBuffer3 != null && !InstantCameraView.this.bothCameras) {
                        if (!this.blendEnabled) {
                            GLES20.glEnable(3042);
                            this.blendEnabled = true;
                        }
                        if (InstantCameraView.this.oldTexturePreviewSize != null) {
                            GLES20.glUniform2f(this.previewSizeHandle, InstantCameraView.this.oldTexturePreviewSize.getWidth(), InstantCameraView.this.oldTexturePreviewSize.getHeight());
                        }
                        GLES20.glVertexAttribPointer(this.textureHandle, 2, 5126, false, 8, (Buffer) floatBuffer3);
                        GLES20.glUniformMatrix4fv(this.textureMatrixHandle, 1, false, InstantCameraView.this.moldSTMatrix, 0);
                        GLES20.glUniform1f(this.alphaHandle, 1.0f);
                        GLES20.glBindTexture(36197, InstantCameraView.this.oldCameraTexture[0]);
                        GLES20.glDrawArrays(5, 0, 4);
                    }
                    if (InstantCameraView.this.previewSize != null) {
                        GLES20.glUniform2f(this.previewSizeHandle, InstantCameraView.this.previewSize[InstantCameraView.this.surfaceIndex].getWidth(), InstantCameraView.this.previewSize[InstantCameraView.this.surfaceIndex].getHeight());
                        GLES20.glUniform2f(this.texelSizeHandle, (1.0f / InstantCameraView.this.previewSize[InstantCameraView.this.surfaceIndex].getWidth()) / 2.0f, (1.0f / InstantCameraView.this.previewSize[InstantCameraView.this.surfaceIndex].getHeight()) / 2.0f);
                    }
                    int i = InstantCameraView.this.cameraTexture[InstantCameraView.this.surfaceIndex];
                    if (i != Integer.MIN_VALUE) {
                        GLES20.glUniformMatrix4fv(this.textureMatrixHandle, 1, false, InstantCameraView.this.mSTMatrix, 0);
                        GLES20.glUniform1f(this.alphaHandle, InstantCameraView.this.cameraTextureAlpha);
                        GLES20.glBindTexture(36197, i);
                        GLES20.glDrawArrays(5, 0, 4);
                    }
                    GLES20.glDisableVertexAttribArray(this.positionHandle);
                    GLES20.glDisableVertexAttribArray(this.textureHandle);
                    GLES20.glBindTexture(36197, 0);
                    GLES20.glUseProgram(0);
                    EGLExt.eglPresentationTimeANDROID(this.eglDisplay, this.eglSurface, this.currentTimestamp);
                    EGL14.eglSwapBuffers(this.eglDisplay, this.eglSurface);
                    createKeyframeThumb();
                    this.frameCount++;
                    if (InstantCameraView.this.oldCameraTexture[0] != 0 && InstantCameraView.this.cameraTextureAlpha < 1.0f && !InstantCameraView.this.bothCameras) {
                        InstantCameraView.access$3516(InstantCameraView.this, ((float) j4) / 2.0E8f);
                        if (InstantCameraView.this.cameraTextureAlpha <= 1.0f) {
                            return;
                        }
                        GLES20.glDisable(3042);
                        this.blendEnabled = false;
                        InstantCameraView.this.cameraTextureAlpha = 1.0f;
                        GLES20.glDeleteTextures(1, InstantCameraView.this.oldCameraTexture, 0);
                        InstantCameraView.this.oldCameraTexture[0] = 0;
                        if (InstantCameraView.this.cameraReady) {
                            return;
                        }
                        InstantCameraView.this.cameraReady = true;
                        runnable = new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda9
                            @Override // java.lang.Runnable
                            public final void run() {
                                InstantCameraView.VideoRecorder.this.lambda$handleVideoFrameAvailable$2();
                            }
                        };
                    } else if (InstantCameraView.this.cameraReady) {
                        return;
                    } else {
                        InstantCameraView.this.cameraReady = true;
                        runnable = new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda10
                            @Override // java.lang.Runnable
                            public final void run() {
                                InstantCameraView.VideoRecorder.this.lambda$handleVideoFrameAvailable$3();
                            }
                        };
                    }
                    AndroidUtilities.runOnUIThread(runnable);
                    return;
                }
            }
            if (this.currentTimestamp != 0 && !this.firstVideoFrameSincePause) {
                j3 = j2 - this.lastTimestamp;
                long currentTimeMillis = (System.currentTimeMillis() - this.lastCommitedFrameTime) * 1000000;
                if (j3 < 0 || Math.abs(currentTimeMillis - j3) > 100000000) {
                    j3 = currentTimeMillis;
                }
            }
            j3 = 0;
            this.lastTimestamp = j2;
            j4 = 0;
            j5 = j3;
            this.firstVideoFrameSincePause = false;
            this.lastCommitedFrameTime = System.currentTimeMillis();
            if (!this.skippedFirst) {
            }
            this.currentTimestamp += j5;
            if (this.videoFirst == -1) {
            }
            this.videoLastDt = j2 - this.videoLast;
            this.videoLast = j2;
            floatBuffer = InstantCameraView.this.textureBuffer;
            floatBuffer2 = InstantCameraView.this.vertexBuffer;
            FloatBuffer floatBuffer32 = InstantCameraView.this.oldTextureTextureBuffer;
            if (floatBuffer != null) {
            }
            FileLog.d("InstantCamera handleVideoFrameAvailable skip frame " + floatBuffer + " " + floatBuffer2);
        }

        public /* synthetic */ void lambda$drainEncoder$14(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
            long j;
            try {
                j = this.mediaMuxer.writeSampleData(this.videoTrackIndex, byteBuffer, bufferInfo, true);
            } catch (Exception e) {
                e.printStackTrace();
                j = 0;
            }
            if (j == 0 || this.writingToDifferentFile || !InstantCameraView.this.allowSendingWhileRecording) {
                return;
            }
            didWriteData(this.videoFile, j, false);
        }

        public /* synthetic */ void lambda$drainEncoder$15(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
            long j;
            try {
                j = this.mediaMuxer.writeSampleData(this.audioTrackIndex, byteBuffer, bufferInfo, false);
            } catch (Exception e) {
                e.printStackTrace();
                j = 0;
            }
            if (j == 0 || this.writingToDifferentFile || !InstantCameraView.this.allowSendingWhileRecording) {
                return;
            }
            didWriteData(this.videoFile, j, false);
        }

        public /* synthetic */ void lambda$handlePauseRecording$4(CountDownLatch countDownLatch) {
            try {
                this.mediaMuxer.finishMovie(InstantCameraView.this.previewFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }

        public /* synthetic */ void lambda$handlePauseRecording$5() {
            InstantCameraView.this.videoEditedInfo = new VideoEditedInfo();
            InstantCameraView.this.videoEditedInfo.roundVideo = true;
            InstantCameraView.this.videoEditedInfo.startTime = -1L;
            InstantCameraView.this.videoEditedInfo.endTime = -1L;
            InstantCameraView.this.videoEditedInfo.file = InstantCameraView.this.file;
            InstantCameraView.this.videoEditedInfo.encryptedFile = InstantCameraView.this.encryptedFile;
            InstantCameraView.this.videoEditedInfo.key = InstantCameraView.this.key;
            InstantCameraView.this.videoEditedInfo.iv = InstantCameraView.this.iv;
            InstantCameraView.this.videoEditedInfo.estimatedSize = Math.max(1L, InstantCameraView.this.size);
            InstantCameraView.this.videoEditedInfo.framerate = 25;
            VideoEditedInfo videoEditedInfo = InstantCameraView.this.videoEditedInfo;
            InstantCameraView.this.videoEditedInfo.originalWidth = 360;
            videoEditedInfo.resultWidth = 360;
            VideoEditedInfo videoEditedInfo2 = InstantCameraView.this.videoEditedInfo;
            InstantCameraView.this.videoEditedInfo.originalHeight = 360;
            videoEditedInfo2.resultHeight = 360;
            InstantCameraView.this.videoEditedInfo.originalPath = InstantCameraView.this.previewFile.getAbsolutePath();
            setupVideoPlayer(InstantCameraView.this.previewFile);
            InstantCameraView.this.videoEditedInfo.estimatedDuration = InstantCameraView.this.recordedTime;
            NotificationCenter.getInstance(InstantCameraView.this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.audioDidSent, Integer.valueOf(InstantCameraView.this.recordingGuid), InstantCameraView.this.videoEditedInfo, InstantCameraView.this.previewFile.getAbsolutePath(), this.keyframeThumbs);
        }

        public /* synthetic */ void lambda$handleStopRecording$10(int i, final SendOptions sendOptions) {
            if (InstantCameraView.this.videoEditedInfo == null) {
                InstantCameraView.this.videoEditedInfo = new VideoEditedInfo();
                InstantCameraView.this.videoEditedInfo.startTime = -1L;
                InstantCameraView.this.videoEditedInfo.endTime = -1L;
            }
            if (InstantCameraView.this.videoEditedInfo.needConvert()) {
                InstantCameraView.this.file = null;
                InstantCameraView.this.encryptedFile = null;
                InstantCameraView.this.key = null;
                InstantCameraView.this.iv = null;
                double d = InstantCameraView.this.videoEditedInfo.estimatedDuration;
                InstantCameraView.this.videoEditedInfo.estimatedDuration = (InstantCameraView.this.videoEditedInfo.endTime >= 0 ? InstantCameraView.this.videoEditedInfo.endTime : InstantCameraView.this.videoEditedInfo.estimatedDuration) - (InstantCameraView.this.videoEditedInfo.startTime >= 0 ? InstantCameraView.this.videoEditedInfo.startTime : 0L);
                VideoEditedInfo videoEditedInfo = InstantCameraView.this.videoEditedInfo;
                double d2 = InstantCameraView.this.size;
                double d3 = InstantCameraView.this.videoEditedInfo.estimatedDuration;
                Double.isNaN(d3);
                Double.isNaN(d);
                Double.isNaN(d2);
                videoEditedInfo.estimatedSize = Math.max(1L, (long) (d2 * (d3 / d)));
                InstantCameraView.this.videoEditedInfo.bitrate = MediaController.VIDEO_BITRATE_480;
                if (InstantCameraView.this.videoEditedInfo.startTime > 0) {
                    InstantCameraView.this.videoEditedInfo.startTime *= 1000;
                }
                if (InstantCameraView.this.videoEditedInfo.endTime > 0) {
                    InstantCameraView.this.videoEditedInfo.endTime *= 1000;
                }
                FileLoader.getInstance(InstantCameraView.this.currentAccount).cancelFileUpload(InstantCameraView.this.cameraFile.getAbsolutePath(), false);
            } else {
                InstantCameraView.this.videoEditedInfo.estimatedSize = Math.max(1L, InstantCameraView.this.size);
            }
            InstantCameraView.this.videoEditedInfo.roundVideo = true;
            InstantCameraView.this.videoEditedInfo.file = InstantCameraView.this.file;
            InstantCameraView.this.videoEditedInfo.encryptedFile = InstantCameraView.this.encryptedFile;
            InstantCameraView.this.videoEditedInfo.key = InstantCameraView.this.key;
            InstantCameraView.this.videoEditedInfo.iv = InstantCameraView.this.iv;
            InstantCameraView.this.videoEditedInfo.framerate = 25;
            VideoEditedInfo videoEditedInfo2 = InstantCameraView.this.videoEditedInfo;
            InstantCameraView.this.videoEditedInfo.originalWidth = 360;
            videoEditedInfo2.resultWidth = 360;
            VideoEditedInfo videoEditedInfo3 = InstantCameraView.this.videoEditedInfo;
            InstantCameraView.this.videoEditedInfo.originalHeight = 360;
            videoEditedInfo3.resultHeight = 360;
            InstantCameraView.this.videoEditedInfo.originalPath = this.videoFile.getAbsolutePath();
            if (i != 1) {
                setupVideoPlayer(this.videoFile);
                InstantCameraView.this.videoEditedInfo.estimatedDuration = InstantCameraView.this.recordedTime;
                NotificationCenter.getInstance(InstantCameraView.this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.audioDidSent, Integer.valueOf(InstantCameraView.this.recordingGuid), InstantCameraView.this.videoEditedInfo, this.videoFile.getAbsolutePath(), this.keyframeThumbs);
            } else if (InstantCameraView.this.delegate.isInScheduleMode()) {
                AlertsCreator.createScheduleDatePickerDialog(InstantCameraView.this.delegate.getParentActivity(), InstantCameraView.this.delegate.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda11
                    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                    public final void didSelectDate(boolean z, int i2) {
                        InstantCameraView.VideoRecorder.this.lambda$handleStopRecording$8(sendOptions, z, i2);
                    }
                }, new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        InstantCameraView.VideoRecorder.this.lambda$handleStopRecording$9();
                    }
                }, InstantCameraView.this.resourcesProvider);
            } else {
                MediaController.PhotoEntry photoEntry = new MediaController.PhotoEntry(0, 0, 0L, this.videoFile.getAbsolutePath(), 0, true, 0, 0, 0L);
                if (sendOptions != null) {
                    photoEntry.ttl = sendOptions.ttl;
                    photoEntry.effectId = sendOptions.effectId;
                }
                InstantCameraView.this.delegate.sendMedia(photoEntry, InstantCameraView.this.videoEditedInfo, sendOptions == null || sendOptions.notify, sendOptions != null ? sendOptions.scheduleDate : 0, false);
            }
        }

        public /* synthetic */ void lambda$handleStopRecording$11() {
            if (this.sentMedia && InstantCameraView.this.videoEditedInfo != null) {
                InstantCameraView.this.videoEditedInfo.notReadyYet = false;
            }
            didWriteData(this.videoFile, 0L, true);
            MediaController.getInstance().requestRecordAudioFocus(false);
        }

        public /* synthetic */ void lambda$handleStopRecording$12() {
            InstantCameraView.this.videoEncoder = null;
        }

        public /* synthetic */ void lambda$handleStopRecording$6(SendOptions sendOptions) {
            InstantCameraView.this.videoEditedInfo = new VideoEditedInfo();
            InstantCameraView.this.videoEditedInfo.startTime = -1L;
            InstantCameraView.this.videoEditedInfo.endTime = -1L;
            InstantCameraView.this.videoEditedInfo.estimatedSize = Math.max(1L, InstantCameraView.this.size);
            InstantCameraView.this.videoEditedInfo.roundVideo = true;
            InstantCameraView.this.videoEditedInfo.file = InstantCameraView.this.file;
            InstantCameraView.this.videoEditedInfo.encryptedFile = InstantCameraView.this.encryptedFile;
            InstantCameraView.this.videoEditedInfo.key = InstantCameraView.this.key;
            InstantCameraView.this.videoEditedInfo.iv = InstantCameraView.this.iv;
            InstantCameraView.this.videoEditedInfo.framerate = 25;
            VideoEditedInfo videoEditedInfo = InstantCameraView.this.videoEditedInfo;
            InstantCameraView.this.videoEditedInfo.originalWidth = 360;
            videoEditedInfo.resultWidth = 360;
            VideoEditedInfo videoEditedInfo2 = InstantCameraView.this.videoEditedInfo;
            InstantCameraView.this.videoEditedInfo.originalHeight = 360;
            videoEditedInfo2.resultHeight = 360;
            InstantCameraView.this.videoEditedInfo.originalPath = this.videoFile.getAbsolutePath();
            InstantCameraView.this.videoEditedInfo.notReadyYet = true;
            InstantCameraView.this.videoEditedInfo.thumb = InstantCameraView.this.firstFrameThumb;
            InstantCameraView.this.videoEditedInfo.estimatedDuration = InstantCameraView.this.recordedTime;
            InstantCameraView.this.firstFrameThumb = null;
            MediaController.PhotoEntry photoEntry = new MediaController.PhotoEntry(0, 0, 0L, this.videoFile.getAbsolutePath(), 0, true, 0, 0, 0L);
            if (sendOptions != null) {
                photoEntry.ttl = sendOptions.ttl;
                photoEntry.effectId = sendOptions.effectId;
            }
            InstantCameraView.this.delegate.sendMedia(photoEntry, InstantCameraView.this.videoEditedInfo, sendOptions == null || sendOptions.notify, sendOptions != null ? sendOptions.scheduleDate : 0, false);
        }

        public /* synthetic */ void lambda$handleStopRecording$7(CountDownLatch countDownLatch) {
            try {
                this.mediaMuxer.finishMovie();
            } catch (Exception e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }

        public /* synthetic */ void lambda$handleStopRecording$8(SendOptions sendOptions, boolean z, int i) {
            MediaController.PhotoEntry photoEntry = new MediaController.PhotoEntry(0, 0, 0L, this.videoFile.getAbsolutePath(), 0, true, 0, 0, 0L);
            if (sendOptions != null) {
                photoEntry.ttl = sendOptions.ttl;
                photoEntry.effectId = sendOptions.effectId;
            }
            InstantCameraView.this.delegate.sendMedia(photoEntry, InstantCameraView.this.videoEditedInfo, z || sendOptions == null || sendOptions.notify, i != 0 ? i : sendOptions != null ? sendOptions.scheduleDate : 0, false);
            InstantCameraView.this.startAnimation(false, false);
        }

        public /* synthetic */ void lambda$handleStopRecording$9() {
            InstantCameraView.this.startAnimation(false, false);
        }

        public /* synthetic */ void lambda$handleVideoFrameAvailable$2() {
            InstantCameraView.this.textureOverlayView.animate().setDuration(120L).alpha(0.0f).setInterpolator(new DecelerateInterpolator()).start();
        }

        public /* synthetic */ void lambda$handleVideoFrameAvailable$3() {
            InstantCameraView.this.textureOverlayView.animate().setDuration(120L).alpha(0.0f).setInterpolator(new DecelerateInterpolator()).start();
        }

        public /* synthetic */ void lambda$prepareEncoder$13(boolean z) {
            if (InstantCameraView.this.cancelled) {
                return;
            }
            try {
                InstantCameraView.this.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            AndroidUtilities.lockOrientation(InstantCameraView.this.delegate.getParentActivity());
            InstantCameraView instantCameraView = InstantCameraView.this;
            instantCameraView.recordPlusTime = z ? instantCameraView.recordedTime : 0L;
            InstantCameraView.this.recordStartTime = System.currentTimeMillis();
            InstantCameraView.this.recording = true;
            InstantCameraView.this.updateFlash();
            InstantCameraView.this.invalidate();
            NotificationCenter.getInstance(InstantCameraView.this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordStarted, Integer.valueOf(InstantCameraView.this.recordingGuid), Boolean.FALSE);
        }

        public /* synthetic */ void lambda$startRecording$0() {
            NotificationCenter.getInstance(InstantCameraView.this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, Integer.valueOf((int) LiteMode.FLAG_CALLS_ANIMATIONS));
        }

        public /* synthetic */ void lambda$stopRecording$1() {
            NotificationCenter.getInstance(InstantCameraView.this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, Integer.valueOf((int) LiteMode.FLAG_CALLS_ANIMATIONS));
        }

        public void prepareEncoder(final boolean z) {
            String createFragmentShader;
            String str;
            setBluetoothScoOn(true);
            try {
                int minBufferSize = AudioRecord.getMinBufferSize(48000, 16, 2);
                if (minBufferSize <= 0) {
                    minBufferSize = 3584;
                }
                int i = 49152 < minBufferSize ? ((minBufferSize / 2048) + 1) * LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM : 49152;
                this.buffers.clear();
                for (int i2 = 0; i2 < 3; i2++) {
                    this.buffers.add(new AudioBufferInfo());
                }
                if (z) {
                    this.prevVideoLast = this.videoLast + this.videoLastDt;
                    this.prevAudioLast = this.audioLast + this.audioLastDt;
                    this.firstVideoFrameSincePause = true;
                } else {
                    this.prevVideoLast = -1L;
                    this.prevAudioLast = -1L;
                    this.currentTimestamp = 0L;
                }
                this.lastTimestamp = -1L;
                this.lastCommitedFrameTime = 0L;
                this.audioStartTime = -1L;
                this.audioFirst = -1L;
                this.videoFirst = -1L;
                this.videoLast = -1L;
                this.videoDiff = -1L;
                this.audioLast = -1L;
                this.audioDiff = -1L;
                this.skippedFirst = false;
                this.skippedTime = 0L;
                AudioRecord audioRecord = new AudioRecord(0, 48000, 16, 2, i);
                this.audioRecorder = audioRecord;
                audioRecord.startRecording();
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("InstantCamera initied audio record with channels " + this.audioRecorder.getChannelCount() + " sample rate = " + this.audioRecorder.getSampleRate() + " bufferSize = " + i);
                }
                this.pauseRecorder = false;
                Thread thread = new Thread(this.recorderRunnable);
                thread.setPriority(10);
                thread.start();
                this.audioBufferInfo = new MediaCodec.BufferInfo();
                this.videoBufferInfo = new MediaCodec.BufferInfo();
                MediaFormat mediaFormat = new MediaFormat();
                mediaFormat.setString("mime", MediaController.AUDIO_MIME_TYPE);
                mediaFormat.setInteger("sample-rate", 48000);
                mediaFormat.setInteger("channel-count", 1);
                mediaFormat.setInteger("bitrate", MessagesController.getInstance(InstantCameraView.this.currentAccount).roundAudioBitrate * 1024);
                mediaFormat.setInteger("max-input-size", 20480);
                MediaCodec createEncoderByType = MediaCodec.createEncoderByType(MediaController.AUDIO_MIME_TYPE);
                this.audioEncoder = createEncoderByType;
                createEncoderByType.configure(mediaFormat, (Surface) null, (MediaCrypto) null, 1);
                this.audioEncoder.start();
                this.videoEncoder = MediaCodec.createEncoderByType(MediaController.VIDEO_MIME_TYPE);
                this.firstEncode = true;
                MediaFormat createVideoFormat = MediaFormat.createVideoFormat(MediaController.VIDEO_MIME_TYPE, this.videoWidth, this.videoHeight);
                createVideoFormat.setInteger("color-format", 2130708361);
                createVideoFormat.setInteger("bitrate", this.videoBitrate);
                createVideoFormat.setInteger("frame-rate", 30);
                createVideoFormat.setInteger("i-frame-interval", 1);
                this.videoEncoder.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
                this.surface = this.videoEncoder.createInputSurface();
                this.videoEncoder.start();
                if (!z) {
                    boolean isSdCardPath = ImageLoader.isSdCardPath(this.videoFile);
                    this.fileToWrite = this.videoFile;
                    if (isSdCardPath) {
                        File file = new File(ApplicationLoader.getFilesDirFixed(), "camera_tmp.mp4");
                        this.fileToWrite = file;
                        if (file.exists()) {
                            this.fileToWrite.delete();
                        }
                        this.writingToDifferentFile = true;
                    }
                    Mp4Movie mp4Movie = new Mp4Movie();
                    mp4Movie.setCacheFile(this.fileToWrite);
                    mp4Movie.setRotation(0);
                    mp4Movie.setSize(this.videoWidth, this.videoHeight);
                    MP4Builder createMovie = new MP4Builder().createMovie(mp4Movie, InstantCameraView.this.isSecretChat, false);
                    this.mediaMuxer = createMovie;
                    createMovie.setAllowSyncFiles(InstantCameraView.this.allowSendingWhileRecording = SharedConfig.deviceIsHigh());
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        InstantCameraView.VideoRecorder.this.lambda$prepareEncoder$13(z);
                    }
                });
                if (this.eglDisplay != EGL14.EGL_NO_DISPLAY) {
                    throw new RuntimeException("EGL already set up");
                }
                android.opengl.EGLDisplay eglGetDisplay = EGL14.eglGetDisplay(0);
                this.eglDisplay = eglGetDisplay;
                if (eglGetDisplay == EGL14.EGL_NO_DISPLAY) {
                    throw new RuntimeException("unable to get EGL14 display");
                }
                int[] iArr = new int[2];
                if (!EGL14.eglInitialize(eglGetDisplay, iArr, 0, iArr, 1)) {
                    this.eglDisplay = null;
                    throw new RuntimeException("unable to initialize EGL14");
                }
                if (this.eglContext == EGL14.EGL_NO_CONTEXT) {
                    android.opengl.EGLConfig[] eGLConfigArr = new android.opengl.EGLConfig[1];
                    if (!EGL14.eglChooseConfig(this.eglDisplay, new int[]{12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, EglBase.EGL_RECORDABLE_ANDROID, 1, 12344}, 0, eGLConfigArr, 0, 1, new int[1], 0)) {
                        throw new RuntimeException("Unable to find a suitable EGLConfig");
                    }
                    this.eglContext = EGL14.eglCreateContext(this.eglDisplay, eGLConfigArr[0], this.sharedEglContext, new int[]{12440, 2, 12344}, 0);
                    this.eglConfig = eGLConfigArr[0];
                }
                EGL14.eglQueryContext(this.eglDisplay, this.eglContext, 12440, new int[1], 0);
                if (this.eglSurface != EGL14.EGL_NO_SURFACE) {
                    throw new IllegalStateException("surface already created");
                }
                android.opengl.EGLSurface eglCreateWindowSurface = EGL14.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, this.surface, new int[]{12344}, 0);
                this.eglSurface = eglCreateWindowSurface;
                if (eglCreateWindowSurface == null) {
                    throw new RuntimeException("surface was null");
                }
                if (!EGL14.eglMakeCurrent(this.eglDisplay, eglCreateWindowSurface, eglCreateWindowSurface, this.eglContext)) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.e("eglMakeCurrent failed " + GLUtils.getEGLErrorString(EGL14.eglGetError()));
                    }
                    throw new RuntimeException("eglMakeCurrent failed");
                }
                GLES20.glBlendFunc(770, 771);
                if (InstantCameraView.this.useCamera2) {
                    str = AndroidUtilities.readRes(R.raw.instant_lanczos_vert);
                    createFragmentShader = AndroidUtilities.readRes(R.raw.instant_lanczos_frag_oes);
                } else {
                    InstantCameraView instantCameraView = InstantCameraView.this;
                    createFragmentShader = instantCameraView.createFragmentShader(instantCameraView.previewSize[0]);
                    str = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n   gl_Position = uMVPMatrix * aPosition;\n   vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
                }
                int loadShader = InstantCameraView.this.loadShader(35633, str);
                int loadShader2 = InstantCameraView.this.loadShader(35632, createFragmentShader);
                if (loadShader == 0 || loadShader2 == 0) {
                    return;
                }
                int glCreateProgram = GLES20.glCreateProgram();
                this.drawProgram = glCreateProgram;
                GLES20.glAttachShader(glCreateProgram, loadShader);
                GLES20.glAttachShader(this.drawProgram, loadShader2);
                GLES20.glLinkProgram(this.drawProgram);
                int[] iArr2 = new int[1];
                GLES20.glGetProgramiv(this.drawProgram, 35714, iArr2, 0);
                if (iArr2[0] == 0) {
                    GLES20.glDeleteProgram(this.drawProgram);
                    this.drawProgram = 0;
                    return;
                }
                this.positionHandle = GLES20.glGetAttribLocation(this.drawProgram, "aPosition");
                this.textureHandle = GLES20.glGetAttribLocation(this.drawProgram, "aTextureCoord");
                this.previewSizeHandle = GLES20.glGetUniformLocation(this.drawProgram, "preview");
                this.resolutionHandle = GLES20.glGetUniformLocation(this.drawProgram, "resolution");
                this.alphaHandle = GLES20.glGetUniformLocation(this.drawProgram, "alpha");
                this.vertexMatrixHandle = GLES20.glGetUniformLocation(this.drawProgram, "uMVPMatrix");
                this.textureMatrixHandle = GLES20.glGetUniformLocation(this.drawProgram, "uSTMatrix");
                this.texelSizeHandle = GLES20.glGetUniformLocation(this.drawProgram, "texelSize");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void setBluetoothScoOn(boolean z) {
            AudioManager audioManager = (AudioManager) ApplicationLoader.applicationContext.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
            if (SharedConfig.recordViaSco && !PermissionRequest.hasPermission("android.permission.BLUETOOTH_CONNECT")) {
                SharedConfig.recordViaSco = false;
                SharedConfig.saveConfig();
            }
            if (!(audioManager.isBluetoothScoAvailableOffCall() && SharedConfig.recordViaSco) && z) {
                return;
            }
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter != null) {
                try {
                    if (defaultAdapter.getProfileConnectionState(1) != 2) {
                    }
                    if (!z && !audioManager.isBluetoothScoOn()) {
                        audioManager.startBluetoothSco();
                        return;
                    } else if (z && audioManager.isBluetoothScoOn()) {
                        audioManager.stopBluetoothSco();
                        return;
                    }
                } catch (SecurityException unused) {
                    return;
                } catch (Throwable th) {
                    FileLog.e(th);
                    if (z) {
                        return;
                    }
                    try {
                        if (audioManager.isBluetoothScoOn()) {
                            audioManager.stopBluetoothSco();
                            return;
                        }
                        return;
                    } catch (Exception e) {
                        FileLog.e(e);
                        return;
                    }
                }
            }
            if (z) {
                return;
            }
            if (!z) {
            }
            if (z) {
            }
        }

        private void setupVideoPlayer(File file) {
            InstantCameraView.this.videoPlayer = new VideoPlayer();
            InstantCameraView.this.videoPlayer.setDelegate(new VideoPlayer.VideoPlayerDelegate() { // from class: org.telegram.ui.Components.InstantCameraView.VideoRecorder.2
                {
                    VideoRecorder.this = this;
                }

                @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                public void onError(VideoPlayer videoPlayer, Exception exc) {
                    FileLog.e(exc);
                }

                @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                public void onRenderedFirstFrame() {
                }

                @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                public /* synthetic */ void onRenderedFirstFrame(AnalyticsListener.EventTime eventTime) {
                    VideoPlayer.VideoPlayerDelegate.-CC.$default$onRenderedFirstFrame(this, eventTime);
                }

                @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                public /* synthetic */ void onSeekFinished(AnalyticsListener.EventTime eventTime) {
                    VideoPlayer.VideoPlayerDelegate.-CC.$default$onSeekFinished(this, eventTime);
                }

                @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                public /* synthetic */ void onSeekStarted(AnalyticsListener.EventTime eventTime) {
                    VideoPlayer.VideoPlayerDelegate.-CC.$default$onSeekStarted(this, eventTime);
                }

                @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                public void onStateChanged(boolean z, int i) {
                    if (InstantCameraView.this.videoPlayer != null && InstantCameraView.this.videoPlayer.isPlaying() && i == 4) {
                        InstantCameraView.this.videoPlayer.seekTo(InstantCameraView.this.videoEditedInfo.startTime > 0 ? InstantCameraView.this.videoEditedInfo.startTime : 0L);
                    }
                }

                @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
                    return false;
                }

                @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                }

                @Override // org.telegram.ui.Components.VideoPlayer.VideoPlayerDelegate
                public void onVideoSizeChanged(int i, int i2, int i3, float f) {
                }
            });
            InstantCameraView.this.videoPlayer.setTextureView(InstantCameraView.this.textureView);
            InstantCameraView.this.videoPlayer.preparePlayer(Uri.fromFile(file), "other");
            InstantCameraView.this.videoPlayer.play();
            InstantCameraView.this.videoPlayer.setMute(true);
            InstantCameraView.this.startProgressTimer();
            AnimatorSet animatorSet = new AnimatorSet();
            FlashViews.ImageViewInvertable imageViewInvertable = InstantCameraView.this.switchCameraButton;
            Property property = View.ALPHA;
            animatorSet.playTogether(ObjectAnimator.ofFloat(imageViewInvertable, property, 0.0f), ObjectAnimator.ofFloat(InstantCameraView.this.flashButton, property, 0.0f), ObjectAnimator.ofInt(InstantCameraView.this.paint, AnimationProperties.PAINT_ALPHA, 0), ObjectAnimator.ofFloat(InstantCameraView.this.muteImageView, property, 1.0f));
            animatorSet.setDuration(180L);
            animatorSet.setInterpolator(new DecelerateInterpolator());
            animatorSet.start();
            EGL14.eglDestroySurface(this.eglDisplay, this.eglSurface);
            this.eglSurface = EGL14.EGL_NO_SURFACE;
            Surface surface = this.surface;
            if (surface != null) {
                surface.release();
                this.surface = null;
            }
            android.opengl.EGLDisplay eGLDisplay = this.eglDisplay;
            if (eGLDisplay != EGL14.EGL_NO_DISPLAY) {
                android.opengl.EGLSurface eGLSurface = EGL14.EGL_NO_SURFACE;
                EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL14.EGL_NO_CONTEXT);
                EGL14.eglDestroyContext(this.eglDisplay, this.eglContext);
                EGL14.eglReleaseThread();
                EGL14.eglTerminate(this.eglDisplay);
            }
            this.eglDisplay = EGL14.EGL_NO_DISPLAY;
            this.eglContext = EGL14.EGL_NO_CONTEXT;
            this.eglConfig = null;
        }

        /* JADX WARN: Code restructure failed: missing block: B:351:0x0261, code lost:
            if (r3 != null) goto L50;
         */
        /* JADX WARN: Code restructure failed: missing block: B:361:0x0283, code lost:
            if (r3 != null) goto L50;
         */
        /* JADX WARN: Code restructure failed: missing block: B:364:0x0288, code lost:
            if (r3 != null) goto L50;
         */
        /* JADX WARN: Code restructure failed: missing block: B:365:0x028a, code lost:
            r3.releaseOutputBuffer(r2, false);
         */
        /* JADX WARN: Code restructure failed: missing block: B:367:0x0293, code lost:
            if ((r17.audioBufferInfo.flags & 4) == 0) goto L48;
         */
        /* JADX WARN: Code restructure failed: missing block: B:425:?, code lost:
            return;
         */
        /* JADX WARN: Removed duplicated region for block: B:224:0x0029  */
        /* JADX WARN: Removed duplicated region for block: B:228:0x0031  */
        /* JADX WARN: Removed duplicated region for block: B:314:0x01cb  */
        /* JADX WARN: Removed duplicated region for block: B:326:0x01eb  */
        /* JADX WARN: Removed duplicated region for block: B:394:0x01d9 A[SYNTHETIC] */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:231:0x0037 -> B:222:0x0011). Please submit an issue!!! */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void drainEncoder(boolean z) {
            ByteBuffer[] outputBuffers;
            int dequeueOutputBuffer;
            ByteBuffer byteBuffer;
            ByteBuffer byteBuffer2;
            int dequeueOutputBuffer2;
            MediaCodec mediaCodec;
            if (z) {
                this.videoEncoder.signalEndOfInputStream();
            }
            int i = 21;
            if (Build.VERSION.SDK_INT >= 21) {
                outputBuffers = null;
                while (true) {
                    dequeueOutputBuffer = this.videoEncoder.dequeueOutputBuffer(this.videoBufferInfo, 10000L);
                    if (dequeueOutputBuffer == -1) {
                        if (!z || this.pauseRecorder) {
                            break;
                        }
                        i = 21;
                    } else {
                        if (dequeueOutputBuffer != -3) {
                            if (dequeueOutputBuffer != -2) {
                                if (dequeueOutputBuffer >= 0) {
                                    ByteBuffer outputBuffer = Build.VERSION.SDK_INT < i ? outputBuffers[dequeueOutputBuffer] : this.videoEncoder.getOutputBuffer(dequeueOutputBuffer);
                                    if (outputBuffer == null) {
                                        throw new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer + " was null");
                                    }
                                    MediaCodec.BufferInfo bufferInfo = this.videoBufferInfo;
                                    int i2 = bufferInfo.size;
                                    if (i2 > 1) {
                                        int i3 = bufferInfo.flags;
                                        if ((i3 & 2) == 0) {
                                            int i4 = this.prependHeaderSize;
                                            if (i4 != 0 && (i3 & 1) != 0) {
                                                bufferInfo.offset += i4;
                                                bufferInfo.size = i2 - i4;
                                            }
                                            if (this.firstEncode && (i3 & 1) != 0) {
                                                if (bufferInfo.size > 100) {
                                                    outputBuffer.position(bufferInfo.offset);
                                                    byte[] bArr = new byte[100];
                                                    outputBuffer.get(bArr);
                                                    int i5 = 0;
                                                    int i6 = 0;
                                                    while (true) {
                                                        if (i5 >= 96) {
                                                            break;
                                                        }
                                                        if (bArr[i5] == 0 && bArr[i5 + 1] == 0 && bArr[i5 + 2] == 0 && bArr[i5 + 3] == 1 && (i6 = i6 + 1) > 1) {
                                                            MediaCodec.BufferInfo bufferInfo2 = this.videoBufferInfo;
                                                            bufferInfo2.offset += i5;
                                                            bufferInfo2.size -= i5;
                                                            break;
                                                        }
                                                        i5++;
                                                    }
                                                }
                                                this.firstEncode = false;
                                            }
                                            if (InstantCameraView.this.WRITE_TO_FILE_IN_BACKGROUND) {
                                                final MediaCodec.BufferInfo bufferInfo3 = new MediaCodec.BufferInfo();
                                                MediaCodec.BufferInfo bufferInfo4 = this.videoBufferInfo;
                                                bufferInfo3.size = bufferInfo4.size;
                                                bufferInfo3.offset = bufferInfo4.offset;
                                                bufferInfo3.flags = bufferInfo4.flags;
                                                bufferInfo3.presentationTimeUs = bufferInfo4.presentationTimeUs;
                                                final ByteBuffer cloneByteBuffer = AndroidUtilities.cloneByteBuffer(outputBuffer);
                                                this.fileWriteQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda14
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        InstantCameraView.VideoRecorder.this.lambda$drainEncoder$14(cloneByteBuffer, bufferInfo3);
                                                    }
                                                });
                                            } else {
                                                long writeSampleData = this.mediaMuxer.writeSampleData(this.videoTrackIndex, outputBuffer, this.videoBufferInfo, true);
                                                if (writeSampleData != 0 && !this.writingToDifferentFile && InstantCameraView.this.allowSendingWhileRecording) {
                                                    didWriteData(this.videoFile, writeSampleData, false);
                                                }
                                            }
                                        } else if (this.videoTrackIndex == -5) {
                                            byte[] bArr2 = new byte[i2];
                                            outputBuffer.limit(bufferInfo.offset + i2);
                                            outputBuffer.position(this.videoBufferInfo.offset);
                                            outputBuffer.get(bArr2);
                                            for (int i7 = this.videoBufferInfo.size - 1; i7 >= 0 && i7 > 3; i7--) {
                                                if (bArr2[i7] == 1 && bArr2[i7 - 1] == 0 && bArr2[i7 - 2] == 0) {
                                                    int i8 = i7 - 3;
                                                    if (bArr2[i8] == 0) {
                                                        byteBuffer = ByteBuffer.allocate(i8);
                                                        byteBuffer2 = ByteBuffer.allocate(this.videoBufferInfo.size - i8);
                                                        byteBuffer.put(bArr2, 0, i8).position(0);
                                                        byteBuffer2.put(bArr2, i8, this.videoBufferInfo.size - i8).position(0);
                                                        break;
                                                    }
                                                }
                                            }
                                            byteBuffer = null;
                                            byteBuffer2 = null;
                                            MediaFormat createVideoFormat = MediaFormat.createVideoFormat(MediaController.VIDEO_MIME_TYPE, this.videoWidth, this.videoHeight);
                                            if (byteBuffer != null && byteBuffer2 != null) {
                                                createVideoFormat.setByteBuffer("csd-0", byteBuffer);
                                                createVideoFormat.setByteBuffer("csd-1", byteBuffer2);
                                            }
                                            this.videoTrackIndex = this.mediaMuxer.addTrack(createVideoFormat, false);
                                        }
                                    }
                                    this.videoEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                                    if ((this.videoBufferInfo.flags & 4) != 0) {
                                        break;
                                    }
                                } else {
                                    continue;
                                }
                            } else {
                                MediaFormat outputFormat = this.videoEncoder.getOutputFormat();
                                if (this.videoTrackIndex == -5) {
                                    this.videoTrackIndex = this.mediaMuxer.addTrack(outputFormat, false);
                                    if (outputFormat.containsKey("prepend-sps-pps-to-idr-frames") && outputFormat.getInteger("prepend-sps-pps-to-idr-frames") == 1) {
                                        this.prependHeaderSize = outputFormat.getByteBuffer("csd-0").limit() + outputFormat.getByteBuffer("csd-1").limit();
                                    }
                                }
                            }
                        } else {
                            if (Build.VERSION.SDK_INT < i) {
                                break;
                            }
                        }
                        i = 21;
                    }
                }
                if (Build.VERSION.SDK_INT < 21) {
                    outputBuffers = this.audioEncoder.getOutputBuffers();
                }
                while (true) {
                    dequeueOutputBuffer2 = this.audioEncoder.dequeueOutputBuffer(this.audioBufferInfo, 0L);
                    if (dequeueOutputBuffer2 != -1) {
                        if (!z) {
                            return;
                        }
                        if ((!this.running && this.sendWhenDone == 0) || this.pauseRecorder) {
                            return;
                        }
                    } else if (dequeueOutputBuffer2 == -3) {
                        if (Build.VERSION.SDK_INT < 21) {
                            outputBuffers = this.audioEncoder.getOutputBuffers();
                        }
                    } else if (dequeueOutputBuffer2 == -2) {
                        MediaFormat outputFormat2 = this.audioEncoder.getOutputFormat();
                        if (this.audioTrackIndex == -5) {
                            this.audioTrackIndex = this.mediaMuxer.addTrack(outputFormat2, true);
                        }
                    } else if (dequeueOutputBuffer2 < 0) {
                        continue;
                    } else {
                        ByteBuffer outputBuffer2 = Build.VERSION.SDK_INT < 21 ? outputBuffers[dequeueOutputBuffer2] : this.audioEncoder.getOutputBuffer(dequeueOutputBuffer2);
                        if (outputBuffer2 == null) {
                            throw new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer2 + " was null");
                        }
                        MediaCodec.BufferInfo bufferInfo5 = this.audioBufferInfo;
                        if ((bufferInfo5.flags & 2) != 0) {
                            bufferInfo5.size = 0;
                        }
                        if (bufferInfo5.size == 0) {
                            mediaCodec = this.audioEncoder;
                        } else if (InstantCameraView.this.WRITE_TO_FILE_IN_BACKGROUND) {
                            final MediaCodec.BufferInfo bufferInfo6 = new MediaCodec.BufferInfo();
                            MediaCodec.BufferInfo bufferInfo7 = this.audioBufferInfo;
                            bufferInfo6.size = bufferInfo7.size;
                            bufferInfo6.offset = bufferInfo7.offset;
                            bufferInfo6.flags = bufferInfo7.flags;
                            bufferInfo6.presentationTimeUs = bufferInfo7.presentationTimeUs;
                            final ByteBuffer cloneByteBuffer2 = AndroidUtilities.cloneByteBuffer(outputBuffer2);
                            this.fileWriteQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda15
                                @Override // java.lang.Runnable
                                public final void run() {
                                    InstantCameraView.VideoRecorder.this.lambda$drainEncoder$15(cloneByteBuffer2, bufferInfo6);
                                }
                            });
                            mediaCodec = this.audioEncoder;
                        } else {
                            long writeSampleData2 = this.mediaMuxer.writeSampleData(this.audioTrackIndex, outputBuffer2, bufferInfo5, false);
                            if (writeSampleData2 != 0 && !this.writingToDifferentFile && InstantCameraView.this.allowSendingWhileRecording) {
                                didWriteData(this.videoFile, writeSampleData2, false);
                            }
                            mediaCodec = this.audioEncoder;
                        }
                    }
                }
            }
            outputBuffers = this.videoEncoder.getOutputBuffers();
            while (true) {
                dequeueOutputBuffer = this.videoEncoder.dequeueOutputBuffer(this.videoBufferInfo, 10000L);
                if (dequeueOutputBuffer == -1) {
                }
                i = 21;
            }
            if (Build.VERSION.SDK_INT < 21) {
            }
            while (true) {
                dequeueOutputBuffer2 = this.audioEncoder.dequeueOutputBuffer(this.audioBufferInfo, 0L);
                if (dequeueOutputBuffer2 != -1) {
                }
            }
        }

        protected void finalize() {
            DispatchQueue dispatchQueue = this.fileWriteQueue;
            if (dispatchQueue != null) {
                dispatchQueue.recycle();
                this.fileWriteQueue = null;
            }
            try {
                android.opengl.EGLDisplay eGLDisplay = this.eglDisplay;
                if (eGLDisplay != EGL14.EGL_NO_DISPLAY) {
                    android.opengl.EGLSurface eGLSurface = EGL14.EGL_NO_SURFACE;
                    EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL14.EGL_NO_CONTEXT);
                    EGL14.eglDestroyContext(this.eglDisplay, this.eglContext);
                    EGL14.eglReleaseThread();
                    EGL14.eglTerminate(this.eglDisplay);
                    this.eglDisplay = EGL14.EGL_NO_DISPLAY;
                    this.eglContext = EGL14.EGL_NO_CONTEXT;
                    this.eglConfig = null;
                }
            } finally {
                super.finalize();
            }
        }

        public void frameAvailable(SurfaceTexture surfaceTexture, Integer num, long j) {
            synchronized (this.sync) {
                try {
                    if (this.ready) {
                        long timestamp = surfaceTexture.getTimestamp();
                        if (timestamp == 0) {
                            int i = this.zeroTimeStamps + 1;
                            this.zeroTimeStamps = i;
                            if (i <= 1) {
                                return;
                            }
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("InstantCamera fix timestamp enabled");
                            }
                        } else {
                            this.zeroTimeStamps = 0;
                            j = timestamp;
                        }
                        this.prevTimestamp = j;
                        this.handler.sendMessage(this.handler.obtainMessage(2, (int) (j >> 32), (int) j, num));
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void pause() {
            this.handler.sendMessage(this.handler.obtainMessage(4));
        }

        public void resume() {
            this.handler.sendMessage(this.handler.obtainMessage(5));
        }

        @Override // java.lang.Runnable
        public void run() {
            Looper.prepare();
            synchronized (this.sync) {
                this.handler = new EncoderHandler(this);
                this.ready = true;
                this.sync.notify();
            }
            Looper.loop();
            synchronized (this.sync) {
                this.ready = false;
            }
        }

        public void startRecording(File file, android.opengl.EGLContext eGLContext) {
            InstantCameraView.access$5608();
            if (this.started && this.handler != null && this.handler.getLooper() != null && this.handler.getLooper().getThread() != null && this.handler.getLooper().getThread().isAlive()) {
                this.sharedEglContext = eGLContext;
                this.handler.sendMessage(this.handler.obtainMessage(0, 1, 0));
            }
            this.started = true;
            int i = MessagesController.getInstance(InstantCameraView.this.currentAccount).roundVideoSize;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    InstantCameraView.VideoRecorder.this.lambda$startRecording$0();
                }
            });
            this.videoFile = file;
            this.videoWidth = i;
            this.videoHeight = i;
            this.videoBitrate = MessagesController.getInstance(InstantCameraView.this.currentAccount).roundVideoBitrate * 1024;
            this.sharedEglContext = eGLContext;
            synchronized (this.sync) {
                try {
                    if (this.running) {
                        return;
                    }
                    this.running = true;
                    Thread thread = new Thread(this, "TextureMovieEncoder");
                    thread.setPriority(10);
                    thread.start();
                    while (!this.ready) {
                        try {
                            this.sync.wait();
                        } catch (InterruptedException unused) {
                        }
                    }
                    if (InstantCameraView.this.WRITE_TO_FILE_IN_BACKGROUND) {
                        DispatchQueue dispatchQueue = new DispatchQueue("IVR_FileWriteQueue");
                        this.fileWriteQueue = dispatchQueue;
                        dispatchQueue.setPriority(10);
                    }
                    this.keyframeThumbs.clear();
                    this.frameCount = 0;
                    DispatchQueue dispatchQueue2 = this.generateKeyframeThumbsQueue;
                    if (dispatchQueue2 != null) {
                        dispatchQueue2.cleanupQueue();
                        this.generateKeyframeThumbsQueue.recycle();
                    }
                    this.generateKeyframeThumbsQueue = new DispatchQueue("keyframes_thumb_queue");
                    this.handler.sendMessage(this.handler.obtainMessage(0));
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void stopRecording(int i, SendOptions sendOptions) {
            this.handler.sendMessage(this.handler.obtainMessage(1, i, 0, sendOptions));
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$VideoRecorder$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    InstantCameraView.VideoRecorder.this.lambda$stopRecording$1();
                }
            });
        }
    }

    public InstantCameraView(Context context, Delegate delegate, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.isFrontface = true;
        this.position = new int[2];
        this.cameraTexture = new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE};
        this.oldCameraTexture = new int[1];
        this.cameraTextureAlpha = 1.0f;
        this.previewSize = new org.telegram.messenger.camera.Size[2];
        this.aspectRatio = SharedConfig.roundCamera16to9 ? new org.telegram.messenger.camera.Size(16, 9) : new org.telegram.messenger.camera.Size(4, 3);
        int i = Build.VERSION.SDK_INT;
        this.useCamera2 = i >= 21 && SharedConfig.isUsingCamera2(this.currentAccount);
        this.camera2Sessions = new Camera2Session[2];
        this.mMVPMatrix = new float[16];
        this.mSTMatrix = new float[16];
        this.moldSTMatrix = new float[16];
        this.drawBlur = true;
        this.WRITE_TO_FILE_IN_BACKGROUND = false;
        this.resourcesProvider = resourcesProvider;
        this.parentView = delegate.getFragmentView();
        setWillNotDraw(false);
        this.delegate = delegate;
        this.recordingGuid = delegate.getClassGuid();
        this.isSecretChat = delegate.isSecretChat();
        Paint paint = new Paint(1) { // from class: org.telegram.ui.Components.InstantCameraView.1
            {
                InstantCameraView.this = this;
            }

            @Override // android.graphics.Paint
            public void setAlpha(int i2) {
                super.setAlpha(i2);
                InstantCameraView.this.invalidate();
            }
        };
        this.paint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setStrokeWidth(AndroidUtilities.dp(3.0f));
        this.paint.setColor(-1);
        this.rect = new RectF();
        FlashViews flashViews = new FlashViews(getContext(), null, this, null);
        this.flashViews = flashViews;
        flashViews.setWarmth(0.5f);
        addView(flashViews.backgroundView, LayoutHelper.createFrame(-1, -1, 119));
        if (i >= 21) {
            InstantViewCameraContainer instantViewCameraContainer = new InstantViewCameraContainer(context) { // from class: org.telegram.ui.Components.InstantCameraView.2
                {
                    InstantCameraView.this = this;
                }

                @Override // android.view.View
                public void setAlpha(float f) {
                    super.setAlpha(f);
                    InstantCameraView.this.invalidate();
                }

                @Override // android.view.View
                public void setRotationY(float f) {
                    super.setRotationY(f);
                    InstantCameraView.this.invalidate();
                }
            };
            this.cameraContainer = instantViewCameraContainer;
            instantViewCameraContainer.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.Components.InstantCameraView.3
                {
                    InstantCameraView.this = this;
                }

                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, InstantCameraView.this.textureViewSize, InstantCameraView.this.textureViewSize);
                }
            });
            this.cameraContainer.setClipToOutline(true);
            this.cameraContainer.setWillNotDraw(false);
        } else {
            final Path path = new Path();
            final Paint paint2 = new Paint(1);
            paint2.setColor(-16777216);
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            InstantViewCameraContainer instantViewCameraContainer2 = new InstantViewCameraContainer(context) { // from class: org.telegram.ui.Components.InstantCameraView.4
                {
                    InstantCameraView.this = this;
                }

                @Override // org.telegram.ui.Components.InstantCameraView.InstantViewCameraContainer, android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    try {
                        super.dispatchDraw(canvas);
                        canvas.drawPath(path, paint2);
                    } catch (Exception unused) {
                    }
                }

                @Override // android.view.View
                protected void onSizeChanged(int i2, int i3, int i4, int i5) {
                    super.onSizeChanged(i2, i3, i4, i5);
                    path.reset();
                    float f = i2 / 2;
                    path.addCircle(f, i3 / 2, f, Path.Direction.CW);
                    path.toggleInverseFillType();
                }

                @Override // android.view.View
                public void setRotationY(float f) {
                    super.setRotationY(f);
                    InstantCameraView.this.invalidate();
                }
            };
            this.cameraContainer = instantViewCameraContainer2;
            instantViewCameraContainer2.setWillNotDraw(false);
            this.cameraContainer.setLayerType(2, null);
        }
        View view = this.cameraContainer;
        int i2 = AndroidUtilities.roundPlayingMessageSize;
        addView(view, new FrameLayout.LayoutParams(i2, i2, 17));
        addView(flashViews.foregroundView, LayoutHelper.createFrame(-1, -1, 119));
        FlashViews.ImageViewInvertable imageViewInvertable = new FlashViews.ImageViewInvertable(context);
        this.switchCameraButton = imageViewInvertable;
        ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER;
        imageViewInvertable.setScaleType(scaleType);
        imageViewInvertable.setContentDescription(LocaleController.getString(R.string.AccDescrSwitchCamera));
        addView(imageViewInvertable, LayoutHelper.createFrame(62, 62.0f, 83, 8.0f, 0.0f, 0.0f, 0.0f));
        imageViewInvertable.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.InstantCameraView$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                InstantCameraView.this.lambda$new$1(view2);
            }
        });
        FlashViews.ImageViewInvertable imageViewInvertable2 = new FlashViews.ImageViewInvertable(context);
        this.flashButton = imageViewInvertable2;
        imageViewInvertable2.setScaleType(scaleType);
        addView(imageViewInvertable2, LayoutHelper.createFrame(62, 62.0f, 83, 58.0f, 0.0f, 0.0f, 0.0f));
        imageViewInvertable2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.InstantCameraView$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                InstantCameraView.this.lambda$new$2(view2);
            }
        });
        updateFlash();
        flashViews.add(imageViewInvertable);
        flashViews.add(imageViewInvertable2);
        ImageView imageView = new ImageView(context);
        this.muteImageView = imageView;
        imageView.setScaleType(scaleType);
        this.muteImageView.setImageResource(R.drawable.video_mute);
        this.muteImageView.setAlpha(0.0f);
        addView(this.muteImageView, LayoutHelper.createFrame(48, 48, 17));
        final Paint paint3 = new Paint(1);
        paint3.setColor(ColorUtils.setAlphaComponent(-16777216, 40));
        BackupImageView backupImageView = new BackupImageView(getContext()) { // from class: org.telegram.ui.Components.InstantCameraView.7
            CellFlickerDrawable flickerDrawable = new CellFlickerDrawable();

            {
                InstantCameraView.this = this;
            }

            @Override // org.telegram.ui.Components.BackupImageView, android.view.View
            public void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                if (InstantCameraView.this.needDrawFlickerStub) {
                    this.flickerDrawable.setParentWidth(InstantCameraView.this.textureViewSize);
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(0.0f, 0.0f, InstantCameraView.this.textureViewSize, InstantCameraView.this.textureViewSize);
                    float width = rectF.width() / 2.0f;
                    canvas.drawRoundRect(rectF, width, width, paint3);
                    rectF.inset(AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f));
                    this.flickerDrawable.draw(canvas, rectF, width, null);
                    invalidate();
                }
            }
        };
        this.textureOverlayView = backupImageView;
        int i3 = AndroidUtilities.roundPlayingMessageSize;
        addView(backupImageView, new FrameLayout.LayoutParams(i3, i3, 17));
        this.setVisibilityFromPause = false;
        setVisibility(4);
        this.blurBehindDrawable = new BlurBehindDrawable(this.parentView, this, 0, resourcesProvider);
    }

    static /* synthetic */ float access$3516(InstantCameraView instantCameraView, float f) {
        float f2 = instantCameraView.cameraTextureAlpha + f;
        instantCameraView.cameraTextureAlpha = f2;
        return f2;
    }

    static /* synthetic */ int access$5608() {
        int i = A;
        A = i + 1;
        return i;
    }

    private boolean allowBigSizeCamera() {
        if (SharedConfig.bigCameraForRound || SharedConfig.deviceIsAboveAverage() || Math.max(SharedConfig.getDevicePerformanceClass(), SharedConfig.getLegacyDevicePerformanceClass()) == 2) {
            return true;
        }
        int hashCode = (Build.MANUFACTURER + " " + Build.DEVICE).toUpperCase().hashCode();
        int i = 0;
        while (true) {
            int[] iArr = ALLOW_BIG_CAMERA_WHITELIST;
            if (i >= iArr.length) {
                return false;
            }
            if (iArr[i] == hashCode) {
                return true;
            }
            i++;
        }
    }

    public static boolean allowBigSizeCameraDebug() {
        if (Math.max(SharedConfig.getDevicePerformanceClass(), SharedConfig.getLegacyDevicePerformanceClass()) == 2) {
            return true;
        }
        int hashCode = (Build.MANUFACTURER + " " + Build.DEVICE).toUpperCase().hashCode();
        int i = 0;
        while (true) {
            int[] iArr = ALLOW_BIG_CAMERA_WHITELIST;
            if (i >= iArr.length) {
                return false;
            }
            if (iArr[i] == hashCode) {
                return true;
            }
            i++;
        }
    }

    private boolean checkPointerIds(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() < 2) {
            return false;
        }
        if (this.pointerId1 == motionEvent.getPointerId(0) && this.pointerId2 == motionEvent.getPointerId(1)) {
            return true;
        }
        return this.pointerId1 == motionEvent.getPointerId(1) && this.pointerId2 == motionEvent.getPointerId(0);
    }

    private org.telegram.messenger.camera.Size chooseOptimalSize(ArrayList arrayList) {
        ArrayList arrayList2 = new ArrayList();
        int i = Build.MANUFACTURER.equalsIgnoreCase("Samsung") ? 1200 : allowBigSizeCamera() ? 1440 : 1200;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if (Math.max(((org.telegram.messenger.camera.Size) arrayList.get(i2)).mHeight, ((org.telegram.messenger.camera.Size) arrayList.get(i2)).mWidth) <= i && Math.min(((org.telegram.messenger.camera.Size) arrayList.get(i2)).mHeight, ((org.telegram.messenger.camera.Size) arrayList.get(i2)).mWidth) >= 320) {
                arrayList2.add((org.telegram.messenger.camera.Size) arrayList.get(i2));
            }
        }
        if (!arrayList2.isEmpty() && allowBigSizeCamera()) {
            Collections.sort(arrayList2, new Comparator() { // from class: org.telegram.ui.Components.InstantCameraView$$ExternalSyntheticLambda1
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$chooseOptimalSize$4;
                    lambda$chooseOptimalSize$4 = InstantCameraView.lambda$chooseOptimalSize$4((org.telegram.messenger.camera.Size) obj, (org.telegram.messenger.camera.Size) obj2);
                    return lambda$chooseOptimalSize$4;
                }
            });
            return (org.telegram.messenger.camera.Size) arrayList2.get(0);
        }
        if (!arrayList2.isEmpty()) {
            arrayList = arrayList2;
        }
        return Build.MANUFACTURER.equalsIgnoreCase("Xiaomi") ? CameraController.chooseOptimalSize(arrayList, 640, 480, this.aspectRatio, false) : CameraController.chooseOptimalSize(arrayList, 480, NotificationCenter.onActivityResultReceived, this.aspectRatio, false);
    }

    public void createCamera(final int i, final SurfaceTexture surfaceTexture) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                InstantCameraView.this.lambda$createCamera$7(i, surfaceTexture);
            }
        });
    }

    public String createFragmentShader(org.telegram.messenger.camera.Size size) {
        return (SharedConfig.deviceIsLow() || !allowBigSizeCamera()) ? "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform float alpha;\nuniform vec2 preview;\nuniform vec2 resolution;\nuniform samplerExternalOES sTexture;\nvoid main() {\n   vec4 textColor = texture2D(sTexture, vTextureCoord);\n   vec2 coord = resolution * 0.5;\n   float radius = 0.51 * resolution.x;\n   float d = length(coord - gl_FragCoord.xy) - radius;\n   float t = clamp(d, 0.0, 1.0);\n   vec3 color = mix(textColor.rgb, vec3(1, 1, 1), t);\n   gl_FragColor = vec4(color * alpha, alpha);\n}\n" : (size == null || ((float) Math.max(size.getHeight(), size.getWidth())) * 0.7f >= ((float) MessagesController.getInstance(this.currentAccount).roundVideoSize)) ? "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform vec2 resolution;\nuniform vec2 preview;\nuniform float alpha;\nuniform samplerExternalOES sTexture;\nvoid main() {\n   vec2 coord = resolution * 0.5;\n   float radius = 0.51 * resolution.x;\n   float d = length(coord - gl_FragCoord.xy) - radius;\n   float t = clamp(d, 0.0, 1.0);\n   if (t == 0.0) {\n       vec2 c_textureSize = preview;\n       vec2 c_onePixel = (1.0 / c_textureSize);\n       vec2 uv = vTextureCoord;\n       vec2 pixel = uv * c_textureSize + 0.5;\n       vec2 frac = fract(pixel);\n       pixel = (floor(pixel) / c_textureSize) - vec2(c_onePixel);\n       vec4 tl = texture2D(sTexture, pixel + vec2(0.0         , 0.0));\n       vec4 tr = texture2D(sTexture, pixel + vec2(c_onePixel.x, 0.0));\n       vec4 bl = texture2D(sTexture, pixel + vec2(0.0         , c_onePixel.y));\n       vec4 br = texture2D(sTexture, pixel + vec2(c_onePixel.x, c_onePixel.y));\n       vec4 x1 = mix(tl, tr, frac.x);\n       vec4 x2 = mix(bl, br, frac.x);\n       gl_FragColor = mix(x1, x2, frac.y) * alpha;   } else {\n       gl_FragColor = vec4(1, 1, 1, alpha);\n   }\n}\n" : "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform float alpha;\nuniform vec2 preview;\nuniform vec2 resolution;\nuniform samplerExternalOES sTexture;\nvoid main() {\n   vec4 textColor = texture2D(sTexture, vTextureCoord);\n   vec2 coord = resolution * 0.5;\n   float radius = 0.51 * resolution.x;\n   float d = length(coord - gl_FragCoord.xy) - radius;\n   float t = clamp(d, 0.0, 1.0);\n   vec3 color = mix(textColor.rgb, vec3(1, 1, 1), t);\n   gl_FragColor = vec4(color * alpha, alpha);\n}\n";
    }

    private boolean initCamera() {
        int i;
        int i2;
        if (this.useCamera2) {
            return true;
        }
        ArrayList<CameraInfo> cameras = CameraController.getInstance().getCameras();
        if (cameras == null) {
            return false;
        }
        CameraInfo cameraInfo = null;
        int i3 = 0;
        while (i3 < cameras.size()) {
            CameraInfo cameraInfo2 = cameras.get(i3);
            if (!cameraInfo2.isFrontface()) {
                cameraInfo = cameraInfo2;
            }
            if ((this.isFrontface && cameraInfo2.isFrontface()) || (!this.isFrontface && !cameraInfo2.isFrontface())) {
                this.selectedCamera = cameraInfo2;
                break;
            }
            i3++;
            cameraInfo = cameraInfo2;
        }
        if (this.selectedCamera == null) {
            this.selectedCamera = cameraInfo;
        }
        CameraInfo cameraInfo3 = this.selectedCamera;
        if (cameraInfo3 == null) {
            return false;
        }
        ArrayList<org.telegram.messenger.camera.Size> previewSizes = cameraInfo3.getPreviewSizes();
        ArrayList<org.telegram.messenger.camera.Size> pictureSizes = this.selectedCamera.getPictureSizes();
        this.previewSize[0] = chooseOptimalSize(previewSizes);
        org.telegram.messenger.camera.Size chooseOptimalSize = chooseOptimalSize(pictureSizes);
        this.pictureSize = chooseOptimalSize;
        if (this.previewSize[0].mWidth != chooseOptimalSize.mWidth) {
            boolean z = false;
            for (int size = previewSizes.size() - 1; size >= 0; size--) {
                org.telegram.messenger.camera.Size size2 = previewSizes.get(size);
                int size3 = pictureSizes.size() - 1;
                while (true) {
                    if (size3 < 0) {
                        break;
                    }
                    org.telegram.messenger.camera.Size size4 = pictureSizes.get(size3);
                    int i4 = size2.mWidth;
                    org.telegram.messenger.camera.Size size5 = this.pictureSize;
                    if (i4 >= size5.mWidth && (i2 = size2.mHeight) >= size5.mHeight && i4 == size4.mWidth && i2 == size4.mHeight) {
                        this.previewSize[0] = size2;
                        this.pictureSize = size4;
                        z = true;
                        break;
                    }
                    size3--;
                }
                if (z) {
                    break;
                }
            }
            if (!z) {
                for (int size6 = previewSizes.size() - 1; size6 >= 0; size6--) {
                    org.telegram.messenger.camera.Size size7 = previewSizes.get(size6);
                    int size8 = pictureSizes.size() - 1;
                    while (true) {
                        if (size8 < 0) {
                            break;
                        }
                        org.telegram.messenger.camera.Size size9 = pictureSizes.get(size8);
                        int i5 = size7.mWidth;
                        if (i5 >= 360 && (i = size7.mHeight) >= 360 && i5 == size9.mWidth && i == size9.mHeight) {
                            this.previewSize[0] = size7;
                            this.pictureSize = size9;
                            z = true;
                            break;
                        }
                        size8--;
                    }
                    if (z) {
                        break;
                    }
                }
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("InstantCamera preview w = " + this.previewSize[0].mWidth + " h = " + this.previewSize[0].mHeight);
        }
        return true;
    }

    private boolean isCameraSessionInitiated() {
        if (this.useCamera2) {
            Camera2Session camera2Session = this.camera2SessionCurrent;
            return camera2Session != null && camera2Session.isInitiated();
        }
        CameraSession cameraSession = this.cameraSession;
        return cameraSession != null && cameraSession.isInitied();
    }

    public static /* synthetic */ int lambda$chooseOptimalSize$4(org.telegram.messenger.camera.Size size, org.telegram.messenger.camera.Size size2) {
        float abs = Math.abs(1.0f - (Math.min(size.mHeight, size.mWidth) / Math.max(size.mHeight, size.mWidth)));
        float abs2 = Math.abs(1.0f - (Math.min(size2.mHeight, size2.mWidth) / Math.max(size2.mHeight, size2.mWidth)));
        if (abs < abs2) {
            return -1;
        }
        return abs > abs2 ? 1 : 0;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(14:3|4|5|(11:7|(1:9)|10|11|(6:13|(1:15)|16|(1:18)|19|(1:26)(2:23|24))|28|29|16|(0)|19|(2:21|26)(1:27))|34|10|11|(0)|28|29|16|(0)|19|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0081, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00b6, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0076 A[Catch: Exception -> 0x0081, TryCatch #1 {Exception -> 0x0081, blocks: (B:56:0x0066, B:58:0x0076, B:63:0x0083), top: B:77:0x0066 }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:80:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createCamera$5() {
        CameraGLThread cameraGLThread;
        Camera.Size currentPictureSize;
        Camera.Size currentPreviewSize;
        if (this.cameraSession == null) {
            return;
        }
        updateFlash();
        boolean z = false;
        try {
            currentPreviewSize = this.cameraSession.getCurrentPreviewSize();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (currentPreviewSize.width == this.previewSize[0].getWidth()) {
            if (currentPreviewSize.height != this.previewSize[0].getHeight()) {
            }
            currentPictureSize = this.cameraSession.getCurrentPictureSize();
            if (currentPictureSize.width == this.pictureSize.getWidth()) {
                if (currentPictureSize.height == this.pictureSize.getHeight()) {
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("InstantCamera camera initied");
                }
                this.cameraSession.setInitied();
                if (!z || (cameraGLThread = this.cameraThread) == null) {
                    return;
                }
                cameraGLThread.reinitForNewCamera();
                return;
            }
            this.pictureSize = new org.telegram.messenger.camera.Size(currentPictureSize.width, currentPictureSize.height);
            FileLog.d("InstantCamera change picture size to w = " + this.pictureSize.getWidth() + " h = " + this.pictureSize.getHeight());
            z = true;
            if (BuildVars.LOGS_ENABLED) {
            }
            this.cameraSession.setInitied();
            if (z) {
                return;
            }
            return;
        }
        this.previewSize[0] = new org.telegram.messenger.camera.Size(currentPreviewSize.width, currentPreviewSize.height);
        FileLog.d("InstantCamera change preview size to w = " + this.previewSize[0].getWidth() + " h = " + this.previewSize[0].getHeight());
        currentPictureSize = this.cameraSession.getCurrentPictureSize();
        if (currentPictureSize.width == this.pictureSize.getWidth()) {
        }
        this.pictureSize = new org.telegram.messenger.camera.Size(currentPictureSize.width, currentPictureSize.height);
        FileLog.d("InstantCamera change picture size to w = " + this.pictureSize.getWidth() + " h = " + this.pictureSize.getHeight());
        z = true;
        if (BuildVars.LOGS_ENABLED) {
        }
        this.cameraSession.setInitied();
        if (z) {
        }
    }

    public /* synthetic */ void lambda$createCamera$6() {
        CameraGLThread cameraGLThread = this.cameraThread;
        if (cameraGLThread != null) {
            cameraGLThread.setCurrentSession(this.cameraSession);
        }
    }

    public /* synthetic */ void lambda$createCamera$7(int i, SurfaceTexture surfaceTexture) {
        Camera2Session camera2Session;
        if (this.cameraThread == null) {
            return;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("InstantCamera create camera session " + i);
        }
        if (!this.useCamera2) {
            if (i == 1) {
                return;
            }
            surfaceTexture.setDefaultBufferSize(this.previewSize[0].getWidth(), this.previewSize[0].getHeight());
            this.cameraSession = new CameraSession(this.selectedCamera, this.previewSize[0], this.pictureSize, 256, true);
            updateFlash();
            this.cameraThread.setCurrentSession(this.cameraSession);
            CameraController.getInstance().openRound(this.cameraSession, surfaceTexture, new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    InstantCameraView.this.lambda$createCamera$5();
                }
            }, new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    InstantCameraView.this.lambda$createCamera$6();
                }
            });
            return;
        }
        if (this.bothCameras) {
            camera2Session = this.camera2Sessions[i];
            if (camera2Session == null) {
                return;
            }
        } else if (i == 1) {
            return;
        } else {
            this.cameraThread.setCurrentSession(this.camera2SessionCurrent);
            camera2Session = this.camera2SessionCurrent;
        }
        camera2Session.open(surfaceTexture);
    }

    public /* synthetic */ void lambda$finishZoom$8(ValueAnimator valueAnimator) {
        if (this.useCamera2) {
            Camera2Session camera2Session = this.camera2SessionCurrent;
            if (camera2Session != null) {
                camera2Session.setZoom(((Float) valueAnimator.getAnimatedValue()).floatValue());
                return;
            }
            return;
        }
        CameraSession cameraSession = this.cameraSession;
        if (cameraSession != null) {
            cameraSession.setZoom(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    public /* synthetic */ void lambda$new$0() {
        if (this.bothCameras) {
            switchCamera();
        }
    }

    public /* synthetic */ void lambda$new$1(View view) {
        if (this.cameraReady && isCameraSessionInitiated() && this.cameraThread != null) {
            if (!this.bothCameras) {
                switchCamera();
            }
            RLottieDrawable rLottieDrawable = this.switchCameraDrawable;
            if (rLottieDrawable != null) {
                rLottieDrawable.setCurrentFrame(0);
                this.switchCameraDrawable.start();
            }
            this.flipAnimationInProgress = true;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.setDuration(580L);
            ofFloat.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            final boolean[] zArr = new boolean[1];
            final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.InstantCameraView$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    InstantCameraView.this.lambda$new$0();
                }
            };
            InstantViewCameraContainer instantViewCameraContainer = this.cameraContainer;
            instantViewCameraContainer.setCameraDistance(instantViewCameraContainer.getMeasuredHeight() * 8.0f);
            BackupImageView backupImageView = this.textureOverlayView;
            backupImageView.setCameraDistance(backupImageView.getMeasuredHeight() * 8.0f);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.InstantCameraView.5
                {
                    InstantCameraView.this = this;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    if (floatValue > 0.5f) {
                        boolean[] zArr2 = zArr;
                        if (!zArr2[0]) {
                            zArr2[0] = true;
                            runnable.run();
                        }
                    }
                    if (floatValue >= 0.5f) {
                        floatValue -= 1.0f;
                    }
                    float f = floatValue * 180.0f;
                    InstantCameraView.this.cameraContainer.setRotationY(f);
                    InstantCameraView.this.textureOverlayView.setRotationY(f);
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.InstantCameraView.6
                {
                    InstantCameraView.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    boolean[] zArr2 = zArr;
                    if (!zArr2[0]) {
                        zArr2[0] = true;
                        runnable.run();
                    }
                    InstantCameraView.this.cameraContainer.setRotationY(0.0f);
                    InstantCameraView.this.textureOverlayView.setRotationY(0.0f);
                    InstantCameraView.this.flipAnimationInProgress = false;
                    InstantCameraView.this.invalidate();
                }
            });
            ofFloat.start();
        }
    }

    public /* synthetic */ void lambda$new$2(View view) {
        this.flashing = !this.flashing;
        updateFlash();
    }

    public /* synthetic */ void lambda$startAnimation$3(boolean z, ValueAnimator valueAnimator) {
        this.animationTranslationY = z ? 0.0f : (getMeasuredHeight() / 2.0f) * ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateTranslationY();
    }

    public int loadShader(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        int[] iArr = new int[1];
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] == 0) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e(GLES20.glGetShaderInfoLog(glCreateShader));
            }
            GLES20.glDeleteShader(glCreateShader);
            return 0;
        }
        return glCreateShader;
    }

    private void saveLastCameraBitmap() {
        Bitmap bitmap = this.textureView.getBitmap();
        if (bitmap == null || bitmap.getPixel(0, 0) == 0) {
            return;
        }
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(this.textureView.getBitmap(), 50, 50, true);
        this.lastBitmap = createScaledBitmap;
        if (createScaledBitmap != null) {
            Utilities.blurBitmap(createScaledBitmap, 7, 1, createScaledBitmap.getWidth(), this.lastBitmap.getHeight(), this.lastBitmap.getRowBytes());
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(ApplicationLoader.getFilesDirFixed(), "icthumb.jpg"));
                this.lastBitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream);
                fileOutputStream.close();
            } catch (Throwable unused) {
            }
        }
    }

    public void startProgressTimer() {
        Timer timer = this.progressTimer;
        if (timer != null) {
            try {
                timer.cancel();
                this.progressTimer = null;
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        Timer timer2 = new Timer();
        this.progressTimer = timer2;
        timer2.schedule(new 11(), 0L, 17L);
    }

    private void stopProgressTimer() {
        Timer timer = this.progressTimer;
        if (timer != null) {
            try {
                timer.cancel();
                this.progressTimer = null;
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    private void switchCamera() {
        if (!this.useCamera2 || !this.bothCameras) {
            saveLastCameraBitmap();
            Bitmap bitmap = this.lastBitmap;
            if (bitmap != null) {
                this.needDrawFlickerStub = false;
                this.textureOverlayView.setImageBitmap(bitmap);
                this.textureOverlayView.setAlpha(1.0f);
            }
        }
        this.isFrontface = !this.isFrontface;
        updateFlash();
        if (!this.useCamera2) {
            CameraSession cameraSession = this.cameraSession;
            if (cameraSession != null) {
                cameraSession.destroy();
                CameraController.getInstance().close(this.cameraSession, null, null);
                this.cameraSession = null;
            }
        } else if (this.bothCameras) {
            this.camera2SessionCurrent = this.camera2Sessions[!this.isFrontface ? 1 : 0];
            this.cameraThread.flipSurfaces();
            return;
        } else {
            Camera2Session camera2Session = this.camera2SessionCurrent;
            if (camera2Session != null) {
                camera2Session.destroy(false);
                this.camera2SessionCurrent = null;
                this.camera2Sessions[this.isFrontface ? 1 : 0] = null;
            }
            Camera2Session[] camera2SessionArr = this.camera2Sessions;
            boolean z = this.isFrontface;
            int i = !z ? 1 : 0;
            Camera2Session create = Camera2Session.create(z, MessagesController.getInstance(UserConfig.selectedAccount).roundVideoSize, MessagesController.getInstance(UserConfig.selectedAccount).roundVideoSize);
            camera2SessionArr[i] = create;
            this.camera2SessionCurrent = create;
            if (create == null) {
                return;
            }
            create.setRecordingVideo(true);
            this.previewSize[0] = new org.telegram.messenger.camera.Size(this.camera2SessionCurrent.getPreviewWidth(), this.camera2SessionCurrent.getPreviewHeight());
            this.cameraThread.setCurrentSession(this.camera2SessionCurrent);
        }
        initCamera();
        this.cameraReady = false;
        this.cameraThread.reinitForNewCamera();
    }

    public void updateFlash() {
        RLottieDrawable rLottieDrawable;
        RLottieDrawable rLottieDrawable2;
        boolean z = this.flashing && this.recording && this.isFrontface;
        if (this.frontFlashing != z) {
            this.frontFlashing = z;
            if (z) {
                this.flashViews.flashIn(null);
            } else {
                this.flashViews.flashOut();
            }
        }
        if (this.useCamera2) {
            Camera2Session camera2Session = this.camera2Sessions[1];
            if (camera2Session != null) {
                camera2Session.setFlash(this.flashing && !this.isFrontface && this.recording);
            }
        } else {
            CameraSession cameraSession = this.cameraSession;
            if (cameraSession != null) {
                cameraSession.setTorchEnabled(this.flashing && !this.isFrontface && this.recording);
            }
        }
        if (this.flashButton != null) {
            Boolean bool = this.wasFlashing;
            if (bool == null || bool.booleanValue() != this.flashing) {
                this.flashButton.setContentDescription(LocaleController.getString(this.flashing ? R.string.AccDescrCameraFlashOff : R.string.AccDescrCameraFlashOn));
                if (this.flashing) {
                    if (this.flashOffDrawable == null) {
                        RLottieDrawable rLottieDrawable3 = new RLottieDrawable(R.raw.roundcamera_flash_off, "roundcamera_flash_off", AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f));
                        this.flashOffDrawable = rLottieDrawable3;
                        rLottieDrawable3.setCallback(this.flashButton);
                    }
                    this.flashButton.setImageDrawable(this.flashOffDrawable);
                    if (this.wasFlashing == null) {
                        rLottieDrawable2 = this.flashOffDrawable;
                        rLottieDrawable2.setCurrentFrame(rLottieDrawable2.getFramesCount() - 1);
                    } else {
                        this.flashOffDrawable.setCurrentFrame(0);
                        rLottieDrawable = this.flashOffDrawable;
                        rLottieDrawable.start();
                    }
                } else {
                    if (this.flashOnDrawable == null) {
                        RLottieDrawable rLottieDrawable4 = new RLottieDrawable(R.raw.roundcamera_flash_on, "roundcamera_flash_on", AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f));
                        this.flashOnDrawable = rLottieDrawable4;
                        rLottieDrawable4.setCallback(this.flashButton);
                    }
                    this.flashButton.setImageDrawable(this.flashOnDrawable);
                    if (this.wasFlashing == null) {
                        rLottieDrawable2 = this.flashOnDrawable;
                        rLottieDrawable2.setCurrentFrame(rLottieDrawable2.getFramesCount() - 1);
                    } else {
                        this.flashOnDrawable.setCurrentFrame(0);
                        rLottieDrawable = this.flashOnDrawable;
                        rLottieDrawable.start();
                    }
                }
                this.wasFlashing = Boolean.valueOf(this.flashing);
            }
        }
    }

    private void updateTranslationY() {
        this.textureOverlayView.setTranslationY(this.animationTranslationY + this.panTranslationY);
        this.cameraContainer.setTranslationY(this.animationTranslationY + this.panTranslationY);
    }

    public boolean blurFullyDrawing() {
        BlurBehindDrawable blurBehindDrawable = this.blurBehindDrawable;
        return blurBehindDrawable != null && blurBehindDrawable.isFullyDrawing() && this.opened;
    }

    public void cancel(boolean z) {
        stopProgressTimer();
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.releasePlayer(true);
            this.videoPlayer = null;
        }
        if (this.textureView == null) {
            return;
        }
        this.cancelled = true;
        this.recording = false;
        this.flashing = false;
        updateFlash();
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordStopped, Integer.valueOf(this.recordingGuid), Integer.valueOf(z ? 0 : 6));
        if (this.cameraThread != null) {
            saveLastCameraBitmap();
            this.cameraThread.shutdown(0, true, 0, 0, 0L);
            this.cameraThread = null;
        } else {
            VideoRecorder videoRecorder = this.videoEncoder;
            if (videoRecorder != null) {
                videoRecorder.stopRecording(0, new SendOptions(true, 0, 0, 0L));
            }
        }
        if (this.cameraFile != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("delete camera file by cancel");
            }
            this.cameraFile.delete();
            AutoDeleteMediaTask.unlockFile(this.cameraFile);
            this.cameraFile = null;
        }
        MediaController.getInstance().requestRecordAudioFocus(false);
        startAnimation(false, false);
        this.blurBehindDrawable.show(false);
        invalidate();
    }

    public void cancelBlur() {
        this.blurBehindDrawable.show(false);
        invalidate();
    }

    public void changeVideoPreviewState(int i, float f) {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer == null) {
            return;
        }
        if (i == 0) {
            startProgressTimer();
            this.videoPlayer.play();
        } else if (i == 1) {
            stopProgressTimer();
            this.videoPlayer.pause();
        } else if (i == 2) {
            videoPlayer.seekTo(f * ((float) videoPlayer.getDuration()));
        }
    }

    protected void clipBlur(Canvas canvas) {
    }

    public void destroy(boolean z) {
        if (!this.useCamera2) {
            CameraSession cameraSession = this.cameraSession;
            if (cameraSession != null) {
                cameraSession.destroy();
                CameraController.getInstance().close(this.cameraSession, !z ? new CountDownLatch(1) : null, null);
                return;
            }
            return;
        }
        int i = 0;
        while (true) {
            Camera2Session[] camera2SessionArr = this.camera2Sessions;
            if (i >= camera2SessionArr.length) {
                return;
            }
            Camera2Session camera2Session = camera2SessionArr[i];
            if (camera2Session != null) {
                camera2Session.destroy(z);
                this.camera2Sessions[i] = null;
            }
            i++;
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.fileUploaded) {
            String str = (String) objArr[0];
            File file = this.cameraFile;
            if (file == null || !file.getAbsolutePath().equals(str)) {
                return;
            }
            this.file = (TLRPC$InputFile) objArr[1];
            this.encryptedFile = (TLRPC$InputEncryptedFile) objArr[2];
            this.size = ((Long) objArr[5]).longValue();
            if (this.encryptedFile != null) {
                this.key = (byte[]) objArr[3];
                this.iv = (byte[]) objArr[4];
            }
        }
    }

    public void finishZoom() {
        float min;
        if (this.finishZoomTransition != null) {
            return;
        }
        if (this.useCamera2) {
            Camera2Session camera2Session = this.camera2SessionCurrent;
            if (camera2Session == null) {
                return;
            }
            min = Utilities.clamp(this.pinchScale, camera2Session.getMaxZoom(), this.camera2SessionCurrent.getMinZoom());
        } else {
            min = Math.min(1.0f, Math.max(0.0f, this.pinchScale - 1.0f));
        }
        if (min > 0.0f) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(min, 0.0f);
            this.finishZoomTransition = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.InstantCameraView$$ExternalSyntheticLambda4
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    InstantCameraView.this.lambda$finishZoom$8(valueAnimator);
                }
            });
            this.finishZoomTransition.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.InstantCameraView.13
                {
                    InstantCameraView.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    InstantCameraView instantCameraView = InstantCameraView.this;
                    if (instantCameraView.finishZoomTransition != null) {
                        instantCameraView.finishZoomTransition = null;
                    }
                }
            });
            this.finishZoomTransition.setDuration(350L);
            this.finishZoomTransition.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.finishZoomTransition.start();
        }
    }

    public InstantViewCameraContainer getCameraContainer() {
        return this.cameraContainer;
    }

    public Rect getCameraRect() {
        this.cameraContainer.getLocationOnScreen(this.position);
        int[] iArr = this.position;
        return new Rect(iArr[0], iArr[1], this.cameraContainer.getWidth(), this.cameraContainer.getHeight());
    }

    public View getFlashButtonView() {
        return this.flashButton;
    }

    public View getMuteImageView() {
        return this.muteImageView;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public View getSwitchButtonView() {
        return this.switchCameraButton;
    }

    public TextureView getTextureView() {
        return this.textureView;
    }

    public void hideCamera(boolean z) {
        ViewGroup viewGroup;
        destroy(z);
        this.cameraContainer.setTranslationX(0.0f);
        this.textureOverlayView.setTranslationX(0.0f);
        this.animationTranslationY = 0.0f;
        updateTranslationY();
        MediaController.getInstance().resumeByRewind();
        TextureView textureView = this.textureView;
        if (textureView != null && (viewGroup = (ViewGroup) textureView.getParent()) != null) {
            viewGroup.removeView(this.textureView);
        }
        this.textureView = null;
        this.cameraContainer.setImageReceiver(null);
    }

    public void invalidateBlur() {
        BlurBehindDrawable blurBehindDrawable = this.blurBehindDrawable;
        if (blurBehindDrawable != null) {
            blurBehindDrawable.invalidate();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileUploaded);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileUploaded);
        FlashViews flashViews = this.flashViews;
        if (flashViews != null) {
            flashViews.flashOut();
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.drawBlur) {
            canvas.save();
            clipBlur(canvas);
            this.blurBehindDrawable.draw(canvas);
            canvas.restore();
        }
        float x = this.cameraContainer.getX();
        float y = this.cameraContainer.getY();
        this.rect.set(x - AndroidUtilities.dp(8.0f), y - AndroidUtilities.dp(8.0f), x + this.cameraContainer.getMeasuredWidth() + AndroidUtilities.dp(8.0f), y + this.cameraContainer.getMeasuredHeight() + AndroidUtilities.dp(8.0f));
        if (this.recording) {
            long currentTimeMillis = (System.currentTimeMillis() - this.recordStartTime) + this.recordPlusTime;
            this.recordedTime = currentTimeMillis;
            this.progress = Math.min(1.0f, ((float) currentTimeMillis) / 60000.0f);
            invalidate();
        }
        if (this.progress != 0.0f) {
            canvas.save();
            if (!this.flipAnimationInProgress) {
                canvas.scale(this.cameraContainer.getScaleX(), this.cameraContainer.getScaleY(), this.rect.centerX(), this.rect.centerY());
            }
            canvas.drawArc(this.rect, -90.0f, this.progress * 360.0f, false, this.paint);
            canvas.restore();
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.updateTextureViewSize) {
            int i3 = ((float) View.MeasureSpec.getSize(i2)) > ((float) View.MeasureSpec.getSize(i)) * 1.3f ? AndroidUtilities.roundPlayingMessageSize : AndroidUtilities.roundMessageSize;
            if (i3 != this.textureViewSize) {
                this.textureViewSize = i3;
                ViewGroup.LayoutParams layoutParams = this.textureOverlayView.getLayoutParams();
                ViewGroup.LayoutParams layoutParams2 = this.textureOverlayView.getLayoutParams();
                int i4 = this.textureViewSize;
                layoutParams2.height = i4;
                layoutParams.width = i4;
                ViewGroup.LayoutParams layoutParams3 = this.cameraContainer.getLayoutParams();
                ViewGroup.LayoutParams layoutParams4 = this.cameraContainer.getLayoutParams();
                int i5 = this.textureViewSize;
                layoutParams4.height = i5;
                layoutParams3.width = i5;
                ((FrameLayout.LayoutParams) this.muteImageView.getLayoutParams()).topMargin = (this.textureViewSize / 2) - AndroidUtilities.dp(24.0f);
                this.textureOverlayView.setRoundRadius(this.textureViewSize / 2);
                if (Build.VERSION.SDK_INT >= 21) {
                    this.cameraContainer.invalidateOutline();
                }
            }
            this.updateTextureViewSize = false;
        }
        super.onMeasure(i, i2);
    }

    public void onPanTranslationUpdate(float f) {
        this.panTranslationY = f / 2.0f;
        updateTranslationY();
        this.blurBehindDrawable.onPanTranslationUpdate(f);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (getVisibility() != 0) {
            this.animationTranslationY = getMeasuredHeight() / 2;
            updateTranslationY();
        }
        this.blurBehindDrawable.checkSizes();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        VideoPlayer videoPlayer;
        if (motionEvent.getAction() == 0 && this.delegate != null && (videoPlayer = this.videoPlayer) != null) {
            boolean z = !videoPlayer.isMuted();
            this.videoPlayer.setMute(z);
            AnimatorSet animatorSet = this.muteAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.muteAnimation = animatorSet2;
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this.muteImageView, View.ALPHA, z ? 1.0f : 0.0f), ObjectAnimator.ofFloat(this.muteImageView, View.SCALE_X, z ? 1.0f : 0.5f), ObjectAnimator.ofFloat(this.muteImageView, View.SCALE_Y, z ? 1.0f : 0.5f));
            this.muteAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.InstantCameraView.12
                {
                    InstantCameraView.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (animator.equals(InstantCameraView.this.muteAnimation)) {
                        InstantCameraView.this.muteAnimation = null;
                    }
                }
            });
            this.muteAnimation.setDuration(180L);
            this.muteAnimation.setInterpolator(new DecelerateInterpolator());
            this.muteAnimation.start();
        }
        if (motionEvent.getActionMasked() == 0 || motionEvent.getActionMasked() == 5) {
            if (this.maybePinchToZoomTouchMode && !this.isInPinchToZoomTouchMode && motionEvent.getPointerCount() == 2 && this.finishZoomTransition == null && this.recording) {
                this.pinchStartDistance = (float) Math.hypot(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0));
                this.pinchScale = 1.0f;
                this.pointerId1 = motionEvent.getPointerId(0);
                this.pointerId2 = motionEvent.getPointerId(1);
                this.isInPinchToZoomTouchMode = true;
            }
            if (motionEvent.getActionMasked() == 0) {
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(this.cameraContainer.getX(), this.cameraContainer.getY(), this.cameraContainer.getX() + this.cameraContainer.getMeasuredWidth(), this.cameraContainer.getY() + this.cameraContainer.getMeasuredHeight());
                this.maybePinchToZoomTouchMode = rectF.contains(motionEvent.getX(), motionEvent.getY());
            }
            return true;
        }
        if (motionEvent.getActionMasked() == 2 && this.isInPinchToZoomTouchMode) {
            int i = -1;
            int i2 = -1;
            for (int i3 = 0; i3 < motionEvent.getPointerCount(); i3++) {
                if (this.pointerId1 == motionEvent.getPointerId(i3)) {
                    i = i3;
                }
                if (this.pointerId2 == motionEvent.getPointerId(i3)) {
                    i2 = i3;
                }
            }
            if (i == -1 || i2 == -1) {
                this.isInPinchToZoomTouchMode = false;
                finishZoom();
                return false;
            }
            float hypot = ((float) Math.hypot(motionEvent.getX(i2) - motionEvent.getX(i), motionEvent.getY(i2) - motionEvent.getY(i))) / this.pinchStartDistance;
            this.pinchScale = hypot;
            if (this.useCamera2) {
                Camera2Session camera2Session = this.camera2SessionCurrent;
                if (camera2Session != null) {
                    this.camera2SessionCurrent.setZoom(Utilities.clamp(hypot, camera2Session.getMaxZoom(), this.camera2SessionCurrent.getMinZoom()));
                }
            } else {
                this.cameraSession.setZoom(Math.min(1.0f, Math.max(0.0f, hypot - 1.0f)));
            }
        } else if ((motionEvent.getActionMasked() == 1 || ((motionEvent.getActionMasked() == 6 && checkPointerIds(motionEvent)) || motionEvent.getActionMasked() == 3)) && this.isInPinchToZoomTouchMode) {
            this.isInPinchToZoomTouchMode = false;
            finishZoom();
        }
        return true;
    }

    public void resetCameraFile() {
        this.cameraFile = null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:106:0x0107, code lost:
        if (r27 != 0) goto L40;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void send(int i, boolean z, int i2, int i3, long j) {
        if (this.textureView == null) {
            return;
        }
        stopProgressTimer();
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.releasePlayer(true);
            this.videoPlayer = null;
        }
        if (i == 4) {
            VideoRecorder videoRecorder = this.videoEncoder;
            if (videoRecorder != null && this.recordedTime > 800) {
                videoRecorder.stopRecording(1, new SendOptions(z, i2, i3, j));
                return;
            }
            if (BuildVars.DEBUG_VERSION && !this.cameraFile.exists()) {
                FileLog.e(new RuntimeException("file not found :( round video"));
            }
            if (this.videoEditedInfo.needConvert()) {
                this.file = null;
                this.encryptedFile = null;
                this.key = null;
                this.iv = null;
                VideoEditedInfo videoEditedInfo = this.videoEditedInfo;
                long j2 = videoEditedInfo.estimatedDuration;
                double d = j2;
                long j3 = videoEditedInfo.startTime;
                if (j3 < 0) {
                    j3 = 0;
                }
                long j4 = videoEditedInfo.endTime;
                if (j4 >= 0) {
                    j2 = j4;
                }
                long j5 = j2 - j3;
                videoEditedInfo.estimatedDuration = j5;
                double d2 = this.size;
                double d3 = j5;
                Double.isNaN(d3);
                Double.isNaN(d);
                Double.isNaN(d2);
                videoEditedInfo.estimatedSize = Math.max(1L, (long) (d2 * (d3 / d)));
                VideoEditedInfo videoEditedInfo2 = this.videoEditedInfo;
                videoEditedInfo2.bitrate = MediaController.VIDEO_BITRATE_480;
                long j6 = videoEditedInfo2.startTime;
                if (j6 > 0) {
                    videoEditedInfo2.startTime = j6 * 1000;
                }
                long j7 = videoEditedInfo2.endTime;
                if (j7 > 0) {
                    videoEditedInfo2.endTime = j7 * 1000;
                }
                FileLoader.getInstance(this.currentAccount).cancelFileUpload(this.cameraFile.getAbsolutePath(), false);
            } else {
                this.videoEditedInfo.estimatedSize = Math.max(1L, this.size);
            }
            VideoEditedInfo videoEditedInfo3 = this.videoEditedInfo;
            videoEditedInfo3.file = this.file;
            videoEditedInfo3.encryptedFile = this.encryptedFile;
            videoEditedInfo3.key = this.key;
            videoEditedInfo3.iv = this.iv;
            MediaController.PhotoEntry photoEntry = new MediaController.PhotoEntry(0, 0, 0L, this.cameraFile.getAbsolutePath(), 0, true, 0, 0, 0L);
            photoEntry.ttl = i3;
            photoEntry.effectId = j;
            this.delegate.sendMedia(photoEntry, this.videoEditedInfo, z, i2, false);
        } else {
            this.cancelled = this.recordedTime < 800;
            this.recording = false;
            this.flashing = false;
            updateFlash();
            int i4 = this.cancelled ? 4 : i == 3 ? 2 : 5;
            if (this.cameraThread != null) {
                NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordStopped, Integer.valueOf(this.recordingGuid), Integer.valueOf(i4));
                int i5 = this.cancelled ? 0 : i == 3 ? 2 : 1;
                saveLastCameraBitmap();
                this.cameraThread.shutdown(i5, z, i2, i3, j);
                this.cameraThread = null;
            }
            if (!this.cancelled) {
                return;
            }
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.audioRecordTooShort, Integer.valueOf(this.recordingGuid), Boolean.TRUE, Integer.valueOf((int) this.recordedTime));
        }
        startAnimation(false, false);
        MediaController.getInstance().requestRecordAudioFocus(false);
    }

    public void setIsMessageTransition(boolean z) {
        this.isMessageTransition = z;
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        BlurBehindDrawable blurBehindDrawable;
        super.setVisibility(i);
        if (i != 0 && (blurBehindDrawable = this.blurBehindDrawable) != null) {
            blurBehindDrawable.clear();
        }
        this.switchCameraButton.setAlpha(0.0f);
        this.flashButton.setAlpha(0.0f);
        this.cameraContainer.setAlpha(0.0f);
        this.textureOverlayView.setAlpha(0.0f);
        this.muteImageView.setAlpha(0.0f);
        this.muteImageView.setScaleX(1.0f);
        this.muteImageView.setScaleY(1.0f);
        this.cameraContainer.setScaleX(this.setVisibilityFromPause ? 1.0f : 0.1f);
        this.cameraContainer.setScaleY(this.setVisibilityFromPause ? 1.0f : 0.1f);
        this.textureOverlayView.setScaleX(this.setVisibilityFromPause ? 1.0f : 0.1f);
        this.textureOverlayView.setScaleY(this.setVisibilityFromPause ? 1.0f : 0.1f);
        if (this.cameraContainer.getMeasuredWidth() != 0) {
            InstantViewCameraContainer instantViewCameraContainer = this.cameraContainer;
            instantViewCameraContainer.setPivotX(instantViewCameraContainer.getMeasuredWidth() / 2);
            InstantViewCameraContainer instantViewCameraContainer2 = this.cameraContainer;
            instantViewCameraContainer2.setPivotY(instantViewCameraContainer2.getMeasuredHeight() / 2);
            BackupImageView backupImageView = this.textureOverlayView;
            backupImageView.setPivotX(backupImageView.getMeasuredWidth() / 2);
            BackupImageView backupImageView2 = this.textureOverlayView;
            backupImageView2.setPivotY(backupImageView2.getMeasuredHeight() / 2);
        }
        try {
            if (i == 0) {
                ((Activity) getContext()).getWindow().addFlags(128);
            } else {
                ((Activity) getContext()).getWindow().clearFlags(128);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void showCamera(boolean z) {
        if (this.textureView != null) {
            return;
        }
        if (this.switchCameraDrawable == null) {
            RLottieDrawable rLottieDrawable = new RLottieDrawable(R.raw.roundcamera_flip, "roundcamera_flip", AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f));
            this.switchCameraDrawable = rLottieDrawable;
            rLottieDrawable.setCurrentFrame(0);
            this.switchCameraDrawable.setCallback(this.switchCameraButton);
        }
        this.switchCameraButton.setImageDrawable(this.switchCameraDrawable);
        this.textureOverlayView.setAlpha(1.0f);
        this.textureOverlayView.invalidate();
        if (this.lastBitmap == null) {
            try {
                this.lastBitmap = BitmapFactory.decodeFile(new File(ApplicationLoader.getFilesDirFixed(), "icthumb.jpg").getAbsolutePath());
            } catch (Throwable unused) {
            }
        }
        Bitmap bitmap = this.lastBitmap;
        if (bitmap != null) {
            this.textureOverlayView.setImageBitmap(bitmap);
        } else {
            this.textureOverlayView.setImageResource(R.drawable.icplaceholder);
        }
        this.cameraReady = false;
        this.selectedCamera = null;
        if (!z) {
            if (!this.useCamera2) {
                this.isFrontface = true;
            }
            updateFlash();
            this.recordedTime = 0L;
            this.progress = 0.0f;
        }
        this.cancelled = false;
        this.file = null;
        this.encryptedFile = null;
        this.key = null;
        this.iv = null;
        this.needDrawFlickerStub = true;
        if (initCamera()) {
            if (MediaController.getInstance().getPlayingMessageObject() != null) {
                if (MediaController.getInstance().getPlayingMessageObject().isVideo() || MediaController.getInstance().getPlayingMessageObject().isRoundVideo()) {
                    MediaController.getInstance().cleanupPlayer(true, true);
                } else if (SharedConfig.pauseMusicOnRecord) {
                    MediaController.getInstance().pauseByRewind();
                }
            }
            if (!z) {
                File directory = FileLoader.getDirectory(3);
                this.cameraFile = new File(directory, System.currentTimeMillis() + "_" + SharedConfig.getLastLocalId() + ".mp4") { // from class: org.telegram.ui.Components.InstantCameraView.8
                    {
                        InstantCameraView.this = this;
                    }

                    @Override // java.io.File
                    public boolean delete() {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.e("delete camera file");
                        }
                        return super.delete();
                    }
                };
            }
            SharedConfig.saveConfig();
            AutoDeleteMediaTask.lockFile(this.cameraFile);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("InstantCamera show round camera " + this.cameraFile.getAbsolutePath());
            }
            if (this.useCamera2) {
                boolean roundDualAvailableStatic = DualCameraView.roundDualAvailableStatic(getContext());
                this.bothCameras = roundDualAvailableStatic;
                if (roundDualAvailableStatic) {
                    int i = 0;
                    while (i < 2) {
                        Camera2Session[] camera2SessionArr = this.camera2Sessions;
                        if (camera2SessionArr[i] == null) {
                            camera2SessionArr[i] = Camera2Session.create(i == 0, MessagesController.getInstance(UserConfig.selectedAccount).roundVideoSize, MessagesController.getInstance(UserConfig.selectedAccount).roundVideoSize);
                            Camera2Session camera2Session = this.camera2Sessions[i];
                            if (camera2Session != null) {
                                camera2Session.setRecordingVideo(true);
                                this.previewSize[i] = new org.telegram.messenger.camera.Size(this.camera2Sessions[i].getPreviewWidth(), this.camera2Sessions[i].getPreviewHeight());
                            }
                        }
                        i++;
                    }
                    updateFlash();
                    Camera2Session[] camera2SessionArr2 = this.camera2Sessions;
                    boolean z2 = this.isFrontface;
                    Camera2Session camera2Session2 = camera2SessionArr2[!z2 ? 1 : 0];
                    this.camera2SessionCurrent = camera2Session2;
                    if (camera2Session2 != null && camera2SessionArr2[z2 ? 1 : 0] == null) {
                        this.bothCameras = false;
                    }
                    if (camera2Session2 == null) {
                        return;
                    }
                } else {
                    Camera2Session[] camera2SessionArr3 = this.camera2Sessions;
                    boolean z3 = this.isFrontface;
                    int i2 = !z3 ? 1 : 0;
                    Camera2Session create = Camera2Session.create(z3, MessagesController.getInstance(UserConfig.selectedAccount).roundVideoSize, MessagesController.getInstance(UserConfig.selectedAccount).roundVideoSize);
                    camera2SessionArr3[i2] = create;
                    this.camera2SessionCurrent = create;
                    if (create == null) {
                        return;
                    }
                    create.setRecordingVideo(true);
                    this.previewSize[0] = new org.telegram.messenger.camera.Size(this.camera2SessionCurrent.getPreviewWidth(), this.camera2SessionCurrent.getPreviewHeight());
                }
            }
            TextureView textureView = new TextureView(getContext());
            this.textureView = textureView;
            textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() { // from class: org.telegram.ui.Components.InstantCameraView.9
                {
                    InstantCameraView.this = this;
                }

                @Override // android.view.TextureView.SurfaceTextureListener
                public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i3, int i4) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("InstantCamera camera surface available");
                    }
                    if (InstantCameraView.this.cameraThread != null || surfaceTexture == null || InstantCameraView.this.cancelled) {
                        return;
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("InstantCamera start create thread");
                    }
                    InstantCameraView.this.cameraThread = new CameraGLThread(surfaceTexture, i3, i4);
                }

                @Override // android.view.TextureView.SurfaceTextureListener
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                    if (InstantCameraView.this.cameraThread != null) {
                        InstantCameraView.this.cameraThread.shutdown(0, true, 0, 0, 0L);
                        InstantCameraView.this.cameraThread = null;
                    }
                    if (!InstantCameraView.this.useCamera2) {
                        if (InstantCameraView.this.cameraSession != null) {
                            CameraController.getInstance().close(InstantCameraView.this.cameraSession, null, null);
                            return true;
                        }
                        return true;
                    }
                    for (int i3 = 0; i3 < InstantCameraView.this.camera2Sessions.length; i3++) {
                        if (InstantCameraView.this.camera2Sessions[i3] != null) {
                            InstantCameraView.this.camera2Sessions[i3].destroy(false);
                            InstantCameraView.this.camera2Sessions[i3] = null;
                        }
                    }
                    return true;
                }

                @Override // android.view.TextureView.SurfaceTextureListener
                public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i3, int i4) {
                    if (InstantCameraView.this.cameraThread != null) {
                        InstantCameraView.this.cameraThread.surfaceWidth = i3;
                        InstantCameraView.this.cameraThread.surfaceHeight = i4;
                        InstantCameraView.this.cameraThread.updateScale();
                    }
                }

                @Override // android.view.TextureView.SurfaceTextureListener
                public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                }
            });
            this.cameraContainer.addView(this.textureView, LayoutHelper.createFrame(-1, -1.0f));
            this.updateTextureViewSize = true;
            this.setVisibilityFromPause = z;
            setVisibility(0);
            startAnimation(true, z);
            MediaController.getInstance().requestRecordAudioFocus(true);
        }
    }

    public void startAnimation(boolean z, final boolean z2) {
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            this.animatorSet.cancel();
        }
        PipRoundVideoView pipRoundVideoView = PipRoundVideoView.getInstance();
        if (pipRoundVideoView != null) {
            pipRoundVideoView.showTemporary(!z);
        }
        if (z && !this.opened) {
            this.cameraContainer.setTranslationX(0.0f);
            this.textureOverlayView.setTranslationX(0.0f);
            this.animationTranslationY = z2 ? 0.0f : getMeasuredHeight() / 2.0f;
            updateTranslationY();
        }
        this.opened = z;
        View view = this.parentView;
        if (view != null) {
            view.invalidate();
        }
        this.blurBehindDrawable.show(z);
        this.animatorSet = new AnimatorSet();
        float dp = (z || this.recordedTime <= 300) ? 0.0f : AndroidUtilities.dp(24.0f) - (getMeasuredWidth() / 2.0f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(z ? 1.0f : 0.0f, z ? 0.0f : 1.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.InstantCameraView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                InstantCameraView.this.lambda$startAnimation$3(z2, valueAnimator);
            }
        });
        AnimatorSet animatorSet2 = this.animatorSet;
        FlashViews.ImageViewInvertable imageViewInvertable = this.switchCameraButton;
        Property property = View.ALPHA;
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(imageViewInvertable, property, z ? 1.0f : 0.0f);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.flashButton, property, z ? 1.0f : 0.0f);
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this.muteImageView, property, 0.0f);
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this.paint, AnimationProperties.PAINT_ALPHA, z ? NotificationCenter.didClearDatabase : 0);
        ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(this.cameraContainer, property, z ? 1.0f : 0.0f);
        InstantViewCameraContainer instantViewCameraContainer = this.cameraContainer;
        Property property2 = View.SCALE_X;
        ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(instantViewCameraContainer, property2, z ? 1.0f : 0.1f);
        InstantViewCameraContainer instantViewCameraContainer2 = this.cameraContainer;
        Property property3 = View.SCALE_Y;
        ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat(instantViewCameraContainer2, property3, z ? 1.0f : 0.1f);
        InstantViewCameraContainer instantViewCameraContainer3 = this.cameraContainer;
        Property property4 = View.TRANSLATION_X;
        animatorSet2.playTogether(ofFloat2, ofFloat3, ofFloat4, ofInt, ofFloat5, ofFloat6, ofFloat7, ObjectAnimator.ofFloat(instantViewCameraContainer3, property4, dp), ObjectAnimator.ofFloat(this.textureOverlayView, property, z ? 1.0f : 0.0f), ObjectAnimator.ofFloat(this.textureOverlayView, property2, z ? 1.0f : 0.1f), ObjectAnimator.ofFloat(this.textureOverlayView, property3, z ? 1.0f : 0.1f), ObjectAnimator.ofFloat(this.textureOverlayView, property4, dp), ofFloat);
        if (z) {
            setTranslationX(0.0f);
        } else {
            this.animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.InstantCameraView.10
                {
                    InstantCameraView.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (animator.equals(InstantCameraView.this.animatorSet)) {
                        InstantCameraView.this.hideCamera(true);
                        InstantCameraView.this.setVisibilityFromPause = false;
                        InstantCameraView.this.setVisibility(4);
                    }
                }
            });
        }
        this.animatorSet.setDuration(180L);
        this.animatorSet.setInterpolator(new DecelerateInterpolator());
        this.animatorSet.start();
    }

    public void togglePause() {
        if (!this.recording) {
            VideoRecorder videoRecorder = this.videoEncoder;
            if (videoRecorder != null) {
                videoRecorder.resume();
                hideCamera(false);
                VideoPlayer videoPlayer = this.videoPlayer;
                if (videoPlayer != null) {
                    videoPlayer.releasePlayer(true);
                    this.videoPlayer = null;
                }
                showCamera(true);
                try {
                    performHapticFeedback(3, 2);
                } catch (Exception unused) {
                }
                AndroidUtilities.lockOrientation(this.delegate.getParentActivity());
                invalidate();
                NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordResumed, new Object[0]);
                return;
            }
            return;
        }
        this.cancelled = this.recordedTime < 800;
        this.recording = false;
        updateFlash();
        if (this.cameraThread != null) {
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recordStopped, Integer.valueOf(this.recordingGuid), Integer.valueOf(this.cancelled ? 4 : 2));
            saveLastCameraBitmap();
            CameraGLThread cameraGLThread = this.cameraThread;
            boolean z = this.cancelled;
            cameraGLThread.shutdown(z ? 0 : 2, true, 0, z ? 0 : -2, 0L);
            this.cameraThread = null;
        }
        if (!this.cancelled) {
            this.videoEncoder.pause();
            return;
        }
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.audioRecordTooShort, Integer.valueOf(this.recordingGuid), Boolean.TRUE, Integer.valueOf((int) this.recordedTime));
        startAnimation(false, false);
        MediaController.getInstance().requestRecordAudioFocus(false);
    }
}
