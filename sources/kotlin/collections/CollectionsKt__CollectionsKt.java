package kotlin.collections;

import java.util.Collection;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class CollectionsKt__CollectionsKt extends CollectionsKt__CollectionsJVMKt {
    public static final Collection asCollection(Object[] objArr) {
        Intrinsics.checkNotNullParameter(objArr, "<this>");
        return new ArrayAsCollection(objArr, false);
    }

    public static List emptyList() {
        return EmptyList.INSTANCE;
    }

    public static final int getLastIndex(List list) {
        Intrinsics.checkNotNullParameter(list, "<this>");
        return list.size() - 1;
    }

    public static List listOf(Object... elements) {
        List asList;
        Intrinsics.checkNotNullParameter(elements, "elements");
        if (elements.length <= 0) {
            return emptyList();
        }
        asList = ArraysKt___ArraysJvmKt.asList(elements);
        return asList;
    }

    public static List optimizeReadOnlyList(List list) {
        Intrinsics.checkNotNullParameter(list, "<this>");
        int size = list.size();
        return size != 0 ? size != 1 ? list : CollectionsKt__CollectionsJVMKt.listOf(list.get(0)) : emptyList();
    }

    public static void throwIndexOverflow() {
        throw new ArithmeticException("Index overflow has happened.");
    }
}
