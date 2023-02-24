package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.util.Util;
/* loaded from: classes.dex */
public class PlaybackException extends Exception implements Bundleable {
    public final int errorCode;
    public final long timestampMs;
    private static final String FIELD_INT_ERROR_CODE = Util.intToStringMaxRadix(0);
    private static final String FIELD_LONG_TIMESTAMP_MS = Util.intToStringMaxRadix(1);
    private static final String FIELD_STRING_MESSAGE = Util.intToStringMaxRadix(2);
    private static final String FIELD_STRING_CAUSE_CLASS_NAME = Util.intToStringMaxRadix(3);
    private static final String FIELD_STRING_CAUSE_MESSAGE = Util.intToStringMaxRadix(4);

    /* JADX INFO: Access modifiers changed from: protected */
    public PlaybackException(String str, Throwable th, int i, long j) {
        super(str, th);
        this.errorCode = i;
        this.timestampMs = j;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(FIELD_INT_ERROR_CODE, this.errorCode);
        bundle.putLong(FIELD_LONG_TIMESTAMP_MS, this.timestampMs);
        bundle.putString(FIELD_STRING_MESSAGE, getMessage());
        Throwable cause = getCause();
        if (cause != null) {
            bundle.putString(FIELD_STRING_CAUSE_CLASS_NAME, cause.getClass().getName());
            bundle.putString(FIELD_STRING_CAUSE_MESSAGE, cause.getMessage());
        }
        return bundle;
    }
}
