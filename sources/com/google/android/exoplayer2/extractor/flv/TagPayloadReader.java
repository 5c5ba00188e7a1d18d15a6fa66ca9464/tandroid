package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;

/* loaded from: classes.dex */
abstract class TagPayloadReader {
    protected final TrackOutput output;

    /* loaded from: classes.dex */
    public static final class UnsupportedFormatException extends ParserException {
        public UnsupportedFormatException(String str) {
            super(str, null, false, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public TagPayloadReader(TrackOutput trackOutput) {
        this.output = trackOutput;
    }

    public final boolean consume(ParsableByteArray parsableByteArray, long j) {
        return parseHeader(parsableByteArray) && parsePayload(parsableByteArray, j);
    }

    protected abstract boolean parseHeader(ParsableByteArray parsableByteArray);

    protected abstract boolean parsePayload(ParsableByteArray parsableByteArray, long j);
}
