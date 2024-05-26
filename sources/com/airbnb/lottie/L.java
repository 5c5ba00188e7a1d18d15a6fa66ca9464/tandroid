package com.airbnb.lottie;

import com.airbnb.lottie.utils.LottieTrace;
/* loaded from: classes.dex */
public class L {
    public static boolean DBG = false;
    private static AsyncUpdates defaultAsyncUpdates = AsyncUpdates.AUTOMATIC;
    private static boolean disablePathInterpolatorCache = true;
    private static ThreadLocal<LottieTrace> lottieTrace = null;
    private static boolean traceEnabled = false;

    public static void beginSection(String str) {
        if (traceEnabled) {
            getTrace().beginSection(str);
        }
    }

    public static float endSection(String str) {
        if (traceEnabled) {
            return getTrace().endSection(str);
        }
        return 0.0f;
    }

    private static LottieTrace getTrace() {
        LottieTrace lottieTrace2 = lottieTrace.get();
        if (lottieTrace2 == null) {
            LottieTrace lottieTrace3 = new LottieTrace();
            lottieTrace.set(lottieTrace3);
            return lottieTrace3;
        }
        return lottieTrace2;
    }

    public static boolean getDisablePathInterpolatorCache() {
        return disablePathInterpolatorCache;
    }

    public static AsyncUpdates getDefaultAsyncUpdates() {
        return defaultAsyncUpdates;
    }
}
