package j$.util.stream;

import j$.util.function.BiConsumer;
import j$.util.function.Function;
import j$.util.function.Supplier;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class m implements Collector {
    private final Supplier a;
    private final BiConsumer b;
    private final j$.util.function.f c;
    private final Function d;
    private final Set e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m(Supplier supplier, J0 j0, l lVar, Set set) {
        Set set2 = Collectors.a;
        b bVar = new b(1);
        this.a = supplier;
        this.b = j0;
        this.c = lVar;
        this.d = bVar;
        this.e = set;
    }

    @Override // j$.util.stream.Collector
    public final BiConsumer accumulator() {
        return this.b;
    }

    @Override // j$.util.stream.Collector
    public final Set characteristics() {
        return this.e;
    }

    @Override // j$.util.stream.Collector
    public final j$.util.function.f combiner() {
        return this.c;
    }

    @Override // j$.util.stream.Collector
    public final Function finisher() {
        return this.d;
    }

    @Override // j$.util.stream.Collector
    public final Supplier supplier() {
        return this.a;
    }
}
