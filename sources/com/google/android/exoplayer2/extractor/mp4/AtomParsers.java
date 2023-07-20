package com.google.android.exoplayer2.extractor.mp4;

import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.audio.AacUtil;
import com.google.android.exoplayer2.audio.Ac3Util;
import com.google.android.exoplayer2.audio.Ac4Util;
import com.google.android.exoplayer2.audio.OpusUtil;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ExtractorUtil;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.extractor.mp4.Atom;
import com.google.android.exoplayer2.extractor.mp4.FixedSampleSizeRechunker;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.mp4.MdtaMetadataEntry;
import com.google.android.exoplayer2.metadata.mp4.SmtaMetadataEntry;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.AvcConfig;
import com.google.android.exoplayer2.video.ColorInfo;
import com.google.android.exoplayer2.video.DolbyVisionConfig;
import com.google.android.exoplayer2.video.HevcConfig;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessagesStorage;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AtomParsers {
    private static final byte[] opusMagic = Util.getUtf8Bytes("OpusHead");

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface SampleSizeBox {
        int getFixedSampleSize();

        int getSampleCount();

        int readNextSampleSize();
    }

    private static int getTrackTypeForHdlr(int i) {
        if (i == 1936684398) {
            return 1;
        }
        if (i == 1986618469) {
            return 2;
        }
        if (i == 1952807028 || i == 1935832172 || i == 1937072756 || i == 1668047728) {
            return 3;
        }
        return i == 1835365473 ? 5 : -1;
    }

    public static List<TrackSampleTable> parseTraks(Atom.ContainerAtom containerAtom, GaplessInfoHolder gaplessInfoHolder, long j, DrmInitData drmInitData, boolean z, boolean z2, Function<Track, Track> function) throws ParserException {
        Track apply;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < containerAtom.containerChildren.size(); i++) {
            Atom.ContainerAtom containerAtom2 = containerAtom.containerChildren.get(i);
            if (containerAtom2.type == 1953653099 && (apply = function.apply(parseTrak(containerAtom2, (Atom.LeafAtom) Assertions.checkNotNull(containerAtom.getLeafAtomOfType(1836476516)), j, drmInitData, z, z2))) != null) {
                arrayList.add(parseStbl(apply, (Atom.ContainerAtom) Assertions.checkNotNull(((Atom.ContainerAtom) Assertions.checkNotNull(((Atom.ContainerAtom) Assertions.checkNotNull(containerAtom2.getContainerAtomOfType(1835297121))).getContainerAtomOfType(1835626086))).getContainerAtomOfType(1937007212)), gaplessInfoHolder));
            }
        }
        return arrayList;
    }

    public static Pair<Metadata, Metadata> parseUdta(Atom.LeafAtom leafAtom) {
        ParsableByteArray parsableByteArray = leafAtom.data;
        parsableByteArray.setPosition(8);
        Metadata metadata = null;
        Metadata metadata2 = null;
        while (parsableByteArray.bytesLeft() >= 8) {
            int position = parsableByteArray.getPosition();
            int readInt = parsableByteArray.readInt();
            int readInt2 = parsableByteArray.readInt();
            if (readInt2 == 1835365473) {
                parsableByteArray.setPosition(position);
                metadata = parseUdtaMeta(parsableByteArray, position + readInt);
            } else if (readInt2 == 1936553057) {
                parsableByteArray.setPosition(position);
                metadata2 = parseSmta(parsableByteArray, position + readInt);
            }
            parsableByteArray.setPosition(position + readInt);
        }
        return Pair.create(metadata, metadata2);
    }

    public static Metadata parseMdtaFromMeta(Atom.ContainerAtom containerAtom) {
        Atom.LeafAtom leafAtomOfType = containerAtom.getLeafAtomOfType(1751411826);
        Atom.LeafAtom leafAtomOfType2 = containerAtom.getLeafAtomOfType(1801812339);
        Atom.LeafAtom leafAtomOfType3 = containerAtom.getLeafAtomOfType(1768715124);
        if (leafAtomOfType == null || leafAtomOfType2 == null || leafAtomOfType3 == null || parseHdlr(leafAtomOfType.data) != 1835299937) {
            return null;
        }
        ParsableByteArray parsableByteArray = leafAtomOfType2.data;
        parsableByteArray.setPosition(12);
        int readInt = parsableByteArray.readInt();
        String[] strArr = new String[readInt];
        for (int i = 0; i < readInt; i++) {
            int readInt2 = parsableByteArray.readInt();
            parsableByteArray.skipBytes(4);
            strArr[i] = parsableByteArray.readString(readInt2 - 8);
        }
        ParsableByteArray parsableByteArray2 = leafAtomOfType3.data;
        parsableByteArray2.setPosition(8);
        ArrayList arrayList = new ArrayList();
        while (parsableByteArray2.bytesLeft() > 8) {
            int position = parsableByteArray2.getPosition();
            int readInt3 = parsableByteArray2.readInt();
            int readInt4 = parsableByteArray2.readInt() - 1;
            if (readInt4 >= 0 && readInt4 < readInt) {
                MdtaMetadataEntry parseMdtaMetadataEntryFromIlst = MetadataUtil.parseMdtaMetadataEntryFromIlst(parsableByteArray2, position + readInt3, strArr[readInt4]);
                if (parseMdtaMetadataEntryFromIlst != null) {
                    arrayList.add(parseMdtaMetadataEntryFromIlst);
                }
            } else {
                Log.w("AtomParsers", "Skipped metadata with unknown key index: " + readInt4);
            }
            parsableByteArray2.setPosition(position + readInt3);
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return new Metadata(arrayList);
    }

    public static void maybeSkipRemainingMetaAtomHeaderBytes(ParsableByteArray parsableByteArray) {
        int position = parsableByteArray.getPosition();
        parsableByteArray.skipBytes(4);
        if (parsableByteArray.readInt() != 1751411826) {
            position += 4;
        }
        parsableByteArray.setPosition(position);
    }

    private static Track parseTrak(Atom.ContainerAtom containerAtom, Atom.LeafAtom leafAtom, long j, DrmInitData drmInitData, boolean z, boolean z2) throws ParserException {
        Atom.LeafAtom leafAtom2;
        long j2;
        long[] jArr;
        long[] jArr2;
        Atom.ContainerAtom containerAtomOfType;
        Pair<long[], long[]> parseEdts;
        Atom.ContainerAtom containerAtom2 = (Atom.ContainerAtom) Assertions.checkNotNull(containerAtom.getContainerAtomOfType(1835297121));
        int trackTypeForHdlr = getTrackTypeForHdlr(parseHdlr(((Atom.LeafAtom) Assertions.checkNotNull(containerAtom2.getLeafAtomOfType(1751411826))).data));
        if (trackTypeForHdlr == -1) {
            return null;
        }
        TkhdData parseTkhd = parseTkhd(((Atom.LeafAtom) Assertions.checkNotNull(containerAtom.getLeafAtomOfType(1953196132))).data);
        if (j == -9223372036854775807L) {
            leafAtom2 = leafAtom;
            j2 = parseTkhd.duration;
        } else {
            leafAtom2 = leafAtom;
            j2 = j;
        }
        long parseMvhd = parseMvhd(leafAtom2.data);
        long scaleLargeTimestamp = j2 != -9223372036854775807L ? Util.scaleLargeTimestamp(j2, 1000000L, parseMvhd) : -9223372036854775807L;
        Pair<Long, String> parseMdhd = parseMdhd(((Atom.LeafAtom) Assertions.checkNotNull(containerAtom2.getLeafAtomOfType(1835296868))).data);
        Atom.LeafAtom leafAtomOfType = ((Atom.ContainerAtom) Assertions.checkNotNull(((Atom.ContainerAtom) Assertions.checkNotNull(containerAtom2.getContainerAtomOfType(1835626086))).getContainerAtomOfType(1937007212))).getLeafAtomOfType(1937011556);
        if (leafAtomOfType == null) {
            throw ParserException.createForMalformedContainer("Malformed sample table (stbl) missing sample description (stsd)", null);
        }
        StsdData parseStsd = parseStsd(leafAtomOfType.data, parseTkhd.id, parseTkhd.rotationDegrees, (String) parseMdhd.second, drmInitData, z2);
        if (z || (containerAtomOfType = containerAtom.getContainerAtomOfType(1701082227)) == null || (parseEdts = parseEdts(containerAtomOfType)) == null) {
            jArr = null;
            jArr2 = null;
        } else {
            jArr2 = (long[]) parseEdts.second;
            jArr = (long[]) parseEdts.first;
        }
        if (parseStsd.format == null) {
            return null;
        }
        return new Track(parseTkhd.id, trackTypeForHdlr, ((Long) parseMdhd.first).longValue(), parseMvhd, scaleLargeTimestamp, parseStsd.format, parseStsd.requiredSampleTransformation, parseStsd.trackEncryptionBoxes, parseStsd.nalUnitLengthFieldLength, jArr, jArr2);
    }

    /* JADX WARN: Removed duplicated region for block: B:116:0x02b1  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x02c1  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x03b2  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x03b4  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x03cc  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x043c  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x0441  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x0444  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x0447  */
    /* JADX WARN: Removed duplicated region for block: B:180:0x044a  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x044d  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x044f  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0453  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x0456  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0465  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x0431 A[EDGE_INSN: B:212:0x0431->B:170:0x0431 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00ff  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0131  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static TrackSampleTable parseStbl(Track track, Atom.ContainerAtom containerAtom, GaplessInfoHolder gaplessInfoHolder) throws ParserException {
        SampleSizeBox stz2SampleSizeBox;
        boolean z;
        int i;
        int i2;
        int i3;
        int fixedSampleSize;
        int i4;
        boolean z2;
        int i5;
        int i6;
        int i7;
        int i8;
        boolean z3;
        int i9;
        Track track2;
        int i10;
        long[] jArr;
        int[] iArr;
        int i11;
        long j;
        long[] jArr2;
        int[] iArr2;
        int i12;
        int i13;
        long[] jArr3;
        int i14;
        int i15;
        long[] jArr4;
        int i16;
        boolean z4;
        int i17;
        long[] jArr5;
        int i18;
        long[] jArr6;
        int[] iArr3;
        int i19;
        int i20;
        boolean z5;
        Atom.LeafAtom leafAtomOfType = containerAtom.getLeafAtomOfType(1937011578);
        if (leafAtomOfType != null) {
            stz2SampleSizeBox = new StszSampleSizeBox(leafAtomOfType, track.format);
        } else {
            Atom.LeafAtom leafAtomOfType2 = containerAtom.getLeafAtomOfType(1937013298);
            if (leafAtomOfType2 == null) {
                throw ParserException.createForMalformedContainer("Track has no sample table size information", null);
            }
            stz2SampleSizeBox = new Stz2SampleSizeBox(leafAtomOfType2);
        }
        int sampleCount = stz2SampleSizeBox.getSampleCount();
        if (sampleCount == 0) {
            return new TrackSampleTable(track, new long[0], new int[0], 0, new long[0], new int[0], 0L);
        }
        Atom.LeafAtom leafAtomOfType3 = containerAtom.getLeafAtomOfType(1937007471);
        if (leafAtomOfType3 == null) {
            leafAtomOfType3 = (Atom.LeafAtom) Assertions.checkNotNull(containerAtom.getLeafAtomOfType(1668232756));
            z = true;
        } else {
            z = false;
        }
        ParsableByteArray parsableByteArray = leafAtomOfType3.data;
        ParsableByteArray parsableByteArray2 = ((Atom.LeafAtom) Assertions.checkNotNull(containerAtom.getLeafAtomOfType(1937011555))).data;
        ParsableByteArray parsableByteArray3 = ((Atom.LeafAtom) Assertions.checkNotNull(containerAtom.getLeafAtomOfType(1937011827))).data;
        Atom.LeafAtom leafAtomOfType4 = containerAtom.getLeafAtomOfType(1937011571);
        ParsableByteArray parsableByteArray4 = leafAtomOfType4 != null ? leafAtomOfType4.data : null;
        Atom.LeafAtom leafAtomOfType5 = containerAtom.getLeafAtomOfType(1668576371);
        ParsableByteArray parsableByteArray5 = leafAtomOfType5 != null ? leafAtomOfType5.data : null;
        ChunkIterator chunkIterator = new ChunkIterator(parsableByteArray2, parsableByteArray, z);
        parsableByteArray3.setPosition(12);
        int readUnsignedIntToInt = parsableByteArray3.readUnsignedIntToInt() - 1;
        int readUnsignedIntToInt2 = parsableByteArray3.readUnsignedIntToInt();
        int readUnsignedIntToInt3 = parsableByteArray3.readUnsignedIntToInt();
        if (parsableByteArray5 != null) {
            parsableByteArray5.setPosition(12);
            i = parsableByteArray5.readUnsignedIntToInt();
        } else {
            i = 0;
        }
        if (parsableByteArray4 != null) {
            parsableByteArray4.setPosition(12);
            i2 = parsableByteArray4.readUnsignedIntToInt();
            if (i2 > 0) {
                i3 = parsableByteArray4.readUnsignedIntToInt() - 1;
                fixedSampleSize = stz2SampleSizeBox.getFixedSampleSize();
                String str = track.format.sampleMimeType;
                if (fixedSampleSize == -1 && (("audio/raw".equals(str) || "audio/g711-mlaw".equals(str) || "audio/g711-alaw".equals(str)) && readUnsignedIntToInt == 0 && i == 0 && i2 == 0)) {
                    i4 = i2;
                    z2 = true;
                } else {
                    i4 = i2;
                    z2 = false;
                }
                if (!z2) {
                    int i21 = chunkIterator.length;
                    long[] jArr7 = new long[i21];
                    int[] iArr4 = new int[i21];
                    while (chunkIterator.moveNext()) {
                        int i22 = chunkIterator.index;
                        jArr7[i22] = chunkIterator.offset;
                        iArr4[i22] = chunkIterator.numSamples;
                    }
                    FixedSampleSizeRechunker.Results rechunk = FixedSampleSizeRechunker.rechunk(fixedSampleSize, jArr7, iArr4, readUnsignedIntToInt3);
                    long[] jArr8 = rechunk.offsets;
                    int[] iArr5 = rechunk.sizes;
                    int i23 = rechunk.maximumSize;
                    long[] jArr9 = rechunk.timestamps;
                    int[] iArr6 = rechunk.flags;
                    long j2 = rechunk.duration;
                    track2 = track;
                    i10 = sampleCount;
                    jArr = jArr8;
                    iArr = iArr5;
                    i11 = i23;
                    iArr2 = iArr6;
                    j = j2;
                    jArr2 = jArr9;
                } else {
                    long[] jArr10 = new long[sampleCount];
                    int[] iArr7 = new int[sampleCount];
                    long[] jArr11 = new long[sampleCount];
                    int[] iArr8 = new int[sampleCount];
                    int i24 = i3;
                    int i25 = 0;
                    int i26 = 0;
                    int i27 = 0;
                    int i28 = 0;
                    int i29 = 0;
                    long j3 = 0;
                    long j4 = 0;
                    int i30 = i;
                    int i31 = readUnsignedIntToInt3;
                    int i32 = readUnsignedIntToInt2;
                    int i33 = readUnsignedIntToInt;
                    int i34 = i4;
                    while (true) {
                        i5 = i33;
                        if (i25 >= sampleCount) {
                            i6 = i32;
                            i7 = i27;
                            i8 = i28;
                            break;
                        }
                        long j5 = j4;
                        int i35 = i28;
                        boolean z6 = true;
                        while (i35 == 0) {
                            z6 = chunkIterator.moveNext();
                            if (!z6) {
                                break;
                            }
                            int i36 = i32;
                            long j6 = chunkIterator.offset;
                            i35 = chunkIterator.numSamples;
                            j5 = j6;
                            i32 = i36;
                            i31 = i31;
                            sampleCount = sampleCount;
                        }
                        int i37 = sampleCount;
                        i6 = i32;
                        int i38 = i31;
                        if (!z6) {
                            Log.w("AtomParsers", "Unexpected end of chunk data");
                            jArr10 = Arrays.copyOf(jArr10, i25);
                            iArr7 = Arrays.copyOf(iArr7, i25);
                            jArr11 = Arrays.copyOf(jArr11, i25);
                            iArr8 = Arrays.copyOf(iArr8, i25);
                            sampleCount = i25;
                            i7 = i27;
                            i8 = i35;
                            break;
                        }
                        if (parsableByteArray5 != null) {
                            while (i29 == 0 && i30 > 0) {
                                i29 = parsableByteArray5.readUnsignedIntToInt();
                                i27 = parsableByteArray5.readInt();
                                i30--;
                            }
                            i29--;
                        }
                        int i39 = i27;
                        jArr10[i25] = j5;
                        iArr7[i25] = stz2SampleSizeBox.readNextSampleSize();
                        if (iArr7[i25] > i26) {
                            i26 = iArr7[i25];
                        }
                        jArr11[i25] = j3 + i39;
                        iArr8[i25] = parsableByteArray4 == null ? 1 : 0;
                        if (i25 == i24) {
                            iArr8[i25] = 1;
                            i34--;
                            if (i34 > 0) {
                                i24 = ((ParsableByteArray) Assertions.checkNotNull(parsableByteArray4)).readUnsignedIntToInt() - 1;
                            }
                        }
                        int i40 = i24;
                        j3 += i38;
                        int i41 = i6 - 1;
                        if (i41 != 0 || i5 <= 0) {
                            i12 = i38;
                            i13 = i5;
                        } else {
                            i41 = parsableByteArray3.readUnsignedIntToInt();
                            i12 = parsableByteArray3.readInt();
                            i13 = i5 - 1;
                        }
                        int i42 = i41;
                        i28 = i35 - 1;
                        i25++;
                        j4 = j5 + iArr7[i25];
                        i24 = i40;
                        i31 = i12;
                        sampleCount = i37;
                        i27 = i39;
                        i33 = i13;
                        i32 = i42;
                    }
                    long j7 = j3 + i7;
                    if (parsableByteArray5 != null) {
                        while (i30 > 0) {
                            if (parsableByteArray5.readUnsignedIntToInt() != 0) {
                                z3 = false;
                                break;
                            }
                            parsableByteArray5.readInt();
                            i30--;
                        }
                    }
                    z3 = true;
                    if (i34 == 0 && i6 == 0 && i8 == 0 && i5 == 0) {
                        i9 = i29;
                        if (i9 == 0 && z3) {
                            track2 = track;
                            i10 = sampleCount;
                            jArr = jArr10;
                            iArr = iArr7;
                            i11 = i26;
                            j = j7;
                            jArr2 = jArr11;
                            iArr2 = iArr8;
                        }
                    } else {
                        i9 = i29;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Inconsistent stbl box for track ");
                    track2 = track;
                    sb.append(track2.id);
                    sb.append(": remainingSynchronizationSamples ");
                    sb.append(i34);
                    sb.append(", remainingSamplesAtTimestampDelta ");
                    sb.append(i6);
                    sb.append(", remainingSamplesInChunk ");
                    sb.append(i8);
                    sb.append(", remainingTimestampDeltaChanges ");
                    sb.append(i5);
                    sb.append(", remainingSamplesAtTimestampOffset ");
                    sb.append(i9);
                    sb.append(!z3 ? ", ctts invalid" : "");
                    Log.w("AtomParsers", sb.toString());
                    i10 = sampleCount;
                    jArr = jArr10;
                    iArr = iArr7;
                    i11 = i26;
                    j = j7;
                    jArr2 = jArr11;
                    iArr2 = iArr8;
                }
                long scaleLargeTimestamp = Util.scaleLargeTimestamp(j, 1000000L, track2.timescale);
                jArr3 = track2.editListDurations;
                if (jArr3 != null) {
                    Util.scaleLargeTimestampsInPlace(jArr2, 1000000L, track2.timescale);
                    return new TrackSampleTable(track, jArr, iArr, i11, jArr2, iArr2, scaleLargeTimestamp);
                }
                if (jArr3.length == 1 && track2.type == 1 && jArr2.length >= 2) {
                    long j8 = ((long[]) Assertions.checkNotNull(track2.editListMediaTimes))[0];
                    long scaleLargeTimestamp2 = j8 + Util.scaleLargeTimestamp(track2.editListDurations[0], track2.timescale, track2.movieTimescale);
                    i14 = i10;
                    if (canApplyEditWithGaplessInfo(jArr2, j, j8, scaleLargeTimestamp2)) {
                        long scaleLargeTimestamp3 = Util.scaleLargeTimestamp(j8 - jArr2[0], track2.format.sampleRate, track2.timescale);
                        i15 = i11;
                        long scaleLargeTimestamp4 = Util.scaleLargeTimestamp(j - scaleLargeTimestamp2, track2.format.sampleRate, track2.timescale);
                        if ((scaleLargeTimestamp3 != 0 || scaleLargeTimestamp4 != 0) && scaleLargeTimestamp3 <= 2147483647L && scaleLargeTimestamp4 <= 2147483647L) {
                            gaplessInfoHolder.encoderDelay = (int) scaleLargeTimestamp3;
                            gaplessInfoHolder.encoderPadding = (int) scaleLargeTimestamp4;
                            Util.scaleLargeTimestampsInPlace(jArr2, 1000000L, track2.timescale);
                            return new TrackSampleTable(track, jArr, iArr, i15, jArr2, iArr2, Util.scaleLargeTimestamp(track2.editListDurations[0], 1000000L, track2.movieTimescale));
                        }
                        jArr4 = track2.editListDurations;
                        if (jArr4.length != 1 && jArr4[0] == 0) {
                            long j9 = ((long[]) Assertions.checkNotNull(track2.editListMediaTimes))[0];
                            for (int i43 = 0; i43 < jArr2.length; i43++) {
                                jArr2[i43] = Util.scaleLargeTimestamp(jArr2[i43] - j9, 1000000L, track2.timescale);
                            }
                            return new TrackSampleTable(track, jArr, iArr, i15, jArr2, iArr2, Util.scaleLargeTimestamp(j - j9, 1000000L, track2.timescale));
                        }
                        boolean z7 = track2.type != 1;
                        int[] iArr9 = new int[jArr4.length];
                        int[] iArr10 = new int[jArr4.length];
                        long[] jArr12 = (long[]) Assertions.checkNotNull(track2.editListMediaTimes);
                        i16 = 0;
                        z4 = false;
                        int i44 = 0;
                        i17 = 0;
                        while (true) {
                            jArr5 = track2.editListDurations;
                            if (i16 < jArr5.length) {
                                break;
                            }
                            long[] jArr13 = jArr;
                            int[] iArr11 = iArr;
                            long j10 = jArr12[i16];
                            if (j10 != -1) {
                                int i45 = i17;
                                boolean z8 = z4;
                                int i46 = i44;
                                long scaleLargeTimestamp5 = Util.scaleLargeTimestamp(jArr5[i16], track2.timescale, track2.movieTimescale);
                                iArr9[i16] = Util.binarySearchFloor(jArr2, j10, true, true);
                                iArr10[i16] = Util.binarySearchCeil(jArr2, j10 + scaleLargeTimestamp5, z7, false);
                                while (iArr9[i16] < iArr10[i16] && (iArr2[iArr9[i16]] & 1) == 0) {
                                    iArr9[i16] = iArr9[i16] + 1;
                                }
                                i44 = i46 + (iArr10[i16] - iArr9[i16]);
                                z5 = z8 | (i45 != iArr9[i16]);
                                i20 = iArr10[i16];
                            } else {
                                i20 = i17;
                                z5 = z4;
                            }
                            i16++;
                            z4 = z5;
                            i17 = i20;
                            jArr = jArr13;
                            iArr = iArr11;
                        }
                        long[] jArr14 = jArr;
                        int[] iArr12 = iArr;
                        boolean z9 = z4;
                        i18 = 0;
                        boolean z10 = z9 | (i44 != i14);
                        long[] jArr15 = !z10 ? new long[i44] : jArr14;
                        int[] iArr13 = !z10 ? new int[i44] : iArr12;
                        int i47 = !z10 ? 0 : i15;
                        int[] iArr14 = !z10 ? new int[i44] : iArr2;
                        long[] jArr16 = new long[i44];
                        int i48 = i47;
                        int[] iArr15 = iArr12;
                        long j11 = 0;
                        int i49 = 0;
                        while (i18 < track2.editListDurations.length) {
                            long j12 = track2.editListMediaTimes[i18];
                            int i50 = iArr9[i18];
                            int[] iArr16 = iArr9;
                            int i51 = iArr10[i18];
                            int[] iArr17 = iArr10;
                            if (z10) {
                                int i52 = i51 - i50;
                                System.arraycopy(jArr14, i50, jArr15, i49, i52);
                                jArr6 = jArr14;
                                iArr3 = iArr15;
                                System.arraycopy(iArr3, i50, iArr13, i49, i52);
                                System.arraycopy(iArr2, i50, iArr14, i49, i52);
                            } else {
                                jArr6 = jArr14;
                                iArr3 = iArr15;
                            }
                            int i53 = i48;
                            while (i50 < i51) {
                                int i54 = i53;
                                int i55 = i51;
                                long[] jArr17 = jArr2;
                                int[] iArr18 = iArr2;
                                int[] iArr19 = iArr14;
                                long j13 = j11;
                                jArr16[i49] = Util.scaleLargeTimestamp(j11, 1000000L, track2.movieTimescale) + Util.scaleLargeTimestamp(Math.max(0L, jArr2[i50] - j12), 1000000L, track2.timescale);
                                if (z10) {
                                    i19 = i54;
                                    if (iArr13[i49] > i19) {
                                        i53 = iArr3[i50];
                                        i49++;
                                        i50++;
                                        i51 = i55;
                                        j11 = j13;
                                        jArr2 = jArr17;
                                        iArr2 = iArr18;
                                        iArr14 = iArr19;
                                    }
                                } else {
                                    i19 = i54;
                                }
                                i53 = i19;
                                i49++;
                                i50++;
                                i51 = i55;
                                j11 = j13;
                                jArr2 = jArr17;
                                iArr2 = iArr18;
                                iArr14 = iArr19;
                            }
                            i18++;
                            i48 = i53;
                            iArr15 = iArr3;
                            j11 += track2.editListDurations[i18];
                            iArr9 = iArr16;
                            jArr2 = jArr2;
                            iArr2 = iArr2;
                            iArr10 = iArr17;
                            jArr14 = jArr6;
                            iArr14 = iArr14;
                        }
                        return new TrackSampleTable(track, jArr15, iArr13, i48, jArr16, iArr14, Util.scaleLargeTimestamp(j11, 1000000L, track2.movieTimescale));
                    }
                } else {
                    i14 = i10;
                }
                i15 = i11;
                jArr4 = track2.editListDurations;
                if (jArr4.length != 1) {
                }
                if (track2.type != 1) {
                }
                int[] iArr92 = new int[jArr4.length];
                int[] iArr102 = new int[jArr4.length];
                long[] jArr122 = (long[]) Assertions.checkNotNull(track2.editListMediaTimes);
                i16 = 0;
                z4 = false;
                int i442 = 0;
                i17 = 0;
                while (true) {
                    jArr5 = track2.editListDurations;
                    if (i16 < jArr5.length) {
                    }
                    i16++;
                    z4 = z5;
                    i17 = i20;
                    jArr = jArr13;
                    iArr = iArr11;
                }
                long[] jArr142 = jArr;
                int[] iArr122 = iArr;
                boolean z92 = z4;
                i18 = 0;
                boolean z102 = z92 | (i442 != i14);
                if (!z102) {
                }
                if (!z102) {
                }
                if (!z102) {
                }
                if (!z102) {
                }
                long[] jArr162 = new long[i442];
                int i482 = i47;
                int[] iArr152 = iArr122;
                long j112 = 0;
                int i492 = 0;
                while (i18 < track2.editListDurations.length) {
                }
                return new TrackSampleTable(track, jArr15, iArr13, i482, jArr162, iArr14, Util.scaleLargeTimestamp(j112, 1000000L, track2.movieTimescale));
            }
            parsableByteArray4 = null;
        } else {
            i2 = 0;
        }
        i3 = -1;
        fixedSampleSize = stz2SampleSizeBox.getFixedSampleSize();
        String str2 = track.format.sampleMimeType;
        if (fixedSampleSize == -1) {
        }
        i4 = i2;
        z2 = false;
        if (!z2) {
        }
        long scaleLargeTimestamp6 = Util.scaleLargeTimestamp(j, 1000000L, track2.timescale);
        jArr3 = track2.editListDurations;
        if (jArr3 != null) {
        }
    }

    private static Metadata parseUdtaMeta(ParsableByteArray parsableByteArray, int i) {
        parsableByteArray.skipBytes(8);
        maybeSkipRemainingMetaAtomHeaderBytes(parsableByteArray);
        while (parsableByteArray.getPosition() < i) {
            int position = parsableByteArray.getPosition();
            int readInt = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == 1768715124) {
                parsableByteArray.setPosition(position);
                return parseIlst(parsableByteArray, position + readInt);
            }
            parsableByteArray.setPosition(position + readInt);
        }
        return null;
    }

    private static Metadata parseIlst(ParsableByteArray parsableByteArray, int i) {
        parsableByteArray.skipBytes(8);
        ArrayList arrayList = new ArrayList();
        while (parsableByteArray.getPosition() < i) {
            Metadata.Entry parseIlstElement = MetadataUtil.parseIlstElement(parsableByteArray);
            if (parseIlstElement != null) {
                arrayList.add(parseIlstElement);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return new Metadata(arrayList);
    }

    private static Metadata parseSmta(ParsableByteArray parsableByteArray, int i) {
        parsableByteArray.skipBytes(12);
        while (parsableByteArray.getPosition() < i) {
            int position = parsableByteArray.getPosition();
            int readInt = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == 1935766900) {
                if (readInt < 14) {
                    return null;
                }
                parsableByteArray.skipBytes(5);
                int readUnsignedByte = parsableByteArray.readUnsignedByte();
                if (readUnsignedByte == 12 || readUnsignedByte == 13) {
                    float f = readUnsignedByte == 12 ? 240.0f : 120.0f;
                    parsableByteArray.skipBytes(1);
                    return new Metadata(new SmtaMetadataEntry(f, parsableByteArray.readUnsignedByte()));
                }
                return null;
            }
            parsableByteArray.setPosition(position + readInt);
        }
        return null;
    }

    private static long parseMvhd(ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(8);
        parsableByteArray.skipBytes(Atom.parseFullAtomVersion(parsableByteArray.readInt()) != 0 ? 16 : 8);
        return parsableByteArray.readUnsignedInt();
    }

    private static TkhdData parseTkhd(ParsableByteArray parsableByteArray) {
        boolean z;
        parsableByteArray.setPosition(8);
        int parseFullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
        parsableByteArray.skipBytes(parseFullAtomVersion == 0 ? 8 : 16);
        int readInt = parsableByteArray.readInt();
        parsableByteArray.skipBytes(4);
        int position = parsableByteArray.getPosition();
        int i = parseFullAtomVersion == 0 ? 4 : 8;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i3 >= i) {
                z = true;
                break;
            } else if (parsableByteArray.getData()[position + i3] != -1) {
                z = false;
                break;
            } else {
                i3++;
            }
        }
        long j = -9223372036854775807L;
        if (z) {
            parsableByteArray.skipBytes(i);
        } else {
            long readUnsignedInt = parseFullAtomVersion == 0 ? parsableByteArray.readUnsignedInt() : parsableByteArray.readUnsignedLongToLong();
            if (readUnsignedInt != 0) {
                j = readUnsignedInt;
            }
        }
        parsableByteArray.skipBytes(16);
        int readInt2 = parsableByteArray.readInt();
        int readInt3 = parsableByteArray.readInt();
        parsableByteArray.skipBytes(4);
        int readInt4 = parsableByteArray.readInt();
        int readInt5 = parsableByteArray.readInt();
        if (readInt2 == 0 && readInt3 == 65536 && readInt4 == -65536 && readInt5 == 0) {
            i2 = 90;
        } else if (readInt2 == 0 && readInt3 == -65536 && readInt4 == 65536 && readInt5 == 0) {
            i2 = 270;
        } else if (readInt2 == -65536 && readInt3 == 0 && readInt4 == 0 && readInt5 == -65536) {
            i2 = 180;
        }
        return new TkhdData(readInt, j, i2);
    }

    private static int parseHdlr(ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(16);
        return parsableByteArray.readInt();
    }

    private static Pair<Long, String> parseMdhd(ParsableByteArray parsableByteArray) {
        parsableByteArray.setPosition(8);
        int parseFullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
        parsableByteArray.skipBytes(parseFullAtomVersion == 0 ? 8 : 16);
        long readUnsignedInt = parsableByteArray.readUnsignedInt();
        parsableByteArray.skipBytes(parseFullAtomVersion == 0 ? 4 : 8);
        int readUnsignedShort = parsableByteArray.readUnsignedShort();
        return Pair.create(Long.valueOf(readUnsignedInt), "" + ((char) (((readUnsignedShort >> 10) & 31) + 96)) + ((char) (((readUnsignedShort >> 5) & 31) + 96)) + ((char) ((readUnsignedShort & 31) + 96)));
    }

    private static StsdData parseStsd(ParsableByteArray parsableByteArray, int i, int i2, String str, DrmInitData drmInitData, boolean z) throws ParserException {
        int i3;
        parsableByteArray.setPosition(12);
        int readInt = parsableByteArray.readInt();
        StsdData stsdData = new StsdData(readInt);
        for (int i4 = 0; i4 < readInt; i4++) {
            int position = parsableByteArray.getPosition();
            int readInt2 = parsableByteArray.readInt();
            ExtractorUtil.checkContainerInput(readInt2 > 0, "childAtomSize must be positive");
            int readInt3 = parsableByteArray.readInt();
            if (readInt3 == 1635148593 || readInt3 == 1635148595 || readInt3 == 1701733238 || readInt3 == 1831958048 || readInt3 == 1836070006 || readInt3 == 1752589105 || readInt3 == 1751479857 || readInt3 == 1932670515 || readInt3 == 1211250227 || readInt3 == 1987063864 || readInt3 == 1987063865 || readInt3 == 1635135537 || readInt3 == 1685479798 || readInt3 == 1685479729 || readInt3 == 1685481573 || readInt3 == 1685481521) {
                i3 = position;
                parseVideoSampleEntry(parsableByteArray, readInt3, i3, readInt2, i, i2, drmInitData, stsdData, i4);
            } else if (readInt3 == 1836069985 || readInt3 == 1701733217 || readInt3 == 1633889587 || readInt3 == 1700998451 || readInt3 == 1633889588 || readInt3 == 1835823201 || readInt3 == 1685353315 || readInt3 == 1685353317 || readInt3 == 1685353320 || readInt3 == 1685353324 || readInt3 == 1685353336 || readInt3 == 1935764850 || readInt3 == 1935767394 || readInt3 == 1819304813 || readInt3 == 1936684916 || readInt3 == 1953984371 || readInt3 == 778924082 || readInt3 == 778924083 || readInt3 == 1835557169 || readInt3 == 1835560241 || readInt3 == 1634492771 || readInt3 == 1634492791 || readInt3 == 1970037111 || readInt3 == 1332770163 || readInt3 == 1716281667) {
                i3 = position;
                parseAudioSampleEntry(parsableByteArray, readInt3, position, readInt2, i, str, z, drmInitData, stsdData, i4);
            } else {
                if (readInt3 == 1414810956 || readInt3 == 1954034535 || readInt3 == 2004251764 || readInt3 == 1937010800 || readInt3 == 1664495672) {
                    parseTextSampleEntry(parsableByteArray, readInt3, position, readInt2, i, str, stsdData);
                } else if (readInt3 == 1835365492) {
                    parseMetaDataSampleEntry(parsableByteArray, readInt3, position, i, stsdData);
                } else if (readInt3 == 1667329389) {
                    stsdData.format = new Format.Builder().setId(i).setSampleMimeType("application/x-camera-motion").build();
                }
                i3 = position;
            }
            parsableByteArray.setPosition(i3 + readInt2);
        }
        return stsdData;
    }

    private static void parseTextSampleEntry(ParsableByteArray parsableByteArray, int i, int i2, int i3, int i4, String str, StsdData stsdData) {
        parsableByteArray.setPosition(i2 + 8 + 8);
        String str2 = "application/ttml+xml";
        ImmutableList immutableList = null;
        long j = Long.MAX_VALUE;
        if (i != 1414810956) {
            if (i == 1954034535) {
                int i5 = (i3 - 8) - 8;
                byte[] bArr = new byte[i5];
                parsableByteArray.readBytes(bArr, 0, i5);
                immutableList = ImmutableList.of(bArr);
                str2 = "application/x-quicktime-tx3g";
            } else if (i == 2004251764) {
                str2 = "application/x-mp4-vtt";
            } else if (i == 1937010800) {
                j = 0;
            } else if (i == 1664495672) {
                stsdData.requiredSampleTransformation = 1;
                str2 = "application/x-mp4-cea-608";
            } else {
                throw new IllegalStateException();
            }
        }
        stsdData.format = new Format.Builder().setId(i4).setSampleMimeType(str2).setLanguage(str).setSubsampleOffsetUs(j).setInitializationData(immutableList).build();
    }

    private static void parseVideoSampleEntry(ParsableByteArray parsableByteArray, int i, int i2, int i3, int i4, int i5, DrmInitData drmInitData, StsdData stsdData, int i6) throws ParserException {
        DrmInitData drmInitData2;
        int i7;
        int i8;
        byte[] bArr;
        float f;
        List<byte[]> list;
        String str;
        int i9 = i2;
        int i10 = i3;
        DrmInitData drmInitData3 = drmInitData;
        StsdData stsdData2 = stsdData;
        parsableByteArray.setPosition(i9 + 8 + 8);
        parsableByteArray.skipBytes(16);
        int readUnsignedShort = parsableByteArray.readUnsignedShort();
        int readUnsignedShort2 = parsableByteArray.readUnsignedShort();
        parsableByteArray.skipBytes(50);
        int position = parsableByteArray.getPosition();
        int i11 = i;
        if (i11 == 1701733238) {
            Pair<Integer, TrackEncryptionBox> parseSampleEntryEncryptionData = parseSampleEntryEncryptionData(parsableByteArray, i9, i10);
            if (parseSampleEntryEncryptionData != null) {
                i11 = ((Integer) parseSampleEntryEncryptionData.first).intValue();
                drmInitData3 = drmInitData3 == null ? null : drmInitData3.copyWithSchemeType(((TrackEncryptionBox) parseSampleEntryEncryptionData.second).schemeType);
                stsdData2.trackEncryptionBoxes[i6] = (TrackEncryptionBox) parseSampleEntryEncryptionData.second;
            }
            parsableByteArray.setPosition(position);
        }
        String str2 = "video/3gpp";
        String str3 = i11 == 1831958048 ? "video/mpeg" : i11 == 1211250227 ? "video/3gpp" : null;
        float f2 = 1.0f;
        byte[] bArr2 = null;
        String str4 = null;
        List<byte[]> list2 = null;
        int i12 = -1;
        int i13 = -1;
        int i14 = -1;
        int i15 = -1;
        ByteBuffer byteBuffer = null;
        EsdsData esdsData = null;
        boolean z = false;
        while (true) {
            if (position - i9 >= i10) {
                drmInitData2 = drmInitData3;
                break;
            }
            parsableByteArray.setPosition(position);
            int position2 = parsableByteArray.getPosition();
            String str5 = str2;
            int readInt = parsableByteArray.readInt();
            if (readInt == 0) {
                drmInitData2 = drmInitData3;
                if (parsableByteArray.getPosition() - i9 == i10) {
                    break;
                }
            } else {
                drmInitData2 = drmInitData3;
            }
            ExtractorUtil.checkContainerInput(readInt > 0, "childAtomSize must be positive");
            int readInt2 = parsableByteArray.readInt();
            if (readInt2 == 1635148611) {
                ExtractorUtil.checkContainerInput(str3 == null, null);
                parsableByteArray.setPosition(position2 + 8);
                AvcConfig parse = AvcConfig.parse(parsableByteArray);
                list2 = parse.initializationData;
                stsdData2.nalUnitLengthFieldLength = parse.nalUnitLengthFieldLength;
                if (!z) {
                    f2 = parse.pixelWidthHeightRatio;
                }
                str4 = parse.codecs;
                str = MediaController.VIDEO_MIME_TYPE;
            } else if (readInt2 == 1752589123) {
                ExtractorUtil.checkContainerInput(str3 == null, null);
                parsableByteArray.setPosition(position2 + 8);
                HevcConfig parse2 = HevcConfig.parse(parsableByteArray);
                list2 = parse2.initializationData;
                stsdData2.nalUnitLengthFieldLength = parse2.nalUnitLengthFieldLength;
                if (!z) {
                    f2 = parse2.pixelWidthHeightRatio;
                }
                str4 = parse2.codecs;
                str = "video/hevc";
            } else {
                if (readInt2 == 1685480259 || readInt2 == 1685485123) {
                    i7 = readUnsignedShort2;
                    i8 = i11;
                    bArr = bArr2;
                    f = f2;
                    list = list2;
                    DolbyVisionConfig parse3 = DolbyVisionConfig.parse(parsableByteArray);
                    if (parse3 != null) {
                        str4 = parse3.codecs;
                        str3 = "video/dolby-vision";
                    }
                } else if (readInt2 == 1987076931) {
                    ExtractorUtil.checkContainerInput(str3 == null, null);
                    str = i11 == 1987063864 ? "video/x-vnd.on2.vp8" : "video/x-vnd.on2.vp9";
                } else if (readInt2 == 1635135811) {
                    ExtractorUtil.checkContainerInput(str3 == null, null);
                    str = "video/av01";
                } else if (readInt2 == 1668050025) {
                    ByteBuffer allocateHdrStaticInfo = byteBuffer == null ? allocateHdrStaticInfo() : byteBuffer;
                    allocateHdrStaticInfo.position(21);
                    allocateHdrStaticInfo.putShort(parsableByteArray.readShort());
                    allocateHdrStaticInfo.putShort(parsableByteArray.readShort());
                    byteBuffer = allocateHdrStaticInfo;
                    i7 = readUnsignedShort2;
                    i8 = i11;
                    position += readInt;
                    i9 = i2;
                    i10 = i3;
                    stsdData2 = stsdData;
                    str2 = str5;
                    drmInitData3 = drmInitData2;
                    i11 = i8;
                    readUnsignedShort2 = i7;
                } else if (readInt2 == 1835295606) {
                    ByteBuffer allocateHdrStaticInfo2 = byteBuffer == null ? allocateHdrStaticInfo() : byteBuffer;
                    short readShort = parsableByteArray.readShort();
                    short readShort2 = parsableByteArray.readShort();
                    short readShort3 = parsableByteArray.readShort();
                    i8 = i11;
                    short readShort4 = parsableByteArray.readShort();
                    short readShort5 = parsableByteArray.readShort();
                    List<byte[]> list3 = list2;
                    short readShort6 = parsableByteArray.readShort();
                    byte[] bArr3 = bArr2;
                    short readShort7 = parsableByteArray.readShort();
                    float f3 = f2;
                    short readShort8 = parsableByteArray.readShort();
                    long readUnsignedInt = parsableByteArray.readUnsignedInt();
                    long readUnsignedInt2 = parsableByteArray.readUnsignedInt();
                    i7 = readUnsignedShort2;
                    allocateHdrStaticInfo2.position(1);
                    allocateHdrStaticInfo2.putShort(readShort5);
                    allocateHdrStaticInfo2.putShort(readShort6);
                    allocateHdrStaticInfo2.putShort(readShort);
                    allocateHdrStaticInfo2.putShort(readShort2);
                    allocateHdrStaticInfo2.putShort(readShort3);
                    allocateHdrStaticInfo2.putShort(readShort4);
                    allocateHdrStaticInfo2.putShort(readShort7);
                    allocateHdrStaticInfo2.putShort(readShort8);
                    allocateHdrStaticInfo2.putShort((short) (readUnsignedInt / 10000));
                    allocateHdrStaticInfo2.putShort((short) (readUnsignedInt2 / 10000));
                    byteBuffer = allocateHdrStaticInfo2;
                    list2 = list3;
                    bArr2 = bArr3;
                    f2 = f3;
                    position += readInt;
                    i9 = i2;
                    i10 = i3;
                    stsdData2 = stsdData;
                    str2 = str5;
                    drmInitData3 = drmInitData2;
                    i11 = i8;
                    readUnsignedShort2 = i7;
                } else {
                    i7 = readUnsignedShort2;
                    i8 = i11;
                    bArr = bArr2;
                    f = f2;
                    list = list2;
                    if (readInt2 == 1681012275) {
                        ExtractorUtil.checkContainerInput(str3 == null, null);
                        str3 = str5;
                    } else if (readInt2 == 1702061171) {
                        ExtractorUtil.checkContainerInput(str3 == null, null);
                        esdsData = parseEsdsFromParent(parsableByteArray, position2);
                        String str6 = esdsData.mimeType;
                        byte[] bArr4 = esdsData.initializationData;
                        list2 = bArr4 != null ? ImmutableList.of(bArr4) : list;
                        str3 = str6;
                        bArr2 = bArr;
                        f2 = f;
                        position += readInt;
                        i9 = i2;
                        i10 = i3;
                        stsdData2 = stsdData;
                        str2 = str5;
                        drmInitData3 = drmInitData2;
                        i11 = i8;
                        readUnsignedShort2 = i7;
                    } else if (readInt2 == 1885434736) {
                        f2 = parsePaspFromParent(parsableByteArray, position2);
                        list2 = list;
                        bArr2 = bArr;
                        z = true;
                        position += readInt;
                        i9 = i2;
                        i10 = i3;
                        stsdData2 = stsdData;
                        str2 = str5;
                        drmInitData3 = drmInitData2;
                        i11 = i8;
                        readUnsignedShort2 = i7;
                    } else if (readInt2 == 1937126244) {
                        bArr2 = parseProjFromParent(parsableByteArray, position2, readInt);
                        list2 = list;
                        f2 = f;
                        position += readInt;
                        i9 = i2;
                        i10 = i3;
                        stsdData2 = stsdData;
                        str2 = str5;
                        drmInitData3 = drmInitData2;
                        i11 = i8;
                        readUnsignedShort2 = i7;
                    } else if (readInt2 == 1936995172) {
                        int readUnsignedByte = parsableByteArray.readUnsignedByte();
                        parsableByteArray.skipBytes(3);
                        if (readUnsignedByte == 0) {
                            int readUnsignedByte2 = parsableByteArray.readUnsignedByte();
                            if (readUnsignedByte2 == 0) {
                                i12 = 0;
                            } else if (readUnsignedByte2 == 1) {
                                i12 = 1;
                            } else if (readUnsignedByte2 == 2) {
                                i12 = 2;
                            } else if (readUnsignedByte2 == 3) {
                                i12 = 3;
                            }
                        }
                    } else if (readInt2 == 1668246642) {
                        int readInt3 = parsableByteArray.readInt();
                        if (readInt3 == 1852009592 || readInt3 == 1852009571) {
                            int readUnsignedShort3 = parsableByteArray.readUnsignedShort();
                            int readUnsignedShort4 = parsableByteArray.readUnsignedShort();
                            parsableByteArray.skipBytes(2);
                            boolean z2 = readInt == 19 && (parsableByteArray.readUnsignedByte() & 128) != 0;
                            i13 = ColorInfo.isoColorPrimariesToColorSpace(readUnsignedShort3);
                            i14 = z2 ? 1 : 2;
                            i15 = ColorInfo.isoTransferCharacteristicsToColorTransfer(readUnsignedShort4);
                        } else {
                            Log.w("AtomParsers", "Unsupported color type: " + Atom.getAtomTypeString(readInt3));
                        }
                    }
                }
                list2 = list;
                bArr2 = bArr;
                f2 = f;
                position += readInt;
                i9 = i2;
                i10 = i3;
                stsdData2 = stsdData;
                str2 = str5;
                drmInitData3 = drmInitData2;
                i11 = i8;
                readUnsignedShort2 = i7;
            }
            str3 = str;
            i7 = readUnsignedShort2;
            i8 = i11;
            position += readInt;
            i9 = i2;
            i10 = i3;
            stsdData2 = stsdData;
            str2 = str5;
            drmInitData3 = drmInitData2;
            i11 = i8;
            readUnsignedShort2 = i7;
        }
        int i16 = readUnsignedShort2;
        byte[] bArr5 = bArr2;
        float f4 = f2;
        List<byte[]> list4 = list2;
        if (str3 == null) {
            return;
        }
        Format.Builder drmInitData4 = new Format.Builder().setId(i4).setSampleMimeType(str3).setCodecs(str4).setWidth(readUnsignedShort).setHeight(i16).setPixelWidthHeightRatio(f4).setRotationDegrees(i5).setProjectionData(bArr5).setStereoMode(i12).setInitializationData(list4).setDrmInitData(drmInitData2);
        int i17 = i13;
        int i18 = i14;
        int i19 = i15;
        if (i17 != -1 || i18 != -1 || i19 != -1 || byteBuffer != null) {
            drmInitData4.setColorInfo(new ColorInfo(i17, i18, i19, byteBuffer != null ? byteBuffer.array() : null));
        }
        if (esdsData != null) {
            drmInitData4.setAverageBitrate(Ints.saturatedCast(esdsData.bitrate)).setPeakBitrate(Ints.saturatedCast(esdsData.peakBitrate));
        }
        stsdData.format = drmInitData4.build();
    }

    private static ByteBuffer allocateHdrStaticInfo() {
        return ByteBuffer.allocate(25).order(ByteOrder.LITTLE_ENDIAN);
    }

    private static void parseMetaDataSampleEntry(ParsableByteArray parsableByteArray, int i, int i2, int i3, StsdData stsdData) {
        parsableByteArray.setPosition(i2 + 8 + 8);
        if (i == 1835365492) {
            parsableByteArray.readNullTerminatedString();
            String readNullTerminatedString = parsableByteArray.readNullTerminatedString();
            if (readNullTerminatedString != null) {
                stsdData.format = new Format.Builder().setId(i3).setSampleMimeType(readNullTerminatedString).build();
            }
        }
    }

    private static Pair<long[], long[]> parseEdts(Atom.ContainerAtom containerAtom) {
        Atom.LeafAtom leafAtomOfType = containerAtom.getLeafAtomOfType(1701606260);
        if (leafAtomOfType == null) {
            return null;
        }
        ParsableByteArray parsableByteArray = leafAtomOfType.data;
        parsableByteArray.setPosition(8);
        int parseFullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
        int readUnsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
        long[] jArr = new long[readUnsignedIntToInt];
        long[] jArr2 = new long[readUnsignedIntToInt];
        for (int i = 0; i < readUnsignedIntToInt; i++) {
            jArr[i] = parseFullAtomVersion == 1 ? parsableByteArray.readUnsignedLongToLong() : parsableByteArray.readUnsignedInt();
            jArr2[i] = parseFullAtomVersion == 1 ? parsableByteArray.readLong() : parsableByteArray.readInt();
            if (parsableByteArray.readShort() != 1) {
                throw new IllegalArgumentException("Unsupported media rate.");
            }
            parsableByteArray.skipBytes(2);
        }
        return Pair.create(jArr, jArr2);
    }

    private static float parsePaspFromParent(ParsableByteArray parsableByteArray, int i) {
        parsableByteArray.setPosition(i + 8);
        return parsableByteArray.readUnsignedIntToInt() / parsableByteArray.readUnsignedIntToInt();
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x0166  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void parseAudioSampleEntry(ParsableByteArray parsableByteArray, int i, int i2, int i3, int i4, String str, boolean z, DrmInitData drmInitData, StsdData stsdData, int i5) throws ParserException {
        int i6;
        int readInt;
        int i7;
        int i8;
        int i9;
        int i10 = i2;
        int i11 = i3;
        DrmInitData drmInitData2 = drmInitData;
        parsableByteArray.setPosition(i10 + 8 + 8);
        if (z) {
            i6 = parsableByteArray.readUnsignedShort();
            parsableByteArray.skipBytes(6);
        } else {
            parsableByteArray.skipBytes(8);
            i6 = 0;
        }
        if (i6 == 0 || i6 == 1) {
            int readUnsignedShort = parsableByteArray.readUnsignedShort();
            parsableByteArray.skipBytes(6);
            int readUnsignedFixedPoint1616 = parsableByteArray.readUnsignedFixedPoint1616();
            parsableByteArray.setPosition(parsableByteArray.getPosition() - 4);
            readInt = parsableByteArray.readInt();
            if (i6 == 1) {
                parsableByteArray.skipBytes(16);
            }
            i7 = readUnsignedFixedPoint1616;
            i8 = readUnsignedShort;
        } else if (i6 != 2) {
            return;
        } else {
            parsableByteArray.skipBytes(16);
            i7 = (int) Math.round(parsableByteArray.readDouble());
            i8 = parsableByteArray.readUnsignedIntToInt();
            parsableByteArray.skipBytes(20);
            readInt = 0;
        }
        int position = parsableByteArray.getPosition();
        int i12 = i;
        if (i12 == 1701733217) {
            Pair<Integer, TrackEncryptionBox> parseSampleEntryEncryptionData = parseSampleEntryEncryptionData(parsableByteArray, i10, i11);
            if (parseSampleEntryEncryptionData != null) {
                i12 = ((Integer) parseSampleEntryEncryptionData.first).intValue();
                drmInitData2 = drmInitData2 == null ? null : drmInitData2.copyWithSchemeType(((TrackEncryptionBox) parseSampleEntryEncryptionData.second).schemeType);
                stsdData.trackEncryptionBoxes[i5] = (TrackEncryptionBox) parseSampleEntryEncryptionData.second;
            }
            parsableByteArray.setPosition(position);
        }
        String str2 = "audio/raw";
        if (i12 == 1633889587) {
            str2 = "audio/ac3";
        } else if (i12 == 1700998451) {
            str2 = "audio/eac3";
        } else if (i12 == 1633889588) {
            str2 = "audio/ac4";
        } else if (i12 == 1685353315) {
            str2 = "audio/vnd.dts";
        } else if (i12 == 1685353320 || i12 == 1685353324) {
            str2 = "audio/vnd.dts.hd";
        } else if (i12 == 1685353317) {
            str2 = "audio/vnd.dts.hd;profile=lbr";
        } else if (i12 == 1685353336) {
            str2 = "audio/vnd.dts.uhd;profile=p2";
        } else if (i12 == 1935764850) {
            str2 = "audio/3gpp";
        } else if (i12 != 1935767394) {
            if (i12 == 1819304813 || i12 == 1936684916) {
                i9 = 2;
            } else if (i12 == 1953984371) {
                i9 = 268435456;
            } else if (i12 == 778924082 || i12 == 778924083) {
                str2 = "audio/mpeg";
            } else if (i12 == 1835557169) {
                str2 = "audio/mha1";
            } else if (i12 == 1835560241) {
                str2 = "audio/mhm1";
            } else if (i12 == 1634492771) {
                str2 = "audio/alac";
            } else if (i12 == 1634492791) {
                str2 = "audio/g711-alaw";
            } else if (i12 == 1970037111) {
                str2 = "audio/g711-mlaw";
            } else if (i12 == 1332770163) {
                str2 = "audio/opus";
            } else if (i12 == 1716281667) {
                str2 = "audio/flac";
            } else if (i12 == 1835823201) {
                str2 = "audio/true-hd";
            } else {
                i9 = -1;
                str2 = null;
            }
            String str3 = str2;
            EsdsData esdsData = null;
            String str4 = null;
            List<byte[]> list = null;
            while (position - i10 < i11) {
                parsableByteArray.setPosition(position);
                int readInt2 = parsableByteArray.readInt();
                ExtractorUtil.checkContainerInput(readInt2 > 0, "childAtomSize must be positive");
                int readInt3 = parsableByteArray.readInt();
                if (readInt3 == 1835557187) {
                    int i13 = readInt2 - 13;
                    byte[] bArr = new byte[i13];
                    parsableByteArray.setPosition(position + 13);
                    parsableByteArray.readBytes(bArr, 0, i13);
                    list = ImmutableList.of(bArr);
                } else if (readInt3 == 1702061171 || (z && readInt3 == 2002876005)) {
                    int findBoxPosition = readInt3 == 1702061171 ? position : findBoxPosition(parsableByteArray, 1702061171, position, readInt2);
                    if (findBoxPosition != -1) {
                        esdsData = parseEsdsFromParent(parsableByteArray, findBoxPosition);
                        str3 = esdsData.mimeType;
                        byte[] bArr2 = esdsData.initializationData;
                        if (bArr2 != null) {
                            if (MediaController.AUIDO_MIME_TYPE.equals(str3)) {
                                AacUtil.Config parseAudioSpecificConfig = AacUtil.parseAudioSpecificConfig(bArr2);
                                i7 = parseAudioSpecificConfig.sampleRateHz;
                                i8 = parseAudioSpecificConfig.channelCount;
                                str4 = parseAudioSpecificConfig.codecs;
                            }
                            list = ImmutableList.of(bArr2);
                        }
                    }
                    position += readInt2;
                    i10 = i2;
                    i11 = i3;
                } else {
                    if (readInt3 == 1684103987) {
                        parsableByteArray.setPosition(position + 8);
                        stsdData.format = Ac3Util.parseAc3AnnexFFormat(parsableByteArray, Integer.toString(i4), str, drmInitData2);
                    } else if (readInt3 == 1684366131) {
                        parsableByteArray.setPosition(position + 8);
                        stsdData.format = Ac3Util.parseEAc3AnnexFFormat(parsableByteArray, Integer.toString(i4), str, drmInitData2);
                    } else if (readInt3 == 1684103988) {
                        parsableByteArray.setPosition(position + 8);
                        stsdData.format = Ac4Util.parseAc4AnnexEFormat(parsableByteArray, Integer.toString(i4), str, drmInitData2);
                    } else if (readInt3 == 1684892784) {
                        if (readInt <= 0) {
                            throw ParserException.createForMalformedContainer("Invalid sample rate for Dolby TrueHD MLP stream: " + readInt, null);
                        }
                        i7 = readInt;
                        i8 = 2;
                    } else if (readInt3 == 1684305011) {
                        stsdData.format = new Format.Builder().setId(i4).setSampleMimeType(str3).setChannelCount(i8).setSampleRate(i7).setDrmInitData(drmInitData2).setLanguage(str).build();
                    } else if (readInt3 == 1682927731) {
                        int i14 = readInt2 - 8;
                        byte[] bArr3 = opusMagic;
                        byte[] copyOf = Arrays.copyOf(bArr3, bArr3.length + i14);
                        parsableByteArray.setPosition(position + 8);
                        parsableByteArray.readBytes(copyOf, bArr3.length, i14);
                        list = OpusUtil.buildInitializationData(copyOf);
                    } else if (readInt3 == 1684425825) {
                        int i15 = readInt2 - 12;
                        byte[] bArr4 = new byte[i15 + 4];
                        bArr4[0] = 102;
                        bArr4[1] = 76;
                        bArr4[2] = 97;
                        bArr4[3] = 67;
                        parsableByteArray.setPosition(position + 12);
                        parsableByteArray.readBytes(bArr4, 4, i15);
                        list = ImmutableList.of(bArr4);
                        position += readInt2;
                        i10 = i2;
                        i11 = i3;
                    } else {
                        if (readInt3 == 1634492771) {
                            int i16 = readInt2 - 12;
                            byte[] bArr5 = new byte[i16];
                            parsableByteArray.setPosition(position + 12);
                            parsableByteArray.readBytes(bArr5, 0, i16);
                            Pair<Integer, Integer> parseAlacAudioSpecificConfig = CodecSpecificDataUtil.parseAlacAudioSpecificConfig(bArr5);
                            i7 = ((Integer) parseAlacAudioSpecificConfig.first).intValue();
                            int intValue = ((Integer) parseAlacAudioSpecificConfig.second).intValue();
                            list = ImmutableList.of(bArr5);
                            i8 = intValue;
                        }
                        position += readInt2;
                        i10 = i2;
                        i11 = i3;
                    }
                    position += readInt2;
                    i10 = i2;
                    i11 = i3;
                }
                position += readInt2;
                i10 = i2;
                i11 = i3;
            }
            if (stsdData.format == null || str3 == null) {
            }
            Format.Builder language = new Format.Builder().setId(i4).setSampleMimeType(str3).setCodecs(str4).setChannelCount(i8).setSampleRate(i7).setPcmEncoding(i9).setInitializationData(list).setDrmInitData(drmInitData2).setLanguage(str);
            if (esdsData != null) {
                language.setAverageBitrate(Ints.saturatedCast(esdsData.bitrate)).setPeakBitrate(Ints.saturatedCast(esdsData.peakBitrate));
            }
            stsdData.format = language.build();
            return;
        } else {
            str2 = "audio/amr-wb";
        }
        i9 = -1;
        String str32 = str2;
        EsdsData esdsData2 = null;
        String str42 = null;
        List<byte[]> list2 = null;
        while (position - i10 < i11) {
        }
        if (stsdData.format == null) {
        }
    }

    private static int findBoxPosition(ParsableByteArray parsableByteArray, int i, int i2, int i3) throws ParserException {
        int position = parsableByteArray.getPosition();
        ExtractorUtil.checkContainerInput(position >= i2, null);
        while (position - i2 < i3) {
            parsableByteArray.setPosition(position);
            int readInt = parsableByteArray.readInt();
            ExtractorUtil.checkContainerInput(readInt > 0, "childAtomSize must be positive");
            if (parsableByteArray.readInt() == i) {
                return position;
            }
            position += readInt;
        }
        return -1;
    }

    private static EsdsData parseEsdsFromParent(ParsableByteArray parsableByteArray, int i) {
        parsableByteArray.setPosition(i + 8 + 4);
        parsableByteArray.skipBytes(1);
        parseExpandableClassSize(parsableByteArray);
        parsableByteArray.skipBytes(2);
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        if ((readUnsignedByte & 128) != 0) {
            parsableByteArray.skipBytes(2);
        }
        if ((readUnsignedByte & 64) != 0) {
            parsableByteArray.skipBytes(parsableByteArray.readUnsignedByte());
        }
        if ((readUnsignedByte & 32) != 0) {
            parsableByteArray.skipBytes(2);
        }
        parsableByteArray.skipBytes(1);
        parseExpandableClassSize(parsableByteArray);
        String mimeTypeFromMp4ObjectType = MimeTypes.getMimeTypeFromMp4ObjectType(parsableByteArray.readUnsignedByte());
        if ("audio/mpeg".equals(mimeTypeFromMp4ObjectType) || "audio/vnd.dts".equals(mimeTypeFromMp4ObjectType) || "audio/vnd.dts.hd".equals(mimeTypeFromMp4ObjectType)) {
            return new EsdsData(mimeTypeFromMp4ObjectType, null, -1L, -1L);
        }
        parsableByteArray.skipBytes(4);
        long readUnsignedInt = parsableByteArray.readUnsignedInt();
        long readUnsignedInt2 = parsableByteArray.readUnsignedInt();
        parsableByteArray.skipBytes(1);
        int parseExpandableClassSize = parseExpandableClassSize(parsableByteArray);
        byte[] bArr = new byte[parseExpandableClassSize];
        parsableByteArray.readBytes(bArr, 0, parseExpandableClassSize);
        return new EsdsData(mimeTypeFromMp4ObjectType, bArr, readUnsignedInt2 > 0 ? readUnsignedInt2 : -1L, readUnsignedInt > 0 ? readUnsignedInt : -1L);
    }

    private static Pair<Integer, TrackEncryptionBox> parseSampleEntryEncryptionData(ParsableByteArray parsableByteArray, int i, int i2) throws ParserException {
        Pair<Integer, TrackEncryptionBox> parseCommonEncryptionSinfFromParent;
        int position = parsableByteArray.getPosition();
        while (position - i < i2) {
            parsableByteArray.setPosition(position);
            int readInt = parsableByteArray.readInt();
            ExtractorUtil.checkContainerInput(readInt > 0, "childAtomSize must be positive");
            if (parsableByteArray.readInt() == 1936289382 && (parseCommonEncryptionSinfFromParent = parseCommonEncryptionSinfFromParent(parsableByteArray, position, readInt)) != null) {
                return parseCommonEncryptionSinfFromParent;
            }
            position += readInt;
        }
        return null;
    }

    static Pair<Integer, TrackEncryptionBox> parseCommonEncryptionSinfFromParent(ParsableByteArray parsableByteArray, int i, int i2) throws ParserException {
        int i3 = i + 8;
        String str = null;
        Integer num = null;
        int i4 = -1;
        int i5 = 0;
        while (i3 - i < i2) {
            parsableByteArray.setPosition(i3);
            int readInt = parsableByteArray.readInt();
            int readInt2 = parsableByteArray.readInt();
            if (readInt2 == 1718775137) {
                num = Integer.valueOf(parsableByteArray.readInt());
            } else if (readInt2 == 1935894637) {
                parsableByteArray.skipBytes(4);
                str = parsableByteArray.readString(4);
            } else if (readInt2 == 1935894633) {
                i4 = i3;
                i5 = readInt;
            }
            i3 += readInt;
        }
        if ("cenc".equals(str) || "cbc1".equals(str) || "cens".equals(str) || "cbcs".equals(str)) {
            ExtractorUtil.checkContainerInput(num != null, "frma atom is mandatory");
            ExtractorUtil.checkContainerInput(i4 != -1, "schi atom is mandatory");
            TrackEncryptionBox parseSchiFromParent = parseSchiFromParent(parsableByteArray, i4, i5, str);
            ExtractorUtil.checkContainerInput(parseSchiFromParent != null, "tenc atom is mandatory");
            return Pair.create(num, (TrackEncryptionBox) Util.castNonNull(parseSchiFromParent));
        }
        return null;
    }

    private static TrackEncryptionBox parseSchiFromParent(ParsableByteArray parsableByteArray, int i, int i2, String str) {
        int i3;
        int i4;
        int i5 = i + 8;
        while (true) {
            byte[] bArr = null;
            if (i5 - i >= i2) {
                return null;
            }
            parsableByteArray.setPosition(i5);
            int readInt = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == 1952804451) {
                int parseFullAtomVersion = Atom.parseFullAtomVersion(parsableByteArray.readInt());
                parsableByteArray.skipBytes(1);
                if (parseFullAtomVersion == 0) {
                    parsableByteArray.skipBytes(1);
                    i4 = 0;
                    i3 = 0;
                } else {
                    int readUnsignedByte = parsableByteArray.readUnsignedByte();
                    i3 = readUnsignedByte & 15;
                    i4 = (readUnsignedByte & 240) >> 4;
                }
                boolean z = parsableByteArray.readUnsignedByte() == 1;
                int readUnsignedByte2 = parsableByteArray.readUnsignedByte();
                byte[] bArr2 = new byte[16];
                parsableByteArray.readBytes(bArr2, 0, 16);
                if (z && readUnsignedByte2 == 0) {
                    int readUnsignedByte3 = parsableByteArray.readUnsignedByte();
                    bArr = new byte[readUnsignedByte3];
                    parsableByteArray.readBytes(bArr, 0, readUnsignedByte3);
                }
                return new TrackEncryptionBox(z, str, readUnsignedByte2, bArr2, i4, i3, bArr);
            }
            i5 += readInt;
        }
    }

    private static byte[] parseProjFromParent(ParsableByteArray parsableByteArray, int i, int i2) {
        int i3 = i + 8;
        while (i3 - i < i2) {
            parsableByteArray.setPosition(i3);
            int readInt = parsableByteArray.readInt();
            if (parsableByteArray.readInt() == 1886547818) {
                return Arrays.copyOfRange(parsableByteArray.getData(), i3, readInt + i3);
            }
            i3 += readInt;
        }
        return null;
    }

    private static int parseExpandableClassSize(ParsableByteArray parsableByteArray) {
        int readUnsignedByte = parsableByteArray.readUnsignedByte();
        int i = readUnsignedByte & MessagesStorage.LAST_DB_VERSION;
        while ((readUnsignedByte & 128) == 128) {
            readUnsignedByte = parsableByteArray.readUnsignedByte();
            i = (i << 7) | (readUnsignedByte & MessagesStorage.LAST_DB_VERSION);
        }
        return i;
    }

    private static boolean canApplyEditWithGaplessInfo(long[] jArr, long j, long j2, long j3) {
        int length = jArr.length - 1;
        return jArr[0] <= j2 && j2 < jArr[Util.constrainValue(4, 0, length)] && jArr[Util.constrainValue(jArr.length - 4, 0, length)] < j3 && j3 <= j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ChunkIterator {
        private final ParsableByteArray chunkOffsets;
        private final boolean chunkOffsetsAreLongs;
        public int index;
        public final int length;
        private int nextSamplesPerChunkChangeIndex;
        public int numSamples;
        public long offset;
        private int remainingSamplesPerChunkChanges;
        private final ParsableByteArray stsc;

        public ChunkIterator(ParsableByteArray parsableByteArray, ParsableByteArray parsableByteArray2, boolean z) throws ParserException {
            this.stsc = parsableByteArray;
            this.chunkOffsets = parsableByteArray2;
            this.chunkOffsetsAreLongs = z;
            parsableByteArray2.setPosition(12);
            this.length = parsableByteArray2.readUnsignedIntToInt();
            parsableByteArray.setPosition(12);
            this.remainingSamplesPerChunkChanges = parsableByteArray.readUnsignedIntToInt();
            ExtractorUtil.checkContainerInput(parsableByteArray.readInt() == 1, "first_chunk must be 1");
            this.index = -1;
        }

        public boolean moveNext() {
            long readUnsignedInt;
            int i = this.index + 1;
            this.index = i;
            if (i == this.length) {
                return false;
            }
            if (this.chunkOffsetsAreLongs) {
                readUnsignedInt = this.chunkOffsets.readUnsignedLongToLong();
            } else {
                readUnsignedInt = this.chunkOffsets.readUnsignedInt();
            }
            this.offset = readUnsignedInt;
            if (this.index == this.nextSamplesPerChunkChangeIndex) {
                this.numSamples = this.stsc.readUnsignedIntToInt();
                this.stsc.skipBytes(4);
                int i2 = this.remainingSamplesPerChunkChanges - 1;
                this.remainingSamplesPerChunkChanges = i2;
                this.nextSamplesPerChunkChangeIndex = i2 > 0 ? this.stsc.readUnsignedIntToInt() - 1 : -1;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class TkhdData {
        private final long duration;
        private final int id;
        private final int rotationDegrees;

        public TkhdData(int i, long j, int i2) {
            this.id = i;
            this.duration = j;
            this.rotationDegrees = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class StsdData {
        public Format format;
        public int nalUnitLengthFieldLength;
        public int requiredSampleTransformation = 0;
        public final TrackEncryptionBox[] trackEncryptionBoxes;

        public StsdData(int i) {
            this.trackEncryptionBoxes = new TrackEncryptionBox[i];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class EsdsData {
        private final long bitrate;
        private final byte[] initializationData;
        private final String mimeType;
        private final long peakBitrate;

        public EsdsData(String str, byte[] bArr, long j, long j2) {
            this.mimeType = str;
            this.initializationData = bArr;
            this.bitrate = j;
            this.peakBitrate = j2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class StszSampleSizeBox implements SampleSizeBox {
        private final ParsableByteArray data;
        private final int fixedSampleSize;
        private final int sampleCount;

        public StszSampleSizeBox(Atom.LeafAtom leafAtom, Format format) {
            ParsableByteArray parsableByteArray = leafAtom.data;
            this.data = parsableByteArray;
            parsableByteArray.setPosition(12);
            int readUnsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
            if ("audio/raw".equals(format.sampleMimeType)) {
                int pcmFrameSize = Util.getPcmFrameSize(format.pcmEncoding, format.channelCount);
                if (readUnsignedIntToInt == 0 || readUnsignedIntToInt % pcmFrameSize != 0) {
                    Log.w("AtomParsers", "Audio sample size mismatch. stsd sample size: " + pcmFrameSize + ", stsz sample size: " + readUnsignedIntToInt);
                    readUnsignedIntToInt = pcmFrameSize;
                }
            }
            this.fixedSampleSize = readUnsignedIntToInt == 0 ? -1 : readUnsignedIntToInt;
            this.sampleCount = parsableByteArray.readUnsignedIntToInt();
        }

        @Override // com.google.android.exoplayer2.extractor.mp4.AtomParsers.SampleSizeBox
        public int getSampleCount() {
            return this.sampleCount;
        }

        @Override // com.google.android.exoplayer2.extractor.mp4.AtomParsers.SampleSizeBox
        public int getFixedSampleSize() {
            return this.fixedSampleSize;
        }

        @Override // com.google.android.exoplayer2.extractor.mp4.AtomParsers.SampleSizeBox
        public int readNextSampleSize() {
            int i = this.fixedSampleSize;
            return i == -1 ? this.data.readUnsignedIntToInt() : i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class Stz2SampleSizeBox implements SampleSizeBox {
        private int currentByte;
        private final ParsableByteArray data;
        private final int fieldSize;
        private final int sampleCount;
        private int sampleIndex;

        @Override // com.google.android.exoplayer2.extractor.mp4.AtomParsers.SampleSizeBox
        public int getFixedSampleSize() {
            return -1;
        }

        public Stz2SampleSizeBox(Atom.LeafAtom leafAtom) {
            ParsableByteArray parsableByteArray = leafAtom.data;
            this.data = parsableByteArray;
            parsableByteArray.setPosition(12);
            this.fieldSize = parsableByteArray.readUnsignedIntToInt() & 255;
            this.sampleCount = parsableByteArray.readUnsignedIntToInt();
        }

        @Override // com.google.android.exoplayer2.extractor.mp4.AtomParsers.SampleSizeBox
        public int getSampleCount() {
            return this.sampleCount;
        }

        @Override // com.google.android.exoplayer2.extractor.mp4.AtomParsers.SampleSizeBox
        public int readNextSampleSize() {
            int i = this.fieldSize;
            if (i == 8) {
                return this.data.readUnsignedByte();
            }
            if (i == 16) {
                return this.data.readUnsignedShort();
            }
            int i2 = this.sampleIndex;
            this.sampleIndex = i2 + 1;
            if (i2 % 2 == 0) {
                int readUnsignedByte = this.data.readUnsignedByte();
                this.currentByte = readUnsignedByte;
                return (readUnsignedByte & 240) >> 4;
            }
            return this.currentByte & 15;
        }
    }
}
