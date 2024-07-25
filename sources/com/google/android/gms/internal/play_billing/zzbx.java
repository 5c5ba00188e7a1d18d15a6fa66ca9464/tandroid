package com.google.android.gms.internal.play_billing;

import com.google.android.gms.internal.play_billing.zzbx;
import com.google.android.gms.internal.play_billing.zzcb;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public class zzbx<MessageType extends zzcb<MessageType, BuilderType>, BuilderType extends zzbx<MessageType, BuilderType>> extends zzaj<MessageType, BuilderType> {
    protected zzcb zza;
    private final zzcb zzb;

    /* JADX INFO: Access modifiers changed from: protected */
    public zzbx(MessageType messagetype) {
        this.zzb = messagetype;
        if (!messagetype.zzt()) {
            this.zza = messagetype.zzi();
            return;
        }
        throw new IllegalArgumentException("Default instance must be immutable.");
    }

    /* renamed from: zzb */
    public final zzbx clone() {
        zzbx zzbxVar = (zzbx) this.zzb.zzu(5, null, null);
        zzbxVar.zza = zze();
        return zzbxVar;
    }

    public final MessageType zzc() {
        MessageType zze = zze();
        if (zze.zzs()) {
            return zze;
        }
        throw new zzef(zze);
    }

    @Override // com.google.android.gms.internal.play_billing.zzde
    /* renamed from: zzd */
    public MessageType zze() {
        if (this.zza.zzt()) {
            this.zza.zzn();
            return (MessageType) this.zza;
        }
        return (MessageType) this.zza;
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
