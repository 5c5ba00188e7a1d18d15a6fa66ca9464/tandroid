package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.analytics.PlayerId;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.chunk.ChunkExtractor;
import java.util.List;
/* loaded from: classes.dex */
public final /* synthetic */ class BundledChunkExtractor$$ExternalSyntheticLambda0 implements ChunkExtractor.Factory {
    public static final /* synthetic */ BundledChunkExtractor$$ExternalSyntheticLambda0 INSTANCE = new BundledChunkExtractor$$ExternalSyntheticLambda0();

    private /* synthetic */ BundledChunkExtractor$$ExternalSyntheticLambda0() {
    }

    @Override // com.google.android.exoplayer2.source.chunk.ChunkExtractor.Factory
    public final ChunkExtractor createProgressiveMediaExtractor(int i, Format format, boolean z, List list, TrackOutput trackOutput, PlayerId playerId) {
        ChunkExtractor lambda$static$0;
        lambda$static$0 = BundledChunkExtractor.lambda$static$0(i, format, z, list, trackOutput, playerId);
        return lambda$static$0;
    }
}
