package com.google.android.gms.internal.clearcut;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
/* loaded from: classes.dex */
final class zzdu<T> implements zzef<T> {
    private final zzdo zzmn;
    private final boolean zzmo;
    private final zzex<?, ?> zzmx;
    private final zzbu<?> zzmy;

    private zzdu(zzex<?, ?> zzexVar, zzbu<?> zzbuVar, zzdo zzdoVar) {
        this.zzmx = zzexVar;
        this.zzmo = zzbuVar.zze(zzdoVar);
        this.zzmy = zzbuVar;
        this.zzmn = zzdoVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> zzdu<T> zza(zzex<?, ?> zzexVar, zzbu<?> zzbuVar, zzdo zzdoVar) {
        return new zzdu<>(zzexVar, zzbuVar, zzdoVar);
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final boolean equals(T t, T t2) {
        if (!this.zzmx.zzq(t).equals(this.zzmx.zzq(t2))) {
            return false;
        }
        if (!this.zzmo) {
            return true;
        }
        return this.zzmy.zza(t).equals(this.zzmy.zza(t2));
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final int hashCode(T t) {
        int hashCode = this.zzmx.zzq(t).hashCode();
        return this.zzmo ? (hashCode * 53) + this.zzmy.zza(t).hashCode() : hashCode;
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final T newInstance() {
        return (T) this.zzmn.zzbd().zzbi();
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final void zza(T t, zzfr zzfrVar) throws IOException {
        Iterator<Map.Entry<?, Object>> it = this.zzmy.zza(t).iterator();
        while (it.hasNext()) {
            Map.Entry<?, Object> next = it.next();
            zzca zzcaVar = (zzca) next.getKey();
            if (zzcaVar.zzav() != zzfq.zzrf || zzcaVar.zzaw() || zzcaVar.zzax()) {
                throw new IllegalStateException("Found invalid MessageSet item.");
            }
            zzfrVar.zza(zzcaVar.zzc(), next instanceof zzct ? ((zzct) next).zzbs().zzr() : next.getValue());
        }
        zzex<?, ?> zzexVar = this.zzmx;
        zzexVar.zzc(zzexVar.zzq(t), zzfrVar);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0061 A[EDGE_INSN: B:24:0x0061->B:25:0x0061 ?: BREAK  , SYNTHETIC] */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(T t, byte[] bArr, int i, int i2, zzay zzayVar) throws IOException {
        zzcg zzcgVar = (zzcg) t;
        zzey zzeyVar = zzcgVar.zzjp;
        if (zzeyVar == zzey.zzea()) {
            zzeyVar = zzey.zzeb();
            zzcgVar.zzjp = zzeyVar;
        }
        zzey zzeyVar2 = zzeyVar;
        while (i < i2) {
            int zza = zzax.zza(bArr, i, zzayVar);
            int i3 = zzayVar.zzfd;
            if (i3 != 11) {
                i = (i3 & 7) == 2 ? zzax.zza(i3, bArr, zza, i2, zzeyVar2, zzayVar) : zzax.zza(i3, bArr, zza, i2, zzayVar);
            } else {
                int i4 = 0;
                zzbb zzbbVar = null;
                while (zza < i2) {
                    zza = zzax.zza(bArr, zza, zzayVar);
                    int i5 = zzayVar.zzfd;
                    int i6 = i5 >>> 3;
                    int i7 = i5 & 7;
                    if (i6 != 2) {
                        if (i6 == 3 && i7 == 2) {
                            zza = zzax.zze(bArr, zza, zzayVar);
                            zzbbVar = (zzbb) zzayVar.zzff;
                        }
                        if (i5 != 12) {
                            break;
                        }
                        zza = zzax.zza(i5, bArr, zza, i2, zzayVar);
                    } else if (i7 == 0) {
                        zza = zzax.zza(bArr, zza, zzayVar);
                        i4 = zzayVar.zzfd;
                    } else if (i5 != 12) {
                    }
                }
                if (zzbbVar != null) {
                    zzeyVar2.zzb((i4 << 3) | 2, zzbbVar);
                }
                i = zza;
            }
        }
        if (i == i2) {
            return;
        }
        throw zzco.zzbo();
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final void zzc(T t) {
        this.zzmx.zzc(t);
        this.zzmy.zzc(t);
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final void zzc(T t, T t2) {
        zzeh.zza(this.zzmx, t, t2);
        if (this.zzmo) {
            zzeh.zza(this.zzmy, t, t2);
        }
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final int zzm(T t) {
        zzex<?, ?> zzexVar = this.zzmx;
        int zzr = zzexVar.zzr(zzexVar.zzq(t)) + 0;
        return this.zzmo ? zzr + this.zzmy.zza(t).zzat() : zzr;
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final boolean zzo(T t) {
        return this.zzmy.zza(t).isInitialized();
    }
}
