package com.google.android.recaptcha.internal;

import com.google.android.recaptcha.internal.zzin;
import com.google.android.recaptcha.internal.zzit;

/* loaded from: classes.dex */
public class zzin<MessageType extends zzit<MessageType, BuilderType>, BuilderType extends zzin<MessageType, BuilderType>> extends zzge<MessageType, BuilderType> {
    protected zzit zza;
    private final zzit zzb;

    protected zzin(MessageType messagetype) {
        this.zzb = messagetype;
        if (messagetype.zzG()) {
            throw new IllegalArgumentException("Default instance must be immutable.");
        }
        this.zza = messagetype.zzs();
    }

    private static void zzd(Object obj, Object obj2) {
        zzkn.zza().zzb(obj.getClass()).zzg(obj, obj2);
    }

    @Override // com.google.android.recaptcha.internal.zzkf
    public final /* synthetic */ zzke zzY() {
        return this.zzb;
    }

    @Override // com.google.android.recaptcha.internal.zzge
    protected final /* synthetic */ zzge zzb(zzgf zzgfVar) {
        zzg((zzit) zzgfVar);
        return this;
    }

    @Override // com.google.android.recaptcha.internal.zzge
    /* renamed from: zzf, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public final zzin zza() {
        zzin zzinVar = (zzin) this.zzb.zzh(5, null, null);
        zzinVar.zza = zzk();
        return zzinVar;
    }

    public final zzin zzg(zzit zzitVar) {
        if (!this.zzb.equals(zzitVar)) {
            if (!this.zza.zzG()) {
                zzn();
            }
            zzd(this.zza, zzitVar);
        }
        return this;
    }

    @Override // com.google.android.recaptcha.internal.zzkd
    /* renamed from: zzh, reason: merged with bridge method [inline-methods] */
    public final MessageType zzj() {
        MessageType zzk = zzk();
        if (zzk.zzo()) {
            return zzk;
        }
        throw new zzlk(zzk);
    }

    @Override // com.google.android.recaptcha.internal.zzkd
    /* renamed from: zzi, reason: merged with bridge method [inline-methods] */
    public MessageType zzk() {
        if (!this.zza.zzG()) {
            return (MessageType) this.zza;
        }
        this.zza.zzB();
        return (MessageType) this.zza;
    }

    protected final void zzm() {
        if (this.zza.zzG()) {
            return;
        }
        zzn();
    }

    protected void zzn() {
        zzit zzs = this.zzb.zzs();
        zzd(zzs, this.zza);
        this.zza = zzs;
    }

    @Override // com.google.android.recaptcha.internal.zzkf
    public final boolean zzo() {
        return zzit.zzF(this.zza, false);
    }
}
