package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.util.Assertions;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class CompositeSequenceableLoader implements SequenceableLoader {
    private long lastAudioVideoBufferedPositionUs;
    private final ImmutableList loadersWithTrackTypes;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class SequenceableLoaderWithTrackTypes implements SequenceableLoader {
        private final SequenceableLoader loader;
        private final ImmutableList trackTypes;

        public SequenceableLoaderWithTrackTypes(SequenceableLoader sequenceableLoader, List list) {
            this.loader = sequenceableLoader;
            this.trackTypes = ImmutableList.copyOf((Collection) list);
        }

        @Override // com.google.android.exoplayer2.source.SequenceableLoader
        public boolean continueLoading(LoadingInfo loadingInfo) {
            return this.loader.continueLoading(loadingInfo);
        }

        @Override // com.google.android.exoplayer2.source.SequenceableLoader
        public long getBufferedPositionUs() {
            return this.loader.getBufferedPositionUs();
        }

        @Override // com.google.android.exoplayer2.source.SequenceableLoader
        public long getNextLoadPositionUs() {
            return this.loader.getNextLoadPositionUs();
        }

        public ImmutableList getTrackTypes() {
            return this.trackTypes;
        }

        @Override // com.google.android.exoplayer2.source.SequenceableLoader
        public boolean isLoading() {
            return this.loader.isLoading();
        }

        @Override // com.google.android.exoplayer2.source.SequenceableLoader
        public void reevaluateBuffer(long j) {
            this.loader.reevaluateBuffer(j);
        }
    }

    public CompositeSequenceableLoader(List list, List list2) {
        ImmutableList.Builder builder = ImmutableList.builder();
        Assertions.checkArgument(list.size() == list2.size());
        for (int i = 0; i < list.size(); i++) {
            builder.add((Object) new SequenceableLoaderWithTrackTypes((SequenceableLoader) list.get(i), (List) list2.get(i)));
        }
        this.loadersWithTrackTypes = builder.build();
        this.lastAudioVideoBufferedPositionUs = -9223372036854775807L;
    }

    public CompositeSequenceableLoader(SequenceableLoader[] sequenceableLoaderArr) {
        this(ImmutableList.copyOf(sequenceableLoaderArr), Collections.nCopies(sequenceableLoaderArr.length, ImmutableList.of((Object) (-1))));
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader
    public boolean continueLoading(LoadingInfo loadingInfo) {
        boolean z;
        boolean z2 = false;
        do {
            long nextLoadPositionUs = getNextLoadPositionUs();
            if (nextLoadPositionUs == Long.MIN_VALUE) {
                break;
            }
            z = false;
            for (int i = 0; i < this.loadersWithTrackTypes.size(); i++) {
                long nextLoadPositionUs2 = ((SequenceableLoaderWithTrackTypes) this.loadersWithTrackTypes.get(i)).getNextLoadPositionUs();
                boolean z3 = nextLoadPositionUs2 != Long.MIN_VALUE && nextLoadPositionUs2 <= loadingInfo.playbackPositionUs;
                if (nextLoadPositionUs2 == nextLoadPositionUs || z3) {
                    z |= ((SequenceableLoaderWithTrackTypes) this.loadersWithTrackTypes.get(i)).continueLoading(loadingInfo);
                }
            }
            z2 |= z;
        } while (z);
        return z2;
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader
    public long getBufferedPositionUs() {
        long j = Long.MAX_VALUE;
        long j2 = Long.MAX_VALUE;
        for (int i = 0; i < this.loadersWithTrackTypes.size(); i++) {
            SequenceableLoaderWithTrackTypes sequenceableLoaderWithTrackTypes = (SequenceableLoaderWithTrackTypes) this.loadersWithTrackTypes.get(i);
            long bufferedPositionUs = sequenceableLoaderWithTrackTypes.getBufferedPositionUs();
            if ((sequenceableLoaderWithTrackTypes.getTrackTypes().contains(1) || sequenceableLoaderWithTrackTypes.getTrackTypes().contains(2) || sequenceableLoaderWithTrackTypes.getTrackTypes().contains(4)) && bufferedPositionUs != Long.MIN_VALUE) {
                j = Math.min(j, bufferedPositionUs);
            }
            if (bufferedPositionUs != Long.MIN_VALUE) {
                j2 = Math.min(j2, bufferedPositionUs);
            }
        }
        if (j != Long.MAX_VALUE) {
            this.lastAudioVideoBufferedPositionUs = j;
            return j;
        }
        if (j2 == Long.MAX_VALUE) {
            return Long.MIN_VALUE;
        }
        long j3 = this.lastAudioVideoBufferedPositionUs;
        return j3 != -9223372036854775807L ? j3 : j2;
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        long j = Long.MAX_VALUE;
        for (int i = 0; i < this.loadersWithTrackTypes.size(); i++) {
            long nextLoadPositionUs = ((SequenceableLoaderWithTrackTypes) this.loadersWithTrackTypes.get(i)).getNextLoadPositionUs();
            if (nextLoadPositionUs != Long.MIN_VALUE) {
                j = Math.min(j, nextLoadPositionUs);
            }
        }
        if (j == Long.MAX_VALUE) {
            return Long.MIN_VALUE;
        }
        return j;
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader
    public boolean isLoading() {
        for (int i = 0; i < this.loadersWithTrackTypes.size(); i++) {
            if (((SequenceableLoaderWithTrackTypes) this.loadersWithTrackTypes.get(i)).isLoading()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader
    public void reevaluateBuffer(long j) {
        for (int i = 0; i < this.loadersWithTrackTypes.size(); i++) {
            ((SequenceableLoaderWithTrackTypes) this.loadersWithTrackTypes.get(i)).reevaluateBuffer(j);
        }
    }
}
