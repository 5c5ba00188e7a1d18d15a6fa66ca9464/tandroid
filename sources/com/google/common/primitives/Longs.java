package com.google.common.primitives;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.RandomAccess;

/* loaded from: classes.dex */
public abstract class Longs {

    /* loaded from: classes.dex */
    private static class LongArrayAsList extends AbstractList implements RandomAccess, Serializable {
    }

    public static int compare(long j, long j2) {
        if (j < j2) {
            return -1;
        }
        return j > j2 ? 1 : 0;
    }

    public static int hashCode(long j) {
        return (int) (j ^ (j >>> 32));
    }

    public static long max(long... jArr) {
        Preconditions.checkArgument(jArr.length > 0);
        long j = jArr[0];
        for (int i = 1; i < jArr.length; i++) {
            long j2 = jArr[i];
            if (j2 > j) {
                j = j2;
            }
        }
        return j;
    }

    public static long[] toArray(Collection collection) {
        if (collection instanceof LongArrayAsList) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(collection);
            throw null;
        }
        Object[] array = collection.toArray();
        int length = array.length;
        long[] jArr = new long[length];
        for (int i = 0; i < length; i++) {
            jArr[i] = ((Number) Preconditions.checkNotNull(array[i])).longValue();
        }
        return jArr;
    }
}
