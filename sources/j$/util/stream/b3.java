package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class b3 extends e3 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public b3(j$.util.s sVar, int i, boolean z) {
        super(sVar, i, z);
    }

    @Override // j$.util.stream.c
    final boolean B0() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final m3 C0(int i, m3 m3Var) {
        throw new UnsupportedOperationException();
    }

    @Override // j$.util.stream.e3, j$.util.stream.Stream
    public void e(Consumer consumer) {
        if (!isParallel()) {
            E0().forEachRemaining(consumer);
            return;
        }
        Objects.requireNonNull(consumer);
        s0(new o0(consumer, true));
    }

    @Override // j$.util.stream.e3, j$.util.stream.Stream
    public void forEach(Consumer consumer) {
        if (isParallel()) {
            super.forEach(consumer);
        } else {
            E0().forEachRemaining(consumer);
        }
    }
}
