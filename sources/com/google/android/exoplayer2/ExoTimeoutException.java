package com.google.android.exoplayer2;
/* loaded from: classes.dex */
public final class ExoTimeoutException extends RuntimeException {
    public final int timeoutOperation;

    public ExoTimeoutException(int i) {
        super(getErrorMessage(i));
        this.timeoutOperation = i;
    }

    private static String getErrorMessage(int i) {
        if (i != 1) {
            if (i != 2) {
                if (i == 3) {
                    return "Detaching surface timed out.";
                }
                return "Undefined timeout.";
            }
            return "Setting foreground mode timed out.";
        }
        return "Player release timed out.";
    }
}
