package com.google.common.primitives;

import com.google.common.base.Preconditions;
/* loaded from: classes.dex */
public final class Longs {
    public static int hashCode(long j) {
        return (int) (j ^ (j >>> 32));
    }

    public static long max(long... jArr) {
        Preconditions.checkArgument(jArr.length > 0);
        long j = jArr[0];
        for (int i = 1; i < jArr.length; i++) {
            if (jArr[i] > j) {
                j = jArr[i];
            }
        }
        return j;
    }
}
