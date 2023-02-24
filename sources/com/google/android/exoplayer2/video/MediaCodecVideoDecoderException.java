package com.google.android.exoplayer2.video;

import android.view.Surface;
import com.google.android.exoplayer2.mediacodec.MediaCodecDecoderException;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
/* loaded from: classes.dex */
public class MediaCodecVideoDecoderException extends MediaCodecDecoderException {
    public MediaCodecVideoDecoderException(Throwable th, MediaCodecInfo mediaCodecInfo, Surface surface) {
        super(th, mediaCodecInfo);
        System.identityHashCode(surface);
        if (surface != null) {
            surface.isValid();
        }
    }
}
