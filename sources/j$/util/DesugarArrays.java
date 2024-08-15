package j$.util;

import j$.util.stream.Stream;
import j$.util.stream.u0;
/* loaded from: classes2.dex */
public class DesugarArrays {
    public static <T> Stream<T> stream(T[] tArr) {
        return u0.W0(f0.m(tArr, 0, tArr.length), false);
    }
}
