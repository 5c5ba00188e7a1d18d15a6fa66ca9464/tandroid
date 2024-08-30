package com.google.android.gms.internal.base;
/* loaded from: classes.dex */
public abstract class zat {
    private static final zaq zaa;
    private static volatile zaq zab;

    static {
        zas zasVar = new zas(null);
        zaa = zasVar;
        zab = zasVar;
    }

    public static zaq zaa() {
        return zab;
    }
}
