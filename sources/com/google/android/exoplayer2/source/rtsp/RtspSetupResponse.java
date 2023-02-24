package com.google.android.exoplayer2.source.rtsp;

import com.google.android.exoplayer2.source.rtsp.RtspMessageUtil;
/* loaded from: classes.dex */
final class RtspSetupResponse {
    public final RtspMessageUtil.RtspSessionHeader sessionHeader;

    public RtspSetupResponse(int i, RtspMessageUtil.RtspSessionHeader rtspSessionHeader, String str) {
        this.sessionHeader = rtspSessionHeader;
    }
}
