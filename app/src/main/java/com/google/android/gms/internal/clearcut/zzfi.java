package com.google.android.gms.internal.clearcut;
/* loaded from: classes.dex */
final class zzfi extends IllegalArgumentException {
    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public zzfi(int i, int i2) {
        super(r0.toString());
        StringBuilder sb = new StringBuilder(54);
        sb.append("Unpaired surrogate at index ");
        sb.append(i);
        sb.append(" of ");
        sb.append(i2);
    }
}
