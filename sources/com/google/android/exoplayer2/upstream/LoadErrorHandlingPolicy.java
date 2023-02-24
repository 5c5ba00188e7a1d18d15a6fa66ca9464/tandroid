package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.source.LoadEventInfo;
import com.google.android.exoplayer2.source.MediaLoadData;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
/* loaded from: classes.dex */
public interface LoadErrorHandlingPolicy {

    /* loaded from: classes.dex */
    public final /* synthetic */ class -CC {
        public static void $default$onLoadTaskConcluded(LoadErrorHandlingPolicy loadErrorHandlingPolicy, long j) {
        }
    }

    FallbackSelection getFallbackSelectionFor(FallbackOptions fallbackOptions, LoadErrorInfo loadErrorInfo);

    int getMinimumLoadableRetryCount(int i);

    long getRetryDelayMsFor(LoadErrorInfo loadErrorInfo);

    void onLoadTaskConcluded(long j);

    /* loaded from: classes.dex */
    public static final class LoadErrorInfo {
        public final int errorCount;
        public final IOException exception;

        public LoadErrorInfo(LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException iOException, int i) {
            this.exception = iOException;
            this.errorCount = i;
        }
    }

    /* loaded from: classes.dex */
    public static final class FallbackOptions {
        public final int numberOfExcludedLocations;
        public final int numberOfExcludedTracks;
        public final int numberOfLocations;
        public final int numberOfTracks;

        public FallbackOptions(int i, int i2, int i3, int i4) {
            this.numberOfLocations = i;
            this.numberOfExcludedLocations = i2;
            this.numberOfTracks = i3;
            this.numberOfExcludedTracks = i4;
        }

        public boolean isFallbackAvailable(int i) {
            if (i == 1) {
                if (this.numberOfLocations - this.numberOfExcludedLocations <= 1) {
                    return false;
                }
            } else if (this.numberOfTracks - this.numberOfExcludedTracks <= 1) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: classes.dex */
    public static final class FallbackSelection {
        public final long exclusionDurationMs;
        public final int type;

        public FallbackSelection(int i, long j) {
            Assertions.checkArgument(j >= 0);
            this.type = i;
            this.exclusionDurationMs = j;
        }
    }
}
