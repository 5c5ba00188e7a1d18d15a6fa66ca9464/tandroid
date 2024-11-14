package org.telegram.messenger.camera;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Range;
import android.view.Surface;
import android.view.WindowManager;
import androidx.core.os.BundleKt$$ExternalSyntheticApiModelOutline1;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.camera.Camera2Session;

/* loaded from: classes3.dex */
public class Camera2Session {
    private CameraCharacteristics cameraCharacteristics;
    private CameraDevice cameraDevice;
    public final String cameraId;
    private final CameraManager cameraManager;
    private final CameraDevice.StateCallback cameraStateCallback;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession captureSession;
    private final CameraCaptureSession.StateCallback captureStateCallback;
    private Runnable doneCallback;
    private boolean flashing;
    private Handler handler;
    private ImageReader imageReader;
    private boolean isClosed;
    private boolean isError;
    private final boolean isFront;
    private boolean isSuccess;
    private long lastTime;
    private float maxZoom;
    private boolean nightMode;
    private final android.util.Size previewSize;
    private boolean recordingVideo;
    private boolean scanningBarcode;
    private Rect sensorSize;
    private Surface surface;
    private SurfaceTexture surfaceTexture;
    private HandlerThread thread;
    private float currentZoom = 1.0f;
    private boolean opened = false;
    private final Rect cropRegion = new Rect();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 1 extends CameraDevice.StateCallback {
        final /* synthetic */ String val$cameraId;

        1(String str) {
            this.val$cameraId = str;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onError$0() {
            Camera2Session.this.isError = true;
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice cameraDevice) {
            Camera2Session.this.cameraDevice = cameraDevice;
            FileLog.d("Camera2Session camera #" + this.val$cameraId + " disconnected");
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice cameraDevice, int i) {
            Camera2Session.this.cameraDevice = cameraDevice;
            FileLog.e("Camera2Session camera #" + this.val$cameraId + " received " + i + " error");
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Camera2Session.1.this.lambda$onError$0();
                }
            });
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice cameraDevice) {
            Camera2Session.this.cameraDevice = cameraDevice;
            Camera2Session.this.lastTime = System.currentTimeMillis();
            FileLog.d("Camera2Session camera #" + this.val$cameraId + " opened");
            Camera2Session.this.checkOpen();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 2 extends CameraCaptureSession.StateCallback {
        final /* synthetic */ String val$cameraId;

        2(String str) {
            this.val$cameraId = str;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigureFailed$1() {
            Camera2Session.this.isError = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigured$0() {
            Camera2Session.this.isSuccess = true;
            if (Camera2Session.this.doneCallback != null) {
                Camera2Session.this.doneCallback.run();
                Camera2Session.this.doneCallback = null;
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
            Camera2Session.this.captureSession = cameraCaptureSession;
            FileLog.e("Camera2Session camera #" + this.val$cameraId + " capture session failed to configure");
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    Camera2Session.2.this.lambda$onConfigureFailed$1();
                }
            });
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
            Camera2Session.this.captureSession = cameraCaptureSession;
            FileLog.e("Camera2Session camera #" + this.val$cameraId + " capture session configured");
            Camera2Session.this.lastTime = System.currentTimeMillis();
            try {
                Camera2Session.this.updateCaptureRequest();
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        Camera2Session.2.this.lambda$onConfigured$0();
                    }
                });
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* loaded from: classes3.dex */
    class 3 implements ImageReader.OnImageAvailableListener {
        final /* synthetic */ File val$file;
        final /* synthetic */ int val$orientation;
        final /* synthetic */ Utilities.Callback val$whenDone;

        3(File file, Utilities.Callback callback, int i) {
            this.val$file = file;
            this.val$whenDone = callback;
            this.val$orientation = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onImageAvailable$0(Utilities.Callback callback, int i) {
            if (callback != null) {
                callback.run(Integer.valueOf(i));
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x003d, code lost:
        
            if (r2 == null) goto L23;
         */
        /* JADX WARN: Removed duplicated region for block: B:25:0x0055 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // android.media.ImageReader.OnImageAvailableListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onImageAvailable(ImageReader imageReader) {
            FileOutputStream fileOutputStream;
            Throwable th;
            IOException e;
            Image acquireLatestImage = imageReader.acquireLatestImage();
            ByteBuffer buffer = acquireLatestImage.getPlanes()[0].getBuffer();
            byte[] bArr = new byte[buffer.remaining()];
            buffer.get(bArr);
            try {
                try {
                    fileOutputStream = new FileOutputStream(this.val$file);
                    try {
                        fileOutputStream.write(bArr);
                        acquireLatestImage.close();
                    } catch (IOException e2) {
                        e = e2;
                        e.printStackTrace();
                        acquireLatestImage.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    acquireLatestImage.close();
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (IOException e4) {
                fileOutputStream = null;
                e = e4;
            } catch (Throwable th3) {
                fileOutputStream = null;
                th = th3;
                acquireLatestImage.close();
                if (fileOutputStream != null) {
                }
                throw th;
            }
            try {
                fileOutputStream.close();
            } catch (IOException e5) {
                e5.printStackTrace();
            }
            final Utilities.Callback callback = this.val$whenDone;
            final int i = this.val$orientation;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Camera2Session.3.lambda$onImageAvailable$0(Utilities.Callback.this, i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class CompareSizesByArea implements Comparator<android.util.Size> {
        CompareSizesByArea() {
        }

        public int compare(android.util.Size size, android.util.Size size2) {
            int width;
            int height;
            int width2;
            int height2;
            width = size.getWidth();
            height = size.getHeight();
            long j = width * height;
            width2 = size2.getWidth();
            long j2 = width2;
            height2 = size2.getHeight();
            return Long.signum(j - (j2 * height2));
        }

        @Override // java.util.Comparator
        public /* bridge */ /* synthetic */ int compare(android.util.Size size, android.util.Size size2) {
            return compare(BundleKt$$ExternalSyntheticApiModelOutline1.m(size), BundleKt$$ExternalSyntheticApiModelOutline1.m(size2));
        }
    }

    private Camera2Session(Context context, boolean z, String str, android.util.Size size) {
        int width;
        int height;
        CameraCharacteristics cameraCharacteristics;
        CameraCharacteristics.Key key;
        Object obj;
        CameraCharacteristics.Key key2;
        Object obj2;
        float f = 1.0f;
        this.maxZoom = 1.0f;
        HandlerThread handlerThread = new HandlerThread("tg_camera2");
        this.thread = handlerThread;
        handlerThread.start();
        this.handler = new Handler(this.thread.getLooper());
        1 r1 = new 1(str);
        this.cameraStateCallback = r1;
        this.captureStateCallback = new 2(str);
        this.isFront = z;
        this.cameraId = str;
        this.previewSize = size;
        this.lastTime = System.currentTimeMillis();
        width = size.getWidth();
        height = size.getHeight();
        this.imageReader = ImageReader.newInstance(width, height, 256, 1);
        CameraManager m = Camera2Session$$ExternalSyntheticApiModelOutline9.m(context.getSystemService("camera"));
        this.cameraManager = m;
        try {
            cameraCharacteristics = m.getCameraCharacteristics(str);
            this.cameraCharacteristics = cameraCharacteristics;
            key = CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE;
            obj = cameraCharacteristics.get(key);
            this.sensorSize = (Rect) obj;
            CameraCharacteristics cameraCharacteristics2 = this.cameraCharacteristics;
            key2 = CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM;
            obj2 = cameraCharacteristics2.get(key2);
            Float f2 = (Float) obj2;
            if (f2 != null && f2.floatValue() >= 1.0f) {
                f = f2.floatValue();
            }
            this.maxZoom = f;
            m.openCamera(str, r1, this.handler);
        } catch (Exception e) {
            FileLog.e(e);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    Camera2Session.this.lambda$new$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkOpen() {
        if (this.opened || this.surfaceTexture == null || this.cameraDevice == null) {
            return;
        }
        this.opened = true;
        this.surface = new Surface(this.surfaceTexture);
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(this.surface);
            arrayList.add(this.imageReader.getSurface());
            this.cameraDevice.createCaptureSession(arrayList, this.captureStateCallback, null);
        } catch (Exception e) {
            FileLog.e(e);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$$ExternalSyntheticLambda33
                @Override // java.lang.Runnable
                public final void run() {
                    Camera2Session.this.lambda$checkOpen$2();
                }
            });
        }
    }

    public static android.util.Size chooseOptimalSize(android.util.Size[] sizeArr, int i, int i2, boolean z) {
        int height;
        int width;
        int height2;
        int width2;
        int width3;
        int height3;
        int width4;
        int height4;
        int height5;
        int width5;
        ArrayList arrayList = new ArrayList(sizeArr.length);
        ArrayList arrayList2 = new ArrayList(sizeArr.length);
        for (android.util.Size size : sizeArr) {
            if (z) {
                height5 = size.getHeight();
                if (height5 <= i2) {
                    width5 = size.getWidth();
                    if (width5 > i) {
                    }
                }
            }
            height = size.getHeight();
            width = size.getWidth();
            if (height == (width * i2) / i) {
                width4 = size.getWidth();
                if (width4 >= i) {
                    height4 = size.getHeight();
                    if (height4 >= i2) {
                        arrayList.add(size);
                    }
                }
            }
            height2 = size.getHeight();
            width2 = size.getWidth();
            if (height2 * width2 <= i * i2 * 4) {
                width3 = size.getWidth();
                if (width3 >= i) {
                    height3 = size.getHeight();
                    if (height3 >= i2) {
                        arrayList2.add(size);
                    }
                }
            }
        }
        return BundleKt$$ExternalSyntheticApiModelOutline1.m(arrayList.size() > 0 ? Collections.min(arrayList, new CompareSizesByArea()) : arrayList2.size() > 0 ? Collections.min(arrayList2, new CompareSizesByArea()) : Collections.max(Arrays.asList(sizeArr), new CompareSizesByArea()));
    }

    public static Camera2Session create(boolean z, int i, int i2) {
        String str;
        android.util.Size size;
        String[] cameraIdList;
        CameraCharacteristics cameraCharacteristics;
        CameraCharacteristics.Key key;
        Object obj;
        CameraCharacteristics.Key key2;
        Object obj2;
        CameraCharacteristics.Key key3;
        Object obj3;
        int width;
        int height;
        float f;
        CameraManager cameraManager;
        android.util.Size[] outputSizes;
        Context context = ApplicationLoader.applicationContext;
        CameraManager m = Camera2Session$$ExternalSyntheticApiModelOutline9.m(context.getSystemService("camera"));
        try {
            cameraIdList = m.getCameraIdList();
            int i3 = 0;
            str = null;
            size = null;
            float f2 = 0.0f;
            while (i3 < cameraIdList.length) {
                try {
                    String str2 = cameraIdList[i3];
                    cameraCharacteristics = m.getCameraCharacteristics(str2);
                    if (cameraCharacteristics != null) {
                        key = CameraCharacteristics.LENS_FACING;
                        obj = cameraCharacteristics.get(key);
                        if (((Integer) obj).intValue() == (!z)) {
                            key2 = CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP;
                            obj2 = cameraCharacteristics.get(key2);
                            StreamConfigurationMap m2 = Camera2Session$$ExternalSyntheticApiModelOutline19.m(obj2);
                            key3 = CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE;
                            obj3 = cameraCharacteristics.get(key3);
                            android.util.Size m3 = BundleKt$$ExternalSyntheticApiModelOutline1.m(obj3);
                            if (m3 == null) {
                                f = 0.0f;
                            } else {
                                width = m3.getWidth();
                                height = m3.getHeight();
                                f = width / height;
                            }
                            float f3 = i / i2;
                            cameraManager = m;
                            if ((f3 >= 1.0f) != (f >= 1.0f)) {
                                f = 1.0f / f;
                            }
                            if (f2 > 0.0f) {
                                if (Math.abs(f3 - f2) > Math.abs(f3 - f)) {
                                }
                                i3++;
                                m = cameraManager;
                            }
                            if (m2 != null && Build.VERSION.SDK_INT >= 23) {
                                outputSizes = m2.getOutputSizes(SurfaceTexture.class);
                                android.util.Size chooseOptimalSize = chooseOptimalSize(outputSizes, i, i2, false);
                                if (chooseOptimalSize != null) {
                                    size = chooseOptimalSize;
                                    str = str2;
                                    f2 = f;
                                }
                                i3++;
                                m = cameraManager;
                            }
                            i3++;
                            m = cameraManager;
                        }
                    }
                    cameraManager = m;
                    i3++;
                    m = cameraManager;
                } catch (Exception e) {
                    e = e;
                    FileLog.e(e);
                    return str == null ? null : null;
                }
            }
        } catch (Exception e2) {
            e = e2;
            str = null;
            size = null;
        }
        if (str == null && size != null) {
            return new Camera2Session(context, z, str, size);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x003c A[Catch: Exception -> 0x0046, TryCatch #0 {Exception -> 0x0046, blocks: (B:3:0x0001, B:7:0x0006, B:16:0x002c, B:18:0x003c, B:19:0x004f, B:22:0x0048), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0048 A[Catch: Exception -> 0x0046, TryCatch #0 {Exception -> 0x0046, blocks: (B:3:0x0001, B:7:0x0006, B:16:0x002c, B:18:0x003c, B:19:0x004f, B:22:0x0048), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int getJpegOrientation() {
        int i;
        CameraCharacteristics.Key key;
        Object obj;
        try {
            Context context = ApplicationLoader.applicationContext;
            if (context == null) {
                return 0;
            }
            int rotation = ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation();
            if (rotation != 0) {
                if (rotation == 1) {
                    i = 90;
                } else if (rotation == 2) {
                    i = NotificationCenter.updateBotMenuButton;
                } else if (rotation == 3) {
                    i = NotificationCenter.attachMenuBotsDidLoad;
                }
                CameraCharacteristics cameraCharacteristics = this.cameraCharacteristics;
                key = CameraCharacteristics.SENSOR_ORIENTATION;
                obj = cameraCharacteristics.get(key);
                Integer num = (Integer) obj;
                return (!this.isFront ? 360 - ((num.intValue() + i) % 360) : (num.intValue() - i) + 360) % 360;
            }
            i = 0;
            CameraCharacteristics cameraCharacteristics2 = this.cameraCharacteristics;
            key = CameraCharacteristics.SENSOR_ORIENTATION;
            obj = cameraCharacteristics2.get(key);
            Integer num2 = (Integer) obj;
            return (!this.isFront ? 360 - ((num2.intValue() + i) % 360) : (num2.intValue() - i) + 360) % 360;
        } catch (Exception e) {
            FileLog.e(e);
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkOpen$2() {
        this.isError = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$destroy$3(Runnable runnable) {
        try {
            this.thread.join();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$destroy$4(final Runnable runnable) {
        CameraCaptureSession cameraCaptureSession = this.captureSession;
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            this.captureSession = null;
        }
        CameraDevice cameraDevice = this.cameraDevice;
        if (cameraDevice != null) {
            cameraDevice.close();
            this.cameraDevice = null;
        }
        ImageReader imageReader = this.imageReader;
        if (imageReader != null) {
            imageReader.close();
            this.imageReader = null;
        }
        this.thread.quitSafely();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                Camera2Session.this.lambda$destroy$3(runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.isError = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$open$1(SurfaceTexture surfaceTexture) {
        this.surfaceTexture = surfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.setDefaultBufferSize(getPreviewWidth(), getPreviewHeight());
        }
        checkOpen();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0057 A[Catch: Exception -> 0x0036, TryCatch #0 {Exception -> 0x0036, blocks: (B:8:0x000e, B:11:0x001e, B:13:0x0028, B:14:0x0032, B:15:0x004d, B:17:0x0057, B:20:0x005e, B:22:0x0069, B:23:0x0090, B:25:0x0094, B:27:0x00a4, B:28:0x00e4, B:33:0x0039, B:35:0x003d, B:38:0x0048, B:40:0x0017), top: B:7:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0069 A[Catch: Exception -> 0x0036, TryCatch #0 {Exception -> 0x0036, blocks: (B:8:0x000e, B:11:0x001e, B:13:0x0028, B:14:0x0032, B:15:0x004d, B:17:0x0057, B:20:0x005e, B:22:0x0069, B:23:0x0090, B:25:0x0094, B:27:0x00a4, B:28:0x00e4, B:33:0x0039, B:35:0x003d, B:38:0x0048, B:40:0x0017), top: B:7:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x005d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateCaptureRequest() {
        CaptureRequest.Builder createCaptureRequest;
        CaptureRequest.Key key;
        int valueOf;
        CaptureRequest.Key key2;
        CaptureRequest build;
        CaptureRequest.Key key3;
        CaptureRequest.Key key4;
        CaptureRequest.Key key5;
        CameraDevice cameraDevice = this.cameraDevice;
        if (cameraDevice == null || this.surface == null || this.captureSession == null) {
            return;
        }
        try {
            int i = 1;
            createCaptureRequest = cameraDevice.createCaptureRequest(this.recordingVideo ? 3 : this.scanningBarcode ? 2 : 1);
            this.captureRequestBuilder = createCaptureRequest;
            if (!this.scanningBarcode) {
                if (this.nightMode) {
                    key = CaptureRequest.CONTROL_SCENE_MODE;
                    valueOf = Integer.valueOf(this.isFront ? 6 : 5);
                }
                CaptureRequest.Builder builder = this.captureRequestBuilder;
                key2 = CaptureRequest.FLASH_MODE;
                if (this.flashing) {
                    i = 0;
                } else if (this.recordingVideo) {
                    i = 2;
                }
                builder.set(key2, Integer.valueOf(i));
                if (this.recordingVideo) {
                    CaptureRequest.Builder builder2 = this.captureRequestBuilder;
                    key4 = CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE;
                    builder2.set(key4, new Range(30, 60));
                    CaptureRequest.Builder builder3 = this.captureRequestBuilder;
                    key5 = CaptureRequest.CONTROL_CAPTURE_INTENT;
                    builder3.set(key5, 3);
                }
                if (this.sensorSize != null && Math.abs(this.currentZoom - 1.0f) >= 0.01f) {
                    int width = this.sensorSize.width() / 2;
                    int height = this.sensorSize.height() / 2;
                    int width2 = (int) ((this.sensorSize.width() * 0.5f) / this.currentZoom);
                    int height2 = (int) ((this.sensorSize.height() * 0.5f) / this.currentZoom);
                    this.cropRegion.set(width - width2, height - height2, width + width2, height + height2);
                    CaptureRequest.Builder builder4 = this.captureRequestBuilder;
                    key3 = CaptureRequest.SCALER_CROP_REGION;
                    builder4.set(key3, this.cropRegion);
                }
                this.captureRequestBuilder.addTarget(this.surface);
                CameraCaptureSession cameraCaptureSession = this.captureSession;
                build = this.captureRequestBuilder.build();
                cameraCaptureSession.setRepeatingRequest(build, null, this.handler);
            }
            key = CaptureRequest.CONTROL_SCENE_MODE;
            valueOf = 16;
            createCaptureRequest.set(key, valueOf);
            CaptureRequest.Builder builder5 = this.captureRequestBuilder;
            key2 = CaptureRequest.FLASH_MODE;
            if (this.flashing) {
            }
            builder5.set(key2, Integer.valueOf(i));
            if (this.recordingVideo) {
            }
            if (this.sensorSize != null) {
                int width3 = this.sensorSize.width() / 2;
                int height3 = this.sensorSize.height() / 2;
                int width22 = (int) ((this.sensorSize.width() * 0.5f) / this.currentZoom);
                int height22 = (int) ((this.sensorSize.height() * 0.5f) / this.currentZoom);
                this.cropRegion.set(width3 - width22, height3 - height22, width3 + width22, height3 + height22);
                CaptureRequest.Builder builder42 = this.captureRequestBuilder;
                key3 = CaptureRequest.SCALER_CROP_REGION;
                builder42.set(key3, this.cropRegion);
            }
            this.captureRequestBuilder.addTarget(this.surface);
            CameraCaptureSession cameraCaptureSession2 = this.captureSession;
            build = this.captureRequestBuilder.build();
            cameraCaptureSession2.setRepeatingRequest(build, null, this.handler);
        } catch (Exception e) {
            FileLog.e("Camera2Sessions setRepeatingRequest error in updateCaptureRequest", e);
        }
    }

    public void destroy(boolean z) {
        destroy(z, null);
    }

    public void destroy(boolean z, final Runnable runnable) {
        this.isClosed = true;
        if (z) {
            this.handler.post(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    Camera2Session.this.lambda$destroy$4(runnable);
                }
            });
            return;
        }
        CameraCaptureSession cameraCaptureSession = this.captureSession;
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            this.captureSession = null;
        }
        CameraDevice cameraDevice = this.cameraDevice;
        if (cameraDevice != null) {
            cameraDevice.close();
            this.cameraDevice = null;
        }
        ImageReader imageReader = this.imageReader;
        if (imageReader != null) {
            imageReader.close();
            this.imageReader = null;
        }
        this.thread.quitSafely();
        try {
            this.thread.join();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (runnable != null) {
            AndroidUtilities.runOnUIThread(runnable);
        }
    }

    public int getCurrentOrientation() {
        return getJpegOrientation();
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x003c A[Catch: Exception -> 0x0046, TryCatch #0 {Exception -> 0x0046, blocks: (B:3:0x0001, B:7:0x0006, B:16:0x002c, B:18:0x003c, B:19:0x004f, B:22:0x0048), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0048 A[Catch: Exception -> 0x0046, TryCatch #0 {Exception -> 0x0046, blocks: (B:3:0x0001, B:7:0x0006, B:16:0x002c, B:18:0x003c, B:19:0x004f, B:22:0x0048), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getDisplayOrientation() {
        int i;
        CameraCharacteristics.Key key;
        Object obj;
        try {
            Context context = ApplicationLoader.applicationContext;
            if (context == null) {
                return 0;
            }
            int rotation = ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation();
            if (rotation != 0) {
                if (rotation == 1) {
                    i = 90;
                } else if (rotation == 2) {
                    i = NotificationCenter.updateBotMenuButton;
                } else if (rotation == 3) {
                    i = NotificationCenter.attachMenuBotsDidLoad;
                }
                CameraCharacteristics cameraCharacteristics = this.cameraCharacteristics;
                key = CameraCharacteristics.SENSOR_ORIENTATION;
                obj = cameraCharacteristics.get(key);
                Integer num = (Integer) obj;
                return (!this.isFront ? 360 - ((num.intValue() + i) % 360) : (num.intValue() - i) + 360) % 360;
            }
            i = 0;
            CameraCharacteristics cameraCharacteristics2 = this.cameraCharacteristics;
            key = CameraCharacteristics.SENSOR_ORIENTATION;
            obj = cameraCharacteristics2.get(key);
            Integer num2 = (Integer) obj;
            return (!this.isFront ? 360 - ((num2.intValue() + i) % 360) : (num2.intValue() - i) + 360) % 360;
        } catch (Exception e) {
            FileLog.e(e);
            return 0;
        }
    }

    public boolean getFlash() {
        return this.flashing;
    }

    public float getMaxZoom() {
        return this.maxZoom;
    }

    public float getMinZoom() {
        return 1.0f;
    }

    public int getPreviewHeight() {
        int height;
        height = this.previewSize.getHeight();
        return height;
    }

    public int getPreviewWidth() {
        int width;
        width = this.previewSize.getWidth();
        return width;
    }

    public int getWorldAngle() {
        int jpegOrientation = getJpegOrientation() - getDisplayOrientation();
        return jpegOrientation < 0 ? jpegOrientation + 360 : jpegOrientation;
    }

    public float getZoom() {
        return this.currentZoom;
    }

    public boolean isInitiated() {
        return (this.isError || !this.isSuccess || this.isClosed) ? false : true;
    }

    public void open(final SurfaceTexture surfaceTexture) {
        this.handler.post(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                Camera2Session.this.lambda$open$1(surfaceTexture);
            }
        });
    }

    public void setFlash(boolean z) {
        if (this.flashing != z) {
            this.flashing = z;
            updateCaptureRequest();
        }
    }

    public void setNightMode(boolean z) {
        if (this.nightMode != z) {
            this.nightMode = z;
            updateCaptureRequest();
        }
    }

    public void setRecordingVideo(boolean z) {
        if (this.recordingVideo != z) {
            this.recordingVideo = z;
            updateCaptureRequest();
        }
    }

    public void setScanningBarcode(boolean z) {
        if (this.scanningBarcode != z) {
            this.scanningBarcode = z;
            updateCaptureRequest();
        }
    }

    public void setZoom(float f) {
        CaptureRequest build;
        if (!isInitiated() || this.captureRequestBuilder == null || this.cameraDevice == null || this.sensorSize == null) {
            return;
        }
        this.currentZoom = Utilities.clamp(f, this.maxZoom, 1.0f);
        updateCaptureRequest();
        try {
            CameraCaptureSession cameraCaptureSession = this.captureSession;
            build = this.captureRequestBuilder.build();
            cameraCaptureSession.setRepeatingRequest(build, null, this.handler);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public boolean takePicture(File file, Utilities.Callback<Integer> callback) {
        CaptureRequest.Builder createCaptureRequest;
        CaptureRequest.Key key;
        CaptureRequest build;
        CaptureRequest.Key key2;
        CameraDevice cameraDevice = this.cameraDevice;
        if (cameraDevice != null && this.captureSession != null) {
            try {
                createCaptureRequest = cameraDevice.createCaptureRequest(2);
                int jpegOrientation = getJpegOrientation();
                key = CaptureRequest.JPEG_ORIENTATION;
                createCaptureRequest.set(key, Integer.valueOf(jpegOrientation));
                this.imageReader.setOnImageAvailableListener(new 3(file, callback, jpegOrientation), null);
                if (this.scanningBarcode) {
                    key2 = CaptureRequest.CONTROL_SCENE_MODE;
                    createCaptureRequest.set(key2, 16);
                }
                createCaptureRequest.addTarget(this.imageReader.getSurface());
                CameraCaptureSession cameraCaptureSession = this.captureSession;
                build = createCaptureRequest.build();
                cameraCaptureSession.capture(build, new CameraCaptureSession.CaptureCallback() { // from class: org.telegram.messenger.camera.Camera2Session.4
                }, null);
                return true;
            } catch (Exception e) {
                FileLog.e("Camera2Sessions takePicture error", e);
            }
        }
        return false;
    }

    public void whenDone(Runnable runnable) {
        if (isInitiated()) {
            runnable.run();
            runnable = null;
        }
        this.doneCallback = runnable;
    }
}
