package org.webrtc;

import android.graphics.ImageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* loaded from: classes3.dex */
public class CameraEnumerationAndroid {
    static final ArrayList<Size> COMMON_RESOLUTIONS = new ArrayList<>(Arrays.asList(new Size(160, 120), new Size(240, 160), new Size(320, 240), new Size(400, 240), new Size(480, 320), new Size(640, 360), new Size(640, 480), new Size(768, 480), new Size(854, 480), new Size(800, 600), new Size(960, 540), new Size(960, 640), new Size(1024, 576), new Size(1024, 600), new Size(1280, 720), new Size(1280, 1024), new Size(1920, 1080), new Size(1920, 1440), new Size(2560, 1440), new Size(3840, 2160)));
    private static final String TAG = "CameraEnumerationAndroid";

    /* loaded from: classes3.dex */
    public static class CaptureFormat {
        public final FramerateRange framerate;
        public final int height;
        public final int imageFormat = 17;
        public final int width;

        /* loaded from: classes3.dex */
        public static class FramerateRange {
            public int max;
            public int min;

            public FramerateRange(int i, int i2) {
                this.min = i;
                this.max = i2;
            }

            public String toString() {
                return "[" + (this.min / 1000.0f) + ":" + (this.max / 1000.0f) + "]";
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof FramerateRange)) {
                    return false;
                }
                FramerateRange framerateRange = (FramerateRange) obj;
                return this.min == framerateRange.min && this.max == framerateRange.max;
            }

            public int hashCode() {
                return (this.min * 65537) + 1 + this.max;
            }
        }

        public CaptureFormat(int i, int i2, int i3, int i4) {
            this.width = i;
            this.height = i2;
            this.framerate = new FramerateRange(i3, i4);
        }

        public CaptureFormat(int i, int i2, FramerateRange framerateRange) {
            this.width = i;
            this.height = i2;
            this.framerate = framerateRange;
        }

        public int frameSize() {
            return frameSize(this.width, this.height, 17);
        }

        public static int frameSize(int i, int i2, int i3) {
            if (i3 != 17) {
                throw new UnsupportedOperationException("Don't know how to calculate the frame size of non-NV21 image formats.");
            }
            return ((i * i2) * ImageFormat.getBitsPerPixel(i3)) / 8;
        }

        public String toString() {
            return this.width + "x" + this.height + "@" + this.framerate;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof CaptureFormat)) {
                return false;
            }
            CaptureFormat captureFormat = (CaptureFormat) obj;
            return this.width == captureFormat.width && this.height == captureFormat.height && this.framerate.equals(captureFormat.framerate);
        }

        public int hashCode() {
            return (((this.width * 65497) + this.height) * 251) + 1 + this.framerate.hashCode();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static abstract class ClosestComparator<T> implements Comparator<T> {
        abstract int diff(T t);

        private ClosestComparator() {
        }

        /* synthetic */ ClosestComparator(AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // java.util.Comparator
        public int compare(T t, T t2) {
            return diff(t) - diff(t2);
        }
    }

    /* renamed from: org.webrtc.CameraEnumerationAndroid$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends ClosestComparator<CaptureFormat.FramerateRange> {
        private static final int MAX_FPS_DIFF_THRESHOLD = 5000;
        private static final int MAX_FPS_HIGH_DIFF_WEIGHT = 3;
        private static final int MAX_FPS_LOW_DIFF_WEIGHT = 1;
        private static final int MIN_FPS_HIGH_VALUE_WEIGHT = 4;
        private static final int MIN_FPS_LOW_VALUE_WEIGHT = 1;
        private static final int MIN_FPS_THRESHOLD = 8000;
        final /* synthetic */ int val$requestedFps;

        private int progressivePenalty(int i, int i2, int i3, int i4) {
            if (i < i2) {
                return i * i3;
            }
            return ((i - i2) * i4) + (i3 * i2);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(int i) {
            super(null);
            this.val$requestedFps = i;
        }

        public int diff(CaptureFormat.FramerateRange framerateRange) {
            return progressivePenalty(framerateRange.min, 8000, 1, 4) + progressivePenalty(Math.abs((this.val$requestedFps * 1000) - framerateRange.max), 5000, 1, 3);
        }
    }

    public static CaptureFormat.FramerateRange getClosestSupportedFramerateRange(List<CaptureFormat.FramerateRange> list, int i) {
        return (CaptureFormat.FramerateRange) Collections.min(list, new AnonymousClass1(i));
    }

    /* renamed from: org.webrtc.CameraEnumerationAndroid$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends ClosestComparator<Size> {
        final /* synthetic */ int val$requestedHeight;
        final /* synthetic */ int val$requestedWidth;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(int i, int i2) {
            super(null);
            this.val$requestedWidth = i;
            this.val$requestedHeight = i2;
        }

        public int diff(Size size) {
            return Math.abs(this.val$requestedWidth - size.width) + Math.abs(this.val$requestedHeight - size.height);
        }
    }

    public static Size getClosestSupportedSize(List<Size> list, int i, int i2) {
        return (Size) Collections.min(list, new AnonymousClass2(i, i2));
    }

    public static void reportCameraResolution(Histogram histogram, Size size) {
        histogram.addSample(COMMON_RESOLUTIONS.indexOf(size) + 1);
    }
}
