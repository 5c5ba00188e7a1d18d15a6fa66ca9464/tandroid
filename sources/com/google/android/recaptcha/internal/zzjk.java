package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public class zzjk {
    private static final zzie zzb = zzie.zza;
    protected volatile zzke zza;
    private volatile zzgw zzc;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzjk)) {
            return false;
        }
        zzjk zzjkVar = (zzjk) obj;
        zzke zzkeVar = this.zza;
        zzke zzkeVar2 = zzjkVar.zza;
        if (zzkeVar == null && zzkeVar2 == null) {
            return zzb().equals(zzjkVar.zzb());
        }
        if (zzkeVar != null && zzkeVar2 != null) {
            return zzkeVar.equals(zzkeVar2);
        }
        if (zzkeVar != null) {
            zzjkVar.zzd(zzkeVar.zzY());
            return zzkeVar.equals(zzjkVar.zza);
        }
        zzd(zzkeVar2.zzY());
        return this.zza.equals(zzkeVar2);
    }

    public int hashCode() {
        return 1;
    }

    public final int zza() {
        if (this.zzc != null) {
            return ((zzgt) this.zzc).zza.length;
        }
        if (this.zza != null) {
            return this.zza.zzn();
        }
        return 0;
    }

    public final zzgw zzb() {
        if (this.zzc != null) {
            return this.zzc;
        }
        synchronized (this) {
            try {
                if (this.zzc != null) {
                    return this.zzc;
                }
                this.zzc = this.zza == null ? zzgw.zzb : this.zza.zzb();
                return this.zzc;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final zzke zzc(zzke zzkeVar) {
        zzke zzkeVar2 = this.zza;
        this.zzc = null;
        this.zza = zzkeVar;
        return zzkeVar2;
    }

    protected final void zzd(zzke zzkeVar) {
        if (this.zza != null) {
            return;
        }
        synchronized (this) {
            if (this.zza != null) {
                return;
            }
            try {
                this.zza = zzkeVar;
                this.zzc = zzgw.zzb;
            } catch (zzje unused) {
                this.zza = zzkeVar;
                this.zzc = zzgw.zzb;
            }
        }
    }
}
