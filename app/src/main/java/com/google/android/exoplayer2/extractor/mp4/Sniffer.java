package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;
/* loaded from: classes3.dex */
final class Sniffer {
    private static final int[] COMPATIBLE_BRANDS = {1769172845, 1769172786, 1769172787, 1769172788, 1769172789, 1769172790, Atom.TYPE_avc1, Atom.TYPE_hvc1, Atom.TYPE_hev1, Atom.TYPE_av01, 1836069937, 1836069938, 862401121, 862401122, 862417462, 862417718, 862414134, 862414646, 1295275552, 1295270176, 1714714144, 1801741417, 1295275600, 1903435808, 1297305174, 1684175153};
    private static final int SEARCH_LENGTH = 4096;

    public static boolean sniffFragmented(ExtractorInput input) throws IOException, InterruptedException {
        return sniffInternal(input, true);
    }

    public static boolean sniffUnfragmented(ExtractorInput input) throws IOException, InterruptedException {
        return sniffInternal(input, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:56:0x00db, code lost:
        r9 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00de, code lost:
        if (r8 == false) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00e2, code lost:
        if (r23 != r9) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00e4, code lost:
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00e8, code lost:
        return r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:?, code lost:
        return r10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static boolean sniffInternal(ExtractorInput input, boolean fragmented) throws IOException, InterruptedException {
        boolean z;
        long inputLength = input.getLength();
        long j = 4096;
        long j2 = -1;
        if (inputLength != -1 && inputLength <= 4096) {
            j = inputLength;
        }
        int bytesToSearch = (int) j;
        ParsableByteArray buffer = new ParsableByteArray(64);
        int bytesSearched = 0;
        boolean foundGoodFileType = false;
        boolean isFragmented = false;
        while (true) {
            if (bytesSearched >= bytesToSearch) {
                z = false;
                break;
            }
            int headerSize = 8;
            buffer.reset(8);
            input.peekFully(buffer.data, 0, 8);
            long atomSize = buffer.readUnsignedInt();
            int atomType = buffer.readInt();
            if (atomSize == 1) {
                headerSize = 16;
                input.peekFully(buffer.data, 8, 8);
                buffer.setLimit(16);
                atomSize = buffer.readLong();
            } else if (atomSize == 0) {
                long fileEndPosition = input.getLength();
                if (fileEndPosition != j2) {
                    atomSize = (fileEndPosition - input.getPeekPosition()) + 8;
                }
            }
            if (atomSize < headerSize) {
                return false;
            }
            bytesSearched += headerSize;
            if (atomType == 1836019574) {
                bytesToSearch += (int) atomSize;
                if (inputLength != j2 && bytesToSearch > inputLength) {
                    bytesToSearch = (int) inputLength;
                }
            } else if (atomType == 1836019558) {
                z = false;
                break;
            } else if (atomType == 1836475768) {
                z = false;
                break;
            } else if ((bytesSearched + atomSize) - headerSize >= bytesToSearch) {
                z = false;
                break;
            } else {
                int atomDataSize = (int) (atomSize - headerSize);
                bytesSearched += atomDataSize;
                if (atomType == 1718909296) {
                    if (atomDataSize >= 8) {
                        buffer.reset(atomDataSize);
                        input.peekFully(buffer.data, 0, atomDataSize);
                        int brandsCount = atomDataSize / 4;
                        int i = 0;
                        while (true) {
                            if (i >= brandsCount) {
                                break;
                            }
                            if (i == 1) {
                                buffer.skipBytes(4);
                            } else if (isCompatibleBrand(buffer.readInt())) {
                                foundGoodFileType = true;
                                break;
                            }
                            i++;
                        }
                        if (!foundGoodFileType) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else if (atomDataSize != 0) {
                    input.advancePeekPosition(atomDataSize);
                }
                j2 = -1;
            }
        }
    }

    private static boolean isCompatibleBrand(int brand) {
        int[] iArr;
        if ((brand >>> 8) == 3368816) {
            return true;
        }
        for (int compatibleBrand : COMPATIBLE_BRANDS) {
            if (compatibleBrand == brand) {
                return true;
            }
        }
        return false;
    }

    private Sniffer() {
    }
}
