package com.google.android.exoplayer2.mediacodec;

import com.google.android.exoplayer2.decoder.DecoderException;
import com.google.android.exoplayer2.util.Util;
/* loaded from: classes.dex */
public class MediaCodecDecoderException extends DecoderException {
    public final MediaCodecInfo codecInfo;
    public final String diagnosticInfo;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MediaCodecDecoderException(Throwable th, MediaCodecInfo mediaCodecInfo) {
        super(r0.toString(), th);
        StringBuilder sb = new StringBuilder();
        sb.append("Decoder failed: ");
        sb.append(mediaCodecInfo == null ? null : mediaCodecInfo.name);
        this.codecInfo = mediaCodecInfo;
        this.diagnosticInfo = Util.SDK_INT >= 21 ? getDiagnosticInfoV21(th) : null;
    }

    private static String getDiagnosticInfoV21(Throwable th) {
        String diagnosticInfo;
        if (MediaCodecDecoderException$$ExternalSyntheticApiModelOutline0.m(th)) {
            diagnosticInfo = MediaCodecDecoderException$$ExternalSyntheticApiModelOutline1.m(th).getDiagnosticInfo();
            return diagnosticInfo;
        }
        return null;
    }
}
