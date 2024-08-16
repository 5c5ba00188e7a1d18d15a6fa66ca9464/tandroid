package com.google.android.exoplayer2.decoder;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.Assertions;
/* loaded from: classes.dex */
public final class DecoderReuseEvaluation {
    public final String decoderName;
    public final int discardReasons;
    public final Format newFormat;
    public final Format oldFormat;
    public final int result;

    public DecoderReuseEvaluation(String str, Format format, Format format2, int i, int i2) {
        Assertions.checkArgument(i == 0 || i2 == 0);
        this.decoderName = Assertions.checkNotEmpty(str);
        this.oldFormat = (Format) Assertions.checkNotNull(format);
        this.newFormat = (Format) Assertions.checkNotNull(format2);
        this.result = i;
        this.discardReasons = i2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || DecoderReuseEvaluation.class != obj.getClass()) {
            return false;
        }
        DecoderReuseEvaluation decoderReuseEvaluation = (DecoderReuseEvaluation) obj;
        return this.result == decoderReuseEvaluation.result && this.discardReasons == decoderReuseEvaluation.discardReasons && this.decoderName.equals(decoderReuseEvaluation.decoderName) && this.oldFormat.equals(decoderReuseEvaluation.oldFormat) && this.newFormat.equals(decoderReuseEvaluation.newFormat);
    }

    public int hashCode() {
        return ((((((((this.result + 527) * 31) + this.discardReasons) * 31) + this.decoderName.hashCode()) * 31) + this.oldFormat.hashCode()) * 31) + this.newFormat.hashCode();
    }
}
