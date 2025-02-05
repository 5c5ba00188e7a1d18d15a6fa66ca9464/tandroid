package com.google.android.exoplayer2.source;

import android.content.Context;
import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.drm.DrmSessionManagerProvider;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource$Factory;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.text.SubtitleDecoderFactory;
import com.google.android.exoplayer2.text.SubtitleExtractor;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.telegram.tgnet.ConnectionsManager;

/* loaded from: classes.dex */
public final class DefaultMediaSourceFactory implements MediaSource.Factory {
    private DataSource.Factory dataSourceFactory;
    private final DelegateFactoryLoader delegateFactoryLoader;
    private long liveMaxOffsetMs;
    private float liveMaxSpeed;
    private long liveMinOffsetMs;
    private float liveMinSpeed;
    private long liveTargetOffsetMs;
    private LoadErrorHandlingPolicy loadErrorHandlingPolicy;
    private MediaSource.Factory serverSideAdInsertionMediaSourceFactory;
    private boolean useProgressiveMediaSourceForSubtitles;

    /* JADX INFO: Access modifiers changed from: private */
    static final class DelegateFactoryLoader {
        private DataSource.Factory dataSourceFactory;
        private DrmSessionManagerProvider drmSessionManagerProvider;
        private final ExtractorsFactory extractorsFactory;
        private LoadErrorHandlingPolicy loadErrorHandlingPolicy;
        private final Map mediaSourceFactorySuppliers = new HashMap();
        private final Set supportedTypes = new HashSet();
        private final Map mediaSourceFactories = new HashMap();

        public DelegateFactoryLoader(ExtractorsFactory extractorsFactory) {
            this.extractorsFactory = extractorsFactory;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ MediaSource.Factory lambda$maybeLoadSupplier$4(DataSource.Factory factory) {
            return new ProgressiveMediaSource.Factory(factory, this.extractorsFactory);
        }

        /* JADX WARN: Removed duplicated region for block: B:20:0x0075  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private Supplier maybeLoadSupplier(int i) {
            Supplier supplier;
            Supplier supplier2;
            if (this.mediaSourceFactorySuppliers.containsKey(Integer.valueOf(i))) {
                return (Supplier) this.mediaSourceFactorySuppliers.get(Integer.valueOf(i));
            }
            final DataSource.Factory factory = (DataSource.Factory) Assertions.checkNotNull(this.dataSourceFactory);
            if (i == 0) {
                final Class asSubclass = DashMediaSource.Factory.class.asSubclass(MediaSource.Factory.class);
                supplier2 = new Supplier() { // from class: com.google.android.exoplayer2.source.DefaultMediaSourceFactory$DelegateFactoryLoader$$ExternalSyntheticLambda4
                    @Override // com.google.common.base.Supplier
                    public final Object get() {
                        MediaSource.Factory access$100;
                        access$100 = DefaultMediaSourceFactory.access$100(asSubclass, factory);
                        return access$100;
                    }
                };
            } else if (i == 1) {
                final Class asSubclass2 = SsMediaSource.Factory.class.asSubclass(MediaSource.Factory.class);
                supplier2 = new Supplier() { // from class: com.google.android.exoplayer2.source.DefaultMediaSourceFactory$DelegateFactoryLoader$$ExternalSyntheticLambda3
                    @Override // com.google.common.base.Supplier
                    public final Object get() {
                        MediaSource.Factory access$100;
                        access$100 = DefaultMediaSourceFactory.access$100(asSubclass2, factory);
                        return access$100;
                    }
                };
            } else {
                if (i != 2) {
                    if (i != 3) {
                        if (i == 4) {
                            supplier = new Supplier() { // from class: com.google.android.exoplayer2.source.DefaultMediaSourceFactory$DelegateFactoryLoader$$ExternalSyntheticLambda0
                                @Override // com.google.common.base.Supplier
                                public final Object get() {
                                    MediaSource.Factory lambda$maybeLoadSupplier$4;
                                    lambda$maybeLoadSupplier$4 = DefaultMediaSourceFactory.DelegateFactoryLoader.this.lambda$maybeLoadSupplier$4(factory);
                                    return lambda$maybeLoadSupplier$4;
                                }
                            };
                        }
                        supplier = null;
                    } else {
                        final Class asSubclass3 = RtspMediaSource$Factory.class.asSubclass(MediaSource.Factory.class);
                        supplier = new Supplier() { // from class: com.google.android.exoplayer2.source.DefaultMediaSourceFactory$DelegateFactoryLoader$$ExternalSyntheticLambda1
                            @Override // com.google.common.base.Supplier
                            public final Object get() {
                                MediaSource.Factory access$000;
                                access$000 = DefaultMediaSourceFactory.access$000(asSubclass3);
                                return access$000;
                            }
                        };
                    }
                    this.mediaSourceFactorySuppliers.put(Integer.valueOf(i), supplier);
                    if (supplier != null) {
                        this.supportedTypes.add(Integer.valueOf(i));
                    }
                    return supplier;
                }
                final Class asSubclass4 = HlsMediaSource.Factory.class.asSubclass(MediaSource.Factory.class);
                supplier2 = new Supplier() { // from class: com.google.android.exoplayer2.source.DefaultMediaSourceFactory$DelegateFactoryLoader$$ExternalSyntheticLambda2
                    @Override // com.google.common.base.Supplier
                    public final Object get() {
                        MediaSource.Factory access$100;
                        access$100 = DefaultMediaSourceFactory.access$100(asSubclass4, factory);
                        return access$100;
                    }
                };
            }
            supplier = supplier2;
            this.mediaSourceFactorySuppliers.put(Integer.valueOf(i), supplier);
            if (supplier != null) {
            }
            return supplier;
        }

        public MediaSource.Factory getMediaSourceFactory(int i) {
            MediaSource.Factory factory = (MediaSource.Factory) this.mediaSourceFactories.get(Integer.valueOf(i));
            if (factory != null) {
                return factory;
            }
            Supplier maybeLoadSupplier = maybeLoadSupplier(i);
            if (maybeLoadSupplier == null) {
                return null;
            }
            MediaSource.Factory factory2 = (MediaSource.Factory) maybeLoadSupplier.get();
            DrmSessionManagerProvider drmSessionManagerProvider = this.drmSessionManagerProvider;
            if (drmSessionManagerProvider != null) {
                factory2.setDrmSessionManagerProvider(drmSessionManagerProvider);
            }
            LoadErrorHandlingPolicy loadErrorHandlingPolicy = this.loadErrorHandlingPolicy;
            if (loadErrorHandlingPolicy != null) {
                factory2.setLoadErrorHandlingPolicy(loadErrorHandlingPolicy);
            }
            this.mediaSourceFactories.put(Integer.valueOf(i), factory2);
            return factory2;
        }

        public void setDataSourceFactory(DataSource.Factory factory) {
            if (factory != this.dataSourceFactory) {
                this.dataSourceFactory = factory;
                this.mediaSourceFactorySuppliers.clear();
                this.mediaSourceFactories.clear();
            }
        }

        public void setDrmSessionManagerProvider(DrmSessionManagerProvider drmSessionManagerProvider) {
            this.drmSessionManagerProvider = drmSessionManagerProvider;
            Iterator it = this.mediaSourceFactories.values().iterator();
            while (it.hasNext()) {
                ((MediaSource.Factory) it.next()).setDrmSessionManagerProvider(drmSessionManagerProvider);
            }
        }

        public void setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy loadErrorHandlingPolicy) {
            this.loadErrorHandlingPolicy = loadErrorHandlingPolicy;
            Iterator it = this.mediaSourceFactories.values().iterator();
            while (it.hasNext()) {
                ((MediaSource.Factory) it.next()).setLoadErrorHandlingPolicy(loadErrorHandlingPolicy);
            }
        }
    }

    private static final class UnknownSubtitlesExtractor implements Extractor {
        private final Format format;

        public UnknownSubtitlesExtractor(Format format) {
            this.format = format;
        }

        @Override // com.google.android.exoplayer2.extractor.Extractor
        public void init(ExtractorOutput extractorOutput) {
            TrackOutput track = extractorOutput.track(0, 3);
            extractorOutput.seekMap(new SeekMap.Unseekable(-9223372036854775807L));
            extractorOutput.endTracks();
            track.format(this.format.buildUpon().setSampleMimeType("text/x-unknown").setCodecs(this.format.sampleMimeType).build());
        }

        @Override // com.google.android.exoplayer2.extractor.Extractor
        public int read(ExtractorInput extractorInput, PositionHolder positionHolder) {
            return extractorInput.skip(ConnectionsManager.DEFAULT_DATACENTER_ID) == -1 ? -1 : 0;
        }

        @Override // com.google.android.exoplayer2.extractor.Extractor
        public void release() {
        }

        @Override // com.google.android.exoplayer2.extractor.Extractor
        public void seek(long j, long j2) {
        }

        @Override // com.google.android.exoplayer2.extractor.Extractor
        public boolean sniff(ExtractorInput extractorInput) {
            return true;
        }
    }

    public DefaultMediaSourceFactory(Context context, ExtractorsFactory extractorsFactory) {
        this(new DefaultDataSource.Factory(context), extractorsFactory);
    }

    public DefaultMediaSourceFactory(DataSource.Factory factory, ExtractorsFactory extractorsFactory) {
        this.dataSourceFactory = factory;
        DelegateFactoryLoader delegateFactoryLoader = new DelegateFactoryLoader(extractorsFactory);
        this.delegateFactoryLoader = delegateFactoryLoader;
        delegateFactoryLoader.setDataSourceFactory(factory);
        this.liveTargetOffsetMs = -9223372036854775807L;
        this.liveMinOffsetMs = -9223372036854775807L;
        this.liveMaxOffsetMs = -9223372036854775807L;
        this.liveMinSpeed = -3.4028235E38f;
        this.liveMaxSpeed = -3.4028235E38f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ MediaSource.Factory access$000(Class cls) {
        return newInstance(cls);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ MediaSource.Factory access$100(Class cls, DataSource.Factory factory) {
        return newInstance(cls, factory);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Extractor[] lambda$createMediaSource$0(Format format) {
        SubtitleDecoderFactory subtitleDecoderFactory = SubtitleDecoderFactory.DEFAULT;
        return new Extractor[]{subtitleDecoderFactory.supportsFormat(format) ? new SubtitleExtractor(subtitleDecoderFactory.createDecoder(format), format) : new UnknownSubtitlesExtractor(format)};
    }

    private static MediaSource maybeClipMediaSource(MediaItem mediaItem, MediaSource mediaSource) {
        MediaItem.ClippingConfiguration clippingConfiguration = mediaItem.clippingConfiguration;
        if (clippingConfiguration.startPositionMs == 0 && clippingConfiguration.endPositionMs == Long.MIN_VALUE && !clippingConfiguration.relativeToDefaultPosition) {
            return mediaSource;
        }
        long msToUs = Util.msToUs(mediaItem.clippingConfiguration.startPositionMs);
        long msToUs2 = Util.msToUs(mediaItem.clippingConfiguration.endPositionMs);
        MediaItem.ClippingConfiguration clippingConfiguration2 = mediaItem.clippingConfiguration;
        return new ClippingMediaSource(mediaSource, msToUs, msToUs2, !clippingConfiguration2.startsAtKeyFrame, clippingConfiguration2.relativeToLiveWindow, clippingConfiguration2.relativeToDefaultPosition);
    }

    private MediaSource maybeWrapWithAdsMediaSource(MediaItem mediaItem, MediaSource mediaSource) {
        Assertions.checkNotNull(mediaItem.localConfiguration);
        mediaItem.localConfiguration.getClass();
        return mediaSource;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static MediaSource.Factory newInstance(Class cls) {
        try {
            return (MediaSource.Factory) cls.getConstructor(null).newInstance(null);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static MediaSource.Factory newInstance(Class cls, DataSource.Factory factory) {
        try {
            return (MediaSource.Factory) cls.getConstructor(DataSource.Factory.class).newInstance(factory);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override // com.google.android.exoplayer2.source.MediaSource.Factory
    public MediaSource createMediaSource(MediaItem mediaItem) {
        Assertions.checkNotNull(mediaItem.localConfiguration);
        String scheme = mediaItem.localConfiguration.uri.getScheme();
        if (scheme != null && scheme.equals("ssai")) {
            return ((MediaSource.Factory) Assertions.checkNotNull(this.serverSideAdInsertionMediaSourceFactory)).createMediaSource(mediaItem);
        }
        MediaItem.LocalConfiguration localConfiguration = mediaItem.localConfiguration;
        int inferContentTypeForUriAndMimeType = Util.inferContentTypeForUriAndMimeType(localConfiguration.uri, localConfiguration.mimeType);
        MediaSource.Factory mediaSourceFactory = this.delegateFactoryLoader.getMediaSourceFactory(inferContentTypeForUriAndMimeType);
        Assertions.checkStateNotNull(mediaSourceFactory, "No suitable media source factory found for content type: " + inferContentTypeForUriAndMimeType);
        MediaItem.LiveConfiguration.Builder buildUpon = mediaItem.liveConfiguration.buildUpon();
        if (mediaItem.liveConfiguration.targetOffsetMs == -9223372036854775807L) {
            buildUpon.setTargetOffsetMs(this.liveTargetOffsetMs);
        }
        if (mediaItem.liveConfiguration.minPlaybackSpeed == -3.4028235E38f) {
            buildUpon.setMinPlaybackSpeed(this.liveMinSpeed);
        }
        if (mediaItem.liveConfiguration.maxPlaybackSpeed == -3.4028235E38f) {
            buildUpon.setMaxPlaybackSpeed(this.liveMaxSpeed);
        }
        if (mediaItem.liveConfiguration.minOffsetMs == -9223372036854775807L) {
            buildUpon.setMinOffsetMs(this.liveMinOffsetMs);
        }
        if (mediaItem.liveConfiguration.maxOffsetMs == -9223372036854775807L) {
            buildUpon.setMaxOffsetMs(this.liveMaxOffsetMs);
        }
        MediaItem.LiveConfiguration build = buildUpon.build();
        if (!build.equals(mediaItem.liveConfiguration)) {
            mediaItem = mediaItem.buildUpon().setLiveConfiguration(build).build();
        }
        MediaSource createMediaSource = mediaSourceFactory.createMediaSource(mediaItem);
        ImmutableList immutableList = ((MediaItem.LocalConfiguration) Util.castNonNull(mediaItem.localConfiguration)).subtitleConfigurations;
        if (!immutableList.isEmpty()) {
            MediaSource[] mediaSourceArr = new MediaSource[immutableList.size() + 1];
            mediaSourceArr[0] = createMediaSource;
            for (int i = 0; i < immutableList.size(); i++) {
                if (this.useProgressiveMediaSourceForSubtitles) {
                    final Format build2 = new Format.Builder().setSampleMimeType(((MediaItem.SubtitleConfiguration) immutableList.get(i)).mimeType).setLanguage(((MediaItem.SubtitleConfiguration) immutableList.get(i)).language).setSelectionFlags(((MediaItem.SubtitleConfiguration) immutableList.get(i)).selectionFlags).setRoleFlags(((MediaItem.SubtitleConfiguration) immutableList.get(i)).roleFlags).setLabel(((MediaItem.SubtitleConfiguration) immutableList.get(i)).label).setId(((MediaItem.SubtitleConfiguration) immutableList.get(i)).id).build();
                    ProgressiveMediaSource.Factory factory = new ProgressiveMediaSource.Factory(this.dataSourceFactory, new ExtractorsFactory() { // from class: com.google.android.exoplayer2.source.DefaultMediaSourceFactory$$ExternalSyntheticLambda0
                        @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
                        public final Extractor[] createExtractors() {
                            Extractor[] lambda$createMediaSource$0;
                            lambda$createMediaSource$0 = DefaultMediaSourceFactory.lambda$createMediaSource$0(Format.this);
                            return lambda$createMediaSource$0;
                        }

                        @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
                        public /* synthetic */ Extractor[] createExtractors(Uri uri, Map map) {
                            Extractor[] createExtractors;
                            createExtractors = createExtractors();
                            return createExtractors;
                        }
                    });
                    LoadErrorHandlingPolicy loadErrorHandlingPolicy = this.loadErrorHandlingPolicy;
                    if (loadErrorHandlingPolicy != null) {
                        factory.setLoadErrorHandlingPolicy(loadErrorHandlingPolicy);
                    }
                    mediaSourceArr[i + 1] = factory.createMediaSource(MediaItem.fromUri(((MediaItem.SubtitleConfiguration) immutableList.get(i)).uri.toString()));
                } else {
                    SingleSampleMediaSource.Factory factory2 = new SingleSampleMediaSource.Factory(this.dataSourceFactory);
                    LoadErrorHandlingPolicy loadErrorHandlingPolicy2 = this.loadErrorHandlingPolicy;
                    if (loadErrorHandlingPolicy2 != null) {
                        factory2.setLoadErrorHandlingPolicy(loadErrorHandlingPolicy2);
                    }
                    mediaSourceArr[i + 1] = factory2.createMediaSource((MediaItem.SubtitleConfiguration) immutableList.get(i), -9223372036854775807L);
                }
            }
            createMediaSource = new MergingMediaSource(mediaSourceArr);
        }
        return maybeWrapWithAdsMediaSource(mediaItem, maybeClipMediaSource(mediaItem, createMediaSource));
    }

    @Override // com.google.android.exoplayer2.source.MediaSource.Factory
    public DefaultMediaSourceFactory setDrmSessionManagerProvider(DrmSessionManagerProvider drmSessionManagerProvider) {
        this.delegateFactoryLoader.setDrmSessionManagerProvider((DrmSessionManagerProvider) Assertions.checkNotNull(drmSessionManagerProvider, "MediaSource.Factory#setDrmSessionManagerProvider no longer handles null by instantiating a new DefaultDrmSessionManagerProvider. Explicitly construct and pass an instance in order to retain the old behavior."));
        return this;
    }

    @Override // com.google.android.exoplayer2.source.MediaSource.Factory
    public DefaultMediaSourceFactory setLoadErrorHandlingPolicy(LoadErrorHandlingPolicy loadErrorHandlingPolicy) {
        this.loadErrorHandlingPolicy = (LoadErrorHandlingPolicy) Assertions.checkNotNull(loadErrorHandlingPolicy, "MediaSource.Factory#setLoadErrorHandlingPolicy no longer handles null by instantiating a new DefaultLoadErrorHandlingPolicy. Explicitly construct and pass an instance in order to retain the old behavior.");
        this.delegateFactoryLoader.setLoadErrorHandlingPolicy(loadErrorHandlingPolicy);
        return this;
    }
}
