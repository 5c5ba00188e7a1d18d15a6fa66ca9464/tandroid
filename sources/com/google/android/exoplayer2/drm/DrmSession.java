package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.decoder.CryptoConfig;
import com.google.android.exoplayer2.drm.DrmSessionEventListener;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/* loaded from: classes.dex */
public interface DrmSession {

    public abstract /* synthetic */ class -CC {
        public static void replaceSession(DrmSession drmSession, DrmSession drmSession2) {
            if (drmSession == drmSession2) {
                return;
            }
            if (drmSession2 != null) {
                drmSession2.acquire(null);
            }
            if (drmSession != null) {
                drmSession.release(null);
            }
        }
    }

    public static class DrmSessionException extends IOException {
        public final int errorCode;

        public DrmSessionException(Throwable th, int i) {
            super(th);
            this.errorCode = i;
        }
    }

    void acquire(DrmSessionEventListener.EventDispatcher eventDispatcher);

    CryptoConfig getCryptoConfig();

    DrmSessionException getError();

    UUID getSchemeUuid();

    int getState();

    boolean playClearSamplesWithoutKeys();

    Map queryKeyStatus();

    void release(DrmSessionEventListener.EventDispatcher eventDispatcher);

    boolean requiresSecureDecoder(String str);
}
