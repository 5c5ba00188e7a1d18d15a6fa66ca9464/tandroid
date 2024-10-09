package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.ParserException;
import java.io.EOFException;

/* loaded from: classes.dex */
public abstract class ExtractorUtil {
    public static void checkContainerInput(boolean z, String str) {
        if (!z) {
            throw ParserException.createForMalformedContainer(str, null);
        }
    }

    public static boolean peekFullyQuietly(ExtractorInput extractorInput, byte[] bArr, int i, int i2, boolean z) {
        try {
            return extractorInput.peekFully(bArr, i, i2, z);
        } catch (EOFException e) {
            if (z) {
                return false;
            }
            throw e;
        }
    }

    public static int peekToLength(ExtractorInput extractorInput, byte[] bArr, int i, int i2) {
        int i3 = 0;
        while (i3 < i2) {
            int peek = extractorInput.peek(bArr, i + i3, i2 - i3);
            if (peek == -1) {
                break;
            }
            i3 += peek;
        }
        return i3;
    }

    public static boolean readFullyQuietly(ExtractorInput extractorInput, byte[] bArr, int i, int i2) {
        try {
            extractorInput.readFully(bArr, i, i2);
            return true;
        } catch (EOFException unused) {
            return false;
        }
    }

    public static boolean skipFullyQuietly(ExtractorInput extractorInput, int i) {
        try {
            extractorInput.skipFully(i);
            return true;
        } catch (EOFException unused) {
            return false;
        }
    }
}
