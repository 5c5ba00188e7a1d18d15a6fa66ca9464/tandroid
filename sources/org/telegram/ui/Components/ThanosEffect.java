package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLES31;
import android.opengl.GLUtils;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Choreographer;
import android.view.TextureView;
import android.view.View;
import com.google.zxing.common.detector.MathUtils;
import java.util.ArrayList;
import java.util.HashMap;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.Cells.BaseCell;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.ThanosEffect;
/* loaded from: classes4.dex */
public class ThanosEffect extends TextureView {
    private static Boolean nothanos;
    private DrawingThread drawThread;
    private final Choreographer.FrameCallback frameCallback;
    private final ArrayList<ToSet> toSet;
    private Runnable whenDone;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void access$400(ThanosEffect thanosEffect) {
        thanosEffect.destroy();
    }

    public static boolean supports() {
        if (nothanos == null) {
            nothanos = Boolean.valueOf(MessagesController.getGlobalMainSettings().getBoolean("nothanos", false));
        }
        Boolean bool = nothanos;
        return (bool == null || !bool.booleanValue()) && Build.VERSION.SDK_INT >= 21;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class ToSet {
        public final Bitmap bitmap;
        public final Runnable doneCallback;
        public final Matrix matrix;
        public final Runnable startCallback;
        public final View view;
        public final ArrayList<View> views;

        public ToSet(View view, Runnable runnable) {
            this.view = view;
            this.views = null;
            this.startCallback = null;
            this.doneCallback = runnable;
            this.bitmap = null;
            this.matrix = null;
        }

        public ToSet(ArrayList<View> arrayList, Runnable runnable) {
            this.view = null;
            this.views = arrayList;
            this.startCallback = null;
            this.doneCallback = runnable;
            this.bitmap = null;
            this.matrix = null;
        }

        public ToSet(Matrix matrix, Bitmap bitmap, Runnable runnable, Runnable runnable2) {
            this.view = null;
            this.views = null;
            this.startCallback = runnable;
            this.doneCallback = runnable2;
            this.matrix = matrix;
            this.bitmap = bitmap;
        }
    }

    public ThanosEffect(Context context, Runnable runnable) {
        super(context);
        this.frameCallback = new Choreographer.FrameCallback() { // from class: org.telegram.ui.Components.ThanosEffect.1
            @Override // android.view.Choreographer.FrameCallback
            public void doFrame(long j) {
                if (ThanosEffect.this.drawThread != null) {
                    ThanosEffect.this.drawThread.requestDraw();
                    if (ThanosEffect.this.drawThread.running) {
                        Choreographer.getInstance().postFrameCallback(this);
                    }
                }
            }
        };
        this.toSet = new ArrayList<>();
        this.whenDone = runnable;
        setOpaque(false);
        setSurfaceTextureListener(new 2());
    }

    /* loaded from: classes4.dex */
    class 2 implements TextureView.SurfaceTextureListener {
        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        2() {
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
            if (ThanosEffect.this.drawThread != null) {
                ThanosEffect.this.drawThread.kill();
                ThanosEffect.this.drawThread = null;
            }
            ThanosEffect thanosEffect = ThanosEffect.this;
            final ThanosEffect thanosEffect2 = ThanosEffect.this;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ThanosEffect$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ThanosEffect.this.invalidate();
                }
            };
            final ThanosEffect thanosEffect3 = ThanosEffect.this;
            thanosEffect.drawThread = new DrawingThread(surfaceTexture, runnable, new Runnable() { // from class: org.telegram.ui.Components.ThanosEffect$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ThanosEffect.access$400(ThanosEffect.this);
                }
            }, i, i2);
            if (ThanosEffect.this.toSet.isEmpty()) {
                return;
            }
            for (int i3 = 0; i3 < ThanosEffect.this.toSet.size(); i3++) {
                ToSet toSet = (ToSet) ThanosEffect.this.toSet.get(i3);
                if (toSet.bitmap != null) {
                    ThanosEffect.this.drawThread.animate(toSet.matrix, toSet.bitmap, toSet.startCallback, toSet.doneCallback);
                } else if (toSet.views != null) {
                    ThanosEffect.this.drawThread.animateGroup(toSet.views, toSet.doneCallback);
                } else {
                    ThanosEffect.this.drawThread.animate(toSet.view, toSet.doneCallback);
                }
            }
            ThanosEffect.this.toSet.clear();
            Choreographer.getInstance().postFrameCallback(ThanosEffect.this.frameCallback);
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            if (ThanosEffect.this.drawThread != null) {
                ThanosEffect.this.drawThread.resize(i, i2);
            }
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            if (ThanosEffect.this.drawThread != null) {
                ThanosEffect.this.drawThread.kill();
                ThanosEffect.this.drawThread = null;
            }
            if (ThanosEffect.this.whenDone != null) {
                Runnable runnable = ThanosEffect.this.whenDone;
                ThanosEffect.this.whenDone = null;
                runnable.run();
                return false;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroy() {
        Runnable runnable = this.whenDone;
        if (runnable != null) {
            this.whenDone = null;
            runnable.run();
        }
    }

    public void scroll(int i, int i2) {
        DrawingThread drawingThread = this.drawThread;
        if (drawingThread != null) {
            boolean z = drawingThread.running;
        }
    }

    public void animateGroup(ArrayList<View> arrayList, Runnable runnable) {
        DrawingThread drawingThread = this.drawThread;
        if (drawingThread != null) {
            drawingThread.animateGroup(arrayList, runnable);
            Choreographer.getInstance().postFrameCallback(this.frameCallback);
            return;
        }
        this.toSet.add(new ToSet(arrayList, runnable));
    }

    public void animate(View view, Runnable runnable) {
        DrawingThread drawingThread = this.drawThread;
        if (drawingThread != null) {
            drawingThread.animate(view, runnable);
            Choreographer.getInstance().postFrameCallback(this.frameCallback);
            return;
        }
        this.toSet.add(new ToSet(view, runnable));
    }

    public void animate(Matrix matrix, Bitmap bitmap, Runnable runnable, Runnable runnable2) {
        DrawingThread drawingThread = this.drawThread;
        if (drawingThread != null) {
            drawingThread.animate(matrix, bitmap, runnable, runnable2);
            Choreographer.getInstance().postFrameCallback(this.frameCallback);
            return;
        }
        this.toSet.add(new ToSet(matrix, bitmap, runnable, runnable2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class DrawingThread extends DispatchQueue {
        private boolean alive;
        private int deltaTimeHandle;
        private int densityHandle;
        private Runnable destroy;
        private int drawProgram;
        private boolean drawnAnimations;
        private EGL10 egl;
        private EGLConfig eglConfig;
        private EGLContext eglContext;
        private EGLDisplay eglDisplay;
        private EGLSurface eglSurface;
        private int gridSizeHandle;
        private int height;
        private final Runnable invalidate;
        private int longevityHandle;
        private int matrixHandle;
        private int offsetHandle;
        private int particlesCountHandle;
        private final ArrayList<Animation> pendingAnimations;
        private int rectPosHandle;
        private int rectSizeHandle;
        private int resetHandle;
        public volatile boolean running;
        private int seedHandle;
        private int sizeHandle;
        private final SurfaceTexture surfaceTexture;
        private int textureHandle;
        private int timeHandle;
        private final ArrayList<Animation> toAddAnimations;
        private final ArrayList<Animation> toRunStartCallback;
        private int width;

        public DrawingThread(SurfaceTexture surfaceTexture, Runnable runnable, Runnable runnable2, int i, int i2) {
            super("ThanosEffect.DrawingThread", false);
            this.alive = true;
            this.pendingAnimations = new ArrayList<>();
            this.toRunStartCallback = new ArrayList<>();
            this.drawnAnimations = false;
            this.toAddAnimations = new ArrayList<>();
            this.surfaceTexture = surfaceTexture;
            this.invalidate = runnable;
            this.destroy = runnable2;
            this.width = i;
            this.height = i2;
            start();
        }

        @Override // org.telegram.messenger.DispatchQueue
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                draw();
            } else if (i == 1) {
                resizeInternal(message.arg1, message.arg2);
                draw();
            } else if (i == 2) {
                killInternal();
            } else if (i == 3) {
                addAnimationInternal((Animation) message.obj);
            } else if (i == 4) {
                for (int i2 = 0; i2 < this.pendingAnimations.size(); i2++) {
                    Animation animation = this.pendingAnimations.get(i2);
                    animation.offsetLeft += message.arg1;
                    animation.offsetTop += message.arg2;
                }
            }
        }

        @Override // org.telegram.messenger.DispatchQueue, java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                init();
                if (!this.toAddAnimations.isEmpty()) {
                    for (int i = 0; i < this.toAddAnimations.size(); i++) {
                        addAnimationInternal(this.toAddAnimations.get(i));
                    }
                    this.toAddAnimations.clear();
                }
                super.run();
            } catch (Exception e) {
                FileLog.e(e);
                for (int i2 = 0; i2 < this.toAddAnimations.size(); i2++) {
                    Animation animation = this.toAddAnimations.get(i2);
                    Runnable runnable = animation.startCallback;
                    if (runnable != null) {
                        AndroidUtilities.runOnUIThread(runnable);
                    }
                    animation.done(false);
                }
                this.toAddAnimations.clear();
                AndroidUtilities.runOnUIThread(ThanosEffect$DrawingThread$$ExternalSyntheticLambda1.INSTANCE);
                killInternal();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$run$0() {
            MessagesController.getGlobalMainSettings().edit().putBoolean("nothanos", ThanosEffect.nothanos = Boolean.TRUE.booleanValue()).apply();
        }

        public void requestDraw() {
            Handler handler = getHandler();
            if (handler != null) {
                handler.sendMessage(handler.obtainMessage(0));
            }
        }

        public void resize(int i, int i2) {
            Handler handler = getHandler();
            if (handler != null) {
                handler.sendMessage(handler.obtainMessage(1, i, i2));
            }
        }

        private void resizeInternal(int i, int i2) {
            if (this.alive) {
                this.width = i;
                this.height = i2;
                GLES31.glViewport(0, 0, i, i2);
                GLES31.glUniform2f(this.sizeHandle, i, i2);
            }
        }

        public void kill() {
            Handler handler = getHandler();
            if (handler != null) {
                handler.sendMessage(handler.obtainMessage(2));
            }
        }

        private void killInternal() {
            if (this.alive) {
                this.alive = false;
                for (int i = 0; i < this.pendingAnimations.size(); i++) {
                    this.pendingAnimations.get(i).done(false);
                }
                SurfaceTexture surfaceTexture = this.surfaceTexture;
                if (surfaceTexture != null) {
                    surfaceTexture.release();
                }
                Looper myLooper = Looper.myLooper();
                if (myLooper != null) {
                    myLooper.quit();
                }
                Runnable runnable = this.destroy;
                if (runnable != null) {
                    AndroidUtilities.runOnUIThread(runnable);
                    this.destroy = null;
                }
            }
        }

        private void init() {
            EGL10 egl10 = (EGL10) EGLContext.getEGL();
            this.egl = egl10;
            EGLDisplay eglGetDisplay = egl10.eglGetDisplay(0);
            this.eglDisplay = eglGetDisplay;
            EGL10 egl102 = this.egl;
            if (eglGetDisplay == EGL10.EGL_NO_DISPLAY) {
                killInternal();
            } else if (!egl102.eglInitialize(eglGetDisplay, new int[2])) {
                killInternal();
            } else {
                EGLConfig[] eGLConfigArr = new EGLConfig[1];
                if (!this.egl.eglChooseConfig(this.eglDisplay, new int[]{12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 64, 12344}, eGLConfigArr, 1, new int[1])) {
                    kill();
                    return;
                }
                EGLConfig eGLConfig = eGLConfigArr[0];
                this.eglConfig = eGLConfig;
                EGLContext eglCreateContext = this.egl.eglCreateContext(this.eglDisplay, eGLConfig, EGL10.EGL_NO_CONTEXT, new int[]{12440, 3, 12344});
                this.eglContext = eglCreateContext;
                if (eglCreateContext == null) {
                    killInternal();
                    return;
                }
                EGLSurface eglCreateWindowSurface = this.egl.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, this.surfaceTexture, null);
                this.eglSurface = eglCreateWindowSurface;
                if (eglCreateWindowSurface == null) {
                    killInternal();
                } else if (!this.egl.eglMakeCurrent(this.eglDisplay, eglCreateWindowSurface, eglCreateWindowSurface, this.eglContext)) {
                    killInternal();
                } else {
                    int glCreateShader = GLES31.glCreateShader(35633);
                    int glCreateShader2 = GLES31.glCreateShader(35632);
                    if (glCreateShader == 0 || glCreateShader2 == 0) {
                        killInternal();
                        return;
                    }
                    GLES31.glShaderSource(glCreateShader, RLottieDrawable.readRes(null, R.raw.thanos_vertex) + "\n// " + Math.random());
                    GLES31.glCompileShader(glCreateShader);
                    int[] iArr = new int[1];
                    GLES31.glGetShaderiv(glCreateShader, 35713, iArr, 0);
                    if (iArr[0] != 1) {
                        FileLog.e("ThanosEffect, compile vertex shader error: " + GLES31.glGetShaderInfoLog(glCreateShader));
                        GLES31.glDeleteShader(glCreateShader);
                        killInternal();
                        return;
                    }
                    GLES31.glShaderSource(glCreateShader2, RLottieDrawable.readRes(null, R.raw.thanos_fragment) + "\n// " + Math.random());
                    GLES31.glCompileShader(glCreateShader2);
                    GLES31.glGetShaderiv(glCreateShader2, 35713, iArr, 0);
                    if (iArr[0] != 1) {
                        FileLog.e("ThanosEffect, compile fragment shader error: " + GLES31.glGetShaderInfoLog(glCreateShader2));
                        GLES31.glDeleteShader(glCreateShader2);
                        killInternal();
                        return;
                    }
                    int glCreateProgram = GLES31.glCreateProgram();
                    this.drawProgram = glCreateProgram;
                    if (glCreateProgram == 0) {
                        killInternal();
                        return;
                    }
                    GLES31.glAttachShader(glCreateProgram, glCreateShader);
                    GLES31.glAttachShader(this.drawProgram, glCreateShader2);
                    GLES31.glTransformFeedbackVaryings(this.drawProgram, new String[]{"outUV", "outPosition", "outVelocity", "outTime"}, 35980);
                    GLES31.glLinkProgram(this.drawProgram);
                    GLES31.glGetProgramiv(this.drawProgram, 35714, iArr, 0);
                    if (iArr[0] != 1) {
                        FileLog.e("ThanosEffect, link program error: " + GLES31.glGetProgramInfoLog(this.drawProgram));
                        killInternal();
                        return;
                    }
                    this.matrixHandle = GLES31.glGetUniformLocation(this.drawProgram, "matrix");
                    this.rectSizeHandle = GLES31.glGetUniformLocation(this.drawProgram, "rectSize");
                    this.rectPosHandle = GLES31.glGetUniformLocation(this.drawProgram, "rectPos");
                    this.resetHandle = GLES31.glGetUniformLocation(this.drawProgram, "reset");
                    this.timeHandle = GLES31.glGetUniformLocation(this.drawProgram, "time");
                    this.deltaTimeHandle = GLES31.glGetUniformLocation(this.drawProgram, "deltaTime");
                    this.particlesCountHandle = GLES31.glGetUniformLocation(this.drawProgram, "particlesCount");
                    this.sizeHandle = GLES31.glGetUniformLocation(this.drawProgram, "size");
                    this.gridSizeHandle = GLES31.glGetUniformLocation(this.drawProgram, "gridSize");
                    this.textureHandle = GLES31.glGetUniformLocation(this.drawProgram, "tex");
                    this.seedHandle = GLES31.glGetUniformLocation(this.drawProgram, "seed");
                    this.densityHandle = GLES31.glGetUniformLocation(this.drawProgram, "dp");
                    this.longevityHandle = GLES31.glGetUniformLocation(this.drawProgram, "longevity");
                    this.offsetHandle = GLES31.glGetUniformLocation(this.drawProgram, "offset");
                    GLES31.glViewport(0, 0, this.width, this.height);
                    GLES31.glEnable(3042);
                    GLES31.glBlendFunc(770, 771);
                    GLES31.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                    GLES31.glUseProgram(this.drawProgram);
                    GLES31.glUniform2f(this.sizeHandle, this.width, this.height);
                }
            }
        }

        private float animationHeightPart(Animation animation) {
            int i = 0;
            for (int i2 = 0; i2 < this.pendingAnimations.size(); i2++) {
                i += this.pendingAnimations.get(i2).viewHeight;
            }
            return animation.viewHeight / i;
        }

        private void draw() {
            if (this.alive) {
                GLES31.glClear(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM);
                int i = 0;
                while (i < this.pendingAnimations.size()) {
                    Animation animation = this.pendingAnimations.get(i);
                    if (animation.firstDraw) {
                        animation.calcParticlesGrid(animationHeightPart(animation));
                        if (animation.startCallback != null) {
                            this.toRunStartCallback.add(animation);
                        }
                    }
                    this.drawnAnimations = true;
                    animation.draw();
                    if (animation.isDead()) {
                        animation.done(true);
                        this.pendingAnimations.remove(i);
                        this.running = !this.pendingAnimations.isEmpty();
                        i--;
                    }
                    i++;
                }
                checkGlErrors();
                try {
                    this.egl.eglSwapBuffers(this.eglDisplay, this.eglSurface);
                    for (int i2 = 0; i2 < this.toRunStartCallback.size(); i2++) {
                        AndroidUtilities.runOnUIThread(this.toRunStartCallback.get(i2).startCallback);
                    }
                    this.toRunStartCallback.clear();
                    if (this.pendingAnimations.isEmpty() && this.drawnAnimations) {
                        killInternal();
                    }
                } catch (Exception unused) {
                    for (int i3 = 0; i3 < this.toRunStartCallback.size(); i3++) {
                        AndroidUtilities.runOnUIThread(this.toRunStartCallback.get(i3).startCallback);
                    }
                    this.toRunStartCallback.clear();
                    for (int i4 = 0; i4 < this.pendingAnimations.size(); i4++) {
                        this.pendingAnimations.get(i4).done(false);
                    }
                    this.pendingAnimations.clear();
                    AndroidUtilities.runOnUIThread(ThanosEffect$DrawingThread$$ExternalSyntheticLambda0.INSTANCE);
                    killInternal();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$draw$1() {
            MessagesController.getGlobalMainSettings().edit().putBoolean("nothanos", ThanosEffect.nothanos = Boolean.TRUE.booleanValue()).apply();
        }

        public void animateGroup(ArrayList<View> arrayList, Runnable runnable) {
            if (this.alive) {
                Animation animation = new Animation(this, arrayList, runnable);
                Handler handler = getHandler();
                this.running = true;
                if (handler == null) {
                    this.toAddAnimations.add(animation);
                } else {
                    handler.sendMessage(handler.obtainMessage(3, animation));
                }
            }
        }

        public void animate(View view, Runnable runnable) {
            if (this.alive) {
                Animation animation = new Animation(view, runnable);
                Handler handler = getHandler();
                this.running = true;
                if (handler == null) {
                    this.toAddAnimations.add(animation);
                } else {
                    handler.sendMessage(handler.obtainMessage(3, animation));
                }
            }
        }

        public void animate(Matrix matrix, Bitmap bitmap, Runnable runnable, Runnable runnable2) {
            if (this.alive) {
                Animation animation = new Animation(matrix, bitmap, runnable, runnable2);
                Handler handler = getHandler();
                this.running = true;
                if (handler == null) {
                    this.toAddAnimations.add(animation);
                } else {
                    handler.sendMessage(handler.obtainMessage(3, animation));
                }
            }
        }

        private void addAnimationInternal(Animation animation) {
            GLES31.glGenTextures(1, animation.texture, 0);
            GLES20.glBindTexture(3553, animation.texture[0]);
            GLES20.glTexParameteri(3553, 10241, 9729);
            GLES20.glTexParameteri(3553, 10240, 9729);
            GLES20.glTexParameteri(3553, 10242, 33071);
            GLES20.glTexParameteri(3553, 10243, 33071);
            GLUtils.texImage2D(3553, 0, animation.bitmap, 0);
            GLES20.glBindTexture(3553, 0);
            animation.bitmap.recycle();
            animation.bitmap = null;
            this.pendingAnimations.add(animation);
            this.running = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public class Animation {
            private Bitmap bitmap;
            public final int[] buffer;
            public int currentBuffer;
            public boolean customMatrix;
            public final float density;
            public Runnable doneCallback;
            public boolean firstDraw;
            public final float[] glMatrixValues;
            public int gridHeight;
            public float gridSize;
            public int gridWidth;
            public boolean invalidateMatrix;
            private long lastDrawTime;
            public float left;
            public float longevity;
            public final Matrix matrix;
            public final float[] matrixValues;
            public float offsetLeft;
            public float offsetTop;
            public int particlesCount;
            public final float seed;
            public Runnable startCallback;
            public final int[] texture;
            public float time;
            public float timeScale;
            public float top;
            public int viewHeight;
            public int viewWidth;
            public ArrayList<View> views;

            public Animation(Matrix matrix, Bitmap bitmap, Runnable runnable, Runnable runnable2) {
                this.views = new ArrayList<>();
                this.lastDrawTime = -1L;
                this.time = 0.0f;
                this.firstDraw = true;
                this.offsetLeft = 0.0f;
                this.offsetTop = 0.0f;
                this.left = 0.0f;
                this.top = 0.0f;
                this.density = AndroidUtilities.density;
                this.longevity = 1.5f;
                this.timeScale = 1.12f;
                this.invalidateMatrix = true;
                this.customMatrix = false;
                this.glMatrixValues = new float[9];
                this.matrixValues = new float[9];
                Matrix matrix2 = new Matrix();
                this.matrix = matrix2;
                this.seed = (float) (Math.random() * 2.0d);
                this.texture = new int[1];
                this.buffer = new int[2];
                float[] fArr = {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
                matrix.mapPoints(fArr);
                this.left = fArr[0];
                this.top = fArr[1];
                this.viewWidth = (int) MathUtils.distance(fArr[2], fArr[3], fArr[6], fArr[7]);
                this.viewHeight = (int) MathUtils.distance(fArr[4], fArr[5], fArr[6], fArr[7]);
                this.customMatrix = true;
                matrix2.set(matrix);
                retrieveMatrixValues();
                this.startCallback = runnable;
                this.doneCallback = runnable2;
                this.bitmap = bitmap;
            }

            /* JADX WARN: Code restructure failed: missing block: B:86:0x021b, code lost:
                if (r0.messages.size() != 1) goto L90;
             */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public Animation(DrawingThread drawingThread, final ArrayList<View> arrayList, Runnable runnable) {
                ChatActivity chatActivity;
                Canvas canvas;
                ArrayList arrayList2;
                ArrayList arrayList3;
                float f;
                ArrayList arrayList4;
                ArrayList arrayList5;
                ChatActivity chatActivity2;
                float f2;
                ArrayList arrayList6;
                int i;
                ArrayList arrayList7;
                ArrayList arrayList8;
                float f3;
                int i2;
                ArrayList arrayList9;
                MessageObject.GroupedMessagePosition groupedMessagePosition;
                HashMap<MessageObject, MessageObject.GroupedMessagePosition> hashMap;
                Animation animation = this;
                DrawingThread.this = drawingThread;
                animation.views = new ArrayList<>();
                animation.lastDrawTime = -1L;
                animation.time = 0.0f;
                animation.firstDraw = true;
                animation.offsetLeft = 0.0f;
                animation.offsetTop = 0.0f;
                animation.left = 0.0f;
                animation.top = 0.0f;
                animation.density = AndroidUtilities.density;
                animation.longevity = 1.5f;
                animation.timeScale = 1.12f;
                animation.invalidateMatrix = true;
                animation.customMatrix = false;
                animation.glMatrixValues = new float[9];
                animation.matrixValues = new float[9];
                animation.matrix = new Matrix();
                animation.seed = (float) (Math.random() * 2.0d);
                animation.texture = new int[1];
                int i3 = 2;
                animation.buffer = new int[2];
                animation.views.addAll(arrayList);
                int i4 = Integer.MIN_VALUE;
                int i5 = Integer.MIN_VALUE;
                int i6 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                int i7 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                for (int i8 = 0; i8 < arrayList.size(); i8++) {
                    View view = arrayList.get(i8);
                    i7 = Math.min(i7, (int) view.getX());
                    i4 = Math.max(i4, ((int) view.getX()) + view.getWidth());
                    i6 = Math.min(i6, (int) view.getY());
                    i5 = Math.max(i5, ((int) view.getY()) + view.getHeight());
                }
                float f4 = i6;
                animation.top = f4;
                float f5 = i7;
                animation.left = f5;
                animation.viewWidth = i4 - i7;
                animation.viewHeight = i5 - i6;
                animation.doneCallback = runnable;
                animation.startCallback = new Runnable() { // from class: org.telegram.ui.Components.ThanosEffect$DrawingThread$Animation$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ThanosEffect.DrawingThread.Animation.lambda$new$0(arrayList);
                    }
                };
                for (int i9 = 0; i9 < arrayList.size(); i9++) {
                    if (arrayList.get(i9) instanceof ChatMessageCell) {
                        ((ChatMessageCell) arrayList.get(i9)).drawingToBitmap = true;
                    }
                }
                animation.bitmap = Bitmap.createBitmap(animation.viewWidth, animation.viewHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(animation.bitmap);
                if (arrayList.size() > 0 && (arrayList.get(0).getParent() instanceof RecyclerListView)) {
                    RecyclerListView recyclerListView = (RecyclerListView) arrayList.get(0).getParent();
                    if (recyclerListView.getParent() instanceof ChatActivity.ChatActivityFragmentView) {
                        ChatActivity.ChatActivityFragmentView chatActivityFragmentView = (ChatActivity.ChatActivityFragmentView) recyclerListView.getParent();
                        ChatActivity chatActivity3 = chatActivityFragmentView.getChatActivity();
                        ArrayList arrayList10 = new ArrayList(10);
                        ArrayList arrayList11 = new ArrayList();
                        ArrayList arrayList12 = new ArrayList();
                        ArrayList arrayList13 = new ArrayList();
                        canvas2.save();
                        int i10 = 0;
                        while (i10 < 3) {
                            arrayList10.clear();
                            if (i10 != i3 || recyclerListView.isFastScrollAnimationRunning()) {
                                int i11 = 0;
                                while (i11 < arrayList.size()) {
                                    View view2 = arrayList.get(i11);
                                    if (view2 instanceof ChatMessageCell) {
                                        ChatMessageCell chatMessageCell = (ChatMessageCell) view2;
                                        if (view2.getY() <= recyclerListView.getHeight() && view2.getY() + view2.getHeight() >= 0.0f && chatMessageCell.getVisibility() != 4 && chatMessageCell.getVisibility() != 8) {
                                            MessageObject.GroupedMessages currentMessagesGroup = chatMessageCell.getCurrentMessagesGroup();
                                            if (currentMessagesGroup == null || (hashMap = currentMessagesGroup.positions) == null) {
                                                f3 = f4;
                                                groupedMessagePosition = null;
                                            } else {
                                                f3 = f4;
                                                groupedMessagePosition = hashMap.get(chatMessageCell.getMessageObject());
                                            }
                                            if (i10 == 0 && (groupedMessagePosition != null || chatMessageCell.getTransitionParams().animateBackgroundBoundsInner)) {
                                                if (groupedMessagePosition == null || groupedMessagePosition.last || (groupedMessagePosition.minX == 0 && groupedMessagePosition.minY == 0)) {
                                                    if (groupedMessagePosition == null || groupedMessagePosition.last) {
                                                        arrayList11.add(chatMessageCell);
                                                    }
                                                    if ((groupedMessagePosition == null || (groupedMessagePosition.minX == 0 && groupedMessagePosition.minY == 0)) && chatMessageCell.hasNameLayout()) {
                                                        arrayList12.add(chatMessageCell);
                                                    }
                                                }
                                                if ((groupedMessagePosition != null || chatMessageCell.getTransitionParams().transformGroupToSingleMessage || chatMessageCell.getTransitionParams().animateBackgroundBoundsInner) && (groupedMessagePosition == null || (groupedMessagePosition.flags & 8) != 0)) {
                                                    arrayList13.add(chatMessageCell);
                                                }
                                            }
                                            if (currentMessagesGroup != null) {
                                                int i12 = i10 == 0 ? 1 : 1;
                                                if ((i10 != i12 || currentMessagesGroup.transitionParams.drawBackgroundForDeletedItems) && ((i10 != 0 || !chatMessageCell.getMessageObject().deleted) && ((i10 != 1 || chatMessageCell.getMessageObject().deleted) && ((i10 != 2 || chatMessageCell.willRemovedAfterAnimation()) && (i10 == 2 || !chatMessageCell.willRemovedAfterAnimation()))))) {
                                                    if (!arrayList10.contains(currentMessagesGroup)) {
                                                        MessageObject.GroupedMessages.TransitionParams transitionParams = currentMessagesGroup.transitionParams;
                                                        transitionParams.left = 0;
                                                        transitionParams.top = 0;
                                                        transitionParams.right = 0;
                                                        transitionParams.bottom = 0;
                                                        transitionParams.pinnedBotton = false;
                                                        transitionParams.pinnedTop = false;
                                                        transitionParams.cell = chatMessageCell;
                                                        arrayList10.add(currentMessagesGroup);
                                                    }
                                                    currentMessagesGroup.transitionParams.pinnedTop = chatMessageCell.isPinnedTop();
                                                    currentMessagesGroup.transitionParams.pinnedBotton = chatMessageCell.isPinnedBottom();
                                                    int left = chatMessageCell.getLeft() + chatMessageCell.getBackgroundDrawableLeft();
                                                    int left2 = chatMessageCell.getLeft() + chatMessageCell.getBackgroundDrawableRight();
                                                    int top = chatMessageCell.getTop() + chatMessageCell.getBackgroundDrawableTop();
                                                    int top2 = chatMessageCell.getTop() + chatMessageCell.getBackgroundDrawableBottom();
                                                    arrayList8 = arrayList12;
                                                    int dp = (chatMessageCell.getCurrentPosition().flags & 4) == 0 ? top - AndroidUtilities.dp(10.0f) : top;
                                                    i2 = i10;
                                                    int dp2 = (chatMessageCell.getCurrentPosition().flags & 8) == 0 ? top2 + AndroidUtilities.dp(10.0f) : top2;
                                                    if (chatMessageCell.willRemovedAfterAnimation()) {
                                                        arrayList9 = arrayList13;
                                                        currentMessagesGroup.transitionParams.cell = chatMessageCell;
                                                    } else {
                                                        arrayList9 = arrayList13;
                                                    }
                                                    MessageObject.GroupedMessages.TransitionParams transitionParams2 = currentMessagesGroup.transitionParams;
                                                    int i13 = transitionParams2.top;
                                                    if (i13 == 0 || dp < i13) {
                                                        transitionParams2.top = dp;
                                                    }
                                                    int i14 = transitionParams2.bottom;
                                                    if (i14 == 0 || dp2 > i14) {
                                                        transitionParams2.bottom = dp2;
                                                    }
                                                    int i15 = transitionParams2.left;
                                                    if (i15 == 0 || left < i15) {
                                                        transitionParams2.left = left;
                                                    }
                                                    int i16 = transitionParams2.right;
                                                    if (i16 == 0 || left2 > i16) {
                                                        transitionParams2.right = left2;
                                                    }
                                                    i11++;
                                                    arrayList13 = arrayList9;
                                                    i10 = i2;
                                                    f4 = f3;
                                                    arrayList12 = arrayList8;
                                                }
                                            }
                                            arrayList8 = arrayList12;
                                            i2 = i10;
                                            arrayList9 = arrayList13;
                                            i11++;
                                            arrayList13 = arrayList9;
                                            i10 = i2;
                                            f4 = f3;
                                            arrayList12 = arrayList8;
                                        }
                                    }
                                    arrayList8 = arrayList12;
                                    f3 = f4;
                                    i2 = i10;
                                    arrayList9 = arrayList13;
                                    i11++;
                                    arrayList13 = arrayList9;
                                    i10 = i2;
                                    f4 = f3;
                                    arrayList12 = arrayList8;
                                }
                                arrayList3 = arrayList12;
                                f = f4;
                                int i17 = i10;
                                ArrayList arrayList14 = arrayList13;
                                int i18 = 0;
                                while (i18 < arrayList10.size()) {
                                    MessageObject.GroupedMessages groupedMessages = (MessageObject.GroupedMessages) arrayList10.get(i18);
                                    float nonAnimationTranslationX = groupedMessages.transitionParams.cell.getNonAnimationTranslationX(true);
                                    MessageObject.GroupedMessages.TransitionParams transitionParams3 = groupedMessages.transitionParams;
                                    float f6 = transitionParams3.left + nonAnimationTranslationX + transitionParams3.offsetLeft;
                                    float f7 = transitionParams3.top + transitionParams3.offsetTop;
                                    float f8 = transitionParams3.offsetRight + transitionParams3.right + nonAnimationTranslationX;
                                    float f9 = transitionParams3.bottom + transitionParams3.offsetBottom;
                                    if (!transitionParams3.backgroundChangeBounds) {
                                        f7 += transitionParams3.cell.getTranslationY();
                                        f9 += groupedMessages.transitionParams.cell.getTranslationY();
                                    }
                                    ArrayList arrayList15 = arrayList10;
                                    f7 = f7 < (chatActivity3.chatListViewPaddingTop - ((float) chatActivity3.chatListViewPaddingVisibleOffset)) - ((float) AndroidUtilities.dp(20.0f)) ? (chatActivity3.chatListViewPaddingTop - chatActivity3.chatListViewPaddingVisibleOffset) - AndroidUtilities.dp(20.0f) : f7;
                                    f9 = f9 > ((float) (recyclerListView.getMeasuredHeight() + AndroidUtilities.dp(20.0f))) ? recyclerListView.getMeasuredHeight() + AndroidUtilities.dp(20.0f) : f9;
                                    float f10 = animation.top;
                                    float f11 = f7 - f10;
                                    float f12 = f9 - f10;
                                    boolean z = (groupedMessages.transitionParams.cell.getScaleX() == 1.0f && groupedMessages.transitionParams.cell.getScaleY() == 1.0f) ? false : true;
                                    if (z) {
                                        canvas2.save();
                                        arrayList7 = arrayList11;
                                        canvas2.scale(groupedMessages.transitionParams.cell.getScaleX(), groupedMessages.transitionParams.cell.getScaleY(), f6 + ((f8 - f6) / 2.0f), f11 + ((f12 - f11) / 2.0f));
                                    } else {
                                        arrayList7 = arrayList11;
                                    }
                                    MessageObject.GroupedMessages.TransitionParams transitionParams4 = groupedMessages.transitionParams;
                                    ChatActivity chatActivity4 = chatActivity3;
                                    float f13 = f5;
                                    int i19 = i18;
                                    int i20 = i17;
                                    ArrayList arrayList16 = arrayList14;
                                    transitionParams4.cell.drawBackground(canvas2, (int) f6, (int) f11, (int) f8, (int) f12, transitionParams4.pinnedTop, transitionParams4.pinnedBotton, false, chatActivityFragmentView.getKeyboardHeight());
                                    MessageObject.GroupedMessages.TransitionParams transitionParams5 = groupedMessages.transitionParams;
                                    transitionParams5.cell = null;
                                    transitionParams5.drawCaptionLayout = groupedMessages.hasCaption;
                                    if (z) {
                                        canvas2.restore();
                                        for (int i21 = 0; i21 < arrayList.size(); i21++) {
                                            View view3 = arrayList.get(i21);
                                            if (view3 instanceof ChatMessageCell) {
                                                ChatMessageCell chatMessageCell2 = (ChatMessageCell) view3;
                                                if (chatMessageCell2.getCurrentMessagesGroup() == groupedMessages) {
                                                    int left3 = chatMessageCell2.getLeft();
                                                    int top3 = chatMessageCell2.getTop();
                                                    view3.setPivotX((f6 - left3) + ((f8 - f6) / 2.0f));
                                                    view3.setPivotY((f11 - top3) + ((f12 - f11) / 2.0f));
                                                }
                                            }
                                        }
                                    }
                                    i18 = i19 + 1;
                                    animation = this;
                                    arrayList10 = arrayList15;
                                    arrayList11 = arrayList7;
                                    chatActivity3 = chatActivity4;
                                    f5 = f13;
                                    i17 = i20;
                                    arrayList14 = arrayList16;
                                }
                                arrayList4 = arrayList10;
                                arrayList5 = arrayList11;
                                chatActivity2 = chatActivity3;
                                f2 = f5;
                                arrayList6 = arrayList14;
                                i = i17;
                            } else {
                                arrayList4 = arrayList10;
                                arrayList3 = arrayList12;
                                arrayList5 = arrayList11;
                                chatActivity2 = chatActivity3;
                                f = f4;
                                f2 = f5;
                                i = i10;
                                arrayList6 = arrayList13;
                            }
                            i10 = i + 1;
                            animation = this;
                            f4 = f;
                            arrayList12 = arrayList3;
                            arrayList10 = arrayList4;
                            arrayList11 = arrayList5;
                            chatActivity3 = chatActivity2;
                            f5 = f2;
                            arrayList13 = arrayList6;
                            i3 = 2;
                        }
                        ArrayList arrayList17 = arrayList12;
                        ArrayList arrayList18 = arrayList11;
                        ChatActivity chatActivity5 = chatActivity3;
                        float f14 = f4;
                        float f15 = f5;
                        ArrayList arrayList19 = arrayList13;
                        for (int i22 = 0; i22 < arrayList.size(); i22++) {
                            View view4 = arrayList.get(i22);
                            canvas2.save();
                            canvas2.translate(view4.getX() - f15, view4.getY() - f14);
                            view4.draw(canvas2);
                            if (view4 instanceof ChatMessageCell) {
                                ((ChatMessageCell) view4).drawOutboundsContent(canvas2);
                            } else if (view4 instanceof ChatActionCell) {
                                ((ChatActionCell) view4).drawOutboundsContent(canvas2);
                            }
                            canvas2.restore();
                        }
                        ChatActivity chatActivity6 = chatActivity5;
                        float y = ((recyclerListView.getY() + chatActivity6.chatListViewPaddingTop) - chatActivity6.chatListViewPaddingVisibleOffset) - AndroidUtilities.dp(4.0f);
                        int size = arrayList18.size();
                        if (size > 0) {
                            int i23 = 0;
                            while (i23 < size) {
                                ArrayList arrayList20 = arrayList18;
                                ChatMessageCell chatMessageCell3 = (ChatMessageCell) arrayList20.get(i23);
                                drawChildElement(recyclerListView, chatActivity6, canvas2, y, chatMessageCell3, 0, chatMessageCell3.getX() - f15, chatMessageCell3.getY() - f14);
                                i23++;
                                chatActivity6 = chatActivity6;
                                canvas2 = canvas2;
                                arrayList18 = arrayList20;
                            }
                            chatActivity = chatActivity6;
                            canvas = canvas2;
                            arrayList2 = arrayList17;
                            arrayList18.clear();
                        } else {
                            chatActivity = chatActivity6;
                            canvas = canvas2;
                            arrayList2 = arrayList17;
                        }
                        int size2 = arrayList2.size();
                        if (size2 > 0) {
                            for (int i24 = 0; i24 < size2; i24++) {
                                ChatMessageCell chatMessageCell4 = (ChatMessageCell) arrayList2.get(i24);
                                drawChildElement(recyclerListView, chatActivity, canvas, y, chatMessageCell4, 1, chatMessageCell4.getX() - f15, chatMessageCell4.getY() - f14);
                            }
                            arrayList2.clear();
                        }
                        int size3 = arrayList19.size();
                        if (size3 > 0) {
                            int i25 = 0;
                            while (i25 < size3) {
                                ArrayList arrayList21 = arrayList19;
                                ChatMessageCell chatMessageCell5 = (ChatMessageCell) arrayList21.get(i25);
                                if (chatMessageCell5.getCurrentPosition() != null || chatMessageCell5.getTransitionParams().animateBackgroundBoundsInner) {
                                    drawChildElement(recyclerListView, chatActivity, canvas, y, chatMessageCell5, 2, chatMessageCell5.getX() - f15, chatMessageCell5.getY() - f14);
                                }
                                i25++;
                                arrayList19 = arrayList21;
                            }
                            arrayList19.clear();
                        }
                        canvas.restore();
                        for (int i26 = 0; i26 < arrayList.size(); i26++) {
                            if (arrayList.get(i26) instanceof ChatMessageCell) {
                                ((ChatMessageCell) arrayList.get(i26)).drawingToBitmap = false;
                            }
                        }
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static /* synthetic */ void lambda$new$0(ArrayList arrayList) {
                for (int i = 0; i < arrayList.size(); i++) {
                    ((View) arrayList.get(i)).setVisibility(8);
                    if (arrayList.get(i) instanceof ChatMessageCell) {
                        ((ChatMessageCell) arrayList.get(i)).setCheckBoxVisible(false, false);
                        ((ChatMessageCell) arrayList.get(i)).setChecked(false, false, false);
                    }
                }
            }

            private void drawChildElement(View view, ChatActivity chatActivity, Canvas canvas, float f, ChatMessageCell chatMessageCell, int i, float f2, float f3) {
                canvas.save();
                float alpha = chatMessageCell.shouldDrawAlphaLayer() ? chatMessageCell.getAlpha() : 1.0f;
                canvas.translate(f2, f3);
                boolean z = true;
                chatMessageCell.setInvalidatesParent(true);
                if (i == 0) {
                    chatMessageCell.drawTime(canvas, alpha, true);
                } else if (i == 1) {
                    chatMessageCell.drawNamesLayout(canvas, alpha);
                } else {
                    chatMessageCell.drawCaptionLayout(canvas, (chatMessageCell.getCurrentPosition() == null || (chatMessageCell.getCurrentPosition().flags & 1) != 0) ? false : false, alpha);
                }
                chatMessageCell.setInvalidatesParent(false);
                canvas.restore();
            }

            public void calcParticlesGrid(float f) {
                int i;
                int i2;
                int devicePerformanceClass = SharedConfig.getDevicePerformanceClass();
                int i3 = devicePerformanceClass != 1 ? devicePerformanceClass != 2 ? 30000 : 120000 : 60000;
                float max = Math.max(AndroidUtilities.dpf2(0.4f), 1.0f);
                int clamp = Utilities.clamp((int) ((this.viewWidth * this.viewHeight) / (max * max)), (int) (i3 * f), 10);
                this.particlesCount = clamp;
                float f2 = this.viewWidth / this.viewHeight;
                int round = (int) Math.round(Math.sqrt(clamp / f2));
                this.gridHeight = round;
                this.gridWidth = Math.round(this.particlesCount / round);
                while (true) {
                    i = this.gridWidth;
                    i2 = this.gridHeight;
                    if (i * i2 >= this.particlesCount) {
                        break;
                    } else if (i / i2 < f2) {
                        this.gridWidth = i + 1;
                    } else {
                        this.gridHeight = i2 + 1;
                    }
                }
                this.particlesCount = i * i2;
                this.gridSize = Math.max(this.viewWidth / i, this.viewHeight / i2);
                GLES31.glGenBuffers(2, this.buffer, 0);
                for (int i4 = 0; i4 < 2; i4++) {
                    GLES31.glBindBuffer(34962, this.buffer[i4]);
                    GLES31.glBufferData(34962, this.particlesCount * 28, null, 35048);
                }
            }

            /* JADX WARN: Removed duplicated region for block: B:19:0x00e0  */
            /* JADX WARN: Removed duplicated region for block: B:26:0x0104  */
            /* JADX WARN: Removed duplicated region for block: B:27:0x010b  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public Animation(View view, Runnable runnable) {
                this.views = new ArrayList<>();
                this.lastDrawTime = -1L;
                this.time = 0.0f;
                this.firstDraw = true;
                this.offsetLeft = 0.0f;
                this.offsetTop = 0.0f;
                this.left = 0.0f;
                this.top = 0.0f;
                this.density = AndroidUtilities.density;
                this.longevity = 1.5f;
                this.timeScale = 1.12f;
                this.invalidateMatrix = true;
                this.customMatrix = false;
                this.glMatrixValues = new float[9];
                this.matrixValues = new float[9];
                this.matrix = new Matrix();
                this.seed = (float) (Math.random() * 2.0d);
                this.texture = new int[1];
                this.buffer = new int[2];
                this.views.add(view);
                this.viewWidth = view.getWidth();
                this.viewHeight = view.getHeight();
                this.top = view.getY();
                this.left = 0.0f;
                if (view instanceof BaseCell) {
                    BaseCell baseCell = (BaseCell) view;
                    this.viewWidth = Math.max(1, baseCell.getBoundsRight() - baseCell.getBoundsLeft());
                    this.left += baseCell.getBoundsLeft();
                }
                this.doneCallback = runnable;
                this.startCallback = new Runnable() { // from class: org.telegram.ui.Components.ThanosEffect$DrawingThread$Animation$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ThanosEffect.DrawingThread.Animation.this.lambda$new$1();
                    }
                };
                this.bitmap = Bitmap.createBitmap(this.viewWidth, this.viewHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(this.bitmap);
                canvas.save();
                canvas.translate(-this.left, 0.0f);
                boolean z = view instanceof ChatMessageCell;
                if (z) {
                    ((ChatMessageCell) view).drawingToBitmap = true;
                }
                boolean z2 = view instanceof ChatActionCell;
                if (z2) {
                    ChatActionCell chatActionCell = (ChatActionCell) view;
                    if (chatActionCell.hasGradientService()) {
                        chatActionCell.drawBackground(canvas, true);
                        view.draw(canvas);
                        if (z) {
                            ChatMessageCell chatMessageCell = (ChatMessageCell) view;
                            ImageReceiver avatarImage = chatMessageCell.getAvatarImage();
                            if (avatarImage != null && avatarImage.getVisible()) {
                                canvas.save();
                                canvas.translate(0.0f, -view.getY());
                                avatarImage.draw(canvas);
                                canvas.restore();
                            }
                            chatMessageCell.drawingToBitmap = false;
                        }
                        if (!z) {
                            ((ChatMessageCell) view).drawOutboundsContent(canvas);
                        } else if (z2) {
                            ((ChatActionCell) view).drawOutboundsContent(canvas);
                        }
                        canvas.restore();
                        this.left += view.getX();
                    }
                }
                if (z) {
                    ChatMessageCell chatMessageCell2 = (ChatMessageCell) view;
                    if (chatMessageCell2.drawBackgroundInParent()) {
                        chatMessageCell2.drawBackgroundInternal(canvas, true);
                    }
                }
                view.draw(canvas);
                if (z) {
                }
                if (!z) {
                }
                canvas.restore();
                this.left += view.getX();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$new$1() {
                for (int i = 0; i < this.views.size(); i++) {
                    this.views.get(i).setVisibility(8);
                    if (this.views.get(i) instanceof ChatMessageCell) {
                        ((ChatMessageCell) this.views.get(i)).setCheckBoxVisible(false, false);
                        ((ChatMessageCell) this.views.get(i)).setChecked(false, false, false);
                    }
                }
            }

            private void retrieveMatrixValues() {
                this.matrix.getValues(this.matrixValues);
                float[] fArr = this.glMatrixValues;
                float[] fArr2 = this.matrixValues;
                fArr[0] = fArr2[0];
                fArr[1] = fArr2[3];
                fArr[2] = fArr2[6];
                fArr[3] = fArr2[1];
                fArr[4] = fArr2[4];
                fArr[5] = fArr2[7];
                fArr[6] = fArr2[2];
                fArr[7] = fArr2[5];
                fArr[8] = fArr2[8];
                this.invalidateMatrix = false;
            }

            public void draw() {
                double d;
                long nanoTime = System.nanoTime();
                long j = this.lastDrawTime;
                if (j < 0) {
                    d = 0.0d;
                } else {
                    double d2 = nanoTime - j;
                    Double.isNaN(d2);
                    d = d2 / 1.0E9d;
                }
                this.lastDrawTime = nanoTime;
                if (this.invalidateMatrix && !this.customMatrix) {
                    this.matrix.reset();
                    this.matrix.postScale(this.viewWidth, this.viewHeight);
                    this.matrix.postTranslate(this.left, this.top);
                    retrieveMatrixValues();
                }
                double d3 = this.time;
                double d4 = this.timeScale;
                Double.isNaN(d4);
                Double.isNaN(d3);
                this.time = (float) (d3 + (d4 * d));
                GLES31.glUniformMatrix3fv(DrawingThread.this.matrixHandle, 1, false, this.glMatrixValues, 0);
                GLES31.glUniform1f(DrawingThread.this.resetHandle, this.firstDraw ? 1.0f : 0.0f);
                GLES31.glUniform1f(DrawingThread.this.timeHandle, this.time);
                GLES31.glUniform1f(DrawingThread.this.deltaTimeHandle, ((float) d) * this.timeScale);
                GLES31.glUniform1f(DrawingThread.this.particlesCountHandle, this.particlesCount);
                GLES31.glUniform3f(DrawingThread.this.gridSizeHandle, this.gridWidth, this.gridHeight, this.gridSize);
                GLES31.glUniform2f(DrawingThread.this.offsetHandle, this.offsetLeft, this.offsetTop);
                GLES31.glUniform2f(DrawingThread.this.rectSizeHandle, this.viewWidth, this.viewHeight);
                GLES31.glUniform1f(DrawingThread.this.seedHandle, this.seed);
                GLES31.glUniform2f(DrawingThread.this.rectPosHandle, 0.0f, 0.0f);
                GLES31.glUniform1f(DrawingThread.this.densityHandle, this.density);
                GLES31.glUniform1f(DrawingThread.this.longevityHandle, this.longevity);
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(3553, this.texture[0]);
                GLES31.glUniform1i(DrawingThread.this.textureHandle, 0);
                GLES31.glBindBuffer(34962, this.buffer[this.currentBuffer]);
                GLES31.glVertexAttribPointer(0, 2, 5126, false, 28, 0);
                GLES31.glEnableVertexAttribArray(0);
                GLES31.glVertexAttribPointer(1, 2, 5126, false, 28, 8);
                GLES31.glEnableVertexAttribArray(1);
                GLES31.glVertexAttribPointer(2, 2, 5126, false, 28, 16);
                GLES31.glEnableVertexAttribArray(2);
                GLES31.glVertexAttribPointer(3, 1, 5126, false, 28, 24);
                GLES31.glEnableVertexAttribArray(3);
                GLES31.glBindBufferBase(35982, 0, this.buffer[1 - this.currentBuffer]);
                GLES31.glVertexAttribPointer(0, 2, 5126, false, 28, 0);
                GLES31.glEnableVertexAttribArray(0);
                GLES31.glVertexAttribPointer(1, 2, 5126, false, 28, 8);
                GLES31.glEnableVertexAttribArray(1);
                GLES31.glVertexAttribPointer(2, 2, 5126, false, 28, 16);
                GLES31.glEnableVertexAttribArray(2);
                GLES31.glVertexAttribPointer(3, 1, 5126, false, 28, 24);
                GLES31.glEnableVertexAttribArray(3);
                GLES31.glBeginTransformFeedback(0);
                GLES31.glDrawArrays(0, 0, this.particlesCount);
                GLES31.glEndTransformFeedback();
                GLES31.glBindBuffer(34962, 0);
                GLES31.glBindBuffer(35982, 0);
                this.firstDraw = false;
                this.currentBuffer = 1 - this.currentBuffer;
            }

            public boolean isDead() {
                return this.time > this.longevity + 0.9f;
            }

            public void done(boolean z) {
                try {
                    GLES31.glDeleteBuffers(2, this.buffer, 0);
                } catch (Exception e) {
                    FileLog.e(e);
                }
                if (DrawingThread.this.drawProgram != 0) {
                    try {
                        GLES31.glDeleteProgram(DrawingThread.this.drawProgram);
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                    DrawingThread.this.drawProgram = 0;
                }
                try {
                    GLES31.glDeleteTextures(1, this.texture, 0);
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
                if (this.doneCallback != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ThanosEffect$DrawingThread$Animation$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            ThanosEffect.DrawingThread.Animation.this.lambda$done$2();
                        }
                    });
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$done$2() {
                Runnable runnable = this.doneCallback;
                if (runnable != null) {
                    runnable.run();
                }
            }
        }

        private void checkGlErrors() {
            while (true) {
                int glGetError = GLES31.glGetError();
                if (glGetError == 0) {
                    return;
                }
                FileLog.e("thanos gles error " + glGetError);
            }
        }
    }
}
