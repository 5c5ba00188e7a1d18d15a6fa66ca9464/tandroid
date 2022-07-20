package org.telegram.messenger.voip;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Point;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Display;
import android.view.WindowManager;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.CapturerObserver;
import org.webrtc.EglBase;
import org.webrtc.Logging;
import org.webrtc.ScreenCapturerAndroid;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.voiceengine.WebRtcAudioRecord;
@TargetApi(18)
/* loaded from: classes.dex */
public class VideoCapturerDevice {
    private static final int CAPTURE_FPS = 30;
    private static final int CAPTURE_HEIGHT;
    private static final int CAPTURE_WIDTH;
    public static EglBase eglBase;
    private static VideoCapturerDevice[] instance;
    public static Intent mediaProjectionPermissionResultData;
    private int currentHeight;
    private int currentWidth;
    private Handler handler;
    private CapturerObserver nativeCapturerObserver;
    private long nativePtr;
    private HandlerThread thread;
    private VideoCapturer videoCapturer;
    private SurfaceTextureHelper videoCapturerSurfaceTextureHelper;

    private static native CapturerObserver nativeGetJavaVideoCapturerObserver(long j);

    private void onAspectRatioRequested(float f) {
    }

    static {
        int i = Build.VERSION.SDK_INT;
        CAPTURE_WIDTH = i <= 19 ? 480 : 1280;
        CAPTURE_HEIGHT = i <= 19 ? 320 : 720;
        instance = new VideoCapturerDevice[2];
    }

    public VideoCapturerDevice(boolean z) {
        if (Build.VERSION.SDK_INT < 18) {
            return;
        }
        Logging.enableLogToDebugOutput(Logging.Severity.LS_INFO);
        Logging.d("VideoCapturerDevice", "device model = " + Build.MANUFACTURER + Build.MODEL);
        AndroidUtilities.runOnUIThread(new VideoCapturerDevice$$ExternalSyntheticLambda9(this, z));
    }

    public /* synthetic */ void lambda$new$0(boolean z) {
        if (eglBase == null) {
            eglBase = EglBase.CC.create(null, EglBase.CONFIG_PLAIN);
        }
        instance[z ? 1 : 0] = this;
        HandlerThread handlerThread = new HandlerThread("CallThread");
        this.thread = handlerThread;
        handlerThread.start();
        this.handler = new Handler(this.thread.getLooper());
    }

    public static void checkScreenCapturerSize() {
        if (instance[1] == null) {
            return;
        }
        Point screenCaptureSize = getScreenCaptureSize();
        VideoCapturerDevice[] videoCapturerDeviceArr = instance;
        int i = videoCapturerDeviceArr[1].currentWidth;
        int i2 = screenCaptureSize.x;
        if (i == i2 && videoCapturerDeviceArr[1].currentHeight == screenCaptureSize.y) {
            return;
        }
        videoCapturerDeviceArr[1].currentWidth = i2;
        videoCapturerDeviceArr[1].currentHeight = screenCaptureSize.y;
        videoCapturerDeviceArr[1].handler.post(new VideoCapturerDevice$$ExternalSyntheticLambda7(videoCapturerDeviceArr[1], screenCaptureSize));
    }

    public static /* synthetic */ void lambda$checkScreenCapturerSize$1(VideoCapturerDevice videoCapturerDevice, Point point) {
        VideoCapturer videoCapturer = videoCapturerDevice.videoCapturer;
        if (videoCapturer != null) {
            videoCapturer.changeCaptureFormat(point.x, point.y, 30);
        }
    }

    private static Point getScreenCaptureSize() {
        int i;
        int i2;
        Display defaultDisplay = ((WindowManager) ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getRealSize(point);
        int i3 = point.x;
        int i4 = point.y;
        float f = i3 > i4 ? i4 / i3 : i3 / i4;
        int i5 = 1;
        while (true) {
            if (i5 > 100) {
                i5 = -1;
                i = -1;
                break;
            }
            float f2 = i5 * f;
            i = (int) f2;
            if (f2 != i) {
                i5++;
            } else if (point.x <= point.y) {
                i = i5;
                i5 = i;
            }
        }
        if (i5 != -1 && f != 1.0f) {
            while (true) {
                int i6 = point.x;
                if (i6 <= 1000 && (i2 = point.y) <= 1000 && i6 % 4 == 0 && i2 % 4 == 0) {
                    break;
                }
                int i7 = i6 - i5;
                point.x = i7;
                int i8 = point.y - i;
                point.y = i8;
                if (i7 < 800 && i8 < 800) {
                    i5 = -1;
                    break;
                }
            }
        }
        if (i5 == -1 || f == 1.0f) {
            float max = Math.max(point.x / 970.0f, point.y / 970.0f);
            point.x = ((int) Math.ceil((point.x / max) / 4.0f)) * 4;
            point.y = ((int) Math.ceil((point.y / max) / 4.0f)) * 4;
        }
        return point;
    }

    private void init(long j, String str) {
        if (Build.VERSION.SDK_INT < 18) {
            return;
        }
        AndroidUtilities.runOnUIThread(new VideoCapturerDevice$$ExternalSyntheticLambda5(this, j, str));
    }

    public /* synthetic */ void lambda$init$5(long j, String str) {
        if (eglBase == null) {
            return;
        }
        this.nativePtr = j;
        if ("screen".equals(str)) {
            if (Build.VERSION.SDK_INT < 21 || this.videoCapturer != null) {
                return;
            }
            this.videoCapturer = new ScreenCapturerAndroid(mediaProjectionPermissionResultData, new AnonymousClass1());
            Point screenCaptureSize = getScreenCaptureSize();
            this.currentWidth = screenCaptureSize.x;
            this.currentHeight = screenCaptureSize.y;
            this.videoCapturerSurfaceTextureHelper = SurfaceTextureHelper.create("ScreenCapturerThread", eglBase.getEglBaseContext());
            this.handler.post(new VideoCapturerDevice$$ExternalSyntheticLambda6(this, screenCaptureSize));
            return;
        }
        CameraEnumerator camera2Enumerator = Camera2Enumerator.isSupported(ApplicationLoader.applicationContext) ? new Camera2Enumerator(ApplicationLoader.applicationContext) : new Camera1Enumerator();
        String[] deviceNames = camera2Enumerator.getDeviceNames();
        int i = 0;
        while (true) {
            if (i >= deviceNames.length) {
                i = -1;
                break;
            } else if (camera2Enumerator.isFrontFacing(deviceNames[i]) == "front".equals(str)) {
                break;
            } else {
                i++;
            }
        }
        if (i == -1) {
            return;
        }
        String str2 = deviceNames[i];
        if (this.videoCapturer == null) {
            this.videoCapturer = camera2Enumerator.createCapturer(str2, new AnonymousClass2());
            this.videoCapturerSurfaceTextureHelper = SurfaceTextureHelper.create("VideoCapturerThread", eglBase.getEglBaseContext());
            this.handler.post(new VideoCapturerDevice$$ExternalSyntheticLambda0(this));
            return;
        }
        this.handler.post(new VideoCapturerDevice$$ExternalSyntheticLambda8(this, str2));
    }

    /* renamed from: org.telegram.messenger.voip.VideoCapturerDevice$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends MediaProjection.Callback {
        AnonymousClass1() {
            VideoCapturerDevice.this = r1;
        }

        @Override // android.media.projection.MediaProjection.Callback
        public void onStop() {
            AndroidUtilities.runOnUIThread(VideoCapturerDevice$1$$ExternalSyntheticLambda0.INSTANCE);
        }

        public static /* synthetic */ void lambda$onStop$0() {
            if (VoIPService.getSharedInstance() != null) {
                VoIPService.getSharedInstance().stopScreenCapture();
            }
        }
    }

    public /* synthetic */ void lambda$init$2(Point point) {
        if (this.videoCapturerSurfaceTextureHelper != null) {
            long j = this.nativePtr;
            if (j == 0) {
                return;
            }
            this.nativeCapturerObserver = nativeGetJavaVideoCapturerObserver(j);
            this.videoCapturer.initialize(this.videoCapturerSurfaceTextureHelper, ApplicationLoader.applicationContext, this.nativeCapturerObserver);
            this.videoCapturer.startCapture(point.x, point.y, 30);
            WebRtcAudioRecord webRtcAudioRecord = WebRtcAudioRecord.Instance;
            if (webRtcAudioRecord == null) {
                return;
            }
            webRtcAudioRecord.initDeviceAudioRecord(((ScreenCapturerAndroid) this.videoCapturer).getMediaProjection());
        }
    }

    /* renamed from: org.telegram.messenger.voip.VideoCapturerDevice$2 */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements CameraVideoCapturer.CameraEventsHandler {
        @Override // org.webrtc.CameraVideoCapturer.CameraEventsHandler
        public void onCameraClosed() {
        }

        @Override // org.webrtc.CameraVideoCapturer.CameraEventsHandler
        public void onCameraDisconnected() {
        }

        @Override // org.webrtc.CameraVideoCapturer.CameraEventsHandler
        public void onCameraError(String str) {
        }

        @Override // org.webrtc.CameraVideoCapturer.CameraEventsHandler
        public void onCameraFreezed(String str) {
        }

        @Override // org.webrtc.CameraVideoCapturer.CameraEventsHandler
        public void onCameraOpening(String str) {
        }

        AnonymousClass2() {
            VideoCapturerDevice.this = r1;
        }

        @Override // org.webrtc.CameraVideoCapturer.CameraEventsHandler
        public void onFirstFrameAvailable() {
            AndroidUtilities.runOnUIThread(VideoCapturerDevice$2$$ExternalSyntheticLambda0.INSTANCE);
        }

        public static /* synthetic */ void lambda$onFirstFrameAvailable$0() {
            if (VoIPService.getSharedInstance() != null) {
                VoIPService.getSharedInstance().onCameraFirstFrameAvailable();
            }
        }
    }

    public /* synthetic */ void lambda$init$3() {
        if (this.videoCapturerSurfaceTextureHelper == null) {
            return;
        }
        this.nativeCapturerObserver = nativeGetJavaVideoCapturerObserver(this.nativePtr);
        this.videoCapturer.initialize(this.videoCapturerSurfaceTextureHelper, ApplicationLoader.applicationContext, this.nativeCapturerObserver);
        this.videoCapturer.startCapture(CAPTURE_WIDTH, CAPTURE_HEIGHT, 30);
    }

    /* renamed from: org.telegram.messenger.voip.VideoCapturerDevice$3 */
    /* loaded from: classes.dex */
    public class AnonymousClass3 implements CameraVideoCapturer.CameraSwitchHandler {
        @Override // org.webrtc.CameraVideoCapturer.CameraSwitchHandler
        public void onCameraSwitchError(String str) {
        }

        AnonymousClass3() {
            VideoCapturerDevice.this = r1;
        }

        @Override // org.webrtc.CameraVideoCapturer.CameraSwitchHandler
        public void onCameraSwitchDone(boolean z) {
            AndroidUtilities.runOnUIThread(new VideoCapturerDevice$3$$ExternalSyntheticLambda0(z));
        }

        public static /* synthetic */ void lambda$onCameraSwitchDone$0(boolean z) {
            if (VoIPService.getSharedInstance() != null) {
                VoIPService.getSharedInstance().setSwitchingCamera(false, z);
            }
        }
    }

    public /* synthetic */ void lambda$init$4(String str) {
        ((CameraVideoCapturer) this.videoCapturer).switchCamera(new AnonymousClass3(), str);
    }

    public static MediaProjection getMediaProjection() {
        VideoCapturerDevice[] videoCapturerDeviceArr = instance;
        if (videoCapturerDeviceArr[1] == null) {
            return null;
        }
        return ((ScreenCapturerAndroid) videoCapturerDeviceArr[1].videoCapturer).getMediaProjection();
    }

    private void onStateChanged(long j, int i) {
        if (Build.VERSION.SDK_INT < 18) {
            return;
        }
        AndroidUtilities.runOnUIThread(new VideoCapturerDevice$$ExternalSyntheticLambda4(this, j, i));
    }

    public /* synthetic */ void lambda$onStateChanged$7(long j, int i) {
        if (this.nativePtr != j) {
            return;
        }
        this.handler.post(new VideoCapturerDevice$$ExternalSyntheticLambda3(this, i));
    }

    public /* synthetic */ void lambda$onStateChanged$6(int i) {
        VideoCapturer videoCapturer = this.videoCapturer;
        if (videoCapturer == null) {
            return;
        }
        if (i == 2) {
            videoCapturer.startCapture(CAPTURE_WIDTH, CAPTURE_HEIGHT, 30);
            return;
        }
        try {
            videoCapturer.stopCapture();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void onDestroy() {
        if (Build.VERSION.SDK_INT < 18) {
            return;
        }
        this.nativePtr = 0L;
        AndroidUtilities.runOnUIThread(new VideoCapturerDevice$$ExternalSyntheticLambda1(this));
    }

    public /* synthetic */ void lambda$onDestroy$9() {
        int i = 0;
        while (true) {
            VideoCapturerDevice[] videoCapturerDeviceArr = instance;
            if (i >= videoCapturerDeviceArr.length) {
                break;
            } else if (videoCapturerDeviceArr[i] == this) {
                videoCapturerDeviceArr[i] = null;
                break;
            } else {
                i++;
            }
        }
        this.handler.post(new VideoCapturerDevice$$ExternalSyntheticLambda2(this));
        try {
            this.thread.quitSafely();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$onDestroy$8() {
        WebRtcAudioRecord webRtcAudioRecord;
        if ((this.videoCapturer instanceof ScreenCapturerAndroid) && (webRtcAudioRecord = WebRtcAudioRecord.Instance) != null) {
            webRtcAudioRecord.stopDeviceAudioRecord();
        }
        VideoCapturer videoCapturer = this.videoCapturer;
        if (videoCapturer != null) {
            try {
                videoCapturer.stopCapture();
                this.videoCapturer.dispose();
                this.videoCapturer = null;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        SurfaceTextureHelper surfaceTextureHelper = this.videoCapturerSurfaceTextureHelper;
        if (surfaceTextureHelper != null) {
            surfaceTextureHelper.dispose();
            this.videoCapturerSurfaceTextureHelper = null;
        }
    }

    private EglBase.Context getSharedEGLContext() {
        if (eglBase == null) {
            eglBase = EglBase.CC.create(null, EglBase.CONFIG_PLAIN);
        }
        EglBase eglBase2 = eglBase;
        if (eglBase2 != null) {
            return eglBase2.getEglBaseContext();
        }
        return null;
    }

    public static EglBase getEglBase() {
        if (eglBase == null) {
            eglBase = EglBase.CC.create(null, EglBase.CONFIG_PLAIN);
        }
        return eglBase;
    }
}
