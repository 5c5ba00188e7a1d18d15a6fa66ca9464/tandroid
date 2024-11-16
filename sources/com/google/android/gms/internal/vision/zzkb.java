package com.google.android.gms.internal.vision;

/* loaded from: classes.dex */
final class zzkb implements zzlf {
    private static final zzkl zzb = new zzka();
    private final zzkl zza;

    public zzkb() {
        this(new zzkd(zzjc.zza(), zza()));
    }

    private zzkb(zzkl zzklVar) {
        this.zza = (zzkl) zzjf.zza((Object) zzklVar, "messageInfoFactory");
    }

    private static zzkl zza() {
        try {
            return (zzkl) Class.forName("com.google.protobuf.DescriptorMessageInfoFactory").getDeclaredMethod("getInstance", null).invoke(null, null);
        } catch (Exception unused) {
            return zzb;
        }
    }

    private static boolean zza(zzki zzkiVar) {
        return zzkiVar.zza() == zzkz.zza;
    }

    @Override // com.google.android.gms.internal.vision.zzlf
    public final zzlc zza(Class cls) {
        zzle.zza(cls);
        zzki zzb2 = this.zza.zzb(cls);
        if (zzb2.zzb()) {
            return zzjb.class.isAssignableFrom(cls) ? zzkq.zza(zzle.zzc(), zzir.zza(), zzb2.zzc()) : zzkq.zza(zzle.zza(), zzir.zzb(), zzb2.zzc());
        }
        if (!zzjb.class.isAssignableFrom(cls)) {
            boolean zza = zza(zzb2);
            zzks zza2 = zzku.zza();
            zzju zza3 = zzju.zza();
            return zza ? zzko.zza(cls, zzb2, zza2, zza3, zzle.zza(), zzir.zzb(), zzkj.zza()) : zzko.zza(cls, zzb2, zza2, zza3, zzle.zzb(), (zziq) null, zzkj.zza());
        }
        boolean zza4 = zza(zzb2);
        zzks zzb3 = zzku.zzb();
        zzju zzb4 = zzju.zzb();
        zzlu zzc = zzle.zzc();
        return zza4 ? zzko.zza(cls, zzb2, zzb3, zzb4, zzc, zzir.zza(), zzkj.zzb()) : zzko.zza(cls, zzb2, zzb3, zzb4, zzc, (zziq) null, zzkj.zzb());
    }
}
