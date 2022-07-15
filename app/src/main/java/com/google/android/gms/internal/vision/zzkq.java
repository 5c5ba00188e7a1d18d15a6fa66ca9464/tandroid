package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes.dex */
final class zzkq<T> implements zzlc<T> {
    private final zzkk zza;
    private final zzlu<?, ?> zzb;
    private final boolean zzc;
    private final zziq<?> zzd;

    private zzkq(zzlu<?, ?> zzluVar, zziq<?> zziqVar, zzkk zzkkVar) {
        this.zzb = zzluVar;
        this.zzc = zziqVar.zza(zzkkVar);
        this.zzd = zziqVar;
        this.zza = zzkkVar;
    }

    public static <T> zzkq<T> zza(zzlu<?, ?> zzluVar, zziq<?> zziqVar, zzkk zzkkVar) {
        return new zzkq<>(zzluVar, zziqVar, zzkkVar);
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final T zza() {
        return (T) this.zza.zzq().zze();
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final boolean zza(T t, T t2) {
        if (!this.zzb.zzb(t).equals(this.zzb.zzb(t2))) {
            return false;
        }
        if (!this.zzc) {
            return true;
        }
        return this.zzd.zza(t).equals(this.zzd.zza(t2));
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final int zza(T t) {
        int hashCode = this.zzb.zzb(t).hashCode();
        return this.zzc ? (hashCode * 53) + this.zzd.zza(t).hashCode() : hashCode;
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zzb(T t, T t2) {
        zzle.zza(this.zzb, t, t2);
        if (this.zzc) {
            zzle.zza(this.zzd, t, t2);
        }
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zza(T t, zzmr zzmrVar) throws IOException {
        Iterator<Map.Entry<?, Object>> zzd = this.zzd.zza(t).zzd();
        while (zzd.hasNext()) {
            Map.Entry<?, Object> next = zzd.next();
            zziw zziwVar = (zziw) next.getKey();
            if (zziwVar.zzc() != zzmo.MESSAGE || zziwVar.zzd() || zziwVar.zze()) {
                throw new IllegalStateException("Found invalid MessageSet item.");
            }
            if (next instanceof zzjr) {
                zzmrVar.zza(zziwVar.zza(), (Object) ((zzjr) next).zza().zzc());
            } else {
                zzmrVar.zza(zziwVar.zza(), next.getValue());
            }
        }
        zzlu<?, ?> zzluVar = this.zzb;
        zzluVar.zzb((zzlu<?, ?>) zzluVar.zzb(t), zzmrVar);
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00be A[EDGE_INSN: B:58:0x00be->B:32:0x00be ?: BREAK  , SYNTHETIC] */
    @Override // com.google.android.gms.internal.vision.zzlc
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void zza(T t, byte[] bArr, int i, int i2, zzhn zzhnVar) throws IOException {
        zzjb zzjbVar = (zzjb) t;
        zzlx zzlxVar = zzjbVar.zzb;
        if (zzlxVar == zzlx.zza()) {
            zzlxVar = zzlx.zzb();
            zzjbVar.zzb = zzlxVar;
        }
        zziu<zzjb.zzf> zza = ((zzjb.zzc) t).zza();
        zzjb.zze zzeVar = null;
        while (i < i2) {
            int zza2 = zzhl.zza(bArr, i, zzhnVar);
            int i3 = zzhnVar.zza;
            if (i3 == 11) {
                int i4 = 0;
                zzht zzhtVar = null;
                while (zza2 < i2) {
                    zza2 = zzhl.zza(bArr, zza2, zzhnVar);
                    int i5 = zzhnVar.zza;
                    int i6 = i5 >>> 3;
                    int i7 = i5 & 7;
                    if (i6 != 2) {
                        if (i6 == 3) {
                            if (zzeVar != null) {
                                zza2 = zzhl.zza(zzky.zza().zza((Class) zzeVar.zzc.getClass()), bArr, zza2, i2, zzhnVar);
                                zza.zza((zziu<zzjb.zzf>) zzeVar.zzd, zzhnVar.zzc);
                            } else if (i7 == 2) {
                                zza2 = zzhl.zze(bArr, zza2, zzhnVar);
                                zzhtVar = (zzht) zzhnVar.zzc;
                            }
                        }
                        if (i5 != 12) {
                            break;
                        }
                        zza2 = zzhl.zza(i5, bArr, zza2, i2, zzhnVar);
                    } else if (i7 == 0) {
                        zza2 = zzhl.zza(bArr, zza2, zzhnVar);
                        i4 = zzhnVar.zza;
                        zzeVar = (zzjb.zze) this.zzd.zza(zzhnVar.zzd, this.zza, i4);
                    } else if (i5 != 12) {
                    }
                }
                if (zzhtVar != null) {
                    zzlxVar.zza((i4 << 3) | 2, zzhtVar);
                }
                i = zza2;
            } else if ((i3 & 7) == 2) {
                zzjb.zze zzeVar2 = (zzjb.zze) this.zzd.zza(zzhnVar.zzd, this.zza, i3 >>> 3);
                if (zzeVar2 != null) {
                    i = zzhl.zza(zzky.zza().zza((Class) zzeVar2.zzc.getClass()), bArr, zza2, i2, zzhnVar);
                    zza.zza((zziu<zzjb.zzf>) zzeVar2.zzd, zzhnVar.zzc);
                } else {
                    i = zzhl.zza(i3, bArr, zza2, i2, zzlxVar, zzhnVar);
                }
                zzeVar = zzeVar2;
            } else {
                i = zzhl.zza(i3, bArr, zza2, i2, zzhnVar);
            }
        }
        if (i == i2) {
            return;
        }
        throw zzjk.zzg();
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zzc(T t) {
        this.zzb.zzd(t);
        this.zzd.zzc(t);
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final boolean zzd(T t) {
        return this.zzd.zza(t).zzf();
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final int zzb(T t) {
        zzlu<?, ?> zzluVar = this.zzb;
        int zze = zzluVar.zze(zzluVar.zzb(t)) + 0;
        return this.zzc ? zze + this.zzd.zza(t).zzg() : zze;
    }
}
