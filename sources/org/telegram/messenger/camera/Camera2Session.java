package org.telegram.messenger.camera;

import android.annotation.TargetApi;
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
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.camera.Camera2Session;
@TargetApi(21)
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

    public float getMinZoom() {
        return 1.0f;
    }

    public static Camera2Session create(boolean z, int i, int i2) {
        android.util.Size size;
        String str;
        CameraManager cameraManager;
        Context context = ApplicationLoader.applicationContext;
        CameraManager cameraManager2 = (CameraManager) context.getSystemService("camera");
        try {
            String[] cameraIdList = cameraManager2.getCameraIdList();
            int i3 = 0;
            size = null;
            str = null;
            float f = 0.0f;
            while (i3 < cameraIdList.length) {
                try {
                    String str2 = cameraIdList[i3];
                    CameraCharacteristics cameraCharacteristics = cameraManager2.getCameraCharacteristics(str2);
                    if (cameraCharacteristics != null && ((Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue() == (!z)) {
                        StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                        android.util.Size size2 = (android.util.Size) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE);
                        float width = size2 == null ? 0.0f : size2.getWidth() / size2.getHeight();
                        float f2 = i / i2;
                        cameraManager = cameraManager2;
                        if ((f2 >= 1.0f) != (width >= 1.0f)) {
                            width = 1.0f / width;
                        }
                        if ((f <= 0.0f || Math.abs(f2 - f) > Math.abs(f2 - width)) && streamConfigurationMap != null && Build.VERSION.SDK_INT >= 23) {
                            android.util.Size chooseOptimalSize = chooseOptimalSize(streamConfigurationMap.getOutputSizes(SurfaceTexture.class), i, i2, false);
                            if (chooseOptimalSize != null) {
                                size = chooseOptimalSize;
                                str = str2;
                                f = width;
                            }
                            i3++;
                            cameraManager2 = cameraManager;
                        }
                        i3++;
                        cameraManager2 = cameraManager;
                    }
                    cameraManager = cameraManager2;
                    i3++;
                    cameraManager2 = cameraManager;
                } catch (Exception e) {
                    e = e;
                    FileLog.e(e);
                    return str == null ? null : null;
                }
            }
        } catch (Exception e2) {
            e = e2;
            size = null;
            str = null;
        }
        if (str == null && size != null) {
            return new Camera2Session(context, z, str, size);
        }
    }

    private Camera2Session(Context context, boolean z, String str, android.util.Size size) {
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
        this.imageReader = ImageReader.newInstance(size.getWidth(), size.getHeight(), LiteMode.FLAG_CHAT_BLUR, 1);
        CameraManager cameraManager = (CameraManager) context.getSystemService("camera");
        this.cameraManager = cameraManager;
        try {
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(str);
            this.cameraCharacteristics = cameraCharacteristics;
            this.sensorSize = (Rect) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            Float f2 = (Float) this.cameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
            if (f2 != null && f2.floatValue() >= 1.0f) {
                f = f2.floatValue();
            }
            this.maxZoom = f;
            cameraManager.openCamera(str, r1, this.handler);
        } catch (Exception e) {
            FileLog.e(e);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Camera2Session.this.lambda$new$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 1 extends CameraDevice.StateCallback {
        final /* synthetic */ String val$cameraId;

        1(String str) {
            this.val$cameraId = str;
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice cameraDevice) {
            Camera2Session.this.cameraDevice = cameraDevice;
            Camera2Session.this.lastTime = System.currentTimeMillis();
            FileLog.d("Camera2Session camera #" + this.val$cameraId + " opened");
            Camera2Session.this.checkOpen();
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

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onError$0() {
            Camera2Session.this.isError = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 2 extends CameraCaptureSession.StateCallback {
        final /* synthetic */ String val$cameraId;

        2(String str) {
            this.val$cameraId = str;
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

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigureFailed$1() {
            Camera2Session.this.isError = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.isError = true;
    }

    public void whenDone(Runnable runnable) {
        if (isInitiated()) {
            runnable.run();
            this.doneCallback = null;
            return;
        }
        this.doneCallback = runnable;
    }

    public void open(final SurfaceTexture surfaceTexture) {
        this.handler.post(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                Camera2Session.this.lambda$open$1(surfaceTexture);
            }
        });
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
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    Camera2Session.this.lambda$checkOpen$2();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkOpen$2() {
        this.isError = true;
    }

    public boolean isInitiated() {
        return (this.isError || !this.isSuccess || this.isClosed) ? false : true;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x003a A[Catch: Exception -> 0x0050, TryCatch #0 {Exception -> 0x0050, blocks: (B:3:0x0001, B:6:0x0006, B:18:0x002c, B:20:0x003a, B:21:0x0046), top: B:26:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0046 A[Catch: Exception -> 0x0050, TRY_LEAVE, TryCatch #0 {Exception -> 0x0050, blocks: (B:3:0x0001, B:6:0x0006, B:18:0x002c, B:20:0x003a, B:21:0x0046), top: B:26:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getDisplayOrientation() {
        int i;
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
                    i = 180;
                } else if (rotation == 3) {
                    i = 270;
                }
                Integer num = (Integer) this.cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                if (!this.isFront) {
                    return (360 - ((num.intValue() + i) % 360)) % 360;
                }
                return ((num.intValue() - i) + 360) % 360;
            }
            i = 0;
            Integer num2 = (Integer) this.cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            if (!this.isFront) {
            }
        } catch (Exception e) {
            FileLog.e(e);
            return 0;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x003a A[Catch: Exception -> 0x0050, TryCatch #0 {Exception -> 0x0050, blocks: (B:3:0x0001, B:6:0x0006, B:18:0x002c, B:20:0x003a, B:21:0x0046), top: B:26:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0046 A[Catch: Exception -> 0x0050, TRY_LEAVE, TryCatch #0 {Exception -> 0x0050, blocks: (B:3:0x0001, B:6:0x0006, B:18:0x002c, B:20:0x003a, B:21:0x0046), top: B:26:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int getJpegOrientation() {
        int i;
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
                    i = 180;
                } else if (rotation == 3) {
                    i = 270;
                }
                Integer num = (Integer) this.cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                if (!this.isFront) {
                    return (360 - ((num.intValue() + i) % 360)) % 360;
                }
                return ((num.intValue() - i) + 360) % 360;
            }
            i = 0;
            Integer num2 = (Integer) this.cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            if (!this.isFront) {
            }
        } catch (Exception e) {
            FileLog.e(e);
            return 0;
        }
    }

    public int getWorldAngle() {
        int jpegOrientation = getJpegOrientation() - getDisplayOrientation();
        return jpegOrientation < 0 ? jpegOrientation + 360 : jpegOrientation;
    }

    public int getCurrentOrientation() {
        return getJpegOrientation();
    }

    public void setZoom(float f) {
        if (!isInitiated() || this.captureRequestBuilder == null || this.cameraDevice == null || this.sensorSize == null) {
            return;
        }
        this.currentZoom = Utilities.clamp(f, this.maxZoom, 1.0f);
        updateCaptureRequest();
        try {
            this.captureSession.setRepeatingRequest(this.captureRequestBuilder.build(), null, this.handler);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public float getZoom() {
        return this.currentZoom;
    }

    public float getMaxZoom() {
        return this.maxZoom;
    }

    public int getPreviewWidth() {
        return this.previewSize.getWidth();
    }

    public int getPreviewHeight() {
        return this.previewSize.getHeight();
    }

    public void destroy(boolean z) {
        destroy(z, null);
    }

    public void destroy(boolean z, final Runnable runnable) {
        this.isClosed = true;
        if (z) {
            this.handler.post(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$$ExternalSyntheticLambda3
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                Camera2Session.this.lambda$destroy$3(runnable);
            }
        });
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

    public void setNightMode(boolean z) {
        if (this.nightMode != z) {
            this.nightMode = z;
            updateCaptureRequest();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCaptureRequest() {
        int i;
        CameraDevice cameraDevice = this.cameraDevice;
        if (cameraDevice == null || this.surface == null || this.captureSession == null) {
            return;
        }
        try {
            if (this.recordingVideo) {
                i = 3;
            } else {
                i = this.scanningBarcode ? 2 : 1;
            }
            CaptureRequest.Builder createCaptureRequest = cameraDevice.createCaptureRequest(i);
            this.captureRequestBuilder = createCaptureRequest;
            if (this.scanningBarcode) {
                createCaptureRequest.set(CaptureRequest.CONTROL_SCENE_MODE, 16);
            } else if (this.nightMode) {
                createCaptureRequest.set(CaptureRequest.CONTROL_SCENE_MODE, Integer.valueOf(this.isFront ? 6 : 5));
            }
            if (this.recordingVideo) {
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, new Range(30, 60));
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_CAPTURE_INTENT, 3);
            }
            if (this.sensorSize != null && Math.abs(this.currentZoom - 1.0f) >= 0.01f) {
                int width = this.sensorSize.width() / 2;
                int height = this.sensorSize.height() / 2;
                int width2 = (int) ((this.sensorSize.width() * 0.5f) / this.currentZoom);
                int height2 = (int) ((this.sensorSize.height() * 0.5f) / this.currentZoom);
                this.cropRegion.set(width - width2, height - height2, width + width2, height + height2);
                this.captureRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, this.cropRegion);
            }
            this.captureRequestBuilder.addTarget(this.surface);
            this.captureSession.setRepeatingRequest(this.captureRequestBuilder.build(), null, this.handler);
        } catch (Exception e) {
            FileLog.e("Camera2Sessions setRepeatingRequest error in updateCaptureRequest", e);
        }
    }

    public boolean takePicture(File file, Utilities.Callback<Integer> callback) {
        CameraDevice cameraDevice = this.cameraDevice;
        if (cameraDevice != null && this.captureSession != null) {
            try {
                CaptureRequest.Builder createCaptureRequest = cameraDevice.createCaptureRequest(2);
                int jpegOrientation = getJpegOrientation();
                createCaptureRequest.set(CaptureRequest.JPEG_ORIENTATION, Integer.valueOf(jpegOrientation));
                this.imageReader.setOnImageAvailableListener(new 3(file, callback, jpegOrientation), null);
                if (this.scanningBarcode) {
                    createCaptureRequest.set(CaptureRequest.CONTROL_SCENE_MODE, 16);
                }
                createCaptureRequest.addTarget(this.imageReader.getSurface());
                this.captureSession.capture(createCaptureRequest.build(), new CameraCaptureSession.CaptureCallback() { // from class: org.telegram.messenger.camera.Camera2Session.4
                }, null);
                return true;
            } catch (Exception e) {
                FileLog.e("Camera2Sessions takePicture error", e);
            }
        }
        return false;
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

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:33:0x0056 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r5v1, types: [android.media.Image] */
        /* JADX WARN: Type inference failed for: r5v5 */
        /* JADX WARN: Type inference failed for: r5v6, types: [org.telegram.messenger.Utilities$Callback] */
        @Override // android.media.ImageReader.OnImageAvailableListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onImageAvailable(ImageReader imageReader) {
            FileOutputStream fileOutputStream;
            Throwable th;
            IOException e;
            final Image acquireLatestImage = imageReader.acquireLatestImage();
            ByteBuffer buffer = acquireLatestImage.getPlanes()[0].getBuffer();
            byte[] bArr = new byte[buffer.remaining()];
            buffer.get(bArr);
            try {
                try {
                    try {
                        fileOutputStream = new FileOutputStream(this.val$file);
                        try {
                            fileOutputStream.write(bArr);
                            acquireLatestImage.close();
                            fileOutputStream.close();
                        } catch (IOException e2) {
                            e = e2;
                            e.printStackTrace();
                            acquireLatestImage.close();
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            acquireLatestImage = this.val$whenDone;
                            final int i = this.val$orientation;
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$3$$ExternalSyntheticLambda0
                                @Override // java.lang.Runnable
                                public final void run() {
                                    Camera2Session.3.lambda$onImageAvailable$0(Utilities.Callback.this, i);
                                }
                            });
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
            } catch (IOException e5) {
                e5.printStackTrace();
            }
            acquireLatestImage = this.val$whenDone;
            final int i2 = this.val$orientation;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.camera.Camera2Session$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Camera2Session.3.lambda$onImageAvailable$0(Utilities.Callback.this, i2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onImageAvailable$0(Utilities.Callback callback, int i) {
            if (callback != null) {
                callback.run(Integer.valueOf(i));
            }
        }
    }

    public static android.util.Size chooseOptimalSize(android.util.Size[] sizeArr, int i, int i2, boolean z) {
        ArrayList arrayList = new ArrayList(sizeArr.length);
        ArrayList arrayList2 = new ArrayList(sizeArr.length);
        for (android.util.Size size : sizeArr) {
            if (!z || (size.getHeight() <= i2 && size.getWidth() <= i)) {
                if (size.getHeight() == (size.getWidth() * i2) / i && size.getWidth() >= i && size.getHeight() >= i2) {
                    arrayList.add(size);
                } else if (size.getHeight() * size.getWidth() <= i * i2 * 4 && size.getWidth() >= i && size.getHeight() >= i2) {
                    arrayList2.add(size);
                }
            }
        }
        if (arrayList.size() > 0) {
            return (android.util.Size) Collections.min(arrayList, new CompareSizesByArea());
        }
        if (arrayList2.size() > 0) {
            return (android.util.Size) Collections.min(arrayList2, new CompareSizesByArea());
        }
        return (android.util.Size) Collections.max(Arrays.asList(sizeArr), new CompareSizesByArea());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class CompareSizesByArea implements Comparator<android.util.Size> {
        CompareSizesByArea() {
        }

        @Override // java.util.Comparator
        public int compare(android.util.Size size, android.util.Size size2) {
            return Long.signum((size.getWidth() * size.getHeight()) - (size2.getWidth() * size2.getHeight()));
        }
    }
}
