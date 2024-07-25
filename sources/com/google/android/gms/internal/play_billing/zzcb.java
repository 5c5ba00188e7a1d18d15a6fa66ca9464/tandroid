package com.google.android.gms.internal.play_billing;

import com.google.android.gms.internal.play_billing.zzbx;
import com.google.android.gms.internal.play_billing.zzcb;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.telegram.tgnet.ConnectionsManager;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public abstract class zzcb<MessageType extends zzcb<MessageType, BuilderType>, BuilderType extends zzbx<MessageType, BuilderType>> extends zzak<MessageType, BuilderType> {
    private static final Map zzb = new ConcurrentHashMap();
    private int zzd = -1;
    protected zzeh zzc = zzeh.zzc();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzcb zzh(Class cls) {
        Map map = zzb;
        zzcb zzcbVar = (zzcb) map.get(cls);
        if (zzcbVar == null) {
            try {
                Class.forName(cls.getName(), true, cls.getClassLoader());
                zzcbVar = (zzcb) map.get(cls);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Class initialization cannot fail.", e);
            }
        }
        if (zzcbVar == null) {
            zzcbVar = (zzcb) ((zzcb) zzeq.zze(cls)).zzu(6, null, null);
            if (zzcbVar == null) {
                throw new IllegalStateException();
            }
            map.put(cls, zzcbVar);
        }
        return zzcbVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static zzcb zzj(zzcb zzcbVar, byte[] bArr, zzbn zzbnVar) throws zzci {
        zzcb zzw = zzw(zzcbVar, bArr, 0, bArr.length, zzbnVar);
        if (zzw == null || zzw.zzs()) {
            return zzw;
        }
        zzci zza = new zzef(zzw).zza();
        zza.zzf(zzw);
        throw zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object zzl(Method method, Object obj, Object... objArr) {
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
    public static Object zzm(zzdf zzdfVar, String str, Object[] objArr) {
        return new zzdo(zzdfVar, str, objArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void zzp(Class cls, zzcb zzcbVar) {
        zzcbVar.zzo();
        zzb.put(cls, zzcbVar);
    }

    private final int zzv(zzdp zzdpVar) {
        return zzdn.zza().zzb(getClass()).zza(this);
    }

    private static zzcb zzw(zzcb zzcbVar, byte[] bArr, int i, int i2, zzbn zzbnVar) throws zzci {
        zzcb zzi = zzcbVar.zzi();
        try {
            zzdp zzb2 = zzdn.zza().zzb(zzi.getClass());
            zzb2.zzh(zzi, bArr, 0, i2, new zzan(zzbnVar));
            zzb2.zzf(zzi);
            return zzi;
        } catch (zzci e) {
            e.zzf(zzi);
            throw e;
        } catch (zzef e2) {
            zzci zza = e2.zza();
            zza.zzf(zzi);
            throw zza;
        } catch (IOException e3) {
            if (e3.getCause() instanceof zzci) {
                throw ((zzci) e3.getCause());
            }
            zzci zzciVar = new zzci(e3);
            zzciVar.zzf(zzi);
            throw zzciVar;
        } catch (IndexOutOfBoundsException unused) {
            zzci zzg = zzci.zzg();
            zzg.zzf(zzi);
            throw zzg;
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            return zzdn.zza().zzb(getClass()).zzj(this, (zzcb) obj);
        }
        return false;
    }

    public final int hashCode() {
        if (zzt()) {
            return zzd();
        }
        int i = this.zza;
        if (i == 0) {
            int zzd = zzd();
            this.zza = zzd;
            return zzd;
        }
        return i;
    }

    public final String toString() {
        return zzdh.zza(this, super.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.play_billing.zzak
    public final int zza(zzdp zzdpVar) {
        if (zzt()) {
            int zza = zzdpVar.zza(this);
            if (zza >= 0) {
                return zza;
            }
            throw new IllegalStateException("serialized size must be non-negative, was " + zza);
        }
        int i = this.zzd & ConnectionsManager.DEFAULT_DATACENTER_ID;
        if (i == Integer.MAX_VALUE) {
            int zza2 = zzdpVar.zza(this);
            if (zza2 >= 0) {
                this.zzd = (this.zzd & Integer.MIN_VALUE) | zza2;
                return zza2;
            }
            throw new IllegalStateException("serialized size must be non-negative, was " + zza2);
        }
        return i;
    }

    final int zzd() {
        return zzdn.zza().zzb(getClass()).zzb(this);
    }

    @Override // com.google.android.gms.internal.play_billing.zzdg
    public final /* synthetic */ zzdf zzf() {
        return (zzcb) zzu(6, null, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final zzbx zzg() {
        return (zzbx) zzu(5, null, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzcb zzi() {
        return (zzcb) zzu(4, null, null);
    }

    @Override // com.google.android.gms.internal.play_billing.zzdf
    public final /* synthetic */ zzde zzk() {
        return (zzbx) zzu(5, null, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void zzn() {
        zzdn.zza().zzb(getClass()).zzf(this);
        zzo();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzo() {
        this.zzd &= ConnectionsManager.DEFAULT_DATACENTER_ID;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzq(int i) {
        this.zzd = (this.zzd & Integer.MIN_VALUE) | ConnectionsManager.DEFAULT_DATACENTER_ID;
    }

    @Override // com.google.android.gms.internal.play_billing.zzdf
    public final void zzr(zzbi zzbiVar) throws IOException {
        zzdn.zza().zzb(getClass()).zzi(this, zzbj.zza(zzbiVar));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zzt() {
        return (this.zzd & Integer.MIN_VALUE) != 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract Object zzu(int i, Object obj, Object obj2);

    public final boolean zzs() {
        byte byteValue = ((Byte) zzu(1, null, null)).byteValue();
        if (byteValue == 1) {
            return true;
        }
        if (byteValue == 0) {
            return false;
        }
        boolean zzk = zzdn.zza().zzb(getClass()).zzk(this);
        zzu(2, true != zzk ? null : this, null);
        return zzk;
    }

    @Override // com.google.android.gms.internal.play_billing.zzdf
    public final int zze() {
        int i;
        if (zzt()) {
            i = zzv(null);
            if (i < 0) {
                throw new IllegalStateException("serialized size must be non-negative, was " + i);
            }
        } else {
            i = this.zzd & ConnectionsManager.DEFAULT_DATACENTER_ID;
            if (i == Integer.MAX_VALUE) {
                i = zzv(null);
                if (i >= 0) {
                    this.zzd = (this.zzd & Integer.MIN_VALUE) | i;
                } else {
                    throw new IllegalStateException("serialized size must be non-negative, was " + i);
                }
            }
        }
        return i;
    }
}
