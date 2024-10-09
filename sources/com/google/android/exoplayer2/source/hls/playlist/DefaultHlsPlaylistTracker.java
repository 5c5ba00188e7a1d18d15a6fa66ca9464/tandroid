package com.google.android.exoplayer2.source.hls.playlist;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.source.LoadEventInfo;
import com.google.android.exoplayer2.source.MediaLoadData;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.playlist.DefaultHlsPlaylistTracker;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMultivariantPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.telegram.tgnet.ConnectionsManager;

/* loaded from: classes.dex */
public final class DefaultHlsPlaylistTracker implements HlsPlaylistTracker, Loader.Callback {
    public static final HlsPlaylistTracker.Factory FACTORY = new HlsPlaylistTracker.Factory() { // from class: com.google.android.exoplayer2.source.hls.playlist.DefaultHlsPlaylistTracker$$ExternalSyntheticLambda0
        @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.Factory
        public final HlsPlaylistTracker createTracker(HlsDataSourceFactory hlsDataSourceFactory, LoadErrorHandlingPolicy loadErrorHandlingPolicy, HlsPlaylistParserFactory hlsPlaylistParserFactory) {
            return new DefaultHlsPlaylistTracker(hlsDataSourceFactory, loadErrorHandlingPolicy, hlsPlaylistParserFactory);
        }
    };
    private final HlsDataSourceFactory dataSourceFactory;
    private MediaSourceEventListener.EventDispatcher eventDispatcher;
    private Loader initialPlaylistLoader;
    private long initialStartTimeUs;
    private boolean isLive;
    private final CopyOnWriteArrayList listeners;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private HlsMultivariantPlaylist multivariantPlaylist;
    private final HashMap playlistBundles;
    private final HlsPlaylistParserFactory playlistParserFactory;
    private Handler playlistRefreshHandler;
    private final double playlistStuckTargetDurationCoefficient;
    private HlsMediaPlaylist primaryMediaPlaylistSnapshot;
    private Uri primaryMediaPlaylistUrl;
    private HlsPlaylistTracker.PrimaryPlaylistListener primaryPlaylistListener;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class FirstPrimaryMediaPlaylistListener implements HlsPlaylistTracker.PlaylistEventListener {
        private FirstPrimaryMediaPlaylistListener() {
        }

        @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.PlaylistEventListener
        public void onPlaylistChanged() {
            DefaultHlsPlaylistTracker.this.listeners.remove(this);
        }

        @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.PlaylistEventListener
        public boolean onPlaylistError(Uri uri, LoadErrorHandlingPolicy.LoadErrorInfo loadErrorInfo, boolean z) {
            MediaPlaylistBundle mediaPlaylistBundle;
            if (DefaultHlsPlaylistTracker.this.primaryMediaPlaylistSnapshot == null) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                List list = ((HlsMultivariantPlaylist) Util.castNonNull(DefaultHlsPlaylistTracker.this.multivariantPlaylist)).variants;
                int i = 0;
                for (int i2 = 0; i2 < list.size(); i2++) {
                    MediaPlaylistBundle mediaPlaylistBundle2 = (MediaPlaylistBundle) DefaultHlsPlaylistTracker.this.playlistBundles.get(((HlsMultivariantPlaylist.Variant) list.get(i2)).url);
                    if (mediaPlaylistBundle2 != null && elapsedRealtime < mediaPlaylistBundle2.excludeUntilMs) {
                        i++;
                    }
                }
                LoadErrorHandlingPolicy.FallbackSelection fallbackSelectionFor = DefaultHlsPlaylistTracker.this.loadErrorHandlingPolicy.getFallbackSelectionFor(new LoadErrorHandlingPolicy.FallbackOptions(1, 0, DefaultHlsPlaylistTracker.this.multivariantPlaylist.variants.size(), i), loadErrorInfo);
                if (fallbackSelectionFor != null && fallbackSelectionFor.type == 2 && (mediaPlaylistBundle = (MediaPlaylistBundle) DefaultHlsPlaylistTracker.this.playlistBundles.get(uri)) != null) {
                    mediaPlaylistBundle.excludePlaylist(fallbackSelectionFor.exclusionDurationMs);
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class MediaPlaylistBundle implements Loader.Callback {
        private boolean activeForPlayback;
        private long earliestNextLoadTimeMs;
        private long excludeUntilMs;
        private long lastSnapshotChangeMs;
        private long lastSnapshotLoadMs;
        private boolean loadPending;
        private final DataSource mediaPlaylistDataSource;
        private final Loader mediaPlaylistLoader = new Loader("DefaultHlsPlaylistTracker:MediaPlaylist");
        private IOException playlistError;
        private HlsMediaPlaylist playlistSnapshot;
        private final Uri playlistUrl;

        public MediaPlaylistBundle(Uri uri) {
            this.playlistUrl = uri;
            this.mediaPlaylistDataSource = DefaultHlsPlaylistTracker.this.dataSourceFactory.createDataSource(4);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean excludePlaylist(long j) {
            this.excludeUntilMs = SystemClock.elapsedRealtime() + j;
            return this.playlistUrl.equals(DefaultHlsPlaylistTracker.this.primaryMediaPlaylistUrl) && !DefaultHlsPlaylistTracker.this.maybeSelectNewPrimaryUrl();
        }

        private Uri getMediaPlaylistUriForReload() {
            HlsMediaPlaylist hlsMediaPlaylist = this.playlistSnapshot;
            if (hlsMediaPlaylist != null) {
                HlsMediaPlaylist.ServerControl serverControl = hlsMediaPlaylist.serverControl;
                if (serverControl.skipUntilUs != -9223372036854775807L || serverControl.canBlockReload) {
                    Uri.Builder buildUpon = this.playlistUrl.buildUpon();
                    HlsMediaPlaylist hlsMediaPlaylist2 = this.playlistSnapshot;
                    if (hlsMediaPlaylist2.serverControl.canBlockReload) {
                        buildUpon.appendQueryParameter("_HLS_msn", String.valueOf(hlsMediaPlaylist2.mediaSequence + hlsMediaPlaylist2.segments.size()));
                        HlsMediaPlaylist hlsMediaPlaylist3 = this.playlistSnapshot;
                        if (hlsMediaPlaylist3.partTargetDurationUs != -9223372036854775807L) {
                            List list = hlsMediaPlaylist3.trailingParts;
                            int size = list.size();
                            if (!list.isEmpty() && ((HlsMediaPlaylist.Part) Iterables.getLast(list)).isPreload) {
                                size--;
                            }
                            buildUpon.appendQueryParameter("_HLS_part", String.valueOf(size));
                        }
                    }
                    HlsMediaPlaylist.ServerControl serverControl2 = this.playlistSnapshot.serverControl;
                    if (serverControl2.skipUntilUs != -9223372036854775807L) {
                        buildUpon.appendQueryParameter("_HLS_skip", serverControl2.canSkipDateRanges ? "v2" : "YES");
                    }
                    return buildUpon.build();
                }
            }
            return this.playlistUrl;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$loadPlaylistInternal$0(Uri uri) {
            this.loadPending = false;
            loadPlaylistImmediately(uri);
        }

        private void loadPlaylistImmediately(Uri uri) {
            ParsingLoadable parsingLoadable = new ParsingLoadable(this.mediaPlaylistDataSource, uri, 4, DefaultHlsPlaylistTracker.this.playlistParserFactory.createPlaylistParser(DefaultHlsPlaylistTracker.this.multivariantPlaylist, this.playlistSnapshot));
            DefaultHlsPlaylistTracker.this.eventDispatcher.loadStarted(new LoadEventInfo(parsingLoadable.loadTaskId, parsingLoadable.dataSpec, this.mediaPlaylistLoader.startLoading(parsingLoadable, this, DefaultHlsPlaylistTracker.this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(parsingLoadable.type))), parsingLoadable.type);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void loadPlaylistInternal(final Uri uri) {
            this.excludeUntilMs = 0L;
            if (this.loadPending || this.mediaPlaylistLoader.isLoading() || this.mediaPlaylistLoader.hasFatalError()) {
                return;
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (elapsedRealtime >= this.earliestNextLoadTimeMs) {
                loadPlaylistImmediately(uri);
            } else {
                this.loadPending = true;
                DefaultHlsPlaylistTracker.this.playlistRefreshHandler.postDelayed(new Runnable() { // from class: com.google.android.exoplayer2.source.hls.playlist.DefaultHlsPlaylistTracker$MediaPlaylistBundle$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        DefaultHlsPlaylistTracker.MediaPlaylistBundle.this.lambda$loadPlaylistInternal$0(uri);
                    }
                }, this.earliestNextLoadTimeMs - elapsedRealtime);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void processLoadedPlaylist(HlsMediaPlaylist hlsMediaPlaylist, LoadEventInfo loadEventInfo) {
            boolean z;
            long j;
            HlsMediaPlaylist hlsMediaPlaylist2 = this.playlistSnapshot;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            this.lastSnapshotLoadMs = elapsedRealtime;
            HlsMediaPlaylist latestPlaylistSnapshot = DefaultHlsPlaylistTracker.this.getLatestPlaylistSnapshot(hlsMediaPlaylist2, hlsMediaPlaylist);
            this.playlistSnapshot = latestPlaylistSnapshot;
            IOException iOException = null;
            if (latestPlaylistSnapshot != hlsMediaPlaylist2) {
                this.playlistError = null;
                this.lastSnapshotChangeMs = elapsedRealtime;
                DefaultHlsPlaylistTracker.this.onPlaylistUpdated(this.playlistUrl, latestPlaylistSnapshot);
            } else if (!latestPlaylistSnapshot.hasEndTag) {
                long size = hlsMediaPlaylist.mediaSequence + hlsMediaPlaylist.segments.size();
                HlsMediaPlaylist hlsMediaPlaylist3 = this.playlistSnapshot;
                if (size < hlsMediaPlaylist3.mediaSequence) {
                    iOException = new HlsPlaylistTracker.PlaylistResetException(this.playlistUrl);
                    z = true;
                } else {
                    double d = elapsedRealtime - this.lastSnapshotChangeMs;
                    double usToMs = Util.usToMs(hlsMediaPlaylist3.targetDurationUs);
                    double d2 = DefaultHlsPlaylistTracker.this.playlistStuckTargetDurationCoefficient;
                    Double.isNaN(usToMs);
                    z = false;
                    if (d > usToMs * d2) {
                        iOException = new HlsPlaylistTracker.PlaylistStuckException(this.playlistUrl);
                    }
                }
                if (iOException != null) {
                    this.playlistError = iOException;
                    DefaultHlsPlaylistTracker.this.notifyPlaylistError(this.playlistUrl, new LoadErrorHandlingPolicy.LoadErrorInfo(loadEventInfo, new MediaLoadData(4), iOException, 1), z);
                }
            }
            HlsMediaPlaylist hlsMediaPlaylist4 = this.playlistSnapshot;
            if (hlsMediaPlaylist4.serverControl.canBlockReload) {
                j = 0;
            } else {
                j = hlsMediaPlaylist4.targetDurationUs;
                if (hlsMediaPlaylist4 == hlsMediaPlaylist2) {
                    j /= 2;
                }
            }
            this.earliestNextLoadTimeMs = (elapsedRealtime + Util.usToMs(j)) - loadEventInfo.loadDurationMs;
            if (this.playlistSnapshot.hasEndTag) {
                return;
            }
            if (this.playlistUrl.equals(DefaultHlsPlaylistTracker.this.primaryMediaPlaylistUrl) || this.activeForPlayback) {
                loadPlaylistInternal(getMediaPlaylistUriForReload());
            }
        }

        public HlsMediaPlaylist getPlaylistSnapshot() {
            return this.playlistSnapshot;
        }

        public boolean isActiveForPlayback() {
            return this.activeForPlayback;
        }

        public boolean isSnapshotValid() {
            int i;
            if (this.playlistSnapshot == null) {
                return false;
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long max = Math.max(30000L, Util.usToMs(this.playlistSnapshot.durationUs));
            HlsMediaPlaylist hlsMediaPlaylist = this.playlistSnapshot;
            return hlsMediaPlaylist.hasEndTag || (i = hlsMediaPlaylist.playlistType) == 2 || i == 1 || this.lastSnapshotLoadMs + max > elapsedRealtime;
        }

        public void loadPlaylist(boolean z) {
            loadPlaylistInternal(z ? getMediaPlaylistUriForReload() : this.playlistUrl);
        }

        public void maybeThrowPlaylistRefreshError() {
            this.mediaPlaylistLoader.maybeThrowError();
            IOException iOException = this.playlistError;
            if (iOException != null) {
                throw iOException;
            }
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public void onLoadCanceled(ParsingLoadable parsingLoadable, long j, long j2, boolean z) {
            LoadEventInfo loadEventInfo = new LoadEventInfo(parsingLoadable.loadTaskId, parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), j, j2, parsingLoadable.bytesLoaded());
            DefaultHlsPlaylistTracker.this.loadErrorHandlingPolicy.onLoadTaskConcluded(parsingLoadable.loadTaskId);
            DefaultHlsPlaylistTracker.this.eventDispatcher.loadCanceled(loadEventInfo, 4);
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public void onLoadCompleted(ParsingLoadable parsingLoadable, long j, long j2) {
            HlsPlaylist hlsPlaylist = (HlsPlaylist) parsingLoadable.getResult();
            LoadEventInfo loadEventInfo = new LoadEventInfo(parsingLoadable.loadTaskId, parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), j, j2, parsingLoadable.bytesLoaded());
            if (hlsPlaylist instanceof HlsMediaPlaylist) {
                processLoadedPlaylist((HlsMediaPlaylist) hlsPlaylist, loadEventInfo);
                DefaultHlsPlaylistTracker.this.eventDispatcher.loadCompleted(loadEventInfo, 4);
            } else {
                this.playlistError = ParserException.createForMalformedManifest("Loaded playlist has unexpected type.", null);
                DefaultHlsPlaylistTracker.this.eventDispatcher.loadError(loadEventInfo, 4, this.playlistError, true);
            }
            DefaultHlsPlaylistTracker.this.loadErrorHandlingPolicy.onLoadTaskConcluded(parsingLoadable.loadTaskId);
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public Loader.LoadErrorAction onLoadError(ParsingLoadable parsingLoadable, long j, long j2, IOException iOException, int i) {
            Loader.LoadErrorAction loadErrorAction;
            LoadEventInfo loadEventInfo = new LoadEventInfo(parsingLoadable.loadTaskId, parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), j, j2, parsingLoadable.bytesLoaded());
            boolean z = iOException instanceof HlsPlaylistParser.DeltaUpdateException;
            if ((parsingLoadable.getUri().getQueryParameter("_HLS_msn") != null) || z) {
                int i2 = iOException instanceof HttpDataSource.InvalidResponseCodeException ? ((HttpDataSource.InvalidResponseCodeException) iOException).responseCode : ConnectionsManager.DEFAULT_DATACENTER_ID;
                if (z || i2 == 400 || i2 == 503) {
                    this.earliestNextLoadTimeMs = SystemClock.elapsedRealtime();
                    loadPlaylist(false);
                    ((MediaSourceEventListener.EventDispatcher) Util.castNonNull(DefaultHlsPlaylistTracker.this.eventDispatcher)).loadError(loadEventInfo, parsingLoadable.type, iOException, true);
                    return Loader.DONT_RETRY;
                }
            }
            LoadErrorHandlingPolicy.LoadErrorInfo loadErrorInfo = new LoadErrorHandlingPolicy.LoadErrorInfo(loadEventInfo, new MediaLoadData(parsingLoadable.type), iOException, i);
            if (DefaultHlsPlaylistTracker.this.notifyPlaylistError(this.playlistUrl, loadErrorInfo, false)) {
                long retryDelayMsFor = DefaultHlsPlaylistTracker.this.loadErrorHandlingPolicy.getRetryDelayMsFor(loadErrorInfo);
                loadErrorAction = retryDelayMsFor != -9223372036854775807L ? Loader.createRetryAction(false, retryDelayMsFor) : Loader.DONT_RETRY_FATAL;
            } else {
                loadErrorAction = Loader.DONT_RETRY;
            }
            boolean isRetry = true ^ loadErrorAction.isRetry();
            DefaultHlsPlaylistTracker.this.eventDispatcher.loadError(loadEventInfo, parsingLoadable.type, iOException, isRetry);
            if (isRetry) {
                DefaultHlsPlaylistTracker.this.loadErrorHandlingPolicy.onLoadTaskConcluded(parsingLoadable.loadTaskId);
            }
            return loadErrorAction;
        }

        public void release() {
            this.mediaPlaylistLoader.release();
        }

        public void setActiveForPlayback(boolean z) {
            this.activeForPlayback = z;
        }
    }

    public DefaultHlsPlaylistTracker(HlsDataSourceFactory hlsDataSourceFactory, LoadErrorHandlingPolicy loadErrorHandlingPolicy, HlsPlaylistParserFactory hlsPlaylistParserFactory) {
        this(hlsDataSourceFactory, loadErrorHandlingPolicy, hlsPlaylistParserFactory, 3.5d);
    }

    public DefaultHlsPlaylistTracker(HlsDataSourceFactory hlsDataSourceFactory, LoadErrorHandlingPolicy loadErrorHandlingPolicy, HlsPlaylistParserFactory hlsPlaylistParserFactory, double d) {
        this.dataSourceFactory = hlsDataSourceFactory;
        this.playlistParserFactory = hlsPlaylistParserFactory;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.playlistStuckTargetDurationCoefficient = d;
        this.listeners = new CopyOnWriteArrayList();
        this.playlistBundles = new HashMap();
        this.initialStartTimeUs = -9223372036854775807L;
    }

    private void createBundles(List list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Uri uri = (Uri) list.get(i);
            this.playlistBundles.put(uri, new MediaPlaylistBundle(uri));
        }
    }

    private static HlsMediaPlaylist.Segment getFirstOldOverlappingSegment(HlsMediaPlaylist hlsMediaPlaylist, HlsMediaPlaylist hlsMediaPlaylist2) {
        int i = (int) (hlsMediaPlaylist2.mediaSequence - hlsMediaPlaylist.mediaSequence);
        List list = hlsMediaPlaylist.segments;
        if (i < list.size()) {
            return (HlsMediaPlaylist.Segment) list.get(i);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HlsMediaPlaylist getLatestPlaylistSnapshot(HlsMediaPlaylist hlsMediaPlaylist, HlsMediaPlaylist hlsMediaPlaylist2) {
        return !hlsMediaPlaylist2.isNewerThan(hlsMediaPlaylist) ? hlsMediaPlaylist2.hasEndTag ? hlsMediaPlaylist.copyWithEndTag() : hlsMediaPlaylist : hlsMediaPlaylist2.copyWith(getLoadedPlaylistStartTimeUs(hlsMediaPlaylist, hlsMediaPlaylist2), getLoadedPlaylistDiscontinuitySequence(hlsMediaPlaylist, hlsMediaPlaylist2));
    }

    private int getLoadedPlaylistDiscontinuitySequence(HlsMediaPlaylist hlsMediaPlaylist, HlsMediaPlaylist hlsMediaPlaylist2) {
        HlsMediaPlaylist.Segment firstOldOverlappingSegment;
        if (hlsMediaPlaylist2.hasDiscontinuitySequence) {
            return hlsMediaPlaylist2.discontinuitySequence;
        }
        HlsMediaPlaylist hlsMediaPlaylist3 = this.primaryMediaPlaylistSnapshot;
        int i = hlsMediaPlaylist3 != null ? hlsMediaPlaylist3.discontinuitySequence : 0;
        return (hlsMediaPlaylist == null || (firstOldOverlappingSegment = getFirstOldOverlappingSegment(hlsMediaPlaylist, hlsMediaPlaylist2)) == null) ? i : (hlsMediaPlaylist.discontinuitySequence + firstOldOverlappingSegment.relativeDiscontinuitySequence) - ((HlsMediaPlaylist.Segment) hlsMediaPlaylist2.segments.get(0)).relativeDiscontinuitySequence;
    }

    private long getLoadedPlaylistStartTimeUs(HlsMediaPlaylist hlsMediaPlaylist, HlsMediaPlaylist hlsMediaPlaylist2) {
        if (hlsMediaPlaylist2.hasProgramDateTime) {
            return hlsMediaPlaylist2.startTimeUs;
        }
        HlsMediaPlaylist hlsMediaPlaylist3 = this.primaryMediaPlaylistSnapshot;
        long j = hlsMediaPlaylist3 != null ? hlsMediaPlaylist3.startTimeUs : 0L;
        if (hlsMediaPlaylist == null) {
            return j;
        }
        int size = hlsMediaPlaylist.segments.size();
        HlsMediaPlaylist.Segment firstOldOverlappingSegment = getFirstOldOverlappingSegment(hlsMediaPlaylist, hlsMediaPlaylist2);
        return firstOldOverlappingSegment != null ? hlsMediaPlaylist.startTimeUs + firstOldOverlappingSegment.relativeStartTimeUs : ((long) size) == hlsMediaPlaylist2.mediaSequence - hlsMediaPlaylist.mediaSequence ? hlsMediaPlaylist.getEndTimeUs() : j;
    }

    private Uri getRequestUriForPrimaryChange(Uri uri) {
        HlsMediaPlaylist.RenditionReport renditionReport;
        HlsMediaPlaylist hlsMediaPlaylist = this.primaryMediaPlaylistSnapshot;
        if (hlsMediaPlaylist == null || !hlsMediaPlaylist.serverControl.canBlockReload || (renditionReport = (HlsMediaPlaylist.RenditionReport) hlsMediaPlaylist.renditionReports.get(uri)) == null) {
            return uri;
        }
        Uri.Builder buildUpon = uri.buildUpon();
        buildUpon.appendQueryParameter("_HLS_msn", String.valueOf(renditionReport.lastMediaSequence));
        int i = renditionReport.lastPartIndex;
        if (i != -1) {
            buildUpon.appendQueryParameter("_HLS_part", String.valueOf(i));
        }
        return buildUpon.build();
    }

    private boolean isVariantUrl(Uri uri) {
        List list = this.multivariantPlaylist.variants;
        for (int i = 0; i < list.size(); i++) {
            if (uri.equals(((HlsMultivariantPlaylist.Variant) list.get(i)).url)) {
                return true;
            }
        }
        return false;
    }

    private void maybeActivateForPlayback(Uri uri) {
        MediaPlaylistBundle mediaPlaylistBundle = (MediaPlaylistBundle) this.playlistBundles.get(uri);
        HlsMediaPlaylist playlistSnapshot = mediaPlaylistBundle.getPlaylistSnapshot();
        if (mediaPlaylistBundle.isActiveForPlayback()) {
            return;
        }
        mediaPlaylistBundle.setActiveForPlayback(true);
        if (playlistSnapshot == null || playlistSnapshot.hasEndTag) {
            return;
        }
        mediaPlaylistBundle.loadPlaylist(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean maybeSelectNewPrimaryUrl() {
        List list = this.multivariantPlaylist.variants;
        int size = list.size();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        for (int i = 0; i < size; i++) {
            MediaPlaylistBundle mediaPlaylistBundle = (MediaPlaylistBundle) Assertions.checkNotNull((MediaPlaylistBundle) this.playlistBundles.get(((HlsMultivariantPlaylist.Variant) list.get(i)).url));
            if (elapsedRealtime > mediaPlaylistBundle.excludeUntilMs) {
                Uri uri = mediaPlaylistBundle.playlistUrl;
                this.primaryMediaPlaylistUrl = uri;
                mediaPlaylistBundle.loadPlaylistInternal(getRequestUriForPrimaryChange(uri));
                return true;
            }
        }
        return false;
    }

    private void maybeSetPrimaryUrl(Uri uri) {
        if (uri.equals(this.primaryMediaPlaylistUrl) || !isVariantUrl(uri)) {
            return;
        }
        HlsMediaPlaylist hlsMediaPlaylist = this.primaryMediaPlaylistSnapshot;
        if (hlsMediaPlaylist == null || !hlsMediaPlaylist.hasEndTag) {
            this.primaryMediaPlaylistUrl = uri;
            MediaPlaylistBundle mediaPlaylistBundle = (MediaPlaylistBundle) this.playlistBundles.get(uri);
            HlsMediaPlaylist hlsMediaPlaylist2 = mediaPlaylistBundle.playlistSnapshot;
            if (hlsMediaPlaylist2 == null || !hlsMediaPlaylist2.hasEndTag) {
                mediaPlaylistBundle.loadPlaylistInternal(getRequestUriForPrimaryChange(uri));
            } else {
                this.primaryMediaPlaylistSnapshot = hlsMediaPlaylist2;
                this.primaryPlaylistListener.onPrimaryPlaylistRefreshed(hlsMediaPlaylist2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean notifyPlaylistError(Uri uri, LoadErrorHandlingPolicy.LoadErrorInfo loadErrorInfo, boolean z) {
        Iterator it = this.listeners.iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            z2 |= !((HlsPlaylistTracker.PlaylistEventListener) it.next()).onPlaylistError(uri, loadErrorInfo, z);
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPlaylistUpdated(Uri uri, HlsMediaPlaylist hlsMediaPlaylist) {
        if (uri.equals(this.primaryMediaPlaylistUrl)) {
            if (this.primaryMediaPlaylistSnapshot == null) {
                this.isLive = !hlsMediaPlaylist.hasEndTag;
                this.initialStartTimeUs = hlsMediaPlaylist.startTimeUs;
            }
            this.primaryMediaPlaylistSnapshot = hlsMediaPlaylist;
            this.primaryPlaylistListener.onPrimaryPlaylistRefreshed(hlsMediaPlaylist);
        }
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((HlsPlaylistTracker.PlaylistEventListener) it.next()).onPlaylistChanged();
        }
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public void addListener(HlsPlaylistTracker.PlaylistEventListener playlistEventListener) {
        Assertions.checkNotNull(playlistEventListener);
        this.listeners.add(playlistEventListener);
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public /* synthetic */ void deactivatePlaylistForPlayback(Uri uri) {
        HlsPlaylistTracker.-CC.$default$deactivatePlaylistForPlayback(this, uri);
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public boolean excludeMediaPlaylist(Uri uri, long j) {
        if (((MediaPlaylistBundle) this.playlistBundles.get(uri)) != null) {
            return !r2.excludePlaylist(j);
        }
        return false;
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public long getInitialStartTimeUs() {
        return this.initialStartTimeUs;
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public HlsMultivariantPlaylist getMultivariantPlaylist() {
        return this.multivariantPlaylist;
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public HlsMediaPlaylist getPlaylistSnapshot(Uri uri, boolean z) {
        HlsMediaPlaylist playlistSnapshot = ((MediaPlaylistBundle) this.playlistBundles.get(uri)).getPlaylistSnapshot();
        if (playlistSnapshot != null && z) {
            maybeSetPrimaryUrl(uri);
            maybeActivateForPlayback(uri);
        }
        return playlistSnapshot;
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public boolean isLive() {
        return this.isLive;
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public boolean isSnapshotValid(Uri uri) {
        return ((MediaPlaylistBundle) this.playlistBundles.get(uri)).isSnapshotValid();
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public void maybeThrowPlaylistRefreshError(Uri uri) {
        ((MediaPlaylistBundle) this.playlistBundles.get(uri)).maybeThrowPlaylistRefreshError();
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public void maybeThrowPrimaryPlaylistRefreshError() {
        Loader loader = this.initialPlaylistLoader;
        if (loader != null) {
            loader.maybeThrowError();
        }
        Uri uri = this.primaryMediaPlaylistUrl;
        if (uri != null) {
            maybeThrowPlaylistRefreshError(uri);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public void onLoadCanceled(ParsingLoadable parsingLoadable, long j, long j2, boolean z) {
        LoadEventInfo loadEventInfo = new LoadEventInfo(parsingLoadable.loadTaskId, parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), j, j2, parsingLoadable.bytesLoaded());
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(parsingLoadable.loadTaskId);
        this.eventDispatcher.loadCanceled(loadEventInfo, 4);
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public void onLoadCompleted(ParsingLoadable parsingLoadable, long j, long j2) {
        HlsPlaylist hlsPlaylist = (HlsPlaylist) parsingLoadable.getResult();
        boolean z = hlsPlaylist instanceof HlsMediaPlaylist;
        HlsMultivariantPlaylist createSingleVariantMultivariantPlaylist = z ? HlsMultivariantPlaylist.createSingleVariantMultivariantPlaylist(hlsPlaylist.baseUri) : (HlsMultivariantPlaylist) hlsPlaylist;
        this.multivariantPlaylist = createSingleVariantMultivariantPlaylist;
        this.primaryMediaPlaylistUrl = ((HlsMultivariantPlaylist.Variant) createSingleVariantMultivariantPlaylist.variants.get(0)).url;
        this.listeners.add(new FirstPrimaryMediaPlaylistListener());
        createBundles(createSingleVariantMultivariantPlaylist.mediaPlaylistUrls);
        LoadEventInfo loadEventInfo = new LoadEventInfo(parsingLoadable.loadTaskId, parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), j, j2, parsingLoadable.bytesLoaded());
        MediaPlaylistBundle mediaPlaylistBundle = (MediaPlaylistBundle) this.playlistBundles.get(this.primaryMediaPlaylistUrl);
        if (z) {
            mediaPlaylistBundle.processLoadedPlaylist((HlsMediaPlaylist) hlsPlaylist, loadEventInfo);
        } else {
            mediaPlaylistBundle.loadPlaylist(false);
        }
        this.loadErrorHandlingPolicy.onLoadTaskConcluded(parsingLoadable.loadTaskId);
        this.eventDispatcher.loadCompleted(loadEventInfo, 4);
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public Loader.LoadErrorAction onLoadError(ParsingLoadable parsingLoadable, long j, long j2, IOException iOException, int i) {
        LoadEventInfo loadEventInfo = new LoadEventInfo(parsingLoadable.loadTaskId, parsingLoadable.dataSpec, parsingLoadable.getUri(), parsingLoadable.getResponseHeaders(), j, j2, parsingLoadable.bytesLoaded());
        long retryDelayMsFor = this.loadErrorHandlingPolicy.getRetryDelayMsFor(new LoadErrorHandlingPolicy.LoadErrorInfo(loadEventInfo, new MediaLoadData(parsingLoadable.type), iOException, i));
        boolean z = retryDelayMsFor == -9223372036854775807L;
        this.eventDispatcher.loadError(loadEventInfo, parsingLoadable.type, iOException, z);
        if (z) {
            this.loadErrorHandlingPolicy.onLoadTaskConcluded(parsingLoadable.loadTaskId);
        }
        return z ? Loader.DONT_RETRY_FATAL : Loader.createRetryAction(false, retryDelayMsFor);
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public void refreshPlaylist(Uri uri) {
        ((MediaPlaylistBundle) this.playlistBundles.get(uri)).loadPlaylist(true);
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public void removeListener(HlsPlaylistTracker.PlaylistEventListener playlistEventListener) {
        this.listeners.remove(playlistEventListener);
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public void start(Uri uri, MediaSourceEventListener.EventDispatcher eventDispatcher, HlsPlaylistTracker.PrimaryPlaylistListener primaryPlaylistListener) {
        this.playlistRefreshHandler = Util.createHandlerForCurrentLooper();
        this.eventDispatcher = eventDispatcher;
        this.primaryPlaylistListener = primaryPlaylistListener;
        ParsingLoadable parsingLoadable = new ParsingLoadable(this.dataSourceFactory.createDataSource(4), uri, 4, this.playlistParserFactory.createPlaylistParser());
        Assertions.checkState(this.initialPlaylistLoader == null);
        Loader loader = new Loader("DefaultHlsPlaylistTracker:MultivariantPlaylist");
        this.initialPlaylistLoader = loader;
        eventDispatcher.loadStarted(new LoadEventInfo(parsingLoadable.loadTaskId, parsingLoadable.dataSpec, loader.startLoading(parsingLoadable, this, this.loadErrorHandlingPolicy.getMinimumLoadableRetryCount(parsingLoadable.type))), parsingLoadable.type);
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker
    public void stop() {
        this.primaryMediaPlaylistUrl = null;
        this.primaryMediaPlaylistSnapshot = null;
        this.multivariantPlaylist = null;
        this.initialStartTimeUs = -9223372036854775807L;
        this.initialPlaylistLoader.release();
        this.initialPlaylistLoader = null;
        Iterator it = this.playlistBundles.values().iterator();
        while (it.hasNext()) {
            ((MediaPlaylistBundle) it.next()).release();
        }
        this.playlistRefreshHandler.removeCallbacksAndMessages(null);
        this.playlistRefreshHandler = null;
        this.playlistBundles.clear();
    }
}
