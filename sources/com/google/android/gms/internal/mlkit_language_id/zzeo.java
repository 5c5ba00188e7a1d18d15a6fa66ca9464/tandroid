package com.google.android.gms.internal.mlkit_language_id;

import j$.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
/* loaded from: classes.dex */
public abstract class zzeo extends zzde {
    private static Map<Object, zzeo> zzd = new ConcurrentHashMap();
    protected zzhg zzb = zzhg.zza();
    private int zzc = -1;

    /* loaded from: classes.dex */
    protected static class zza extends zzdj {
        private final zzeo zza;

        public zza(zzeo zzeoVar) {
            this.zza = zzeoVar;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class zzb extends zzdh {
        protected zzeo zza;
        protected boolean zzb = false;
        private final zzeo zzc;

        /* JADX INFO: Access modifiers changed from: protected */
        public zzb(zzeo zzeoVar) {
            this.zzc = zzeoVar;
            this.zza = (zzeo) zzeoVar.zza(zze.zzd, (Object) null, (Object) null);
        }

        private static void zza(zzeo zzeoVar, zzeo zzeoVar2) {
            zzgk.zza().zza(zzeoVar).zzb(zzeoVar, zzeoVar2);
        }

        public /* synthetic */ Object clone() {
            zzb zzbVar = (zzb) this.zzc.zza(zze.zze, (Object) null, (Object) null);
            zzbVar.zza((zzeo) zzf());
            return zzbVar;
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzdh
        public final zzb zza(zzeo zzeoVar) {
            if (this.zzb) {
                zzc();
                this.zzb = false;
            }
            zza(this.zza, zzeoVar);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void zzc() {
            zzeo zzeoVar = (zzeo) this.zza.zza(zze.zzd, (Object) null, (Object) null);
            zza(zzeoVar, this.zza);
            this.zza = zzeoVar;
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzfy
        /* renamed from: zzd */
        public zzeo zzf() {
            if (this.zzb) {
                return this.zza;
            }
            zzeo zzeoVar = this.zza;
            zzgk.zza().zza(zzeoVar).zzb(zzeoVar);
            this.zzb = true;
            return this.zza;
        }

        /* renamed from: zze */
        public final zzeo zzg() {
            zzeo zzeoVar = (zzeo) zzf();
            if (zzeoVar.zzi()) {
                return zzeoVar;
            }
            throw new zzhe(zzeoVar);
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzgb
        public final /* synthetic */ zzfz zzn() {
            return this.zzc;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class zzc extends zzeo implements zzgb {
        protected zzej zzc = zzej.zza();
    }

    /* loaded from: classes.dex */
    public static abstract class zzd extends zzb implements zzgb {
        /* JADX INFO: Access modifiers changed from: protected */
        public zzd(zzc zzcVar) {
            super(zzcVar);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.mlkit_language_id.zzeo.zzb
        public void zzc() {
            super.zzc();
            zzeo zzeoVar = this.zza;
            ((zzc) zzeoVar).zzc = (zzej) ((zzc) zzeoVar).zzc.clone();
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzeo.zzb
        public /* synthetic */ zzeo zzd() {
            return (zzc) zzf();
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzeo.zzb, com.google.android.gms.internal.mlkit_language_id.zzfy
        public /* synthetic */ zzfz zzf() {
            zzeo zzf;
            if (this.zzb) {
                zzf = this.zza;
            } else {
                ((zzc) this.zza).zzc.zzb();
                zzf = super.zzf();
            }
            return (zzc) zzf;
        }
    }

    /* loaded from: classes.dex */
    public enum zze {
        public static final int zza = 1;
        public static final int zzb = 2;
        public static final int zzc = 3;
        public static final int zzd = 4;
        public static final int zze = 5;
        public static final int zzf = 6;
        public static final int zzg = 7;
        private static final /* synthetic */ int[] zzh = {1, 2, 3, 4, 5, 6, 7};

        public static int[] zza() {
            return (int[]) zzh.clone();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzeo zza(Class cls) {
        zzeo zzeoVar = zzd.get(cls);
        if (zzeoVar == null) {
            try {
                Class.forName(cls.getName(), true, cls.getClassLoader());
                zzeoVar = zzd.get(cls);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Class initialization cannot fail.", e);
            }
        }
        if (zzeoVar == null) {
            zzeoVar = (zzeo) ((zzeo) zzhn.zza(cls)).zza(zze.zzf, (Object) null, (Object) null);
            if (zzeoVar == null) {
                throw new IllegalStateException();
            }
            zzd.put(cls, zzeoVar);
        }
        return zzeoVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static zzew zza(zzew zzewVar) {
        int size = zzewVar.size();
        return zzewVar.zzb(size == 0 ? 10 : size << 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Object zza(zzfz zzfzVar, String str, Object[] objArr) {
        return new zzgm(zzfzVar, str, objArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object zza(Method method, Object obj, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Couldn't use Java reflection to implement protocol message reflection.", e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new RuntimeException("Unexpected exception thrown by generated accessor method.", cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void zza(Class cls, zzeo zzeoVar) {
        zzd.put(cls, zzeoVar);
    }

    protected static final boolean zza(zzeo zzeoVar, boolean z) {
        byte byteValue = ((Byte) zzeoVar.zza(zze.zza, (Object) null, (Object) null)).byteValue();
        if (byteValue == 1) {
            return true;
        }
        if (byteValue == 0) {
            return false;
        }
        boolean zzc2 = zzgk.zza().zza(zzeoVar).zzc(zzeoVar);
        if (z) {
            zzeoVar.zza(zze.zzb, zzc2 ? zzeoVar : null, (Object) null);
        }
        return zzc2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.gms.internal.mlkit_language_id.zzeu, com.google.android.gms.internal.mlkit_language_id.zzer] */
    public static zzeu zzk() {
        return zzer.zzd();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static zzew zzl() {
        return zzgn.zzd();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            return zzgk.zza().zza(this).zza(this, (zzeo) obj);
        }
        return false;
    }

    public int hashCode() {
        int i = this.zza;
        if (i != 0) {
            return i;
        }
        int zza2 = zzgk.zza().zza(this).zza(this);
        this.zza = zza2;
        return zza2;
    }

    public String toString() {
        return zzga.zza(this, super.toString());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final zzb zza(zzeo zzeoVar) {
        return zzh().zza(zzeoVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract Object zza(int i, Object obj, Object obj2);

    @Override // com.google.android.gms.internal.mlkit_language_id.zzde
    final void zza(int i) {
        this.zzc = i;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzfz
    public final void zza(zzea zzeaVar) {
        zzgk.zza().zza(this).zza((Object) this, (zzib) zzed.zza(zzeaVar));
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzde
    final int zzg() {
        return this.zzc;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final zzb zzh() {
        return (zzb) zza(zze.zze, (Object) null, (Object) null);
    }

    public final boolean zzi() {
        return zza(this, true);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzfz
    public final int zzj() {
        if (this.zzc == -1) {
            this.zzc = zzgk.zza().zza(this).zzd(this);
        }
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzfz
    public final /* synthetic */ zzfy zzm() {
        zzb zzbVar = (zzb) zza(zze.zze, (Object) null, (Object) null);
        zzbVar.zza(this);
        return zzbVar;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgb
    public final /* synthetic */ zzfz zzn() {
        return (zzeo) zza(zze.zzf, (Object) null, (Object) null);
    }
}
