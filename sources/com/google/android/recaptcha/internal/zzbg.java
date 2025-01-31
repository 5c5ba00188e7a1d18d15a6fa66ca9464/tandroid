package com.google.android.recaptcha.internal;

import android.content.Context;
import java.util.HashMap;

/* loaded from: classes.dex */
public final class zzbg {
    private final String zza;
    private final Context zzb;
    private final zzab zzc;
    private final zzbh zzd;
    private final HashMap zze = new HashMap();
    private final zzt zzf;

    public zzbg(String str, Context context, zzab zzabVar, zzt zztVar, zzbh zzbhVar) {
        this.zza = str;
        this.zzb = context;
        this.zzc = zzabVar;
        this.zzf = zztVar;
        this.zzd = zzbhVar;
    }

    public final void zza(zzbb zzbbVar) {
        zze(zzbbVar, 3, null);
    }

    public final void zzb(zzbb zzbbVar, zzp zzpVar, String str) {
        String valueOf = String.valueOf(zzpVar.zzb().zza());
        int zza = zzpVar.zza().zza();
        String zzd = zzpVar.zzd();
        zzmq zzg = zzmr.zzg();
        zzg.zzp(valueOf);
        zzg.zzd(zza);
        if (zzd != null) {
            zzg.zze(zzd);
        }
        zze(zzbbVar, 4, (zzmr) zzg.zzj());
    }

    public final void zzd(zzpd zzpdVar) {
        this.zzd.zza(zzpdVar);
    }

    protected final void zze(zzbb zzbbVar, int i, zzmr zzmrVar) {
        zzx zzxVar;
        zzbf zzbfVar = (zzbf) this.zze.get(zzbbVar);
        if (zzbfVar != null) {
            zznf zza = zzbfVar.zza(i, zzmrVar, this.zzb);
            zzpc zzi = zzpd.zzi();
            zzi.zzd(zza);
            zzpd zzpdVar = (zzpd) zzi.zzj();
            zzv zzvVar = zzv.zza;
            zzne zza2 = zzbbVar.zza();
            long zzf = zza.zzf() * 1000;
            zzne zzneVar = zzne.zza;
            int ordinal = zza2.ordinal();
            if (ordinal == 1) {
                zzxVar = zzx.zzd;
            } else if (ordinal == 2) {
                zzxVar = zzx.zze;
            } else if (ordinal == 5) {
                zzxVar = zzx.zzf;
            } else if (ordinal == 6) {
                zzxVar = zzx.zzg;
            } else if (ordinal != 24) {
                switch (ordinal) {
                    case 12:
                        zzxVar = zzx.zzh;
                        break;
                    case 13:
                        zzxVar = zzx.zzi;
                        break;
                    case 14:
                        zzxVar = zzx.zzj;
                        break;
                    default:
                        zzxVar = zzx.zzb;
                        break;
                }
            } else {
                zzxVar = zzx.zzk;
            }
            zzv.zza(zzxVar.zza(), zzf);
            this.zzd.zza(zzpdVar);
        }
    }
}
