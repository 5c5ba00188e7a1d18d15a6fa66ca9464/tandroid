package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import android.text.TextUtils;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.analytics.PlayerId;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSessionEventListener;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsSampleStreamWrapper;
import com.google.android.exoplayer2.source.hls.playlist.HlsMultivariantPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.trackselection.ExoTrackSelection;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public final class HlsMediaPeriod implements MediaPeriod, HlsPlaylistTracker.PlaylistEventListener {
    private final Allocator allocator;
    private final boolean allowChunklessPreparation;
    private int audioVideoSampleStreamWrapperCount;
    private SequenceableLoader compositeSequenceableLoader;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final HlsDataSourceFactory dataSourceFactory;
    private final DrmSessionEventListener.EventDispatcher drmEventDispatcher;
    private final DrmSessionManager drmSessionManager;
    private final MediaSourceEventListener.EventDispatcher eventDispatcher;
    private final HlsExtractorFactory extractorFactory;
    private final LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private MediaPeriod.Callback mediaPeriodCallback;
    private final TransferListener mediaTransferListener;
    private final int metadataType;
    private int pendingPrepareCount;
    private final PlayerId playerId;
    private final HlsPlaylistTracker playlistTracker;
    private TrackGroupArray trackGroups;
    private final boolean useSessionKeys;
    private final HlsSampleStreamWrapper.Callback sampleStreamWrapperCallback = new SampleStreamWrapperCallback();
    private final IdentityHashMap<SampleStream, Integer> streamWrapperIndices = new IdentityHashMap<>();
    private final TimestampAdjusterProvider timestampAdjusterProvider = new TimestampAdjusterProvider();
    private HlsSampleStreamWrapper[] sampleStreamWrappers = new HlsSampleStreamWrapper[0];
    private HlsSampleStreamWrapper[] enabledSampleStreamWrappers = new HlsSampleStreamWrapper[0];

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long readDiscontinuity() {
        return -9223372036854775807L;
    }

    static /* synthetic */ int access$106(HlsMediaPeriod hlsMediaPeriod) {
        int i = hlsMediaPeriod.pendingPrepareCount - 1;
        hlsMediaPeriod.pendingPrepareCount = i;
        return i;
    }

    public HlsMediaPeriod(HlsExtractorFactory hlsExtractorFactory, HlsPlaylistTracker hlsPlaylistTracker, HlsDataSourceFactory hlsDataSourceFactory, TransferListener transferListener, DrmSessionManager drmSessionManager, DrmSessionEventListener.EventDispatcher eventDispatcher, LoadErrorHandlingPolicy loadErrorHandlingPolicy, MediaSourceEventListener.EventDispatcher eventDispatcher2, Allocator allocator, CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, boolean z, int i, boolean z2, PlayerId playerId) {
        this.extractorFactory = hlsExtractorFactory;
        this.playlistTracker = hlsPlaylistTracker;
        this.dataSourceFactory = hlsDataSourceFactory;
        this.mediaTransferListener = transferListener;
        this.drmSessionManager = drmSessionManager;
        this.drmEventDispatcher = eventDispatcher;
        this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
        this.eventDispatcher = eventDispatcher2;
        this.allocator = allocator;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.allowChunklessPreparation = z;
        this.metadataType = i;
        this.useSessionKeys = z2;
        this.playerId = playerId;
        this.compositeSequenceableLoader = compositeSequenceableLoaderFactory.createCompositeSequenceableLoader(new SequenceableLoader[0]);
    }

    public void release() {
        this.playlistTracker.removeListener(this);
        for (HlsSampleStreamWrapper hlsSampleStreamWrapper : this.sampleStreamWrappers) {
            hlsSampleStreamWrapper.release();
        }
        this.mediaPeriodCallback = null;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void prepare(MediaPeriod.Callback callback, long j) {
        this.mediaPeriodCallback = callback;
        this.playlistTracker.addListener(this);
        buildAndPrepareSampleStreamWrappers(j);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
        for (HlsSampleStreamWrapper hlsSampleStreamWrapper : this.sampleStreamWrappers) {
            hlsSampleStreamWrapper.maybeThrowPrepareError();
        }
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return (TrackGroupArray) Assertions.checkNotNull(this.trackGroups);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long selectTracks(ExoTrackSelection[] exoTrackSelectionArr, boolean[] zArr, SampleStream[] sampleStreamArr, boolean[] zArr2, long j) {
        SampleStream[] sampleStreamArr2 = sampleStreamArr;
        int[] iArr = new int[exoTrackSelectionArr.length];
        int[] iArr2 = new int[exoTrackSelectionArr.length];
        for (int i = 0; i < exoTrackSelectionArr.length; i++) {
            iArr[i] = sampleStreamArr2[i] == null ? -1 : this.streamWrapperIndices.get(sampleStreamArr2[i]).intValue();
            iArr2[i] = -1;
            if (exoTrackSelectionArr[i] != null) {
                TrackGroup trackGroup = exoTrackSelectionArr[i].getTrackGroup();
                int i2 = 0;
                while (true) {
                    HlsSampleStreamWrapper[] hlsSampleStreamWrapperArr = this.sampleStreamWrappers;
                    if (i2 >= hlsSampleStreamWrapperArr.length) {
                        break;
                    } else if (hlsSampleStreamWrapperArr[i2].getTrackGroups().indexOf(trackGroup) != -1) {
                        iArr2[i] = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
            }
        }
        this.streamWrapperIndices.clear();
        int length = exoTrackSelectionArr.length;
        SampleStream[] sampleStreamArr3 = new SampleStream[length];
        SampleStream[] sampleStreamArr4 = new SampleStream[exoTrackSelectionArr.length];
        ExoTrackSelection[] exoTrackSelectionArr2 = new ExoTrackSelection[exoTrackSelectionArr.length];
        HlsSampleStreamWrapper[] hlsSampleStreamWrapperArr2 = new HlsSampleStreamWrapper[this.sampleStreamWrappers.length];
        int i3 = 0;
        int i4 = 0;
        boolean z = false;
        while (i4 < this.sampleStreamWrappers.length) {
            for (int i5 = 0; i5 < exoTrackSelectionArr.length; i5++) {
                ExoTrackSelection exoTrackSelection = null;
                sampleStreamArr4[i5] = iArr[i5] == i4 ? sampleStreamArr2[i5] : null;
                if (iArr2[i5] == i4) {
                    exoTrackSelection = exoTrackSelectionArr[i5];
                }
                exoTrackSelectionArr2[i5] = exoTrackSelection;
            }
            HlsSampleStreamWrapper hlsSampleStreamWrapper = this.sampleStreamWrappers[i4];
            int i6 = i3;
            int i7 = length;
            int i8 = i4;
            ExoTrackSelection[] exoTrackSelectionArr3 = exoTrackSelectionArr2;
            HlsSampleStreamWrapper[] hlsSampleStreamWrapperArr3 = hlsSampleStreamWrapperArr2;
            boolean selectTracks = hlsSampleStreamWrapper.selectTracks(exoTrackSelectionArr2, zArr, sampleStreamArr4, zArr2, j, z);
            int i9 = 0;
            boolean z2 = false;
            while (true) {
                if (i9 >= exoTrackSelectionArr.length) {
                    break;
                }
                SampleStream sampleStream = sampleStreamArr4[i9];
                if (iArr2[i9] == i8) {
                    Assertions.checkNotNull(sampleStream);
                    sampleStreamArr3[i9] = sampleStream;
                    this.streamWrapperIndices.put(sampleStream, Integer.valueOf(i8));
                    z2 = true;
                } else if (iArr[i9] == i8) {
                    Assertions.checkState(sampleStream == null);
                }
                i9++;
            }
            if (z2) {
                hlsSampleStreamWrapperArr3[i6] = hlsSampleStreamWrapper;
                i3 = i6 + 1;
                if (i6 == 0) {
                    hlsSampleStreamWrapper.setIsTimestampMaster(true);
                    if (!selectTracks) {
                        HlsSampleStreamWrapper[] hlsSampleStreamWrapperArr4 = this.enabledSampleStreamWrappers;
                        if (hlsSampleStreamWrapperArr4.length != 0 && hlsSampleStreamWrapper == hlsSampleStreamWrapperArr4[0]) {
                        }
                    }
                    this.timestampAdjusterProvider.reset();
                    z = true;
                } else {
                    hlsSampleStreamWrapper.setIsTimestampMaster(i8 < this.audioVideoSampleStreamWrapperCount);
                }
            } else {
                i3 = i6;
            }
            i4 = i8 + 1;
            hlsSampleStreamWrapperArr2 = hlsSampleStreamWrapperArr3;
            length = i7;
            exoTrackSelectionArr2 = exoTrackSelectionArr3;
            sampleStreamArr2 = sampleStreamArr;
        }
        System.arraycopy(sampleStreamArr3, 0, sampleStreamArr2, 0, length);
        HlsSampleStreamWrapper[] hlsSampleStreamWrapperArr5 = (HlsSampleStreamWrapper[]) Util.nullSafeArrayCopy(hlsSampleStreamWrapperArr2, i3);
        this.enabledSampleStreamWrappers = hlsSampleStreamWrapperArr5;
        this.compositeSequenceableLoader = this.compositeSequenceableLoaderFactory.createCompositeSequenceableLoader(hlsSampleStreamWrapperArr5);
        return j;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void discardBuffer(long j, boolean z) {
        for (HlsSampleStreamWrapper hlsSampleStreamWrapper : this.enabledSampleStreamWrappers) {
            hlsSampleStreamWrapper.discardBuffer(j, z);
        }
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public void reevaluateBuffer(long j) {
        this.compositeSequenceableLoader.reevaluateBuffer(j);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public boolean continueLoading(long j) {
        if (this.trackGroups == null) {
            for (HlsSampleStreamWrapper hlsSampleStreamWrapper : this.sampleStreamWrappers) {
                hlsSampleStreamWrapper.continuePreparing();
            }
            return false;
        }
        return this.compositeSequenceableLoader.continueLoading(j);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public boolean isLoading() {
        return this.compositeSequenceableLoader.isLoading();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        return this.compositeSequenceableLoader.getNextLoadPositionUs();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public long getBufferedPositionUs() {
        return this.compositeSequenceableLoader.getBufferedPositionUs();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long seekToUs(long j) {
        HlsSampleStreamWrapper[] hlsSampleStreamWrapperArr = this.enabledSampleStreamWrappers;
        if (hlsSampleStreamWrapperArr.length > 0) {
            boolean seekToUs = hlsSampleStreamWrapperArr[0].seekToUs(j, false);
            int i = 1;
            while (true) {
                HlsSampleStreamWrapper[] hlsSampleStreamWrapperArr2 = this.enabledSampleStreamWrappers;
                if (i >= hlsSampleStreamWrapperArr2.length) {
                    break;
                }
                hlsSampleStreamWrapperArr2[i].seekToUs(j, seekToUs);
                i++;
            }
            if (seekToUs) {
                this.timestampAdjusterProvider.reset();
            }
        }
        return j;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long getAdjustedSeekPositionUs(long j, SeekParameters seekParameters) {
        HlsSampleStreamWrapper[] hlsSampleStreamWrapperArr;
        for (HlsSampleStreamWrapper hlsSampleStreamWrapper : this.enabledSampleStreamWrappers) {
            if (hlsSampleStreamWrapper.isVideoSampleStream()) {
                return hlsSampleStreamWrapper.getAdjustedSeekPositionUs(j, seekParameters);
            }
        }
        return j;
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.PlaylistEventListener
    public void onPlaylistChanged() {
        for (HlsSampleStreamWrapper hlsSampleStreamWrapper : this.sampleStreamWrappers) {
            hlsSampleStreamWrapper.onPlaylistUpdated();
        }
        this.mediaPeriodCallback.onContinueLoadingRequested(this);
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.PlaylistEventListener
    public boolean onPlaylistError(Uri uri, LoadErrorHandlingPolicy.LoadErrorInfo loadErrorInfo, boolean z) {
        boolean z2 = true;
        for (HlsSampleStreamWrapper hlsSampleStreamWrapper : this.sampleStreamWrappers) {
            z2 &= hlsSampleStreamWrapper.onPlaylistError(uri, loadErrorInfo, z);
        }
        this.mediaPeriodCallback.onContinueLoadingRequested(this);
        return z2;
    }

    private void buildAndPrepareSampleStreamWrappers(long j) {
        Map<String, DrmInitData> emptyMap;
        HlsMultivariantPlaylist hlsMultivariantPlaylist = (HlsMultivariantPlaylist) Assertions.checkNotNull(this.playlistTracker.getMultivariantPlaylist());
        if (this.useSessionKeys) {
            emptyMap = deriveOverridingDrmInitData(hlsMultivariantPlaylist.sessionKeyDrmInitData);
        } else {
            emptyMap = Collections.emptyMap();
        }
        Map<String, DrmInitData> map = emptyMap;
        boolean z = !hlsMultivariantPlaylist.variants.isEmpty();
        List<HlsMultivariantPlaylist.Rendition> list = hlsMultivariantPlaylist.audios;
        List<HlsMultivariantPlaylist.Rendition> list2 = hlsMultivariantPlaylist.subtitles;
        this.pendingPrepareCount = 0;
        ArrayList arrayList = new ArrayList();
        List<int[]> arrayList2 = new ArrayList<>();
        if (z) {
            buildAndPrepareMainSampleStreamWrapper(hlsMultivariantPlaylist, j, arrayList, arrayList2, map);
        }
        buildAndPrepareAudioSampleStreamWrappers(j, list, arrayList, arrayList2, map);
        this.audioVideoSampleStreamWrapperCount = arrayList.size();
        int i = 0;
        while (i < list2.size()) {
            HlsMultivariantPlaylist.Rendition rendition = list2.get(i);
            String str = "subtitle:" + i + ":" + rendition.name;
            ArrayList arrayList3 = arrayList2;
            int i2 = i;
            HlsSampleStreamWrapper buildSampleStreamWrapper = buildSampleStreamWrapper(str, 3, new Uri[]{rendition.url}, new Format[]{rendition.format}, null, Collections.emptyList(), map, j);
            arrayList3.add(new int[]{i2});
            arrayList.add(buildSampleStreamWrapper);
            buildSampleStreamWrapper.prepareWithMultivariantPlaylistInfo(new TrackGroup[]{new TrackGroup(str, rendition.format)}, 0, new int[0]);
            i = i2 + 1;
            arrayList2 = arrayList3;
        }
        this.sampleStreamWrappers = (HlsSampleStreamWrapper[]) arrayList.toArray(new HlsSampleStreamWrapper[0]);
        int[][] iArr = (int[][]) arrayList2.toArray(new int[0]);
        this.pendingPrepareCount = this.sampleStreamWrappers.length;
        for (int i3 = 0; i3 < this.audioVideoSampleStreamWrapperCount; i3++) {
            this.sampleStreamWrappers[i3].setIsTimestampMaster(true);
        }
        for (HlsSampleStreamWrapper hlsSampleStreamWrapper : this.sampleStreamWrappers) {
            hlsSampleStreamWrapper.continuePreparing();
        }
        this.enabledSampleStreamWrappers = this.sampleStreamWrappers;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0064  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void buildAndPrepareMainSampleStreamWrapper(HlsMultivariantPlaylist hlsMultivariantPlaylist, long j, List<HlsSampleStreamWrapper> list, List<int[]> list2, Map<String, DrmInitData> map) {
        boolean z;
        boolean z2;
        int i;
        int size = hlsMultivariantPlaylist.variants.size();
        int[] iArr = new int[size];
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < hlsMultivariantPlaylist.variants.size(); i4++) {
            Format format = hlsMultivariantPlaylist.variants.get(i4).format;
            if (format.height > 0 || Util.getCodecsOfType(format.codecs, 2) != null) {
                iArr[i4] = 2;
                i2++;
            } else if (Util.getCodecsOfType(format.codecs, 1) != null) {
                iArr[i4] = 1;
                i3++;
            } else {
                iArr[i4] = -1;
            }
        }
        if (i2 > 0) {
            size = i2;
            z = true;
        } else if (i3 < size) {
            size -= i3;
            z = false;
            z2 = true;
            Uri[] uriArr = new Uri[size];
            Format[] formatArr = new Format[size];
            int[] iArr2 = new int[size];
            int i5 = 0;
            for (i = 0; i < hlsMultivariantPlaylist.variants.size(); i++) {
                if ((!z || iArr[i] == 2) && (!z2 || iArr[i] != 1)) {
                    HlsMultivariantPlaylist.Variant variant = hlsMultivariantPlaylist.variants.get(i);
                    uriArr[i5] = variant.url;
                    formatArr[i5] = variant.format;
                    iArr2[i5] = i;
                    i5++;
                }
            }
            String str = formatArr[0].codecs;
            int codecCountOfType = Util.getCodecCountOfType(str, 2);
            int codecCountOfType2 = Util.getCodecCountOfType(str, 1);
            boolean z3 = (codecCountOfType2 != 1 || (codecCountOfType2 == 0 && hlsMultivariantPlaylist.audios.isEmpty())) && codecCountOfType <= 1 && codecCountOfType2 + codecCountOfType > 0;
            HlsSampleStreamWrapper buildSampleStreamWrapper = buildSampleStreamWrapper("main", (!z || codecCountOfType2 <= 0) ? 0 : 1, uriArr, formatArr, hlsMultivariantPlaylist.muxedAudioFormat, hlsMultivariantPlaylist.muxedCaptionFormats, map, j);
            list.add(buildSampleStreamWrapper);
            list2.add(iArr2);
            if (this.allowChunklessPreparation || !z3) {
            }
            ArrayList arrayList = new ArrayList();
            if (codecCountOfType > 0) {
                Format[] formatArr2 = new Format[size];
                for (int i6 = 0; i6 < size; i6++) {
                    formatArr2[i6] = deriveVideoFormat(formatArr[i6]);
                }
                arrayList.add(new TrackGroup("main", formatArr2));
                if (codecCountOfType2 > 0 && (hlsMultivariantPlaylist.muxedAudioFormat != null || hlsMultivariantPlaylist.audios.isEmpty())) {
                    arrayList.add(new TrackGroup("main:audio", deriveAudioFormat(formatArr[0], hlsMultivariantPlaylist.muxedAudioFormat, false)));
                }
                List<Format> list3 = hlsMultivariantPlaylist.muxedCaptionFormats;
                if (list3 != null) {
                    for (int i7 = 0; i7 < list3.size(); i7++) {
                        arrayList.add(new TrackGroup("main:cc:" + i7, list3.get(i7)));
                    }
                }
            } else {
                Format[] formatArr3 = new Format[size];
                for (int i8 = 0; i8 < size; i8++) {
                    formatArr3[i8] = deriveAudioFormat(formatArr[i8], hlsMultivariantPlaylist.muxedAudioFormat, true);
                }
                arrayList.add(new TrackGroup("main", formatArr3));
            }
            TrackGroup trackGroup = new TrackGroup("main:id3", new Format.Builder().setId("ID3").setSampleMimeType("application/id3").build());
            arrayList.add(trackGroup);
            buildSampleStreamWrapper.prepareWithMultivariantPlaylistInfo((TrackGroup[]) arrayList.toArray(new TrackGroup[0]), 0, arrayList.indexOf(trackGroup));
            return;
        } else {
            z = false;
        }
        z2 = false;
        Uri[] uriArr2 = new Uri[size];
        Format[] formatArr4 = new Format[size];
        int[] iArr22 = new int[size];
        int i52 = 0;
        while (i < hlsMultivariantPlaylist.variants.size()) {
        }
        String str2 = formatArr4[0].codecs;
        int codecCountOfType3 = Util.getCodecCountOfType(str2, 2);
        int codecCountOfType22 = Util.getCodecCountOfType(str2, 1);
        if (codecCountOfType22 != 1) {
        }
        HlsSampleStreamWrapper buildSampleStreamWrapper2 = buildSampleStreamWrapper("main", (!z || codecCountOfType22 <= 0) ? 0 : 1, uriArr2, formatArr4, hlsMultivariantPlaylist.muxedAudioFormat, hlsMultivariantPlaylist.muxedCaptionFormats, map, j);
        list.add(buildSampleStreamWrapper2);
        list2.add(iArr22);
        if (this.allowChunklessPreparation) {
        }
    }

    private void buildAndPrepareAudioSampleStreamWrappers(long j, List<HlsMultivariantPlaylist.Rendition> list, List<HlsSampleStreamWrapper> list2, List<int[]> list3, Map<String, DrmInitData> map) {
        ArrayList arrayList = new ArrayList(list.size());
        ArrayList arrayList2 = new ArrayList(list.size());
        ArrayList arrayList3 = new ArrayList(list.size());
        HashSet hashSet = new HashSet();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i).name;
            if (hashSet.add(str)) {
                arrayList.clear();
                arrayList2.clear();
                arrayList3.clear();
                boolean z = true;
                for (int i2 = 0; i2 < list.size(); i2++) {
                    if (Util.areEqual(str, list.get(i2).name)) {
                        HlsMultivariantPlaylist.Rendition rendition = list.get(i2);
                        arrayList3.add(Integer.valueOf(i2));
                        arrayList.add(rendition.url);
                        arrayList2.add(rendition.format);
                        z &= Util.getCodecCountOfType(rendition.format.codecs, 1) == 1;
                    }
                }
                String str2 = "audio:" + str;
                HlsSampleStreamWrapper buildSampleStreamWrapper = buildSampleStreamWrapper(str2, 1, (Uri[]) arrayList.toArray((Uri[]) Util.castNonNullTypeArray(new Uri[0])), (Format[]) arrayList2.toArray(new Format[0]), null, Collections.emptyList(), map, j);
                list3.add(Ints.toArray(arrayList3));
                list2.add(buildSampleStreamWrapper);
                if (this.allowChunklessPreparation && z) {
                    buildSampleStreamWrapper.prepareWithMultivariantPlaylistInfo(new TrackGroup[]{new TrackGroup(str2, (Format[]) arrayList2.toArray(new Format[0]))}, 0, new int[0]);
                }
            }
        }
    }

    private HlsSampleStreamWrapper buildSampleStreamWrapper(String str, int i, Uri[] uriArr, Format[] formatArr, Format format, List<Format> list, Map<String, DrmInitData> map, long j) {
        return new HlsSampleStreamWrapper(str, i, this.sampleStreamWrapperCallback, new HlsChunkSource(this.extractorFactory, this.playlistTracker, uriArr, formatArr, this.dataSourceFactory, this.mediaTransferListener, this.timestampAdjusterProvider, list, this.playerId), map, this.allocator, j, format, this.drmSessionManager, this.drmEventDispatcher, this.loadErrorHandlingPolicy, this.eventDispatcher, this.metadataType);
    }

    private static Map<String, DrmInitData> deriveOverridingDrmInitData(List<DrmInitData> list) {
        ArrayList arrayList = new ArrayList(list);
        HashMap hashMap = new HashMap();
        int i = 0;
        while (i < arrayList.size()) {
            DrmInitData drmInitData = list.get(i);
            String str = drmInitData.schemeType;
            i++;
            int i2 = i;
            while (i2 < arrayList.size()) {
                DrmInitData drmInitData2 = (DrmInitData) arrayList.get(i2);
                if (TextUtils.equals(drmInitData2.schemeType, str)) {
                    drmInitData = drmInitData.merge(drmInitData2);
                    arrayList.remove(i2);
                } else {
                    i2++;
                }
            }
            hashMap.put(str, drmInitData);
        }
        return hashMap;
    }

    private static Format deriveVideoFormat(Format format) {
        String codecsOfType = Util.getCodecsOfType(format.codecs, 2);
        return new Format.Builder().setId(format.id).setLabel(format.label).setContainerMimeType(format.containerMimeType).setSampleMimeType(MimeTypes.getMediaMimeType(codecsOfType)).setCodecs(codecsOfType).setMetadata(format.metadata).setAverageBitrate(format.averageBitrate).setPeakBitrate(format.peakBitrate).setWidth(format.width).setHeight(format.height).setFrameRate(format.frameRate).setSelectionFlags(format.selectionFlags).setRoleFlags(format.roleFlags).build();
    }

    private static Format deriveAudioFormat(Format format, Format format2, boolean z) {
        String str;
        Metadata metadata;
        int i;
        int i2;
        int i3;
        String str2;
        String str3;
        if (format2 != null) {
            str2 = format2.codecs;
            metadata = format2.metadata;
            int i4 = format2.channelCount;
            i2 = format2.selectionFlags;
            int i5 = format2.roleFlags;
            String str4 = format2.language;
            str3 = format2.label;
            i3 = i4;
            i = i5;
            str = str4;
        } else {
            String codecsOfType = Util.getCodecsOfType(format.codecs, 1);
            Metadata metadata2 = format.metadata;
            if (z) {
                int i6 = format.channelCount;
                int i7 = format.selectionFlags;
                int i8 = format.roleFlags;
                str = format.language;
                str2 = codecsOfType;
                str3 = format.label;
                i3 = i6;
                i2 = i7;
                metadata = metadata2;
                i = i8;
            } else {
                str = null;
                metadata = metadata2;
                i = 0;
                i2 = 0;
                i3 = -1;
                str2 = codecsOfType;
                str3 = null;
            }
        }
        return new Format.Builder().setId(format.id).setLabel(str3).setContainerMimeType(format.containerMimeType).setSampleMimeType(MimeTypes.getMediaMimeType(str2)).setCodecs(str2).setMetadata(metadata).setAverageBitrate(z ? format.averageBitrate : -1).setPeakBitrate(z ? format.peakBitrate : -1).setChannelCount(i3).setSelectionFlags(i2).setRoleFlags(i).setLanguage(str).build();
    }

    /* loaded from: classes.dex */
    private class SampleStreamWrapperCallback implements HlsSampleStreamWrapper.Callback {
        private SampleStreamWrapperCallback() {
        }

        @Override // com.google.android.exoplayer2.source.hls.HlsSampleStreamWrapper.Callback
        public void onPrepared() {
            HlsSampleStreamWrapper[] hlsSampleStreamWrapperArr;
            if (HlsMediaPeriod.access$106(HlsMediaPeriod.this) > 0) {
                return;
            }
            int i = 0;
            for (HlsSampleStreamWrapper hlsSampleStreamWrapper : HlsMediaPeriod.this.sampleStreamWrappers) {
                i += hlsSampleStreamWrapper.getTrackGroups().length;
            }
            TrackGroup[] trackGroupArr = new TrackGroup[i];
            int i2 = 0;
            for (HlsSampleStreamWrapper hlsSampleStreamWrapper2 : HlsMediaPeriod.this.sampleStreamWrappers) {
                int i3 = hlsSampleStreamWrapper2.getTrackGroups().length;
                int i4 = 0;
                while (i4 < i3) {
                    trackGroupArr[i2] = hlsSampleStreamWrapper2.getTrackGroups().get(i4);
                    i4++;
                    i2++;
                }
            }
            HlsMediaPeriod.this.trackGroups = new TrackGroupArray(trackGroupArr);
            HlsMediaPeriod.this.mediaPeriodCallback.onPrepared(HlsMediaPeriod.this);
        }

        @Override // com.google.android.exoplayer2.source.hls.HlsSampleStreamWrapper.Callback
        public void onPlaylistRefreshRequired(Uri uri) {
            HlsMediaPeriod.this.playlistTracker.refreshPlaylist(uri);
        }

        @Override // com.google.android.exoplayer2.source.SequenceableLoader.Callback
        public void onContinueLoadingRequested(HlsSampleStreamWrapper hlsSampleStreamWrapper) {
            HlsMediaPeriod.this.mediaPeriodCallback.onContinueLoadingRequested(HlsMediaPeriod.this);
        }
    }
}
