package com.google.android.gms.internal.clearcut;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.UserManager;
import android.util.Log;
import androidx.core.content.PermissionChecker;

/* loaded from: classes.dex */
public abstract class zzae {
    private static final Object zzdn = new Object();
    private static boolean zzdo = false;
    private static volatile Boolean zzdp;
    private static volatile Boolean zzdq;
    private static Context zzh;
    private final zzao zzdr;
    final String zzds;
    private final String zzdt;
    private final Object zzdu;
    private Object zzdv;
    private volatile zzab zzdw;
    private volatile SharedPreferences zzdx;

    private zzae(zzao zzaoVar, String str, Object obj) {
        this.zzdv = null;
        this.zzdw = null;
        this.zzdx = null;
        if (zzaoVar.zzef == null && zzaoVar.zzeg == null) {
            throw new IllegalArgumentException("Must pass a valid SharedPreferences file name or ContentProvider URI");
        }
        if (zzaoVar.zzef != null && zzaoVar.zzeg != null) {
            throw new IllegalArgumentException("Must pass one of SharedPreferences file name or ContentProvider URI");
        }
        this.zzdr = zzaoVar;
        String valueOf = String.valueOf(zzaoVar.zzeh);
        String valueOf2 = String.valueOf(str);
        this.zzdt = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
        String valueOf3 = String.valueOf(zzaoVar.zzei);
        String valueOf4 = String.valueOf(str);
        this.zzds = valueOf4.length() != 0 ? valueOf3.concat(valueOf4) : new String(valueOf3);
        this.zzdu = obj;
    }

    /* synthetic */ zzae(zzao zzaoVar, String str, Object obj, zzai zzaiVar) {
        this(zzaoVar, str, obj);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0022 A[Catch: all -> 0x0014, TryCatch #0 {all -> 0x0014, blocks: (B:6:0x0007, B:8:0x000d, B:11:0x001e, B:13:0x0022, B:14:0x0025, B:15:0x0027, B:20:0x0016), top: B:5:0x0007 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void maybeInit(Context context) {
        boolean isDeviceProtectedStorage;
        if (zzh == null) {
            synchronized (zzdn) {
                try {
                    if (Build.VERSION.SDK_INT >= 24) {
                        isDeviceProtectedStorage = context.isDeviceProtectedStorage();
                        if (isDeviceProtectedStorage) {
                            if (zzh != context) {
                                zzdp = null;
                            }
                            zzh = context;
                        }
                    }
                    Context applicationContext = context.getApplicationContext();
                    if (applicationContext != null) {
                        context = applicationContext;
                    }
                    if (zzh != context) {
                    }
                    zzh = context;
                } catch (Throwable th) {
                    throw th;
                }
            }
            zzdo = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static zzae zza(zzao zzaoVar, String str, Object obj, zzan zzanVar) {
        return new zzal(zzaoVar, str, obj, zzanVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static zzae zza(zzao zzaoVar, String str, String str2) {
        return new zzak(zzaoVar, str, str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static zzae zza(zzao zzaoVar, String str, boolean z) {
        return new zzaj(zzaoVar, str, Boolean.valueOf(z));
    }

    private static Object zza(zzam zzamVar) {
        try {
            return zzamVar.zzp();
        } catch (SecurityException unused) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return zzamVar.zzp();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    static boolean zza(final String str, boolean z) {
        final boolean z2 = false;
        if (zzn()) {
            return ((Boolean) zza(new zzam(str, z2) { // from class: com.google.android.gms.internal.clearcut.zzah
                private final String zzea;
                private final boolean zzeb = false;

                {
                    this.zzea = str;
                }

                @Override // com.google.android.gms.internal.clearcut.zzam
                public final Object zzp() {
                    Boolean valueOf;
                    valueOf = Boolean.valueOf(zzy.zza(zzae.zzh.getContentResolver(), this.zzea, this.zzeb));
                    return valueOf;
                }
            })).booleanValue();
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x007c A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x007d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final Object zzl() {
        boolean z;
        boolean isDeviceProtectedStorage;
        Object systemService;
        boolean isUserUnlocked;
        if (zza("gms:phenotype:phenotype_flag:debug_bypass_phenotype", false)) {
            String valueOf = String.valueOf(this.zzds);
            Log.w("PhenotypeFlag", valueOf.length() != 0 ? "Bypass reading Phenotype values for flag: ".concat(valueOf) : new String("Bypass reading Phenotype values for flag: "));
        } else if (this.zzdr.zzeg != null) {
            if (this.zzdw == null) {
                this.zzdw = zzab.zza(zzh.getContentResolver(), this.zzdr.zzeg);
            }
            final zzab zzabVar = this.zzdw;
            String str = (String) zza(new zzam(this, zzabVar) { // from class: com.google.android.gms.internal.clearcut.zzaf
                private final zzae zzdy;
                private final zzab zzdz;

                {
                    this.zzdy = this;
                    this.zzdz = zzabVar;
                }

                @Override // com.google.android.gms.internal.clearcut.zzam
                public final Object zzp() {
                    return (String) this.zzdz.zzg().get(this.zzdy.zzds);
                }
            });
            if (str != null) {
                return zzb(str);
            }
        } else if (this.zzdr.zzef != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                isDeviceProtectedStorage = zzh.isDeviceProtectedStorage();
                if (!isDeviceProtectedStorage) {
                    if (zzdq == null || !zzdq.booleanValue()) {
                        systemService = zzh.getSystemService((Class<Object>) UserManager.class);
                        isUserUnlocked = ((UserManager) systemService).isUserUnlocked();
                        zzdq = Boolean.valueOf(isUserUnlocked);
                    }
                    z = zzdq.booleanValue();
                    if (z) {
                        return null;
                    }
                    if (this.zzdx == null) {
                        this.zzdx = zzh.getSharedPreferences(this.zzdr.zzef, 0);
                    }
                    SharedPreferences sharedPreferences = this.zzdx;
                    if (sharedPreferences.contains(this.zzds)) {
                        return zza(sharedPreferences);
                    }
                }
            }
            z = true;
            if (z) {
            }
        }
        return null;
    }

    private final Object zzm() {
        String str;
        if (this.zzdr.zzej || !zzn() || (str = (String) zza(new zzam(this) { // from class: com.google.android.gms.internal.clearcut.zzag
            private final zzae zzdy;

            {
                this.zzdy = this;
            }

            @Override // com.google.android.gms.internal.clearcut.zzam
            public final Object zzp() {
                return this.zzdy.zzo();
            }
        })) == null) {
            return null;
        }
        return zzb(str);
    }

    private static boolean zzn() {
        if (zzdp == null) {
            Context context = zzh;
            if (context == null) {
                return false;
            }
            zzdp = Boolean.valueOf(PermissionChecker.checkCallingOrSelfPermission(context, "com.google.android.providers.gsf.permission.READ_GSERVICES") == 0);
        }
        return zzdp.booleanValue();
    }

    public final Object get() {
        if (zzh == null) {
            throw new IllegalStateException("Must call PhenotypeFlag.init() first");
        }
        if (this.zzdr.zzek) {
            Object zzm = zzm();
            if (zzm != null) {
                return zzm;
            }
            Object zzl = zzl();
            if (zzl != null) {
                return zzl;
            }
        } else {
            Object zzl2 = zzl();
            if (zzl2 != null) {
                return zzl2;
            }
            Object zzm2 = zzm();
            if (zzm2 != null) {
                return zzm2;
            }
        }
        return this.zzdu;
    }

    protected abstract Object zza(SharedPreferences sharedPreferences);

    protected abstract Object zzb(String str);

    final /* synthetic */ String zzo() {
        return zzy.zza(zzh.getContentResolver(), this.zzdt, (String) null);
    }
}
