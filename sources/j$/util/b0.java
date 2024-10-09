package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;

/* loaded from: classes2.dex */
final class b0 extends a implements Q {
    @Override // j$.util.Q
    public final void a(Consumer consumer) {
        consumer.getClass();
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return a.j(this);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return a.k(this, i);
    }

    @Override // j$.util.Q
    public final boolean s(Consumer consumer) {
        consumer.getClass();
        return false;
    }
}
