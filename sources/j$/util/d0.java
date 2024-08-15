package j$.util;

import j$.util.Iterator;
import j$.util.function.Consumer;
import java.util.Collection;
import java.util.Comparator;
import org.telegram.messenger.LiteMode;
import org.telegram.tgnet.ConnectionsManager;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class d0 implements Q {
    private final Collection a;
    private java.util.Iterator b = null;
    private final int c;
    private long d;
    private int e;

    public d0(Collection collection, int i) {
        this.a = collection;
        this.c = (i & LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM) == 0 ? i | 64 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM : i;
    }

    @Override // j$.util.Q
    public final boolean a(Consumer consumer) {
        consumer.getClass();
        if (this.b == null) {
            Collection collection = this.a;
            this.b = collection.iterator();
            this.d = collection.size();
        }
        if (this.b.hasNext()) {
            consumer.accept(this.b.next());
            return true;
        }
        return false;
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return this.c;
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        if (this.b == null) {
            Collection collection = this.a;
            this.b = collection.iterator();
            long size = collection.size();
            this.d = size;
            return size;
        }
        return this.d;
    }

    @Override // j$.util.Q
    public final void forEachRemaining(Consumer consumer) {
        consumer.getClass();
        java.util.Iterator it = this.b;
        if (it == null) {
            Collection collection = this.a;
            java.util.Iterator it2 = collection.iterator();
            this.b = it2;
            this.d = collection.size();
            it = it2;
        }
        if (it instanceof Iterator) {
            ((Iterator) it).forEachRemaining(consumer);
        } else {
            Iterator.-CC.$default$forEachRemaining(it, consumer);
        }
    }

    @Override // j$.util.Q
    public Comparator getComparator() {
        if (a.k(this, 4)) {
            return null;
        }
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return a.i(this);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return a.k(this, i);
    }

    @Override // j$.util.Q
    public final Q trySplit() {
        long j;
        java.util.Iterator it = this.b;
        if (it == null) {
            Collection collection = this.a;
            java.util.Iterator it2 = collection.iterator();
            this.b = it2;
            j = collection.size();
            this.d = j;
            it = it2;
        } else {
            j = this.d;
        }
        if (j <= 1 || !it.hasNext()) {
            return null;
        }
        int i = this.e + 1024;
        if (i > j) {
            i = (int) j;
        }
        if (i > 33554432) {
            i = ConnectionsManager.FileTypeVideo;
        }
        Object[] objArr = new Object[i];
        int i2 = 0;
        do {
            objArr[i2] = it.next();
            i2++;
            if (i2 >= i) {
                break;
            }
        } while (it.hasNext());
        this.e = i2;
        long j2 = this.d;
        if (j2 != Long.MAX_VALUE) {
            this.d = j2 - i2;
        }
        return new W(objArr, 0, i2, this.c);
    }
}
