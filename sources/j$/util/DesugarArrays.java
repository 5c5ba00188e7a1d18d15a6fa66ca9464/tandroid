package j$.util;

import j$.util.stream.Stream;
import j$.util.stream.o1;
/* loaded from: classes2.dex */
public class DesugarArrays {
    public static s a(Object[] objArr, int i, int i2) {
        return I.m(objArr, i, i2, 1040);
    }

    public static <T> Stream<T> stream(T[] tArr) {
        return o1.y(I.m(tArr, 0, tArr.length, 1040), false);
    }
}
