package com.google.android.exoplayer2.source.rtsp.reader;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
/* loaded from: classes.dex */
public interface RtpPayloadReader {
    void consume(ParsableByteArray parsableByteArray, long j, int i, boolean z) throws ParserException;

    void createTracks(ExtractorOutput extractorOutput, int i);

    void onReceivingFirstPacket(long j, int i);

    void seek(long j, long j2);
}
