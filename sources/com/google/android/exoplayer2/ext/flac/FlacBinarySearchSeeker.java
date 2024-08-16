package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.ext.flac.FlacDecoderJni;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.FlacStreamMetadata;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
/* loaded from: classes.dex */
final class FlacBinarySearchSeeker extends BinarySearchSeeker {
    private static final int MIN_FRAME_HEADER_SIZE = 6;
    private final FlacDecoderJni decoderJni;

    /* loaded from: classes.dex */
    public static final class OutputFrameHolder {
        public final ByteBuffer byteBuffer;
        public long timeUs = 0;

        public OutputFrameHolder(ByteBuffer byteBuffer) {
            this.byteBuffer = byteBuffer;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FlacBinarySearchSeeker(FlacStreamMetadata flacStreamMetadata, long j, long j2, FlacDecoderJni flacDecoderJni, OutputFrameHolder outputFrameHolder) {
        super(new FlacBinarySearchSeeker$$ExternalSyntheticLambda0(flacStreamMetadata), new FlacTimestampSeeker(flacDecoderJni, outputFrameHolder), flacStreamMetadata.getDurationUs(), 0L, flacStreamMetadata.totalSamples, j, j2, flacStreamMetadata.getApproxBytesPerFrame(), Math.max(6, flacStreamMetadata.minFrameSize));
        Objects.requireNonNull(flacStreamMetadata);
        this.decoderJni = (FlacDecoderJni) Assertions.checkNotNull(flacDecoderJni);
    }

    @Override // com.google.android.exoplayer2.extractor.BinarySearchSeeker
    protected void onSeekOperationFinished(boolean z, long j) {
        if (z) {
            return;
        }
        this.decoderJni.reset(j);
    }

    /* loaded from: classes.dex */
    private static final class FlacTimestampSeeker implements BinarySearchSeeker.TimestampSeeker {
        private final FlacDecoderJni decoderJni;
        private final OutputFrameHolder outputFrameHolder;

        @Override // com.google.android.exoplayer2.extractor.BinarySearchSeeker.TimestampSeeker
        public /* bridge */ /* synthetic */ void onSeekFinished() {
            BinarySearchSeeker.TimestampSeeker.-CC.$default$onSeekFinished(this);
        }

        private FlacTimestampSeeker(FlacDecoderJni flacDecoderJni, OutputFrameHolder outputFrameHolder) {
            this.decoderJni = flacDecoderJni;
            this.outputFrameHolder = outputFrameHolder;
        }

        @Override // com.google.android.exoplayer2.extractor.BinarySearchSeeker.TimestampSeeker
        public BinarySearchSeeker.TimestampSearchResult searchForTimestamp(ExtractorInput extractorInput, long j) throws IOException {
            ByteBuffer byteBuffer = this.outputFrameHolder.byteBuffer;
            long position = extractorInput.getPosition();
            this.decoderJni.reset(position);
            try {
                this.decoderJni.decodeSampleWithBacktrackPosition(byteBuffer, position);
                if (byteBuffer.limit() == 0) {
                    return BinarySearchSeeker.TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
                }
                long lastFrameFirstSampleIndex = this.decoderJni.getLastFrameFirstSampleIndex();
                long nextFrameFirstSampleIndex = this.decoderJni.getNextFrameFirstSampleIndex();
                long decodePosition = this.decoderJni.getDecodePosition();
                if (lastFrameFirstSampleIndex > j || nextFrameFirstSampleIndex <= j) {
                    if (nextFrameFirstSampleIndex <= j) {
                        return BinarySearchSeeker.TimestampSearchResult.underestimatedResult(nextFrameFirstSampleIndex, decodePosition);
                    }
                    return BinarySearchSeeker.TimestampSearchResult.overestimatedResult(lastFrameFirstSampleIndex, position);
                }
                this.outputFrameHolder.timeUs = this.decoderJni.getLastFrameTimestamp();
                return BinarySearchSeeker.TimestampSearchResult.targetFoundResult(extractorInput.getPosition());
            } catch (FlacDecoderJni.FlacFrameDecodeException unused) {
                return BinarySearchSeeker.TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
            }
        }
    }
}
