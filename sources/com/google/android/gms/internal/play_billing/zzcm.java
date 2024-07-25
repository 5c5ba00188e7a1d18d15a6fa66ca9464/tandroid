package com.google.android.gms.internal.play_billing;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzcm extends zzal implements RandomAccess, zzcn {
    private static final zzcm zzb = new zzcm(false);
    private final List zzc;

    public zzcm() {
        this(10);
    }

    private static String zzi(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof zzba) {
            return ((zzba) obj).zzm(zzcg.zzb);
        }
        return zzcg.zzd((byte[]) obj);
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ void add(int i, Object obj) {
        zza();
        this.zzc.add(i, (String) obj);
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.play_billing.zzal, java.util.AbstractList, java.util.List
    public final boolean addAll(int i, Collection collection) {
        zza();
        if (collection instanceof zzcn) {
            collection = ((zzcn) collection).zzh();
        }
        boolean addAll = this.zzc.addAll(i, collection);
        ((AbstractList) this).modCount++;
        return addAll;
    }

    @Override // com.google.android.gms.internal.play_billing.zzal, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final void clear() {
        zza();
        this.zzc.clear();
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.play_billing.zzal, java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object remove(int i) {
        zza();
        Object remove = this.zzc.remove(i);
        ((AbstractList) this).modCount++;
        return zzi(remove);
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object set(int i, Object obj) {
        zza();
        return zzi(this.zzc.set(i, (String) obj));
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zzc.size();
    }

    @Override // com.google.android.gms.internal.play_billing.zzcf
    public final /* bridge */ /* synthetic */ zzcf zzd(int i) {
        if (i < size()) {
            throw new IllegalArgumentException();
        }
        ArrayList arrayList = new ArrayList(i);
        arrayList.addAll(this.zzc);
        return new zzcm(arrayList);
    }

    @Override // com.google.android.gms.internal.play_billing.zzcn
    public final zzcn zze() {
        return zzc() ? new zzel(this) : this;
    }

    @Override // com.google.android.gms.internal.play_billing.zzcn
    public final Object zzf(int i) {
        return this.zzc.get(i);
    }

    @Override // java.util.AbstractList, java.util.List
    /* renamed from: zzg */
    public final String get(int i) {
        Object obj = this.zzc.get(i);
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof zzba) {
            zzba zzbaVar = (zzba) obj;
            String zzm = zzbaVar.zzm(zzcg.zzb);
            if (zzbaVar.zzi()) {
                this.zzc.set(i, zzm);
            }
            return zzm;
        }
        byte[] bArr = (byte[]) obj;
        String zzd = zzcg.zzd(bArr);
        if (zzev.zzd(bArr)) {
            this.zzc.set(i, zzd);
        }
        return zzd;
    }

    @Override // com.google.android.gms.internal.play_billing.zzcn
    public final List zzh() {
        return Collections.unmodifiableList(this.zzc);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzcm(int i) {
        super(true);
        ArrayList arrayList = new ArrayList(i);
        this.zzc = arrayList;
    }

    private zzcm(ArrayList arrayList) {
        super(true);
        this.zzc = arrayList;
    }

    private zzcm(boolean z) {
        super(false);
        this.zzc = Collections.emptyList();
    }

    @Override // com.google.android.gms.internal.play_billing.zzal, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection collection) {
        return addAll(size(), collection);
    }
}
