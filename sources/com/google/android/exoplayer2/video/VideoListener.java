package com.google.android.exoplayer2.video;

import android.graphics.SurfaceTexture;

/* loaded from: classes.dex */
public interface VideoListener {
    boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture);

    void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture);
}
