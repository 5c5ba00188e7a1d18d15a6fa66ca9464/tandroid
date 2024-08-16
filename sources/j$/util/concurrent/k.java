package j$.util.concurrent;

import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class k implements Map.Entry {
    final int a;
    final Object b;
    volatile Object c;
    volatile k d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k(int i, Object obj, Object obj2, k kVar) {
        this.a = i;
        this.b = obj;
        this.c = obj2;
        this.d = kVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public k a(Object obj, int i) {
        Object obj2;
        if (obj != null) {
            k kVar = this;
            do {
                if (kVar.a == i && ((obj2 = kVar.b) == obj || (obj2 != null && obj.equals(obj2)))) {
                    return kVar;
                }
                kVar = kVar.d;
            } while (kVar != null);
            return null;
        }
        return null;
    }

    @Override // java.util.Map.Entry
    public final boolean equals(Object obj) {
        Map.Entry entry;
        Object key;
        Object value;
        Object obj2;
        Object obj3;
        return (obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null && (value = entry.getValue()) != null && (key == (obj2 = this.b) || key.equals(obj2)) && (value == (obj3 = this.c) || value.equals(obj3));
    }

    @Override // java.util.Map.Entry
    public final Object getKey() {
        return this.b;
    }

    @Override // java.util.Map.Entry
    public final Object getValue() {
        return this.c;
    }

    @Override // java.util.Map.Entry
    public final int hashCode() {
        return this.b.hashCode() ^ this.c.hashCode();
    }

    @Override // java.util.Map.Entry
    public final Object setValue(Object obj) {
        throw new UnsupportedOperationException();
    }

    public final String toString() {
        return this.b + "=" + this.c;
    }
}
