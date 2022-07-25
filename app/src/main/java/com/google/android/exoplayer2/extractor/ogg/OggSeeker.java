package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.SeekMap;
import java.io.IOException;
/* loaded from: classes.dex */
interface OggSeeker {
    /* renamed from: createSeekMap */
    SeekMap mo94createSeekMap();

    long read(ExtractorInput extractorInput) throws IOException, InterruptedException;

    void startSeek(long j);
}
