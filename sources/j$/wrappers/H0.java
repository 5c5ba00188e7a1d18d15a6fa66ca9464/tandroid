package j$.wrappers;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.BaseStream;
/* loaded from: classes2.dex */
public final /* synthetic */ class H0 implements BaseStream {
    final /* synthetic */ j$.util.stream.g a;

    private /* synthetic */ H0(j$.util.stream.g gVar) {
        this.a = gVar;
    }

    public static /* synthetic */ BaseStream i0(j$.util.stream.g gVar) {
        if (gVar == null) {
            return null;
        }
        return gVar instanceof G0 ? ((G0) gVar).a : new H0(gVar);
    }

    @Override // java.util.stream.BaseStream, java.lang.AutoCloseable
    public /* synthetic */ void close() {
        this.a.close();
    }

    @Override // java.util.stream.BaseStream
    public /* synthetic */ boolean isParallel() {
        return this.a.isParallel();
    }

    @Override // java.util.stream.BaseStream
    public /* synthetic */ Iterator iterator() {
        return this.a.iterator();
    }

    @Override // java.util.stream.BaseStream
    public /* synthetic */ BaseStream onClose(Runnable runnable) {
        return i0(this.a.onClose(runnable));
    }

    @Override // java.util.stream.BaseStream
    public /* synthetic */ BaseStream parallel() {
        return i0(this.a.parallel());
    }

    @Override // java.util.stream.BaseStream
    public /* synthetic */ BaseStream sequential() {
        return i0(this.a.sequential());
    }

    @Override // java.util.stream.BaseStream
    public /* synthetic */ Spliterator spliterator() {
        return h.a(this.a.spliterator());
    }

    @Override // java.util.stream.BaseStream
    public /* synthetic */ BaseStream unordered() {
        return i0(this.a.unordered());
    }
}
