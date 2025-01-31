package com.google.android.recaptcha.internal;

import com.google.android.recaptcha.internal.zzge;
import com.google.android.recaptcha.internal.zzgf;

/* loaded from: classes.dex */
public abstract class zzge<MessageType extends zzgf<MessageType, BuilderType>, BuilderType extends zzge<MessageType, BuilderType>> implements zzkd {
    @Override // 
    public abstract zzge zza();

    protected abstract zzge zzb(zzgf zzgfVar);

    @Override // com.google.android.recaptcha.internal.zzkd
    public final /* bridge */ /* synthetic */ zzkd zzc(zzke zzkeVar) {
        if (zzY().getClass().isInstance(zzkeVar)) {
            return zzb((zzgf) zzkeVar);
        }
        throw new IllegalArgumentException("mergeFrom(MessageLite) can only merge messages of the same type.");
    }
}
