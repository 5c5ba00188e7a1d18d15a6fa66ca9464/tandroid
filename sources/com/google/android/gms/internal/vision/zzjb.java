package com.google.android.gms.internal.vision;

import j$.util.concurrent.ConcurrentHashMap;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
/* loaded from: classes.dex */
public abstract class zzjb extends zzhf {
    private static Map<Object, zzjb> zzd = new ConcurrentHashMap();
    protected zzlx zzb = zzlx.zza();
    private int zzc = -1;

    /* loaded from: classes.dex */
    protected static class zza extends zzhg {
        private final zzjb zza;

        public zza(zzjb zzjbVar) {
            this.zza = zzjbVar;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class zzb extends zzhe {
        protected zzjb zza;
        protected boolean zzb = false;
        private final zzjb zzc;

        /* JADX INFO: Access modifiers changed from: protected */
        public zzb(zzjb zzjbVar) {
            this.zzc = zzjbVar;
            this.zza = (zzjb) zzjbVar.zza(zzg.zzd, (Object) null, (Object) null);
        }

        private static void zza(zzjb zzjbVar, zzjb zzjbVar2) {
            zzky.zza().zza(zzjbVar).zzb(zzjbVar, zzjbVar2);
        }

        private final zzb zzb(byte[] bArr, int i, int i2, zzio zzioVar) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            try {
                zzky.zza().zza(this.zza).zza(this.zza, bArr, 0, i2, new zzhn(zzioVar));
                return this;
            } catch (zzjk e) {
                throw e;
            } catch (IOException e2) {
                throw new RuntimeException("Reading from byte array should not throw IOException.", e2);
            } catch (IndexOutOfBoundsException unused) {
                throw zzjk.zza();
            }
        }

        public /* synthetic */ Object clone() {
            zzb zzbVar = (zzb) this.zzc.zza(zzg.zze, (Object) null, (Object) null);
            zzbVar.zza((zzjb) zze());
            return zzbVar;
        }

        @Override // com.google.android.gms.internal.vision.zzhe
        public final /* synthetic */ zzhe zza(byte[] bArr, int i, int i2, zzio zzioVar) {
            return zzb(bArr, 0, i2, zzioVar);
        }

        @Override // com.google.android.gms.internal.vision.zzhe
        public final zzb zza(zzjb zzjbVar) {
            if (this.zzb) {
                zzb();
                this.zzb = false;
            }
            zza(this.zza, zzjbVar);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void zzb() {
            zzjb zzjbVar = (zzjb) this.zza.zza(zzg.zzd, (Object) null, (Object) null);
            zza(zzjbVar, this.zza);
            this.zza = zzjbVar;
        }

        @Override // com.google.android.gms.internal.vision.zzkn
        /* renamed from: zzc */
        public zzjb zze() {
            if (this.zzb) {
                return this.zza;
            }
            zzjb zzjbVar = this.zza;
            zzky.zza().zza(zzjbVar).zzc(zzjbVar);
            this.zzb = true;
            return this.zza;
        }

        /* renamed from: zzd */
        public final zzjb zzf() {
            zzjb zzjbVar = (zzjb) zze();
            if (zzjbVar.zzk()) {
                return zzjbVar;
            }
            throw new zzlv(zzjbVar);
        }

        @Override // com.google.android.gms.internal.vision.zzkm
        public final /* synthetic */ zzkk zzr() {
            return this.zzc;
        }
    }

    /* loaded from: classes.dex */
    public static class zze extends zzim {
    }

    /* loaded from: classes.dex */
    public enum zzg {
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
    public static zzjb zza(Class cls) {
        zzjb zzjbVar = zzd.get(cls);
        if (zzjbVar == null) {
            try {
                Class.forName(cls.getName(), true, cls.getClassLoader());
                zzjbVar = zzd.get(cls);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Class initialization cannot fail.", e);
            }
        }
        if (zzjbVar == null) {
            zzjbVar = (zzjb) ((zzjb) zzma.zza(cls)).zza(zzg.zzf, (Object) null, (Object) null);
            if (zzjbVar == null) {
                throw new IllegalStateException();
            }
            zzd.put(cls, zzjbVar);
        }
        return zzjbVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static zzjl zza(zzjl zzjlVar) {
        int size = zzjlVar.size();
        return zzjlVar.zza(size == 0 ? 10 : size << 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Object zza(zzkk zzkkVar, String str, Object[] objArr) {
        return new zzla(zzkkVar, str, objArr);
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
    public static void zza(Class cls, zzjb zzjbVar) {
        zzd.put(cls, zzjbVar);
    }

    protected static final boolean zza(zzjb zzjbVar, boolean z) {
        byte byteValue = ((Byte) zzjbVar.zza(zzg.zza, (Object) null, (Object) null)).byteValue();
        if (byteValue == 1) {
            return true;
        }
        if (byteValue == 0) {
            return false;
        }
        boolean zzd2 = zzky.zza().zza(zzjbVar).zzd(zzjbVar);
        if (z) {
            zzjbVar.zza(zzg.zzb, zzd2 ? zzjbVar : null, (Object) null);
        }
        return zzd2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.gms.internal.vision.zzjj, com.google.android.gms.internal.vision.zzjd] */
    public static zzjj zzn() {
        return zzjd.zzd();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static zzjl zzo() {
        return zzlb.zzd();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            return zzky.zza().zza(this).zza(this, (zzjb) obj);
        }
        return false;
    }

    public int hashCode() {
        int i = this.zza;
        if (i != 0) {
            return i;
        }
        int zza2 = zzky.zza().zza(this).zza(this);
        this.zza = zza2;
        return zza2;
    }

    public String toString() {
        return zzkp.zza(this, super.toString());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract Object zza(int i, Object obj, Object obj2);

    @Override // com.google.android.gms.internal.vision.zzkk
    public final void zza(zzii zziiVar) {
        zzky.zza().zza(this).zza((Object) this, (zzmr) zzil.zza(zziiVar));
    }

    @Override // com.google.android.gms.internal.vision.zzhf
    final void zzb(int i) {
        this.zzc = i;
    }

    @Override // com.google.android.gms.internal.vision.zzhf
    final int zzi() {
        return this.zzc;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final zzb zzj() {
        return (zzb) zza(zzg.zze, (Object) null, (Object) null);
    }

    public final boolean zzk() {
        return zza(this, true);
    }

    @Override // com.google.android.gms.internal.vision.zzkk
    public final int zzm() {
        if (this.zzc == -1) {
            this.zzc = zzky.zza().zza(this).zzb(this);
        }
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.vision.zzkk
    public final /* synthetic */ zzkn zzp() {
        zzb zzbVar = (zzb) zza(zzg.zze, (Object) null, (Object) null);
        zzbVar.zza(this);
        return zzbVar;
    }

    @Override // com.google.android.gms.internal.vision.zzkk
    public final /* synthetic */ zzkn zzq() {
        return (zzb) zza(zzg.zze, (Object) null, (Object) null);
    }

    @Override // com.google.android.gms.internal.vision.zzkm
    public final /* synthetic */ zzkk zzr() {
        return (zzjb) zza(zzg.zzf, (Object) null, (Object) null);
    }
}
