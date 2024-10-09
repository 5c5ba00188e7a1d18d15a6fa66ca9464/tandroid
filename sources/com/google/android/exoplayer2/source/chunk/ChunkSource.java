package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.source.LoadingInfo;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import java.util.List;

/* loaded from: classes.dex */
public interface ChunkSource {
    long getAdjustedSeekPositionUs(long j, SeekParameters seekParameters);

    void getNextChunk(LoadingInfo loadingInfo, long j, List list, ChunkHolder chunkHolder);

    int getPreferredQueueSize(long j, List list);

    void maybeThrowError();

    void onChunkLoadCompleted(Chunk chunk);

    boolean onChunkLoadError(Chunk chunk, boolean z, LoadErrorHandlingPolicy.LoadErrorInfo loadErrorInfo, LoadErrorHandlingPolicy loadErrorHandlingPolicy);

    void release();

    boolean shouldCancelLoad(long j, Chunk chunk, List list);
}
