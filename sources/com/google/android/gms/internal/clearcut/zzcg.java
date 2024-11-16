package com.google.android.gms.internal.clearcut;

import j$.util.concurrent.ConcurrentHashMap;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class zzcg extends zzas {
    private static Map<Object, zzcg> zzjr = new ConcurrentHashMap();
    protected zzey zzjp = zzey.zzea();
    private int zzjq = -1;

    public static abstract class zza extends zzat {
        private final zzcg zzjs;
        protected zzcg zzjt;
        protected boolean zzju = false;

        protected zza(zzcg zzcgVar) {
            this.zzjs = zzcgVar;
            this.zzjt = (zzcg) zzcgVar.zza(zzg.zzkg, (Object) null, (Object) null);
        }

        private static void zza(zzcg zzcgVar, zzcg zzcgVar2) {
            zzea.zzcm().zzp(zzcgVar).zzc(zzcgVar, zzcgVar2);
        }

        public /* synthetic */ Object clone() {
            zza zzaVar = (zza) this.zzjs.zza(zzg.zzkh, (Object) null, (Object) null);
            zzaVar.zza((zzcg) zzbi());
            return zzaVar;
        }

        @Override // com.google.android.gms.internal.clearcut.zzat
        public final zza zza(zzcg zzcgVar) {
            zzbf();
            zza(this.zzjt, zzcgVar);
            return this;
        }

        @Override // com.google.android.gms.internal.clearcut.zzdq
        public final /* synthetic */ zzdo zzbe() {
            return this.zzjs;
        }

        protected void zzbf() {
            if (this.zzju) {
                zzcg zzcgVar = (zzcg) this.zzjt.zza(zzg.zzkg, (Object) null, (Object) null);
                zza(zzcgVar, this.zzjt);
                this.zzjt = zzcgVar;
                this.zzju = false;
            }
        }

        @Override // com.google.android.gms.internal.clearcut.zzdp
        /* renamed from: zzbg, reason: merged with bridge method [inline-methods] */
        public zzcg zzbi() {
            if (this.zzju) {
                return this.zzjt;
            }
            zzcg zzcgVar = this.zzjt;
            zzea.zzcm().zzp(zzcgVar).zzc(zzcgVar);
            this.zzju = true;
            return this.zzjt;
        }

        public final zzcg zzbh() {
            zzcg zzcgVar = (zzcg) zzbi();
            byte byteValue = ((Byte) zzcgVar.zza(zzg.zzkd, (Object) null, (Object) null)).byteValue();
            boolean z = true;
            if (byteValue != 1) {
                if (byteValue == 0) {
                    z = false;
                } else {
                    z = zzea.zzcm().zzp(zzcgVar).zzo(zzcgVar);
                    zzcgVar.zza(zzg.zzke, z ? zzcgVar : null, (Object) null);
                }
            }
            if (z) {
                return zzcgVar;
            }
            throw new zzew(zzcgVar);
        }
    }

    public static class zzb extends zzau {
        private zzcg zzjs;

        public zzb(zzcg zzcgVar) {
            this.zzjs = zzcgVar;
        }
    }

    public enum zzg {
        public static final int zzkd = 1;
        public static final int zzke = 2;
        public static final int zzkf = 3;
        public static final int zzkg = 4;
        public static final int zzkh = 5;
        public static final int zzki = 6;
        public static final int zzkj = 7;
        private static final /* synthetic */ int[] zzkk = {1, 2, 3, 4, 5, 6, 7};
        public static final int zzkl = 1;
        public static final int zzkm = 2;
        private static final /* synthetic */ int[] zzkn = {1, 2};
        public static final int zzko = 1;
        public static final int zzkp = 2;
        private static final /* synthetic */ int[] zzkq = {1, 2};

        public static int[] values$50KLMJ33DTMIUPRFDTJMOP9FE1P6UT3FC9QMCBQ7CLN6ASJ1EHIM8JB5EDPM2PR59HKN8P949LIN8Q3FCHA6UIBEEPNMMP9R0() {
            return (int[]) zzkk.clone();
        }
    }

    private static zzcg zza(zzcg zzcgVar, byte[] bArr) {
        zzcg zzcgVar2 = (zzcg) zzcgVar.zza(zzg.zzkg, (Object) null, (Object) null);
        try {
            zzea.zzcm().zzp(zzcgVar2).zza(zzcgVar2, bArr, 0, bArr.length, new zzay());
            zzea.zzcm().zzp(zzcgVar2).zzc(zzcgVar2);
            if (zzcgVar2.zzex == 0) {
                return zzcgVar2;
            }
            throw new RuntimeException();
        } catch (IOException e) {
            if (e.getCause() instanceof zzco) {
                throw ((zzco) e.getCause());
            }
            throw new zzco(e.getMessage()).zzg(zzcgVar2);
        } catch (IndexOutOfBoundsException unused) {
            throw zzco.zzbl().zzg(zzcgVar2);
        }
    }

    protected static Object zza(zzdo zzdoVar, String str, Object[] objArr) {
        return new zzec(zzdoVar, str, objArr);
    }

    static Object zza(Method method, Object obj, Object... objArr) {
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

    protected static void zza(Class cls, zzcg zzcgVar) {
        zzjr.put(cls, zzcgVar);
    }

    protected static zzcg zzb(zzcg zzcgVar, byte[] bArr) {
        zzcg zza2 = zza(zzcgVar, bArr);
        if (zza2 != null) {
            byte byteValue = ((Byte) zza2.zza(zzg.zzkd, (Object) null, (Object) null)).byteValue();
            boolean z = true;
            if (byteValue != 1) {
                if (byteValue == 0) {
                    z = false;
                } else {
                    z = zzea.zzcm().zzp(zza2).zzo(zza2);
                    zza2.zza(zzg.zzke, z ? zza2 : null, (Object) null);
                }
            }
            if (!z) {
                throw new zzco(new zzew(zza2).getMessage()).zzg(zza2);
            }
        }
        return zza2;
    }

    protected static zzcn zzbb() {
        return zzeb.zzcn();
    }

    static zzcg zzc(Class cls) {
        zzcg zzcgVar = zzjr.get(cls);
        if (zzcgVar == null) {
            try {
                Class.forName(cls.getName(), true, cls.getClassLoader());
                zzcgVar = zzjr.get(cls);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Class initialization cannot fail.", e);
            }
        }
        if (zzcgVar != null) {
            return zzcgVar;
        }
        String name = cls.getName();
        throw new IllegalStateException(name.length() != 0 ? "Unable to get default instance for: ".concat(name) : new String("Unable to get default instance for: "));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (((zzcg) zza(zzg.zzki, (Object) null, (Object) null)).getClass().isInstance(obj)) {
            return zzea.zzcm().zzp(this).equals(this, (zzcg) obj);
        }
        return false;
    }

    public int hashCode() {
        int i = this.zzex;
        if (i != 0) {
            return i;
        }
        int hashCode = zzea.zzcm().zzp(this).hashCode(this);
        this.zzex = hashCode;
        return hashCode;
    }

    public String toString() {
        return zzdr.zza(this, super.toString());
    }

    protected abstract Object zza(int i, Object obj, Object obj2);

    @Override // com.google.android.gms.internal.clearcut.zzdo
    public final int zzas() {
        if (this.zzjq == -1) {
            this.zzjq = zzea.zzcm().zzp(this).zzm(this);
        }
        return this.zzjq;
    }

    @Override // com.google.android.gms.internal.clearcut.zzdo
    public final void zzb(zzbn zzbnVar) {
        zzea.zzcm().zze(getClass()).zza(this, zzbp.zza(zzbnVar));
    }

    @Override // com.google.android.gms.internal.clearcut.zzdo
    public final /* synthetic */ zzdp zzbc() {
        zza zzaVar = (zza) zza(zzg.zzkh, (Object) null, (Object) null);
        zzaVar.zza(this);
        return zzaVar;
    }

    @Override // com.google.android.gms.internal.clearcut.zzdo
    public final /* synthetic */ zzdp zzbd() {
        return (zza) zza(zzg.zzkh, (Object) null, (Object) null);
    }

    @Override // com.google.android.gms.internal.clearcut.zzdq
    public final /* synthetic */ zzdo zzbe() {
        return (zzcg) zza(zzg.zzki, (Object) null, (Object) null);
    }

    @Override // com.google.android.gms.internal.clearcut.zzas
    final void zzf(int i) {
        this.zzjq = i;
    }

    @Override // com.google.android.gms.internal.clearcut.zzas
    final int zzs() {
        return this.zzjq;
    }
}
