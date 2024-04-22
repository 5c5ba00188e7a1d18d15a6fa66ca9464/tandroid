package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
final class D extends E implements s {
    @Override // j$.util.s
    public boolean b(Consumer consumer) {
        Objects.requireNonNull(consumer);
        return false;
    }

    @Override // j$.util.s
    public void forEachRemaining(Consumer consumer) {
        Objects.requireNonNull(consumer);
    }

    @Override // j$.util.s
    public Comparator getComparator() {
        throw new IllegalStateException();
    }

    @Override // j$.util.s
    public /* synthetic */ long getExactSizeIfKnown() {
        return a.e(this);
    }

    @Override // j$.util.s
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return a.f(this, i);
    }
}
