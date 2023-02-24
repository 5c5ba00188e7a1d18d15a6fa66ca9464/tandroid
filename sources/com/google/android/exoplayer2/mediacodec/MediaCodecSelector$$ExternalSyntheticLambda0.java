package com.google.android.exoplayer2.mediacodec;

import java.util.List;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaCodecSelector$$ExternalSyntheticLambda0 implements MediaCodecSelector {
    public static final /* synthetic */ MediaCodecSelector$$ExternalSyntheticLambda0 INSTANCE = new MediaCodecSelector$$ExternalSyntheticLambda0();

    private /* synthetic */ MediaCodecSelector$$ExternalSyntheticLambda0() {
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecSelector
    public final List getDecoderInfos(String str, boolean z, boolean z2) {
        return MediaCodecUtil.getDecoderInfos(str, z, z2);
    }
}
