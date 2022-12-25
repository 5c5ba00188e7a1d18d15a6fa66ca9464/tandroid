package com.google.android.gms.internal.clearcut;

import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* JADX WARN: Incorrect field signature: TK; */
/* loaded from: classes.dex */
public final class zzep implements Comparable<zzep>, Map.Entry<K, V> {
    private V value;
    private final /* synthetic */ zzei zzos;
    private final Comparable zzov;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public zzep(zzei zzeiVar, K k, V v) {
        this.zzos = zzeiVar;
        this.zzov = k;
        this.value = v;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzep(zzei zzeiVar, Map.Entry<K, V> entry) {
        this(zzeiVar, (Comparable) entry.getKey(), entry.getValue());
    }

    private static boolean equals(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    @Override // java.lang.Comparable
    public final /* synthetic */ int compareTo(zzep zzepVar) {
        return ((Comparable) getKey()).compareTo((Comparable) zzepVar.getKey());
    }

    @Override // java.util.Map.Entry
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) obj;
            return equals(this.zzov, entry.getKey()) && equals(this.value, entry.getValue());
        }
        return false;
    }

    @Override // java.util.Map.Entry
    public final /* synthetic */ Object getKey() {
        return this.zzov;
    }

    @Override // java.util.Map.Entry
    public final V getValue() {
        return this.value;
    }

    @Override // java.util.Map.Entry
    public final int hashCode() {
        Comparable comparable = this.zzov;
        int hashCode = comparable == null ? 0 : comparable.hashCode();
        V v = this.value;
        return hashCode ^ (v != 0 ? v.hashCode() : 0);
    }

    @Override // java.util.Map.Entry
    public final V setValue(V v) {
        this.zzos.zzdu();
        V v2 = this.value;
        this.value = v;
        return v2;
    }

    public final String toString() {
        String valueOf = String.valueOf(this.zzov);
        String valueOf2 = String.valueOf(this.value);
        StringBuilder sb = new StringBuilder(valueOf.length() + 1 + valueOf2.length());
        sb.append(valueOf);
        sb.append("=");
        sb.append(valueOf2);
        return sb.toString();
    }
}
