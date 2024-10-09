package com.google.android.gms.internal.mlkit_language_id;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.RandomAccess;

/* loaded from: classes.dex */
final class zzgn extends zzdi implements RandomAccess {
    private static final zzgn zza;
    private Object[] zzb;
    private int zzc;

    static {
        zzgn zzgnVar = new zzgn(new Object[0], 0);
        zza = zzgnVar;
        zzgnVar.b_();
    }

    private zzgn(Object[] objArr, int i) {
        this.zzb = objArr;
        this.zzc = i;
    }

    private final void zza(int i) {
        if (i < 0 || i >= this.zzc) {
            throw new IndexOutOfBoundsException(zzc(i));
        }
    }

    private final String zzc(int i) {
        int i2 = this.zzc;
        StringBuilder sb = new StringBuilder(35);
        sb.append("Index:");
        sb.append(i);
        sb.append(", Size:");
        sb.append(i2);
        return sb.toString();
    }

    public static zzgn zzd() {
        return zza;
    }

    @Override // java.util.AbstractList, java.util.List
    public final void add(int i, Object obj) {
        int i2;
        zzc();
        if (i < 0 || i > (i2 = this.zzc)) {
            throw new IndexOutOfBoundsException(zzc(i));
        }
        Object[] objArr = this.zzb;
        if (i2 < objArr.length) {
            System.arraycopy(objArr, i, objArr, i + 1, i2 - i);
        } else {
            Object[] objArr2 = new Object[((i2 * 3) / 2) + 1];
            System.arraycopy(objArr, 0, objArr2, 0, i);
            System.arraycopy(this.zzb, i, objArr2, i + 1, this.zzc - i);
            this.zzb = objArr2;
        }
        this.zzb[i] = obj;
        this.zzc++;
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzdi, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean add(Object obj) {
        zzc();
        int i = this.zzc;
        Object[] objArr = this.zzb;
        if (i == objArr.length) {
            this.zzb = Arrays.copyOf(objArr, ((i * 3) / 2) + 1);
        }
        Object[] objArr2 = this.zzb;
        int i2 = this.zzc;
        this.zzc = i2 + 1;
        objArr2[i2] = obj;
        ((AbstractList) this).modCount++;
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public final Object get(int i) {
        zza(i);
        return this.zzb[i];
    }

    @Override // java.util.AbstractList, java.util.List
    public final Object remove(int i) {
        zzc();
        zza(i);
        Object[] objArr = this.zzb;
        Object obj = objArr[i];
        if (i < this.zzc - 1) {
            System.arraycopy(objArr, i + 1, objArr, i, (r2 - i) - 1);
        }
        this.zzc--;
        ((AbstractList) this).modCount++;
        return obj;
    }

    @Override // java.util.AbstractList, java.util.List
    public final Object set(int i, Object obj) {
        zzc();
        zza(i);
        Object[] objArr = this.zzb;
        Object obj2 = objArr[i];
        objArr[i] = obj;
        ((AbstractList) this).modCount++;
        return obj2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzew
    public final /* synthetic */ zzew zzb(int i) {
        if (i >= this.zzc) {
            return new zzgn(Arrays.copyOf(this.zzb, i), this.zzc);
        }
        throw new IllegalArgumentException();
    }
}
