package j$.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;

/* loaded from: classes2.dex */
final class B extends d0 {
    final /* synthetic */ SortedSet f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    B(SortedSet sortedSet, Collection collection) {
        super(collection, 21);
        this.f = sortedSet;
    }

    @Override // j$.util.d0, j$.util.Q
    public final Comparator getComparator() {
        return this.f.comparator();
    }
}
