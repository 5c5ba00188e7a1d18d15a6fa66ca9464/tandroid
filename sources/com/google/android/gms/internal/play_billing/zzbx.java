package com.google.android.gms.internal.play_billing;

/* loaded from: classes.dex */
public abstract class zzbx extends zzaj {
    protected zzcb zza;
    private final zzcb zzb;

    /* JADX INFO: Access modifiers changed from: protected */
    public zzbx(zzcb zzcbVar) {
        this.zzb = zzcbVar;
        if (zzcbVar.zzt()) {
            throw new IllegalArgumentException("Default instance must be immutable.");
        }
        this.zza = zzcbVar.zzi();
    }

    /* renamed from: zzb, reason: merged with bridge method [inline-methods] */
    public final zzbx clone() {
        zzbx zzbxVar = (zzbx) this.zzb.zzu(5, null, null);
        zzbxVar.zza = zze();
        return zzbxVar;
    }

    public final zzcb zzc() {
        zzcb zze = zze();
        if (zze.zzs()) {
            return zze;
        }
        throw new zzef(zze);
    }

    @Override // com.google.android.gms.internal.play_billing.zzde
    /* renamed from: zzd, reason: merged with bridge method [inline-methods] */
    public zzcb zze() {
        if (!this.zza.zzt()) {
            return this.zza;
        }
        this.zza.zzn();
        return this.zza;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void zzg() {
        if (this.zza.zzt()) {
            return;
        }
        zzh();
    }

    protected void zzh() {
        zzcb zzi = this.zzb.zzi();
        zzdn.zza().zzb(zzi.getClass()).zzg(zzi, this.zza);
        this.zza = zzi;
    }
}
