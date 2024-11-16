package com.google.android.gms.internal.mlkit_language_id;

/* loaded from: classes.dex */
final class zzfm implements zzgo {
    private static final zzfw zzb = new zzfp();
    private final zzfw zza;

    public zzfm() {
        this(new zzfo(zzep.zza(), zza()));
    }

    private zzfm(zzfw zzfwVar) {
        this.zza = (zzfw) zzeq.zza((Object) zzfwVar, "messageInfoFactory");
    }

    private static zzfw zza() {
        try {
            return (zzfw) Class.forName("com.google.protobuf.DescriptorMessageInfoFactory").getDeclaredMethod("getInstance", null).invoke(null, null);
        } catch (Exception unused) {
            return zzb;
        }
    }

    private static boolean zza(zzfx zzfxVar) {
        return zzfxVar.zza() == zzgl.zza;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgo
    public final zzgp zza(Class cls) {
        zzgr.zza(cls);
        zzfx zzb2 = this.zza.zzb(cls);
        if (zzb2.zzb()) {
            return zzeo.class.isAssignableFrom(cls) ? zzgc.zza(zzgr.zzc(), zzeg.zza(), zzb2.zzc()) : zzgc.zza(zzgr.zza(), zzeg.zzb(), zzb2.zzc());
        }
        if (!zzeo.class.isAssignableFrom(cls)) {
            boolean zza = zza(zzb2);
            zzge zza2 = zzgg.zza();
            zzfj zza3 = zzfj.zza();
            return zza ? zzgd.zza(cls, zzb2, zza2, zza3, zzgr.zza(), zzeg.zzb(), zzfu.zza()) : zzgd.zza(cls, zzb2, zza2, zza3, zzgr.zzb(), null, zzfu.zza());
        }
        boolean zza4 = zza(zzb2);
        zzge zzb3 = zzgg.zzb();
        zzfj zzb4 = zzfj.zzb();
        zzhh zzc = zzgr.zzc();
        return zza4 ? zzgd.zza(cls, zzb2, zzb3, zzb4, zzc, zzeg.zza(), zzfu.zzb()) : zzgd.zza(cls, zzb2, zzb3, zzb4, zzc, null, zzfu.zzb());
    }
}
