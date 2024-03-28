package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class c3 extends f3 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public c3(j$.util.t tVar, int i, boolean z) {
        super(tVar, i, z);
    }

    @Override // j$.util.stream.c
    final boolean E0() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final n3 F0(int i, n3 n3Var) {
        throw new UnsupportedOperationException();
    }

    @Override // j$.util.stream.f3, j$.util.stream.Stream
    public void e(Consumer consumer) {
        if (!isParallel()) {
            H0().forEachRemaining(consumer);
            return;
        }
        Objects.requireNonNull(consumer);
        v0(new o0(consumer, true));
    }

    @Override // j$.util.stream.f3, j$.util.stream.Stream
    public void forEach(Consumer consumer) {
        if (isParallel()) {
            super.forEach(consumer);
        } else {
            H0().forEachRemaining(consumer);
        }
    }
}
