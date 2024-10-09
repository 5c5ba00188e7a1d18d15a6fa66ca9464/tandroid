package com.google.android.exoplayer2.extractor;

import android.net.Uri;
import com.google.android.exoplayer2.ext.flac.FlacLibrary;
import com.google.android.exoplayer2.extractor.amr.AmrExtractor;
import com.google.android.exoplayer2.extractor.avi.AviExtractor;
import com.google.android.exoplayer2.extractor.flac.FlacExtractor;
import com.google.android.exoplayer2.extractor.flv.FlvExtractor;
import com.google.android.exoplayer2.extractor.jpeg.JpegExtractor;
import com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer2.extractor.ogg.OggExtractor;
import com.google.android.exoplayer2.extractor.ts.Ac3Extractor;
import com.google.android.exoplayer2.extractor.ts.Ac4Extractor;
import com.google.android.exoplayer2.extractor.ts.AdtsExtractor;
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.extractor.wav.WavExtractor;
import com.google.android.exoplayer2.util.FileTypes;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.common.collect.ImmutableList;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public final class DefaultExtractorsFactory implements ExtractorsFactory {
    private static final int[] DEFAULT_EXTRACTOR_ORDER = {5, 4, 12, 8, 3, 10, 9, 11, 6, 2, 0, 1, 7, 16, 15, 14};
    private static final ExtensionLoader FLAC_EXTENSION_LOADER = new ExtensionLoader(new ExtensionLoader.ConstructorSupplier() { // from class: com.google.android.exoplayer2.extractor.DefaultExtractorsFactory$$ExternalSyntheticLambda0
        @Override // com.google.android.exoplayer2.extractor.DefaultExtractorsFactory.ExtensionLoader.ConstructorSupplier
        public final Constructor getConstructor() {
            Constructor flacExtractorConstructor;
            flacExtractorConstructor = DefaultExtractorsFactory.getFlacExtractorConstructor();
            return flacExtractorConstructor;
        }
    });
    private static final ExtensionLoader MIDI_EXTENSION_LOADER = new ExtensionLoader(new ExtensionLoader.ConstructorSupplier() { // from class: com.google.android.exoplayer2.extractor.DefaultExtractorsFactory$$ExternalSyntheticLambda1
        @Override // com.google.android.exoplayer2.extractor.DefaultExtractorsFactory.ExtensionLoader.ConstructorSupplier
        public final Constructor getConstructor() {
            Constructor midiExtractorConstructor;
            midiExtractorConstructor = DefaultExtractorsFactory.getMidiExtractorConstructor();
            return midiExtractorConstructor;
        }
    });
    private int adtsFlags;
    private int amrFlags;
    private boolean constantBitrateSeekingAlwaysEnabled;
    private int flacFlags;
    private int fragmentedMp4Flags;
    private int matroskaFlags;
    private int mp3Flags;
    private int mp4Flags;
    private int tsFlags;
    private boolean constantBitrateSeekingEnabled = true;
    private int tsMode = 1;
    private int tsTimestampSearchBytes = 112800;
    private ImmutableList tsSubtitleFormats = ImmutableList.of();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ExtensionLoader {
        private final ConstructorSupplier constructorSupplier;
        private final AtomicBoolean extensionLoaded = new AtomicBoolean(false);
        private Constructor extractorConstructor;

        /* loaded from: classes.dex */
        public interface ConstructorSupplier {
            Constructor getConstructor();
        }

        public ExtensionLoader(ConstructorSupplier constructorSupplier) {
            this.constructorSupplier = constructorSupplier;
        }

        private Constructor maybeLoadExtractorConstructor() {
            synchronized (this.extensionLoaded) {
                if (this.extensionLoaded.get()) {
                    return this.extractorConstructor;
                }
                try {
                    return this.constructorSupplier.getConstructor();
                } catch (ClassNotFoundException unused) {
                    this.extensionLoaded.set(true);
                    return this.extractorConstructor;
                } catch (Exception e) {
                    throw new RuntimeException("Error instantiating extension", e);
                }
            }
        }

        public Extractor getExtractor(Object... objArr) {
            Constructor maybeLoadExtractorConstructor = maybeLoadExtractorConstructor();
            if (maybeLoadExtractorConstructor == null) {
                return null;
            }
            try {
                return (Extractor) maybeLoadExtractorConstructor.newInstance(objArr);
            } catch (Exception e) {
                throw new IllegalStateException("Unexpected error creating extractor", e);
            }
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0002. Please report as an issue. */
    private void addExtractorsForFileType(int i, List list) {
        Extractor ac3Extractor;
        switch (i) {
            case 0:
                ac3Extractor = new Ac3Extractor();
                list.add(ac3Extractor);
                return;
            case 1:
                ac3Extractor = new Ac4Extractor();
                list.add(ac3Extractor);
                return;
            case 2:
                ac3Extractor = new AdtsExtractor((this.constantBitrateSeekingAlwaysEnabled ? 2 : 0) | this.adtsFlags | (this.constantBitrateSeekingEnabled ? 1 : 0));
                list.add(ac3Extractor);
                return;
            case 3:
                ac3Extractor = new AmrExtractor((this.constantBitrateSeekingAlwaysEnabled ? 2 : 0) | this.amrFlags | (this.constantBitrateSeekingEnabled ? 1 : 0));
                list.add(ac3Extractor);
                return;
            case 4:
                ac3Extractor = FLAC_EXTENSION_LOADER.getExtractor(Integer.valueOf(this.flacFlags));
                if (ac3Extractor == null) {
                    ac3Extractor = new FlacExtractor(this.flacFlags);
                }
                list.add(ac3Extractor);
                return;
            case 5:
                ac3Extractor = new FlvExtractor();
                list.add(ac3Extractor);
                return;
            case 6:
                ac3Extractor = new MatroskaExtractor(this.matroskaFlags);
                list.add(ac3Extractor);
                return;
            case 7:
                ac3Extractor = new Mp3Extractor((this.constantBitrateSeekingAlwaysEnabled ? 2 : 0) | this.mp3Flags | (this.constantBitrateSeekingEnabled ? 1 : 0));
                list.add(ac3Extractor);
                return;
            case 8:
                list.add(new FragmentedMp4Extractor(this.fragmentedMp4Flags));
                ac3Extractor = new Mp4Extractor(this.mp4Flags);
                list.add(ac3Extractor);
                return;
            case 9:
                ac3Extractor = new OggExtractor();
                list.add(ac3Extractor);
                return;
            case 10:
                ac3Extractor = new PsExtractor();
                list.add(ac3Extractor);
                return;
            case 11:
                ac3Extractor = new TsExtractor(this.tsMode, new TimestampAdjuster(0L), new DefaultTsPayloadReaderFactory(this.tsFlags, this.tsSubtitleFormats), this.tsTimestampSearchBytes);
                list.add(ac3Extractor);
                return;
            case 12:
                ac3Extractor = new WavExtractor();
                list.add(ac3Extractor);
                return;
            case 13:
            default:
                return;
            case 14:
                ac3Extractor = new JpegExtractor();
                list.add(ac3Extractor);
                return;
            case 15:
                ac3Extractor = MIDI_EXTENSION_LOADER.getExtractor(new Object[0]);
                if (ac3Extractor == null) {
                    return;
                }
                list.add(ac3Extractor);
                return;
            case 16:
                ac3Extractor = new AviExtractor();
                list.add(ac3Extractor);
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Constructor getFlacExtractorConstructor() {
        Boolean bool = Boolean.TRUE;
        int i = FlacLibrary.$r8$clinit;
        if (!bool.equals(FlacLibrary.class.getMethod("isAvailable", null).invoke(null, null))) {
            return null;
        }
        ExtractorsFactory extractorsFactory = com.google.android.exoplayer2.ext.flac.FlacExtractor.FACTORY;
        return com.google.android.exoplayer2.ext.flac.FlacExtractor.class.asSubclass(Extractor.class).getConstructor(Integer.TYPE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Constructor getMidiExtractorConstructor() {
        return Class.forName("com.google.android.exoplayer2.decoder.midi.MidiExtractor").asSubclass(Extractor.class).getConstructor(null);
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
    public synchronized Extractor[] createExtractors() {
        return createExtractors(Uri.EMPTY, new HashMap());
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
    public synchronized Extractor[] createExtractors(Uri uri, Map map) {
        ArrayList arrayList;
        try {
            int[] iArr = DEFAULT_EXTRACTOR_ORDER;
            arrayList = new ArrayList(iArr.length);
            int inferFileTypeFromResponseHeaders = FileTypes.inferFileTypeFromResponseHeaders(map);
            if (inferFileTypeFromResponseHeaders != -1) {
                addExtractorsForFileType(inferFileTypeFromResponseHeaders, arrayList);
            }
            int inferFileTypeFromUri = FileTypes.inferFileTypeFromUri(uri);
            if (inferFileTypeFromUri != -1 && inferFileTypeFromUri != inferFileTypeFromResponseHeaders) {
                addExtractorsForFileType(inferFileTypeFromUri, arrayList);
            }
            for (int i : iArr) {
                if (i != inferFileTypeFromResponseHeaders && i != inferFileTypeFromUri) {
                    addExtractorsForFileType(i, arrayList);
                }
            }
        } catch (Throwable th) {
            throw th;
        }
        return (Extractor[]) arrayList.toArray(new Extractor[arrayList.size()]);
    }
}
