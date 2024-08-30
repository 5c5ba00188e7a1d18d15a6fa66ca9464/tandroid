package com.google.android.gms.internal.clearcut;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.Iterator;
import java.util.Map;
/* loaded from: classes.dex */
final class zzdu implements zzef {
    private final zzdo zzmn;
    private final boolean zzmo;
    private final zzex zzmx;
    private final zzbu zzmy;

    private zzdu(zzex zzexVar, zzbu zzbuVar, zzdo zzdoVar) {
        this.zzmx = zzexVar;
        this.zzmo = zzbuVar.zze(zzdoVar);
        this.zzmy = zzbuVar;
        this.zzmn = zzdoVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzdu zza(zzex zzexVar, zzbu zzbuVar, zzdo zzdoVar) {
        return new zzdu(zzexVar, zzbuVar, zzdoVar);
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final boolean equals(Object obj, Object obj2) {
        if (this.zzmx.zzq(obj).equals(this.zzmx.zzq(obj2))) {
            if (this.zzmo) {
                return this.zzmy.zza(obj).equals(this.zzmy.zza(obj2));
            }
            return true;
        }
        return false;
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final int hashCode(Object obj) {
        int hashCode = this.zzmx.zzq(obj).hashCode();
        return this.zzmo ? (hashCode * 53) + this.zzmy.zza(obj).hashCode() : hashCode;
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final Object newInstance() {
        return this.zzmn.zzbd().zzbi();
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final void zza(Object obj, zzfr zzfrVar) {
        Iterator it = this.zzmy.zza(obj).iterator();
        if (it.hasNext()) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(((Map.Entry) it.next()).getKey());
            throw null;
        }
        zzex zzexVar = this.zzmx;
        zzexVar.zzc(zzexVar.zzq(obj), zzfrVar);
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0061 A[EDGE_INSN: B:50:0x0061->B:27:0x0061 ?: BREAK  , SYNTHETIC] */
    @Override // com.google.android.gms.internal.clearcut.zzef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(Object obj, byte[] bArr, int i, int i2, zzay zzayVar) {
        zzcg zzcgVar = (zzcg) obj;
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
        if (i != i2) {
            throw zzco.zzbo();
        }
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final void zzc(Object obj) {
        this.zzmx.zzc(obj);
        this.zzmy.zzc(obj);
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final void zzc(Object obj, Object obj2) {
        zzeh.zza(this.zzmx, obj, obj2);
        if (this.zzmo) {
            zzeh.zza(this.zzmy, obj, obj2);
        }
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final int zzm(Object obj) {
        zzex zzexVar = this.zzmx;
        int zzr = zzexVar.zzr(zzexVar.zzq(obj));
        return this.zzmo ? zzr + this.zzmy.zza(obj).zzat() : zzr;
    }

    @Override // com.google.android.gms.internal.clearcut.zzef
    public final boolean zzo(Object obj) {
        return this.zzmy.zza(obj).isInitialized();
    }
}
