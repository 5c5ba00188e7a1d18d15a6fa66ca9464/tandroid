package com.google.android.exoplayer2.drm;

import android.media.DeniedByServerException;
import android.media.NotProvisionedException;
import com.google.android.exoplayer2.analytics.MediaMetricsListener$$ExternalSyntheticApiModelOutline48;
import com.google.android.exoplayer2.analytics.MediaMetricsListener$$ExternalSyntheticApiModelOutline49;
import com.google.android.exoplayer2.analytics.MediaMetricsListener$$ExternalSyntheticApiModelOutline51;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
public abstract class DrmUtil {

    private static final class Api18 {
        public static boolean isDeniedByServerException(Throwable th) {
            return th instanceof DeniedByServerException;
        }

        public static boolean isNotProvisionedException(Throwable th) {
            return th instanceof NotProvisionedException;
        }
    }

    private static final class Api21 {
        public static boolean isMediaDrmStateException(Throwable th) {
            return MediaMetricsListener$$ExternalSyntheticApiModelOutline48.m(th);
        }

        public static int mediaDrmStateExceptionToErrorCode(Throwable th) {
            String diagnosticInfo;
            diagnosticInfo = MediaMetricsListener$$ExternalSyntheticApiModelOutline49.m(th).getDiagnosticInfo();
            return Util.getErrorCodeForMediaDrmErrorCode(Util.getErrorCodeFromPlatformDiagnosticsInfo(diagnosticInfo));
        }
    }

    private static final class Api23 {
        public static boolean isMediaDrmResetException(Throwable th) {
            return MediaMetricsListener$$ExternalSyntheticApiModelOutline51.m(th);
        }
    }

    public static int getErrorCodeForMediaDrmException(Exception exc, int i) {
        int i2 = Util.SDK_INT;
        if (i2 >= 21 && Api21.isMediaDrmStateException(exc)) {
            return Api21.mediaDrmStateExceptionToErrorCode(exc);
        }
        if (i2 >= 23 && Api23.isMediaDrmResetException(exc)) {
            return 6006;
        }
        if (i2 >= 18 && Api18.isNotProvisionedException(exc)) {
            return 6002;
        }
        if (i2 >= 18 && Api18.isDeniedByServerException(exc)) {
            return 6007;
        }
        if (exc instanceof UnsupportedDrmException) {
            return 6001;
        }
        if (exc instanceof DefaultDrmSessionManager.MissingSchemeDataException) {
            return 6003;
        }
        if (exc instanceof KeysExpiredException) {
            return 6008;
        }
        if (i == 1) {
            return 6006;
        }
        if (i == 2) {
            return 6004;
        }
        if (i == 3) {
            return 6002;
        }
        throw new IllegalArgumentException();
    }
}
