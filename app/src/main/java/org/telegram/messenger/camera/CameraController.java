package org.telegram.messenger.camera;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Base64;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.SerializedData;
/* loaded from: classes.dex */
public class CameraController implements MediaRecorder.OnInfoListener {
    private static final int CORE_POOL_SIZE = 1;
    private static volatile CameraController Instance = null;
    private static final int KEEP_ALIVE_SECONDS = 60;
    private static final int MAX_POOL_SIZE = 1;
    protected volatile ArrayList<CameraInfo> cameraInfos;
    private boolean cameraInitied;
    private boolean loadingCameras;
    private boolean mirrorRecorderVideo;
    private VideoTakeCallback onVideoTakeCallback;
    private String recordedFile;
    private MediaRecorder recorder;
    CameraView recordingCurrentCameraView;
    protected ArrayList<String> availableFlashModes = new ArrayList<>();
    private ArrayList<Runnable> onFinishCameraInitRunnables = new ArrayList<>();
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());

    /* loaded from: classes.dex */
    public interface VideoTakeCallback {
        void onFinishVideoRecording(String str, long j);
    }

    public static CameraController getInstance() {
        CameraController cameraController = Instance;
        if (cameraController == null) {
            synchronized (CameraController.class) {
                cameraController = Instance;
                if (cameraController == null) {
                    cameraController = new CameraController();
                    Instance = cameraController;
                }
            }
        }
        return cameraController;
    }

    public void cancelOnInitRunnable(Runnable runnable) {
        this.onFinishCameraInitRunnables.remove(runnable);
    }

    public void initCamera(Runnable runnable) {
        initCamera(runnable, false);
    }

    private void initCamera(Runnable runnable, boolean z) {
        if (this.cameraInitied) {
            return;
        }
        if (runnable != null && !this.onFinishCameraInitRunnables.contains(runnable)) {
            this.onFinishCameraInitRunnables.add(runnable);
        }
        if (this.loadingCameras || this.cameraInitied) {
            return;
        }
        this.loadingCameras = true;
        this.threadPool.execute(new CameraController$$ExternalSyntheticLambda13(this, z, runnable));
    }

    public /* synthetic */ void lambda$initCamera$4(boolean z, Runnable runnable) {
        String str;
        Exception e;
        Camera.CameraInfo cameraInfo;
        String str2;
        int i;
        CameraController cameraController = this;
        String str3 = "cameraCache";
        String str4 = "APP_PAUSED";
        try {
            if (cameraController.cameraInfos == null) {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                String string = globalMainSettings.getString(str3, null);
                CameraController$$ExternalSyntheticLambda17 cameraController$$ExternalSyntheticLambda17 = CameraController$$ExternalSyntheticLambda17.INSTANCE;
                ArrayList<CameraInfo> arrayList = new ArrayList<>();
                if (string != null) {
                    SerializedData serializedData = new SerializedData(Base64.decode(string, 0));
                    int readInt32 = serializedData.readInt32(false);
                    for (int i2 = 0; i2 < readInt32; i2++) {
                        CameraInfo cameraInfo2 = new CameraInfo(serializedData.readInt32(false), serializedData.readInt32(false));
                        int readInt322 = serializedData.readInt32(false);
                        for (int i3 = 0; i3 < readInt322; i3++) {
                            cameraInfo2.previewSizes.add(new Size(serializedData.readInt32(false), serializedData.readInt32(false)));
                        }
                        int readInt323 = serializedData.readInt32(false);
                        for (int i4 = 0; i4 < readInt323; i4++) {
                            cameraInfo2.pictureSizes.add(new Size(serializedData.readInt32(false), serializedData.readInt32(false)));
                        }
                        arrayList.add(cameraInfo2);
                        Collections.sort(cameraInfo2.previewSizes, cameraController$$ExternalSyntheticLambda17);
                        Collections.sort(cameraInfo2.pictureSizes, cameraController$$ExternalSyntheticLambda17);
                    }
                    serializedData.cleanup();
                    str = str4;
                } else {
                    int numberOfCameras = Camera.getNumberOfCameras();
                    Camera.CameraInfo cameraInfo3 = new Camera.CameraInfo();
                    int i5 = 4;
                    int i6 = 0;
                    while (i6 < numberOfCameras) {
                        try {
                            Camera.getCameraInfo(i6, cameraInfo3);
                            CameraInfo cameraInfo4 = new CameraInfo(i6, cameraInfo3.facing);
                            if (ApplicationLoader.mainInterfacePaused && ApplicationLoader.externalInterfacePaused) {
                                throw new RuntimeException(str4);
                            }
                            Camera open = Camera.open(cameraInfo4.getCameraId());
                            Camera.Parameters parameters = open.getParameters();
                            List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
                            int i7 = 0;
                            while (true) {
                                cameraInfo = cameraInfo3;
                                str = str4;
                                if (i7 >= supportedPreviewSizes.size()) {
                                    break;
                                }
                                try {
                                    Camera.Size size = supportedPreviewSizes.get(i7);
                                    int i8 = size.width;
                                    List<Camera.Size> list = supportedPreviewSizes;
                                    if ((i8 != 1280 || size.height == 720) && (i = size.height) < 2160 && i8 < 2160) {
                                        str2 = str3;
                                        cameraInfo4.previewSizes.add(new Size(i8, i));
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.d("preview size = " + size.width + " " + size.height);
                                        }
                                        i7++;
                                        cameraInfo3 = cameraInfo;
                                        str4 = str;
                                        supportedPreviewSizes = list;
                                        str3 = str2;
                                    }
                                    str2 = str3;
                                    i7++;
                                    cameraInfo3 = cameraInfo;
                                    str4 = str;
                                    supportedPreviewSizes = list;
                                    str3 = str2;
                                } catch (Exception e2) {
                                    e = e2;
                                    cameraController = this;
                                    FileLog.e(e, !str.equals(e.getMessage()));
                                    AndroidUtilities.runOnUIThread(new CameraController$$ExternalSyntheticLambda12(cameraController, z, e, runnable));
                                    return;
                                }
                            }
                            String str5 = str3;
                            List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
                            for (int i9 = 0; i9 < supportedPictureSizes.size(); i9++) {
                                Camera.Size size2 = supportedPictureSizes.get(i9);
                                if (size2.width == 1280 && size2.height != 720) {
                                }
                                if (!"samsung".equals(Build.MANUFACTURER) || !"jflteuc".equals(Build.PRODUCT) || size2.width < 2048) {
                                    cameraInfo4.pictureSizes.add(new Size(size2.width, size2.height));
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("picture size = " + size2.width + " " + size2.height);
                                    }
                                }
                            }
                            open.release();
                            arrayList.add(cameraInfo4);
                            Collections.sort(cameraInfo4.previewSizes, cameraController$$ExternalSyntheticLambda17);
                            Collections.sort(cameraInfo4.pictureSizes, cameraController$$ExternalSyntheticLambda17);
                            i5 += ((cameraInfo4.previewSizes.size() + cameraInfo4.pictureSizes.size()) * 8) + 8;
                            i6++;
                            cameraInfo3 = cameraInfo;
                            str4 = str;
                            str3 = str5;
                        } catch (Exception e3) {
                            e = e3;
                            str = str4;
                            cameraController = this;
                            FileLog.e(e, !str.equals(e.getMessage()));
                            AndroidUtilities.runOnUIThread(new CameraController$$ExternalSyntheticLambda12(cameraController, z, e, runnable));
                            return;
                        }
                    }
                    String str6 = str3;
                    str = str4;
                    SerializedData serializedData2 = new SerializedData(i5);
                    serializedData2.writeInt32(arrayList.size());
                    for (int i10 = 0; i10 < numberOfCameras; i10++) {
                        CameraInfo cameraInfo5 = arrayList.get(i10);
                        serializedData2.writeInt32(cameraInfo5.cameraId);
                        serializedData2.writeInt32(cameraInfo5.frontCamera);
                        int size3 = cameraInfo5.previewSizes.size();
                        serializedData2.writeInt32(size3);
                        for (int i11 = 0; i11 < size3; i11++) {
                            Size size4 = cameraInfo5.previewSizes.get(i11);
                            serializedData2.writeInt32(size4.mWidth);
                            serializedData2.writeInt32(size4.mHeight);
                        }
                        int size5 = cameraInfo5.pictureSizes.size();
                        serializedData2.writeInt32(size5);
                        for (int i12 = 0; i12 < size5; i12++) {
                            Size size6 = cameraInfo5.pictureSizes.get(i12);
                            serializedData2.writeInt32(size6.mWidth);
                            serializedData2.writeInt32(size6.mHeight);
                        }
                    }
                    globalMainSettings.edit().putString(str6, Base64.encodeToString(serializedData2.toByteArray(), 0)).commit();
                    serializedData2.cleanup();
                    cameraController = this;
                }
                try {
                    cameraController.cameraInfos = arrayList;
                } catch (Exception e4) {
                    e = e4;
                    FileLog.e(e, !str.equals(e.getMessage()));
                    AndroidUtilities.runOnUIThread(new CameraController$$ExternalSyntheticLambda12(cameraController, z, e, runnable));
                    return;
                }
            } else {
                str = str4;
            }
            AndroidUtilities.runOnUIThread(new CameraController$$ExternalSyntheticLambda4(cameraController));
        } catch (Exception e5) {
            e = e5;
            str = str4;
        }
    }

    public static /* synthetic */ int lambda$initCamera$0(Size size, Size size2) {
        int i = size.mWidth;
        int i2 = size2.mWidth;
        if (i < i2) {
            return 1;
        }
        if (i > i2) {
            return -1;
        }
        int i3 = size.mHeight;
        int i4 = size2.mHeight;
        if (i3 < i4) {
            return 1;
        }
        return i3 > i4 ? -1 : 0;
    }

    public /* synthetic */ void lambda$initCamera$1() {
        this.loadingCameras = false;
        this.cameraInitied = true;
        if (!this.onFinishCameraInitRunnables.isEmpty()) {
            for (int i = 0; i < this.onFinishCameraInitRunnables.size(); i++) {
                this.onFinishCameraInitRunnables.get(i).run();
            }
            this.onFinishCameraInitRunnables.clear();
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.cameraInitied, new Object[0]);
    }

    public /* synthetic */ void lambda$initCamera$3(boolean z, Exception exc, Runnable runnable) {
        this.onFinishCameraInitRunnables.clear();
        this.loadingCameras = false;
        this.cameraInitied = false;
        if (z || !"APP_PAUSED".equals(exc.getMessage()) || runnable == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(new CameraController$$ExternalSyntheticLambda8(this, runnable), 1000L);
    }

    public /* synthetic */ void lambda$initCamera$2(Runnable runnable) {
        initCamera(runnable, true);
    }

    public boolean isCameraInitied() {
        return this.cameraInitied && this.cameraInfos != null && !this.cameraInfos.isEmpty();
    }

    public void close(CameraSession cameraSession, CountDownLatch countDownLatch, Runnable runnable) {
        cameraSession.destroy();
        this.threadPool.execute(new CameraController$$ExternalSyntheticLambda2(runnable, cameraSession, countDownLatch));
        if (countDownLatch != null) {
            try {
                countDownLatch.await();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public static /* synthetic */ void lambda$close$5(Runnable runnable, CameraSession cameraSession, CountDownLatch countDownLatch) {
        if (runnable != null) {
            runnable.run();
        }
        Camera camera = cameraSession.cameraInfo.camera;
        if (camera != null) {
            try {
                camera.stopPreview();
                cameraSession.cameraInfo.camera.setPreviewCallbackWithBuffer(null);
            } catch (Exception e) {
                FileLog.e(e);
            }
            try {
                cameraSession.cameraInfo.camera.release();
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            cameraSession.cameraInfo.camera = null;
        }
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    public ArrayList<CameraInfo> getCameras() {
        return this.cameraInfos;
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x005f, code lost:
        r1 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0060, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0061, code lost:
        if (r3 <= 8) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0063, code lost:
        r2 = pack(r10, r1, 4, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x006a, code lost:
        if (r2 == 1229531648) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x006f, code lost:
        if (r2 == 1296891946) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0071, code lost:
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0072, code lost:
        if (r2 != 1229531648) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0075, code lost:
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0076, code lost:
        r2 = pack(r10, r1 + 4, 4, r5) + 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x007f, code lost:
        if (r2 < 10) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0081, code lost:
        if (r2 <= r3) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0084, code lost:
        r1 = r1 + r2;
        r3 = r3 - r2;
        r2 = pack(r10, r1 - 2, 2, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x008c, code lost:
        r4 = r2 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x008e, code lost:
        if (r2 <= 0) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0092, code lost:
        if (r3 < 12) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x009a, code lost:
        if (pack(r10, r1, 2, r5) != 274) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x009c, code lost:
        r10 = pack(r10, r1 + 8, 2, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00a2, code lost:
        if (r10 == 3) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00a5, code lost:
        if (r10 == 6) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00a7, code lost:
        if (r10 == 8) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00a9, code lost:
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00aa, code lost:
        return 270;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00ad, code lost:
        return 90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00b0, code lost:
        return 180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00b3, code lost:
        r1 = r1 + 12;
        r3 = r3 - 12;
        r2 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00b9, code lost:
        return 0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int getOrientation(byte[] bArr) {
        int i;
        if (bArr == null) {
            return 0;
        }
        int i2 = 0;
        while (true) {
            boolean z = true;
            if (i2 + 3 >= bArr.length) {
                break;
            }
            int i3 = i2 + 1;
            if ((bArr[i2] & 255) != 255) {
                break;
            }
            int i4 = bArr[i3] & 255;
            if (i4 != 255) {
                i3++;
                if (i4 != 216 && i4 != 1) {
                    if (i4 != 217 && i4 != 218) {
                        int pack = pack(bArr, i3, 2, false);
                        if (pack >= 2 && (i = i3 + pack) <= bArr.length) {
                            if (i4 == 225 && pack >= 8 && pack(bArr, i3 + 2, 4, false) == 1165519206 && pack(bArr, i3 + 6, 2, false) == 0) {
                                i2 = i3 + 8;
                                int i5 = pack - 8;
                                break;
                            }
                            i2 = i;
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            i2 = i3;
        }
        return 0;
    }

    private static int pack(byte[] bArr, int i, int i2, boolean z) {
        int i3;
        if (z) {
            i += i2 - 1;
            i3 = -1;
        } else {
            i3 = 1;
        }
        int i4 = 0;
        while (true) {
            int i5 = i2 - 1;
            if (i2 > 0) {
                i4 = (bArr[i] & 255) | (i4 << 8);
                i += i3;
                i2 = i5;
            } else {
                return i4;
            }
        }
    }

    public boolean takePicture(File file, CameraSession cameraSession, Runnable runnable) {
        if (cameraSession == null) {
            return false;
        }
        CameraInfo cameraInfo = cameraSession.cameraInfo;
        boolean isFlipFront = cameraSession.isFlipFront();
        try {
            cameraInfo.camera.takePicture(null, null, new CameraController$$ExternalSyntheticLambda0(file, cameraInfo, isFlipFront, runnable));
            return true;
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
    }

    public static /* synthetic */ void lambda$takePicture$6(File file, CameraInfo cameraInfo, boolean z, Runnable runnable, byte[] bArr, Camera camera) {
        Bitmap bitmap;
        int photoSize = (int) (AndroidUtilities.getPhotoSize() / AndroidUtilities.density);
        String format = String.format(Locale.US, "%s@%d_%d", Utilities.MD5(file.getAbsolutePath()), Integer.valueOf(photoSize), Integer.valueOf(photoSize));
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            options.inJustDecodeBounds = false;
            options.inPurgeable = true;
            bitmap = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        } catch (Throwable th) {
            FileLog.e(th);
            bitmap = null;
        }
        Bitmap bitmap2 = bitmap;
        try {
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (cameraInfo.frontCamera != 0 && z) {
            Matrix matrix = new Matrix();
            matrix.setRotate(getOrientation(bArr));
            matrix.postScale(-1.0f, 1.0f);
            Bitmap createBitmap = Bitmaps.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
            if (createBitmap != bitmap2) {
                bitmap2.recycle();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            createBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.getFD().sync();
            fileOutputStream.close();
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(createBitmap), format, false);
            if (runnable == null) {
                return;
            }
            runnable.run();
            return;
        }
        FileOutputStream fileOutputStream2 = new FileOutputStream(file);
        fileOutputStream2.write(bArr);
        fileOutputStream2.flush();
        fileOutputStream2.getFD().sync();
        fileOutputStream2.close();
        if (bitmap2 != null) {
            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmap2), format, false);
        }
        if (runnable == null) {
            return;
        }
        runnable.run();
    }

    public void startPreview(CameraSession cameraSession) {
        if (cameraSession == null) {
            return;
        }
        this.threadPool.execute(new CameraController$$ExternalSyntheticLambda15(cameraSession));
    }

    public static /* synthetic */ void lambda$startPreview$7(CameraSession cameraSession) {
        CameraInfo cameraInfo = cameraSession.cameraInfo;
        Camera camera = cameraInfo.camera;
        if (camera == null) {
            try {
                Camera open = Camera.open(cameraInfo.cameraId);
                cameraInfo.camera = open;
                camera = open;
            } catch (Exception e) {
                cameraSession.cameraInfo.camera = null;
                if (camera != null) {
                    camera.release();
                }
                FileLog.e(e);
                return;
            }
        }
        camera.startPreview();
    }

    public void stopPreview(CameraSession cameraSession) {
        if (cameraSession == null) {
            return;
        }
        this.threadPool.execute(new CameraController$$ExternalSyntheticLambda14(cameraSession));
    }

    public static /* synthetic */ void lambda$stopPreview$8(CameraSession cameraSession) {
        CameraInfo cameraInfo = cameraSession.cameraInfo;
        Camera camera = cameraInfo.camera;
        if (camera == null) {
            try {
                Camera open = Camera.open(cameraInfo.cameraId);
                cameraInfo.camera = open;
                camera = open;
            } catch (Exception e) {
                cameraSession.cameraInfo.camera = null;
                if (camera != null) {
                    camera.release();
                }
                FileLog.e(e);
                return;
            }
        }
        camera.stopPreview();
    }

    public void openRound(CameraSession cameraSession, SurfaceTexture surfaceTexture, Runnable runnable, Runnable runnable2) {
        if (cameraSession == null || surfaceTexture == null) {
            if (!BuildVars.LOGS_ENABLED) {
                return;
            }
            FileLog.d("failed to open round " + cameraSession + " tex = " + surfaceTexture);
            return;
        }
        this.threadPool.execute(new CameraController$$ExternalSyntheticLambda16(cameraSession, runnable2, surfaceTexture, runnable));
    }

    public static /* synthetic */ void lambda$openRound$9(CameraSession cameraSession, Runnable runnable, SurfaceTexture surfaceTexture, Runnable runnable2) {
        Camera camera = cameraSession.cameraInfo.camera;
        try {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("start creating round camera session");
            }
            if (camera == null) {
                CameraInfo cameraInfo = cameraSession.cameraInfo;
                Camera open = Camera.open(cameraInfo.cameraId);
                cameraInfo.camera = open;
                camera = open;
            }
            camera.getParameters();
            cameraSession.configureRoundCamera(true);
            if (runnable != null) {
                runnable.run();
            }
            camera.setPreviewTexture(surfaceTexture);
            camera.startPreview();
            if (runnable2 != null) {
                AndroidUtilities.runOnUIThread(runnable2);
            }
            if (!BuildVars.LOGS_ENABLED) {
                return;
            }
            FileLog.d("round camera session created");
        } catch (Exception e) {
            cameraSession.cameraInfo.camera = null;
            if (camera != null) {
                camera.release();
            }
            FileLog.e(e);
        }
    }

    public void open(CameraSession cameraSession, SurfaceTexture surfaceTexture, Runnable runnable, Runnable runnable2) {
        if (cameraSession == null || surfaceTexture == null) {
            return;
        }
        this.threadPool.execute(new CameraController$$ExternalSyntheticLambda9(this, cameraSession, runnable2, surfaceTexture, runnable));
    }

    public /* synthetic */ void lambda$open$10(CameraSession cameraSession, Runnable runnable, SurfaceTexture surfaceTexture, Runnable runnable2) {
        CameraInfo cameraInfo = cameraSession.cameraInfo;
        Camera camera = cameraInfo.camera;
        if (camera == null) {
            try {
                Camera open = Camera.open(cameraInfo.cameraId);
                cameraInfo.camera = open;
                camera = open;
            } catch (Exception e) {
                cameraSession.cameraInfo.camera = null;
                if (camera != null) {
                    camera.release();
                }
                FileLog.e(e);
                return;
            }
        }
        List<String> supportedFlashModes = camera.getParameters().getSupportedFlashModes();
        this.availableFlashModes.clear();
        if (supportedFlashModes != null) {
            for (int i = 0; i < supportedFlashModes.size(); i++) {
                String str = supportedFlashModes.get(i);
                if (str.equals("off") || str.equals("on") || str.equals("auto")) {
                    this.availableFlashModes.add(str);
                }
            }
            cameraSession.checkFlashMode(this.availableFlashModes.get(0));
        }
        if (runnable != null) {
            runnable.run();
        }
        cameraSession.configurePhotoCamera();
        camera.setPreviewTexture(surfaceTexture);
        camera.startPreview();
        if (runnable2 != null) {
            AndroidUtilities.runOnUIThread(runnable2);
        }
    }

    public void recordVideo(CameraSession cameraSession, File file, boolean z, VideoTakeCallback videoTakeCallback, Runnable runnable, CameraView cameraView) {
        if (cameraSession == null) {
            return;
        }
        CameraInfo cameraInfo = cameraSession.cameraInfo;
        Camera camera = cameraInfo.camera;
        if (cameraView != null) {
            this.recordingCurrentCameraView = cameraView;
            this.onVideoTakeCallback = videoTakeCallback;
            this.recordedFile = file.getAbsolutePath();
            this.threadPool.execute(new CameraController$$ExternalSyntheticLambda5(this, camera, cameraSession, cameraView, file, runnable));
            return;
        }
        this.threadPool.execute(new CameraController$$ExternalSyntheticLambda6(this, camera, cameraSession, z, file, cameraInfo, videoTakeCallback, runnable));
    }

    public /* synthetic */ void lambda$recordVideo$12(Camera camera, CameraSession cameraSession, CameraView cameraView, File file, Runnable runnable) {
        try {
            if (camera == null) {
                return;
            }
            try {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(cameraSession.getCurrentFlashMode().equals("on") ? "torch" : "off");
                camera.setParameters(parameters);
                cameraSession.onStartRecord();
            } catch (Exception e) {
                FileLog.e(e);
            }
            AndroidUtilities.runOnUIThread(new CameraController$$ExternalSyntheticLambda11(this, cameraView, file, runnable));
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    public /* synthetic */ void lambda$recordVideo$11(CameraView cameraView, File file, Runnable runnable) {
        cameraView.startRecording(file, new CameraController$$ExternalSyntheticLambda3(this));
        if (runnable != null) {
            runnable.run();
        }
    }

    public /* synthetic */ void lambda$recordVideo$13(Camera camera, CameraSession cameraSession, boolean z, File file, CameraInfo cameraInfo, VideoTakeCallback videoTakeCallback, Runnable runnable) {
        if (camera != null) {
            try {
                try {
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(cameraSession.getCurrentFlashMode().equals("on") ? "torch" : "off");
                    camera.setParameters(parameters);
                } catch (Exception e) {
                    FileLog.e(e);
                }
                camera.unlock();
                try {
                    this.mirrorRecorderVideo = z;
                    MediaRecorder mediaRecorder = new MediaRecorder();
                    this.recorder = mediaRecorder;
                    mediaRecorder.setCamera(camera);
                    this.recorder.setVideoSource(1);
                    this.recorder.setAudioSource(5);
                    cameraSession.configureRecorder(1, this.recorder);
                    this.recorder.setOutputFile(file.getAbsolutePath());
                    this.recorder.setMaxFileSize(1073741824L);
                    this.recorder.setVideoFrameRate(30);
                    this.recorder.setMaxDuration(0);
                    Size chooseOptimalSize = chooseOptimalSize(cameraInfo.getPictureSizes(), 720, 480, new Size(16, 9));
                    this.recorder.setVideoEncodingBitRate(Math.min(chooseOptimalSize.mHeight, chooseOptimalSize.mWidth) >= 720 ? 3500000 : 1800000);
                    this.recorder.setVideoSize(chooseOptimalSize.getWidth(), chooseOptimalSize.getHeight());
                    this.recorder.setOnInfoListener(this);
                    this.recorder.prepare();
                    this.recorder.start();
                    this.onVideoTakeCallback = videoTakeCallback;
                    this.recordedFile = file.getAbsolutePath();
                    if (runnable == null) {
                        return;
                    }
                    AndroidUtilities.runOnUIThread(runnable);
                } catch (Exception e2) {
                    this.recorder.release();
                    this.recorder = null;
                    FileLog.e(e2);
                }
            } catch (Exception e3) {
                FileLog.e(e3);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00c4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void finishRecordingVideo() {
        Throwable th;
        Bitmap bitmap;
        Exception e;
        Exception e2;
        MediaMetadataRetriever mediaMetadataRetriever;
        String extractMetadata;
        MediaMetadataRetriever mediaMetadataRetriever2 = null;
        long j = 0;
        try {
            try {
                mediaMetadataRetriever = new MediaMetadataRetriever();
                try {
                    mediaMetadataRetriever.setDataSource(this.recordedFile);
                    if (mediaMetadataRetriever.extractMetadata(9) != null) {
                        j = (int) Math.ceil(((float) Long.parseLong(extractMetadata)) / 1000.0f);
                    }
                } catch (Exception e3) {
                    e2 = e3;
                    FileLog.e(e2);
                    if (mediaMetadataRetriever != null) {
                        try {
                            mediaMetadataRetriever.release();
                        } catch (Exception e4) {
                            e = e4;
                            FileLog.e(e);
                            long j2 = j;
                            Bitmap createVideoThumbnail = SendMessagesHelper.createVideoThumbnail(this.recordedFile, 1);
                            if (!this.mirrorRecorderVideo) {
                            }
                            File file = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 87, new FileOutputStream(file));
                            SharedConfig.saveConfig();
                            AndroidUtilities.runOnUIThread(new CameraController$$ExternalSyntheticLambda7(this, file, bitmap, j2));
                        }
                    }
                    long j22 = j;
                    Bitmap createVideoThumbnail2 = SendMessagesHelper.createVideoThumbnail(this.recordedFile, 1);
                    if (!this.mirrorRecorderVideo) {
                    }
                    File file2 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 87, new FileOutputStream(file2));
                    SharedConfig.saveConfig();
                    AndroidUtilities.runOnUIThread(new CameraController$$ExternalSyntheticLambda7(this, file2, bitmap, j22));
                }
                try {
                    mediaMetadataRetriever.release();
                } catch (Exception e5) {
                    e = e5;
                    FileLog.e(e);
                    long j222 = j;
                    Bitmap createVideoThumbnail22 = SendMessagesHelper.createVideoThumbnail(this.recordedFile, 1);
                    if (!this.mirrorRecorderVideo) {
                    }
                    File file22 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 87, new FileOutputStream(file22));
                    SharedConfig.saveConfig();
                    AndroidUtilities.runOnUIThread(new CameraController$$ExternalSyntheticLambda7(this, file22, bitmap, j222));
                }
            } catch (Throwable th2) {
                th = th2;
                if (0 != 0) {
                    try {
                        mediaMetadataRetriever2.release();
                    } catch (Exception e6) {
                        FileLog.e(e6);
                    }
                }
                throw th;
            }
        } catch (Exception e7) {
            e2 = e7;
            mediaMetadataRetriever = null;
        } catch (Throwable th3) {
            th = th3;
            if (0 != 0) {
            }
            throw th;
        }
        long j2222 = j;
        Bitmap createVideoThumbnail222 = SendMessagesHelper.createVideoThumbnail(this.recordedFile, 1);
        if (!this.mirrorRecorderVideo) {
            Bitmap createBitmap = Bitmap.createBitmap(createVideoThumbnail222.getWidth(), createVideoThumbnail222.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            canvas.scale(-1.0f, 1.0f, createBitmap.getWidth() / 2, createBitmap.getHeight() / 2);
            canvas.drawBitmap(createVideoThumbnail222, 0.0f, 0.0f, (Paint) null);
            createVideoThumbnail222.recycle();
            bitmap = createBitmap;
        } else {
            bitmap = createVideoThumbnail222;
        }
        File file222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 87, new FileOutputStream(file222));
        } catch (Throwable th4) {
            FileLog.e(th4);
        }
        SharedConfig.saveConfig();
        AndroidUtilities.runOnUIThread(new CameraController$$ExternalSyntheticLambda7(this, file222, bitmap, j2222));
    }

    public /* synthetic */ void lambda$finishRecordingVideo$14(File file, Bitmap bitmap, long j) {
        if (this.onVideoTakeCallback != null) {
            String absolutePath = file.getAbsolutePath();
            if (bitmap != null) {
                ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmap), Utilities.MD5(absolutePath), false);
            }
            this.onVideoTakeCallback.onFinishVideoRecording(absolutePath, j);
            this.onVideoTakeCallback = null;
        }
    }

    @Override // android.media.MediaRecorder.OnInfoListener
    public void onInfo(MediaRecorder mediaRecorder, int i, int i2) {
        if (i == 800 || i == 801 || i == 1) {
            MediaRecorder mediaRecorder2 = this.recorder;
            this.recorder = null;
            if (mediaRecorder2 != null) {
                mediaRecorder2.stop();
                mediaRecorder2.release();
            }
            if (this.onVideoTakeCallback == null) {
                return;
            }
            finishRecordingVideo();
        }
    }

    public void stopVideoRecording(CameraSession cameraSession, boolean z) {
        CameraView cameraView = this.recordingCurrentCameraView;
        if (cameraView != null) {
            cameraView.stopRecording();
            this.recordingCurrentCameraView = null;
            return;
        }
        this.threadPool.execute(new CameraController$$ExternalSyntheticLambda10(this, cameraSession, z));
    }

    public /* synthetic */ void lambda$stopVideoRecording$16(CameraSession cameraSession, boolean z) {
        MediaRecorder mediaRecorder;
        try {
            Camera camera = cameraSession.cameraInfo.camera;
            if (camera != null && (mediaRecorder = this.recorder) != null) {
                this.recorder = null;
                try {
                    mediaRecorder.stop();
                } catch (Exception e) {
                    FileLog.e(e);
                }
                try {
                    mediaRecorder.release();
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                try {
                    camera.reconnect();
                    camera.startPreview();
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
                try {
                    cameraSession.stopVideoRecording();
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
            }
            try {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode("off");
                camera.setParameters(parameters);
            } catch (Exception e5) {
                FileLog.e(e5);
            }
            this.threadPool.execute(new CameraController$$ExternalSyntheticLambda1(camera, cameraSession));
            if (!z && this.onVideoTakeCallback != null) {
                finishRecordingVideo();
            } else {
                this.onVideoTakeCallback = null;
            }
        } catch (Exception unused) {
        }
    }

    public static /* synthetic */ void lambda$stopVideoRecording$15(Camera camera, CameraSession cameraSession) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(cameraSession.getCurrentFlashMode());
            camera.setParameters(parameters);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static Size chooseOptimalSize(List<Size> list, int i, int i2, Size size) {
        ArrayList arrayList = new ArrayList(list.size());
        ArrayList arrayList2 = new ArrayList(list.size());
        int width = size.getWidth();
        int height = size.getHeight();
        for (int i3 = 0; i3 < list.size(); i3++) {
            Size size2 = list.get(i3);
            if (size2.getHeight() == (size2.getWidth() * height) / width && size2.getWidth() >= i && size2.getHeight() >= i2) {
                arrayList.add(size2);
            } else if (size2.getHeight() * size2.getWidth() <= i * i2 * 4) {
                arrayList2.add(size2);
            }
        }
        if (arrayList.size() > 0) {
            return (Size) Collections.min(arrayList, new CompareSizesByArea());
        }
        if (arrayList2.size() > 0) {
            return (Size) Collections.min(arrayList2, new CompareSizesByArea());
        }
        return (Size) Collections.max(list, new CompareSizesByArea());
    }

    /* loaded from: classes.dex */
    public static class CompareSizesByArea implements Comparator<Size> {
        CompareSizesByArea() {
        }

        public int compare(Size size, Size size2) {
            return Long.signum((size.getWidth() * size.getHeight()) - (size2.getWidth() * size2.getHeight()));
        }
    }
}
