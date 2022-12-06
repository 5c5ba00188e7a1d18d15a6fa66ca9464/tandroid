package com.google.android.gms.internal.location;
/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
/* loaded from: classes.dex */
final class zzdq extends zzdo {
    private final zzds zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzdq(zzds zzdsVar, int i) {
        super(zzdsVar.size(), i);
        this.zza = zzdsVar;
    }

    @Override // com.google.android.gms.internal.location.zzdo
    protected final Object zza(int i) {
        return this.zza.get(i);
    }
}
