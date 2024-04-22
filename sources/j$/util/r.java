package j$.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class r extends G {
    final /* synthetic */ SortedSet f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public r(SortedSet sortedSet, Collection collection, int i) {
        super(collection, i);
        this.f = sortedSet;
    }

    @Override // j$.util.G, j$.util.s
    public Comparator getComparator() {
        return this.f.comparator();
    }
}
