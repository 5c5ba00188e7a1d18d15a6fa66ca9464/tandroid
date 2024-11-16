package com.google.android.gms.internal.vision;

import java.util.NoSuchElementException;

/* loaded from: classes.dex */
final class zzhs extends zzhu {
    private int zza = 0;
    private final int zzb;
    private final /* synthetic */ zzht zzc;

    zzhs(zzht zzhtVar) {
        this.zzc = zzhtVar;
        this.zzb = zzhtVar.zza();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.zza < this.zzb;
    }

    @Override // com.google.android.gms.internal.vision.zzhy
    public final byte zza() {
        int i = this.zza;
        if (i >= this.zzb) {
            throw new NoSuchElementException();
        }
        this.zza = i + 1;
        return this.zzc.zzb(i);
    }
}
