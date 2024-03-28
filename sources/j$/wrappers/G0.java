package j$.wrappers;

import java.util.Iterator;
import java.util.stream.BaseStream;
/* loaded from: classes2.dex */
public final /* synthetic */ class G0 implements j$.util.stream.g {
    final /* synthetic */ BaseStream a;

    private /* synthetic */ G0(BaseStream baseStream) {
        this.a = baseStream;
    }

    public static /* synthetic */ j$.util.stream.g l0(BaseStream baseStream) {
        if (baseStream == null) {
            return null;
        }
        return baseStream instanceof H0 ? ((H0) baseStream).a : new G0(baseStream);
    }

    @Override // j$.util.stream.g, java.lang.AutoCloseable
    public /* synthetic */ void close() {
        this.a.close();
    }

    @Override // j$.util.stream.g
    public /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // j$.util.stream.g
    public /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g onClose(Runnable runnable) {
        return l0(this.a.onClose(runnable));
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g parallel() {
        return l0(this.a.parallel());
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g sequential() {
        return l0(this.a.sequential());
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.t spliterator() {
        return g.a(this.a.spliterator());
    }

    @Override // j$.util.stream.g
    public /* synthetic */ j$.util.stream.g unordered() {
        return l0(this.a.unordered());
    }
}
