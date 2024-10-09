package kotlin.collections;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes.dex */
abstract class ArraysKt___ArraysJvmKt extends ArraysKt__ArraysKt {
    public static final List asList(Object[] objArr) {
        Intrinsics.checkNotNullParameter(objArr, "<this>");
        List asList = ArraysUtilJVM.asList(objArr);
        Intrinsics.checkNotNullExpressionValue(asList, "asList(this)");
        return asList;
    }
}
