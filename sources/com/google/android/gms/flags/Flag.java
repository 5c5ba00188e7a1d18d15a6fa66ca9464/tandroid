package com.google.android.gms.flags;

/* loaded from: classes.dex */
public abstract class Flag {
    private final String mKey;
    private final int zze;
    private final Object zzf;

    public static class BooleanFlag extends Flag {
        public BooleanFlag(int i, String str, Boolean bool) {
            super(i, str, bool);
        }
    }

    private Flag(int i, String str, Object obj) {
        this.zze = i;
        this.mKey = str;
        this.zzf = obj;
        Singletons.flagRegistry().zza(this);
    }

    public static BooleanFlag define(int i, String str, Boolean bool) {
        return new BooleanFlag(i, str, bool);
    }
}
