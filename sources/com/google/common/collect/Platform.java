package com.google.common.collect;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
/* loaded from: classes.dex */
final class Platform {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> Map<K, V> newHashMapWithExpectedSize(int i) {
        return CompactHashMap.createWithExpectedSize(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> Map<K, V> preservesInsertionOrderOnPutsMap() {
        return CompactHashMap.create();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T[] newArray(T[] tArr, int i) {
        return (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T[] copy(Object[] objArr, int i, int i2, T[] tArr) {
        return (T[]) Arrays.copyOfRange(objArr, i, i2, tArr.getClass());
    }
}
