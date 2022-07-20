package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
/* loaded from: classes.dex */
public final /* synthetic */ class CompositeMediaSource$$ExternalSyntheticLambda0 implements MediaSource.MediaSourceCaller {
    public final /* synthetic */ CompositeMediaSource f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ CompositeMediaSource$$ExternalSyntheticLambda0(CompositeMediaSource compositeMediaSource, Object obj) {
        this.f$0 = compositeMediaSource;
        this.f$1 = obj;
    }

    @Override // com.google.android.exoplayer2.source.MediaSource.MediaSourceCaller
    public final void onSourceInfoRefreshed(MediaSource mediaSource, Timeline timeline) {
        this.f$0.lambda$prepareChildSource$0(this.f$1, mediaSource, timeline);
    }
}
