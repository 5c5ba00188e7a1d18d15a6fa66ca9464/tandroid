package org.webrtc;
/* loaded from: classes4.dex */
public interface RefCounted {
    @CalledByNative
    void release();

    @CalledByNative
    void retain();
}
