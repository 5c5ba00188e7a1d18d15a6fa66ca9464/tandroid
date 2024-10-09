package com.google.android.exoplayer2.source;

import android.net.Uri;
import com.google.android.exoplayer2.analytics.PlayerId;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.upstream.DataReader;
import java.util.Map;

/* loaded from: classes.dex */
public interface ProgressiveMediaExtractor {

    /* loaded from: classes.dex */
    public interface Factory {
        ProgressiveMediaExtractor createProgressiveMediaExtractor(PlayerId playerId);
    }

    void disableSeekingOnMp3Streams();

    long getCurrentInputPosition();

    void init(DataReader dataReader, Uri uri, Map map, long j, long j2, ExtractorOutput extractorOutput);

    int read(PositionHolder positionHolder);

    void release();

    void seek(long j, long j2);
}
