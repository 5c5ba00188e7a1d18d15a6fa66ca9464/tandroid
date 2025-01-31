package com.google.android.recaptcha.internal;

import com.google.android.recaptcha.internal.zzin;
import com.google.android.recaptcha.internal.zzit;
import j$.util.concurrent.ConcurrentHashMap;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.telegram.messenger.LiteMode;
import org.telegram.tgnet.ConnectionsManager;

/* loaded from: classes.dex */
public abstract class zzit<MessageType extends zzit<MessageType, BuilderType>, BuilderType extends zzin<MessageType, BuilderType>> extends zzgf<MessageType, BuilderType> {
    private static final Map zzb = new ConcurrentHashMap();
    private int zzd = -1;
    protected zzlm zzc = zzlm.zzc();

    protected static Object zzA(zzke zzkeVar, String str, Object[] objArr) {
        return new zzkp(zzkeVar, str, objArr);
    }

    protected static void zzD(Class cls, zzit zzitVar) {
        zzitVar.zzC();
        zzb.put(cls, zzitVar);
    }

    protected static final boolean zzF(zzit zzitVar, boolean z) {
        byte byteValue = ((Byte) zzitVar.zzh(1, null, null)).byteValue();
        if (byteValue == 1) {
            return true;
        }
        if (byteValue == 0) {
            return false;
        }
        boolean zzl = zzkn.zza().zzb(zzitVar.getClass()).zzl(zzitVar);
        if (z) {
            zzitVar.zzh(2, true != zzl ? null : zzitVar, null);
        }
        return zzl;
    }

    private final int zzf(zzkr zzkrVar) {
        return zzkn.zza().zzb(getClass()).zza(this);
    }

    private static zzit zzg(zzit zzitVar) {
        if (zzitVar == null || zzitVar.zzo()) {
            return zzitVar;
        }
        zzje zza = new zzlk(zzitVar).zza();
        zza.zzh(zzitVar);
        throw zza;
    }

    private static zzit zzi(zzit zzitVar, byte[] bArr, int i, int i2, zzie zzieVar) {
        zzit zzs = zzitVar.zzs();
        try {
            zzkr zzb2 = zzkn.zza().zzb(zzs.getClass());
            zzb2.zzi(zzs, bArr, 0, i2, new zzgj(zzieVar));
            zzb2.zzf(zzs);
            return zzs;
        } catch (zzje e) {
            e = e;
            if (e.zzl()) {
                e = new zzje(e);
            }
            e.zzh(zzs);
            throw e;
        } catch (zzlk e2) {
            zzje zza = e2.zza();
            zza.zzh(zzs);
            throw zza;
        } catch (IOException e3) {
            if (e3.getCause() instanceof zzje) {
                throw ((zzje) e3.getCause());
            }
            zzje zzjeVar = new zzje(e3);
            zzjeVar.zzh(zzs);
            throw zzjeVar;
        } catch (IndexOutOfBoundsException unused) {
            zzje zzj = zzje.zzj();
            zzj.zzh(zzs);
            throw zzj;
        }
    }

    public static zzir zzq(zzke zzkeVar, Object obj, zzke zzkeVar2, zziw zziwVar, int i, zzmb zzmbVar, Class cls) {
        return new zzir(zzkeVar, "", null, new zziq(null, i, zzmbVar, false, false), cls);
    }

    static zzit zzr(Class cls) {
        Map map = zzb;
        zzit zzitVar = (zzit) map.get(cls);
        if (zzitVar == null) {
            try {
                Class.forName(cls.getName(), true, cls.getClassLoader());
                zzitVar = (zzit) map.get(cls);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Class initialization cannot fail.", e);
            }
        }
        if (zzitVar == null) {
            zzitVar = (zzit) ((zzit) zzlv.zze(cls)).zzh(6, null, null);
            if (zzitVar == null) {
                throw new IllegalStateException();
            }
            map.put(cls, zzitVar);
        }
        return zzitVar;
    }

    protected static zzit zzt(zzit zzitVar, InputStream inputStream) {
        zzhc zzhaVar;
        int i = zzhc.zzd;
        if (inputStream == null) {
            byte[] bArr = zzjc.zzd;
            int length = bArr.length;
            zzhaVar = zzhc.zzH(bArr, 0, 0, false);
        } else {
            zzhaVar = new zzha(inputStream, LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM, null);
        }
        zzie zzieVar = zzie.zza;
        zzit zzs = zzitVar.zzs();
        try {
            zzkr zzb2 = zzkn.zza().zzb(zzs.getClass());
            zzb2.zzh(zzs, zzhd.zzq(zzhaVar), zzieVar);
            zzb2.zzf(zzs);
            zzg(zzs);
            return zzs;
        } catch (zzje e) {
            e = e;
            if (e.zzl()) {
                e = new zzje(e);
            }
            e.zzh(zzs);
            throw e;
        } catch (zzlk e2) {
            zzje zza = e2.zza();
            zza.zzh(zzs);
            throw zza;
        } catch (IOException e3) {
            if (e3.getCause() instanceof zzje) {
                throw ((zzje) e3.getCause());
            }
            zzje zzjeVar = new zzje(e3);
            zzjeVar.zzh(zzs);
            throw zzjeVar;
        } catch (RuntimeException e4) {
            if (e4.getCause() instanceof zzje) {
                throw ((zzje) e4.getCause());
            }
            throw e4;
        }
    }

    protected static zzit zzu(zzit zzitVar, byte[] bArr) {
        zzit zzi = zzi(zzitVar, bArr, 0, bArr.length, zzie.zza);
        zzg(zzi);
        return zzi;
    }

    protected static zziy zzv() {
        return zziu.zzf();
    }

    protected static zzja zzw() {
        return zzjt.zzf();
    }

    protected static zzjb zzx() {
        return zzko.zze();
    }

    protected static zzjb zzy(zzjb zzjbVar) {
        int size = zzjbVar.size();
        return zzjbVar.zzd(size == 0 ? 10 : size + size);
    }

    static Object zzz(Method method, Object obj, Object... objArr) {
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

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return zzkn.zza().zzb(getClass()).zzk(this, (zzit) obj);
    }

    public final int hashCode() {
        if (zzG()) {
            return zzm();
        }
        int i = this.zza;
        if (i != 0) {
            return i;
        }
        int zzm = zzm();
        this.zza = zzm;
        return zzm;
    }

    public final String toString() {
        return zzkg.zza(this, super.toString());
    }

    protected final void zzB() {
        zzkn.zza().zzb(getClass()).zzf(this);
        zzC();
    }

    final void zzC() {
        this.zzd &= ConnectionsManager.DEFAULT_DATACENTER_ID;
    }

    final void zzE(int i) {
        this.zzd = (this.zzd & Integer.MIN_VALUE) | ConnectionsManager.DEFAULT_DATACENTER_ID;
    }

    final boolean zzG() {
        return (this.zzd & Integer.MIN_VALUE) != 0;
    }

    @Override // com.google.android.recaptcha.internal.zzke
    public final /* synthetic */ zzkd zzW() {
        return (zzin) zzh(5, null, null);
    }

    @Override // com.google.android.recaptcha.internal.zzke
    public final /* synthetic */ zzkd zzX() {
        zzin zzinVar = (zzin) zzh(5, null, null);
        zzinVar.zzg(this);
        return zzinVar;
    }

    @Override // com.google.android.recaptcha.internal.zzkf
    public final /* synthetic */ zzke zzY() {
        return (zzit) zzh(6, null, null);
    }

    @Override // com.google.android.recaptcha.internal.zzgf
    final int zza(zzkr zzkrVar) {
        if (zzG()) {
            int zza = zzkrVar.zza(this);
            if (zza >= 0) {
                return zza;
            }
            throw new IllegalStateException("serialized size must be non-negative, was " + zza);
        }
        int i = this.zzd & ConnectionsManager.DEFAULT_DATACENTER_ID;
        if (i != Integer.MAX_VALUE) {
            return i;
        }
        int zza2 = zzkrVar.zza(this);
        if (zza2 >= 0) {
            this.zzd = (this.zzd & Integer.MIN_VALUE) | zza2;
            return zza2;
        }
        throw new IllegalStateException("serialized size must be non-negative, was " + zza2);
    }

    @Override // com.google.android.recaptcha.internal.zzke
    public final void zze(zzhh zzhhVar) {
        zzkn.zza().zzb(getClass()).zzj(this, zzhi.zza(zzhhVar));
    }

    protected abstract Object zzh(int i, Object obj, Object obj2);

    final int zzm() {
        return zzkn.zza().zzb(getClass()).zzb(this);
    }

    @Override // com.google.android.recaptcha.internal.zzke
    public final int zzn() {
        int i;
        if (zzG()) {
            i = zzf(null);
            if (i < 0) {
                throw new IllegalStateException("serialized size must be non-negative, was " + i);
            }
        } else {
            i = this.zzd & ConnectionsManager.DEFAULT_DATACENTER_ID;
            if (i == Integer.MAX_VALUE) {
                i = zzf(null);
                if (i < 0) {
                    throw new IllegalStateException("serialized size must be non-negative, was " + i);
                }
                this.zzd = (this.zzd & Integer.MIN_VALUE) | i;
            }
        }
        return i;
    }

    @Override // com.google.android.recaptcha.internal.zzkf
    public final boolean zzo() {
        return zzF(this, true);
    }

    protected final zzin zzp() {
        return (zzin) zzh(5, null, null);
    }

    final zzit zzs() {
        return (zzit) zzh(4, null, null);
    }
}
